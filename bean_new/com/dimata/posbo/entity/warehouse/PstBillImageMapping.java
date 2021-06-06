/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.warehouse;

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

public class PstBillImageMapping extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_BILLIMAGEMAPPING = "bill_image_proof_mapping";
  public static final int FLD_IMAGE_ID = 0;
  public static final int FLD_BILL_MAIN_ID = 1;
  public static final int FLD_FILE_NAME = 2;
  public static final int FLD_DOCUMENT_NAME = 3;
  public static final int FLD_KETERANGAN = 4;

  public static String[] fieldNames = {
    "IMAGE_ID",
    "BILL_MAIN_ID",
    "FILE_NAME",
    "DOCUMENT_NAME",
    "KETERANGAN"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_LONG,
    TYPE_STRING,
    TYPE_STRING,
    TYPE_STRING
  };

  public PstBillImageMapping() {
  }

  public PstBillImageMapping(int i) throws DBException {
    super(new PstBillImageMapping());
  }

  public PstBillImageMapping(String sOid) throws DBException {
    super(new PstBillImageMapping(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstBillImageMapping(long lOid) throws DBException {
    super(new PstBillImageMapping(0));
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
    return TBL_BILLIMAGEMAPPING;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstBillImageMapping().getClass().getName();
  }

  public static BillImageMapping fetchExc(long oid) throws DBException {
    try {
      BillImageMapping entBillImageMapping = new BillImageMapping();
      PstBillImageMapping pstBillImageMapping = new PstBillImageMapping(oid);
      entBillImageMapping.setOID(oid);
      entBillImageMapping.setBillMainId(pstBillImageMapping.getlong(FLD_BILL_MAIN_ID));
      entBillImageMapping.setFileName(pstBillImageMapping.getString(FLD_FILE_NAME));
      entBillImageMapping.setDocumentName(pstBillImageMapping.getString(FLD_DOCUMENT_NAME));
      entBillImageMapping.setKeterangan(pstBillImageMapping.getString(FLD_KETERANGAN));
      return entBillImageMapping;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstBillImageMapping(0), DBException.UNKNOWN);
    }
  }

  public long fetchExc(Entity entity) throws Exception {
    BillImageMapping entBillImageMapping = fetchExc(entity.getOID());
    entity = (Entity) entBillImageMapping;
    return entBillImageMapping.getOID();
  }

  public static synchronized long updateExc(BillImageMapping entBillImageMapping) throws DBException {
    try {
      if (entBillImageMapping.getOID() != 0) {
        PstBillImageMapping pstBillImageMapping = new PstBillImageMapping(entBillImageMapping.getOID());
        pstBillImageMapping.setLong(FLD_BILL_MAIN_ID, entBillImageMapping.getBillMainId());
        pstBillImageMapping.setString(FLD_FILE_NAME, entBillImageMapping.getFileName());
        pstBillImageMapping.setString(FLD_DOCUMENT_NAME, entBillImageMapping.getDocumentName());
        pstBillImageMapping.setString(FLD_KETERANGAN, entBillImageMapping.getKeterangan());
        pstBillImageMapping.update();
        return entBillImageMapping.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstBillImageMapping(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((BillImageMapping) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstBillImageMapping pstBillImageMapping = new PstBillImageMapping(oid);
      pstBillImageMapping.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstBillImageMapping(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(BillImageMapping entBillImageMapping) throws DBException {
    try {
      PstBillImageMapping pstBillImageMapping = new PstBillImageMapping(0);
      pstBillImageMapping.setLong(FLD_BILL_MAIN_ID, entBillImageMapping.getBillMainId());
      pstBillImageMapping.setString(FLD_FILE_NAME, entBillImageMapping.getFileName());
      pstBillImageMapping.setString(FLD_DOCUMENT_NAME, entBillImageMapping.getDocumentName());
      pstBillImageMapping.setString(FLD_KETERANGAN, entBillImageMapping.getKeterangan());
      pstBillImageMapping.insert();
      entBillImageMapping.setOID(pstBillImageMapping.getlong(FLD_IMAGE_ID));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstBillImageMapping(0), DBException.UNKNOWN);
    }
    return entBillImageMapping.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((BillImageMapping) entity);
  }

  public static void resultToObject(ResultSet rs, BillImageMapping entBillImageMapping) {
    try {
      entBillImageMapping.setOID(rs.getLong(PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_IMAGE_ID]));
      entBillImageMapping.setBillMainId(rs.getLong(PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_BILL_MAIN_ID]));
      entBillImageMapping.setFileName(rs.getString(PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_FILE_NAME]));
      entBillImageMapping.setDocumentName(rs.getString(PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_DOCUMENT_NAME]));
      entBillImageMapping.setKeterangan(rs.getString(PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_KETERANGAN]));
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
      String sql = "SELECT * FROM " + TBL_BILLIMAGEMAPPING;
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
        BillImageMapping entBillImageMapping = new BillImageMapping();
        resultToObject(rs, entBillImageMapping);
        lists.add(entBillImageMapping);
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

  public static boolean checkOID(long entBillImageMappingId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_BILLIMAGEMAPPING + " WHERE "
              + PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_IMAGE_ID] + " = " + entBillImageMappingId;
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
      String sql = "SELECT COUNT(" + PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_IMAGE_ID] + ") FROM " + TBL_BILLIMAGEMAPPING;
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
          BillImageMapping entBillImageMapping = (BillImageMapping) list.get(ls);
          if (oid == entBillImageMapping.getOID()) {
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
    public static void updateFileName(String fileName, long idDokumen) {
    try {
      String sql = "UPDATE " + PstBillImageMapping.TBL_BILLIMAGEMAPPING
              + " SET " + PstBillImageMapping.fieldNames[FLD_FILE_NAME] + " = '" + fileName + "'"
              + " WHERE " + PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_IMAGE_ID]
              + " = " + idDokumen;
      System.out.println("sql updateFileName : " + sql);
      int result = DBHandler.execUpdate(sql);
    } catch (Exception e) {
      System.out.println("\tExc updateFileName : " + e.toString());
    } finally {
      //System.out.println("\tFinal updatePresenceStatus");
    }
  }
}
