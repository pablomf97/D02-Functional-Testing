<%--
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('SPONSOR')">

	<display:table pagesize="10" class="displaytag"
		name="sponsorships" requestURI="sponsorship/list.do" id="row">

		<display:column titleKey="sponsorship.banner">
			<jstl:out value="${row.banner}"></jstl:out>
		</display:column>


		<display:column titleKey="sponsorship.target">
			<jstl:out value="${row.target}"></jstl:out>
		</display:column>

		<display:column titleKey="sponsorship.sponsor">
			<jstl:out value="${row.sponsor.userAccount.username}"></jstl:out>
		</display:column>

		<display:column titleKey="sponsorship.display">
			<a href="sponsorship/display.do?sponsorshipId=${row.id}"> <spring:message
					code="sponsorship.display" />
			</a>
		</display:column>

	</display:table>

	<a href="sponsorship/create.do"> <spring:message
			code="sponsorship.create" />

	</a>&nbsp;&nbsp;&nbsp;
		
	<acme:cancel url="welcome/index.do" code="sponsorship.cancel" />


</security:authorize>