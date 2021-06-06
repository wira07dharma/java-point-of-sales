<%-- 
    Document   : homexframe
    Created on : Jun 3, 2014, 10:11:04 AM
    Author     : sangtel6
--%>

<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatStockOpname"%>
<%@page import="com.dimata.posbo.session.purchasing.SessPurchaseOrder"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatCosting"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatReturn"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatDispatch"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatReceive"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" %>
<%@ include file = "main/javainit.jsp" %>
<html>
<!-- #BeginTemplate "/Templates/main-menu-left-frames.dwt" --> 
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Untitled Document</title>
<!-- #EndEditable --> <!-- #BeginEditable "headerscript" --> 
<SCRIPT language=JavaScript>
function hideObjectForMenuJournal(){     
}
	 
function hideObjectForMenuReport(){
}
	
function hideObjectForMenuPeriod(){
}
	
function hideObjectForMenuMasterData(){
}

function hideObjectForMenuSystem(){ 
}

function showObjectForMenu(){
}
</SCRIPT>
<!-- #EndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="styles/main.css" type="text/css">
</head>
<body class="bodystyle" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="1" cellpadding="1" height="100%">
  <tr> 
    <td width="91%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
        <tr> 
          <td height="20" class="contenttitle" >&nbsp;
		<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td valign="top"><!-- #BeginEditable "content" --> 
            <table width="100%" border="0" cellspacing="1" cellpadding="1" height="100%">
              <tr> 
                <td width="91%" valign="top" align="left" > 
                  <table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
                    <tr> 
                      <td valign="top">
                        <%
                                //int vecSize=0;
                                Vector ReceiveMaterialInformation = new Vector(1, 1);
                                Vector ReceiveMaterialNonSupplierInformation = new Vector(1, 1);
                                Vector matDispatchInformation = new Vector(1, 1);
                                Vector matDispatchUnitInformation = new Vector(1, 1);
                                Vector returnInformation = new Vector(1, 1);
                                Vector pembiayaanInformation = new Vector(1, 1);
                                Vector orderPembelianInformation = new Vector(1, 1);
                                int vecSizeOpnema =0;
                                SessMatReceive sessMatReceive = new SessMatReceive();
                                SessMatDispatch sessMatDispatch = new SessMatDispatch();
                                SessMatReturn sessMatReturn = new SessMatReturn();
                                SessMatCosting sessMatCosting = new SessMatCosting();
                                SessPurchaseOrder sessPurchaseOrder = new SessPurchaseOrder();
                                SessMatStockOpname sessMatStockOpname = new SessMatStockOpname();

                                //update opie-eyek 20130805
                                if(MODUS_TRANSFER == MODUS_TRANSFER_SERVER){
                                    String whereClausex = " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");
                                    orderPembelianInformation= sessPurchaseOrder.getPOMaterialListInformation(whereClausex);
                                    ReceiveMaterialInformation= sessMatReceive.getListReceiveMaterialInformation(whereClausex);
                                    ReceiveMaterialNonSupplierInformation = sessMatReceive.getListReceiveMaterialNonSupplierInformation(whereClausex);
                                    matDispatchInformation=sessMatDispatch.getMatDispatchInformation(whereClausex);
                                    matDispatchUnitInformation=sessMatDispatch.getMatDispatchUnitInformation(whereClausex);
                                    returnInformation=sessMatReturn.getListReturnMaterialInformation(whereClausex);
                                    pembiayaanInformation=sessMatCosting.getMatCostingInformation(whereClausex);
                                    vecSizeOpnema =sessMatStockOpname.getListOpnameInformation(whereClausex);
                                }
                        %>
                        <tr>
                          <td>&nbsp;.</td>
                        </tr>
                        <tr>
                        <td align="center">&nbsp;</td>
                        </tr>
                        <tr>
                        <td align="center">
                            <b><font size="4">
                                    -
                                     <%if(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){%>MODUL ADMINISTRATOR
                                     <%}else{%>
                                     ADMINISTRATOR MODULE
                                     <%}%>
                                     -
                                </font>
                            </b>
                        </td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                        </tr>
                        <% //update opie-eyek 20130805
                        if(MODUS_TRANSFER == MODUS_TRANSFER_SERVER){
                            if(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){ //indonesia
                            %>
                            <%if( vecSizeOpnema != 0) {%>
                                <tr>
                                    <%--document penerimaan dari pembelian--%>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Document Opname dengan status Final = <b><%=vecSizeOpnema%> Document</b>.&nbsp;
                                        Silahkan lakukan koreksi stok terlebih dahulu sehingga posting bisa dilakukan <a href="<%=approot%>/warehouse/material/stock/mat_stock_correction_src.jsp"><blink>Click Here</blink></a></td>
                                </tr>
                             <%}else{%>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Tidak Ada Document Opname dengan status Final</td>
                                </tr>
                            <%}%>
                            <tr>
                              <td>&nbsp;</td>
                            </tr>
                            <tr>
                              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>1.&nbsp;Document Order Pembelian  <a href="<%=approot%>/purchasing/material/pom/srcpomaterial.jsp">( Click Here )</a></b></td>
                            </tr>
                            <%if((orderPembelianInformation != null) && (orderPembelianInformation.size() > 0)) {%>
                                <%
                                for (int i = 0; i < orderPembelianInformation.size(); i++) {
                                     Vector temp = (Vector)orderPembelianInformation.get(i);
                                %>
                                <tr>
                                    <%--document penerimaan dari pembelian--%>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Document Order Pembelian dengan Status <%if(i==0 && temp.get(1).equals(0)){%>
                                        draft
                                        <%}else{%>
                                        final
                                        <%}%>
                                        = <%=temp.get(0)%>&nbsp;Document</td>
                                </tr>
                                <%
                                }
                                %>

                             <%}else{%>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Tidak Ada Document Order Pembelian</td>
                                </tr>
                            <%}%>
                            <tr>
                              <td>&nbsp;</td>
                            </tr>
                            <tr>
                              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>2.&nbsp;Document Penerimaan dari Pembelian  <a href="<%=approot%>/warehouse/material/receive/src_receive_material.jsp">( Click Here )</a></b></td>
                            </tr>
                            <%if((ReceiveMaterialInformation != null) && (ReceiveMaterialInformation.size() > 0)) {%>
                                <%
                                for (int i = 0; i < ReceiveMaterialInformation.size(); i++) {
                                     Vector temp = (Vector)ReceiveMaterialInformation.get(i);
                                %>
                                <tr>
                                    <%--document penerimaan dari pembelian--%>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Document Penerimaan dari Pembelian dengan Status <%if(i==0  && temp.get(1).equals(0) ){%>
                                        draft
                                        <%}else{%>
                                        final
                                        <%}%>
                                        = <%=temp.get(0)%>&nbsp;Document</td>
                                </tr>
                                <%
                                }
                                %>

                             <%}else{%>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Tidak Ada Document Penerimaan dari Pembelian </td>
                                </tr>
                            <%}%>
                            <tr>
                              <td>&nbsp;</td>
                            </tr>
                             <%--document penerimaan dari toko--%>
                             <tr>
                              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>3.&nbsp;Document Penerimaan dari Toko <a href="<%=approot%>/warehouse/material/receive/srcreceive_store_wh_material.jsp">( Click Here )</a></b></td>
                            </tr>
                            <%if((ReceiveMaterialNonSupplierInformation != null) && (ReceiveMaterialNonSupplierInformation.size() > 0)) {%>
                                <%
                                for (int i = 0; i < ReceiveMaterialNonSupplierInformation.size(); i++) {
                                     Vector temp = (Vector)ReceiveMaterialNonSupplierInformation.get(i);
                                %>
                                <tr>
                                    <%--document penerimaan dari pembelian--%>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Document Penerimaan Dari Toko dengan Status <%if(i==0  && temp.get(1).equals(0) ){%>
                                        draft
                                        <%}else{%>
                                        final
                                        <%}%>
                                        = <%=temp.get(0)%>&nbsp;Document</td>
                                </tr>
                                <%
                                }
                                %>
                            <%}else{%>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Tidak Ada Document Penerimaan dari Toko</td>
                                </tr>
                            <%}%>
                            <tr>
                              <td>&nbsp;</td>
                            </tr>
                              <%--document Transfer --%>
                            <tr>
                              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>4.&nbsp;Document Transfer <a href="<%=approot%>/warehouse/material/dispatch/srcdf_stock_wh_material.jsp">( Click Here )</b></td>
                            </tr>
                            <%if((matDispatchInformation != null) && (matDispatchInformation.size() > 0)) {%>
                                <%
                                for (int i = 0; i < matDispatchInformation.size(); i++) {
                                     Vector temp = (Vector)matDispatchInformation.get(i);
                                %>
                                <tr>
                                    <%--document penerimaan dari pembelian--%>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Document Transfer dengan Status <%if(i==0  && temp.get(1).equals(0) ){%>
                                        draft
                                        <%}else{%>
                                        final
                                        <%}%>
                                        = <%=temp.get(0)%>&nbsp;Document</td>
                                </tr>
                                <%
                                }
                                %>
                            <%}else{%>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Tidak Ada Document Transfer</td>
                                </tr>
                            <%}%>
                            <tr>
                              <td>&nbsp;</td>
                            </tr>
                           <tr>
                              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>5.&nbsp;Document Transfer Unit <a href="<%=approot%>/warehouse/material/dispatch/srcdf_unit_wh_material.jsp">( Click Here )</b></td>
                            </tr>
                            <%if((matDispatchUnitInformation != null) && (matDispatchUnitInformation.size() > 0)) {%>
                                <%
                                for (int i = 0; i < matDispatchUnitInformation.size(); i++) {
                                     Vector temp = (Vector)matDispatchUnitInformation.get(i);
                                %>
                                <tr>
                                    <%--document penerimaan dari pembelian--%>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Document Transfer Unit dengan Status <%if(i==0  && temp.get(1).equals(0) ){%>
                                        draft
                                        <%}else{%>
                                        final
                                        <%}%>
                                        = <%=temp.get(0)%>&nbsp;Document</td>
                                </tr>
                                <%
                                }
                                %>
                            <%}else{%>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Tidak Ada Document Transfer Unit</td>
                                </tr>
                            <%}%>
                            <tr>
                              <td>&nbsp;</td>
                            </tr>
                            <tr>
                              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>6.&nbsp;Document Return <a href="<%=approot%>/warehouse/material/return/src_return_material.jsp">( Click Here )</b></td>
                            </tr>
                            <%if((returnInformation != null) && (returnInformation.size() > 0)) {%>
                                <%
                                for (int i = 0; i < returnInformation.size(); i++) {
                                     Vector temp = (Vector)returnInformation.get(i);
                                %>
                                <tr>
                                    <%--document penerimaan dari pembelian--%>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Document Return dengan Status <%if(i==0  && temp.get(1).equals(0) ){%>
                                        draft
                                        <%}else{%>
                                        final
                                        <%}%>
                                        = <%=temp.get(0)%>&nbsp;Document</td>
                                </tr>
                                <%
                                }
                                %>
                            <%}else{%>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Tidak Ada Document Return</td>
                                </tr>
                            <%}%>
                            <tr>
                              <td>&nbsp;</td>
                            </tr>
                            <tr>
                              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>7.&nbsp;Document Pembiayaan (Costing) <a href="<%=approot%>/warehouse/material/dispatch/srccosting_material.jsp">( Click Here )</b></td>
                            </tr>
                            <%if((pembiayaanInformation != null) && (pembiayaanInformation.size() > 0)) {%>
                                <%
                                for (int i = 0; i < pembiayaanInformation.size(); i++) {
                                     Vector temp = (Vector)pembiayaanInformation.get(i);
                                %>
                                <tr>
                                    <%--document penerimaan dari pembelian--%>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Document Pembiayaan (Costing) dengan Status <%if(i==0  && temp.get(1).equals(0) ){%>
                                        draft
                                        <%}else{%>
                                        final
                                        <%}%>
                                        = <%=temp.get(0)%>&nbsp;Document</td>
                                </tr>
                                <%
                                }
                                %>
                            <%}else{%>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Tidak Ada Document Pembiayaan (Costing)</td>
                                </tr>
                            <%}%>
                            <tr>
                              <td>&nbsp;</td>
                            </tr>
                           <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Untuk melakukan posting <a href="<%=approot%>/master/posting/posting_stock_new.jsp"><blink>Click Here</blink></a></td>
                            </tr>
                        <%}else{ //english%>
                            <%if( vecSizeOpnema != 0) {%>
                                <tr>
                                    <%--document penerimaan dari pembelian--%>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Document Opname status Final = <b><%=vecSizeOpnema%> Document</b>.&nbsp;
                                        Please do correction stock in advance so that the post can be done <a href="<%=approot%>/warehouse/material/stock/mat_stock_correction_src.jsp"><blink>Click Here</blink></a></td>
                                </tr>
                             <%}else{%>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; No Document Opname With Final Status </td>
                                </tr>
                            <%}%>
                            <tr>
                              <td>&nbsp;</td>
                            </tr>
                            <tr>
                              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>1.&nbsp;Document Purchase Order  <a href="<%=approot%>/purchasing/material/pom/srcpomaterial.jsp">( Click Here )</a></b></td>
                            </tr>
                            <%if((orderPembelianInformation != null) && (orderPembelianInformation.size() > 0)) {%>
                                <%
                                for (int i = 0; i < orderPembelianInformation.size(); i++) {
                                     Vector temp = (Vector)orderPembelianInformation.get(i);
                                %>
                                <tr>
                                    <%--document penerimaan dari pembelian--%>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Document Purchase Order With Status <%if(i==0 && temp.get(1).equals(0)){%>
                                        draft
                                        <%}else{%>
                                        final
                                        <%}%>
                                        = <%=temp.get(0)%>&nbsp;Document</td>
                                </tr>
                                <%
                                }
                                %>

                             <%}else{%>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; No Document Purchase Order</td>
                                </tr>
                            <%}%>
                            <tr>
                              <td>&nbsp;</td>
                            </tr>
                            <tr>
                              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>2.&nbsp;Document Receive From Purchase Order<a href="<%=approot%>/warehouse/material/receive/src_receive_material.jsp">( Click Here )</a></b></td>
                            </tr>
                            <%if((ReceiveMaterialInformation != null) && (ReceiveMaterialInformation.size() > 0)) {%>
                                <%
                                for (int i = 0; i < ReceiveMaterialInformation.size(); i++) {
                                     Vector temp = (Vector)ReceiveMaterialInformation.get(i);
                                %>
                                <tr>
                                    <%--document penerimaan dari pembelian--%>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Document Receive From Purchase Order With Status <%if(i==0  && temp.get(1).equals(0) ){%>
                                        draft
                                        <%}else{%>
                                        final
                                        <%}%>
                                        = <%=temp.get(0)%>&nbsp;Document</td>
                                </tr>
                                <%
                                }
                                %>

                             <%}else{%>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; No Document Receive From Purchase Order </td>
                                </tr>
                            <%}%>
                            <tr>
                              <td>&nbsp;</td>
                            </tr>
                             <%--document penerimaan dari toko--%>
                             <tr>
                              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>3.&nbsp;Document Receive From Transfer <a href="<%=approot%>/warehouse/material/receive/srcreceive_store_wh_material.jsp">( Click Here )</a></b></td>
                            </tr>
                            <%if((ReceiveMaterialNonSupplierInformation != null) && (ReceiveMaterialNonSupplierInformation.size() > 0)) {%>
                                <%
                                for (int i = 0; i < ReceiveMaterialNonSupplierInformation.size(); i++) {
                                     Vector temp = (Vector)ReceiveMaterialNonSupplierInformation.get(i);
                                %>
                                <tr>
                                    <%--document penerimaan dari pembelian--%>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Document Receive From Transfer With Status <%if(i==0  && temp.get(1).equals(0) ){%>
                                        draft
                                        <%}else{%>
                                        final
                                        <%}%>
                                        = <%=temp.get(0)%>&nbsp;Document</td>
                                </tr>
                                <%
                                }
                                %>
                            <%}else{%>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; No Document Receive From Transfer</td>
                                </tr>
                            <%}%>
                            <tr>
                              <td>&nbsp;</td>
                            </tr>
                              <%--document Transfer --%>
                            <tr>
                              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>4.&nbsp;Document Transfer <a href="<%=approot%>/warehouse/material/dispatch/srcdf_stock_wh_material.jsp">( Click Here )</b></td>
                            </tr>
                            <%if((matDispatchInformation != null) && (matDispatchInformation.size() > 0)) {%>
                                <%
                                for (int i = 0; i < matDispatchInformation.size(); i++) {
                                     Vector temp = (Vector)matDispatchInformation.get(i);
                                %>
                                <tr>
                                    <%--document penerimaan dari pembelian--%>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Document Transfer With Status <%if(i==0  && temp.get(1).equals(0) ){%>
                                        draft
                                        <%}else{%>
                                        final
                                        <%}%>
                                        = <%=temp.get(0)%>&nbsp;Document</td>
                                </tr>
                                <%
                                }
                                %>
                            <%}else{%>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; No Document Transfer</td>
                                </tr>
                            <%}%>
                            <tr>
                              <td>&nbsp;</td>
                            </tr>
                           <tr>
                              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>5.&nbsp;Document Transfer Unit <a href="<%=approot%>/warehouse/material/dispatch/srcdf_unit_wh_material.jsp">( Click Here )</b></td>
                            </tr>
                            <%if((matDispatchUnitInformation != null) && (matDispatchUnitInformation.size() > 0)) {%>
                                <%
                                for (int i = 0; i < matDispatchUnitInformation.size(); i++) {
                                     Vector temp = (Vector)matDispatchUnitInformation.get(i);
                                %>
                                <tr>
                                    <%--document penerimaan dari pembelian--%>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Document Transfer Unit With Status <%if(i==0  && temp.get(1).equals(0) ){%>
                                        draft
                                        <%}else{%>
                                        final
                                        <%}%>
                                        = <%=temp.get(0)%>&nbsp;Document</td>
                                </tr>
                                <%
                                }
                                %>
                            <%}else{%>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; No Document Transfer Unit</td>
                                </tr>
                            <%}%>
                            <tr>
                              <td>&nbsp;</td>
                            </tr>
                            <tr>
                              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>6.&nbsp;Document Return <a href="<%=approot%>/warehouse/material/return/src_return_material.jsp">( Click Here )</b></td>
                            </tr>
                            <%if((returnInformation != null) && (returnInformation.size() > 0)) {%>
                                <%
                                for (int i = 0; i < returnInformation.size(); i++) {
                                     Vector temp = (Vector)returnInformation.get(i);
                                %>
                                <tr>
                                    <%--document penerimaan dari pembelian--%>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Document Return With Status <%if(i==0  && temp.get(1).equals(0) ){%>
                                        draft
                                        <%}else{%>
                                        final
                                        <%}%>
                                        = <%=temp.get(0)%>&nbsp;Document</td>
                                </tr>
                                <%
                                }
                                %>
                            <%}else{%>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; No Document Return</td>
                                </tr>
                            <%}%>
                            <tr>
                              <td>&nbsp;</td>
                            </tr>
                            <tr>
                              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>7.&nbsp;Document  Costing  <a href="<%=approot%>/warehouse/material/dispatch/srccosting_material.jsp">( Click Here )</b></td>
                            </tr>
                            <%if((pembiayaanInformation != null) && (pembiayaanInformation.size() > 0)) {%>
                                <%
                                for (int i = 0; i < pembiayaanInformation.size(); i++) {
                                     Vector temp = (Vector)pembiayaanInformation.get(i);
                                %>
                                <tr>
                                    <%--document penerimaan dari pembelian--%>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Document Costing With Status <%if(i==0  && temp.get(1).equals(0) ){%>
                                        draft
                                        <%}else{%>
                                        final
                                        <%}%>
                                        = <%=temp.get(0)%>&nbsp;Document</td>
                                </tr>
                                <%
                                }
                                %>
                            <%}else{%>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; No Document Costing</td>
                                </tr>
                            <%}%>
                            <tr>
                              <td>&nbsp;</td>
                            </tr>
                           <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; To do Posting <a href="<%=approot%>/master/posting/posting_stock_new.jsp"><blink>Click Here</blink></a></td>
                            </tr>
                          <%}
                          }%>
                        <tr>
                          <td>&nbsp;</td>
                        </tr>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20" class="footer"> 
      <%@ include file = "main/footer.jsp" %>
    </td>
  </tr>
</table>
</body>
<!-- #EndTemplate -->
</html>
