package com.comdosoft.homework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.comdosoft.homework.pojo.ListeningPojo;
import com.comdosoft.homework.pojo.QuestionPojo;
import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.Urlinterface;

public class SpeakPrepareActivity extends Activity implements Urlinterface,
		OnPreparedListener, OnCompletionListener {
	private int mp3Index = 0;
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
	private List<String> mp3List;
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
			case 3:
				player.stop();
				player.release();
				player = null;
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
		if (history.size()==0) {
			Log.i(tag, ""+history.size());
		}
		if (homework.isWork_history()) {// 表示查看历史记录
			questionlist = list.get(homework.getQuestion_index())
					.getQuesttionList();
			homework.setQ_package_id(list.get(homework.getQuestion_index())
					.getId());
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
			stop();
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
			mp3List = new ArrayList<String>();
			for (int i = 0; i < questionlist.size(); i++) {
				mp3List.add(IP + questionlist.get(i).getUrl());
			}
			// 从文件系统播放
			if (player.isPlaying()) {// 正在播放
				stop();
			} else {
				play(mp3List.get(mp3Index));
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

	/**
	 * 播放音乐
	 * 
	 * @param playPosition
	 */
	private void play(String path) {
		try {
			player.reset();// 把各项参数恢复到初始状态
			/**
			 * 通过MediaPlayer.setDataSource()
			 * 的方法,将URL或文件路径以字符串的方式传入.使用setDataSource ()方法时,要注意以下三点:
			 * 1.构建完成的MediaPlayer 必须实现Null 对像的检查.
			 * 2.必须实现接收IllegalArgumentException 与IOException
			 * 等异常,在很多情况下,你所用的文件当下并不存在. 3.若使用URL 来播放在线媒体文件,该文件应该要能支持pragressive
			 * 下载.
			 */
			player.setDataSource(path);
			player.prepare();// 进行缓冲
			player.setOnPreparedListener(this);
			player.setOnCompletionListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onPrepared(MediaPlayer mp) {
		mp.start();
	}

	public void stop() {
		if (player.isPlaying()) {
			player.stop();
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

	// 音频播放结束后继续播放下一个音频,直到所有音频播放完毕
	@Override
	public void onCompletion(MediaPlayer mp) {
		try {
			if (++mp3Index < mp3List.size()) {
				mp.reset();
				mp.setDataSource(mp3List.get(mp3Index));
				mp.prepare();
				mp.setOnPreparedListener(this);
				mp.setOnCompletionListener(this);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MyDialog("确认不在继续答题吗？", "确定", "取消");
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
