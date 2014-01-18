package com.comdosoft.homework.pojo;

public class WorkPojo {

	private int id;
	private String start_time;
	private String end_time;
	private String name;
	private String question_packages_url;
	private String listening_schedule;
	private String reading_schedule;
	private boolean type;

	public WorkPojo() {
	}

	public WorkPojo(int id, String start_time, String end_time, String name,
			String question_packages_url, String listening_schedule,
			String reading_schedule, boolean type) {
		this.id = id;
		this.start_time = start_time;
		this.end_time = end_time;
		this.name = name;
		this.question_packages_url = question_packages_url;
		this.listening_schedule = listening_schedule;
		this.reading_schedule = reading_schedule;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public boolean isType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuestion_packages_url() {
		return question_packages_url;
	}

	public void setQuestion_packages_url(String question_packages_url) {
		this.question_packages_url = question_packages_url;
	}

	public String getListening_schedule() {
		return listening_schedule;
	}

	public void setListening_schedule(String listening_schedule) {
		this.listening_schedule = listening_schedule;
	}

	public String getReading_schedule() {
		return reading_schedule;
	}

	public void setReading_schedule(String reading_schedule) {
		this.reading_schedule = reading_schedule;
	}

}
