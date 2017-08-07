package com.xiangxun.wirlessorder.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.xiangxun.wirlessorder.view.R;

/**
 * 系统时间
 * 
 * @author tiejiang
 */
public class Utils {
//	public static String primitiveUrl = "http://10.8.12.69:8080/demo/";
//	public static String primitiveUrl = "http://172.23.181.4:8080/demo/";
//	public static String primitiveUrl = "http://192.168.18.21:8080/demo/";
	public static String primitiveUrl = "http://10.8.12.158:8080/demo/";
	//获得系统当前时间座位订单号一部分
	public static String obtainCurrentTime(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");       
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间        
		String str = formatter.format(curDate);  
		return str;
	}
	//菜单图片数组
	public static int[] vegImage = {R.drawable.dongpozhouzi, R.drawable.fuqifeipian, R.drawable.ganshaoguiyu,
		R.drawable.gongbaojiding, R.drawable.huiguorou,  R.drawable.shuizhuyu, R.drawable.yuxiangrousi,
		R.drawable.ganshaoyanli, R.drawable.mapodoufu, R.drawable.suancaiyu,R.drawable.fengdunmudan,
		R.drawable.hupimaodou, R.drawable.huangshandunge, R.drawable.huotuidunjiayu, R.drawable.mizhihongyu,
		R.drawable.qingzhengshiji, R.drawable.xianggubanli, R.drawable.xiangguhe, R.drawable.yanxianjueyu,
		R.drawable.yangmeiwanzi};
}
