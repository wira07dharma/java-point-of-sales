/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;
import org.json.JSONObject;

/**
 *
 * @author IanRizky
 */
public class PstClsSpending extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

	public static final String TBL_CLSSPENDING = "pos_cls_spending";
	public static final int FLD_SPENDING_ID = 0;
	public static final int FLD_TICKET_NAME = 1;
	public static final int FLD_PRICE = 2;
	public static final int FLD_MINIMUM_SPENDING = 3;
	public static final int FLD_DETAILS = 4;
	public static final int FLD_VALUE = 5;
	public static final int FLD_EVENT_OID = 6;

	public static String[] fieldNames = {
		"CLS_SPENDING_ID",
		"TICKET_NAME",
		"PRICE",
		"MINIMUM_SPENDING",
		"DETAILS",
		"VALUE",
		"EVENT_OID"
	};

	public static int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING,
		TYPE_FLOAT,
		TYPE_FLOAT,
		TYPE_STRING,
		TYPE_FLOAT,
		TYPE_LONG
	};

	public PstClsSpending() {
	}

	public PstClsSpending(int i) throws DBException {
		super(new PstClsSpending());
	}

	public PstClsSpending(String sOid) throws DBException {
		super(new PstClsSpending(0));
		if (!locate(sOid)) {
			throw new DBException(this, DBException.RECORD_NOT_FOUND);
		} else {
			return;
		}
	}

	public PstClsSpending(long lOid) throws DBException {
		super(new PstClsSpending(0));
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
		return TBL_CLSSPENDING;
	}

	public String[] getFieldNames() {
		return fieldNames;
	}

	public int[] getFieldTypes() {
		return fieldTypes;
	}

	public String getPersistentName() {
		return new PstClsSpending().getClass().getName();
	}

	public static ClsSpending fetchExc(long oid) throws DBException {
		try {
			ClsSpending entClsSpending = new ClsSpending();
			PstClsSpending pstClsSpending = new PstClsSpending(oid);
			entClsSpending.setOID(oid);
			entClsSpending.setTicketName(pstClsSpending.getString(FLD_TICKET_NAME));
			entClsSpending.setPrice(pstClsSpending.getdouble(FLD_PRICE));
			entClsSpending.setMinimumSpending(pstClsSpending.getdouble(FLD_MINIMUM_SPENDING));
			entClsSpending.setDetails(pstClsSpending.getString(FLD_DETAILS));
			entClsSpending.setValue(pstClsSpending.getdouble(FLD_VALUE));
			entClsSpending.setEventOid(pstClsSpending.getlong(FLD_EVENT_OID));
			return entClsSpending;
		} catch (DBException dbe) {
			throw dbe;
		} catch (Exception e) {
			throw new DBException(new PstClsSpending(0), DBException.UNKNOWN);
		}
	}

	public long fetchExc(Entity entity) throws Exception {
		ClsSpending entClsSpending = fetchExc(entity.getOID());
		entity = (Entity) entClsSpending;
		return entClsSpending.getOID();
	}

	public static synchronized long updateExc(ClsSpending entClsSpending) throws DBException {
		try {
			if (entClsSpending.getOID() != 0) {
				PstClsSpending pstClsSpending = new PstClsSpending(entClsSpending.getOID());
				pstClsSpending.setString(FLD_TICKET_NAME, entClsSpending.getTicketName());
				pstClsSpending.setDouble(FLD_PRICE, entClsSpending.getPrice());
				pstClsSpending.setDouble(FLD_MINIMUM_SPENDING, entClsSpending.getMinimumSpending());
				pstClsSpending.setString(FLD_DETAILS, entClsSpending.getDetails());
				pstClsSpending.setDouble(FLD_VALUE, entClsSpending.getValue());
				pstClsSpending.setLong(FLD_EVENT_OID, entClsSpending.getEventOid());
				pstClsSpending.update();
				return entClsSpending.getOID();
			}
		} catch (DBException dbe) {
			throw dbe;
		} catch (Exception e) {
			throw new DBException(new PstClsSpending(0), DBException.UNKNOWN);
		}
		return 0;
	}

	public long updateExc(Entity entity) throws Exception {
		return updateExc((ClsSpending) entity);
	}

	public static synchronized long deleteExc(long oid) throws DBException {
		try {
			PstClsSpending pstClsSpending = new PstClsSpending(oid);
			pstClsSpending.delete();
		} catch (DBException dbe) {
			throw dbe;
		} catch (Exception e) {
			throw new DBException(new PstClsSpending(0), DBException.UNKNOWN);
		}
		return oid;
	}

	public long deleteExc(Entity entity) throws Exception {
		if (entity == null) {
			throw new DBException(this, DBException.RECORD_NOT_FOUND);
		}
		return deleteExc(entity.getOID());
	}

	public static synchronized long insertExc(ClsSpending entClsSpending) throws DBException {
		try {
			PstClsSpending pstClsSpending = new PstClsSpending(0);
			pstClsSpending.setString(FLD_TICKET_NAME, entClsSpending.getTicketName());
			pstClsSpending.setDouble(FLD_PRICE, entClsSpending.getPrice());
			pstClsSpending.setDouble(FLD_MINIMUM_SPENDING, entClsSpending.getMinimumSpending());
			pstClsSpending.setString(FLD_DETAILS, entClsSpending.getDetails());
			pstClsSpending.setDouble(FLD_VALUE, entClsSpending.getValue());
			pstClsSpending.setLong(FLD_EVENT_OID, entClsSpending.getEventOid());
			pstClsSpending.insert();
			entClsSpending.setOID(pstClsSpending.getLong(FLD_SPENDING_ID));
		} catch (DBException dbe) {
			throw dbe;
		} catch (Exception e) {
			throw new DBException(new PstClsSpending(0), DBException.UNKNOWN);
		}
		return entClsSpending.getOID();
	}

	public long insertExc(Entity entity) throws Exception {
		return insertExc((ClsSpending) entity);
	}

	public static void resultToObject(ResultSet rs, ClsSpending entClsSpending) {
		try {
			entClsSpending.setOID(rs.getLong(PstClsSpending.fieldNames[PstClsSpending.FLD_SPENDING_ID]));
			entClsSpending.setTicketName(rs.getString(PstClsSpending.fieldNames[PstClsSpending.FLD_TICKET_NAME]));
			entClsSpending.setPrice(rs.getDouble(PstClsSpending.fieldNames[PstClsSpending.FLD_PRICE]));
			entClsSpending.setMinimumSpending(rs.getDouble(PstClsSpending.fieldNames[PstClsSpending.FLD_MINIMUM_SPENDING]));
			entClsSpending.setDetails(rs.getString(PstClsSpending.fieldNames[PstClsSpending.FLD_DETAILS]));
			entClsSpending.setValue(rs.getDouble(PstClsSpending.fieldNames[PstClsSpending.FLD_VALUE]));
			entClsSpending.setEventOid(rs.getLong(PstClsSpending.fieldNames[PstClsSpending.FLD_EVENT_OID]));
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
			String sql = "SELECT * FROM " + TBL_CLSSPENDING;
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
				ClsSpending entClsSpending = new ClsSpending();
				resultToObject(rs, entClsSpending);
				lists.add(entClsSpending);
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

	public static boolean checkOID(long entClsSpendingId) {
		DBResultSet dbrs = null;
		boolean result = false;
		try {
			String sql = "SELECT * FROM " + TBL_CLSSPENDING + " WHERE "
					+ PstClsSpending.fieldNames[PstClsSpending.FLD_SPENDING_ID] + " = " + entClsSpendingId;
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
			String sql = "SELECT COUNT(" + PstClsSpending.fieldNames[PstClsSpending.FLD_SPENDING_ID] + ") FROM " + TBL_CLSSPENDING;
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
					ClsSpending entClsSpending = (ClsSpending) list.get(ls);
					if (oid == entClsSpending.getOID()) {
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
                ClsSpending clsSpending = PstClsSpending.fetchExc(oid);
                object.put(PstClsSpending.fieldNames[PstClsSpending.FLD_SPENDING_ID], clsSpending.getOID());
                object.put(PstClsSpending.fieldNames[PstClsSpending.FLD_DETAILS], clsSpending.getDetails());
                object.put(PstClsSpending.fieldNames[PstClsSpending.FLD_EVENT_OID], clsSpending.getEventOid());
                object.put(PstClsSpending.fieldNames[PstClsSpending.FLD_MINIMUM_SPENDING], clsSpending.getMinimumSpending());
                object.put(PstClsSpending.fieldNames[PstClsSpending.FLD_TICKET_NAME], clsSpending.getTicketName());
                object.put(PstClsSpending.fieldNames[PstClsSpending.FLD_VALUE], clsSpending.getValue());
                object.put(PstClsSpending.fieldNames[PstClsSpending.FLD_PRICE], clsSpending.getPrice());
            }catch(Exception exc){}

            return object;
        }
    
    
}