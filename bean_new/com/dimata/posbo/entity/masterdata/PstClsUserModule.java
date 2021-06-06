/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author IanRizky
 */

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;
import org.json.JSONObject;

public class PstClsUserModule extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

	public static final String TBL_CLS_USER_MODULE = "pos_cls_user_module";
	public static final int FLD_CLS_USER_MODULE_ID = 0;
	public static final int FLD_USER_ID = 1;
	public static final int FLD_EVENT_OID = 2;
	public static final int FLD_ID_MODUL_ENABLE = 3;

	public static String[] fieldNames = {
		"CLS_USER_MODULE_ID",
		"USER_ID",
		"EVENT_OID",
		"ID_MODUL_ENABLE"
	};

	public static int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_INT
	};

	public PstClsUserModule() {
	}

	public PstClsUserModule(int i) throws DBException {
		super(new PstClsUserModule());
	}

	public PstClsUserModule(String sOid) throws DBException {
		super(new PstClsUserModule(0));
		if (!locate(sOid)) {
			throw new DBException(this, DBException.RECORD_NOT_FOUND);
		} else {
			return;
		}
	}

	public PstClsUserModule(long lOid) throws DBException {
		super(new PstClsUserModule(0));
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
		return TBL_CLS_USER_MODULE;
	}

	public String[] getFieldNames() {
		return fieldNames;
	}

	public int[] getFieldTypes() {
		return fieldTypes;
	}

	public String getPersistentName() {
		return new PstClsUserModule().getClass().getName();
	}

	public static ClsUserModule fetchExc(long oid) throws DBException {
		try {
			ClsUserModule entClsUserModule = new ClsUserModule();
			PstClsUserModule pstClsUserModule = new PstClsUserModule(oid);
			entClsUserModule.setOID(oid);
			entClsUserModule.setUserId(pstClsUserModule.getLong(FLD_USER_ID));
			entClsUserModule.setEventOID(pstClsUserModule.getLong(FLD_EVENT_OID));
			entClsUserModule.setIdModulEnable(pstClsUserModule.getInt(FLD_ID_MODUL_ENABLE));
			return entClsUserModule;
		} catch (DBException dbe) {
			throw dbe;
		} catch (Exception e) {
			throw new DBException(new PstClsUserModule(0), DBException.UNKNOWN);
		}
	}

	public long fetchExc(Entity entity) throws Exception {
		ClsUserModule entClsUserModule = fetchExc(entity.getOID());
		entity = (Entity) entClsUserModule;
		return entClsUserModule.getOID();
	}

	public static synchronized long updateExc(ClsUserModule entClsUserModule) throws DBException {
		try {
			if (entClsUserModule.getOID() != 0) {
				PstClsUserModule pstClsUserModule = new PstClsUserModule(entClsUserModule.getOID());
				pstClsUserModule.setLong(FLD_USER_ID, entClsUserModule.getUserId());
				pstClsUserModule.setLong(FLD_EVENT_OID, entClsUserModule.getEventOID());
				pstClsUserModule.setInt(FLD_ID_MODUL_ENABLE, entClsUserModule.getIdModulEnable());
				pstClsUserModule.update();
				return entClsUserModule.getOID();
			}
		} catch (DBException dbe) {
			throw dbe;
		} catch (Exception e) {
			throw new DBException(new PstClsUserModule(0), DBException.UNKNOWN);
		}
		return 0;
	}

	public long updateExc(Entity entity) throws Exception {
		return updateExc((ClsUserModule) entity);
	}

	public static synchronized long deleteExc(long oid) throws DBException {
		try {
			PstClsUserModule pstClsUserModule = new PstClsUserModule(oid);
			pstClsUserModule.delete();
		} catch (DBException dbe) {
			throw dbe;
		} catch (Exception e) {
			throw new DBException(new PstClsUserModule(0), DBException.UNKNOWN);
		}
		return oid;
	}

	public long deleteExc(Entity entity) throws Exception {
		if (entity == null) {
			throw new DBException(this, DBException.RECORD_NOT_FOUND);
		}
		return deleteExc(entity.getOID());
	}

	public static synchronized long insertExc(ClsUserModule entClsUserModule) throws DBException {
		try {
			PstClsUserModule pstClsUserModule = new PstClsUserModule(0);
			pstClsUserModule.setLong(FLD_USER_ID, entClsUserModule.getUserId());
			pstClsUserModule.setLong(FLD_EVENT_OID, entClsUserModule.getEventOID());
			pstClsUserModule.setInt(FLD_ID_MODUL_ENABLE, entClsUserModule.getIdModulEnable());
			pstClsUserModule.insert();
			entClsUserModule.setOID(pstClsUserModule.getlong(FLD_CLS_USER_MODULE_ID));
		} catch (DBException dbe) {
			throw dbe;
		} catch (Exception e) {
			throw new DBException(new PstClsUserModule(0), DBException.UNKNOWN);
		}
		return entClsUserModule.getOID();
	}

	public long insertExc(Entity entity) throws Exception {
		return insertExc((ClsUserModule) entity);
	}

	public static void resultToObject(ResultSet rs, ClsUserModule entClsUserModule) {
		try {
			entClsUserModule.setOID(rs.getLong(PstClsUserModule.fieldNames[PstClsUserModule.FLD_CLS_USER_MODULE_ID]));
			entClsUserModule.setUserId(rs.getLong(PstClsUserModule.fieldNames[PstClsUserModule.FLD_USER_ID]));
			entClsUserModule.setEventOID(rs.getLong(PstClsUserModule.fieldNames[PstClsUserModule.FLD_EVENT_OID]));
			entClsUserModule.setIdModulEnable(rs.getInt(PstClsUserModule.fieldNames[PstClsUserModule.FLD_ID_MODUL_ENABLE]));
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
			String sql = "SELECT * FROM " + TBL_CLS_USER_MODULE;
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
				ClsUserModule entClsUserModule = new ClsUserModule();
				resultToObject(rs, entClsUserModule);
				lists.add(entClsUserModule);
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
	
	public static Vector listUser(int limitStart, int recordToGet, String whereClause, String order) {
		Vector lists = new Vector();
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT DISTINCT "+fieldNames[FLD_USER_ID]+" FROM " + TBL_CLS_USER_MODULE;
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
				long id = rs.getLong(1);
				lists.add(id);
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

	public static boolean checkOID(long entClsUserModuleId) {
		DBResultSet dbrs = null;
		boolean result = false;
		try {
			String sql = "SELECT * FROM " + TBL_CLS_USER_MODULE + " WHERE "
					+ PstClsUserModule.fieldNames[PstClsUserModule.FLD_CLS_USER_MODULE_ID] + " = " + entClsUserModuleId;
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
			String sql = "SELECT COUNT(" + PstClsUserModule.fieldNames[PstClsUserModule.FLD_CLS_USER_MODULE_ID] + ") FROM " + TBL_CLS_USER_MODULE;
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
					ClsUserModule entClsUserModule = (ClsUserModule) list.get(ls);
					if (oid == entClsUserModule.getOID()) {
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
		} else if (start == (vectSize - recordToGet)) {
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
		return cmd;
	}
        
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                ClsUserModule clsUserModule = PstClsUserModule.fetchExc(oid);
                object.put(PstClsUserModule.fieldNames[PstClsUserModule.FLD_CLS_USER_MODULE_ID], clsUserModule.getOID());
                object.put(PstClsUserModule.fieldNames[PstClsUserModule.FLD_EVENT_OID], clsUserModule.getEventOID());
                object.put(PstClsUserModule.fieldNames[PstClsUserModule.FLD_ID_MODUL_ENABLE], clsUserModule.getIdModulEnable());
                object.put(PstClsUserModule.fieldNames[PstClsUserModule.FLD_USER_ID], clsUserModule.getUserId());
            }catch(Exception exc){}

            return object;
        }
}

