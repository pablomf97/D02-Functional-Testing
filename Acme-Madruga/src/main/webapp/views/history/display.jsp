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

	<table class="displayStyle">
		<tr>
			<td><strong> <spring:message code="history.inception" />
					:
			</strong></td>
			<td><jstl:out value="${history.inceptionRecord.title}"></jstl:out></td>
			<td></td>
		</tr>



		<tr>
			<td><strong><spring:message code="history.periods" />:
			</strong></td>
			<td></td>
			<td><a
				href="periodRecord/brotherhood/list.do?historyId?${history.id}">
					<spring:message code="list.periods" />
			</a></td>
		</tr>

		<tr>
			<td><strong><spring:message code="history.legals" />:
			</strong></td>
			<td></td>
			<td><a
				href="legalRecord/brotherhood/list.do?historyId?${history.id}">
					<spring:message code="list.legals" />
			</a></td>
		</tr>

		<tr>
			<td><strong><spring:message code="history.links" />: </strong></td>
			<td></td>
			<td><a
				href="linkRecord/brotherhood/list.do?historyId?${history.id}"> <spring:message
						code="list.links" />
			</a></td>
		</tr>

		<tr>
			<td><strong><spring:message
						code="history.miscellaneous" />: </strong></td>
			<td></td>
			<td><a
				href="miscellaneousRecord/brotherhood/list.do?historyId?${history.id}">
					<spring:message code="list.miscellaneous" />
			</a></td>
		</tr>

	</table>
</security:authorize>
