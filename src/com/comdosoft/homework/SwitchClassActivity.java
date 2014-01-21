package com.comdosoft.homework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


import com.comdosoft.homework.pojo.ClassPojo;
import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SwitchClassActivity extends Activity
{
	private EditText SwitchClass_Et;
	private ListView switchclassLv;
	private ImageButton switchClassIB;
	private HomeWork hw;
	private List<ClassPojo> classList=new ArrayList<ClassPojo>();
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.switchclass);
		hw = (HomeWork) getApplication();
		SwitchClass_Et=(EditText) findViewById(R.id.SwitchClass_Et);
		SwitchClass_Et.clearFocus();
		switchclassLv=(ListView) findViewById(R.id.switchclassLv);
		switchClassIB=(ImageButton) findViewById(R.id.switchClassIB);
		final HomeWork hw=(HomeWork) getApplication();
		switchClassIB.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Thread thread=new Thread()
				{
					public void run()
					{
						try {
							HashMap<String,String> mp=new HashMap<String,String>();
							mp.put("verification_code", SwitchClass_Et.getText().toString());
							mp.put("student_id", String.valueOf(hw.getUser_id()));
							String json=HomeWorkTool.doPost(Urlinterface.Validation_into_class, mp);
							JSONObject jsonobject=new JSONObject(json);
							Log.i("aa", json);
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
					mp.put("student_id", String.valueOf(hw.getUser_id()));
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
			hw.setMainItem(3);
			intent.setClass(SwitchClassActivity.this, HomeWorkMainActivity.class);
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
	public class SwitchClassAdapter extends BaseAdapter
	{
		private  List<ClassPojo> classlist;
		private Context context;
		public SwitchClassAdapter(Context context,List<ClassPojo> classlist )
		{
			this.context=context;
			this.classlist=classlist;
		}
		public int getCount() {
			return classlist.size();
		}

		public Object getItem(int position) {
			return classlist.get(position);
		}

		public long getItemId(int position) {
			return position;
		}
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater
					.from(context);
			TextView scTv = null;
			if(convertView==null)
			{
				convertView=inflater.inflate(R.layout.switchclass_one, null);
				convertView.setPadding(0, 5, 0, 5);
				scTv=(TextView) convertView.findViewById(R.id.switchclass_oneTv);
				scTv.setWidth(479);
				scTv.setHeight(55);
				scTv.setText(classlist.get(position).getName());
				scTv.setGravity(Gravity.CENTER);
				scTv.setTextSize(24);
				scTv.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v) {
						new Thread()
						{
							public void run()
							{
								HomeWork hw=(HomeWork) context;
								hw.setClass_id(classlist.get(position).getId());
								hw.setMainItem(0);
								Intent intent = new Intent(context,HomeWorkMainActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
								context.getApplicationContext().startActivity(intent);
								SwitchClassActivity.this.finish();
							}
						}.start();
					}
				});
			}
			return convertView;
		}

	}
}
