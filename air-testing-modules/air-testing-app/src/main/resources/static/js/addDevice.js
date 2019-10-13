if(localStorage.loginUsername){
	$("#helloName").text("您好，"+localStorage.loginUsername);
}
// 执行添加
function doAdd(QRcode) {
	clearMsg();
	var deviceDesc = $("#deviceDesc").val();
	$.ajax({
		type:"POST",
		url:"/app/device/addDevice",
		dataType:"json",
		data:{"QRcode":QRcode,"deviceDesc":deviceDesc},
		beforeSend: function (xhr) {
			xhr.setRequestHeader("token", localStorage.token);
		},
		success:function(result){
			var resultCode=result.resultCode;
			switch(resultCode) {
				case 200:
					// 清空设备二维码信息
					// localStorage.QRcode = "";
					// 页面调转至设备列表页
					window.location.href="/pages/deviceList.html";
				   break;
				case 400:
					var msgs = result.msg;
					msgs.forEach(showMsg);
				   break;
				case 2001:
					$("#msgbox").prepend("<span class='span'>设备无效！</span><br/>");
				   break;
				case 2002:
					$("#msgbox").prepend("<span class='span'>用户设备已添加</span><br/>");
				   break;
				case 2003:
					$("#msgbox").prepend("<span class='span'>\"" +deviceDesc+"\"该设备描述已使用，请换一个描述！</span><br/>");
					break;
				default:
					$("#msgbox").prepend("<span class='span'>未知错误！</span><br/>");
		   }
		}
	});
}
// 显示异常消息
function showMsg(item, index){
	if(item.indexOf("QRcode")>=0){
		$("#msgbox").prepend("<span class='span'>设备二维码不能为空！</span><br/>");
	}
	if(item.indexOf("deviceDesc")>=0){
		$("#msgbox").prepend("<span class='span'>设备描述不能为空！</span><br/>");
	}
}
// 清空异常消息
function clearMsg(){
	$("#msgbox").html("");
}

$(function(){
	var  QRcode= $.query.get("QRcode");
	$("#addDeviceBtn").on("click",function(){
		doAdd(QRcode);
	})
	$("#backBtn").on("click",function(){
		// 页面调转至设备列表页
		window.location.href="/pages/deviceList.html";
	})
})