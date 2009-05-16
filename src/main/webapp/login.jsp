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
 
  $Id: admindomain.jsp 317 2009-05-13 21:46:21Z shred $
--%>
<%@ include file="/WEB-INF/jsp/fragments/includes.jspf" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<fmt:message scope="request" var="title" key="login.title"/>
<c:import url="/WEB-INF/jsp/fragments/header.jspf"/>

<c:if test="${not empty param.failed}">
<div class="note"><fmt:message key="login.failed"/></div>
</c:if>

<form action="j_spring_security_check" method="post">
  <table>
    <tr>
      <td><fmt:message key="login.user"/>:</td>
      <td><input type="text" name="j_username" /></td>
    </tr>
    <tr>
      <td><fmt:message key="login.password"/>:</td>
      <td><input type="password" name="j_password" /></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td><input type="checkbox" name="_spring_security_remember_me" /> <fmt:message key="login.rememberme"/></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td><input type="submit" value="<fmt:message key="login.submit"/>" /></td>
    </tr>
  </table>
</form>

<c:import url="/WEB-INF/jsp/fragments/footer.jspf"/>