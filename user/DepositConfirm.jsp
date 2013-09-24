<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="csc309.A4.User" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Deposit Summary</title>
</head>
<body>

<h1>Deposit Summary</h1> 
<s:actionerror/>

<s:form action="User_client">
	<s:submit value="Client Home" />
</s:form>
	
<hr>

<table>
	<tr>
		<td>Deposit Amount: </td><td><s:property value="depoAmount" /></td>
	</tr>
	<tr>
		<td>Balance: </td><td><s:property value="balance" /></td>
	</tr>
</table>
</body>
</html>