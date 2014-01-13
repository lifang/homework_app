package com.comdosoft.homework;

import java.util.ArrayList;
import java.util.List;

import com.comdosoft.homework.adapter.MainClssStuAdapter;
import com.comdosoft.homework.pojo.ClassStuPojo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Main_classes extends Activity 
{
	private int width;
	private int height;
	private LinearLayout main_class_classesll;
	private LinearLayout main_class_classesll2;	
	private LinearLayout main_class_oneIVll;
	private ImageView main_class_oneIV;
	private TextView main_class_classesTv;
	private TextView main_class_oneTv1;
	private TextView main_class_oneTv2;
	private GridView main_class_classGv; 
	private List<ClassStuPojo> stuList=new ArrayList<ClassStuPojo>();
	private String ceshiJson="{\"status\":\"success\",\"notice\":\"\u767b\u9646\u6210\u529f\uff01\",\"student\":{\"id\":1,\"name\":\"\u5f20\u4e09\",\"nickname\":\"\u4e0a\u5584\u82e5\u6c34\",\"avatar_url\":\"/homework_manage/public/homework_system/avatars/students/student_1.png\"},\"class\":{\"id\":1,\"name\":\"\u73ed\u7ea71\",\"tearcher_name\":\"2222\",\"tearcher_id\":1},\"classmates\":[{\"avatar_url\":\"/homework_manage/public/homework_system/avatars/students/student_1.png\",\"id\":1,\"name\":\"\u5f20\u4e09\",\"nickname\":\"\u4e0a\u5584\u82e5\u6c34\"},{\"avatar_url\":\"homework_system/avatars/students/student_2.png\",\"id\":2,\"name\":\"\u5f20\u4e09\",\"nickname\":\"\u4e0a\u5584\u82e5\u6c341\"},{\"avatar_url\":\"/ulrrr/kkkk\",\"id\":3,\"name\":\"\u5f20\u4e09\",\"nickname\":\"\u4e0a\u5584\u82e5\u6c342\"},{\"avatar_url\":\"/ulrrr/kkkk\",\"id\":4,\"name\":\"\u5f20\u4e09\",\"nickname\":\"\u4e0a\u5584\u82e5\u6c343\"}],\"task_messages\":[],\"microposts\":{\"page\":1,\"pages_count\":2,\"details_microposts\":[{\"avatar_url\":\"121111\",\"content\":\"1\",\"created_at\":\"2014-01-09T10:54:21+08:00\",\"id\":1,\"name\":\"2222\",\"nickname\":null,\"user_id\":1,\"user_types\":0},{\"avatar_url\":\"121111\",\"content\":\"9\",\"created_at\":\"2014-01-09T10:54:21+08:00\",\"id\":9,\"name\":\"2222\",\"nickname\":null,\"user_id\":1,\"user_types\":0},{\"avatar_url\":\"121111\",\"content\":\"10\",\"created_at\":\"2014-01-09T10:54:21+08:00\",\"id\":10,\"name\":\"2222\",\"nickname\":null,\"user_id\":1,\"user_types\":0},{\"avatar_url\":\"avatar_teacher1\",\"content\":\"2\",\"created_at\":\"2014-01-09T10:54:26+08:00\",\"id\":2,\"name\":\"teacher1\",\"nickname\":null,\"user_id\":2,\"user_types\":0},{\"avatar_url\":\"avatar_teacher2\",\"content\":\"3\",\"created_at\":\"2014-01-09T10:54:29+08:00\",\"id\":3,\"name\":\"teacher2\",\"nickname\":null,\"user_id\":3,\"user_types\":0},{\"avatar_url\":\"avatar_teacher3\",\"content\":\"4\",\"created_at\":\"2014-01-09T10:54:30+08:00\",\"id\":4,\"name\":\"teacher3\",\"nickname\":null,\"user_id\":4,\"user_types\":0},{\"avatar_url\":\"avatar_teacher3\",\"content\":\"5\",\"created_at\":\"2014-01-09T10:54:31+08:00\",\"id\":5,\"name\":\"teacher3\",\"nickname\":null,\"user_id\":4,\"user_types\":0},{\"avatar_url\":\"avatar_teacher6\",\"content\":\"6\",\"created_at\":\"2014-01-09T10:54:31+08:00\",\"id\":6,\"name\":\"teacher6\",\"nickname\":null,\"user_id\":7,\"user_types\":0},{\"avatar_url\":\"avatar_teacher5\",\"content\":\"7\",\"created_at\":\"2014-01-09T10:54:32+08:00\",\"id\":7,\"name\":\"teacher5\",\"nickname\":null,\"user_id\":6,\"user_types\":0},{\"avatar_url\":\"121111\",\"content\":\"8\",\"created_at\":\"2014-01-09T10:54:33+08:00\",\"id\":8,\"name\":\"2222\",\"nickname\":null,\"user_id\":1,\"user_types\":0}]},\"daily_tasks\":{\"dealing_tasks\":[],\"unfinish_tasks\":[{\"end_time\":\"2014-01-10T02:35:46+08:00\",\"id\":1,\"name\":\"package1\",\"question_packages_url\":\"31312312313123\",\"status\":0},{\"end_time\":\"2014-01-10T02:35:46+08:00\",\"id\":2,\"name\":\"package2\",\"question_packages_url\":\"1111\",\"status\":0}],\"finish_tasks\":[]}}";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_class_classes);
		Display display=this.getWindowManager().getDefaultDisplay();
		width=display.getWidth();
		height=display.getHeight();
		stuList.add(new ClassStuPojo(1, "MIA", "192.168.1.125", "蔡莹"));
		stuList.add(new ClassStuPojo(1, "LIA", "192.168.1.125", "蔡莹"));
		stuList.add(new ClassStuPojo(1, "LIA", "192.168.1.125", "蔡莹"));
		stuList.add(new ClassStuPojo(1, "LIA", "192.168.1.125", "蔡莹"));
		
		setView();
	}
	public void setView()
	{
		main_class_oneIVll=(LinearLayout) findViewById(R.id.main_class_classes_include).findViewById(R.id.main_class_oneIVll);
		main_class_oneIVll.setLayoutParams(new LinearLayout.LayoutParams(394, (int)(height*0.25*0.6)));
		main_class_oneIVll.setPadding(0, (int)(height*0.25*0.2) , 0, 0);
		main_class_oneIVll.setGravity(Gravity.CENTER);
		main_class_oneIV=(ImageView) findViewById(R.id.main_class_classes_include).findViewById(R.id.main_class_oneIV);
		main_class_oneIV.setLayoutParams(new LinearLayout.LayoutParams(182, 182));
		main_class_oneTv1=(TextView)findViewById(R.id.main_class_classes_include).findViewById(R.id.main_class_oneTv1);
		main_class_oneTv1.setText("gg");
		main_class_oneTv2=(TextView)findViewById(R.id.main_class_classes_include).findViewById(R.id.main_class_oneTv2);
		main_class_oneTv2.setText("GSGG");
		main_class_classesll=(LinearLayout) findViewById(R.id.main_class_classesll);
		main_class_classesll.setLayoutParams(new LinearLayout.LayoutParams(394, (int)(height*0.75*0.1)));
		main_class_classesTv=(TextView) findViewById(R.id.main_class_classesTv);
		main_class_classesTv.setWidth(182);
		main_class_classesll2=(LinearLayout) findViewById(R.id.main_class_classesll2);
		main_class_classesll2.setLayoutParams(new LinearLayout.LayoutParams(394, (int)(height*0.75*0.9)));
		main_class_classGv=(GridView) findViewById(R.id.main_class_classGv);
		main_class_classGv.setLayoutParams(new LinearLayout.LayoutParams(394, (int)(height*0.75*0.9)));
		main_class_classGv.setNumColumns(3);
		main_class_classGv.setAdapter(new MainClssStuAdapter(getApplicationContext(),stuList,width,height));
	}
}
