<%@ page import="java.sql.*" %>
<%@ page import="WifiData.WifiService" %> <%-- java클래스 import / package.class명 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>와이파이 정보 가져오기</title>
</head>
<body>
	<%
		int totalCnt = 0;
		try {
			Class.forName("org.sqlite.JDBC");
			String dbFile = "Wifi-List.db";
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
			
			WifiService wifiService = new WifiService();
			
			wifiService.getWifiInfoFromAPIBatch();
			totalCnt = wifiService.beforeTotalWifiInfoCnt;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	%>
	
	<p style="font-size: 200%; font-weight: bold; text-align: center;"> <%=totalCnt%>개의 WIFI 정보를 정상적으로 저장하였습니다.</p>
	
	<p style="text-align: center;"><a href="http://localhost:8080/Mission1_1/index.jsp">홈으로 가기</a></p>
	
</body>
</html>