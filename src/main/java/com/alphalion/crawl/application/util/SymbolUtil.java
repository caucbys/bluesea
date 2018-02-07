package com.alphalion.crawl.application.util;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author SongBaoYu
 * @date 2018/1/13 上午11:59
 */
public class SymbolUtil {

    private static final int ISIN_LENGTH = 12;
    private static final int CUSIP_LENGTH = 9;
    private static final int SEDOL_LENGTH = 7;

    private static final int[] SEDOL_WEIGHT = {1, 3, 1, 7, 3, 9};


    private static int transferChar2Number(char c) {
        int num = c - '0';
        if (num >= 0 && num <= 9) {
            return num;
        }

        num = c - 'A' + 10;
        return num;
    }

    private static int[] char2Numbers(char c) {
        int[] ints;
        int number = transferChar2Number(c);
        if (0 <= number && number <= 9) {
            ints = new int[1];
            ints[0] = number;
            return ints;
        }

        ints = new int[2];
        ints[0] = number / 10;
        ints[1] = number % 10;
        return ints;
    }


    private static List<Integer> chars2Numbers(String identifier) {
        char[] chars = identifier.toCharArray();
        List<Integer> numbers = new ArrayList<>(chars.length * 2);
        for (char aChar : chars) {
            int[] ns = char2Numbers(aChar);
            for (int n : ns) {
                numbers.add(n);
            }
        }

        return numbers;
    }


    private static int sum(List<Integer> numbers) {
        int[] odd;//奇数位
        int[] even;//偶数位
        int size = numbers.size();

        //把数字分成两组，奇数位组和偶数位组
        even = new int[size / 2];
        boolean isEven = size % 2 == 0;
        if (isEven) {
            odd = new int[size / 2];
        } else {
            odd = new int[size / 2 + 1];
        }

        for (int i = 0; i < size; ++i) {
            if (i % 2 == 0) {
                odd[i / 2] = numbers.get(i);
            } else {
                even[(i - 1) / 2] = numbers.get(i);
            }
        }

        //把最后一位所在的组每个数字字符乘2，如果结果是两位数，则个位数与十位数相加，最后累计求和
        //另一个数组直接求和
        int oddSum = 0;
        int evenSum = 0;
        if (isEven) {
            for (int n : even) {
                if (5 <= n && n <= 9) {
                    n = 2 * n - 9;
                } else {
                    n *= 2;
                }
                evenSum += n;
            }

            for (int m : odd) {
                oddSum += m;
            }
        } else {
            for (int n : odd) {
                if (5 <= n && n <= 9) {
                    n = 2 * n - 9;
                } else {
                    n *= 2;
                }
                oddSum += n;
            }

            for (int m : even) {
                evenSum += m;
            }
        }

        return oddSum + evenSum;
    }


    //校验最后一位
    private static boolean checkSum(String identifier) {
        List<Integer> numbers = chars2Numbers(identifier.substring(0, identifier.length() - 1));
        String lastNumStr = identifier.substring(identifier.length() - 1);
        Integer lastNum = Integer.valueOf(lastNumStr);

        int sum = sum(numbers);
        //sum对10求余
        sum %= 10;
        //从十相减
        sum = 10 - sum;
        //在对10求余
        sum %= 10;


        if (lastNum.equals(sum)) {
            return true;
        }
        return false;
    }

    public static boolean checkISIN(String ISIN) {
        if (Strings.isNullOrEmpty(ISIN) || ISIN_LENGTH != ISIN.length()) {
            return false;
        }
        try {
            boolean valid = checkSum(ISIN);
            return valid;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkCUSIP(String CUSIP) {
        if (Strings.isNullOrEmpty(CUSIP) || CUSIP_LENGTH != CUSIP.length()) {
            return false;
        }
        try {
            boolean valid = checkSum(CUSIP);
            if (!valid) {
                return checkWKN(CUSIP);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean checkSEDOL(String SEDOL) {
        if (Strings.isNullOrEmpty(SEDOL) || SEDOL_LENGTH != SEDOL.length()) {
            return false;
        }
        try {
            String sedol = SEDOL.substring(0, SEDOL_LENGTH - 1);
            String lastLetterStr = SEDOL.substring(SEDOL_LENGTH - 1);
            Integer lastDigit = Integer.valueOf(lastLetterStr);

            int sum = 0;
            char[] chars = sedol.toCharArray();
            for (int i = 0; i < SEDOL_LENGTH - 1; ++i) {
                int n = transferChar2Number(chars[i]);
                sum += n * SEDOL_WEIGHT[i];
            }

            sum %= 10;
            sum = 10 - sum;
            sum %= 10;

            if (lastDigit.equals(sum)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkSYMBOL(String SYMBOL) {
        if (Strings.isNullOrEmpty(SYMBOL)) {
            return false;
        }

        String pattern = "^[0-9a-zA-Z]+$";
        boolean matches = Pattern.matches(pattern, SYMBOL);
        return matches;
    }

    public static boolean checkWKN(String wkn) {
        if (Strings.isNullOrEmpty(wkn)) {
            return false;
        }

        if (wkn.length() == 9) {
            return true;
        }

        return false;
    }


    public static void main(String[] args) {

        System.out.println("res = " + checkCUSIP("120831169"));
    }


    public static boolean checkInvalidValue(String invalidValue) {
        if (Strings.isNullOrEmpty(invalidValue)) {
            return false;
        }

        return true;
    }
}