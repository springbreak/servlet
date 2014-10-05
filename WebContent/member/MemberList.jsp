<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.*" %>
<%
Map<String, String> headerMap = new LinkedHashMap<String, String>(); 
headerMap.put("ID", "Id");
headerMap.put("NAME", "Name");
headerMap.put("EMAIL", "E-mail");
headerMap.put("CREATED_DATE", "Created Date");
request.setAttribute("headerList", headerMap);
%>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원 목록</title>
</head>
<body>
	<jsp:include page="/Header.jsp" />
	
	<h1>회원목록2</h1>
	<p>
		<a href='add.do'>신규 회원</a>
	</p>
	<table>
		<tr>	
			<c:forEach var="entry" items="${headerList}">
			<c:set var="asc" value="${entry.key}_ASC" />
			<c:set var="desc" value="${entry.key}_DESC" />
			<th>
			<c:choose>
				<c:when test="${orderCond eq asc}">
					<a href="list.do?orderCond=${desc}">${entry.value}↑</a>
				</c:when>
				<c:when test="${orderCond eq desc}">
					<a href="list.do?orderCond=${asc}">${entry.value}↓</a>
				</c:when>
				<c:otherwise>
					<a href="list.do?orderCond=${asc}">${entry.value}</a>
				</c:otherwise>
			</c:choose>
			</th>
			</c:forEach>
			<th>Delete</th>
		</tr>
		<c:forEach var="member" items="${members}"> 
		<tr>
			<td>${member.id}</td>
  			<td>
  				<a href='update.do?id=${member.id}'>${member.name}</a>
  			</td>
  			<td>${member.email}<td/>
  			<td>${member.createdDate}</td>
  			<td>
  				<a href='delete.do?id=${member.id}'>삭제</a>
  			<td/>
		</tr>
		</c:forEach>
	</table>
	<jsp:include page="/Tail.jsp" />
</body>
</html>