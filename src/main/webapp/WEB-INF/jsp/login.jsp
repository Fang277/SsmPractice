<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>ログイン</title>
    <link rel="stylesheet" type="text/css"
          href='<c:url value='/static/css/style.css'/>'>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function() {

            $("input").on("input change blur", function(event){
                $("#info").text("");

                if ('' == $("#loginId").val()) {
                    $("#info").text("ログインIDを入力してください");
                    event.preventDefault();
                } else if ('' == $("#password").val()) {
                    $("#info").text("パスポートを入力してください");
                    event.preventDefault();
                } else {
                    console.log("not empty")
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
                    }, 3000); // 2000毫秒 = 2秒
                }

            }
            alert()


        })
    </script>
</head>
<body>
<div id="alert" class="alert" data-tip-msg="${tipMSG}"> </div>

<form id="form" action="${pageContext.request.contextPath}/checkLogin"
      method="post">
    <table>
        <thead>
        <tr>
            <th colspan="2">
                <h1>ログイン</h1>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td class="tip">アカウント</td>
            <td class="input"><input type="text" name="loginId" id="loginId"></td>

        </tr>

        <tr>
            <td class="tip">パスポート</td>
            <td class="input"><input type="password" name="password" id="password"></td>

        </tr>
        <tr>
            <td></td>
            <td class="error"><span id="info"><c:out value="${errorMessage}" /></span>
            </td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" id="submit" value="登録"></td>

        </tr>
        </tbody>
    </table>
</form>

</body>
</html>