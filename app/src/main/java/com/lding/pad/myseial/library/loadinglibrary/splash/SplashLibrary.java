package com.lding.pad.myseial.library.loadinglibrary.splash;

import android.os.SystemClock;
import android.util.Log;

/**
 * 模拟的加载库, 模拟高耗时
 * <p>
 * Created by wangchenlong on 16/7/27.
 *
 * SplashActivity使用Dagger注入加载库. 模拟Splash的耗时较长的加载库, 并提供若干信息返回.
 */
public class SplashLibrary {
    private static final String TAG = SplashActivity.class.getSimpleName();

    public SplashLibrary() {
        // 模拟高耗时任务, 需要5秒
        for (int i = 0; i < 5; i++) {
            Log.d(TAG, String.format("i = %1$s", i));
            SystemClock.sleep(1000);
        }
    }

    public String usefulString() {
        return "Useful string. " + getClass().getName();
    }

    // 初始化完成
    public String initializedString() {//已初始化
//        return "Initialized " + getClass().getSimpleName();
        return "初始化完成";
    }
}
