<% 
/* 
 * Page Name  		:  memberregistrationhistory.jsp
 * Created on 		:  [date] [time] AM/PM 
 * 
 * @author  		:  [authorName] 
 * @version  		:  [version] 
 */

/*******************************************************************
 * Page Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 			: [output ...] 
 *******************************************************************/
%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package posbo -->
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.masterdata.*" %>
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%//@ page import = "com.dimata.posbo.entity.admin.*" %>

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_MEMBER); %>
<%@ include file = "../main/checkuser.jsp" %>


<%!
public static final String textListTitle[][] =
{
	{"Status Daftar","Harus diisi"},
	{"Member History","required"}
};

public static final String textListHeader[][] =
{
	{"Tipe Member","Status","Barcode","Nama","Alamat","No Telp","No HP","Tempat/Tgl Lahir","Jenis Kelamin","Agama","No ID","Nama Studio","Tgl Mendaftar","Tgl Mulai Berlaku","Tgl Akhir Berlaku"},
	{"Member Type","Status","Barcode","Name","Address","Telp No", "HP No","Place/Birthdate","Sex","Religion","ID Number","Studio Name","Registration Date","Valid Start Date","Valid Expired Date"}
};
	
public String drawList(Vector objectClass,  long memberRegistrationHistoryId, int language)
{
	if(objectClass!=null&&objectClass.size()>0)
	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("70%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader("Registration Date","34%");
		ctrlist.addHeader("Valid Start Date","33%");
		ctrlist.addHeader("Valid Expired Date","33%");
	
		ctrlist.setLinkRow(0);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int index = -1;
	
		for (int i = 0; i < objectClass.size(); i++) {
			MemberRegistrationHistory memberRegistrationHistory = (MemberRegistrationHistory)objectClass.get(i);
			 Vector rowx = new Vector();
			 if(memberRegistrationHistoryId == memberRegistrationHistory.getOID())
				 index = i;
	
			String str_dt_RegistrationDate = ""; 
			try
			{
				Date dt_RegistrationDate = memberRegistrationHistory.getRegistrationDate();
				if(dt_RegistrationDate==null)
				{
					dt_RegistrationDate = new Date();
				}	
				str_dt_RegistrationDate = Formater.formatDate(dt_RegistrationDate, language, "dd MMMM yyyy");
			}
				catch(Exception e){ str_dt_RegistrationDate = ""; 
			}
	
			rowx.add(str_dt_RegistrationDate);
	
			String str_dt_ValidStartDate = ""; 
			try
			{
				Date dt_ValidStartDate = memberRegistrationHistory.getValidStartDate();
				if(dt_ValidStartDate==null){
					dt_ValidStartDate = new Date();
				}	
				str_dt_ValidStartDate = Formater.formatDate(dt_ValidStartDate, language, "dd MMMM yyyy");
			}
				catch(Exception e){ str_dt_ValidStartDate = ""; 
			}
	
			rowx.add(str_dt_ValidStartDate);
	
			String str_dt_ValidExpiredDate = ""; 
			try
			{
				Date dt_ValidExpiredDate = memberRegistrationHistory.getValidExpiredDate();
				if(dt_ValidExpiredDate==null){
					dt_ValidExpiredDate = new Date();
				}	
				str_dt_ValidExpiredDate = Formater.formatDate(dt_ValidExpiredDate, language, "dd MMMM yyyy");
			}
			catch(Exception e)
			{ 
				str_dt_ValidExpiredDate = ""; 
			}
	
			rowx.add(str_dt_ValidExpiredDate);
	
			lstData.add(rowx);
			lstLinkData.add(String.valueOf(memberRegistrationHistory.getOID()));
		}
		return ctrlist.draw(index);
	}
	else
	{
		return "<div class=\"comment\">Data Tidak Ditemukan.......</div>";
	}
}
%>

<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidMemberRegistrationHistory  = FRMQueryString.requestLong(request, "hidden_member_registration_history_id");
int source = FRMQueryString.requestInt(request, "source");
long oidMemberReg = FRMQueryString.requestLong(request, "hidden_contact_id");    
if(source==1 || source==2 || source==3){
    oidMemberReg = FRMQueryString.requestLong(request, "hidden_contacts_id");    
}
/*variable declaration*/
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = ""+PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_MEMBER_ID]+" = "+oidMemberReg;;
String orderClause = ""+PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_REGISTRATION_DATE]+" DESC, "+
					PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_VALID_START_DATE]+" DESC, "+
					PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_VALID_EXPIRED_DATE]+" DESC ";

