package cn.ucai.fulicenter201702.data.local;

import android.content.Context;

import cn.ucai.fulicenter201702.data.bean.User;

/**
 * Created by clawpo on 2017/5/10.
 */

public class UserDao {

    public UserDao(Context context) {
        DBManager.getInstance().initDB(context);
    }

    public User getUser(String username){
        return DBManager.getInstance().getUser(username);
    }

    public boolean saveUser(User user){
        return DBManager.getInstance().saveUser(user);
    }
}
