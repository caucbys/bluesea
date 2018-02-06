package com.alphalion.crawl.application.util;

import com.alibaba.fastjson.JSON;
import com.alphalion.crawl.application.constant.ProductConstant;
import com.alphalion.crawl.mapper.entity.ProductSymbolsNetEntity;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author SongBaoYu
 * @date 2018/1/4 下午4:12
 */
@Slf4j
public class CrawlUtil {

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36";
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final String BASE_URL = "https://www.google.com/search?q=%s&r=%s";


    public static ProductSymbolsNetEntity crawlSymbol(String cusip) throws IOException {
        ProductSymbolsNetEntity extenedProduct = new ProductSymbolsNetEntity();
        Document document = downloadHtml(String.format(BASE_URL, cusip, ProductConstant.SymbolTypes.ISIN));
        Elements spans = document.select("span");

        for (Element span : spans) {
            parseElements(span, ProductConstant.SymbolTypes.ISIN, extenedProduct);
            if (StringUtils.isNotEmpty(extenedProduct.getIsin())) {
                break;
            }
        }

        if (StringUtils.isEmpty(extenedProduct.getSymbol())) {
            document = downloadHtml(BASE_URL + cusip + "+" + ProductConstant.SymbolTypes.SYMBOL);
            spans = document.select("span");

            for (Element span : spans) {
                parseElements(span, ProductConstant.SymbolTypes.SYMBOL, extenedProduct);
                if (StringUtils.isNotEmpty(extenedProduct.getSymbol())) {
                    break;
                }
            }
        }

        return extenedProduct;
    }


    private static ProductSymbolsNetEntity parseElements(Element span, String symbolTypes, ProductSymbolsNetEntity extenedProduct) {
        if (span.hasText()) {
            String spanText = span.text();
            if (spanText.toUpperCase().matches(".*" + symbolTypes + ".*")) {
                System.out.println("spanText = [" + spanText + "]");
                parseSymbolFromSpanText(spanText, extenedProduct);
            }
        }

        return extenedProduct;
    }

    public static Document downloadHtml(String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        Document document = connection.userAgent(USER_AGENT)
                .timeout(CONNECTION_TIMEOUT).get();

        return document;
    }

    private static ProductSymbolsNetEntity parseSymbolFromSpanText(String spanText, ProductSymbolsNetEntity extenedProduct) {
        String[] split = spanText.split("\\.");
        for (String str : split) {
            try {
                str = str.trim();
                if (str.indexOf(ProductConstant.SymbolTypes.ISIN) > -1 && StringUtils.isEmpty(extenedProduct.getIsin())) {
                    String ISIN = str.substring(ProductConstant.SymbolTypes.ISIN.length() + 1).trim();
                    if (SymbolUtil.checkISIN(ISIN)) {
                        extenedProduct.setIsin(ISIN);
                    }
                } else if (str.indexOf(ProductConstant.SymbolTypes.CUSIP) > -1 && StringUtils.isEmpty(extenedProduct.getCusip())) {
                    String CUSIP = str.substring(ProductConstant.SymbolTypes.CUSIP.length() + 1).trim();
                    if (SymbolUtil.checkCUSIP(CUSIP)) {
                        extenedProduct.setCusip(CUSIP);
                    }
                } else if(str.indexOf(ProductConstant.SymbolTypes.WKN)>-1 && Strings.isNullOrEmpty(extenedProduct.getCusip())){
                    String WKN = str.substring(ProductConstant.SymbolTypes.WKN.length() + 1).trim();
                    if (SymbolUtil.checkWKN(WKN)) {
                        extenedProduct.setCusip(WKN);
                    }
                }
                else if (StringUtils.isEmpty(extenedProduct.getSymbol())) {
                    int index = str.toUpperCase().indexOf(ProductConstant.SymbolTypes.SYMBOL);
                    if (index > -1) {
                        String symbol = str.toUpperCase().substring(index + ProductConstant.SymbolTypes.SYMBOL.length() + 1).trim();
                        if (StringUtils.isEmpty(symbol)) {
                            symbol = str.substring("Simbol".length() + 1).trim();
                            if (StringUtils.isNotEmpty(symbol)) {
                                if (symbol.length() >= 12) {
                                    symbol = symbol.substring(0, 4);
                                }
                            }
                        }

                        if (SymbolUtil.checkSYMBOL(symbol)) {
                            extenedProduct.setSymbol(symbol);
                        }
                    }
                }

                if (SymbolUtil.checkISIN(extenedProduct.getIsin())) {
                    break;
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
                ex.printStackTrace();
            }

        }
        return extenedProduct;
    }


    private static String getRootPath() {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String rootPath = path.replace("/target/classes", "");
        rootPath = rootPath.replace("/target/test-classes", "");
        return rootPath;
    }


}
