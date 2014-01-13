package com.comdosoft.homework.adapter;

import java.util.List;

import com.comdosoft.homework.R;
import com.comdosoft.homework.pojo.ClassStuPojo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainClssStuAdapter extends  BaseAdapter
{

	private  List<ClassStuPojo> stuList;
	private Context context;
	private int width;
	private int height;
	private ImageView main_class_oneIV;
	private TextView main_class_oneTv1;
	private TextView main_class_oneTv2;
	public MainClssStuAdapter() {
	}
	public MainClssStuAdapter(Context context,List<ClassStuPojo> stulist,int width,int height)
	{
		this.context=context;
		this.stuList=stulist;
		this.width=width;
		this.height=height;

	}
	public int getCount() {
		return stuList.size();
	}
	public Object getItem(int position) {
		return stuList.get(position);
	}
	public long getItemId(int position) {
		return position;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater
				.from(context);
		if(convertView==null)
		{
			convertView = inflater.inflate(R.layout.main_class_one,null);
		
			main_class_oneIV=(ImageView) convertView.findViewById(R.id.main_class_oneIV);
			main_class_oneTv1=(TextView) convertView.findViewById(R.id.main_class_oneTv1);
			main_class_oneTv2=(TextView) convertView.findViewById(R.id.main_class_oneTv2);
			convertView.setPadding((int)(184*0.1), (int)(height*0.75*0.9*0.01), (int)(184*0.1), (int)(height*0.75*0.9*0.01));
		}
		main_class_oneIV.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
//		main_class_oneIV.setBackgroundDrawable(background);
		main_class_oneTv1.setText(stuList.get(position).getNick_Name());
		main_class_oneTv2.setText("("+stuList.get(position).getStudent_Name()+")");
		return convertView;
	}

}
