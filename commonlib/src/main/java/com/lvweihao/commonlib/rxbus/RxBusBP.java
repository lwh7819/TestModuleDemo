package com.lvweihao.commonlib.rxbus;

import android.support.annotation.NonNull;
import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * 支持背压
 * Created by lv.weihao on 2018/1/19.
 */
public class RxBusBP {

    private final FlowableProcessor<Object> mBus;

    private RxBusBP() {
        mBus = PublishProcessor.create().toSerialized();
    }

    private static class Holder {
        private static RxBusBP instance = new RxBusBP();
    }

    public static RxBusBP getInstance() {
        return Holder.instance;
    }

    public void post(@NonNull Object obj) {
        mBus.onNext(obj);
    }

    public <T> Flowable<T> register(Class<T> clz) {
        return mBus.ofType(clz);
    }

    public Flowable<Object> register() {
        return mBus;
    }

    public void unregisterAll() {
        //会将所有由mBus生成的Flowable都置completed状态后续的所有消息都收不到了
        mBus.onComplete();
    }

    public boolean hasSubscribers() {
        return mBus.hasSubscribers();
    }

}
