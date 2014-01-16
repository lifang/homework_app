package com.comdosoft.homework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.comdosoft.homework.pojo.ListeningPojo;
import com.comdosoft.homework.pojo.QuestionPojo;
import com.comdosoft.homework.tools.ListeningQuestionMap;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

// 拼写准备    马龙    2014年1月16日
public class DictationPrepareActivity extends Activity implements
		OnClickListener, OnPreparedListener, OnCompletionListener {

	// private String JSON =
	// "	{\"status\":true,\"notice\":\"\",\"package\":{\"listening\":[{\"id\":\"1\",\"branch_questions\":[{\"id\":\"2\",\"content\":\"This is an apple.\",\"resource_url\":\"/question_packages_1/resource2.mp3\"},{\"id\":\"3\",\"content\":\"Why is Google undertaking such a venture?\",\"resource_url\":\"/question_packages_1/resource3.mp3\"}]},{\"id\":\"2\",\"branch_questions\":[{\"id\":\"4\",\"content\":\"The company likes to present itself as having lofty aspirations.\",\"resource_url\":\"/question_packages_2/resource4.mp3\"},{\"id\":\"5\",\"content\":\"At its centre, however, is one simple issue: that of copyright.\",\"resource_url\":\"/question_packages_2/resource5.mp3\"}]}],\"reading\":[{\"id\":\"3\",\"branch_questions\":[{\"id\":\"2\",\"content\":\"This is an apple.\",\"resource_url\":\"/question_packages_1/resource2.mp3\"},{\"id\":\"3\",\"content\":\"Why is Google undertaking such a venture?\",\"resource_url\":\"/question_packages_1/resource3.mp3\"}]},{\"id\":\"4\",\"branch_questions\":[{\"id\":\"4\",\"content\":\"The company likes to present itself as having lofty aspirations.\",\"resource_url\":\"/question_packages_2/resource4.mp3\"},{\"id\":\"5\",\"content\":\"At its centre, however, is one simple issue: that of copyright.\",\"resource_url\":\"/question_packages_2/resource5.mp3\"}]}]},\"user_answers\":{\"listening\":[{\"id\":\"1\",\"branch_questions\":[{\"id\":\"2\",\"answer\":\"This is;||;This is an ;||;This is an apple\"},{\"id\":\"3\",\"answer\":\"Why is Google;||;Why is Google __ venture;||;Why is Google undertaking such a venture?\"}]}],\"reading\":[{\"id\":\"1\",\"branch_questions\":[{\"id\":\"2\",\"answer\":\"/test.mp3;||;/test.mp3\"}]}]}}";
	private List<String> mp3List = new ArrayList<String>();
	private int mp3Index = 0;
	private MediaPlayer mediaPlayer = new MediaPlayer();
	private ImageView dictationImg;
	private ProgressDialog mPd;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				mPd.dismiss();
				List<ListeningPojo> listeningList = ListeningQuestionMap
						.getListeningPojoList();
				for (int i = 0; i < listeningList.size(); i++) {
					List<QuestionPojo> question = listeningList.get(i)
							.getQuesttionList();
					for (int j = 0; j < question.size(); j++) {
						// Toast.makeText(getApplicationContext(),
						// question.get(j).toString(), 0).show();
					}
				}
				break;
			case 2:
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
		mPd = new ProgressDialog(DictationPrepareActivity.this);
		mp3List.add("/mnt/sdcard/voice_1.mp3");
		mp3List.add("/mnt/sdcard/voice_1.mp3");
		mp3List.add("/mnt/sdcard/qlx.mp3");
		mPd.setMessage("正在加载...");
		mPd.show();
		new MyThread().start();
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

	// 解析 听写题目 json
	public void analyzeJson(String json) {
		try {
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
				ListeningQuestionMap.addListeningPojo(new ListeningPojo(id,
						question));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 获取听写题目json
	class MyThread extends Thread {
		@Override
		public void run() {
			super.run();
			try {
				// analyzeJson(HomeWorkTool
				// .sendGETRequest(
				// "http://192.168.0.130:3000/api/students/into_daily_tasks",
				// null));
				// analyzeJson(JSON);
				mHandler.sendEmptyMessage(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.question_dictation_img:
			playerAmr();
			break;
		case R.id.question_dictation_next:
			Intent intent = new Intent(this, DictationBeginActivity.class);
			startActivity(intent);
			break;
		case R.id.question_dictation_exit:
			this.finish();
			break;
		}
	}

	@Override
	public void onDestroy() {
		mediaPlayer.release();
		mediaPlayer = null;
		super.onDestroy();
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
	}

	// 音频播放完后继续播放下一个音频,直到所有音频播放完毕
	@Override
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
