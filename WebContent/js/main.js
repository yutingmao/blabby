//登录
function login(){
	var name = $("#user_name").val();
	var pass = $("#user_pass").val();
	
	if(name == ''){
		$("#err").html('<span style="color: red;font-size: 12px;">请输入用户名weibo!</span>');
		$("#user_name").focus();
		return;
	}
	if(pass == ''){
		$("#err").html('<span style="color: red;font-size: 12px;">请输入密码!</span>');
		$("#user_pass").focus();
		return;
	}
	//登录系统
	ajaxLogAndReg("log",name,pass);
}

//注册
function register(){
	var name = $("#user_name").val();
	var pass = $("#user_pass").val();
	var rpass = $("#user_rpass").val();
	var patrn = /^[\x00-\xFF]+$/;
	
	name = $.trim(name);
	$("#user_name").val(name);

	if(name == ''){
		$("#err").html('<span style="color: red;font-size: 12px;">请输入用户名!</span>');
		$("#user_name").focus();
		return;
	}
	if(!patrn.test(name)){
		$("#err").html('<span style="color: red;font-size: 12px;">用户名只能为英文字符!</span>');
		$("#user_name").val("").focus();
		return;
	}
	if(pass == ''){
		$("#err").html('<span style="color: red;font-size: 12px;">请输入密码!</span>');
		$("#user_pass").focus();
		return;
	}
	if(pass != rpass){
		$("#err").html('<span style="color: red;font-size: 12px;">新密码与确认密码不符!</span>');
		$("#user_pass").val("").focus();
		$("#user_rpass").val("");
		return;
	}
	//注册系统
	ajaxLogAndReg("reg",name,pass);
}
//ajax登录和注册操作
function ajaxLogAndReg(type,name,password){
	$.ajax( {
		url : 'UserAction?action='+type,
		type : 'POST',
		timeout : 5000,
		data : {
			'name' : name,
			'password' : password
		},
		dataType:'json',
		beforeSend : function() {
			// 等待过程中显示等待画面
			$("#wait").removeClass("hidden");
		},
		success : function(data,status) {
			
			if(typeof(data) != 'undefined'){
				$("#wait").addClass("hidden");
				
				if(data.error != '' && typeof(data.error) != 'undefined'){
					$("#err").html('<span style="color: red;font-size: 12px;">'+data.error+'</span>');
				}
				if(data.url != '' && typeof(data.url) != 'undefined'){
					window.location.href = data.url;
				}
				if(data.msg != '' && typeof(data.msg) != 'undefined'){
					$("#err").html(data.msg);
				}
			}
		},
		error : function(data,status,e) {
			$("#wait").addClass("hidden");
			$("#err").html('<span style="color: red;font-size: 12px;">服务器连接异常!</span>');
		}
	});
}
//获取表情名称
function getFace(obj){
	$("#publishtext").append("["+$(obj).attr("emotion")+"]");
}
//显示表情
function showEmotion(id){
	if($("#"+id).html() == ''){
		$.ajax( {
			url : 'face.jsp',
			type : 'POST',
			timeout : 5000,
			success : function(data) {
				$("#"+id).html(data);
			}
		});
	}
	//如果表情隐藏则显示,否则隐藏
	$("#"+id).toggleClass("hidden");
}
//广播消息
function sendMessage(){
	var msg = $("#broadcasttext")[0].value;
	
	msg = $.trim(msg);
	if(msg.length <= 0){
		$("#broadcasttext").val("");
		alert("广播内容不能为空或空格!");
		$("#broadcasttext").focus();
		return;
	}
	$.ajax( {
		url : 'BroadcastAction?action=send',
		type : 'POST',
		timeout : 5000,
		data : {
			'msg' : msg
		},
		success : function(data) {
			$("#broadcasttext")[0].value = "";
		}
	});
}
//显示广播消息,使用的是DWR
function showMessage(msg){
	var msgBox = document.createElement("div");
	var elem = document.documentElement;
	/*var user = $("#blog_user_name").html(); //获取用户名称
	
	if(user == null || user == '' || msg.substring(0,msg.indexOf(":",0)) == user){
		// 发送消息的一方为自己,则不显示,msg格式为"用户名"+":"+"消息"
		return;
	}*/
	msgBox.innerHTML = msg/*.substring(msg.indexOf(":",0)+1,msg.length)*/;
	msgBox.style.cssText = "visibility:hidden; position: fixed; width: 200px;text-align:center; font-size: 14px; background-color:#eee;" +
			"padding: 20px 0px; filter:alpha(opacity=80); opacity:0.8; top:" + (elem.clientHeight-100)/2 + "px;" +
			"left:" + (elem.clientWidth-200)/2 + "px; border:4px solid #ccc; _position: absolute;_top: expression(documentElement.scrollTop +" + (elem.clientHeight-100)/2 + "+ 'px');_z-index:200;";
	document.body.appendChild(msgBox);
	msgBox.style.visibility = "visible";
	setTimeout(function(){document.body.removeChild(msgBox);}, 10000);
}
//添加关注
function addAttention(viewerId,viewedId){
//	alert(viewerId+" "+viewedId);
	$.ajax( {
		url : 'AttentionAction?action=add',
		type : 'POST',
		timeout : 5000,
		data : {
			'viewerid' : viewerId,
			'viewedid' : viewedId
		},
		success : function(data) {
			alert(data);
		}
	});
}
//取消关注
function cancelAttention(viewerId,viewedId){
	$.ajax( {
		url : 'AttentionAction?action=cancel',
		type : 'POST',
		timeout : 5000,
		data : {
			'viewerid' : viewerId,
			'viewedid' : viewedId
		},
		success : function(data) {
			alert(data);
		}
	});
}