package com.comdosoft.homework;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.homework.adapter.Adapter;
import com.comdosoft.homework.pojo.Micropost;
import com.comdosoft.homework.pull.XListView;
import com.comdosoft.homework.pull.XListView.IXListViewListener;
import com.comdosoft.homework.tools.HomeWorkTool;

public class Class_xinxiliu extends Activity implements IXListViewListener {
	private XListView listView;
	private MicropostAdapter adapter;
	private List<Micropost> list= new ArrayList<Micropost>();
	private Handler mHandler;
	private int start = 0;
	private int page=1;
	private static int refreshCnt = 0;
	private View layout;
	private ListView listView2;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.class_middle);
//		layout = this.findViewById(R.id.child_micropost);  //  回复  隐藏的  内容

		listView = (XListView) findViewById(R.id.pull_refresh_list);
		listView.setPullLoadEnable(true);
		listView.setDivider(null);
//		(String id, String user_id, String user_types, String name,
//				String nickname, String content, String avatar_url, Long created_at) {
		
		Micropost m1 = new Micropost("1","12","student","张","若相守1","etwevececx2423 sdfd","http://csdnimg.cn/www/images/csdnindex_logo.gif",(long) 234124);
		Micropost m2 = new Micropost("2","12","student","张","若相守2","etwevececx2423 sdfd","http://csdnimg.cn/www/images/csdnindex_logo.gif",(long) 234124);
		Micropost m3 = new Micropost("3","12","student","张","若相守3","etwevececx2423 sdfd","http://csdnimg.cn/www/images/csdnindex_logo.gif",(long) 234124);
		
		
		list.add(m1);list.add(m2);list.add(m3);
		
		 adapter = new MicropostAdapter();
		listView.setAdapter(adapter);
