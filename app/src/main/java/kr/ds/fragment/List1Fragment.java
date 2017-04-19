package kr.ds.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.LocationSource;
import com.kr.zenithcompany.R;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


import kr.ds.adapter.ListAdapter;
import kr.ds.config.Config;
import kr.ds.data.BaseResultListener;
import kr.ds.data.ListData;
import kr.ds.db.BookMarkDB;
import kr.ds.handler.ListHandler;
import kr.ds.utils.DsKeyBoardUtils;
import kr.ds.utils.SharedPreference;


/**
 * Created by Administrator on 2016-08-31.
 */
public class List1Fragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private ArrayList<ListHandler> mData;
    private ArrayList<ListHandler> mMainData;
    private int mNumber = 20;
    private int mPage = 1;
    private int startPage = 0;
    private int endPage = 0;

    private View mView;
    private ProgressBar mProgressBar;
    private ListView mListView;
    private ListData mListData;
    private ListAdapter mListAdapter;
    private int mCurrentScrollState;
    private Boolean mIsTheLoding = false;
    private SwipeRefreshLayout mSwipeLayout;

    private final static int LIST = 0;
    private final static int ONLOAD = 1;
    private final static int REFRESH = 2;
    private Context mContext;

    private String mParam = "";

    private BookMarkDB mBookMarkDB;
    private Cursor mCursor;

    private EditText mEditTextMessage;
    private ImageView mImageViewBtn;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mContext = getActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mView = inflater.inflate(R.layout.fragment_list1, null);

        mEditTextMessage = (EditText)mView.findViewById(R.id.editText_message);

        mEditTextMessage.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mEditTextMessage.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_NEXT)
                {
                    try {
                        mParam = setCodeParam()+"&search="+ URLEncoder.encode(mEditTextMessage.getText().toString(),"utf-8");
                        mProgressBar.setVisibility(View.VISIBLE);
                        setList();
                        DsKeyBoardUtils.getInstance().hideKeyboard(getActivity());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });

        (mImageViewBtn = (ImageView) mView.findViewById(R.id.imageView_btn)).setOnClickListener(this);

        mListView = (ListView)mView.findViewById(R.id.listView);
        //mListView.setScrollViewCallbacks(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                try{
                    mBookMarkDB = new BookMarkDB(mContext);

                    mBookMarkDB.open();
                    mCursor = mBookMarkDB.BookMarkConfirm(mData.get(position).getUrl());
                    if (mCursor.getCount() == 0) {
                        mBookMarkDB.createNote(mData.get(position).getTitle(), mData.get(position).getUrl(),mData.get(position).getImage());
                    }
                    mCursor.close();
                    mBookMarkDB.close();
                    Intent NextIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mData.get(position).getUrl()));
                    startActivity(NextIntent);
                }catch (Exception e){
                    Toast.makeText(mContext, "오류가 발생 되었습니다. 계속 문제가 발생시 관리자에게 문의 해주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        mProgressBar = (ProgressBar)mView.findViewById(R.id.progressBar);
        mSwipeLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.colorPrimary);
        return mView;
    }

    private String setCodeParam(){
        String param = "";
        if(SharedPreference.getBooleanSharedPreference(mContext, Config.ALL_CODE)){
            param += "?all_code=ok";
        }else{
            param += "?all_code=";
        }
        param += "&codes="+SharedPreference.getSharedPreference(mContext, Config.CODES);

        return param;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        mParam = setCodeParam();
        mProgressBar.setVisibility(View.VISIBLE);
        setList();
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                mCurrentScrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                int topRowVerticalPosition = (mListView == null || mListView.getChildCount() == 0) ? 0 : mListView.getChildAt(0).getTop();
                if(mData != null ){
                    mSwipeLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
                }else{
                    mSwipeLayout.setEnabled(false);
                }
                boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
                if(!mIsTheLoding && loadMore &&  mCurrentScrollState != SCROLL_STATE_IDLE){
                    mIsTheLoding = true;
                    onLoadMore();
                }
            }
        });
    }

    public void setList(){

        new ListData().clear().setCallBack(new BaseResultListener() {
            @Override
            public <T> void OnComplete() {

            }
            @Override
            public <T> void OnComplete(Object data) {
                mProgressBar.setVisibility(View.GONE);
                mIsTheLoding = false;
                if(data != null){
                    mPage = 1;

                    mMainData = (ArrayList<ListHandler>) data;
                    if(mMainData.size() - ((mPage-1)*mNumber) > 0){
                        if(mMainData.size() >= mPage * mNumber){
                            startPage = (mPage-1) * mNumber;
                            endPage = mPage * mNumber;
                        }else{
                            startPage = (mPage-1) * mNumber;
                            endPage = mMainData.size();
                        }
                        mData  = new ArrayList<>();
                        for(int i=startPage; i< endPage; i++){
                            mData.add(mMainData.get(i));
                        }
                        mListAdapter = new ListAdapter(mContext, mData);
                        AlphaInAnimationAdapter mAlphaInAnimationAdapter = new AlphaInAnimationAdapter(mListAdapter);
                        mAlphaInAnimationAdapter.setAbsListView(mListView);
                        mListView.setAdapter(mAlphaInAnimationAdapter);
                    }
                }else{
                    Toast.makeText(mContext, "데이터가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    mListView.setAdapter(null);
                }
            }

            @Override
            public void OnMessage(String str) {

            }
        }).setUrl(Config.URL+ Config.URL_LIST).setParam(mParam).getView();
    }

    public void setListRefresh(){
        try {
            mParam = setCodeParam()+"&search="+ URLEncoder.encode(mEditTextMessage.getText().toString(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new ListData().clear().setCallBack(new BaseResultListener() {
            @Override
            public <T> void OnComplete() {

            }
            @Override
            public <T> void OnComplete(Object data) {
                mSwipeLayout.setRefreshing(false);
                mIsTheLoding = false;
                if(data != null){
                    mPage = 1;

                    mMainData = (ArrayList<ListHandler>) data;
                    if(mMainData.size() - ((mPage-1)*mNumber) > 0){
                        if(mMainData.size() >= mPage * mNumber){
                            startPage = (mPage-1) * mNumber;
                            endPage = mPage * mNumber;
                        }else{
                            startPage = (mPage-1) * mNumber;
                            endPage = mMainData.size();
                        }
                        mData  = new ArrayList<>();
                        for(int i=startPage; i< endPage; i++){
                            mData.add(mMainData.get(i));
                        }
                        mListAdapter = new ListAdapter(mContext, mData);
                        AlphaInAnimationAdapter mAlphaInAnimationAdapter = new AlphaInAnimationAdapter(mListAdapter);
                        mAlphaInAnimationAdapter.setAbsListView(mListView);
                        mListView.setAdapter(mAlphaInAnimationAdapter);
                    }
                }else{
                    Toast.makeText(mContext, "데이터가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    mListView.setAdapter(null);
                }
            }

            @Override
            public void OnMessage(String str) {

            }
        }).setUrl(Config.URL+ Config.URL_LIST).setParam(mParam).getView();
    }

    public void setListOnLoad(){
        mPage++;
        if(mMainData.size() - ((mPage-1)*mNumber) < 0){
            mIsTheLoding = true;
        }else{
            if(mMainData.size() >= mPage * mNumber){
                startPage = (mPage-1) * mNumber;
                endPage = mPage * mNumber;
            }else{
                startPage = (mPage-1) * mNumber;
                endPage = mMainData.size();
            }
            for(int i=startPage; i< endPage; i++){
                mData.add(mMainData.get(i));
            }
            mListAdapter.notifyDataSetChanged();
            mIsTheLoding = false;
        }
        mProgressBar.setVisibility(View.GONE);
    }
    @Override
    public void onRefresh() {
        setListRefresh();
        // TODO Auto-generated method stub
    }

    public void onLoadMore(){

        mProgressBar.setVisibility(View.VISIBLE);
        setListOnLoad();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView_btn:
                try {
                    mParam = setCodeParam()+"&search="+ URLEncoder.encode(mEditTextMessage.getText().toString(),"utf-8");
                    mProgressBar.setVisibility(View.VISIBLE);
                    setList();
                    DsKeyBoardUtils.getInstance().hideKeyboard(getActivity());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
