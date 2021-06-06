<%-- 
    Document   : menu_icon
    Created on : Dec 21, 2013, 11:33:18 AM
    Author     : dimata005
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"df_stock_wh_material_edit.jsp
  "http://www.w3.org/TR/html4/loose.dtd">
<style type="text/css">
  #wrapper { width: 1050px; margin: 0 auto; }
  #wrapperspace { width: 1000px; margin: 0 auto; }
  #wrapper-pembelian { width: 1320px; margin: 0 auto; }
  #wrapper-masterdata { width: 1450px; margin: 0 auto; }
  #column-left { 
    width: 350px; float: left; /*background: red;*/
  }
  #column-center {
    width: 350px; float: left; /*background: yellow;*/
  }
  #column-right {
    width: 350px; float: left; /*background: purple;*/
  }

  #column-left1 {
    width: 262px; float: left; /*background: red;*/
  }
  #column-center2 {
    width: 264px; float: left; /*background: yellow;*/
  }
  #column-right3 {
    width: 262px; float: left; /*background: purple;*/
  }
  #column-right-two4 {
    width: 262px; float: left; /*background: purple;*/
  }
  #column-master {
    width: 230px; float: left; /*background: red;*/
  }
  #column-master1 {
    width: 200px; float: left; /*background: red;*/
  }
  A.menulink:LINK {
    font-family : tahoma, sans-serif;
    font-size : 11px;
    font-weight : bold;
    text-decoration : none;
    color: #000000;
  }
  A.menulink:hover {
    font-family: Tahoma, Arial, sans-serif;
    font-size: 11px;
    font-weight: bold;
    color: #000000;
    text-decoration: none;
  }

  A.menulink:visited {

    font-family : tahoma, sans-serif;
    font-size : 11px;
    font-weight : bold;
    text-decoration : none;
    color: #000000;
  }
</style>
<table  border="0" cellpadding="0" width="100%" border="1">
  <tr>
    <td valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <%--
      <tr>
        <td height="15" class="mainheader">
