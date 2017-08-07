package com.xiangxun.wirlessorder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "myData";//历史消费统计表
	private static final int DATABASE_VERSION = 1;
	//行程规划表的属性值
	public static final String TABLE_NAME = "TravelRoutePlanning";
	public static final String COL_USER_NAME = "pName";
	public static final String COL_CART_NAME = "cartName";
	public static final String COL_CONSUME_TIME = "consumeTime";
	public static final String COL_CONSUME_TOTAL = "totalConsume";
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE "
			+ TABLE_NAME
			+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COL_USER_NAME + " TEXT, "
			+ COL_CART_NAME + " TEXT, "
			+ COL_CONSUME_TIME + " TEXT, "
			+ COL_CONSUME_TOTAL + " TEXT"
			+ ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
	
}
