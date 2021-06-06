<%@page import="com.dimata.gui.jsp.ControlCheckBox"%>
<%@page import="com.dimata.posbo.entity.search.SrcSaleReport"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.common.entity.contact.PstContactClass"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.StandartRate"%>
<%@page import="com.dimata.common.entity.payment.PstStandartRate"%>
<%@page import="com.dimata.common.entity.payment.PriceType"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page import="com.dimata.posbo.entity.search.SrcReportPotitionStock,
                 com.dimata.posbo.form.search.FrmSrcReportPotitionStock,
                 com.dimata.posbo.session.warehouse.SessReportPotitionStock,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlDate"%>
<%@ page import="com.dimata.posbo.entity.masterdata.PstMerk"%>
<%@ page import="com.dimata.posbo.entity.masterdata.Merk"%>
<%@ page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@ page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_POTITION); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListHeader[][] = {
    {"Lokasi","Supplier","Kategori","Periode","Sku","Sub Kategori","Merk","Semua","Informasi","Nilai Stok Dari"},
    {"Location","Supplier","Category","Period","Sku","Sub Category","Merk","All","Information","Stock Vallue From"}	
};

public static final String textListTitleHeader[][] = {
    {"Stok","Laporan","Posisi Stok","Pencarian"," s/d ","Laporan Posisi Stok"},
    {"Stock","Report","Stock Position","Searching"," to ","Stock Potition Report"}
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
int type = FRMQueryString.requestInt(request,"type");
int includeWarehouse = FRMQueryString.requestInt(request, "INCLUDE_WAREHOUSE");

ControlLine ctrLine = new ControlLine();

SrcReportPotitionStock srcReportPotitionStock = new SrcReportPotitionStock();
FrmSrcReportPotitionStock frmSrcReportPotitionStock = new FrmSrcReportPotitionStock();
try {
    srcReportPotitionStock = (SrcReportPotitionStock)session.getValue(SessReportPotitionStock.SESS_SRC_STOCK_POTITION_REPORT);
} catch(Exception e) {
    srcReportPotitionStock = new SrcReportPotitionStock();
}
if(srcReportPotitionStock==null){
    srcReportPotitionStock = new SrcReportPotitionStock();
}

try {
    session.removeValue(SessReportPotitionStock.SESS_SRC_STOCK_POTITION_REPORT);
} catch(Exception e){
}

/** */
Date dateFrom = new Date();
Date dateTo = new Date();
if(srcReportPotitionStock.getDateFrom()!=null){
    dateFrom = srcReportPotitionStock.getDateFrom();
}

if(srcReportPotitionStock.getDateTo()!=null){
    dateTo = srcReportPotitionStock.getDateTo();
}

/** get location list */
Vector val_locationid = new Vector(1,1);
Vector key_locationid = new Vector(1,1);
//Vector vt_loc = new Vector(1,1);
//vt_loc = PstLocation.list(0,0,"",PstLocation.fieldNames[PstLocation.FLD_NAME]);
String whereLocViewReport = PstDataCustom.whereLocReportViewStock(userId, "user_view_sale_stock_report_location");
Vector vt_loc = PstLocation.list(0,0,whereLocViewReport,PstLocation.fieldNames[PstLocation.FLD_NAME]);
for(int d=0;d<vt_loc.size();d++) {
    Location loc = (Location)vt_loc.get(d);
    val_locationid.add(""+loc.getOID()+"");
    key_locationid.add(loc.getName());
}

/** get category list */
/*Vector materGroup = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_NAME]);
Vector vectGroupVal = new Vector(1,1);
Vector vectGroupKey = new Vector(1,1);
vectGroupVal.add(textListHeader[SESS_LANGUAGE][7]+" "+textListHeader[SESS_LANGUAGE][2]);
vectGroupKey.add("0");
if(materGroup!=null && materGroup.size()>0) {
    for(int i=0; i<materGroup.size(); i++) {
        Category mGroup = (Category)materGroup.get(i);
        vectGroupVal.add(mGroup.getName());
        vectGroupKey.add(""+mGroup.getOID());
    }
}*/

/** get merk list */
Vector materMerk = PstMerk.list(0,0,"",PstMerk.fieldNames[PstMerk.FLD_NAME]);
Vector vectMerkVal = new Vector(1,1);
Vector vectMerkKey = new Vector(1,1);
vectMerkKey.add("0");
vectMerkVal.add(textListHeader[SESS_LANGUAGE][7]+" "+textListHeader[SESS_LANGUAGE][6]);
if(materMerk!=null && materMerk.size()>0){
    for(int i=0; i<materMerk.size(); i++){
        Merk merk = (Merk)materMerk.get(i);
        vectMerkKey.add(""+merk.getOID());
        vectMerkVal.add(merk.getName());
    }
}

/** get info showed */
Vector vectInfoShowedVal = new Vector(1,1);
Vector vectInfoShowedKey = new Vector(1,1);
vectInfoShowedKey.add(String.valueOf(SrcReportPotitionStock.SHOW_BOTH));
vectInfoShowedVal.add(SrcReportPotitionStock.stringInfoShowed[SESS_LANGUAGE][SrcReportPotitionStock.SHOW_BOTH]);
vectInfoShowedKey.add(String.valueOf(SrcReportPotitionStock.SHOW_QTY_ONLY));
vectInfoShowedVal.add(SrcReportPotitionStock.stringInfoShowed[SESS_LANGUAGE][SrcReportPotitionStock.SHOW_QTY_ONLY]);
vectInfoShowedKey.add(String.valueOf(SrcReportPotitionStock.SHOW_VALUE_ONLY));
vectInfoShowedVal.add(SrcReportPotitionStock.stringInfoShowed[SESS_LANGUAGE][SrcReportPotitionStock.SHOW_VALUE_ONLY]);

/** get stock value by */
Vector vectStockValueVal = new Vector(1,1);
Vector vectStockValueKey = new Vector(1,1);
vectStockValueKey.add(String.valueOf(SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER));
vectStockValueVal.add(SrcReportPotitionStock.stringStockValueBy[SESS_LANGUAGE][SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER]);
vectStockValueKey.add(String.valueOf(SrcReportPotitionStock.STOCK_VALUE_BY_COGS_TRANSACTION));
vectStockValueVal.add(SrcReportPotitionStock.stringStockValueBy[SESS_LANGUAGE][SrcReportPotitionStock.STOCK_VALUE_BY_COGS_TRANSACTION]);

/*tambahan*/
String ordPrice = PstPriceType.fieldNames[PstPriceType.FLD_INDEX]; 
Vector listPriceType = PstPriceType.list(0,0,"",ordPrice);
 for(int i=0;i<listPriceType.size();i++){
    PriceType prType = (PriceType)listPriceType.get(i);
    vectStockValueKey.add(""+prType.getOID());
    vectStockValueVal.add("HARGA "+prType.getCode());
 }
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function changeInfoShowed() {
    var infoShowed = document.frmsrcReportPotitionStock.<%=FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_INFO_SHOWED]%>.value;
    
    if(infoShowed == '<%=SrcReportPotitionStock.SHOW_QTY_ONLY%>') {
        document.all.stockValueBy.style.display = 'none';
        document.all.stockValueByz.style.display = 'none';
        
    }
    else if(infoShowed == '<%=SrcReportPotitionStock.SHOW_VALUE_ONLY%>' || infoShowed == '<%=SrcReportPotitionStock.SHOW_BOTH%>') {
        document.all.stockValueBy.style.display = '';
        document.all.stockValueByz.style.display = '';
    }
    else {
        document.all.stockValueBy.style.display = 'none';
         document.all.stockValueByz.style.display = 'none';
    }
}

