<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.gui.jsp.ControlDate"%>
<%@page import="java.util.Date"%>
<%@page import="com.dimata.posbo.entity.masterdata.MemberGroup"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMemberRegistrationHistory"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMemberGroup"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.MemberReg"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>

<%@page import="com.dimata.qdep.form.FRMHandler"%>
<%@page import="com.dimata.common.entity.payment.DailyRate"%>
<%@page import="com.dimata.common.form.payment.CtrlDailyRate"%>
<%@page import="com.dimata.common.form.payment.FrmDailyRate"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import ="com.dimata.common.entity.location.PstNegara"%>
<%@page import ="com.dimata.common.entity.location.Negara"%>
<%@page import ="com.dimata.common.entity.location.Kabupaten"%>
<%@page import ="com.dimata.common.entity.location.Provinsi"%>
<%@page import ="com.dimata.common.entity.location.PstProvinsi"%>
<%@page import ="com.dimata.common.entity.location.PstKabupaten"%>
<%@page import ="com.dimata.common.entity.location.PstKecamatan"%>
<%@page import ="com.dimata.common.form.location.FrmPropinsi"%> 
<%@page import ="com.dimata.common.form.location.FrmNegara"%> 
<%@page import ="com.dimata.common.form.location.FrmKabupaten"%>
<%@page import ="com.dimata.common.form.location.CtrlProvinsi"%>
<%@page import ="com.dimata.common.form.location.CtrlKabupaten"%>
<%@page import ="com.dimata.common.form.location.CtrlKecamatan"%>
<%@page import ="com.dimata.hanoman.entity.masterdata.PstContact"%>
<%@page import ="com.dimata.hanoman.entity.masterdata.PstContactClass"%>
<%@page import ="com.dimata.hanoman.form.masterdata.CtrlContact"%>
<%@page import ="com.dimata.hanoman.entity.masterdata.Contact"%>
<%@page import ="com.dimata.hanoman.form.masterdata.FrmContact"%>


<%@ page import="com.dimata.common.entity.location.Kecamatan,
                 com.dimata.common.form.location.FrmKecamatan,               
                 com.dimata.qdep.form.FRMQueryString,
                 
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
    /*
 * 
 * Created on 	:  [date] [time] AM/PM
 *
 * @author  	:  [Fitra]
 * @version  	:  [version]
 */

	{"Perusahaan","Harus diisi","0=cash ; >0 Kredit"},
	{"Company","required","0=cash ; >0 Kredit"}
};

public static final String textListHeader[][] =
{
	{"Tipe Member","Status","Kode","Nama Perusahaan","Alamat","No Telp","No HP","Tmp/Tgl Lahir",//7
     "Jns Kelamin","Agama","No ID","Credit Limit","Nama","Tgl Mendaftar","Tgl Mulai Berlaku",//7
	"Tgl Akhir Berlaku","Nama Produk","Persen","Jumlah","Data Utama","Potongan Khusus","Status Pendaftaran","Status Akhir","Consigment Limit",//9
        "Payment History Note","Member Currency ID","Fax","City","Province","Negara","Kode Pos","View History","Waktu Jatuh Tempo","hari",
        "Tgl Mendaftar", "E-mail","Username","Password","re-type Password","No Handphone","Fax","Akun Bankt","Akun Bank 2","Alamat Perusahaan"},//
	{"Member Type","Status","Code","Name","Address","Telp Number", "HP Number","Place/Birthdate",
     "Sex","Religion","ID Number","Company Name","Registration Date","Valid Start Date",
	"Valid Expired Date","Product Name","Percent","Amount","Main Data","Special Discount","Registration Status","Last Status","Member Currency ID","Fax","Town","Province","Country","Postal Code","View History","Day Term Of Payment","days"}
};
%>

<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidContact = FRMQueryString.requestLong(request, "hidden_contacts_id");
long oidMemberReg = FRMQueryString.requestLong(request, "hidden_contact_id");
long oidContactClass = FRMQueryString.requestLong(request, "hidden_contactClass_id");
long oidContactClassAssign = FRMQueryString.requestLong(request, "hidden_contactClassAssign_id");
long oidMemberRegistrationHistory = FRMQueryString.requestLong(request, "hidden_member_registration_history_id");
long oidPersonalBonus = FRMQueryString.requestLong(request, "hidden_personal_discount_id");





int recordToGet = 10;
String msgString = "";
String msgsString = "";
int iErrCode = FRMMessage.NONE;
int iErrCodes = FRMMessage.NONE;
int contactClassType = 3;
String whereClause = "";
String orderClause = "";

MemberReg memberreg = new MemberReg();
//Contact contact = new Contact();


ControlLine ctrLine = new ControlLine();
//CtrlMemberReg ctrlMemberReg = new CtrlMemberReg(request);
CtrlContact ctrlContact = new CtrlContact(request);

//iErrCode = ctrlMemberReg.action(iCommand, oidMemberReg,userId,userName );

