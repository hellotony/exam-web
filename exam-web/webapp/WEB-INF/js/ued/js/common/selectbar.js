/**
 * 自定义下拉菜单
 */
;
(function(window, document, undefined) {
	$(document).click(function(){
		$(".select-wrapper").removeClass("s-open")
	})
	
	
	var Select = {};
	
	Select.selectThis = function(el,e){
		e.stopPropagation();
		var _this = $(el);
		var _parent = _this.parent();
		var _select = _parent.siblings("select");
		var index = _this.index();
		_select.val(_select.find("option").eq(index).val());
		_parent.parent().removeClass("s-open");
		_select.change();
	}
	
	Select.showList = function(el,e){
		e.stopPropagation();
		var _this = $(el);
		var _select = _this.siblings("select");
		var _options = _select.find("option");
		var _li = "";
		var _ul = _this.siblings("ul");
		
		if(_ul.is(".pos-fix")){
			//解决响应式表格里面  下拉框隐藏bug
			var left = _this.offset().left;
			var top = _this.offset().top;
			var width = _this.width()*(parseInt(_ul.attr("data-width"))||100)/100;
			_ul.css({
				"position":"fixed",
				"left": left,
				"top": top + _this.height(),
				"width": width
			});
			$(".page-container").get(0).onmousewheel = function(){
				_ul.hide(function(){ $(".page-container").unbind();})
				.parent().removeClass("s-open");
			}
		}
		
		if(_this.parent().is(".s-open")){
			$(".select-wrapper").removeClass("s-open");//先隐藏其他显示的下拉菜单
			return;
		}
		$(".select-wrapper").removeClass("s-open");//先隐藏其他显示的下拉菜单
		
		if(_this.prev().attr("disabled")=="disabled"){
			return;
		}
		if(!_options.size()){
			return;
		}
		
		//解决联动不更新数据bug
		if(_ul.attr("data-ok")!="true"||_ul.attr("data-reload")=="true"){
			_options.each(function(i){
				var title = "";
				if($(this).attr("title")){
					title = $(this).attr("title");
				}
				if(_ul.attr("data-boardLocation")=="true"){ //只针对上车地点的显示
					if($.trim($(this).text()).length>23){ //如果大于23个字符，则显示title
						title = title||$(this).text();
					}
				}
				_li += "<li class='select-li' onclick='Select.selectThis(this,event)' "+
							"title='"+title+"'>"+$(this).text()+"</li>";
			});
			_ul.empty().attr("data-ok","true").append(_li);
		}
		_ul.parent().addClass("s-open");
	}

	/*
	* 战士模糊搜索  下拉框
	*/
	Select.showFuzzySearchList = function(el,e){
		$(".select-wrapper").removeClass("s-open");
		e.stopPropagation();
		var _this = $(el);
		var val = $.trim(_this.val());
		var _select = _this.siblings("select");
		var _options = _select.find("option");
		var _parent = _this.parent();
		var _li = "";
		var _ul = _this.siblings("ul");

		//绑定键盘事件
		_this.unbind("keyup").bind("keyup",function(){
			if(!_this.val()){//注入框为空，显示默认列表
				_parent.addClass("s-open");
			}else{
				_parent.removeClass("s-open");
			}
		})

		if(val||_parent.is(".s-open")){//如果已经有值，或者已经展开，不做任何操作
			return;	
		}

		if(_ul.attr("data-ok") != "true"){//如果没有渲染过，则渲染数据
			_options.each(function(i){
				var _opt = $(this);
				var title = "";
				if(_opt.attr("title")){
					title = _opt.attr("title");
				}
				_li += "<li class='select-li' data-id='" + _opt.val() + "' data-text='" +_opt.text() + "'";
				_li += "onclick='Select.selectThisItem(this,event)' " + "title='"+title+"'>"+_opt.text()+"</li>";
			});
			_ul.empty().attr("data-ok","true").append(_li);
		}

		_parent.addClass("s-open");
	}



	/*
	* 选择模糊搜索选项
	*/
	Select.selectThisItem = function(el,e){
		e.stopPropagation();
		var _this = $(el);
		var _wrapper = _this.parents(".select-wrapper");
		var _input = _wrapper.find("input.select-input");
		_input.attr({
			"textid" : $.trim(_this.attr("data-id")),
			"value" :  $.trim(_this.attr("data-text"))
		});
		_wrapper.removeClass("s-open");

	}



	window.Select = Select;
	
})(window, document);
