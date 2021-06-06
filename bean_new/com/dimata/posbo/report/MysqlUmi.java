/*
 * MysqlUmi.java
 *
 * Created on March 29, 2004, 11:19 PM
 */

package com.dimata.posbo.report;

/* java package */ 
import java.io.*; 
import java.util.*;
import java.util.Date;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.text.SimpleDateFormat;

/* qdep package */
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.qdep.form.*;
import com.dimata.util.*;

/**
 *
 * @author  Administrator
 * @version 
 */
public class MysqlUmi {

    public static final int WEEKLY_WH = 0;
    public static final int WEEKLY_STORE = 1;
    
    /** 
     * Untuk mencari laporan weekly Dbf
     * @param srcWeeklyDbf  
     * @created by Edhy
     * @return
     */
    synchronized public static Vector reportWeeklyDbf(int weeklyType, SrcWeeklyDbf srcWeeklyDbf){        
        Vector result = new Vector(1,1);
        DBResultSet dbrs = null;
        try{           
            boolean rslt = false;
            if(weeklyType==WEEKLY_WH){
                rslt = Dbfumi.processWeeklyDbfOnWh(srcWeeklyDbf);
            }else{
                rslt = Dbfumi.processWeeklyDbfOnStore(srcWeeklyDbf);                
            }    
            
            String sql = "SELECT SKU,"+ 
            " NAME,"+ 
            " UNIT,"+ 
            " SELL_PRICE,"+ 
            " SUM(QTY_AWAL+QTY_RECEIVE-QTY_TRANSFER-QTY_RETURN) AS QTY_AKHIR"+ 
            " FROM POS_WEEKLY_REPORT"+
            " GROUP BY SKU"+
            " ORDER BY SKU";
            
            System.out.println("reportWeeklyDbf SQL : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                Vector temp = new Vector(1,1);
                
                temp.add(String.valueOf(rs.getString(1)));
                temp.add(String.valueOf(rs.getString(2)));
                temp.add(String.valueOf(rs.getString(3)));
                temp.add(new Double(rs.getDouble(4)));
                temp.add(new Double(rs.getDouble(5)));
                
                result.add(temp);
            }           
            
            // empty table temporary
            deleteTemporaryTable("POS_WEEKLY_REPORT");
        }catch(Exception e){
            System.out.println("reportWeeklyDbf err >> "+e.toString());
        }
        return result;
    }


    /** 
     * delete data temporary
     * @param tableName
     * @created by Edhy
     */
    public static void deleteTemporaryTable(String tableName){
        try{            
            String sql = "DELETE FROM "+tableName;
            System.out.println("deleteTemporaryTable sql : "+sql);            
            DBHandler.execUpdate(sql);
        }catch(Exception e){
            System.out.println("deleteTemporaryTable err >> "+e.toString());
        }
    }

    
    public static void main(String[] args){    
        SrcWeeklyDbf srcWeeklyDbf = new SrcWeeklyDbf();
        srcWeeklyDbf.setKdKategori("1");
        srcWeeklyDbf.setKdSubKategori("001");
        srcWeeklyDbf.setTglPilih(new Date(103,2,31));
        srcWeeklyDbf.setTahun("2003");
        
        Vector hasil = reportWeeklyDbf(WEEKLY_WH,srcWeeklyDbf);
        
        if(hasil!=null && hasil.size()>0){
            for(int i=0; i<hasil.size(); i++){
                Vector temp = (Vector)hasil.get(i);
                System.out.print(String.valueOf(temp.get(0)));
                System.out.print(", "+String.valueOf(temp.get(1)));
                System.out.print(", "+String.valueOf(temp.get(2)));
                System.out.print(", "+((Double)temp.get(3)).doubleValue());
                System.out.println(", "+((Integer)temp.get(4)).intValue());
            }    
        }    
    }    

}
