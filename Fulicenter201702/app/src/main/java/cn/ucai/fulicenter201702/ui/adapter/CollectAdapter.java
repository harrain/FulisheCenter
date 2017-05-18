package cn.ucai.fulicenter201702.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter201702.R;
import cn.ucai.fulicenter201702.application.FuLiCenterApplication;
import cn.ucai.fulicenter201702.application.I;
import cn.ucai.fulicenter201702.data.bean.CollectBean;
import cn.ucai.fulicenter201702.data.bean.MessageBean;
import cn.ucai.fulicenter201702.data.net.IUserModel;
import cn.ucai.fulicenter201702.data.net.OnCompleteListener;
import cn.ucai.fulicenter201702.data.utils.CommonUtils;
import cn.ucai.fulicenter201702.data.utils.ImageLoader;
import cn.ucai.fulicenter201702.ui.activity.CollectsListActivity;
import cn.ucai.fulicenter201702.ui.activity.GoodsDetailActivity;

/**
 * Created by clawpo on 2017/5/4.
 */

public class CollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<CollectBean> list;
    Context context;
    boolean isMore = true;
    IUserModel model;

    public CollectAdapter(List<CollectBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public IUserModel getModel() {
        return model;
    }

    public void setModel(IUserModel model) {
        this.model = model;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==I.TYPE_FOOTER)
            return new FooterViewHolder(View.inflate(context, R.layout.item_footer, null));
        else
            return new GoodsViewHolder(View.inflate(context, R.layout.item_collect, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position)==I.TYPE_FOOTER){
            ((FooterViewHolder)holder).mTvFooter.setText(getFooter());
        }else{
            final CollectBean bean = list.get(position);
            ((GoodsViewHolder)holder).mTvGoodsName.setText(bean.getGoodsName());
            ImageLoader.downloadImg(context, ((GoodsViewHolder)holder).mIvGoodsThumb,
                    bean.getGoodsThumb());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CollectsListActivity)context).startActivityForResult(new Intent(context,GoodsDetailActivity.class)
                            .putExtra(I.GoodsDetails.KEY_GOODS_ID,bean.getGoodsId()),I.REQUEST_CODE_GO_DETAIL);
                }
            });

            ((GoodsViewHolder) holder).mIvDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeCollect(position);
                }
            });
        }

    }

    private void removeCollect(final int position) {
        if (model!=null){
            model.removeCollect(context, String.valueOf(list.get(position).getGoodsId()),
                    FuLiCenterApplication.getInstance().getCurrentUser().getMuserName(),
                    new OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result!=null && result.isSuccess()){
                                CommonUtils.showLongToast(result.getMsg());
                                list.remove(position);
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(String error) {

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

    public void addData(ArrayList<CollectBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void initData(ArrayList<CollectBean> list) {
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
        @BindView(R.id.iv_del)
        ImageView mIvDel;

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
}
