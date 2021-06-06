<%-- 
    Document   : voucher
    Created on : Aug 24, 2015, 5:48:54 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.gui.jsp.ControlDate"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.posbo.entity.masterdata.Voucher"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstVoucher"%>
<%@page import="com.dimata.posbo.form.masterdata.FrmVoucher"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.posbo.form.masterdata.CtrlVoucher"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@ page language = "java" %>
<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG); %>
<%@ include file = "../../main/checkuser.jsp" %>
<%
boolean privEditPrice = true;
%>

<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListHeader[][] =
{
	{"Kode","Nama","Jumlah", "Type","Create Date","Expired Date","Outlet","Status","Issued Date","Authorized","No"},
	{"Kode","Name","value", "Type","Create Date","Expired Date","Outlet","Status","Issued Date","Authorized","No"}
};

/* this method used to list material department */
public String drawList(int language,Vector objectClass,long costingId, int start)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
        
	ctrlist.dataFormat(textListHeader[language][10],"2%","center","left");
	ctrlist.dataFormat(textListHeader[language][0],"10%","center","left");
        ctrlist.dataFormat(textListHeader[language][1],"10%","center","left");
        ctrlist.dataFormat(textListHeader[language][2],"10%","center","left");
        ctrlist.dataFormat(textListHeader[language][3],"10%","center","left");
        ctrlist.dataFormat(textListHeader[language][4],"10%","center","left");
        ctrlist.dataFormat(textListHeader[language][5],"10%","center","left");
        ctrlist.dataFormat(textListHeader[language][6],"13%","center","left");
        ctrlist.dataFormat(textListHeader[language][7],"5%","center","left");
        ctrlist.dataFormat(textListHeader[language][8],"10%","center","left");
        ctrlist.dataFormat(textListHeader[language][9],"10%","center","left");

	ctrlist.setLinkRow(1);
	ctrlist.setLinkSufix("");
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();
	ctrlist.setLinkPrefix("javascript:cmdEdit('");
	ctrlist.setLinkSufix("')");
	ctrlist.reset();
	
	for(int i=0; i<objectClass.size(); i++){
		Voucher voucher = (Voucher)objectClass.get(i);
		Vector rowx = new Vector();
		
		rowx.add(""+(i+start+1));
		rowx.add(""+voucher.getVoucherNo());
		rowx.add(""+voucher.getVoucherName());
                rowx.add(""+voucher.getVoucherNominal());
                rowx.add(voucher.getVoucherType()==1?"Regular":"Compliment");
                rowx.add(""+voucher.getVoucherCreateDate());
                rowx.add(""+voucher.getVoucherExpired());
                rowx.add(""+voucher.getVoucherOutletName());
                rowx.add(voucher.getVoucherStatus()==0?"Draf":voucher.getVoucherStatus()==2?"Final":"Posted");
                if(voucher.getVoucherStatus()==2){//final
                    rowx.add(voucher.getVoucherIssuedDate());
                    rowx.add(voucher.getVoucherAuthorizedName());
                }else{//draft
                    rowx.add("");
                    rowx.add("");
                }
		lstData.add(rowx);
		lstLinkData.add(String.valueOf(voucher.getOID()));
	}
        
	return ctrlist.draw();
}
%>

<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidVoucher = FRMQueryString.requestLong(request, "hidden_voucher_id");
int searchType = FRMQueryString.requestInt(request, "searchType");
String defaultLocation = PstSystemProperty.getValueByName("OUTLET_DEFAULT_LOCATION");

/*variable declaration*/
int recordToGet = 15;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
whereClause+="("+PstVoucher.fieldNames[PstVoucher.FLD_VOUCHEROUTLET]+"='"+defaultLocation+"' OR "+PstVoucher.fieldNames[PstVoucher.FLD_VOUCHEROUTLET]+"='0')";
String orderClause = "";

//costing Title
String costingTitle = "Voucher";//i_pstDocType.getDocTitle(docType);

