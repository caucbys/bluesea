package com.alphalion.crawl.service.impl;

import com.alibaba.fastjson.JSON;
import com.alphalion.crawl.application.constant.Constant;
import com.alphalion.crawl.application.constant.ProductConstant;
import com.alphalion.crawl.application.constant.SymbolsConstant;
import com.alphalion.crawl.application.util.TimeUtils;
import com.alphalion.crawl.mapper.ProductEntityMapper;
import com.alphalion.crawl.mapper.ProductSettlementDetailEntityMapper;
import com.alphalion.crawl.mapper.ProductSymbolsEntityMapper;
import com.alphalion.crawl.mapper.ProductSymbolsNetEntityMapper;
import com.alphalion.crawl.mapper.entity.ProductEntity;
import com.alphalion.crawl.mapper.entity.ProductSettlementDetailEntity;
import com.alphalion.crawl.mapper.entity.ProductSymbolsEntity;
import com.alphalion.crawl.mapper.entity.ProductSymbolsNetEntity;
import com.alphalion.crawl.service.ICacheService;
import com.alphalion.crawl.service.IProductSymbolsService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SongBaoYu
 * @date 2018/1/11 下午4:54
 */
@Service
@Slf4j
public class ProductSymbolsServiceImpl implements IProductSymbolsService {

    @Autowired
    private ProductSymbolsNetEntityMapper productSymbolsNetEntityMapper;
    @Autowired
    private ProductSymbolsEntityMapper productSymbolsEntityMapper;
    @Autowired
    private ProductEntityMapper productEntityMapper;
    @Autowired
    private ProductSettlementDetailEntityMapper productSettlementDetailEntityMapper;
    @Autowired
    private ICacheService cacheService;

