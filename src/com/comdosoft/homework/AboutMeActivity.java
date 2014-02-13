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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AboutMeActivity extends Activity {
	private ListView listview;
	List<AboutMePojo> listam ;
	private String user_id;
	private String school_class_id;

	HomeWork hw = (HomeWork) getApplication();
	HomeWorkMainActivity hwma = new HomeWorkMainActivity();

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutme);
		listview = (ListView) findViewById(R.id.aboutmeLv);
		SharedPreferences sp = getSharedPreferences(Urlinterface.SHARED, 0);
		user_id = sp.getString("user_id", "null");
		school_class_id = sp.getString("school_class_id", "null");

		getnews();
	}
	protected void onResume() {
		
		super.onResume();
		getnews();
	}
	public void getnews() {
		new Thread()
		{
			public void run()
			{
				while(true)
				{
					try {
						if (HomeWorkTool.isConnect(AboutMeActivity.this)) {
							get_News();
						}
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				listview.setAdapter(new AboutMeAdapter(
						getApplicationContext(), listview));
				break;
			case 1:
				break;
			}
		}
	};

	// 解析获取到的Json
	public int getNewsJson(String json) {
		try {
			JSONObject jsonobject = new JSONObject(json);
			listam= new ArrayList<AboutMePojo>();
			String status = (String) jsonobject.get("status");
			if (status.equals("success")) {
				JSONArray jsonarray = jsonobject.getJSONArray("messages");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject jsonobject2 = jsonarray.getJSONObject(i);
					List<String> liststr = divisionStr(jsonobject2
							.getString("content"));
					String jsonstatus = liststr.get(0);
					String content = liststr.get(1);

					String created_at = divisionTime(jsonobject2
							.getString("created_at"));
					String id = jsonobject2.getString("id");
					String micropost_id = jsonobject2.getString("micropost_id");
					String sender_avatar_url = jsonobject2
							.getString("sender_avatar_url");
					String sender_name = jsonobject2.getString("sender_name");
					String user_id = jsonobject2.getString("user_id");
					listam.add(new AboutMePojo(id, micropost_id, user_id,
							sender_avatar_url, sender_name, jsonstatus,
							content, created_at));
				}
				Log.i("bbb", "size:"+listam.size());
				return listam.size();
			} else {
				String notic = (String) jsonobject.get("notic");
				Toast.makeText(getApplicationContext(), notic, 1).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 请求获取和我相关的消息
	public void get_News() {
		Thread thread = new Thread() {
			public void run() {
				if (!httpGetNews(user_id, school_class_id).equals(null)) {
					getNewsJson(httpGetNews(user_id, school_class_id));
					handler1.sendEmptyMessage(0);
				}
			}

		};
		thread.start();
	}

	// HTTP请求
	public String httpGetNews(String user_id, String school_class_id) {
		try {
			HashMap<String, String> mp = new HashMap<String, String>();
			mp.put("user_id", user_id);
			mp.put("school_class_id", school_class_id);
			String json = HomeWorkTool
					.sendGETRequest(Urlinterface.get_News, mp);
			return json;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 分割content
	public List<String> divisionStr(String str) {
		List<String> list = new ArrayList<String>();
		int temp1 = str.indexOf(";||;");
		int temp2 = str.lastIndexOf("]]");
		list.add(str.substring(temp2 + 2, temp1));
		list.add(str.substring(temp1 + 4, str.length()));
		return list;
	}

	// 分割时间
	public String divisionTime(String timeStr) {
		int temp1 = timeStr.indexOf("T");
		int temp2 = timeStr.lastIndexOf("+");
		return timeStr.substring(0, temp1) + " "
		+ timeStr.substring(temp1 + 1, temp2);
	}
	public static class ViewHolder
	{
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
		public TextView tv4;
		public TextView tv5;
		public ImageButton Ib;

	}
	// 适配器
	public class AboutMeAdapter extends BaseAdapter {
		private Context context;
		private ListView listview;
		AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
		HomeWork hw = (HomeWork) getApplication();
		public AboutMeAdapter() {
		}

		public AboutMeAdapter( Context context,
				ListView listview) {
			this.context = context;
			this.listview = listview;
		}

		public int getCount() {
			return listam.size();
		}

		public Object getItem(int position) {
			return listam.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			ViewHolder  holder=null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.aboutme_one, null);
				holder=new ViewHolder();
				convertView.setPadding(0, 15, 0, 15);
				holder.tv1 = (TextView) convertView
						.findViewById(R.id.aboutme_oneTv);
				holder.tv2 = (TextView) convertView
						.findViewById(R.id.aboutme_oneTv2);
				holder.tv3 = (TextView) convertView
						.findViewById(R.id.aboutme_oneTv3);
				holder.tv4 = (TextView) convertView
						.findViewById(R.id.aboutme_oneTv4);
				holder.tv5 = (TextView) convertView
						.findViewById(R.id.aboutme_oneTv5);
				holder.Ib = (ImageButton) convertView
						.findViewById(R.id.aboutme_oneIb);
				ImageView iv = (ImageView) convertView
						.findViewById(R.id.aboutme_oneIv);
				String strUrl = Urlinterface.IP
						+ listam.get(position).getSender_avatar_url();
				iv.setTag(strUrl + listam.get(position).getId());
				Bitmap bm = asyncImageLoader.asyncLoadImage(strUrl
						+ listam.get(position).getId(), 0, callback);
				iv.setImageBitmap(null);
				if (bm == null) {
					iv.setBackgroundResource(R.drawable.ic_launcher);
				} else {
					iv.setImageBitmap(bm);
				}
				bm = null;
				convertView.setTag(holder);
			}
			else
			{
				holder=(ViewHolder)convertView.getTag();
			}
			holder.tv1.setText(listam.get(position).getSender_name());
			holder.tv2.setText(listam.get(position).getStatus());
			Log.i("aa",position+"");
			holder.tv3.setText(listam.get(position).getContent());
			holder.tv4.setText(listam.get(position).getCreated_at());
			// 查看
			holder.tv5.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new Thread() {
						public void run() {
							try {
								HashMap<String, String> mp = new HashMap<String, String>();
								mp.put("user_id", listam.get(position)
										.getUser_id());
								mp.put("school_class_id",
										String.valueOf(school_class_id));
								mp.put("message_id", listam.get(position)
										.getId());
								Log.i("aaa",
										listam.get(position).getUser_id()
										+ "/"
										+ String.valueOf(school_class_id)
										+ "/"
										+ listam.get(position)
										.getId());
								String json = HomeWorkTool.doPost(
										Urlinterface.read_message, mp);
								// Log.i("bbb",
								// "message_id:"+listam.get(position).getId()+"json:"+json);
								JSONObject jsonobject = new JSONObject(json);
								String status = jsonobject
										.getString("status");
								Message msg = new Message();
								if (status.equals("error")) {
									msg.what = 3;
									handler.sendMessage(msg);
								} else if (status.equals("success")) {
									hw.setNewsFlag(true);
									hw.setLastcount(hw.getLastcount() - 1);
									hw.setNoselect_message(json);
									listam.remove(position);
									msg.what = 2;
									handler.sendMessage(msg);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}.start();

				}
			});

			// 删除
			holder.Ib.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new Thread() {
						@SuppressWarnings({ "rawtypes", "unchecked" })
						public void run() {
							try {
								HashMap<String, String> mp = new HashMap();
								mp.put("user_id", user_id);
								mp.put("school_class_id", school_class_id);
								mp.put("message_id", listam.get(position)
										.getId());
								String json = HomeWorkTool.doPost(
										Urlinterface.delete_message, mp);
								JSONObject jsonobject = new JSONObject(json);
								String notice = jsonobject
										.getString("notice");
								String status = (String) jsonobject
										.get("status");
								Message msg = new Message();
								msg.obj = notice;
								if (status.equals("success")) {
									msg.what = 0;
									listam.remove(position);
									hw.setLastcount(hw.getLastcount() - 1);
									handler.sendMessage(msg);
								} else {
									msg.what = 1;
									handler.sendMessage(msg);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}.start();
				}
			});
			return convertView;

		}

		// hadnler操作
		Handler handler = new Handler() {
			public void dispatchMessage(Message msg) {
				super.dispatchMessage(msg);
				switch (msg.what) {
				case 0:
					listview.setAdapter(new AboutMeAdapter(
							getApplicationContext(), listview));
					Toast.makeText(AboutMeActivity.this, msg.obj.toString(), 0)
					.show();
					break;
				case 1:
					Toast.makeText(context, msg.obj.toString(), 0).show();
					break;
				case 2:
					listview.setAdapter(new AboutMeAdapter(
							getApplicationContext(), listview));
					HomeWorkMainActivity.instance.tabhost.setCurrentTab(0);
					// Intent intent = new
					// Intent(AboutMeActivity.this,HomeWorkMainActivity.class);
					// intent.putExtra("type", 1);
					// HomeWorkMainActivity.instance.finish();
					// startActivity(intent);
					break;
				case 3:
					Toast.makeText(AboutMeActivity.this, "信息错误", 0).show();
					break;
				default:
					break;
				}
			}
		};
		// 显示头像所用的回调
		LoadFinishCallBack callback = new LoadFinishCallBack() {
			public void loadFinish(String strUrl, int i, Bitmap bitmap) {
				if (bitmap != null) {
					ImageView imageView = (ImageView) listview
							.findViewWithTag(strUrl);
					if (imageView != null) {
						imageView.setImageBitmap(bitmap);
					}
				}
			};
		};
	}
}
