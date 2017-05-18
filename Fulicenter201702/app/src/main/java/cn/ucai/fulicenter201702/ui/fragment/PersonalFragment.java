package cn.ucai.fulicenter201702.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter201702.R;
import cn.ucai.fulicenter201702.application.FuLiCenterApplication;
import cn.ucai.fulicenter201702.data.bean.MessageBean;
import cn.ucai.fulicenter201702.data.bean.User;
import cn.ucai.fulicenter201702.data.net.IUserModel;
import cn.ucai.fulicenter201702.data.net.OnCompleteListener;
import cn.ucai.fulicenter201702.data.net.UserModel;
import cn.ucai.fulicenter201702.data.utils.ImageLoader;
import cn.ucai.fulicenter201702.ui.activity.CollectsListActivity;
import cn.ucai.fulicenter201702.ui.activity.SettingsActivity;

/**
 * Created by clawpo on 2017/5/11.
 */

public class PersonalFragment extends Fragment {
    User user;
    @BindView(R.id.iv_user_avatar)
    ImageView mIvUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.center_user_order_lis)
    GridView mCenterUserOrderLis;
    IUserModel model;
    @BindView(R.id.tv_collect_count)
    TextView mTvCollectCount;
    int collecCount = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_personal, null);
        ButterKnife.bind(this, view);
        initOrderList();
        model = new UserModel();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        mTvCollectCount.setText(String.valueOf(collecCount));
        user = FuLiCenterApplication.getInstance().getCurrentUser();
        if (user != null) {
            mTvUserName.setText(user.getMuserNick());
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), getContext(), mIvUserAvatar);
            initCollectCount();
        }
    }

    private void initCollectCount() {
        model.loadCollectsCount(getContext(), user.getMuserName(),
                new OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            collecCount = Integer.parseInt(result.getMsg());
                        }else{
                            collecCount = 0;
                        }
                        mTvCollectCount.setText(String.valueOf(collecCount));

                    }

                    @Override
                    public void onError(String error) {
                        collecCount = 0;
                        mTvCollectCount.setText(String.valueOf(collecCount));
                    }
                });
    }

    private void initOrderList() {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> order1 = new HashMap<String, Object>();
        order1.put("order", R.drawable.order_list1);
        data.add(order1);
        HashMap<String, Object> order2 = new HashMap<String, Object>();
        order2.put("order", R.drawable.order_list2);
        data.add(order2);
        HashMap<String, Object> order3 = new HashMap<String, Object>();
        order3.put("order", R.drawable.order_list3);
        data.add(order3);
        HashMap<String, Object> order4 = new HashMap<String, Object>();
        order4.put("order", R.drawable.order_list4);
        data.add(order4);
        HashMap<String, Object> order5 = new HashMap<String, Object>();
        order5.put("order", R.drawable.order_list5);
        data.add(order5);
        SimpleAdapter adapter = new SimpleAdapter(getContext(), data, R.layout.simple_adapter,
                new String[]{"order"}, new int[]{R.id.iv_order});
        mCenterUserOrderLis.setAdapter(adapter);
    }

    @OnClick({R.id.center_top, R.id.center_user_info})
    public void onSettings(View view) {
        startActivity(new Intent(getContext(), SettingsActivity.class));
    }

    @OnClick(R.id.layout_center_collect)
    public void onCollectLayoutClick(){
        startActivity(new Intent(getContext(),CollectsListActivity.class));
    }
}
