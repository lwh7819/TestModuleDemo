package com.lvweihao.commonlib.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;

import com.lvweihao.commonlib.R;

/**
 * Created by lv.weihao on 2018/3/8.
 * 计时器
 */
public class TimeCount extends CountDownTimer {
    private Button mButton;
    private Context context;

    public TimeCount(long millisInFuture, long countDownInterval, Context context, Button mButton) {
        super(millisInFuture, countDownInterval);
        this.mButton = mButton;
        this.context = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mButton.setBackground(context.getResources().getDrawable(R.drawable.login_btn_bg_unclickable));
        mButton.setClickable(false);
        mButton.setText("(" + millisUntilFinished / 1000 + ") 秒后可重新发送");
    }

    @Override
    public void onFinish() {
        mButton.setText("重新获取验证码");
        mButton.setClickable(true);
        mButton.setBackground(context.getResources().getDrawable(R.drawable.login_btn_bg));

    }
}
