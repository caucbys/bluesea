package com.alphalion.crawl.controller;

import com.alphalion.crawl.controller.dto.Result;
import com.alphalion.crawl.mapper.ProductEntityMapper;
import com.alphalion.crawl.mapper.ProductSymbolsEntityMapper;
import com.alphalion.crawl.mapper.ProductSymbolsNetEntityMapper;
import com.alphalion.crawl.mapper.entity.ProductEntity;
import com.alphalion.crawl.mapper.entity.ProductSymbolsEntity;
import com.alphalion.crawl.mapper.entity.ProductSymbolsNetEntity;
import com.alphalion.crawl.service.IProductSymbolsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author SongBaoYu
 * @date 2018/2/7 下午7:11
 */

@Api(description = "产品相关接口")
@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private IProductSymbolsService productSymbolsService;
    @Autowired
    private ProductSymbolsEntityMapper productSymbolsEntityMapper;
    @Autowired
    private ProductSymbolsNetEntityMapper productSymbolsNetEntityMapper;
    @Autowired
    private ProductEntityMapper productEntityMapper;

    @ApiOperation(httpMethod = "POST", value = "添加产品", notes = "添加产品，主键自增")
    @RequestMapping(method = RequestMethod.POST, value = "/addProductEntity")
    public Result addProductEntity(@RequestBody ProductEntity productEntity) {
        Long productId = productEntity.getProduct_id();
        if (null == productId || 0 == productId) {
            productId = productSymbolsService.getNextProductId();
        }

        if (null != productId) {
            productEntity.setProduct_id(productId);
            productEntityMapper.insertSelective(productEntity);
            return Result.successed(productId);
        }
        return Result.failed("操作失败");
    }


    @ApiOperation(httpMethod = "POST", value = "添加产品", notes = "添加产品，主键自增")
    @RequestMapping(method = RequestMethod.POST, value = "/addProduct")
    public Result addProduct() {
        Long productId = productSymbolsService.getNextProductId();
        if (null != productId) {
            productSymbolsService.addProduct(productId);
            return Result.successed(productId);
        }
        return Result.failed("操作失败");
    }

    @ApiOperation(httpMethod = "POST", value = "查询产品symbols信息", notes = "需要symbol")
    @RequestMapping(method = RequestMethod.POST, value = "/listSymbols")
    public Result listSymbols(@RequestBody ProductSymbolsEntity symbol) {
        List<ProductSymbolsEntity> symbolsEntities = productSymbolsEntityMapper.select(symbol);
        return Result.successed(symbolsEntities);
    }

    @ApiOperation(httpMethod = "POST", value = "获取下一产品ID", notes = "获取下一产品ID")
    @RequestMapping(method = RequestMethod.POST, value = "/getNextProductId")
    public Result getNextProductId() {
        Long productId = productSymbolsService.getNextProductId();
        return Result.successed(productId);
    }


    @ApiOperation(httpMethod = "POST", value = "录入symbols信息", notes = "需要productId")
    @RequestMapping(method = RequestMethod.POST, value = "/insert")
    public Result insert(@RequestBody ProductSymbolsEntity symbol) {
        if (null == symbol.getProduct_id() || 0 == symbol.getProduct_id()) {
            return Result.failed("录入失败:需要productId");
        }
        int r = productSymbolsEntityMapper.insertSelective(symbol);
        return Result.successed("录入成功记录数为:" + r);
    }

    @ApiOperation(httpMethod = "POST", value = "录入备用symbols信息", notes = "备用")
    @RequestMapping(method = RequestMethod.POST, value = "/insertBakSymbols")
    public Result insertBakSymbols(@RequestBody ProductSymbolsNetEntity symbol) {
        int r = productSymbolsNetEntityMapper.insertSelective(symbol);
        return Result.successed("录入成功记录数为:" + r);
    }

    @ApiOperation(httpMethod = "POST", value = "录入产品和symbols信息", notes = "会生成product和symbols两种记录")
    @RequestMapping(method = RequestMethod.POST, value = "/addProductAndSymbols")
    public Result addProductAndSymbols(@RequestBody ProductSymbolsNetEntity netEntity) {
        Long productId = netEntity.getProduct_id();
        if (null == productId || 0 == productId) {
            productId = productSymbolsService.getNextProductId();
        }

        if (null != productId) {
            netEntity.setProduct_id(productId);
            try {
                productSymbolsService.addProductAndSymbols(netEntity);
                return Result.successed(productId);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.failed(e.getMessage());
            }
        }
        return Result.failed("不能获取有效的productId");
    }


}
