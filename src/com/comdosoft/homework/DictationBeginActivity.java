package com.comdosoft.homework;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.comdosoft.homework.pojo.DictationPojo;
import com.comdosoft.homework.pojo.QuestionPojo;
import com.comdosoft.homework.tools.HomeWorkParams;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.ListeningQuestionMap;
import com.comdosoft.homework.tools.Soundex_Levenshtein;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class DictationBeginActivity extends Activity implements OnClickListener {

	private int linearLayoutIndex = 0;
	private int qpIndex = 0;
	private String symbol;
	private List<Integer> indexList = new ArrayList<Integer>();
	private List<QuestionPojo> qpList = new ArrayList<QuestionPojo>();
	private List<DictationPojo> dictationList = new ArrayList<DictationPojo>();
	private List<EditText> etList = new ArrayList<EditText>();
	private List<LinearLayout> linearLayoutList = new ArrayList<LinearLayout>();
	private LinearLayout editLinearLayout;
	private TextView mesText;
	private TextView page;
	private TextView check;

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

		qpList = ListeningQuestionMap.getListeningPojo(0).getQuesttionList();
		init();
	}

	public void init() {
		linearLayoutIndex = 0;
		etList.clear();
		dictationList.clear();
		linearLayoutList.clear();
		editLinearLayout.removeAllViews();
		String content = qpList.get(qpIndex).getContent();
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

		page.setText(1 + qpIndex++ + "/" + qpList.size());
	}

	public void initView(int i) {
		LayoutInflater li = LayoutInflater.from(this);
		LinearLayout l = (LinearLayout) li.inflate(
				R.layout.question_dictation_begin_item, null);
		EditText et = (EditText) l.findViewById(R.id.question_item_edit);
		l.removeView(et);
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

	public void check() {
		if (check.getText().toString().equals("检查")) {
			// 检查
			for (int i = 0; i < etList.size(); i++) {
				String s = etList.get(i).getText().toString();
				if (s != null && !s.equals("")) {
					int value = Soundex_Levenshtein.dragonEngine(s,
							dictationList.get(i).getValue());
					if (value > 5) {
						dictationList.get(i).setFlag(1);
						etList.get(i).setTextColor(Color.rgb(146, 184, 27));
					} else {
						etList.get(i).setTextColor(Color.rgb(240, 134, 41));
					}
				}
			}

			// 答错提示剩余单词
			indexList.clear();
			StringBuffer sb = new StringBuffer();
			sb.append(HomeWorkParams.QUESTION_DICTATION_ERROR_MES);
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
			check.setText("检查");
			init();
		}
	}

	class MyTrehad extends Thread {

		@Override
		public void run() {
			super.run();
			HomeWorkTool
					.doPost("http://192.168.0.101:3004/api/students/record_answer_info", null);
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
		}
	}

}
