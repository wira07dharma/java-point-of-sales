/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.report.masterdata;

import com.dimata.common.entity.payment.PstPriceType;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lowagie.text.pdf.*;

import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.dimata.posbo.session.masterdata.SessMaterial;
import com.dimata.posbo.entity.search.SrcMaterial;
import com.dimata.posbo.form.search.FrmSrcMaterial;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.posbo.entity.masterdata.*;

//adding priceType
import com.dimata.common.entity.payment.PriceType;
import com.dimata.common.entity.payment.PstPriceType;

//form handler
import com.dimata.posbo.session.masterdata.SessMaterialPriceTag;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.util.Formater;

/**
 *
 * @author PT. Dimata
 */
public class PrintAllListPriceTagPdf extends HttpServlet {

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
    public static Font fontLsContent = new Font(Font.TIMES_NEW_ROMAN, 11);
    public static Font fontPrice = new Font(Font.TIMES_NEW_ROMAN, 26,Font.BOLD);
    public static Font fontLsBarcode = new Font(Font.TIMES_NEW_ROMAN, 11);

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
            Vector listPriceType = new Vector();
            int countList = 0;
            HttpSession session = request.getSession(true);
            //long oidMaterial = FRMQueryString.requestLong(request, "hidden_material_id");
            SrcMaterial srcMaterial = new SrcMaterial();
            //SessMaterial sessMaterial = new SessMaterial();
            //FrmSrcMaterial frmSrcMaterial = new FrmSrcMaterial(request, srcMaterial);
            try {
                srcMaterial = (SrcMaterial) session.getValue(SessMaterial.SESS_SRC_MATERIAL);
                long categoryId = srcMaterial.getCategoryId();
                
            Vector vectPriceTypeId = new Vector(1, 1);
            String[] strPriceTypeId = request.getParameterValues(FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_PRICE_TYPE_ID]);
            if (strPriceTypeId != null && strPriceTypeId.length > 0) {
                for (int i = 0; i < strPriceTypeId.length; i++) {
                    try {
                        vectPriceTypeId.add(strPriceTypeId[i]);
                    } catch (Exception exc) {
                        System.out.println("err");
                    }
                }
                srcMaterial.setPriceTypeId(vectPriceTypeId);
                
            }
                if (srcMaterial == null) {
                    srcMaterial = new SrcMaterial();
                }
            } catch (Exception e) {
                //out.println(textListTitleHeader[SESS_LANGUAGE][3]);
                srcMaterial = new SrcMaterial();
            }
            
            try {

                //countList = SessMaterial.getCountSearch(srcMaterial);
                long priceTypeId = 0;
                String code = "";
                //list = SessMaterial.searchMaterial(srcMaterial, 0, 5000);
                listPriceType = SessMaterialPriceTag.listPriceType(srcMaterial);
                if ((listPriceType != null) && (listPriceType.size() > 0)) {
                    try {
                        for (int i = 0; i < listPriceType.size(); i++) {
                            PriceType priceType = (PriceType) listPriceType.get(i);
                            //Vector vctfrs = (Vector) listPriceType.get(i);
                            //PriceType priceType = (PriceType) vctfrs.get(0);
                            priceTypeId = priceType.getOID();
                            code = priceType.getCode();

                            document.add(getListHeaderPriceType(code));

                            list = SessMaterialPriceTag.searchMaterialPriceTag(srcMaterial, 0, 0, priceTypeId);

                            if ((list != null) && (list.size() > 0)) {
                                try {
                                    document.add(getContent(list, document, writer, listPriceType));
                                } catch (Exception e) {
                                    System.out.println("Exc search Material : " + e.toString());
                                    list = new Vector();
                                }
                            }
                        }

                    } catch (Exception e) {
                        System.out.println("Exc search Material : " + e.toString());
                        list = new Vector();
                    }
                }
            } catch (Exception e) {
                System.out.println("Exc search Material : " + e.toString());
                listPriceType = new Vector();
            }

            //Vector header = new Vector(1, 1);
            //Vector vctContent = new Vector(1, 1);

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
            table.addCell(new Phrase("DAFTAR PRINT LABEL HARGA", fontTitle));
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
        int ctnInt[] = {10, 10, 10};
        Table table = new Table(3);

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
            table.addCell(new Phrase("Price", fontListHeader));

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

    private static Table getListHeaderPriceType(String code) throws BadElementException, DocumentException {
        //int ctnInt[] = {3, 20, 10, 13, 8, 12, 10, 12, 12};
        //int ctnInt[] = {3, 10, 10, 12, 10, 5, 10};
        int ctnInt[] = {80};
        Table table = new Table(1);

        try {
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(100);
            table.setWidths(ctnInt);
            table.setBorderWidth(0);
            table.setCellpadding(1);
            table.setCellspacing(0);

            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_LEFT);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase("" + code, fontListHeader));




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

    private static Table getContent(Vector vct, Document document, PdfWriter writer, Vector vctPrc) throws BadElementException, DocumentException {
        Vector vHeader = (Vector) vct.get(0);
        Vector body = vct;
        int ctnInt[] = {10, 10, 10};
        Table table = new Table(3);
        

        try {
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(100);
            table.setWidths(ctnInt);
            table.setBorderWidth(1);
            table.setCellpadding(1);
            //table.setCellpadding(0);
            table.setCellspacing(0);

            boolean newPage = false;

            table.setDefaultCellBackgroundColor(Color.WHITE);
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_LEFT);

            if (body != null && body.size() > 0) {
                try {
                    for (int i = 0; i < body.size(); i = i + 3) {
                        //Material material = (Material)body.get(i);
                        Vector vctfrs = (Vector) body.get(i);
                        Material material = (Material) vctfrs.get(0);
                        Category category = (Category) vctfrs.get(1);
                        Merk merk = (Merk) vctfrs.get(3);
                        PriceTypeMapping priceTypeMapping = (PriceTypeMapping) vctfrs.get(4);

                        Material material2 = new Material();
                        Category category2 = new Category();
                        Merk merk2 = new Merk();
                        PriceTypeMapping priceTypeMapping2 = new PriceTypeMapping();

                        Material material3 = new Material();
                        Category category3 = new Category();
                        Merk merk3 = new Merk();
                        PriceTypeMapping priceTypeMapping3 = new PriceTypeMapping();

                        if (i + 1 < body.size()) {
                            Vector vctfrs2 = (Vector) body.get(i + 1);
                            material2 = (Material) vctfrs2.get(0);
                            category2 = (Category) vctfrs2.get(1);
                            merk2 = (Merk) vctfrs2.get(3);
                            priceTypeMapping2 = (PriceTypeMapping) vctfrs2.get(4);
                        }

                        if (i + 2 < body.size()) {
                            Vector vctfrs3 = (Vector) body.get(i + 2);
                            material3 = (Material) vctfrs3.get(0);
                            category3 = (Category) vctfrs3.get(1);
                            merk3 = (Merk) vctfrs3.get(3);
                            priceTypeMapping3 = (PriceTypeMapping) vctfrs3.get(4);
                        }
                        
           
                        table.setDefaultColspan(1);
                        
                        
                        table.addCell(new Phrase("" + material.getName(), fontLsContent));
                        table.addCell(new Phrase("" + material2.getName(), fontLsContent));
                        table.addCell(new Phrase("" + material3.getName(), fontLsContent));

                        table.addCell(new Phrase("Rp.         " + Formater.formatNumber(priceTypeMapping.getPrice(), "###,###"), fontPrice));
                        table.addCell(new Phrase("Rp.         " + Formater.formatNumber(priceTypeMapping2.getPrice(), "###,###"), fontPrice));
                        table.addCell(new Phrase("Rp.         " + Formater.formatNumber(priceTypeMapping3.getPrice(), "###,###"), fontPrice));

                        table.addCell(new Phrase("" + material.getBarCode(), fontLsContent));
                        table.addCell(new Phrase("" + material2.getBarCode(), fontLsContent));
                        table.addCell(new Phrase("" + material3.getBarCode(), fontLsContent));

                        table.setDefaultColspan(3);
                        table.addCell(new Phrase("", fontLsContent));
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

                            document.add(table);
                            document.newPage();
                            table = new Table(3);
                            table.setDefaultCellBackgroundColor(Color.WHITE);
                            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                            table.setDefaultVerticalAlignment(Table.ALIGN_LEFT);
                            table.setDefaultColspan(1);
                            table.setBorderColor(new Color(255, 255, 255));
                            table.setWidth(100);
                            table.setWidths(ctnInt);
                            table.setBorderWidth(0);
                            table.setCellpadding(1);
                            table.setCellspacing(0);

                            newPage = true;
                        } else {
                            /** hapus baris kosong */
                            table.deleteLastRow();
                            table.deleteLastRow();
                            table.deleteLastRow();
                            table.deleteLastRow();
                            table.deleteLastRow();
                            table.deleteLastRow();

                            newPage = false;
                        }

                    }

                    if (newPage == false) {
                    }
                } catch (Exception e) {
                    System.out.println("exc contenct" + e.toString());
                }
            }


        } catch (Exception exc) {
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
