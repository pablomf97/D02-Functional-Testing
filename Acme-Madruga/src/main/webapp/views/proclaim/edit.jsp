<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('CHAPTER')">
	<jstl:if test="${possible}">
		<form:form modelAttribute="proclaim" action="proclaim/create.do"
			id="form">

			<spring:message code="proclaim.description" />:
			<br>
			<form:textarea code="proclaim.description" path="description" />

			<br>
			<br>
			
			<input type="submit" name="save"
				value="<spring:message code="proclaim.save"/>"
				onclick="return confirm(''); " />&nbsp;
		
		
		<acme:cancel url="welcome/index.do" code="proclaim.cancel" />

		</form:form>

	</jstl:if>

	<jstl:if test="${!possible}">

		<h3>
			<spring:message code="no.permission" />
		</h3>

	</jstl:if>

</security:authorize>