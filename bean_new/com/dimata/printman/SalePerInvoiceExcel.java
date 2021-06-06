/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.printman;
/**
 *
 * @author dimata005
 */
import com.dimata.common.entity.contact.ContactList;
import com.dimata.posbo.entity.warehouse.MatStockOpnameItem.*;
import com.dimata.qdep.form.FRMHandler.*;
import com.dimata.qdep.form.FRMQueryString.*;
import com.dimata.qdep.entity.I_PstDocType.*;
import com.dimata.posbo.entity.warehouse.MatStockOpname.*;
import com.dimata.posbo.form.warehouse.FrmMatStockOpname.*;
import com.dimata.posbo.form.warehouse.CtrlMatStockOpname.*;
import com.dimata.gui.jsp.ControlLine.*;
import com.dimata.qdep.form.FRMMessage.*;
import com.dimata.posbo.entity.warehouse.PstMatStockOpnameItem.*;
import com.dimata.common.entity.contact.ContactList.*;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.location.PstLocation.*;
import com.dimata.common.entity.location.Location.*;
import com.dimata.common.entity.contact.PstContactList.*;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.billing.PstPendingOrder;
import com.dimata.pos.entity.masterCashier.CashMaster;
import com.dimata.pos.entity.masterCashier.PstCashMaster;
import com.dimata.pos.entity.payment.PstCashPayment;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */import com.dimata.posbo.entity.search.SrcSaleReport;

import com.dimata.posbo.report.sale.SaleReportDocument;
import com.dimata.qdep.form.FRMHandler;

import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Formater;
import java.text.NumberFormat;
import javax.servlet.*;
import javax.servlet.http.*;

import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
//import javax.servlet.http.HttpSession;
/**
 *
 * @author Satrya Ramayu
 */
