package com.lvweihao.commonlib.utils;

import android.content.Context;

import java.util.Map;

/**
 * 类型处理Util
 */
public class TypeListUtil {

   /**
    * 文件转换map数组
    * @param ctx 上下文
    * @param file 文件路径
    * @return map数组
    */
   public static Map<String, Object> loadResFile(Context ctx, String file) {
      try {
         String res = FileUtil.loadAssertFile(ctx, file);
         return JsonUtil.fastJson2ObjMap(res);
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         return null;
      }
   }
}
