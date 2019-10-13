// 登入
function login() {
	clearMsg();
	var username = $("#loginName").val();
	var password = $("#passwd").val();
	var verifyCode = $("#verifyCode").val();

	$.ajax({
		type:"POST",
		url:"/app/user/login",
		dataType:"json",
		data:{"username":username,"password":password,"verifyCode":verifyCode},
		success:function(result){
			var resultCode=result.resultCode;
			switch(resultCode) {
				case 200:
					// 获取并存储token
					localStorage.token=result.token;
					// 设置登入用户名
					localStorage.loginUsername = username;
					// 判断是否保存用户名和密码
					if(localStorage.isRememberPwd){
						localStorage.username = username;
						localStorage.password = password;
					}else{
						localStorage.username = "";
						localStorage.password = "";
					}
					// 执行页面调转
					window.location.href="/pages/home.html";
				   break;
				case 400:
					var msgs = result.msg;
					msgs.forEach(showMsg);
				   break;
				case 1002:
					$("#msgbox").prepend("<span class='span'>验证码无效！</span><br/>");
				   break;
				case 1003:
					$("#msgbox").prepend("<span class='span'>用户名或密码错误！</span><br/>");
				   break;
				default:
					$("#msgbox").prepend("<span class='span'>未知错误！</span><br/>");
		   }
		}
	});
}
// 获取验证码
function getVerify(){
	var username = document.getElementById("loginName").value;
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
	$("#loginbtn").on("click",function(){
		login();
	})
	$(document).keyup(function(event){
		if(event.keyCode ==13){
			$("#loginbtn").trigger("click");
		}
	});
	$("#loginName").on("change",function(){
		getVerify();
	})
	$("#verifyCodeImg").on("click",function(){
		getVerify();
	})
	$("#rememberPwd").on("click",function(){
		localStorage.isRememberPwd=$("#rememberPwd").is(":checked");
	})
	
	// 根据之前的选择设置状态
	if(localStorage.isRememberPwd=="true"){
		$("#rememberPwd").attr("checked",true);
		$("#loginName").val(localStorage.username);
		$("#passwd").val(localStorage.password);
		getVerify();
	}else{
		$("#rememberPwd").attr("checked",false);
	}
})