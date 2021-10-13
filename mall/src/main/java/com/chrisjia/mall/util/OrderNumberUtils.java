package com.chrisjia.mall.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class OrderNumberUtils {

    // OrderNum prefix
    private static final String PREFIX = "00100";

    private static final int[] RandomNums = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};

    private static final int MaxLength = 5;

    private static String convertUserIdToNum(Long id) {
        String idString = id.toString();
        StringBuilder idStringBuilder = new StringBuilder();
        for (int i = idString.length() - 1; i >= 0; i--) {
            idStringBuilder.append(RandomNums[idString.charAt(i) - '0']);
        }
        return idStringBuilder.append(getRandomNum(MaxLength - idStringBuilder.length())).toString();
    }

    private static String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("HHmmss");
        return dateFormat.format(new Date());
    }

    private static long getRandomNum(long n) {
        long min = 1, max = 9;
        for (int i = 1; i < n; i++) {
            min *= 10;
            max *= 10;
        }
        long rangeLong = (((long) (new Random().nextDouble() * (max - min)))) + min;
        return rangeLong;
    }

    private static synchronized String getTimePlusNum(Long userId) {
        userId = userId == null ? 10000 : userId;
        return getTime() + convertUserIdToNum(userId);
    }

    public static String generateOrderNum(Long userId) {
        return PREFIX + getTimePlusNum(userId);
    }
}
