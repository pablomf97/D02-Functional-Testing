<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- Listing grid -->



<display:table pagesize="5" class="displaytag" name="platforms"
	requestURI="platform/list.do" id="row">
	<!-- Attributes-->

	<display:column titleKey="platform.title" sortable="true">
		<jstl:out value="${row.title }"></jstl:out>
	</display:column>

	<display:column titleKey="platform.description" sortable="true">
		<jstl:out value="${row.description }"></jstl:out>
	</display:column>

	<!-- Action links -->

	<display:column>
		<a href="platform/display.do?platformId=${row.id}"> <spring:message
				code="platform.display" />
		</a>
	</display:column>
	<security:authorize access="hasRole('BROTHERHOOD')">
		<display:column>
			<a href="platform/edit.do?platformId=${row.id}"> <spring:message
					code="platform.edit" />
			</a>
		</display:column>
	</security:authorize>
</display:table>


<security:authorize access="hasRole('BROTHERHOOD')">
	<p>
		<a href="platform/create.do"><spring:message
				code="platform.create" /></a>
	</p>
</security:authorize>
