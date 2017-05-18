package cn.ucai.fulicenter201702.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;

import cn.ucai.fulicenter201702.R;
import cn.ucai.fulicenter201702.data.bean.CategoryChildBean;
import cn.ucai.fulicenter201702.data.utils.CommonUtils;
import cn.ucai.fulicenter201702.ui.adapter.CatFiterAdapter;

/**
 * Created by clawpo on 2017/5/9.
 */

public class CatFiterCategoryButton extends android.support.v7.widget.AppCompatButton {
    PopupWindow mPopupWindow;
    Context context;
    boolean isExpan = false;
    CatFiterAdapter adapter;
    GridView gv;
    public CatFiterCategoryButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setCatFiterListener();
    }

    private void setCatFiterListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpan){
                    if (mPopupWindow!=null && mPopupWindow.isShowing()){
                        mPopupWindow.dismiss();
                    }
                }else{
                    initPopWin();
                }
                setArrow();
            }
        });
    }

    private void setArrow() {
        Drawable end = context.getDrawable(isExpan? R.mipmap.arrow2_down:R.mipmap.arrow2_up);
        setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,end,null);
        isExpan = !isExpan;
    }

    private void initPopWin(){
        if (mPopupWindow==null){
            mPopupWindow = new PopupWindow(context);
            mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xbb000000));
//            TextView tv = new TextView(context);
//            tv.setTextColor(getResources().getColor(R.color.red));
//            tv.setTextSize(30);
//            tv.setText("CatFilterCategoryButton");
//            mPopupWindow.setContentView(tv);
            mPopupWindow.setContentView(gv);
        }
        mPopupWindow.showAsDropDown(this);
    }

    public void initView(String groupName, ArrayList<CategoryChildBean> list){
        if (groupName==null || list==null || list.size()==0){
            CommonUtils.showLongToast("数据获取异常，请重试！");
            return;
        }
        this.setText(groupName);
        adapter = new CatFiterAdapter(context,list,groupName);
        gv = new GridView(context);
        gv.setNumColumns(GridView.AUTO_FIT);
        gv.setHorizontalSpacing(10);
        gv.setVerticalSpacing(10);
        gv.setAdapter(adapter);
    }

    public void release(){
        if (mPopupWindow!=null){
            mPopupWindow.dismiss();
        }
    }


}
