<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html >
<html lang="zh-cn">
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/infoWindow.css"/>
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
      #pathpanel{
        left:10px;
      	top:60px;
      	position:absolute;
      	z-index: 10;
      }
      #pathpanel #start{
      	width:300px;
      }
      #pathpanel #end{
      	width:300px;
      }
    </style>
    <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=880f7eba81210cfb1cd402e6d44248f0&plugin=AMap.Walking,AMap.Autocomplete,AMap.PlaceSearch"></script>
    <script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
   	<title>寻物启事</title>
  </head>
  <body>
	<div id="container"></div>
	<div id="search">
	  <input id="getlostinfo" type="button" onclick="getAllInfo()" value="getAllInf">
	  <input id="addlostinfo" type="button" onclick="addLostInfo()" value="发布区域">
	  <input id="addlostinfo" type="button" onclick="createPathPanel()" value="发布路线">
	</div>

	
	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
 
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    
	<script>
	var map = new AMap.Map('container', {
    	resizeEnable: true,
        center: [119.209636, 26.03011],    // 地图中心点
        zoom: 15,    // 地图显示的缩放级别
        expandZoomRange:true
    });
	AMap.service('AMap.Geocoder',function(){//回调函数
	    //实例化Geocoder
	    geocoder = new AMap.Geocoder({
	        city: "010"//城市，默认：“全国”
	    });
	    //TODO: 使用geocoder 对象完成相关功能
	});
	</script>
	<script src="js/addMarker.js"></script>
	<script src="js/infoWindow.js"></script>
	<script src="js/createinfoWindow.js"></script>
	<script src="js/createPathPanel.js"></script>
	<script>
	
	
	function getAllInfo(){
		$.ajax({
			url:"GetAllLostInfoServlet",    // 传递给Servlet
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
		// 添加地图的click事件
		var clickMap = function(e) {
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
    	    map.off('click',clickMap);
    	}
    	map.on('click', clickMap);
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