package com.lvweihao.commonlib.net;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by lv.weihao on 2018/1/15.
 * 平台用户认证返回体
 */
public class AuthResult extends JSONObject{
    /**
     * success
     */
    public String getAccessToken() {
        return this.getString("access_token");
    }

    public String getTokenType() {
        return this.getString("token_type");
    }

    public String getRefreshToken() {
        return this.getString("refresh_token");
    }

    public String getExpiresIn() {
        return this.getString("expires_in");
    }

    public String getScope() {
        return this.getString("scope");
    }

    /**
     * 用户信息错误
     */
    public String getErrorDescription() {
        return this.getString("error_description");
    }

    /**
     * CD密钥错误
     */
    public String getTimestamp() {
        return this.getString("timestamp");
    }

    public String getStatus() {
        return this.getString("status");
    }

    public String getError() {
        return this.getString("error");
    }

    public String getMessage() {
        return this.getString("message");
    }

    public String getPath() {
        return this.getString("path");
    }
}