public class SalePerInvoiceExcel  extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

    }

    /** Destroys the servlet.
     */
    public void destroy() {
    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        //HttpSession sessionName = request.getSession(true);
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
         //HttpSession sessionName = request.getSession(true);
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }

    private static HSSFFont createFont(HSSFWorkbook wb, int size, int color, boolean isBold) {
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) size);
        font.setColor((short) color);
        if (isBold) {
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        }
        return font;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        System.out.println("---===| Excel Report |===---");

        response.setContentType("application/x-msexcel"); // excel 2010
        //response.setContentType("application/vnd.ms-excel");
        HSSFWorkbook wb = new HSSFWorkbook();


        //Style
        HSSFCellStyle styleTitle = wb.createCellStyle();
        styleTitle.setFillBackgroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        styleTitle.setFillForegroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        styleTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styleTitle.setFont(createFont(wb, 12, HSSFFont.COLOR_NORMAL /*HSSFFont.BLACK*/, true));

        HSSFCellStyle styleSubTitle = wb.createCellStyle();
        styleSubTitle.setFillBackgroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        styleSubTitle.setFillForegroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        styleSubTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styleSubTitle.setFont(createFont(wb, 10, HSSFFont.COLOR_NORMAL /*HSSFFont.BLACK*/, true));

        HSSFCellStyle styleHeader = wb.createCellStyle();
        styleHeader.setFillBackgroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());//HSSFCellStyle.GREY_25_PERCENT);
        styleHeader.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());//HSSFCellStyle.GREY_25_PERCENT);
        styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styleHeader.setFont(createFont(wb, 12, HSSFFont.BOLDWEIGHT_BOLD /*HSSFFont.BLACK*/, true));

        HSSFCellStyle styleHeaderBig = wb.createCellStyle();
        styleHeaderBig.setFillBackgroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());//HSSFCellStyle.GREY_25_PERCENT);
        styleHeaderBig.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());//HSSFCellStyle.GREY_25_PERCENT);
        styleHeaderBig.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styleHeaderBig.setFont(createFont(wb, 12, HSSFFont.COLOR_NORMAL /*HSSFFont.BLACK*/, true));

        HSSFCellStyle styleFooter = wb.createCellStyle();
        styleFooter.setFillBackgroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());//HSSFCellStyle.GREY_25_PERCENT);
        styleFooter.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());//HSSFCellStyle.GREY_25_PERCENT);
        styleFooter.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styleFooter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleFooter.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleFooter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleFooter.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleFooter.setFont(createFont(wb, 10, HSSFFont.COLOR_NORMAL /*HSSFFont.BLACK*/, true));

        HSSFCellStyle styleValueBold = wb.createCellStyle();
        styleValueBold.setFillBackgroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());//HSSFCellStyle.GREY_25_PERCENT);
        styleValueBold.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());//HSSFCellStyle.GREY_25_PERCENT);
        styleValueBold.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styleValueBold.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleValueBold.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleValueBold.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleValueBold.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleValueBold.setFont(createFont(wb, 12, HSSFFont.COLOR_NORMAL /*HSSFFont.BLACK*/, true));

        HSSFCellStyle styleValue = wb.createCellStyle();
        styleValue.setFillBackgroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        styleValue.setFillForegroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        styleValue.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styleValue.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleValue.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleValue.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleValue.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleValue.setFont(createFont(wb, 10, HSSFFont.COLOR_NORMAL /*HSSFFont.BLACK*/, false));

        //create by satrya 2013-01-03
        HSSFCellStyle formatterFloat = wb.createCellStyle();
        //HSSFDataFormat df = wb.createDataFormat();
        //formatterFloat.setDataFormat(df.getFormat("#0.##0"));
        formatterFloat.setFillBackgroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        formatterFloat.setFillForegroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        formatterFloat.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        formatterFloat.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        formatterFloat.setBorderTop(HSSFCellStyle.BORDER_THIN);
        formatterFloat.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        formatterFloat.setBorderRight(HSSFCellStyle.BORDER_THIN);
        formatterFloat.setFont(createFont(wb, 10, HSSFFont.COLOR_NORMAL /*HSSFFont.BLACK*/, false));

        //int startItem = FRMQueryString.requestInt(request,"start_item");
        //long oidStockOpname = FRMQueryString.requestLong(request, "hidden_opname_id");
        HttpSession session = request.getSession(true);
        SrcSaleReport srcSaleReport = new SrcSaleReport();
       
       // srcSaleReport = (SrcSaleReport)session.getValue(SaleReportDocument.SALE_REPORT_DOC);

        try {
                srcSaleReport = (SrcSaleReport) session.getValue(SaleReportDocument.SALE_REPORT_DOC);

                if (srcSaleReport == null) {
                    srcSaleReport = new SrcSaleReport();
                }
            } catch (Exception e) {
                //out.println(textListTitleHeader[SESS_LANGUAGE][3]);
               srcSaleReport = new SrcSaleReport();
            }



        int iSaleReportType = FRMQueryString.requestInt(request, "sale_type");
        
        //fungsi untuk menampilkan query laporan
        String startDate = Formater.formatDate(srcSaleReport.getDateFrom(),"yyyy-MM-dd 00:00:00");
        String endDate = Formater.formatDate(srcSaleReport.getDateTo(),"yyyy-MM-dd 23:59:59");
        String createstartDate = Formater.formatDate(srcSaleReport.getDateFrom(),"yyyyMMdd");
        response.setHeader("Content-Disposition","attachment;filename=rekap_perinvoice_"+createstartDate+".xls");
            
            String whereCash = "";
            String whereCredit = "";
            String where = "";//PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"'"+
			//" AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE;
        
            if(iSaleReportType==6){
                whereCash = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"'"+
                                    " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE+
                                    " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0";
                whereCredit = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"'"+
                                    " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE+
                                    " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1";
            }else{
                where = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"'"+
                                    " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE+
                                    " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+iSaleReportType;
            }

            if(iSaleReportType == 6){
                whereCash = whereCash  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE;
                whereCredit = whereCredit  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED;
            }

            if(iSaleReportType == PstCashPayment.CASH){
                    where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE;
            }else{
                    where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED;
            }

            if(srcSaleReport.getShiftId()!=0) {
                      where = where + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
                      whereCash = whereCash + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
                      whereCredit = whereCredit + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
            }

            if(srcSaleReport.getLocationId()!=0) {
                    where = where + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
                     whereCash = whereCash + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
                      whereCredit = whereCredit + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
            }       

            if(srcSaleReport.getCurrencyOid()!=0) {
                    where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+"="+srcSaleReport.getCurrencyOid();
                    whereCash = whereCash + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+"="+srcSaleReport.getCurrencyOid();
                    whereCredit = whereCredit + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+"="+srcSaleReport.getCurrencyOid();
            }

            //+perCashier
            if (srcSaleReport.getCashMasterId()!=0) {
                      where = where + " AND MSTR. " + PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID]+"="+srcSaleReport.getCashMasterId();
                       whereCash = whereCash + " AND MSTR. " + PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID]+"="+srcSaleReport.getCashMasterId();
                        whereCredit = whereCredit + " AND MSTR. " + PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID]+"="+srcSaleReport.getCashMasterId();
             }