FrmContact frmContact = ctrlContact.getForm();
//FrmMemberReg frmMemberReg = ctrlMemberReg.getForm();
//FrmMemberReg frmMember = ctrlMemberReg.getForm();
CtrlMemberReg ctrlMemberReg = new CtrlMemberReg(request);
FrmMemberReg frmMemberReg = ctrlMemberReg.getForm();
MemberReg memberReg = ctrlMemberReg.getMemberReg();
Contact contact = ctrlContact.getContact();
//MemberReg memberReg = ctrlMemberReg.getMemberReg();
//MemberReg homeCountry = ctrlMemberReg.getMemberReg();
msgsString = ctrlContact.getMessage();
//msgString =  ctrlMemberReg.getMessage();
//int process = memberReg.getProcessStatus();


Vector listContact = new Vector(1,1);



String where = "";
String whContact = "CLS."+ PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+ " = " +contactClassType;
listContact = PstContact.listContactByType(0,0,whContact ,where);

int excCode = FRMMessage.NONE;
if(iCommand == Command.SAVE){
	frmContact.requestEntityObject(contact);
	String pwd = FRMQueryString.requestString(request,frmContact.fieldNames[FrmContact.FRM_FIELD_PASSWORD]); 
	String repwd  = FRMQueryString.requestString(request,frmContact.fieldNames[FrmContact.FRM_FIELD_RE_PASSWORD]); 
	if(!pwd.equals(repwd)){
		excCode = FRMMessage.ERR_PWDSYNC;
		msgString = FRMMessage.getMessage(excCode);
	}
}


if(excCode == FRMMessage.NONE){
	excCode = ctrlContact.action(iCommand, oidContact,oidContactClassAssign, contactClassType,userId, userName);
	msgString =  ctrlContact.getMessage();
	contact = ctrlContact.getContact();
    if(excCode<0)
        excCode = 0;
}


if( (iCommand==Command.SAVE) && (frmContact.errorSize()==0)  ){
	iCommand = Command.EDIT;
	oidContact = contact.getOID();
}

// jika command DELETE, maka langsung akses ke page list
if(iCommand==Command.DELETE){
    if(frmContact.errorSize()==0){
	    response.sendRedirect(approot+"/masterdata/memberCompany_list.jsp?command="+Command.FIRST);
    }else{
        //iCommand = Command.EDIT;
        contact = PstContact.fetchExc(oidContact);
    }
}
//limit



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
            CtrlKecamatan ctrlKecamatan = new CtrlKecamatan(request);
            //FrmKecamatan frmKecamatan = ctrlKecamatan.getForm();
            //Kecamatan kecamatan = ctrlKecamatan.getKecamatan();
          // add by fitra          
          //String sMemberRegistrasi = FRMQueryString.requestString(request,FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_COUNTRY]);
          String sHomeCountry = FRMQueryString.requestString(request, frmContact.fieldNames[FrmContact.FRM_FIELD_HOME_COUNTRY]);
          String sHomeProvince = FRMQueryString.requestString(request, frmContact.fieldNames[FrmContact.FRM_FIELD_HOME_PROVINCE]);
           //String sProvince = FRMQueryString.requestString(request, FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_PROVINCE]);
           
            String sRegency =  FRMQueryString.requestString(request,frmContact.fieldNames[FrmContact.FRM_FIELD_HOME_TOWN]);
             String sCompCountry =  FRMQueryString.requestString(request,FrmContact.fieldNames[FrmContact.FRM_FIELD_COMP_COUNTRY]);
          String sCompProvince =  FRMQueryString.requestString(request,FrmContact.fieldNames[FrmContact.FRM_FIELD_COMP_PROVINCE]);
          String sCompRegency =  FRMQueryString.requestString(request,FrmContact.fieldNames[FrmContact.FRM_FIELD_COMP_REGENCY]);
          String nationality = FRMQueryString.requestString(request, FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_NATIONALITY]);   
           String homeCountry = FRMQueryString.requestString(request, FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_COUNTRY]); 
           String homeProvince = FRMQueryString.requestString(request, FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_PROVINCE]);
           String homeTown = FRMQueryString.requestString(request, FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_TOWN]);
           String country = FRMQueryString.requestString(request, FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_COUNTRY]);
           String province = FRMQueryString.requestString(request, FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_PROVINCE]);
          //String sHomeCountry = FRMQueryString.requestString(request, frmMember.fieldNames[FrmMemberReg.FRM_FIELD_CONTACT_ID]);

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
        
        
        oidContact = contact.getOID();
        
       

	if(((iCommand==Command.SAVE)))
        {
	%>
 <jsp:forward page="memberCompany_list.jsp"> 
<jsp:param name="start" value="<%=start%>"/>
<jsp:param name="hidden_contacts_id" value="<%=contact.getOID()%>"/>
<jsp:param name="command" value="<%=Command.LIST%>" />	
</jsp:forward>
<%
}
   %>     
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">


