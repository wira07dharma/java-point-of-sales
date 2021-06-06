/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.entity.masterdata;

/* package java */

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.posbo.entity.warehouse.*;
import com.dimata.qdep.entity.I_PstDocType;
import com.dimata.pos.entity.billing.*;
import com.dimata.pos.entity.payment.*;
import com.dimata.pos.entity.balance.*;
import org.json.JSONObject;
/**
 *
 * @author Dimata
 */
public class PstTempPostDoc extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    //public static final String TBL_TempPostDoc = "POS_TempPostDoc";
    public static final String TBL_TEMP_POST_DOC = "temp_post_doc";

    public static final int FLD_DOC_TYPE = 0;
    public static final int FLD_DOC_ID = 1;


    public static final String[] fieldNames =
            {
                "DOC_TYPE",
                "DOC_ID",
            };

    public static final int[] fieldTypes =
            {
                TYPE_INT,
                TYPE_LONG,
                
            };

    public PstTempPostDoc() {
    }

    public PstTempPostDoc(int i) throws DBException {
        super(new PstTempPostDoc());
    }

    public PstTempPostDoc(String sOid) throws DBException {
        super(new PstTempPostDoc(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstTempPostDoc(long lOid) throws DBException {
        super(new PstTempPostDoc(0));
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
        return TBL_TEMP_POST_DOC;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstTempPostDoc().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        TempPostDoc tempPostDoc = fetchExc(ent.getOID());
        ent = (Entity) tempPostDoc;
        return tempPostDoc.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((TempPostDoc) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((TempPostDoc) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static TempPostDoc fetchExc(long oid) throws DBException {
        try {
            TempPostDoc tempPostDoc = new TempPostDoc();
            PstTempPostDoc pstTempPostDoc = new PstTempPostDoc(oid);
            tempPostDoc.setOID(oid);

            tempPostDoc.setDocType(pstTempPostDoc.getInt(FLD_DOC_TYPE));
            tempPostDoc.setDocId(pstTempPostDoc.getlong(FLD_DOC_ID));
            

            return tempPostDoc;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTempPostDoc(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(TempPostDoc tempPostDoc) throws DBException {
        try {
            PstTempPostDoc pstTempPostDoc = new PstTempPostDoc(0);

            pstTempPostDoc.setInt(FLD_DOC_TYPE, tempPostDoc.getDocType());
            pstTempPostDoc.setLong(FLD_DOC_ID, tempPostDoc.getDocId());
            

            pstTempPostDoc.insert();
            
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTempPostDoc(0), DBException.UNKNOWN);
        }
        return tempPostDoc.getOID();
    }

    public static long updateExc(TempPostDoc tempPostDoc) throws DBException {
        try {
            if (tempPostDoc.getOID() != 0) {
                PstTempPostDoc pstTempPostDoc = new PstTempPostDoc(tempPostDoc.getOID());

                pstTempPostDoc.setInt(FLD_DOC_TYPE, tempPostDoc.getDocType());
                pstTempPostDoc.setLong(FLD_DOC_ID, tempPostDoc.getDocId());
                


                pstTempPostDoc.update();
                return tempPostDoc.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTempPostDoc(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstTempPostDoc PstTempPostDoc = new PstTempPostDoc(oid);
            PstTempPostDoc.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstTempPostDoc(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_TEMP_POST_DOC;
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
                TempPostDoc TempPostDoc = new TempPostDoc();
                resultToObject(rs, TempPostDoc);
                lists.add(TempPostDoc);
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

    private static void resultToObject(ResultSet rs, TempPostDoc tempPostDoc) {
        try {
            
            tempPostDoc.setDocType(rs.getInt(PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_TYPE]));
            tempPostDoc.setDocId(rs.getLong(PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID]));
            
        } catch (Exception e) {
        }
    }

    

    /*
     * Method ini digunakan untuk menghapus isi tabel temp_post_doc pada saat proses posting
     * 20110831
     * Created by Mirahu
     */
    public static void deleteHistoryPosting() {
        try {
            System.out.println("== >> DELETE HISTORY PREV POSTING in TABLE " +TBL_TEMP_POST_DOC);
            String sql = "DELETE FROM " + TBL_TEMP_POST_DOC;
            
            DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /** insert select doc recive ke temp_post_doc sebelum mulai posting
      * 20110831
      * @param locationId
      * Created by Mirahu
      */
    public static boolean insertSelectReceivePosting(long locationId, Connection con) {
        try {
            String sql = "INSERT INTO " + TBL_TEMP_POST_DOC +
                    " ( " +fieldNames[FLD_DOC_ID] + 
                    "," + fieldNames[FLD_DOC_TYPE] + ")" +
                    " SELECT DISTINCT " +
                    " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                    ", " + I_DocType.MAT_DOC_TYPE_LMRR +
                    " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " AS REC " +
                    " WHERE REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
                    " = " + locationId +
                    " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_FINAL;

            System.out.println("SQL Insert Select Receive : " + sql);
            
            DBHandler.execUpdate(sql, con);
            return true;
        } catch (Exception e) {
            System.out.println("Exc. in insertSelectReceivePosting(#,#) >> " + e.toString());
            return false;
        }
    }

    /** insert select doc recive ke temp_post_doc sebelum mulai posting
      * 20111010
      * @param locationId
      * Created by Opie-eyek 20121027
      */
    public static boolean insertSelectReceivePosting(long locationId) {
        try {
            String sql = "INSERT INTO " + TBL_TEMP_POST_DOC +
                    " ( " +fieldNames[FLD_DOC_ID] +
                    "," + fieldNames[FLD_DOC_TYPE] + ")" +
                    " SELECT DISTINCT " +
                    " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] +
                    ", " + I_DocType.MAT_DOC_TYPE_LMRR +
                    " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " AS REC " +
                    " WHERE REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] +
                    " = " + locationId +
                    " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_FINAL;

            System.out.println("SQL Insert Select Receive : " + sql);
            DBHandler.execUpdate(sql);
            return true;
        } catch (Exception e) {
            System.out.println("Exc. in insertSelectReceivePosting(#,#) >> " + e.toString());
            return false;
        }
    }

    /** insert select doc dispatch ke temp_post_doc sebelum mulai posting
      * 20121010
      * @param locationId
      * Created by Opie-eyek 20121027
      */
    public static boolean insertSelectDispatchPosting(long locationId) {
        try {
            String sql = "INSERT INTO " + TBL_TEMP_POST_DOC +
                    " ( " +fieldNames[FLD_DOC_ID] +
                    "," + fieldNames[FLD_DOC_TYPE] + ")" +
                    " SELECT DISTINCT " +
                    " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                    ", " + I_DocType.MAT_DOC_TYPE_DF +
                    " FROM " + PstMatDispatch.TBL_DISPATCH + " AS DF " +
                    " WHERE DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
                    " = " + locationId +
                    " AND DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_FINAL;

            System.out.println("SQL Insert Select Dispatch : " + sql);

            DBHandler.execUpdate(sql);
            return true;
        } catch (Exception e) {
            System.out.println("Exc. in insertSelectDispatchPosting(#,#) >> " + e.toString());
            return false;
        }
    }

    
     /** insert select doc dispatch ke temp_post_doc sebelum mulai posting 
      * 20110831
      * @param locationId
      * Created by Mirahu
      */   
    public static boolean insertSelectDispatchPosting(long locationId, Connection con) {
        try {
            String sql = "INSERT INTO " + TBL_TEMP_POST_DOC +
                    " ( " +fieldNames[FLD_DOC_ID] + 
                    "," + fieldNames[FLD_DOC_TYPE] + ")" +
                    " SELECT DISTINCT " +
                    " DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] +
                    ", " + I_DocType.MAT_DOC_TYPE_DF +
                    " FROM " + PstMatDispatch.TBL_DISPATCH + " AS DF " +
                    " WHERE DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] +
                    " = " + locationId +
                    " AND DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_FINAL;

            System.out.println("SQL Insert Select Dispatch : " + sql);
            
            DBHandler.execUpdate(sql,con);
            return true;
        } catch (Exception e) {
            System.out.println("Exc. in insertSelectDispatchPosting(#,#) >> " + e.toString());
            return false;
        }
    }

    /** insert select doc costing ke temp_post_doc sebelum mulai posting
      * 20110831-fix 14082012
      * @param locationId
      * Created by Mirahu
      */
    public static boolean insertSelectCostingPosting(long locationId, Connection con) {
        try {
            String sql = "INSERT INTO " + TBL_TEMP_POST_DOC +
                    " ( " +fieldNames[FLD_DOC_ID] +
                    "," + fieldNames[FLD_DOC_TYPE] + ")" +
                    " SELECT DISTINCT " +
                    " COST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                    ", " + I_DocType.MAT_DOC_TYPE_COS +
                    " FROM " + PstMatCosting.TBL_COSTING + " AS COST " +
                    " WHERE COST." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                    " = " + locationId +
                    " AND COST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_FINAL;

            System.out.println("SQL Insert Select Costing : " + sql);

            DBHandler.execUpdate(sql,con);
            return true;
            
        } catch (Exception e) {
            System.out.println("Exc. in insertSelectCostingPosting(#,#) >> " + e.toString());
            return false;
        }
    }

     /** insert select doc costing ke temp_post_doc sebelum mulai posting
      * 20110831-fix 14082012
      * @param locationId
      * Created by Opie-eyek 20121027
      */
    public static boolean insertSelectCostingPosting(long locationId) {
        try {
            String sql = "INSERT INTO " + TBL_TEMP_POST_DOC +
                    " ( " +fieldNames[FLD_DOC_ID] +
                    "," + fieldNames[FLD_DOC_TYPE] + ")" +
                    " SELECT DISTINCT " +
                    " COST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] +
                    ", " + I_DocType.MAT_DOC_TYPE_COS +
                    " FROM " + PstMatCosting.TBL_COSTING + " AS COST " +
                    " WHERE COST." + PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID] +
                    " = " + locationId +
                    " AND COST." + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_FINAL;

            System.out.println("SQL Insert Select Costing : " + sql);

            DBHandler.execUpdate(sql);
            return true;

        } catch (Exception e) {
            System.out.println("Exc. in insertSelectCostingPosting(#,#) >> " + e.toString());
            return false;
        }
    }

    /** insert select doc return ke temp_post_doc sebelum mulai posting
      * 20110831
      * @param locationId
      * Created by Mirahu
      */
    public static boolean insertSelectReturnPosting(long locationId, Connection con) {
        try {
            String sql = "INSERT INTO " + TBL_TEMP_POST_DOC +
                    " ( " +fieldNames[FLD_DOC_ID] +
                    "," + fieldNames[FLD_DOC_TYPE] + ")" +
                    " SELECT DISTINCT " +
                    " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                    ", " + I_DocType.MAT_DOC_TYPE_ROMR +
                    " FROM " + PstMatReturn.TBL_MAT_RETURN + " AS RET " +
                    " WHERE RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                    " = " + locationId +
                    " AND RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_FINAL;

            System.out.println("SQL Insert Select Return : " + sql);

            DBHandler.execUpdate(sql,con);
            return true;
        } catch (Exception e) {
            System.out.println("Exc. in insertSelectReturnPosting(#,#) >> " + e.toString());
            return false;
        }
    }

    /** insert select doc return ke temp_post_doc sebelum mulai posting
      * 20121010
      * @param locationId
      * Created by Opie-eyek 20121027
      */
    public static boolean insertSelectReturnPosting(long locationId) {
        try {
            String sql = "INSERT INTO " + TBL_TEMP_POST_DOC +
                    " ( " +fieldNames[FLD_DOC_ID] +
                    "," + fieldNames[FLD_DOC_TYPE] + ")" +
                    " SELECT DISTINCT " +
                    " RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                    ", " + I_DocType.MAT_DOC_TYPE_ROMR +
                    " FROM " + PstMatReturn.TBL_MAT_RETURN + " AS RET " +
                    " WHERE RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_LOCATION_ID] +
                    " = " + locationId +
                    " AND RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_FINAL;

            System.out.println("SQL Insert Select Return : " + sql);

            DBHandler.execUpdate(sql);
            return true;
        } catch (Exception e) {
            System.out.println("Exc. in insertSelectReturnPosting(#,#) >> " + e.toString());
            return false;
        }
    }



    /** insert select doc sales ke temp_post_doc sebelum mulai posting
      * 20110831
      * @param locationId
      * Created by Mirahu
      */

    public static boolean insertSelectSalesPosting(long locationId, Connection con) {
        try {
            String sql = "INSERT INTO " + TBL_TEMP_POST_DOC +
                    " ( " +fieldNames[FLD_DOC_ID] +
                    "," + fieldNames[FLD_DOC_TYPE] + ")" +
                    " SELECT DISTINCT " +
                    " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    ", " + I_DocType.MAT_DOC_TYPE_SALE +
                    " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS CBM " +
                    " INNER JOIN " + PstCashCashier.TBL_CASH_CASHIER + " AS CSH " +
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] +
                    " = CSH." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] +
                    " WHERE CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                    " = " + locationId +
                    " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_FINAL +
                    " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED+
                    " AND (CBM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_OPEN+
                    " OR CBM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"!="+PstBillMain.TRANS_TYPE_CASH+")"+
                    "AND CSH." + PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID] +
                        " != 1";

            System.out.println("SQL Insert Select Sales : " + sql);

            DBHandler.execUpdate(sql,con);
            return true;
        } catch (Exception e) {
            System.out.println("Exc. in insertSelectSalesPosting(#,#) >> " + e.toString());
            return false;
        }
    }

     /** insert select doc sales ke temp_post_doc sebelum mulai posting
      * 20121010
      * @param locationId
      * Created by Opie-eyek 20121027
      */

    public static boolean insertSelectSalesPosting(long locationId) {
        try {
            String sql = "INSERT INTO " + TBL_TEMP_POST_DOC +
                    " ( " +fieldNames[FLD_DOC_ID] +
                    "," + fieldNames[FLD_DOC_TYPE] + ")" +
                    " SELECT DISTINCT " +
                    " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                    ", " + I_DocType.MAT_DOC_TYPE_SALE +
                    " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS CBM " +
                    " INNER JOIN " + PstCashCashier.TBL_CASH_CASHIER + " AS CSH " +
                    " ON CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] +
                    " = CSH." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] +
                    " WHERE CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +
                    " = " + locationId +
                    " AND (CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_FINAL +" OR "+PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_DRAFT+" OR "+PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + "=" + I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED+") "+
                    " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED+
                    " AND (CBM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_OPEN+
                    " OR CBM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"!="+PstBillMain.TRANS_TYPE_CASH+")"+
                    "AND CSH." + PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID] +
                        " != 1";

            System.out.println("SQL Insert Select Sales : " + sql);

            DBHandler.execUpdate(sql);
            return true;
        } catch (Exception e) {
            System.out.println("Exc. in insertSelectSalesPosting(#,#) >> " + e.toString());
            return false;
        }
    }
    
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                TempPostDoc tempPostDoc = PstTempPostDoc.fetchExc(oid);
                object.put(PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_ID], tempPostDoc.getOID());
                object.put(PstTempPostDoc.fieldNames[PstTempPostDoc.FLD_DOC_TYPE], tempPostDoc.getDocType());
            }catch(Exception exc){}

            return object;
        }




}
