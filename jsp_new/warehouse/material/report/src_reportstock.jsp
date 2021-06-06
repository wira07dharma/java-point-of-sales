<%@page import="com.dimata.common.entity.payment.PriceType"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_REPORT); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
	{"Stok","Laporan","Global","Pencarian","Tidak ada data","Laporan Stok","Cetak Laporan Stok"},
	{"Stock","Report","Global","Searching","No stock available","Stock Report","Print Stock Report"}
};

/* this constant used to list text of listHeader */
public static final String textListHeader[][] = {
	{"Periode","Lokasi","Supplier","Kategori","Semua"},
	{"Period","Location","Supplier","Category","All"}
};
%>


<!-- Jsp Block -->
<%
/**
* get data from 'hidden form'
*/
int iCommand = FRMQueryString.requestCommand(request);

ControlLine ctrLine = new ControlLine();

SrcReportStock srcReportStock = new SrcReportStock();
FrmSrcReportStock frmSrcReportStock = new FrmSrcReportStock();
try {
	srcReportStock = (SrcReportStock)session.getValue(SessReportStock.SESS_SRC_REPORT_STOCK);
}
catch(Exception e) {
	srcReportStock = new SrcReportStock();
}


if(srcReportStock==null) {
	srcReportStock = new SrcReportStock();
}

try {
	session.removeValue(SessReportStock.SESS_SRC_REPORT_STOCK_LIST);
}
catch(Exception e) {
}

/** get period list */
Vector materPeriode = PstPeriode.list(0,0,"",""); //PstPeriode.fieldNames[PstPeriode.FLD_STATUS] + " <> " + PstPeriode.FLD_STATUS_RUNNING,"");
Vector vectmatPerVal = new Vector(1,1);
Vector vectmatPerKey = new Vector(1,1);
if(materPeriode!=null && materPeriode.size()>0) {
	for(int i=0; i<materPeriode.size(); i++) {
		Periode mper = (Periode)materPeriode.get(i);
		vectmatPerVal.add(""+mper.getPeriodeName() + " ( "+mper.getStartDate()+" s/d "+mper.getEndDate()+" )" );
		vectmatPerKey.add(""+mper.getOID());																	  	
	}
}

/** get location list */
Vector val_locationid = new Vector(1,1);
Vector key_locationid = new Vector(1,1); 
//Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_NAME]);
boolean checkDataAllLocReportView = PstDataCustom.checkDataAllLocReportView(userId, "user_view_sale_stock_report_location","0");
String whereLocViewReport = PstDataCustom.whereLocReportViewStock(userId, "user_view_sale_stock_report_location");
Vector vt_loc = PstLocation.list(0,0,whereLocViewReport, PstLocation.fieldNames[PstLocation.FLD_NAME]);

if(checkDataAllLocReportView){
    val_locationid.add("0");
    key_locationid.add(textListHeader[SESS_LANGUAGE][4]+" "+textListHeader[SESS_LANGUAGE][1]);
}

for(int d=0;d<vt_loc.size();d++) {
	Location loc = (Location)vt_loc.get(d);
	val_locationid.add(""+loc.getOID()+"");
	key_locationid.add(loc.getName());
}

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdSearch() {
	document.frmsrcreportstock.command.value="<%=Command.LIST%>";
	document.frmsrcreportstock.action="reportstock_list.jsp";
	document.frmsrcreportstock.submit();
}

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
<script type="text/javascript" src="../../../styles/jquery.min.js"></script>
<script src="../../../styles/dimata-app.js" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function(){
        //added by dewok 2018
        var getDataFunction = function(onDone, onSuccess, approot, command, dataSend, servletName, dataAppendTo, notification, dataType){		
            $(this).getData({
               onDone	: function(data){
                   onDone(data);
               },
               onSuccess	: function(data){
                    onSuccess(data);
               },
               approot          : approot,
               dataSend         : dataSend,
               servletName	: servletName,
               dataAppendTo	: dataAppendTo,
               notification     : notification,
               ajaxDataType	: dataType
            });
        };
        
        $('.loc').change(function(){
            var idLocation = $(this).val();            
            var dataFor = "getEtalaseByLocation";
            var command = "<%=Command.NONE%>";
            var approot = "<%=approot%>";
            var dataSend = {
                "FRM_FIELD_DATA_FOR": dataFor,
                "FRM_FIELD_LOCATION_ID": idLocation,
                "command": command,
                "USE_FOR": "stock_report_global"
            };
            onDone = function (data) {
                
            };
            onSuccess = function (data) {

            };
            getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxPenerimaan", ".eta", false, "json");            
        });
    });
