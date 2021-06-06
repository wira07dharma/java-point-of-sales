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

public class PstMatReceiveItem extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    public static final String TBL_MAT_RECEIVE_ITEM = "pos_receive_material_item";
    
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
    public static final int FLD_QTY_ENTRI = 15;
    public static final int FLD_UNIT_KONVERSI_ID = 16;
    public static final int FLD_PRICE_KONVERSI=17;
    public static final int FLD_BONUS = 18;
    public static final int FLD_BERAT = 19;
    public static final int FLD_CASH_BILL_MAIN_ID = 20;
    public static final int FLD_CASH_BILL_DETAIL_ID = 21;
    public static final int FLD_REMARK = 22;
    public static final int FLD_BERAT_AWAL = 23;
    public static final int FLD_RUSAK = 24;
    public static final int FLD_SORTING_STATUS = 25;
    public static final int FLD_PREV_MATERIAL_ID = 26;
    public static final int FLD_COLOR_ID = 27;
    public static final int FLD_GONDOLA_ID = 28;

    
    public static final String[] fieldNames = {
        "RECEIVE_MATERIAL_ITEM_ID",//0
        "RECEIVE_MATERIAL_ID",//1
        "MATERIAL_ID",//2
        "EXPIRED_DATE",//3
        "UNIT_ID",//4
        "COST",//5
        "CURRENCY_ID",//6
        "QTY",//7
        "DISCOUNT",//8
        "TOTAL",//9
        "RESIDUE_QTY",//10
        "DISCOUNT2",//11
        "DISCOUNT_NOMINAL",//12
        "CURR_BUYING_PRICE",//13
        "FORWARDER_COST",//14
        "QTY_ENTRI",//15
        "UNIT_KONVERSI_ID",//16
        "PRICE_KONVERSI",//17
        "BONUS",//18
        "BERAT",//19
        "CASH_BILL_MAIN_ID",//20
        "CASH_BILL_DETAIL_ID",//21
        "REMARK",//22
        "BERAT_AWAL",//23
        "RUSAK",//24
        "SORTING_STATUS", //25
        "PREV_MATERIAL_ID", //26
        "COLOR_ID", //27
        "GONDOLA_ID"
    };
    
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,//0
        TYPE_LONG,//1
        TYPE_LONG,//2
        TYPE_DATE,//3
        TYPE_LONG,//4
        TYPE_FLOAT,//5
        TYPE_LONG,//6
        TYPE_FLOAT,//7
        TYPE_FLOAT,//8
        TYPE_FLOAT,//9
        TYPE_FLOAT,//10
        TYPE_FLOAT,//11
        TYPE_FLOAT,//12
        TYPE_FLOAT,//13
        TYPE_FLOAT,//14
        TYPE_FLOAT,//15
        TYPE_LONG,//16
        TYPE_FLOAT,//17
        TYPE_INT,//18
        TYPE_FLOAT,//19
        TYPE_LONG,//20
        TYPE_LONG,//21
        TYPE_STRING,//22
        TYPE_FLOAT,//23
        TYPE_INT,//24
        TYPE_INT,//25
        TYPE_LONG,//26
        TYPE_LONG,
        TYPE_LONG
    };

    
