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
			<td><strong> <spring:message code="procession.title" />	: </strong></td>
			<td><jstl:out value="${procession.title}" /></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="procession.ticker" /> : </strong></td>
			<td><jstl:out value="${procession.ticker}" /></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="procession.description" /> : </strong></td>
			<td><jstl:out value="${procession.description}" /></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="procession.organisedMoment" /> : </strong></td>
			<td><jstl:out value="${procession.organisedMoment}" /></td>
		</tr>
		
		<tr>
			<td><strong> <spring:message code="procession.maxCols" /> : </strong></td>
			<td><jstl:out value="${procession.maxCols}" /></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="procession.isDraft" /> : </strong></td>
			<td><jstl:out value="${procession.isDraft}" /></td>
		</tr>

	</table>
	<div></div>
	<jstl:choose>
		<jstl:when test="${not empty procession.platforms}">
			<h3>
				<strong> <spring:message code="procession.platform" />
				</strong>
			</h3>
			<jstl:forEach var="platform" items="${procession.platforms}">
				<table>
					<tr>
						<td><strong> <spring:message code="procession.platform.title" />
								:
						</strong></td>
						<td><jstl:out value="${platform.title}">
							</jstl:out></td>
					</tr>		
					
					<tr>
						<td><strong> <spring:message code="procession.platform.description" />
								:
						</strong></td>
						<td><jstl:out value="${platform.description}">
							</jstl:out></td>
					</tr>
					<tr><td>
					<a href="platform/display.do?platformId=${platform.id}"> <spring:message
							code="procession.platform.display" />	</a></td>
					</tr>
				</table>
			</jstl:forEach>
		</jstl:when>
		<jstl:otherwise>
			<p>
				<strong> <spring:message code="procession.no.platforms" />
				</strong>
			</p>
		</jstl:otherwise>
	</jstl:choose>
	
	<div></div>

	<input type="button" name="back"
		value="<spring:message code="procession.back" />"
		onclick="window.history.back()" />

	<jstl:if test="${isPrincipal && procession.isDraft}">
		<input type="button" name="edit"
			value="<spring:message code="procession.edit" />"
			onclick="redirect: location.href = 'procession/edit.do?processionId=${procession.id}';" />
	</jstl:if>

</security:authorize>

<security:authorize access="hasRole('MEMBER')">

	<jstl:choose>
		<jstl:when test="${!procession.isDraft}">
			<table class="displayStyle">
				<tr>
					<td><strong> <spring:message code="procession.title" />
							:
					</strong></td>
					<td><jstl:out value="${procession.title}" /></td>
				</tr>

				<tr>
					<td><strong> <spring:message code="procession.ticker" />
							:
					</strong></td>
					<td><jstl:out value="${procession.ticker}" /></td>
				</tr>

				<tr>
					<td><strong> <spring:message
								code="procession.description" /> :
					</strong></td>
					<td><jstl:out value="${procession.description}" /></td>
				</tr>

				<tr>
					<td><strong> <spring:message
								code="procession.organisedMoment" /> :
					</strong></td>
					<td><jstl:out value="${procession.organisedMoment}" /></td>

				</tr>

			</table>
		</jstl:when>
		<jstl:otherwise>
			<p>
				<spring:message code="procession.notAllowed" />
			</p>
		</jstl:otherwise>
	</jstl:choose>
	<div></div>

	<input type="button" name="back"
		value="<spring:message code="procession.back" />"
		onclick="window.history.back()" />

</security:authorize>