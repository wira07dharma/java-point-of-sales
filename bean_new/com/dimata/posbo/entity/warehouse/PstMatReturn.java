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

public class PstMatReturn extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_Document {
    
    public static final  String TBL_MAT_RETURN = "pos_return_material";
    
    public static final int FLD_RETURN_MATERIAL_ID  = 0;
    public static final int FLD_LOCATION_ID         = 1;
    public static final int FLD_LOCATION_TYPE       = 2;
    public static final int FLD_RETURN_TO           = 3;
    public static final int FLD_RETURN_DATE         = 4;
    public static final int FLD_RET_CODE            = 5;
    public static final int FLD_RET_CODE_CNT        = 6;
    public static final int FLD_RETURN_STATUS       = 7;
    public static final int FLD_RETURN_SOURCE       = 8;
    public static final int FLD_SUPPLIER_ID         = 9;
    public static final int FLD_PURCHASE_ORDER_ID   = 10;
    public static final int FLD_RECEIVE_MATERIAL_ID = 11;
    public static final int FLD_REMARK              = 12;
    public static final int FLD_RETURN_REASON       = 13;
    public static final int FLD_TRANSFER_STATUS     = 14;
    public static final int FLD_INVOICE_SUPPLIER    = 15;
    public static final int FLD_LAST_UPDATE         = 16;
    public static final int FLD_CURRENCY_ID         = 17;
    public static final int FLD_TRANS_RATE          = 18;
    
    public static final  String[] fieldNames = {
        "RETURN_MATERIAL_ID",
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
        "LAST_UPDATE",
        "CURRENCY_ID",
        "TRANS_RATE"
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
        TYPE_DATE,
        TYPE_LONG,
        TYPE_FLOAT
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
        {"Rusak","Kadaluarsa","Mutasi","Tolak","Lainnya"},
        {"Broken","Expired","Mutation","Reject","Else"}
    };
    
    public static Vector getReturnReasonType(int language) {
        Vector result = new Vector(1,1);
        for(int i=0; i<strReturnReasonList[language].length; i++) {
            result.add(strReturnReasonList[language][i]);
        }
        return result;
    }
    
    public PstMatReturn() {
    }
    
    public PstMatReturn(int i) throws DBException {
        super(new PstMatReturn());
    }
    
