package com.comdosoft.homework;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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

public class SpeakHistoryActivity extends Activity implements Urlinterface,
		OnPreparedListener {

	public String content;// 记录本题正确答案
	private TextView question_speak_title;
	private MediaPlayer player;
	public int number;// 播放次数
	private LinearLayout question_speak_tishi;
	private TextView question_speak_content;
	private List<List<String>> question_history;
	private HomeWork homework;
	private List<QuestionPojo> branch_questions;
	private int index = 0;
	private int question_history_size;
	private ImageView question_speak_img;
	private String path;
	public String error_str = "";// 记录错误的词
	private ProgressDialog prodialog;
	private boolean playFlag = false;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				question_speak_tishi.removeAllViews();
				index += 1;
				question_speak_title.setText((index + 1) + "/"
						+ branch_questions.size());
				content = branch_questions.get(index).getContent();
				question_speak_content.setText(content);

				if (question_history_size < (homework.getQuestion_index() + 1)) {
					setTishi("暂无错误词汇");
				} else if (question_history.get(homework.getQuestion_index())
						.size() < branch_questions.size()) {
					setTishi("暂无错误词汇");
				} else {
					setTishi(question_history.get(homework.getQuestion_index())
							.get(index));
				}
				break;
			case 1:
				question_speak_img.setImageDrawable(getResources().getDrawable(
						R.drawable.jzlbgreen));
				break;
			case 2:
				question_speak_img.setImageDrawable(getResources().getDrawable(
						R.drawable.jzlb));
				break;
			case 3:
				prodialog.dismiss();
				break;
			}
			super.handleMessage(msg);
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_speak_history);
		homework = (HomeWork) getApplication();
		homework.setNewsFlag(true);
		initialize();

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
		question_speak_img = (ImageView) findViewById(R.id.question_speak_img);
		question_speak_title = (TextView) findViewById(R.id.question_speak_title);
		question_speak_title.setText((index + 1) + "/"
				+ branch_questions.size());
		question_speak_content = (TextView) findViewById(R.id.question_speak_content);
		question_speak_content.setText(branch_questions.get(0).getContent());
		question_speak_tishi = (LinearLayout) findViewById(R.id.question_speak_tishi);
		player = new MediaPlayer();
		question_history = homework.getQuestion_history();
		question_history_size = homework.getQuestion_history().size();
		// 添加错词提示
		if (question_history_size < (homework.getQuestion_index() + 1)) {
			setTishi("暂无错误词汇");
		} else {
			setTishi(question_history.get(homework.getQuestion_index()).get(0));
		}
	}

	public void onclicks(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.question_speak_exit:
			MyDialog("确认要退出吗?", "确认", "取消", 0);
			break;
		case R.id.question_speak_next:
			stop();
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
			path = IP + branch_questions.get(index).getUrl();
			if (player.isPlaying()) {// 正在播放
				stop();
			} else {
				if (playFlag) {
					handler.sendEmptyMessage(1);
					player.start();
				} else {
					playFlag = true;
					prodialog = new ProgressDialog(SpeakHistoryActivity.this);
					prodialog.setMessage("正在缓冲...");
					prodialog.show();
					new Thread(new setPlay()).start();
				}
			}
			break;
		}
	}

	public void setTishi(String str) {
		Log.i(tag, str);
		if (str.contains("-!-")) {
			String[] str_arr = str.split("-!-");
			for (int i = 0; i < str_arr.length; i++) {
				initView(str_arr[i], i);
			}
		} else {
			initView(str, 0);
		}
	}

	public void initView(String str, int i) {
		TextView tv = new TextView(getApplicationContext());
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, 80);
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
							HomeWorkMainActivity.class);
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
							HomeWorkMainActivity.class);
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

	class setPlay implements Runnable {
		public void run() {
			play(path);
		}
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onPrepared(MediaPlayer mp) {
		handler.sendEmptyMessage(1);
		handler.sendEmptyMessage(3);
		player.start();// 开始播放
	}

	public void stop() {
		handler.sendEmptyMessage(2);
		if (player.isPlaying()) {
			player.pause();
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
			player.stop();// 停止
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
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MyDialog("确认要退出吗?", "确认", "取消", 0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}