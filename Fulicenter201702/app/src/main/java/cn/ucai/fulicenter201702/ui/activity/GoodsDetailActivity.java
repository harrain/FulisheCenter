package cn.ucai.fulicenter201702.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.ucai.fulicenter201702.R;
import cn.ucai.fulicenter201702.application.FuLiCenterApplication;
import cn.ucai.fulicenter201702.application.I;
import cn.ucai.fulicenter201702.data.bean.AlbumsBean;
import cn.ucai.fulicenter201702.data.bean.GoodsDetailsBean;
import cn.ucai.fulicenter201702.data.bean.MessageBean;
import cn.ucai.fulicenter201702.data.bean.PropertiesBean;
import cn.ucai.fulicenter201702.data.bean.User;
import cn.ucai.fulicenter201702.data.net.GoodsModel;
import cn.ucai.fulicenter201702.data.net.IGoodsModel;
import cn.ucai.fulicenter201702.data.net.IUserModel;
import cn.ucai.fulicenter201702.data.net.OnCompleteListener;
import cn.ucai.fulicenter201702.data.net.UserModel;
import cn.ucai.fulicenter201702.data.utils.CommonUtils;
import cn.ucai.fulicenter201702.data.utils.L;
import cn.ucai.fulicenter201702.ui.view.FlowIndicator;
import cn.ucai.fulicenter201702.ui.view.SlideAutoLoopView;

/**
 * Created by clawpo on 2017/5/8.
 */

