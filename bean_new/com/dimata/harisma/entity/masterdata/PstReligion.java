/* Created on 	:  [date] [time] AM/PM
 *
 * @author  	: karya
 * @version  	: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.harisma.entity.masterdata;

import java.sql.*;
import java.util.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.entity.*;

import com.dimata.harisma.entity.employee.*;
import com.dimata.harisma.entity.recruitment.*;
import com.dimata.posbo.db.*;

public class PstReligion extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_HR_RELIGION = "hr_religion";

    public static final int FLD_RELIGION_ID = 0;
    public static final int FLD_RELIGION = 1;

    public static final String[] fieldNames = {
        "RELIGION_ID",
        "RELIGION"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING
    };

    public PstReligion() {
    }

    public PstReligion(int i) throws DBException {
        super(new PstReligion());
    }

    public PstReligion(String sOid) throws DBException {
        super(new PstReligion(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstReligion(long lOid) throws DBException {
        super(new PstReligion(0));
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
        return TBL_HR_RELIGION;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstReligion().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        Religion religion = fetchExc(ent.getOID());
        ent = (Entity) religion;
        return religion.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((Religion) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((Religion) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static Religion fetchExc(long oid) throws DBException {
        try {
            Religion religion = new Religion();
            PstReligion pstReligion = new PstReligion(oid);
            religion.setOID(oid);

            religion.setReligion(pstReligion.getString(FLD_RELIGION));

            return religion;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReligion(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(Religion religion) throws DBException {
        try {
            PstReligion pstReligion = new PstReligion(0);

            pstReligion.setString(FLD_RELIGION, religion.getReligion());

            pstReligion.insert();
            religion.setOID(pstReligion.getlong(FLD_RELIGION_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReligion(0), DBException.UNKNOWN);
        }
        return religion.getOID();
    }

    public static long updateExc(Religion religion) throws DBException {
        try {
            if (religion.getOID() != 0) {
                PstReligion pstReligion = new PstReligion(religion.getOID());

                pstReligion.setString(FLD_RELIGION, religion.getReligion());

                pstReligion.update();
                return religion.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReligion(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstReligion pstReligion = new PstReligion(oid);
            pstReligion.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReligion(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_RELIGION;
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
                Religion religion = new Religion();
                resultToObject(rs, religion);
                lists.add(religion);
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

    private static void resultToObject(ResultSet rs, Religion religion) {
        try {
            religion.setOID(rs.getLong(PstReligion.fieldNames[PstReligion.FLD_RELIGION_ID]));
            religion.setReligion(rs.getString(PstReligion.fieldNames[PstReligion.FLD_RELIGION]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long religionId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_RELIGION + " WHERE " +
                    PstReligion.fieldNames[PstReligion.FLD_RELIGION_ID] + " = " + religionId;

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
            String sql = "SELECT COUNT(" + PstReligion.fieldNames[PstReligion.FLD_RELIGION_ID] + ") FROM " + TBL_HR_RELIGION;
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
                    Religion religion = (Religion) list.get(ls);
                    if (oid == religion.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }


    public static boolean checkMaster(long oid) {
        if (PstEmployee.checkReligion(oid) || PstRecrApplication.checkReligion(oid))
            return true;
        else
            return false;
    }

    /**
     * gadnyana
     * @return
     */
    public static Hashtable getReligion(){
        Vector list = list(0,0,"","");
        Hashtable has = new Hashtable();
        if(list!=null && list.size()>0){
            for(int k=0;k<list.size();k++){
                Religion religion = (Religion)list.get(k);
                has.put(religion.getReligion().toUpperCase(),religion);
            }
        }
        return has;
    }
}
