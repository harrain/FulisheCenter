package cn.ucai.fulicenter201702.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter201702.R;
import cn.ucai.fulicenter201702.data.bean.CategoryChildBean;
import cn.ucai.fulicenter201702.data.bean.CategoryGroupBean;
import cn.ucai.fulicenter201702.data.net.GoodsModel;
import cn.ucai.fulicenter201702.data.net.IGoodsModel;
import cn.ucai.fulicenter201702.data.net.OnCompleteListener;
import cn.ucai.fulicenter201702.data.utils.L;
import cn.ucai.fulicenter201702.data.utils.ResultUtils;
import cn.ucai.fulicenter201702.ui.adapter.CategoryAdapter;

import static android.R.id.list;

/**
 * Created by clawpo on 2017/5/5.
 */

public class CategoryFragment extends Fragment {
    private static final String TAG = "CategoryFragment";
    @BindView(R.id.tv_nomore)
    TextView mTvNomore;
    IGoodsModel model;
    Unbinder bind;
    ProgressDialog pd;
    CategoryAdapter adapter;
    ArrayList<CategoryGroupBean> groupList = new ArrayList<>();
    List<ArrayList<CategoryChildBean>> childList = new ArrayList<>();
    int groupCount = 0;
    @BindView(R.id.elv_category)
    ExpandableListView mElvCategory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, null);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new GoodsModel();
        initDialog();
        loadData();
    }

    private void loadData() {
        model.loadCreategoryGorup(getContext(), new OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                L.e(TAG, "result=" + result);

                if (result != null) {
                    groupList = ResultUtils.array2List(result);
                    if (childList!=null){
                        childList.clear();
                    }
                    for (int i = 0; i < groupList.size(); i++) {
                        childList.add(new ArrayList<CategoryChildBean>());
                        loadChildData(groupList.get(i).getId(),i);
                    }
                } else {
                    setListVisibility(false);
                }
            }

            @Override
            public void onError(String error) {
                L.e("main", "error=" + error);
                pd.dismiss();
                setListVisibility(false);
            }
        });
    }

    private void loadChildData(int parentId,final int index) {
        model.loadCreategoryChild(getContext(), parentId, new OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                groupCount++;
                L.e(TAG, "result=" + result);

                if (result != null) {
                    ArrayList<CategoryChildBean> list = ResultUtils.array2List(result);
                    childList.set(index,list);
                } else {

                }
                if (groupCount == groupList.size()) {
                    updateUI();
                    pd.dismiss();
                    setListVisibility(true);
                }
            }

            @Override
            public void onError(String error) {
                L.e("main", "error=" + error);
                groupCount++;
                if (groupCount == groupList.size()) {
                    pd.dismiss();
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

    void setListVisibility(boolean visibility) {
        mTvNomore.setVisibility(visibility ? View.GONE : View.VISIBLE);
        mElvCategory.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.tv_nomore)
    public void reloadData() {
        pd.show();
        loadData();
    }

    private void updateUI() {
        L.e(TAG, "updateUI....list=" + list);
        L.e(TAG, "updateUI....adapter=" + adapter);
        if (adapter == null) {
            adapter = new CategoryAdapter(groupList,childList,getContext());
            mElvCategory.setAdapter(adapter);
        } else {
//            adapter.initData(list);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bind != null) {
            bind.unbind();
        }
    }
}
