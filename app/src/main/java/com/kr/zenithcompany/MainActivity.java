package com.kr.zenithcompany;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import java.util.ArrayList;

import kr.ds.adapter.ListAdapter;
import kr.ds.config.Config;
import kr.ds.data.BaseResultListener;
import kr.ds.data.ListData;
import kr.ds.data.PopupData;
import kr.ds.fragment.BaseFragment;
import kr.ds.fragment.BookMarkFragment;
import kr.ds.fragment.List1Fragment;
import kr.ds.fragment.Tab5Fragment;
import kr.ds.fragment.Tab6Fragment;
import kr.ds.fragment.TopListFragment;
import kr.ds.fragment.WebFragment;
import kr.ds.handler.ListHandler;
import kr.ds.handler.PopupHandler;
import kr.ds.popup.PopupDialog;
import kr.ds.utils.DsObjectUtils;
import kr.ds.utils.SharedPreference;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private FragmentManager mFm;
    private FragmentTransaction mFt;
    private BaseFragment mFragment = null;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private final int TAB1 = 1;
    private final int TAB2 = 2;
    private final int TAB3 = 3;
    private final int TAB4 = 4;
    private final int TAB5 = 5;


    private Bundle mBundle;

    private LinearLayout mLinearLayoutTab1,mLinearLayoutTab2,mLinearLayoutTab3,mLinearLayoutTab4,mLinearLayoutTab5;
    private ImageView mImageViewTab1,mImageViewTab2,mImageViewTab3,mImageViewTab4, mImageViewTab5;

    private ImageView mImageViewSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        (mLinearLayoutTab1 = (LinearLayout)findViewById(R.id.linearLayout_tab1)).setOnClickListener(this);
        (mLinearLayoutTab2 = (LinearLayout)findViewById(R.id.linearLayout_tab2)).setOnClickListener(this);
        (mLinearLayoutTab3 = (LinearLayout)findViewById(R.id.linearLayout_tab3)).setOnClickListener(this);
        (mLinearLayoutTab4 = (LinearLayout)findViewById(R.id.linearLayout_tab4)).setOnClickListener(this);
        (mLinearLayoutTab5 = (LinearLayout)findViewById(R.id.linearLayout_tab5)).setOnClickListener(this);
        (mImageViewSetting = (ImageView) findViewById(R.id.imageView_setting)).setOnClickListener(this);

        mImageViewTab1 = (ImageView) findViewById(R.id.imageView_tab1);
        mImageViewTab2 = (ImageView) findViewById(R.id.imageView_tab2);
        mImageViewTab3 = (ImageView) findViewById(R.id.imageView_tab3);
        mImageViewTab4 = (ImageView) findViewById(R.id.imageView_tab4);
        mImageViewTab5 = (ImageView) findViewById(R.id.imageView_tab5);


        if (checkPlayServices() && DsObjectUtils.getInstance(getApplicationContext()).isEmpty(SharedPreference.getSharedPreference(getApplicationContext(), Config.TOKEN))) { //토큰이 없는경우..
            Intent intent = new Intent(getApplicationContext(), RegistrationIntentService.class);
            startService(intent);
        }
        setFragment(TAB1);
        setPopup();

    }
    private void setPopup(){
        new PopupData().clear().setCallBack(new BaseResultListener() {
            @Override
            public <T> void OnComplete() {

            }
            @Override
            public <T> void OnComplete(Object data) {
                if(data != null) {
                    PopupHandler popupHandler = (PopupHandler) data;
                    if(!DsObjectUtils.isEmpty(popupHandler.getHidden())){
                        if(popupHandler.getHidden().matches("N")){
                            final PopupDialog mDialog = new PopupDialog(popupHandler);// call the static method
                            mDialog.show(getSupportFragmentManager(), "dialog");
                        }
                    }

                }
            }

            @Override
            public void OnMessage(String str) {

            }
        }).setUrl(Config.URL+ Config.URL_POPUP).setParam("").getView();
    }

    private void setFragment(int tab) {

        mImageViewTab1.setImageResource(R.drawable.tab1_off);
        mImageViewTab2.setImageResource(R.drawable.tab2_off);
        mImageViewTab3.setImageResource(R.drawable.tab3_off);
        mImageViewTab4.setImageResource(R.drawable.tab4_off);
        mImageViewTab5.setImageResource(R.drawable.tab5_off);

        if(tab == TAB1){
            mImageViewTab1.setImageResource(R.drawable.tab1_on);
            mFragment = BaseFragment.newInstance(List1Fragment.class);
        }else if(tab == TAB2){
            mImageViewTab2.setImageResource(R.drawable.tab2_on);
            mFragment = BaseFragment.newInstance(TopListFragment.class);
        }else if(tab == TAB3){
            mImageViewTab3.setImageResource(R.drawable.tab3_on);
            mFragment = BaseFragment.newInstance(Tab5Fragment.class);
        }else if(tab == TAB4){
            mImageViewTab4.setImageResource(R.drawable.tab4_on);
            mFragment = BaseFragment.newInstance(Tab6Fragment.class);
        }else if(tab == TAB5){
            mImageViewTab5.setImageResource(R.drawable.tab5_on);
            mFragment = BaseFragment.newInstance(BookMarkFragment.class);
        }

        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mFt.replace(R.id.content_frame, mFragment);
        mFt.commitAllowingStateLoss();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                //finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linearLayout_tab1:
                setFragment(TAB1);
                break;
            case R.id.linearLayout_tab2:
                setFragment(TAB2);
                break;
            case R.id.linearLayout_tab3:
                setFragment(TAB3);
                break;
            case R.id.linearLayout_tab4:
                setFragment(TAB4);
                break;
            case R.id.linearLayout_tab5:
                setFragment(TAB5);
                break;
            case R.id.imageView_setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;

        }
    }
}
