package com.lding.pad.myseial.library.loadinglibrary.conn;

import android.support.annotation.NonNull;
import java.util.Random;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

/**
 * Demo的Module(模块)
 */
@Module
public class DemoModule {
    @NonNull
    private final DemoApp mApplication;

    /**
     * 模块的初始化
     * @param application 应用
     */
    public DemoModule(@NonNull DemoApp application) {
        mApplication = application;
    }

    // 单例
    @Provides @Singleton @NonNull
    public DemoApp provideApp() {
        return mApplication;
    }

    // 随机
    @Provides @Singleton @NonNull
    public Random random() {
        return new Random();
    }
}
