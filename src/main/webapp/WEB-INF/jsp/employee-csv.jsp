<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>csvファイルアップロード</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static/css/style_list.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
	$(document).ready(function() {
		//获取上下文路径
        var contextPath = $('body').data('context-path');
		
		var lastLine = "";
		var currentColorIndex = 0;
		var colors = [ "#D9E1F2",  "#E2EFDA" ]; // 颜色数组，交替使用

		$('.error-ul li').each(function() {
			var text = $(this).text();
			var linePrefix = text.split(':')[0]; // 获取类似于 "Line1" 的前缀

			if (linePrefix !== lastLine) {
				// 如果当前行前缀和上一个不同，则切换背景颜色
				currentColorIndex = (currentColorIndex + 1) % colors.length;
				lastLine = linePrefix;
			}

			$(this).css('background-color', colors[currentColorIndex]);
		});

		$("#return-button").click(function() {
             window.location.href = contextPath + '/employeeList';
         });

		$("#tem-down").click(function() {
            window.location.href = contextPath + '/templateDownload';
        });
	});
</script>

</head>
<body data-context-path="${pageContext.request.contextPath}">
	<div id="csv-body">
	
		<div id="csv-area">
			<input type="button" id="tem-down" value="テンプレート　ダウンロード" />
			<h2>csvファイルアップロード</h2>
			<form action="${pageContext.request.contextPath}/employeeCsvInsert"
				method="post" enctype="multipart/form-data">
				<label for="csvFile">csvファイルを選択してください:</label> <input type="file"
					id="csvFile" name="csvFile" accept=".csv" required> <br>
				<br> <input type="submit" value="アップロード">
				<input id="return-button" type="button" value="戻る">
				
			</form>

			<h3 class="error-heading">検証情報</h3>
			<div class="error-container">
				<c:if test="${not empty errorMessages}">
					<ul class="error-ul">
						<c:forEach var="error" items="${errorMessages}">
							<li class="error-li">${error}</li>
						</c:forEach>
					</ul>
				</c:if>
				
			</div>
		</div>
	</div>

</body>
</html>