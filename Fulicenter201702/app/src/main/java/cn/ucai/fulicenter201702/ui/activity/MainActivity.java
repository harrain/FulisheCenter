package cn.ucai.fulicenter201702.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter201702.R;
import cn.ucai.fulicenter201702.application.FuLiCenterApplication;
import cn.ucai.fulicenter201702.application.I;
import cn.ucai.fulicenter201702.data.utils.L;
import cn.ucai.fulicenter201702.ui.fragment.BoutiqueFragment;
import cn.ucai.fulicenter201702.ui.fragment.CartFragment;
import cn.ucai.fulicenter201702.ui.fragment.CategoryFragment;
import cn.ucai.fulicenter201702.ui.fragment.NewGoodsFragment;
import cn.ucai.fulicenter201702.ui.fragment.PersonalFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    NewGoodsFragment mNewGoodsFragment;
    BoutiqueFragment mBoutiqueFragment;
    CategoryFragment mCategoryFragment;
    CartFragment mCartFragment;
    PersonalFragment mPersonalFragment;
    Fragment[] mFragments;
    RadioButton[] mRadioButtons;
    int currentIndex, index;
    @BindView(R.id.layout_new_good)
    RadioButton mLayoutNewGood;
    @BindView(R.id.layout_boutique)
    RadioButton mLayoutBoutique;
    @BindView(R.id.layout_category)
    RadioButton mLayoutCategory;
    @BindView(R.id.layout_cart)
    RadioButton mLayoutCart;
    @BindView(R.id.layout_personal_center)
    RadioButton mLayoutPersonalCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragment();
        initRedioButton();
        showFragment();
    }

    private void initRedioButton() {
        mRadioButtons = new RadioButton[5];
        mRadioButtons[0] = mLayoutNewGood;
        mRadioButtons[1] = mLayoutBoutique;
        mRadioButtons[2] = mLayoutCategory;
        mRadioButtons[3] = mLayoutCart;
        mRadioButtons[4] = mLayoutPersonalCenter;
    }

    private void initFragment() {
        mNewGoodsFragment = new NewGoodsFragment();
        mBoutiqueFragment = new BoutiqueFragment();
        mCategoryFragment = new CategoryFragment();
        mCartFragment = new CartFragment();
        mPersonalFragment = new PersonalFragment();
        mFragments = new Fragment[5];
        mFragments[0] = mNewGoodsFragment;
        mFragments[1] = mBoutiqueFragment;
        mFragments[2] = mCategoryFragment;
        mFragments[3] = mCartFragment;
        mFragments[4] = mPersonalFragment;
    }

    private void showFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mFragments[0])
                .add(R.id.fragment_container, mFragments[1])
                .add(R.id.fragment_container, mFragments[2])
                .show(mFragments[0])
                .hide(mFragments[1])
                .hide(mFragments[2])
                .commit();
    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.layout_new_good:
                index = 0;
                break;
            case R.id.layout_boutique:
                index = 1;
                break;
            case R.id.layout_category:
                index = 2;
                break;
            case R.id.layout_cart:
                if (FuLiCenterApplication.getInstance().getCurrentUser() == null) {
                    startActivityForResult(new Intent(MainActivity.this, LoginActivity.class),
                            I.REQUEST_CODE_LOGIN_FROM_CART);
                } else {
                    index = 3;
                }
                break;
            case R.id.layout_personal_center:
                if (FuLiCenterApplication.getInstance().getCurrentUser() == null) {
                    startActivityForResult(new Intent(MainActivity.this, LoginActivity.class),
                            I.REQUEST_CODE_LOGIN);
                } else {
                    index = 4;
                }
                break;
        }
        setFragment();
    }

    private void setFragment() {
        if (index != currentIndex) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(mFragments[currentIndex]);
            if (!mFragments[index].isAdded()) {
                fragmentTransaction.add(R.id.fragment_container, mFragments[index]);
            }
            fragmentTransaction.show(mFragments[index]);
            fragmentTransaction.commit();
            currentIndex = index;
        }
        setRedioButton();
    }

    private void setRedioButton() {
        for (int i = 0; i < mRadioButtons.length; i++) {
            mRadioButtons[i].setChecked(i==index?true:false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        L.e(TAG,"onActivityResult,requestCode="+requestCode+",resultCode="+resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == I.REQUEST_CODE_LOGIN) {
                index = 4;
            }
            if (requestCode == I.REQUEST_CODE_LOGIN_FROM_CART){
                index = 3;
            }
            setFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.e(TAG,"onResume,index="+index+",user="+FuLiCenterApplication.getInstance().getCurrentUser());
        if (index==4 && FuLiCenterApplication.getInstance().getCurrentUser() == null){
            index = 0;
            setFragment();
        }
    }
}
