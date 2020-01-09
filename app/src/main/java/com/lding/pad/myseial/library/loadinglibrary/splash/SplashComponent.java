package com.lding.pad.myseial.library.loadinglibrary.splash;

import com.lding.pad.myseial.library.loadinglibrary.conn.DemoComponent;
import dagger.Component;

/**
 * 闪屏的组件(Component)
 * <p>
 * Created by wangchenlong on 16/7/27.
 *
 * 启动页的组件, 依赖应用组件, 添加启动模块.
 */
@Component(dependencies = DemoComponent.class, modules = SplashModule.class)
@SplashScope
public interface SplashComponent {
    void inject(SplashActivity splashActivity);
}
