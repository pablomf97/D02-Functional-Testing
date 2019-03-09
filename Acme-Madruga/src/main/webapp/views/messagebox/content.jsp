<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
		<h2>
			<jstl:out value="${box.name}"></jstl:out>
		</h2>
<div>
	<jstl:if test="${not empty messageCode}">
		<h4>
			<spring:message code="${messageCode}" />
		</h4>
	</jstl:if>
	<h3><spring:message code="messagebox.boxes" /></h3>
	<display:table name="messageBoxes" id="messagebox" requestURI="${requestURI}"
		pagesize="10">
		<display:column>
			<jstl:out value="${messagebox.name}"></jstl:out>
		</display:column>

		<display:column>
			<button
				onClick="window.location.href='messagebox/content.do?Id=${messagebox.id}'">
				<spring:message code="messagebox.seeM" />
			</button>
		</display:column>

		<display:column titleKey="messagebox.edit">
			<jstl:if test="${messagebox.isPredefined == false}">
				<button
					onClick="window.location.href='messagebox/edit.do?Id=${messagebox.id}'">
					<spring:message code="messagebox.edit" />
				</button>
			</jstl:if>
		</display:column>
		<display:column titleKey="messagebox.delete">
			<jstl:if test="${messagebox.isPredefined == false}">
				<button
					onClick="window.location.href='messagebox/delete.do?Id=${messagebox.id}'">
					<spring:message code="messagebox.delete" />
				</button>
			</jstl:if>
		</display:column>

	</display:table>
	<br />
		<h3><spring:message code="message.messages" /></h3>
	
	<display:table name="messages" id="row" pagesize="5"
		requestURI="${requestURI}">
<display:column titleKey="message.subject">
			<jstl:out value="${row.subject}"></jstl:out>
		</display:column>
				<display:column titleKey="message.actor.sender"  >
			<jstl:out value="${row.sender.userAccount.username}"></jstl:out>
		</display:column>
				<display:column titleKey="message.sendTime">
			<jstl:out value="${row.sentMoment}"></jstl:out>
		</display:column>
				<display:column titleKey="message.priority">
			<jstl:out value="${row.priority}"></jstl:out>
		</display:column>
				<display:column titleKey="message.tags">
			<jstl:out value="${row.tags}"></jstl:out>
		</display:column>
		<display:column titleKey="message.move">
			<button
				onClick="window.location.href='message/actor/edit.do?messageId=${row.id}'">
				<spring:message code="message.move" />
			</button>
		</display:column>
				<display:column titleKey="message.display">
			<button
				onClick="window.location.href='message/actor/display.do?messageId=${row.id}'">
				<spring:message code="message.display" />
			</button>
		</display:column>

	</display:table> 

</div>
