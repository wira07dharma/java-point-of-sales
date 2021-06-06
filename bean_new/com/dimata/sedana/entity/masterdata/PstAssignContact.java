/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.masterdata;

/** 
 *
 * @author Regen
 */
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.sedana.entity.tabungan.DataTabungan;
import com.dimata.sedana.entity.tabungan.PstDataTabungan;
import com.dimata.sedana.entity.tabungan.PstTransaksi;
import com.dimata.sedana.entity.tabungan.Transaksi;
import com.dimata.util.Command;
import java.util.Vector;

public class PstAssignContact extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_ASSIGNCONTACT = "sedana_assign_contact_tabungan";
  public static final int FLD_ASSIGN_TABUNGAN_ID = 0;
  public static final int FLD_MASTER_TABUNGAN_ID = 1;
  public static final int FLD_CONTACT_ID = 2;
  public static final int FLD_NO_TABUNGAN = 3;

  public static String[] fieldNames = {
    "ASSIGN_TABUNGAN_ID",
    "MASTER_TABUNGAN_ID",
    "CONTACT_ID",
    "NO_TABUNGAN"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_LONG,
    TYPE_LONG,
    TYPE_STRING
  };

  public PstAssignContact() {
  }

  public PstAssignContact(int i) throws DBException {
    super(new PstAssignContact());
  }

  public PstAssignContact(String sOid) throws DBException {
    super(new PstAssignContact(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstAssignContact(long lOid) throws DBException {
    super(new PstAssignContact(0));
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
    return TBL_ASSIGNCONTACT;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstAssignContact().getClass().getName();
  }

  public static AssignContact fetchExc(long oid) throws DBException {
    try {
      AssignContact entAssignContact = new AssignContact();
      PstAssignContact pstAssignContact = new PstAssignContact(oid);
      entAssignContact.setOID(oid);
      entAssignContact.setMasterTabunganId(pstAssignContact.getlong(FLD_MASTER_TABUNGAN_ID));
      entAssignContact.setContactId(pstAssignContact.getlong(FLD_CONTACT_ID));
      entAssignContact.setNoTabungan(pstAssignContact.getString(FLD_NO_TABUNGAN));
      return entAssignContact;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAssignContact(0), DBException.UNKNOWN);
    }
  }

  public long fetchExc(Entity entity) throws Exception {
    AssignContact entAssignContact = fetchExc(entity.getOID());
    entity = (Entity) entAssignContact;
    return entAssignContact.getOID();
  }

  public static synchronized long updateExc(AssignContact entAssignContact) throws DBException {
    try {
      if (entAssignContact.getOID() != 0) {
        PstAssignContact pstAssignContact = new PstAssignContact(entAssignContact.getOID());
        pstAssignContact.setLong(FLD_MASTER_TABUNGAN_ID, entAssignContact.getMasterTabunganId());
        pstAssignContact.setLong(FLD_CONTACT_ID, entAssignContact.getContactId());
        pstAssignContact.setString(FLD_NO_TABUNGAN, entAssignContact.getNoTabungan());
        pstAssignContact.update();
        return entAssignContact.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAssignContact(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((AssignContact) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstAssignContact pstAssignContact = new PstAssignContact(oid);
      pstAssignContact.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAssignContact(0), DBException.UNKNOWN);
    }
    return oid;
  }
  
//  public static synchronized long deleteWithCheck(long oid) {
//    DBResultSet dbrs = null;
//    long id = -1;
//    try {
//      String sql = "SELECT COUNT(0) FROM ( SELECT t.* FROM sedana_detail_transaksi dt JOIN `sedana_transaksi` t USING (`TRANSAKSI_ID`) JOIN aiso_data_tabungan adt USING (`id_simpanan`) WHERE adt.`ASSIGN_TABUNGAN_ID`="+oid+" UNION SELECT t.* FROM `aiso_pinjaman` p LEFT JOIN sedana_transaksi t ON p.`PINJAMAN_ID`=t.`PINJAMAN_ID` JOIN `sedana_assign_contact_tabungan` c ON c.`ASSIGN_TABUNGAN_ID`=p.`ASSIGN_TABUNGAN_ID` JOIN `aiso_data_tabungan` d ON (d.`ASSIGN_TABUNGAN_ID` = c.`ASSIGN_TABUNGAN_ID` AND d.`ID_JENIS_SIMPANAN`=p.`ID_JENIS_SIMPANAN`) OR (d.`ID_SIMPANAN`=p.`WAJIB_ID_JENIS_SIMPANAN`) WHERE d.`ASSIGN_TABUNGAN_ID`="+oid+" ) t WHERE STATUS = 7";
//      dbrs = DBHandler.execQueryResult(sql);
//      ResultSet rs = dbrs.getResultSet();
//      int count = rs.next()?rs.getInt(1): 0;
//      if(count==0) {
//        String q1 = "DELETE FROM `aiso_pinjaman` WHERE (`ASSIGN_TABUNGAN_ID`, `ID_JENIS_SIMPANAN`) IN (SELECT d.`ASSIGN_TABUNGAN_ID`, d.`ID_JENIS_SIMPANAN` FROM `aiso_data_tabungan` d WHERE d.`ASSIGN_TABUNGAN_ID`="+oid+")";
//        (new DBHandler()).delete(q1);
//        Vector<DataTabungan> dts = PstDataTabungan.list(0, 0, PstDataTabungan.fieldNames[PstDataTabungan.FLD_ASSIGN_TABUNGAN_ID]+"="+oid, "");
//        for(DataTabungan d: dts) {
//          Vector<Transaksi> ts = PstTransaksi.list(0, 0, PstTransaksi.fieldNames[PstTransaksi.FLD_TRANSAKSI_ID]+" IN (SELECT `TRANSAKSI_ID` FROM `sedana_detail_transaksi` WHERE `ID_SIMPANAN`="+d.getOID()+")", "");
//          for(Transaksi t: ts) {
//            PstTransaksi.deleteExc(t.getOID());
//          }
//          PstDataTabungan.deleteExc(d.getOID());
//        }
//        deleteExc(oid);
//        id=oid;
//      }
//    } catch (DBException dbe) {
//      dbe.printStackTrace();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    
//    return id;
//  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(AssignContact entAssignContact) throws DBException {
    try {
      PstAssignContact pstAssignContact = new PstAssignContact(0);
      pstAssignContact.setLong(FLD_MASTER_TABUNGAN_ID, entAssignContact.getMasterTabunganId());
      pstAssignContact.setLong(FLD_CONTACT_ID, entAssignContact.getContactId());
      pstAssignContact.setString(FLD_NO_TABUNGAN, entAssignContact.getNoTabungan());
      pstAssignContact.insert();
      entAssignContact.setOID(pstAssignContact.getlong(FLD_ASSIGN_TABUNGAN_ID));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAssignContact(0), DBException.UNKNOWN);
    }
    return entAssignContact.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((AssignContact) entity);
  }

  public static void resultToObject(ResultSet rs, AssignContact entAssignContact) {
    try {
      entAssignContact.setOID(rs.getLong(PstAssignContact.fieldNames[PstAssignContact.FLD_ASSIGN_TABUNGAN_ID]));
      entAssignContact.setMasterTabunganId(rs.getLong(PstAssignContact.fieldNames[PstAssignContact.FLD_MASTER_TABUNGAN_ID]));
      entAssignContact.setContactId(rs.getLong(PstAssignContact.fieldNames[PstAssignContact.FLD_CONTACT_ID]));
      entAssignContact.setNoTabungan(rs.getString(PstAssignContact.fieldNames[PstAssignContact.FLD_NO_TABUNGAN]));
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
      String sql = "SELECT * FROM " + TBL_ASSIGNCONTACT;
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
        AssignContact entAssignContact = new AssignContact();
        resultToObject(rs, entAssignContact);
        lists.add(entAssignContact);
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

  public static boolean checkOID(long entAssignContactId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_ASSIGNCONTACT + " WHERE "
              + PstAssignContact.fieldNames[PstAssignContact.FLD_ASSIGN_TABUNGAN_ID] + " = " + entAssignContactId;
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
      String sql = "SELECT COUNT(" + PstAssignContact.fieldNames[PstAssignContact.FLD_ASSIGN_TABUNGAN_ID] + ") FROM " + TBL_ASSIGNCONTACT;
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
          AssignContact entAssignContact = (AssignContact) list.get(ls);
          if (oid == entAssignContact.getOID()) {
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
  
  public static String queryAssignTabungan(long oidContact) {
    return "SELECT "+fieldNames[FLD_MASTER_TABUNGAN_ID]+" FROM `"+TBL_ASSIGNCONTACT+"` WHERE `"+fieldNames[FLD_CONTACT_ID]+"`="+oidContact;
  }
  
  public static String queryTransaksiId(String noTabungan) {
    return "SELECT DISTINCT TRANSAKSI_ID FROM `sedana_assign_contact_tabungan` ct JOIN `aiso_data_tabungan` USING (`ASSIGN_TABUNGAN_ID`) JOIN `sedana_detail_transaksi` USING (`ID_SIMPANAN`) WHERE ct.`NO_TABUNGAN` = '"+noTabungan+"'";
  }
  
}
