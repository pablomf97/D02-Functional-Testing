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

<jstl:choose>

	<jstl:when test="${isPrincipal}">
		<security:authorize access="hasAnyRole('BROTHERHOOD')">


			<table class="displayStyle">
				<tr>
					<td><strong> <spring:message code="segment.expectedTimeOrigin" />
							:
					</strong></td>
					<td><jstl:out value="${segment.expectedTimeOrigin}" /></td>
				</tr>

				<tr>
					<td><strong> <spring:message code="segment.expectedTimeDestination" />
							:
					</strong></td>
					<td><jstl:out value="${segment.expectedTimeDestination}" /></td>
				</tr>

				<tr>
					<td><strong> <spring:message
								code="segment.parade" /> :
					</strong></td>
					<td><jstl:out value="${segment.parade.title}" /></td>
				</tr>

				<tr>
					<td><strong> <spring:message
								code="segment.origin" /> :
					</strong></td>
					<td><jstl:out value="${segment.origin}" /></td>
				</tr>

				<tr>
					<td><strong> <spring:message
								code="segment.destination" /> :
					</strong></td>
					<td><jstl:out value="${segment.destination}" /></td>
				</tr>

			</table>

			<div></div>

			<input type="button" name="back"
				value="<spring:message code="segment.back" />"
				onclick="window.history.back()" />

			<jstl:if test="${isPrincipal && segment.isEditable}">
				<input type="button" name="edit"
					value="<spring:message code="segment.edit" />"
					onclick="redirect: location.href = 'segment/edit.do?segmentId=${segment.id}';" />
			</jstl:if>

		</security:authorize>
	</jstl:when>
	<jstl:otherwise>

		<jstl:choose>
			<jstl:when test="${!segment.parade.isDraft}">
				<table class="displayStyle">
					<tr>
					<td><strong> <spring:message code="segment.expectedTimeOrigin" />
							:
					</strong></td>
					<td><jstl:out value="${segment.expectedTimeOrigin}" /></td>
				</tr>

				<tr>
					<td><strong> <spring:message code="segment.expectedTimeDestination" />
							:
					</strong></td>
					<td><jstl:out value="${segment.expectedTimeDestination}" /></td>
				</tr>

				<tr>
					<td><strong> <spring:message
								code="segment.parade" /> :
					</strong></td>
					<td><jstl:out value="${segment.parade.title}" /></td>
				</tr>

				<tr>
					<td><strong> <spring:message
								code="segment.origin" /> :
					</strong></td>
					<td><jstl:out value="${segment.origin}" /></td>
				</tr>

				<tr>
					<td><strong> <spring:message
								code="segment.destination" /> :
					</strong></td>
					<td><jstl:out value="${segment.destination}" /></td>
				</tr>

				</table>
			</jstl:when>
			<jstl:otherwise>
				<p>
					<spring:message code="segment.notAllowed" />
				</p>
			</jstl:otherwise>
		</jstl:choose>
		<div></div>

		<input type="button" name="back"
			value="<spring:message code="segment.back" />"
			onclick="window.history.back()" />

	</jstl:otherwise>
</jstl:choose>