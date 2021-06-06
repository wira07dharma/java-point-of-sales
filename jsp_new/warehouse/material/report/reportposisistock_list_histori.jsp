<%-- 
    Document   : reportposisistock_list_histori
    Created on : Oct 14, 2016, 12:15:39 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.posbo.session.warehouse.SessHistoriPosisiStok"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcStockCard"%>
<%@page import="com.dimata.hanoman.entity.masterdata.Contact"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstContact"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.common.entity.payment.PstStandartRate"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@ page import="com.dimata.posbo.entity.search.SrcReportPotitionStock,
                 com.dimata.posbo.form.search.FrmSrcReportPotitionStock,
                 com.dimata.posbo.session.warehouse.SessReportPotitionStock,
                 com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.util.Command,
                 com.dimata.posbo.entity.masterdata.Material,
                 com.dimata.posbo.entity.masterdata.MaterialStock,
                 com.dimata.posbo.entity.masterdata.Unit,
                 com.dimata.posbo.entity.masterdata.Merk,
                 com.dimata.posbo.entity.masterdata.PstMerk,
                 com.dimata.posbo.form.masterdata.CtrlMaterialStock"%>
<%@ page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@ page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_POTITION); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
public static final String textListGlobal[][] = {
    {"Tidak ada data", "LAPORAN HISTORI POSISI STOK", "Dari", "s/d", "Periode", "Lokasi", "Merk", "Kategori", "Semua",
             "Laporan Posisi Stock", "Cetak Laporan Posisi Stock", "Berdasarkan", "Nilai Stok dari","Supplier"},
    {"No data available", "STOCK POSITION REPORT", "From", "to", "Periode", "Location", "Merk", "Category", "All",
             "Stock Potition Report", "Print Stock Potition Report", "By", "Stock Value from","Supplier"}
};

public static final String textListTitleTable[][] = {
    {"NO","SKU","NAMA BARANG","HARGA BELI","UNIT","JUMLAH STOK","AWAL","OPNAME","TERIMA","TRANSFER","RETUR SUPP.","JUAL","RETUR CUST.","SALDO","NILAI STOK","HARGA JUAL","BARCODE","BIAYA"},
    {"NO","SKU","GOODS NAME","COGS","UNIT","QUANTITY STOCK","BEGINNING","OPNAME","RECEIVE","DISPATCH","SUPP. RETURN","SALE","CUST. RETURN","SALDO","STOCK VALUE","SALE PRICE","BARCODE","COST"}
};
%>

<%
/**
 * instantiate some object used in this page
 */
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int type = FRMQueryString.requestInt(request, "type");
CtrlMaterialStock ctrlMaterialStock = new CtrlMaterialStock(request);
ControlLine ctrLine = new ControlLine();
SrcReportPotitionStock srcReportPotitionStock = new SrcReportPotitionStock();
SessReportPotitionStock objSessReportPotitionStock = new SessReportPotitionStock();
FrmSrcReportPotitionStock frmSrcReportPotitionStock = new FrmSrcReportPotitionStock(request, srcReportPotitionStock);

String whereClause = "";
boolean isCategory = false;
boolean isSubCategory = false;
boolean isSupplier = false;

/**
 * handle current search data session
 */
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
    try {
        srcReportPotitionStock = (SrcReportPotitionStock)session.getValue(SessReportPotitionStock.SESS_SRC_STOCK_POTITION_REPORT);
        if (srcReportPotitionStock == null) srcReportPotitionStock = new SrcReportPotitionStock();
    } catch(Exception e) {
        srcReportPotitionStock = new SrcReportPotitionStock();
    }
} else {
    frmSrcReportPotitionStock.requestEntityObject(srcReportPotitionStock);
    session.putValue(SessReportPotitionStock.SESS_SRC_STOCK_POTITION_REPORT, srcReportPotitionStock);
}

Location location = new Location();
if (srcReportPotitionStock.getLocationId() != 0) {
    try	{
        location = PstLocation.fetchExc(srcReportPotitionStock.getLocationId());
    } catch(Exception e) {
        System.out.println(e.toString());
    }
} else {
    location.setName(textListGlobal[SESS_LANGUAGE][8]+" "+textListGlobal[SESS_LANGUAGE][5]);
}

Category category = new Category();
if(srcReportPotitionStock.getCategoryId() != 0) {
    try {
        category = PstCategory.fetchExc(srcReportPotitionStock.getCategoryId());
    } catch(Exception e) {
        System.out.println(e.toString());
    }
} else {
    category.setName(textListGlobal[SESS_LANGUAGE][8]+" "+textListGlobal[SESS_LANGUAGE][7]);
}

