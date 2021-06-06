/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Vector;
import org.json.JSONObject;
/**
 *
 * @author dimata005
 */

public class PstPriceProtectionItem extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    public static final String TBL_POS_PRICE_PROTECTION_ITEM = "pos_price_protection_item";

    public static final int FLD_POS_PRICE_PROTECTION_ITEM_ID = 0;
    public static final int FLD_POS_PRICE_PROTECTION_ID = 1;
    public static final int FLD_MATERIAL_ID = 2;
    public static final int FLD_AMOUNT = 3;
    public static final int FLD_STOCK_ON_HAND = 4;
    public static final int FLD_TOTAL_AMOUNT = 5;

    public static final String[] fieldNames = {
        "POS_PRICE_PROTECTION_ITEM_ID",
        "POS_PRICE_PROTECTION_ID",
        "MATERIAL_ID",
        "AMOUNT",
        "STOCK_ON_HAND",
        "TOTAL_AMOUNT"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT
    };


    public PstPriceProtectionItem() {
    }

    public PstPriceProtectionItem(int i) throws DBException {
        super(new PstPriceProtectionItem());
    }

    public PstPriceProtectionItem(String sOid) throws DBException {
        super(new PstPriceProtectionItem(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstPriceProtectionItem(long lOid) throws DBException {
        super(new PstPriceProtectionItem(0));
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
        return TBL_POS_PRICE_PROTECTION_ITEM;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPriceProtectionItem().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        PriceProtectionItem priceProtectionItem = fetchExc(ent.getOID());
        ent = (Entity) priceProtectionItem;
        return priceProtectionItem.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((PriceProtectionItem) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((PriceProtectionItem) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static PriceProtectionItem fetchExc(long oid) throws DBException {
        try {
            PriceProtectionItem priceProtectionItem = new PriceProtectionItem();
            PstPriceProtectionItem pstPriceProtectionItem = new PstPriceProtectionItem(oid);
            priceProtectionItem.setOID(oid);

            priceProtectionItem.setPriceProtectionId(pstPriceProtectionItem.getlong(FLD_POS_PRICE_PROTECTION_ID));
            priceProtectionItem.setAmount(pstPriceProtectionItem.getdouble(FLD_STOCK_ON_HAND));
            priceProtectionItem.setStockOnHand(pstPriceProtectionItem.getdouble(FLD_AMOUNT));
            priceProtectionItem.setTotalAmount(pstPriceProtectionItem.getdouble(FLD_TOTAL_AMOUNT));
            priceProtectionItem.setMaterialId(pstPriceProtectionItem.getlong(FLD_MATERIAL_ID));
            return priceProtectionItem;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceProtectionItem(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(PriceProtectionItem priceProtectionItem) throws DBException {
        try {
            PstPriceProtectionItem pstPriceProtectionItem = new PstPriceProtectionItem(0);

            pstPriceProtectionItem.setLong(FLD_POS_PRICE_PROTECTION_ID, priceProtectionItem.getPriceProtectionId());
            pstPriceProtectionItem.setLong(FLD_MATERIAL_ID, priceProtectionItem.getMaterialId());
            pstPriceProtectionItem.setDouble(FLD_AMOUNT, priceProtectionItem.getAmount());
            pstPriceProtectionItem.setDouble(FLD_STOCK_ON_HAND, priceProtectionItem.getStockOnHand());
            pstPriceProtectionItem.setDouble(FLD_TOTAL_AMOUNT, priceProtectionItem.getTotalAmount());
            
            pstPriceProtectionItem.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstPriceProtectionItem.getInsertSQL());
            priceProtectionItem.setOID(pstPriceProtectionItem.getlong(FLD_POS_PRICE_PROTECTION_ID));
            
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceProtectionItem(0), DBException.UNKNOWN);
        }
        return priceProtectionItem.getOID();
    }

    public static long updateExc(PriceProtectionItem priceProtectionItem) throws DBException {
        try {
            if (priceProtectionItem.getOID() != 0) {
                PstPriceProtectionItem pstPriceProtectionItem = new PstPriceProtectionItem(priceProtectionItem.getOID());

                pstPriceProtectionItem.setLong(FLD_POS_PRICE_PROTECTION_ID, priceProtectionItem.getPriceProtectionId());
                pstPriceProtectionItem.setLong(FLD_MATERIAL_ID, priceProtectionItem.getMaterialId());
                pstPriceProtectionItem.setDouble(FLD_AMOUNT, priceProtectionItem.getAmount());
                pstPriceProtectionItem.setDouble(FLD_STOCK_ON_HAND, priceProtectionItem.getStockOnHand());
                pstPriceProtectionItem.setDouble(FLD_TOTAL_AMOUNT, priceProtectionItem.getTotalAmount());
                
                pstPriceProtectionItem.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstPriceProtectionItem.getUpdateSQL());
                return priceProtectionItem.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceProtectionItem(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstPriceProtectionItem pstPriceProtectionItem = new PstPriceProtectionItem(oid);
            pstPriceProtectionItem.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstPriceProtectionItem.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceProtectionItem(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_PRICE_PROTECTION_ITEM;
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
                PriceProtectionItem priceProtectionItem = new PriceProtectionItem();
                resultToObject(rs, priceProtectionItem);
                lists.add(priceProtectionItem);
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
    
    
    
    public static Vector listInnerJoinMaterial(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
           // String sql = "SELECT * FROM " + TBL_POS_PRICE_PROTECTION_ITEM + " AS PP";
            
           String sql = " SELECT PPPI.*,PM.SKU,PM.NAME FROM pos_price_protection_item PPPI"+
                        " INNER JOIN pos_material AS PM"+
                        " ON PM.MATERIAL_ID=PPPI.MATERIAL_ID";

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
                Vector vct = new Vector();
                PriceProtectionItem priceProtectionItem = new PriceProtectionItem();
                Material material = new Material();
                //resultToObject(rs, priceProtectionItem);
                priceProtectionItem.setOID(rs.getLong(PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_POS_PRICE_PROTECTION_ITEM_ID]));
                priceProtectionItem.setPriceProtectionId(rs.getLong(PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_POS_PRICE_PROTECTION_ID]));
                priceProtectionItem.setPriceProtectionId(rs.getLong(PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_POS_PRICE_PROTECTION_ID]));
                priceProtectionItem.setStockOnHand(rs.getDouble(PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_STOCK_ON_HAND])); 
                priceProtectionItem.setAmount(rs.getDouble(PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_AMOUNT]));  
                priceProtectionItem.setTotalAmount(rs.getDouble(PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_TOTAL_AMOUNT]));  
                
                material.setOID(rs.getLong(PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_MATERIAL_ID]));
                material.setSku(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_SKU]));
                material.setName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                
                vct.add(priceProtectionItem);
                vct.add(material);
                lists.add(vct);
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
            String sql = "SELECT * FROM " + TBL_POS_PRICE_PROTECTION_ITEM;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                PriceProtectionItem priceProtectionItem = new PriceProtectionItem();
                resultToObject(rs, priceProtectionItem);
                lists.add(priceProtectionItem);
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
            String sql = "DELETE FROM " + TBL_POS_PRICE_PROTECTION_ITEM;
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


    private static void resultToObject(ResultSet rs, PriceProtectionItem priceProtectionItem) {
        try {
            
            priceProtectionItem.setOID(rs.getLong(PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_POS_PRICE_PROTECTION_ITEM_ID]));
            priceProtectionItem.setPriceProtectionId(rs.getLong(PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_POS_PRICE_PROTECTION_ID]));
            priceProtectionItem.setMaterialId(rs.getLong(PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_MATERIAL_ID]));
            priceProtectionItem.setAmount(rs.getDouble(PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_AMOUNT]));
            priceProtectionItem.setStockOnHand(rs.getDouble(PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_STOCK_ON_HAND]));
            priceProtectionItem.setTotalAmount(rs.getDouble(PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_TOTAL_AMOUNT]));
            
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long discountTypeId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_PRICE_PROTECTION_ITEM + " WHERE " +
                    PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_POS_PRICE_PROTECTION_ID] + " = " + discountTypeId;

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
            String sql = "SELECT COUNT(" + PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_POS_PRICE_PROTECTION_ID] + ") FROM " + TBL_POS_PRICE_PROTECTION_ITEM;
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
    
    
     public static double getSum(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(" + PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_TOTAL_AMOUNT] + ") FROM " + TBL_POS_PRICE_PROTECTION_ITEM;
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
                    PriceProtectionItem priceProtectionItem = (PriceProtectionItem) list.get(ls);
                    if (oid == priceProtectionItem.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }


   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         PriceProtectionItem priceProtectionItem = PstPriceProtectionItem.fetchExc(oid);
         object.put(PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_POS_PRICE_PROTECTION_ITEM_ID], priceProtectionItem.getOID());
         object.put(PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_POS_PRICE_PROTECTION_ID], priceProtectionItem.getPriceProtectionId());
         object.put(PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_MATERIAL_ID], priceProtectionItem.getMaterialId());
         object.put(PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_AMOUNT], priceProtectionItem.getAmount());
         object.put(PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_STOCK_ON_HAND], priceProtectionItem.getStockOnHand());
         object.put(PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_TOTAL_AMOUNT], priceProtectionItem.getTotalAmount());
      }catch(Exception exc){}
      return object;
   }
   
}