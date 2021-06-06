<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@page import="com.dimata.posbo.entity.admin.MappingUserGroup"%>
<%@page import="com.dimata.posbo.entity.admin.PstMappingUserGroup"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page import="com.dimata.posbo.entity.search.SrcSaleReport,
                 com.dimata.posbo.form.search.FrmSrcSaleReport,
                 com.dimata.posbo.report.sale.SaleReportDocument,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.common.entity.contact.PstContactClass,
                 com.dimata.common.entity.contact.ContactClass,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlDate"%>
<%@ page language = "java" %>
<%@ page import = "com.dimata.posbo.session.masterdata.*" %>

<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<!-- package java -->
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_SALES_DETAIL); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */

public static int FLD_LOCATION =0;
public static int FLD_SHIFT =1;
public static int FLD_CASHIER =2;
public static int FLD_SUPPLIER =3;
public static int FLD_CATEGORY =4;
public static int FLD_MERK =5;
public static int FLD_TANGGAL =6;
public static int FLD_SORT_BY =7;
public static int FLD_SALES =8;
public static int FLD_SUB_CATEGORY =9;
public static int FLD_TRANSACTION_TYPE =10;
public static int FLD_GROUPED_BY=11;
public static int FLD_VIEW_PHOTO=12;
public static int FLD_URUTKAN=16;


public static final String textListHeader[][] = {
    {"Lokasi","Shift","Kasir","Supplier","Kategori","Merk",
     "Periode","Urut Berdasar","Nama Sales","Sub Kategori",
     "Tipe Transaksi","Grup","Tampilkan Foto","Semua","Ya","Tidak","Urutkan Berdasarkan"},
    {"Location","Shift","Cashier","Supplier","Category","Mark",
     "Period","Sort By","Sales Person","Sub Category",
     "Transaction Type","Group","View Photo","All","Yes","No","Order By"}
};

public static final String textListTitleHeader[][] = {
    {"Laporan Penjualan","Laporan Penjualan Detail","Pencarian","Cari Laporan"},
    {"Sale Report","Detail Sale Report","Searching","Search Report"}
};

