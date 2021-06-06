package com.dimata.posbo.entity.warehouse;
/*
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.I_Document;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.util.lang.I_Language;
import com.dimata.util.Command;
import com.dimata.posbo.session.warehouse.SessMatCosting;
import com.dimata.posbo.db.*;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;

import java.util.Vector;
import java.util.Date;
import java.sql.ResultSet;
//import connection
import java.sql.Connection;*/
/* package qdep */
import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Date;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.I_Document;
import java.sql.Connection;
import java.util.Vector;
import com.dimata.posbo.session.warehouse.SessMatCosting;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.util.Command;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import org.json.JSONObject;

public class PstMatCosting extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_Document {

    public static final String TBL_COSTING = "pos_costing_material";

    public static final int FLD_COSTING_MATERIAL_ID = 0;
    public static final int FLD_LOCATION_ID = 1;
    public static final int FLD_COSTING_TO = 2;
    public static final int FLD_LOCATION_TYPE = 3;
    public static final int FLD_COSTING_DATE = 4;
    public static final int FLD_COSTING_CODE = 5;
    public static final int FLD_COSTING_CODE_COUNTER = 6;
    public static final int FLD_COSTING_STATUS = 7;
    public static final int FLD_REMARK = 8;
    public static final int FLD_INVOICE_SUPPLIER = 9;
    public static final int FLD_TRANSFER_STATUS = 10;
    public static final int FLD_LAST_UPDATE = 11;
    public static final int FLD_COSTING_ID = 12;
    public static final int FLD_CASH_CASHIER_ID = 13;
    public static final int FLD_ENABLE_STOCK_FISIK = 14;
    public  static final int FLD_CONTACT_ID = 15;
    public  static final int FLD_DOCUMENT_ID = 16;
    
    public static final String[] fieldNames = {
        "COSTING_MATERIAL_ID",
        "LOCATION_ID",
        "COSTING_TO",
        "LOCATION_TYPE",
        "COSTING_DATE",
        "COSTING_CODE",
        "COSTING_CODE_COUNTER",
        "COSTING_STATUS",
        "REMARK",
        "INVOICE_SUPPLIER",
        "TRANSFER_STATUS",
        "LAST_UPDATE",
        //add column costing_id
        "COSTING_ID",
        //add_column cash_cashier_id
        "CASH_CASHIER_ID",
        //add column enable_stock_fisik
        "ENABLE_STOCK_FISIK",
        
        "CONTACT_ID",
        "DOCUMENT_ID"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_DATE,
        //add column costing_id
        TYPE_LONG,
        //add column cash_cashier_id
        TYPE_LONG,
        //add column enable stock fisik
        TYPE_INT,
        
        TYPE_LONG,
        TYPE_LONG
    };

    public static final int FLD_TYPE_COSTING_LOCATION_STORE = 0;
    public static final int FLD_TYPE_COSTING_LOCATION_WAREHOUSE = 1;
    public static final int FLD_TYPE_COSTING_LOCATION_HPP = 4;

    public static final String[] fieldTypeCosting = {
        "Store",
        "Warehouse"
    };

    public PstMatCosting() {
    }

    public PstMatCosting(int i) throws DBException {
        super(new PstMatCosting());
    }

