package com.dimata.printman;

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.gui.jsp.ControlList;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.payment.PstCashPayment;
import com.dimata.pos.session.billing.SessBilling;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.search.SrcSaleReport;
import com.dimata.posbo.report.sale.SaleReportDocument;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Formater;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Witar
 */
public class SalesDetailExcel extends HttpServlet {
    
    public static final String textListGlobal[][] = {
	{"Laporan Penjualan", "Laporan Penjualan Invoice Detail","Semua Location","Penjualan Cash","Penjualan Credit"},
	{"Sale Report", "Invoice Detail Sale Report","All Location","Cash Sales"," Credit Sales"}
    };
    public static final String textListMaterialHeader[][] = {
        {"No","Tgl","Nomor","Konsumen","Sales Bruto","Diskon","Pajak","Pelayanan","Netto Inv.","DP Deduction","HPP","Kasir","Detail Item","Catatan","Guest"},
        {"No","Date","Number","Customer","Sales Bruto","Discount","Tax","Service","Netto Inv.","DP Deduction","Cogs","Sales","Item Detail","Remark","Guest"}
    };
    public static final String textListMaterialDetailHeader[][] = {
        {"Kode","Nama Produk","Harga","Diskon","Qty","Unit","Harga Total"},
        {"Code","Product Name","Price","Discount","Qty","Unit","Total Price"}
    };
    public static final String textListTitleHeader[][] = {
        {"Laporan Rekap Penjualan Harian","LAPORAN REKAP PENJUALAN PER ","Tidak ada data transaksi ..","Lokasi","Shift","Laporan","Cetak Laporan Penjualan Invoice Detail","Tipe","Tanggal","s/d","Mata Uang"},
        {"Daily Sales Recapitulation Report","SALES RECAPITULATION REPORT PER SHIFT","No transaction data available ..","Location","Shift","Report","Print Invoice Detail Sale Report","Type","Date","to","Currency Type"}
    };
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, java.io.IOException {        
        processRequest(request, response);
    }

    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, java.io.IOException {
        processRequest(request, response);
    }
    
    public String drawListDetail(int language,Vector objectClass, long currencyId, double rate) {
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.setCellSpacing("1");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.setBorder(1);
            ctrlist.dataFormat(textListMaterialDetailHeader[language][0],"17%","0","0","center","bottom"); //kode produk
            ctrlist.dataFormat(textListMaterialDetailHeader[language][1],"17%","0","0","center","bottom"); //nama produk
            ctrlist.dataFormat(textListMaterialDetailHeader[language][2],"9%","0","0","center","bottom"); //harga
            ctrlist.dataFormat(textListMaterialDetailHeader[language][3],"9%","0","0","center","bottom"); //diskon
            ctrlist.dataFormat(textListMaterialDetailHeader[language][4],"7%","0","0","center","bottom"); //qty
            ctrlist.dataFormat(textListMaterialDetailHeader[language][5],"5%","0","0","center","bottom"); //unit
            ctrlist.dataFormat(textListMaterialDetailHeader[language][6],"13%","0","0","center","bottom"); //harga total

            ctrlist.setLinkRow(-1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            Vector rowx = new Vector();

            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();

            for(int i=0; i<objectClass.size(); i++) {
                Vector temp = (Vector)objectClass.get(i);
                Billdetail billDetail = (Billdetail)temp.get(0);
                Unit unit = (Unit)temp.get(1);

                rowx = new Vector();
                rowx.add("<div align='left'>"+billDetail.getSku()+"</div>");
                rowx.add("<div align='left'>"+billDetail.getItemName()+"</div>");
                if(currencyId != 0) {
                    rowx.add("<div align='right'>"+FRMHandler.userFormatStringDecimal(billDetail.getItemPrice())+"</div>");
                    rowx.add("<div align='right'>"+FRMHandler.userFormatStringDecimal(billDetail.getDisc())+"</div>");
                }
                else {
                    rowx.add("<div align='right'>"+FRMHandler.userFormatStringDecimal(billDetail.getItemPrice() * rate)+"</div>");
                    rowx.add("<div align='right'>"+FRMHandler.userFormatStringDecimal(billDetail.getDisc() * rate)+"</div>");
                }
                rowx.add("<div align='right'>"+FRMHandler.userFormatStringDecimal(billDetail.getQty())+"</div>");
                rowx.add("<div align='left'>"+unit.getCode()+"</div>");
                if(currencyId != 0) {
                    rowx.add("<div align='right'>"+FRMHandler.userFormatStringDecimal(billDetail.getTotalPrice())+"</div>");
                }else {
                    rowx.add("<div align='right'>"+FRMHandler.userFormatStringDecimal(billDetail.getTotalPrice() * rate)+"</div>");
                }

                lstData.add(rowx);
                lstLinkData.add(String.valueOf(0));
            }
            result = ctrlist.draw();
	}
	return result;
}
    
