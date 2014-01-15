package com.comdosoft.homework.tools;

public interface Urlinterface {
	static final String tag = "homework";
	static final String SHARED = "HW";
//	static final String IP = "http://192.168.0.250:3008";
//	static final String IP = "http://192.168.0.101:3004/";
	static final String IP = "http://192.168.0.130:3000/";
	
	//获取班级每日任务等信息
	static final String CLASS_INFO = IP + "/api/students/get_class_info";
	//提交作业
	static final String FINISH_QUESTION_PACKGE = IP + "/api/students/finish_question_packge";
	//记录每一题
	static final String RECORD_ANSWER_INFO = IP + "/api/students/record_answer_info";
	//加载作业题目
	static final String INTO_DAILY_TASKS = IP + "/api/students/into_daily_tasks";
}