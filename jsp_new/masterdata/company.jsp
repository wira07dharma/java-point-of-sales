<%-- 
    Document   : company
    Created on : Jan 24, 2014, 3:45:09 PM
    Created by     : Fitra
--%>


<%@page import="com.dimata.common.form.company.CtrlCompany"%>

<%@page import="com.dimata.util.Command"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.common.entity.payment.StandartRate"%>
<%@page import="com.dimata.common.entity.payment.PstStandartRate"%>
<%@page import="com.dimata.common.entity.location.Location"%>
<%@page import="com.dimata.common.entity.location.PstLocation"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.masterdata.*" %>

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_COMPANY); %>
<%@ include file = "../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListHeader[][] = 
{
	{"No","Kode Perusahaan","Nama Perusahaan","Alamat","Nama Akhir","Alamat","Kota","Provinsi","Negara","Telepon","No Handphone","Fax","Email","Postal Code","Perusahaan"},
	{"No","Company Code","Company Name","Person Address","Person Last Name","Buss Address","Town","Province","Country","Telp_nr","Telp Mobile","Fax","Email Company","Postal Code","Company"}
};

/* this method used to list material unit */
public String drawList(int language,int iCommand,FrmCompany frmObject,Company objEntity,Vector objectClass,long unitId,int start)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListHeader[language][0],"4%");
	ctrlist.addHeader(textListHeader[language][1],"6%");
	ctrlist.addHeader(textListHeader[language][2],"20%");
        ctrlist.addHeader(textListHeader[language][5],"20%");
        ctrlist.addHeader(textListHeader[language][6],"10%");
        ctrlist.addHeader(textListHeader[language][7],"10%");
        ctrlist.addHeader(textListHeader[language][8],"10%");
        ctrlist.addHeader(textListHeader[language][9],"20%");
         ctrlist.addHeader(textListHeader[language][10],"20%");
         ctrlist.addHeader(textListHeader[language][11],"20%");
         ctrlist.addHeader(textListHeader[language][12],"20%");
          ctrlist.addHeader(textListHeader[language][13],"20%");

        ctrlist.setLinkRow(1);
	ctrlist.setLinkPrefix("javascript:cmdEdit('");
	ctrlist.setLinkSufix("')");	
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();
	Vector rowx = new Vector(1,1);
	ctrlist.reset();
	int index = -1;
	if(start<0)
	start = 0;
        Vector val_base=new Vector();
        Vector key_base=new Vector();

        Vector val_curr=new Vector();
        Vector key_curr=new Vector();

        String whereLoc = PstLocation.fieldNames[PstLocation.FLD_TYPE]+"='"+PstLocation.TYPE_LOCATION_WAREHOUSE+"'";
        Vector listLocation = PstLocation.list(0,0,whereLoc,"");
        Vector listCurrency =PstStandartRate.getActiveStandardRateSales();
        
	val_base.add("0");
        key_base.add("-");

        val_curr.add("0");
        key_curr.add("-");

        for(int i=0;i<listLocation.size();i++){
            Location xLocation = (Location)listLocation.get(i);
            val_base.add(""+xLocation.getOID()+"");
            key_base.add(xLocation.getName());
        }

        for(int i=0;i<listCurrency.size();i++){
            StandartRate standartRate = (StandartRate)listCurrency.get(i);
            val_curr.add(""+standartRate.getCurrencyTypeId()+"");
            key_curr.add(standartRate.getCurrencyName());
        }
        
	for(int i = 0; i < objectClass.size(); i++) 
	{
		Company company = (Company)objectClass.get(i);
		rowx = new Vector();
		if(unitId == company.getOID())
		index = i; 
			
		start = start + 1;
               
                /*String locationName = "-";
                for(int k=0;k<listLocation.size();k++){
                Location xLocation = (Location)listLocation.get(k);
                if(xLocation.getOID()==matSales.getLocationId()){
                    locationName = xLocation.getName();
                    break;
                    }
                }*/

                /*String defaultCurrency = "-";
                for(int k=0;k<listCurrency.size();k++){
                StandartRate standartRate = (StandartRate)listCurrency.get(k);
                if(standartRate.getCurrencyTypeId()==matSales.getDefaultCurrencyId()){
                    defaultCurrency = standartRate.getCurrencyName();
                    break;
                    }
                }*/
                
             
						
		
			
			rowx.add(""+start);
			rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(company.getOID())+"')\">"+company.getCompanyCode()+"</a>");
			rowx.add(company.getCompanyName());
                        //adding login id & password by mirahu 20120514
                        rowx.add(company.getBusAddress());
                        rowx.add(company.getTown());
			rowx.add(company.getProvince());
                        rowx.add(company.getCountry());
                        //adding login id & password by mirahu 20120514
                        rowx.add(company.getTelpNr());
                       rowx.add(company.getTelpMobile());
                        rowx.add(company.getFax());
			rowx.add(company.getEmailCompany());
                        rowx.add(company.getPostalCode());
                

		lstData.add(rowx);
	
	
	
        }
	
	return ctrlist.draw();
}
%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidCompany = FRMQueryString.requestLong(request, "hidden_company_id");
String companyTitle = textListHeader[SESS_LANGUAGE][14]; //com.dimata.posbo.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.posbo.jsp.JspInfo.MATERIAL_SALES];

