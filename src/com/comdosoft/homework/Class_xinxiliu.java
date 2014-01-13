package com.comdosoft.homework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
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

import com.comdosoft.homework.adapter.Adapter;
import com.comdosoft.homework.pojo.Micropost;
import com.comdosoft.homework.pull.XListView;
import com.comdosoft.homework.pull.XListView.IXListViewListener;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;

public class Class_xinxiliu extends Activity implements IXListViewListener,
		Urlinterface {
	private XListView listView_mes;
	private MicropostAdapter adapter;
	private List<Micropost> list = new ArrayList<Micropost>();
	private Handler mHandler;
	private int start = 0;
	private int page = 1;
	private static int refreshCnt = 0;
	private View layout;
	private ListView listView2;
	private EditText fabiao_content;
	private String school_class_id; // 学生所在班级
	private String user_id; // 学生 id 上面 会传过来的

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.class_middle);

		Intent intent = getIntent();//
		user_id = intent.getStringExtra("user_id"); // 获得上个页面传过来的 user_id

		fabiao_content = (EditText) findViewById(R.id.class_fabiao_content);

		listView_mes = (XListView) findViewById(R.id.pull_refresh_list);
		listView_mes.setPullLoadEnable(true);
		listView_mes.setDivider(null);
		// (String id, String user_id, String user_types, String name,
		// String nickname, String content, String avatar_url, Long created_at)
		// {

		Micropost m1 = new Micropost("1", "12", "student", "张", "若相守1",
				"etwevececx2423 sdfd",
				"http://csdnimg.cn/www/images/csdnindex_logo.gif",
				(long) 234124);
		Micropost m2 = new Micropost("2", "12", "student", "张", "若相守2",
				"etwevececx2423 sdfd",
				"http://csdnimg.cn/www/images/csdnindex_logo.gif",
				(long) 234124);
		Micropost m3 = new Micropost("3", "12", "student", "张", "若相守3",
				"etwevececx2423 sdfd",
				"http://csdnimg.cn/www/images/csdnindex_logo.gif",
				(long) 234124);

		list.add(m1);
		list.add(m2);
		list.add(m3);

		adapter = new MicropostAdapter();
		listView_mes.setAdapter(adapter);

		listView_mes.setXListViewListener(this);
		mHandler = new Handler();
	}

	// 发表
	public void class_fabiao(View v) {
		String result = null;
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

				result = HomeWorkTool
						.sendGETRequest(
								"http://192.168.199.121:3000/api/students/news_release",
								map);
				// result = HomeWorkTool.sendGETRequest(NEWS_RELEASE, map);
				// Toast.makeText(getApplicationContext(), NEWS_RELEASE,
				// 1).show();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Toast.makeText(getApplicationContext(), "方法执行---result：" + result,
					0).show();
			if (result != null && result.length() != 0) {
				JSONObject array;
				try {
					array = new JSONObject(result);// {"status":"success","notice":"\u52a0\u8f7d\u5b8c\u6210","teacher_id":29,"teacher_name":"gs",
					// "classmates":[{"edu_number":"100101012","id":28,"name":"student_2"},{"edu_number":"100101013","id":29,"name":"student_3"},{"edu_number":"100101014","id":30,"name":"student_4"},{"edu_number":"100101015","id":31,"name":"student_5"},{"edu_number":"100101016","id":32,"name":"student_6"},{"edu_number":"100101017","id":33,"name":"student_7"},{"edu_number":"100101018","id":34,"name":"student_8"},{"edu_number":"100101019","id":35,"name":"student_9"},{"edu_number":"56789","id":36,"name":"ddd"}]}

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
			} else {
				Toast.makeText(getApplicationContext(),
						"方法执行---result为null：" + result, 0).show();
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
				(long) 234124);
		Micropost m2 = new Micropost("2", "12", "student", "张", "若相守2",
				"etwevececx2423 sdfd",
				"http://csdnimg.cn/www/images/csdnindex_logo.gif",
				(long) 234124);
		Micropost m3 = new Micropost("3", "12", "student", "张", "若相守3",
				"etwevececx2423 sdfd",
				"http://csdnimg.cn/www/images/csdnindex_logo.gif",
				(long) 234124);

		list.add(m1);
		list.add(m2);
		list.add(m3);

		// mAdapter.notifyDataSetChanged();
		adapter = new MicropostAdapter();
		listView_mes.setAdapter(adapter);
		// Toast.makeText(getApplicationContext(), "方法没写", 1).show();
	}

	// 我的
	public void class_button_myself(View v) {

		list.clear();

		// 获得第一页信息

		Micropost m1 = new Micropost("1", "12", "student", "张", "若相守11",
				"etwevececx2423 sdfd",
				"http://csdnimg.cn/www/images/csdnindex_logo.gif",
				(long) 234124);
		Micropost m2 = new Micropost("2", "12", "student", "张", "若相守22",
				"etwevececx2423 sdfd",
				"http://csdnimg.cn/www/images/csdnindex_logo.gif",
				(long) 234124);
		Micropost m3 = new Micropost("3", "12", "student", "张", "若相守33",
				"etwevececx2423 sdfd",
				"http://csdnimg.cn/www/images/csdnindex_logo.gif",
				(long) 234124);

		list.add(m1);
		list.add(m2);
		list.add(m3);

		// mAdapter.notifyDataSetChanged();
		adapter = new MicropostAdapter();
		listView_mes.setAdapter(adapter);
		// Toast.makeText(getApplicationContext(), "方法没写", 1).show();
	}

	// 回复
	int huifu = 0;

	public void partents_huifu(View v) {

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
<<<<<<< HEAD

				// 获得第一页信息

				Micropost m1 = new Micropost("1", "12", "student", "张", "若相守1",
						"etwevececx2423 sdfd",
						"http://csdnimg.cn/www/images/csdnindex_logo.gif",
						(long) 234124);
				Micropost m2 = new Micropost("2", "12", "student", "张", "若相守2",
						"etwevececx2423 sdfd",
						"http://csdnimg.cn/www/images/csdnindex_logo.gif",
						(long) 234124);
				Micropost m3 = new Micropost("3", "12", "student", "张", "若相守3",
						"etwevececx2423 sdfd",
						"http://csdnimg.cn/www/images/csdnindex_logo.gif",
						(long) 234124);

				list.add(m1);
				list.add(m2);
				list.add(m3);

=======
				
				//   获得第一页信息
				
				Micropost m1 = new Micropost("1","12","student","张","若相守1","etwevececx2423as sdfd","http://csdnimg.cn/www/images/csdnindex_logo.gif",(long) 234124);
				Micropost m2 = new Micropost("2","12","student","张","若相守2","etwevececx2423 sdfd","http://csdnimg.cn/www/images/csdnindex_logo.gif",(long) 234124);
				Micropost m3 = new Micropost("3","12","student","张","若相守3","etwevececx2423 sdfd","http://csdnimg.cn/www/images/csdnindex_logo.gif",(long) 234124);
				
				
				list.add(m1);list.add(m2);list.add(m3);
				
>>>>>>> 7f2e6bd8da4f974e533f4ed51f43a0b3fbf8aced
				// mAdapter.notifyDataSetChanged();
				adapter = new MicropostAdapter();
				listView_mes.setAdapter(adapter);
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
						(long) 234124);

				list.add(m4);
				adapter.notifyDataSetChanged();
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

			Micropost mess = list.get(position);

			// Micropost_senderName.setText(mess.getSender_name());
			// Micropost_content.setText(mess.getSender_content());
			// SimpleDateFormat dateformat1=new
			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// String a1=dateformat1.format(new Date(mess.getReceiver_date()));
			//
			// Micropost_date.setText(a1);
			if (mess.getAvatar_url()!=null) {  //  设置头像
				 Bitmap dBitmap = BitmapFactory.decodeFile(mess.getAvatar_url());
				 Drawable face_drawable= new BitmapDrawable(dBitmap);
				 face.setBackgroundDrawable(face_drawable); 
			}
			Micropost_senderName.setText(mess.getNickname());  //  发消息的人
			Micropost_content.setText(mess.getContent());   //   消息内容
			//消息日期   到时  根据拿到的数据在修改
			Micropost_content.setText(mess.getCreated_at()+"");   //   消息日期
			
		/***
		 * 
		 *   该处  留给     关注的  方法（   应该只对  别人发的信息关注吧？？？？？）
		 *   guanzhu   关注  按钮  
		 *   if(user_id.equals(mess.getUser_id())) {  
		 *   guanzhu.setVisibility(View.GONE);
			}
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * ******/
			
			
			
			
			if (user_id.equals(mess.getUser_id())) {  //  主消息删除按钮  只是在本人时显示
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
																			// 隐藏的
																			// 内容
					//
					if (number == 1) {// 第一次点击时 确保点击 哪一个 都会显示
						layout1.setVisibility(View.VISIBLE);
						listView2 = (ListView) layout1.findViewById(R.id.aa);
						listView2.setDivider(null);
						Adapter ad = new Adapter(layout1.getContext(), list,
								R.layout.child_micropost_item);
						listView2.setAdapter(ad);
						HomeWorkTool
								.setListViewHeightBasedOnChildren(listView2);

					}

					if (position_huifu_num == position && number != 1) {// 第一次点击以后的点击
																		// 确保连续点击
																		// 哪一个
																		// 2次时
																		// 都会显示正确的效果

						if (huifu_num == 1) {
							layout1.setVisibility(View.VISIBLE);
							listView2 = (ListView) layout1
									.findViewById(R.id.aa);
							listView2.setDivider(null);
							Adapter ad = new Adapter(layout1.getContext(),
									list, R.layout.child_micropost_item);
							listView2.setAdapter(ad);
							HomeWorkTool
									.setListViewHeightBasedOnChildren(listView2);
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
						listView2 = (ListView) layout1.findViewById(R.id.aa);
						listView2.setDivider(null);
						Adapter ad = new Adapter(layout1.getContext(), list,
								R.layout.child_micropost_item);
						listView2.setAdapter(ad);
						HomeWorkTool
								.setListViewHeightBasedOnChildren(listView2);
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

}
