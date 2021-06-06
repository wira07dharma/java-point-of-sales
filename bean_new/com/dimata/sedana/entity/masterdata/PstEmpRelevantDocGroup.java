/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.masterdata;

import com.dimata.qdep.db.DBException;
import com.dimata.qdep.db.DBHandler;
import com.dimata.qdep.db.DBResultSet;
import com.dimata.qdep.db.I_DBInterface;
import com.dimata.qdep.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author dedy_blinda
 */
public class PstEmpRelevantDocGroup extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    public static final String TBL_EMP_RELVT_DOC_GRP = "hr_emp_relvnt_doc_group";
    public static final int FLD_EMP_RELVT_DOC_GRP_ID = 0;
    public static final int FLD_DOC_GROUP = 1;
    public static final int FLD_DOC_GROUP_DESC = 2;
    
    /* Dedy - 20160219 */
   /* public static final int FLD_SYSTEM_NAME = 3;
    public static final int FLD_MODUL = 4;
    public static final int FLD_DOC_GROUP_TIPE = 5;*/
           
    public static String[] fieldNames = {
        "EMP_RELVT_DOC_GRP_ID",
        "DOC_GROUP",
        "DOC_GROUP_DESC",
       /* "SYSTEM_NAME",
        "MODUL",
        "DOC_GROUP_TIPE"*/
    };
    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
       /* TYPE_INT,
        TYPE_INT,
        TYPE_INT*/
    };

    public PstEmpRelevantDocGroup() {
    }

    public PstEmpRelevantDocGroup(int i) throws DBException {
        super(new PstEmpRelevantDocGroup());
    }

    public PstEmpRelevantDocGroup(String sOid) throws DBException {
        super(new PstEmpRelevantDocGroup(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstEmpRelevantDocGroup(long lOid) throws DBException {
        super(new PstEmpRelevantDocGroup(0));
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
        return TBL_EMP_RELVT_DOC_GRP;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstEmpRelevantDocGroup().getClass().getName();
    }

    public static EmpRelevantDocGroup fetchExc(long oid) throws DBException {
        try {
            EmpRelevantDocGroup entEmpRelvtDocGrp = new EmpRelevantDocGroup();
            PstEmpRelevantDocGroup pstEmpRelvtDocGrp = new PstEmpRelevantDocGroup(oid);
            entEmpRelvtDocGrp.setOID(oid);
            entEmpRelvtDocGrp.setDocGroup(pstEmpRelvtDocGrp.getString(FLD_DOC_GROUP));
            entEmpRelvtDocGrp.setDocGroupDesc(pstEmpRelvtDocGrp.getString(FLD_DOC_GROUP_DESC));
            /*entEmpRelvtDocGrp.setSystemName(pstEmpRelvtDocGrp.getInt(FLD_SYSTEM_NAME));
            entEmpRelvtDocGrp.setModul(pstEmpRelvtDocGrp.getInt(FLD_MODUL));
            entEmpRelvtDocGrp.setDocGroupTipe(pstEmpRelvtDocGrp.getInt(FLD_DOC_GROUP_TIPE));*/
            return entEmpRelvtDocGrp;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmpRelevantDocGroup(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        EmpRelevantDocGroup entEmpRelvtDocGrp = fetchExc(entity.getOID());
        entity = (Entity) entEmpRelvtDocGrp;
        return entEmpRelvtDocGrp.getOID();
    }

    public static synchronized long updateExc(EmpRelevantDocGroup entEmpRelvtDocGrp) throws DBException {
        try {
            if (entEmpRelvtDocGrp.getOID() != 0) {
                PstEmpRelevantDocGroup pstEmpRelvtDocGrp = new PstEmpRelevantDocGroup(entEmpRelvtDocGrp.getOID());
                pstEmpRelvtDocGrp.setString(FLD_DOC_GROUP, entEmpRelvtDocGrp.getDocGroup());
                pstEmpRelvtDocGrp.setString(FLD_DOC_GROUP_DESC, entEmpRelvtDocGrp.getDocGroupDesc());
               /* pstEmpRelvtDocGrp.setInt(FLD_SYSTEM_NAME, entEmpRelvtDocGrp.getSystemName());
                pstEmpRelvtDocGrp.setInt(FLD_MODUL, entEmpRelvtDocGrp.getModul());
                pstEmpRelvtDocGrp.setInt(FLD_DOC_GROUP_TIPE, entEmpRelvtDocGrp.getDocGroupTipe());*/
                pstEmpRelvtDocGrp.update();
                return entEmpRelvtDocGrp.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmpRelevantDocGroup(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((EmpRelevantDocGroup) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstEmpRelevantDocGroup pstEmpRelvtDocGrp = new PstEmpRelevantDocGroup(oid);
            pstEmpRelvtDocGrp.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmpRelevantDocGroup(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(EmpRelevantDocGroup entEmpRelvtDocGrp) throws DBException {
        try {
            PstEmpRelevantDocGroup pstEmpRelvtDocGrp = new PstEmpRelevantDocGroup(0);
            pstEmpRelvtDocGrp.setString(FLD_DOC_GROUP, entEmpRelvtDocGrp.getDocGroup());
            pstEmpRelvtDocGrp.setString(FLD_DOC_GROUP_DESC, entEmpRelvtDocGrp.getDocGroupDesc());
           /* pstEmpRelvtDocGrp.setInt(FLD_SYSTEM_NAME, entEmpRelvtDocGrp.getSystemName());
            pstEmpRelvtDocGrp.setInt(FLD_MODUL, entEmpRelvtDocGrp.getModul());
            pstEmpRelvtDocGrp.setInt(FLD_DOC_GROUP_TIPE, entEmpRelvtDocGrp.getDocGroupTipe());*/
            pstEmpRelvtDocGrp.insert();
            entEmpRelvtDocGrp.setOID(pstEmpRelvtDocGrp.getlong(FLD_EMP_RELVT_DOC_GRP_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmpRelevantDocGroup(0), DBException.UNKNOWN);
        }
        return entEmpRelvtDocGrp.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((EmpRelevantDocGroup) entity);
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_EMP_RELVT_DOC_GRP;
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
                EmpRelevantDocGroup empRelevantDocGroup = new EmpRelevantDocGroup();
                resultToObject(rs, empRelevantDocGroup);
                lists.add(empRelevantDocGroup);
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

    public static void resultToObject(ResultSet rs, EmpRelevantDocGroup entEmpRelvtDocGrp) {
        try {
            entEmpRelvtDocGrp.setOID(rs.getLong(PstEmpRelevantDocGroup.fieldNames[PstEmpRelevantDocGroup.FLD_EMP_RELVT_DOC_GRP_ID]));
            entEmpRelvtDocGrp.setDocGroup(rs.getString(PstEmpRelevantDocGroup.fieldNames[PstEmpRelevantDocGroup.FLD_DOC_GROUP]));
            entEmpRelvtDocGrp.setDocGroupDesc(rs.getString(PstEmpRelevantDocGroup.fieldNames[PstEmpRelevantDocGroup.FLD_DOC_GROUP_DESC]));
            /*entEmpRelvtDocGrp.setSystemName(rs.getInt(PstEmpRelevantDocGroup.fieldNames[PstEmpRelevantDocGroup.FLD_SYSTEM_NAME]));
            entEmpRelvtDocGrp.setModul(rs.getInt(PstEmpRelevantDocGroup.fieldNames[PstEmpRelevantDocGroup.FLD_MODUL]));
            entEmpRelvtDocGrp.setDocGroupTipe(rs.getInt(PstEmpRelevantDocGroup.fieldNames[PstEmpRelevantDocGroup.FLD_DOC_GROUP_TIPE]));*/
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long docGroupId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_EMP_RELVT_DOC_GRP + " WHERE "
                    + PstEmpRelevantDocGroup.fieldNames[PstEmpRelevantDocGroup.FLD_EMP_RELVT_DOC_GRP_ID] + " = '" + docGroupId + "'";

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
            String sql = "SELECT COUNT(" + PstEmpRelevantDocGroup.fieldNames[PstEmpRelevantDocGroup.FLD_EMP_RELVT_DOC_GRP_ID] + ") FROM " + TBL_EMP_RELVT_DOC_GRP;
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


    /* This method used to find current data */
    public static int findLimitStart(long oid, int recordToGet, String whereClause, String order) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, order);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    EmpRelevantDocGroup empRelevantDocGroup = (EmpRelevantDocGroup) list.get(ls);
                    if (oid == empRelevantDocGroup.getOID()) {
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
}
