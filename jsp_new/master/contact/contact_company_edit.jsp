
<%@page import="com.dimata.posbo.session.masterdata.SessMemberReg"%>
<%@page import="com.dimata.harisma.entity.masterdata.Religion"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstReligion"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstContact"%>
<%@page import="com.dimata.posbo.entity.masterdata.MemberGroup"%>
<%@page import="com.dimata.common.entity.location.Kecamatan"%>
<%@page import="com.dimata.common.entity.location.PstKecamatan"%>
<%@page import="com.dimata.common.entity.location.Kabupaten"%>
<%@page import="com.dimata.common.entity.location.PstKabupaten"%>
<%@page import="com.dimata.common.entity.location.PstProvinsi"%>
<%@page import="com.dimata.common.entity.location.Provinsi"%>
<%@page import="com.dimata.common.entity.location.Negara"%>
<%@page import="com.dimata.common.entity.location.PstNegara"%>
<%@page import="com.dimata.common.entity.location.Location"%>
<%@page import="com.dimata.common.entity.location.PstLocation"%>
<%@page import="com.dimata.posbo.form.masterdata.FrmMatVendorPrice"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstUnit"%>
<%@page import="com.dimata.posbo.entity.masterdata.Unit"%>
<%@page import="com.dimata.posbo.entity.masterdata.MatVendorPrice"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMatVendorPrice"%>
<%@page import="com.dimata.posbo.entity.masterdata.MemberRegistrationHistory"%>
<% 
/* 
 * Page Name  		:  contactlist_edit.jsp
 * Created on 		:  [date] [time] AM/PM 
 * 
 * @author  		: karya 
 * @version  		: 01 
 */

/*******************************************************************
 * Page Description : [project description ... ] 
 * Imput Parameters : [input parameter ...] 
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
<!--package prochain02 -->
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.form.contact.*" %>
<%@ page import = "com.dimata.common.session.contact.*" %>
<%@ page import=  "com.dimata.posbo.entity.masterdata.PstMemberRegistrationHistory" %>
<%//@ page import = "com.dimata.prochain02.entity.admin.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_SUPPLIER); %>
<%@ include file = "../../main/checkuser.jsp" %>



<%!
public static final String textListTitleHeader[][] =
{
	{"DATA SUPPLIER","Kode Supplier","Kelompok","Nama","Kontak Person","Alamat Bisnis","Kota","Propinsi","Negara","No Telepon","No Fax","Email","Website","Location","Waktu Kirim"},
	{"SUPPLIER DATA","Supplier Code","Group","Name","Contact Person","Business Address","City","Province","Country","Phone","Fax","Email","Website","Location","Lead Time"}
};

public static final String textListTitle[][] =
{
	{"Supplier","Harus diisi","0=cash ; >0 Kredit"},
	{"Supplier","required","0=cash ; >0 Kredit"}
};

/*public static final String textListHeader[][] =
{
	{"Tipe Member","Status","Kode","Nama Depan","Alamat","No Telp","No HP","Tmp/Tgl Lahir",//7
     "Jns Kelamin","Agama","No ID","Credit Limit","Nama Perusahaan","Tgl Mendaftar","Tgl Mulai Berlaku",//7
	"Tgl Akhir Berlaku","Nama Produk","Persen","Jumlah","Data Utama","Potongan Khusus","Status Pendaftaran","Status Akhir","Consigment Limit",//9
        "Payment History Note","Member Currency ID","Fax","City","Province","Negara","Kode Pos","View History","Waktu Jatuh Tempo","hari","Tambah Barang","Add Kontrak","Waktu Kirim"},//7
	{"Member Type","Status","Code","First Name","Address","Telp Number", "HP Number","Place/Birthdate",
     "Sex","Religion","ID Number","Company Name","Registration Date","Valid Start Date",
	"Valid Expired Date","Product Name","Percent","Amount","Main Data","Special Discount","Registration Status","Last Status","Member Currency ID","Fax","Town","Province","Country","Postal Code","View History","Day Term Of Payment","days","Add Item","Price Contract","Lead Time"}
};*/


public static final String textListHeader[][] =
{
	{"Tipe Member","Status","Kode","Nama Contact Person","Alamat","No Telp","No HP","Tmp/Tgl Lahir",//7
         "Jns Kelamin","Agama","No ID","Credit Limit","Nama Perusahaan","Tgl Mendaftar","Tgl Mulai Berlaku",//7
	"Periode Kontrak","Nama Produk","Persen","Jumlah","Data Utama","Potongan Khusus","Status Pendaftaran","Status Akhir","Consigment Limit",//9
        "Payment History Note","Member Currency ID","Fax","Kota","Provinsi","Negara","Kode Pos","View History","Waktu Jatuh Tempo","hari",
        "Tgl Mendaftar", "E-mail","Username","Password","re-type Password","No Handphone","Fax","Akun Bank","Akun Bank 2","Alamat Perusahaan"},//
        
        
	{"Member Type","Status","Code","Name Contact Person","Address","Telp Number","HP Number","Place/Birthdate",//7
         "Sex","Relegion","ID Number","Limit Credit ","Company","Registration Date","Valid Start Date",//7
	"Valid Expired Date","Product Name","Percent","Amount","Main Data","Special Discount","Registration Status","Last Status"," Limit Consigment",//9
        "Payment History Note","Member Currency ID","Fax","City","Province","Country","Postal Code","View History","Day Term Of Payment","Days",
        "Registration Date", "E-mail","Username","Password","re-type Password","Handphone Number","Fax","Bank Account","Bank Account 2","Company Address"}
        //{"Member Type","Status","Code","Name","Address","Telp Number", "HP Number","Place/Birthdate",
        //"Sex","Religion","ID Number","Company Name","Registration Date","Valid Start Date",
	//"Valid Expired Date","Product Name","Percent","Amount","Main Data","Special Discount","Registration Status","Last Status",
        //"Member Currency ID","Fax","Town","Province","Country","Postal Code","View History","Day Term Of Payment","days"}
};

public static final String textListTitleItem[][] =
{
	{"Daftar Harga Supplier","SKU","Klik sku untuk kembali ke edit barang","Nama Barang",
	 "Tidak ada data vendor price ..","Supplier","Kode","Unit","Harga Beli","Barcode",
	  "Discount Terakhir","Keterangan","PPN Terakhir","Harga Beli Terakhir",
	  "Tambah Harga Supplier","Harga Supplier","Kembali ke Daftar Barang","Mata Uang","Biaya Kirim","Diskon 1 Terakhir","Diskon 2 Terakhir","%"},
	{"Supplier Price List","SKU","Click sku for back to goods edit","Name",
	 "No data vendor price ..","Supplier","Code","Unit","Buying Price","Barcode",
	 "Last Discount","Description","Last Task","Last Buying Price",
	 "Add Supplier Price","Supplier Price","Back to Search Result","Currency","Cost Cargo", "Last Discount 1","Last Discount 2","%"}

};

