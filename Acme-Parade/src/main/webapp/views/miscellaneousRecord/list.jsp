<%--
 * action-1.jsp
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

<security:authorize access="hasRole('BROTHERHOOD')">

	<display:table pagesize="10" class="displaytag"
		name="miscellaneousRecords"
		requestURI="miscellanousReord/list.do?historyId=${historyId}" id="row">

		<display:column titleKey="miscellaneousRecord.title">
			<jstl:out value="${row.title}"></jstl:out>
		</display:column>

		<display:column titleKey="miscellaneousRecord.display">
			<a
				href="miscellaneousRecord/display.do?miscellaneousRecordId=${row.id}">
				<spring:message code="miscellaneousRecord.display" />
			</a>
		</display:column>
	</display:table>

	<a href="miscellaneousRecord/create.do"> <spring:message
			code="miscellaneousRecord.create" />

	</a>&nbsp;&nbsp;&nbsp;
		
	<acme:cancel url="history/display.do"
		code="miscellaneousRecord.cancel" />

</security:authorize>
