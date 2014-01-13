package com.comdosoft.homework;

import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SpeakPrepareActivity extends Activity {
	public String content = "When I was a little girl ,I dreamed to grow up. Because I think a child doesn't has freedam,and can't do anything himself.But now I have grow up,to my surprise,I feel more tired and have more surfrng.Though I can do something myself, I don't feel happy at all.I believe you also have the same thoughs with me. when every us was a child , we wanted to grow up, but when we became a older man,we don't have such nice life as wish.";
	private TextView question_speak_title;
	private TextView question_speak_content;
	private MediaPlayer player;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_speak_prepare);

		initialize();
		question_speak_title.setText("朗读题");
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
