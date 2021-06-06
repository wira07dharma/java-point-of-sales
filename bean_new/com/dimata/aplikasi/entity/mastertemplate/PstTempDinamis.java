/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aplikasi.entity.mastertemplate;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;



import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Vector;
import javax.print.DocFlavor;
/**
 *
 * @author arin
 */
public class PstTempDinamis extends DBHandler implements I_DBInterface,I_DBType,I_PersintentExc,I_Language{
    public static final String TB_TEMPLATEDINAMIS="tb_templatedinamis";
    public static final int FLD_TEMP_DINAMIS_ID=0;
    public static final int FLD_TEMP_VERSION_NO=1;
    public static final int FLD_TEMP_COLOR=2;
    public static final int FLD_TEMP_COLOR_HEADER=3;
    public static final int FLD_TEMP_COLOR_CONTENT=4;
    public static final int FLD_TEMP_BG_MENU=5;
    public static final int FLD_TEMP_FONT_MENU=6;
    public static final int FLD_TEMP_HOVER_MENU=7;
    public static final int FLD_TEMP_NAVIGATION=8;
    public static final int FLD_LANGUAGE=9;
    public static final int FLD_TEMP_COLOR_HEADER2=10;
    public static final int FLD_GARIS_HAEADER1=11;
    public static final int FLD_GARIS_HEADER2=12;
    public static final int FLD_GARIS_CONTENT=13;
    public static final int FLD_FOOTER_GARIS=14;
    public static final int FLD_FOOTER_BACKGROUND=15;
    public static final int FLD_TABLE_HEADER=16;
    public static final int FLD_TABLE_CELL=17;
    
