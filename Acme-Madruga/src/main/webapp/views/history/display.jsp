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
			<td><strong><spring:message
						code="history.inceptionRecord" />: </strong></td>
			<td><jstl:out value="${inceptionRecord.title}"></jstl:out></td>
			<td><a
				href="inceptionRecord.display.do?inceptionRecordId=${inceptionRecord.id}">
					<spring:message code="history.inceptionRecord.display" />
			</a></td>
		</tr>







		<tr>
			<td><strong><spring:message
						code="history.first.miscellaneousRecord" />: </strong></td>
			<jstl:if test="${!miscellaneousRecords.isEmpty()}">
			<td><jstl:out value="${miscellaneousRecord.title}"></jstl:out></td>

			<td><a
				href="miscellaneousRecord/list.do?historyId=${history.id}"> <spring:message
						code="history.miscellaneousRecord.list" />
			</a></td>
			</jstl:if>
		</tr>

		<tr>
			<td><strong><spring:message
						code="history.first.periodRecord" />: </strong></td>
			<jstl:if test="${!periodRecords.isEmpty()}">
			<td><jstl:out value="${periodRecord.title}"></jstl:out></td>

			<td><a href="periodRecord/list.do?historyId=${history.id}">
					<spring:message code="history.periodRecord.list" />
			</a></td>
			</jstl:if>
		</tr>

		<tr>
			<td><strong><spring:message
						code="history.first.legalRecord" />: </strong></td>
			<jstl:if test="${!legalRecords.isEmpty()}">
			<td><jstl:out value="${legalRecord.title}"></jstl:out></td>

			<td><a href="legalRecord/list.do?historyId=${history.id}"> <spring:message
						code="history.legalRecord.list" />
			</a></td>
			</jstl:if>
		</tr>

		<tr>
			<td><strong><spring:message
						code="history.first.linkRecord" />: </strong></td>
			<jstl:if test="${!linkRecords.isEmpty()}">
				<td><jstl:out value="${linkRecord.title}"></jstl:out></td>

				<td><a href="linkRecord/list.do?historyId=${history.id}"> <spring:message
							code="history.linkRecord.list" />
				</a></td>
			</jstl:if>
			
			<jstl:if test="${linkRecords.isEmpty()}">
				<td>
					<spring:message code="history.empty.record"/>
				</td>
			</jstl:if>

		</tr>




	</table>
</security:authorize>
