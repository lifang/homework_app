package com.comdosoft.homework;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.HomeWorkParams;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;

public class RegistrationActivity extends Activity implements Urlinterface {
	private ImageView faceImage;
	private EditText reg_nicheng;//
	private EditText reg_xingming; //
	private EditText reg_banjiyanzhengma;
	private ProgressDialog prodialog;
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
		File file = new File(Environment.getExternalStorageDirectory() + "/1"
				+ IMAGE_FILE_NAME);

		if (file.exists()) {
			file.delete();

		}
		// Date d = new Date();
		// open_id = d.toString();
		// 上面两句代码 用来获得不一样的 qq_uid，，，测试 用，，后期删除

		Intent intent = getIntent();//
		open_id = intent.getStringExtra("open_id"); // 获得上个页面传过来的 QQ openid

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
			Intent intentp = new Intent();
			intentp.setClass(RegistrationActivity.this, SettingPhoto.class);//
			startActivityForResult(intentp, 0);  
		}
	};


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
			
		
		switch (resultCode) {
		// 如果是直接从相册获取
		 case RESULT_OK:  

	            Bundle bundle = data.getExtras();  
	            String uri = bundle.getString("uri"); 
	            BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 7;//7就代表容量变为以前容量的1/7
	            Bitmap bm = BitmapFactory.decodeFile(uri, options);
				
				faceImage.setBackgroundDrawable(new BitmapDrawable(bm)); 
	            
	            
	            break;  
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
		
	}

	Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				prodialog.dismiss();
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
							editor.putString("user_id", user_id);
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

							Intent it = new Intent(RegistrationActivity.this,
									ErrorDisplay.class);
							it.putExtra("notice", notice);
							startActivity(it);

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 7:
				Toast.makeText(getApplicationContext(),
						HomeWorkParams.INTERNET, Toast.LENGTH_SHORT).show();
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
		String nicknames = nickname.replaceAll(" ", "");
		String names = name.replaceAll(" ", "");
		String verification_codes = verification_code.replaceAll(" ", "");
		
		if (nickname.length() == 0 || name.length() == 0
				|| verification_code.length() == 0|| nicknames.equals("")|| names.equals("")|| verification_codes.equals("")) {
			Intent it = new Intent(RegistrationActivity.this,
					ErrorDisplay.class);
			it.putExtra("notice", "编辑框不能为空");
			startActivity(it);

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

						entity.addPart("nickname", new StringBody(nickname,
								Charset.forName("UTF-8")));
						entity.addPart("name",
								new StringBody(name, Charset.forName("UTF-8")));
						entity.addPart("verification_code", new StringBody(
								verification_code));

						json = HomeWorkTool.sendPhostimg(
								Urlinterface.RECORD_PERSON_INFO, entity);
						Message msg = new Message();// 创建Message 对象
						msg.what = 0;
						msg.obj = json;
						mHandler.sendMessage(msg);

					} catch (Exception e) {
						mHandler.sendEmptyMessage(7);
					}
				}
			};

			if (HomeWorkTool.isConnect(RegistrationActivity.this)) {
				prodialog = new ProgressDialog(RegistrationActivity.this);
				prodialog.setMessage(HomeWorkParams.PD_REG);
				prodialog.setCanceledOnTouchOutside(false);
				prodialog.show();
				thread.start();
			} else {
				Toast.makeText(getApplicationContext(),
						HomeWorkParams.INTERNET, 0).show();
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.exit(0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
