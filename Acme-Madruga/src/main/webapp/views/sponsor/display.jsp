
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

<img src="${sponsor.photo }" height="250"
	alt="<spring:message code="profile.photo" /> <jstl:out value="${sponsor.userAccount.username }"/>" />
<p>
	<b><spring:message code="profile.name" /></b>:
	<jstl:out value="${sponsor.surname }" />
	,
	<jstl:out value="${sponsor.name }" />
	<jstl:out value="${sponsor.middleName }" />
</p>
<p>
	<b><spring:message code="profile.email" /></b> :
	<jstl:out value="${sponsor.email }" />
</p>
<p>
	<b><spring:message code="profile.phone" /></b>:
	<jstl:out value="${sponsor.phoneNumber }" />
</p>
<p>
	<b><spring:message code="profile.address" /></b>:
	<jstl:out value="${sponsor.address }" />
</p>
<!-- 

	<b><spring:message code="profile.score" /></b>:
			<p>
		<jstl:if test="${sponsor.score != null}">
			<jstl:out value="${sponsor.score }" />
		</jstl:if>
	<p>
		<jstl:if test="${sponsor.score == null}">
			<jstl:out value="N/A" />
		</jstl:if>
	</p>

	<p>
		<b><spring:message code="profile.spammer" /></b>:
	<p>
		<jstl:if test="${sponsor.score != null}">
			<jstl:out value="${sponsor.spammer }" />
		</jstl:if>
	<p>
		<jstl:if test="${sponsor.spammer == null}">
			<jstl:out value="N/A" />
		</jstl:if>
	</p>
	
	<jstl:if test="${isPrincipal}">
	<input type="button" name="flagSpammers"
		value="<spring:message code="admin.flag.spammers" />"
		onclick="redirect: location.href = 'sponsor/flag-spammers.do';" />
		
		<input type="button" name="flagSpammers"
		value="<spring:message code="admin.compute.score" />"
		onclick="redirect: location.href = 'sponsor/compute-scores.do';" />		
	</jstl:if> -->

