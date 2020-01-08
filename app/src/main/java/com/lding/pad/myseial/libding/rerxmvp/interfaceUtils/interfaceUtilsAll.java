package com.lding.pad.myseial.libding.rerxmvp.interfaceUtils;
import android.content.Context;

import com.lding.pad.myseial.libding.entity.GetListRsp;
import com.lding.pad.myseial.libding.rerxmvp.base.OnHttpCallBack;

public class interfaceUtilsAll {

    /**
     * V视图层
     */
    public   interface ILoginView {
        Context getCurContext();//获取上下文对象,用于保存数据等

        void showProgress();//可以显示进度条

        void hideProgress();//可以隐藏进度条

        void showInfo(String info);//提示用户,提升友好交互

        void showErrorMsg(String msg);//发生错误就显示错误信息

        void toMain();//跳转到主页面

        void toRegister();//跳转到注册页面

        GetListRsp getUserLoginInfo();//获取用户登录信息
    }

    /**
     * P视图与逻辑处理的连接层
     */
    public   interface ILoginPresenter {
        void login();//唯一的桥梁就是登录了
    }

    /**
     * 逻辑处理层
     */
    public   interface ILoginModel {
        void login(GetListRsp getListRsp, OnHttpCallBack<GetListRsp> callBack);//登录

        void saveUserInfo(Context context, GetListRsp getListRsp,String token);//登录成功就保存用户信息
    }
}
