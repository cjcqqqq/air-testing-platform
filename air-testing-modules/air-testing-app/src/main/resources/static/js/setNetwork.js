if(localStorage.loginUsername){
	$("#helloName").text("您好，"+localStorage.loginUsername);
}
// 刷新数据
function update(){
	var deviceId = sessionStorage.setNetDeviceId;
	$.ajax({
		type:"POST",
		url:"/app/device/queryUserReport",
		beforeSend: function (xhr) {
			xhr.setRequestHeader("token", localStorage.token);
		},
		dataType:"json",
		data:{"deviceId":deviceId},
		success:function(result){
			if(result.resultCode == 200){
				// 设置在线状态
				if(result.data.isOnline==1){
					$("#onlineStatus").text("在线");
					$("#onlineStatus").removeClass("red");
					$("#onlineStatus").addClass("green");
				}else {
					$("#onlineStatus").text("离线");
					$("#onlineStatus").removeClass("green");
					$("#onlineStatus").addClass("red");
				}
			}
		},
		error:function(result){
			// 服务器异常
			alert("服务器异常");
		}
	});
}
// 显示异常消息
function showMsg(item, index){
	if(item.indexOf("deviceId")>=0){
		$("#msgbox").prepend("<span class='span'>未知设备！</span><br/>");
	}
	if(item.indexOf("account")>=0){
		$("#msgbox").prepend("<span class='span'>网络名称不能为空！</span><br/>");
	}
	if(item.indexOf("password")>=0){
		$("#msgbox").prepend("<span class='span'>网络密码不能为空！</span><br/>");
	}
}
// 清空异常消息
function clearMsg(){
	$("#msgbox").html("");
}
// 设置网络
function setNetwork() {
	clearMsg();
	var deviceId = sessionStorage.setNetDeviceId;
	var account = $("#account").val();
	var password = $("#password").val();

	$.ajax({
		type:"POST",
		url:"/app/device/wifiSetting",
		beforeSend: function (xhr) {
			xhr.setRequestHeader("token", localStorage.token);
		},
		dataType:"json",
		data:{"deviceId":deviceId,"account":account,"password":password},
		success:function(result){
			var resultCode=result.resultCode;
			switch(resultCode) {
				case 200:
					alert("网络设置成功！");
					// 执行页面调转
					window.location.href="/pages/deviceList.html";
					break;
				case 400:
					var msgs = result.msg;
					msgs.forEach(showMsg);
					break;
				case 2004:
					alert("设备未联网，请按步骤1操作！");
					break;
				default:
					$("#msgbox").prepend("<span class='span'>未知错误！</span><br/>");
			}
		}
	});

}
$(function(){
	$("#deviceDesc").text(sessionStorage.setNetDeviceDesc);
	update();
	setInterval(update, 5000);

	$("#backBtn").on("click",function(){
		// 执行页面调转
		window.location.href="/pages/deviceList.html";
	})
	$("#setNetBtn").on("click",function(){
		setNetwork();
	})
	$(document).keyup(function(event){
		if(event.keyCode ==13){
			$("#setNetBtn").trigger("click");
		}
	});
})