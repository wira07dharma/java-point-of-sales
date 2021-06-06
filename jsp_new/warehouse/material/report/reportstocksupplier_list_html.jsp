<%@page import="com.dimata.common.entity.payment.StandartRate"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.posbo.entity.search.SrcMaterial"%>
<%@page import="com.dimata.common.entity.payment.PriceType"%>
<%@page import="com.dimata.common.entity.payment.PstStandartRate"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatCostingStokFisik"%>
<%@ page import="com.dimata.qdep.form.FRMHandler,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.posbo.entity.search.SrcReportStock,
                 com.dimata.posbo.session.warehouse.SessReportStock,
                 com.dimata.posbo.form.search.FrmSrcReportStock,
                 com.dimata.util.Command,
                 com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.common.entity.contact.PstContactList"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_REPORT_BY_SUPPLIER); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!
int DATA_NULL = 0;
int DATA_PRINT = 1;

public static final String textListGlobal[][] = {
	{"Tidak ada data", "LAPORAN STOK PER SUPLIER", "Periode", "Lokasi", "Suplier", "Semua", "Laporan Stok", "Cetak Laporan Stok"},
	{"No available data", "STOCK REPORT BY SUPPLIER", "Period", "Location", "Supplier", "All", "Stock Report", "Print Stock Report"}
};
public static final String textListMaterialHeader[][] = {
	{"NO","SKU","NAMA BARANG","QTY", "SATUAN", "HRG BELI","TOTAL BELI","HPP","NILAI STOCK","BARCODE","HARGA JUAL","TOTAL HARGA JUAL"},
	{"NO","CODE","NAME","QTY", "UNIT", "COST","TOTAL COST","COGS","STOCK VALUE","BARCODE","SALES PRICE","TOTAL SALES PRICE"}
};

