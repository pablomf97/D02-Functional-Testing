<%--
 * action-1.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<security:authorize access="hasRole('BROTHERHOOD')">

	<table class="displayStyle">
		<tr>
			<td><strong> <spring:message
						code="platform.title" /> :
			</strong></td>
			<td><jstl:out value="${platform.title}">
				</jstl:out></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="platform.pictures" />
					:
			</strong></td>
			<td><jstl:out value="${platform.pictures}">
				</jstl:out></td>
		</tr>
		
		<tr>
			<td><strong> <spring:message code="platform.description" />
					:
			</strong></td>
			<td><jstl:out value="${platform.description}">
				</jstl:out></td>
		</tr>
		
	</table>
	<div></div>
	
		<jstl:if test="${isPrincipal}">
			<input type="button" name="edit"
			value="<spring:message code="platform.edit" />"
			onclick="redirect: location.href = 'platform/edit.do?platformId=${platform.id}';" />
		</jstl:if>
	
		<input type="button" name="back"
		value="<spring:message code="platform.back" />"
		onclick="window.history.back()" />
		


</security:authorize>

<security:authorize access="isAnonymous()">
	<table class="displayStyle">
		<tr>
			<td><strong> <spring:message
						code="platform.title" /> :
			</strong></td>
			<td><jstl:out value="${platform.title}">
				</jstl:out></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="platform.pictures" />
					:
			</strong></td>
			<td><jstl:out value="${platform.pictures}">
				</jstl:out></td>
		</tr>
		
		<tr>
			<td><strong> <spring:message code="platform.description" />
					:
			</strong></td>
			<td><jstl:out value="${platform.description}">
				</jstl:out></td>
		</tr>
		
	</table>
	
	<div></div>
	
		<input type="button" name="back"
		value="<spring:message code="platform.back" />"
		onclick="window.history.back()" />
</security:authorize>
