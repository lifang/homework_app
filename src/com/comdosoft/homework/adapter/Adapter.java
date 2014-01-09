package com.comdosoft.homework.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.comdosoft.homework.Class_xinxiliu;
import com.comdosoft.homework.R;
import com.comdosoft.homework.pojo.Micropost;

public class Adapter extends BaseAdapter {

	private List<Micropost> Micropostlist; //   在绑定的数据 
	private int resource;  // 绑定的条目界面
	private LayoutInflater inflater;
private Context context1;

	

	public Adapter(Context context,List<Micropost> Micropostlist, int resource) {
		
		this.Micropostlist = Micropostlist;
		this.resource = resource;
		this.context1= context;
	
	
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return Micropostlist.size();//  数据总数
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return Micropostlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView( final int position, View convertView, ViewGroup parent) {
		Log.i("111111111", Micropostlist.size()+"--"); 
		if(convertView==null){
			convertView = inflater.inflate(resource, null);//  生成条目界面对象
		}
		ImageView face = (ImageView) convertView.findViewById(R.id.user_face);
		TextView Micropost_whoToWho = (TextView) convertView.findViewById(R.id.message_senderName);
		ImageButton button1 = (ImageButton) convertView.findViewById(R.id.button1);  //  删除按钮   应该为  
		TextView Micropost_content = (TextView) convertView.findViewById(R.id.micropost_content);
		TextView Micropost_date = (TextView) convertView.findViewById(R.id.micropost_date);
		Button guanzhu = (Button) convertView.findViewById(R.id.micropost_guanzhu);  //  关注  
		Button huifu = (Button) convertView.findViewById(R.id.micropost_huifu);  //  回复
		
//		ListView listView =(ListView) convertView.findViewById(R.id.child_micropost); 
		
	
		
		
		 Micropost mess = Micropostlist.get(position);
		 
//		 Micropost_senderName.setText(mess.getSender_name());
//		 Micropost_content.setText(mess.getSender_content());
//		 SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		  String a1=dateformat1.format(new Date(mess.getReceiver_date()));
//
//		 Micropost_date.setText(a1);
		 Micropost_whoToWho.setText(mess.getNickname());
		 button1.setOnClickListener(new OnClickListener() {
			 @Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Micropostlist.remove(position);  
				 BaseAdapter adapter = new Adapter(context1, Micropostlist, R.layout.micropost_item);
               adapter.notifyDataSetChanged(); 
			}
		});
		 
		 

	
		return convertView;
	}

	

}
