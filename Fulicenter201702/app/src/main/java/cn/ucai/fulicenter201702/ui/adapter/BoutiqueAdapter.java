package cn.ucai.fulicenter201702.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter201702.R;
import cn.ucai.fulicenter201702.application.I;
import cn.ucai.fulicenter201702.data.bean.BoutiqueBean;
import cn.ucai.fulicenter201702.data.utils.ImageLoader;
import cn.ucai.fulicenter201702.ui.activity.BoutiqueChildActivity;

/**
 * Created by clawpo on 2017/5/5.
 */

public class BoutiqueAdapter extends RecyclerView.Adapter<BoutiqueAdapter.BoutiqueViewHolder> {
    List<BoutiqueBean> list;
    Context context;

    public BoutiqueAdapter(List<BoutiqueBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public BoutiqueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoutiqueViewHolder(View.inflate(context, R.layout.item_boutique, null));
    }

    @Override
    public void onBindViewHolder(BoutiqueViewHolder holder, int position) {
        final BoutiqueBean bean = list.get(position);
        holder.mTvBoutiqueTitle.setText(bean.getTitle());
        holder.mTvBoutiqueName.setText(bean.getName());
        holder.mTvBoutiqueDescription.setText(bean.getDescription());
        ImageLoader.downloadImg(context,holder.mIvBoutiqueImg,bean.getImageurl());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, BoutiqueChildActivity.class)
                .putExtra(I.NewAndBoutiqueGoods.CAT_ID,bean.getId())
                .putExtra(I.Boutique.TITLE,bean.getTitle()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void initData(ArrayList<BoutiqueBean> list) {
        if (this.list!=null){
            this.list.clear();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    class BoutiqueViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivBoutiqueImg)
        ImageView mIvBoutiqueImg;
        @BindView(R.id.tvBoutiqueTitle)
        TextView mTvBoutiqueTitle;
        @BindView(R.id.tvBoutiqueName)
        TextView mTvBoutiqueName;
        @BindView(R.id.tvBoutiqueDescription)
        TextView mTvBoutiqueDescription;
        @BindView(R.id.layout_boutique_item)
        RelativeLayout mLayoutBoutiqueItem;

        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
