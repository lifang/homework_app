package com.comdosoft.homework.adapter;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.comdosoft.homework.Classxinxiliu;
import com.comdosoft.homework.R;
import com.comdosoft.homework.pojo.AboutMePojo;
import com.comdosoft.homework.tools.AsyncImageLoader;
import com.comdosoft.homework.tools.AsyncImageLoader.LoadFinishCallBack;
import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.Urlinterface;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AboutMeAdapter extends BaseAdapter
{
	private List<AboutMePojo> Amlist;
	private Context context;
	private ListView listview;
	private int class_id;
	AsyncImageLoader asyncImageLoader=new AsyncImageLoader();
	HomeWork hw=(HomeWork) context.getApplicationContext();
	public AboutMeAdapter()
	{
	}
	public AboutMeAdapter(List<AboutMePojo> amlist, Context context,ListView listview,int class_id) {
		Amlist = amlist;
		this.context = context;
		this.listview=listview;
		this.class_id=class_id;
	}
	public int getCount() {
		return Amlist.size();
	}
	public Object getItem(int position) {
		return Amlist.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater
				.from(context);
		if(convertView==null)
		{
			convertView = inflater.inflate(R.layout.aboutme_one,null);

			TextView tv1= (TextView) convertView.findViewById(R.id.aboutme_oneTv);
			TextView tv2= (TextView) convertView.findViewById(R.id.aboutme_oneTv2);
			TextView tv3= (TextView) convertView.findViewById(R.id.aboutme_oneTv3);
			TextView tv4= (TextView) convertView.findViewById(R.id.aboutme_oneTv4);
			TextView tv5= (TextView) convertView.findViewById(R.id.aboutme_oneTv5);
			ImageButton Ib=(ImageButton) convertView.findViewById(R.id.aboutme_oneIb);
			tv1.setText(Amlist.get(position).getSender_name());
			tv2.setText(Amlist.get(position).getStatus());
			tv3.setText(Amlist.get(position).getContent());
			tv4.setText(Amlist.get(position).getCreated_at());

			tv5.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v) {
					new Thread()
					{
						@SuppressWarnings({ "unchecked", "rawtypes" })
						public void run()
						{
							HashMap<String, Integer> mp=new HashMap();
							mp.put("user_id", Integer.valueOf(Amlist.get(position).getUser_id()));
							mp.put("school_class_id",class_id);
							mp.put("message_id", Integer.valueOf(Amlist.get(position).getMicropost_id()));
							String json=HomeWorkTool.doPost(Urlinterface.read_message, mp);
							Message msg=new Message();
							msg.what=2;
							hw.setNoselect_message(json);
							handler.sendMessage(msg);
						}
					}.start();

				}
			});
			Ib.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v) {
					new Thread()
					{
						@SuppressWarnings({ "rawtypes", "unchecked" })
						public void run()
						{
							try {
								HashMap<String, Integer> mp=new HashMap();
								mp.put("user_id", Integer.valueOf(Amlist.get(position).getUser_id()));
								mp.put("school_class_id",class_id);
								mp.put("message_id", Integer.valueOf(Amlist.get(position).getId()));
								String json=HomeWorkTool.doPost(Urlinterface.delete_message, mp);
								JSONObject jsonobject=new JSONObject(json);
								String notice=jsonobject.getString("notice");
								String status=(String) jsonobject.get("status");
								Message msg=new Message();
								msg.what=0;
								msg.obj=notice;
								if(status.equals("success"))
								{
									Amlist.remove(position);
								}
								else
								{
									handler.sendEmptyMessage(1);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}.start();
				}
			});

			ImageView iv=(ImageView) convertView.findViewById(R.id.aboutme_oneIv);
			String strUrl=Urlinterface.IP+Amlist.get(position).getSender_avatar_url();
			iv.setTag(strUrl+Amlist.get(position).getId());
			Bitmap bm = asyncImageLoader.asyncLoadImage(strUrl+Amlist.get(position).getId(), 0, callback);  
			iv.setImageBitmap(null);  
			if(bm==null) {  
				iv.setBackgroundResource(R.drawable.ic_launcher);
			} else {  
				iv.setImageBitmap(bm);	
			} 
			bm=null;
		}	
		return convertView;

	}
	@SuppressLint({ "HandlerLeak", "ShowToast" })
	Handler handler=new Handler()
	{
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what) {
			case 0:
				notifyDataSetChanged();
				break;
			case 1:
				Toast.makeText(context, msg.obj.toString(), 0).show();
				break;
			case 2:
				Intent intent = new Intent(context,Classxinxiliu.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				context.getApplicationContext().startActivity(intent);
				break;
			default:
				break;
			}
		}
	};
	LoadFinishCallBack callback = new LoadFinishCallBack() {  
		public void loadFinish(String strUrl, int i, Bitmap bitmap) {  
			if (bitmap != null) {  
				ImageView imageView = (ImageView) listview.findViewWithTag(strUrl);  
				if(imageView != null) {  
					imageView.setImageBitmap(bitmap);  
				}  
			}  
		};  
	};  
}