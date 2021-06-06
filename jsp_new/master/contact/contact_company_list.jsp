<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package prochain02 -->
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.form.contact.*" %>
<%@ page import = "com.dimata.posbo.session.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_SUPPLIER); %>
<%@ include file = "../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListHeader[][] = {
	{"No","Kode","Nama Perusahaan","Kontak Person","Telepon","Fax","Handphone","Alamat Bisnis","Alamat Rumah"},
	{"No","Code","Company Name","Person Name","Telephon","Faximile","Mobile Phone","Bussiness Addr","Home Addr"}	
};

public static final String textListTitleHeader[][] = {
	{"Cetak "},
	{"Print "}
};

public Vector drawList(int language,Vector objectClass ,int start){
	Vector result = new Vector(1,1);
	String listAll = "";
	Vector tableHeader = new Vector(1,1);
	
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListHeader[language][0],"3%");
	ctrlist.addHeader(textListHeader[language][1],"5%");
	ctrlist.addHeader(textListHeader[language][2],"15%");
	ctrlist.addHeader(textListHeader[language][3],"15%");
	ctrlist.addHeader(textListHeader[language][4],"8%");
	ctrlist.addHeader(textListHeader[language][5],"8%");
	//ctrlist.addHeader(textListHeader[language][6],"8%");
	ctrlist.addHeader(textListHeader[language][7],"15%");
	//ctrlist.addHeader(textListHeader[language][8],"15%");
	
	// untuk header pada PDF
	tableHeader.add(textListHeader[language][0]);
	tableHeader.add(textListHeader[language][2]);
	tableHeader.add(textListHeader[language][3]);
	tableHeader.add(textListHeader[language][4]);
	tableHeader.add(textListHeader[language][5]);
	tableHeader.add(textListHeader[language][7]);
	
	ctrlist.setLinkRow(1);
	ctrlist.setLinkSufix("");
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();
	ctrlist.setLinkPrefix("javascript:cmdEdit('");
	ctrlist.setLinkSufix("')");
	ctrlist.reset();
	
	if(start<0)
		start =0;
		
	for (int i = 0; i < objectClass.size(); i++) {
		ContactList contactList = (ContactList)objectClass.get(i);
		Vector rowx = new Vector();
		
		start = start + 1;
		rowx.add(String.valueOf(start)); 		
		rowx.add(cekNull(contactList.getContactCode())); 
		rowx.add(cekNull(contactList.getCompName()));
		rowx.add(cekNull(contactList.getPersonName())+" "+cekNull(contactList.getPersonLastname()));
		rowx.add(cekNull(contactList.getTelpNr()));
		rowx.add(cekNull(contactList.getFax()));
		//rowx.add(cekNull(contactList.getTelpMobile()));
		rowx.add(cekNull(contactList.getBussAddress()));
		//rowx.add(cekNull(contactList.getHomeAddr()));

		lstData.add(rowx);
		lstLinkData.add(String.valueOf(contactList.getOID()));
	}
	listAll = ctrlist.draw();
	result.add(listAll);
	result.add(tableHeader);
	return result;
}

public String cekNull(String val){
	if(val.equals("null"))
		val = "";
	return val;	
}
%>

<%
ControlLine ctrLine = new ControlLine();
CtrlContactList ctrlContactList = new CtrlContactList(request);
long oidContactList = FRMQueryString.requestLong(request, "hidden_contact_id");
String companyTitle = "Supplier"; //com.dimata.common.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.common.jsp.JspInfo.CONTACT_COMPANY];	

int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 15;
int vectSize = 0;
String whereClause = "";
String orderClause = PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE];

whereClause = ""+PstContactList.fieldNames[PstContactList.FLD_CONTACT_TYPE]+" = "+PstContactList.EXT_COMPANY+
			  " OR "+PstContactList.fieldNames[PstContactList.FLD_CONTACT_TYPE]+" = "+PstContactList.OWN_COMPANY;

SrcMemberReg srcMemberReg = new SrcMemberReg();

