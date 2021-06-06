<%@page import="com.dimata.common.entity.payment.PriceType"%>
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
<%@ page import = "com.dimata.common.entity.payment.CurrencyType"%>
<%@ page import = "com.dimata.common.entity.payment.PstCurrencyType"%>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE_REPORT, AppObjInfo.OBJ_PURCHASE_RECEIVE_REPORT_BY_RECEIPT); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
    {"Penerimaan","Dari Pembelian","Laporan Per Nota","Pencarian","Laporan Penerimaan"},
    {"Receive","From Purchase","Report By Invoice","Search","Receive Report"}
};

/* this constant used to list text of listHeader */
public static final String textListHeader[][] = {
    {"Lokasi","Supplier","Kategori","Periode","Urut Berdasar","Sub Kategori"," s/d ","Mata Uang","Semua","Semua Lokasi","Tipe Harga"},
    {"Location","Supplier","Category","Period","Sort By","Sub Category"," to ","Currency","All","All Location","Price Type"}
};

public String getJspTitle(int index, int language, String prefiks, boolean addBody) {
    String result = "";
    if(addBody) {
        if(language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){
            result = textListHeader[language][index] + " " + prefiks;
        } else {
            result = prefiks + " " + textListHeader[language][index];
        }
    } else {
        result = textListHeader[language][index];
    }
    return result;
}

%>


<!-- Jsp Block -->
<%
/**
 * get data from 'hidden form'
 */
int iCommand = FRMQueryString.requestCommand(request);

ControlLine ctrLine = new ControlLine();

SrcReportReceive srcReportReceive = new SrcReportReceive();
FrmSrcReportReceive frmSrcReportReceive = new FrmSrcReportReceive();
try{
    srcReportReceive = (SrcReportReceive)session.getValue(SessReportReceive.SESS_SRC_REPORT_RECEIVE_INVOICE);
} catch(Exception e){
    srcReportReceive = new SrcReportReceive();
}


if(srcReportReceive==null){
    srcReportReceive = new SrcReportReceive();
}

try{
    session.removeValue(SessReportReceive.SESS_SRC_REPORT_RECEIVE_INVOICE);
} catch(Exception e){
}

/** get location list */
Vector val_locationid = new Vector(1,1);
Vector key_locationid = new Vector(1,1);
//Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_NAME]);
//add opie-eyek
//algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
String whereClause = " ("+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE +
                   " OR "+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE +")";

whereClause += " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");

Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");
val_locationid.add("0");
key_locationid.add(textListHeader[SESS_LANGUAGE][9]);
for(int d=0;d<vt_loc.size();d++){
    Location loc = (Location)vt_loc.get(d);
    val_locationid.add(""+loc.getOID()+"");
    key_locationid.add(loc.getName());
}

/** get currency list */
Vector val_currency = new Vector(1,1);
Vector key_currency = new Vector(1,1);
Vector vCurrency = PstCurrencyType.list(0,0,PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE,"");
val_currency.add("0");
key_currency.add(textListHeader[SESS_LANGUAGE][8]);
for(int e=0; e<vCurrency.size(); e++) {
    CurrencyType objCurrencyType = (CurrencyType)vCurrency.get(e);
    val_currency.add(""+objCurrencyType.getOID());
    key_currency.add(objCurrencyType.getCode());
}
/*supplier*/
Vector val_supplier = new Vector(1,1);
Vector key_supplier = new Vector(1,1);
String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
				 " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
				 " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
				 " != "+PstContactList.DELETE;
Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
val_supplier.add("0");
key_supplier.add(textListHeader[SESS_LANGUAGE][8]);
for(int d=0; d<vt_supp.size(); d++){
	ContactList cnt = (ContactList)vt_supp.get(d);
	String cntName = cnt.getCompName();
	val_supplier.add(String.valueOf(cnt.getOID()));
	key_supplier.add(cntName);  
}
String select_supplier = ""+srcReportReceive.getSupplierId();

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function cmdSearch(){
    var view = document.frmsrcreceivereport.view.value;
    document.frmsrcreceivereport.command.value="<%=Command.LIST%>";
    document.frmsrcreceivereport.action="reportreceiveinvoice_list.jsp?view="+view;
    document.frmsrcreceivereport.submit();
}

<!--

//-------------- script control line -------------------
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

