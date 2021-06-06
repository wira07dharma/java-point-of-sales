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
import org.json.JSONObject;

public class PstMatConReturn extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_Document {
    
    public static final  String TBL_MAT_RETURN = "pos_return_con_material";
    
    public static final  int FLD_RETURN_MATERIAL_ID     = 0;
    public static final  int FLD_LOCATION_ID            = 1;
    public static final  int FLD_LOCATION_TYPE          = 2;
    public static final  int FLD_RETURN_TO              = 3;
    public static final  int FLD_RETURN_DATE            = 4;
    public static final  int FLD_RET_CODE               = 5;
    public static final  int FLD_RET_CODE_CNT           = 6;
    public static final  int FLD_RETURN_STATUS          = 7;
    public static final  int FLD_RETURN_SOURCE          = 8;
    public static final  int FLD_SUPPLIER_ID            = 9;
    public static final  int FLD_PURCHASE_ORDER_ID      = 10;
    public static final  int FLD_RECEIVE_MATERIAL_ID    = 11;
    public static final  int FLD_REMARK                 = 12;
    public static final  int FLD_RETURN_REASON          = 13;
    public static final  int FLD_TRANSFER_STATUS        = 14;
    public static final  int FLD_INVOICE_SUPPLIER       = 15;
    public static final  int FLD_LAST_UPDATE            = 16;

    public static final  String[] fieldNames = {
        "RETURN_CON_MATERIAL_ID",
        "LOCATION_ID",
        "LOCATION_TYPE",
        "RETURN_TO",
        "RETURN_DATE",
        "RETURN_CODE",
        "RETURN_CODE_COUNTER",
        "RETURN_STATUS",
        "RETURN_SOURCE",
        "SUPPLIER_ID",
        "PURCHASE_ORDER_ID",
        "RECEIVE_MATERIAL_ID",
        "REMARK",
        "RETURN_REASON",
        "TRANSFER_STATUS",
        "INVOICE_SUPPLIER",
        "LAST_UPDATE"
    };
    
