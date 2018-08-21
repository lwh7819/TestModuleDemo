package com.lvweihao.testmoduledemo;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lvweihao.commonlib.pub.BaseApplication;
import com.lvweihao.commonlib.utils.AppUtils;

/**
 * Created by lv.weihao on 2018/8/21.
 */

public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        if (AppUtils.isAppDebug(mContext)) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }
}
