<%-- 
    Document   : reportposisistock_list_histori_excel
    Created on : Feb 8, 2018, 9:59:33 AM
    Author     : dimata005
--%>

<%-- 
    Document   : reportposisistock_list_histori_html
    Created on : Oct 18, 2016, 11:51:05 PM
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

response.setContentType("application/x-msexcel"); 
response.setHeader("Content-Disposition","attachment; filename=" + "report_history_posisi_stock.xls" );      
    
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
    window.open("reportposisistock_list_1_html.jsp?report_title1=<%=textListGlobal[SESS_LANGUAGE][1]%>&report_title2=<%=reportTilte%>&FRM_FIELD_PRICE_TYPE_ID=<%=srcReportPotitionStock.getPriceTypeId()%>","form_stockpotitionreport");
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
   //alert("jajs");
   var locationId='<%=srcReportPotitionStock.getLocationId()%>';
   var date ='<%=Formater.formatDate(srcReportPotitionStock.getDateTo(),"dd-MM-yyyy")%>';
   var datefrom ='<%=Formater.formatDate(srcReportPotitionStock.getDateFrom(),"dd-MM-yyyy")%>';
   //alert("asa"+date);
   var strvalue  = "../report/src_stockcard.jsp?type=1&<%=FrmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_MATERIAL_ID]%>="+oidMat+"&txtkode="+txtkode+"&txtnama="+txtnama+"&locationId="+locationId+"&typeCheckKartu=3&fromEndDate="+date+"&toDate="+datefrom;
    winSrcMaterial = window.open(strvalue,"material", "height=600,width=800,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}
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
</head> 
<body >    
<table>
  <tr> 
    <td valign="top" align="left"> 
        <div id="data">
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
                        <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][5]).toUpperCase()%> : <%=location.getName().toUpperCase()%></b></td>
                        <td width="1%" height="14" valign="middle" class="command"></td>
                        <td width="89%" height="14" valign="middle" class="command"></td>
                      </tr>
                      <tr align="left" valign="top">
                        <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][7]).toUpperCase()%> : <%=category.getName().toUpperCase()%></b></td>
                        <td width="1%" height="14" valign="middle" class="command"></td>
                        <td width="89%" height="14" valign="middle" class="command"></td>
                      </tr>
                      <tr align="left" valign="top">
                        <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][6]).toUpperCase()%> : <%=merk.getName().toUpperCase()%></b></td>
                        <td width="1%" height="14" valign="middle" class="command"></td>
                        <td width="89%" height="14" valign="middle" class="command"></td>
                      </tr>
                      <tr align="left" valign="top">
                        <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][13]).toUpperCase()%> : 
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
                        </b></td>
                        <td width="1%" height="14" valign="middle" class="command"> </td>
                        <td width="89%" height="14" valign="middle" class="command"></td>
                      </tr>
                      <tr align="left" valign="top"> 
                        <td align="center" valign="middle" colspan="3">
                     <%if(true) {%>
                           <!-- list of report position stok -->
                           <table width="100%" class="listarea" >
                              <tr>
                                <td>
                                  <table width="100%" class="tg" border="1">
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
                                        <th class="tg-baqh" rowspan="2">HPP<br></th>
                                        <%
                                          Vector listCurrStandardX = PstStandartRate.listCurrStandard(0);  
                                          if(listTypeHrga != null && listTypeHrga.size()>0 && 0==0){
                                                for(int i = 0; i<listTypeHrga.size();i++){
                                                    for(int j=0;j<listCurrStandardX.size();j++){
                                                        %>
                                                             <th class="tg-baqh" rowspan="2">Harga Jual<br></th>
                                                        <%
                                                    }
                                                }
                                            }
                                          %>
                                        <th class="tg-baqh" rowspan="2">Unit</th>
                                        <th class="tg-baqh" rowspan="2">Saldo Awal<br></th>
                                        <th class="tg-baqh" colspan="4">Inventory In<br></th>
                                        <th class="tg-baqh" colspan="5">Inventory Out<br></th>
                                        <th class="tg-baqh" rowspan="2">Subtotal</th>
                                        <th class="tg-baqh" rowspan="2">Opname</th>
                                        <th class="tg-baqh" colspan="2">Selisih Opname<br></th>
                                        <th class="tg-baqh" rowspan="2">Balance</th>
                                        <th class="tg-baqh" rowspan="2">Nilai<br>Stok (HPP)</th>
                                      </tr>
                                      <tr>
                                        <td class="tg-baqh">Rec<br>Supp<br></td>
                                        <td class="tg-baqh">Rec<br>Warehouse<br></td>
                                        <td class="tg-baqh">Return<br>Sales<br></td>
                                        <td class="tg-baqh">Total <br>In<br></td>
                                        <td class="tg-baqh">Transf.</td>
                                        <td class="tg-baqh">Return<br>Supp<br></td>
                                        <td class="tg-baqh">Sale</td>
                                        <td class="tg-baqh">Cost<br></td>
                                        <td class="tg-baqh">Total<br>Out<br></td>
                                        <td class="tg-baqh">(+)</td>
                                        <td class="tg-baqh">(-)</td>
                                      </tr>
                                      <% SessHistoriPosisiStok.getReportStockAll(out,srcReportPotitionStock);%>
                                    </table>    
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
          Note : Saldo Awal adalah perhitungan stok dari awal periode sampai dengan 1 hari sebelum tanggal awal pencarian<br>
                 tanda (-) artinya item tersebut tidak ada pembuatan dokument baik penerimaan ataupun penjualan pada tanggal pencarian
      </td>
  </tr> 
</table>
</body>
<!-- #EndTemplate --></html>
