
// 加载数据
function loaddata() {
	$.ajax({
		type:"GET",
		url:"http://127.0.0.1:9999/device/getAllLatestDevice",
		dataType:"json",
		success:function(result){
			var str=null;
			for(var i=0;i<result.data.length;i++){


				var unixTimestamp = new Date( result.data[i].createtime ) ;
				commonTime = unixTimestamp.toLocaleString();
				//alert(commonTime);
				var unixTimestamp1 = new Date( result.data[i].updatetime ) ;
				commonTime1 = unixTimestamp1.toLocaleString();

				str+="<tr>";
				str+="<td>"+result.data[i].id+"</td>";
				str+="<td>"+result.data[i].deviceCode+"</td>";
				str+="<td>"+result.data[i].deviceDesc+"</td>";
				str+="<td>"+result.data[i].address+"</td>";
				str+="<td>"+result.data[i].longitude+"</td>";
				str+="<td>"+result.data[i].latitude+"</td>";
				str+="<td>"+result.data[i].sim+"</td>";
				str+="<td>"+result.data[i].protocol+"</td>";
				str+="<td>"+result.data[i].collectInterval+"</td>";
				str+="<td>"+commonTime+"</td>";
				str+="<td>"+commonTime1+"</td>";
				str+="<td><a href=\"javascript:void(0);\" onclick='doDel("+result.data[i].id+",this)'>删除</a></td>";
				str+="</tr>";

			}
			$("#stuid tbody").empty();
			$("#stuid tbody").append(str);
		}
	});
}

loaddata();