/*variable declaration*/
int recordToGet = 20;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = "";

CtrlCompany ctrlCompany = new CtrlCompany(request);
ControlLine ctrLine = new ControlLine();
Vector listCompany = new Vector(1,1);

/*switch statement */
iErrCode = ctrlCompany.action(iCommand , oidCompany); 
/* end switch*/
FrmCompany frmCompany = ctrlCompany.getForm(); 

/*count list All Sales*/
int vectSize = PstCompany.getCount(whereClause); 

/*switch list Sales*/
if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST))
  {
		start = ctrlCompany.actionList(iCommand, start, vectSize, recordToGet); 
  } 
/* end switch list*/

Company company = ctrlCompany.getCompany();

String locationTitle = textListHeader[SESS_LANGUAGE][0];
msgString =  ctrlCompany.getMessage(); 

/* get record to display */
listCompany = PstCompany.list(start,recordToGet, whereClause , orderClause); 

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listCompany.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else
	 {
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listCompany = PstCompany.list(start,recordToGet, whereClause , orderClause);
}
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function cmdAdd()
{
	document.frmmatcompany.hidden_company_id.value="0";
	document.frmmatcompany.command.value="<%=Command.ADD%>";
	document.frmmatcompany.prev_command.value="<%=prevCommand%>";
	document.frmmatcompany.action="company_old.jsp";
	document.frmmatcompany.submit();
}

function cmdAsk(oidCompany)
{
	document.frmmatcompany.hidden_company_id.value=oidCompany;
	document.frmmatcompany.command.value="<%=Command.ASK%>";
	document.frmmatcompany.prev_command.value="<%=prevCommand%>";
	document.frmmatcompany.action="company_old.jsp";
	document.frmmatcompany.submit();
}

function cmdConfirmDelete(oidCompany)
{
	document.frmmatcompany.hidden_company_id.value=oidCompany;
	document.frmmatcompany.command.value="<%=Command.DELETE%>";
	document.frmmatcompany.prev_command.value="<%=prevCommand%>";
	document.frmmatcompany.action="company_old.jsp";
	document.frmmatcompany.submit();
}

function cmdSave()
{
	document.frmmatcompany.command.value="<%=Command.SAVE%>";
	document.frmmatcompany.prev_command.value="<%=prevCommand%>";
	document.frmmatcompany.action="company_old.jsp";
	document.frmmatcompany.submit();
}

function cmdEdit(oidCompany)
{
	document.frmmatcompany.hidden_company_id.value=oidCompany;
	document.frmmatcompany.command.value="<%=Command.EDIT%>";
	document.frmmatcompany.prev_command.value="<%=prevCommand%>";
	document.frmmatcompany.action="company_old.jsp";
	document.frmmatcompany.submit();
}

function cmdCancel(oidCompany)
{
	document.frmmatcompany.hidden_company_id.value=oidCompany;
	document.frmmatcompany.command.value="<%=Command.EDIT%>";
	document.frmmatcompany.prev_command.value="<%=prevCommand%>";
	document.frmmatcompany.action="company_old.jsp";
	document.frmmatcompany.submit();
}

