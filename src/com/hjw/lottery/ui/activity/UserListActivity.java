package com.hjw.lottery.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.hjw.lottery.App;
import com.hjw.lottery.R;
import com.hjw.lottery.rest.entities.User;
import com.hjw.lottery.ui.adapter.UserAdapter;

public class UserListActivity extends BaseActivity{
	
	ListView listView;
	
	List<User> mUsers = new ArrayList<User>();
	UserAdapter mUserAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		mUsers = App.getInstance().getDBHelper().getAllUser(App.getInstance().getUserDB());
		findViews();
		initView();
		initTitlebar();
	}
	
	private void initTitlebar() {
		setTitle("成员列表");
		getKechengActionBar().setRightText("添加成员");
		getKechengActionBar().getRightTextButton().setOnClickListener(l);
	}

	private void initView() {
		mUserAdapter = new UserAdapter(this, mUsers);
		listView.setAdapter(mUserAdapter);
	}

	private void findViews() {
		listView = (ListView) findViewById(R.id.list);
	}
	
    OnClickListener l = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.action_textview:
				Intent intent = new Intent(UserListActivity.this, AddUserActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	};
	
	protected void onResume() {
		super.onResume();
		mUsers.clear();
		mUsers.addAll(App.getInstance().getDBHelper().getAllUser(App.getInstance().getUserDB()));
		mUserAdapter.notifyDataSetChanged();
	};

}
