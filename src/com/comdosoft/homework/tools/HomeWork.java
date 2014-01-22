package com.comdosoft.homework.tools;

import java.util.List;

import android.app.Application;

import com.comdosoft.homework.pojo.ListeningPojo;
import com.comdosoft.homework.pojo.QuestionPojo;

public class HomeWork extends Application {
	private int Question_allNumber;
	private List<QuestionPojo> branch_questions;
	private List<ListeningPojo> question_list;
	private int question_id;
	private int question_index;
	private List<List<String>> question_history;
	private int p_q_package_id;
	private int q_package_id;
	private boolean work_history;// 查看历史开关
	private int mainItem;
	private int user_id;
	private int class_id;
	private int message_id;
	private String noselect_message;
	private int history_item;
	public String getNoselect_message() {
		return noselect_message;
	}

	public void setNoselect_message(String noselect_message) {
		this.noselect_message = noselect_message;
	}

	public HomeWork() {
		this.setQuestion_index(0);
		this.setHistory_item(0);
		this.setMainItem(0);
		this.setMessage_id(-1);
	}

	public int getQuestion_index() {
		return question_index;
	}

	public void setQuestion_index(int question_index) {
		this.question_index = question_index;
	}

	public int getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}

	public List<QuestionPojo> getBranch_questions() {
		return branch_questions;
	}

	public void setBranch_questions(List<QuestionPojo> branch_questions) {
		this.branch_questions = branch_questions;
	}

	public int getQuestion_allNumber() {
		return Question_allNumber;
	}

	public void setQuestion_allNumber(int question_allNumber) {
		Question_allNumber = question_allNumber;
	}

	public List<ListeningPojo> getQuestion_list() {
		return question_list;
	}

	public void setQuestion_list(List<ListeningPojo> question_list) {
		this.question_list = question_list;
	}

	public List<List<String>> getQuestion_history() {
		return question_history;
	}

	public void setQuestion_history(List<List<String>> question_history) {
		this.question_history = question_history;
	}

	public boolean isWork_history() {
		return work_history;
	}

	public void setWork_history(boolean work_history) {
		this.work_history = work_history;
	}

	public int getMainItem() {
		return mainItem;
	}

	public void setMainItem(int mainItem) {
		this.mainItem = mainItem;
	}

	public int getP_q_package_id() {
		return p_q_package_id;
	}

	public void setP_q_package_id(int p_q_package_id) {
		this.p_q_package_id = p_q_package_id;
	}

	public int getQ_package_id() {
		return q_package_id;
	}

	public void setQ_package_id(int q_package_id) {
		this.q_package_id = q_package_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getClass_id() {
		return class_id;
	}

	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}

	public int getMessage_id() {
		return message_id;
	}
	public void setMessage_id(int message_id) {
		this.message_id = message_id;
	}

	public int getHistory_item() {
		return history_item;
	}

	public void setHistory_item(int history_item) {
		this.history_item = history_item;
	}

}
