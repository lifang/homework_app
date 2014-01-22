package com.comdosoft.homework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.comdosoft.homework.pojo.DictationPojo;
import com.comdosoft.homework.pojo.QuestionPojo;
import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.HomeWorkParams;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.ListeningQuestionList;
import com.comdosoft.homework.tools.Soundex_Levenshtein;
import com.comdosoft.homework.tools.Urlinterface;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


// 拼写答题    马龙    2014年1月21日
public class DictationBeginActivity extends Activity implements
		OnClickListener, HomeWorkParams, OnPreparedListener, Urlinterface {

	private int linearLayoutIndex = 0;
	private int smallIndex = 0;
	private int bigIndex = 0;
	private int publish_question_package_id;
	private String symbol;
	private String mp3URL;
	private List<Integer> indexList = new ArrayList<Integer>();
	private List<QuestionPojo> qpList = new ArrayList<QuestionPojo>();
	private List<DictationPojo> dictationList = new ArrayList<DictationPojo>();
	private List<EditText> etList = new ArrayList<EditText>();
	private List<LinearLayout> linearLayoutList = new ArrayList<LinearLayout>();
	private LinearLayout editLinearLayout;
	private TextView mesText;
	private TextView page;
	private TextView check;
	private StringBuffer answer = new StringBuffer();
	private MediaPlayer mediaPlayer = new MediaPlayer();
	private ProgressDialog mPd;
	private HomeWork homeWork;

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
		findViewById(R.id.question_dictation_play).setOnClickListener(this);
		mPd = new ProgressDialog(this);
		mPd.setMessage(HomeWorkParams.PD_FINISH_QUESTION);

		homeWork = (HomeWork) getApplication();
		publish_question_package_id = homeWork.getP_q_package_id();

		init();
	}

	// 初始化
	public void init() {
		// 清除数据
		linearLayoutIndex = 0;
		etList.clear();
		dictationList.clear();
		linearLayoutList.clear();
		editLinearLayout.removeAllViews();

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
		}

		page.setText(1 + smallIndex++ + "/" + qpList.size());
	}

	// 动态添加答题格子
	public void initView(int i) {
		LayoutInflater li = LayoutInflater.from(this);
		LinearLayout l = (LinearLayout) li.inflate(
				R.layout.question_dictation_begin_item, null);
		EditText et = (EditText) l.findViewById(R.id.question_item_edit);
		l.removeView(et);
		et.setText(dictationList.get(i).getValue());
		etList.add(et);
		if (i == 0 || i % 5 == 0) {
			LinearLayout linear = new LinearLayout(getApplicationContext());
			linear.setOrientation(LinearLayout.HORIZONTAL);
			linearLayoutList.add(linear);
			if (i > 0) {
				linearLayoutIndex++;
			}
		}
		linearLayoutList.get(linearLayoutIndex).addView(et);

		// 最后的标点符号
		if (i == dictationList.size() - 1) {
			TextView tv = new TextView(getApplicationContext());
			tv.setText(symbol);
			tv.setTextSize(24);
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			lp.topMargin = 20;
			lp.leftMargin = 10;
			tv.setLayoutParams(lp);
			linearLayoutList.get(linearLayoutIndex).addView(tv);
		}
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
					if (value > 5) {
						dictationList.get(i).setFlag(1);
						etList.get(i).setTextColor(Color.rgb(146, 184, 27));
					} else {
						answer.append(s).append("-!-");
						etList.get(i).setTextColor(Color.rgb(240, 134, 41));
					}
				}
			}

			// 答错提示剩余单词
			indexList.clear();
			StringBuffer sb = new StringBuffer();
			sb.append(QUESTION_DICTATION_ERROR_MES);
			for (int i = 0; i < dictationList.size(); i++) {
				if (dictationList.get(i).getFlag() == 0) {
					indexList.add(i);
				}
			}
			if (indexList.size() > 0) {
				mesText.setVisibility(LinearLayout.VISIBLE);
				Random r = new Random(System.currentTimeMillis());
				sb.append("\n◆"
						+ dictationList.get(
								indexList.get(r.nextInt(indexList.size())))
								.getValue());
				mesText.setText(sb.toString());
			} else {
				check.setText("Next");
				mesText.setVisibility(LinearLayout.GONE);
			}

		} else {
			// Next
			if (bigIndex < ListeningQuestionList.getListeningPojoList().size()) {
				if (answer.length() > 0) {
					answer.delete(answer.length() - 3, answer.length());
				}
				mPd.show();
				new MyTrehad(bigIndex, smallIndex).start();
				if (smallIndex == qpList.size()) {
					// 切换答题
					bigIndex++;
					smallIndex = 0;

					if (bigIndex == ListeningQuestionList
							.getListeningPojoList().size()) {
						MyDialog("恭喜完成今天的朗读作业!", "确认", "取消", 2);
						return;
					}

					ListeningQuestionList.Record_Count = ListeningQuestionList.Record_Count + 1;
					MyDialog("你已经答完本题确认继续下一题吗?", "确认", "取消", 1);
					return;
				}
				check.setText("检查");
				init();
			}
		}
	}

	// 切换下一题
	public void nextQuestion() {
		mPd = new ProgressDialog(DictationBeginActivity.this);
		mPd.setMessage(HomeWorkParams.PD_FINISH_QUESTION);
		mPd.show();
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

		@Override
		public void run() {
			super.run();
			Map<String, String> map = new HashMap<String, String>();
			map.put("student_id", homeWork.getUser_id() + "");
			map.put("school_class_id ", homeWork.getClass_id() + "");
			map.put("publish_question_package_id", publish_question_package_id
					+ "");
			map.put("branch_question_id ", ListeningQuestionList
					.getListeningPojo(bIndex).getId() + "");
			map.put("question_id", qpList.get(sIndex - 1).getId() + "");
			map.put("answer", answer.toString());
			map.put("question_types", "0");
			Log.i("Ax",
					"bigId:"
							+ ListeningQuestionList.getListeningPojo(bIndex)
									.getId());
			Log.i("Ax", "question_id:" + qpList.get(sIndex - 1).getId());
			HomeWorkTool.doPost(SAVE_DICTATION, map);
			answer.delete(0, answer.length());
			mPd.dismiss();
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
					// check.setText("检查");
					// init();
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
			playerAmr();
			break;
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
	}

	@Override
	public void onDestroy() {
		mediaPlayer.release();
		mediaPlayer = null;
		super.onDestroy();
	}

}
