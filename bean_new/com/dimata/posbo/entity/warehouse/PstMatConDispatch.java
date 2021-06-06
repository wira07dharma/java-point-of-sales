package com.dimata.posbo.entity.warehouse;

/* package java */

import com.dimata.common.entity.custom.PstDataCustom;
import org.json.JSONObject;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.qdep.form.FRMHandler;

/* package garment */
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.session.warehouse.*;

public class PstMatConDispatch extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_Document {

    public static final String TBL_DISPATCH = "pos_dispatch_con_material";

    public static final int FLD_DISPATCH_MATERIAL_ID = 0;
    public static final int FLD_LOCATION_ID = 1;
    public static final int FLD_DISPATCH_TO = 2;
    public static final int FLD_LOCATION_TYPE = 3;
    public static final int FLD_DISPATCH_DATE = 4;
    public static final int FLD_DISPATCH_CODE = 5;
    public static final int FLD_DISPATCH_CODE_COUNTER = 6;
    public static final int FLD_DISPATCH_STATUS = 7;
    public static final int FLD_REMARK = 8;
    public static final int FLD_INVOICE_SUPPLIER = 9;
    public static final int FLD_TRANSFER_STATUS = 10;
    public static final int FLD_LAST_UPDATE = 11;

    public static final String[] fieldNames = {
        "DISPATCH_CON_MATERIAL_ID",
        "LOCATION_ID",
        "DISPATCH_TO",
        "LOCATION_TYPE",
        "DISPATCH_DATE",
        "DISPATCH_CODE",
        "DISPATCH_CODE_COUNTER",
        "DISPATCH_STATUS",
        "REMARK",
        "INVOICE_SUPPLIER",
        "TRANSFER_STATUS",
        "LAST_UPDATE"
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
        TYPE_DATE
    };

    public static final int FLD_TYPE_DISPATCH_LOCATION_STORE = 0;
    public static final int FLD_TYPE_DISPATCH_LOCATION_WAREHOUSE = 1;

    public static final String[] fieldTypeDispatch = {
        "Store",
        "Warehouse"
    };

    public PstMatConDispatch() {
    }

    public PstMatConDispatch(int i) throws DBException {
        super(new PstMatConDispatch());
    }

