package com.lvweihao.commonlib.rxbus;

import android.support.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * rxbus 不支持背压
 * Created by lv.weihao on 2018/1/19.
 */
public class RxBus {
    private final Subject<Object> mBus;

    private RxBus() {
        mBus = PublishSubject.create().toSerialized();
    }

    public static RxBus getInstance() {
        return Holder.BUS;
    }

    public void post(@NonNull Object obj) {
        mBus.onNext(obj);
    }

    public <T> Observable<T> register(Class<T> tClass) {
        return mBus.ofType(tClass);
    }

    public Observable<Object> register() {
        return mBus;
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    public void unregisterAll() {
        //会将所有由mBus生成的Observable都置completed状态,后续的所有消息都收不到了
        mBus.onComplete();
    }

    private static class Holder {
        private static final RxBus BUS = new RxBus();
    }

}
