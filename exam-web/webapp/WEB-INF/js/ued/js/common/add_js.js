;
var AddJs = AddJs || {}
AddJs = new (function(window, document){
    var _self = this;
    this.path_config = System.getContext();
    this.currentUrl;
    var urlMap = {
        "terminal" : _self.path_config + "/terminal/add", // 终端信息管理
        "eleBaseInfo" : _self.path_config + "/elevator/add", // 电梯基本信息管理

        "city" : _self.path_config + "/city/add", // 城市管理
        "county" : _self.path_config + "/county/add", // 区县管理
        "neighborhood": _self.path_config + "/neighborhood/add", // 社区添加

        "company": _self.path_config + "/company/add", // 公司管理
        "staff": _self.path_config + "/staff/add", // 员工管理

        "account": _self.path_config + "/account/add", // 账号管理
        "eleGroup": _self.path_config + "/group/add", // 电梯分组
        "groupAssign": _self.path_config + "/group/assign/add" // 指派分组

    };

    this.add = function (menu) {
        _self.currentUrl = urlMap[menu];

        var params = $("#addForm").serializeArray();
        var addParams = {};
        var flag=true;
        $.each(params, function(i, param){
            var item = $("#addForm #"+param.name)
            if(typeof(item) != 'undefined' && item.attr("necessary")!='undefined' && item.attr("necessary")=='true' && $.trim(param.value)==''){
                flag=false;
                return;
            }
            addParams[param.name] = param.value;
        });
        if(!flag){
            System.MsgBox.alert("必填项不能为空或者空格!");
            return;
        }
        if(menu=="eleBaseInfo"){
            addParams['groupIds'] = $("#addForm").find("#groupIds").val();
        }
        System.ajaxSubmitDecode(System.getRequestParams(addParams), 'post', _self.currentUrl, function(data) {
            if(data.success){
                System.MsgBox.info("新增成功!");
                $("#addModal").modal('hide');
                $("#addModal").removeData("bs.modal");
                setTimeout(function () {
                    CommonJs.currentPage = 1;
                    $("a[id="+menu+"]").click();

                },300)
            } else {
                System.MsgBox.alert(data.msg);
            }
        });
    },

    this.addModal = function () {
        $("#addForm").find("input,select").val("");
    },
    // 修改公司类型
    this.changeCompanyType = function(obj) {
        if($("#"+obj+"Form #companyTypeId").val() == 3){
            $("#"+obj+"Form #level").show();
        } else {
            $("#"+obj+"Form #level").hide();
        }
    },
    // 分配模块
    this.addAssignModal = function () {
        $("#addForm").find("input,select").val("");
        setTimeout(function () {
            $("#addForm").find("select").select2({width: "100%"});
        },200);
    },
    // 添加电梯按钮事件
    this.addElevator = function () {
        $("#addForm").find("input,select").val("");
        $("#addForm").find("select[name='isActive']").val(0);
        $("#addForm").find("#malfunctionCompanyId,#inspectorCompanyId,#terminalId,#neighborhoodId").select2({
            placeholder: "---必填---",
            width: "100%"
        });
        $("#addForm").find("#manufactureCompanyId,#propertyCompanyId,#groupIds").select2({width: "100%"});
    },
    // 更改维保公司
    this.changeCompany = function (obj, select) {
        var companyId;
        if(select == 'mainten'){companyId = $("#"+obj+"Form #malfunctionCompanyId").val();}
        else {companyId = $("#"+obj+"Form #inspectorCompanyId").val();}
        var params = {
            "companyId" : companyId
        }
        System.ajaxSubmitDecode(System.getRequestParams(params),'post', _self.path_config + "/staff/queryByCompanyId", function (result) {
            if(result.success){
                var data = result.data;
                var options = ""
                for(var i=0;i<data.length;i++){
                    options += "<option value='"+data[i].staffId+"'>"+data[i].name+"</option>"
                }
                if(select == 'mainten'){
                    $("#"+obj+"Form #malfunctionUserAId,#malfunctionUserBId").html(options).select2({
                        placeholder: "---必填---",
                        width: "100%"
                    });
                    options = "<option value='0'>无</option>" + options;
                    $("#"+obj+"Form #malfunctionUserBId").html(options).select2({
                        width: "100%"
                    });
                } else {
                    $("#"+obj+"Form #inspectorAId,#inspectorBId").html(options).select2({
                        placeholder: "---必填---",
                        width: "100%"
                    });
                    options = "<option value='0'>无</option>" + options;
                    $("#"+obj+"Form #inspectorBId").html(options).select2({
                        width: "100%"
                    });
                }
            } else {
                System.MsgBox.alert("加载员工信息异常:" + result.msg);
            }
        });
    }
})(window, document);