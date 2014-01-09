package com.comdosoft.homework;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.comdosoft.homework.adapter.MicropostAdapter;
import com.comdosoft.homework.pojo.Micropost;
import com.comdosoft.homework.pull.XListView;

public class Class_xinxiliu extends Activity {
	private XListView listView;
	private MicropostAdapter adapter;
	private List<Micropost> list= new ArrayList<Micropost>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.class_middle);
		

		listView = (XListView) findViewById(R.id.pull_refresh_list);
		listView.setPullLoadEnable(true);
		
//		(String id, String user_id, String user_types, String name,
//				String nickname, String content, String avatar_url, Long created_at) {
		
		Micropost m1 = new Micropost("1","12","student","张","若相守","etwevececx2423 sdfd","http://csdnimg.cn/www/images/csdnindex_logo.gif",(long) 234124);
		Micropost m2 = new Micropost("2","12","student","张","若相守","etwevececx2423 sdfd","http://csdnimg.cn/www/images/csdnindex_logo.gif",(long) 234124);
		Micropost m3 = new Micropost("3","12","student","张","若相守","etwevececx2423 sdfd","http://csdnimg.cn/www/images/csdnindex_logo.gif",(long) 234124);
		
		
		list.add(m1);list.add(m2);list.add(m3);
		
		 adapter = new MicropostAdapter(this, list, R.layout.micropost_item);
		listView.setAdapter(adapter);
//		listView.setPullLoadEnable(false);
//		listView.setPullRefreshEnable(false);
		listView.setXListViewListener(this);
		mHandler = new Handler();
	}

	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime("刚刚");
	}
	
	
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				start = ++refreshCnt;
				list.clear();
				
				//   获得第一页信息
				
				
				
				// mAdapter.notifyDataSetChanged();
				adapter = new MessageAdapter(Messages.this, list, R.layout.message_item,height);
				listView.setAdapter(adapter);
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				

				
//				page = page+1;
//				if (page<=pages_count)
//				{
//					
//					String result2 = Service.getClassesList(page);
//					
//					
//							JSONObject array;
//							try
//							{
//								array = new JSONObject(result2);// {"page":1,"pages_count":1,"status":"success","notice":"\u52a0\u8f7d\u5b8c\u6210","school_classes":[{"id":5,"name":"1","students_count":null,"teacher_name":"123"},
//								
//								page = Integer.valueOf(array.getString("page"));
//								 pages_count = Integer.valueOf(array.getString("pages_count"));
//								 notice = array.getString("notice");
//								 classListJson = array.getString("school_classes");
//								 JSONArray jsonArray2 = new JSONArray(classListJson);
//								 //{"id":5,"name":"1","students_count":null,"teacher_name":"123"}
//								 
//								
//									 for (int i = 0; i < jsonArray2.length(); ++i) {
//									 JSONObject o = (JSONObject) jsonArray2.get(i);
//									 String id = o.getString("id");
//									 String name = o.getString("name");
//									 int students_count = o.getInt("school_class_student_relations_count");
//									 String teacher_name = o.getString("teacher_name");
//									
//									 Classes menu = new Classes(id,name,students_count,teacher_name);
//									
//									 list.add(menu);
//									 }
//								Toast.makeText(getApplicationContext(), notice, 0).show();
//							
//							} catch (JSONException e)
//							{
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}	
//				}
				list.add(new Message(5,  "student",11,  "张111三", "千万人铅污染却如此热情却像刺猬vr","student",22,
						(long) 123334556));
				
				adapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}


}
