package com.comdosoft.homework.pojo;

import java.sql.Date;

public class AboutMePojo 
{
	private int id;							//消息列表中id
	private int micropost_id;				//消息id
	private int user_id;					//登陆id
	private String sender_avatar_url	;   //头像所在 url
	private String sender_name;				//姓名
	private String status;					//回复的状态，是日志，还是状态，还是关注
	private String content;					//消息的内容
	private String created_at;				//日期

	public AboutMePojo() {
	}
	public AboutMePojo(int id, int micropost_id, int user_id,
			String sender_avatar_url, String sender_name, String status, String content, String created_at) {
		this.id = id;
		this.micropost_id = micropost_id;
		this.user_id = user_id;
		this.sender_avatar_url = sender_avatar_url;
		this.sender_name = sender_name;
		this.status = status;
		this.content = content;
		this.created_at = created_at;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMicropost_id() {
		return micropost_id;
	}

	public void setMicropost_id(int micropost_id) {
		this.micropost_id = micropost_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getSender_avatar_url() {
		return sender_avatar_url;
	}

	public void setSender_avatar_url(String sender_avatar_url) {
		this.sender_avatar_url = sender_avatar_url;
	}

	public String getSender_name() {
		return sender_name;
	}

	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

}
