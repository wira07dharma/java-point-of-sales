/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author Regen
 */
public class PstKolektibilitasPembayaranDetails extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_KOLEKTIBILITASPEMBAYARANDETAILS = "sedana_kolektibilitas_pembayaran_details";
  public static final int FLD_KOLEKTIBILITASDETAILID = 0;
  public static final int FLD_KOLEKTIBILITASID = 1;
  public static final int FLD_TIPEKREIDT = 2;
  public static final int FLD_MAXHARITUNGGAKANPOKOK = 3;
  public static final int FLD_MAXHARIJUMLAHTUNGGAKANBUNGA = 4;

  public static String[] fieldNames = {
    "KOLEKTIBILITAS_DETAIL_ID",
    "KOLEKTIBILITAS_ID",
    "TIPE_KREDIT",
    "MAX_HARI_TUNGGAKAN_POKOK",
    "MAX_HARI_JUMLAH_TUNGGAKAN_BUNGA"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_LONG,
    TYPE_INT,
    TYPE_INT,
    TYPE_INT
  };

  public PstKolektibilitasPembayaranDetails() {
  }

  public PstKolektibilitasPembayaranDetails(int i) throws DBException {
    super(new PstKolektibilitasPembayaranDetails());
  }

  public PstKolektibilitasPembayaranDetails(String sOid) throws DBException {
    super(new PstKolektibilitasPembayaranDetails(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstKolektibilitasPembayaranDetails(long lOid) throws DBException {
    super(new PstKolektibilitasPembayaranDetails(0));
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
    return TBL_KOLEKTIBILITASPEMBAYARANDETAILS;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstKolektibilitasPembayaranDetails().getClass().getName();
  }

  public static KolektibilitasPembayaranDetails fetchExc(long oid) throws DBException {
    try {
      KolektibilitasPembayaranDetails entKolektibilitasPembayaranDetails = new KolektibilitasPembayaranDetails();
      PstKolektibilitasPembayaranDetails pstKolektibilitasPembayaranDetails = new PstKolektibilitasPembayaranDetails(oid);
      entKolektibilitasPembayaranDetails.setOID(oid);
      entKolektibilitasPembayaranDetails.setKolektibilitasId(pstKolektibilitasPembayaranDetails.getlong(FLD_KOLEKTIBILITASID));
      entKolektibilitasPembayaranDetails.setTipeKreidt(pstKolektibilitasPembayaranDetails.getInt(FLD_TIPEKREIDT));
      entKolektibilitasPembayaranDetails.setMaxHariTunggakanPokok(pstKolektibilitasPembayaranDetails.getInt(FLD_MAXHARITUNGGAKANPOKOK));
      entKolektibilitasPembayaranDetails.setMaxHariJumlahTunggakanBunga(pstKolektibilitasPembayaranDetails.getInt(FLD_MAXHARIJUMLAHTUNGGAKANBUNGA));
      return entKolektibilitasPembayaranDetails;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstKolektibilitasPembayaranDetails(0), DBException.UNKNOWN);
    }
  }

  public long fetchExc(Entity entity) throws Exception {
    KolektibilitasPembayaranDetails entKolektibilitasPembayaranDetails = fetchExc(entity.getOID());
    entity = (Entity) entKolektibilitasPembayaranDetails;
    return entKolektibilitasPembayaranDetails.getOID();
  }

  public static synchronized long updateExc(KolektibilitasPembayaranDetails entKolektibilitasPembayaranDetails) throws DBException {
    try {
      if (entKolektibilitasPembayaranDetails.getOID() != 0) {
        PstKolektibilitasPembayaranDetails pstKolektibilitasPembayaranDetails = new PstKolektibilitasPembayaranDetails(entKolektibilitasPembayaranDetails.getOID());
        pstKolektibilitasPembayaranDetails.setLong(FLD_KOLEKTIBILITASID, entKolektibilitasPembayaranDetails.getKolektibilitasId());
        pstKolektibilitasPembayaranDetails.setInt(FLD_TIPEKREIDT, entKolektibilitasPembayaranDetails.getTipeKreidt());
        pstKolektibilitasPembayaranDetails.setInt(FLD_MAXHARITUNGGAKANPOKOK, entKolektibilitasPembayaranDetails.getMaxHariTunggakanPokok());
        pstKolektibilitasPembayaranDetails.setInt(FLD_MAXHARIJUMLAHTUNGGAKANBUNGA, entKolektibilitasPembayaranDetails.getMaxHariJumlahTunggakanBunga());
        pstKolektibilitasPembayaranDetails.update();
        return entKolektibilitasPembayaranDetails.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstKolektibilitasPembayaranDetails(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((KolektibilitasPembayaranDetails) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstKolektibilitasPembayaranDetails pstKolektibilitasPembayaranDetails = new PstKolektibilitasPembayaranDetails(oid);
      pstKolektibilitasPembayaranDetails.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstKolektibilitasPembayaranDetails(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(KolektibilitasPembayaranDetails entKolektibilitasPembayaranDetails) throws DBException {
    try {
      PstKolektibilitasPembayaranDetails pstKolektibilitasPembayaranDetails = new PstKolektibilitasPembayaranDetails(0);
      pstKolektibilitasPembayaranDetails.setLong(FLD_KOLEKTIBILITASID, entKolektibilitasPembayaranDetails.getKolektibilitasId());
      pstKolektibilitasPembayaranDetails.setInt(FLD_TIPEKREIDT, entKolektibilitasPembayaranDetails.getTipeKreidt());
      pstKolektibilitasPembayaranDetails.setInt(FLD_MAXHARITUNGGAKANPOKOK, entKolektibilitasPembayaranDetails.getMaxHariTunggakanPokok());
      pstKolektibilitasPembayaranDetails.setInt(FLD_MAXHARIJUMLAHTUNGGAKANBUNGA, entKolektibilitasPembayaranDetails.getMaxHariJumlahTunggakanBunga());
      pstKolektibilitasPembayaranDetails.insert();
      entKolektibilitasPembayaranDetails.setOID(pstKolektibilitasPembayaranDetails.getlong(FLD_KOLEKTIBILITASDETAILID));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstKolektibilitasPembayaranDetails(0), DBException.UNKNOWN);
    }
    return entKolektibilitasPembayaranDetails.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((KolektibilitasPembayaranDetails) entity);
  }

  public static void resultToObject(ResultSet rs, KolektibilitasPembayaranDetails entKolektibilitasPembayaranDetails) {
    try {
      entKolektibilitasPembayaranDetails.setOID(rs.getLong(PstKolektibilitasPembayaranDetails.fieldNames[PstKolektibilitasPembayaranDetails.FLD_KOLEKTIBILITASDETAILID]));
      entKolektibilitasPembayaranDetails.setKolektibilitasId(rs.getLong(PstKolektibilitasPembayaranDetails.fieldNames[PstKolektibilitasPembayaranDetails.FLD_KOLEKTIBILITASID]));
      entKolektibilitasPembayaranDetails.setTipeKreidt(rs.getInt(PstKolektibilitasPembayaranDetails.fieldNames[PstKolektibilitasPembayaranDetails.FLD_TIPEKREIDT]));
      entKolektibilitasPembayaranDetails.setMaxHariTunggakanPokok(rs.getInt(PstKolektibilitasPembayaranDetails.fieldNames[PstKolektibilitasPembayaranDetails.FLD_MAXHARITUNGGAKANPOKOK]));
      entKolektibilitasPembayaranDetails.setMaxHariJumlahTunggakanBunga(rs.getInt(PstKolektibilitasPembayaranDetails.fieldNames[PstKolektibilitasPembayaranDetails.FLD_MAXHARIJUMLAHTUNGGAKANBUNGA]));
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
      String sql = "SELECT * FROM " + TBL_KOLEKTIBILITASPEMBAYARANDETAILS;
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
        KolektibilitasPembayaranDetails entKolektibilitasPembayaranDetails = new KolektibilitasPembayaranDetails();
        resultToObject(rs, entKolektibilitasPembayaranDetails);
        lists.add(entKolektibilitasPembayaranDetails);
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

  public static Vector getJoin(int limitStart, int recordToGet, String whereClause, String order) {
    Vector lists = new Vector();
    DBResultSet dbrs = null;
    try {
      String sql = "SELECT * FROM " + TBL_KOLEKTIBILITASPEMBAYARANDETAILS + " JOIN " + PstKolektibilitasPembayaran.TBL_KOLEKTIBILITASPEMBAYARAN + " USING (`KOLEKTIBILITAS_ID`) ";
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
        KolektibilitasPembayaranDetails entKolektibilitasPembayaranDetails = new KolektibilitasPembayaranDetails();
        try {
          entKolektibilitasPembayaranDetails.setOID(rs.getLong(PstKolektibilitasPembayaranDetails.fieldNames[PstKolektibilitasPembayaranDetails.FLD_KOLEKTIBILITASDETAILID]));
          entKolektibilitasPembayaranDetails.setKolektibilitasId(rs.getLong(PstKolektibilitasPembayaranDetails.fieldNames[PstKolektibilitasPembayaranDetails.FLD_KOLEKTIBILITASID]));
          entKolektibilitasPembayaranDetails.setTipeKreidt(rs.getInt(PstKolektibilitasPembayaranDetails.fieldNames[PstKolektibilitasPembayaranDetails.FLD_TIPEKREIDT]));
          entKolektibilitasPembayaranDetails.setMaxHariTunggakanPokok(rs.getInt(PstKolektibilitasPembayaranDetails.fieldNames[PstKolektibilitasPembayaranDetails.FLD_MAXHARITUNGGAKANPOKOK]));
          entKolektibilitasPembayaranDetails.setMaxHariJumlahTunggakanBunga(rs.getInt(PstKolektibilitasPembayaranDetails.fieldNames[PstKolektibilitasPembayaranDetails.FLD_MAXHARIJUMLAHTUNGGAKANBUNGA]));
          entKolektibilitasPembayaranDetails.setKodeKolektibilitas(rs.getString(PstKolektibilitasPembayaran.fieldNames[PstKolektibilitasPembayaran.FLD_KODE_KOLEKTIBILITAS]));
          entKolektibilitasPembayaranDetails.setTingkatKolektibilitas(rs.getInt(PstKolektibilitasPembayaran.fieldNames[PstKolektibilitasPembayaran.FLD_TINGKAT_KOLEKTIBILITAS]));
          entKolektibilitasPembayaranDetails.setJudulKolektibilitas(rs.getString(PstKolektibilitasPembayaran.fieldNames[PstKolektibilitasPembayaran.FLD_JUDUL_KOLEKTIBILITAS]));
          entKolektibilitasPembayaranDetails.setTingkatResiko(rs.getFloat(PstKolektibilitasPembayaran.fieldNames[PstKolektibilitasPembayaran.FLD_TINGKAT_RESIKO]));
          lists.add(entKolektibilitasPembayaranDetails);
        } catch (Exception e) {
        }
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

  public static boolean checkOID(long entKolektibilitasPembayaranDetailsId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_KOLEKTIBILITASPEMBAYARANDETAILS + " WHERE "
              + PstKolektibilitasPembayaranDetails.fieldNames[PstKolektibilitasPembayaranDetails.FLD_KOLEKTIBILITASID] + " = " + entKolektibilitasPembayaranDetailsId;
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
      String sql = "SELECT COUNT(" + PstKolektibilitasPembayaranDetails.fieldNames[PstKolektibilitasPembayaranDetails.FLD_KOLEKTIBILITASDETAILID] + ") FROM " + TBL_KOLEKTIBILITASPEMBAYARANDETAILS;
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
          KolektibilitasPembayaranDetails entKolektibilitasPembayaranDetails = (KolektibilitasPembayaranDetails) list.get(ls);
          if (oid == entKolektibilitasPembayaranDetails.getOID()) {
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
