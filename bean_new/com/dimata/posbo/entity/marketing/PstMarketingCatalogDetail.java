/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.marketing;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import static com.dimata.qdep.db.I_DBType.TYPE_ID;
import static com.dimata.qdep.db.I_DBType.TYPE_INT;
import static com.dimata.qdep.db.I_DBType.TYPE_LONG;
import static com.dimata.qdep.db.I_DBType.TYPE_PK;
import static com.dimata.qdep.db.I_DBType.TYPE_STRING;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

/**
 *
 * @author Dimata 007
 */
public class PstMarketingCatalogDetail extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MARKETING_KATALOG_DETAIL = "pos_marketing_katalog_detail";
    public static final int FLD_MARKETING_KATALOG_DETAIL_ID = 0;
    public static final int FLD_MARKETING_KATALOG_ID = 1;
    public static final int FLD_MARKETING_KATALOG_MATERIAL_ID = 2;
    public static final int FLD_MARKETING_KATALOG_MATERIAL_CATEGORY_ID = 3;
    

    public static String[] fieldNames = {
        "MARKETING_KATALOG_DETAIL_ID",
        "MARKETING_KATALOG_ID",
        "MARKETING_MATERIAL_ID",
        "MARKETING_MATERIAL_CATEGORY_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG
    };
    
    public PstMarketingCatalogDetail() {
    }

    public PstMarketingCatalogDetail(int i) throws DBException {
        super(new PstMarketingCatalogDetail());
    }

    public PstMarketingCatalogDetail(String sOid) throws DBException {
        super(new PstMarketingCatalogDetail(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMarketingCatalogDetail(long lOid) throws DBException {
        super(new PstMarketingCatalogDetail(0));
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
        return TBL_MARKETING_KATALOG_DETAIL;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMarketingCatalogDetail().getClass().getName();
    }

     public static MarketingCatalogDetail fetchExc(long oid) throws DBException {
        try {
            MarketingCatalogDetail marketingCatalogDetail = new MarketingCatalogDetail();
            PstMarketingCatalogDetail pstMarketingCatalogDetail = new PstMarketingCatalogDetail(oid);
            marketingCatalogDetail.setOID(oid);
            marketingCatalogDetail.setMarketing_catalog_id(pstMarketingCatalogDetail.getLong(FLD_MARKETING_KATALOG_ID));
            marketingCatalogDetail.setMarketing_material_id(pstMarketingCatalogDetail.getLong(FLD_MARKETING_KATALOG_MATERIAL_ID));
            marketingCatalogDetail.setMarketing_material_category_id(pstMarketingCatalogDetail.getLong(FLD_MARKETING_KATALOG_MATERIAL_CATEGORY_ID));
            
            return marketingCatalogDetail;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingCatalogDetail(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MarketingCatalogDetail marketingCatalogDetail = fetchExc(entity.getOID());
        entity = (Entity) marketingCatalogDetail;
        return marketingCatalogDetail.getOID();
    }

    public static synchronized long updateExc(MarketingCatalogDetail marketingCatalogDetail) throws DBException {
        try {
            if (marketingCatalogDetail.getOID() != 0) {
                PstMarketingCatalogDetail pstMarketingCatalogDetail = new PstMarketingCatalogDetail(marketingCatalogDetail.getOID());
                pstMarketingCatalogDetail.setLong(FLD_MARKETING_KATALOG_ID, marketingCatalogDetail.getMarketing_catalog_id());
                pstMarketingCatalogDetail.setLong(FLD_MARKETING_KATALOG_MATERIAL_ID, marketingCatalogDetail.getMarketing_material_id());
                pstMarketingCatalogDetail.setLong(FLD_MARKETING_KATALOG_MATERIAL_CATEGORY_ID, marketingCatalogDetail.getMarketing_material_category_id());
              
                pstMarketingCatalogDetail.update();
                return marketingCatalogDetail.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingCatalogDetail(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MarketingCatalogDetail) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMarketingCatalogDetail pstMarketingCatalogDetail = new PstMarketingCatalogDetail(oid);
            pstMarketingCatalogDetail.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingCatalogDetail(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MarketingCatalogDetail marketingCatalogDetail) throws DBException {
        try {
            
                PstMarketingCatalogDetail pstMarketingCatalogDetail = new PstMarketingCatalogDetail(0);
                pstMarketingCatalogDetail.setLong(FLD_MARKETING_KATALOG_ID, marketingCatalogDetail.getMarketing_catalog_id());
                pstMarketingCatalogDetail.setLong(FLD_MARKETING_KATALOG_MATERIAL_ID, marketingCatalogDetail.getMarketing_material_id());
                pstMarketingCatalogDetail.setLong(FLD_MARKETING_KATALOG_MATERIAL_CATEGORY_ID, marketingCatalogDetail.getMarketing_material_category_id());
                
            pstMarketingCatalogDetail.insert();
            marketingCatalogDetail.setOID(pstMarketingCatalogDetail.getlong(FLD_MARKETING_KATALOG_DETAIL_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingCatalogDetail(0), DBException.UNKNOWN);
        }
        return marketingCatalogDetail.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MarketingCatalogDetail) entity);
    }

    public static void resultToObject(ResultSet rs, MarketingCatalogDetail marketingCatalogDetail) {
        try {
            marketingCatalogDetail.setOID(rs.getLong(PstMarketingCatalogDetail.fieldNames[PstMarketingCatalogDetail.FLD_MARKETING_KATALOG_DETAIL_ID]));
            marketingCatalogDetail.setMarketing_catalog_id(rs.getLong(PstMarketingCatalogDetail.fieldNames[PstMarketingCatalogDetail.FLD_MARKETING_KATALOG_ID]));
            marketingCatalogDetail.setMarketing_material_id(rs.getLong(PstMarketingCatalogDetail.fieldNames[PstMarketingCatalogDetail.FLD_MARKETING_KATALOG_MATERIAL_ID]));
            marketingCatalogDetail.setMarketing_material_category_id(rs.getLong(PstMarketingCatalogDetail.fieldNames[PstMarketingCatalogDetail.FLD_MARKETING_KATALOG_MATERIAL_CATEGORY_ID]));           
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
            String sql = "SELECT * FROM " + TBL_MARKETING_KATALOG_DETAIL;
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
                MarketingCatalogDetail marketingCatalogDetail = new MarketingCatalogDetail();
                resultToObject(rs, marketingCatalogDetail);
                lists.add(marketingCatalogDetail);
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

    public static boolean checkOID(long marketingCatalogDetailId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MARKETING_KATALOG_DETAIL + " WHERE "
                    + PstMarketingCatalogDetail.fieldNames[PstMarketingCatalogDetail.FLD_MARKETING_KATALOG_DETAIL_ID] + " = " + marketingCatalogDetailId;
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
            String sql = "SELECT COUNT(" + PstMarketingCatalogDetail.fieldNames[PstMarketingCatalogDetail.FLD_MARKETING_KATALOG_DETAIL_ID] + ") FROM " + TBL_MARKETING_KATALOG_DETAIL;
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
                    MarketingCatalogDetail marketingCatalogDetail = (MarketingCatalogDetail) list.get(ls);
                    if (oid == marketingCatalogDetail.getOID()) {
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
        } else {
            if (start == (vectSize - recordToGet)) {
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
        }
        return cmd;
    }
}
