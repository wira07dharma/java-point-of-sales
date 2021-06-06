/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.anggota;

import com.dimata.qdep.db.DBException;
import com.dimata.qdep.db.DBHandler;
import com.dimata.qdep.db.DBResultSet;
import com.dimata.qdep.db.I_DBInterface;
import com.dimata.qdep.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.util.lang.I_Language;
import com.mysql.jdbc.ResultSet;
import java.util.Vector;

/**
 *
 * @author HaddyPuutraa
 */
public class PstAnggotaEducation extends DBHandler implements I_Language, I_DBType, I_DBInterface {

    public static final String TBL_ANGGOTA_EDUCATION_NAME = "aiso_anggota_edu";

    public static final int FLD_ANGGOTA_ID = 0;
    public static final int FLD_EDUCATION_ID = 1;
    public static final int FLD_EDUCATION_DETAIL = 2;

    public static String[] fieldNames = {
        "ANGGOTA_ID",
        "EDUCATION_ID",
        "EDUCATION_DETAIL"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_FK,
        TYPE_LONG + TYPE_PK + TYPE_FK,
        TYPE_STRING
    };

    public int index;

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_ANGGOTA_EDUCATION_NAME;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstAnggotaEducation().getClass().getName();
    }

    public PstAnggotaEducation() {
    }

    public PstAnggotaEducation(int i) throws DBException {
        super(new PstEducation());
    }

    public PstAnggotaEducation(String sOid) throws DBException {
        super(new PstAnggotaEducation(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstAnggotaEducation(long lOid) throws DBException {
        super(new PstAnggotaEducation(0));
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

    public PstAnggotaEducation(long anggotaOid, long educationOid) throws DBException {
        super(new PstAnggotaEducation(0));

        if (!locate(anggotaOid, educationOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public static AnggotaEducation fetchExc(long anggotaId, long educationId) throws DBException {
        try {
            AnggotaEducation anggotaEducation = new AnggotaEducation();
            PstAnggotaEducation pstAnggotaEducation = new PstAnggotaEducation(anggotaId, educationId);
            anggotaEducation.setAnggotaId(anggotaId);
            anggotaEducation.setEducationId(educationId);

            anggotaEducation.setEducationDetail(pstAnggotaEducation.getString(FLD_EDUCATION_DETAIL));

            return anggotaEducation;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAnggotaEducation(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity ent) throws Exception {
        AnggotaEducation anggotaEducation = fetchExc(ent.getOID(0), ent.getOID(1));
        ent = (Entity) anggotaEducation;
        return anggotaEducation.getOID();
    }

    public static long insertExc(AnggotaEducation entObj) throws DBException {
        try {
            PstAnggotaEducation pstObj = new PstAnggotaEducation(0);

            pstObj.setLong(FLD_ANGGOTA_ID, entObj.getAnggotaId());
            pstObj.setLong(FLD_EDUCATION_ID, entObj.getEducationId());
            pstObj.setString(FLD_EDUCATION_DETAIL, entObj.getEducationDetail());

            pstObj.insert();
            return 0;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAnggotaEducation(0), DBException.UNKNOWN);
        }
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((AnggotaEducation) ent);
    }

    public static long updateExc(AnggotaEducation entObj, long oldEducationId) throws DBException {
        if (entObj != null && entObj.getAnggotaId() != 0) {
            try {
                PstAnggotaEducation pstObj = new PstAnggotaEducation(entObj.getAnggotaId(), oldEducationId);
                pstObj.setLong(FLD_EDUCATION_ID, entObj.getEducationId());
                pstObj.setString(FLD_EDUCATION_DETAIL, entObj.getEducationDetail());

                pstObj.update();
                return entObj.getAnggotaId();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return oldEducationId;
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((AnggotaEducation) ent);
    }

    public static long deleteExc(long educationOid, long anggotaOid) throws DBException {
        try {
            PstAnggotaEducation pstAnggotaEducation = new PstAnggotaEducation(educationOid, anggotaOid);

            pstAnggotaEducation.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEducation(0), DBException.UNKNOWN);
        }
        return anggotaOid;
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID(), ent.getOID());
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstAnggotaEducation.fieldNames[PstAnggotaEducation.FLD_EDUCATION_ID] + ") " + " FROM " + TBL_ANGGOTA_EDUCATION_NAME;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            //System.out.println(sql);
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

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_ANGGOTA_EDUCATION_NAME;

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
                AnggotaEducation anggotaEducation = new AnggotaEducation();
                resultToObject(rs, anggotaEducation);
                lists.add(anggotaEducation);
            }
            return lists;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    private static void resultToObject(ResultSet rs, AnggotaEducation anggotaEducation) {
        try {
            anggotaEducation.setAnggotaId(rs.getLong(PstAnggotaEducation.fieldNames[PstAnggotaEducation.FLD_ANGGOTA_ID]));
            anggotaEducation.setEducationId(rs.getLong(PstAnggotaEducation.fieldNames[PstAnggotaEducation.FLD_EDUCATION_ID]));
            anggotaEducation.setEducationDetail(rs.getString(PstAnggotaEducation.fieldNames[PstAnggotaEducation.FLD_EDUCATION_DETAIL]));

        } catch (Exception e) {
        }
    }

    //Opsional khusus untuk tabel Anggota_Edu
    /*
     * Update tabel angota_edu
     * menerima parameter Entity anggotaEducation dan educationId yang lama kemudian return integer
     */
    public static int updateAnggotaEducation(AnggotaEducation objEntity, long oldEducationId) throws DBException {
        int upd = 0;
        try {
            if (objEntity.getAnggotaId() != 0 && oldEducationId != 0) {
                String sql = "UPDATE " + TBL_ANGGOTA_EDUCATION_NAME + " SET "
                        + fieldNames[FLD_EDUCATION_ID] + " = " + objEntity.getEducationId() + " , "
                        + fieldNames[FLD_EDUCATION_DETAIL] + " = '" + objEntity.getEducationDetail() + "' "
                        + " WHERE " + fieldNames[FLD_ANGGOTA_ID] + " = " + objEntity.getAnggotaId() + " AND "
                        + " " + fieldNames[FLD_EDUCATION_ID] + " = " + oldEducationId;

                upd = DBHandler.execUpdate(sql);
                return upd;
            }
        } catch (DBException dbe) {
            throw dbe;
        }
        return upd;
    }

    public static int insertAnggotaEducation(AnggotaEducation objEntity) throws DBException {
        int ins = 0;
        try {
            if (objEntity.getAnggotaId() != 0 && objEntity.getEducationId() != 0) {
                String sql = "INSERT INTO " + TBL_ANGGOTA_EDUCATION_NAME + " (";
                for (int i = 0; i < fieldNames.length; i++) {
                    if (i == fieldNames.length - 1) {
                        sql = sql + fieldNames[i] + " ) VALUES ( "
                                + objEntity.getAnggotaId() + " , "
                                + objEntity.getEducationId() + " , '"
                                + objEntity.getEducationDetail() + "' ) ";
                    } else {
                        sql = sql + fieldNames[i] + " , ";
                    }
                }
                ins = DBHandler.execSqlInsert(sql);
                return ins;
            }
        } catch (DBException dbe) {
            throw dbe;
        }
        return ins;
    }

    public static int deleteAnggotaEducation(AnggotaEducation objEntity) throws DBException {
        int del = 0;
        try {
            if (objEntity.getAnggotaId() != 0 && objEntity.getEducationId() != 0) {
                String sql = "DELETE FROM " + TBL_ANGGOTA_EDUCATION_NAME
                        + " WHERE " + fieldNames[FLD_ANGGOTA_ID] + " = " + objEntity.getAnggotaId() + " AND "
                        + " " + fieldNames[FLD_EDUCATION_ID] + " = " + objEntity.getEducationId();

                del = DBHandler.execUpdate(sql);
                return del;
            }
        } catch (DBException dbe) {
            throw dbe;
        }
        return del;
    }

}
