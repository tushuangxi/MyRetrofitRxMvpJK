package com.lding.pad.myseial.libding.rerxmvp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.lding.pad.myseial.R;
import com.lding.pad.myseial.libding.entity.GetListRsp;
import com.lding.pad.myseial.libding.rerxmvp.base.BaseActivity;
import com.lding.pad.myseial.libding.rerxmvp.interfaceUtils.interfaceUtilsAll;
import com.lding.pad.myseial.libding.rerxmvp.presenter.LoginPresenter;
import com.lding.pad.myseial.libding.utils.ToastUtils;
import com.lding.pad.myseial.libding.widget.loading.LoadingActivityDialog;
import com.lding.pad.myseial.library.gifloadinglibrary.ui.GifActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录页面
 * (看例子之前看一遍下面直白的解释,看完之后再看一遍就更明白MVP模式了)
 * --------M层   对P层传递过来的userInfo进行登录(网络请求)判断,处理完成之后将处理结果回调给P层
 * --------P层   传递完数据给M层处理之后,实例化回调对象,成功了就通知V层登录成功,失败了就通知V层显示错误信息
 * --------V层   负责响应用户的交互(获取数据---->提示操作结果)
 */
public class LoginActivity extends BaseActivity implements interfaceUtilsAll.ILoginView {


    @BindView(R.id.et_login_userName)
    EditText etLoginUserName;
    @BindView(R.id.et_login_pwd)
    EditText etLoginPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    LoadingActivityDialog loadDialogView;

    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mLoginPresenter = new LoginPresenter(this);
    }


    @Override
    public Context getCurContext() {
        return mActivity;
    }

    @Override
    public void showProgress() {
        if (loadDialogView==null){
            loadDialogView = LoadingActivityDialog.createDialog(LoginActivity.this);
            loadDialogView.show();
        }

    }

    @Override
    public void hideProgress() {
        if (loadDialogView!=null){
            loadDialogView.dismiss();
        }

    }

    @Override
    public void showInfo(String info) {
        ToastUtils.showToast(mActivity, info);
    }

    @Override
    public void showErrorMsg(String msg) {
        ToastUtils.showToast(mActivity, msg);
    }

    @Override
    public void toMain() {
        Intent intent = new Intent(LoginActivity.this, GifActivity.class);
        startActivity(intent);
    }

    @Override
    public void toRegister() {

    }

    @Override
    public GetListRsp getUserLoginInfo() {

        return new GetListRsp();
    }


    public void onRegister(View view) {
        toRegister();
    }

    @OnClick({ R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_login:
                mLoginPresenter.login();
                break;
        }
    }
}
