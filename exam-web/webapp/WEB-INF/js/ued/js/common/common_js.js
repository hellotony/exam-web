;
var CommonJs = CommonJs || {}
CommonJs = new (function(window, document){
    var _self = this;
    this.path_config = System.getContext();
    var initParam = {
        pageSize:15,
        currentPage:1
    }
    this.initParam = initParam;
    this.currentUrl;
    var urlMap = {
        "terminal" : _self.path_config + "/terminal/html", // 终端信息管理
        "eleBaseInfo" : _self.path_config + "/elevator/html", // 电梯基本信息管理

        "city" : _self.path_config + "/city/html", // 城市管理
        "county" : _self.path_config + "/county/html", // 区县管理
        "neighborhood": _self.path_config + "/neighborhood/html", // 社区管理

        "company": _self.path_config + "/company/html", // 公司管理
        "staff": _self.path_config + "/staff/html", // 员工管理

        "account": _self.path_config + "/account/html", // 账号管理
        "eleGroup": _self.path_config + "/group/html", // 电梯分组
        "groupAssign": _self.path_config + "/group/assign/html" // 指派分组

    };

    this.switchMenu = function (menu) {
        _self.currentPage=1;
        _self.container = $("#resultDiv");
        JLoading.open("正在拼命的搜索，请稍等...");
        _self.currentUrl = urlMap[menu];
        System.ajaxSubmit(System.getRequestParams(), 'post', _self.currentUrl, function(data) {
            JLoading.immediatelyClose();
            $("#page-wrapper").html(data);
            var totalCnt = $("#totalCount").val()
            $('.pagination').jqPagination({
                current_page: _self.currentPage,
                max_page: $("#pageInfo").val(),
                page_string : '{current_page}/{max_page}页,共'+totalCnt+'条',
                paged: function(page) {
                    _self.pagination(page);
                }
            });
        });
        // 移除效果
        $("a[name='menuItem']").each(function () {
           $(this).removeClass("active");
        });
        $("#"+menu).addClass("active");
    },
    this.queryList = function () {
        _self.currentPage=1
        var params = $("#search_condition").serializeArray();
        var queryParams = {};
        $.each(params, function(i, param){
            queryParams[param.name] = param.value;
        });
        _self.initParam = $.extend({},queryParams,_self.initParam);
        //加载搜索结果
        JLoading.open("正在拼命的搜索，请稍等...");
        System.ajaxSubmit(System.getRequestParams(queryParams), 'post', _self.currentUrl, function(data) {
            JLoading.immediatelyClose();
            $("#page-wrapper").html(data);
            var totalCnt = $("#totalCount").val()
            $('.pagination').jqPagination({
                current_page: _self.currentPage,
                max_page: $("#pageInfo").val(),
                page_string : '{current_page}/{max_page}页,共'+totalCnt+'条',
                paged		: function(page) {
                    _self.pagination(page);
                }
            });
            System.initDisplayToggle();
        });
    },

    this.initContent = function(containerId, sourceCodeUrl, searchParam){
        JLoading.open();
        _self.container = $("#" + containerId);
        _self.sourceCodeUrl = _self.currentUrl;
        if(searchParam.currentPage>0){
            searchParam.page = _self.currentPage;
        }else{
            searchParam.page = 1;
            _self.currentPage = 1;
        }
        searchParam.pageSize = _self.pageSize;
        var params = $("#search_condition").serializeArray();
        $.each(params, function(i, param){
            searchParam[param.name] = param.value;
        });
        _self.initParam = searchParam;
        $.post(_self.currentUrl, System.getRequestParams(searchParam),
            function(data) {
                JLoading.close();
                $("#page-wrapper").html(data);
                var totalCnt = $("#totalCount").val()
                $('.pagination').jqPagination({
                    current_page: _self.currentPage,
                    max_page: $("#pageInfo").val(),
                    page_string : '{current_page}/{max_page}页,共'+totalCnt+'条',
                    paged		: function(page) {
                        _self.pagination(page);
                    }
                });
                System.initDisplayToggle();
            }
        );
    },

    this.pagination = function(page) {
        if(page<1){
            page = 1;
        }
        _self.initParam.currentPage = page;
        _self.initParam.pageSize = _self.initParam.pageSize
        _self.pageSize = _self.initParam.pageSize;
        _self.initContent("page-wrapper", _self.currentUrl, _self.initParam);
        _self.currentPage = page;
    }

})(window, document);