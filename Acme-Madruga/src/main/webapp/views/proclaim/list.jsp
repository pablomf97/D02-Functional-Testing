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

<security:authorize access="isAnonymous()">

	<display:table pagesize="10" class="displaytag"
		name="proclaims"
		requestURI="proclaim/list.do?chapterId=${chapterId}" id="row">
		
		
		<display:column titleKey="proclaim.publishedMoment" sortable="true">
			<jstl:out value="${row.publishedMoment}"></jstl:out>
		</display:column>
		
		<display:column titleKey="proclaim.description">
			<jstl:out value="${row.description}"></jstl:out>
		</display:column>
		
	</display:table>
		
	<acme:cancel url="chapter/list.do"
		code="proclaim.cancel" />

</security:authorize>
