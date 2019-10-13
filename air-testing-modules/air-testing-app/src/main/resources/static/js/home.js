Date.prototype.format = function(fmt) {
	var o = {
		"M+" : this.getMonth()+1,                 //月份
		"d+" : this.getDate(),                    //日
		"h+" : this.getHours(),                   //小时
		"m+" : this.getMinutes(),                 //分
		"s+" : this.getSeconds(),                 //秒
		"q+" : Math.floor((this.getMonth()+3)/3), //季度
		"S"  : this.getMilliseconds()             //毫秒
	};
	if(/(y+)/.test(fmt)) {
		fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	}
	for(var k in o) {
		if(new RegExp("("+ k +")").test(fmt)){
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
		}
	}
	return fmt;
}
if(localStorage.loginUsername){
	$("#helloName").text("您好，"+localStorage.loginUsername);
}
// 显示设备列表
function showDeviceList(item, index){
	var isSelect = item.isDefault==1?'selected':'';
	$("#devices").append("<option value='"+item.deviceId+"' "+isSelect+">"+item.deviceDesc+"</option>");
}
// 刷新数据
function update(){
	var deviceId=$("#devices").val();
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

				var latestFormaldehydeVal = result.data.formaldehyde;
				var latestTvocVal = result.data.tvoc/100;
				if(latestFormaldehydeVal && latestFormaldehydeVal != "null"){
					// 设置末次上报值
					$("#latestFormaldehyde").text(latestFormaldehydeVal);
					// 设置值说明
					if(latestFormaldehydeVal<0.03){
						$("#formaldehydeExplain").text("优");
					}else if(latestFormaldehydeVal<=0.08){
						$("#formaldehydeExplain").text("良");
					}else {
						$("#formaldehydeExplain").text("差");
					}

				}
				if(latestTvocVal && latestTvocVal != "null") {
					// 设置末次上报值
					$("#latestTvoc").text(latestTvocVal);
					// 设置值说明
					if (latestTvocVal < 0.07) {
						$("#tvocExplain").text("优");
					} else if (latestTvocVal <= 0.3) {
						$("#tvocExplain").text("良");
					} else {
						$("#tvocExplain").text("差");
					}
				}
				if((latestFormaldehydeVal && latestFormaldehydeVal != "null") || (latestTvocVal && latestTvocVal != "null")){
					// 设置末次上报时间
					var collectTi = new Date( result.data.collectTime).format("yyyy-MM-dd hh:mm:ss");
					$("#latestTime").text(collectTi);
				}

				// 基于准备好的dom，初始化echarts实例
				var hoursTrendChart = echarts.init(document.getElementById('24HoursTrend'));
				var hoursTrendX = [];
				var hoursTrendFormaldehydeVal = [];
				var hoursTrendTvocVal = [];
				if(result.data.hourInfos && result.data.hourInfos != "null" && result.data.hourInfos.length > 0){
					$.each(result.data.hourInfos, function (index, item) {
						var pts01 = item.collectTime;
						var pts = new Date(pts01);
						var hour = pts.getHours();
						hoursTrendX.push(hour);
						hoursTrendFormaldehydeVal.push(item.formaldehyde);
						hoursTrendTvocVal.push(item.tvoc/100);
					});
				}

				var colors = ['#cc0033', '#660099'];
				hoursTrendOption = {
					title: {
						text: '24小时趋势'
					},
					color: colors,

					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'cross'
						}
					},
					grid: {
						right: '20%'
					},
					legend: {
						data:['甲醛','TVOC']
					},
					xAxis: [
						{
							type: 'category',
							axisTick: {
								alignWithLabel: true
							},
							data: hoursTrendX
						}
					],
					yAxis: [
						{
							type: 'value',
							name: '甲醛(μg/m3)',
							min: 0,
							max: 1,
							position: 'left',
							axisLine: {
								lineStyle: {
									color: colors[0]
								}
							},
							axisLabel: {
								formatter: '{value}'
							}
						},
						{
							type: 'value',
							name: 'TVOC(mg/m3)',
							min: 0,
							max: 2,
							position: 'right',
							axisLine: {
								lineStyle: {
									color: colors[1]
								}
							},
							axisLabel: {
								formatter: '{value}'
							}
						}
					],
					series: [
						{
							name:'甲醛',
							type:'line',
							yAxisIndex: 0,
							data:hoursTrendFormaldehydeVal
						},
						{
							name:'TVOC',
							type:'line',
							yAxisIndex: 1,
							data:hoursTrendTvocVal
						}
					]
				};

				// 使用刚指定的配置项和数据显示图表。
				hoursTrendChart.setOption(hoursTrendOption);

				// 基于准备好的dom，初始化echarts实例
				var daysTrendChart = echarts.init(document.getElementById('30DayTrend'));
				var daysTrendX = [];
				var daysTrendFormaldehydeVal = [];
				var daysTrendTvocVal = [];
				if(result.data.dayinfos && result.data.dayinfos != "null" && result.data.dayinfos.length > 0){
					$.each(result.data.dayinfos, function (index, item) {
						var pts01 = item.collectTime;
						var pts = new Date(pts01);
						var day = pts.getDay();
						daysTrendX.push(day);
						daysTrendFormaldehydeVal.push(item.formaldehyde);
						daysTrendTvocVal.push(item.tvoc/100);
					});
				}

				daysTrendOption = {
					title: {
						text: '30天趋势'
					},
					color: colors,

					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'cross'
						}
					},
					grid: {
						right: '20%'
					},
					legend: {
						data:['甲醛','TVOC']
					},
					xAxis: [
						{
							type: 'category',
							axisTick: {
								alignWithLabel: true
							},
							data: daysTrendX
						}
					],
					yAxis: [
						{
							type: 'value',
							name: '甲醛(μg/m3)',
							min: 0,
							max: 1,
							position: 'left',
							axisLine: {
								lineStyle: {
									color: colors[0]
								}
							},
							axisLabel: {
								formatter: '{value}'
							}
						},
						{
							type: 'value',
							name: 'TVOC(mg/m3)',
							min: 0,
							max: 2,
							position: 'right',
							axisLine: {
								lineStyle: {
									color: colors[1]
								}
							},
							axisLabel: {
								formatter: '{value}'
							}
						}
					],
					series: [
						{
							name:'甲醛',
							type:'line',
							yAxisIndex: 0,
							data:daysTrendFormaldehydeVal
						},
						{
							name:'TVOC',
							type:'line',
							yAxisIndex: 1,
							data:daysTrendTvocVal
						}
					]
				};

				// 使用刚指定的配置项和数据显示图表。
				daysTrendChart.setOption(daysTrendOption);
			}
		},
		error:function(result){
			// 服务器异常
			alert("服务器异常");
		}
	});
}
$(function(){
	//为Select添加事件，当选择其中一项时触发
	$("#devices").change(function()
	{
		update();
	});

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
					// 内容清空
					$("#devices").empty();
					result.data.forEach(showDeviceList);
					// 获取用户设备报表数据
					update();
				}else {
					// 页面调转至设备添加页
					window.parent.location.href="/pages/deviceList.html";
				}
			}
		},
		error:function(result){
			// 服务器异常
			alert("服务器异常");
		}
	});

	setInterval(update, 5000);

})