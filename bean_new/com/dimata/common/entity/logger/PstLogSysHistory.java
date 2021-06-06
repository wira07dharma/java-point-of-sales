/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.entity.logger;


import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author AGUS
 */

public class PstLogSysHistory extends DBHandler 
        implements I_DBInterface,I_DBType,I_PersintentExc,I_Language{
    
    public static final String TBL_LOG_HISTORY = "doc_log_history";
    public static final int FLD_LOG_ID = 0;
    public static final int FLD_LOG_DOCUMENT_ID = 1;
    public static final int FLD_LOG_USER_ID = 2;
    public static final int FLD_LOG_LOGIN_NAME = 3;
    public static final int FLD_LOG_DOCUMENT_NUMBER = 4;
    public static final int FLD_LOG_DOCUMENT_TYPE = 5;
    public static final int FLD_LOG_USER_ACTION = 6;
    public static final int FLD_LOG_OPEN_URL = 7;
    public static final int FLD_LOG_UPDATE_DATE = 8;
    public static final int FLD_LOG_APPLICATION = 9;
    public static final int FLD_LOG_DETAIL = 10;
    
   //NAMA MENGIKUTI NAMA2 FIELD DI DATABASE
    public static String [] fieldNames = {
        "LOG_ID",//0
        "LOG_DOCUMENT_ID",//1
        "LOG_USER_ID",//2
        "LOG_LOGIN_NAME",//3
        "LOG_DOCUMENT_NUMBER",//4
        "LOG_DOCUMENT_TYPE",//5
        "LOG_USER_ACTION",
        "LOG_OPEN_URL",
        "LOG_UPDATE_DATE",
        "LOG_APPLICATION",
        "LOG_DETAIL"
    };
    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_STRING
    };

     public PstLogSysHistory() {
    }

    public PstLogSysHistory(int i) throws DBException {
        super(new PstLogSysHistory());//merupakan induk constarktor dari DBHandler lalu membuat baru PstLogSysHistory
    }

    public PstLogSysHistory(String sOid) throws DBException {
        super(new PstLogSysHistory(0));//merupakan induk construktor dari DBHandler lalu membuat new PstLogSysHistory lalu memberi nilai defaoult 0
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstLogSysHistory(long lOid) throws DBException {
        super(new PstLogSysHistory(0));//merupakan induk construktor dari DBHandler, 0 fungsinya sebagai default PSTGEJALA
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
        return TBL_LOG_HISTORY;
    }
    public String[] getFieldNames() {
        return fieldNames;
    }
    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstLogSysHistory().getClass().getName();
    }
    public long fetchExc(Entity ent) throws Exception {
        LogSysHistory logSysHistory = fetchExc(ent.getOID());
        ent = (Entity) logSysHistory;
        return logSysHistory.getOID();
    }
    
    public static synchronized long insertExc(LogSysHistory logSysHistory) 
            throws DBException {
         PstLogSysHistory pstLogSysHistory = new PstLogSysHistory(0);
    try {
            pstLogSysHistory.setLong(FLD_LOG_DOCUMENT_ID, logSysHistory.getLogDocumentId());
            pstLogSysHistory.setLong(FLD_LOG_USER_ID, logSysHistory.getLogUserId());
            pstLogSysHistory.setString(FLD_LOG_LOGIN_NAME, logSysHistory.getLogLoginName());
            pstLogSysHistory.setString(FLD_LOG_DOCUMENT_NUMBER, logSysHistory.getLogDocumentNumber());
            pstLogSysHistory.setString(FLD_LOG_DOCUMENT_TYPE, logSysHistory.getLogDocumentType());
            pstLogSysHistory.setString(FLD_LOG_USER_ACTION, logSysHistory.getLogUserAction());
            pstLogSysHistory.setString(FLD_LOG_OPEN_URL, logSysHistory.getLogOpenUrl());
            pstLogSysHistory.setDate(FLD_LOG_UPDATE_DATE, logSysHistory.getLogUpdateDate());
            pstLogSysHistory.setString(FLD_LOG_DOCUMENT_TYPE, logSysHistory.getLogDocumentType());
            pstLogSysHistory.setString(FLD_LOG_APPLICATION, logSysHistory.getLogApplication());
            pstLogSysHistory.setString(FLD_LOG_DETAIL, logSysHistory.getLogDetail());
            pstLogSysHistory.insert();
            
            logSysHistory.setOID(pstLogSysHistory.getlong(FLD_LOG_ID));

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstLogSysHistory(0), DBException.UNKNOWN);
        }
        return logSysHistory.getOID();
    }
    public static long insertUpdateLog(LogSysHistory logHis) throws DBException{
        PstLogSysHistory pstLogSysHistory = new PstLogSysHistory(0);       
        try {
            // check apakah detail log sudah ada
            boolean exist =false;
            // exist=checkLog
            if(exist){
            //jika ada : update
            } else {
             // jika tidak insert
             pstLogSysHistory.insertExc(logHis);
            }
            } catch (DBException dbe) {
                throw dbe;
            } catch (Exception e) {
                throw new DBException(new PstLogSysHistory(0), DBException.UNKNOWN);
            }
            return logHis.getOID();
        }
    
     public static long insertLog(LogSysHistory logHis) throws DBException{
        PstLogSysHistory pstLogSysHistory = new PstLogSysHistory(0);       
        try {
            pstLogSysHistory.insertExc(logHis);


            } catch (DBException dbe) {
                throw dbe;
            } catch (Exception e) {
                throw new DBException(new PstLogSysHistory(0), DBException.UNKNOWN);
            }
            return logHis.getOID();
        }
 
    /*public static long insertUpdateLog(PurchaseOrder po) {
        return insertUpdateLog(po);
    }*/
    /**
     * Keterangan: untuk mengambil data dari database berdasarkan oid jenisItemsId dan kemudaian di set objecknya
     * @param oid : negaraId
     * @return
     * @throws DBException 
     */
    public static LogSysHistory fetchExc(long oid) throws DBException {
        LogSysHistory logSysHistory = new LogSysHistory();
        /*try {
            LogSysHistory logSysHistory = new LogSysHistory();
            PstLogSysHistory pstLogSysHistory = new PstLogSysHistory(oid);
            logSysHistory.setOID(oid);
            logSysHistory.setSource_log_id(pstLogSysHistory.getLong(FLD_SOURCE_LOG_ID));
            logSysHistory.setModule_transfer(pstLogSysHistory.getString(FLD_MODUL_TRANSFER));
            logSysHistory.setApplication(pstLogSysHistory.getString(FLD_APPLICATION));
            logSysHistory.setUpdate_date(pstLogSysHistory.getDate(FLD_UPDATE_DATE));
            logSysHistory.setHistory(pstLogSysHistory.getString(FLD_HISTORY));
            return logSysHistory;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstLogSysHistory(0), DBException.UNKNOWN);
        }*/
        return logSysHistory;
    }

    
    public long insertExc(Entity ent) throws Exception {

        return insertExc((LogSysHistory) ent);

    }
      /**
     * Ketrangan: fungsi untuk melakukan insert to database
     * @param logSysHistory
     * @return
     * @throws DBException 
     */
    

    public long updateExc(Entity ent) throws Exception {
        return updateExc((LogSysHistory) ent);
    }

