package com.comdosoft.homework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.PredicateLayout;
import com.comdosoft.homework.tools.Soundex_Levenshtein;
import com.comdosoft.homework.tools.Urlinterface;

public class SpeakBeginActivity extends Activity implements Urlinterface {
	public String content = "This is an apple.";// 正确答案
	private TextView question_speak_title;
	private MediaPlayer player;
	private PredicateLayout PredicateLayout;
	public List<View> view_list;
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;// 语音code
	public List<String> str_list;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_speak);

		initialize();
		question_speak_title.setText("1/2");
		SetTextView();
	}

	// 初始化
	public void initialize() {
		question_speak_title = (TextView) findViewById(R.id.question_speak_title);
		PredicateLayout = (PredicateLayout) findViewById(R.id.question_speak_content);
		player = new MediaPlayer();
	}

	// 设置textview
	public void SetTextView() {
		view_list = new ArrayList<View>();
		String[] str = content.split(" ");
		for (int i = 0; i < str.length; i++) {
			View view1 = View.inflate(this,
					R.layout.question_speak_linearlayout, null);
			LinearLayout layout = (LinearLayout) view1
					.findViewById(R.id.layout);
			TextView text = (TextView) view1.findViewById(R.id.text);
			View color = (View) view1.findViewById(R.id.color);
			text.setText(str[i].toString());
			color.setBackgroundColor(getResources().getColor(
					R.color.work_date_untextcolor));
			view_list.add(color);
			PredicateLayout.addView(layout);
		}
	}

	public void onclicks(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.question_speak_exit:
			SpeakBeginActivity.this.finish();
			intent.setClass(SpeakBeginActivity.this, HomeWorkIngActivity.class);
			startActivity(intent);
			break;
		case R.id.question_speak_next:

			break;
		case R.id.question_speak_img:// 播放音频
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
		case R.id.speak:// 语音
			try {
				// 通过Intent传递语音识别的模式，开启语音
				Intent speak_intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				// 语言模式和自由模式的语音识别
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				// 提示语音开始
				intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "开始语音");
				// 开始语音识别
				startActivityForResult(speak_intent,
						VOICE_RECOGNITION_REQUEST_CODE);
			} catch (Exception e) {
				Builder builder = new Builder(SpeakBeginActivity.this);
				builder.setTitle("提示");
				builder.setMessage("您的设备未安装语音引擎,点击确定开始安装。");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (HomeWorkTool.copyApkFromAssets(
										SpeakBeginActivity.this,
										"VoiceSearch.apk", Environment
												.getExternalStorageDirectory()
												.getAbsolutePath()
												+ "/VoiceSearch.apk")) {
									Intent intent = new Intent(
											Intent.ACTION_VIEW);
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									intent.setDataAndType(
											Uri.parse("file://"
													+ Environment
															.getExternalStorageDirectory()
															.getAbsolutePath()
													+ "/VoiceSearch.apk"),
											"application/vnd.android.package-archive");
									SpeakBeginActivity.this
											.startActivity(intent);
								}
							}
						});
				builder.setNegativeButton("取消", null);
				builder.show();
			}
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 回调获取从谷歌得到的数据
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			// 取得语音的字符
			ArrayList<String> results = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			String speak = results.get(0);//用户语音返回的字符串
			str_list = new ArrayList<String>();
			String s = content.replaceAll("(?i)[^a-zA-Z0-9\u4E00-\u9FA5]", " ");//去除标点符号
			String[] item = s.split(" ");
		 	for (int i = 0; i < item.length; i++) {
		 		str_list.add(item[i]);
			}
			List<int[]> code_list = Soundex_Levenshtein.Engine(speak, str_list);
			if (code_list.size()>0) {
				for (int i = 0; i < code_list.size(); i++) {
					Log.i(tag, str_list.get(code_list.get(i)[0])+"->相似度:"+code_list.get(i)[1]);
					if (code_list.get(i)[1]>=8) {
						view_list.get(code_list.get(i)[0]).setBackgroundColor(getResources().getColor(
					R.color.question_speak_ok));
					}else if (code_list.get(i)[1]>=4) {
						view_list.get(code_list.get(i)[0]).setBackgroundColor(getResources().getColor(
								R.color.work_content_over_textcolor));
					}else{
						view_list.get(code_list.get(i)[0]).setBackgroundColor(getResources().getColor(
								R.color.work_date_untextcolor));
					}
				}
			}
		}
		// else {
		// rs = "null";
		// }
		super.onActivityResult(requestCode, resultCode, data);
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
