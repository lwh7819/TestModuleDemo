package com.lvweihao.commonlib.rxbus;

/**
 * Created by lv.weihao on 2018/1/19.
 */
public class CompNameEvent<T> {
    public static final int COMP_NAME_REQUEST_CODE = 100;
    public static final int PROVIDER_NAME_REQUEST_CODE = 101;
    public static final int USER_REQUEST_CODE = 102;
    public static final int ACCEPTER_COMP_NAME_REQUEST_CODE = 103;
    public static final int MANAGEMENT_COMP_NAME_REQUEST_CODE = 104;
    public static final int STOP_COMP_NAME_REQUEST_CODE = 105;
    public static final int ORGANIZATION_NAME = 106;

    private T data;

    private String compName;
    private String compCode;
    private String regionCode;
    private int requestCode;

    public CompNameEvent(T data , int requestCode) {
        this.data = data;
        this.requestCode = requestCode;
    }

    public CompNameEvent(String compName, String compCode, int requestCode) {
        this.compName = compName;
        this.compCode = compCode;
        this.requestCode = requestCode;
    }

    public CompNameEvent(String compName, String compCode, String regionCode, int requestCode) {
        this.compName = compName;
        this.compCode = compCode;
        this.regionCode = regionCode;
        this.requestCode = requestCode;
    }

    public T getData() {
        return data;
    }

    public String getCompName() {
        return compName;
    }

    public String getCompCode() {
        return compCode;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public String getRegionCode() {return regionCode;}
}
