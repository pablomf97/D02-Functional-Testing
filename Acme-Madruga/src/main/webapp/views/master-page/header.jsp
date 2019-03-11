<%--
 * header.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<div style="float: right">
	<a href="?language=es" id="es"><img src="images/sp.png"
		style="width: 50px; height: 25px" /></a> <a href="?language=en" id="en"><img
		src="images/uk.png" style="width: 50px; height: 25px" /></a>
</div>
<div>
	<a href="#"><img style="height: 255px; padding-bottom: 12px"
		src="${banner}" alt="Acme Madruga Co., Inc." /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMINISTRATOR')">
			<li><a class="fNiv"><spring:message
						code="master.page.administrator.creations" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="administrator/edit.do?id=0"><spring:message
								code="master.page.administrator.register" /></a></li>

				</ul></li>

			<li><a class="fNiv"><spring:message
						code="master.page.administrator.positions" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="position/administrator/list.do"><spring:message
								code="master.page.administrator.positions.show" /></a></li>
					<li><a href="position/administrator/create.do"><spring:message
								code="master.page.administrator.position.new" /></a></li>
				</ul></li>

			<li><a class="fNiv" href="statistics/administrator/display.do"><spring:message
						code="master.page.administrator.dashboard" /></a></li>

			<li><a class="fNiv"><spring:message
						code="master.page.administrator.zone" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="zone/administrator/list.do"><spring:message
								code="master.page.administrator.zones.show" /></a></li>
					<li><a href="zone/administrator/create.do"><spring:message
								code="master.page.administrator.zone.new" /></a></li>
				</ul></li>

			<li><a class="fNiv"><spring:message
						code="master.page.brotherhood.system" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="sysconfig/administrator/display.do"><spring:message
								code="master.page.brotherhood.sysconfig.manage" /></a></li>
				</ul></li>

		</security:authorize>


		<security:authorize access="hasRole('MEMBER')">
			<li><a class="fNiv"><spring:message
						code="master.page.member" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="enrolment/member/list.do"><spring:message
								code="master.page.member.enrollments" /></a></li>
				</ul></li>


			<li><a class="fNiv"><spring:message
						code="master.page.member.procession" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="procession/member,brotherhood/list.do"><spring:message
								code="master.page.member.procession.show" /></a></li>

				</ul></li>


			<li><a class="fNiv" href="march/member,brotherhood/list.do"><spring:message
						code="master.page.member.march" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="march/member,brotherhood/list.do"><spring:message
								code="master.page.member.march.show" /></a></li>
					<li><a href="march/create.do"><spring:message
								code="master.page.member.march.new" /></a></li>
				</ul></li>

			<li><a class="fNiv" href="finder/member/search.do"><spring:message
						code="master.page.member.finder" /></a></li>


		</security:authorize>

		<security:authorize access="hasRole('BROTHERHOOD')">
			<li><a class="fNiv"><spring:message
						code="master.page.brotherhood.enrolments" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="enrolment/brotherhood/list.do"><spring:message
								code="master.page.brotherhood.enrolments.manage" /></a></li>
				</ul></li>

			<li><a class="fNiv"><spring:message
						code="master.page.brotherhood.platform" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="platform/list.do"><spring:message
								code="master.page.brotherhood.platform.show" /></a></li>
					<li><a href="platform/create.do"><spring:message
								code="master.page.brotherhood.platform.new" /></a></li>
				</ul></li>

			<li><a class="fNiv"><spring:message
						code="master.page.member.procession" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="procession/member,brotherhood/list.do"><spring:message
								code="master.page.member.procession.show" /></a></li>
					<li><a href="procession/create.do"><spring:message
								code="master.page.member.procession.new" /></a></li>
				</ul></li>

			<li><a class="fNiv" href="march/member,brotherhood/list.do"><spring:message
						code="master.page.member.march" /></a></li>

		</security:authorize>

		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message
						code="master.page.login" /></a></li>

			<li><a class="fNiv"><spring:message
						code="master.page.signup" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="member/edit.do?id=0"><spring:message
								code="master.page.register.member" /></a></li>
					<li><a href="brotherhood/edit.do?id=0"><spring:message
								code="master.page.register.brotherhood" /></a></li>
				</ul></li>

			<li><a class="fNiv" href="brotherhood/list.do"><spring:message
						code="master.page.administrator.brotherhoods" /></a>
					<ul>
						<li class="arrow"></li>
						<li><a href="brotherhood/members/list.do"><spring:message
								code="master.page.brotherhood.members.list" /></a></li>
					</ul></li>

		</security:authorize>

		<security:authorize access="isAuthenticated()">

			<li><a class="fNiv" href="brotherhood/list.do"><spring:message
						code="master.page.administrator.brotherhoods" /></a>
					<ul>
						<li class="arrow"></li>
						<li><a href="brotherhood/members/list.do"><spring:message
								code="master.page.brotherhood.members.list" /></a></li>
					</ul></li>
						

			<li><a class="fNiv"> <spring:message
						code="master.page.profile" /> (<security:authentication
						property="principal.username" />)
			</a>
				<ul>
					<li class="arrow"></li>
					<li><a href="actor/display.do"><spring:message
								code="master.page.profile.view" /></a></li>
					<li><a href="actor/edit.do"><spring:message
								code="master.page.profile.edit" /></a></li>
					<li><a href="box/actor/list.do"><spring:message
								code="master.page.profile.message.boxes" /></a></li>
					<li><a href="messagebox/create.do"><spring:message
								code="master.page.box.new" /></a></li>
					<li><a href="j_spring_security_logout"><spring:message
								code="master.page.logout" /> </a></li>
				</ul></li>

		</security:authorize>
	</ul>
</div>
	
		<security:authorize access="isAuthenticated()">
		<jstl:if test="${pageContext.response.locale.language == 'es'}">
		<h2><strong style="color:red;"><jstl:out value="${breachNotification.get('Español')}"></jstl:out><br/> </strong></h2>
		</jstl:if>
		<jstl:if test="${pageContext.response.locale.language == 'en'}">
			<h2><strong style="color:red;"> <jstl:out value="${breachNotification.get('English')}" ></jstl:out><br/> </strong></h2>
		</jstl:if>
	</security:authorize>
