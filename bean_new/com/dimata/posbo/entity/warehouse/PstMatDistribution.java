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

/* package garment */
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.session.warehouse.*;
import com.dimata.common.entity.location.*;
import org.json.JSONObject;

public class PstMatDistribution extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_Document {

    public static final String TBL_DISTRIBUTION = "pos_distribution_material";

    public static final int FLD_DISTRIBUTION_MATERIAL_ID = 0;
    public static final int FLD_LOCATION_ID = 1;
    public static final int FLD_LOCATION_TYPE = 2;
    public static final int FLD_DISTRIBUTION_DATE = 3;
    public static final int FLD_DISTRIBUTION_CODE = 4;
    public static final int FLD_DISTRIBUTION_CODE_COUNTER = 5;
    public static final int FLD_DISTRIBUTION_STATUS = 6;
    public static final int FLD_REMARK = 7;
    public static final int FLD_INVOICE_SUPPLIER = 8;


    public static final String[] fieldNames = {
        "DISTRIBUTION_MATERIAL_ID",
        "LOCATION_ID",
        "LOCATION_TYPE",
        "DISTRIBUTION_DATE",
        "DISTRIBUTION_CODE",
        "DISTRIBUTION_CODE_COUNTER",
        "DISTRIBUTION_STATUS",
        "REMARK",
        "INVOICE_SUPPLIER"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_INT,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING
    };

    public static final int FLD_TYPE_DISTRIBUTION_LOCATION_STORE = 0;
    public static final int FLD_TYPE_DISTRIBUTION_LOCATION_WAREHOUSE = 1;

    public static final String[] fieldTypeDispatch = {
        "Store",
        "Warehouse"
    };

    public PstMatDistribution() {
    }

    public PstMatDistribution(int i) throws DBException {
        super(new PstMatDistribution());
    }