if(iCommand==Command.LIST||iCommand == Command.FIRST || iCommand == Command.PREV ||
   iCommand == Command.NEXT || iCommand == Command.LAST || iCommand == Command.BACK) {
	try	{
		srcMemberReg = (SrcMemberReg)session.getValue("SESS_SUPPLIER");
	}
	catch(Exception e) {
		srcMemberReg = new SrcMemberReg();
	}
	
	if(session.getValue("SESS_SUPPLIER")==null)	{
		srcMemberReg = new SrcMemberReg();		
	}		
	session.putValue("SESS_SUPPLIER",srcMemberReg);
}

vectSize = SessMemberReg.getCountSupplier(srcMemberReg);

if (iCommand == Command.NONE) { 
	iCommand = Command.FIRST;
}

if((iCommand!=Command.FIRST)&&(iCommand!=Command.NEXT)&&(iCommand!=Command.PREV)&&(iCommand!=Command.LIST)) {
	//start = PstContactList.findLimitStart(oidContactList,recordToGet, whereClause);
}

ctrlContactList.action(iCommand , oidContactList, new Vector());

if((iCommand==Command.FIRST)||(iCommand==Command.NEXT)||(iCommand==Command.PREV)||(iCommand==Command.LAST)||(iCommand==Command.LIST)) {
	start = ctrlContactList.actionList(iCommand, start, vectSize, recordToGet);
}

Vector records = SessMemberReg.searchSupplier(srcMemberReg,0,0);
Vector list = drawList(SESS_LANGUAGE,records, start);
Vector listHeaderPdf = new Vector(1,1);
Vector tableHeaderPdf = new Vector(1,1);
Vector contentPdf = new Vector(1,1);
String str = "";

try {
	str = (String)list.get(0);
	tableHeaderPdf = (Vector)list.get(1); 
}catch(Exception e) {
	System.out.println("\n"+e.toString());
}

