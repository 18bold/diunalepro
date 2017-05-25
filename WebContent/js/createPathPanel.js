/**
 * 
 */
function initPath()
{
	//输入提示
	var autoOptions1 = {
	    input: "start"
	};
	var autoOptions2 = {
		    input: "end"
	};
	var auto1 = new AMap.Autocomplete(autoOptions1);
	var auto2 = new AMap.Autocomplete(autoOptions2);
	placeSearch = new AMap.PlaceSearch({
	    map: map,
		panel:panel
	});  //构造地点查询类
	AMap.event.addListener(placeSearch, "markerClick", markerClick);//注册监听，当选中某条记录时会触发
	AMap.event.addListener(auto1, "select", select);//注册监听，当选中某条记录时会触发
	AMap.event.addListener(auto2, "select", select);//注册监听，当选中某条记录时会触发
}
function select(e) 
{
    placeSearch.setCity(e.poi.adcode);
    placeSearch.search(e.poi.name);  //关键字查询查询
}
function markerClick(e)
{
	alert("点击");
}

var StartMarker,EndMarker;

function createPathPanel()
{
	var pathPanel = document.createElement("div");
	pathPanel.id = "pathpanel";
	
	var StartPanel = document.createElement("div");
	var txtStart = document.createElement("input");
	txtStart.id = "start";
	txtStart.name = "start";
	txtStart.type = "text";
	var btnStart = document.createElement("input");
	btnStart.type = "button";
	btnStart.value = "选择起点"
	btnStart.onclick = addStartMarker;
	StartPanel.appendChild(txtStart);
	StartPanel.appendChild(btnStart);
	
	var EndPanel = document.createElement("div");
	var txtEnd = document.createElement("input");
	txtEnd.id = "end";
	txtEnd.name = "end";
	txtStart.type = "text";
	var btnEnd = document.createElement("input");
	btnEnd.type = "button";
	btnEnd.value = "选择终点"
	btnEnd.onclick = addEndMarker;
	EndPanel.appendChild(txtEnd);
	EndPanel.appendChild(btnEnd);
	
	// 定义中部内容
    var middle = document.createElement("div");
    middle.className = "new-info-middle";
    middle.style.backgroundColor = 'white';
    // 创建一个表单
    var form = document.createElement("form");
    form.id = "pathinfoform";
    form.name = "pathinfoform";
    form.enctype= "multipart/form-data";
    var lable = document.createElement("lable");
    lable.innerHTML = "名称";
    // 创建一个input输入name
    var name = document.createElement("input");
    name.type = "text";
    name.name = "name";
    // 创建一个input输入file
    var file = document.createElement("input");
    file.type = "file";
    file.name = "pic";
    // 创建一个input存储patharr
    var patharr = document.createElement("input");
    patharr.type = "hidden";
    patharr.name = "patharr";
    patharr.id = "patharr";
    var sub = document.createElement("input");
    sub.type = "button";
    sub.value = "发布";
    sub.onclick = sendPathForm;
    
    form.appendChild(lable);
    form.appendChild(name);
    form.appendChild(file);
    form.appendChild(patharr);
    form.appendChild(sub);
    middle.appendChild(form);
    
	var panel = document.createElement("div");
	panel.id = "panel";
	
	pathPanel.appendChild(StartPanel);
	pathPanel.appendChild(EndPanel);
	pathPanel.appendChild(middle);
	pathPanel.appendChild(panel);
	
	document.body.appendChild(pathPanel);
	initPath();
}
function addStartMarker()
{
	map.setDefaultCursor("crosshair");    // 设置鼠标样式
	// 添加地图的click事件
	var clickMap = function(e) {
		var markerOption = {  
	        map:map,
		    draggable: true,
		    position:[e.lnglat.getLng(),e.lnglat.getLat()]
		};
		StartMarker=new AMap.Marker(markerOption); 
		map.panTo([e.lnglat.getLng(),e.lnglat.getLat()]);
		map.setDefaultCursor("pointer");
	    map.off('click',clickMap);
	 	//逆地理编码
	    var lnglatXY=[e.lnglat.getLng(),e.lnglat.getLat()];//地图上所标点的坐标
	    geocoder.getAddress(lnglatXY, function(status, result) {
	        if (status === 'complete' && result.info === 'OK') {
	           //获得了有效的地址信息:
	           //即，result.regeocode.formattedAddress
	           document.getElementById("start").value=result.regeocode.formattedAddress;
	        }else{
	           //获取地址失败
	        }
	    }); 
    	if (EndMarker ==null)
    	{}
    	else
    	{
    		getPath(StartMarker,EndMarker);
    	}
	}
	map.on('click', clickMap);

}
function addEndMarker()
{
	map.setDefaultCursor("crosshair");    // 设置鼠标样式
	// 添加地图的click事件
	var clickMap = function(e) {
		var markerOption = {  
	        map:map,
		    draggable: true,
		    position:[e.lnglat.getLng(),e.lnglat.getLat()]
		};
		EndMarker=new AMap.Marker(markerOption); 
		map.panTo([e.lnglat.getLng(),e.lnglat.getLat()]);
		map.setDefaultCursor("pointer");
	    map.off('click',clickMap);
	 	//逆地理编码
	    var lnglatXY=[e.lnglat.getLng(),e.lnglat.getLat()];//地图上所标点的坐标
	    geocoder.getAddress(lnglatXY, function(status, result) {
	        if (status === 'complete' && result.info === 'OK') {
	           //获得了有效的地址信息:
	           //即，result.regeocode.formattedAddress
	           document.getElementById("end").value=result.regeocode.formattedAddress;
	        }else{
	           //获取地址失败
	        }
	    }); 
    	if (StartMarker ==null)
    	{}
    	else
    	{
    		getPath(StartMarker,EndMarker);
    	}
	}
	map.on('click', clickMap);
}
function getPath(StartMarker,EndMarker)
{
    // 步行导航
    var walking = new AMap.Walking({
        map: map
    }); 
    // 根据起终点坐标规划步行路线
    walking.search([StartMarker.getPosition().getLng(),StartMarker.getPosition().getLat()], [EndMarker.getPosition().getLng(),EndMarker.getPosition().getLat ()],function(status, result){
		if(status === 'complete'){
			var arr = result.routes;
			var patharr = "";
			for (var i=0; i<result.count; i++)
			{
				var brr = arr[i].steps;
				for (var j in brr)
				{
					var crr = brr[j].path;
					for (var k=0; k<crr.length; k++)
					{
						patharr += crr[k].toString( );
						patharr += "@";
					}
				}
			}
			console.log(patharr);
			var patharrform = document.getElementById("patharr");
			patharrform.value = patharr;
        }
    });
}
function sendPathForm()
{
	var form = new FormData(document.getElementById("pathinfoform"));
    $.ajax({
        url:"AddLostInfoByPathServlet",
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