</script>
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
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
    <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
        <%if (menuUsed == MENU_PER_TRANS) {%>
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
        <%} else {%>
        <tr bgcolor="#FFFFFF">
            <td height="10" ID="MAINMENU">
                <%@include file="../../../styletemplate/template_header.jsp" %>
            </td>
        </tr>
        <%}%>
        <tr> 
            <td valign="top" align="left"> 
                <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                    <tr> 
                        <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
                            <%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%> &gt; <%=textListGlobal[SESS_LANGUAGE][3]%><!-- #EndEditable --></td>
                    </tr>
                    <tr> 
                        <td><!-- #BeginEditable "content" --> 
                            <form name="frmsrcreportstock" method="post" action="">
                                <input type="hidden" name="command" value="<%=iCommand%>">
                                <table width="100%" border="0">
                                    <tr> 
                                        <td colspan="2"> 
                                            <table width="100%" border="0" cellspacing="1" cellpadding="1">
                                                <tr>
                                                    <td height="21" colspan="3"><hr size="1"></td>
                                                </tr>
                                                <tr> 
                                                    <td height="19" valign="" width="9%" align="left"><%=textListHeader[SESS_LANGUAGE][0]%></td>
                                                    <td height="19" valign="" width="1%" align="left">:</td>
                                                    <td height="19" width="90%" valign="" align="left"><%=(ControlCombo.draw(frmSrcReportStock.fieldNames[frmSrcReportStock.FRM_FIELD_PERIODE_ID], "formElemen", null, "" + srcReportStock.getPeriodeId(), vectmatPerKey, vectmatPerVal, null))%></td>
                                                </tr>
                                                <tr> 
                                                    <td height="21" width="9%"><%=textListHeader[SESS_LANGUAGE][1]%></td>
                                                    <td height="21" width="1%">:</td>
                                                    <td height="21" width="90%"><%=(ControlCombo.draw(frmSrcReportStock.fieldNames[frmSrcReportStock.FRM_FIELD_LOCATION_ID], null, "" + srcReportStock.getLocationId(), val_locationid, key_locationid, "", "formElemen loc"))%>                        </td>
                                                </tr>
                                                <!--tr> 
                                                  <td height="19" valign="" width="9%" align="left"><%=textListHeader[SESS_LANGUAGE][2]%></td>
                                                  <td height="19" valign="" width="1%" align="left">:</td>
                                                  <td height="19" width="90%" valign="" align="left">&nbsp; 
                                                <%
                                                    Vector val_supplier = new Vector(1, 1);
                                                    Vector key_supplier = new Vector(1, 1);
                                                    String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]
                                                            + " = " + PstContactClass.CONTACT_TYPE_SUPPLIER
                                                            + " AND " + PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]
                                                            + " != " + PstContactList.DELETE;
                                                    Vector vt_supp = PstContactList.listContactByClassType(0, 0, wh_supp, PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
                                                    val_supplier.add("0");
                                                    key_supplier.add(textListHeader[SESS_LANGUAGE][4] + " " + textListHeader[SESS_LANGUAGE][2]);
                                                    if (vt_supp != null && vt_supp.size() > 0) {
                                                        for (int d = 0; d < vt_supp.size(); d++) {
                                                            ContactList cnt = (ContactList) vt_supp.get(d);
                                                            String cntName = cnt.getCompName();
                                                            val_supplier.add(String.valueOf(cnt.getOID()));
                                                            key_supplier.add(cntName);
                                                        }
                                                    }
                                                    String select_supplier = "" + srcReportStock.getSupplierId();
                                                    out.println(ControlCombo.draw(frmSrcReportStock.fieldNames[frmSrcReportStock.FRM_FIELD_SUPPLIER_ID], null, select_supplier, val_supplier, key_supplier, "", "formElemen"));
                                                %>
                      </td>
                    </tr>
                    <tr> 
                      <td height="21" valign="" width="9%" align="left"><%=textListHeader[SESS_LANGUAGE][4]%></td>
                      <td height="21" valign="" width="1%" align="left">:</td>
                      <td height="21" width="90%" valign="" align="left">&nbsp; 
                                                <%
                                                    Vector materGroup = PstCategory.list(0, 0, "", PstCategory.fieldNames[PstCategory.FLD_NAME]);
                                                    Vector vectGroupVal = new Vector(1, 1);
                                                    Vector vectGroupKey = new Vector(1, 1);
                                                    vectGroupKey.add("0");
                                                    vectGroupVal.add(textListHeader[SESS_LANGUAGE][4] + " " + textListHeader[SESS_LANGUAGE][3]);
                                                    if (materGroup != null && materGroup.size() > 0) {
                                                        for (int i = 0; i < materGroup.size(); i++) {
                                                            Category mGroup = (Category) materGroup.get(i);
                                                            vectGroupVal.add(mGroup.getName());
                                                            vectGroupKey.add("" + mGroup.getOID());
                                                        }
                                                    }
                                                    out.println(ControlCombo.draw(frmSrcReportStock.fieldNames[frmSrcReportStock.FRM_FIELD_CATEGORY_ID], "formElemen", null, "" + srcReportStock.getCategoryId(), vectGroupKey, vectGroupVal, null));
                                                %> 
                                              </td>
                    </tr-->
                                                <tr>
                                                    <td height="21" valign="" align="left">Etalase</td>
                                                    <td height="21" valign="" align="left">:</td>
                                                    <td height="21" valign="" align="left"><%
                                                        String whereKsg = "";
                                                        if (srcReportStock.getLocationId() != 0) {
                                                            whereKsg = "" + PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID] + "='" + srcReportStock.getLocationId() + "'";
                                                        }
                                                        Vector listKsg = PstKsg.list(0, 0, whereKsg, PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID]+","+PstKsg.fieldNames[PstKsg.FLD_CODE]);
                                                        Vector vectKsgVal = new Vector(1, 1);
                                                        Vector vectKsgKey = new Vector(1, 1);
                                                        //if (srcReportStock.getLocationId() == 0) {
                                                            vectKsgKey.add("All...");
                                                            vectKsgVal.add("");
                                                        //}
                                                        for (int i = 0; i < listKsg.size(); i++) {
                                                            Ksg matKsg = (Ksg) listKsg.get(i);
                                                            Location loc = new Location();
                                                            if (matKsg.getLocationId() > 0) {
                                                                loc = PstLocation.fetchExc(matKsg.getLocationId());
                                                            }
                                                            vectKsgKey.add(loc.getCode() + " - " + matKsg.getCode());
                                                            vectKsgVal.add(matKsg.getCode());
                                                        }
                                                        %> <%=ControlCombo.draw(frmSrcReportStock.fieldNames[frmSrcReportStock.FRM_FIELD_KSG], "formElemen eta", null, "" + srcReportStock.getKsg(), vectKsgVal, vectKsgKey, "onChange=\"javascript:changeUnit()\"")%></td>
                                                </tr>
                                                <%--update opie-eyek 25012013 untuk sort by--%>
                                                <tr>
                                                    <td height="21" valign="" align="left">Sort By </td>
                                                    <td height="21" valign="" align="left">:</td>
                                                    <td height="21" valign="" align="left">
                                                        <%
                                                            /* String whereKsg = "";
                                                             Vector listKsg = PstKsg.list(0,0,whereKsg,PstKsg.fieldNames[PstKsg.FLD_CODE]);
                                                             Vector vectKsgVal = new Vector(1,1);
                                                             Vector vectKsgKey = new Vector(1,1);
                                                             vectKsgKey.add("All...");
                                                             vectKsgVal.add("");
                                                             for(int i=0; i<listKsg.size(); i++){
                                                             Ksg matKsg = (Ksg)listKsg.get(i);
                                                             vectKsgKey.add(matKsg.getCode()+" - "+matKsg.getName());
                                                             vectKsgVal.add(matKsg.getCode());
                                                             }*/
                                                            Vector key_sort = new Vector(1, 1);
                                                            Vector val_sort = new Vector(1, 1);

                                                            key_sort.add("0");
                                                            val_sort.add("SKU");

                                                            key_sort.add("1");
                                                            val_sort.add("Nama Barang");

                                                            key_sort.add("2");
                                                            val_sort.add("Qty");

                                                            key_sort.add("3");
                                                            val_sort.add("Satuan");

                                                            key_sort.add("4");
                                                            val_sort.add("Nilai Stok");

                                                            key_sort.add("5");
                                                            val_sort.add("Category");

                                                            String select_sort = "" + srcReportStock.getSortBy();

                                                        %>
                                                        <%=ControlCombo.draw(frmSrcReportStock.fieldNames[frmSrcReportStock.FRM_FIELD_SORT_BY], "formElemen", null, "" + srcReportStock.getSortBy(), key_sort, val_sort, "onChange=\"javascript:changeUnit()\"")%></td>
                                                </tr>
                                                <tr> 
                                                    <td height="19" valign="" width="9%" align="left">View 0 Qty</td>
                                                    <td height="19" valign="" width="1%" align="left">:</td>
                                                    <%
                                                        ControlCheckBox controlCheckBox = new ControlCheckBox();
                                                        Vector prQtyVal = new Vector(1, 1);
                                                        Vector prQtyKey = new Vector(1, 1);
                                                        prQtyVal.add("1");
                                                        prQtyKey.add("Yes");
                                                        controlCheckBox.setWidth(5);
                                                    %>
                                                    <td height="19" width="90%" valign="" align="left"><%=controlCheckBox.draw("viewzerostock", prQtyVal, prQtyKey, prQtyVal)%></td>
                                                </tr>
                                                <tr>
                                                    <td height="19" valign="" width="9%" align="left">View Harga Jual</td>
                                                    <td height="19" valign="" width="1%" align="left">:</td>
                                                    <td height="19" width="90%" valign="" align="left">
                                                        <%
                                                            //add opie-eyek 20130805
                                                            String ordPrice = PstPriceType.fieldNames[PstPriceType.FLD_INDEX];
                                                            Vector listPriceType = PstPriceType.list(0, 0, "", ordPrice);
                                                            Vector prTypeVal = new Vector(1, 1);
                                                            Vector prTypeKey = new Vector(1, 1);
                                                            for (int i = 0; i < listPriceType.size(); i++) {
                                                                PriceType prType = (PriceType) listPriceType.get(i);
                                                                prTypeVal.add("" + prType.getOID() + "");
                                                                prTypeKey.add("" + prType.getCode() + "");
                                                            }
                                                            controlCheckBox.setWidth(5);

                                                        %> 
                                                        <%=controlCheckBox.draw(FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_PRICE_TYPE_ID], prTypeVal, prTypeKey, new Vector())%>
                                                    </td>
                                                </tr>
                                                <tr> 
                                                    <td height="21" valign="" width="9%" align="left">&nbsp;</td>
                                                    <td height="21" valign="" width="1%" align="left">&nbsp;</td>
                                                    <td height="21" width="90%" valign="" align="left">&nbsp;</td>
                                                </tr>
                                                <tr> 
                                                    <td height="21" valign="" width="9%" align="left">&nbsp;</td>
                                                    <td height="21" valign="" width="1%" align="left">&nbsp;</td>
                                                    <td height="21" width="90%" valign="" align="left"> 
                                                        <table width="40%" border="0" cellspacing="0" cellpadding="0">
                                                            <tr> 
                                                              <!--td width="4%" nowrap><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][5], ctrLine.CMD_SEARCH, true)%>"></a></td-->
                                                                <td width="96%" nowrap class="command">&nbsp;&nbsp;<a class="btn btn-primary" href="javascript:cmdSearch()"><i class="fa fa-search"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][5], ctrLine.CMD_SEARCH, true)%></a></td>
                                                            </tr>
                                                        </table>                        </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                            <!-- #EndEditable --></td> 
                    </tr> 
                </table>
            </td>
        </tr>
        <tr> 
            <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
                <%if (menuUsed == MENU_ICON) {%>
                <%@include file="../../../styletemplate/footer.jsp" %>
                <%} else {%>
                <%@ include file = "../../../main/footer.jsp" %>
                <%}%>
                <!-- #EndEditable --> </td>
        </tr>
    </table>
</body>
<!-- #EndTemplate --></html>
