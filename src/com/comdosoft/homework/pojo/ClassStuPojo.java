package com.comdosoft.homework.pojo;

public class ClassStuPojo 
{
	private int id;
	private String Student_Name;
	private String Head_portrait_Url;
	private String Nick_Name;
	public ClassStuPojo() {
	}	
	public ClassStuPojo(int id, String student_Name, String head_portrait_Url,
			String nick_Name) {
		super();
		this.id = id;
		Student_Name = student_Name;
		Head_portrait_Url = head_portrait_Url;
		Nick_Name = nick_Name;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNick_Name() {
		return Nick_Name;
	}
	public void setNick_Name(String nick_Name) {
		Nick_Name = nick_Name;
	}

	public String getHead_portrait_Url() {
		return Head_portrait_Url;
	}
	public void setHead_portrait_Url(String head_portrait_Url) {
		Head_portrait_Url = head_portrait_Url;
	}
	public String getStudent_Name() {
		return Student_Name;
	}
	public void setStudent_Name(String student_Name) {
		Student_Name = student_Name;
	}

}
