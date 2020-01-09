package com.lding.pad.myseial.library.loadinglibrary.splash;

import android.support.annotation.NonNull;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Named;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import rx.AsyncEmitter;
import rx.Observable;
import rx.Single;

/**
 * 闪屏模块(Module)
 * <p>
 * Created by wangchenlong on 16/7/27.
 */
@Module
public class SplashModule {
    public static final String SPLASH_ACTIVITY = "SplashActivity";

    // 提供三种不同的方式延迟加载
    public static final String OBSERVABLE_SPLASH_LIBRARY = "observable_splash_library";
    public static final String OBSERVABLE_SPLASH_LIBRARY_FROM_CALLABLE = "observable_splash_library_from_callable";
    public static final String OBSERVABLE_SPLASH_LIBRARY_ASYNC = "observable_splash_library_async";

    // 组件是否初始化完成, 不同的对象, 有相同的返回值时, 使用Named标记
    @NonNull
    @Provides @SplashScope @Named(SPLASH_ACTIVITY)
    public AtomicBoolean initialized() {
        return new AtomicBoolean(false);
    }

    @NonNull
    @Provides @SplashScope
    public SplashLibrary splashLibrary() {
        return new SplashLibrary();
    }


    //这四种模式, 可以相互替代, 使用其中一个即可. 对于Provider的区分, 使用Named标记具体注入. SplashScope标签表示模块在组件内复用, 不会重复创建.

    //Defer加载. 只有在订阅Observable的时候, 才会调用splashLazy.get(), 防止阻塞线程
    @NonNull
    @Provides @SplashScope @Named(OBSERVABLE_SPLASH_LIBRARY)
    public Observable<SplashLibrary> splashLibraryObservable(
            final Lazy<SplashLibrary> splashLazy) {
        return Observable.defer(() -> Observable.just(splashLazy.get()));
    }

    //加载缓慢任务需要使用Rxjava的Defer操作.  FromCallback加载, 是Defer的简化形式
    @NonNull
    @Provides @SplashScope @Named(OBSERVABLE_SPLASH_LIBRARY_FROM_CALLABLE)
    public Observable<SplashLibrary> splashLibraryObservableFromCallable(
            final Lazy<SplashLibrary> splashLazy) {
        return Observable.fromCallable(splashLazy::get);
    }

    //FromAsync加载, 使用Emitter模式, 处理获得的数据.
    @NonNull
    @Provides @SplashScope @Named(OBSERVABLE_SPLASH_LIBRARY_ASYNC)
    public Observable<SplashLibrary> splashLibraryObservableAsync(
            final Lazy<SplashLibrary> splashLazy) {
        return Observable.fromAsync(emitter -> emitter.onNext(splashLazy.get())
                , AsyncEmitter.BackpressureMode.NONE);
    }

    //Single模式, 只发送单个数值, 也可以使用Single替代, 简洁.
    @Provides @NonNull
    @SplashScope
    public Single<SplashLibrary> splashLibrarySingle(
            final Lazy<SplashLibrary> splashLazy) {
        return Single.defer(() -> Single.just(splashLazy.get()));
    }
}
