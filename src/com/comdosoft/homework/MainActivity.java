package com.comdosoft.homework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		this.finish();
		
	}

	
	public void HomeWorkIngActivity(View v) {
		
		Intent intent = new Intent(MainActivity.this, HomeWorkMainActivity.class);
		startActivity(intent);
		
	}
	
	
	public void xinxiliu(View v) {
		
		Intent intent = new Intent(MainActivity.this, Classxinxiliu.class);
		startActivity(intent);
		
		
	}
	public void RegistrationActivity(View v) {
		
		Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
		startActivity(intent);
		
	}
	
	public void SettingActivity(View v) {
		
		Intent intent = new Intent(MainActivity.this, SettingActivity.class);
		startActivity(intent);
		
	}

}
