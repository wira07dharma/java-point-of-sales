/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.report.marketing;

import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.Merk;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMerk;
import com.dimata.qdep.form.FRMHandler;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Sunima
 */
public class PrintPriceListPdf extends HttpServlet{
    public void init(ServletConfig config) throws ServletException{
        super.init(config);
    }
    
    // destroy the servlet
    public void destroy(){
    }
    
    // setting the color values
    public static Color border = new Color(0x00, 0x00, 0x00);
    public static Color bgColor = new Color(220, 220, 220);
    // setting font for user choosen
    public static Font fontTitle = new Font(Font.TIMES_NEW_ROMAN, 14, Font.BOLD, border);
    public static Font fontMainHeader = new Font(Font.TIMES_NEW_ROMAN, 12, Font.BOLD, border);
    public static Font fontHeader = new Font(Font.TIMES_NEW_ROMAN, 12, Font.ITALIC, border);
    public static Font fontListHeader = new Font(Font.TIMES_NEW_ROMAN, 10, Font.BOLD, border);
    public static Font fontListContent = new Font(Font.TIMES_NEW_ROMAN, 10);
    
    // string head 
    public static final String textPriceListHeader[] = {
        "NO", "SKU", "NAMA & JENIS PRODUK", "MERK", "HARGA", "18x Angs.", "15x Angs.", "12x Angs.", "6x Angs.", "PRICE LIST PRODUCT"
    };
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Color bgColor = new Color(200, 200, 200);
        Rectangle rectangle = new Rectangle(20, 20, 20, 20);
        rectangle.rotate();
        Document document = new Document(PageSize.A4, 20, 20, 30, 30);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try{
            // creating instance pdf writer
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            // adding some metadata
            document.addSubject("This is a Subject");
            document.addSubject("this is a subject two");
            
            // add an footer
            HeaderFooter footer = new HeaderFooter(new Phrase(new Chunk("", fontListContent)), false);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setBorder(HeaderFooter.NO_BORDER);
            
            // open document
            document.open();
            
            // get data form session
            Vector list = new Vector();
            int countList = 0;
            int SESS_LANGUAGE = 0;
            HttpSession session = request.getSession(true);
            
            try{
                 list = PstCategory.list(0, 0, "", "");
            }catch(Exception e){
            
            }
            
            // get image url
            String pathImage = "http://" + request.getServerName() + ":" + request.getServerPort() +"/"+request.getContextPath()+ "/images/company_pdf.jpg";
            
            com.lowagie.text.Image gambar = null;
            try{
                gambar = Image.getInstance(pathImage);
            }catch(Exception e){
            
            }
            // Take data from pos Category
                document.add(getHeaderImage(gambar));
                document.add(getContent(list, document, writer));
        }catch(Exception e){
        
        }
        
        // close document
        document.close();
        
