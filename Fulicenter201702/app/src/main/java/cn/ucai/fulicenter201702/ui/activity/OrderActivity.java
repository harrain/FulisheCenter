package cn.ucai.fulicenter201702.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter201702.R;
import cn.ucai.fulicenter201702.application.I;

/**
 * Created by clawpo on 2017/5/17.
 */

public class OrderActivity extends AppCompatActivity {
    int payPrice;
    @BindView(R.id.backClickArea)
    LinearLayout mBackClickArea;
    @BindView(R.id.tv_common_title)
    TextView mTvCommonTitle;
    @BindView(R.id.ed_order_name)
    EditText mEdOrderName;
    @BindView(R.id.ed_order_phone)
    EditText mEdOrderPhone;
    @BindView(R.id.spin_order_province)
    Spinner mSpinOrderProvince;
    @BindView(R.id.ed_order_street)
    EditText mEdOrderStreet;
    @BindView(R.id.tv_order_price)
    TextView mTvOrderPrice;

    private static String URL = "http://218.244.151.190/demo/charge";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        payPrice = getIntent().getIntExtra(I.Cart.PAY_PRICE, 0);
        initView();
        initPingPP();
    }

    private void initPingPP() {
        //设置需要使用的支付方式
        PingppOne.enableChannels(new String[] { "wx", "alipay", "upacp", "bfb", "jdpay_wap" });

        // 提交数据的格式，默认格式为json
        // PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";

        PingppLog.DEBUG = true;
    }

    private void initView() {
        mTvCommonTitle.setText(R.string.order_title);
        mTvOrderPrice.setText("合计：￥ "+payPrice);
    }

    @OnClick(R.id.tv_order_buy)
    public void onBuyClick() {
        if (checkInput()){
            pay();
        }
    }

    private void pay() {
        // 产生个订单号
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

        // 计算总金额（以分为单位）
        int amount = payPrice * 100;

        // 构建账单json对象
        JSONObject bill = new JSONObject();

        // 自定义的额外信息 选填
        JSONObject extras = new JSONObject();
        try {
            extras.put("extra1", "extra1");
            extras.put("extra2", "extra2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            bill.put("order_no", orderNo);
            bill.put("amount", amount);
            bill.put("extras", extras);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //壹收款: 创建支付通道的对话框
        PingppOne.showPaymentChannels(this, bill.toString(), URL, new PaymentHandler() {
            @Override public void handlePaymentResult(Intent data) {
                if (data != null) {
                    /**
                     * code：支付结果码  -2:服务端错误、 -1：失败、 0：取消、1：成功
                     * error_msg：支付结果信息
                     */
                    int code = data.getExtras().getInt("code");
                    String result = data.getExtras().getString("result");
                }
            }
        });
    }

    private boolean checkInput() {
        String receiveName=mEdOrderName.getText().toString();
        if(TextUtils.isEmpty(receiveName)){
            mEdOrderName.setError("收货人姓名不能为空");
            mEdOrderName.requestFocus();
            return false;
        }
        String mobile=mEdOrderPhone.getText().toString();
        if(TextUtils.isEmpty(mobile)){
            mEdOrderPhone.setError("手机号码不能为空");
            mEdOrderPhone.requestFocus();
            return false;
        }
        if(!mobile.matches("[\\d]{11}")){
            mEdOrderPhone.setError("手机号码格式错误");
            mEdOrderPhone.requestFocus();
            return false;
        }
        String area=mSpinOrderProvince.getSelectedItem().toString();
        if(TextUtils.isEmpty(area)){
            Toast.makeText(OrderActivity.this,"收货地区不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        String address=mEdOrderStreet.getText().toString();
        if(TextUtils.isEmpty(address)){
            mEdOrderStreet.setError("街道地址不能为空");
            mEdOrderStreet.requestFocus();
            return false;
        }
        return true;
    }

    @OnClick(R.id.backClickArea)
    public void onBackClick(){
        finish();
    }
}
