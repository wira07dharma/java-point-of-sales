/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author PT. Dimata
 */

/* package java */
import com.dimata.common.entity.custom.PstDataCustom;
import java.sql.*;
import java.util.*;
/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;

/* package posbo */
//import com.dimata.posbo.db.DBHandler;
//import com.dimata.posbo.db.DBException;
//import com.dimata.posbo.db.DBLogger;
//import com.dimata.posbo.entity.masterdata.*;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.common.entity.payment.PstDiscountType;
import org.json.JSONObject;

public class PstDiscountQtyMapping extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
     //public static final  String TBL_POS_DISCOUNT_MAPPING = "POS_DISCOUNT_QTY_MAPPING";
    public static final  String TBL_POS_DISCOUNT_QTY_MAPPING = "pos_discount_qty_mapping";

    public static final  int FLD_DISCOUNT_TYPE_ID = 0;
    public static final  int FLD_CURRENCY_TYPE_ID = 1;
    public static final  int FLD_LOCATION_ID = 2;
    public static final  int FLD_MATERIAL_ID = 3;
    public static final  int FLD_START_QTY = 4;
    public static final  int FLD_TO_QTY = 5;
    public static final  int FLD_DISCOUNT_VALUE = 6;
    public static final  int FLD_DISCOUNT_TYPE = 7;
    
    public static final int DISC_PERSEN = 0;
    public static final int DISC_ITEM_UNIT = 1;
    public static final int DISC_ITEM_BELANJA = 2;

    public static final  String[] fieldNames = {
        "DISCOUNT_TYPE_ID",
        "CURRENCY_TYPE_ID",
        "LOCATION_ID",
        "MATERIAL_ID",
        "START_QTY",
        "TO_QTY",
        "DISCOUNT_VALUE",
        "DISCOUNT_TYPE"
    };

    public static final  int[] fieldTypes = {
        TYPE_PK + TYPE_FK + TYPE_LONG,
        TYPE_PK + TYPE_FK + TYPE_LONG,
        TYPE_PK + TYPE_FK + TYPE_LONG,
        TYPE_PK + TYPE_FK + TYPE_LONG,
        TYPE_PK + TYPE_FK + TYPE_FLOAT,
        TYPE_PK + TYPE_FK + TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT

    };

     //discount_type
    public static final int PCT=0;
    public static final int ITEM_UNIT=1;
    public static final int ITEM_BUYING=2;

    public static final String[] typeDiscount = {
        "(%) Per Item Unit",
        "(Rp) Per Item Unit",
        "(Rp) Per Kode Barang"
    };

    public PstDiscountQtyMapping(){
    }

    public PstDiscountQtyMapping(int i) throws DBException {
        super(new PstDiscountQtyMapping());
    }

    public PstDiscountQtyMapping(String sOid) throws DBException {
        super(new PstDiscountQtyMapping(0));
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
  // public PstDiscountQtyMapping(long lOid) throws DBException {
       // super(new PstDiscountQtyMapping(0));
       // String sOid = "0";
        //try {
           // sOid = String.valueOf(lOid);
      //  } catch (Exception e) {
           // throw new DBException(this, DBException.RECORD_NOT_FOUND);
       // }
        //if (!locate(sOid))
            //throw new DBException(this, DBException.RECORD_NOT_FOUND);
       // else
           // return;
   // }

    public PstDiscountQtyMapping(long discountTypeId, long currencyTypeId, long locationId, long materialId) throws DBException {
      super(new PstDiscountQtyMapping(0));
        long[] arrId = {discountTypeId, currencyTypeId, locationId, materialId};
        if(!locate(arrId))
          throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
          return;
  }

    public int getFieldSize(){
        return fieldNames.length;
    }

    public String getTableName(){
        return TBL_POS_DISCOUNT_QTY_MAPPING;
    }

    public String[] getFieldNames(){
        return fieldNames;
    }

    public int[] getFieldTypes(){
        return fieldTypes;
    }

    public String getPersistentName(){
        return new PstDiscountQtyMapping().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception{
        DiscountQtyMapping discountqtymapping = fetchExc(ent.getOID(0), ent.getOID(1), ent.getOID(2), ent.getOID(3) );
        //DiscountQtyMapping discountqtymapping = fetchExc(ent.getOID());
        ent = (Entity)discountqtymapping;
        return discountqtymapping.getOID(0);
    }

    
    
    public long insertExc(Entity ent) throws Exception{
        return insertExc((DiscountQtyMapping) ent);
    }

    public long updateExc(Entity ent) throws Exception{
        return updateExc((DiscountQtyMapping) ent);
    }

    public long deleteExc(Entity ent) throws Exception{
        if(ent==null){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent);
    }

      public static DiscountQtyMapping fetchExc(long discountTypeId, long currencyTypeId, long locationId, long materialId) throws DBException{
      //public static DiscountQtyMapping fetchExc(long oid) throws DBException{
        try{
            DiscountQtyMapping discountqtymapping = new DiscountQtyMapping();
            //PstDiscountQtyMapping pstDiscountQtyMapping = new PstDiscountQtyMapping(oid);
            PstDiscountQtyMapping pstDiscountQtyMapping = new PstDiscountQtyMapping(discountTypeId, currencyTypeId, locationId, materialId);
            discountqtymapping.setDiscountTypeId(discountTypeId);
            discountqtymapping.setCurrencyTypeId(currencyTypeId);
            discountqtymapping.setLocationId(locationId);
            discountqtymapping.setMaterialId(materialId);
            //discountqtymapping.setDiscountTypeId(pstDiscountQtyMapping.getlong(FLD_DISCOUNT_TYPE_ID));
            //discountqtymapping.setCurrencyTypeId(pstDiscountQtyMapping.getlong(FLD_CURRENCY_TYPE_ID));
            //discountqtymapping.setLocationId(pstDiscountQtyMapping.getlong(FLD_LOCATION_ID));
            //discountqtymapping.setMaterialId(pstDiscountQtyMapping.getlong(FLD_MATERIAL_ID));
            discountqtymapping.setStartQty(pstDiscountQtyMapping.getdouble(FLD_START_QTY));
            discountqtymapping.setToQty(pstDiscountQtyMapping.getdouble(FLD_TO_QTY));
            discountqtymapping.setDiscountValue(pstDiscountQtyMapping.getdouble(FLD_DISCOUNT_VALUE));
            discountqtymapping.setDiscountType(pstDiscountQtyMapping.getInt(FLD_DISCOUNT_TYPE));
            return discountqtymapping;

        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstDiscountQtyMapping(0),DBException.UNKNOWN);
        }
    }

      
      
    
      

    public static long insertExc(DiscountQtyMapping discountqtymapping) throws DBException{
        try{
            PstDiscountQtyMapping pstDiscountQtyMapping = new PstDiscountQtyMapping(0);

            pstDiscountQtyMapping.setLong(FLD_DISCOUNT_TYPE_ID, discountqtymapping.getDiscountTypeId());
            pstDiscountQtyMapping.setLong(FLD_CURRENCY_TYPE_ID, discountqtymapping.getCurrencyTypeId());
            pstDiscountQtyMapping.setLong(FLD_LOCATION_ID, discountqtymapping.getLocationId());
            pstDiscountQtyMapping.setLong(FLD_MATERIAL_ID, discountqtymapping.getMaterialId());
            pstDiscountQtyMapping.setDouble(FLD_START_QTY, discountqtymapping.getStartQty());
            pstDiscountQtyMapping.setDouble(FLD_TO_QTY, discountqtymapping.getToQty());
            pstDiscountQtyMapping.setDouble(FLD_DISCOUNT_VALUE, discountqtymapping.getDiscountValue());
            pstDiscountQtyMapping.setInt(FLD_DISCOUNT_TYPE, discountqtymapping.getDiscountType());

            pstDiscountQtyMapping.insert();
            
            long oidDataSync=PstDataSyncSql.insertExc(pstDiscountQtyMapping.getInsertSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstDiscountQtyMapping.getInsertSQL());

        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstDiscountQtyMapping(0),DBException.UNKNOWN);
        }
        return discountqtymapping.getOID();
    }

    public static long updateExc(DiscountQtyMapping discountqtymapping) throws DBException{
        try{
            if(discountqtymapping.getOID() != 0){
                PstDiscountQtyMapping pstDiscountQtyMapping = new PstDiscountQtyMapping(discountqtymapping.getDiscountTypeId(), discountqtymapping.getCurrencyTypeId(), discountqtymapping.getLocationId(),discountqtymapping.getMaterialId());

                pstDiscountQtyMapping.setDouble(FLD_START_QTY, discountqtymapping.getStartQty());
                pstDiscountQtyMapping.setDouble(FLD_TO_QTY, discountqtymapping.getToQty());
                pstDiscountQtyMapping.setDouble(FLD_DISCOUNT_VALUE, discountqtymapping.getDiscountValue());
                pstDiscountQtyMapping.setInt(FLD_DISCOUNT_TYPE, discountqtymapping.getDiscountType());

                pstDiscountQtyMapping.update();

                long oidDataSync=PstDataSyncSql.insertExc(pstDiscountQtyMapping.getUpdateSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstDiscountQtyMapping.getUpdateSQL());
                return discountqtymapping.getDiscountTypeId();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstDiscountQtyMapping(0),DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long discountTypeId, long currencyTypeId, long locationId, long materialId) throws DBException{
        try{
            PstDiscountQtyMapping pstDiscountQtyMapping = new PstDiscountQtyMapping(discountTypeId, currencyTypeId, locationId, materialId);
            pstDiscountQtyMapping.delete();

            long oidDataSync = PstDataSyncSql.insertExc(pstDiscountQtyMapping.getDeleteSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstDiscountQtyMapping.getDeleteSQL());

        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstDiscountQtyMapping(0),DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_POS_DISCOUNT_QTY_MAPPING;
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
                DiscountQtyMapping discountqtymapping = new DiscountQtyMapping();
                resultToObject(rs, discountqtymapping);
                lists.add(discountqtymapping);
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

    public static Vector listDiscQtyAll(int limitStart,int recordToGet, String whereClause, String order){
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " +
                         " DISCQTY." + fieldNames[FLD_DISCOUNT_TYPE_ID]+
                         ", DISCQTY." + fieldNames[FLD_CURRENCY_TYPE_ID]+
                         ", DISCQTY." + fieldNames[FLD_LOCATION_ID]+
                         ", DISCQTY." + fieldNames[FLD_MATERIAL_ID]+
                         //" CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE] +
                         //", DISC." + PstDiscountType.fieldNames[PstDiscountType.FLD_CODE] +
                         //", LOC." + PstLocation.fieldNames[PstLocation.FLD_CODE] +
                         //", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                         ", DISCQTY." + fieldNames[FLD_START_QTY] +
                         ", DISCQTY." + fieldNames[FLD_TO_QTY] +
                         ", DISCQTY." + fieldNames[FLD_DISCOUNT_VALUE]+
                         ", DISCQTY." + fieldNames[FLD_DISCOUNT_TYPE] +
                         " FROM " + TBL_POS_DISCOUNT_QTY_MAPPING + " DISCQTY " +
                         " INNER JOIN " + PstDiscountType.TBL_POS_DISCOUNT_TYPE+ " DISC " +
                         " ON DISCQTY." + fieldNames[FLD_DISCOUNT_TYPE_ID] +
                         " = DISC." + PstDiscountType.fieldNames[PstDiscountType.FLD_DISCOUNT_TYPE_ID] +
                         " INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE+ " CURR " +
                         " ON DISCQTY." + fieldNames[FLD_CURRENCY_TYPE_ID] +
                         " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
                         " INNER JOIN " + PstLocation.TBL_P2_LOCATION+ " LOC " +
                         " ON DISCQTY." + fieldNames[FLD_LOCATION_ID] +
                         " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] +
                         " INNER JOIN " + PstMaterial.TBL_MATERIAL+ " MAT " +
                         " ON DISCQTY." + fieldNames[FLD_MATERIAL_ID] +
                         " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];

            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if(order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            if(limitStart == 0 && recordToGet == 0)
                sql = sql + "";
            else
                sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;

            dbrs = DBHandler.execQueryResult(sql);
            System.out.println("SQL List Disc Qty : " + sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                //Vector temp = new Vector();
                DiscountQtyMapping discountqtymapping = new DiscountQtyMapping();
                //DiscountType discountType = new DiscountType();
                //CurrencyType currencyType = new CurrencyType();
                //Location location = new Location();
                //Material material = new Material();

                //currencyType.setCode(rs.getString(1));
                //temp.add(currencyType);

                //discountType.setCode(rs.getString(2));
                //temp.add(discountType);

                //location.setCode(rs.getString(3));
                //temp.add(location);

                //material.setName(rs.getString(4));
               // temp.add(material);
                discountqtymapping.setDiscountTypeId(rs.getLong(1));
                discountqtymapping.setCurrencyTypeId(rs.getLong(2));
                discountqtymapping.setLocationId(rs.getLong(3));
                discountqtymapping.setMaterialId(rs.getLong(4));
                discountqtymapping.setStartQty(rs.getDouble(5));
                discountqtymapping.setToQty(rs.getDouble(6));
                discountqtymapping.setDiscountValue(rs.getDouble(7));
                discountqtymapping.setDiscountType(rs.getInt(8));
                lists.add(discountqtymapping);

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

      
    private static void resultToObject(ResultSet rs, DiscountQtyMapping discountqtymapping){
        try{
            discountqtymapping.setDiscountTypeId(rs.getLong(PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_TYPE_ID]));
            discountqtymapping.setCurrencyTypeId(rs.getLong(PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_CURRENCY_TYPE_ID]));
            discountqtymapping.setLocationId(rs.getLong(PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_LOCATION_ID]));
            discountqtymapping.setMaterialId(rs.getLong(PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_MATERIAL_ID]));
            discountqtymapping.setStartQty(rs.getDouble(PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_START_QTY]));
            discountqtymapping.setToQty(rs.getDouble(PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_TO_QTY]));
            discountqtymapping.setDiscountValue(rs.getDouble(PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_VALUE]));
            discountqtymapping.setDiscountType(rs.getInt(PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_TYPE]));
            

        }catch(Exception e){ }
    }

    public static boolean checkOID(long discountTypeId, long currencyTypeId, long locationId, long materialId){
        DBResultSet dbrs = null;
        boolean result = false;
        try{
            String sql = "SELECT * FROM " + TBL_POS_DISCOUNT_QTY_MAPPING + " WHERE " +
            PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_TYPE_ID] + " = " + discountTypeId + " AND " +
            PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_CURRENCY_TYPE_ID] + " = " + currencyTypeId + " AND " +
            PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_LOCATION_ID] + " = " + locationId + " AND " +
            PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_MATERIAL_ID] + " = " + materialId;

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
            String sql = "SELECT COUNT("+ PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_CURRENCY_TYPE_ID] + ") FROM " + TBL_POS_DISCOUNT_QTY_MAPPING;
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
 
   public static int deleteDiscountQtyMapping(long discountTypeId, long currencyTypeId, long locationId, long materialId) {
        
        DBResultSet dbrs = null;
       try {
          String sql = "DELETE FROM " + TBL_POS_DISCOUNT_QTY_MAPPING+
          " WHERE "+PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_TYPE_ID] +
          " = " + discountTypeId +
          " AND "+PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_CURRENCY_TYPE_ID] +
          " = " + currencyTypeId +
          " AND "+PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_LOCATION_ID] +
          " = " + locationId +
          " AND "+PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_MATERIAL_ID] +
          " = " + materialId ;

         DBHandler.execUpdate(sql);

         long oidDataSync = PstDataSyncSql.insertExc(sql);
         PstDataSyncStatus.insertExc(oidDataSync);
             
        }catch(Exception e) {
            System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }
   

    public static long deleteByDiscountTypeId(long discountTypeId) {
        try {
            String sql = "DELETE FROM " + TBL_POS_DISCOUNT_QTY_MAPPING+
            " WHERE "+PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_TYPE_ID] +
            " = " + discountTypeId ;
             int a = DBHandler.execUpdate(sql);

            long oidDataSync = PstDataSyncSql.insertExc(sql);
            PstDataSyncStatus.insertExc(oidDataSync);
            return discountTypeId;
        }catch(Exception exc) {
            System.out.println(" exception delete by Owner ID "+exc.toString());
        }
        return 0;
    }

    public static long deleteByCurrencyTypeId(long currencyTypeId) {
        try {
            String sql = "DELETE FROM " + TBL_POS_DISCOUNT_QTY_MAPPING+
            " WHERE "+PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_CURRENCY_TYPE_ID] +
            " = " + currencyTypeId ;

             int a = DBHandler.execUpdate(sql);

            long oidDataSync = PstDataSyncSql.insertExc(sql);
            PstDataSyncStatus.insertExc(oidDataSync);
            return currencyTypeId;
        }catch(Exception exc) {
            System.out.println(" exception delete by Owner ID "+exc.toString());
        }
        return 0;
    }

    public static long deleteByLocationId(long locationId) {
        try {
            String sql = "DELETE FROM " + TBL_POS_DISCOUNT_QTY_MAPPING+
            " WHERE "+PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_LOCATION_ID] +
            " = " + locationId ;

            int a = DBHandler.execUpdate(sql);

            long oidDataSync = PstDataSyncSql.insertExc(sql);
            PstDataSyncStatus.insertExc(oidDataSync);
            return locationId;
        }catch(Exception exc) {
            System.out.println(" exception delete by Owner ID "+exc.toString());
        }
        return 0;
    }

    public static long deleteByMaterialId(long materialId) {
        try {
            String sql = "DELETE FROM " + TBL_POS_DISCOUNT_QTY_MAPPING+
            " WHERE "+PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_MATERIAL_ID] +
            " = " + materialId ;

            int delete = DBHandler.execUpdate(sql);

            long oidDataSync = PstDataSyncSql.insertExc(sql);
            PstDataSyncStatus.insertExc(oidDataSync);
            return materialId;
        }catch(Exception exc) {
            System.out.println(" exception delete by Owner ID "+exc.toString());
        }
        return 0;
    }

    /* This method used to find current data */
    public static int findLimitStart(long discountTypeId, long currencyTypeId, long locationId, long materialId,  int recordToGet, String whereClause){
        String order = "";
        int size = getCount(whereClause);
        int start = 0;
        boolean found =false;
        for(int i=0; (i < size) && !found ; i=i+recordToGet){
            Vector list =  list(i,recordToGet, whereClause, order);
            start = i;
            if(list.size()>0){
                for(int ls=0;ls<list.size();ls++){
                    DiscountQtyMapping discountqtymapping = (DiscountQtyMapping)list.get(ls);
                    //if(oid == discountqtymapping.getOID())
                    if(discountTypeId == discountqtymapping.getDiscountTypeId()&&currencyTypeId==discountqtymapping.getCurrencyTypeId()&&locationId==discountqtymapping.getLocationId()&&materialId==discountqtymapping.getMaterialId())
                        found=true;
                }
            }
        }
        if((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }

    public static Vector getDiscountQty(Vector result,long oidMaterial,long oidDiscType,long oidCurrType, long oidLocation){
        Vector list = new Vector(1,1);
        if(result!=null&&result.size()>0){
            for(int i=0;i<result.size();i++){
                DiscountQtyMapping discQtyMapping = (DiscountQtyMapping)result.get(i);
                if(oidMaterial==discQtyMapping.getMaterialId()&&oidDiscType==discQtyMapping.getDiscountTypeId()&&oidCurrType==discQtyMapping.getCurrencyTypeId()&&oidLocation==discQtyMapping.getLocationId()){
                    list.add(""+discQtyMapping.getStartQty());
                    list.add(""+discQtyMapping.getToQty());
                    list.add(""+discQtyMapping.getDiscountValue());
                    list.add(""+discQtyMapping.getDiscountType());

                    return list;
                }
            }
        }
        return list;
    }
  
    public static JSONObject fetchJSON(long discountTypeId, long currencyTypeId, long locationId, long materialId){
            JSONObject object = new JSONObject();
            try {
                DiscountQtyMapping discountQtyMapping = PstDiscountQtyMapping.fetchExc(discountTypeId, currencyTypeId, locationId, materialId) ;
                object.put(PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_TYPE_ID], discountQtyMapping.getDiscountTypeId());
                object.put(PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_LOCATION_ID], discountQtyMapping.getCurrencyTypeId());
                object.put(PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_MATERIAL_ID], discountQtyMapping.getMaterialId());
                object.put(PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_TYPE], discountQtyMapping.getDiscountType());
                object.put(PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_VALUE], discountQtyMapping.getDiscountValue());
                object.put(PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_START_QTY], discountQtyMapping.getStartQty());
                object.put(PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_TO_QTY], discountQtyMapping.getToQty());
                

            }catch(Exception exc){}

            return object;
        }
}
