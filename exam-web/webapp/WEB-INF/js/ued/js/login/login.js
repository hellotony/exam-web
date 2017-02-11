;
var Login = Login || {}
Login = new (function(window, document){
	var _self = this;
	this.path_config = System.getContext();
	this.url_config = {
		login : _self.path_config + "/login/login",
        addInfo : _self.path_config + "/user/updateInfo"
	}
    this.login = function(){
        var params = {
            "phone" : $("#phone").val(),
            "password" : $("#password").val()
        }
        System.ajaxSubmitDecode(System.getRequestParams(params), 'post', _self.url_config.login, function(data) {
            if(data.success){
                window.location.href = _self.path_config + data.data + "/"+$("#phone").val();
            } else {
                $("#errorMsg").html(data.msg);
                $("#errorMsg").show();
            }
        });
        return false;
    },

    this.startExam = function () {
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
            alert("必填项不能为空或者空格!");
            return;
        }
        System.ajaxSubmitDecode(System.getRequestParams(addParams), 'post', _self.url_config.addInfo, function(data) {
            if(data.success){
                // 加载题目
                window.location.href = _self.path_config  + "/main/startExam/" + addParams.phone ;
            } else {
                alert(data.msg);
            }
        });
    }

})(window, document);
