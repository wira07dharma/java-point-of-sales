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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import org.json.JSONObject;

/**
 *
 * @author Dimata 007
 */
public class PstEvent extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_EVENT = "pos_event";
    public static final int FLD_EVENT_OID = 0;
    public static final int FLD_EVENT_CODE = 1;
    public static final int FLD_EVENT_TITLE = 2;
    public static final int FLD_DESCRIPTION = 3;
    public static final int FLD_EVENT_DATETIME = 4;
    public static final int FLD_PRICE_TYPE_ID = 5;
    public static final int FLD_TAG_DEPOSIT = 6;
    public static final int FLD_COMPANY_ID = 7;
    public static final int FLD_EVENT_END_DATETIME = 8;
    public static final int FLD_CURRENCY_TYPE_ID = 9;
	public static final int FLD_REFUND_ENABLE = 10;
	public static final int FLD_LIMIT_REFUND_DATE = 11;
	public static final int FLD_REFUND_MODE = 12;
	public static final int FLD_LOCATION_ID = 13;

    public static String[] fieldNames = {
        "EVENT_OID",
        "EVENT_CODE",
        "EVENT_TITLE",
        "DESCRIPTION",
        "EVENT_DATETIME",
        "PRICE_TYPE_ID",
        "TAG_DEPOSIT",
        "COMPANY_ID",
        "EVENT_END_DATETIME",
        "CURRENCY_TYPE_ID",
		"REFUND_ENABLE",
		"LIMIT_REFUND_DATE",
		"REFUND_MODE",
		"LOCATION_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_LONG,
		TYPE_INT,
		TYPE_DATE,
		TYPE_INT,
		TYPE_LONG
    };
	
	public static final int MODE_ALL = 0;
	public static final int MODE_OFFLINE = 1;
	public static final int MODE_ONLINE = 2;
	
	public static final String[] strMode = {
		"All","Offline","Online"
	};

    public PstEvent() {
    }

    public PstEvent(int i) throws DBException {
        super(new PstEvent());
    }

    public PstEvent(String sOid) throws DBException {
        super(new PstEvent(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstEvent(long lOid) throws DBException {
        super(new PstEvent(0));
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
        return TBL_EVENT;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstEvent().getClass().getName();
    }

    public static Event fetchExc(long oid) throws DBException {
        try {
            Event entEvent = new Event();
            PstEvent pstEvent = new PstEvent(oid);
            entEvent.setOID(oid);
            entEvent.setEventCode(pstEvent.getString(FLD_EVENT_CODE));
            entEvent.setEventTitle(pstEvent.getString(FLD_EVENT_TITLE));
            entEvent.setDescription(pstEvent.getString(FLD_DESCRIPTION));
            entEvent.setEventDatetime(pstEvent.getDate(FLD_EVENT_DATETIME));
            entEvent.setPriceTypeId(pstEvent.getlong(FLD_PRICE_TYPE_ID));
            entEvent.setTagDeposit(pstEvent.getdouble(FLD_TAG_DEPOSIT));
            entEvent.setCompanyId(pstEvent.getlong(FLD_COMPANY_ID));
            entEvent.setEventEndDatetime(pstEvent.getDate(FLD_EVENT_END_DATETIME));
            entEvent.setCurrencyTypeId(pstEvent.getlong(FLD_CURRENCY_TYPE_ID));
			entEvent.setEnableRefund(pstEvent.getInt(FLD_REFUND_ENABLE));
			entEvent.setLimitRefund(pstEvent.getDate(FLD_LIMIT_REFUND_DATE));
			entEvent.setRefundMode(pstEvent.getInt(FLD_REFUND_MODE));
			entEvent.setLocationId(pstEvent.getlong(FLD_LOCATION_ID));
            return entEvent;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEvent(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        Event entEvent = fetchExc(entity.getOID());
        entity = (Entity) entEvent;
        return entEvent.getOID();
    }

    public static synchronized long updateExc(Event entEvent) throws DBException {
        try {
            if (entEvent.getOID() != 0) {
                PstEvent pstEvent = new PstEvent(entEvent.getOID());
                pstEvent.setString(FLD_EVENT_CODE, entEvent.getEventCode());
                pstEvent.setString(FLD_EVENT_TITLE, entEvent.getEventTitle());
                pstEvent.setString(FLD_DESCRIPTION, entEvent.getDescription());
                pstEvent.setDate(FLD_EVENT_DATETIME, entEvent.getEventDatetime());
                pstEvent.setLong(FLD_PRICE_TYPE_ID, entEvent.getPriceTypeId());
                pstEvent.setDouble(FLD_TAG_DEPOSIT, entEvent.getTagDeposit());
                pstEvent.setLong(FLD_COMPANY_ID, entEvent.getCompanyId());
                pstEvent.setDate(FLD_EVENT_END_DATETIME, entEvent.getEventEndDatetime());
                pstEvent.setLong(FLD_CURRENCY_TYPE_ID, entEvent.getCurrencyTypeId());
				pstEvent.setInt(FLD_REFUND_ENABLE, entEvent.getEnableRefund());
				pstEvent.setDate(FLD_LIMIT_REFUND_DATE, entEvent.getLimitRefund());
				pstEvent.setInt(FLD_REFUND_MODE, entEvent.getRefundMode());
				pstEvent.setLong(FLD_LOCATION_ID, entEvent.getLocationId());
                pstEvent.update();
                return entEvent.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEvent(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((Event) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstEvent pstEvent = new PstEvent(oid);
            pstEvent.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEvent(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(Event entEvent) throws DBException {
        try {
            PstEvent pstEvent = new PstEvent(0);
            pstEvent.setString(FLD_EVENT_CODE, entEvent.getEventCode());
            pstEvent.setString(FLD_EVENT_TITLE, entEvent.getEventTitle());
            pstEvent.setString(FLD_DESCRIPTION, entEvent.getDescription());
            pstEvent.setDate(FLD_EVENT_DATETIME, entEvent.getEventDatetime());
            pstEvent.setLong(FLD_PRICE_TYPE_ID, entEvent.getPriceTypeId());
            pstEvent.setDouble(FLD_TAG_DEPOSIT, entEvent.getTagDeposit());
            pstEvent.setLong(FLD_COMPANY_ID, entEvent.getCompanyId());
            pstEvent.setDate(FLD_EVENT_END_DATETIME, entEvent.getEventEndDatetime());
            pstEvent.setLong(FLD_CURRENCY_TYPE_ID, entEvent.getCurrencyTypeId());
			pstEvent.setInt(FLD_REFUND_ENABLE, entEvent.getEnableRefund());
			pstEvent.setDate(FLD_LIMIT_REFUND_DATE, entEvent.getLimitRefund());
			pstEvent.setInt(FLD_REFUND_MODE, entEvent.getRefundMode());
			pstEvent.setLong(FLD_LOCATION_ID, entEvent.getLocationId());
            pstEvent.insert();
            entEvent.setOID(pstEvent.getlong(FLD_EVENT_OID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEvent(0), DBException.UNKNOWN);
        }
        return entEvent.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((Event) entity);
    }

    public static void resultToObject(ResultSet rs, Event entEvent) {
        try {
            entEvent.setOID(rs.getLong(PstEvent.fieldNames[PstEvent.FLD_EVENT_OID]));
            entEvent.setEventCode(rs.getString(PstEvent.fieldNames[PstEvent.FLD_EVENT_CODE]));
            entEvent.setEventTitle(rs.getString(PstEvent.fieldNames[PstEvent.FLD_EVENT_TITLE]));
            entEvent.setDescription(rs.getString(PstEvent.fieldNames[PstEvent.FLD_DESCRIPTION]));
            entEvent.setEventDatetime(rs.getTimestamp(PstEvent.fieldNames[PstEvent.FLD_EVENT_DATETIME]));
            entEvent.setPriceTypeId(rs.getLong(PstEvent.fieldNames[PstEvent.FLD_PRICE_TYPE_ID]));
            entEvent.setTagDeposit(rs.getDouble(PstEvent.fieldNames[PstEvent.FLD_TAG_DEPOSIT]));
            entEvent.setCompanyId(rs.getLong(PstEvent.fieldNames[PstEvent.FLD_COMPANY_ID]));
            entEvent.setEventEndDatetime(rs.getTimestamp(PstEvent.fieldNames[PstEvent.FLD_EVENT_END_DATETIME]));
            entEvent.setCurrencyTypeId(rs.getLong(PstEvent.fieldNames[PstEvent.FLD_CURRENCY_TYPE_ID]));
			entEvent.setEnableRefund(rs.getInt(PstEvent.fieldNames[PstEvent.FLD_REFUND_ENABLE]));
			entEvent.setLimitRefund(rs.getDate(PstEvent.fieldNames[PstEvent.FLD_LIMIT_REFUND_DATE]));
			entEvent.setRefundMode(rs.getInt(PstEvent.fieldNames[PstEvent.FLD_REFUND_MODE]));
			entEvent.setLocationId(rs.getLong(PstEvent.fieldNames[PstEvent.FLD_LOCATION_ID]));
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
            String sql = "SELECT * FROM " + TBL_EVENT;
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
                Event entEvent = new Event();
                resultToObject(rs, entEvent);
                lists.add(entEvent);
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

    public static boolean checkOID(long entEventId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_EVENT + " WHERE "
                    + PstEvent.fieldNames[PstEvent.FLD_EVENT_OID] + " = " + entEventId;
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
            String sql = "SELECT COUNT(" + PstEvent.fieldNames[PstEvent.FLD_EVENT_OID] + ") FROM " + TBL_EVENT;
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
                    Event entEvent = (Event) list.get(ls);
                    if (oid == entEvent.getOID()) {
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

    public static ArrayList listEventItem(long eventId) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "
                    + "  m.MATERIAL_ID, "
                    + "  m.NAME, "
                    + "  m.SKU, "
                    + "  c.NAME AS CATEGORY, "
					+ "  p.NAME AS SUB_CATEGORY, "
                    + "  ei.ITEM_PRICE "
                    + " FROM "
                    + "  pos_event_item ei "
                    + "  INNER JOIN pos_material m "
                    + "    ON m.MATERIAL_ID = ei.MATERIAL_ID "
                    + "  INNER JOIN pos_category c "
                    + "    ON c.CATEGORY_ID = m.CATEGORY_ID "
					+ "  INNER JOIN pos_sub_category p "
                    + "    ON p.SUB_CATEGORY_ID = m.SUB_CATEGORY_ID "
                    + " WHERE ei.EVENT_OID = '" + eventId + "'"
                    + " ORDER BY c.NAME,p.NAME,m.NAME, m.SKU "
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            ArrayList lists = new ArrayList();
            while (rs.next()) {
                HashMap data = new HashMap();
                data.put("MATERIAL_ID", "" + rs.getLong("MATERIAL_ID"));
                data.put("MATERIAL_NAME", "" + rs.getString("NAME"));
                data.put("MATERIAL_SKU", "" + rs.getString("SKU"));
                data.put("CATEGORY_NAME", "" + rs.getString("CATEGORY"));
				data.put("SUB_CATEGORY_NAME", "" + rs.getString("SUB_CATEGORY"));
                data.put("ITEM_PRICE", "" + rs.getDouble("ITEM_PRICE"));
                lists.add(data);
            }
            rs.close();
            return lists;
        } catch (DBException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList();
    }

    public static int checkMappingEventItemExist(long eventId, long itemId) {
        DBResultSet dbrs = null;
        int result = 0;
        try {
            String sql = "SELECT * FROM pos_event_item "
                    + " WHERE EVENT_OID = '" + eventId + "'"
                    + " AND MATERIAL_ID = '" + itemId + "'"
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result++;
            }
            rs.close();
            return result;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static int saveEventItem(long eventId, long itemId, double price, long currencyTypeId) {
        int result = 0;
        try {
            String sql = " INSERT INTO pos_event_item (EVENT_OID, MATERIAL_ID, ITEM_PRICE, CURRENCY_TYPE_ID) "
                    + " VALUES(" + eventId + "," + itemId + "," + price + "," + currencyTypeId + ");";
            result = DBHandler.execUpdate(sql);
        } catch (NumberFormatException e) {
            System.out.println("err : " + e.toString());
        } catch (DBException e) {
            System.out.println("err : " + e.toString());
        }
        return result;
    }
	
	public static int updateEventPrice(long eventId, long itemId, double price) {
        int result = 0;
        try {
            String sql = " UPDATE pos_event_item SET ITEM_PRICE = '"+price+"' WHERE EVENT_OID = '"+eventId+"' "
					+ "AND MATERIAL_ID = '"+itemId+"';";
            result = DBHandler.execUpdate(sql);
        } catch (NumberFormatException e) {
            System.out.println("err : " + e.toString());
        } catch (DBException e) {
            System.out.println("err : " + e.toString());
        }
        return result;
    }

    public static void deleteEventItem(long eventId) {
        try {
            String sql = "DELETE FROM pos_event_item WHERE EVENT_OID = '" + eventId + "'";
            int result = DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        }
    }

    public static int deleteEventItem(long eventId, long itemId) {
        int result = 0;
        try {
            String sql = "DELETE FROM pos_event_item "
                    + " WHERE EVENT_OID = '" + eventId + "'"
                    + " AND MATERIAL_ID = '" + itemId + "'";
            result = DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println("err : " + e.getMessage());
        }
        return result;
    }

    public static ArrayList listMappingBarItem(long eventId) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "
                    + "  e.EVENT_OID, "
                    + "  e.EVENT_TITLE, "
                    + "  r.ROOM_ID, "
                    + "  r.NAME, "
                    + "  m.MATERIAL_ID, "
                    + "  m.NAME "
                    + " FROM "
                    + "  pos_event_room_item eri "
                    + "  INNER JOIN pos_event e "
                    + "    ON e.EVENT_OID = eri.EVENT_OID "
                    + "  INNER JOIN pos_room r "
                    + "    ON r.ROOM_ID = eri.ROOM_ID "
                    + "  INNER JOIN pos_material m "
                    + "    ON m.MATERIAL_ID = eri.MATERIAL_ID "
                    + " WHERE e.EVENT_OID = '" + eventId + "' "
                    + " ORDER BY e.EVENT_CODE, "
                    + "  r.NAME, "
                    + "  m.NAME"
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            ArrayList lists = new ArrayList();
            while (rs.next()) {
                HashMap data = new HashMap();
                data.put("EVENT_ID", "" + rs.getString("EVENT_OID"));
                data.put("EVENT_TITLE", "" + rs.getString("EVENT_TITLE"));
                data.put("ROOM_ID", "" + rs.getString("r.ROOM_ID"));
                data.put("ROOM_NAME", "" + rs.getString("r.NAME"));
                data.put("ITEM_ID", "" + rs.getString("m.MATERIAL_ID"));
                data.put("ITEM_NAME", "" + rs.getString("m.NAME"));
                lists.add(data);
            }
            rs.close();
            return lists;
        } catch (DBException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList();
    }

    public static int checkMappingBarItemExist(long roomId, long eventId, long itemId) {
        DBResultSet dbrs = null;
        int result = 0;
        try {
            String sql = "SELECT * FROM pos_event_room_item "
                    + " WHERE ROOM_ID = '" + roomId + "'"
                    + " AND EVENT_OID = '" + eventId + "'"
                    + " AND MATERIAL_ID = '" + itemId + "'"
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result++;
            }
            rs.close();
            return result;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static int checkMappingBarItemExist(long eventId, long itemId) {
        DBResultSet dbrs = null;
        int result = 0;
        try {
            String sql = "SELECT * FROM pos_event_room_item "
                    + " WHERE EVENT_OID = '" + eventId + "'"
                    + " AND MATERIAL_ID = '" + itemId + "'"
                    + "";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result++;
            }
            rs.close();
            return result;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static int saveMappingBarItem(long roomId, long eventId, long itemId) {
        int result = 0;
        try {
            String sql = " INSERT INTO pos_event_room_item (ROOM_ID, EVENT_OID, MATERIAL_ID) "
                    + " VALUES(" + roomId + "," + eventId + "," + itemId + ");";
            result = DBHandler.execUpdate(sql);
        } catch (NumberFormatException e) {
            System.out.println("err : " + e.toString());
        } catch (DBException e) {
            System.out.println("err : " + e.toString());
        }
        return result;
    }

    public static int deleteMappingBarItem(long eventId) {
        int result = 0;
        try {
            String sql = "DELETE FROM pos_event_room_item "
                    + " WHERE EVENT_OID = '" + eventId + "'"
                    + "";
            result = DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println("err : " + e.getMessage());
        }
        return result;
    }
    
    public static int deleteMappingBarItem(long eventId, long roomId, long itemId) {
        int result = 0;
        try {
            String sql = "DELETE FROM pos_event_room_item "
                    + " WHERE EVENT_OID = '" + eventId + "'"
                    + " AND ROOM_ID = '" + roomId + "'"
                    + " AND MATERIAL_ID = '" + itemId + "'";
            result = DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println("err : " + e.getMessage());
        }
        return result;
    }
  
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                Event event = PstEvent.fetchExc(oid);
                object.put(PstEvent.fieldNames[PstEvent.FLD_EVENT_OID], event.getOID());
                object.put(PstEvent.fieldNames[PstEvent.FLD_COMPANY_ID], event.getCompanyId());
                object.put(PstEvent.fieldNames[PstEvent.FLD_CURRENCY_TYPE_ID], event.getCurrencyTypeId());
                object.put(PstEvent.fieldNames[PstEvent.FLD_DESCRIPTION], event.getDescription());
                object.put(PstEvent.fieldNames[PstEvent.FLD_EVENT_CODE], event.getEventCode());
                object.put(PstEvent.fieldNames[PstEvent.FLD_EVENT_DATETIME], event.getEventDatetime());
                object.put(PstEvent.fieldNames[PstEvent.FLD_EVENT_END_DATETIME], event.getEventEndDatetime());
                object.put(PstEvent.fieldNames[PstEvent.FLD_EVENT_TITLE], event.getEventTitle());
                object.put(PstEvent.fieldNames[PstEvent.FLD_LIMIT_REFUND_DATE], event.getLimitRefund());
                object.put(PstEvent.fieldNames[PstEvent.FLD_LOCATION_ID], event.getLocationId());
                object.put(PstEvent.fieldNames[PstEvent.FLD_PRICE_TYPE_ID], event.getPriceTypeId());
                object.put(PstEvent.fieldNames[PstEvent.FLD_REFUND_ENABLE], event.getEnableRefund());
                object.put(PstEvent.fieldNames[PstEvent.FLD_REFUND_MODE], event.getRefundMode());
                object.put(PstEvent.fieldNames[PstEvent.FLD_TAG_DEPOSIT], event.getTagDeposit());
            }catch(Exception exc){}

            return object;
        }
}
