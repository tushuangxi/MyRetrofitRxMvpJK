package com.lding.pad.myseial.libding.rerxmvp.model;

import android.content.Context;
import android.util.Log;

import com.lding.pad.myseial.libding.entity.GetListRsp;
import com.lding.pad.myseial.libding.http.ApiService;
import com.lding.pad.myseial.libding.http.ServiceMapParams;
import com.lding.pad.myseial.libding.http.manager.RetrofitUtils;
import com.lding.pad.myseial.libding.rerxmvp.base.OnHttpCallBack;
import com.lding.pad.myseial.libding.rerxmvp.interfaceUtils.interfaceUtilsAll;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginModel implements interfaceUtilsAll.ILoginModel {
    /**
     * 登录的具体业务处理--------网络请求都在这里做喽
     * --------V层   负责响应用户的交互(获取数据---->提示操作结果)
     * --------P层   传递完数据给M层处理之后,实例化回调对象,成功了就通知V层登录成功,失败了就通知V层显示错误信息
     * --------M层   对P层传递过来的userInfo进行登录(网络请求)判断,处理完成之后将处理结果回调给P层
     *
     * @param userInfo P层传递过来的数据
     * @param callBack P层传递过来的回调对象----------说白了就是成功还是失败了,你总的告诉我一声,我好通知V层来处理[这里可以不用回调,在代码中使用EventBus或者传递一个Handler过来也可以,不建议这样操作]
     */

    @Override
    public void login(final GetListRsp userInfo, final OnHttpCallBack<GetListRsp> callBack) {
        //登录的网络请求
        RetrofitUtils.newInstence()//实例化Retrofit对象
                .create(ApiService.class)//创建Rxjava---->LoginService对象
                .userLogin(ServiceMapParams.getGetListRspMapParams())//调用登录的接口
                .subscribeOn(Schedulers.newThread())//在新线程中执行登录请求
                .observeOn(AndroidSchedulers.mainThread())//在主线程中执行
                .subscribe(new Subscriber<GetListRsp>() {//网络(登录)请求回调
                    @Override
                    public void onCompleted() {
                        //完成的时候调用
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("",e.getMessage() + "--");
                        e.printStackTrace();
                        //失败的时候调用-----一下可以忽略 直接 callBack.onFaild("请求失败");
                        if (e instanceof HttpException) {
                            HttpException httpException = (HttpException) e;
                            //httpException.response().errorBody().string()
                            int code = httpException.code();
                            if (code == 500 || code == 404) {
                                callBack.onFaild("服务器出错");
                            }
                        } else if (e instanceof ConnectException) {
                            callBack.onFaild("网络断开,请打开网络!");
                        } else if (e instanceof SocketTimeoutException) {
                            callBack.onFaild("网络连接超时!!");
                        } else {
                            callBack.onFaild("发生未知错误" + e.getMessage());
                            Log.e("Myloy", e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(GetListRsp getListRsp) {
                        callBack.onSuccessful(getListRsp);//登录成功------获取完数据,返回给P---P获取到数据之后就将数据交回给V

//                        if (loginResultUserHttpResult.getResultCode() == 0) {
//                            callBack.onSuccessful(loginResultUserHttpResult.getData());//登录成功------获取完数据,返回给P---P获取到数据之后就将数据交回给V
//                        } else {
//                            callBack.onFaild("用户名或密码错误!");//登录失败
//                        }
                    }
                });
    }

    /**
     * 将信息保存在sp中
     */
    @Override
    public void saveUserInfo(Context context, GetListRsp getListRsp, String token) {
        context.getSharedPreferences("appName", Context.MODE_PRIVATE).edit()
                .putString("userName",  getListRsp.toString())
                .putString("token", token)
                .commit();
    }
}
