<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@ page import="WifiData.SearchData" %>
<%@ page import="WifiData.WifiService" %> <%-- java클래스 import / package.class명 --%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>위치 히스토리 목록</title>
</head>
<body>
	<h1>위치 히스토리 목록</h1>
	<a href="http://localhost:8080/Mission1_1/index.jsp">홈</a> | <a href="http://localhost:8080/Mission1_1/history.jsp">위치 히스토리 목록</a> | <a href="http://localhost:8080/Mission1_1/load-wifi.jsp">Open API 와이파이 정보 가져오기</a> <br><br>
	<table border="1"; bordercolor="white"; width="100%";>
		<tr align="center"; bgcolor="#04B486"; span style="color:white">
			<th>ID</th>
			<th>X좌표</th>
			<th>Y좌표</th>
			<th>조회일자</th>
			<th>비고</th>
		</tr>
		<%
			ArrayList<SearchData> list = new ArrayList<>();	
		
			WifiService wifiService = new WifiService();
			list = wifiService.readHistoryData();
			int a = wifiService.readHistoryDataCnt();
			for(int i = a - 1; i >= 0; i--) { %>
			<tr align="center";>
				<td><%=i + 1 %></td>
				<td><%=list.get(i).getLAT() %></td>
				<td><%=list.get(i).getLNT() %></td>
				<td><%=list.get(i).getSearchDateTime() %></td>
				<td><button type="button" id="<%=list.get(i).getSearchDateTime() %>" onclick="deleteDataUseloadDate(this)"> 삭제 </button></td>
			</tr>
			<%
			}
		%>
		
	</table>
	<script>
		function deleteDataUseloadDate(e) {
			const loadDate = e.id;
			location.href="http://localhost:8080/Mission1_1/delete.jsp?loadDate="+loadDate;
		}
	
	</script>
</body>
</html>