    public PstMatCosting(String sOid) throws DBException {
        super(new PstMatCosting(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstMatCosting(long lOid) throws DBException {
        super(new PstMatCosting(0));
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
        return TBL_COSTING;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMatCosting().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        MatCosting matCosting = fetchExc(ent.getOID());
        ent = (Entity) matCosting;
        return matCosting.getOID();
    }

    synchronized public long insertExc(Entity ent) throws Exception {
        return insertExc((MatCosting) ent);
    }

    synchronized public long updateExc(Entity ent) throws Exception {
        return updateExc((MatCosting) ent);
    }

    synchronized public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static MatCosting fetchExc(long oid) throws DBException {
        try {
            MatCosting matCosting = new MatCosting();
            PstMatCosting pstMatCosting = new PstMatCosting(oid);
            matCosting.setOID(oid);

            matCosting.setLocationId(pstMatCosting.getlong(FLD_LOCATION_ID));
            matCosting.setCostingTo(pstMatCosting.getlong(FLD_COSTING_TO));
            matCosting.setLocationType(pstMatCosting.getInt(FLD_LOCATION_TYPE));
            matCosting.setCostingDate(pstMatCosting.getDate(FLD_COSTING_DATE));
            matCosting.setCostingCode(pstMatCosting.getString(FLD_COSTING_CODE));
            matCosting.setCostingCodeCounter(pstMatCosting.getInt(FLD_COSTING_CODE_COUNTER));
            matCosting.setCostingStatus(pstMatCosting.getInt(FLD_COSTING_STATUS));
            matCosting.setRemark(pstMatCosting.getString(FLD_REMARK));
            matCosting.setInvoiceSupplier(pstMatCosting.getString(FLD_INVOICE_SUPPLIER));
            matCosting.setTransferStatus(pstMatCosting.getInt(FLD_TRANSFER_STATUS));
            matCosting.setLastUpdate(pstMatCosting.getDate(FLD_LAST_UPDATE));
            //add column costing_id
            matCosting.setCostingId(pstMatCosting.getlong(FLD_COSTING_ID));
            //add column cash_cashier_id
            matCosting.setCashCashierId(pstMatCosting.getlong(FLD_CASH_CASHIER_ID));
            //add column enableStockFisik
            matCosting.setEnableStockFisik(pstMatCosting.getInt(FLD_ENABLE_STOCK_FISIK));

            matCosting.setContactId(pstMatCosting.getlong(FLD_CONTACT_ID));
            //Ed 
            matCosting.setDocumentId(pstMatCosting.getlong(FLD_DOCUMENT_ID));
                    
            return matCosting;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatCosting(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(MatCosting matCosting) throws DBException {
        try {
            PstMatCosting pstMatCosting = new PstMatCosting(0);

            pstMatCosting.setLong(FLD_LOCATION_ID, matCosting.getLocationId());
            pstMatCosting.setLong(FLD_COSTING_TO, matCosting.getCostingTo());
            pstMatCosting.setInt(FLD_LOCATION_TYPE, matCosting.getLocationType());
            pstMatCosting.setDate(FLD_COSTING_DATE, matCosting.getCostingDate());
            pstMatCosting.setString(FLD_COSTING_CODE, matCosting.getCostingCode());
            pstMatCosting.setInt(FLD_COSTING_CODE_COUNTER, matCosting.getCostingCodeCounter());
            pstMatCosting.setInt(FLD_COSTING_STATUS, matCosting.getCostingStatus());
            pstMatCosting.setString(FLD_REMARK, matCosting.getRemark());
            pstMatCosting.setString(FLD_INVOICE_SUPPLIER, matCosting.getInvoiceSupplier());
            pstMatCosting.setInt(FLD_TRANSFER_STATUS, matCosting.getTransferStatus());
            pstMatCosting.setDate(FLD_LAST_UPDATE, matCosting.getLastUpdate());
            //add column costing_id
            pstMatCosting.setLong(FLD_COSTING_ID, matCosting.getCostingId());
            //add column cash_cashier_id
            pstMatCosting.setLong(FLD_CASH_CASHIER_ID, matCosting.getCashCashierId());
            //add enable stock fisik
            pstMatCosting.setInt(FLD_ENABLE_STOCK_FISIK, matCosting.getEnableStockFisik());
            
            pstMatCosting.setLong(FLD_CONTACT_ID, matCosting.getContactId());
            //Ed
            pstMatCosting.setLong(FLD_DOCUMENT_ID, matCosting.getDocumentId());
            
            pstMatCosting.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatCosting.getInsertSQL());
            matCosting.setOID(pstMatCosting.getlong(FLD_COSTING_MATERIAL_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatCosting(0), DBException.UNKNOWN);
        }
        return matCosting.getOID();
    }
    
    
     public static long insertExcByOid(MatCosting matCosting) throws DBException {
        try {
            PstMatCosting pstMatCosting = new PstMatCosting(0);

            pstMatCosting.setLong(FLD_LOCATION_ID, matCosting.getLocationId());
            pstMatCosting.setLong(FLD_COSTING_TO, matCosting.getCostingTo());
            pstMatCosting.setInt(FLD_LOCATION_TYPE, matCosting.getLocationType());
            pstMatCosting.setDate(FLD_COSTING_DATE, matCosting.getCostingDate());
            pstMatCosting.setString(FLD_COSTING_CODE, matCosting.getCostingCode());
            pstMatCosting.setInt(FLD_COSTING_CODE_COUNTER, matCosting.getCostingCodeCounter());
            pstMatCosting.setInt(FLD_COSTING_STATUS, matCosting.getCostingStatus());
            pstMatCosting.setString(FLD_REMARK, matCosting.getRemark());
            pstMatCosting.setString(FLD_INVOICE_SUPPLIER, matCosting.getInvoiceSupplier());
            pstMatCosting.setInt(FLD_TRANSFER_STATUS, matCosting.getTransferStatus());
            pstMatCosting.setDate(FLD_LAST_UPDATE, matCosting.getLastUpdate());
            //add column costing_id
            pstMatCosting.setLong(FLD_COSTING_ID, matCosting.getCostingId());
            //add column cash_cashier_id
            pstMatCosting.setLong(FLD_CASH_CASHIER_ID, matCosting.getCashCashierId());
            //add enable stock fisik
            pstMatCosting.setInt(FLD_ENABLE_STOCK_FISIK, matCosting.getEnableStockFisik());
            
            pstMatCosting.setLong(FLD_CONTACT_ID, matCosting.getContactId());
            //Ed
            pstMatCosting.setLong(FLD_DOCUMENT_ID, matCosting.getDocumentId());
            
            pstMatCosting.insertByOid(matCosting.getOID());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatCosting(0), DBException.UNKNOWN);
        }
        return matCosting.getOID();
    }
    
    
    //insert with transcation con by mirahu 20120913
    
    public static long insertExc(MatCosting matCosting, Connection objConnection) throws DBException {
        try {
            PstMatCosting pstMatCosting = new PstMatCosting(0);

            pstMatCosting.setLong(FLD_LOCATION_ID, matCosting.getLocationId());
            pstMatCosting.setLong(FLD_COSTING_TO, matCosting.getCostingTo());
            pstMatCosting.setInt(FLD_LOCATION_TYPE, matCosting.getLocationType());
            pstMatCosting.setDate(FLD_COSTING_DATE, matCosting.getCostingDate());
            pstMatCosting.setString(FLD_COSTING_CODE, matCosting.getCostingCode());
            pstMatCosting.setInt(FLD_COSTING_CODE_COUNTER, matCosting.getCostingCodeCounter());
            pstMatCosting.setInt(FLD_COSTING_STATUS, matCosting.getCostingStatus());
            pstMatCosting.setString(FLD_REMARK, matCosting.getRemark());
            pstMatCosting.setString(FLD_INVOICE_SUPPLIER, matCosting.getInvoiceSupplier());
            pstMatCosting.setInt(FLD_TRANSFER_STATUS, matCosting.getTransferStatus());
            pstMatCosting.setDate(FLD_LAST_UPDATE, matCosting.getLastUpdate());
            //add column costing_id
            pstMatCosting.setLong(FLD_COSTING_ID, matCosting.getCostingId());
            //add column cash_cashier_id
            pstMatCosting.setLong(FLD_CASH_CASHIER_ID, matCosting.getCashCashierId());
            //add enable stock fisik
            pstMatCosting.setInt(FLD_ENABLE_STOCK_FISIK, matCosting.getEnableStockFisik());
            
            pstMatCosting.setLong(FLD_CONTACT_ID, matCosting.getContactId());
            //Ed
            pstMatCosting.setLong(FLD_DOCUMENT_ID, matCosting.getDocumentId());
            
            pstMatCosting.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatCosting.getInsertSQL());
            matCosting.setOID(pstMatCosting.getlong(FLD_COSTING_MATERIAL_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatCosting(0), DBException.UNKNOWN);
        }
        return matCosting.getOID();
    }

    public static long updateExc(MatCosting matCosting) throws DBException {
        try {
            if (matCosting.getOID() != 0) {
                PstMatCosting pstMatCosting = new PstMatCosting(matCosting.getOID());

                pstMatCosting.setLong(FLD_LOCATION_ID, matCosting.getLocationId());
                pstMatCosting.setLong(FLD_COSTING_TO, matCosting.getCostingTo());
                pstMatCosting.setInt(FLD_LOCATION_TYPE, matCosting.getLocationType());
                pstMatCosting.setDate(FLD_COSTING_DATE, matCosting.getCostingDate());
                pstMatCosting.setString(FLD_COSTING_CODE, matCosting.getCostingCode());
                pstMatCosting.setInt(FLD_COSTING_CODE_COUNTER, matCosting.getCostingCodeCounter());
                pstMatCosting.setInt(FLD_COSTING_STATUS, matCosting.getCostingStatus());
                pstMatCosting.setString(FLD_REMARK, matCosting.getRemark());
                pstMatCosting.setString(FLD_INVOICE_SUPPLIER, matCosting.getInvoiceSupplier());
                pstMatCosting.setInt(FLD_TRANSFER_STATUS, matCosting.getTransferStatus());
                pstMatCosting.setDate(FLD_LAST_UPDATE, matCosting.getLastUpdate());
                //add column costing_id
                pstMatCosting.setLong(FLD_COSTING_ID, matCosting.getCostingId());
                //Ed
                pstMatCosting.setLong(FLD_DOCUMENT_ID, matCosting.getDocumentId());

                //add column cash_cashier_id
                pstMatCosting.setLong(FLD_CASH_CASHIER_ID, matCosting.getCashCashierId());
                
                //adding column enable stock fisik
                pstMatCosting.setInt(FLD_ENABLE_STOCK_FISIK, matCosting.getEnableStockFisik());

                pstMatCosting.setLong(FLD_CONTACT_ID, matCosting.getContactId());
                
                pstMatCosting.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMatCosting.getUpdateSQL());
                
                return matCosting.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatCosting(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstMatCosting pstMatCosting = new PstMatCosting(oid);
            //Delete Item First
            PstMatCostingItem pstDFItem = new PstMatCostingItem();
            long result = pstDFItem.deleteExcByParent(oid);
            pstMatCosting.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatCosting.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatCosting(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_COSTING;
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

            //System.out.println("PstMatCosting.list() sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MatCosting matCosting = new MatCosting();
                resultToObject(rs, matCosting);
                lists.add(matCosting);
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

    public static void resultToObject(ResultSet rs, MatCosting matCosting) {
        try {
            matCosting.setOID(rs.getLong(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID]));
            matCosting.setLocationId(rs.getLong(PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID]));
            matCosting.setCostingTo(rs.getLong(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_TO]));
            matCosting.setLocationType(rs.getInt(PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_TYPE]));
            matCosting.setCostingDate(rs.getDate(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]));
            matCosting.setCostingCode(rs.getString(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE]));
            matCosting.setCostingCodeCounter(rs.getInt(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE_COUNTER]));
            matCosting.setCostingStatus(rs.getInt(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS]));
            matCosting.setRemark(rs.getString(PstMatCosting.fieldNames[PstMatCosting.FLD_REMARK]));
            matCosting.setInvoiceSupplier(rs.getString(PstMatCosting.fieldNames[PstMatCosting.FLD_INVOICE_SUPPLIER]));
            matCosting.setTransferStatus(rs.getInt(PstMatCosting.fieldNames[PstMatCosting.FLD_TRANSFER_STATUS]));
            //add column costing_id
            matCosting.setCostingId(rs.getLong(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_ID]));
            //add column cash cashier id
            matCosting.setCashCashierId(rs.getLong(PstMatCosting.fieldNames[PstMatCosting.FLD_CASH_CASHIER_ID]));
            //add column enable stock fisik
            matCosting.setEnableStockFisik(rs.getInt(PstMatCosting.fieldNames[PstMatCosting.FLD_ENABLE_STOCK_FISIK]));
            
            matCosting.setContactId(rs.getLong(PstMatCosting.fieldNames[PstMatCosting.FLD_CONTACT_ID]));
            //Ed
            matCosting.setDocumentId(rs.getLong(PstMatCosting.fieldNames[PstMatCosting.FLD_DOCUMENT_ID]));
            
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long matCostingId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_COSTING +
                    " WHERE " + fieldNames[FLD_COSTING_MATERIAL_ID] +
                    " = " + matCostingId;

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
        int hasil = 0;
        try {
            String sql = "SELECT COUNT(" + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] + ") FROM " + TBL_COSTING;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            hasil = count;
        } catch (Exception e) {
            hasil = 0;
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
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
                    MatCosting matCosting = (MatCosting) list.get(ls);
                    if (oid == matCosting.getOID())
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

    public static long AutoInsertDf(Vector listItem, long oidLocation, long oidCostingTo, int locType) {
        long hasil = 0;
        SessMatCosting sessCosting = new SessMatCosting();
        try {
            MatCosting mydf = new MatCosting();
            mydf.setCostingStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
            mydf.setCostingDate(new Date());
            mydf.setLocationId(oidLocation);
            mydf.setCostingTo(oidCostingTo);
            mydf.setLocationType(locType);
            mydf.setRemark("");
            mydf.setCostingCodeCounter(sessCosting.getMaxCostingCounter(new Date(), mydf) + 1);
            mydf.setCostingCode(sessCosting.generateCostingCode(mydf));
            //Insert dispatch
            hasil = PstMatCosting.insertExc(mydf);
            if (hasil != 0) {
                //Insert df item
                for (int i = 0; i < listItem.size(); i++) {
                    long oidDfItem = 0;
                    Vector temp = (Vector) listItem.get(i);
                    String sku = (String) temp.get(0);
                    if (sku.length() > 0) {
                        //Fetch material info
                        Material myMat = PstMaterial.fetchBySku(sku);
                        int quantity = Integer.parseInt((String) temp.get(1));
                        MatCostingItem dfItem = new MatCostingItem();
                        dfItem.setCostingMaterialId(hasil);
                        dfItem.setMaterialId(myMat.getOID());
                        dfItem.setUnitId(myMat.getDefaultStockUnitId());
                        dfItem.setQty(quantity);
                        try {
                            oidDfItem = PstMatCostingItem.insertExc(dfItem);
                        } catch (Exception e) {
                        }
                    } else {
                        oidDfItem = 1;
                    }
                    if (oidDfItem == 0) break;
                }
            }
        } catch (Exception ex) {
            System.out.println("AutoInsertDf : " + ex);
        }
        return hasil;
    }



    /*-------------------- start implements I_Document -------------------------*/
    /**
     * this method used to get status of 'document'
     * return int of currentDocumentStatus
     */
    public int getDocumentStatus(long documentId) {
        DBResultSet dbrs = null;
        try {
            MatCosting matCosting = fetchExc(documentId);
            return matCosting.getCostingStatus();
        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return -1;
    }

    /**
     * this method used to set status of 'document'
     * return int of currentDocumentStatus
     */
    public int setDocumentStatus(long documentId, int indexStatus) {
        /*DBResultSet dbrs = null;
        try
        {
            String sql = "UPDATE " + TBL_DISPATCH +
                " SET " + fieldNames[FLD_DISPATCH_STATUS] + " = " + indexStatus +
                " WHERE " + fieldNames[FLD_DISPATCH_MATERIAL_ID] + " = " + documentId;
            dbrs = DBHandler.execQueryResult(sql);*/
        return indexStatus;
        /*}
        catch(Exception e)
        {
            System.out.println("Err : "+e.toString());
        }
        finally
        {
            DBResultSet.close(dbrs);
        }
        return -1;*/
    }
    /*-------------------- end implements I_Document --------------------------*/


    /** digunakan untuk update status transfer data secara otomatis.
     *  jika residue qty item semuanya 0 maka status transfer CLOSED.
     *  jika residue qty masih ada yang lebih dari 0 maka status transfer DRAFT.
     * @param oid
     */
    public static void processUpdate(long oid) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(" + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_RESIDUE_QTY] + ") " +
                    " AS SUM_" + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_RESIDUE_QTY] +
                    " FROM " + PstMatCostingItem.TBL_MAT_COSTING_ITEM +
                    " WHERE " + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] + " = " + oid;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int sumqty = 0;
            while (rs.next()) {
                sumqty = rs.getInt("SUM_" + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_RESIDUE_QTY]);
            }