CtrlVoucher ctrlVoucher = new CtrlVoucher(request);
ControlLine ctrLine = new ControlLine();
Vector listVoucher = new Vector(1,1);
Vector val_voucher_type = new Vector(1,1);
Vector key_voucher_type = new Vector(1,1);
val_voucher_type.add("1");
key_voucher_type.add("Regular");
val_voucher_type.add("2");
key_voucher_type.add("Compliment");

Vector val_voucher_search_type = new Vector(1,1);
Vector key_voucher_search_type = new Vector(1,1);
val_voucher_search_type.add("0");
key_voucher_search_type.add("All");
val_voucher_search_type.add("1");
key_voucher_search_type.add("Regular");
val_voucher_search_type.add("2");
key_voucher_search_type.add("Compliment");

/*switch statement */
iErrCode = ctrlVoucher.action(iCommand , oidVoucher);
/* end switch*/
FrmVoucher frmVoucher = ctrlVoucher.getForm();
if(iCommand==Command.SEARCH){
    if(searchType != 0){
        whereClause+=" AND "+PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERTYPE]+" = "+searchType;
    }
}
/*count list All Costing*/
int vectSize = PstVoucher.getCount(whereClause);

/*switch list Costing*/
if((iCommand == Command.FIRST || iCommand == Command.PREV )||(iCommand == Command.NEXT || iCommand == Command.LAST)){
    start = ctrlVoucher.actionList(iCommand, start, vectSize, recordToGet);
}
/* end switch list*/

Voucher voucher = ctrlVoucher.getVoucher();
msgString =  ctrlVoucher.getMessage();

/* get record to display */

