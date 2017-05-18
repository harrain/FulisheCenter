package cn.ucai.fulicenter201702.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter201702.R;
import cn.ucai.fulicenter201702.application.I;
import cn.ucai.fulicenter201702.data.bean.NewGoodsBean;
import cn.ucai.fulicenter201702.data.net.GoodsModel;
import cn.ucai.fulicenter201702.data.net.IGoodsModel;
import cn.ucai.fulicenter201702.data.net.OnCompleteListener;
import cn.ucai.fulicenter201702.data.utils.L;
import cn.ucai.fulicenter201702.data.utils.ResultUtils;
import cn.ucai.fulicenter201702.ui.adapter.GoodsAdapter;
import cn.ucai.fulicenter201702.ui.view.SpaceItemDecoration;

/**
 * Created by clawpo on 2017/5/4.
 */

public class NewGoodsFragment extends Fragment {
    private static final String TAG = "NewGoodsFragment";
    @BindView(R.id.tv_refresh)
    TextView mTvRefresh;
    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;
    @BindView(R.id.tv_nomore)
    TextView mTvNomore;
    IGoodsModel model;
    GoodsAdapter adapter;
    GridLayoutManager gm;
    int catId = I.CAT_ID;
    int pageId = 1;
    int pageSize = I.PAGE_SIZE_DEFAULT;
    Unbinder bind;
    ProgressDialog pd;

    public NewGoodsFragment(){

    }

    public NewGoodsFragment(int catId) {
        L.e(TAG,"NewGoodsFragment,catid="+catId);
        this.catId = catId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newgoods, null);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDialog();

        model = new GoodsModel();
        initView();
        loadData();
        setListener();
    }

    private void initDialog() {
        pd = new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.load_more));
        pd.show();
    }

    private void initView() {
        gm = new GridLayoutManager(getContext(), I.COLUM_NUM);
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
        mTvRefresh.setVisibility(visibility?View.VISIBLE:View.GONE);
    }

    void setListVisibility(boolean visibility){
        mTvNomore.setVisibility(visibility?View.GONE:View.VISIBLE);
        mSrl.setVisibility(visibility?View.VISIBLE:View.GONE);
    }

    public void loadData(){
        L.e(TAG,"NewGoodsFragment,catid="+catId);
        model.loadNewGoodsData(getContext(), catId, pageId, pageSize,
                new OnCompleteListener<NewGoodsBean[]>() {
                    @Override
                    public void onSuccess(NewGoodsBean[] result) {
                        pd.dismiss();
                        setLayoutVisibility(false);
                        setListVisibility(true);

                        L.e("main","result="+result);
                        if (result!=null){
                            ArrayList<NewGoodsBean> list = ResultUtils.array2List(result);
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

    private void updateUI(ArrayList<NewGoodsBean> list) {
        L.e(TAG,"updateUI....list="+list);
        L.e(TAG,"updateUI....adapter="+adapter);
        if (adapter==null){
            adapter = new GoodsAdapter(list,getContext());
            mRvGoods.setAdapter(adapter);
        }else{
            if (pageId==1){
                adapter.initData(list);
            }else {
                adapter.addData(list);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bind!=null){
            bind.unbind();
        }
    }

    public void sortGoods(int sortBy){
        if (adapter!=null){
            adapter.sortGoods(sortBy);
        }
    }
}
