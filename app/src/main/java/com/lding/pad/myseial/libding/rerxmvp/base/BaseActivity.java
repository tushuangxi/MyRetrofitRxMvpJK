package com.lding.pad.myseial.libding.rerxmvp.base;

import android.app.Activity;
import android.os.Bundle;
import com.lding.pad.myseial.libding.utils.ZTLUtils;

public class BaseActivity extends Activity {
    public Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        new ZTLUtils(mActivity).setTranslucentStatus();
    }
}
