package com.comdosoft.homework;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.homework.adapter.MainClssStuAdapter;
import com.comdosoft.homework.pojo.Child_Micropost;
import com.comdosoft.homework.pojo.ClassStuPojo;
import com.comdosoft.homework.pojo.Micropost;
import com.comdosoft.homework.pull.XListView;
import com.comdosoft.homework.pull.XListView.IXListViewListener;
import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.HomeWorkParams;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;


public class Classxinxiliu extends Activity implements IXListViewListener,HomeWorkParams,
Urlinterface {
	private XListView listView_mes;
	private MicropostAdapter micropostAdapter;
	private List<Micropost> list = new ArrayList<Micropost>();
	private List<Child_Micropost> child_list = new ArrayList<Child_Micropost>();

	private Bitmap bm = null;
	private View layout;
	private ListView listView2;
	private EditText fabiao_content;
	private String id = "1";  //  学生的 注册id ，，唯一
	private String user_id = "8"; // 学生 id 上面 会传过来的 学生student_id，
	private String user_name = "丁作强"; // 从拿到的班级信息中获取
	private String school_class_id = "1";// 班级id，学生所在班级
	private String micropost_id = "";// 主消息id
	private Handler handler;
	private String reciver_id = null; // 接收者 id
	private String user_types = "1";
	private String reciver_types = null;// 接收者 类型

	private EditText Reply_edit; // 回复 编辑框
	private int focus = -1;
	private int number = 0;
	private int position_huifu_num = -1;
	private int pages_count = 1;// 主消息 分页加载的 总页数
	private int page = 1; // 主消息 分页加载的  第几页
	private int child_pages_count = 1;//  子消息分页加载的 总页数
	private int child_page = 1; // 子消息 分页加载的  第几页
	private int micropost_type = 0; // 0 代表 全部 1 代表 有关我的
	private int  DelNum = -1;
	private List care = new ArrayList();  //  关注的消息 id  
	private String json = "";
	private HomeWork homework;

	private String user_Url;
	private ImageView main_class_oneIV;
	private TextView main_class_classesTv;
	private TextView main_class_oneTv1;
	private TextView main_class_oneTv2;
	private GridView main_class_classGv; 
	private Bitmap bitamp;
	private int width;
	private int height;
	private List<ClassStuPojo> stuList=new ArrayList<ClassStuPojo>();
	private int message_id=-1;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.class_middle);
		homework = (HomeWork) getApplication();
		Display display=this.getWindowManager().getDefaultDisplay();
		width=display.getWidth();
		height=display.getHeight();
		care = new ArrayList();

		SharedPreferences  preferences = getSharedPreferences(SHARED, Context.MODE_PRIVATE);

		user_id = preferences.getString("user_id", null);

		school_class_id = preferences.getString("school_class_id", null);



		//		 Intent intent = getIntent();//
		//		 json = intent.getStringExtra("json"); // 获得上个页面传过来的

		main_class_classGv=(GridView) findViewById(R.id.main_class_classGv);
		main_class_classesTv=(TextView) findViewById(R.id.main_class_classesTv);
		main_class_oneIV=(ImageView) findViewById(R.id.main_class_classes_include).findViewById(R.id.main_class_oneIV);
		main_class_oneTv1=(TextView)findViewById(R.id.main_class_classes_include).findViewById(R.id.main_class_oneTv1);
		main_class_oneTv2=(TextView)findViewById(R.id.main_class_classes_include).findViewById(R.id.main_class_oneTv2);
		fabiao_content = (EditText) findViewById(R.id.class_fabiao_content);
		listView_mes = (XListView) findViewById(R.id.pull_refresh_list);
		listView_mes.setPullLoadEnable(true);
		listView_mes.setDivider(null);

		thread.start();	
		handler = new Handler();
	}
	Thread thread=new Thread()
	{
		public void run()
		{
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("student_id", user_id);
				map.put("school_class_id", school_class_id);

				json = HomeWorkTool.sendGETRequest(Urlinterface.get_class_info, map);
				Message msg = new Message();//  创建Message 对象
				msg.what = 0;
				msg.obj = json;
				mHandler.sendMessage(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	Thread thread1 = new Thread(new Runnable() {
		public void run() {
			try {
				bitamp=BitmapFactory.decodeStream(new URL(user_Url).openConnection().getInputStream());
				mHandler.sendEmptyMessage(1);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	});
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				final String json =  (String) msg.obj;
				//						if (HomeWorkTool.isConnect(getApplicationContext())) {
				JSONObject array0;
				try {
					array0 = new JSONObject(json);
					JSONObject student = array0.getJSONObject("student");  //   获得学生的信息
					id = student.getString("id");
					user_id = student.getString("user_id");
					String avatar_url=student.getString("avatar_url");			//获取本人头像昂所有在地址
					String name=student.getString("name");						
					String nick_name=student.getString("nickname");
					String notice = array0.getString("notice");
					user_Url=Urlinterface.IP+avatar_url;
					thread1.start();
					main_class_oneTv1.setText(name);				//设置本名
					main_class_oneTv2.setText(nick_name);			//设置外号
					JSONObject class1 = array0.getJSONObject("class");  //  或得班级信息
					String class_name=class1.getString("name");			//获取class_name
					main_class_classesTv.setText(class_name);
					school_class_id = class1.getString("id");
					//循环获取班级学生的信息classmates
					JSONArray jsonArray = array0.getJSONArray("classmates");
					for(int i=0;i<jsonArray.length();i++)
					{ 
						JSONObject jsonObject2 = (JSONObject)jsonArray.get(i); 
						//						String stu_Url=(String) jsonObject2.get("avatar_url");
						String stu_Url=null;
						int id=jsonObject2.getInt("id");
						String stuname=jsonObject2.getString("name");
						String nickname=jsonObject2.getString("nickname");
						stuList.add(new ClassStuPojo(id,stuname,stu_Url,nickname));
					}
					main_class_classGv.setNumColumns(3);
					main_class_classGv.setAdapter(new MainClssStuAdapter(main_class_classGv,getApplicationContext(),stuList,width,height));
					JSONArray follow_microposts_id = array0.getJSONArray("follow_microposts_id");
					for (int i = 0; i < follow_microposts_id.length(); ++i) {
						int fmi = follow_microposts_id.getInt(i);
						care.add(fmi);
					}

					String micropostsListJson = array0
					.getString("microposts");
					JSONObject microposts = new JSONObject(
							micropostsListJson);
					page = Integer.parseInt(microposts.getString("page"));
					pages_count = Integer.parseInt(microposts
							.getString("pages_count"));
					String details_microposts = microposts
					.getString("details_microposts");
					// page":1,"pages_count":2,"details_microposts":
					JSONArray jsonArray2 = new JSONArray(details_microposts);

					for (int i = 0; i < jsonArray2.length(); ++i) {
						JSONObject o = (JSONObject) jsonArray2.get(i);
						String micropost_id = o.getString("micropost_id");
						String user_id = o.getString("user_id");
						String user_types = o.getString("user_types");
						String micropost_name = o.getString("name");

						String content = o.getString("content");
						String micropost_avatar_url = o.getString("avatar_url");
						String created_at = o.getString("created_at");
						String reply_microposts_count = o
						.getString("reply_microposts_count");

						Micropost micropost = new Micropost(micropost_id, user_id,
								user_types, micropost_name,content,
								micropost_avatar_url, created_at,
								reply_microposts_count);
						list.add(micropost);
					}	
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 


				//  查看   跳到本界面的   处理操作
				message_id  = homework.getMessage_id();
				if(message_id!=-1){
					homework.setMessage_id(-1);  //  将  公共变量message_id  设置为  -1
					child_list = new ArrayList<Child_Micropost>();
					//					

					final Handler mHandler = new Handler() {
						public void handleMessage(android.os.Message msg) {
							switch (msg.what) {
							case 0:
								final String json7 =  (String) msg.obj;
								child_list = new ArrayList<Child_Micropost>();
								if ("error".equals(json7)) {

								} else {
									JSONObject array;
									try {
										array = new JSONObject(json7);

										String status = array.getString("status");
										String notice = array.getString("notice");
										child_list = new ArrayList<Child_Micropost>();
										if ("success".equals(status)) {
											String micropostsListJson = array
											.getString("reply_microposts");
											JSONObject microposts = new JSONObject(
													micropostsListJson);
											child_page = Integer.parseInt(microposts.getString("page"));
											child_pages_count = Integer.parseInt(microposts
													.getString("pages_count"));
											String reply_microposts = microposts
											.getString("reply_microposts");
											JSONArray jsonArray2 = new JSONArray(
													reply_microposts);

											for (int i = 0; i < jsonArray2.length(); ++i) {
												JSONObject o = (JSONObject) jsonArray2
												.get(i);
												String id = o.getString("id");
												String sender_id = o
												.getString("sender_id");
												String sender_types = o
												.getString("sender_types");
												String sender_name = o
												.getString("sender_name");


												String sender_avatar_url = o
												.getString("sender_avatar_url");
												String content = o.getString("content");
												String reciver_name = o
												.getString("reciver_name");

												String reciver_avatar_url = o
												.getString("reciver_avatar_url");
												String created_at = o
												.getString("created_at");

												Child_Micropost child = new Child_Micropost(
														id, sender_id, sender_types,
														sender_name, 
														sender_avatar_url, content,
														reciver_name, 
														created_at);
												child_list.add(child);
											}

										} else {
											Toast.makeText(getApplicationContext(),
													notice, 1).show();
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}								

								break;
							default:
								break;									
							}
						}
					};
					Thread thread=new Thread()
					{
						public void run()
						{
							try {
								Map<String, String> map = new HashMap<String, String>();									
								map.put("micropost_id", message_id+"");
								String	re = HomeWorkTool.sendGETRequest(
										Urlinterface.get_reply_microposts, map);
								Message msg = new Message();//  创建Message 对象
								msg.what = 0;
								msg.obj = re;
								mHandler.sendMessage(msg);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					thread.start();	

					int a=0;
					for (int i = 0; i < list.size(); i++) {

						if (Integer.parseInt(list.get(i).getId())==message_id) {
							focus = i;   //   要展开的  主消息  的   位置
							number = 1;	

							micropost_type = 1;
							page = 1;
							break;
						}else{
							a= i+1;
						}
					}
					if(a==list.size()){//  若第一页主消息中没有  提示信息所在，，则  单独显示  该条提示信息

						String mess = homework.getNoselect_message();
						list.clear();
						//  该处解析  消息  json  并放入  list
						//						{"status":"success","micropost":{"content":"ull","created_at":"2014-01-16T17:51:43+08:00","id":1,"reply_microposts_count":0,"school_class_id":1,"updated_at":"2014-01-16T17:51:43+08:00","user_id":1,"user_types":1}}

						JSONObject js;
						try {
							js = new JSONObject(mess);
							String micropost = js.getString("micropost");
							JSONObject o =new JSONObject(micropost);
							String micropost_id = o.getString("micropost_id");
							String user_id = o.getString("user_id");
							String user_types = o.getString("user_types");
							String micropost_name = o.getString("name");

							String content = o.getString("content");
							String micropost_avatar_url = o.getString("avatar_url");
							//							String micropost_avatar_url = "";
							String created_at = o.getString("created_at");
							String reply_microposts_count = o
							.getString("reply_microposts_count");

							Micropost mic = new Micropost(micropost_id, user_id,
									user_types, micropost_name,content,
									micropost_avatar_url, created_at,
									reply_microposts_count);
							list.add(mic);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//						


					}

				}
				micropostAdapter = new MicropostAdapter();
				listView_mes.setAdapter(micropostAdapter);
				//				listView_mes.setSelection(focus);
				listView_mes.setXListViewListener(Classxinxiliu.this);
				handler = new Handler();
				break;
			case 1:
				main_class_oneIV.setImageBitmap(bitamp);
				break;
			default:
				break;
			}
			//			removeMessages(msg.what);
		}
	};



	// 发表
	public void class_fabiao(View v) {


		final Handler class_fabiaoHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					final String json1 =  (String) msg.obj;
					if (json1.length() != 0) {
						JSONObject array1;
						try {
							array1 = new JSONObject(json1);//
							String status = array1.getString("status");
							String notice = array1.getString("notice");

							if ("success".equals(status)) {
								Toast.makeText(getApplicationContext(), notice, 1)
								.show();
								fabiao_content.setText("");
								
							} else {
								Toast.makeText(getApplicationContext(), notice, 1)
								.show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {
						// Toast.makeText(getApplicationContext(), R.string.senderError,
						// 1)
						// .show();
						Toast.makeText(getApplicationContext(), "  result  :null", 1)
						.show();
					}
					break;
				default:
					break;

				}
			}
		};


		final String fabiaoContents = fabiao_content.getText().toString();
		if (fabiaoContents.length() == 0) {
			Toast.makeText(getApplicationContext(), "内容不能为空", 0).show();

		} else {

			Thread thread=new Thread()
			{
				public void run()
				{
					try {

						Map<String, String> map = new HashMap<String, String>();
						map.put("content", fabiaoContents);
						map.put("user_id", user_id);
						map.put("user_types",user_types);
						map.put("school_class_id", school_class_id);

						String 	senderjson = HomeWorkTool.doPost(Urlinterface.NEWS_RELEASE, map);
						Message msg = new Message();//  创建Message 对象
						msg.what = 0;
						msg.obj = senderjson;
						class_fabiaoHandler.sendMessage(msg);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			thread.start();	


		}

	}

	// 回复信息
	public void reply_message(View v) {

		final Handler mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					final String json2 =  (String) msg.obj;
					if (json2.length() == 0) {

					} else {
						JSONObject array2;
						try {
							array2 = new JSONObject(json2);//
							String status = array2.getString("status");
							String notice = array2.getString("notice");

							if ("success".equals(status)) {
								Toast.makeText(getApplicationContext(), notice, 0)
								.show();
								micropostAdapter = new MicropostAdapter();
								listView_mes.setAdapter(micropostAdapter);

								listView_mes.setSelection(focus);
							} else {
								Toast.makeText(getApplicationContext(), notice, 0)
								.show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				default:
					break;
				}
			}
		};
		json = "";
		String reply_edit = Reply_edit.getText().toString();
		if (reply_edit.length() == 0) {
			Toast.makeText(getApplicationContext(), "内容不能为空", 0).show();
		} else {
			Thread thread=new Thread()
			{
				public void run()
				{
					try {
						String reply_edit = Reply_edit.getText().toString();
						Map<String, String> map = new HashMap<String, String>();
						map.put("content", reply_edit);
						map.put("sender_id", user_id);
						map.put("sender_types", user_types);
						map.put("micropost_id", micropost_id);
						map.put("reciver_id", reciver_id);
						map.put("reciver_types", reciver_types);
						map.put("school_class_id", school_class_id);
						json = HomeWorkTool.doPost(Urlinterface.reply_message, map);
						Message msg = new Message();//  创建Message 对象
						msg.what = 0;
						msg.obj = json;
						mHandler.sendMessage(msg);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			thread.start();	
		}
	}

	/*
	 * 解析  全部  模块中的   主消息
	 * 
	 * 
	 * */
	void parseJson_all(String json3){

		if ("error".equals(json3)) {
		} else { 
			JSONObject array3;
			try {
				array3 = new JSONObject(json3);
				String status = array3.getString("status");
				String notice = array3.getString("notice");

				if ("success".equals(status)) {
					String micropostsListJson = array3
					.getString("microposts");
					JSONObject microposts = new JSONObject(
							micropostsListJson);
					page = Integer.parseInt(microposts
							.getString("page"));
					pages_count = Integer.parseInt(microposts
							.getString("pages_count"));
					String details_microposts = microposts
					.getString("details_microposts");
					// page":1,"pages_count":2,"details_microposts":
					JSONArray jsonArray2 = new JSONArray(
							details_microposts);
					for (int i = 0; i < jsonArray2.length(); ++i) {
						JSONObject o = (JSONObject) jsonArray2
						.get(i);
						String micropost_id = o.getString("micropost_id");
						String user_id = o.getString("user_id");
						String user_types = o
						.getString("user_types");
						String name = o.getString("name");											
						String content = o.getString("content");
						String avatar_url = o
						.getString("avatar_url");
						String created_at = o
						.getString("created_at");

						String reply_microposts_count = o
						.getString("reply_microposts_count");

						Micropost micropost = new Micropost(micropost_id,
								user_id, user_types, name,
								content, avatar_url,
								created_at, reply_microposts_count);
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

	}

	/*
	 * 解析  我的  模块中的   主消息
	 * 
	 * 
	 * */
	void parseJson_Myself(String json3){

		if ("error".equals(json3)) {

		} else {
			JSONObject array;
			try {
				array = new JSONObject(json3);

				Boolean status = array.getBoolean("status");
				String notice = array.getString("notice");

				if ( true ==status) {
					String micropostsListJson = array.getString("details_microposts");
					pages_count = Integer.parseInt(array.getString("pages_count"));

					JSONArray jsonArray2 = new JSONArray(micropostsListJson);

					for (int i = 0; i < jsonArray2.length(); ++i) {
						JSONObject o = (JSONObject) jsonArray2.get(i);
						String micropost_id = o.getString("micropost_id");
						String user_id = o.getString("user_id");
						String user_types = o.getString("user_types");
						String name = o.getString("name");
						String content = o.getString("content");
						String avatar_url = o.getString("avatar_url");
						String created_at = o.getString("created_at");
						String reply_microposts_count = o
						.getString("reply_microposts_count");
						Micropost micropost = new Micropost(micropost_id, user_id,
								user_types, name, content,
								avatar_url, created_at, reply_microposts_count);
						list.add(micropost);
					}
				} else {
					Toast.makeText(getApplicationContext(), notice, 1).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// 全部
	public void class_button_all(View v) {
		final Handler mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					final String json3 =  (String) msg.obj;
					parseJson_all(json3);

					micropostAdapter = new MicropostAdapter();
					listView_mes.setAdapter(micropostAdapter);
				default:
					break;
				}
			}
		};
		json="";
		Button b = (Button) findViewById(R.id.class_button_all);
		b.setBackgroundDrawable(getResources().getDrawable(R.drawable.an));
		Button b2 = (Button) findViewById(R.id.class_button_myself);
		b2.setBackgroundDrawable(getResources().getDrawable(R.drawable.an2));		
		focus=-1;
		micropost_type = 0;
		list.clear();
		Thread thread=new Thread()
		{
			public void run()
			{
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("student_id", user_id);
					map.put("school_class_id", school_class_id);
					map.put("page", "1");					
					json = HomeWorkTool.sendGETRequest(
							Urlinterface.GET_MICROPOSTS, map);
					Message msg = new Message();//  创建Message 对象
					msg.what = 0;
					msg.obj = json;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		thread.start();	
	}

	//	 我的
	public void class_button_myself(View v) {
		final Handler mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					final String jsonmyself =  (String) msg.obj;
					parseJson_Myself(jsonmyself);
					micropostAdapter = new MicropostAdapter();
					listView_mes.setAdapter(micropostAdapter);
					break;
				default:
					break;						
				}
			}
		};
		json="";
		Button b = (Button) findViewById(R.id.class_button_all);
		b.setBackgroundDrawable(getResources().getDrawable(R.drawable.an2));
		Button b2 = (Button) findViewById(R.id.class_button_myself);
		b2.setBackgroundDrawable(getResources().getDrawable(R.drawable.an));
		focus=-1;
		micropost_type = 1;
		page = 1;
		list.clear();		
		Thread thread=new Thread()
		{
			public void run()
			{
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("user_id", user_id);
					map.put("school_class_id", school_class_id);
					map.put("page", page + "");					
					json = HomeWorkTool.sendGETRequest(Urlinterface.MY_MICROPOSTS,
							map);
					Message msg = new Message();//  创建Message 对象
					msg.what = 0;
					msg.obj = json;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		thread.start();		
	}
	private void onLoad() {
		listView_mes.stopRefresh();
		listView_mes.stopLoadMore();
		listView_mes.setRefreshTime("刚刚");
	}
	@Override
	public void onRefresh() {

		focus=-1;
		page = 1;
		final Handler mHandleronRefresh = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					final String json_all =  (String) msg.obj;
					parseJson_all(json_all);
					micropostAdapter = new MicropostAdapter();
					listView_mes.setAdapter(micropostAdapter);
					onLoad();
					break;

				case 1:
					final String jsonmyself =  (String) msg.obj;
					parseJson_Myself(jsonmyself);
					micropostAdapter = new MicropostAdapter();
					listView_mes.setAdapter(micropostAdapter);
					onLoad();
					break;	 
				}
			}
		};


		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				list.clear();

				Thread thread=new Thread()
				{
					public void run()
					{//  
						// 获得第一页信息

						// if (HomeWorkTool.isConnect(getApplicationContext())) {
						if (micropost_type == 0) {// 全部
							Map<String, String> map = new HashMap<String, String>();
							map.put("student_id", user_id);
							map.put("school_class_id", school_class_id);
							map.put("page", "1");

							String result = "";
							try {
								result = HomeWorkTool.sendGETRequest(
										Urlinterface.GET_MICROPOSTS, map);
								Message msg = new Message();//  创建Message 对象
								msg.what = 0;
								msg.obj = result;
								mHandleronRefresh.sendMessage(msg);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}

						if (micropost_type == 1) { // 有关我的

							Map<String, String> map = new HashMap<String, String>();
							map.put("user_id", user_id);
							map.put("school_class_id", school_class_id);
							map.put("page", page + "");
							String result = "";
							try {
								result = HomeWorkTool.sendGETRequest(
										Urlinterface.MY_MICROPOSTS, map);
								Message msg = new Message();//  创建Message 对象
								msg.what = 1;
								msg.obj = result;
								mHandleronRefresh.sendMessage(msg);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}

					}
				};
				thread.start();	



			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {

		final Handler mHandleronLoadMore = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					final String json_all =  (String) msg.obj;
					parseJson_all(json_all);
					micropostAdapter.notifyDataSetChanged();
					onLoad();
					break;

				case 1:
					final String jsonmyself =  (String) msg.obj;
					parseJson_Myself(jsonmyself);
					micropostAdapter.notifyDataSetChanged();
					onLoad();
					break;	 
				}
			}
		};

		focus=-1;
		page = page + 1;

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {

				Thread thread=new Thread()
				{
					public void run()
					{//  全部      页面加载 更多
						if (page <= pages_count && micropost_type == 0) {
							// get 参数school_class_id student_id page

							Map<String, String> map = new HashMap<String, String>();
							map.put("student_id", user_id);
							map.put("school_class_id", school_class_id);
							map.put("page", page + "");
							String result = "";
							try {
								result = HomeWorkTool.sendGETRequest(
										Urlinterface.GET_MICROPOSTS, map);

							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							Message msg = new Message();//  创建Message 对象
							msg.what = 0;
							msg.obj = result;
							mHandleronLoadMore.sendMessage(msg);

						}

						//  我的      页面加载 更多
						if (page <= pages_count &&micropost_type == 1) {
							Map<String, String> map = new HashMap<String, String>();
							map.put("user_id", user_id);
							map.put("school_class_id", school_class_id);
							map.put("page", page+"");

							String result = "";
							try {
								result = HomeWorkTool.sendGETRequest(
										Urlinterface.MY_MICROPOSTS, map);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							Message msg = new Message();//  创建Message 对象
							msg.what = 1;
							msg.obj = result;
							mHandleronLoadMore.sendMessage(msg);
						}

					}
				};
				thread.start();	

			}
		}, 2000);
		//		micropostAdapter.notifyDataSetChanged();
		//		onLoad();
	}

	public class MicropostAdapter extends BaseAdapter {

		int huifu_num = 0;

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

		public View getView(final int position, View convertView,
				ViewGroup parent) {

			Log.i("111111111", list.size() + "--");
			LayoutInflater inflater = Classxinxiliu.this.getLayoutInflater();
			final View view = inflater.inflate(R.layout.micropost_item, null);

			final	ImageView face = (ImageView) view.findViewById(R.id.user_face); // 头像
			TextView Micropost_senderName = (TextView) view
			.findViewById(R.id.message_senderName); // 谁发的
			ImageButton button1 = (ImageButton) view.findViewById(R.id.button1); // 删除按钮
			TextView Micropost_content = (TextView) view
			.findViewById(R.id.micropost_content); // 消息内容
			TextView Micropost_date = (TextView) view
			.findViewById(R.id.micropost_date); // 日期
			final Button guanzhu = (Button) view.findViewById(R.id.micropost_guanzhu); // 关注
			Button lookMore = (Button) view.findViewById(R.id.lookMore); // 关注
			Button huifu = (Button) view.findViewById(R.id.micropost_huifu); // 回复

			layout = view.findViewById(R.id.child_micropost); // 回复 隐藏的 内容

			final Micropost mess = list.get(position);


			if (HomeWorkTool.isConnect(getApplicationContext())) {

				if ("null".equals(mess.getAvatar_url())) { // 设置头像

				}else {	
					final Handler childHandler = new Handler() {
						public void handleMessage(android.os.Message msg) {
							switch (msg.what) {
							case 0:
								final Drawable face_drawable =  (Drawable) msg.obj;
								face.setBackgroundDrawable(face_drawable);
								break;
							default:
								break;

							}
						}
					};
					Thread thread=new Thread()
					{
						public void run()
						{
							try {

								HttpClient hc = new DefaultHttpClient();

								HttpGet hg = new HttpGet(Urlinterface.IP
										+ mess.getAvatar_url());//

								try {
									HttpResponse hr = hc.execute(hg);
									bm = BitmapFactory.decodeStream(hr.getEntity()
											.getContent());
									Drawable face_drawable = new BitmapDrawable(bm);

									Message msg = new Message();//  创建Message 对象
									msg.what = 0;
									msg.obj = face_drawable;

									childHandler.sendMessage(msg);
								} catch (Exception e) {

								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					thread.start();


				}
			}

			Micropost_senderName.setText(mess.getName()); // 发消息的人

			Micropost_content.setText(mess.getContent()); // 消息内容

			// 消息日期 到时 根据拿到的数据在修改
			// SimpleDateFormat dateformat1=new
			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// String a1=dateformat1.format(new Date(mess.getCreated_at()));
			Micropost_date.setText(divisionTime(mess.getCreated_at())); // 消息日期
			int mic_id =  Integer.parseInt(mess.getId());
			for (int i = 0; i < care.size(); i++) {
				int a=	(Integer) care.get(i);
				if (a==mic_id) {
					guanzhu.setText("已关注");  //  显示  已关注
				}
			}


			guanzhu.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v) {
					final Handler gzHandler=new Handler()
					{
						public void handleMessage(android.os.Message msg) {
							JSONObject jsonobject;
							switch (msg.what) {
							case 0:
								try {
									jsonobject = new JSONObject(msg.obj.toString());
									String status=jsonobject.getString("status");
									String notic=jsonobject.getString("notice");
									if(status.equals("success"))
									{
										care.add(mess.getId());  
										guanzhu.setText("已关注");
									}
									Toast.makeText(getApplicationContext(), notic, 0).show();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							case 1:
								try {
									jsonobject = new JSONObject(msg.obj.toString());
									String status=jsonobject.getString("status");
									String notic=jsonobject.getString("notice");
									if(status.equals("success"))
									{
										care.remove(mess.getId());  
										guanzhu.setText("关注");
									}
									Toast.makeText(getApplicationContext(), notic, 0).show();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							default:
								break;

							}
						}
					};
					Thread gzthread=new Thread()
					{
						@SuppressWarnings({ "rawtypes", "unchecked" })
						public void run()
						{
							try {
								HashMap<String,String> mp=new HashMap();
								mp.put("user_id", String.valueOf(user_id));
								mp.put("micropost_id", String.valueOf(mess.getId()));
								String str=null;
								Message msg = new Message();//  创建Message 对象
								if(guanzhu.getText().equals("关注"))
								{
									str=HomeWorkTool.sendGETRequest(Urlinterface.add_concern, mp);
									msg.what = 0;
									msg.obj = str;
								}
								else if(guanzhu.getText().equals("已关注"))
								{
									str=HomeWorkTool.sendGETRequest(Urlinterface.unfollow, mp);
									msg.what = 1;
									msg.obj = str;
								}
								gzHandler.sendMessage(msg);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					gzthread.start();
				}
			});

			if (user_id.equals(mess.getUser_id())) { // 主消息删除按钮 只是在本人时显示
			} else {
				button1.setVisibility(View.GONE);
			}
			button1.setTag(position);
			button1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//					String position = v.getTag().toString();


					final Handler mHandler = new Handler() {
						public void handleMessage(android.os.Message msg) {
							switch (msg.what) {
							case 0:
								final String json5 =  (String) msg.obj;
								if (json5.equals("error")) {

								} else {
									JSONObject array;
									try {
										array = new JSONObject(json5);//
										String status = array.getString("status");
										String notice = array.getString("notice");

										if ("success".equals(status)) {
											// 删除成功的话,刷新界面
											list.remove(DelNum);
											micropostAdapter = new MicropostAdapter();
											listView_mes.setAdapter(micropostAdapter);

										}
										Toast.makeText(getApplicationContext(), notice, 1)
										.show();
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								break;
							default:
								break;

							}
						}
					};
					json="";
					DelNum = Integer.parseInt(v.getTag().toString());
					Thread thread=new Thread()
					{
						public void run()
						{
							try {


								Map<String, String> map = new HashMap<String, String>();
								map.put("micropost_id", mess.getId()+"");

								json = HomeWorkTool.sendGETRequest(
										Urlinterface.DELETE_POSTS, map);
								Message msg = new Message();//  创建Message 对象
								msg.what = 0;
								msg.obj = json;
								mHandler.sendMessage(msg);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					thread.start();	


				}
			});

			View layout1 = view.findViewById(R.id.child_micropost); // 回复
			micropost_id = mess.getId();// 点击 回复 默认 给主消息回复 记录 主消息 id
			reciver_id = mess.getUser_id();
			reciver_types = mess.getUser_types();
			if (focus == position && number == 1) {
				layout1.setVisibility(View.VISIBLE);
				Reply_edit = (EditText) layout1.findViewById(R.id.reply_edit);
				Reply_edit.requestFocus();
				Reply_edit.setHint(user_name + " "+HomeWorkParams.REPLY+" " + mess.getName() + ":");
				
				final Handler mHandler = new Handler() {
					public void handleMessage(android.os.Message msg) {
						switch (msg.what) {
						case 0:
							final String json7 =  (String) msg.obj;
							child_list = new ArrayList<Child_Micropost>();
							if ("error".equals(json7)) {

							} else {
								JSONObject array;
								try {
									array = new JSONObject(json7);

									String status = array.getString("status");
									String notice = array.getString("notice");
									child_list = new ArrayList<Child_Micropost>();
									if ("success".equals(status)) {
										String micropostsListJson = array
										.getString("reply_microposts");
										JSONObject microposts = new JSONObject(
												micropostsListJson);
										child_page = Integer.parseInt(microposts.getString("page"));
										child_pages_count = Integer.parseInt(microposts
												.getString("pages_count"));
										String reply_microposts = microposts
										.getString("reply_microposts");
										JSONArray jsonArray2 = new JSONArray(
												reply_microposts);

										for (int i = 0; i < jsonArray2.length(); ++i) {
											JSONObject o = (JSONObject) jsonArray2
											.get(i);
											String id = o.getString("id");
											String sender_id = o
											.getString("sender_id");
											String sender_types = o
											.getString("sender_types");
											String sender_name = o
											.getString("sender_name");


											String sender_avatar_url = o
											.getString("sender_avatar_url");
											String content = o.getString("content");
											String reciver_name = o
											.getString("reciver_name");

											String reciver_avatar_url = o
											.getString("reciver_avatar_url");
											String created_at = o
											.getString("created_at");

											Child_Micropost child = new Child_Micropost(
													id, sender_id, sender_types,
													sender_name, 
													sender_avatar_url, content,
													reciver_name, 
													created_at);
											child_list.add(child);
										}

									} else {
										Toast.makeText(getApplicationContext(),
												notice, 1).show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}								
							
							break;
						default:
							break;									
						}
					}
				};
				Thread thread=new Thread()
				{
					public void run()
					{
						try {
							Map<String, String> map = new HashMap<String, String>();									
							map.put("micropost_id", micropost_id);
							json = HomeWorkTool.sendGETRequest(
									Urlinterface.get_reply_microposts, map);
							Message msg = new Message();//  创建Message 对象
							msg.what = 0;
							msg.obj = json;
							mHandler.sendMessage(msg);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				thread.start();	
				
				
				if (child_list.size()!=0) {
					listView2 = (ListView) layout1.findViewById(R.id.aa);
					listView2.setDivider(null);
					Adapter ad = new Adapter();
					listView2.setAdapter(ad);
					HomeWorkTool.setListViewHeightBasedOnChildren(listView2);
				}

			}

			
			
			
			
			if (mess.getReply_microposts_count()!=null) {
				huifu.setText(HomeWorkParams.REPLY+"("+mess.getReply_microposts_count() +")");	
			}
			//  查看更多  
			if (child_list.size()==0) {
				lookMore.setVisibility(View.GONE);
			}
			lookMore.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// huifu_num = huifu_num + 1;

					final Handler mHandler = new Handler() {
						public void handleMessage(android.os.Message msg) {
							switch (msg.what) {
							case 0:
								final String json6 =  (String) msg.obj;
								if ("error".equals(json6)) {

								} else {
									JSONObject array;
									try {
										array = new JSONObject(json6);

										String status = array.getString("status");
										String notice = array.getString("notice");

										if ("success".equals(status)) {
											String micropostsListJson = array
											.getString("reply_microposts");
											JSONObject microposts = new JSONObject(
													micropostsListJson);
											child_page = Integer.parseInt(microposts.getString("page"));
											child_pages_count = Integer.parseInt(microposts
													.getString("pages_count"));
											String reply_microposts = microposts
											.getString("reply_microposts");
											JSONArray jsonArray2 = new JSONArray(
													reply_microposts);

											for (int i = 0; i < jsonArray2.length(); ++i) {
												JSONObject o = (JSONObject) jsonArray2
												.get(i);
												String id = o.getString("id");
												String sender_id = o
												.getString("sender_id");
												String sender_types = o
												.getString("sender_types");
												String sender_name = o
												.getString("sender_name");

												String sender_avatar_url = o
												.getString("sender_avatar_url");
												String content = o.getString("content");
												String reciver_name = o
												.getString("reciver_name");

												String reciver_avatar_url = o
												.getString("reciver_avatar_url");
												String created_at = o
												.getString("created_at");

												Child_Micropost child = new Child_Micropost(
														id, sender_id, sender_types,
														sender_name, 
														sender_avatar_url, content,
														reciver_name, 
														created_at);
												child_list.add(child);
											}

										} else {
											Toast.makeText(getApplicationContext(),
													notice, 1).show();
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
								micropostAdapter = new MicropostAdapter();
								listView_mes.setAdapter(micropostAdapter);
								//}
								listView_mes.setSelection(focus);
								break;
							default:
								break;

							}
						}
					};
					child_page=child_page+1;
					if (child_page<=child_pages_count) {
						if (HomeWorkTool.isConnect(getApplicationContext())) {
							Thread thread=new Thread()
							{
								public void run()
								{
									try {
										Map<String, String> map = new HashMap<String, String>();
										map.put("micropost_id", micropost_id);
										map.put("page", child_page+"");
										String	js = HomeWorkTool.sendGETRequest(
												Urlinterface.get_reply_microposts, map);
										Message msg = new Message();//  创建Message 对象
										msg.what = 0;
										msg.obj = js;
										mHandler.sendMessage(msg);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							};
							thread.start();	
						}					
					}						
				}
			});

			// 点击 回复 默认 给主消息回复
			huifu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// huifu_num = huifu_num + 1;

					number = number + 1;
					if (number == 2) {
						number = 0;
					}
					if (position_huifu_num != position) {
						number = 1;
					}

					focus = position;
					child_list = new ArrayList<Child_Micropost>();
					//					
					json="";

			
					micropostAdapter = new MicropostAdapter();
					listView_mes.setAdapter(micropostAdapter);

					listView_mes.setSelection(focus);
					position_huifu_num = position;
				}
			});
			return view;
		}
	}
	//分割时间
	public String divisionTime(String timeStr)
	{
		int temp1=timeStr.indexOf("T");
		int temp2=timeStr.lastIndexOf("+");
		return timeStr.substring(0, temp1)+" "+timeStr.substring(temp1+1, temp2);
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
			LayoutInflater inflater = Classxinxiliu.this.getLayoutInflater();
			View child_view = inflater.inflate(R.layout.child_micropost_item,
					null);

			final ImageView face = (ImageView) child_view
			.findViewById(R.id.child_user_face); // 头像
			TextView Micropost_whoToWho = (TextView) child_view
			.findViewById(R.id.child_message_senderName); // 张三 回复 李四

			TextView Micropost_content = (TextView) child_view
			.findViewById(R.id.child_micropost_content); // 内容

			Button delete = (Button) child_view
			.findViewById(R.id.child_micropost_delete); // 删除
			Button reply = (Button) child_view
			.findViewById(R.id.child_micropost_huifu); // 回复

			final Child_Micropost child_Micropost = child_list.get(position);
			if (HomeWorkTool.isConnect(getApplicationContext())) {

				if (child_Micropost.getSender_avatar_url() != null) { // 设置头像

					final Handler childHandler = new Handler() {
						public void handleMessage(android.os.Message msg) {
							switch (msg.what) {
							case 0:
								final Drawable face_drawable =  (Drawable) msg.obj;
								face.setBackgroundDrawable(face_drawable);
								break;
							default:
								break;

							}
						}
					};
					Thread thread=new Thread()
					{
						public void run()
						{
							try {

								HttpClient hc = new DefaultHttpClient();

								HttpGet hg = new HttpGet(Urlinterface.IP
										+ child_Micropost.getSender_avatar_url());//

								try {
									HttpResponse hr = hc.execute(hg);
									bm = BitmapFactory.decodeStream(hr.getEntity()
											.getContent());
									Drawable face_drawable = new BitmapDrawable(bm);

									Message msg = new Message();//  创建Message 对象
									msg.what = 0;
									msg.obj = face_drawable;

									childHandler.sendMessage(msg);
								} catch (Exception e) {

								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					thread.start();
					Drawable face_drawable = new BitmapDrawable(bm);
					face.setBackgroundDrawable(face_drawable);
				}
			}
			Micropost_whoToWho.setText(child_Micropost.getSender_name()
					+ "  回复   " + child_Micropost.getReciver_name()); //
			// SimpleDateFormat dateformat1=new
			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// String a1=dateformat1.format(new
			// Date(child_Micropost.getCreated_at()));
			Micropost_content.setText(child_Micropost.getContent() + " ("
					+ divisionTime(child_Micropost.getCreated_at()) + ")"); // 消息内容

			if (user_id.equals(child_Micropost.getSender_id())||user_id.equals(list.get(focus).getUser_id())) {
				delete.setVisibility(View.VISIBLE);
			}
			delete.setTag(position);
			delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					DelNum = Integer.parseInt(v.getTag().toString());
					json="";
					final Handler mHandler = new Handler() {
						public void handleMessage(android.os.Message msg) {
							switch (msg.what) {
							case 0:
								final String json8 =  (String) msg.obj;


								if (json8.length() != 0) {
									JSONObject array;
									try {
										array = new JSONObject(json8);//
										String status = array.getString("status");
										String notice = array.getString("notice");

										if ("success".equals(status)) {
											// 删除成功的话,刷新界面
											// 删除成功的话,刷新界面
											child_list.remove(DelNum);
											micropostAdapter = new MicropostAdapter();
											listView_mes.setAdapter(micropostAdapter);
											listView_mes.setSelection(focus);

										}
										Toast.makeText(getApplicationContext(), notice, 1)
										.show();
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								break;
							default:
								break;

							}
						}
					};
					Thread thread=new Thread()
					{
						public void run()
						{
							try {

								Map<String, String> map = new HashMap<String, String>();
								map.put("reply_micropost_id", child_Micropost.getId());

								json = HomeWorkTool.doPost(
										Urlinterface.DELETE_REPLY_POSTS, map);
								Message msg = new Message();//  创建Message 对象
								msg.what = 0;
								msg.obj = json;
								mHandler.sendMessage(msg);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					thread.start();	


					//					Toast.makeText(getApplicationContext(), "notice+", 1)
					//							.show();
				}
			});

			reply.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Reply_edit.setHint(user_name + " 回复  "
							+ child_Micropost.getSender_name() + " :");

					reciver_id = child_Micropost.getSender_id();
					reciver_types = child_Micropost.getSender_types();
				}
			});
			return child_view;
		}

	}

}
