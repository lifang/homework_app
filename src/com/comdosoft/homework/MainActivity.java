package com.comdosoft.homework;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.comdosoft.homework.tools.HomeWorkTool;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private static final String SCOPE = "get_user_info";
	private static final String APP_ID = "101003848";
	private String mes;
	private String json;
	private Tencent mTencent;
	private Button btn3;
	private String openid;
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
				intent.setClass(getApplicationContext(),
						HomeWorkIngActivity.class);
				break;
			case 2:
				intent.putExtra("json", json);
				intent.setClass(getApplicationContext(),
						HomeWorkIngActivity.class);
				break;
			}
			startActivity(intent);
		}

	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.qq_login).setOnClickListener(this);
		mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
		mPd = new ProgressDialog(MainActivity.this);
		mPd.setMessage("正在登陆...");

		btn3 = (Button) findViewById(R.id.button3);
		// this.finish();
		btn3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						SwitchClassActivity.class);
				startActivity(intent);
			}
		});
	}

	public void HomeWorkIngActivity(View v) {

		Intent intent = new Intent(MainActivity.this,
				HomeWorkMainActivity.class);
		startActivity(intent);

	}

	public void xinxiliu(View v) {

		Intent intent = new Intent(MainActivity.this, Classxinxiliu.class);
		startActivity(intent);

	}

	public void RegistrationActivity(View v) {
		Intent intent = new Intent(MainActivity.this,
				RegistrationActivity.class);
		startActivity(intent);
	}

	public void SettingActivity(View v) {
		Intent intent = new Intent(MainActivity.this, SettingActivity.class);
		startActivity(intent);
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

	private void onClickLogin() {
		if (!mTencent.isSessionValid()) {
			IUiListener listener = new BaseUiListener();
			mTencent.login(this, SCOPE, listener);
		} else {
			mTencent.logout(this);
		}
	}

	@Override
	public void onClick(View v) {
		onClickLogin();
	}

	class MyThread extends Thread {
		@Override
		public void run() {
			super.run();
			Map<String, String> map = new HashMap<String, String>();
			map.put("open_id", openid);
			json = HomeWorkTool.doPost(
					"http://192.168.0.127:3004/api/students/login", map);
			analyzeJson(json);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mTencent.onActivityResult(requestCode, resultCode, data);
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