// untuk proses ke PDF
listHeaderPdf.add(0, SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar "+companyTitle : companyTitle+" List");
listHeaderPdf.add(1, companyAddress.get(0)); // name
listHeaderPdf.add(2, companyAddress.get(1)); // address
listHeaderPdf.add(3, companyAddress.get(2)); // telp/fax

contentPdf.add(listHeaderPdf);
contentPdf.add(srcMemberReg);
contentPdf.add(tableHeaderPdf);

session.removeAttribute("SESS_CONTACT_LIST_PDF");
session.putValue("SESS_CONTACT_LIST_PDF", contentPdf);

%>
<!-- End of Jsp Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<%@include file="../../styles/plugin_component.jsp" %>
<script language="JavaScript">
<!--
function cmdAdd(){
	document.frm_contactlist.command.value="<%=Command.ADD%>";
	document.frm_contactlist.action="contact_company_edit.jsp";
	document.frm_contactlist.submit();
}

function cmdEdit(oid){
	document.frm_contactlist.command.value="<%=Command.EDIT%>";
	document.frm_contactlist.hidden_contact_id.value = oid;
	document.frm_contactlist.action="contact_company_edit.jsp";
	document.frm_contactlist.submit();
}

function cmdBack(){
	document.frm_contactlist.command.value="<%=Command.BACK%>";
	document.frm_contactlist.action="srccontactcompany.jsp";
	document.frm_contactlist.submit();
}

function cmdListFirst(){
	document.frm_contactlist.command.value="<%=Command.FIRST%>";
	document.frm_contactlist.action="contact_company_list.jsp";
	document.frm_contactlist.submit();
}

function cmdListPrev(){
	document.frm_contactlist.command.value="<%=Command.PREV%>";
	document.frm_contactlist.action="contact_company_list.jsp";
	document.frm_contactlist.submit();
}

function cmdListNext(){
	document.frm_contactlist.command.value="<%=Command.NEXT%>";
	document.frm_contactlist.action="contact_company_list.jsp";
	document.frm_contactlist.submit();
}

function cmdListLast(){
	document.frm_contactlist.command.value="<%=Command.LAST%>";
	document.frm_contactlist.action="contact_company_list.jsp";
	document.frm_contactlist.submit();
}

function printForm() {
    if ("<%=typeOfBusinessDetail == 2 %>") {
        window.open("print_out_supplier.jsp?command=<%=Command.LIST %>", "print_out_supplier");
    } else {
        window.open("<%=approot%>/servlet/com.dimata.posbo.report.masterdata.SupplierListPdf", "", "scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
    }
}

//-------------- script control line -------------------
//-->
</script>

<style>
    .line {
        margin-left: 15px;
        border-bottom: 3px solid #cccccc;
    }
    input#myInput {
    margin: 1% 2% 0% 1%;
    width: 15%;
}
div#example_wrapper {
    margin: auto 1%;
}
</style>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" --> 
<SCRIPT language=JavaScript>
<!--
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
    <section class="content-header">
      <h1>Masterdata <small> <%=companyTitle%></small> </h1>
      <ol class="breadcrumb">
        <li><%=companyTitle%></li>
      </ol>
    </section>
   <p class="line"></p>

        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_contactlist" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="hidden_contact_id" value="<%=oidContactList%>">
              <table border="0" width="100%">		
              </table><table id="example" class="table table-striped table-bordered" style="width:100%">
                    <thead>
                      <tr style="background-color: #428bca; color: #fff;">
                        <th><%=textListHeader[SESS_LANGUAGE][0]%></th>
                        <th><%=textListHeader[SESS_LANGUAGE][1]%></th>
                        <th><%=textListHeader[SESS_LANGUAGE][2]%></th>
                        <th><%=textListHeader[SESS_LANGUAGE][3]%></th>
                        <th><%=textListHeader[SESS_LANGUAGE][4]%></th>
                        <th><%=textListHeader[SESS_LANGUAGE][5]%></th>
                        <!--<th><%=textListHeader[SESS_LANGUAGE][6]%></th>-->
                        <th><%=textListHeader[SESS_LANGUAGE][7]%></th>
                      </tr>
                    </thead>
                    <tbody>
                    <%
                       for (int i = 0; i < records.size(); i++) {
                        ContactList contactList = (ContactList)records.get(i);
                        start = start + 1;
                    %>
                    <tr>
                      <td><%=start %></td>
                      <td><a href="javascript:cmdEdit('<%=contactList.getOID()%>')"><%=contactList.getContactCode() %></a></td>
                      <td><%=contactList.getCompName()%></td>
                      <td><%=contactList.getPersonName()+" "+cekNull(contactList.getPersonLastname()) %></td>
                      <td><%=cekNull(contactList.getTelpNr())%></td>
                      <td><%=cekNull(contactList.getFax())%></td>
                      <td><%=cekNull(contactList.getBussAddress())%></td>
                    </tr>
                    <%}%>
                    </tbody>
                  </table>

              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td width="46%" nowrap align="left" class="command"> 
                    <table cellpadding="0" cellspacing="0" border="0">
                      <tr> 
                        <td height="22" valign="middle" colspan="3" width="967"> 
                            <table width="60%" border="0" cellspacing="2" cellpadding="3">
                                <tr>
                                    <%if (privAdd) {%>
                                    <!--td width="3%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, companyTitle, ctrLine.CMD_ADD, true)%>"></a></td-->
                                    <td nowrap width="10%"><a href="javascript:cmdAdd()" class="btn btn-lg btn-primary"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, companyTitle, ctrLine.CMD_ADD, true)%></a></td>
                                                <%}%>
                                                                            <!--td width="3%" nowrap><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, companyTitle, ctrLine.CMD_BACK_SEARCH, true)%>"></a></td-->
                                    <td nowrap class="command" width="10%"><a class="btn btn-lg btn-primary" href="javascript:cmdBack()"><i class="fa fa-arrow-left"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, companyTitle, ctrLine.CMD_BACK_SEARCH, true)%></a></td>
                                    <!--td nowrap valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0"></a></td-->
                                    <td width="30%" nowrap>&nbsp; <a class="btn btn-lg btn-primary" href="javascript:printForm()" class="command" ><i class="fa fa-print"></i> <%=textListTitleHeader[SESS_LANGUAGE][0] + " "%><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar " + companyTitle : companyTitle + " List"%></u></a></td>
                            </table>
                        </td>
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
            <%@include file="../../styletemplate/footer.jsp" %>

        <%}else{%>
            <%@ include file = "../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
  <script>
      $(document).ready( function () {
    $('#example').DataTable({
			"ordering": true
    });
} );
  </script>
</body>
<!-- #EndTemplate --></html>
