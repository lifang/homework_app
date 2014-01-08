package com.comdosoft.homework.pojo;

public class QuestionCasePojo {

	private int id;
	private int Count_all;
	private int Count_over;
	private int second;
	private String end_time;
	private int type;

	public QuestionCasePojo() {
	}


	public QuestionCasePojo(int id, int count_all, int count_over, int second,
			String end_time, int type) {
		super();
		this.id = id;
		Count_all = count_all;
		Count_over = count_over;
		this.second = second;
		this.end_time = end_time;
		this.type = type;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCount_all() {
		return Count_all;
	}

	public void setCount_all(int count_all) {
		Count_all = count_all;
	}

	public int getCount_over() {
		return Count_over;
	}

	public void setCount_over(int count_over) {
		Count_over = count_over;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


	public int getSecond() {
		return second;
	}


	public void setSecond(int second) {
		this.second = second;
	}

}
