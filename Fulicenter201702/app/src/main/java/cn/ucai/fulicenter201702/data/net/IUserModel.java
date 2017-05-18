package cn.ucai.fulicenter201702.data.net;

import android.content.Context;

import java.io.File;

import cn.ucai.fulicenter201702.data.bean.CartBean;
import cn.ucai.fulicenter201702.data.bean.CollectBean;
import cn.ucai.fulicenter201702.data.bean.MessageBean;

/**
 * Created by clawpo on 2017/5/10.
 */

public interface IUserModel {
    void register(Context context, String username, String usernick, String password,
                  OnCompleteListener<String> listener);
    void login(Context context, String username, String password, OnCompleteListener<String> listener);
    void updateNick(Context context, String username, String nickname, OnCompleteListener<String> listener);
    void uploadAvatar(Context context, String username, String avatarType, File file,
                      OnCompleteListener<String> listener);
    void loadCollectsCount(Context context, String username, OnCompleteListener<MessageBean> listener);
    void addCollect(Context context, String goodsId, String username, OnCompleteListener<MessageBean> listener);
    void removeCollect(Context context, String goodsId, String username, OnCompleteListener<MessageBean> listener);
    void isCollect(Context context, String goodsId, String username, OnCompleteListener<MessageBean> listener);
    void loadCollects(Context context, String username, int pageId, int pageSize, OnCompleteListener<CollectBean[]> listener);
    void addCart(Context context, int goodsId, String username, int count, boolean isChecked, OnCompleteListener<MessageBean> listener);
    void removeCart(Context context, int cartId, OnCompleteListener<MessageBean> listener);
    void updateCart(Context context, int cartId, int count, boolean isChecked, OnCompleteListener<MessageBean> listener);
    void loadCart(Context context, String username, OnCompleteListener<CartBean[]> listener);
}
