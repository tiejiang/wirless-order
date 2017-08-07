package com.xiangxun.wirlessorder.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.wheel.StrericWheelAdapter;
import com.wheel.WheelView;
import com.xiangxun.wirlessorder.util.ObtainData;
import com.xiangxun.wirlessorder.util.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class RestaurantJiMei extends Activity {
	private GridView gridView;
	private ArrayList<HashMap<String, Object>> listItem;
	private SimpleAdapter adapter;
	private String[] textString = {"����ȫ��", "ԤԼ", "����ͳ��", "���", "�����"};
	private EditText num;
	//ʱ��ѡ�������
/** Called when the activity is first created. */
	
	private int minYear = 1970;  //��С���
	private int fontSize = 13; 	 //�����С
	private WheelView yearWheel,monthWheel,dayWheel,hourWheel,minuteWheel,secondWheel;
	public static String[] yearContent=null;
	public static String[] monthContent=null;
	public static String[] dayContent=null;
	public static String[] hourContent = null;
	public static String[] minuteContent=null;
	private TextView time_tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jimeimainview);
		gridView = (GridView)findViewById(R.id.gridView);
		listItem = new ArrayList<HashMap<String,Object>>();
		int[] image= {R.drawable.wholeview, R.drawable.reserve, R.drawable.tongji, 
				R.drawable.order, R.drawable.location};
		for (int i = 0; i < textString.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", image[i]);
			map.put("ItemText", textString[i]);
			listItem.add(map);
		}
		adapter = new SimpleAdapter(this, listItem, R.layout.gridview_item,
				new String[] {"ItemImage", "ItemText"}, new int[] {R.id.ItemImage, R.id.ItemText});
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new ItemClickListener());
		initContent();
	}
    public void initContent()
	{
		yearContent = new String[10];
		for(int i=0;i<10;i++)
			yearContent[i] = String.valueOf(i+2013);
		
		monthContent = new String[12];
		for(int i=0;i<12;i++)
		{
			monthContent[i]= String.valueOf(i+1);
			if(monthContent[i].length()<2)
	        {
				monthContent[i] = "0"+monthContent[i];
	        }
		}
			
		dayContent = new String[31];
		for(int i=0;i<31;i++)
		{
			dayContent[i]=String.valueOf(i+1);
			if(dayContent[i].length()<2)
	        {
				dayContent[i] = "0"+dayContent[i];
	        }
		}	
		hourContent = new String[24];
		for(int i=0;i<24;i++)
		{
			hourContent[i]= String.valueOf(i);
			if(hourContent[i].length()<2)
	        {
				hourContent[i] = "0"+hourContent[i];
	        }
		}
		minuteContent = new String[60];
		for(int i=0;i<60;i++)
		{
			minuteContent[i]=String.valueOf(i);
			if(minuteContent[i].length()<2)
	        {
				minuteContent[i] = "0"+minuteContent[i];
	        }
		}
	}
	class ItemClickListener implements OnItemClickListener{
		
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
			switch (arg2) {
			case 0:
				//����ȫ��
				Intent intent = new Intent();
				intent.setClass(RestaurantJiMei.this, WholeView.class);
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;
			case 1:
				//ԤԼ
//				Intent intentReserve = new Intent();
//				intentReserve.setClass(RestaurantJiMei.this, TableSelect.class);
//				startActivity(intentReserve);
//				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
//				finish();
				LayoutInflater inflater = LayoutInflater.from(RestaurantJiMei.this);
				final View timeView = inflater.inflate(R.layout.time_picker, null);
				showBuilder(timeView);
				
				break;
			case 2:
				//����ͳ��
				Intent intentExpenditrue = new Intent();
				intentExpenditrue.setClass(RestaurantJiMei.this, ListExpenditure.class);
				startActivity(intentExpenditrue);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				finish();
				break;
//			case 3:
//				//����ȡ��
//				break;
//			case 4:
//				//����
//				break;
			case 3:
				//���
				Intent intent1 = new Intent();
				intent1.setClass(RestaurantJiMei.this, Menu.class);
				startActivity(intent1);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;
			case 4:
				//�����
				Intent intentGo = new Intent();
				intentGo.setClass(RestaurantJiMei.this, MapDisplay.class);
				startActivity(intentGo);
				break;
			}
		}
	}
	//������ʱ��ѡ���builder
	private void showBuilder(final View view){
		Calendar calendar = Calendar.getInstance();
	    int curYear = calendar.get(Calendar.YEAR);
        int curMonth= calendar.get(Calendar.MONTH)+1;
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        int curHour = calendar.get(Calendar.HOUR_OF_DAY);
        int curMinute = calendar.get(Calendar.MINUTE);
//      int curSecond = calendar.get(Calendar.SECOND);
// 	    time_tv = (TextView)view.findViewById(R.id.time_tv);
        num = (EditText)view.findViewById(R.id.num);
        
        
 	    
	    yearWheel = (WheelView)view.findViewById(R.id.yearwheel);
	    monthWheel = (WheelView)view.findViewById(R.id.monthwheel);
	    dayWheel = (WheelView)view.findViewById(R.id.daywheel);
	    hourWheel = (WheelView)view.findViewById(R.id.hourwheel);
	    minuteWheel = (WheelView)view.findViewById(R.id.minutewheel);
//	    secondWheel = (WheelView)view.findViewById(R.id.secondwheel);
	    
	    yearWheel.setAdapter(new StrericWheelAdapter(yearContent));
	 	yearWheel.setCurrentItem(curYear-2013);
	    yearWheel.setCyclic(true);
	    yearWheel.setInterpolator(new AnticipateOvershootInterpolator());
        
 
        monthWheel.setAdapter(new StrericWheelAdapter(monthContent));
       
        monthWheel.setCurrentItem(curMonth-1);
     
        monthWheel.setCyclic(true);
        monthWheel.setInterpolator(new AnticipateOvershootInterpolator());
        
        dayWheel.setAdapter(new StrericWheelAdapter(dayContent));
        dayWheel.setCurrentItem(curDay-1);
        dayWheel.setCyclic(true);
        dayWheel.setInterpolator(new AnticipateOvershootInterpolator());
        
        hourWheel.setAdapter(new StrericWheelAdapter(hourContent));
        hourWheel.setCurrentItem(curHour);
        hourWheel.setCyclic(true);
        hourWheel.setInterpolator(new AnticipateOvershootInterpolator());
        
        minuteWheel.setAdapter(new StrericWheelAdapter(minuteContent));
        minuteWheel.setCurrentItem(curMinute);
        minuteWheel.setCyclic(true);
        minuteWheel.setInterpolator(new AnticipateOvershootInterpolator());
	    //��ȡ�˻���Ϣ
        final SharedPreferences sp = RestaurantJiMei.this.getSharedPreferences("user_msg", MODE_WORLD_WRITEABLE);
        
		AlertDialog alertDialog = new AlertDialog.Builder(RestaurantJiMei.this)
    	.setTitle("�û�����" + sp.getString("name", "0"))
    	.setView(view)
    	.setCancelable(false)
    	.setPositiveButton("�ύ", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//�ϴ�����˵Ĳ���
		        String userName = null;//�û���
		        String appointTime = null;//ԤԼʱ��
		        String mealNum = null;//�Ͳ�����
		        String currentTime = null;//��ǰʱ��
				// TODO Auto-generated method stub
//				int id = v.getId();
//		    	if(id==R.id.pick_bt)
//		    	{
//		    		View view = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.time_picker, null); 
//		            AlertDialog.Builder builder = new AlertDialog.Builder(this);  
//		            builder.setView(view); 
		            
		            
			        
//			        secondWheel.setAdapter(new StrericWheelAdapter(secondContent));
//			        secondWheel.setCurrentItem(curSecond);
//			        secondWheel.setCyclic(true);
//			        secondWheel.setInterpolator(new AnticipateOvershootInterpolator());
					 
//			        builder.setTitle("ѡȡʱ��");  
//			        builder.setPositiveButton("ȷ  ��", new DialogInterface.OnClickListener() {  
//
//			        	@Override  
//		            	public void onClick(DialogInterface dialog, int which) {  
		            	
			        		StringBuffer sb = new StringBuffer();  
			        		sb.append(yearWheel.getCurrentItemValue()).append("-")
			        		.append(monthWheel.getCurrentItemValue()).append("-")
			        		.append(dayWheel.getCurrentItemValue());
		     
			        		sb.append(" ");  
			        		sb.append(hourWheel.getCurrentItemValue())  
			        		.append(":").append(minuteWheel.getCurrentItemValue());
//			        		.append(":").append(secondWheel.getCurrentItemValue()); 
			        		/**
			        		 * ���ϴ�����˵�����
			        		 * */
			        		appointTime = sb.toString();
			        		System.out.println("Time:" + sb);
			        		mealNum = num.getText().toString().trim();
			        		currentTime = Utils.obtainCurrentTime();
			        		userName = sp.getString("name", "0");
			        		
			        		//test
//			        		System.out.println("ԤԼʱ�䣻" + appointTime + "\n" + "�Ͳ�������" + mealNum + "\n" + "��ǰʱ�䣺" + currentTime + "\n" + "�û�����" + userName);
		        			//�����ϴ����ݵķ���
			        		new ObtainData().remoteReservation(userName + currentTime, appointTime, userName, mealNum);
			        		Toast.makeText(getApplicationContext(), "ԤԼ�ɹ���", Toast.LENGTH_SHORT).show();
			        		dialog.cancel();	
//		            	} 
//			        });  
//		       
//			        builder.show();
//		    	}
				
				
			}
		})
		.setNegativeButton("ȡ��", null)
		.create();
		alertDialog.show();

	}
	 //���ؼ����¼�����
//	  private long exitTime = 0;
//		@Override  
//	  public boolean onKeyDown(int keyCode, KeyEvent event){  
//         if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)  
//         {  
//             if((System.currentTimeMillis()-exitTime) > 2000)  //System.currentTimeMillis()���ۺ�ʱ���ã��϶�����2000   
//             {  
//                Toast.makeText(getApplicationContext(), "�ٰ�һ���˳���½",Toast.LENGTH_SHORT).show();                                  
//                exitTime = System.currentTimeMillis();  
//             }  
//             else  
//             {  
////            	Intent intent = new Intent(RestaurantJiMei.this, MainActivity.class);				
//// 				startActivity(intent);
// 				finish();
// 				System.exit(0);  
//             }  
//             return true;  
//         }  
//         return super.onKeyDown(keyCode, event);    
//	  }
}
