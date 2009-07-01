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
 
  $Id: adminindex.jsp 342 2009-07-01 22:11:38Z shred $
--%>
<%@ include file="/WEB-INF/jsp/fragments/includes.jspf" %>
<%@ page import="org.shredzone.repowatch.web.util.Sequencer" %>
<% pageContext.setAttribute("sequence", new Sequencer("gridodd", "grideven")); %>

<fmt:message scope="request" var="title" key="admin.title"/>
<c:import url="/WEB-INF/jsp/fragments/header.jspf"/>

<h2><fmt:message key="admin.repositories"/></h2>
<table class="grid">
  <tr class="gridhead">
    <th><fmt:message key="admin.release"/></th>
    <th><fmt:message key="admin.repository"/></th>
    <th>&nbsp;</th>
  </tr>
  <c:forEach var="domain" items="${domainList}">
    <tr class="gridhilite">
      <td><c:out value="${domain.name} ${domain.release}"/></td>
      <td>&nbsp;</td>
      <td><a href="<c:url value="/admin/domain/edit/${domain.id}.html"/>"><fmt:message key="admin.edit"/></a>
        | <a href="<c:url value="/admin/domain/delete/${domain.id}.html"/>"><fmt:message key="admin.delete"/></a>
        | <a href="<c:url value="/admin/resync/domain/${domain.id}.html"/>"><fmt:message key="admin.resync"/></a></td>
    </tr>
    <c:forEach var="repo" items="${repoMap[domain]}">
      <tr class="${sequence.next}">
        <td>&nbsp;</td>
        <td><c:out value="${repo.name} (${repo.architecture})"/></td>
        <td><a href="<c:url value="/admin/repo/edit/${repo.id}.html"/>"><fmt:message key="admin.edit"/></a>
          | <a href="<c:url value="/admin/repo/delete/${repo.id}.html"/>"><fmt:message key="admin.delete"/></a>
          | <a href="<c:url value="/admin/resync/repo/${repo.id}.html"/>"><fmt:message key="admin.resync"/></a></td>
      </tr>
    </c:forEach>
    <tr class="${sequence.next}">
      <td>&nbsp;</td>
      <td><a href="<c:url value="/admin/repo/add/${domain.id}.html"/>"><fmt:message key="admin.add"/></a></td>
      <td>&nbsp;</td>
    </tr>
  </c:forEach>
  <tr class="gridhead">
    <td><a href="<c:url value="/admin/domain/add.html"/>"><fmt:message key="admin.add"/></a></td>
    <td>&nbsp;</td>
    <td><a href="<c:url value="/admin/resync/all.html"/>"><fmt:message key="admin.resyncall"/></a></td>
  </tr>
</table>

<c:import url="/WEB-INF/jsp/fragments/footer.jspf"/>