<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
<h1>${serverTime }</h1>
<h1>${user }</h1>

    <c:forEach var="entity" items="${list}">
        <li>${entity.title}</li>
        <li>${entity.context}</li>
        <li>${entity.regdt}</li>
    </c:forEach>



</body>
</html>