////		listView.setPullLoadEnable(false);
////		listView.setPullRefreshEnable(false);
		listView.setXListViewListener(this);
		mHandler = new Handler();
	}

	
	
	
	
	//   发表
	public void class_fabiao(View v) {
		
		Toast.makeText(getApplicationContext(), "方法没写", 1).show();
	}

		//   全部
		public void class_button_all(View v) {
			list.clear();
			
			//   获得第一页信息
			
			Micropost m1 = new Micropost("1","12","student","张","若相守1","etwevececx2423 sdfd","http://csdnimg.cn/www/images/csdnindex_logo.gif",(long) 234124);
			Micropost m2 = new Micropost("2","12","student","张","若相守2","etwevececx2423 sdfd","http://csdnimg.cn/www/images/csdnindex_logo.gif",(long) 234124);
			Micropost m3 = new Micropost("3","12","student","张","若相守3","etwevececx2423 sdfd","http://csdnimg.cn/www/images/csdnindex_logo.gif",(long) 234124);
			
			
			list.add(m1);list.add(m2);list.add(m3);
			
			// mAdapter.notifyDataSetChanged();
			adapter = new MicropostAdapter();
			listView.setAdapter(adapter);
//			Toast.makeText(getApplicationContext(), "方法没写", 1).show();
		}
		//   我的
		public void class_button_myself(View v) {
			
			list.clear();
			
			//   获得第一页信息
			
			Micropost m1 = new Micropost("1","12","student","张","若相守11","etwevececx2423 sdfd","http://csdnimg.cn/www/images/csdnindex_logo.gif",(long) 234124);
			Micropost m2 = new Micropost("2","12","student","张","若相守22","etwevececx2423 sdfd","http://csdnimg.cn/www/images/csdnindex_logo.gif",(long) 234124);
			Micropost m3 = new Micropost("3","12","student","张","若相守33","etwevececx2423 sdfd","http://csdnimg.cn/www/images/csdnindex_logo.gif",(long) 234124);
			
			
			list.add(m1);list.add(m2);list.add(m3);
			
			// mAdapter.notifyDataSetChanged();
			adapter = new MicropostAdapter();
			listView.setAdapter(adapter);
//			Toast.makeText(getApplicationContext(), "方法没写", 1).show();
		}
	//  回复
		int huifu = 0;

		public void partents_huifu(View v)
		{
			

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
				
				Micropost m1 = new Micropost("1","12","student","张","若相守1","etwevececx2423as sdfd","http://csdnimg.cn/www/images/csdnindex_logo.gif",(long) 234124);
				Micropost m2 = new Micropost("2","12","student","张","若相守2","etwevececx2423 sdfd","http://csdnimg.cn/www/images/csdnindex_logo.gif",(long) 234124);
				Micropost m3 = new Micropost("3","12","student","张","若相守3","etwevececx2423 sdfd","http://csdnimg.cn/www/images/csdnindex_logo.gif",(long) 234124);
				
				
				list.add(m1);list.add(m2);list.add(m3);
				
				// mAdapter.notifyDataSetChanged();
				adapter = new MicropostAdapter();
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
				

				
				page = page+1;
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
				Micropost m4 = new Micropost("4","12","student","张","若相守","etwevececx2423 sdfd","http://csdnimg.cn/www/images/csdnindex_logo.gif",(long) 234124);
				
				
				list.add(m4);
				adapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}


	public class MicropostAdapter extends BaseAdapter {

int huifu_num=0;
int position_huifu_num=-1;
int number=0;
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			
			return list.size();//  数据总数
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		@Override
		public View getView( final int position, View convertView, ViewGroup parent) {
			Log.i("111111111", list.size()+"--"); 
		    LayoutInflater inflater = Class_xinxiliu.this.getLayoutInflater(); 
             final View view = inflater.inflate(R.layout.micropost_item, null); 

			 ImageView face = (ImageView) view.findViewById(R.id.user_face); //  头像
			TextView Micropost_whoToWho = (TextView) view.findViewById(R.id.message_senderName);  // 谁谁   回复  水水
			ImageButton button1 = (ImageButton) view.findViewById(R.id.button1);  //  删除按钮
			TextView Micropost_content = (TextView) view.findViewById(R.id.micropost_content);  //  消息内容
			TextView Micropost_date = (TextView) view.findViewById(R.id.micropost_date);  //   日期
			Button guanzhu = (Button) view.findViewById(R.id.micropost_guanzhu);  //  关注  
			Button huifu = (Button) view.findViewById(R.id.micropost_huifu);  //  回复
			layout = view.findViewById(R.id.child_micropost);  //  回复  隐藏的  内容

//			
//		
//			
//			
			 Micropost mess = list.get(position);
			 
//			 Micropost_senderName.setText(mess.getSender_name());
//			 Micropost_content.setText(mess.getSender_content());
//			 SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			  String a1=dateformat1.format(new Date(mess.getReceiver_date()));
	//
//			 Micropost_date.setText(a1);
			 Micropost_whoToWho.setText(mess.getNickname());
			
			 button1.setTag( position); 

			 button1.setOnClickListener(new OnClickListener() {
				 @Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 int position = Integer.parseInt(v.getTag().toString()); 

					 list.remove(position);  
					 MicropostAdapter.this.notifyDataSetChanged(); 

				}
			});
			 
			 
			
			 huifu.setOnClickListener(new OnClickListener() {
				
				 @Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 huifu_num=huifu_num+1;
					 number=number+1;
					 View layout1 = view.findViewById(R.id.child_micropost);  //  回复  隐藏的  内容
//						
					if (number==1) {// 第一次点击时   确保点击  哪一个       都会显示
						layout1.setVisibility(View.VISIBLE);
						 listView2 = (ListView) layout1.findViewById(R.id.aa);
						 listView2.setDivider(null);
						Adapter ad = new Adapter(layout1.getContext(), list, R.layout.child_micropost_item);  
						listView2.setAdapter(ad);
						HomeWorkTool.setListViewHeightBasedOnChildren(listView2);
					
					} 
					 
					if (position_huifu_num==position&&number!=1) {// 第一次点击以后的点击   确保连续点击  哪一个   2次时    都会显示正确的效果
						
					
							if (huifu_num == 1)
							{
								layout1.setVisibility(View.VISIBLE);
								listView2 = (ListView) layout1.findViewById(R.id.aa);
								listView2.setDivider(null);
								Adapter ad = new Adapter(layout1.getContext(), list, R.layout.child_micropost_item);  
								listView2.setAdapter(ad);
								HomeWorkTool.setListViewHeightBasedOnChildren(listView2);
							}
							if (huifu_num == 2)
							{
								layout1.setVisibility(View.GONE);
								huifu_num = 0;
							}
//							 Toast.makeText(getApplicationContext(), huifu_num+"", 0).show();
							
					}
					if (position_huifu_num!=position&&number!=1) {// 第一次点击以后的点击   确保连续点击不同的两个按钮时    都会显示正确的效果
						
					
						layout1.setVisibility(View.VISIBLE);
						listView2 = (ListView) layout1.findViewById(R.id.aa);
						listView2.setDivider(null);
						Adapter ad = new Adapter(layout1.getContext(), list, R.layout.child_micropost_item);  
						listView2.setAdapter(ad);
						HomeWorkTool.setListViewHeightBasedOnChildren(listView2);
							huifu_num = 1;
							
//						 Toast.makeText(getApplicationContext(), huifu_num+"", 0).show();
						
				}
					
					position_huifu_num=position;
				}
			});
	return view;
		}

		

	}
	
	class Huifu_num {
		private int huifu_num;
		
	}
	
	
}
