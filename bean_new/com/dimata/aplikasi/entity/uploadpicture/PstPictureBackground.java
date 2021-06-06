/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aplikasi.entity.uploadpicture;

import com.dimata.aplikasi.entity.mastertemplate.PstTempDinamis;
import com.dimata.aplikasi.entity.mastertemplate.TempDinamis;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.io.File;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author arin
 */
public class PstPictureBackground extends DBHandler implements I_DBInterface,I_DBType,I_PersintentExc,I_Language{
    public static final String TB_PICTURE_BACKGROUND="tb_picture_background";
    public static final int FLD_PICTURE_BACKGROUND_ID=0;
    public static final int FLD_LOGIN_ID=1;
    public static final int FLD_NAMA_PICTURE=2;
    public static final int FLD_KETERANGAN_PICTURE=3;
    public static final int FLD_PICTURE=4;
    
    public static String[]fieldNames = {
        "PICTURE_BACKGROUND_ID",
        "LOGIN_ID",
        "NAMA_PICTURE",
        "KETERANGAN_PICTURE",
        "PICTURE"
    };
    public static int[]fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING
    }; 
    /**
     * Keterangan: ini untuk membuat constractor dari sebuah objek,
     * implementasinya yaitu new PstPictureBackground();
     */
   public PstPictureBackground(){
    }
    public PstPictureBackground(int i)throws DBException{
    
        super(new PstPictureBackground());
    
    }
    
    public PstPictureBackground(String sOid)throws DBException{
        
        super(new PstPictureBackground());   
        
        if(!locate(sOid)){
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }else{
            return;
        }
    }
    
    public PstPictureBackground(long lOid)throws DBException{
        
        super(new PstPictureBackground(0));
        
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
        return TB_PICTURE_BACKGROUND;
    }
    public String[]getFieldNames(){
        return fieldNames;
    }
    public int[] getFieldTypes(){
        return fieldTypes;
    }
    public String getPersistentName(){
        return new PstPictureBackground().getClass().getName();
    }

    public long fetchExc(Entity ent)throws Exception{
        PictureBackground pictureBackground = fetchExc(ent.getOID());
        ent=(Entity)pictureBackground;
        return pictureBackground.getOID();
    }
    public static PictureBackground fetchExc(long oid)throws DBException{
        
        try{
            
            PictureBackground pictureBackground=new PictureBackground();
            
            PstPictureBackground pstPictureBackground=new PstPictureBackground(oid);
            
            pictureBackground.setOID(oid);
            pictureBackground.setLoginId(pstPictureBackground.getLong(FLD_LOGIN_ID));
            pictureBackground.setNamaPicture(pstPictureBackground.getString(FLD_NAMA_PICTURE));
            pictureBackground.setKeterangan(pstPictureBackground.getString(FLD_KETERANGAN_PICTURE));
            pictureBackground.setUploadPicture(pstPictureBackground.getString(FLD_PICTURE));
                 
            return pictureBackground;
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception exc){
            throw new DBException(new PstPictureBackground(0), DBException.UNKNOWN);
        }
       
    }
            
    public long insertExc(Entity ent)throws Exception{
        return insertExc((TempDinamis) ent);
    }
    
    
    public static synchronized PictureBackground insertExc(PictureBackground pictureBackground)throws DBException{
        
        try{
            
            PstPictureBackground pstPictureBackground = new PstPictureBackground(0);
            pstPictureBackground.setLong(FLD_LOGIN_ID,pictureBackground.getLoginId());
            pstPictureBackground.setString(FLD_NAMA_PICTURE,pictureBackground.getNamaPicture());
            pstPictureBackground.setString(FLD_KETERANGAN_PICTURE,pictureBackground.getKeterangan());
            pstPictureBackground.setString(FLD_PICTURE,pictureBackground.getUploadPicture());
            pstPictureBackground.insert();
            
            return pictureBackground;
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception exc){
          //  throw new DBException(new PstTempDinamis(0), DBException.UNKNOWN);
           throw new DBException(new PstPictureBackground(0),DBException.UNKNOWN);
        }
       
    }
    
    public long updateExc(Entity ent) throws Exception {
          return updateExc((PictureBackground) ent);
    }
    public static long updateExc(PictureBackground pictureBackground)throws DBException{
        try{
            if(pictureBackground.getOID()!=0){
                PstPictureBackground pstpictureBackground = new PstPictureBackground(pictureBackground.getOID());
                pstpictureBackground.setString(FLD_NAMA_PICTURE,pictureBackground.getNamaPicture());
                pstpictureBackground.setString(FLD_KETERANGAN_PICTURE,pictureBackground.getKeterangan());
                pstpictureBackground.setString(FLD_PICTURE,pictureBackground.getUploadPicture());
                pstpictureBackground.update();
                
                return pictureBackground.getOID();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstPictureBackground(0),DBException.UNKNOWN);
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
            PstPictureBackground pictureBackground = new PstPictureBackground(oid);
            
            pictureBackground.delete();
            
        } catch(DBException dbe){
            
            throw dbe;
            
        }catch(Exception e){
            throw new DBException(new PstPictureBackground(0),DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order){
        Vector lists = new Vector();
        
        DBResultSet dbrs=null;
        
        try{
            
            String sql = " SELECT * FROM " + TB_PICTURE_BACKGROUND;
            
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
            
                PictureBackground pictureBackground = new PictureBackground();
                
                resultToObject(rs,pictureBackground);
                
                lists.add(pictureBackground);
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
    public static void resultToObject(ResultSet rs, PictureBackground pictureBackground){
        
        try {
            
            pictureBackground.setOID(rs.getLong(PstPictureBackground.fieldNames[PstPictureBackground.FLD_PICTURE_BACKGROUND_ID]));
            pictureBackground.setNamaPicture(rs.getString(PstPictureBackground.fieldNames[PstPictureBackground.FLD_NAMA_PICTURE]));
            pictureBackground.setKeterangan(rs.getString(PstPictureBackground.fieldNames[PstPictureBackground.FLD_KETERANGAN_PICTURE]));
            pictureBackground.setUploadPicture(rs.getString(PstPictureBackground.fieldNames[PstPictureBackground.FLD_PICTURE]));
            pictureBackground.setLoginId(rs.getLong(PstPictureBackground.fieldNames[PstPictureBackground.FLD_LOGIN_ID]));
        } catch(Exception e){
            System.out.println("exc"+e);
        }
    }
    public static boolean checkOID(long mSid){
     
          DBResultSet dbrs = null;
        
        boolean result = false;
        
        try {
            
            String sql = "SELECT * FROM"+TB_PICTURE_BACKGROUND+"WHERE"
                    + PstPictureBackground.fieldNames[PstPictureBackground.FLD_PICTURE_BACKGROUND_ID]+"="+ mSid;
            
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
        
            String sql = " SELECT COUNT(" + PstPictureBackground.fieldNames[PstPictureBackground.FLD_PICTURE_BACKGROUND_ID]+") FROM " + TB_PICTURE_BACKGROUND;
            
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
    }/**
  * keterangan : untuk delete picture background
  * @param ctx
  * @param pictureBackground 
  */
 public static void deletePictureBackground(PictureBackground pictureBackground,String pathImg){
     String path= pathImg + File.separator+pictureBackground.getUploadPicture();
     System.out.println(path);
     try{
         new File(path).delete(); 
     }catch(Exception exc){
     
         System.out.println("Cannot delete picture"+exc);
     }
    
 }
    }