//        if(iSaleReportType != 6 ){
//            where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+iSaleReportType;
//        }else{
//             where = where  + " AND ("+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 OR "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1)";
//        }
//        
//        if(iSaleReportType == PstCashPayment.CASH){
//                where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE;
//        }else{
//                where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED;
//        }
//
//        if(srcSaleReport.getShiftId()!=0) {
//                //where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
//                  where = where + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
//        }
//
//        if(srcSaleReport.getLocationId()!=0) {
//                //where = where + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
//                where = where + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
//        }
//
//        if(srcSaleReport.getCurrencyOid()!=0) {
//                where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+"="+srcSaleReport.getCurrencyOid();
//        }
//
//        //+perCashier
//        if (srcSaleReport.getCashMasterId()!=0) {
//                //where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"="+srcSaleReport.getCashMasterId();
//                  where = where + " AND MSTR. " + PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID]+"="+srcSaleReport.getCashMasterId();
//         }

        String order = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
        Vector records = new Vector();
        Vector recordsCredit = new Vector();
        //records = PstBillMain.listPerCashier(0, 0, where, order);
            if(iSaleReportType==6){
                records=PstBillMain.listPerCashier(0, 0, whereCash, order);
                recordsCredit=PstBillMain.listPerCashier(0, 0, whereCredit, order);
           }else{
               records=PstBillMain.listPerCashier(0, 0, where, order);
           }

        Location location = new Location();
        try{
                location = PstLocation.fetchExc(srcSaleReport.getLocationId());
        }
        catch(Exception e){
                location.setName("Semua toko");
        }

        String cashier = "All Cashier";
        if(srcSaleReport.getCashMasterId() != 0) {
                String whereClause = PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID]+"="+srcSaleReport.getCashMasterId();
                Vector listCashier = PstCashMaster.list(0, 0, whereClause, "");
                CashMaster cashMaster = (CashMaster)listCashier.get(0);
                cashier = ""+cashMaster.getCashierNumber();

        }

        //untuk nama sheet
        HSSFSheet sheet = wb.createSheet("Koreksi Stok ");

        //sheet.createFreezePane( 0, 1, 0, 1 );

        HSSFRow row = sheet.createRow((short) 0);
        HSSFCell cell = row.createCell((short) 0);

        //int newsValue = 54666700;
        NumberFormat testFormat = NumberFormat.getCurrencyInstance(Locale.US);
        testFormat.setMinimumFractionDigits(0);
        String currency = "";
        String status ="";

        

        String[] tableTitle = {
            "REPORT PENJUALAN PER INVOICE " ,
            "LOKASI : "+location.getName(),
            "DATE : "+Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")+" s/d "+Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy"),
            "TIPE : "+((iSaleReportType==PstBillMain.TRANS_TYPE_CASH) ? "Penjualan Cash" : iSaleReportType==PstBillMain.TRANS_TYPE_CREDIT ?  "Penjualan Kredit" : "Penjualan Cash Dan Kredit"),
            "KASIR : "+cashier
        };

            /*=============== HEADER TABLE ====================*/

            String[] tableHeader = new String[11];//+ (vSpesialLeaveSymbole.size() * 2) + vReason.size()];
            ///employee
            tableHeader[0] = "NO";
            tableHeader[1] = "NOMOR";
            tableHeader[2] = "TANGGAl";
            tableHeader[3] = "KONSUMEN";
            tableHeader[4] = "HPP";
            tableHeader[5] = "SALES NETTO";
            tableHeader[6] = "DISC";
            tableHeader[7] = "TAX";
            tableHeader[8] = "SERVICE";
            tableHeader[9] = "SALES BRUTO";
            tableHeader[10]= "DP DEDUCTION";
            /**
             *@DESC     :UNTUK COUNT ROW
             */
            int countRow = 0;

            /**
             * @DESC    : TITTLE
             */
            for (int k = 0; k < tableTitle.length; k++) {
                row = sheet.createRow((short) (countRow));
                countRow++;
                cell = row.createCell((short) 0);
                cell.setCellValue(tableTitle[k]);
                cell.setCellStyle(styleHeaderBig);
            }
           //sheetsetAutoFilter(CellRangeAddress.valueOf("A1:C5"));
           sheet.createFreezePane(0, 8, 0, 8);

           countRow = countRow + 2;
            /**
             * @DESC    : HEADER
             */
            row = sheet.createRow((short) (countRow));
            countRow++;
            for (int k = 0; k < tableHeader.length; k++) {
                cell = row.createCell((short) k);
                cell.setCellValue(tableHeader[k]);
                cell.setCellStyle(styleHeader);
            }
            //sheet.( 1, 0, 1, 0 );

            //print isi dari excel
            int start = 0;
            double bruto = 0;
            double diskon = 0;
            double pajak = 0;
            double servis = 0;
            double netto = 0;
            double dp = 0;
            double totalBruto = 0;
            double totalDisc = 0;
            double totalTax = 0;
            double totalService = 0;
            double totalNetto = 0;
            double totalDp = 0;
            double totalCost = 0;
            try{
                     for (int idxRecord = 0; idxRecord < records.size(); idxRecord++) {
                         BillMain billMain = (BillMain)records.get(idxRecord);
                         ContactList contactlist = new ContactList();
                            try	{
                                contactlist = PstContactList.fetchExc(billMain.getCustomerId());
                            }catch(Exception e) {
                                            //System.out.println("Contact not found ...");
                            }
                         
                         //Vector temp = (Vector)records.get(idxRecord);
                         //MatStockOpnameItem soItem = (MatStockOpnameItem)temp.get(0);
                         //Material mat = (Material)temp.get(1);
                         //Unit unit = (Unit)temp.get(2);
                         start =start+1;
                         //double qtyLost = (soItem.getQtyOpname() + soItem.getQtySold()) - soItem.getQtySystem();
                         int collPos = 0;

                         row = sheet.createRow((short) (countRow));
                         countRow++;

                         //untuk no urut
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(start);
                         cell.setCellStyle(styleValue);
                         collPos++;

                         //collPos = "NOMOR";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(billMain.getInvoiceNumber());
                         cell.setCellStyle(styleValue);
                         collPos++;

                         //collPos = "TANGGAl";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(Formater.formatDate(billMain.getBillDate(),"dd/MM/yyyy"));
                         cell.setCellStyle(styleValue);
                         collPos++;

                         //collPos = "KONSUMEN";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(contactlist.getPersonName());
                         cell.setCellStyle(styleValue);
                         collPos++;

                         //collPos = "HPP";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(billMain.getNotes());
                         cell.setCellStyle(styleValue);
                         collPos++;

                         if(srcSaleReport.getCurrencyOid() != 0) {
                            bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID());
                                            diskon = billMain.getDiscount();
                                            pajak = billMain.getTaxValue();
                                            servis = billMain.getServiceValue();
                                    }
                                    else {
                                            bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID()) * billMain.getRate();
                                            diskon = billMain.getDiscount() * billMain.getRate();
                                            pajak = billMain.getTaxValue() * billMain.getRate();
                                            servis = billMain.getServiceValue() * billMain.getRate();
                                    }
                                    netto = bruto - diskon + pajak + servis;
                                    dp = PstPendingOrder.getDpValue(billMain.getOID());



                         //collPos = "SALES NETTO";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(bruto);
                         cell.setCellStyle(styleValue);
                         collPos++;

                         //collPos = "DISC";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(diskon);
                         cell.setCellStyle(styleValue);
                         collPos++;

                         //collPos ="TAX";;
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(pajak);
                         cell.setCellStyle(styleValue);
                         collPos++;

                        // collPos ="SERVICE";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(servis);
                         cell.setCellStyle(styleValue);
                         collPos++;

                          // collPos ="SALES BRUTO";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(netto);
                         cell.setCellStyle(styleValue);
                         collPos++;

                          // collPos ="DP DEDUCTION";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(dp);
                         cell.setCellStyle(styleValue);
                         collPos++;

                         totalBruto += bruto;
                         totalDisc += diskon;
                         totalTax += pajak;
                         totalService += servis;
                         totalNetto += netto;
                         totalDp += dp;
                     }
                     
                     
                    for (int idxRecord = 0; idxRecord < recordsCredit.size(); idxRecord++) {
                         BillMain billMain = (BillMain)recordsCredit.get(idxRecord);
                         ContactList contactlist = new ContactList();
                            try	{
                                contactlist = PstContactList.fetchExc(billMain.getCustomerId());
                            }catch(Exception e) {
                                            //System.out.println("Contact not found ...");
                            }
                         
                         //Vector temp = (Vector)records.get(idxRecord);
                         //MatStockOpnameItem soItem = (MatStockOpnameItem)temp.get(0);
                         //Material mat = (Material)temp.get(1);
                         //Unit unit = (Unit)temp.get(2);
                         start =start+1;
                         //double qtyLost = (soItem.getQtyOpname() + soItem.getQtySold()) - soItem.getQtySystem();
                         int collPos = 0;

                         row = sheet.createRow((short) (countRow));
                         countRow++;

                         //untuk no urut
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(start);
                         cell.setCellStyle(styleValue);
                         collPos++;

                         //collPos = "NOMOR";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(billMain.getInvoiceNumber());
                         cell.setCellStyle(styleValue);
                         collPos++;

                         //collPos = "TANGGAl";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(Formater.formatDate(billMain.getBillDate(),"dd/MM/yyyy"));
                         cell.setCellStyle(styleValue);
                         collPos++;

                         //collPos = "KONSUMEN";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(contactlist.getPersonName());
                         cell.setCellStyle(styleValue);
                         collPos++;

                         //collPos = "HPP";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(billMain.getNotes());
                         cell.setCellStyle(styleValue);
                         collPos++;

                         if(srcSaleReport.getCurrencyOid() != 0) {
                            bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID());
                                            diskon = billMain.getDiscount();
                                            pajak = billMain.getTaxValue();
                                            servis = billMain.getServiceValue();
                                    }
                                    else {
                                            bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID()) * billMain.getRate();
                                            diskon = billMain.getDiscount() * billMain.getRate();
                                            pajak = billMain.getTaxValue() * billMain.getRate();
                                            servis = billMain.getServiceValue() * billMain.getRate();
                                    }
                                    netto = bruto - diskon + pajak + servis;
                                    dp = PstPendingOrder.getDpValue(billMain.getOID());



                         //collPos = "SALES NETTO";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(bruto);
                         cell.setCellStyle(styleValue);
                         collPos++;

                         //collPos = "DISC";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(diskon);
                         cell.setCellStyle(styleValue);
                         collPos++;

                         //collPos ="TAX";;
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(pajak);
                         cell.setCellStyle(styleValue);
                         collPos++;

                        // collPos ="SERVICE";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(servis);
                         cell.setCellStyle(styleValue);
                         collPos++;

                          // collPos ="SALES BRUTO";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(netto);
                         cell.setCellStyle(styleValue);
                         collPos++;

                          // collPos ="DP DEDUCTION";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(dp);
                         cell.setCellStyle(styleValue);
                         collPos++;

                         totalBruto += bruto;
                         totalDisc += diskon;
                         totalTax += pajak;
                         totalService += servis;
                         totalNetto += netto;
                         totalDp += dp;
                     }

             //untuk print footer nya
             String[] tableFooter = new String[11];
             tableFooter[0] = "";
             tableFooter[1] = "";
             tableFooter[2] = "";
             tableFooter[3] = "";
             tableFooter[4] = "TOTAL";
             tableFooter[5] = FRMHandler.userFormatStringDecimal(totalBruto);
             tableFooter[6] = FRMHandler.userFormatStringDecimal(totalDisc);
             tableFooter[7] = FRMHandler.userFormatStringDecimal(totalTax);
             tableFooter[8] = FRMHandler.userFormatStringDecimal(totalService);
             tableFooter[9] = FRMHandler.userFormatStringDecimal(totalNetto);
             tableFooter[10] =FRMHandler.userFormatStringDecimal(totalDp);

             countRow = countRow + 2;
             row = sheet.createRow((short) (countRow));
             countRow++;
             for (int k = 0; k < tableFooter.length; k++) {
                cell = row.createCell((short) k);
                cell.setCellValue(tableFooter[k]);
                cell.setCellStyle(styleHeader);
             }
            }catch( Exception exc){
                  System.out.print(exc);
            }

            sheet.setAutoFilter(CellRangeAddress.valueOf("A8:I8"));
            ServletOutputStream sos = response.getOutputStream();
            wb.write(sos);
            sos.close();
    }
}
