<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "/main/javainit.jsp" %>
<%  
  String url = request.getParameter("menu");
  if (url != null && url.length() > 0) {
    boolean cek = true;
  }  
  String titlemenu = request.getParameter("titlemenu");
%>

<!DOCTYPE html>
<html>
  <head>
    <%@ include file = "/styles/lte_head.jsp" %>
    <title>Dimata - ProChain POS</title>
  </head>
  <body class="flexbox" style="background-color: rgb(245, 247, 255) !important;">
    <section class="content-header">
      <h1>
        <%=titlemenu%>
        <small></small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
        <li class="active">Widgets</li>
      </ol>
    </section>
    <div class="content">
      <%if (url != null && url.length() > 0 && url.equalsIgnoreCase("home")) {%>
        <%
            response.sendRedirect("../home_interactive.jsp");
        %>
      <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("chart")) {%>
        <%
            response.sendRedirect("../dashboard.jsp");
        %>
      <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("penjualan")) {%>
        <%@ include file="/menuapp/list_penjualan.jsp"%>
      <% } else if (url != null && url.length() > 0 && url.equalsIgnoreCase("piutang")) {%>
        <%@ include file="/menuapp/list_piutang.jsp"%>
      <% } else if (url != null && url.length() > 0 && url.equalsIgnoreCase("pembelian")) {%>
        <%@ include file="/menuapp/list_pembelian.jsp"%>
      <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("penerimaan")) {%>
        <%@ include file="/menuapp/list_penerimaan.jsp"%>
      <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("return")) {%>
        <%@ include file="/menuapp/list_return.jsp"%>
      <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("transfer")) {%>
        <%@ include file="/menuapp/list_transfer.jsp"%>
      <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("pembiayaan")) {%>
        <%@ include file="/menuapp/list_pembiayaan.jsp"%>
      <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("produksi")) {%>
        <%@ include file="/menuapp/list_produksi.jsp"%>
      <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("stock")) {%>
        <%@ include file="/menuapp/list_stock.jsp"%>
      <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("masterdata")) {%>
        <%@ include file="/menuapp/list_masterdata.jsp"%>
      <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("sistem")) {%>
        <%@ include file="/menuapp/list_sistem.jsp"%>
      <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("keluar")) {%>
      <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("help")) {%>
        <%@ include file="/menuapp/list_help.jsp"%>
      <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("kasir")) {%>
        <%@ include file="/menuapp/list_kasir.jsp"%>
      <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("promosi")) {%>
        <%@ include file="/menuapp/list_promosi.jsp"%>
      <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("cashless")) {%>
        <%@ include file="/menuapp/list_cashless.jsp"%>
      <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("beacukai")) {%>
        <%@include file="/menuapp/list_beacukai.jsp"%>
      <%} else {%>
        Tidak ada modul yang anda cari
      <%}%>
  </body>
</html>