public String drawList2(JspWriter outObj, int language, Vector objectClass, long locationId, long supplierId, Vector listTypeHrga) {
	//Vector result = new Vector(1,1);
        String result = "";
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("99%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.setBorder(1);
		ctrlist.addHeader(textListMaterialHeader[language][0],"5%");
		ctrlist.addHeader(textListMaterialHeader[language][1],"10%");
                ctrlist.addHeader(textListMaterialHeader[language][9],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][2],"20%");

		ctrlist.addHeader(textListMaterialHeader[language][3],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][4],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][7],"10%");
                ctrlist.addHeader(textListMaterialHeader[language][8],"15%");

                Vector listCurrStandardX = PstStandartRate.listCurrStandard(0);  
                if(listTypeHrga != null && listTypeHrga.size()>0){
                    for(int i = 0; i<listTypeHrga.size();i++){
                        for(int j=0;j<listCurrStandardX.size();j++){
                            Vector temp = (Vector)listCurrStandardX.get(j);
                            CurrencyType curr = (CurrencyType)temp.get(0);
                            PriceType pricetype = (PriceType)listTypeHrga.get(i);
                            ctrlist.addHeader(textListMaterialHeader[language][10],"10%");
                            ctrlist.addHeader(textListMaterialHeader[language][11],"10%");
                        }
                    }
                }
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		
		double totalNilaiStock = 0;
                
		double subQty=0;
                double subAvergae=0;
                double subsaldo=0;

                double grandQty=0;
                double grandAvergae=0;
                double grandsaldo=0;

                long oldSupplierId=0;
                boolean usesubtotal=false;
                double totalNilaiJual=0;
		for(int i=0; i<objectClass.size(); i++) {
			Vector vt = (Vector)objectClass.get(i);			
			Material material = (Material)vt.get(0);
			MaterialStock materialStock = (MaterialStock)vt.get(1);
			Unit unit = (Unit)vt.get(2);
			ContactList contact = (ContactList) vt.get(3);
                        //cek qty
                        Date dateNow = new Date();
                        double stokSisa = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(material.getOID(), locationId, dateNow, 0);
                        materialStock.setQty(stokSisa);
                        
                        grandQty = grandQty+materialStock.getQty();
                        grandAvergae = grandAvergae+material.getAveragePrice();
                        grandsaldo = grandsaldo+(material.getAveragePrice() * materialStock.getQty());

                        

			Vector rowx = new Vector();
                        
                        //header
                        if(supplierId==0){
                        
                            if(oldSupplierId!=contact.getOID()){
                                    oldSupplierId=contact.getOID();

                                    if(i!=0){
                                        rowx = new Vector();
                                        rowx.add("");
                                        rowx.add("");
                                        rowx.add("");
                                        rowx.add("<b>SUBTOTAL<b>");
                                        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(subQty)+"</b></div>");
                                        rowx.add("<div align=\"center\"></div>");
                                        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(subAvergae)+"</b></div>");
                                        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(subsaldo)+"</b></div>");
                                        if(listTypeHrga != null && listTypeHrga.size()>0){
                                            for(int k = 0; k<listTypeHrga.size();k++){
                                                for(int j=0;j<listCurrStandardX.size();j++){
                                                    Vector temp = (Vector)listCurrStandardX.get(j);
                                                    CurrencyType curr = (CurrencyType)temp.get(0);
                                                    PriceType pricetype = (PriceType)listTypeHrga.get(k);
                                                    rowx.add("<div align=\"center\"></div>");
                                                    rowx.add("<div align=\"center\"></div>");
                                                }
                                            }
                                        }
                                        lstData.add(rowx);
                                        
                                        rowx = new Vector();
                                        rowx.add("<div align=\"right\"></div>");
                                        rowx.add("<div align=\"right\"></div>");
                                        rowx.add("<div align=\"right\"></div>");
                                        rowx.add("<div align=\"right\"></div>");
                                        rowx.add("<div align=\"right\"></div>");
                                        rowx.add("<div align=\"right\"></div>");
                                        rowx.add("<div align=\"right\"></div>");
                                        rowx.add("<div align=\"right\"></div>");
                                        if(listTypeHrga != null && listTypeHrga.size()>0){
                                            for(int k = 0; k<listTypeHrga.size();k++){
                                                for(int j=0;j<listCurrStandardX.size();j++){
                                                    Vector temp = (Vector)listCurrStandardX.get(j);
                                                    CurrencyType curr = (CurrencyType)temp.get(0);
                                                    PriceType pricetype = (PriceType)listTypeHrga.get(k);
                                                    rowx.add("<div align=\"center\"></div>");
                                                    rowx.add("<div align=\"center\"></div>");
                                                }
                                            }
                                        }
                                        lstData.add(rowx);
                                    }
                                    
                                    subQty = 0;
                                    subAvergae = 0;
                                    subsaldo =0;

                                    subQty = subQty+materialStock.getQty();
                                    subAvergae = subAvergae+material.getAveragePrice();
                                    subsaldo = subsaldo+(material.getAveragePrice() * materialStock.getQty());


                                    rowx = new Vector();
                                    rowx.add("");
                                    rowx.add("");
                                    rowx.add("");
                                    rowx.add("<b>"+contact.getCompName()+"</b>");
                                    rowx.add("<div align=\"right\"></div>");
                                    rowx.add("<div align=\"center\"></div>");
                                    rowx.add("<div align=\"right\"></div>");
                                    rowx.add("<div align=\"right\"></div>");
                                    if(listTypeHrga != null && listTypeHrga.size()>0){
                                            for(int k = 0; k<listTypeHrga.size();k++){
                                                for(int j=0;j<listCurrStandardX.size();j++){
                                                    Vector temp = (Vector)listCurrStandardX.get(j);
                                                    CurrencyType curr = (CurrencyType)temp.get(0);
                                                    PriceType pricetype = (PriceType)listTypeHrga.get(k);
                                                    rowx.add("<div align=\"center\"></div>");
                                                    rowx.add("<div align=\"center\"></div>");
                                                }
                                            }
                                    }
                                    lstData.add(rowx);
                                    
                                    
                            }else{
                                subQty = subQty+materialStock.getQty();
                                subAvergae = subAvergae+material.getAveragePrice();
                                subsaldo = subsaldo+(material.getAveragePrice() * materialStock.getQty());
                            }
                        }
                        
                        rowx = new Vector();
			rowx.add(String.valueOf(i+1)+".");
			rowx.add(material.getSku());
                        rowx.add("<div style='mso-number-format:\"\\@\"' align=\"left\">"+material.getBarCode()+"</div>");
			rowx.add(material.getName());
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQty())+"</div>");
			rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(material.getAveragePrice())+"</div>");
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
                        
			lstData.add(rowx);
		}
                
                Vector rowx = new Vector();
                if(supplierId==0){
                    rowx = new Vector();
                    rowx.add("");
                    rowx.add("");
                    rowx.add("");
                    rowx.add("<b>SUBTOTAL</b>");
                    rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(subQty)+"</b></div>");
                    rowx.add("<div align=\"center\"></div>");
                    rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(subAvergae)+"</b></div>");
                    rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(subsaldo)+"</b></div>");
                    if(listTypeHrga != null && listTypeHrga.size()>0){
                            for(int k = 0; k<listTypeHrga.size();k++){
                                for(int j=0;j<listCurrStandardX.size();j++){
                                    Vector temp = (Vector)listCurrStandardX.get(j);
                                    CurrencyType curr = (CurrencyType)temp.get(0);
                                    PriceType pricetype = (PriceType)listTypeHrga.get(k);
                                    rowx.add("<div align=\"center\"></div>");
                                    rowx.add("<div align=\"center\"></div>");
                                }
                            }
                    }
                    lstData.add(rowx);
                }
                rowx = new Vector();
                rowx.add("");
                rowx.add("");
                rowx.add("");
                rowx.add("<b>GRAND TOTAL</b>");
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(grandQty)+"</b></div>");
                rowx.add("<div align=\"center\"></div>");
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(grandAvergae)+"</b></div>");
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(grandsaldo)+"</b></div>");
                if(listTypeHrga != null && listTypeHrga.size()>0){
                        for(int k = 0; k<listTypeHrga.size();k++){
                            for(int j=0;j<listCurrStandardX.size();j++){
                                Vector temp = (Vector)listCurrStandardX.get(j);
                                CurrencyType curr = (CurrencyType)temp.get(0);
                                PriceType pricetype = (PriceType)listTypeHrga.get(k);
                                rowx.add("<div align=\"center\"></div>");
                                rowx.add("<div align=\"center\"><b>"+FRMHandler.userFormatStringDecimal(totalNilaiJual)+"</b></div>");
                            }
                        }
                }
                lstData.add(rowx);


                ctrlist.draw(outObj);
	}
	else{
	}
	return result;
}

