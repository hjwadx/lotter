package com.hjw.lottery;

import java.util.LinkedList;
import java.util.List;

import com.hjw.lottery.utils.DatabaseHelper;

import fm.jihua.common.utils.Const;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class App extends fm.jihua.common.App{
	
	private List<Activity> activityList = new LinkedList<Activity>();
	private DatabaseHelper mDBHelper;
	private SQLiteDatabase mUserDB;
//	private DatabaseHelper mDBHelper;
//	private SQLiteDatabase mAccountDB;
//
//	private ImageCache mImageCache;
//	private static final String IMAGE_CACHE_DIR = "thumbs";
//	private LruCache<String, Object> mMemoryCache;
//	private static final int DEFAULT_MEM_CACHE_SIZE = 100 * 1024 ; // 100K
	
	
//	public static int mTimeSlotLength; 
	
	public App() {
		
	}
	

	public static App getInstance(){
		return (App) _app;
	}
	
	@Override
	public void onCreate() {
//		HttpUtil.setVersion(Const.getAppVersionName(this));
//		DataService.setProvider(new RestService());
//		MessageManager.getInstance().initialize(this.getApplicationContext());
		super.onCreate();
		readDatabase();
//		try {
//			//for startup
//			ImageCacheParams cacheParams = new ImageCacheParams(this, IMAGE_CACHE_DIR);
//	        // Set memory cache to 25% of mem class
//	        cacheParams.setMemCacheSizePercent(this, 0.25f);
//			mImageCache = new ImageCache(cacheParams);
//			mImageCache.initDiskCacheAsync();
//			mMemoryCache = new LruCache<String, Object>(DEFAULT_MEM_CACHE_SIZE);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	
	public void removeActivity(Activity activity){
		activityList.remove(activity);
	}
	
	public int getActivityCount(){
		return activityList.size();
	}
	
	public Activity getTopActivity(){
		if (activityList.size() > 0) {
			return activityList.get(activityList.size() - 1);
		}
		return null;
	}
	
	// 遍历所有Activity并finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
	
	private void readDatabase() {
		try {
			mDBHelper = new DatabaseHelper(this);
			mUserDB = mDBHelper.getWritableDatabase();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(Const.TAG, "App readDatabse Exception:" + e.getMessage());
		}
	}
	
	public SQLiteDatabase getUserDB() {
		return mUserDB;
	}
	
	public DatabaseHelper getDBHelper() {
		return mDBHelper;
	}

}
