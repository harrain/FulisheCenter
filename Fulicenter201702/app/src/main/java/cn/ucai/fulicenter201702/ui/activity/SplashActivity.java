package cn.ucai.fulicenter201702.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter201702.R;
import cn.ucai.fulicenter201702.application.FuLiCenterApplication;
import cn.ucai.fulicenter201702.data.bean.User;
import cn.ucai.fulicenter201702.data.local.UserDao;
import cn.ucai.fulicenter201702.data.utils.L;
import cn.ucai.fulicenter201702.data.utils.SharePrefrenceUtils;

/**
 * Created by clawpo on 2017/5/3.
 */

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    private final static int time = 5000;
    MyCountDownTimer cdt;
    @BindView(R.id.tv_skip)
    TextView mTvSkip;
    Unbinder bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bind = ButterKnife.bind(this);
        cdt = new MyCountDownTimer(time, 1000);
        cdt.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (FuLiCenterApplication.getInstance().getCurrentUser()==null){
                    String username = SharePrefrenceUtils.getInstance().getUserName();
                    L.e(TAG,"username="+username);
                    if (username!=null){
                        UserDao dao = new UserDao(SplashActivity.this);
                        User user = dao.getUser(username);
                        L.e(TAG,"user="+user);
                        if (user!=null){
                            FuLiCenterApplication.getInstance().setCurrentUser(user);
                        }
                    }
                }
            }
        }).start();
    }

    @OnClick(R.id.tv_skip) void skip(){
        cdt.cancel();
        cdt.onFinish();
    }


    class MyCountDownTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTvSkip.setText(getString(R.string.skip) + " " + millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind!=null){
            bind.unbind();
        }
    }
}
