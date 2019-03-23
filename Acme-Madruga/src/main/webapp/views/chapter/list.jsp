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

<security:authorize access="isAnonymous()">
	
	<display:table pagesize="10" class="displaytag"
		name="chapters" requestURI = "chapter/list.do" id="row">
	
	<display:column titleKey="chapter.title">
		<jstl:out value="${row.title}"></jstl:out>
	</display:column>
	
	<display:column titleKey="chapter.zone">
		<jstl:out value="${row.zone.name}"></jstl:out>
	</display:column>
	
	<display:column titleKey="show.chapter.zone">
		<a href="zone/display.do?chapterId=${row.id}">
			<spring:message code="show.chapter.zone"/>
		</a>
	</display:column>
	
	<display:column titleKey="show.brotherhoods">
		<a href="brotherhood/listSimple.do?chapterId=${row.id}">
			<spring:message code="show.brotherhoods"/>
		</a>
	</display:column>
	
	<display:column titleKey="show.parades">
		<a href="procession/listSimple.do?chapterId=${row.id}">
			<spring:message code="show.parades"/>
		</a>
	</display:column>
	
	<display:column titleKey="show.proclaims">
		<a href="proclaim/list.do?chapterId=${row.id}">
			<spring:message code="show.proclaims"/>
		</a>
	</display:column>
	</display:table>
	
</security:authorize>