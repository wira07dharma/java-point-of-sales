<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.posbo.form.purchasing.CtrlPurchaseOrder"%>
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
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.purchasing.*" %>
<%@ page import = "com.dimata.posbo.entity.purchasing.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>
<%//@ page import = "com.dimata.posbo.entity.admin.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_ORDER); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%
/** Check privilege except VIEW, view is already checked on checkuser.jsp as basic access */
boolean privView=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_VIEW));

boolean privSubmit=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_SUBMIT));
%>


<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */




public static final String textListHeader[][] = 
{
	{"Kode","Supplier","Tanggal","Status","Urut Berdasar","Order Pembelian","Gudang","Semua Tanggal","Dari","s/d"},
	{"Code","Supplier","Date","Status","Sort By","Purchase Order","Warehouse","All Date","From","To"}
};






public static final String textMainHeader[][] = 
{
	{"Gudang","Order Barang"},
	{"Warehouse","Purchase Order"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = 
{
	{"No","Kode","Tanggal","Supplier","Status","Keterangan","Mata Uang", "Lokasi", "Kode Bea Cukai"},
	{"No","Code","Date","Supplier","Status","Remark","Currency", "Location", "Customs Code"}
};

public String drawList(int language,Vector objectClass,int start,int docType,I_DocStatus i_status)
{
	String result = "";
	if(objectClass!=null && objectClass.size()>0)
	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
		ctrlist.addHeader(textListMaterialHeader[language][1],"14%");
		ctrlist.addHeader(textListMaterialHeader[language][2],"7%");
		int dutyFree = SessPurchaseOrder.getStrDutyFree();
		if(dutyFree == 1){
			ctrlist.addHeader(textListMaterialHeader[language][7],"10%");
			ctrlist.addHeader(textListMaterialHeader[language][8],"10%");
		}
		ctrlist.addHeader(textListMaterialHeader[language][6],"7%");
		ctrlist.addHeader(textListMaterialHeader[language][3],"15%");
		ctrlist.addHeader(textListMaterialHeader[language][4],"7%");
		ctrlist.addHeader(textListMaterialHeader[language][5],"25%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		if(start<0)
		{
			start = 0;		
		}	
		
		for(int i=0; i<objectClass.size(); i++){
			Vector vt = (Vector)objectClass.get(i);
			PurchaseOrder po = (PurchaseOrder)vt.get(0);
			ContactList contact = (ContactList)vt.get(1);		
			String cntName = contact.getCompName();					
			if(cntName.length()==0){
				cntName = String.valueOf(contact.getPersonName()+" "+contact.getPersonLastname());					
			}			
			start = start + 1;
			
			Vector rowx = new Vector();				
			rowx.add(""+start);
			rowx.add(po.getPoCode());
			
			String str_dt_PurchDate = ""; 
			try{
				Date dt_PurchDate = po.getPurchDate();
				if(dt_PurchDate==null){
					dt_PurchDate = new Date();
				}	
				str_dt_PurchDate = Formater.formatDate(dt_PurchDate, "dd-MM-yyyy");
			}
			catch(Exception e){ str_dt_PurchDate = ""; }
	
			rowx.add(str_dt_PurchDate);
			if(dutyFree == 1){
				Location location = (Location) vt.get(2);
				rowx.add(location.getName());
				rowx.add("");
			}
            // currency
            CurrencyType currType = new CurrencyType();
            try{
                currType = PstCurrencyType.fetchExc(po.getCurrencyId());
            }catch(Exception e){}
            rowx.add("<div align=\"center\">" + currType.getCode() + "</div>");
			rowx.add(cntName);
                        if(po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_APPROVED){
                            rowx.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_APPROVED]);
                        }else{
                            rowx.add(i_status.getDocStatusName(docType,po.getPoStatus()));
                        }  
			
                        rowx.add(po.getRemark());
	
			lstData.add(rowx);
			lstLinkData.add(String.valueOf(po.getOID()));
		}
		result = ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data order pembelian ...</div>";		
	}
	return result;
}


public String getJspTitle(int index, int language, String prefiks, boolean addBody){
	String result = "";
	if(addBody){
		if(language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){	
			result = textListHeader[language][index] + " " + prefiks;
		}else{
			result = prefiks + " " + textListHeader[language][index];		
		}
	}else{
		result = textListHeader[language][index];
	} 
	return result;
}