%>

<!-- Jsp Block -->
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int viewzerostock = FRMQueryString.requestInt(request, "viewzerostock");

ControlLine ctrLine = new ControlLine();
SrcReportStock srcReportStock = new SrcReportStock();
SessReportStock sessReportStock = new SessReportStock();
FrmSrcReportStock frmSrcReportStock = new FrmSrcReportStock(request, srcReportStock);

try{ 
    srcReportStock = (SrcReportStock)session.getValue(SessReportStock.SESS_SRC_REPORT_STOCK); 
    if (srcReportStock == null) srcReportStock = new SrcReportStock();
}catch(Exception e){ 
    srcReportStock = new SrcReportStock();
}

boolean calculateQtyNull = false;
if(viewzerostock==1){
    calculateQtyNull = true;
}else{
    calculateQtyNull = false;
}
//Vector listStockPerSupplier = sessReportStock.getReportStockPerSupplier(srcReportStock, calculateQtyNull); // getReportStock
Vector listStockPerSupplier = sessReportStock.getReportStockPerSupplierDistinct(srcReportStock, calculateQtyNull); // getReportStock

//Vector vct = drawList2(SESS_LANGUAGE, listStockPerSupplier);
//String strDrawlist = (String)vct.get(0);
//String strNilaiStock = (String)vct.get(1);

Location location = new Location();
if (srcReportStock.getLocationId() != 0) {
	try	{
		location = PstLocation.fetchExc(srcReportStock.getLocationId());
	}
	catch(Exception e) {
		System.out.println("Exc when get lokasi");
	}
}
else {
	location.setName(textListGlobal[SESS_LANGUAGE][5]+" "+textListGlobal[SESS_LANGUAGE][3]);
}

Periode periode = new Periode();
try {
	periode = PstPeriode.fetchExc(srcReportStock.getPeriodeId());
}
catch(Exception e) {
	System.out.println("Exc when get periode");
}

ContactList contactList = new ContactList();
try{
	contactList = PstContactList.fetchExc(srcReportStock.getSupplierId());
}catch(Exception e){
	System.out.println("Exc when get periode");
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

%> 
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
function cmdBack() {
	document.frm_reportstock.command.value="<%=Command.BACK%>";
	document.frm_reportstock.action="src_reportstocksupplier.jsp";
	document.frm_reportstock.submit();
} 

function printForm() {
	window.open("buff_pdf_liststockreportsuplier.jsp", "stock_report_by_supplier")
}

function printFormExcel() {
        var pricetypei=document.frm_reportstock.FRM_FIELD_PRICE_TYPE_ID.value;
	window.open("reportstocksupplier_list_excel.jsp?FRM_FIELD_PRICE_TYPE_ID='"+pricetypei+"'&viewzerostock=<%=viewzerostock%>", "stock_report_by_supplier")
}


function printFormHtml() {
        var pricetypei=document.frm_reportstock.FRM_FIELD_PRICE_TYPE_ID.value;
	window.open("reportstocksupplier_list_html.jsp?FRM_FIELD_PRICE_TYPE_ID='"+pricetypei+"'&viewzerostock=<%=viewzerostock%>", "stock_report_by_supplier")
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
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
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
				  	<h4><strong><%=textListGlobal[SESS_LANGUAGE][1]%></strong></h3>
				  </td>
                </tr>
                <tr align="left" valign="top">
                  <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][2]).toUpperCase()%></b></td>
				  <td width="1%" height="14" valign="middle" class="command"><b>:</b></td>
				  <td width="89%" height="14" valign="middle" class="command"><%=periode.getPeriodeName().toUpperCase()%></td>
                </tr>
                <tr align="left" valign="top">
                  <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][3]).toUpperCase()%></b></td>
				  <td width="1%" height="14" valign="middle" class="command"><b>:</b></td>
				  <td width="89%" height="14" valign="middle" class="command"><%=location.getName().toUpperCase()%></td>
                </tr>
                <tr align="left" valign="top">
                  <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][4]).toUpperCase()%></b></td>
				  <td width="1%" height="14" valign="middle" class="command"><b>:</b></td>
				  <td width="89%" height="14" valign="middle" class="command"><%=contactList.getCompName().toUpperCase()%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" align="center" colspan="3"><%=drawList2(out,SESS_LANGUAGE, listStockPerSupplier,srcReportStock.getLocationId(),srcReportStock.getSupplierId(),listTypeHrga)%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" align="center" colspan="3">&nbsp;</td>
                </tr>
              </table>
            </form>
            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
