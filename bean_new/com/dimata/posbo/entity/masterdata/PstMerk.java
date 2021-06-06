/* Created on 	:  [date] [time] AM/PM
 *
 * @author	 :
 * @version	 :
 */
/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/
package com.dimata.posbo.entity.masterdata;

/* package java */
import com.dimata.common.entity.custom.PstDataCustom;
import java.sql.*;
import java.util.*;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
//import com.dimata.qdep.entity.*;

/* package garment */
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.Entity;
import org.json.JSONObject;

public class PstMerk extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    //public static final  String TBL_MAT_MERK = "POS_MERK";
    public static final String TBL_MAT_MERK = "pos_merk";
    public static final int FLD_MERK_ID = 0;
    public static final int FLD_NAME = 1;
    public static final int FLD_CODE = 2;
    //adding status ditampilkan atau tidak by mirahu 20120511
    public static final int FLD_STATUS = 3;
    public static final String[] fieldNames = {
        "MERK_ID",
        "NAME",
        "CODE",
    //adding status ditampilkan atau tidak by mirahu 20120511
        "STATUS"
        
            
    };
    
     //adding status ditampilkan atau tidak by mirahu 20120511
        public static int USE_NO_SHOW = 0 ;
        public static int USE_SHOW = 1;
    
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING, TYPE_STRING, TYPE_INT
    };

    public PstMerk() {
    }

    public PstMerk(int i) throws DBException {
        super(new PstMerk());
    }

    public PstMerk(String sOid) throws DBException {
        super(new PstMerk(0));
        try {
            if (!locate(sOid)) {
                throw new DBException(this, DBException.RECORD_NOT_FOUND);
            } else {
                return;
            }
        } catch (Exception e) {
        }
    }

    public PstMerk(long lOid) throws DBException {
        super(new PstMerk(0));
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
        return TBL_MAT_MERK;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMerk().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        Merk matmerk = fetchExc(ent.getOID());
        ent = (Entity) matmerk;
        return matmerk.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((Merk) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((Merk) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static Merk fetchExc(long oid) throws DBException {
        try {
            Merk matmerk = new Merk();
            PstMerk pstMerk = new PstMerk(oid);
            matmerk.setOID(oid);

            matmerk.setName(pstMerk.getString(FLD_NAME));
            matmerk.setCode(pstMerk.getString(FLD_CODE));
            //adding status ditampilkan atau tidak by mirahu 20120511
            matmerk.setStatus(pstMerk.getInt(FLD_STATUS));

            return matmerk;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMerk(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(Merk matmerk) throws DBException {
        try {
            PstMerk pstMerk = new PstMerk(0);

            pstMerk.setString(FLD_NAME, matmerk.getName());
            pstMerk.setString(FLD_CODE, matmerk.getCode());
            //adding status ditampilkan atau tidak by mirahu 20120511
            pstMerk.setInt(FLD_STATUS, matmerk.getStatus());

            pstMerk.insert();

            long oidDataSync=PstDataSyncSql.insertExc(pstMerk.getInsertSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
            matmerk.setOID(pstMerk.getlong(FLD_MERK_ID));
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMerk.getInsertSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMerk(0), DBException.UNKNOWN);
        }
        return matmerk.getOID();
    }

    public static long updateExc(Merk matmerk) throws DBException {
        try {
            if (matmerk.getOID() != 0) {
                PstMerk pstMerk = new PstMerk(matmerk.getOID());

                pstMerk.setString(FLD_NAME, matmerk.getName());
                pstMerk.setString(FLD_CODE, matmerk.getCode());
                //adding status ditampilkan atau tidak by mirahu 20120511
                pstMerk.setInt(FLD_STATUS, matmerk.getStatus());

                pstMerk.update();

                long oidDataSync=PstDataSyncSql.insertExc(pstMerk.getUpdateSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMerk.getUpdateSQL());
                return matmerk.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMerk(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstMerk pstMerk = new PstMerk(oid);
            pstMerk.delete();

            long oidDataSync = PstDataSyncSql.insertExc(pstMerk.getDeleteSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMerk.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMerk(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_MERK;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }

                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }

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
                Merk matmerk = new Merk();
                resultToObject(rs, matmerk);
                lists.add(matmerk);
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
     * adnyana
     * untuk mencari list merk
     * @return hashtable
     */
    public static Hashtable getListAccountMerk() {
        Hashtable lists = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_MERK;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Merk matmerk = new Merk();
                resultToObject(rs, matmerk);
                lists.put(matmerk.getName().toUpperCase(), matmerk);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Hashtable();
    }

    public static void resultToObject(ResultSet rs, Merk matmerk) {
        try {
            matmerk.setOID(rs.getLong(PstMerk.fieldNames[PstMerk.FLD_MERK_ID]));
            matmerk.setName(rs.getString(PstMerk.fieldNames[PstMerk.FLD_NAME]));
            matmerk.setCode(rs.getString(PstMerk.fieldNames[PstMerk.FLD_CODE]));
            //adding status ditampilkan atau tidak by mirahu 20120511
            matmerk.setStatus(rs.getInt(PstMerk.fieldNames[PstMerk.FLD_STATUS]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long merkId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_MERK + " WHERE " +
                    PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + " = " + merkId;

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
            String sql = "SELECT COUNT(" + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + ") FROM " + TBL_MAT_MERK;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

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
                    Merk matmerk = (Merk) list.get(ls);
                    if (oid == matmerk.getOID()) {
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
    
    public static boolean checkMerk(String code, int type) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "";
            
            if(type==1){//kode
                sql="SELECT * FROM " + TBL_MAT_MERK + " WHERE " +
                    PstMerk.fieldNames[PstMerk.FLD_CODE] + " = '" + code+"'";
            }else{//nama
                 sql="SELECT * FROM " + TBL_MAT_MERK + " WHERE " +
                    PstMerk.fieldNames[PstMerk.FLD_NAME] + " = '" + code+"'";
            }   

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
    
    public static Merk fetchByName(String name) {
        com.dimata.posbo.db.DBResultSet dbrs = null;
        Merk merk = new Merk();
        try {
            String sql = "SELECT * FROM " + TBL_MAT_MERK +
                    " WHERE " + fieldNames[FLD_NAME] +
                    " = '" + name + "'";
            dbrs = com.dimata.posbo.db.DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                resultToObject(rs, merk);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            com.dimata.posbo.db.DBResultSet.close(dbrs);
        }
        return merk;
    }
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                Merk merk = PstMerk.fetchExc(oid);
                object.put(PstMerk.fieldNames[PstMerk.FLD_MERK_ID], merk.getOID());
                object.put(PstMerk.fieldNames[PstMerk.FLD_CODE], merk.getCode());
                object.put(PstMerk.fieldNames[PstMerk.FLD_NAME], merk.getName());
                object.put(PstMerk.fieldNames[PstMerk.FLD_STATUS], merk.getStatus());
            }catch(Exception exc){}

            return object;
        }    
}
