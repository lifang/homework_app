package com.comdosoft.homework;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
<<<<<<< HEAD
import java.util.Date;
=======
>>>>>>> a1e393cd2a159b0c380da738a64c97d771c314c1

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
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
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
	
	private String qq_uid="asfds";  //   QQ  的   open  id   

	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";

	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
private String json="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_registration);
		
		
		Date d = new Date();
		qq_uid = d.toString();
		//  上面两句代码 用来获得不一样的   qq_uid，，，测试 用，，后期删除
		
		
		
//		Intent intent = getIntent();// 
//		qq_uid = intent.getStringExtra("qq_uid");   // 获得上个页面传过来的   QQ  openid

		layout = this.findViewById(R.id.reg_photolayout); // 隐藏内容
		layout2 = this.findViewById(R.id.reg_photolayout2); // 隐藏内容
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
	public void reg_congxiangce(View v) {
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
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:
					 final String res =  (String) msg.obj;
					    if (json.length()!=0) {
							JSONObject array;
							
								try {
									array = new JSONObject(json);
									String status = array.getString("status");
									String notice = array.getString("notice");
									
									if ("success".equals(status)) {
										
									
//										Toast.makeText(getApplicationContext(), notice, 0).show();
										//  添加页面跳转   跳到班级页面
										  Intent intent = new Intent();
										intent.putExtra("json",json);
											intent.setClass(RegistrationActivity.this,com.comdosoft.homework.Classxinxiliu.class);//
											startActivity(intent);
											RegistrationActivity.this.finish();
										
										
									}else{
									
										layout2.setVisibility(View.VISIBLE);
										TextView reg_error = (TextView) layout2.findViewById(R.id.regerror);
										reg_error.setText(notice);
//										Toast.makeText(getApplicationContext(), notice, 0).show();
										
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
			
			Thread thread=new Thread()
			{
				public void run()
				{
					try {

						String nickname = reg_nicheng.getText().toString();
						String name = reg_xingming.getText().toString();
						String verification_code = reg_banjiyanzhengma.getText().toString();
						MultipartEntity entity = new MultipartEntity(); 
						entity.addPart("qq_uid", new StringBody(qq_uid));
						File f = new File(Environment.getExternalStorageDirectory()
								+ "/1" + IMAGE_FILE_NAME);
						if (f.exists()) {
							entity.addPart("avatar", new FileBody(new File(Environment.getExternalStorageDirectory()
									+ "/1" + IMAGE_FILE_NAME)));
						}else {
							Drawable normal = getResources().getDrawable(R.drawable.moren); 
							File file = new File(Environment.getExternalStorageDirectory()
									+ "/moren.jpg");

								if (file.exists()) {
									file.delete();

								}
								Bitmap bitmap = ((BitmapDrawable)normal).getBitmap();  
								file.createNewFile();
								FileOutputStream stream = new FileOutputStream(file);
								ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
								bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
								byte[] buf = stream1.toByteArray(); // 将图片流以字符串形式存储下来
								// byte[] buf = s.getBytes();
								stream.write(buf);
								stream.close();
							
						
							entity.addPart("avatar", new FileBody(new File(Environment.getExternalStorageDirectory()
									+ "/moren.jpg")));
							
						}

						entity.addPart("nickname", new StringBody(nickname));
						entity.addPart("name", new StringBody(name));
						entity.addPart("verification_code", new StringBody(verification_code));
						
					 json = HomeWorkTool.sendPhostimg(Urlinterface.RECORD_PERSON_INFO, entity);
					 Message msg = new Message();//  创建Message 对象
						msg.what =0 ;
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
	
//  班级验证码错误   时触发的方法
	public String reg_fanhui(View v) throws Exception {
		
		
		layout2.setVisibility(View.GONE);
		return null;
	}

}
