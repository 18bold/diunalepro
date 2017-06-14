<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html >
<html lang="zh-cn">
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/infoWindow.css"/>
    <link href="css/uploader-image.css" rel="stylesheet"  type="text/css" />
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
      #picSearch{
      	left:10px;
      	top:60px;
      	position:absolute;
      	z-index: 10;
      }
      #upload-target { margin-bottom: 15px; }
	  #upload-view { min-height: 200px; _height: 200px; background: #fff; border: 1px solid green; }
	  #log { margin: 10px 0; white-space: nowrap; clear: both; }
	  .x-button { padding: 5px 25px; border-radius: 3px; text-align: center; text-decoration: none; background-color: #0a82e4; color: #ffffff; font-size: 17px; margin: 0; white-space: nowrap; cursor: pointer; min-width: 60px; _width: 60px; }
	  
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
	  <input id="addlostbypath" type="button" onclick="createPathPanel()" value="发布路线">
	  <input id="createPicSearch" type="button" onclick="createPicSearch()" value="图片搜索">
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
	<script type="text/javascript" src="js/addMarker.js"></script>
	<script type="text/javascript" src="js/infoWindow.js"></script>
	<script type="text/javascript" src="js/createinfoWindow.js"></script>
	<script type="text/javascript" src="js/createPathPanel.js"></script>
	<script type="text/javascript" src="js/Q.js"></script>
	<script type="text/javascript" src="js/Q.Uploader.js"></script>
	<script type="text/javascript" src="js/Q.Uploader.Image.js"></script>
	<script>
    function write(msg) {
       // document.getElementById("log").innerHTML += (msg != undefined ? msg : " ") + "<br />";
    }
	function createPicSearch()
	{
		if (document.getElementById("div")===null)
		{
			var picSearch = document.createElement("div");
			picSearch.id = "picSearch";
			var da = document.createElement("div");
			var a = document.createElement("a");
			a.id = "upload-target";
			a.className = "x-button";
			a.innerHTML = "添加图片并上传";
			var view = document.createElement("div");
			view.id = "upload-image-view";
			view.className = "clearfix";
			var log = document.createElement("div");
			da.appendChild(a);
			picSearch.appendChild(da);
			picSearch.appendChild(view);
			picSearch.appendChild(log);
		    document.body.appendChild(picSearch);

	        var Uploader = Q.Uploader,
	            formatSize = Q.formatSize,
	            boxView = document.getElementById("upload-image-view");

	        var uploader = new Uploader({
	            url: "PicSearchLostServlet",
	            target: document.getElementById("upload-target"),
	            view: boxView,
	            //auto: false,

	            allows: ".jpg,.png,.gif,.bmp",

	            //图片缩放
	            scale: {
	                //要缩放的图片格式
	                types: ".jpg",
	                //最大图片大小(width|height)
	                maxWidth: 600
	            },
	            data: {

	            },

	            on: {
	                //添加之前触发
	                add: function (task) {
	                    if (task.disabled) return alert("允许上传的文件格式为：" + this.ops.allows);
	                },
	                //图片预览后触发
	                preview: function (data) {
	                    write(data.task.name + " : " + data.src);
	                },
	                //图片压缩后触发,如果图片或浏览器不支持压缩,则不触发
	                scale: function (data) {
	                    write(data.task.name + " : 已压缩！");
	                }
	            },
	            UI: {
	            	over: function (task) {
	            		//alert("时间" + task.time + "大小" + task.size + "上传的大小" + task.loaded);
	            		alert(task.json.message +" " + task.json.result.pic);
	            	}
	            }
	        });
		}
		
	}
    function log(msg) {
        document.getElementById("log").innerHTML += (msg != undefined ? msg : "") + "<br />";
    }
    
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