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

<fmt:message scope="request" var="title" key="search.title"/>
<c:import url="/WEB-INF/jsp/fragments/header.jspf"/>
<c:import url="/WEB-INF/jsp/fragments/searchmask.jspf"/>

<h2><fmt:message key="search.result"/></h2>
<c:import url="/WEB-INF/jsp/fragments/browser.jspf"/>
<table class="grid">
  <tr class="gridhead">
    <th><fmt:message key="search.release"/></th>
    <th><fmt:message key="search.package"/></th>
    <th><fmt:message key="search.summary"/></th>
  </tr>
  <c:forEach var="pack" items="${packageList}">
    <tr class="${sequence.next}">
      <td><c:out value="${pack.domain.name} ${pack.domain.release}"/></td>
      <td><a href="<c:url value="/package/${pack.domain.name}/${pack.domain.release}/${pack.name}.html"/>"><c:out value="${pack.name}"/></a></td>
      <td><c:out value="${pack.summary}"/></td>
    </tr>
  </c:forEach>
</table>

<%@ include file="/WEB-INF/jsp/fragments/footer.jspf" %>
