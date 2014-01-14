package com.comdosoft.homework;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;

public class RegistrationActivity extends Activity {
	private ImageButton faceImage;
	private EditText reg_nicheng;//
	private EditText reg_xingming; //
	private EditText reg_banjiyanzhengma;
	private View layout;//  选择头像界面
	private View layout2;//  班级验证码错误  返回界面
	private String tp; // 头像资源
	
	private String qq_uid;  //   QQ  的   open  id   

	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";

	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_registration);
		
		
//		Intent intent = getIntent();// 
//		qq_uid = intent.getStringExtra("qq_uid");   // 获得上个页面传过来的   QQ  openid

		layout = this.findViewById(R.id.photolayout); // 隐藏内容
		layout2 = this.findViewById(R.id.photolayout2); // 隐藏内容
		faceImage = (ImageButton) findViewById(R.id.reg_touxiang);
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
	public void sel_paizhaoshangchuan(View v) {
		
		layout.setVisibility(View.GONE);

		Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 判断存储卡是否可以用，可用进行存储
		if (HomeWorkTool.isHasSdcard()) {

		
				File file = new File(Environment.getExternalStorageDirectory()
						+ "/" + IMAGE_FILE_NAME);

					if (file.exists()) {
						file.delete();

					}
					 file = new File(Environment.getExternalStorageDirectory()
							+ "/" + IMAGE_FILE_NAME);
						if (!file.exists()) {
							intentFromCapture.putExtra(
									MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(new File(Environment
										.getExternalStorageDirectory(),
											IMAGE_FILE_NAME)));

			}

		}

		startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);

	}
	/**
	 * 从相册选择
	 */
	public void sel_congxiangce(View v) {
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
							Environment.getExternalStorageDirectory()+"/"
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
		intent.putExtra("outputX", 176);
		intent.putExtra("outputY", 186);
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
				faceImage.setImageDrawable(drawable);
			} catch (IOException e) {

				e.printStackTrace();
			}
		
		}
	}
	
	
	//   确认按钮 点击时触发的方法
	public String reg_queren(View v) throws Exception {
		
		String nickname = reg_nicheng.getText().toString();
		String name = reg_xingming.getText().toString();
		String verification_code = reg_banjiyanzhengma.getText().toString();
		if(nickname.length()==0||name.length()==0||verification_code.length()==0) {
			layout2.setVisibility(View.VISIBLE);
			TextView reg_error = (TextView) layout2.findViewById(R.id.regerror);
			reg_error.setText(R.string.edit_null);
		}else {
			MultipartEntity entity = new MultipartEntity();
			
			try {
				entity.addPart("qq_uid", new StringBody(qq_uid));
				File f = new File(Environment.getExternalStorageDirectory()
						+ "/1" + IMAGE_FILE_NAME);
				if (f.exists()) {
					entity.addPart("avatar", new FileBody(new File(Environment.getExternalStorageDirectory()
							+ "/1" + IMAGE_FILE_NAME)));
				}
				entity.addPart("nickname", new StringBody(nickname));
				entity.addPart("name", new StringBody(name));
				entity.addPart("verification_code", new StringBody(verification_code));
				
				String json = HomeWorkTool.sendPhostimg(Urlinterface.RECORD_PERSON_INFO, entity);
				if (json.length()!=0) {
					JSONObject array;
					
						try {
							array = new JSONObject(json);
							String status = array.getString("status");
							String notice = array.getString("notice");
							String avatar_url = array.getString("avatar_url");
							Log.i("aa", avatar_url);
							if ("success".equals(status)) {
								Toast.makeText(getApplicationContext(), notice, 0).show();
			
							}else{
							
								layout2.setVisibility(View.VISIBLE);
								TextView reg_error = (TextView) layout2.findViewById(R.id.regerror);
								reg_error.setText(notice);
								
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
				}
//				Toast.makeText(getApplicationContext(), json, 0).show();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	
		return null;
	}

	
//  班级验证码错误   时触发的方法
	public String reg_fanhui(View v) throws Exception {
		
		
		layout2.setVisibility(View.GONE);
		return null;
	}

}