public boolean getStatus(long index, Vector vct)
{
	if(vct!=null && vct.size()>0)
	{
		for(int i=0; i<vct.size(); i++)
		{
			ContactClassAssign cntAs = (ContactClassAssign)vct.get(i);
			if(index == cntAs.getContactClassId())
			{
				return true;
			}
		}
	}
	return false;
}

public boolean getStatus(long index, Vector vct, int iCommand)
{
	if(iCommand==Command.ADD)
	{
		return true;
	}
	else
	{		
		if(vct!=null && vct.size()>0)
		{
			for(int i=0; i<vct.size(); i++)
			{
				ContactClassAssign cntAs = (ContactClassAssign)vct.get(i);
				if(index == cntAs.getContactClassId())
				{
					return true;
				}
			}
		}
	}
	return false;
}

public String cekNull(String val)
{
	if((val==null) || (val.equals("null")))
		val = "";
	return val;	
}


public String drawList(int iCommand, Vector objectClass,int start,int SESS_LANGUAGE)

	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");

		ctrlist.addHeader("No","3%","0","0");
		ctrlist.addHeader(textListTitleItem[SESS_LANGUAGE][3],"20%","0","0");
		ctrlist.addHeader(textListTitleItem[SESS_LANGUAGE][6],"10%","0","0");
		ctrlist.addHeader(textListTitleItem[SESS_LANGUAGE][9],"10%","0","0");
                ctrlist.addHeader(textListTitleItem[SESS_LANGUAGE][17],"10%","0","0");
                ctrlist.addHeader(textListTitleItem[SESS_LANGUAGE][7],"5%","0","0");
		ctrlist.addHeader(textListTitleItem[SESS_LANGUAGE][8],"10%","0","0");
                // add discount %
                ctrlist.addHeader(textListTitleItem[SESS_LANGUAGE][19],"5%","0","0");
                ctrlist.addHeader(textListTitleItem[SESS_LANGUAGE][20],"5%","0","0");
                //end of add discount %
		ctrlist.addHeader(textListTitleItem[SESS_LANGUAGE][10],"5%","0","0");
                ctrlist.addHeader(textListTitleItem[SESS_LANGUAGE][12],"5%","0","0");
		ctrlist.addHeader(textListTitleItem[SESS_LANGUAGE][13],"10%","0","0");
		ctrlist.addHeader(textListTitleItem[SESS_LANGUAGE][11],"20%","0","0");


		ctrlist.setLinkRow(0);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		Vector rowx = new Vector(1,1);
		ctrlist.reset();
		int index = -1;
		String whereCls = "";
		String orderCls = "";

		if(start < 0)
			start = 0;

		for (int i = 0; i < objectClass.size(); i++) {
			 MatVendorPrice matVendorPrice = (MatVendorPrice)objectClass.get(i);
			 rowx = new Vector();
			 start = start + 1;

            Unit unit = new Unit();
            try{
                unit = PstUnit.fetchExc(matVendorPrice.getBuyingUnitId());
            }catch(Exception e){}
            
            Material material = new Material();
              try{
                material = PstMaterial.fetchExc(matVendorPrice.getMaterialId());
            }catch(Exception e){}
            
            
            rowx.add(""+start+"");
            rowx.add("<a href=\"javascript:cmdEditPriceList('"+String.valueOf(matVendorPrice.getOID())+"')\">"+material.getName()+"</a>");

            rowx.add(material.getSku());
            
            rowx.add(material.getBarCode());
            
            CurrencyType currencyType = new CurrencyType();
            try{
                currencyType = PstCurrencyType.fetchExc(matVendorPrice.getPriceCurrency());
            }catch(Exception e){}
            rowx.add(currencyType.getCode());
            rowx.add(unit.getCode());
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matVendorPrice.getOrgBuyingPrice())+"</div>");
            // add discount %
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matVendorPrice.getLastDiscount1())+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matVendorPrice.getLastDiscount2())+"</div>");
            //end of add discount %
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matVendorPrice.getLastDiscount())+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matVendorPrice.getLastVat())+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matVendorPrice.getCurrBuyingPrice())+"</div>");
            rowx.add(matVendorPrice.getDescription());

			lstData.add(rowx);
		}
		return ctrlist.draw();
	}

public static Vector parserVendorCode(String vCode){
    Vector vt = new Vector();
    StringTokenizer st0 = new StringTokenizer(vCode.toUpperCase(),";");
    while(st0.hasMoreTokens()){
        String strTemp1 = st0.nextToken();
        System.out.println("test : "+strTemp1);
        vt.add(strTemp1);
    }
    return vt;
}

%>
<!-- Jsp Block -->

<%
CtrlContactList ctrlContactList = new CtrlContactList(request);
long oidContactList = FRMQueryString.requestLong(request, "hidden_contact_id");
String sourceLink = FRMQueryString.requestString(request, "source_link");
long oidMemberRegistrationHistory = FRMQueryString.requestLong(request, "hidden_member_registration_history_id");
String companyTitle = "Supplier"; 

int iErrCode = FRMMessage.ERR_NONE;
String errMsg = ""; 
String whereClause = "";
String orderClause = "";
String msgString = "";
int iCommand = FRMQueryString.requestCommand(request);
int pictCommand = FRMQueryString.requestInt(request,"pict_command");
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int start = FRMQueryString.requestInt(request,"start");

ControlLine ctrLine = new ControlLine();
System.out.println("=============== >>>> contactList : >>> : "+oidContactList);

String whCntClass = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] + " = " + PstContactClass.CONTACT_TYPE_SUPPLIER;
Vector cntClass = PstContactClass.list(0, 0, whCntClass, "");
Vector classAssign = new Vector(1,1);
for(int a=0;a<cntClass.size();a++)
{
	long oid = FRMQueryString.requestLong(request,"contact_"+a);
	if(oid!=0)
	{
		ContactClassAssign cntAssign = 	new ContactClassAssign();
		cntAssign.setContactClassId(oid);
		classAssign.add(cntAssign);
	}
}

iErrCode = ctrlContactList.action(iCommand , oidContactList,classAssign);
msgString =  ctrlContactList.getMessage();	
String whereAssign = " "+PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID]+" = "+oidContactList;
if(iCommand==Command.EDIT)
{
	classAssign = PstContactClassAssign.list(0,0,whereAssign,"");
}	

errMsg = ctrlContactList.getMessage();
FrmContactList frmContactList = ctrlContactList.getForm();
ContactList contactList = ctrlContactList.getContactList();
oidContactList = contactList.getOID();

    System.out.println("=============== >>>> contactList : "+contactList.getContactCode()+" >>> : "+oidContactList);

whereClause = ""+PstContactList.fieldNames[PstContactList.FLD_CONTACT_TYPE]+" = "+PstContactList.EXT_COMPANY+
			" OR "+PstContactList.fieldNames[PstContactList.FLD_CONTACT_TYPE]+" = "+PstContactList.OWN_COMPANY;
//Vector vectContact = PstContactList.list(0,0,whereClause,"");	
whereClause = "";