    @Override
    public ProductSymbolsNetEntity selectOneBySymbol(String symbol) {
        if (Strings.isNullOrEmpty(symbol)) {
            return null;
        }

        Example example = new Example(ProductSymbolsNetEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andCondition("(cusip='" + symbol + "' or isin='" + symbol + "' or symbol='" + symbol + "' or sedol='" + symbol + "')");

        List<ProductSymbolsNetEntity> productSymbolsNetEntities = productSymbolsNetEntityMapper.selectByExample(example);
        if (null != productSymbolsNetEntities && !productSymbolsNetEntities.isEmpty()) {
            return productSymbolsNetEntities.get(0);
        }
        return null;
    }

    @Override
    public List<ProductSymbolsEntity> listCusipProductsByIsin(String isin) {
        if (Strings.isNullOrEmpty(isin)) {
            return new ArrayList<>(0);
        }

        Example example = new Example(ProductSymbolsEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("symbol", isin);
        criteria.andEqualTo("type_of_symbol", ProductConstant.SymbolTypes.ISIN);
        List<ProductSymbolsEntity> productSymbolsEntities = productSymbolsEntityMapper.selectByExample(example);

        if (null != productSymbolsEntities && !productSymbolsEntities.isEmpty()) {
            long productId = productSymbolsEntities.get(0).getProduct_id();
            example.clear();
            criteria.andEqualTo("type_of_symbol", ProductConstant.SymbolTypes.CUSIP);
            criteria.andEqualTo("product_id", productId);

            productSymbolsEntities = productSymbolsEntityMapper.selectByExample(example);
        }
        return productSymbolsEntities;
    }

    @Override
    public ProductSymbolsEntity queryMaxCusipSymbolSByISIN(String isin) {
        if (Strings.isNullOrEmpty(isin)) {
            return null;
        }
        Example example = new Example(ProductSymbolsEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type_of_symbol", ProductConstant.SymbolTypes.ISIN);
        criteria.andLike("symbol", "%" + isin + "%");
        List<ProductSymbolsEntity> isinSymbolsEntities = productSymbolsEntityMapper.selectByExample(example);

        if (null != isinSymbolsEntities && !isinSymbolsEntities.isEmpty()) {
            long productId = isinSymbolsEntities.get(0).getProduct_id();
            example.clear();
            criteria.andEqualTo("type_of_symbol", ProductConstant.SymbolTypes.CUSIP);
            criteria.andEqualTo("product_id", productId);

            List<ProductSymbolsEntity> cusipSymbolsEntities = productSymbolsEntityMapper.selectByExample(example);
            if (null != cusipSymbolsEntities && !cusipSymbolsEntities.isEmpty()) {
                ProductSymbolsEntity cusipRes = new ProductSymbolsEntity();
                for (ProductSymbolsEntity cusipSymbolsEntity : cusipSymbolsEntities) {
                    if (TimeUtils.compareDate(cusipSymbolsEntity.getBusiness_thru_date(), cusipRes.getBusiness_thru_date()) >= 0) {
                        cusipRes = cusipSymbolsEntity;
                    }
                }
                return cusipRes;
            }

            return isinSymbolsEntities.get(0);
        }

        return null;
    }


    @Override
    public int updateProductSymBusiThruById(long id) {
        java.util.Date businessDate = cacheService.getBusinessDate();

        ProductSymbolsEntity productSymbolsEntity = new ProductSymbolsEntity();
        productSymbolsEntity.setId(id);
        productSymbolsEntity.setBusiness_thru_date(businessDate);
        productSymbolsEntity.setProcess_out_date(businessDate);
        int rows = productSymbolsEntityMapper.updateByPrimaryKeySelective(productSymbolsEntity);
        return rows;
    }

    @Override
    public int insertCusipProductSymbols(String cusip, long productId) {
        java.util.Date businessDate = cacheService.getBusinessDate();

        ProductSymbolsEntity productSymbolsEntity = new ProductSymbolsEntity();
        productSymbolsEntity.setProduct_id(productId);
        productSymbolsEntity.setSymbol(cusip);
        productSymbolsEntity.setType_of_symbol(ProductConstant.SymbolTypes.CUSIP);
        productSymbolsEntity.setProcess_in_date(businessDate);
        productSymbolsEntity.setProcess_out_date(Constant.INFINITY_DATE);
        productSymbolsEntity.setBusiness_from_date(businessDate);
        productSymbolsEntity.setBusiness_thru_date(Constant.INFINITY_DATE);
        productSymbolsEntity.setCreate_time(TimeUtils.getNow());
        productSymbolsEntity.setUpdate_reason("Quantex");
        productSymbolsEntity.setUpdate_by("system");

        int r = productSymbolsEntityMapper.insertSelective(productSymbolsEntity);
        return r;
    }

    @Override
    public boolean isRepeat(ProductSymbolsEntity productSymbol) {
        Example example = new Example(ProductSymbolsEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("symbol", productSymbol.getSymbol());
        criteria.andEqualTo("type_of_symbol", productSymbol.getType_of_symbol());
        criteria.andEqualTo("process_out_date", Constant.INFINITY_DATE_STR);
        criteria.andEqualTo("business_thru_date", Constant.INFINITY_DATE_STR);
        if (productSymbol.getProduct_id() > 0) {
            criteria.andEqualTo("product_id", productSymbol.getProduct_id());
        }

        List<ProductSymbolsEntity> productSymbolsEntities = productSymbolsEntityMapper.selectByExample(example);
        if (null == productSymbolsEntities || productSymbolsEntities.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public int addProduct(long productId) {
        java.util.Date businessDate = cacheService.getBusinessDate();

        ProductEntity product = new ProductEntity();
        product.setProduct_id(productId);
        product.setMarket_code("none");
        product.setTrading_status(ProductConstant.TradingStatus.TRADING);
        product.setRecord_status(ProductConstant.STATUS_ACTIVE);
        product.setAsset_type(ProductConstant.Asset.EQUITY);
        product.setSecurity_type(ProductConstant.Security.COMMON_STOCK);
        product.setIssue_category(ProductConstant.IssueCategories.US_CORP);
        product.setSic_code("6029");
        product.setSource("Quantex");
        product.setBusiness_from_date(new Date(businessDate.getTime()));
        product.setBusiness_thru_date(Constant.INFINITY_SQL_DATE);
        product.setProcess_in_date(new Date(businessDate.getTime()));
        product.setProcess_out_date(Constant.INFINITY_SQL_DATE);
        product.setUpdate_by("system");

        product.setCurrency_code(Constant.CURRENCY_US);
        product.setSecurity_subtype(ProductConstant.SecuritySub.ETF);
        product.setLong_description("none");
        product.setExchange_id(1);

        product.setGics_code("none");
        product.setShort_name("none");
        product.setCountry_of_issue("United States USA");
        product.setCountry_of_trade("United States USA");

        product.setCreate_time(TimeUtils.getTimestamp());
        product.setUpdate_time(TimeUtils.getTimestamp());
        product.setUpdate_reason("newFound");
        product.setUpdate_source("net");

        int rows = productEntityMapper.insertSelective(product);
        return rows;
    }


    @Override
    public int addSettlement(ProductSettlementDetailEntity settlementDetailEntity) {
        settlementDetailEntity.setHard_to_borrow(true);
        settlementDetailEntity.setSettlement_currency(Constant.CURRENCY_US);
        settlementDetailEntity.setDtc_indicator("none");
        settlementDetailEntity.setNsc_indicator("none");
        settlementDetailEntity.setCreate_time(TimeUtils.getTimestamp());

        java.util.Date businessDate = cacheService.getBusinessDate();
        settlementDetailEntity.setProcess_in_date(businessDate);
        settlementDetailEntity.setProcess_out_date(Constant.INFINITY_DATE);

        int rows = productSettlementDetailEntityMapper.insertSelective(settlementDetailEntity);
        return rows;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addProductSymbols(ProductSymbolsNetEntity netProduct) {
        java.util.Date businessDate = cacheService.getBusinessDate();
        ProductSymbolsEntity productSymbolsEntity = new ProductSymbolsEntity();
        productSymbolsEntity.setProduct_id(netProduct.getProduct_id());
        productSymbolsEntity.setProcess_in_date(businessDate);
        productSymbolsEntity.setProcess_out_date(Constant.INFINITY_DATE);
        productSymbolsEntity.setBusiness_from_date(businessDate);
        productSymbolsEntity.setBusiness_thru_date(Constant.INFINITY_DATE);
        productSymbolsEntity.setCreate_time(TimeUtils.getTimestamp());
        productSymbolsEntity.setUpdate_by("system");
        productSymbolsEntity.setUpdate_reason("newFound");

        int rows = 0;
        if (!Strings.isNullOrEmpty(netProduct.getIsin())) {
            productSymbolsEntity.setType_of_symbol(ProductConstant.SymbolTypes.ISIN);
            productSymbolsEntity.setSymbol(netProduct.getIsin());

            boolean repeat = isRepeat(productSymbolsEntity);
            if (!repeat) {
                rows += productSymbolsEntityMapper.insertSelective(productSymbolsEntity);
            }
        }

        if (!Strings.isNullOrEmpty(netProduct.getCusip())) {
            productSymbolsEntity.setType_of_symbol(ProductConstant.SymbolTypes.CUSIP);
            productSymbolsEntity.setSymbol(netProduct.getCusip());

            boolean repeat = isRepeat(productSymbolsEntity);
            if (!repeat) {
                rows += productSymbolsEntityMapper.insertSelective(productSymbolsEntity);
            }
        }

        if (!Strings.isNullOrEmpty(netProduct.getSymbol())) {
            productSymbolsEntity.setType_of_symbol(ProductConstant.SymbolTypes.SYMBOL);
            productSymbolsEntity.setSymbol(netProduct.getSymbol());

            boolean repeat = isRepeat(productSymbolsEntity);
            if (!repeat) {
                rows += productSymbolsEntityMapper.insertSelective(productSymbolsEntity);
            }
        }

        if (!Strings.isNullOrEmpty(netProduct.getSedol())) {
            productSymbolsEntity.setType_of_symbol(ProductConstant.SymbolTypes.SEDOL);
            productSymbolsEntity.setSymbol(netProduct.getSedol());

            boolean repeat = isRepeat(productSymbolsEntity);
            if (!repeat) {
                rows += productSymbolsEntityMapper.insertSelective(productSymbolsEntity);
            }

        }


        return rows;
    }

    @Override
    public Long getNextProductId() {
        Long productId = productEntityMapper.selectMaxProductId();
        if (null != productId) {
            return productId + 1;
        }
        return null;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addProductAndSymbols(ProductSymbolsNetEntity productSymbolsNetEntity) throws Exception {
        int r = addProduct(productSymbolsNetEntity.getProduct_id());
        if (1 != r) {
            log.error("productInfo====={}", JSON.toJSON(productSymbolsNetEntity));
            throw new Exception("insert product error: insert rows<>1");
        }

        r = addProductSymbols(productSymbolsNetEntity);
        if (r < 1) {
            log.error("productSymbolsInfo====={}", JSON.toJSON(productSymbolsNetEntity));
            throw new Exception("insert productSymbols error: insert rows=0");
        }
        return true;
    }

}
