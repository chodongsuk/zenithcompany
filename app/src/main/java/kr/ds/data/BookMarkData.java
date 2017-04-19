package kr.ds.data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import kr.ds.asynctask.DsAsyncTask;
import kr.ds.asynctask.DsAsyncTaskCallback;
import kr.ds.db.BookMarkDB;
import kr.ds.handler.ListHandler;
import kr.ds.httpclient.DsHttpClient;
import kr.ds.utils.DsObjectUtils;

/**
 * Created by Administrator on 2016-08-31.
 */
public class BookMarkData extends BaseData {

    private String URL = "";
    private String PARAM = "";

    private ListHandler mListHandler;
    private ArrayList<ListHandler> mData;
    private BaseResultListener mResultListener;

    private BookMarkDB mBookMarkDB;
    private Cursor mCursor;

    public BookMarkData(Context context){
        mBookMarkDB = new BookMarkDB(context);

        /*
        mBookMarkDB.open();
        mCursor = mBookMarkDB.fetchAllForType();
        mCursor.moveToFirst();
        while (!mCursor.isAfterLast()) {
            mFdUids += mCursor.getString(1);
            mCursor.moveToNext();
            if (!mCursor.isAfterLast()) {
                mFdUids += ",";
            }
        }
        mCursor.close();
        mBookMarkDB.close();
         */
    }

    @Override
    public BaseData clear() {
        if (mData != null) {
            mData = null;
        }
        mData = new ArrayList<ListHandler>();
        if (mListHandler != null) {
            mListHandler = null;
        }
        mListHandler = new ListHandler();
        return this;
    }

    @Override
    public BaseData setUrl(String url) {
        return this;
    }
    @Override
    public BaseData setParam(String param) {
        return this;
    }

    @Override
    public BaseData getView() {
        new DsAsyncTask<String[]>().setCallable(new Callable<String[]>() {
            @Override
            public String[] call() throws Exception {

                mBookMarkDB.open();
                mCursor = mBookMarkDB.fetchAllForType();
                mCursor.moveToFirst();
                while (!mCursor.isAfterLast()) {
                    mCursor.getString(1);
                    mData.add(new ListHandler());
                    if (mData.size() > 0) {
                        mListHandler = mData.get(mData.size() - 1);

                        mListHandler.setTitle(mCursor.getString(1));
                        mListHandler.setUrl(mCursor.getString(2));
                        if(!DsObjectUtils.isEmpty(mCursor.getString(3))) {
                            mListHandler.setImage(mCursor.getString(3));
                        }else{
                            mListHandler.setImage("http://zenithcompany1.cafe24.com/json/image/noimage.jpg");
                        }

                    }
                    mCursor.moveToNext();
                }
                mCursor.close();
                mBookMarkDB.close();

                String[] summery = new String[2];
                summery[0] = "success";
                summery[1] = "";
                return summery;

            }
        }).setCallback(new DsAsyncTaskCallback<String[]>() {
            @Override
            public void onPreExecute() {
            }
            @Override
            public void onPostExecute(String[] result) {
                if (result[0].matches("success")) {
                    if (mResultListener != null) {
                        mResultListener.OnComplete(mData);
                        mResultListener.OnMessage(result[1]);
                    }
                } else {
                    if (mResultListener != null) {
                        mResultListener.OnComplete(null);
                        mResultListener.OnMessage(result[1]);
                    }
                }
            }
            @Override
            public void onCancelled() {
            }
            @Override
            public void Exception(Exception e) {
                if (mResultListener != null) {
                    mResultListener.OnMessage(e.getMessage() + "");
                }
            }
        }).execute();
        return this;
    }

    @Override
    public <T> BaseData getViewPost(T post) {
        return this;
    }

    @Override
    public <T> BaseData setCallBack(T resultListener) {
        mResultListener = (BaseResultListener) resultListener;
        return this;
    }


}
