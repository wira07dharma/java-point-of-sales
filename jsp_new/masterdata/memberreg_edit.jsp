
<%@page import="com.dimata.qdep.form.FRMHandler"%>
<%@page import="com.dimata.common.entity.payment.DailyRate"%>
<%@page import="com.dimata.common.form.payment.CtrlDailyRate"%>
<%@page import="com.dimata.common.form.payment.FrmDailyRate"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import ="com.dimata.common.entity.location.*"%>
<%@page import ="com.dimata.common.form.location.*"%>

<%@ page import="com.dimata.qdep.form.FRMQueryString,
                 com.dimata.posbo.form.masterdata.FrmMemberReg,
                 com.dimata.posbo.form.masterdata.CtrlMemberReg,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.util.Command,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlDate,
                 com.dimata.harisma.entity.masterdata.Religion,
                 com.dimata.harisma.entity.masterdata.PstReligion"%>
<%@ page language = "java" %>
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_MEMBER); %>
<%@ include file = "../main/checkuser.jsp" %>



<%!
public static final String textListTitle[][] =
{
	{"Anggota","Harus diisi","0=cash ; >0 Kredit"},
	{"Member","required","0=cash ; >0 Kredit"}
};

public static final String textListHeader[][] =
{
	{"Tipe Member","Status","Kode","Nama Depan","Alamat","No Telp","No HP","Tmp/Tgl Lahir",//7
         "Jns Kelamin","Agama","No ID","Credit Limit","Nama Perusahaan","Tgl Mendaftar","Tgl Mulai Berlaku",//14
	"Tgl Akhir Berlaku","Nama Produk","Persen","Jumlah","Data Utama","Potongan Khusus","Status Pendaftaran","Status Akhir","Consigment Limit",//23
        "Payment History Note","Member Currency ID","Fax","Kota","Provinsi","Negara","Kode Pos","View History","Waktu Jatuh Tempo","hari",//33
        "Kontak Person","Nama Belakang","Area","Kewarganegaraan","Pekerjaan","Email 1","Email 2","Data Perusahaan","Email"},//34
	
        {"Member Type","Status","Code","First Name","Address","Telp Number","HP Number","Place/Birthdate",//7
     "Sex","Religion","ID Number","Credit Limit","Company Name","Registration Date","Valid Start Date",//14
	"Valid Expired Date","Product Name","Percent","Amount","Main Data","Special Discount","Registration Status","Last Status","Consigment Limit",//9
        "Payment History Note","Member Currency ID","Fax","City","Province","Country","Postal Code","View History","Day Term Of Payment","days",
        "Contact Person","Last Name","Area","Nationality","Occuption","Email 1","Email 2","Company Profile","Email"}//34
        
        //{"Member Type","Status","Code","First Name","Address","Telp Number", "HP Number","Place/Birthdate",
     //"Sex","Religion","ID Number","Company Name","Registration Date","Valid Start Date",
        //"Valid Expired Date","Product Name","Percent","Amount","Main Data","Special Discount","Registration Status","Last Status",
        //"Member Currency ID","Fax","Town","Province","Country","Postal Code","View History","Day Term Of Payment","days"}
};
%>

<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidMemberReg = FRMQueryString.requestLong(request, "hidden_contact_id");
long oidMemberRegistrationHistory = FRMQueryString.requestLong(request, "hidden_member_registration_history_id");
long oidPersonalBonus = FRMQueryString.requestLong(request, "hidden_personal_discount_id");

String homeCountry = FRMQueryString.requestString(request, FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_COUNTRY]);
String country = FRMQueryString.requestString(request, FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_COUNTRY]);

String homeProvince = FRMQueryString.requestString(request, FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_PROVINCE]);
String province = FRMQueryString.requestString(request, FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_PROVINCE]);

String homeTown = FRMQueryString.requestString(request, FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_TOWN]);
String townx = FRMQueryString.requestString(request, FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_TOWN]);

String nationality = FRMQueryString.requestString(request, FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_NATIONALITY]);

int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = "";

MemberReg memberreg = new MemberReg();

ControlLine ctrLine = new ControlLine();
CtrlMemberReg ctrlMemberReg = new CtrlMemberReg(request);
iErrCode = ctrlMemberReg.action(iCommand, oidMemberReg,userId,userName );
FrmMemberReg frmMemberReg = ctrlMemberReg.getForm();
MemberReg memberReg = ctrlMemberReg.getMemberReg();
msgString =  ctrlMemberReg.getMessage();
int process = memberReg.getProcessStatus();

Vector listMemberReg = new Vector(1,1);
int vectSize = PstMemberReg.getCount(whereClause);

if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE)){
	start = 0;//PstMemberReg.findLimitStart(memberReg.getOID(),recordToGet, whereClause);
}

if( (iCommand==Command.FIRST) || (iCommand==Command.PREV) || (iCommand==Command.NEXT) || (iCommand==Command.LAST) ){
	start = ctrlMemberReg.actionList(iCommand, start, vectSize, recordToGet);
}

listMemberReg = PstMemberReg.list(start,recordToGet, whereClause , orderClause);
if(listMemberReg.size()<1 && start>0){
	 if (vectSize - recordToGet > recordToGet){
		 start = start - recordToGet;
	 }else{
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST;
	 }
	 listMemberReg = PstMemberReg.list(start, recordToGet, whereClause , orderClause);
}

// jika command SAVE, maka biarkan page ini muncul dengan state EDIT, supaya tidak bolak balik ke list
if( (iCommand==Command.SAVE) && (frmMemberReg.errorSize()==0)  ){
	iCommand = Command.EDIT;
	oidMemberReg = memberReg.getOID();
}

