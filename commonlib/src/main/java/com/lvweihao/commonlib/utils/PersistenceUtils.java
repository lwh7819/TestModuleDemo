package com.lvweihao.commonlib.utils;


import android.content.Context;
import android.os.Environment;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 持久化工具类
 */
public class PersistenceUtils {
   private static ConcurrentHashMap<String, ReentrantReadWriteLock> fileLocks = new ConcurrentHashMap<>();

   private static ReentrantReadWriteLock getLock(String path) {
      ReentrantReadWriteLock lock = fileLocks.get(path);
      if (lock == null) {
         lock = new ReentrantReadWriteLock();
         ReentrantReadWriteLock oldLock = fileLocks.putIfAbsent(path, lock);
         if (oldLock != null) {
            lock = oldLock;
         }
      }
      return lock;
   }

   /**
    * 释放锁
    * @param path
    */
   public static void removeLock(String path) {
      fileLocks.remove(path);
   }

   /**
    * 从Assets中的fileName文件读取内容并返回字符串
    * @param ctx
    * @param fileName
    * @return
    */
   public static String readStringFromAssetsFile(Context ctx, String fileName) {
      byte[] data = readBytesFromAssetsFile(ctx, fileName);
      if (data != null && data.length != 0) {
         return new String(data);
      }
      return null;
   }

   /**
    * 获取ROM上名为"wjydzf"的文件夹
    * @return 如存在则直接返回文件夹,不存在新建后返回
    */
   public static File rootDir() {
      File root = new File(Environment.getExternalStorageDirectory(), "wjydzf");
      if (!root.exists()) {
         root.mkdir();
      }
      return root;
   }

   /**
    * 获取zjwj文件夹下文件夹
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
    * 获取资源文件并缓存
    * @param cxt 上下文
    * @param fileName 文件名称
    * @return 缓存文件
    */
   public static File getAssetFileToTempDir(Context cxt, String fileName) {
      try {
         File file = new File(dirWithName("temp"), fileName);
         file.getParentFile().mkdirs();
         file.createNewFile();

         InputStream is = cxt.getAssets().open(fileName);
         FileOutputStream fos = new FileOutputStream(file);
         byte[] buffer = new byte[1024];

         int i = 0;
         while ((i = is.read(buffer)) > 0) {
            fos.write(buffer, 0, i);
         }

         fos.close();
         is.close();

         return file;
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return null;
   }

   /**
    * 从Assets中的fileName文件读取内容并返回字节数组
    * @param ctx
    * @param fileName
    * @return
    */
   public static byte[] readBytesFromAssetsFile(Context ctx, String fileName) {
      byte[] data = null;

      if (ctx != null && fileName != null && !fileName.trim().isEmpty()) {
         InputStream is = null;

         try {
            is = ctx.getAssets().open(fileName);
            data = readContentFromStream(is);
         } catch (IOException e) {
            // go throw
         } finally {
            closeQuietly(is);
         }
      }

      return data;
   }

   /**
    * 从file文件读取内容并返回字符串
    * @param file
    * @return
    */
   public static String readStringFromFile(File file) {
      byte[] data = readContentFromFile(file);
      if (data != null && data.length != 0) {
         return new String(data);
      }
      return null;
   }

   /**
    * 从file文件读取内容并返回字字节数组
    * @param file
    * @return
    */
   public static byte[] readContentFromFile(File file) {
      byte[] data = null;

      if (file != null && file.exists() && file.isFile()) {
         ReentrantReadWriteLock.ReadLock readLock = getLock(file.getAbsolutePath()).readLock();
         readLock.lock();

         FileInputStream fis = null;

         try {
            fis = new FileInputStream(file);
            data = readContentFromStream(fis);
         } catch (IOException e) {
            // go throw
         } finally {
            closeQuietly(fis);
            readLock.unlock();
         }
      }

      return data;
   }

   /**
    * 从流中读取数据并返回字节数组
    * @param is
    * @return
    * @throws IOException
    */
   public static byte[] readContentFromStream(InputStream is) throws IOException {
      BufferedInputStream bis = new BufferedInputStream(is, 8192);
      byte[] data = new byte[is.available()];
      int offset = 0;

      while (offset < data.length) {
         int count = bis.read(data, offset, data.length - offset);
         offset += count;
      }

      return data;
   }


   /**
    * 爆粗字符串到file文件中
    * @param string
    * @param file
    * @return
    */
   public static boolean saveStringToFile(String string, File file) {
      try {
         return saveContentToFile(string.getBytes("utf-8"), file);
      } catch (UnsupportedEncodingException e) {
         return false;
      }
   }

   /**
    * 保存字节数组到file文件中
    * @param data
    * @param file
    * @return
    */
   public static boolean saveContentToFile(byte[] data, File file) {
      ReentrantReadWriteLock.WriteLock writeLock = getLock(file.getAbsolutePath()).writeLock();
      boolean succeed = true;

      if (writeLock.tryLock()) {
         FileOutputStream fos = null;

         try {
            fos = new FileOutputStream(file, false);
            fos.write(data);
         } catch (IOException e) {
            succeed = false;
         } finally {
            closeQuietly(fos);
            writeLock.unlock();
         }
      }

      return succeed;
   }

   /**
    *关闭流并释放与此流关联的所有系统资源。
    * @param closeable
    */
   public static void closeQuietly(Closeable closeable) {
      try {
         if (closeable != null) {
            closeable.close();
         }
      } catch (IOException e) {}
   }
}
