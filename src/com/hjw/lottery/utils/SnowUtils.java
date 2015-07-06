package com.hjw.lottery.utils;



import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sunkai.snowdemo.SnowCoveredLandscapeView;

public class SnowUtils {
	
	private static SnowUtils tutorUtils;
	
	SnowCoveredLandscapeView snow;
	
	boolean isShow;

	public static final SnowUtils getInstance() {
		if (tutorUtils == null) {
			tutorUtils = new SnowUtils();
		}
		return tutorUtils;
	}
	
	public void showSnowView(Activity context, ViewGroup container){
//		final ViewGroup parent = (ViewGroup)context.getWindow().getDecorView();
		container.removeView(snow);
		isShow = true;
		
		snow = new SnowCoveredLandscapeView(context, true);
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		snow.setViewSize(dm.heightPixels, dm.widthPixels);
		mRedrawHandler.start(600);
		
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		container.addView(snow, lp);
	}
	
	public void removeSnowView(final Activity context, final ViewGroup container){
//		final ViewGroup parent = (ViewGroup)context.getWindow().getDecorView();
		snow.stop();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						container.removeView(snow);
						isShow = false;
					}
				});
			}
		}).start();
	}
	
	
	
	private RefreshHandler mRedrawHandler = new RefreshHandler();

	class RefreshHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			//snow.addRandomSnow();
			snow.refresh();
			if(isShow){
				sleep(40);
			}
		}
		
		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
		
		public void start(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};
	
	public boolean isShow(){
		return isShow;
	}

}
