
//css,js静态文件加载器
//项目中依赖的所有静态文件
var cssArr = {
	bootstrap : {
		bootstrap_datetimepicker: "/css/bootstrap/bootstrap-datetimepicker.css",
		bootstrap : "/css/bootstrap/bootstrap.css",
		bootstrap_css : "/css/bootstrap/bootstrap_map.css",
		bootstrap_map : "/css/bootstrap/bootstrap.css.map",
		bootstrap_min_map : "/css/bootstrap/bootstrap.min.css.map",
		bootstrap_min : "/css/bootstrap/bootstrap.min.css",
		bootstrap_min_css : "/css/bootstrap/bootstrap.min_map.css",
		font_awesome : "/css/bootstrap/font-awesome.css"
	},
	common : {
		btn : "/css/common/btn.css",
		index : "/css/common/index.css",
		jquery_ui : "/css/common/jquery-ui.css",
		sideBar : "/css/common/sideBar.css",
		eleDetail : "/css/common/eleDetail.css"
	},
	gal : "/css/gal/gal.css",
	invoice : "/css/invoice/invoice.css",
	my97date : {
		skin : {
			def : "js/My97DatePicker/skin/default/datepiker.css",
			whyGreen : "js/My97DatePicker/skin/whyGreen/datepiker.css"
		},
		WdatePicker : "js/My97DatePicker/skin/WdatePicker.css"
	},
	pagination : "/css/pagination/jqpagination.css",
	slide : "/css/slide/slide_block.css",
	whole : "/css/whole/whole.css",
	zTree : "/css/ZTree/zTreeStyle.css",
	desc_deta : "/css/desc_deta.css",
	index : "/css/index.css",
	occupying : "/css/occupying.css",
	style : "/css/style.css",
	riskTest : "/css/common/risk_test.css",
	TNSearch : "/css/TNSearch.css",

    map : "/css/map/map.css",
	main : "/css/main/main.css",
	login : {
		login : "/css/login/login.css",
		style : "/css/login/style-responsive.css",
		bootstrap_min : "/css/login/bootstrap.min.css",
		bootstrap_reset : "/css/login/bootstrap-reset.css",
		custom : "/css/login/custom-ico-fonts.css",
		jquery_ui : "/css/login/jquery-ui-1.10.3.css"
	}
}

var jsArr = {
	bootstrap : "/js/bootstrap/bootstrap.js",
	calendar : {
		kalendae : "/js/calendar/kalendae.js"
	},
	common : {
		base64 : "/js/common/base64.js",
		bootstrap_paginator : "/js/common/bootstrap-paginator.js",
		hours_panel : "/js/common/HoursPanel.js",
		jbootstrap_page : "/js/common/jBootstrapPage.js",
		jquery : "/js/common/jquery-3.1.0.js",
		jquery_ui : "/js/common/jquery-ui.min.js",
		jquery_jqpagination : "/js/common/jquery.jqpagination.js",
		jquery_treeview_pack : "/js/common/jquery.treeview.pack.js",
		json2 : "/js/common/json2.js",
		scrollpic2 : "/js/common/scrollpic2.js",
		selectbar : "/js/common/selectbar.js",
		system : "/js/common/system.js",
		TNSearch : "/js/common/TNSearch.js",
		string : "/js/common/string.js",
		ajaxfileupload:"/js/common/jquery.ajaxfileupload.js"
	},
	my97date : {
		lang : {
			en : "/js/My97DatePicker/lang/en.js",
			zh_cn : "/js/My97DatePicker/lang/zh_cn.js",
			zh_tw : "/js/My97DatePicker/lang/zh_tw.js"
		},
		skin : {},
		calendar : "/js/My97DatePicker/calendar.js",
		WdatePicker : "/js/My97DatePicker/WdatePicker.js"
	},
	weather : "/js/weather/commonWeather.js",
	login :{
		login : "/js/login/login.js",
		jquery_1_10_2 : "/js/login/jquery-1.10.2.min.js",
		bootstrap : "/js/login/bootstrap.min.js",
		modernizr : "/js/login/modernizr.min.js"
	},
	map : "/js/map/map.js",
	main : "/js/main/main.js",
	alarm : "/js/alarm/alarm.js",
	deviceStatus : "/js/deviceStatus/deviceStatus.js",
	common_js : "/js/common/common_js.js",
	add_js : "/js/common/add_js.js",
	del_js : "/js/common/del_js.js",
	update_js : "/js/common/update_js.js",
	answer : "/js/common/answer.js",
	login_success : "/js/login/login_success.js",
	eleNeigh : "/js/eleNeigh/eleNeigh.js"
}

//前端css、js等资源
var uedFrom = "";

var href = window.location.href;

if(href.indexOf("localhost") != -1){
	uedFrom = "http://localhost:8080/exam-web/js/ued"
} else {
	uedFrom = "http://180.76.142.131:8080/exam-web/js/ued"
}

;(function(window){
	var JSLoader = {};

	JSLoader.loader = function(config){
		var cssjs = "";
		for(var i=0;i<config.cssArr.length;i++){
			cssjs += '<link rel="stylesheet" type="text/css" href="'+uedFrom+config.cssArr[i]+'"></link>';
		}
		
		for(var i=0;i<config.jsArr.length;i++){
			cssjs += '<script type="text/javascript" src="'+uedFrom+config.jsArr[i] + '" + ></script>';
		}
		document.write(cssjs);
	}
	window.JSLoader = JSLoader;

})(window);
