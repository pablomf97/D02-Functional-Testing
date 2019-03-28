<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- Listing grid -->

<display:table pagesize="5" class="displaytag" name="members"
	requestURI="brotherhood/members/list.do" id="member">
	<!-- Attributes-->

	<display:column titleKey="member.name">
		<jstl:out value="${member.name}" />
	</display:column>

	<display:column titleKey="member.email">
		<jstl:out value="${member.email}" />
	</display:column>

	<display:column titleKey="member.userAccount">
		<jstl:out value="${member.userAccount.username}" />
	</display:column>
	
	<!-- Action links -->


	<display:column>

		<a href="member/display.do?id=${member.id}"> <spring:message
				code="member.show" />
		</a>

	</display:column>

</display:table>

