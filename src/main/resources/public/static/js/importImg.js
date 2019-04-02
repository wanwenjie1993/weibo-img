layui.use('element', function(){
  var element = layui.element;
  element.on('nav(demo)', function(elem){
    //console.log(elem)
    layer.msg(elem.text());
  });
});
$(document).ready(function() {
	$('.picurl > input').bind('focus mouseover',
		function() {
			if(this.value) {
				this.select()
			}
		});
	$("input[type='file']").change(function(e) {
		images_upload(this.files)
	});
	var obj = $('body');
	obj.on('dragenter',
		function(e) {
			e.stopPropagation();
			e.preventDefault()
		});
	obj.on('dragover',
		function(e) {
			e.stopPropagation();
			e.preventDefault()
		});
	obj.on('drop',
		function(e) {
			e.preventDefault();
			images_upload(e.originalEvent.dataTransfer.files)
		})
});

function url_uploaddj() {
layer.open({
  type: 1 //Page层类型
  ,area: ['500px', '300px']
  ,title: '在线上传'
  ,shade: 0.6 //遮罩透明度
  ,maxmin: true //允许全屏最小化
  ,anim: 0 //0-6的动画形式，-1不开启
  ,content: '<textarea class="form-control-jsxs" name="urls" rows="3" id="urls" placeholder="在这里输入图片链接,支持多个链接(一行一个链接)同时上传喔~"></textarea><div class="jsxs-an"><button type="button" class="layui-btn layui-btn-normal" onclick="url_upload();">上传</button></div>'
});  
};


var url_upload = function() {
	var urls = $("#urls").val();
	var url_arr = urls.split("\n");
	if(urls == "" || url_arr.length == 0) {
		layer.msg('请贴入需要上传的网络图片地址.');
		return
	}else{
		layer.closeAll();
	}
	$('.layui-row > button')[1].innerHTML = '上传中';
	for(var i = 0; i < url_arr.length; i++) {
		$.ajax({
			url: 'http://127.0.0.1:8092/sina/upload.php',
			type: 'POST',
			data: {
				img: url_arr[i]
			},
			cache: false,
			dataType: 'json',
			success: function(data) {
				flag++;
				if(data.success) {
					$('#url-res-txt').append('' + data.data[0] + '\n');
					$('.layui-row > button')[0].innerHTML = '成功 ' + flag + '/' + files.length;
					var apc = "<img src='" + data.data[0] + "'><p>" + data.data[0] + "</p><br>";
					$('.preview').css('display', 'block');
					$(".preview>hr").after(apc)
				} else {
					$('.layui-row > button')[0].innerHTML = '第' + flag + '张上传失败'
				}
			},
			error: function(XMLResponse) {
				alert("error:" + XMLResponse.responseText)
			}
		})
	}
};


var images_upload = function(files) {
	var flag = 0;
	$('textarea').empty();
	if(files.length>6){
		alert("一次最大上传6张");
		return;
	}
	$(files).each(function(key, value) {
		$('.layui-row > button')[0].innerHTML = '上传中';
		image_form = new FormData();
		image_form.append('file', value);
		console.log(image_form);
		$.ajax({
			url: '/sina/upload.php',
			//multipart本地图片上传
			type: 'POST',
			data: image_form,
			mimeType: 'multipart/form-data',
			contentType: false,
			cache: false,
			processData: false,
			dataType: 'json',
			success: function(data) {
				flag++;
				if(data.success) {
					$('#url-res-txt').append('' + data.data[0] + '\n');
					$('.layui-row > button')[0].innerHTML = '成功 ' + flag + '/' + files.length;
					var apc = "<hr /><img src='" + data.data[0] + "'><p>" + data.data[0] + "</p><br>";
					$('.preview').css('display', 'block');
					$(".preview").append(apc)
				} else {
					$('.layui-row > button')[0].innerHTML = '第' + flag + '张上传失败'
				}
			},
			error: function(XMLResponse) {
				alert("error:" + XMLResponse.responseText)
			}
		})
	})
};
document.onpaste = function(e) {
	var data = e.clipboardData;
	for(var i = 0; i < data.items.length; i++) {
		var item = data.items[i];
		if(item.kind == 'file' && item.type.match(/^image\//i)) {
			var blob = item.getAsFile();
			images_upload(blob)
		}
	}
}

!
function() {
	function n(n, e, t) {
		return n.getAttribute(e) || t
	}

	function e(n) {
		return document.getElementsByTagName(n)
	}

	function t() {
		var t = e("script"),
			o = t.length,
			i = t[o - 1];
		return {
			l: o,
			z: n(i, "zIndex", -1),
			o: n(i, "opacity", .5),
			c: n(i, "color", "0,0,0"),
			n: n(i, "count", 99)
		}
	}

	function o() {
		a = m.width = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth,
			c = m.height = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight
	}

	function i() {
		r.clearRect(0, 0, a, c);
		var n, e, t, o, m, l;
		s.forEach(function(i, x) {
				for(i.x += i.xa, i.y += i.ya, i.xa *= i.x > a || i.x < 0 ? -1 : 1, i.ya *= i.y > c || i.y < 0 ? -1 : 1, r.fillRect(i.x - .5, i.y - .5, 1, 1), e = x + 1; e < u.length; e++) n = u[e],
					null !== n.x && null !== n.y && (o = i.x - n.x, m = i.y - n.y, l = o * o + m * m, l < n.max && (n === y && l >= n.max / 2 && (i.x -= .03 * o, i.y -= .03 * m), t = (n.max - l) / n.max, r.beginPath(), r.lineWidth = t / 2, r.strokeStyle = "rgba(" + d.c + "," + (t + .2) + ")", r.moveTo(i.x, i.y), r.lineTo(n.x, n.y), r.stroke()))
			}),
			x(i)
	}
	var a, c, u, m = document.createElement("canvas"),
		d = t(),
		l = "c_n" + d.l,
		r = m.getContext("2d"),
		x = window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame ||
		function(n) {
			window.setTimeout(n, 1e3 / 45)
		},
		w = Math.random,
		y = {
			x: null,
			y: null,
			max: 2e4
		};
	m.id = l,
		m.style.cssText = "position:fixed;top:0;left:0;z-index:" + d.z + ";opacity:" + d.o,
		e("body")[0].appendChild(m),
		o(),
		window.onresize = o,
		window.onmousemove = function(n) {
			n = n || window.event,
				y.x = n.clientX,
				y.y = n.clientY
		},
		window.onmouseout = function() {
			y.x = null,
				y.y = null
		};
	for(var s = [], f = 0; d.n > f; f++) {
		var h = w() * a,
			g = w() * c,
			v = 2 * w() - 1,
			p = 2 * w() - 1;
		s.push({
			x: h,
			y: g,
			xa: v,
			ya: p,
			max: 6e3
		})
	}
	u = s.concat([y]),
		setTimeout(function() {
				i()
			},
			100)
}();