package com.comdosoft.homework;

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
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.homework.pojo.QuestionPojo;
import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.HomeWorkParams;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.ListeningQuestionList;
import com.comdosoft.homework.tools.PredicateLayout;
import com.comdosoft.homework.tools.Soundex_Levenshtein;
import com.comdosoft.homework.tools.Urlinterface;

public class SpeakBeginActivity extends Activity implements Urlinterface,
		OnPreparedListener, OnCompletionListener {
	public String content;// 记录本题正确答案
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
	private boolean Speak_type = false;
	private int student_id;
	private int school_class_id;
	private int publish_question_package_id;
	private int branch_question_id;
	private HomeWork homework;
	private List<QuestionPojo> branch_questions;
	private int index = 0;
	private String message;
	public String error_str = "";// 记录错误的词
	private ProgressDialog prodialog;
	private int speak_number;
	private int over_static;
	private ImageView question_speak_img;
	private String path;
	private boolean playFlag = false;
	private int width;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent intent = new Intent();
			Builder builder = new Builder(SpeakBeginActivity.this);
			builder.setTitle("提示");
			switch (msg.what) {
			case 0:
				prodialog.dismiss();
				index += 1;
				PredicateLayout.removeAllViews();
				question_speak_title.setText((index + 1) + "/"
						+ branch_questions.size());
				content = branch_questions.get(index).getContent();
				SetTextView();
				break;
			case 1:
				prodialog.dismiss();
				homework.setHistory_item(homework.getHistory_item() + 1);
				MyDialog("你已经答完本题确认继续下一题吗?", "确认", "取消", 1);
				break;
			case 2:
				prodialog.dismiss();
				homework.setQuestion_index(homework.getQuestion_index() + 1);
				SpeakBeginActivity.this.finish();
				intent.setClass(SpeakBeginActivity.this,
						SpeakPrepareActivity.class);
				startActivity(intent);
				break;
			case 3:
				prodialog.dismiss();
				builder.setMessage(message);
				builder.setPositiveButton("确定", null);
				builder.show();
				break;
			case 4:
				prodialog.dismiss();
				SpeakBeginActivity.this.finish();
				intent.setClass(SpeakBeginActivity.this,
						HomeWorkMainActivity.class);
				startActivity(intent);
				break;
			case 5:
				// MyDialog("恭喜完成今天的朗读作业!", "确认", "取消", 2);
				prodialog.dismiss();
				builder.setMessage("提交作业失败");
				builder.setPositiveButton("确定", null);
				builder.show();
				break;
			case 6:
				prodialog.dismiss();
				MyDialog("恭喜完成今天的朗读作业!", "确认", "取消", 2);
				break;
			case 7:
				question_speak_img.setImageDrawable(getResources().getDrawable(
						R.drawable.jzlbgreen));
				break;
			case 8:
				question_speak_img.setImageDrawable(getResources().getDrawable(
						R.drawable.jzlb));
				break;
			case 9:
				prodialog.dismiss();
				break;
			}
			super.handleMessage(msg);
		}
	};
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_speak_begin);
		homework = (HomeWork) getApplication();
		initialize();
		SetTextView();
		Display display = this.getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		publish_question_package_id = homework.getP_q_package_id();
		Log.i(tag, publish_question_package_id + "===");
		student_id = homework.getUser_id();
		school_class_id = homework.getClass_id();
		homework.setNewsFlag(true);
	}

	// 初始化
	public void initialize() {
		branch_questions = homework.getBranch_questions();
		content = branch_questions.get(0).getContent();
		question_speak_title = (TextView) findViewById(R.id.question_speak_title);
		question_speak_title.setText((index + 1) + "/"
				+ branch_questions.size());
		PredicateLayout = (PredicateLayout) findViewById(R.id.question_speak_content);
		question_speak_tishi = (TextView) findViewById(R.id.question_speak_tishi);
		question_speak_tishi.setVisibility(View.GONE);
		question_speak_img = (ImageView) findViewById(R.id.question_speak_img);
		player = new MediaPlayer();
		ok_speak = new HashMap<Integer, String>();// 用于记录正确的词
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
			if (HomeWorkTool.isConnect(getApplicationContext())) {
				if (speak_number == 4 || Speak_type == true) {
					speak_number = 0;
					stop();
					playFlag = false;
					Speak_type = false;
					question_speak_tishi.setVisibility(View.GONE);
					int ye = 0;
					if (homework.getHistory_item() >= homework
							.getQuestion_list().size()) {
						ye = homework.getQuestion_index();
					} else {
						ye = homework.getHistory_item();
					}
					if ((ye + 1) < homework.getQuestion_allNumber()) {
						prodialog = new ProgressDialog(SpeakBeginActivity.this);
						prodialog.setMessage(HomeWorkParams.PD_FINISH_QUESTION);
						prodialog.show();
						branch_question_id = branch_questions.get(index)
								.getId();
						if ((index + 1) < branch_questions.size()) {
							over_static = 0;
						} else {
							over_static = 1;
						}
						Thread thread = new Thread(new Record_answer_info());// 记录小题
						thread.start();
					} else {
						prodialog = new ProgressDialog(SpeakBeginActivity.this);
						prodialog.setMessage(HomeWorkParams.PD_FINISH_QUESTION);
						prodialog.show();
						branch_question_id = branch_questions.get(index)
								.getId();
						if ((index + 1) < branch_questions.size()) {
							over_static = 0;
						} else {
							over_static = 2;
							// new Thread(new SendWorkOver()).start();// 记录大題
						}
						Thread thread = new Thread(new Record_answer_info());// 记录小题
						thread.start();
					}
				} else {
					Toast.makeText(SpeakBeginActivity.this, "请先完成本题作业!",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						HomeWorkParams.INTERNET, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.question_speak_img:// 播放音频
			if (HomeWorkTool.isConnect(getApplicationContext())) {
				// 从文件系统播放
				path = IP + branch_questions.get(index).getUrl();
				if (player.isPlaying()) {// 正在播放
					stop();
				} else {
					if (playFlag) {
						handler.sendEmptyMessage(7);
						player.start();
					} else {
						playFlag = true;
						prodialog = new ProgressDialog(SpeakBeginActivity.this);
						prodialog.setMessage("正在缓冲...");
						prodialog.show();
						new Thread(new setPlay()).start();
					}
				}
			} else {
				Toast.makeText(getApplicationContext(),
						HomeWorkParams.INTERNET, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.speak:// 语音
			if (speak_number < 4) {
				speak_number += 1;
				Toast.makeText(SpeakBeginActivity.this,
						"第" + speak_number + "次答题", Toast.LENGTH_SHORT).show();
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
									if (HomeWorkTool
											.copyApkFromAssets(
													SpeakBeginActivity.this,
													"VoiceSearch.apk",
													Environment
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
			} else {
				Toast.makeText(SpeakBeginActivity.this, "本题的回答次数已经用完了,请继续下一题!",
						Toast.LENGTH_SHORT).show();
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
			Log.i("suanfa", "语音返回--->" + speak);
			str_list = new ArrayList<String>();
			content = content.replaceAll("'s","s");
			content = content.replaceAll("(?i)[^a-zA-Z0-9\u4E00-\u9FA5]", " ");// 去除标点符号
			content = content.replaceAll("  ", " ");
			String[] ok_arr = content.split(" ");
			Log.i("suanfa", "正确答案->" + content);
			String[] item = speak.split(" ");
			for (int i = 0; i < item.length; i++) {
				str_list.add(item[i]);
			}
			
			List<int[]> code_list = Soundex_Levenshtein.Engine2(content,
					str_list);
			Log.i("suanfa", code_list.size() + "");
			if (code_list.size() > 0) {
				for (int i = 0; i < code_list.size(); i++) {
					// Log.i(tag, str_list.get(code_list.get(i)[0]) + "->相似度:"
					// + code_list.get(i)[1]);
					if (code_list.get(i)[1] >= 7) {
						ok_speak.put(code_list.get(i)[0],
								ok_arr[code_list.get(i)[0]]);
						view_list.get(code_list.get(i)[0]).setBackgroundColor(
								getResources().getColor(R.color.lvse));
					} else {
						if (!error_str.contains(ok_arr[code_list.get(i)[0]])) {
							error_str += ok_arr[code_list.get(i)[0]] + "-!-";
						}
						view_list.get(code_list.get(i)[0]).setBackgroundColor(
								getResources().getColor(R.color.juhuang));
					}
				}

			} else {
				for (int i = 0; i < code_list.size(); i++) {
					view_list.get(code_list.get(i)[0]).setBackgroundColor(
							getResources().getColor(R.color.juhuang));
				}
			}
			Log.i(tag, ok_speak.size() + "-" + str_list.size());
			question_speak_tishi.setVisibility(View.VISIBLE);
			if (ok_speak.size() == ok_arr.length) {
				question_speak_tishi.setText(R.string.question_speak_tishi_ok);
				Speak_type = true;
			} else {
				question_speak_tishi.setText(R.string.question_speak_tishi);
			}
		} else {
			if (speak_number > 0) {
				speak_number -= 1;
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
					dialog.dismiss();
					SpeakBeginActivity.this.finish();
					intent.setClass(SpeakBeginActivity.this,
							HomeWorkMainActivity.class);
					startActivity(intent);
					break;
				case 1:
					dialog.dismiss();
					handler.sendEmptyMessage(2);

					break;
				case 2:
					dialog.dismiss();
					homework.setQuestion_index(0);
					SpeakBeginActivity.this.finish();
					intent.setClass(SpeakBeginActivity.this,
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
					SpeakBeginActivity.this.finish();
					intent.setClass(SpeakBeginActivity.this,
							HomeWorkMainActivity.class);
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

	// 记录小题的错词
	class Record_answer_info implements Runnable {
		public void run() {
			Looper.prepare();
			Log.i("linshi", "错词：" + error_str);
			Log.i("linshi",
					student_id + "->" + school_class_id + "->"
							+ publish_question_package_id + "->"
							+ homework.getQuestion_id() + "->"
							+ branch_question_id);
			Map<String, String> map = new HashMap<String, String>();
			map.put("school_class_id", school_class_id + "");
			map.put("student_id", student_id + "");
			map.put("publish_question_package_id", publish_question_package_id
					+ "");
			map.put("branch_question_id", branch_question_id + "");
			map.put("question_id", homework.getQuestion_id() + "");
			map.put("answer", error_str);
			map.put("question_types", 1 + "");

			String json;
			try {
				json = HomeWorkTool.doPost(RECORD_ANSWER_INFO, map);
				JSONObject obj = new JSONObject(json);
				if (obj.getString("status").equals("success")) {
					switch (over_static) {
					case 0:
						handler.sendEmptyMessage(0);
						break;
					case 1:
						handler.sendEmptyMessage(1);
						break;
					case 2:
						Log.i(tag, ListeningQuestionList.Record_Count
								+ "***"
								+ ListeningQuestionList.getListeningPojoList()
										.size());
						if (ListeningQuestionList.Record_Count == ListeningQuestionList
								.getListeningPojoList().size()) {
							new Thread(new SendWorkOver()).start();
						} else if (ListeningQuestionList.getListeningPojoList()
								.size() == 0) {
							new Thread(new SendWorkOver()).start();
						} else {
							handler.sendEmptyMessage(6);
						}
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Looper.loop();
		}
	}

	// 记录大题完成情况
	class SendWorkOver implements Runnable {
		public void run() {
			Looper.prepare();
			Map<String, String> map = new HashMap<String, String>();
			map.put("school_class_id", school_class_id + "");
			map.put("student_id", student_id + "");
			map.put("publish_question_package_id", publish_question_package_id
					+ "");
			Log.i(tag, school_class_id + "-" + student_id + "-" + "-"
					+ publish_question_package_id);
			String json;
			try {
				json = HomeWorkTool.doPost(FINISH_QUESTION_PACKGE, map);
				JSONObject obj = new JSONObject(json);
				if (obj.getString("status").equals("success")) {
					handler.sendEmptyMessage(6);

				} else {
					message = obj.getString("notice");
					handler.sendEmptyMessage(3);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Looper.loop();
		}
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
			Log.i("linshi", path + "/" + index);
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
		handler.sendEmptyMessage(9);
		handler.sendEmptyMessage(7);
		player.start();// 开始播放
	}

	public void stop() {
		handler.sendEmptyMessage(8);
		if (player.isPlaying()) {
			player.pause();
		}
	}

	protected void onStart() {
		if (player == null)
			player = new MediaPlayer();
		super.onStart();
	}

	// 停止音频
	protected void onStop() {
		if (player.isPlaying()) {// 正在播放
			player.stop();
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
			MyDialog("你还没有做完题,确认要退出吗?", "确认", "取消", 0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onCompletion(MediaPlayer mp) {
		Log.i("linshi", "over");
		handler.sendEmptyMessage(8);
	}
}
