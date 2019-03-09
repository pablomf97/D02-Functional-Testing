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

<!-- Listing grid -->

<security:authorize access="hasRole('ADMINISTRATOR')">
	<display:table name="zones" id="row"
		requestURI="zone/administrator/list.do" pagesize="5"
		class="displaytag">

		<!-- Action links -->

		<display:column>

			<a href="zone/administrator/edit.do?zoneId=${row.id}"> <spring:message
					code="zone.edit" />
			</a>

		</display:column>

		<!-- Attributes -->
		<spring:message code="zone.name" var="nameHeader" />
		<display:column title="${nameHeader}">
			<jstl:out value="${row.name}" />
		</display:column>

		<spring:message code="zone.pictures" var="pictureHeader" />
		<display:column property="pictures" title="${pictureHeader}">
			<jstl:out value="${row.pictures}" />
		</display:column>

		<div>
			<a href="zone/administrator/create.do"> <spring:message
					code="zone.create" />
			</a>
		</div>



	</display:table>

</security:authorize>
