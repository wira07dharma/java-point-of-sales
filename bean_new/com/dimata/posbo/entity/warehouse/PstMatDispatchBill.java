/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Unit;
import static com.dimata.posbo.entity.warehouse.PstMatReceiveItem.FLD_CURRENCY_ID;
import static com.dimata.posbo.entity.warehouse.PstMatReceiveItem.FLD_UNIT_ID;
import static com.dimata.posbo.entity.warehouse.PstMatReceiveItem.fieldNames;
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PstMatDispatchBill extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

	public static final String TBL_MATDISPATCHBILL = "pos_dispatch_material_bill";
	public static final int FLD_POS_DISPATCH_MATERIAL_ID = 0;
	public static final int FLD_DISPATCH_MATERIAL_ITEM_ID = 1;
	public static final int FLD_CASH_BILL_MAIN_ID = 2;
	public static final int FLD_STATUS = 3;

	public static String[] fieldNames = {
		"POS_DISPATCH_MATERIAL_ID",
		"DISPATCH_MATERIAL_ITEM_ID",
		"CASH_BILL_MAIN_ID",
		"STATUS"
	};

	public static int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_INT
	};

	public static String[] statusNames = {
		"In Transit", "Received", "Taken", "Not Taken"
	};
	
	public PstMatDispatchBill() {
	}

	public PstMatDispatchBill(int i) throws DBException {
		super(new PstMatDispatchBill());
	}

	public PstMatDispatchBill(String sOid) throws DBException {
		super(new PstMatDispatchBill(0));
		if (!locate(sOid)) {
			throw new DBException(this, DBException.RECORD_NOT_FOUND);
		} else {
			return;
		}
	}

	public PstMatDispatchBill(long lOid) throws DBException {
		super(new PstMatDispatchBill(0));
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
		return TBL_MATDISPATCHBILL;
	}

	public String[] getFieldNames() {
		return fieldNames;
	}

	public int[] getFieldTypes() {
		return fieldTypes;
	}

	public String getPersistentName() {
		return new PstMatDispatchBill().getClass().getName();
	}

	public static MatDispatchBill fetchExc(long oid) throws DBException {
		try {
			MatDispatchBill entMatDispatchBill = new MatDispatchBill();
			PstMatDispatchBill pstMatDispatchBill = new PstMatDispatchBill(oid);
			entMatDispatchBill.setOID(oid);
			entMatDispatchBill.setCashBillOid(pstMatDispatchBill.getLong(FLD_CASH_BILL_MAIN_ID));
			entMatDispatchBill.setStatus(pstMatDispatchBill.getInt(FLD_STATUS));
			entMatDispatchBill.setMatItemOid(pstMatDispatchBill.getLong(FLD_DISPATCH_MATERIAL_ITEM_ID));
			return entMatDispatchBill;
		} catch (DBException dbe) {
			throw dbe;
		} catch (Exception e) {
			throw new DBException(new PstMatDispatchBill(0), DBException.UNKNOWN);
		}
	}

	public long fetchExc(Entity entity) throws Exception {
		MatDispatchBill entMatDispatchBill = fetchExc(entity.getOID());
		entity = (Entity) entMatDispatchBill;
		return entMatDispatchBill.getOID();
	}

	public static synchronized long updateExc(MatDispatchBill entMatDispatchBill) throws DBException {
		try {
			if (entMatDispatchBill.getOID() != 0) {
				PstMatDispatchBill pstMatDispatchBill = new PstMatDispatchBill(entMatDispatchBill.getOID());
				pstMatDispatchBill.setLong(FLD_CASH_BILL_MAIN_ID, entMatDispatchBill.getCashBillOid());
				pstMatDispatchBill.setInt(FLD_STATUS, entMatDispatchBill.getStatus());
				pstMatDispatchBill.setLong(FLD_DISPATCH_MATERIAL_ITEM_ID, entMatDispatchBill.getMatItemOid());
				pstMatDispatchBill.update();
				return entMatDispatchBill.getOID();
			}
		} catch (DBException dbe) {
			throw dbe;
		} catch (Exception e) {
			throw new DBException(new PstMatDispatchBill(0), DBException.UNKNOWN);
		}
		return 0;
	}

	public long updateExc(Entity entity) throws Exception {
		return updateExc((MatDispatchBill) entity);
	}

	public static synchronized long deleteExc(long oid) throws DBException {
		try {
			PstMatDispatchBill pstMatDispatchBill = new PstMatDispatchBill(oid);
			pstMatDispatchBill.delete();
		} catch (DBException dbe) {
			throw dbe;
		} catch (Exception e) {
			throw new DBException(new PstMatDispatchBill(0), DBException.UNKNOWN);
		}
		return oid;
	}

	public long deleteExc(Entity entity) throws Exception {
		if (entity == null) {
			throw new DBException(this, DBException.RECORD_NOT_FOUND);
		}
		return deleteExc(entity.getOID());
	}

	public static synchronized long insertExc(MatDispatchBill entMatDispatchBill) throws DBException {
		try {
			PstMatDispatchBill pstMatDispatchBill = new PstMatDispatchBill(0);
			pstMatDispatchBill.setLong(FLD_CASH_BILL_MAIN_ID, entMatDispatchBill.getCashBillOid());
			pstMatDispatchBill.setInt(FLD_STATUS, entMatDispatchBill.getStatus());
			pstMatDispatchBill.setLong(FLD_DISPATCH_MATERIAL_ITEM_ID, entMatDispatchBill.getMatItemOid());
			pstMatDispatchBill.insert();
			entMatDispatchBill.setOID(pstMatDispatchBill.getLong(FLD_POS_DISPATCH_MATERIAL_ID));
		} catch (DBException dbe) {
			throw dbe;
		} catch (Exception e) {
			throw new DBException(new PstMatDispatchBill(0), DBException.UNKNOWN);
		}
		return entMatDispatchBill.getOID();
	}

	public long insertExc(Entity entity) throws Exception {
		return insertExc((MatDispatchBill) entity);
	}

	public static void resultToObject(ResultSet rs, MatDispatchBill entMatDispatchBill) {
		try {
			entMatDispatchBill.setOID(rs.getLong(PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_POS_DISPATCH_MATERIAL_ID]));
			entMatDispatchBill.setCashBillOid(rs.getLong(PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_CASH_BILL_MAIN_ID]));
			entMatDispatchBill.setMatItemOid(rs.getLong(PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_DISPATCH_MATERIAL_ITEM_ID]));
			entMatDispatchBill.setStatus(rs.getInt(PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_STATUS]));
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
			String sql = "SELECT * FROM " + TBL_MATDISPATCHBILL;
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
				MatDispatchBill entMatDispatchBill = new MatDispatchBill();
				resultToObject(rs, entMatDispatchBill);
				lists.add(entMatDispatchBill);
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

	public static Vector getListInvoices(int limitStart, int recordToGet, String whereClause, String order){
		Vector result = new Vector(1,1);
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT DISTINCT CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
					+ " , DMB." + PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_POS_DISPATCH_MATERIAL_ID]
					+ " , CBM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] 
