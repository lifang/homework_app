package com.comdosoft.homework.pojo;

public class QuestionCasePojo {

	private int id;
	private int Count_all;
	private int Count_over;
	private String end_time;
	private String create_time;
	private int type;

	public QuestionCasePojo() {
	}

	public QuestionCasePojo(int id, int count_all, int count_over,
			String end_time, String create_time, int type) {
		this.id = id;
		Count_all = count_all;
		Count_over = count_over;
		this.end_time = end_time;
		this.create_time = create_time;
		this.type = type;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
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

}
