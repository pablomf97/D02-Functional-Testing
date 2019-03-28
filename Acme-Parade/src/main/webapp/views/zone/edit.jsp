<%--
 * action-2.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<security:authorize access="hasRole('ADMINISTRATOR')">
	<jstl:if test="${permission == true}">

		<form:form action="zone/administrator/edit.do" modelAttribute="zone"
			id="zoneForm">

			<form:hidden path="id" />

			<acme:textbox code="zone.name" path="name" />
			<br>

			<acme:textbox code="zone.pictures" path="pictures" />
			<br>
			<br>

			<acme:submit code="zone.save" name="save" />&nbsp;
			


			<jstl:if test="${zone.id != 0 and isSelected == false}">
				<acme:delete code="zone.delete" name="delete"
					confirmation="zone.confirm.delete" />&nbsp;

		</jstl:if>

			<acme:cancel code="zone.cancel" url="zone/administrator/list.do" />
			<br />
			<br />
		</form:form>
	</jstl:if>

	<jstl:if test="${permission == false }">
		<spring:message code="zone.noPermission" />
	</jstl:if>
</security:authorize>



