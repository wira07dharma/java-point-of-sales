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

package com.dimata.common.entity.contact;

/* package java */

import java.sql.*;
import java.util.*;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.*;

/* package prochain */

import com.dimata.common.entity.contact.*;

public class PstContactClass extends DBHandler implements I_DBInterface,
        I_DBType, I_PersintentExc, I_Language, I_PersistentExcSynch {

    public static final String TBL_CONTACT_CLASS = "contact_class";
    public static final int FLD_CONTACT_CLASS_ID = 0;
    public static final int FLD_CLASS_NAME = 1;
    public static final int FLD_CLASS_DESCRIPTION = 2;
    public static final int FLD_CLASS_TYPE = 3;
    public static final int FLD_SERIES_NUMBER = 4;

    public static final String[] fieldNames = {
        "CONTACT_CLASS_ID",
        "CLASS_NAME",
        "CLASS_DESCRIPTION",
        "CLASS_TYPE",
        "SERIES_NUM"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_STRING
    };

    public static final int CONTACT_TYPE_TRAVEL_AGENT = 0;
    public static final int CONTACT_TYPE_SUPPLIER = 1;
    public static final int CONTACT_TYPE_GUIDE = 2;
    public static final int CONTACT_TYPE_COMPANY = 3;
    public static final int CONTACT_TYPE_EMPLOYEE = 4;
    public static final int FLD_CLASS_SHIPPER = 5;
    public static final int FLD_CLASS_CLIENT = 6;
    public static final int CONTACT_TYPE_MEMBER = 7;
    public static final int FLD_CLASS_VENDOR = 8;
    public static final int FLD_CLASS_OTHERS = 9;
    public static final int CONTACT_TYPE_CUSTOMER = 10;
    public static final int CONTACT_TYPE_WAITING_LIST = 11;

    public static final String[] contactType = {
        "Travel Agent",//0
        "Supplier",//1
        "Guide",//2
        "Company",//3
        "Employee",//4
        "Shipper",//5
        "Client",//6
        "Member",//7
        "Vendor",//8
        "Others",//9
        "Customer F",//10
        "Waiting List"//11
    };


    public static final String[] fieldClassType = {
        "Travel Agent",
        "Supplier",
        "Guide",
        "Company",
        "Employee",
        "Shipper",
        "Client",
        "Member",
        "Vendor",
        "Others",
        "Customer",
        "Waiting List"
    };

    public PstContactClass() {
    }

    public PstContactClass(int i) throws DBException {
        super(new PstContactClass());
    }

    public PstContactClass(String sOid) throws DBException {
        super(new PstContactClass(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstContactClass(long lOid) throws DBException {
        super(new PstContactClass(0));
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
        return TBL_CONTACT_CLASS;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstContactClass().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        ContactClass contactclass = fetchExc(ent.getOID());
        ent = (Entity) contactclass;
        return contactclass.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((ContactClass) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((ContactClass) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static ContactClass fetchExc(long oid) throws DBException {
        try {
            ContactClass contactclass = new ContactClass();
            PstContactClass pstContactClass = new PstContactClass(oid);
            contactclass.setOID(oid);

            contactclass.setClassName(pstContactClass.getString(FLD_CLASS_NAME));
            contactclass.setClassDescription(pstContactClass.getString(FLD_CLASS_DESCRIPTION));
            contactclass.setClassType(pstContactClass.getInt(FLD_CLASS_TYPE));
            contactclass.setSeriesNumber(pstContactClass.getString(FLD_SERIES_NUMBER));

            return contactclass;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstContactClass(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(ContactClass contactclass) throws DBException {
        try {
            PstContactClass pstContactClass = new PstContactClass(0);

            pstContactClass.setString(FLD_CLASS_NAME, contactclass.getClassName());
            pstContactClass.setString(FLD_CLASS_DESCRIPTION, contactclass.getClassDescription());
            pstContactClass.setInt(FLD_CLASS_TYPE, contactclass.getClassType());
            pstContactClass.setString(FLD_SERIES_NUMBER, contactclass.getSeriesNumber());

            pstContactClass.insert();
            contactclass.setOID(pstContactClass.getlong(FLD_CONTACT_CLASS_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstContactClass(0), DBException.UNKNOWN);
        }
        return contactclass.getOID();
    }


    /*** 3 function for data synchronization ***/
    public long insertExcSynch(Entity ent) throws Exception {
        return insertExcSynch((ContactClass) ent);
    }

    public static long insertExcSynch(ContactClass contactclass) throws DBException {
        long newOID = 0;
        long originalOID = contactclass.getOID();
        try {
            newOID = insertExc(contactclass);
            if (newOID != 0) {  // sukses insert ?
                updateSynchOID(newOID, originalOID);
                return originalOID;
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstContactClass(0), DBException.UNKNOWN);
        }
        return 0;
    }


    /**
     * update oid lama dengan oid baru
     * @param newOID
     * @param originalOID
     * @throws DBException
     * @return
     */
    public static long updateSynchOID(long newOID, long originalOID) throws DBException {
        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE " + TBL_CONTACT_CLASS +
                    " SET " + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
                    " = " + originalOID +
                    " WHERE " + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
                    " = " + newOID;

            DBHandler.execUpdate(sql);
            return originalOID;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }


    public static long updateExc(ContactClass contactclass) throws DBException {
        try {
            if (contactclass.getOID() != 0) {
                PstContactClass pstContactClass = new PstContactClass(contactclass.getOID());

                pstContactClass.setString(FLD_CLASS_NAME, contactclass.getClassName());
                pstContactClass.setString(FLD_CLASS_DESCRIPTION, contactclass.getClassDescription());
                pstContactClass.setInt(FLD_CLASS_TYPE, contactclass.getClassType());
                pstContactClass.setString(FLD_SERIES_NUMBER, contactclass.getSeriesNumber());

                pstContactClass.update();
                return contactclass.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstContactClass(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstContactClass pstContactClass = new PstContactClass(oid);
            pstContactClass.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstContactClass(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT  * FROM " + PstContactClass.TBL_CONTACT_CLASS;
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
                    ;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ContactClass contactclass = new ContactClass();
                resultToObject(rs, contactclass);
                lists.add(contactclass);
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

    /** gadnyana
     * untuk mencari oidclass
     * berdasarkan type
     * @param type
     * @return
     */
    public static long getOidClassType(int type) {
        long oidClassType = 0;
        try {
            Vector list = list(0, 0, fieldNames[FLD_CLASS_TYPE] + "=" + type, "");
            if(list!=null && list.size()>0){
                for(int k=0;k<list.size();k++){
                    ContactClass contactClass = (ContactClass)list.get(0);
                    oidClassType = contactClass.getOID();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return oidClassType;
    }


    private static void resultToObject(ResultSet rs, ContactClass contactclass) {
        try {
            contactclass.setOID(rs.getLong(PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID]));
            contactclass.setClassName(rs.getString(PstContactClass.fieldNames[PstContactClass.FLD_CLASS_NAME]));
            contactclass.setClassDescription(rs.getString(PstContactClass.fieldNames[PstContactClass.FLD_CLASS_DESCRIPTION]));
            contactclass.setClassType(rs.getInt(PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]));
            contactclass.setSeriesNumber(rs.getString(PstContactClass.fieldNames[PstContactClass.FLD_SERIES_NUMBER]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long contactClassId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_CONTACT_CLASS + " WHERE " +
                    PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] + " = " + contactClassId;

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
            String sql = "SELECT COUNT(" + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] + ") FROM " + TBL_CONTACT_CLASS;
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
                    ContactClass contactclass = (ContactClass) list.get(ls);
                    if (oid == contactclass.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }
}
