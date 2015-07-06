package com.hjw.lottery.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hjw.lottery.rest.entities.User;

import fm.jihua.common.utils.AppLogger;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_USER_FILE = "lottery";
	public static final String DATABASE_TABLE_USERS = "users";

	private final static String DATABASE_NAME = DATABASE_USER_FILE;
	private final static int DATABASE_VERSION = 1;
	private final String TAG = getClass().getSimpleName();

	private Object userLock = new Object();

//	App mApp;
	// private Object transactionLock = new Object();

	Context mContext;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.mContext = context;
//		mApp = (App) mContext.getApplicationContext();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String user_create = "CREATE TABLE IF NOT EXISTS "
				+ DATABASE_TABLE_USERS
				+ "(ID INTEGER PRIMARY KEY,NAME TEXT);";
		db.beginTransaction();
		try {
			// Create tables & test data
			db.execSQL(user_create);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			Log.e(TAG, "Error creating tables and debug data" + e.getMessage());
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= newVersion) {
			return;
		}
		db.beginTransaction();
		try {
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			Log.e(TAG, "Error creating tables and debug data" + e.getMessage());
		} finally {
			db.endTransaction();
		}
	}

	public void cleanData(SQLiteDatabase db) {
		execSQL(db, "delete from " + DATABASE_TABLE_USERS + ";");
	}

	public void execSQL(SQLiteDatabase db, String sql) {
		db.beginTransaction();
		try {
			// Create tables & test data
			db.execSQL(sql);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			Log.e(TAG, "Error execSQL " + sql + ";ErrorMessage:" + e.getMessage());
		} finally {
			db.endTransaction();
		}
	}

	public void execSQL(SQLiteDatabase db, String sql, Object[] params) {
		db.beginTransaction();
		try {
			// Create tables & test data
			db.execSQL(sql, params);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			Log.e(TAG, "Error execSQL " + sql + ";ErrorMessage:" + e.getMessage());
		} finally {
			db.endTransaction();
		}
	}
	
	public List<User> getAllUser(SQLiteDatabase db) {
		synchronized (userLock) {
			List<User> list = new ArrayList<User>();
			try {
				Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE_USERS, null);
				int count = cursor.getCount();
				if (count > 0) {
					cursor.moveToFirst();
					while (!cursor.isAfterLast()) {
						User user = new User(cursor.getInt(0), cursor.getString(1));
						list.add(user);
						cursor.moveToNext();
					}
				}
				cursor.close();
			} catch (SQLException e) {
				AppLogger.e(TAG, "Error getRecords" + e.getMessage());
			}
			AppLogger.d(TAG, "after sync getCourses");
			return list;
		}
	}
	
	public void addUser(SQLiteDatabase db, User user) {
		try {
			if (!existRecord(db, DATABASE_TABLE_USERS, user.name)) {
				db.execSQL(
						"INSERT INTO "
								+ DATABASE_TABLE_USERS
								+ "(NAME) "
								+ "VALUES(?);",
						new Object[] { user.name});
			}
		} catch (SQLException e) {
			AppLogger.e(TAG, e.getMessage(), e);
		}
	}
	
	public void deleteUser(SQLiteDatabase db, int userId) {
		try {
			db.execSQL("delete from " + DATABASE_TABLE_USERS
					+ " where ID = ? ", new String[] {
					String.valueOf(userId) });
		} catch (SQLException e) {
			AppLogger.e(TAG, "Error execSQL deleteUser:" + userId
					+ ";ErrorMessage:" + e.getMessage());
		} finally {

		}
	}
	
	public boolean existRecord(SQLiteDatabase db, String table, String name) {
		boolean result = false;
		try {
			// ID INTEGER PRIMARY KEY, COURSEID INTEGER, CONTENT TEXT, CRATORID
			// INTEGER, CREATED_AT LONG
			Cursor cursor = db.rawQuery("SELECT 1 FROM " + table
					+ " where name = ?;", new String[] { name });
			result = cursor.getCount() > 0;
			cursor.close();
		} catch (SQLException e) {
			AppLogger.e(TAG, e.getMessage(), e);
		}
		return result;
	}

}
