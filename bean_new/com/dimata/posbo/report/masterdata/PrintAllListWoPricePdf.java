/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.report.masterdata;

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

import com.dimata.posbo.session.masterdata.SessMaterial;
import com.dimata.posbo.entity.search.SrcMaterial;
import com.dimata.posbo.form.search.FrmSrcMaterial;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.posbo.entity.masterdata.*;

/**
 *
 * @author PT. Dimata
 */
public class PrintAllListWoPricePdf extends HttpServlet {

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
    public static Font fontTitle = new Font(Font.TIMES_NEW_ROMAN, 14, Font.BOLD, border);
    public static Font fontMainHeader = new Font(Font.TIMES_NEW_ROMAN, 12, Font.BOLD, border);
    public static Font fontHeader = new Font(Font.TIMES_NEW_ROMAN, 12, Font.ITALIC, border);
    public static Font fontListHeader = new Font(Font.TIMES_NEW_ROMAN, 10, Font.BOLD, border);
    public static Font fontLsContent = new Font(Font.TIMES_NEW_ROMAN, 10);

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Color bgColor = new Color(200, 200, 200);
        Rectangle rectangle = new Rectangle(20, 20, 20, 20);
        rectangle.rotate();
        Document document = new Document(PageSize.A4, 20, 20, 30, 30);
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
            //document.setFooter(footer);

            document.open();

            /* get data from session */
            Vector list = new Vector();
            int countList = 0;
            HttpSession session = request.getSession(true);
            //long oidMaterial = FRMQueryString.requestLong(request, "hidden_material_id");
            SrcMaterial srcMaterial = new SrcMaterial();
            //SessMaterial sessMaterial = new SessMaterial();
            FrmSrcMaterial frmSrcMaterial = null;
            try {
                srcMaterial = (SrcMaterial) session.getValue(SessMaterial.SESS_SRC_MATERIAL);

                if (srcMaterial == null) {
                    srcMaterial = new SrcMaterial();
                }
            } catch (Exception e) {
                //out.println(textListTitleHeader[SESS_LANGUAGE][3]);
                srcMaterial = new SrcMaterial();
            }
            try {
                countList = SessMaterial.getCountSearch(srcMaterial);
                list = SessMaterial.searchMaterial(srcMaterial, 0, 5000);
            } catch (Exception e) {
                System.out.println("Exc search Material : " + e.toString());
                list = new Vector();
            }

