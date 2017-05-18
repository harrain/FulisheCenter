package cn.ucai.fulicenter201702.data.net;

import android.content.Context;

import cn.ucai.fulicenter201702.application.I;
import cn.ucai.fulicenter201702.data.bean.BoutiqueBean;
import cn.ucai.fulicenter201702.data.bean.CategoryChildBean;
import cn.ucai.fulicenter201702.data.bean.CategoryGroupBean;
import cn.ucai.fulicenter201702.data.bean.GoodsDetailsBean;
import cn.ucai.fulicenter201702.data.bean.NewGoodsBean;
import cn.ucai.fulicenter201702.data.utils.OkHttpUtils;

/**
 * Created by clawpo on 2017/5/4.
 */

public class GoodsModel implements IGoodsModel {
    @Override
    public void loadNewGoodsData(Context context, int catId, int pageId, int pageSize,
                                 OnCompleteListener<NewGoodsBean[]> listener) {
        String requestUrl = I.REQUEST_FIND_NEW_BOUTIQUE_GOODS;
        if (catId>0){
            requestUrl = I.REQUEST_FIND_GOODS_DETAILS;
        }
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(requestUrl)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID, String.valueOf(catId))
                .addParam(I.PAGE_ID, String.valueOf(pageId))
                .addParam(I.PAGE_SIZE, String.valueOf(pageSize))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }

    @Override
    public void loadBoutiqueData(Context context, OnCompleteListener<BoutiqueBean[]> listener) {
        OkHttpUtils<BoutiqueBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .targetClass(BoutiqueBean[].class)
                .execute(listener);
    }

    @Override
    public void loadGoodsDetail(Context context, int goodsId, OnCompleteListener<GoodsDetailsBean> listener) {
        OkHttpUtils<GoodsDetailsBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.GoodsDetails.KEY_GOODS_ID,String.valueOf(goodsId))
                .targetClass(GoodsDetailsBean.class)
                .execute(listener);
    }

    @Override
    public void loadCreategoryGorup(Context context, OnCompleteListener<CategoryGroupBean[]> listener) {
        OkHttpUtils<CategoryGroupBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(listener);
    }

    @Override
    public void loadCreategoryChild(Context context, int parentId, OnCompleteListener<CategoryChildBean[]> listener) {
        OkHttpUtils<CategoryChildBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID,String.valueOf(parentId))
                .targetClass(CategoryChildBean[].class)
                .execute(listener);
    }
}
