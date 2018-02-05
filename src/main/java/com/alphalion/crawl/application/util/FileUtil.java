package com.alphalion.crawl.application.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * @author SongBaoYu
 * @date 2018/2/3 下午4:46
 */
public class FileUtil {


    public static OutputStream downloadFileFromFile(String savePath, String url) {
        try {
            String fileName = parseFileNameFromUrl(url);
            URL src = new URL(url);
            InputStream inputStream = src.openStream();
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(savePath + fileName));

            int b = 0;
            while ((b = inputStream.read()) != -1) {
                outputStream.write(b);
            }

            outputStream.close();
            inputStream.close();
            return outputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String parseFileNameFromUrl(String url) {
        int index = url.lastIndexOf("/");
        if (index >= 0) {
            return url.substring(index+1);
        }

        return "unknown";
    }
}
