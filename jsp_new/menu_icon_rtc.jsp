<%-- 
    Document   : menu_icon_rtc
    Created on : Jan 22, 2014, 4:13:31 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.entity.masterdata.PstMemberReg"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcAccPayable"%>
<%@page import="com.dimata.posbo.entity.search.SrcAccPayable"%>
<%@page import="com.dimata.posbo.session.arap.SessAccPayable"%>
<%@page import="com.dimata.posbo.session.arap.SessAccPayable"%>
<%@page import="com.dimata.posbo.entity.search.SrcSaleReport"%>
<%@page import="com.dimata.posbo.session.sales.SessAr"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatStockOpname"%>
<%@page import="com.dimata.posbo.session.purchasing.SessPurchaseOrder"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatCosting"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatReturn"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatDispatch"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatReceive"%>
<%@page import="java.util.Vector"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"df_stock_wh_material_edit.jsp
   "http://www.w3.org/TR/html4/loose.dtd">

<style type="text/css">
    #wrapper { width: 1050px; margin: 0 auto; }
    #wrapperspace { width: 1000px; margin: 0 auto; }
    #wrapperspace-masterdata { width: 1400px; margin: 0 auto; }
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
    #wrapper-gudang { width: 1335px; margin: 0 auto; }
    #wrapper-penjualan { width: 1330px; margin: 0 auto; }
    #column-left-gudang{
       width: 410px; float: left; /*background: red;*/
    }
    #column-center2 {
        width: 264px; float: left; /*background: yellow;*/
    }
    #column-center-gudang{
        width: 320px;
        float: left;
    }
    #column-right3 {
        width: 262px; float: left; /*background: purple;*/
    }
     #column-right-two4 {
        width: 200px; float: left; /*background: purple;*/
    }
    #column-right-masterdata{
        width: 250px; float: left;
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
    #tdStyle {
        padding: 7px 5px;
        border-radius: 3px;
    }
    #tdStyle:hover{
        background-color: #eee;
    }
</style>
<script>
    function cmdSearch() {       
	var st_mm = document.getElementById("start_mm_now").value;
        var st_dd = document.getElementById("start_dd_now").value;
        var st_yy = document.getElementById("start_yy_now").value;
        var en_mm = document.getElementById("end_mm_now").value;
        var en_dd = document.getElementById("end_dd_now").value;
        var en_yy = document.getElementById("end_yy_now").value;
        document.frmSrcOpname.command.value="1";

        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_START_DATE]%>_mn.value=st_mm;
        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_START_DATE]%>_dy.value=st_dd;
        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_START_DATE]%>_yr.value=st_yy;
        
        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_END_DATE]%>_mn.value=en_mm;
        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_END_DATE]%>_dy.value=en_dd;
        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_END_DATE]%>_yr.value=en_yy;
        
	document.frmSrcOpname.action="arap/payable/payable_view.jsp";
	document.frmSrcOpname.submit();
    }
    function cmdSearch3() {
	var st_mm = document.getElementById("start_mm_now").value;
        var st_dd = document.getElementById("start_dd_now").value;
        var st_yy = document.getElementById("start_yy_now").value;
        var en_mm = document.getElementById("end_mm_3").value;
        var en_dd = document.getElementById("end_dd_3").value;
        var en_yy = document.getElementById("end_yy_3").value;
        document.frmSrcOpname.command.value="1";
        
        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_START_DATE]%>_mn.value=st_mm;
        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_START_DATE]%>_dy.value=st_dd;
        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_START_DATE]%>_yr.value=st_yy;
        
        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_END_DATE]%>_mn.value=en_mm;
        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_END_DATE]%>_dy.value=en_dd;
        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_END_DATE]%>_yr.value=en_yy;
        
	document.frmSrcOpname.action="arap/payable/payable_view.jsp";
	document.frmSrcOpname.submit();
    }
    function cmdSearch7() {
	var st_mm = document.getElementById("start_mm_now").value;
        var st_dd = document.getElementById("start_dd_now").value;
        var st_yy = document.getElementById("start_yy_now").value;
        var en_mm = document.getElementById("end_mm_7").value;
        var en_dd = document.getElementById("end_dd_7").value;
        var en_yy = document.getElementById("end_yy_7").value;
        document.frmSrcOpname.command.value="1";
        
        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_START_DATE]%>_mn.value=st_mm;
        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_START_DATE]%>_dy.value=st_dd;
        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_START_DATE]%>_yr.value=st_yy;
        
        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_END_DATE]%>_mn.value=en_mm;
        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_END_DATE]%>_dy.value=en_dd;
        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_END_DATE]%>_yr.value=en_yy;
        
	document.frmSrcOpname.action="arap/payable/payable_view.jsp";
	document.frmSrcOpname.submit();
    }
    // update by Hendra McHen 2015-01-05
    function datePiutang(nilai){
        var st_mm = document.getElementById("start_mm_now").value;
        var st_dd = document.getElementById("start_dd_now").value;
        var st_yy = document.getElementById("start_yy_now").value;
        var dayEnd = document.getElementById("batas_hari").value;

        document.frmSrcOpname.command.value="1";
        document.frmSrcOpname.date_kedepan.value=nilai;
        document.frmSrcOpname.batas_hari.value=dayEnd;

        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_START_DATE]%>_mn.value=st_mm;
        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_START_DATE]%>_dy.value=st_dd;
        document.frmSrcOpname.<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_START_DATE]%>_yr.value=st_yy;

        
	document.frmSrcOpname.action="cashieronline/srcCustomerInvoice.jsp";
	document.frmSrcOpname.submit();
    }
    
    function cmdSearchPiutang(){
        datePiutang("0");
    }
    
    function cmdSearchPiutang3(){
        datePiutang("3");
    }
    
    function cmdSearchPiutang7(){
        datePiutang("7");
    }
