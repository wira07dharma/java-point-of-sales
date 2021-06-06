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

public class PstCostingStockCode extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_POS_COSTING_MATERIAL_CODE = "pos_costing_material_item_code";

    public static final int FLD_COSTING_MATERIAL_CODE_ID = 0;
    public static final int FLD_COSTING_MATERIAL_ITEM_ID = 1;
    public static final int FLD_COSTING_MATERIAL_ID = 2;
    public static final int FLD_STOCK_CODE = 3;
    public static final int FLD_STOCK_VALUE = 4;

    public static final String[] fieldNames = {
        "COSTING_MATERIAL_ITEM_CODE_ID",
        "COSTING_MATERIAL_ITEM_ID",
        "COSTING_MATERIAL_ID",
        "STOCK_CODE",
        "VALUE"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_FLOAT
    };

    public PstCostingStockCode() {
    }

    public PstCostingStockCode(int i) throws DBException {
        super(new PstCostingStockCode());
    }

    public PstCostingStockCode(String sOid) throws DBException {
        super(new PstCostingStockCode(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstCostingStockCode(long lOid) throws DBException {
        super(new PstCostingStockCode(0));
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
        return TBL_POS_COSTING_MATERIAL_CODE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstCostingStockCode().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        CostingStockCode stockCode = fetchExc(ent.getOID());
        ent = (Entity) stockCode;
        return stockCode.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((CostingStockCode) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((CostingStockCode) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static CostingStockCode fetchExc(long oid) throws DBException {
        try {
            CostingStockCode dispatchStockCode = new CostingStockCode();
            PstCostingStockCode pstCostingStockCode = new PstCostingStockCode(oid);
            dispatchStockCode.setOID(oid);

            dispatchStockCode.setCostingMaterialItemId(pstCostingStockCode.getlong(FLD_COSTING_MATERIAL_ITEM_ID));
            dispatchStockCode.setCostingMaterialId(pstCostingStockCode.getlong(FLD_COSTING_MATERIAL_ID));
            dispatchStockCode.setStockCode(pstCostingStockCode.getString(FLD_STOCK_CODE));
            dispatchStockCode.setStockValue(pstCostingStockCode.getdouble(FLD_STOCK_VALUE));
            
            return dispatchStockCode;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCostingStockCode(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(CostingStockCode materialStockCode) throws DBException {
        try {
            PstCostingStockCode pstCostingStockCode = new PstCostingStockCode(0);

            pstCostingStockCode.setLong(FLD_COSTING_MATERIAL_ITEM_ID, materialStockCode.getCostingMaterialItemId());
            pstCostingStockCode.setLong(FLD_COSTING_MATERIAL_ID, materialStockCode.getCostingMaterialId());
            pstCostingStockCode.setString(FLD_STOCK_CODE, materialStockCode.getStockCode());
            pstCostingStockCode.setDouble(FLD_STOCK_VALUE, materialStockCode.getStockValue());
            
            pstCostingStockCode.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstCostingStockCode.getInsertSQL());
            materialStockCode.setOID(pstCostingStockCode.getlong(FLD_COSTING_MATERIAL_CODE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCostingStockCode(0), DBException.UNKNOWN);
        }
        return materialStockCode.getOID();
    }

    public static long updateExc(CostingStockCode materialStockCode) throws DBException {
        try {
            if (materialStockCode.getOID() != 0) {
                PstCostingStockCode pstCostingStockCode = new PstCostingStockCode(materialStockCode.getOID());

                pstCostingStockCode.setLong(FLD_COSTING_MATERIAL_ITEM_ID, materialStockCode.getCostingMaterialItemId());
                pstCostingStockCode.setLong(FLD_COSTING_MATERIAL_ID, materialStockCode.getCostingMaterialId());
                pstCostingStockCode.setString(FLD_STOCK_CODE, materialStockCode.getStockCode());
                pstCostingStockCode.setDouble(FLD_STOCK_VALUE, materialStockCode.getStockValue());
                
                pstCostingStockCode.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstCostingStockCode.getUpdateSQL());
                return materialStockCode.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCostingStockCode(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstCostingStockCode pstCostingStockCode = new PstCostingStockCode(oid);
            pstCostingStockCode.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstCostingStockCode.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCostingStockCode(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_COSTING_MATERIAL_CODE;
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
                CostingStockCode materialStockCode = new CostingStockCode();
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

    private static void resultToObject(ResultSet rs, CostingStockCode materialStockCode) {
        try {
            materialStockCode.setOID(rs.getLong(PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_CODE_ID]));
            materialStockCode.setCostingMaterialItemId(rs.getLong(PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_ITEM_ID]));
            materialStockCode.setCostingMaterialId(rs.getLong(PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_ID]));
            materialStockCode.setStockCode(rs.getString(PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_STOCK_CODE]));
            materialStockCode.setStockValue(rs.getDouble(PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_STOCK_VALUE]));
            
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long oid) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_COSTING_MATERIAL_CODE + " WHERE " +
                    PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_CODE_ID] + " = " + oid;

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
            String sql = "SELECT COUNT(" + PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_CODE_ID] + ") FROM " + TBL_POS_COSTING_MATERIAL_CODE;
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
                    CostingStockCode materialStockCode = (CostingStockCode) list.get(ls);
                    if (oid == materialStockCode.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }

    public static Vector getSerialCodeReturnBy(long oidMat, long oidRec) {
        Vector list = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DCODE .* FROM " + TBL_POS_COSTING_MATERIAL_CODE + " AS DCODE " +
                    " INNER JOIN " + PstMatCostingItem.TBL_MAT_COSTING_ITEM + " AS IT " +
                    " ON IT."+PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ITEM_ID]+" = DCODE."+fieldNames[FLD_COSTING_MATERIAL_ITEM_ID]+
                    " WHERE IT."+PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ITEM_ID]+"='" + oidRec+"'"+
                    " AND IT."+PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID]+" ='"+oidMat+"'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                CostingStockCode  costingStockCode = new CostingStockCode();
                resultToObject(rs, costingStockCode);
                list.add(costingStockCode);
            }
        } catch (Exception e) {
            System.out.println("");
        }
        return list;
    }


    public static Vector listPostingCostingSerialCode(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT PRMIC.*,PRMI.MATERIAL_ID FROM " + TBL_POS_COSTING_MATERIAL_CODE+" AS PRMIC "+
                         " INNER JOIN pos_costing_material_item AS PRMI ON PRMI.COSTING_MATERIAL_ITEM_ID=PRMIC.COSTING_MATERIAL_ITEM_ID "+
                         " INNER JOIN pos_costing_material AS PRM ON PRM.COSTING_MATERIAL_ID=PRMI.COSTING_MATERIAL_ID ";
            
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
                SourceStockCode sourceStockCode = new SourceStockCode();//(SourceStockCode)
                resultToObjectToPosting(rs, sourceStockCode);
                lists.add(sourceStockCode); 
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

    private static void resultToObjectToPosting(ResultSet rs, SourceStockCode materialStockCode) {
        try {
            materialStockCode.setOID(rs.getLong(PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_CODE_ID]));
            materialStockCode.setSourceId(rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID]));
            materialStockCode.setStockCode(rs.getString(PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_STOCK_CODE]));
            materialStockCode.setStockValue(rs.getDouble(PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_STOCK_VALUE]));
        } catch (Exception e) {
        }
    }
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         CostingStockCode costingStockCode = PstCostingStockCode.fetchExc(oid);
         object.put(PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_CODE_ID], costingStockCode.getOID());
         object.put(PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_ITEM_ID], costingStockCode.getCostingMaterialItemId());
         object.put(PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_COSTING_MATERIAL_ID], costingStockCode.getCostingMaterialId());
         object.put(PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_STOCK_CODE], costingStockCode.getStockCode());
         object.put(PstCostingStockCode.fieldNames[PstCostingStockCode.FLD_STOCK_VALUE], costingStockCode.getStockValue());
      }catch(Exception exc){}
      return object;
   }
}
