package com.comdosoft.homework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.comdosoft.homework.pojo.ListeningPojo;
import com.comdosoft.homework.pojo.QuestionPojo;
import com.comdosoft.homework.tools.ListeningQuestionList;
import com.comdosoft.homework.tools.Urlinterface;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

// 拼写准备    马龙    2014年1月21日
public class DictationPrepareActivity extends Activity implements
		OnClickListener, OnPreparedListener, OnCompletionListener, Urlinterface {

	private String JSON = "	{\"status\":true,\"notice\":\"\",\"package\":{\"listening\":[{\"id\":\"1\",\"branch_questions\":[{\"id\":\"2\",\"content\":\"This is an apple.\",\"resource_url\":\"/question_packages_1/resource2.mp3\"},{\"id\":\"3\",\"content\":\"Why is Google undertaking such a venture?\",\"resource_url\":\"/question_packages_1/resource3.mp3\"}]},{\"id\":\"2\",\"branch_questions\":[{\"id\":\"4\",\"content\":\"The company likes to present itself as having lofty aspirations.\",\"resource_url\":\"/question_packages_2/resource4.mp3\"},{\"id\":\"5\",\"content\":\"At its centre, however, is one simple issue: that of copyright.\",\"resource_url\":\"/question_packages_2/resource5.mp3\"}]}],\"reading\":[{\"id\":\"3\",\"branch_questions\":[{\"id\":\"2\",\"content\":\"This is an apple.\",\"resource_url\":\"/question_packages_1/resource2.mp3\"},{\"id\":\"3\",\"content\":\"Why is Google undertaking such a venture?\",\"resource_url\":\"/question_packages_1/resource3.mp3\"}]},{\"id\":\"4\",\"branch_questions\":[{\"id\":\"4\",\"content\":\"The company likes to present itself as having lofty aspirations.\",\"resource_url\":\"/question_packages_2/resource4.mp3\"},{\"id\":\"5\",\"content\":\"At its centre, however, is one simple issue: that of copyright.\",\"resource_url\":\"/question_packages_2/resource5.mp3\"}]}]},\"user_answers\":{\"listening\":[{\"id\":\"1\",\"branch_questions\":[{\"id\":\"2\",\"answer\":\"This is;||;This is an ;||;This is an apple\"},{\"id\":\"3\",\"answer\":\"Why is Google;||;Why is Google __ venture;||;Why is Google undertaking such a venture?\"}]}],\"reading\":[{\"id\":\"1\",\"branch_questions\":[{\"id\":\"2\",\"answer\":\"/test.mp3;||;/test.mp3\"}]}]}}";
	private int mp3Index = 0;
	private List<String> mp3List = new ArrayList<String>();
	private MediaPlayer mediaPlayer = new MediaPlayer();
	private ImageView dictationImg;
	private ProgressDialog mPd;
	private Handler mHandler = new Handler() {
		
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				break;
			}
		}
	};

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_dictation_prepare);
		findViewById(R.id.question_dictation_next).setOnClickListener(this);
		findViewById(R.id.question_dictation_exit).setOnClickListener(this);
		dictationImg = (ImageView) findViewById(R.id.question_dictation_img);
		dictationImg.setOnClickListener(this);
		// setMp3Url();
	}

	
	protected void onRestart() {
		super.onRestart();
		Log.i("Ax", "onRestart");
		setMp3Url();
	}

	// 设置音频路径
	public void setMp3Url() {
		mp3List.clear();
		mp3Index = 0;
		// int index = ListeningQuestionList.getRecordCount();
		int index = ListeningQuestionList.Record_Count;
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

	// 解析 听写 json
	public void analyzeJson(String json) {
		try {
			// 解析听写记录
			if (new JSONObject(json).getJSONObject("user_answers")
					.getJSONArray("listening").length() > 0) {
				JSONArray answer = new JSONObject(json).getJSONObject(
						"user_answers").getJSONArray("listening");
				for (int i = 0; i < answer.length(); i++) {
					JSONObject jn = answer.getJSONObject(i);
					JSONArray jArr = jn.getJSONArray("branch_questions");
					List<String> smallList = new ArrayList<String>();
					for (int j = 0; j < jArr.length(); j++) {
						JSONObject jb = jArr.getJSONObject(j);
						smallList.add(jb.getString("answer"));
					}
					ListeningQuestionList.addAnswer(smallList);
				}
			}

			// 解析听写题目
			JSONArray ja = new JSONObject(json).getJSONObject("package")
					.getJSONArray("listening");
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jn = ja.getJSONObject(i);
				JSONArray jArr = jn.getJSONArray("branch_questions");
				int id = jn.getInt("id");
				List<QuestionPojo> question = new ArrayList<QuestionPojo>();
				for (int j = 0; j < jArr.length(); j++) {
					JSONObject jb = jArr.getJSONObject(j);
					question.add(new QuestionPojo(jb.getInt("id"), jb
							.getString("content"), jb.getString("resource_url")));
				}
				ListeningQuestionList.addListeningPojo(new ListeningPojo(id,
						question));

				ListeningQuestionList.Record_Count = 0;
				// ListeningQuestionList.Record_Count = ListeningQuestionList
				// .getRecordCount();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 获取听写题目json
	class MyThread extends Thread {
		
		public void run() {
			super.run();
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("student_id", "1");
				map.put("publish_question_package_id", "1");
				// analyzeJson(HomeWorkTool
				// .sendGETRequest(
				// "http://192.168.0.250:3004/api/students/into_daily_tasks",
				// map));
				analyzeJson(JSON);
				setMp3Url();
				mHandler.sendEmptyMessage(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.question_dictation_img:
			playerAmr();
			break;
		case R.id.question_dictation_next:
			DictationPrepareActivity.this.finish();
			intent.setClass(this, DictationBeginActivity.class);
			startActivity(intent);
			break;
		case R.id.question_dictation_exit:
			this.finish();
			intent.setClass(this, HomeWorkMainActivity.class);
			startActivity(intent);
			break;
		}
	}

	
	public void onDestroy() {
		mediaPlayer.release();
		mediaPlayer = null;
		super.onDestroy();
	}

	
	public void onPrepared(MediaPlayer mp) {
		mp.start();
	}

	// 音频播放结束后继续播放下一个音频,直到所有音频播放完毕
	
	public void onCompletion(MediaPlayer mp) {
		try {
			if (++mp3Index < mp3List.size()) {
				mp.reset();
				mp.setDataSource(mp3List.get(mp3Index));
				mp.prepare();
				mp.setOnPreparedListener(this);
				mp.setOnCompletionListener(this);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
