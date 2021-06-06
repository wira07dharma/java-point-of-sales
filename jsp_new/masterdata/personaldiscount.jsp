<% 
/* 
 * Page Name  		:  personaldiscount.jsp
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
<%@ page import="com.dimata.common.entity.custom.DataCustom"%>
<%@ page import="com.dimata.posbo.session.sales.SessSaleCommision"%>
<%//@ page import = "com.dimata.posbo.entity.admin.*" %>

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_MEMBER); %>
<%@ include file = "../main/checkuser.jsp" %>
<%!
public static final String textListTitle[][] =
{
	{"Diskon Khusus","Harus diisi"},
	{"Personal Discount","required"}
};

public static final String textListHeader[][] =
{
	{"Tipe Member","Status","Barcode","Nama","Alamat","No Telp","No HP","Tempat/Tgl Lahir","Jenis Kelamin","Agama","No ID","Nama Studio","Tanggal Daftar","Tanggal Mulai Berlaku",
	"Tanggal Akhir Berlaku","Nama Produk","Persentase","Nominal"},
	{"Member Type","Status","Barcode","Name","Address","Telp No", "HP No","Place/Birthdate","Sex","Religion","ID Number","Studio Name","Registration Date","Valid Start Date",
	"Valid Expired Date","Product Name","Percentage","Amount"}
};

public String drawList(Vector objectClass ,  long personalDiscountId, int languange)
{
	if(objectClass!=null&&objectClass.size()>0){
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("70%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader(textListHeader[languange][15],"50%");
		ctrlist.addHeader(textListHeader[languange][16],"25%");
		ctrlist.addHeader(textListHeader[languange][17],"25%");

		ctrlist.setLinkRow(0);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int index = -1;

		for (int i = 0; i < objectClass.size(); i++)
		{
			PersonalDiscount personalDiscount = (PersonalDiscount)objectClass.get(i);
			Vector rowx = new Vector();
			if(personalDiscountId == personalDiscount.getOID())
				 index = i;

			String materialNm = "";
			try
			{
				Material material = PstMaterial.fetchExc(personalDiscount.getMaterialId());
				materialNm = material.getName();
			}
			catch(Exception e)
			{
				System.out.println("Exc");
			}
			rowx.add(materialNm);
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(personalDiscount.getPersDiscPct())+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(personalDiscount.getPersDiscVal())+"</div>");

			lstData.add(rowx);
			lstLinkData.add(String.valueOf(personalDiscount.getOID()));
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
long oidPersonalDiscount  = FRMQueryString.requestLong(request, "hidden_personal_discount_id");
long oidMemberReg = FRMQueryString.requestLong(request, "hidden_contact_id");
long materialId = FRMQueryString.requestLong(request, "hidden_material_id");

int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_CONTACT_ID]+" = "+oidMemberReg ;
String orderClause = "";

CtrlPersonalDiscount ctrlPersonalDiscount = new CtrlPersonalDiscount(request);
ControlLine ctrLine = new ControlLine();
Vector listPersonalDiscount = new Vector(1,1);

iErrCode = ctrlPersonalDiscount.action(iCommand , oidPersonalDiscount,oidMemberReg);
FrmPersonalDiscount frmPersonalDiscount = ctrlPersonalDiscount.getForm();
FRMHandler frmHandler = new FRMHandler();

int vectSize = PstPersonalDiscount.getCount(whereClause);
PersonalDiscount personalDiscount = ctrlPersonalDiscount.getPersonalDiscount();
msgString =  ctrlPersonalDiscount.getMessage();

if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE))
{
	start = PstPersonalDiscount.findLimitStart(personalDiscount.getOID(),recordToGet, whereClause);
}

if((iCommand == Command.FIRST || iCommand == Command.PREV )||(iCommand == Command.NEXT || iCommand == Command.LAST))
{
	start = ctrlPersonalDiscount.actionList(iCommand, start, vectSize, recordToGet);
}

listPersonalDiscount = PstPersonalDiscount.list(start,recordToGet, whereClause , orderClause);
if (listPersonalDiscount.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
	 {
	  	 start = start - recordToGet;
	 }
	 else
	 {
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST;
	 }
	 listPersonalDiscount = PstPersonalDiscount.list(start,recordToGet, whereClause , orderClause);
}

MemberReg memberReg = new MemberReg();
MemberGroup mGroup = new MemberGroup();
Religion religion = new Religion();
if(oidMemberReg!=0)
{
	try
	{
		memberReg = PstMemberReg.fetchExc(oidMemberReg);
		mGroup = PstMemberGroup.fetchExc(memberReg.getMemberGroupId());
		religion = PstReligion.fetchExc(memberReg.getMemberReligionId());
	}
	catch(Exception e)
	{
		System.out.println("Exc");
	}
}

//if((iCommand==Command.SAVE)&&(frmPersonalDiscount.errorSize()==0)||(iCommand==Command.DELETE))
if( (iCommand==Command.SAVE) && (frmPersonalDiscount.errorSize()==0) )
{
    SessSaleCommision.insertDataCommision(request,oidMemberReg);
    iCommand = Command.ADD;
	personalDiscount = new PersonalDiscount();
	oidPersonalDiscount = 0;
	materialId = 0;
}

String matName = "";
Material material = new Material();
long oid = materialId==0?personalDiscount.getMaterialId() : materialId;

if(oid!=0)
{
	try
	{
		material = PstMaterial.fetchExc(oid);
		matName = material.getName();
	}
	catch(Exception e)
	{
		System.out.println("Exc");
	}
}

/*
if(((iCommand==Command.SAVE)&&(frmPersonalDiscount.errorSize()==0))||(iCommand==Command.DELETE)){
	response.sendRedirect(""+approot+"/masterdata/memberreg_edit.jsp?command="+Command.EDIT+"&&hidden_contact_id="+oidMemberReg+"&&hidden_personal_discount_id="+oidPersonalDiscount);
}*/
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdCariMaterial()
{
	var matname = document.frmmemberregistrationhistory.txt_nama.value;
	window.open("srcmaterial.jsp?command=<%=Command.LIST%>&&FRM_FIELD_MATNAME="+matname+"&&hidden_contact_id=<%=oidMemberReg%>","cari_material", "height=600,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function cmdAdd(){
	document.frmmemberregistrationhistory.hidden_personal_discount_id.value="0";
	document.frmmemberregistrationhistory.command.value="<%=Command.ADD%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=prevCommand%>";
	document.frmmemberregistrationhistory.action="personaldiscount.jsp";
	document.frmmemberregistrationhistory.submit();
}

function cmdAsk(oidPersonalDiscount){
	document.frmmemberregistrationhistory.hidden_personal_discount_id.value=oidPersonalDiscount;
	document.frmmemberregistrationhistory.command.value="<%=Command.ASK%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=prevCommand%>";
	document.frmmemberregistrationhistory.action="personaldiscount.jsp";
	document.frmmemberregistrationhistory.submit();
}

function cmdConfirmDelete(oidPersonalDiscount){
	document.frmmemberregistrationhistory.hidden_personal_discount_id.value=oidPersonalDiscount;
	document.frmmemberregistrationhistory.command.value="<%=Command.DELETE%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=prevCommand%>";
	document.frmmemberregistrationhistory.action="personaldiscount.jsp";
	document.frmmemberregistrationhistory.submit();
}
function cmdSave(){
	document.frmmemberregistrationhistory.command.value="<%=Command.SAVE%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=prevCommand%>";
	document.frmmemberregistrationhistory.action="personaldiscount.jsp";
	document.frmmemberregistrationhistory.submit();
}

function cmdEdit(oidPersonalDiscount){
	document.frmmemberregistrationhistory.hidden_personal_discount_id.value=oidPersonalDiscount;
	document.frmmemberregistrationhistory.command.value="<%=Command.EDIT%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=prevCommand%>";
	document.frmmemberregistrationhistory.action="personaldiscount.jsp";
	document.frmmemberregistrationhistory.submit();
}

function cmdCancel(oidPersonalDiscount){
	document.frmmemberregistrationhistory.hidden_personal_discount_id.value=oidPersonalDiscount;
	document.frmmemberregistrationhistory.command.value="<%=Command.EDIT%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=prevCommand%>";
	document.frmmemberregistrationhistory.action="personaldiscount.jsp";
	document.frmmemberregistrationhistory.submit();
}

function cmdBack(){
	document.frmmemberregistrationhistory.command.value="<%=Command.BACK%>";
	document.frmmemberregistrationhistory.action="personaldiscount.jsp";
	document.frmmemberregistrationhistory.submit();
}

function cmdBack2Member(oidPersonalDiscount,oidContact){
	document.frmmemberregistrationhistory.hidden_personal_discount_id.value=oidPersonalDiscount;
	document.frmmemberregistrationhistory.hidden_contact_id.value=oidContact;
	document.frmmemberregistrationhistory.command.value="<%=Command.EDIT%>";
	document.frmmemberregistrationhistory.action="memberreg_edit.jsp";
	document.frmmemberregistrationhistory.submit();
}

function cmdListFirst(){
	document.frmmemberregistrationhistory.command.value="<%=Command.FIRST%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=Command.FIRST%>";
	document.frmmemberregistrationhistory.action="personaldiscount.jsp";
	document.frmmemberregistrationhistory.submit();
}

function cmdListPrev(){
	document.frmmemberregistrationhistory.command.value="<%=Command.PREV%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=Command.PREV%>";
	document.frmmemberregistrationhistory.action="personaldiscount.jsp";
	document.frmmemberregistrationhistory.submit();
}

function cmdListNext(){
	document.frmmemberregistrationhistory.command.value="<%=Command.NEXT%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=Command.NEXT%>";
	document.frmmemberregistrationhistory.action="personaldiscount.jsp";
	document.frmmemberregistrationhistory.submit();
}

function cmdListLast(){
	document.frmmemberregistrationhistory.command.value="<%=Command.LAST%>";
	document.frmmemberregistrationhistory.prev_command.value="<%=Command.LAST%>";
	document.frmmemberregistrationhistory.action="personaldiscount.jsp";
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
              <input type="hidden" name="hidden_personal_discount_id" value="<%=oidPersonalDiscount%>">
              <input type="hidden" name="hidden_contact_id" value="<%=oidMemberReg%>">
              <input type="hidden" name="hidden_material_id" value="<%=materialId%>">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top">
                  <td valign="top" colspan="3">
                    <hr>
                  </td>
                </tr>
                <tr align="left" valign="top">
                  <td valign="middle" colspan="3">
                    <table width="100%" border="0" cellspacing="1" cellpadding="0">
                      <tr align="left" valign="top"> 
                        <td valign="middle" colspan="4"> <table width="100%" border="0" cellspacing="1" cellpadding="1">
                            <tr align="left" valign="top"> 
                              <td height="21" valign="top" width="8%"><%=textListHeader[SESS_LANGUAGE][0]%></td>
                              <td height="21" valign="top" width="1%">:</td>
                              <td height="21" colspan="3" width="91%"><b><i><%=mGroup.getCode()%></i></b> 
                            <tr align="left" valign="top"> 
                              <td height="21" valign="top" width="8%"><%=textListHeader[SESS_LANGUAGE][2]%></td>
                              <td height="21" valign="top" width="1%">:</td>
                              <td height="21" colspan="3" width="91%"> <b><i><%= memberReg.getContactCode() %></i></b> 
                            <tr align="left" valign="top"> 
                              <td height="21" valign="top" width="8%"><%=textListHeader[SESS_LANGUAGE][3]%></td>
                              <td height="21" valign="top" width="1%">:</td>
                              <td height="21" colspan="3" width="91%"> <b><i><%= memberReg.getPersonName() %></i></b> </table></td>
                      </tr>
                      <tr align="left" valign="top"> 
                        <td height="21" valign="middle" colspan="4" class="mainheader">&nbsp;</td>
                      </tr>
                      <tr align="left" valign="top"> 
                        <td height="21" valign="middle" colspan="4"><b><u>Daftar 
                          Potongan Khusus</u></b></td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="21" valign="middle" colspan="4"><%=drawList(listPersonalDiscount,oidPersonalDiscount,SESS_LANGUAGE)%></td>
                      </tr>
                      <tr align="left" valign="top"> 
                        <td height="21" valign="middle" colspan="4">&nbsp;</td>
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
								cmd =prevCommand;
							  }
						   }

						   ctrLine.setLocationImg(approot+"/images");
						   ctrLine.initDefault();
						   out.println(ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet));
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
                        <td height="21" valign="top" width="10%"><%=textListHeader[SESS_LANGUAGE][15]%></td>
                        <td height="21" valign="top" width="1%">:</td>
                        <td height="21" colspan="2" width="89%"> <input type="text" readonly name="txt_nama" value="<%=matName%>" class="formElemen" size="40"> 
                          <a href="javascript:cmdCariMaterial()">CHK</a> <input type="hidden" name="<%=frmPersonalDiscount.fieldNames[FrmPersonalDiscount.FRM_MATERIAL_ID] %>"  value="<%=personalDiscount.getMaterialId() %>" > 
                      <tr align="left" valign="top"> 
                        <td height="21" valign="top" width="10%"><%=textListHeader[SESS_LANGUAGE][16]%></td>
                        <td height="21" valign="top" width="1%">:</td>
                        <td height="21" colspan="2" width="89%"> <input type="text" name="<%=frmPersonalDiscount.fieldNames[FrmPersonalDiscount.FRM_PERSONAL_DISC_PCT] %>" value="<%=frmHandler.userFormatStringDecimal(personalDiscount.getPersDiscPct())%>" size="10" class="formElemen" style="text-align:right" maxlength="3"> 
                      <tr align="left" valign="top"> 
                        <td height="21" valign="top" width="10%"><%=textListHeader[SESS_LANGUAGE][17]%></td>
                        <td height="21" valign="top" width="1%">:</td>
                        <td height="21" colspan="2" width="89%"> <input type="text" name="<%=frmPersonalDiscount.fieldNames[FrmPersonalDiscount.FRM_PERSONAL_DISC_VAL] %>" value="<%=frmHandler.userFormatStringDecimal(personalDiscount.getPersDiscVal())%>" class="formElemen" style="text-align:right" size="20"> 
                          <!--tr align="left" valign="top" >
                        <td colspan="4" class="command"><table width="100%" border="0" cellspacing="1" cellpadding="1">
                            <tr>
                              <td colspan="3">&nbsp;</td>
                            </tr>
                            <tr>
                              <td colspan="3"><em>This data for include in invoice
                                internal created </em></td>
                            </tr>
                            <%
                                try{
                                Vector listCost = SessSaleCommision.getDataCommision(oidMemberReg);
                                if(iCommand == Command.ADD){
                                    listCost = new Vector();                                        
                                }
                                DataCustom dataCustomOther = new DataCustom();
                                dataCustomOther.setDataValue("0");
                                DataCustom dataCustomCargo = new DataCustom();
                                dataCustomCargo.setDataValue("0");
                                DataCustom dataCustomSupp = new DataCustom();
                                dataCustomSupp.setDataValue("0");
                                DataCustom dataCustomPpn = new DataCustom();
                                dataCustomPpn.setDataValue("0");
                                if(listCost!=null && listCost.size()>0){
                                    dataCustomOther = (DataCustom)listCost.get(0);
                                    dataCustomCargo = (DataCustom)listCost.get(1);
                                    dataCustomSupp = (DataCustom)listCost.get(2);
                                    dataCustomPpn = (DataCustom)listCost.get(3);
                                }
                            %>
                            <tr>
                              <td width="19%">Cost Other(STO-Merc.) %</td>
                              <td width="1%">&nbsp;</td>
                              <td width="80%"><input type="text" name="<%=SessSaleCommision.strNameCommision[SessSaleCommision.COST_OTHER] %>" value="<%=dataCustomOther.getDataValue()%>" size="10" class="formElemen" style="text-align:right" maxlength="4"></td>
                            </tr>
                            <tr>
                              <td>Cost Cargo %</td>
                              <td>&nbsp;</td>
                              <td><input type="text" name="<%=SessSaleCommision.strNameCommision[SessSaleCommision.COST_CARGO] %>" value="<%=dataCustomCargo.getDataValue()%>" size="10" class="formElemen" style="text-align:right" maxlength="4"></td>
                            </tr>
                            <tr>
                              <td>Supplier %</td>
                              <td>&nbsp;</td>
                              <td><input type="text" name="<%=SessSaleCommision.strNameCommision[SessSaleCommision.COST_SUPPLIER] %>" value="<%=dataCustomSupp.getDataValue()%>" size="10" class="formElemen" style="text-align:right" maxlength="4"></td>
                            </tr>
                            <tr>
                              <td>PPN %</td>
                              <td>&nbsp;</td>
                              <td><input type="text" name="<%=SessSaleCommision.strNameCommision[SessSaleCommision.COST_PPN]%>" value="<%=dataCustomPpn.getDataValue()%>" size="10" class="formElemen" style="text-align:right" maxlength="4"></td>
                            </tr>
                            <tr>
                              <td>&nbsp;</td>
                              <td>&nbsp;</td>
                              <td>&nbsp;</td>
                            </tr>
                            <%}catch(Exception e){
                                System.out.println("sdfsdfsdfds"+e.toString() );
                            }%>
                          </table></td>
                      </tr-->
                      <tr align="left" valign="top" > 
                        <td colspan="4" class="command">&nbsp;</td>
                      </tr>
                      <%
					  }
					  %>
                      <tr align="left" valign="top" > 
                        <td colspan="4" class="command"> <%
							ctrLine.setLocationImg(approot+"/images");
							ctrLine.initDefault(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0]);
							ctrLine.setTableWidth("100%");
							ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_SAVE,true));
							ctrLine.setBackImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true));
							ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ASK,true));
							ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_CANCEL,false));

							String scomDel = "javascript:cmdAsk('"+oidPersonalDiscount+"')";
							String sconDelCom = "javascript:cmdConfirmDelete('"+oidPersonalDiscount+"')";
							String scancel = "javascript:cmdEdit('"+oidPersonalDiscount+"')";
							String scomBack = "javascript:cmdBack()";
							ctrLine.setBackCommand(scomBack);
							ctrLine.setCommandStyle("command");
							ctrLine.setColCommStyle("command");

							// set command caption
							ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true));
							ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_SAVE,true));
							ctrLine.setBackCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true));
							ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ASK,true));
							ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_DELETE,true));
							ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_CANCEL,false));

							if (privDelete)
							{
								ctrLine.setConfirmDelCommand(sconDelCom);
								ctrLine.setDeleteCommand(scomDel);
								ctrLine.setEditCommand(scancel);
							}
							else
							{
								ctrLine.setConfirmDelCaption("");
								ctrLine.setDeleteCaption("");
								ctrLine.setEditCaption("");
							}

							if(privAdd == false  && privUpdate == false)
							{
								ctrLine.setSaveCaption("");
							}

							if (privAdd == false)
							{
								ctrLine.setAddCaption("");
							}
						  %> <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%> </td>
                      </tr>
                      <tr align="left" valign="top"> 
                        <table width="17%" border="0" cellspacing="2" cellpadding="3">
                          <tr> 
                            <td width="18%"><a href="javascript:cmdBack2Member('<%=oidPersonalDiscount%>','<%=oidMemberReg%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1001','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image1001" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="Kembali ke Data Member"></a></td>
                            <td nowrap width="82%"><a href="javascript:cmdBack2Member('<%=oidPersonalDiscount%>','<%=oidMemberReg%>')" class="command">Kembali 
                              ke Data Member</a></td>
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
