package com.hjw.lottery.ui.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.hjw.lottery.App;
import com.hjw.lottery.R;
import com.hjw.lottery.rest.entities.User;
import com.hjw.lottery.ui.view.GifMovieView;
import com.hjw.lottery.utils.Compatibility;
import com.hjw.lottery.utils.SnowUtils;

import fm.jihua.common.ui.helper.Hint;

public class MainActivity extends BaseActivity implements SensorEventListener{
	
	Button mButton;
	TextView mResult;
	GifMovieView mGifView;
	
	List<User> mUsers = new ArrayList<User>();
	
	boolean isLotterying;
	
	//定义sensor管理器
    private SensorManager mSensorManager;
    
	// 震动
	private Vibrator vibrator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mUsers = App.getInstance().getDBHelper().getAllUser(App.getInstance().getUserDB());
		//获取传感器管理服务
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		//震动
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

		findViews();
		initView();
		initTitlebar();
	}

	private void initTitlebar() {
		setTitle("抽签下楼拿饭啦");
		getKechengActionBar().setLefttButtonGone();
		getKechengActionBar().setRightText("查看成员");
		getKechengActionBar().getRightTextButton().setOnClickListener(l);
		
	}

	private void initView() {
		mButton.setOnClickListener(l);
	}

	private void findViews() {
		mButton = (Button) findViewById(R.id.button);
		mResult = (TextView) findViewById(R.id.result);
		mGifView = (GifMovieView) findViewById(R.id.gif);
	}
	
	OnClickListener l = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.action_textview:
				Intent intent = new Intent(MainActivity.this, UserListActivity.class);
				startActivity(intent);
				break;
			case R.id.button:
				if(mUsers == null || mUsers.size() == 0){
					Hint.showTipsShort(MainActivity.this, "一个成员都没有，先添加成员才能抽签哦！");
					return;
				}
				if(isLotterying){
					return;
				}
				isLotterying = true;
				Animation shake = AnimationUtils.loadAnimation(App.getInstance(), R.anim.shake);
				shake.setDuration(1500);
				shake.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						mResult.setText("正在抽签");
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						lottery();
					}
				});
				mResult.startAnimation(shake);
				break;
			default:
				break;
			}
		}
	};
	
	private void lottery(){
		int result = (int)(Math.random()*mUsers.size());
		if("刘晨".equals(mUsers.get(result).name) || mUsers.get(result).name.contains("晨")){
			if((int)(Math.random()*100)%3 != 0){
				result = (int)(Math.random()*mUsers.size());
			}
		}
		mResult.setText(mUsers.get(result).name);
		isLotterying = false;
		
		if(isChristmas()){
			showSnow();
		} else {
			if((int)(Math.random()*100)%10 != 0){
				showGif();
			} else {
				showSnow();
			}
		}
	}
	
	boolean isChristmas(){
		Calendar cal = Calendar.getInstance();
		if(cal.get(Calendar.MONTH) == 11 && cal.get(Calendar.DAY_OF_MONTH) < 26 && cal.get(Calendar.DAY_OF_MONTH) > 22) {
			return true;
		}
		return false;
	}
	
	private void showSnow() {
		if(SnowUtils.getInstance().isShow()){
			return;
		}
		SnowUtils.getInstance().showSnowView(this, (ViewGroup)getWindow().getDecorView());
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				SnowUtils.getInstance().removeSnowView(MainActivity.this, (ViewGroup)getWindow().getDecorView());
			}
		}).start();
	}

	private void showGif() {
		mGifView.setVisibility(View.VISIBLE);
		mGifView.setPaused(true);
		mGifView.setMovieResource(R.drawable.laugh);
		mGifView.setPaused(false);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						mGifView.setVisibility(View.GONE);
						mGifView.setMovie(null);
					}
				});
			}
		}).start();
	}

	protected void onResume() {
		super.onResume();
		mUsers.clear();
		mUsers.addAll(App.getInstance().getDBHelper().getAllUser(App.getInstance().getUserDB()));
		
		// 加速度传感器
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				// 还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，
				// 根据不同应用，需要的反应速率不同，具体根据实际情况设定
				SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onStop(){
	  mSensorManager.unregisterListener(this);
	  super.onStop();
	}

	@Override
	protected void onPause(){
	  mSensorManager.unregisterListener(this);
	  super.onPause();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		int sensorType = event.sensor.getType();

		// values[0]:X轴，values[1]：Y轴，values[2]：Z轴
		float[] values = event.values;

		if (sensorType == Sensor.TYPE_ACCELEROMETER) {

			/*
			 * 因为一般正常情况下，任意轴数值最大就在9.8~10之间，只有在你突然摇动手机的时候，瞬时加速度才会突然增大或减少。
			 * 所以，经过实际测试，只需监听任一轴的加速度大于14的时候，改变你需要的设置就OK了~~~
			 */
			if ((Math.abs(values[0]) > 12 || Math.abs(values[1]) > 12 || Math.abs(values[2]) > 12)) {

				// 摇动手机后,do something
				Compatibility.callOnClick(mButton);
//				mButton.callOnClick();
//				mButton.performClick();

				// 摇动手机后，再伴随震动提示~~
				vibrator.vibrate(500);

			}
		}

		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		//当传感器精度改变时回调该方法，Do nothing.
	};
}
