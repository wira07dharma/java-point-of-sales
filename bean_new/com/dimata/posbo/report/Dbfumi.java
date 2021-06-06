/*
 * Dbfumi.java
 *
 * Created on March 29, 2004, 2:34 PM
 */

package com.dimata.posbo.report;

/* java package */
import java.io.*;
import java.util.*;
import java.util.Date;
import java.sql.*;

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
public class Dbfumi {
    
    /**
     * Open local connection untuk fetch data dari database umi (.dbf)
     */
    public static Connection openKoneksiDbf() {
        Connection koneksi = null;
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            koneksi = DriverManager.getConnection("jdbc:odbc:possmuodbc");
        }
        catch(Exception exc) {
            System.out.println("Error open connection : " + exc);
        }
        return koneksi;
    }
    
    /*
     * Close local connection untuk fetch data dari database umi (.dbf)
     */
    public static void closeKoneksiDbf(Connection koneksi) {
        try {
            koneksi.close();
        }
        catch(Exception exc) {
            System.out.println("Error close connection : " + exc);
        }
    }
    
    /**
     * fetch data category used by search page
     * @created by Edhy
     * @param koneksi
     * @return
     */
    public static Vector getDataCategory(){
        Vector vectData = new Vector(1,1);
        Connection koneksi = null;
        try{
            koneksi = openKoneksiDbf();
            
            String sql = "SELECT MS_KATAG.KD_KATAG, MS_KATAG.NM_KATAG"+
            " FROM MS_KATAG";
            
            System.out.println("getDataCategory SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                Vector temp = new Vector(1,1);
                
                temp.add(rs.getString(1));
                temp.add(rs.getString(2));
                
                vectData.add(temp);
            }
        }catch(Exception e){
            System.out.println("getDataCategory Err >> "+e.toString());
        }finally{
            closeKoneksiDbf(koneksi);
            return vectData;
        }
    }
    
    /**
     * fetch data category used by search page
     * @created by Edhy
     * @param koneksi
     * @return
     */
    public static Vector getDataSubCategory(String categoryCode){
        Vector vectData = new Vector(1,1);
        Connection koneksi = null;
        try{
            koneksi = openKoneksiDbf();
            
            String sql = "SELECT MS_SBKTG.KD_SBKTG, MS_SBKTG.NM_SBKTG"+
            " FROM MS_KATAG INNER JOIN MS_SBKTG ON MS_KATAG.KD_KATAG = MS_SBKTG.KD_KATAG"+
            " WHERE (((MS_KATAG.KD_KATAG)='"+categoryCode+"'))";
            
            System.out.println("getDataSubCategory SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                Vector temp = new Vector(1,1);
                
                temp.add(rs.getString(1));
                temp.add(rs.getString(2));
                
                vectData.add(temp);
            }
        }catch(Exception e){
            System.out.println("getDataSubCategory Err >> "+e.toString());
        }finally{
            closeKoneksiDbf(koneksi);
            return vectData;
        }
    }
    
    /**
     * fetch data category used by search page
     * @created by Edhy
     * @param koneksi
     * @return
     */
    public static Vector getDataStore(){
        Vector vectData = new Vector(1,1);
        Connection koneksi = null;
        try{
            koneksi = openKoneksiDbf();
            
            String sql = "SELECT MS_TOKO.KD_TOKO, MS_TOKO.NM_TOKO" +
            " FROM MS_TOKO";
            
            System.out.println("getDataStore SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                Vector temp = new Vector(1,1);
                
                temp.add(rs.getString(1));
                temp.add(rs.getString(2));
                
                vectData.add(temp);
            }
        }catch(Exception e){
            System.out.println("getDataStore Err >> "+e.toString());
        }finally{
            closeKoneksiDbf(koneksi);
            return vectData;
        }
    }
    
    /**
     * fetch data from table transactian dbf (Receive,Dispatch,Return,BalanceLastYear)
     * and insert into to Mysql
     * @param srcWeeklyDbf
     * @created by Edhy
     * @return
     */
    public static boolean processWeeklyDbfOnWh(SrcWeeklyDbf srcWeeklyDbf){
        boolean result = false;
        Connection koneksi = null;
        
        try{
            koneksi = openKoneksiDbf();
        }catch(Exception exc){
            System.out.println("Koneksi ke Dbf Err >> " + exc);
        }
        
        result = processDataReceiveOnWh(koneksi,srcWeeklyDbf);
        if(result == true){
            result = processDataDispatchOnWh(koneksi,srcWeeklyDbf);
        }else{
            System.out.println("Process Data Receive On Wh fail!!!");
        }
        
        if(result == true){
            result = processDataReturnOnWh(koneksi,srcWeeklyDbf);
        }else{
            System.out.println("Process Data Dispatch On Wh fail!!!");
        }
        
        if(result == true){
            result = processDataBalaceLastYearOnWh(koneksi,srcWeeklyDbf);
        }else{
            System.out.println("Process Data Return On Wh fail!!!");
        }
        
        if(result == false){
            System.out.println("Process Data Balace Last Year On Wh fail!!!");
        }
        
        closeKoneksiDbf(koneksi);
        return result;
    }
    
    /**
     * fetch data from table transactian dbf (Receive,Dispatch,Return,BalanceLastYear)
     * and insert into to Mysql
     * @param srcWeeklyDbf
     * @created by Edhy
     * @return
     */
    public static boolean processWeeklyDbfOnStore(SrcWeeklyDbf srcWeeklyDbf){
        boolean result = false;
        Connection koneksi = null;
        
        try{
            koneksi = openKoneksiDbf();
        }catch(Exception exc){
            System.out.println("Koneksi ke Dbf Err >> " + exc);
        }
        
        result = processDataReceiveOnStore(koneksi,srcWeeklyDbf);
        if(result == true){
            result = processDataDispatchOnStore(koneksi,srcWeeklyDbf);
        }else{
            System.out.println("Process Data Receive On Store fail!!!");
        }
        
        if(result == true){
            result = processDataReturnOnStore(koneksi,srcWeeklyDbf);
        }else{
            System.out.println("Process Data Dispatch On Store fail!!!");
        }
        
        if(result == true){
            result = processDataBalaceLastYearOnStore(koneksi,srcWeeklyDbf);
        }else{
            System.out.println("Process Data Return On Store fail!!!");
        }
        
        if(result == false){
            System.out.println("Process Data Balace Last Year On Store fail!!!");
        }
        
        closeKoneksiDbf(koneksi);
        return result;
    }
    
    /**
     * fetch data from table transactian Receive on Warehouse (TR_TRMGD.DBF)
     * and insert into to Mysql
     * @param koneksi
     * @param srcWeeklyDbf
     * @created by Edhy
     */
    public static boolean processDataReceiveOnWh(Connection koneksi, SrcWeeklyDbf srcWeeklyDbf){
        boolean result = false;
        Vector vectData = new Vector(1,1);
        int intTahun = Integer.parseInt(srcWeeklyDbf.getTahun());
        Date tglAwalTahun = new Date(intTahun-1900,0,1);
        try{
            String sql = "SELECT MS_BRG.KD_BRG"+
            ", MS_BRG.NM_BRG"+
            ", MS_BRG.HG_JUAL"+
            ", MS_BRG.SATUAN"+
            ", TR_TRMGD.QTY" +
            " FROM MS_BRG"+
            " INNER JOIN (TR_TRMGH INNER JOIN TR_TRMGD ON TR_TRMGH.NO_BUKTI = TR_TRMGD.NO_BUKTI)"+
            " ON MS_BRG.KD_BRG = TR_TRMGD.KD_BRG"+
            " WHERE (((TR_TRMGH.TG_BUKTI) BETWEEN #"+Formater.formatDate(tglAwalTahun,"yyyy-MM-dd")+"#"+
            " AND #"+Formater.formatDate(srcWeeklyDbf.getTglPilih(),"yyyy-MM-dd")+"#))";
            
            if(srcWeeklyDbf.getKdKategori().length()>0){
                sql = sql + " AND MS_BRG.KD_KATAG"+"='"+srcWeeklyDbf.getKdKategori()+"'";
            }
            
            if(srcWeeklyDbf.getKdSubKategori().length()>0){
                sql = sql + " AND MS_BRG.KD_SBKTG"+"='"+srcWeeklyDbf.getKdSubKategori()+"'";
            }
            
            System.out.println("processDataReceiveOnWh SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                Vector temp = new Vector(1,1);
                
                temp.add(rs.getString(1));
                temp.add(rs.getString(2));
                temp.add(new Double(rs.getDouble(3)));
                temp.add(rs.getString(4));
                temp.add(new Double(rs.getDouble(5)));
                
                vectData.add(temp);
            }
            // insert to mysql
            insertDataReceive(vectData);
            
            result = true;
        }catch(Exception e){
            System.out.println("processDataReceiveOnWh Err >> "+e.toString());
        }
        return result;
    }
    
    
    /**
     * fetch data from table transactian Receive on Store (TR_TRMTD.DBF)
     * and insert into Mysql
     * @param koneksi
     * @param srcWeeklyDbf
     * @created by Edhy
     */
    public static boolean processDataReceiveOnStore(Connection koneksi, SrcWeeklyDbf srcWeeklyDbf){
        boolean result = false;
        Vector vectData = new Vector(1,1);
        int intTahun = Integer.parseInt(srcWeeklyDbf.getTahun());
        Date tglAwalTahun = new Date(intTahun-1900,0,1);
        try{
            String sql = "SELECT MS_BRG.KD_BRG"+
            ", MS_BRG.NM_BRG"+
            ", MS_BRG.HG_JUAL"+
            ", MS_BRG.SATUAN"+
            ", TR_TRMTD.QTY" +
            " FROM MS_BRG"+
            " INNER JOIN (TR_TRMTH INNER JOIN TR_TRMTD ON TR_TRMTH.NO_BUKTI = TR_TRMTD.NO_BUKTI)"+
            " ON MS_BRG.KD_BRG = TR_TRMTD.KD_BRG"+
            " WHERE (((TR_TRMTH.TG_BUKTI) BETWEEN #"+Formater.formatDate(tglAwalTahun,"yyyy-MM-dd")+"#"+
            " AND #"+Formater.formatDate(srcWeeklyDbf.getTglPilih(),"yyyy-MM-dd")+"#))";
            
            if(srcWeeklyDbf.getKdKategori().length()>0){
                sql = sql + " AND MS_BRG.KD_KATAG"+"='"+srcWeeklyDbf.getKdKategori()+"'";
            }
            
            if(srcWeeklyDbf.getKdSubKategori().length()>0){
                sql = sql + " AND MS_BRG.KD_SBKTG"+"='"+srcWeeklyDbf.getKdSubKategori()+"'";
            }
            
            System.out.println("processDataReceiveOnStore SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                Vector temp = new Vector(1,1);
                
                temp.add(rs.getString(1));
                temp.add(rs.getString(2));
                temp.add(new Double(rs.getDouble(3)));
                temp.add(rs.getString(4));
                temp.add(new Double(rs.getDouble(5)));
                
                vectData.add(temp);
            }
            
            //nsert data to Mysql
            insertDataReceive(vectData);
            
            result = true;
        }catch(Exception e){
            System.out.println("processDataReceiveOnStore Err >> "+e.toString());
        }
        return result;
    }
    
    /**
     * insert data receive from Dbf to Mysql
     * @param vectReceive
     * @created by Edhy
     */
    public static void insertDataReceive(Vector vectReceive){
        if(vectReceive!=null && vectReceive.size()>0){
            int maxReceive = vectReceive.size();
            for(int i=0; i<maxReceive; i++){
                Vector temp = (Vector)vectReceive.get(i);
                try{
                    
                    // for replace caracter if found "'", with "`"
                    String nm = String.valueOf(temp.get(1));
                    if (nm.compareToIgnoreCase("'") >= 0) {
                        nm = nm.replace('\'','`');
                    }
                    
                    String sql = "INSERT INTO POS_WEEKLY_REPORT VALUES("+
                    "'"+String.valueOf(temp.get(0))+"'"+
                    ",'"+nm+"'"+
                    ",'"+String.valueOf(temp.get(3))+"'"+
                    ","+((Double)temp.get(2)).doubleValue()+
                    ",0"+
                    ","+((Integer)temp.get(4)).intValue()+
                    ",0"+
                    ",0"+
                    ",0)";
                    
                    //System.out.println("insertDataReceive sql"+sql);
                    int status = DBHandler.execUpdate(sql);
                }catch(Exception e){
                    System.out.println("insertDataReceive err >>"+e.toString());
                }
            }
        }
    }
    
    
    /**
     * fetch data from table transactian Return on Warehouse (TR_RTRGD.DBF)
     * and insert into Mysql
     * @param koneksi
     * @param srcWeeklyDbf
     * @created by Edhy
     */
    public static boolean processDataReturnOnWh(Connection koneksi, SrcWeeklyDbf srcWeeklyDbf){
        boolean result = false;
        Vector vectData = new Vector(1,1);
        int intTahun = Integer.parseInt(srcWeeklyDbf.getTahun());
        Date tglAwalTahun = new Date(intTahun-1900,0,1);
        try{
            String sql = "SELECT MS_BRG.KD_BRG"+
            ", MS_BRG.NM_BRG"+
            ", MS_BRG.HG_JUAL"+
            ", MS_BRG.SATUAN"+
            ", TR_RTRGD.QTY" +
            " FROM MS_BRG"+
            " INNER JOIN (TR_RTRGH INNER JOIN TR_RTRGD ON TR_RTRGH.NO_BUKTI = TR_RTRGD.NO_BUKTI)"+
            " ON MS_BRG.KD_BRG = TR_RTRGD.KD_BRG"+
            " WHERE (((TR_RTRGH.TG_BUKTI) BETWEEN #"+Formater.formatDate(tglAwalTahun,"yyyy-MM-dd")+"#"+
            " AND #"+Formater.formatDate(srcWeeklyDbf.getTglPilih(),"yyyy-MM-dd")+"#))";
            
            if(srcWeeklyDbf.getKdKategori().length()>0){
                sql = sql + " AND MS_BRG.KD_KATAG"+"='"+srcWeeklyDbf.getKdKategori()+"'";
            }
            
            if(srcWeeklyDbf.getKdSubKategori().length()>0){
                sql = sql + " AND MS_BRG.KD_SBKTG"+"='"+srcWeeklyDbf.getKdSubKategori()+"'";
            }
            
            System.out.println("processDataReturnOnWh SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                Vector temp = new Vector(1,1);
                
                temp.add(rs.getString(1));
                temp.add(rs.getString(2));
                temp.add(new Double(rs.getDouble(3)));
                temp.add(rs.getString(4));
                temp.add(new Double(rs.getDouble(5)));
                
                vectData.add(temp);
            }
            
            // insert into Mysql
            insertDataReturn(vectData);
            
            result = true;
        }catch(Exception e){
            System.out.println("processDataReturnOnWh Err >> "+e.toString());
        }
        return result;
    }
    
    
    /**
     * fetch data from table transactian Return on Store (TR_RTRTD.DBF)
     * and insert into Mysql
     * @param koneksi
     * @param srcWeeklyDbf
     * @created by Edhy
     */
    public static boolean processDataReturnOnStore(Connection koneksi, SrcWeeklyDbf srcWeeklyDbf){
        boolean result = false;
        Vector vectData = new Vector(1,1);
        int intTahun = Integer.parseInt(srcWeeklyDbf.getTahun());
        Date tglAwalTahun = new Date(intTahun-1900,0,1);
        try{
            String sql = "SELECT MS_BRG.KD_BRG"+
            ", MS_BRG.NM_BRG"+
            ", MS_BRG.HG_JUAL"+
            ", MS_BRG.SATUAN"+
            ", TR_RTRTD.QTY" +
            " FROM MS_BRG"+
            " INNER JOIN (TR_RTRTH INNER JOIN TR_RTRTD ON TR_RTRTH.NO_BUKTI = TR_RTRTD.NO_BUKTI)"+
            " ON MS_BRG.KD_BRG = TR_RTRTD.KD_BRG"+
            " WHERE (((TR_RTRTH.TG_BUKTI) BETWEEN #"+Formater.formatDate(tglAwalTahun,"yyyy-MM-dd")+"#"+
            " AND #"+Formater.formatDate(srcWeeklyDbf.getTglPilih(),"yyyy-MM-dd")+"#))";
            
            if(srcWeeklyDbf.getKdKategori().length()>0){
                sql = sql + " AND MS_BRG.KD_KATAG"+"='"+srcWeeklyDbf.getKdKategori()+"'";
            }
            
            if(srcWeeklyDbf.getKdSubKategori().length()>0){
                sql = sql + " AND MS_BRG.KD_SBKTG"+"='"+srcWeeklyDbf.getKdSubKategori()+"'";
            }
            
            System.out.println("processDataReturnOnStore SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                Vector temp = new Vector(1,1);
                
                temp.add(rs.getString(1));
                temp.add(rs.getString(2));
                temp.add(new Double(rs.getDouble(3)));
                temp.add(rs.getString(4));
                temp.add(new Double(rs.getDouble(5)));
                
                vectData.add(temp);
            }
            // insert data to Mysql
            insertDataReturn(vectData);
            
            result = true;
        }catch(Exception e){
            System.out.println("processDataReturnOnStore Err >> "+e.toString());
        }
        return result;
    }
    
    /**
     * insert data return Store to Mysql
     * @param vectReceive
     * @created by Edhy
     */
    public static void insertDataReturn(Vector vectReturn){
        if(vectReturn!=null && vectReturn.size()>0){
            int maxReturn = vectReturn.size();
            for(int i=0; i<maxReturn; i++){
                Vector temp = (Vector)vectReturn.get(i);
                try{
                    
                    // for replace caracter if found "'", with "`"
                    String nm = String.valueOf(temp.get(1));
                    if (nm.compareToIgnoreCase("'") >= 0) {
                        nm = nm.replace('\'','`');
                    }
                    
                    String sql = "INSERT INTO POS_WEEKLY_REPORT VALUES("+
                    "'"+String.valueOf(temp.get(0))+"'"+
                    ",'"+nm+"'"+
                    ",'"+String.valueOf(temp.get(3))+"'"+
                    ","+((Double)temp.get(2)).doubleValue()+
                    ",0"+
                    ",0"+
                    ",0"+
                    ","+((Double)temp.get(4)).doubleValue()+
                    ",0)";
                    
                    //System.out.println("insertDataReturn sql"+sql);
                    int status = DBHandler.execUpdate(sql);
                }catch(Exception e){
                    System.out.println("insertDataReturn errr"+e.toString());
                }
            }
        }
    }
    
    /**
     * fetch data from table transactian Dispatch on Warehouse (TR_KLRGD.DBF)
     * and insert into Mysql
     * @param koneksi
     * @param srcWeeklyDbf
     * @created by Edhy
     */
    public static boolean processDataDispatchOnWh(Connection koneksi, SrcWeeklyDbf srcWeeklyDbf){
        boolean result = false;
        Vector vectData = new Vector(1,1);
        int intTahun = Integer.parseInt(srcWeeklyDbf.getTahun());
        Date tglAwalTahun = new Date(intTahun-1900,0,1);
        try{
            String sql = "SELECT MS_BRG.KD_BRG"+
            ", MS_BRG.NM_BRG"+
            ", MS_BRG.HG_JUAL"+
            ", MS_BRG.SATUAN"+
            ", TR_KLRGD.QTY" +
            " FROM MS_BRG"+
            " INNER JOIN (TR_KLRGH INNER JOIN TR_KLRGD ON TR_KLRGH.NO_BUKTI = TR_KLRGD.NO_BUKTI)"+
            " ON MS_BRG.KD_BRG = TR_KLRGD.KD_BRG"+
            " WHERE (((TR_KLRGH.TG_BUKTI) BETWEEN #"+Formater.formatDate(tglAwalTahun,"yyyy-MM-dd")+"#"+
            " AND #"+Formater.formatDate(srcWeeklyDbf.getTglPilih(),"yyyy-MM-dd")+"#))";
            
            if(srcWeeklyDbf.getKdKategori().length()>0){
                sql = sql + " AND MS_BRG.KD_KATAG"+"='"+srcWeeklyDbf.getKdKategori()+"'";
            }
            
            if(srcWeeklyDbf.getKdSubKategori().length()>0){
                sql = sql + " AND MS_BRG.KD_SBKTG"+"='"+srcWeeklyDbf.getKdSubKategori()+"'";
            }
            
            System.out.println("processDataDispatchOnWh SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                Vector temp = new Vector(1,1);
                
                temp.add(rs.getString(1));
                temp.add(rs.getString(2));
                temp.add(new Double(rs.getDouble(3)));
                temp.add(rs.getString(4));
                temp.add(new Double(rs.getDouble(5)));
                
                vectData.add(temp);
            }
            
            //insert data to Mysql
            insertDataDispatch(vectData);
            
            result = true;
        }catch(Exception e){
            System.out.println("processDataDispatchOnWh Err >> "+e.toString());
        }
        return result;
    }
    
    
    /**
     * fetch data from table transactian Dispatch on Store (TR_KLRTD.DBF)
     * and insert into Mysql
     * @param koneksi
     * @param srcWeeklyDbf
     * @created by Edhy
     */
    public static boolean processDataDispatchOnStore(Connection koneksi, SrcWeeklyDbf srcWeeklyDbf){
        boolean result = false;
        Vector vectData = new Vector(1,1);
        int intTahun = Integer.parseInt(srcWeeklyDbf.getTahun());
        Date tglAwalTahun = new Date(intTahun-1900,0,1);
        try{
            String sql = "SELECT MS_BRG.KD_BRG"+
            ", MS_BRG.NM_BRG"+
            ", MS_BRG.HG_JUAL"+
            ", MS_BRG.SATUAN"+
            ", TR_KLRTD.QTY" +
            " FROM MS_BRG"+
            " INNER JOIN (TR_KLRTH INNER JOIN TR_KLRTD ON TR_KLRTH.NO_BUKTI = TR_KLRTD.NO_BUKTI)"+
            " ON MS_BRG.KD_BRG = TR_KLRTD.KD_BRG"+
            " WHERE (((TR_KLRTH.TG_BUKTI) BETWEEN #"+Formater.formatDate(tglAwalTahun,"yyyy-MM-dd")+"#"+
            " AND #"+Formater.formatDate(srcWeeklyDbf.getTglPilih(),"yyyy-MM-dd")+"#))";
            
            if(srcWeeklyDbf.getKdKategori().length()>0){
                sql = sql + " AND MS_BRG.KD_KATAG"+"='"+srcWeeklyDbf.getKdKategori()+"'";
            }
            
            if(srcWeeklyDbf.getKdSubKategori().length()>0){
                sql = sql + " AND MS_BRG.KD_SBKTG"+"='"+srcWeeklyDbf.getKdSubKategori()+"'";
            }
            
            System.out.println("processDataDispatchOnStore SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                Vector temp = new Vector(1,1);
                
                temp.add(rs.getString(1));
                temp.add(rs.getString(2));
                temp.add(new Double(rs.getDouble(3)));
                temp.add(rs.getString(4));
                temp.add(new Double(rs.getDouble(5)));
                
                vectData.add(temp);
            }
            //insert data to Mysql
            insertDataDispatch(vectData);
            
            result = true;
        }catch(Exception e){
            System.out.println("processDataDispatchOnStore Err >> "+e.toString());
        }
        return result;
    }
    
    /**
     * insert data dispatch Wh to Mysql
     * @param vectReceive
     * @created by Edhy
     */
    public static void insertDataDispatch(Vector vectDispatch){
        if(vectDispatch!=null && vectDispatch.size()>0){
            int maxDispatch = vectDispatch.size();
            for(int i=0; i<maxDispatch; i++){
                Vector temp = (Vector)vectDispatch.get(i);
                try{
                    
                    // for replace caracter if found "'", with "`"
                    String nm = String.valueOf(temp.get(1));
                    if (nm.compareToIgnoreCase("'") >= 0) {
                        nm = nm.replace('\'','`');
                    }
                    
                    String sql = "INSERT INTO POS_WEEKLY_REPORT VALUES("+
                    "'"+String.valueOf(temp.get(0))+"'"+
                    ",'"+nm+"'"+
                    ",'"+String.valueOf(temp.get(3))+"'"+
                    ","+((Double)temp.get(2)).doubleValue()+
                    ",0"+
                    ",0"+
                    ","+((Double)temp.get(4)).doubleValue()+
                    ",0"+
                    ",0)";
                    
                    //System.out.println("insertDataDispatch sql"+sql);
                    int status = DBHandler.execUpdate(sql);
                }catch(Exception e){
                    System.out.println("insertDataDispatch err >> "+e.toString());
                }
            }
        }
    }
    
    /**
     * fetch data from table StockPeriode on Warehouse (MS_STKG.DBF)
     * @param koneksi
     * @param srcWeeklyDbf
     * @created by Edhy
     * @return
     */
    public static boolean processDataBalaceLastYearOnWh(Connection koneksi, SrcWeeklyDbf srcWeeklyDbf){
        boolean result = false;
        Vector vectData = new Vector(1,1);
        try{
            String sql = "SELECT MS_STKG.KD_BRG"+
            ", MS_BRG.NM_BRG"+
            ", MS_BRG.HG_JUAL"+
            ", MS_BRG.SATUAN"+
            ", MS_STKG.SALDO"+
            " FROM (MS_SBKTG AS MS_SBKT INNER JOIN (MS_KATAG INNER JOIN MS_BRG ON MS_KATAG.KD_KATAG = MS_BRG.KD_KATAG) ON MS_SBKT.KD_SBKTG = MS_BRG.KD_SBKTG) INNER JOIN MS_STKG ON MS_BRG.KD_BRG = MS_STKG.KD_BRG"+
            " WHERE (MS_STKG.TAHUN='"+srcWeeklyDbf.getTahun()+"')";
            
            if(srcWeeklyDbf.getKdKategori().length()>0){
                sql = sql + " AND (MS_BRG.KD_KATAG"+"='"+srcWeeklyDbf.getKdKategori()+"')";
            }
            
            if(srcWeeklyDbf.getKdSubKategori().length()>0){
                sql = sql + " AND (MS_BRG.KD_SBKTG"+"='"+srcWeeklyDbf.getKdSubKategori()+"')";
            }
            
            System.out.println("processDataBalaceLastYearOnWh SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                Vector temp = new Vector(1,1);
                
                temp.add(rs.getString(1));
                temp.add(rs.getString(2));
                temp.add(new Double(rs.getDouble(3)));
                temp.add(rs.getString(4));
                temp.add(new Integer(rs.getInt(5)));
                
                vectData.add(temp);
            }
            System.out.println("== >>>> WH vectData : "+vectData.size());
            //insert into Mysql
            insertDataBalanceLastYear(vectData);
            
            result = true;
        }catch(Exception e){
            System.out.println("processDataBalaceLastYearOnWh Err >> "+e.toString());
        }
        return result;
    }
    
    
    /**
     * fetch data from table StockPeriode on Store (MS_STKT.DBF)
     * and insert into Mysql
     * @param koneksi
     * @param srcWeeklyDbf
     * @created by Edhy
     * @return
     */
    public static boolean processDataBalaceLastYearOnStore(Connection koneksi, SrcWeeklyDbf srcWeeklyDbf){
        boolean result = false;
        Vector vectData = new Vector(1,1);
        try{
            
            String sql = "SELECT MS_STKT.KD_BRG"+
            ", MS_BRG.NM_BRG"+
            ", MS_BRG.HG_JUAL"+
            ", MS_BRG.SATUAN"+
            ", MS_STKT.SALDO"+
            " FROM (MS_SBKTG AS MS_SBKT INNER JOIN (MS_KATAG INNER JOIN MS_BRG ON MS_KATAG.KD_KATAG = MS_BRG.KD_KATAG) ON MS_SBKT.KD_SBKTG = MS_BRG.KD_SBKTG) INNER JOIN MS_STKT ON MS_BRG.KD_BRG = MS_STKT.KD_BRG"+
            " WHERE (MS_STKT.TAHUN='"+srcWeeklyDbf.getTahun()+"'AND (MS_STKT.KD_TOKO)='"+srcWeeklyDbf.getKdLokasi()+"')";
            
            if(srcWeeklyDbf.getKdKategori().length()>0){
                sql = sql + " AND (MS_BRG.KD_KATAG"+"='"+srcWeeklyDbf.getKdKategori()+"')";
            }
            
            if(srcWeeklyDbf.getKdSubKategori().length()>0){
                sql = sql + " AND (MS_BRG.KD_SBKTG"+"='"+srcWeeklyDbf.getKdSubKategori()+"')";
            }
            
            System.out.println("processDataBalaceLastYearOnStore SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                Vector temp = new Vector(1,1);
                
                temp.add(rs.getString(1));
                temp.add(rs.getString(2));
                temp.add(new Double(rs.getDouble(3)));
                temp.add(rs.getString(4));
                temp.add(new Integer(rs.getInt(5)));
                
                vectData.add(temp);
            }
            
            System.out.println("== >>>> STORE vectData : "+vectData.size());
            //insert into Mysql
            insertDataBalanceLastYear(vectData);
            
            result = true;
        }catch(Exception e){
            System.out.println("processDataBalaceLastYearOnStore Err >> "+e.toString());
        }
        return result;
    }
    
    /**
     * insert data dispatch Wh to Mysql
     * @param vectReceive
     * @created by Edhy
     */
    public static void insertDataBalanceLastYear(Vector vectBalanceLastYear){
        if(vectBalanceLastYear!=null && vectBalanceLastYear.size()>0){
            int maxBalance = vectBalanceLastYear.size();
            for(int i=0; i<maxBalance; i++){
                Vector temp = (Vector)vectBalanceLastYear.get(i);
                try{
                    
                    // for replace caracter if found "'", with "`"
                    String nm = String.valueOf(temp.get(1));
                    if (nm.compareToIgnoreCase("'") >= 0) {
                        nm = nm.replace('\'','`');
                    }
                    
                    String sql = "INSERT INTO POS_WEEKLY_REPORT VALUES("+
                    "'"+String.valueOf(temp.get(0))+"'"+
                    ",'"+nm+"'"+
                    ",'"+String.valueOf(temp.get(3))+"'"+
                    ","+((Double)temp.get(2)).doubleValue()+
                    ","+((Integer)temp.get(4)).intValue()+
                    ",0"+
                    ",0"+
                    ",0"+
                    ",0)";
                    
                    //System.out.println("insertDataBalanceLastYear sql"+sql);
                    int status = DBHandler.execUpdate(sql);
                }catch(Exception e){
                    System.out.println("insertDataBalanceLastYear err >>"+e.toString());
                }
            }
        }
    }
    
    
    
    public static void main(String[] args){
        //Connection koneksi = null;
        
        SrcWeeklyDbf srcWeeklyDbf = new SrcWeeklyDbf();
        srcWeeklyDbf.setKdKategori("1");
        srcWeeklyDbf.setKdSubKategori("001");
        srcWeeklyDbf.setTglPilih(new Date(104,2,31));
        srcWeeklyDbf.setTahun("2004");
        
        try {
            //koneksi = openKoneksiDbf();
            //processDataBalaceLastYearOnStore(koneksi,srcWeeklyDbf);
            boolean hasil = processWeeklyDbfOnWh(srcWeeklyDbf);
        }
        catch(Exception exc) {
            System.out.println("Koneksi Error : " + exc);
        }
        
    }
    
}
