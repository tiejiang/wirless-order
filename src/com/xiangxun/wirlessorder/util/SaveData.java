/**
 * 保存数据到数据库
 * */
package com.xiangxun.wirlessorder.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xiangxun.wirlessorder.data.DBHelper;

public class SaveData {
	private DBHelper dbHelper;
	private SQLiteDatabase sqlDb;
	public void saveExpenditureData(Context context, String username, String cartname, String time, String total){
		dbHelper = new DBHelper(context);
		sqlDb = dbHelper.getReadableDatabase();
		ContentValues cv = new ContentValues(4);
		cv.put(DBHelper.COL_USER_NAME, username);
		cv.put(DBHelper.COL_CART_NAME, cartname);
		cv.put(DBHelper.COL_CONSUME_TIME, time);
		cv.put(DBHelper.COL_CONSUME_TOTAL, total);
		sqlDb.insert(DBHelper.TABLE_NAME, null, cv);
	}
}