listVoucher = PstVoucher.listJoin(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 after delete*/
if (listVoucher.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;
	 else
	 {
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST;
	 }
	 listVoucher = PstVoucher.listJoin(start,recordToGet, whereClause , orderClause);

}
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdAdd()
{
	document.frmvoucher.hidden_voucher_id.value="0";
	document.frmvoucher.command.value="<%=Command.ADD%>";
	document.frmvoucher.prev_command.value="<%=prevCommand%>";
	document.frmvoucher.action="voucher.jsp";
	document.frmvoucher.submit();
}

function cmdAsk(oidCosting)
{
	document.frmvoucher.hidden_voucher_id.value=oidCosting;
	document.frmvoucher.command.value="<%=Command.ASK%>";
	document.frmvoucher.prev_command.value="<%=prevCommand%>";
	document.frmvoucher.action="voucher.jsp";
	document.frmvoucher.submit();
}

function cmdConfirmDelete(oidCosting)
{
	document.frmvoucher.hidden_voucher_id.value=oidCosting;
	document.frmvoucher.command.value="<%=Command.DELETE%>";
	document.frmvoucher.prev_command.value="<%=prevCommand%>";
	document.frmvoucher.action="voucher.jsp";
	document.frmvoucher.submit();
}
function cmdSave()
{
	document.frmvoucher.command.value="<%=Command.SAVE%>";
	document.frmvoucher.prev_command.value="<%=prevCommand%>";
	document.frmvoucher.action="voucher.jsp";
	document.frmvoucher.submit();
}

function cmdEdit(oidCosting)
{
	document.frmvoucher.hidden_voucher_id.value=oidCosting;
	document.frmvoucher.command.value="<%=Command.EDIT%>";
	document.frmvoucher.prev_command.value="<%=prevCommand%>";
	document.frmvoucher.action="voucher.jsp";
	document.frmvoucher.submit();
}

function cmdCancel(oidCosting)
{
	document.frmvoucher.hidden_voucher_id.value=oidCosting;
	document.frmvoucher.command.value="<%=Command.EDIT%>";
	document.frmvoucher.prev_command.value="<%=prevCommand%>";
	document.frmvoucher.action="voucher.jsp";
	document.frmvoucher.submit();
}

function cmdBack()
{
	document.frmvoucher.command.value="<%=Command.BACK%>";
	document.frmvoucher.action="voucher.jsp";
	document.frmvoucher.submit();
}

function cmdListFirst()
{
	document.frmvoucher.command.value="<%=Command.FIRST%>";
	document.frmvoucher.prev_command.value="<%=Command.FIRST%>";
	document.frmvoucher.action="voucher.jsp";
	document.frmvoucher.submit();
}

function cmdListPrev()
{
	document.frmvoucher.command.value="<%=Command.PREV%>";
	document.frmvoucher.prev_command.value="<%=Command.PREV%>";
	document.frmvoucher.action="voucher.jsp";
	document.frmvoucher.submit();
}

function cmdListNext()
{
	document.frmvoucher.command.value="<%=Command.NEXT%>";
	document.frmvoucher.prev_command.value="<%=Command.NEXT%>";
	document.frmvoucher.action="voucher.jsp";
	document.frmvoucher.submit();
}

function cmdListLast()
{
	document.frmvoucher.command.value="<%=Command.LAST%>";
	document.frmvoucher.prev_command.value="<%=Command.LAST%>";
	document.frmvoucher.action="voucher.jsp";
	document.frmvoucher.submit();
}

function checkSearchType(){
    document.frmvoucher.command.value="<%=Command.SEARCH%>";
    document.frmvoucher.action="voucher.jsp";
    document.frmvoucher.submit();
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
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../stylesheets/general_home_style.css" type="text/css"
rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
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
      <%@ include file = "../../main/header.jsp" %>
      <!-- #EndEditable --></td>
  </tr>
  <tr>
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
  <%}else{%>
   <tr bgcolor="#FFFFFF">
    <td height="10" ID="MAINMENU">
      <%@include file="../../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            Masterdata &gt; <%=costingTitle%><!-- #EndEditable --></td>
        </tr>
        
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frmvoucher" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_voucher_id" value="<%=oidVoucher%>">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top">
                  <td height="8"  colspan="3">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="8" valign="middle" colspan="3">
                          <hr size="1">
                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar "+costingTitle : costingTitle+" List"%></u></td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="14" valign="middle" colspan="3" class="comment">
                            &nbsp;                       
                           <%=ControlCombo.draw("searchType","formElemen", null, ""+searchType, val_voucher_search_type, key_voucher_search_type, "onChange=\"javascript:checkSearchType(this)\"")%>
                        </td>
                      </tr>
                      <%
                        if (listVoucher.size()>0){
                        %>
                          <tr align="left" valign="top">
                            <td height="22" valign="middle" colspan="3"> <%=drawList(SESS_LANGUAGE,listVoucher,oidVoucher,start)%> </td>
                          </tr>
                      <%}%>
                      <tr align="left" valign="top">
                        <td height="8" align="left" colspan="3" class="command">
                          <span class="command">
                          <%
                                int cmd = 0;
                                        if ((iCommand == Command.FIRST || iCommand == Command.PREV )||
                                             (iCommand == Command.NEXT || iCommand == Command.LAST))
                                                     cmd =iCommand;
                                else{
                                       if(iCommand == Command.NONE || prevCommand == Command.NONE)
                                             cmd = Command.FIRST;
                                       else
                                             cmd =prevCommand;
                                }
                         %>
                         <% ctrLine.setLocationImg(approot+"/images");
                            ctrLine.initDefault();
                         %>
                         <%=ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet)%> </span> </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3">
                         <%if(iCommand!=Command.ADD && iCommand!=Command.EDIT && iCommand!=Command.ASK && iErrCode==FRMMessage.NONE){%>
                          <table width="17%" border="0" cellspacing="2" cellpadding="3">
                            <tr>
                              <%if(privAdd){%>
                                <td nowrap width="82%"><a class="btn-primary btn-lg" href="javascript:cmdAdd()" class="command"><i class="fa fa-plus-circle"> &nbsp; <%=ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_ADD,true)%></i></a></td>
                              <%}%>
                            </tr>
                          </table>
                          <%}%>
			</td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr align="left" valign="top">
                  <td height="8" valign="middle" colspan="3">
                    <%
                        if((iCommand == Command.ADD)
                           || (iCommand == Command.EDIT)
                               || (iCommand == Command.ASK)
                               || ((iCommand==Command.SAVE || iCommand==Command.DELETE) && (iErrCode>0))
                              )
                        {
                      %>
                    <table width="100%" border="0" cellspacing="2" cellpadding="0">
                      <tr align="left">
                        <td colspan="10" class="comment" height="30"><u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Editor "+costingTitle : costingTitle+" Editor"%></u></td>
                      
                      <tr align="left">
                        <td width="9%">&nbsp;<%=textListHeader[SESS_LANGUAGE][0]%></td>
                        <td width="1%">:
                        <td colspan="8" width="90%">
                          <input type="text" name="<%=frmVoucher.fieldNames[frmVoucher.FRM_FIELD_VOUCHERNO] %>"  value="<%=voucher.getVoucherNo() %>" class="formElemen" size="30">
                          * <%=frmVoucher.getErrorMsg(frmVoucher.FRM_FIELD_VOUCHERNO)%>
                    
                      <tr align="left">
                        <td width="9%">&nbsp;<%=textListHeader[SESS_LANGUAGE][1]%></td>
                        <td width="1%">:
                        <td colspan="8" width="90%">
                          <input type="text" name="<%=frmVoucher.fieldNames[frmVoucher.FRM_FIELD_VOUCHERNAME] %>"  value="<%=voucher.getVoucherName() %>" class="formElemen" size="30">
                          * <%=frmVoucher.getErrorMsg(frmVoucher.FRM_FIELD_VOUCHERNAME)%>
                          
                       <tr align="left">
                        <td width="9%">&nbsp;<%=textListHeader[SESS_LANGUAGE][2]%></td>
                        <td width="1%">:
                        <td colspan="8" width="90%">
                          <input type="text" name="<%=frmVoucher.fieldNames[frmVoucher.FRM_FIELD_VOUCHERNOMINAL] %>"  value="<%=voucher.getVoucherNominal()%>" class="formElemen" size="30">
                          * <%=frmVoucher.getErrorMsg(frmVoucher.FRM_FIELD_VOUCHERNOMINAL)%>   
                       
                       <tr align="left">
                        <td width="9%">&nbsp;<%=textListHeader[SESS_LANGUAGE][3]%></td>
                        <td width="1%">:
                        <td colspan="8" width="90%">                     
                           <%=ControlCombo.draw(frmVoucher.fieldNames[frmVoucher.FRM_FIELD_VOUCHERTYPE],"formElemen", null, ""+voucher.getVoucherType(), val_voucher_type, key_voucher_type, "")%>
                            * <%=frmVoucher.getErrorMsg(frmVoucher.FRM_FIELD_VOUCHERTYPE)%>      
                      
                      <tr align="left">
                        <td width="9%">&nbsp;<%=textListHeader[SESS_LANGUAGE][4]%></td>
                        <td width="1%">:
                        <td colspan="8" width="90%">
                            <%=ControlDate.drawDateWithStyle(frmVoucher.fieldNames[frmVoucher.FRM_FIELD_VOUCHERCREATEDATE], voucher.getVoucherCreateDate()==null?new Date():voucher.getVoucherCreateDate(), 1,-70, "formElemen", "") %> **
                       
                       <tr align="left">
                        <td width="9%">&nbsp;<%=textListHeader[SESS_LANGUAGE][5]%></td>
                        <td width="1%">:
                        <td colspan="8" width="90%">
                            <%=ControlDate.drawDateWithStyle(frmVoucher.fieldNames[frmVoucher.FRM_FIELD_VOUCHEREXPIRED], voucher.getVoucherExpired()==null?new Date():voucher.getVoucherExpired(), 1,-70, "formElemen", "") %> **
                       
                        <tr align="left">
                        <td width="9%">&nbsp;<%=textListHeader[SESS_LANGUAGE][6]%></td>
                        <td width="1%">:
                        <td colspan="8" width="90%">
                            <%
                                Vector val_lokasi_type = new Vector(1,1);
                                Vector key_lokasi_type = new Vector(1,1);
                                Vector vlocation = new Vector();
                                vlocation = PstLocation.listLocationStore(0, 0, "", "");
                                val_lokasi_type.add("0");
                                key_lokasi_type.add("ALL Location");
                                if(vlocation.size()>0){
                                    for (int i = 0; i < vlocation.size(); i++) {
                                        Location location = (Location) vlocation.get(i);
                                        val_lokasi_type.add(""+location.getOID());
                                        key_lokasi_type.add(""+location.getName());
                                    }
                                }
                               %>                         
                            <%=ControlCombo.draw(frmVoucher.fieldNames[frmVoucher.FRM_FIELD_VOUCHEROUTLET],"formElemen", null, ""+voucher.getVoucherOutlet(), val_lokasi_type, key_lokasi_type, "")%>
                      
                      <tr align="left">
                        <td width="9%">&nbsp;<%=textListHeader[SESS_LANGUAGE][7]%></td>
                        <td width="1%">:
                        <td colspan="8" width="90%">
                            <%
                                Vector val_status_type = new Vector(1,1);
                                Vector key_status_type = new Vector(1,1);
                                val_status_type.add("0");
                                key_status_type.add("Draft");
                               %>                         
                            <%=ControlCombo.draw(frmVoucher.fieldNames[frmVoucher.FRM_FIELD_VOUCHERSTATUS],"formElemen", null, ""+voucher.getVoucherStatus(), val_status_type, key_status_type, "")%>
      
                      <tr align="left" >
                        <td colspan="10" class="command" valign="top">                      
                      </tr>
                      <tr align="left" >
                        <td colspan="10" class="command" valign="top">                      
                      </tr>
                      <tr align="left" >
                        <td colspan="10" class="command" valign="top">
                          <%
                                ctrLine.setLocationImg(approot+"/images");

                                // set image alternative caption
                                ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_SAVE,true));
                                ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_BACK,true)+" List");
                                ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_ASK,true));
                                ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_CANCEL,false));

                                ctrLine.initDefault();
                                ctrLine.setTableWidth("80%");
                                String scomDel = "javascript:cmdAsk('"+oidVoucher+"')";
                                String sconDelCom = "javascript:cmdConfirmDelete('"+oidVoucher+"')";
                                String scancel = "javascript:cmdEdit('"+oidVoucher+"')";
                                ctrLine.setCommandStyle("command");
                                ctrLine.setColCommStyle("command");

                                // set command caption
                                ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_SAVE,true));
                                ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_BACK,true)+" List");
                                ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_ASK,true));
                                ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_DELETE,true));
                                ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_CANCEL,false));


                                if (privDelete){
                                        ctrLine.setConfirmDelCommand(sconDelCom);
                                        ctrLine.setDeleteCommand(scomDel);
                                        ctrLine.setEditCommand(scancel);
                                }else{
                                        ctrLine.setConfirmDelCaption("");
                                        ctrLine.setDeleteCaption("");
                                        ctrLine.setEditCaption("");
                                }

                                if(privAdd == false  && privUpdate == false){
                                        ctrLine.setSaveCaption("");
                                }

                                if (privAdd == false){
                                        ctrLine.setAddCaption("");
                                }
                                ctrLine.setAddCaption("");
                          %>
                          <%
                            if( (iCommand==Command.ADD)
                                || (iCommand==Command.EDIT)
                                    || (iCommand==Command.ASK)
                                || ((iCommand==Command.SAVE || iCommand==Command.DELETE) && iErrCode>0)
                                  )
                            {
                            out.println(ctrLine.drawImage(iCommand, iErrCode, msgString));
                            }
                          %>
                      </tr>
                    </table>
                    <script language="javascript">
                            document.frmcategory.<%=frmVoucher.fieldNames[frmVoucher.FRM_FIELD_VOUCHERNO]%>.focus();
                    </script>
                    <%}%>
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
            <%@include file="../../styletemplate/footer.jsp" %>

        <%}else{%>
            <%@ include file = "../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