// jika command DELETE, maka langsung akses ke page list
if(iCommand==Command.DELETE){
    if(frmMemberReg.errorSize()==0){
	    response.sendRedirect(approot+"/masterdata/memberreg_list.jsp?command="+Command.FIRST);
    }else{
        //iCommand = Command.EDIT;
        memberReg = PstMemberReg.fetchExc(oidMemberReg);
    }
}



/*
if((iCommand==Command.SAVE)&&(frmMemberReg.errorSize()==0)||iCommand==Command.DELETE){
	iCommand = Command.ADD;
	oidMemberReg = 0;
	memberReg = new MemberReg();
	if(iCommand==Command.DELETE){
		response.sendRedirect(""+approot+"/masterdata/memberreg_list.jsp?command="+Command.FIRST+"");
	}
	if(process!=PstMemberReg.INSERT){
		response.sendRedirect(""+approot+"/masterdata/memberreg_list.jsp?command="+Command.FIRST+"&hidden_contact_id="+oidMemberReg);
	}
}
*/

String ordGroup = ""+PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE];
Vector listGroup = PstMemberGroup.list(0,0,"",ordGroup);

// mencari data member history yang terakhir utk ditampilkan
String whHistory = PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_MEMBER_ID]+" = "+oidMemberReg;
String ordHistory = PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_REGISTRATION_DATE]+" DESC, "+
					PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_VALID_START_DATE]+" DESC, "+
					PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_VALID_EXPIRED_DATE]+" DESC ";
Vector listHistory = PstMemberRegistrationHistory.list(0,1,whHistory,ordHistory);
%>

<%

	CtrlDailyRate ctrlDailyRate = new CtrlDailyRate(request);
	long oidDailyRate = FRMQueryString.requestLong(request, "hidden_daily_rate_id");


	String errMsg = "";


	//out.println("iCommand : "+iCommand);
	//ControlLine ctrLine = new ControlLine();

	iErrCode = ctrlDailyRate.action(iCommand , oidDailyRate);

	errMsg = ctrlDailyRate.getMessage();
	FrmDailyRate frmDailyRate = ctrlDailyRate.getForm();
	DailyRate dailyRate = ctrlDailyRate.getDailyRate();
	oidDailyRate = dailyRate.getOID();

	if(((iCommand==Command.SAVE)||(iCommand==Command.DELETE))&&(frmDailyRate.errorSize()<1))
	%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdEnter(index)
{
	if(event.keyCode == 13)
	{
		var oid = document.frmmemberreg.<%=FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_MEMBER_GROUP_ID]%>.value;
		var check = new Boolean();
		check = false;
		<%
	if(listGroup!=null&&listGroup.size()>0)
	{
			MemberGroup mGroup = new MemberGroup();
                        for(int i=0;i<listGroup.size();i++){
                            mGroup = (MemberGroup)listGroup.get(i);
                            if(mGroup.getGroupType()==PstMemberGroup.AGEN){
                                break;
                            }
                        }
	%>
		var strOID = "<%=mGroup.getOID()%>";
		if(oid==<%=""+mGroup.getOID()%>){
			check = true;
		}
		<%}
		%>

		if(check)
		{
			document.all.data_agen.style.display="";
			document.all.company.style.display="";
			document.all.address.style.display="";
			document.all.telp.style.display="";
		}
		else
		{
			document.all.data_agen.style.display="none";
			document.all.company.style.display="none";
			document.all.address.style.display="none";
			document.all.telp.style.display="none";
		}

		switch(index)
		{
			case '<%=FrmMemberReg.FRM_FIELD_MEMBER_GROUP_ID%>' :
				document.frmmemberreg.<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_PERSON_NAME]%>.focus();
				break;

			case '<%=FrmMemberReg.FRM_FIELD_MEMBER_STATUS%>' :
				document.frmmemberreg.<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_PERSON_NAME]%>.focus();
				break;

			case '<%=FrmMemberReg.FRM_FIELD_PERSON_NAME%>' :
				document.frmmemberreg.<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_ADDR]%>.focus();
				break;

			case '<%=FrmMemberReg.FRM_FIELD_HOME_ADDR%>' :
				document.frmmemberreg.<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_TELP]%>.focus();
				break;

			case '<%=FrmMemberReg.FRM_FIELD_HOME_TELP%>' :
				document.frmmemberreg.<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_FAX]%>.focus();
				break;

			case '<%=FrmMemberReg.FRM_FIELD_HOME_FAX%>' :
				document.frmmemberreg.<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_TOWN]%>.focus();
				break;

			case '<%=FrmMemberReg.FRM_FIELD_HOME_TOWN%>' :
				document.frmmemberreg.<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_MEMBER_BIRTH_DATE]%>.focus();
				break;

			case '<%=FrmMemberReg.FRM_FIELD_MEMBER_BIRTH_DATE%>' :
				document.frmmemberreg.<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_MEMBER_SEX]%>.focus();
				break;

			case '<%=FrmMemberReg.FRM_FIELD_MEMBER_SEX%>' :
				document.frmmemberreg.<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_MEMBER_RELIGION_ID]%>.focus();
				break;

			case '<%=FrmMemberReg.FRM_FIELD_MEMBER_RELIGION_ID%>' :
				document.frmmemberreg.<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_MEMBER_ID_CARD_NUMBER]%>.focus();
				break;

			case '<%=FrmMemberReg.FRM_FIELD_MEMBER_ID_CARD_NUMBER%>' :
				if(check)
				{
					document.frmmemberreg.<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_COMP_NAME]%>.focus();
				}
				else
				{
					cmdSave();
				}
				break;

			case '<%=FrmMemberReg.FRM_FIELD_COMP_NAME%>' :
				document.frmmemberreg.<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_BUSS_ADDRESS]%>.focus();
				break;

			case '<%=FrmMemberReg.FRM_FIELD_BUSS_ADDRESS%>' :
				document.frmmemberreg.<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_TELP_NR]%>.focus();
				break;

			case '<%=FrmMemberReg.FRM_FIELD_TELP_NR%>' :
				cmdSave();
				break;

			default :
				break;
		}
	}
}

