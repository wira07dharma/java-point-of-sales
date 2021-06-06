/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.entity.location;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import com.mysql.jdbc.ResultSet;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author Acer
 */
public class PstKecamatan extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    public static final String TBL_HR_KECAMATAN = "hr_kecamatan";
    public static final String TBL_HR_KABUPATEN = "hr_kabupaten";
    public static final String TBL_HR_PROPINSI = "hr_propinsi";
    public static final String TBL_HR_NEGARA = "hr_negara";
    
    public static final int FLD_ID_KECAMATAN = 0;
    public static final int FLD_KD_KECAMATAN = 1;
    public static final int FLD_NM_KECAMATAN = 2;
   // public static final int FLD_ID_NEGARA = 3;
    //public static final int FLD_ID_PROPINSI = 4;
    public static final int FLD_ID_KABUPATEN = 3;
    public static final String[] fieldNames = {
        "ID_KECAM",
        "KD_KECAM",
        "NAMA_KECAM",
        //"ID_NEG",
        //"ID_PROP",
        "ID_KAB"
    };
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        //TYPE_LONG,
        //TYPE_LONG,
        TYPE_LONG
    };

    public PstKecamatan() {
    }

    public PstKecamatan(int i) throws DBException {
        super(new PstKecamatan());
    }

    public PstKecamatan(String sOid) throws DBException {
        super(new PstKecamatan(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstKecamatan(long lOid) throws DBException {
        super(new PstKecamatan(0));
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
        return TBL_HR_KECAMATAN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstKecamatan().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        Kecamatan kecamatan = fetchExc(ent.getOID());
        ent = (Entity) kecamatan;
        return kecamatan.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((Kecamatan) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((Kecamatan) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static Kecamatan fetchExc(long oid) throws DBException {
        try {
            Kecamatan kecamatan = new Kecamatan();
            PstKecamatan pstKecamatan = new PstKecamatan(oid);
            kecamatan.setOID(oid);

            kecamatan.setKdKecamatan(pstKecamatan.getString(FLD_KD_KECAMATAN));
            kecamatan.setNmKecamatan(pstKecamatan.getString(FLD_NM_KECAMATAN));
            //kecamatan.setIdNegara(pstKecamatan.getlong(FLD_ID_NEGARA));
            //kecamatan.setIdPropinsi(pstKecamatan.getlong(FLD_ID_PROPINSI));
            kecamatan.setIdKabupaten(pstKecamatan.getlong(FLD_ID_KABUPATEN));
            //Provinsi prov = PstProvinsi.fetchExc(kabupaten.getIdPropinsi());
            Kabupaten kab = PstKabupaten.fetchExc(kecamatan.getIdKabupaten());
            kecamatan.setIdPropinsi(kab.getIdPropinsi());
            Provinsi prov = PstProvinsi.fetchExc(kab.getIdPropinsi());
            kab.setIdNegara(prov.getIdNegara());
            kecamatan.setIdNegara(kab.getIdNegara());



            return kecamatan;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstKecamatan(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(Kecamatan kecamatan) throws DBException {
        try {
            PstKecamatan pstKecamatan = new PstKecamatan(0);

            pstKecamatan.setString(FLD_KD_KECAMATAN, kecamatan.getKdKecamatan());
            pstKecamatan.setString(FLD_NM_KECAMATAN, kecamatan.getNmKecamatan());
            //pstKecamatan.setLong(FLD_ID_NEGARA, kecamatan.getIdNegara());
            //pstKecamatan.setLong(FLD_ID_PROPINSI, kecamatan.getIdPropinsi());
            pstKecamatan.setLong(FLD_ID_KABUPATEN, kecamatan.getIdKabupaten());

            pstKecamatan.insert();
            kecamatan.setOID(pstKecamatan.getlong(FLD_ID_KECAMATAN));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstKecamatan(0), DBException.UNKNOWN);
        }
        return kecamatan.getOID();
    }

    public static long updateExc(Kecamatan kecamatan) throws DBException {
        try {
            if (kecamatan.getOID() != 0) {
                PstKecamatan pstKecamatan = new PstKecamatan(kecamatan.getOID());

                pstKecamatan.setString(FLD_KD_KECAMATAN, kecamatan.getKdKecamatan());
                pstKecamatan.setString(FLD_NM_KECAMATAN, kecamatan.getNmKecamatan());
                //pstKecamatan.setLong(FLD_ID_NEGARA, kecamatan.getIdNegara());
                //pstKecamatan.setLong(FLD_ID_PROPINSI, kecamatan.getIdPropinsi());
                pstKecamatan.setLong(FLD_ID_KABUPATEN, kecamatan.getIdKabupaten());

                pstKecamatan.update();
                return kecamatan.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstKecamatan(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstKecamatan pstKecamatan = new PstKecamatan(oid);
            pstKecamatan.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstKecamatan(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_HR_KECAMATAN;
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
            ResultSet rs = (ResultSet) dbrs.getResultSet();
            while (rs.next()) {
                Kecamatan kecamatan = new Kecamatan();
                resultToObject(rs, kecamatan);
                lists.add(kecamatan);
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

    private static void resultToObject(ResultSet rs, Kecamatan kecamatan) {
        try {
            kecamatan.setOID(rs.getLong(PstKecamatan.fieldNames[PstKecamatan.FLD_ID_KECAMATAN]));
            kecamatan.setKdKecamatan(rs.getString(PstKecamatan.fieldNames[PstKecamatan.FLD_KD_KECAMATAN]));
            kecamatan.setNmKecamatan(rs.getString(PstKecamatan.fieldNames[PstKecamatan.FLD_NM_KECAMATAN]));
            //kecamatan.setIdNegara(rs.getLong(PstKecamatan.fieldNames[PstKecamatan.FLD_ID_NEGARA]));
            //kecamatan.setIdPropinsi(rs.getLong(PstKecamatan.fieldNames[PstKecamatan.FLD_ID_PROPINSI]));
            kecamatan.setIdKabupaten(rs.getLong(PstKecamatan.fieldNames[PstKecamatan.FLD_ID_KABUPATEN]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long idKecamatan) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_KECAMATAN + " WHERE "
                    + PstKecamatan.fieldNames[PstKecamatan.FLD_ID_KECAMATAN] + " = " + idKecamatan;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = (ResultSet) dbrs.getResultSet();

            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);

        }
        return result;
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstKecamatan.fieldNames[PstKecamatan.FLD_ID_KECAMATAN] + ") FROM " + TBL_HR_KECAMATAN;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = (ResultSet) dbrs.getResultSet();

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


    /* This method used to find current datainduk */
    public static int findLimitStart(long oid, int recordToGet, String whereClause, String order) {
        //String order = "";
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, order);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    Kecamatan kecamatan = (Kecamatan) list.get(ls);
                    if (oid == kecamatan.getOID()) {
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

    public static Hashtable getListKecamatan() {
        Hashtable lists = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_KECAMATAN;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = (ResultSet) dbrs.getResultSet();
            while (rs.next()) {
                Kecamatan pnsKecamatan = new Kecamatan();
                resultToObject(rs, pnsKecamatan);
                lists.put(pnsKecamatan.getNmKecamatan().toUpperCase(), pnsKecamatan);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Hashtable();
    }

    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + mdl;
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

    public static Vector listJoinKec(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_KECAMATAN + " kec "
                    + " INNER JOIN " + TBL_HR_KABUPATEN + " k "
                    + " ON kec." + PstKecamatan.fieldNames[PstKecamatan.FLD_ID_KABUPATEN]
                    + " = k." + PstKabupaten.fieldNames[PstKabupaten.FLD_ID_KABUPATEN]
                    + " INNER JOIN " + TBL_HR_PROPINSI + " p "
                    + " ON k." + PstKabupaten.fieldNames[PstKabupaten.FLD_ID_PROPINSI]
                    + " = p." + PstProvinsi.fieldNames[PstProvinsi.FLD_ID_PROVINSI]
                    + " INNER JOIN " + TBL_HR_NEGARA + " n "
                    + " ON p." + PstProvinsi.fieldNames[PstProvinsi.FLD_ID_NEGARA]
                    + " = n." + PstNegara.fieldNames[PstNegara.FLD_ID_NEGARA];

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
            ResultSet rs = (ResultSet) dbrs.getResultSet();
            while (rs.next()) {
                Kecamatan kecamatan = new Kecamatan();
                resultToObject(rs, kecamatan);
                lists.add(kecamatan);
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
    
}
