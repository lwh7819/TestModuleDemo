package com.lvweihao.commonlib.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.*;

/**
 * JSON处理Util
 */
@SuppressWarnings("unchecked")
public class JsonUtil {

   public static Map<String, Object> fastJson2ObjMap(String obj) {
      return JSON.parseObject(obj, Map.class);
   }

   public static Map<String, String> fastJson2Map(String obj) {
      return JSON.parseObject(obj, Map.class);
   }

   public static List array2List(JSONArray array) {
      String js = JSONObject.toJSONString(array, SerializerFeature.WriteClassName);//将array数组转换成字符串
      List<JSONObject>  collection = JSONObject.parseArray(js, JSONObject.class);//把字符串转换成集合
      return collection;
   }
}
