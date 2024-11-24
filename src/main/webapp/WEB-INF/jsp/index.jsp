<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
<h2>Hello World!</h2>
<a href="<c:url value='/tolist' />">转到页面</a>
<hr>
<a href="${pageContext.request.contextPath}/toTest">测试参数校验页面</a>
</body>
</html>
