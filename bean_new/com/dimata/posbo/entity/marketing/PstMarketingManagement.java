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
public class PstMarketingManagement extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MARKETING_MANAGEMENT = "pos_marketing_brosur";
    public static final int FLD_MARKETING_MANAGEMENT_ID = 0;
    public static final int FLD_MARKETING_MANAGEMENT_DESCRIPTION = 1;
    public static final int FLD_MARKETING_MANAGEMENT_START_DATE = 2;
    public static final int FLD_MARKETING_MANAGEMENT_END_DATE = 3;
    public static final int FLD_MARKETING_MANAGEMENT_TITLE = 4;
    public static final int FLD_MARKETING_MANAGEMENT_STATUS = 5;
    public static final int FLD_MARKETING_MANAGEMENT_NOTE = 6;
    public static final int FLD_MARKETING_MANAGEMENT_CREATE_DATE = 7;
    public static final int FLD_MARKETING_MANAGEMENT_CHECKED_DATE = 8;
    public static final int FLD_MARKETING_MANAGEMENT_EDITED_DATE = 9;
    

    public static String[] fieldNames = {
        "MARKETING_MANAGEMENT_ID",
        "MARKETING_DESCRIPTION",
        "START_DATE",
        "END_DATE",
        "MARKETING_TITLE",
        "MARKETING_STATUS",
        "MARKETING_NOTE",
        "CREATE_DATE",
        "CHECKED_DATE",
        "EDITED_DATE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_INT,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_DATE
    };
    
    
    
    public static final int STATUS_DRAFT = 0;
    public static final int STATUS_CANCEL = 1;
    public static final int STATUS_APPROVE = 2;
    
    
    public static final String[] status = {"Draft","Cancel", "Approve"};

    public PstMarketingManagement() {
    }

    public PstMarketingManagement(int i) throws DBException {
        super(new PstMarketingManagement());
    }

    public PstMarketingManagement(String sOid) throws DBException {
        super(new PstMarketingManagement(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMarketingManagement(long lOid) throws DBException {
        super(new PstMarketingManagement(0));
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
        return TBL_MARKETING_MANAGEMENT;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMarketingManagement().getClass().getName();
    }

    public static MarketingManagement fetchExc(long oid) throws DBException {
        try {
            MarketingManagement entMarketingManagement = new MarketingManagement();
            PstMarketingManagement pstMarketingManagement = new PstMarketingManagement(oid);
            entMarketingManagement.setOID(oid);
            entMarketingManagement.setMarketing_description(pstMarketingManagement.getString(FLD_MARKETING_MANAGEMENT_DESCRIPTION));
            entMarketingManagement.setStart_date(pstMarketingManagement.getDate(FLD_MARKETING_MANAGEMENT_START_DATE));
            entMarketingManagement.setEnd_date(pstMarketingManagement.getDate(FLD_MARKETING_MANAGEMENT_END_DATE));
            entMarketingManagement.setMarketing_title(pstMarketingManagement.getString(FLD_MARKETING_MANAGEMENT_TITLE));
            entMarketingManagement.setMarketing_status(pstMarketingManagement.getInt(FLD_MARKETING_MANAGEMENT_STATUS));
            entMarketingManagement.setMarketing_note(pstMarketingManagement.getString(FLD_MARKETING_MANAGEMENT_NOTE));
            entMarketingManagement.setCreate_date(pstMarketingManagement.getDate(FLD_MARKETING_MANAGEMENT_CREATE_DATE));
            entMarketingManagement.setChecked_date(pstMarketingManagement.getDate(FLD_MARKETING_MANAGEMENT_CHECKED_DATE));
            entMarketingManagement.setEdited_date(pstMarketingManagement.getDate(FLD_MARKETING_MANAGEMENT_EDITED_DATE));
            return entMarketingManagement;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingManagement(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MarketingManagement entMarketingManagement = fetchExc(entity.getOID());
        entity = (Entity) entMarketingManagement;
        return entMarketingManagement.getOID();
    }

    public static synchronized long updateExc(MarketingManagement entMarketingManagement) throws DBException {
        try {
            if (entMarketingManagement.getOID() != 0) {
                PstMarketingManagement pstMarketingManagement = new PstMarketingManagement(entMarketingManagement.getOID());
                pstMarketingManagement.setString(FLD_MARKETING_MANAGEMENT_DESCRIPTION, entMarketingManagement.getMarketing_description());
                pstMarketingManagement.setDate(FLD_MARKETING_MANAGEMENT_START_DATE, entMarketingManagement.getStart_date());
                pstMarketingManagement.setDate(FLD_MARKETING_MANAGEMENT_END_DATE, entMarketingManagement.getEnd_date());
                pstMarketingManagement.setString(FLD_MARKETING_MANAGEMENT_TITLE, entMarketingManagement.getMarketing_title());
                pstMarketingManagement.setInt(FLD_MARKETING_MANAGEMENT_STATUS, entMarketingManagement.getMarketing_status());
                pstMarketingManagement.setString(FLD_MARKETING_MANAGEMENT_NOTE, entMarketingManagement.getMarketing_note());
                pstMarketingManagement.setDate(FLD_MARKETING_MANAGEMENT_CREATE_DATE, entMarketingManagement.getCreate_date());
                pstMarketingManagement.setDate(FLD_MARKETING_MANAGEMENT_CHECKED_DATE, entMarketingManagement.getChecked_date());
                pstMarketingManagement.setDate(FLD_MARKETING_MANAGEMENT_EDITED_DATE, entMarketingManagement.getEdited_date());
                pstMarketingManagement.update();
                return entMarketingManagement.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingManagement(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MarketingManagement) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMarketingManagement pstMarketingManagement = new PstMarketingManagement(oid);
            pstMarketingManagement.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingManagement(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MarketingManagement entMarketingManagement) throws DBException {
        try {
            PstMarketingManagement pstMarketingManagement = new PstMarketingManagement(0);
                pstMarketingManagement.setString(FLD_MARKETING_MANAGEMENT_DESCRIPTION, entMarketingManagement.getMarketing_description());
                pstMarketingManagement.setDate(FLD_MARKETING_MANAGEMENT_START_DATE, entMarketingManagement.getStart_date());
                pstMarketingManagement.setDate(FLD_MARKETING_MANAGEMENT_END_DATE, entMarketingManagement.getEnd_date());
                pstMarketingManagement.setString(FLD_MARKETING_MANAGEMENT_TITLE, entMarketingManagement.getMarketing_title());
                pstMarketingManagement.setInt(FLD_MARKETING_MANAGEMENT_STATUS, entMarketingManagement.getMarketing_status());
                pstMarketingManagement.setString(FLD_MARKETING_MANAGEMENT_NOTE, entMarketingManagement.getMarketing_note());
                pstMarketingManagement.setDate(FLD_MARKETING_MANAGEMENT_CREATE_DATE, entMarketingManagement.getCreate_date());
                pstMarketingManagement.setDate(FLD_MARKETING_MANAGEMENT_CHECKED_DATE, entMarketingManagement.getChecked_date());
                pstMarketingManagement.setDate(FLD_MARKETING_MANAGEMENT_EDITED_DATE, entMarketingManagement.getEdited_date());
            pstMarketingManagement.insert();
            entMarketingManagement.setOID(pstMarketingManagement.getlong(FLD_MARKETING_MANAGEMENT_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMarketingManagement(0), DBException.UNKNOWN);
        }
        return entMarketingManagement.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MarketingManagement) entity);
    }

    public static void resultToObject(ResultSet rs, MarketingManagement entMarketingManagement) {
        try {
            entMarketingManagement.setOID(rs.getLong(PstMarketingManagement.fieldNames[PstMarketingManagement.FLD_MARKETING_MANAGEMENT_ID]));
            entMarketingManagement.setMarketing_description(rs.getString(PstMarketingManagement.fieldNames[PstMarketingManagement.FLD_MARKETING_MANAGEMENT_DESCRIPTION]));
            entMarketingManagement.setStart_date(rs.getDate(PstMarketingManagement.fieldNames[PstMarketingManagement.FLD_MARKETING_MANAGEMENT_START_DATE]));
            entMarketingManagement.setEnd_date(rs.getDate(PstMarketingManagement.fieldNames[PstMarketingManagement.FLD_MARKETING_MANAGEMENT_END_DATE]));
            entMarketingManagement.setMarketing_title(rs.getString(PstMarketingManagement.fieldNames[PstMarketingManagement.FLD_MARKETING_MANAGEMENT_TITLE]));
            entMarketingManagement.setMarketing_status(rs.getInt(PstMarketingManagement.fieldNames[PstMarketingManagement.FLD_MARKETING_MANAGEMENT_STATUS]));
            entMarketingManagement.setMarketing_note(rs.getString(PstMarketingManagement.fieldNames[PstMarketingManagement.FLD_MARKETING_MANAGEMENT_NOTE]));
            entMarketingManagement.setCreate_date(rs.getDate(PstMarketingManagement.fieldNames[PstMarketingManagement.FLD_MARKETING_MANAGEMENT_CREATE_DATE]));
            entMarketingManagement.setCreate_date(rs.getDate(PstMarketingManagement.fieldNames[PstMarketingManagement.FLD_MARKETING_MANAGEMENT_CHECKED_DATE]));
            entMarketingManagement.setCreate_date(rs.getDate(PstMarketingManagement.fieldNames[PstMarketingManagement.FLD_MARKETING_MANAGEMENT_EDITED_DATE]));
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
            String sql = "SELECT * FROM " + TBL_MARKETING_MANAGEMENT;
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
                MarketingManagement entMarketingManagement = new MarketingManagement();
                resultToObject(rs, entMarketingManagement);
                lists.add(entMarketingManagement);
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

    public static boolean checkOID(long entMarketingManagementId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MARKETING_MANAGEMENT + " WHERE "
                    + PstMarketingManagement.fieldNames[PstMarketingManagement.FLD_MARKETING_MANAGEMENT_ID] + " = " + entMarketingManagementId;
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
            String sql = "SELECT COUNT(" + PstMarketingManagement.fieldNames[PstMarketingManagement.FLD_MARKETING_MANAGEMENT_ID] + ") FROM " + TBL_MARKETING_MANAGEMENT;
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
                    MarketingManagement entMarketingManagement = (MarketingManagement) list.get(ls);
                    if (oid == entMarketingManagement.getOID()) {
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
