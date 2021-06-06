<%@page import="com.dimata.common.entity.payment.StandartRate"%>
<%@page import="com.dimata.posbo.entity.search.SrcMaterial"%>
<%@page import="com.dimata.common.entity.payment.PriceType"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstStandartRate"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatCostingStokFisik"%>
<%@ page import="com.dimata.qdep.form.FRMHandler,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.entity.search.SrcReportStock,
                 com.dimata.posbo.session.warehouse.SessReportStock,
                 com.dimata.posbo.form.search.FrmSrcReportStock,
                 com.dimata.util.Command,
                 com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.posbo.form.masterdata.CtrlMaterialStock"%>
<%@ page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_REPORT); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<!-- Jsp Block -->
<%!
public static final String textListGlobal[][] = {
    {"Tidak ada data","LAPORAN STOK GLOBAL","Periode","Lokasi","Semua","Laporan Stok","Cetak Laporan Stok","Cetak Price Tag"},
    {"No available data", "GLOBAL STOCK REPORT","Period","Location","All","Stock Report","Print Stock Report","Print Price Tag"}
};

public static final String textListMaterialHeader[][] = {
    {"NO","SKU","NAMA BARANG","QTY", "SATUAN", "HRG BELI","TOTAL BELI","HPP","NILAI STOK","ETALASE","TOTAL JUAL","BARCODE","HARGA JUAL","TOTAL HARGA JUAL"},
    {"NO","CODE","NAME","QTY", "UNIT", "COST","TOTAL COST","COGS","STOCK VALUE","KSG","TOTAL PRICE","BARCODE","SALES PRICE","TOTAL SALES PRICE"}
};

