<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<security:authorize access="hasRole('BROTHERHOOD')">

	<form:form modelAttribute="legalRecord" action="legalRecord/edit.do"
		id="form">

		<form:hidden path="id" />

		<acme:textbox code="legalRecord.title" path="title" />
		<br>
		<br>

		<spring:message code="legalRecord.description" />
		<br>
		<form:textarea code="legalRecord.description" path="description" />

		<br>
		<br>
		<acme:textbox code="legalRecord.name" path="name" />
		<br>
		<br>
		<acme:textbox code="legalRecord.VAT" path="VAT" />
		<br>
		<br>
		<acme:textbox code="legalRecord.laws" path="laws" />
		<br>
		<br>
		<acme:submit code="legalRecord.save" name="save" />&nbsp;
		<input type="button" name="back"
			value="<spring:message code="legalRecord.cancel" />"
			onclick="window.history.back()" />
		
		<jstl:if test="${legalRecord.id != 0 }">
		<acme:delete name="delete" confirmation="mr.confirm.delete"
			code="legalRecord.delete" />
		<br />
		<br />
		</jstl:if>
	</form:form>

</security:authorize>