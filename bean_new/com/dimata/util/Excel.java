/*
 * Excel.java
 *
 * Created on October 15, 2002, 5:17 PM
 */

package com.dimata.util;

import java.util.*;

import java.io.InputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;


import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.usermodel.*;

/**
 *
 * @author  karya
 * @version 
 */
public class Excel {

    private int NUM_CELL = 0;

    public static Hashtable listType = new Hashtable();

    public void setListType(String col, int type){
        listType.put(col,String.valueOf(type));
        System.out.println("listType : "+listType);
    }

    public static int getListType(String col){
        try{
            String str = (String)listType.get(col);
            return Integer.parseInt(str);
        }catch(Exception e){
            return -1;
        }
    }

    public int getNumberOfColumn(){
        return  NUM_CELL;
    }

    /** Creates new Excel */
    public Excel() {
    }

    public static void ReadWrite(String fin, String fout) throws IOException {
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fin));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row = sheet.getRow(2);
            HSSFCell cell = row.getCell((short)3);
            if (cell == null)
                cell = row.createCell((short)3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue("a test");

            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(fout);
            wb.write(fileOut);
            fileOut.close();
        }
        catch (Exception e) {
            System.out.println("---=== Error : ReadWrite ===---\n" + e);
        }
    }
    
    public static void Read(String fin) throws IOException {
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fin));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            
            int rows = sheet.getPhysicalNumberOfRows();
            System.out.println("\tgetNumberOfSheets = " + wb.getNumberOfSheets());
            System.out.println("\tgetPhysicalNumberOfRows = " + sheet.getPhysicalNumberOfRows());
            
            for (int r = 0; r < rows; r++) {
                HSSFRow row = sheet.getRow(r);
                int cells = row.getPhysicalNumberOfCells();
                System.out.println("\tgetPhysicalNumberOfCells = " + cells);
                
                System.out.println("ROW " + row.getRowNum());
                for (int c = 0; c < cells; c++){
                    HSSFCell cell  = row.getCell((short) c);
                    String   value = null;

                    switch (cell.getCellType()){
                        case HSSFCell.CELL_TYPE_FORMULA :
                            value = "FORMULA ";
                            break;

                        case HSSFCell.CELL_TYPE_NUMERIC :
                            value = "NUMERIC value="
                                    + cell.getNumericCellValue();
                            break;

                        case HSSFCell.CELL_TYPE_STRING :
                            value = "STRING value="
                                    + cell.getStringCellValue();
                            break;

                        default :
                    }
                    System.out.println("CELL col="
                                       + cell.getCellNum()
                                       + " VALUE=" + value);
                }
            }
        }
        catch (Exception e) {
            System.out.println("---=== Error : Read ===---\n" + e);
        }
    }
    
    public static Vector ReadStream(InputStream is) throws IOException {
        Vector result = new Vector();
        
        try {
            POIFSFileSystem fs = new POIFSFileSystem(is);
            System.out.println("mjdfgsdfgnsdfgsydfs");

            HSSFWorkbook wb = new HSSFWorkbook(fs);
            System.out.println("creating workbook");
            
            int numOfSheets = wb.getNumberOfSheets();
            System.out.println("numOfSheets = " + numOfSheets);
            
            for (int i=0; i<numOfSheets; i++) {
                int r = 0;
                HSSFSheet sheet = (HSSFSheet)wb.getSheetAt(i);
                System.out.println("creating sheet");
                int rows = sheet.getPhysicalNumberOfRows();
                System.out.println("counting number of rows");
                //System.out.println("\tgetNumberOfSheets = " + wb.getNumberOfSheets());
                //System.out.println("\tgetPhysicalNumberOfRows = " + sheet.getPhysicalNumberOfRows());

                // loop untuk row dimulai dari 3 (0, 1, 2 diabaikan)
                int start = (i == 0) ? 0 : 3;
                for (r=start; r < rows; r++) {
                    HSSFRow row = sheet.getRow(r);
                    int cells = row.getPhysicalNumberOfCells();
                    //System.out.println("\tgetPhysicalNumberOfCells = " + cells);
                    //System.out.println("ROW " + row.getRowNum());
                    for (int c = 0; c < cells; c++)
                    {
                        HSSFCell cell  = row.getCell((short) c);
                        String   value = null;
                        switch (cell.getCellType())
                        {
                            case HSSFCell.CELL_TYPE_FORMULA :
                                //value = "FORMULA ";
                                value = "";
                                break;
                            case HSSFCell.CELL_TYPE_NUMERIC :
                                //value = "NUMERIC value=" + cell.getNumericCellValue();
                                value = String.valueOf(cell.getNumericCellValue());
                                break;
                            case HSSFCell.CELL_TYPE_STRING :
                                //value = "STRING value=" + cell.getStringCellValue();
                                value = String.valueOf(cell.getStringCellValue());
                                break;
                            default :
                                value = ""+cell.getDateCellValue();
                        }
                        //System.out.println("CELL col=" + cell.getCellNum() + " VALUE=" + value);
                        result.addElement(value);
                    }//end of row
                } //end of sheet
            } //end of all sheets
            //return result;
        }
        catch (Exception e) {
            System.out.println("---=== Error : ReadStream ===---\n" + e);
        }
        return result;
    }

    // eka ...
    // params : Input Stream is stream from excel file
    //			numHeaderRow is number of row spend for header
    //			numcell is number of cell counted from row
   	//				numCell > 0, means cell for the type of file on excel is fixed
    //				numcell == 0 means system will check number of cell automaticaly, number of cell is
   	//				dinamyc for the kind of excel file
    //=============================================
    public Vector ReadStream(InputStream is, int numHeaderRow, int numCell) throws IOException {
        Vector result = new Vector();
        
        try {
            POIFSFileSystem fs = new POIFSFileSystem(is);
            System.out.println("mjdfgsdfgnsdfgsydfs");

            HSSFWorkbook wb = new HSSFWorkbook(fs);
            System.out.println("creating workbook");
            
            int numOfSheets = wb.getNumberOfSheets();
            System.out.println("numOfSheets = " + numOfSheets);
            
            for (int i=0; i<numOfSheets; i++) {
                int r = 0;
                HSSFSheet sheet = (HSSFSheet)wb.getSheetAt(i);
                System.out.println("creating sheet");
                int rows = sheet.getPhysicalNumberOfRows();
                System.out.println("counting number of rows");
                //System.out.println("\tgetNumberOfSheets = " + wb.getNumberOfSheets());
                //System.out.println("\tgetPhysicalNumberOfRows = " + sheet.getPhysicalNumberOfRows());

                // loop untuk row dimulai dari numHeaderRow (0, .. numHeaderRow diabaikan) => untuk yang bukan sheet pertaman
                int start = (i == 0) ? 0 : numHeaderRow;
                for (r=start; r < rows; r++) {
                    try{
	                    HSSFRow row = sheet.getRow(r);
	                    int cells = 0;
                        //if number of cell is static
                        if(numCell > 0){
                        	cells = numCell;
                        }
                        //number of cell is dinamyc
                        else{
                            cells = row.getPhysicalNumberOfCells();
                        }

                        // ambil jumlah kolom yang sebenarnya
                        NUM_CELL = cells;

	                    System.out.println("\tgetPhysicalNumberOfCells = " + cells);
	                    //System.out.println("ROW " + row.getRowNum());
	                    for (int c = 0; c < cells; c++)
	                    {
	                        HSSFCell cell  = row.getCell((short) c);
	                        String   value = null;
	                        switch (cell.getCellType())
	                        {
	                            case HSSFCell.CELL_TYPE_FORMULA :
	                                //value = "FORMULA ";
	                                value = "";
	                                break;
	                            case HSSFCell.CELL_TYPE_NUMERIC :
	                                //value = "NUMERIC value=" + cell.getNumericCellValue();
	                                value = String.valueOf(cell.getNumericCellValue());
	                                break;
	                            case HSSFCell.CELL_TYPE_STRING :
	                                //value = "STRING value=" + cell.getStringCellValue();
	                                value = String.valueOf(cell.getStringCellValue());
	                                break;
	                            default :
	                        }
	                        //System.out.println("CELL col=" + cell.getCellNum() + " VALUE=" + value);
	                        result.addElement(value);
	                    }//end of row
                    }
	                catch(Exception e){
	                    System.out.println("=> Can't get data ..sheet : "+i+", row : "+r+", => Exception e : "+e.toString());
	                }
	            } //end of sheet

            } //end of all sheets
            //return result;
        }
        catch (Exception e) {
            System.out.println("---=== Error : ReadStream ===---\n" + e);
        }
        return result;
    }
    
    public static Vector ReadStream(InputStream is, int numcol) throws IOException {
        Vector result = new Vector();
        
        try {
            POIFSFileSystem fs = new POIFSFileSystem(is);
            System.out.println("instreammmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            
            int rows = sheet.getPhysicalNumberOfRows();
            System.out.println("\tgetNumberOfSheets = " + wb.getNumberOfSheets());
            //System.out.println("\tgetPhysicalNumberOfRows = " + sheet.getPhysicalNumberOfRows());
            
            for (int r = 0; r < rows; r++) {
                HSSFRow row = sheet.getRow(r);

                int cells = row.getPhysicalNumberOfCells();
                //System.out.println("\tgetPhysicalNumberOfCells = " + cells);
                //System.out.println("ROW " + row.getRowNum());
                //for (int c = 0; c < cells; c++)
                for (int c = 0; c < numcol; c++)                {
                    try {
                        HSSFCell cell  = row.getCell((short) c);
                        int lType = getListType(String.valueOf(c));
                        System.out.println("lType=" +lType);
                        if(lType!=-1){
                            // cell.setCellType(lType);
                        }
                        String   value = null;
                        switch (cell.getCellType()){
                            case HSSFCell.CELL_TYPE_FORMULA :
                                //value = "FORMULA ";
                                value = "";
                                break;
                            case HSSFCell.CELL_TYPE_NUMERIC :
                                value = String.valueOf(cell.getNumericCellValue());
                                break;
                            case HSSFCell.CELL_TYPE_STRING :
                                value = String.valueOf(cell.getStringCellValue());
                                break;
                            default :
                        }
                        //System.out.println("instreammmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
                        //System.out.println("CELL col=" + cell.getCellNum() + " VALUE=" + value);
                        result.addElement(value);
                    }
                    catch (Exception e) {
                        //result.addElement("<font color=\"green\">-null-</font>");
                        result.addElement("");
                        System.out.println("\n\taddElement error : " + e);
                    }
                }
            }
            //return result;
        }
        catch (Exception e) {
            System.out.println("---=== Error : ReadStream ===---\n" + e);
        }
        return result;
    }

    public static Vector ReadStreamByCol(InputStream is, int numcol) throws IOException {
        Vector result = new Vector();

        try {
            POIFSFileSystem fs = new POIFSFileSystem(is);
            System.out.println("instreammmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);

            int rows = sheet.getPhysicalNumberOfRows();
            System.out.println("\tgetNumberOfSheets = " + wb.getNumberOfSheets());
            //System.out.println("\tgetPhysicalNumberOfRows = " + sheet.getPhysicalNumberOfRows());

            for (int r = 0; r < rows; r++) {
                HSSFRow row = sheet.getRow(r);

                int cells = row.getPhysicalNumberOfCells();
                //System.out.println("\tgetPhysicalNumberOfCells = " + cells);
                //System.out.println("ROW " + row.getRowNum());
                //for (int c = 0; c < cells; c++)
                for (int c = 0; c < numcol; c++)                {
                    try {
                        HSSFCell cell  = row.getCell((short) c);
                        int lType = getListType(String.valueOf(c));
                        System.out.println("lType=" +lType);
                        if(lType!=-1){
                            // cell.setCellType(lType);
                        }
                        String   value = null;
                        switch (cell.getCellType()){
                            case HSSFCell.CELL_TYPE_FORMULA :
                                //value = "FORMULA ";
                                value = "";
                                break;
                            case HSSFCell.CELL_TYPE_NUMERIC :
                                value = String.valueOf(cell.getNumericCellValue());
                                break;
                            case HSSFCell.CELL_TYPE_STRING :
                                value = String.valueOf(cell.getStringCellValue());
                                break;
                            default :
                                value = ""+cell.getDateCellValue();
                                System.out.println("val date >>>: "+cell.getDateCellValue());
                                break;
                        }
                        if(r > 0 && c==2){
                            //value = ""+cell.getDateCellValue();
                            result.addElement(cell.getDateCellValue());
                            System.out.println("val date >>>: "+cell.getDateCellValue());
                        }else{
                            result.addElement(value);
                        }

                        //System.out.println("instreammmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
                        //System.out.println("CELL col=" + cell.getCellNum() + " VALUE=" + value);
                        // result.addElement(value);
                    }
                    catch (Exception e) {
                        //result.addElement("<font color=\"green\">-null-</font>");
                        result.addElement("");
                        System.out.println("\n\taddElement error : " + e);
                    }
                }
            }
            //return result;
        }
        catch (Exception e) {
            System.out.println("---=== Error : ReadStream ===---\n" + e);
        }
        return result;
    }

    /**
     *
     * @param is
     * @param numcol
     * @param sheetNum
     * @param cellNum
     * @return
     * @throws IOException
     */
    public static Vector ReadStream(InputStream is, int numcol, int sheetNum,int cellNum) throws IOException {
        Vector result = new Vector();

        try {
            POIFSFileSystem fs = new POIFSFileSystem(is);
            System.out.println("instreammmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(sheetNum);

            int rows = sheet.getPhysicalNumberOfRows();
            System.out.println("\tgetNumberOfSheets = " + wb.getNumberOfSheets());
            //System.out.println("\tgetPhysicalNumberOfRows = " + sheet.getPhysicalNumberOfRows());

            for (int r = 0; r < rows; r++) {
                HSSFRow row = sheet.getRow(r);
                int cells = row.getPhysicalNumberOfCells();
                for (int c = 0; c < numcol; c++){
                    try {
                        HSSFCell cell  = row.getCell((short) c);
                        String   value = null;
                        switch (cell.getCellType()){
                            case HSSFCell.CELL_TYPE_FORMULA :
                                //value = "FORMULA ";
                                value = cell.getStringCellValue();
                                break;
                            case HSSFCell.CELL_TYPE_NUMERIC :
                                //value = "NUMERIC value=" + cell.getNumericCellValue();
                                value = String.valueOf(cell.getNumericCellValue());
                                break;
                            case HSSFCell.CELL_TYPE_STRING :
                                //value = "STRING value=" + cell.getStringCellValue();
                                value = String.valueOf(cell.getStringCellValue());
                                break;
                            default :
                        }
                        result.addElement(value);
                    }
                    catch (Exception e) {
                        result.addElement("");
                        System.out.println("\n\taddElement error : " + e);
                    }
                }
            }
            //return result;
        }
        catch (Exception e) {
            System.out.println("---=== Error : ReadStream ===---\n" + e);
        }
        return result;
    }

    /*
    public static int getNumOfColumn(InputStream is) throws IOException {
        int collumn =  0;
        try{
    		HSSFWorkbook wb = new HSSFWorkbook(is);
            HSSFSheet sheet = wb.getSheetAt(0);
            short row = 0;
           // short cel = (short)C;
            //HSSFCell cell = new HSSFCell(wb,sheet,row,row);
           // collumn = cell.getCellNum();
            return collumn;
        }catch(Exception exc){
            System.out.println(exc);
        }
        return 0;
    } */

}
