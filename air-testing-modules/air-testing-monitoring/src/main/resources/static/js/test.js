map = new BMap.Map("allmap");
map.enableScrollWheelZoom();
map.enableKeyboard();                         // 启用键盘操作。
map.addControl(new BMap.NavigationControl()); // 添加平移缩放控件
map.addControl(new BMap.ScaleControl());                    // 添加比例尺控件
map.addControl(new BMap.OverviewMapControl());
//map.centerAndZoom(new BMap.Point(123.468496,41.834763), 16);

//定时刷新
$(document).ready(function () {
    setInterval("openmapByType()",10000)

})
function openmapByType(showType) {
    清除地图上的覆盖物
    map.clearOverlays();
    var data_info =[];
    $.ajax
    ({
        type: "post",
        url: "/report/getAllDeviceLatestInfo",
        async: false,
        dataType: 'json',
        data: {type : showType},
        success: function (result) {
            $.each(result.data,function(index,item){
                var lng = item.longitude;
                var lat = item.latitude;
                var str1 =  item.longitude.toString();
                var str2 =  item.latitude.toString();
                var str3 =  item.typeValue.toString();
                var  str4 = item.type.toString();
                var pts = [lng,lat,"坐标："+str1+";"+str2+";"+"监测值："+str3];
                data_info.push(pts);
            });
            //初始化地图，设置中心点坐标和地图级别
            var point = new BMap.Point(data_info[0][0], data_info[0][1]);
            map.centerAndZoom(point, 17);
        }
    });

    var opts = {
        width : 250,     // 信息窗口宽度
        height: 80,     // 信息窗口高度
        title : "信息窗口" , // 信息窗口标题
        enableMessage:true//设置允许信息窗发送短息
    };
    for(var i=0;i<data_info.length;i++){
        var marker = new BMap.Marker(new BMap.Point(data_info[i][0],data_info[i][1]));  // 创建标注
        var content = data_info[i][2];
        map.addOverlay(marker);               // 将标注添加到地图中
        addClickHandler(content,marker);
    }
    function addClickHandler(content,marker){
        marker.addEventListener("click",function(e){
            openInfo(content,e)}
        );
    }
    function openInfo(content,e){
        var p = e.target;
        var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
        var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象
        map.openInfoWindow(infoWindow,point); //开启信息窗口
    }


}