public String drawList(JspWriter outObj, int language, Vector objectClass, int start, int brandInUse, long locationId, Vector listTypeHrga, int typeOfBusinessDetail, String useForRaditya) {
    String result ="";
    if(objectClass!=null && objectClass.size()>0) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        
        ctrlist.addHeader(textListMaterialHeader[language][0],"5%");//no
        ctrlist.addHeader(textListMaterialHeader[language][1],"10%");//sku
        ctrlist.addHeader(textListMaterialHeader[language][11],"20%");//barcode
        ctrlist.addHeader(textListMaterialHeader[language][2],"20%");//nama
        if(brandInUse==1) {
            ctrlist.addHeader(textListMaterialHeader[language][9],"10%");//etalase
        }
        ctrlist.addHeader(textListMaterialHeader[language][3],"10%");//qty
        if(typeOfBusinessDetail == 2){
            ctrlist.addHeader("Berat","10%");
        } else {
            ctrlist.addHeader(textListMaterialHeader[language][4],"8%");//satuan
         if(useForRaditya.equals("1")){
            ctrlist.addHeader(textListMaterialHeader[language][7],"10%");//harga HPP
          }else{
            ctrlist.addHeader(textListMaterialHeader[language][5],"10%");//harga beli
          }
        }
        ctrlist.addHeader(textListMaterialHeader[language][8],"10%");//nilai stock
        Vector listCurrStandardX = PstStandartRate.listCurrStandard(0);  
        if(listTypeHrga != null && listTypeHrga.size()>0){
            for(int i = 0; i<listTypeHrga.size();i++){
                for(int j=0;j<listCurrStandardX.size();j++){
                    Vector temp = (Vector)listCurrStandardX.get(j);
                    CurrencyType curr = (CurrencyType)temp.get(0);
                    PriceType pricetype = (PriceType)listTypeHrga.get(i);
                    ctrlist.addHeader(textListMaterialHeader[language][12],"10%");
                    ctrlist.addHeader(textListMaterialHeader[language][13],"10%");
                }
            }
        }
        ctrlist.setLinkRow(1);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        
        double totalNilaiStock = 0;
        double totalNilaiJual = 0;
        double totalQtyStock = 0;
        double totalBeratStock = 0;
        for(int i=0; i<objectClass.size(); i++) {
            Vector vt = (Vector)objectClass.get(i);
            Material material = (Material)vt.get(0);
            MaterialStock materialStock = (MaterialStock)vt.get(1);
            Unit unit = (Unit)vt.get(2);
            Date dateNow = new Date();
            double stokSisa = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(material.getOID(), locationId, dateNow, 0);
            materialStock.setQty(stokSisa);
            
            //added by dewok 2018
            Material newmat = new Material();
            Category category = new Category();
            Color color = new Color();
            try {
                newmat = PstMaterial.fetchExc(materialStock.getMaterialUnitId());
                category = PstCategory.fetchExc(newmat.getCategoryId());
                color = PstColor.fetchExc(newmat.getPosColor());
            } catch (Exception e) {
                
            }
            String itemName = "" + material.getName();
            if(typeOfBusinessDetail == 2){
                if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                    itemName = "" + category.getName() + " " + color.getColorName() + " " + newmat.getName();
                } else if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                    itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + newmat.getName();
                }
            }

            Vector rowx = new Vector();
            rowx.add(String.valueOf(start+i+1)+".");
            rowx.add(material.getSku());
            rowx.add(""+material.getBarcode());
	    rowx.add(itemName);	
            if(brandInUse==1) {
                rowx.add(""+material.getGondolaName());
            }
			
            
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQty())+"</div>");            
            if(typeOfBusinessDetail == 2){
                rowx.add("<div align=\"right\">"+String.format("%,.3f", materialStock.getBerat())+"</div>");
            } else {
                rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
         if(useForRaditya.equals("1")){
                rowx.add("<div align=\"center\">"+FRMHandler.userFormatStringDecimal(material.getAveragePrice())+"</div>"); 
          }else{
                rowx.add("<div align=\"center\">"+FRMHandler.userFormatStringDecimal(material.getAveragePrice())+"</div>");                
          }
            }
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(material.getAveragePrice() * materialStock.getQty())+"</div>");

            SrcMaterial srcMaterial = new SrcMaterial();
            Hashtable memberPrice = SessMaterial.getPriceSaleInTypePriceMember(srcMaterial, material, material.getOID());
            if(listTypeHrga != null && listTypeHrga.size()>0){
                for(int k = 0; k<listTypeHrga.size();k++){
                    PriceType pricetype = (PriceType)listTypeHrga.get(k); 
                    for(int j=0;j<listCurrStandardX.size();j++){
                        Vector temp = (Vector)listCurrStandardX.get(j);
                        Vector tempStand = (Vector)listCurrStandardX.get(j);
                        CurrencyType currx = (CurrencyType)tempStand.get(0);
                        StandartRate standart = (StandartRate)tempStand.get(1);
                        PriceTypeMapping pTypeMapping = null; 
                        if(memberPrice!=null && !memberPrice.isEmpty()){
                           pTypeMapping = (PriceTypeMapping) memberPrice.get(""+pricetype.getOID()+"_"+standart.getOID());
                        }
                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(pTypeMapping.getPrice())+"</div>");
                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(pTypeMapping.getPrice()*materialStock.getQty())+"</div>");
                        totalNilaiJual=totalNilaiJual+(pTypeMapping.getPrice()*materialStock.getQty());
                    }
                }
            }

	    totalQtyStock = totalQtyStock + materialStock.getQty(); 		
            totalNilaiJual = totalNilaiJual + (material.getDefaultPrice() * materialStock.getQty());
            totalNilaiStock += (material.getAveragePrice() * materialStock.getQty());
            totalBeratStock = totalBeratStock + materialStock.getBerat();
            
            lstData.add(rowx);
            }
        
            Vector rowx = new Vector();
            
            
            rowx = new Vector();
            rowx.add("");
			
            if(brandInUse==1) {
                rowx.add("");
            }
            rowx.add("<div align=\"right\">&nbsp;</div>");
            rowx.add("<div align=\"right\">&nbsp;</div>");
            rowx.add("<div align=\"right\"><b>TOTAL</b></div>");
            rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalQtyStock)+"</b></div>");            
            if(typeOfBusinessDetail == 2){
                rowx.add("<div align=\"right\"><b>"+String.format("%,.3f", totalBeratStock)+"</b></div>");
            } else {
                rowx.add("<div align=\"right\">&nbsp;</div>");
                rowx.add("<div align=\"right\">&nbsp;</div>");
            }
            rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalNilaiStock)+"</b></div>");
            if(listTypeHrga != null && listTypeHrga.size()>0){
            for(int i = 0; i<listTypeHrga.size();i++){
                for(int j=0;j<listCurrStandardX.size();j++){
                        Vector temp = (Vector)listCurrStandardX.get(j);
                        CurrencyType curr = (CurrencyType)temp.get(0);
                        PriceType pricetype = (PriceType)listTypeHrga.get(i);
                        rowx.add("<div align=\"right\">&nbsp;</div>");
                        rowx.add("<div align=\"right\">&nbsp;<b>"+FRMHandler.userFormatStringDecimal(totalNilaiJual)+"</b></div>");
                    }
                }
            }
            lstData.add(rowx);
            ctrlist.draw(outObj);
    } else{
        result = "<b>Data tidak ditemukan...</b>";
    }
    return result;
}
%>


<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int viewzerostock = FRMQueryString.requestInt(request, "viewzerostock");

