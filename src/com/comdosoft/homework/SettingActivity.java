package com.comdosoft.homework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class SettingActivity extends Activity {

	//    设置          界面  
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		

		
	}

	
	public void HomeWorkIngActivity(View v) {
		
		Intent intent = new Intent(SettingActivity.this, HomeWorkIngActivity.class);
		startActivity(intent);
		
	}
	public void xinxiliu(View v) {
		
		Intent intent = new Intent(SettingActivity.this, Class_xinxiliu.class);
		startActivity(intent);
		
	}
	
	
	

}
