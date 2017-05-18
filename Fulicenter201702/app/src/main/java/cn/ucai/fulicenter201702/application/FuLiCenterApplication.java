package cn.ucai.fulicenter201702.application;

import android.app.Application;

import cn.sharesdk.framework.ShareSDK;
import cn.ucai.fulicenter201702.data.bean.User;

/**
 * Created by clawpo on 2017/5/3.
 */

public class FuLiCenterApplication extends Application {
    private static FuLiCenterApplication instance;
    private User currentUser = null;
    private boolean isLogined = false;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ShareSDK.initSDK(this);
    }

    public static FuLiCenterApplication getInstance() {
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        setLogined(currentUser==null?false:true);
    }

    public boolean isLogined() {
        return isLogined;
    }

    public void setLogined(boolean logined) {
        isLogined = logined;
    }
}
