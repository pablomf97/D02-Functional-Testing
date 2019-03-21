<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<security:authorize access="hasRole('BROTHERHOOD')">

	<jstl:if test="${linkRecord.id == 0}">

		<form:form modelAttribute="linkRecord" action="linkRecord/edit.do"
			id="form">

			<form:hidden path="id" />

			<acme:textbox code="linkRecord.title" path="title" />
			<br>
			<br>

			<spring:message code="linkRecord.description" />
			<br>
			<form:textarea code="linkRecord.description" path="description" />
			<br>
			<br>
			<form:label path="linkedBrotherhood">
				<spring:message code="linkRecord.brotherhood.title" /> :
		</form:label>
			<form:select path="linkedBrotherhood">
				<form:option label="-----" value="0" />
				<form:options items="${brotherhoods}" itemLabel="title"
					itemValue="id" />
					
			</form:select>
			<form:errors cssClass="error" path="linkedBrotherhood" />
			<br>
			<br>

			<acme:submit code="linkRecord.save" name="save" />&nbsp;
		
		<acme:cancel url="linkRecord/list.do?historyId=${historyId}"
				code="linkRecord.cancel" />

			

		</form:form>


	</jstl:if>

	<jstl:if test="${linkRecord.id != 0}">
		<form:form modelAttribute="linkRecord" action="linkRecord/edit.do"
			id="form">

			<form:hidden path="id" />

			<acme:textbox code="linkRecord.title" path="title" />
			<br>
			<br>

			<spring:message code="linkRecord.description" />
			<br>
			<form:textarea code="linkRecord.description" path="description" />

			<acme:submit code="linkRecord.save" name="save" />&nbsp;
		
		<acme:cancel url="linkRecord/list.do?historyId=${historyId}"
				code="linkRecord.cancel" />

			<acme:delete name="delete" confirmation="lr.confirm.delete"
				code="linkRecord.delete" />
			<br />
			<br />

		</form:form>
	</jstl:if>

</security:authorize>