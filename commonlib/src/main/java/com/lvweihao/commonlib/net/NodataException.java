package com.lvweihao.commonlib.net;

/**
 * Created by lv.weihao on 2018/1/15.
 * 与后台通讯时获取空对象错误
 */
public class NodataException extends RuntimeException {

    private int errorCode;
    private String detailMessage;

    public NodataException(String detailMessage) {
        super(detailMessage);
    }

    public NodataException(int resultCode, String detailMessage) {
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
