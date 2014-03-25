package com.comdosoft.homework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.HomeWorkParams;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;

public class HomeWorkMainActivity extends TabActivity implements Urlinterface {
	TabHost tabhost;
	TabHost.TabSpec spec1, spec2, spec3, spec4;
	private Resources res;
	public Field mBottomLeftStrip;
	public Field mBottomRightStrip;
	private HomeWork homework;
	private int count;
	private boolean flag = true;
	private boolean flag_hw = true;
	private int lastCount;
	private int Size;
	private String num = "0";
	private int hw_num = 0;
	private int width;
	private List<Integer> new_idlist = new ArrayList<Integer>();
	public static HomeWorkMainActivity instance = null;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;
	private boolean cancelUpdate = false;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homewrok_main);
		homework = (HomeWork) getApplication();
		instance = this;
		tabhost = getTabHost();
		res = getResources();

		Display display = this.getWindowManager().getDefaultDisplay();
		width = display.getWidth();

		Intent intent1 = new Intent(this, Classxinxiliu.class);
		spec1 = tabhost.newTabSpec("spec1")
				.setIndicator("", res.getDrawable(R.drawable.th1_1))
				.setContent(intent1);
		tabhost.addTab(spec1);

		Intent intent2 = new Intent(this, HomeWorkIngActivity.class);
		spec2 = tabhost.newTabSpec("spec2")
				.setIndicator("", res.getDrawable(R.drawable.th2_2))
				.setContent(intent2);
		tabhost.addTab(spec2);

		Intent intent3 = new Intent(this, AboutMeActivity.class);
		spec3 = tabhost.newTabSpec("spec3")
				.setIndicator("", res.getDrawable(R.drawable.th3_3))
				.setContent(intent3);
		tabhost.addTab(spec3);

		Intent intent4 = new Intent(this, SettingActivity.class);
		spec4 = tabhost.newTabSpec("spec4")
				.setIndicator("", res.getDrawable(R.drawable.th4_4))
				.setContent(intent4);
		tabhost.addTab(spec4);
		tabhost.setCurrentTab(homework.getMainItem());
		updateTabStyle(tabhost);
		if (HomeWorkTool.isConnect(HomeWorkMainActivity.this)) {
			thread.start();
			thread_work.start();
			GetCurrent_Version.start();
		} else {
			Builder builder = new Builder(HomeWorkMainActivity.this);
			builder.setTitle("提示");
			builder.setMessage(R.string.internet_error);
			builder.setPositiveButton("确定", null);
			builder.show();
		}
	}

	Thread thread = new Thread() {
		public void run() {
			indexNews();
		}
	};

	Thread thread_work = new Thread() {
		public void run() {
			getHomeWork();
		}
	};
	//
	Thread GetCurrent_Version = new Thread() {
		public void run() {
			Map<String, String> mp = new HashMap<String, String>();
			try {
				String json = HomeWorkTool.sendGETRequest(version, mp);
				if (!json.equals("")) {
					JSONObject obj = new JSONObject(json);
					double version = obj.getDouble("current_version");
					if (version > Urlinterface.current_version) {
						handler.sendEmptyMessage(3);
					}
				}
			} catch (Exception e) {
			}
		}
	};

	Handler handler = new Handler() {
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what) {
			case 0:
				TextView textview = (TextView) tabhost.getTabWidget()
						.getChildAt(2).findViewById(android.R.id.title);
				if (width >= 1200) {
					textview.setPadding(27, 5, 5, 53);
				} else {
					textview.setPadding(20, 5, 5, 40);
				}
				textview.setTextSize(10);

				textview.setTextColor(Color.parseColor("#ffffff"));
				if (homework.isNewsFlag() == true) {
					num = msg.obj.toString();
					if (Integer.valueOf(msg.obj.toString()) <= 0) {
					} else {
						View mView = tabhost.getTabWidget().getChildAt(2);// 0是代表第一个Tab
						ImageView imageView = (ImageView) mView
								.findViewById(android.R.id.icon);// 获取控件imageView
						imageView.setImageDrawable(getResources().getDrawable(
								R.drawable.news)); // 改变我们需要的图标
						textview.setText(msg.obj + "");
						Log.i("MyReceiver", msg.obj + "<<<");

					}
				} else {
					if (msg.obj.toString().equals("0")) {
						textview.setText("");
					} else {
						textview.setText("");
					}
				}
				break;
			case 1:
				TextView homework_view = (TextView) tabhost.getTabWidget()
						.getChildAt(1).findViewById(android.R.id.title);
				if (width >= 1200) {
					homework_view.setPadding(27, 5, 5, 53);
				} else {
					homework_view.setPadding(20, 5, 5, 40);
				}
				homework_view.setTextSize(10);
				homework_view.setTextColor(Color.parseColor("#ffffff"));
				if (flag_hw == true && hw_num != 0
						&& homework.getMainItem() != 1) {
					View mView = tabhost.getTabWidget().getChildAt(1);// 0是代表第一个Tab
					ImageView imageView = (ImageView) mView
							.findViewById(android.R.id.icon);// 获取控件imageView
					if (flag_hw) {
						imageView.setImageDrawable(getResources().getDrawable(
								R.drawable.th2_2_new));// 改变我们需要的图标
					} else {

					}
					homework_view.setText(hw_num + "");
				} else {
					if (hw_num == 0) {
						homework_view.setText("");
					} else {
						homework_view.setText("");
					}
				}
				break;
			case 2:
				Toast.makeText(getApplicationContext(),
						HomeWorkParams.INTERNET, Toast.LENGTH_SHORT).show();
				break;
			case 3:
				if (homework.isUpdate()) {
					Builder builder = new Builder(HomeWorkMainActivity.this);
					builder.setTitle("提示");
					builder.setMessage("检测到新版本,您需要更新吗？");
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									showDownloadDialog();
								}
							});
					builder.setNegativeButton("下次再说",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									homework.setUpdate(false);
								}
							}).show();
				}
				break;
			case 4:
				// 设置进度条位置
				mProgress.setProgress(progress);
				break;
			case 5:
				// 安装文件
				installApk();
				break;
			}
		}
	};

	public void indexNews() {
		SharedPreferences sp = getSharedPreferences(Urlinterface.SHARED, 0);
		String user_id = sp.getString("user_id", "null");
		String school_class_id = sp.getString("school_class_id", "null");
		while (flag) {
			if (HomeWorkTool.isConnect(HomeWorkMainActivity.this)) {
				try {
					HashMap<String, String> mp = new HashMap<String, String>();
					mp.put("user_id", user_id);
					mp.put("school_class_id", school_class_id);
					String json = HomeWorkTool.sendGETRequest(
							Urlinterface.get_News, mp);
					Message msg = new Message();
					Size = getNewsJson(json);
					if (homework.isNewsFlag()) {
						if (homework.getLastcount() == getNewsJson(json)) {
							count = lastCount;
						} else {
							count = getNewsJson(json) - homework.getLastcount();
						}
					} else {
						if (homework.getLastcount() == getNewsJson(json)) {
							count = 0;
						} else {
							count = getNewsJson(json) - homework.getLastcount();
						}
						count = 0;
					}
					homework.setHw_number(hw_num);
					homework.setNew_id_list(new_idlist);
					msg.what = 0;
					msg.obj = count;
					Log.i("aaa", count + "");
					handler.sendMessage(msg);
					thread.sleep(60000);
				} catch (Exception e) {
					// handler.sendEmptyMessage(2);

				}
			}
		}
	}

	public void getHomeWork() {
		SharedPreferences sp = getSharedPreferences(Urlinterface.SHARED, 0);
		String id = sp.getString("id", "null");
		String school_class_id = sp.getString("school_class_id", "null");
		while (flag_hw) {
			if (HomeWorkTool.isConnect(getApplicationContext())) {
				HashMap<String, String> mp = new HashMap<String, String>();
				mp.put("student_id", id);
				mp.put("school_class_id", school_class_id);
				try {
					String json = HomeWorkTool.sendGETRequest(
							Urlinterface.NEW_HOMEWORK, mp);
					homework.setHw_number(0);
					homework.setNew_id_list(new_idlist);
					JSONObject obj = new JSONObject(json);
					JSONArray arr = obj.getJSONArray("new_id");
					hw_num = arr.length();
					for (int i = 0; i < arr.length(); i++) {
						new_idlist.add(arr.getInt(i));
					}
					Log.i("suanfa", hw_num + "");
					homework.setHw_number(hw_num);
					homework.setNew_id_list(new_idlist);
					handler.sendEmptyMessage(1);
					thread_work.sleep(60000);
				} catch (Exception e) {
					// handler.sendEmptyMessage(2);
				}
			}
		}
	}

	public int getNewsJson(String json) {
		try {
			JSONObject jsonobject = new JSONObject(json);
			String status = (String) jsonobject.get("status");
			if (status.equals("success")) {
				JSONArray jsonarray = jsonobject.getJSONArray("messages");
				return jsonarray.length();
			} else {
				String notic = (String) jsonobject.get("notic");
				Toast.makeText(getApplicationContext(), notic,
						Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private void updateTabStyle(final TabHost mTabHost) {
		final TabWidget tabWidget = mTabHost.getTabWidget();
		tabWidget.setBackgroundColor(res.getColor(R.color.lvse));
		tabWidget.setGravity(Gravity.CENTER_VERTICAL);
		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			RelativeLayout tabView = (RelativeLayout) mTabHost.getTabWidget()
					.getChildAt(i);
			if (width == 800) {
				LayoutParams lp = new LayoutParams(50, 90);
				tabView.setLayoutParams(lp);
			}
			ImageView img = (ImageView) tabWidget.getChildAt(i).findViewById(
					android.R.id.icon);

			if (i == homework.getMainItem()) {
				tabView.setBackgroundColor(res.getColor(R.color.white));
				switch (i) {
				case 0:
					img.setImageResource(R.drawable.th1);
					break;
				case 1:
					img.setImageResource(R.drawable.th2);
					break;
				case 2:
					img.setImageResource(R.drawable.th3);
					break;
				case 3:
					img.setImageResource(R.drawable.th4);
					break;
				}
			} else {
				tabView.setBackgroundColor(res.getColor(R.color.lvse));
				switch (i) {
				case 0:
					img.setImageResource(R.drawable.th1_1);
					break;
				case 1:
					img.setImageResource(R.drawable.th2_2);
					break;
				case 2:
					img.setImageResource(R.drawable.th3_3);
					break;
				case 3:
					img.setImageResource(R.drawable.th4_4);
					break;
				}
			}
			img.setPadding(0, 15, 0, 15);
			/**
			 * 此方法是为了去掉系统默认的色白的底角
			 * 
			 * 在 TabWidget中mBottomLeftStrip、mBottomRightStrip
			 * 都是私有变量，但是我们可以通过反射来获取
			 * 
			 * 由于Android 2.2，2.3的接口不同，加个判断
			 */

			if (Float.valueOf(Build.VERSION.RELEASE.substring(0, 3)) <= 2.1) {
				try {
					mBottomLeftStrip = tabWidget.getClass().getDeclaredField(
							"mBottomLeftStrip");
					mBottomRightStrip = tabWidget.getClass().getDeclaredField(
							"mBottomRightStrip");
					if (!mBottomLeftStrip.isAccessible()) {
						mBottomLeftStrip.setAccessible(true);
					}
					if (!mBottomRightStrip.isAccessible()) {
						mBottomRightStrip.setAccessible(true);
					}
					mBottomLeftStrip.set(tabWidget,
							getResources().getDrawable(R.drawable.no));
					mBottomRightStrip.set(tabWidget, getResources()
							.getDrawable(R.drawable.no));

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {

				// 如果是2.2,2.3版本开发,可以使用一下方法tabWidget.setStripEnabled(false)
				// tabWidget.setStripEnabled(false);

				// 但是很可能你开发的android应用是2.1版本，
				// tabWidget.setStripEnabled(false)编译器是无法识别而报错的,这时仍然可以使用上面的
				// 反射实现，但是代码的改改

				try {
					// 2.2,2.3接口是mLeftStrip，mRightStrip两个变量，当然代码与上面部分重复了
					mBottomLeftStrip = tabWidget.getClass().getDeclaredField(
							"mLeftStrip");
					mBottomRightStrip = tabWidget.getClass().getDeclaredField(
							"mRightStrip");
					if (!mBottomLeftStrip.isAccessible()) {
						mBottomLeftStrip.setAccessible(true);
					}
					if (!mBottomRightStrip.isAccessible()) {
						mBottomRightStrip.setAccessible(true);
					}
					mBottomLeftStrip.set(tabWidget,
							getResources().getDrawable(R.drawable.no));
					mBottomRightStrip.set(tabWidget, getResources()
							.getDrawable(R.drawable.no));

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
				public void onTabChanged(String tabId) {
					for (int i = 0; i < tabWidget.getChildCount(); i++) {
						RelativeLayout tabView = (RelativeLayout) mTabHost
								.getTabWidget().getChildAt(i);
						ImageView img = (ImageView) tabWidget.getChildAt(i)
								.findViewById(android.R.id.icon);

						// if (mTabHost.getCurrentTab() == 1) {
						// Log.i("aaa", "----");
						// flag_hw = false;
						// handler.sendEmptyMessage(1);
						// } else {
						// flag_hw = true;
						// }

						if (mTabHost.getCurrentTab() == i) {
							homework.setMainItem(i);
							tabView.setBackgroundColor(res
									.getColor(R.color.white));
							switch (i) {
							case 0:
								homework.setNewsFlag(true);
								flag_hw = true;
								img.setImageResource(R.drawable.th1);
								break;
							case 1:
								Log.i("aaa", "------");
								homework.setNewsFlag(true);
								flag_hw = false;
								hw_num = 0;
								handler.sendEmptyMessage(1);
								img.setImageResource(R.drawable.th2);
								break;
							case 2:
								homework.setNewsFlag(false);
								flag_hw = true;
								homework.setLastcount(Size);
								Log.i("aaa", homework.getLastcount() + "<---");
								TextView textview = (TextView) tabhost
										.getTabWidget().getChildAt(2)
										.findViewById(android.R.id.title);
								textview.setPadding(27, 5, 5, 53);
								textview.setText("");
								num = "0";
								img.setImageResource(R.drawable.th3);
								break;
							case 3:
								homework.setNewsFlag(true);
								flag_hw = true;
								img.setImageResource(R.drawable.th4);
								break;
							}
						} else {
							tabView.setBackgroundColor(res
									.getColor(R.color.lvse));
							switch (i) {
							case 0:
								img.setImageResource(R.drawable.th1_1);
								break;
							case 1:
								if (flag_hw && hw_num > 0) {
									handler.sendEmptyMessage(1);
								} else {
									img.setImageResource(R.drawable.th2_2);
								}
								break;
							case 2:
								if (homework.isNewsFlag() && !num.equals("0")) {
									img.setImageResource(R.drawable.news);
								} else {
									img.setImageResource(R.drawable.th3_3);
								}
								break;
							case 3:
								img.setImageResource(R.drawable.th4_4);
								break;
							}
						}
						img.setPadding(0, 15, 0, 15);
					}
				}
			});
		}
	}

	public void showDownloadDialog() {
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(HomeWorkMainActivity.this);
		builder.setTitle("正在更新");
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater
				.from(HomeWorkMainActivity.this);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 现在文件
		downloadApk();
	}

	/**
	 * 下载文件线程
	 * 
	 * @author coolszy
	 * @date 2012-4-26
	 * @blog http://blog.92coding.com
	 */
	public class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath + "download";
					URL url = new URL(fileurl);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, filename);
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						handler.sendEmptyMessage(4);
						if (numread <= 0) {
							// 下载完成
							handler.sendEmptyMessage(5);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * 下载apk文件
	 */
	public void downloadApk() {
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, filename);
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		startActivity(i);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.exit(0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}