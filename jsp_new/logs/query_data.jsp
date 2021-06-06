<%-- 
    Document   : query_data
    Created on : Oct 19, 2016, 7:35:03 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.common.entity.periode.Periode"%>
<%@page import="com.dimata.common.entity.periode.PstPeriode"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@page import="java.util.Date"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcReportPotitionStock"%>
<%@page import="com.dimata.posbo.session.warehouse.SessReportPotitionStock"%>
<%@page import="com.dimata.posbo.entity.search.SrcReportPotitionStock"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.posbo.form.masterdata.CtrlMaterialStock"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.util.Formater"%>
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
    {"Tidak ada data", "LAPORAN POSISI STOK", "Dari", "s/d", "Periode", "Lokasi", "Merk", "Kategori", "Semua",
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
Date dateKoreksi = FRMQueryString.requestDate(request,"fromEndDate");
Date toDate = FRMQueryString.requestDate(request,"toDate");
String whereClause = "";
boolean isCategory = false;
boolean isSubCategory = false;
boolean isSupplier = false;

/**
 * handle current search data session
 */
try {
    srcReportPotitionStock = (SrcReportPotitionStock)session.getValue(SessReportPotitionStock.SESS_SRC_STOCK_POTITION_REPORT);
    srcReportPotitionStock.setDateFrom(toDate);
    srcReportPotitionStock.setDateTo(dateKoreksi);
} catch(Exception e) {
    srcReportPotitionStock = new SrcReportPotitionStock();
}
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">



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


</SCRIPT>


<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td valign="top" align="left"> 
        <div id="data">
            <style type="text/css">
                .tg  {border-collapse:collapse;border-spacing:0;margin:0px auto;}
                .tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}
                .tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:5px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}
                .tg .tg-baqh{text-align:center;vertical-align:top}
                .tg .tg-yw4l{vertical-align:top}
                @media screen and (max-width: 767px) {.tg {width: auto !important;}.tg col {width: auto !important;}.tg-wrap {overflow-x: auto;-webkit-overflow-scrolling: touch;margin: auto 0px;}}</style>

                <!-- #EndEditable --> 
                </head> 
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
               <tr> 
                   <td height="20" ><b>Berdasarkan Periode Pencarian</b></td>
              </tr>   
              <tr> 
                <td height="20" ><%=SessHistoriPosisiStok.queryPosisiStok(srcReportPotitionStock)%></td>
              </tr> 
              <tr> 
                  <td height="20" ><br><br><b>Berdasarkan Saldo Awal</b></td>
              </tr> 
              <tr> 
                <%
                    Vector vect = PstPeriode.list(0, 0, "", PstPeriode.fieldNames[PstPeriode.FLD_START_DATE]);
                    Date startDateFromSaldoAwal = (Date) srcReportPotitionStock.getDateFrom().clone();
                    if (vect != null && vect.size() > 0) {
                        Periode periode = (Periode) vect.get(0);
                        srcReportPotitionStock.setDateFrom(periode.getStartDate());
                    }
                    Date startMTD = (Date) startDateFromSaldoAwal.clone();
                    startMTD.setDate(startDateFromSaldoAwal.getDate()-1);
                    srcReportPotitionStock.setDateTo(startMTD);
                %>  
                <td height="20" ><%=SessHistoriPosisiStok.queryPosisiStok(srcReportPotitionStock)%></td>
              </tr> 
            </table>
      </div>                  
    </td>
  </tr> 
</table>
</body>
<!-- #EndTemplate --></html>
