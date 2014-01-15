package com.comdosoft.homework;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SettingActivity extends Activity {

	//    设置          界面  
	private ImageButton faceImage;
	private EditText nickname;//
	private EditText name; //
	private View layout;//  选择头像界面
	
	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";

	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	
	
	private  String student_id="1";  //   用户  id
private String avatar_url="/homework_system/avatars/students/student_1.jpg";   //  用户头像
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
//		Intent intent = getIntent();// 
//		student_id= intent.getStringExtra("student_id");
//		avatar_url= intent.getStringExtra("avatar_url");
		layout = this.findViewById(R.id.set_photolayout); // 隐藏内容
		faceImage = (ImageButton) findViewById(R.id.set_touxiang);
		nickname = (EditText) findViewById(R.id.set_nickname);
		name = (EditText) findViewById(R.id.set_name);
		if (HomeWorkTool.isConnect(getApplicationContext())) {
			
			if (avatar_url!= null||avatar_url.length()!=0) { // 设置头像
				HttpClient hc = new DefaultHttpClient();

				HttpGet hg = new HttpGet(Urlinterface.IP+avatar_url);//
				
				try {
					HttpResponse hr = hc.execute(hg);
					Bitmap	bm = BitmapFactory.decodeStream(hr.getEntity().getContent());
					Drawable face_drawable = new BitmapDrawable(bm);
					faceImage.setBackgroundDrawable(face_drawable);
				} catch (Exception e) {

					
				}
		
			}}
		
		
		faceImage.setOnClickListener(listener);
	}

	
	public void saveUpdata(View v) {
	

		String nicknames = nickname.getText().toString();
		String names = name.getText().toString();

		//  此处调用方法上传到  服务器     （student_id，图片数据   ） ！！！！！！！！！！！
		MultipartEntity entity = new MultipartEntity(); 
		try {
			entity.addPart("student_id", new StringBody(student_id));
			File f = new File(Environment.getExternalStorageDirectory()
					+ "/1" + IMAGE_FILE_NAME);
			if (f.exists()) {
				entity.addPart("avatar", new FileBody(new File(Environment.getExternalStorageDirectory()
						+ "/1" + IMAGE_FILE_NAME)));
			}
			
			entity.addPart("nickname", new StringBody(nicknames));
			entity.addPart("name", new StringBody(names));
	
			String json = HomeWorkTool.sendPhostimg(Urlinterface.MODIFY_PERSON_INFO, entity);
			if (json.length()!=0) {
				JSONObject array;
				
					try {
						array = new JSONObject(json);
						String status = array.getString("status");
						String notice = array.getString("notice");
						
						if ("success".equals(status)) {
							
							Toast.makeText(getApplicationContext(), notice, 0).show();
						}else{
							Toast.makeText(getApplicationContext(), notice, 0).show();	
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

			}
//			Toast.makeText(getApplicationContext(), json, 0).show();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		                      
		                      
		
	}
	public void changeClass(View v) {
		
		
		
	}
	
	public void exit_user(View v) {
		
		
		
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
