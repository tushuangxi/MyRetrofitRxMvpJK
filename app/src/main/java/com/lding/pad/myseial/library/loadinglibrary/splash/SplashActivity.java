package com.lding.pad.myseial.library.loadinglibrary.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lding.pad.myseial.R;
import com.lding.pad.myseial.libding.rerxmvp.view.activity.LoginActivity;
import com.lding.pad.myseial.library.loadinglibrary.conn.DemoApp;
import com.lding.pad.myseial.library.loadinglibrary.main.MainActivity;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;
import javax.inject.Named;
import dagger.Lazy;
import dagger.internal.Preconditions;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 启动页面, 初始化需要长时间加载的库
 * <p>
 * Created by wangchenlong on 16/7/27.
 * 在Android项目的应用启动前, 一般都需要加载若干功能库或者发送网络请求, 这些操作需要在首页加载前完成, 因此多数应用选择添加首屏广告或者Logo.
 * 既能提供充足的加载时间, 又能赚取商业利润和产品曝光. 最优的方案是根据耗时任务需要的时间, 设置首屏的显示时间. 本文使用Dagger与RxJava控制首页的显示时间.
 */

public class SplashActivity extends AppCompatActivity {

    @Inject Lazy<SplashLibrary> splashLibraryLazy; // 延迟闪屏库

    @Inject @Named(SplashModule.OBSERVABLE_SPLASH_LIBRARY)
    Observable<SplashLibrary> mObservable; // 闪屏库观察者 //观察SplashLibrary的延迟加载是否完成

    @Inject @Named(SplashModule.SPLASH_ACTIVITY)
    AtomicBoolean initialized; // 闪屏模块初始化

    private Subscription mSubscription; // 闪屏订阅者

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        DemoApp.app(this).splashComponent().inject(this);

        // 检测依赖注入释是否成功
        Preconditions.checkNotNull(splashLibraryLazy);
        Preconditions.checkNotNull(mObservable);
        Preconditions.checkNotNull(initialized);
    }

    //使用RxJava加载启动库, 使用计算线程, 在成功后跳转主页面, 在失败后弹出信息提示. 注意在加载库的控制,
    // 保证只加载一次. 在页面关闭时, 取消注册订阅, 即unsubscribe()
    @Override
    protected void onStart() {
        super.onStart();

        // 初始化成功
        if (initialized.get()) {
            openMainAndFinish(this, splashLibraryLazy.get());
        } else {
            // 延迟加载数据
            mSubscription = mObservable
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSuccess, this::onError);
        }
    }

    // 加载成功
    private void onSuccess(SplashLibrary library) {
        initialized.set(true);
        openMainAndFinish(SplashActivity.this, library);
    }

    // 加载失败
    private void onError(Throwable e) {
        Toast.makeText(SplashActivity.this, R.string.error_fatal, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // 取消 注册订阅者
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 释放资源
        mSubscription = null;
    }

    private static void openMainAndFinish(@NonNull Activity activity, @NonNull SplashLibrary splashLibrary) {
        // 提示加载库完成
        Toast.makeText(activity, splashLibrary.initializedString(), Toast.LENGTH_SHORT).show();

        // 跳转主页面
//        Intent intent = new Intent(activity, MainActivity.class);
//        intent.putExtra(MainActivity.EXTRA_USEFUL_STRING, splashLibrary.usefulString());
//        activity.startActivity(intent);

        //登录
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra(MainActivity.EXTRA_USEFUL_STRING, splashLibrary.usefulString());
        activity.startActivity(intent);

        // 跳转页面完成关闭当前页面
        activity.finish();
    }

    //使用Dagger+RxJava的形式是处理网络请求的优秀做法. 应用的启动页处理耗时的数据加载, 对于提升用户体验而言, 非常重要.
}
