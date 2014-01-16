package com.comdosoft.homework;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;
import com.comdosoft.homework.adapter.MainClssStuAdapter;
import com.comdosoft.homework.pojo.ClassStuPojo;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
	String ceshiJson = "{\"status\":\"success\",\"notice\":\"\u767b\u9646\u6210\u529f\uff01\"," +
			"\"student\":{\"id\":1,\"name\":\"tea\",\"nickname\":\"\u4e0a\u5584\u82e5\u6c34\",\"avatar_url\":\"/homework_system/avatars/students/student_1.jpg\"}," +
			"\"class\":{\"id\":1,\"name\":\"eeeee\",\"tearcher_name\":\"tea\",\"tearcher_id\":1}," +
			"\"classmates\":[{\"avatar_url\":\"/homework_system/avatars/students/student_1.jpg\",\"id\":1,\"name\":\"tea\",\"nickname\":\"\u4e0a\u5584\u82e5\u6c34\"}" +
			",{\"avatar_url\":\"/homework_system/avatars/students/student_1.jpg\",\"id\":2,\"name\":\"tea\",\"nickname\":\"\u4e0a\u5584\u82e5\u6c341\"}," +
			"{\"avatar_url\":null,\"id\":3,\"name\":null,\"nickname\":\"\u4e0a\u5584\u82e5\u6c342\"}," +
			"{\"avatar_url\":null,\"id\":4,\"name\":null,\"nickname\":\"\u4e0a\u5584\u82e5\u6c343\"}]}}";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_class_classes);
		Display display=this.getWindowManager().getDefaultDisplay();
		width=display.getWidth();
		height=display.getHeight();
		//		analysisjson();
		stuList.add(new ClassStuPojo(1,"tea","/homework_system/avatars/students/student_1.jpg","u4e0a\u5584\u82e5\u6c34"));
		stuList.add(new ClassStuPojo(2,"tea","/homework_system/avatars/students/student_2.png","u4e0a\u5584\u82e5\u6c341"));
		stuList.add(new ClassStuPojo(3,"tea","/homework_system/avatars/students/student_1.jpg","u4e0a\u5584\u82e5\u6c342"));
		stuList.add(new ClassStuPojo(4,"tea","/homework_system/avatars/students/student_1.jpg","u4e0a\u5584\u82e5\u6c343"));
		setView();
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					bitamp=BitmapFactory.decodeStream(new URL("http://192.168.0.101:3004/homework_system/avatars/students/student_1.jpg").openConnection().getInputStream());
					handler.sendEmptyMessage(0);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
	//解析json-classmates
//	public void analysisjson()
//	{
//		
//		try {
//			
//			JSONArray jsonArray = jsonObject.getJSONArray("classmates");
//			for(int i=0;i<jsonArray.length();i++)
//			{ 
//				JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i); 
//				String stu_Url=(String) jsonObject2.get("avatar_url");
//				int id=jsonObject2.getInt("id");
//				String name=jsonObject2.getString("name");
//				String nickname=jsonObject2.getString("nickname");
//
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
//	}
	public void setView()
	{
		main_class_oneIVll=(LinearLayout) findViewById(R.id.main_class_classes_include).findViewById(R.id.main_class_oneIVll);
		main_class_oneIVll.setLayoutParams(new LinearLayout.LayoutParams(394, (int)(height*0.25*0.6)));
		main_class_oneIVll.setPadding(0, (int)(height*0.25*0.2) , 0, 0);
		main_class_oneIVll.setGravity(Gravity.CENTER);
		main_class_oneIV=(ImageView) findViewById(R.id.main_class_classes_include).findViewById(R.id.main_class_oneIV);
		main_class_oneIV.setLayoutParams(new LinearLayout.LayoutParams(182, 182));
		main_class_oneTv1=(TextView)findViewById(R.id.main_class_classes_include).findViewById(R.id.main_class_oneTv1);
		main_class_oneTv1.setText("tea");
		main_class_oneTv2=(TextView)findViewById(R.id.main_class_classes_include).findViewById(R.id.main_class_oneTv2);
		main_class_oneTv2.setText("u4e0a\u5584\u82e5\u6c34");
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

