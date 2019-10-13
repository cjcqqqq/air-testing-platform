// 校验登入状态
function checklogin() {
	if(localStorage.token){
		$.ajax({
			type:"POST",
			url:"/app/user/authentication",
			contentType: "application/json;charset=utf-8",
			beforeSend: function (xhr) {
				xhr.setRequestHeader("token", localStorage.token);
			},
			success:function(result){
				if(GetUrlRelativePath() != "/pages/login.html" && result.resultCode != 200){
					// 页面调转至登入页面
					window.location.href="/pages/login.html";
				}else if(GetUrlRelativePath() == "/pages/login.html"){
					// 设置登入用户名
					localStorage.loginUsername = result.userInfo.username;
					// 页面调转至主页
					window.location.href="/pages/home.html";
				}
			},
			error:function(result){
				if(GetUrlRelativePath() != "/pages/login.html"){
					// 页面调转至登入页面
					window.location.href="/pages/login.html";
				}
			}
		});
	}else{
		// 页面调转至登入页面
		window.location.href="/pages/login.html";
	}
}
function GetUrlRelativePath()
{
	var url = window.location.href;
	var arrUrl = url.split("//");

	var start = arrUrl[1].indexOf("/");
	var relUrl = arrUrl[1].substring(start);//stop省略，截取从start开始到结尾的所有字符

	if(relUrl.indexOf("?") != -1){
		relUrl = relUrl.split("?")[0];
	}
	return relUrl;
}
// 登出
function logout() {
	if(localStorage.token){
		$.ajax({
			type:"POST",
			url:"/app/user/logout",
			beforeSend: function (xhr) {
				xhr.setRequestHeader("token", localStorage.token);
			},
			success:function(result){
				// 页面调转至登入页面
				window.location.href="/pages/login.html";
			},
			error:function(result){
				// 页面调转至登入页面
				window.location.href="/pages/login.html";
			}
		});
	}else{
		// 页面调转至登入页面
		window.location.href="/pages/login.html";
	}
}
checklogin();