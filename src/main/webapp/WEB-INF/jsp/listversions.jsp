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
 
  $Id: listversions.jsp 278 2009-03-04 00:23:03Z shred $
--%>
<%@ include file="/WEB-INF/jsp/fragments/includes.jspf" %>
<%@ page import="org.shredzone.repowatch.web.util.Sequencer" %>
<% pageContext.setAttribute("sequence", new Sequencer("gridodd", "grideven")); %>

<fmt:message scope="request" var="title" key="vers.title"/>
<c:import url="/WEB-INF/jsp/fragments/header.jspf"/>

<h2><c:out value="${domain.name}"/> <c:out value="${domain.release}"/> <c:out value="${repository.name}"/> (<c:out value="${repository.architecture}"/>)</h2>
<table class="grid">
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="vers.release"/></td>
    <td><c:out value="${domain.name}"/> <c:out value="${domain.release}"/></td>
  </tr>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="vers.repository"/></td>
    <td><c:out value="${repository.name}"/> (<c:out value="${repository.architecture}"/>)</td>
  </tr>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="vers.baseurl"/></td>
    <td><a href="<c:url value="${repository.baseUrl}"/>" class="ext"><c:out value="${repository.baseUrl}"/></a></td>
  </tr>
  <c:if test="${not empty repository.repoviewUrl}">
    <tr class="gridrow">
      <td class="gridlabel"><fmt:message key="vers.repoviewurl"/></td>
      <td><a href="<c:url value="${repository.repoviewUrl}index.html"/>" class="ext"><c:out value="${repository.repoviewUrl}index.html"/></a></td>
    </tr>
  </c:if>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="vers.since"/></td>
    <td><fmt:formatDate value="${repository.firstScanned}" type="both" dateStyle="full"/></td>
  </tr>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="vers.lastscanned"/></td>
    <td><fmt:formatDate value="${repository.lastScanned}" type="both" dateStyle="full"/></td>
  </tr>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="vers.lastmodified"/></td>
    <td><fmt:formatDate value="${repository.lastModifiedDate}" type="both" dateStyle="full"/></td>
  </tr>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="vers.see"/></td>
    <td><a href="<c:url value="/changes/${domain.name}/${domain.release}/${repository.name}/${repository.architecture}.html"/>">[<fmt:message key="vers.see.changes"/>]</a></td>
  </tr>
</table>

<h2><fmt:message key="vers.packages"/></h2>
<c:import url="/WEB-INF/jsp/fragments/browser.jspf"/>
<table class="grid">
  <tr class="gridhead">
    <th><fmt:message key="vers.name"/></th>
    <th><fmt:message key="vers.version"/></th>
    <th><fmt:message key="vers.summary"/></th>
  </tr>
  <c:forEach var="version" items="${versionList}">
    <tr class="${sequence.next}">
      <td><a href="<c:url value="/package/${domain.name}/${domain.release}/${version.package.name}.html"/>"><c:out value="${version.package.name}"/></a></td>
      <td><c:if test="${not empty version.repository.repoviewUrl}">
        <a href="<c:out value="${version.repository.repoviewUrl}"/><c:out value="${version.package.name}"/>.html" title="<fmt:message key="vers.showrepoview"/>"><img src="<c:url value="/img/info.png"/>" width="14" height="14" alt="repoview" border="0" /></a>
      </c:if><c:out value="${version.ver}"/>-<c:out value="${version.rel}"/></td>
      <td><c:out value="${version.package.summary}"/></td>
    </tr>
  </c:forEach>
</table>

<c:import url="/WEB-INF/jsp/fragments/footer.jspf"/>