CtrlMaterialStock ctrlMaterialStock = new CtrlMaterialStock(request);
ControlLine ctrLine = new ControlLine();
SrcReportStock srcReportStock = new SrcReportStock();
SessReportStock sessReportStock = new SessReportStock();
FrmSrcReportStock frmSrcReportStock = new FrmSrcReportStock(request, srcReportStock);

if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
    try {
        srcReportStock = (SrcReportStock)session.getValue(SessReportStock.SESS_SRC_REPORT_STOCK);
        if (srcReportStock == null) srcReportStock = new SrcReportStock();
    } catch(Exception e) {
        srcReportStock = new SrcReportStock();
    }
} else {
    frmSrcReportStock.requestEntityObject(srcReportStock);
    session.putValue(SessReportStock.SESS_SRC_REPORT_STOCK, srcReportStock);
}

Location location = new Location();
if (srcReportStock.getLocationId() != 0) {
    try	{
        location = PstLocation.fetchExc(srcReportStock.getLocationId());
    } catch(Exception e) {
    }
} else {
    location.setName(textListGlobal[SESS_LANGUAGE][4]+" "+textListGlobal[SESS_LANGUAGE][3]);
}

Periode periode = new Periode();
try {
    periode = PstPeriode.fetchExc(srcReportStock.getPeriodeId());
} catch(Exception e){
}

/** use tis variable for display stock with zero qty */
//boolean calculateQtyNull = false;
boolean calculateQtyNull = false;
if(viewzerostock==1){
    calculateQtyNull = true;
}else{
    calculateQtyNull = false;
}

/** get size stock list and stock value */
//Vector vctStockValue = sessReportStock.getStockValue(srcReportStock, calculateQtyNull);;
//Vector vctStockValue = sessReportStock.getStockValueDistinct(srcReportStock, calculateQtyNull);
//int vectSize = Integer.parseInt((String)vctStockValue.get(0));
//double stockValue = Double.parseDouble((String)vctStockValue.get(1));
//int recordToGet = 500;

if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST) {
    //start = ctrlMaterialStock.actionList(iCommand,start,vectSize,recordToGet);
}

Vector vectMember = new Vector(1,1);
String[] strMember = null;
Vector listTypeHrga =  new Vector ();
strMember = request.getParameterValues("FRM_FIELD_PRICE_TYPE_ID");
String sStrMember="";
if(strMember!=null && strMember.length>0) {
       for(int i=0; i<strMember.length; i++) {
               try {
                   if(strMember[i] != null && strMember[i].length()>0){ 
                    vectMember.add(strMember[i]);
                   sStrMember=sStrMember+strMember[i]+",";
                   }
               }
               catch(Exception exc) {
                       System.out.println("err");
               }
       }
       if(sStrMember != null && sStrMember.length()>0){
           sStrMember=sStrMember.substring(0, sStrMember.length()-1);
           String whereClauses = PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID]+ " IN("+sStrMember+")";
            listTypeHrga =  PstPriceType.list(0, 0, whereClauses, "");
       }
}
Vector listStockAll = sessReportStock.getReportStockAllSummary2(srcReportStock, calculateQtyNull, start, 0);
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
function cmdEdit(oid) {
}

function cmdListFirst() {
	document.frm_reportstock.command.value="<%=Command.FIRST%>";
	document.frm_reportstock.action="reportstock_list.jsp";
	document.frm_reportstock.submit();
}

function cmdListPrev() {
	document.frm_reportstock.command.value="<%=Command.PREV%>";
	document.frm_reportstock.action="reportstock_list.jsp";
	document.frm_reportstock.submit();
}

function cmdListNext() {
	document.frm_reportstock.command.value="<%=Command.NEXT%>";
	document.frm_reportstock.action="reportstock_list.jsp";
	document.frm_reportstock.submit();
}

function cmdListLast() {
	document.frm_reportstock.command.value="<%=Command.LAST%>";
	document.frm_reportstock.action="reportstock_list.jsp";
	document.frm_reportstock.submit();
}

function cmdBack() {
	document.frm_reportstock.command.value="<%=Command.BACK%>";
	document.frm_reportstock.action="src_reportstock.jsp";
	document.frm_reportstock.submit();
}

function printForm() {
        var pricetypei=document.frm_reportstock.FRM_FIELD_PRICE_TYPE_ID.value;
        if("<%=typeOfBusinessDetail%>" == 2) {
            window.open("report_stock_global.jsp?FRM_FIELD_PRICE_TYPE_ID='"+pricetypei+"'&viewzerostock=<%=viewzerostock%>","form_stock_report");
        } else {
            window.open("reportstock_list_html.jsp?FRM_FIELD_PRICE_TYPE_ID='"+pricetypei+"'&viewzerostock=<%=viewzerostock%>","form_stock_report");
        }
}

