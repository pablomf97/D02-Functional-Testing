<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jstl:choose>
	<jstl:when test="${isPrincipal && parade.status == 'SUBMITTED'}">
		<form:form action="parade/rejectb.do" modelAttribute="parade"
			id="form">
			<fieldset>
				<br>
				<form:hidden path="id" />

				<acme:textarea code="parade.reason" path="reason" />
				<br /> <br />

			</fieldset>

			<acme:submit code="parade.reject" name="reject" />&nbsp; 
		<acme:cancel code="parade.cancel" url="welcome/index.do" />
			<br />

		</form:form>
	</jstl:when>
	<jstl:otherwise>
		<p>
			<spring:message code="parade.notAllowed" />
		</p>
	</jstl:otherwise>
</jstl:choose>