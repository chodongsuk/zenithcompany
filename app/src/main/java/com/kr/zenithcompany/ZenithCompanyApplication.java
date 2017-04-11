package com.kr.zenithcompany;

import android.support.multidex.MultiDexApplication;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import kr.ds.config.Config;
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
	}
}
