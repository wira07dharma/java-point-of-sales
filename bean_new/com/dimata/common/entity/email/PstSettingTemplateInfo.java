/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.entity.email;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

/**
 *
 * @author Dimata 007
 */
public class PstSettingTemplateInfo extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_SETTING_TEMPLATE_INFO = "setting_template_info";
    public static final int FLD_TEMPLATE_INFO_ID = 0;
    public static final int FLD_TEMPLATE_INFO_APPLICATION = 1;
    public static final int FLD_TEMPLATE_INFO_TYPE_MENU = 2;
    public static final int FLD_TEMPLATE_INFO_TYPE_SEND = 3;
    public static final int FLD_TEMPLATE_INFO_TYPE_SEND_BY = 4;
    public static final int FLD_TEMPLATE_INFO_CONTENT_TEXT = 5;
    public static final int FLD_TEMPLATE_INFO_CONTENT_HTML = 6;
    public static final int FLD_TEMPLATE_INFO_SUBJECT = 7;
    public static final int FLD_TEMPLATE_INFO_DATE_START = 8;
    public static final int FLD_TEMPLATE_INFO_DATE_END = 9;

    public static String[] fieldNames = {
        "TEMPLATE_INFO_ID",
        "TEMPLATE_INFO_APPLICATION",
        "TEMPLATE_INFO_TYPE_MENU",
        "TEMPLATE_INFO_TYPE_SEND",
        "TEMPLATE_INFO_TYPE_SEND_BY",
        "TEMPLATE_INFO_CONTENT_TEXT",
        "TEMPLATE_INFO_CONTENT_HTML",
        "TEMPLATE_INFO_SUBJECT",
        "TEMPLATE_INFO_DATE_START",
        "TEMPLATE_INFO_DATE_END"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_DATE
    };

    public PstSettingTemplateInfo() {
    }

    public PstSettingTemplateInfo(int i) throws DBException {
        super(new PstSettingTemplateInfo());
    }

    public PstSettingTemplateInfo(String sOid) throws DBException {
        super(new PstSettingTemplateInfo(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstSettingTemplateInfo(long lOid) throws DBException {
        super(new PstSettingTemplateInfo(0));
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
        return TBL_SETTING_TEMPLATE_INFO;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstSettingTemplateInfo().getClass().getName();
    }

    public static SettingTemplateInfo fetchExc(long oid) throws DBException {
        try {
            SettingTemplateInfo entSettingTemplateInfo = new SettingTemplateInfo();
            PstSettingTemplateInfo pstSettingTemplateInfo = new PstSettingTemplateInfo(oid);
            entSettingTemplateInfo.setOID(oid);
            entSettingTemplateInfo.setTemplateInfoApplication(pstSettingTemplateInfo.getString(FLD_TEMPLATE_INFO_APPLICATION));
            entSettingTemplateInfo.setTemplateInfoTypeMenu(pstSettingTemplateInfo.getString(FLD_TEMPLATE_INFO_TYPE_MENU));
            entSettingTemplateInfo.setTemplateInfoTypeSend(pstSettingTemplateInfo.getInt(FLD_TEMPLATE_INFO_TYPE_SEND));
            entSettingTemplateInfo.setTemplateInfoTypeSendBy(pstSettingTemplateInfo.getInt(FLD_TEMPLATE_INFO_TYPE_SEND_BY));
            entSettingTemplateInfo.setTemplateInfoContentText(pstSettingTemplateInfo.getString(FLD_TEMPLATE_INFO_CONTENT_TEXT));
            entSettingTemplateInfo.setTemplateInfoContentHtml(pstSettingTemplateInfo.getString(FLD_TEMPLATE_INFO_CONTENT_HTML));
            entSettingTemplateInfo.setTemplateInfoSubject(pstSettingTemplateInfo.getString(FLD_TEMPLATE_INFO_SUBJECT));
            entSettingTemplateInfo.setTemplateInfoDateStart(pstSettingTemplateInfo.getDate(FLD_TEMPLATE_INFO_DATE_START));
            entSettingTemplateInfo.setTemplateInfoDateEnd(pstSettingTemplateInfo.getDate(FLD_TEMPLATE_INFO_DATE_END));
            return entSettingTemplateInfo;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSettingTemplateInfo(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        SettingTemplateInfo entSettingTemplateInfo = fetchExc(entity.getOID());
        entity = (Entity) entSettingTemplateInfo;
        return entSettingTemplateInfo.getOID();
    }

    public static synchronized long updateExc(SettingTemplateInfo entSettingTemplateInfo) throws DBException {
        try {
            if (entSettingTemplateInfo.getOID() != 0) {
                PstSettingTemplateInfo pstSettingTemplateInfo = new PstSettingTemplateInfo(entSettingTemplateInfo.getOID());
                pstSettingTemplateInfo.setString(FLD_TEMPLATE_INFO_APPLICATION, entSettingTemplateInfo.getTemplateInfoApplication());
                pstSettingTemplateInfo.setString(FLD_TEMPLATE_INFO_TYPE_MENU, entSettingTemplateInfo.getTemplateInfoTypeMenu());
                pstSettingTemplateInfo.setInt(FLD_TEMPLATE_INFO_TYPE_SEND, entSettingTemplateInfo.getTemplateInfoTypeSend());
                pstSettingTemplateInfo.setInt(FLD_TEMPLATE_INFO_TYPE_SEND_BY, entSettingTemplateInfo.getTemplateInfoTypeSendBy());
                pstSettingTemplateInfo.setString(FLD_TEMPLATE_INFO_CONTENT_TEXT, entSettingTemplateInfo.getTemplateInfoContentText());
                pstSettingTemplateInfo.setString(FLD_TEMPLATE_INFO_CONTENT_HTML, entSettingTemplateInfo.getTemplateInfoContentHtml());
                pstSettingTemplateInfo.setString(FLD_TEMPLATE_INFO_SUBJECT, entSettingTemplateInfo.getTemplateInfoSubject());
                pstSettingTemplateInfo.setDate(FLD_TEMPLATE_INFO_DATE_START, entSettingTemplateInfo.getTemplateInfoDateStart());
                pstSettingTemplateInfo.setDate(FLD_TEMPLATE_INFO_DATE_END, entSettingTemplateInfo.getTemplateInfoDateEnd());
                pstSettingTemplateInfo.update();
                return entSettingTemplateInfo.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSettingTemplateInfo(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((SettingTemplateInfo) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstSettingTemplateInfo pstSettingTemplateInfo = new PstSettingTemplateInfo(oid);
            pstSettingTemplateInfo.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSettingTemplateInfo(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(SettingTemplateInfo entSettingTemplateInfo) throws DBException {
        try {
            PstSettingTemplateInfo pstSettingTemplateInfo = new PstSettingTemplateInfo(0);
            pstSettingTemplateInfo.setString(FLD_TEMPLATE_INFO_APPLICATION, entSettingTemplateInfo.getTemplateInfoApplication());
            pstSettingTemplateInfo.setString(FLD_TEMPLATE_INFO_TYPE_MENU, entSettingTemplateInfo.getTemplateInfoTypeMenu());
            pstSettingTemplateInfo.setInt(FLD_TEMPLATE_INFO_TYPE_SEND, entSettingTemplateInfo.getTemplateInfoTypeSend());
            pstSettingTemplateInfo.setInt(FLD_TEMPLATE_INFO_TYPE_SEND_BY, entSettingTemplateInfo.getTemplateInfoTypeSendBy());
            pstSettingTemplateInfo.setString(FLD_TEMPLATE_INFO_CONTENT_TEXT, entSettingTemplateInfo.getTemplateInfoContentText());
            pstSettingTemplateInfo.setString(FLD_TEMPLATE_INFO_CONTENT_HTML, entSettingTemplateInfo.getTemplateInfoContentHtml());
            pstSettingTemplateInfo.setString(FLD_TEMPLATE_INFO_SUBJECT, entSettingTemplateInfo.getTemplateInfoSubject());
            pstSettingTemplateInfo.setDate(FLD_TEMPLATE_INFO_DATE_START, entSettingTemplateInfo.getTemplateInfoDateStart());
            pstSettingTemplateInfo.setDate(FLD_TEMPLATE_INFO_DATE_END, entSettingTemplateInfo.getTemplateInfoDateEnd());
            pstSettingTemplateInfo.insert();
            entSettingTemplateInfo.setOID(pstSettingTemplateInfo.getlong(FLD_TEMPLATE_INFO_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSettingTemplateInfo(0), DBException.UNKNOWN);
        }
        return entSettingTemplateInfo.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((SettingTemplateInfo) entity);
    }

    public static void resultToObject(ResultSet rs, SettingTemplateInfo entSettingTemplateInfo) {
        try {
            entSettingTemplateInfo.setOID(rs.getLong(PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_ID]));
            entSettingTemplateInfo.setTemplateInfoApplication(rs.getString(PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_APPLICATION]));
            entSettingTemplateInfo.setTemplateInfoTypeMenu(rs.getString(PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_TYPE_MENU]));
            entSettingTemplateInfo.setTemplateInfoTypeSend(rs.getInt(PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_TYPE_SEND]));
            entSettingTemplateInfo.setTemplateInfoTypeSendBy(rs.getInt(PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_TYPE_SEND_BY]));
            entSettingTemplateInfo.setTemplateInfoContentText(rs.getString(PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_CONTENT_TEXT]));
            entSettingTemplateInfo.setTemplateInfoContentHtml(rs.getString(PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_CONTENT_HTML]));
            entSettingTemplateInfo.setTemplateInfoSubject(rs.getString(PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_SUBJECT]));
            entSettingTemplateInfo.setTemplateInfoDateStart(rs.getDate(PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_DATE_START]));
            entSettingTemplateInfo.setTemplateInfoDateEnd(rs.getDate(PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_DATE_END]));
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
            String sql = "SELECT * FROM " + TBL_SETTING_TEMPLATE_INFO;
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
                SettingTemplateInfo entSettingTemplateInfo = new SettingTemplateInfo();
                resultToObject(rs, entSettingTemplateInfo);
                lists.add(entSettingTemplateInfo);
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

    public static boolean checkOID(long entSettingTemplateInfoId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_SETTING_TEMPLATE_INFO + " WHERE "
                    + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_ID] + " = " + entSettingTemplateInfoId;
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
            String sql = "SELECT COUNT(" + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_ID] + ") FROM " + TBL_SETTING_TEMPLATE_INFO;
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
                    SettingTemplateInfo entSettingTemplateInfo = (SettingTemplateInfo) list.get(ls);
                    if (oid == entSettingTemplateInfo.getOID()) {
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