</script>
<%
//int appObjCodeKasirPayment = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_KASIR, AppObjInfo.OBJ_KASIR_PAYMENT) ;
//boolean privApprovalKasirPayment = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeKasirPayment, AppObjInfo.COMMAND_VIEW));

int appObjCodeKasirInvoice = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_KASIR, AppObjInfo.OBJ_INVOICE); 
boolean privApprovalInvoicing = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeKasirInvoice, AppObjInfo.COMMAND_VIEW));
%>
<%
    /*
     * Document : Checking date
     * Date : 2014-12-04
     * Author : Hendra McHen
     */
    Date today = new Date();  
    Calendar calendar = Calendar.getInstance();  
    calendar.setTime(today);  

    calendar.add(Calendar.MONTH, 1);  
    calendar.set(Calendar.DAY_OF_MONTH, 1);  
    calendar.add(Calendar.DATE, -1);  

    Date lastDayOfMonth = calendar.getTime();  

    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String dateNow = sdf.format(today);
    String yearCurr = dateNow.substring(0, 4);//
    String monthCurr = dateNow.substring(5, 7);
    String dayCurr = dateNow.substring(8, 10);
    
    String dateEnd = sdf.format(lastDayOfMonth);
    String dayEnd = dateEnd.substring(8, 10);
    String monthEnd = dateEnd.substring(5, 7);
    String yearEnd = dateEnd.substring(0, 4);
    
    
    int dayNow = Integer.parseInt(dayCurr);
    int dayAkhir = Integer.parseInt(dayEnd);
    int monthAkhir = Integer.parseInt(monthEnd);
    int yearAkhir = Integer.parseInt(yearEnd);
    int day3 = dayNow + 3;
    int day7 = dayNow + 7;
    String day3kedepan = "";
    String day7kedepan = "";
    String monthEnd3 = "";
    String yearEnd3 = "";
    String monthEnd7 = "";
    String yearEnd7 = "";
    
    if(day3 > dayAkhir){
        
        day3kedepan = String.valueOf(day3 - dayAkhir);
        monthAkhir = monthAkhir + 1;
        if (monthAkhir > 12){
            monthEnd3 = "01";
            yearAkhir = yearAkhir + 1;
            yearEnd3 = String.valueOf(yearAkhir);
        } else {
            monthEnd3 = String.valueOf(monthAkhir);
            yearEnd3 = yearEnd;
        }
    } else {
        day3kedepan = String.valueOf(day3);
        monthEnd3 = monthEnd;
        yearEnd3 = yearEnd;
    }
    
    if(day7 > dayAkhir){
        day7kedepan = String.valueOf(day7 - dayAkhir);
        monthAkhir = monthAkhir + 1;
        if (monthAkhir > 12){
            monthEnd7 = "01";
            //yearAkhir = yearAkhir + 1;
            yearEnd7 = String.valueOf(yearAkhir);
        } else {
            monthEnd7 = String.valueOf(monthAkhir);
            yearEnd7 = yearEnd;
        }
    } else {
        day7kedepan = String.valueOf(day7);
        monthEnd7 = monthEnd;
        yearEnd7 = yearEnd;
    }

