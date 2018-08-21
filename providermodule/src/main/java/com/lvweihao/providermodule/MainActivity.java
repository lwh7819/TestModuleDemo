package com.lvweihao.providermodule;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lvweihao.commonlib.baseactivity.BaseActivity;

@Route(path = "/provider/mainActivity")
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_activity_main);

        setTitle("provider");
        setBackButtonVisible(true);
    }
}
