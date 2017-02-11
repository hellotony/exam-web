;
var UpdateJs = UpdateJs || {}
UpdateJs = new (function(window, document){
    var _self = this;
    this.path_config = System.getContext();
    this.currentUrl;
    var urlMap = {
        "terminal" : _self.path_config + "/terminal/update", // 终端信息管理
        "eleBaseInfo" : _self.path_config + "/elevator/update", // 电梯基本信息管理

        "city" : _self.path_config + "/city/update", // 城市管理
        "county" : _self.path_config + "/county/update", // 区县管理
        "neighborhood": _self.path_config + "/neighborhood/update", // 社区删除

        "company": _self.path_config + "/company/update", // 公司管理
        "staff": _self.path_config + "/staff/update", // 员工管理

        "account": _self.path_config + "/account/update", // 账号管理
        "eleGroup": _self.path_config + "/group/update", // 电梯分组
        "groupAssign": _self.path_config + "/group/assign/update" // 指派分组
    };

    this.update = function (menu, id) {
        _self.currentUrl = urlMap[menu];
        var params = $("#updateForm").serializeArray();
        var updateParams = {};
        var flag=true;
        $.each(params, function(i, param){
            var item = $("#updateForm #"+param.name)
            if(typeof(item) != 'undefined' && item.attr("necessary")!='undefined' && item.attr("necessary")=='true' && $.trim(param.value)==''){
                flag=false;
                return;
            }
            updateParams[param.name] = param.value;
        });
        if(!flag){
            System.MsgBox.alert("必填项不能为空或者空格!");
            return;
        }
        if(menu=="eleBaseInfo"){
            updateParams['groupIds'] = $("#updateForm").find("#groupIds").val();
        }
        System.ajaxSubmitDecode(System.getRequestParams(updateParams), 'post', _self.currentUrl, function(data) {
            if(data.success){
                System.MsgBox.info("更新成功!");
                $("#updateModal").modal('hide');
                $("#updateModal").removeData("bs.modal");
                setTimeout(function () {
                    CommonJs.currentPage = 1;
                    $("a[id="+menu+"]").click();
                },300)
            } else {
                System.MsgBox.alert(data.msg);
            }
        });
    },

    this.updateAccount = function(userName, roleId, userId, userDesc) {
        $("#updateForm").find("input[name='userName']").val(userName);
        $("#updateForm").find("select[name='roleId']").val(roleId);
        $("#updateForm").find("input[name='userId']").val(userId);
        $("#updateForm").find("input[name='userDesc']").val(userDesc);
    },

    this.updateCity = function(cityName, cityId, provinceId, cityCode) {
        $("#updateForm").find("input[name='cityName']").val(cityName);
        $("#updateForm").find("input[name='cityId']").val(cityId);
        $("#updateForm").find("select[name='provinceId']").val(provinceId);
        $("#updateForm").find("input[name='cityCode']").val(cityCode);
    },

    this.updateCompany = function(companyName, companyTypeId, companyId, companyAddress, companyTel, level) {
        $("#updateForm").find("input[name='companyName']").val(companyName);
        $("#updateForm").find("input[name='companyId']").val(companyId);
        $("#updateForm").find("select[name='companyTypeId']").val(companyTypeId);
        $("#updateForm").find("input[name='companyAddress']").val(companyAddress);
        $("#updateForm").find("input[name='companyTel']").val(companyTel);
        $("#updateForm").find("input[name='level']").val(level);

        if($("#updateForm #companyTypeId").val() == 3){
            $("#updateForm #level").show();
        }
    },

    this.updateCounty = function(countyName, countyId, cityId, countyCode) {
        $("#updateForm").find("input[name='countyName']").val(countyName);
        $("#updateForm").find("input[name='countyId']").val(countyId);
        $("#updateForm").find("select[name='cityId']").val(cityId);
        $("#updateForm").find("input[name='countyCode']").val(countyCode);

    },

    this.changeStaffType = function (obj) {
        var type=$("#"+obj+"Form #type").val();
        if(type!=''){
            System.ajaxSubmitDecode(System.getRequestParams(),"post", _self.path_config + "/company/queryByType/"+type,function(data){
                if(data.success){
                    var options = "<option value=''>---必填---</option>";
                    for(var i=0;i<data.data.length;i++){
                        options+="<option value='"+data.data[i].companyId+"'>"+data.data[i].companyName+"</option>"
                    }
                    $("#"+obj+"Form #companyId").html(options);
                } else {
                    System.MsgBox.alert("加载公司异常!");
                }

            });
        }
    },

    this.updateStaff = function (name, staffId, companyId, email, tel, type) {
        if(type!=''){
            System.ajaxSubmitDecode(System.getRequestParams(),"post", _self.path_config + "/company/queryByType/"+type,function(data){
                if(data.success){
                    var options = "<option value=''>---必填---</option>";
                    for(var i=0;i<data.data.length;i++){
                        options+="<option value='"+data.data[i].companyId+"'>"+data.data[i].companyName+"</option>"
                    }
                    $("#updateForm #companyId").html(options);

                    $("#updateForm").find("input[name='name']").val(name);
                    $("#updateForm").find("input[name='staffId']").val(staffId);
                    $("#updateForm").find("select[name='companyId']").val(companyId);
                    $("#updateForm").find("input[name='email']").val(email);
                    $("#updateForm").find("input[name='tel']").val(tel);
                    $("#updateForm").find("select[name='type']").val(parseInt(type));
                } else {
                    System.MsgBox.alert("加载公司异常!");
                }

            });
        }
    },

    this.updateTerminal = function(terminalId, screenWidth, screenHeight, ADClinetVersion, linPhoneVersion, sensorVersion,
            pointPosition, SIM, screenInstallationMode, systemVersion) {
        $("#updateForm").find("input[id='terminalId']").val(terminalId);
        $("#updateForm").find("input[name='terminalId']").val(terminalId);
        $("#updateForm").find("input[name='screenWidth']").val(screenWidth);
        $("#updateForm").find("input[name='screenHeight']").val(screenHeight);
        $("#updateForm").find("input[name='ADClinetVersion']").val(ADClinetVersion);
        $("#updateForm").find("input[name='linPhoneVersion']").val(linPhoneVersion);
        $("#updateForm").find("input[name='sensorVersion']").val(sensorVersion);
        $("#updateForm").find("input[name='pointPosition']").val(pointPosition);
        $("#updateForm").find("input[name='SIM']").val(SIM);
        $("#updateForm").find("input[name='screenInstallationMode']").val(screenInstallationMode);
        $("#updateForm").find("input[name='systemVersion']").val(systemVersion);
    },

    this.updateGroup = function(groupName, groupDesc, eltGroupId) {
        $("#updateForm").find("input[name='eltGroupId']").val(eltGroupId);
        $("#updateForm").find("input[name='groupName']").val(groupName);
        $("#updateForm").find("input[name='groupDesc']").val(groupDesc);
    },
    
    this.updateElevator = function (eltBaseID,installPosition,neighborhoodId,registrationCode,useNo,active,terminalId,
                                    manufactureCompanyId,installationCompany,installationDate,malfunctionCompanyId,malfunctionUserAId,
                                    malfunctionUserBId,propertyCompanyId,inspectorCompanyId,inspectorAId,inspectorBId,licenceNo,productDate,
                                    certifitionNo,rescueCall) {
        $("#updateForm").find("input[id='eltBaseID']").val(eltBaseID);
        $("#updateForm").find("input[name='installPosition']").val(installPosition);
        $("#updateForm").find("select[name='neighborhoodId']").val(neighborhoodId).select2({width:"100%"});
        $("#updateForm").find("input[name='registrationCode']").val(registrationCode);
        $("#updateForm").find("input[name='useNo']").val(useNo);
        $("#updateForm").find("select[name='active']").val(active);
        $("#updateForm").find("select[name='terminalId']").append("<option value='"+terminalId+"'>"+terminalId+"</option>");
        $("#updateForm").find("select[name='terminalId']").val(terminalId).select2({width: "100%"});
        $("#updateForm").find("select[name='manufactureCompanyId']").val(manufactureCompanyId).select2({width:"100%"});
        $("#updateForm").find("input[name='installationCompany']").val(installationCompany);
        $("#updateForm").find("input[name='installationDate']").val(installationDate.substring(0,10));
        $("#updateForm").find("select[name='malfunctionCompanyId']").val(malfunctionCompanyId).select2({width:"100%"});

        $("#updateForm").find("select[name='propertyCompanyId']").val(propertyCompanyId).select2({width:"100%"});
        $("#updateForm").find("select[name='inspectorCompanyId']").val(inspectorCompanyId).select2({width:"100%"});

        $("#updateForm").find("input[name='licenceNo']").val(licenceNo);
        $("#updateForm").find("input[name='productDate']").val(productDate.substring(0,10));
        $("#updateForm").find("input[name='certifitionNo']").val(certifitionNo);
        $("#updateForm").find("input[name='rescueCall']").val(rescueCall);

        UpdateJs.changeCompany('update', 'mainten', malfunctionCompanyId, malfunctionUserAId, malfunctionUserBId);
        UpdateJs.changeCompany('update', 'inspect', inspectorCompanyId, inspectorAId, inspectorBId);

        // 获取所属分组ids
        System.ajaxSubmitDecode(System.getRequestParams({"id":eltBaseID}),'post',_self.path_config+"/elevator/queryGroupIds",function (data) {
           if(data.success){
               $("#updateForm").find("select[name='groupIds']").val(data.data).select2({width:"100%"});
           } else {
               System.MsgBox.alert("加载电梯分组异常");
           }
        });
    },

    // 更改维保公司
    this.changeCompany = function (obj, select, companyId, AId, BId) {
        var params = {
            "companyId": companyId
        }
        System.ajaxSubmitDecode(System.getRequestParams(params), 'post', _self.path_config + "/staff/queryByCompanyId", function (result) {
            if (result.success) {
                var data = result.data;
                var options = ""
                for (var i = 0; i < data.length; i++) {
                    options += "<option value='" + data[i].staffId + "'>" + data[i].name + "</option>"
                }
                if (select == 'mainten') {
                    $("#" + obj + "Form #malfunctionUserAId").html(options).select2({
                        placeholder: "---必填---",
                        width: "100%"
                    });
                    $("#updateForm").find("select[name='malfunctionUserAId']").val(AId).trigger("change");
                    options = "<option value='0'>无</option>" + options;
                    $("#" + obj + "Form #malfunctionUserBId").html(options);
                    $("#updateForm").find("select[name='malfunctionUserBId']").val(BId).trigger("change");
                    $("#" + obj + "Form #malfunctionUserBId").select2({
                        width: "100%"
                    });
                } else {
                    $("#" + obj + "Form #inspectorAId").html(options).select2({
                        placeholder: "---必填---",
                        width: "100%"
                    });
                    $("#updateForm").find("select[name='inspectorAId']").val(AId).trigger("change");
                    options = "<option value='0'>无</option>" + options;
                    $("#" + obj + "Form #inspectorBId").html(options);
                    $("#updateForm").find("select[name='inspectorBId']").val(BId).trigger("change");
                    $("#" + obj + "Form #inspectorBId").select2({
                        width: "100%"
                    });
                }
            } else {
                System.MsgBox.alert("加载员工信息异常:" + result.msg);
            }
        });
    },
    // 通知列表
    this.noticeList = function (eltBaseId, propertyCompanyId, malfunctionCompanyId) {
        $("#noticeForm #eltBaseID").val(eltBaseId);
        var params = {
            "eltBaseId": eltBaseId,
            "propertyCompanyId": propertyCompanyId,
            "malfunctionCompanyId": malfunctionCompanyId
        }
        System.ajaxSubmitDecode(System.getRequestParams(params), 'post', _self.path_config + "/staff/queryNoticeList", function (result) {
            if(result.success){
                // 物业人员
                var wyStaff = result.data.wyStaff;
                var wyOptions = ""
                for(var i=0;i<wyStaff.length;i++){
                    var selected = "";
                    if(wyStaff[i].checked==1){selected="selected"}
                    wyOptions += "<option value='"+wyStaff[i].staffId+"' "+selected+ ">"+wyStaff[i].name+"</option>>"
                }
                $("#noticeForm #wyStaffs").html(wyOptions).select2({width: "100%"});
                // 维保人员
                var wbStaff = result.data.wbStaff;
                var wbOptions = ""
                for(var i=0;i<wbStaff.length;i++){
                    var selected = "";
                    if(wbStaff[i].checked==1){selected="selected"}
                    wbOptions += "<option value='"+wbStaff[i].staffId+"' "+selected+ ">"+wbStaff[i].name+"</option>>"
                }
                $("#noticeForm #wbStaffs").html(wbOptions).select2({width: "100%"});
            } else {
                System.MsgBox.alert("加载通知人员列表异常:" + result.msg);
            }
        });
    },
    // 更新通知人
    this.updateNotice = function () {
        var eltBaseId = $("#noticeForm #eltBaseID").val();
        var wbStaffs = $("#noticeForm #wbStaffs").val();
        var wyStaffs = $("#noticeForm #wyStaffs").val();
        var params = {
            "eltBaseId" : eltBaseId,
            "wbStaffs" : wbStaffs,
            "wyStaffs" : wyStaffs
        };
        System.ajaxSubmitDecode(System.getRequestParams(params), 'post', _self.path_config + "/staff/notice", function(data) {
            if(data.success){
                System.MsgBox.info("操作成功!");
                $("#noticeModal").modal('hide');
                $("#noticeModal").removeData("bs.modal");
            } else {
                System.MsgBox.alert(data.msg);
            }
        });
    },
    // 设置
    this.setting = function (terminalId) {
        $("#setSingleForm,#setFloor").find("input,select").val("");
        $("#setSingleForm,#setFloor").find("input[name='terminalId']").val(terminalId);
    }
    // 楼层格式化
    this.floorChange = function () {
        var reg = /~/g;
        var val = $("#floors").val();
        var floorVal="";
        if(reg.test(val)){
            var start = val.split("~")[0];
            var end = val.split("~")[1];
            for(var i=start;i<=end;i++){
                if(i==0){
                    continue;
                }
                floorVal += i + " ";
            }
            $("#floors").val(floorVal.substr(0,floorVal.length-1));
        }
    },
    // 设置楼层
    this.setFloor = function () {
        var floors = $("#floors").val();
        if(floors == null || $.trim(floors) == ""){
            alert("请输入经留楼层！");
            return;
        }
        _self.floorChange();
        var terminalId = $("#setFloor").find("input[name='terminalId']").val();
        var params = {
            "terminalId" : terminalId,
            "content": floors,
            "msgType": 267
        }
        System.ajaxSubmitDecode(System.getRequestParams(params), 'post', _self.path_config + "/elevator/sendData2C", function(data) {
            if(data.success){
                System.MsgBox.info("设置成功!");
            } else {
                System.MsgBox.alert("设置失败!");
            }
        });
    }
    // 获取信号位
    this.getSingles = function () {
        var terminalId = $("#setSingleForm").find("input[name='terminalId']").val();
        var params = {
            "terminalId" : terminalId,
            "msgType": 268
        }
        System.ajaxSubmitDecode(System.getRequestParams(params), 'post', _self.path_config + "/elevator/sendData2C", function(data) {
            if(data.success){
                System.MsgBox.info("获取成功!");
                var bitpoint = data.data.msgProperties.bitPoint;
                $("#aq").val(bitpoint.charAt(0));
                $("#jx").val(bitpoint.charAt(1));
                $("#sq").val(bitpoint.charAt(2));
                $("#xq").val(bitpoint.charAt(3));
                $("#tm").val(bitpoint.charAt(4));
                $("#jm").val(bitpoint.charAt(5));
                $("#sx").val(bitpoint.charAt(6));
                $("#xx").val(bitpoint.charAt(7));
                $("#sm").val(bitpoint.charAt(8));
                $("#xm").val(bitpoint.charAt(9));
                $("#yx").val(bitpoint.charAt(10));
                $("#bz").val(bitpoint.charAt(11));
                $("#bl1").val(bitpoint.charAt(12));
                $("#bl2").val(bitpoint.charAt(13));
                $("#bl3").val(bitpoint.charAt(14));
                $("#bl4").val(bitpoint.charAt(15));
                $("#bl5").val(bitpoint.charAt(16));
                $("#bl6").val(bitpoint.charAt(17));
            } else {
                System.MsgBox.alert("获取失败!");
            }
        });
    },
    // 设置信号
    this.setSingle = function () {
        var info="";
        $("input[name='signalBitInput']").each(function(index,value){
            info=info+$(value).val();
        });
        var terminalId = $("#setSingleForm").find("input[name='terminalId']").val();
        var params = {
            "terminalId" : terminalId,
            "content": info,
            "msgType": 260
        }
        System.ajaxSubmitDecode(System.getRequestParams(params), 'post', _self.path_config + "/elevator/sendData2C", function(data) {
            if(data.success){
                System.MsgBox.info("设置成功!");
            } else {
                System.MsgBox.alert("设置失败!");
            }
        });
    }
    // 查看电梯
    this.elevatorList = function (eltGroupId) {
        $("#elevatorForm #eltGroupId").val(eltGroupId);
        var params = {
            "eltGroupId": eltGroupId
        }
        System.ajaxSubmitDecode(System.getRequestParams(params), 'post', _self.path_config + "/group/queryElevatorList", function (result) {
            if(result.success){
                var list = result.data;
                var options = ""
                for(var i=0;i<list.length;i++){
                    var selected = "";
                    if(list[i].checked==1){selected="selected"}
                    options += "<option value='"+list[i].elevatorId+"' "+selected+ ">"+list[i].department+"("+list[i].terminalId+")"+"</option>>"
                }
                $("#elevatorForm #elevators").html(options).select2({width: "150%"});
            } else {
                System.MsgBox.alert("加载电梯列表异常!");
            }
        });
    }
    // 保存
    this.saveElevator = function () {
        var eltGroupId = $("#elevatorForm #eltGroupId").val();
        var elevators = $("#elevatorForm #elevators").val();
        var params = {
            "eltGroupId": eltGroupId,
            "elevators": elevators
        }
        System.ajaxSubmitDecode(System.getRequestParams(params), 'post', _self.path_config + "/group/saveElevatorList", function (result) {
            if(result.success){
                System.MsgBox.info("更新成功!");
                $("#elevatorModal").modal('hide');
                $("#elevatorModal").removeData("bs.modal");
            } else {
                System.MsgBox.alert("更新失败!");
            }
        });
    }
})(window, document);