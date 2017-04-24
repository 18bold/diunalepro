/**
 * 
 */
	// 自定义窗体创建的函数 position:marker的坐标
	function createinfoWindow(position)
	{
		var info = document.createElement("div");
        info.className = "new-info";

        // 定义顶部标题
        var top = document.createElement("div");
        var titleD = document.createElement("div");
        var closeX = document.createElement("img");
        top.className = "info-top";
        titleD.innerHTML = "发布";
        closeX.src = "http://webapi.amap.com/images/close2.gif";
        closeX.onclick = closeInfoWindow;

        top.appendChild(titleD);
        top.appendChild(closeX);
        info.appendChild(top);

        // 定义中部内容
        var middle = document.createElement("div");
        middle.className = "new-info-middle";
        middle.style.backgroundColor = 'white';
        // 创建一个表单
        var form = document.createElement("form");
        form.id = "newinfoform";
        form.name = "newinfoform";
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
        var lng = document.createElement("input");
        lng.type = "hidden";
        lng.name = "lng";
        lng.value = position.getLng();
        var lat = document.createElement("input");
        lat.type = "hidden";
        lat.name = "lat";
        lat.value = position.getLat();
        var sub = document.createElement("input");
        sub.type = "button";
        sub.value = "确定";
        sub.onclick = sendForm;
        
        form.appendChild(lable);
        form.appendChild(name);
        form.appendChild(file);
        form.appendChild(lng);
        form.appendChild(lat);
        form.appendChild(sub);
        middle.appendChild(form);
        info.appendChild(middle);

        // 定义底部内容
        var bottom = document.createElement("div");
        bottom.className = "info-bottom";
        bottom.style.position = 'relative';
        bottom.style.top = '0px';
        bottom.style.margin = '0 auto';
        var sharp = document.createElement("img");
        sharp.src = "http://webapi.amap.com/images/sharp.png";
        bottom.appendChild(sharp);
        info.appendChild(bottom);
        return info;
	}