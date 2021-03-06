package com.comdosoft.homework;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

// 登录    马龙    2014年2月12日
public class LoginActivity extends Activity implements OnClickListener,
		Urlinterface {

	private static final String SCOPE = "get_user_info";
	private static final String APP_ID = "101003848";
	private String mes;
	private String json;
	private String openid;
	private Tencent mTencent;
	private SharedPreferences sp;
	private ProgressDialog mPd;
	private HomeWork homework;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mPd.dismiss();
			Toast.makeText(getApplicationContext(), mes, 0).show();
			Intent intent = new Intent();
			switch (msg.what) {
			case 1:
				intent.putExtra("open_id", openid);
				intent.setClass(getApplicationContext(),
						RegistrationActivity.class);
				break;
			case 2:
				LoginActivity.this.finish();
				intent.setClass(getApplicationContext(),
						Appstart.class);
				break;
			}
			LoginActivity.this.finish();
			startActivity(intent);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		findViewById(R.id.qq_login).setOnClickListener(this);
		homework = (HomeWork) getApplication();
		mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
		mPd = new ProgressDialog(LoginActivity.this);
		mPd.setMessage("正在登录...");
		sp = getSharedPreferences(SHARED, MODE_PRIVATE);
		if (!sp.getString("user_id", "").equals("")
				&& !sp.getString("school_class_id", "").equals("")) {
			LoginActivity.this.finish();
			Intent intent = new Intent(getApplicationContext(),
					Appstart.class);
			startActivity(intent);
			// onClickLogin();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mTencent.onActivityResult(requestCode, resultCode, data);
	}

	private void onClickLogin() {
		if (!mTencent.isSessionValid()) {
			IUiListener listener = new BaseUiListener();
			mTencent.login(this, SCOPE, listener);
		} else {
			mTencent.logout(this);
		}
	}

	public void analyzeJson(String json) {
		try {
			JSONObject jo = new JSONObject(json);
			String status = jo.getString("status");
			mes = jo.getString("notice");
			if (status.equals("error")) {
				mHandler.sendEmptyMessage(1);
			} else {
				JSONObject student = jo.getJSONObject("student"); // 获得学生的信息
				String id = student.getString("id");
				String user_id = student.getString("user_id");
				Log.i("linshi", user_id + "------");
				String avatar_url = student.getString("avatar_url"); // 获取本人头像昂所有在地址
				String name = student.getString("name");
				String nick_name = student.getString("nickname");
				JSONObject class1 = jo.getJSONObject("class"); // 或得班级信息

				String school_class_id = class1.getString("id");
				Log.i("Ax", user_id + "----------------" + school_class_id);
				SharedPreferences preferences = getSharedPreferences(SHARED,
						Context.MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.putString("name", name);
				Log.i("aaa", user_id + "----------------" + name);
				editor.putString("user_id", user_id);
				editor.putString("id", id);
				editor.putString("avatar_url", avatar_url);
				editor.putString("nickname", nick_name);
				editor.putString("school_class_id", school_class_id);
				editor.commit();
				homework.setClass_id(Integer.parseInt(school_class_id));
				homework.setUser_id(Integer.parseInt(user_id));
				mHandler.sendEmptyMessage(2);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	class MyThread extends Thread {
		@Override
		public void run() {
			super.run();
			Log.i("Ax", "aaaaaa");
			Map<String, String> map = new HashMap<String, String>();
			map.put("open_id", openid);
			json = HomeWorkTool.doPost(QQ_LOGIN, map);
			Log.i("Ax", "json---" + json);
			analyzeJson(json);
		}
	}

	private class BaseUiListener implements IUiListener {
		@Override
		public void onComplete(JSONObject response) {
			doComplete(response);
		}

		protected void doComplete(JSONObject values) {
			try {
				Log.i("Ax", "bbbbb");
				mPd.show();
				openid = values.getString("openid");
				new MyThread().start();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onError(UiError e) {
		}

		@Override
		public void onCancel() {
		}

	}

	@Override
	public void onClick(View v) {
		onClickLogin();
	}

	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.exit(0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
