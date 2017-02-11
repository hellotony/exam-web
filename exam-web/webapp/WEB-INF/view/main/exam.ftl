<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Exam - 在线测试</title>
    <script type="text/javascript" src="${context.contextPath}/js/config/JSLoader.js"></script>
    <script type="text/javascript" src="${context.contextPath}/js/config/main.js"></script>
</head>
<body style="background-color:#1fc587">
<div class="wrapper">
    <input id="uid" type="hidden" value="${uid}"/>
	<div id="answer" class="card_wrap">
		<!--Q1-->
		<#if titleVos?exists>
            <#list titleVos as title>
                <div class="card_cont <#if title_index == 0>card1</#if>" id="${title.abstractNo!""}">
                    <div class="card">
                        <p class="question"><span>Q${title.abstractNo!""}: ${title.content!""}</span></p>
                        <ul class="select">
                            <#if title.options?exists>
                                <#list title.options as option>
                                        <#if title.type==1>
                                        <li>
                                            <input id="q_${option.id}" type="radio" name="r-group-${title.abstractNo}"
                                               onclick="turnTO(${option.nextTitleId}, ${title.nextTitleId}, ${title.abstractNo!""}, ${option.id!""})">
                                            <label for="q_${option.id}">${option.content!""}</label>
                                        </li>
                                        </#if>
                                        <#if title.type==2>
                                        <#--<li>-->
                                            <input id="q_${option.id}" type="checkbox" name="r-group-${title.abstractNo}" value="${option.id}">
                                            <label for="q_${option.id}">${option.content!""}</label>
                                        <#--</li>-->
                                        </#if>

                                </#list>
                            </#if>
                        </ul>
                        <#if title.type==2>
                            <div class="card_bottom"><a class="prev" onclick="turnTOMulti(${title.abstractNo}, ${title.nextTitleId}, ${title.id})">下一题</a></div>
                        </#if>
                    </div>
                </div>
            </#list>
		</#if>
	</div><!--/card_wrap-->
</div>
<script>
    $(function(){
        $("#answer").answerSheet({});
    });
    function turnTO(opId, tiId, dId, optId) {
        var showId = opId == 0 ? (tiId == 0 ? 0 : tiId) :opId;
        if(showId==0){return;}
        $("#"+showId).addClass('card1');
        $("#"+dId).removeClass('card1');
        var answer = optId;
        var userId = $("#uid").val();
        var param = {userId:userId,titleId:dId,optionId: answer}
        // 存入数据库
        System.ajaxSubmitDecode(System.getRequestParams(param), 'post', System.getContext() + "/main/answer", function(data) {
            if(data.success){
                // 加载题目

            } else {
                alert(data.msg);
            }
        });
    }
    function turnTOMulti(divId, nextId, id) {
        if(divId==3){
            if($($("#q_4")).is(":checked")){
                nextId = 4
            }else if($($("#q_14")).is(":checked")){
                nextId = 5;
            }
        } else if(divId==4){
            if($($("#q_14")).is(":checked")){
                nextId = 5;
            }
        }
        $("#"+nextId).addClass('card1');
        $("#"+divId).removeClass('card1');
        // 存入数据库
        var answer = $("input[name='r-group-"+(divId)+"']:checked").val();
        var userId = $("#uid").val();
        var param = {userId:userId,titleId:id,optionId: answer}
        System.ajaxSubmitDecode(System.getRequestParams(param), 'post', System.getContext() + "/main/answer", function(data) {
            if(data.success){
                // 加载题目
            } else {
                alert(data.msg);
            }
        });
    }
</script>
</body>
</html>

