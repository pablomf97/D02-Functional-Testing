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

	<form:form modelAttribute="legalRecord" action="legalRecord/edit.do"
		id="form">

		<form:hidden path="id" />

		<acme:textbox code="legalRecord.title" path="title" />
		<br>
		<br>

		<spring:message code="legalRecord.description" />
		<br>
		<form:textarea code="legalRecord.description" path="description" />
		<form:errors cssClass="error" path="description" />

		<br>
		<br>
		<acme:textbox code="legalRecord.name" path="name" />
		<br>
		<br>
		<acme:textbox code="legalRecord.VAT" path="VAT"  placeholder="0.21" />
		<br>
		<br>
		<spring:message code="legalRecord.laws" />
 		:
		<button type="button" onClick="addFields()">
		<spring:message code="add" />
	</button>
	<div id="container"></div>
	<jstl:forEach items="${laws}" var="law">
		<input name=laws value="${law}" />
	</jstl:forEach>
	<form:errors path="laws" cssClass="error" />
	
		<br>
		<br>
		<acme:submit code="legalRecord.save" name="save" />&nbsp;
		<input type="button" name="back"
			value="<spring:message code="legalRecord.cancel" />"
			onclick="window.history.back()" />
		
		<jstl:if test="${legalRecord.id != 0 }">
		<acme:delete name="delete" confirmation="mr.confirm.delete"
			code="legalRecord.delete" />
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
		input.name = "laws";
		container.appendChild(input);
	}
	
	</script>

</security:authorize>