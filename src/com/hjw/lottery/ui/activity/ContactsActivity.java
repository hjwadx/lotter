package com.hjw.lottery.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hjw.lottery.App;
import com.hjw.lottery.R;
import com.hjw.lottery.rest.entities.User;
import com.hjw.lottery.ui.adapter.UserAdapter;
import com.hjw.lottery.utils.ContactsUtils;

import fm.jihua.common.ui.helper.Hint;

public class ContactsActivity extends BaseActivity{
	
	ListView listView;

	List<User> mUsers = new ArrayList<User>();
	UserAdapter mUserAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		getContacts();
		findViews();
		initView();
		initTitlebar();
	}
	
	private void getContacts() {
		mUsers = ContactsUtils.getAllContacts(this);
	}

	private void initTitlebar() {
		setTitle("选取联系人");
	}

	private void initView() {
		mUserAdapter = new UserAdapter(this, mUsers);
		mUserAdapter.setHideButton(true);
		listView.setAdapter(mUserAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				User user = mUsers.get(position);
				App.getInstance().getDBHelper().addUser(App.getInstance().getUserDB(), user);
				Hint.showTipsShort(ContactsActivity.this,"添加" + user.name + "成功");
			}
		});
	}

	private void findViews() {
		listView = (ListView) findViewById(R.id.list);
	}

}
