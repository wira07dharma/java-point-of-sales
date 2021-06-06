package com.dimata.posbo.entity.warehouse;

/* package java */
import com.dimata.common.entity.custom.PstDataCustom;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.lang.*;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
 
/* package garment */
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.masterdata.*;
import org.json.JSONObject;

public class PstMatDistributionDetail extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    public static final  String TBL_MAT_DISTRIBUTION_DETAIL = "pos_distribution_detail";
    
    public static final  int FLD_DISTRIBUTION_DETAIL_ID     = 0;
    public static final  int FLD_DISPATCH_MATERIAL_ID       = 1;
    public static final  int FLD_DISTRIBUTION_MATERIAL_ID   = 2;
    public static final  int FLD_LOCATION_ID                = 3;
    public static final  int FLD_MATERIAL_ID                = 4;
    public static final  int FLD_QTY                        = 5;
    
    
    public static final  String[] fieldNames = {
        "DISTRIBUTION_DETAIL_ID",
        "DISPATCH_MATERIAL_ID",
        "DISTRIBUTION_MATERIAL_ID",
        "LOCATION_ID",
        "MATERIAL_ID",
        "QTY"
    };
    
    public static final  int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT
    };
    
    public PstMatDistributionDetail() {
    }
    
    public PstMatDistributionDetail(int i) throws DBException {
        super(new PstMatDistributionDetail());
    }
    
    public PstMatDistributionDetail(String sOid) throws DBException {
        super(new PstMatDistributionDetail(0));
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstMatDistributionDetail(long lOid) throws DBException {
        super(new PstMatDistributionDetail(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        }catch(Exception e) {
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public int getFieldSize(){
        return fieldNames.length;
    }
    
    public String getTableName(){
        return TBL_MAT_DISTRIBUTION_DETAIL;
    }
    
    public String[] getFieldNames(){
        return fieldNames;
    }
    
    public int[] getFieldTypes(){
        return fieldTypes;
    }
    
    public String getPersistentName(){
        return new PstMatDistributionDetail().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception{
        MatDistributionDetail matDistributionDetail = fetchExc(ent.getOID());
        ent = (Entity)matDistributionDetail;
        return matDistributionDetail.getOID();
    }
    
    synchronized public long insertExc(Entity ent) throws Exception{
        return insertExc((MatDistributionDetail) ent);
    }
    
    synchronized public long updateExc(Entity ent) throws Exception{
        return updateExc((MatDistributionDetail) ent);
    }
    
    synchronized public long deleteExc(Entity ent) throws Exception{
        if(ent==null){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public static MatDistributionDetail fetchExc(long oid) throws DBException {
        try {
            MatDistributionDetail matDistributionDetail = new MatDistributionDetail();
            PstMatDistributionDetail pstMatDistributionDetail = new PstMatDistributionDetail(oid);
            matDistributionDetail.setOID(oid);
            
            matDistributionDetail.setDispatchMaterialId(pstMatDistributionDetail.getlong(FLD_DISPATCH_MATERIAL_ID));
            matDistributionDetail.setMaterialId(pstMatDistributionDetail.getlong(FLD_MATERIAL_ID));
            matDistributionDetail.setDistributionMaterialId(pstMatDistributionDetail.getlong(FLD_DISTRIBUTION_MATERIAL_ID));
            matDistributionDetail.setLocationId(pstMatDistributionDetail.getlong(FLD_LOCATION_ID));
            matDistributionDetail.setQty(pstMatDistributionDetail.getdouble(FLD_QTY));
            
            return matDistributionDetail;
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatDistributionDetail(0),DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(MatDistributionDetail matDistributionDetail) throws DBException {
        try {
            PstMatDistributionDetail pstMatDistributionDetail = new PstMatDistributionDetail(0);
            
            pstMatDistributionDetail.setLong(FLD_DISPATCH_MATERIAL_ID, matDistributionDetail.getDispatchMaterialId());
            pstMatDistributionDetail.setLong(FLD_MATERIAL_ID, matDistributionDetail.getMaterialId());
            pstMatDistributionDetail.setLong(FLD_DISTRIBUTION_MATERIAL_ID, matDistributionDetail.getDistributionMaterialId());
            pstMatDistributionDetail.setLong(FLD_LOCATION_ID, matDistributionDetail.getLocationId());
            pstMatDistributionDetail.setDouble(FLD_QTY, matDistributionDetail.getQty());
            
            pstMatDistributionDetail.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatDistributionDetail.getInsertSQL());
            matDistributionDetail.setOID(pstMatDistributionDetail.getlong(FLD_DISTRIBUTION_DETAIL_ID));
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatDistributionDetail(0),DBException.UNKNOWN);
        }
        return matDistributionDetail.getOID();
    }
    
    public static long updateExc(MatDistributionDetail matDistributionDetail) throws DBException {
        long result = 0;
        try {
            if(matDistributionDetail.getOID() != 0) {
                PstMatDistributionDetail pstMatDistributionDetail = new PstMatDistributionDetail(matDistributionDetail.getOID());
                
                pstMatDistributionDetail.setLong(FLD_DISPATCH_MATERIAL_ID, matDistributionDetail.getDispatchMaterialId());
                pstMatDistributionDetail.setLong(FLD_MATERIAL_ID, matDistributionDetail.getMaterialId());
                pstMatDistributionDetail.setLong(FLD_DISTRIBUTION_MATERIAL_ID, matDistributionDetail.getDistributionMaterialId());
                pstMatDistributionDetail.setLong(FLD_LOCATION_ID, matDistributionDetail.getLocationId());
                pstMatDistributionDetail.setDouble(FLD_QTY, matDistributionDetail.getQty());
                
                pstMatDistributionDetail.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMatDistributionDetail.getUpdateSQL());
                result = matDistributionDetail.getOID();
            }
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatDistributionDetail(0),DBException.UNKNOWN);
        }
        return result;
    }
    
    public static long deleteExc(long oid) throws DBException {
        try {
            PstMatDistributionDetail pstMatDistributionDetail = new PstMatDistributionDetail(oid);
            pstMatDistributionDetail.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatDistributionDetail.getDeleteSQL());
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatDistributionDetail(0),DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static Vector listAll(){
        return list(0, 500, "","");
    }
    
    public static Vector list(int limitStart,int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_DISTRIBUTION_DETAIL;
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if(order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL :
                    if(limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
                    break;
                    
                case DBHandler.DBSVR_POSTGRESQL :
                    if(limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " +recordToGet + " OFFSET "+ limitStart ;
                    break;
                    
                case DBHandler.DBSVR_SYBASE :
                    break;
                    
                case DBHandler.DBSVR_ORACLE :
                    break;
                    
                case DBHandler.DBSVR_MSSQL :
                    break;
                    
                default:
                    ;
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                MatDistributionDetail matDistributionDetail = new MatDistributionDetail();
                resultToObject(rs, matDistributionDetail);
                lists.add(matDistributionDetail);
            }
            rs.close();
            return lists;
            
        }catch(Exception e) {
            System.out.println(e);
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    //Untuk menampilkan komplit dengan material dan unit
    public static Vector list(int limitStart,int recordToGet, long oidMatDispatch, long oidLocation) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "+
            " DFI." + fieldNames[FLD_DISTRIBUTION_DETAIL_ID] +
            " , DFI." + fieldNames[FLD_MATERIAL_ID] +
            " , DFI." + fieldNames[FLD_LOCATION_ID] +
            " , DFI." + fieldNames[FLD_QTY] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            " , UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] +
            " , UNT."+PstUnit.fieldNames[PstUnit.FLD_CODE] +
            " FROM (" + TBL_MAT_DISTRIBUTION_DETAIL+ " DFI" +
            " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON DFI." + fieldNames[FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ")" +
            " INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " WHERE DFI." + fieldNames[FLD_DISTRIBUTION_MATERIAL_ID] +
            " = " + oidMatDispatch;
            
            if(oidLocation!=0)
                sql = sql + " AND DFI."+ fieldNames[FLD_LOCATION_ID] +"="+oidLocation;
                
            sql = sql + " ORDER BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
             
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL :
                    if(limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
                    break;
                    
                case DBHandler.DBSVR_POSTGRESQL :
                    if(limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " +recordToGet + " OFFSET "+ limitStart ;
                    break;
                    
                case DBHandler.DBSVR_SYBASE :
                    break;
                    
                case DBHandler.DBSVR_ORACLE :
                    break;
                    
                case DBHandler.DBSVR_MSSQL :
                    break;
                    
                default:
                    ;
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                Vector temp = new Vector();
                MatDistributionDetail dfItem = new MatDistributionDetail();
                Material mat = new Material();
                Unit unit = new Unit();
                
                dfItem.setOID(rs.getLong(fieldNames[FLD_DISTRIBUTION_DETAIL_ID]));
                dfItem.setMaterialId(rs.getLong(fieldNames[FLD_MATERIAL_ID]));
                dfItem.setLocationId(rs.getLong(fieldNames[FLD_LOCATION_ID]));
                dfItem.setQty(rs.getDouble(fieldNames[FLD_QTY]));
                temp.add(dfItem);
                
                mat.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                mat.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                mat.setDefaultCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                mat.setDefaultCostCurrencyId(rs.getLong(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID]));
                temp.add(mat);
                
                unit.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
                temp.add(unit);
                
                lists.add(temp);
            }
            rs.close();
            
        }
        catch(Exception e) {
            lists = new Vector();
            System.out.println(e);
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }
    
    public static void resultToObject(ResultSet rs, MatDistributionDetail matDistributionDetail) {
        try {
            matDistributionDetail.setOID(rs.getLong(PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_DISTRIBUTION_DETAIL_ID]));
            matDistributionDetail.setDispatchMaterialId(rs.getLong(PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_DISPATCH_MATERIAL_ID]));
            matDistributionDetail.setMaterialId(rs.getLong(PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_MATERIAL_ID]));
            matDistributionDetail.setDistributionMaterialId(rs.getLong(PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_DISTRIBUTION_MATERIAL_ID]));
            matDistributionDetail.setLocationId(rs.getLong(PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_LOCATION_ID]));
            matDistributionDetail.setQty(rs.getDouble(PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_QTY]));
        }
        catch(Exception e)
        { }
    }
    
    public static boolean checkOID(long materialDispatchItemId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_DISTRIBUTION_DETAIL+
            " WHERE " + PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_DISTRIBUTION_DETAIL_ID] +
            " = " + materialDispatchItemId;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                result = true;
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println("err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    public static int getCount(String whereClause) {
        int count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT("+ PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_DISTRIBUTION_DETAIL_ID] +
            ") AS CNT FROM " + TBL_MAT_DISTRIBUTION_DETAIL;
            
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                count = rs.getInt("CNT");
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }  
    
    
    /* This method used to find current data */
    public static int findLimitStart( long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0; 
        boolean found =false;
        for(int i=0; (i < size) && !found ; i=i+recordToGet){
            Vector list =  list(i,recordToGet, whereClause, orderClause);
            start = i;
            if(list.size()>0){
                for(int ls=0;ls<list.size();ls++){
                    MatDistributionDetail matDistributionDetail = (MatDistributionDetail)list.get(ls);
                    if(oid == matDistributionDetail.getOID())
                        found=true;
                }
            }
        }
        if((start >= size) && (size > 0))
            start = start - recordToGet;
        
        return start;
    }
    /* This method used to find command where current data */
    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + (recordToGet - mdl);
        if(start == 0)
            cmd =  Command.FIRST;
        else{
            if(start == (vectSize-recordToGet))
                cmd = Command.LAST;
            else{
                start = start + recordToGet;
                if(start <= (vectSize - recordToGet)){
                    cmd = Command.NEXT;
                    //System.out.println("next.......................");
                }else{
                    start = start - recordToGet;
                    if(start > 0){
                        cmd = Command.PREV;
                        //System.out.println("prev.......................");
                    }
                }
            }
        }
        
        return cmd;
    }
    
    public static long deleteExcByParent(long oid) throws DBException {
        long hasil = 0;
        try {
            String sql = "DELETE FROM " + TBL_MAT_DISTRIBUTION_DETAIL+
            " WHERE " + fieldNames[FLD_DISTRIBUTION_MATERIAL_ID] +
            " = "  + oid;
            int result = execUpdate(sql);
            hasil = oid;
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatDistributionDetail(0),DBException.UNKNOWN);
        }
        return hasil;
    }
    
    /** di gunakan untuk mencari object receive item
     * @param invoice = invoice supplier
     * @param oidMaterial = oid material/barang
     * @return
     */
    public static MatDistributionDetail getObjectDistributionItem(long oidMaterial, long oidDistribusi){
        MatDistributionDetail matDistributionDetail = new MatDistributionDetail();
        try{
            String whereClause = PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_DISTRIBUTION_MATERIAL_ID]+"="+oidDistribusi+
            " AND "+PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_MATERIAL_ID]+"="+oidMaterial;
            Vector vect = PstMatDistributionDetail.list(0,0,whereClause,"");
            if(vect!=null && vect.size()>0){
                matDistributionDetail = (MatDistributionDetail)vect.get(0);
            }
            
        }catch(Exception e){}
        return matDistributionDetail;
    }


   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MatDistributionDetail matDistributionDetail = PstMatDistributionDetail.fetchExc(oid);
         object.put(PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_DISTRIBUTION_DETAIL_ID], matDistributionDetail.getOID());
         object.put(PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_DISPATCH_MATERIAL_ID], matDistributionDetail.getDispatchMaterialId());
         object.put(PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_DISTRIBUTION_MATERIAL_ID], matDistributionDetail.getDistributionMaterialId());
         object.put(PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_LOCATION_ID], matDistributionDetail.getLocationId());
         object.put(PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_MATERIAL_ID], matDistributionDetail.getMaterialId());
         object.put(PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_QTY], matDistributionDetail.getQty());
      }catch(Exception exc){}
      return object;
   }
}
