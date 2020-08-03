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
					// $("#onlineStatus").removeClass("red");
					// $("#onlineStatus").addClass("green");
					// $("#onlineStatus").removeClass("label label-info");
					// $("#onlineStatus").addClass("label label-success");
					$("#status").removeClass("offline");
					$("#status").addClass("online");
				}else {
					$("#onlineStatus").text("离线");
					// $("#onlineStatus").removeClass("green");
					// $("#onlineStatus").addClass("red");
					// $("#onlineStatus").removeClass("label label-info");
					// $("#onlineStatus").addClass("label label-danger");
					$("#status").removeClass("online");
					$("#status").addClass("offline");
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
						var hour = pts.getHours()+'时';
						hoursTrendX.push(hour);
						hoursTrendFormaldehydeVal.push(item.formaldehyde);
						hoursTrendTvocVal.push(item.tvoc/100);
					});
				}

				hoursTrendOption = {
					title: {
						text: '24小时趋势'
					},
					tooltip: {
						trigger: 'axis',
						color: '#339999',
						formatter: function(params) {
						　　var result = ''
						　　var dotHtml = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#1197b8"></span>'
						　　params.forEach(function (item) {
						　　　　result += item.axisValue + "</br>" + dotHtml + item.data
						　　})
						　　return result
						}
					},
					grid: {
						left: '15%'
					},
					legend: {
						data:['甲醛','TVOC'],
						selectedMode: 'single'
					},
					calculable: true,
					xAxis: [
						{
							type: 'category',
							boundaryGap : false,
							// axisLine: {//坐标轴线
							// 	lineStyle: {
							// 	  color: '#660066'
							// 	}
							//   },
							// textStyle: {
							// 	color: '#339999',//坐标值得具体的颜色
							// },
							data: hoursTrendX
						}
					],
					yAxis: [
						{
							type: 'value',
							name: '(μg/m3)',
							splitLine: {
								show: false
							  },
							position: 'left',
							// axisLine: {
							// 	lineStyle: {
							// 		color: '#660066'
							// 	}
							// },
							// axisTick: {
							// 	show: false
							//   },
							  
							  splitArea: {
								show: false
							  },
							// axisLabel: {
							// 	textStyle: {
							// 		color: '#339999',//坐标值得具体的颜色
							// 	},
							// 	//formatter: '{value}'
							// }
						},
					],
					dataZoom: [{
						type: 'inside',//图表下方的伸缩条，slider表示有滑动块的，inside表示内置的(鼠标滚轮把..)
						start: 70,////伸缩条开始位置（1-100），可以随时更改
						end: 100 //伸缩条结束位置（1-100），可以随时更改
					  }],
					series: [
						{
							name:'甲醛',
							type:'line',
							smooth:true,
							areaStyle: { //区域填充样式
								normal: {
									opacity : 0.3	                    
								}
							},
							itemStyle: {  
								normal: {   //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
									color: new echarts.graphic.LinearGradient(0, 0, 0, 1,[{
											offset: 0, color: '#339999' // 0% 处的颜色
										}, {
											offset: 1, color: '#fff' // 100% 处的颜色
										}]
									),  //背景渐变色 
									lineStyle:{  
										color:'#339999'  
									}
								},
							  },
							label: {
								normal: {
									show: true,
									position: 'top',
									color: '#339999'
								}
							},
							data:hoursTrendFormaldehydeVal
						},
						{
							name:'TVOC',
							type:'line',
							smooth:true,
							areaStyle: { //区域填充样式
								normal: {
									opacity : 0.3	                    
								}
							},
							itemStyle: {  
								  normal: {   //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
									  color: new echarts.graphic.LinearGradient(0, 0, 0, 1,[{
											  offset: 0, color: '#FF9966' // 0% 处的颜色
										  }, {
											  offset: 1, color: '#fff' // 100% 处的颜色
										  }]
									  ),  //背景渐变色 
									  lineStyle:{  
									color:'#FF9966'  
								  }
								  },
							  },
							  label: {
								normal: {
									show: true,
									position: 'top',
									color: '#FF9966'
								}
							},
							// yAxisIndex: 1,
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
						var day = pts.getDate()+'日';
						daysTrendX.push(day);
						daysTrendFormaldehydeVal.push(item.formaldehyde);
						daysTrendTvocVal.push(item.tvoc/100);
					});
				}

				daysTrendOption = {
					title: {
						text: '30天趋势'
					},
					tooltip: {
						trigger: 'axis',
						color: '#339999',
						formatter: function(params) {
						　　var result = ''
						　　var dotHtml = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:#1197b8"></span>'
						　　params.forEach(function (item) {
						　　　　result += item.axisValue + "</br>" + dotHtml + item.data
						　　})
						　　return result
						}
					},
					grid: {
						left: '15%'
					},
					legend: {
						data:['甲醛','TVOC'],
						selectedMode: 'single'
					},
					calculable: true,
					xAxis: [
						{
							type: 'category',
							boundaryGap : false,
							axisTick: {////////////////
								alignWithLabel: true
							},
							data: daysTrendX
						}
					],
					yAxis: [
						{
							type: 'value',
							name: '(μg/m3)',
							position: 'left',
							splitLine: {
								show: false
							},
							splitArea: {
								show: false
							},
						}
					],
					dataZoom: [{
						type: 'inside',//图表下方的伸缩条，slider表示有滑动块的，inside表示内置的(鼠标滚轮把..)
						start: 75,////伸缩条开始位置（1-100），可以随时更改
						end: 100 //伸缩条结束位置（1-100），可以随时更改
					  }],
					series: [
						{
							name:'甲醛',
							type:'line',
							smooth:true,
							areaStyle: { //区域填充样式
								normal: {
									opacity : 0.3	                    
								}
							},
							itemStyle: {  
								normal: {   //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
									color: new echarts.graphic.LinearGradient(0, 0, 0, 1,[{
											offset: 0, color: '#339999' // 0% 处的颜色
										}, {
											offset: 1, color: '#fff' // 100% 处的颜色
										}]
									),  //背景渐变色 
									lineStyle:{  
										color:'#339999'  
									}
								},
							  },
							label: {
								normal: {
									show: true,
									position: 'top',
									color: '#339999'
								}
							},
							data:daysTrendFormaldehydeVal
						},
						{
							name:'TVOC',
							type:'line',
							smooth:true,
							areaStyle: { //区域填充样式
								normal: {
									opacity : 0.3	                    
								}
							},
							itemStyle: {  
								  normal: {   //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
									  color: new echarts.graphic.LinearGradient(0, 0, 0, 1,[{
											  offset: 0, color: '#FF9966' // 0% 处的颜色
										  }, {
											  offset: 1, color: '#fff' // 100% 处的颜色
										  }]
									  ),  //背景渐变色 
									  lineStyle:{  
									color:'#FF9966'  
								  }
								  },
							  },
							  label: {
								normal: {
									show: true,
									position: 'top',
									color: '#FF9966'
								}
							},
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