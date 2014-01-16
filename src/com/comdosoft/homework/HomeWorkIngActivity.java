package com.comdosoft.homework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.comdosoft.homework.pojo.QuestionCasePojo;
import com.comdosoft.homework.pojo.WorkDatePojo;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;

public class HomeWorkIngActivity extends Activity implements Urlinterface {
	private String json = "{\"status\":\"success\",\"notice\":\"\u767b\u9646\u6210\u529f\uff01\",\"student\":{\"id\":1,\"name\":\"\u5f20\u4e09\",\"nickname\":\"\u4e0a\u5584\u82e5\u6c34\",\"avatar_url\":\"/homework_manage/public/homework_system/avatars/students/student_1.png\"},\"class\":{\"id\":1,\"name\":\"\u73ed\u7ea71\",\"tearcher_name\":\"2222\",\"tearcher_id\":1},\"classmates\":[{\"avatar_url\":\"/homework_manage/public/homework_system/avatars/students/student_1.png\",\"id\":1,\"name\":\"\u5f20\u4e09\",\"nickname\":\"\u4e0a\u5584\u82e5\u6c34\"},{\"avatar_url\":\"homework_system/avatars/students/student_2.png\",\"id\":2,\"name\":\"\u5f20\u4e09\",\"nickname\":\"\u4e0a\u5584\u82e5\u6c341\"},{\"avatar_url\":\"/ulrrr/kkkk\",\"id\":3,\"name\":\"\u5f20\u4e09\",\"nickname\":\"\u4e0a\u5584\u82e5\u6c342\"},{\"avatar_url\":\"/ulrrr/kkkk\",\"id\":4,\"name\":\"\u5f20\u4e09\",\"nickname\":\"\u4e0a\u5584\u82e5\u6c343\"}],\"task_messages\":[],\"microposts\":{\"page\":1,\"pages_count\":2,\"details_microposts\":[{\"avatar_url\":\"121111\",\"content\":\"1\",\"created_at\":\"2014-01-09T10:54:21+08:00\",\"id\":1,\"name\":\"2222\",\"nickname\":null,\"user_id\":1,\"user_types\":0},{\"avatar_url\":\"121111\",\"content\":\"9\",\"created_at\":\"2014-01-09T10:54:21+08:00\",\"id\":9,\"name\":\"2222\",\"nickname\":null,\"user_id\":1,\"user_types\":0},{\"avatar_url\":\"121111\",\"content\":\"10\",\"created_at\":\"2014-01-09T10:54:21+08:00\",\"id\":10,\"name\":\"2222\",\"nickname\":null,\"user_id\":1,\"user_types\":0},{\"avatar_url\":\"avatar_teacher1\",\"content\":\"2\",\"created_at\":\"2014-01-09T10:54:26+08:00\",\"id\":2,\"name\":\"teacher1\",\"nickname\":null,\"user_id\":2,\"user_types\":0},{\"avatar_url\":\"avatar_teacher2\",\"content\":\"3\",\"created_at\":\"2014-01-09T10:54:29+08:00\",\"id\":3,\"name\":\"teacher2\",\"nickname\":null,\"user_id\":3,\"user_types\":0},{\"avatar_url\":\"avatar_teacher3\",\"content\":\"4\",\"created_at\":\"2014-01-09T10:54:30+08:00\",\"id\":4,\"name\":\"teacher3\",\"nickname\":null,\"user_id\":4,\"user_types\":0},{\"avatar_url\":\"avatar_teacher3\",\"content\":\"5\",\"created_at\":\"2014-01-09T10:54:31+08:00\",\"id\":5,\"name\":\"teacher3\",\"nickname\":null,\"user_id\":4,\"user_types\":0},{\"avatar_url\":\"avatar_teacher6\",\"content\":\"6\",\"created_at\":\"2014-01-09T10:54:31+08:00\",\"id\":6,\"name\":\"teacher6\",\"nickname\":null,\"user_id\":7,\"user_types\":0},{\"avatar_url\":\"avatar_teacher5\",\"content\":\"7\",\"created_at\":\"2014-01-09T10:54:32+08:00\",\"id\":7,\"name\":\"teacher5\",\"nickname\":null,\"user_id\":6,\"user_types\":0},{\"avatar_url\":\"121111\",\"content\":\"8\",\"created_at\":\"2014-01-09T10:54:33+08:00\",\"id\":8,\"name\":\"2222\",\"nickname\":null,\"user_id\":1,\"user_types\":0}]},\"daily_tasks\":{\"dealing_tasks\":[],\"unfinish_tasks\":[{\"end_time\":\"2014-01-10T02:35:46+08:00\",\"id\":1,\"name\":\"package1\",\"question_packages_url\":\"31312312313123\",\"status\":0},{\"end_time\":\"2014-01-10T02:35:46+08:00\",\"id\":2,\"name\":\"package2\",\"question_packages_url\":\"1111\",\"status\":0}],\"finish_tasks\":[]}}";