    public String drawList(int language, Vector objectClass, int start, SrcSaleReport srcSaleReport, long currencyId, int paymentDinamis) {
        String result = "";
        String frmCurrency = "#,###";
        String frmCurrencyUsd = "#,###.##";
	if(objectClass!=null && objectClass.size()>0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgentitle");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.setCellSpacing("1");
            ctrlist.setBorder(1);

            ctrlist.dataFormat(textListMaterialHeader[language][0],"3%","0","0","center","bottom"); //no
            ctrlist.dataFormat(textListMaterialHeader[language][2],"7%","0","0","center","bottom"); // no inv
            ctrlist.dataFormat(textListMaterialHeader[language][1],"6%","0","0","center","bottom"); // tgl
            ctrlist.dataFormat(textListMaterialHeader[language][11],"6%","0","0","center","bottom"); // spg
            ctrlist.dataFormat(textListMaterialHeader[language][14],"6%","0","0","center","bottom"); // guest
            ctrlist.dataFormat(textListMaterialHeader[language][13],"6%","0","0","center","bottom"); // remark
            ctrlist.dataFormat(textListMaterialHeader[language][12],"36%","0","0","center","bottom"); // detail
            ctrlist.dataFormat(textListMaterialHeader[language][8],"9%","0","0","center","bottom"); // trans
            ctrlist.dataFormat(textListMaterialHeader[language][5],"7%","0","0","center","bottom"); // disc
            ctrlist.dataFormat(textListMaterialHeader[language][6],"7%","0","0","center","bottom"); // tax
            ctrlist.dataFormat(textListMaterialHeader[language][7],"7%","0","0","center","bottom"); // tax
            //ctrlist.dataFormat(textListMaterialHeader[language][8],"9%","0","0","center","bottom"); // nett
            ctrlist.dataFormat(textListMaterialHeader[language][4],"9%","0","0","center","bottom"); // trans

            ctrlist.setLinkRow(-1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            Vector rowx = new Vector();

            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();

            double bruto = 0;
            double diskon = 0;
            double pajak = 0;
            double service = 0;
            double netto = 0;

            double totalBruto = 0;
            double totalDisc = 0;
            double totalTax = 0;
            double totalService = 0;						
            double totalNetto = 0;

            double grandBruto = 0, grandDiskon = 0, grandPajak = 0, grandService = 0, grandNetto = 0;
            String designMat = String.valueOf(PstSystemProperty.getValueByName("DESIGN_MATERIAL_FOR"));
            int DESIGN_MATERIAL_FOR = Integer.parseInt(designMat);

            String where = "";
            double rate = 0;
		
            for(int i=0; i<objectClass.size(); i++) {
                BillMain billMain = (BillMain)objectClass.get(i);
                ContactList contactlist = new ContactList();
                try {
                    contactlist = PstContactList.fetchExc(billMain.getCustomerId());
                }
                catch(Exception e) {
                        System.out.println("Contact not found ...");
                }
                if(paymentDinamis!=2){
                    rate = billMain.getRate();
                }else{
                    rate=1;
                }
                rowx = new Vector();			
                rowx.add("<div align='center'>"+(start+i+1)+"</div>");
                rowx.add("<div align='left'>"+billMain.getInvoiceNumber()+"</div>");
                rowx.add("<div align='left'>"+Formater.formatDate(billMain.getBillDate(),"dd/MM/yyyy HH:mm:ss")+"</div>");               
                rowx.add("<div align='left'>"+PstAppUser.getNameCashier(billMain.getAppUserId())+"</div>");
                rowx.add("<div align='left'>"+billMain.getGuestName()+"</div>");
                rowx.add("<div align='left'>"+billMain.getNotes()+"</div>");
                where = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID();
                Vector list = SessBilling.listInvoiceDetail(0, 0, where, "");
                rowx.add(drawListDetail(language, list, currencyId, rate));

                if(currencyId != 0) {                        
                    bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID()); 
                    diskon = billMain.getDiscount();
                    pajak = billMain.getTaxValue();
                    service = billMain.getServiceValue();
                }else {
                    bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID()) * rate;
                    diskon = billMain.getDiscount() * rate;
                    pajak = billMain.getTaxValue() * rate;
                    service = billMain.getServiceValue() * rate;
                }                
                netto = bruto - diskon + pajak + service;
                
                rowx.add("<div align='right'>"+FRMHandler.userFormatStringDecimal(bruto)+"</div>");
                rowx.add("<div align='right'>"+FRMHandler.userFormatStringDecimal(diskon)+"</div>");
                rowx.add("<div align='right'>"+FRMHandler.userFormatStringDecimal(pajak)+"</div>");
                rowx.add("<div align='right'>"+FRMHandler.userFormatStringDecimal(service)+"</div>");
                rowx.add("<div align='right'>"+Formater.formatNumber(netto, frmCurrency)+"</div>");

                totalBruto += bruto;
                totalDisc += diskon;
                totalTax += pajak;
                grandBruto += bruto;
                totalService +=service;
                totalNetto += netto;

                lstData.add(rowx);
                lstLinkData.add(String.valueOf(billMain.getOID()));
            }

            rowx = new Vector();
            rowx.add("<div align='left'></div>");
            rowx.add("<div align='left'></div>");
            rowx.add("<div align='left'></div>");
            rowx.add("<div align='left'></div>");
            rowx.add("<div align='left'></div>");
            rowx.add("<div align='left'></div>");
            rowx.add("<div align='left'><b>Sub Total</b></div>");
            rowx.add("<div align='right'><b>"+FRMHandler.userFormatStringDecimal(totalBruto)+"</b></div>");
            rowx.add("<div align='right'><b>"+FRMHandler.userFormatStringDecimal(totalDisc)+"</b></div>");
            rowx.add("<div align='right'><b>"+FRMHandler.userFormatStringDecimal(totalTax)+"</b></div>");
            rowx.add("<div align='right'><b>"+FRMHandler.userFormatStringDecimal(totalService)+"</b></div>");
            rowx.add("<div align='right'><b>"+Formater.formatNumber(totalNetto, frmCurrency)+"</b></div>");
            lstData.add(rowx);
            if(paymentDinamis==2){
                srcSaleReport.setCurrencyOid(1);
            }
            Vector grandTotal = SessBilling.getGrandTotal(srcSaleReport);
            if(DESIGN_MATERIAL_FOR==1){
            }else{
                grandBruto = Double.parseDouble((String)grandTotal.get(0));
            }

            grandDiskon = Double.parseDouble((String)grandTotal.get(1));
            grandPajak = Double.parseDouble((String)grandTotal.get(2));
            grandService = Double.parseDouble((String)grandTotal.get(3));
            grandNetto = grandBruto - grandDiskon + grandPajak + grandService;

            rowx = new Vector();
            rowx.add("<div align='left'></div>");
            rowx.add("<div align='left'></div>");
            rowx.add("<div align='left'></div>");
            rowx.add("<div align='left'></div>");
            rowx.add("<div align='left'></div>");
            rowx.add("<div align='left'><b>Grand Total</b></div>");
            rowx.add("<div align='right'><b>"+FRMHandler.userFormatStringDecimal(grandBruto)+"</b></div>");
            rowx.add("<div align='right'><b>"+FRMHandler.userFormatStringDecimal(grandDiskon)+"</b></div>");
            rowx.add("<div align='right'><b>"+FRMHandler.userFormatStringDecimal(grandPajak)+"</b></div>");
            rowx.add("<div align='right'><b>"+FRMHandler.userFormatStringDecimal(grandService)+"</b></div>");
            rowx.add("<div align='right'><b>"+Formater.formatNumber(grandNetto, frmCurrency)+"</b></div>");
            lstData.add(rowx);

            lstLinkData.add(String.valueOf(0));

            result = ctrlist.draw();
        }
        return result;
    }
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, java.io.IOException {
        
        System.out.println("---===| Excel Report |===---");
        response.setContentType("application/x-msexcel"); 
        
        HttpSession session = request.getSession(true);
        SrcSaleReport srcSaleReport = new SrcSaleReport();
        
        try {
            srcSaleReport = (SrcSaleReport) session.getValue(SaleReportDocument.SALE_REPORT_DOC);
            if (srcSaleReport == null) {
                srcSaleReport = new SrcSaleReport();
            }
        } catch (Exception e) {            
            srcSaleReport = new SrcSaleReport();
        }
        
        int iSaleReportType = FRMQueryString.requestInt(request, "sale_type");
        int language = FRMQueryString.requestInt(request, "language");
        
        String startDate = Formater.formatDate(srcSaleReport.getDateFrom(),"yyyy-MM-dd 00:00:00");
        String endDate = Formater.formatDate(srcSaleReport.getDateTo(),"yyyy-MM-dd 23:59:59");
        String createstartDate = Formater.formatDate(srcSaleReport.getDateFrom(),"yyyyMMdd");
        response.setHeader("Content-Disposition","attachment;filename=reportlistsale_detail"+createstartDate+".xls");
        
        //TANGGAL TRANSAKSI
        String where = ""
            + " "+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"'"
            + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE;
        if(iSaleReportType != -1){
            where += " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+iSaleReportType;
        }

        //TIPE PENJUALAN
        if(iSaleReportType == PstCashPayment.CASH) {
            where += ""
            + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE;
        }else{
            where += " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED;
        }
        
        //SHIFT
        if(srcSaleReport.getShiftId()!=0) {
            where += " AND "+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
        }

        //LOKASI
        if(srcSaleReport.getLocationId()!=0) {
            where += " AND "+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
        }

        //CURRENCY
        if(srcSaleReport.getCurrencyOid()!=0) {
            where += " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+"="+srcSaleReport.getCurrencyOid();
        }
        
        Location location = new Location();
        try {
            location = PstLocation.fetchExc(srcSaleReport.getLocationId());
        }
        catch(Exception e) {
            location.setName(textListGlobal[language][2]);
        }
        
        int paymentDinamis = 0;
        paymentDinamis = Integer.parseInt(PstSystemProperty.getValueByName("USING_PAYMENT_DYNAMIC"));
        String order = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
        Vector records = PstBillMain.list(0,0, where,order);
        
        String strCurrencyType = "All";
        if(srcSaleReport.getCurrencyOid() != 0) {
            String whereClause = PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+"="+srcSaleReport.getCurrencyOid();
            Vector listCurrencyType = PstCurrencyType.list(0, 0, whereClause, "");
            CurrencyType currencyType = (CurrencyType)listCurrencyType.get(0);
            strCurrencyType = currencyType.getCode();
        }
        
    
        String result = ""
            + "<table border='1' width='100%' cellspacing='0' cellpadding='3'>"
                + "<tr align='left' valign='top'>" 
                    + "<td  valign='middle' colspan='3' align='center'>&nbsp;</td>" 
                + "</tr>"
                + "<tr align='left' valign='top'>"
                    + "<td valign='middle' colspan='3' class='command'>"
                        + "<b>"+textListTitleHeader[language][3]+" : "+location.getName()+"</b>"
                    + "</td>"
                + "</tr>"
                + "<tr align='left' valign='top'> "
                    + "<td valign='middle' colspan='3' class='command'>"
                        + "<b>"
                            + ""+textListTitleHeader[language][8]+" : "
                            + ""+Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")+""
                            + ""+Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy")+""
                        +"</b>"
                    + "</td>"
                + "</tr>"
                + "<tr align=\"left\" valign=\"top\">" 
                    + "<td valign='middle' colspan='3'>"
                        + "<b>"
                            + ""+textListTitleHeader[language][7]+" : ";
                            if(iSaleReportType==PstBillMain.TRANS_TYPE_CASH) {
                                result += ""+textListGlobal[language][3]+"" ;
                            }else{
                                result += ""+textListGlobal[language][4]+"";
                            }
        result +=""
                        + "</b>"
                    + "</td>" 
                + "</tr>"
                + "<tr align='left' valign='top'>" 
                    + "<td valign='middle' colspan='3'>"
                        + "<b>"
                            + ""+textListTitleHeader[language][10]+" : "
                            + ""+strCurrencyType+""
                        + "</b>"
                    + "</td>" 
                + "</tr>"
                + "<tr align='left' valign='top'>"
                    + "<td  valign='middle' colspan='3'>"
                        + drawList(language, records, 0, srcSaleReport, srcSaleReport.getCurrencyOid(),paymentDinamis)
                    + "</td>"
                + "</tr>"
            + "</table>";
  
        response.getWriter().write(result);
    }
}
