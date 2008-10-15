<%--
  Repowatch -- A repository watcher
    (C) 2008 Richard "Shred" K�rber
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
 
  $Id: searchresult.jsp 181 2008-07-22 11:35:11Z shred $
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
  <c:forEach var="package" items="${packageList}">
    <tr class="${sequence.next}">
      <td><c:out value="${package.domain.name}"/> <c:out value="${package.domain.release}"/></td>
      <td><a href="<c:url value="/package/${package.domain.name}/${package.domain.release}/${package.name}.html"/>"><c:out value="${package.name}"/></a></td>
      <td><c:out value="${package.summary}"/></td>
    </tr>
  </c:forEach>
</table>

<%@ include file="/WEB-INF/jsp/fragments/footer.jspf" %>