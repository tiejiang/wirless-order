/**
 * �ӷ���˻�ȡ����
 * ��������˵�json��ʽ����
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
	// ���json�˵�
	public static String requestMenuContent(){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// ����������
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
		// ���������url
		String url = Utils.primitiveUrl + "cartes/getCartesByCondition";
		// ����������HttpPost
		HttpPost request = HttpUtil.getHttpPost(url);
		// ���ò�ѯ����
		request.setEntity(entity1);
		// �����Ӧ���
		String result= HttpUtil.queryStringForPost(request);
		return result;
    }
	// ����json���� (�����˵�����)
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
	//���json������Ϣ
	public static String requestTableInfo(){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// ����������
		params.add(new BasicNameValuePair("retNums", "10"));
		params.add(new BasicNameValuePair("orderField", "dtno"));
		params.add(new BasicNameValuePair("orderDirection", "asc"));
		UrlEncodedFormEntity entity1=null;
		try {
			 entity1 =  new UrlEncodedFormEntity(params,HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// ���������url
		String url = Utils.primitiveUrl + "diningtables/getDiningtablesByCondition";
		// ����������HttpPost
		HttpPost request = HttpUtil.getHttpPost(url);
		// ���ò�ѯ����
		request.setEntity(entity1);
		// �����Ӧ���
		String result= HttpUtil.queryStringForPost(request);
		return result;
	}
	// ����json���� (������������)
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
	// ���json����
	public static String requestCommentContent(String carteno){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// ����������
		params.add(new BasicNameValuePair("start", "0"));
		params.add(new BasicNameValuePair("retNums", "10"));//���ӷ���˷���ʮ������
		params.add(new BasicNameValuePair("orderField", "cptime"));
		params.add(new BasicNameValuePair("orderDirection", "desc"));
		params.add(new BasicNameValuePair("carteno", carteno));//��Ӧ�˵��ŵ�����
		UrlEncodedFormEntity entity1=null;
		try {
			entity1 =  new UrlEncodedFormEntity(params,HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// ���������url
		String url = Utils.primitiveUrl + "fooddevalus/getFoodevaluationListCartenoByCondition";
		// ����������HttpPost
		HttpPost request = HttpUtil.getHttpPost(url);
		// ���ò�ѯ����
		request.setEntity(entity1);
		// �����Ӧ���
		String result= HttpUtil.queryStringForPost(request);
		return result;
    }
	// ����json���� (������������)
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
	//�ύ���۵������
	public static void submitComment(final String cartname, final String username, final String comment){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// ����������
		params.add(new BasicNameValuePair("carteno", cartname));
		params.add(new BasicNameValuePair("guuser", username));//���ӷ���˷���ʮ������
		params.add(new BasicNameValuePair("carteping", comment));
		UrlEncodedFormEntity entity1=null;
		try {
			entity1 =  new UrlEncodedFormEntity(params,HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// ���������url
		String url = Utils.primitiveUrl + "fooddevalus/createFoodevaluation";
		// ����������HttpPost
		HttpPost request = HttpUtil.getHttpPost(url);
		// ���ò�ѯ����
		request.setEntity(entity1);
		// �����Ӧ���
		String result= HttpUtil.queryStringForPost(request);
		Log.e("�ύ�ɹ���", "ok!");
	}
	//�ϴ���ע����Ϣ
	public static void postRegisterMsg(String usernameStr, String passwordStr, String nameStr, String phonenumStr){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// ����������
		params.add(new BasicNameValuePair("guuser", usernameStr));
		params.add(new BasicNameValuePair("gupwd", passwordStr));//���ӷ���˷���ʮ������
		params.add(new BasicNameValuePair("guname", nameStr));
		params.add(new BasicNameValuePair("guphone", phonenumStr));
		UrlEncodedFormEntity entity1=null;
		try {
			entity1 =  new UrlEncodedFormEntity(params,HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// ���������url
		String url = Utils.primitiveUrl + "guinformations/getcreateGuinformationsByCondition";
		// ����������HttpPost
		HttpPost request = HttpUtil.getHttpPost(url);
		// ���ò�ѯ����
		request.setEntity(entity1);
		// �����Ӧ���
		String result= HttpUtil.queryStringForPost(request);
		Log.e("�ύ�ɹ���", "ok!");
	}
	/**
	 * �ϴ�Զ��ԤԼ��Ϣ
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
	    		// ����������
	    		params.add(new BasicNameValuePair("Subno", Subno));//ԤԼ��
	    		params.add(new BasicNameValuePair("Subtime", Subtime));//ԤԼʱ��
	    		params.add(new BasicNameValuePair("Guuser", Guuser));//�û���
	    		params.add(new BasicNameValuePair("Subnums", Subnums));//����
	    		UrlEncodedFormEntity entity1=null;
	    		try {
	 			entity1 =  new UrlEncodedFormEntity(params,HTTP.UTF_8);
	 		} catch (UnsupportedEncodingException e) {
	 			e.printStackTrace();
	 		}
	 		// ���������url
	 		String url = Utils.primitiveUrl + "subscribes/getcreateSubscribesByCondition";
	 		// ����������HttpPost
	 		HttpPost request = HttpUtil.getHttpPost(url);
	 		// ���ò�ѯ����
	 		request.setEntity(entity1);
	 		// �����Ӧ���
			String result = HttpUtil.queryStringForPost(request);
			Log.e("�ύ�ɹ���", "ok!");
			}
		}).start();
		return res;
		 
	}
	
}
