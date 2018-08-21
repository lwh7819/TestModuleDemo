package com.lvweihao.commonlib.net;

/**
 * Created by lv.weihao on 2018/1/15.
 * 与后台通讯时捕获的异常类
 */
public class HttpException extends RuntimeException {

    private int errorCode;
    private String detailMessage;

    public HttpException(String detailMessage) {
        super(detailMessage);
    }

    public HttpException(int resultCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = resultCode;
        this.detailMessage = detailMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }
    public String  getDetailMessage() {
        return detailMessage;
    }
}