CtrlMemberRegistrationHistory ctrlMemberRegistrationHistory = new CtrlMemberRegistrationHistory(request);
ControlLine ctrLine = new ControlLine();
Vector listMemberRegistrationHistory = new Vector(1,1);

/*switch statement */
//iErrCode = ctrlMemberRegistrationHistory.action(iCommand , oidMemberRegistrationHistory);
iErrCode = ctrlMemberRegistrationHistory.action(iCommand,oidMemberRegistrationHistory,oidMemberReg,userId,userName);
/* end switch*/
FrmMemberRegistrationHistory frmMemberRegistrationHistory = ctrlMemberRegistrationHistory.getForm();

/*count list All MemberRegistrationHistory*/
int vectSize = PstMemberRegistrationHistory.getCount(whereClause);

MemberRegistrationHistory memberRegistrationHistory = ctrlMemberRegistrationHistory.getMemberRegistrationHistory();
msgString =  ctrlMemberRegistrationHistory.getMessage();

/*switch list MemberRegistrationHistory*/
if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE))
	start = PstMemberRegistrationHistory.findLimitStart(memberRegistrationHistory.getOID(),recordToGet, whereClause);

if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlMemberRegistrationHistory.actionList(iCommand, start, vectSize, recordToGet);
 } 
/* end switch list*/

/* get record to display */
listMemberRegistrationHistory = PstMemberRegistrationHistory.list(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listMemberRegistrationHistory.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else{
		 start = 0 ;
		 //iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listMemberRegistrationHistory = PstMemberRegistrationHistory.list(start,recordToGet, whereClause , orderClause);
}

/* get data Member reg */
MemberReg memberReg = new MemberReg();
MemberGroup mGroup = new MemberGroup();
Religion religion = new Religion();
if(oidMemberReg!=0){
	try{
		memberReg = PstMemberReg.fetchExc(oidMemberReg);
		mGroup = PstMemberGroup.fetchExc(memberReg.getMemberGroupId());
		religion = PstReligion.fetchExc(memberReg.getMemberReligionId());
	}catch(Exception e){
	}
}
if(((iCommand==Command.SAVE)&&(frmMemberRegistrationHistory.errorSize()==0))||(iCommand==Command.DELETE)){
    if(source==0){
        response.sendRedirect(""+approot+"/masterdata/memberreg_edit.jsp?command="+Command.EDIT+"&&hidden_contact_id="+oidMemberReg+"&&hidden_member_registration_history_id="+oidMemberRegistrationHistory);
    }else if(source==1){
        response.sendRedirect(""+approot+"/masterdata/travel.jsp?command="+Command.EDIT+"&&hidden_contacts_id="+oidMemberReg+"&&hidden_member_registration_history_id="+oidMemberRegistrationHistory);
    
    }else if(source==2){
        response.sendRedirect(""+approot+"/masterdata/memberRegCompany_edit.jsp?command="+Command.EDIT+"&&hidden_contacts_id="+oidMemberReg+"&&hidden_member_registration_history_id="+oidMemberRegistrationHistory);
    
    }else if(source==2){
           response.sendRedirect(""+approot+"/masterdata/guide_edit.jsp?command="+Command.EDIT+"&&hidden_contacts_id="+oidMemberReg+"&&hidden_member_registration_history_id="+oidMemberRegistrationHistory);
    
    }
}

String backSource="";
if(source==0){
    //response.sendRedirect(""+approot+"/masterdata/memberreg_edit.jsp?command="+Command.EDIT+"&&hidden_contact_id="+oidMemberReg+"&&hidden_member_registration_history_id="+oidMemberRegistrationHistory);
    backSource="memberreg_edit.jsp";
}else if(source==1){
    //response.sendRedirect(""+approot+"/masterdata/travel.jsp?command="+Command.EDIT+"&&hidden_contacts_id="+oidMemberReg+"&&hidden_member_registration_history_id="+oidMemberRegistrationHistory);
    backSource="travel.jsp";
}else if(source==2){
    backSource="memberRegCompany_edit.jsp";
}else if(source==3){
    backSource="guide_edit.jsp";
}
/* list history */
String whHistory = PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_MEMBER_ID]+" = "+oidMemberReg;
String ordHistory = PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_REGISTRATION_DATE]+" DESC, "+
					PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_VALID_START_DATE]+" DESC, "+
					PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_VALID_EXPIRED_DATE]+" DESC ";
