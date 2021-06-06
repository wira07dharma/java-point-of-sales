/*
 * ApInvoiceReportPdf.java
 *
 * Created on May 22, 2007, 2:18 PM
 */

package com.dimata.posbo.report.arap;

/**
 *
 * @author  gwawan
 */
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.dimata.qdep.form.FRMHandler;

import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.session.billing.SessBilling;
import com.dimata.posbo.report.sale.SaleReportDocument;

public class ArInvoiceDetailReportPdf extends HttpServlet {
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
    // setting the color values
    public static Color border = new Color(0x00, 0x00, 0x00);
    public static Color bgColor = new Color(220, 220, 220);
    
    // setting some fonts in the color chosen by the user
    public static Font fontTitle = new Font(Font.TIMES_NEW_ROMAN, 13, Font.BOLD, border);
    public static Font fontMainHeader = new Font(Font.TIMES_NEW_ROMAN, 10, Font.BOLD, border);
    public static Font fontHeader = new Font(Font.TIMES_NEW_ROMAN, 10, Font.ITALIC, border);
    public static Font fontListHeader = new Font(Font.TIMES_NEW_ROMAN, 8, Font.BOLD, border);
    public static Font fontLsContent = new Font(Font.TIMES_NEW_ROMAN, 7);
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Color bgColor = new Color(200, 200, 200);
        Rectangle rectangle = new Rectangle(20, 20, 20, 20);
        rectangle.rotate();
        Document document = new Document(PageSize.A4.rotate(), 20, 20, 10, 0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            //step2.2: creating an instance of the writer
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            // step 3.1: adding some metadata to the document
            document.addSubject("This is a subject.");
            document.addSubject("This is a subject two.");
            
            //HeaderFooter header = new HeaderFooter(new Phrase("This is a header."), false);
            HeaderFooter footer = new HeaderFooter(new Phrase(new Chunk("", fontLsContent)), false);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setBorder(HeaderFooter.NO_BORDER);
            //document.setHeader(header);
            document.setFooter(footer);
            
            document.open();
            
            /* get data from session */
            Vector list = new Vector();
            HttpSession sess = request.getSession(true);
            try {
                list = (Vector) sess.getValue(SaleReportDocument.AR_INVOICE_PDF);
            } catch (Exception e) {
                System.out.println("Exc : " + e.toString());
                list = new Vector();
            }
            
            Vector header = new Vector(1, 1);
            Vector vctContent = new Vector(1, 1);
            if ((list != null) && (list.size() > 0)) {
                /*header = (Vector)list.get(0);
                vctContent = (Vector) list.get(1);
                document.add(getHeader(header));
                document.add(getContent(vctContent, document, writer));*/
                document.add(getContent(list, document, writer));
            }
            
        } catch (Exception e) {
            System.out.println("Exception e : " + e.toString());
        }
        
        // step 5: closing the document
        document.close();
        