<!-- #BeginEditable "contenttitle" -->
          <!-- #EndEditable -->
        </td>
      </tr>--%>
        <tr>
          <td>
            <!-- #BeginEditable "content" -->
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <%if (url != null && url.length() > 0 && url.equalsIgnoreCase("home")) {%>

              <%//@ include file="home.jsp"%>
              <%@ include file="home_interactive.jsp"%>

              <%}
                else if (url

                != null && url.length () 
                  > 0 && url.equalsIgnoreCase("penjualan")) {%>
              <div id="wrapperspace">&nbsp
                <br>
              </div>
              <div id="wrapper">
                <div id="column-left">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/1.Laporan.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan" : "Report"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/src_reportsale_global.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Penjualan Global" : "Sales Global"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/src_reportsale_detail_global.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Penjualan Detail" : "Sales Detail"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/src_reportsale_global_margin.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Gross Margin" : "Gross Margin"%> </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/src_reportsalerekaptanggal.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Rekap Harian" : "Daily Summary"%> </font>
                      </b>
                    </a>
                  </div>
                  <%--<div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <a href="<%=approot%>/store/sale/report/src_reportsalerekaptanggal.jsp" class="menulink">
                          <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                          &nbsp;<b>
<font size="3">
<%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Summary Rekap Harian Per Location":"Daily Summary Location "%> </font>
</b>
                      </a>
                  </div>--%>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/src_reportpendingorder.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pending Order" : "Pending Order"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/src_reportsale.jsp?sale_type=6" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Invoice" : "By Invoice"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <% if (!typeOfBusiness.equals("0")) {%>      
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/report_daily_cashier.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Harian Kasir" : "Daily Cashier Report"%> </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/report_top_menu.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Top Menu" : "Top Menu Report"%> </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/report_credit_card.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Kartu Kredit" : "Credit Card Report"%> </font>
                      </b>
                    </a>
                  </div> 
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/report_compliment.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Compliment" : "Compliment Report"%> </font>
                      </b>
                    </a>
                  </div> 
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/report_summary_sales.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Rangkuman Penjualan" : "Summary Sales Report"%> </font>
                      </b>
                    </a>
                  </div>

                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/src_reportsale_spesial_request.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Report Spesial Request" : "Report Spesial Request"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/src_report_void.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Report Void" : "Report Void"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/src_report_err.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Report Error" : "Report Error"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <%}%>
                  <%if (typeOfBusiness.equals("3")) {%>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/src_report_cancel.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Report Cancel" : "Report Cancel"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <%}%>
                </div>
                <div id="column-center">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_empety.png" align="absmiddle">
                      <b>
                        <font size="5">&nbsp;</font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/src_reportsale_detail.jsp?sale_type=6" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">Invoice Detail</font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/src_reportreturn.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">Return Per Invoice</font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/src_reportrekapkategori.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Rekapitulasi Report" : "Rekapitulasi Report"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/src_reportrekap.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Rekap Per KSG" : "Rekap Per KSG"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/src_report_cash_opening.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Cash Opening" : "Per Cash Opening"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/src_reportsale_detail_global_100.jsp?urutan=1" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "100 Item Terlaris" : "100 Best Seller Items"%>
                        </font>
                      </b>
                    </a>
                  </div>    
                  <%if (typeOfBusiness.equals("3")) {%>    
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/src_cashier_sales_sum.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">Cashier Sales Summary</font>
                      </b>
                    </a>
                  </div>
                  <%}%>        
                </div>

                <div id="column-right">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/2.Piutang.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Piutang" : "A/R"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/src_reportsalepaymentcredit.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pelunasan Piutang" : "A/R Payment"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/store/sale/report/src_reportar.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Rekap Piutang" : "A/R Summary"%>
                        </font>
                      </b>
                    </a>
                  </div>
                </div>
              </div>
              <%}
                else if (url

                != null && url.length () 
                  > 0 && url.equalsIgnoreCase("pembelian")) {%>
              <div id="wrapperspace">&nbsp
                <br>
              </div>
              <div id="wrapper-pembelian">
                <%if (privPurchaseRequestWarehouse) {%>
                <div id="column-left">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/3.OrderPembelian.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Request Order" : "Purchase Request"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/purchasing/material/pom/srcprmaterial.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah Request" : "Add/Update Request"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <%if (true) {%>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportstockmin.jsp?typeRequest=1" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Minimum Stok" : "Minimum Stok"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <%}%>   
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;
                  </div>    
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;
                  </div>  
                </div>
                <%}%>      
                <div id="column-left">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/3.OrderPembelian.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Order Pembelian" : "Purchase Order"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/purchasing/material/pom/srcpomaterial.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah Tambah" : "Add/Update"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <%if (privPurchaseRequestWarehouse) {%>    
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/purchasing/material/pom/srcprforpomaterial.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Dengan Purchase Request" : "With Purchase Request"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <%}%>     
                </div>
                <%if (typeOfBusiness.equals("2") || typeOfBusiness.equals("0")) {%>
                <div id="column-center">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/4.stok.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Minimum Stok" : "Minimum Stock"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportstockmin.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah" : "Update/Add"%>
                        </font>
                      </b>
                    </a>
                  </div>
                </div>
                <%}%>
                <div id="column-left1">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/5.hutang.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Hutang" : "A/P"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/arap/payable/payable_search.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Rekap" : "Summary"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/arap/payable/ap_summary_search.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Rekap Detail" : "Detail Summary"%>
                        </font>
                      </b>
                    </a>
                  </div>
                </div>
              </div>
              <% }
                else if (url

                != null && url.length () 
                  > 0 && url.equalsIgnoreCase("penerimaan")) {%>
              <div id="wrapperspace">&nbsp
                <br>
              </div>
              <div id="wrapper">
                <div id="column-left">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/3.PenerimaanPembelian.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Dari Pembelian" : "From PO"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/receive/src_receive_material.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah" : "Update/Add"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan" : "Report"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportreceive_all.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;&nbsp;<b>
                        <font size="2">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Global" : "Global"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportreceiveinvoice.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;&nbsp;<b>
                        <font size="2">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Nota" : "By Invoice"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportreceive.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;&nbsp;<b>
                        <font size="2">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Supplier" : "By Supplier"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportreceivekategori.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;&nbsp;<b>
                        <font size="2">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Kategori" : "By Category"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportreceiveinvoicesummary.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;&nbsp;<b>
                        <font size="2">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Summary Per Nota" : "Summary By Invoice"%>
                        </font>
                      </b>
                    </a>
                  </div>
                </div>
                <div id="column-center">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/6.daritoko.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Dari Toko / Gudang" : "From Store / Warehouse"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/receive/srcreceive_store_wh_material.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah" : "Update/Add"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan" : "Report"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportreceiveinternal.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;&nbsp;<b>
                        <font size="2">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Global" : "Global"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportreceiveinternalinvoice.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;&nbsp;<b>
                        <font size="2">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Nota" : "By Receipt"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportreceiveinternalkategori.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;&nbsp;<b>
                        <font size="2">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Kategori" : "By Category"%>
                        </font>
                      </b>
                    </a>
                  </div>
                </div>
              </div>
              <%}
                else if (url

                != null && url.length () 
                  > 0 && url.equalsIgnoreCase("return")) {%>
              <div id="wrapperspace">&nbsp
                <br>
              </div>
              <div id="wrapper">
                <div id="column-left">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/7.retur.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Return Dari Pembelian" : "Return"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/return/src_return_material.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah" : "Update/Add"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan" : "Report"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportreturninvoice.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;&nbsp;<b>
                        <font size="2">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Invoice" : "By Invoice"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportreturn.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;&nbsp;<b>
                        <font size="2">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Supplier" : "By Supplier"%>
                        </font>
                      </b>
                    </a>
                  </div>
                </div>
                <%if (typeOfBusiness.equals("3")) {%>
                <div id="column-center">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/7.retur.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Return Dari Penjualan" : "Return From Sale"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/conreturn/src_return_from_sale.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah / Tambah" : "Ubah / Tambah"%>
                        </font>
                      </b>
                    </a>
                  </div>
                </div>
                <%}%>
              </div>
              <%}
                else if (url

                != null && url.length () 
                  > 0 && url.equalsIgnoreCase("transfer")) {%>
              <div id="wrapperspace">&nbsp
                <br>
              </div>
              <div id="wrapper">
                <div id="column-left">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/9.transfer.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Transfer" : "Dispatch"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/dispatch/srcdf_stock_wh_material.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah / Tambah" : "Update/Change"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <%if (privApprovalRequestTransfer) {%>    
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/dispatch/srcprfortransferrequest.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Dengan Transfer Request" : "With Dispatch Request"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <%}%>       
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportstockmin_transfer.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Transfer Minimum Stok" : "Transfer With Minimum Stok"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;
                  </div>    
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;
                  </div>
                  <%if (privApprovalRequestTransfer) {%>
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/3.OrderPembelian.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Request Transfer" : "Dispatch Request"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/purchasing/material/pom/srcprtowarehousematerial.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah Request" : "Add/Update Request"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <%}%>    
                </div>
                <div id="column-center">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/transferLaporan.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan" : "Report"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportdispatch.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan" : "Report"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportdispatchinvoice.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Invoice" : "By Invoice"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportdispatchsupplier.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Supplier" : "By Supplier"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportdispatchkategori.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Kategori" : "By Category"%>
                        </font>
                      </b>
                    </a>
                  </div>
                </div>
                <%if ((typeOfBusiness.equals("0") || typeOfBusiness.equals("2") && privApprovalTransferUnit)) {%>
                <div id="column-right">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/transferUnit.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Transfer Unit" : "Unit Dispatch"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/dispatch/srcdf_unit_wh_material.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah" : "Update/Add"%>
                        </font>
                      </b>
                    </a>
                  </div>
                </div>
                <%}%>
              </div>
              <%}
                else if (url

                != null && url.length () 
                  > 0 && url.equalsIgnoreCase("pembiayaan")) {%>
              <div id="wrapperspace">&nbsp
                <br>
              </div>
              <div id="wrapper">
                <div id="column-left">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/pembiayaan.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pembiayaan" : "Costing"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/dispatch/srccosting_material.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah" : "Update/Add"%>
                        </font>
                      </b>
                    </a>
                  </div>
                </div>
                <div id="column-center">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/laporanPembiayaan.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan" : "Report"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportcosting.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Global" : "Global"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportcostingkategori.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Kategori" : "By Category"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportcostinginvoice.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Nota" : "By Receipt"%>
                        </font>
                      </b>
                    </a>
                  </div>
                </div>
              </div>
              <%}
                else if (url

                != null && url.length () 
                  > 0 && url.equalsIgnoreCase("stock")) {%>
              <div id="wrapperspace">&nbsp
                <br>
              </div>
              <div id="wrapper">
                <div id="column-left">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/opname.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Opname" : "Opname"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/stock/mat_opname_src.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah" : "Update/Add"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/barcode/importtextfilesrc.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Opname (Used File)  " : "Opname (Used File) "%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/stock/mat_stock_correction_src.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Koreksi Stok" : "Correction Stock"%>
                        </font>
                      </b>
                    </a>
                  </div>
                </div>
                <div id="column-center">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/laporanStock.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan" : "Report"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportstock.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Global" : "Global"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportstockkategori.jsp?tipe=1" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Kategori" : "By Category"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportstocksupplier.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Supplier" : "By Supplier"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_stockcard.jsp?type=1" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Kartu Stok" : "Stock Card"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportposisistock.jsp?type=1" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Posisi Stok" : "Stock Position"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportposisistock_history.jsp?type=1" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Histori Posisi Stok" : "Stock Position History"%>
                        </font>
                      </b>
                    </a>
                  </div>    
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportselisihkoreksistok.jsp?type=1" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Selisih Koreksi Stok" : "List Lost Correction Stock"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportselisihkoreksistok_daily.jsp?type=1&daily=1" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daily Selisih Koreksi Stok" : "List Lost Correction Stock"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <%if (false) {%>

                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/report/src_reportstocktracking.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Penelusuran Stok" : "Stock Tracking"%>
                        </font>
                      </b>
                    </a>
                  </div> 
                  <%}%>      
                </div>
                <div id="column-right">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/posting.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Posting" : "Posting"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/posting/posting_stock_new.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Posting Stok" : "Posting Stock"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/posting/srcmaterial_reposting_stock.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Re Posting Stok" : "Re Posting Stock"%>
                        </font>
                      </b>
                    </a>
                  </div>
                </div>
              </div>
              <%}
                else if (url

                != null && url.length () 
                  > 0 && url.equalsIgnoreCase("masterdata")) {%>
              <div id="wrapperspace">&nbsp
                <br>
              </div>
              <div id="wrapper-masterdata">
                <div id="column-master1">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/masterdata.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Master Data" : "Master Data"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/masterdata/company.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Company" : "Company"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/location/country.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Country" : "Country"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/location/provinsi.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Provinsi" : "province"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/location/kabupaten.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Kota" : "Regency"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/location/kecamatan.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Area" : "Area"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/location/location.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Lokasi" : "Location"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/masterdata/master_group.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Master Group" : "Master Group"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/masterdata/master_type.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Master Type" : "Master Type"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <%if (false) {%>
                  <%}%>         
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/masterdata/master_employee.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Master Pegawai" : "Master Employee"%>
                        </font>
                      </b>
                    </a>
                  </div>

                </div>
                <div id="column-master">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_empety.png" align="absmiddle">
                      <b>
                        <font size="5">&nbsp;</font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/periode/period.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Periode" : "Stock Period"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <% if (typeOfBusiness.equals("0")) {%>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/material/matksg.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Master Gondola" : "Master Gondola"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <%}%>     
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/material/matcategory.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Kategori Barang" : "Category"%>
                        </font>
                      </b>
                    </a>
                  </div>    
                  <%
                    int useSubCategory = Integer.parseInt((String) com.dimata.system.entity.PstSystemProperty.getValueIntByName("USE_SUB_CATEGORY"));
                    if (useSubCategory == 1) {%>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/material/matsubcategory.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Sub Kategori Barang" : "Sub Category"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <%}%>    
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/material/matmerk.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=merkName%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/material/matunit.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Satuan" : "Unit List"%>
                        </font>
                      </b>
                    </a>
                  </div>

                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/material/srcmaterial.jsp?typemenu=0" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Item" : "Items"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <% if (!typeOfBusiness.equals("0")) {%>          
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/material/srcmaterial.jsp?typemenu=1" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Menu" : "Set Menu"%>
                        </font>
                      </b>
                    </a>
                  </div>         
                  <%}%>         
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/material/src_update_harga.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Edit Item" : "Edit Items"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/material/src_print_barcode.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Print Barcode" : "Print Barcode"%>
                        </font>
                      </b>
                    </a>
                  </div> 
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/material/mapping_item_store_request.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Mapping Item SR" : "Mapping Item SR"%>
                        </font>
                      </b>
                    </a>
                  </div> 
                </div>
                <div id="column-master">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_empety.png" align="absmiddle">
                      <b>
                        <font size="5">&nbsp;</font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/contact/srccontactcompany.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Supplier" : "Supplier List"%>
                        </font>
                      </b>
                    </a>
                  </div>

                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/masterdata/pricetype.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Tipe Harga" : "Price Type"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/masterdata/discounttype.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Tipe Potongan" : "Discount Type"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/masterdata/membergroup.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Tipe Customer" : "Member Type"%>
                        </font>
                      </b>
                    </a>
                  </div>
                </div>
                <div id="column-master">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_empety.png" align="absmiddle">
                      <b>
                        <font size="5">&nbsp;</font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/masterdata/srcmemberreg_point.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Point Member" : "Member Points"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/masterdata/srcmemberreg.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Member" : "Member List"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <% if (!typeOfBusiness.equals("0")) {%>          
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/masterdata/srctravel.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Travel" : "Travel List"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/masterdata/srcMemberCompany.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Company Member" : "Company Member"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/masterdata/srcguide.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Guide" : "Guide List"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div style="display:none" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/masterdata/contactclass.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Contact Class" : "Contact Class"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div style="display:none" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/masterdata/contactlist.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Contact List" : "Contact List"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <%}%>          
                </div>
                <div id="column-master">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_empety.png" align="absmiddle">
                      <b>
                        <font size="5">&nbsp;</font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/masterdata/currencytype.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Jenis Mata Uang" : "Currency List"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/masterdata/dailyrate_list.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Nilai Tukar Harian" : "Daily Rate"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/masterdata/standartrate.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Nilai Tukar Standart" : "Standar Rate"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/material/matcosting.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Tipe Biaya" : "Costing Type"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/material/mat_type_sales.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Tipe Sales" : "Sales Type"%>
                        </font>
                      </b>
                    </a>
                  </div>         
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/payment/pay_system.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Sistem Pembayaran" : "Payment System"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <%--<div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                           <a href="<%=approot%>/master/payment/voucher.jsp" class="menulink">
                               <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                               &nbsp;<b>
<font size="3">
<%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Voucher":"Voucher"%>
</font>
</b>
                           </a>
                  </div>--%>         
                </div>
                <div id="column-right-two4">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_empety.png" align="absmiddle">
                      <b>
                        <font size="5">&nbsp;</font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/cashier/master_kasir.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Master Kasir" : "Cashier Master"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/masterdata/shift.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Shift Kerja" : "Working Shift"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/masterdata/sales.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Sales" : "Salest List"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <%if (typeOfBusiness.equals("2")) {%>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/location/roomclass.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Tipe Ruangan" : "Room Class List"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/location/room.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Ruangan" : "Room List"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/location/tableroom.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Meja" : "Table List"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <%}%>
                </div>
              </div>
              <%}
                else if (url

                != null && url.length () 
                  > 0 && url.equalsIgnoreCase("sistem")) {%>
              <div id="wrapperspace">&nbsp
                <br>
              </div>
              <div id="wrapper">
                <div id="column-left" style="width:262px;">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/sistem.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Sistem" : "System"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/system/userlist.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pemakai" : "Users"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/system/grouplist.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Group" : "Group"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/system/privilegelist.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Hak Akses" : "Privilege"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/system/sysprop.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pengaturan Aplikasi" : "System Setting"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/styletemplate/chage_template.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Modif Template" : "Modif Template"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/system/open_document.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Open Status Dokument" : "Open Document Status"%>
                        </font>
                      </b>
                    </a>
                  </div>         
                </div>
                <div id="column-center" style="width:262px;">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/tutup periode.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Tutup<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Periode" : "Close<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Period"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/closing/closing_monthly.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Setting Tutup Periode" : "Close Period"%>
                        </font>
                      </b>
                    </a>
                  </div>
                </div>
                <div id="column-right" style="width:262px;">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/transferdata.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pertukaran <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Data" : "Data <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Exchange"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/DBconnection/connection.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Internet Connection" : "Internet Connection"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/data_sync/transfer_data.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Transfer Ke Outlet" : "Transfer To Outlet"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/data_sync/transfer_data_to_server.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Transfer Penjualan" : "Sales Transfer"%>
                        </font>
                      </b>
                    </a>
                  </div>
                </div>
                <div id="column-center" style="width:262px;">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/book_search.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Bantuan" : "Help"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="javascript:openHelp()" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Bantuan" : "Help"%>
                        </font>
                      </b>
                    </a>
                  </div>
                </div>
              </div>
              <%}
                else if (url

                != null && url.length () 
                
                > 0 && url.equalsIgnoreCase("keluar")) {%>
              <%} else if (url

                != null && url.length () 
                  > 0 && url.equalsIgnoreCase("help")) {%>
              <div id="wrapperspace">&nbsp
                <br>
              </div>
              <div id="wrapper">
                <div id="column-left">
                  <div id="column-center">
                    <div align="left">
                      <a href="#" class="menulink">
                        <img src="<%=approot%>/menustylesheet/icon_menu_pos/sistem.png" align="absmiddle">&nbsp;<b>
                          <font size="5">
                          <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Sistem" : "System"%>
                          </font>
                        </b>
                      </a>
                    </div>
                    <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <a href="javascript:openHelp()" class="menulink">
                        <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                        &nbsp;<b>
                          <font size="3">
                          <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pemakai" : "Users"%>
                          </font>
                        </b>
                      </a>
                    </div>
                  </div>
                </div>        
              </div>
              <%}
                else if (url

                != null && url.length () 
                  > 0 && url.equalsIgnoreCase("kasir")) {%>
              <div id="wrapperspace">&nbsp
                <br>
              </div>
              <div id="wrapper">
                <div id="column-left">
                  <div id="column-center">
                    <div align="left">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/2.Piutang.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Invoicing" : "Invoicing"%>
                        </font>
                      </b>
                    </div>
                    <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <a href="<%=approot%>/cashieronline/open_shift.jsp?typeListCashier=1" class="menulink">
                        <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                        &nbsp;<b>
                          <font size="3">
                          <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Open List Invoice Order " : "Open List Invoice Order"%>
                          </font>
                        </b>
                      </a>
                    </div>
                    <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <a href="<%=approot%>/cashieronline/open_shift.jsp?typeListCashier=5" class="menulink">
                        <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                        &nbsp;<b>
                          <font size="3">
                          <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Void & Return Invoice" : "Void & Return  Invoice"%>
                          </font>
                        </b>
                      </a>
                    </div>
                  </div>

                </div>
                <%-- Cashier --%>
                <div id="column-right">
                  <div align="left">
                    <img src="<%=approot%>/menustylesheet/icon_menu_pos/2.Piutang.png" align="absmiddle">&nbsp;<b>
                      <font size="5">
                      <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Cashier Payment" : "Cashier Payment"%>
                      </font>
                    </b>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/cashieronline/open_shift.jsp?typeListCashier=3" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "List Invoice" : "List  Invoice"%>
                        </font>
                      </b>
                    </a>
                  </div>
                </div> 
                <div id="column-right">
                  <div align="left">
                    <img src="<%=approot%>/menustylesheet/icon_menu_pos/9.transfer.png" align="absmiddle">&nbsp;<b>
                      <font size="5">
                      <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pengiriman Barang" : "Delivery Order"%>
                      </font>
                    </b>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/warehouse/material/dispatch/src_delivery_order_sales.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pengiriman Barang" : "Delivery Order"%>
                        </font>
                      </b>
                    </a>
                  </div>
                </div>        
              </div>
              <%}
                else if (url

                != null && url.length () 
                  > 0 && url.equalsIgnoreCase("promosi")) {%>
              <div id="wrapperspace">&nbsp
                <br>
              </div>
              <div id="wrapper">
                <div id="column-left">
                  <div align="left">
                    <a href="#" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/promosi.png" align="absmiddle">&nbsp;<b>
                        <font size="5">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Promosi" : "Promotion"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/master/payment/voucher.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Voucher" : "Voucher"%>
                        </font>
                      </b>
                    </a>
                  </div>
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/promotion/newsinfo.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Berita dan Informasi" : "News and Info"%>
                        </font>
                      </b>
                    </a>
                  </div>  
                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="<%=approot%>/promotion/marketing-promotion.jsp" class="menulink">
                      <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                      &nbsp;<b>
                        <font size="3">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Promosi" : "Promotion"%>
                        </font>
                      </b>
                    </a>
                  </div> 
                </div>
              </div>
              <%}

                
                
              else{%>
              <tr>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td align="center">Tidak ada modul yang anda cari</td>
              </tr>
              <%}%>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>