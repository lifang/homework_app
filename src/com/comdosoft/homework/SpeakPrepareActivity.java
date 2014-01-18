package com.comdosoft.homework;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.comdosoft.homework.pojo.ListeningPojo;
import com.comdosoft.homework.pojo.QuestionPojo;
import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.Urlinterface;

public class SpeakPrepareActivity extends Activity implements Urlinterface {
	private String content = "";
	private TextView question_speak_title;
	private TextView question_speak_content;
	private MediaPlayer player;
	private String message;
	private ProgressDialog prodialog;
	private List<ListeningPojo> list;
	private HomeWork homework;
	private List<QuestionPojo> questionlist;
	private List<List<String>> history;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			prodialog.dismiss();
			Builder builder = new Builder(SpeakPrepareActivity.this);
			builder.setTitle("提示");
			switch (msg.what) {
			case 1:
				// questionlist =
				// list.get(homework.getQuestion_index()).getQuesttionList();
				// for (int i = 0; i < questionlist.size(); i++) {
				// content += questionlist.get(i).getContent() + "\n";
				// }
				// question_speak_content.setText(content);
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

		list = homework.getQuestion_list();
		history = homework.getQuestion_history();

		if (homework.isWork_history()) {// 表示查看历史记录
			questionlist = list.get(homework.getQuestion_index())
					.getQuesttionList();
			homework.setQ_package_id(list.get(homework.getQuestion_index()).getId());
			for (int i = 0; i < questionlist.size(); i++) {
				content += questionlist.get(i).getContent() + "\n";
			}
		} else {
			questionlist = list.get(history.size()).getQuesttionList();
			homework.setQ_package_id(list.get(history.size()).getId());
			for (int i = 0; i < questionlist.size(); i++) {
				content += questionlist.get(i).getContent() + "\n";
			}
		}
		question_speak_content.setText(content);
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
			SpeakPrepareActivity.this.finish();
			if (homework.isWork_history()) {// 进入答题历史页面
				homework.setBranch_questions(list.get(
						homework.getQuestion_index()).getQuesttionList());
				intent.setClass(SpeakPrepareActivity.this,
						SpeakHistoryActivity.class);
				startActivity(intent);
			} else {
				homework.setBranch_questions(list.get(history.size())
						.getQuesttionList());
				homework.setBranch_question_id(list.get(history.size()).getId());
				intent.setClass(SpeakPrepareActivity.this,
						SpeakBeginActivity.class);
				startActivity(intent);
			}
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
						HomeWorkMainActivity.class);
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent intent = new Intent();
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SpeakPrepareActivity.this.finish();
			intent.setClass(SpeakPrepareActivity.this,
					HomeWorkMainActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