    public PstMatConDispatch(String sOid) throws DBException {
        super(new PstMatConDispatch(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstMatConDispatch(long lOid) throws DBException {
        super(new PstMatConDispatch(0));
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
        return TBL_DISPATCH;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMatConDispatch().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        MatConDispatch matdispatch = fetchExc(ent.getOID());
        ent = (Entity) matdispatch;
        return matdispatch.getOID();
    }

    synchronized public long insertExc(Entity ent) throws Exception {
        return insertExc((MatConDispatch) ent);
    }

    synchronized public long updateExc(Entity ent) throws Exception {
        return updateExc((MatConDispatch) ent);
    }

    synchronized public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static MatConDispatch fetchExc(long oid) throws DBException {
        try {
            MatConDispatch matdispatch = new MatConDispatch();
            PstMatConDispatch pstMatConDispatch = new PstMatConDispatch(oid);
            matdispatch.setOID(oid);

            matdispatch.setLocationId(pstMatConDispatch.getlong(FLD_LOCATION_ID));
            matdispatch.setDispatchTo(pstMatConDispatch.getlong(FLD_DISPATCH_TO));
            matdispatch.setLocationType(pstMatConDispatch.getInt(FLD_LOCATION_TYPE));
            matdispatch.setDispatchDate(pstMatConDispatch.getDate(FLD_DISPATCH_DATE));
            matdispatch.setDispatchCode(pstMatConDispatch.getString(FLD_DISPATCH_CODE));
            matdispatch.setDispatchCodeCounter(pstMatConDispatch.getInt(FLD_DISPATCH_CODE_COUNTER));
            matdispatch.setDispatchStatus(pstMatConDispatch.getInt(FLD_DISPATCH_STATUS));
            matdispatch.setRemark(pstMatConDispatch.getString(FLD_REMARK));
            matdispatch.setInvoiceSupplier(pstMatConDispatch.getString(FLD_INVOICE_SUPPLIER));
            matdispatch.setTransferStatus(pstMatConDispatch.getInt(FLD_TRANSFER_STATUS));
            matdispatch.setLast_update(pstMatConDispatch.getDate(FLD_LAST_UPDATE));

            return matdispatch;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatConDispatch(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(MatConDispatch matdispatch) throws DBException {
        try {
            PstMatConDispatch pstMatConDispatch = new PstMatConDispatch(0);

            pstMatConDispatch.setLong(FLD_LOCATION_ID, matdispatch.getLocationId());
            pstMatConDispatch.setLong(FLD_DISPATCH_TO, matdispatch.getDispatchTo());
            pstMatConDispatch.setInt(FLD_LOCATION_TYPE, matdispatch.getLocationType());
            pstMatConDispatch.setDate(FLD_DISPATCH_DATE, matdispatch.getDispatchDate());
            pstMatConDispatch.setString(FLD_DISPATCH_CODE, matdispatch.getDispatchCode());
            pstMatConDispatch.setInt(FLD_DISPATCH_CODE_COUNTER, matdispatch.getDispatchCodeCounter());
            pstMatConDispatch.setInt(FLD_DISPATCH_STATUS, matdispatch.getDispatchStatus());
            pstMatConDispatch.setString(FLD_REMARK, matdispatch.getRemark());
            pstMatConDispatch.setString(FLD_INVOICE_SUPPLIER, matdispatch.getInvoiceSupplier());
            pstMatConDispatch.setInt(FLD_TRANSFER_STATUS, matdispatch.getTransferStatus());
            pstMatConDispatch.setDate(FLD_LAST_UPDATE, matdispatch.getLast_update());

            pstMatConDispatch.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatConDispatch.getInsertSQL());
            matdispatch.setOID(pstMatConDispatch.getlong(FLD_DISPATCH_MATERIAL_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatConDispatch(0), DBException.UNKNOWN);
        }
        return matdispatch.getOID();
    }

    public static long updateExc(MatConDispatch matdispatch) throws DBException {
        try {
            if (matdispatch.getOID() != 0) {
                PstMatConDispatch pstMatConDispatch = new PstMatConDispatch(matdispatch.getOID());

                pstMatConDispatch.setLong(FLD_LOCATION_ID, matdispatch.getLocationId());
                pstMatConDispatch.setLong(FLD_DISPATCH_TO, matdispatch.getDispatchTo());
                pstMatConDispatch.setInt(FLD_LOCATION_TYPE, matdispatch.getLocationType());
                pstMatConDispatch.setDate(FLD_DISPATCH_DATE, matdispatch.getDispatchDate());
                pstMatConDispatch.setString(FLD_DISPATCH_CODE, matdispatch.getDispatchCode());
                pstMatConDispatch.setInt(FLD_DISPATCH_CODE_COUNTER, matdispatch.getDispatchCodeCounter());
                pstMatConDispatch.setInt(FLD_DISPATCH_STATUS, matdispatch.getDispatchStatus());
                pstMatConDispatch.setString(FLD_REMARK, matdispatch.getRemark());
                pstMatConDispatch.setString(FLD_INVOICE_SUPPLIER, matdispatch.getInvoiceSupplier());
                pstMatConDispatch.setInt(FLD_TRANSFER_STATUS, matdispatch.getTransferStatus());
                pstMatConDispatch.setDate(FLD_LAST_UPDATE, matdispatch.getLast_update());

                pstMatConDispatch.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMatConDispatch.getUpdateSQL());
                return matdispatch.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatConDispatch(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstMatConDispatch pstMatConDispatch = new PstMatConDispatch(oid);
            //Delete Item First
            PstMatConDispatchItem pstDFItem = new PstMatConDispatchItem();
            long result = pstDFItem.deleteExcByParent(oid);
            pstMatConDispatch.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatConDispatch.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatConDispatch(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_DISPATCH;
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

            //System.out.println("PstMatConDispatch.list() sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MatConDispatch matdispatch = new MatConDispatch();
                resultToObject(rs, matdispatch);
                lists.add(matdispatch);
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

    public static void resultToObject(ResultSet rs, MatConDispatch matdispatch) {
        try {
            matdispatch.setOID(rs.getLong(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_MATERIAL_ID]));
            matdispatch.setLocationId(rs.getLong(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_LOCATION_ID]));
            matdispatch.setDispatchTo(rs.getLong(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_TO]));
            matdispatch.setLocationType(rs.getInt(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_LOCATION_TYPE]));
            Date date = DBHandler.convertDate(rs.getDate(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_DATE]), rs.getTime(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_DATE]));
            matdispatch.setDispatchDate(date); //rs.getDate(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_DATE]));
            matdispatch.setDispatchCode(rs.getString(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE]));
            matdispatch.setDispatchCodeCounter(rs.getInt(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE_COUNTER]));
            matdispatch.setDispatchStatus(rs.getInt(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_STATUS]));
            matdispatch.setRemark(rs.getString(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_REMARK]));
            matdispatch.setInvoiceSupplier(rs.getString(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_INVOICE_SUPPLIER]));
            matdispatch.setTransferStatus(rs.getInt(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_TRANSFER_STATUS]));

