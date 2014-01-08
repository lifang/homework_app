package com.comdosoft.homework.pojo;



import android.R.integer;

public class Micropost
{
	


	private String id;  //  信息  id
	private String  user_id;//创建者id
	private String user_types;//创建者类型
	private String  name;//主消息的创建者名字，
	private String  nickname;//主消息的创建者昵称
	private String  content;  //  消息内容
	private String avatar_url; //创建者头像
	private Long  created_at;//创建时间
	
	public Micropost(){}

	public Micropost(String id, String user_id, String user_types, String name,
			String nickname, String content, String avatar_url, Long created_at) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.user_types = user_types;
		this.name = name;
		this.nickname = nickname;
		this.content = content;
		this.avatar_url = avatar_url;
		this.created_at = created_at;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_types() {
		return user_types;
	}

	public void setUser_types(String user_types) {
		this.user_types = user_types;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAvatar_url() {
		return avatar_url;
	}

	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}

	public Long getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Long created_at) {
		this.created_at = created_at;
	}
	

	
}
