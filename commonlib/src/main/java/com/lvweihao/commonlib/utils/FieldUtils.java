package com.lvweihao.commonlib.utils;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lvweihao.commonlib.R;
import com.lvweihao.commonlib.pub.MWClient;
import com.lvweihao.commonlib.view.InputButton;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

public class FieldUtils {
    /**
     * 获取JSONObject对象的值
     * @param obj JSONObject对象
     * @param key JSONObject对象的键
     * @return JSONObject对象对应键的值,如果为空返回空字符串
     */
    public static String getStrField(JSONObject obj, String key) {
        Object o = obj.get(key);
        return (o == null) ? "" : o.toString();
    }

    /**
     * 获取JSONObject对象的值
     * @param obj JSONObject对象
     * @param key JSONObject对象的键
     * @return JSONObject对象对应键的值,如果为空返回"0"
     */
    public static String getNumField(JSONObject obj, String key) {
        Object o = obj.get(key);
        return (o == null) ? "0" : o.toString();
    }

    /**
     * TextView字符串赋值
     * @param tv TextView视图
     * @param o Object对象
     */
    public static void setFieldText(TextView tv, Object o) {
        String txt = (o == null) ? "" : o.toString();
        tv.setText(txt);
    }

    public static boolean putString(JSONObject obj, String key, TextView tv) {
        String str = tv.getText().toString();
        if (str.isEmpty()) {
            obj.remove(key);
            return false;
        }

        obj.put(key, str);
        return true;
    }

    public static String getInputButtonCode(InputButton inputButton) {
        if (inputButton.getInputValue() == null) {
            return "";
        } else {
            return inputButton.getInputValue().toString();
        }
    }

    public static String getTypeNameFromCode(String typeFile, String code) {
        return getTypeNameFromCode(typeFile, code, null);
    }

    public static String getTypeNameFromCode(String typeFile, String code, String subName) {
        String str = PersistenceUtils.readStringFromAssetsFile(MWClient.getConText(), typeFile);
        JSONArray array = JSON.parseArray(str);
        return getTypeNameFromCode(array, code, subName);
    }

    public static String getTypeNameFromCode(JSONArray array, String code, String subName) {
        return getTypeNameFromCode(array, code, subName, "NAME");
    }

    /**
     * 从JSONArray中返回指定的字符串。
     * @param array JSONArray原数组
     * @param code JSON中"CODE"对应的字符串
     * @param subName 想要返回的字符串在JSONArray中的最后一级的父节点名称
     * @param keyName 返回的字符串对应的key
     * @return
     */
    public static String getTypeNameFromCode(JSONArray array, String code, String subName, String keyName){
        if (code != null) {
            for (Object obj : array) {
                JSONObject jObj = (JSONObject) obj;

                if (code.equals(jObj.getString("CODE"))) {
                    return jObj.getString(keyName);
                }

                if (!BaseUtils.isBlankString(subName) && jObj.containsKey(subName)) {
                    String name = getTypeNameFromCode(jObj.getJSONArray(subName), code, subName, keyName);
                    if (name != null) {
                        return name;
                    }
                }
            }
        }
        return null;
    }
}