    public PstMatDistribution(String sOid) throws DBException {
        super(new PstMatDistribution(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstMatDistribution(long lOid) throws DBException {
        super(new PstMatDistribution(0));
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
        return TBL_DISTRIBUTION;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMatDistribution().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        MatDistribution matDistribution = fetchExc(ent.getOID());
        ent = (Entity) matDistribution;
        return matDistribution.getOID();
    }

    synchronized public long insertExc(Entity ent) throws Exception {
        return insertExc((MatDistribution) ent);
    }

    synchronized public long updateExc(Entity ent) throws Exception {
        return updateExc((MatDistribution) ent);
    }

    synchronized public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static MatDistribution fetchExc(long oid) throws DBException {
        try {
            MatDistribution matDistribution = new MatDistribution();
            PstMatDistribution PstMatDistribution = new PstMatDistribution(oid);
            matDistribution.setOID(oid);

            matDistribution.setLocationId(PstMatDistribution.getlong(FLD_LOCATION_ID));
            matDistribution.setLocationType(PstMatDistribution.getInt(FLD_LOCATION_TYPE));
            matDistribution.setDistributionDate(PstMatDistribution.getDate(FLD_DISTRIBUTION_DATE));
            matDistribution.setDistributionCode(PstMatDistribution.getString(FLD_DISTRIBUTION_CODE));
            matDistribution.setDistributionCodeCounter(PstMatDistribution.getInt(FLD_DISTRIBUTION_CODE_COUNTER));
            matDistribution.setDistributionStatus(PstMatDistribution.getInt(FLD_DISTRIBUTION_STATUS));
            matDistribution.setRemark(PstMatDistribution.getString(FLD_REMARK));
            matDistribution.setInvoiceSupplier(PstMatDistribution.getString(FLD_INVOICE_SUPPLIER));

            return matDistribution;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatDistribution(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(MatDistribution matDistribution) throws DBException {
        try {
            PstMatDistribution PstMatDistribution = new PstMatDistribution(0);

            PstMatDistribution.setLong(FLD_LOCATION_ID, matDistribution.getLocationId());
            PstMatDistribution.setInt(FLD_LOCATION_TYPE, matDistribution.getLocationType());
            PstMatDistribution.setDate(FLD_DISTRIBUTION_DATE, matDistribution.getDistributionDate());
            PstMatDistribution.setString(FLD_DISTRIBUTION_CODE, matDistribution.getDistributionCode());
            PstMatDistribution.setInt(FLD_DISTRIBUTION_CODE_COUNTER, matDistribution.getDistributionCodeCounter());
            PstMatDistribution.setInt(FLD_DISTRIBUTION_STATUS, matDistribution.getDistributionStatus());
            PstMatDistribution.setString(FLD_REMARK, matDistribution.getRemark());
            PstMatDistribution.setString(FLD_INVOICE_SUPPLIER, matDistribution.getInvoiceSupplier());

            PstMatDistribution.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(PstMatDistribution.getInsertSQL());
            matDistribution.setOID(PstMatDistribution.getlong(FLD_DISTRIBUTION_MATERIAL_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatDistribution(0), DBException.UNKNOWN);
        }
        return matDistribution.getOID();
    }

    public static long updateExc(MatDistribution matDistribution) throws DBException {
        try {
            if (matDistribution.getOID() != 0) {
                PstMatDistribution PstMatDistribution = new PstMatDistribution(matDistribution.getOID());

                PstMatDistribution.setLong(FLD_LOCATION_ID, matDistribution.getLocationId());
                PstMatDistribution.setInt(FLD_LOCATION_TYPE, matDistribution.getLocationType());
                PstMatDistribution.setDate(FLD_DISTRIBUTION_DATE, matDistribution.getDistributionDate());
                PstMatDistribution.setString(FLD_DISTRIBUTION_CODE, matDistribution.getDistributionCode());
                PstMatDistribution.setInt(FLD_DISTRIBUTION_CODE_COUNTER, matDistribution.getDistributionCodeCounter());
                PstMatDistribution.setInt(FLD_DISTRIBUTION_STATUS, matDistribution.getDistributionStatus());
                PstMatDistribution.setString(FLD_REMARK, matDistribution.getRemark());
                PstMatDistribution.setString(FLD_INVOICE_SUPPLIER, matDistribution.getInvoiceSupplier());

                PstMatDistribution.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(PstMatDistribution.getUpdateSQL());
                return matDistribution.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatDistribution(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstMatDistribution PstMatDistribution = new PstMatDistribution(oid);
            //Delete Item First
            PstMatDistributionDetail pstDFItem = new PstMatDistributionDetail();
            long result = pstDFItem.deleteExcByParent(oid);
            PstMatDistribution.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(PstMatDistribution.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatDistribution(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_DISTRIBUTION;
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
                MatDistribution matDistribution = new MatDistribution();
                resultToObject(rs, matDistribution);
                lists.add(matDistribution);
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

    public static void resultToObject(ResultSet rs, MatDistribution matDistribution) {
        try {
            matDistribution.setOID(rs.getLong(PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_MATERIAL_ID]));
            matDistribution.setLocationId(rs.getLong(PstMatDistribution.fieldNames[PstMatDistribution.FLD_LOCATION_ID]));
            matDistribution.setLocationType(rs.getInt(PstMatDistribution.fieldNames[PstMatDistribution.FLD_LOCATION_TYPE]));
            matDistribution.setDistributionDate(rs.getDate(PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_DATE]));
            matDistribution.setDistributionCode(rs.getString(PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_CODE]));
            matDistribution.setDistributionCodeCounter(rs.getInt(PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_CODE_COUNTER]));
            matDistribution.setDistributionStatus(rs.getInt(PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_STATUS]));
            matDistribution.setRemark(rs.getString(PstMatDistribution.fieldNames[PstMatDistribution.FLD_REMARK]));
            matDistribution.setInvoiceSupplier(rs.getString(PstMatDistribution.fieldNames[PstMatDistribution.FLD_INVOICE_SUPPLIER]));
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long matDispatchId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_DISTRIBUTION +
                    " WHERE " + fieldNames[FLD_DISTRIBUTION_MATERIAL_ID] +
                    " = " + matDispatchId;

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
            String sql = "SELECT COUNT(" + PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_MATERIAL_ID] + ") AS CNT FROM " + TBL_DISTRIBUTION;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt("CNT");
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
                    MatDistribution matDistribution = (MatDistribution) list.get(ls);
                    if (oid == matDistribution.getOID())
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

    public static long AutoInsertDf(Vector listItem, long oidLocation, long oidDispatchTo, int locType) {
        long hasil = 0;
        /*SessMatDistribution sessDispatch = new SessMatDistribution();
        try {
            MatDistribution mydf = new MatDistribution();
            mydf.setDispatchStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
            mydf.setDispatchDate(new Date());
            mydf.setLocationId(oidLocation);
            mydf.setDispatchTo(oidDispatchTo);
            mydf.setLocationType(locType);
            mydf.setRemark("");
            mydf.setDispatchCodeCounter(sessDispatch.getMaxDispatchCounter(new Date(),mydf)+1);
            mydf.setDispatchCode(sessDispatch.generateDispatchCode(mydf));
            //Insert dispatch
            hasil = PstMatDistribution.insertExc(mydf);
            if (hasil != 0) {
                //Insert df item
                for (int i = 0; i<listItem.size(); i++) {
                    long oidDfItem = 0;
                    Vector temp = (Vector)listItem.get(i);
                    String sku = (String)temp.get(0);
                    if (sku.length() > 0) {
                        //Fetch material info
                        Material myMat = PstMaterial.fetchBySku(sku);
                        int quantity = Integer.parseInt((String)temp.get(1));
                        MatDistributionItem dfItem = new MatDistributionItem();
                        dfItem.setDispatchMaterialId(hasil);
                        dfItem.setMaterialId(myMat.getOID());
                        dfItem.setQty(quantity);
                        oidDfItem = PstMatDistributionItem.insertExc(dfItem);
                    }
                    else {
                        oidDfItem = 1;
                    }
                    if (oidDfItem == 0) break;
                }
            }
        }
        catch(Exception ex) {
            System.out.println("AutoInsertDf : " + ex);
        }*/
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
            MatDistribution matDispatch = fetchExc(documentId);
            return matDispatch.getDistributionStatus();
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


    /**
     * ini adalah proses pembuatan dispatch yang datanya
     * di dapat dari distribusi.
     */
    public static int generateDispatch(long oidDistribution) {
        int result = 0;
        if (oidDistribution != 0) {
            //System.out.println("===>> PROCESS TRANSFER DISTRIBUTION !!!");
            MatDistribution matDistribution = new MatDistribution();
            try {
                // GET DISTRIBUTION
                try {
                    matDistribution = PstMatDistribution.fetchExc(oidDistribution);
                } catch (Exception e) {
                }

                // PENCARIAN LOKASI UNTUK MEMUDAHKAN DALAM PEMBUATAN DF
                String whereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + "=" + PstLocation.TYPE_LOCATION_STORE;
                Vector vtLocation = PstLocation.list(0, 0, whereClause, "");
                if (vtLocation != null && vtLocation.size() > 0) {
                    for (int k = 0; k < vtLocation.size(); k++) {
                        Location loc = (Location) vtLocation.get(k);
                        whereClause = PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_LOCATION_ID] + "=" + loc.getOID() +
                                " AND " + PstMatDistributionDetail.fieldNames[PstMatDistributionDetail.FLD_DISTRIBUTION_MATERIAL_ID] + "=" + oidDistribution;
                        Vector dstData = PstMatDistributionDetail.list(0, 0, whereClause, "");

                        if (dstData != null && dstData.size() > 0) {
                            MatDispatch matDispatch = new MatDispatch();
                            matDispatch.setDispatchDate(matDistribution.getDistributionDate());
                            matDispatch.setDispatchTo(loc.getOID());
                            matDispatch.setLocationId(matDistribution.getLocationId());
                            matDispatch.setInvoiceSupplier(matDistribution.getInvoiceSupplier());
                            matDispatch.setDispatchStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                            /**
                             * mengapa type store ?
                             * karena data yang di transafer adalaha data yang ada di warehouse ke toko.
                             */
                            matDispatch.setLocationType(PstLocation.TYPE_LOCATION_STORE);
                            matDispatch.setRemark("Transfer from distribusi number " + matDistribution.getDistributionCode().toUpperCase());

                            // GET COUNTER FOR CREATE CODE DISTRIBUTION
                            SessMatDispatch sessDispatch = new SessMatDispatch();
                            int maxCounter = sessDispatch.getMaxDispatchCounter(matDispatch.getDispatchDate(), matDispatch);
                            maxCounter = maxCounter + 1;

                            matDispatch.setDispatchCodeCounter(maxCounter);
                            matDispatch.setDispatchCode(sessDispatch.generateDispatchCode(matDispatch));

                            long oid = 0; // oid dispatch after insert
                            try {
                                oid = PstMatDispatch.insertExc(matDispatch);
                            } catch (Exception e) {
                                result = 1;
                            }

                            // CEK OID, IF OID NOT 0, THEN INSERT ITEM FOR DF
                            if (oid != 0) {
                                for (int i = 0; i < dstData.size(); i++) {
                                    MatDistributionDetail matDistributionDetai = (MatDistributionDetail) dstData.get(i);
                                    // to find unit id
                                    Material mat = new Material();
                                    mat = PstMaterial.fetchExc(matDistributionDetai.getMaterialId());

                                    MatDispatchItem matDispatchItem = new MatDispatchItem();
                                    matDispatchItem.setDispatchMaterialId(oid);
                                    matDispatchItem.setMaterialId(matDistributionDetai.getMaterialId());
                                    matDispatchItem.setQty(matDistributionDetai.getQty());
                                    matDispatchItem.setResidueQty(matDistributionDetai.getQty());
                                    matDispatchItem.setUnitId(mat.getDefaultStockUnitId());

                                    try {
                                        //System.out.println("===>> INSERT ITM DF");
                                        PstMatDispatchItem.insertExc(matDispatchItem);
                                    } catch (Exception exc) {
                                        result = 1;
                                    }

                                    // update DF ID
                                    try {
                                        //System.out.println("===>> UPDATE ITM DIS");
                                        matDistributionDetai.setDispatchMaterialId(oid);
                                        PstMatDistributionDetail.updateExc(matDistributionDetai);
                                    } catch (Exception exc) {
                                        result = 1;
                                    }
                                }
                            }
                        }
                    }

                    matDistribution.setDistributionStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
                    PstMatDistribution.updateExc(matDistribution);

                    //System.out.println("===>> PROCESS TRANSFER DISTRIBUTION END !!!");
                }
            } catch (Exception e) {
                System.out.println("===>> ERR PROCESS TRANSFER " + e.toString());
                result = 1;
            }
        }

        return result;
    }


   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MatDistribution matDistribution = PstMatDistribution.fetchExc(oid);
         object.put(PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_MATERIAL_ID], matDistribution.getOID());
         object.put(PstMatDistribution.fieldNames[PstMatDistribution.FLD_LOCATION_ID], matDistribution.getLocationId());
         object.put(PstMatDistribution.fieldNames[PstMatDistribution.FLD_LOCATION_TYPE], matDistribution.getLocationType());
         object.put(PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_DATE], matDistribution.getDistributionDate());
         object.put(PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_CODE], matDistribution.getDistributionCode());
         object.put(PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_CODE_COUNTER], matDistribution.getDistributionCodeCounter());
         object.put(PstMatDistribution.fieldNames[PstMatDistribution.FLD_DISTRIBUTION_STATUS], matDistribution.getDistributionStatus());
         object.put(PstMatDistribution.fieldNames[PstMatDistribution.FLD_REMARK], matDistribution.getRemark());
         object.put(PstMatDistribution.fieldNames[PstMatDistribution.FLD_INVOICE_SUPPLIER], matDistribution.getInvoiceSupplier());
      }catch(Exception exc){}
      return object;
   }
}
