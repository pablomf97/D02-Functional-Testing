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

<security:authorize access="hasRole('MEMBER')">

	<display:table name="parades" id="row"
		requestURI="finder/member/list.do" pagesize="10" class="displaytag">

		<!-- Attributes-->

		<display:column titleKey="parade.title" sortable="true">
			<jstl:out value="${row.title}" />
		</display:column>
		<display:column titleKey="parade.ticker" sortable="true">
			<jstl:out value="${row.ticker}" />
		</display:column>
		<display:column property="description"
			titleKey="parade.description">
			<jstl:out value="${row.description}" />
		</display:column>
		<display:column titleKey="parade.organisedMoment" sortable="true">
			<jstl:out value="${row.organisedMoment}" />
		</display:column>

		<!-- Action links -->

		<display:column>
			<a href="parade/display.do?paradeId=${row.id}"> <spring:message
					code="parade.display" />
			</a>
		</display:column>


	</display:table>

	<input type="button" name="cancel"
		value="<spring:message code="finder.back" />"
		onclick="javascript: relativeRedir('finder/member/search.do');" />
</security:authorize>