        // we have written the pdfstream to a ByteArrayOutputStream,
        // now we are going to write this outputStream to the ServletOutputStream
        // after we have set the contentlength (see http://www.lowagie.com/iText/faq.html#msie)
        response.setContentType("application/pdf");
        response.setContentLength(baos.size());
        ServletOutputStream out = response.getOutputStream();
        baos.writeTo(out);
        out.flush();
    }
    
    
    /* this method make table header */
    private static Table getHeader(Vector vct) throws BadElementException, DocumentException {
        
        if (vct != null && vct.size() > 0) {
            
            int ctnInt[] = {10, 1, 89};
            Table table = new Table(3);
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(100);
            table.setWidths(ctnInt);
            table.setCellpadding(1);
            table.setCellspacing(0);
            
            //  nama company, alamat,telp
            table.setDefaultColspan(3);
            table.setDefaultCellBorder(Table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase((String)vct.get(0), fontHeader));
            table.addCell(new Phrase((String)vct.get(1), fontHeader));
            table.addCell(new Phrase((String)vct.get(2), fontHeader));
            
            // judul report
            table.setDefaultCellBorder(table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase((String)vct.get(3), fontTitle));
            table.addCell(new Phrase("", fontTitle));
            
            // group
            table.setDefaultColspan(1);
            table.setDefaultCellBorder(table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            
            // lokasi
            table.addCell(new Phrase((String)vct.get(4), fontMainHeader));
            table.addCell(new Phrase(":", fontTitle));
            table.addCell(new Phrase((String)vct.get(5), fontMainHeader));
            
            // tanggal
            String strTemp = (String)vct.get(6);
            if(strTemp.length() > 0) {
                table.addCell(new Phrase((String)vct.get(6), fontMainHeader));
                table.addCell(new Phrase(":", fontTitle));
                table.addCell(new Phrase((String)vct.get(7)+" "+(String)vct.get(8)+" "+(String)vct.get(9), fontMainHeader));
            }
            
            // status
            table.addCell(new Phrase((String)vct.get(10), fontMainHeader));
            table.addCell(new Phrase(":", fontTitle));
            table.addCell(new Phrase((String)vct.get(11), fontMainHeader));
            
            // mata uang
            table.addCell(new Phrase((String)vct.get(12), fontMainHeader));
            table.addCell(new Phrase(":", fontTitle));
            table.addCell(new Phrase((String)vct.get(13), fontMainHeader));
            
            return table;
        }
        
        return new Table(1);
    }
    
    
    private static Table getListHeader(Vector header) throws BadElementException, DocumentException {
        //int ctnInt[] = {3, 9, 6, 7, 5, 7, 5, 3, 5, 4, 7, 7, 6, 6, 7, 7, 7};
        int ctnInt[] = {3, 9, 6, 7, 5, 7, 5, 3, 5, 4, 7, 7, 6, 6, 7, 7, 7, 7};
        //Table table = new Table(17);
        Table table = new Table(18);
        try{
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(100);
            table.setWidths(ctnInt);
            table.setBorderWidth(0);
            table.setCellpadding(1);
            table.setCellspacing(0);
            
            table.setDefaultRowspan(2);
            
            // No
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(0), fontListHeader));
            
            // Nama Konsumen
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(1), fontListHeader));
            
            // Nomor Invoice
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(2),fontListHeader));
            
            // Keterangan
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(3), fontListHeader));
            
            // Tanggal
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(4), fontListHeader));
            
            table.setDefaultRowspan(1);
            table.setDefaultColspan(6);
            //table.setDefaultColspan(7);
            // Detail Item
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(5), fontListHeader));
            
            table.setDefaultRowspan(2);
            table.setDefaultColspan(1);
            // Total Kredit
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(6), fontListHeader));
            
            // Diskon
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(7), fontListHeader));
            
            // Pajak
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(8), fontListHeader));

            // Service
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(9), fontListHeader));
            
            // Retur
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            //table.addCell(new Phrase((String)header.get(9), fontListHeader));
            table.addCell(new Phrase((String)header.get(10), fontListHeader));
            
            // Telah Dibayar
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            //table.addCell(new Phrase((String)header.get(10), fontListHeader));
            table.addCell(new Phrase((String)header.get(11), fontListHeader));
            
            // Saldo Piutang
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            //table.addCell(new Phrase((String)header.get(11), fontListHeader));
            table.addCell(new Phrase((String)header.get(12), fontListHeader));
            
            table.setDefaultRowspan(1);
            table.setDefaultColspan(1);
            /** Kode Item */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            //table.addCell(new Phrase((String)header.get(12), fontListHeader));
            table.addCell(new Phrase((String)header.get(13), fontListHeader));
            
            /** Unit */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            //table.addCell(new Phrase((String)header.get(13), fontListHeader));
            table.addCell(new Phrase((String)header.get(14), fontListHeader));
            
            /** Harga */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            //table.addCell(new Phrase((String)header.get(14), fontListHeader));
            table.addCell(new Phrase((String)header.get(15), fontListHeader));
            
            /** Qty */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            //table.addCell(new Phrase((String)header.get(15), fontListHeader));
            table.addCell(new Phrase((String)header.get(16), fontListHeader));
            
            /** Diskon */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            //table.addCell(new Phrase((String)header.get(16), fontListHeader));
            table.addCell(new Phrase((String)header.get(17), fontListHeader));
            
            /** Total */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            //table.addCell(new Phrase((String)header.get(17), fontListHeader));
            table.addCell(new Phrase((String)header.get(18), fontListHeader));
            
        }catch(Exception e){
            System.out.println("exc header"+e.toString());
        }
        
        return table;
    }
    
    private static Table getListFooter(Table table, Vector footer) throws BadElementException, DocumentException {
        try {
            /** SUB TOTAL */
            table.setDefaultColspan(11);
            //table.setDefaultColspan(12);
            
            // Title
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase("Sub Total", fontListHeader));
            
            table.setDefaultColspan(1);
            
            // Total Kredit
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase((String)footer.get(0), fontListHeader));
            
            // Diskon
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase((String)footer.get(1), fontListHeader));
            
            // Pajak
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase((String)footer.get(2), fontListHeader));

            // Service
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase((String)footer.get(3), fontListHeader));
            
            // Retur
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase((String)footer.get(3), fontListHeader));
            table.addCell(new Phrase((String)footer.get(4), fontListHeader));
            
            // Telah Dibayar
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase((String)footer.get(4), fontListHeader));
            table.addCell(new Phrase((String)footer.get(5), fontListHeader));
            
            // Saldo
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase((String)footer.get(5), fontListHeader));
            table.addCell(new Phrase((String)footer.get(6), fontListHeader));

           
            
            /** GRAND TOTAL */
            table.setDefaultColspan(11);
            //table.setDefaultColspan(12);
            
            // Title
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase("Grand Total", fontListHeader));
            
            table.setDefaultColspan(1);
            
            // Total Kredit
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase((String)footer.get(6), fontListHeader));
            table.addCell(new Phrase((String)footer.get(7), fontListHeader));
            
            // Diskon
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase((String)footer.get(7), fontListHeader));
            table.addCell(new Phrase((String)footer.get(8), fontListHeader));
            
            // Pajak
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase((String)footer.get(8), fontListHeader));
            table.addCell(new Phrase((String)footer.get(9), fontListHeader));
            
            // Retur
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase((String)footer.get(9), fontListHeader));
            table.addCell(new Phrase((String)footer.get(10), fontListHeader));
            
            // Telah Dibayar
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase((String)footer.get(10), fontListHeader));
            table.addCell(new Phrase((String)footer.get(11), fontListHeader));
            
            // Saldo
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase((String)footer.get(11), fontListHeader));
            table.addCell(new Phrase((String)footer.get(12), fontListHeader));

            // Service
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase((String)footer.get(13), fontListHeader));
            
        }catch(Exception e){
            System.out.println("exc footer"+e.toString());
        }
        
        return table;
    }
    
    private static Table getContent(Vector vct, Document document, PdfWriter writer) throws BadElementException, DocumentException {
        document.add(getHeader((Vector)vct.get(0)));
        
        Vector vctContent = (Vector)vct.get(1);
        Vector header = (Vector)vctContent.get(0);
        Vector body = (Vector)vctContent.get(1);
        Vector footer = (Vector)vctContent.get(2);
        
        Table table = getListHeader(header);
        
        String where = "";
        int nomor = 1;
        int counter = 1;
        boolean newPage = false;
        
        long currencyId = 0;
        double rate = 0;
        
        double amount = 0;
        double diskon = 0;
        double pajak = 0;
        double retur = 0;
        double payment = 0;
        double saldo = 0;

        //adding service
        double service = 0;
        
        double subAmount = 0;
        double subDiskon = 0;
        double subPajak = 0;
        double subRetur = 0;
        double subPayment = 0;
        double subSaldo = 0;

        //adding service
        double subService = 0;
        
        if (body != null && body.size() > 0) {
            try {
                for (int i = 0; i < body.size(); i++) {
                    Vector listInvoiceDetail = new Vector(1,1);
                    Vector vctfrs = (Vector)body.get(i);
                    
                    /*currencyId = Long.parseLong((String)vctfrs.get(10));
                    rate = Double.parseDouble((String)vctfrs.get(11));
                    
                    amount = Double.parseDouble((String)vctfrs.get(5));
                    diskon = Double.parseDouble((String)vctfrs.get(6));
                    pajak = Double.parseDouble((String)vctfrs.get(7));
                    retur = Double.parseDouble((String)vctfrs.get(8));
                    payment = Double.parseDouble((String)vctfrs.get(9));
                    saldo = Double.parseDouble((String)vctfrs.get(10));
                    //service
                    service = Double.parseDouble((String)vctfrs.get(12));
                    listInvoiceDetail = (Vector)vctfrs.get(13);*/

                    currencyId = Long.parseLong((String)vctfrs.get(11));
                    rate = Double.parseDouble((String)vctfrs.get(12));

                    amount = Double.parseDouble((String)vctfrs.get(5));
                    diskon = Double.parseDouble((String)vctfrs.get(6));
                    pajak = Double.parseDouble((String)vctfrs.get(7));
                     //service
                    service = Double.parseDouble((String)vctfrs.get(8));
                    retur = Double.parseDouble((String)vctfrs.get(13));
                    payment = Double.parseDouble((String)vctfrs.get(9));
                    saldo = Double.parseDouble((String)vctfrs.get(10));

                    //listInvoiceDetail = (Vector)vctfrs.get(13);

                    listInvoiceDetail = (Vector)vctfrs.get(14);

                    subAmount += amount;
                    subDiskon += diskon;
                    subPajak += pajak;
                    subRetur += retur;
                    subPayment += payment;
                    subSaldo += saldo;
                    //service
                    subService += service;
                    
                    for(int j=0; j<listInvoiceDetail.size(); j++) {
                        Vector temp = (Vector)listInvoiceDetail.get(j);
                        Billdetail billDetail = (Billdetail)temp.get(0);
                        Unit unit = (Unit)temp.get(1);
                        
                        table.setDefaultCellBackgroundColor(Color.WHITE);
                        if(j == 0) {
                            table.setDefaultColspan(1);
                            // No
                            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
                            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                            table.addCell(new Phrase(String.valueOf(nomor), fontLsContent));
                            
                            // Nama Customer
                            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                            table.addCell(new Phrase((String)vctfrs.get(1), fontLsContent));
                            
                            // No Invoice
                            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
                            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                            table.addCell(new Phrase((String)vctfrs.get(2), fontLsContent));
                            
                            // Remark
                            String remark = (String)vctfrs.get(3);
                            remark = remark.replaceAll("\r\n", " ");
                            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                            table.addCell(new Phrase(remark, fontLsContent));
                            
                            // Tanggal
                            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                            table.addCell(new Phrase((String)vctfrs.get(4), fontLsContent));
                            
                            nomor++;
                        }
                        else {
                            //table.setDefaultColspan(5);
                            table.setDefaultColspan(6);
                            table.addCell(new Phrase("", fontLsContent));
                        }
                        
                        table.setDefaultColspan(1);
                        // Kode
                        table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                        table.addCell(new Phrase(billDetail.getSku(), fontLsContent));
                        
                        // Harga
                        table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                        if(currencyId == 0) {
                            table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(billDetail.getItemPrice() * rate), fontLsContent));
                        }
                        else {
                            table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(billDetail.getItemPrice()), fontLsContent));
                        }
                        
                        // Unit
                        table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
                        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                        table.addCell(new Phrase(unit.getCode(), fontLsContent));
                        
                        // Qty
                        table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                        table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(billDetail.getQty()), fontLsContent));
                        
                        // Diskon
                        table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                        if(currencyId == 0) {
                            table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(billDetail.getDisc() * rate), fontLsContent));
                        }
                        else {
                            table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(billDetail.getDisc()), fontLsContent));
                        }
                        
                        // Total
                        table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                        if(currencyId == 0) {
                            table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(billDetail.getTotalPrice() * rate), fontLsContent));
                        }
                        else {
                            table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(billDetail.getTotalPrice()), fontLsContent));
                        }
                        
                        if(j == 0) {
                            table.setDefaultColspan(1);
                            // Total Kredit
                            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                            table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(amount), fontLsContent));
                            
                            // Diskon
                            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                            table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(diskon), fontLsContent));
                            
                            // Pajak
                            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                            table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(pajak), fontLsContent));

                            // Service
                            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                            table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(service), fontLsContent));
                            
                            // Retur
                            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                            table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(retur), fontLsContent));
                            
                            // Telah Dibayar
                            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                            table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(payment), fontLsContent));
                            
                            // Saldo Piutang
                            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                            table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(saldo), fontLsContent));
                            
                        }
                        else {
                            table.setDefaultColspan(6);
                            table.addCell(new Phrase("", fontLsContent));
                        }
                        
                        /** buat baris kosong */
                        //table.setDefaultColspan(17);
                        table.setDefaultColspan(18);
                        table.addCell(new Phrase("", fontLsContent));
                        table.addCell(new Phrase("", fontLsContent));
                        table.addCell(new Phrase("", fontLsContent));
                        table.addCell(new Phrase("", fontLsContent));
                        table.addCell(new Phrase("", fontLsContent));
                        table.addCell(new Phrase("", fontLsContent));
                        //add new blank
                        //table.addCell(new Phrase("", fontLsContent));
                        
                        if (!writer.fitsPage(table)) {
                            /** hapus baris kosong */
                            table.deleteLastRow();
                            table.deleteLastRow();
                            table.deleteLastRow();
                            table.deleteLastRow();
                            table.deleteLastRow();
                            table.deleteLastRow();
                            
                            Vector tempFooter = new Vector();
                            
                            /** Sub Total*/
                            tempFooter.add(FRMHandler.userFormatStringDecimal(subAmount));
                            tempFooter.add(FRMHandler.userFormatStringDecimal(subDiskon));
                            tempFooter.add(FRMHandler.userFormatStringDecimal(subPajak));
                            //add footer
                            tempFooter.add(FRMHandler.userFormatStringDecimal(subService));
                            tempFooter.add(FRMHandler.userFormatStringDecimal(subRetur));
                            tempFooter.add(FRMHandler.userFormatStringDecimal(subPayment));
                            tempFooter.add(FRMHandler.userFormatStringDecimal(subSaldo));
                            
                            /** Grand Total */
                            tempFooter.add((String)footer.get(0));
                            tempFooter.add((String)footer.get(1));
                            tempFooter.add((String)footer.get(2));
                            tempFooter.add((String)footer.get(3));
                            tempFooter.add((String)footer.get(4));
                            tempFooter.add((String)footer.get(5));
                            tempFooter.add((String)footer.get(6));
                            
                            document.add(getListFooter(table, tempFooter));
                            
                            /** set variabel sub = 0 untuk next page */
                            subAmount = 0;
                            subDiskon = 0;
                            subPajak = 0;
                            subRetur = 0;
                            subPayment = 0;
                            subSaldo = 0;
                            subService = 0;
                            
                            document.newPage();
                            document.add(getHeader((Vector)vct.get(0)));
                            table = getListHeader(header);
                            
                            newPage = true;
                        }
                        else {
                            /** hapus baris kosong */
                            table.deleteLastRow();
                            table.deleteLastRow();
                            table.deleteLastRow();
                            table.deleteLastRow();
                            table.deleteLastRow();
                            table.deleteLastRow();
                            
                            newPage = false;
                        }
                        
                        counter++;
                    }
                }
                
                if(newPage == false) {
                    Vector tempFooter = new Vector();
                    /** Sub Total*/
                    tempFooter.add(FRMHandler.userFormatStringDecimal(subAmount));
                    tempFooter.add(FRMHandler.userFormatStringDecimal(subDiskon));
                    tempFooter.add(FRMHandler.userFormatStringDecimal(subPajak));
                    tempFooter.add(FRMHandler.userFormatStringDecimal(subService));
                    tempFooter.add(FRMHandler.userFormatStringDecimal(subRetur));
                    tempFooter.add(FRMHandler.userFormatStringDecimal(subPayment));
                    tempFooter.add(FRMHandler.userFormatStringDecimal(subSaldo));
                    
                    /** Grand Total */
                    tempFooter.add((String)footer.get(0));
                    tempFooter.add((String)footer.get(1));
                    tempFooter.add((String)footer.get(2));
                    tempFooter.add((String)footer.get(3));
                    tempFooter.add((String)footer.get(4));
                    tempFooter.add((String)footer.get(5));
                    tempFooter.add((String)footer.get(6));

                    
                    table = getListFooter(table, tempFooter);
                }
                
            }catch(Exception e){
                System.out.println("exc contenct"+e.toString());
            }
        }
        
        return table;
    }
    
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
}
