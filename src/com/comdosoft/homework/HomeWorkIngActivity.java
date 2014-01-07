package com.comdosoft.homework;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.comdosoft.homework.pojo.WorkPojo;
import com.comdosoft.homework.tools.Urlinterface;

public class HomeWorkIngActivity extends Activity implements Urlinterface {
	
	private ListView working_date_list;
	private List<WorkPojo> work_list;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_working);

		initialize();// 初始化
		work_list = new ArrayList<WorkPojo>();
		for (int i = 0; i < 5; i++) {
			WorkPojo work = new WorkPojo(1, "2014年1月3日", "2014/1/4日", 1);
			work_list.add(work);
		}
		MyAdapter adapter = new MyAdapter(HomeWorkIngActivity.this);
		working_date_list.setAdapter(adapter);
	
	}

	// 初始化
	public void initialize() {
		working_date_list = (ListView) findViewById(R.id.working_date_list);
		
	}
	
	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private Context mContext = null;

		public MyAdapter(Context context) {
			mContext = context;
			mInflater = LayoutInflater.from(mContext);
		}

		public int getCount() {
			return work_list.size();
		}

		public Object getItem(int arg0) {
			return work_list.get(arg0);
		}

		public long getItemId(int arg0) {
			return arg0;
		}
 
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.working_adapter, null);
			}
			ImageView newwork_img = (ImageView)convertView.findViewById(R.id.newwork_img);
			ImageView sign_img = (ImageView)convertView.findViewById(R.id.sign_img);
			TextView work_start_date = (TextView)convertView.findViewById(R.id.work_start_date);
			
			
			return convertView;
		}
	}
}
