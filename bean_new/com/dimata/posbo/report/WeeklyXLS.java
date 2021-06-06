/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Mar 26, 2004
 * Time: 9:22:09 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.report;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import org.apache.poi.hssf.usermodel.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.common.entity.location.Location;

public class WeeklyXLS extends HttpServlet {

    public static int periodetype = 0;
    /** Initializes the servlet.
    */
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {

        // get session value
        HttpSession session = request.getSession();
        Vector vt = (Vector) session.getValue("SESS_MAT_WEEKLY_XLS");

        Vector vtcat = (Vector)vt.get(0);
        Vector records = (Vector)vt.get(1);

        Category categ = (Category)vtcat.get(0);
        SubCategory subCateg = (SubCategory)vtcat.get(1);
        String strDate = (String)vtcat.get(2);
        Location location = (Location)vtcat.get(3);
        int type = Integer.parseInt(""+vtcat.get(4));
        String last = "";
        try{
            last = (String)vtcat.get(5);
        }catch(Exception e){}
        periodetype = type;

        //response.setContentType("text/html");
        //response.setContentType("application/vnd.ms-excel");
        response.setContentType("application/x-msexcel");
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("WEEKLY REPORT");
        //sheet.setAlternativeExpression(true); //
        //sheet.setColumnWidth((short)3,(short)(20));

        //System.out.println("col width :"+sheet.getColumnWidth((short)4));

        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFFont.BOLDWEIGHT_BOLD);

        HSSFCellStyle style = wb.createCellStyle();
        style.setFillBackgroundColor(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFont(font);

        HSSFCellStyle stylehd = wb.createCellStyle();
        stylehd.setFillBackgroundColor(HSSFCellStyle.SOLID_FOREGROUND);
        stylehd.setFillForegroundColor(HSSFCellStyle.SOLID_FOREGROUND);
        stylehd.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // Create a row and put some cells in it. Rows are 0 based.
        // ROW 1
        HSSFRow row = sheet.createRow((short) 0);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(location.getName().toUpperCase());
        cell.setCellStyle(style);

        // ROW 2
        row = sheet.createRow((short) (1));
        cell = row.createCell((short) 0);
        cell.setCellValue("REPORTING MONTHLY "+last.toUpperCase());
        cell.setCellStyle(style);

        cell = row.createCell((short) 11);
        if(type==0){
            cell.setCellValue("PERIODE I");
        }else{
            cell.setCellValue("PERIODE II");
        }
        cell.setCellStyle(style);

        // ROW 3
        row = sheet.createRow((short) (2));
        cell = row.createCell((short) 0);
        cell.setCellValue("Category");

        cell = row.createCell((short) 2);
        cell.setCellValue(": "+categ.getName());

        cell = row.createCell((short) 11);
        cell.setCellValue("BULAN : "+strDate.toUpperCase());
        cell.setCellStyle(style);

        // ROW 4
        row = sheet.createRow((short) (3));
        cell = row.createCell((short) 0);
        cell.setCellValue("Sub Category");

        cell = row.createCell((short) 2);
        cell.setCellValue(": "+subCateg.getName());

        // create heater detail
        row = getHeaderDetail(sheet,stylehd);
        row = getWeeklyDetail(records,sheet,style);
        System.out.println("== >>>> row "+row.getRowNum());

        // last proses for write to xls
        ServletOutputStream sos = response.getOutputStream();
        wb.write(sos);
        sos.close();
    }

