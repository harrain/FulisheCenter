package cn.ucai.fulicenter201702.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import cn.ucai.fulicenter201702.data.bean.BoutiqueBean;
import cn.ucai.fulicenter201702.data.net.GoodsModel;
import cn.ucai.fulicenter201702.data.net.IGoodsModel;
import cn.ucai.fulicenter201702.data.net.OnCompleteListener;
import cn.ucai.fulicenter201702.data.utils.L;
import cn.ucai.fulicenter201702.data.utils.ResultUtils;
import cn.ucai.fulicenter201702.ui.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter201702.ui.view.SpaceItemDecoration;

/**
 * Created by clawpo on 2017/5/5.
 */

public class BoutiqueFragment extends Fragment {
    private static final String TAG = "BoutiqueFragment";
    @BindView(R.id.tv_refresh)
    TextView mTvRefresh;
    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;
    @BindView(R.id.tv_nomore)
    TextView mTvNomore;
    IGoodsModel model;
    Unbinder bind;
    ProgressDialog pd;
    BoutiqueAdapter adapter;
    LinearLayoutManager llm;

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
        model = new GoodsModel();
        initDialog();
        initView();
        loadData();
        setListener();
    }

    private void loadData() {
        model.loadBoutiqueData(getContext(), new OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                pd.dismiss();
                setLayoutVisibility(false);
                setListVisibility(true);

                if (result!=null){
                    ArrayList<BoutiqueBean> list = ResultUtils.array2List(result);
                    updateUI(list);
                }else{
                    if (adapter==null || adapter.getItemCount()==1){
                        setListVisibility(false);
                    }
                }
            }

            @Override
            public void onError(String error) {
                L.e("main","error="+error);
                pd.dismiss();
                setLayoutVisibility(false);
                if (adapter==null || adapter.getItemCount()==1){
                    setListVisibility(false);
                }
            }
        });
    }


    private void initDialog() {
        pd = new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.load_more));
        pd.show();
    }

    private void initView() {
        llm = new LinearLayoutManager(getContext());
        mRvGoods.setLayoutManager(llm);
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
    }

    private void setPullDownListener() {
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setLayoutVisibility(true);
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

    @OnClick(R.id.tv_nomore)
    public void reloadData(){
        pd.show();
        loadData();
    }

    private void updateUI(ArrayList<BoutiqueBean> list) {
        if (adapter==null){
            adapter = new BoutiqueAdapter(list,getContext());
            mRvGoods.setAdapter(adapter);
        }else{
            adapter.initData(list);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bind!=null){
            bind.unbind();
        }
    }
}