    public static String[]fieldNames = {
        "TEMP_DINAMIS_ID",
        "TEMP_VERSION_NO",
        "TEMP_COLOR",
        "TEMP_COLOR_HEADER",
        "TEMP_COLOR_CONTENT",
        "TEMP_BG_MENU",
        "TEMP_FONT_MENU",
        "TEMP_HOVER_MENU", 
        "TEMP_NAVIGATION",
        "TEMP_LANGUAGE",
        "TEMP_COLOR_HEADER2",
        "GARIS_HAEADER1",
        "GARIS_HEADER2",
        "GARIS_CONTENT",
        "FOOTER_GARIS",
        "FOOTER_BACKGROUND",
        "TABLE_HEADER",
        "TABLE_CELL"
    };
    public static int[]fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING
    };

   
    /**
     * Keterangan: ini ini untuk membuat constractor dari sebuah objek,
     * implementasinya yaitu new PstTempDinamis();
     */
   public PstTempDinamis(){
    }
    public PstTempDinamis(int i)throws DBException{
    
        super(new PstTempDinamis());
    
    }
    
    public PstTempDinamis(String sOid)throws DBException{
        
        super(new PstTempDinamis());   
        
        if(!locate(sOid)){
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }else{
            return;
        }
    }
    
    public PstTempDinamis(long lOid)throws DBException{
        
        super(new PstTempDinamis(0));
        
        String sOid="0";
        
        try{
             
            sOid=String.valueOf(lOid);
        }catch(Exception e){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        
        if(!locate(sOid)){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }else{
            return;
        }
    }
    
    public int getFieldSize(){
        return fieldNames.length;
    }
    public String getTableName(){
        return TB_TEMPLATEDINAMIS;
    }
    public String[]getFieldNames(){
        return fieldNames;
    }
    public int[] getFieldTypes(){
        return fieldTypes;
    }
    public String getPersistentName(){
        return new PstTempDinamis().getClass().getName();
    }

    public long fetchExc(Entity ent)throws Exception{
        TempDinamis tempDinamis = fetchExc(ent.getOID());
        ent=(Entity)tempDinamis;
        return tempDinamis.getOID();
    }
    public static TempDinamis fetchExc(long oid)throws DBException{
        
        try{
            
            TempDinamis tempDinamis=new TempDinamis();
            
            PstTempDinamis pstTempDinamis=new PstTempDinamis(oid);
            
            tempDinamis.setOID(oid);
            tempDinamis.setTempVersionNo(pstTempDinamis.getString(FLD_TEMP_VERSION_NO));
            tempDinamis.setTempColor(pstTempDinamis.getString(FLD_TEMP_COLOR));
            tempDinamis.setHeaderColor(pstTempDinamis.getString(FLD_TEMP_COLOR_HEADER));
            tempDinamis.setTempColorHeader(pstTempDinamis.getString(FLD_TEMP_COLOR_HEADER2));
            tempDinamis.setGarisHeader1(pstTempDinamis.getString(FLD_GARIS_HAEADER1));
            tempDinamis.setGarisHeader1(pstTempDinamis.getString(FLD_GARIS_HEADER2));
            tempDinamis.setContentColor(pstTempDinamis.getString(FLD_TEMP_COLOR_CONTENT));
            tempDinamis.setBgMenu(pstTempDinamis.getString(FLD_TEMP_BG_MENU));
            tempDinamis.setFontMenu(pstTempDinamis.getString(FLD_TEMP_FONT_MENU));
            tempDinamis.setHoverMenu(pstTempDinamis.getString(FLD_TEMP_HOVER_MENU));
            tempDinamis.setNavigation(pstTempDinamis.getString(FLD_TEMP_NAVIGATION));;
            //tempDinamis.setLanguage(pstTempDinamis.getString(FLD_LANGUAGE));;
            tempDinamis.setGarisContent(pstTempDinamis.getString(FLD_TEMP_COLOR_CONTENT));
            tempDinamis.setFooterGaris(pstTempDinamis.getString(FLD_FOOTER_GARIS));
            tempDinamis.setFooterBackground(pstTempDinamis.getString(FLD_FOOTER_BACKGROUND));
            tempDinamis.setTableHeader(pstTempDinamis.getString(FLD_TABLE_HEADER));
            tempDinamis.setTableCell(pstTempDinamis.getString(FLD_TABLE_CELL));
            
             return tempDinamis;
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception exc){
            throw new DBException(new PstTempDinamis(0), DBException.UNKNOWN);
        }
       
    }
            
    public long insertExc(Entity ent)throws Exception{
        return insertExc((TempDinamis) ent);
    }
    
    public static synchronized long insertExc(TempDinamis tempDinamis)throws DBException{
        
        try{
            
            PstTempDinamis pstTempDinamis = new PstTempDinamis(0);
            
            pstTempDinamis.setString(FLD_TEMP_VERSION_NO,tempDinamis.getTempVersionNo());
            pstTempDinamis.setString(FLD_TEMP_COLOR,tempDinamis.getTempColor());
            pstTempDinamis.setString(FLD_TEMP_COLOR_HEADER,tempDinamis.getHeaderColor());
            pstTempDinamis.setString(FLD_TEMP_COLOR_HEADER2,tempDinamis.getTempColorHeader());
            pstTempDinamis.setString(FLD_GARIS_HAEADER1,tempDinamis.getGarisHeader1());
            pstTempDinamis.setString(FLD_GARIS_HEADER2,tempDinamis.getGarisHeader2());
            pstTempDinamis.setString(FLD_TEMP_COLOR_CONTENT,tempDinamis.getContentColor());
            pstTempDinamis.setString(FLD_TEMP_BG_MENU,tempDinamis.getBgMenu());
            pstTempDinamis.setString(FLD_TEMP_FONT_MENU,tempDinamis.getFontMenu());
            pstTempDinamis.setString(FLD_TEMP_HOVER_MENU,tempDinamis.getHoverMenu());
            pstTempDinamis.setString(FLD_TEMP_NAVIGATION,tempDinamis.getNavigation());
            //pstTempDinamis.setString(FLD_LANGUAGE,tempDinamis.getLanguage());
            pstTempDinamis.setString(FLD_GARIS_CONTENT,tempDinamis.getGarisContent());
            pstTempDinamis.setString(FLD_FOOTER_GARIS,tempDinamis.getFooterGaris());
            pstTempDinamis.setString(FLD_FOOTER_BACKGROUND,tempDinamis.getFooterBackground());
            pstTempDinamis.setString(FLD_TABLE_HEADER, tempDinamis.getTableHeader());
            pstTempDinamis.setString(FLD_TABLE_CELL, tempDinamis.getTableCell());
            
            pstTempDinamis.insert();
            
            tempDinamis.setOID(pstTempDinamis.getlong(FLD_TEMP_DINAMIS_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstTempDinamis(0),DBException.UNKNOWN);
        }
        return tempDinamis.getOID();
    }
    
    public long updateExc(Entity ent) throws Exception {
          return updateExc((TempDinamis) ent);
    }
    public static long updateExc(TempDinamis tempDinamis)throws DBException{
        try{
            if(tempDinamis.getOID()!=0){
                PstTempDinamis pstTempDinamis = new PstTempDinamis(tempDinamis.getOID());
               pstTempDinamis.setString(FLD_TEMP_VERSION_NO,tempDinamis.getTempVersionNo());
            pstTempDinamis.setString(FLD_TEMP_COLOR,tempDinamis.getTempColor());
            pstTempDinamis.setString(FLD_TEMP_COLOR_HEADER,tempDinamis.getHeaderColor());
            pstTempDinamis.setString(FLD_TEMP_COLOR_HEADER2,tempDinamis.getTempColorHeader());
            pstTempDinamis.setString(FLD_GARIS_HAEADER1,tempDinamis.getGarisHeader1());
            pstTempDinamis.setString(FLD_GARIS_HEADER2,tempDinamis.getGarisHeader2());
            pstTempDinamis.setString(FLD_TEMP_COLOR_CONTENT,tempDinamis.getContentColor());
            pstTempDinamis.setString(FLD_TEMP_BG_MENU,tempDinamis.getBgMenu());
            pstTempDinamis.setString(FLD_TEMP_FONT_MENU,tempDinamis.getFontMenu());
            pstTempDinamis.setString(FLD_TEMP_HOVER_MENU,tempDinamis.getHoverMenu());
            pstTempDinamis.setString(FLD_TEMP_NAVIGATION,tempDinamis.getNavigation());
            //pstTempDinamis.setString(FLD_LANGUAGE,tempDinamis.getLanguage());
            pstTempDinamis.setString(FLD_GARIS_CONTENT,tempDinamis.getGarisContent());
            pstTempDinamis.setString(FLD_FOOTER_GARIS,tempDinamis.getFooterGaris());
            pstTempDinamis.setString(FLD_FOOTER_BACKGROUND,tempDinamis.getFooterBackground());
            pstTempDinamis.setString(FLD_TABLE_HEADER, tempDinamis.getTableHeader());
            pstTempDinamis.setString(FLD_TABLE_CELL, tempDinamis.getTableCell());
            
            pstTempDinamis.update();
                
                return tempDinamis.getOID();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstTempDinamis(0),DBException.UNKNOWN);
        }
        return 0;
    }
    public long deleteExc(Entity ent) throws Exception{
        
        if(ent==null){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        
        return deleteExc(ent.getOID());
    
    }
    
    public static long deleteExc(long oid)throws DBException{
        
        try{
            PstTempDinamis pstTempDinamis = new PstTempDinamis(oid);
            
            pstTempDinamis.delete();
            
        } catch(DBException dbe){
            
            throw dbe;
            
        }catch(Exception e){
            throw new DBException(new PstTempDinamis(0),DBException.UNKNOWN);
        }
        return oid;
    }
   
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order){
        Vector lists = new Vector();
        
        DBResultSet dbrs=null;
        
        try{
            
            String sql = " SELECT * FROM " + TB_TEMPLATEDINAMIS;
            
            if (whereClause != null && whereClause.length()>0){
                sql = sql+ " WHERE "+ whereClause;
            }
            
            if (order != null && order.length()>0){
                sql = sql + " ORDER BY "+ order;
            }
            
            if(limitStart == 0 && recordToGet ==0){
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + " , " + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            
            ResultSet rs = dbrs.getResultSet();
   
            while (rs.next()){
            
                TempDinamis tempDinamis = new TempDinamis();
                
                resultToObject(rs,tempDinamis);
                
                lists.add(tempDinamis);
            }
            
            rs.close();
            
            return lists;
            
        }catch(Exception e){
            
            System.out.println(e);
            
        }finally{
            DBResultSet.close(dbrs);
            
        }
        return new Vector();
    }
    public static void resultToObject(ResultSet rs, TempDinamis tempDinamis){
        
        try {
            
            tempDinamis.setOID(rs.getLong(PstTempDinamis.fieldNames[PstTempDinamis.FLD_TEMP_DINAMIS_ID]));
            tempDinamis.setTempVersionNo(rs.getString(PstTempDinamis.fieldNames[PstTempDinamis.FLD_TEMP_VERSION_NO]));
            tempDinamis.setTempColor(rs.getString(PstTempDinamis.fieldNames[PstTempDinamis.FLD_TEMP_COLOR]));
            tempDinamis.setHeaderColor(rs.getString(PstTempDinamis.fieldNames[PstTempDinamis.FLD_TEMP_COLOR_HEADER]));
            tempDinamis.setTempColorHeader(rs.getString(PstTempDinamis.fieldNames[PstTempDinamis.FLD_TEMP_COLOR_HEADER2]));
            tempDinamis.setGarisHeader1(rs.getString(PstTempDinamis.fieldNames[PstTempDinamis.FLD_GARIS_HAEADER1]));
            tempDinamis.setGarisHeader2(rs.getString(PstTempDinamis.fieldNames[PstTempDinamis.FLD_GARIS_HEADER2]));
            tempDinamis.setContentColor(rs.getString(PstTempDinamis.fieldNames[PstTempDinamis.FLD_TEMP_COLOR_CONTENT]));
            tempDinamis.setBgMenu(rs.getString(PstTempDinamis.fieldNames[PstTempDinamis.FLD_TEMP_BG_MENU]));
            tempDinamis.setFontMenu(rs.getString(PstTempDinamis.fieldNames[PstTempDinamis.FLD_TEMP_FONT_MENU]));
            tempDinamis.setHoverMenu(rs.getString(PstTempDinamis.fieldNames[PstTempDinamis.FLD_TEMP_HOVER_MENU]));
            tempDinamis.setNavigation(rs.getString(PstTempDinamis.fieldNames[PstTempDinamis.FLD_TEMP_NAVIGATION]));
            tempDinamis.setBackground(rs.getString(PstTempDinamis.fieldNames[PstTempDinamis.FLD_LANGUAGE]));
            tempDinamis.setGarisContent(rs.getString(PstTempDinamis.fieldNames[PstTempDinamis.FLD_GARIS_CONTENT]));
            tempDinamis.setFooterGaris(rs.getString(PstTempDinamis.fieldNames[PstTempDinamis.FLD_FOOTER_GARIS]));
            tempDinamis.setFooterBackground(rs.getString(PstTempDinamis.fieldNames[PstTempDinamis.FLD_FOOTER_BACKGROUND]));
            tempDinamis.setTableCell(rs.getString(PstTempDinamis.fieldNames[PstTempDinamis.FLD_TABLE_CELL]));
            tempDinamis.setTableHeader(rs.getString(PstTempDinamis.fieldNames[PstTempDinamis.FLD_TABLE_HEADER]));
            
        } catch(Exception e){
            System.out.println("exc"+e);
        }
    }
    public static boolean checkOID(long mSid){
     
          DBResultSet dbrs = null;
        
        boolean result = false;
        
        try {
            
            String sql = "SELECT * FROM"+TB_TEMPLATEDINAMIS+"WHERE"
                    + PstTempDinamis.fieldNames[PstTempDinamis.FLD_TEMP_DINAMIS_ID]+"="+ mSid;
            
            dbrs = DBHandler.execQueryResult(sql);
            
            ResultSet rs = dbrs.getResultSet();
            
            
            while (rs.next()){
                result = true;
        }
            rs.close();
            
    }catch(Exception e){
        
        System.out.println("err : " + e.toString());
        
    }finally{
            
            DBResultSet.close(dbrs);
            
            return result;
        }     
        
    }
    /**
     * 
     * @param whereClause
     * @return 
     */
    public static  int getCount(String whereClause){
    
        DBResultSet dbrs = null;
        
        try {
        
            String sql = " SELECT COUNT(" + PstTempDinamis.fieldNames[PstTempDinamis.FLD_TEMP_DINAMIS_ID]+") FROM " + TB_TEMPLATEDINAMIS;
            
            if (whereClause != null && whereClause.length()>0){
            }
            
            dbrs = DBHandler.execQueryResult(sql);//excute query sql
            
            ResultSet rs = dbrs.getResultSet();
            
            
            int count = 0;
            
            while (rs.next()){
            
                count = rs.getInt(1);
            }
            
            rs.close();
            
            return count;
            
        }catch(Exception e){
        
            return 0;
            
        }finally{
        
            DBResultSet.close(dbrs);
        }
    }
    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause){
    
        int size = getCount(whereClause);
        
        int start=0;
        
        boolean found = false;
        
        for (int i = 0; (i < size) && !found; i = i + recordToGet){
        
            Vector list = list(i, recordToGet, whereClause, orderClause);
            
            start = i ;
            
            if (list.size()>0){
            
                for (int ls = 0; ls < list.size(); ls++){
                
                    TempDinamis tempDinamis = (TempDinamis) list.get(ls);
                    
                    if (oid == tempDinamis.getOID()){
                    
                        found= true;
                    }
                }
            }
         }
         if ((start >= size)&& (size > 0)){
             start = start - recordToGet;
         }
     
         return start;
         
    }
    /* This method used to find command where current data */
    
    public static int findLimitCommand(int start, int recordToGet, int vectSize){
    
        int cmd = Command.LIST;
        
        int md1 = vectSize % recordToGet;
        
        vectSize = vectSize + (recordToGet - md1);
        
        if (start == 0){
            cmd = Command.FIRST;
        }else {
        
            if (start == (vectSize - recordToGet)){
                cmd = Command.LAST;
            }else{
            
                start = start + recordToGet;
                
                if(start == (vectSize - recordToGet)){
                
                    cmd = Command.NEXT;
                    
                    System.out.println("next.................");
                }else{
                
                    start=start-recordToGet;
                    
                    if (start > 0){
                    
                        cmd = Command.PREV;
                        
                        System.out.println("prev.............");
                    }
                }
            }
        }
        
        return cmd;
    }

    }