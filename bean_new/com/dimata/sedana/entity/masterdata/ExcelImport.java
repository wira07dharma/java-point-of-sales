/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.masterdata;

import com.dimata.util.blob.TextLoader;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 *
 * @author Gunadi
 */
public class ExcelImport {
    
    public static int MD_KREDIT_JENIS_KREDIT = 0;
    public static int MD_KREDIT_SUMBER_DANA = 1;
    public static int MD_KREDIT_KOLEKTIBILITAS_PEMBAYARAN = 2;
    public static int MD_KREDIT_PENJAMIN_KREDIT = 3;
    public static int MD_TABUNGAN_JENIS_ITEM = 4;
    public static int MD_TABUNGAN_AFILIASI = 5;
    public static int MD_TABUNGAN_MASTER_TABUNGAN = 6;
    
    public static final String[] modulName = {
        "Jenis Kredit",
        "Sumber Dana",
        "Kolektibilitas Pembayaran",
        "Penjamin Kredit",
        "Jenis Item",
        "Afiliasi",
        "Master Tabungan"
    };
    
    public static String importJenisKredit(ServletConfig config, HttpServletRequest request, HttpServletResponse response, JspWriter output){
        String html = "";
        int NUM_HEADER = 2;
        int NUM_CELL = 0;
        
        String tdColor = "#FFF;";
        
        try {
            TextLoader uploader = new TextLoader();
            ByteArrayInputStream inStream = null;

            uploader.uploadText(config, request, response);
            Object obj = uploader.getTextFile("file");
            byte byteText[] = null;
            byteText = (byte[]) obj;
            inStream = new ByteArrayInputStream(byteText);

            POIFSFileSystem fs = new POIFSFileSystem(inStream);

            HSSFWorkbook wb = new HSSFWorkbook(fs);
            System.out.println("creating workbook");

            int numOfSheets = wb.getNumberOfSheets();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            int errInsert = 0;
            for (int i = 0; i < numOfSheets; i++) {
                
            }
        } catch (Exception e) {
            System.out.println("---=== Error : ReadStream ===---\n" + e);
        }
        
        return null;
    }
    
}
