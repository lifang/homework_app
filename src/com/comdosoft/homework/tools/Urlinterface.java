package com.comdosoft.homework.tools;

public interface Urlinterface {
	static final String tag = "homework";
	static final String SHARED = "HW";


//  static final String IP = "http://192.168.0.101:3004";
	  String IP = "http://192.168.0.250:3004";

	// static final String IP = "http://192.168.0.101:3004";
//	 static final String IP = "http://192.168.0.127:3000";

	// 获取班级每日任务等信息
	static final String CLASS_INFO = IP + "/api/students/get_class_info";
	// 提交作业
	static final String FINISH_QUESTION_PACKGE = IP
			+ "/api/students/finish_question_packge";
	// 记录每一题
	static final String RECORD_ANSWER_INFO = IP
			+ "/api/students/record_answer_info";
	// 加载作业题目
	static final String INTO_DAILY_TASKS = IP
			+ "/api/students/into_daily_tasks";

	// static final String IP = "http://192.168.0.101:3004";


	// static final String HEADIMG = IP + "/user_head_img/";
	static final String NEWS_RELEASE = IP + "/api/students/news_release";  //  发表消息
	//	 String UPLOAD_FACE = IP + "/api/students/upload_avatar";  //  上传头像
	String get_reply_microposts = IP + "/api/students/get_reply_microposts";  //  获得  子信息
	String get_class_info = IP + "/api/students/get_class_info";  //  获得  班级信息
	String reply_message = IP + "/api/students/reply_message";  //  回复 信息
	String MODIFY_PERSON_INFO = IP + "/api/students/modify_person_info";  //  修改个人信息 
	String RECORD_PERSON_INFO = IP + "/api/students/record_person_info";  //  登记个人信息
	//     李芳: http://192.168.0.130:3000/api/students/delete_posts
	String DELETE_POSTS = IP + "/api/students/delete_posts";  //  删除主消息
	String GET_MICROPOSTS = IP + "/api/students/get_microposts";  //  分页 获取  主消息
	String get_class=IP+"/api/students/get_my_classes";		//获得所有班级
	String Validation_into_class=IP+"/api/students/validate_verification_code";		//输入验证码，进入班级
	String get_News=IP+"/api/students/get_messages";							//获取所有的新消息
	String DELETE_REPLY_POSTS = IP + "/api/students/delete_reply_microposts"; // 删除
																				// 子消息

	String MY_MICROPOSTS = IP + "/api/students/my_microposts"; // 分页 获取 子消息
	String delete_message=IP+"/api/students/delete_message";		//删除消息

}
