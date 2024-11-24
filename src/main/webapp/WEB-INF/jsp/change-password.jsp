<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>パスワード修正</title>
    <link rel="stylesheet" type="text/css" href='<c:url value='/static/css/style.css'/>'>
    <link rel="stylesheet" type="text/css" href='<c:url value='/static/css/style_list.css'/>'>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/form_check.js"></script>

    <script>
        $(document).ready(function() {
        	//获取上下文路径
            var contextPath = $('body').data('context-path');

            //返回按钮
            $("#return-button").click(function(){
            	window.location.href = contextPath + '/employeeList';
            })
            
            //提交按钮绑定事件
            $("#submit").click(function(e){
                //调用失去焦点事件
                $("input[type='password']").blur();
                if('' != $("#info").text()){
                    //有错误信息的时候，阻止表单提交
                    console.log("info:" + $("#info").text())
                    e.preventDefault();
                }
            })

            //输入框失去焦点绑定事件（检查表单）
            $("input[type='password']").on("input change blur", function (){
                // check表单
                if($("#old-password").val() == "" || $("#old-password").val() == null){
                    $("#info").text("パスワードを入力してください")
                }
                else if($("#new-password").val() == "" || $("#old-password").val() == null){
                    $("#info").text("新しいパスワードを入力してください")
                }
                else if($("#new-password-check").val() == "" || $("#old-password").val() == null){
                    $("#info").text("パスワードをもう一度入力してください")
                }else {
                    //清空错误信息
                	$("#info").text("")
                	//检查密码正确
                    var errorMSG = checkForm();
                    if (errorMSG != ''){
                        $("#info").text(errorMSG)
                    }else {
                        console.log("检查无异常")
                    }
                    
                }
            })

            $("#alert").css('display','none')
            function alert(){
                var alertBox = $("#alert")
                var tipMSG = alertBox.data("tip-msg")
                console.log("tipMSG: "+ tipMSG);
                if (tipMSG != null && tipMSG != ''){
                    alertBox.text(tipMSG);
                    alertBox.css('display', 'block')
                    setTimeout(function() {
                        alertBox.css('display', 'none')
                    }, 5000); // 2000毫秒 = 2秒
                }

            }
            alert()
            
        })
        
        
        
    </script>
</head>
<body data-context-path="${pageContext.request.contextPath}">
<div id="alert" class="alert" data-tip-msg="${tipMSG}"> </div>

<form id="form" action="${pageContext.request.contextPath}/passwordChange"
      method="post">
    <table>
        <thead>
        <tr>
            <th colspan="2">
                <h1>パスワード修正</h1>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td class="tip">旧パスポート</td>
            <td class="input"><input type="password" name="old-password"
                                     id="old-password"></td>
        </tr>

        <tr>
            <td class="tip">新パスポート</td>
            <td class="input"><input type="password" name="new-password"
                                     id="new-password"></td>
        </tr>

        <tr>
            <td class="tip">新パスポート確認</td>
            <td class="input"><input type="password" name="new-password-check"
                                     id="new-password-check"></td>
        </tr>

        <tr>
            <td></td>
            <td class="error">
                <span id="info">${errorMSG}</span>
            </td>
        </tr>

        <tr>
            <td></td>
            <td>
            <input type="button" id="return-button" value="戻る">
            <input type="submit" id="submit" value="確定">
            </td>

        </tr>
        </tbody>
    </table>
</form>

</body>
</html>