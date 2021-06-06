<%-- 
    Document   : src_price_protection
    Created on : Dec 19, 2014, 3:38:58 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.entity.warehouse.PriceProtection"%>
<%@page import="com.dimata.posbo.session.warehouse.SessPriceProtection"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatReceive"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseOrder"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<% 
/* 
 * Page Name  		:  src_price_protection.jsp
 * Created on 		:  Selasa, 2 Agustus 2007 10:40 AM 
 * 
 * @author  		:  gwawan
 * @version  		:  -
 */

/*******************************************************************
 * Page Description : page ini merupakan gabungan dari page :
 						- srcreceive_wh_supp_material.jsp 
						- srcreceive_wh_supp_po_material.jsp
 * Imput Parameters : [input parameter ...] 
 * Output 			: [output ...] 
 *******************************************************************/
%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.search.SrcMatReceive,
                   com.dimata.posbo.form.search.FrmSrcMatReceive,
                   com.dimata.posbo.session.warehouse.SessMatReceive,
                   com.dimata.posbo.entity.warehouse.PstMatReceive" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
	{"Price Protection","Price Protection","Pencarian","Daftar","Edit","","Tanpa PO","Tidak ada data penerimaan barang"},
	{"Price Protection","Price Protection","Search","List","Edit","","Without PO","Receiving goods not found"}
};

/* this constant used to list text of listHeader */
public static final String textListHeader[][] = {
	{"Nomor","Supplier","Invoice Supplier","Lokasi Terima","Tanggal","Dari"," s/d ","Status","Urut Berdasar","Semua"},
	{"Number","Supplier","Supplier Invoice","Receive Location","Date","From"," to ","Status","Sort By","All"}
};





/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
	{"No","Kode","Tanggal","Status","Amount","Catatan","Status","Keterangan"},
	{"No","Code","Date","Status","Amount","Remark","Status","Description"}
};

public String drawList(int language,Vector objectClass,int start,int docType,I_DocStatus i_status) {
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
		ctrlist.addHeader(textListMaterialHeader[language][1],"14%");
		ctrlist.addHeader(textListMaterialHeader[language][2],"8%");
		ctrlist.addHeader(textListMaterialHeader[language][3],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][4],"14%");
		ctrlist.addHeader(textListMaterialHeader[language][5],"18%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		if(start < 0)	{
			start = 0;		
		}	
		
		for(int i=0; i<objectClass.size(); i++) {
                    
			Vector vt = (Vector)objectClass.get(i);
                        PriceProtection priceProtection = (PriceProtection) vt.get(0);
                        
			String str_dt_RecDate = ""; 
			try {
				Date dt_RecDate = priceProtection.getDateCreated();
				if(dt_RecDate==null) {
					dt_RecDate = new Date();
				}	
				str_dt_RecDate = Formater.formatDate(dt_RecDate, "dd-MM-yyyy");
			}
			catch(Exception e) {
                            str_dt_RecDate = "";
                        }
			
			Vector rowx = new Vector();				
			rowx.add(""+(start + i + 1));//0
			rowx.add(priceProtection.getNumberPP());//1
                        rowx.add(str_dt_RecDate);//2
			rowx.add(i_status.getDocStatusName(docType,priceProtection.getStatus()));//3
                        rowx.add(""+priceProtection.getTotalAmount());//4
			rowx.add(""+priceProtection.getRemark());//5
	
			lstData.add(rowx);
			lstLinkData.add(String.valueOf(priceProtection.getOID()));
		}
		result = ctrlist.draw();
	}
	else{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][7]+"</div>";		
	}
	return result;
}


public String getJspTitle(int index, int language, String prefiks, boolean addBody) {
	String result = "";
	if(addBody) {
		if(language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){	
			result = textListHeader[language][index] + " " + prefiks;
		}
		else {
			result = prefiks + " " + textListHeader[language][index];		
		}
	}
	else {
		result = textListHeader[language][index];
	} 
	return result;
}

