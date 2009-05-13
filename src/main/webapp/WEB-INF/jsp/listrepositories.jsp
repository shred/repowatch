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
 
  $Id: listrepositories.jsp 317 2009-05-13 21:46:21Z shred $
--%>
<%@ include file="/WEB-INF/jsp/fragments/includes.jspf" %>
<%@ page import="org.shredzone.repowatch.web.util.Sequencer" %>
<% pageContext.setAttribute("sequence", new Sequencer("gridodd", "grideven")); %>

<fmt:message scope="request" var="title" key="repos.title"/>
<c:import url="/WEB-INF/jsp/fragments/header.jspf"/>

<h2><fmt:message key="repos.repositories"/></h2>
<table class="grid">
  <tr class="gridhead">
    <th><fmt:message key="repos.release"/></th>
    <th><fmt:message key="repos.repository"/></th>
    <th><fmt:message key="repos.packages"/></th>
    <th><fmt:message key="repos.lastupdated"/></th>
    <th>&nbsp;</th>
  </tr>
  <c:forEach var="repo" items="${repositoryList}">
    <tr class="${sequence.next}">
      <td><c:out value="${repo.domain.name}"/> <c:out value="${repo.domain.release}"/></td>
      <td><c:out value="${repo.name}"/> (<c:out value="${repo.architecture}"/>)</td>
      <td style="text-align:right"><fmt:formatNumber value="${counterMap[repo]}"/></td>
      <td style="text-align:center"><fmt:formatDate value="${repo.lastModifiedDate}" type="both" dateStyle="full"/></td>
      <td><a href="<c:url value="/repo/${repo.domain.name}/${repo.domain.release}/${repo.name}/${repo.architecture}.html"/>"><fmt:message key="repos.listpackages"/></a>
        | <a href="<c:url value="/changes/${repo.domain.name}/${repo.domain.release}/${repo.name}/${repo.architecture}.html"/>"><fmt:message key="repos.listchanges"/></a>
        | <a href="<c:url value="/rss/${repo.domain.name}/${repo.domain.release}/${repo.name}/${repo.architecture}.rss"/>" class="rss">RSS</a></td>
    </tr>
  </c:forEach>
</table>

<c:import url="/WEB-INF/jsp/fragments/footer.jspf"/>