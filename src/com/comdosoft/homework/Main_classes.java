package com.comdosoft.homework;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;
import com.comdosoft.homework.adapter.MainClssStuAdapter;
import com.comdosoft.homework.pojo.ClassStuPojo;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Main_classes extends Activity 
{
	private int width;
	private int height;
	private LinearLayout main_class_classesll;
	private LinearLayout main_class_classesll2;	
	private LinearLayout main_class_oneIVll;
	private ImageView main_class_oneIV;
	private TextView main_class_classesTv;
	private TextView main_class_oneTv1;
	private TextView main_class_oneTv2;
	private GridView main_class_classGv; 
	private Bitmap bitamp;
	private List<ClassStuPojo> stuList=new ArrayList<ClassStuPojo>();
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_class_classes);
		Display display=this.getWindowManager().getDefaultDisplay();
		width=display.getWidth();
		height=display.getHeight();
		stuList.add(new ClassStuPojo(1,"tea","/homework_system/avatars/students/student_1.jpg","u4e0a\u5584\u82e5\u6c34"));
		stuList.add(new ClassStuPojo(2,"tea","/homework_system/avatars/students/student_2.png","u4e0a\u5584\u82e5\u6c341"));
		stuList.add(new ClassStuPojo(3,"tea","/homework_system/avatars/students/student_1.jpg","u4e0a\u5584\u82e5\u6c342"));
		stuList.add(new ClassStuPojo(4,"tea","/homework_system/avatars/students/student_1.jpg","u4e0a\u5584\u82e5\u6c343"));
		setView();

		Map<String, String> map = new HashMap<String, String>();
		map.put("student_id", "1");
		map.put("school_class_id", "1");
		//		if (HomeWorkTool.isConnect(getApplicationContext())) {
		String result="";
		try {
			result = HomeWorkTool.sendGETRequest(Urlinterface.get_class_info, map);
			Log.i("aa", result);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//		thread.start();
	}
	//本人的头像解析
	//	Thread thread = new Thread(new Runnable() {
	//		public void run() {
	//			try {
	//				bitamp=BitmapFactory.decodeStream(new URL("http://192.168.0.101:3004/homework_system/avatars/students/student_1.jpg").openConnection().getInputStream());
	//				handler.sendEmptyMessage(0);
	//			} catch (MalformedURLException e) {
	//				// TODO Auto-generated catch block
	//				e.printStackTrace();
	//			} catch (IOException e) {
	//				// TODO Auto-generated catch block
	//				e.printStackTrace();
	//			}
	//		}
	//	});
	//	解析json-classmates
	//		public void analysisjson()
	//		{
	//			
	//			try {
	//	JSONObject jsonobject=new JSONObject("asd");
	//	String avatar_url=jsonobject.getString("avatar_url");
	//	String name=jsonobject.getString("name");
	//	String nick_name=jsonobject.getString("nickname");
	//				JSONArray jsonArray = jsonObject.getJSONArray("classmates");
	//				for(int i=0;i<jsonArray.length();i++)
	//				{ 
	//					JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i); 
	//					String stu_Url=(String) jsonObject2.get("avatar_url");
	//					int id=jsonObject2.getInt("id");
	//					String name=jsonObject2.getString("name");
	//					String nickname=jsonObject2.getString("nickname");
	//					stuList.add(new ClassStuPojo(id,name,stu_Url,nickname));
	//				}
	//			} catch (JSONException e) {
	//				e.printStackTrace();
	//			}
	//	
	//		}
	public void setView()
	{
		main_class_oneIVll=(LinearLayout) findViewById(R.id.main_class_classes_include).findViewById(R.id.main_class_oneIVll);
		main_class_oneIVll.setLayoutParams(new LinearLayout.LayoutParams(394, (int)(height*0.25*0.6)));
		main_class_oneIVll.setPadding(0, (int)(height*0.25*0.2) , 0, 0);
		main_class_oneIVll.setGravity(Gravity.CENTER);
		main_class_oneIV=(ImageView) findViewById(R.id.main_class_classes_include).findViewById(R.id.main_class_oneIV);
		main_class_oneIV.setLayoutParams(new LinearLayout.LayoutParams(182, 182));
		main_class_oneTv1=(TextView)findViewById(R.id.main_class_classes_include).findViewById(R.id.main_class_oneTv1);
		//		main_class_oneTv1.setText(name);				设置本名
		main_class_oneTv2=(TextView)findViewById(R.id.main_class_classes_include).findViewById(R.id.main_class_oneTv2);
		//		main_class_oneTv2.setText(nick_name);			设置外号

		main_class_classesll=(LinearLayout) findViewById(R.id.main_class_classesll);
		main_class_classesll.setLayoutParams(new LinearLayout.LayoutParams(394, (int)(height*0.75*0.1)));
		main_class_classesTv=(TextView) findViewById(R.id.main_class_classesTv);
		main_class_classesTv.setWidth(182);
		main_class_classesll2=(LinearLayout) findViewById(R.id.main_class_classesll2);
		main_class_classesll2.setLayoutParams(new LinearLayout.LayoutParams(394, (int)(height*0.75*0.9)));
		main_class_classGv=(GridView) findViewById(R.id.main_class_classGv);
		main_class_classGv.setLayoutParams(new LinearLayout.LayoutParams(394, (int)(height*0.75*0.9)));
		main_class_classGv.setNumColumns(3);
		main_class_classGv.setAdapter(new MainClssStuAdapter(main_class_classGv,getApplicationContext(),stuList,width,height));
	}
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what)
			{
			case 0:
				main_class_oneIV.setImageBitmap(bitamp);
				break;
			}
		}
	};
}