public boolean getTruedFalse(Vector vect, int index) {
	for(int i=0;i<vect.size();i++) {
		int iStatus = Integer.parseInt((String)vect.get(i));
		if(iStatus==index)
			return true;
	}
	return false;
}
%>


<!-- Jsp Block -->
<%
/**
* get approval status for create document 
*/
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int systemName = I_DocType.SYSTEM_MATERIAL;
int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_LMRR);
boolean privManageData = true;
%>


<%
/**
* get data from 'hidden form'
*/
int iCommand = FRMQueryString.requestCommand(request);
long hidden_priceprotection_id = FRMQueryString.requestLong(request, "hidden_priceprotection_id");


int iErrCode = FRMMessage.ERR_NONE;

String msgStr = "";

int start = FRMQueryString.requestInt(request, "start");

int recordToGet = 20;
int vectSize = 0;

/**
* declaration of some identifier
*/
String recCode = i_pstDocType.getDocCode(docType);
String recTitle = "Terima Barang"; // i_pstDocType.getDocTitle(docType);
String recItemTitle = recTitle + " Item";

/**
* ControlLine 
*/
ControlLine ctrLine = new ControlLine();

CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
SrcMatReceive srcMatReceive = new SrcMatReceive();
SessMatReceive sessMatReceive = new SessMatReceive();
FrmSrcMatReceive frmSrcMatReceive = new FrmSrcMatReceive(request, srcMatReceive);


String sOidNumber = FRMQueryString.requestString(request,frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVENUMBER]); 
int oidDate = FRMQueryString.requestInt(request, frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEDATESTATUS]);
int oidSortBy = FRMQueryString.requestInt(request,frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESORTBY]); 
String sOidVendor = FRMQueryString.requestString(request,frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_VENDORNAME]); 
String sInvoiceSupp ="";
if(iCommand!=23){
    sInvoiceSupp = FRMQueryString.requestString(request,frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_INVOICE_SUPPLIER]); 
}
long oidLoc = FRMQueryString.requestLong(request,frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_LOCATION_ID]); 
int PurchaseID = FRMQueryString.requestInt(request, "PurchaseID");

if (oidDate == 1 || sOidNumber!= ""  || oidSortBy != 0 || sOidVendor !="" || sInvoiceSupp !="" || oidLoc !=0 && iCommand != Command.FIRST){ 
frmSrcMatReceive.requestEntityObject(srcMatReceive);
}

Vector vectSt = new Vector(1,1);
String[] strStatus = request.getParameterValues(FrmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]);
if(strStatus!=null && strStatus.length>0) {
     for(int i=0; i<strStatus.length; i++) {        
            try {
                    vectSt.add(strStatus[i]);
            }
            catch(Exception exc) {
                    System.out.println("err");
            }
     }
}

srcMatReceive.setReceiveSource(-1);
srcMatReceive.setReceivestatus(vectSt);   

if(PurchaseID==-1){
    srcMatReceive.setPurchaseOrderId(PurchaseID);
}
if(PurchaseID==-2){
    srcMatReceive.setPurchaseOrderId(PurchaseID);
}
  
String whereClausex = PstDataCustom.whereLocReportView(userId, "user_create_document_location");

vectSize = SessPriceProtection.countPriceProtection(srcMatReceive, whereClausex);

if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST) {
	start = ctrlMatReceive.actionList(iCommand,start,vectSize,recordToGet);
}

Vector records = SessPriceProtection.searchPriceProtection(srcMatReceive,start,recordToGet,whereClausex);

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">


function cmdSearch(){
	document.frm_src_priceprotection.command.value="<%=Command.LIST%>";
	document.frm_src_priceprotection.action="src_price_protection.jsp";
	document.frm_src_priceprotection.submit();
}

