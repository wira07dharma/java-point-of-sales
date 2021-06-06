package com.dimata.posbo.entity.warehouse;

/* package java */

import com.dimata.common.entity.custom.PstDataCustom;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.common.entity.payment.CurrencyType;

/* package garment */
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.common.entity.payment.PstCurrencyType;
import org.json.JSONObject;

public class PstMatConReceiveItem extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    public static final String TBL_MAT_RECEIVE_ITEM = "pos_receive_con_material_item";
    
    public static final int FLD_RECEIVE_MATERIAL_ITEM_ID = 0;
    public static final int FLD_RECEIVE_MATERIAL_ID = 1;
    public static final int FLD_MATERIAL_ID = 2;
    public static final int FLD_EXPIRED_DATE = 3;
    public static final int FLD_UNIT_ID = 4;
    public static final int FLD_COST = 5;
    public static final int FLD_CURRENCY_ID = 6;
    public static final int FLD_QTY = 7;
    public static final int FLD_DISCOUNT = 8;
    public static final int FLD_TOTAL = 9;
    public static final int FLD_RESIDUE_QTY = 10;
    public static final int FLD_DISCOUNT2 = 11;
    public static final int FLD_DISC_NOMINAL = 12;
    public static final int FLD_CURR_BUYING_PRICE = 13;
    public static final int FLD_FORWADER_COST = 14;
    
    public static final String[] fieldNames = {
        "RECEIVE_CON_MATERIAL_ITEM_ID",
        "RECEIVE_CON_MATERIAL_ID",
        "MATERIAL_ID",
        "EXPIRED_DATE",
        "UNIT_ID",
        "COST",
        "CURRENCY_ID",
        "QTY",
        "DISCOUNT",
        "TOTAL",
        "RESIDUE_QTY",
        "DISCOUNT2",
        "DISCOUNT_NOMINAL",
        "CURR_BUYING_PRICE",
        "FORWARDER_COST"
    };
    
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT
    };
    
    public PstMatConReceiveItem() {
    }
    
    public PstMatConReceiveItem(int i) throws DBException {
        super(new PstMatConReceiveItem());
    }
    
    public PstMatConReceiveItem(String sOid) throws DBException {
        super(new PstMatConReceiveItem(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstMatConReceiveItem(long lOid) throws DBException {
        super(new PstMatConReceiveItem(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }
    
    public String getTableName() {
        return TBL_MAT_RECEIVE_ITEM;
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String getPersistentName() {
        return new PstMatConReceiveItem().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception {
        MatConReceiveItem matreceiveitem = fetchExc(ent.getOID());
        ent = (Entity) matreceiveitem;
        return matreceiveitem.getOID();
    }
    
    synchronized public long insertExc(Entity ent) throws Exception {
        return insertExc((MatConReceiveItem) ent);
    }
    
    synchronized public long updateExc(Entity ent) throws Exception {
        return updateExc((MatConReceiveItem) ent);
    }
    
    synchronized public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public static MatConReceiveItem fetchExc(long oid) throws DBException {
        try {
            MatConReceiveItem matreceiveitem = new MatConReceiveItem();
            PstMatConReceiveItem pstMatConReceiveItem = new PstMatConReceiveItem(oid);
            matreceiveitem.setOID(oid);
            
            matreceiveitem.setReceiveMaterialId(pstMatConReceiveItem.getlong(FLD_RECEIVE_MATERIAL_ID));
            matreceiveitem.setMaterialId(pstMatConReceiveItem.getlong(FLD_MATERIAL_ID));
            matreceiveitem.setExpiredDate(pstMatConReceiveItem.getDate(FLD_EXPIRED_DATE));
            matreceiveitem.setUnitId(pstMatConReceiveItem.getlong(FLD_UNIT_ID));
            matreceiveitem.setCost(pstMatConReceiveItem.getdouble(FLD_COST));
            matreceiveitem.setCurrencyId(pstMatConReceiveItem.getlong(FLD_CURRENCY_ID));
            matreceiveitem.setQty(pstMatConReceiveItem.getdouble(FLD_QTY));
            matreceiveitem.setDiscount(pstMatConReceiveItem.getdouble(FLD_DISCOUNT));
            matreceiveitem.setTotal(pstMatConReceiveItem.getdouble(FLD_TOTAL));
            matreceiveitem.setResidueQty(pstMatConReceiveItem.getdouble(FLD_RESIDUE_QTY));
            matreceiveitem.setDiscount2(pstMatConReceiveItem.getdouble(FLD_DISCOUNT2));
            matreceiveitem.setDiscNominal(pstMatConReceiveItem.getdouble(FLD_DISC_NOMINAL));
            matreceiveitem.setCurrBuyingPrice(pstMatConReceiveItem.getdouble(FLD_CURR_BUYING_PRICE));
            matreceiveitem.setForwarderCost(pstMatConReceiveItem.getdouble(FLD_FORWADER_COST));
            
            return matreceiveitem;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatConReceiveItem(0), DBException.UNKNOWN);
        }
    }
    
    synchronized public static long insertExc(MatConReceiveItem matreceiveitem) throws DBException {
        try {
            PstMatConReceiveItem pstMatConReceiveItem = new PstMatConReceiveItem(0);
            
            pstMatConReceiveItem.setLong(FLD_RECEIVE_MATERIAL_ID, matreceiveitem.getReceiveMaterialId());
            pstMatConReceiveItem.setLong(FLD_MATERIAL_ID, matreceiveitem.getMaterialId());
            pstMatConReceiveItem.setDate(FLD_EXPIRED_DATE, matreceiveitem.getExpiredDate());
            pstMatConReceiveItem.setLong(FLD_UNIT_ID, matreceiveitem.getUnitId());
            pstMatConReceiveItem.setDouble(FLD_COST, matreceiveitem.getCost());
            pstMatConReceiveItem.setLong(FLD_CURRENCY_ID, matreceiveitem.getCurrencyId());
            pstMatConReceiveItem.setDouble(FLD_QTY, matreceiveitem.getQty());
            pstMatConReceiveItem.setDouble(FLD_DISCOUNT, matreceiveitem.getDiscount());
            pstMatConReceiveItem.setDouble(FLD_TOTAL, matreceiveitem.getTotal());
            pstMatConReceiveItem.setDouble(FLD_RESIDUE_QTY, matreceiveitem.getResidueQty());
            pstMatConReceiveItem.setDouble(FLD_DISCOUNT2, matreceiveitem.getDiscount2());
            pstMatConReceiveItem.setDouble(FLD_DISC_NOMINAL, matreceiveitem.getDiscNominal());
            pstMatConReceiveItem.setDouble(FLD_CURR_BUYING_PRICE, matreceiveitem.getCurrBuyingPrice());
            pstMatConReceiveItem.setDouble(FLD_FORWADER_COST, matreceiveitem.getForwarderCost());
            
            pstMatConReceiveItem.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatConReceiveItem.getInsertSQL());
            matreceiveitem.setOID(pstMatConReceiveItem.getlong(FLD_RECEIVE_MATERIAL_ITEM_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatConReceiveItem(0), DBException.UNKNOWN);
        }
        return matreceiveitem.getOID();
    }
    
    synchronized public static long updateExc(MatConReceiveItem matreceiveitem) throws DBException {
        try {
            if (matreceiveitem.getOID() != 0) {
                PstMatConReceiveItem pstMatConReceiveItem = new PstMatConReceiveItem(matreceiveitem.getOID());
                
                pstMatConReceiveItem.setLong(FLD_RECEIVE_MATERIAL_ID, matreceiveitem.getReceiveMaterialId());
                pstMatConReceiveItem.setLong(FLD_MATERIAL_ID, matreceiveitem.getMaterialId());
                pstMatConReceiveItem.setDate(FLD_EXPIRED_DATE, matreceiveitem.getExpiredDate());
                pstMatConReceiveItem.setLong(FLD_UNIT_ID, matreceiveitem.getUnitId());
                pstMatConReceiveItem.setDouble(FLD_COST, matreceiveitem.getCost());
                pstMatConReceiveItem.setLong(FLD_CURRENCY_ID, matreceiveitem.getCurrencyId());
                pstMatConReceiveItem.setDouble(FLD_QTY, matreceiveitem.getQty());
                pstMatConReceiveItem.setDouble(FLD_DISCOUNT, matreceiveitem.getDiscount());
                pstMatConReceiveItem.setDouble(FLD_TOTAL, matreceiveitem.getTotal());
                pstMatConReceiveItem.setDouble(FLD_RESIDUE_QTY, matreceiveitem.getResidueQty());
                pstMatConReceiveItem.setDouble(FLD_DISCOUNT2, matreceiveitem.getDiscount2());
                pstMatConReceiveItem.setDouble(FLD_DISC_NOMINAL, matreceiveitem.getDiscNominal());
                pstMatConReceiveItem.setDouble(FLD_CURR_BUYING_PRICE, matreceiveitem.getCurrBuyingPrice());
                pstMatConReceiveItem.setDouble(FLD_FORWADER_COST, matreceiveitem.getForwarderCost());
                
                pstMatConReceiveItem.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMatConReceiveItem.getUpdateSQL());
                return matreceiveitem.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatConReceiveItem(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    synchronized public static long deleteExc(long oid) throws DBException {
        try {
            PstMatConReceiveItem pstMatConReceiveItem = new PstMatConReceiveItem(oid);
            pstMatConReceiveItem.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatConReceiveItem.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatConReceiveItem(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_MAT_RECEIVE_ITEM;
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
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MatConReceiveItem matreceiveitem = new MatConReceiveItem();
                resultToObject(rs, matreceiveitem);
                lists.add(matreceiveitem);
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
    
    /**
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @return
     * @update by Edhy
     */
    public static Vector list(int limitStart, int recordToGet, String whereClause) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT RMI." + fieldNames[FLD_RECEIVE_MATERIAL_ITEM_ID] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                         " , UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                         " , RMI." + fieldNames[FLD_COST] +
                         " , RMI." +  fieldNames[FLD_CURRENCY_ID] + // CURR CODE
                         " , RMI." + fieldNames[FLD_QTY] +
                         " , RMI." + fieldNames[FLD_TOTAL] +
                         " , RMI." + fieldNames[FLD_MATERIAL_ID] +
                         " , RMI." + fieldNames[FLD_UNIT_ID] +
                         " , RMI." + fieldNames[FLD_CURRENCY_ID] +
                         " , RMI." + fieldNames[FLD_EXPIRED_DATE] +
                         " , RMI." + fieldNames[FLD_RESIDUE_QTY] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
                         " , RMI." + fieldNames[FLD_CURR_BUYING_PRICE] +
                         " , RMI." + fieldNames[FLD_DISCOUNT] +
                         " , RMI." + fieldNames[FLD_DISCOUNT2] +
                         " , RMI." + fieldNames[FLD_DISC_NOMINAL] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_REQUIRED_SERIAL_NUMBER] +
                         " , UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                         " , RMI." + fieldNames[FLD_FORWADER_COST] +
                         " , CUR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE] +
                         " FROM " + TBL_MAT_RECEIVE_ITEM + " RMI" +
                         " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                         " ON RMI." + fieldNames[FLD_MATERIAL_ID] +
                         " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                         " INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
                         " ON RMI." + fieldNames[FLD_UNIT_ID] +
                         " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                         
                         " LEFT JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE+ " CUR" +
                         " ON RMI." + fieldNames[FLD_CURRENCY_ID] +
                         
                         " = CUR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID];
            
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            
            sql = sql + " ORDER BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            
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
            
            //System.out.println("SQL : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector temp = new Vector();
                MatConReceiveItem matreceiveitem = new MatConReceiveItem();
                Material mat = new Material();
                Unit unit = new Unit();
                CurrencyType curr = new CurrencyType();
                
                matreceiveitem.setOID(rs.getLong(1));
                matreceiveitem.setCost(rs.getDouble(5));
                matreceiveitem.setQty(rs.getDouble(7));
                matreceiveitem.setTotal(rs.getDouble(8));
                matreceiveitem.setMaterialId(rs.getLong(9));
                matreceiveitem.setUnitId(rs.getLong(10));
                matreceiveitem.setCurrencyId(rs.getLong(11));
                matreceiveitem.setExpiredDate(rs.getDate(12));
                matreceiveitem.setResidueQty(rs.getDouble(13));
                matreceiveitem.setCurrBuyingPrice(rs.getDouble(15));
                matreceiveitem.setDiscount(rs.getDouble(16));
                matreceiveitem.setDiscount2(rs.getDouble(17));
                matreceiveitem.setDiscNominal(rs.getDouble(18));
                matreceiveitem.setForwarderCost(rs.getDouble(22));
                temp.add(matreceiveitem);
                
                mat.setSku(rs.getString(2));
                mat.setName(rs.getString(3));
                mat.setDefaultPrice(rs.getDouble(14));
                mat.setRequiredSerialNumber(rs.getInt(19));
                mat.setDefaultStockUnitId(rs.getLong(21));
                temp.add(mat);
                
                unit.setCode(rs.getString(4));
                temp.add(unit);
                
                curr.setOID(rs.getLong(6));
                curr.setCode(rs.getString("CUR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]));
                temp.add(curr);
                
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
    
    public static void resultToObject(ResultSet rs, MatConReceiveItem matreceiveitem) {
        try {
            matreceiveitem.setOID(rs.getLong(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]));
            matreceiveitem.setReceiveMaterialId(rs.getLong(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_RECEIVE_MATERIAL_ID]));
            matreceiveitem.setMaterialId(rs.getLong(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_MATERIAL_ID]));
            matreceiveitem.setExpiredDate(rs.getDate(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_EXPIRED_DATE]));
            matreceiveitem.setUnitId(rs.getLong(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_UNIT_ID]));
            matreceiveitem.setCost(rs.getDouble(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_COST]));
            matreceiveitem.setCurrencyId(rs.getLong(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_CURRENCY_ID]));
            matreceiveitem.setQty(rs.getDouble(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_QTY]));
            matreceiveitem.setDiscount(rs.getDouble(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_DISCOUNT]));
            matreceiveitem.setTotal(rs.getDouble(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_TOTAL]));
            matreceiveitem.setResidueQty(rs.getDouble(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_RESIDUE_QTY]));
            matreceiveitem.setDiscount2(rs.getDouble(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_DISCOUNT2]));
            matreceiveitem.setDiscNominal(rs.getDouble(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_DISC_NOMINAL]));
            matreceiveitem.setCurrBuyingPrice(rs.getDouble(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_CURR_BUYING_PRICE]));
            matreceiveitem.setForwarderCost(rs.getDouble(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_FORWADER_COST]));
            
        } catch (Exception e) {
        }
    }
    
    public static boolean checkOID(long matReceiveItemId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_RECEIVE_ITEM +
                         " WHERE " + fieldNames[FLD_RECEIVE_MATERIAL_ITEM_ID] + " = " + matReceiveItemId;
            
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
        }
        return result;
    }
    
    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + fieldNames[FLD_RECEIVE_MATERIAL_ITEM_ID] +
                         ") FROM " + TBL_MAT_RECEIVE_ITEM;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            
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
                    MatConReceiveItem matreceiveitem = (MatConReceiveItem) list.get(ls);
                    if (oid == matreceiveitem.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;
        
        return start;
    }
    
    /* This method used to find command where current data */
    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + (recordToGet - mdl);
        if (start == 0)
            cmd = Command.FIRST;
        else {
            if (start == (vectSize - recordToGet))
                cmd = Command.LAST;
            else {
                start = start + recordToGet;
                if (start <= (vectSize - recordToGet)) {
                    cmd = Command.NEXT;
                    //System.out.println("next.......................");
                } else {
                    start = start - recordToGet;
                    if (start > 0) {
                        cmd = Command.PREV;
                        //System.out.println("prev.......................");
                    }
                }
            }
        }
        return cmd;
    }
    
    public static double getTotal(String whereClause) {
        DBResultSet dbrs = null;
        double amount = 0;
        try {
            String sql = "SELECT SUM(" + fieldNames[FLD_TOTAL] +
                         ") AS MNT FROM " + TBL_MAT_RECEIVE_ITEM;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                amount = rs.getDouble("MNT");
            }
            
            rs.close();
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
        return amount;
    }
    
    /** Fungsi ini digunakan untuk mendapatkan total nilai LGR dalam mata uang standar
     * create by: gwawan@dimata 16 Agustus 2007
     */
    public static double getTotal(long oidRcv) {
        DBResultSet dbrs = null;
        double amount = 0;
        try {
            String sql = " select sum(rcvi."+PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_TOTAL]+") * ";
            sql += " rcv."+PstMatConReceive.fieldNames[PstMatConReceive.FLD_TRANS_RATE]+" as amount";
            sql += " from "+PstMatConReceive.TBL_MAT_RECEIVE+" rcv";
            sql += " inner join "+PstMatConReceiveItem.TBL_MAT_RECEIVE_ITEM+" rcvi";
            sql += " on rcv."+PstMatConReceive.fieldNames[PstMatConReceive.FLD_RECEIVE_MATERIAL_ID];
            sql += " = rcvi."+PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_RECEIVE_MATERIAL_ID];
            
            if (oidRcv != 0)
                sql += " where "+PstMatConReceive.fieldNames[PstMatConReceive.FLD_RECEIVE_MATERIAL_ID]+" = "+oidRcv;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                amount = rs.getDouble("amount");
            }
            
            rs.close();
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
        return amount;
    }
    
    /**
     * this method used to check if specify material already exist in current receiveItem
     * return "true" ---> if material already exist in receiveItem
     * return "false" ---> if material not available in receiveItem
     */
    public static boolean materialExist(long oidMaterial, long oidReceiveMaterial) {
        DBResultSet dbrs = null;
        boolean bool = false;
        try {
            String sql = "SELECT ITM." + PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_MATERIAL_ID] +
                         " FROM " + PstMatConReceiveItem.TBL_MAT_RECEIVE_ITEM + " AS ITM " +
                         " INNER JOIN " + PstMatConReceive.TBL_MAT_RECEIVE + " AS MAT " +
                         " ON ITM." + PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
                         " = MAT." + PstMatConReceive.fieldNames[PstMatConReceive.FLD_RECEIVE_MATERIAL_ID] +
                         " WHERE ITM." + PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial +
                         " AND ITM." + PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_MATERIAL_ID] + "=" + oidMaterial;
            //System.out.println("PstMatConReceiveItem.materialExist.sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                bool = true;
                break;
            }
        } catch (Exception e) {
            System.out.println("PstMatConReceiveItem.materialExist.err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return bool;
        }
    }
    
    public static long deleteExcByParent(long oid) throws DBException {
        long hasil = 0;
        try {
            String sql = "DELETE FROM " + TBL_MAT_RECEIVE_ITEM +
                         " WHERE " + fieldNames[FLD_RECEIVE_MATERIAL_ID] + " = " + oid;
            int result = execUpdate(sql);
            hasil = oid;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatConReceiveItem(0), DBException.UNKNOWN);
        }
        return hasil;
    }
    
    
    /** di gunakan untuk mencari object receive item
     * @param invoice = invoice supplier
     * @param oidMaterial = oid material/barang
     * @return
     */
    public static MatConReceiveItem getObjectReceiveItem(String invoice, long recFromDispatchId, long oidMaterial) {
        MatConReceiveItem matreceiveitem = new MatConReceiveItem();
        try {
            String whereClause = "";
            if (recFromDispatchId == 0) {
                whereClause = PstMatConReceive.fieldNames[PstMatConReceive.FLD_INVOICE_SUPPLIER] + "='" + invoice + "'";
            } else {
                whereClause = PstMatConReceive.fieldNames[PstMatConReceive.FLD_RECEIVE_MATERIAL_ID] + "=" + recFromDispatchId;
            }
            Vector vt = PstMatConReceive.list(0, 0, whereClause, "");
            MatConReceive matreceive = new MatConReceive();
            if (vt != null && vt.size() > 0) {
                matreceive = (MatConReceive) vt.get(0);
            }
            
            whereClause = PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + matreceive.getOID() +
                          " AND " + PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_MATERIAL_ID] + "=" + oidMaterial;
            Vector vect = PstMatConReceiveItem.list(0, 0, whereClause, "");
            if (vect != null && vect.size() > 0) {
                matreceiveitem = (MatConReceiveItem) vect.get(0);
            }
            
        } catch (Exception e) {
            System.out.println("Error getObjectReceiveItem : "+e.toString());
        }
        return matreceiveitem;
    }
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MatConReceiveItem matConReceiveItem = PstMatConReceiveItem.fetchExc(oid);
         object.put(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID], matConReceiveItem.getOID());
         object.put(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_RECEIVE_MATERIAL_ID], matConReceiveItem.getReceiveMaterialId());
         object.put(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_MATERIAL_ID], matConReceiveItem.getMaterialId());
         object.put(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_EXPIRED_DATE], matConReceiveItem.getExpiredDate());
         object.put(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_UNIT_ID], matConReceiveItem.getUnitId());
         object.put(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_COST], matConReceiveItem.getCost());
         object.put(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_CURRENCY_ID], matConReceiveItem.getCurrencyId());
         object.put(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_QTY], matConReceiveItem.getQty());
         object.put(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_DISCOUNT], matConReceiveItem.getDiscount());
         object.put(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_TOTAL], matConReceiveItem.getTotal());
         object.put(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_RESIDUE_QTY], matConReceiveItem.getResidueQty());
         object.put(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_DISCOUNT2], matConReceiveItem.getDiscount2());
         object.put(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_DISC_NOMINAL], matConReceiveItem.getDiscNominal());
         object.put(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_CURR_BUYING_PRICE], matConReceiveItem.getCurrBuyingPrice());
         object.put(PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_FORWADER_COST], matConReceiveItem.getForwarderCost());
      }catch(Exception exc){}
      return object;
   }
}
