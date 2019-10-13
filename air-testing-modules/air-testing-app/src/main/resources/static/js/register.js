//用户检测
function checkUsername() {
	clearMsg();
	var username = $("#registerName").val();
	$.ajax({
		type:"POST",
		url:"/app/user/checkUsername",
		dataType:"json",
		data:{"username":username},
		success:function(result){
			var resultCode=result.resultCode;
			if(resultCode == 1001){
				clearMsg();
				$("#msgbox").prepend("<span class='span'>用户名已存在，请换一个！</span><br/>");
			}
		}
	});
}
//用户检测
function checkPwd() {
	clearMsg();
	var password = $("#passwd").val();
	var password2 = $("#passwd2").val();

	if(password != password2){
		$("#msgbox").prepend("<span class='span'>密码与确认密码不一致，请确认！</span><br/>");
	}
}
// 注册
function register() {
	clearMsg();
	var username = $("#registerName").val();
	var password = $("#passwd").val();
	var password2 = $("#passwd2").val();
	var verifyCode = $("#verifyCode").val();

	if(password != password2){
		$("#msgbox").prepend("<span class='span'>密码与确认密码不一致，请确认！</span><br/>");
		return;
	}

	$.ajax({
		type:"POST",
		url:"/app/user/register",
		dataType:"json",
		data:{"username":username,"password":password,"verifyCode":verifyCode},
		success:function(result){
			var resultCode=result.resultCode;
			switch(resultCode) {
				case 200:
					alert("注册成功！");
					// 执行页面调转
					window.location.href="/pages/login.html";
					break;
				case 400:
					var msgs = result.msg;
					msgs.forEach(showMsg);
					break;
				case 1002:
					$("#msgbox").prepend("<span class='span'>验证码无效！</span><br/>");
					break;
				default:
					$("#msgbox").prepend("<span class='span'>未知错误！</span><br/>");
			}
		}
	});

}
// 获取验证码
function getVerify(){
	var username = document.getElementById("registerName").value;
	$("#verifyCodeImg").attr('src',"/app/user/getVerify?username="+username+"&random="+Math.random());
}
// 显示异常消息
function showMsg(item, index){
	if(item.indexOf("username")>=0){
		$("#msgbox").prepend("<span class='span'>账号不能为空！</span><br/>");
	}
	if(item.indexOf("password")>=0){
		$("#msgbox").prepend("<span class='span'>密码不能为空！</span><br/>");
	}
	if(item.indexOf("verifyCode")>=0){
		$("#msgbox").prepend("<span class='span'>验证码不能为空！</span><br/>");
	}
}
// 清空异常消息
function clearMsg(){
	$("#msgbox").html("");
}

$(function(){
	$("#registerbtn").on("click",function(){
		register();
	})
	$(document).keyup(function(event){
		if(event.keyCode ==13){
			$("#registerbtn").trigger("click");
		}
	});
	$("#registerName").on("change",function(){
		checkUsername();
		getVerify();
	})
	$("#passwd2").on("change",function(){
		checkPwd();
	})
	$("#verifyCodeImg").on("click",function(){
		getVerify();
	})
	
	$("#backBtn").on("click",function(){
		// 执行页面调转
		window.location.href="/pages/login.html";
	})
})