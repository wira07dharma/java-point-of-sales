/**
 * User: wardana
 * Date: Apr 8, 2004
 * Time: 1:00:51 PM
 * Version: 1.0
 */
package com.dimata.harisma.entity.employee;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;



import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.Entity;
import com.dimata.util.lang.I_Language;
import com.dimata.util.Formater;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Date;

public class PstDpStockManagement extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_DP_STOCK_MANAGEMENT = "HR_DP_STOCK_MANAGEMENT";

    public static final int FLD_DP_STOCK_ID = 0;
    public static final int FLD_LEAVE_STOCK_ID = 1;
    public static final int FLD_DP_QTY = 2;
    public static final int FLD_OWNING_DATE = 3;
    public static final int FLD_EXPIRED_DATE = 4;
    public static final int FLD_EXCEPTION_FLAG = 5;
    public static final int FLD_EXPIRED_DATE_EXC = 6;
    public static final int FLD_DP_STATUS = 7;
    public static final int FLD_NOTE = 8;

    public static final String[] fieldNames = {
        "DP_STOCK_ID",
        "LEAVE_STOCK_ID",
        "DP_QTY",
        "OWNING_DATE",
        "EXPIRED_DATE",
        "EXCEPTION_FLAG",
        "EXPIRED_DATE_EXC",
        "DP_STATUS",
        "NOTE"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_INT,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT,
        TYPE_DATE,
        TYPE_INT,
        TYPE_STRING
    };

    public static int DP_QTY_COUNT = 2;     // in days
    public static int DP_EXPIRED_COUNT = 4; // in month;

    public static int EXC_STS_NO = 0;
    public static int EXC_STS_YES = 1;

    public static int DP_STS_AKTIF = 0;
    public static int DP_STS_NOT_AKTIF = 1;

    public PstDpStockManagement() {
    }

    public PstDpStockManagement(int i) throws DBException {
        super(new PstDpStockManagement());
    }

    public PstDpStockManagement(String sOid) throws DBException {
        super(new PstDpStockManagement(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstDpStockManagement(long lOid) throws DBException {
        super(new PstDpStockManagement(0));
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
        return TBL_DP_STOCK_MANAGEMENT;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstDpStockManagement().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        DpStockManagement objDpStockMgn = fetchExc(ent.getOID());
        return objDpStockMgn.getOID();
    }

    public static DpStockManagement fetchExc(long oid) throws DBException {
        try {
            DpStockManagement objDpStockMgn = new DpStockManagement();
            PstDpStockManagement objPstDpStockMgn = new PstDpStockManagement(oid);
            objDpStockMgn.setOID(oid);

            objDpStockMgn.setlLeaveStockId(objPstDpStockMgn.getlong(FLD_LEAVE_STOCK_ID));
            objDpStockMgn.setiDpQty(objPstDpStockMgn.getInt(FLD_DP_QTY));
            objDpStockMgn.setDtOwningDate(objPstDpStockMgn.getDate(FLD_OWNING_DATE));
            objDpStockMgn.setDtExpiredDate(objPstDpStockMgn.getDate(FLD_EXPIRED_DATE));
            objDpStockMgn.setiExceptionFlag(objPstDpStockMgn.getInt(FLD_EXCEPTION_FLAG));
            objDpStockMgn.setDtExpiredDateExc(objPstDpStockMgn.getDate(FLD_EXPIRED_DATE_EXC));
            objDpStockMgn.setiDpStatus(objPstDpStockMgn.getInt(FLD_DP_STATUS));
            objDpStockMgn.setStNote(objPstDpStockMgn.getString(FLD_NOTE));

            return objDpStockMgn;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDpStockManagement(0), DBException.UNKNOWN);
        }
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((DpStockManagement) ent);
    }

    public static long updateExc(DpStockManagement objDpStockMgn) throws DBException {
        try {
            if (objDpStockMgn.getOID() != 0) {
                PstDpStockManagement objPstDpStockMgn = new PstDpStockManagement(objDpStockMgn.getOID());

                objPstDpStockMgn.setLong(FLD_LEAVE_STOCK_ID, objDpStockMgn.getlLeaveStockId());
                objPstDpStockMgn.setInt(FLD_DP_QTY, objDpStockMgn.getiDpQty());
                objPstDpStockMgn.setDate(FLD_OWNING_DATE, objDpStockMgn.getDtOwningDate());
                objPstDpStockMgn.setDate(FLD_EXPIRED_DATE, objDpStockMgn.getDtExpiredDate());
                objPstDpStockMgn.setInt(FLD_EXCEPTION_FLAG, objDpStockMgn.getiExceptionFlag());
                objPstDpStockMgn.setDate(FLD_EXPIRED_DATE_EXC, objDpStockMgn.getDtExpiredDateExc());
                objPstDpStockMgn.setInt(FLD_DP_STATUS, objDpStockMgn.getiDpStatus());
                objPstDpStockMgn.setString(FLD_NOTE, objDpStockMgn.getStNote());

                objPstDpStockMgn.update();
                return objDpStockMgn.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDpStockManagement(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long deleteExc(Entity ent) throws Exception {
        return deleteExc(ent.getOID());
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstDpStockManagement objPstDpStockMgn = new PstDpStockManagement(oid);
            objPstDpStockMgn.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDpStockManagement(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((DpStockManagement)ent);
    }

    public static long insertExc(DpStockManagement objDpStockMgn) throws DBException {
        try {
            PstDpStockManagement objPstDpStockMgn = new PstDpStockManagement(0);

            objPstDpStockMgn.setLong(FLD_LEAVE_STOCK_ID, objDpStockMgn.getlLeaveStockId());
            objPstDpStockMgn.setInt(FLD_DP_QTY, objDpStockMgn.getiDpQty());
            objPstDpStockMgn.setDate(FLD_OWNING_DATE, objDpStockMgn.getDtOwningDate());
            objPstDpStockMgn.setDate(FLD_EXPIRED_DATE, objDpStockMgn.getDtExpiredDate());
            objPstDpStockMgn.setInt(FLD_EXCEPTION_FLAG, objDpStockMgn.getiExceptionFlag());
            objPstDpStockMgn.setDate(FLD_EXPIRED_DATE_EXC, objDpStockMgn.getDtExpiredDateExc());
            objPstDpStockMgn.setInt(FLD_DP_STATUS, objDpStockMgn.getiDpStatus());
            objPstDpStockMgn.setString(FLD_NOTE, objDpStockMgn.getStNote());

            objPstDpStockMgn.insert();
            objDpStockMgn.setOID(objPstDpStockMgn.getlong(FLD_DP_STOCK_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDpStockManagement(0), DBException.UNKNOWN);
        }
        return objDpStockMgn.getOID();
    }

    private static void resultToObject(ResultSet rs, DpStockManagement objDpStockMgn) {
        try {
            objDpStockMgn.setOID(rs.getLong(PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STOCK_ID]));
            objDpStockMgn.setlLeaveStockId(rs.getLong(PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_LEAVE_STOCK_ID]));
            objDpStockMgn.setiDpQty(rs.getInt(PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_QTY]));
            objDpStockMgn.setDtOwningDate(rs.getDate(PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_OWNING_DATE]));
            objDpStockMgn.setDtExpiredDate(rs.getDate(PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EXPIRED_DATE]));
            objDpStockMgn.setiExceptionFlag(rs.getInt(PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EXCEPTION_FLAG]));
            objDpStockMgn.setDtExpiredDateExc(rs.getDate(PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EXPIRED_DATE_EXC]));
            objDpStockMgn.setiDpStatus(rs.getInt(PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STATUS]));
            objDpStockMgn.setStNote(rs.getString(PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_NOTE]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Vector listDpByExpDate(Date dtExpDate){
        Vector vList = new Vector();
        DBResultSet dbrs;
        String stSQL = " SELECT * FROM "+ TBL_DP_STOCK_MANAGEMENT +
                       " WHERE ("+PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EXPIRED_DATE]+ " = '"+Formater.formatDate(dtExpDate, "yyyy-MM-dd") + "' " +
                       " OR "+PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EXPIRED_DATE_EXC]+ " = '"+Formater.formatDate(dtExpDate, "yyyy-MM-dd") + "') " +
                       " AND "+PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STATUS] + " = " +PstDpStockManagement.DP_STS_AKTIF;
        try{
            dbrs = DBHandler.execQueryResult(stSQL);
            ResultSet rs = dbrs.getResultSet();
            DpStockManagement objDpStockMgn = new DpStockManagement();
            while(rs.next()){
                objDpStockMgn = new DpStockManagement();
                resultToObject(rs, objDpStockMgn);
                vList.add(objDpStockMgn);
            }
        }
        catch(DBException dbe){
            dbe.printStackTrace();
        }
        catch(SQLException sqle){
            sqle.printStackTrace();
        }
        return vList;
    }
}
