package com.lvweihao.commonlib.net;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by lv.weihao on 2018/1/15.
 * 处理UAP服务端传输过来的数据，result为0时，data为服务端传送的数据主体。result为1时，error表示与服务端通讯错误的消息提示message。
 */
public class HttpResultUAP<T> {

    public static final int SUCCESS = 0;

    @JSONField(name = "status")
    private int result;

    @JSONField(name = "data")
    private T data;

    @JSONField(name = "result")
    private JSONObject error;

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
            return (T) new Object();
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
        return error.getString("msg");
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