public String getJspTitle(int index, int language, String prefiks, boolean addBody) {
    String result = "";
    if(addBody) {
        if(language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){
            result = textListHeader[language][index] + " " + prefiks;
        } else {
            result = prefiks + " " + textListHeader[language][index];
        }
    } else {
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

ControlLine ctrLine = new ControlLine();

SrcSaleReport srcSaleReportDetail = new SrcSaleReport();
FrmSrcSaleReport frmSrcSaleReport = new FrmSrcSaleReport();

SaleReportDocument saleReportDocumentDetail = new SaleReportDocument();
try{
    srcSaleReportDetail  = (SrcSaleReport )session.getValue(SaleReportDocument.SALE_REPORT_DOC_DETAIL);
}catch(Exception e){
    srcSaleReportDetail  = new SrcSaleReport();
}

if(srcSaleReportDetail ==null){
    srcSaleReportDetail = new SrcSaleReport();
}
srcSaleReportDetail.setTransType(6);

try{
    session.removeValue(SaleReportDocument.SALE_REPORT_DOC_DETAIL);
}catch(Exception e){}
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--

function cmdSearch(){
    document.frmsrcreportsale.command.value="<%=Command.LIST%>";
    document.frmsrcreportsale.global_group.value=document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_GROUP_BY]%>.value;
    document.frmsrcreportsale.sort_method.value=<%=SrcSaleReport.SORT_BY_ITEM%>;//document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_GROUP_BY]%>.value;
    document.frmsrcreportsale.sort_desc.value="1";
    document.frmsrcreportsale.action="reportsale_list_detail_global.jsp";
    document.frmsrcreportsale.submit();
}

function cmdSwitchGroup(){
    var groupMeth = document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_GROUP_BY]%>.value;
    switch(groupMeth){
        case '<%=SrcSaleReport.GROUP_BY_CATEGORY%>':
            document.all.category_combo.style.display="";
            document.all.sales_combo.style.display="none";
            document.all.shift_combo.style.display="none";
            document.all.supplier_combo.style.display="none";
            document.all.merk_combo.style.display="none";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_CATEGORY_ID]%>.selectedIndex=0;
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SALES_PERSON_ID]%>.value="0";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SHIFT_ID]%>.value="0";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SUPPLIER_ID]%>.value="0";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_MARK_ID]%>.value="0";
            break;
        case '<%=SrcSaleReport.GROUP_BY_SALES%>':
            document.all.category_combo.style.display="none";
            document.all.sales_combo.style.display="";
            document.all.shift_combo.style.display="none";
            document.all.supplier_combo.style.display="none";
            document.all.merk_combo.style.display="none";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_CATEGORY_ID]%>.value="0";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SALES_PERSON_ID]%>.selectedIndex=0;
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SHIFT_ID]%>.value="0";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SUPPLIER_ID]%>.value="0";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_MARK_ID]%>.value="0";
            break;
        case '<%=SrcSaleReport.GROUP_BY_SHIFT%>':
            document.all.category_combo.style.display="none";
            document.all.sales_combo.style.display="none";
            document.all.shift_combo.style.display="";
            document.all.supplier_combo.style.display="none";
            document.all.merk_combo.style.display="none";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_CATEGORY_ID]%>.value="0";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SALES_PERSON_ID]%>.value="0";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SHIFT_ID]%>.selectedIndex=0;
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SUPPLIER_ID]%>.value="0";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_MARK_ID]%>.value="0";
            break;
        case '<%=SrcSaleReport.GROUP_BY_SUPPLIER%>':
            document.all.category_combo.style.display="none";
            document.all.sales_combo.style.display="none";
            document.all.shift_combo.style.display="none";
            document.all.supplier_combo.style.display="";
            document.all.merk_combo.style.display="none";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_CATEGORY_ID]%>.value="0";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SALES_PERSON_ID]%>.value="0";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SHIFT_ID]%>.value="0";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SUPPLIER_ID]%>.selectedIndex=0;
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_MARK_ID]%>.value="0";
            break;
        case '<%=SrcSaleReport.GROUP_BY_MARK%>':
            document.all.category_combo.style.display="none";
            document.all.sales_combo.style.display="none";
            document.all.shift_combo.style.display="none";
            document.all.supplier_combo.style.display="none";
            document.all.merk_combo.style.display="";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_CATEGORY_ID]%>.values="0";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SALES_PERSON_ID]%>.value="0";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SHIFT_ID]%>.value="0";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SUPPLIER_ID]%>.value="0";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_MARK_ID]%>.selectedIndex=0;
            break;
        default:
            document.all.category_combo.style.display="";
            document.all.sales_combo.style.display="none";
            document.all.shift_combo.style.display="none";
            document.all.supplier_combo.style.display="none";
            document.all.merk_combo.style.display="none";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_CATEGORY_ID]%>.selectedIndex=0;
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SALES_PERSON_ID]%>.value="0";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SHIFT_ID]%>.value="0";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SUPPLIER_ID]%>.value="0";
            document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_MARK_ID]%>.value="0";
            break;
    }
    
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
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css"
rel="stylesheet" />
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --><%=textListTitleHeader[SESS_LANGUAGE][0]%> &gt; <%=textListTitleHeader[SESS_LANGUAGE][1]%> &gt; <%=textListTitleHeader[SESS_LANGUAGE][2]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frmsrcreportsale" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="global_group" value="0">
              <input type="hidden" name="sort_method" value="0">
              <input type="hidden" name="sort_desc" value="0">
              <table width="100%" border="0">
                <tr>
                  <td colspan="2">
                    <hr size="1">
                  </td>
                </tr>
                <tr>
                  <td colspan="2">
                    <table width="100%" border="0" cellspacing="3" cellpadding="1">
                      <tr> 
                        <td height="21"><%=getJspTitle(FLD_TANGGAL,SESS_LANGUAGE,"",true)%></td>
                        <td height="21">:</td>
                        <td height="21">&nbsp; <%=ControlDate.drawDate(frmSrcSaleReport.fieldNames[frmSrcSaleReport.FRM_FLD_FROM_DATE],srcSaleReportDetail.getDateFrom(),"formElemen",4,-8)%> &nbsp;s/d &nbsp; <%=ControlDate.drawDate(frmSrcSaleReport.fieldNames[frmSrcSaleReport.FRM_FLD_TO_DATE],srcSaleReportDetail.getDateTo(),"formElemen",4,-8)%></td>
                      </tr>
                      <tr>
                        <td height="21" width="14%"><%=getJspTitle(FLD_LOCATION,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" width="0%">:</td>
                        <td height="21" width="86%">&nbsp;
                        <%
                         Vector val_locationid = new Vector(1,1);
                         Vector key_locationid = new Vector(1,1);
                         //Vector vt_loc = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE,PstLocation.fieldNames[PstLocation.FLD_NAME]);
                         boolean checkDataAllLocReportView = PstDataCustom.checkDataAllLocReportView(userId, "user_view_sale_stock_report_location","0");
                         String whereLocViewReport = PstDataCustom.whereLocReportView(userId, "user_view_sale_stock_report_location","");
                         Vector vt_loc = PstLocation.listLocationStore(0,0,whereLocViewReport,PstLocation.fieldNames[PstLocation.FLD_NAME]);
                         if(checkDataAllLocReportView){
                            val_locationid.add("0");
                            key_locationid.add(textListHeader[SESS_LANGUAGE][13]+" "+textListHeader[SESS_LANGUAGE][0]);
                         }
                         for(int d=0;d<vt_loc.size();d++) {
                             Location loc = (Location)vt_loc.get(d);
                             val_locationid.add(""+loc.getOID()+"");
                             key_locationid.add(loc.getName());
                         }
                         %> <%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_LOCATION_ID], null, ""+srcSaleReportDetail.getLocationId(), val_locationid, key_locationid, "", "formElemen")%>
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="14%" align="left"><%=getJspTitle(FLD_TRANSACTION_TYPE,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" valign="top" width="0%" align="left">:</td>
                        <td height="21" width="86%" valign="top" align="left">&nbsp; 
                          <%
                          
                            Vector val_docid = new Vector(1,1);

                            Vector key_docid = new Vector(1,1);
                             val_docid.add(""+SrcSaleReport.TYPE_CASH_CREDIT+"");
                            key_docid.add(SrcSaleReport.reportType[SESS_LANGUAGE][SrcSaleReport.TYPE_CASH_CREDIT]);
                            val_docid.add(""+SrcSaleReport.TYPE_CASH+"");
                            key_docid.add(SrcSaleReport.reportType[SESS_LANGUAGE][SrcSaleReport.TYPE_CASH]);
                            val_docid.add(""+SrcSaleReport.TYPE_CREDIT+"");
                            key_docid.add(SrcSaleReport.reportType[SESS_LANGUAGE][SrcSaleReport.TYPE_CREDIT]);
                            val_docid.add(""+SrcSaleReport.TYPE_RETUR+"");
                            key_docid.add(SrcSaleReport.reportType[SESS_LANGUAGE][SrcSaleReport.TYPE_RETUR]);
                            val_docid.add(""+SrcSaleReport.TYPE_GIFT+"");
                            key_docid.add(SrcSaleReport.reportType[SESS_LANGUAGE][SrcSaleReport.TYPE_GIFT]);
                            String select_doc = "Invoice";
			  %> <%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_TIPE_TRANSAKSI], null, ""+srcSaleReportDetail.getTransType(), val_docid, key_docid, "", "formElemen")%>
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="14%" align="left"><%=getJspTitle(FLD_GROUPED_BY,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" valign="top" width="0%" align="left">:</td>
                        <td height="21" width="86%" valign="top" align="left">&nbsp; 
                         <%
                          Vector val_groupby= new Vector(1,1);
                          Vector key_groupby= new Vector(1,1);
                          val_groupby.add(""+SrcSaleReport.SORT_BY_ITEM+"");
                          key_groupby.add(SrcSaleReport.viewOrderBy[SESS_LANGUAGE][SrcSaleReport.SORT_BY_ITEM]);
                          val_groupby.add(""+SrcSaleReport.GROUP_BY_CATEGORY+"");
                          key_groupby.add(SrcSaleReport.groupMethod[SESS_LANGUAGE][SrcSaleReport.GROUP_BY_CATEGORY]);
                          val_groupby.add(""+SrcSaleReport.GROUP_BY_SALES+"");
                          key_groupby.add(SrcSaleReport.groupMethod[SESS_LANGUAGE][SrcSaleReport.GROUP_BY_SALES]);
                          val_groupby.add(""+SrcSaleReport.GROUP_BY_SHIFT+"");
                          key_groupby.add(SrcSaleReport.groupMethod[SESS_LANGUAGE][SrcSaleReport.GROUP_BY_SHIFT]);
                          val_groupby.add(""+SrcSaleReport.GROUP_BY_SUPPLIER+"");
                          key_groupby.add(SrcSaleReport.groupMethod[SESS_LANGUAGE][SrcSaleReport.GROUP_BY_SUPPLIER]);
                          //String default_groupby = SrcSaleReport.groupMethod[SESS_LANGUAGE][SrcSaleReport.GROUP_BY_CATEGORY];
                          String default_groupby = ""+srcSaleReportDetail.getGroupBy();
			 %> <%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_GROUP_BY], null, default_groupby, val_groupby, key_groupby, "onChange=\"javascript:cmdSwitchGroup()\"", "formElemen")%>
                        </td>
                      </tr>
                      <tr id="category_combo" style="display: none;"> 
                        <td height="21" width="14%"><%=getJspTitle(FLD_CATEGORY,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" width="0%">:</td>
                        <td height="21" width="86%">&nbsp;
                        <%
                         Vector val_categoryid = new Vector(1,1);
                         Vector key_categoryid = new Vector(1,1);
                         Vector vt_cat = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_NAME]);
                         val_categoryid.add("0");
                         key_categoryid.add(textListHeader[SESS_LANGUAGE][13]+" "+textListHeader[SESS_LANGUAGE][4]);
                         for(int d=0;d<vt_cat.size();d++) {
                             Category cat = (Category)vt_cat.get(d);
                             val_categoryid.add(""+cat.getOID()+"");
                             key_categoryid.add(cat.getName());
                         }
                         %> <%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_CATEGORY_ID], null, ""+srcSaleReportDetail.getCategoryId(), val_categoryid, key_categoryid, "", "formElemen")%>
                        </td>
                      </tr>
                      <tr id="sales_combo" style="display: none;">
                        <td height="21" width="14%"><%=getJspTitle(FLD_SALES,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" width="0%">:</td>
                        <td height="21" width="86%">&nbsp;
                        <%
                        Vector val_sales= new Vector(1,1);
                         Vector key_sales= new Vector(1,1);
      
                         String whereClause = PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_GROUP_USER_ID] + " = 6";
                          Vector sale = PstMappingUserGroup.list(0, 0, whereClause, "");
                          
                         val_sales.add("0");
                         key_sales.add("All Sales");
                         for(int d=0;d<sale.size();d++) {
                           MappingUserGroup mg = (MappingUserGroup) sale.get(d);
                           AppUser ap = new AppUser();
                           try {
                               ap = PstAppUser.fetch(mg.getUserId());;
                             } catch (Exception e) {
                             }
                             val_sales.add(""+ap.getOID()+"");
                             key_sales.add(ap.getFullName());
                         }
                         %> <%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SALES_PERSON_ID], null, ""+srcSaleReportDetail.getSalesPersonId(), val_sales, key_sales, "", "formElemen")%>
                        </td>
                      </tr>
                      <tr id="shift_combo" style="display: none;">
                        <td height="21" width="14%"><%=getJspTitle(FLD_SHIFT,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" width="0%">:</td>
                        <td height="21" width="86%">&nbsp;
                        <%
                         Vector val_shiftid = new Vector(1,1);
                         Vector key_shiftid = new Vector(1,1);
                         Vector vt_shift = PstShift.listAll();
                         val_shiftid.add("0");
                         key_shiftid.add("Semua Shift");
                         for(int d=0;d<vt_shift.size();d++) {
                             Shift shf = (Shift)vt_shift.get(d);
                             val_shiftid.add(""+shf.getOID()+"");
                             key_shiftid.add(shf.getName());
                         }
                         
                         String select_shift = ""+srcSaleReportDetail.getShiftId();
                         %> <%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SHIFT_ID], null, select_shift, val_shiftid, key_shiftid, "", "formElemen")%>
                        </td>
                      </tr>
                      <tr id="supplier_combo" style="display: none;">
                        <td height="21" width="14%"><%=getJspTitle(FLD_SUPPLIER,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" width="0%">:</td>
                        <td height="21" width="86%">&nbsp;
                        <%
                        Vector val_supplier= new Vector(1,1);
                         Vector key_supplier= new Vector(1,1);
                         String whClauseSupp = " CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] + "=" + PstContactClass.FLD_CLASS_VENDOR+
                                 " OR CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] + "=" + PstContactClass.CONTACT_TYPE_SUPPLIER;
                         
                         String orderClause = PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE];
                         String whereClause = ""+PstContactList.fieldNames[PstContactList.FLD_CONTACT_TYPE]+" = "+PstContactList.EXT_COMPANY+
                                 " OR "+PstContactList.fieldNames[PstContactList.FLD_CONTACT_TYPE]+" = "+PstContactList.OWN_COMPANY;
                         
                         String ordBySupp =  PstContactList.fieldNames[PstContactList.FLD_COMP_NAME];
                         SrcMemberReg srcMemberReg = new SrcMemberReg();
                         Vector vt_supplier = SessMemberReg.searchSupplier(srcMemberReg,0,0);
                         for(int d=0;d<vt_supplier.size();d++) {
                             ContactList contact = (ContactList)vt_supplier.get(d);
                             val_supplier.add(""+contact.getOID()+"");
                             key_supplier.add(contact.getContactCode()+" - "+contact.getCompName());
                         }
                         %> <%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SUPPLIER_ID], null, ""+srcSaleReportDetail.getSupplierId(), val_supplier, key_supplier, "", "formElemen")%>
                        </td>
                      </tr>
                      <tr id="merk_combo" style="display: none;">
                        <td height="21" width="14%"><%=getJspTitle(FLD_MERK,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" width="0%">:</td>
                        <td height="21" width="86%">&nbsp;
                        <%
                         Vector val_merkid= new Vector(1,1);
                         Vector key_merkid= new Vector(1,1);
                         Vector vt_merk= PstMerk.list(0,0,"",PstMerk.fieldNames[PstMerk.FLD_NAME]);
                         for(int d=0;d<vt_merk.size();d++) {
                             Merk merk = (Merk)vt_merk.get(d);
                             val_merkid.add(""+merk.getOID()+"");
                             key_merkid.add(merk.getName());
                         }
                         %> <%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_MARK_ID], null, ""+srcSaleReportDetail.getMarkId(), val_merkid, key_merkid, "", "formElemen")%>
                        </td>
                      </tr>
                       <tr>
                        <td height="21" width="14%"><%=getJspTitle(FLD_URUTKAN,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="85%" valign="top" align="left">&nbsp;
                        <%
                        Vector val_order= new Vector(1,1);
                        Vector key_order= new Vector(1,1);
                        val_order.add(""+SrcSaleReport.SORT_BY_CATEGORY+"");
                        key_order.add(SrcSaleReport.viewOrderBy[SESS_LANGUAGE][SrcSaleReport.SORT_BY_CATEGORY]);
                        val_order.add(""+SrcSaleReport.SORT_BY_SUPPLIER+"");
                        key_order.add(SrcSaleReport.viewOrderBy[SESS_LANGUAGE][SrcSaleReport.SORT_BY_SUPPLIER]);
                        val_order.add(""+SrcSaleReport.SORT_BY_MARK+"");
                        key_order.add(SrcSaleReport.viewOrderBy[SESS_LANGUAGE][SrcSaleReport.SORT_BY_MARK]);
                        val_order.add(""+SrcSaleReport.SORT_BY_SHIFT+"");
                        key_order.add(SrcSaleReport.viewOrderBy[SESS_LANGUAGE][SrcSaleReport.SORT_BY_SHIFT]);
                        
                        String default_order = SrcSaleReport.viewOrderBy[SESS_LANGUAGE][SrcSaleReport.SORT_BY_CATEGORY];
			%>
			<%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_ORDER_BY], null, ""+srcSaleReportDetail.getSortBy(), val_order, key_order, "", "formElemen")%></td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="14%" align="left"><%=getJspTitle(FLD_VIEW_PHOTO,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" valign="top" width="0%" align="left">:</td>
                        <td height="21" width="86%" valign="top" align="left">
                          <!--input type="checkbox" <%if(srcSaleReportDetail.getImageView()==1){%>checked<%}%> name="<%=FrmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_VIEW_PHOTO]%>" value="1"> check if you want photo viewed. -->
                          <input type="radio" <%if(srcSaleReportDetail.getImageView()==0){%>checked<%}%> name="<%=FrmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_VIEW_PHOTO]%>" value="0"> <%=textListHeader[SESS_LANGUAGE][15]%> <br>
                          <input type="radio" <%if(srcSaleReportDetail.getImageView()==1){%>checked<%}%> name="<%=FrmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_VIEW_PHOTO]%>" value="1"> <%=textListHeader[SESS_LANGUAGE][14]%>
                        </td>
                      </tr>
                      <tr>
                        <td height="21" valign="top" align="left">&nbsp;</td>
                        <td height="21" valign="top" align="left">&nbsp;</td>
                        <td height="21" valign="top" align="left">&nbsp;</td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="14%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="0%" align="left">&nbsp;</td>
                        <td height="21" width="86%" valign="top" align="left"> 
                          <table width="40%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td nowrap width="7%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=textListTitleHeader[SESS_LANGUAGE][3]%>"></a></td>
                              <td nowrap width="3%">&nbsp;</td>
                              <td class="command" nowrap width="90%"><a href="javascript:cmdSearch()"><%=textListTitleHeader[SESS_LANGUAGE][3]%></a></td>
                            </tr>
                          </table></td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </form>
            <script language=JavaScript>
                document.frmsrcreportsale.<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_GROUP_BY]%>.value = "<%=SrcSaleReport.GROUP_BY_CATEGORY%>";
                cmdSwitchGroup();
            </script>
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
