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
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author dedy_blinda
 */
public class PstEmpRelevantDoc extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_HR_EMP_RELEVANT_DOC = "sedana_relevant_doc";//"HR_EMP_RELEVANT_DOC";

  public static final int FLD_DOC_RELEVANT_ID = 0;
  public static final int FLD_ANGGOTA_ID = 1;
  public static final int FLD_DOC_TITLE = 2;
  public static final int FLD_DOC_DESCRIPTION = 3;
  public static final int FLD_FILE_NAME = 4;
  public static final int FLD_DOC_ATTACH_FILE = 5;
  public static final int FLD_RELVT_DOC_GRP_ID = 6;

  public static final String[] fieldNames = {
    "DOC_RELEVANT_ID",
    "ANGGOTA_ID",
    "DOC_TITLE",
    "DOC_DESCRIPTION",
    "FILE_NAME",
    "DOC_ATTACH_FILE",
    "RELVT_DOC_GRP_ID"
  };

  public static final int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_LONG,
    TYPE_STRING,
    TYPE_STRING,
    TYPE_STRING,
    TYPE_STRING,
    TYPE_LONG,};

  /**
   * Creates a new instance of PstEmpRelevantDoc
   */
  public PstEmpRelevantDoc() {
  }

  public PstEmpRelevantDoc(int i) throws DBException {
    super(new PstEmpRelevantDoc());
  }

  public PstEmpRelevantDoc(String sOid) throws DBException {
    super(new PstEmpRelevantDoc(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstEmpRelevantDoc(long lOid) throws DBException {
    super(new PstEmpRelevantDoc(0));
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

  public long deleteExc(Entity ent) throws Exception {
    if (ent == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(ent.getOID());
  }

  public long fetchExc(Entity ent) throws Exception {
    EmpRelevantDoc empRelevantDoc = fetchExc(ent.getOID());
    ent = (Entity) empRelevantDoc;
    return empRelevantDoc.getOID();
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int getFieldSize() {
    return fieldNames.length;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstEmpRelevantDoc().getClass().getName();
  }

  public String getTableName() {
    return TBL_HR_EMP_RELEVANT_DOC;
  }

  public long insertExc(Entity ent) throws Exception {
    return insertExc((EmpRelevantDoc) ent);
  }

  public long updateExc(Entity ent) throws Exception {
    return updateExc((EmpRelevantDoc) ent);
  }

  public static EmpRelevantDoc fetchExc(long oid) throws DBException {
    try {
      EmpRelevantDoc empRelevantDoc = new EmpRelevantDoc();
      PstEmpRelevantDoc pstEmpRelevantDoc = new PstEmpRelevantDoc(oid);
      empRelevantDoc.setOID(oid);

      empRelevantDoc.setAnggotaId(pstEmpRelevantDoc.getlong(FLD_ANGGOTA_ID));
      empRelevantDoc.setDocTitle(pstEmpRelevantDoc.getString(FLD_DOC_TITLE));
      empRelevantDoc.setDocDescription(pstEmpRelevantDoc.getString(FLD_DOC_DESCRIPTION));
      empRelevantDoc.setFileName(pstEmpRelevantDoc.getString(FLD_FILE_NAME));
      empRelevantDoc.setDocAttachFile(pstEmpRelevantDoc.getString(FLD_DOC_ATTACH_FILE));
      empRelevantDoc.setRelvtDocGrpId(pstEmpRelevantDoc.getlong(FLD_RELVT_DOC_GRP_ID));

      return empRelevantDoc;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstEmpRelevantDoc(0), DBException.UNKNOWN);
    }
  }

  public static long insertExc(EmpRelevantDoc empRelevantDoc) throws DBException {
    try {
      PstEmpRelevantDoc pstEmpRelevantDoc = new PstEmpRelevantDoc(0);
      pstEmpRelevantDoc.setLong(FLD_ANGGOTA_ID, empRelevantDoc.getAnggotaId());
      pstEmpRelevantDoc.setString(FLD_DOC_TITLE, empRelevantDoc.getDocTitle());
      pstEmpRelevantDoc.setString(FLD_DOC_DESCRIPTION, empRelevantDoc.getDocDescription());
      pstEmpRelevantDoc.setString(FLD_FILE_NAME, empRelevantDoc.getFileName());
      pstEmpRelevantDoc.setString(FLD_DOC_ATTACH_FILE, empRelevantDoc.getDocAttachFile());
      pstEmpRelevantDoc.setLong(FLD_RELVT_DOC_GRP_ID, empRelevantDoc.getRelvtDocGrpId());

      pstEmpRelevantDoc.insert();
      empRelevantDoc.setOID(pstEmpRelevantDoc.getlong(FLD_DOC_RELEVANT_ID));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstEmpRelevantDoc(0), DBException.UNKNOWN);
    }
    return empRelevantDoc.getOID();
  }

  public static long updateExc(EmpRelevantDoc empRelevantDoc) throws DBException {
    try {
      if (empRelevantDoc.getOID() != 0) {
        PstEmpRelevantDoc pstEmpRelevantDoc = new PstEmpRelevantDoc(empRelevantDoc.getOID());

        pstEmpRelevantDoc.setLong(FLD_ANGGOTA_ID, empRelevantDoc.getAnggotaId());
        pstEmpRelevantDoc.setString(FLD_DOC_TITLE, empRelevantDoc.getDocTitle());
        pstEmpRelevantDoc.setString(FLD_DOC_DESCRIPTION, empRelevantDoc.getDocDescription());
        pstEmpRelevantDoc.setString(FLD_FILE_NAME, empRelevantDoc.getFileName());
        pstEmpRelevantDoc.setString(FLD_DOC_ATTACH_FILE, empRelevantDoc.getDocAttachFile());
        pstEmpRelevantDoc.setLong(FLD_RELVT_DOC_GRP_ID, empRelevantDoc.getRelvtDocGrpId());

        pstEmpRelevantDoc.update();
        return empRelevantDoc.getOID();

      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstEmpRelevantDoc(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public static long deleteExc(long oid) throws DBException {
    try {
      PstEmpRelevantDoc pstEmpRelevantDoc = new PstEmpRelevantDoc(oid);
      pstEmpRelevantDoc.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstEmpRelevantDoc(0), DBException.UNKNOWN);
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
      String sql = "SELECT * FROM " + TBL_HR_EMP_RELEVANT_DOC;
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
        EmpRelevantDoc empRelevantDoc = new EmpRelevantDoc();
        resultToObject(rs, empRelevantDoc);
        lists.add(empRelevantDoc);
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

  private static void resultToObject(ResultSet rs, EmpRelevantDoc empRelevantDoc) {
    try {
      empRelevantDoc.setOID(rs.getLong(PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_RELEVANT_ID]));
      empRelevantDoc.setAnggotaId(rs.getLong(PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_ANGGOTA_ID]));
      empRelevantDoc.setDocTitle(rs.getString(PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_TITLE]));
      empRelevantDoc.setDocDescription(rs.getString(PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_DESCRIPTION]));
      empRelevantDoc.setFileName(rs.getString(PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_FILE_NAME]));
      empRelevantDoc.setDocAttachFile(rs.getString(PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_ATTACH_FILE]));
      empRelevantDoc.setRelvtDocGrpId(rs.getLong(PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_RELVT_DOC_GRP_ID]));

    } catch (Exception e) {
    }
  }

  public static boolean checkOID(long empRelevantDocId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_HR_EMP_RELEVANT_DOC + " WHERE "
              + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_RELEVANT_ID] + " = '" + empRelevantDocId + "'";

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
      String sql = "SELECT COUNT(" + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_RELEVANT_ID] + ") FROM " + TBL_HR_EMP_RELEVANT_DOC;
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
  public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
    int size = getCount(whereClause);
    int start = 0;
    boolean found = false;
    for (int i = 0; (i < size) && !found; i = i + recordToGet) {
      Vector list = list(i, recordToGet, whereClause, orderClause);
      start = i;
      if (list.size() > 0) {
        for (int ls = 0; ls < list.size(); ls++) {
          EmpRelevantDoc empRelevantDoc = (EmpRelevantDoc) list.get(ls);
          if (oid == empRelevantDoc.getOID()) {
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

  public static long deleteByEmployee(long emplOID) {
    try {
      String sql = " DELETE FROM " + PstEmpRelevantDoc.TBL_HR_EMP_RELEVANT_DOC
              + " WHERE " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_ANGGOTA_ID]
              + " = " + emplOID;

      int status = DBHandler.execUpdate(sql);
    } catch (Exception exc) {
      System.out.println("error delete language by employee " + exc.toString());
    }

    return emplOID;
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

  /**
   * Check if employee with this OID already exist return true if exist, false
   * otherwise
   *
   * @param employeeId
   * @return
   */
  public static EmpRelevantDoc getObjEmpPicture(long employeeId) {
    EmpRelevantDoc objEmpRelevantDoc = new EmpRelevantDoc();
    DBResultSet dbrs = null;
    try {
      String sql = "SELECT " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_RELEVANT_ID]
              + ", " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_ANGGOTA_ID]
              + ", " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_TITLE]
              + ", " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_DESCRIPTION]
              + ", " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_FILE_NAME]
              + ", " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_ATTACH_FILE]
              + " FROM " + TBL_HR_EMP_RELEVANT_DOC
              + " WHERE " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_ANGGOTA_ID]
              + " = " + employeeId;

      System.out.println("SQL PstEmpRelevantDoc.getObjEmpPicture : " + sql);
      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      while (rs.next()) {
        objEmpRelevantDoc.setOID(rs.getLong(1));
        objEmpRelevantDoc.setAnggotaId(rs.getLong(2));
        objEmpRelevantDoc.setDocTitle(rs.getString(3));
        objEmpRelevantDoc.setDocDescription(rs.getString(4));
        objEmpRelevantDoc.setFileName(rs.getString(5));
        objEmpRelevantDoc.setDocAttachFile(rs.getString(6));

        return objEmpRelevantDoc;
      }
    } catch (Exception e) {
      System.out.println(e);
    } finally {
      DBResultSet.close(dbrs);
      return objEmpRelevantDoc;
    }
  }

  /**
   * Check if employee with this OID already exist return true if exist, false
   * otherwise
   *
   * @param employeeId
   * @return
   */
  public static EmpRelevantDoc getTitle(String title) {
    EmpRelevantDoc objEmpRelevantDoc = new EmpRelevantDoc();
    DBResultSet dbrs = null;
    try {
      String sql = "SELECT " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_RELEVANT_ID]
              + ", " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_ANGGOTA_ID]
              + ", " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_TITLE]
              + ", " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_DESCRIPTION]
              + ", " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_FILE_NAME]
              + ", " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_ATTACH_FILE]
              + " FROM " + TBL_HR_EMP_RELEVANT_DOC
              + " WHERE " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_TITLE]
              + " = '" + title.trim() + "'";

      System.out.println("SQL PstEmpRelevantDoc.getTitle : " + sql);
      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      while (rs.next()) {
        objEmpRelevantDoc.setOID(rs.getLong(1));
        objEmpRelevantDoc.setAnggotaId(rs.getLong(2));
        objEmpRelevantDoc.setDocTitle(rs.getString(3));
        objEmpRelevantDoc.setDocDescription(rs.getString(4));
        objEmpRelevantDoc.setFileName(rs.getString(5));
        objEmpRelevantDoc.setDocAttachFile(rs.getString(6));

        return objEmpRelevantDoc;
      }
    } catch (Exception e) {
      System.out.println(e);
    } finally {
      DBResultSet.close(dbrs);
      return objEmpRelevantDoc;
    }
  }

  public static void updateFileName(String fileName, long idRelevant) {
    try {
      String sql = "UPDATE " + PstEmpRelevantDoc.TBL_HR_EMP_RELEVANT_DOC
              + " SET " + PstEmpRelevantDoc.fieldNames[FLD_FILE_NAME] + " = '" + fileName + "'"
              + " WHERE " + PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_RELEVANT_ID]
              + " = " + idRelevant;
      System.out.println("sql PstEmpRelevantDoc.updateFileName : " + sql);
      int result = DBHandler.execUpdate(sql);
    } catch (Exception e) {
      System.out.println("\tExc updateFileName : " + e.toString());
    } finally {
      //System.out.println("\tFinal updatePresenceStatus");
    }
  }

  public static void main(String args[]) {
        //long result = getPeriodIdBySelectedDate(new Date(104,5,31));
    //long result = getPeriodIdJustBefore(504404240112808778L);
    //System.out.println("result : " + result);

    /*Date netDate = new Date();*/
    EmpRelevantDoc empRelevantDoc = new EmpRelevantDoc();
    empRelevantDoc.setDocRelevantId(Long.parseLong("504404357178636270"));
    empRelevantDoc.setAnggotaId(12356565L);
    empRelevantDoc.setDocTitle("test");
    empRelevantDoc.setDocDescription("test desc");
    empRelevantDoc.setFileName("file name");
       // empRelevantDoc.setDate(new Date());
    //empRelevantDoc.setDocAttachFile("attach");

    try {
      PstEmpRelevantDoc.updateExc(empRelevantDoc);
      System.out.println("masuk tabel");
    } catch (Exception e) {
      System.out.println("err tabel");
      System.out.println("Err" + e.toString());
    }

    /* String stOut = listDateOut(26, 226, 504404343502872505L);
     System.out.println("dtOut........"+stOut);   
     Date dtOut = Formater.formatDate(stOut, "yyyy-MM-dd HH:mm");
     System.out.println("dtOut........"+dtOut);    
     String dtActualReal = Formater.formatDate(dtOut, "yyyy-MM-dd");
     String dtTimeActualReal = Formater.formatTimeLocale(dtOut, "HH:mm");
     System.out.println("dtActualReal........"+dtActualReal);
     System.out.println("dtTimeActualReal........"+dtTimeActualReal);
     Date dtTimeReal = Formater.formatDate(dtTimeActualReal, "HH:mm");
     System.out.println("dtTimeReal........"+dtTimeReal);
     
     String strCoba = "10";
     double coba = Double.parseDouble(strCoba);
     System.out.println("coba:::::::::::::::::::::::::::::::::::"+coba);*/
  }
}
