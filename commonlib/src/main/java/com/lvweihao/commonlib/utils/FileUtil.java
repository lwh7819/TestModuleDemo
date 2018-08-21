package com.lvweihao.commonlib.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件处理Util
 */
public class FileUtil {

   /**
    * 载入声明文件
    * @param cxt 上下文
    * @param fileName 文件名
    * @return 文件流字符串
    * @throws IOException
    */
   public static String loadAssertFile(Context cxt, String fileName) throws IOException {
      InputStream is = cxt.getAssets().open(fileName);
      byte[] buffer = new byte[is.available()];
      is.read(buffer);
      is.close();

      return new String(buffer);
   }

   /**
    * 获取temp文件夹
    * @return temp文件夹
    */
   public static File tempDir() {
      return dirWithName("temp");
   }

   /**
    * 获取zjxghis文件夹下文件夹
    * @param name 文件夹名
    * @return 如存在直接返回,不存在新建后返回
    */
   public static File dirWithName(String name) {
      File dir = new File(rootDir(), name);
      if (!dir.exists()) {
         dir.mkdir();
      }
      return dir;
   }

   /**
    * 获取ROM上名为"wjsdxg"的文件夹
    * @return 如存在则直接返回文件夹,不存在新建后返回
    */
   public static File rootDir() {
      File root = new File(Environment.getExternalStorageDirectory(), "zjylfw");
      if (!root.exists()) {
         root.mkdir();
      }
      return root;
   }

   /**
    * 获取app.apk文件
    * @return app.apk文件
    */
   public static File apkFile() {
      return new File(tempDir(), "app.apk");
   }
}
