package com.comdosoft.homework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.homework.pojo.ListeningPojo;
import com.comdosoft.homework.pojo.QuestionPojo;
import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.HomeWorkParams;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;

public class SpeakPrepareActivity extends Activity implements Urlinterface,
		OnPreparedListener, OnCompletionListener {
	private int mp3Index = 0;
	private String content = "";
	private TextView question_speak_title;
	private LinearLayout layout;
//	private TextView question_speak_content;
	private TextView img_title;
	private MediaPlayer player;
	private String message;
	private ProgressDialog prodialog;
	private List<ListeningPojo> list;
	private HomeWork homework;
	private List<QuestionPojo> questionlist;
	private int History_item;
	private List<String> mp3List;
	private ImageView question_speak_img;
	private boolean playFlag = false;
	private int index;
	private List<TextView> tvlist;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			Builder builder = new Builder(SpeakPrepareActivity.this);
			builder.setTitle("提示");
			switch (msg.what) {
			case 1:
				break;
			case 2:
				builder.setMessage(message);
				builder.setPositiveButton("确定", null);
				builder.show();
				break;
			case 3:
				player.stop();
				player.release();
				player = null;
				break;
			case 4:
				Log.i("linshi", "kai");
				question_speak_img.setImageDrawable(getResources().getDrawable(
						R.drawable.dictation_laba2));
				break;
			case 5:
				Log.i("linshi", "guan");
				question_speak_img.setImageDrawable(getResources().getDrawable(
						R.drawable.dictation_laba1));
				break;
			case 6:
				prodialog.dismiss();
				break;
			case 7:
				prodialog = new ProgressDialog(SpeakPrepareActivity.this);
				prodialog.setCanceledOnTouchOutside(false);
				prodialog.setMessage("正在缓冲...");
				prodialog.show();
				break;
			case 8:
				for (int i = 0; i < tvlist.size(); i++) {
					tvlist.get(i).setTextColor(getResources().getColor(R.color.question_title_bg1));
				}
				Toast.makeText(getApplicationContext(), "点击右上角开始按钮进入答题界面", Toast.LENGTH_SHORT).show();
				break;
			case 9:
				for (int j = 0; j < tvlist.size(); j++) {
					if (j==index) {
						tvlist.get(index).setTextColor(getResources().getColor(R.color.lvse));
					}else{
						tvlist.get(j).setTextColor(getResources().getColor(R.color.question_title_bg1));
					}
				}
				break;
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_speak_prepare);
		homework = (HomeWork) getApplication();
		initialize();
		question_speak_title.setText("朗读题");
		homework.setNewsFlag(true);
		tvlist = new ArrayList<TextView>();
		Log.i("suanfa", homework.isWork_history() + "=======");
		list = homework.getQuestion_list();
		History_item = homework.getHistory_item();
		Log.i("linshi", homework.getHistory_item() + "-=" + list.size());
		if ((History_item >= list.size() && homework.getQuestion_history()
				.get(History_item - 1).size() >= list.get(list.size() - 1)
				.getQuesttionList().size())
				|| homework.isWork_history()) {// 表示查看历史记录
			questionlist = list.get(homework.getQuestion_index())
					.getQuesttionList();
			homework.setQ_package_id(list.get(homework.getQuestion_index())
					.getId());
			for (int i = 0; i < questionlist.size(); i++) {
				content += questionlist.get(i).getContent() + "\n";
			}
			img_title.setText("重听");
		} else {
			Log.i("linshi", History_item + "-->" + list.size());
			if (History_item == 0) {
				questionlist = list.get(0).getQuesttionList();
				homework.setQ_package_id(list.get(0).getId());
			} else {
				int number = list.get(History_item - 1).getQuesttionList()
						.size();// 题目实际题数
				int size;
				if (homework.getQuestion_history().size() == 0) {
					size = number;
				} else {
					size = homework.getQuestion_history()
							.get(homework.getHistory_item() - 1).size();// 题目实际题数
				}
				Log.i("linshi", homework.getHistory_item() - 1 + "--->>"
						+ number + ";;;;;;;;" + size);
				if (number > size) {
					questionlist = list.get(homework.getHistory_item() - 1)
							.getQuesttionList();
					homework.setQ_package_id(list.get(
							homework.getHistory_item() - 1).getId());
				} else {
					questionlist = list.get(homework.getHistory_item())
							.getQuesttionList();
					homework.setQ_package_id(list.get(
							homework.getHistory_item()).getId());
				}
			}
			for (int i = 0; i < questionlist.size(); i++) {
				content += questionlist.get(i).getContent() + "\n";
			}
			img_title.setText("预听");
		}
		
		for (int i = 0; i < questionlist.size(); i++) {
			setTextView(i);
		}
//		question_speak_content.setText(sp);
	}

	// 初始化
	public void initialize() {
		question_speak_title = (TextView) findViewById(R.id.question_speak_title);
		layout = (LinearLayout) findViewById(R.id.tvll);
//		question_speak_content = (TextView) findViewById(R.id.question_speak_content);
		question_speak_img = (ImageView) findViewById(R.id.question_speak_img);
		img_title = (TextView) findViewById(R.id.tou);
		player = new MediaPlayer();

	}
	
	private void setTextView(int i){
		View view1 = View.inflate(this, R.layout.text_view,
				null);
		TextView tv = (TextView) view1
				.findViewById(R.id.question_speak_content);
		tv.setText(questionlist.get(i).getContent());
		tvlist.add(tv);
		layout.addView(view1);
	}
	

	public void onclicks(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.question_speak_exit:
			MyDialog("确认不在继续答题吗？", "确定", "取消");
			break;
		case R.id.question_speak_next:
			stop();
			if ((History_item >= list.size() && homework.getQuestion_history()
					.get(History_item - 1).size() >= list.get(list.size() - 1)
					.getQuesttionList().size())
					|| homework.isWork_history()) {// 进入答题历史页面
				homework.setBranch_questions(list.get(
						homework.getQuestion_index()).getQuesttionList());
				intent.setClass(SpeakPrepareActivity.this,
						SpeakHistoryActivity.class);
				startActivity(intent);
			} else {
				if (homework.getHistory_item() == 0) {
					homework.setBranch_questions(list.get(0).getQuesttionList());
					homework.setQuestion_id(list.get(0).getId());
				} else {
					int number = list.get(homework.getHistory_item() - 1)
							.getQuesttionList().size();// 题目实际题数
					int size;
					if (homework.getQuestion_history().size() == 0) {
						size = number;
					} else {
						size = homework.getQuestion_history()
								.get(homework.getHistory_item() - 1).size();// 已做题数
					}
					Log.i("suanfa", size + "-");
					if (number > size) {
						List<QuestionPojo> qlist = list.get(
								homework.getHistory_item() - 1)
								.getQuesttionList();
						Log.i("suanfa", qlist.size() + "-");
						for (int i = 0; i < size; i++) {
							qlist.remove(0);
						}
						Log.i("suanfa", qlist.size() + "三---"
								+ qlist.get(0).getContent());
						homework.setBranch_questions(qlist);
						homework.setQuestion_id(list.get(
								homework.getHistory_item() - 1).getId());
						homework.setHistory_item(homework.getHistory_item() - 1);
					} else {
						homework.setBranch_questions(list.get(
								homework.getHistory_item()).getQuesttionList());
						homework.setQuestion_id(list.get(
								homework.getHistory_item()).getId());
					}
				}
				SpeakPrepareActivity.this.finish();
				intent.setClass(SpeakPrepareActivity.this,
						SpeakBeginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.question_speak_img:
			if (HomeWorkTool.isConnect(getApplicationContext())) {
				mp3List = new ArrayList<String>();
				for (int i = 0; i < questionlist.size(); i++) {
					Log.i("linshi", IP + questionlist.get(i).getUrl());
					mp3List.add(IP + questionlist.get(i).getUrl());
				}
				// 从文件系统播放
				if (player.isPlaying()) {// 暂停播放
					stop();
				} else {
					if (mp3Index >= mp3List.size()) {
						mp3Index = 0;
						handler.sendEmptyMessage(7);
						new Thread(new setPlay()).start();
					} else if (playFlag) {
						handler.sendEmptyMessage(4);
						player.start();
					} else {
						playFlag = true;
						handler.sendEmptyMessage(7);
						new Thread(new setPlay()).start();
					}
				}
			} else {
				Toast.makeText(getApplicationContext(),
						HomeWorkParams.INTERNET, Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	private void MyDialog(String title, String btn_one, String Btn_two) {
		final Intent intent = new Intent();
		final Dialog dialog = new Dialog(this, R.style.Transparent);
		dialog.setContentView(R.layout.my_dialog);
		dialog.setCancelable(true);
		TextView title_tv = (TextView) dialog.findViewById(R.id.dialog_title);
		title_tv.setText(title);
		Button dialog_ok = (Button) dialog.findViewById(R.id.dialog_ok);
		dialog_ok.setText(btn_one);
		Button dialog_no = (Button) dialog.findViewById(R.id.dialog_no);
		dialog_no.setText(Btn_two);
		dialog_ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				SpeakPrepareActivity.this.finish();
				intent.setClass(SpeakPrepareActivity.this,
						HomeWorkMainActivity.class);
				startActivity(intent);
			}
		});
		dialog_no.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * 播放音乐
	 * 
	 * @param playPosition
	 */
	class setPlay implements Runnable {
		public void run() {
			index = 0;
			Log.i("aaa", index +"-index");
			play(mp3List.get(mp3Index));
		}
	}

	private void play(String path) {
		try {
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
		handler.sendEmptyMessage(6);
		handler.sendEmptyMessage(4);
		handler.sendEmptyMessage(9);
		mp.start();
	}

	public void stop() {
		handler.sendEmptyMessage(5);
		if (player.isPlaying()) {
			player.pause();
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

	// 音频播放结束后继续播放下一个音频,直到所有音频播放完毕
	public void onCompletion(MediaPlayer mp) {
		try {
			if (++mp3Index < mp3List.size()) {
				index = mp3Index;
				Log.i("aaa", index+"-index");
				mp.reset();
				mp.setDataSource(mp3List.get(mp3Index));
				mp.prepare();
				mp.setOnPreparedListener(this);
				mp.setOnCompletionListener(this);
//				handler.sendEmptyMessage(9);
			} else {
				handler.sendEmptyMessage(5);
				handler.sendEmptyMessage(8);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MyDialog("确认不在继续答题吗？", "确定", "取消");
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
