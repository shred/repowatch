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
 
  $Id: listchanges.jsp 317 2009-05-13 21:46:21Z shred $
--%>
<%@ include file="/WEB-INF/jsp/fragments/includes.jspf" %>
<%@ page import="org.shredzone.repowatch.web.util.Sequencer" %>
<% pageContext.setAttribute("sequence", new Sequencer("gridodd", "grideven")); %>

<fmt:message scope="request" var="title" key="chg.title"/>
<c:set scope="request" var="rsslink" value="${rsslink}"/>
<c:import url="/WEB-INF/jsp/fragments/header.jspf"/>

<h2><fmt:message key="chg.changes">
      <fmt:param value="${domain.name} ${domain.release} ${repository.name} (${repository.architecture})"/>
    </fmt:message></h2>
<table class="grid">
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="chg.release"/></td>
    <td><c:out value="${domain.name}"/> <c:out value="${domain.release}"/></td>
  </tr>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="chg.repository"/></td>
    <td><c:out value="${repository.name}"/> (<c:out value="${repository.architecture}"/>)</td>
  </tr>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="chg.baseurl"/></td>
    <td><a href="<c:url value="${repository.baseUrl}"/>" class="ext"><c:out value="${repository.baseUrl}"/></a></td>
  </tr>
  <c:if test="${not empty repository.repoviewUrl}">
    <tr class="gridrow">
      <td class="gridlabel"><fmt:message key="chg.repoviewurl"/></td>
      <td><a href="<c:url value="${repository.repoviewUrl}index.html"/>" class="ext"><c:out value="${repository.repoviewUrl}index.html"/></a></td>
    </tr>
  </c:if>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="chg.since"/></td>
    <td><fmt:formatDate value="${repository.firstScanned}" type="both" dateStyle="full"/></td>
  </tr>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="chg.lastscanned"/></td>
    <td><fmt:formatDate value="${repository.lastScanned}" type="both" dateStyle="full"/></td>
  </tr>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="chg.lastmodified"/></td>
    <td><fmt:formatDate value="${repository.lastModifiedDate}" type="both" dateStyle="full"/></td>
  </tr>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="chg.filter"/></td>
    <td><form style="margin:0px" action="${selfurl}">
	    <input type="checkbox" name="updates" value="1" onchange="form.submit()" <c:if test="${updates}">checked="checked"</c:if> /><fmt:message key="chg.includeupdates"/>
    </form></td>
  </tr>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="chg.rss"/></td>
    <td><a href="<c:url value="/${rsslink}"/>" class="rss"><fmt:message key="chg.subscriberss"/></a></td>
  </tr>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="chg.see"/></td>
    <td><a href="<c:url value="/repo/${domain.name}/${domain.release}/${repository.name}/${repository.architecture}.html"/>">[<fmt:message key="chg.see.packages"/>]</a></td>
  </tr>
</table>

<h2><fmt:message key="chg.changelist"/></h2>
<c:import url="/WEB-INF/jsp/fragments/browser.jspf"/>
<table class="grid">
  <tr class="gridhead">
    <th><fmt:message key="chg.date"/></th>
    <th><fmt:message key="chg.what"/></th>
    <th><fmt:message key="chg.package"/></th>
    <th><fmt:message key="chg.version"/></th>
    <th><fmt:message key="chg.summary"/></th>
  </tr>
  <c:forEach var="change" items="${changeList}">
    <tr class="${sequence.next}">
      <td class="datefmt"><fmt:formatDate value="${change.timestamp}" type="date" dateStyle="full"/></td>
      <c:choose>
        <c:when test="${change.change eq 'ADDED'}"><td class="packadded"><img src="${pageContext.request.contextPath}/img/added.png" width="12" height="12" alt="<fmt:message key="chg.added"/>" title="<fmt:message key="chg.added"/>" /></td></c:when>
        <c:when test="${change.change eq 'REMOVED'}"><td class="packremoved"><img src="${pageContext.request.contextPath}/img/removed.png" width="12" height="12" alt="<fmt:message key="chg.removed"/>" title="<fmt:message key="chg.removed"/>" /></td></c:when>
        <c:otherwise><td class="packupdated"><img src="${pageContext.request.contextPath}/img/updated.png" width="12" height="12" alt="<fmt:message key="chg.updated"/>" title="<fmt:message key="chg.updated"/>" /></td></c:otherwise>
      </c:choose>
      <td><a href="<c:url value="/package/${domain.name}/${domain.release}/${change.package.name}.html"/>"><c:out value="${change.package.name}"/></a></td>
      <td><c:out value="${change.ver}"/>-<c:out value="${change.rel}"/></td>
      <td><c:out value="${change.package.summary}"/></td>
    </tr>
  </c:forEach>
</table>

<c:import url="/WEB-INF/jsp/fragments/footer.jspf"/>