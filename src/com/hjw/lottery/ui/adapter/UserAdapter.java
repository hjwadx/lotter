package com.hjw.lottery.ui.adapter;

import java.util.List;

import com.hjw.lottery.App;
import com.hjw.lottery.R;
import com.hjw.lottery.rest.entities.User;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserAdapter extends BaseAdapter{
	
	Activity mActivity;
	List<User> users;
	boolean hide_button;
	
	public UserAdapter(Activity parent, List<User> users) {
		this.mActivity = parent;
		this.users = users;
	}

	@Override
	public int getCount() {
		return users.size();
	}

	@Override
	public Object getItem(int position) {
		return users.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setHideButton(boolean hide){
		hide_button = hide;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
			holder = new ViewHolder();
			holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.remove = (Button) convertView.findViewById(R.id.remove);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		final User user = users.get(position);
		holder.name.setText(user.name);
		if(hide_button){
			holder.remove.setVisibility(View.GONE);
		}
		holder.remove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				App.getInstance().getDBHelper().deleteUser(App.getInstance().getUserDB(), user.id);
				users.remove(user);
				mActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						notifyDataSetChanged();
					}
				});
			}
		});
		return convertView;
	}
	
	static class ViewHolder{
		ImageView avatar;
		TextView name;
		Button remove;
	}

}
