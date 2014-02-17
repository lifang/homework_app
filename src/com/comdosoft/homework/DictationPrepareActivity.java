package com.comdosoft.homework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.comdosoft.homework.pojo.ListeningPojo;
import com.comdosoft.homework.pojo.QuestionPojo;
import com.comdosoft.homework.tools.ListeningQuestionList;
import com.comdosoft.homework.tools.Urlinterface;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

// 拼写准备    马龙    2014年2月12日
public class DictationPrepareActivity extends Activity implements
		OnClickListener, OnPreparedListener, OnCompletionListener, Urlinterface {

	private int mp3Index = 0;
	private List<String> mp3List = new ArrayList<String>();
	private MediaPlayer mediaPlayer = new MediaPlayer();
	private ImageView dictationImg;
	private boolean playFlag = false;
	private ProgressDialog mPd;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				mPd.dismiss();
				dictationImg.setImageResource(R.drawable.dictation_laba2);
				break;
			case 2:
				dictationImg.setImageResource(R.drawable.dictation_laba1);
				break;
			case 3:
				mPd.show();
				break;
			case 4:
				dictationImg.setImageResource(R.drawable.dictation_laba2);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_dictation_prepare);
		findViewById(R.id.question_dictation_next).setOnClickListener(this);
		findViewById(R.id.question_dictation_exit).setOnClickListener(this);
		dictationImg = (ImageView) findViewById(R.id.question_dictation_img);
		dictationImg.setOnClickListener(this);
		mPd = new ProgressDialog(this);
		mPd.setCanceledOnTouchOutside(false);
		mPd.setMessage("正在缓冲...");
		setMp3Url();
		if (ListeningQuestionList.Record_Count == ListeningQuestionList.listeningList
				.size()) {
			TextView t = (TextView) findViewById(R.id.question_prepare_mes);
			t.setText("重听");
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		mHandler.sendEmptyMessage(2);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		setMp3Url();
	}

	// 设置音频路径
	public void setMp3Url() {
		mp3List.clear();
		mp3Index = 0;
		int index = ListeningQuestionList.Record_Count == ListeningQuestionList.listeningList
				.size() ? 0 : ListeningQuestionList.Record_Count;
		ListeningPojo lp = ListeningQuestionList.getListeningPojo(index);
		List<QuestionPojo> qpList = lp.getQuesttionList();
		for (int i = 0; i < qpList.size(); i++) {
			mp3List.add(IP + qpList.get(i).getUrl());
		}
	}

	// 播放音频
	public void playerAmr() {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(mp3List.get(mp3Index));
			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnCompletionListener(this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}

	class MyMediaPlay extends Thread {
		@Override
		public void run() {
			super.run();
			playerAmr();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.question_dictation_img:
			if (mediaPlayer.isPlaying()) {
				mHandler.sendEmptyMessage(2);
				stop();
			} else {
				mHandler.sendEmptyMessage(1);
				if (playFlag) {
					mediaPlayer.start();
				} else {
					playFlag = true;
					mHandler.sendEmptyMessage(3);
					new MyMediaPlay().start();
				}
			}
			break;
		case R.id.question_dictation_next:
			finish();
			Intent intent = new Intent();
			if (ListeningQuestionList.Record_Count == ListeningQuestionList.listeningList
					.size()) {
				intent.setClass(this, DictationRecordActivity.class);
			} else {
				intent.setClass(this, DictationBeginActivity.class);
			}
			startActivity(intent);
			break;
		case R.id.question_dictation_exit:
			MyDialog("你还没有做完题,确认要退出吗?", "确认", "取消", 0);
			break;
		}
	}

	// 销毁音频
	@Override
	public void onDestroy() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		super.onDestroy();
	}

	// 停止音频
	protected void onStop() {
		if (mediaPlayer.isPlaying()) {// 正在播放
			mediaPlayer.stop();// 停止
		}
		super.onStop();
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mHandler.sendEmptyMessage(1);
		mp.start();
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
			} else {
				mHandler.sendEmptyMessage(2);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 自定义dialog设置
	private void MyDialog(String title, String btn_one, String Btn_two,
			final int type) {
		final Dialog dialog = new Dialog(this, R.style.Transparent);
		dialog.setContentView(R.layout.my_dialog);
		dialog.setCancelable(true);

		ImageView dialog_img = (ImageView) dialog.findViewById(R.id.dialog_img);
		TextView title_tv = (TextView) dialog.findViewById(R.id.dialog_title);
		Button dialog_ok = (Button) dialog.findViewById(R.id.dialog_ok);
		Button dialog_no = (Button) dialog.findViewById(R.id.dialog_no);

		title_tv.setText(title);
		dialog_ok.setText(btn_one);
		dialog_no.setText(Btn_two);

		dialog_ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				DictationPrepareActivity.this.finish();
				Intent intent = new Intent();
				intent.setClass(DictationPrepareActivity.this,
						HomeWorkMainActivity.class);
				startActivity(intent);
			}
		});
		dialog_no.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MyDialog("你还没有做完题,确认要退出吗?", "确认", "取消", 0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