function cmdAdd()
{
	document.frmmemberreg.hidden_contact_id.value="0";
	document.frmmemberreg.command.value="<%=Command.ADD%>";
	document.frmmemberreg.prev_command.value="<%=prevCommand%>";
	document.frmmemberreg.action="memberreg_edit.jsp";
	document.frmmemberreg.submit();
}

function cmdAddHistory(oid)
{
	document.frmmemberreg.hidden_contact_id.value=oid;
	document.frmmemberreg.command.value="<%=Command.LIST%>";
	document.frmmemberreg.prev_command.value="<%=prevCommand%>";
	document.frmmemberreg.action="memberregistrationhistory.jsp";
	document.frmmemberreg.submit();
}

function cmdViewHistory(oid) {
    var strvalue ="../main/historypo.jsp?command=<%=Command.FIRST%>"+
                     "&oidDocHistory="+oid;
    window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function cmdAddDiscount(oid)
{
	document.frmmemberreg.hidden_contact_id.value=oid;
	document.frmmemberreg.command.value="<%=Command.LIST%>";
	document.frmmemberreg.prev_command.value="<%=prevCommand%>";
	document.frmmemberreg.action="personaldiscount.jsp";
	document.frmmemberreg.submit();
}

function cmdAsk(oidMemberReg)
{
	document.frmmemberreg.hidden_contact_id.value=oidMemberReg;
	document.frmmemberreg.command.value="<%=Command.ASK%>";
	document.frmmemberreg.prev_command.value="<%=prevCommand%>";
	document.frmmemberreg.action="memberreg_edit.jsp";
	document.frmmemberreg.submit();
}

function cmdConfirmDelete(oidMemberReg)
{
	document.frmmemberreg.hidden_contact_id.value=oidMemberReg;
	document.frmmemberreg.command.value="<%=Command.DELETE%>";
	document.frmmemberreg.prev_command.value="<%=prevCommand%>";
	document.frmmemberreg.action="memberreg_edit.jsp";
	document.frmmemberreg.submit();
}

function cmdSave()
{
	document.frmmemberreg.command.value="<%=Command.SAVE%>";
	document.frmmemberreg.prev_command.value="<%=prevCommand%>";
	document.frmmemberreg.action="memberreg_edit.jsp";
	document.frmmemberreg.submit();
}

function cmdEdit(oidMemberReg)
{
	document.frmmemberreg.hidden_contact_id.value=oidMemberReg;
	document.frmmemberreg.command.value="<%=Command.EDIT%>";
	document.frmmemberreg.prev_command.value="<%=prevCommand%>";
	document.frmmemberreg.action="memberreg_edit.jsp";
	document.frmmemberreg.submit();
}

function cmdCancel(oidMemberReg)
{
	document.frmmemberreg.hidden_contact_id.value=oidMemberReg;
	document.frmmemberreg.command.value="<%=Command.EDIT%>";
	document.frmmemberreg.prev_command.value="<%=prevCommand%>";
	document.frmmemberreg.action="memberreg_edit.jsp";
	document.frmmemberreg.submit();
}

function cmdBack()
{
	document.frmmemberreg.command.value="<%=Command.BACK%>";
	document.frmmemberreg.action="memberreg_list.jsp";
	document.frmmemberreg.submit();
}

function cmdListFirst()
{
	document.frmmemberreg.command.value="<%=Command.FIRST%>";
	document.frmmemberreg.prev_command.value="<%=Command.FIRST%>";
	document.frmmemberreg.action="memberreg_edit.jsp";
	document.frmmemberreg.submit();
}

function cmdListPrev()
{
	document.frmmemberreg.command.value="<%=Command.PREV%>";
	document.frmmemberreg.prev_command.value="<%=Command.PREV%>";
	document.frmmemberreg.action="memberreg_edit.jsp";
	document.frmmemberreg.submit();
}

function cmdListNext()
{
	document.frmmemberreg.command.value="<%=Command.NEXT%>";
	document.frmmemberreg.prev_command.value="<%=Command.NEXT%>";
	document.frmmemberreg.action="memberreg_edit.jsp";
	document.frmmemberreg.submit();
}

function cmdListLast()
{
	document.frmmemberreg.command.value="<%=Command.LAST%>";
	document.frmmemberreg.prev_command.value="<%=Command.LAST%>";
	document.frmmemberreg.action="memberreg_edit.jsp";
	document.frmmemberreg.submit();
}

 function cmdUpdateKec(){
                document.frmmemberreg.command.value="<%=iCommand%>";
                document.frmmemberreg.commandRefresh.value= "1";
                document.frmmemberreg.action="memberreg_edit.jsp";
                document.frmmemberreg.submit();
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
    <link href="../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" --> <!-- #EndEditable -->
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
            <form name="frmmemberreg" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_personal_discount_id" value="<%=oidPersonalBonus%>">
              <input type="hidden" name="hidden_contact_id" value="<%=oidMemberReg%>">
              <input type="hidden" name="hidden_member_registration_history_id" value="<%=oidMemberRegistrationHistory%>">
              <input type="hidden" name="commandRefresh" value= "0">
              <input type="hidden" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_CONTACT_TYPE] %>" value="7">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top">
                  <td height="8" valign="middle" colspan="3">
                    <table width="100%" border="0" cellspacing="1" cellpadding="0" id="COMP">
                      <tr align="left">
                        <td height="21" valign="middle" colspan="6">
                          <hr>
                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td colspan="6">
                          <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <td width="50%" valign="top">
                                <table width="100%" border="0" cellspacing="1" cellpadding="1">
                                  <tr id="jumlah" align="left" valign="top">
                                    <td height="21" valign="top" class="mainheader"><u><%=textListHeader[SESS_LANGUAGE][34]%></u> </td>
                                    <td height="21" valign="top">&nbsp;</td>
                                    <td height="21" valign="top" colspan="3" class="comment">*)=
                                      <%=textListTitle[SESS_LANGUAGE][1]%></td>
                                  <tr id="kode" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][2]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" <%if(oidMemberReg==504404268268772501L){%>readonly<%}%> name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_CONTACT_CODE] %>"  value="<%= memberReg.getContactCode() %>" class="formElemen">
                                        * <%= frmMemberReg.getErrorMsg(FrmMemberReg.FRM_FIELD_CONTACT_CODE) %>
                                  <tr id="tipe_member" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][0]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <%
                                            Vector membergroupid_value = new Vector(1,1);
                                            Vector membergroupid_key = new Vector(1,1);
                                            String sel_membergroupid = ""+memberReg.getMemberGroupId();

                                            String cmdGroup = "onkeydown=\"javascript:cmdEnter('"+FrmMemberReg.FRM_FIELD_MEMBER_GROUP_ID+"')\"";
                                            if(listGroup!=null&&listGroup.size()>0){
                                                    for(int i=0;i<listGroup.size();i++){
                                                            MemberGroup mGroup = (MemberGroup)listGroup.get(i);
                                                            //if (mGroup.getViewCustomerType()!=0){
                                                                membergroupid_key.add(""+mGroup.getOID());
                                                                membergroupid_value.add(mGroup.getCode());
                                                            //}
                                                            
                                                    }
                                            }
                                       %>
                                      <%= ControlCombo.draw(frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_MEMBER_GROUP_ID],null, sel_membergroupid, membergroupid_key, membergroupid_value, cmdGroup, "formElemen") %>
                                  <tr id="nama" align="left" valign="top">
                                    <td height="21" valign="top" width="18%">Title</td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_TITLE] %>"  value="<%= memberReg.getTitle() %>" class="formElemen" size="10" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_TITLE%>')">
                                      <%= frmMemberReg.getErrorMsg(FrmMemberReg.FRM_FIELD_TITLE) %>
                                  <tr id="nama" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][3]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_PERSON_NAME] %>"  value="<%= memberReg.getPersonName() %>" class="formElemen" size="50" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_PERSON_NAME%>')">
                                      * <%= frmMemberReg.getErrorMsg(FrmMemberReg.FRM_FIELD_PERSON_NAME) %>
                                   <tr id="nama" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][35]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_PERSON_LASTNAME] %>"  value="<%= memberReg.getPersonLastname()%>" class="formElemen" size="50" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_PERSON_LASTNAME%>')">
                                      <%= frmMemberReg.getErrorMsg(FrmMemberReg.FRM_FIELD_PERSON_LASTNAME) %>
                                  <tr align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][29]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3"><%
                                        Vector neg_value = new Vector(1, 1);
                                        Vector neg_key = new Vector(1, 1);
                                        neg_value.add("0");
                                        neg_key.add("select ...");
                                        Vector listNeg = PstNegara.list(0, 0, "", " NAMA_NGR ");
                                        for (int i = 0; i < listNeg.size(); i++) {
                                            Negara neg = (Negara) listNeg.get(i);
                                            neg_key.add(neg.getNmNegara());
                                            neg_value.add(neg.getNmNegara());
                                        }
                                        //String selectNegara ="";
                                        if(memberReg.getHomeCountry()!=""|| memberReg.getHomeCountry()!=null){
                                            homeCountry=  memberReg.getHomeCountry();
                                        }
                                        %>
                                        <%= ControlCombo.draw(frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_COUNTRY], "formElemen", null,""+homeCountry, neg_value, neg_key, "")%>
                                    </td>
                                </tr>
                                <td width="55%">
                                  <tr align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][28]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3"><%
                                        Vector pro_value = new Vector(1, 1);
                                        Vector pro_key = new Vector(1, 1);
                                        Provinsi pro = new Provinsi();
                                        pro_value.add("0");
                                        pro_key.add("select ...");
                                        //Vector listPro = PstProvinsi.list(0, 0, "", " NAMA_PROP ");
                                        String strWhere = "";//"neg."+PstNegara.fieldNames[PstNegara.FLD_NM_NEGARA] + "='" +homeCountry+"'";
                                        Vector listPro = PstProvinsi.list(0, 0, strWhere, "NAMA_PROP");
                                        for (int i = 0; i < listPro.size(); i++) {
                                            Provinsi prov = (Provinsi) listPro.get(i);
                                            pro_key.add(prov.getNmProvinsi());
                                            pro_value.add(prov.getNmProvinsi());
                                        }

                                        if(memberReg.getHomeCountry()!=""|| memberReg.getHomeCountry()!=null){
                                            homeProvince= memberReg.getHomeProvince();
                                        }

                                        %>
                                        <%= ControlCombo.draw(frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_PROVINCE], "formElemen", null, "" + homeProvince, pro_value, pro_key, "")%>
                                    </td>
                                <tr align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][27]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3"><%
                                    Vector kab_value = new Vector(1, 1);
                                    Vector kab_key = new Vector(1, 1);
                                    kab_value.add("0");
                                    kab_key.add("select ...");
                                    //Vector listKab = PstKabupaten.list(0, 0, "", " NAMA_KAB ");
                                    String strWhereKab =  "";//"prov."+PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI] + "='"+homeProvince+"'";
                                    Vector listKab = PstKabupaten.list(0, 0, strWhereKab, "NAMA_KAB");
                                    for (int i = 0; i < listKab.size(); i++) {
                                        Kabupaten kab = (Kabupaten) listKab.get(i);
                                        kab_key.add(kab.getNmKabupaten());
                                        kab_value.add(kab.getNmKabupaten());
                                    }
                                    if(memberReg.getHomeTown()!=""|| memberReg.getHomeTown() !=null){
                                        homeTown= memberReg.getHomeTown();
                                    }
                                    %> <%= ControlCombo.draw(frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_TOWN], "formElemen", null, "" +homeTown, kab_value, kab_key, "")%>
                                </td>
                                </tr>
                                <tr id="Area">
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][36]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                        <%
                                        Vector kacHome_value = new Vector(1, 1);
                                        Vector kacHome_key = new Vector(1, 1);
                                        kacHome_value.add("0");
                                        kacHome_key.add("select ...");
                                        //Vector listKab = PstKabupaten.list(0, 0, "", " NAMA_KAB ");
                                        String strWhereKacHome=  "";//"prov."+PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI] + "='"+province+"'";//PstKabupaten.fieldNames[PstKabupaten.FLD_ID_PROPINSI] + "='"+memberReg.getProvince()+"'";
                                        Vector listKacHome = PstKecamatan.list(0, 0, strWhereKacHome, "");
                                        for (int i = 0; i < listKacHome.size(); i++) {
                                            Kecamatan kac = (Kecamatan) listKacHome.get(i);
                                            kacHome_key.add(kac.getNmKecamatan());
                                            kacHome_value.add(kac.getNmKecamatan());
                                        }
                                        %><%= ControlCombo.draw(FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_STATE], "formElemen", null,memberReg.getHomeState() , kacHome_value, kacHome_key,"")%>
                                    </td>
                                  </tr>
                                  <tr id="alamat" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][4]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <textarea name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_ADDR] %>" class="formElemen" cols="30" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_HOME_ADDR%>')"><%=memberReg.getHomeAddr() %></textarea>
                                      * <%= frmMemberReg.getErrorMsg(FrmMemberReg.FRM_FIELD_HOME_ADDR) %>
                                   <tr id="kodepost" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][30]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_POSTALCODE] %>"  value="<%=memberReg.getHomePostalCode()%>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_HOME_POSTALCODE%>')">
                                      * <%= frmMemberReg.getErrorMsg(FrmMemberReg.FRM_FIELD_HOME_POSTALCODE) %>
                                  <tr id="no_telp" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][5]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_TELP] %>"  value="<%= memberReg.getHomeTelp() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_HOME_TELP%>')">
                                  <tr id="no_hp" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][6]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_TELP_MOBILE] %>"  value="<%= memberReg.getTelpMobile() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_HOME_FAX%>')">
                                  <tr id="tempat_tanggal_lahir" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][7]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_TOWN] %>"  value="<%= memberReg.getHomeTown() %>" class="formElemen" size="20" >
                                      <%
						  				String cmdDate = "onkeydown=\"javascript:cmdEnter('"+FrmMemberReg.FRM_FIELD_MEMBER_BIRTH_DATE+"')\"";
						  				%>
                                      <%=	ControlDate.drawDateWithStyle(frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_MEMBER_BIRTH_DATE], memberReg.getMemberBirthDate()==null?new Date():memberReg.getMemberBirthDate(), 1,-70, "formElemen", "") %> **
                                  <tr id="jen_kel" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][8]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" width="17%">
                                      <% Vector membersex_value = new Vector(1,1);
                                            Vector membersex_key = new Vector(1,1);
                                            String sel_membersex = ""+memberReg.getMemberSex();
                                       //membersex_key.add("---select---");
                                       //membersex_value.add("");
                                       for(int i=0;i<PstMemberReg.sexNames.length;i++){
                                                    membersex_key.add(""+i);
                                                    membersex_value.add(PstMemberReg.sexNames[0][i]);
                                       }
                                       String cmdSex = "onkeydown=\"javascript:cmdEnter('"+FrmMemberReg.FRM_FIELD_MEMBER_SEX+"')\"";
                                       %>
                                      <%= ControlCombo.draw(frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_MEMBER_SEX],null, sel_membersex, membersex_key, membersex_value, cmdSex, "formElemen") %>                                     <td id="agama" height="21" width="7%">
                                    <td width="55%">
                                  <tr align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][9]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <% Vector memberreligionid_value = new Vector(1,1);
                                            Vector memberreligionid_key = new Vector(1,1);
                                            String sel_memberreligionid = ""+memberReg.getMemberReligionId();
                                           Vector listReligion = PstReligion.list(0,0,"","");
                                           if(listReligion!=null&&listReligion.size()>0){
                                                for(int i=0;i<listReligion.size();i++){
                                                        Religion religion = (Religion)listReligion.get(i);
                                                        memberreligionid_key.add(""+religion.getOID());
                                                        memberreligionid_value.add(religion.getReligion());
                                                }
                                           }
                                           String cmdRelig= "onkeydown=\"javascript:cmdEnter('"+FrmMemberReg.FRM_FIELD_MEMBER_RELIGION_ID+"')\"";
                                       %>
                                      <%= ControlCombo.draw(frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_MEMBER_RELIGION_ID],null, sel_memberreligionid, memberreligionid_key, memberreligionid_value, cmdRelig, "formElemen") %>
                                  <tr align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][37]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3"><%
                                        Vector nation_value = new Vector(1, 1);
                                        Vector nation_key = new Vector(1, 1);
                                        nation_value.add("0");
                                        nation_key.add("select ...");
                                        Vector listNation = PstNegara.list(0, 0, "", " NAMA_NGR ");
                                        for (int i = 0; i < listNeg.size(); i++) {
                                            Negara neg = (Negara) listNeg.get(i);
                                            nation_key.add(neg.getNmNegara());
                                            nation_value.add(neg.getNmNegara());
                                        }
                                        //String selectNegara ="";
                                        if(memberReg.getNationality()!=""|| memberReg.getNationality()!=null){
                                            nationality=memberReg.getNationality();
                                        }
                                        %>
                                        <%= ControlCombo.draw(frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_NATIONALITY], "formElemen", null,""+nationality, nation_value, nation_key, "")%>
                                    </td>
                                </tr>
                                <tr id="pekerjaan" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][38]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_MEMBER_OCCUPATION] %>"  value="<%= memberReg.getMemberOccupation() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_MEMBER_OCCUPATION%>')">
                                <tr id="email1" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][39]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_EMAIL] %>"  value="<%= memberReg.getHomeEmail() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_HOME_EMAIL%>')">

                                <tr id="email2" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][40]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_EMAIL] %>"  value="<%= memberReg.getEmail() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_EMAIL%>')">

                                  <tr id="no_id" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][10]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_MEMBER_ID_CARD_NUMBER] %>"  value="<%= memberReg.getMemberIdCardNumber() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_MEMBER_ID_CARD_NUMBER%>')">
                                <tr id="consigment_limit">
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][23]%></td>
                                    <td width="4%">:</td>
                                    <td>
                                    <%
                                    String wherex  = PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE;
                                    Vector listCurr = PstCurrencyType.list(0,0,wherex,"");
                                    Vector vectCurrVal = new Vector(1,1);
                                    Vector vectCurrKey = new Vector(1,1);
                                    for(int i=0; i<listCurr.size(); i++){
                                        CurrencyType currencyType = (CurrencyType)listCurr.get(i);
                                        vectCurrKey.add(currencyType.getCode());
                                        vectCurrVal.add(""+currencyType.getOID());
                                    }
                                    %>
                                    <%=ControlCombo.draw(frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT],"formElemen", "--select--", ""+memberReg.getCurrencyTypeIdMemberConsigmentLimit(), vectCurrVal, vectCurrKey, "")%>
                                    </td>
                                    <td><input type="text"  class="formElemen" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_MEMBER_CONSIGMENT_LIMIT]%>" value="<%=FRMHandler.userFormatStringDecimal( memberReg.getMemberConsigmentLimit()) %>"  class="formElemen" size="15" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_MEMBER_CONSIGMENT_LIMIT%>')">
                                    </td>
                                  </tr>
                                  <tr id="credit_limit">
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][11]%></td>
                                    <td width="4%">:</td>
                                    <td valign="top">
                                    <%=ControlCombo.draw(frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT],"formElemen", "--select--", ""+memberReg.getCurrencyTypeIdMemberCreditLimit(), vectCurrVal, vectCurrKey, "")%>
                                    </td>
                                    <td colspan="0">
                                      <input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_MEMBER_CREDIT_LIMIT]%>"  value="<%= FRMHandler.userFormatStringDecimal(memberReg.getMemberCreditLimit())%>" class="formElemen" size="30" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_MEMBER_CREDIT_LIMIT%>')">
                                    </td>
                                  </tr>
                                   <tr id="day_term_of_payment" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][32]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_DAY_OF_PAYMENT] %>"  value="<%= memberReg.getDayOfPayment() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_DAY_OF_PAYMENT%>')"> <%=textListHeader[SESS_LANGUAGE][33]%>
                                  <tr><td height="21" width="20" valign="right" colspan="3" class="comment"></td></tr>
                                  </tr>
                                  </table>
                              </td>
                              <td valign="top" width="50%">
                                <table width="100%" border="0" cellspacing="1" cellpadding="1">
                                  <tr id="data_agen">
                                    <td class="mainheader" colspan="5"><u><%=textListHeader[SESS_LANGUAGE][41]%></u></td>
                                  </tr>
                                  <tr id="company">
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][12]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                      <input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_COMP_NAME]%>"  value="<%= memberReg.getCompName() %>" class="formElemen" size="30" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_COMP_NAME%>')">
                                    </td>
                                  </tr>
                                  <tr id="country">
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][29]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                      <%
                                        Vector nega_value = new Vector(1, 1);
                                        Vector nega_key = new Vector(1, 1);
                                        nega_value.add("0");
                                        nega_key.add("select ...");
                                        Vector listNegg = PstNegara.list(0, 0, "", " NAMA_NGR ");
                                        for (int i = 0; i < listNegg.size(); i++) {
                                           Negara nega = (Negara) listNegg.get(i);
                                            nega_key.add(nega.getNmNegara());
                                            nega_value.add(nega.getNmNegara());
                                        }

                                         //String selectNegara ="";
                                        if(memberReg.getCountry()!=""|| memberReg.getCountry()!=null){
                                            country=  memberReg.getCountry();
                                        }
                                        %>
                                        <%= ControlCombo.draw(frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_COUNTRY], "formElemen", null,country, nega_value, nega_key, "")%>
                                        <%--<input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_COUNTRY]%>"  value="<%= memberReg.getCountry()%>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_COUNTRY%>')">
                                        --%>
                                    </td>
                                  </tr>
                                  <tr id="Town">
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][28]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                        <%
                                        Vector prov_value = new Vector(1, 1);
                                        Vector prov_key = new Vector(1, 1);
                                        Provinsi prov = new Provinsi();
                                        prov_value.add("0");
                                        prov_key.add("select ...");
                                        String strWheres = "";//"neg."+PstNegara.fieldNames[PstNegara.FLD_NM_NEGARA] + "='"+country+"'";// PstProvinsi.fieldNames[PstProvinsi.FLD_ID_NEGARA] + "= '"+country+"'";
                                        Vector listProv = PstProvinsi.list(0, 0, strWheres, "NAMA_PROP");
                                        for (int i = 0; i < listProv.size(); i++) {
                                            Provinsi provv = (Provinsi) listProv.get(i);
                                            prov_key.add(provv.getNmProvinsi());
                                            prov_value.add(provv.getNmProvinsi());
                                        }
                                        if(memberReg.getProvince()!=""|| memberReg.getProvince()!=null){
                                            province= memberReg.getProvince();
                                        }
                                        %>
                                        <%= ControlCombo.draw(frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_PROVINCE], "formElemen", null, "" +province, prov_value, prov_key, "")%>
                                      <%--<input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_PROVINCE]%>"  value="<%= memberReg.getProvince() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_PROVINCE%>')">
                                      --%>
                                      </td>
                                  </tr>
                                  <tr id="Town">
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][27]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                        <%
                                        Vector kabb_value = new Vector(1, 1);
                                        Vector kabb_key = new Vector(1, 1);
                                        kabb_value.add("0");
                                        kabb_key.add("select ...");
                                        //Vector listKab = PstKabupaten.list(0, 0, "", " NAMA_KAB ");
                                        String strWhereKabb =  "";//"prov."+PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI] + "='"+province+"'";//PstKabupaten.fieldNames[PstKabupaten.FLD_ID_PROPINSI] + "='"+memberReg.getProvince()+"'";
                                        Vector listKabb = PstKabupaten.list(0, 0, strWhereKabb, "NAMA_KAB");
                                        for (int i = 0; i < listKabb.size(); i++) {
                                            Kabupaten town = (Kabupaten) listKabb.get(i);
                                            kabb_key.add(town.getNmKabupaten());
                                            kabb_value.add(town.getNmKabupaten());
                                        }
                                        %><%= ControlCombo.draw(FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_TOWN], "formElemen", null,memberReg.getTown() , kabb_value, kabb_key,"")%>
                                        <%--<input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_TOWN]%>"  value="<%= memberReg.getTown() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_TOWN%>')">
                                        --%>
                                    </td>
                                  </tr>
                                  <tr id="Area">
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][36]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                        <%
                                        Vector kac_value = new Vector(1, 1);
                                        Vector kac_key = new Vector(1, 1);
                                        kac_value.add("0");
                                        kac_key.add("select ...");
                                        //Vector listKab = PstKabupaten.list(0, 0, "", " NAMA_KAB ");
                                        String strWhereKac=  "";//"prov."+PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI] + "='"+province+"'";//PstKabupaten.fieldNames[PstKabupaten.FLD_ID_PROPINSI] + "='"+memberReg.getProvince()+"'";
                                        Vector listKac = PstKecamatan.list(0, 0, strWhereKac, "");
                                        for (int i = 0; i < listKac.size(); i++) {
                                            Kecamatan kac = (Kecamatan) listKac.get(i);
                                            kac_key.add(kac.getNmKecamatan());
                                            kac_value.add(kac.getNmKecamatan());
                                        }
                                        %><%= ControlCombo.draw(FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_COMP_STATE], "formElemen", null,memberReg.getCompState() , kac_value, kac_key,"")%>
                                    </td>
                                  </tr>
                                  <tr id="address">
                                    <td width="31%" valign="top"><%=textListHeader[SESS_LANGUAGE][4]%></td>
                                    <td width="4%" valign="top">:</td>
                                    <td colspan="3">
                                      <textarea name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_BUSS_ADDRESS]%>" class="formElemen" cols="50" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_BUSS_ADDRESS%>')"><%= memberReg.getBussAddress() %></textarea>
                                    </td>
                                  </tr>
                                  <tr id="address">
                                    <td width="31%" valign="top"><%=textListHeader[SESS_LANGUAGE][42]%></td>
                                    <td width="4%" valign="top">:</td>
                                    <td colspan="3">
                                      <input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_COMP_EMAIL]%>"  value="<%= memberReg.getCompEmail() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_COMP_EMAIL%>')">
                                    </td>
                                  </tr>
                                  <tr id="telp">
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][5]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                      <input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_TELP_NR]%>"  value="<%= memberReg.getTelpNr() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_TELP_NR%>')">
                                    </td>
                                  </tr>
                                  <tr id="Fax">
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][26]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                      <input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_FAX]%>"  value="<%= memberReg.getFax() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_FAX%>')">
                                    </td>
                                  </tr>
                                  <tr id="zip">
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][30]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                      <input type="text" name="<%=frmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_POSTAL_CODE]%>"  value="<%= memberReg.getPostalCode()%>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmMemberReg.FRM_FIELD_POSTAL_CODE%>')">
                                    </td>
                                  </tr>
                                  <tr align="left">
                                     <td width="31%">Location Intern</td>
                                     <td width="4%">:</td>
                                     <td colspan="3">
                                        <%
                                        Vector listLocation = PstLocation.listAll();
                                        Vector val_terms = new Vector(1,1);
                                        Vector key_terms = new Vector(1,1);
                                        val_terms.add("0");
                                        key_terms.add("None");
                                        for(int d=0; d<listLocation.size(); d++){
                                            Location location = (Location)listLocation.get(d);
                                            val_terms.add(""+location.getOID());
                                            key_terms.add(location.getName());
                                        }
                                        String select_loc = ""+memberReg.getLocationId();
                                        %>  
                                        <%=ControlCombo.draw(frmMemberReg.fieldNames[frmMemberReg.FRM_LOCATION_ID],null,select_loc,val_terms,key_terms,"","formElemen")%>
                                        <%=frmMemberReg.getErrorMsg(frmMemberReg.FRM_LOCATION_ID)%>
                                      </td>
                                    </tr>
                                </table>
                                <%
                                if(oidMemberReg!=0)
                                {
                                %>
                                <br>
                                <table width="100%" border="0" cellspacing="1" cellpadding="1">
                                  <tr>
                                    <td class="mainheader" colspan="5"><u><%=textListHeader[SESS_LANGUAGE][22]%></u> </td>
                                  </tr>
                                  <%
                                    String str_dt_RegistrationDate = "";
                                    String str_dt_ValidStartDate = "";
                                    String str_dt_ValidExpiredDate = "";
                                    if(listHistory!=null&&listHistory.size()==1){
                                            MemberRegistrationHistory memberRegistrationHistory = (MemberRegistrationHistory)listHistory.get(0);
                                            str_dt_RegistrationDate = Formater.formatDate(memberRegistrationHistory.getRegistrationDate(), SESS_LANGUAGE, "dd MMMM yyyy");
                                            str_dt_ValidStartDate = Formater.formatDate(memberRegistrationHistory.getValidStartDate(), SESS_LANGUAGE,"dd MMMM yyyy");
                                            str_dt_ValidExpiredDate = Formater.formatDate(memberRegistrationHistory.getValidExpiredDate(), SESS_LANGUAGE, "dd MMMM yyyy");
                                    }
                                  %>
                                  <tr>
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][13]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                      <input type="text" name="frm_regdate"  value="<%=str_dt_RegistrationDate%>" class="formElemen" disabled="yes">
                                    </td>
                                  </tr>
                                  <tr>
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][14]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                      <input type="text" name="frm_startdate"  value="<%=str_dt_ValidStartDate%>" class="formElemen" disabled="yes">
                                    </td>
                                  </tr>
                                  <tr>
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][15]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                      <input type="text" name="frm_enddate"  value="<%=str_dt_ValidExpiredDate%>" class="formElemen" disabled="yes">
                                    </td>
                                  </tr>
                                  <tr>
                                    <td width="31%">&nbsp;</td>
                                    <td width="4%">&nbsp;</td>
                                    <td colspan="3" valign="top">
                                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top">
                                          <td height="22" valign="middle" width="5%"><a href="javascript:cmdAddHistory('<%=oidMemberReg%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1001','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image1001" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=textListHeader[SESS_LANGUAGE][15]%>"></a></td>
                                          <td height="22" valign="middle" width="95%"><a href="javascript:cmdAddHistory('<%=oidMemberReg%>')"><%=textListHeader[SESS_LANGUAGE][15]%></a></td>
                                        </tr>
                                      </table>
                                    </td>
                                  </tr>
                                </table>
                                <%}%>
                              </td>
                            </tr>
                          </table>
                        </td>
                      <tr>
                        <td colspan="6">&nbsp;</td>
                      </tr>
                      <tr>
                        <td colspan="6">
                          <%
                                    ctrLine.setLocationImg(approot+"/images");
                                    ctrLine.initDefault(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0]);
                                    ctrLine.setTableWidth("100%");
                                    ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_SAVE,true));
                                    ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true)+" List");
                                    ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ASK,true));
                                    ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_CANCEL,false));

                                    String scomDel = "javascript:cmdAsk('"+oidMemberReg+"')";
                                    String sconDelCom = "javascript:cmdConfirmDelete('"+oidMemberReg+"')";
                                    String scancel = "javascript:cmdEdit('"+oidMemberReg+"')";
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

                                    if(iCommand == Command.EDIT && privUpdate == false){
                                            ctrLine.setSaveCaption("");
                                    }

                                    if (privAdd == false){
                                            ctrLine.setAddCaption("");
                                    }
                                    if(oidMemberReg==504404268268772501L){
                                        ctrLine.setDeleteCaption("");
                                    }
									%>
                          <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%></td>
                      </tr>
                      <tr>
                        <td colspan="6">&nbsp;</td>
                      </tr>
                      <tr>
                        <td colspan="6">&nbsp;</td>
                      </tr>
                      <tr>
                        <td colspan="6">
                          <%
                          if( (oidMemberReg!=0) && (iCommand!=Command.ASK) && (iCommand!=Command.DELETE))
                          {
                          %>
                          <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr align="left" valign="top">
                              <!--td height="22" valign="middle" width="3%">
                                <div align="center"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true)%>"></a></div>
                              </td-->
                              <%if(privAdd){%>
                              <td height="22" valign="middle" width="15%"><a class="btn btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true)%></a></td>
                              <%}%>
                              <!--td height="22" valign="middle" width="3%">
                                <div align="center"><a href="javascript:cmdAddDiscount('<%=oidMemberReg%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=textListHeader[SESS_LANGUAGE][20]%>"></a></div>
                              </td-->
                              <td height="22" valign="middle" width="79%"><a class="btn btn-primary" href="javascript:cmdAddDiscount('<%=oidMemberReg%>')"><i class="fa fa-file"></i> <%=textListHeader[SESS_LANGUAGE][20]%></a></td>
                            </tr>
                          </table>
                          <%}%>
                        </td>
                      </tr>
                      <tr>
                          <td colspan="6">
                              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                  <tr align="left" valign="top">
                                      <td height="22" valign="middle" width="3%">
                                        <div align="center">&nbsp;</div>
                                      </td>
                                      <td height="22" valign="middle" width="3%">
                                        <div align="center">&nbsp;</div>
                                      </td>
                                      <td height="22" valign="middle" width="3%">
                                        <div align="center">&nbsp;</div>
                                      </td>
                                      <td height="22" valign="middle" width="3%">
                                        <div align="center">
                                            <!--a href="javascript:cmdViewHistory('<%=oidMemberReg%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=textListHeader[SESS_LANGUAGE][20]%>"></a-->
                                            &nbsp;&nbsp;
                                            <a class="btn btn-primary" href="javascript:cmdViewHistory('<%=oidMemberReg%>')"><i class="fa fa-file"></i> <%=textListHeader[SESS_LANGUAGE][31]%></a></div>
                                      </td>
                                  </tr>
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
            <%@include file="../styletemplate/footer.jsp" %>

        <%}else{%>
            <%@ include file = "../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
