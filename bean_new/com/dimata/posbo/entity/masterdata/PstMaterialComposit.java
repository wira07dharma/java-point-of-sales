package com.dimata.posbo.entity.masterdata;

/* package java */
import com.dimata.common.entity.custom.PstDataCustom;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import org.json.JSONObject;

public class PstMaterialComposit extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    //public static final  String TBL_MATERIAL_COMPOSIT = "POS_MATERIAL_COMPOSIT";
    public static final  String TBL_MATERIAL_COMPOSIT = "pos_material_composit";
    
    public static final  int FLD_MATERIAL_COMPOSIT_ID   = 0;
    public static final  int FLD_MATERIAL_ID            = 1;
    public static final  int FLD_MATERIAL_COMPOSER_ID   = 2;
    public static final  int FLD_UNIT_ID                = 3;
    public static final  int FLD_QTY                    = 4;
	public static final  int FLD_SEQUENCE_IDX			= 5;
	public static final  int FLD_AUTO_FILL				= 6;
    
    public static final  String[] fieldNames = {
        "MATERIAL_COMPOSIT_ID",
        "MATERIAL_ID",
        "MATERIAL_COMPOSER_ID",
        "UNIT_ID",
        "QTY",
		"SEQUENCE_IDX",
		"AUTO_FILL"
    };
    
    public static final  int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
		TYPE_INT,
		TYPE_INT
    };
    
    
    public PstMaterialComposit(){
    }
    
    public PstMaterialComposit(int i) throws DBException {
        super(new PstMaterialComposit());
    }
    
    public PstMaterialComposit(String sOid) throws DBException {
        super(new PstMaterialComposit(0));
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstMaterialComposit(long lOid) throws DBException {
        super(new PstMaterialComposit(0));
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
        return TBL_MATERIAL_COMPOSIT;
    }
    
    public String[] getFieldNames(){
        return fieldNames;
    }
    
    public int[] getFieldTypes(){
        return fieldTypes;
    }
    
    public String getPersistentName(){
        return new PstMaterialComposit().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception{
        MaterialComposit materialComposit = fetchExc(ent.getOID());
        ent = (Entity)materialComposit;
        return materialComposit.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception{
        return insertExc((MaterialComposit) ent);
    }
    
    public long updateExc(Entity ent) throws Exception{
        return updateExc((MaterialComposit) ent);
    }
    
    public long deleteExc(Entity ent) throws Exception{
        if(ent==null){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public static MaterialComposit fetchExc(long oid) throws DBException {
        try {
            MaterialComposit materialComposit = new MaterialComposit();
            PstMaterialComposit pstMaterialComposit = new PstMaterialComposit(oid);
            materialComposit.setOID(oid);
            
            materialComposit.setMaterialId(pstMaterialComposit.getlong(FLD_MATERIAL_ID));
            materialComposit.setMaterialComposerId(pstMaterialComposit.getlong(FLD_MATERIAL_COMPOSER_ID));
            materialComposit.setUnitId(pstMaterialComposit.getlong(FLD_UNIT_ID));
            materialComposit.setQty(pstMaterialComposit.getdouble(FLD_QTY));
			materialComposit.setSequenceIdx(pstMaterialComposit.getInt(FLD_SEQUENCE_IDX));
			materialComposit.setAutoFill(pstMaterialComposit.getInt(FLD_AUTO_FILL));
            
            return materialComposit;
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstMaterialComposit(0),DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(MaterialComposit materialComposit) throws DBException {
        try {
            PstMaterialComposit pstMaterialComposit = new PstMaterialComposit(0);
            
            pstMaterialComposit.setLong(FLD_MATERIAL_ID, materialComposit.getMaterialId());
            pstMaterialComposit.setLong(FLD_MATERIAL_COMPOSER_ID, materialComposit.getMaterialComposerId());
            pstMaterialComposit.setLong(FLD_UNIT_ID, materialComposit.getUnitId());
            pstMaterialComposit.setDouble(FLD_QTY, materialComposit.getQty());
			pstMaterialComposit.setInt(FLD_SEQUENCE_IDX, materialComposit.getSequenceIdx());
			pstMaterialComposit.setInt(FLD_AUTO_FILL, materialComposit.getAutoFill());
            
            pstMaterialComposit.insert();

            long oidDataSync=PstDataSyncSql.insertExc(pstMaterialComposit.getInsertSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
            materialComposit.setOID(pstMaterialComposit.getlong(FLD_MATERIAL_COMPOSIT_ID));
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMaterialComposit.getInsertSQL());
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstMaterialComposit(0),DBException.UNKNOWN);
        }
        return materialComposit.getOID();
    }
    
    public static long updateExc(MaterialComposit materialComposit) throws DBException{
        try{
            if(materialComposit.getOID() != 0){
                PstMaterialComposit pstMaterialComposit = new PstMaterialComposit(materialComposit.getOID());
                
                pstMaterialComposit.setLong(FLD_MATERIAL_ID, materialComposit.getMaterialId());
                pstMaterialComposit.setLong(FLD_MATERIAL_COMPOSER_ID, materialComposit.getMaterialComposerId());
                pstMaterialComposit.setLong(FLD_UNIT_ID, materialComposit.getUnitId());
                pstMaterialComposit.setDouble(FLD_QTY, materialComposit.getQty());
				pstMaterialComposit.setInt(FLD_SEQUENCE_IDX, materialComposit.getSequenceIdx());
				pstMaterialComposit.setInt(FLD_AUTO_FILL, materialComposit.getAutoFill());
                
                pstMaterialComposit.update();

                long oidDataSync=PstDataSyncSql.insertExc(pstMaterialComposit.getUpdateSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMaterialComposit.getUpdateSQL());
                return materialComposit.getOID();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstMaterialComposit(0),DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long oid) throws DBException {
        try {
            PstMaterialComposit pstMaterialComposit = new PstMaterialComposit(oid);
            pstMaterialComposit.delete();

            long oidDataSync = PstDataSyncSql.insertExc(pstMaterialComposit.getDeleteSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMaterialComposit.getDeleteSQL());
        }
        catch(DBException dbe) {
            System.out.println("DBE Exception : " + dbe);
            throw dbe;
        }
        catch(Exception e) {
            System.out.println("Exception : " + e);
            throw new DBException(new PstMaterialComposit(0),DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static void deleteByMaterial(long oidMaterial) {
        DBResultSet dbrs = null;
        try {
            String sql = "DELETE FROM " + PstMaterialComposit.TBL_MATERIAL_COMPOSIT +
            " WHERE " + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID] +
            " = " + oidMaterial;
            DBHandler.execUpdate(sql);

            long oidDataSync = PstDataSyncSql.insertExc(sql);
            PstDataSyncStatus.insertExc(oidDataSync);
        }
        catch(Exception e) {
            System.out.println("PstMaterialComposit.deleteByMaterial() err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
    }
    
    public static Vector listAll() {
        return list(0, 500, "","");
    }
    
   /* public static Vector list(int limitStart,int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT MC." + fieldNames[FLD_MATERIAL_COMPOSIT_ID] +
            ", MC." + fieldNames[FLD_MATERIAL_ID] +
            ", MC." + fieldNames[FLD_MATERIAL_COMPOSER_ID] +
            ", MC." + fieldNames[FLD_UNIT_ID] +
            ", MC." + fieldNames[FLD_QTY] +
            ", UN." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            " FROM (" + TBL_MATERIAL_COMPOSIT +
            " MC INNER JOIN " + PstUnit.TBL_P2_UNIT +
            " UN ON MC." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_UNIT_ID] +
            " = UN." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL +
            " MAT ON MC." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
            
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
                Vector temp = new Vector();
                MaterialComposit materialComposit = new MaterialComposit();
                Unit unit = new Unit();
                
                materialComposit.setOID(rs.getLong(1));
                materialComposit.setMaterialId(rs.getLong(2));
                materialComposit.setMaterialComposerId(rs.getLong(3));
                materialComposit.setUnitId(rs.getLong(4));
                materialComposit.setQty(rs.getDouble(5));
                temp.add(materialComposit);
                
                unit.setCode(rs.getString(6));
                temp.add(unit);
                
                lists.add(temp);
            }
            rs.close();
            return lists;
            
        }
        catch(Exception e) {
            System.out.println(e);
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }*/


    public static Vector list(int limitStart,int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT MC." + fieldNames[FLD_MATERIAL_COMPOSIT_ID] +
            ", MC." + fieldNames[FLD_MATERIAL_ID] +
            ", MC." + fieldNames[FLD_MATERIAL_COMPOSER_ID] +
            ", MC." + fieldNames[FLD_UNIT_ID] +
            ", MC." + fieldNames[FLD_QTY] +
			", UN." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE] +
			", MC." + fieldNames[FLD_SEQUENCE_IDX] +
			", MC." + fieldNames[FLD_AUTO_FILL] +
            " FROM (" + TBL_MATERIAL_COMPOSIT +
            " MC INNER JOIN " + PstUnit.TBL_P2_UNIT +
            " UN ON MC." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_UNIT_ID] +
            " = UN." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL +
            " MAT ON MC." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];

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
                Vector temp = new Vector();
                MaterialComposit materialComposit = new MaterialComposit();
                Unit unit = new Unit();
                Material material = new Material();

                materialComposit.setOID(rs.getLong(1));
                materialComposit.setMaterialId(rs.getLong(2));
                materialComposit.setMaterialComposerId(rs.getLong(3));
                materialComposit.setUnitId(rs.getLong(4));
                materialComposit.setQty(rs.getDouble(5));
				materialComposit.setSequenceIdx(rs.getInt(13));
				materialComposit.setAutoFill(rs.getInt(14));
                temp.add(materialComposit);

                unit.setCode(rs.getString(6));
                temp.add(unit);

                material.setSku(rs.getString(7));
                material.setBarCode(rs.getString(8));
                material.setName(rs.getString(9));
                material.setAveragePrice(rs.getDouble(10));
                material.setDefaultCost(rs.getDouble(11));
                material.setCurrBuyPrice(rs.getDouble(12));
                temp.add(material);

                lists.add(temp);
            }
            rs.close();
            return lists;

        }
        catch(Exception e) {
            System.out.println(e);
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    public static Vector listProduksi(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MATERIAL_COMPOSIT;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }
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
                MaterialComposit materialComposit = new MaterialComposit();
                resultToObject(rs, materialComposit);
                lists.add(materialComposit);
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
    
    public static void resultToObject(ResultSet rs, MaterialComposit materialComposit) {
        try {
            materialComposit.setOID(rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSIT_ID]));
            materialComposit.setMaterialId(rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID]));
            materialComposit.setMaterialComposerId(rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID]));
            materialComposit.setUnitId(rs.getLong(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_UNIT_ID]));
            materialComposit.setQty(rs.getDouble(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_QTY]));
			materialComposit.setSequenceIdx(rs.getInt(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_SEQUENCE_IDX]));
			materialComposit.setAutoFill(rs.getInt(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_AUTO_FILL]));
            
        }catch(Exception e){ }
    }
    
    public static boolean checkOID(long materialCompositId){
        DBResultSet dbrs = null;
        boolean result = false;
        try{
            String sql = "SELECT * FROM " + TBL_MATERIAL_COMPOSIT + " WHERE " +
            fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSIT_ID] +
            " = " + materialCompositId;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                result = true;
            }
            rs.close();
        }catch(Exception e){
            System.out.println("err : "+e.toString());
        }finally{
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    public static int getCount(String whereClause){
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(MC."+ fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSIT_ID] + ") FROM " +
            TBL_MATERIAL_COMPOSIT + " MC";
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            int count = 0;
            while(rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        }catch(Exception e) {
            return 0;
        }finally {
            DBResultSet.close(dbrs);
        }
    }

    //Total Nilai stock dan Harga beli terakhir
     //by Mirahu
    //13 Juni 2011
    public static Vector grandTotalComposit(long materialId) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT SUM(MC." + fieldNames[FLD_QTY] +
            " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + ") AS SUM_STOCK_VALUE " +
            //", SUM(MC." + fieldNames[FLD_QTY] +
            //" * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + ") AS SUM_COST_VALUE " +
            ", SUM(MC." + fieldNames[FLD_QTY] +
            " * MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE] + ") AS SUM_COST_VALUE " +
            " FROM (" + TBL_MATERIAL_COMPOSIT +
            " MC INNER JOIN " + PstUnit.TBL_P2_UNIT +
            " UN ON MC." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_UNIT_ID] +
            " = UN." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstMaterial.TBL_MATERIAL +
            " MAT ON MC." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " WHERE MC." + fieldNames[FLD_MATERIAL_ID] +
            " = " + materialId;

            System.out.println("sql list > " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                 Vector temp = new Vector();

                temp.add(rs.getDouble("SUM_STOCK_VALUE"));
                temp.add(rs.getDouble("SUM_COST_VALUE"));
                
                result.add(temp);
            }

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    // end of grand total

    /*
     * Update Stock Value & Cost Master
     * By Mirahu
     * 13 Juni 2011
     */
    public static boolean updateCostAndStockValueMaster(long oidMaterial, double stockValue, double newCost, double newCostNoPpn, double lastVat) {
        boolean hasil = false;
        try {
            // Select all receive today with detail
            String sql = "UPDATE " + PstMaterial.TBL_MATERIAL +
                    " SET " + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                    " = " + stockValue +
                    "," + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                    " = " + newCostNoPpn +
                    "," + PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE] +
                    " = " + newCost +
                    "," + PstMaterial.fieldNames[PstMaterial.FLD_LAST_VAT] +
                    " = " +lastVat;
                    sql = sql + " WHERE " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    " = " + oidMaterial;
            int a = DBHandler.execUpdate(sql);
            hasil = true;
        } catch (Exception exc) {
            System.out.println("eRR >>= UpdCost : " + exc);
        }
        return hasil;
    }

    public static Vector ListComponentComposit(long oidMaterial) {
        Vector hasil = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSIT_ID] +
                         ", COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID] +
                         ", COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID] +
                         ", COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_QTY]+
                         ", COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_UNIT_ID] +
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]+
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                         ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                         " FROM " + PstMaterialComposit.TBL_MATERIAL_COMPOSIT + " COMP " +
                         " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT " +
                         " ON COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID]+
                         " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                         " INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNIT " +
                         " ON COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_UNIT_ID] +
                         " = UNIT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                         " WHERE COMP." + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID] +
                         " = " + oidMaterial;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                //MaterialComposit objMaterialComposit = new MaterialComposit();

                Vector temp = new Vector();
                MaterialComposit materialComposit = new MaterialComposit();
                Material material = new Material();

                materialComposit.setOID(rs.getLong(1));
                materialComposit.setMaterialComposerId(rs.getLong(2));
                materialComposit.setMaterialId(rs.getLong(3));
                materialComposit.setQty(rs.getDouble(4));
                materialComposit.setUnitId(rs.getLong(5));
                temp.add(materialComposit);

                material.setAveragePrice(rs.getDouble(6));
                material.setDefaultCost(rs.getDouble(7));
                material.setMaterialType(rs.getInt(8));
                temp.add(material);

                hasil.add(temp);

            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("GetMaterialComposer : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }


   /**
     * this method used to check if specify material already exist in current compositItem
     * return "true" ---> if material already exist in orderItem
     * return "false" ---> if material not available in orderItem
     * by Mirahu 20110803
     */
    public static boolean materialExist(long oidComposerMaterial, long oidMaterial) {
        DBResultSet dbrs = null;
        boolean bool = false;
        try {
            String sql = "SELECT COMP." + fieldNames[FLD_MATERIAL_COMPOSER_ID] +
            " FROM " + TBL_MATERIAL_COMPOSIT + " AS COMP " +
            " WHERE COMP." + fieldNames[FLD_MATERIAL_ID] + "=" + oidMaterial  +
            " AND COMP." +  fieldNames[FLD_MATERIAL_COMPOSER_ID] + "=" + oidComposerMaterial;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                bool = true;
                break;
            }
        } catch (Exception e) {
            System.out.println("PstMaterialComposit.materialExist.err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return bool;
    }
     public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                MaterialComposit materialComposit = PstMaterialComposit.fetchExc(oid);
                object.put(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSIT_ID], materialComposit.getOID());
                object.put(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_AUTO_FILL], materialComposit.getAutoFill());
                object.put(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID], materialComposit.getMaterialComposerId());
                object.put(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_QTY], materialComposit.getQty());
                object.put(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID], materialComposit.getMaterialId());
                object.put(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_SEQUENCE_IDX], materialComposit.getSequenceIdx());
                object.put(PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_UNIT_ID], materialComposit.getUnitId());
            }catch(Exception exc){}

            return object;
        }
}
