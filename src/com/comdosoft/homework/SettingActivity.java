package com.comdosoft.homework;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
import android.widget.ImageButton;
import android.widget.Toast;

import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;

public class SettingActivity extends Activity implements Urlinterface {

	// 设置 界面
	private ImageButton faceImage;
	private EditText nickname;//
	private EditText name; //
	private View layout;// 选择头像界面
	private String json = "";

	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";

	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private Bitmap bm = null;
	private String nameS = ""; //
	private String nicknameS = ""; //
	private String id = "8"; // 用户 id，，切记 不是 user_id
	private String user_id = "8"; // 用户 id，，切记 不是 user_id
	private String avatar_url = "/homework_system/avatars/students/2014-01/student_5.jpg"; // 用户头像

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

		layout = this.findViewById(R.id.set_photolayout); // 隐藏内容
		faceImage = (ImageButton) findViewById(R.id.set_touxiang);
		nickname = (EditText) findViewById(R.id.set_nickname);
		name = (EditText) findViewById(R.id.set_name);
		name.setText(nameS);
		nickname.setText(nicknameS);
		// if (HomeWorkTool.isConnect(getApplicationContext())) {

		if (avatar_url != null || avatar_url.length() != 0) { // 设置头像
			// HttpGet hg = new HttpGet(Urlinterface.IP
			// + avatar_url);//

			GetCSDNLogoTask task = new GetCSDNLogoTask();
			task.execute(Urlinterface.IP + avatar_url);//

			faceImage.setOnClickListener(listener);

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
				final String res = (String) msg.obj;
				if (res.length() != 0) {
					JSONObject array;

					try {
						array = new JSONObject(res);
						String status = array.getString("status");
						String notice = array.getString("notice");

						if ("success".equals(status)) {

							Toast.makeText(getApplicationContext(), notice, 0)
									.show();
							SharedPreferences preferences = getSharedPreferences(
									SHARED, Context.MODE_PRIVATE);
							Editor editor = preferences.edit();
							editor.putString("name", nameS);
							editor.putString("user_id", id);
							editor.putString("id", id);
							editor.putString("avatar_url", avatar_url);
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

					String nicknames = nickname.getText().toString();
					String names = name.getText().toString();
					MultipartEntity entity = new MultipartEntity();

					entity.addPart("student_id", new StringBody(id));
					File f = new File(Environment.getExternalStorageDirectory()
							+ "/1" + IMAGE_FILE_NAME);
					if (f.exists()) {
						entity.addPart("avatar", new FileBody(new File(
								Environment.getExternalStorageDirectory()
										+ "/1" + IMAGE_FILE_NAME)));
					} else {
						Drawable normal = getResources().getDrawable(
								R.drawable.moren);
						File file = new File(
								Environment.getExternalStorageDirectory()
										+ "/moren.jpg");

						if (file.exists()) {
							file.delete();

						}
						Bitmap bitmap = ((BitmapDrawable) normal).getBitmap();
						file.createNewFile();
						FileOutputStream stream = new FileOutputStream(file);
						ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
						bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
						byte[] buf = stream1.toByteArray(); // 将图片流以字符串形式存储下来
						// byte[] buf = s.getBytes();
						stream.write(buf);
						stream.close();

						entity.addPart("avatar", new FileBody(new File(
								Environment.getExternalStorageDirectory()
										+ "/moren.jpg")));

					}

					entity.addPart("nickname", new StringBody(nicknames));
					entity.addPart("name", new StringBody(names));

					json = HomeWorkTool.sendPhostimg(
							Urlinterface.MODIFY_PERSON_INFO, entity);
					Message msg = new Message();// 创建Message 对象
					msg.what = 0;
					msg.obj = json;
					mHandler.sendMessage(msg);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		thread.start();

	}

	public void changeClass(View v) {
		Intent intent = new Intent();
		intent.setClass(this, SwitchClassActivity.class);//
		startActivity(intent);

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
		Intent intent = new Intent();
		intent.setClass(this, LoginActivity.class);//
		startActivity(intent);
		HomeWorkMainActivity.instance.finish();

	}

	private View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			layout.setVisibility(View.VISIBLE);
		}
	};

	/**
	 * 拍照上传
	 */
	public void set_paizhaoshangchuan(View v) {

		layout.setVisibility(View.GONE);

		Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 判断存储卡是否可以用，可用进行存储
		if (HomeWorkTool.isHasSdcard()) {

			File file = new File(Environment.getExternalStorageDirectory()
					+ "/" + IMAGE_FILE_NAME);

			if (file.exists()) {
				file.delete();

			}
			file = new File(Environment.getExternalStorageDirectory() + "/"
					+ IMAGE_FILE_NAME);
			if (!file.exists()) {
				intentFromCapture
						.putExtra(MediaStore.EXTRA_OUTPUT, Uri
								.fromFile(new File(Environment
										.getExternalStorageDirectory(),
										IMAGE_FILE_NAME)));

			}

		}

		startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);

	}

	/**
	 * 从相册选择
	 */
	public void set_congxiangce(View v) {
		layout.setVisibility(View.GONE);

		Intent intentFromGallery = new Intent();
		intentFromGallery.setType("image/*"); // 设置文件类型
		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {

			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if (HomeWorkTool.isHasSdcard()) {
					File tempFile = new File(
							Environment.getExternalStorageDirectory() + "/"
									+ IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
							.show();
				}

				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 350);
		intent.putExtra("outputY", 400);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 2);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			faceImage.setImageDrawable(drawable);
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/1" + IMAGE_FILE_NAME);

			try {

				if (file.exists()) {
					file.delete();

				}

				file.createNewFile();
				FileOutputStream stream = new FileOutputStream(file);
				ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
				byte[] buf = stream1.toByteArray(); // 将图片流以字符串形式存储下来
				// byte[] buf = s.getBytes();
				stream.write(buf);
				stream.close();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
	}
}
