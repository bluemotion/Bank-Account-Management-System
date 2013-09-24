<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="csc309.A4.User" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>TransHistory</title>
</head>
<body>

<h1>Transaction History</h1> 
<s:actionerror/>
<h2>Deposits</h2>
<table border='1'>

		
	<tr><td>id</td><td>account_id</td><td>ammount</td></tr>
	<s:iterator value="deposits">
		<tr>
			<td><s:property value="id" /></td>
			<td><s:property value="account_id" /></td>
			<td><s:property value="ammount" /></td>
		</tr>
	</s:iterator>
</table>
<hr>
<h2>Payments</h2>
<table border='1'>
	<tr><td>id</td><td>account_id</td><td>payee_id</td><td>ammount</td><td>date</td></tr>
	<s:iterator value="payments">
		<tr>
			<td><s:property value="id" /></td>
			<td><s:property value="account_id" /></td>
			<td><s:property value="payee_id" /></td>
			<td><s:property value="ammount" /></td>
			<td><s:property value="date" /></td>
		</tr>
	</s:iterator>
</table>
<hr>
<s:form action="TransBack.action">
	<s:submit value="Back" />
</s:form>

</body>
</html>