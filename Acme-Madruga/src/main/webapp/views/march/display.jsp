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


<security:authorize access="hasAnyRole('BROTHERHOOD','MEMBER')">

	<table class="displayStyle">
		<tr>
			<td><strong> <spring:message
						code="march.procession" /> :
			</strong></td>
			<td><jstl:out value="${march.procession.title}">
				</jstl:out></td>
		</tr>
		
		<tr>
			<td><strong> <spring:message
						code="march.brotherhood" /> :
			</strong></td>
			<td><jstl:out value="${march.procession.brotherhood.title}">
				</jstl:out></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="march.status" />
					:
			</strong></td>
			<td><jstl:out value="${march.status}">
				</jstl:out></td>
		</tr>
		
		<jstl:if test="${march.status == 'APPROVED'}">
		<tr>
			<td><strong> <spring:message code="march.row" />
					:
			</strong></td>
			<td><jstl:out value="${march.row}">
				</jstl:out></td>
		</tr>

		<tr>
			<td><strong> <spring:message
						code="march.column" /> :
			</strong></td>
			<td><jstl:out value="${march.col}">
				</jstl:out></td>

		</tr>
		</jstl:if>
		
		<jstl:if test="${march.status == 'REJECTED'}">
		<tr>
			<td><strong> <spring:message
						code="march.reason" /> :
			</strong></td>
			<td><jstl:out value="${march.reason}">
				</jstl:out></td>

		</tr>
		</jstl:if>
		
	</table>
	<div></div>
	
		<input type="button" name="back"
		value="<spring:message code="march.back" />"
		onclick="window.history.back()" />

</security:authorize>