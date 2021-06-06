<%@page import="com.dimata.common.entity.system.PstSystemProperty"%>
<header class="main-header">
  <% /* Logo */ %>
  <a href="" target="_blank" class="logo">
    <% /* mini logo for sidebar mini 50x50 pixels */ %>
    <span class="logo-mini"><b>P</b>RC</span>
    <% /* logo for regular state and mobile devices */ %>
    <span class="logo-lg"><b>Dimata</b> PROCHAIN</span>
  </a>
  <% /* Header Navbar: style can be found in header.less */ %>
  <nav class="navbar navbar-static-top">
    <% /* Sidebar toggle button*/ %>
    <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
      <span class="sr-only">Toggle navigation</span>
    </a>

    <div class="navbar-custom-menu">
      <ul class="nav navbar-nav">
        <% /* Control Sidebar Toggle Button */ %>
        <li>
          <a href="<%=approot%>/logout.jsp" ><i class="fa fa-lock"></i> &nbsp; Logout</a>
        </li>
      </ul>
    </div>
  </nav>
</header>
<% /* Left side column. contains the logo and sidebar */ %>
<aside class="main-sidebar">
  <% /* sidebar: style can be found in sidebar.less */ %>
  <section class="sidebar">
    <% /* Sidebar user panel */ %>
    <div class="user-panel">
      <div>
        <img src="<%=approot%>/images/logo_prochain.jpg" alt="User Image" width="200px">
      </div>
      <%--<div class="pull-left info">
        <p>Login as <%=userName%></p>
        <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
      </div>--%>
    </div>
    <% /* search form */ %>
    <form action="#" method="get" class="sidebar-form">
      <!--div class="input-group">
        <input type="text" name="q" class="form-control" placeholder="Search...">
        <span class="input-group-btn">
          <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
          </button>
        </span>
      </div-->
    </form>
    <% /* /.search form */ %>
    <% /* sidebar menu: : style can be found in sidebar.less */ %>
    <%@ include file = "/menuapp/sidebar.jsp" %>
  </section>
  <% /* /.sidebar */ %>
</aside>