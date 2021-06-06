package com.dimata.aiso.entity.masterdata.anggota;

import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;
import java.sql.*;
import java.util.*;

public class PstPosition extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  /**
   * keteranan: fungsi ini adalah inisialisasi database dengan tabel bernama
   * tb_anggota
   */
  public static final String TBL_TB_POSITION = "aiso_position";

  public static final int FLD_POSITION_ID = 0;
  public static final int FLD_POSITION_NAME = 1;

  public static final String[] fieldNames = {
    "POSITION_ID",
    "POSITION_NAME"
  };

  /**
   * menginisialisasikan tipe data untuk database..
   */
  public static final int[] fieldTypes = {
    TYPE_LONG + TYPE_ID + TYPE_PK,
    TYPE_STRING,};

  public PstPosition() {
  }

  /**
   * fungsi yang otomatis akan dipanggil setiap kali melakukan instasiasi
   * terhadap suatu kelas
   *
   * @param i
   * @throws DBException
   */
  public PstPosition(int i) throws DBException {
    super(new PstPosition());
  }

  public PstPosition(String sOid) throws DBException {
    super(new PstPosition(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstPosition(long lOid) throws DBException {
    super(new PstPosition(0));
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
    return TBL_TB_POSITION;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstPosition().getClass().getName();
  }

  public long fetchExc(Entity ent) throws Exception {
    Position position = fetchExc(ent.getOID());
    ent = (Entity) position;
    return position.getOID();
  }

  public long insertExc(Entity ent) throws Exception {
    return insertExc((Position) ent);
  }

  public long updateExc(Entity ent) throws Exception {
    return updateExc((Position) ent);
  }

  public long deleteExc(Entity ent) throws Exception {
    if (ent == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(ent.getOID());
  }

  public static Position fetchExc(long oid) {
    Position position = new Position();
    try {
      PstPosition pstPosition = new PstPosition(oid);
      position.setOID(oid);
      position.setPositionName(pstPosition.getString(FLD_POSITION_NAME));

    } catch (DBException dbe) {
      System.out.println(dbe);
    } catch (Exception e) {
      System.out.println(e);
    }
    return position;
  }

  public static long insertExc(Position position) throws DBException {

    try {

      PstPosition pstPosition = new PstPosition(0);

      pstPosition.setString(FLD_POSITION_NAME, position.getPositionName());

      pstPosition.insert();

      position.setOID(pstPosition.getlong(FLD_POSITION_ID));

    } catch (DBException dbe) {

      throw dbe;

    } catch (Exception e) {

      throw new DBException(new PstPosition(0), DBException.UNKNOWN);

    }

    return position.getOID();

  }

  public static long updateExc(Position position) throws DBException {
    try {
      if (position.getOID() != 0) {
        PstPosition pstPosition = new PstPosition(position.getOID());
        pstPosition.setString(FLD_POSITION_NAME, position.getPositionName());
        pstPosition.update();
        return position.getOID();
      }

    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstPosition(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public static long deleteExc(long oid) throws DBException {
    try {
      /**
       * Mendefinisikan objek sekalligus menginisialisasi nilai objek.
       */
      PstPosition pstPosition = new PstPosition(oid);
      pstPosition.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstPosition(0), DBException.UNKNOWN);
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
      String sql = "SELECT * FROM " + TBL_TB_POSITION;
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
        Position position = new Position();
        resultToObject(rs, position);
        lists.add(position);
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

  public static void resultToObject(ResultSet rs, Position position) {
    try {

      position.setOID(rs.getLong(PstPosition.fieldNames[PstPosition.FLD_POSITION_ID]));

      position.setPositionName(rs.getString(PstPosition.fieldNames[PstPosition.FLD_POSITION_NAME]));

    } catch (Exception e) {
      System.out.println("Exception" + e);
    }
  }

  public static boolean checkOID(long osId) {

    DBResultSet dbrs = null;

    boolean result = false;

    try {

      String sql = "SELECT * FROM " + TBL_TB_POSITION + " WHERE "
              + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] + " = " + osId;

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
      String sql = "SELECT COUNT(" + PstPosition.fieldNames[PstPosition.FLD_POSITION_NAME] + ") FROM " + TBL_TB_POSITION;
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
          Position position = (Position) list.get(ls);
          if (oid == position.getOID()) {
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
  /* This method used to find command where current data */

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