public class GoodsDetailActivity extends AppCompatActivity {
    private static final String TAG = "GoodsDetailActivity";
    int goodsId;
    IGoodsModel model;
    @BindView(R.id.tv_good_name_english)
    TextView mTvGoodNameEnglish;
    @BindView(R.id.tv_good_name)
    TextView mTvGoodName;
    @BindView(R.id.tv_good_price_shop)
    TextView mTvGoodPriceShop;
    @BindView(R.id.tv_good_price_current)
    TextView mTvGoodPriceCurrent;
    @BindView(R.id.salv)
    SlideAutoLoopView mSalv;
    @BindView(R.id.indicator)
    FlowIndicator mIndicator;
    @BindView(R.id.wv_good_brief)
    WebView mWvGoodBrief;
    Unbinder bind;
    User user;
    IUserModel userModel;
    boolean isCollect = false;
    @BindView(R.id.iv_good_collect)
    ImageView mIvGoodCollect;
    GoodsDetailsBean mGoodsDetailsBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        bind = ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e(TAG, "goodsId=" + goodsId);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
        if (mSalv != null) {
            mSalv.stopPlayLoop();
        }
    }

    private void initData() {
        if (goodsId == 0) {
            finish();
        } else {
            model = new GoodsModel();
            userModel = new UserModel();
            loadData();
        }
    }

    private void loadData() {
        model.loadGoodsDetail(GoodsDetailActivity.this, goodsId,
                new OnCompleteListener<GoodsDetailsBean>() {
                    @Override
                    public void onSuccess(GoodsDetailsBean result) {
                        L.e(TAG, "result=" + result);
                        if (result != null) {
                            mGoodsDetailsBean = result;
                            showData(result);
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });

        loadCollectStatus();
    }

    private void loadCollectStatus() {
        user = FuLiCenterApplication.getInstance().getCurrentUser();
        if (user != null) {
            userModel.isCollect(GoodsDetailActivity.this, String.valueOf(goodsId), user.getMuserName(),
                    new OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            isCollect = result != null && result.isSuccess() ? true : false;
                            updateCollectUI();
                        }

                        @Override
                        public void onError(String error) {
                            isCollect = false;
                            updateCollectUI();
                        }
                    });
        }
    }

    private void updateCollectUI() {
        mIvGoodCollect.setImageResource(isCollect?R.mipmap.bg_collect_out:R.mipmap.bg_collect_in);
    }

    private void showData(GoodsDetailsBean bean) {
        mTvGoodNameEnglish.setText(bean.getGoodsEnglishName());
        mTvGoodName.setText(bean.getGoodsName());
        mTvGoodPriceCurrent.setText(bean.getCurrencyPrice());
        mTvGoodPriceShop.setText(bean.getShopPrice());
        mSalv.startPlayLoop(mIndicator, getAlbumImgUrl(bean), getAlbumImgCount(bean));
        mWvGoodBrief.loadDataWithBaseURL(null, bean.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
    }

    private String[] getAlbumImgUrl(GoodsDetailsBean bean) {
        AlbumsBean[] imgs = getAlbumImg(bean);
        if (imgs != null) {
            String[] urls = new String[imgs.length];
            for (int i = 0; i < imgs.length; i++) {
                urls[i] = imgs[i].getImgUrl();
            }
            return urls;
        }
        return null;
    }

    private int getAlbumImgCount(GoodsDetailsBean bean) {
        AlbumsBean[] imgs = getAlbumImg(bean);
        if (imgs != null) {
            return imgs.length;
        }
        return 0;
    }

    private AlbumsBean[] getAlbumImg(GoodsDetailsBean bean) {
        if (bean.getProperties() != null && bean.getProperties().length > 0) {
            PropertiesBean propertiesBean = bean.getProperties()[0];
            if (propertiesBean != null && propertiesBean.getAlbums() != null) {
                return propertiesBean.getAlbums();
            }
        }
        return null;
    }

    @OnClick(R.id.backClickArea)
    public void onClick() {
        setResult(RESULT_OK,new Intent().putExtra(I.Goods.KEY_GOODS_ID,goodsId)
        .putExtra(I.Goods.KEY_IS_COLLECT,isCollect));

        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK,new Intent().putExtra(I.Goods.KEY_GOODS_ID,goodsId)
                .putExtra(I.Goods.KEY_IS_COLLECT,isCollect));
        super.onBackPressed();
    }

    @OnClick(R.id.iv_good_collect)
    public void onCollectClick(){
        if (user==null){
            startActivityForResult(new Intent(GoodsDetailActivity.this,LoginActivity.class),0);
        }else{
            if (isCollect){
                userModel.removeCollect(GoodsDetailActivity.this,String.valueOf(goodsId),
                        user.getMuserName(),mListener);
            }else{
                userModel.addCollect(GoodsDetailActivity.this,String.valueOf(goodsId),
                        user.getMuserName(),mListener);
            }
        }
    }

    OnCompleteListener<MessageBean> mListener = new OnCompleteListener<MessageBean>() {
        @Override
        public void onSuccess(MessageBean result) {
            isCollect = !isCollect;
            updateCollectUI();

//            if (result!=null && result.getMsg()!=""){
                CommonUtils.showLongToast(result.getMsg());
//            }
        }

        @Override
        public void onError(String error) {
            CommonUtils.showLongToast(error);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            loadCollectStatus();
        }
    }

    @OnClick(R.id.iv_good_cart)
    public void onCartClick(){
        if (FuLiCenterApplication.getInstance().isLogined()){
            addCart();
        }else{
            startActivityForResult(new Intent(GoodsDetailActivity.this,LoginActivity.class),0);
        }
    }

    private void addCart() {
        userModel.addCart(GoodsDetailActivity.this, goodsId, user.getMuserName(),
                I.ADD_CART_COUNT_DEFAULT, false, new OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result!=null && result.isSuccess()){
                            CommonUtils.showLongToast(R.string.add_goods_success);
                            sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART)
                            .putExtra(I.Cart.class.toString(),mGoodsDetailsBean));
                        }else{
                            CommonUtils.showLongToast(R.string.add_goods_fail);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        CommonUtils.showLongToast(R.string.add_goods_fail);
                    }
                });
    }

    @OnClick(R.id.iv_good_share)
    public void shareSocial(){
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle(mGoodsDetailsBean.getGoodsName());
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(mGoodsDetailsBean.getShareUrl());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mGoodsDetailsBean.getGoodsBrief());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(I.DOWNLOAD_IMG_URL + mGoodsDetailsBean.getGoodsImg());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("哇这么好，给我来一打!!!");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
}
