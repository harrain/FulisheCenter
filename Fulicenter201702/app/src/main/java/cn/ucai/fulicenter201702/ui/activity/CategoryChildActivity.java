package cn.ucai.fulicenter201702.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter201702.R;
import cn.ucai.fulicenter201702.application.I;
import cn.ucai.fulicenter201702.data.bean.CategoryChildBean;
import cn.ucai.fulicenter201702.ui.fragment.NewGoodsFragment;
import cn.ucai.fulicenter201702.ui.view.CatFiterCategoryButton;

/**
 * Created by clawpo on 2017/5/9.
 */

public class CategoryChildActivity extends AppCompatActivity {
    Unbinder bind;
    NewGoodsFragment fragment;
    boolean priceAsc, addTimeAsc;
    int sortBy = I.SORT_BY_ADDTIME_DESC;
    @BindView(R.id.btn_sort_price)
    Button mBtnSortPrice;
    @BindView(R.id.btn_sort_addtime)
    Button mBtnSortAddtime;
    @BindView(R.id.cat_filter)
    CatFiterCategoryButton mCatFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_child);
        bind = ButterKnife.bind(this);
        int catId = getIntent().getIntExtra(I.CategoryChild.CAT_ID, I.CAT_ID);
        fragment = new NewGoodsFragment(catId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
        String groupName = getIntent().getStringExtra(I.CategoryGroup.NAME);
        ArrayList<CategoryChildBean> list = (ArrayList<CategoryChildBean>) getIntent().getSerializableExtra(I.CategoryChild.ID);
        mCatFilter.initView(groupName,list);
    }

    @Override
    protected void onDestroy() {
        if (bind != null) {
            bind.unbind();
        }
        if (mCatFilter!=null){
            mCatFilter.release();
        }
        super.onDestroy();
    }

    @OnClick({R.id.btn_sort_price, R.id.btn_sort_addtime, R.id.backClickArea})
    public void onClick(View view) {
        Drawable end;
        switch (view.getId()) {
            case R.id.btn_sort_price:
                priceAsc = !priceAsc;
                sortBy = priceAsc ? I.SORT_BY_PRICE_ASC : I.SORT_BY_PRICE_DESC;
                end = getDrawable(priceAsc ? R.drawable.arrow_order_up : R.drawable.arrow_order_down);
                mBtnSortPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
                break;
            case R.id.btn_sort_addtime:
                addTimeAsc = !addTimeAsc;
                sortBy = addTimeAsc ? I.SORT_BY_ADDTIME_ASC : I.SORT_BY_ADDTIME_DESC;
                end = getDrawable(addTimeAsc ? R.drawable.arrow_order_up : R.drawable.arrow_order_down);
                mBtnSortAddtime.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
                break;
            case R.id.backClickArea:
                finish();
                break;
        }
        fragment.sortGoods(sortBy);
    }
}
