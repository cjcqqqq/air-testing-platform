if(localStorage.loginUsername){
	$("#helloName").text("您好，"+localStorage.loginUsername);
}
function scan() {
	cordova.plugins.barcodeScanner.scan(
		function (scanResult) {
			$.ajax({
				type:"POST",
				url:"/app/device/checkQRcode",
				beforeSend: function (xhr) {
					xhr.setRequestHeader("token", localStorage.token);
				},
				dataType:"json",
				data:{"QRcode":scanResult.text},
				success:function(result){
					// 页面调转至设备添加页
					window.location.href="/pages/addDevice.html?QRcode="+scanResult.text;
				},
				error:function(result){
					// 设备无效
					alert("无效设备，请扫描正确二维码");
				}
			});
		},
		function (error) {
			alert("扫描失败: " + error);
		}
	);
}
// 扫描设备二维码
function setNet(deviceId, deviceDesc) {
	sessionStorage.setNetDeviceId = deviceId;
	sessionStorage.setNetDeviceDesc = deviceDesc;
	// 页面调转至设备添加页
	window.parent.location.href="/pages/setNetwork.html";
}
// 显示设备列表
function showDeviceList(item, index){
	var sart = item.isDefault==1?'*':'';
	var onlineStatus = item.isOnline==1?"<font class='green'>在线</font>":"<font class='red'>离线</font>";
	if(index == 0){
		$("<tr class='tr0'><td>"+sart+(index+1)+"</td><td>"+item.deviceDesc+"</td><td>"+onlineStatus+"</td><td><a href=\"javascript:void(0);\" onclick=\"doDelete('"+item.id +"','"+item.deviceDesc+"')\">删除</a><br/><br/><a href=\"javascript:void(0);\" onclick=\"setDefault('"+item.deviceId+"')\">设为默认</a><br/><br/><a href=\"javascript:void(0);\" onclick=\"setNet('"+item.deviceId +"','"+item.deviceDesc+"')\">网络设置</a></td></tr>").insertAfter(".table-head");
	}else{
		$("<tr class='tr"+index+"'><td>"+sart+(index+1)+"</td><td>"+item.deviceDesc+"</td><td>"+onlineStatus+"</td><td><a href=\"javascript:void(0);\" onclick=\"doDelete('"+item.id +"','"+item.deviceDesc+"')\">删除</a><br/><br/><a href=\"javascript:void(0);\" onclick=\"setDefault('"+item.deviceId+"')\">设为默认</a><br/><br/><a href=\"javascript:void(0);\" onclick=\"setNet('"+item.deviceId +"','"+item.deviceDesc+"')\">网络设置</a></td></tr>").insertAfter(".tr"+(index-1)+"");
	}

}
// 删除设备
function doDelete(mappingId,deviceDesc) {
	if(window.confirm("你确定要删除\""+deviceDesc+"\"设备吗？")){
		$.ajax({
			type:"POST",
			url:"/app/device/deleteUserDevice",
			beforeSend: function (xhr) {
				xhr.setRequestHeader("token", localStorage.token);
			},
			dataType:"json",
			data:{"mappingId":mappingId},
			success:function(result){
				if(result.resultCode == 200){
					// 页面跳转至设备列表页
					window.location.href="/pages/deviceList.html";
				}
			},
			error:function(result){
				// 服务器异常
				alert("服务器异常");
			}
		});
		return true;
	}else{
		return false;
	}
}
// 设置默认设备
function setDefault(deviceId) {
	$.ajax({
		type:"POST",
		url:"/app/device/setDefault",
		beforeSend: function (xhr) {
			xhr.setRequestHeader("token", localStorage.token);
		},
		dataType:"json",
		data:{"deviceId":deviceId},
		success:function(result){
			if(result.resultCode == 200){
				// 页面跳转至设备列表页
				window.location.href="/pages/deviceList.html";
			}
		},
		error:function(result){
			// 服务器异常
			alert("服务器异常");
		}
	});
}
$(function(){
	$.ajax({
		type:"POST",
		url:"/app/device/getUserDeviceList",
		beforeSend: function (xhr) {
			xhr.setRequestHeader("token", localStorage.token);
		},
		dataType:"json",
		success:function(result){
			if(result.resultCode == 200){
				if(result.data.length > 0){
					result.data.forEach(showDeviceList);
				}else {
					$("<tr><td colspan='4'>暂无设备</td></tr>").insertBefore(".table-head");
				}
			}
		},
		error:function(result){
			// 服务器异常
			alert("服务器异常");
		}
	});

})
