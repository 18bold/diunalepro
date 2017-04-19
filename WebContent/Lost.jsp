<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html lang="zh-cn">
  <head>
  	<meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <link rel="stylesheet" href="CSS/infoWindow.css"/>
    <style type="text/css">
      body,html,#container{
        height: 100%;
        margin: 0px;
        position:relative;
        z-index: 0;
      }
      #search{
      	left:10px;
      	top:30px;
      	position:absolute;
      	z-index: 10;
      }
    </style>
    <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=880f7eba81210cfb1cd402e6d44248f0&plugin=AMap.Walking"></script>
    <script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
   	<title>失物招领</title>
  </head>
  <body>
	<div id="container"></div>
	<div id="search">
	  <input id="getlostinfo" type="button" onclick="getAllInfo()" value="加载">
	  <input id="addlostinfo" type="button" onclick="addLostInfo()" value="添加">
	</div>
	<script>
	var map = new AMap.Map('container', {
    	resizeEnable: true,
        center: [119.209636, 26.03011],    // 地图中心点
        zoom: 15,    // 地图显示的缩放级别
        expandZoomRange:true
    });
	</script>
	<script src="JS/addMarker.js"></script>
	<script src="JS/infoWindow.js"></script>
	<script src="JS/createinfoWindow.js"></script>
	<script>
	// 利用步行导航绘制路径
	function getInfoByPath()
	{
	    // 步行导航
	    var walking = new AMap.Walking({
	        map: map
	    }); 
	    // 根据起终点坐标规划步行路线
	    walking.search([119.212483,26.023488], [119.208971, 26.032154],function(status, result){
			if(status === 'complete'){
				var arr = result.routes;
				for (var i=0; i<result.count; i++)
				{
					var brr = arr[i].steps;
					for (var j in brr)
					{
						var crr = brr[j].path;
						for (var k in crr)
						{
							console.log(crr[k]);
						}
					}
				}
	        }
	    });
	}
	getInfoByPath();
	function getAllInfo(){
		$.ajax({
			url:"GetLostInfoServlet",    // 传递给Servlet
			type:"GET",    // get方法
			dataType:"json",
			data:{
				method:"getallinfo"
			},
			success:function(data){
				var obj=eval(data);
				addMarkers(map,obj);
			},
			error:function(data){
				var obj=eval(data);
				alert(obj);
			}
		});
	}
	
	// 发布寻物启事
	function addLostInfo()
	{
		map.clearMap();    // 清除覆盖物
		map.setDefaultCursor("crosshair");    // 设置鼠标样式
    	map.on('click', function(e) {
    		map.clearMap();
    		var markerOption = {  
		        map:map,
			    draggable: true,
			    position:[e.lnglat.getLng(),e.lnglat.getLat()]
    		};
    		newmarker=new AMap.Marker(markerOption); 
    		map.panTo([e.lnglat.getLng(),e.lnglat.getLat()]);
    		map.setDefaultCursor("pointer");
    		// 设置marker的监听事件
    		AMap.event.addListener(newmarker,'click',newinfoWindow);
    	});
    	map.setFitView();
	}
	// marker的监听事件:创建新的窗体
	function newinfoWindow()
	{
		var infoWindow=new AMap.InfoWindow({
			isCustom: true,  // 使用自定义窗体
	        content: createinfoWindow(this.getPosition()),    // 自定义窗体创建的函数（在createinfoWindow.js里）
	        offset: new AMap.Pixel(16, -45)
        });
        infoWindow.open(map,this.getPosition());
	}
	function sendForm()
	{
		var form = new FormData(document.getElementById("newinfoform"));
        $.ajax({
            url:"AddLostInfoServlet",
            type:"post",
            data:form,
            processData:false,
            contentType:false,
            success:function(data){
            	alert("success")
            },
            error:function(e){
                alert("faild");
            }
        });    
	}
	</script>
  </body>
</html>