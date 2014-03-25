package com.comdosoft.homework.pojo;

import java.util.List;

public class ListHistoryPojo {
	private int status;
	private String update_time;
	private int questions_item;
	private int branch_item;
	private int use_time;
	private List<HistoryPojo> historyList;

	public ListHistoryPojo() {
		super();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public int getQuestions_item() {
		return questions_item;
	}

	public void setQuestions_item(int questions_item) {
		this.questions_item = questions_item;
	}

	public int getBranch_item() {
		return branch_item;
	}

	public void setBranch_item(int branch_item) {
		this.branch_item = branch_item;
	}

	public int getUse_time() {
		return use_time;
	}

	public void setUse_time(int use_time) {
		this.use_time = use_time;
	}

	public List<HistoryPojo> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<HistoryPojo> historyList) {
		this.historyList = historyList;
	}

}