function cmdEnters (index)
{
	if(event.keyCode == 13)
	{
		var oid = document.frmcontact.<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_MEMBER_GROUP_ID]%>.value;
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
			case '<%=FrmContact.FRM_FIELD_MEMBER_GROUP_ID%>' :
				document.frmcontact.<%=frmContact.fieldNames[FrmContact.FRM_FIELD_PERSON_NAME]%>.focus();
				break;
                                

			

			case '<%=FrmContact.FRM_FIELD_PERSON_NAME%>' :
				document.frmcontact.<%=frmContact.fieldNames[FrmContact.FRM_FIELD_HOME_ADDR]%>.focus();
				break;

			case '<%=FrmContact.FRM_FIELD_HOME_ADDR%>' :
				document.frmcontact.<%=frmContact.fieldNames[FrmContact.FRM_FIELD_HOME_TELP]%>.focus();
				break;

			case '<%=FrmContact.FRM_FIELD_HOME_TELP%>' :
				document.frmcontact.<%=frmContact.fieldNames[FrmContact.FRM_FIELD_HOME_FAX]%>.focus();
				break;

			case '<%=FrmContact.FRM_FIELD_HOME_FAX%>' :
				document.frmcontact.<%=frmContact.fieldNames[FrmContact.FRM_FIELD_HOME_TOWN]%>.focus();
				break;

			case '<%=FrmContact.FRM_FIELD_EMAIL%>' :
				document.frmcontact.<%=frmContact.fieldNames[FrmContact.FRM_FIELD_USER_ID]%>.focus();
				break;


			

			

			case '<%=FrmContact.FRM_FIELD_COMP_NAME%>' :
				document.frmcontact.<%=frmContact.fieldNames[FrmContact.FRM_FIELD_BUSS_ADDRESS]%>.focus();
				break;

			case '<%=FrmContact.FRM_FIELD_BUSS_ADDRESS%>' :
				document.frmcontact.<%=frmContact.fieldNames[FrmContact.FRM_FIELD_TELP_NR]%>.focus();
				break;

			case '<%=FrmContact.FRM_FIELD_TELP_NR%>' :
				cmdSave();
				break;

			default :
				break;
		}
	}
}



function cmdViewHistory(oid) {
    var strvalue ="../main/historypo.jsp?command=<%=Command.FIRST%>"+
                     "&oidDocHistory="+oid;
    window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}


function cmdAdd()
{
	document.frmcontact.hidden_contacts_id.value="0";
	document.frmcontact.command.value="<%=Command.ADD%>";
	document.frmcontact.prev_command.value="<%=prevCommand%>";
	document.frmcontact.action="memberRegCompany_edit.jsp";
	document.frmcontact.submit();
}

function cmdAddHistory(oid)
{
	document.frmcontact.hidden_contacts_id.value=oid;
	document.frmcontact.command.value="<%=Command.LIST%>";
	document.frmcontact.prev_command.value="<%=prevCommand%>";
	document.frmcontact.action="memberregistrationhistory.jsp";
	document.frmcontact.submit();
}

