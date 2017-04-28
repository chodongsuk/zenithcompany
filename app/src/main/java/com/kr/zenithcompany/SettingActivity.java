package com.kr.zenithcompany;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import java.util.ArrayList;

import kr.ds.adapter.CodeAdapter;
import kr.ds.adapter.ListAdapter;
import kr.ds.config.Config;
import kr.ds.data.BaseResultListener;
import kr.ds.data.CodeData;
import kr.ds.data.ListData;
import kr.ds.handler.CodeHandler;
import kr.ds.handler.VersionCheckHandler;
import kr.ds.utils.DsObjectUtils;
import kr.ds.utils.SharedPreference;
import kr.ds.utils.VersionUtils;
import kr.ds.utils.gcmHandler;

/**
 * Created by Administrator on 2017-04-12.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private ArrayList<CodeHandler> mData;
    private ListView mListView;
    private CodeAdapter mCodeAdapter;
    public static CheckBox mCheckBoxAll;

    private CheckBox mCheckBoxPush;
    private ArrayList<VersionCheckHandler> DATA = new ArrayList<VersionCheckHandler>();
    private LinearLayout mLinearLayoutPush;
    private LinearLayout mLinearLayoutArea;
    private LinearLayout mLinearLayoutReView;
    private LinearLayout mLinearLayoutNotice;
    private String regId;
    private String androidId;
    private String url = "http://zenithcompany1.cafe24.com/json/gcm/gcm_send_check.php";
    private String updateurl = "http://zenithcompany1.cafe24.com/json/gcm/gcm_send_update.php";
    private TextView mTextViewVersion;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle("설정");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mListView = (ListView)findViewById(R.id.listView);
        mLinearLayoutPush = (LinearLayout)findViewById(R.id.linearLayout_push);
        (mLinearLayoutReView = (LinearLayout)findViewById(R.id.linearLayout_review)).setOnClickListener(this);
        (mLinearLayoutNotice = (LinearLayout)findViewById(R.id.linearLayout_notice)).setOnClickListener(this);
        (mCheckBoxPush = (CheckBox)findViewById(R.id.checkBox_push)).setOnClickListener(this);
       mTextViewVersion = (TextView)findViewById(R.id.textView_version);
        (mCheckBoxAll = (CheckBox)findViewById(R.id.checkBox)).setOnClickListener(this);

        mTextViewVersion.setText(new VersionUtils().VersionName(getApplicationContext()));

        if(!DsObjectUtils.getInstance(getApplicationContext()).isEmpty(SharedPreference.getSharedPreference(getApplicationContext(), Config.TOKEN))){
            mLinearLayoutPush.setVisibility(View.VISIBLE);
            regId = SharedPreference.getSharedPreference(getApplicationContext(), Config.TOKEN);
            androidId = SharedPreference.getSharedPreference(getApplicationContext(), Config.ANDROID_ID);
            if(!DsObjectUtils.getInstance(getApplicationContext()).isEmpty(regId) && !DsObjectUtils.getInstance(getApplicationContext()).isEmpty(androidId)) {
                new regSendCheckTask().execute(androidId, regId, "", url);
            }
        }else{
            mLinearLayoutPush.setVisibility(View.GONE);
        }

        if(SharedPreference.getBooleanSharedPreference(getApplicationContext(), Config.ALL_CODE)){
            mCheckBoxAll.setChecked(true);
        }else{
            mCheckBoxAll.setChecked(false);
        }
        new CodeData(getApplicationContext(), CodeData.LIST).clear().setCallBack(new BaseResultListener() {
            @Override
            public <T> void OnComplete() {

            }

            @Override
            public <T> void OnComplete(Object data) {
                if(data != null){
                    mData = (ArrayList<CodeHandler>) data;

                    mCodeAdapter = new CodeAdapter(getApplicationContext(), mData);
                    AlphaInAnimationAdapter mAlphaInAnimationAdapter = new AlphaInAnimationAdapter(mCodeAdapter);
                    mAlphaInAnimationAdapter.setAbsListView(mListView);
                    mListView.setAdapter(mAlphaInAnimationAdapter);

                }
            }
            @Override
            public void OnMessage(String str) {

            }
        }).setUrl(Config.URL+Config.URL_CODE).getView();
    }

    private class regSendCheckTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            return new gcmHandler().HttpPostData(params[0], params[1],params[2],params[3]);
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(result.trim().matches("Y")){//gcm id값이 없을경우
                setBackgroundChecked(mCheckBoxPush, true);
            }else{
                setBackgroundChecked(mCheckBoxPush, false);
            }
        }
    }

    public void setBackgroundChecked(CheckBox toggleButton, boolean ischeck){
        if(ischeck == false){
            toggleButton.setChecked(false);
        }else{
            toggleButton.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.checkBox:
                if(mCheckBoxAll.isChecked()){
                    setAllCheck(true);
                }else{
                    setAllCheck(false);
                }
                break;
            case R.id.checkBox_push:
                if(mCheckBoxPush.isChecked() == true){
                    new regSendCheckTask().execute(androidId, regId, "Y",updateurl);
                    setBackgroundChecked(mCheckBoxPush, true);
                }else{
                    new regSendCheckTask().execute(androidId, regId, "N",updateurl);
                    setBackgroundChecked(mCheckBoxPush, false);
                }
                break;
            case R.id.linearLayout_review:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=com.kr.zenithcompany")));
                } catch (Exception e) {
                    // TODO: handle exception
                    Toast.makeText(getApplicationContext(), "플레이 스토어 설치 후 이용 가능합니다.",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.linearLayout_notice:
                Intent intent = new Intent(this, NoticeActivity.class);
                startActivity(intent);
                break;

        }
    }

    private void setAllCheck(boolean check){
        if(check){
            String codes = "";
            for(int i=0; i<mData.size(); i++){
                if(i < mData.size()-1){
                    codes += mData.get(i).getCode()+",";
                }else{
                    codes += mData.get(i).getCode();
                }
                mData.get(i).setCheck(true);
            }
            SharedPreference.putSharedPreference(getApplicationContext(), Config.ALL_CODE, true);
            SharedPreference.putSharedPreference(getApplicationContext(), Config.CODES, codes);
            mCodeAdapter.notifyDataSetChanged();

        }else{
            for(int i=0; i<mData.size(); i++){
                mData.get(i).setCheck(false);
            }
            SharedPreference.putSharedPreference(getApplicationContext(), Config.ALL_CODE, false);
            SharedPreference.putSharedPreference(getApplicationContext(), Config.CODES, "");
            mCodeAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
