package cn.ucai.fulicenter201702.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter201702.R;
import cn.ucai.fulicenter201702.application.I;
import cn.ucai.fulicenter201702.data.bean.Result;
import cn.ucai.fulicenter201702.data.bean.User;
import cn.ucai.fulicenter201702.data.net.IUserModel;
import cn.ucai.fulicenter201702.data.net.OnCompleteListener;
import cn.ucai.fulicenter201702.data.net.UserModel;
import cn.ucai.fulicenter201702.data.utils.CommonUtils;
import cn.ucai.fulicenter201702.data.utils.L;
import cn.ucai.fulicenter201702.data.utils.MD5;
import cn.ucai.fulicenter201702.data.utils.ResultUtils;

/**
 * Created by clawpo on 2017/5/10.
 */

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.nick)
    EditText mNick;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.confirm_password)
    EditText mConfirmPassword;
    String username, usernick, password;
    IUserModel model;
    ProgressDialog pd;
    @BindView(R.id.tv_common_title)
    TextView mTvCommonTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mTvCommonTitle.setText(R.string.register);
    }

    @OnClick({R.id.backClickArea, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backClickArea:
                finish();
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    private void register() {
        initDiglog();
        if (checkInput()) {
            model = new UserModel();
            model.register(RegisterActivity.this, username, usernick, MD5.getMessageDigest(password),
                    new OnCompleteListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            L.e(TAG, "s=" + s);
                            if (s != null) {
                                Result result = ResultUtils.getResultFromJson(s, User.class);
                                if (result != null) {
                                    if (result.getRetCode() == I.MSG_REGISTER_USERNAME_EXISTS) {
                                        mUsername.requestFocus();
                                        mUsername.setError(getString(R.string.register_fail_exists));
                                    } else if (result.getRetCode() == I.MSG_REGISTER_FAIL) {
                                        mUsername.requestFocus();
                                        mUsername.setError(getString(R.string.register_fail));
                                    } else {
                                        registerSuccess();
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
        } else {
            dismissDialog();
        }
    }

    private void initDiglog() {
        pd = new ProgressDialog(RegisterActivity.this);
        pd.setMessage(getString(R.string.registering));
        pd.show();
    }

    private void dismissDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    private void registerSuccess() {
        CommonUtils.showLongToast(R.string.register_success);
        setResult(RESULT_OK, new Intent().putExtra(I.User.USER_NAME, username));
        finish();
    }

    private boolean checkInput() {
        username = mUsername.getText().toString().trim();
        usernick = mNick.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        String cpwd = mConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            mUsername.requestFocus();
            mUsername.setError(getString(R.string.user_name_connot_be_empty));
            return false;
        }
        if (!username.matches("[a-zA-Z]\\w{5,15}")) {
            mUsername.requestFocus();
            mUsername.setError(getString(R.string.illegal_user_name));
            return false;
        }
        if (TextUtils.isEmpty(usernick)) {
            mNick.requestFocus();
            mNick.setError(getString(R.string.nick_name_connot_be_empty));
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            mPassword.requestFocus();
            mPassword.setError(getString(R.string.password_connot_be_empty));
            return false;
        }
        if (TextUtils.isEmpty(cpwd)) {
            mConfirmPassword.requestFocus();
            mConfirmPassword.setError(getString(R.string.confirm_password_connot_be_empty));
            return false;
        }
        if (!password.equals(cpwd)) {
            mConfirmPassword.requestFocus();
            mConfirmPassword.setError(getString(R.string.two_input_password));
            return false;
        }
        return true;
    }
}
