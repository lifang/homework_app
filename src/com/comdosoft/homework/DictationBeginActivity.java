package com.comdosoft.homework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.comdosoft.homework.pojo.DictationPojo;
import com.comdosoft.homework.pojo.QuestionPojo;
import com.comdosoft.homework.tools.HomeWorkParams;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.ListeningQuestionList;
import com.comdosoft.homework.tools.Soundex_Levenshtein;
import com.comdosoft.homework.tools.Urlinterface;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

// 拼写答题    马龙    2014年1月18日
public class DictationBeginActivity extends Activity implements
		OnClickListener, HomeWorkParams, OnPreparedListener, Urlinterface {

	private int linearLayoutIndex = 0;
	private int smallIndex = 0;
	private int bigIndex = 0;
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
		bigIndex = 0;

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
						answer.append(s).append(",||,");
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
			if (bigIndex < ListeningQuestionList.getListeningPojoList().size() - 1) {
				if (smallIndex == qpList.size()) {
					// 大题里最后一小题
					// ListeningQuestionList.delListeningPojoList(0);
					// Intent intnet = new Intent(getApplicationContext(),
					// HomeWorkIngActivity.class);
					// startActivity(intnet);
					bigIndex++;
					smallIndex = 0;
					// new MyTrehad().start();
					check.setText("检查");
					init();
				} else {
					// 切换下一小题
					if (answer.length() > 0) {
						answer.delete(answer.length() - 4, answer.length());
					}
					// new MyTrehad().start();
					check.setText("检查");
					init();
				}
			}
		}
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

		@Override
		public void run() {
			super.run();
			Map<String, String> map = new HashMap<String, String>();
			map.put("student_id", "1");
			map.put("school_class_id ", "1");
			map.put("publish_question_package_id", "1");
			map.put("branch_question_id ", ListeningQuestionList
					.getListeningPojo(0).getId() + "");
			map.put("question_id",
					qpList.get(smallIndex > 0 ? smallIndex - 1 : 0).getId()
							+ "");
			map.put("answer", answer.toString());
			map.put("question_types", "0");
			Log.i("Ax", "post:" + HomeWorkTool.doPost(SAVE_DICTATION, map));
			answer.delete(0, answer.length());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.question_dictation_exit:
			this.finish();
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
