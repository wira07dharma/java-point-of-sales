/*
 * ApInvoiceReportPdf.java
 *
 * Created on May 14, 2007, 13:01 PM
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
import com.dimata.posbo.entity.warehouse.PstMatReceiveItem;
import com.dimata.posbo.entity.warehouse.MatReceiveItem;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.qdep.form.FRMQueryString;

public class ApInvoiceWithItemReportPdf extends HttpServlet {
    
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
        Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 10);
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
            
            String approot = "";
            //update opie-eyek 28012013
             try {
                approot = FRMQueryString.requestString(request, "approot");

             } catch (Exception e) {
                    System.out.println("Exc : " + e.toString());
             }

            String pathImage = "http://" + request.getServerName() + ":" + request.getServerPort() + approot + "/images/company_pdf.jpg";
            System.out.println("approot = " + pathImage);
            com.lowagie.text.Image gambar = null;
            try {
                gambar = com.lowagie.text.Image.getInstance(pathImage);
            } catch (Exception ex) {
                System.out.println("gambar >>>>>> = " + gambar.getImageMask());
            }

            
            /* get data from session */
            Vector list = new Vector(1,1);
            HttpSession sess = request.getSession(true);
            try {
                list = (Vector) sess.getValue("REPORT_AP_PDF");
            } catch (Exception e) {
                System.out.println("Exc : " + e.toString());
                list = new Vector();
            }
            
            if ((list != null) && (list.size() > 0)) {
                document.add(getContent(list, document, writer,gambar));
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
    private static Table getHeader(Vector vct,com.lowagie.text.Image gambar) throws BadElementException, DocumentException {
        
        if (vct != null && vct.size() > 0) {
            
            int ctnInt[] = {100};
            Table table = new Table(1);
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(100);
            table.setWidths(ctnInt);
            table.setCellpadding(1);
            table.setCellspacing(0);
            
            // nama company, alamat,telp
            table.setDefaultCellBorder(Table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase("",fontTitle));
            table.addCell(new Phrase("",fontTitle));
            table.addCell(new Phrase("",fontTitle));
            table.addCell(new Phrase(new Chunk(gambar, 0, 0)));
            table.addCell(new Phrase((String)vct.get(0), fontHeader));
            table.addCell(new Phrase((String)vct.get(1), fontHeader));
            table.addCell(new Phrase((String)vct.get(2), fontHeader));
            
            // header, judul report, tanggal
            table.setDefaultCellBorder(table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase((String)vct.get(3), fontTitle));
            table.addCell(new Phrase((String)vct.get(4), fontTitle));
            table.addCell(new Phrase("", fontMainHeader));
            
            // group
            table.setDefaultCellBorder(table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase((String)vct.get(5), fontMainHeader));
            table.addCell(new Phrase((String)vct.get(6), fontMainHeader));
            
            return table;
        }
        
        return new Table(1);
    }
    
    
    private static Table getListHeader(Vector header) throws BadElementException, DocumentException {
        int ctnInt[] = {3, 9, 9, 8, 6, 7, 3, 5, 7, 9, 9, 7, 9, 9};
        Table table = new Table(14);
        try{
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(100);
            table.setWidths(ctnInt);
            table.setBorderWidth(0);
            table.setCellpadding(1);
            table.setCellspacing(0);
            
            table.setDefaultRowspan(2);
            /** No */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(0), fontListHeader));
            
            /** Nama Supplier */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(1), fontListHeader));
            
            /** Nomor Invoice */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(2),fontListHeader));
            
            /** Keterangan */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(3), fontListHeader));
            
            /** Tanggal Invoice */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(4), fontListHeader));
            
            table.setDefaultRowspan(1);
            table.setDefaultColspan(5);
            /** Detail Item */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(5), fontListHeader));
            
            table.setDefaultRowspan(2);
            table.setDefaultColspan(1);
            /** Total */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(6), fontListHeader));
            
            /** Pajak */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(7), fontListHeader));
            
            /** Pembayaran */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(8), fontListHeader));
            
            /** Saldo Hutang */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(9), fontListHeader));
            
            table.setDefaultRowspan(1);
            table.setDefaultColspan(1);
            /** Kode Item */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(10), fontListHeader));
            
            /** Unit */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(11), fontListHeader));
            
            /** Harga */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(12), fontListHeader));
            
            /** Qty */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(13), fontListHeader));
            
            /** Total */
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase((String)header.get(14), fontListHeader));
            
            
        }catch(Exception e){
            System.out.println("exc header"+e.toString());
        }
        
        return table;
    }
    
    private static Table getListFooter(Table table, Vector footer) throws BadElementException, DocumentException {
        try {
            /** SUB TOTAL */
            table.setDefaultColspan(10);
            
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase("Sub Total", fontListHeader));
            
            table.setDefaultColspan(1);
            
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase((String)footer.get(0), fontListHeader));
            
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase((String)footer.get(1), fontListHeader));
            
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase((String)footer.get(2), fontListHeader));
            
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase((String)footer.get(3), fontListHeader));
            
            
            /** GRAND TOTAL */
            table.setDefaultColspan(10);
            
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase("Grand Total", fontListHeader));
            
            table.setDefaultColspan(1);
            
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase((String)footer.get(4), fontListHeader));
            
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase((String)footer.get(5), fontListHeader));
            
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase((String)footer.get(6), fontListHeader));
            
            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase((String)footer.get(7), fontListHeader));
            
        }catch(Exception e){
            System.out.println("exc footer"+e.toString());
        }
        
        return table;
    }
    
    private static Table getContent(Vector vct, Document document, PdfWriter writer,com.lowagie.text.Image gambar) throws BadElementException, DocumentException {
        boolean newPage = false;
        
        Vector header = (Vector)vct.get(0);
        Vector vctContent = (Vector)vct.get(1);
        Vector footer = (Vector)vct.get(2);
        
        /** generate header report */
        document.add(getHeader(header,gambar));
        
        Vector tblHeader = (Vector)vctContent.get(0);
        Vector body = (Vector)vctContent.get(1);
        Table table = getListHeader(tblHeader);
        
        int nomor = 1;
        int counter = 1;
        double total = 0;
        double tax = 0;
        double payment = 0;
        double saldo = 0;
        double subTotal = 0;
        double subTax = 0;
        double subPayment = 0;
        double subSaldo = 0;
        
        String whereClause = "";
        
        if (body != null && body.size() > 0) {
            try{
                for (int i = 0; i < body.size(); i++) {
                    Vector listMatReceiveItem = new Vector(1,1);
                    Vector vctfrs = (Vector)body.get(i);
                    
                    total = Double.parseDouble((String)vctfrs.get(5));
                    tax = Double.parseDouble((String)vctfrs.get(6));
                    payment = Double.parseDouble((String)vctfrs.get(7));
                    saldo = Double.parseDouble((String)vctfrs.get(8));
                    listMatReceiveItem = (Vector)vctfrs.get(9);
                    
                    subTotal += total;
                    subTax += tax;
                    subPayment += payment;
                    subSaldo += saldo;
                    
                    if(listMatReceiveItem.size() > 0 ) {
                        for(int j=0; j<listMatReceiveItem.size(); j++) {
                            Vector tmp = (Vector)listMatReceiveItem.get(j);
                            MatReceiveItem recItem = (MatReceiveItem)tmp.get(0);
                            Material mat = (Material)tmp.get(1);
                            Unit unit = (Unit)tmp.get(2);
                            
                            table.setDefaultCellBackgroundColor(Color.WHITE);
                            
                            if(j==0) {
                                table.setDefaultColspan(1);
                                // No
                                table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
                                table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                                table.addCell(new Phrase(String.valueOf(nomor), fontLsContent));
                                
                                // Nama Supplier
                                table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                                table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                                table.addCell(new Phrase((String)vctfrs.get(1), fontLsContent));
                                
                                // Nomor Invoice
                                table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                                table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                                table.addCell(new Phrase((String)vctfrs.get(2), fontLsContent));
                                
                                // Tanggal Invoice
                                table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                                table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                                table.addCell(new Phrase((String)vctfrs.get(3), fontLsContent));
                                
                                // Keterangan
                                table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                                table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                                table.addCell(new Phrase((String)vctfrs.get(4), fontLsContent));
                                
                                nomor++;
                            }
                            else {
                                table.setDefaultColspan(5);
                                table.addCell(new Phrase("", fontLsContent));
                            }
                            
                            table.setDefaultColspan(1);
                            // Kode Item
                            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                            table.addCell(new Phrase(mat.getSku(), fontLsContent));
                            
                            // Unit
                            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
                            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                            table.addCell(new Phrase(unit.getCode(), fontLsContent));
                            
                            // Harga
                            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                            table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(recItem.getCost()), fontLsContent));
                            
                            // Qty
                            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                            table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(recItem.getQty()), fontLsContent));
                            
                            // Harga Total Item
                            table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                            table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(recItem.getTotal()), fontLsContent));
                            
                            if(j==0) {
                                table.setDefaultColspan(1);
                                // Total
                                table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                                table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                                table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(total), fontLsContent));
                                
                                // Tax
                                table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                                table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                                table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(tax), fontLsContent));
                                
                                // Payment
                                table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                                table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                                table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(payment), fontLsContent));
                                
                                // Saldo AP
                                table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                                table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                                table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(saldo), fontLsContent));
                            }
                            else {
                                table.setDefaultColspan(4);
                                table.addCell(new Phrase("", fontLsContent));
                            }
                            
                            /** buat baris kosong */
                            table.setDefaultColspan(14);
                            table.addCell(new Phrase("", fontLsContent));
                            table.addCell(new Phrase("", fontLsContent));
                            table.addCell(new Phrase("", fontLsContent));
                            table.addCell(new Phrase("", fontLsContent));
                            table.addCell(new Phrase("", fontLsContent));
                            table.addCell(new Phrase("", fontLsContent));
                            
                            if (!writer.fitsPage(table)) {
                                /** hapus baris kosong */
                                table.deleteLastRow();
                                table.deleteLastRow();
                                table.deleteLastRow();
                                table.deleteLastRow();
                                table.deleteLastRow();
                                table.deleteLastRow();
                                
                                Vector tempFooter = new Vector(1,1);
                                
                                /** Sub Total */
                                tempFooter.add(FRMHandler.userFormatStringDecimal(subTotal));
                                tempFooter.add(FRMHandler.userFormatStringDecimal(subTax));
                                tempFooter.add(FRMHandler.userFormatStringDecimal(subPayment));
                                tempFooter.add(FRMHandler.userFormatStringDecimal(subSaldo));
                                
                                /** Grand Total */
                                tempFooter.add((String)footer.get(0));
                                tempFooter.add((String)footer.get(1));
                                tempFooter.add((String)footer.get(2));
                                tempFooter.add((String)footer.get(3));
                                document.add(getListFooter(table, tempFooter));
                                
                                /** set variabel sub = 0 untuk next page */
                                subTotal = 0;
                                subTax = 0;
                                subPayment = 0;
                                subSaldo = 0;
                                
                                document.newPage();
                                document.add(getHeader(header,gambar));
                                table = getListHeader(tblHeader);
                                
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
                    else { //kondisi untuk ap tanpa detail
                        table.setDefaultColspan(1);
                        // No
                        table.setDefaultCellBackgroundColor(Color.WHITE);
                        table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
                        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                        table.addCell(new Phrase(String.valueOf(nomor), fontLsContent));
                        
                        // Nama Supplier
                        table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                        table.addCell(new Phrase((String)vctfrs.get(1), fontLsContent));
                        
                        // Nomor Invoice
                        table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                        table.addCell(new Phrase((String)vctfrs.get(2), fontLsContent));
                        
                        // Tanggal Invoice
                        table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                        table.addCell(new Phrase((String)vctfrs.get(3), fontLsContent));
                        
                        // Keterangan
                        table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                        table.addCell(new Phrase((String)vctfrs.get(4), fontLsContent));
                        
                        // Detail AP Item
                        table.setDefaultColspan(5);
                        table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                        table.addCell(new Phrase("", fontLsContent));
                        
                        table.setDefaultColspan(1);
                        // Total
                        table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                        table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(total), fontLsContent));
                        
                        // Tax
                        table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                        table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(tax), fontLsContent));
                        
                        // Payment
                        table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                        table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(payment), fontLsContent));
                        
                        // Saldo AP
                        table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                        table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                        table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(saldo), fontLsContent));
                        
                        /** buat baris kosong */
                        table.setDefaultColspan(14);
                        table.addCell(new Phrase("", fontLsContent));
                        table.addCell(new Phrase("", fontLsContent));
                        table.addCell(new Phrase("", fontLsContent));
                        table.addCell(new Phrase("", fontLsContent));
                        table.addCell(new Phrase("", fontLsContent));
                        table.addCell(new Phrase("", fontLsContent));
                        
                        if (!writer.fitsPage(table)) {
                            /** hapus baris kosong */
                            table.deleteLastRow();
                            table.deleteLastRow();
                            table.deleteLastRow();
                            table.deleteLastRow();
                            table.deleteLastRow();
                            table.deleteLastRow();
                            
                            Vector tempFooter = new Vector(1,1);
                            
                            /** Sub Total */
                            tempFooter.add(FRMHandler.userFormatStringDecimal(subTotal));
                            tempFooter.add(FRMHandler.userFormatStringDecimal(subTax));
                            tempFooter.add(FRMHandler.userFormatStringDecimal(subPayment));
                            tempFooter.add(FRMHandler.userFormatStringDecimal(subSaldo));
                            
                            /** Grand Total */
                            tempFooter.add((String)footer.get(0));
                            tempFooter.add((String)footer.get(1));
                            tempFooter.add((String)footer.get(2));
                            tempFooter.add((String)footer.get(3));
                            document.add(getListFooter(table, tempFooter));
                            
                            /** set variabel sub = 0 untuk next page */
                            subTotal = 0;
                            subTax = 0;
                            subPayment = 0;
                            subSaldo = 0;
                            
                            document.newPage();
                            document.add(getHeader(header,gambar));
                            table = getListHeader(tblHeader);
                            
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
                        nomor++;
                    }
                }
                
                /** menampilkan footer ketika report tidak satu halaman penuh */
                if(newPage ==  false) {
                    Vector tempFooter = new Vector(1,1);
                    
                    /** Sub Total */
                    tempFooter.add(FRMHandler.userFormatStringDecimal(subTotal));
                    tempFooter.add(FRMHandler.userFormatStringDecimal(subTax));
                    tempFooter.add(FRMHandler.userFormatStringDecimal(subPayment));
                    tempFooter.add(FRMHandler.userFormatStringDecimal(subSaldo));
                    
                    /** Grand Total */
                    tempFooter.add((String)footer.get(0));
                    tempFooter.add((String)footer.get(1));
                    tempFooter.add((String)footer.get(2));
                    tempFooter.add((String)footer.get(3));
                    
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
