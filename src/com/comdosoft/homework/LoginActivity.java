package com.comdosoft.homework;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

// 登录    马龙    2014年1月20日
public class LoginActivity extends Activity implements Urlinterface {

	private static final String SCOPE = "get_user_info";
	private static final String APP_ID = "101003848";
	private String mes;
	private String json;
	private String openid;
	private Tencent mTencent;
	private SharedPreferences sp;
	private ProgressDialog mPd;
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
				intent.putExtra("json", json);
				intent.setClass(getApplicationContext(),
						HomeWorkMainActivity.class);
				break;
			}
			startActivity(intent);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
		mPd = new ProgressDialog(LoginActivity.this);
		mPd.setMessage("正在登陆...");
		sp = getPreferences(0);

		if (sp.getString("user_id", "").equals("")
				&& sp.getString("class_id", "").equals("")) {
			onClickLogin();
		} else {
			Intent intent = new Intent(getApplicationContext(),
					HomeWorkMainActivity.class);
			startActivity(intent);
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
			Map<String, String> map = new HashMap<String, String>();
			map.put("open_id", openid);
			json = HomeWorkTool.doPost(QQ_LOGIN, map);
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

}
