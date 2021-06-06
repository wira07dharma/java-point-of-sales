
/* Created on 	:  [date] [time] AM/PM
 *
 * @author	 :
 * @version	 :
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.posbo.entity.masterdata;

/* package java */
import com.dimata.common.entity.custom.PstDataCustom;
import java.io.*
;
import java.sql.*
;import java.util.*
;import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;

/* package posbo */
//import com.dimata.posbo.db.DBHandler;
//import com.dimata.posbo.db.DBException;
//import com.dimata.posbo.db.DBLogger;
import com.dimata.posbo.entity.masterdata.*;
import org.json.JSONObject;

public class PstDiscountMapping extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    //public static final  String TBL_POS_DISCOUNT_MAPPING = "POS_DISCOUNT_MAPPING";
    public static final  String TBL_POS_DISCOUNT_MAPPING = "pos_discount_mapping";
    
    public static final  int FLD_DISCOUNT_TYPE_ID = 0;
    public static final  int FLD_MATERIAL_ID = 1;
    public static final  int FLD_CURRENCY_TYPE_ID = 2;
    //public static final  int FLD_DISCOUNT_TYPE = 3;
    //public static final  int FLD_DISCOUNT = 4;
    public static final  int FLD_DISCOUNT_PCT = 3;
    public static final  int FLD_DISCOUNT_VALUE = 4;
    
    
    public static final  String[] fieldNames = {
        "DISCOUNT_TYPE_ID",
        "MATERIAL_ID",
        "CURRENCY_TYPE_ID",
        "DISCOUNT_PCT",
        "DISCOUNT_VALUE"
        /*
        "DISCOUNT_TYPE",
        "DISCOUNT"*/
    };
    
    public static final  int[] fieldTypes = {
        TYPE_PK + TYPE_FK + TYPE_LONG,
        TYPE_PK + TYPE_FK + TYPE_LONG,
        TYPE_PK + TYPE_FK + TYPE_LONG,
        /*TYPE_INT,
        TYPE_FLOAT*/
        TYPE_FLOAT,
        TYPE_FLOAT
    };
    
    public PstDiscountMapping(){
    }
    
    public PstDiscountMapping(int i) throws DBException {
        super(new PstDiscountMapping());
    }
    
    public PstDiscountMapping(String sOid) throws DBException {
        super(new PstDiscountMapping(0));
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstDiscountMapping(long discountTypeId, long materialId, long currencyTypeId) throws DBException {
        super(new PstDiscountMapping(0));
        long[] arrId = {discountTypeId, materialId, currencyTypeId};
        if(!locate(arrId))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public int getFieldSize(){
        return fieldNames.length;
    }
    
    public String getTableName(){
        return TBL_POS_DISCOUNT_MAPPING;
    }
    
    public String[] getFieldNames(){
        return fieldNames;
    }
    
    public int[] getFieldTypes(){
        return fieldTypes;
    }
    
    public String getPersistentName(){
        return new PstDiscountMapping().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception{
        DiscountMapping discountmapping = fetchExc(ent.getOID(0), ent.getOID(1), ent.getOID(2) );
        ent = (Entity)discountmapping;
        return discountmapping.getOID(0);
    }
    
    public long insertExc(Entity ent) throws Exception{
        return insertExc((DiscountMapping) ent);
    }
    
    public long updateExc(Entity ent) throws Exception{
        return updateExc((DiscountMapping) ent);
    }
    
    public long deleteExc(Entity ent) throws Exception{
        if(ent==null){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent);
    }
    
    public static DiscountMapping fetchExc(long discountTypeId, long materialId, long currencyTypeId) throws DBException{
        try{
            DiscountMapping discountmapping = new DiscountMapping();
            PstDiscountMapping pstDiscountMapping = new PstDiscountMapping(discountTypeId, materialId, currencyTypeId);
            discountmapping.setDiscountTypeId(discountTypeId);
            discountmapping.setMaterialId(materialId);
            discountmapping.setCurrencyTypeId(currencyTypeId);
            
            //discountmapping.setDiscountType(pstDiscountMapping.getInt(FLD_DISCOUNT_TYPE));
            //discountmapping.setDiscount(pstDiscountMapping.getdouble(FLD_DISCOUNT));
            discountmapping.setDiscountPct(pstDiscountMapping.getdouble(FLD_DISCOUNT_PCT));
            discountmapping.setDiscountValue(pstDiscountMapping.getdouble(FLD_DISCOUNT_VALUE));
            
            return discountmapping;
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstDiscountMapping(0),DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(DiscountMapping discountmapping) throws DBException{
        try{
            PstDiscountMapping pstDiscountMapping = new PstDiscountMapping(0);
            
            pstDiscountMapping.setLong(FLD_DISCOUNT_TYPE_ID, discountmapping.getDiscountTypeId());
            pstDiscountMapping.setLong(FLD_MATERIAL_ID, discountmapping.getMaterialId());
            pstDiscountMapping.setLong(FLD_CURRENCY_TYPE_ID, discountmapping.getCurrencyTypeId());
            //pstDiscountMapping.setInt(FLD_DISCOUNT_TYPE, discountmapping.getDiscountType());
            //pstDiscountMapping.setDouble(FLD_DISCOUNT, discountmapping.getDiscount());
            pstDiscountMapping.setDouble(FLD_DISCOUNT_PCT, discountmapping.getDiscountPct());
            pstDiscountMapping.setDouble(FLD_DISCOUNT_VALUE, discountmapping.getDiscountValue());

           // pstDiscountMapping.setData_sync(true);

            pstDiscountMapping.insert();
            long oidDataSync = PstDataSyncSql.insertExc(pstDiscountMapping.getInsertSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstDiscountMapping.getInsertSQL());
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstDiscountMapping(0),DBException.UNKNOWN);
        }
        return discountmapping.getOID();
    }
    
    public static long updateExc(DiscountMapping discountmapping) throws DBException{
        try{
            if(discountmapping.getOID() != 0){
                PstDiscountMapping pstDiscountMapping = new PstDiscountMapping(discountmapping.getDiscountTypeId(), discountmapping.getMaterialId(), discountmapping.getCurrencyTypeId());
                
                //pstDiscountMapping.setInt(FLD_DISCOUNT_TYPE, discountmapping.getDiscountType());
                //pstDiscountMapping.setDouble(FLD_DISCOUNT, discountmapping.getDiscount());
                pstDiscountMapping.setDouble(FLD_DISCOUNT_PCT, discountmapping.getDiscountPct());
                pstDiscountMapping.setDouble(FLD_DISCOUNT_VALUE, discountmapping.getDiscountValue());
                
                pstDiscountMapping.update();

                long oidDataSync=PstDataSyncSql.insertExc(pstDiscountMapping.getUpdateSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstDiscountMapping.getUpdateSQL());
                return discountmapping.getDiscountTypeId();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstDiscountMapping(0),DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long discountTypeId, long materialId, long currencyTypeId) throws DBException{
        try{
            PstDiscountMapping pstDiscountMapping = new PstDiscountMapping(discountTypeId, materialId, currencyTypeId);
            pstDiscountMapping.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstDiscountMapping.getDeleteSQL());
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstDiscountMapping(0),DBException.UNKNOWN);
        }
        return discountTypeId;
    }
    
    public static Vector listAll(){
        return list(0, 500, "","");
    }
    
    public static Vector list(int limitStart,int recordToGet, String whereClause, String order){
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_DISCOUNT_MAPPING;
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if(order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            if(limitStart == 0 && recordToGet == 0)
                sql = sql + "";
            else
                sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                DiscountMapping discountmapping = new DiscountMapping();
                resultToObject(rs, discountmapping);
                lists.add(discountmapping);
            }
            rs.close();
            return lists;
            
        }catch(Exception e) {
            System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    private static void resultToObject(ResultSet rs, DiscountMapping discountmapping){
        try{
            discountmapping.setDiscountTypeId(rs.getLong(PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_DISCOUNT_TYPE_ID]));
            discountmapping.setMaterialId(rs.getLong(PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_MATERIAL_ID]));
            discountmapping.setCurrencyTypeId(rs.getLong(PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_CURRENCY_TYPE_ID]));
            //discountmapping.setDiscountType(rs.getInt(PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_DISCOUNT_TYPE]));
            //discountmapping.setDiscount(rs.getDouble(PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_DISCOUNT]));
            discountmapping.setDiscountPct(rs.getDouble(PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_DISCOUNT_PCT]));
            discountmapping.setDiscountValue(rs.getDouble(PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_DISCOUNT_VALUE]));
            
        }catch(Exception e){ }
    }
    
    public static boolean checkOID(long discountTypeId, long materialId, long currencyTypeId){
        DBResultSet dbrs = null;
        boolean result = false;
        try{
            String sql = "SELECT * FROM " + TBL_POS_DISCOUNT_MAPPING + " WHERE " +
            PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_DISCOUNT_TYPE_ID] + " = " + discountTypeId + " AND " +
            PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_MATERIAL_ID] + " = " + materialId + " AND " +
            PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_CURRENCY_TYPE_ID] + " = " + currencyTypeId;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) { result = true; }
            rs.close();
        }catch(Exception e){
            System.out.println("err : "+e.toString());
        }finally{
            DBResultSet.close(dbrs);
            return result;
        }
    }
    
    public static int getCount(String whereClause){
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT("+ PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_CURRENCY_TYPE_ID] + ") FROM " + TBL_POS_DISCOUNT_MAPPING;
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            int count = 0;
            while(rs.next()) { count = rs.getInt(1); }
            
            rs.close();
            return count;
        }catch(Exception e) {
            return 0;
        }finally {
            DBResultSet.close(dbrs);
        }
    }
    
    
    public static long deleteByDiscountTypeId(long discountTypeId) {
        try {
            String sql = "DELETE FROM " + TBL_POS_DISCOUNT_MAPPING+
            " WHERE "+PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_DISCOUNT_TYPE_ID] +
            " = " + discountTypeId ;
            
            DBHandler.execQueryResult(sql);
            return discountTypeId;
        }catch(Exception exc) {
            System.out.println(" exception delete by Owner ID "+exc.toString());
        }
        return 0;
    }
    public static long deleteByMaterialId(long materialId) {
        try {
            String sql = "DELETE FROM " + TBL_POS_DISCOUNT_MAPPING+
            " WHERE "+PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_MATERIAL_ID] +
            " = " + materialId ;
            //PstDiscountMapping.setData_sync1(true);

            int delete = DBHandler.execUpdate(sql);
            long oidDataSync =PstDataSyncSql.insertExc(sql);//isnert ke tabel data_sync_sql
            PstDataSyncStatus.insertExc(oidDataSync);//insert ke tabel data_sync_status

            return materialId;
        }catch(Exception exc) {
            System.out.println(" exception delete by Owner ID "+exc.toString());
        }
        return 0;
    }
    public static long deleteByCurrencyTypeId(long currencyTypeId) {
        try {
            String sql = "DELETE FROM " + TBL_POS_DISCOUNT_MAPPING+
            " WHERE "+PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_CURRENCY_TYPE_ID] +
            " = " + currencyTypeId ;
            
            int a = DBHandler.execUpdate(sql);
            return currencyTypeId;
        }catch(Exception exc) {
            System.out.println(" exception delete by Owner ID "+exc.toString());
        }
        return 0;
    }
    /* This method used to find current data */
    public static int findLimitStart(long discountTypeId, long materialId, long currencyTypeId, int recordToGet, String whereClause){
        String order = "";
        int size = getCount(whereClause);
        int start = 0;
        boolean found =false;
        for(int i=0; (i < size) && !found ; i=i+recordToGet){
            Vector list =  list(i,recordToGet, whereClause, order);
            start = i;
            if(list.size()>0){
                for(int ls=0;ls<list.size();ls++){
                    DiscountMapping discountmapping = (DiscountMapping)list.get(ls);
                    //if(oid == discountmapping.getOID())
                    if(discountTypeId == discountmapping.getDiscountTypeId()&&materialId==discountmapping.getMaterialId()&&currencyTypeId==discountmapping.getCurrencyTypeId())
                        found=true;
                }
            }
        }
        if((start >= size) && (size > 0))
            start = start - recordToGet;
        
        return start;
    }
    
    public static Vector getDiscount(Vector result,long oidMaterial,long oidDiscType,long oidCurrType){
        Vector list = new Vector(1,1);
        if(result!=null&&result.size()>0){
            for(int i=0;i<result.size();i++){
                DiscountMapping discMapping = (DiscountMapping)result.get(i);               
                if(oidMaterial==discMapping.getMaterialId()&&oidDiscType==discMapping.getDiscountTypeId()&&oidCurrType==discMapping.getCurrencyTypeId()){
                    list.add(""+discMapping.getDiscountPct());
                    list.add(""+discMapping.getDiscountValue());
                    return list;
                }
            }
        }
        return list;
    }
  
    public static JSONObject fetchJSON(long discountTypeId, long materialId, long currencyTypeId){
            JSONObject object = new JSONObject();
            try {
                DiscountMapping discountMapping = PstDiscountMapping.fetchExc(discountTypeId, materialId, currencyTypeId);
                object.put(PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_DISCOUNT_TYPE_ID], discountMapping.getOID());
                object.put(PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_DISCOUNT_PCT], discountMapping.getDiscountPct());
                object.put(PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_CURRENCY_TYPE_ID], discountMapping.getCurrencyTypeId());
                object.put(PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_DISCOUNT_VALUE], discountMapping.getDiscountValue());
                object.put(PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_MATERIAL_ID], discountMapping.getMaterialId());
            }catch(Exception exc){}

            return object;
        }
}
