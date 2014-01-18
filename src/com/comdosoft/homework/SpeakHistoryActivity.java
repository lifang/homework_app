package com.comdosoft.homework;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.homework.pojo.QuestionPojo;
import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.Urlinterface;

public class SpeakHistoryActivity extends Activity implements Urlinterface {

	public String content;// 记录本题正确答案
	private TextView question_speak_title;
	private MediaPlayer player;
	public int number;// 播放次数
	private LinearLayout question_speak_tishi;
	private TextView question_speak_content;
	private List<List<String>> question_history;
	private int student_id = 1;
	private int ti_id = 1;
	private HomeWork homework;
	private List<QuestionPojo> branch_questions;
	private int index = 0;
	private int question_history_size;
	
	private static String SDFile;
	public String error_str = "";// 记录错误的词
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				index += 1;
				question_speak_title.setText((index + 1) + "/"
						+ branch_questions.size());
				content = branch_questions.get(index).getContent();
				question_speak_content.setText(content);
				setTishi(question_history.get(homework.getQuestion_index()).get(index));
				break;
			case 1:
				break;
			}
			super.handleMessage(msg);
		}
	};
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_speak_history);
		homework = (HomeWork) getApplication();

		initialize();
		SDFile = "/sdcard/homework/" + student_id + "/" + ti_id + "/";
		File file = new File(SDFile);
		if (!file.exists()) {
			file.mkdirs();
		}

		Toast.makeText(this, "查看历史信息", Toast.LENGTH_SHORT).show();
		// for (int j = 0; j <
		// homework.getQuestion_history().get(homework.getQuestion_index()).get.length;
		// j++) {
		//
		// }
		// initView(i);
	}

	// 初始化
	public void initialize() {
		branch_questions = homework.getBranch_questions();
		content = branch_questions.get(0).getContent();
		question_speak_title = (TextView) findViewById(R.id.question_speak_title);
		question_speak_title.setText((index + 1) + "/"
				+ branch_questions.size());
		question_speak_content = (TextView) findViewById(R.id.question_speak_content);
		question_speak_content.setText(branch_questions.get(0).getContent());
		question_speak_tishi = (LinearLayout) findViewById(R.id.question_speak_tishi);
		player = new MediaPlayer();
		question_history = homework.getQuestion_history();
		// 添加错词提示
		question_history_size = homework.getQuestion_history().size();
		setTishi(question_history.get(homework.getQuestion_index()).get(0));
	}

	public void onclicks(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.question_speak_exit:
			MyDialog("确认要退出吗?", "确认", "取消", 0);
			break;
		case R.id.question_speak_next:
			int ye = homework.getQuestion_index();
			if ((ye + 1) < homework.getQuestion_allNumber()) {
				Log.i(tag, ye + "-" + homework.getQuestion_allNumber() + "-"
						+ index + "-" + branch_questions.size());
				if ((index + 1) < branch_questions.size()) {
					handler.sendEmptyMessage(0);
				} else {
					Log.i(tag, error_str);
					homework.setQuestion_index(homework.getQuestion_index() + 1);
					SpeakHistoryActivity.this.finish();
					intent.setClass(SpeakHistoryActivity.this,
							SpeakPrepareActivity.class);
					startActivity(intent);
				}
			} else {
				if ((index + 1) < branch_questions.size()) {
					handler.sendEmptyMessage(0);
				} else {
					MyDialog("这已经是最后一题了!", "确认", "取消", 2);
				}
			}
			break;
		case R.id.question_speak_img:// 播放音频
			// 从文件系统播放
			String path = SDFile + "test.mp3";
			try {
				player.setDataSource(path);
				if (player.isPlaying()) {// 正在播放
					Toast.makeText(this, "正在播放..", Toast.LENGTH_SHORT).show();
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

	public void setTishi(String str) {
		Log.i(tag, str);
		if (str.contains("-->")) {
			String[] str_arr = str.split("-->");
			for (int i = 0; i < str_arr.length; i++) {
				initView(str_arr[i], i);
			}
		} else {
			initView(str, 0);
		}
	}

	public void initView(String str, int i) {
		TextView tv = new TextView(getApplicationContext());
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, 100);
		tv.setLayoutParams(lp);
		tv.setText("  " + str);
		tv.setTextColor(Color.rgb(157, 156, 156));
		tv.setTextSize(24);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		if (i % 2 == 0) {
			tv.setBackgroundColor(Color.rgb(231, 231, 231));
		}
		question_speak_tishi.addView(tv);
	}

	// 自定义dialog设置
	private void MyDialog(String title, String btn_one, String Btn_two,
			final int type) {
		final Intent intent = new Intent();
		final Dialog dialog = new Dialog(this, R.style.Transparent);
		dialog.setContentView(R.layout.my_dialog);
		dialog.setCancelable(true);

		ImageView dialog_img = (ImageView) dialog.findViewById(R.id.dialog_img);

		TextView title_tv = (TextView) dialog.findViewById(R.id.dialog_title);
		title_tv.setText(title);
		Button dialog_ok = (Button) dialog.findViewById(R.id.dialog_ok);
		dialog_ok.setText(btn_one);
		Button dialog_no = (Button) dialog.findViewById(R.id.dialog_no);
		dialog_no.setText(Btn_two);
		dialog_ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				switch (type) {
				case 0:
					dialog.dismiss();
					SpeakHistoryActivity.this.finish();
					intent.setClass(SpeakHistoryActivity.this,
							HomeWorkIngActivity.class);
					startActivity(intent);
					break;
				case 1:
					homework.setQuestion_index(homework.getQuestion_index() + 1);
					dialog.dismiss();
					SpeakHistoryActivity.this.finish();
					intent.setClass(SpeakHistoryActivity.this,
							SpeakPrepareActivity.class);
					startActivity(intent);
					break;
				case 2:
					dialog.dismiss();
					homework.setQuestion_index(0);
					SpeakHistoryActivity.this.finish();
					intent.setClass(SpeakHistoryActivity.this,
							HomeWorkIngActivity.class);
					startActivity(intent);
					break;
				}
			}
		});
		dialog_no.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				switch (type) {
				case 0:
					dialog.dismiss();
					break;
				case 1:
					dialog.dismiss();
					SpeakHistoryActivity.this.finish();
					intent.setClass(SpeakHistoryActivity.this,
							HomeWorkIngActivity.class);
					startActivity(intent);
					break;
				}
			}
		});
		if (type == 2) {
			dialog_no.setVisibility(View.GONE);
			dialog_ok.setBackgroundColor(getResources().getColor(R.color.lvse));
		} else {
			dialog_img.setVisibility(View.GONE);
		}
		dialog.show();
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