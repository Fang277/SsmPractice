<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>社員リスト</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static/css/style_list.css">
<script
	src="${pageContext.request.contextPath}/webjars/jquery/3.6.0/jquery.min.js"></script>
<script
	src="${pageContext.request.contextPath}/static/js/employee-list.js"></script>
<script src="${pageContext.request.contextPath}/static/js/form_check.js"></script>
<script>
	$(document).ready(function(){
		//页面初始查询所有员工（无条件）
		searchByConditions(1);
		initButtons();
		initBirthInput();

		//初始化顶部提示框
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
<body data-context-path="${pageContext.request.contextPath}"
	data-current-employee="${sessionScope.currentEmployee}">
	
	<div id="alert" class="alert" data-tip-msg="${tipMSG}"> </div>
	
	<!-- 用户信息 -->
	<div id="user-info">
		<h2 style="float: left; margin: 0; white-space: nowrap;">社員情報システム</h2>
		<c:if test="${not empty sessionScope.currentEmployee}">
			<span>hi, <c:out
					value="${sessionScope.currentEmployee.username}" /></span>
			<a href="${pageContext.request.contextPath}/logout"
				class="button logout-button">ログアウト</a>
			<button
				onclick="location.href='${pageContext.request.contextPath}/passwordChange'"
				class="button password-change-button">パスワード修正</button>
		</c:if>
		<c:if test="${empty sessionScope.currentEmployee}">
			<a href="${pageContext.request.contextPath}/login" class="button">ログイン</a>
		</c:if>
	</div>

	<div
		style="display: flex; justify-content: space-between; align-items: flex-start;">
		<!-- 检索条件 -->
		<div id="search-area">
			<form action="${pageContext.request.contextPath}/searchEmployee"
				method="post" id="search-form">
				<table>
					<tr>
						<td style="width: 30%;">社員番号</td>
						<td><input type="text" name="empId"></td>
					</tr>
					<tr>
						<td>姓名</td>
						<td><input type="text" name="username"></td>
					</tr>
					<tr>
						<td>年齢</td>
						<td><input type="text" name="age"></td>
					</tr>
					<tr>
						<td>性別</td>
						<td><select name="gender">
								<option value="">-</option>
								<option value="True">男</option>
								<option value="False">女</option>
						</select></td>
					</tr>
					<tr>
						<td>婚姻状況</td>
						<td><input type="radio" id="single" name="maritalStatus"
							value="False"> <label for="single">未婚</label> <input
							type="radio" id="married" name="maritalStatus" value="True">
							<label for="married">既婚</label></td>
					</tr>
					<tr>
						<td>入社年月日</td>
						<td><input type="text" name="joinDate" id="joinDate"></td>
					</tr>
					<tr>
						<td>出生年月日</td>
						<td class="select-container"><select name="birthYear"></select>
							<select name="birthMonth"></select> <select name="birthDay"></select>
						</td>
					</tr>
					<tr>
						<td></td>
						<td id="errorArea"><span id="errorMSG"></span> <input
							type="button" value="リセット" id="reset-button"> <input
							type="submit" value="検索" id="search-button"></td>
					</tr>
				</table>
			</form>
		</div>

		<!-- 新规按钮 -->
		<div id="new-button">
			<button>新規</button>
		</div>
	</div>

	<!-- 自定义提示框 -->
	<div id="dialog-overlay"></div>
	<div id="custom-dialog">
		<div class="dialog-content">
			
		</div>
		<div class="dialog-buttons">
			<button id="new-page-button" class="dialog-button">新規画面</button>
			<button id="csv-upload-button" class="dialog-button">CSVアップロード</button>
		</div>
		<div class="dialog-content">
			<button id="close-dialog" class="dialog-button">閉じる</button>
		</div>
	</div>

	<div class="pagination"></div>

	<!-- 员工列表 -->
	<div id="employee-list">
		<form>
			<table id="employeeListTable">
				<thead>
					<tr>
						<th></th>
						<th>社員番号</th>
						<th>姓名</th>
						<th>年齢</th>
						<th>性別</th>
						<th>婚姻状況</th>
						<th>入社年月日</th>
						<th>出生年月日</th>
						<th>住所</th>
						<th>学歴</th>
						<th>オプション</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</form>
	</div>
</body>
</html>