function printFormExcel() {
        //window.open("reportstock_list_excel.jsp","form_stock_report");
        var pricetypei=document.frm_reportstock.FRM_FIELD_PRICE_TYPE_ID.value;
        window.open("reportstock_list_excel.jsp?FRM_FIELD_PRICE_TYPE_ID='"+pricetypei+"'&viewzerostock=<%=viewzerostock%>","form_stock_report");
}

function printPriceTag(){
     var strvalue  = "<%=approot%>/masterdata/barcode_stok.jsp?command=<%=Command.ADD%>&start=<%=start%>";
    winSrcMaterial = window.open(strvalue,"searchtipeharga", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}
}
//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->&nbsp;<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportstock" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="FRM_FIELD_PRICE_TYPE_ID" value="<%=sStrMember%>">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top"> 
                  <td height="14" colspan="3" align="center" valign="middle">
                    <h4><strong><%=textListGlobal[SESS_LANGUAGE][1]%></strong></h4>
                  </td>
                </tr>
                <tr align="left" valign="top">
                  <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][2]).toUpperCase()%></b></td>
                  <td width="1%" height="14" valign="middle" class="command">:</td>
                  <td width="89%" height="14" valign="middle" class="command"><%=periode.getPeriodeName().toUpperCase() %> ( <%= periode.getStartDate()%> - <%= periode.getEndDate()%> )</td>
                </tr>
                <tr align="left" valign="top">
                  <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][3]).toUpperCase()%></b></td>
		  <td width="1%" height="14" valign="middle" class="command">:</td>
		  <td width="89%" height="14" valign="middle" class="command"><%=location.getName().toUpperCase()%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" align="center" valign="middle" colspan="3"><%=drawList(out,SESS_LANGUAGE, listStockAll, start,brandInUse,srcReportStock.getLocationId(), listTypeHrga, typeOfBusinessDetail, useForRaditya)%></td>
                </tr>
                <%--
                <tr align="left" valign="top"> 
                  <td height="8" align="left" colspan="3" class="command"> 
                    <span class="command"> 
                      <%
                            ctrLine.setLocationImg(approot+"/images");
                            ctrLine.initDefault();
                            out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
                      %>
                    </span>
                  </td>
                </tr>
		<% if(listStockAll.size() != 0) { %>
                <tr align="left" valign="top"> 
                  <td height="22" align="right" valign="middle" colspan="3">
		    <h3><font><b>TOTAL <%=textListMaterialHeader[SESS_LANGUAGE][8]%> : &nbsp;&nbsp;&nbsp; <%=FRMHandler.userFormatStringDecimal(stockValue)%></b></font></h3>
                  </td>
                </tr>
		<% } %>
                    --%>
                <tr align="left" valign="top"> 
                  <td height="18" valign="top" colspan="3"> <table width="100%" border="0">
                      <tr> 
                        <td width="80%"> 
                           <table width="55%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <!--td nowrap width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][5],ctrLine.CMD_BACK_SEARCH,true)%>"></a></td-->
                              <td nowrap width="2%">&nbsp;</td>
                              <td class="command" nowrap width="92%"><a class="btn btn-primary" href="javascript:cmdBack()"><i class="fa fa-arrow-left"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][5],ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                            </tr>
                          </table>
                        </td>
                        <td width="20%"> 
                        <% if(listStockAll.size() != 0) { %>
                          <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr> 
                              <!--td width="5%" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][6]%>"></a></td-->
                              <td width="25%" nowrap>&nbsp; <a class="btn btn-primary" href="javascript:printForm()" class="command" ><i class="fa fa-print"></i> 
                                <%=textListGlobal[SESS_LANGUAGE][6]%></a></td>
                              <!--td width="5%" valign="top"><a href="javascript:printFormExcel()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][6]%>"></a></td-->
                              <td width="25%" nowrap>&nbsp; <a class="btn btn-primary" href="javascript:printFormExcel()" class="command" ><i class="fa fa-print"></i> 
                                Export Excel</a></td>
                              <!--td width="5%" valign="top"><a href="javascript:printPriceTag()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][7]%>"></a></td-->
                              <td width="25%" nowrap>&nbsp; <a class="btn btn-primary" href="javascript:printPriceTag()" class="command" ><i class="fa fa-print"></i> 
                                <%=textListGlobal[SESS_LANGUAGE][7]%></a></td>
                            </tr>
                          </table>
                        <% } %>
                        </td>
                      </tr>
                    </table></td>
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
            <%@include file="../../../styletemplate/footer.jsp" %>
        <%}else{%>
            <%@ include file = "../../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