function cmdViewHistory(oid) {
    var strvalue ="../main/historypo.jsp?command=<%=Command.FIRST%>"+
                     "&oidDocHistory="+oid;
    window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function cmdAddDiscount(oid)
{
	document.frmcontact.hidden_contacts_id.value=oid;
	document.frmcontact.command.value="<%=Command.LIST%>";
	document.frmcontact.prev_command.value="<%=prevCommand%>";
	document.frmcontact.action="personaldiscount.jsp";
	document.frmcontact.submit();
}

function cmdAsk(oidContact)
{
	document.frmcontact.hidden_contacts_id.value=oidContact;
	document.frmcontact.command.value="<%=Command.ASK%>";
	document.frmcontact.prev_command.value="<%=prevCommand%>";
	document.frmcontact.action="memberRegCompany_edit.jsp";
	document.frmcontact.submit();
}

function cmdConfirmDelete(oidContact)
{
	document.frmcontact.hidden_contacts_id.value=oidContact;
	document.frmcontact.command.value="<%=Command.DELETE%>";
	document.frmcontact.prev_command.value="<%=prevCommand%>";
	document.frmcontact.action="memberRegCompany_edit.jsp";
	document.frmcontact.submit();
}

function cmdSave()
{
	document.frmcontact.command.value="<%=Command.SAVE%>";
	document.frmcontact.prev_command.value="<%=prevCommand%>";
	document.frmcontact.action="memberRegCompany_edit.jsp";
	document.frmcontact.submit();
}

 function cmdUpdateKec(){
                document.frmcontact.command.value="<%=iCommand%>";
                document.frmcontact.commandRefresh.value= "1";
                document.frmcontact.action="memberRegCompany_edit.jsp";
                document.frmcontact.submit();
            }
            
            
 function cmdUpdateProv(){
                
                document.frmcontact.command.value="<%=Command.EDIT%>";
               
                document.frmcontact.action="memberRegCompany_edit.jsp";
                document.frmcontact.submit();
            }           

function cmdEdit(oidContact)
{
	document.frmcontact.hidden_contacts_id.value=oidContact;
	document.frmcontact.command.value="<%=Command.EDIT%>";
	document.frmcontact.prev_command.value="<%=prevCommand%>";
	document.frmcontact.action="memberRegCompany_edit.jsp";
	document.frmcontact.submit();
}

function cmdCancel(oidContact)
{
	document.frmcontact.hidden_contacts_id.value=oidContact;
	document.frmcontact.command.value="<%=Command.EDIT%>";
	document.frmcontact.prev_command.value="<%=prevCommand%>";
	document.frmcontact.action="memberRegCompany_edit.jsp";
	document.frmcontact.submit();
}

function cmdBack()
{
	document.frmcontact.command.value="<%=Command.BACK%>";
	document.frmcontact.action="memberCompany_list.jsp";
	document.frmcontact.submit();
}

function cmdListFirst()
{
	document.frmcontact.command.value="<%=Command.FIRST%>";
	document.frmcontact.prev_command.value="<%=Command.FIRST%>";
	document.frmcontact.action="memberRegCompany_edit.jsp";
	document.frmcontact.submit();
}

function cmdListPrev()
{
	document.frmcontact.command.value="<%=Command.PREV%>";
	document.frmcontact.prev_command.value="<%=Command.PREV%>";
	document.frmcontact.action="memberRegCompany_edit.jsp";
	document.frmcontact.submit();
}

function cmdListNext()
{
	document.frmcontact.command.value="<%=Command.NEXT%>";
	document.frmcontact.prev_command.value="<%=Command.NEXT%>";
	document.frmcontact.action="memberRegCompany_edit.jsp";
	document.frmcontact.submit();
}

function cmdListLast()
{
	document.frmcontact.command.value="<%=Command.LAST%>";
	document.frmcontact.prev_command.value="<%=Command.LAST%>";
	document.frmcontact.action="memberRegCompany_edit.jsp";
	document.frmcontact.submit();
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
    <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../styles/main.css" type="text/css">
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
            Membership &gt; <%=textListTitle[SESS_LANGUAGE][0]%> <!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form name="frmcontact" method ="post" action="">
                  <input type="hidden" name="commandRefresh" value= "0">
              <input type="hidden" name="command" value="<%=iCommand%>">
             
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_personal_discount_id" value="<%=oidPersonalBonus%>">
              <input type="hidden" name="hidden_contacts_id" value="<%=oidContact%>">
               <input type="hidden" name="hidden_contactClass_id" value="<%=oidContactClass%>">
                  <input type="hidden" name="hidden_contactClassAssign_id" value="<%=oidContactClassAssign%>">
              <input type="hidden" name="hidden_member_registration_history_id" value="<%=oidMemberRegistrationHistory%>">
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
                                <table width="100%" border="0" cellspacing="0" cellpadding="1">
                                  <tr id="jumlah" align="left" valign="top">
                                    <td height="21" valign="top" class="mainheader"><u><%=textListHeader[SESS_LANGUAGE][18]%></u> </td>
                                    <td height="21" valign="top">&nbsp;</td>
                                    <td height="21" valign="top" colspan="3" class="comment">*)=
                                      <%=textListTitle[SESS_LANGUAGE][1]%></td>
                                 
                                    
                                      <tr id="Registration Date" align="left" valign="top">
                                    <td height="21" valign="top" width="39%"><%=textListHeader[SESS_LANGUAGE][34]%></td>
                                    <td height="21" valign="top" width="15%">:</td>
                                    <td height="21" colspan="3">
                                      
                                      <%
						  				String cmdRegDate = "onkeydown=\"javascript:cmdEnter('"+FrmContact.FRM_FIELD_REGDATE+"')\"";
						  				%>
                                                                                <%=	ControlDate.drawDateWithStyle(frmContact.fieldNames[FrmContact.FRM_FIELD_REGDATE], contact.getRegdate()==null?new Date():contact.getRegdate(), 1,-70, "formElemen", "") %> **
                                    
                                    <tr id="kode" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][2]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" <%if(oidContact==504404268268772501L){%>readonly<%}%> name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_CONTACT_CODE] %>"  value="<%= contact.getContactCode() %>" class="formElemen">
                                        * <%= frmContact.getErrorMsg(FrmContact.FRM_FIELD_CONTACT_CODE) %>
                                        
                                        
                                 <tr id="tipe_member" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][0]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <%
                                            Vector membergroupid_value = new Vector(1,1);
                                            Vector membergroupid_key = new Vector(1,1);
                                            String sel_membergroupid = ""+contact.getMemberGroupId();

                                            String cmdGroup = "onkeydown=\"javascript:cmdEnter('"+FrmContact.FRM_FIELD_MEMBER_GROUP_ID+"')\"";
                                            if(listGroup!=null&&listGroup.size()>0){
                                                    for(int i=0;i<listGroup.size();i++){
                                                            MemberGroup mGroup = (MemberGroup)listGroup.get(i);
                                                            membergroupid_key.add(""+mGroup.getOID());
                                                            membergroupid_value.add(mGroup.getCode());
                                                    }
                                            }
                                       %>
                                      <%= ControlCombo.draw(frmContact.fieldNames[FrmContact.FRM_FIELD_MEMBER_GROUP_ID],null, sel_membergroupid, membergroupid_key, membergroupid_value, cmdGroup, "formElemen") %>        
                                        
                                <tr id="nama" align="left" valign="top">
                                    <td height="21" valign="top" width="18%">Title</td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_TITLE] %>"  value="<%= contact.getTitle() %>" class="formElemen" size="10" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_TITLE%>')">
                                      * <%= frmContact.getErrorMsg(FrmContact.FRM_FIELD_TITLE) %>        
                                   <tr id="nama" align="left" valign="top">
                                    <td height="21" valign="top" width="18%">Nama Depan</td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_PERSON_NAME] %>"  value="<%= contact.getPersonName() %>" class="formElemen" size="30" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_PERSON_NAME%>')">
                                      * <%= frmContact.getErrorMsg(FrmContact.FRM_FIELD_PERSON_NAME) %>
                                  <tr id="nama" align="left" valign="top">
                                    <td height="21" valign="top" width="18%">Nama Belakang</td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_PERSON_LASTNAME] %>"  value="<%= contact.getPersonLastname()%>" class="formElemen" size="30" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_PERSON_LASTNAME%>')">
                                      * <%= frmContact.getErrorMsg(FrmContact.FRM_FIELD_PERSON_LASTNAME) %>    
                                        
                                      
                                      
                                      
                                      
                               <%-- <tr id="nama" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][3]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_COMP_NAME] %>"  value="<%= contact.getCompName() %>" class="formElemen" size="30" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_COMP_NAME%>')">
                                      * <%= frmContact.getErrorMsg(FrmContact.FRM_FIELD_COMP_NAME) %>      --%>   
                                 
                                      
                                      
                                   <tr id="username" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][36]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                    <input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_USER_ID] %>"  value="<%= contact.getUserId() %>" class="formElemen" size="30" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_USER_ID%>')">
                                       <%= frmContact.getErrorMsg(FrmContact.FRM_FIELD_USER_ID) %>   
                                       
                                       <%--    
                                   <tr id="password" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][37]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="password" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_PASSWORD] %>"  value="<%= contact.getPassword() %>" class="formElemen" size="50" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_PASSWORD%>')">
                                       <%= frmContact.getErrorMsg(FrmContact.FRM_FIELD_PASSWORD) %>       
                                      
                                      
                                    <tr id="re-password" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][38]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                     <input type="password" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_RE_PASSWORD] %>"  value="<%= contact.getPassword() %>" class="formElemen" size="50" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_RE_PASSWORD%>')">
                                       <%= frmContact.getErrorMsg(FrmContact.FRM_FIELD_RE_PASSWORD) %>  
                                       
  <script language="JavaScript">                                    
                                       
    function checkForm(form)
  {
    

    if(form.<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_PASSWORD]%>.value != "" && form.<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_PASSWORD]%>.value == form.<%=frmContact.fieldNames[FrmContact.FRM_FIELD_RE_PASSWORD]%>.value) {
      if(form.<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_PASSWORD]%>.value.length <= 6) {
        alert("Error: Password must contain at least six characters!");
        form.<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_PASSWORD]%>.focus();
        return false;
      }
      
     
     
     
    } else {
      alert("Error: Please check that you've entered and confirmed your password!");
      form.<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_PASSWORD]%>.focus();
      return false;
    }

    alert("You entered a valid password: " + form.<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_PASSWORD]%>.value);
    return true;
  }
    </script>        --%>                           
   
                                      
                                 
                                  
                                  <!-- add by fitra -->
                                  <tr align="left" valign="top">
                                    <td height="21" valign="top" width="18%">Negara</td>
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
                                        if(contact.getHomeCountry()!=""|| contact.getHomeCountry()!=null){
                                            homeCountry=  contact.getHomeCountry();
                                                                                                                                        }
                                                                                                                        %>
                                        <%= ControlCombo.draw(frmContact.fieldNames[FrmContact.FRM_FIELD_HOME_COUNTRY], "formElemen", null,""+homeCountry, neg_value, neg_key, "")%>
                                                                                                                    </td>
                                                                                                                </tr>
                                                                                                                <!-- add by fitra -->
                                                                                                                <td width="55%">
                                  <tr align="left" valign="top">
                                    <td height="21" valign="top" width="18%">Provinsi</td>
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

                                        if(contact.getHomeCountry()!=""|| contact.getHomeCountry()!=null){
                                            homeProvince= contact.getHomeProvince();
                                                                                                                              }

                                                                                                                        %>
                                        <%= ControlCombo.draw(frmContact.fieldNames[FrmContact.FRM_FIELD_HOME_PROVINCE], "formElemen", null, "" + homeProvince, pro_value, pro_key, "")%>
                                                                                                                    </td>
                                  <tr align="left" valign="top">
                                    <td height="21" valign="top" width="18%">Kota</td>
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
                                    if(contact.getHomeTown()!=""|| contact.getHomeTown() !=null){
                                        homeTown= contact.getHomeTown();
                                                                                                                                }
                                    %> <%= ControlCombo.draw(frmContact.fieldNames[FrmContact.FRM_FIELD_HOME_TOWN], "formElemen", null, "" +homeTown, kab_value, kab_key, "")%>
                                                                                                                    </td>
                                                                                                                </tr>
                                <tr id="Area">
                                    <td width="31%">Area</td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                        <%
                                        Vector kacHome_value = new Vector(1, 1);
                                        Vector kacHome_key = new Vector(1, 1);
                                        kacHome_value.add("0");
                                        kacHome_key.add("select ...");
                                        //Vector listKab = PstKabupaten.list(0, 0, "", " NAMA_KAB ");
                                        String strWhereKacHome=  "";//"prov."+PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI] + "='"+province+"'";//PstKabupaten.fieldNames[PstKabupaten.FLD_ID_PROPINSI] + "='"+contact.getProvince()+"'";
                                        Vector listKacHome = PstKecamatan.list(0, 0, strWhereKacHome, "");
                                        for (int i = 0; i < listKacHome.size(); i++) {
                                            Kecamatan kac = (Kecamatan) listKacHome.get(i);
                                            kacHome_key.add(kac.getNmKecamatan());
                                            kacHome_value.add(kac.getNmKecamatan());
                                        }
                                        %><%= ControlCombo.draw(FrmContact.fieldNames[FrmContact.FRM_FIELD_HOME_STATE], "formElemen", null,contact.getHomeState() , kacHome_value, kacHome_key,"")%>
                                    </td>
                                  </tr>                                                                              
                                                                                                              
                                    <tr id="alamat" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][4]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <textarea name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_HOME_ADDR] %>" class="formElemen" cols="30" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_HOME_ADDR%>')"><%=contact.getHomeAddr() %></textarea>
                                      * <%= frmContact.getErrorMsg(FrmContact.FRM_FIELD_HOME_ADDR) %>
                                   <tr id="kodepost" align="left" valign="top">
                                    <td height="21" valign="top" width="18%">Kode Pos</td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_HOME_POSTALCODE] %>"  value="<%=contact.getHomePostalCode()%>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_HOME_POSTALCODE%>')">
                                      * <%= frmContact.getErrorMsg(FrmContact.FRM_FIELD_HOME_POSTALCODE) %>
                                    <tr id="no_telp" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][5]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_HOME_TELP] %>"  value="<%= contact.getHomeTelp() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_HOME_TELP%>')">
                                  <tr id="no_hp" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][6]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_TELP_MOBILE] %>"  value="<%= contact.getTelpMobile() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_HOME_FAX%>')">
                                  <tr id="tempat_tanggal_lahir" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][7]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_BIRTH_PLACE] %>"  value="<%= contact.getBirthPlace() %>" class="formElemen" size="20" >
                                      <%
						  				String cmdDate = "onkeydown=\"javascript:cmdEnter('"+FrmContact.FRM_FIELD_MEMBER_BIRTH_DATE+"')\"";
						  				%>
                                      <%=	ControlDate.drawDateWithStyle(frmContact.fieldNames[FrmContact.FRM_FIELD_MEMBER_BIRTH_DATE], contact.getMemberBirthDate()==null?new Date():contact.getMemberBirthDate(), 1,-70, "formElemen", "") %> **
                                  <tr id="jen_kel" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][8]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" width="17%">
                                      <% Vector membersex_value = new Vector(1,1);
                                            Vector membersex_key = new Vector(1,1);
                                            String sel_membersex = ""+contact.getMemberSex();
                                       //membersex_key.add("---select---");
                                       //membersex_value.add("");
                                       for(int i=0;i<PstContact.sexNames.length;i++){
                                                    membersex_key.add(""+i);
                                                    membersex_value.add(PstContact.sexNames[0][i]);
                                       }
                                       String cmdSex = "onkeydown=\"javascript:cmdEnter('"+FrmContact.FRM_FIELD_MEMBER_SEX+"')\"";
                                       %>
                                      <%= ControlCombo.draw(frmContact.fieldNames[FrmContact.FRM_FIELD_MEMBER_SEX],null, sel_membersex, membersex_key, membersex_value, cmdSex, "formElemen") %>                                     <td id="agama" height="21" width="7%">
                                    <td width="55%">
                                  <tr align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][9]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <% Vector memberreligionid_value = new Vector(1,1);
                                            Vector memberreligionid_key = new Vector(1,1);
                                            String sel_memberreligionid = ""+contact.getMemberReligionId();
                                           Vector listReligion = PstReligion.list(0,0,"","");
                                           if(listReligion!=null&&listReligion.size()>0){
                                                for(int i=0;i<listReligion.size();i++){
                                                        Religion religion = (Religion)listReligion.get(i);
                                                        memberreligionid_key.add(""+religion.getOID());
                                                        memberreligionid_value.add(religion.getReligion());
                                                }
                                           }
                                           String cmdRelig= "onkeydown=\"javascript:cmdEnter('"+FrmContact.FRM_FIELD_MEMBER_RELIGION_ID+"')\"";
                                       %>
                                      <%= ControlCombo.draw(frmContact.fieldNames[FrmContact.FRM_FIELD_MEMBER_RELIGION_ID],null, sel_memberreligionid, memberreligionid_key, memberreligionid_value, cmdRelig, "formElemen") %>
                                  <tr align="left" valign="top">
                                    <td height="21" valign="top" width="18%">Nationality</td>
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
                                        if(contact.getNationality()!=""|| contact.getNationality()!=null){
                                            nationality=contact.getNationality();
                                        }
                                        %>
                                        <%= ControlCombo.draw(frmContact.fieldNames[FrmContact.FRM_FIELD_NATIONALITY], "formElemen", null,""+nationality, nation_value, nation_key, "")%>
                                    </td>
                                </tr>
                                <tr id="pekerjaan" align="left" valign="top">
                                    <td height="21" valign="top" width="18%">Pekerjaan</td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_MEMBER_OCCUPATION] %>"  value="<%= contact.getMemberOccupation() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_MEMBER_OCCUPATION%>')">
                                <tr id="email1" align="left" valign="top">
                                    <td height="21" valign="top" width="18%">Email 1</td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_HOME_EMAIL] %>"  value="<%= contact.getHomeEmail() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_HOME_EMAIL%>')">
                                  
                                <tr id="email2" align="left" valign="top">
                                    <td height="21" valign="top" width="18%">Email 2</td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_EMAIL] %>"  value="<%= contact.getEmail() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_EMAIL%>')">
                                      
                                  <tr id="no_id" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][10]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_MEMBER_ID_CARD_NUMBER] %>"  value="<%= contact.getMemberIdCardNumber() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_MEMBER_ID_CARD_NUMBER%>')">
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
                                    <%=ControlCombo.draw(frmContact.fieldNames[FrmContact.FRM_FIELD_CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT],"formElemen", "--select--", ""+contact.getCurrencyTypeIdMemberConsigmentLimit(), vectCurrVal, vectCurrKey, "")%>
                                    </td>
                                    <td><input type="text"  class="formElemen" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_MEMBER_CONSIGMENT_LIMIT]%>" value="<%=FRMHandler.userFormatStringDecimal( contact.getMemberConsigmentLimit()) %>"  class="formElemen" size="15" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_MEMBER_CONSIGMENT_LIMIT%>')">
                                    </td>
                                  </tr>
                                  <tr id="credit_limit">
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][11]%></td>
                                    <td width="4%">:</td>
                                    <td valign="top">
                                    <%=ControlCombo.draw(frmContact.fieldNames[FrmContact.FRM_FIELD_CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT],"formElemen", "--select--", ""+contact.getCurrencyTypeIdMemberCreditLimit(), vectCurrVal, vectCurrKey, "")%>
                                    </td>
                                    <td colspan="0">
                                      <input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_MEMBER_CREDIT_LIMIT]%>"  value="<%= FRMHandler.userFormatStringDecimal(contact.getMemberCreditLimit())%>" class="formElemen" size="30" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_MEMBER_CREDIT_LIMIT%>')">
                                    </td>
                                  </tr>
                                   <tr id="day_term_of_payment" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][32]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_DAY_OF_PAYMENT] %>"  value="<%= contact.getDayOfPayment() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_DAY_OF_PAYMENT%>')"> <%=textListHeader[SESS_LANGUAGE][33]%>
                                  <tr><td height="21" width="20" valign="right" colspan="3" class="comment"></td></tr>
                                  </tr>                                                                        
                          </table>
                        <br>
                        <td valign="top" width="50%">       
                                <table width="70%" border="0" cellspacing="1" cellpadding="1"> 
                                       
                                 <tr id="data_agen">
                                    <td class="mainheader" colspan="5"><u>Data Perusahaan</u></td>
                                  </tr>      
                                     <tr id="company">
                                    <td width="31%">Nama Perusahaan</td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                      <input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_COMP_NAME]%>"  value="<%= contact.getCompName() %>" class="formElemen" size="30" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_COMP_NAME%>')">
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
                                        if(contact.getCountry()!=""|| contact.getCountry()!=null){
                                            country=  contact.getCountry();
                                                                                                                        }
                                                                                                                        %>
                                        <%= ControlCombo.draw(frmContact.fieldNames[FrmContact.FRM_FIELD_COUNTRY], "formElemen", null,country, nega_value, nega_key, "")%>
                                        <%--<input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_COUNTRY]%>"  value="<%= contact.getCountry()%>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_COUNTRY%>')">
                                        --%>
                                                                                                                    </td>
                                                                                                                </tr>
                                  <tr id="Town">
                                    <td width="31%">Provinsi</td>
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
                                        if(contact.getProvince()!=""|| contact.getProvince()!=null){
                                            province= contact.getProvince();
                                                                                                                                     }
                                                                                                                        %>
                                        <%= ControlCombo.draw(frmContact.fieldNames[FrmContact.FRM_FIELD_PROVINCE], "formElemen", null, "" +province, prov_value, prov_key, "")%>
                                      <%--<input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_PROVINCE]%>"  value="<%= contact.getProvince() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_PROVINCE%>')">
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
                                        String strWhereKabb =  "";//"prov."+PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI] + "='"+province+"'";//PstKabupaten.fieldNames[PstKabupaten.FLD_ID_PROPINSI] + "='"+contact.getProvince()+"'";
                                        Vector listKabb = PstKabupaten.list(0, 0, strWhereKabb, "NAMA_KAB");
                                        for (int i = 0; i < listKabb.size(); i++) {
                                            Kabupaten town = (Kabupaten) listKabb.get(i);
                                            kabb_key.add(town.getNmKabupaten());
                                            kabb_value.add(town.getNmKabupaten());
                                                                                                                        }
                                        %><%= ControlCombo.draw(FrmContact.fieldNames[FrmContact.FRM_FIELD_TOWN], "formElemen", null,contact.getTown() , kabb_value, kabb_key,"")%>
                                        <%--<input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_TOWN]%>"  value="<%= contact.getTown() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_TOWN%>')">
                                        --%>
                                    </td>
                                  </tr>
                                  <tr id="Area">
                                    <td width="31%">Area</td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                        <%
                                        Vector kac_value = new Vector(1, 1);
                                        Vector kac_key = new Vector(1, 1);
                                        kac_value.add("0");
                                        kac_key.add("select ...");
                                        //Vector listKab = PstKabupaten.list(0, 0, "", " NAMA_KAB ");
                                        String strWhereKac=  "";//"prov."+PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI] + "='"+province+"'";//PstKabupaten.fieldNames[PstKabupaten.FLD_ID_PROPINSI] + "='"+contact.getProvince()+"'";
                                        Vector listKac = PstKecamatan.list(0, 0, strWhereKac, "");
                                        for (int i = 0; i < listKac.size(); i++) {
                                            Kecamatan kac = (Kecamatan) listKac.get(i);
                                            kac_key.add(kac.getNmKecamatan());
                                            kac_value.add(kac.getNmKecamatan());
                                                                                                                            }
                                        %><%= ControlCombo.draw(FrmContact.fieldNames[FrmContact.FRM_FIELD_COMP_STATE], "formElemen", null,contact.getCompState() , kac_value, kac_key,"")%>
                                    </td>
                                  </tr>

                                  <tr id="address">
                                    <td width="31%" valign="top">Email</td>
                                    <td width="4%" valign="top">:</td>
                                    <td colspan="3">
                                      <input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_COMP_EMAIL]%>"  value="<%= contact.getCompEmail() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_COMP_EMAIL%>')">
                                                                                                                    </td>
                                                                                                                </tr>
                                  <tr id="telp">
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][5]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                      <input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_TELP_NR]%>"  value="<%= contact.getTelpNr() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_TELP_NR%>')">
                                    </td>
                                  </tr>
                                  <tr id="Fax">
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][26]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                      <input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_FAX]%>"  value="<%= contact.getFax() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_FAX%>')">
                                    </td>
                                  </tr>
                                  <tr id="zip">
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][30]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                      <input type="text" name="<%=frmContact.fieldNames[FrmContact.FRM_FIELD_POSTAL_CODE]%>"  value="<%= contact.getPostalCode()%>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContact.FRM_FIELD_POSTAL_CODE%>')">
                                    </td>
                                  </tr>
                                        </table>
                                      
                                     
                                      
                                      <br>
                                       
                                      
                                      
                                  
                                <%
                                if(oidContact!=0)
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
                                  
                                  
                                    <td width="31%">&nbsp;</td>
                                    <td width="4%">&nbsp;</td>
                                    <td colspan="3" valign="top">
                                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top">
                                          <td height="22" valign="middle" width="5%"><a href="javascript:cmdAddHistory('<%=oidContact%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1001','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image1001" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=textListHeader[SESS_LANGUAGE][15]%>"></a></td>
                                          <td height="22" valign="middle" width="95%"><a href="javascript:cmdAddHistory('<%=oidContact%>')"><%=textListHeader[SESS_LANGUAGE][15]%></a></td>
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

                                    String scomDel = "javascript:cmdAsk('"+oidContact+"')";
                                    String sconDelCom = "javascript:cmdConfirmDelete('"+oidContact+"')";
                                    String scancel = "javascript:cmdEdit('"+oidContact+"')";
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
                                    if(oidContact==504404268268772501L){
                                        ctrLine.setDeleteCaption("");
                                    }
									%>
                          <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%></td>
                      </tr>
                      <tr>
                        <td colspan="6">
                          <%
                          if( (oidContact!=0) && (iCommand!=Command.ASK) && (iCommand!=Command.DELETE))
                          {
                          %>
                          <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr align="left" valign="top">
                              <!--td height="22" valign="middle" width="3%">
                                <div align="center"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true)%>"></a></div>
                              </td-->
                              <td height="22" valign="middle" width="15%"><a class="btn btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true)%></a></td>
                              <!--td height="22" valign="middle" width="3%">
                                <div align="center"><a href="javascript:cmdAddDiscount('<%=oidContact%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=textListHeader[SESS_LANGUAGE][20]%>"></a></div>
                              </td-->
                              <td height="22" valign="middle" width="79%"><a class="btn btn-primary" href="javascript:cmdAddDiscount('<%=oidContact%>')"><i class="fa fa-file"></i> <%=textListHeader[SESS_LANGUAGE][20]%></a></td>
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
                                            <!--a href="javascript:cmdViewHistory('<%=oidContact%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=textListHeader[SESS_LANGUAGE][20]%>"></a-->
                                            &nbsp;&nbsp;
                                            <a class="btn btn-primary" href="javascript:cmdViewHistory('<%=oidContact%>')"><i class="fa fa-file"></i> <%=textListHeader[SESS_LANGUAGE][31]%></a></div>
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
            <%@include file="../../styletemplate/footer.jsp" %>

        <%}else{%>
            <%@ include file = "../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
