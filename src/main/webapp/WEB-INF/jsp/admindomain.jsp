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
 
  $Id: admindomain.jsp 324 2009-05-16 11:05:23Z shred $
--%>
<%@ include file="/WEB-INF/jsp/fragments/includes.jspf" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<fmt:message scope="request" var="title" key="admin.title"/>
<c:import url="/WEB-INF/jsp/fragments/header.jspf"/>

<form:form commandName="domain">
  <table>
    <tr>
      <td><fmt:message key="admin.name"/>:</td>
      <td><form:input path="name" /></td>
      <td><form:errors path="name" /></td>
    </tr>
    <tr>
      <td><fmt:message key="admin.release"/>:</td>
      <td><form:input path="release" /></td>
      <td><form:errors path="release" /></td>
    </tr>
    <tr>
      <td><fmt:message key="admin.homeurl"/>:</td>
      <td><form:input path="homeUrl" /></td>
      <td><form:errors path="homeUrl" /></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td><input type="submit" value="<fmt:message key="admin.submit"/>" /></td>
      <td><form:errors path="*" /></td>
    </tr>
  </table>
</form:form>

<c:import url="/WEB-INF/jsp/fragments/footer.jspf"/>
