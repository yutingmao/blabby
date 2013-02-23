package com.nineteeneightyeight.entity;

/**
 * 系统用户实体类，负责保存和返回用户信息
 * 
 * @author flytreeleft
 */
public class UserEntity {
	// 用户ID
	private int id = -1;
	// 用户名
	private String name = "";
	// 用户的真实姓名
	private String realName = "";
	// 用户性别
	private int gender = 0;
	// 用户密码
	private String pwd = "";
	// 用户出生日期
	private String birthday = "";
	// 用户所在地地址
	private String address = "";
	// 用户简介
	private String brief = "";

	public UserEntity() {
		super();
	}

	public UserEntity(int id) {
		super();
		this.id = id;
	}

	public UserEntity(int id, String name, int gender, String pwd) {
		super();
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.pwd = pwd;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRealname() {
		return (realName != null && !realName.isEmpty() ? realName : "Unknown");
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}
}
