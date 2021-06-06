/* Created on 	:  [date] [time] AM/PM
 *
 * @author  	: karya
 * @version  	: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.common.entity.logger;

import java.sql.*;
import java.util.*;
import java.util.Date;

import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.*;

public class PstDocLogger extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_DOC_LOGGER = "doc_logger";

    public static final int FLD_DOC_LOGGER_ID = 0;
    public static final int FLD_DOC_TYPE = 1;
    public static final int FLD_DOC_OID = 2;
    public static final int FLD_DOC_NUMBER = 3;
    public static final int FLD_DESCRIPTION = 4;
    public static final int FLD_DOC_DATE = 5;

    public static final String[] fieldNames = {
        "DOC_LOGGER_ID",
        "DOC_TYPE",
        "DOC_OID",
        "DOC_NUMBER",
        "DESCRIPTION",
        "DOC_DATE"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_INT,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE
    };

    public PstDocLogger() {
    }

    public PstDocLogger(int i) throws DBException {
        super(new PstDocLogger());
    }

    public PstDocLogger(String sOid) throws DBException {
        super(new PstDocLogger(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstDocLogger(long lOid) throws DBException {
        super(new PstDocLogger(0));
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
        return TBL_DOC_LOGGER;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstDocLogger().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        try {
            DocLogger docLogger = (DocLogger) ent;
            long oid = ent.getOID();
            PstDocLogger pstDocLogger = new PstDocLogger(oid);
            docLogger.setOID(oid);

            docLogger.setDocType(pstDocLogger.getInt(FLD_DOC_TYPE));
            docLogger.setDocOid(pstDocLogger.getlong(FLD_DOC_OID));
            docLogger.setDocNumber(pstDocLogger.getString(FLD_DOC_NUMBER));
            docLogger.setDescription(pstDocLogger.getString(FLD_DESCRIPTION));
            docLogger.setDocDate(pstDocLogger.getDate(FLD_DOC_DATE));

            return docLogger.getOID();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDocLogger(0), DBException.UNKNOWN);
        }
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((DocLogger) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((DocLogger) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static DocLogger fetchExc(long oid) throws DBException {
        try {
            DocLogger docLogger = new DocLogger();
            PstDocLogger pstDocLogger = new PstDocLogger(oid);
            docLogger.setOID(oid);

            docLogger.setDocType(pstDocLogger.getInt(FLD_DOC_TYPE));
            docLogger.setDocOid(pstDocLogger.getlong(FLD_DOC_OID));
            docLogger.setDocNumber(pstDocLogger.getString(FLD_DOC_NUMBER));
            docLogger.setDescription(pstDocLogger.getString(FLD_DESCRIPTION));
            docLogger.setDocDate(pstDocLogger.getDate(FLD_DOC_DATE));

            return docLogger;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDocLogger(0), DBException.UNKNOWN);
        }
    }

    synchronized public static long insertExc(DocLogger docLogger) throws DBException {
        try {
            PstDocLogger pstDocLogger = new PstDocLogger(0);

            pstDocLogger.setInt(FLD_DOC_TYPE, docLogger.getDocType());
            pstDocLogger.setLong(FLD_DOC_OID, docLogger.getDocOid());
            pstDocLogger.setString(FLD_DOC_NUMBER, docLogger.getDocNumber());
            pstDocLogger.setString(FLD_DESCRIPTION, docLogger.getDescription());
            pstDocLogger.setDate(FLD_DOC_DATE, docLogger.getDocDate());

            pstDocLogger.insert();
            docLogger.setOID(pstDocLogger.getlong(FLD_DOC_LOGGER_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDocLogger(0), DBException.UNKNOWN);
        }
        return docLogger.getOID();
    }

    synchronized public static long updateExc(DocLogger docLogger) throws DBException {
        try {
            if (docLogger.getOID() != 0) {
                PstDocLogger pstLocation = new PstDocLogger(docLogger.getOID());

                pstLocation.setString(FLD_DOC_NUMBER, docLogger.getDocNumber());
                pstLocation.setInt(FLD_DOC_TYPE, docLogger.getDocType());
                pstLocation.setLong(FLD_DOC_OID, docLogger.getDocOid());
                pstLocation.setString(FLD_DESCRIPTION, docLogger.getDescription());
                pstLocation.setDate(FLD_DOC_DATE, docLogger.getDocDate());

                pstLocation.update();
                return docLogger.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDocLogger(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstDocLogger pstLocation = new PstDocLogger(oid);
            pstLocation.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDocLogger(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_DOC_LOGGER;
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
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            //System.out.println("List sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                DocLogger docLogger = new DocLogger();
                resultToObject(rs, docLogger);
                lists.add(docLogger);
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

    synchronized private static void resultToObject(ResultSet rs, DocLogger docLogger) {
        try {
            docLogger.setOID(rs.getLong(PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_LOGGER_ID]));
            docLogger.setDocNumber(rs.getString(PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_NUMBER]));
            docLogger.setDocType(rs.getInt(PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_TYPE]));
            docLogger.setDocOid(rs.getLong(PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_OID]));
            docLogger.setDescription(rs.getString(PstDocLogger.fieldNames[PstDocLogger.FLD_DESCRIPTION]));
            docLogger.setDocDate(rs.getDate(PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_DATE]));
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long locationId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_DOC_LOGGER + " WHERE " +
                    PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_LOGGER_ID] + " = " + locationId;

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
            String sql = "SELECT COUNT(" + PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_LOGGER_ID] + ") FROM " + TBL_DOC_LOGGER;
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
    
    public static int getMaxTypeLog(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT MAX(" + PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_TYPE] + ") FROM " + TBL_DOC_LOGGER;
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
    public static int findLimitStart(long oid, int recordToGet, String whereClause) {
        String order = "";
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, order);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    DocLogger docLogger = (DocLogger) list.get(ls);
                    if (oid == docLogger.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }


    /***  function for data synchronization ***/
    public long insertExcSynch(Entity ent) throws Exception {
        return insertExcSynch((DocLogger) ent);
    }

    synchronized public static long insertExcSynch(DocLogger docLogger) throws DBException {
        long newOID = 0;
        long originalOID = docLogger.getOID();
        try {
            newOID = insertExc(docLogger);
            if (newOID != 0) {  // sukses insert ?
                updateSynchOID(newOID, originalOID);
                return originalOID;
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDocLogger(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long updateSynchOID(long newOID, long originalOID) throws DBException {
        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE " + PstDocLogger.TBL_DOC_LOGGER + " SET " +
                    PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_LOGGER_ID] + " = " + originalOID +
                    " WHERE " + PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_LOGGER_ID] +
                    " = " + newOID;

            int Result = DBHandler.execUpdate(sql);
            return originalOID;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public long synchronizeOID(long oldOID, long newOID) {
        String sql = "UPDATE " + TBL_DOC_LOGGER +
                " SET " + fieldNames[FLD_DOC_LOGGER_ID] + "=" + newOID +
                " WHERE " + fieldNames[FLD_DOC_LOGGER_ID] + "=" + oldOID;

        try {
            DBHandler.execUpdate(sql);
        } catch (Exception e) {
            return 0;
        }
        return newOID;
    }


    /** gadnyana
     * delete doc logger
     * @param docCode
     * @param docType
     * @return
     */
    synchronized public static boolean deleteDataBo_inDocLogger(String docCode, int docType) {
        try {
            String whereClause = fieldNames[FLD_DOC_NUMBER] + "='" + docCode + "' AND " +
                    fieldNames[FLD_DOC_TYPE] + "=" + docType;
            Vector list = PstDocLogger.list(0, 0, whereClause, "");
            DocLogger dLogger = new DocLogger();
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    dLogger = (DocLogger) list.get(k);
                }
                deleteExc(dLogger.getOID());
            }
        } catch (Exception e) {
            System.out.println("Error data insert ke dokumen logger : " + e.toString());
        }
        return true;
    }


    /** gadnyana
     * untuk peng-updatetan data yang ada di doc logger
     * sesuai dengan number documen dan type documen
     * @return
     */
    synchronized public static boolean updateDataBo_toDocLogger(DocLogger docLogger) {
        try {
            String whereClause = fieldNames[FLD_DOC_NUMBER] + "='" + docLogger.getDocNumber() + "' AND " +
                    fieldNames[FLD_DOC_TYPE] + "=" + docLogger.getDocType();
            Vector list = PstDocLogger.list(0, 0, whereClause, "");
            DocLogger dLogger = new DocLogger();
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    dLogger = (DocLogger) list.get(k);
                }
                dLogger.setDocNumber(docLogger.getDocNumber());
                dLogger.setDocDate(docLogger.getDocDate());
                dLogger.setDescription(docLogger.getDescription());
                insertExc(dLogger);
            }
        } catch (Exception e) {
            System.out.println("Error data insert ke dokumen logger : " + e.toString());
        }
        return true;
    }


    /** gadnyana
     * untuk update data doc logger sesuai dengan data yang baru
     * tapi yang tidak berubah adalah number dari document
     * @param docLogger
     * @return
     */
    synchronized public static boolean updateDataBo_toDocLogger(String docCode, int docType, Date ddate, String desc) {
        try {
            String whereClause = fieldNames[FLD_DOC_NUMBER] + "='" + docCode + "' AND " +
                    fieldNames[FLD_DOC_TYPE] + "=" + docType;
            Vector list = PstDocLogger.list(0, 0, whereClause, "");
            DocLogger dLogger = new DocLogger();
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    dLogger = (DocLogger) list.get(k);
                }
                dLogger.setDocNumber(docCode);
                dLogger.setDocType(docType);
                dLogger.setDocDate(ddate);
                dLogger.setDescription(desc);
                updateExc(dLogger);
            }
        } catch (Exception e) {
            System.out.println("Error data insert ke dokumen logger : " + e.toString());
        }
        return true;
    }

    /** gadnyana
     * untuk insert data bo ke doc logger
     * yang di pakai dalam implementasi IJ
     * @param docCode
     * @param docType
     * @param ddate
     * @param desc
     * @return
     */
    synchronized public static boolean insertDataBo_toDocLogger(String docCode, int docType, Date ddate, String desc) {
        try {
            DocLogger dLogger = new DocLogger();
            dLogger.setDocNumber(docCode);
            dLogger.setDocType(docType);
            dLogger.setDocDate(ddate);
            dLogger.setDescription(desc);

            insertExc(dLogger);
        } catch (Exception e) {
            System.out.println("Error data insert ke dokumen logger : " + e.toString());
        }
        return true;
    }


    /** gadnyana
     *  fungsi ini ada karena penjualan di jurnal per shift
     * tidak per doc.
     * @param docCode
     * @param docType
     * @param ddate
     * @param desc
     * @return
     */
    synchronized public static boolean insertUpdateDataBo_toDocLogger(String docCode, int docType, Date ddate, String desc) 
    {
        try 
        {
            String whereClause = fieldNames[FLD_DOC_NUMBER] + "='" + docCode + 
                                 "' AND " + fieldNames[FLD_DOC_TYPE] + "=" + docType;
            Vector list = PstDocLogger.list(0, 0, whereClause, "");
            DocLogger dLogger = new DocLogger();
            if (list != null && list.size() > 0) 
            {        
                dLogger = (DocLogger) list.get(0);
                dLogger.setDocNumber(docCode);
                dLogger.setDocType(docType);
                dLogger.setDocDate(ddate);
                dLogger.setDescription(desc);
                updateExc(dLogger);
            }
            else 
            {
                dLogger = new DocLogger();
                dLogger.setDocNumber(docCode);
                dLogger.setDocType(docType);
                dLogger.setDocDate(ddate);
                dLogger.setDescription(desc);
                insertExc(dLogger);
            }
        }
        catch (Exception e) 
        {
            System.out.println("Error data insert ke dokumen logger : " + e.toString());
        }
        return true;
    }

    /**
     *
     * @param docOid
     * @param docType
     * @param docCode
     * @param ddate
     * @param desc
     * @return
     */    
    synchronized public static boolean generateDocLogger(long docOid, String docCode, int docType, Date ddate, String desc) 
    {
        try 
        {
            String whereClause = fieldNames[FLD_DOC_OID] + " = " + docOid;
            Vector list = PstDocLogger.list(0, 0, whereClause, "");
            DocLogger dLogger = new DocLogger();
            if (list != null && list.size() > 0) 
            {        
                dLogger = (DocLogger) list.get(0);                
                dLogger.setDocOid(docOid);
                dLogger.setDocType(docType);
                dLogger.setDocNumber(docCode);                
                dLogger.setDocDate(ddate);
                dLogger.setDescription(desc);
                updateExc(dLogger);
            }
            else 
            {
                dLogger = new DocLogger();
                dLogger.setDocOid(docOid);                
                dLogger.setDocType(docType);
                dLogger.setDocNumber(docCode);
                dLogger.setDocDate(ddate);
                dLogger.setDescription(desc);
                insertExc(dLogger);
            }
        }
        catch (Exception e) 
        {
            System.out.println("Error data insert ke dokumen logger : " + e.toString());
        }
        return true;
    }
    
}
