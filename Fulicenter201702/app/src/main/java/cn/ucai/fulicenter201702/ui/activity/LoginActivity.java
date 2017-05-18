package cn.ucai.fulicenter201702.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter201702.R;
import cn.ucai.fulicenter201702.application.FuLiCenterApplication;
import cn.ucai.fulicenter201702.application.I;
import cn.ucai.fulicenter201702.data.bean.Result;
import cn.ucai.fulicenter201702.data.bean.User;
import cn.ucai.fulicenter201702.data.local.UserDao;
import cn.ucai.fulicenter201702.data.net.IUserModel;
import cn.ucai.fulicenter201702.data.net.OnCompleteListener;
import cn.ucai.fulicenter201702.data.net.UserModel;
import cn.ucai.fulicenter201702.data.utils.L;
import cn.ucai.fulicenter201702.data.utils.MD5;
import cn.ucai.fulicenter201702.data.utils.ResultUtils;
import cn.ucai.fulicenter201702.data.utils.SharePrefrenceUtils;

/**
 * Created by clawpo on 2017/5/10.
 */

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.password)
    EditText mPassword;
    String username,password;
    ProgressDialog pd;
    IUserModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_login, R.id.btn_register, R.id.backClickArea})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), 0);
                break;
            case R.id.backClickArea:
                finish();
        }
    }

    private void initDialog(){
        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage(getString(R.string.logining));
        pd.show();
    }
    private void dismissDialog(){
        if (pd!=null && pd.isShowing()){
            pd.dismiss();
        }
    }

    private void login() {
        initDialog();
        if (checkInput()){
            model = new UserModel();
            model.login(LoginActivity.this, username, MD5.getMessageDigest(password),
                    new OnCompleteListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            if (s!=null){
                                Result<User> result = ResultUtils.getResultFromJson(s, User.class);
                                if (result!=null){
                                    if (result.getRetCode()==I.MSG_LOGIN_UNKNOW_USER){
                                        setUserNameMsg(R.string.login_fail_unknow_user);
                                    }else if (result.getRetCode() == I.MSG_LOGIN_ERROR_PASSWORD){
                                        setUserNameMsg(R.string.login_fail_error_password);
                                    }else{
                                        User user = result.getRetData();
                                        loginSuccess(user);
                                    }
                                }
                            }
                            dismissDialog();
                        }

                        @Override
                        public void onError(String error) {
                            dismissDialog();
                        }
                    });
        }else{
            dismissDialog();
        }
    }

    private void loginSuccess(User user) {
        L.e(TAG,"user="+user);
        FuLiCenterApplication.getInstance().setCurrentUser(user);
        SharePrefrenceUtils.getInstance().setUserName(username);
        UserDao dao = new UserDao(LoginActivity.this);
        dao.saveUser(user);
        setResult(RESULT_OK);
        finish();
    }

    private void setUserNameMsg(int msgId){
        mUsername.requestFocus();
        mUsername.setError(getString(msgId));
    }

    private boolean checkInput() {
        username = mUsername.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)){
            setUserNameMsg(R.string.user_name_connot_be_empty);
            return false;
        }
        if (!username.matches("[a-zA-Z]\\w{5,15}")){
            mUsername.requestFocus();
            mUsername.setError(getString(R.string.illegal_user_name));
            return false;
        }
        if (TextUtils.isEmpty(password)){
            mPassword.requestFocus();
            mPassword.setError(getString(R.string.password_connot_be_empty));
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String username = data.getStringExtra(I.User.USER_NAME);
            mUsername.setText(username);
        }
    }
}
