package com.comdosoft.homework.pojo;

public class WorkDatePojo {

	private int id;
	private String time;
	private int type;

	public WorkDatePojo() {
	}

	public WorkDatePojo(int id, String time, int type) {
		this.id = id;
		this.time = time;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