public boolean getTruedFalse(Vector vect, int index){
	for(int i=0;i<vect.size();i++){
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
int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_POR);
%>


<%
/**
* get data from 'hidden form'
*/
int iCommand = FRMQueryString.requestCommand(request);

/**
* declaration of some identifier
*/
String poCode = "PO"; //i_pstDocType.getDocCode(docType);
String poTitle = textListHeader[SESS_LANGUAGE][5];
String poItemTitle = poTitle + " Item";
String poTitleBlank = "";

/**
* ControlLine 
*/
ControlLine ctrLine = new ControlLine();



long oidPurchaseOrder = FRMQueryString.requestLong(request, "hidden_material_order_id");

/**
* initialitation some variable
*/
int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";

int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 10;
int vectSize = 0;
String whereClause = "";



CtrlPurchaseOrder ctrlPurchaseOrder = new CtrlPurchaseOrder(request);
SrcPurchaseOrder srcPurchaseOrder = new SrcPurchaseOrder();
SessPurchaseOrder sessPurchaseOrder = new SessPurchaseOrder();
FrmSrcPurchaseOrder frmSrcPurchaseOrder = new FrmSrcPurchaseOrder(request, srcPurchaseOrder);


String sOidNumber = FRMQueryString.requestString(request,frmSrcPurchaseOrder.fieldNames[FrmSrcPurchaseOrder.FRM_FIELD_PRMNUMBER]); 
int oidDate = FRMQueryString.requestInt(request, frmSrcPurchaseOrder.fieldNames[FrmSrcPurchaseOrder.FRM_FIELD_STATUSDATE]);
int oidSortBy = FRMQueryString.requestInt(request,frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_SORTBY]);
String vendorName = FRMQueryString.requestString(request,frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_VENDORNAME]);


if (oidDate == 1 || sOidNumber!= ""  || oidSortBy != 0 ){ 
frmSrcPurchaseOrder.requestEntityObject(srcPurchaseOrder);
}

if (vendorName.length()>0){
    srcPurchaseOrder.setVendorname(vendorName);
}

	 Vector vectSt = new Vector(1,1);
	 String[] strStatus = request.getParameterValues(FrmSrcPurchaseOrder.fieldNames[FrmSrcPurchaseOrder.FRM_FIELD_PRMSTATUS]);
	 if(strStatus!=null && strStatus.length>0){
		 for(int i=0; i<strStatus.length; i++){
			try{
				vectSt.add(strStatus[i]);
			}catch(Exception exc){
				System.out.println("err");
			}
		 }
	 }
	 srcPurchaseOrder.setPrmstatus(vectSt);
       
String whereClausex = PstDataCustom.whereLocReportView(userId, "user_create_document_location");



vectSize = sessPurchaseOrder.getCountPurchaseOrderMaterial(srcPurchaseOrder,docType,whereClausex);
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST){
	start = ctrlPurchaseOrder.actionList(iCommand,start,vectSize,recordToGet);
}	
Vector records = sessPurchaseOrder.searchPurchaseOrderMaterial(srcPurchaseOrder,docType,start,recordToGet,whereClausex);

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdAdd(){
	document.frmsrcordermaterial.command.value="<%=Command.ADD%>";
	document.frmsrcordermaterial.approval_command.value="<%=Command.SAVE%>";	
	document.frmsrcordermaterial.add_type.value="<%=ADD_TYPE_SEARCH%>";				
	document.frmsrcordermaterial.action="pomaterial_edit.jsp";
	if(compareDateForAdd()==true)
		document.frmsrcordermaterial.submit();
}

function cmdSearch(){
	document.frmsrcordermaterial.command.value="<%=Command.LIST%>";
	document.frmsrcordermaterial.action="srcpomaterial.jsp";
	document.frmsrcordermaterial.submit();
}

function cmdSearch2(event){
    if (event.keyCode==13){
        cmdSearch();
    }
}

