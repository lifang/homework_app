package com.comdosoft.homework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


import com.comdosoft.homework.adapter.SwitchClassAdapter;
import com.comdosoft.homework.pojo.ClassPojo;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class SwitchClassActivity extends Activity
{
	private EditText SwitchClass_Et;
	private ListView switchclassLv;
	private ImageButton switchClassIB;
	private List<ClassPojo> classList=new ArrayList<ClassPojo>();
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.switchclass);
		SwitchClass_Et=(EditText) findViewById(R.id.SwitchClass_Et);
		SwitchClass_Et.clearFocus();
		switchclassLv=(ListView) findViewById(R.id.switchclassLv);
		switchClassIB=(ImageButton) findViewById(R.id.switchClassIB);
		switchClassIB.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Thread thread=new Thread()
				{
					public void run()
					{
						try {
							HashMap<String,String> mp=new HashMap<String,String>();
							mp.put("verification_code", SwitchClass_Et.getText().toString());
							mp.put("student_id", "1");
							String json=HomeWorkTool.doPost(Urlinterface.Validation_into_class, mp);
							JSONObject jsonobject=new JSONObject(json);
							String status=jsonobject.getString("status");
							if(status.equals("error"))
							{
								String notice=jsonobject.getString("notice");
								Toast.makeText(SwitchClassActivity.this, notice, 1).show();
							}
							else
							{
								Intent intent = new Intent(SwitchClassActivity.this,Classxinxiliu.class);
								startActivity(intent);
							}
							handler.sendEmptyMessage(1);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				thread.start();
			}
		});
		Thread thread=new Thread()
		{
			public void run()
			{
				try {
					HashMap<String,String> mp=new HashMap<String,String>();
					mp.put("student_id", "1");
					String json=HomeWorkTool.sendGETRequest(Urlinterface.get_class,mp);
					JSONObject jsonobject=new JSONObject(json);
					JSONArray jsonarray=jsonobject.getJSONArray("classes");
					for(int i=0;i<jsonarray.length();i++)
					{
						JSONObject jsonobject2=jsonarray.getJSONObject(i);
						int class_id=(Integer) jsonobject2.get("class_id");
						String class_name=jsonobject2.getString("class_name");
						classList.add(new ClassPojo(class_id,class_name));
					}
					handler.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent intent = new Intent();
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SwitchClassActivity.this.finish();
			intent.setClass(SwitchClassActivity.this, SettingActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what)
			{
			case 0:
				switchclassLv.setAdapter(new SwitchClassAdapter(getApplication(),classList));
				break;
			case 1:
				break;
			}
		}
	};
}