Vector listHistory = PstMemberRegistrationHistory.list(0,0,whHistory,ordHistory);
//System.out.println("listHistory : " + listHistory.size());	
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdEditHistory(oidHistory,oidMemberReg)
{
	document.frmmemberregistrationhistory.hidden_member_registration_history_id.value=oidHistory;
	document.frmmemberregistrationhistory.command.value="<%=Command.EDIT%>";	
	document.frmmemberregistrationhistory.prev_command.value="<%=prevCommand%>";
	document.frmmemberregistrationhistory.action="memberregistrationhistory.jsp";	
	document.frmmemberregistrationhistory.submit();
}

function cmdAdd(){
	document.frmmemberregistrationhistory.hidden_member_registration_history_id.value="0";
	document.frmmemberregistrationhistory.command.value="<%=Command.ADD%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=prevCommand%>";
	document.frmmemberregistrationhistory.action="memberregistrationhistory.jsp";
	document.frmmemberregistrationhistory.submit();
}

function cmdAsk(oidMemberRegistrationHistory){
	document.frmmemberregistrationhistory.hidden_member_registration_history_id.value=oidMemberRegistrationHistory;
	document.frmmemberregistrationhistory.command.value="<%=Command.ASK%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=prevCommand%>";
	document.frmmemberregistrationhistory.action="memberregistrationhistory.jsp";
	document.frmmemberregistrationhistory.submit();
}

function cmdConfirmDelete(oidMemberRegistrationHistory){
	document.frmmemberregistrationhistory.hidden_member_registration_history_id.value=oidMemberRegistrationHistory;
	document.frmmemberregistrationhistory.command.value="<%=Command.DELETE%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=prevCommand%>";
	document.frmmemberregistrationhistory.action="memberregistrationhistory.jsp";
	document.frmmemberregistrationhistory.submit();
}
function cmdSave(){
	document.frmmemberregistrationhistory.command.value="<%=Command.SAVE%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=prevCommand%>";
	document.frmmemberregistrationhistory.action="memberregistrationhistory.jsp";
	document.frmmemberregistrationhistory.submit();
	}

function cmdEdit(oidMemberRegistrationHistory){
	document.frmmemberregistrationhistory.hidden_member_registration_history_id.value=oidMemberRegistrationHistory;
	document.frmmemberregistrationhistory.command.value="<%=Command.EDIT%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=prevCommand%>";
	document.frmmemberregistrationhistory.action="memberregistrationhistory.jsp";
	document.frmmemberregistrationhistory.submit();
	}

function cmdCancel(oidMemberRegistrationHistory){
	document.frmmemberregistrationhistory.hidden_member_registration_history_id.value=oidMemberRegistrationHistory;
	document.frmmemberregistrationhistory.command.value="<%=Command.EDIT%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=prevCommand%>";
	document.frmmemberregistrationhistory.action="memberregistrationhistory.jsp";
	document.frmmemberregistrationhistory.submit();
}

function cmdBack(){
	document.frmmemberregistrationhistory.command.value="<%=Command.BACK%>";
	document.frmmemberregistrationhistory.action="memberregistrationhistory.jsp";
	document.frmmemberregistrationhistory.submit();
	}

function cmdBack2Member(oidMemberRegistrationHistory,oidContact,sourceBack){
	document.frmmemberregistrationhistory.hidden_member_registration_history_id.value=oidMemberRegistrationHistory;
	document.frmmemberregistrationhistory.hidden_contact_id.value=oidContact;
        document.frmmemberregistrationhistory.hidden_contacts_id.value=oidContact;
	document.frmmemberregistrationhistory.command.value="<%=Command.EDIT%>";
	document.frmmemberregistrationhistory.action=sourceBack;
	document.frmmemberregistrationhistory.submit();
	}

function cmdListFirst(){
	document.frmmemberregistrationhistory.command.value="<%=Command.FIRST%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=Command.FIRST%>";
	document.frmmemberregistrationhistory.action="memberregistrationhistory.jsp";
	document.frmmemberregistrationhistory.submit();
}

function cmdListPrev(){
	document.frmmemberregistrationhistory.command.value="<%=Command.PREV%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=Command.PREV%>";
	document.frmmemberregistrationhistory.action="memberregistrationhistory.jsp";
	document.frmmemberregistrationhistory.submit();
	}

