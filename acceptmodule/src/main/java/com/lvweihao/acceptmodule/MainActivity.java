package com.lvweihao.acceptmodule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lvweihao.commonlib.baseactivity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R2.id.btn_go)
    Button btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setTitle("accpet");
    }

    @OnClick(R2.id.btn_go)
    public void onViewClicked() {
        // 1. 应用内简单的跳转(通过URL跳转在'进阶用法'中)
        ARouter.getInstance().build("/provider/mainActivity").navigation();

//// 2. 跳转并携带参数
//        ARouter.getInstance().build("/test/1")
//                .withLong("key1", 666L)
//                .withString("key3", "888")
//                .withObject("key4", new Test("Jack", "Rose"))
//                .navigation();
    }
}