	public int school_class_id = 1;
	public int student_id = 1;
	private ListView working_date_list;
	private List<WorkDatePojo> work_list;
	private List<QuestionCasePojo> question_list;
	private int index;
	private ListView working_content_list;
	private ProgressDialog prodialog;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			prodialog.dismiss();
			Builder builder = new Builder(HomeWorkIngActivity.this);
			builder.setTitle("提示");
			switch (msg.what) {
			case 1:

				break;
			case 2:
				builder.setMessage("11");
				builder.setPositiveButton("确定", null);
				builder.show();
				break;
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_working);

		initialize();// 初始化
		index = 0;

		// prodialog = new ProgressDialog(HomeWorkIngActivity.this);
		// prodialog.setMessage(HomeWorkParams.PD_CLASS_INFO);
		// prodialog.show();
		// Thread thread = new Thread(new getClassInfo());
		// thread.start();

		work_list = new ArrayList<WorkDatePojo>();
		question_list = new ArrayList<QuestionCasePojo>();
		for (int i = 0; i < 5; i++) {
			WorkDatePojo work = new WorkDatePojo(1, "2014年1月3日", 1);
			work_list.add(work);
		}

		QuestionCasePojo question = new QuestionCasePojo(1, 5, 1, "2014/1/4日",
				"2014/1/3日", 0);
		question_list.add(question);
		QuestionCasePojo question2 = new QuestionCasePojo(1, 5, 5, "2013/1/4日",
				"2014/1/3日", 1);
		question_list.add(question2);

		final MyWorkDateAdapter date_adapter = new MyWorkDateAdapter(
				HomeWorkIngActivity.this);
		final MyQuestionAdapter question_adapter = new MyQuestionAdapter(
				HomeWorkIngActivity.this);
		working_date_list.setAdapter(date_adapter);
		working_content_list.setAdapter(question_adapter);