Merk merk = new Merk();
if(srcReportPotitionStock.getMerkId() != 0) {
    try {
        merk = PstMerk.fetchExc(srcReportPotitionStock.getMerkId());
    } catch(Exception e) {
        System.out.println(e.toString());
    }
} else {
    merk.setName(textListGlobal[SESS_LANGUAGE][8]+" "+textListGlobal[SESS_LANGUAGE][6]);
}

//proses pengambilan userid dilakukan di javainit
srcReportPotitionStock.setUserId(userId);

/**
 * START GET POTITION STOCK REPORT
 * Prosedur proses mendapatkan laporan posisi stock
 * 1. Rekap data seluruh transaksi
 * 2. Mengambil nilai stok dan jumlah list dari data hasil rekap
 * 3. Mengambil list data hasil rekap sesuai dengan permintaan ControlList
 */
/** get report title */
String reportTilte = "";
if(srcReportPotitionStock.getInfoShowed() == SrcReportPotitionStock.SHOW_QTY_ONLY) {
    reportTilte = (textListGlobal[SESS_LANGUAGE][11]+" "+SrcReportPotitionStock.stringInfoShowed[SESS_LANGUAGE][SrcReportPotitionStock.SHOW_QTY_ONLY]);
} else if(srcReportPotitionStock.getInfoShowed() == SrcReportPotitionStock.SHOW_VALUE_ONLY) {
    reportTilte = (textListGlobal[SESS_LANGUAGE][11]+" "+textListGlobal[SESS_LANGUAGE][12]);
    if(srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
        reportTilte += (" "+SrcReportPotitionStock.stringStockValueBy[SESS_LANGUAGE][SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER]);
    } else if(srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_TRANSACTION) {
        reportTilte += (" "+SrcReportPotitionStock.stringStockValueBy[SESS_LANGUAGE][SrcReportPotitionStock.STOCK_VALUE_BY_COGS_TRANSACTION]);
    }
}

    
    
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function cmdBack() {
    document.frm_reportstock.command.value="<%=Command.BACK%>";
    document.frm_reportstock.action="src_reportposisistock_history.jsp";
    document.frm_reportstock.submit();
}

function printForm() {
    //window.open("reportstock_form_print.jsp","stockposisireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
    window.open("buff_pdf_stockpotitionreport.jsp?report_title1=<%=textListGlobal[SESS_LANGUAGE][1]%>&report_title2=<%=reportTilte%>","form_stockpotitionreport");
}

function printFormExcel() {
    //window.open("reportstock_form_print.jsp","stockposisireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
    window.open("reportposisistock_list_1_excel.jsp?report_title1=<%=textListGlobal[SESS_LANGUAGE][1]%>&report_title2=<%=reportTilte%>&FRM_FIELD_PRICE_TYPE_ID=<%=srcReportPotitionStock.getPriceTypeId()%>","form_stockpotitionreport");
}

function printFormHtmlx() {
    //window.open("reportstock_form_print.jsp","stockposisireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
    var date ='<%=Formater.formatDate(srcReportPotitionStock.getDateTo(),"dd-MM-yyyy")%>';
    var datefrom ='<%=Formater.formatDate(srcReportPotitionStock.getDateFrom(),"dd-MM-yyyy")%>';
    window.open("reportposisistock_list_histori_html.jsp?report_title1=<%=textListGlobal[SESS_LANGUAGE][1]%>&report_title2=<%=reportTilte%>&FRM_FIELD_PRICE_TYPE_ID=<%=srcReportPotitionStock.getPriceTypeId()%>&fromEndDate="+date+"&toDate="+datefrom+"","form_stockpotitionreport");
}

function printFormExcel() {
    //window.open("reportstock_form_print.jsp","stockposisireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
    var date ='<%=Formater.formatDate(srcReportPotitionStock.getDateTo(),"dd-MM-yyyy")%>';
    var datefrom ='<%=Formater.formatDate(srcReportPotitionStock.getDateFrom(),"dd-MM-yyyy")%>';
    window.open("reportposisistock_list_histori_excel.jsp?report_title1=<%=textListGlobal[SESS_LANGUAGE][1]%>&report_title2=<%=reportTilte%>&FRM_FIELD_PRICE_TYPE_ID=<%=srcReportPotitionStock.getPriceTypeId()%>&fromEndDate="+date+"&toDate="+datefrom+"","form_stockpotitionreport");
}
//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
function cmdListFirst() {
    document.frm_reportstock.command.value="<%=Command.FIRST%>";
    document.frm_reportstock.action="reportposisistock_list_1.jsp";
    document.frm_reportstock.submit();
}