function cmdSearch() {
    document.frmsrcReportPotitionStock.command.value="<%=Command.LIST%>";
    //document.frmsrcReportPotitionStock.action="reportposisistock_list.jsp";
    document.frmsrcReportPotitionStock.action="reportposisistock_list_1.jsp";
    document.frmsrcReportPotitionStock.submit();
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
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
            <%=textListTitleHeader[SESS_LANGUAGE][0]%> &gt; <%=textListTitleHeader[SESS_LANGUAGE][1]%> &gt; <%=textListTitleHeader[SESS_LANGUAGE][2]%> &gt; <%=textListTitleHeader[SESS_LANGUAGE][3]%> <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmsrcReportPotitionStock" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="type" value="<%=type%>">
              <input type="hidden" name="<%=frmSrcReportPotitionStock.fieldNames[frmSrcReportPotitionStock.FRM_FIELD_USER_ID]%>" value="<%=userId%>">
              <input type="hidden" name="<%=frmSrcReportPotitionStock.fieldNames[frmSrcReportPotitionStock.FRM_FIELD_GENERATE_REPORT]%>" value="true">
              <table width="100%" border="0">
                <tr>
                  <td colspan="2">
                    <hr size="1">
                  </td>
                </tr>
                <tr>
                  <td colspan="2">
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td height="21" valign="" align="left"><%=getJspTitle(3,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" valign="" align="left">:</td>
                        <td height="21" valign="" align="left"><%=ControlDate.drawDate(frmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_DATE_FROM], dateFrom,"formElemen",1,-5)%> <%=textListTitleHeader[SESS_LANGUAGE][4]%> <%=	ControlDate.drawDate(frmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_DATE_TO], dateTo,"formElemen",1,-5) %> </td>
                      </tr>
                      <tr>
                        <td height="21" width="9%"><%=getJspTitle(0,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="90%">
                            <%=ControlCombo.draw(frmSrcReportPotitionStock.fieldNames[frmSrcReportPotitionStock.FRM_FIELD_LOCATION_ID], null, ""+srcReportPotitionStock.getLocationId(), val_locationid, key_locationid, "", "formElemen")%>
                            <%
                                String checkWarehouse = "";
                                if (iCommand == Command.BACK && includeWarehouse == 1) {
                                    checkWarehouse = "checked";
                                }
                            %>
                            <input type="checkbox" <%= checkWarehouse %> name="INCLUDE_WAREHOUSE" value="1"> Include warehouse
                        </td>
                      </tr>
                      <tr>
                        <td height="21" valign="" align="left"><%=textListHeader[SESS_LANGUAGE][2]%></td>
                        <td height="21" valign="" align="left">:</td>
                        <td height="21" valign="" width="90%" align="left">
                            <%-- <%=(ControlCombo.draw(frmSrcReportPotitionStock.fieldNames[frmSrcReportPotitionStock.FRM_FIELD_CATEGORY_ID],"formElemen", null, ""+srcReportPotitionStock.getCategoryId(), vectGroupKey, vectGroupVal, null))%> --%>
                             <select  name="<%=frmSrcReportPotitionStock.fieldNames[frmSrcReportPotitionStock.FRM_FIELD_CATEGORY_ID]%>" class="formElemen">
                               <option value="0">Semua Category</option>
                               <%
                                Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_NAME]);
                                //Category newCategory = new Category();
                                //add opie-eyek 20130821
                                String checked="selected";
                                Vector materGroup = PstCategory.structureList(masterCatAcak) ;
                                Vector vectGroupVal = new Vector(1,1);
                                Vector vectGroupKey = new Vector(1,1);
                                if(materGroup!=null && materGroup.size()>0) {
                                    String parent="";
                                   // Vector<Category> resultTotal= new Vector();
                                    Vector<Long> levelParent = new Vector<Long>();
                                    for(int i=0; i<materGroup.size(); i++) {
                                        Category mGroup = (Category)materGroup.get(i);
                                            if(mGroup.getCatParentId()!=0){
                                                for(int lv=levelParent.size()-1; lv > -1; lv--){
                                                    long oidLevel=levelParent.get(lv);
                                                    if(oidLevel==mGroup.getCatParentId()){
                                                        break;
                                                    }else{
                                                        levelParent.remove(lv);
                                                    }
                                                }
                                                parent="";
                                                for(int lv=0; lv<levelParent.size(); lv++){
                                                   parent=parent+"&nbsp;&nbsp;";
                                                }
                                                levelParent.add(mGroup.getOID());

                                            }else{
                                                levelParent.removeAllElements();
                                                levelParent.add(mGroup.getOID());
                                                parent="";
                                            }
                                        %>
                                            <option value="<%=mGroup.getOID()%>" <%=mGroup.getOID()==srcReportPotitionStock.getCategoryId()?checked:""%> ><%=parent%><%=mGroup.getName()%></option>
                                        <%
                                    }
                                } else {
                                    vectGroupVal.add("Tidak Ada Category");
                                    vectGroupKey.add("0");
                                }
                              %>
                          </select>
                        </td>
                      </tr>
                      <tr>
                        <td height="21" valign="" align="left"><%=getJspTitle(6,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" valign="" align="left">:</td>
                        <td height="21" valign="" align="left"><%=(ControlCombo.draw(frmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_MERK_ID],"formElemen", null, ""+srcReportPotitionStock.getMerkId(), vectMerkKey, vectMerkVal, null))%></td>
                      </tr>
                      <tr>
                        <td height="21" valign="" align="left"><%=getJspTitle(8,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" valign="" align="left">:</td>
                        <td height="21" valign="" align="left"><%=(ControlCombo.draw(frmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_INFO_SHOWED], null, ""+srcReportPotitionStock.getInfoShowed(), vectInfoShowedKey, vectInfoShowedVal, "onChange=\"changeInfoShowed();\"", "formElemen"))%></td>
                      </tr>
                      <tr id="stockValueBy" >
                        <td height="21" valign="" align="left"><%=getJspTitle(9,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" valign="" align="left">:</td>
                        <td height="21" valign="" align="left"><%=(ControlCombo.draw(frmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_STOCK_VALUE_BY],"formElemen", null, ""+srcReportPotitionStock.getStockValueBy(), vectStockValueKey, vectStockValueVal, null))%></td>
                      </tr>
                      <tr id="stockValueByz" >
                        <td height="21" valign="" align="left">Mata Uang</td>
                        <td height="21" valign="" align="left">:</td>
                        <%
                           Vector standartValueVal = new Vector(1,1);
                           Vector standartValueKey = new Vector(1,1);
                           Vector listCurrStandard = PstStandartRate.listCurrStandard(0);
                           for(int j=0;j<listCurrStandard.size();j++){
                                Vector temp = (Vector)listCurrStandard.get(j);
                                CurrencyType curr = (CurrencyType) temp.get(0);
                                StandartRate standart = (StandartRate)temp.get(1);
                                standartValueVal.add(""+standart.getOID());
                                standartValueKey.add(""+curr.getCode());
                                        
                           }
                        %>
                        <td height="21" valign="" align="left"><%=(ControlCombo.draw(frmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_STANDART_ID],"formElemen", null, ""+srcReportPotitionStock.getStandartId(),standartValueVal , standartValueKey, null))%></td>
                      </tr>
                      <%--supplier--%>
                      <!-- option summary nd detail -->
                     <tr>
                        <td height="21" valign="" width="9%" align="left">Supplier</td>
                        <td height="21" valign="" width="1%" align="left">:</td>
                        <td height="21" valign="" width="90%" align="left">
                                <%
                                        Vector val_supplier = new Vector(1,1);
                                        Vector key_supplier = new Vector(1,1);

                                        String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                                                                         " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
                                                                         " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
                                                                         " != "+PstContactList.DELETE;
                                        Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
                                        val_supplier.add("0");
                                        key_supplier.add("Semua Supplier");
                                        if(vt_supp!=null && vt_supp.size()>0){
                                                for(int d=0; d<vt_supp.size(); d++){
                                                        ContactList cnt = (ContactList)vt_supp.get(d);
                                                        String cntName = cnt.getCompName();
                                                        val_supplier.add(String.valueOf(cnt.getOID()));
                                                        key_supplier.add(cntName);  
                                                }
                                        }
                                        String select_supplier = ""+srcReportPotitionStock.getSupplierId();

                                %>
                                <%=ControlCombo.draw(frmSrcReportPotitionStock.fieldNames[frmSrcReportPotitionStock.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"","formElemen")%>
                        </td>
                      </tr>
                      <tr>
                        <td height="21" valign="" width="9%" align="left">Price Type By</td>
                        <td height="21" valign="" width="1%" align="left">:</td>
                        <td height="21" valign="" width="90%" align="left">
                            <%
                                //add opie-eyek 20130805
                                ControlCheckBox controlCheckBox = new ControlCheckBox();
                                 Vector prTypeVal = new Vector(1,1);
                                 Vector prTypeKey = new Vector(1,1);
                                for(int i=0;i<listPriceType.size();i++) {
                                        PriceType prType = (PriceType)listPriceType.get(i);
					prTypeVal.add(""+prType.getOID()+"");
					prTypeKey.add(""+prType.getCode()+"");
				}
                              controlCheckBox.setWidth(5);
                              
                          %> 
                          <%=controlCheckBox.draw(frmSrcReportPotitionStock.fieldNames[frmSrcReportPotitionStock.FRM_FIELD_PRICE_TYPE_ID], prTypeVal, prTypeKey, new Vector())%>
                        </td>
                      </tr>
                      <tr>
                        <td height="21" valign="" width="9%" align="left">Group By</td>
                        <td height="21" valign="" width="1%" align="left">:</td>
                        <td height="21" valign="" width="90%" align="left">
                                <%
                                        Vector val_groupby= new Vector(1,1);
                                        Vector key_groupby= new Vector(1,1);
                                        
                                        val_groupby.add(""+SrcSaleReport.GROUP_BY_ITEM+"");
                                        key_groupby.add(SrcSaleReport.groupMethod[SESS_LANGUAGE][SrcSaleReport.GROUP_BY_ITEM]);
                                        val_groupby.add(""+SrcSaleReport.GROUP_BY_CATEGORY+"");
                                        key_groupby.add(SrcSaleReport.groupMethod[SESS_LANGUAGE][SrcSaleReport.GROUP_BY_CATEGORY]);
                                        val_groupby.add(""+SrcSaleReport.GROUP_BY_SUPPLIER+"");
                                        key_groupby.add(SrcSaleReport.groupMethod[SESS_LANGUAGE][SrcSaleReport.GROUP_BY_SUPPLIER]);
                                        val_groupby.add(""+SrcSaleReport.GROUP_BY_STOCK+"");
                                        key_groupby.add(SrcSaleReport.groupMethod[SESS_LANGUAGE][SrcSaleReport.GROUP_BY_STOCK]);
                                        String default_groupby = SrcSaleReport.groupMethod[SESS_LANGUAGE][SrcSaleReport.GROUP_BY_CATEGORY];
                                %>
                                <%=ControlCombo.draw(frmSrcReportPotitionStock.fieldNames[frmSrcReportPotitionStock.FRM_FIELD_GROUP_BY],null,default_groupby,val_groupby,key_groupby,"","formElemen")%>
                        </td>
                      </tr>
                      <!-- option summary nd detail -->
                       <tr>
                        <td height="21" valign="" width="9%" align="left">View</td>
                        <td height="21" valign="" width="1%" align="left">:</td>
                        <td height="21" valign="" width="90%" align="left">
                        <%
						Vector val_viewType = new Vector(1,1);
						Vector key_viewType = new Vector(1,1);

						val_viewType.add("0");
						key_viewType.add("Detail");

						val_viewType.add("1");
						key_viewType.add("Summary");


                                                String select_viewType = "0";
						%>
                        	<%=ControlCombo.draw("view_type", null, select_viewType, val_viewType, key_viewType, "", "formElemen")%>
						</td>
                      </tr>
                      <tr>
                        <td colspan="3" height="21" valign="" width="9%" align="left">&nbsp;</td>
                      </tr>
                      <tr>
                        <td height="21" width="10%" colspan="2">&nbsp;</td>
                        <td height="21" width="90%" valign="" align="left"> 
                          <table width="40%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <!--td width="4%" nowrap><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][5],ctrLine.CMD_SEARCH,true)%>"></a></td-->
                              <td width="96%" nowrap class="command">&nbsp;<a class="btn btn-primary" href="javascript:cmdSearch()"><i class="fa fa-search"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][5],ctrLine.CMD_SEARCH,true)%></a></td>
                            </tr>
                          </table></td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </form>
            <script language="JavaScript">
                changeInfoShowed();
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
