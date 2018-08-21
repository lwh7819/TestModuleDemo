package com.lvweihao.commonlib.utils;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringUtils {

   private static final String REGEX_URL = "^https?://[\\w.-]+(:\\d+)?[/\\w .-]*$";

   /**
    * 判断字符串是否为null或全为空格
    *
    * @param s 待校验字符串
    * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
    */
   public static boolean isSpace(String s) {
      return (s == null || s.trim().length() == 0);
   }

   /**
    * 获取当前日期
    * @param format 日期格式化
    * @return 日期字符串
    */
   public static String getDateString(String format) {
      SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
      return formatter.format(new Date());
   }

   /**
    * 格式化日期
    * @param date 需要格式化日期的字符串
    * @return 根据源字符串的不同返回不同格式的字符串
    * @throws ParseException 转换错误
     */
   public static String formatDate(String date) throws ParseException {
      SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat formatYMDHM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      StringBuffer sb = new StringBuffer();
      if (date.length() >= 10) {
         sb.append(new SimpleDateFormat("yyyy年MM月dd日").format(formatYMD.parse(date)));
      }
      if (date.length() >= 16) {
         sb.append(new SimpleDateFormat("HH时mm分").format(formatYMDHM.parse(date)));
      }
      return sb.toString();
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

   public static String NotNullString(String str) {
      if (str == null || str.trim().isEmpty()) {
         return "";
      } else {
         return str;
      }
   }
}
