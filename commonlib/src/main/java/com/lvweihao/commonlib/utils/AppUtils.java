package com.lvweihao.commonlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lvweihao.commonlib.baseactivity.AppManager;

import java.lang.reflect.Field;

/**
 * Created by lv.weihao on 2017/11/7.
 */
public class AppUtils {

    private static final String SP_DEFAULTS = "defaults";
    private static final String DEVICE_ID = "deviceId";

    /**
     * 判断App是否是Debug版本
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppDebug(Context context) {
        if (StringUtils.isSpace(context.getPackageName())) return false;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(), 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获取版本号
     * @param ctx
     * @return
     */
    public static int getVersionCode(Context ctx) {
        try {
            return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    /**
     * 获取版本名称
     * @param ctx
     * @return
     */
    public static String getVersionName(Context ctx) {
        try {
            return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /**
     * 获取设备号
     * @param ctx 上下文
     * @return deviceId
     */
    public static String getDeviceId(Context ctx) {
        String deviceId = getDefaultSetting(ctx, DEVICE_ID, null);

        if (deviceId == null) {
            WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);

            boolean wifiEnable = wifi.isWifiEnabled();
            if (!wifiEnable) {
                wifi.setWifiEnabled(true);
            }

            WifiInfo wi = wifi.getConnectionInfo();
            if (wi != null) {
                String mac = wi.getMacAddress();
                try{
                    deviceId = StringUtils.md5(mac).substring(11, 21).toUpperCase();
                }catch (Exception e){

                }
            } else {
                deviceId = "0000000000";
            }

            if (!wifiEnable) {
                wifi.setWifiEnabled(false);
            }

            saveDefaultSetting(ctx, DEVICE_ID, deviceId);
        }
        return deviceId;
    }

    /**
     * 7.0沉浸式适配
     * @param context
     */
    public static void setStateBarColor(Activity context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(context.getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
            } catch (Exception e) {}
        }
    }

    /**
     * 状态栏透明
     * @param activity
     */
    public static void enableTranslucentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }
    
    /**
     * SharePreferences保存
     * @param ctx
     * @param key
     * @param value
     */
    public static void saveDefaultSetting(Context ctx, String key, String value) {
        SharedPreferences sp = ctx.getSharedPreferences(SP_DEFAULTS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * SharedPreferences保存
     * @param ctx
     * @param key
     * @param value
     */
    public static void saveDefaultSetting(Context ctx, String key, int value) {
        SharedPreferences sp = ctx.getSharedPreferences(SP_DEFAULTS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * SharedPreferences获取
     * @param ctx
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getDefaultSetting(Context ctx, String key, String defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(SP_DEFAULTS, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    /**
     * SharedPreferences获取
     * @param ctx
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getDefaultSetting(Context ctx, String key, int defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(SP_DEFAULTS, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    /**
     * 退出程序
     * @param context
     */

    public static void exit(Context context) {
        //退出
        AppManager.getAppManager().AppExit(context);
    }
}
