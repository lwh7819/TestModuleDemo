package com.lvweihao.commonlib.net;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.lvweihao.commonlib.R;
import com.lvweihao.commonlib.baseactivity.AppManager;
import com.lvweihao.commonlib.utils.PopUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

/**
 * Created by lv.weihao on 2018/1/15.
 * 返回内容回掉并统一处理错误信息
 */
public abstract class HttpCallback<T> implements Observer<T>{

    private Context mContext;
    private boolean isLogin = false;
    private int loginType;

    public HttpCallback(Context context) {
        this.mContext = context;
    }

    public HttpCallback(Context context, boolean isLogin, int loginType) {
        this.mContext = context;
        this.isLogin = isLogin;
        this.loginType = loginType;
    }
    @Override
    public void onSubscribe(Disposable d) {
        PopUtil.showWaitingDialog(mContext);
    }

    @Override
    public void onNext(T t) {
        PopUtil.dismissWaitingDialog();
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        PopUtil.dismissWaitingDialog();
        if (e instanceof IOException || e instanceof NetworkErrorException || e instanceof UnknownHostException) {
            PopUtil.showToast(mContext, mContext.getString(R.string.error_msg_connection));
        } else if (e instanceof TimeoutException) {
            PopUtil.showToast(mContext, mContext.getString(R.string.error_msg_timeout));
        } else if (e instanceof HttpException) {
            PopUtil.showToast(mContext, e.getMessage());
        } else if (e instanceof NodataException){
            PopUtil.showToast(mContext, e.getMessage());
        } else if (e instanceof retrofit2.HttpException){
            PopUtil.showToast(mContext, mContext.getString(R.string.error_msg_server));
        } else {
            PopUtil.showToast(mContext, mContext.getString(R.string.error_msg_server));
        }
        onFinished();
    }

    @Override
    public void onComplete() {
        onFinished();
    }

    /**
     * 返回成功
     * @param t
     */
    public abstract void onSuccess(T t);

    /**
     * 不管返回成功或失败最后都会调用
     */
    public void onFinished() {};
}
