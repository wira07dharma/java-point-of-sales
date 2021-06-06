/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

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
import org.json.JSONObject;

public class PstAlternatif extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_ALTERNATIF = "pos_alternatif";
  public static final int FLD_POS_ALTERNATIF_ID = 0;
  public static final int FLD_ITEM_ID = 1;
  public static final int FLD_ROOM_CLASS_ID = 2;
  public static final int FLD_PRIORITY = 3;

  public static String[] fieldNames = {
    "POS_ALTERNATIF_ID",
    "ITEM_ID",
    "ROOM_CLASS_ID",
    "PRIORITY"
  };

  public static int[] fieldTypes = {
    TYPE_LONG + TYPE_PK + TYPE_ID,
    TYPE_LONG,
    TYPE_LONG,
    TYPE_INT
  };

  public PstAlternatif() {
  }

  public PstAlternatif(int i) throws DBException {
    super(new PstAlternatif());
  }

  public PstAlternatif(String sOid) throws DBException {
    super(new PstAlternatif(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstAlternatif(long lOid) throws DBException {
    super(new PstAlternatif(0));
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
    return TBL_ALTERNATIF;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstAlternatif().getClass().getName();
  }

  public static Alternatif fetchExc(long oid) throws DBException {
    try {
      Alternatif entAlternatif = new Alternatif();
      PstAlternatif pstAlternatif = new PstAlternatif(oid);
      entAlternatif.setOID(oid);
      entAlternatif.setItemId(pstAlternatif.getlong(FLD_ITEM_ID));
      entAlternatif.setRoomClassId(pstAlternatif.getlong(FLD_ROOM_CLASS_ID));
      entAlternatif.setPriority(pstAlternatif.getInt(FLD_PRIORITY));
      return entAlternatif;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAlternatif(0), DBException.UNKNOWN);
    }
  }

  public long fetchExc(Entity entity) throws Exception {
    Alternatif entAlternatif = fetchExc(entity.getOID());
    entity = (Entity) entAlternatif;
    return entAlternatif.getOID();
  }

  public static synchronized long updateExc(Alternatif entAlternatif) throws DBException {
    try {
      if (entAlternatif.getOID() != 0) {
        PstAlternatif pstAlternatif = new PstAlternatif(entAlternatif.getOID());
        pstAlternatif.setLong(FLD_ITEM_ID, entAlternatif.getItemId());
        pstAlternatif.setLong(FLD_ROOM_CLASS_ID, entAlternatif.getRoomClassId());
        pstAlternatif.setInt(FLD_PRIORITY, entAlternatif.getPriority());
        pstAlternatif.update();
        return entAlternatif.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAlternatif(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((Alternatif) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstAlternatif pstAlternatif = new PstAlternatif(oid);
      pstAlternatif.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAlternatif(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(Alternatif entAlternatif) throws DBException {
    try {
      PstAlternatif pstAlternatif = new PstAlternatif(0);
      pstAlternatif.setLong(FLD_ITEM_ID, entAlternatif.getItemId());
      pstAlternatif.setLong(FLD_ROOM_CLASS_ID, entAlternatif.getRoomClassId());
      pstAlternatif.setInt(FLD_PRIORITY, entAlternatif.getPriority());
      pstAlternatif.insert();
      entAlternatif.setOID(pstAlternatif.getlong(FLD_POS_ALTERNATIF_ID));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstAlternatif(0), DBException.UNKNOWN);
    }
    return entAlternatif.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((Alternatif) entity);
  }

  public static void resultToObject(ResultSet rs, Alternatif entAlternatif) {
    try {
      entAlternatif.setOID(rs.getLong(PstAlternatif.fieldNames[PstAlternatif.FLD_POS_ALTERNATIF_ID]));
      entAlternatif.setItemId(rs.getLong(PstAlternatif.fieldNames[PstAlternatif.FLD_ITEM_ID]));
      entAlternatif.setRoomClassId(rs.getLong(PstAlternatif.fieldNames[PstAlternatif.FLD_ROOM_CLASS_ID]));
      entAlternatif.setPriority(rs.getInt(PstAlternatif.fieldNames[PstAlternatif.FLD_PRIORITY]));
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
      String sql = "SELECT * FROM " + TBL_ALTERNATIF;
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
        Alternatif entAlternatif = new Alternatif();
        resultToObject(rs, entAlternatif);
        lists.add(entAlternatif);
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

  public static boolean checkOID(long entAlternatifId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_ALTERNATIF + " WHERE "
              + PstAlternatif.fieldNames[PstAlternatif.FLD_POS_ALTERNATIF_ID] + " = " + entAlternatifId;
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
      String sql = "SELECT COUNT(" + PstAlternatif.fieldNames[PstAlternatif.FLD_POS_ALTERNATIF_ID] + ") FROM " + TBL_ALTERNATIF;
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
          Alternatif entAlternatif = (Alternatif) list.get(ls);
          if (oid == entAlternatif.getOID()) {
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
  
  public static boolean deleteAlternatifByItemId(long itemId) {
    boolean r = true;
    DBResultSet dbrs = null;
    try {
      String sql = "DELETE FROM `pos_alternatif` WHERE ITEM_ID = "+itemId;
      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
    } catch (Exception e) {
      r = false;
    } finally {
      DBResultSet.close(dbrs);
    }
    return r;
  }
  
          // ------------------ end added by eri  -----------------------------------
	public static JSONObject fetchJSON(long oid){
		JSONObject object = new JSONObject();
		try {
                 Alternatif alternatif = PstAlternatif.fetchExc(oid);
                 object.put(PstAlternatif.fieldNames[PstAlternatif.FLD_POS_ALTERNATIF_ID], alternatif.getOID());
                 object.put(PstAlternatif.fieldNames[PstAlternatif.FLD_ITEM_ID], alternatif.getItemId());
                 object.put(PstAlternatif.fieldNames[PstAlternatif.FLD_ROOM_CLASS_ID], alternatif.getRoomClassId());
                 object.put(PstAlternatif.fieldNames[PstAlternatif.FLD_PRIORITY], alternatif.getPriority());
                }catch(Exception exc){}
            
                return object;
            }
  
}
