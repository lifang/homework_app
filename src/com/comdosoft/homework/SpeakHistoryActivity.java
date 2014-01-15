package com.comdosoft.homework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.PredicateLayout;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SpeakHistoryActivity extends Activity {
	public String content = "This is an apple.";// 正确答案
	private TextView question_speak_title;
	private TextView question_speak_content;
	private TextView question_speak_tishi;
	private MediaPlayer player;
	private ArrayList<String> error_list;
	private String SDFile;
	private String uid;
	private String ti_id;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_speak_history);

		initialize();

	}

	// 初始化
	public void initialize() {
		SDFile = "/sdcard/homework/" + uid + "/" + ti_id + "/";
		question_speak_title = (TextView) findViewById(R.id.question_speak_title);
		question_speak_title.setText("1/2");
		question_speak_content = (TextView) findViewById(R.id.question_speak_content);
		question_speak_content.setText(content);
		question_speak_tishi = (TextView) findViewById(R.id.question_speak_tishi);
		player = new MediaPlayer();
		error_list = new ArrayList<String>();
	}

	public void onclicks(View v) {
		switch (v.getId()) {
		case R.id.question_speak_exit:
			MyDialog("你确认要退出吗?", "确认", "取消", 0);
			break;
		case R.id.question_speak_next:
			// MyDialog("你已经答完本题确认继续下一题吗?", "确认", "取消", 1);
			MyDialog("你已经浏览完本次作业,确认退出吗？", "确认", "取消", 2);
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
					SpeakHistoryActivity.this.finish();
					intent.setClass(SpeakHistoryActivity.this,
							HomeWorkIngActivity.class);
					startActivity(intent);
					break;
				case 1:

					break;
				case 2:
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
}
