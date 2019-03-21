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


<display:table pagesize="5" class="displaytag" name="brotherhoods"
	requestURI="brotherhood/listSimple.do?chapterId=${chapter.id}"
	id="brotherhood">
	<!-- Attributes-->

	<display:column titleKey="brotherhood.title" sortable="true">
		<jstl:out value="${brotherhood.title}" />
	</display:column>

	<display:column titleKey="brotherhood.establishmentDate">
		<jstl:out value="${brotherhood.establishmentDate}" />
	</display:column>



</display:table>

<acme:cancel code="actor.cancel" url="chapter/list.do" />

