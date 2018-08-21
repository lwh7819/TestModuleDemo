package com.lvweihao.commonlib.baseactivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvweihao.commonlib.R;
import com.lvweihao.commonlib.utils.AppUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import io.reactivex.disposables.CompositeDisposable;

/**
 * activity基类，实现导航栏
 * <p>
 * Created by lv.weihao on 2018/1/15.
 */

public class BaseActivity extends AppCompatActivity {
    private final static int LEFT_BTN = 101;
    private final static int RIGHT_BTN = 102;

    private FrameLayout mContentView;
    private TextView mTitleView;

    private View mNavigationBar;
    private Button mLeftButton;
    private Button mRightButton;
    private Button mCloseButton;
    private ImageView mShadow;

    private SystemBarTintManager tintManager;

    public CompositeDisposable mCompositeDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.setStateBarColor(this);
        super.setContentView(R.layout.activity_navigation);

        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        setStatusBarColor(getResources().getColor(R.color.status_bar_color_main));

        mContentView = (FrameLayout) findViewById(R.id.content_view);
        mTitleView = (TextView) findViewById(R.id.navigation_bar_title);
        mTitleView.setText(getTitle());

        mNavigationBar = findViewById(R.id.navigation_bar);
        mLeftButton = (Button) findViewById(R.id.navigation_bar_btn_left);
        mRightButton = (Button) findViewById(R.id.navigation_bar_btn_right);
        mCloseButton = (Button) findViewById(R.id.navigation_bar_btn_close);
        mShadow = (ImageView) findViewById(R.id.shadow);

        //添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {//rx_java注意isDisposed是返回是否取消订阅
            mCompositeDisposable.dispose();
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
    }

    /**
     * 是否使状态栏透明化的方法
     *
     * @param on 是否透明
     */
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @Override
    public void setContentView(int layoutResID) {
        setContentView(getLayoutInflater().inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        mContentView.addView(view);
    }


    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentView.addView(view, params);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTitleView.setText(getTitle());
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        mTitleView.setText(getTitle());
    }

    /**
     * 设置关闭按钮显示与否
     *
     * @param visible  是否显示
     * @param listener 监听事件
     */
    public void setCloseButtonVisible(boolean visible, View.OnClickListener listener) {
        if (visible) {
            mCloseButton.setVisibility(View.VISIBLE);
            mCloseButton.setOnClickListener(listener);
        } else {
            mCloseButton.setVisibility(View.GONE);
        }
    }

    /**
     * 设置返回按钮显示与否
     *
     * @param visible 是否显示
     */
    public void setBackButtonVisible(boolean visible) {
        if (visible) {
            setLeftButton(R.drawable.navigation_bar_back_selector, -1, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            setLeftButton(-1, -1, null);
        }
    }

    /**
     * 设置导航栏左边按钮显示与否及样式
     *
     * @param icResID    图标资源
     * @param titleResID 主题资源
     * @param listener   监听对象
     * @return
     */
    public Button setLeftButton(int icResID, int titleResID, View.OnClickListener listener) {
        return setButton(LEFT_BTN, icResID, titleResID, listener);
    }

    /**
     * 设置导航栏右边按钮显示与否及样式
     *
     * @param icResID    图标资源
     * @param titleResID 主题资源
     * @param listener   监听对象
     * @return
     */
    public Button setRightButton(int icResID, int titleResID, View.OnClickListener listener) {
        return setButton(RIGHT_BTN, icResID, titleResID, listener);
    }

    public Button setLeftButton(Drawable icon, CharSequence title, View.OnClickListener listener) {
        return setButton(LEFT_BTN, icon, title, listener);
    }

    public Button setRightButton(Drawable icon, CharSequence title, View.OnClickListener listener) {
        return setButton(RIGHT_BTN, icon, title, listener);
    }

    /**
     * 设置各按钮样式的各种实现
     *
     * @param buttonType button类型
     * @param icResID    图标资源
     * @param titleResID 主题资源
     * @param listener   监听对象
     * @return
     */
    private Button setButton(int buttonType, int icResID, int titleResID, View.OnClickListener listener) {
        Drawable icon;
        CharSequence title;

        try {
            icon = getResources().getDrawable(icResID);
        } catch (Resources.NotFoundException e) {
            icon = null;
        }

        try {
            title = getResources().getText(titleResID);
        } catch (Resources.NotFoundException e) {
            title = null;
        }

        return setButton(buttonType, icon, title, listener);
    }

    private Button setButton(int buttonType, Drawable icon, CharSequence title, View.OnClickListener listener) {
        Button mButton = null;
        switch (buttonType) {
            case LEFT_BTN:
                mButton = mLeftButton;
                break;
            case RIGHT_BTN:
                mButton = mRightButton;
                break;
        }

        mButton.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        mButton.setText(title);

        mButton.setOnClickListener(listener);
        mButton.setVisibility(listener == null ? View.GONE : View.VISIBLE);

        if (listener == null) {
            mButton.setVisibility(View.GONE);
            return null;
        } else {
            mButton.setVisibility(View.VISIBLE);
            return mButton;
        }
    }

    /**
     * 设置导航栏颜色
     *
     * @param color 颜色
     */
    public void setStatusBarColor(int color) {
        // 自定义颜色
        tintManager.setTintColor(color);
    }

    /**
     * 设置导航栏是否显示
     *
     * @param visible   是否显示
     * @param animation 转场动画对象
     */
    public void setNavigationBarVisible(boolean visible, boolean animation) {
        if (animation) {
            if (visible) {
                mNavigationBar.animate().translationY(0).alpha(1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        mNavigationBar.setVisibility(View.VISIBLE);
                        mNavigationBar.setAlpha(0);
                    }
                });
            } else {
                mNavigationBar.animate().translationY(-mNavigationBar.getHeight()).alpha(0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mNavigationBar.setVisibility(View.GONE);
                    }
                });
            }
        } else {
            mNavigationBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏导航栏下边界阴影
     */
    public void hideShadow() {
        mShadow.setVisibility(View.GONE);
    }
}
