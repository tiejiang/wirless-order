package com.xiangxun.wirlessorder.view;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText userEditText, pwdEditText;
	private Button cancelButton, loginButton, registerButton;
	private String username, pwd;
	private static String jsonStr = "" ;
	private static final int MAX_REQUIRE_TIME = 3;
	private static final int SHOW_HINT_TIME = 500;
	private boolean isConnect;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		if (android.os.Build.VERSION.SDK_INT > 9) { 
			 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); 
			 StrictMode.setThreadPolicy(policy); 
			 } 
		
		//�������״̬
		ConnectivityManager con=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);  
		boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();  
		boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();  
		if(wifi|internet){  
		    //ִ����ز���  
			isConnect = true;
		}else{  
		    Toast.makeText(getApplicationContext(),  "������������", Toast.LENGTH_LONG).show();  
		    isConnect = false;
		}
		
		userEditText = (EditText)findViewById(R.id.userEditText);
		pwdEditText = (EditText)findViewById(R.id.pwdEditText);
		cancelButton = (Button)findViewById(R.id.cancelButton);
		loginButton = (Button)findViewById(R.id.loginButton);
		registerButton = (Button)findViewById(R.id.registerButton);
		
		cancelButton.setOnClickListener(this);
		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.loginButton:
			if (isConnect) {
				if (validate()) {
					if (login()) {
						Toast.makeText(LoginActivity.this, "��¼ʧ�ܣ�", SHOW_HINT_TIME).show();
					}else {
						saveUserMsg(username, pwd);
						//��½�������� ����������
						Intent intent = new Intent();
						intent.setClass(LoginActivity.this, MainActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
					}
				}
			}else {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("����")
				.setMessage("�����Ƿ���������")
				.setPositiveButton("ȷ��", null);
				builder.setCancelable(false);
				builder.create()
				.show();
			}
			
			break;

		case R.id.cancelButton:
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, MainActivity.class);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			finish();
			System.exit(0);
			break;
		case R.id.registerButton:
			//ע���ҵ���߼�
			LayoutInflater layoutInflater = LayoutInflater.from(LoginActivity.this);
			final View registerView = layoutInflater.inflate(R.layout.register, null);
			showRegister(registerView);
			break;
		}
	}
	//ע���dialog �� view
	private void showRegister(final View view){
		AlertDialog builder = new AlertDialog.Builder(LoginActivity.this)
		.setView(view)
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				EditText username = (EditText)view.findViewById(R.id.username);
				EditText password = (EditText)view.findViewById(R.id.password);
				EditText name = (EditText)view.findViewById(R.id.name);
				EditText phonenum = (EditText)view.findViewById(R.id.phonenum);
				
				String usernameStr = username.getText().toString().trim();
				String passwordStr = password.getText().toString().trim();
				String nameStr = name.getText().toString().trim();
				String phonenumStr = phonenum.getText().toString().trim();
				//�ϴ�ע����Ϣ
				ObtainData.postRegisterMsg(usernameStr, passwordStr, nameStr, phonenumStr);
			}
		})
		.setNegativeButton("ȡ��", null)
		.create();
		builder.show();
	}
	// ��֤����  �����Ƿ�Ϊ��
	private boolean validate(){
		username = userEditText.getText().toString().trim();
		if(username.equals("")){
			Toast.makeText(LoginActivity.this, "�û������Ǳ����", SHOW_HINT_TIME).show();
			return false;
		}
		pwd = pwdEditText.getText().toString().trim();
		if(pwd.equals("")){
			Toast.makeText(LoginActivity.this, "�û������Ǳ�����!", SHOW_HINT_TIME).show();
			return false;
		}
		return true;
	}
	// ��¼
	private boolean login(){
		jsonStr = queryForHttpPost(username, pwd);
		Log.e("json����:", jsonStr);
		return parseJsonArray(jsonStr);
	}
	// �������� �����Ӧ
	private String queryForHttpPost(String account, String password){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// ����������
		params.add(new BasicNameValuePair("guuser", account));
		params.add(new BasicNameValuePair("gupwd", password));
		UrlEncodedFormEntity entity1=null;
		try {
			 entity1 =  new UrlEncodedFormEntity(params,HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// ���������url
		String url = Utils.primitiveUrl + "guinformations/getGuinformationsByCondition";
		// ����������HttpPost
		HttpPost request = HttpUtil.getHttpPost(url);
		// ���ò�ѯ����
		request.setEntity(entity1);
		// �����Ӧ���
		String result= HttpUtil.queryStringForPost(request);
		return result;
	}
	// �����û�������
	private void saveUserMsg(String username, String pwd){
		String id = username;
		String name = pwd;
		// ������Ϣ
		SharedPreferences pre = getSharedPreferences("user_msg", MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor editor = pre.edit();
		editor.putString("id", id);
		editor.putString("name", name);
		editor.commit();
	}
	// ����json���� (������������)
//	private void parseJson(String json){
//		String username = null;
//		String userpwd = null;
//		JSONObject jsonObject;
//		try {
//			jsonObject = new JSONObject(json);
//			username = jsonObject.getString("msg");
////			userpwd = jsonObject.getString("gupwd");
//			Log.v("TAG ��������", username);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	// ����json���� (��������)
	private boolean parseJsonArray(String json){
		boolean isnull = false;//��ʶjsonArray�Ƿ�Ϊ��
		String username = "";
		String userpwd = "";
		try {
			JSONArray jsonArray = new JSONObject(json).getJSONArray("dataList");
			if (jsonArray.isNull(0)) {
				isnull = true;
			}else {
				isnull = false;
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = (JSONObject)jsonArray.get(i);
					username = jsonObj.getString("guuser");
					userpwd = jsonObj.getString("gupwd");
					Log.v("TAG Array", "name:" + username + "password:" + userpwd);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isnull;
	}
}
