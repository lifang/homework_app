package com.comdosoft.homework.tools;



public interface Urlinterface {
	static final String tag = "homework";
	static final String SHARED = "HW";

<<<<<<< HEAD
	static final String IP = "http://192.168.2.55:3004";
	static final String IP2="http://192.168.0.130:3000/";
=======
	static final String IP = "http://192.168.0.101:3004";
>>>>>>> 505cf62b84b92934043051807763f2d674066a76
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
	 
<<<<<<< HEAD
	 String get_class=IP2+"/api/students/get_my_classes";		//获得所有班级

=======
>>>>>>> 505cf62b84b92934043051807763f2d674066a76
}