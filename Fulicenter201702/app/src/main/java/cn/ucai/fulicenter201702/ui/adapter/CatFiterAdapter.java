package cn.ucai.fulicenter201702.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter201702.R;
import cn.ucai.fulicenter201702.application.I;
import cn.ucai.fulicenter201702.data.bean.CategoryChildBean;
import cn.ucai.fulicenter201702.data.utils.ImageLoader;
import cn.ucai.fulicenter201702.ui.activity.CategoryChildActivity;

/**
 * Created by clawpo on 2017/5/9.
 */

public class CatFiterAdapter extends BaseAdapter {
    Context context;
    ArrayList<CategoryChildBean> list;
    String groupName;

    public CatFiterAdapter(Context context, ArrayList<CategoryChildBean> list,String groupName) {
        this.context = context;
        this.list = list;
        this.groupName = groupName;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public CategoryChildBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CatFilterViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_cat_fiter, null);
            holder = new CatFilterViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (CatFilterViewHolder) convertView.getTag();
        }
        holder.bind(position);
        return convertView;
    }

    class CatFilterViewHolder {
        @BindView(R.id.ivCategoryChildThumb)
        ImageView mIvCategoryChildThumb;
        @BindView(R.id.tvCategoryChildName)
        TextView mTvCategoryChildName;
        @BindView(R.id.layout_category_child)
        RelativeLayout mLayoutCategoryChild;

        CatFilterViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            final CategoryChildBean bean = list.get(position);
            ImageLoader.downloadImg(context,mIvCategoryChildThumb,bean.getImageUrl());
            mTvCategoryChildName.setText(bean.getName());
            mLayoutCategoryChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,CategoryChildActivity.class)
                            .putExtra(I.CategoryChild.CAT_ID,bean.getId())
                            .putExtra(I.CategoryGroup.NAME,groupName)
                            .putExtra(I.CategoryChild.ID,list));
                    ((CategoryChildActivity)context).finish();
                }
            });
        }
    }
}
