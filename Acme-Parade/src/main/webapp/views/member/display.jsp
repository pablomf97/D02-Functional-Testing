<%--
 * action-2.jsp
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

<h2>
	<spring:message code="profile.seeing" />
	<jstl:out value="${username }" />
</h2>

<img src="${member.photo }" height="250"
	alt="<spring:message code="profile.photo" /> <jstl:out value="${member.userAccount.username }"/>" />
<p>
	<b><spring:message code="profile.name" /></b>:
	<jstl:out value="${member.surname }" />
	,
	<jstl:out value="${member.name }" />
	<jstl:out value="${member.middleName }" />
</p>
<p>
	<b><spring:message code="profile.email" /></b> :
	<jstl:out value="${member.email }" />
</p>
<p>
	<b><spring:message code="profile.phone" /></b>:
	<jstl:out value="${member.phoneNumber }" />
</p>
<p>
	<b><spring:message code="profile.address" /></b>:
	<jstl:out value="${member.address }" />
</p>

<table class="displayStyle">
	<tr>
		<td><display:table pagesize="5" class="displaytag"
				name="brotherhoods" requestURI="member/display.do" id="brotherhoods">

				<display:column titleKey="actor.brotherhoods" sortable="true">
					<jstl:out value="${brotherhoods.title}" />
				</display:column>

			</display:table></td>
</table>
	<security:authorize access="isAuthenticated()">

		<%
			String name = (String) pageContext.getAttribute("user", PageContext.SESSION_SCOPE);
		%>
		<jstl:if test=" ${name == member.userAccount.username}">
<div><a href="member/export.do"><spring:message
								code="export" /></a></div>

		</jstl:if>
	</security:authorize>

								
<security:authorize access="hasRole('ADMINISTRATOR')">
	<p>
		<b><spring:message code="profile.score" /></b>:
		<jstl:out value="${member.score }" />
	</p>
	<p>
		<b><spring:message code="profile.spammer" /></b>:
		<jstl:out value="${member.spammer }" />
	</p>
</security:authorize>

