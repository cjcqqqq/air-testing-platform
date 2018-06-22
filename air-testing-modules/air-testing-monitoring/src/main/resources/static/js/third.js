/**
 * Created by pbw on 2018/5/8.
 */
var map = new BMap.Map("container");          // 创建地图实例
map.enableScrollWheelZoom(); // 允许滚轮缩放



if (!isSupportCanvas()) {
    alert('热力图目前只支持有canvas支持的浏览器,您所使用的浏览器不能使用热力图功能~')
}
//详细的参数,可以查看heatmap.js的文档 https://github.com/pa7/heatmap.js/blob/master/README.md
//参数说明如下:
/* visible 热力图是否显示,默认为true
 * opacity 热力的透明度,1-100
 * radius 势力图的每个点的半径大小
 * gradient  {JSON} 热力图的渐变区间 . gradient如下所示
 *
 *	{
 .2:'rgb(0, 255, 255)',
 .5:'rgb(0, 110, 255)',
 .8:'rgb(100, 0, 255)'
 }
 其中 key 表示插值的位置, 0~1.
 value 为颜色值.
 */

//按类别显示热力图

//定时刷新
$(document).ready(function () {
    setInterval("openHeatmapByType(3)",30000)

})
function openHeatmapByType(showType) {
    // 清除地图上的覆盖物
    map.clearOverlays();

    var points =[];

    $.ajax
    ({
        type: "post",
        url: "/report/getAllDeviceLatestInfo",
        async: false,
        dataType: 'json',
        data: {type : showType},
        success: function (result) {
            $.each(result.data,function(index,item){
                var pts = {"lng": item.longitude, "lat": item.latitude, "count": item.typeValue};
                points.push(pts);
            });
            // 初始化地图，设置中心点坐标和地图级别
            var point = new BMap.Point(points[0].lng, points[0].lat);
            map.centerAndZoom(point, 17);
        }
    });

    heatmapOverlay = new BMapLib.HeatmapOverlay({"radius": 20});
    map.addOverlay(heatmapOverlay);
    heatmapOverlay.setDataSet({data:points,max:100});
    heatmapOverlay.show();

}

openHeatmapByType(3);

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
