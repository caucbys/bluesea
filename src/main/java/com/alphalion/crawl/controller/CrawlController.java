package com.alphalion.crawl.controller;

import com.alphalion.crawl.service.ICrawlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author SongBaoYu
 * @date 2018/1/10 下午9:24
 */

@Api(description = "爬取产品数据接口类")
@RequestMapping(value = "/crawl")
@Controller
public class CrawlController {

    @Autowired
    private ICrawlService crawlService;


    @ApiOperation(httpMethod = "POST",value = "启动爬取线程接口",notes = "启动爬取线程接口")
    @RequestMapping(method = RequestMethod.POST,value = "/crawlProductSymbols")
    @ResponseBody
    public String crawlProductSymbols(){
        crawlService.crawlProductIdentifiers();
        return "crawl process has started";
    }
}
