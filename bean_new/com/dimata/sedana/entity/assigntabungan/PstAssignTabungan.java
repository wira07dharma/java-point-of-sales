/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.assigntabungan;

/**
 *
 * @author Regen
 */
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import static com.dimata.sedana.entity.masterdata.PstAssignContact.fieldNames;
import com.dimata.sedana.entity.tabungan.JenisSimpanan;
import com.dimata.sedana.entity.tabungan.PstJenisSimpanan;
import static com.dimata.sedana.entity.tabungan.PstJenisSimpanan.TBL_JENISSIMPANAN;
import com.dimata.sedana.session.Tabungan;
import com.dimata.util.Command;
import java.util.Vector;

public class PstAssignTabungan extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_ASSIGNTABUNGAN = "sedana_assign_tabungan";
  public static final int FLD_ASSIGN_MASTER_TABUNGAN_ID = 0;
  public static final int FLD_MASTER_TABUNGAN = 1;
  public static final int FLD_ID_JENIS_SIMPANAN = 2;

  public static String[] fieldNames = {
    "ASSIGN_MASTER_TABUNGAN_ID",
    "MASTER_TABUNGAN",
    "ID_JENIS_SIMPANAN"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_LONG,
    TYPE_LONG
  };

  public PstAssignTabungan() {
  }

  public PstAssignTabungan(int i) throws DBException {
    super(new PstAssignTabungan());
  }

  public PstAssignTabungan(String sOid) throws DBException {
    super(new PstAssignTabungan(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstAssignTabungan(long lOid) throws DBException {
    super(new PstAssignTabungan(0));
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
    return TBL_ASSIGNTABUNGAN;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstAssignTabungan().getClass().getName();
  }

  public static AssignTabungan fetchExc(long oid) throws DBException {
    try {
      AssignTabungan entAssignTabungan = new AssignTabungan();
      PstAssignTabungan pstAssignTabungan = new PstAssignTabungan(oid);
      entAssignTabungan.setOID(oid);
      entAssignTabungan.setMasterTabungan(pstAssignTabungan.getLong(FLD_MASTER_TABUNGAN));
      entAssignTabungan.setIdJenisSimpanan(pstAssignTabungan.getLong(FLD_ID_JENIS_SIMPANAN));
      return entAssignTabungan;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAssignTabungan(0), DBException.UNKNOWN);
    }
  }

  public long fetchExc(Entity entity) throws Exception {
    AssignTabungan entAssignTabungan = fetchExc(entity.getOID());
    entity = (Entity) entAssignTabungan;
    return entAssignTabungan.getOID();
  }

  public static synchronized long updateExc(AssignTabungan entAssignTabungan) throws DBException {
    try {
      if (entAssignTabungan.getOID() != 0) {
        PstAssignTabungan pstAssignTabungan = new PstAssignTabungan(entAssignTabungan.getOID());
        pstAssignTabungan.setLong(FLD_MASTER_TABUNGAN, entAssignTabungan.getMasterTabungan());
        pstAssignTabungan.setLong(FLD_ID_JENIS_SIMPANAN, entAssignTabungan.getIdJenisSimpanan());
        pstAssignTabungan.update();
        return entAssignTabungan.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAssignTabungan(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((AssignTabungan) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstAssignTabungan pstAssignTabungan = new PstAssignTabungan(oid);
      pstAssignTabungan.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAssignTabungan(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(AssignTabungan entAssignTabungan) throws DBException {
    try {
      PstAssignTabungan pstAssignTabungan = new PstAssignTabungan(0);
      pstAssignTabungan.setLong(FLD_MASTER_TABUNGAN, entAssignTabungan.getMasterTabungan());
      pstAssignTabungan.setLong(FLD_ID_JENIS_SIMPANAN, entAssignTabungan.getIdJenisSimpanan());
      pstAssignTabungan.insert();
      entAssignTabungan.setOID(pstAssignTabungan.getlong(FLD_ASSIGN_MASTER_TABUNGAN_ID));
    } catch (DBException dbe) {
      throw dbe;
    }
    return entAssignTabungan.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((AssignTabungan) entity);
  }

  public static void resultToObject(ResultSet rs, AssignTabungan entAssignTabungan) {
    try {
      entAssignTabungan.setOID(rs.getLong(PstAssignTabungan.fieldNames[PstAssignTabungan.FLD_ASSIGN_MASTER_TABUNGAN_ID]));
      entAssignTabungan.setMasterTabungan(rs.getLong(PstAssignTabungan.fieldNames[PstAssignTabungan.FLD_MASTER_TABUNGAN]));
      entAssignTabungan.setIdJenisSimpanan(rs.getLong(PstAssignTabungan.fieldNames[PstAssignTabungan.FLD_ID_JENIS_SIMPANAN]));
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
      String sql = "SELECT * FROM " + TBL_ASSIGNTABUNGAN;
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
        AssignTabungan entAssignTabungan = new AssignTabungan();
        resultToObject(rs, entAssignTabungan);
        lists.add(entAssignTabungan);
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
  
  public static Vector listJenisSimpanan(int limitStart, int recordToGet, String whereClause, String order) {
    Vector lists = new Vector();
    DBResultSet dbrs = null;
    try {
      String sql = "SELECT "+PstAssignTabungan.fieldNames[PstAssignTabungan.FLD_ASSIGN_MASTER_TABUNGAN_ID]+" AS ID_JENIS_SIMPANAN	, NAMA_SIMPANAN	, SETORAN_MIN	, BUNGA	, PROVISI	, BIAYA_ADMIN	, DESC_JENIS_SIMPANAN	, FREKUENSI_SIMPANAN	, FREKUENSI_PENARIKAN	, BASIS_HARI_BUNGA	 FROM " + TBL_JENISSIMPANAN + " " +
                   "JOIN "+PstAssignTabungan.fieldNames[PstAssignTabungan.FLD_ID_JENIS_SIMPANAN]+" USING("+fieldNames[FLD_ID_JENIS_SIMPANAN]+") "
      ;
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
        JenisSimpanan entJenisSimpanan = new JenisSimpanan();
        PstJenisSimpanan.resultToObject(rs, entJenisSimpanan);
        lists.add(entJenisSimpanan);
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
  
  public static boolean checkOID(long entAssignTabunganId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_ASSIGNTABUNGAN + " WHERE "
              + PstAssignTabungan.fieldNames[PstAssignTabungan.FLD_ASSIGN_MASTER_TABUNGAN_ID] + " = " + entAssignTabunganId;
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
      String sql = "SELECT COUNT(" + PstAssignTabungan.fieldNames[PstAssignTabungan.FLD_ASSIGN_MASTER_TABUNGAN_ID] + ") FROM " + TBL_ASSIGNTABUNGAN;
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
          AssignTabungan entAssignTabungan = (AssignTabungan) list.get(ls);
          if (oid == entAssignTabungan.getOID()) {
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
  
  public static String getIdJenisSimpananByTabungan(Tabungan tabungan) {
    String q =  "SELECT DISTINCT `ID_JENIS_SIMPANAN` "+
                "FROM `sedana_assign_contact_tabungan` "+
                "JOIN `sedana_master_tabungan` USING(`MASTER_TABUNGAN_ID`) " +
                "JOIN `sedana_assign_tabungan` ON `MASTER_TABUNGAN`=`MASTER_TABUNGAN_ID` "+
                "WHERE `CONTACT_ID` = "+tabungan.getOID()+" "+
                "AND `sedana_assign_contact_tabungan`.NO_TABUNGAN='"+tabungan.getNoTabungan()+"'"
            ;
    
    return q;
  }
  
}
