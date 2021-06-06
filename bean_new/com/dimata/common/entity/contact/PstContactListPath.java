
package com.dimata.common.entity.contact;

/**
 *
 * @author Witar
 */

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

public class PstContactListPath extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_CONTACTLISTPATH = "contact_list_path";
    public static final int FLD_CONTACT_LIST_PATH = 0;
    public static final int FLD_CONTACT_ID = 1;
    public static final int FLD_ADDRESS = 2;
    public static final int FLD_LATITUDE = 3;
    public static final int FLD_LONGITUDE = 4;

    public static String[] fieldNames = {
       "CONTACT_LIST_PATH",
       "CONTACT_ID",
       "ADDRESS",
       "LATITUDE",
       "LONGITUDE"
    };

    public static int[] fieldTypes = {
       TYPE_LONG+TYPE_PK+TYPE_ID,
       TYPE_LONG,
       TYPE_STRING,
       TYPE_FLOAT,
       TYPE_FLOAT
    };

    public PstContactListPath() {
    }

    public PstContactListPath(int i) throws DBException {
       super(new PstContactListPath());
    }

    public PstContactListPath(String sOid) throws DBException {
        super(new PstContactListPath(0));
        if(!locate(sOid))
           throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
           return;
    }

    public PstContactListPath(long lOid) throws DBException {
        super(new PstContactListPath(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        }catch(Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_CONTACTLISTPATH;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstContactListPath().getClass().getName();
    }

    public static ContactListPath fetchExc(long oid) throws DBException {
        try {
            ContactListPath entContactListPath = new ContactListPath();
            PstContactListPath pstContactListPath = new PstContactListPath(oid);
            entContactListPath.setOID(oid);
            entContactListPath.setContactId(pstContactListPath.getlong(FLD_CONTACT_ID));
            entContactListPath.setAddress(pstContactListPath.getString(FLD_ADDRESS));
            entContactListPath.setLatitude(pstContactListPath.getdouble(FLD_LATITUDE));
            entContactListPath.setLongitude(pstContactListPath.getdouble(FLD_LONGITUDE));
           return entContactListPath;
        } catch (DBException dbe) {
           throw dbe;
        } catch (Exception e) {
           throw new DBException(new PstContactListPath(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        ContactListPath entContactListPath = fetchExc(entity.getOID());
        entity = (Entity) entContactListPath;
        return entContactListPath.getOID();
    }

    public static synchronized long updateExc(ContactListPath entContactListPath) throws DBException {
        try {
            if (entContactListPath.getOID() != 0) {
                PstContactListPath pstContactListPath = new PstContactListPath(entContactListPath.getOID());
                pstContactListPath.setLong(FLD_CONTACT_ID, entContactListPath.getContactId());
                pstContactListPath.setString(FLD_ADDRESS, entContactListPath.getAddress());
                pstContactListPath.setDouble(FLD_LATITUDE, entContactListPath.getLatitude());
                pstContactListPath.setDouble(FLD_LONGITUDE, entContactListPath.getLongitude());
                pstContactListPath.update();
                return entContactListPath.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstContactListPath(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((ContactListPath) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstContactListPath pstContactListPath = new PstContactListPath(oid);
            pstContactListPath.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstContactListPath(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {   
            throw new DBException(this, DBException.RECORD_NOT_FOUND);      
        }   
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(ContactListPath entContactListPath) 
    throws DBException {
        try {
            PstContactListPath pstContactListPath = new PstContactListPath(0);
            pstContactListPath.setLong(FLD_CONTACT_ID, entContactListPath.getContactId());
            pstContactListPath.setString(FLD_ADDRESS, entContactListPath.getAddress());
            pstContactListPath.setDouble(FLD_LATITUDE, entContactListPath.getLatitude());
            pstContactListPath.setDouble(FLD_LONGITUDE, entContactListPath.getLongitude());
            pstContactListPath.insert();
            entContactListPath.setOID(pstContactListPath.getLong(FLD_CONTACT_LIST_PATH));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstContactListPath(0), DBException.UNKNOWN);
        }
        return entContactListPath.getOID();
    }
    public long insertExc(Entity entity) throws Exception {
        return insertExc((ContactListPath) entity);
    }
    public static void resultToObject(ResultSet rs, ContactListPath entContactListPath) {
        try {
            entContactListPath.setOID(rs.getLong(PstContactListPath.fieldNames[PstContactListPath.FLD_CONTACT_LIST_PATH]));
            entContactListPath.setContactId(rs.getLong(PstContactListPath.fieldNames[PstContactListPath.FLD_CONTACT_ID]));
            entContactListPath.setAddress(rs.getString(PstContactListPath.fieldNames[PstContactListPath.FLD_ADDRESS]));
            entContactListPath.setLatitude(rs.getDouble(PstContactListPath.fieldNames[PstContactListPath.FLD_LATITUDE]));
            entContactListPath.setLongitude(rs.getDouble(PstContactListPath.fieldNames[PstContactListPath.FLD_LONGITUDE]));
        } catch (Exception e) {
        }
    }
    
    public static Vector listAll() {return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_CONTACTLISTPATH;
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
                ContactListPath entContactListPath = new ContactListPath();
                resultToObject(rs, entContactListPath);
                lists.add(entContactListPath);
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

    public static boolean checkOID(long entContactListPathId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_CONTACTLISTPATH + " WHERE "
            + PstContactListPath.fieldNames[PstContactListPath.FLD_CONTACT_LIST_PATH] + " = " + entContactListPathId;
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
            String sql = "SELECT COUNT(" + PstContactListPath.fieldNames[PstContactListPath.FLD_CONTACT_LIST_PATH] + ") FROM " + TBL_CONTACTLISTPATH;
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
                    ContactListPath entContactListPath = (ContactListPath) list.get(ls);
                    if (oid == entContactListPath.getOID()) {
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
