package com.comdosoft.homework;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.HomeWorkParams;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;
import com.comdosoft.homework.Classxinxiliu;

public class RegistrationActivity extends Activity implements Urlinterface {
	private ImageView faceImage;
	private EditText reg_nicheng;//
	private EditText reg_xingming; //
	private EditText reg_banjiyanzhengma;
	private View layout;// 选择头像界面
	private View layout2;// 班级验证码错误 返回界面
	private String tp; // 头像资源
	private HomeWork hw;
	private String open_id = "asfds"; // QQ 的 open id
	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";

	/* 请求码 */

	private String json = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_registration);
		hw = (HomeWork) getApplication();
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/1" + IMAGE_FILE_NAME);

			if (file.exists()) {
				file.delete();

			}
		// Date d = new Date();
		// open_id = d.toString();
		// 上面两句代码 用来获得不一样的 qq_uid，，，测试 用，，后期删除

		Intent intent = getIntent();//
		open_id = intent.getStringExtra("open_id"); // 获得上个页面传过来的 QQ openid

		layout = this.findViewById(R.id.reg_photolayout); // 隐藏内容
		layout2 = this.findViewById(R.id.reg_photolayout2); // 隐藏内容
		faceImage = (ImageView) findViewById(R.id.reg_touxiang);
		reg_nicheng = (EditText) findViewById(R.id.reg_nicheng);
		reg_xingming = (EditText) findViewById(R.id.reg_xingming);
		reg_banjiyanzhengma = (EditText) findViewById(R.id.reg_banjiyanzhengma);
		// 设置事件监听
		faceImage.setOnClickListener(listener);

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
	public void reg_paizhaoshangchuan(View v) {

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

		startActivityForResult(intentFromCapture, 2);

	}

	/**
	 * 从相册选择
	 */
	public void reg_congxiangce(View v) {
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
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						"image/*");
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
						+ "/"
						+ IMAGE_FILE_NAME);
				startPhotoZoom(Uri.fromFile(temp));
			} else {
				Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
						.show();
			}
			
			break;
		// 取得裁剪后的图片
		case 3:
			/**
			 * 非空判断大家一定要验证，如果不验证的话，
			 * 在剪裁之后如果发现不满意，要重新裁剪，丢弃
			 * 当前功能时，会报NullException，小马只
			 * 在这个地方加下，大家可以根据不同情况在合适的
			 * 地方做判断处理类似情况
			 * 
			 */
			if(data != null){
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
				faceImage.setBackgroundDrawable(drawable);
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				final String res = (String) msg.obj;
				if (json.length() != 0) {
					JSONObject array;

					try {
						array = new JSONObject(json);
						String status = array.getString("status");
						String notice = array.getString("notice");

						if ("success".equals(status)) {
							JSONObject student = array.getJSONObject("student"); // 获得学生的信息
							String id = student.getString("id");
							String user_id = student.getString("user_id");
							Log.i("linshi", user_id + "------");
							String avatar_url = student.getString("avatar_url"); // 获取本人头像昂所有在地址
							String name = student.getString("name");
							String nick_name = student.getString("nickname");
							// service.save(id, user_id, nick_name, nick_name,
							// avatar_url);
							JSONObject class1 = array.getJSONObject("class"); // 或得班级信息
							// 获取class_name

							String school_class_id = class1.getString("id");
							Log.i(tag, user_id + "------" + school_class_id);
							SharedPreferences preferences = getSharedPreferences(
									SHARED, Context.MODE_PRIVATE);
							Editor editor = preferences.edit();
							editor.putString("name", name);
							editor.putString("user_id", id);
							editor.putString("id", id);
							editor.putString("avatar_url", avatar_url);
							editor.putString("nickname", nick_name);
							editor.putString("school_class_id", school_class_id);
							editor.commit();
							hw.setClass_id(Integer.parseInt(school_class_id));
							hw.setUser_id(Integer.parseInt(user_id));

							Toast.makeText(getApplicationContext(), notice, 0)
									.show();
							// 添加页面跳转 跳到班级页面
							Intent intent = new Intent();
							intent.setClass(
									RegistrationActivity.this,
									com.comdosoft.homework.HomeWorkMainActivity.class);//
							startActivity(intent);
							RegistrationActivity.this.finish();

						} else {

							layout2.setVisibility(View.VISIBLE);
							TextView reg_error = (TextView) layout2
									.findViewById(R.id.regerror);
							reg_error.setText(notice);
							// Toast.makeText(getApplicationContext(), notice,
							// 0).show();

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

	// 确认按钮 点击时触发的方法
	public String reg_queren(View v) throws Exception {

		String nickname = reg_nicheng.getText().toString();
		String name = reg_xingming.getText().toString();
		String verification_code = reg_banjiyanzhengma.getText().toString();
		if (nickname.length() == 0 || name.length() == 0
				|| verification_code.length() == 0) {
			layout2.setVisibility(View.VISIBLE);
			TextView reg_error = (TextView) layout2.findViewById(R.id.regerror);
			reg_error.setText(R.string.edit_null);
		} else {

			Thread thread = new Thread() {
				public void run() {
					try {

						String nickname = reg_nicheng.getText().toString();
						String name = reg_xingming.getText().toString();
						String verification_code = reg_banjiyanzhengma
								.getText().toString();
						MultipartEntity entity = new MultipartEntity();
						entity.addPart("open_id", new StringBody(open_id));
						File f = new File(
								Environment.getExternalStorageDirectory()
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
							Bitmap bitmap = ((BitmapDrawable) normal)
									.getBitmap();
							file.createNewFile();
							FileOutputStream stream = new FileOutputStream(file);
							ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
							bitmap.compress(Bitmap.CompressFormat.JPEG, 60,
									stream);
							byte[] buf = stream1.toByteArray(); // 将图片流以字符串形式存储下来
							// byte[] buf = s.getBytes();
							stream.write(buf);
							stream.close();

							entity.addPart("avatar", new FileBody(new File(
									Environment.getExternalStorageDirectory()
											+ "/moren.jpg")));

						}

						entity.addPart("nickname", new StringBody(nickname));
						entity.addPart("name", new StringBody(name));
						entity.addPart("verification_code", new StringBody(
								verification_code));

						json = HomeWorkTool.sendPhostimg(
								Urlinterface.RECORD_PERSON_INFO, entity);
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
			
			if (HomeWorkTool.isConnect(RegistrationActivity.this)) {
				thread.start();
			}
			else {
				Toast.makeText(getApplicationContext(), HomeWorkParams.INTERNET, 0).show();
			}
			

		}
		return null;
	}

	public String inStream2String(InputStream is) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len = -1;
		while ((len = is.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}
		return new String(baos.toByteArray());
	}

	// 班级验证码错误 时触发的方法
	public String reg_fanhui(View v) throws Exception {

		layout2.setVisibility(View.GONE);
		return null;
	}

}
