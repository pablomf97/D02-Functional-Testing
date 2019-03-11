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

	<form:form action="enrolment/brotherhood/edit.do"
		modelAttribute="enrolment">

		<form:hidden path="id" />

		<p>
			<spring:message code="enrolment.position.edit" />
		</p>

		<jstl:choose>
			<jstl:when test="${pageContext.response.locale.language == 'es'}">
				<select name="positionT">
					<jstl:forEach items="${positions}" var="x">
						<option value="${x.name.get('Español')}">
							<jstl:out value="${x.name.get('Español')}" />
						</option>
					</jstl:forEach>
				</select>
			</jstl:when>
			<jstl:otherwise>
				<select name="positionT">
					<jstl:forEach items="${positions}" var="x">
						<option value="${x.name.get('English')}">
							<jstl:out value="${x.name.get('English')}" />
						</option>
					</jstl:forEach>
				</select>
			</jstl:otherwise>
		</jstl:choose>
		<br />
		<br />
		<acme:submit code="commit.enrolment" name="save"/>&nbsp;
	</form:form>

</security:authorize>