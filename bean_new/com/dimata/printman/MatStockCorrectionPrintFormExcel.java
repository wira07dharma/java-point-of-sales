/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.printman;

/**
 *
 * @author dimata005
 */

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
import com.dimata.common.entity.location.PstLocation.*;
import com.dimata.common.entity.location.Location.*;
import com.dimata.common.entity.contact.PstContactList.*;
/**
 *
 * @author opie-eyek 17022013
 */import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.Unit;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.dimata.posbo.entity.warehouse.MatStockOpname;
import com.dimata.posbo.entity.warehouse.MatStockOpnameItem;
import com.dimata.posbo.entity.warehouse.PstMatStockOpname;
import com.dimata.posbo.entity.warehouse.PstMatStockOpnameItem;
import com.dimata.qdep.entity.I_DocStatus;
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

/**
 *
 * @author Satrya Ramayu
 */
public class MatStockCorrectionPrintFormExcel  extends HttpServlet {

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
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
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

        int startItem = FRMQueryString.requestInt(request,"start_item");
        long oidStockOpname = FRMQueryString.requestLong(request, "hidden_opname_id");

        //fungsi untuk menampilkan query laporan koreksi stock opname
        MatStockOpname so = new MatStockOpname();

        try{
            so = PstMatStockOpname.fetchExc(oidStockOpname);
        }catch(Exception a){}

        //oidStockOpname = so.getOID();
        int recordToGetItem = 0;
        Vector listMatStockOpnameItem = PstMatStockOpnameItem.list(startItem,recordToGetItem,oidStockOpname);

        //menentukan lokasi
        Location loc1 = new Location();
        try{
            loc1 = PstLocation.fetchExc(so.getLocationId());
        }catch(Exception e){}

        //untuk nama sheet
        HSSFSheet sheet = wb.createSheet("Koreksi Stok " + so.getStockOpnameNumber());

        //sheet.createFreezePane( 0, 1, 0, 1 );

        HSSFRow row = sheet.createRow((short) 0);
        HSSFCell cell = row.createCell((short) 0);

        //int newsValue = 54666700;
        NumberFormat testFormat = NumberFormat.getCurrencyInstance(Locale.US);
        testFormat.setMinimumFractionDigits(0);
        String currency = "";
        String status ="";
        if(so.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT){
         status =(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
        }else if(so.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL){
         status =(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
        }else if(so.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
         status =(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
        }else if(so.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED){
         status =(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
        }

        String[] tableTitle = {
            "KOREKSI STOCK",
            "NO : " + so.getStockOpnameNumber(),
            "DATE : " + (so.getStockOpnameDate() != null ? Formater.formatDate(so.getStockOpnameDate(),"dd MMMM yyyy"): ""),
            "LOKASI : "+ loc1.getName(),
            "STATUS : "+ status
        };

            /*=============== HEADER TABLE ====================*/

            String[] tableHeader = new String[11];//+ (vSpesialLeaveSymbole.size() * 2) + vReason.size()];
            ///employee
            tableHeader[0] = "NO";
            tableHeader[1] = "SKU";
            tableHeader[2] = "NAMA";
            tableHeader[3] = "UNIT";
            tableHeader[4] = "QTY OPNAME";
            tableHeader[5] = "QTY SYSTEM";
            tableHeader[6] = "QTY SELISIH";
            tableHeader[7] = "COST";
            tableHeader[8] = "NILAI SELISIH";
            tableHeader[9] = "( + )";
            tableHeader[10] = "( - )";
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
            double selisihMinus=0.0;
            double selisihPlus=0.0;
            //print isi dari excel
            int start = 0;
            try{
                     for (int idxRecord = 0; idxRecord < listMatStockOpnameItem.size(); idxRecord++) {
                         Vector temp = (Vector)listMatStockOpnameItem.get(idxRecord);
                         MatStockOpnameItem soItem = (MatStockOpnameItem)temp.get(0);
                         Material mat = (Material)temp.get(1);
                         Unit unit = (Unit)temp.get(2);
                         start =start+1;
                         double qtyLost = (soItem.getQtyOpname() + soItem.getQtySold()) - soItem.getQtySystem();
                         int collPos = 0;
                      
                         row = sheet.createRow((short) (countRow));
                         countRow++;

                         //untuk no urut
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(start);
                         cell.setCellStyle(styleValue);
                         collPos++;

                         //collPos = "SKU";;
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(mat.getSku());
                         cell.setCellStyle(styleValue);
                         collPos++;

                         //collPos = "NAMA";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(mat.getName());
                         cell.setCellStyle(styleValue);
                         collPos++;

                         //collPos = UNIT
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(unit.getCode());
                         cell.setCellStyle(styleValue);
                         collPos++;

                         //collPos = "QTY OPNAME";;
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(soItem.getQtyOpname());
                         cell.setCellStyle(styleValue);
                         collPos++;

                         //collPos = "QTY SYSTEM";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(soItem.getQtySystem());
                         cell.setCellStyle(styleValue);
                         collPos++;

                         //collPos = "QTY SELISIH";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(qtyLost);
                         cell.setCellStyle(styleValue);
                         collPos++;

                         //collPos = "COST";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(soItem.getCost());
                         cell.setCellStyle(styleValue);
                         collPos++;

                        // collPos = "NILAI SELISIH";
                         cell = row.createCell((short) collPos);
                         cell.setCellValue(qtyLost * soItem.getCost());
                         cell.setCellStyle(styleValue);
                         collPos++;


                         double resultNilaiSelisih =qtyLost * soItem.getCost();
                         if(resultNilaiSelisih > 0 ){
                             selisihPlus=selisihPlus+resultNilaiSelisih;
                             cell = row.createCell((short) collPos);
                             cell.setCellValue(resultNilaiSelisih);
                             cell.setCellStyle(styleValue);
                             collPos++;

                             cell = row.createCell((short) collPos);
                             cell.setCellValue("0");
                             cell.setCellStyle(styleValue);
                             collPos++;
                         }else{
                            selisihMinus=selisihMinus+resultNilaiSelisih;
                            cell = row.createCell((short) collPos);
                            cell.setCellValue("0");
                            cell.setCellStyle(styleValue);
                            collPos++;

                            selisihMinus=selisihMinus+resultNilaiSelisih;
                            cell = row.createCell((short) collPos);
                            cell.setCellValue(resultNilaiSelisih);
                            cell.setCellStyle(styleValue);
                            collPos++;
                         }



                     }

             //untuk print footer nya
             String[] tableFooter = new String[11];
             tableFooter[0] = "";
             tableFooter[1] = "";
             tableFooter[2] = "";
             tableFooter[3] = "";
             tableFooter[4] = "";
             tableFooter[5] = "";
             tableFooter[6] = "";
             tableFooter[7] = "TOTAL LOST";
             try{
                  //String xx = FRMHandler.userFormatStringDecimal(PstMatStockOpnameItem.getLostQty(oidStockOpname));
                  //double yy = Double.parseDouble(xx)
                  tableFooter[8] = FRMHandler.userFormatStringDecimal(PstMatStockOpnameItem.getLostQty(oidStockOpname));
                  tableFooter[9] = FRMHandler.userFormatStringDecimal(selisihPlus);
                  tableFooter[10] = FRMHandler.userFormatStringDecimal(selisihMinus);
             }catch(Exception exc){
                  tableFooter[8] ="";
                  System.out.print("MatStockCorrectionPrintFormExcel Bermasalah");
             }

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
