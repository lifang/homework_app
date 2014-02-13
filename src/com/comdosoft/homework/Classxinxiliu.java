package com.comdosoft.homework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.homework.adapter.MainClssStuAdapter;
import com.comdosoft.homework.pojo.Child_Micropost;
import com.comdosoft.homework.pojo.ClassStuPojo;
import com.comdosoft.homework.pojo.Micropost;
import com.comdosoft.homework.tools.HomeWork;
import com.comdosoft.homework.tools.HomeWorkParams;
import com.comdosoft.homework.tools.HomeWorkTool;
import com.comdosoft.homework.tools.PullToRefreshView;
import com.comdosoft.homework.tools.PullToRefreshView.OnFooterRefreshListener;
import com.comdosoft.homework.tools.PullToRefreshView.OnHeaderRefreshListener;
import com.comdosoft.homework.tools.Urlinterface;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class Classxinxiliu extends Activity implements OnHeaderRefreshListener,
		OnFooterRefreshListener, Urlinterface {
	private HomeWork hw;
	private String user_id = "66"; // 学生 id 上面 会传过来的 学生student_id，
	private String id = "66";
	private String school_class_id = "1";
	private GridView main_class_classGv;
	private ImageView main_class_oneIV;
	private TextView main_class_classesTv;
	private TextView main_class_oneTv1;
	private TextView main_class_oneTv2;
	private EditText fabiao_content; // 发表框;
	private ProgressDialog prodialog;
	private String lookStr = "";
	// -------------------------------------------------------------------
	private String json = "{\"status\":\"success\",\"notice\":\"\u767b\u9646\u6210\u529f\uff01\",\"student\":{\"id\":66,\"name\":\"hrueieurh \",\"user_id\":66,\"nickname\":\"yeueieiri \",\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\"},\"class\":{\"id\":1,\"name\":\"1401\",\"tearcher_name\":\"fgf\",\"tearcher_id\":1},\"classmates\":[{\"avatar_url\":\"/assets/default_avater.jpg\",\"id\":1,\"name\":\"nan\",\"nickname\":\"zxn\"}],\"task_messages\":[],\"microposts\":{\"page\":1,\"pages_count\":4,\"details_microposts\":[{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"ww\u6211\u6d4b\u5b89\u5fbdi\u2026\u2026\n\n\u6d4b\u8bd5\u4e00\u4e0b\",\"created_at\":\"2014-01-27T14:12:02+08:00\",\"micropost_id\":145,\"name\":\"hrueieurh \",\"reply_microposts_count\":1,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"556355265335 \",\"created_at\":\"2014-01-23T07:17:25+08:00\",\"micropost_id\":104,\"name\":\"hrueieurh \",\"reply_microposts_count\":0,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"Babbitt \",\"created_at\":\"2014-01-23T02:58:48+08:00\",\"micropost_id\":103,\"name\":\"hrueieurh \",\"reply_microposts_count\":4,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"\u5173\u4e8e\u52a0\u5f3a\",\"created_at\":\"2014-01-22T10:11:11+08:00\",\"micropost_id\":99,\"name\":\"???\",\"reply_microposts_count\":6,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"\u7684\u7684\u7684\",\"created_at\":\"2014-01-22T09:47:22+08:00\",\"micropost_id\":97,\"name\":\"hrueieurh \",\"reply_microposts_count\":5,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"\u5927\u5927\u65b9\u65b9\u53d1\",\"created_at\":\"2014-01-22T09:47:12+08:00\",\"micropost_id\":96,\"name\":\"hrueieurh \",\"reply_microposts_count\":0,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"hjk\",\"created_at\":\"2014-01-22T06:43:12+08:00\",\"micropost_id\":95,\"name\":\"???\",\"reply_microposts_count\":19,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"ffg\",\"created_at\":\"2014-01-22T02:57:52+08:00\",\"micropost_id\":94,\"name\":\"???\",\"reply_microposts_count\":6,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"UI o\",\"created_at\":\"2014-01-21T10:48:36+08:00\",\"micropost_id\":93,\"name\":\"???\",\"reply_microposts_count\":2,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"rrrrr\",\"created_at\":\"2014-01-21T10:13:22+08:00\",\"micropost_id\":86,\"name\":\"hrueieurh \",\"reply_microposts_count\":16,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"rrrrr\",\"created_at\":\"2014-01-21T10:13:21+08:00\",\"micropost_id\":85,\"name\":\"hrueieurh \",\"reply_microposts_count\":7,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"dffff\",\"created_at\":\"2014-01-21T10:13:19+08:00\",\"micropost_id\":84,\"name\":\"hrueieurh \",\"reply_microposts_count\":0,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"\u5feb\u4e86\uff0c\u5feb\u4e86\u5feb\u4e86\",\"created_at\":\"2014-01-21T06:46:09+08:00\",\"micropost_id\":73,\"name\":\"??\",\"reply_microposts_count\":0,\"user_id\":8,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"dddddd\",\"created_at\":\"2014-01-20T06:24:07+08:00\",\"micropost_id\":72,\"name\":\"???\",\"reply_microposts_count\":3,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"ggjjkkjhghhjjj\",\"created_at\":\"2014-01-20T04:41:44+08:00\",\"micropost_id\":71,\"name\":\"???\",\"reply_microposts_count\":0,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"\u98ce\u98ce\u5149\u5149\u97e9\u56fd\u521a\u521a\u597d\u53d1\u98ce\u98ce\u5149\u5149\u5730\u65b9\u98ce\u683c\u56de\u5bb6\u98ce\u683c\u54c8\u54c8\u54c8\u98ce\u683c\u5475\u5475\u7684\u611f\u89c9\u7684\u98ce\u683c\u4e2a\",\"created_at\":\"2014-01-20T02:52:47+08:00\",\"micropost_id\":70,\"name\":\"???\",\"reply_microposts_count\":1,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"\u89c4\u5212\u5c40\",\"created_at\":\"2014-01-18T10:57:36+08:00\",\"micropost_id\":69,\"name\":\"???\",\"reply_microposts_count\":0,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"\u4f1a\",\"created_at\":\"2014-01-18T10:41:09+08:00\",\"micropost_id\":68,\"name\":\"???\",\"reply_microposts_count\":1,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"\u4e2a\u770b\u8fc7\",\"created_at\":\"2014-01-18T10:40:54+08:00\",\"micropost_id\":67,\"name\":\"???\",\"reply_microposts_count\":0,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"yyuu\",\"created_at\":\"2014-01-18T10:33:45+08:00\",\"micropost_id\":66,\"name\":\"???\",\"reply_microposts_count\":0,\"user_id\":9,\"user_types\":1}]},\"daily_tasks\":[{\"id\":2,\"name\":\"2014-1-20\u4f5c\u4e1a\",\"start_time\":\"2014-01-20T00:00:00+08:00\",\"end_time\":\"2014-01-30T00:00:00+08:00\",\"question_packages_url\":\"/question_package_1.js\",\"listening_schedule\":\"1/4\",\"reading_schedule\":\"0/4\"}],\"follow_microposts_id\":[86,97,97,96,95,93,71,93,71,99],\"messages\":[{\"content\":\"[[fgh]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;jjj\",\"created_at\":\"2014-01-26T12:32:23+08:00\",\"id\":554,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[fgh]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;uui\",\"created_at\":\"2014-01-26T12:32:07+08:00\",\"id\":553,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[fgh]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;huu\",\"created_at\":\"2014-01-26T12:31:59+08:00\",\"id\":552,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[fgh]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;jjii\",\"created_at\":\"2014-01-26T12:30:43+08:00\",\"id\":550,\"micropost_id\":85,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u5173\u6ce8\u7684\u6d88\u606f\uff1a;||;ca\",\"created_at\":\"2014-01-23T03:04:38+08:00\",\"id\":208,\"micropost_id\":86,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;ca\",\"created_at\":\"2014-01-23T03:04:30+08:00\",\"id\":189,\"micropost_id\":86,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u5173\u6ce8\u7684\u6d88\u606f\uff1a;||;ca\",\"created_at\":\"2014-01-23T03:04:30+08:00\",\"id\":198,\"micropost_id\":86,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;a\",\"created_at\":\"2014-01-23T03:04:00+08:00\",\"id\":187,\"micropost_id\":97,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;CA\",\"created_at\":\"2014-01-23T03:03:46+08:00\",\"id\":186,\"micropost_id\":97,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;ding\",\"created_at\":\"2014-01-23T03:01:09+08:00\",\"id\":185,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;@\",\"created_at\":\"2014-01-23T03:00:51+08:00\",\"id\":184,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;ca\",\"created_at\":\"2014-01-23T03:00:29+08:00\",\"id\":183,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;ca\",\"created_at\":\"2014-01-23T03:00:29+08:00\",\"id\":182,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;abc\",\"created_at\":\"2014-01-23T02:07:00+08:00\",\"id\":180,\"micropost_id\":99,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;''''\",\"created_at\":\"2014-01-23T02:05:37+08:00\",\"id\":175,\"micropost_id\":97,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u54c8\u54c8\u54c8\u5c31\",\"created_at\":\"2014-01-22T10:09:41+08:00\",\"id\":163,\"micropost_id\":97,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u53cd\u5f39\u6709\",\"created_at\":\"2014-01-22T10:09:22+08:00\",\"id\":162,\"micropost_id\":97,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u89c4\u5212\",\"created_at\":\"2014-01-22T10:09:15+08:00\",\"id\":161,\"micropost_id\":97,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:41+08:00\",\"id\":159,\"micropost_id\":85,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:40+08:00\",\"id\":157,\"micropost_id\":85,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:39+08:00\",\"id\":153,\"micropost_id\":85,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:34+08:00\",\"id\":144,\"micropost_id\":86,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:32+08:00\",\"id\":135,\"micropost_id\":86,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:30+08:00\",\"id\":126,\"micropost_id\":86,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:28+08:00\",\"id\":117,\"micropost_id\":86,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66}]}";
	public PullToRefreshView mPullToRefreshView;
	public LinearLayout Linear_layout;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;
	public ImageLoader imageLoader;
	private String classname;
	private int child_page = 1; // 子消息 分页加载的 第几页
	private int child_pages_count;// 子消息总页数
	private int page;// 当前第几页
	private int pages_count;// 总页数
	private String reciver_id = "";
	private String reciver_types = "";
	private String micropost_id = "";
	private String lookStr_micropost_id;
	private int list_item;// list集合的最后一位索引
	private int user_types = 1;
	public List<Boolean> gk_list;// 回复开关集合
	public List<RelativeLayout> item_huifu;// 回复开关集合
	private int micropost_type;// 微博类型 0表是全部 1表示我的
	private List<Micropost> list;
	private List<String> care;
	private List<Button> guanzhu_list;
	private List<ListView> list_list;
	private List<Button> btlist;
	private List<ZiAdapter> ziAdapter_list;
	private List<EditText> Reply_edit_list;
	private String avatar_url;
	private String user_name;
	private String nick_name;
	private int focus = -1;
	private int lass_count = 1;
	private int lass_count2 = 1;
	private String user_Url;
	private int width;
	private int height;
	private List<ClassStuPojo> stuList ;
	private ArrayList<Child_Micropost> child_list;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				prodialog.dismiss();
				init();
//				main_class_oneTv1.setText(user_name); // 设置本名
//				main_class_oneTv2.setText(nick_name); // 设置外号
//				main_class_classesTv.setText(classname);
//				imageLoader.displayImage(IP + avatar_url, main_class_oneIV,
//						options, animateFirstListener);
//				main_class_classGv.setAdapter(new MainClssStuAdapter(
//						main_class_classGv, getApplicationContext(), stuList,
//						width, height));
				break;

			case 2:
				focus = -1;
				list.clear();
				click_list();
				final String json_all = (String) msg.obj;
				if (micropost_type == 0) {// 0全部 1自己
					parseJson_all(json_all);
				} else {
					parseJson_Myself(json_all);
				}
				for (int i = 0; i < list.size(); i++) {
					setlayout(i);
				}
				mPullToRefreshView.onHeaderRefreshComplete();
				break;
			case 3:
				final String json_all2 = (String) msg.obj;
				if (micropost_type == 0) {// 0全部 1自己
					parseJson_all(json_all2);
				} else {
					parseJson_Myself(json_all2);
				}
				for (int i = list_item; i < list.size(); i++) {
					setlayout(i);
				}
				mPullToRefreshView.onFooterRefreshComplete();
				break;
			case 4:
				prodialog.dismiss();
				break;
			case 5:
				Button b = (Button) findViewById(R.id.class_button_all);
				b.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.an2));
				Button b2 = (Button) findViewById(R.id.class_button_myself);
				b2.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.an));
				break;

			case 6:
				focus = -1;
				list.clear();
				click_list();
				//
				final String json_all21 = (String) msg.obj;

				parseJson_Myself(json_all21);

				lookStr = hw.getNoselect_message();
				Log.i("linshi", "1");
				Micropost lookStr_micropost = new Micropost();
				if (lookStr != null && !lookStr.equals("")) {
					Log.i("linshi", "2");
					// care.clear();
					page = 1;
					micropost_type = 1;
					hw.setNoselect_message(""); // 将 公共变量Noselect_message
												// 设置为 ""
					child_list = new ArrayList<Child_Micropost>();

					handler.sendEmptyMessage(5);

					JSONObject js;
					try {
						Log.i("linshi", lookStr);
						js = new JSONObject(lookStr);
						String micropost = js.getString("micropost");
						JSONArray jsonArray2;
						try {
							jsonArray2 = new JSONArray(micropost);
							for (int i = 0; i < jsonArray2.length(); ++i) {
								JSONObject o = (JSONObject) jsonArray2.get(i);
								lookStr_micropost_id = o
										.getString("micropost_id");
								String user_id = o.getString("user_id");
								String user_types = o.getString("user_types");
								String name = o.getString("name");
								String content = o.getString("content");
								String avatar_url = o.getString("avatar_url");
								String created_at = o.getString("created_at");
								String reply_microposts_count = o
										.getString("reply_microposts_count");
								lookStr_micropost = new Micropost(
										lookStr_micropost_id, user_id,
										user_types, name, content, avatar_url,
										created_at, reply_microposts_count);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

					Log.i("linshi", lookStr_micropost.toString());

					int a = 0;
					if (list.size() != 0) {

						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).getId()
									.equals(lookStr_micropost_id)) {
								focus = i; // 要展开的 主消息 的 位置
								break;
							} else {
								a = i + 1;
							}
						}
						//
						// Log.i("linshi", a + "/" + focus);
						if (a == list.size()) {// 若第一页主消息中没有 提示信息所在，则
							pages_count = 1; // 0 标记 用于表示从别的页面跳到本页面，在上拉加载时会用到
							list.clear();
							focus = 0; // 要展开的 主消息 的 位置
							list.add(lookStr_micropost);
						}

					}
					// focus = 0;
					Log.i("linshi", list.size() + "");
				}
				for (int i = 0; i < list.size(); i++) {
					setlayout(i);
				}
				break;
			case 7:
