/*
 * Copyright (C) Bluesea Fintech, Inc, 北京晨灏科技有限公司，Bluesea Fintech USA, LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 */

package com.alphalion.crawl.application.util;

import com.alibaba.fastjson.JSON;

import java.util.Random;

/**
 * Created by zhangla on 2016/12/26.
 */
public class RandomUtil {

    private static final Random random = new Random();

    public static final char[] charPool = new char[62];

    static {
        int i = 0;
        for (char c = '0'; c <= '9'; c++) {
            charPool[i++] = c;
        }
        for (char c = 'a'; c <= 'z'; c++) {
            charPool[i++] = c;
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            charPool[i++] = c;
        }
    }

    public static String randomStr(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(charPool[random.nextInt(charPool.length)]);
        }
        return stringBuilder.toString();
    }


    static final int[] arr;

    static {
        arr = new int[300];
        for (int i = 1; i <= 300; ++i) {
            arr[i - 1] = i;
        }
    }

    public static void main(String[] args) {
        Random random = new Random();

        int len = 300;
        for (int i = 0; i < 300; ++i) {
            int n = random.nextInt(300) % len;
            --len;
            int old = arr[n];
            arr[n] = arr[len];
            arr[len] = old;
        }

        System.out.println("arr = " + JSON.toJSON(arr));

    }

    private static int random(int low, int high) {
        Random random = new Random(System.currentTimeMillis());
        int res = random.nextInt(high - low + 1) + low;
        return res;
    }


}