function cmdEdit(oid){
	document.frmsrcordermaterial.hidden_material_order_id.value=oid;
	document.frmsrcordermaterial.start.value=0;
	document.frmsrcordermaterial.approval_command.value="<%=Command.APPROVE%>";					
	document.frmsrcordermaterial.command.value="<%=Command.EDIT%>";
	document.frmsrcordermaterial.action="pomaterial_edit.jsp";
	document.frmsrcordermaterial.submit();
}

function cmdListFirst(){
	document.frmsrcordermaterial.command.value="<%=Command.FIRST%>";
	document.frmsrcordermaterial.action="srcpomaterial.jsp";
	document.frmsrcordermaterial.submit();
}

function cmdListPrev(){
	document.frmsrcordermaterial.command.value="<%=Command.PREV%>";
	document.frmsrcordermaterial.action="srcpomaterial.jsp";
	document.frmsrcordermaterial.submit();
}

function cmdListNext(){
	document.frmsrcordermaterial.command.value="<%=Command.NEXT%>";
	document.frmsrcordermaterial.action="srcpomaterial.jsp";
	document.frmsrcordermaterial.submit();
}

function cmdListLast(){
	document.frmsrcordermaterial.command.value="<%=Command.LAST%>";
	document.frmsrcordermaterial.action="srcpomaterial.jsp";
	document.frmsrcordermaterial.submit();
}