		working_date_list
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						index = position;
						working_date_list.setAdapter(date_adapter);
					}
				});
		working_content_list
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						HomeWorkIngActivity.this.finish();
						Intent intent = new Intent();
						switch (question_list.get(position).getType()) {
						case 0:
							intent.setClass(HomeWorkIngActivity.this,
									SpeakPrepareActivity.class);
							break;
						case 1:
							intent.setClass(HomeWorkIngActivity.this,
									DictationPrepareActivity.class);
							break;
						}
						startActivity(intent);
					}
				});
	}

	// 初始化
	public void initialize() {
		working_date_list = (ListView) findViewById(R.id.working_date_list);
		working_content_list = (ListView) findViewById(R.id.working_content_list);
	}

	public class MyWorkDateAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private Context mContext = null;

		public MyWorkDateAdapter(Context context) {
			mContext = context;
			mInflater = LayoutInflater.from(mContext);
		}

		public int getCount() {
			return work_list.size();
		}

		public Object getItem(int arg0) {
			return work_list.get(arg0);
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
			TextView newwork_img = (TextView) convertView
					.findViewById(R.id.newwork_text);
			ImageView sign_img = (ImageView) convertView
					.findViewById(R.id.sign_img);
			TextView work_start_date = (TextView) convertView
					.findViewById(R.id.work_start_date);

			if (position == 0) {// 设置新作业标识
				newwork_img.setVisibility(View.VISIBLE);
			} else {
				newwork_img.setVisibility(View.INVISIBLE);
			}
			if (position == index) {// 设置选中项图片
				sign_img.setBackgroundResource(R.drawable.jt);
				work_start_date.setTextColor(getResources().getColor(
						R.color.lvse));
			} else {
				sign_img.setBackgroundResource(R.drawable.jt2);
				work_start_date.setTextColor(getResources().getColor(
						R.color.shenhui));
			}
			work_start_date.setText(work_list.get(position).getTime());
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
			return question_list.size();
		}

		public Object getItem(int arg0) {
			return question_list.get(arg0);
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
			ImageView work_question_img = (ImageView) convertView
					.findViewById(R.id.work_question_img);
			TextView work_question_case = (TextView) convertView
					.findViewById(R.id.work_question_case);
			TextView work_question_end = (TextView) convertView
					.findViewById(R.id.work_question_end);

			switch (question_list.get(position).getType()) {
			case 0:// 0表示听写
				if (question_list.get(position).getCount_over() == question_list
						.get(position).getCount_all()) {// 表示已完成
					work_question_img.setBackgroundResource(R.drawable.tx2);
					work_question_end.setText("已完成");
					work_question_end.setTextColor(getResources().getColor(
							R.color.juhuang));
				} else {
					work_question_img.setBackgroundResource(R.drawable.tx);
					work_question_end.setText("截至时间："
							+ question_list.get(position).getEnd_time());
					work_question_end.setTextColor(Color.RED);
				}

				break;
			case 1:// 1表示朗读
				if (question_list.get(position).getCount_over() == question_list
						.get(position).getCount_all()) {// 表示已完成
					work_question_img.setBackgroundResource(R.drawable.ld);
					work_question_end.setText("已完成");
					work_question_end.setTextColor(getResources().getColor(
							R.color.juhuang));
				} else {
					work_question_img.setBackgroundResource(R.drawable.ld2);
					work_question_end.setText("截至时间："
							+ question_list.get(position).getEnd_time());
					work_question_end.setTextColor(Color.RED);
				}
				break;
			}

			work_question_case.setText("完成："
					+ question_list.get(position).getCount_over() + "/"
					+ question_list.get(position).getCount_all());
			return convertView;
		}
	}

	class getClassInfo implements Runnable {
		public void run() {
			Looper.prepare();
			Map<String, String> map = new HashMap<String, String>();
			map.put("school_class_id", school_class_id + "");
			map.put("student_id", student_id + "");
			String json;
			try {
				json = HomeWorkTool.sendGETRequest(CLASS_INFO, map);
				JSONObject obj = new JSONObject(json);
				if (obj.getString("status").equals("success")) {
					JSONObject daily_tasks = obj.getJSONObject("daily_tasks");
					JSONArray dealing_tasks = daily_tasks
							.getJSONArray("dealing_tasks");// 正在进行的任务
					JSONArray unfinish_tasks = daily_tasks
							.getJSONArray("unfinish_tasks");// 新的任务
					JSONArray finish_tasks = daily_tasks
							.getJSONArray("finish_tasks");// 已经完成的任务
					if (dealing_tasks.length() > 0) {
					}
					if (unfinish_tasks.length() > 0) {
						for (int i = 0; i < unfinish_tasks.length(); i++) {
							JSONObject item = unfinish_tasks.getJSONObject(i);
						}
					}
					if (finish_tasks.length() > 0) {
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Looper.loop();
		}
	}
}
