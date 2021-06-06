/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.admin;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

public class PstMappingUserGroup extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

  public static final String TBL_MAPPING_USER_GROUP = "mapping_user_group";
  public static final int FLD_MAPPING_USER_GROUP_ID = 0;
  public static final int FLD_USER_ID = 1;
  public static final int FLD_GROUP_USER_ID = 2;
  public static final int FLD_COMPANY_ID = 3;

  public static String[] fieldNames = {
    "MAPPING_USER_GROUP_ID",
    "USER_ID",
    "GROUP_USER_ID",
    "COMPANY_ID"
  };

  public static int[] fieldTypes = {
    TYPE_LONG+TYPE_PK+TYPE_ID,
    TYPE_LONG,
    TYPE_LONG,
    TYPE_LONG
  };

  public PstMappingUserGroup() {
  }

  public PstMappingUserGroup(int i) throws DBException {
    super(new PstMappingUserGroup());
  }

  public PstMappingUserGroup(String sOid) throws DBException {
    super(new PstMappingUserGroup(0));
    if (!locate(sOid)) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    } else {
      return;
    }
  }

  public PstMappingUserGroup(long lOid) throws DBException {
    super(new PstMappingUserGroup(0));
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
    return TBL_MAPPING_USER_GROUP;
  }

  public String[] getFieldNames() {
    return fieldNames;
  }

  public int[] getFieldTypes() {
    return fieldTypes;
  }

  public String getPersistentName() {
    return new PstMappingUserGroup().getClass().getName();
  }

  public static MappingUserGroup fetchExc(long oid) throws DBException {
    try {
      MappingUserGroup entMappingUserGroup = new MappingUserGroup();
      PstMappingUserGroup pstMappingusergroup = new PstMappingUserGroup(oid);
      entMappingUserGroup.setOID(oid);
      entMappingUserGroup.setUserId(pstMappingusergroup.getlong(FLD_USER_ID));
      entMappingUserGroup.setGroupUserId(pstMappingusergroup.getlong(FLD_GROUP_USER_ID));
      entMappingUserGroup.setCompanyId(pstMappingusergroup.getlong(FLD_COMPANY_ID));
      return entMappingUserGroup;
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstMappingUserGroup(0), DBException.UNKNOWN);
    }
  }

  public long fetchExc(Entity entity) throws Exception {
    MappingUserGroup entMappingUserGroup = fetchExc(entity.getOID());
    entity = (Entity) entMappingUserGroup;
    return entMappingUserGroup.getOID();
  }

  public static synchronized long updateExc(MappingUserGroup entMappingUserGroup) throws DBException {
    try {
      if (entMappingUserGroup.getOID() != 0) {
        PstMappingUserGroup pstMappingusergroup = new PstMappingUserGroup(entMappingUserGroup.getOID());
        pstMappingusergroup.setLong(FLD_USER_ID, entMappingUserGroup.getUserId());
        pstMappingusergroup.setLong(FLD_GROUP_USER_ID, entMappingUserGroup.getGroupUserId());
        pstMappingusergroup.setLong(FLD_COMPANY_ID, entMappingUserGroup.getCompanyId());
        pstMappingusergroup.update();
        return entMappingUserGroup.getOID();
      }
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstMappingUserGroup(0), DBException.UNKNOWN);
    }
    return 0;
  }

  public long updateExc(Entity entity) throws Exception {
    return updateExc((MappingUserGroup) entity);
  }

  public static synchronized long deleteExc(long oid) throws DBException {
    try {
      PstMappingUserGroup pstMappingusergroup = new PstMappingUserGroup(oid);
      pstMappingusergroup.delete();
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstMappingUserGroup(0), DBException.UNKNOWN);
    }
    return oid;
  }

  public long deleteExc(Entity entity) throws Exception {
    if (entity == null) {
      throw new DBException(this, DBException.RECORD_NOT_FOUND);
    }
    return deleteExc(entity.getOID());
  }

  public static synchronized long insertExc(MappingUserGroup entMappingUserGroup) throws DBException {
    try {
      PstMappingUserGroup pstMappingusergroup = new PstMappingUserGroup(0);
      pstMappingusergroup.setLong(FLD_USER_ID, entMappingUserGroup.getUserId());
      pstMappingusergroup.setLong(FLD_GROUP_USER_ID, entMappingUserGroup.getGroupUserId());
      pstMappingusergroup.setLong(FLD_COMPANY_ID, entMappingUserGroup.getCompanyId());
      pstMappingusergroup.insert();
      entMappingUserGroup.setOID(pstMappingusergroup.getlong(FLD_MAPPING_USER_GROUP_ID));
    } catch (DBException dbe) {
      throw dbe;
    } catch (Exception e) {
      throw new DBException(new PstMappingUserGroup(0), DBException.UNKNOWN);
    }
    return entMappingUserGroup.getOID();
  }

  public long insertExc(Entity entity) throws Exception {
    return insertExc((MappingUserGroup) entity);
  }

  public static void resultToObject(ResultSet rs, MappingUserGroup entMappingUserGroup) {
    try {
      entMappingUserGroup.setOID(rs.getLong(PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_MAPPING_USER_GROUP_ID]));
      entMappingUserGroup.setUserId(rs.getLong(PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_USER_ID]));
      entMappingUserGroup.setGroupUserId(rs.getLong(PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_GROUP_USER_ID]));
      entMappingUserGroup.setCompanyId(rs.getLong(PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_COMPANY_ID]));
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
      String sql = "SELECT * FROM " + TBL_MAPPING_USER_GROUP;
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
        MappingUserGroup entMappingUserGroup = new MappingUserGroup();
        resultToObject(rs, entMappingUserGroup);
        lists.add(entMappingUserGroup);
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

  public static boolean checkOID(long entMappingusergroupId) {
    DBResultSet dbrs = null;
    boolean result = false;
    try {
      String sql = "SELECT * FROM " + TBL_MAPPING_USER_GROUP + " WHERE "
              + PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_MAPPING_USER_GROUP_ID] + " = " + entMappingusergroupId;
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
      String sql = "SELECT COUNT(" + PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_MAPPING_USER_GROUP_ID] + ") FROM " + TBL_MAPPING_USER_GROUP;
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
          MappingUserGroup entMappingUserGroup = (MappingUserGroup) list.get(ls);
          if (oid == entMappingUserGroup.getOID()) {
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


    public static long deleteGroupByUser(long oid)
  {
    PstMappingUserGroup pstObj = new PstMappingUserGroup();
    DBResultSet dbrs = null;
    try {
      String sql = "DELETE FROM " + pstObj.getTableName()
              + " WHERE " + PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_USER_ID]
              + " = '" + oid + "' AND " + PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_COMPANY_ID] + " = 0";

      System.out.println(sql);
      int status = DBHandler.execUpdate(sql);
      return oid;
    } catch (Exception e) {
      System.out.println(e);
    } finally {
      DBResultSet.close(dbrs);
    }

    return 0;
  }


    public static long deleteCompByUser(long oid)
  {
    PstMappingUserGroup pstObj = new PstMappingUserGroup();
    DBResultSet dbrs = null;
    try {
      String sql = "DELETE FROM " + pstObj.getTableName()
              + " WHERE " + PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_USER_ID]
              + " = '" + oid + "' AND "+PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_GROUP_USER_ID]+" = 0";
      System.out.println(sql);
      int status = DBHandler.execUpdate(sql);
      return oid;
    } catch (Exception e) {
      System.out.println(e);
    } finally {
      DBResultSet.close(dbrs);
    }

    return 0;
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
