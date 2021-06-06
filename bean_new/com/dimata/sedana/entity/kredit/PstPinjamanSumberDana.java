/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.kredit;

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

public class PstPinjamanSumberDana extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_PINJAMANSUMBERDANA = "sedana_pinjaman_sumber_dana";
    public static final int FLD_AISO_PINJAMAN_SUMBER_DANA_ID = 0;
    public static final int FLD_PINJAMAN_ID = 1;
    public static final int FLD_SUMBER_DANA_ID = 2;
    public static final int FLD_JUMLAH_DANA = 3;

    public static String[] fieldNames = {
        "AISO_PINJAMAN_SUMBER_DANA_ID",
        "PINJAMAN_ID",
        "SUMBER_DANA_ID",
        "JUMLAH_DANA"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT
    };

    public PstPinjamanSumberDana() {
    }

    public PstPinjamanSumberDana(int i) throws DBException {
        super(new PstPinjamanSumberDana());
    }

    public PstPinjamanSumberDana(String sOid) throws DBException {
        super(new PstPinjamanSumberDana(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstPinjamanSumberDana(long lOid) throws DBException {
        super(new PstPinjamanSumberDana(0));
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
        return TBL_PINJAMANSUMBERDANA;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPinjamanSumberDana().getClass().getName();
    }

    public static PinjamanSumberDana fetchExc(long oid) throws DBException {
        try {
            PinjamanSumberDana entPinjamanSumberDana = new PinjamanSumberDana();
            PstPinjamanSumberDana pstPinjamanSumberDana = new PstPinjamanSumberDana(oid);
            entPinjamanSumberDana.setOID(oid);
            entPinjamanSumberDana.setPinjamanId(pstPinjamanSumberDana.getlong(FLD_PINJAMAN_ID));
            entPinjamanSumberDana.setSumberDanaId(pstPinjamanSumberDana.getlong(FLD_SUMBER_DANA_ID));
            entPinjamanSumberDana.setJumlahDana(pstPinjamanSumberDana.getdouble(FLD_JUMLAH_DANA));
            return entPinjamanSumberDana;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPinjamanSumberDana(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        PinjamanSumberDana entPinjamanSumberDana = fetchExc(entity.getOID());
        entity = (Entity) entPinjamanSumberDana;
        return entPinjamanSumberDana.getOID();
    }

    public static synchronized long updateExc(PinjamanSumberDana entPinjamanSumberDana) throws DBException {
        try {
            if (entPinjamanSumberDana.getOID() != 0) {
                PstPinjamanSumberDana pstPinjamanSumberDana = new PstPinjamanSumberDana(entPinjamanSumberDana.getOID());
                pstPinjamanSumberDana.setLong(FLD_PINJAMAN_ID, entPinjamanSumberDana.getPinjamanId());
                pstPinjamanSumberDana.setLong(FLD_SUMBER_DANA_ID, entPinjamanSumberDana.getSumberDanaId());
                pstPinjamanSumberDana.setDouble(FLD_JUMLAH_DANA, entPinjamanSumberDana.getJumlahDana());
                pstPinjamanSumberDana.update();
                return entPinjamanSumberDana.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPinjamanSumberDana(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((PinjamanSumberDana) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstPinjamanSumberDana pstPinjamanSumberDana = new PstPinjamanSumberDana(oid);
            pstPinjamanSumberDana.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPinjamanSumberDana(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(PinjamanSumberDana entPinjamanSumberDana) throws DBException {
        try {
            PstPinjamanSumberDana pstPinjamanSumberDana = new PstPinjamanSumberDana(0);
            pstPinjamanSumberDana.setLong(FLD_PINJAMAN_ID, entPinjamanSumberDana.getPinjamanId());
            pstPinjamanSumberDana.setLong(FLD_SUMBER_DANA_ID, entPinjamanSumberDana.getSumberDanaId());
            pstPinjamanSumberDana.setDouble(FLD_JUMLAH_DANA, entPinjamanSumberDana.getJumlahDana());
            pstPinjamanSumberDana.insert();
            entPinjamanSumberDana.setOID(pstPinjamanSumberDana.getlong(FLD_AISO_PINJAMAN_SUMBER_DANA_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPinjamanSumberDana(0), DBException.UNKNOWN);
        }
        return entPinjamanSumberDana.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((PinjamanSumberDana) entity);
    }

    public static void resultToObject(ResultSet rs, PinjamanSumberDana entPinjamanSumberDana) {
        try {
            entPinjamanSumberDana.setOID(rs.getLong(PstPinjamanSumberDana.fieldNames[PstPinjamanSumberDana.FLD_AISO_PINJAMAN_SUMBER_DANA_ID]));
            entPinjamanSumberDana.setPinjamanId(rs.getLong(PstPinjamanSumberDana.fieldNames[PstPinjamanSumberDana.FLD_PINJAMAN_ID]));
            entPinjamanSumberDana.setSumberDanaId(rs.getLong(PstPinjamanSumberDana.fieldNames[PstPinjamanSumberDana.FLD_SUMBER_DANA_ID]));
            entPinjamanSumberDana.setJumlahDana(rs.getDouble(PstPinjamanSumberDana.fieldNames[PstPinjamanSumberDana.FLD_JUMLAH_DANA]));
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
            String sql = "SELECT * FROM " + TBL_PINJAMANSUMBERDANA;
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
                PinjamanSumberDana entPinjamanSumberDana = new PinjamanSumberDana();
                resultToObject(rs, entPinjamanSumberDana);
                lists.add(entPinjamanSumberDana);
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

    public static boolean checkOID(long entPinjamanSumberDanaId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_PINJAMANSUMBERDANA + " WHERE "
                    + PstPinjamanSumberDana.fieldNames[PstPinjamanSumberDana.FLD_AISO_PINJAMAN_SUMBER_DANA_ID] + " = " + entPinjamanSumberDanaId;
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
            String sql = "SELECT COUNT(" + PstPinjamanSumberDana.fieldNames[PstPinjamanSumberDana.FLD_AISO_PINJAMAN_SUMBER_DANA_ID] + ") FROM " + TBL_PINJAMANSUMBERDANA;
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
                    PinjamanSumberDana entPinjamanSumberDana = (PinjamanSumberDana) list.get(ls);
                    if (oid == entPinjamanSumberDana.getOID()) {
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