function cmdListNext(){
	document.frmmemberregistrationhistory.command.value="<%=Command.NEXT%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=Command.NEXT%>";
	document.frmmemberregistrationhistory.action="memberregistrationhistory.jsp";
	document.frmmemberregistrationhistory.submit();
}

function cmdListLast(){
	document.frmmemberregistrationhistory.command.value="<%=Command.LAST%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=Command.LAST%>";
	document.frmmemberregistrationhistory.action="memberregistrationhistory.jsp";
	document.frmmemberregistrationhistory.submit();
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
<link rel="stylesheet" href="../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../styles/tab.css" type="text/css">
<%if(menuUsed == MENU_ICON){%>
    <link href="../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" --> 
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
            Membership &gt; <%=textListTitle[SESS_LANGUAGE][0]%> <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmmemberregistrationhistory" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_member_registration_history_id" value="<%=oidMemberRegistrationHistory%>">
              <input type="hidden" name="hidden_contact_id" value="<%=oidMemberReg%>">
              <input type="hidden" name="hidden_contacts_id" value="<%=oidMemberReg%>">
              <input type="hidden" name="source" value="<%=source%>">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
			    <tr align="left"> 
				  <td height="21" valign="middle" colspan="3"><hr></td>
			    </tr>			  
                <tr align="left" valign="top"> 
                  <td height="8" valign="middle" colspan="3"> 
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr align="left" valign="top"> 
                        <td height="21" colspan="4"> 
                          <table width="100%" border="0" cellspacing="1" cellpadding="1">
                            <tr align="left" valign="top"> 
                              <td height="21" valign="top" width="10%"><%=textListHeader[SESS_LANGUAGE][0]%></td>
                              <td height="21" valign="top" width="1%">:</td>
                              <td height="21" width="89%"> <b><i><%=mGroup.getCode()%></i></b> 
                            
                            <tr align="left" valign="top"> 
                              <td height="21" valign="top" width="10%"><%=textListHeader[SESS_LANGUAGE][2]%></td>
                              <td height="21" valign="top" width="1%">:</td>
                              <td height="21" width="89%"> <b><i><%= memberReg.getContactCode() %></i></b> 
                            
                            <tr align="left" valign="top"> 
                              <td height="21" valign="top" width="10%"><%=textListHeader[SESS_LANGUAGE][3]%></td>
                              <td height="21" valign="top" width="1%">:</td>
                              <td height="21" width="89%"> <b><i><%= memberReg.getPersonName() %></i></b> 
                            
                          </table>
                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="21" valign="top" colspan="4">&nbsp;</td>
                      </tr>	
                      <tr align="left" valign="top"> 
                        <td height="21" valign="top" colspan="4"><b><u>Data Status Pendaftaran</u></b></td>
                      </tr>                      					  					  					
                      <tr align="left" valign="top"> 
                        <td height="21" valign="middle" colspan="4"><%=drawList(listHistory, oidMemberRegistrationHistory, SESS_LANGUAGE)%> </td>
                      </tr>
                      <tr align="left" valign="top"> 
                        <td height="21" valign="middle" colspan="4"> <span class="command"> 
                          <% 
						   int cmd = 0;
						   if ((iCommand == Command.FIRST || iCommand == Command.PREV) || (iCommand == Command.NEXT || iCommand == Command.LAST))
						   {
							  cmd =iCommand; 
						   }		
						   else
						   {
							  if(iCommand == Command.NONE || prevCommand == Command.NONE)
							  {
								cmd = Command.FIRST;
							  }	
							  else 
							  {
								cmd = prevCommand; 
							  }	
						   } 
						   
						   ctrLine.setLocationImg(approot+"/images");
						   ctrLine.initDefault();
						   out.println(ctrLine.drawImageListLimit(cmd, vectSize, start, recordToGet));						   
						  %>
                          </span> </td>
                      </tr>					  
					  
					  <%
					  if(iCommand!=Command.ADD && iCommand!=Command.EDIT && iCommand!=Command.ASK && iErrCode==FRMMessage.NONE)
					  {
					  %>
                      <tr align="left" valign="top"> 					  						
                          <table width="17%" border="0" cellspacing="2" cellpadding="3">
                            <tr> 
                              <td width="18%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true)%>"></a></td>
                              <td nowrap width="82%"><a href="javascript:cmdAdd()" class="command"><%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true)%></a></td>
                            </tr>
                          </table>
                      </tr>						  
					  <%
					  }
					  %>					  
					  
					  <%
					  if((iCommand == Command.ADD) || (iCommand == Command.EDIT) || (iCommand == Command.ASK) || ((iCommand==Command.SAVE || iCommand==Command.DELETE) && (iErrCode>0))	)
					  {					  
					  %>					  
                      <tr align="left" valign="top"> 
                        <td height="21" valign="top" width="10%"><%=textListHeader[SESS_LANGUAGE][12]%></td>
                        <td height="21" valign="top" width="1%">:</td>
                        <td height="21" colspan="2" width="89%"> <%=	ControlDate.drawDateWithStyle(frmMemberRegistrationHistory.fieldNames[FrmMemberRegistrationHistory.FRM_FIELD_REGISTRATION_DATE], memberRegistrationHistory.getRegistrationDate()==null?new Date():memberRegistrationHistory.getRegistrationDate(), 3,-5, "formElemen", "") %> 
                      </tr>						
                      <tr align="left" valign="top"> 
                        <td height="21" valign="top" width="10%"><%=textListHeader[SESS_LANGUAGE][13]%></td>
                        <td height="21" valign="top" width="1%">:</td>
                        <td height="21" colspan="2" width="89%"> <%=	ControlDate.drawDateWithStyle(frmMemberRegistrationHistory.fieldNames[FrmMemberRegistrationHistory.FRM_FIELD_VALID_START_DATE], memberRegistrationHistory.getValidStartDate()==null?new Date():memberRegistrationHistory.getValidStartDate(), 3,-5, "formElemen", "") %> 
                      </tr>						
                      <tr align="left" valign="top"> 
                        <td height="21" valign="top" width="10%"><%=textListHeader[SESS_LANGUAGE][14]%></td>
                        <td height="21" valign="top" width="1%">:</td>
                        <td height="21" colspan="2" width="89%"> <%=	ControlDate.drawDateWithStyle(frmMemberRegistrationHistory.fieldNames[FrmMemberRegistrationHistory.FRM_FIELD_VALID_EXPIRED_DATE], memberRegistrationHistory.getValidExpiredDate()==null?new Date():memberRegistrationHistory.getValidExpiredDate(), 3,-5, "formElemen", "") %> 
                      </tr>						
                      <tr align="left" valign="top" > 
                        <td colspan="4" class="command">&nbsp;</td>
                      </tr>
					  <%
					  }
					  %>
					  
                      <tr align="left" valign="top" > 
                        <td colspan="4" class="command"> 
                          <%
							ctrLine.setLocationImg(approot+"/images");
							ctrLine.initDefault();
							ctrLine.setTableWidth("100%");
							ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_SAVE,true));
							ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true)+" List");
							ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ASK,true));
							ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_CANCEL,false));
							
							String scomDel = "javascript:cmdAsk('"+oidMemberRegistrationHistory+"')";
							String sconDelCom = "javascript:cmdConfirmDelete('"+oidMemberRegistrationHistory+"')";
							String scancel = "javascript:cmdEdit('"+oidMemberRegistrationHistory+"')";
							String scomBack = "javascript:cmdBack()";
							ctrLine.setBackCommand(scomBack);
							ctrLine.setCommandStyle("command");
							ctrLine.setColCommStyle("command");
							
							// set command caption
							ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true));
							ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_SAVE,true));
							ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true)+" List");
							ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ASK,true));
							ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_DELETE,true));
							ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_CANCEL,false));	

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
							%>
                          <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%> </td>
                      </tr>
                      <tr align="left" valign="top"> 					  						
                          <table width="17%" border="0" cellspacing="2" cellpadding="3">
                            <tr> 
                              <td width="18%"><a href="javascript:cmdBack2Member('<%=oidMemberRegistrationHistory%>','<%=oidMemberReg%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1001','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image1001" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="Kembali ke Data Member"></a></td>
                              <td nowrap width="82%"><a href="javascript:cmdBack2Member('<%=oidMemberRegistrationHistory%>','<%=oidMemberReg%>','<%=backSource%>')" class="command">Kembali ke Data Member</a></td>
                            </tr>
                          </table>
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
            <%@include file="../styletemplate/footer.jsp" %>

        <%}else{%>
            <%@ include file = "../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