            Date dates = DBHandler.convertDate(rs.getDate(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_LAST_UPDATE]), rs.getTime(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_LAST_UPDATE]));
            matdispatch.setLast_update(dates);

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long matDispatchId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_DISPATCH +
                    " WHERE " + fieldNames[FLD_DISPATCH_MATERIAL_ID] +
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
            String sql = "SELECT COUNT(" + PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_MATERIAL_ID] + ") FROM " + TBL_DISPATCH;
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
                    MatConDispatch matdispatch = (MatConDispatch) list.get(ls);
                    if (oid == matdispatch.getOID())
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
        SessMatConDispatch sessDispatch = new SessMatConDispatch();        
        try {
            MatConDispatch mydf = new MatConDispatch();
            mydf.setDispatchStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
            mydf.setDispatchDate(new Date());
            mydf.setLocationId(oidLocation);
            mydf.setDispatchTo(oidDispatchTo);
            mydf.setLocationType(locType);
            mydf.setRemark("");
            mydf.setDispatchCodeCounter(sessDispatch.getMaxDispatchCounter(new Date(), mydf) + 1);
            mydf.setDispatchCode(sessDispatch.generateDispatchCode(mydf));
            //Insert dispatch
            hasil = PstMatConDispatch.insertExc(mydf);
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
                        MatConDispatchItem dfItem = new MatConDispatchItem();
                        dfItem.setDispatchMaterialId(hasil);
                        dfItem.setMaterialId(myMat.getOID());
                        dfItem.setUnitId(myMat.getDefaultStockUnitId()); 			
                        dfItem.setQty(quantity);
                        oidDfItem = PstMatConDispatchItem.insertExc(dfItem);
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
            MatConDispatch matDispatch = fetchExc(documentId);
            return matDispatch.getDispatchStatus();
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
            String sql = "SELECT SUM(" + PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_RESIDUE_QTY] + ") " +
                    " AS SUM_" + PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_RESIDUE_QTY] +
                    " FROM " + PstMatConDispatchItem.TBL_MAT_DISPATCH_ITEM +
                    " WHERE " + PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_DISPATCH_MATERIAL_ID] + " = " + oid;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int sumqty = 0;
            while (rs.next()) {
                sumqty = rs.getInt("SUM_" + PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_RESIDUE_QTY]);
            }

            MatConDispatch matDispatch = new MatConDispatch();
            matDispatch = PstMatConDispatch.fetchExc(oid);
            if (sumqty == 0) {
                matDispatch.setTransferStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
            } else {
                matDispatch.setTransferStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
            }
            // update transfer status
            PstMatConDispatch.updateExc(matDispatch);

        } catch (Exception e) {
        }
    }
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MatConDispatch matConDispatch = PstMatConDispatch.fetchExc(oid);
         object.put(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_MATERIAL_ID], matConDispatch.getOID());
         object.put(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_LOCATION_ID], matConDispatch.getLocationId());
         object.put(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_TO], matConDispatch.getDispatchTo());
         object.put(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_LOCATION_TYPE], matConDispatch.getLocationType());
         object.put(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_DATE], matConDispatch.getDispatchDate());
         object.put(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE], matConDispatch.getDispatchCode());
         object.put(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_CODE_COUNTER], matConDispatch.getDispatchCodeCounter());
         object.put(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_STATUS], matConDispatch.getDispatchStatus());
         object.put(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_REMARK], matConDispatch.getRemark());
         object.put(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_INVOICE_SUPPLIER], matConDispatch.getInvoiceSupplier());
         object.put(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_TRANSFER_STATUS], matConDispatch.getTransferStatus());
         object.put(PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_LAST_UPDATE], matConDispatch.getLast_update());
      }catch(Exception exc){}
      return object;
   }
}
