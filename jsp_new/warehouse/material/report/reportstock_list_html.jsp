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
    {"NO","SKU","NAMA BARANG","QTY", "SATUAN", "HRG BELI","TOTAL BELI","HPP","NILAI STOK","KSG","TOTAL JUAL","BARCODE","HARGA JUAL","TOTAL HARGA JUAL"},
    {"NO","CODE","NAME","QTY", "UNIT", "COST","TOTAL COST","COGS","STOCK VALUE","KSG","TOTAL PRICE","BARCODE","SALES PRICE","TOTAL SALES PRICE"}
};

public String drawList(JspWriter outObj, int language, Vector objectClass, int start, int brandInUse, long locationId, Vector listTypeHrga) {
    String result ="";
    if(objectClass!=null && objectClass.size()>0) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.setBorder(1);

        ctrlist.addHeader(textListMaterialHeader[language][0],"5%");
        ctrlist.addHeader(textListMaterialHeader[language][1],"10%");
        ctrlist.addHeader(textListMaterialHeader[language][11],"20%");
        ctrlist.addHeader(textListMaterialHeader[language][2],"20%");
        if(brandInUse==1) {
                ctrlist.addHeader(textListMaterialHeader[language][9],"10%");
        }
        ctrlist.addHeader(textListMaterialHeader[language][3],"10%");
        ctrlist.addHeader(textListMaterialHeader[language][4],"8%");
        ctrlist.addHeader(textListMaterialHeader[language][5],"10%");
        ctrlist.addHeader(textListMaterialHeader[language][8],"10%");
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
        for(int i=0; i<objectClass.size(); i++) {
            Vector vt = (Vector)objectClass.get(i);
            Material material = (Material)vt.get(0);
            MaterialStock materialStock = (MaterialStock)vt.get(1);
            Unit unit = (Unit)vt.get(2);
            Date dateNow = new Date();
            double stokSisa = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(material.getOID(), locationId, dateNow, 0);
            materialStock.setQty(stokSisa);

            Vector rowx = new Vector();
            rowx.add(String.valueOf(start+i+1)+".");
            rowx.add(material.getSku());
            rowx.add(""+material.getBarcode());
	    rowx.add(material.getName());	
            if(brandInUse==1) {
                rowx.add(""+material.getGondolaName());
            }
			
            
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQty())+"</div>");
            rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
            rowx.add("<div align=\"center\">"+FRMHandler.userFormatStringDecimal(material.getAveragePrice())+"</div>");
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
            rowx.add("<div align=\"right\">&nbsp;</div>");
            rowx.add("<div align=\"right\">&nbsp;</div>");
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

try {
    srcReportStock = (SrcReportStock)session.getValue(SessReportStock.SESS_SRC_REPORT_STOCK);
    if (srcReportStock == null) srcReportStock = new SrcReportStock();
} catch(Exception e) {
    srcReportStock = new SrcReportStock();
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
                  <td height="22" align="center" valign="middle" colspan="3"><%=drawList(out,SESS_LANGUAGE, listStockAll, start,brandInUse,srcReportStock.getLocationId(), listTypeHrga)%></td>
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
