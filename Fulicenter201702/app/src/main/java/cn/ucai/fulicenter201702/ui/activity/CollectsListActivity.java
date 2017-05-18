package cn.ucai.fulicenter201702.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter201702.R;
import cn.ucai.fulicenter201702.application.FuLiCenterApplication;
import cn.ucai.fulicenter201702.application.I;
import cn.ucai.fulicenter201702.data.bean.CollectBean;
import cn.ucai.fulicenter201702.data.bean.User;
import cn.ucai.fulicenter201702.data.net.IUserModel;
import cn.ucai.fulicenter201702.data.net.OnCompleteListener;
import cn.ucai.fulicenter201702.data.net.UserModel;
import cn.ucai.fulicenter201702.data.utils.L;
import cn.ucai.fulicenter201702.data.utils.ResultUtils;
import cn.ucai.fulicenter201702.ui.adapter.CollectAdapter;
import cn.ucai.fulicenter201702.ui.view.SpaceItemDecoration;

/**
 * Created by clawpo on 2017/5/15.
 */

public class CollectsListActivity extends AppCompatActivity {
    private static final String TAG = "CollectsListActivity";
    @BindView(R.id.tv_common_title)
    TextView mTvCommonTitle;
    @BindView(R.id.tv_refresh)
    TextView mTvRefresh;
    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;
    @BindView(R.id.tv_nomore)
    TextView mTvNomore;
    IUserModel model;
    CollectAdapter adapter;
    GridLayoutManager gm;
    int catId = I.CAT_ID;
    int pageId = 1;
    int pageSize = I.PAGE_SIZE_DEFAULT;
    Unbinder bind;
    ProgressDialog pd;
    ArrayList<CollectBean> collectList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collects_list);
        bind = ButterKnife.bind(this);initDialog();

        model = new UserModel();
        initView();
        loadData();
        setListener();
    }

    private void initDialog() {
        pd = new ProgressDialog(CollectsListActivity.this);
        pd.setMessage(getString(R.string.load_more));
        pd.show();
    }

    private void initView() {
        gm = new GridLayoutManager(CollectsListActivity.this, I.COLUM_NUM);
        gm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
//                L.e(TAG,"getSpanSize="+position);
                if (adapter==null || position==adapter.getItemCount()-1){
                    return I.COLUM_NUM;
                }
                return 1;
            }
        });
        mRvGoods.setLayoutManager(gm);
        mSrl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_yellow)
        );
        mRvGoods.addItemDecoration(new SpaceItemDecoration(12));
        mTvCommonTitle.setText(R.string.collect_title);
    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullUpListener() {
        mRvGoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItemPosition = gm.findLastVisibleItemPosition();
                if (adapter!=null
                        && adapter.getItemCount()-1==lastVisibleItemPosition
                        && newState == RecyclerView.SCROLL_STATE_IDLE
                        && adapter.isMore()){
                    pageId++;
                    loadData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void setPullDownListener() {
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setLayoutVisibility(true);
                pageId=1;
                loadData();
            }
        });
    }

    void setLayoutVisibility(boolean visibility){
        mSrl.setRefreshing(visibility);
        mTvRefresh.setVisibility(visibility? View.VISIBLE:View.GONE);
    }

    void setListVisibility(boolean visibility){
        mTvNomore.setVisibility(visibility?View.GONE:View.VISIBLE);
        mSrl.setVisibility(visibility?View.VISIBLE:View.GONE);
    }

    public void loadData(){
        User user = FuLiCenterApplication.getInstance().getCurrentUser();
        if (user==null){
            finish();
            return;
        }
        L.e(TAG,"NewGoodsFragment,catid="+catId);
        model.loadCollects(CollectsListActivity.this, user.getMuserName(), pageId, pageSize,
                new OnCompleteListener<CollectBean[]>() {
                    @Override
                    public void onSuccess(CollectBean[] result) {
                        pd.dismiss();
                        setLayoutVisibility(false);
                        setListVisibility(true);

                        L.e("main","result="+result);
                        if (result!=null){
                            ArrayList<CollectBean> list = ResultUtils.array2List(result);
                            updateUI(list);
                        }else{
                            if (adapter==null || adapter.getItemCount()==1){
                                setListVisibility(false);
                            }
                        }
                        if (adapter!=null) {
                            if (result != null && result.length == pageSize) {
                                adapter.setMore(true);
                            } else {
                                adapter.setMore(false);
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        L.e("main","error="+error);
                        pd.dismiss();
                        setLayoutVisibility(false);
                        L.e(TAG,"adapter="+adapter);
                        if (adapter==null || adapter.getItemCount()==1){
                            setListVisibility(false);
                        }
                    }
                });
    }

    @OnClick(R.id.tv_nomore)
    public void reloadData(){
        pd.show();
        loadData();
    }

    private void updateUI(ArrayList<CollectBean> list) {
        L.e(TAG,"updateUI....list="+list);
        L.e(TAG,"updateUI....adapter="+adapter);
        if (adapter==null){
            collectList = new ArrayList<>();
            collectList.addAll(list);
            adapter = new CollectAdapter(collectList,CollectsListActivity.this);
            mRvGoods.setAdapter(adapter);
            adapter.setModel(model);
        }else{
            if (pageId==1){
                collectList.clear();
                collectList.addAll(list);
                adapter.initData(list);
            }else {
                collectList.addAll(list);
                adapter.addData(list);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind!=null){
            bind.unbind();
        }
    }

    @OnClick(R.id.backClickArea)
    public void onClick() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == I.REQUEST_CODE_GO_DETAIL && resultCode == RESULT_OK){
            int goodsId = data.getIntExtra(I.Goods.KEY_GOODS_ID, 0);
            boolean isCollect = data.getBooleanExtra(I.Goods.KEY_IS_COLLECT,true);
            L.e(TAG,"onActivityResult,goodsId="+goodsId+",isCollect="+isCollect);
            if (!isCollect){
                collectList.remove(new CollectBean(goodsId));
                adapter.notifyDataSetChanged();
            }
        }
    }
}
