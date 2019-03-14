<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<security:authorize access="hasRole('BROTHERHOOD')">

	<form:form modelAttribute = "miscellaneousRecord" action="miscellaneousRecord/edit.do"
		id="form">
		
		<form:hidden path="id"/>
		
		<acme:textbox code="miscellaneousRecord.title" path="title"/><br><br>
		
		<spring:message code="miscellaneousRecord.description" /> <br>
		<form:textarea  code="miscallenousRecord.description" path="description"/>
		
		<br>
		<br>
		
		<acme:submit code="miscellaneousRecord.save" name = "save"/>&nbsp;
		
		<acme:cancel url="miscellaneousRecord/list.do?historyId=${historyId}" code="miscellaneousRecord.cancel"/>
		
		<acme:delete name="delete" confirmation="mr.confirm.delete" code="miscellaneousRecord.delete"/>
		<br/>
		<br/>
		
</form:form>

</security:authorize>