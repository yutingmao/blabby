DROP DATABASE IF EXISTS MicroBlogDB; /* 如果存在MicroBlogDB数据库，则删除 */
CREATE DATABASE MicroBlogDB CHARACTER SET=UTF8; /* 创建数据库 */
USE MicroBlogDB; /* 打开数据库 */

DROP TABLE IF EXISTS CommentTable;
DROP TABLE IF EXISTS StatusTable;
DROP TABLE IF EXISTS AttentionTable;
DROP TABLE IF EXISTS UserTable;
/* 创建数据库表 */

/* 1、创建用户表 */
CREATE TABLE UserTable(
	id int AUTO_INCREMENT PRIMARY KEY,/* 代理主键，自动编号 */
	name char(20) UNIQUE NOT NULL,/* 用户名，唯一 */
	realname varchar(20), /*用户真实姓名*/
	gender int DEFAULT '0' CHECK(gender='0' OR gender='1'),/* 性别，0为男，1为女，默认为0 */
	pwd char(15)  NOT NULL,/* 密码 */  
	birthday char(10),/* 出生日期 */
	address varchar(50), /* 地址 */
	brief varchar(200) /* 简介 */
)ENGINE=InnoDB CHARSET=UTF8;

/* 2、创建关注表 */
CREATE TABLE AttentionTable(
	viewer_id int,/* 关注者主键 */
	viewed_id int,/* 被关注者主键 */
	PRIMARY KEY(viewer_id,viewed_id),
	FOREIGN KEY(viewer_id) REFERENCES UserTable(id),
	FOREIGN KEY(viewed_id) REFERENCES UserTable(id)
)ENGINE=InnoDB CHARSET=UTF8;

/* 3、创建状态表 */
CREATE TABLE StatusTable(
	id int AUTO_INCREMENT PRIMARY KEY,/* 代理主键，自动编号 */
	user_id int, /* 用户主键 */
	publish_date char(20) NOT NULL, /* 发布日期 */
	content varchar(140) NOT NULL, /* 状态内容，最多140个中文字符 */
	FOREIGN KEY(user_id) REFERENCES UserTable(id)
)ENGINE=InnoDB CHARSET=UTF8;

/* 4、创建评论表 */
CREATE TABLE CommentTable(
	id int AUTO_INCREMENT PRIMARY KEY, /* 代理主键，自动编号 */
	status_id int, /* 被评论状态的主键 */
	from_id int , /* 评论用户的主键 */
	to_id int, /* 被评论用户的主键 */
	comment_date char(20) NOT NULL, /* 评论日期 */
	comment varchar(140) NOT NULL, /* 评论内容，最多140个中文字符 */
	FOREIGN KEY(status_id) REFERENCES StatusTable(id),
	FOREIGN KEY(from_id) REFERENCES UserTable(id),
	FOREIGN KEY(to_id) REFERENCES UserTable(id)
)ENGINE=InnoDB CHARSET=UTF8;

/* 5、创建活跃度表视图,活跃度指发表状态数与评论他人状态数之和,但不包括对自己的状态的评论 */
DROP VIEW IF EXISTS ActiveTable;

CREATE VIEW ActiveTable(id) AS select user_id from StatusTable union all select from_id from CommentTable where from_id != to_id;

insert into UserTable (name,realname,gender,pwd,birthday,address,brief) values("default","default",0,"default","2010-10-19","default","default");