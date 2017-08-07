package com.xiangxun.wirlessorder.view;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.xiangxun.wirlessorder.data.DBHelper;

public class ListExpenditure extends Activity{
	private ListView listview;
	private DBHelper dbHelper;
	private SQLiteDatabase sqlDB;
	private Cursor mCursor;
	private SimpleCursorAdapter mSimpleCursorAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expenditure);
		listview = (ListView)findViewById(R.id.listview);
		showExperditureData();
	}
	public void showExperditureData(){
		dbHelper = new DBHelper(ListExpenditure.this);
		sqlDB = dbHelper.getWritableDatabase();
		String[] colums = new String[] {"_id", DBHelper.COL_USER_NAME, DBHelper.COL_CART_NAME, DBHelper.COL_CONSUME_TIME, DBHelper.COL_CONSUME_TOTAL};
		mCursor = sqlDB.query(DBHelper.TABLE_NAME, colums, null, null, null, null, null);
		String[] headers = new String[] {DBHelper.COL_USER_NAME, DBHelper.COL_CART_NAME, DBHelper.COL_CONSUME_TIME, DBHelper.COL_CONSUME_TOTAL};
		mSimpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.expenditure_item, mCursor, headers, 
				new int[]{R.id.text1, R.id.text2,  R.id.text3, R.id.text4});
		listview.setAdapter(mSimpleCursorAdapter);
	}
	//返回键的事件处理
	  @Override  
	  public boolean onKeyDown(int keyCode, KeyEvent event){  
       if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)  
       {  
      	Intent intent = new Intent(ListExpenditure.this, RestaurantJiMei.class);				
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			finish();
			System.exit(0);  
			return true;  
       }  
       return super.onKeyDown(keyCode, event);    
	  }
	
	
}
