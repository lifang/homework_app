package com.comdosoft.homework;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.comdosoft.homework.adapter.MicropostAdapter;
import com.comdosoft.homework.pojo.Micropost;
import com.comdosoft.homework.pull.XListView;

public class class_xinxiliu extends Activity {
	private XListView listView;
	private MicropostAdapter adapter;
	private List<Micropost> list= new ArrayList<Micropost>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.class_middle);
		

		listView = (XListView) findViewById(R.id.pull_refresh_list);
		listView.setPullLoadEnable(true);
		 adapter = new MicropostAdapter(this, list, R.layout.micropost_item);
//		listView.setAdapter(adapter);
////		listView.setPullLoadEnable(false);
////		listView.setPullRefreshEnable(false);
//		listView.setXListViewListener(this);
//		mHandler = new Handler();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
