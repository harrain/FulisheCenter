package cn.ucai.fulicenter201702.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter201702.R;
import cn.ucai.fulicenter201702.application.I;
import cn.ucai.fulicenter201702.data.bean.NewGoodsBean;
import cn.ucai.fulicenter201702.data.utils.ImageLoader;
import cn.ucai.fulicenter201702.ui.activity.GoodsDetailActivity;

/**
 * Created by clawpo on 2017/5/4.
 */

public class GoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<NewGoodsBean> list;
    Context context;
    boolean isMore = true;

    public GoodsAdapter(List<NewGoodsBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==I.TYPE_FOOTER)
            return new FooterViewHolder(View.inflate(context, R.layout.item_footer, null));
        else
            return new GoodsViewHolder(View.inflate(context, R.layout.item_goods, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==I.TYPE_FOOTER){
            ((FooterViewHolder)holder).mTvFooter.setText(getFooter());
        }else{
            final NewGoodsBean bean = list.get(position);
            ((GoodsViewHolder)holder).mTvGoodsName.setText(bean.getGoodsName());
            ((GoodsViewHolder)holder).mTvGoodsPrice.setText(bean.getCurrencyPrice());
            ImageLoader.downloadImg(context, ((GoodsViewHolder)holder).mIvGoodsThumb,
                    bean.getGoodsThumb());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,GoodsDetailActivity.class)
                    .putExtra(I.GoodsDetails.KEY_GOODS_ID,bean.getGoodsId()));
                }
            });
        }

    }

    private int getFooter() {
        return isMore?R.string.load_more:R.string.no_more;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==getItemCount()-1)
            return I.TYPE_FOOTER;
        else
            return I.TYPE_ITEM;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size()+1 : 1;
    }

    public void addData(ArrayList<NewGoodsBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void initData(ArrayList<NewGoodsBean> list) {
        if (this.list!=null){
            list.clear();
        }
        addData(list);
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivGoodsThumb)
        ImageView mIvGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView mTvGoodsName;
        @BindView(R.id.tvGoodsPrice)
        TextView mTvGoodsPrice;
        @BindView(R.id.layout_goods)
        LinearLayout mLayoutGoods;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_footer)
        TextView mTvFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void sortGoods(final int sortBy){
        Collections.sort(list, new Comparator<NewGoodsBean>() {
            @Override
            public int compare(NewGoodsBean left, NewGoodsBean right) {
                int result=0;
                switch (sortBy){
                    case I.SORT_BY_PRICE_ASC:
                        result = getPrice(left.getCurrencyPrice())-getPrice(right.getCurrencyPrice());
                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result = getPrice(right.getCurrencyPrice())-getPrice(left.getCurrencyPrice());
                        break;
                    case I.SORT_BY_ADDTIME_ASC:
                        result = (int) (left.getAddTime()-right.getAddTime());
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result = (int) (right.getAddTime()-left.getAddTime());
                        break;
                }
                return result;
            }
        });
        notifyDataSetChanged();
    }

    private int getPrice(String currencyPrice) {
        String price = currencyPrice.substring(currencyPrice.indexOf("￥")+1);
        return Integer.parseInt(price);
    }
}