    /** gadnyana
     * method yang di gunakan untuk pembuatan header detail dari weekly report
     */
    public static HSSFRow getHeaderDetail(HSSFSheet sheet,HSSFCellStyle style){
        // ROW 5
        HSSFRow row = sheet.createRow((short) (4));
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("NO");
        cell.setCellStyle(style);

        cell = row.createCell((short) 1);
        cell.setCellValue("SKU");
        cell.setCellStyle(style);

        cell = row.createCell((short) 2);
        cell.setCellValue("NAMA");
        cell.setCellStyle(style);

        cell = row.createCell((short) 3);
        cell.setCellValue("HARGA JUAL");
        cell.setCellStyle(style);

        cell = row.createCell((short) 4);
        cell.setCellValue("STOCK TAKING");
        cell.setCellStyle(style);

        cell = row.createCell((short) 5);
        cell.setCellValue("BOQ");
        cell.setCellStyle(style);

        cell = row.createCell((short) 6);
        cell.setCellValue("IN(1)");
        cell.setCellStyle(style);

        cell = row.createCell((short) 8);
        cell.setCellValue("IN(2)");
        cell.setCellStyle(style);

        cell = row.createCell((short) 10);
        cell.setCellValue("SALES");
        cell.setCellStyle(style);

        if(periodetype==0){
            cell = row.createCell((short) 25);
            cell.setCellValue("TOTAL SALES");
            cell.setCellStyle(style);

            cell = row.createCell((short) 26);
            cell.setCellValue("TOTAL RETUR");
            cell.setCellStyle(style);

            cell = row.createCell((short) 27);
            cell.setCellValue("CORRECTION STOCK");
            cell.setCellStyle(style);

            cell = row.createCell((short) 28);
            cell.setCellValue("SISA");
            cell.setCellStyle(style);

            cell = row.createCell((short) 29);
            cell.setCellValue("REMAKS");
            cell.setCellStyle(style);

            row = sheet.createRow((short) (5));
            cell = row.createCell((short) 6);
            cell.setCellValue("Date");
            cell.setCellStyle(style);

            cell = row.createCell((short) 7);
            cell.setCellValue("Qty");
            cell.setCellStyle(style);

            cell = row.createCell((short) 8);
            cell.setCellValue("Date");
            cell.setCellStyle(style);

            cell = row.createCell((short) 9);
            cell.setCellValue("Qty");
            cell.setCellStyle(style);

            for(int k=1;k<=15;k++){
                cell = row.createCell((short) (9+k));
                cell.setCellValue(""+k);
                cell.setCellStyle(style);
            }
        }else{
            cell = row.createCell((short) 26);
            cell.setCellValue("TOTAL SALES");
            cell.setCellStyle(style);

            cell = row.createCell((short) 27);
            cell.setCellValue("TOTAL RETUR");
            cell.setCellStyle(style);

            cell = row.createCell((short) 28);
            cell.setCellValue("CORRECTION STOCK");
            cell.setCellStyle(style);

            cell = row.createCell((short) 29);
            cell.setCellValue("SISA");
            cell.setCellStyle(style);

            cell = row.createCell((short) 30);
            cell.setCellValue("REMAKS");
            cell.setCellStyle(style);

            row = sheet.createRow((short) (5));
            cell = row.createCell((short) 6);
            cell.setCellValue("Date");
            cell.setCellStyle(style);

            cell = row.createCell((short) 7);
            cell.setCellValue("Qty");
            cell.setCellStyle(style);

            cell = row.createCell((short) 8);
            cell.setCellValue("Date");
            cell.setCellStyle(style);

            cell = row.createCell((short) 9);
            cell.setCellValue("Qty");
            cell.setCellStyle(style);

            for(int k=1;k<=16;k++){
                cell = row.createCell((short) (9+k));
                cell.setCellValue(""+(15+k));
                cell.setCellStyle(style);
            }
        }
        return row;
    }

    /**gadnyana
     * Method yang di gunakan loop item weeky
     */
    public static HSSFRow getWeeklyDetail(Vector objectClass,HSSFSheet sheet,HSSFCellStyle style){
        HSSFRow row = null;
        try{
            if(objectClass.size()>0){
                double totqty = 0;
                for(int j=0;j<objectClass.size();j++){
                    Vector vt = (Vector)objectClass.get(j);
                    Material mat = (Material)vt.get(0);
                    Unit unt = (Unit)vt.get(1);
                    MaterialStock matStock = (MaterialStock)vt.get(2);

                    Vector rowx = new Vector();

                    row = sheet.createRow((short) (6+j));
                    HSSFCell cell = row.createCell((short) 0);
                    cell.setCellValue(String.valueOf(j+1));

                    cell = row.createCell((short) 1);
                    cell.setCellValue(mat.getSku());

                    cell = row.createCell((short) 2);
                    cell.setCellValue(mat.getName());

                    cell = row.createCell((short) 3);
                    cell.setCellValue(mat.getDefaultPrice());

                    cell = row.createCell((short) 5);
                    cell.setCellValue(matStock.getOpeningQty());

                    totqty = totqty + matStock.getOpeningQty();
                }
                row = getLastHeader(totqty,row.getRowNum(),sheet, style);
            }
        }catch(Exception e){
            System.out.println("=>>> ERROR GET DETAIL WEEKLY :"+e.toString());
        }
        return row;
    }

    public static HSSFRow getLastHeader(double totQty,int maxrow,
                                     HSSFSheet sheet,HSSFCellStyle style){
        HSSFRow row = sheet.createRow((short) (maxrow+1));
        HSSFCell cell = row.createCell((short) 1);
        cell.setCellValue("TOTAL");

        cell = row.createCell((short) 5);
        cell.setCellValue(""+totQty);

        row = sheet.createRow((short) (maxrow+3));
        cell = row.createCell((short) 1);
        cell.setCellValue("Controller,");

        cell = row.createCell((short) 11);
        cell.setCellValue("Inventory,");

        cell = row.createCell((short) 22);
        cell.setCellValue("Supervisor,");

        row = sheet.createRow((short) (maxrow+6));
        cell = row.createCell((short) 1);
        cell.setCellValue("(...........................)");

        cell = row.createCell((short) 11);
        cell.setCellValue("(...........................)");

        cell = row.createCell((short) 22);
        cell.setCellValue("(...........................)");

        return row;
    }


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
}
