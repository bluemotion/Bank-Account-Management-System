<%@ page import="csc309.A4.User" %>
<%@ page import="csc309.A4.UserController" %>

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome <s:property value="#session.logged.first" /> <s:property value="#session.logged.last" /></title>
</head>
<body>
	<h1>Client</h1>
	<s:actionerror/>
	<h2 class="style2">Welcome <s:property value="#session.logged.first" /> <s:property value="#session.logged.last" /></h2>
	
  <table>
    <tr>
      <td>Account Balance:</td>
      <td>$ <s:property value="balance" /></td>
    </tr>
  </table>
  	<s:form action="transHis.action">
		<s:submit value="View history" />
	</s:form>
    	
		<hr>
		<span class="style1">Deposit</span>
<form action="User_deposit.action">
		<table>								
 			<tr> 
				<td>Amounts($)</td>
			  <td> <input type="text" name="DepoAmount" /> </td>
			</tr>	
		</table> 
     	<input type="submit" value="Deposit"  />
		<input type="reset" value="Clear Deposit Form" />
</form>
        
      <hr>
      
      
<form action="User_payment.action">      
		<p class="style1">Bill Payment</p>
		<table> 
<tr>
				<td>Select Payee</td>
                <td>
				<select name="PayeeName">
						<s:iterator value="payees">
							<option value="<s:property value="name" />"><s:property value="name" /></option>
						</s:iterator>
				  </select>
				</td>
		</tr>								
 			<tr> 
				<td>Amount($)</td>
			  	<td> <input type="text" name="PaymentAmount" /> </td>
			</tr>	
  </table>
		  <input type="submit" value="Confirm Payment"  />
		  <input type="reset" value = "Clear Payment Form" />
</form>

<hr>

	<form action="User_logout.action">	
		<input type="submit" value='Logout' />
	</form>
</body>
</html>