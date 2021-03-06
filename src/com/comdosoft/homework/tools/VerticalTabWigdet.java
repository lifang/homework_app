package com.comdosoft.homework.tools;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabWidget;

public class VerticalTabWigdet extends TabWidget {
	Resources res;
	public VerticalTabWigdet(Context context, AttributeSet attrs) {
		super(context, attrs);
		res = context.getResources();
		setOrientation(LinearLayout.VERTICAL);
	}

	
	public void addView(View child) {
		LinearLayout.LayoutParams lp = new LayoutParams(80, 120);
		lp.setMargins(0, 0, 0, 0);
		child.setLayoutParams(lp);
		super.addView(child);
	}

}