//if(((iCommand==Command.SAVE)||(iCommand==Command.DELETE))&&(frmContactList.errorSize()<1) &&  (sourceLink==null || (sourceLink.equals("materialdosearch.jsp")==false)))
if((iCommand==Command.DELETE))
{
%>
<jsp:forward page="contact_company_list.jsp"> 
<jsp:param name="start" value="<%=start%>"/>
<jsp:param name="hidden_contact_id" value="<%=contactList.getOID()%>"/>
<jsp:param name="command" value="<%=Command.LIST%>" />	
</jsp:forward>
<%
}

%>
<!-- End of Jsp Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function  setSupplierOnLGRX(){
    alert("Hello");

}

<% if(  iCommand==Command.SAVE && (frmContactList.errorSize()<1 || true) &&  sourceLink!=null && sourceLink.equals("materialdosearch.jsp")){ %>
     function  setSupplierOnLGR(){
    <%
    String cntName = "";
    try{
    cntName  = contactList.getCompName();
                                if(cntName.length()==0){
                                    cntName = contactList.getPersonName()+" "+contactList.getPersonLastname();
                                }
    } catch(Exception exc){
          System.out.println("Exception "+exc);
        }
    %>
     var optionContact=self.opener.document.frm_recmaterial.<%=com.dimata.posbo.form.warehouse.FrmMatReceive.fieldNames[com.dimata.posbo.form.warehouse.FrmMatReceive.FRM_FIELD_SUPPLIER_ID]%>;
     try{
        optionContact.options[optionContact.length]= new Option("<%=cntName%>","<%=contactList.getOID()%>", false, true );        
        self.close();
     } catch(err){
        alert("Supplier tidak bisa di set : "+err) ;
     }
    }

<%}%>

function cmdCancel(){
	document.frm_contactlist.command.value="<%=Command.CANCEL%>";
	document.frm_contactlist.action="contact_company_edit.jsp";
	document.frm_contactlist.submit();
} 

function cmdEdit(oid){ 
	document.frm_contactlist.command.value="<%=Command.EDIT%>";
	document.frm_contactlist.action="contact_company_edit.jsp";
	document.frm_contactlist.submit(); 
} 

function cmdSave(){
	document.frm_contactlist.command.value="<%=Command.SAVE%>"; 
	document.frm_contactlist.action="contact_company_edit.jsp";
	document.frm_contactlist.submit();
}

function cmdAsk(oid){
	document.frm_contactlist.command.value="<%=Command.ASK%>"; 
	document.frm_contactlist.action="contact_company_edit.jsp";
	document.frm_contactlist.submit();
} 

function cmdConfirmDelete(oid){
	document.frm_contactlist.command.value="<%=Command.DELETE%>";
	document.frm_contactlist.action="contact_company_edit.jsp"; 
	document.frm_contactlist.submit();
}  

function cmdBack(){
	document.frm_contactlist.command.value="<%=Command.FIRST%>"; 
	document.frm_contactlist.action="contact_company_list.jsp";
	document.frm_contactlist.submit();
}


function cmdEditPriceList(vendorId){
	document.frm_contactlist.hidden_contact_id.value=vendorId;
	document.frm_contactlist.command.value="<%=Command.DETAIL%>";
	document.frm_contactlist.action="contact_company_list.jsp";
	document.frm_contactlist.submit();
}

function changeFocus(element, evt){
    if(evt.keyCode == 13) {
    if(element.name == "<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_CONTACT_CODE]%>") {
        document.frm_contactlist.<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_COMP_NAME]%>.focus();
    }
    else if(element.name == "<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_COMP_NAME]%>") {
        document.frm_contactlist.<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_PERSON_NAME]%>.focus();
    }
    else if(element.name == "<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_PERSON_NAME]%>") {
        document.frm_contactlist.<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_BUSS_ADDRESS]%>.focus();
    }
    else if(element.name == "<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_BUSS_ADDRESS]%>") {
        document.frm_contactlist.<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_TOWN]%>.focus();
    }
    else if(element.name == "<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_TOWN]%>") {
        document.frm_contactlist.<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_PROVINCE]%>.focus();
    }
    else if(element.name == "<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_PROVINCE]%>") {
        document.frm_contactlist.<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_COUNTRY]%>.focus();
    }
    else if(element.name == "<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_COUNTRY]%>") {
        document.frm_contactlist.<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_TELP_NR]%>.focus();
    }
    else if(element.name == "<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_TELP_NR]%>") {
        document.frm_contactlist.<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_FAX]%>.focus();
    }
    else if(element.name == "<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_FAX]%>") {
        document.frm_contactlist.<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_EMAIL]%>.focus();
    }
    else if(element.name == "<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_EMAIL]%>") {
        document.frm_contactlist.<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_WEBSITE]%>.focus();
    }
    else if(element.name == "<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_WEBSITE]%>") {
        cmdSave();
    }
    else {
        cmdSave();
    }
    }
}

function cmdAddHistory(oid)
{
	document.frm_contactlist.hidden_contact_id.value=oid;
	document.frm_contactlist.command.value="<%=Command.LIST%>";
	document.frm_contactlist.prev_command.value="<%=prevCommand%>";
	document.frm_contactlist.action="contractregistrationhistory.jsp";
	document.frm_contactlist.submit();
}

