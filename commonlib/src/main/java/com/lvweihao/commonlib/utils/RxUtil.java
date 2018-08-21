package com.lvweihao.commonlib.utils;

import com.lvweihao.commonlib.net.HttpException;
import com.lvweihao.commonlib.net.HttpResult;
import com.lvweihao.commonlib.net.HttpResultUAP;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 *RXjava工具类
 */
public class RxUtil {

    /**
     * 对Http返回结果做预处理，对逻辑错误抛出异常
     *
     * @param <T>
     * @return
     */
    public static <T> Function<HttpResult<T>, T> handleHttpResult() {
        return new Function<HttpResult<T>, T>() {

            @Override
            public T apply(HttpResult<T> tHttpResult) throws Exception {
                if (tHttpResult.getResult() != HttpResult.SUCCESS) {
                    throw new HttpException(tHttpResult.getResult(), tHttpResult.getMessage());
                }
//                String accessToken = tHttpResult.getAccessToken();
//                if (accessToken != null) {
//                    MWUser mwUser = MWUser.getCurrentUser();
//                    if (mwUser != null) {
//                        mwUser.setAccessToken(accessToken);
//                        MWUser.changeCurrentUser(mwUser);
//                    }
//                }
                return tHttpResult.getData();
            }
        };
    }

    /**
     * 对Http返回结果做预处理，对逻辑错误抛出异常
     *
     * @param <T>
     * @return
     */
    public static <T> Function<HttpResultUAP<T>, T> handleHttpResultUAP() {
        return new Function<HttpResultUAP<T>, T>() {

            @Override
            public T apply(HttpResultUAP<T> tHttpResultUAP) throws Exception {
                if (tHttpResultUAP.getResult() != HttpResultUAP.SUCCESS) {
                    throw new HttpException(tHttpResultUAP.getResult(), tHttpResultUAP.getMessage());
                }
                return tHttpResultUAP.getData();
            }
        };
    }

    /**
     * RXJava线程切换
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> setThread(){
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
