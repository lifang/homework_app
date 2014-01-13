package com.comdosoft.homework.tools;



public interface Urlinterface {
	static final String tag = "homework";
	static final String SHARED = "HW";
	static final String IP = "http://192.168.0.101:3004";
	// static final String HEADIMG = IP + "/user_head_img/";
	static final String NEWS_RELEASE = IP + "/api/students/news_release";  //  发表消息
	 String UPLOAD_FACE = IP + "/api/students/upload_avatar";  //  上传头像
	 String get_reply_microposts = IP + "/api/students/get_reply_microposts";  //  获得  子信息
	
	 String get_class_info = IP + "/api/students/get_class_info";  //  获得  班级信息
	 String reply_message = IP + "/api/students/reply_message";  //  回复  获得  班级信息
	 
	 
}