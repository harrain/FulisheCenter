package cn.ucai.fulicenter201702.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import cn.ucai.fulicenter201702.data.utils.CommonUtils;
import cn.ucai.fulicenter201702.data.utils.ResultUtils;

/**
 * Created by clawpo on 2017/5/11.
 */

public class UpdateNickActivity extends AppCompatActivity {
    @BindView(R.id.backClickArea)
    LinearLayout mBackClickArea;
    @BindView(R.id.tv_common_title)
    TextView mTvCommonTitle;
    @BindView(R.id.et_update_user_name)
    EditText mEtUpdateUserName;
    IUserModel model;
    User user;
    ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nick);
        ButterKnife.bind(this);
        initView();
        initData();
        model = new UserModel();
    }

    private void initView() {
        mTvCommonTitle.setText(R.string.update_user_nick);
    }

    private void initData() {
        user = FuLiCenterApplication.getInstance().getCurrentUser();
        if (user!=null){
            mEtUpdateUserName.setText(user.getMuserNick());
            mEtUpdateUserName.selectAll();
        } else{
            finish();
        }
    }

    @OnClick(R.id.backClickArea)
    public void onBackClick(){
        finish();
    }

    private void initDialog(){
        pd = new ProgressDialog(UpdateNickActivity.this);
        pd.setMessage(getString(R.string.update_user_nick));
        pd.show();
    }

    private void dismissDialog(){
        if (pd!=null && pd.isShowing()){
            pd.dismiss();
        }
    }


    @OnClick(R.id.btn_save)
    public void onUpdateNick(){
        initDialog();
        String newNick = mEtUpdateUserName.getText().toString().trim();
        if (checkInput()) {
            model.updateNick(UpdateNickActivity.this, user.getMuserName(), newNick,
                    new OnCompleteListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            if (s != null) {
                                Result<User> result = ResultUtils.getResultFromJson(s, User.class);
                                if (result != null) {
                                    if (result.getRetCode() == I.MSG_USER_SAME_NICK) {
                                        CommonUtils.showLongToast(R.string.update_nick_fail_unmodify);
                                    } else if (result.getRetCode() == I.MSG_USER_UPDATE_NICK_FAIL) {
                                        CommonUtils.showLongToast(R.string.update_fail);
                                    } else {
                                        updateSuccess(result.getRetData());
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

    private boolean checkInput() {
        String newNick = mEtUpdateUserName.getText().toString().trim();
        if (TextUtils.isEmpty(newNick)){
            CommonUtils.showLongToast(R.string.nick_name_connot_be_empty);
            return false;
        } else if(newNick.equals(user.getMuserNick())){
            CommonUtils.showLongToast(R.string.update_nick_fail_unmodify);
            return false;
        }
        return true;
    }

    private void updateSuccess(User user) {
        CommonUtils.showLongToast(R.string.update_user_nick_success);
        UserDao dao = new UserDao(UpdateNickActivity.this);
        dao.saveUser(user);
        FuLiCenterApplication.getInstance().setCurrentUser(user);
        setResult(RESULT_OK);
        finish();
    }
}