        // bring to pdf
        response.setContentType("application/pdf");
        response.setContentLength(baos.size());
        ServletOutputStream out = response.getOutputStream();
        baos.writeTo(out);
        out.flush();
        
    }
   
    // make image header
 private static Table getHeaderImage(Image gambar)throws BadElementException, DocumentException {
		Table table = new Table(1);

		try {
			int ctnInt[] = {100};
			table.setBorderColor(new Color(255, 255, 255));
			table.setWidth(100);
			table.setWidths(ctnInt);
			table.setSpacing(1);
			table.setPadding(0);
			table.setDefaultCellBorder(Table.NO_BORDER);

			createEmptySpace(table, 10);

			//image in header
			gambar.setAlignment(Image.MIDDLE);
			gambar.scaleAbsolute(100, 100);
			table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
			table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
			table.addCell(new Phrase(new Chunk(gambar, 0, 0)));
                        
                        //sub title
			table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
			table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
			table.addCell(new Phrase(PrintPriceListPdf.textPriceListHeader[9],fontTitle));

			createEmptySpace(table, 2);

		} catch (Exception e) {
		}
		return table;
	}
 
 private static void createEmptySpace(Table table, int space) throws BadElementException, DocumentException {
		for (int i = 0; i < space; i++) {
			table.setDefaultCellBorder(Table.NO_BORDER);
			table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
			table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
			table.addCell(new Phrase("", fontListContent));
		}

	}
    // make table header
    private static Table getHeader(String name) throws BadElementException, DocumentException{
        
       
            
            // int ctnInt
            int ctnInt[] = {100};
            Table table = new Table(1);
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(100);
            table.setWidths(ctnInt);
            table.setCellpadding(1);
            table.setCellspacing(0);
            
            table.setDefaultCellBorder(table.NO_BORDER);
            table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.addCell(new Phrase(name, fontTitle));
            return table;
    }
    
      private static Table getListHeader() throws BadElementException, DocumentException {
        
        int ctnInt[] = {2, 4, 8, 6, 4, 4, 4, 4};
        Table table = new Table(8); 
        
        try {
            table.setBorderColor(new Color(255, 255, 255));
            table.setWidth(100);
            table.setWidths(ctnInt);
            
            table.setBorderWidth(0);
            table.setCellpadding(1);
            table.setCellspacing(0);
            table.setDefaultRowspan(2);

            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase(PrintPriceListPdf.textPriceListHeader[0], fontListHeader));

            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase(PrintPriceListPdf.textPriceListHeader[1], fontListHeader));

            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase(PrintPriceListPdf.textPriceListHeader[2], fontListHeader));

            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);           
            table.addCell(new Phrase(PrintPriceListPdf.textPriceListHeader[3], fontListHeader));
            
            table.setDefaultRowspan(1);
            table.setDefaultColspan(4);
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase(PrintPriceListPdf.textPriceListHeader[4], fontListHeader));
            
            table.setDefaultColspan(1);
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase(PrintPriceListPdf.textPriceListHeader[5], fontListHeader));
            
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase(PrintPriceListPdf.textPriceListHeader[6], fontListHeader));
            
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase(PrintPriceListPdf.textPriceListHeader[7], fontListHeader));
            
            table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
            table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
            table.setDefaultCellBackgroundColor(bgColor);
            table.addCell(new Phrase(PrintPriceListPdf.textPriceListHeader[8], fontListHeader));


        } catch (Exception e) {
            System.out.println("exc header" + e.toString());
        }

        return table;
    }
      
      private static Table getListFooter(Table table, Vector footer) throws BadElementException, DocumentException {
        try {
            
        } catch (Exception e) {
            System.out.println("exc footer" + e.toString());
        }

        return table;
    }
    
       private static Table getContent(Vector vct, Document document, PdfWriter writer) throws BadElementException, DocumentException {
      
           Vector body = vct;
           String hello  = "";
           Table table = new Table(1);
           
           try{
               for(int i = 0; i < body.size(); i++ ){
               Category category = (Category) body.get(i);
               
               long categoryOid = category.getOID();
               String categoryName = category.getName();
               
               if(categoryOid > 0){
                   
                   // getvalidationm categories adder
                     float adder = 0;
                     if(categoryOid == 20181231021L || categoryOid == 720577508051741365L){
                        adder = (float) 5/ (float) 100;
                     }else if(categoryOid == 720577508051725474L){
                           adder = (float) 15/ (float) 100;
                     }else{
                         adder = (float) 10/ (float) 100;
                    }
                     
                    
                        Vector materialData = new Vector();
               materialData = PstMaterial.list(0, 0, ""+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]+"="+categoryOid+"", "");                
               if(materialData.size() > 0){
                        document.add(getHeader(categoryName));
                        table = getListHeader();
                   try{
                   for(int m = 0; m < materialData.size(); m++){
               Material material = (Material) materialData.get(m);      
                    // get the price average
                    double avgPrice = material.getAveragePrice();
                    long merkId = material.getMerkId();
                    Merk merk = PstMerk.fetchExc(merkId);
                    String merkName = merk.getName();
                    
                    // bring to formulas
                        double price18 = ((avgPrice + (avgPrice * adder)) + ((avgPrice + (avgPrice * adder)) * ((float) 124/ (float) 100))) / 18;
                        double price15 = ((avgPrice + (avgPrice * adder)) + ((avgPrice + (avgPrice * adder)) * ((float) 117/ (float) 100))) / 15;
                        double price12 = ((avgPrice + (avgPrice * adder)) + ((avgPrice + (avgPrice * adder)) * ((float) 110/ (float) 100))) / 12;
                        double price6 = ((avgPrice + (avgPrice * adder)) + ((avgPrice + (avgPrice * adder)) * ((float) 1))) / 6;
                    //
                    table.setDefaultCellBackgroundColor(Color.WHITE);
                    table.setDefaultHorizontalAlignment(Table.ALIGN_CENTER);
                    table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                    table.addCell(new Phrase(String.valueOf(m + 1), fontListContent));
                    
                    
                    table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                    table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                    table.addCell(new Phrase(material.getSku(), fontListContent));
                    
                    table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                    table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                    table.addCell(new Phrase(material.getName(), fontListContent));
                    
                    table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                    table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                    table.addCell(new Phrase(merkName, fontListContent));
                    
                    table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                    table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                    table.addCell(new Phrase("Rp. "+FRMHandler.userFormatStringDecimal(price18), fontListContent));
                    
                    table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                    table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                    table.addCell(new Phrase("Rp. "+FRMHandler.userFormatStringDecimal(price15), fontListContent));
                   
                    table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                    table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                    table.addCell(new Phrase("Rp. "+FRMHandler.userFormatStringDecimal(price12), fontListContent));
                    
                    table.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
                    table.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
                    table.addCell(new Phrase("Rp. "+FRMHandler.userFormatStringDecimal(price6), fontListContent));
                    
                     if(!writer.fitsPage(table)){
                         /** hapus baris kosong */
                        table.deleteLastRow();
                        table.deleteLastRow();
                        table.deleteLastRow();
                        table.deleteLastRow();
                        
                        // add footer
                        document.add(getListFooter(table, body));
                        
                        // add new page
                        document.newPage();
                        table = getListHeader();
                    }
               }
                      
                    // validation content size
                    if(!writer.fitsPage(table)){
                         /** hapus baris kosong */
                        table.deleteLastRow();
                        table.deleteLastRow();
                        table.deleteLastRow();
                        table.deleteLastRow();
                        
                        // add footer
                        document.add(getListFooter(table, body));
                        
                        // add new page
                        document.newPage();
                        table = getListHeader();
                    }
               }catch(Exception e){
                   
               }
                     
               }   
               }
               if(i == 0){
                   
               document.add(table);
               }
           }
           //document.add(createEmptySpace(table, 0));
           }catch(Exception e){
           
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