    public static final  int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_INT,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_DATE
    };
    
    
    //Return Source
    public static final int SOURCE_FROM_RECEIVE = 0;
    public static final int SOURCE_FROM_PO      = 1;
    public static final int SOURCE_FROM_ELSE    = 2;
    
    public static String[][] strReturnSourceList = {
        {"Penerimaan Material","Order Pembelian","Lainnya"},
        {"Material Receiving","Purchase Order","Else"}
    };
    
    public static Vector getReturnSourceType(int language) {
        Vector result = new Vector(1,1);
        for(int i=0; i<strReturnSourceList[language].length; i++) {
            result.add(strReturnSourceList[language][i]);
        }
        return result;
    }
    
    //Return Reason
    public static final int RETURN_FOR_BROKEN   = 0;
    public static final int RETURN_FOR_EXPIRED  = 1;
    public static final int RETURN_FOR_MUTATION = 2;
    public static final int RETURN_FOR_ELSE     = 3;
    
    public static String[][] strReturnReasonList = {
        {"Rusak","Kadaluarsa","Mutasi","Lainnya"},
        {"Broken","Expired","Mutation","Else"}
    };
    
    public static Vector getReturnReasonType(int language) {
        Vector result = new Vector(1,1);
        for(int i=0; i<strReturnReasonList[language].length; i++) {
            result.add(strReturnReasonList[language][i]);
        }
        return result;
    }
    
    public PstMatConReturn() {
    }
    
    public PstMatConReturn(int i) throws DBException {
        super(new PstMatConReturn());
    }
    
    public PstMatConReturn(String sOid) throws DBException {
        super(new PstMatConReturn(0));
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstMatConReturn(long lOid) throws DBException {
        super(new PstMatConReturn(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        }
        catch(Exception e) {
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }
    
    public String getTableName() {
        return TBL_MAT_RETURN;
    }
    
    public String[] getFieldNames(){
        return fieldNames;
    }
    
    public int[] getFieldTypes(){
        return fieldTypes;
    }
    
    public String getPersistentName(){
        return new PstMatConReturn().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception{
        MatConReturn matreturn = fetchExc(ent.getOID());
        ent = (Entity)matreturn;
        return matreturn.getOID();
    }
    
    synchronized public long insertExc(Entity ent) throws Exception{
        return insertExc((MatConReturn) ent);
    }
    
    synchronized public long updateExc(Entity ent) throws Exception{
        return updateExc((MatConReturn) ent);
    }
    
    synchronized public long deleteExc(Entity ent) throws Exception{
        if(ent==null){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public static MatConReturn fetchExc(long oid) throws DBException {
        try {
            MatConReturn matreturn = new MatConReturn();
            PstMatConReturn pstMatConReturn = new PstMatConReturn(oid);
            matreturn.setOID(oid);
            
            matreturn.setLocationId(pstMatConReturn.getlong(FLD_LOCATION_ID));
            matreturn.setLocationType(pstMatConReturn.getInt(FLD_LOCATION_TYPE));
            matreturn.setReturnTo(pstMatConReturn.getlong(FLD_RETURN_TO));
            matreturn.setReturnDate(pstMatConReturn.getDate(FLD_RETURN_DATE));
            matreturn.setRetCode(pstMatConReturn.getString(FLD_RET_CODE));
            matreturn.setRetCodeCnt(pstMatConReturn.getInt(FLD_RET_CODE_CNT));
            matreturn.setReturnStatus(pstMatConReturn.getInt(FLD_RETURN_STATUS));
            matreturn.setReturnSource(pstMatConReturn.getInt(FLD_RETURN_SOURCE));
            matreturn.setSupplierId(pstMatConReturn.getlong(FLD_SUPPLIER_ID));
            matreturn.setPurchaseOrderId(pstMatConReturn.getlong(FLD_PURCHASE_ORDER_ID));
            matreturn.setReceiveMaterialId(pstMatConReturn.getlong(FLD_RECEIVE_MATERIAL_ID));
            matreturn.setRemark(pstMatConReturn.getString(FLD_REMARK));
            matreturn.setReturnReason(pstMatConReturn.getInt(FLD_RETURN_REASON));
            matreturn.setTransferStatus(pstMatConReturn.getInt(FLD_TRANSFER_STATUS));
            matreturn.setInvoiceSupplier(pstMatConReturn.getString(FLD_INVOICE_SUPPLIER));
            matreturn.setLastUpdate(pstMatConReturn.getDate(FLD_LAST_UPDATE));

            return matreturn;
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatConReturn(0),DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(MatConReturn matreturn) throws DBException {
        try {
            PstMatConReturn pstMatConReturn = new PstMatConReturn(0);
            
            pstMatConReturn.setLong(FLD_LOCATION_ID, matreturn.getLocationId());
            pstMatConReturn.setInt(FLD_LOCATION_TYPE, matreturn.getLocationType());
            pstMatConReturn.setLong(FLD_RETURN_TO, matreturn.getReturnTo());
            pstMatConReturn.setDate(FLD_RETURN_DATE, matreturn.getReturnDate());
            pstMatConReturn.setString(FLD_RET_CODE, matreturn.getRetCode());
            pstMatConReturn.setInt(FLD_RET_CODE_CNT, matreturn.getRetCodeCnt());
            pstMatConReturn.setInt(FLD_RETURN_STATUS, matreturn.getReturnStatus());
            pstMatConReturn.setInt(FLD_RETURN_SOURCE, matreturn.getReturnSource());
            pstMatConReturn.setLong(FLD_SUPPLIER_ID, matreturn.getSupplierId());
            pstMatConReturn.setLong(FLD_PURCHASE_ORDER_ID, matreturn.getPurchaseOrderId());
            pstMatConReturn.setLong(FLD_RECEIVE_MATERIAL_ID, matreturn.getReceiveMaterialId());
            pstMatConReturn.setString(FLD_REMARK, matreturn.getRemark());
            pstMatConReturn.setInt(FLD_RETURN_REASON, matreturn.getReturnReason());
            pstMatConReturn.setInt(FLD_TRANSFER_STATUS, matreturn.getTransferStatus());
            pstMatConReturn.setString(FLD_INVOICE_SUPPLIER, matreturn.getInvoiceSupplier());
            pstMatConReturn.setDate(FLD_LAST_UPDATE, matreturn.getLastUpdate());

            pstMatConReturn.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatConReturn.getInsertSQL());
            matreturn.setOID(pstMatConReturn.getlong(FLD_RETURN_MATERIAL_ID));
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatConReturn(0),DBException.UNKNOWN);
        }
        return matreturn.getOID();
    }
    
    public static long updateExc(MatConReturn matreturn) throws DBException {
        try {
            if(matreturn.getOID() != 0) {
                PstMatConReturn pstMatConReturn = new PstMatConReturn(matreturn.getOID());
                
                pstMatConReturn.setLong(FLD_LOCATION_ID, matreturn.getLocationId());
                pstMatConReturn.setInt(FLD_LOCATION_TYPE, matreturn.getLocationType());
                pstMatConReturn.setLong(FLD_RETURN_TO, matreturn.getReturnTo());
                pstMatConReturn.setDate(FLD_RETURN_DATE, matreturn.getReturnDate());
                pstMatConReturn.setString(FLD_RET_CODE, matreturn.getRetCode());
                pstMatConReturn.setInt(FLD_RET_CODE_CNT, matreturn.getRetCodeCnt());
                pstMatConReturn.setInt(FLD_RETURN_STATUS, matreturn.getReturnStatus());
                pstMatConReturn.setInt(FLD_RETURN_SOURCE, matreturn.getReturnSource());
                pstMatConReturn.setLong(FLD_SUPPLIER_ID, matreturn.getSupplierId());
                pstMatConReturn.setLong(FLD_PURCHASE_ORDER_ID, matreturn.getPurchaseOrderId());
                pstMatConReturn.setLong(FLD_RECEIVE_MATERIAL_ID, matreturn.getReceiveMaterialId());
                pstMatConReturn.setString(FLD_REMARK, matreturn.getRemark());
                pstMatConReturn.setInt(FLD_RETURN_REASON, matreturn.getReturnReason());
                pstMatConReturn.setInt(FLD_TRANSFER_STATUS, matreturn.getTransferStatus());
                pstMatConReturn.setString(FLD_INVOICE_SUPPLIER, matreturn.getInvoiceSupplier());
                pstMatConReturn.setDate(FLD_LAST_UPDATE, matreturn.getLastUpdate());

                pstMatConReturn.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMatConReturn.getUpdateSQL());
                return matreturn.getOID();
                
            }
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatConReturn(0),DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long oid) throws DBException {
        try {
            PstMatConReturn pstMatConReturn = new PstMatConReturn(oid);
            //Delete Item First
            PstMatConReturnItem pstRETItem = new PstMatConReturnItem();
            long result = pstRETItem.deleteExcByParent(oid);
            pstMatConReturn.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatConReturn.getDeleteSQL());
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatConReturn(0),DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static Vector list(int limitStart,int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_RETURN;
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if(order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL :
                    if(limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
                    break;
                    
                case DBHandler.DBSVR_POSTGRESQL :
                    if(limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " +recordToGet + " OFFSET "+ limitStart ;
                    break;
                    
                case DBHandler.DBSVR_SYBASE :
                    break;
                    
                case DBHandler.DBSVR_ORACLE :
                    break;
                    
                case DBHandler.DBSVR_MSSQL :
                    break;
                    
                default:
                    ;
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                MatConReturn matreturn = new MatConReturn();
                resultToObject(rs, matreturn);
                lists.add(matreturn);
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }
    
    public static void resultToObject(ResultSet rs, MatConReturn matreturn) {
        try {
            matreturn.setOID(rs.getLong(fieldNames[FLD_RETURN_MATERIAL_ID]));
            matreturn.setLocationId(rs.getLong(fieldNames[FLD_LOCATION_ID]));
            matreturn.setLocationType(rs.getInt(fieldNames[FLD_LOCATION_TYPE]));
            matreturn.setReturnTo(rs.getLong(fieldNames[FLD_RETURN_TO]));
            matreturn.setReturnDate(rs.getDate(PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE]));
            matreturn.setRetCode(rs.getString(fieldNames[FLD_RET_CODE]));
            matreturn.setRetCodeCnt(rs.getInt(fieldNames[FLD_RET_CODE_CNT]));
            matreturn.setReturnStatus(rs.getInt(fieldNames[FLD_RETURN_STATUS]));
            matreturn.setSupplierId(rs.getLong(fieldNames[FLD_SUPPLIER_ID]));
            matreturn.setPurchaseOrderId(rs.getLong(fieldNames[FLD_PURCHASE_ORDER_ID]));
            matreturn.setReceiveMaterialId(rs.getLong(fieldNames[FLD_RECEIVE_MATERIAL_ID]));
            matreturn.setRemark(rs.getString(fieldNames[FLD_REMARK]));
            matreturn.setReturnReason(rs.getInt(fieldNames[FLD_RETURN_REASON]));
            matreturn.setTransferStatus(rs.getInt(fieldNames[FLD_TRANSFER_STATUS]));
            matreturn.setInvoiceSupplier(rs.getString(fieldNames[FLD_INVOICE_SUPPLIER]));
            matreturn.setLastUpdate(rs.getDate(fieldNames[FLD_LAST_UPDATE]));
        }
        catch(Exception e)
        { }
    }
    
    public static boolean checkOID(long matReturnId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_RETURN +
            " WHERE " + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_MATERIAL_ID] +
            " = " + matReturnId;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                result = true;
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println("err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT("+ PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_MATERIAL_ID] + ") FROM " + TBL_MAT_RETURN;
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }
    
    
    /* This method used to find current data */
    public static int findLimitStart( long oid, int recordToGet, String whereClause, String orderClause){
        int size = getCount(whereClause);
        int start = 0;
        boolean found =false;
        for(int i=0; (i < size) && !found ; i=i+recordToGet){
            Vector list =  list(i,recordToGet, whereClause, orderClause);
            start = i;
            if(list.size()>0){
                for(int ls=0;ls<list.size();ls++){
                    MatConReturn matreturn = (MatConReturn)list.get(ls);
                    if(oid == matreturn.getOID())
                        found=true;
                }
            }
        }
        if((start >= size) && (size > 0))
            start = start - recordToGet;
        
        return start;
    }
    /* This method used to find command where current data */
    public static int findLimitCommand(int start, int recordToGet, int vectSize){
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + (recordToGet - mdl);
        if(start == 0)
            cmd =  Command.FIRST;
        else{
            if(start == (vectSize-recordToGet))
                cmd = Command.LAST;
            else{
                start = start + recordToGet;
                if(start <= (vectSize - recordToGet)){
                    cmd = Command.NEXT;
                    //System.out.println("next.......................");
                }else{
                    start = start - recordToGet;
                    if(start > 0){
                        cmd = Command.PREV;
                        //System.out.println("prev.......................");
                    }
                }
            }
        }
        
        return cmd;
    }
    
    /*-------------------- start implements I_Document -------------------------*/
    /**
     * this method used to get status of 'document'
     * return int of currentDocumentStatus
     */
    public int getDocumentStatus(long documentId) {
        DBResultSet dbrs = null;
        try {
            MatConReturn matReturn = fetchExc(documentId);
            return matReturn.getReturnStatus();
        }
        catch(Exception e) {
            System.out.println("Err : "+e.toString());
        }
        finally {
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
            String sql = "UPDATE " + TBL_MAT_RETURN +
                " SET " + fieldNames[FLD_RETURN_STATUS] +
                " = " + indexStatus +
                " WHERE " + fieldNames[FLD_RETURN_MATERIAL_ID] +
                " = " + documentId;
            dbrs = DBHandler.execQueryResult(sql);*/
        return indexStatus ;
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
    
    public static long AutoInsertReturn(Vector listItem, long oidLocation, long oidSupplier,
    int locType, long returnTo, int returnSource) {
        long hasil = 0;
        try {
            MatConReturn myret = new MatConReturn();
            myret.setLocationId(oidLocation);
            myret.setLocationType(locType);
            myret.setReturnTo(returnTo);
            myret.setSupplierId(oidSupplier);
            myret.setReturnDate(new Date());
            myret.setReturnStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
            myret.setReturnSource(returnSource);
            myret.setRemark("");
            myret.setRetCodeCnt(SessMatConReturn.getIntCode(myret,new Date(),0,1));
            myret.setRetCode(SessMatConReturn.getCodeReturn(myret));
            //Insert return
            hasil = PstMatConReturn.insertExc(myret);
            if (hasil != 0) {
                //Insert return item
                for (int i = 0; i<listItem.size(); i++) {
                    long oidRETItem = 0;
                    Vector temp = (Vector)listItem.get(i);
                    String sku = (String)temp.get(0);
                    if (sku.length() > 0) {
                        //Fetch material info
                        Material myMat = PstMaterial.fetchBySku(sku);
                        boolean masuk = false;
                        //Cek supplier utk material tsb (toSupplier), jika tidak sama lewati saja
                        if (myMat.getSupplierId() == oidSupplier) masuk = true;
                        //Cek apakah return to Warehouse
                        if ((oidSupplier == 0) && (returnTo != 0)) masuk = true;
                        if (masuk == true) {
                            int quantity = Integer.parseInt((String)temp.get(1));
                            MatConReturnItem retItem = new MatConReturnItem();
                            retItem.setReturnMaterialId(hasil);
                            retItem.setMaterialId(myMat.getOID());
                            retItem.setUnitId(myMat.getDefaultStockUnitId());
                            retItem.setQty(quantity);
                            retItem.setCost(myMat.getDefaultCost());
                            retItem.setCurrencyId(myMat.getDefaultCostCurrencyId());
                            retItem.setTotal(quantity * myMat.getDefaultCost());
                            oidRETItem = PstMatConReturnItem.insertExc(retItem);
                        }
                        else {
                            oidRETItem = 1;
                        }
                    }
                    else {
                        oidRETItem = 1;
                    }
                    if (oidRETItem == 0) break;
                }
            }
        }
        catch(Exception ex) {
            System.out.println("AutoInsertReturn : " + ex);
        }
        return hasil;
    }
    
    
    /** digunakan untuk update status transfer data secara otomatis.
     *  jika residue qty item semuanya 0 maka status transfer CLOSED.
     *  jika residue qty masih ada yang lebih dari 0 maka status transfer DRAFT.
     * @param oid
     */    
    public static void processUpdate(long oid){
        DBResultSet dbrs = null;
        try{
            String sql = "SELECT SUM("+PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_RESIDUE_QTY]+") "+
                " AS SUM_"+PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_RESIDUE_QTY]+
                " FROM "+PstMatConReturnItem.TBL_MAT_RETURN_ITEM+
                " WHERE "+PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_RETURN_MATERIAL_ID]+" = "+oid;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int sumqty = 0;
            while(rs.next()){
                sumqty = rs.getInt("SUM_"+PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_RESIDUE_QTY]);
            }
            
            MatConReturn matReturn = new MatConReturn();
            matReturn = PstMatConReturn.fetchExc(oid);
            if(sumqty==0){
                matReturn.setTransferStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
            }else{
                matReturn.setTransferStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
            }
            // update transfer status
            PstMatConReturn.updateExc(matReturn);
            
        }catch(Exception e){}
    }


   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MatConReturn matConReturn = PstMatConReturn.fetchExc(oid);
         object.put(PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_MATERIAL_ID], matConReturn.getOID());
         object.put(PstMatConReturn.fieldNames[PstMatConReturn.FLD_LOCATION_ID], matConReturn.getLocationId());
         object.put(PstMatConReturn.fieldNames[PstMatConReturn.FLD_LOCATION_TYPE], matConReturn.getLocationType());
         object.put(PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_TO], matConReturn.getReturnTo());
         object.put(PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_DATE], matConReturn.getReturnDate());
         object.put(PstMatConReturn.fieldNames[PstMatConReturn.FLD_RET_CODE], matConReturn.getRetCode());
         object.put(PstMatConReturn.fieldNames[PstMatConReturn.FLD_RET_CODE_CNT], matConReturn.getRetCodeCnt());
         object.put(PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_STATUS], matConReturn.getReturnStatus());
         object.put(PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_SOURCE], matConReturn.getReturnSource());
         object.put(PstMatConReturn.fieldNames[PstMatConReturn.FLD_SUPPLIER_ID], matConReturn.getSupplierId());
         object.put(PstMatConReturn.fieldNames[PstMatConReturn.FLD_PURCHASE_ORDER_ID], matConReturn.getPurchaseOrderId());
         object.put(PstMatConReturn.fieldNames[PstMatConReturn.FLD_RECEIVE_MATERIAL_ID], matConReturn.getReceiveMaterialId());
         object.put(PstMatConReturn.fieldNames[PstMatConReturn.FLD_REMARK], matConReturn.getRemark());
         object.put(PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_REASON], matConReturn.getReturnReason());
         object.put(PstMatConReturn.fieldNames[PstMatConReturn.FLD_TRANSFER_STATUS], matConReturn.getTransferStatus());
         object.put(PstMatConReturn.fieldNames[PstMatConReturn.FLD_INVOICE_SUPPLIER], matConReturn.getInvoiceSupplier());
         object.put(PstMatConReturn.fieldNames[PstMatConReturn.FLD_LAST_UPDATE], matConReturn.getLastUpdate());
      }catch(Exception exc){}
      return object;
   } 
}