function cmdBack()
{
	document.frmmatcompany.command.value="<%=Command.BACK%>";
	document.frmmatcompany.action="company_old.jsp";
	document.frmmatcompany.submit();
}

function cmdListFirst()
{
	document.frmmatcompany.command.value="<%=Command.FIRST%>";
	document.frmmatcompany.prev_command.value="<%=Command.FIRST%>";
	document.frmmatcompany.action="company_old.jsp";
	document.frmmatcompany.submit();
}

function cmdListPrev()
{
	document.frmmatcompany.command.value="<%=Command.PREV%>";
	document.frmmatcompany.prev_command.value="<%=Command.PREV%>";
	document.frmmatcompany.action="company_old.jsp";
	document.frmmatcompany.submit();
}

function cmdListNext()
{
	document.frmmatcompany.command.value="<%=Command.NEXT%>";
	document.frmmatcompany.prev_command.value="<%=Command.NEXT%>";
	document.frmmatcompany.action="company_old.jsp";
	document.frmmatcompany.submit();
}

function cmdListLast()
{
	document.frmmatcompany.command.value="<%=Command.LAST%>";
	document.frmmatcompany.prev_command.value="<%=Command.LAST%>";
	document.frmmatcompany.action="company_old.jsp";
	document.frmmatcompany.submit();
}

//-------------- script form image -------------------

