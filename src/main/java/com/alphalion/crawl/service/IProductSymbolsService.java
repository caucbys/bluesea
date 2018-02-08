package com.alphalion.crawl.service;

import com.alphalion.crawl.mapper.entity.ProductSettlementDetailEntity;
import com.alphalion.crawl.mapper.entity.ProductSymbolsEntity;
import com.alphalion.crawl.mapper.entity.ProductSymbolsNetEntity;

import java.util.List;

/**
 * @author SongBaoYu
 * @date 2018/1/11 下午4:52
 */
public interface IProductSymbolsService {

    ProductSymbolsNetEntity selectOneBySymbol(String symbol);

    List<ProductSymbolsEntity> listCusipProductsByIsin(String isin);

    ProductSymbolsEntity queryMaxCusipSymbolSByISIN(ProductSymbolsNetEntity symbol) throws Exception;


    int updateProductSymBusiThruById(long id);

    int insertCusipProductSymbols(String cusip, long productId);

    boolean isRepeat(ProductSymbolsEntity productSymbol);

    int addProduct(long productId);

    int addSettlement(ProductSettlementDetailEntity settlementDetailEntity);

    int addProductSymbols(ProductSymbolsNetEntity productSymbolsNetEntity);

    Long getNextProductId();

    boolean addProductAndSymbols(ProductSymbolsNetEntity productSymbolsNetEntity) throws Exception;

}
