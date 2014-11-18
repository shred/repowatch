<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%--
  Repowatch -- A repository watcher
    (C) 2008 Richard "Shred" Körber
    http://repowatch.shredzone.org/
 -----------------------------------------------------------------------
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
--%>
<%@ include file="/WEB-INF/jsp/fragments/includes.jspf" %>
<%@ page import="org.shredzone.repowatch.web.util.Sequencer" %>
<% pageContext.setAttribute("sequence", new Sequencer("gridodd", "grideven")); %>

<fmt:message scope="request" var="title" key="admin.title"/>
<c:import url="/WEB-INF/jsp/fragments/header.jspf"/>

<h2><fmt:message key="admin.resynctitle"/></h2>
<table class="grid">
  <tr class="gridhead">
    <th><fmt:message key="admin.repository"/></th>
    <th><fmt:message key="admin.status"/></th>
    <th><fmt:message key="admin.time"/></th>
  </tr>
  <c:forEach var="result" items="${resultList}">
    <tr class="${sequence.next}">
      <td><c:out value="${result.repository.domain.name} ${result.repository.domain.release} ${result.repository.name} (${result.repository.architecture})"/></td>
      <td><c:if test="${not empty result.exception}">
        <div class="resyncbad">
          <strong><c:out value="${result.exception.message}"/></strong><br />
          <c:if test="${not empty result.exception.cause}">
            <c:out value="${result.exception.cause['class'].name}: ${result.exception.cause.message}"/>
          </c:if>
        </div>
      </c:if><c:if test="${empty result.exception}">
        <div class="resyncgood"><fmt:message key="admin.resyncgood"/></div>
      </c:if></td>
      <td style="text-align: right"><c:out value="${result.requiredTime} ms"/></td>
    </tr>
  </c:forEach>
</table>

<c:import url="/WEB-INF/jsp/fragments/footer.jspf"/>