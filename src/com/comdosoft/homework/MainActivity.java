package com.comdosoft.homework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		this.finish();
		
	}

	
	public void HomeWorkIngActivity(View v) {
		
		Intent intent = new Intent(MainActivity.this, HomeWorkIngActivity.class);
		startActivity(intent);
		
	}
	public void xinxiliu(View v) {
		
		Intent intent = new Intent(MainActivity.this, class_xinxiliu.class);
		startActivity(intent);
		
	}
	
	
	

}
