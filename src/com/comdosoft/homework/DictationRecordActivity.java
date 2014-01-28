package com.comdosoft.homework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.ListeningQuestionList;

// 拼写记录    马龙    2014年1月20日
public class DictationRecordActivity extends Activity implements
		OnClickListener, OnPreparedListener {

	private int bigIndex = 0;
	private int smallIndex = 0;
	private String mp3URL;
	private LinearLayout linearLayout;
	private TextView topic;
	private TextView page;
	private List<List<String>> bigList = new ArrayList<List<String>>();
	private List<String> smallList = new ArrayList<String>();
	private List<String> errorList = new ArrayList<String>();
	private MediaPlayer mediaPlayer = new MediaPlayer();
	private HomeWork homeWork;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_dictation_record);
		homeWork = (HomeWork) getApplication();
		homeWork.setNewsFlag(true);
		findViewById(R.id.question_dictation_exit).setOnClickListener(this);
		findViewById(R.id.question_dictation_check).setOnClickListener(this);
		findViewById(R.id.question_dictation_play).setOnClickListener(this);
		topic = (TextView) findViewById(R.id.question_dictation_topic);
		page = (TextView) findViewById(R.id.question_dictation_page);
		linearLayout = (LinearLayout) findViewById(R.id.question_dictation_record_linearLayout);

		initData();
	}

	public void initData() {
		errorList.clear();
		linearLayout.removeAllViews();

		if (smallIndex == smallList.size() && bigIndex < bigList.size() - 1) {
			bigIndex++;
			smallIndex = 0;
		}

		bigList = ListeningQuestionList.getAnswerList();
		smallList = bigList.get(bigIndex);
		String[] sArr = smallList.get(smallIndex).split("-!-");

		errorList.add("你需要多听,多写的词:");
		for (int i = 0; i < sArr.length; i++) {
			errorList.add(sArr[i]);
		}

		if (smallIndex < smallList.size()) {
			smallIndex++;
		}

		if (errorList.size() > 2) {
			linearLayout.setVisibility(linearLayout.VISIBLE);
			for (int i = 0; i < errorList.size(); i++) {
				initView(i);
			}
		} else {
			linearLayout.setVisibility(LinearLayout.GONE);
		}

		mp3URL = ListeningQuestionList.getListeningPojo(bigIndex)
				.getQuesttionList().get(smallIndex - 1).getUrl();
		topic.setText(ListeningQuestionList.getListeningPojo(bigIndex)
				.getQuesttionList().get(smallIndex - 1).getContent());
		page.setText(smallIndex + "/" + smallList.size());
	}

	public void initView(int i) {
		TextView tv = new TextView(getApplicationContext());
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, 80);
		tv.setLayoutParams(lp);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setText("  " + errorList.get(i));
		tv.setTextColor(Color.rgb(157, 156, 156));
		tv.setTextSize(24);
		if (i % 2 != 0) {
			tv.setBackgroundColor(Color.rgb(231, 231, 231));
		} else {
			tv.setBackgroundColor(Color.rgb(255, 255, 255));
		}
		linearLayout.addView(tv);
	}

	// 播放音频
	public void playerAmr() {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(mp3URL);
			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.question_dictation_exit:
			MyDialog("确认要退出吗?", "确认", "取消", 0);
			break;
		case R.id.question_dictation_check:
			if (smallIndex == smallList.size()
					&& bigIndex == bigList.size() - 1) {
				MyDialog("确认要退出吗?", "确认", "取消", 0);
			} else if (smallIndex <= smallList.size()
					&& bigIndex < bigList.size()) {
				initData();
			}
			break;
		case R.id.question_dictation_play:
			playerAmr();
			break;
		}
	}

	public void onPrepared(MediaPlayer mp) {
		mp.start();
	}

	public void onDestroy() {
		mediaPlayer.release();
		mediaPlayer = null;
		super.onDestroy();
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
				DictationRecordActivity.this.finish();
				Intent intent = new Intent();
				intent.setClass(DictationRecordActivity.this,
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
			MyDialog("确认要退出吗?", "确认", "取消", 0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