//				final String sd = (String) msg.obj;
//				Toast.makeText(getApplicationContext(), sd, Toast.LENGTH_SHORT)
//				.show();
				Toast.makeText(getApplicationContext(), "111", Toast.LENGTH_SHORT)
				.show();
				break;
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.class_middle);
		hw = (HomeWork) getApplication();

//		main_class_classGv = (GridView) findViewById(R.id.main_class_classGv);
//		main_class_classesTv = (TextView) findViewById(R.id.main_class_classesTv);
//		main_class_oneIV = (ImageView) findViewById(
//				R.id.main_class_classes_include).findViewById(
//				R.id.main_class_oneIV);
//		main_class_oneTv1 = (TextView) findViewById(
//				R.id.main_class_classes_include).findViewById(
//				R.id.main_class_oneTv1);
//		main_class_oneTv2 = (TextView) findViewById(
//				R.id.main_class_classes_include).findViewById(
//				R.id.main_class_oneTv2);
//		main_class_classGv.setNumColumns(3);

		SharedPreferences preferences = getSharedPreferences(SHARED,
				Context.MODE_PRIVATE);

		user_id = preferences.getString("user_id", null);
		id = preferences.getString("id", null);
		school_class_id = preferences.getString("school_class_id", null);
		user_id = preferences.getString("user_id", null);

		micropost_type = 0;// 默认现实全部
		item_huifu = new ArrayList<RelativeLayout>();
		guanzhu_list = new ArrayList<Button>();
		ziAdapter_list = new ArrayList<ZiAdapter>();
		btlist = new ArrayList<Button>();
		Reply_edit_list = new ArrayList<EditText>();
		list = new ArrayList<Micropost>();
		list_list = new ArrayList<ListView>();
		gk_list = new ArrayList<Boolean>();
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.moren)
				.showImageForEmptyUri(R.drawable.moren)
				.showImageOnFail(R.drawable.moren).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(0)).cacheInMemory(false)
				.cacheOnDisc(false).build();
		lass_count = 1;
		lass_count2 = 2;
		if (HomeWorkTool.isConnect(Classxinxiliu.this)) {
			prodialog = new ProgressDialog(Classxinxiliu.this);
			prodialog.setMessage(HomeWorkParams.PD_CLASS_INFO);
			prodialog.show();
			Thread thread = new Thread(new get_class_info());
			thread.start();
			
		} else {
			Toast.makeText(getApplicationContext(), HomeWorkParams.INTERNET, 0)
					.show();
		}
		lass_count2 = 1;
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (HomeWorkTool.isConnect(Classxinxiliu.this)) {
			lass_count = lass_count + 1;
			Thread thread = new Thread(new get_class_info());
			if (lass_count != 2) {

				thread.start();
			}
		} else {
			Toast.makeText(getApplicationContext(), HomeWorkParams.INTERNET, 0)
					.show();
		}
	}

	public void init() {
		item_huifu = new ArrayList<RelativeLayout>();

		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		Linear_layout = (LinearLayout) findViewById(R.id.layout);
		click_list();
		if (list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				setlayout(i);
			}
		} else {
			Toast.makeText(getApplicationContext(), "暂无记录", Toast.LENGTH_SHORT)
					.show();
		}
		fabiao_content = (EditText) findViewById(R.id.class_fabiao_content);

	}

	public void setlayout(final int i) {
		final Micropost mess = list.get(i);
		ZiAdapter Adapter = new ZiAdapter();
		ziAdapter_list.add(Adapter);
		final View convertView = LayoutInflater.from(Classxinxiliu.this)
				.inflate(R.layout.class_layout, null);
		ImageView face = (ImageView) convertView.findViewById(R.id.user_face); // 头像
		TextView Micropost_senderName = (TextView) convertView
				.findViewById(R.id.message_senderName); // 谁发的
		ImageButton button1 = (ImageButton) convertView
				.findViewById(R.id.button1); // 删除按钮
		TextView Micropost_content = (TextView) convertView
				.findViewById(R.id.micropost_content); // 消息内容
		TextView Micropost_date = (TextView) convertView
				.findViewById(R.id.micropost_date); // 日期
		final Button guanzhu = (Button) convertView
				.findViewById(R.id.micropost_guanzhu); // 关注
		final ListView listView2 = (ListView) convertView// 子消息的list
				.findViewById(R.id.aa);//
		list_list.add(listView2);
		final EditText Reply_edit = (EditText) convertView
				.findViewById(R.id.reply_edit);
		Reply_edit_list.add(Reply_edit);
		guanzhu_list.add(guanzhu);
		gk_list.add(true);
		// item_huifu.add(convertView);
		final Button lookMore = (Button) convertView
				.findViewById(R.id.lookMore); // 查看更多
		lookMore.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setlookMore(listView2, mess);
			}
		});

		Button huifu = (Button) convertView.findViewById(R.id.micropost_huifu);// 回复,用于显示隐藏的内容
		btlist.add(huifu);

		Button Button_huifu = (Button) convertView
				.findViewById(R.id.Button_huifu); // 隐藏内容中的回复按钮

		Button_huifu.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setButton_huifu(listView2, mess, Reply_edit);
			}
		});

		Log.i("linshi", IP + mess.getAvatar_url());
		// 设置头像
		if (mess.getAvatar_url().equals("")
				|| mess.getAvatar_url().equals("null")) {
		} else {
//			face.setScaleType(ImageView.ScaleType.FIT_XY);
			imageLoader.displayImage(IP + mess.getAvatar_url(), face, options,
					animateFirstListener);
		}

		Micropost_senderName.setText(mess.getName()); // 发消息的人
		Micropost_content.setText(mess.getContent()); // 消息内容
		Micropost_date.setText(divisionTime(mess.getCreated_at())); // 消息日期
		String mic_id = mess.getId();
		for (int j = 0; j < care.size(); j++) {
			String a = (String) care.get(j);
			if (a.equals(mic_id)) {
				guanzhu.setText("已关注"); // 显示 已关注
			}
		}
		/**
		 * 关注
		 */
		if (micropost_type == 1 || user_id.equals(mess.getUser_id().toString())) {
			guanzhu.setVisibility(View.GONE); // 自己的消息不关注
		}
		// 点击关注
		guanzhu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				add_concern(i, mess);
			}
		});
		/**
		 * 删除
		 */
		if (user_id.equals(mess.getUser_id())) { // 主消息删除按钮 只是在本人时显示
		} else {
			button1.setVisibility(View.GONE);
		}
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Dialog dialog = new AlertDialog.Builder(Classxinxiliu.this)
						.setTitle("提示")
						.setMessage("您确认要删除么?")
						.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										del_micropost(i, mess);

									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										dialog.dismiss();
									}
								}).create();

				dialog.show();

			}
		});
		final RelativeLayout layout1 = (RelativeLayout) convertView
				.findViewById(R.id.child_micropost); // 回复界面
		item_huifu.add(layout1);
		// 隐藏部分的内容
		huifu.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				setHuiFu(i, mess, layout1, Reply_edit, listView2, lookMore);
			}
		});
		if (lookStr != "" && i == focus) {
			setHuiFu(i, mess, layout1, Reply_edit, listView2, lookMore);
		}
		if (mess.getReply_microposts_count() != null) {
			huifu.setText(HomeWorkParams.REPLY + "("
					+ mess.getReply_microposts_count() + ")");
		}
		Linear_layout.addView(convertView);
	}

	// 加载更多
	public void onFooterRefresh(PullToRefreshView view) {
		// focus = -1;
		page = page + 1;
		if (page <= pages_count) {
			list_item = list.size();
			Thread thread = new Thread() {
				public void run() {// 全部 页面加载 更多
					if (micropost_type == 0) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("student_id", id);
						map.put("school_class_id", school_class_id);
						map.put("page", page + "");
						String result = "";
						try {
							result = HomeWorkTool.sendGETRequest(
									Urlinterface.GET_MICROPOSTS, map);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						Message msg = new Message();// 创建Message 对象
						msg.what = 3;
						msg.obj = result;
						handler.sendMessage(msg);
					}
					// 我的 页面加载 更多
					if (micropost_type == 1) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("user_id", user_id);
						map.put("school_class_id", school_class_id);
						map.put("page", page + "");
						String result = "";
						try {
							result = HomeWorkTool.sendGETRequest(
									Urlinterface.MY_MICROPOSTS, map);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						Message msg = new Message();// 创建Message 对象
						msg.what = 3;
						msg.obj = result;
						handler.sendMessage(msg);
					}
				}
			};

			if (HomeWorkTool.isConnect(Classxinxiliu.this)) {

				thread.start();
			} else {
				Toast.makeText(getApplicationContext(),
						HomeWorkParams.INTERNET, 0).show();
			}

			// if (page_own == 0) {
			// focus = -1;
			// micropostAdapter.notifyDataSetChanged();
			// onLoad();
			// }

		} else {
			Toast.makeText(getApplicationContext(), "已经是最后一页了..",
					Toast.LENGTH_SHORT).show();
			mPullToRefreshView.onFooterRefreshComplete();
		}

		// mPullToRefreshView.onFooterRefreshComplete();
	}

	// 刷新
	public void onHeaderRefresh(PullToRefreshView view) {
		shuaxin();
	}

	/*
	 * 子消息 适配器
	 */
	public class ZiAdapter extends BaseAdapter {

		public int getCount() {
			return child_list.size();// 数据总数
		}

		public Object getItem(int position2) {
			return child_list.get(position2);
		}

		public long getItemId(int position2) {
			return position2;
		}

		public View getView(final int position2, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = Classxinxiliu.this.getLayoutInflater();
			View child_view = inflater.inflate(R.layout.child_micropost_item,
					null);
			ImageView face = (ImageView) child_view
					.findViewById(R.id.child_user_face); // 头像
			TextView Micropost_whoToWho = (TextView) child_view
					.findViewById(R.id.child_message_senderName); // 张三 回复李四
			TextView Micropost_content = (TextView) child_view
					.findViewById(R.id.child_micropost_content); // 内容
			Button delete = (Button) child_view
					.findViewById(R.id.child_micropost_delete); // 删除
			Button reply = (Button) child_view
					.findViewById(R.id.child_micropost_huifu); // 回复
			final Child_Micropost child_Micropost = child_list.get(position2);
			if (child_Micropost.getSender_avatar_url() != null) { // 设置头像
//				face.setScaleType(ImageView.ScaleType.FIT_XY);
				imageLoader.displayImage(IP
						+ child_list.get(position2).getSender_avatar_url(),
						face, options, animateFirstListener);
			}
			Micropost_whoToWho.setText(child_Micropost.getSender_name()
					+ "  回复   " + child_Micropost.getReciver_name()); //
			Micropost_content.setText(child_Micropost.getContent() + " ("
					+ divisionTime(child_Micropost.getCreated_at()) + ")"); // 消息内容
			if (user_id.equals(child_Micropost.getSender_id())) {// 自己回复的帖子现实删除按钮
				delete.setVisibility(View.VISIBLE);
			}
			delete.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					final int item = position2;
					Log.i("linshi", item + "--------------");
					final Handler mHandler = new Handler() {
						public void handleMessage(android.os.Message msg) {
							switch (msg.what) {
							case 0:
								final String json8 = (String) msg.obj;
								if (json8.length() != 0) {
									JSONObject array;
									try {
										array = new JSONObject(json8);//
										String status = array
												.getString("status");
										String notice = array
												.getString("notice");
										if ("success".equals(status)) {
											child_list.remove(item);
											ziAdapter_list.get(focus)
													.notifyDataSetChanged();
											HomeWorkTool
													.setListViewHeightBasedOnChildren(list_list
															.get(focus));
											list.get(focus)
													.setReply_microposts_count(
															child_list.size()
																	+ "");
											btlist.get(focus).setText(
													HomeWorkParams.REPLY + "("
															+ child_list.size()
															+ ")");
										}
										Toast.makeText(getApplicationContext(),
												notice, Toast.LENGTH_SHORT)
												.show();
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
								break;
							default:
								break;
							}
						}
					};
					final Thread thread = new Thread() {
						public void run() {
							try {
								Map<String, String> map = new HashMap<String, String>();
								map.put("reply_micropost_id",
										child_Micropost.getId());
								String child_delete_json = HomeWorkTool.doPost(
										Urlinterface.DELETE_REPLY_POSTS, map);
								Message msg = new Message();// 创建Message 对象
								msg.what = 0;
								msg.obj = child_delete_json;
								mHandler.sendMessage(msg);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};

					Dialog dialog = new AlertDialog.Builder(Classxinxiliu.this)
							.setTitle("提示")
							.setMessage("您确认要删除么?")
							.setPositiveButton("确认",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

											if (HomeWorkTool
													.isConnect(Classxinxiliu.this)) {

												thread.start();
											} else {
												Toast.makeText(
														getApplicationContext(),
														HomeWorkParams.INTERNET,
														0).show();
											}

										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

											dialog.dismiss();
										}
									}).create();

					dialog.show();

				}
			});

			reply.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Reply_edit_list.get(focus).setHint(
							user_name + " 回复  "
									+ child_Micropost.getSender_name() + " :");
					reciver_id = child_Micropost.getSender_id();
					reciver_types = child_Micropost.getSender_types();
				}
			});
			return child_view;
		}
	}

	// 头像
	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	private void setJson(String json) {

		try {
			stuList= new ArrayList<ClassStuPojo>();
			JSONObject obj = new JSONObject(json);
			// 学生信息
			JSONObject student = obj.getJSONObject("student"); // 获得学生的信息
			// id = student.getString("id");
			// user_id = student.getString("user_id");
			avatar_url = student.getString("avatar_url"); // 获取本人头像昂所有在地址

			SharedPreferences preferences = getSharedPreferences(SHARED,
					Context.MODE_PRIVATE);
			Editor editor = preferences.edit();
			editor.putString("avatar_url", avatar_url);
			editor.commit();
			user_name = student.getString("name");
			nick_name = student.getString("nickname");

			// 微博
			JSONObject microposts = obj.getJSONObject("microposts");
			page = Integer.parseInt(microposts.getString("page"));
			pages_count = Integer.parseInt(microposts.getString("pages_count"));
			String details_microposts = microposts
					.getString("details_microposts");
			// page":1,"pages_count":2,"details_microposts":
			parseJson_details_microposts(details_microposts);
			
			// 班级头像和名字
			JSONObject class1 = obj.getJSONObject("class"); // 或得班级信息
			String class_name = class1.getString("name"); // 获取class_name
			classname = class1.getString("name");

			school_class_id = class1.getString("id");
			
			// 循环获取班级学生的信息classmates
//			Message msg = new Message();// 创建Message 对象
//			msg.what = 7;
//			msg.obj = school_class_id;
//			handler.sendMessage(msg);
//			handler.sendEmptyMessage(7);
//				JSONArray jsonArray = obj.getJSONArray("classmates");
//			for (int i = 0; i < jsonArray.length(); i++) {
//				JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
//				String stu_Url =  jsonObject2.getString("avatar_url");
//				int id = jsonObject2.getInt("id");
//				String stuname = jsonObject2.getString("name");
//				String nickname = jsonObject2.getString("nickname");
//				if (Integer.valueOf(user_id) == id) {
//
//				} else {
//					stuList.add(new ClassStuPojo(id, stuname, stu_Url, nickname));
//				}
//			}
			Log.i("linshi", stuList.size() + "");

			// 微博id
			care = new ArrayList<String>();
			JSONArray follow_microposts_id = obj
					.getJSONArray("follow_microposts_id");
			for (int i = 0; i < follow_microposts_id.length(); ++i) {
				String fmi = follow_microposts_id.getInt(i) + "";
				care.add(fmi);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void setSkipJson() {
		// 查看 跳到本界面的 处理操作

		care.clear();
		// page_own = 1;
		// focus = -1;
		micropost_type = 1;
		page = 1;
		// list = new ArrayList<Micropost>();
		json = "";
		Button b = (Button) findViewById(R.id.class_button_all);
		b.setBackgroundDrawable(getResources().getDrawable(R.drawable.an2));
		Button b2 = (Button) findViewById(R.id.class_button_myself);
		b2.setBackgroundDrawable(getResources().getDrawable(R.drawable.an));

		Thread thread = new Thread() {
			public void run() {
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("user_id", user_id);
					map.put("school_class_id", school_class_id);
					map.put("page", page + "");
					json = HomeWorkTool.sendGETRequest(
							Urlinterface.MY_MICROPOSTS, map);
					Message msg = new Message();// 创建Message 对象
					msg.what = 6;
					msg.obj = json;
					handler.sendMessage(msg);
					// handler.sendEmptyMessage(4);// 关闭prodialog
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		if (HomeWorkTool.isConnect(Classxinxiliu.this)) {

			thread.start();
		} else {
			Toast.makeText(getApplicationContext(), HomeWorkParams.INTERNET, 0)
					.show();
		}
		//
		//
		// Thread thread = new Thread() {
		// public void run() {
		// try {
		// Map<String, String> map = new HashMap<String, String>();
		// map.put("user_id", user_id);
		// map.put("school_class_id", school_class_id);
		// map.put("page", "1");
		// json = HomeWorkTool.sendGETRequest(
		// Urlinterface.MY_MICROPOSTS, map);
		// Message msg = new Message();// 创建Message 对象
		// msg.what = 6;
		// msg.obj = json;
		// handler.sendMessage(msg);
		// // handler.sendEmptyMessage(4);// 关闭prodialog
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// };
		// thread.start();

	}

	/*
	 * 解析 全部 模块中的 主消息
	 */
	void parseJson_all(String json3) {

		if ("error".equals(json3)) {
		} else {
			JSONObject array3;
			try {
				array3 = new JSONObject(json3);
				String status = array3.getString("status");
				String notice = array3.getString("notice");
				if ("success".equals(status)) {
					String micropostsListJson = array3.getString("microposts");
					JSONObject microposts = new JSONObject(micropostsListJson);
					page = Integer.parseInt(microposts.getString("page"));
					pages_count = Integer.parseInt(microposts
							.getString("pages_count"));
					String details_microposts = microposts
							.getString("details_microposts");
					JSONArray follow_microposts_id = array3
							.getJSONArray("follow_microposts_id");
					for (int i = 0; i < follow_microposts_id.length(); ++i) {
						String fmi = follow_microposts_id.getInt(i) + "";
						care.add(fmi);
					}
					parseJson_details_microposts(details_microposts);
				} else {
					Toast.makeText(getApplicationContext(), notice,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * 解析 我的 模块中的 主消息
	 */
	void parseJson_Myself(String json3) {

		if ("error".equals(json3)) {
		} else {
			JSONObject array;
			try {
				array = new JSONObject(json3);
				Boolean status = array.getBoolean("status");
				String notice = array.getString("notice");
				if (true == status) {
					String micropostsListJson = array
							.getString("details_microposts");
					page = Integer.parseInt(array.getString("page"));
					pages_count = Integer.parseInt(array
							.getString("pages_count"));
					parseJson_details_microposts(micropostsListJson);
				} else {
					Toast.makeText(getApplicationContext(), notice,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * 解析 json 中， "details_microposts" 部分的 数据
	 */

	void parseJson_details_microposts(String details_microposts) {

		JSONArray jsonArray2;
		try {
			jsonArray2 = new JSONArray(details_microposts);
			for (int i = 0; i < jsonArray2.length(); ++i) {
				JSONObject o = (JSONObject) jsonArray2.get(i);
				String micropost_id = o.getString("micropost_id");
				String user_id = o.getString("user_id");
				String user_types = o.getString("user_types");
				String name = o.getString("name");
				String content = o.getString("content");
				String avatar_url = o.getString("avatar_url");
				String created_at = o.getString("created_at");
				String reply_microposts_count = o
						.getString("reply_microposts_count");
				Micropost micropost = new Micropost(micropost_id, user_id,
						user_types, name, content, avatar_url, created_at,
						reply_microposts_count);
				list.add(micropost);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 解析 回复 模块中的 子消息
	 */
	void parseJson_childMicropost(String json3) {
		if ("error".equals(json3)) {
		} else {
			JSONObject array;
			try {
				array = new JSONObject(json3);
				String status = array.getString("status");
				String notice = array.getString("notice");
				if ("success".equals(status)) {
					String micropostsListJson = array
							.getString("reply_microposts");
					JSONObject microposts = new JSONObject(micropostsListJson);
					child_page = Integer.parseInt(microposts.getString("page"));
					child_pages_count = Integer.parseInt(microposts
							.getString("pages_count"));
					String reply_microposts = microposts
							.getString("reply_microposts");
					JSONArray jsonArray2 = new JSONArray(reply_microposts);
					for (int i = 0; i < jsonArray2.length(); ++i) {
						JSONObject o = (JSONObject) jsonArray2.get(i);
						String id = o.getString("id");
						String sender_id = o.getString("sender_id");
						String sender_types = o.getString("sender_types");
						String sender_name = o.getString("sender_name");
						String sender_avatar_url = o
								.getString("sender_avatar_url");
						String content = o.getString("content");
						String reciver_name = o.getString("reciver_name");
						// String reciver_avatar_url = o
						// .getString("reciver_avatar_url");
						String created_at = o.getString("created_at");
						Child_Micropost child = new Child_Micropost(id,
								sender_id, sender_types, sender_name,
								sender_avatar_url, content, reciver_name,
								created_at);
						child_list.add(child);
					}
				} else {
					Toast.makeText(getApplicationContext(), notice,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	// 分割时间
	public String divisionTime(String timeStr) {
		int temp1 = timeStr.indexOf("T");
		int temp2 = timeStr.lastIndexOf("+");
		return timeStr.substring(0, temp1) + " "
				+ timeStr.substring(temp1 + 1, temp2);
	}

	// 添加关注
	public void add_concern(final int i, final Micropost mess) {
		prodialog = new ProgressDialog(Classxinxiliu.this);
		if (guanzhu_list.get(i).getText().toString().equals("关注")) {
			prodialog.setMessage("正在添加关注");
		} else if (guanzhu_list.get(i).getText().toString()
				.equals("已关注")) {
			prodialog.setMessage("正在取消关注");
		}
		
		prodialog.show();
		final Handler gzHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				JSONObject jsonobject;
				prodialog.dismiss();
				switch (msg.what) {
				case 0:
					try {
						jsonobject = new JSONObject(msg.obj.toString());
						String status = jsonobject.getString("status");
						String notic = jsonobject.getString("notice");
						if (status.equals("success")) {
							care.add(mess.getId().toString());
							guanzhu_list.get(i).setText("已关注");
						}
						Toast.makeText(getApplicationContext(), notic,
								Toast.LENGTH_SHORT).show();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case 1:
					try {
						jsonobject = new JSONObject(msg.obj.toString());
						String status = jsonobject.getString("status");
						String notic = jsonobject.getString("notice");
						if (status.equals("success")) {
							care.remove(mess.getId().toString());
							guanzhu_list.get(i).setText("关注");
						}
						Toast.makeText(getApplicationContext(), notic,
								Toast.LENGTH_SHORT).show();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
		};
		Thread gzthread = new Thread() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void run() {
				try {

					HashMap<String, String> mp = new HashMap();
					mp.put("user_id", String.valueOf(user_id));
					mp.put("micropost_id", String.valueOf(mess.getId()));
					String str = null;
					Message msg = new Message();// 创建Message 对象
					if (guanzhu_list.get(i).getText().toString().equals("关注")) {
						str = HomeWorkTool.sendGETRequest(
								Urlinterface.add_concern, mp);
						msg.what = 0;
						msg.obj = str;
					} else if (guanzhu_list.get(i).getText().toString()
							.equals("已关注")) {
						str = HomeWorkTool.sendGETRequest(
								Urlinterface.unfollow, mp);
						msg.what = 1;
						msg.obj = str;
					}
					gzHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		if (HomeWorkTool.isConnect(Classxinxiliu.this)) {

			gzthread.start();
		} else {
			Toast.makeText(getApplicationContext(), HomeWorkParams.INTERNET, 0)
					.show();
		}

	}

	// 删除
	public void del_micropost(final int i, final Micropost mess) {
		prodialog = new ProgressDialog(Classxinxiliu.this);
		prodialog.setMessage("正在删除消息");
		prodialog.show();
		final Handler mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					final String json5 = (String) msg.obj;
					prodialog.dismiss();
					if (json5.equals("error")) {

					} else {
						JSONObject array;
						try {
							array = new JSONObject(json5);//
							String status = array.getString("status");
							String notice = array.getString("notice");

							if ("success".equals(status)) {
								// 删除成功的话,刷新界面
								focus = -1;
								list.remove(i);
								click_list();
								for (int i = 0; i < list.size(); i++) {
									setlayout(i);
								}
							}
							Toast.makeText(getApplicationContext(), notice,
									Toast.LENGTH_SHORT).show();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					break;
				default:
					break;
				}
			}
		};
		json = "";
		Thread thread = new Thread() {
			public void run() {
				try {

					Map<String, String> map = new HashMap<String, String>();
					map.put("micropost_id", mess.getId() + "");
					json = HomeWorkTool.sendGETRequest(
							Urlinterface.DELETE_POSTS, map);
					Message msg = new Message();// 创建Message 对象
					msg.what = 0;
					msg.obj = json;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		if (HomeWorkTool.isConnect(Classxinxiliu.this)) {

			thread.start();
		} else {
			Toast.makeText(getApplicationContext(), HomeWorkParams.INTERNET, 0)
					.show();
		}
	}

	// 回复
	public void setButton_huifu(final ListView listv, final Micropost mess,
			final EditText Reply_edit) {
		final Handler mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					final String json2 = (String) msg.obj;
					if (json2.length() == 0) {

					} else {
						JSONObject array2;
						try {
							array2 = new JSONObject(json2);//
							String status = array2.getString("status");
							String notice = array2.getString("notice");

							if ("success".equals(status)) {
								Reply_edit.setText("");
								Toast.makeText(getApplicationContext(), notice,
										Toast.LENGTH_SHORT).show();

								final Handler mHandler = new Handler() {
									public void handleMessage(
											android.os.Message msg) {
										switch (msg.what) {
										case 0:
											final String json7 = (String) msg.obj;
											child_list = new ArrayList<Child_Micropost>();
											parseJson_childMicropost(json7);
											list.get(focus)
													.setReply_microposts_count(
															child_list.size()
																	+ "");
											btlist.get(focus).setText(
													HomeWorkParams.REPLY + "("
															+ child_list.size()
															+ ")");

											listv.setAdapter(ziAdapter_list
													.get(focus));
											HomeWorkTool
													.setListViewHeightBasedOnChildren(listv);

											break;
										default:
											break;
										}
									}
								};
								Thread thread = new Thread() {
									public void run() {
										try {
											Map<String, String> map = new HashMap<String, String>();
											map.put("micropost_id",
													micropost_id);
											String js2 = HomeWorkTool
													.sendGETRequest(
															Urlinterface.get_reply_microposts,
															map);
											Message msg = new Message();// 创建Message
																		// 对象
											msg.what = 0;
											msg.obj = js2;
											mHandler.sendMessage(msg);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								};
								if (HomeWorkTool.isConnect(Classxinxiliu.this)) {

									thread.start();
								} else {
									Toast.makeText(getApplicationContext(), HomeWorkParams.INTERNET, 0)
											.show();
								}

							} else {
								Toast.makeText(getApplicationContext(), notice,
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					break;
				default:
					break;
				}
			}
		};

		String reply_edit = Reply_edit.getText().toString();
		String kongge = reply_edit.replaceAll(" ", "");
		if (reply_edit.length() == 0 || kongge.equals("")) {
			Toast.makeText(getApplicationContext(), "内容不能为空",
					Toast.LENGTH_SHORT).show();
		} else {
			Thread thread = new Thread() {
				public void run() {
					try {

						String reply_edit = Reply_edit.getText().toString();
						Map<String, String> map = new HashMap<String, String>();
						map.put("content", reply_edit);
						map.put("sender_id", user_id);
						map.put("sender_types", user_types + "");
						map.put("micropost_id", micropost_id);
						map.put("reciver_id", reciver_id);
						map.put("reciver_types", reciver_types);
						map.put("school_class_id", school_class_id);
						Log.i("bbb","sender_id:"+user_id+"sender_types"+user_types +"micropost_id"+micropost_id+"reciver_id"+reciver_id+"reciver_types"+reciver_types+"school_class_id"+school_class_id);
						String js1 = HomeWorkTool.doPost(
								Urlinterface.reply_message, map);
						Message msg = new Message();// 创建Message 对象
						msg.what = 0;
						msg.obj = js1;
						mHandler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			if (HomeWorkTool.isConnect(Classxinxiliu.this)) {

				thread.start();
			} else {
				Toast.makeText(getApplicationContext(), HomeWorkParams.INTERNET, 0)
						.show();
			}
		}
	}

	// 发表
	public void class_fabiao(View v) {
		final Handler class_fabiaoHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				prodialog.dismiss();
				switch (msg.what) {
				case 0:
					final String json1 = (String) msg.obj;
					if (json1.length() != 0) {
						JSONObject array1;
						try {
							array1 = new JSONObject(json1);//
							String status = array1.getString("status");
							String notice = array1.getString("notice");

							if ("success".equals(status)) {
								Toast.makeText(getApplicationContext(), notice,
										Toast.LENGTH_SHORT).show();
								list.clear();
								click_list();
								fabiao_content.setText("");
								shuaxin();
							} else {
								Toast.makeText(getApplicationContext(), notice,
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
					break;
				default:
					break;
				}
			}
		};

		final String fabiaoContents = fabiao_content.getText().toString();
		String kongge = fabiaoContents.replaceAll(" ", "");
		if (fabiaoContents.length() == 0 || kongge.equals("")) {
			Toast.makeText(getApplicationContext(), "内容不能为空",
					Toast.LENGTH_SHORT).show();
		} else {
			Thread thread = new Thread() {
				public void run() {
					try {

						Map<String, String> map = new HashMap<String, String>();
						map.put("content", fabiaoContents);
						map.put("user_id", user_id);
						map.put("user_types", user_types + "");
						map.put("school_class_id", school_class_id);
						String senderjson = HomeWorkTool.doPost(
								Urlinterface.NEWS_RELEASE, map);
						Message msg = new Message();// 创建Message 对象
						msg.what = 0;
						msg.obj = senderjson;
						class_fabiaoHandler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			prodialog = new ProgressDialog(Classxinxiliu.this);
			prodialog.setMessage("正在发表...");
			prodialog.show();
			if (HomeWorkTool.isConnect(Classxinxiliu.this)) {

				thread.start();
			} else {
				Toast.makeText(getApplicationContext(), HomeWorkParams.INTERNET, 0)
						.show();
			}
		}
	}

	// 回复隐藏变显示
	public void setHuiFu(int i, final Micropost mess, RelativeLayout layout1,
			EditText Reply_edit, final ListView listView2, final Button lookMore) {

		final Handler mHandler2 = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					final String i = (String) msg.obj;
					for (int j = 0; j < item_huifu.size(); j++) {
						if (j != Integer.parseInt(i)) {
							item_huifu.get(j).setVisibility(View.GONE);

						}
					}
					break;
				default:
					break;
				}
			}
		};

		child_list = new ArrayList<Child_Micropost>();
		if (gk_list.get(i) == true) {

			gk_list.set(i, false);
			// Message msg = new Message();// 创建Message
			// // 对象
			// msg.what = 0;
			// msg.obj = i;
			// mHandler2.sendMessage(msg);

			for (int j = 0; j < item_huifu.size(); j++) {
				if (j != i) {
					item_huifu.get(j).setVisibility(View.GONE);

				}
			}

			focus = i;
			micropost_id = mess.getId();// 点击 回复 默认 给主消息回复 记录 主消息 id
			reciver_id = mess.getUser_id();
			reciver_types = mess.getUser_types();
			layout1.setVisibility(View.VISIBLE);

			Reply_edit.setHint(user_name + " " + HomeWorkParams.REPLY + " "
					+ mess.getName() + ":");
			listView2.setVisibility(View.VISIBLE);

			int si = Integer.parseInt(list.get(focus)
					.getReply_microposts_count().toString());

//			if (si > 0 || !lookStr.equals("")) {
				prodialog = new ProgressDialog(Classxinxiliu.this);
				prodialog.setMessage("正在加载中");
				prodialog.show();

				final Handler mHandler = new Handler() {
					public void handleMessage(android.os.Message msg) {
						prodialog.dismiss();
						switch (msg.what) {
						case 0:
							if (child_list.size() > 0) {// 如果没有子消息，隐藏加载更多按钮
								lookMore.setVisibility(View.VISIBLE);
							} else {
								lookMore.setVisibility(View.GONE);
							}
							listView2.setAdapter(ziAdapter_list.get(focus));
							HomeWorkTool
									.setListViewHeightBasedOnChildren(listView2);
							break;
						default:
							break;
						}
					}
				};
				Thread thread = new Thread() {
					public void run() {
						try {
							Map<String, String> map = new HashMap<String, String>();
							map.put("micropost_id", mess.getId());
							String reply = HomeWorkTool.sendGETRequest(
									Urlinterface.get_reply_microposts, map);
							parseJson_childMicropost(reply);
							mHandler.sendEmptyMessage(0);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				if (HomeWorkTool.isConnect(Classxinxiliu.this)) {

					thread.start();
				} else {
					Toast.makeText(getApplicationContext(), HomeWorkParams.INTERNET, 0)
							.show();
				}
//			}
//			else {
//				if (child_list.size() > 0) {// 如果没有子消息，隐藏加载更多按钮
//					lookMore.setVisibility(View.VISIBLE);
//				} else {
//					lookMore.setVisibility(View.GONE);
//				}
//				listView2.setAdapter(ziAdapter_list.get(focus));
//				HomeWorkTool.setListViewHeightBasedOnChildren(listView2);
//			}

		} else {
			gk_list.set(i, true);
			layout1.setVisibility(View.GONE);
		}
	}

	// 子消息加载更多
	public void setlookMore(final ListView listview, final Micropost mess) {
		final Handler mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					final String json6 = (String) msg.obj;
					parseJson_childMicropost(json6);
					ziAdapter_list.get(focus).notifyDataSetChanged();
					HomeWorkTool.setListViewHeightBasedOnChildren(listview);
					break;
				default:
					break;
				}
			}
		};
		child_page = child_page + 1;
		Log.i("linshi", 1 + "/");
		if (child_page <= child_pages_count) {
			Log.i("linshi", 2 + "/");
			Log.i("linshi", child_page + "/");
			Thread thread = new Thread() {
				public void run() {
					try {
						Map<String, String> map = new HashMap<String, String>();
						map.put("micropost_id", mess.getId());
						map.put("page", child_page + "");
						String js = HomeWorkTool.sendGETRequest(
								Urlinterface.get_reply_microposts, map);
						Message msg = new Message();// 创建Message
													// 对象
						msg.what = 0;
						msg.obj = js;
						mHandler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			if (HomeWorkTool.isConnect(Classxinxiliu.this)) {

				thread.start();
			} else {
				Toast.makeText(getApplicationContext(), HomeWorkParams.INTERNET, 0)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "已是最后一页",
					Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * 点击 全部 时 ，触发该方法
	 */
	public void class_button_all(View v) {
		prodialog = new ProgressDialog(Classxinxiliu.this);
		prodialog.setMessage(HomeWorkParams.PD_CLASS_INFO);
		prodialog.show();
		list = new ArrayList<Micropost>();
		// page_own = 1;
		// focus = -1;
		micropost_type = 0;
		list.clear();
		Button b = (Button) findViewById(R.id.class_button_all);
		b.setBackgroundDrawable(getResources().getDrawable(R.drawable.an));
		Button b2 = (Button) findViewById(R.id.class_button_myself);
		b2.setBackgroundDrawable(getResources().getDrawable(R.drawable.an2));

		Thread thread = new Thread() {
			public void run() {
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("student_id", id);
					map.put("school_class_id", school_class_id);
					map.put("page", "1");
					String json = HomeWorkTool.sendGETRequest(
							Urlinterface.GET_MICROPOSTS, map);
					Message msg = new Message();// 创建Message 对象
					msg.what = 2;
					msg.obj = json;
					handler.sendMessage(msg);
					handler.sendEmptyMessage(4);// 关闭prodialog
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		if (HomeWorkTool.isConnect(Classxinxiliu.this)) {

			thread.start();
		} else {
			Toast.makeText(getApplicationContext(), HomeWorkParams.INTERNET, 0)
					.show();
		}
	}

	/*
	 * 点击 "我的" 时,触发该方法
	 */
	public void class_button_myself(View v) {
		prodialog = new ProgressDialog(Classxinxiliu.this);
		prodialog.setMessage(HomeWorkParams.PD_CLASS_INFO);
		prodialog.show();
		care.clear();
		// page_own = 1;
		// focus = -1;
		micropost_type = 1;
		page = 1;
		list = new ArrayList<Micropost>();
		json = "";
		Button b = (Button) findViewById(R.id.class_button_all);
		b.setBackgroundDrawable(getResources().getDrawable(R.drawable.an2));
		Button b2 = (Button) findViewById(R.id.class_button_myself);
		b2.setBackgroundDrawable(getResources().getDrawable(R.drawable.an));

		Thread thread = new Thread() {
			public void run() {
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("user_id", user_id);
					map.put("school_class_id", school_class_id);
					map.put("page", page + "");
					json = HomeWorkTool.sendGETRequest(
							Urlinterface.MY_MICROPOSTS, map);
					Message msg = new Message();// 创建Message 对象
					msg.what = 2;
					msg.obj = json;
					handler.sendMessage(msg);
					handler.sendEmptyMessage(4);// 关闭prodialog
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		if (HomeWorkTool.isConnect(Classxinxiliu.this)) {

			thread.start();
		} else {
			Toast.makeText(getApplicationContext(), HomeWorkParams.INTERNET, 0)
					.show();
		}
	}

	class get_class_info implements Runnable {
		public void run() {
			try {

				lookStr = hw.getNoselect_message();
				list.clear();
				if (lookStr != null && !lookStr.equals("")) {

					setSkipJson();

				} else {
					
					Map<String, String> map = new HashMap<String, String>();
					map.put("student_id", id);
					map.put("school_class_id", school_class_id);

					json = HomeWorkTool.sendGETRequest(
							Urlinterface.get_class_info, map);
					setJson(json);
					
					
					handler.sendEmptyMessage(0);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void shuaxin() {
		click_list();
		Thread thread = new Thread() {
			public void run() {// 获得第一页信息

				if (micropost_type == 0) {// 全部
					Map<String, String> map = new HashMap<String, String>();
					map.put("student_id", id);
					map.put("school_class_id", school_class_id);
					map.put("page", "1");
					try {

						String result = HomeWorkTool.sendGETRequest(
								Urlinterface.GET_MICROPOSTS, map);
						Message msg = new Message();// 创建Message 对象
						msg.what = 2;
						msg.obj = result;
						handler.sendMessage(msg);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				if (micropost_type == 1) { // 有关我的
					Map<String, String> map = new HashMap<String, String>();
					map.put("user_id", user_id);
					map.put("school_class_id", school_class_id);
					map.put("page", "1");
					String result = "";
					try {
						result = HomeWorkTool.sendGETRequest(
								Urlinterface.MY_MICROPOSTS, map);
						Message msg = new Message();// 创建Message 对象
						msg.what = 2;
						msg.obj = result;
						handler.sendMessage(msg);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		};
		if (HomeWorkTool.isConnect(Classxinxiliu.this)) {

			thread.start();
		} else {
			Toast.makeText(getApplicationContext(), HomeWorkParams.INTERNET, 0)
					.show();
		}
	}

	public void click_list() {
		Linear_layout.removeAllViews();
		Reply_edit_list.clear();
		 guanzhu_list.clear();
		gk_list.clear();
		btlist.clear();
		ziAdapter_list.clear();
	}

}
