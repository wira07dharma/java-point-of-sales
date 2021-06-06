/* Created on 	:  [date] [time] AM/PM
 *
 * @author	 :
 * @version	 :
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.posbo.entity.masterdata;

import java.sql.*;
import java.util.*;

import com.dimata.util.lang.I_Language;
import com.dimata.qdep.entity.*;
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.masterdata.*;
import org.json.JSONObject;

public class PstCodeRange extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_POS_RANGE_CODE = "pos_range_code";

    public static final int FLD_RANGE_CODE_ID = 0;
    public static final int FLD_FROM_RANGE_CODE = 1;
    public static final int FLD_TO_RANGE_NAME = 2;

    public static final String[] fieldNames = {
        "RANGE_CODE_ID",
        "FROMCODE",
        "TOCODE"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING
    };

    public PstCodeRange() {
    }

    public PstCodeRange(int i) throws DBException {
        super(new PstCodeRange());
    }

    public PstCodeRange(String sOid) throws DBException {
        super(new PstCodeRange(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstCodeRange(long lOid) throws DBException {
        super(new PstCodeRange(0));
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
        return TBL_POS_RANGE_CODE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstCodeRange().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        CodeRange codeRange = fetchExc(ent.getOID());
        ent = (Entity) codeRange;
        return codeRange.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((CodeRange) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((CodeRange) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static CodeRange fetchExc(long oid) throws DBException {
        try {
            CodeRange codeRange = new CodeRange();
            PstCodeRange pstCodeRange = new PstCodeRange(oid);
            codeRange.setOID(oid);

            codeRange.setFromRangeCode(pstCodeRange.getString(FLD_FROM_RANGE_CODE));
            codeRange.setToRangeCode(pstCodeRange.getString(FLD_TO_RANGE_NAME));

            return codeRange;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCodeRange(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(CodeRange codeRange) throws DBException {
        try {
            PstCodeRange pstCodeRange = new PstCodeRange(0);

            pstCodeRange.setString(FLD_FROM_RANGE_CODE, codeRange.getFromRangeCode());
            pstCodeRange.setString(FLD_TO_RANGE_NAME, codeRange.getToRangeCode());

            pstCodeRange.insert();
            codeRange.setOID(pstCodeRange.getlong(FLD_RANGE_CODE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCodeRange(0), DBException.UNKNOWN);
        }
        return codeRange.getOID();
    }

    public static long updateExc(CodeRange codeRange) throws DBException {
        try {
            if (codeRange.getOID() != 0) {
                PstCodeRange pstCodeRange = new PstCodeRange(codeRange.getOID());

                pstCodeRange.setString(FLD_FROM_RANGE_CODE, codeRange.getFromRangeCode());
                pstCodeRange.setString(FLD_TO_RANGE_NAME, codeRange.getToRangeCode());

                pstCodeRange.update();
                return codeRange.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCodeRange(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstCodeRange pstCodeRange = new PstCodeRange(oid);
            pstCodeRange.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCodeRange(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_RANGE_CODE;
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
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                CodeRange codeRange = new CodeRange();
                resultToObject(rs, codeRange);
                lists.add(codeRange);
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

    private static void resultToObject(ResultSet rs, CodeRange codeRange) {
        try {
            codeRange.setOID(rs.getLong(PstCodeRange.fieldNames[PstCodeRange.FLD_RANGE_CODE_ID]));
            codeRange.setFromRangeCode(rs.getString(PstCodeRange.fieldNames[PstCodeRange.FLD_FROM_RANGE_CODE]));
            codeRange.setToRangeCode(rs.getString(PstCodeRange.fieldNames[PstCodeRange.FLD_TO_RANGE_NAME]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long discountTypeId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_RANGE_CODE + " WHERE " +
                    PstCodeRange.fieldNames[PstCodeRange.FLD_RANGE_CODE_ID] + " = " + discountTypeId;

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
            String sql = "SELECT COUNT(" + PstCodeRange.fieldNames[PstCodeRange.FLD_RANGE_CODE_ID] + ") FROM " + TBL_POS_RANGE_CODE;
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
                    CodeRange codeRange = (CodeRange) list.get(ls);
                    if (oid == codeRange.getOID())
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
                CodeRange codeRange = PstCodeRange.fetchExc(oid);
                object.put(PstCodeRange.fieldNames[PstCodeRange.FLD_RANGE_CODE_ID], codeRange.getOID());
                object.put(PstCodeRange.fieldNames[PstCodeRange.FLD_FROM_RANGE_CODE], codeRange.getFromRangeCode());
                object.put(PstCodeRange.fieldNames[PstCodeRange.FLD_TO_RANGE_NAME], codeRange.getToRangeCode());
            }catch(Exception exc){}

            return object;
        }
}