</SCRIPT>
<!-- #EndEditable --> 
</head> 

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
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
            &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%> &gt; <%=textListGlobal[SESS_LANGUAGE][3]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmsrcreceivereport" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <table width="100%" border="0">
                <tr>
                  <td valign="top" colspan="2">
                    <hr size="1">
                  </td>
                </tr>
                <tr> 
                  <td colspan="2"> 
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr> 
                        <td valign="top" width="11%" align="left"><%=getJspTitle(3,SESS_LANGUAGE,"",true)%></td>
                        <td valign="top" width="1%" align="left">:</td>
                        <td width="88%" align="left"><%=ControlDate.drawDate(frmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_FROM], srcReportReceive.getDateFrom(),"formElemen",1,-5)%><%=textListHeader[SESS_LANGUAGE][6]%><%=ControlDate.drawDate(frmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_TO], srcReportReceive.getDateTo(),"formElemen",1,-5) %></td>
                      </tr>
                      <tr>
                        <td valign="top" align="left"><%=getJspTitle(0,SESS_LANGUAGE,"",true)%></td>
                        <td valign="top" align="left">:</td>
                        <td align="left"><%=ControlCombo.draw(FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_LOCATION_ID], null, ""+srcReportReceive.getLocationId(), val_locationid, key_locationid, "", "formElemen")%> 
                        </td>
                      </tr>
                      <tr>
                        <td valign="top" align="left"><%=getJspTitle(7,SESS_LANGUAGE,"",true)%></td>
                        <td valign="top" align="left">:</td>
                        <td align="left"><%=ControlCombo.draw(FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_CURRENCY_ID], null, ""+srcReportReceive.getCurrencyId(), val_currency, key_currency, "", "formElemen")%> 
                        </td>
                      </tr>
                      <tr> 
                        <td valign="top" align="left"><%=getJspTitle(1,SESS_LANGUAGE,"",true)%></td>
                        <td valign="top" align="left">:</td>
                        <td align="left"><%=ControlCombo.draw(frmSrcReportReceive.fieldNames[frmSrcReportReceive.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"","formElemen")%> </td>
                      </tr>
                      <tr>
                        <td valign="top" align="left"><%=getJspTitle(10,SESS_LANGUAGE,"",true)%></td>
                        <td valign="top" align="left">:</td>
                        <td align="left">
                            <%//=ControlCombo.draw(FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_CURRENCY_ID], null, ""+srcReportReceive.getCurrencyId(), val_currency, key_currency, "", "formElemen")%> 
                            <%
                                //add opie-eyek 20130805
                                ControlCheckBox controlCheckBox = new ControlCheckBox();
                                String ordPrice = PstPriceType.fieldNames[PstPriceType.FLD_INDEX]; 
                                Vector listPriceType = PstPriceType.list(0,0,"",ordPrice);
                                 Vector prTypeVal = new Vector(1,1);
                                 Vector prTypeKey = new Vector(1,1);
                                for(int i=0;i<listPriceType.size();i++) {
                                        PriceType prType = (PriceType)listPriceType.get(i);
					prTypeVal.add(""+prType.getOID()+"");
					prTypeKey.add(""+prType.getCode()+"");
				}
                              controlCheckBox.setWidth(5);
                              
                          %> 
                          <%=controlCheckBox.draw(FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_PRICE_TYPE_ID], prTypeVal, prTypeKey, new Vector())%>
                        </td>
                      </tr>
                      <tr>
                        <td valign="top" align="left">View</td>
                        <td valign="top" align="left">:</td>
                        <td align="left">
                        <%
                        Vector val_view= new Vector(1,1);
                        Vector key_view= new Vector(1,1);
                        val_view.add(""+SrcSaleReport.VIEW_BY_SUMMARY+"");
                        key_view.add(SrcSaleReport.viewMethod[SESS_LANGUAGE][SrcSaleReport.VIEW_BY_SUMMARY]);
                        val_view.add(""+SrcSaleReport.VIEW_BY_DETAIL+"");
                        key_view.add(SrcSaleReport.viewMethod[SESS_LANGUAGE][SrcSaleReport.VIEW_BY_DETAIL]);
                        
                        String default_viewreport = SrcSaleReport.viewMethod[SESS_LANGUAGE][SrcSaleReport.VIEW_BY_SUMMARY];
			%>
			<%=ControlCombo.draw("view", null, ""+SrcSaleReport.VIEW_BY_SUMMARY, val_view, key_view, "", "formElemen")%></td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="12%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="0%" align="left">&nbsp;</td>
                        <td height="21" width="88%" valign="top" align="left">&nbsp;</td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="12%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="0%" align="left">&nbsp;</td>
                        <td height="21" width="88%" valign="top" align="left"> 
                          <table width="40%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td width="90%" nowrap class="command"><a class="btn-primary btn-lg" href="javascript:cmdSearch()"><i class="fa fa-search"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][4],ctrLine.CMD_SEARCH,true)%></i></a></td>
                            </tr>
                          </table></td>
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