function cmdListPrev() {
    document.frm_reportstock.command.value="<%=Command.PREV%>";
    document.frm_reportstock.action="reportposisistock_list_1.jsp";
    document.frm_reportstock.submit();
}

function cmdListNext() {
    document.frm_reportstock.command.value="<%=Command.NEXT%>";
    document.frm_reportstock.action="reportposisistock_list_1.jsp";
    document.frm_reportstock.submit();
}

function cmdListLast() {
    document.frm_reportstock.command.value="<%=Command.LAST%>";
    document.frm_reportstock.action="reportposisistock_list_1.jsp";
    document.frm_reportstock.submit();
}

function cmdViewKartuStock(oidMat,txtkode,txtnama){
   var locationId='<%=srcReportPotitionStock.getLocationId()%>';
   var date ='<%=Formater.formatDate(srcReportPotitionStock.getDateTo(),"dd-MM-yyyy")%>';
   var datefrom ='<%=Formater.formatDate(srcReportPotitionStock.getDateFrom(),"dd-MM-yyyy")%>';
   var strvalue  = "../report/src_stockcard.jsp?type=1&<%=FrmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_MATERIAL_ID]%>="+oidMat+"&txtkode="+txtkode+"&txtnama="+txtnama+"&locationId="+locationId+"&typeCheckKartu=3&fromEndDate="+date+"&toDate="+datefrom;
    winSrcMaterial = window.open(strvalue,"material", "height=600,width=800,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}
}

function cmdQuery(){
    var date ='<%=Formater.formatDate(srcReportPotitionStock.getDateTo(),"dd-MM-yyyy")%>';
    var datefrom ='<%=Formater.formatDate(srcReportPotitionStock.getDateFrom(),"dd-MM-yyyy")%>';
    window.open("../../../logs/query_data.jsp?report_title1=<%=textListGlobal[SESS_LANGUAGE][1]%>&report_title2=<%=reportTilte%>&FRM_FIELD_PRICE_TYPE_ID=<%=srcReportPotitionStock.getPriceTypeId()%>&fromEndDate="+date+"&toDate="+datefrom+"","form_stockpotitionreport");

}
//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
 
function MM_preloadImages() { //v3.0
    var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.0
    var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
    if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
    for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
    if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
    var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
    if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

//-->
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<SCRIPT language=JavaScript>
function hideObjectForMarketing(){    
} 
	 
function hideObjectForWarehouse(){ 
}
	
function hideObjectForProduction(){
}
	
function hideObjectForPurchasing(){
}

function hideObjectForAccounting(){
}

function hideObjectForHRD(){
}

function hideObjectForGallery(){
}

function hideObjectForMasterData(){
}


function cmdViewKartuStock(oidMat,txtkode,txtnama){
   //alert("jajs");
   var locationId='<%=srcReportPotitionStock.getLocationId()%>';
   var date ='<%=Formater.formatDate(srcReportPotitionStock.getDateTo(),"dd-MM-yyyy")%>';
   var datefrom ='<%=Formater.formatDate(srcReportPotitionStock.getDateFrom(),"dd-MM-yyyy")%>';
   //alert("asa"+date);
   var strvalue  = "../report/src_stockcard.jsp?type=1&<%=FrmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_MATERIAL_ID]%>="+oidMat+"&txtkode="+txtkode+"&txtnama="+txtnama+"&locationId="+locationId+"&typeCheckKartu=3&fromEndDate="+date+"&toDate="+datefrom;
    winSrcMaterial = window.open(strvalue,"material", "height=600,width=800,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}
}

</SCRIPT>
<%--
<script type="text/javascript">
    function printFormHtml(){
            var data = document.getElementById("data").innerHTML;
            var mywindow = window.open('', 'title Here', 'height=400,width=600');
            //mywindow.document.open().write('application/x-msexcel');
            mywindow.document.write(data);
    };
</script>
--%>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <%if(menuUsed == MENU_PER_TRANS){%>
  <tr>
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
      <%@ include file = "../../../main/header.jsp" %>
      <!-- #EndEditable --></td>
  </tr>
  <tr>
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
  <%}else{%>
   <tr bgcolor="#FFFFFF">
    <td height="10" ID="MAINMENU">
      <%@include file="../../../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
        <div id="data">
            <style type="text/css">
                .tg {border-collapse:collapse;border-spacing:0;margin:0px auto;}
                .tg td{font-family:Arial, sans-serif;font-size:12px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}
                .tg th{font-family:Arial, sans-serif;font-size:12px;font-weight:normal;padding:5px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}
                .tg .tg-baqh{text-align:center;vertical-align: middle; background-color: #DDD; font-weight: bold}
                .tg .tg-yw4l{vertical-align:top}
                @media screen and (max-width: 767px) {.tg {width: auto !important;}.tg col {width: auto !important;}.tg-wrap {overflow-x: auto;-webkit-overflow-scrolling: touch;margin: auto 0px;}}</style>


                <!-- #EndEditable --> 
                </head> 
            <table width="100%" border="0" cellspacing="0" cellpadding="0">  
              <tr> 
                <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --><!-- #EndEditable --></td>
              </tr>
              <tr> 
                <td><!-- #BeginEditable "content" -->
                  <form name="frm_reportstock" method="post" action="">
                    <input type="hidden" name="command" value="">
                    <input type="hidden" name="add_type" value="">
                    <input type="hidden" name="start" value="<%=start%>">
                    <input type="hidden" name="type" value="<%=type%>">
                    <input type="hidden" name="approval_command">
                    <table width="100%" cellspacing="0" cellpadding="3">
                      <tr align="left" valign="top">
                        <td height="14" colspan="3" align="center" valign="middle">
                          <h4><strong><%=textListGlobal[SESS_LANGUAGE][1]%><br>
                          <% if(srcReportPotitionStock.getInfoShowed() != SrcReportPotitionStock.SHOW_BOTH) { out.println("("+reportTilte+")"); } %>
                          </strong></h4>
                        </td>
                      </tr>
                      <tr align="left" valign="top"> 
                        <td width="10%" height="14" valign="middle" class="command"><b>HISTORI <%=(textListGlobal[SESS_LANGUAGE][4]).toUpperCase()%></b></td>
                        <td width="1%" height="14" valign="middle" class="command"><strong>:</strong></td>
                        <td width="89%" height="14" valign="middle" class="command">
                          <%=Formater.formatDate(srcReportPotitionStock.getDateFrom(), "dd-MM-yyyy")%> <%=textListGlobal[SESS_LANGUAGE][3]%> <%=Formater.formatDate(srcReportPotitionStock.getDateTo(), "dd-MM-yyyy")%>
                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][5]).toUpperCase()%></b></td>
                        <td width="1%" height="14" valign="middle" class="command"><strong>:</strong></td>
                        <td width="89%" height="14" valign="middle" class="command"><%=location.getName().toUpperCase()%></td>
                      </tr>
                      <tr align="left" valign="top">
                        <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][7]).toUpperCase()%></b></td>
                        <td width="1%" height="14" valign="middle" class="command"><strong>:</strong></td>
                        <td width="89%" height="14" valign="middle" class="command"><%=category.getName().toUpperCase()%></td>
                      </tr>
                      <tr align="left" valign="top">
                        <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][6]).toUpperCase()%></b></td>
                        <td width="1%" height="14" valign="middle" class="command"><strong>:</strong></td>
                        <td width="89%" height="14" valign="middle" class="command"><%=merk.getName().toUpperCase()%></td>
                      </tr>
                      <tr align="left" valign="top">
                        <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][13]).toUpperCase()%></b></td>
                        <td width="1%" height="14" valign="middle" class="command"><strong>:</strong></td>
                        <td width="89%" height="14" valign="middle" class="command">
                            <%
                                String name=" Semua Supplier ";
                                if(srcReportPotitionStock.getSupplierId()!=0){
                                    try{
                                        Contact contact = PstContact.fetchExc(srcReportPotitionStock.getSupplierId());
                                        name=contact.getCompName();
                                    }catch(Exception ex){
                                    }
                                }
                            %>
                            <%=name.toUpperCase()%>
                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td width="10%" height="14" valign="middle" class="command"><b>TYPE VIEW</b></td>
                        <td width="1%" height="14" valign="middle" class="command"><strong>:</strong></td>
                        <td width="89%" height="14" valign="middle" class="command">
                        <%
                            boolean loopNext = true;
                            String view="";
                            int typeDeadStok= srcReportPotitionStock.getViedDeadStok();
                            if(typeDeadStok!=SessHistoriPosisiStok.VIEW_DEAD_STOK_ALL){
                                if(typeDeadStok==SessHistoriPosisiStok.VIEW_DEAD_STOK_NO_MOVEMENT){
                                    view="ITEM YANG TIDAK PERNAH ADA TRANSAKSI APAPUN";
                                }else if(typeDeadStok==SessHistoriPosisiStok.VIEW_DEAD_STOK_YES_MOVEMENT_NO_SALE){
                                    view="ITEM YANG TIDAK ADA PENJUALAN";
                                }else if (typeDeadStok==SessHistoriPosisiStok.VIEW_DEAD_STOK_NO_MOVE_AND_YES_MOVE_NO_SALE){
                                    view="HANYA ITEM YANG ADA TRANSAKSI";
                                }
                            }else{
                                view="SEMUA ITEM";
                            }

                        %>
                        <%=view%>
                        </td>
                      </tr>
                      <tr align="left" valign="top"> 
                        <td align="center" valign="middle" colspan="3">
                     <%if(true) {%>
                           <!-- list of report position stok -->
                           <table width="100%" class="listarea">
                              <tr>
                                <td>
                                  <table width="100%" class="tg" border="0">
                                      <%
                                          Vector vectMember = new Vector(1,1);
                                          String[] strMember = null;
                                          Vector listTypeHrga =  new Vector ();
                                          strMember = request.getParameterValues("FRM_FIELD_PRICE_TYPE_ID");
                                          String sStrMember="";
                                          int colspan = 9;
                                          if(strMember!=null && strMember.length>0) {
                                                 for(int i=0; i<strMember.length; i++) {
                                                         try {
                                                             if(strMember[i] != null && strMember[i].length()>0){ 
                                                              vectMember.add(strMember[i]);
                                                             sStrMember=sStrMember+strMember[i]+",";
                                                             }
                                                         }
                                                         catch(Exception exc) {
                                                                 System.out.println("err");
                                                         }
                                                 }
                                                 if(sStrMember != null && sStrMember.length()>0){
                                                      sStrMember=sStrMember.substring(0, sStrMember.length()-1);
                                                      String whereClauses = PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID]+ " IN("+sStrMember+")";
                                                      listTypeHrga =  PstPriceType.list(0, 0, whereClauses, "");
                                                 }
                                                 srcReportPotitionStock.setvPriceTypeId(listTypeHrga);
                                                 colspan=colspan;
                                          }
                                      %>  

                                      <tr>
                                        <th class="tg-baqh" rowspan="2">No</th>
                                        <th class="tg-baqh" rowspan="2">SKU</th>
                                        <th class="tg-baqh" rowspan="2">Barcode</th>
                                        <th class="tg-baqh" rowspan="2">Nama</th>
                                        <th class="tg-baqh" rowspan="2">HPP</th>
                                        <%
                                          Vector listCurrStandardX = PstStandartRate.listCurrStandard(0);  
                                          if(listTypeHrga != null && listTypeHrga.size()>0 && 0==0){
                                                for(int i = 0; i<listTypeHrga.size();i++){
                                                    for(int j=0;j<listCurrStandardX.size();j++){
                                                        %>
                                                             <th class="tg-baqh" rowspan="2">Harga Jual</th>
                                                        <%
                                                    }
                                                }
                                            }
                                          %>
                                        <th class="tg-baqh" rowspan="2">Unit</th>
                                        <th class="tg-baqh" rowspan="2">Saldo Awal</th>
                                        <th class="tg-baqh" colspan="5">Inventory In</th>
                                        <th class="tg-baqh" colspan="6">Inventory Out</th>
                                        <th class="tg-baqh" rowspan="2">Subtotal</th>
                                        <th class="tg-baqh" rowspan="2">Opname</th>
                                        <th class="tg-baqh" colspan="2">Selisih Opname</th>
                                        <th class="tg-baqh" rowspan="2">Balance</th>
                                        <th class="tg-baqh" rowspan="2">Nilai Stok (HPP)</th>
                                      </tr>
                                      <tr>
                                        <td class="tg-baqh">Rec. Supp</td>
                                        <td class="tg-baqh">Rec. WH</td>
                                        <td class="tg-baqh">Ret. Sales</td>
                                        <td class="tg-baqh">Prod. In</td>
                                        <td class="tg-baqh">Total In</td>
                                        <td class="tg-baqh">Transf.</td>
                                        <td class="tg-baqh">Ret. Supp.</td>
                                        <td class="tg-baqh">Sale</td>
                                        <td class="tg-baqh">Cost</td>
                                        <td class="tg-baqh">Prod. Out</td>
                                        <td class="tg-baqh">Total Out</td>
                                        <td class="tg-baqh">(+)</td>
                                        <td class="tg-baqh">(-)</td>
                                      </tr>
                                      <% SessHistoriPosisiStok.getReportStockAll(out,srcReportPotitionStock);%>
                                </td>
                              </tr>
                          </table>
                           <!-- End of list report position stock -->
                        </td>
                      </tr>
                      <%}else {%>
                      <%//}%>
                      <!-- Command If summary failed -->
                      <tr>
                          <td>
                            <table width="99%" class="listarea">
                              <tr>
                                <td width="99%" align ="center">Process Failed...</td>
                              <tr>
                            </table>
                          </td>
                      </tr>
                      <%}%>
                      <!--End Of Command If summary failed -->
                      <%--
                      <tr align="left" valign="top"> 
                        <td height="8" align="left" colspan="3" class="command"> 
                          <span class="command">
                            <%
                                  ctrLine.setLocationImg(approot+"/images");
                                  ctrLine.initDefault();
                                  out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
                            %>
                          </span>
                        </td>
                      </tr>
                      --%>
                      
                    </table>
                  </form>
                  <!-- #EndEditable --></td> 
              </tr> 
            </table>
      </div>                  
    </td>
  </tr>
  <tr align="left" valign="top">
      <td height="18" valign="top" colspan="3">
          <b>Note :</b>
          <p>
              Saldo Awal adalah perhitungan stok dari awal periode sampai dengan 1 hari sebelum tanggal awal pencarian<br>
              Balance = Saldo Awal + Total In - Total Out + ( Selisih Opname)
          </p>
          <br>
      </td>
  </tr> 
  <tr align="left" valign="top"> 
                        <td height="18" valign="top" colspan="3"> <table width="100%" border="0">
                            <tr> 
                              <td width="80%"> <table width="55%" border="0" cellspacing="0" cellpadding="0">
                                  <tr> 
                                    <!--td nowrap width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][9],ctrLine.CMD_BACK_SEARCH,true)%>"></a></td-->
                                    <td nowrap width="2%">&nbsp;</td>
                                    <td class="command" nowrap width="92%"><a class="btn btn-primary" href="javascript:cmdBack()"><i class="fa fa-arrow-left"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][9],ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                                  </tr>
                                </table></td>
                              <td width="20%">
                              <% //if(vctReportStock.size() != 0) { %>
                              <% if(true) { %>
                                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                  <tr> 
                                    <!--td width="5%" valign="top"><a href="javascript:printFormHtmlx()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][10]%>"></a></td-->
                                    <td width="35%" nowrap>&nbsp; <a class="btn btn-primary" href="javascript:printFormHtmlx()" class="command" ><i class="fa fa-print"></i> <%=textListGlobal[SESS_LANGUAGE][10]%> Html</a></td>  
                                    <td width="35%" nowrap>&nbsp; <a class="btn btn-primary" href="javascript:printFormExcel()" class="command" ><i class="fa fa-print"></i>Export Excel</a></td>  
                                    <%if(MODUS_APPLICATION_MODE==MODE_TESTING){%>
                                             <!--td width="5%" valign="top"><a href="javascript:cmdQuery()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][10]%>"></a></td-->
                                            <td width="35%" nowrap>&nbsp; <a class="btn btn-primary" href="javascript:cmdQuery()" class="command" ><i class="fa fa-print"></i> Query For Implementator</td>  
                                    <%}%>
                                    <%--  
                                    <td width="5%" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][10]%>"></a></td>
                                    <td width="25%" nowrap>&nbsp; <a href="javascript:printForm()" class="command" ><%=textListGlobal[SESS_LANGUAGE][10]%></a></td>
                                    
                                    <td width="5%" valign="top"><a href="javascript:printFormExcel()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][10]%>"></a></td>
                                    <td width="25%" nowrap>&nbsp; <a href="javascript:printFormExcel()" class="command" ><%=textListGlobal[SESS_LANGUAGE][10]%> Excel</a></td>
                                    --%>
                                  </tr>
                                </table>
                              <% } %></td>
                            </tr>
                          </table>
                        </td>
                      </tr>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
      <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../../styletemplate/footer.jsp" %>
        <%}else{%>
            <%@ include file = "../../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
