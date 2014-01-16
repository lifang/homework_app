package com.comdosoft.homework;

import java.util.ArrayList;
import java.util.List;

import com.comdosoft.homework.adapter.SwitchClassAdapter;
import com.comdosoft.homework.pojo.ClassPojo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SwitchClassActivity extends Activity
{
	private EditText SwitchClass_Et;
	private ListView switchclassLv;
	private List<ClassPojo> classList=new ArrayList<ClassPojo>();
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.switchclass);
		classList.add(new ClassPojo(1,"123"));
		classList.add(new ClassPojo(2,"123"));
		classList.add(new ClassPojo(3,"123"));
		classList.add(new ClassPojo(4,"123"));
		classList.add(new ClassPojo(5,"123"));
		SwitchClass_Et=(EditText) findViewById(R.id.SwitchClass_Et);
		SwitchClass_Et.clearFocus();
		switchclassLv=(ListView) findViewById(R.id.switchclassLv);
		
		switchclassLv.setAdapter(new SwitchClassAdapter(getApplication(),classList));
		
	}
	
}
