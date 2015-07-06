package com.hjw.lottery.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.hjw.lottery.App;
import com.hjw.lottery.R;
import com.hjw.lottery.rest.entities.User;
import com.hjw.lottery.utils.CommonUtils;

import fm.jihua.common.ui.helper.Hint;

public class AddUserActivity extends BaseActivity{
	
	Button mButton;
	EditText mResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_user);
		findViews();
		initView();
		initTitlebar();
	}
	
	private void initTitlebar() {
		setTitle("添加成员");
		getKechengActionBar().setRightText("联系人");
		getKechengActionBar().getRightTextButton().setOnClickListener(l);
	}

	private void initView() {
		mButton.setOnClickListener(l);
	}

	private void findViews() {
		mButton = (Button) findViewById(R.id.button);
		mResult = (EditText) findViewById(R.id.result);
	}
	
	OnClickListener l = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button:
				Hint.showTipsShort(AddUserActivity.this, "commit");
				String name = mResult.getText().toString().trim();
				if(CommonUtils.isNullString(name)){
					Hint.showTipsShort(AddUserActivity.this,"请输入名字");
					return;
				}
				App.getInstance().getDBHelper().addUser(App.getInstance().getUserDB(), new User(name));
				Hint.showTipsShort(AddUserActivity.this,"添加" + name + "成功");
				mResult.setText("");
				break;
			case R.id.action_textview:
				Hint.showTipsShort(AddUserActivity.this, "联系人");
				Intent intent = new Intent(AddUserActivity.this, ContactsActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	};
}
