package com.alphalion.crawl.controller;

import com.alibaba.fastjson.JSON;
import com.alphalion.crawl.controller.dto.AuthRequest;
import com.alphalion.crawl.controller.dto.Result;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author SongBaoYu
 * @date 2018/1/16 上午10:34
 */

@Api(description = "测试接口类")
@RestController
@RequestMapping(value = "/test")
@Slf4j
public class TestController {


    @ApiOperation(httpMethod = "GET", value = "hello", notes = "hello")
    @RequestMapping(value = "/hello/{msg}", method = RequestMethod.GET)
    public Result hello(@PathVariable("msg") String msg) {
        log.info("hello: {}", msg);
        return Result.successed(msg);
    }


    @ApiOperation(httpMethod = "POST", value = "welcome", notes = "welcome")
    @RequestMapping(value = "/welcome", method = RequestMethod.POST)
    public Result welcome(@RequestParam(value = "page") Integer page) {
        return Result.successed(page);
    }


    @ApiOperation(httpMethod = "POST", value = "auth", notes = "auth")
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public Result doAuth(@RequestBody AuthRequest authRequest) {
        log.info("request===={}", JSON.toJSON(authRequest));
        if (Strings.isNullOrEmpty(authRequest.getUserName())) {
            return Result.failed("userName cann`t be null.");
        }

        return Result.successed(authRequest);
    }

    public static void main(String[] args) {


    }


}
