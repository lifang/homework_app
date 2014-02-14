package com.comdosoft.homework;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.comdosoft.homework.tools.HomeWorkParams;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;

public class SettingActivity extends Activity implements Urlinterface {

	// 设置 界面
	private ImageView faceImage;
	private EditText nickname;//
	private EditText name; //
	private View layout;// 选择头像界面
	private String json = "";

	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";

	/* 请求码 */
	private ProgressDialog prodialog;
	private Bitmap bm = null;
	private String nameS = ""; //
	private String nicknameS = ""; //
	private String id = "8"; // 用户 id，，切记 不是 user_id
	private String user_id = "8"; // 用户 id，，切记 不是 user_id
	private String avatar_url = ""; // 用户头像

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		SharedPreferences preferences = getSharedPreferences(SHARED,
				Context.MODE_PRIVATE);

		user_id = preferences.getString("user_id", null);
		if (user_id != null) {
			id = preferences.getString("id", null);
			avatar_url = preferences.getString("avatar_url", "");
			nicknameS = preferences.getString("nickname", "");
			nameS = preferences.getString("name", "");

		}
		File file = new File(Environment.getExternalStorageDirectory() + "/1"
				+ IMAGE_FILE_NAME);

		if (file.exists()) {
			file.delete();

		}

	
		faceImage = (ImageView) findViewById(R.id.set_touxiang);
		faceImage.setOnClickListener(listener);

		nickname = (EditText) findViewById(R.id.set_nickname);
		name = (EditText) findViewById(R.id.set_name);
		name.setText(nameS);
		nickname.setText(nicknameS);
		if (HomeWorkTool.isConnect(getApplicationContext())) {

			if (avatar_url != null || avatar_url.length() != 0) { // 设置头像

				GetCSDNLogoTask task = new GetCSDNLogoTask();
				task.execute(Urlinterface.IP + avatar_url);//

				
			}
		} else {
			Toast.makeText(getApplicationContext(), HomeWorkParams.INTERNET, 0)
					.show();
		}
	}

	class GetCSDNLogoTask extends AsyncTask<String, Integer, Drawable> {

		@Override
		protected Drawable doInBackground(String... params) {

			HttpClient hc = new DefaultHttpClient();

			HttpGet hg = new HttpGet(params[0]);//
			final Drawable face_drawable;
			try {
				HttpResponse hr = hc.execute(hg);
				Bitmap bm = BitmapFactory.decodeStream(hr.getEntity()
						.getContent());
				face_drawable = new BitmapDrawable(bm);

			} catch (Exception e) {

				return null;
			}

			return face_drawable;
		}

		protected void onPostExecute(Drawable face_drawable) {
			if (face_drawable != null) {

				faceImage.setBackgroundDrawable(face_drawable);
			} else {

			}
		}

	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				prodialog.dismiss();
				final String res = (String) msg.obj;
				if (res.length() != 0) {
					JSONObject array;

					try {
						array = new JSONObject(res);
						Boolean status = array.getBoolean("status");
						String notice = array.getString("notice");

						if (status == true) {

							Toast.makeText(getApplicationContext(), notice, 0)
									.show();
							SharedPreferences preferences = getSharedPreferences(
									SHARED, Context.MODE_PRIVATE);
							Editor editor = preferences.edit();
							editor.putString("name", nameS);
							editor.putString("user_id", id);
							editor.putString("id", id);
							editor.putString("nickname", nicknameS);

							editor.commit();
							name.setText(nameS);
							nickname.setText(nicknameS);

						} else {
							Toast.makeText(getApplicationContext(), notice, 0)
									.show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				break;
			case 7:
				prodialog.dismiss();
				Toast.makeText(getApplicationContext(), HomeWorkParams.INTERNET,
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;

			}
		}
	};

	// 保存设置
	public void saveUpdata(View v) {

		Thread thread = new Thread() {
			public void run() {
				try {

					nicknameS = nickname.getText().toString();
					nameS = name.getText().toString();
					MultipartEntity entity = new MultipartEntity();

					entity.addPart("student_id", new StringBody(id));
					File f = new File(Environment.getExternalStorageDirectory()
							+ "/1" + IMAGE_FILE_NAME);
					if (f.exists()) {
						entity.addPart("avatar", new FileBody(new File(
								Environment.getExternalStorageDirectory()
										+ "/1" + IMAGE_FILE_NAME)));
					}

					entity.addPart("nickname", new StringBody(nicknameS,
							Charset.forName("UTF-8")));
					entity.addPart("name",
							new StringBody(nameS, Charset.forName("UTF-8")));

					json = HomeWorkTool.sendPhostimg(
							Urlinterface.MODIFY_PERSON_INFO, entity);
					Message msg = new Message();// 创建Message 对象
					msg.what = 0;
					msg.obj = json;
					mHandler.sendMessage(msg);

				} catch (Exception e) {
					mHandler.sendEmptyMessage(7);
				}
			}
		};

		if (nickname.length() == 0 || name.length() == 0) {
			Toast.makeText(getApplicationContext(),
					HomeWorkParams.NAME_NICKNAME_ISNULL, 0).show();
		} else {

			if (HomeWorkTool.isConnect(SettingActivity.this)) {
				prodialog = new ProgressDialog(SettingActivity.this);
				prodialog.setMessage("正在提交数据...");
				prodialog.show();
				thread.start();
			} else {
				Toast.makeText(getApplicationContext(),
						HomeWorkParams.INTERNET, 0).show();
			}

		}
	}

	public void changeClass(View v) {

		if (HomeWorkTool.isConnect(SettingActivity.this)) {
			Intent intent = new Intent();
			intent.setClass(this, SwitchClassActivity.class);//
			startActivity(intent);
		} else {
			Toast.makeText(getApplicationContext(), HomeWorkParams.INTERNET, 0)
					.show();
		}
	}

	public void exit_user(View v) {

		// 跳转到 登陆界面 ， 清空本地参数

		SharedPreferences preferences = getSharedPreferences(SHARED,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("user_id", "");
		editor.putString("school_class_id", "");
		editor.putString("id", "");
		editor.commit();
		if (HomeWorkTool.isConnect(getApplicationContext())) {
			Intent intent = new Intent();
			intent.setClass(this, LoginActivity.class);//
			startActivity(intent);
			HomeWorkMainActivity.instance.finish();
		}else{
			System.exit(0);
		}

	}

	private View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intentp = new Intent();
			intentp.setClass(SettingActivity.this, SettingPhoto.class);//
			startActivityForResult(intentp, 0);  
		}
	};


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
			
		
		switch (resultCode) {
		// 如果是直接从相册获取
		 case RESULT_OK:  
			 
	            Bundle bundle = data.getExtras();  
	            String info = bundle.getString("info"); 
	            BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 7;//7就代表容量变为以前容量的1/7
	            Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()
						+ "/1" + IMAGE_FILE_NAME, options);
				
				faceImage.setBackgroundDrawable(new BitmapDrawable(bm)); 
	            
	            
	            break;  
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
		
	}

	
	
}
