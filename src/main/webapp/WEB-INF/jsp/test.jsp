<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>测试javabean参数校验</title>
</head>
<body>
<h1>测试javabean参数校验</h1>
<form action="${pageContext.request.contextPath}/testValidate">
	<p>社员番号</p><input type="text" name="empId"/>
	<p>姓名</p><input type="text" name="username"/>
	<p>年齡</p><input type="text" name="age"/>
	<p>性别</p><input type="text" name="gender"/>
	<input type="submit" value="提交"/>
</form>

</body>
</html>