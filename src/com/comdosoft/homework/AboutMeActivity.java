package com.comdosoft.homework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.comdosoft.homework.pojo.AboutMePojo;
import com.comdosoft.homework.tools.HomeWorkTool;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class AboutMeActivity extends Activity 
{
	private ListView listview;
	List<AboutMePojo> listam=new ArrayList<AboutMePojo>();
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutme);
		listview=(ListView) findViewById(R.id.aboutmeLv);
		//listview.setAdapter(new AboutMeAdapter());
		HashMap mp=new HashMap();
		mp.put("user_id", "1");
		mp.put("school_class_id", "1");
		Log.i("aa", "测试能否联通网络");
		try {
//			http://192.168.0.250:3004/api/students/into_daily_tasks?student_id=1&publish_question_package_id=1
			String json=HomeWorkTool.sendGETRequest("http://192.168.0.250:3004/api/students/get_messages", mp);
			Log.i("aa", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