function cmdDelPict(oidCompany)
{
	document.frmimage.hidden_company_id.value=oidcompany;
	document.frmimage.command.value="<%=Command.POST%>";
	document.frmimage.action="company_old.jsp";
	document.frmimage.submit();
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
    <link href="../stylesheets/general_home_style.css" type="text/css"
rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../styles/tab.css" type="text/css">
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
      <%@ include file = "../main/header.jsp" %>
      <!-- #EndEditable --></td>
  </tr>
  <tr>
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
  <%}else{%>
   <tr bgcolor="#FFFFFF">
    <td height="10" ID="MAINMENU">
      <%@include file="../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
            Masterdata &gt; Company </td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmmatcompany" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_company_id" value="<%=oidCompany%>">
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
                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;</td>
						</tr>
                      <%
							try{
							%>
                      <tr align="left" valign="top"> 
                        <td height="22" valign="middle" colspan="3"> <%=drawList(SESS_LANGUAGE,iCommand,frmCompany, company ,listCompany,oidCompany,start)%> </td>
                      </tr>
                      <% 
						  }catch(Exception exc){ 
						  }%>
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
                              <!--td width="18%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,companyTitle,ctrLine.CMD_ADD,true)%>"></a></td>
                              <td nowrap width="82%"><a href="javascript:cmdAdd()" class="command"><%=ctrLine.getCommand(SESS_LANGUAGE,"Company",ctrLine.CMD_ADD,true)%></a></td-->
                              <td height="22" valign="middle" colspan="3" width="172"><a href="javascript:cmdAdd()" class="btn-primary btn-lg"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,locationTitle,ctrLine.CMD_ADD,true)%></a></td>
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
					  if((iCommand ==Command.ADD)
						||(iCommand==Command.EDIT)
						||(iCommand==Command.ASK)					  
					    ||((iCommand==Command.SAVE) && iErrCode>0) || (iCommand==Command.DELETE && iErrCode>0))
					  {
					%>
                    <table width="100%" border="0" cellspacing="2" cellpadding="2">
                      <tr> 
                        <td colspan="2" class="comment" height="30"><u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Editor "+locationTitle : locationTitle+" Editor"%></u></td>
                      </tr>
                      
                      
                      
                             
                      <tr> 
                        <td height="100%" width="100%" colspan="2"> 
                          <table border="0" cellspacing="1" cellpadding="1" width="100%">
                            <tr align="left"> 
                              <td width="12%"><%=textListHeader[SESS_LANGUAGE][1]%></td>
                              <td width="1%" valign="top">:</td>
                              <td width="87%" valign="top"> 
                                <input type="text" name="<%=frmCompany.fieldNames[FrmCompany.FRM_FLD_COMPANY_CODE] %>"  value="<%= company.getCompanyCode() %>" class="formElemen" size="20">
                                * <%= frmCompany.getErrorMsg(FrmCompany.FRM_FLD_COMPANY_CODE) %> </td>
                            </tr>
                            <tr align="left"> 
                              <td width="12%"><%=textListHeader[SESS_LANGUAGE][2]%></td>
                              <td width="1%" valign="top">:</td>
                              <td width="87%" valign="top"> 
                                <input type="text" name="<%=frmCompany.fieldNames[FrmCompany.FRM_FLD_COMPANY_NAME] %>"  value="<%= company.getCompanyName() %>" class="formElemen" size="40">
                                * <%= frmCompany.getErrorMsg(FrmCompany.FRM_FLD_COMPANY_NAME) %></td>
                            </tr>
                            <tr align="left"> 
                              <td width="12%"><%=textListHeader[SESS_LANGUAGE][5]%></td>
                              <td width="1%" valign="top">:</td>
                              <td width="87%" valign="top"> 
                                 <textarea   name="<%=frmCompany.fieldNames[FrmCompany.FRM_FLD_BUSS_ADDRESS] %>"  class="formElemen" cols="50" ><%= company.getBusAddress() %></textarea>
                              </td>
                            </tr>
                            <tr align="left"> 
                              <td width="12%"><%=textListHeader[SESS_LANGUAGE][6]%></td>
                              <td width="1%" valign="top">:</td>
                              <td width="87%" valign="top"> 
                                <input type="text" name="<%=frmCompany.fieldNames[FrmCompany.FRM_FLD_TOWN] %>"  value="<%= company.getTown() %>" class="formElemen" size="40">
                              </td>
                            </tr>
                            <tr align="left"> 
                              <td width="12%"><%=textListHeader[SESS_LANGUAGE][7]%></td>
                              <td width="1%" valign="top">:</td>
                              <td width="87%" valign="top"> 
                                <input type="text" name="<%=frmCompany.fieldNames[FrmCompany.FRM_FLD_PROVINCE] %>"  value="<%= company.getProvince() %>" class="formElemen" size="30">
                              </td>
                            </tr>
                            <tr align="left"> 
                              <td width="12%"><%=textListHeader[SESS_LANGUAGE][8]%></td>
                              <td width="1%" valign="top">:</td>
                              <td width="87%" valign="top"> 
                                <input type="text" name="<%=frmCompany.fieldNames[FrmCompany.FRM_FLD_COUNTRY] %>"  value="<%= company.getCountry() %>" class="formElemen" size="30">
                              </td>
                            </tr>
                            
                            <!-- update by fitra -->
                           
                            
                            
                            
                        
                            <%  
							/*
							Vector allContact = PstContactList.list(0,0,PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]+"="+company.getContactId(),"");
							String contactName = "";
							if(allContact != null && allContact.size()>0){
								ContactList contact = (ContactList)allContact.get(0);										
								contactName = contact.getPersonName()+ " "+contact.getPersonLastname();
							}
							*/
						    %>
							<!--	   
                            <tr align="left"> 
                              <td width="12%"><%//=textListHeader[SESS_LANGUAGE][11]%></td>
                              <td width="1%" valign="top">:</td>
                              <td width="87%" valign="top"> 
                                <input type="text" readonly name="contact_name" value="<%//=contactName%>" size="40" class="formElemen">
                                &nbsp;<a href="javascript:srcContact()">CHK</a> 
                              </td>
                            </tr>
							-->
                         <tr align="left"> 
                              <td width="12%"><%=textListHeader[SESS_LANGUAGE][9]%></td>
                              <td width="1%" valign="top">:</td>
                              <td width="87%" valign="top"> 
                                <input type="text" name="<%=frmCompany.fieldNames[FrmCompany.FRM_FLD_TELP_NR] %>"  value="<%= company.getTelpNr() %>" class="formElemen" size="30">
                              </td>
                            </tr>
                                                        
                                                        
                            <%--add opie 13-06-2012 untuk penambahan percentase tax dan service --%>
                           <tr align="left"> 
                              <td width="12%"><%=textListHeader[SESS_LANGUAGE][10]%></td>
                              <td width="1%" valign="top">:</td>
                              <td width="87%" valign="top"> 
                                <input type="text" name="<%=frmCompany.fieldNames[FrmCompany.FRM_FLD_TELP_MOBILE] %>"  value="<%= company.getTelpMobile() %>" class="formElemen" size="30">
                              </td>
                            </tr>
                            <tr align="left"> 
                              <td width="12%"><%=textListHeader[SESS_LANGUAGE][11]%></td>
                              <td width="1%" valign="top">:</td>
                              <td width="87%" valign="top"> 
                                <input type="text" name="<%=frmCompany.fieldNames[FrmCompany.FRM_FLD_FAX] %>"  value="<%= company.getFax() %>" class="formElemen" size="30">
                              </td>
                            </tr>
                           
                           <tr align="left"> 
                              <td width="12%"><%=textListHeader[SESS_LANGUAGE][12]%></td>
                              <td width="1%" valign="top">:</td>
                              <td width="87%" valign="top"> 
                                <input type="text" name="<%=frmCompany.fieldNames[FrmCompany.FRM_FLD_EMAIL_COMPANY] %>"  value="<%= company.getEmailCompany() %>" class="formElemen" size="30">
                              </td>
                            </tr>
                            
                             
                           <tr align="left"> 
                              <td width="12%"><%=textListHeader[SESS_LANGUAGE][13]%></td>
                              <td width="1%" valign="top">:</td>
                              <td width="87%" valign="top"> 
                                <input type="text" name="<%=frmCompany.fieldNames[FrmCompany.FRM_FLD_POSTAL_CODE] %>"  value="<%= company.getPostalCode() %>" class="formElemen" size="30">
                              </td>
                            </tr>
                            <%-- finish eyek 13-06-2012--%>
                            
                          </table>
                        </td>
                      </tr>
                
                      
                      
                
                
                <tr align="left" valign="top" > 
                  <td colspan="3" class="command"> 
                    <%
                    ctrLine.setLocationImg(approot+"/images");

                    // set image alternative caption
                    ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,companyTitle,ctrLine.CMD_SAVE,true));
                    ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,companyTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,companyTitle,ctrLine.CMD_BACK,true)+" List");							
                    ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,companyTitle,ctrLine.CMD_ASK,true));							
                    ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,companyTitle,ctrLine.CMD_CANCEL,false));														

                    ctrLine.initDefault();
                    ctrLine.setTableWidth("80%");
                    String scomDel = "javascript:cmdAsk('"+oidCompany+"')";
                    String sconDelCom = "javascript:cmdConfirmDelete('"+oidCompany+"')";
                    String scancel = "javascript:cmdEdit('"+oidCompany+"')"; 
                    ctrLine.setCommandStyle("command");
                    ctrLine.setColCommStyle("command");

                    // set command caption
                    ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,companyTitle,ctrLine.CMD_SAVE,true));
                    ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,companyTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,companyTitle,ctrLine.CMD_BACK,true)+" List");							
                    ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,companyTitle,ctrLine.CMD_ASK,true));							
                    ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,companyTitle,ctrLine.CMD_DELETE,true));														
                    ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,companyTitle,ctrLine.CMD_CANCEL,false));									

                    if (privDelete){
                            ctrLine.setConfirmDelCommand(sconDelCom);
                            ctrLine.setDeleteCommand(scomDel);
                            ctrLine.setEditCommand(scancel);
                    }else{ 
                            ctrLine.setConfirmDelCaption("");
                            ctrLine.setDeleteCaption("");
                            ctrLine.setEditCaption("");
                    }

                    if(iCommand == Command.EDIT && privUpdate == false){
                            ctrLine.setSaveCaption("");
                    }

                    if (privAdd == false){
                            ctrLine.setAddCaption("");
                    }
                    %>
                    
                    <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%></td>
                </tr>
              </table>
               <%}%> 
            
            <!-- #EndEditable -->
          </td> 
        </tr> 
      </table>
            
      </form>      
    </td>
  </tr>
  
   </table>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 

 <%if(menuUsed == MENU_ICON){%>
            <%@include file="../styletemplate/footer.jsp" %>

        <%}else{%>
            <%@ include file = "../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
