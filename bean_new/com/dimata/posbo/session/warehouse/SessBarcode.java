/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.warehouse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.Barcode;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.BarcodeEAN;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
 
public class SessBarcode {
 
 public static void main(String[] args) throws FileNotFoundException, DocumentException, IOException {
 
    Document document = new Document(new Rectangle(PageSize.A5));    
    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("D:/Diani Opiari/Java4s_BarCode_128.pdf"));    
        document.open();
        
//        document.add(new Paragraph("Diani Opiari"));
//        Barcode128 code128 = new Barcode128();
//        code128.setGenerateChecksum(true);
//        code128.setCode("1234554321");
//        document.add(code128.createImageWithBarcode(writer.getDirectContent(), null, null));
//        
//        document.add(new Paragraph("Diani Opiari"));
//        code128 = new Barcode128();
//        code128.setGenerateChecksum(true);
//        code128.setCode("1234554321");
//        document.add(code128.createImageWithBarcode(writer.getDirectContent(), null, null));
        
       PdfPTable table = new PdfPTable(2);
       table.setWidthPercentage(100);
       for (int i = 0; i < 2; i++) {
            table.addCell(createBarcode(writer, String.format("%08d", i)));
       }
       //document.add
       document.add(table);
       
       document.close();
        
    System.out.println("Document Generated...!!!!!!");
  }
 
  public static PdfPCell createBarcode(PdfWriter writer, String code) throws DocumentException, IOException {
    BarcodeEAN barcode = new BarcodeEAN();
    barcode.setCodeType(Barcode.EAN8);
    barcode.setCode(code);
    PdfPCell cell = null;//new PdfPCell();
    
    Paragraph p = new Paragraph("Student name");
    p.setAlignment("CENTER");
    //cell.addElement(p);
//    
//    p = new Paragraph("-");
//    p.setAlignment("CENTER");
//    cell.addElement(p);
    
    //cell.addElement(barcode.createImageWithBarcode(writer.getDirectContent(), null, null));
    
    cell.setPadding(10);
    return cell;
}
 
 
}