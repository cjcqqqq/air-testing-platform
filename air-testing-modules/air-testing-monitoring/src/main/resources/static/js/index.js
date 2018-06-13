/**
 * Created by pbw on 2018/4/16.
 */

var map = new BMap.Map("container");          // 创建地图实例
map.enableScrollWheelZoom(); // 允许滚轮缩放
var stype = getQueryString("showType");

switch(stype)
{
	case "1":
		$(document).attr("title","温度热力图");
		break;
	case "12":
		$(document).attr("title","湿度热力图");
		break;
	case "9":
		$(document).attr("title","PM1.0热力图");
		break;
	case "2":
		$(document).attr("title","PM2.5热力图");
		break;
	case "3":
		$(document).attr("title","PM10热力图");
		break;
	case "11":
		$(document).attr("title","甲醛热力图");
		break;
	case "13":
		$(document).attr("title","有机气态物质热力图");
		break;
	case "4":
		$(document).attr("title","CO热力图");
		break;
	case "5":
		$(document).attr("title","CO2热力图");
		break;
	case "6":
		$(document).attr("title","NO热力图");
		break;
	case "7":
		$(document).attr("title","NO2热力图");
		break;
	case "8":
		$(document).attr("title","O3热力图");
		break;
	case "10":
		$(document).attr("title","SO2热力图");
		break;
	default:
		$(document).attr("title","温度热力图");
		break;
}

if (!isSupportCanvas()) {
	alert('热力图目前只支持有canvas支持的浏览器,您所使用的浏览器不能使用热力图功能~')
}
//详细的参数,可以查看heatmap.js的文档 https://github.com/pa7/heatmap.js/blob/master/README.md
//参数说明如下:
/* visible 热力图是否显示,默认为true
* opacity 热力的透明度,1-100
* radius 势力图的每个点的半径大小
* gradient  {JSON} 热力图的渐变区间 . gradient如下所示
*	{
.2:'rgb(0, 255, 255)',
.5:'rgb(0, 110, 255)',
.8:'rgb(100, 0, 255)'
}
其中 key 表示插值的位置, 0~1.
value 为颜色值.
*/

//按类别显示热力图
function openHeatmapByType() {
	// 清除地图上的覆盖物
	map.clearOverlays();

	var points =[];

	$.ajax
	({
		type: "post",
		url: "http://180.76.137.169:8888/report/getAllDeviceLatestInfo",
		async: false,
		dataType: 'json',
		data: {type : stype},
		success: function (result) {
			$.each(result.data,function(index,item){
				var pts = {"lng": item.longitude, "lat": item.latitude, "count": item.typeValue};
				points.push(pts);
			});
			// 初始化地图，设置中心点坐标和地图级别
			var point = new BMap.Point(points[0].lng, points[0].lat);
			map.centerAndZoom(point, 11);
		}
	});

	heatmapOverlay = new BMapLib.HeatmapOverlay({"radius": 20});
	map.addOverlay(heatmapOverlay);
	heatmapOverlay.setDataSet({data:points,max:100});
	heatmapOverlay.show();
}

openHeatmapByType();

//定时刷新
$(document).ready(function () {
	setInterval("location.replace(location.href);",10000);
})
            
function setGradient() {
	/*格式如下所示:
	 {
	 0:'rgb(102, 255, 0)',
	 .5:'rgb(255, 170, 0)',
	 1:'rgb(255, 0, 0)'
	 }*/
	var gradient = {};
	var colors = document.querySelectorAll("input[type='color']");
	colors = [].slice.call(colors, 0);
	colors.forEach(function (ele) {
	    gradient[ele.getAttribute("data-key")] = ele.value;
	});
	heatmapOverlay.setOptions({"gradient": gradient});
}

//判断浏览区是否支持canvas
function isSupportCanvas() {
	var elem = document.createElement('canvas');
	return !!(elem.getContext && elem.getContext('2d'));
}

function getQueryString(name)
{
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null)return  unescape(r[2]); return null;
}