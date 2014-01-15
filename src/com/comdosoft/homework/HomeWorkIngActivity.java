package com.comdosoft.homework;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.comdosoft.homework.pojo.WorkDatePojo;
import com.comdosoft.homework.pojo.QuestionCasePojo;
import com.comdosoft.homework.tools.Urlinterface;

public class HomeWorkIngActivity extends Activity implements Urlinterface {

	private ListView working_date_list;
	private List<WorkDatePojo> work_list;
	private List<QuestionCasePojo> question_list;
	private int index;
	private ListView working_content_list;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_working);

		initialize();// 初始化
		index = 0;

		work_list = new ArrayList<WorkDatePojo>();
		question_list = new ArrayList<QuestionCasePojo>();
		for (int i = 0; i < 5; i++) {
			WorkDatePojo work = new WorkDatePojo(1, "2014年1月3日", 1);
			work_list.add(work);
		}

		QuestionCasePojo question = new QuestionCasePojo(1, 5, 1, 600,
				"2014/1/4日", 0);
		question_list.add(question);
		QuestionCasePojo question2 = new QuestionCasePojo(1, 5, 5, 500,
				"2013/1/4日", 1);
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
							intent.setClass(HomeWorkIngActivity.this, SpeakPrepareActivity.class);
							break;
						case 1:
							intent.setClass(HomeWorkIngActivity.this, DictationPrepareActivity.class);
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
						R.color.work_date_textcolor));
			} else {
				sign_img.setBackgroundResource(R.drawable.jt2);
				work_start_date.setTextColor(getResources().getColor(
						R.color.work_date_untextcolor));
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
			TextView work_question_time = (TextView) convertView
					.findViewById(R.id.work_question_time);
			TextView work_question_end = (TextView) convertView
					.findViewById(R.id.work_question_end);

			String time = "";
			if (question_list.get(position).getSecond() > 0) {
				int minute = question_list.get(position).getSecond() / 60;
				int second = question_list.get(position).getSecond() % 60;
				time = minute + ":" + second;
			} else {
				time = "00:00";
			}
			switch (question_list.get(position).getType()) {
			case 0:// 0表示朗读题
				if (question_list.get(position).getCount_over() == question_list
						.get(position).getCount_all()) {// 表示已完成
					work_question_img.setBackgroundResource(R.drawable.ld);
					work_question_end.setText("已完成");
					work_question_end.setTextColor(getResources().getColor(
							R.color.work_content_over_textcolor));
					work_question_time.setText("用时：" + time);
				} else {
					work_question_img.setBackgroundResource(R.drawable.ld2);
					work_question_end.setText("截至时间："
							+ question_list.get(position).getEnd_time());
					work_question_end.setTextColor(Color.RED);
				}
				break;
			case 1:// 1表示听写题
				if (question_list.get(position).getCount_over() == question_list
						.get(position).getCount_all()) {// 表示已完成
					work_question_img.setBackgroundResource(R.drawable.tx2);
					work_question_end.setText("已完成");
					work_question_end.setTextColor(getResources().getColor(
							R.color.work_content_over_textcolor));
					work_question_time.setText("用时：" + time);
				} else {
					work_question_img.setBackgroundResource(R.drawable.tx);
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
}
