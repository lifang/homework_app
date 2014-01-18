package com.comdosoft.homework;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button btn3;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn3=(Button) findViewById(R.id.button3);
		//		this.finish();
		btn3.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,SwitchClassActivity.class);
				startActivity(intent);
			}
			
		});
	}


	public void HomeWorkIngActivity(View v) {

		Intent intent = new Intent(MainActivity.this, HomeWorkIngActivity.class);
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