function cmdAddPriceList(oid){
        document.frm_contactlist.hidden_contact_id.value=oid;
	document.frm_contactlist.command.value="<%=Command.LIST%>";
	document.frm_contactlist.prev_command.value="<%=Command.FIRST%>";
	document.frm_contactlist.action="vdrmaterialpricecontract.jsp";
	document.frm_contactlist.submit();
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

<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" --> 
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Masterdata 
            &gt; <%=companyTitle%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_contactlist" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="hidden_contact_id" value="<%=oidContactList%>">
              <input type="hidden" name="source_link" value="<%=sourceLink%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_member_registration_history_id" value="<%=oidMemberRegistrationHistory%>">
              <table border="0" width="100%">
                <tr> 
                  <td height="8"> 
                    <hr size="1">
                  </td>
                </tr>
              </table>
              <tr align="left" valign="top">
                <td colspan="6">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="50%" valign="top">
                              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                   <tr id="jumlah" align="left" valign="top">
                                    <td height="9" valign="top" class="mainheader"><u>Kontak Person</u> </td>
                                    <td height="21" valign="top">&nbsp;</td>
                                    <td height="21" valign="top" colspan="3" class="comment">*)=
                                      <%=textListTitle[SESS_LANGUAGE][1]%></td>
                                      <tr id="Registration Date" align="left" valign="top">
                                    <td height="9" valign="top" width="39%"><%=textListHeader[SESS_LANGUAGE][34]%></td>
                                    <td height="21" valign="top" width="15%">:</td>
                                    <td height="21" colspan="3">
                                      <%
                                        String cmdRegDate = "onkeydown=\"javascript:cmdEnter('"+FrmContactList.FRM_FIELD_REGDATE+"')\"";
                                        %>
                                        <%=	ControlDate.drawDateWithStyle(FrmContactList.fieldNames[FrmContactList.FRM_FIELD_REGDATE], contactList.getRegdate()==null?new Date():contactList.getRegdate(), 1,-70, "formElemen", "") %>
                                    
                                   <tr id="nama" align="left" valign="top">
                                    <td height="9" valign="top" width="18%">Title</td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_TITLE] %>"  value="<%= contactList.getTitle() %>" class="formElemen" size="10" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_TITLE%>')">
                                      <%= frmContactList.getErrorMsg(FrmContactList.FRM_FIELD_TITLE) %>  
                                <tr id="nama" align="left" valign="top">
                                    <td height="9" valign="top" width="18%">Nama Depan </td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_PERSON_NAME] %>"  value="<%= contactList.getPersonName() %>" class="formElemen" size="50" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_PERSON_NAME%>')">
                                      <%= frmContactList.getErrorMsg(FrmContactList.FRM_FIELD_PERSON_NAME) %>        
                                    <tr id="nama" align="left" valign="top">
                                    <td height="9" valign="top" width="18%">Nama Belakang</td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_PERSON_LASTNAME] %>"  value="<%= contactList.getPersonLastname()%>" class="formElemen" size="50" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_PERSON_LASTNAME%>')">
                                      <%= frmContactList.getErrorMsg(FrmContactList.FRM_FIELD_PERSON_LASTNAME) %>   
                                  <tr align="left" valign="top">
                                    <td height="5" valign="top" width="20%">Negara</td>
                                    <td height="21" valign="top" width="3%">:</td>
                                    <td height="21" colspan="3">
                                        <%
                                            Vector neg_value = new Vector(1, 1);
                                            Vector neg_key = new Vector(1, 1);
                                            neg_value.add("0");
                                            neg_key.add("select ...");
                                            Vector listNeg = PstNegara.list(0, 0, "", " NAMA_NGR ");
                                            for (int i = 0; i < listNeg.size(); i++) {
                                                Negara neg = (Negara) listNeg.get(i);
                                                neg_key.add(neg.getNmNegara());
                                                neg_value.add(String.valueOf(neg.getNmNegara()));
                                            }
                                            %>
                                            <%= ControlCombo.draw(FrmContactList.fieldNames[FrmContactList.FRM_FIELD_HOME_COUNTRY], "formElemen", null, "" + contactList.getHomeCountry(), neg_value, neg_key)%>
                                        </td>
                                    </tr>
                                <!-- add by fitra -->
                                 <td width="55%">
                                  <tr align="left" valign="top">
                                    <td height="9" valign="top" width="8%">Provinsi</td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3"><%
                                        Vector pro_value = new Vector(1, 1);
                                        Vector pro_key = new Vector(1, 1);
                                        Provinsi pro = new Provinsi();
                                        pro_value.add("0");
                                        pro_key.add("select ...");
                                        
                                        Vector listPro = PstProvinsi.list(0, 0, "", "NAMA_PROP");
                                        for (int i = 0; i < listPro.size(); i++) {
                                            Provinsi prov = (Provinsi) listPro.get(i);
                                            pro_key.add(prov.getNmProvinsi());
                                            pro_value.add(String.valueOf(prov.getNmProvinsi()));
                                        }

                                        %>
                                        <%= ControlCombo.draw(FrmContactList.fieldNames[FrmContactList.FRM_FIELD_HOME_PROVINCE], "formElemen", null, "" + contactList.getHomeProvince(), pro_value, pro_key)%>
                                    </td>
                                </tr>
                                <!-- add by fitra -->
                                <td width="55%">
                                  <tr align="left" valign="top">
                                    <td height="9" valign="top" width="8%">Kota</td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3"><%
                                        Vector kab_value = new Vector(1, 1);
                                        Vector kab_key = new Vector(1, 1);
                                        kab_value.add("0");
                                        kab_key.add("select ...");
                                        //Vector listKab = PstKabupaten.list(0, 0, "", " NAMA_KAB ");

                                        Vector listKab = PstKabupaten.list(0, 0,"" , "NAMA_KAB");
                                        for (int i = 0; i < listKab.size(); i++) {
                                            Kabupaten kab = (Kabupaten) listKab.get(i);
                                            kab_key.add(kab.getNmKabupaten());
                                            kab_value.add(String.valueOf(kab.getNmKabupaten()));
                                        }

                                        %> <%= ControlCombo.draw(FrmContactList.fieldNames[FrmContactList.FRM_FIELD_HOME_TOWN], "formElemen", null, "" + contactList.getHomeTown() , kab_value, kab_key)%>
                                    </td>
                                </tr>
                                <tr id="Area">
                                    <td width="9%">Area</td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                        <%
                                        Vector kacHome_value = new Vector(1, 1);
                                        Vector kacHome_key = new Vector(1, 1);
                                        kacHome_value.add("0");
                                        kacHome_key.add("select ...");
                                        //Vector listKab = PstKabupaten.list(0, 0, "", " NAMA_KAB ");
                                        String strWhereKacHome=  "";//"prov."+PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI] + "='"+province+"'";//PstKabupaten.fieldNames[PstKabupaten.FLD_ID_PROPINSI] + "='"+contactList.getProvince()+"'";
                                        Vector listKacHome = PstKecamatan.list(0, 0, strWhereKacHome, "");
                                        for (int i = 0; i < listKacHome.size(); i++) {
                                            Kecamatan kac = (Kecamatan) listKacHome.get(i);
                                            kacHome_key.add(kac.getNmKecamatan());
                                            kacHome_value.add(kac.getNmKecamatan());
                                        }
                                        %><%= ControlCombo.draw(FrmContactList.fieldNames[FrmContactList.FRM_FIELD_HOME_STATE], "formElemen", null,contactList.getHomeState() , kacHome_value, kacHome_key,"")%>
                                    </td>
                                  </tr>
                                  <tr id="no_telp" align="left" valign="top">
                                    <td height="9" valign="top" width="8%"><%=textListHeader[SESS_LANGUAGE][4]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <textarea cols="30" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_HOME_ADDR] %>"  class="formElemen" onKeyDown="javascript:cmdenters('<%=FrmContactList.FRM_FIELD_HOME_ADDR%>')"><%= contactList.getHomeAddr() %></textarea>
                                   <tr id="kodepost" align="left" valign="top">
                                    <td height="9" valign="top" width="18%">Kode Pos</td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_HOME_POSTALCODE] %>"  value="<%=contactList.getHomePostalCode()%>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_HOME_POSTALCODE%>')">
                                    <tr id="no_telp" align="left" valign="top">
                                    <td height="9" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][5]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_HOME_TELP] %>"  value="<%= contactList.getHomeTelp() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_HOME_TELP%>')">
                                  <tr id="no_hp" align="left" valign="top">
                                    <td height="9" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][6]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_TELP_MOBILE] %>"  value="<%= contactList.getTelpMobile() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_HOME_FAX%>')">
                                  <tr id="tempat_tanggal_lahir" align="left" valign="top">
                                    <td height="9" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][7]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_HOME_TOWN] %>"  value="<%= contactList.getHomeTown() %>" class="formElemen" size="20" >
                                      <%
						  				String cmdDate = "onkeydown=\"javascript:cmdEnter('"+FrmContactList.FRM_FIELD_MEMBER_BIRTH_DATE+"')\"";
						  				%>
                                      <%=	ControlDate.drawDateWithStyle(FrmContactList.fieldNames[FrmContactList.FRM_FIELD_MEMBER_BIRTH_DATE], contactList.getMemberBirthDate()==null?new Date():contactList.getMemberBirthDate(), 1,-70, "formElemen", "") %> **
                                  <tr id="jen_kel" align="left" valign="top">
                                    <td height="9" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][8]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" width="17%">
                                      <% Vector membersex_value = new Vector(1,1);
                                            Vector membersex_key = new Vector(1,1);
                                            String sel_membersex = ""+contactList.getMemberSex();
                                       for(int i=0;i<PstContact.sexNames.length;i++){
                                                    membersex_key.add(""+i);
                                                    membersex_value.add(PstContact.sexNames[0][i]);
                                       }
                                       String cmdSex = "onkeydown=\"javascript:cmdEnter('"+FrmContactList.FRM_FIELD_MEMBER_SEX+"')\"";
                                       %>
                                      <%= ControlCombo.draw(FrmContactList.fieldNames[FrmContactList.FRM_FIELD_MEMBER_SEX],null, sel_membersex, membersex_key, membersex_value, cmdSex, "formElemen") %>                                     <td id="agama" height="21" width="7%">
                                    <td width="55%">
                                  <tr align="left" valign="top">
                                    <td height="9" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][9]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <% Vector memberreligionid_value = new Vector(1,1);
                                           Vector memberreligionid_key = new Vector(1,1);
                                           String sel_memberreligionid = ""+contactList.getMemberReligionId();
                                           Vector listReligion = PstReligion.list(0,0,"","");
                                           if(listReligion!=null&&listReligion.size()>0){
                                                for(int i=0;i<listReligion.size();i++){
                                                        Religion religion = (Religion)listReligion.get(i);
                                                        memberreligionid_key.add(""+religion.getOID());
                                                        memberreligionid_value.add(religion.getReligion());
                                                }
                                           }
                                           String cmdRelig= "onkeydown=\"javascript:cmdEnter('"+FrmContactList.FRM_FIELD_MEMBER_RELIGION_ID+"')\"";
                                       %>
                                      <%= ControlCombo.draw(FrmContactList.fieldNames[FrmContactList.FRM_FIELD_MEMBER_RELIGION_ID],null, sel_memberreligionid, memberreligionid_key, memberreligionid_value, cmdRelig, "formElemen") %>
                                  <tr align="left" valign="top">
                                    <td height="9" valign="top" width="18%">Nationality</td>
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
                                        %>
                                        <%= ControlCombo.draw(FrmContactList.fieldNames[FrmContactList.FRM_FIELD_NATIONALITY], "formElemen", null,""+contactList.getNationality(), nation_value, nation_key, "")%>
                                    </td>
                                </tr>
                                <tr id="pekerjaan" align="left" valign="top">
                                    <td height="9" valign="top" width="18%">Pekerjaan</td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_MEMBER_OCCUPATION] %>"  value="<%= contactList.getMemberOccupation() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_MEMBER_OCCUPATION%>')">
                                <tr id="email1" align="left" valign="top">
                                    <td height="9" valign="top" width="18%">Email 1</td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_HOME_EMAIL] %>"  value="<%= contactList.getHomeEmail() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_HOME_EMAIL%>')">
                                      
                                <tr id="email2" align="left" valign="top">
                                    <td height="9" valign="top" width="18%">Email 2</td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_EMAIL] %>"  value="<%= contactList.getEmail() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_EMAIL%>')">
                                      
                                  <tr id="no_id" align="left" valign="top">
                                    <td height="9" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][10]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_MEMBER_ID_CARD_NUMBER] %>"  value="<%= contactList.getMemberIdCardNumber() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_MEMBER_ID_CARD_NUMBER%>')">
                                   <tr><td height="21" width="20" valign="right" colspan="3" class="comment"></td></tr>
                                  </tr>
                                        </table>
                                      <br>
                                   <td valign="top" width="80%">       
                                <table width="70%" border="0" cellspacing="1" cellpadding="1">   
                                    <tr id="data_agen">
                                        <td class="mainheader" colspan="5"><u>Data Perusahaan</u></td>
                                    </tr>  
                                    <tr align="left"> 
                                     <td width="9%"><%=textListTitleHeader[SESS_LANGUAGE][1]%></td>
                                     <td width="4%">:</td>
                                     <td colspan="3">  
                                        <input type="hidden" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_CONTACT_TYPE]%>" value="<%=PstContactList.EXT_COMPANY%>">
                                        <%
                                            //added by dewok 2018-04-25 for litama
                                            //-----kode otomatis
                                            if (iCommand == Command.ADD && typeOfBusinessDetail == 2) {
                                                String format = "00";
                                                int count = 1 + SessMemberReg.getCountSupplier(null);
                                                try {            
                                                    String kode = format.substring(0, format.length() - String.valueOf(count).length()) + count;
                                                    contactList.setContactCode(""+kode);
                                                } catch (Exception e) {
                                                    contactList.setContactCode(""+count);
                                                }                                                
                                            }
                                            //-----end of kode otomatis
                                        %>
                                        <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_CONTACT_CODE]%>" value="<%=cekNull(contactList.getContactCode())%>" class="formElemen" size="10" onKeyUp="javascript:changeFocus(this,event)">
                                        * <%=frmContactList.getErrorMsg(FrmContactList.FRM_FIELD_CONTACT_CODE)%></td>
                                    </tr>
                                    <tr align="left"> 
                                     <td width="9%"><%=textListTitleHeader[SESS_LANGUAGE][2]%></td>
                                     <td width="4%">:</td>
                                     <td colspan="3">       
                                        <%for(int i=0; i < cntClass.size(); i++){ 
                                                                                            ContactClass contactClass = (ContactClass)cntClass.get(i);
                                                                                    %>
                                        <input type="checkbox" name="contact_<%=i%>" <%if(getStatus(contactClass.getOID(), classAssign, iCommand)){%>checked<%}%> value="<%=contactClass.getOID()%>">
                                        <%=contactClass.getClassName()%>&nbsp; 
                                        <%}%>
                                      </td>
                                    </tr>   
                                   <tr id="company">
                                    <td width="9%"><%=textListHeader[SESS_LANGUAGE][12]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                      <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_COMP_NAME]%>"  value="<%= contactList.getCompName() %>" class="formElemen" size="30" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_COMP_NAME%>')">
                                    </td>
                                  </tr>
                                  <tr id="country">
                                    <td width="9%"><%=textListHeader[SESS_LANGUAGE][29]%></td>
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
                                        /*if(contactList.getCountry()!=""|| contactList.getCountry()!=null){
                                            country=  contactList.getCountry();
                                                                                                                                        }*/
                                                                                                                        %>
                                        <%= ControlCombo.draw(FrmContactList.fieldNames[FrmContactList.FRM_FIELD_COUNTRY], "formElemen", null,contactList.getCountry(), nega_value, nega_key, "")%>
                                        <%--<input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_COUNTRY]%>"  value="<%= contactList.getCountry()%>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_COUNTRY%>')">
                                        --%>
                                        </td>
                                    </tr>
                                  <tr id="Town">
                                    <td width="9%">Provinsi</td>
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
                                        /*if(contactList.getProvince()!=""|| contactList.getProvince()!=null){
                                            province= contactList.getProvince();
                                                                                                                              }*/
                                                                                                                        %>
                                        <%= ControlCombo.draw(FrmContactList.fieldNames[FrmContactList.FRM_FIELD_PROVINCE], "formElemen", null, "" +contactList.getProvince(), prov_value, prov_key, "")%>
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
                                            String strWhereKabb =  "";//"prov."+PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI] + "='"+province+"'";//PstKabupaten.fieldNames[PstKabupaten.FLD_ID_PROPINSI] + "='"+contactList.getProvince()+"'";
                                            Vector listKabb = PstKabupaten.list(0, 0, strWhereKabb, "NAMA_KAB");
                                            for (int i = 0; i < listKabb.size(); i++) {
                                            Kabupaten town = (Kabupaten) listKabb.get(i);
                                            kabb_key.add(town.getNmKabupaten());
                                            kabb_value.add(town.getNmKabupaten());
                                            }
                                        %>
                                        <%= ControlCombo.draw(FrmContactList.fieldNames[FrmContactList.FRM_FIELD_TOWN], "formElemen", null,contactList.getTown() , kabb_value, kabb_key,"")%>
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
                                        String strWhereKac=  "";//"prov."+PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI] + "='"+province+"'";//PstKabupaten.fieldNames[PstKabupaten.FLD_ID_PROPINSI] + "='"+contactList.getProvince()+"'";
                                        Vector listKac = PstKecamatan.list(0, 0, strWhereKac, "");
                                        for (int i = 0; i < listKac.size(); i++) {
                                            Kecamatan kac = (Kecamatan) listKac.get(i);
                                            kac_key.add(kac.getNmKecamatan());
                                            kac_value.add(kac.getNmKecamatan());
                                                                                                                                }
                                        %><%= ControlCombo.draw(FrmContactList.fieldNames[FrmContactList.FRM_FIELD_COMP_STATE], "formElemen", null,contactList.getCompState() , kac_value, kac_key,"")%>
                                    </td>
                                  </tr>
                                  <tr id="address">
                                    <td width="31%" valign="top"><%=textListHeader[SESS_LANGUAGE][4]%></td>
                                    <td width="4%" valign="top">:</td>
                                    <td colspan="3">
                                      <textarea name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_BUSS_ADDRESS]%>" class="formElemen" cols="50" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_BUSS_ADDRESS%>')"><%= contactList.getBussAddress() %></textarea>
                                    </td>
                                  </tr>
                                  <tr id="address">
                                    <td width="31%" valign="top">Email</td>
                                    <td width="4%" valign="top">:</td>
                                    <td colspan="3">
                                      <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_COMP_EMAIL]%>"  value="<%= contactList.getCompEmail()%>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_COMP_EMAIL%>')">
                                    </td>
                                  </tr>
                                  <tr id="telp">
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][5]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                      <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_TELP_NR]%>"  value="<%= contactList.getTelpNr() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_TELP_NR%>')">
                                    </td>
                                  </tr>
                                  <tr id="Fax">
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][26]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                      <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_FAX]%>"  value="<%= contactList.getFax() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_FAX%>')">
                                    </td>
                                  </tr>
                                  <tr id="zip">
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][30]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                      <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_POSTAL_CODE]%>"  value="<%=contactList.getPostalCode()%>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_POSTAL_CODE%>')">
                                    </td>
                                  </tr> 
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
                                    <%=ControlCombo.draw(FrmContactList.fieldNames[FrmContactList.FRM_FIELD_CURRENCY_TYPE_ID_MEMBER_CONSIGMENT_LIMIT],"formElemen", "--select--", ""+contactList.getCurrencyTypeIdMemberConsigmentLimit(), vectCurrVal, vectCurrKey, "")%>
                                    </td>
                                    <td><input type="text"  class="formElemen" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_MEMBER_CONSIGMENT_LIMIT]%>" value="<%=FRMHandler.userFormatStringDecimal( contactList.getMemberConsigmentLimit()) %>"  class="formElemen" size="15" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_MEMBER_CONSIGMENT_LIMIT%>')">
                                    </td>
                                  </tr>
                                  <tr id="credit_limit">
                                    <td width="31%"><%=textListHeader[SESS_LANGUAGE][11]%></td>
                                    <td width="4%">:</td>
                                    <td valign="top">
                                    <%=ControlCombo.draw(FrmContactList.fieldNames[FrmContactList.FRM_FIELD_CURRENCY_TYPE_ID_MEMBER_CREDIT_LIMIT],"formElemen", "--select--", ""+contactList.getCurrencyTypeIdMemberCreditLimit(), vectCurrVal, vectCurrKey, "")%>
                                    </td>
                                    <td colspan="0">
                                      <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_MEMBER_CREDIT_LIMIT]%>"  value="<%= FRMHandler.userFormatStringDecimal(contactList.getMemberCreditLimit())%>" class="formElemen" size="30" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_MEMBER_CREDIT_LIMIT%>')">
                                    </td>
                                  </tr>
                                   <tr align="left">
                                    <td width="31%"><%=textListTitleHeader[SESS_LANGUAGE][14]%></td>
                                    <td width="4%">:</td>
                                    <td colspan="3">
                                        <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_TERM_OF_DELIVERY]%>" value="<%=contactList.getTermOfDelivery()%>" class="formElemen" size="30" onKeyUp="javascript:changeFocus(this,event)">
                                        <%=frmContactList.getErrorMsg(FrmContactList.FRM_FIELD_TERM_OF_DELIVERY)%></td>
                                    </tr>
                                   <tr id="day_term_of_payment" align="left" valign="top">
                                    <td height="21" valign="top" width="18%"><%=textListHeader[SESS_LANGUAGE][32]%></td>
                                    <td height="21" valign="top">:</td>
                                    <td height="21" colspan="3">
                                      <input type="text" name="<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_DAY_OF_PAYMENT] %>"  value="<%= contactList.getDayOfPayment() %>" class="formElemen" onKeyDown="javascript:cmdEnter('<%=FrmContactList.FRM_FIELD_DAY_OF_PAYMENT%>')"> <%=textListHeader[SESS_LANGUAGE][33]%>
                                   </tr>
                                   <tr align="left">
                                      <td height="21" valign="top" width="18%">Location Intern</td>
                                      <td height="21" valign="top">:</td>
                                      <td height="21" colspan="3">
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
                                        String select_loc = ""+contactList.getLocationId();
                                        %>  
                                        <%=ControlCombo.draw(frmContactList.fieldNames[frmContactList.FRM_FIELD_LOCATION_ID],null,select_loc,val_terms,key_terms,"","formElemen")%>
                                        <%=frmContactList.getErrorMsg(FrmContactList.FRM_FIELD_LOCATION_ID)%>
                                      </td>
                                    </tr>
                                </table>
                              <% 
                                String whHistory = PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_MEMBER_ID]+" = "+oidContactList;
                                String ordHistory = PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_REGISTRATION_DATE]+" DESC, "+
                                PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_VALID_START_DATE]+" DESC, "+
                                PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_VALID_EXPIRED_DATE]+" DESC ";
                                Vector listHistory = PstMemberRegistrationHistory.list(0,1,whHistory,ordHistory);
                              %>
                                <%
                                if(oidContactList!=0)
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
                                          <td height="22" valign="middle" width="5%"><a href="javascript:cmdAddHistory('<%=oidContactList%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1001','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image1001" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=textListHeader[SESS_LANGUAGE][15]%>"></a></td>
                                          <td height="22" valign="middle" width="95%"><a href="javascript:cmdAddHistory('<%=oidContactList%>')"><%=textListHeader[SESS_LANGUAGE][15]%></a></td>
                                        </tr>
                                      </table>
                                    </td>
                                  </tr>
                                  <%if(listHistory.size()==1){%>
                                      <tr>
                                        <td class="mainheader" colspan="5"><u><%=textListTitleItem[SESS_LANGUAGE][0]%></u> </td>
                                      </tr>
                                      <tr>
                                          <%--list barang--%>
                                          <%
                                          Vector listMatVendorPrice = new Vector(1,1);
                                          whereClause = ""+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID]+"="+oidContactList;
                                          listMatVendorPrice = PstMatVendorPrice.list(0,0, whereClause , "");
                                           %>
                                           <td valign="middle" colspan="3"> <%= drawList(iCommand,listMatVendorPrice,-1,SESS_LANGUAGE)%> </td>
                                      </tr>
                                      <%if(iCommand==Command.DETAIL){%>
                                            <% MatVendorPrice matVendorPrice = new MatVendorPrice();   
                                                Material material = new Material();
                                            %>
                                            <tr align="left" valign="top">
                                              <td height="22" colspan="3">
                                                <table width="100%" border="0" cellspacing="1" cellpadding="1">
                                                  <tr>
                                                    <td width="9%">&nbsp;</td>
                                                    <td width="1%">&nbsp;</td>
                                                    <td width="37%">&nbsp;</td>
                                                    <td width="3%">&nbsp;</td>
                                                    <td width="12%">&nbsp;</td>
                                                    <td width="1%">&nbsp;</td>
                                                    <td width="37%">&nbsp;</td>
                                                  </tr>
                                                  <tr>
                                                    <td width="9%"> <div align="left"><%=textListTitleItem[SESS_LANGUAGE][5]%></div></td>
                                                    <td width="1%">:</td>
                                                    <td width="37%"> <input type="text" size="30" readonly name="txt_vendorname" value="<%=PstMatVendorPrice.getVendorName(matVendorPrice.getVendorId())%>" class="formElemen">
                                                      <a href="javascript:check()">CHK</a></td>
                                                    <td width="3%">&nbsp;</td>
                                                    <td width="12%"> <div align="left"><%=textListTitleItem[SESS_LANGUAGE][17]%></div></td>
                                                    <td width="1%">:</td>
                                                    <td width="37%"> <%
                                                        String whereCurr  = PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE;
                                                        listCurr = PstCurrencyType.list(0,0,whereCurr,"");
                                                        vectCurrVal = new Vector(1,1);
                                                        vectCurrKey = new Vector(1,1);
                                                        for(int i=0; i<listCurr.size(); i++){
                                                            CurrencyType currencyType = (CurrencyType)listCurr.get(i);
                                                            vectCurrKey.add(currencyType.getCode());
                                                            vectCurrVal.add(""+currencyType.getOID());
                                                        }
                                                      %> <%=ControlCombo.draw(FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_PRICE_CURRENCY],"formElemen", null, ""+matVendorPrice.getPriceCurrency(), vectCurrVal, vectCurrKey, "")%> </td>
                                                  </tr>
                                                  <tr>
                                                    <td width="9%"><%=textListTitleHeader[SESS_LANGUAGE][6]%></td>
                                                    <td width="1%">:</td>
                                                    <td width="37%"> <input tabindex="1" type="text" size="20" name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_VENDOR_PRICE_CODE]%>" value="<%=matVendorPrice.getVendorPriceCode()%>" class="formElemen">                        </td>
                                                    <td width="3%">&nbsp;</td>
                                                    <td width="12%"><div align="left"><%=textListTitleHeader[SESS_LANGUAGE][7]%></div></td>
                                                    <td>:</td>
                                                    <td> <%
                                                        Unit firstUnit = new Unit();
                                                        try{
                                                            firstUnit = PstUnit.fetchExc(material.getDefaultStockUnitId());
                                                        }catch(Exception e){}

                                                        whereClause = PstUnit.fieldNames[PstUnit.FLD_BASE_UNIT_ID]+"="+material.getDefaultStockUnitId();
                                                        Vector listUnit = PstUnit.list(0,0,whereClause,"");
                                                        Vector vectUnitVal = new Vector(1,1);
                                                        Vector vectUnitKey = new Vector(1,1);

                                                        vectUnitKey.add(firstUnit.getCode());
                                                        vectUnitVal.add(""+firstUnit.getOID());
                                                        for(int i=0; i<listUnit.size(); i++)
                                                        {
                                                            Unit mateUnit = (Unit)listUnit.get(i);
                                                            vectUnitKey.add(mateUnit.getCode());
                                                            vectUnitVal.add(""+mateUnit.getOID());
                                                        }
                                                      %> <%=ControlCombo.draw(FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_BUYING_UNIT_ID],"formElemen", null, ""+matVendorPrice.getBuyingUnitId(), vectUnitVal, vectUnitKey, " tabindex=\"4\"")%></td>
                                                  </tr>
                                                  <tr>

                                                    <td width="9%"> <div align="left"><%=textListTitleItem[SESS_LANGUAGE][9]%></div></td>
                                                    <td width="1%">:</td>
                                                    <td width="37%"> <input tabindex="2" type="text" size="20" name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_VENDOR_PRICE_BARCODE]%>" value="<%=matVendorPrice.getVendorPriceBarcode()%>" class="formElemen">                        </td>
                                                    <td width="3%">&nbsp;</td>
                                                    <td width="12%"> <%=textListTitleItem[SESS_LANGUAGE][8]%> </td>
                                                    <td>:</td>
                                                    <td> <input tabindex="5" type="text" size="15"  name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_ORG_BUYING_PRICE]%>" value="<%=FRMHandler.userFormatStringDecimal(matVendorPrice.getOrgBuyingPrice())%>" class="formElemen" onKeyUp="javascript:calculate1()" style="text-align:right">                        </td>
                                                  </tr>
                                                  <tr>
                                                    <td>&nbsp;</td>
                                                    <td valign="top">&nbsp;</td>
                                                    <td width="37%" rowspan="5" valign="top"> <textarea tabindex="3" cols="30" name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_DESCRIPTION]%>" class="formElemen" rows="3"><%=matVendorPrice.getDescription()%></textarea>                        </td>
                                                    <td>&nbsp;</td>
                                                    <td><%=textListTitleItem[SESS_LANGUAGE][18]%></td>
                                                    <td>:</td>
                                                    <td><input tabindex="6" type="text" size="15"  name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_COST_CARGO]%>" value="<%=FRMHandler.userFormatStringDecimal(matVendorPrice.getLastCostCargo())%>" class="formElemen" onKeyUp="javascript:calculate1()" style="text-align:right"></td>
                                                  </tr>
                                                  <tr>
                                                    <td width="9%"> <div align="left"><%=textListTitleItem[SESS_LANGUAGE][11]%></div></td>
                                                    <td width="1%" valign="top">:</td>
                                                    <td width="3%">&nbsp;</td>
                                                    <td width="12%"> <div align="left"><%=textListTitleHeader[SESS_LANGUAGE][19]%></div></td>
                                                    <td>:</td>

                                                    <td> <input tabindex="6" type="text" size="7"  name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_DISCOUNT_1]%>" value="<%=FRMHandler.userFormatStringDecimal(matVendorPrice.getLastDiscount1())%>" class="formElemen" onKeyUp="javascript:calculate1()" style="text-align:right">
                                                      % </td>
                                                  </tr>
                                                  <tr>
                                                    <td width="9%"> <div align="left"></div></td>
                                                    <td width="1%" valign="top">&nbsp;</td>
                                                    <td width="3%">&nbsp;</td>
                                                    <td width="12%"><div align="left"><%=textListTitleItem[SESS_LANGUAGE][20]%></div></td>
                                                    <td>:</td>
                                                    <td> <input tabindex="7" type="text" size="7"  name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_DISCOUNT_2]%>" value="<%=FRMHandler.userFormatStringDecimal(matVendorPrice.getLastDiscount2())%>" class="formElemen" onKeyUp="javascript:calculate1()" style="text-align:right">
                                                      % </td>
                                                  </tr>
                                                  <tr>
                                                    <td>&nbsp;</td>
                                                    <td valign="top">&nbsp;</td>
                                                    <td>&nbsp;</td>
                                                    <td><div align="left"><%=textListTitleItem[SESS_LANGUAGE][10]%></div></td>
                                                    <td>:</td>
                                                    <td> <input tabindex="8" type="text" size="15"  name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_DISCOUNT]%>" value="<%=FRMHandler.userFormatStringDecimal(matVendorPrice.getLastDiscount())%>" class="formElemen" onKeyUp="javascript:calculate1()" style="text-align:right"></td>
                                                  </tr>
                                                  <tr>
                                                    <td>&nbsp;</td>
                                                    <td valign="top">&nbsp;</td>
                                                    <td>&nbsp;</td>
                                                    <td><div align="left"><%=textListTitleItem[SESS_LANGUAGE][12]%></div></td>
                                                    <td>:</td>
                                                    <td><input tabindex="8" type="text" size="15"  name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_VAT]%>" value="<%=FRMHandler.userFormatStringDecimal(matVendorPrice.getLastVat())%>" class="formElemen" onKeyUp="javascript:calculate1()" style="text-align:right"></td>
                                                  </tr>
                                                                      <tr>
                                                    <td>&nbsp;</td>
                                                    <td valign="top">&nbsp;</td>
                                                    <td>&nbsp;</td>
                                                    <td></td>
                                                    <td><%=textListTitleHeader[SESS_LANGUAGE][13]%></td>
                                                    <td>:</td>
                                                                            <td><input tabindex="8" type="text" size="15"  name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_CURR_BUYING_PRICE]%>" value="<%=FRMHandler.userFormatStringDecimal(matVendorPrice.getCurrBuyingPrice())%>" class="formElemen" style="text-align:right">
                                                                            (* automatic <a tabindex="9" href="javascript:calculate1()">calculate</a>
                                                      )</td>
                                                  </tr>
                                                                      <tr>
                                                    <td>&nbsp;</td>
                                                    <td valign="top">&nbsp;</td>
                                                    <td>&nbsp;</td>
                                                    <td>&nbsp;</td>
                                                    <td>&nbsp;</td>
                                                    <td>&nbsp;</td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                            <%}else{%>
                                                 <tr>
                                                    <td width="31%">&nbsp;</td>
                                                    <td width="4%">&nbsp;</td>
                                                    <td colspan="3" valign="top">
                                                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                        <tr align="left" valign="top">
                                                          <td height="22" valign="middle" width="5%"><a href="javascript:cmdAddPriceList('<%=oidContactList%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1001','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image1001" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=textListHeader[SESS_LANGUAGE][15]%>"></a></td>
                                                          <td height="22" valign="middle" width="95%"><a href="javascript:cmdAddPriceList('<%=oidContactList%>')">Contract</a></td>
                                                        </tr>
                                                      </table>
                                                    </td>
                                                  </tr>
                                            <%}%>
                                    <%}%>
                                </table>      
                            <%}%>
                        </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <table width="100%" cellspacing="1" cellpadding="1" >
                
                <tr align="left"> 
                  <td width="1%"  valign="top"  >&nbsp;</td>
                  <td colspan="3"  valign="top"  > 
                    <%
					ctrLine.setLocationImg(approot+"/images");
					
					// set image alternative caption
					ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,companyTitle,ctrLine.CMD_SAVE,true));
					ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,companyTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,companyTitle,ctrLine.CMD_BACK,true)+" List");							
					ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,companyTitle,ctrLine.CMD_ASK,true));							
					ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,companyTitle,ctrLine.CMD_CANCEL,false));														
					
					ctrLine.initDefault(SESS_LANGUAGE,companyTitle);
					ctrLine.setTableWidth("80%");
					String scomDel = "javascript:cmdAsk('"+oidContactList+"')";
					String sconDelCom = "javascript:cmdConfirmDelete('"+oidContactList+"')";
					String scancel = "javascript:cmdEdit('"+oidContactList+"')";
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
					//ctrLine.setAddCaption("");

                                        if(  sourceLink!=null && sourceLink.equals("materialdosearch.jsp")){
                                              ctrLine.setBackCaption("");
                                            }

				  %>

                    <%if(iCommand==Command.ADD || iCommand==Command.EDIT || iCommand==Command.ASK || iCommand==Command.SAVE || ((iCommand==Command.DELETE)&&iErrCode!=FRMMessage.NONE)){%>
                       <%=ctrLine.drawImage(iCommand, iErrCode, msgString)%>
                    <%}%>
                  </td>
                </tr>
                <tr align="left">
                  <!--<td colspan="4"><a href="javascript:setSupplierOnLGR();">Set Supplier on LGR</a>&nbsp;
                     <% if(  iCommand==Command.SAVE && (frmContactList.errorSize()<1) &&  sourceLink!=null && sourceLink.equals("materialdosearch.jsp")){ %>
                       test 
                    <%}%>
                  </td>-->
                </tr>
              </table>
            </form>
            <% if( iCommand==Command.SAVE && (frmContactList.errorSize()<1) &&  sourceLink!=null && sourceLink.equals("materialdosearch.jsp")){ %>
                &nbsp;

                    <%} else {%>
            <script language="JavaScript">
                document.frm_contactlist.<%=FrmContactList.fieldNames[FrmContactList.FRM_FIELD_CONTACT_CODE]%>.focus();
            </script>
            <%}%>
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
