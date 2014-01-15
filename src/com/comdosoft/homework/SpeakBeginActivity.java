package com.comdosoft.homework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.homework.tools.HomeWorkParams;
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
	public int number;// 播放次数
	private TextView question_speak_tishi;
	private Map<Integer, String> ok_speak;
	public MediaRecorder mediaRecorder;
	
	private int student_id = 1;
	private int ti_id = 1;
	private int school_class_id = 1;
	private int publish_question_package_id = 1;
	private int question_package_id = 1;
	
	
	private static String SDFile;
	public List<String> error_list;
	private ProgressDialog prodialog;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				break;
			case 1:
				break;
			}
			super.handleMessage(msg);
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_speak_begin);

		initialize();
		question_speak_title.setText("1/2");
		SetTextView();
		SDFile = "/sdcard/homework/" + student_id + "/" + ti_id + "/";
		File file = new File(SDFile);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	// 初始化
	public void initialize() {
		question_speak_title = (TextView) findViewById(R.id.question_speak_title);
		PredicateLayout = (PredicateLayout) findViewById(R.id.question_speak_content);
		question_speak_tishi = (TextView) findViewById(R.id.question_speak_tishi);
		question_speak_tishi.setVisibility(View.GONE);
		player = new MediaPlayer();
		ok_speak = new HashMap<Integer, String>();// 用于记录正确的词
		error_list = new ArrayList<String>();
	}

	// 设置textview
	public void SetTextView() {
		view_list = new ArrayList<View>();
		String[] str = content.split(" ");
		for (int i = 0; i < str.length; i++) {
			View view1 = View.inflate(this, R.layout.question_speak_begin_item,
					null);
			LinearLayout layout = (LinearLayout) view1
					.findViewById(R.id.layout);
			TextView text = (TextView) view1.findViewById(R.id.text);
			View color = (View) view1.findViewById(R.id.color);
			text.setText(str[i].toString());
			color.setBackgroundColor(getResources().getColor(R.color.shenhui));
			view_list.add(color);
			PredicateLayout.addView(layout);
		}
	}

	public void onclicks(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.question_speak_exit:
			MyDialog("你还没有做完题,确认要退出吗?", "确认", "取消", 0);
			break;
		case R.id.question_speak_next:
			// MyDialog("你已经答完本题确认继续下一题吗?", "确认", "取消", 1);
			MyDialog("恭喜完成今天的朗读作业!", "确认", "取消", 2);
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
			// stopService(service_intent);
			// 取得语音的字符
			ArrayList<String> results = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String speak = results.get(0);// 用户语音返回的字符串
			str_list = new ArrayList<String>();
			String s = content.replaceAll("(?i)[^a-zA-Z0-9\u4E00-\u9FA5]", " ");// 去除标点符号
			String[] item = s.split(" ");
			for (int i = 0; i < item.length; i++) {
				str_list.add(item[i]);
			}
			// question_speak_tishi
			List<int[]> code_list = Soundex_Levenshtein.Engine(speak, str_list);
			if (code_list.size() > 0) {
				for (int i = 0; i < code_list.size(); i++) {
					Log.i(tag, str_list.get(code_list.get(i)[0]) + "->相似度:"
							+ code_list.get(i)[1]);
					if (code_list.get(i)[1] >= 8) {
						ok_speak.put(code_list.get(i)[0],
								str_list.get(code_list.get(i)[0]));
						view_list.get(code_list.get(i)[0]).setBackgroundColor(
								getResources().getColor(R.color.lvse));
					} else if (code_list.get(i)[1] >= 4) {
						error_list.add(str_list.get(code_list.get(i)[0]));// 记录错误信息
						view_list.get(code_list.get(i)[0]).setBackgroundColor(
								getResources().getColor(R.color.juhuang));
					} else {
						error_list.add(str_list.get(code_list.get(i)[0]));// 记录错误信息
						view_list.get(code_list.get(i)[0]).setBackgroundColor(
								getResources().getColor(R.color.shenhui));
					}
				}
			} else {
				for (int i = 0; i < code_list.size(); i++) {
					view_list.get(code_list.get(i)[0]).setBackgroundColor(
							getResources().getColor(R.color.shenhui));
				}
			}
			Log.i(tag, ok_speak.size() + "-" + str_list.size());
			if (ok_speak.size() != str_list.size()) {
				question_speak_tishi.setVisibility(View.VISIBLE);
			} else {
				question_speak_tishi.setVisibility(View.GONE);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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
					SpeakBeginActivity.this.finish();
					intent.setClass(SpeakBeginActivity.this,
							HomeWorkIngActivity.class);
					startActivity(intent);
					break;
				case 1:

					break;
				case 2:
					prodialog = new ProgressDialog(SpeakBeginActivity.this);
					prodialog.setMessage(HomeWorkParams.PD_FINISH_QUESTION);
					prodialog.show();
					Thread thread = new Thread(new SendWorkOver());
					thread.start();
//					SpeakBeginActivity.this.finish();
//					intent.setClass(SpeakBeginActivity.this,
//							HomeWorkIngActivity.class);
//					startActivity(intent);
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
					SpeakBeginActivity.this.finish();
					intent.setClass(SpeakBeginActivity.this,
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
	
	class Record_answer_info implements Runnable {
		public void run() {
			Looper.prepare();
			Map<String, String> map = new HashMap<String, String>();
			map.put("school_class_id", school_class_id + "");
			map.put("student_id", student_id + "");
			map.put("question_package_id", question_package_id + "");
//			map.put("question_id", question_id + "");
//			map.put("branch_question_id", branch_question_id + "");
//			map.put("answer", answer);
//			map.put("question_types", question_types+"");
			
			String json;
			try {
				json = HomeWorkTool.doPost(RECORD_ANSWER_INFO, map);
				JSONObject obj = new JSONObject(json);
				if (obj.getString("status").equals("success")) {
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Looper.loop();
		}
	}

	class SendWorkOver implements Runnable {
		public void run() {
			Looper.prepare();
			Map<String, String> map = new HashMap<String, String>();
			map.put("school_class_id", school_class_id + "");
			map.put("student_id", student_id + "");
			map.put("question_package_id", question_package_id + "");
			map.put("publish_question_package_id", publish_question_package_id + "");
			String json;
			try {
				json = HomeWorkTool.doPost(FINISH_QUESTION_PACKGE, map);
				JSONObject obj = new JSONObject(json);
				if (obj.getString("status").equals("success")) {
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Looper.loop();
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
