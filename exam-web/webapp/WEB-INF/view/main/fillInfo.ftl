<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Exam - 信息完善</title>
    <script type="text/javascript" src="${context.contextPath}/js/config/JSLoader.js"></script>
    <script type="text/javascript" src="${context.contextPath}/js/config/login.js"></script>
    <!-- Bootstrap Core CSS -->
    <link href="${context.contextPath}/js/front/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="${context.contextPath}/js/front/vendor/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="${context.contextPath}/js/front/dist/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="${context.contextPath}/js/front/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-md-4 col-md-offset-4">
                <div class="login-panel panel panel-default">
                    <div class="panel-heading" style="text-align: center;">
                        <h3 class="panel-title">完整详细信息</h3>
                    </div>
                    <div class="panel-body">
                        <form role="form" id="addForm">
                            <fieldset>
                                <div class="form-group">
                                    <input class="form-control" placeholder="姓名(必填)" name="name" id="name" autofocus necessary="true">
                                    <input type="hidden" id="phone" name="phone" value="${phone!""}"/>
                                </div>
                                <div class="form-group">
                                    <select class="form-control" name="gender" id="gender" necessary="true">
                                        <option value="1">女</option>
                                        <option value="2">男</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <input necessary="true" class="form-control" type="text" placeholder="出生年月日(必填)" name="birthday" id="birthday" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly>
                                </div>
                                <div class="form-group">
                                    <input class="form-control" placeholder="血型(必填)" name="bloodType" id="bloodType" necessary="true" />
                                </div>
                                <div class="form-group">
                                    <input class="form-control" placeholder="身高cm(必填)" name="height" id="height" type="number" necessary="true" />
                                </div>
                                <div class="form-group">
                                    <input class="form-control" placeholder="体重kg(必填)" name="weight" id="weight" type="number" necessary="true" />
                                </div>
                                <div class="form-group">
                                    <input class="form-control" placeholder="工作单位" name="companyName" id="companyName" />
                                </div>
                                <!-- Change this to a button or input when using this as a form -->
                                <a onclick="Login.startExam()" class="btn btn-lg btn-success btn-block">开始测试</a>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- jQuery -->
    <script src="${context.contextPath}/js/front/vendor/jquery/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${context.contextPath}/js/front/vendor/bootstrap/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="${context.contextPath}/js/front/vendor/metisMenu/metisMenu.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="${context.contextPath}/js/front/dist/js/sb-admin-2.js"></script>
</body>
</html>
