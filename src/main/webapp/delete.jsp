<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="WifiData.WifiService" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	WifiService wifiService = new WifiService();
	String loadDate = request.getParameter("loadDate");
	wifiService.deleteHistory(loadDate);
	response.sendRedirect("history.jsp");
	%>
</body>
</html>