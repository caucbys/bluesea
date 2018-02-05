package com.alphalion.crawl.service.impl;

import com.alibaba.fastjson.JSON;
import com.alphalion.crawl.application.constant.ProductConstant;
import com.alphalion.crawl.application.constant.SymbolsConstant;
import com.alphalion.crawl.application.util.CrawlUtil;
import com.alphalion.crawl.application.util.PdfUtil;
import com.alphalion.crawl.application.util.RandomUtil;
import com.alphalion.crawl.mapper.ProductSymbolsNetEntityMapper;
import com.alphalion.crawl.mapper.entity.ProductSymbolsNetEntity;
import com.alphalion.crawl.service.ICrawlService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author SongBaoYu
 * @date 2018/1/10 下午8:43
 */

@Service(value = "crawlServiceImpl")
@Slf4j
public class CrawlServiceImpl implements ICrawlService {

    @Autowired
    private ProductSymbolsNetEntityMapper productSymbolsNetEntityMapper;
    private static final String IDENTIFIER_BASE_URL = "https://yoursri.com/companies?b_start:int=";
    private static final int IDENTIFIER_PAGE_SIZE = 100;
    private final Set<String> cusipSet;


    CrawlServiceImpl() {
        cusipSet = new CopyOnWriteArraySet<>();
    }


    @Override
    public void crawlProductIdentifiers() {
        new Thread(new CrawlUrlThread()).start();
    }

    @Override
    public void crawlSymbolsFromPDF() {
        try {
            Set<ProductSymbolsNetEntity> set = PdfUtil.readContent(SymbolsConstant.WESPATH_PDF_UR);
            if (null != set && !set.isEmpty()) {
                int rows = productSymbolsNetEntityMapper.batchInsertSymbolsIgnoreErrors(new ArrayList<>(set));
                System.out.println("wait for insert ProductSymbolsNetEntity rows=" + set.size() + ".");
                System.out.println("insert ProductSymbolsNetEntity rows=" + rows + " successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private final class CrawlUrlThread implements Runnable {
        private final Set<String> identifierUrls;

        public CrawlUrlThread() {
            Thread.currentThread().setName("crawl-url-thread");
            identifierUrls = new CopyOnWriteArraySet<>();
        }

        @Override
        public void run() {
            Document document = null;
            Elements dts = null;
            Elements href = null;

            for (int i = 1; i < 109; ++i) {
                try {
                    document = CrawlUtil.downloadHtml(IDENTIFIER_BASE_URL + (i - 1) * IDENTIFIER_PAGE_SIZE);

                    dts = document.select("#content-core dl dt");
                    if (dts.hasText()) {
                        for (Element dt : dts) {
                            href = dt.select("span a[href]");
                            if (href.hasAttr("href")) {
                                String link = href.attr("href");
                                System.out.println("link======" + link);
                                if (StringUtils.isNotEmpty(link)) {
                                    identifierUrls.add(link);
                                }

                                if (identifierUrls.size() > 300) {
                                    new Thread(new CrawlIdentifierThread(new HashSet<>(identifierUrls))).start();
                                    identifierUrls.clear();
                                }
                            }

                        }
                    }
                } catch (IOException e) {
                    log.error("read link from https://yoursri.com time out page={}.", i);
                    e.printStackTrace();
                }
            }

            if (!identifierUrls.isEmpty()) {
                new Thread(new CrawlIdentifierThread(identifierUrls)).start();
            }

            System.out.println("crawl url thread has finished...");
        }
    }


    private final class CrawlIdentifierThread implements Runnable {

        private final Set<String> urls;
        private final Set<ProductSymbolsNetEntity> productSymbolsNetEntitySet;
        private final String threadName;


        public CrawlIdentifierThread(Set<String> urls) {
            threadName = "crawl-identifier-thread-" + RandomUtil.randomStr(6);
            Thread.currentThread().setName(threadName);
            this.urls = urls;
            productSymbolsNetEntitySet = new CopyOnWriteArraySet<>();
        }

        @Override
        public void run() {
            String requestUrl = null;
            Document document = null;
            Element tbody = null;
            Elements trs = null;
            Element td1 = null;
            ProductSymbolsNetEntity extenedProduct = null;

            for (String url : urls) {
                requestUrl = url + "/view-details?ajax_load=" + System.currentTimeMillis() + "&";
                try {
                    document = CrawlUtil.downloadHtml(requestUrl);
                    tbody = document.selectFirst("table.investmentInfo");
                    trs = tbody.select("tr");
                    extenedProduct = new ProductSymbolsNetEntity();
                    for (Element tr : trs) {
                        td1 = tr.select("td").first();
                        if (td1.hasClass("bgDarkGrey")) {
                            String columnName = td1.text().toUpperCase().trim();
                            String tr2Text = td1.nextElementSibling().text().trim();
                            if (columnName.indexOf("TICKER") > -1 || columnName.indexOf(ProductConstant.SymbolTypes.SYMBOL) > -1) {
                                extenedProduct.setSymbol(tr2Text);
                            } else if (columnName.indexOf(ProductConstant.SymbolTypes.ISIN) > -1) {
                                extenedProduct.setIsin(tr2Text);
                            } else if (columnName.indexOf(ProductConstant.SymbolTypes.CUSIP) > -1) {
                                extenedProduct.setCusip(tr2Text);
                            }
                        }
                    }

                    //数据去重
                    String cusip = extenedProduct.getCusip();
                    if (!Strings.isNullOrEmpty(cusip)) {
                        if (!cusipSet.contains(cusip)) {
                            productSymbolsNetEntitySet.add(extenedProduct);
                            System.out.println("productInfo[" + threadName + "]=" + JSON.toJSON(extenedProduct));
                        }
                    }
                } catch (IOException e) {
                    log.error("url=={} read time out.", url);
                    e.printStackTrace();
                }
            }
            productSymbolsNetEntityMapper.batchInsertSymbolsIgnoreErrors(new ArrayList<>(productSymbolsNetEntitySet));
            System.out.println(threadName + " ended...");
        }
    }

    @PostConstruct
    public void releaseResourses() {
        cusipSet.clear();
    }


}