function cmdAddPO(){
	document.frm_src_priceprotection.command.value="<%=Command.ADD%>";
	document.frm_src_priceprotection.approval_command.value="<%=Command.SAVE%>";	
	document.frm_src_priceprotection.add_type.value="<%=ADD_TYPE_SEARCH%>";				
	document.frm_src_priceprotection.action="price_protection_edit.jsp";
	document.frm_src_priceprotection.submit();
}

function cmdAdd(){
	document.frm_src_priceprotection.command.value="<%=Command.ADD%>";
	document.frm_src_priceprotection.approval_command.value="<%=Command.SAVE%>";	
	document.frm_src_priceprotection.add_type.value="<%=ADD_TYPE_SEARCH%>";				
	document.frm_src_priceprotection.action="receive_wh_supp_material_edit.jsp";
	if(compareDateForAdd()==true)
	document.frm_src_priceprotection.submit();
}

function cmdEdit(idRcv){
	document.frm_src_priceprotection.hidden_priceprotection_id.value=idRcv;
	document.frm_src_priceprotection.start.value=0;
	document.frm_src_priceprotection.approval_command.value="<%=Command.APPROVE%>";					
	document.frm_src_priceprotection.command.value="<%=Command.EDIT%>";
	document.frm_src_priceprotection.action="price_protection_edit.jsp";
	document.frm_src_priceprotection.submit();
}

function cmdListFirst(){
	document.frm_src_priceprotection.command.value="<%=Command.FIRST%>";
	document.frm_src_priceprotection.action="src_price_protection.jsp";
	document.frm_src_priceprotection.submit();
}

function cmdListPrev(){
	document.frm_src_priceprotection.command.value="<%=Command.PREV%>";
	document.frm_src_priceprotection.action="src_price_protection.jsp";
	document.frm_src_priceprotection.submit();
}

function cmdListNext(){
	document.frm_src_priceprotection.command.value="<%=Command.NEXT%>";
	document.frm_src_priceprotection.action="src_price_protection.jsp";
	document.frm_src_priceprotection.submit();
}

function cmdListLast(){
	document.frm_src_priceprotection.command.value="<%=Command.LAST%>";
	document.frm_src_priceprotection.action="src_price_protection.jsp";
	document.frm_src_priceprotection.submit();
}

function cmdBack(){
	document.frm_src_priceprotection.command.value="<%=Command.BACK%>";
	document.frm_src_priceprotection.action="src_price_protection.jsp";
	document.frm_src_priceprotection.submit();
}


