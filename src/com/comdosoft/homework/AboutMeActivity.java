package com.comdosoft.homework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.comdosoft.homework.adapter.AboutMeAdapter;
import com.comdosoft.homework.pojo.AboutMePojo;
import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;

public class AboutMeActivity extends Activity 
{
	private ListView listview;
	List<AboutMePojo> listam ;
	private int count=0;
	@SuppressWarnings("unused")
	private int num;
	HomeWork hw=(HomeWork) getApplication();
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutme);
		listview=(ListView) findViewById(R.id.aboutmeLv);
		Log.i("aa", "aboutmeActivity");
		thread.start();

	}
	Thread thread=new Thread()
	{
		public void run()
		{
			try {
				while(true)
				{
					HashMap<String, String> mp=new HashMap<String, String>();
					mp.put("user_id","1");
					mp.put("school_class_id","1");
					mp.put("message_id","12");
					String json=HomeWorkTool.doPost(Urlinterface.read_message, mp);
					Log.i("aa",json);
//					get_News();
					Thread.sleep(5000);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what)
			{
			case 0:
				listview.setAdapter(new AboutMeAdapter(listam,getApplicationContext(),listview));
				break;
			case 1:
				break;
			}
		}
	};
	public void get_News()
	{
		try {
			listam=new ArrayList<AboutMePojo>();
			HashMap<String,String> mp=new HashMap<String,String>();
			mp.put("user_id", String.valueOf(hw.getUser_id()));
			mp.put("school_class_id", String.valueOf(hw.getClass_id()));
			String json	= HomeWorkTool.sendGETRequest(Urlinterface.get_News, mp);
			Log.i("aa", json);
			JSONObject jsonobject=new JSONObject(json);
			String status=(String) jsonobject.get("status");
			if(status.equals("success"))
			{
				JSONArray jsonarray= jsonobject.getJSONArray("messages");
				for(int i=0;i<jsonarray.length();i++)
				{
					JSONObject jsonobject2=jsonarray.getJSONObject(i);
					List<String> liststr=divisionStr(jsonobject2.getString("content"));
					String jsonstatus=liststr.get(0);
					String content=liststr.get(1);
					String created_at=divisionTime(jsonobject2.getString("created_at"));
					String id=jsonobject2.getString("id");
					Log.i("aa", id);
					String micropost_id=jsonobject2.getString("micropost_id");
					Log.i("aa", micropost_id);
					String sender_avatar_url=jsonobject2.getString("sender_avatar_url");
					String sender_name=jsonobject2.getString("sender_name");
					String user_id=jsonobject2.getString("user_id");
					Log.i("aa", user_id);
					listam.add(new AboutMePojo(id,micropost_id,user_id,sender_avatar_url,sender_name,jsonstatus,content,created_at));
				}
				if(count<=listam.size())
				{
					num=listam.size()-count;
					count=listam.size();
				}
				handler.sendEmptyMessage(0);
			}
			else
			{
				String notic=(String) jsonobject.get("notic");
				Toast.makeText(getApplicationContext(), notic, 1).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent intent = new Intent();
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AboutMeActivity.this.finish();
			intent.setClass(AboutMeActivity.this, AboutMeActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	//分割content
	public List<String> divisionStr(String str)
	{
		List<String> list=new ArrayList<String>();
		int temp1=str.indexOf(";||;");
		int temp2=str.lastIndexOf("]]");
		list.add(str.substring(temp2+2, temp1));
		list.add(str.substring(temp1+4,str.length()));
		return list;
	}
	//分割时间
	public String divisionTime(String timeStr)
	{
		int temp1=timeStr.indexOf("T");
		int temp2=timeStr.lastIndexOf("+");
		return timeStr.substring(0, temp1)+" "+timeStr.substring(temp1+1, temp2);
	}
}
