package com.comdosoft.homework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.homework.pojo.Child_Micropost;
import com.comdosoft.homework.pojo.Micropost;
import com.comdosoft.homework.pull.XListView;
import com.comdosoft.homework.pull.XListView.IXListViewListener;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;

public class Class_xinxiliu extends Activity implements IXListViewListener,
		Urlinterface {
	private XListView listView_mes;
	private MicropostAdapter micropostAdapter;
	private List<Micropost> list = new ArrayList<Micropost>();
	private List<Child_Micropost> child_list = new ArrayList<Child_Micropost>();
	private Handler mHandler;
	private int start = 0;
	private int page = 1;
	private static int refreshCnt = 0;
	private View layout;
	private ListView listView2;
	private EditText fabiao_content;
	private String school_class_id; // 学生所在班级
	private String user_id = ""; // 学生 id 上面 会传过来的 学生id，
	private String class_id = "";// 班级id，
	private String micropost_id = "";// 主消息id
private String sender_types; // 发送类型
	private String reciver_id ;   // 接收者  id  
	private String reciver_types;// 接收者 类型
	
	private EditText Reply_edit;   //  回复    编辑框
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.class_middle);

		// Intent intent = getIntent();//
		// user_id = intent.getStringExtra("user_id"); // 获得上个页面传过来的 user_id
		//
		 fabiao_content = (EditText) findViewById(R.id.class_fabiao_content);

		listView_mes = (XListView) findViewById(R.id.pull_refresh_list);
		listView_mes.setPullLoadEnable(true);
		listView_mes.setDivider(null);
		
		list = new ArrayList<Micropost>();
		Micropost m1 = new Micropost("1", "12", "student", "张", "若相守1",
				"etwevececx2423 sdfd",
				"http://csdnimg.cn/www/images/csdnindex_logo.gif",
				"2012-2-3 02:34:56");
		Micropost m2 = new Micropost("2", "12", "student", "张", "若相守2",
				"etwevececx2423 sdfd",
				"http://csdnimg.cn/www/images/csdnindex_logo.gif",
				"2012-2-3 02:34:56");
		Micropost m3 = new Micropost("3", "12", "student", "张", "若相守3",
				"etwevececx2423 sdfd",
				"http://csdnimg.cn/www/images/csdnindex_logo.gif",
				"2012-2-3 02:34:56");

		list.add(m1);
		list.add(m2);
		list.add(m3);
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", user_id);
		map.put("class_id", class_id);
		String result = HomeWorkTool.doPost(
				Urlinterface.get_class_info, map);
		if (result.length()!=0) {
			JSONObject array;
			try {
				array = new JSONObject(result);
				

				String status = array.getString("status");
				String notice = array.getString("notice");
				
				if ("success".equals(status)) {
					String micropostsListJson = array
							.getString("microposts");
					JSONArray jsonArray2 = new JSONArray(
							micropostsListJson);

					int a = jsonArray2.length();
					
//	(String id, String user_id, String user_types, String name, String nickname, String content, String avatar_url, Long created_at)

//					[{id,content,user_id创建者id,user_types创建者类型，
//						name主消息的创建者名字，nickname主消息的创建者昵称,
//						avatar_url创建者头像,created_at创建时间}],
					for (int i = 0; i < jsonArray2.length(); ++i) {
						JSONObject o = (JSONObject) jsonArray2
								.get(i);
						String id = o.getString("id");
						String user_id = o.getString("user_id");
						String user_types = o.getString("user_types");
						String name = o.getString("name");
						String nickname = o.getString("nickname");
						String content = o.getString("content");
						String avatar_url = o.getString("avatar_url");
						String created_at = o.getString("created_at");

						Micropost micropost=new Micropost( id,  user_id,  user_types,  name,  nickname,  content,  avatar_url,  created_at) ;
						list.add(micropost);
					}

				} else {
					Toast.makeText(getApplicationContext(), notice,
							1).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		
		micropostAdapter = new MicropostAdapter();
		listView_mes.setAdapter(micropostAdapter);

		listView_mes.setXListViewListener(this);
		mHandler = new Handler();
	}

	// 发表
	public void class_fabiao(View v) {
		String result = "";
		String fabiaoContent = fabiao_content.getText().toString();
		if (fabiaoContent.length() == 0) {
			Toast.makeText(getApplicationContext(), "内容不能为空", 0).show();

		} else {// http://localhost:3000/api/students/news_release?
				// content='kkdkdkdk'&
			// user_id=1&
			// user_types=1&
			// school_class_id=1

			Map<String, String> map = new HashMap<String, String>();
			map.put("content", fabiaoContent);
			map.put("user_id", "1");
			map.put("user_types", "1");
			map.put("school_class_id", "1");

			try {

//				result = HomeWorkTool
//						.sendGETRequest(
//								"http://192.168.199.121:3000/api/students/news_release",
//								map);
				 result = HomeWorkTool.sendGETRequest(Urlinterface.NEWS_RELEASE, map);
				// Toast.makeText(getApplicationContext(), NEWS_RELEASE,
				// 1).show();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Toast.makeText(getApplicationContext(), "方法执行---result：" + result,
					0).show();
			if (result.equals("error")) {
				
			} else {
				JSONObject array;
				try {
					array = new JSONObject(result);// 
					String status = array.getString("status");
					String notice = array.getString("notice");

					if ("success".equals(status)) {
						Toast.makeText(getApplicationContext(), notice, 1)
								.show();
					} else {
						Toast.makeText(getApplicationContext(), notice, 1)
								.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	
	//    回复信息
	public void reply_message(View v) {
		String result = "";
		String reply_edit = Reply_edit.getText().toString();
		if (reply_edit.length() == 0) {
			Toast.makeText(getApplicationContext(), "内容不能为空", 0).show();

		} else {
//			sender_id = params[:sender_id]
//sender_types = params[:sender_types]
//		               content = params[:content]
//		   reciver_id = params[:reciver_id]
//reciver_types = params[:reciver_types]
//		
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("content", reply_edit);
			map.put("sender_id", user_id);
			map.put("sender_types", sender_types);
			map.put("reciver_id", reciver_id);
			map.put("reciver_types",reciver_types);
			try {

//				result = HomeWorkTool
//						.sendGETRequest(
//								"http://192.168.199.121:3000/api/students/news_release",
//								map);
				 result = HomeWorkTool.sendGETRequest(Urlinterface.NEWS_RELEASE, map);
				// Toast.makeText(getApplicationContext(), NEWS_RELEASE,
				// 1).show();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Toast.makeText(getApplicationContext(), "方法执行---result：" + result,
					0).show();
			if (result.equals("error")) {
				
			} else {
				JSONObject array;
				try {
					array = new JSONObject(result);// 
					String status = array.getString("status");
					String notice = array.getString("notice");

					if ("success".equals(status)) {
						Toast.makeText(getApplicationContext(), notice, 1)
								.show();
					} else {
						Toast.makeText(getApplicationContext(), notice, 1)
								.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}
	
	
	// 全部
	public void class_button_all(View v) {
		list.clear();

		// 获得第一页信息

		Micropost m1 = new Micropost("1", "12", "student", "张", "若相守1",
				"etwevececx2423 sdfd",
				"http://csdnimg.cn/www/images/csdnindex_logo.gif",
				"2012-2-3 02:34:56");
		Micropost m2 = new Micropost("2", "12", "student", "张", "若相守2",
				"etwevececx2423 sdfd",
				"http://csdnimg.cn/www/images/csdnindex_logo.gif",
				"2012-2-3 02:34:56");
		Micropost m3 = new Micropost("3", "12", "student", "张", "若相守3",
				"etwevececx2423 sdfd",
				"http://csdnimg.cn/www/images/csdnindex_logo.gif",
				"2012-2-3 02:34:56");

		list.add(m1);
		list.add(m2);
		list.add(m3);

		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", user_id);
		map.put("class_id", class_id);
		String result = HomeWorkTool.doPost(
				Urlinterface.get_class_info, map);
		if (result.length()!=0) {
			JSONObject array;
			try {
				array = new JSONObject(result);
				

				String status = array.getString("status");
				String notice = array.getString("notice");
				
				if ("success".equals(status)) {
					String micropostsListJson = array
							.getString("microposts");
					JSONArray jsonArray2 = new JSONArray(
							micropostsListJson);

					int a = jsonArray2.length();
					
//	(String id, String user_id, String user_types, String name, String nickname, String content, String avatar_url, Long created_at)

//					[{id,content,user_id创建者id,user_types创建者类型，
//						name主消息的创建者名字，nickname主消息的创建者昵称,
//						avatar_url创建者头像,created_at创建时间}],
					for (int i = 0; i < jsonArray2.length(); ++i) {
						JSONObject o = (JSONObject) jsonArray2
								.get(i);
						String id = o.getString("id");
						String user_id = o.getString("user_id");
						String user_types = o.getString("user_types");
						String name = o.getString("name");
						String nickname = o.getString("nickname");
						String content = o.getString("content");
						String avatar_url = o.getString("avatar_url");
						String created_at = o.getString("created_at");

						Micropost micropost=new Micropost( id,  user_id,  user_types,  name,  nickname,  content,  avatar_url,  created_at) ;
						list.add(micropost);
					}

				} else {
					Toast.makeText(getApplicationContext(), notice,
							1).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		
		// mAdapter.notifyDataSetChanged();
		micropostAdapter = new MicropostAdapter();
		listView_mes.setAdapter(micropostAdapter);
		// Toast.makeText(getApplicationContext(), "方法没写", 1).show();
	}

	// 我的
	public void class_button_myself(View v) {

		list.clear();

		// 获得第一页信息

		Micropost m1 = new Micropost("1", "12", "student", "张", "若相守11",
				"etwevececx2423 sdfd",
				"http://csdnimg.cn/www/images/csdnindex_logo.gif",
				"2012-2-3 02:34:56");
		Micropost m2 = new Micropost("2", "12", "student", "张", "若相守22",
				"etwevececx2423 sdfd",
				"http://csdnimg.cn/www/images/csdnindex_logo.gif",
				"2012-2-3 02:34:56");
		Micropost m3 = new Micropost("3", "12", "student", "张", "若相守33",
				"etwevececx2423 sdfd",
				"http://csdnimg.cn/www/images/csdnindex_logo.gif",
				"2012-2-3 02:34:56");

		list.add(m1);
		list.add(m2);
		list.add(m3);

		// mAdapter.notifyDataSetChanged();
		micropostAdapter = new MicropostAdapter();
		listView_mes.setAdapter(micropostAdapter);
		// Toast.makeText(getApplicationContext(), "方法没写", 1).show();
	}


	private void onLoad() {
		listView_mes.stopRefresh();
		listView_mes.stopLoadMore();
		listView_mes.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				start = ++refreshCnt;
				list.clear();

				// 获得第一页信息

				Micropost m1 = new Micropost("1", "12", "student", "张", "若相守1",
						"etwevececx2423 sdfd",
						"http://csdnimg.cn/www/images/csdnindex_logo.gif",
						"2012-2-3 02:34:56");
				Micropost m2 = new Micropost("2", "12", "student", "张", "若相守2",
						"etwevececx2423 sdfd",
						"http://csdnimg.cn/www/images/csdnindex_logo.gif",
						"2012-2-3 02:34:56");
				Micropost m3 = new Micropost("3", "12", "student", "张", "若相守3",
						"etwevececx2423 sdfd",
						"http://csdnimg.cn/www/images/csdnindex_logo.gif",
						"2012-2-3 02:34:56");

				list.add(m1);
				list.add(m2);
				list.add(m3);

				// mAdapter.notifyDataSetChanged();
				micropostAdapter = new MicropostAdapter();
				listView_mes.setAdapter(micropostAdapter);
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {

				page = page + 1;
				// if (page<=pages_count)
				// {
				//
				// String result2 = Service.getClassesList(page);
				//
				//
				// JSONObject array;
				// try
				// {
				// array = new JSONObject(result2);//
				// {"page":1,"pages_count":1,"status":"success","notice":"\u52a0\u8f7d\u5b8c\u6210","school_classes":[{"id":5,"name":"1","students_count":null,"teacher_name":"123"},
				//
				// page = Integer.valueOf(array.getString("page"));
				// pages_count =
				// Integer.valueOf(array.getString("pages_count"));
				// notice = array.getString("notice");
				// classListJson = array.getString("school_classes");
				// JSONArray jsonArray2 = new JSONArray(classListJson);
				// //{"id":5,"name":"1","students_count":null,"teacher_name":"123"}
				//
				//
				// for (int i = 0; i < jsonArray2.length(); ++i) {
				// JSONObject o = (JSONObject) jsonArray2.get(i);
				// String id = o.getString("id");
				// String name = o.getString("name");
				// int students_count =
				// o.getInt("school_class_student_relations_count");
				// String teacher_name = o.getString("teacher_name");
				//
				// Classes menu = new
				// Classes(id,name,students_count,teacher_name);
				//
				// list.add(menu);
				// }
				// Toast.makeText(getApplicationContext(), notice, 0).show();
				//
				// } catch (JSONException e)
				// {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }
				Micropost m4 = new Micropost("4", "12", "student", "张", "若相守",
						"etwevececx2423 sdfd",
						"http://csdnimg.cn/www/images/csdnindex_logo.gif",
						"2012-2-3 02:34:56");

				list.add(m4);
				micropostAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

	public class MicropostAdapter extends BaseAdapter {

		int huifu_num = 0;
		int position_huifu_num = -1;
		int number = 0;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			return list.size();// 数据总数
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			Log.i("111111111", list.size() + "--");
			LayoutInflater inflater = Class_xinxiliu.this.getLayoutInflater();
			final View view = inflater.inflate(R.layout.micropost_item, null);

			ImageView face = (ImageView) view.findViewById(R.id.user_face); // 头像
			TextView Micropost_senderName = (TextView) view
					.findViewById(R.id.message_senderName); // 谁发的
			ImageButton button1 = (ImageButton) view.findViewById(R.id.button1); // 删除按钮
			TextView Micropost_content = (TextView) view
					.findViewById(R.id.micropost_content); // 消息内容
			TextView Micropost_date = (TextView) view
					.findViewById(R.id.micropost_date); // 日期
			Button guanzhu = (Button) view.findViewById(R.id.micropost_guanzhu); // 关注
			Button huifu = (Button) view.findViewById(R.id.micropost_huifu); // 回复
			layout = view.findViewById(R.id.child_micropost); // 回复 隐藏的 内容

			final Micropost mess = list.get(position);

			// Micropost_senderName.setText(mess.getSender_name());
			// Micropost_content.setText(mess.getSender_content());
			// SimpleDateFormat dateformat1=new
			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// String a1=dateformat1.format(new Date(mess.getReceiver_date()));
			//
			// Micropost_date.setText(a1);
			if (HomeWorkTool.isConnect(getApplicationContext())) {
				
				
				if (mess.getAvatar_url() != null) { // 设置头像
					HttpClient hc = new DefaultHttpClient();

					HttpGet hg = new HttpGet(mess.getAvatar_url());//
					final Bitmap bm;
					try {
						HttpResponse hr = hc.execute(hg);
						bm = BitmapFactory
								.decodeStream(hr.getEntity().getContent());
					} catch (Exception e) {

						return null;
					}
					Drawable face_drawable = new BitmapDrawable(bm);
					face.setBackgroundDrawable(face_drawable);	
				}
					
				}
			Micropost_senderName.setText(mess.getNickname()); // 发消息的人
			Micropost_content.setText(mess.getContent()); // 消息内容
			// 消息日期 到时 根据拿到的数据在修改
			Micropost_date.setText(mess.getCreated_at() + ""); // 消息日期

			/***
			 * 
			 * 该处 留给 关注的 方法（ 应该只对 别人发的信息关注吧？？？？？） guanzhu 关注 按钮
			 * if(user_id.equals(mess.getUser_id())) {
			 * guanzhu.setVisibility(View.GONE); }
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * ******/

			if (user_id.equals(mess.getUser_id())) { // 主消息删除按钮 只是在本人时显示
			} else {
				button1.setVisibility(View.GONE);
			}
			button1.setTag(position);
			button1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int position = Integer.parseInt(v.getTag().toString());

					// 调用 删除 主消息的方法 （ id ） ??? 要不要给出提示 确认删除？？？
					// 删除消息 micropost_id
					// 删除成功的话,刷新界面
					list.remove(position);
					MicropostAdapter.this.notifyDataSetChanged();

				}
			});

			huifu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					huifu_num = huifu_num + 1;
					number = number + 1;
					View layout1 = view.findViewById(R.id.child_micropost); // 回复
					micropost_id = mess.getId();
					reciver_id = mess.getUser_id();
					reciver_types= mess.getUser_types();
//					micropost_id    reciver_id  reciver_types
//					Toast.makeText(getApplicationContext(), position+"----position", 1).show();
					
					
					child_list = new ArrayList<Child_Micropost>();
					Child_Micropost c1 = new Child_Micropost("1", "12",
							"student", "张", "若相守1",
							"http://csdnimg.cn/www/images/csdnindex_logo.gif",
							"asd斯蒂芬斯蒂芬反反复复反反复复反反复复反反复复吩咐", "张", "若相守1",
							"2013-2-2");
					Child_Micropost c2 = new Child_Micropost("1", "12",
							"student", "张", "若相守1",
							"http://csdnimg.cn/www/images/csdnindex_logo.gif",
							"asd斯蒂芬斯蒂芬反反复复反反复复反反复复反反复复吩咐", "张", "若相守1",
							"2013-2-2");
					Child_Micropost c3 = new Child_Micropost("1", "12",
							"student", "张", "若相守1",
							"http://csdnimg.cn/www/images/csdnindex_logo.gif",
							"asd斯蒂芬斯蒂芬反反复复反反复复反反复复反反复复吩咐", "张", "若相守1",
							"2013-2-2");
					Child_Micropost c4 = new Child_Micropost("1", "12",
							"student", "张", "若相守1",
							"http://csdnimg.cn/www/images/csdnindex_logo.gif",
							"asd斯蒂芬斯蒂芬反反复复反反复复反反复复反反复复吩咐", "张", "若相守1",
							"2013-2-2");

					child_list.add(c1);
					child_list.add(c2);
					child_list.add(c3);
					child_list.add(c4);

					Map<String, String> map = new HashMap<String, String>();
					map.put("user_id", user_id);
					map.put("class_id", class_id);
					map.put("micropost_id", micropost_id);

					String result = HomeWorkTool.doPost(
							Urlinterface.get_reply_microposts, map);
					if (result.length() !=0) {
						JSONObject array;
						try {
							array = new JSONObject(result);

							String status = array.getString("status");
							String notice = array.getString("notice");
							child_list = new ArrayList<Child_Micropost>();
							if ("success".equals(status)) {
								String micropostsListJson = array
										.getString("reply_microposts");
								JSONArray jsonArray2 = new JSONArray(
										micropostsListJson);

								int a = jsonArray2.length();
								// reply_microposts:[{id,sender_id,发送者id，sender_types发送者类型,
								// sender_name发送者姓名,sender_nickname发送者昵称，
								// sender_avatar_url发送者头像url,content消息内容,
								// reciver_name接受者姓名,reciver_nickname接受者昵称,
								// reciver_avatar_url接受者头像url,created_at创建时间}
								for (int i = 0; i < jsonArray2.length(); ++i) {
									JSONObject o = (JSONObject) jsonArray2
											.get(i);
									String id = o.getString("id");
									String sender_id = o.getString("sender_id");
									String sender_types = o
											.getString("sender_types");
									String sender_name = o
											.getString("sender_name");
									String sender_nickname = o
											.getString("sender_nickname");

									String sender_avatar_url = o
											.getString("sender_avatar_url");
									String content = o.getString("content");
									String reciver_name = o
											.getString("reciver_name");
									String reciver_nickname = o
											.getString("reciver_nickname");
									String reciver_avatar_url = o
											.getString("reciver_avatar_url");
									String created_at = o
											.getString("created_at");

									Child_Micropost child = new Child_Micropost(
											id, sender_id, sender_types,
											sender_name, sender_nickname,
											sender_avatar_url, content,
											reciver_name, reciver_nickname,
											created_at);
									child_list.add(child);
								}

							} else {
								Toast.makeText(getApplicationContext(), notice,
										1).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					// 内容
					//
					if (number == 1) {// 第一次点击时 确保点击 哪一个 都会显示
						layout1.setVisibility(View.VISIBLE);
						Reply_edit=  (EditText) layout1.findViewById(R.id.reply_edit);
						Reply_edit.setHint("回复  "+mess.getName()+":");
						listView2 = (ListView) layout1.findViewById(R.id.aa);
						listView2.setDivider(null);
						Adapter ad = new Adapter();
						listView2.setAdapter(ad);
						HomeWorkTool.setListViewHeightBasedOnChildren(listView2);

					}

					if (position_huifu_num == position && number != 1) {// 第一次点击以后的点击
																		// 确保连续点击
																		// 哪一个
																		// 2次时
																		// 都会显示正确的效果

						if (huifu_num == 1) {
							layout1.setVisibility(View.VISIBLE);
							Reply_edit=  (EditText) layout1.findViewById(R.id.reply_edit);
							Reply_edit.setHint("回复  "+mess.getName()+":");
							listView2 = (ListView) layout1
									.findViewById(R.id.aa);
							listView2.setDivider(null);
							Adapter ad = new Adapter();
							listView2.setAdapter(ad);
							HomeWorkTool.setListViewHeightBasedOnChildren(listView2);
						}
						if (huifu_num == 2) {
							layout1.setVisibility(View.GONE);
							huifu_num = 0;
						}
						// Toast.makeText(getApplicationContext(), huifu_num+"",
						// 0).show();

					}
					if (position_huifu_num != position && number != 1) {// 第一次点击以后的点击
																		// 确保连续点击不同的两个按钮时
																		// 都会显示正确的效果

						layout1.setVisibility(View.VISIBLE);
						Reply_edit=  (EditText) layout1.findViewById(R.id.reply_edit);
						Reply_edit.setHint("回复  "+mess.getName()+":");
						listView2 = (ListView) layout1.findViewById(R.id.aa);
						listView2.setDivider(null);
						Adapter ad = new Adapter();
						listView2.setAdapter(ad);
						HomeWorkTool.setListViewHeightBasedOnChildren(listView2);
						huifu_num = 1;

						// Toast.makeText(getApplicationContext(), huifu_num+"",
						// 0).show();

					}

					position_huifu_num = position;
				}
			});
			return view;
		}

	}

	public class Adapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			return child_list.size();// 数据总数
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return child_list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			Log.i("111111111", child_list.size() + "--");
			LayoutInflater inflater = Class_xinxiliu.this.getLayoutInflater();
			 View child_view = inflater.inflate(R.layout.child_micropost_item, null);

			ImageView face = (ImageView) child_view
					.findViewById(R.id.child_user_face);  // 头像
			TextView Micropost_whoToWho = (TextView) child_view
					.findViewById(R.id.child_message_senderName); //  张三 回复 李四

			TextView Micropost_content = (TextView) child_view
					.findViewById(R.id.child_micropost_content);  //   内容

			Button delete = (Button) child_view
					.findViewById(R.id.child_micropost_delete); // 删除
			Button reply = (Button) child_view
					.findViewById(R.id.child_micropost_huifu); // 回复

			final Child_Micropost child_Micropost = child_list.get(position);
			if (HomeWorkTool.isConnect(getApplicationContext())) {
				
			if (child_Micropost.getSender_avatar_url() != null) { // 设置头像
				HttpClient hc = new DefaultHttpClient();

				HttpGet hg = new HttpGet(child_Micropost.getSender_avatar_url());//
				final Bitmap bm;
				try {
					HttpResponse hr = hc.execute(hg);
					bm = BitmapFactory
							.decodeStream(hr.getEntity().getContent());
				} catch (Exception e) {

					return null;
				}
				Drawable face_drawable = new BitmapDrawable(bm);
				face.setBackgroundDrawable(face_drawable);
			}}
			Micropost_whoToWho.setText(child_Micropost.getSender_name() + "  回复   "
					+ child_Micropost.getReciver_name()); //
			Micropost_content.setText(child_Micropost.getContent() + " ("
					+ child_Micropost.getCreated_at() + ")"); // 消息内容
			
			
			 delete.setOnClickListener(new OnClickListener() {
			 @Override
			 public void onClick(View v) {
			 // TODO Auto-generated method stub
//			Toast.makeText(getApplicationContext(), position+"", 1).show();
				 
				 Reply_edit.setHint("回复  "+child_Micropost.getSender_name()+":");
				 micropost_id = child_Micropost.getId();
					reciver_id = child_Micropost.getSender_id();
					reciver_types= child_Micropost.getSender_types();
			}
			 });

			return child_view;
		}

	}
	
	

}
