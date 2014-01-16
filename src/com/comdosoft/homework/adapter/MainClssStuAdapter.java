package com.comdosoft.homework.adapter;

import java.util.List;

import com.comdosoft.homework.R;
import com.comdosoft.homework.pojo.ClassStuPojo;
import com.comdosoft.homework.tools.AsyncImageLoader;
import com.comdosoft.homework.tools.AsyncImageLoader.LoadFinishCallBack;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainClssStuAdapter extends BaseAdapter
{

	private  List<ClassStuPojo> stuList;
	private Context context;
	private int width;
	private int height;
	private GridView gridView;
	AsyncImageLoader asyncImageLoader=new AsyncImageLoader();
	public MainClssStuAdapter(GridView gridView, Context context,List<ClassStuPojo> stulist,int width,int height)
	{
		this.gridView=gridView;
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
		OneView oneView; 

		if(convertView==null)
		{
			convertView = inflater.inflate(R.layout.main_class_one,null);
			oneView = new OneView();
			oneView.main_class_oneIV=(ImageView) convertView.findViewById(R.id.main_class_oneIV);
			oneView.main_class_oneTv1=(TextView) convertView.findViewById(R.id.main_class_oneTv1);
			oneView.main_class_oneTv2=(TextView) convertView.findViewById(R.id.main_class_oneTv2);
			oneView.main_class_oneIV.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
			convertView.setPadding((int)(184*0.1), (int)(height*0.75*0.9*0.01), (int)(184*0.1), (int)(height*0.75*0.9*0.01));
			convertView.setTag(oneView);
		}
		else
		{
			oneView = (OneView) convertView.getTag();
		}
		String strUrl="http://192.168.0.101:3004"+stuList.get(position).getHead_portrait_Url();
		oneView.main_class_oneIV.setTag(strUrl+stuList.get(position).getId());
		Bitmap bm = asyncImageLoader.asyncLoadImage(strUrl+stuList.get(position).getId(), 0, callback);  
		oneView.main_class_oneIV.setImageBitmap(null);  
		if(bm==null) {  
			oneView.main_class_oneIV.setBackgroundResource(R.drawable.ic_launcher);
		} else {  
			oneView.main_class_oneIV.setImageBitmap(bm);
		} 
		bm=null;
		oneView.main_class_oneTv1.setText(stuList.get(position).getNick_Name());
		oneView.main_class_oneTv2.setText("("+stuList.get(position).getStudent_Name()+")");
		return convertView;
	}
	private class OneView{  
		ImageView main_class_oneIV ;
		TextView main_class_oneTv1 ;
		TextView main_class_oneTv2 ;
	} 
	LoadFinishCallBack callback = new LoadFinishCallBack() {  
		public void loadFinish(String strUrl, int i, Bitmap bitmap) {  
			if (bitmap != null) {  
				ImageView imageView = (ImageView) gridView.findViewWithTag(strUrl);  
				if(imageView != null) {  
					imageView.setImageBitmap(bitmap);  
				}  
			}  
		};  
	};  
}
