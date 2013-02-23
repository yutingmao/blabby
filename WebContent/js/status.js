//动态统计状态区域字符
function countStatusChar(area,num){
	var text = area.value;
	
	if(text.length <= num){
		$("#countBody")[0].innerHTML = num - text.length;
	} else {
		area.value = text.substring(0,num);
	}
}
//动态统计评论区域字符
function countCommentChar(area,statusId,num){
	var text = area.value;
	
	if(text.length <= num){
		$("#countArea"+statusId)[0].innerHTML = num - text.length;
	} else {
		area.value = text.substring(0,num);
	}
}
//发布状态
function publishStatus(userid){
	var status = $("#publishtext").val();
	
	status = $.trim(status);
	if(status == ''){
		$("#publishtext").val("");
		$("#publishtext").focus();
		$("#showmsg").html("Cannot be blank!");
		$("#showmsg").removeClass("hidden");		
		return;
	}

	$.ajax( {
		url : 'StatusAction?action=publish',
		type : 'POST',
		timeout : 5000,
		data : {
			'status' : status,
			'userid' : userid
		},
		beforeSend : function() {
			// 等待过程中显示等待画面
			$("#showmsg").addClass("hidden");
			$("#waitpublish").removeClass("hidden");
		},
		error : function() {
			$("#showmsg").html("Failed to connect with server!");
			$("#showmsg").removeClass("hidden");
		},
		success : function(data) {
			//清空原来的状态
			$("#publishtext").val("");
			// 显示状态信息
			$("#waitpublish").addClass("hidden");
			$("#showmsg").html(data);
			$("#showmsg").removeClass("hidden");
			//显示新的状态
			getStatus();
		}
	});
}
// 获取状态信息
function getStatus() {
	var statusBoard = $("#status_board")[0];
	var range = $("#status_range")[0].value;
	var userid = $("#user_id")[0].value;
	var index = $("#index")[0].value;
	var pagesize = $("#page_size")[0].value;
	$.ajax( {
		url : 'status/status.jsp',
		type : 'POST',
		timeout : 5000,
		data : {
			'range' : range,
			'userid' : userid,
			'index' : index,
			'pagesize' : pagesize
		},
		error : function() {
			statusBoard.innerHTML = '<div class="waitstatus">  Failed to read...</div>';
		},
		beforeSend : function() {
			// 等待过程中显示等待画面
			statusBoard.innerHTML = '<div class="waitstatus"><img src="images/wait.gif"/>　Waiting...</div>';
		},
		error : function() {
			statusBoard.innerHTML = '<div class="waitstatus">  Failed to read...</div>';
		},
		success : function(data) {
			// 显示状态信息
			statusBoard.innerHTML = data;
		}
	});
//	setTimeout(getStatus, 60000);
}
// 获取评论
// userid为所要评论的状态所对应的用户id,
// statusid为所评论状态的id
function getComments(userid, statusid) {
	var commentArea = $("#comment" + statusid)[0];
	// 删除原来的评论列表，以更新评论信息
	var length = commentArea.childNodes.length;
	for ( var i = 0; i < length; i++) {
		// 先移除原有的评论
		commentArea.removeChild(commentArea.childNodes[i]);
	}

	if (length == 0) {// 获取并展开评论列表
		$.ajax( {
			url : 'status/comment.jsp',
			type : 'POST',
			data : {
				'userid' : userid,
				'statusid' : statusid
			},
			dataType : 'html',
			timeout : 3000,
			beforeSend : function() {
				// 等待过程中显示等待画面
				commentArea.innerHTML = '<div class="comment_d comment_vis_d">'+
										'<table class="comment_table"><tr><td>'+
											'<div class="comment_div"><img src="images/wait.gif"/>Please Wait...</div>'+
										'</td></tr></div>';
			},
			error : function() {
				commentArea.innerHTML = '<div class="comment_d comment_vis_d">'+
										'<table class="comment_table"><tr><td>'+
											'<div class="comment_div"><p>Failed!</p></div>'+
										'</td></tr></div>';
			},
			success : function(data) {
				commentArea.innerHTML = data; // 显示评论状态
			}
		});
	} else { // 隐藏(实际为删除)评论列表
	}
}
// 发表评论,fromId为发表评论的用户ID,toId为所评状态的用户ID,statusId为所评论的状态的ID
function comment(fromId,toId,statusId){
	var comments = $("#commenttext"+statusId).val();
	
	comments = $.trim(comments);
	if(comments == ''){
		$("#commenttext"+statusId).val("");
		alert("Cannot be blank!");
		$("#commenttext"+statusId).focus();
		return;
	}
	
	$.ajax( {
		url : 'CommentAction?action=comment',
		type : 'POST',
		timeout : 5000,
		data : {
			'fromid' : fromId,
			'toid' : toId,
			'statusid' : statusId,
			'comment' : comments
		},
		success : function(data) {
			var commentArea = $("#comment" + statusId)[0];
			// 删除原来的评论列表，以更新评论信息
			var length = commentArea.childNodes.length;
			for ( var i = 0; i < length; i++) {
				// 先移除原有的评论
				commentArea.removeChild(commentArea.childNodes[i]);
			}
			// 重新刷新评论
			getComments(toId, statusId);
		}
	});
}
// 删除状态
function deleteStatus(statusId){
	$.ajax( {
		url : 'StatusAction?action=delete',
		type : 'POST',
		timeout : 5000,
		data : {
			'statusid' : statusId
		},
		success : function(data) {
			//刷新状态
			getStatus();
		}
	});
}
//删除评论,statusOwnerId为评论所对应状态的用户的ID,statusId为评论对应的状态的ID,commentId为评论的ID
function deleteComment(statusOwnerId,statusId,commentId){
	$.ajax( {
		url : 'CommentAction?action=delete',
		type : 'POST',
		timeout : 5000,
		data : {
			'commentid' : commentId
		},
		success : function(data) {
			var commentArea = $("#comment" + statusId)[0];
			// 删除原来的评论列表，以更新评论信息
			var length = commentArea.childNodes.length;
			for ( var i = 0; i < length; i++) {
				// 先移除原有的评论
				commentArea.removeChild(commentArea.childNodes[i]);
			}
			// 重新刷新评论
			getComments(statusOwnerId, statusId);
		}
	});
}