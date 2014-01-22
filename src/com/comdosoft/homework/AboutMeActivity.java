package com.comdosoft.homework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.comdosoft.homework.pojo.AboutMePojo;
import com.comdosoft.homework.tools.AsyncImageLoader;
import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;
import com.comdosoft.homework.tools.AsyncImageLoader.LoadFinishCallBack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AboutMeActivity extends Activity 
{
	private ListView listview;
	List<AboutMePojo> listam ;
	private String user_id;
	private String school_class_id;
	private int count=0;
	@SuppressWarnings("unused")
	private int num;
	HomeWork hw=(HomeWork) getApplication();
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutme);
		listview=(ListView) findViewById(R.id.aboutmeLv);
		SharedPreferences sp = getSharedPreferences(Urlinterface.SHARED, 0);
		user_id = sp.getString("user_id", "null");
		school_class_id = sp.getString("school_class_id", "null");

		new Thread()
		{
			public void run()
			{
				//				while(true)
				//				{
				try {
					get_News();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//				}
			}
		}.start();
	}
	Handler handler1 = new Handler(){
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
		listam=new ArrayList<AboutMePojo>();

		Thread thread=new Thread()
		{
			public void run()
			{
				try {
					HashMap<String,String> mp=new HashMap<String,String>();
					mp.put("user_id","1");
					mp.put("school_class_id", "1");
					String json = HomeWorkTool.sendGETRequest(Urlinterface.get_News, mp);
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
							String micropost_id=jsonobject2.getString("micropost_id");
							Log.i("aa", micropost_id);
							String sender_avatar_url=jsonobject2.getString("sender_avatar_url");
							String sender_name=jsonobject2.getString("sender_name");
							String user_id=jsonobject2.getString("user_id");
							listam.add(new AboutMePojo(id,micropost_id,user_id,sender_avatar_url,sender_name,jsonstatus,content,created_at));
						}
						if(count<=listam.size())
						{
							num=listam.size()-count;
							count=listam.size();
						}
						handler1.sendEmptyMessage(0);
					}
					else
					{
						String notic=(String) jsonobject.get("notic");
						Toast.makeText(getApplicationContext(), notic, 1).show();
					} 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};	
		thread.start();
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
	public class AboutMeAdapter extends BaseAdapter
	{
		private List<AboutMePojo> Amlist;
		private Context context;
		private ListView listview;
		private int class_id;
		AsyncImageLoader asyncImageLoader=new AsyncImageLoader();
		HomeWork hw=(HomeWork) context;
		public AboutMeAdapter()
		{
		}
		public AboutMeAdapter(List<AboutMePojo> amlist, Context context,ListView listview) {
			Amlist = amlist;
			this.context = context;
			this.listview=listview;
		}
		public int getCount() {
			return Amlist.size();
		}
		public Object getItem(int position) {
			return Amlist.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater
					.from(context);
			if(convertView==null)
			{
				convertView = inflater.inflate(R.layout.aboutme_one,null);
				convertView.setPadding(0, 10, 0,10);
				TextView tv1= (TextView) convertView.findViewById(R.id.aboutme_oneTv);
				TextView tv2= (TextView) convertView.findViewById(R.id.aboutme_oneTv2);
				TextView tv3= (TextView) convertView.findViewById(R.id.aboutme_oneTv3);
				TextView tv4= (TextView) convertView.findViewById(R.id.aboutme_oneTv4);
				TextView tv5= (TextView) convertView.findViewById(R.id.aboutme_oneTv5);
				ImageButton Ib=(ImageButton) convertView.findViewById(R.id.aboutme_oneIb);
				tv1.setText(Amlist.get(position).getSender_name());
				tv2.setText(Amlist.get(position).getStatus());
				tv3.setText(Amlist.get(position).getContent());
				tv4.setText(Amlist.get(position).getCreated_at());
				//查看
				tv5.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v) {
						new Thread()
						{
							public void run()
							{
								try {
									HashMap<String, String> mp=new HashMap();
									mp.put("user_id", Amlist.get(position).getUser_id());
									mp.put("school_class_id",String.valueOf(class_id));
									mp.put("message_id", Amlist.get(position).getMicropost_id());
									String json=HomeWorkTool.doPost(Urlinterface.read_message, mp);
									Log.i("aa", json);
									JSONObject jsonobject=new JSONObject(json);
									String status=jsonobject.getString("status");
									Message msg=new Message();
									//									
									if(status.equals("error"))
									{
										msg.what=3;
										handler.sendMessage(msg);
									}
									else if(status.equals("success"))
									{
										hw.setMessage_id(Integer.valueOf(Amlist.get(position).getMicropost_id()));
										Log.i("aa", hw.getMessage_id()+"消息ID");
										hw.setNoselect_message(json);
										msg.what=2;
										handler.sendMessage(msg);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}.start();

					}
				});

				//删除
				Ib.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v) {
						new Thread()
						{
							@SuppressWarnings({ "rawtypes", "unchecked" })
							public void run()
							{
								try {
									HashMap<String, Integer> mp=new HashMap();
									mp.put("user_id", Integer.valueOf(Amlist.get(position).getUser_id()));
									mp.put("school_class_id",class_id);
									mp.put("message_id", Integer.valueOf(Amlist.get(position).getId()));
									Log.i("aa", "user_id:"+Amlist.get(position).getUser_id()+"class_id:"+class_id+"messag_id:"+Amlist.get(position).getId());
									String json=HomeWorkTool.doPost(Urlinterface.delete_message, mp);
									JSONObject jsonobject=new JSONObject(json);
									String notice=jsonobject.getString("notice");
									String status=(String) jsonobject.get("status");
									Message msg=new Message();
									msg.obj=notice;
									if(status.equals("success"))
									{
										msg.what=0;
										Amlist.remove(position);
										handler.sendMessage(msg);
									}
									else
									{
										msg.what=1;
										handler.sendMessage(msg);
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}.start();
					}
				});
				ImageView iv=(ImageView) convertView.findViewById(R.id.aboutme_oneIv);
				String strUrl=Urlinterface.IP+Amlist.get(position).getSender_avatar_url();
				iv.setTag(strUrl+Amlist.get(position).getId());
				Bitmap bm = asyncImageLoader.asyncLoadImage(strUrl+Amlist.get(position).getId(), 0, callback);  
				iv.setImageBitmap(null);  
				if(bm==null) {  
					iv.setBackgroundResource(R.drawable.ic_launcher);
				} else {  
					iv.setImageBitmap(bm);	
				} 
				bm=null;
			}	
			return convertView;

		}
		Handler handler=new Handler()
		{
			public void dispatchMessage(Message msg) {
				super.dispatchMessage(msg);
				switch (msg.what) {
				case 0:
					Toast.makeText(AboutMeActivity.this,msg.obj.toString() , 0).show();
					notifyDataSetChanged();
					break;
				case 1:
					Toast.makeText(context, msg.obj.toString(), 0).show();
					break;
				case 2:
					HomeWork hw=(HomeWork) getApplication();
					hw.setMainItem(0);
					Intent intent = new Intent(AboutMeActivity.this,HomeWorkMainActivity.class);
					AboutMeActivity.this.finish();
					startActivity(intent);
					break;
				case 3:
					Toast.makeText(AboutMeActivity.this, "查看错误", 0).show();
					break;
				default:
					break;
				}
			}
		};
		LoadFinishCallBack callback = new LoadFinishCallBack() {  
			public void loadFinish(String strUrl, int i, Bitmap bitmap) {  
				if (bitmap != null) {  
					ImageView imageView = (ImageView) listview.findViewWithTag(strUrl);  
					if(imageView != null) {  
						imageView.setImageBitmap(bitmap);  
					}  
				}  
			};  
		};  
	}
}
