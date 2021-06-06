package com.dimata.posbo.entity.masterdata;

import com.dimata.common.entity.location.PstLocation;
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;
import org.json.JSONObject;

public class PstKitchenProduksiMapping extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_KITCHENPRODUKSIMAPPING = "pos_kitchen_produksi_mapping";
    public static final int FLD_POSMAPPINGPRODUKSIID = 0;
    public static final int FLD_SUBCATEGORYID = 1;
    public static final int FLD_LOCATIONORDERID = 2;
    public static final int FLD_LOCATIONPRODUKSIID = 3;

    public static String[] fieldNames = {
        "POS_MAPPING_PRODUKSI_ID",
        "SUB_CATEGORY_ID",
        "LOCATION_ORDER_ID",
        "LOCATION_PRODUKSI_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG
    };

    public PstKitchenProduksiMapping() {
    }

    public PstKitchenProduksiMapping(int i) throws DBException {
        super(new PstKitchenProduksiMapping());
    }

    public PstKitchenProduksiMapping(String sOid) throws DBException {
        super(new PstKitchenProduksiMapping(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstKitchenProduksiMapping(long lOid) throws DBException {
        super(new PstKitchenProduksiMapping(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_KITCHENPRODUKSIMAPPING;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstKitchenProduksiMapping().getClass().getName();
    }

    public static KitchenProduksiMapping fetchExc(long oid) throws DBException {
        try {
            KitchenProduksiMapping entKitchenProduksiMapping = new KitchenProduksiMapping();
            PstKitchenProduksiMapping pstKitchenProduksiMapping = new PstKitchenProduksiMapping(oid);
            entKitchenProduksiMapping.setOID(oid);
            entKitchenProduksiMapping.setSubCategoryId(pstKitchenProduksiMapping.getlong(FLD_SUBCATEGORYID));
            entKitchenProduksiMapping.setLocationOrderId(pstKitchenProduksiMapping.getlong(FLD_LOCATIONORDERID));
            entKitchenProduksiMapping.setLocationProduksiId(pstKitchenProduksiMapping.getlong(FLD_LOCATIONPRODUKSIID));
            return entKitchenProduksiMapping;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstKitchenProduksiMapping(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        KitchenProduksiMapping entKitchenProduksiMapping = fetchExc(entity.getOID());
        entity = (Entity) entKitchenProduksiMapping;
        return entKitchenProduksiMapping.getOID();
    }

    public static synchronized long updateExc(KitchenProduksiMapping entKitchenProduksiMapping) throws DBException {
        try {
            if (entKitchenProduksiMapping.getOID() != 0) {
                PstKitchenProduksiMapping pstKitchenProduksiMapping = new PstKitchenProduksiMapping(entKitchenProduksiMapping.getOID());
                pstKitchenProduksiMapping.setLong(FLD_SUBCATEGORYID, entKitchenProduksiMapping.getSubCategoryId());
                pstKitchenProduksiMapping.setLong(FLD_LOCATIONORDERID, entKitchenProduksiMapping.getLocationOrderId());
                pstKitchenProduksiMapping.setLong(FLD_LOCATIONPRODUKSIID, entKitchenProduksiMapping.getLocationProduksiId());
                pstKitchenProduksiMapping.update();
                return entKitchenProduksiMapping.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstKitchenProduksiMapping(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((KitchenProduksiMapping) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstKitchenProduksiMapping pstKitchenProduksiMapping = new PstKitchenProduksiMapping(oid);
            pstKitchenProduksiMapping.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstKitchenProduksiMapping(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(KitchenProduksiMapping entKitchenProduksiMapping) throws DBException {
        try {
            PstKitchenProduksiMapping pstKitchenProduksiMapping = new PstKitchenProduksiMapping(0);
            pstKitchenProduksiMapping.setLong(FLD_SUBCATEGORYID, entKitchenProduksiMapping.getSubCategoryId());
            pstKitchenProduksiMapping.setLong(FLD_LOCATIONORDERID, entKitchenProduksiMapping.getLocationOrderId());
            pstKitchenProduksiMapping.setLong(FLD_LOCATIONPRODUKSIID, entKitchenProduksiMapping.getLocationProduksiId());
            pstKitchenProduksiMapping.insert();
            entKitchenProduksiMapping.setOID(pstKitchenProduksiMapping.getlong(FLD_POSMAPPINGPRODUKSIID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstKitchenProduksiMapping(0), DBException.UNKNOWN);
        }
        return entKitchenProduksiMapping.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((KitchenProduksiMapping) entity);
    }

    public static void resultToObject(ResultSet rs, KitchenProduksiMapping entKitchenProduksiMapping) {
        try {
            entKitchenProduksiMapping.setOID(rs.getLong(PstKitchenProduksiMapping.fieldNames[PstKitchenProduksiMapping.FLD_POSMAPPINGPRODUKSIID]));
            entKitchenProduksiMapping.setSubCategoryId(rs.getLong(PstKitchenProduksiMapping.fieldNames[PstKitchenProduksiMapping.FLD_SUBCATEGORYID]));
            entKitchenProduksiMapping.setLocationOrderId(rs.getLong(PstKitchenProduksiMapping.fieldNames[PstKitchenProduksiMapping.FLD_LOCATIONORDERID]));
            entKitchenProduksiMapping.setLocationProduksiId(rs.getLong(PstKitchenProduksiMapping.fieldNames[PstKitchenProduksiMapping.FLD_LOCATIONPRODUKSIID]));
        } catch (Exception e) {
        }
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_KITCHENPRODUKSIMAPPING;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                KitchenProduksiMapping entKitchenProduksiMapping = new KitchenProduksiMapping();
                resultToObject(rs, entKitchenProduksiMapping);
                lists.add(entKitchenProduksiMapping);
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

    public static Vector listJoin(int limitStart, int recordToGet, String whereClause, String order, int mappingProduksi) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT ";

            sql = sql + "M." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " AS LOCATION_ORDER_ID ";
            sql = sql + ",M." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " AS LOCATION_ORDER ";
            sql = sql + ",L." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " AS LOCATION_PRODUKSI_ID ";
            sql = sql + ",L." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " AS LOCATION_PRODUKSI ";
            sql = sql + ",PM." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " AS NAME_CAT ";
            sql = sql + ",KP." + PstKitchenProduksiMapping.fieldNames[PstKitchenProduksiMapping.FLD_POSMAPPINGPRODUKSIID] + " AS PRODUKSI_ID ";

            if (mappingProduksi == 0) {//pos merk
                sql = sql + ",PM." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + " AS NAME_CAT_ID ";
            } else if (mappingProduksi == 1) { //pos_sub_category
                sql = sql + ",PM." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + " AS NAME_CAT_ID ";
            } else {//pos_category
                sql = sql + ",PM." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " AS NAME_CAT_ID ";
            }

            sql = sql + "FROM " + TBL_KITCHENPRODUKSIMAPPING + " AS KP ";

            if (mappingProduksi == 0) {//pos merk
                sql = sql + " INNER JOIN " + PstMerk.TBL_MAT_MERK + " AS PM ON PM." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "=KP." + PstKitchenProduksiMapping.fieldNames[PstKitchenProduksiMapping.FLD_SUBCATEGORYID];
            } else if (mappingProduksi == 1) { //pos_sub_category
                sql = sql + " INNER JOIN " + PstSubCategory.TBL_SUB_CATEGORY + " AS PM ON PM." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + "=KP." + PstKitchenProduksiMapping.fieldNames[PstKitchenProduksiMapping.FLD_SUBCATEGORYID];
            } else {//pos_category
                sql = sql + " INNER JOIN " + PstCategory.TBL_CATEGORY + " AS PM ON PM." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + "=KP." + PstKitchenProduksiMapping.fieldNames[PstKitchenProduksiMapping.FLD_SUBCATEGORYID];
            }
            sql = sql + " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " AS M ON M." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + "=KP." + PstKitchenProduksiMapping.fieldNames[PstKitchenProduksiMapping.FLD_LOCATIONORDERID];
            sql = sql + " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " AS L ON L." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + "=KP." + PstKitchenProduksiMapping.fieldNames[PstKitchenProduksiMapping.FLD_LOCATIONPRODUKSIID];

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                KitchenProduksiMapping entKitchenProduksiMapping = new KitchenProduksiMapping();
                entKitchenProduksiMapping.setOID(rs.getLong("PRODUKSI_ID"));
                entKitchenProduksiMapping.setLocationOrder(rs.getString("LOCATION_ORDER"));
                entKitchenProduksiMapping.setLocationOrderId(rs.getLong("LOCATION_ORDER_ID"));
                entKitchenProduksiMapping.setLocationProduksi(rs.getString("LOCATION_PRODUKSI"));
                entKitchenProduksiMapping.setLocationProduksiId(rs.getLong("LOCATION_PRODUKSI_ID"));

                entKitchenProduksiMapping.setSubCategory(rs.getString("NAME_CAT"));
                entKitchenProduksiMapping.setSubCategoryId(rs.getLong("NAME_CAT_ID"));

                lists.add(entKitchenProduksiMapping);
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

    public static boolean checkOID(long entKitchenProduksiMappingId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_KITCHENPRODUKSIMAPPING + " WHERE "
                    + PstKitchenProduksiMapping.fieldNames[PstKitchenProduksiMapping.FLD_POSMAPPINGPRODUKSIID] + " = " + entKitchenProduksiMappingId;
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
            String sql = "SELECT COUNT(" + PstKitchenProduksiMapping.fieldNames[PstKitchenProduksiMapping.FLD_POSMAPPINGPRODUKSIID] + ") FROM " + TBL_KITCHENPRODUKSIMAPPING;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
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

    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    KitchenProduksiMapping entKitchenProduksiMapping = (KitchenProduksiMapping) list.get(ls);
                    if (oid == entKitchenProduksiMapping.getOID()) {
                        found = true;
                    }
                }
            }
        }
        if ((start >= size) && (size > 0)) {
            start = start - recordToGet;
        }
        return start;
    }

    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + (recordToGet - mdl);
        if (start == 0) {
            cmd = Command.FIRST;
        } else if (start == (vectSize - recordToGet)) {
            cmd = Command.LAST;
        } else {
            start = start + recordToGet;
            if (start <= (vectSize - recordToGet)) {
                cmd = Command.NEXT;
                System.out.println("next.......................");
            } else {
                start = start - recordToGet;
                if (start > 0) {
                    cmd = Command.PREV;
                    System.out.println("prev.......................");
                }
            }
        }
        return cmd;
    }
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                KitchenProduksiMapping kitchenProduksiMapping = PstKitchenProduksiMapping.fetchExc(oid);
                object.put(PstKitchenProduksiMapping.fieldNames[PstKitchenProduksiMapping.FLD_POSMAPPINGPRODUKSIID], kitchenProduksiMapping.getOID());
                object.put(PstKitchenProduksiMapping.fieldNames[PstKitchenProduksiMapping.FLD_LOCATIONORDERID], kitchenProduksiMapping.getLocationOrderId());
                object.put(PstKitchenProduksiMapping.fieldNames[PstKitchenProduksiMapping.FLD_LOCATIONPRODUKSIID], kitchenProduksiMapping.getLocationProduksiId());
                object.put(PstKitchenProduksiMapping.fieldNames[PstKitchenProduksiMapping.FLD_SUBCATEGORYID], kitchenProduksiMapping.getSubCategoryId());
            }catch(Exception exc){}

            return object;
        }
}
