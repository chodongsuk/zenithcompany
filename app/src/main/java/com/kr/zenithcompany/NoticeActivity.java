package com.kr.zenithcompany;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import kr.ds.fragment.BaseFragment;
import kr.ds.fragment.BookMarkFragment;
import kr.ds.fragment.WebFragment;

/**
 * Created by Administrator on 2017-04-28.
 */
public class NoticeActivity extends BaseActivity{
    private Toolbar mToolbar;
    private FragmentManager mFm;
    private FragmentTransaction mFt;
    private BaseFragment mFragment = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle("공지사항");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mFragment = BaseFragment.newInstance(WebFragment.class);
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mFt.replace(R.id.content_frame, mFragment);
        mFt.commitAllowingStateLoss();
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
