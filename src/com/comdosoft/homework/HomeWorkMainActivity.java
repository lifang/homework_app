package com.comdosoft.homework;

import java.lang.reflect.Field;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.Urlinterface;

public class HomeWorkMainActivity extends TabActivity implements Urlinterface {
	TabHost tabhost;
	TabHost.TabSpec spec1, spec2, spec3, spec4;
	private Resources res;
	public Field mBottomLeftStrip;
	public Field mBottomRightStrip;
	private HomeWork homework;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homewrok_main);
		homework = (HomeWork) getApplication();
		Log.i(tag, "1111111111");
		tabhost = getTabHost();
		res = getResources();
		Intent intent1 = new Intent(this, Spec1.class);
		spec1 = tabhost.newTabSpec("spec1")
				.setIndicator("", res.getDrawable(R.drawable.th1_1))
				.setContent(intent1);
		tabhost.addTab(spec1);

		Intent intent2 = new Intent(this, HomeWorkIngActivity.class);
		spec2 = tabhost.newTabSpec("spec2")
				.setIndicator("", res.getDrawable(R.drawable.th2_2))
				.setContent(intent2);
		tabhost.addTab(spec2);

		Intent intent3 = new Intent(this, Spec3.class);
		spec3 = tabhost.newTabSpec("spec3")
				.setIndicator("", res.getDrawable(R.drawable.th3_3))
				.setContent(intent3);
		tabhost.addTab(spec3);

		Intent intent4 = new Intent(this, Spec4.class);
		spec4 = tabhost.newTabSpec("spec4")
				.setIndicator("", res.getDrawable(R.drawable.th4_4))
				.setContent(intent4);
		tabhost.addTab(spec4);
		tabhost.setCurrentTab(homework.getMainItem());
		updateTabStyle(tabhost);
	}

	private void updateTabStyle(final TabHost mTabHost) {
		final TabWidget tabWidget = mTabHost.getTabWidget();
		tabWidget.setBackgroundColor(res.getColor(R.color.lvse));
		tabWidget.setGravity(Gravity.CENTER_VERTICAL);
		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			RelativeLayout tabView = (RelativeLayout) mTabHost.getTabWidget()
					.getChildAt(i);
			ImageView img = (ImageView) tabWidget.getChildAt(i).findViewById(
					android.R.id.icon);

			if (i == homework.getMainItem()) {
				tabView.setBackgroundColor(res.getColor(R.color.white));
				switch (i) {
				case 0:
					img.setImageResource(R.drawable.th1);
					break;
				case 1:
					img.setImageResource(R.drawable.th2);
					break;
				case 2:
					img.setImageResource(R.drawable.th3);
					break;
				case 3:
					img.setImageResource(R.drawable.th4);
					break;
				}
			} else {
				tabView.setBackgroundColor(res.getColor(R.color.lvse));
				switch (i) {
				case 0:
					img.setImageResource(R.drawable.th1_1);
					break;
				case 1:
					img.setImageResource(R.drawable.th2_2);
					break;
				case 2:
					img.setImageResource(R.drawable.th3_3);
					break;
				case 3:
					img.setImageResource(R.drawable.th4_4);
					break;
				}
			}
			img.setPadding(0, 15, 0, 15);
			/**
			 * 此方法是为了去掉系统默认的色白的底角
			 * 
			 * 在 TabWidget中mBottomLeftStrip、mBottomRightStrip
			 * 都是私有变量，但是我们可以通过反射来获取
			 * 
			 * 由于Android 2.2，2.3的接口不同，加个判断
			 */

			if (Float.valueOf(Build.VERSION.RELEASE.substring(0, 3)) <= 2.1) {
				try {
					mBottomLeftStrip = tabWidget.getClass().getDeclaredField(
							"mBottomLeftStrip");
					mBottomRightStrip = tabWidget.getClass().getDeclaredField(
							"mBottomRightStrip");
					if (!mBottomLeftStrip.isAccessible()) {
						mBottomLeftStrip.setAccessible(true);
					}
					if (!mBottomRightStrip.isAccessible()) {
						mBottomRightStrip.setAccessible(true);
					}
					mBottomLeftStrip.set(tabWidget,
							getResources().getDrawable(R.drawable.no));
					mBottomRightStrip.set(tabWidget, getResources()
							.getDrawable(R.drawable.no));

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {

				// 如果是2.2,2.3版本开发,可以使用一下方法tabWidget.setStripEnabled(false)
				// tabWidget.setStripEnabled(false);

				// 但是很可能你开发的android应用是2.1版本，
				// tabWidget.setStripEnabled(false)编译器是无法识别而报错的,这时仍然可以使用上面的
				// 反射实现，但是代码的改改

				try {
					// 2.2,2.3接口是mLeftStrip，mRightStrip两个变量，当然代码与上面部分重复了
					mBottomLeftStrip = tabWidget.getClass().getDeclaredField(
							"mLeftStrip");
					mBottomRightStrip = tabWidget.getClass().getDeclaredField(
							"mRightStrip");
					if (!mBottomLeftStrip.isAccessible()) {
						mBottomLeftStrip.setAccessible(true);
					}
					if (!mBottomRightStrip.isAccessible()) {
						mBottomRightStrip.setAccessible(true);
					}
					mBottomLeftStrip.set(tabWidget,
							getResources().getDrawable(R.drawable.no));
					mBottomRightStrip.set(tabWidget, getResources()
							.getDrawable(R.drawable.no));

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
				public void onTabChanged(String tabId) {
					for (int i = 0; i < tabWidget.getChildCount(); i++) {
						RelativeLayout tabView = (RelativeLayout) mTabHost
								.getTabWidget().getChildAt(i);
						ImageView img = (ImageView) tabWidget.getChildAt(i)
								.findViewById(android.R.id.icon);
						if (mTabHost.getCurrentTab() == i) {
							homework.setMainItem(i);
							tabView.setBackgroundColor(res
									.getColor(R.color.white));
							switch (i) {
							case 0:
								img.setImageResource(R.drawable.th1);
								break;
							case 1:
								img.setImageResource(R.drawable.th2);
								break;
							case 2:
								img.setImageResource(R.drawable.th3);
								break;
							case 3:
								img.setImageResource(R.drawable.th4);
								break;
							}
						} else {
							tabView.setBackgroundColor(res
									.getColor(R.color.lvse));
							switch (i) {
							case 0:
								img.setImageResource(R.drawable.th1_1);
								break;
							case 1:
								img.setImageResource(R.drawable.th2_2);
								break;
							case 2:
								img.setImageResource(R.drawable.th3_3);
								break;
							case 3:
								img.setImageResource(R.drawable.th4_4);
								break;
							}
						}
						img.setPadding(0, 15, 0, 15);
					}
				}
			});
		}
	}
}