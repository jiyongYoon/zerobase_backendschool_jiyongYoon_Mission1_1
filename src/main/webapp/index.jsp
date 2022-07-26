<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="WifiData.WifiService" %> <%-- java클래스 import / package.class명 --%>
<%@ page import="WifiData.SearchData" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="java.util.*" %>
    
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
<title>와이파이 정보 구하기</title>
</head>
<body>

<header>
	<h1> 와이파이 정보 구하기 </h1>
</header>
	<a href="http://localhost:8080/Mission1_1/index.jsp">홈</a> | <a href="http://localhost:8080/Mission1_1/history.jsp">위치 히스토리 목록</a> | <a href="http://localhost:8080/Mission1_1/load-wifi.jsp">Open API 와이파이 정보 가져오기</a> <br><br>
	<form>
		LAT: <input type="text" id="lat" name="lat"> , LNT: <input type="text" id="lnt" name="lnt">
		<button type="button" onclick="getUserLocation()">내 위치 가져오기</button> 
		<button type="submit" method="post" onclick="getNearWifiInfo()">근처 WIFI정보 가져오기</button> 
	</form>   
	
	<br>
	<table>
		<tr border="1"; bordercolor="white"; bgcolor="#04B486"; align="center"; width=100%; span style="color:white">
			<th>거리(Km)</th>
			<th>관리번호</th>
			<th>자치구</th>
			<th>와이파이명</th>
			<th>도로명주소</th>
			<th>상세주소</th>
			<th>설치위치(층)</th>
			<th>설치유형</th>
			<th>설치기관</th>
			<th>서비스구분</th>
			<th>망종류</th>
			<th>설치년도</th>
			<th>실내외구분</th>
			<th>WIFI접속환경</th>
			<th>X좌표</th>
			<th>Y좌표</th>
			<th>작업일자</th>
		</tr>
		<tr>
			<th colspan="17">자료불러온 후 조회해주세요</th>
		</tr>
	</table>


	<script>
    function getUserLocation() {
    	navigator.geolocation.getCurrentPosition(function(pos) {
    		console.log(pos);
            var lat = pos.coords.latitude;
            var lnt = pos.coords.longitude;
            alert("현재 위치는 : " + lat + ", "+ lnt);
        });
    }
    
    function getNearWifiInfo() {
    	navigator.geolocation.getCurrentPosition(function(pos) {
	   		console.log(pos);
	        var lat = pos.coords.latitude;
	        var lnt = pos.coords.longitude;
	    	var time = new Date();
	        alert(lat + " " +lnt + time);
	        <%
	        	Date now = new Date();
	        	String lat = request.getParameter("lat");
	        	String lnt = request.getParameter("lnt");
	        	String time = now.toString();
	        	WifiService wifiService = new WifiService();
	    		wifiService.insertHisroty(lat, lnt, time);
        	%>
    	});
    }
    </script>
    
    
</body>
</html>