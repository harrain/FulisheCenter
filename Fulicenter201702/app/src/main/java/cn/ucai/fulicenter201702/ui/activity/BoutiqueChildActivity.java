package cn.ucai.fulicenter201702.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter201702.R;
import cn.ucai.fulicenter201702.application.I;
import cn.ucai.fulicenter201702.ui.fragment.NewGoodsFragment;

/**
 * Created by clawpo on 2017/5/5.
 */

public class BoutiqueChildActivity extends AppCompatActivity {
    @BindView(R.id.backClickArea)
    LinearLayout mBackClickArea;
    @BindView(R.id.tv_common_title)
    TextView mTvCommonTitle;
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    Unbinder bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique_child);
        bind = ButterKnife.bind(this);
        int catId = getIntent().getIntExtra(I.NewAndBoutiqueGoods.CAT_ID,I.CAT_ID);
        String title = getIntent().getStringExtra(I.Boutique.TITLE);
        mTvCommonTitle.setText(title);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container,new NewGoodsFragment(catId))
                .commit();
    }

    @OnClick(R.id.backClickArea)
    public void onAreaClick(){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind!=null){
            bind.unbind();
        }
    }
}
