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
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>


<h2>
	<spring:message code="profile.seeing" />
	<jstl:out value="${username }" />
</h2>

<img src="${administrator.photo }" height="250"
	alt="<spring:message code="profile.photo" /> <jstl:out value="${administrator.userAccount.username }"/>" />
<p>
	<b><spring:message code="profile.name" /></b>:
	<jstl:out value="${administrator.surname }" />
	,
	<jstl:out value="${administrator.name }" />
	<jstl:out value="${administrator.middleName }" />
</p>
<p>
	<b><spring:message code="profile.email" /></b> :
	<jstl:out value="${administrator.email }" />
</p>
<p>
	<b><spring:message code="profile.phone" /></b>:
	<jstl:out value="${administrator.phoneNumber }" />
</p>
<p>
	<b><spring:message code="profile.address" /></b>:
	<jstl:out value="${administrator.address }" />
</p>

<security:authorize access="hasRole('ADMINISTRATOR')">
	<b><spring:message code="profile.score" /></b>:

		<jstl:if test="${administrator.score != null}">
			<fmt:formatNumber type = "number" maxFractionDigits = "2" value = "${administrator.score}" var= "ok"/>
			<jstl:out value="${ok} "/>
		</jstl:if>
	<p>
		<jstl:if test="${administrator.score == null}">
			<jstl:out value="N/A" />
		</jstl:if>
	</p>

	<p>
		<b><spring:message code="profile.spammer" /></b>:
		<jstl:choose>
			<jstl:when test="${administrator.spammer == null}">
				<jstl:out value="N/A" />	
			</jstl:when>
			<jstl:when test="${administrator.spammer == true}">
				<spring:message code="spammer.true" />
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="spammer.false" />
			</jstl:otherwise>
		
		</jstl:choose>
	<p>
	
	<jstl:if test="${isPrincipal}">
	<input type="button" name="flagSpammers"
		value="<spring:message code="admin.flag.spammers" />"
		onclick="redirect: location.href = 'administrator/flag-spammers.do';" />
		
		<input type="button" name="computeSpammers"
		value="<spring:message code="admin.compute.score" />"
		onclick="redirect: location.href = 'administrator/compute-scores.do';" />		
	</jstl:if>
</security:authorize>

