package com.alphalion.crawl.service.impl;

import com.alibaba.fastjson.JSON;
import com.alphalion.crawl.application.config.UrlConfig;
import com.alphalion.crawl.application.constant.InvalidMessageConstant;
import com.alphalion.crawl.application.constant.ProductConstant;
import com.alphalion.crawl.application.util.CrawlUtil;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

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
    public List<String> processInvalidProducts() {
        InvalidMessageEntity queryCondition = new InvalidMessageEntity();
        queryCondition.setStatus("E");
        List<InvalidMessageEntity> invalidMessages = invalidMessageEntityMapper.select(queryCondition);

        if (null == invalidMessages || invalidMessages.isEmpty()) {
            return new ArrayList<>(0);
        }

        List<Long> invalidMsgIds = new ArrayList<>();
        HashMap<String, InvalidMessageEntity> invalidMsgMap = new HashMap<>();

        for (InvalidMessageEntity invalidMessage : invalidMessages) {
            String invalidValue = invalidMessage.getInvalid_value();
            if (SymbolUtil.checkInvalidValue(invalidMessage)) {
                invalidMsgMap.put(invalidValue, invalidMessage);
            } else {
                invalidMsgIds.add(invalidMessage.getId());
            }
        }

        Collection<InvalidMessageEntity> newSymbols = invalidMsgMap.values();
        ProductSymbolsNetEntity productSymbolsNetEntity = null;
        InvalidMessageEntity nextMsg = null;

        //备用产品库查询产品----精准匹配
        List<ProductSymbolsNetEntity> dbSymbols = new ArrayList<>();
        Iterator<InvalidMessageEntity> iterator = newSymbols.iterator();
        while (iterator.hasNext()) {
            nextMsg = iterator.next();
            productSymbolsNetEntity = productSymbolsService.selectOneBySymbol(nextMsg.getInvalid_value());
            boolean valid = addSymbols(dbSymbols, productSymbolsNetEntity, nextMsg.getInvalid_value());
            if (valid) {
                iterator.remove();
            }
        }

        //爬取产品信息---广泛搜索
        List<ProductSymbolsNetEntity> netSymbols = new ArrayList<>();
        iterator = newSymbols.iterator();
        while (iterator.hasNext()) {
            nextMsg = iterator.next();
            try {
                productSymbolsNetEntity = CrawlUtil.crawlSymbol(nextMsg.getInvalid_value());
                boolean valid = addSymbols(netSymbols, productSymbolsNetEntity, nextMsg.getInvalid_value());
                if (valid) {
                    iterator.remove();
                }
            } catch (Exception e) {
                log.error("爬取产品出错: {}", e.getMessage());
            }

        }

        //未搜索到的产品
        List<ProductSymbolsNetEntity> unknownSymbols = new ArrayList<>();
        iterator = newSymbols.iterator();
        while (iterator.hasNext()) {
            nextMsg = iterator.next();
            unknownSymbols.add(new ProductSymbolsNetEntity(nextMsg.getInvalid_value(), "", "", ""));
        }


        List<ProductSymbolsNetEntity> hasFoundSymbols = new ArrayList<>();
        hasFoundSymbols.addAll(dbSymbols);
        hasFoundSymbols.addAll(netSymbols);
//        hasFoundSymbols.addAll(unknownSymbols);
        log.info("dbSymbols====" + JSON.toJSON(dbSymbols));
        log.info("netSymbols====" + JSON.toJSON(netSymbols));
        log.info("unknownSymbols====" + JSON.toJSON(unknownSymbols));

        //插入有效产品
        List<String> successfulInvalidValues = new ArrayList<>();
        List<String> failedInvalidValues = new ArrayList<>();
        ProductSymbolsEntity maxCusipSymbolInfo;
        for (ProductSymbolsNetEntity foundSymbol : hasFoundSymbols) {
            try {
                String isin = foundSymbol.getIsin();
                if (Strings.isNullOrEmpty(isin)) {
                    isin = foundSymbol.getCusip();
                }

                maxCusipSymbolInfo = productSymbolsService.queryMaxCusipSymbolSByISIN(isin);
                //数据库中存在过期的产品
                if (null != maxCusipSymbolInfo) {
                    if (ProductConstant.SymbolTypes.CUSIP.equals(maxCusipSymbolInfo.getType_of_symbol())) {
                        productSymbolsService.updateProductSymBusiThruById(maxCusipSymbolInfo.getId());
                    } else if (ProductConstant.SymbolTypes.ISIN.equals(maxCusipSymbolInfo.getType_of_symbol())) {
                        //插入新的产品
                        productSymbolsService.insertCusipProductSymbols(foundSymbol.getCusip(), maxCusipSymbolInfo.getProduct_id());
                        successfulInvalidValues.add(foundSymbol.getInvalid_value());
                    }
                } else {//不存在
                    Long productId = productSymbolsService.getNextProductId();
                    if (null == productId) {
                        failedInvalidValues.add(foundSymbol.getInvalid_value());
                        continue;
                    }
                    foundSymbol.setProduct_id(productId);
                    productSymbolsService.addProductAndSymbols(productSymbolsNetEntity);
                    successfulInvalidValues.add(foundSymbol.getInvalid_value());
                }


            } catch (Exception e) {
                failedInvalidValues.add(foundSymbol.getInvalid_value());
                e.printStackTrace();
            }
        }


        //不规则记录
        if (!invalidMsgIds.isEmpty()) {
            updInvalidMsgStaByIds(invalidMsgIds);
            log.info("delete invalid_message logically for ids={},invalid_value={}", JSON.toJSON(invalidMsgIds));
        }


        log.warn("Crawl process has finished...");
        log.warn("successfulInvalidValues={}", JSON.toJSON(successfulInvalidValues) + " need to replay...");
        log.warn("failedInvalidValues={}", JSON.toJSON(failedInvalidValues) + " need to review...");


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

        return successfulInvalidValues;
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

    @Override
    public int updInvalidMsgStaByIds(List<Long> ids) {
        Example example = new Example(InvalidMessageEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", ids);

        InvalidMessageEntity updValue = new InvalidMessageEntity();
        updValue.setStatus(InvalidMessageConstant.Status.INVALID_MESSAGE_DELETED);
        updValue.setUpdate_by("system");
        updValue.setUpdate_time(new Date());

        int rows = invalidMessageEntityMapper.updateByExampleSelective(updValue, example);
        return rows;
    }


    /**
     * either ISIN or CUSIP is valid
     *
     * @param list
     * @param symbolInfo
     */
    private boolean addSymbols(List list, ProductSymbolsNetEntity symbolInfo, String invalidValue) {
        if (null != symbolInfo) {
            boolean valid = SymbolUtil.checkCUSIP(symbolInfo.getCusip()) || SymbolUtil.checkISIN(symbolInfo.getIsin());
            if (valid) {
                symbolInfo.setInvalid_value(invalidValue);
                list.add(symbolInfo);
                return true;
            }
        }
        return false;
    }

}
