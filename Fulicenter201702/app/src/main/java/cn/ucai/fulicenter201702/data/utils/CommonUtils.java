package cn.ucai.fulicenter201702.data.utils;

import android.content.Context;
import android.widget.Toast;

import cn.ucai.fulicenter201702.application.FuLiCenterApplication;

public class CommonUtils {
    private static Toast toast;

    public static void showLongToast(String msg){
        showToast(FuLiCenterApplication.getInstance(),msg,Toast.LENGTH_LONG).show();
//        Toast.makeText(FuLiCenterApplication.getInstance(),msg,Toast.LENGTH_LONG).show();
    }
    public static void showShortToast(String msg){
        showToast(FuLiCenterApplication.getInstance(),msg,Toast.LENGTH_SHORT).show();
//        Toast.makeText(FuLiCenterApplication.getInstance(),msg,Toast.LENGTH_SHORT).show();
    }
    public static void showLongToast(int rId){
        showLongToast(FuLiCenterApplication.getInstance().getString(rId));
    }
    public static void showShortToast(int rId){
        showShortToast(FuLiCenterApplication.getInstance().getString(rId));
    }

    public static Toast showToast(Context context,String msg,int length){
        if (toast==null){
            toast = Toast.makeText(context,msg,length);
        }else{
            toast.setText(msg);
        }
        return toast;
    }
}
