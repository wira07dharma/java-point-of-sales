<%@page import="com.dimata.common.entity.system.PstSystemProperty"%>
<ul class="sidebar-menu">
  <%--<li class="header">MAIN NAVIGATION</li>--%>
  <li class="treeview">
    <a data-for="frame-menu" class="i-link">
      <i class="fa fa-home"></i> <span>Home</span>
      <span class="pull-right-container">
        <i class="fa fa-angle-left pull-right"></i>
      </span>
    </a>
    <ul class="treeview-menu">
      <li><a href="<%=approot%>/menuapp/menu_page.jsp?menu=chart&titlemenu=Dashboard" class="i-link" data-for="frame-menu" ><i class="fa fa-circle-o"></i> Dashboard Chart</a></li>  
      <li><a href="<%=approot%>/menuapp/menu_page.jsp?menu=home&titlemenu=Dashboard" class="i-link" data-for="frame-menu" ><i class="fa fa-circle-o"></i> Dashboar Document</a></li>
    </ul>
  </li>
  <% 
	String showCashless = PstSystemProperty.getValueByName("USE_CASHLESS_MODULE");
	if (showCashless.equals("1")){
  %>
  <li><a href="<%=approot%>/menuapp/menu_page.jsp?menu=cashless&titlemenu=Cashless" data-for="frame-menu" class="i-link"><i class="fa fa-dollar"></i> <span><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Cashless" : "Cashless"%></span></a></li>
  <%
	}
  %>
  <%if(PstSystemProperty.getValueByName("ENABLE_DUTY_FREE").equals("1")) {%>
  <li><a href="<%=approot%>/menuapp/menu_page.jsp?menu=beacukai&titlemenu=Bea Cukai Customs" data-for="frame-menu" class="i-link"><i class="fa fa-building"></i> <span><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Bea Cukai Customs" : "Bea Cukai Customs"%></span></a></li>
  <%}%>
  <li><a href="<%=approot%>/menuapp/menu_page.jsp?menu=masterdata&titlemenu=Master Data" data-for="frame-menu" class="i-link"><i class="fa fa-database"></i> <span><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Master Data" : "Data Master"%></span></a></li>
  <li><a href="<%=approot%>/menuapp/menu_page.jsp?menu=pembelian&titlemenu=Pembelian" data-for="frame-menu" class="i-link"><i class="fa fa-cart-plus"></i> <span><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pembelian" : "Purchasing"%></span></a></li>
  <li><a href="<%=approot%>/menuapp/menu_page.jsp?menu=penerimaan&titlemenu=Penerimaan" data-for="frame-menu" class="i-link"><i class="fa fa-download"></i> <span><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Penerimaan" : "Receiving"%></span></a></li>
  <li><a href="<%=approot%>/menuapp/menu_page.jsp?menu=return&titlemenu=Return" data-for="frame-menu" class="i-link"><i class="fa fa-refresh"></i> <span><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Return" : "Return"%></span></a></li>
  <li><a href="<%=approot%>/menuapp/menu_page.jsp?menu=transfer&titlemenu=Transfer" data-for="frame-menu" class="i-link"><i class="fa fa-upload"></i> <span><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Transfer" : "Transfer"%></span></a></li>
  <li><a href="<%=approot%>/menuapp/menu_page.jsp?menu=pembiayaan&titlemenu=Pembiayaan" data-for="frame-menu" class="i-link"><i class="fa fa-money"></i> <span><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pembiayaan" : "Costing"%></span></a></li>
  <% if (typeOfBusinessDetail != 2) {%>
  <li><a href="<%=approot%>/menuapp/menu_page.jsp?menu=produksi&titlemenu=Produksi" data-for="frame-menu" class="i-link"><i class="fa fa-industry"></i> <span><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Produksi" : "Production"%></span></a></li>
  <%}%>
  <li><a href="<%=approot%>/menuapp/menu_page.jsp?menu=stock&titlemenu=Stock" data-for="frame-menu" class="i-link"><i class="fa fa-cubes"></i> <span><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Stok" : "Stock"%></span></a></li>
  <li><a href="<%=approot%>/menuapp/menu_page.jsp?menu=promosi&titlemenu=Promosi" data-for="frame-menu" class="i-link"><i class="fa fa-tags"></i> <span><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Promosi" : "Promotions"%></span></a></li>
  <li><a href="<%=approot%>/menuapp/menu_page.jsp?menu=penjualan&titlemenu=Laporan Penjualan" data-for="frame-menu" class="i-link"><i class="fa fa-print"></i> <span><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Penjualan" : "Sales Report"%></span></a></li>
  <% if (useForRaditya.equals("0")) {%>
  <li><a href="<%=approot%>/menuapp/menu_page.jsp?menu=piutang&titlemenu=Laporan Piutang" data-for="frame-menu" class="i-link"><i class="fa fa-print"></i> <span> <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Piutang" : "AR Report"%><span></span></a></li>
  <% }%>
  <li><a href="<%=approot%>/menuapp/menu_page.jsp?menu=sistem&titlemenu=Sistem" data-for="frame-menu" class="i-link"><i class="fa fa-server"></i> <span> <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Sistem" : "System"%></span></a></li>
</ul>