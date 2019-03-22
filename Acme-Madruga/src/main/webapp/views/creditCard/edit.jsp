<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<p>
	<spring:message code="actor.edit" />
</p>

<form:form action="creditCard/edit.do" modelAttribute="creditCard"
	methodParam="post">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<acme:textbox code="creditCard.holder" path="holder" />
		<br>
		<br>

	<form:select path="make" items="${makes}" />
	<br />

	<acme:textbox code="creditCard.number" path="number" />
		<br>
		<br>

<acme:textbox code="creditCard.expirationMonth" path="expirationMonth" />
		<br>
		<br>

<acme:textbox code="creditCard.expirationYear" path="expirationYear" />
		<br>
		<br>


<acme:textbox code="creditCard.CVV" path="CVV" />
		<br>
		<br>
	<input type="submit" name="save" id="save"
		value='<spring:message code="creditCard.save"/>' />
	<input type="button" name="cancel"
		value="<spring:message code="creditCard.cancel" />"
		onclick="window.history.back()" />
	<br />

</form:form>