//    public static final String[] sortingKey = {
//        "Open", "Direct Sales"
//    };
//    public static final int[] sortingVal = {
//        SORT_OPEN, SORT_DIRECT_SALES
//    };
    
    public static final int SORT_OPEN = 0;
    public static final int SORT_DIRECT_SALES = 1;
    public static final int SORT_LEBUR = 2;
    
    public static final String[] sortingKey = {
        "Open", "Direct Sales", "Lebur"
    };
    public static final int[] sortingVal = {
        SORT_OPEN, SORT_DIRECT_SALES, SORT_LEBUR
    };
    
    public static Vector getSortingKey(){
        Vector key = new Vector();        
        for(int i=0; i < sortingKey.length;i++ ){
            key.add(""+sortingKey[i]);
        }
        return key;
    }

    public static Vector getSortingVal(){
        Vector val = new Vector();        
        for(int i=0; i < sortingVal.length;i++ ){
            val.add(""+sortingVal[i]);
        }
        return val;
    }
    
    public PstMatReceiveItem() {
    }
    
    public PstMatReceiveItem(int i) throws DBException {
        super(new PstMatReceiveItem());
    }
    
    public PstMatReceiveItem(String sOid) throws DBException {
        super(new PstMatReceiveItem(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstMatReceiveItem(long lOid) throws DBException {
        super(new PstMatReceiveItem(0));
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
        return new PstMatReceiveItem().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception {
        MatReceiveItem matreceiveitem = fetchExc(ent.getOID());
        ent = (Entity) matreceiveitem;
        return matreceiveitem.getOID();
    }
    
    synchronized public long insertExc(Entity ent) throws Exception {
        return insertExc((MatReceiveItem) ent);
    }
    
    synchronized public long updateExc(Entity ent) throws Exception {
        return updateExc((MatReceiveItem) ent);
    }
    
    synchronized public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public static MatReceiveItem fetchExc(long oid) throws DBException {
        try {
            MatReceiveItem matreceiveitem = new MatReceiveItem();
            PstMatReceiveItem pstMatReceiveItem = new PstMatReceiveItem(oid);
            matreceiveitem.setOID(oid);
            
            matreceiveitem.setReceiveMaterialId(pstMatReceiveItem.getlong(FLD_RECEIVE_MATERIAL_ID));
            matreceiveitem.setMaterialId(pstMatReceiveItem.getlong(FLD_MATERIAL_ID));
            matreceiveitem.setExpiredDate(pstMatReceiveItem.getDate(FLD_EXPIRED_DATE));
            matreceiveitem.setUnitId(pstMatReceiveItem.getlong(FLD_UNIT_ID));
            matreceiveitem.setCost(pstMatReceiveItem.getdouble(FLD_COST));
            matreceiveitem.setCurrencyId(pstMatReceiveItem.getlong(FLD_CURRENCY_ID));
            matreceiveitem.setQty(pstMatReceiveItem.getdouble(FLD_QTY));
            matreceiveitem.setDiscount(pstMatReceiveItem.getdouble(FLD_DISCOUNT));
            matreceiveitem.setTotal(pstMatReceiveItem.getdouble(FLD_TOTAL));
            matreceiveitem.setResidueQty(pstMatReceiveItem.getdouble(FLD_RESIDUE_QTY));
            matreceiveitem.setDiscount2(pstMatReceiveItem.getdouble(FLD_DISCOUNT2));
            matreceiveitem.setDiscNominal(pstMatReceiveItem.getdouble(FLD_DISC_NOMINAL));
            matreceiveitem.setCurrBuyingPrice(pstMatReceiveItem.getdouble(FLD_CURR_BUYING_PRICE));
            matreceiveitem.setForwarderCost(pstMatReceiveItem.getdouble(FLD_FORWADER_COST));
            matreceiveitem.setQtyEntry(pstMatReceiveItem.getdouble(FLD_QTY_ENTRI));
            matreceiveitem.setUnitKonversi(pstMatReceiveItem.getlong(FLD_UNIT_KONVERSI_ID));
            matreceiveitem.setPriceKonv(pstMatReceiveItem.getdouble(FLD_PRICE_KONVERSI));
            matreceiveitem.setBonus(pstMatReceiveItem.getInt(FLD_BONUS));
            matreceiveitem.setBerat(pstMatReceiveItem.getdouble(FLD_BERAT));
            matreceiveitem.setCashBillMainId(pstMatReceiveItem.getlong(FLD_CASH_BILL_MAIN_ID));
            matreceiveitem.setCashBillDetailId(pstMatReceiveItem.getlong(FLD_CASH_BILL_DETAIL_ID));
            matreceiveitem.setRemark(pstMatReceiveItem.getString(FLD_REMARK));
            matreceiveitem.setSortStatus(pstMatReceiveItem.getInt(FLD_SORTING_STATUS));
            matreceiveitem.setPrevMaterialId(pstMatReceiveItem.getlong(FLD_PREV_MATERIAL_ID));
            matreceiveitem.setBeratAwal(pstMatReceiveItem.getdouble(FLD_BERAT_AWAL));
            matreceiveitem.setRusak(pstMatReceiveItem.getInt(FLD_RUSAK));
            matreceiveitem.setColorId(pstMatReceiveItem.getlong(FLD_COLOR_ID));
            matreceiveitem.setGondolaId(pstMatReceiveItem.getInt(FLD_GONDOLA_ID));
            
            return matreceiveitem;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatReceiveItem(0), DBException.UNKNOWN);
        }
    }
    
    synchronized public static long insertExc(MatReceiveItem matreceiveitem) throws DBException {
        try {
            PstMatReceiveItem pstMatReceiveItem = new PstMatReceiveItem(0);
            
            pstMatReceiveItem.setLong(FLD_RECEIVE_MATERIAL_ID, matreceiveitem.getReceiveMaterialId());
            pstMatReceiveItem.setLong(FLD_MATERIAL_ID, matreceiveitem.getMaterialId());
            pstMatReceiveItem.setDate(FLD_EXPIRED_DATE, matreceiveitem.getExpiredDate());
            pstMatReceiveItem.setLong(FLD_UNIT_ID, matreceiveitem.getUnitId());
            pstMatReceiveItem.setDouble(FLD_COST, matreceiveitem.getCost());
            pstMatReceiveItem.setLong(FLD_CURRENCY_ID, matreceiveitem.getCurrencyId());
            pstMatReceiveItem.setDouble(FLD_QTY, matreceiveitem.getQty());
            pstMatReceiveItem.setDouble(FLD_DISCOUNT, matreceiveitem.getDiscount());
            pstMatReceiveItem.setDouble(FLD_TOTAL, matreceiveitem.getTotal());
            pstMatReceiveItem.setDouble(FLD_RESIDUE_QTY, matreceiveitem.getResidueQty());
            pstMatReceiveItem.setDouble(FLD_DISCOUNT2, matreceiveitem.getDiscount2());
            pstMatReceiveItem.setDouble(FLD_DISC_NOMINAL, matreceiveitem.getDiscNominal());
            pstMatReceiveItem.setDouble(FLD_CURR_BUYING_PRICE, matreceiveitem.getCurrBuyingPrice());
            pstMatReceiveItem.setDouble(FLD_FORWADER_COST, matreceiveitem.getForwarderCost());
            pstMatReceiveItem.setDouble(FLD_QTY_ENTRI, matreceiveitem.getQtyEntry());
            pstMatReceiveItem.setLong(FLD_UNIT_KONVERSI_ID,matreceiveitem.getUnitKonversi());
            pstMatReceiveItem.setDouble(FLD_PRICE_KONVERSI, matreceiveitem.getPriceKonv());
            pstMatReceiveItem.setInt(FLD_BONUS, matreceiveitem.getBonus());
            pstMatReceiveItem.setDouble(FLD_BERAT, matreceiveitem.getBerat());
            pstMatReceiveItem.setLong(FLD_CASH_BILL_MAIN_ID, matreceiveitem.getCashBillMainId());
            pstMatReceiveItem.setLong(FLD_CASH_BILL_DETAIL_ID, matreceiveitem.getCashBillDetailId());
            pstMatReceiveItem.setString(FLD_REMARK, matreceiveitem.getRemark());
            pstMatReceiveItem.setInt(FLD_SORTING_STATUS, matreceiveitem.getSortStatus());
            pstMatReceiveItem.setLong(FLD_PREV_MATERIAL_ID, matreceiveitem.getPrevMaterialId());
            pstMatReceiveItem.setDouble(FLD_BERAT_AWAL, matreceiveitem.getBeratAwal());
            pstMatReceiveItem.setInt(FLD_RUSAK, matreceiveitem.getRusak());
            pstMatReceiveItem.setLong(FLD_COLOR_ID, matreceiveitem.getColorId());
            pstMatReceiveItem.setLong(FLD_GONDOLA_ID, matreceiveitem.getGondolaId());
            
            pstMatReceiveItem.insert();
            matreceiveitem.setOID(pstMatReceiveItem.getlong(FLD_RECEIVE_MATERIAL_ITEM_ID));
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatReceiveItem.getInsertSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatReceiveItem(0), DBException.UNKNOWN);
        }
        return matreceiveitem.getOID();
    }
    
    synchronized public static long updateExc(MatReceiveItem matreceiveitem) throws DBException {
        try {
            if (matreceiveitem.getOID() != 0) {
                PstMatReceiveItem pstMatReceiveItem = new PstMatReceiveItem(matreceiveitem.getOID());
                
                pstMatReceiveItem.setLong(FLD_RECEIVE_MATERIAL_ID, matreceiveitem.getReceiveMaterialId());
                pstMatReceiveItem.setLong(FLD_MATERIAL_ID, matreceiveitem.getMaterialId());
                pstMatReceiveItem.setDate(FLD_EXPIRED_DATE, matreceiveitem.getExpiredDate());
                pstMatReceiveItem.setLong(FLD_UNIT_ID, matreceiveitem.getUnitId());
                pstMatReceiveItem.setDouble(FLD_COST, matreceiveitem.getCost());
                pstMatReceiveItem.setLong(FLD_CURRENCY_ID, matreceiveitem.getCurrencyId());
                pstMatReceiveItem.setDouble(FLD_QTY, matreceiveitem.getQty());
                pstMatReceiveItem.setDouble(FLD_DISCOUNT, matreceiveitem.getDiscount());
                pstMatReceiveItem.setDouble(FLD_TOTAL, matreceiveitem.getTotal());
                pstMatReceiveItem.setDouble(FLD_RESIDUE_QTY, matreceiveitem.getResidueQty());
                pstMatReceiveItem.setDouble(FLD_DISCOUNT2, matreceiveitem.getDiscount2());
                pstMatReceiveItem.setDouble(FLD_DISC_NOMINAL, matreceiveitem.getDiscNominal());
                pstMatReceiveItem.setDouble(FLD_CURR_BUYING_PRICE, matreceiveitem.getCurrBuyingPrice());
                pstMatReceiveItem.setDouble(FLD_FORWADER_COST, matreceiveitem.getForwarderCost());
                pstMatReceiveItem.setDouble(FLD_QTY_ENTRI, matreceiveitem.getQtyEntry());
                pstMatReceiveItem.setLong(FLD_UNIT_KONVERSI_ID,matreceiveitem.getUnitKonversi());
                pstMatReceiveItem.setDouble(FLD_PRICE_KONVERSI, matreceiveitem.getPriceKonv());
                pstMatReceiveItem.setInt(FLD_BONUS, matreceiveitem.getBonus()); 
                pstMatReceiveItem.setDouble(FLD_BERAT, matreceiveitem.getBerat());
                pstMatReceiveItem.setLong(FLD_CASH_BILL_MAIN_ID, matreceiveitem.getCashBillMainId());
                pstMatReceiveItem.setLong(FLD_CASH_BILL_DETAIL_ID, matreceiveitem.getCashBillDetailId());
                pstMatReceiveItem.setString(FLD_REMARK, matreceiveitem.getRemark());
                pstMatReceiveItem.setInt(FLD_SORTING_STATUS, matreceiveitem.getSortStatus());
                pstMatReceiveItem.setLong(FLD_PREV_MATERIAL_ID, matreceiveitem.getPrevMaterialId());
                pstMatReceiveItem.setDouble(FLD_BERAT_AWAL, matreceiveitem.getBeratAwal());
                pstMatReceiveItem.setInt(FLD_RUSAK, matreceiveitem.getRusak());
                pstMatReceiveItem.setLong(FLD_COLOR_ID, matreceiveitem.getColorId());
                pstMatReceiveItem.setLong(FLD_GONDOLA_ID, matreceiveitem.getGondolaId());
                
                pstMatReceiveItem.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMatReceiveItem.getUpdateSQL());
                return matreceiveitem.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatReceiveItem(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
     public static long updateExcForTransferUnit(MatReceiveItem matreceiveitem) throws DBException {
        try {
            if (matreceiveitem.getOID() != 0) {
                PstMatReceiveItem pstMatReceiveItem = new PstMatReceiveItem(matreceiveitem.getOID());
                
                pstMatReceiveItem.setLong(FLD_RECEIVE_MATERIAL_ID, matreceiveitem.getReceiveMaterialId());
                pstMatReceiveItem.setLong(FLD_MATERIAL_ID, matreceiveitem.getMaterialId());
                pstMatReceiveItem.setDate(FLD_EXPIRED_DATE, matreceiveitem.getExpiredDate());
                pstMatReceiveItem.setLong(FLD_UNIT_ID, matreceiveitem.getUnitId());
                pstMatReceiveItem.setDouble(FLD_COST, matreceiveitem.getCost());
                pstMatReceiveItem.setLong(FLD_CURRENCY_ID, matreceiveitem.getCurrencyId());
                pstMatReceiveItem.setDouble(FLD_QTY, matreceiveitem.getQty());
                pstMatReceiveItem.setDouble(FLD_DISCOUNT, matreceiveitem.getDiscount());
                pstMatReceiveItem.setDouble(FLD_TOTAL, matreceiveitem.getTotal());
                pstMatReceiveItem.setDouble(FLD_RESIDUE_QTY, matreceiveitem.getResidueQty());
                pstMatReceiveItem.setDouble(FLD_DISCOUNT2, matreceiveitem.getDiscount2());
                pstMatReceiveItem.setDouble(FLD_DISC_NOMINAL, matreceiveitem.getDiscNominal());
                pstMatReceiveItem.setDouble(FLD_CURR_BUYING_PRICE, matreceiveitem.getCurrBuyingPrice());
                pstMatReceiveItem.setDouble(FLD_FORWADER_COST, matreceiveitem.getForwarderCost());
                pstMatReceiveItem.setDouble(FLD_QTY_ENTRI, matreceiveitem.getUnitKonversi());
                pstMatReceiveItem.setLong(FLD_UNIT_KONVERSI_ID,matreceiveitem.getUnitKonversi());
                pstMatReceiveItem.setDouble(FLD_PRICE_KONVERSI, matreceiveitem.getPriceKonv());
                
                pstMatReceiveItem.setInt(FLD_BONUS, matreceiveitem.getBonus()); 
                pstMatReceiveItem.setDouble(FLD_BERAT, matreceiveitem.getBerat());
                pstMatReceiveItem.setLong(FLD_CASH_BILL_MAIN_ID, matreceiveitem.getCashBillMainId());
                pstMatReceiveItem.setLong(FLD_CASH_BILL_DETAIL_ID, matreceiveitem.getCashBillDetailId());
                pstMatReceiveItem.setString(FLD_REMARK, matreceiveitem.getRemark());
                pstMatReceiveItem.setInt(FLD_SORTING_STATUS, matreceiveitem.getSortStatus());
                pstMatReceiveItem.setLong(FLD_PREV_MATERIAL_ID, matreceiveitem.getPrevMaterialId());
                pstMatReceiveItem.setDouble(FLD_BERAT_AWAL, matreceiveitem.getBeratAwal());
                pstMatReceiveItem.setInt(FLD_RUSAK, matreceiveitem.getRusak());
                pstMatReceiveItem.setLong(FLD_COLOR_ID, matreceiveitem.getColorId());
                pstMatReceiveItem.setLong(FLD_GONDOLA_ID, matreceiveitem.getGondolaId());
                pstMatReceiveItem.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMatReceiveItem.getUpdateSQL());
                return matreceiveitem.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatReceiveItem(0), DBException.UNKNOWN);
        }
        return 0;
    }

    synchronized public static long deleteExc(long oid) throws DBException {
        try {
            PstMatReceiveItem pstMatReceiveItem = new PstMatReceiveItem(oid);
            pstMatReceiveItem.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatReceiveItem.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatReceiveItem(0), DBException.UNKNOWN);
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
                MatReceiveItem matreceiveitem = new MatReceiveItem();
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

        public static Vector listWithCurrentHPP(int limitStart, int recordToGet, String whereClause, String order) {
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
                MatReceiveItem matreceiveitem = new MatReceiveItem();
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
     * add opie-eyek 20130812 untuk mencari harga penerimaan terakhir
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @param order
     * @return
     */
      public static Vector listLastReceive(long oidMaterial,int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            /*
             *  SELECT PRM.RECEIVE_DATE, PRMI.MATERIAL_ID, PRMI.COST FROM pos_receive_material AS PRM
                INNER JOIN pos_receive_material_item AS PRMI
                ON PRM.RECEIVE_MATERIAL_ID = PRMI.RECEIVE_MATERIAL_ID
                WHERE PRMI.MATERIAL_ID='504404356808324760'
                ORDER BY PRM.receive_date DESC LIMIT 0,1;
             */

            String sql = " SELECT  PRM."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]+", PRM."+PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE]+", PRM."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]+", PRMI.MATERIAL_ID, PRMI.COST, "
                       + "  PRM.SUPPLIER_ID, PRM."+PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE]
                       + ", PRMI."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]
                       + " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " AS PRM "+
                         " INNER JOIN "+TBL_MAT_RECEIVE_ITEM+" AS PRMI " +
                         " ON PRM."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]+" = PRMI."+fieldNames[FLD_RECEIVE_MATERIAL_ID]+""+
                         " WHERE PRMI."+fieldNames[FLD_MATERIAL_ID]+"='"+oidMaterial+"'";
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " AND " + whereClause;
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
                MatReceiveItem matreceiveitem = new MatReceiveItem();
                resultToObjectLastReceive(rs, matreceiveitem);
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
         if(limitStart<0){
        limitStart =0;
        }
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
                         //adding barcode by mirahu 20120427
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] +
                         " , RMI." + fieldNames[FLD_UNIT_KONVERSI_ID] +" AS UNIT_KONVERSI "+
                         " , RMI." + fieldNames[FLD_QTY_ENTRI] +" AS QTY_ENTRI "+
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE] +
                         " , RMI." + fieldNames[FLD_PRICE_KONVERSI] +
                         " , RMI." + fieldNames[FLD_BERAT] +
                    
                         //added by wira 2019 for get category code at pos_category
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                         //added by dewok 2018
                         //" , MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] +
                         //" , MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_BERAT] +
                         " FROM " + TBL_MAT_RECEIVE_ITEM + " RMI" +
                         " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                         " ON RMI." + fieldNames[FLD_MATERIAL_ID] +
                         " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                         " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
                         " ON RMI." + fieldNames[FLD_UNIT_ID] +
                         " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                         
                         " LEFT JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE+ " CUR" +
                         " ON RMI." + fieldNames[FLD_CURRENCY_ID] +
                         
                         " = CUR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID];
            
                         //added by dewok 2018 for get qty n berat stock
