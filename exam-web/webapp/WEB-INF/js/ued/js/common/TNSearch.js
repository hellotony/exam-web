/**
 * 搜索控件
 * @author cwj & yq
 * @author 2013-07-18
 */
(function(window, document, undefined) {
	//搜索内容默认模板
	var searchHtml = {
		//整体布局
		wrap: "<div class='searchDiv' style='display:none'><div class='searchHeader'></div><ul class='searchList' style='width:100%;height:auto;'></ul><div class='searchFooter'></div></div>",
		//头部布局
		header: [''],
		//中间布局
		//带有title的搜索列表
		center: [
			'<li class="searchRes" extrakey="@<%=key%>#" title="@<%=title%>#">@<%=key%>#<span class="ellipsis" style="">@<%=title%>#</span><div style="width:100%; height:1px; overflow:hidden;"></div></li>',
			'<li class="searchRes" extrakey="@<%=key%>#">@<%=key%>#</li>',
			'<li id="tempInputContent" style="display:none" extrakey="<%=key%>"></li>'
		],
		tempInputContent: ['<li id="tempInputContent" style="display:none" extrakey="<%=key%>"></li>'],
		//底部布局(暂不开放)
		footer: ['<div style="right: 2px; position: absolute; bottom: 4px;"><a class="pag_prevTool pag_tool" href="javascript:void(0)" style="margin-left: 5px; background-color: rgb(204, 204, 204); color: rgb(255, 255, 255); opacity: 0.8;"><span><%=prev%></span></a><a class="pag_nextTool pag_tool" href="javascript:void(0)" style="background-color: rgb(204, 204, 204); color: rgb(255, 255, 255);"><span><%=next%></span></a></div><div style="top: 2px; position: absolute;left: <%=left%>" class="alert alert-info"></div>']
	};
	//默认参数
	var defaults = {
		view: "default", //default/mutiple/tree
		width: '280px', //body层最大高度
		maxHeight: '400px', //body层最大高度
		count: 4, //一行显示多少列, 仅当view = mutiple时有效
		maxShowCount: 20, //最多显示多少条数据,0为不限制
		checked: false, //是否需要全选按钮
		attrs: [], //选择后添加到input框中的属性
		showTitle: "", //显示每条数据的提示信息，即右侧信息
		showKey: "key", //显示信息
		resultField: "", //数据中转时使用，获取数据中的某些数据
		showNode: "", //用户自定义的显示布局，用户自定义布局时showTitle与showKeys无用
		headNode: searchHtml.header, //预留头部自定义布局
		footerNode: searchHtml.footer, //预留底部自定义布局
		pointCss: '', //单个数据项的CSS样式(暂未开放)
		searchParam: "", //按哪个字段来搜
		param: {}, //搜索参数
		requestModel: 'server', //本地数据local，远程数据server，
		handleData: $.noop, //用户自定义数据处理，比如过滤数据，入参是所有搜索出来的数据(与max-count配合使用)
		selectFn: $.noop, //选择某项数据时的自定义处理事件，入参是选中的数据
		success: $.noop, //回调函数
		noSearch: false, //是否执行搜索操作
		paramChange: $.noop, //搜索参数处理，比如OA接口写得不适用suggest控件，需要在传给后台的参数上做些处理
		time: 300, //延时搜索时间
		noSearchKey: [9, 16, 17, 18, 20, 33, 34, 37, 38, 39, 40, 91, 144], //不执行查询操作的键值9,16,17,18,20,27,33,34,37,38,39,40,91,144
		autoComplete: false, //搜索数据是否默认展示第一条
		isSetAttrs: true, //是否使用属性值
		searchWe: true //是否禁止输入框为空时搜索
	};

	/**
	 * TNSearch构造函数
	 * @param <object> o
	 * @return <void>
	 */
	var TNSearch = function TNSearch(o) {
		if (!this.tnSearch) return new TNSearch(o);
		if (o.param) {
			System.util.merge(o.param, defaults.param);
		}
		this.searchHtml = System.util.merge({}, searchHtml);
		this.opts = System.util.merge(o || {}, TNSearch.defaults, defaults, {
			SID: System.guid()
		});
		wrapCSS['width'] = this.opts.width;
		wrapUlCSS['max-height'] = this.opts.maxHeight;

		this.store = new Store({
			url: this.opts.url,
			model: this.opts.model
		}).setParam(this.opts.param);

		this.nodes = {
			wrapNode: null,
			inputNode: this.opts.el,
			headerNode: null,
			resultNode: null,
			selectedNode: [],
			lastSelectedNode: null,
			footerNode: null
		};
		//所有建议数据
		this.data = null;
		//已选建议数据
		this.selectedData = [];
		this.tnSearch();
	};
	//控件自带参数表
	TNSearch.defaults = {};

	System.util.merge(TNSearch.prototype, {
		/**
		 * 初始化
		 * @return {[type]} [description]
		 */
		tnSearch: function() {
			//初始化函数
			var opts = this.opts;
			//设置
			this.nodes.wrapNode = $(this.searchHtml.wrap).data({
				'opts': opts,
				'grid': this
			});
			this.nodes.wrapNode.attr('id', 'TNSearch_' + opts.SID)
			if (this.opts.el.position() && this.opts.el.position().top) {
				wrapCSS['top'] = this.opts.el.position().top + parseInt(this.opts.el.css('height')) + 5;
			}
			//初始化容器层$('body').append(this.nodes.wrapNode)
			$(this.nodes.wrapNode).css(wrapCSS);
			this.nodes.headerNode = $(".searchHeader", this.nodes.wrapNode);
			this.nodes.resultNode = $(".searchList", this.nodes.wrapNode);
			this.nodes.footerNode = $(".searchFooter", this.nodes.wrapNode);
			this.nodes.resultNode.css(wrapUlCSS);

			//渲染控件
			this.render();
			//事件处理
			this.bindEvent();
			//添加进页面
			this.nodes.inputNode.after(this.nodes.wrapNode);
			return this;
		},

		/**
		 * 数据中转
		 * @param  {[type]} data 请求数据
		 * @return {[type]}      中转后的数据（对象数组）
		 */
		dataTransform: function(data) {
			var grid = this,
				returnData = [];
			if(data){
				//获取数据中resultField
				if (grid.opts.resultField) {
					if (data && System.type.isObject(data)) {
						data = data[grid.opts.resultField];
					} else {
						throw new Error("数据格式错误");
					}
				}

				//每次显示的最大行数
				if(data.length > 0){
					if (grid.opts.maxShowCount && grid.opts.maxShowCount != 0) {
						data.splice(20);
					}		
				}
			}
			//如果外部中转函数存在，则执行外部中转函数
			if (grid.opts.handleData && System.type.isFunction(grid.opts.handleData)) {
				returnData = grid.opts.handleData.call(grid, data);
			}
			return returnData || data;
		},

		/**
		 * 请求控制中转
		 * @param  {[type]}   data 本地数据
		 * @param  {Function} fn   回调函数
		 * @return {[type]}        [description]
		 */
		requestTransform: function(data, fn) {
			var grid = this;
			// 数据处理（请求后台数据或者使用本地数据）
			if (fn && System.type.isFunction(fn)) {
				this.success = fn;
			}
			grid.store.getData(grid, data, fn);
		},

		/**
		 * 请求后渲染页面
		 * @param  {[type]}   data 请求后的数据
		 * @param  {Function} fn   回调函数
		 * @return {[type]}        [description]
		 */
		dealRequestData: function(data, fn) {
			var grid = this;
			//数据中转
			grid.data = grid.dataTransform(data);
			//渲染建议内容(延时)
			grid.appendFlag = setTimeout(function() {
				grid.renderSearchList(grid.data);
			}, 0)
			//回调函数
			if (fn && System.type.isFunction(fn)) {
				fn.call(grid);
			}
		},

		/**
		 * 
		 * @return {[type]} [description]
		 */
		getDataHtml: function() {
			return this.opts.el.html();
		},

		/**
		 * 搜索相关变量重置
		 * @return {[type]} [description]
		 */
		resetSearchEnv: function() {
			var grid = this;
			$('li#tempInputContent', grid.nodes.resultNode).attr('extrakey', grid.nodes.inputNode.val());
			grid.nodes.lastSelectedNode = null;
		},

		/**
		 * 渲染建议层
		 * @return {[type]} [description]
		 */
		render: function() {
			var opts = this.opts;

			if (opts)
			//渲染数据 
			this.renderHeader();
			this.renderFooter();

			//将滚动条放置到第一行
			if (this.nodes.resultNode.scrollTop()) this.nodes.resultNode.scrollTop(0);
			//创建一个隐藏节点用于存储文本框内预设节点
			this.nodes.resultNode.append(System.tmpl(this.searchHtml.center[2], {
				key: this.nodes.inputNode.val()
			}));
			return this;
		},

		/**
		 * 渲染建议层头部
		 * @return {[type]} [description]
		 */
		renderHeader: function() {
			//显示头部数据(全选取消全选按钮)
			// this.nodes.headerNode.html('<div style="height:20px; border:#f00 1px solid;">head</div>');
			return; //返回this.node.headerNode
		},

		/**
		 * 渲染展示层
		 * @param  {[type]} data [description]
		 * @return {[type]}      [description]
		 */
		renderSearchList: function(data) {
			//显示列表信息
			var grid = this;
			var html = '',
				searchTemp;

			//用户自定义布局
			if (!grid.opts.showNode) {
				grid.opts.showTitle ? searchTemp = grid.searchHtml.center[0] : searchTemp = grid.searchHtml.center[1];
				//搜索建议转换
				searchTemp = System.tmpl(searchTemp, {
					key: grid.opts.showKey,
					title: grid.opts.showTitle
				}).replace(/@/gi, "<%=").replace(/#/gi, "%>");
				grid.opts.showNode = searchTemp;
			}
			if (data) {
				$("li", grid.nodes.resultNode).slice(0, $("li", grid.nodes.resultNode).length).remove();
				//如果当前input框已经失去焦点则不继续执行
				if (grid.opts.requestModel == 'server' && document.activeElement != grid.nodes.inputNode[0]) {
					grid.nodes.wrapNode.css("display", "none");
					return;
				}
				//无相关数据
				if (data.length == 0) {
					$('li#tempInputContent', grid.nodes.resultNode).html("无相关数据").show();
					grid.resetSearchEnv();
					return;
				}
				for (var i in data) {
					if (data[i] && System.type.isObject(data[i])) {
						//结果集绑定数据
						html = $(System.tmpl(grid.opts.showNode, data[i])).data({
							"data": data[i]
						});
						grid.nodes.resultNode.append(html);
						//默认选中第一个
						if (i == 0) {
							grid.selectEvent(html, null, null, null, !grid.opts.autoComplete);
						}
					} else {
						throw new Error("数据格式错误");
					}
				}

			}

			//清除标志
			if (grid.appendFlag) {
				clearTimeout(grid.appendFlag);
				grid.appendFlag = false;
			}

			$('li#tempInputContent', grid.nodes.resultNode).html("正在加载数据……").hide();
			return this;
		},

		renderFooter: function() {
			//显示脚部数据
			// this.nodes.footerNode.html('<div style="height:20px; border:#f00 1px solid;">foot</div>');
			return; //返回this.node.footerNode
		},

		/**
		 * 存入本地数据，（仅在local时此设置数据有意义）
		 * @param  {[type]}   data [description]
		 * @param  {Function} fn   [description]
		 * @return {[type]}        [description]
		 */
		setData: function(data, fn) {
			var grid = this;
			//数据处理
			this.requestTransform(data, fn);
			//使用本地数据
			this.data = data;
			//节点加载完成后处理
			var isAppendOver = setInterval(function() {
				if (!grid.appendFlag) {
					clearInterval(isAppendOver);
					grid.nodes.wrapNode.hide();
					if (fn && System.type.isFunction(fn)) {
						fn.call(grid);
					};
				}
			}, 200);
		},
		//设置被选中数据
		setSelectData: function(data) {

		},

		/**
		 * 获取所有搜索建议
		 * @return {[type]} [description]
		 */
		getData: function() {
			return this.data;
		},

		/**获取被选中的数据Array格式返回
		 * @return {[type]} [description]
		 */
		getSelectData: function() {
			//获取已经选中的所有数据，标示为selected="selected"
			return this.selectedData;
		},

		/**
		 * 给input框添加属性
		 * @return {[type]} [description]
		 */
		setAttrs: function() {
			var grid = this.nodes.wrapNode.data("grid");
			if (grid.opts.attrs.length != 0) {
				var addAttrs = {};
				//清空input框
				if(!$.trim(grid.nodes.inputNode.val())) {
					$.each(grid.opts.attrs, function(j, obj) {
						grid.nodes.inputNode.removeAttr(obj);
					});
					return;
				}
				//无相关数据或者
				if (grid.getSelectData().length == 0) {
					$.each(grid.opts.attrs, function(j, obj) {
						addAttrs[obj] = "-1";
					});
					grid.nodes.inputNode.attr(addAttrs);
					return;
				}
				$.each(grid.getSelectData(), function(i, item) {
					var addAttrs = {};
					$.each(grid.opts.attrs, function(j, obj) {
						addAttrs[obj] = item[obj];
					});
					grid.nodes.inputNode.attr(addAttrs);
				});
			}
		},

		/**
		 * 事件处理中心
		 * @param  {[type]} e [description]
		 * @return {[type]}   [description]
		 */
		bindEvent: function(e) {
			var grid = this;

			var targetNode = this.nodes.inputNode;
			var divNode = this.nodes.resultNode;

			//获取焦点事件
			targetNode.focus(function(e) {
				grid.showHandler(e);
				grid.keyUpHandler(e);
			});

			//失去焦点事件
			targetNode.blur(function(e) {
				grid.blurHandler(e);
			});

			//上下键事件
			targetNode.keydown(function(e) {
				e.stopPropagation();
				if (e.keyCode == 38 || e.keyCode == 40) {
					//上下键事件处理函数
					grid.keyDownHandler(e.keyCode == 38 ? -1 : 1);
				}
				//enter事件以及esc事件处理
				if (e.keyCode == 13 || e.keyCode == 27) {
					grid.blurHandler(e);
				}
			});
			
			if(targetNode && targetNode.length>0 && typeof(targetNode[0]) !="undifined"){
				// 搜索触发事件
				targetNode[0].oninput = targetNode[0].onpropertychange = function(e) {
					grid.keyUpHandler(e);
				};
			}
			divNode.mousemove(function(e) {
				if (!grid.nodes.wrapNode.is(":visible")) {
					return;
				}
				grid.mouseHandler(e);
			});
		},

		/**
		 * 鼠标点击处理函数，中转函数
		 * @param  {[type]} e [description]
		 * @return {[type]}   [description]
		 */
		mouseHandler: function(e) {
			//选择事件
			if ($(e.target).is('li')) {
				this.selectEvent($(e.target), this.nodes.lastSelectedNode, e, null);
			}

		},

		/**
		 * 键盘点击处理函数，中转函数
		 * @return {[type]} [description]
		 */
		keyUpHandler: function(e) {
			// 是否需要搜索
			var grid = this;

			// 隐藏底部提示栏
			$('li#tempInputContent', grid.nodes.resultNode).html("").hide();
			// 停止搜索
			if (self.requestStarting) {
				clearTimeout(self.requestStarting);
			}
			// 延迟搜索
			var oldSearchKey = $('li#tempInputContent', grid.nodes.resultNode).attr('extrakey');
			// 是否需要搜索
			if (grid.opts.noSearch) {
				grid.localDataSearch();
				return;
			}
			//input框为空时从头开始搜索
			if (grid.opts.searchWe && grid.nodes.inputNode.val() == "") {
				//隐藏搜索层
//				grid.nodes.inputNode.blur().focus();
				grid.blurHandler(e);
				grid.showHandler(e);
				return;
			}
			//当搜索条件变化时才请求后台
			if (oldSearchKey != grid.nodes.inputNode.val() || !grid.nodes.wrapNode.is(":visible")) {
				//初次搜索，不用延时操作
				self.requestStarting = setTimeout(function() {
					$('li#tempInputContent', grid.nodes.resultNode).attr('extrakey', grid.nodes.inputNode.val());
					//加载完成后显示建议层
					grid.requestTransform(null, function() {
						if ( !! $.trim(grid.nodes.inputNode.val()) || !grid.opts.searchWe) {
							grid.nodes.wrapNode.show();
						} else {
							grid.nodes.wrapNode.hide();
						}
					});
				}, grid.opts.time);
			}
		},

		/**
		 * 本地数据搜索
		 * @return {[type]} [description]
		 */
		localDataSearch: function() {
			var grid = this;
			//拼音搜索
			var engine = pinyinEngine();
			var keyword = grid.nodes.inputNode.val();
			var indexs = [];
			//缓存所有数据
			var resultDatas = grid.getData();
			for (var i = 0, len = resultDatas.length; i < len; i++) {
				var seaKey = [resultDatas[i][grid.opts.showKey].toString()];
				if (grid.opts.showTitle) {
					seaTitle = resultDatas[i][grid.opts.showTitle].toString();
					seaKey.push(seaTitle);
				}
				if (seaKey) {
					engine.setCache(seaKey, [seaKey, i]);
				}
			}
			engine.search(keyword, function(data) {
				indexs.push(data[1]);
			});

			//隐藏显示相关li
			grid.liToggle(indexs);
		},

		/** 显示隐藏建议层
		 * [ description]
		 * @param  {[type]} indexs [description]
		 * @return {[type]}        [description]
		 */
		liToggle: function(indexs) {
			var grid = this;
			$("li", grid.nodes.resultNode).hide();
			if (System.type.isArray(indexs)) {
				grid.nodes.wrapNode.show();
				if (indexs.length == 0) {
					$('li#tempInputContent', grid.nodes.resultNode).html("无相关数据").show();
					return;
				}
				for (var liIndex = 0; liIndex <= indexs.length; liIndex++) {
					$($("li", grid.nodes.resultNode)[indexs[liIndex] + 1]).show();
				}
				//默认选中第一项
				grid.selectEvent($($("li", grid.nodes.resultNode)[indexs[0] + 1]), grid.nodes.lastSelectedNode, null, null, !grid.opts.autoComplete);
			}
		},

		/**
		 * 键盘点击处理函数，中转函数
		 * @param  {[type]} liCal [description]
		 * @return {[type]}       [description]
		 */
		keyDownHandler: function(liCal) {
			//上下选择事件
			var grid = this;
			if (this.nodes.wrapNode.css('display') == 'none' || grid.getData().length == 0) return;

			var liNodes = $("li", grid.nodes.resultNode);
			var liCount = $("li", grid.nodes.resultNode).length - 1;
			//当前li的index
			var nowSearchLi = $.inArray($("li[isfocus='true']", grid.nodes.resultNode)[0], liNodes);
			//下一个li的index
			var nextSearchLi = nowSearchLi + liCal;
			//by cwj遇到预留的li直接下一个
			// if ($(liNodes[nextSearchLi]).attr("id") == "tempInputContent" ) {
			// 	nextSearchLi += liCal;
			// }
			//本地数据bug修复
			keyDownShow();
			//end
			if (nextSearchLi > liCount) {
				nextSearchLi = 1;
				keyDownShow();
			} else if (nextSearchLi < 0) {
				nextSearchLi = liCount;
				keyDownShow();
			}
			//上下键事件处理函数
			grid.selectEvent($(liNodes[nextSearchLi]), $(liNodes[nowSearchLi]), liCal, null);

			//预留字段和本地数据搜索的情况下
			function keyDownShow(){
				if($(liNodes[nextSearchLi]).css('display') == 'none' || $(liNodes[nextSearchLi]).attr("id") == "tempInputContent" ){
					nextSearchLi += liCal;
					keyDownShow();
				}else{
					return nextSearchLi;
				}
			}
		},

		/**
		 * input框失去焦点事件
		 * @param  {[type]} e [description]
		 * @return {[type]}   [description]
		 */
		blurHandler: function(e) {
			//input框失去焦点事件
			var grid = this;
			//无建议层时
			if (!grid.nodes.wrapNode.is(":visible")) {
				if (!$.trim(grid.nodes.inputNode.val())) {
					//添加属性，搜索值为空时设置属性为-1
					if (grid.opts.view == "default") {
						grid.selectedData = [];
						if (grid.opts.isSetAttrs) {
							grid.setAttrs();
						}
					}
				}
				return;
			}
			// 如果有请求则停止
			// 停止搜索
			if (self.requestStarting) {
				clearTimeout(self.requestStarting);
			}
			//清除标志
			if (grid.appendFlag) {
				clearTimeout(grid.appendFlag);
				grid.appendFlag = false;
			}
			if (e.keyCode && e.keyCode == 27) {
				grid.nodes.inputNode.val($("li#tempInputContent", grid.nodes.resultNode).attr('extrakey'));
				grid.nodes.lastSelectedNode = null;
			} else {
				//显示已选择数据
				grid.resultShowType();
			}
			//已选数据处理
			//获取已选建议的数据
			grid.getResultDatas();
			//添加属性
			if (grid.opts.view == "default" && grid.opts.isSetAttrs) {
				grid.setAttrs();
			}
			//用户自定义选择事件
			if (grid.opts.selectFn && System.type.isFunction(grid.opts.selectFn)) {
				grid.opts.selectFn.call(grid, grid.getSelectData());
			}
			//每次清空所有的节点,必须在最后清除节点，不然获取不到data数据
			if (!this.opts.noSearch) {
				$("li", grid.nodes.resultNode).slice(1, $("li", grid.nodes.resultNode).length).remove();
			}

			//隐藏搜索层
			grid.nodes.wrapNode.css("display", "none");

			//测试
		},

		/**
		 * 单选和多选不同的显示方式
		 * @return {[type]} [description]
		 */
		resultShowType: function() {
			var grid = this;
			if (grid.opts.view == "default" && grid.nodes.lastSelectedNode) {
				grid.nodes.inputNode.val(grid.nodes.inputNode.val() == "" ? "" : grid.nodes.lastSelectedNode.attr("extrakey"));
				grid.nodes.lastSelectedNode.attr("selected", "selected");
			}
		},

		/**
		 * 获取已选择的数据
		 * @return {[type]} [description]
		 */
		getResultDatas: function() {
			var grid = this;

			//已选数据处理
			//获取已选建议的数据
			if (grid.opts.view == "default") {
				grid.selectedData = [];
			}
			$.each(grid.getResultNodes(), function(i, item) {
				grid.selectedData.push($(item).data("data"));
			});
		},

		/**
		 * 获取目标集合
		 * @return {[type]} [description]
		 */
		getResultNodes: function() {
			var grid = this;

			return $("li[selected='selected']", grid.nodes.resultNode);
		},

		/**
		 * 建议层位置渲染
		 * @param  {[type]} e         [description]
		 * @param  {[type]} searchStr [description]
		 * @return {[type]}           [description]
		 */
		showHandler: function(e, searchStr) {
			var searchElemetLeft = this.nodes.inputNode.position().left + parseInt(this.nodes.inputNode.css('marginLeft'));
			var searchElemetTop = parseInt(this.nodes.inputNode.css('marginBottom'));
			//判断input框是否存在margin-bottom
			this.nodes.wrapNode.css({
				"left": searchElemetLeft,
				"top": parseInt(this.nodes.inputNode.position().top)+parseInt(this.nodes.inputNode.css('height')),
				"margin-top": 0 /*parseInt(searchElemetTop > 0 ? searchElemetTop*(-1) : 0)+parseInt(this.nodes.inputNode.height())+parseInt(this.nodes.inputNode.css('margin-top'))+"px"*/
			});

			// 不必搜索时，只显示数据
			if (this.opts.noSearch) {
				this.nodes.wrapNode.css("display", "");
			}
		},

		/**
		 * 选择事件处理函数
		 * @param  {[type]}   selected   [description]
		 * @param  {[type]}   unselected [description]
		 * @param  {[type]}   e          [description]
		 * @param  {Function} callback   [description]
		 * @param {[type]} flag 是否给input框赋值
		 * @return {[type]}              [description]
		 */
		selectEvent: function(selected, unselected, e, callback, flag) {
			var grid = this;
			if (unselected) {
				if (!System.type.isArray(unselected)) {
					unselected = [unselected];
				}
				for (var i in unselected) {
					unselected[i].removeClass("selectedCSS").addClass("unSelectedCSS").attr("isfocus", "");
					//单选判断是否存在已选择的项
					if (grid.opts.view == "default" && unselected[i].attr("selected")) {
						unselected[i].removeAttr("selected");
					}
					if (this.nodes.selectedNode) {
						this.nodes.selectedNode = $.grep(this.nodes.selectedNode, function(n) {
							return n.is(unselected[i]);
						});
					}
				};
			}

			if (selected && !selected.is($("#tempInputContent", grid.nodes.resultNode))) {
				this.nodes.lastSelectedNode = selected;
				$(selected).removeClass("unSelectedCSS").addClass("selectedCSS").attr("isfocus", true);

				//重设层scrollTop
				var toTop = $(selected).position().top - grid.nodes.headerNode.height();
				if ($(selected).attr('id') == 'tempInputContent') {
					//选到隐藏节点说明是第一个
					grid.nodes.resultNode.scrollTop(0);
				} else if (toTop < 0) {
					//向上选择
					grid.nodes.resultNode.scrollTop(grid.nodes.resultNode.scrollTop() + toTop);
				} else if (grid.nodes.resultNode.height() - toTop < $(selected).outerHeight()) {
					//向下越界
					grid.nodes.resultNode.scrollTop(grid.nodes.resultNode.scrollTop() + $(selected).outerHeight() - grid.nodes.resultNode.height() + toTop)
				};
			}
			if (!flag) {
				grid.nodes.inputNode.val($(selected).attr('extrakey'));
			}
			if (callback && System.type.isFunction(callback)) {
				callback.call(grid, e);
			}
		}
	});
	
	/**
	 * 数据请求
	 * @param {[type]} options [description]
	 */
	var Store = function Store(options) {
		if (!this.init) return new Store(options);
		this.opts = System.util.merge(options || {}, Store.defaults);
	};

	Store.defaults = {
		model: "server",
		url: "",
		param: {}
	};

	System.util.merge(Store.prototype, {
		init: function() {
			return this;
		},
		setUrl: function(url) {
			this.opts.url = url;
			return this;
		},
		setParam: function(param) {
			if (this.param) {
				this.param = {};
			};
			this.param = param;
			return this;
		},
		getData: function(grid, data, flag) {
			var url, opts = this.opts;
			//参数处理
			//搜索参数
			this.param[grid.opts.searchParam] = $.trim(grid.nodes.inputNode.val());
			if (grid.opts.paramChange) {
				grid.opts.paramChange.call(grid, this.param);
			}
			try {
				url = opts.url;
			} catch (e) {
				throw new Error("URL地址错误，请输入正确地址");
			}
			if (opts.model === "server" && url && !data) {
				SystemAjax.request({
					url: url,
					type: "GET",
					data: this.param,
					listener: {
						success: function(json) {
							// $('li#tempInputContent', grid.nodes.resultNode).html("正在加载数据……").hide();
							grid.dealRequestData.call(grid, json.data, flag);
						},
						beforerequest: function() {
							$("li", grid.nodes.resultNode).slice(1, $("li", grid.nodes.resultNode).length).remove();
							$('li#tempInputContent', grid.nodes.resultNode).html("正在加载数据……").show();
							grid.nodes.wrapNode.show();
						}
					}
				});
			} else {
				if (data && System.type.isObject(data)) {
					grid.dealRequestData.call(grid, data, flag);
				};
			}
		}
	});

	//css样式
	var wrapCSS = {};

	var wrapUlCSS = {
		"overflow": "auto"
	};

	window.TNSearch = TNSearch;
	window.serverTransfer = false;
})(window, document);