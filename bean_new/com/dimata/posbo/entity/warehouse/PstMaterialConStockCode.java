/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Dec 9, 2004
 * Time: 11:29:15 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.Entity;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;

import java.util.Vector;
import java.sql.ResultSet;
import org.json.JSONObject;

public class PstMaterialConStockCode extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    //public static final String TBL_POS_MATERIAL_STOCK_CODE = "POS_MATERIAL_STOCK_CODE";
    public static final String TBL_POS_MATERIAL_STOCK_CODE = "pos_material_con_stock_code";
    
    public static final int FLD_MATERIAL_STOCK_CODE_ID = 0;
    public static final int FLD_MATERIAL_ID = 1;
    public static final int FLD_LOCATION_ID = 2;
    public static final int FLD_STOCK_CODE = 3;
    public static final int FLD_STOCK_STATUS = 4;
    
    public static final String[] fieldNames = {
        "MATERIAL_STOCK_CODE_ID",
        "MATERIAL_ID",
        "LOCATION_ID",
        "STOCK_CODE",
        "STOCK_STATUS"
    };
    
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_INT
    };
    
    
    public static final int FLD_STOCK_STATUS_GOOD = 0;
    public static final int FLD_STOCK_STATUS_BAD = 1;
    public static final int FLD_STOCK_STATUS_LOSS = 2;
    public static final int FLD_STOCK_STATUS_SOLED = 3;
    public static final int FLD_STOCK_STATUS_PROCESS = 4;
    public static final int FLD_STOCK_STATUS_DELIVERED = 5;
    public static final int FLD_STOCK_STATUS_RETURN = 6;
    
    public static final String[] fieldStatus = {
        "Good Stock",
        "Bad Stock",
        "Loss Stock",
        "Soled",
        "Process",
        "Delivered",
        "Return"
    };
    
    
    public PstMaterialConStockCode() {
    }
    
    public PstMaterialConStockCode(int i) throws DBException {
        super(new PstMaterialConStockCode());
    }
    
    public PstMaterialConStockCode(String sOid) throws DBException {
        super(new PstMaterialConStockCode(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstMaterialConStockCode(long lOid) throws DBException {
        super(new PstMaterialConStockCode(0));
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
        return TBL_POS_MATERIAL_STOCK_CODE;
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String getPersistentName() {
        return new PstMaterialConStockCode().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception {
        MaterialConStockCode materialStockCode = fetchExc(ent.getOID());
        ent = (Entity) materialStockCode;
        return materialStockCode.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception {
        return insertExc((MaterialConStockCode) ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((MaterialConStockCode) ent);
    }
    
    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public static MaterialConStockCode fetchExc(long oid) throws DBException {
        try {
            MaterialConStockCode materialStockCode = new MaterialConStockCode();
            PstMaterialConStockCode pstMaterialConStockCode = new PstMaterialConStockCode(oid);
            materialStockCode.setOID(oid);
            
            materialStockCode.setMaterialId(pstMaterialConStockCode.getlong(FLD_MATERIAL_ID));
            materialStockCode.setLocationId(pstMaterialConStockCode.getlong(FLD_LOCATION_ID));
            materialStockCode.setStockCode(pstMaterialConStockCode.getString(FLD_STOCK_CODE));
            materialStockCode.setStockStatus(pstMaterialConStockCode.getInt(FLD_STOCK_STATUS));
            
            return materialStockCode;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialConStockCode(0), DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(MaterialConStockCode materialStockCode) throws DBException {
        try {
            PstMaterialConStockCode pstMaterialConStockCode = new PstMaterialConStockCode(0);
            
            pstMaterialConStockCode.setLong(FLD_MATERIAL_ID, materialStockCode.getMaterialId());
            pstMaterialConStockCode.setLong(FLD_LOCATION_ID, materialStockCode.getLocationId());
            pstMaterialConStockCode.setString(FLD_STOCK_CODE, materialStockCode.getStockCode());
            pstMaterialConStockCode.setInt(FLD_STOCK_STATUS, materialStockCode.getStockStatus());
            
            pstMaterialConStockCode.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMaterialConStockCode.getInsertSQL());
            materialStockCode.setOID(pstMaterialConStockCode.getlong(FLD_MATERIAL_STOCK_CODE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialConStockCode(0), DBException.UNKNOWN);
        }
        return materialStockCode.getOID();
    }
    
    public static long updateExc(MaterialConStockCode materialStockCode) throws DBException {
        try {
            if (materialStockCode.getOID() != 0) {
                PstMaterialConStockCode pstMaterialConStockCode = new PstMaterialConStockCode(materialStockCode.getOID());
                
                pstMaterialConStockCode.setLong(FLD_MATERIAL_ID, materialStockCode.getMaterialId());
                pstMaterialConStockCode.setLong(FLD_LOCATION_ID, materialStockCode.getLocationId());
                pstMaterialConStockCode.setString(FLD_STOCK_CODE, materialStockCode.getStockCode());
                pstMaterialConStockCode.setInt(FLD_STOCK_STATUS, materialStockCode.getStockStatus());
                
                pstMaterialConStockCode.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMaterialConStockCode.getUpdateSQL());
                return materialStockCode.getOID();
                
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialConStockCode(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long oid) throws DBException {
        try {
            PstMaterialConStockCode pstMaterialConStockCode = new PstMaterialConStockCode(oid);
            pstMaterialConStockCode.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMaterialConStockCode.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialConStockCode(0), DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_MATERIAL_STOCK_CODE;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            if (limitStart == 0 && recordToGet == 0)
                sql = sql + "";
            else
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MaterialConStockCode materialStockCode = new MaterialConStockCode();
                resultToObject(rs, materialStockCode);
                lists.add(materialStockCode);
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
    
    public static Vector list(String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_MATERIAL_STOCK_CODE;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MaterialConStockCode materialStockCode = new MaterialConStockCode();
                resultToObject(rs, materialStockCode);
                lists.add(materialStockCode);
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
     * bobo
     * @param whereClause
     * @param order
     * @return
     */
    public static int deleteStockCode(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "DELETE FROM " + TBL_POS_MATERIAL_STOCK_CODE;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            
            DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            return 1;
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }
    
    
    private static void resultToObject(ResultSet rs, MaterialConStockCode materialStockCode) {
        try {
            materialStockCode.setOID(rs.getLong(PstMaterialConStockCode.fieldNames[PstMaterialConStockCode.FLD_MATERIAL_STOCK_CODE_ID]));
            materialStockCode.setMaterialId(rs.getLong(PstMaterialConStockCode.fieldNames[PstMaterialConStockCode.FLD_MATERIAL_ID]));
            materialStockCode.setLocationId(rs.getLong(PstMaterialConStockCode.fieldNames[PstMaterialConStockCode.FLD_LOCATION_ID]));
            materialStockCode.setStockCode(rs.getString(PstMaterialConStockCode.fieldNames[PstMaterialConStockCode.FLD_STOCK_CODE]));
            materialStockCode.setStockStatus(rs.getInt(PstMaterialConStockCode.fieldNames[PstMaterialConStockCode.FLD_STOCK_STATUS]));
            
        } catch (Exception e) {
        }
    }
    
    public static boolean checkOID(long discountTypeId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_MATERIAL_STOCK_CODE + " WHERE " +
            PstMaterialConStockCode.fieldNames[PstMaterialConStockCode.FLD_MATERIAL_STOCK_CODE_ID] + " = " + discountTypeId;
            
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
            return result;
        }
    }
    
    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstMaterialConStockCode.fieldNames[PstMaterialConStockCode.FLD_MATERIAL_STOCK_CODE_ID] + ") FROM " + TBL_POS_MATERIAL_STOCK_CODE;
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
                    MaterialConStockCode materialStockCode = (MaterialConStockCode) list.get(ls);
                    if (oid == materialStockCode.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;
        
        return start;
    }
    
    
    /** adnyana
     * untuk pengecekan data code di stock barang
     * ini di gunakan pada saat stock corection
     * @param code
     * @param materialId
     * @return
     */
    public static MaterialConStockCode cekExistByCode(String code, long materialId) {
        MaterialConStockCode materialStockCode = new MaterialConStockCode();
        try {
            String where = fieldNames[FLD_STOCK_CODE] + "='" + code + "' AND " + fieldNames[FLD_MATERIAL_ID] + "=" + materialId;
            // " AND " + fieldNames[FLD_STOCK_STATUS] + "=" + FLD_STOCK_STATUS_GOOD;
            Vector list = list(0, 0, where, "");
            if (list != null && list.size() > 0) {
                materialStockCode = (MaterialConStockCode) list.get(0);
            }
        } catch (Exception e) {
        }
        return materialStockCode;
    }
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MaterialConStockCode materialConStockCode = PstMaterialConStockCode.fetchExc(oid);
         object.put(PstMaterialConStockCode.fieldNames[PstMaterialConStockCode.FLD_MATERIAL_STOCK_CODE_ID], materialConStockCode.getOID());
         object.put(PstMaterialConStockCode.fieldNames[PstMaterialConStockCode.FLD_MATERIAL_ID], materialConStockCode.getMaterialId());
         object.put(PstMaterialConStockCode.fieldNames[PstMaterialConStockCode.FLD_LOCATION_ID], materialConStockCode.getLocationId());
         object.put(PstMaterialConStockCode.fieldNames[PstMaterialConStockCode.FLD_STOCK_CODE], materialConStockCode.getStockStatus());
         object.put(PstMaterialConStockCode.fieldNames[PstMaterialConStockCode.FLD_STOCK_STATUS], materialConStockCode.getStockCode());
      }catch(Exception exc){}
      return object;
   }  
}
