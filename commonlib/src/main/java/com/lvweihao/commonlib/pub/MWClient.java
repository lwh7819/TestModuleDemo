package com.lvweihao.commonlib.pub;

import android.content.Context;

import com.lvweihao.commonlib.net.HttpClient;
import com.lvweihao.commonlib.utils.AppUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lv.weihao on 2018/1/15.
 */
public class MWClient extends HttpClient {
    public static final String APP_KEY = "A9911FB3F1381138";

    /**
     * 上下文
     */
    private static Context sContext = null;
    /**
     * 版本号
     */
    private static int sVersionCode;
    /**
     * 设备号
     */
    private static String sDeviceId;
    /**
     * 版本名称
     */
    private static String sVersionName;
    /**
     * 用户
     */
    private MWUser currentUser = null;
    /**
     * JDClient实体
     */
    private static MWClient mClient;
    /**
     * 网络接口api实体
     */
    private static MWApi mApi;

    //单例模式
    private MWClient() {
    }

    /**
     * 获取单例MWClient对象
     *
     * @return MWClient
     */
    public synchronized static MWClient getInstance() {
        if (mClient == null) {
            synchronized (MWClient.class) {
                if (mClient == null) {
                    mClient = new MWClient();
                }
            }
        }
        return mClient;
    }

    /**
     * 用于在application中注册
     *
     * @param context
     */
    public static void init(Context context) {
        if (sContext != null) {
            throw new IllegalStateException("Already initialized");
        }

        sContext = context;
        sVersionCode = AppUtils.getVersionCode(context);
        sVersionName = AppUtils.getVersionName(context);
        sDeviceId = AppUtils.getDeviceId(context);
    }

    /**
     * 获取上下文
     *
     * @return context
     */
    public static Context getConText() {
        if (sContext == null) {
            throw new IllegalStateException("Call MWCliet.initialized first.");
        }
        return sContext;
    }

    /**
     * 创建params
     *
     * @return params
     */
    public static Map<String, String> newParams() {
        return new HashMap<String, String>() {
        };
    }

    public static String getUserId() {
        MWUser mwUser = MWUser.getCurrentUser();
        if (mwUser != null) {
            return mwUser.getUserId();
        } else {
            return "";
        }
    }

    public static String getBeareaToken() {
        MWUser mwUser = MWUser.getCurrentUser();
        if (mwUser != null) {
            String beareaToken = mwUser.getAccessToken();
            return beareaToken;
        } else {
            return "";
        }
    }

    public static File getClientDocumentDir() {
        return getConText().getDir("client", Context.MODE_PRIVATE);
    }

    public MWUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(MWUser currentUser) {
        this.currentUser = currentUser;
    }

    public int getVersionCode() {
        return sVersionCode;
    }

    public void setVersionCode(int sVersionCode) {
        MWClient.sVersionCode = sVersionCode;
    }

    public String getDeviceId() {
        return sDeviceId;
    }

    public void setDeviceId(String sDeviceId) {
        MWClient.sDeviceId = sDeviceId;
    }

    public String getVersionName() {
        return sVersionName;
    }

    public void setVersionName(String sVersionName) {
        MWClient.sVersionName = sVersionName;
    }

    public String getVersionString() {
        return String.format("%s(Build %d)", sVersionName, sVersionCode);
    }

    public static String getDefaultSetting(String key, String defaultValue) {
        if (sContext == null) {
            return defaultValue;
        } else {
            return AppUtils.getDefaultSetting(sContext, key, defaultValue);
        }
    }

    public static int getDefaultSetting(String key, int defaultValue) {
        if (sContext == null) {
            return defaultValue;
        } else {
            return AppUtils.getDefaultSetting(sContext, key, defaultValue);
        }
    }

    public static void saveDefaultSetting(String key, String value) {
        if (sContext != null) {
            AppUtils.saveDefaultSetting(sContext, key, value);
        }
    }

    public static void saveDefaultSetting(String key, int value) {
        if (sContext != null) {
            AppUtils.saveDefaultSetting(sContext, key, value);
        }
    }

    /**
     * 获取api实体
     *
     * @return MWApi
     */
    public MWApi Api() {
        return mApi;
    }

    @Override
    protected String getBaseUrl() {
        //测试
//        return "http://192.168.22.223:2205/resource/api/hzyf/";

        //开发
//        return "http://192.168.22.221:8005/api/hzyf/";

        //正式
        return "https://www.hzwsjds.com/resource/api/hzyf/";
    }

    @Override
    protected void initApi() {
        mApi = retrofit.create(MWApi.class);
    }
}
