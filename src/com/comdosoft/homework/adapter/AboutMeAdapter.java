package com.comdosoft.homework.adapter;

import java.util.List;

import com.comdosoft.homework.R;
import com.comdosoft.homework.pojo.AboutMePojo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AboutMeAdapter extends BaseAdapter
{
	private List<AboutMePojo> Amlist;
	private Context context;
	public int getCount() {
		return Amlist.size();
	}
	public Object getItem(int position) {
		return Amlist.get(position);
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
			
		}
		return null;
	}

}