/**
 * Keterangan: fungsi untuk update data to database
 * @param logSysHistory
 * @return
 * @throws DBException 
 */
    public static long updateExc(LogSysHistory logSysHistory) throws DBException {
        /*try {
            if (logSysHistory.getOID() != 0) {
                PstLogSysHistory pstLogSysHistory = new PstLogSysHistory(logSysHistory.getOID());
                        
                pstLogSysHistory.setLong(FLD_SOURCE_LOG_ID, logSysHistory.getSource_log_id());
                pstLogSysHistory.setString(FLD_MODUL_TRANSFER, logSysHistory.getModule_transfer());
                pstLogSysHistory.setString(FLD_APPLICATION, logSysHistory.getApplication());
                pstLogSysHistory.setDate(FLD_UPDATE_DATE, logSysHistory.getUpdate_date());
                pstLogSysHistory.setString(FLD_HISTORY, logSysHistory.getHistory());
                pstLogSysHistory.update();

                return logSysHistory.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstLogSysHistory(0), DBException.UNKNOWN);
        }
*/
        return 0;
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

/**
 * Keterangan: delete data logSysHistory
 * @param oid
 * @return
 * @throws DBException 
 */
    public static long deleteExc(long oid) throws DBException {
       /* try {
            PstLogSysHistory pstLogSysHistory = new PstLogSysHistory(oid);
            pstLogSysHistory.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstLogSysHistory(0), DBException.UNKNOWN);
        }*/
        return oid;
    }

     /**
     * KETERANGAN: Fungsi untuk melakukan list table logSysHistory , berdasarkan parameter di bawah
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @param order
     * @return 
     */
    public static Vector listPurchaseOrder(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + fieldNames[PstLogSysHistory.FLD_LOG_LOGIN_NAME] + ", " + fieldNames[PstLogSysHistory.FLD_LOG_USER_ACTION] +
                    ", " + fieldNames[PstLogSysHistory.FLD_LOG_UPDATE_DATE] + ", " + fieldNames[PstLogSysHistory.FLD_LOG_APPLICATION] +  ", " + fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_TYPE] +
                    ", " + fieldNames[PstLogSysHistory.FLD_LOG_DETAIL] + ", " + fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_NUMBER] + " FROM " + TBL_LOG_HISTORY;
            
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
            while (rs.next()) 
            {
                LogSysHistory logSysHistory = new LogSysHistory();
                resultToObject(rs, logSysHistory);
                lists.add(logSysHistory);
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
    
    
    // add by fitra 06-05-2014
    

  
  
  
  
    
    public static Vector listAll() {

        return listPurchaseOrder(0, 500, "", "");

    }
    
    //SET NILAI UNTUK DITAMPILKAN
     public static void resultToObject(ResultSet rs, LogSysHistory logSysHistory) {
         
        try {
            logSysHistory.setLogDocumentNumber(rs.getString(PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_NUMBER]));
            
            //logSysHistory.setOID(rs.getLong(PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_ID]));
            logSysHistory.setLogApplication(rs.getString(PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_APPLICATION]));
            //logSysHistory.setLogDocumentId(rs.getLong(PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID]));
            //logSysHistory.setLogUserId(rs.getLong(PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_USER_ID]));
            logSysHistory.setLogLoginName(rs.getString(PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_LOGIN_NAME]));
            logSysHistory.setLogDocumentType(rs.getString(PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_TYPE]));
            logSysHistory.setLogUserAction(rs.getString(PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_USER_ACTION]));
           // logSysHistory.setLogOpenUrl(rs.getString(PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_OPEN_URL]));
            logSysHistory.setLogUpdateDate(rs.getTimestamp(PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_UPDATE_DATE]));
            logSysHistory.setLogDetail(rs.getString(PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL]));
        } catch (Exception e) {
        }
     }
     
    public static boolean checkOID(long mSId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_LOG_HISTORY + " WHERE "
                    + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_ID] + " = " + mSId;
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
            String sql = "SELECT COUNT(" + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_ID] 
                    + ") FROM " + TBL_LOG_HISTORY;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            dbrs = DBHandler.execQueryResult(sql);//execute query sql
            ResultSet rs = dbrs.getResultSet();
            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);//ambil isi ResultSet yg 1 atau PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_JENIS_ITEM_ID] 
            }
            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    
    
     public static Date getDateLog(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_UPDATE_DATE] 
                    + " FROM " + TBL_LOG_HISTORY;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            dbrs = DBHandler.execQueryResult(sql);//execute query sql
            ResultSet rs = dbrs.getResultSet();
            Date count = null;
            while (rs.next()) {
                 count = DBHandler.convertDate(rs.getDate(1), rs.getTime(1));
            //billMain.setBillDate(date);
            //    count = rs.getDate(1);//ambil isi ResultSet yg 1 atau PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_JENIS_ITEM_ID] 
            }
            rs.close();
            return count;
        } catch (Exception e) {
            return null;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
     
    public static String getLogLoginName(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_LOGIN_NAME] 
                    + " FROM " + TBL_LOG_HISTORY;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            dbrs = DBHandler.execQueryResult(sql);//execute query sql
            ResultSet rs = dbrs.getResultSet();
            String count = "";
            while (rs.next()) {
                 count = rs.getString(1);
            //billMain.setBillDate(date);
            //    count = rs.getDate(1);//ambil isi ResultSet yg 1 atau PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_JENIS_ITEM_ID] 
            }
            rs.close();
            return count;
        } catch (Exception e) {
            return null;
        } finally {
            DBResultSet.close(dbrs);
        }
    } 
    
    /**
     * keterangan: limit
     * @param oid : ini merupakan oid logSysHistory
     * @param recordToGet
     * @param whereClause
     * @param orderClause
     * @return 
     */
    public static int findLimitStart(long oid, int recordToGet
            , String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = listPurchaseOrder(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    LogSysHistory jenisItems = (LogSysHistory) list.get(ls);
                    if (oid == jenisItems.getOID()) {
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
    /* This method used to find command where current data */

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
   /**
    * mengambil user id yg membuat pertama kali dokumen atau data
    * @param docId
    * @return user ID
    */ 
    public static long getFirstUserId(long docId){
        long userId =0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT "+PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_USER_ID]+" FROM " + TBL_LOG_HISTORY
                    + " WHERE " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID] + " = " + docId 
                    + " ORDER BY "+ PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_UPDATE_DATE] +" ASC  LIMIT 0,1";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                userId = rs.getLong(1);
                break;
            }
            rs.close();
            return userId;
            
        }catch(Exception e) {
            System.out.println(e.toString());
        }
        return userId;
    }
    
    /**
     * INSERT DATA KETIKA SUDAH 2 JAM, PROSES : 
     * 1. TAMPILKAN WAKTU UPDATE TERAKHIR BERDASARKAN ID DOKUMEN TERTENTU
     * 2. SELISIHKAN WAKTU UPDATE TERAKHIR DENGAN WAKTU SEKARANG
     * 3. JIKA LEBIH BESAR DARI 2 JAM,,MAKA INSERTKAN DATA
     */
    public static int getLastUpdateTime(long docId) {
    int checkTime =0;
        
        DBResultSet dbrs = null;
        try {
            
            String sql = "SELECT HOUR(TIMEDIFF(NOW(), "+PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_UPDATE_DATE]
                    +")) FROM "+TBL_LOG_HISTORY+" WHERE "+PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID]
                    +"="+docId+" ORDER BY "+PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_UPDATE_DATE]+" DESC LIMIT 0,1;" ;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                checkTime = rs.getInt(1);
                break;
            }
            
            rs.close();
            return checkTime;
            
        }catch(Exception e) {
            System.out.println(e.toString());
        }
        return checkTime;
    }
}