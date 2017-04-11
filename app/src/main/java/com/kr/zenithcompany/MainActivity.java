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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import kr.ds.config.Config;
import kr.ds.fragment.BaseFragment;
import kr.ds.fragment.List1Fragment;
import kr.ds.fragment.TopListFragment;
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

    private Bundle mBundle;

    private LinearLayout mLinearLayoutTab1,mLinearLayoutTab2,mLinearLayoutTab3,mLinearLayoutTab4;
    private ImageView mImageViewTab1,mImageViewTab2,mImageViewTab3,mImageViewTab4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        (mLinearLayoutTab1 = (LinearLayout)findViewById(R.id.linearLayout_tab1)).setOnClickListener(this);
        (mLinearLayoutTab2 = (LinearLayout)findViewById(R.id.linearLayout_tab2)).setOnClickListener(this);
        (mLinearLayoutTab3 = (LinearLayout)findViewById(R.id.linearLayout_tab3)).setOnClickListener(this);
        (mLinearLayoutTab4 = (LinearLayout)findViewById(R.id.linearLayout_tab4)).setOnClickListener(this);

        mImageViewTab1 = (ImageView) findViewById(R.id.imageView_tab1);
        mImageViewTab2 = (ImageView) findViewById(R.id.imageView_tab2);
        mImageViewTab3 = (ImageView) findViewById(R.id.imageView_tab3);
        mImageViewTab4 = (ImageView) findViewById(R.id.imageView_tab4);

        if (checkPlayServices() && DsObjectUtils.getInstance(getApplicationContext()).isEmpty(SharedPreference.getSharedPreference(getApplicationContext(), Config.TOKEN))) { //토큰이 없는경우..
            Intent intent = new Intent(getApplicationContext(), RegistrationIntentService.class);
            startService(intent);
        }
        setFragment(TAB1);
    }

    private void setFragment(int tab) {
        if(tab == TAB1){
            mFragment = BaseFragment.newInstance(TopListFragment.class);
        }else if(tab == TAB2){
            mFragment = BaseFragment.newInstance(List1Fragment.class);
        }else if(tab == TAB3){
            mFragment = BaseFragment.newInstance(List1Fragment.class);
        }else if(tab == TAB4){
            mFragment = BaseFragment.newInstance(List1Fragment.class);
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
                Toast.makeText(getApplicationContext(), "새로운 기능이 여름에 오픈됩니다.", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
