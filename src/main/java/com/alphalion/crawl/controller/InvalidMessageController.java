package com.alphalion.crawl.controller;

import com.alphalion.crawl.controller.dto.Result;
import com.alphalion.crawl.service.IInvalidMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author SongBaoYu
 * @date 2018/1/12 下午2:33
 */
@Api(description = "invalid_message 操作类接口APIs文档")
@RestController
@RequestMapping(value = "/invalidmessage")
public class InvalidMessageController {

    @Autowired
    private IInvalidMessageService invalidMessageService;

    @ApiOperation(httpMethod = "POST", value = "处理invalid message接口", notes = "处理invalid message")
    @RequestMapping(value = "/processInvalidProducts", method = RequestMethod.POST)
    public Result processInvalidProducts() {
        List<String> invalidProducts = invalidMessageService.processInvalidProducts();
        return Result.successed(invalidProducts);
    }


}
