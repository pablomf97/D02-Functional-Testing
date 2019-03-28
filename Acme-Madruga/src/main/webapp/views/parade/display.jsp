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
			<td><strong> <spring:message code="parade.title" />	: </strong></td>
			<td><jstl:out value="${parade.title}" /></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="parade.ticker" /> : </strong></td>
			<td><jstl:out value="${parade.ticker}" /></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="parade.description" /> : </strong></td>
			<td><jstl:out value="${parade.description}" /></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="parade.organisedMoment" /> : </strong></td>
			<td><jstl:out value="${parade.organisedMoment}" /></td>
		</tr>
		
		<tr>
			<td><strong> <spring:message code="parade.maxCols" /> : </strong></td>
			<td><jstl:out value="${parade.maxCols}" /></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="parade.isDraft" /> : </strong></td>
			<td><jstl:out value="${parade.isDraft}" /></td>
		</tr>

	</table>
	<div></div>
	<jstl:choose>
		<jstl:when test="${not empty parade.platforms}">
			<h3>
				<strong> <spring:message code="parade.platform" />
				</strong>
			</h3>
			<jstl:forEach var="platform" items="${parade.platforms}">
				<table>
					<tr>
						<td><strong> <spring:message code="parade.platform.title" />
								:
						</strong></td>
						<td><jstl:out value="${platform.title}">
							</jstl:out></td>
					</tr>		
					
					<tr>
						<td><strong> <spring:message code="parade.platform.description" />
								:
						</strong></td>
						<td><jstl:out value="${platform.description}">
							</jstl:out></td>
					</tr>
					<tr><td>
					<a href="platform/display.do?platformId=${platform.id}"> <spring:message
							code="parade.platform.display" />	</a></td>
					</tr>
				</table>
			</jstl:forEach>
		</jstl:when>
		<jstl:otherwise>
			<p>
				<strong> <spring:message code="parade.no.platforms" />
				</strong>
			</p>
		</jstl:otherwise>
	</jstl:choose>
	
	<div></div>

	<input type="button" name="back"
		value="<spring:message code="parade.back" />"
		onclick="window.history.back()" />

	<jstl:if test="${isPrincipal && parade.isDraft}">
		<input type="button" name="edit"
			value="<spring:message code="parade.edit" />"
			onclick="redirect: location.href = 'parade/edit.do?paradeId=${parade.id}';" />
	</jstl:if>
	<jstl:if test="${isPrincipal}">
		<input type="button" name="copy"
		value="<spring:message code="parade.copy" />"
		onclick="redirect: location.href = 'parade/copy.do?paradeId=${parade.id}';"  />
	</jstl:if>
</security:authorize>

<security:authorize access="isAnonymous() or hasAnyRole('MEMBER','CHAPTER','ADMINISTRATOR')">

	<jstl:choose>
		<jstl:when test="${!parade.isDraft}">
			<table class="displayStyle">
				<tr>
					<td><strong> <spring:message code="parade.title" />
							:
					</strong></td>
					<td><jstl:out value="${parade.title}" /></td>
				</tr>

				<tr>
					<td><strong> <spring:message code="parade.ticker" />
							:
					</strong></td>
					<td><jstl:out value="${parade.ticker}" /></td>
				</tr>

				<tr>
					<td><strong> <spring:message
								code="parade.description" /> :
					</strong></td>
					<td><jstl:out value="${parade.description}" /></td>
				</tr>

				<tr>
					<td><strong> <spring:message
								code="parade.organisedMoment" /> :
					</strong></td>
					<td><jstl:out value="${parade.organisedMoment}" /></td>

				</tr>

			</table>
		</jstl:when>
		<jstl:otherwise>
			<p>
				<spring:message code="parade.notAllowed" />
			</p>
		</jstl:otherwise>
	</jstl:choose>
	<div></div>

	<input type="button" name="back"
		value="<spring:message code="parade.back" />"
		onclick="window.history.back()" />

</security:authorize>