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
//Tidak terpakai...
package com.dimata.posbo.entity.masterdata;

import java.sql.*;
import java.util.*;

import com.dimata.util.lang.I_Language;
import com.dimata.qdep.entity.*;
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.masterdata.*;
import org.json.JSONObject;

public class PstDiscountType extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    //public static final  String TBL_POS_DISCOUNT_TYPE = "POS_DISCOUNT_TYPE";
    public static final String TBL_POS_DISCOUNT_TYPE = "pos_discount_type";
    //public static final String TBL_POS_DISCOUNT_TYPE = "discount_type";

    public static final int FLD_DISCOUNT_TYPE_ID = 0;
    public static final int FLD_CODE = 1;
    public static final int FLD_NAME = 2;

    public static final String[] fieldNames = {
        "DISCOUNT_TYPE_ID",
        "CODE",
        "NAME"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING
    };

    public PstDiscountType() {
    }

    public PstDiscountType(int i) throws DBException {
        super(new PstDiscountType());
    }

    public PstDiscountType(String sOid) throws DBException {
        super(new PstDiscountType(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstDiscountType(long lOid) throws DBException {
        super(new PstDiscountType(0));
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
        return TBL_POS_DISCOUNT_TYPE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstDiscountType().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        DiscountType discounttype = fetchExc(ent.getOID());
        ent = (Entity) discounttype;
        return discounttype.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((DiscountType) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((DiscountType) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static DiscountType fetchExc(long oid) throws DBException {
        try {
            DiscountType discounttype = new DiscountType();
            PstDiscountType pstDiscountType = new PstDiscountType(oid);
            discounttype.setOID(oid);

            discounttype.setCode(pstDiscountType.getString(FLD_CODE));
            discounttype.setName(pstDiscountType.getString(FLD_NAME));

            return discounttype;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDiscountType(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(DiscountType discounttype) throws DBException {
        try {
            PstDiscountType pstDiscountType = new PstDiscountType(0);

            pstDiscountType.setString(FLD_CODE, discounttype.getCode());
            pstDiscountType.setString(FLD_NAME, discounttype.getName());

            pstDiscountType.insert();

            long oidDataSync=PstDataSyncSql.insertExc(pstDiscountType.getInsertSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
            discounttype.setOID(pstDiscountType.getlong(FLD_DISCOUNT_TYPE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDiscountType(0), DBException.UNKNOWN);
        }
        return discounttype.getOID();
    }

    public static long updateExc(DiscountType discounttype) throws DBException {
        try {
            if (discounttype.getOID() != 0) {
                PstDiscountType pstDiscountType = new PstDiscountType(discounttype.getOID());

                pstDiscountType.setString(FLD_CODE, discounttype.getCode());
                pstDiscountType.setString(FLD_NAME, discounttype.getName());

                pstDiscountType.update();

                long oidDataSync=PstDataSyncSql.insertExc(pstDiscountType.getUpdateSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
                return discounttype.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDiscountType(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstDiscountType pstDiscountType = new PstDiscountType(oid);
            pstDiscountType.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDiscountType(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_DISCOUNT_TYPE;
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
                DiscountType discounttype = new DiscountType();
                resultToObject(rs, discounttype);
                lists.add(discounttype);
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

    private static void resultToObject(ResultSet rs, DiscountType discounttype) {
        try {
            discounttype.setOID(rs.getLong(PstDiscountType.fieldNames[PstDiscountType.FLD_DISCOUNT_TYPE_ID]));
            discounttype.setCode(rs.getString(PstDiscountType.fieldNames[PstDiscountType.FLD_CODE]));
            discounttype.setName(rs.getString(PstDiscountType.fieldNames[PstDiscountType.FLD_NAME]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long discountTypeId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_DISCOUNT_TYPE + " WHERE " +
                    PstDiscountType.fieldNames[PstDiscountType.FLD_DISCOUNT_TYPE_ID] + " = " + discountTypeId;

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
            String sql = "SELECT COUNT(" + PstDiscountType.fieldNames[PstDiscountType.FLD_DISCOUNT_TYPE_ID] + ") FROM " + TBL_POS_DISCOUNT_TYPE;
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
                    DiscountType discounttype = (DiscountType) list.get(ls);
                    if (oid == discounttype.getOID())
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
                DiscountType discountType = PstDiscountType.fetchExc(oid);
                object.put(PstDiscountType.fieldNames[PstDiscountType.FLD_DISCOUNT_TYPE_ID], discountType.getOID());
                object.put(PstDiscountType.fieldNames[PstDiscountType.FLD_CODE], discountType.getCode());
                object.put(PstDiscountType.fieldNames[PstDiscountType.FLD_NAME], discountType.getName());
            }catch(Exception exc){}

            return object;
        }
}