    public PstMatReturn(String sOid) throws DBException {
        super(new PstMatReturn(0));
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstMatReturn(long lOid) throws DBException {
        super(new PstMatReturn(0));
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
        return new PstMatReturn().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception{
        MatReturn matreturn = fetchExc(ent.getOID());
        ent = (Entity)matreturn;
        return matreturn.getOID();
    }
    
    synchronized public long insertExc(Entity ent) throws Exception{
        return insertExc((MatReturn) ent);
    }
    
    synchronized public long updateExc(Entity ent) throws Exception{
        return updateExc((MatReturn) ent);
    }
    
    synchronized public long deleteExc(Entity ent) throws Exception{
        if(ent==null){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public static MatReturn fetchExc(long oid) throws DBException {
        try {
            MatReturn matreturn = new MatReturn();
            PstMatReturn pstMatReturn = new PstMatReturn(oid);
            matreturn.setOID(oid);
            
            matreturn.setLocationId(pstMatReturn.getlong(FLD_LOCATION_ID));
            matreturn.setLocationType(pstMatReturn.getInt(FLD_LOCATION_TYPE));
            matreturn.setReturnTo(pstMatReturn.getlong(FLD_RETURN_TO));
            matreturn.setReturnDate(pstMatReturn.getDate(FLD_RETURN_DATE));
            matreturn.setRetCode(pstMatReturn.getString(FLD_RET_CODE));
            matreturn.setRetCodeCnt(pstMatReturn.getInt(FLD_RET_CODE_CNT));
            matreturn.setReturnStatus(pstMatReturn.getInt(FLD_RETURN_STATUS));
            matreturn.setReturnSource(pstMatReturn.getInt(FLD_RETURN_SOURCE));
            matreturn.setSupplierId(pstMatReturn.getlong(FLD_SUPPLIER_ID));
            matreturn.setPurchaseOrderId(pstMatReturn.getlong(FLD_PURCHASE_ORDER_ID));
            matreturn.setReceiveMaterialId(pstMatReturn.getlong(FLD_RECEIVE_MATERIAL_ID));
            matreturn.setRemark(pstMatReturn.getString(FLD_REMARK));
            matreturn.setReturnReason(pstMatReturn.getInt(FLD_RETURN_REASON));
            matreturn.setTransferStatus(pstMatReturn.getInt(FLD_TRANSFER_STATUS));
            matreturn.setInvoiceSupplier(pstMatReturn.getString(FLD_INVOICE_SUPPLIER));
            matreturn.setLastUpdate(pstMatReturn.getDate(FLD_LAST_UPDATE));
            matreturn.setCurrencyId(pstMatReturn.getlong(FLD_CURRENCY_ID));
            matreturn.setTransRate(pstMatReturn.getdouble(FLD_TRANS_RATE));
            
            return matreturn;
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatReturn(0),DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(MatReturn matreturn) throws DBException {
        try {
            PstMatReturn pstMatReturn = new PstMatReturn(0);
            
            pstMatReturn.setLong(FLD_LOCATION_ID, matreturn.getLocationId());
            pstMatReturn.setInt(FLD_LOCATION_TYPE, matreturn.getLocationType());
            pstMatReturn.setLong(FLD_RETURN_TO, matreturn.getReturnTo());
            pstMatReturn.setDate(FLD_RETURN_DATE, matreturn.getReturnDate());
            pstMatReturn.setString(FLD_RET_CODE, matreturn.getRetCode());
            pstMatReturn.setInt(FLD_RET_CODE_CNT, matreturn.getRetCodeCnt());
            pstMatReturn.setInt(FLD_RETURN_STATUS, matreturn.getReturnStatus());
            pstMatReturn.setInt(FLD_RETURN_SOURCE, matreturn.getReturnSource());
            pstMatReturn.setLong(FLD_SUPPLIER_ID, matreturn.getSupplierId());
            pstMatReturn.setLong(FLD_PURCHASE_ORDER_ID, matreturn.getPurchaseOrderId());
            pstMatReturn.setLong(FLD_RECEIVE_MATERIAL_ID, matreturn.getReceiveMaterialId());
            pstMatReturn.setString(FLD_REMARK, matreturn.getRemark());
            pstMatReturn.setInt(FLD_RETURN_REASON, matreturn.getReturnReason());
            pstMatReturn.setInt(FLD_TRANSFER_STATUS, matreturn.getTransferStatus());
            pstMatReturn.setString(FLD_INVOICE_SUPPLIER, matreturn.getInvoiceSupplier());
            pstMatReturn.setDate(FLD_LAST_UPDATE, matreturn.getLastUpdate());
            pstMatReturn.setLong(FLD_CURRENCY_ID, matreturn.getCurrencyId());
            pstMatReturn.setDouble(FLD_TRANS_RATE, matreturn.getTransRate());
            
            pstMatReturn.insert();
            matreturn.setOID(pstMatReturn.getlong(FLD_RETURN_MATERIAL_ID));
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatReturn.getInsertSQL());
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatReturn(0),DBException.UNKNOWN);
        }
        return matreturn.getOID();
    }
    
    public static long updateExc(MatReturn matreturn) throws DBException {
        try {
            if(matreturn.getOID() != 0) {
                PstMatReturn pstMatReturn = new PstMatReturn(matreturn.getOID());
                
                pstMatReturn.setLong(FLD_LOCATION_ID, matreturn.getLocationId());
                pstMatReturn.setInt(FLD_LOCATION_TYPE, matreturn.getLocationType());
                pstMatReturn.setLong(FLD_RETURN_TO, matreturn.getReturnTo());
                pstMatReturn.setDate(FLD_RETURN_DATE, matreturn.getReturnDate());
                pstMatReturn.setString(FLD_RET_CODE, matreturn.getRetCode());
                pstMatReturn.setInt(FLD_RET_CODE_CNT, matreturn.getRetCodeCnt());
                pstMatReturn.setInt(FLD_RETURN_STATUS, matreturn.getReturnStatus());
                pstMatReturn.setInt(FLD_RETURN_SOURCE, matreturn.getReturnSource());
                pstMatReturn.setLong(FLD_SUPPLIER_ID, matreturn.getSupplierId());
                pstMatReturn.setLong(FLD_PURCHASE_ORDER_ID, matreturn.getPurchaseOrderId());
                pstMatReturn.setLong(FLD_RECEIVE_MATERIAL_ID, matreturn.getReceiveMaterialId());
                pstMatReturn.setString(FLD_REMARK, matreturn.getRemark());
                pstMatReturn.setInt(FLD_RETURN_REASON, matreturn.getReturnReason());
                pstMatReturn.setInt(FLD_TRANSFER_STATUS, matreturn.getTransferStatus());
                pstMatReturn.setString(FLD_INVOICE_SUPPLIER, matreturn.getInvoiceSupplier());
                pstMatReturn.setDate(FLD_LAST_UPDATE, matreturn.getLastUpdate());
                pstMatReturn.setLong(FLD_CURRENCY_ID, matreturn.getCurrencyId());
                pstMatReturn.setDouble(FLD_TRANS_RATE, matreturn.getTransRate());
                
                pstMatReturn.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMatReturn.getUpdateSQL());
                return matreturn.getOID();
                
            }
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatReturn(0),DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long oid) throws DBException {
        try {
            PstMatReturn pstMatReturn = new PstMatReturn(oid);
            //Delete Item First
            PstMatReturnItem pstRETItem = new PstMatReturnItem();
            long result = pstRETItem.deleteExcByParent(oid);
            pstMatReturn.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatReturn.getDeleteSQL());
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatReturn(0),DBException.UNKNOWN);
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
                MatReturn matreturn = new MatReturn();
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
    
    public static void resultToObject(ResultSet rs, MatReturn matreturn) {
        try {
            matreturn.setOID(rs.getLong(fieldNames[FLD_RETURN_MATERIAL_ID]));
            matreturn.setLocationId(rs.getLong(fieldNames[FLD_LOCATION_ID]));
            matreturn.setLocationType(rs.getInt(fieldNames[FLD_LOCATION_TYPE]));
            matreturn.setReturnTo(rs.getLong(fieldNames[FLD_RETURN_TO]));
            matreturn.setReturnDate(rs.getDate(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE]));
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
            matreturn.setCurrencyId(rs.getLong(fieldNames[FLD_CURRENCY_ID]));
            matreturn.setTransRate(rs.getDouble(fieldNames[FLD_TRANS_RATE]));
        }
        catch(Exception e) {
        }
    }
    
    public static boolean checkOID(long matReturnId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_RETURN +
            " WHERE " + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
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
            String sql = "SELECT COUNT("+ PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] + ") FROM " + TBL_MAT_RETURN;
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
                    MatReturn matreturn = (MatReturn)list.get(ls);
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
            MatReturn matReturn = fetchExc(documentId);
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
            MatReturn myret = new MatReturn();
            myret.setLocationId(oidLocation);
            myret.setLocationType(locType);
            myret.setReturnTo(returnTo);
            myret.setSupplierId(oidSupplier);
            myret.setReturnDate(new Date());
            myret.setReturnStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
            myret.setReturnSource(returnSource);
            myret.setRemark("");
            myret.setRetCodeCnt(SessMatReturn.getIntCode(myret,new Date(),0,1));
            myret.setRetCode(SessMatReturn.getCodeReturn(myret));
            //Insert return
            hasil = PstMatReturn.insertExc(myret);
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
                            MatReturnItem retItem = new MatReturnItem();
                            retItem.setReturnMaterialId(hasil);
                            retItem.setMaterialId(myMat.getOID());
                            retItem.setUnitId(myMat.getDefaultStockUnitId());
                            retItem.setQty(quantity);
                            retItem.setCost(myMat.getDefaultCost());
                            retItem.setCurrencyId(myMat.getDefaultCostCurrencyId());
                            retItem.setTotal(quantity * myMat.getDefaultCost());
                            oidRETItem = PstMatReturnItem.insertExc(retItem);
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
            String sql = "SELECT SUM("+PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RESIDUE_QTY]+") "+
            " AS SUM_"+PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RESIDUE_QTY]+
            " FROM "+PstMatReturnItem.TBL_MAT_RETURN_ITEM+
            " WHERE "+PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID]+" = "+oid;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int sumqty = 0;
            while(rs.next()){
                sumqty = rs.getInt("SUM_"+PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RESIDUE_QTY]);
            }
            
            MatReturn matReturn = new MatReturn();
            matReturn = PstMatReturn.fetchExc(oid);
            if(sumqty==0){
                matReturn.setTransferStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
            }else{
                matReturn.setTransferStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
            }
            // update transfer status
            PstMatReturn.updateExc(matReturn);
            
        }catch(Exception e){}
    }
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MatReturn matReturn = PstMatReturn.fetchExc(oid);
         object.put(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID], matReturn.getOID());
         object.put(PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID], matReturn.getLocationId());
         object.put(PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_TYPE], matReturn.getLocationType());
         object.put(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_TO], matReturn.getReturnTo());
         object.put(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_DATE], matReturn.getReturnDate());
         object.put(PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE], matReturn.getRetCode());
         object.put(PstMatReturn.fieldNames[PstMatReturn.FLD_RET_CODE_CNT], matReturn.getRetCodeCnt());
         object.put(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS], matReturn.getReturnStatus());
         object.put(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_SOURCE], matReturn.getReturnSource());
         object.put(PstMatReturn.fieldNames[PstMatReturn.FLD_SUPPLIER_ID], matReturn.getSupplierId());
         object.put(PstMatReturn.fieldNames[PstMatReturn.FLD_PURCHASE_ORDER_ID], matReturn.getPurchaseOrderId());
         object.put(PstMatReturn.fieldNames[PstMatReturn.FLD_RECEIVE_MATERIAL_ID], matReturn.getReceiveMaterialId());
         object.put(PstMatReturn.fieldNames[PstMatReturn.FLD_REMARK], matReturn.getRemark());
         object.put(PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_REASON], matReturn.getReturnReason());
         object.put(PstMatReturn.fieldNames[PstMatReturn.FLD_TRANSFER_STATUS], matReturn.getTransferStatus());
         object.put(PstMatReturn.fieldNames[PstMatReturn.FLD_INVOICE_SUPPLIER], matReturn.getInvoiceSupplier());
         object.put(PstMatReturn.fieldNames[PstMatReturn.FLD_LAST_UPDATE], matReturn.getLastUpdate());
         object.put(PstMatReturn.fieldNames[PstMatReturn.FLD_CURRENCY_ID], matReturn.getCurrencyId());
         object.put(PstMatReturn.fieldNames[PstMatReturn.FLD_TRANS_RATE], matReturn.getTransRate());
      }catch(Exception exc){}
      return object;
   } 
}
