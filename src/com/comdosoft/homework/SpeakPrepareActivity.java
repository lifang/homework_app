package com.comdosoft.homework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.comdosoft.homework.pojo.ListeningPojo;
import com.comdosoft.homework.pojo.QuestionPojo;
import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.HomeWorkParams;
import com.comdosoft.homework.tools.Urlinterface;

public class SpeakPrepareActivity extends Activity implements Urlinterface {
	private String json = " {\"status\":true,\"notice\":\"\",\"package\":{\"listening\":[{\"id\":\"1\",\"branch_questions\":[{\"id\":\"2\",\"content\":\"This is an apple.\",\"resource_url\":\"/question_packages_1/resource2.mp3\"},{\"id\":\"3\",\"content\":\"Why is Google undertaking such a venture?\",\"resource_url\":\"/question_packages_1/resource3.mp3\"}]},{\"id\":\"2\",\"branch_questions\":[{\"id\":\"4\",\"content\":\"The company likes to present itself as having lofty aspirations.\",\"resource_url\":\"/question_packages_2/resource4.mp3\"},{\"id\":\"5\",\"content\":\"At its centre, however, is one simple issue: that of copyright.\",\"resource_url\":\"/question_packages_2/resource5.mp3\"}]}],\"reading\":[{\"id\":\"3\",\"branch_questions\":[{\"id\":\"2\",\"content\":\"This is an apple.\",\"resource_url\":\"/question_packages_1/resource2.mp3\"},{\"id\":\"3\",\"content\":\"Why is Google undertaking such a venture?\",\"resource_url\":\"/question_packages_1/resource3.mp3\"}]},{\"id\":\"4\",\"branch_questions\":[{\"id\":\"4\",\"content\":\"The company likes to present itself as having lofty aspirations.\",\"resource_url\":\"/question_packages_2/resource4.mp3\"},{\"id\":\"5\",\"content\":\"At its centre, however, is one simple issue: that of copyright.\",\"resource_url\":\"/question_packages_2/resource5.mp3\"}]}]},\"user_answers\":{\"listening\":[{\"id\":\"1\",\"branch_questions\":[{\"id\":\"2\",\"answer\":\"This is;||;This is an ;||;This is an apple\"},{\"id\":\"3\",\"answer\":\"Why is Google;||;Why is Google __ venture;||;Why is Google undertaking such a venture?\"}]}],\"reading\":[{\"id\":\"1\",\"branch_questions\":[{\"id\":\"2\",\"answer\":\"/test.mp3;||;/test.mp3\"}]}]}}";
	private String content = "";
	private TextView question_speak_title;
	private TextView question_speak_content;
	private MediaPlayer player;
	private int student_id = 1;
	private String message;
	private ProgressDialog prodialog;
	private List<ListeningPojo> list;
	private HomeWork homework;
	private List<QuestionPojo> questionlist;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			prodialog.dismiss();
			Builder builder = new Builder(SpeakPrepareActivity.this);
			builder.setTitle("提示");
			switch (msg.what) {
			case 1:
				Log.i(tag, homework.getQuestion_index()+"");
				questionlist = list.get(homework.getQuestion_index()).getQuesttionList();
				for (int i = 0; i < questionlist.size(); i++) {
					content += questionlist.get(i).getContent() + "\n";
				}
				question_speak_content.setText(content);
				break;
			case 2:
				builder.setMessage(message);
				builder.setPositiveButton("确定", null);
				builder.show();
				break;
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_speak_prepare);
		homework = (HomeWork) getApplication();
		initialize();
		question_speak_title.setText("朗读题");

		prodialog = new ProgressDialog(SpeakPrepareActivity.this);
		prodialog.setMessage(HomeWorkParams.PD_CLASS_INFO);
		prodialog.show();
		Thread thread = new Thread(new getQuestion());
		thread.start();
	}

	// 初始化
	public void initialize() {
		question_speak_title = (TextView) findViewById(R.id.question_speak_title);
		question_speak_content = (TextView) findViewById(R.id.question_speak_content);
		player = new MediaPlayer();
	}

	public void onclicks(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.question_speak_exit:
			MyDialog("确认不在继续答题吗？", "确定", "取消");
			break;
		case R.id.question_speak_next:
			homework.setBranch_questions(list.get(homework.getQuestion_index()).getQuesttionList());
			homework.setBranch_question_id(list.get(homework.getQuestion_index()).getId());
			SpeakPrepareActivity.this.finish();
			intent.setClass(SpeakPrepareActivity.this, SpeakBeginActivity.class);
			startActivity(intent);
			break;
		case R.id.question_speak_img:
			// 从文件系统播放
			String path = "/sdcard/homework/test.mp3";
			try {
				player.setDataSource(path);
				if (player.isPlaying()) {// 正在播放
					player.pause();
				} else {
					player.prepare();
					player.start();
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	private void MyDialog(String title, String btn_one, String Btn_two) {
		final Intent intent = new Intent();
		final Dialog dialog = new Dialog(this, R.style.Transparent);
		dialog.setContentView(R.layout.my_dialog);
		dialog.setCancelable(true);
		TextView title_tv = (TextView) dialog.findViewById(R.id.dialog_title);
		title_tv.setText(title);
		Button dialog_ok = (Button) dialog.findViewById(R.id.dialog_ok);
		dialog_ok.setText(btn_one);
		Button dialog_no = (Button) dialog.findViewById(R.id.dialog_no);
		dialog_no.setText(Btn_two);
		dialog_ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				SpeakPrepareActivity.this.finish();
				intent.setClass(SpeakPrepareActivity.this,
						HomeWorkIngActivity.class);
				startActivity(intent);
			}
		});
		dialog_no.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	class getQuestion implements Runnable {
		public void run() {
			Looper.prepare();
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("student_id", student_id + "");
			maps.put("p_q_package_id", 1 + "");
			// String json;
			try {
				// json = HomeWorkTool.sendGETRequest(INTO_DAILY_TASKS, maps);
				JSONObject obj = new JSONObject(json);
				if (obj.getBoolean("status")) {
					analyzeJson(json);
				} else {
					message = obj.getString("notice");
					handler.sendEmptyMessage(2);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Looper.loop();
		}
	}

	// 解析json
	public void analyzeJson(String json) {
		try {
			list = new ArrayList<ListeningPojo>();
			JSONArray ja = new JSONObject(json).getJSONObject("package")
					.getJSONArray("reading");
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jn = ja.getJSONObject(i);
				JSONArray jArr = jn.getJSONArray("branch_questions");
				int id = jn.getInt("id");
				List<QuestionPojo> question = new ArrayList<QuestionPojo>();
				for (int j = 0; j < jArr.length(); j++) {
					JSONObject jb = jArr.getJSONObject(j);
					question.add(new QuestionPojo(jb.getInt("id"), jb
							.getString("content"), jb.getString("resource_url")));
				}
				list.add(new ListeningPojo(id, question));
			}
			homework.setQuestion_allNumber(list.size());
			handler.sendEmptyMessage(1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void onStart() {
		if (player == null)
			player = new MediaPlayer();
		super.onStart();
	}

	// 暂停音频
	protected void onStop() {
		if (player.isPlaying()) {// 正在播放
			player.pause();// 暂停
		}
		super.onStop();
	}

	// 销毁音频
	protected void onDestroy() {
		if (player != null) {
			player.stop();
			player.release();
			player = null;
		}
		super.onDestroy();
	}
}
