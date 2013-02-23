// 显示修改个人资料区域
function showInfoEditor(){  
	$("#err").addClass("hidden");
	$("#photo_editor").addClass("hidden");
	$("#music_editor").addClass("hidden");
	$("#psw_editor").addClass("hidden");
	$("#info_editor").removeClass("hidden");
}
// 显示头像修改区域
function showPhotoEditor(){
	$("#err").addClass("hidden");
	$("#photo_editor").removeClass("hidden");
	$("#music_editor").addClass("hidden");
	$("#psw_editor").addClass("hidden");
	$("#info_editor").addClass("hidden");
}
// 显示修改密码区域
function showPswEditor(){
	$("#err").addClass("hidden");
	$("#photo_editor").addClass("hidden");
	$("#music_editor").addClass("hidden");
	$("#psw_editor").removeClass("hidden");
	$("#info_editor").addClass("hidden");
}
// 显示修改音乐区域
function showMusicEditor(){
	$("#err").addClass("hidden");
	$("#photo_editor").addClass("hidden");
	$("#music_editor").removeClass("hidden");
	$("#psw_editor").addClass("hidden");
	$("#info_editor").addClass("hidden");
}
//保存用户信息
function saveInfo(){
	var realName = $("#realname")[0].value;
	if(realName.length <= 0){
		$("#err")[0].innerHTML = "Please enter Name";
		return;
	}
	
	$.ajax( {
		url : 'UserAction?action=info',
		type : 'POST',
		timeout : 5000,
		data : {
			'realname' : realName,
			'gender' : $("input:checked").val(),
			'date' :$("#year")[0].value+"-"+$("#month")[0].value+"-"+$("#day")[0].value,
			'address' : $("#address")[0].value,
			'brief' : $("#brief")[0].value
		},
		beforeSend : function() {
			$("#err").removeClass("hidden");
			// 等待过程中显示等待画面
			$("#err")[0].innerHTML = '<img src="images/wait.gif"/>　Please Wait...';
		},
		error : function() {
			$("#err").removeClass("hidden");
			$("#err")[0].innerHTML = "Failed to connect with Server!";
		},
		success : function(data) {
			$("#err").removeClass("hidden");
			$("#err")[0].innerHTML = data;
		}
	});
}
//保存头像
function photoUpload()
{
	var allow = new Array('gif','png','jpg','jpeg');
	if(!checkFileType("photo",allow)){
		alert("Only accept files with 'gif,png,jpg,jpeg' format");
		return;
	}
	$.blockUI.defaults.fadeOut=5000;
	// ajax开始上传前锁定界面
	$("#photo_editor").ajaxStart(function(){
		$.blockUI({message:'<h1><img src="images/wait.gif"/>Uploading...</h1>'});
	});
	// 上传
	$.ajaxFileUpload({
		url:'FileAction?type=photo',
		secureuri:false,
		fileElementId:'photo',
		dataType: 'json',
		success: function (data, status){
			if(typeof(data) != 'undefined'){
				if(data.error != '' && typeof(data.error) != 'undefined') {
					$.blockUI({message:'<h1>'+data.error+'</h1>'});
				}
				if(data.msg != '' && typeof(data.msg) != 'undefined') {
					$.blockUI({message:'<h1>'+data.msg+'</h1>'});
				}
				if(data.url != '' && typeof(data.url) != 'undefined') {
					//显示新上传的头像
					$("#photo_img")[0].src = data.url;
				}
			}
		},
		error: function (data, status, e){
			if(typeof(e) != 'undefined') {
				$.blockUI({message:e});
			}
		}
	});

	// ajax完成解锁
	$("#photo_editor").ajaxComplete(function(){
		$.unblockUI();
	});

}
//保存音乐
function musicUpload()
{
	var allow = new Array('mp3','mid','wav','wma','swf');
	if(!checkFileType("music",allow)){
		alert("Only accept files with 'mp3,mid,wav,wma,swf' format");
		return;
	}
	$.blockUI.defaults.fadeOut=5000;
	$("#music_editor").ajaxStart(function(){
		$.blockUI({message:'<h1><img src="images/wait.gif"/>Uploading...</h1>'});
	});
	// 上传
	$.ajaxFileUpload({
		url:'FileAction?type=music',
		secureuri:false,
		fileElementId:'music',
		dataType: 'json',
		success: function (data, status){
			if(typeof(data) != 'undefined'){
				if(data.error != '' && typeof(data.error) != 'undefined') {
					$.blockUI({message:'<h1>'+data.error+'</h1>'});
				}
				if(data.msg != '' && typeof(data.msg) != 'undefined') {
					$.blockUI({message:'<h1>'+data.msg+'</h1>'});
				}
				if(data.url != '' && typeof(data.url) != 'undefined') {
					//跳转到指定页面
					window.location.href = data.url;
				}
			}
		},
		error: function (data, status, e){
			if(typeof(e) != 'undefined') {
				$.blockUI({message:e});
			}
		}
	});

	// ajax完成解锁
	$("#music_editor").ajaxComplete(function(){
		$.unblockUI();
	});

}
//检测文件类型
function checkFileType(fileId,typeArray){
	var ext = $('#'+fileId).val().split('.').pop().toLowerCase();
	if(jQuery.inArray(ext, typeArray) == -1) {
	    return false;
	} else {
		return true;
	}
}
//修改密码
function checkPsw(){
	var oldPass = $("#oldpass").val();
	var newPass = $("#newpass").val();
	var repeatPass = $("#repeatpass").val();
	
	$("#err").removeClass("hidden");
	if(oldPass == ""){
		$("#err").html("Please enter old password...");
		return;
	}
	if(newPass == ""){
		$("#err").html("Please enter new password...");
		return;
	}
	if(repeatPass == ""){
		$("#err").html("Please confirm new password...");
		return;
	}
	if(newPass != repeatPass){
		$("#err").html("Inconsistent password");
		$("#newpass").val("");
		$("#repeatpass").val("");
		return;
	}
	$.ajax( {
		url : 'UserAction?action=pwd',
		type : 'POST',
		timeout : 5000,
		data : {
			'oldpass' : oldPass,
			'newpass' : newPass,
			'repeatpass' : repeatPass
		},
		success : function(data) {
			$("#err").html(data);
			$("#newpass").val("");
			$("#repeatpass").val("");
			$("#oldpass").val("");
		}
	});
}