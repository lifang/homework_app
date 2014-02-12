package com.comdosoft.homework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.comdosoft.homework.pojo.DictationPojo;
import com.comdosoft.homework.pojo.QuestionPojo;
import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.HomeWorkParams;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.ListeningQuestionList;
import com.comdosoft.homework.tools.Soundex_Levenshtein;
import com.comdosoft.homework.tools.Urlinterface;

// 拼写答题    马龙    2014年2月10日
public class DictationBeginActivity extends Activity implements
		OnClickListener, HomeWorkParams, OnPreparedListener,
		OnCompletionListener, Urlinterface {
	private int linearLayoutIndex = 0;
	private int mesLinearLayoutIndex = 0;
	private int smallIndex;
	private int bigIndex = 0;
	private int class_id;
	private int student_id;
	private int publish_question_package_id;
	private String log;
	private String symbol;
	private String mp3URL;
	private boolean mesFlag = false;
	private boolean playFlag = false;
	private List<Integer> indexList = new ArrayList<Integer>();
	private List<QuestionPojo> qpList = new ArrayList<QuestionPojo>();
	private List<DictationPojo> dictationList = new ArrayList<DictationPojo>();
	private List<EditText> etList = new ArrayList<EditText>();
	private List<TextView> tvList = new ArrayList<TextView>();
	private List<LinearLayout> linearLayoutList = new ArrayList<LinearLayout>();
	private List<LinearLayout> mesLinearLayoutList = new ArrayList<LinearLayout>();
	private Map<String, Integer> errorMap = new HashMap<String, Integer>();
	private LinearLayout editLinearLayout;
	private TextView mesText;
	private TextView page;
	private TextView check;
	private ImageView mPlayImg;
	private StringBuffer answer = new StringBuffer();
	private MediaPlayer mediaPlayer = new MediaPlayer();
	private ProgressDialog mPd;
	private HomeWork homeWork;
	private LayoutParams etlp;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				mPd.dismiss();
				break;
			case 2:
				mPlayImg.setImageResource(R.drawable.dictation_laba4);
				break;
			case 3:
				mPlayImg.setImageResource(R.drawable.dictation_laba3);
				break;
			case 4:
				mPd.setMessage("正在缓冲...");
				mPd.show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_dictation_begin);
		editLinearLayout = (LinearLayout) findViewById(R.id.question_dictation_linearLayout);
		mesText = (TextView) findViewById(R.id.question_dictation_mes);
		page = (TextView) findViewById(R.id.question_dictation_title);
		check = (TextView) findViewById(R.id.question_dictation_check_next);
		findViewById(R.id.question_dictation_exit).setOnClickListener(this);
		findViewById(R.id.question_dictation_check).setOnClickListener(this);
		mPlayImg = (ImageView) findViewById(R.id.question_dictation_play);
		mPlayImg.setOnClickListener(this);

		mPd = new ProgressDialog(this);
		mPd.setMessage(HomeWorkParams.PD_FINISH_QUESTION);
		homeWork = (HomeWork) getApplication();
		homeWork.setNewsFlag(true);
		publish_question_package_id = homeWork.getP_q_package_id();
		student_id = homeWork.getUser_id();
		class_id = homeWork.getClass_id();

		smallIndex = ListeningQuestionList.Small_Index;

		homeWork = (HomeWork) getApplication();
		publish_question_package_id = homeWork.getP_q_package_id();
		student_id = homeWork.getUser_id();
		class_id = homeWork.getClass_id();

		smallIndex = ListeningQuestionList.Small_Index;

		etlp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		etlp.leftMargin = 10;

		init();
	}

	// 初始化
	public void init() {
		// 清除数据
		mesFlag = false;
		linearLayoutIndex = 0;
		mesLinearLayoutIndex = 0;
		etList.clear();
		dictationList.clear();
		linearLayoutList.clear();
		mesLinearLayoutList.clear();
		errorMap.clear();
		editLinearLayout.removeAllViews();
		mesText.setVisibility(LinearLayout.GONE);

		playFlag = false;
		handler.sendEmptyMessage(3);

		// 获取已答过题目记录数
		// bigIndex = ListeningQuestionList.getRecordCount();
		bigIndex = ListeningQuestionList.Record_Count;

		// 获取小题数据
		qpList = ListeningQuestionList.getListeningPojo(bigIndex)
				.getQuesttionList();

		// 获取当前大&小题数据
		mp3URL = IP + qpList.get(smallIndex).getUrl();
		String content = qpList.get(smallIndex).getContent();
		String[] sArr = content.substring(0, content.length() - 1).split(" ");
		for (int i = 0; i < sArr.length; i++) {
			dictationList.add(new DictationPojo(sArr[i], 0));
		}

		// 获取标点
		symbol = content.substring(content.length() - 1, content.length());

		for (int i = 0; i < dictationList.size(); i++) {
			initView(i);
		}
		for (int i = 0; i < linearLayoutList.size(); i++) {
			editLinearLayout.addView(linearLayoutList.get(i));
			editLinearLayout.addView(mesLinearLayoutList.get(i));
		}

		page.setText(1 + smallIndex++ + "/" + qpList.size());
	}

	// 动态添加答题格子
	public void initView(int i) {
		EditText et = new EditText(getApplicationContext());
		String value = dictationList.get(i).getValue();
		et.setText(value);
		et.setWidth(value.length() * 20 + 80);
		et.setHeight(40);
		et.setGravity(Gravity.CENTER);
		et.setLayoutParams(etlp);
		et.setSingleLine(true);
		if (i == 0 || i % 4 == 0) {
			LinearLayout linear = new LinearLayout(getApplicationContext());
			linear.setOrientation(LinearLayout.HORIZONTAL);
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			lp.topMargin = 10;
			linear.setLayoutParams(lp);
			linearLayoutList.add(linear);
			if (i > 0) {
				linearLayoutIndex++;
			}
		}
		etList.add(et);
		linearLayoutList.get(linearLayoutIndex).addView(et);

		// 最后的标点符号
		if (i == dictationList.size() - 1) {
			TextView tv = new TextView(getApplicationContext());
			tv.setText(symbol);
			tv.setTextSize(24);
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			lp.topMargin = 10;
			lp.leftMargin = 10;
			tv.setLayoutParams(lp);
			linearLayoutList.get(linearLayoutIndex).addView(tv);
		}

		initMesView(i);
	}

	// 初始化不完全正确提示
	public void initMesView(int i) {
		TextView et = new TextView(getApplicationContext());
		String value = dictationList.get(i).getValue();
		et.setText(value);
		et.setWidth(value.length() * 20 + 80);
		et.setHeight(20);
		et.setTextSize(16);
		et.setTextColor(Color.rgb(110, 107, 107));
		et.setGravity(Gravity.CENTER);
		et.setLayoutParams(etlp);
		et.setSingleLine(true);
		et.setVisibility(View.INVISIBLE);
		if (i == 0 || i % 4 == 0) {
			LinearLayout linear = new LinearLayout(getApplicationContext());
			linear.setOrientation(LinearLayout.HORIZONTAL);
			mesLinearLayoutList.add(linear);
			if (i > 0) {
				mesLinearLayoutIndex++;
			}
		}
		tvList.add(et);
		mesLinearLayoutList.get(mesLinearLayoutIndex).addView(et);
	}

	// 检查算法
	public void check() {
		// 检查
		if (check.getText().toString().equals("检查")) {
			for (int i = 0; i < etList.size(); i++) {
				String s = etList.get(i).getText().toString();
				if (s != null && !s.equals("")) {
					int value = Soundex_Levenshtein.dragonEngine(s,
							dictationList.get(i).getValue());
					if (dictationList.get(i).getValue().equals(s)) {
						// 全对
						dictationList.get(i).setFlag(1);
						tvList.get(i).setVisibility(View.INVISIBLE);
						etList.get(i).setTextColor(Color.rgb(146, 184, 27));
					} else if (value > 5) {
						// 半对
						mesFlag = true;
						dictationList.get(i).setFlag(1);
						etList.get(i).setTextColor(Color.rgb(240, 134, 41));
						tvList.get(i).setVisibility(View.VISIBLE);
					} else {
						// 错误
						errorMap.put(dictationList.get(i).getValue(), 1);
						dictationList.get(i).setFlag(0);
						answer.append(s).append("-!-");
						tvList.get(i).setVisibility(View.INVISIBLE);
						etList.get(i).setTextColor(Color.rgb(255, 0, 0));
					}
				} else {
					dictationList.get(i).setFlag(0);
				}
			}

			// 答错提示剩余单词
			indexList.clear();
			for (int i = 0; i < dictationList.size(); i++) {
				if (dictationList.get(i).getFlag() == 0) {
					indexList.add(i);
				}
			}
			if (indexList.size() > 0) {
				StringBuffer sb = new StringBuffer();
				if (mesFlag) {
					sb.append(QUESTION_DICTATION_ERROR_MES_TWO);
				} else {
					sb.append(QUESTION_DICTATION_ERROR_MES);
				}
				mesText.setVisibility(LinearLayout.VISIBLE);
				Random r = new Random(System.currentTimeMillis());
				sb.append("\n◆"
						+ dictationList.get(
								indexList.get(r.nextInt(indexList.size())))
								.getValue());
				mesText.setText(sb.toString());
			} else {
				if (mesFlag) {
					mesText.setVisibility(LinearLayout.VISIBLE);
					mesText.setText(QUESTION_DICTATION_ERROR_MES_ONE);
				}
				check.setText("继续");
			}

		} else {
			// Next
			if (bigIndex < ListeningQuestionList.getListeningPojoList().size()) {
				if (answer.length() > 0) {
					answer.delete(answer.length() - 3, answer.length());
				}
				mPd.setMessage(HomeWorkParams.PD_FINISH_QUESTION);
				mPd.show();
				new MyTrehad(bigIndex, smallIndex).start();
				ListeningQuestionList.Small_Index = ListeningQuestionList.Small_Index + 1;
				if (smallIndex == qpList.size()) {
					// 切换大题
					bigIndex++;
					smallIndex = 0;

					ListeningQuestionList.Small_Index = 0;
					ListeningQuestionList.Record_Count = ListeningQuestionList.Record_Count + 1;

					if (bigIndex == ListeningQuestionList
							.getListeningPojoList().size()) {
						mPd.show();
						if (homeWork.getHistory_item() == homeWork
								.getQuestion_allNumber()) {
							new SendWorkOver().start();
						} else if (homeWork.getQuestion_allNumber() == 0) {
							new SendWorkOver().start();
						}
						MyDialog("恭喜完成今天的听写作业!", "确认", "取消", 2);
						return;
					}

					MyDialog("你已经答完本题确认继续下一题吗?", "确认", "取消", 1);
					return;
				}
				check.setText("检查");
				init();
			}
		}
	}

	// 提交答题记录
	class MyTrehad extends Thread {
		private int bIndex, sIndex;

		public void MyThread() {
		}

		public MyTrehad(int bIndex, int sIndex) {
			super();
			this.bIndex = bIndex;
			this.sIndex = sIndex;
		}

		public void run() {
			super.run();
			StringBuffer sb = new StringBuffer();
			Iterator iterator = errorMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) iterator
						.next();
				sb.append(entry.getKey()).append("-!-");
			}
			if (answer.length() > 0) {
				answer.delete(answer.length() - 3, answer.length());
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("student_id", student_id + "");
			map.put("school_class_id", class_id + "");
			map.put("publish_question_package_id", publish_question_package_id
					+ "");
			map.put("question_id",
					ListeningQuestionList.getListeningPojo(bIndex).getId() + "");
			map.put("branch_question_id", qpList.get(sIndex - 1).getId() + "");
			map.put("answer", sb.toString() + "");
			map.put("question_types", "0");

			log = HomeWorkTool.doPost(RECORD_ANSWER_INFO, map);
			Log.i("Ax", log);
			Log.i("Ax", sb.toString());
			answer.delete(0, answer.length());
			sb.delete(0, sb.length());
			handler.sendEmptyMessage(1);
		}
	}

	class SendWorkOver extends Thread {
		public void run() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("student_id", student_id + "");
			map.put("school_class_id", class_id + "");
			map.put("publish_question_package_id", publish_question_package_id
					+ "");
			log = HomeWorkTool.doPost(FINISH_QUESTION_PACKGE, map);
			Log.i("Ax", log);
			handler.sendEmptyMessage(1);
			handler.sendEmptyMessage(2);
		}
	}

	public void myFinish() {
		finish();
		Intent intent = new Intent();
		intent.setClass(DictationBeginActivity.this, HomeWorkMainActivity.class);
		startActivity(intent);
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
				switch (type) {
				case 0:
					myFinish();
					break;
				case 1:
					Intent intent = new Intent();
					intent.setClass(DictationBeginActivity.this,
							DictationPrepareActivity.class);
					startActivity(intent);
					break;
				case 2:
					myFinish();
					break;
				}
			}
		});
		dialog_no.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				switch (type) {
				case 0:
					break;
				case 1:
					myFinish();
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

	// 播放音频
	public void playerAmr() {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(mp3URL);
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

	class MyMediaPlay extends Thread {
		@Override
		public void run() {
			super.run();
			playerAmr();
		}
	}

	public void stop() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.question_dictation_exit:
			MyDialog("你还没有做完题,确认要退出吗?", "确认", "取消", 0);
			break;
		case R.id.question_dictation_check:
			check();
			break;
		case R.id.question_dictation_play:
			if (!playFlag) {
				new MyMediaPlay().start();
				playFlag = true;
				handler.sendEmptyMessage(4);
			} else if (mediaPlayer.isPlaying()) {
				handler.sendEmptyMessage(3);
				mediaPlayer.pause();
			} else {
				handler.sendEmptyMessage(2);
				mediaPlayer.start();
			}
			break;
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		handler.sendEmptyMessage(1);
		handler.sendEmptyMessage(2);
		mp.start();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		handler.sendEmptyMessage(3);
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

}
