package com.dimata.sedana.entity.masterdata;

/**
 *
 * @author Regen
 */
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

public class PstMasterLoket extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_SEDANA_MASTER_LOKET = "sedana_master_loket";
    public static final int FLD_MASTER_LOKET_ID = 0;
    public static final int FLD_LOKET_NUMBER = 1;
    public static final int FLD_LOCATION_ID = 2;
    public static final int FLD_LOKET_NAME = 3;
    public static final int FLD_LOKET_TYPE = 4;

    public static String[] fieldNames = {
        "MASTER_LOKET_ID",
        "LOKET_NUMBER",
        "LOCATION_ID",
        "LOKET_NAME",
        "LOKET_TYPE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_INT,
        TYPE_LONG + TYPE_FK,
        TYPE_STRING,
        TYPE_INT
    };

    public static int LOKET_TYPE_OFFICE = 0;
    public static int LOKET_TYPE_FIELD = 1;

    public static String[] loketTypeTitle = {"Kantor", "Lapangan"};

    public PstMasterLoket() {
    }

    public PstMasterLoket(int i) throws DBException {
        super(new PstMasterLoket());
    }

    public PstMasterLoket(String sOid) throws DBException {
        super(new PstMasterLoket(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMasterLoket(long lOid) throws DBException {
        super(new PstMasterLoket(0));
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
        return TBL_SEDANA_MASTER_LOKET;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMasterLoket().getClass().getName();
    }

    public static MasterLoket fetchExc(long oid) throws DBException {
        try {
            MasterLoket entMasterLoket = new MasterLoket();
            PstMasterLoket pstMasterLoket = new PstMasterLoket(oid);
            entMasterLoket.setOID(oid);
            entMasterLoket.setLoketNumber(pstMasterLoket.getInt(FLD_LOKET_NUMBER));
            entMasterLoket.setLocationId(pstMasterLoket.getlong(FLD_LOCATION_ID));
            entMasterLoket.setLoketName(pstMasterLoket.getString(FLD_LOKET_NAME));
            entMasterLoket.setLoketType(pstMasterLoket.getInt(FLD_LOKET_TYPE));
            return entMasterLoket;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMasterLoket(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MasterLoket entMasterLoket = fetchExc(entity.getOID());
        entity = (Entity) entMasterLoket;
        return entMasterLoket.getOID();
    }

    public static synchronized long updateExc(MasterLoket entMasterLoket) throws DBException {
        try {
            if (entMasterLoket.getOID() != 0) {
                PstMasterLoket pstMasterLoket = new PstMasterLoket(entMasterLoket.getOID());
                pstMasterLoket.setInt(FLD_LOKET_NUMBER, entMasterLoket.getLoketNumber());
                pstMasterLoket.setLong(FLD_LOCATION_ID, entMasterLoket.getLocationId());
                pstMasterLoket.setString(FLD_LOKET_NAME, entMasterLoket.getLoketName());
                pstMasterLoket.setInt(FLD_LOKET_TYPE, entMasterLoket.getLoketType());
                pstMasterLoket.update();
                return entMasterLoket.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMasterLoket(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MasterLoket) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMasterLoket pstMasterLoket = new PstMasterLoket(oid);
            pstMasterLoket.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMasterLoket(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MasterLoket entMasterLoket) throws DBException {
        try {
            PstMasterLoket pstMasterLoket = new PstMasterLoket(0);
            pstMasterLoket.setInt(FLD_LOKET_NUMBER, entMasterLoket.getLoketNumber());
            pstMasterLoket.setLong(FLD_LOCATION_ID, entMasterLoket.getLocationId());
            pstMasterLoket.setString(FLD_LOKET_NAME, entMasterLoket.getLoketName());
            pstMasterLoket.setInt(FLD_LOKET_TYPE, entMasterLoket.getLoketType());
            pstMasterLoket.insert();
            entMasterLoket.setOID(pstMasterLoket.getlong(FLD_MASTER_LOKET_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMasterLoket(0), DBException.UNKNOWN);
        }
        return entMasterLoket.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MasterLoket) entity);
    }

    public static void resultToObject(ResultSet rs, MasterLoket entMasterLoket) {
        try {
            entMasterLoket.setOID(rs.getLong(PstMasterLoket.fieldNames[PstMasterLoket.FLD_MASTER_LOKET_ID]));
            entMasterLoket.setLoketNumber(rs.getInt(PstMasterLoket.fieldNames[PstMasterLoket.FLD_LOKET_NUMBER]));
            entMasterLoket.setLocationId(rs.getLong(PstMasterLoket.fieldNames[PstMasterLoket.FLD_LOCATION_ID]));
            entMasterLoket.setLoketName(rs.getString(PstMasterLoket.fieldNames[PstMasterLoket.FLD_LOKET_NAME]));
            entMasterLoket.setLoketType(rs.getInt(PstMasterLoket.fieldNames[PstMasterLoket.FLD_LOKET_TYPE]));
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
            String sql = "SELECT * FROM " + TBL_SEDANA_MASTER_LOKET;
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
                MasterLoket entMasterLoket = new MasterLoket();
                resultToObject(rs, entMasterLoket);
                lists.add(entMasterLoket);
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

    public static boolean checkOID(long entMasterLoketId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_SEDANA_MASTER_LOKET + " WHERE "
                    + PstMasterLoket.fieldNames[PstMasterLoket.FLD_MASTER_LOKET_ID] + " = " + entMasterLoketId;
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
            String sql = "SELECT COUNT(" + PstMasterLoket.fieldNames[PstMasterLoket.FLD_MASTER_LOKET_ID] + ") FROM " + TBL_SEDANA_MASTER_LOKET;
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
                    MasterLoket entMasterLoket = (MasterLoket) list.get(ls);
                    if (oid == entMasterLoket.getOID()) {
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
