package com.comdosoft.homework;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.comdosoft.homework.tools.HomeWorkTool;

public class RegistrationActivity extends Activity {
	private ImageButton faceImage;
	private EditText reg_nicheng;//
	private EditText reg_xingming; //
	private EditText reg_banjiyanzhengma;
	private View layout;
	private String tp; // 头像资源
	
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
		layout = this.findViewById(R.id.photolayout); // 隐藏内容
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

		Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 判断存储卡是否可以用，可用进行存储
		if (HomeWorkTool.isHasSdcard()) {

			intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
					.fromFile(new File(Environment
							.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
		}

		startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);

	}
	/**
	 * 从相册选择
	 */
	public void sel_congxiangce(View v) {
		
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
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
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
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			 photo.compress(Bitmap.CompressFormat.JPEG, 60, stream); 
			 byte[] b = stream.toByteArray(); // 将图片流以字符串形式存储下来
			  
			  tp = new String(b);
		}
	}

	
	//   确认按钮 点击时触发的方法
	public String reg_queren(View v) throws Exception {
		
		String nicheng = reg_nicheng.getText().toString();
		String xingming = reg_xingming.getText().toString();
		String banjiyanzhengma = reg_banjiyanzhengma.getText().toString();
		if (tp!=null) {
			
			//  将数据传给服务器
			
		}
		 
		

		Toast.makeText(getApplicationContext(), "方法没写", 1).show();
		return null;
	}

}
