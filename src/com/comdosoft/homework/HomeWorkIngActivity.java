package com.comdosoft.homework;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.homework.pojo.ListeningPojo;
import com.comdosoft.homework.pojo.QuestionPojo;
import com.comdosoft.homework.pojo.WorkPojo;
import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.HomeWorkParams;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.ListeningQuestionList;
import com.comdosoft.homework.tools.Urlinterface;

public class HomeWorkIngActivity extends Activity implements Urlinterface {
	// private String json =
	// "{\"status\":\"success\",\"notice\":\"\u767b\u9646\u6210\u529f\uff01\",\"student\":{\"id\":111,\"name\":\"nameeeeee\",\"user_id\":15,\"nickname\":\"11111nickname\",\"avatar_url\":\"/homework_system/avatars/students/2014-01-/student_111.png\"},\"class\":{\"id\":1,\"name\":\"eeeee\",\"tearcher_name\":\"tea\",\"tearcher_id\":1},\"classmates\":[{\"avatar_url\":\"/homework_system/avatars/students/2014-01/student_1.png\",\"id\":1,\"name\":\"tea\",\"nickname\":\"\u4e0a\u5584\u82e5\u6c34\"}],\"task_messages\":[],\"microposts\":{\"page\":1,\"pages_count\":11,\"details_microposts\":[{\"avatar_url\":\"/homework_system/avatars/students/2014-01/student_1.png\",\"content\":\"KKK\",\"created_at\":\"2014-01-16T15:50:47+08:00\",\"micropost_id\":22,\"name\":\"tea\",\"reply_microposts_count\":null,\"user_id\":1,\"user_types\":1},{\"avatar_url\":\"/homework_system/avatars/students/2014-01/student_1.png\",\"content\":\"65469855655\",\"created_at\":\"2014-01-16T15:19:26+08:00\",\"micropost_id\":21,\"name\":\"tea\",\"reply_microposts_count\":null,\"user_id\":1,\"user_types\":1}]},\"daily_tasks\":[{\"id\":1,\"name\":\"package1\",\"start_time\":\"2014-01-14T18:35:46+08:00\",\"end_time\":\"2014-01-30T02:35:46+08:00\",\"question_packages_url\":\"31312312313123\",\"listening_schedule\":\"0/4\",\"reading_schedule\":\"0/5\"},{\"id\":2,\"name\":\"package2\",\"start_time\":\"2014-01-14T18:35:46+08:00\",\"end_time\":\"2014-01-10T02:35:46+08:00\",\"question_packages_url\":\"1111\",\"listening_schedule\":\"0/6\",\"reading_schedule\":\"7/7\"}],\"follow_microposts_id\":[]}";
	// private String qsjson =
	// "{\"status\":true,\"notice\":\"\",\"package\":{\"listening\":[{\"id\":\"1\",\"branch_questions\":[{\"id\":\"2\",\"content\":\"This is an apple.\",\"resource_url\":\"/question_packages_1/resource2.mp3\"},{\"id\":\"3\",\"content\":\"Why is Google undertaking such a venture?\",\"resource_url\":\"/question_packages_1/resource3.mp3\"}]},{\"id\":\"2\",\"branch_questions\":[{\"id\":\"4\",\"content\":\"The company likes to present itself as having lofty aspirations.\",\"resource_url\":\"/question_packages_2/resource4.mp3\"},{\"id\":\"5\",\"content\":\"At its centre, however, is one simple issue: that of copyright.\",\"resource_url\":\"/question_packages_2/resource5.mp3\"}]}],\"reading\":[{\"id\":\"3\",\"branch_questions\":[{\"id\":\"2\",\"content\":\"This is an apple.\",\"resource_url\":\"/question_packages_1/resource2.mp3\"},{\"id\":\"3\",\"content\":\"Why is Google undertaking such a venture?\",\"resource_url\":\"/question_packages_1/resource3.mp3\"}]},{\"id\":\"4\",\"branch_questions\":[{\"id\":\"4\",\"content\":\"The company likes to present itself as having lofty aspirations.\",\"resource_url\":\"/question_packages_2/resource4.mp3\"},{\"id\":\"5\",\"content\":\"At its centre, however, is one simple issue: that of copyright.\",\"resource_url\":\"/question_packages_2/resource5.mp3\"}]}]},\"user_answers\":{\"listening\":[{\"id\":\"1\",\"branch_questions\":[{\"id\":\"2\",\"answer\":\"This is-->This is an -->This is an apple\"},{\"id\":\"3\",\"answer\":\"Why is Google-->Why is Google __ venture-->Why is Google undertaking such a venture?\"}]}],\"reading\":[{\"id\":\"1\",\"branch_questions\":[{\"id\":\"2\",\"answer\":\"/test.mp3-->/test.mp3\"}]}]}}";
	public boolean jiazai = false;
	public String school_class_id;
	public String student_id;
	private ListView working_date_list;
	private ListView working_content_list;
	private ProgressDialog prodialog;
	private ArrayList<WorkPojo> list;
	private MyWorkDateAdapter date_adapter;
	private MyQuestionAdapter question_adapter;
	private String message = "";
	private int p_q_package_id;
	private ArrayList<ListeningPojo> questionlist;
	private HomeWork homework;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Builder builder = new Builder(HomeWorkIngActivity.this);
			builder.setTitle("提示");
			switch (msg.what) {
			case 1:
				prodialog.dismiss();
				if (list.size() != 0) {
					working_date_list.setAdapter(date_adapter);
					working_content_list.setAdapter(question_adapter);
					p_q_package_id = list.get(homework.getWork_date_item())
							.getId();
					if (list.get(homework.getWork_date_item()).isType()) {
						homework.setWork_history(true);
					} else {
						homework.setWork_history(false);
					}
					prodialog = new ProgressDialog(HomeWorkIngActivity.this);
					prodialog.setMessage(HomeWorkParams.PD_QUESTION_INFO);
					prodialog.setCanceledOnTouchOutside(false);
					prodialog.show();
					Thread thread = new Thread(new getQuestion());
					thread.start();
				} else {
					builder.setMessage("暂无任何任务!");
					builder.setPositiveButton("确定", null);
					builder.show();
				}
				break;
			case 2:
				builder.setMessage(message);
				builder.setPositiveButton("确定", null);
				builder.show();
				break;
			case 3:
				prodialog.dismiss();
				break;
			case 4:
				prodialog.dismiss();
				builder.setMessage(message);
				builder.setPositiveButton("确定", null);
				builder.show();
				break;
			case 5:
				builder.setMessage("获取任务列表失败");
				builder.setPositiveButton("确定", null);
				builder.show();
				break;
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_working);
		homework = (HomeWork) getApplication();// 初始化
		initialize();// 初始化

