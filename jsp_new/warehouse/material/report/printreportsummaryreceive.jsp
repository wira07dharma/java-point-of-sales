<%-- 
    Document   : printreportsummaryreceive
    Created on : Nov 11, 2016, 4:34:49 PM
    Author     : Witar
--%>

<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import="com.dimata.common.entity.location.Location"%>
<%@page import="com.dimata.common.entity.location.PstLocation"%>
<%@page import="com.dimata.common.entity.payment.StandartRate"%>
<%@page import="com.dimata.qdep.form.FRMHandler"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseOrder"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstPurchaseOrder"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.common.entity.payment.PriceType"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@page import="com.dimata.common.entity.payment.PstStandartRate"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.posbo.session.warehouse.SessReportReceive"%>
<%@page import="com.dimata.posbo.entity.search.SrcReportReceive"%>
<%@page import="com.dimata.util.Formater"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcReportReceive"%>
<%@page import="java.util.Date"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dimata-Prochain</title>
    </head>
    <body>
        <style>
            table {
    border-collapse: collapse;
}

table, th, td {
    border: 1px solid black;
}
        </style>
        <style media="print">
  .noPrint{ display: none; }
 
</style>
        <%
            String textListHeader[][] ={ 
                {"No","Tanggal","Invoice","PO No","Kode Penerimaan","Suplier","Keterangan","Total Qty","Total Beli","Total HPP","Total Harga Jual","Margin","Tidak ada data tersedia"},
                {"No","Date","Invoice","No PO","Receiving Code","Supplier","Description","Total Qty","TOtal Purchase,Total HPP","Total Sales Price","Margin","No Data Available"}
            };
            
            String htmlReturn="";
            String dateFromCatch = FRMQueryString.requestString(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_FROM]+"");
            String dateToCatch = FRMQueryString.requestString(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_TO]+"");

            Date dateFrom = Formater.formatDate(dateFromCatch, "MM/dd/yyyy");
            Date dateTo = Formater.formatDate(dateToCatch, "MM/dd/yyyy");
            /*
            Date dateFrom = FRMQueryString.requestDate(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_FROM]+"");
            Date dateTo = FRMQueryString.requestDate(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_TO]+"");*/
            /*
            long locationId = FRMQueryString.requestLong(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_LOCATION_ID]+"");
            long currencyId = FRMQueryString.requestLong(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_CURRENCY_ID]+"");
            long supplierId = FRMQueryString.requestLong(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_SUPPLIER_ID]+"");
            */
            int language = FRMQueryString.requestInt(request, "language");
            SrcReportReceive srcReportReceive = new SrcReportReceive();
            SessReportReceive sessReportReceive = new SessReportReceive();
            FrmSrcReportReceive frmSrcReportReceive = new FrmSrcReportReceive(request, srcReportReceive);
            frmSrcReportReceive.requestEntityObject(srcReportReceive);

            String priceTypeMultiple[] = request.getParameterValues(""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_PRICE_TYPE_ID]+"");
            String priceTypeInText="";
            try {
                for (int a = 0; a<priceTypeMultiple.length;a++){
                    if (priceTypeInText.length()>0){
                        priceTypeInText = priceTypeInText + "," + priceTypeMultiple[a];
                    }else{
                        priceTypeInText =  priceTypeMultiple[a];
                    }
                }
            } catch (Exception e) {
            }
            String locationMultiple [] = request.getParameterValues(""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_LOCATION_ID]+"");
            String locationInText="";
            try {
                for (int b = 0; b<locationMultiple.length;b++){
                    if (!locationMultiple[b].equals("0")){
                        if (locationInText.length()>0){
                            locationInText = locationInText + "," + locationMultiple[b];
                        }else{
                            locationInText =  locationMultiple[b];
                        }
                    }
                }
            } catch (Exception e) {
            }
        srcReportReceive.setMultiLocation(locationInText);
            srcReportReceive.setMultiPriceType(priceTypeInText);
            srcReportReceive.setDateFrom(dateFrom);
            srcReportReceive.setDateTo(dateTo);
            Vector records = sessReportReceive.getReportReceiveSummary(srcReportReceive);
            Vector listCurrStandardX = PstStandartRate.listCurrStandard(0); 

            Vector listTypeHrga =  new Vector (); 
            if(priceTypeInText!=null && priceTypeInText.length()>0) {
                String whereClauses = PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID]+ " IN("+priceTypeInText+")";
                listTypeHrga =  PstPriceType.list(0, 0, whereClauses, "");
            }

            int jumlahTambahanKolom = 0;
            long oidLocation = FRMQueryString.requestLong(request, "FRM_FIELD_LOCATION_ID");
            
            String nameLocation = "";
            try {
                for (int b = 0; b<locationMultiple.length;b++){
                    if (!locationMultiple[b].equals("0")){
                        String locationName = "";
                        if (locationMultiple[b].equals("0")){
                            locationName = "Semua Lokasi";
                        }else{
                            Location entLocation = new Location();
                            try{
                                entLocation = PstLocation.fetchExc(Long.parseLong(locationMultiple[b]));
                                
                            }catch(Exception ex){

                            }
                            locationName = entLocation.getName();
                        }                      
                        if (nameLocation.length()>0){                            
                            nameLocation = nameLocation + "," + locationName;
                        }else{
                            nameLocation =  locationName;
                        }
                    }
                }
            } catch (Exception e) {
            }
            
            long currencyID = FRMQueryString.requestLong(request, "FRM_FIELD_CURRENCY_ID");
            CurrencyType entCurrency = new CurrencyType();
            try{
                entCurrency = PstCurrencyType.fetchExc(currencyID);
            }catch(Exception ex){
                
            }
            
            String namaCurrency = "";
            if (currencyID==0){
                namaCurrency = "Semua Mata Uang";
            }else{
                namaCurrency = entCurrency.getName();
            }
            
            htmlReturn += ""
                + "<table border=0 style='border:none; width:100%'>"
                    + "<tr>"
                        + "<td colspan='3' style='border:none;'><center><h3>Laporan Summary Penerimaan</center></h3></td>"
                    + "</tr>"
                    + "<tr style='font-weight:bold;'>"
                        + "<td style='width:10%;border:none;'>Periode</h4></td>"
                        + "<td style='width:5%;border:none;'>:</td>"
                        + "<td style='width:85%;border:none;'>"+ Formater.formatDate(dateFrom, "dd/MM/yyyy")+" s/d "+ Formater.formatDate(dateTo, "dd/MM/yyyy")+"</td>"
                    + "</tr>"
                    + "<tr style='font-weight:bold;'>"
                        + "<td style='width:10%;border:none;'>Lokasi</h4></td>"
                        + "<td style='width:5%;border:none;'>:</td>"
                        + "<td style='width:85%;border:none;'>"+nameLocation+"</td>"
                    + "</tr>"
                    + "<tr style='font-weight:bold;'>"
                        + "<td style='width:10%;border:none;'>Mata Uang</h4></td>"
                        + "<td style='width:5%;border:none;'>:</td>"
                        + "<td style='width:85%;border:none;'>"+namaCurrency+"</td>"
                    + "</tr>"
                + "<table>"
                + "<br>" 
                + "<table style='width:100%' class='table table-striped table-bordered table-hover'>"
                    + "<tr>"
                        + "<th>"+textListHeader[language][0]+"</th>"
                        + "<th>"+textListHeader[language][1]+"</th>"
                        + "<th>"+textListHeader[language][2]+"</th>"
                        + "<th>"+textListHeader[language][3]+"</th>"
                        + "<th>"+textListHeader[language][4]+"</th>"
                        + "<th>"+textListHeader[language][5]+"</th>"
                        + "<th>"+textListHeader[language][6]+"</th>"
                        + "<th>"+textListHeader[language][7]+"</th>"
                        + "<th>"+textListHeader[language][8]+"</th>"
                        + "<th>"+textListHeader[language][9]+"</th>";
                        if(listTypeHrga != null && listTypeHrga.size()>0){
                        for(int i = 0; i<listTypeHrga.size();i++){
                            for(int j=0;j<listCurrStandardX.size();j++){
                                Vector temp = (Vector)listCurrStandardX.get(j);
                                CurrencyType curr = (CurrencyType)temp.get(0);
                                PriceType pricetype = (PriceType)listTypeHrga.get(i); 
                                //ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
                                //ctrlist.dataFormat("","5%","0","0","right","bottom"); // total harga jual
                                htmlReturn += ""
                                    + "<th>"+textListHeader[language][10]+"</th>"
                                    + "<th>"+textListHeader[language][11]+"</th>";
                                jumlahTambahanKolom += 2;
                            }
                        }
                    }
                    htmlReturn += ""
                    + "</tr>";
                    long noBill = 0;
                    boolean firstRow = true;
                    double totalRec = 0.00;
                    double totalPrice = 0.00;
                    double totalQty = 0;
                    double subTotalRec = 0.00;

                    double subTotalBeli = 0.00;
                    double subTotalQty = 0;
                    double subTotalHPP=0;
                    double subTotalHargaJual=0;

                    int baris = 0;
                    int countTrue = 0;

                    double subTotalJual = 0.00;
                    double totalJual = 0.00;
                    double totalLastJual = 0.00;
                    double[] arrSubTotalHargaJual = new double[listTypeHrga.size()];

                    int noColspan = 10 +jumlahTambahanKolom;
                    if (records.size()>0){
                        for(int i=0; i<records.size(); i++) {
                            int counter = i + 1;                                
                            Vector vt = (Vector)records.get(i);
                            MatReceive rec = (MatReceive)vt.get(0);
                            MatReceiveItem rmi = (MatReceiveItem)vt.get(1);
                            Material mat = (Material)vt.get(2);
                            ContactList cnt = (ContactList)vt.get(3);

                            String nomorPo="Tanpa PO";
                            try{
                                if(rec.getPurchaseOrderId()!=0){
                                    PurchaseOrder purchaseOrder = PstPurchaseOrder.fetchExc(rec.getPurchaseOrderId());
                                    nomorPo = purchaseOrder.getPoCode();
                                }
                            }catch(Exception ex){
                            }

                            htmlReturn += ""
                            + "<tr>"
                                + "<td>"+counter+"</td>"
                                + "<td>"+Formater.formatDate(rec.getReceiveDate(), "dd-MM-yyyy")+"</td>"
                                + "<td>"+rec.getInvoiceSupplier()+"</td>"
                                + "<td>"+nomorPo+"</td>"
                                + "<td>"+rec.getRecCode()+"</td>"
                                + "<td>"+cnt.getCompName()+"</td>"
                                + "<td>"+rec.getRemark()+"</td>"
                                + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(rmi.getQty())+"</td>"
                                + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(rmi.getTotal())+"</td>"
                                + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</td>";

                                subTotalQty += rmi.getQty();
                                subTotalBeli += rmi.getTotal();
                                subTotalHPP += mat.getDefaultPrice();
                                if(listTypeHrga != null && listTypeHrga.size()>0){
                                    for(int a = 0; a<listTypeHrga.size();a++){
                                        PriceType pricetype = (PriceType)listTypeHrga.get(a); 
                                        for(int j=0;j<listCurrStandardX.size();j++){
                                            Vector temp = (Vector)listCurrStandardX.get(j);
                                            CurrencyType curr = (CurrencyType)temp.get(0);
                                            StandartRate objStandartRate = PstStandartRate.getActiveStandardRate(curr.getOID());
                                            double total = SessReportReceive.getTotalPriceReceiveByMemberAndReceiveId(rec.getOID(), objStandartRate.getOID(), pricetype.getOID());
                                            //ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
                                            //ctrlist.dataFormat("","5%","0","0","right","bottom"); // total harga jual
                                            double margin = ((total-mat.getDefaultPrice())/mat.getDefaultPrice())*100;
                                            htmlReturn += ""
                                                + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(total)+"</td>"
                                                + "<td style='text-align:right;'>"+margin+"</td>";

                                            arrSubTotalHargaJual[a] += total;
                                        }
                                    }
                                }
                            htmlReturn += ""
                            + "</tr>";
                            
                        }
                        htmlReturn += ""
                        + "<tr style='font-weight:bold'>"
                            + "<td colspan='7'><center>TOTAL</center></td>"
                            + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(subTotalQty)+"</td>"
                            + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(subTotalBeli)+"</td>"
                            + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(subTotalHPP)+"</td>";
                            if(listTypeHrga != null && listTypeHrga.size()>0){
                                for(int a = 0; a<listTypeHrga.size();a++){
                                    PriceType pricetype = (PriceType)listTypeHrga.get(a); 
                                    for(int j=0;j<listCurrStandardX.size();j++){

                                        htmlReturn += ""
                                            + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(arrSubTotalHargaJual[a])+"</td>"
                                            + "<td style='text-align:right;'></td>";                                                      
                                    }
                                }
                            }
                        htmlReturn += ""
                        + "</tr>";
                    }else{
                        htmlReturn += "<tr><td colspan='"+noColspan+"'><center>"+textListHeader[language][12]+"</center></td></tr>";
                    }

                    htmlReturn += ""
                + "</table>";
                htmlReturn +=""
                    + "<table id='tableSIgn' width='100%' border='0' style='border:0px;' cellspacing='0' cellpadding='0' >"
                        +"<tr align='left' valign='top'> "
                            +"<td style='border:0px;' height='22' valign='middle' colspan='4'></td>"
                        +"</tr>"
                        +"<tr align='left' valign='top'> "
                            +" <td style='border:0px;' height='40' valign='middle' colspan='4'></td>"
                        +"</tr>"
                        +"<tr> "
                            +" <td style='border:0px;' width='25%' align='left' nowrap>Mengetahui</td>  "
                            +"<td style='border:0px;' width='25%' align='left' nowrap></td>"
                            +"<td style='border:0px;' align='25' valign='left' nowrap>Accounting &nbps;&nbps;&nbps;&nbps;</td>"
                            +"<td style='border:0px;' width='25%' align='left' nowrap>EDP/Inventory</td>"
                        +"</tr>"
                        +"<tr align='left' valign='top'> "
                            +" <td style='border:0px;' height='75' valign='middle' colspan='4'></td>"
                        +"</tr>"
                        +"<tr> "
                            +"<td style='border:0px;' width='25%' align='left' nowrap> (.................................) "
                            +"</td>  "
                            +"<td style='border:0px;' width='25%' align='left' nowrap> "
                            +"</td>"
                            +"<td style='border:0px;' align='25%' valign='left' nowrap> (.................................) "
                            +"</td>"
                            +"<td style='border:0px;' width='25%' align='left' nowrap> (.................................) "
                            +"</td>"
                        +"</tr>"
                        +"<tr align='left' valign='top'> "
                            +" <td style='border:0px;' height='22' valign='middle' colspan='4'></td>"
                        +" </tr>"
                    +"</table>";           
                 htmlReturn += "<br>"
                + "<button class='noPrint' onclick='window.print();' type='button'>&nbsp; Print &nbsp;</button>";
                htmlReturn +="" 
                + "<br>"
                + "<button class='noPrint' onclick='window.print();' type='button'>&nbsp; Print &nbsp;</button>";
        
            out.println(htmlReturn);
        %>
    </body>
</html>
