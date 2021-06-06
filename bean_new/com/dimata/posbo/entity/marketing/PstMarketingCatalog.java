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
public class PstMarketingCatalog extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MARKETING_KATALOG = "pos_marketing_katalog";
    public static final int FLD_MARKETING_KATALOG_ID = 0;
    public static final int FLD_MARKETING_KATALOG_TITLE= 1;
    public static final int FLD_MARKETING_KATALOG_START_DATE = 2;
    public static final int FLD_MARKETING_KATALOG_END_DATE = 3;
    public static final int FLD_MARKETING_KATALOG_STATUS = 4;
    

    public static String[] fieldNames = {
        "MARKETING_KATALOG_ID",
        "MARKETING_KATALOG_TITLE",
        "MARKETING_KATALOG_START_DATE",
        "MARKETING_KATALOG_END_DATE",
        "MARKETING_KATALOG_STATUS"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT
    };
    
    public static final String[] status = {"Draft","Cancel", "Approve"};
    
    public PstMarketingCatalog() {
    }

    public PstMarketingCatalog(int i) throws DBException {
        super(new PstMarketingCatalog());
    }

    public PstMarketingCatalog(String sOid) throws DBException {
        super(new PstMarketingCatalog(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMarketingCatalog(long lOid) throws DBException {
        super(new PstMarketingCatalog(0));
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
        return TBL_MARKETING_KATALOG;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMarketingCatalog().getClass().getName();
    }

     public static MarketingCatalog fetchExc(long oid) throws DBException {
        try {
            MarketingCatalog marketingCatalog = new MarketingCatalog();
            PstMarketingCatalog pstMarketingCatalog = new PstMarketingCatalog(oid);
            marketingCatalog.setOID(oid);
            marketingCatalog.setMarketing_katalog_title(pstMarketingCatalog.getString(FLD_MARKETING_KATALOG_TITLE));
            marketingCatalog.setMarketing_katalog_start_date(pstMarketingCatalog.getDate(FLD_MARKETING_KATALOG_START_DATE));
            marketingCatalog.setMarketing_katalog_end_date(pstMarketingCatalog.getDate(FLD_MARKETING_KATALOG_END_DATE));
            marketingCatalog.setMarketing_katalog_status(pstMarketingCatalog.getInt(FLD_MARKETING_KATALOG_STATUS));
            return marketingCatalog;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingCatalog(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MarketingCatalog marketingCatalog = fetchExc(entity.getOID());
        entity = (Entity) marketingCatalog;
        return marketingCatalog.getOID();
    }

    public static synchronized long updateExc(MarketingCatalog marketingCatalog) throws DBException {
        try {
            if (marketingCatalog.getOID() != 0) {
                PstMarketingCatalog pstMarketingCatalog = new PstMarketingCatalog(marketingCatalog.getOID());
                pstMarketingCatalog.setString(FLD_MARKETING_KATALOG_TITLE, marketingCatalog.getMarketing_katalog_title());
                pstMarketingCatalog.setDate(FLD_MARKETING_KATALOG_START_DATE, marketingCatalog.getMarketing_katalog_start_date());
                pstMarketingCatalog.setDate(FLD_MARKETING_KATALOG_END_DATE, marketingCatalog.getMarketing_katalog_end_date());
                pstMarketingCatalog.setInt(FLD_MARKETING_KATALOG_STATUS, marketingCatalog.getMarketing_katalog_status());
                pstMarketingCatalog.update();
                return marketingCatalog.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingCatalog(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MarketingCatalog) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMarketingCatalog pstMarketingCatalog = new PstMarketingCatalog(oid);
            pstMarketingCatalog.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingCatalog(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MarketingCatalog marketingCatalog) throws DBException {
        try {
            PstMarketingCatalog pstMarketingCatalog = new PstMarketingCatalog(0);
                pstMarketingCatalog.setString(FLD_MARKETING_KATALOG_TITLE, marketingCatalog.getMarketing_katalog_title());
                pstMarketingCatalog.setDate(FLD_MARKETING_KATALOG_START_DATE, marketingCatalog.getMarketing_katalog_start_date());
                pstMarketingCatalog.setDate(FLD_MARKETING_KATALOG_END_DATE, marketingCatalog.getMarketing_katalog_end_date());
                pstMarketingCatalog.setInt(FLD_MARKETING_KATALOG_STATUS, marketingCatalog.getMarketing_katalog_status());
              
            pstMarketingCatalog.insert();
            marketingCatalog.setOID(pstMarketingCatalog.getlong(FLD_MARKETING_KATALOG_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingCatalog(0), DBException.UNKNOWN);
        }
        return marketingCatalog.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MarketingCatalog) entity);
    }

    public static void resultToObject(ResultSet rs, MarketingCatalog marketingCatalog) {
        try {
            marketingCatalog.setOID(rs.getLong(PstMarketingCatalog.fieldNames[PstMarketingCatalog.FLD_MARKETING_KATALOG_ID]));
            marketingCatalog.setMarketing_katalog_title(rs.getString(PstMarketingCatalog.fieldNames[PstMarketingCatalog.FLD_MARKETING_KATALOG_TITLE]));
            marketingCatalog.setMarketing_katalog_start_date(rs.getDate(PstMarketingCatalog.fieldNames[PstMarketingCatalog.FLD_MARKETING_KATALOG_START_DATE]));
            marketingCatalog.setMarketing_katalog_end_date(rs.getDate(PstMarketingCatalog.fieldNames[PstMarketingCatalog.FLD_MARKETING_KATALOG_END_DATE]));
            marketingCatalog.setMarketing_katalog_status(rs.getInt(PstMarketingCatalog.fieldNames[PstMarketingCatalog.FLD_MARKETING_KATALOG_STATUS]));
           
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
            String sql = "SELECT * FROM " + TBL_MARKETING_KATALOG;
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
                MarketingCatalog marketingCatalog = new MarketingCatalog();
                resultToObject(rs, marketingCatalog);
                lists.add(marketingCatalog);
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

    public static boolean checkOID(long marketingCatalogId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MARKETING_KATALOG + " WHERE "
                    + PstMarketingCatalog.fieldNames[PstMarketingCatalog.FLD_MARKETING_KATALOG_ID] + " = " + marketingCatalogId;
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
            String sql = "SELECT COUNT(" + PstMarketingCatalog.fieldNames[PstMarketingCatalog.FLD_MARKETING_KATALOG_ID] + ") FROM " + TBL_MARKETING_KATALOG;
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
                    MarketingCatalog marketingCatalog = (MarketingCatalog) list.get(ls);
                    if (oid == marketingCatalog.getOID()) {
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