//                         sql += " INNER JOIN " + PstMatReceive.TBL_MAT_RECEIVE + " RM "
//                                 + " ON RM." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] 
//                                 + " = RMI." + fieldNames[FLD_RECEIVE_MATERIAL_ID] 
//                                 + " INNER JOIN " + PstMaterialStock.TBL_MATERIAL_STOCK + " MS " 
//                                 + " ON MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] 
//                                 + " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] 
//                                 + "";
            
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
                MatReceiveItem matreceiveitem = new MatReceiveItem();
                Material mat = new Material();
                Unit unit = new Unit();
                CurrencyType curr = new CurrencyType();
                //MaterialStock stock = new MaterialStock();
                
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
                matreceiveitem.setUnitKonversi(rs.getLong("UNIT_KONVERSI"));
                matreceiveitem.setQtyEntry(rs.getDouble("QTY_ENTRI"));
                matreceiveitem.setPriceKonv(rs.getDouble("PRICE_KONVERSI"));
                matreceiveitem.setBerat(rs.getDouble(29));
                temp.add(matreceiveitem);
                
                mat.setSku(rs.getString(2));
                mat.setName(rs.getString(3));
                mat.setDefaultPrice(rs.getDouble(14));
                mat.setRequiredSerialNumber(rs.getInt(19));
                mat.setDefaultStockUnitId(rs.getLong(21));
                mat.setBarCode(rs.getString(24));
                mat.setCategoryId(rs.getLong(30));
                mat.setCurrBuyPrice(rs.getDouble("CURR_BUY_PRICE"));
                mat.setAveragePrice(rs.getInt(31));
                temp.add(mat);
                
                unit.setCode(rs.getString(4));
                temp.add(unit);
                
                curr.setOID(rs.getLong(6));
                curr.setCode(rs.getString("CUR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]));
                temp.add(curr);
                
                //stock.setQty(rs.getDouble("MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]));
                //stock.setBerat(rs.getDouble("MS." + PstMaterialStock.fieldNames[PstMaterialStock.FLD_BERAT]));
                //temp.add(stock);
                
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

    /**
     *
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @param order  Alias of Table : MAT. = material  RMI.= Receive Material
     * @return  vector of vector of Receive Material Item and Material
     * update opie-eyek 20130812 penambahan MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE] agar bisa di cek, berapa harga beli sekarang dan harga beli terakhir di sistem
     */
    public static Vector listVectorRecItemComplete(int limitStart, int recordToGet, String whereClause, String order) {
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
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE] +
                         " , RMI." + fieldNames[FLD_UNIT_KONVERSI_ID] +" AS UNIT_KONVERSI "+
                         " , RMI." + fieldNames[FLD_QTY_ENTRI] +" AS QTY_ENTRI "+
                         " , RMI." + fieldNames[FLD_BERAT] +
                         " , RMI." + fieldNames[FLD_REMARK] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_KADAR_ID] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_COLOR_ID] +
                         " , RMI." + fieldNames[FLD_SORTING_STATUS] +
                         " , RMI." + fieldNames[FLD_GONDOLA_ID] +
                         " , RMI." + fieldNames[FLD_COLOR_ID] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
                         " FROM " + TBL_MAT_RECEIVE_ITEM + " RMI" +
                         " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                         " ON RMI." + fieldNames[FLD_MATERIAL_ID] +
                         " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
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
                Ksg ksg = new Ksg();
                Color color = new Color();
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
                matreceiveitem.setUnitKonversi(rs.getLong("UNIT_KONVERSI"));
                matreceiveitem.setQtyEntry(rs.getDouble("QTY_ENTRI"));
                matreceiveitem.setBerat(rs.getDouble(27));
                matreceiveitem.setRemark(rs.getString(28));    
                matreceiveitem.setSortStatus(rs.getInt(35));
                matreceiveitem.setGondolaId(rs.getLong(36));
                matreceiveitem.setColorId(rs.getLong(37));
                temp.add(matreceiveitem);

                mat.setSku(rs.getString(2));
                mat.setName(rs.getString(3));
                mat.setDefaultPrice(rs.getDouble(14));
                mat.setRequiredSerialNumber(rs.getInt(19));
                mat.setDefaultStockUnitId(rs.getLong(21));
                mat.setCurrBuyPrice(rs.getDouble("MAT." +  PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE]));
                mat.setBarCode(rs.getString("BARCODE"));
                mat.setPosKadar(rs.getLong(30));
                mat.setGondolaCode(rs.getLong(31));
                mat.setMaterialJenisType(rs.getInt(32));
                mat.setCategoryId(rs.getLong(33));
                mat.setPosColor(rs.getLong(34));
                mat.setAveragePrice(rs.getLong(38));
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
	
	public static Vector listVectorRecItemCompleteDutyFree(int limitStart, int recordToGet, String whereClause, String order) {
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
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE] +
                         " , RMI." + fieldNames[FLD_UNIT_KONVERSI_ID] +" AS UNIT_KONVERSI "+
                         " , RMI." + fieldNames[FLD_QTY_ENTRI] +" AS QTY_ENTRI "+
                         " , RMI." + fieldNames[FLD_BERAT] +
                         " , RMI." + fieldNames[FLD_REMARK] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_KADAR_ID] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_COLOR_ID] +
                         " , RMI." + fieldNames[FLD_SORTING_STATUS] +
                         " , RMI." + fieldNames[FLD_GONDOLA_ID] +
                         " , RMI." + fieldNames[FLD_COLOR_ID] +
                         " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] +
						 " , REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_JENIS_DOKUMEN] +
						 " , REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_NOMOR_BC] +
						 " , REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_TANGGAL_BC] +
						 " , CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY] +
						 " , CAT." + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                         " FROM " + TBL_MAT_RECEIVE_ITEM + " RMI" +
                         " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
                         " ON RMI." + fieldNames[FLD_MATERIAL_ID] +
                         " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
						 " INNER JOIN " + PstMatReceive.TBL_MAT_RECEIVE + " REC" +
                         " ON RMI." + fieldNames[FLD_RECEIVE_MATERIAL_ID] +
                         " = REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
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
                Ksg ksg = new Ksg();
                Color color = new Color();
                CurrencyType curr = new CurrencyType();
				MatReceive matReceive = new MatReceive();
				Category cat = new Category();

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
                matreceiveitem.setUnitKonversi(rs.getLong("UNIT_KONVERSI"));
                matreceiveitem.setQtyEntry(rs.getDouble("QTY_ENTRI"));
                matreceiveitem.setBerat(rs.getDouble(27));
                matreceiveitem.setRemark(rs.getString(28));    
                matreceiveitem.setSortStatus(rs.getInt(35));
                matreceiveitem.setGondolaId(rs.getLong(36));
                matreceiveitem.setColorId(rs.getLong(37));
                temp.add(matreceiveitem);

                mat.setSku(rs.getString(2));
                mat.setName(rs.getString(3));
                mat.setDefaultPrice(rs.getDouble(14));
                mat.setRequiredSerialNumber(rs.getInt(19));
                mat.setDefaultStockUnitId(rs.getLong(21));
                mat.setCurrBuyPrice(rs.getDouble("MAT." +  PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE]));
                mat.setBarCode(rs.getString("BARCODE"));
                mat.setPosKadar(rs.getLong(30));
                mat.setGondolaCode(rs.getLong(31));
                mat.setMaterialJenisType(rs.getInt(32));
                mat.setCategoryId(rs.getLong(33));
                mat.setPosColor(rs.getLong(34));
                mat.setAveragePrice(rs.getLong(43));
                temp.add(mat);

                unit.setCode(rs.getString(4));
                temp.add(unit);

                curr.setOID(rs.getLong(6));
                curr.setCode(rs.getString("CUR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]));
                temp.add(curr);
				
				matReceive.setJenisDokumen(rs.getString(38));
				matReceive.setNomorBc(rs.getString(39));
				matReceive.setTglBc(rs.getDate(40));
				temp.add(matReceive);
						
				cat.setCode(rs.getString(42));
				cat.setName(rs.getString(41));
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
    
    public static void resultToObject(ResultSet rs, MatReceiveItem matreceiveitem) {
        try {
            matreceiveitem.setOID(rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]));
            matreceiveitem.setReceiveMaterialId(rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]));
            matreceiveitem.setMaterialId(rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]));
            matreceiveitem.setExpiredDate(rs.getDate(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE]));
            matreceiveitem.setUnitId(rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID]));
            matreceiveitem.setCost(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST]));
            matreceiveitem.setCurrencyId(rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID]));
            matreceiveitem.setQty(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]));
            matreceiveitem.setDiscount(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT]));
            matreceiveitem.setTotal(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL]));
            matreceiveitem.setResidueQty(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RESIDUE_QTY]));
            matreceiveitem.setDiscount2(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT2]));
            matreceiveitem.setDiscNominal(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISC_NOMINAL]));
            matreceiveitem.setCurrBuyingPrice(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURR_BUYING_PRICE]));
            matreceiveitem.setForwarderCost(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_FORWADER_COST]));
            matreceiveitem.setQtyEntry(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY_ENTRI]));
            matreceiveitem.setUnitKonversi(rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_KONVERSI_ID]));
            matreceiveitem.setPriceKonv(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_PRICE_KONVERSI]));
            matreceiveitem.setBonus(rs.getInt(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS]));
            matreceiveitem.setBerat(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BERAT]));
            matreceiveitem.setCashBillMainId(rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CASH_BILL_MAIN_ID]));
            matreceiveitem.setCashBillDetailId(rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CASH_BILL_DETAIL_ID]));
            matreceiveitem.setRemark(rs.getString(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_REMARK]));
           // matreceiveitem.setSupplierId(rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]));
           matreceiveitem.setSortStatus(rs.getInt(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_SORTING_STATUS]));
           matreceiveitem.setPrevMaterialId(rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_PREV_MATERIAL_ID]));
           matreceiveitem.setColorId(rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COLOR_ID]));
           matreceiveitem.setGondolaId(rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_GONDOLA_ID]));
            
        } catch (Exception e) {
        }
    }


     public static void resultToObjectLastReceive(ResultSet rs, MatReceiveItem matreceiveitem) {
        try {
            matreceiveitem.setReceiveDate(rs.getDate(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]));
            matreceiveitem.setMaterialId(rs.getLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]));
            matreceiveitem.setCost(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST]));
            matreceiveitem.setRecCode(rs.getString(PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE]));
            matreceiveitem.setReceiveMaterialId(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]));
            matreceiveitem.setSupplierId(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID]));
            matreceiveitem.setExchangeRate(rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE]));
            matreceiveitem.setQty(rs.getDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]));
            
            //matreceiveitem.setBonus(rs.getInt(PstMatReceive.fieldNames[PstMatReceive.fld_]));
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
                    MatReceiveItem matreceiveitem = (MatReceiveItem) list.get(ls);
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
    
    /**
     * add opie-eyek 20141210
     * @param whereClause
     * @return 
     */
    public static double getTotalAmount(String whereClause) {
        DBResultSet dbrs = null;
        double amount = 0;
        try {
            String sql = "SELECT SUM(" + fieldNames[FLD_COST] +"*"+fieldNames[FLD_QTY]+") AS MNT FROM " + TBL_MAT_RECEIVE_ITEM;
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
            String sql = " select sum(rcvi."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL]+") * ";
            sql += " rcv."+PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE]+" as amount";
            sql += " from "+PstMatReceive.TBL_MAT_RECEIVE+" rcv";
            sql += " inner join "+PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM+" rcvi";
            sql += " on rcv."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID];
            sql += " = rcvi."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID];
            
            if (oidRcv != 0)
                sql += " where "+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]+" = "+oidRcv;
            
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
            String sql = "SELECT ITM." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
                         " FROM " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " AS ITM " +
                         " INNER JOIN " + PstMatReceive.TBL_MAT_RECEIVE + " AS MAT " +
                         " ON ITM." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] +
                         " = MAT." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                         " WHERE ITM." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial +
                         " AND ITM." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] + "=" + oidMaterial;
            //System.out.println("PstMatReceiveItem.materialExist.sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                bool = true;
                break;
            }
        } catch (Exception e) {
            System.out.println("PstMatReceiveItem.materialExist.err : " + e.toString());
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
            throw new DBException(new PstMatReceiveItem(0), DBException.UNKNOWN);
        }
        return hasil;
    }

    /**
     * For searching cost by oidDfRecGroup
     * By Mirahu
     */
     public static double getCostTarget(long oidRecItem, long oidDfRecGroup) throws DBException {
        long hasil = 0;
        try {
            String sql = "SELECT RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST] +
                         " FROM " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " AS RMI " +
                         " INNER JOIN " + PstMatDispatchReceiveItem.TBL_MAT_DISPATCH_RECEIVE_ITEM + " AS DFRI " +
                         " ON DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID] +
                         " = RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID] +
                         " WHERE RMI." + PstMatReceive.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidRecItem +
                         " AND DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID] + "=" + oidDfRecGroup +
                         " AND RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] + "!= 0";
            int result = execUpdate(sql);
            //hasil = oid;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatReceiveItem(0), DBException.UNKNOWN);
        }
        return hasil;
    }
     
    
     /** Fungsi ini digunakan untuk mendapatkan qty barang yang baru diterima berdasarkan po
     * create by: Mirahu@dimata 27 April 2012
     */
    public static double getQtyReceive(long oidPurchaseOrder, long materialId) {
        DBResultSet dbrs = null;
        double amount = 0;
        try {
            String sql = " select sum(rcvi."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY]+") as amount";
            sql += " from "+PstMatReceive.TBL_MAT_RECEIVE+" rcv";
            sql += " inner join "+PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM+" rcvi";
            sql += " on rcv."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID];
            sql += " = rcvi."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID];
            sql += " where rcv."+PstMatReceive.fieldNames[PstMatReceive.FLD_PURCHASE_ORDER_ID]+" = "+oidPurchaseOrder;
            sql += " and rcvi."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]+" = "+materialId;
            sql += " and (rcv. "+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+" = 2";
            sql += " or rcv. "+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+" = 5)";
            
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

    
    
    /** di gunakan untuk mencari object receive item
     * @param invoice = invoice supplier
     * @param oidMaterial = oid material/barang
     * @return
     */
    public static MatReceiveItem getObjectReceiveItem(String invoice, long recFromDispatchId, long oidMaterial) {
        MatReceiveItem matreceiveitem = new MatReceiveItem();
        try {
            String whereClause = "";
            if (recFromDispatchId == 0) {
                whereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER] + "='" + invoice + "'";
            } else {
                whereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] + "=" + recFromDispatchId;
            }
            Vector vt = PstMatReceive.list(0, 0, whereClause, "");
            MatReceive matreceive = new MatReceive();
            if (vt != null && vt.size() > 0) {
                matreceive = (MatReceive) vt.get(0);
            }
            
            whereClause = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + matreceive.getOID() +
                          " AND " + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] + "=" + oidMaterial;
            Vector vect = PstMatReceiveItem.list(0, 0, whereClause, "");
            if (vect != null && vect.size() > 0) {
                matreceiveitem = (MatReceiveItem) vect.get(0);
            }
            
        } catch (Exception e) {
            System.out.println("Error getObjectReceiveItem : "+e.toString());
        }
        return matreceiveitem;
    }
    
    
    
     public static boolean discountExist(long oidReceiveMaterial) {
        DBResultSet dbrs = null;
        boolean bool = false;
        try {
            String sql = "SELECT ITM." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] +
                         " FROM " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " AS ITM " +
                         " WHERE ITM." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial+
                         " AND ITM." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS] + "='1'" ;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                bool = true;
                break;
            }
        } catch (Exception e) {
            System.out.println("PstMatReceiveItem.materialExist.err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return bool;
        }
    }
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MatReceiveItem matReceiveItem = PstMatReceiveItem.fetchExc(oid);
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID], matReceiveItem.getOID());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID], matReceiveItem.getReceiveMaterialId());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID], matReceiveItem.getMaterialId());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_EXPIRED_DATE], matReceiveItem.getExpiredDate());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID], matReceiveItem.getUnitId());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST], matReceiveItem.getCost());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID], matReceiveItem.getCurrencyId());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY], matReceiveItem.getQty());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT], matReceiveItem.getDiscount());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL], matReceiveItem.getTotal());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RESIDUE_QTY], matReceiveItem.getResidueQty());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT2], matReceiveItem.getDiscount2());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISC_NOMINAL], matReceiveItem.getDiscNominal());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURR_BUYING_PRICE], matReceiveItem.getCurrBuyingPrice());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_FORWADER_COST], matReceiveItem.getForwarderCost());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY_ENTRI], matReceiveItem.getQtyEntry());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_KONVERSI_ID], matReceiveItem.getUnitKonversi());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_PRICE_KONVERSI], matReceiveItem.getPriceKonv());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS], matReceiveItem.getBonus());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BERAT], matReceiveItem.getBerat());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CASH_BILL_MAIN_ID], matReceiveItem.getCashBillMainId());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CASH_BILL_DETAIL_ID], matReceiveItem.getCashBillDetailId());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_REMARK], matReceiveItem.getRemark());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BERAT_AWAL], matReceiveItem.getBeratAwal());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RUSAK], matReceiveItem.getRusak());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_SORTING_STATUS], matReceiveItem.getSortStatus());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_PREV_MATERIAL_ID], matReceiveItem.getPrevMaterialId());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COLOR_ID], matReceiveItem.getColorId());
         object.put(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_GONDOLA_ID], matReceiveItem.getGondolaId());
      }catch(Exception exc){}
      return object;
   }  
   
   public static long syncExc(JSONObject jSONObject){
	   long oid = 0;
	   if (jSONObject != null) {
		   oid = jSONObject.optLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID], 0);
		   if (oid > 0) {
			   MatReceiveItem matReceiveItem = new MatReceiveItem();
			   matReceiveItem.setOID(jSONObject.optLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID], 0));
			   matReceiveItem.setReceiveMaterialId(jSONObject.optLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID], 0));
			   matReceiveItem.setMaterialId(jSONObject.optLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID], 0));
			   matReceiveItem.setExpiredDate(Formater.formatDate(jSONObject.optString(PstMatReceive.fieldNames[PstMatReceive.FLD_EXPIRED_DATE], "0000-00-00"), "yyyy-MM-dd"));
			   matReceiveItem.setUnitId(jSONObject.optLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_ID], 0));
			   matReceiveItem.setCost(jSONObject.optDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COST], 0));
			   matReceiveItem.setCurrencyId(jSONObject.optLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURRENCY_ID], 0));
			   matReceiveItem.setQty(jSONObject.optDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY], 0));
			   matReceiveItem.setDiscount(jSONObject.optDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT], 0));
			   matReceiveItem.setTotal(jSONObject.optDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_TOTAL], 0));
			   matReceiveItem.setResidueQty(jSONObject.optDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RESIDUE_QTY], 0));
			   matReceiveItem.setDiscount2(jSONObject.optDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISCOUNT2], 0));
			   matReceiveItem.setDiscNominal(jSONObject.optDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_DISC_NOMINAL], 0));
			   matReceiveItem.setCurrBuyingPrice(jSONObject.optDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CURR_BUYING_PRICE], 0));
			   matReceiveItem.setForwarderCost(jSONObject.optDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_FORWADER_COST], 0));
			   matReceiveItem.setQtyEntry(jSONObject.optDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_QTY_ENTRI], 0));
			   matReceiveItem.setUnitKonversi(jSONObject.optLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_UNIT_KONVERSI_ID], 0));
			   matReceiveItem.setPriceKonv(jSONObject.optDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_PRICE_KONVERSI], 0));
			   matReceiveItem.setBonus(jSONObject.optInt(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS], 0));
			   matReceiveItem.setBerat(jSONObject.optDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BERAT], 0));
			   matReceiveItem.setCashBillMainId(jSONObject.optLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CASH_BILL_MAIN_ID], 0));
			   matReceiveItem.setCashBillDetailId(jSONObject.optLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CASH_BILL_DETAIL_ID], 0));
			   matReceiveItem.setRemark(jSONObject.optString(PstMatReceive.fieldNames[PstMatReceive.FLD_REMARK], ""));
			   matReceiveItem.setSortStatus(jSONObject.optInt(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_SORTING_STATUS], 0));
			   matReceiveItem.setPrevMaterialId(jSONObject.optLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_PREV_MATERIAL_ID], 0));
			   matReceiveItem.setBeratAwal(jSONObject.optDouble(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BERAT_AWAL], 0));
			   matReceiveItem.setRusak(jSONObject.optInt(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RUSAK], 0));
			   matReceiveItem.setColorId(jSONObject.optLong(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_COLOR_ID], 0));
			   matReceiveItem.setGondolaId(jSONObject.optInt(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_GONDOLA_ID], 0));

			   
			   boolean checkOidMatRecItem = PstMatReceiveItem.checkOID(oid);
			   try {
				   if (checkOidMatRecItem) {
					   oid = PstMatReceiveItem.updateExc(matReceiveItem);
				   } else {
					   oid = PstMatReceiveItem.insertByOid(matReceiveItem);
				   }
			   } catch (Exception exc) {
				   oid = 0;
			   }
		   }
	   }
	   return oid;
   }
   
   public static long insertByOid(MatReceiveItem matreceiveitem) throws DBException {
        try {
            PstMatReceiveItem pstMatReceiveItem = new PstMatReceiveItem(0);
            
            pstMatReceiveItem.setLong(FLD_RECEIVE_MATERIAL_ID, matreceiveitem.getReceiveMaterialId());
            pstMatReceiveItem.setLong(FLD_MATERIAL_ID, matreceiveitem.getMaterialId());
            pstMatReceiveItem.setDate(FLD_EXPIRED_DATE, matreceiveitem.getExpiredDate());
            pstMatReceiveItem.setLong(FLD_UNIT_ID, matreceiveitem.getUnitId());
            pstMatReceiveItem.setDouble(FLD_COST, matreceiveitem.getCost());
            pstMatReceiveItem.setLong(FLD_CURRENCY_ID, matreceiveitem.getCurrencyId());
            pstMatReceiveItem.setDouble(FLD_QTY, matreceiveitem.getQty());
            pstMatReceiveItem.setDouble(FLD_DISCOUNT, matreceiveitem.getDiscount());
            pstMatReceiveItem.setDouble(FLD_TOTAL, matreceiveitem.getTotal());
            pstMatReceiveItem.setDouble(FLD_RESIDUE_QTY, matreceiveitem.getResidueQty());
            pstMatReceiveItem.setDouble(FLD_DISCOUNT2, matreceiveitem.getDiscount2());
            pstMatReceiveItem.setDouble(FLD_DISC_NOMINAL, matreceiveitem.getDiscNominal());
            pstMatReceiveItem.setDouble(FLD_CURR_BUYING_PRICE, matreceiveitem.getCurrBuyingPrice());
            pstMatReceiveItem.setDouble(FLD_FORWADER_COST, matreceiveitem.getForwarderCost());
            pstMatReceiveItem.setDouble(FLD_QTY_ENTRI, matreceiveitem.getQtyEntry());
            pstMatReceiveItem.setLong(FLD_UNIT_KONVERSI_ID,matreceiveitem.getUnitKonversi());
            pstMatReceiveItem.setDouble(FLD_PRICE_KONVERSI, matreceiveitem.getPriceKonv());
            pstMatReceiveItem.setInt(FLD_BONUS, matreceiveitem.getBonus());
            pstMatReceiveItem.setDouble(FLD_BERAT, matreceiveitem.getBerat());
            pstMatReceiveItem.setLong(FLD_CASH_BILL_MAIN_ID, matreceiveitem.getCashBillMainId());
            pstMatReceiveItem.setLong(FLD_CASH_BILL_DETAIL_ID, matreceiveitem.getCashBillDetailId());
            pstMatReceiveItem.setString(FLD_REMARK, matreceiveitem.getRemark());
            pstMatReceiveItem.setInt(FLD_SORTING_STATUS, matreceiveitem.getSortStatus());
            pstMatReceiveItem.setLong(FLD_PREV_MATERIAL_ID, matreceiveitem.getPrevMaterialId());
            pstMatReceiveItem.setDouble(FLD_BERAT_AWAL, matreceiveitem.getBeratAwal());
            pstMatReceiveItem.setInt(FLD_RUSAK, matreceiveitem.getRusak());
            pstMatReceiveItem.setLong(FLD_COLOR_ID, matreceiveitem.getColorId());
            pstMatReceiveItem.setLong(FLD_GONDOLA_ID, matreceiveitem.getGondolaId());
            
            pstMatReceiveItem.insertByOid(matreceiveitem.getOID());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatReceiveItem(0), DBException.UNKNOWN);
        }
        return matreceiveitem.getOID();
    }
   
}