function fnTrapKD(){
   if (event.keyCode == 13) {
		document.all.aSearch.focus();
		cmdSearch();
   }
}

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
         <script language="JavaScript">
                window.focus();
        </script>
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
        <script language="JavaScript">
                window.focus();
        </script>
      <%@include file="../../../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
            &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%>  <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_src_priceprotection" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="add_type" value="">			
              <input type="hidden" name="approval_command">			    			  			  			  			  			  
              <input type="hidden" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">
              <input type="hidden" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVE_SOURCE]%>" value="<%=PstMatReceive.SOURCE_FROM_SUPPLIER%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="hidden_priceprotection_id" value="<%=hidden_priceprotection_id%>">
              <input type="hidden" name="PurchaseID" value="<%=PurchaseID%>">
              <table width="100%" border="0">
                <tr>
                  <td valign="top" colspan="1">
                   
                  </td>
                </tr>
				<tr> 
                  <td colspan="3"> 
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr> 
                        <td height="21" width="20%"><%=getJspTitle(0,SESS_LANGUAGE,recCode,false)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="87%">&nbsp; 
                          <input tabindex="1" type="text" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVENUMBER] %>"  value="<%= srcMatReceive.getReceivenumber() %>" class="formElemen" size="20" onKeyDown="javascript:fnTrapKD()">
                        </td>
                      </tr>
                      <%--
                      <tr> 
                        <td height="21" width="12%"><%=getJspTitle(1,SESS_LANGUAGE,recCode,false)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="87%">&nbsp; 
                          <input type="text" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_VENDORNAME] %>"  value="<%= srcMatReceive.getVendorname() %>" class="formElemen" size="30" onKeyDown="javascript:fnTrapKD()">
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" width="12%"><%=getJspTitle(2,SESS_LANGUAGE,recCode,false)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="87%">&nbsp; 
                          <input type="text" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_INVOICE_SUPPLIER] %>"  value="<%= srcMatReceive.getInvoiceSupplier() %>" class="formElemen" size="20" onKeyDown="javascript:fnTrapKD()">
                        </td>
                      </tr>
                      --%>
                      <tr> 
                        <td height="21" width="12%"><%=getJspTitle(3,SESS_LANGUAGE,recCode,false)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="87%">&nbsp; 
                          <% 
                                Vector obj_locationid = new Vector(1,1); 
                                Vector val_locationid = new Vector(1,1); 
                                Vector key_locationid = new Vector(1,1); 
                                //add opie-eyek
                                //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
                                String whereClause = " ("+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE + 
                                                   " OR "+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE +")";

                                whereClause += " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");

                                Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");

                                val_locationid.add(String.valueOf(0));
                                key_locationid.add(getJspTitle(9,SESS_LANGUAGE,recCode,false)+" "+getJspTitle(3,SESS_LANGUAGE,recCode,false));
                                for(int d=0; d<vt_loc.size(); d++) {
                                        Location loc = (Location)vt_loc.get(d);
                                        val_locationid.add(""+loc.getOID()+"");
                                        key_locationid.add(loc.getName());
                                }
                          %>                 
                          <%=ControlCombo.draw(frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_LOCATION_ID], null, "" +oidLoc , val_locationid, key_locationid, " onKeyDown=\"javascript:fnTrapKD()\"", "formElemen")%>
                        </td>
                      </tr>
                      <tr> 
                        <td height="43" rowspan="2" valign="top" width="12%" align="left"><%=getJspTitle(4,SESS_LANGUAGE,recCode,false)%></td>
                        <td height="43" rowspan="2" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="87%" valign="top" align="left"> 
                          <input type="radio" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEDATESTATUS] %>" <%if(srcMatReceive.getReceivedatestatus()==0){%>checked<%}%> value="0" onKeyDown="javascript:fnTrapKD()">
                          <%=getJspTitle(9,SESS_LANGUAGE,recCode,false)%>&nbsp;<%=getJspTitle(4,SESS_LANGUAGE,recCode,false)%></td>
                      </tr>
                      <tr align="left"> 
                        <td height="21" width="87%" valign="top"> 
                          <input type="radio" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEDATESTATUS] %>" <%if(srcMatReceive.getReceivedatestatus()==1){%>checked<%}%> value="1" onKeyDown="javascript:fnTrapKD()">
                          <%=getJspTitle(5,SESS_LANGUAGE,recCode,false)%> <%=ControlDate.drawDate(frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEFROMDATE], srcMatReceive.getReceivefromdate(),"formElemen",1,-5)%> <%=getJspTitle(6,SESS_LANGUAGE,recCode,false)%> <%=ControlDate.drawDate(frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVETODATE], srcMatReceive.getReceivetodate(),"formElemen",1,-5) %> </td>
                      </tr>
                    </table>
                    <td valign="top" colspan="1">
                    <table width="85%" border="0" cellspacing="1" cellpadding="1">  
                     
                      <tr> 
                        <td height="21" valign="top" width="12%" align="left"><%=getJspTitle(7,SESS_LANGUAGE,recCode,false)%></td>
                        <td height="21" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="87%" valign="top" align="left"> 
                          <%
                           int indexPrStatus = 0; 
                           String strPrStatus = "Draft";
                          %>
                          <input type="checkbox" class="formElemen" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcMatReceive.getReceivestatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  

                          <%
                           indexPrStatus = 1; 
                           strPrStatus = "To Be Confirm";
                          %>
                          <input type="checkbox" class="formElemen" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcMatReceive.getReceivestatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
                          <%
                           indexPrStatus = 2; 
                           strPrStatus = "Final";
                          %>
                          <input type="checkbox" class="formElemen" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcMatReceive.getReceivestatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
                         
                        </td>
                      </tr>
                      <tr> 
                        <td height="19" valign="top" width="12%" align="left"><%=getJspTitle(8,SESS_LANGUAGE,recCode,false)%></td>
                        <td height="19" valign="top" width="1%" align="left">:</td>
                        <td height="19" width="87%" valign="top" align="left"> 
                          <% 
                                Vector key_sort = new Vector(1,1); 						  
                                Vector val_sort = new Vector(1,1); 
                                int maxItem = textListHeader[0].length-1;
                                for(int i=0; i<maxItem; i++){ 
                                        key_sort.add(""+i);							
                                        val_sort.add(""+getJspTitle(i,SESS_LANGUAGE,recCode,false)); 
                                }
                                String select_sort = ""+srcMatReceive.getReceivesortby(); 
                                out.println("&nbsp;"+ControlCombo.draw(frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESORTBY], null, select_sort,key_sort,val_sort," onKeyDown=\"javascript:fnTrapKD()\"","formElemen"));
                          %>
                        </td>
                      </tr>
                     
                    </table>
                  </td>
                </tr>
              </table>
            </form>
            <!-- #EndEditable --></td> 
        </tr> 
         <tr align="left" valign="top">
          <td height="18" valign="top" colspan="3"> 
            <table width="50%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                    <td nowrap width="1%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][0],ctrLine.CMD_SEARCH,true)%>"></a></td>
                      <td class="command" nowrap width="26%"><a href="javascript:cmdSearch()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][0],ctrLine.CMD_SEARCH,true)%></a></td>
                      <% if(privAdd){%>
                        <td nowrap width="4%"><a href="javascript:cmdAddPO()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][0]+" "+textListGlobal[SESS_LANGUAGE][5],ctrLine.CMD_ADD,true)%>"></a></td>
                        <td class="command" nowrap width="31%"><a href="javascript:cmdAddPO()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][1]+" "+textListGlobal[SESS_LANGUAGE][5],ctrLine.CMD_ADD,true)%></a></td>
                      <%}%>
              </tr>
            </table>
          </td>
        </tr> 
      <tr align="left" valign="top">
          <td height="18" valign="top" colspan="3"> 
            <table width="50%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                    <td nowrap width="1%">&nbsp;</td>
                    <td class="command" nowrap width="26%">&nbsp;</td>

                    <% if(privAdd){%>
                        <td nowrap width="4%">&nbsp;</td>
                        <td class="command" nowrap width="31%">&nbsp;</td>
                        <td nowrap width="4%">&nbsp;</td>
                        <td class="command" nowrap width="31%">&nbsp;</td>
                    <%}%>
              </tr>
            </table>
          </td>
        </tr> 
         <%if(iCommand==Command.LIST || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand == Command.LAST){%>	 
        <tr align="left" valign="top"> 
				<td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE,records,start,docType,i_status)%></td>
        </tr>	
        
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
        
         <tr align="left" valign="top">
                  <td height="18" valign="top" colspan="3"> 
                    <table width="50%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <% if(privAdd){%>
                            <td nowrap width="4%"><a href="javascript:cmdAddPO()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][0]+" "+textListGlobal[SESS_LANGUAGE][5],ctrLine.CMD_ADD,true)%>"></a></td>
                            <td class="command" nowrap width="31%"><a href="javascript:cmdAddPO()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][1]+" "+textListGlobal[SESS_LANGUAGE][5],ctrLine.CMD_ADD,true)%></a></td>
                        <%}%>
                      </tr>
                    </table>
                  </td>
        </tr>
        
            <%}%>
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

