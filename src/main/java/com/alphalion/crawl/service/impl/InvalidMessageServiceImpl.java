package com.alphalion.crawl.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alphalion.crawl.application.config.UrlConfig;
import com.alphalion.crawl.application.constant.InvalidMessageConstant;
import com.alphalion.crawl.application.util.CrawlUtil;
import com.alphalion.crawl.application.util.HttpUtil;
import com.alphalion.crawl.application.util.SymbolUtil;
import com.alphalion.crawl.mapper.BusinessDateEntityMapper;
import com.alphalion.crawl.mapper.InvalidMessageEntityMapper;
import com.alphalion.crawl.mapper.ProductSymbolsNetEntityMapper;
import com.alphalion.crawl.mapper.entity.InvalidMessageEntity;
import com.alphalion.crawl.mapper.entity.ProductSymbolsEntity;
import com.alphalion.crawl.mapper.entity.ProductSymbolsNetEntity;
import com.alphalion.crawl.service.IInvalidMessageService;
import com.alphalion.crawl.service.IProductSymbolsService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @author SongBaoYu
 * @date 2018/1/9 上午10:38
 */

@Slf4j
@Service("invalidMessageService")
public class InvalidMessageServiceImpl implements IInvalidMessageService {

    @Autowired
    private InvalidMessageEntityMapper invalidMessageEntityMapper;
    @Autowired
    ProductSymbolsNetEntityMapper productSymbolsNetEntityMapper;
    @Autowired
    private IProductSymbolsService productSymbolsService;
    @Autowired
    private BusinessDateEntityMapper businessDateEntityMapper;
    @Autowired
    private UrlConfig urlConfig;


    @Override
    public void seekProduct(List<String> invalidValues) {
        if (null == invalidValues || invalidValues.isEmpty()) {
            return;
        }

        for (String invalidValue : invalidValues) {
            try {
                CrawlUtil.crawlSymbol(invalidValue);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Set<String> processInvalidProducts() {
        InvalidMessageEntity queryCondition = new InvalidMessageEntity();
        queryCondition.setStatus("E");
        List<InvalidMessageEntity> invalidMessages = invalidMessageEntityMapper.select(queryCondition);

        if (null == invalidMessages || invalidMessages.isEmpty()) {
            return new HashSet<String>(0);
        }

        int size = invalidMessages.size();
        ProductSymbolsNetEntity productSymbolsNetEntity = null;
        List<ProductSymbolsEntity> exitsProductSymbols = null;
        Set<Long> msgIds = new HashSet<>(size);
        Set<String> invalidValues = new HashSet<>(size / 2);



        for (InvalidMessageEntity invalidMessage : invalidMessages) {
            try {
                String invalidValue = invalidMessage.getInvalid_value();
                if (InvalidMessageConstant.Values.ADD.equalsIgnoreCase(invalidValue) || InvalidMessageConstant.Values.CXL.equalsIgnoreCase(invalidValue) || Strings.isNullOrEmpty(invalidValue)) {
                    updInvalidMsgStaById(invalidMessage.getId());
                    log.info("delete invalid_message logically for id={},invalid_value={}",invalidMessage.getId(),invalidValue);
                } else if(SymbolUtil.checkCUSIP(invalidValue)){
                    if (invalidValues.contains(invalidValue)) {
                        continue;
                    } else {
                        invalidValues.add(invalidValue);
                    }

                    boolean exitsOldProduct = false;
                    //备用产品库查询ISIN产品----精准匹配
                    productSymbolsNetEntity = productSymbolsService.selectOneBySymbol(invalidValue);
                    if (null == productSymbolsNetEntity || Strings.isNullOrEmpty(productSymbolsNetEntity.getIsin())) {
                        //爬取ISIN产品信息---广泛搜索
                        productSymbolsNetEntity = CrawlUtil.crawlSymbol(invalidValue);
                    }

                    if (null != productSymbolsNetEntity && !Strings.isNullOrEmpty(productSymbolsNetEntity.getIsin())) {
                        exitsProductSymbols = productSymbolsService.listCusipProductsByIsin(productSymbolsNetEntity.getIsin());

                        //数据库中已存在旧的产品
                        if (!exitsProductSymbols.isEmpty()) {
                            //更新已有产品有效日期
                            long productId = 0L;
                            for (ProductSymbolsEntity exitsProductSymbol : exitsProductSymbols) {
                                productSymbolsService.updateProductSymBusiThruById(exitsProductSymbol.getId());
                                productId = exitsProductSymbol.getProduct_id();
                            }
                            //插入新的产品
                            productSymbolsService.insertCusipProductSymbols(invalidValue, productId);
                            msgIds.add(invalidMessage.getId());
                            exitsOldProduct = true;
                        }
                    }

                    //未爬取到ISIN产品;或爬取到但是数据库中不存在旧产品记录
                    if (!exitsOldProduct) {
                        Long productId = productSymbolsService.getNextProductId();
                        if (null == productId) {
                            continue;
                        }
                        if (null == productSymbolsNetEntity) {
                            productSymbolsNetEntity = new ProductSymbolsNetEntity();
                            productSymbolsNetEntity.setCusip(invalidValue);
                        }
                        productSymbolsNetEntity.setProduct_id(productId);
                        try {
                            productSymbolsService.addProductAndSymbols(productSymbolsNetEntity);
                            msgIds.add(invalidMessage.getId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(invalidValues.isEmpty()){
            log.warn("Crawl process has finished...");
            log.warn("There's no invalid message has been processed automatically...");
            log.warn("The replay process need to start manually...");
        }else{
            log.warn("Crawl process has finished...");
            log.warn("The replay process need to start manually...");
            log.warn("invalidValues={}", JSON.toJSON(invalidValues) +" need to replay...");
            log.warn("pIds={}",msgIds);
        }

        //do replay
//        if (!msgIds.isEmpty()) {
//            Map<String, Object> params = new HashMap<>(2);
//
//            try {
//                params.put("name",urlConfig.getUserName());
//                params.put("pswd",urlConfig.getPassword());
//
//                JSONObject header= new JSONObject();
//                header.put(HttpHeaders.ACCEPT,"application/json, text/plain, */*");
//                header.put(HttpHeaders.CONTENT_TYPE,"application/json;charset=UTF-8");
//                header.put(HttpHeaders.USER_AGENT,"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
//                header.put("Cookie","token=2511e7c314dc492a9e519ca055468d19");
//                String loginResult = HttpUtil.doPostSSL(urlConfig.getLogin(),JSON.toJSONString(header), params);
//                log.warn("loginResult====={}",loginResult);
//
//                params.clear();
//                params.put("ids", JSONArray.toJSONString(msgIds));
//                String postRes = HttpUtil.doPost(urlConfig.getInvalidMessageReplay(), params);
//                log.warn("replayRes={}",postRes);
//            } catch (Exception e) {
//                log.error("do replay error");
//                e.printStackTrace();
//            }
//        }

        return invalidValues;
    }

    @Override
    public int updInvalidMsgStaById(long id) {
        InvalidMessageEntity updInvalidMsg = new InvalidMessageEntity();
        updInvalidMsg.setId(id);
        updInvalidMsg.setStatus(InvalidMessageConstant.Status.INVALID_MESSAGE_DELETED);
        updInvalidMsg.setUpdate_by("system");
        updInvalidMsg.setUpdate_time(new Date());

        int rows = invalidMessageEntityMapper.updateByPrimaryKeySelective(updInvalidMsg);
        return rows;
    }


}
