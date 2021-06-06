package com.dimata.posbo.entity.masterdata;

/* package java */

import com.dimata.common.entity.custom.PstDataCustom;
import java.sql.*;
import java.util.*;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import org.json.JSONObject;

public class PstUnit extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    //public static final String TBL_P2_UNIT = "POS_UNIT";
    public static final String TBL_P2_UNIT = "pos_unit";
    
    public static final int FLD_UNIT_ID = 0;
    public static final int FLD_CODE = 1;
    public static final int FLD_NAME = 2;
    public static final int FLD_QTY_PER_BASE_UNIT = 3;
    public static final int FLD_BASE_UNIT_ID = 4;

    private static Hashtable qtyPerBaseUnit = null;
    
    private static Hashtable unitCode = null;

    public static final String[] fieldNames = {
        "UNIT_ID",
        "CODE",
        "NAME",
        "QTY_PER_BASE_UNIT",
        "BASE_UNIT_ID"
    };
    
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_LONG
    };
    
    public PstUnit() {
    }
    
    public PstUnit(int i) throws DBException {
        super(new PstUnit());
    }
    
    public PstUnit(String sOid) throws DBException {
        super(new PstUnit(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstUnit(long lOid) throws DBException {
        super(new PstUnit(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }
    
    public String getTableName() {
        return TBL_P2_UNIT;
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String getPersistentName() {
        return new PstUnit().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception {
        Unit matUnit = fetchExc(ent.getOID());
        ent = (Entity) matUnit;
        return matUnit.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception {
        return insertExc((Unit) ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((Unit) ent);
    }
    
    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public static Unit fetchExc(long oid) throws DBException {
        try {
            Unit matUnit = new Unit();
            PstUnit pstUnit = new PstUnit(oid);
            matUnit.setOID(oid);
            
            matUnit.setCode(pstUnit.getString(FLD_CODE));
            matUnit.setName(pstUnit.getString(FLD_NAME));
            matUnit.setQtyPerBaseUnit(pstUnit.getdouble(FLD_QTY_PER_BASE_UNIT));
            matUnit.setBaseUnitId(pstUnit.getlong(FLD_BASE_UNIT_ID));
            
            return matUnit;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstUnit(0), DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(Unit matUnit) throws DBException {
        try {
            PstUnit pstUnit = new PstUnit(0);
            
            pstUnit.setString(FLD_CODE, matUnit.getCode());
            pstUnit.setString(FLD_NAME, matUnit.getName());
            pstUnit.setDouble(FLD_QTY_PER_BASE_UNIT, matUnit.getQtyPerBaseUnit());
            pstUnit.setLong(FLD_BASE_UNIT_ID, matUnit.getBaseUnitId());
            
            pstUnit.insert();
            long oidDataSync=PstDataSyncSql.insertExc(pstUnit.getInsertSQL());
            PstDataSyncStatus.insertExc(oidDataSync);

            matUnit.setOID(pstUnit.getlong(FLD_UNIT_ID));
			
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstUnit.getInsertSQL());
			
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstUnit(0), DBException.UNKNOWN);
        }
        return matUnit.getOID();
    }
    
    public static long updateExc(Unit matUnit) throws DBException {
        try {
            if (matUnit.getOID() != 0) {
                PstUnit pstUnit = new PstUnit(matUnit.getOID());
                
                pstUnit.setString(FLD_CODE, matUnit.getCode());
                pstUnit.setString(FLD_NAME, matUnit.getName());
                pstUnit.setDouble(FLD_QTY_PER_BASE_UNIT, matUnit.getQtyPerBaseUnit());
                pstUnit.setLong(FLD_BASE_UNIT_ID, matUnit.getBaseUnitId());
                
                pstUnit.update();

                long oidDataSync=PstDataSyncSql.insertExc(pstUnit.getUpdateSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstUnit.getUpdateSQL());
                return matUnit.getOID();
                
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstUnit(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long oid) throws DBException {
        try {
            PstUnit pstUnit = new PstUnit(oid);
            pstUnit.delete();

            long oidDataSync = PstDataSyncSql.insertExc(pstUnit.getDeleteSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstUnit.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstUnit(0), DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_P2_UNIT;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    
                    break;
                    
                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    
                    break;
                    
                case DBHandler.DBSVR_SYBASE:
                    break;
                    
                case DBHandler.DBSVR_ORACLE:
                    break;
                    
                case DBHandler.DBSVR_MSSQL:
                    break;
                    
                default:
                    ;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Unit matUnit = new Unit();
                resultToObject(rs, matUnit);
                lists.add(matUnit);
            }
            rs.close();
            return lists;
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    /**
     * adnyana
     * untuk mengambil semua unit
     * si simpan dalam hashtable
     * @return
     */
    public static Hashtable getListUnitHastable() {
        Hashtable lists = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_P2_UNIT;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Unit matUnit = new Unit();
                resultToObject(rs, matUnit);
                lists.put(matUnit.getCode().toUpperCase(),matUnit);
            }
            rs.close();
            return lists;
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Hashtable();
    }
    
    private static void resultToObject(ResultSet rs, Unit matUnit) {
        try {
            matUnit.setOID(rs.getLong(PstUnit.fieldNames[PstUnit.FLD_UNIT_ID]));
            matUnit.setCode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
            matUnit.setName(rs.getString(PstUnit.fieldNames[PstUnit.FLD_NAME]));
            matUnit.setQtyPerBaseUnit(rs.getDouble(PstUnit.fieldNames[PstUnit.FLD_QTY_PER_BASE_UNIT]));
            matUnit.setBaseUnitId(rs.getLong(PstUnit.fieldNames[PstUnit.FLD_BASE_UNIT_ID]));
        } catch (Exception e) {
        }
    }
    
    public static boolean checkOID(long unitId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_P2_UNIT + " WHERE " +
            PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] + " = " + unitId;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
    /**
     * untuk get oid unit
     * @return
     */
    public static long getOidUnit(){
        DBResultSet dbrs = null;
        Unit unit = new Unit();
        try{
            Vector list = list(0,0,"",fieldNames[FLD_QTY_PER_BASE_UNIT]);
            if(list!=null && list.size()>0){
                unit = (Unit)list.get(0);
            }
        }catch(Exception e){
            System.out.println("err getOidUnit : "+e.toString());
        }
        return unit.getOID();
    }
    
    
    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] + ") FROM " + TBL_P2_UNIT;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            
            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    
    
    /* This method used to find current data */
    public static int findLimitStart(long oid, int recordToGet, String whereClause) {
        String order = "";
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, order);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    Unit matUnit = (Unit) list.get(ls);
                    if (oid == matUnit.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;
        
        return start;
    }
    
    
    public static long processUnit(String prevSaleUnit, String currSaleUnit, String currBuyUnit, double qtyPerBase, long oidPrevSaleUnit) {
        long oidBaseUnit = 0;
        System.out.println(prevSaleUnit + " :: " + currSaleUnit + " :: " + currBuyUnit + " :: " + qtyPerBase + " :: " + oidPrevSaleUnit);
        if (!prevSaleUnit.equals(currSaleUnit)) {
            if (currSaleUnit.equals(currBuyUnit)) {
                try {
                    Unit unit = new Unit();
                    unit.setCode(currSaleUnit);
                    unit.setName(currSaleUnit);
                    unit.setBaseUnitId(0);
                    unit.setQtyPerBaseUnit(1);
                    
                    oidBaseUnit = PstUnit.insertExc(unit);
                } catch (Exception e) {
                    System.out.println("Exc : " + e.toString());
                }
            } else {
                try {
                    // unit base
                    Unit unit = new Unit();
                    unit.setCode(currSaleUnit);
                    unit.setName(currSaleUnit);
                    unit.setBaseUnitId(0);
                    unit.setQtyPerBaseUnit(1);
                    oidBaseUnit = PstUnit.insertExc(unit);
                    
                    // unit extended
                    unit = new Unit();
                    unit.setCode(currBuyUnit);
                    unit.setName(currBuyUnit);
                    unit.setBaseUnitId(oidBaseUnit);
                    unit.setQtyPerBaseUnit(qtyPerBase);
                    long oidExtendedUnit = PstUnit.insertExc(unit);
                } catch (Exception e) {
                    System.out.println("Exc : " + e.toString());
                }
            }
        } else {
            if (!currSaleUnit.equals(currBuyUnit)) {
                try {
                    // unit extended
                    Unit unit = new Unit();
                    unit.setCode(currBuyUnit);
                    unit.setName(currBuyUnit);
                    unit.setBaseUnitId(oidPrevSaleUnit);
                    unit.setQtyPerBaseUnit(qtyPerBase);
                    long oidExtendedUnit = PstUnit.insertExc(unit);
                    
                    oidBaseUnit = oidPrevSaleUnit;
                } catch (Exception e) {
                    System.out.println("Exc : " + e.toString());
                }
            }
        }
        
        return oidBaseUnit;
    }
    
    
    public static void transferUnit() {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM TEMP_TAIGA_UNIT ORDER BY UNIT_ID";
            System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            String currSaleUnit = "";
            String currBuyUnit = "";
            String prevSaleUnit = "";
            double qtyPerBase = 1;
            long oidPrevBaseUnit = 0;
            
            int i = 0;
            while (rs.next()) {
                prevSaleUnit = currSaleUnit;
                currSaleUnit = rs.getString(1);
                currBuyUnit = rs.getString(2);
                qtyPerBase = rs.getDouble(3);
                
                oidPrevBaseUnit = processUnit(prevSaleUnit, currSaleUnit, currBuyUnit, qtyPerBase, oidPrevBaseUnit);
                //System.out.println(currSaleUnit+" - "+currBuyUnit+" - "+qtyPerBase);
                //System.out.println("i="+(i++));
            }
            //System.out.println("selesai...");
            rs.close();
        } catch (Exception e) {
            System.out.println("Exc : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        //System.out.println("selesai...");
    }
    
    public static void transferMaterial() {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DISTINCT U.UNIT_ID, M.BUY_UNIT_CODE" +
            " FROM POS_TAIGA_UNIT AS U" +
            " INNER JOIN POS_TAIGA_MATERIAL_ORG M" +
            " ON U.CODE = M.BUY_UNIT_CODE" +
            " ORDER BY U.CODE";
            
            //System.out.println("sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                updateBuyUnit(rs.getLong(1), rs.getString(2));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Exc : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        //System.out.println("selesai...");
    }
    
    
    public static void updateBuyUnit(long oidBuyUnit, String buyUnitCode) {
        try {
            String sql = "UPDATE POS_TAIGA_MATERIAL_ORG" +
            " SET BUY_UNIT_ID = " + oidBuyUnit +
            " WHERE BUY_UNIT_CODE = '" + buyUnitCode + "'";
            
            //System.out.println("sql : "+sql);
            int status = DBHandler.execUpdate(sql);
            //System.out.println("status : " + status);
        } catch (Exception e) {
            System.out.println("Exc : " + e.toString());
        } finally {
            System.out.println("finally...");
        }
        //System.out.println("selesai...");
    }

    /**
     * get konversi unit
     * by Mirahu & Pak Tut
     * 20110811
     */

   public static double getUnitFactory (long unitFromId, long unitToId, int recurDeep, int MaxRecurDeep){

       if(unitFromId == unitToId){
           return 1.0;
       } else {
           Unit uFrom = new Unit();
           Unit uTo = new Unit();
           try {
               uFrom = PstUnit.fetchExc(unitFromId);
           }catch (Exception e){
               System.out.println(e);
           }
          try {
               uTo = PstUnit.fetchExc(unitToId);
           }catch (Exception e){
               System.out.println(e);
           }
           if (uFrom.getBaseUnitId()!= 0){
             if (uFrom.getBaseUnitId() == uTo.getOID()){
                 return uFrom.getQtyPerBaseUnit();
             } else {
                    return uFrom.getQtyPerBaseUnit() * getUnitFactory(uFrom.getBaseUnitId(), uTo.getOID(),recurDeep+1, MaxRecurDeep);
            }
         } else {
               if (uTo.getBaseUnitId() == uFrom.getOID()){
                 return 1 / uTo.getQtyPerBaseUnit();
             } else {
                 return 1 /uTo.getQtyPerBaseUnit() * getUnitFactory(uFrom.getBaseUnitId(), uTo.getOID(),recurDeep+1, MaxRecurDeep);
         }
       }
     }
   }
    
    /**
     * get qty per base unit
     * @param unitId
     * @param baseUnitId
     * @return
     */
    public static double getQtyPerBaseUnit(long unitId, long baseUnitId) {
        DBResultSet dbrs = null;
        double result = 1;
        try {
            String sql = "SELECT " + PstUnit.fieldNames[PstUnit.FLD_QTY_PER_BASE_UNIT] +
            " FROM " + PstUnit.TBL_P2_UNIT +
            " WHERE " + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " = " + unitId +
            " AND " + PstUnit.fieldNames[PstUnit.FLD_BASE_UNIT_ID] +
            " = " + baseUnitId;
            
            //System.out.println("getQtyPerBaseUnit : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getDouble(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Exc : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }
    
    //membuat hastable mencari code unit berdasarkan unitID
    public static String getKodeUnitByHash(long unitId){
        String kodeUnit="";
        try {
            if(unitCode==null || unitCode.size()==0){
                loadUnitCodeByHash();
            }
            kodeUnit = (String) unitCode.get(""+unitId);
            
        }catch (Exception e) {
            System.out.println("Exc getQtyPerBaseUnitByHash : " + e.toString());
        } finally {
            return kodeUnit;
        }
    }
    
    
    //membuat hastable
    public static double getQtyPerBaseUnitByHash(long unitId, long baseUnitId) {

        double result = 1;
        try {
           if(qtyPerBaseUnit==null){
            loadQtyPerBaseUnitByHash();
           }
           Double qty = (Double) qtyPerBaseUnit.get(""+unitId+"_"+baseUnitId);
           if(qty!=null){
           result = qty.doubleValue();
           }
        } catch (Exception e) {
            System.out.println("Exc getQtyPerBaseUnitByHash : " + e.toString());
        } finally {
            return result;
        }
    }

      public static void loadQtyPerBaseUnitByHash() {

        try {
           if(qtyPerBaseUnit==null){
               qtyPerBaseUnit = new Hashtable();
           }
           Vector listUnit = list(0, 0 , "", "");
           if(listUnit!=null){
             for(int i=0;i<listUnit.size();i++){
                Unit unit = (Unit)listUnit.get(i);
                qtyPerBaseUnit.put(""+unit.getOID()+"_"+unit.getBaseUnitId(), new Double(unit.getQtyPerBaseUnit()));
             }
           }
        } catch (Exception e) {
            System.out.println("Exc : " + e.toString());
        } finally {
            return ;
        }
    }
      
       /**
       *  membuat hastable untuk mencari code berdasarkan unit Code
       */
    public static void loadUnitCodeByHash() {

        try {
           if(unitCode==null){
               unitCode = new Hashtable();
           }
           Vector listUnit = list(0, 0 , "", "");
           if(listUnit!=null){
             for(int i=0;i<listUnit.size();i++){
                Unit unit = (Unit)listUnit.get(i);
                unitCode.put(""+unit.getOID(), unit.getCode());
             }
           }
        } catch (Exception e) {
            System.out.println("Exc : " + e.toString());
        } finally {
            return ;
        }
    }



    /*
     * List Get Unit based on unit base
     * By Mirahu 10 Juni 2011
     */

    public static Vector listUnitBaseUnitId(int limitStart, int recordToGet, String order, long unitBaseId ) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_P2_UNIT +
                    " WHERE " + fieldNames[FLD_UNIT_ID] +
                    " = '" + unitBaseId + "' or " + fieldNames[FLD_BASE_UNIT_ID]+
                    " = '" + unitBaseId + "'" ;

            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    break;
                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    break;
                case DBHandler.DBSVR_SYBASE:
                    break;
                case DBHandler.DBSVR_ORACLE:
                    break;
                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Unit matUnit = new Unit();
                resultToObject(rs, matUnit);
                lists.add(matUnit);

            }
            rs.close();
            return lists;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
     public static Vector<Unit> structureList(Vector<Unit> list){
        if(list == null || list.size()<1){
            return  new Vector();
        }
        Vector<Unit> resultTotal= new Vector();
        int idx=0;
        for(int i=0;i <=list.size() ;i++){
          Unit cat = (Unit) list.get(idx);
          if(0==cat.getBaseUnitId()){
            resultTotal.add(cat);
            list.remove(0);
            idx=0;
            i=0;
            //i=i>0?i-1:0;
          }else{
            boolean child=false;
            for(int k=0; k<resultTotal.size();k++){
                 Unit catParent = (Unit) resultTotal.get(k);
                    if(catParent.getOID()==cat.getBaseUnitId()){
                       resultTotal.insertElementAt(cat, k+1);
                       list.remove(idx);
                       child=true;
                    }
            }
            if(child){
               idx=0;
               i=0;
            }else{
               idx=idx+1;
               i=0;
            }
          }
        }
        return resultTotal;
      }
     
     public static Unit fetchByCode(String code) {
        com.dimata.posbo.db.DBResultSet dbrs = null;
        Unit unit = new Unit();
        try {
            String sql = "SELECT * FROM " + TBL_P2_UNIT +
                    " WHERE " + fieldNames[FLD_CODE] +
                    " = '" + code + "'";
            dbrs = com.dimata.posbo.db.DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                resultToObject(rs, unit);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            com.dimata.posbo.db.DBResultSet.close(dbrs);
        }
        return unit;
    }
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                Unit unit = PstUnit.fetchExc(oid);
                object.put(PstUnit.fieldNames[PstUnit.FLD_UNIT_ID], unit.getOID());
                object.put(PstUnit.fieldNames[PstUnit.FLD_BASE_UNIT_ID], unit.getBaseUnitId());
                object.put(PstUnit.fieldNames[PstUnit.FLD_CODE], unit.getCode());
                object.put(PstUnit.fieldNames[PstUnit.FLD_NAME], unit.getName());
                object.put(PstUnit.fieldNames[PstUnit.FLD_QTY_PER_BASE_UNIT], unit.getQtyPerBaseUnit());
            }catch(Exception exc){}

            return object;
        }    
}
