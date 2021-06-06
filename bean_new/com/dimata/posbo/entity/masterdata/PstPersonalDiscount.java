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

/* package java */

import com.dimata.common.entity.custom.PstDataCustom;
import java.io.*
        ;
import java.sql.*
        ;
import java.util.*
        ;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import org.json.JSONObject;

public class PstPersonalDiscount extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    //public static final  String TBL_POS_PERSONAL_DISC = "POS_PERSONAL_DISC";
    public static final String TBL_POS_PERSONAL_DISC = "pos_personal_disc";

    public static final int FLD_PERSONAL_DISC_ID = 0;
    public static final int FLD_MATERIAL_ID = 1;
    public static final int FLD_PERS_DISC_VALUE = 2;
    public static final int FLD_CONTACT_ID = 3;
    public static final int FLD_PERS_DISC_PCT = 4;

    public static final String[] fieldNames = {
            "PERS_DISC_ID",
            "MATERIAL_ID",
            "PERS_DISC_VALUE",
            "CONTACT_ID",
            "PERS_DISC_PCT"
    };

    public static final int[] fieldTypes = {
            TYPE_LONG + TYPE_PK + TYPE_ID,
            TYPE_LONG,
            TYPE_FLOAT,
            TYPE_LONG,
            TYPE_FLOAT
    };

    public PstPersonalDiscount() {
    }

    public PstPersonalDiscount(int i) throws DBException {
        super(new PstPersonalDiscount());
    }

    public PstPersonalDiscount(String sOid) throws DBException {
        super(new PstPersonalDiscount(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstPersonalDiscount(long lOid) throws DBException {
        super(new PstPersonalDiscount(0));
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
        return TBL_POS_PERSONAL_DISC;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPersonalDiscount().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        PersonalDiscount personaldiscount = fetchExc(ent.getOID());
        ent = (Entity) personaldiscount;
        return personaldiscount.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((PersonalDiscount) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((PersonalDiscount) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static PersonalDiscount fetchExc(long oid) throws DBException {
        try {
            PersonalDiscount personaldiscount = new PersonalDiscount();
            PstPersonalDiscount pstPersonalDiscount = new PstPersonalDiscount(oid);
            personaldiscount.setOID(oid);

            personaldiscount.setMaterialId(pstPersonalDiscount.getlong(FLD_MATERIAL_ID));
            personaldiscount.setPersDiscVal(pstPersonalDiscount.getdouble(FLD_PERS_DISC_VALUE));
            personaldiscount.setContactId(pstPersonalDiscount.getlong(FLD_CONTACT_ID));
            personaldiscount.setPersDiscPct(pstPersonalDiscount.getdouble(FLD_PERS_DISC_PCT));

            return personaldiscount;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPersonalDiscount(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(PersonalDiscount personaldiscount) throws DBException {
        try {
            PstPersonalDiscount pstPersonalDiscount = new PstPersonalDiscount(0);

            pstPersonalDiscount.setLong(FLD_MATERIAL_ID, personaldiscount.getMaterialId());
            pstPersonalDiscount.setDouble(FLD_PERS_DISC_VALUE, personaldiscount.getPersDiscVal());
            pstPersonalDiscount.setLong(FLD_CONTACT_ID, personaldiscount.getContactId());
            pstPersonalDiscount.setDouble(FLD_PERS_DISC_PCT, personaldiscount.getPersDiscPct());

            pstPersonalDiscount.insert();

            long oidDataSync=PstDataSyncSql.insertExc(pstPersonalDiscount.getInsertSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
            personaldiscount.setOID(pstPersonalDiscount.getlong(FLD_PERSONAL_DISC_ID));
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstPersonalDiscount.getInsertSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPersonalDiscount(0), DBException.UNKNOWN);
        }
        return personaldiscount.getOID();
    }

    public static long updateExc(PersonalDiscount personaldiscount) throws DBException {
        try {
            if (personaldiscount.getOID() != 0) {
                PstPersonalDiscount pstPersonalDiscount = new PstPersonalDiscount(personaldiscount.getOID());

                pstPersonalDiscount.setLong(FLD_MATERIAL_ID, personaldiscount.getMaterialId());
                pstPersonalDiscount.setDouble(FLD_PERS_DISC_VALUE, personaldiscount.getPersDiscVal());
                pstPersonalDiscount.setLong(FLD_CONTACT_ID, personaldiscount.getContactId());
                pstPersonalDiscount.setDouble(FLD_PERS_DISC_PCT, personaldiscount.getPersDiscPct());

                pstPersonalDiscount.update();

                long oidDataSync=PstDataSyncSql.insertExc(pstPersonalDiscount.getUpdateSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstPersonalDiscount.getUpdateSQL());
                return personaldiscount.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPersonalDiscount(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstPersonalDiscount pstPersonalDiscount = new PstPersonalDiscount(oid);
            pstPersonalDiscount.delete();

            long oidDataSync = PstDataSyncSql.insertExc(pstPersonalDiscount.getDeleteSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstPersonalDiscount.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPersonalDiscount(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_PERSONAL_DISC;
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
                PersonalDiscount personaldiscount = new PersonalDiscount();
                resultToObject(rs, personaldiscount);
                lists.add(personaldiscount);
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

    public static Vector listData(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_PERSONAL_DISC+
                " INNER JOIN "+PstMaterial.TBL_MATERIAL+
                " ON "+TBL_POS_PERSONAL_DISC+"."+fieldNames[FLD_MATERIAL_ID]+"="+
                PstMaterial.TBL_MATERIAL+"."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
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
                PersonalDiscount personaldiscount = new PersonalDiscount();
                resultToObject(rs, personaldiscount);
                lists.add(personaldiscount);
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

    private static void resultToObject(ResultSet rs, PersonalDiscount personaldiscount) {
        try {
            personaldiscount.setOID(rs.getLong(PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_PERSONAL_DISC_ID]));
            personaldiscount.setMaterialId(rs.getLong(PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_MATERIAL_ID]));
            personaldiscount.setPersDiscVal(rs.getDouble(PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_PERS_DISC_VALUE]));
            personaldiscount.setPersDiscPct(rs.getDouble(PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_PERS_DISC_PCT]));
            personaldiscount.setContactId(rs.getLong(PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_CONTACT_ID]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long dailyRateId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_PERSONAL_DISC + " WHERE " +
                    PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_PERSONAL_DISC_ID] + " = " + dailyRateId;

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

    public static boolean checkOID(long materialId, long memberId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_PERSONAL_DISC + " WHERE " +
                    PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_MATERIAL_ID] + " = " + materialId +
                    " AND " + PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_CONTACT_ID] + " = " + memberId;

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

    /**
     * gadnyana
     * pengecekan sekaligus return oid per. discount
     *
     * @param materialId
     * @param memberId
     */
    public static long checkPersonalDiscount(long materialId, long memberId) {
        DBResultSet dbrs = null;
        long result = 0;
        try {
            String sql = "SELECT * FROM " + TBL_POS_PERSONAL_DISC + " WHERE " +
                    PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_MATERIAL_ID] + " = " + materialId +
                    " AND " + PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_CONTACT_ID] + " = " + memberId;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = rs.getLong(fieldNames[FLD_PERSONAL_DISC_ID]);
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
            String sql = "SELECT COUNT(" + PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_PERSONAL_DISC_ID] + ") FROM " + TBL_POS_PERSONAL_DISC;
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
                    PersonalDiscount personaldiscount = (PersonalDiscount) list.get(ls);
                    if (oid == personaldiscount.getOID())
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
                PersonalDiscount personalDiscount = PstPersonalDiscount.fetchExc(oid);
                object.put(PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_PERSONAL_DISC_ID], personalDiscount.getOID());
                object.put(PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_CONTACT_ID], personalDiscount.getContactId());
                object.put(PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_MATERIAL_ID], personalDiscount.getMaterialId());
                object.put(PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_PERS_DISC_PCT], personalDiscount.getPersDiscPct());
                object.put(PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_PERS_DISC_VALUE], personalDiscount.getPersDiscVal());
            }catch(Exception exc){}

            return object;
        }
}
