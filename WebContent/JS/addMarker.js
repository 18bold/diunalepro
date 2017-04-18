/**
 * 添加Marker
 * map地图对象，data数据
 */
function addMarkers(map,data)
{
	var Arr=data.datas;    // 失物招领信息数组
    var num=data.count;    // marker个数
    map.clearMap();    // 清除覆盖物
	for (var x = 0; x < num; x++)
	{	
		// 使用闭包，为每个marker添加相应的infoWindow
		(function (i)    
		{
			var marker = new AMap.Marker({
				position: [Arr[i].lng,Arr[i].lat]
			});
			marker.setMap(map);
			
			// 实例化信息窗体
		    var title = Arr[i].name,    // 物体名称
		        content = [];
		    content.push('<img src="'+Arr[i].pic+'">');
		    content.push("电话：010-64733333");
		    content.push("<a href=''>详细信息</a>");
		    var infoWindow = new AMap.InfoWindow({
		        isCustom: true,  // 使用自定义窗体
		        content: createInfoWindow(title, content.join("<br/>")),
		        offset: new AMap.Pixel(16, -45)
		    });
		    
			// 鼠标点击marker弹出自定义的信息窗体
	        AMap.event.addListener(marker, 'click', function() {
	            infoWindow.open(map, marker.getPosition());
	        });
		})(x)
	}
}