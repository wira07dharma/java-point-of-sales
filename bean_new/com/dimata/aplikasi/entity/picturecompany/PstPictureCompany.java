/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aplikasi.entity.picturecompany;

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
 * @author user
 */
public class PstPictureCompany extends DBHandler implements I_DBInterface,I_DBType,I_PersintentExc,I_Language{
    public static final String TB_COMPANY_PICTURE="tb_company_picture";
    private static int FLD_PICTURE_COMPANY_ID=0;
    private static int FLD_NAMA_PICTURE=1;
    
    private static String[]fieldNames ={
       "PICTURE_COMPANY_ID",
       "NAMA_PICTURE"
    };
    private static int[]fieldTypes={
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING
    };
    //public static final String pathImg="C:/PutriPramesti_PP/tugasPutri/trening/WebDinamis_construck_20130918/jsp/imgcompany/";
    /**
     * Keterangan: ini ini untuk membuat constractor dari sebuah objek,
     * implementasinya yaitu new PstTempDinamis();
     */
   public PstPictureCompany(){
    }
    public PstPictureCompany(int i)throws DBException{
    
        super(new PstPictureCompany());
    
    }
    
    public PstPictureCompany(String sOid)throws DBException{
        
        super(new PstPictureCompany());   
        
        if(!locate(sOid)){
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }else{
            return;
        }
    }
    
    public PstPictureCompany(long lOid)throws DBException{
        
        super(new PstPictureCompany(0));
        
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
        return TB_COMPANY_PICTURE;
    }
    public String[]getFieldNames(){
        return fieldNames;
    }
    public int[] getFieldTypes(){
        return fieldTypes;
    }
    public String getPersistentName(){
        return new PstPictureCompany().getClass().getName();
    }

    public long fetchExc(Entity ent)throws Exception{
        PictureCompany pictureCompany = fetchExc(ent.getOID());
        ent=(Entity)pictureCompany;
        return pictureCompany.getOID();
    }
    public static PictureCompany fetchExc(long oid)throws DBException{
        
        try{
            
            PictureCompany pictureCompany=new PictureCompany();
            
            PstPictureCompany pstPictureCompany=new PstPictureCompany(oid);
            
            pictureCompany.setOID(oid);
            pictureCompany.setNamaPicture(pstPictureCompany.getString(FLD_NAMA_PICTURE));
            
             return pictureCompany;
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception exc){
            throw new DBException(new PstPictureCompany(0), DBException.UNKNOWN);
        }
       
    }
            
    public long insertExc(Entity ent)throws Exception{
        return insertExc((PictureCompany) ent);
    }
    
    public static synchronized long insertExc(PictureCompany pictureCompany)throws DBException{
        
        try{
            
            PstPictureCompany pstPictureCompany = new PstPictureCompany(0);
            
            pstPictureCompany.setString(FLD_NAMA_PICTURE,pictureCompany.getNamaPicture());
            pstPictureCompany.insert();
            
            pictureCompany.setOID(pstPictureCompany.getlong(FLD_PICTURE_COMPANY_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstPictureCompany(0),DBException.UNKNOWN);
        }
        return pictureCompany.getOID();
    }
    
    public long updateExc(Entity ent) throws Exception {
          return updateExc((PictureCompany) ent);
    }
    public static long updateExc(PictureCompany pictureCompany)throws DBException{
        try{
            if(pictureCompany.getOID()!=0){
                PstPictureCompany pstPictureCompany = new PstPictureCompany(pictureCompany.getOID());
                pstPictureCompany.setString(FLD_NAMA_PICTURE,pictureCompany.getNamaPicture());
                pstPictureCompany.update();
                
                return pictureCompany.getOID();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstPictureCompany(0),DBException.UNKNOWN);
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
            PstPictureCompany pstPictureCompany = new PstPictureCompany(oid);
            
            pstPictureCompany.delete();
            
        } catch(DBException dbe){
            
            throw dbe;
            
        }catch(Exception e){
            throw new DBException(new PstPictureCompany(0),DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order){
        Vector lists = new Vector();
        
        DBResultSet dbrs=null;
        
        try{
            
            String sql = " SELECT * FROM " + TB_COMPANY_PICTURE;
            
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
            
                PictureCompany pictureCompany = new PictureCompany();
                
                resultToObject(rs,pictureCompany);
                
                lists.add(pictureCompany);
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
    public static void resultToObject(ResultSet rs, PictureCompany pictureCompany){
        
        try {
            
            pictureCompany.setOID(rs.getLong(PstPictureCompany.fieldNames[PstPictureCompany.FLD_PICTURE_COMPANY_ID]));
            pictureCompany.setNamaPicture(rs.getString(PstPictureCompany.fieldNames[PstPictureCompany.FLD_NAMA_PICTURE]));
        } catch(Exception e){
            System.out.println("exc"+e);
        }
    }
    public static boolean checkOID(long mSid){
     
          DBResultSet dbrs = null;
        
        boolean result = false;
        
        try {
            
            String sql = "SELECT * FROM"+TB_COMPANY_PICTURE+"WHERE"
                    + PstPictureCompany.fieldNames[PstPictureCompany.FLD_PICTURE_COMPANY_ID]+"="+ mSid;
            
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
        
            String sql = " SELECT COUNT(" + PstPictureCompany.fieldNames[PstPictureCompany.FLD_PICTURE_COMPANY_ID]+") FROM " + TB_COMPANY_PICTURE;
            
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
                
                    PictureCompany pictureCompany = (PictureCompany) list.get(ls);
                    
                    if (oid == pictureCompany.getOID()){
                    
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
 /**
  * keterangan : untuk delete picture company
  * @param ctx
  * @param pictureCompany 
  */
 public static void deletePictureCompany(PictureCompany pictureCompany,String pathImg){
     String path= pathImg + File.separator+pictureCompany.getNamaPicture();
     System.out.println(path);
     try{
         new File(path).delete(); 
     }catch(Exception exc){
     
         System.out.println("Cannot delete picture"+exc);
     }
    
 }
    
}
