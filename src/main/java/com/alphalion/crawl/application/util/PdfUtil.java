package com.alphalion.crawl.application.util;

import com.alibaba.fastjson.JSON;
import com.alphalion.crawl.mapper.entity.ProductSymbolsNetEntity;
import com.google.common.base.Strings;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author SongBaoYu
 * @date 2018/2/3 下午5:19
 */
public class PdfUtil {

    public static Set<ProductSymbolsNetEntity> readContent(String url) throws Exception {
        URL src = new URL(url);
        InputStream inputStream = src.openStream();
        PDDocument document = PDDocument.load(inputStream);

        PDDocumentCatalog documentCatalog = document.getDocumentCatalog();
        List<PDPage> pages = documentCatalog.getAllPages();

        HashSet<ProductSymbolsNetEntity> productSymbolsNetEntitySet = new HashSet<>();
        if (null != pages) {
            int size = pages.size();
            PDFTextStripper pst = new PDFTextStripper();
            StringWriter sw;
            ProductSymbolsNetEntity net;

            for (int i = 0; i < size; ++i) {
                //获取指定页主要文本
                sw = new StringWriter();
                pst.setStartPage(i + 1);
                pst.setEndPage(i + 1);
                pst.writeText(document, sw);
                String table = sw.getBuffer().toString();

                //根据数据行拆分
                String[] rows = table.split("\n");
                if (null != rows && rows.length > 0) {
                    for (int k = 0; k < rows.length; ++k) {

                        //具体内容按行解析
                        String row = rows[k];
                        if (!Strings.isNullOrEmpty(row)) {
                            String[] columns = row.split(" ");
                            if (null != columns && columns.length > 0) {
                                System.out.println("row = [" + row + "]");
                                for (int m = columns.length - 1; m >= 0; --m) {
                                    String col = columns[m].trim();
                                    if (SymbolUtil.checkISIN(col)) {
                                        net = new ProductSymbolsNetEntity();
                                        net.setIsin(col);

                                        --m;
                                        if (m > -1) {
                                            col = columns[m].trim();
                                            if (col.length() == 9) {
                                                net.setCusip(col);
                                                net.setSymbol(columns[m - 1].trim());
                                            } else {
                                                net.setSymbol(col);
                                            }
                                        }
                                        System.out.println("res = " + JSON.toJSON(net));
                                        productSymbolsNetEntitySet.add(net);
                                        break;
                                    }
                                }
                            }
                        }

                    }
                }

            }
        }


        return productSymbolsNetEntitySet;
    }


    public static void main(String[] args) {
    }
}
