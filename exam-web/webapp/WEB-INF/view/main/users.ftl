<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Exam - 工作台</title>
    <!-- jQuery -->
    <script src="${context.contextPath}/js/front/vendor/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="${context.contextPath}/js/config/JSLoader.js"></script>

    <script type="text/javascript" src="${context.contextPath}/js/config/admin.js"></script>
    <!-- Bootstrap Core CSS -->
    <link href="${context.contextPath}/js/front/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="${context.contextPath}/js/front/vendor/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="${context.contextPath}/js/front/dist/css/sb-admin-2.css" rel="stylesheet">

    <!-- Morris Charts CSS -->
    <link href="${context.contextPath}/js/front/vendor/morrisjs/morris.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="${context.contextPath}/js/front/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- jqPagination styles -->
    <link rel="stylesheet" href="${context.contextPath}/css/jqpagination.css" />

    <!-- select2 -->
    <link href="${context.contextPath}/js/front/dist/css/select2.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<script>
    function hide(){
        $("#addModal").hide();
    }

    function show() {
        $("#addModal").show();
    }

    function back() {
        this.param = {"phoneNumber" : 0};
        this.path_config = System.getContext();
        window.location.href = path_config+"/main/admin";
    }
    function answer(id) {
        this.param = {"phoneNumber" : 0};
        this.path_config = System.getContext();
        window.location.href = path_config+"/main/answers/"+id;
    }

    function register() {
        var number = $("#installPosition").val();
        this.path_config = System.getContext();
        this.param = {"phoneNumber" : number};
        System.ajaxSubmitDecode(System.getRequestParams(param), 'post', path_config+"/main/regist", function(data) {
            if(data.success){
                $("#addModal").hide();
                $("#result").show();
            } else {
                alert("注册失败！");
            }
        });
    }

    function hideResult() {
        $("#result").hide();
    }

    function userList() {
        this.param = {"phoneNumber" : 0};
        this.path_config = System.getContext();
        window.location.href = path_config+"/main/users";
    }
</script>

<body>
    <div id="wrapper">
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0px;">
            <div class="navbar-header" >
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a href="#" class="navbar-brand">管理后台</a>
            </div>

            <div class="navbar-default sidebar" role="navigation1" id="userTable" aria-hidden="true" style="display: none;">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu1">
                        <li id="col1">
                            <a href="#" onclick="goBack()"><i class="fa dashboard fa-fw"></i>返回</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="navbar-default sidebar" role="navigation" id="label">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                        <li>
                            <a href="#" onclick="show()"><i class="fa fa-dashboard fa-fw"></i>注册</a>
                        </li>
                        <li>
                            <a href="#" onclick="userList()"><i class="fa fa-bar-chart-o fa-fw"></i>测试结果列表</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    </div>

    <div id="page-wrapper">
        <div class="navbar-default sidebar" role="navigation1" id="userTable" aria-hidden="false">
            <div class="sidebar-nav navbar-collapse">
                <ul class="nav" id="side-menu1">
                    <li id="col1">
                        <a href="#"><i class="fa dashboard fa-fw"></i>用户列表</a>
                    </li>
                <#list user as l>
                    <li id="col1">
                        <a href="#" onclick="answer(${l.id})"><i class="fa dashboard fa-fw"></i>${l.name}</a>
                    </li>
                </#list>
                </ul>
            </div>
        </div>
    </div>

    <div id="page-wrapper">
        <div class="row">
            <div class="modal fade in" id="addModal" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
                <div class="modal-dialog" style="margin-top: 10%;">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" onclick="hide()"  aria-hidden="true">×</button>
                            <h4 class="modal-title" id="myModalLabel">输入手机号码</h4>
                        </div>
                        <div class="modal-body"style="width: 60%;margin-left: 20%">
                            <form role="form" id="addForm" class="form-horizontal">
                                <div class="form-group">
                                    <label class="col-md-6"><input name="installPosition" id="installPosition" necessary="true"
                                                                   placeholder="必填" class="form-control input-sm"/></label>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-info" onclick="register()">注册</button>
                            <button type="button" class="btn btn-default" onclick="hide()">关闭</button>
                        </div>
                    </div>
                    <!-- /.modal-content -->
                </div>
                <!-- /.modal-dialog -->
            </div>
            <div class="modal fade in" id="result" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
                <div class="modal-dialog" style="margin-top: 10%;">
                    <div class="modal-content">
                        <div class="modal-body"style="width: 60%;margin-left: 20%">
                            <form role="form" id="addForm" class="form-horizontal">
                                <div class="form-group">
                                    <label class="col-md-4">
                                        <br><br>
                                        <span>注册成功！ </span>
                                    </label>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" onclick="hideResult()">关闭</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap Core JavaScript -->
    <script src="${context.contextPath}/js/front/vendor/bootstrap/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="${context.contextPath}/js/front/vendor/metisMenu/metisMenu.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="${context.contextPath}/js/front/dist/js/sb-admin-2.js"></script>

    <!-- pagination JavaScript -->
    <script src="${context.contextPath}/js/front/js/jquery.jqpagination.js"></script>

    <!-- select2 -->
    <script src="${context.contextPath}/js/front/dist/js/select2.min.js"></script>
</body>
</html>
