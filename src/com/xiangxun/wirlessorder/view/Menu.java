package com.xiangxun.wirlessorder.view;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xiangxun.wirlessorder.util.HttpUtil;
import com.xiangxun.wirlessorder.util.ObtainData;
import com.xiangxun.wirlessorder.util.SaveData;
import com.xiangxun.wirlessorder.util.Utils;

public class Menu extends Activity implements OnClickListener {
	private GridView gridview;
	private ListView list;//抽屉数据列表
	private Button btndeal, btnselect;
	private SimpleAdapter simpleAdapter;
	private ArrayList<HashMap<String, Object>> listItem;//gridview的listItem
	private SimpleAdapter adapter;
	private List<HashMap<String, Object>> serverMenuList = new ArrayList();//接收服务端的菜单
	private List<HashMap<String, Object>> menuList = new ArrayList();//封装已点菜map的menulist
	private HashMap<String, Object> vegmap;//点菜的vegmap
	private int finalPrize;//订单的最终价格
	private String userName;

    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);
        //解决网络异常
        if (android.os.Build.VERSION.SDK_INT > 9) { 
			 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); 
			 StrictMode.setThreadPolicy(policy); 
			 } 
        list = (ListView)findViewById(R.id.list);
        btndeal = (Button)findViewById(R.id.btndeal);
        btnselect = (Button)findViewById(R.id.btnselect);
       
        btndeal.setOnClickListener(this);
        btnselect.setOnClickListener(this);
        //获得GridView组件
        gridview = (GridView) findViewById(R.id.gridview);
        listItem = new ArrayList<HashMap<String,Object>>();
        //加载、解析服务端数据
        serverMenuList = ObtainData.parseJsonArray(ObtainData.requestMenuContent());
        
		for (int i = 0; i < serverMenuList.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("image",  Utils.vegImage[i]);
			map.put("name", serverMenuList.get(i).get("dishName"));
			map.put("price", "¥："  + serverMenuList.get(i).get("dishPrice"));
			map.put("vegid", "编号：" + serverMenuList.get(i).get("menuId"));
			listItem.add(map);
		}
		adapter = new SimpleAdapter(this, listItem, R.layout.menu_gridview,
				new String[] {"image", "name", "price", "vegid"}, 
				new int[] {R.id.image, R.id.name, R.id.price, R.id.vegid});
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new ItemClickListener());
		gridview.setOnItemLongClickListener(new ItemLongClickListener());
       }
    //长按菜品显示菜品评论以及参加评论
    class ItemLongClickListener implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
			HashMap<String, Object> item = (HashMap<String, Object>)arg0.getItemAtPosition(arg2);
			final String cartName = ((String)item.get("vegid")).substring(3);
			//System.out.println("menuId" + cartName);
			String[] commentStr = ObtainData.parseJsonCommentArray(ObtainData.requestCommentContent(cartName));
			AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
			builder.setTitle("用户评论");
			builder.setItems(commentStr, null);
			builder.setNegativeButton("确定", null);
			builder.setPositiveButton("写评论去？", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					editComment(cartName, userName);
				}
			});
			builder.create();
			builder.show();
			
			return false;
		}
    }
    //编写评论
    public void editComment(final String cartName, final String userName){
    	LayoutInflater inflater = LayoutInflater.from(Menu.this);
    	final View commentView = inflater.inflate(R.layout.dialog, null);
    	AlertDialog alertDialog = new AlertDialog.Builder(Menu.this)
    	.setTitle("添加评论")
    	.setView(commentView)
    	.setPositiveButton("提交", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				EditText edit = (EditText)commentView.findViewById(R.id.editText);
				String commentStr = edit.getText().toString();
				Log.e("comment content", commentStr);
				//调用上载数据的方法
				ObtainData.submitComment(cartName, userName, commentStr);
			}
		})
		.setNegativeButton("取消", null)
		.create();
		alertDialog.show();
    }

    //菜品选择以及选择后长按删除
	class ItemClickListener implements OnItemClickListener{
		int prize = 0; //接收删除修改后的订单价格
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (TableSelect.isSelect) {
				System.out.println("TableSelect.isSelect:" + TableSelect.isSelect);
				// 桌位选择后才能点餐
				HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);  
			    //显示当前所选菜名 
				String vegName = (String)item.get("name");
				String vegPrice = (String)item.get("price");
				Log.e("抽屉的菜名和单价：", vegName + vegPrice);
				Toast.makeText(Menu.this, "您已选择了一道:" + vegName, Toast.LENGTH_SHORT).show();
				//封装所选菜名
				vegmap = new HashMap<String, Object>();
				vegmap.put("name", vegName);
				vegmap.put("price", vegPrice);
				menuList.add(vegmap);
				//给抽屉绑定数据	
				simpleAdapter = new SimpleAdapter(Menu.this, menuList, 
						R.layout.orderedtext, 
						new String[]{"name", "price"},
						new int[]{R.id.text, R.id.price});
				list.setAdapter(simpleAdapter);
				//计算总价
				int total = calculatePrize();
				//长按删除所点菜品
				list.setOnItemLongClickListener(new OnItemLongClickListener() {
		            @Override
		            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		            	final int deleteId = position;
		    			AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
		    			builder.setTitle("提示").setMessage("要移除订单中这道菜吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
				    			//HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);  
								menuList.remove(deleteId);
								//若订单中有删除菜品操作则再次调用计算单价的方法
								finalPrize = calculatePrize();
				                simpleAdapter.notifyDataSetChanged();
							}
						}).setNegativeButton("取消", null).show();
		                return true;
		            }
		        });
				if (total != prize && prize != 0) {
					finalPrize = prize;
				}else {
					finalPrize = total;
				}
			}else {
				AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
				builder.setMessage("您还没有选择桌位哦！")
				.setPositiveButton("选桌位去！", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setClass(Menu.this, TableSelect.class);
						startActivity(intent);
						overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
						finish();
					}
				}).show();
			}
		}
    }
	
	/*
	 * 获取原始单价数组来计算或者从menuList里面得到String类型再转型后计算----总价
	 * 使用后者的方法
	 * */
	private int calculatePrize(){
		int totalPrize = 0;
		float[] unitPrize = new float[menuList.size()];
		for (int i = 0; i < menuList.size(); i++) {
			Map map = (HashMap)menuList.get(i);
			String netPrize = (String)map.get("price");
			String unit = netPrize.substring(2);//去掉“¥：”符号
			unitPrize[i] = Float.parseFloat(unit);
			//System.out.println("单次点击价格信息：" + unitPrize);
			totalPrize += unitPrize[i];
		}
		System.out.println("打印价格信息：" + totalPrize);
		return totalPrize;
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btndeal:
			//下单  已点菜品的数据封装上传
			String str = String.valueOf(finalPrize);
			showDialog("确认订单, 本次消费：" + str + "元");
			break;

		case R.id.btnselect:
			Intent intent = new Intent();
			intent.setClass(Menu.this, TableSelect.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			break;
		}
		
	}
	private void showDialog(String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg)
		       .setCancelable(false)
		       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   String nameStr = "";
		        	   String price = String.valueOf(finalPrize);
		        	   //上传订单数据
		        	   for (int i = 0; i < menuList.size(); i++) {
			        		// 获得其中点菜map
			   				Map map = (Map)menuList.get(i);
			   				// 获得点菜项
							String name = (String) map.get("name");
							nameStr = nameStr + "," + name;
							Log.e("上传订单中的菜名：", name + price);
		        	   }
		        	   //获得用户信息
		        	   SharedPreferences sp = Menu.this.getSharedPreferences("user_msg", MODE_WORLD_WRITEABLE);
		        	   userName = sp.getString("name", "0");
		        	   //系统当前时间
		        	   String currentTime = Utils.obtainCurrentTime();
		        	   Log.v("用户名、当前时间", userName + currentTime);
		        	   
		        	   List<NameValuePair> params = new ArrayList<NameValuePair>();
			       		// 添加请求参数
			       		params.add(new BasicNameValuePair("carteno", nameStr));
			       		params.add(new BasicNameValuePair("pricsum", price));
			       		params.add(new BasicNameValuePair("indentno", userName + currentTime));
			       		params.add(new BasicNameValuePair("dtno", TableSelect.selectedTableId));
			       		UrlEncodedFormEntity entity1=null;
			       		try {
			    			entity1 =  new UrlEncodedFormEntity(params,HTTP.UTF_8);
			    		} catch (UnsupportedEncodingException e) {
			    			e.printStackTrace();
			    		}
			    		// 请求服务器url
			    		String url = Utils.primitiveUrl + "indents/getcreateIndentsByCondition";
			    		// 获得请求对象HttpPost
			    		HttpPost request = HttpUtil.getHttpPost(url);
			    		// 设置查询参数
			    		request.setEntity(entity1);
						Toast.makeText(Menu.this, "上传成功！", Toast.LENGTH_LONG).show();
						//保存订单到数据库
						new SaveData().saveExpenditureData(getApplicationContext(), userName, nameStr, currentTime, price);
						//finish();
		           }
		       }).setNegativeButton("取消", null);
		AlertDialog alert = builder.create();
		alert.show();
	}
	//返回键的事件处理
		@Override  
	  public boolean onKeyDown(int keyCode, KeyEvent event){  
         if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)  
         {  
        	Intent intent = new Intent(Menu.this, RestaurantJiMei.class);				
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			finish();
			System.exit(0);  
			return true;  
         }  
         return super.onKeyDown(keyCode, event);    
	  }
	
}
      
    
