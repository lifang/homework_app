package com.comdosoft.homework.adapter;

import java.util.List;

import com.comdosoft.homework.R;
import com.comdosoft.homework.pojo.ClassPojo;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater
				.from(context);
		TextView scTv = null;
		if(convertView==null)
		{
			convertView=inflater.inflate(R.layout.switchclass_one, null);
			convertView.setPadding(0, 5, 0, 5);
			scTv=(TextView) convertView.findViewById(R.id.switchclass_oneTv);
			scTv.setBackgroundColor(R.color.lvse);
			scTv.setWidth(479);
			scTv.setHeight(55);
			scTv.setText(classlist.get(position).getName());
			scTv.setGravity(Gravity.CENTER);
			scTv.setTextSize(24);
		}
		return convertView;
	}

}
