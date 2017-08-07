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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xiangxun.wirlessorder.util.HttpUtil;
import com.xiangxun.wirlessorder.util.ObtainData;
import com.xiangxun.wirlessorder.util.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TableSelect extends Activity {
	private GridView tableGridView;
	private List<HashMap<String, Object>> serverTableList = new ArrayList();//从服务端得到的座位id
	private SimpleAdapter simpleAdapter;
	private List<HashMap<String, Object>> list = new ArrayList();
	private HashMap<String, Object> map ;
	private AlertDialog.Builder builder;
	public static String selectedTableId = null;
	public static boolean isSelect = false;//桌号是否已经选择
	private String id = null;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table);
		//解决网络异常
		if (android.os.Build.VERSION.SDK_INT > 9) { 
			 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); 
			 StrictMode.setThreadPolicy(policy); 
			 } 
		tableGridView = (GridView)findViewById(R.id.tableGridView);
		//获得服务端桌号信息
		System.out.println("桌号信息： " + ObtainData.requestTableInfo());
		serverTableList = ObtainData.parseJsonTableArray(ObtainData.requestTableInfo());
		for (int i = 0; i < serverTableList.size(); i++) {
			map = new HashMap();
			//是空桌位才加载
			if (((String)(serverTableList.get(i).get("dtname"))).equals("0")) {
				map.put("tableImage", R.drawable.tableorder);
				map.put("tableId", "桌号:" + serverTableList.get(i).get("dtno"));
				list.add(map);
			}else {
				continue;
			}
		}
		simpleAdapter = new SimpleAdapter(this, list, R.layout.table_item, 
				new String[] {"tableImage", "tableId", "tableStatus"}, 
				new int[] {R.id.tableImage, R.id.tableId});
		tableGridView.setAdapter(simpleAdapter);
		//选择座位监听
		tableGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//选择桌号
				HashMap<String, Object> tableId = (HashMap<String, Object>)arg0.getItemAtPosition(arg2);
				if (selectedTableId  == null) {
					id = ((String)tableId.get("tableId")).substring(3);
					Log.e("getTable ID", id);
					confirmMessage("确定选择当前桌位吗？", id);
					simpleAdapter.notifyDataSetChanged();//更新数据
					selectedTableId = id;
				}else {
					AlertDialog.Builder builderHint = new AlertDialog.Builder(TableSelect.this);
					builderHint.setTitle("您已经选择了桌位了！")
					.setMessage("需要重新选择吗？")
//					.setPositiveButton("确定", new DialogInterface.OnClickListener(){
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							Log.e("首次选择的dtno", id);
//							// TODO Auto-generated method stub
//							// 绑定账号和桌位号 上载服务端
//							List<NameValuePair> params = new ArrayList<NameValuePair>();
//							// 添加请求参数
//							params.add(new BasicNameValuePair("dtno", id));
//							params.add(new BasicNameValuePair("dtname", "0"));
//							UrlEncodedFormEntity entity1=null;
//							try {
//								entity1 =  new UrlEncodedFormEntity(params,HTTP.UTF_8);
//							} catch (UnsupportedEncodingException e) {
//								e.printStackTrace();
//							}
//							// 请求服务器url
//							String url = Utils.primitiveUrl + "diningtables/updateDiningtables";
//							// 获得请求对象HttpPost
//							HttpPost request = HttpUtil.getHttpPost(url);
//							// 设置查询参数
//							request.setEntity(entity1);
//							// 获得响应结果
//							String result= HttpUtil.queryStringForPost(request);
//							//重新从服务端获取更新的桌号信息
//							serverTableList = ObtainData.parseJsonTableArray(ObtainData.requestTableInfo());
//							simpleAdapter.notifyDataSetChanged();
//							isSelect = false;
//						}
//					})
					.setNegativeButton("取消", null)
					.show();
				}
				//finish();
				Log.e("全局的tableid值：", selectedTableId);
			}
		});
	}
	//弹出框 上载桌位信息
	private void confirmMessage(String msg, final String id){
		builder = new AlertDialog.Builder(this);
		builder.setTitle(msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// 绑定账号和桌位号 上载服务端
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				// 添加请求参数
				params.add(new BasicNameValuePair("dtno", id));
				params.add(new BasicNameValuePair("dtname", "1"));
				UrlEncodedFormEntity entity1=null;
				try {
					entity1 =  new UrlEncodedFormEntity(params,HTTP.UTF_8);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				// 请求服务器url
				String url = Utils.primitiveUrl + "diningtables/updateDiningtables";
				// 获得请求对象HttpPost
				HttpPost request = HttpUtil.getHttpPost(url);
				// 设置查询参数
				request.setEntity(entity1);
				// 获得响应结果
				String result= HttpUtil.queryStringForPost(request);
				if (result != null) {
					Toast.makeText(TableSelect.this, "数据上传成功！", Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(TableSelect.this, "数据上传失败！", Toast.LENGTH_LONG).show();
				}
				isSelect = true;
				finish();
				Intent  intent = new Intent();
				intent.setClass(TableSelect.this, Menu.class);
				startActivity(intent);
				finish();
			}
		}).setNegativeButton("取消重选", null).show();
		
	}
	 //返回键的事件处理
	  @Override  
	  public boolean onKeyDown(int keyCode, KeyEvent event){  
         if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)  
         {  
        	Intent intent = new Intent(TableSelect.this, Menu.class);				
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			finish();
			System.exit(0);  
			return true;  
         }  
         return super.onKeyDown(keyCode, event);    
	  }
}