            MatCosting matCosting = new MatCosting();
            matCosting = PstMatCosting.fetchExc(oid);
            if (sumqty == 0) {
                matCosting.setTransferStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
            } else {
                matCosting.setTransferStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
            }
            // update transfer status
            PstMatCosting.updateExc(matCosting);

        } catch (Exception e) {
        }
    }
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MatCosting matCosting = PstMatCosting.fetchExc(oid);
         object.put(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID], matCosting.getOID());
         object.put(PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID], matCosting.getLocationId());
         object.put(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_TO], matCosting.getCostingTo());
         object.put(PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_TYPE], matCosting.getLocationType());
         object.put(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE], matCosting.getCostingDate());
         object.put(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE], matCosting.getCostingCode());
         object.put(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_CODE_COUNTER], matCosting.getCostingCodeCounter());
         object.put(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS], matCosting.getCostingStatus());
         object.put(PstMatCosting.fieldNames[PstMatCosting.FLD_REMARK], matCosting.getRemark());
         object.put(PstMatCosting.fieldNames[PstMatCosting.FLD_INVOICE_SUPPLIER], matCosting.getInvoiceSupplier());
         object.put(PstMatCosting.fieldNames[PstMatCosting.FLD_TRANSFER_STATUS], matCosting.getTransferStatus());
         object.put(PstMatCosting.fieldNames[PstMatCosting.FLD_LAST_UPDATE], matCosting.getLastUpdate());
         object.put(PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_ID], matCosting.getCostingId());
         object.put(PstMatCosting.fieldNames[PstMatCosting.FLD_CASH_CASHIER_ID], matCosting.getCashCashierId());
         object.put(PstMatCosting.fieldNames[PstMatCosting.FLD_ENABLE_STOCK_FISIK], matCosting.getEnableStockFisik());
         object.put(PstMatCosting.fieldNames[PstMatCosting.FLD_CONTACT_ID], matCosting.getContactId());
         object.put(PstMatCosting.fieldNames[PstMatCosting.FLD_DOCUMENT_ID], matCosting.getDocumentId());
      }catch(Exception exc){}
      return object;
   }
}
