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

	<form:form modelAttribute="periodRecord"
		action="periodRecord/edit.do" id="form">

		<form:hidden path="id" />

		<acme:textbox code="periodRecord.title" path="title" />
		<br>
		<br>

		<spring:message code="periodRecord.description" />
		<br>
		<form:textarea code="periodRecord.description" path="description" />
		<form:errors path="description" cssClass="error" />
		<br>
		<br>
		<acme:textbox code="periodRecord.startYear" path="startYear"  placeholder="2015" />
		<br>
		<br>
		<acme:textbox code="periodRecord.endYear" path="endYear"  placeholder="2020" />
		<br>
		<br>
		<spring:message code="periodRecord.photos" />
		
 		:
		<button type="button" onClick="addFields()">
			<spring:message code="add" />
		</button>
		<div id="container"></div>
		<jstl:forEach items="${photos}" var="pic">
			<input name=photos value="${pic}" placeholder="https://www.imgur.com/f3odiyg"/>
		</jstl:forEach>
		<form:errors path="photos" cssClass="error" />
		<jstl:if test="${not empty s }">
		<a class="error"><spring:message code = "${s}"/></a>
		</jstl:if>
		<br>
		<br>
		<acme:submit code="periodRecord.save" name="save" />&nbsp;
		<input type="button" name="back"
			value="<spring:message code="periodRecord.cancel" />"
			onclick="window.history.back()" />

		&nbsp;
			<jstl:if test="${periodRecord.id != 0 }">
		<acme:delete name="delete" confirmation="mr.confirm.delete"
			code="legalRecord.delete" />&nbsp;
		<br />
		<br />
		</jstl:if>


	</form:form>
	<script>
		function addFields() {
			// Container <div> where dynamic content will be placed
			var container = document.getElementById("container");
			// Create an <input> element, set its type and name attributes
			var input = document.createElement("input");
			input.type = "text";
			input.name = "photos";
			container.appendChild(input);
		}
	</script>

</security:authorize>