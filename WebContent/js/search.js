//显示基本搜索
function showSimpleSearch(){
	$("#advance").addClass("hidden");
	$("#simple").removeClass("hidden");
}
//显示高级搜索
function showAdvanceSearch(){
	$("#advance").removeClass("hidden");
	$("#simple").addClass("hidden");
}
//基本搜索用户
function simpleSearch(){
	$.ajax( {
		url : 'plaza/users.jsp',
		type : 'POST',
		timeout : 5000,
		data : {
			'name' : $("#sname")[0].value
		},
		beforeSend : function() {
			// 等待过程中显示等待画面
			$("#rs_panel")[0].innerHTML = '<img src="images/wait.gif"/>　Searching...';
		},
		error : function() {
			$("#rs_panel")[0].innerHTML = "Failed to connect with Server!";
		},
		success : function(data) {
			$("#rs_panel")[0].innerHTML = data;
		}
	});
}
//高级搜索用户
function advancedSearch(){
	$.ajax( {
		url : 'plaza/users.jsp',
		type : 'POST',
		timeout : 5000,
		data : {
			'name' : $("#aname")[0].value,
			'sex' : $("#sex")[0].value
		},
		beforeSend : function() {
			// 等待过程中显示等待画面
			$("#rs_panel")[0].innerHTML = '<img src="images/wait.gif"/>　Searching...';
		},
		error : function() {
			$("#rs_panel")[0].innerHTML = "Failed to connect with Server!";
		},
		success : function(data) {
			$("#rs_panel")[0].innerHTML = data;
		}
	});
}