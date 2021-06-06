<!DOCTYPE html>
<html>
  <head>
    <%@ include file = "/main/javainit.jsp" %>
    <%@ include file = "/styles/lte_head.jsp" %>
    <title>Dimata - ProChain POS</title>
  </head>
  <%--<body class="hold-transition skin-orange sidebar-collapse sidebar-mini">--%>
  <body class="hold-transition skin-orange sidebar-mini">
    <div class="wrapper">
      <% /* Header page and sidebar menu. */ %>
      <%@ include file = "/main/header.jsp" %>
      <% /* /.sidebar %>

      <% /* Content Wrapper. Contains page content */ %>
      <div class="content-wrapper">
        <iframe id="frame-menu" class="app-frame" onload="resizeFrame(this)" style="display: none;" src="dashboard.jsp" ></iframe>
        <% /* /.content */ %>
      </div>
      <% /* /.content-wrapper */ %>
      <%@ include file = "/main/footer_boostrap.jsp" %>
	  <%@ include file = "versions.jsp" %>
    </div>
	<script>
          function showVersion() {
          $("#modal-default").modal("show");
          }
	</script>
    <% /* ./wrapper */ %>
  </body>
</html>
