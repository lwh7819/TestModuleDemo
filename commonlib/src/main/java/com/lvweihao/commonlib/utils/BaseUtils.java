package com.lvweihao.commonlib.utils;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BaseUtils {
    private static final String REGEX_URL = "^https?://[\\w.-]+(:\\d+)?[/\\w .-]*$";

    /**
     * 判断字符串是否为标准http格式
     * @param str 原字符串
     * @return true：http格式的字符串；false：空字符串或者不是标准格式的http地址
     */
    public static boolean isValidUrl(String str) {
        return !isBlankString(str) && str.matches(REGEX_URL);
    }

    /**
     * 判断字符串是否为空或者是否是空字符串
     * @param str 原字符串
     * @return true：字符串为空或者空字符串 false：非空字符串
     */
    public static boolean isBlankString(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 判断两个对象是否相等
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean isEqual(Object obj1, Object obj2) {
        return obj1 == obj2 || (obj1 != null && obj1.equals(obj2));
    }

    public static String getFormatDateTime(String formatter, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatter, Locale.getDefault());
        return simpleDateFormat.format(date);
    }


    /**
     * 返回字符串str的MD5值
     * @param str
     * @return
     */
    public static String md5(final String str) {
        try {
            final byte[] hash = MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8"));
            final StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
