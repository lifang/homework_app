package com.comdosoft.homework.tools;

import java.util.List;

import android.app.Application;

import com.comdosoft.homework.pojo.ListeningPojo;
import com.comdosoft.homework.pojo.QuestionPojo;

public class HomeWork extends Application {
	private int Question_allNumber;
	private List<QuestionPojo> branch_questions;
	private int branch_question_id;
	private int question_index;

	public HomeWork() {
		this.setQuestion_index(0);
	}

	public int getQuestion_index() {
		return question_index;
	}

	public void setQuestion_index(int question_index) {
		this.question_index = question_index;
	}


	public int getBranch_question_id() {
		return branch_question_id;
	}

	public void setBranch_question_id(int branch_question_id) {
		this.branch_question_id = branch_question_id;
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

}
