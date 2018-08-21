package com.lvweihao.commonlib.net;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by lv.weihao on 2018/1/15.
 * 处理服务端传输过来的数据，result为0时，data为服务端传送的数据主体。result为1时，data表示与服务端通讯错误的消息提示message。
 */
public class HttpResult<T> {

    public static final int SUCCESS = 0;

    @JSONField(name = "status")
    private int result;

    @JSONField(name = "data")
    private T data;

    @JSONField(name = "error")
    private JSONObject error;

    @JSONField(name = "access_token")
    private String accessToken;

    private String code;
    private String message;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public T getData() {
        if (data == null) {
            throw new NodataException("没有数据");
        } else {
            return data;
        }
    }
    
    public void setData(T data) {
        this.data = data;
    }

    public JSONObject getError() {
        return error;
    }

    public void setError(JSONObject error) {
        this.error = error;
    }

    public String getCode() {
        return error.getString("code");
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return error.getString("message");
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
