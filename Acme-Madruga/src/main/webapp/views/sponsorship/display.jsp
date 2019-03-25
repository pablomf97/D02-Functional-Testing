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

<security:authorize access="hasRole('SPONSOR')">
	<jstl:if test="${possible}">
		<table class="displayStyle">
			<tr>

				<td><strong><spring:message
							code="sponsorship.banner" /> : </strong></td>
				<td><jstl:out value="${sponsorship.banner}">

					</jstl:out></td>


			</tr>


			<tr>

				<td><strong><spring:message
							code="sponsorship.target" /> : </strong></td>
				<td><jstl:out value="${sponsorship.target}">

					</jstl:out></td>

			</tr>
			
			<tr>

				<td><strong><spring:message
							code="sponsorship.deactivated" /> : </strong></td>
				<td><jstl:out value="${sponsorship.isDeactivated}">

					</jstl:out></td>

			</tr>
			
			
			<tr>

				<td><strong><spring:message
							code="sponsorship.creditCard" /> : </strong></td>
				<td><jstl:out value="${sponsorship.creditCard.number}">

					</jstl:out></td>

			</tr>
			
			<tr>

				<td><strong><spring:message
							code="sponsorship.parade" /> : </strong></td>
				<td><jstl:out value="${sponsorship.parade.title}">

					</jstl:out></td>

			</tr>
			
			<tr>

				<td><strong><spring:message
							code="sponsorship.sponsor" /> : </strong></td>
				<td><jstl:out value="${sponsorship.sponsor.userAccount.username}">

					</jstl:out></td>

			</tr>

		</table>

		<input type="button" name="edit"
			value="<spring:message code="sponsorship.edit"	/>"
			onclick="redirect: location.href = 'sponsorship/edit.do?sponsorshipId=${sponsorship.id}';" />


		<input type="button" name="back"
			value="<spring:message code="sponsorship.cancel" />"
			onclick="window.history.back()" />
			
	</jstl:if>

	<jstl:if test="${!possible}">

	</jstl:if>

</security:authorize>
