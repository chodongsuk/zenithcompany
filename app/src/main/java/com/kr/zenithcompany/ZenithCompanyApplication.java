package com.kr.zenithcompany;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.lang.reflect.Array;
import java.util.ArrayList;

import kr.ds.config.Config;
import kr.ds.data.BaseResultListener;
import kr.ds.data.CodeData;
import kr.ds.handler.CodeHandler;
import kr.ds.utils.DsObjectUtils;
import kr.ds.utils.SharedPreference;
import kr.ds.utils.UniqueID;


public class ZenithCompanyApplication extends MultiDexApplication {
	/**
	 * 이미지 로더, 이미지 캐시, 요청 큐를 초기화한다.
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		if(DsObjectUtils.isEmpty(SharedPreference.getSharedPreference(getApplicationContext(), Config.ANDROID_ID))){
			SharedPreference.putSharedPreference(getApplicationContext(), Config.ANDROID_ID, UniqueID.getUniqueID());
		}
		Log.i("TEST","start");
		if(DsObjectUtils.isEmpty(SharedPreference.getSharedPreference(getApplicationContext(), Config.CODE_DATE))){
			SharedPreference.putSharedPreference(getApplicationContext(), Config.CODE_DATE, "ok");
			SharedPreference.putSharedPreference(getApplicationContext(), Config.ALL_CODE, true);

			new CodeData(getApplicationContext(), CodeData.NEW).clear().setCallBack(new BaseResultListener() {
				@Override
				public <T> void OnComplete() {

				}

				@Override
				public <T> void OnComplete(Object data) {
					if(data != null){
						ArrayList<CodeHandler> mData = (ArrayList<CodeHandler>) data;
						String codes = "";
						for(int i=0; i<mData.size(); i++){
							if(i < mData.size()-1){
								codes += mData.get(i).getCode()+",";
							}else{
								codes += mData.get(i).getCode();
							}
						}
						SharedPreference.putSharedPreference(getApplicationContext(), Config.CODES, codes);
						Log.i("TEST",codes+"");
					}
				}
				@Override
				public void OnMessage(String str) {

				}
			}).setUrl(Config.URL+Config.URL_CODE).getView();
		}
	}
}
