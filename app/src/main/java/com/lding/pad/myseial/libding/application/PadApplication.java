package com.lding.pad.myseial.libding.application;

import android.app.Application;
import android.content.Context;

import com.lding.pad.myseial.libding.utils.LibLoader;


/**
 * Created by tushuangxi 2019.1.26
 */
public class PadApplication extends Application {
    private static Context mContext;

    private static PadApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        //获取context
        mContext = getApplicationContext();

        mInstance = this;
        LibLoader.init(this);
    }
    //创建一个静态的方法，以便获取context对象
    public static Context getContext(){
        return mContext;
    }

}