function cmdBack(){
	document.frmsrcordermaterial.command.value="<%=Command.BACK%>";
	document.frmsrcordermaterial.action="srcpomaterial.jsp";
	document.frmsrcordermaterial.submit();
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
            <%=textListHeader[SESS_LANGUAGE][6]%> &gt; <%=textListHeader[SESS_LANGUAGE][5]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmsrcordermaterial" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="add_type" value="">			
              <input type="hidden" name="approval_command">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="hidden_material_order_id" value="<%=oidPurchaseOrder%>">
              <input type="hidden" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.fieldNames[PstLocation.TYPE_LOCATION_WAREHOUSE]%>">
              <table width="100%" border="0">
                <tr> 
                  <td colspan="2">&nbsp; 
                  </td>
                </tr>
                <tr> 
                  <td colspan="2"> 
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr> 
                        <td height="21" width="15%"><%=getJspTitle(0,SESS_LANGUAGE,poCode,false)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="90%">&nbsp; 
                          <input type="text" name="<%=frmSrcPurchaseOrder.fieldNames[FrmSrcPurchaseOrder.FRM_FIELD_PRMNUMBER] %>" onkeydown="cmdSearch2(event)"  value="<%= srcPurchaseOrder.getPrmnumber() %>" class="formElemen enterTrigger" size="20">
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" width="9%"><%=getJspTitle(1,SESS_LANGUAGE,poCode,false)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="90%">&nbsp; 
                            <input type="text" name="<%=frmSrcPurchaseOrder.fieldNames[FrmSrcPurchaseOrder.FRM_FIELD_VENDORNAME] %>" onkeydown="cmdSearch2(event)"  value="<%= vendorName %>" class="formElemen enterTrigger" size="30">
                        </td>
                      </tr>
                      <tr> 
                        <td height="43" rowspan="2" valign="top" width="9%" align="left"><%=getJspTitle(2,SESS_LANGUAGE,poCode,false)%></td>
                        <td height="43" rowspan="2" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="90%" valign="top" align="left"> 
                          <input type="radio" name="<%=frmSrcPurchaseOrder.fieldNames[FrmSrcPurchaseOrder.FRM_FIELD_STATUSDATE] %>" <%if(srcPurchaseOrder.getStatusdate()==0){%>checked<%}%> value="0">
                          <%=textListHeader[SESS_LANGUAGE][7]%></td>
                      </tr>
                      <tr align="left"> 
                        <td height="21" width="90%" valign="top"> 
                          <input type="radio" name="<%=frmSrcPurchaseOrder.fieldNames[FrmSrcPurchaseOrder.FRM_FIELD_STATUSDATE] %>" <%if(srcPurchaseOrder.getStatusdate()==1){%>checked<%}%> value="1">
                          <%=textListHeader[SESS_LANGUAGE][8]%> <%=ControlDate.drawDate(frmSrcPurchaseOrder.fieldNames[FrmSrcPurchaseOrder.FRM_FIELD_PRMDATEFROM], srcPurchaseOrder.getPrmdatefrom(),"formElemen",1,-5)%>  &nbsp;<%=textListHeader[SESS_LANGUAGE][9]%>&nbsp; <%=ControlDate.drawDate(frmSrcPurchaseOrder.fieldNames[FrmSrcPurchaseOrder.FRM_FIELD_PRMDATETO], srcPurchaseOrder.getPrmdateto(),"formElemen",1,-5) %> </td>
                      </tr>
                      
                       
                     </table> 
                     
                   <td>
                    <table width="85%" border="0" cellspacing="1" cellpadding="1">   
                      <tr> 
                        <td height="21" valign="top" width="20%" align="left"><%=getJspTitle(3,SESS_LANGUAGE,poCode,false)%></td>
                        <td height="21" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="90%" valign="top" align="left"> 
                            <%
                           int indexPrStatus = 0; 
                           String strPrStatus = "Draft";
                          %>
                          <input type="checkbox" class="formElemen" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcPurchaseOrder.getPrmstatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  

                          <%
                           indexPrStatus = 1; 
                           strPrStatus = "To Be Confirm";
                          %>
                          <input type="checkbox" class="formElemen" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcPurchaseOrder.getPrmstatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  

                          <%
                           indexPrStatus = 10; 
                           strPrStatus = "Approved";
                          %>
                          <input type="checkbox" class="formElemen" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcPurchaseOrder.getPrmstatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  

                          <%
                           indexPrStatus = 2; 
                           strPrStatus = "Final";
                          %>
                          <input type="checkbox" class="formElemen" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcPurchaseOrder.getPrmstatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
                          <%
                           indexPrStatus = 5; 
                           strPrStatus = "Closed";
                          %>
                          <input type="checkbox" class="formElemen" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcPurchaseOrder.getPrmstatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
                          <%
                           indexPrStatus = 7; 
                           strPrStatus = "Posted";
                          %>
                          <input type="checkbox" class="formElemen" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcPurchaseOrder.getPrmstatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  

                        </td>
                      </tr>
                      <tr> 
                        <td height="19" valign="top" width="9%" align="left"><%=getJspTitle(4,SESS_LANGUAGE,poCode,false)%></td>
                        <td height="19" valign="top" width="1%" align="left">:</td>
                        <td height="19" width="90%" valign="top" align="left"> 
                          <% 
                                Vector key_sort = new Vector(1,1); 						  
                                Vector val_sort = new Vector(1,1); 

                                key_sort.add("0");							
                                val_sort.add("Kode");							

                                key_sort.add("1");							
                                val_sort.add("Tanggal");							

                                key_sort.add("2");							
                                val_sort.add("Status");							

                                key_sort.add("3");							
                                val_sort.add("Supplier");							

                                String select_sort = ""+srcPurchaseOrder.getSortby(); 
                                out.println("&nbsp;"+ControlCombo.draw(frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_SORTBY], null, select_sort,key_sort,val_sort,"","formElemen"));
                          %>
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="9%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="90%" valign="top" align="left">&nbsp;</td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                <td height="21" colspan="3" align="left" valign="top">
                  <table width="47%" border="0" cellspacing="0" cellpadding="0">
                        <tr> 
                            <td height="50" valign="top" width="9%" align="left">&nbsp;</td>
                        </tr>
                        <tr>
                          <td class="command" nowrap width="50%"><a class="btn-primary btn-lg" href="javascript:cmdSearch()"><i class="fa fa-search">&nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_SEARCH,true)%></i></a></td>
                             <%if(privAdd){%>
                                <td class="command" nowrap width="72%"><a class="btn-primary btn-lg" href="javascript:cmdAdd()"><i class="fa fa-plus-circle">&nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_ADD,true)%></i></a></td>
                             <%}%>
                        </tr>
                  </table>
                </td>
              </tr>
              </table>
            </form>
            <!-- #EndEditable --></td> 
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
		<td height="18" valign="top" align="left" colspan="2">
		    <table width="36%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <td height="20" valign="top" width="9%" align="left">&nbsp;</td>
                     </tr>
                      <tr>
                        <%if(privAdd){%>
                        <td class="command" nowrap width="72%"><a class="btn-primary btn-lg" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_ADD,true)%></i></a></td>
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
</noframes> 
<!-- #EndTemplate --></html>