            Vector header = new Vector(1, 1);
            Vector vctContent = new Vector(1, 1);
            if ((list != null) && (list.size() > 0)) {
                /*header = (Vector)list.get(0);
                vctContent = (Vector)list.get(1);
                document.add(getHeader(header));*/
                //document.add(getContent(vctContent, document, writer));
                document.add(getContent(list, document, writer));

            }

        } catch (Exception e) {
            System.out.println("Exception list : " + e.toString());
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

            //int ctnInt[] = {13, 2, 80};
            int ctnInt[] = {100};
            Table table = new Table(1);
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(100);
            table.setWidths(ctnInt);
            table.setCellpadding(1);
            table.setCellspacing(0);

            //  nama company, alamat,telp
            //table.setDefaultColspan(3);
            //table.setDefaultCellBorder(Table.NO_BORDER);
            //table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            //table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase((String) vct.get(0), fontHeader));
            //table.addCell(new Phrase((String) vct.get(1), fontHeader));
            //table.addCell(new Phrase((String) vct.get(2), fontHeader));

            // judul report
            table.setDefaultCellBorder(table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase("DAFTAR CATALOG BARANG", fontTitle));
            //table.addCell(new Phrase("", fontTitle));

            // group
            //table.setDefaultColspan(1);
            //table.setDefaultCellBorder(table.NO_BORDER);
            //table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            //table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);

            // lokasi
            //table.addCell(new Phrase((String) vct.get(4), fontMainHeader));
            //table.addCell(new Phrase(":", fontTitle));
            // table.addCell(new Phrase((String) vct.get(5), fontMainHeader));

            // tanggal
            //String strTemp = (String) vct.get(6);
            //if (strTemp.length() > 0) {
            //table.addCell(new Phrase((String) vct.get(6), fontMainHeader));
            //table.addCell(new Phrase(":", fontTitle));
            // table.addCell(new Phrase((String) vct.get(7) + " " + (String) vct.get(8) + " " + (String) vct.get(9), fontMainHeader));
            // }

            // status
            // table.addCell(new Phrase((String) vct.get(10), fontMainHeader));
            // table.addCell(new Phrase(":", fontTitle));
            // table.addCell(new Phrase((String) vct.get(11), fontMainHeader));

            // mata uang
            //table.addCell(new Phrase((String) vct.get(12), fontMainHeader));
            //table.addCell(new Phrase(":", fontTitle));
            //table.addCell(new Phrase((String) vct.get(13), fontMainHeader));

            return table;
        }

        return new Table(1);
    }

    private static Table getListHeader() throws BadElementException, DocumentException {
        //int ctnInt[] = {3, 20, 10, 13, 8, 12, 10, 12, 12};
        //int ctnInt[] = {3, 10, 10, 12, 10, 5, 10};
        int ctnInt[] = {3, 7, 7, 24, 4};
        Table table = new Table(5);

        try {
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(100);
            table.setWidths(ctnInt);
            table.setBorderWidth(0);
            table.setCellpadding(1);
            table.setCellspacing(0);

            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase("No.", fontListHeader));

            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase("Kode", fontListHeader));

            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase("Barcode", fontListHeader));

            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase("Category / Sub Category / Name", fontListHeader));

            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase("Qty Stok", fontListHeader));

            //table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            //table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.setDefaultCellBackgroundColor(bgColor);
            //table.addCell(new Phrase((String) header.get(5), fontListHeader));

            //table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            //table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.setDefaultCellBackgroundColor(bgColor);
            //table.addCell(new Phrase((String) header.get(6), fontListHeader));


        } catch (Exception e) {
            System.out.println("exc header" + e.toString());
        }

        return table;
    }

    private static Table getListFooter(Table table, Vector footer) throws BadElementException, DocumentException {
        try {
            /** SUB TOTAL */
            //table.setDefaultColspan(5);
            // Title
            //table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            //table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase("Sub Total", fontListHeader));
            //table.setDefaultColspan(1);
            // Total Kredit
            //table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            //table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase((String) footer.get(0), fontListHeader));
            // Retur
            //table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            //table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase((String) footer.get(1), fontListHeader));
            // Telah Dibayar
            //table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            //table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase((String) footer.get(2), fontListHeader));
            // Saldo
            //table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            //table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase((String) footer.get(3), fontListHeader));
            /** GRAND TOTAL */
            //table.setDefaultColspan(5);
            // Title
            //table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            //table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase("Grand Total", fontListHeader));
            //table.setDefaultColspan(1);
            // Total Kredit
            //table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            //table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase((String) footer.get(4), fontListHeader));
            // Retur
            //table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            //table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase((String) footer.get(5), fontListHeader));
            // Telah Dibayar
            //table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            //table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase((String) footer.get(6), fontListHeader));
            // Saldo
            //table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
            //table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            //table.addCell(new Phrase((String) footer.get(7), fontListHeader));
        } catch (Exception e) {
            System.out.println("exc footer" + e.toString());
        }

        return table;
    }

    private static Table getContent(Vector vct, Document document, PdfWriter writer) throws BadElementException, DocumentException {
        Vector vHeader = (Vector) vct.get(0);
        document.add(getHeader(vHeader));

        //Vector vctContent = (Vector) vct.get(1);
        //Vector header = (Vector) vctContent.get(0);
        Vector body = vct;
        //Vector footer = (Vector) vctContent.get(2);

        Table table = getListHeader();

        boolean newPage = false;
        //double amount = 0;
        //double retur = 0;
        //double payment = 0;
        //double saldo = 0;
        //double subAmount = 0;
        //double subRetur = 0;
        //double subPayment = 0;
        //double subSaldo = 0;

        if (body != null && body.size() > 0) {
            try {
                for (int i = 0; i < body.size(); i++) {
                    //Material material = (Material)body.get(i);
                    Vector vctfrs = (Vector) body.get(i);
                    Material material = (Material) vctfrs.get(0);
                    Category category = (Category) vctfrs.get(1);
                    Merk merk = (Merk) vctfrs.get(3);

                    //amount = Double.parseDouble((String) vctfrs.get(5));
                    //retur = Double.parseDouble((String) vctfrs.get(6));
                    //payment = Double.parseDouble((String) vctfrs.get(7));
                    //saldo = Double.parseDouble((String) vctfrs.get(8));

                    //subAmount += amount;
                    //subRetur += retur;
                    //subPayment += payment;
                    // subSaldo += saldo;

                    table.setDefaultCellBackgroundColor(Color.WHITE);
                    table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
                    table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                    table.addCell(new Phrase(String.valueOf(i + 1), fontLsContent));

                    table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                    table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                    table.addCell(new Phrase(material.getSku(), fontLsContent));

                    //table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
                    table.setDefaultHorizontalAlignment(Table.LEFT);
                    table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                    table.addCell(new Phrase(material.getBarCode(), fontLsContent));

                   // table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
                    table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                    table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                    table.addCell(new Phrase(material.getName(), fontLsContent));

                    table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
                    table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                    table.addCell(new Phrase("", fontLsContent));

                    //String remark = (String) vctfrs.get(3);
                    //remark = remark.replaceAll("\r\n", " ");
                    //table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                    //table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                    //table.addCell(new Phrase(remark, fontLsContent));
                    //table.addCell(new Phrase((String) vctfrs.get(4), fontLsContent));

                    //table.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
                    //table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                    //table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(amount), fontLsContent));
                    //table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(retur), fontLsContent));
                    //table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(payment), fontLsContent));
                    //table.addCell(new Phrase(FRMHandler.userFormatStringDecimal(saldo), fontLsContent));

                    /** buat baris kosong */
                    table.setDefaultColspan(5);
                    table.addCell(new Phrase("", fontLsContent));
                    table.addCell(new Phrase("", fontLsContent));
                    table.addCell(new Phrase("", fontLsContent));
                    table.addCell(new Phrase("", fontLsContent));


                    table.setDefaultColspan(1);

                    if (!writer.fitsPage(table)) {
                        /** hapus baris kosong */
                        table.deleteLastRow();
                        table.deleteLastRow();
                        table.deleteLastRow();
                        table.deleteLastRow();

                        Vector tempFooter = new Vector();
                        /** Sub Total*/
                        //tempFooter.add(FRMHandler.userFormatStringDecimal(subAmount));
                        //tempFooter.add(FRMHandler.userFormatStringDecimal(subRetur));
                        //tempFooter.add(FRMHandler.userFormatStringDecimal(subPayment));
                        //tempFooter.add(FRMHandler.userFormatStringDecimal(subSaldo));
                        /** Grand Total */
                        //tempFooter.add((String) footer.get(0));
                        //tempFooter.add((String) footer.get(1));
                        //tempFooter.add((String) footer.get(2));
                        //tempFooter.add((String) footer.get(3));
                        document.add(getListFooter(table, tempFooter));

                        /** set variabel sub* = 0 untuk next page */
                        //subAmount = 0;
                        //subRetur = 0;
                        //subPayment = 0;
                        //subSaldo = 0;
                        document.newPage();
                        document.add(getHeader(vHeader));
                        table = getListHeader();

                        newPage = true;
                    } else {
                        /** hapus baris kosong */
                        table.deleteLastRow();
                        table.deleteLastRow();
                        table.deleteLastRow();
                        table.deleteLastRow();

                        newPage = false;
                    }

                }

                if (newPage == false) {
                    Vector tempFooter = new Vector();
                    /** Sub Total*/
                    //tempFooter.add(FRMHandler.userFormatStringDecimal(subAmount));
                    //tempFooter.add(FRMHandler.userFormatStringDecimal(subRetur));
                    //tempFooter.add(FRMHandler.userFormatStringDecimal(subPayment));
                    //tempFooter.add(FRMHandler.userFormatStringDecimal(subSaldo));
                    /** Grand Total */
                    //tempFooter.add((String) footer.get(0));
                    //tempFooter.add((String) footer.get(1));
                    //tempFooter.add((String) footer.get(2));
                    //tempFooter.add((String) footer.get(3));
                    //table = getListFooter(table, tempFooter);
                }
            } catch (Exception e) {
                System.out.println("exc contenct" + e.toString());
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