		SharedPreferences sp = getSharedPreferences(SHARED, 0);
		student_id = sp.getString("id", "null");
		school_class_id = sp.getString("school_class_id", "null");
		Log.i(tag, school_class_id + "====");
		homework.setClass_id(Integer.valueOf(school_class_id));
		homework.setUser_id(Integer.valueOf(student_id));

		working_date_list
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (list.get(position).isType()) {
							homework.setWork_history(true);
						} else {
							homework.setWork_history(false);
						}
						Log.i("suanfa", homework.isWork_history() + "");
						homework.setWork_date_item(position);
						p_q_package_id = list.get(position).getId();
						homework.setP_q_package_id(p_q_package_id);
						working_date_list.setAdapter(date_adapter);
						working_content_list.setAdapter(question_adapter);
						prodialog = new ProgressDialog(HomeWorkIngActivity.this);
						prodialog.setMessage(HomeWorkParams.PD_QUESTION_INFO);
						prodialog.setCanceledOnTouchOutside(false);
						prodialog.show();
						Thread thread = new Thread(new getQuestion());
						thread.start();
					}
				});

		working_content_list
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent();
						switch (position) {
						case 0:
							if (ListeningQuestionList.listeningList.size() != 0) {
								HomeWorkMainActivity.instance.finish();
								intent.setClass(HomeWorkIngActivity.this,
										DictationPrepareActivity.class);
								startActivity(intent);
							} else {
								Toast.makeText(HomeWorkIngActivity.this,
										"本任务暂无作业..", Toast.LENGTH_SHORT).show();
							}
							break;
						case 1:
							if (questionlist.size() != 0) {
								HomeWorkMainActivity.instance.finish();
								intent.setClass(HomeWorkIngActivity.this,
										SpeakPrepareActivity.class);
								startActivity(intent);
							} else {
								Toast.makeText(HomeWorkIngActivity.this,
										"本任务暂无作业..", Toast.LENGTH_SHORT).show();
							}
							break;
						}

					}
				});
	}

	// 初始化
	public void initialize() {
		working_date_list = (ListView) findViewById(R.id.working_date_list);
		working_content_list = (ListView) findViewById(R.id.working_content_list);
		date_adapter = new MyWorkDateAdapter(HomeWorkIngActivity.this);
		question_adapter = new MyQuestionAdapter(HomeWorkIngActivity.this);
	}

	public class MyWorkDateAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private Context mContext = null;

		public MyWorkDateAdapter(Context context) {
			mContext = context;
			mInflater = LayoutInflater.from(mContext);
		}

		public int getCount() {
			return list.size();
		}

		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.work_date_adapter,
						null);
			}
			if (list.size() != 0) {

				TextView newwork_img = (TextView) convertView
						.findViewById(R.id.newwork_text);
				ImageView sign_img = (ImageView) convertView
						.findViewById(R.id.sign_img);
				TextView work_start_date = (TextView) convertView
						.findViewById(R.id.work_start_date);
				List<Integer> new_id_list = homework.getNew_id_list();
				if (new_id_list.size() == 0) {
					newwork_img.setVisibility(View.INVISIBLE);
				} else {
					boolean new_static = false;
					for (int i = 0; i < new_id_list.size(); i++) {
						if (list.get(position).getId() == new_id_list.get(i)) {
							new_static = true;
						}
					}
					if (new_static) {// 设置新作业标识
						Log.i("aaa", position + "///" + homework.getHw_number());
						newwork_img.setVisibility(View.VISIBLE);
					} else {
						newwork_img.setVisibility(View.INVISIBLE);
					}
				}
				if (position == homework.getWork_date_item()) {// 设置选中项图片
					sign_img.setBackgroundResource(R.drawable.jt);
					work_start_date.setTextColor(getResources().getColor(
							R.color.lvse));
				} else {
					sign_img.setBackgroundResource(R.drawable.jt2);
					work_start_date.setTextColor(getResources().getColor(
							R.color.shenhui));
				}
				work_start_date.setText(list.get(position).getStart_time());
			}
			return convertView;
		}
	}

	public class MyQuestionAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private Context mContext = null;

		public MyQuestionAdapter(Context context) {
			mContext = context;
			mInflater = LayoutInflater.from(mContext);
		}

		public int getCount() {
			return 2;
		}

		public Object getItem(int arg0) {
			return arg0;
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.work_question_adapter,
						null);
			}
			LinearLayout zill = (LinearLayout) convertView
					.findViewById(R.id.zill);
			ImageView work_question_img = (ImageView) convertView
					.findViewById(R.id.work_question_img);
			TextView work_question_case = (TextView) convertView
					.findViewById(R.id.work_question_case);
			TextView work_question_end = (TextView) convertView
					.findViewById(R.id.work_question_end);

			switch (position) {
			case 0:// 0表示听写
				String[] Listening_schedule = list
						.get(homework.getWork_date_item())
						.getListening_schedule().split("/");
				if (Listening_schedule[0].equals(Listening_schedule[1])) {// 表示已完成
					work_question_img.setBackgroundResource(R.drawable.tx2);
					work_question_end.setText("已完成");
					work_question_end.setTextColor(getResources().getColor(
							R.color.juhuang));
				} else {
					work_question_img.setBackgroundResource(R.drawable.tx);
					work_question_end.setText("截至时间："
							+ list.get(homework.getWork_date_item())
									.getEnd_time());
					work_question_end.setTextColor(Color.RED);
				}
				work_question_case.setText("完成：   "
						+ list.get(homework.getWork_date_item())
								.getListening_schedule());
				if (list.get(homework.getWork_date_item())
						.getListening_schedule().equals("0/0")) {
					zill.setVisibility(View.GONE);
				}
				break;
			case 1:// 1表示朗读
				String[] Reading_schedule = list
						.get(homework.getWork_date_item())
						.getReading_schedule().split("/");
				if (Reading_schedule[0].equals(Reading_schedule[1])) {// 表示已完成
					work_question_img.setBackgroundResource(R.drawable.ld);
					work_question_end.setText("已完成");
					work_question_end.setTextColor(getResources().getColor(
							R.color.juhuang));
				} else {
					work_question_img.setBackgroundResource(R.drawable.ld2);
					work_question_end.setText("截至时间："
							+ list.get(homework.getWork_date_item())
									.getEnd_time());
					work_question_end.setTextColor(Color.RED);
				}
				work_question_case.setText("完成：   "
						+ list.get(homework.getWork_date_item())
								.getReading_schedule());

				if (list.get(homework.getWork_date_item())
						.getReading_schedule().equals("0/0")) {
					zill.setVisibility(View.GONE);
				}
				break;
			}
			return convertView;
		}
	}

	// 获取任务列表
	class getClassInfo implements Runnable {
		public void run() {
			Looper.prepare();
			Map<String, String> map = new HashMap<String, String>();
			map.put("school_class_id", school_class_id + "");
			map.put("student_id", student_id + "");
			String json;
			try {
				Log.i("linshi", "1");
				json = HomeWorkTool.sendGETRequest(CLASS_INFO, map);
				JSONObject obj = new JSONObject(json);
				if (obj.getString("status").equals("success")) {
					analyzeJson(json);
				} else {
					handler.sendEmptyMessage(5);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Looper.loop();
		}
	}

	// 獲取題目
	class getQuestion implements Runnable {
		public void run() {
			Looper.prepare();
			questionlist = new ArrayList<ListeningPojo>();
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("student_id", student_id + "");
			maps.put("publish_question_package_id", p_q_package_id + "");
			String qsjson;
			try {
				qsjson = HomeWorkTool.sendGETRequest(INTO_DAILY_TASKS, maps);
				Log.i("linshi", qsjson);
				JSONObject obj = new JSONObject(qsjson);
				if (obj.getBoolean("status")) {
					QuestionJson(qsjson);
				} else {
					message = obj.getString("notice");
					handler.sendEmptyMessage(4);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Looper.loop();
		}
	}

	// 解析json
	public void analyzeJson(String json) {
		Calendar Window_day = Calendar.getInstance();
		Calendar wrok_day = Calendar.getInstance();
		try {
			list = new ArrayList<WorkPojo>();
			JSONArray ja = new JSONObject(json).getJSONArray("daily_tasks");
			if (ja.length() != 0) {
				for (int i = 0; i < ja.length(); i++) {
					boolean type;
					JSONObject item = ja.getJSONObject(i);
					String date = item.getString("end_time");
					date = HomeWorkTool.divisionTime(date);
					String day = date.split(" ")[0];
					String[] dayarr = day.split("-");
					String t = date.split(" ")[1];
					String[] timearr = t.split(":");
					wrok_day.set(Integer.valueOf(dayarr[0]),
							Integer.valueOf(dayarr[1]) - 1,
							Integer.valueOf(dayarr[2]),
							Integer.valueOf(timearr[0]),
							Integer.valueOf(timearr[1]),
							Integer.valueOf(timearr[2]));
					// a比c小,返回-1,
					// a与c相同,返回0
					// a比c大,返回1
					int zhi = Window_day.compareTo(wrok_day);
					if (zhi == -1) {
						type = false;
					} else {
						type = true;
					}
					Log.i("suanfa", "任务状态:" + type);
					String start_time = returnDATE(item.getString("start_time"));
					String end_day = returnDATE(item.getString("end_time"));
					String end_time = HomeWorkTool.divisionTime(item
							.getString("end_time"));
					end_time = end_time.split(" ")[1];
					list.add(new WorkPojo(item.getInt("id"), start_time,
							end_day + " " + end_time, item.getString("name"),
							item.getString("question_packages_url"), item
									.getString("listening_schedule"), item
									.getString("reading_schedule"), type));
				}
			}
			if (list.size() > 0) {
				p_q_package_id = list.get(0).getId();
				Log.i(tag, p_q_package_id + "");
				homework.setP_q_package_id(p_q_package_id);
			}
			handler.sendEmptyMessage(1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 解析题目json
	public void QuestionJson(String json) {
		try {
			ListeningQuestionList.Record_Count = 0;
			ListeningQuestionList.Small_Index = 0;
			ListeningQuestionList.listeningList.clear();
			ListeningQuestionList.answerList.clear();
			// 阅读
			JSONArray ja = new JSONObject(json).getJSONObject("package")
					.getJSONArray("reading");
			if (ja.length() != 0) {
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jn = ja.getJSONObject(i);
					JSONArray jArr = jn.getJSONArray("branch_questions");
					int id = jn.getInt("id");
					List<QuestionPojo> question = new ArrayList<QuestionPojo>();
					for (int j = 0; j < jArr.length(); j++) {
						JSONObject jb = jArr.getJSONObject(j);
						String str = jb.getString("resource_url").replaceAll(
								" ", "");
						question.add(new QuestionPojo(jb.getInt("id"), jb
								.getString("content"), str));
					}
					questionlist.add(new ListeningPojo(id, question));
				}
			}
			Log.i("suanfa", questionlist.size() + "=");
			homework.setQuestion_list(questionlist);
			homework.getQuestion_allNumber();
			homework.setQuestion_allNumber(questionlist.size());
			// 解析听写题目
			JSONArray jarray = new JSONObject(json).getJSONObject("package")
					.getJSONArray("listening");
			if (jarray.length() != 0) {
				for (int i = 0; i < jarray.length(); i++) {
					JSONObject jn = jarray.getJSONObject(i);
					JSONArray jArr = jn.getJSONArray("branch_questions");
					int id = jn.getInt("id");
					List<QuestionPojo> question = new ArrayList<QuestionPojo>();
					for (int j = 0; j < jArr.length(); j++) {
						JSONObject jb = jArr.getJSONObject(j);
						question.add(new QuestionPojo(jb.getInt("id"), jb
								.getString("content"), jb
								.getString("resource_url")));
					}
					ListeningQuestionList.addListeningPojo(new ListeningPojo(
							id, question));
				}
			}
			// 加載历史信息
			List<List<String>> questionhistory = new ArrayList<List<String>>();
			if (!new JSONObject(json).getString("user_answers").equals("")) {
				// 解析阅读记录
				JSONArray user_answers = new JSONObject(json).getJSONObject(
						"user_answers").getJSONArray("reading");
				Log.i("linshi", "user_answers.length" + user_answers.length());
				for (int i = 0; i < user_answers.length(); i++) {
					JSONObject jn = user_answers.getJSONObject(i);
					JSONArray jArr = jn.getJSONArray("branch_questions");
					List<String> question = new ArrayList<String>();
					for (int j = 0; j < jArr.length(); j++) {
						JSONObject jb = jArr.getJSONObject(j);
						Log.i("linshi", jb.getString("answer"));
						question.add(jb.getString("answer"));
					}
					questionhistory.add(question);
				}
				// 解析听写记录
				JSONArray answer = new JSONObject(json).getJSONObject(
						"user_answers").getJSONArray("listening");
				if (answer.length() != 0) {
					for (int i = 0; i < answer.length(); i++) {
						JSONObject jn = answer.getJSONObject(i);
						JSONArray jArr = jn.getJSONArray("branch_questions");
						List<String> smallList = new ArrayList<String>();
						for (int j = 0; j < jArr.length(); j++) {
							JSONObject jb = jArr.getJSONObject(j);
							smallList.add(jb.getString("answer"));
							ListeningQuestionList.Small_Index = j + 1;
						}
						if (ListeningQuestionList.getListeningPojo(i)
								.getQuesttionList().size() == jArr.length()) {
							ListeningQuestionList.Small_Index = 0;
							ListeningQuestionList.Record_Count = i + 1;
						} else {
							ListeningQuestionList.Record_Count = i;
						}
						ListeningQuestionList.addAnswer(smallList);
					}
				}
			}
			homework.setQuestion_history(questionhistory);
			homework.setHistory_item(questionhistory.size());
			handler.sendEmptyMessage(3);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void onResume() {
		if (HomeWorkTool.isConnect(HomeWorkIngActivity.this)) {
			prodialog = new ProgressDialog(HomeWorkIngActivity.this);
			prodialog.setMessage(HomeWorkParams.PD_CLASS_INFO);
			prodialog.setCanceledOnTouchOutside(false);
			prodialog.show();
			Thread thread = new Thread(new getClassInfo());
			thread.start();
		}
		super.onResume();
	}

	public String returnDATE(String str) {
		String time = str.split("T")[0];
		String[] times = time.split("-");
		String DATH = times[0] + "年" + times[1] + "月" + times[2] + "日";
		return DATH;
	}
}