//					+ " , DMB." + PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_STATUS]
					+ " , CBM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS]
					+ " FROM " + PstMatDispatchBill.TBL_MATDISPATCHBILL + " AS DMB"
					+ " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " AS CBM"
					+ " ON DMB." + PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_CASH_BILL_MAIN_ID]
					+ " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
					+ " WHERE DMB." + PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_CASH_BILL_MAIN_ID]
					+ " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID];
			if (whereClause != null && whereClause.length() > 0) {
				sql = sql + " AND " + whereClause;
			}
			if (order != null && order.length() > 0) {
				sql = sql + " ORDER BY " + order;
			}
			if (limitStart == 0 && recordToGet == 0) {
				sql = sql + "";
			} else {
				sql = sql + " LIMIT " + limitStart + "," + recordToGet;
			}
			System.out.println(sql);
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while (rs.next()) {
				Vector tempList = new Vector(1,1);
				MatDispatchBill mdb = new MatDispatchBill();
				BillMain bm = new BillMain();
				mdb.setOID(rs.getLong(PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_POS_DISPATCH_MATERIAL_ID]));
				mdb.setCashBillOid(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]));
				mdb.setStatus(rs.getInt(PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_STATUS]));
				bm.setInvoiceNumber(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]));
				bm.setOID(mdb.getCashBillOid());
				bm.setStatus(rs.getInt(PstBillMain.fieldNames[PstBillMain.FLD_STATUS]));
				
				tempList.add(mdb);
				tempList.add(bm);
				
				result.add(tempList);
			}
			rs.close();
			
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			DBResultSet.close(dbrs);
		}		
		return result;
	}
	
	public static Vector getInvoiceDetail(int limitStart, int recordToGet, String whereClause, String order, long oid){
		Vector result = new Vector(1,1);
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT DISTINCT"
					+ " CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_SKU]
					+ " , CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME]
					+ " , CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]
					+ " , CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE]
					+ " , CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE]
					+ " FROM "+ PstBillDetail.TBL_CASH_BILL_DETAIL +" AS CBD"
					+ " WHERE CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
					+ " =" + oid;
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
			System.out.println(sql);
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while (rs.next()) {
				Billdetail bd = new Billdetail();
				
				bd.setSku(rs.getString(PstBillDetail.fieldNames[PstBillDetail.FLD_SKU]));
				bd.setItemName(rs.getString(PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME]));
				bd.setQty(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]));
				bd.setItemPrice(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE]));
				bd.setTotalPrice(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE]));
				
				result.add(bd);
			}
			rs.close();
			
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			DBResultSet.close(dbrs);
		}		
		return result;
	}
	
	public static JSONObject listInvoices(int limitStart, int recordToGet, String whereClause, String order){
		JSONObject result = new JSONObject();
		JSONArray subResult = new JSONArray();
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT DISTINCT CBM.INVOICE_NUMBER, CBD.QTY,"
					+ " DMB.`STATUS`, CBM.CASH_BILL_MAIN_ID, DMB.POS_DISPATCH_MATERIAL_ID"
					+ " FROM "+ PstMatDispatchBill.TBL_MATDISPATCHBILL +" AS DMB"
					+ " INNER JOIN "+ PstBillMain.TBL_CASH_BILL_MAIN +" AS CBM"
					+ " ON DMB."+ PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_CASH_BILL_MAIN_ID] 
					+ " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
					+ " INNER JOIN "+ PstBillDetail.TBL_CASH_BILL_DETAIL +" AS CBD"
					+ " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
					+ " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
					+ " WHERE DMB."+ PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_CASH_BILL_MAIN_ID]
					+ " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
					+ " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
					+ " = CBD." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID];
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
			int i = 1;
			while (rs.next()) {
				JSONArray jaObj = new JSONArray();
				jaObj.put(i);
				jaObj.put(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]));
				jaObj.put(rs.getInt(PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]));
				jaObj.put(statusNames[rs.getInt(PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_STATUS])]);
				
				String button = "<button class=\"btn btn-primary text-center\">"
								+ "<i class=\"fa fa-address-book\"></i>"
								+ "Update Status"
								+ "</button>";
				jaObj.put(button);
				subResult.put(jaObj);
				i++;
			}
			rs.close();
			
			try {
				result.put("iTotalRecords", i);
				result.put("iTotalDisplayRecords", i);
				result.put("aaData", subResult);
			} catch (JSONException e) {
			}
			
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			DBResultSet.close(dbrs);
		}		
		return result;
	}
	
	public static boolean checkOID(long entMatDispatchBillId) {
		DBResultSet dbrs = null;
		boolean result = false;
		try {
			String sql = "SELECT * FROM " + TBL_MATDISPATCHBILL + " WHERE "
					+ PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_POS_DISPATCH_MATERIAL_ID] + " = " + entMatDispatchBillId;
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
			String sql = "SELECT COUNT(" + PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_POS_DISPATCH_MATERIAL_ID] + ") FROM " + TBL_MATDISPATCHBILL;
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
					MatDispatchBill entMatDispatchBill = (MatDispatchBill) list.get(ls);
					if (oid == entMatDispatchBill.getOID()) {
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
	
	public static Vector listDispatchItemCompleteDutyFree(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                         " , UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_REQUIRED_SERIAL_NUMBER] +
                         " , UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                         " , CUR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_KADAR_ID] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_COLOR_ID] +
                         " , DIS." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_JENIS_DOKUMEN] +
						 " , DIS." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_NOMOR_BC] +
						 " , CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY] +
						 " , CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
						 " , DET." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] +
                         " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL + " DET" +
                         " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " BILL" +
                         " ON DET." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                         " = BILL." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
						 " INNER JOIN " +TBL_MATDISPATCHBILL + " MAP" +
                         " ON MAP." + fieldNames[FLD_CASH_BILL_MAIN_ID] +
                         " = BILL." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
						 " INNER JOIN " + PstMatReceive.TBL_MAT_RECEIVE + " DIS" +
                         " ON MAP." + fieldNames[FLD_POS_DISPATCH_MATERIAL_ID] +
                         " = DIS." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
						 " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                         " ON DET." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                         " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                         " INNER JOIN " + PstCategory.TBL_CATEGORY + " CAT" +
                         " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                         " = CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                         " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
                         " ON RMI." + fieldNames[FLD_UNIT_ID] +
                         " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +

                         " LEFT JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE+ " CUR" +
                         " ON RMI." + fieldNames[FLD_CURRENCY_ID] +

                         " = CUR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID];

            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    ;
            }

            System.out.println("SQL : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector temp = new Vector();
                MatReceiveItem matreceiveitem = new MatReceiveItem();
                Material mat = new Material();
                Unit unit = new Unit();
                CurrencyType curr = new CurrencyType();
				MatDispatch dispatch = new MatDispatch();
				Category cat = new Category();
				Billdetail det = new Billdetail();

                det.setQty(rs.getDouble(20));
                temp.add(det);

                mat.setSku(rs.getString(1));
                mat.setName(rs.getString(2));
                mat.setDefaultPrice(rs.getDouble(4));
                mat.setRequiredSerialNumber(rs.getInt(5));
                mat.setDefaultStockUnitId(rs.getLong(7));
                mat.setCurrBuyPrice(rs.getDouble("MAT." +  PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE]));
                mat.setBarCode(rs.getString("BARCODE"));
                temp.add(mat);

                unit.setCode(rs.getString(3));
                temp.add(unit);

                curr.setOID(rs.getLong(8));
                curr.setCode(rs.getString("CUR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]));
                temp.add(curr);
				
				dispatch.setJenisDokumen(rs.getString(16));
				dispatch.setNomorBeaCukai(rs.getString(17));
				//dispatch.setTglBc(rs.getDate(40));
				temp.add(dispatch);
						
				cat.setCode(rs.getString(18));
				cat.setName(rs.getString(19));
				temp.add(cat);

                lists.add(temp);
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
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MatDispatchBill matDispatchBill = PstMatDispatchBill.fetchExc(oid);
         object.put(PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_POS_DISPATCH_MATERIAL_ID], matDispatchBill.getOID());
         object.put(PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_DISPATCH_MATERIAL_ITEM_ID], matDispatchBill.getCashBillOid());
         object.put(PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_CASH_BILL_MAIN_ID], matDispatchBill.getStatus());
         object.put(PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_STATUS], matDispatchBill.getMatItemOid());
      }catch(Exception exc){}
      return object;
   }
}
