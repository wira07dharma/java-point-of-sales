<%-- 
    Document   : list_pos_protection
    Created on : Jun 10, 2014, 2:28:14 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.form.arap.FrmAccPayableDetail"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.common.entity.contact.PstContactClass"%>
<%@page import="com.dimata.posbo.entity.warehouse.PriceProtection"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstPriceProtection"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmPriceProtection"%>
<%@page import="com.dimata.posbo.entity.search.SrcPriceProtection"%>

<%-- 
    Document   : src_rekap_payment
    Created on : Mar 10, 2014, 5:09:42 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.common.entity.contact.PstContactClass"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmPriceProtectionDistribution"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlPriceProtectionDistribution"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmPriceProtectionItem"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlPriceProtectionItem"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmPriceProtection"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlPriceProtection"%>

<%@page import="com.dimata.pos.entity.payment.CashCreditPayments"%>
<%@page import="com.dimata.pos.entity.payment.PstCashCreditPayment"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page import="com.dimata.qdep.form.FRMQueryString,
                 com.dimata.posbo.entity.search.SrcSaleReport,
                 com.dimata.posbo.form.search.FrmSrcSaleReport,
                 com.dimata.posbo.report.sale.SaleReportDocument,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlDate,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.posbo.entity.warehouse.*,
                 com.dimata.common.entity.payment.PstCurrencyType,
                 com.dimata.common.entity.payment.CurrencyType,
                 com.dimata.gui.jsp.ControlCombo"%>
<%@ page language = "java" %>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_AR, AppObjInfo.OBJ_PAYMENT); %>
<%@ include file = "../../main/checkuser.jsp" %>



<%!

public static final String textListHeader[][] = 
{
	{"Lokasi","Shift","Kasir","Supplier","Kategori","Merk","Tanggal","Urut Berdasar","Kode Sales","Sub Kategori","Tipe Transaksi","Dikelompokkan berdasar","Mata Uang"},
	{"Location","Shift","Cashier","Supplier","Category","Mark","Sale Date","Sort By","Sales Code","Sub Category","Transaction Type","Grouped by","Currency"}	
};

public static final String textListTitleHeader[][] =   
{
	{"Laporan Transaksi","Laporan Pelunasan Piutang","Pencarian","Cari Laporan","Update PP"},
	{"Transaction Report","Receivable Payment","Searching","Report Search","Update PP"}   	
};

public static final String textHeaderPayment[][] =   
{
	{"NO","Date","Nomor PP","Tanggal Pembuatan","Amount","Status","Tanggal Update","Approve","Paid"},
	{"NO","Date","Number PP","Tanggal Pembuatan","Amount","Status","Tanggal Update","Approve","Paid"}
};


public static final String textListOrderItem[][] =
{
	{"No","Kode","Nama Supplier","Rate","Amount","Status","Action"},//10
	{"No","Code","Vendor Name","Rate","Amount","Status","Action"}
};

public String drawList(int language,Vector objectClass, int start) {
    
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        
        ctrlist.addHeader(textListOrderItem[language][0],"3%");//0
	ctrlist.addHeader(textListOrderItem[language][2],"30%");//1
	ctrlist.addHeader(textListOrderItem[language][3],"17%");//2
        ctrlist.addHeader(textListOrderItem[language][4],"15%");//3
        ctrlist.addHeader(textListOrderItem[language][5],"20%");//4
        
        ctrlist.setLinkRow(1);
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        Vector rowx = new Vector(1, 1);
        ctrlist.reset();
        int index = -1;
        if (start < 0) {
            start = 0;
        }

        for (int i = 0; i < objectClass.size(); i++) {
            
            Vector temp = (Vector)objectClass.get(i);
            PriceProtectionDistribution priceProtectionDistribution = (PriceProtectionDistribution)temp.get(0);
            ContactList contactList = (ContactList)temp.get(1);
            
            start = start + 1;
            rowx = new Vector();
            rowx.add(""+start+"");
            rowx.add("<a href=\"javascript:cmdEdit('"+priceProtectionDistribution.getOID()+"','','"+priceProtectionDistribution.getAmountIssue()+"')\">"+contactList.getCompName());
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(priceProtectionDistribution.getExchangeRate())+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(priceProtectionDistribution.getAmountIssue())+"</div>");

            if(priceProtectionDistribution.getStatus()==0){
                rowx.add("<div align=\"center\">Draft</div>");
            }else{
                rowx.add("<div align=\"center\">Closed</div>");
            }
            
            lstData.add(rowx);
        }
        
        return ctrlist.draw();
}



public String getJspTitle(int index, int language, String prefiks, boolean addBody)
{
	String result = "";
	if(addBody)
	{
		if(language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){	
			result = textListHeader[language][index] + " " + prefiks;
		}
		else
		{
			result = prefiks + " " + textListHeader[language][index];		
		}
	}
	else
	{
		result = textListHeader[language][index];
	} 
	return result;
}

%>


<!-- Jsp Block -->
<%
/**
* get data from 'hidden form'
*/
int iCommand = FRMQueryString.requestCommand(request);
long oidSupplierID =  FRMQueryString.requestLong(request,"supplierId");

/*
SrcPriceProtection srcPriceProtection = new SrcPriceProtection(); 
FrmPriceProtection frmPriceProtection = new FrmPriceProtection(request, srcPriceProtection);
frmPriceProtection.requestEntity(srcPriceProtection);

Vector records= new Vector();
String where=" STATUS='2'";

if(oidSupplierID!=0){
    where=where+" AND SUPPLIER_ID='"+oidSupplierID+"'";
}

records = PstPriceProtection.list(0, 0, where, "");*/
String whereClousex="PPPI."+PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_SUPPLIER_ID] + 
                    " = " + oidSupplierID+" AND PPPI.STATUS=0 ";

Vector listPriceProtectionDistribution = PstPriceProtectionDistribution.listInnerJoinSupplier(0,0,whereClousex,PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_POS_PRICE_PROTECTION_DISTRIBUTION_ID]);


%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function cmdSearch(){
	document.frmsrcreportsale.command.value="<%=Command.LIST%>";
	document.frmsrcreportsale.action="search_payment_pp.jsp";
	document.frmsrcreportsale.submit();
}


function cmdUpdate(){
	document.frmsrcreportsale.command.value="<%=Command.UPDATE%>";
	document.frmsrcreportsale.action="search_payment_pp.jsp";
	document.frmsrcreportsale.submit();
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
    
function cmdEdit(matOid,matCode, paid){
    
    self.opener.document.forms.frap.hidden_oid_pp.value = matOid;
    self.opener.document.forms.frap.<%=FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_AMOUNT]%>.value = paid;
    self.opener.document.forms.frap.amount.value = paid;
    self.close();	
    
}    
    
    
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
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->&nbsp;Pembelian > List Price Protection<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmsrcreportsale" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">           
                  <table width="100%" border="0">
                    <tr> 
                      <td> 
                          <%=drawList(SESS_LANGUAGE,listPriceProtectionDistribution, 0)%>
                      </td>
                    </tr>
                  </table>
            </form>
            <SCRIPT language=JavaScript>
<!--
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
            <!-- #EndEditable --></td> 
        </tr> 
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

