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

		layout = this.findViewById(R.id.set_photolayout); // 隐藏内容
		faceImage = (ImageView) findViewById(R.id.set_touxiang);
		nickname = (EditText) findViewById(R.id.set_nickname);
		name = (EditText) findViewById(R.id.set_name);
		name.setText(nameS);
		nickname.setText(nicknameS);
		if (HomeWorkTool.isConnect(getApplicationContext())) {

			if (avatar_url != null || avatar_url.length() != 0) { // 设置头像

				GetCSDNLogoTask task = new GetCSDNLogoTask();
				task.execute(Urlinterface.IP + avatar_url);//

				faceImage.setOnClickListener(listener);

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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		if (nickname.length() == 0 || name.length() == 0) {
			Toast.makeText(getApplicationContext(),
					HomeWorkParams.NAME_NICKNAME_ISNULL, 0).show();
		} else {

			if (HomeWorkTool.isConnect(SettingActivity.this)) {
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
		try {

			Intent intentFromCapture = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
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
					intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
							.fromFile(new File(Environment
									.getExternalStorageDirectory(),
									IMAGE_FILE_NAME)));

				}

			}

			startActivityForResult(intentFromCapture, 2);
		} catch (Exception e) {

			Toast.makeText(getApplicationContext(), HomeWorkParams.CAPTURE, 0)
					.show();
		}

	}

	/**
	 * 从相册选择
	 */
	public void set_congxiangce(View v) {
		layout.setVisibility(View.GONE);

		Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);

		/**
		 * 下面这句话，与其它方式写是一样的效果，如果：
		 * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		 * intent.setType(""image/*");设置数据类型
		 * 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
		 * 这个地方小马有个疑问，希望高手解答下：就是这个数据URI与类型为什么要分两种形式来写呀？有什么区别？
		 */
		intentFromGallery.setDataAndType(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(intentFromGallery, 1);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 如果是直接从相册获取
		case 1:
			startPhotoZoom(data.getData());
			break;
		// 如果是调用相机拍照时
		case 2:

			if (HomeWorkTool.isHasSdcard()) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/" + IMAGE_FILE_NAME);
				startPhotoZoom(Uri.fromFile(temp));
			} else {
				Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
						.show();
			}

			break;
		// 取得裁剪后的图片
		case 3:
			/**
			 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
			 * 当前功能时，会报NullException，小马只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
			 * 
			 */
			if (data != null) {
				getImageToView(data);
			}
			break;
		default:
			break;

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
		intent.putExtra("outputX", 176);
		intent.putExtra("outputY", 186);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
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
			faceImage.setBackgroundDrawable(drawable);
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
