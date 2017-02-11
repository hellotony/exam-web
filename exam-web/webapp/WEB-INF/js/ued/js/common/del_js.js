;
var DelJs = DelJs || {}
DelJs = new (function(window, document){
    var _self = this;
    this.path_config = System.getContext();
    this.currentUrl;
    var urlMap = {
        "terminal" : _self.path_config + "/terminal/del", // 终端信息管理
        "eleBaseInfo" : _self.path_config + "/elevator/del", // 电梯基本信息管理

        "city" : _self.path_config + "/city/del", // 城市管理
        "county" : _self.path_config + "/county/del", // 区县管理
        "neighborhood": _self.path_config + "/neighborhood/del", // 社区删除

        "company": _self.path_config + "/company/del", // 公司管理
        "staff": _self.path_config + "/staff/del", // 员工管理

        "account": _self.path_config + "/account/del", // 账号管理
        "eleGroup": _self.path_config + "/group/del", // 电梯分组
        "groupAssign": _self.path_config + "/group/assign/del" // 指派分组

    };

    this.del = function (menu, id) {
        System.MsgBox.confirm("确定要删除此条记录?", function () {
            _self.currentUrl = urlMap[menu];
            var delParam = {
                "id" : id
            }
            System.ajaxSubmitDecode(System.getRequestParams(delParam), 'post', _self.currentUrl, function(data) {
                if(data.success){
                    System.MsgBox.info("删除成功!");
                    setTimeout(function () {
                        CommonJs.currentPage = 1;
                        $("a[id="+menu+"]").click();
                    },300)
                } else {
                    System.MsgBox.alert(data.msg);
                }
            });
        })
    }

})(window, document);