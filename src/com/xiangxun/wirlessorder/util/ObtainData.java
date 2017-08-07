/**
 * 从服务端获取数据
 * 解析服务端的json格式数据
 * */
package com.xiangxun.wirlessorder.util;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xiangxun.wirlessorder.view.Menu;
import com.xiangxun.wirlessorder.view.TableSelect;

public class ObtainData {
	static String jsonStr = "";
	static String jsonTableInfoStr = "";
	// 获得json菜单
	public static String requestMenuContent(){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 添加请求参数
		params.add(new BasicNameValuePair("start", "0"));
		params.add(new BasicNameValuePair("retNums", "100"));
		params.add(new BasicNameValuePair("orderField", "carteno"));
		params.add(new BasicNameValuePair("orderDirection", "asc"));
		UrlEncodedFormEntity entity1=null;
		try {
			entity1 =  new UrlEncodedFormEntity(params,HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 请求服务器url
		String url = Utils.primitiveUrl + "cartes/getCartesByCondition";
		// 获得请求对象HttpPost
		HttpPost request = HttpUtil.getHttpPost(url);
		// 设置查询参数
		request.setEntity(entity1);
		// 获得响应结果
		String result= HttpUtil.queryStringForPost(request);
		return result;
    }
	// 解析json数据 (解析菜单数组)
	public static ArrayList<HashMap<String, Object>> parseJsonArray(String json){
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		try {
			JSONArray jsonArray = new JSONObject(json).getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = (JSONObject)jsonArray.get(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("menuId", jsonObj.getString("carteno"));
				map.put("dishName", jsonObj.getString("cartename"));
				map.put("dishPrice", jsonObj.getString("price"));
				list.add(map);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	//获得json桌号信息
	public static String requestTableInfo(){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 添加请求参数
		params.add(new BasicNameValuePair("retNums", "10"));
		params.add(new BasicNameValuePair("orderField", "dtno"));
		params.add(new BasicNameValuePair("orderDirection", "asc"));
		UrlEncodedFormEntity entity1=null;
		try {
			 entity1 =  new UrlEncodedFormEntity(params,HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 请求服务器url
		String url = Utils.primitiveUrl + "diningtables/getDiningtablesByCondition";
		// 获得请求对象HttpPost
		HttpPost request = HttpUtil.getHttpPost(url);
		// 设置查询参数
		request.setEntity(entity1);
		// 获得响应结果
		String result= HttpUtil.queryStringForPost(request);
		return result;
	}
	// 解析json数据 (解析桌号数组)
	public static ArrayList<HashMap<String, Object>> parseJsonTableArray(String json){
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		try {
			JSONArray jsonArray = new JSONObject(json).getJSONArray("dataList");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = (JSONObject)jsonArray.get(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("dtno", jsonObj.getString("dtno"));
				map.put("dtname", jsonObj.getString("dtname"));
				list.add(map);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	// 获得json评论
	public static String requestCommentContent(String carteno){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 添加请求参数
		params.add(new BasicNameValuePair("start", "0"));
		params.add(new BasicNameValuePair("retNums", "10"));//最多从服务端返回十条评论
		params.add(new BasicNameValuePair("orderField", "cptime"));
		params.add(new BasicNameValuePair("orderDirection", "desc"));
		params.add(new BasicNameValuePair("carteno", carteno));//相应菜单号的评论
		UrlEncodedFormEntity entity1=null;
		try {
			entity1 =  new UrlEncodedFormEntity(params,HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 请求服务器url
		String url = Utils.primitiveUrl + "fooddevalus/getFoodevaluationListCartenoByCondition";
		// 获得请求对象HttpPost
		HttpPost request = HttpUtil.getHttpPost(url);
		// 设置查询参数
		request.setEntity(entity1);
		// 获得响应结果
		String result= HttpUtil.queryStringForPost(request);
		return result;
    }
	// 解析json数据 (解析评论数组)
	public static String[] parseJsonCommentArray(String json) {
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONObject(json).getJSONArray("dataList");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] commentStr = new String[jsonArray.length()];
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObj = null;
			try {
				jsonObj = (JSONObject)jsonArray.get(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			try {
				commentStr[i] = jsonObj.getString("carteping");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return commentStr;
	}
	//提交评论到服务端
	public static void submitComment(final String cartname, final String username, final String comment){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 添加请求参数
		params.add(new BasicNameValuePair("carteno", cartname));
		params.add(new BasicNameValuePair("guuser", username));//最多从服务端返回十条评论
		params.add(new BasicNameValuePair("carteping", comment));
		UrlEncodedFormEntity entity1=null;
		try {
			entity1 =  new UrlEncodedFormEntity(params,HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 请求服务器url
		String url = Utils.primitiveUrl + "fooddevalus/createFoodevaluation";
		// 获得请求对象HttpPost
		HttpPost request = HttpUtil.getHttpPost(url);
		// 设置查询参数
		request.setEntity(entity1);
		// 获得响应结果
		String result= HttpUtil.queryStringForPost(request);
		Log.e("提交成功！", "ok!");
	}
	//上传组注册信息
	public static void postRegisterMsg(String usernameStr, String passwordStr, String nameStr, String phonenumStr){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 添加请求参数
		params.add(new BasicNameValuePair("guuser", usernameStr));
		params.add(new BasicNameValuePair("gupwd", passwordStr));//最多从服务端返回十条评论
		params.add(new BasicNameValuePair("guname", nameStr));
		params.add(new BasicNameValuePair("guphone", phonenumStr));
		UrlEncodedFormEntity entity1=null;
		try {
			entity1 =  new UrlEncodedFormEntity(params,HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 请求服务器url
		String url = Utils.primitiveUrl + "guinformations/getcreateGuinformationsByCondition";
		// 获得请求对象HttpPost
		HttpPost request = HttpUtil.getHttpPost(url);
		// 设置查询参数
		request.setEntity(entity1);
		// 获得响应结果
		String result= HttpUtil.queryStringForPost(request);
		Log.e("提交成功！", "ok!");
	}
	/**
	 * 上传远程预约信息
	 * @param 
	 * 
	 * 
	 * */
	public String remoteReservation(final String Subno, final String Subtime, final String Guuser, final String Subnums){
		String res = null;
		new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<NameValuePair> params = new ArrayList<NameValuePair>();
	    		// 添加请求参数
	    		params.add(new BasicNameValuePair("Subno", Subno));//预约号
	    		params.add(new BasicNameValuePair("Subtime", Subtime));//预约时间
	    		params.add(new BasicNameValuePair("Guuser", Guuser));//用户名
	    		params.add(new BasicNameValuePair("Subnums", Subnums));//人数
	    		UrlEncodedFormEntity entity1=null;
	    		try {
	 			entity1 =  new UrlEncodedFormEntity(params,HTTP.UTF_8);
	 		} catch (UnsupportedEncodingException e) {
	 			e.printStackTrace();
	 		}
	 		// 请求服务器url
	 		String url = Utils.primitiveUrl + "subscribes/getcreateSubscribesByCondition";
	 		// 获得请求对象HttpPost
	 		HttpPost request = HttpUtil.getHttpPost(url);
	 		// 设置查询参数
	 		request.setEntity(entity1);
	 		// 获得响应结果
			String result = HttpUtil.queryStringForPost(request);
			Log.e("提交成功！", "ok!");
			}
		}).start();
		return res;
		 
	}
	
}
