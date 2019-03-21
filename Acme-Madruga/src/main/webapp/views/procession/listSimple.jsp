<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

	<!-- Listing grid -->

	<display:table pagesize="5" class="displaytag" name="parades"
		requestURI="procession/listSimple.do?chapterId=${chapter.id}" id="row">


	
		<!-- Attributes-->
				
		<display:column titleKey="procession.title" sortable="true" >
			<jstl:out value="${row.title }"></jstl:out>
		</display:column>
				
		<display:column titleKey="procession.description" >
			<jstl:out value="${row.description }"></jstl:out>
		</display:column>
				
		<display:column titleKey="procession.organisedMoment" sortable="true" >
			<jstl:out value="${row.organisedMoment }"></jstl:out>
		</display:column>
		
		<display:column titleKey="procession.brotherhood"  >
			<jstl:out value="${row.brotherhood.title }"></jstl:out>
		</display:column>
		
	</display:table>
		
	<acme:cancel code="procession.cancel" url="chapter/list.do" />
	