%>
<table  border="0" cellpadding="0" width="100%" border="1">
<tr>
    <td valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="15" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            <!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <%if (url != null && url.length() > 0 && url.equalsIgnoreCase("home")) {%>
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
                                if(MODUS_TRANSFER == MODUS_TRANSFER_SERVER && privApprovalKasirPayment==false && privApprovalInvoicing==false){
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
                        if(MODUS_TRANSFER == MODUS_TRANSFER_SERVER && privApprovalKasirPayment==false && privApprovalInvoicing==false){%>
                        <tr>
                            <td style='padding-left:12px'>  
                                <table>
                                    <tr>
                                        <td>
                        <table> 
                            <tr>
                                <%if( vecSizeOpnema != 0) {%>
                                    <td id="tdStyle">Document Opname dengan status Final = <b><%=vecSizeOpnema%> Document</b>.&nbsp;
                                        Silahkan lakukan koreksi stok terlebih dahulu sehingga posting bisa dilakukan <a href="<%=approot%>/warehouse/material/stock/mat_stock_correction_src.jsp"><blink>Click Here</blink></a>
                                    </td>
                                        
                                    <%}else{%>
                                       <td id="tdStyle">Tidak Ada Document Opname dengan status Final</td>
                                   <%}%>
                                   <td id="tdStyle">
                                       &nbsp;
                                   </td>
                            </tr>
                            <tr>
                                <td id="tdStyle">
                                    <b>1.&nbsp;Document Order Pembelian  <a href="<%=approot%>/purchasing/material/pom/srcpomaterial.jsp">( Click Here )</a></b>
                                    <%if((orderPembelianInformation != null) && (orderPembelianInformation.size() > 0)) {%>
                                        <%
                                        for (int i = 0; i < orderPembelianInformation.size(); i++) {
                                             Vector temp = (Vector)orderPembelianInformation.get(i);
                                        %>
                                        <div>Document Order Pembelian dengan Status <%if(i==0 && temp.get(1).equals(0)){%>
                                                draft
                                                <%}else{%>
                                                final
                                                <%}%>
                                                = <%=temp.get(0)%>&nbsp;Document
                                        </div>
                                        <%
                                        }
                                        %>

                                     <%}else{%>
                                     <div>Tidak Ada Document Order Pembelian</div>
                                    <%}%>
                                </td>
                                <td id="tdStyle">&nbsp;</td>
                            </tr>
                            <tr>
                                <td id="tdStyle">
                                    <b>2.&nbsp;Document Penerimaan dari Pembelian  <a href="<%=approot%>/warehouse/material/receive/src_receive_material.jsp">( Click Here )</a></b>
                                    <%if((ReceiveMaterialInformation != null) && (ReceiveMaterialInformation.size() > 0)) {%>
                                        <%
                                        for (int i = 0; i < ReceiveMaterialInformation.size(); i++) {
                                             Vector temp = (Vector)ReceiveMaterialInformation.get(i);
                                        %>
                                        <div>Document Penerimaan dari Pembelian dengan Status <%if(i==0  && temp.get(1).equals(0) ){%>
                                                draft
                                                <%}else{%>
                                                final
                                                <%}%>
                                                = <%=temp.get(0)%>&nbsp;Document
                                        </div>
                                        <%
                                        }
                                        %>

                                     <%}else{%>
                                        <div>Tidak Ada Document Penerimaan dari Pembelian</div>
                                    <%}%>
                                </td>
                                <td id="tdStyle">&nbsp;</td>
                            </tr>
                            <tr>
                                <td id="tdStyle">
                                    <b>3.&nbsp;Document Penerimaan dari Toko <a href="<%=approot%>/warehouse/material/receive/srcreceive_store_wh_material.jsp">( Click Here )</a></b>
                                    <%if((ReceiveMaterialNonSupplierInformation != null) && (ReceiveMaterialNonSupplierInformation.size() > 0)) {%>
                                    <%
                                    for (int i = 0; i < ReceiveMaterialNonSupplierInformation.size(); i++) {
                                         Vector temp = (Vector)ReceiveMaterialNonSupplierInformation.get(i);
                                    %>
                                    <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Document Penerimaan Dari Toko dengan Status <%if(i==0  && temp.get(1).equals(0) ){%>
                                            draft
                                            <%}else{%>
                                            final
                                            <%}%>
                                            = <%=temp.get(0)%>&nbsp;Document
                                    </div>
                                    <%
                                    }
                                    %>
                                <%}else{%>
                                    <div>Tidak Ada Document Penerimaan dari Toko</div>
                                <%}%>
                                </td>
                                <td id="tdStyle">&nbsp;</td>
                            </tr>
                            <tr>
                                <td id="tdStyle">
                                    <b>4.&nbsp;Document Transfer <a href="<%=approot%>/warehouse/material/dispatch/srcdf_stock_wh_material.jsp">( Click Here )</a></b>
                                    <%if((matDispatchInformation != null) && (matDispatchInformation.size() > 0)) {%>
                                    <%
                                    for (int i = 0; i < matDispatchInformation.size(); i++) {
                                         Vector temp = (Vector)matDispatchInformation.get(i);
                                    %>
                                    <div>Document Transfer dengan Status <%if(i==0  && temp.get(1).equals(0) ){%>
                                            draft
                                            <%}else{%>
                                            final
                                            <%}%>
                                            = <%=temp.get(0)%>&nbsp;Document
                                    </div>
                                    <%
                                    }
                                    %>
                                <%}else{%>
                                    <div> Tidak Ada Document Transfer</div>
                                <%}%>
                                </td>
                                <td id="tdStyle">&nbsp;</td>
                            </tr>
                            <tr>
                                <td id="tdStyle">
                                    <b>5.&nbsp;Document Transfer Unit <a href="<%=approot%>/warehouse/material/dispatch/srcdf_unit_wh_material.jsp">( Click Here )</a></b>
                                    <%if((matDispatchUnitInformation != null) && (matDispatchUnitInformation.size() > 0)) {%>
                                        <%
                                        for (int i = 0; i < matDispatchUnitInformation.size(); i++) {
                                             Vector temp = (Vector)matDispatchUnitInformation.get(i);
                                        %>
                                        <div>Document Transfer Unit dengan Status <%if(i==0  && temp.get(1).equals(0) ){%>
                                                draft
                                                <%}else{%>
                                                final
                                                <%}%>
                                                = <%=temp.get(0)%>&nbsp;Document
                                        </div>
                                        <%
                                        }
                                        %>
                                    <%}else{%>
                                    <div> Tidak Ada Document Transfer Unit</div>
                                    <%}%>
                                </td>
                                <td id="tdStyle">&nbsp;</td>
                            </tr>
                            <tr>
                                <td id="tdStyle">
                                    <b>6.&nbsp;Document Return <a href="<%=approot%>/warehouse/material/return/src_return_material.jsp">( Click Here )</a></b>
                                    <%if((returnInformation != null) && (returnInformation.size() > 0)) {%>
                                        <%
                                        for (int i = 0; i < returnInformation.size(); i++) {
                                             Vector temp = (Vector)returnInformation.get(i);
                                        %>
                                        <div>Document Return dengan Status <%if(i==0  && temp.get(1).equals(0) ){%>
                                                draft
                                                <%}else{%>
                                                final
                                                <%}%>
                                                = <%=temp.get(0)%>&nbsp;Document
                                        </div>
                                        <%
                                        }
                                        %>
                                    <%}else{%>
                                    <div> Tidak Ada Document Return</div>
                                    <%}%>
                                </td>
                                <td id="tdStyle">&nbsp;</td>
                            </tr>
                            <tr>
                                <td id="tdStyle">
                                    <b>7.&nbsp;Document Pembiayaan (Costing) <a href="<%=approot%>/warehouse/material/dispatch/srccosting_material.jsp">( Click Here )</a></b>
                                    <%if((pembiayaanInformation != null) && (pembiayaanInformation.size() > 0)) {%>
                                        <%
                                        for (int i = 0; i < pembiayaanInformation.size(); i++) {
                                             Vector temp = (Vector)pembiayaanInformation.get(i);
                                        %>
                                        <div>Document Pembiayaan (Costing) dengan Status <%if(i==0  && temp.get(1).equals(0) ){%>
                                                draft
                                                <%}else{%>
                                                final
                                                <%}%>
                                                = <%=temp.get(0)%>&nbsp;Document
                                        </div>
                                        <%
                                        }
                                        %>
                                    <%}else{%>
                                    <div>Tidak Ada Document Pembiayaan (Costing)</div>
                                    <%}%>
                                </td>
                                <td id="tdStyle">&nbsp;</td>
                            </tr>
                            <tr>
                                <td id="tdStyle">
                                    <%
                                    com.dimata.posbo.entity.search.SrcAccPayable srcAccPayable = new SrcAccPayable();
                                    String longcurr="504404261456952182";
                                    long oidcurr=Long.valueOf(longcurr);
                                    srcAccPayable.setCurrencyId(oidcurr);
                                    // update 2014-12-30 Hendra McHen
                                    
                                  
                                    String dateEnd3 = yearEnd3+"-"+monthEnd3+"-"+day3kedepan;
                                    String dateEnd7 = yearEnd7+"-"+monthEnd7+"-"+day7kedepan;
                                    
                                    int vectSizeHutang = SessAccPayable.getCountListAP(dateNow, dateNow);
                                    int vectSizeHutang3 = SessAccPayable.getCountListAP(dateNow, dateEnd3);
                                    int vectSizeHutang7 = SessAccPayable.getCountListAP(dateNow, dateEnd7);
                                    
                                    PstMemberReg pstMemberRegCount = new PstMemberReg();
                                    int vectSizePiutang = pstMemberRegCount.countListMember(dateNow, dateNow);
                                    int vectSizePiutang3 = pstMemberRegCount.countListMember(dateNow, dateEnd3);
                                    int vectSizePiutang7 = pstMemberRegCount.countListMember(dateNow, dateEnd7);
                                   %> 
                                   <b>8.&nbsp;Document Hutang <a href="<%=approot%>/arap/payable/payable_search.jsp">( Click Here )</a></b>
                                   <div>Document hutang <%=vectSizeHutang%> Document</div>
                                </td>
                                <td id="tdStyle">&nbsp;</td>
                            </tr>
                            <tr>
                                <td id="tdStyle">
                                    Untuk melakukan posting <a href="<%=approot%>/master/posting/posting_stock_new.jsp"><blink>Click Here</blink></a>
                                </td>
                                <td id="tdStyle">&nbsp;</td>
                            </tr>
                        </table>
                                        </td>
                                        <td valign='top'>
                                            <table>
                                                <tr>
                                                    <td id="tdStyle">
                                                       

                                                        
                                                        <b>Cek hutang</b>
                                       <form name="frmSrcOpname" method="post">
                                        <input type="hidden" name="command" value="1">
                                        <input type="hidden" id="date_kedepan" name="date_kedepan" value="" />
                                        <input type="hidden" id="batas_hari" name="batas_hari" value="<%=dayAkhir%>" />
                                           <input type="hidden" name="<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_SUPPLIER_NAME]%>" value="" size="40"> 
                                           <input type="hidden" name="<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_INVOICE_NUMBER]%>" value="" size="30">
                                           <input type="hidden" name="<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_CURRENCY_ID]%>" value="504404261456952182" size="30">
                                           <input type="hidden" name="<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_INVOICE_PAYMENT_DATE_STATUS]%>" value="1">
                                           
                                            <input type="hidden" name="<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_START_DATE]%>_mn" value="" />
                                            <input type="hidden" name="<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_START_DATE]%>_dy" value="" />
                                            <input type="hidden" name="<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_START_DATE]%>_yr" value="" />

                                            <input type="hidden" name="<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_END_DATE]%>_mn" value="" />
                                            <input type="hidden" name="<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_END_DATE]%>_dy" value="" />
                                            <input type="hidden" name="<%=FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_PAYMENT_END_DATE]%>_yr" value="" />
                                           
                                       <div>    
                                           <input type="hidden" id="start_mm_now" name="start_mm_now" value="<%=monthCurr%>" />
                                           <input type="hidden" id="start_dd_now" name="start_dd_now" value="<%=dayCurr%>" />
                                           <input type="hidden" id="start_yy_now" name="start_yy_now" value="<%=yearCurr%>" />
                                           <input type="hidden" id="end_mm_now" name="end_mm_now" value="<%=monthCurr%>" />
                                           <input type="hidden" id="end_dd_now" name="end_dd_now" value="<%=dayCurr%>" />
                                           <input type="hidden" id="end_yy_now" name="end_yy_now" value="<%=yearCurr%>" />
                                           Cek hutang hari ini (<%=vectSizeHutang%> document) - (<a href="javascript:cmdSearch()">Click here</a>)
                                       </div>
                                       <div>
                                           
                                           <input type="hidden" id="end_mm_3" name="end_mm_3" value="<%=monthEnd3%>" />
                                           <input type="hidden" id="end_dd_3" name="end_dd_3" value="<%=day3kedepan%>" />
                                           <input type="hidden" id="end_yy_3" name="end_yy_3" value="<%=yearEnd3%>" />
                                           Cek hutang 3 hari kedepan (<%=vectSizeHutang3%> document) - (<a href="javascript:cmdSearch3()">Click here</a>)
                                       </div>
                                       <div>
                                           
                                           <input type="hidden" id="end_mm_7" name="end_mm_7" value="<%=monthEnd7%>" />
                                           <input type="hidden" id="end_dd_7" name="end_dd_7" value="<%=day7kedepan%>" />
                                           <input type="hidden" id="end_yy_7" name="end_yy_7" value="<%=yearEnd7%>" />
                                           Cek hutang 7 hari kedepan (<%=vectSizeHutang7%> document) - (<a href="javascript:cmdSearch7()">Click here</a>)
                                       </div>
                                       </form>
                                                        
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td valign='top' id="tdStyle">
                                                        <b>Cek Piutang</b>
                                                        
                                                        <div>Cek piutang hari ini (<%=vectSizePiutang%> document) - (<a href="javascript:cmdSearchPiutang()">Click here</a>)</div>  
                                                        
                                                        <div>Cek piutang 3 hari kedepan (<%=vectSizePiutang3%> document) - (<a href="javascript:cmdSearchPiutang3()">Click here</a>)</div>
                                                        
                                                        <div>Cek piutang 7 hari kedepan (<%=vectSizePiutang7%> document) - (<a href="javascript:cmdSearchPiutang7()">Click here</a>)</div>
                                                        
                                                        
                                                    </td>
                                                </tr>
                                            </table>   
                                        </td>
                                    </tr>
                                </table>
                    </td>
                    </tr>
                        <%}else if(MODUS_TRANSFER == MODUS_TRANSFER_SERVER && privApprovalInvoicing==true) {%>
                            <%
                                    String whereClause =PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " = '0'"+
                                                          " AND cm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!=2"+
                                                          " AND cm."+PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING]+"!=2";
                                    BillMain bMain = new BillMain();
                                    bMain.setInvoiceNumber("");
                                    bMain.setCustomerId(0);
                                    bMain.setCustName("");
                                    bMain.setPersonName("");
                                    int vectSize = PstBillMain.getCount(0, bMain, 0, whereClause,"");
                              %>

                            <tr>
                              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>1.&nbsp;Pending Invoicing Sales <a href="<%=approot%>/cashieronline/src_list_open_bill.jsp">( Click Here )</a></b></td>
                            </tr>
                            <tr>
                              <%--document penerimaan dari pembelian--%>
                              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Document Sales Order <%=vectSize%> Document</td>
                            </tr>
                        <%}else{%>
                            <%
                                    SrcSaleReport srcSaleReport = new SrcSaleReport();
                                    srcSaleReport.setTransStatus(1);
                                    Vector records = SessAr.getArInvoice(0, 0, srcSaleReport);  
                            %>
                            <tr>
                              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>1.&nbsp;Piutang  <a href="<%=approot%>/cashieronline/src_list_open_bill.jsp">( Click Here )</a></b></td>
                            </tr>
                            <tr>
                              <%--document penerimaan dari pembelian--%>
                              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Piutang <%=records.size()%></td>
                            </tr>       
                        <%}%>
                        <tr>
                          <td>&nbsp;</td>
                        </tr>
                  <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("penjualan")) {%>
                      <div id="wrapperspace">&nbsp
                          <br>
                      </div>
                      <div id="wrapper-penjualan">
                          <%-- Invoicing --%>
                          <div id="column-center">
                               <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/2.Piutang.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Invoicing":"Invoicing"%></font></b>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <%if(privApprovalKasirPayment){%>
                                        <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3" color="#D3D3D3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Invoicing ":"Invoicing"%></font></b>
                                    <%}else{%>
                                        <a href="<%=approot%>/cashieronline/open_shift.jsp?typeListCashier=1" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Invoicing ":"Invoicing"%></font></b>
                                       </a>
                                    <%}%>
                               </div>
                               
                               <%--
                                <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <%if(privApprovalKasirPayment){%>
                                         <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3" color="#D3D3D3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Return Invoice":"Return Invoice"%></font></b>
                                    <%}else{%>
                                        <a href="<%=approot%>/cashieronline/open_shift.jsp?typeListCashier=2" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Return Invoice":"Return Invoice"%></font></b>
                                       </a>
                                    <%}%>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <%if(privApprovalKasirPayment){%>
                                         <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3" color="#D3D3D3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Void Invoice":"Void Invoice"%></font></b>
                                    <%}else{%>
                                        <a href="<%=approot%>/cashieronline/open_shift.jsp?typeListCashier=5" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Void Invoice":"Void Invoice"%></font></b>
                                       </a>
                                    <%}%>
                               </div>
                               --%>
                                <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <%if(privApprovalKasirPayment){%>
                                         <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3" color="#D3D3D3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Void & Return Invoice":"Void & Return  Invoice"%></font></b>
                                    <%}else{%>
                                        <a href="<%=approot%>/cashieronline/open_shift.jsp?typeListCashier=5" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Void & Return Invoice":"Void & Return  Invoice"%></font></b>
                                       </a>
                                    <%}%>
                               </div>
                          </div>
                          <%-- Cashier --%>
                          <div id="column-center">
                               <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/2.Piutang.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Cashier Payment":"Cashier Payment"%></font></b>
                               </div>
                               <%-- di RTC tidak menggunakan cash, semua ada membernya.
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                  <%if(privApprovalKasirPayment){%>
                                     <a href="<%=approot%>/cashieronline/open_shift.jsp?typeListCashier=1" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"List Cash Invoice":"List Cash Invoice"%></font></b>
                                     </a>
                                  <%}else{%>
                                     <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                     &nbsp;<b><font size="3" color="#D3D3D3" ><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"List Cash Invoice":"List Cash Invoice"%></font></b>
                                  <%}%>
                               </div>--%>
                                <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <%if(privApprovalKasirPayment){%>
                                        <a href="<%=approot%>/cashieronline/open_shift.jsp?typeListCashier=3" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"List Invoice":"List  Invoice"%></font></b>
                                       </a>
                                    <%}else{%>
                                            <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3" color="#D3D3D3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"List Invoice":"List Invoice"%></font></b>
                                    <%}%>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <%if(privApprovalKasirPayment){%>
                                        <a href="<%=approot%>/cashieronline/open_shift.jsp?typeListCashier=4" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Return Payment Invoice Cash":"Return Invoice Cash"%></font></b>
                                       </a>
                                   <%}else{%>
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3" color="#D3D3D3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Return Payment Invoice Cash":"Return Payment Invoice Cash"%></font></b>
                                   <%}%>
                               </div>
                          </div>
                          <%-- report --%>
                          <div id="column-center">
                               <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/1.Laporan.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Laporan":"Report"%></font></b>
                                  
                               </div>
                              <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/store/sale/report/src_reportsale_global.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Penjualan Global":"Sales Global"%></font></b>
                                   </a>
                               </div>
                              <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/store/sale/report/src_reportsale_detail_global.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Penjualan Detail":"Sales Detail"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/store/sale/report/src_reportsale_global_margin.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Gross Margin":"Gross Margin"%> </font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/store/sale/report/src_reportsalerekaptanggal.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Rekap Harian":"Daily Summary"%> </font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/store/sale/report/src_reportpendingorder.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Pending Order":"Pending Order"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/store/sale/report/src_reportsale.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Per Invoice":"By Invoice"%></font></b>
                                   </a>
                               </div>

                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/store/sale/report/src_reportsale_detail.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3">Invoice Detail</font></b>
                                   </a>
                               </div>
                              <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/store/sale/report/src_reportreturn.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3">Return Per Invoice</font></b>
                                   </a>
                               </div>
                                <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/store/sale/report/src_reportrekapkategori.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Rekapitulasi Report":"Rekapitulasi Report"%></font></b>
                                   </a>
                               </div>
                                <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/store/sale/report/src_reportrekap.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Rekap Per KSG":"Rekap Per KSG"%></font></b>
                                   </a>
                               </div>
                                <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/store/sale/report/src_report_cash_opening.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Per Cash Opening":"Per Cash Opening"%></font></b>
                                   </a>
                               </div>
                          </div>
                          
                          <%-- Piutang --%>
                          <div id="column-center2">
                               <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/2.Piutang.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Piutang":"A/R"%></font></b>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/store/sale/report/src_reportsalepaymentcredit.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Pelunasan Piutang":"A/R Payment"%></font></b>
                                   </a>
                               </div>
                                <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/store/sale/report/src_reportar.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Rekap Piutang":"A/R Summary"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/store/sale/report/src_rekap_payment.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Pembayaran Check":"Rekap Pembayaran"%></font></b>
                                   </a>
                               </div>
                          </div>
                      </div>
                  <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("pembelian")) {%>
                       <div id="wrapperspace">&nbsp
                          <br>
                      </div>
                      <div id="wrapper">
                          <div id="column-left">
                                <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/3.OrderPembelian.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Order Pembelian":"Purchase Order"%></font></b>
                                   
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/purchasing/material/pom/srcpomaterial.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Ubah Tambah":"Add/Update"%></font></b>
                                   </a>
                               </div>
                          </div>
                           <%if(typeOfBusiness.equals("2") || typeOfBusiness.equals("0")){%>
                          <div id="column-center">
                               <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/4.stok.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Minimum Stok":"Minimum Stock"%></font></b>
                                  
                               </div>
                                <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportstockmin.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Ubah/Tambah":"Update/Add"%></font></b>
                                   </a>
                               </div>
                          </div>
                          <%}%>
                          <div id="column-right">
                              <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/5.hutang.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Hutang":"A/P"%></font></b>
                                  
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/arap/payable/payable_search.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Rekap":"Summary"%></font></b>
                                   </a>
                               </div>
                                <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/arap/payable/ap_summary_search.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Rekap Detail":"Detail Summary"%></font></b>
                                   </a>
                               </div>
                          </div>  
                          <div id="column-center">
                             <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/3.PenerimaanPembelian.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Penerimaan Dari Pembelian":"Receive From Purchase Order"%></font></b>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/receive/src_receive_material.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Ubah/Tambah":"Update/Add"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Laporan":"Report"%></font></b>
                                
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportreceive_all.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;&nbsp;<b><font size="2"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Global":"Global"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportreceiveinvoice.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;&nbsp;<b><font size="2"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Per Nota":"By Receipt"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportreceive.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;&nbsp;<b><font size="2"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Per Supplier":"By Supplier"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportreceivekategori.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;&nbsp;<b><font size="2"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Per Kategori":"By Category"%></font></b>
                                   </a>
                               </div>
                          </div>   
                          <div id="column-center">
                               <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/3.PenerimaanPembelian.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Price Protection":"Price Protection"%></font></b>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/priceprotection/src_price_protection.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;&nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Ubah/Tambah":"Update/Add"%></font></b>
                                   </a>
                               </div>
                                   <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/warehouse/material/priceprotection/src_issue_price_protection.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;&nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Issue PP":"Issue PP"%></font></b>
                                       </a>
                                   </div>    
                          </div>    
                      </div>
                  <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("penerimaan")) {%>
                       <div id="wrapperspace">&nbsp
                          <br>
                      </div>
                      <div id="wrapper-gudang">
                          <div id="column-left-gudang">
                              <%--penerimaan--%>
                               <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/3.PenerimaanPembelian.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Penerimaan Dari Pembelian":"Receive From Purchase Order"%></font></b>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/receive/src_receive_material.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Ubah/Tambah":"Update/Add"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Laporan":"Report"%></font></b>
                                
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportreceive_all.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;&nbsp;<b><font size="2"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Global":"Global"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportreceiveinvoice.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;&nbsp;<b><font size="2"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Per Nota":"By Receipt"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportreceive.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;&nbsp;<b><font size="2"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Per Supplier":"By Supplier"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportreceivekategori.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;&nbsp;<b><font size="2"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Per Kategori":"By Category"%></font></b>
                                   </a>
                               </div>
                               <%-- --%>
                               <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/6.daritoko.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Penerimaan Dari Gudang":"Receive From Warehouse"%></font></b>
                               
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/receive/srcreceive_store_wh_material.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Ubah/Tambah":"Update/Add"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Laporan":"Report"%></font></b>
                              
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportreceiveinternal.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;&nbsp;<b><font size="2"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Global":"Global"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportreceiveinternalinvoice.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;&nbsp;<b><font size="2"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Per Nota":"By Receipt"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportreceiveinternalkategori.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;&nbsp;<b><font size="2"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Per Kategori":"By Category"%></font></b>
                                   </a>
                               </div>
                          </div>
                          <div id="column-center-gudang">
                              <%-- --%>
                              <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/9.transfer.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Transfer Ke Gudang":"Transfer"%></font></b>
                              
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/dispatch/srcdf_stock_wh_material.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Ubah / Tambah":"Update/Change"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">&nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Laporan":"Report"%></font></b>
                             
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportdispatch.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Semua":"All"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportdispatchinvoice.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Per Invoice":"By Invoice"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportdispatchsupplier.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Per Supplier":"By Supplier"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportdispatchkategori.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Per Kategori":"By Category"%></font></b>
                                   </a>
                               </div>
                               <%if(typeOfBusiness.equals("3")){%>
                                   <div align="left">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/9.transfer.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Pengiriman Barang":"Delivery Order"%></font></b>
                                   </div>
                                    <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/warehouse/material/dispatch/src_delivery_order_sales.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Pengiriman Barang":"Delivery Order"%></font></b>
                                       </a>
                                   </div>
                               <%}%>
                          </div>
                          <div id="column-right">
                              <%-- return--%>
                               <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/7.retur.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Return Dari Pembelian":"Return"%></font></b>
                               
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/return/src_return_material.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Ubah/Tambah":"Update/Add"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Laporan":"Report"%></font></b>
                               
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportreturninvoice.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;&nbsp;<b><font size="2"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Per Invoice":"By Invoice"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportreturn.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;&nbsp;<b><font size="2"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Per Supplier":"By Supplier"%></font></b>
                                   </a>
                               </div>
                               <%-- return--%>
                               <%if(typeOfBusiness.equals("3")){%>
                                   <div align="left">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/7.retur.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Return Dari Penjualan":"Return From Sale"%></font></b>
                                   </div>
                                   <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/warehouse/material/conreturn/src_return_from_sale.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Return Barang Dari Penjualan":"Return Barang Dari Penjualan"%></font></b>
                                       </a>
                                   </div>

                                   <div align="left">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/7.retur.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Replecement":"Replecement"%></font></b>
                                   </div>
                                   <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/warehouse/material/receive/src_replacement_pembelian.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Replacement":"Replacement"%></font></b>
                                       </a>
                                   </div>
                               <%}%>
                          </div>
                          <%--untuk masterdata --%>
                          <div id="column-right-masterdata">
                               <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/masterdata.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Masterdata":"Return"%></font></b>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/material/matcategory.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Kategori Barang":"Category"%></font></b>
                                       </a>
                               </div>
                               
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/material/matmerk.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=merkName%></font></b>
                                       </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/material/matunit.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Daftar Satuan":"Unit List"%></font></b>
                                       </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/material/srcmaterial.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Daftar Barang":"Items"%></font></b>
                                       </a>
                               </div>
                                <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/transferUnit.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Grouping Item":"Grouping Item"%></font></b>
                               </div>
                                <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/warehouse/material/dispatch/srcdf_unit_wh_material.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Ubah/Tambah":"Change/Add"%></font></b>
                                       </a>
                               </div>
                          </div>
                      </div>  
                  <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("pembiayaan")) {%>
                      <div id="wrapperspace">&nbsp
                          <br>
                      </div>
                      <div id="wrapper">
                          <div id="column-left">
                               <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/pembiayaan.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Pembiayaan":"Costing"%></font></b>
                               
                               </div>
                                <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/dispatch/srccosting_material.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Ubah/Tambah":"Update/Add"%></font></b>
                                   </a>
                               </div>
                          </div>
                          <div id="column-center">
                              <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/laporanPembiayaan.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Laporan":"Report"%></font></b>
                               
                               </div>
                                 <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportcosting.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Global":"Global"%></font></b>
                                   </a>
                               </div>
                                <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportcostingkategori.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Per Kategori":"By Category"%></font></b>
                                   </a>
                               </div>
                                 <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportcostinginvoice.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Per Nota":"By Receipt"%></font></b>
                                   </a>
                               </div>
                          </div>
                      </div>
                  <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("stock")) {%>
                      <div id="wrapperspace">&nbsp
                          <br>
                      </div>
                      <div id="wrapper">
                          <div id="column-left">
                            <div align="left">
                                   <img src="<%=approot%>/menustylesheet/icon_menu_pos/opname.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Opname":"Opname"%></font></b>
                            
                           </div>
                           <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/stock/mat_opname_src.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Ubah/Tambah":"Update/Add"%></font></b>
                                   </a>
                           </div>
                            <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/barcode/importtextfilesrc.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Opname (Used File)  ":"Opname (Used File) "%></font></b>
                                   </a>
                               </div>
                                <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/stock/mat_stock_correction_src.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Koreksi Stok":"Correction Stock"%></font></b>
                                   </a>
                               </div>
                          </div>
                          <div id="column-center">
                               <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/laporanStock.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Laporan":"Report"%></font></b>
                              
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportstock.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Global":"Global"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportstockkategori.jsp?tipe=1" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Per Kategori":"By Category"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportstocksupplier.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Per Supplier":"By Supplier"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_stockcard.jsp?type=1" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Kartu Stok":"Stock Card"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportposisistock.jsp?type=1" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Posisi Stok":"Stock Position"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/warehouse/material/report/src_reportselisihkoreksistok.jsp?type=1" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Selisih Koreksi Stok":"List Lost Correction Stock"%></font></b>
                                   </a>
                               </div>
                          </div>
                          <div id="column-right">
                              <%--
                              <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/posting.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Posting":"Posting"%></font></b>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/master/posting/posting_stock_new.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Posting Stok":"Posting Stock"%></font></b>
                                   </a>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/master/posting/srcmaterial_reposting_stock.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Re Posting Stok":"Re Posting Stock"%></font></b>
                                   </a>
                               </div>--%>
                          </div>
                      </div>
                  <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("masterdata")) {%>
                      <div id="wrapperspace">&nbsp
                          <br>
                      </div>
                      <div id="wrapper-masterdata">
                          <div id="column-left1">
                            <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/masterdata.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Master Data":"Master Data"%></font></b>
                               
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                   <a href="<%=approot%>/master/periode/period.jsp" class="menulink">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                       &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Daftar Periode":"Stock Period"%></font></b>
                                   </a>
                              </div>
                              <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/material/matcategory.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Kategori Barang":"Category"%></font></b>
                                       </a>
                              </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/material/matmerk.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=merkName%></font></b>
                                       </a>
                              </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/material/matunit.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Daftar Satuan":"Unit List"%></font></b>
                                       </a>
                              </div>
                                <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/location/location.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Daftar Lokasi":"Location List"%></font></b>
                                       </a>
                              </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/material/srcmaterial.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Daftar Barang":"Items"%></font></b>
                                       </a>
                              </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/material/src_update_harga.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Harga Barang":"Price Items"%></font></b>
                                       </a>
                              </div>         
                          </div>

                          <div id="column-center2">
                             <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_empety.png" align="absmiddle"><b><font size="5">&nbsp;</font></b>
                             
                             </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/contact/srccontactcompany.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Daftar Supplier":"Supplier List"%></font></b>
                                       </a>
                              </div>

                             <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/masterdata/pricetype.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Tipe Harga":"Price Type"%></font></b>
                                       </a>
                              </div>
                              <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/masterdata/discounttype.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Tipe Potongan":"Discount Type"%></font></b>
                                       </a>
                              </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/masterdata/membergroup.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Tipe Customer":"Member Type"%></font></b>
                                       </a>
                              </div>
                              <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/masterdata/srcmemberreg.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Daftar Member":"Member List"%></font></b>
                                       </a>
                              </div>
                             <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/masterdata/srcmemberreg_point.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Point Member":"Member Points"%></font></b>
                                       </a>
                              </div>

                          </div>
                          <div id="column-right3">
                             <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_empety.png" align="absmiddle"><b><font size="5">&nbsp;</font></b>
                                
                               </div>
                              <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/masterdata/currencytype.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Jenis Mata Uang":"Currency List"%></font></b>
                                       </a>
                              </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/masterdata/dailyrate_list.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Nilai Tukar Harian":"Daily Rate"%></font></b>
                                       </a>
                              </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/masterdata/standartrate.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Nilai Tukar Standart":"Standar Rate"%></font></b>
                                       </a>
                              </div>
                              <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/material/matcosting.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Tipe Biaya":"Costing Type"%></font></b>
                                       </a>
                              </div>
                              <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/payment/pay_system.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Sistem Pembayaran":"Payment System"%></font></b>
                                       </a>
                              </div>
                          </div>
                          <div id="column-right-two4">
                                 <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_empety.png" align="absmiddle"><b><font size="5">&nbsp;</font></b>
                              
                               </div>
                                <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/cashier/master_kasir.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Master Kasir":"Cashier Master"%></font></b>
                                       </a>
                              </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/masterdata/shift.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Shift Kerja":"Working Shift"%></font></b>
                                       </a>
                              </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/masterdata/sales.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Daftar Sales":"Salest List"%></font></b>
                                       </a>
                              </div>
                             <%if(typeOfBusiness.equals("2")){%>
                                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                           <a href="<%=approot%>/master/location/room.jsp" class="menulink">
                                               <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                               &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Daftar Ruangan":"Room List"%></font></b>
                                           </a>
                                  </div>
                                  <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                           <a href="<%=approot%>/master/location/tableroom.jsp" class="menulink">
                                               <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                               &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Daftar Meja":"Table List"%></font></b>
                                           </a>
                                  </div>
                               <%}%>
                           </div>
                           <div id="column-right3">
                               <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_empety.png" align="absmiddle"><b><font size="5">&nbsp;</font></b>
                               </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/location/country.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Country":"Country"%></font></b>
                                       </a>
                              </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/location/provinsi.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Provinsi":"Provinsi"%></font></b>
                                       </a>
                              </div>
                                <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/location/kabupaten.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Kota":"Kota"%></font></b>
                                       </a>
                              </div>
                              <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/location/kecamatan.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Area":"Area"%></font></b>
                                       </a>
                              </div>
                              <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/contact/relegion.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Agama":"Relegion"%></font></b>
                                       </a>
                              </div>
                           </div>
                      </div>
                  <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("sistem")) {%>
                      <div id="wrapperspace">&nbsp
                          <br>
                      </div>
                      <div id="wrapper">
                          <div id="column-left">
                                <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/sistem.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Sistem":"System"%></font></b>
                              
                               </div>
                                <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/system/userlist.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Pemakai":"Users"%></font></b>
                                       </a>
                              </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/system/grouplist.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Group":"Group"%></font></b>
                                       </a>
                              </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/system/privilegelist.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Hak Akses":"Privilege"%></font></b>
                                       </a>
                              </div>
                              <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/system/sysprop.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Pengaturan Aplikasi":"System Setting"%></font></b>
                                       </a>
                              </div>
                              <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/styletemplate/chage_template.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Modif Template":"Modif Template"%></font></b>
                                       </a>
                              </div>
                          </div>
                          <div id="column-center">
                                <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/tutup periode.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Tutup Periode":"Close Period"%></font></b>
                               
                               </div>
                                 <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/closing/closing_monthly.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Setting Tutup Periode":"Close Period"%></font></b>
                                       </a>
                              </div>
                          </div>
                          <div id="column-right">
                                <div align="left">
                                       <img src="<%=approot%>/menustylesheet/icon_menu_pos/transferdata.png" align="absmiddle">&nbsp;<b><font size="5"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Pertukaran Data":"Data Exchange"%></font></b>
                               
                               </div>
                              <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/DBconnection/connection.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Internet Connection":"Internet Connection"%></font></b>
                                       </a>
                              </div>
                                 <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/data_sync/transfer_data.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Transfer Ke Outlet":"Transfer To Outlet"%></font></b>
                                       </a>
                              </div>
                               <div align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                       <a href="<%=approot%>/master/data_sync/transfer_data_to_server.jsp" class="menulink">
                                           <img src="<%=approot%>/menustylesheet/icon_menu_pos/report_penjualan.png" align="absmiddle">
                                           &nbsp;<b><font size="3"><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Transfer Penjualan":"Sales Transfer"%></font></b>
                                       </a>
                              </div>
                          </div>
                      </div>
                   <%} else if (url != null && url.length() > 0 && url.equalsIgnoreCase("keluar")) {%>

                  <%}else{%>
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
