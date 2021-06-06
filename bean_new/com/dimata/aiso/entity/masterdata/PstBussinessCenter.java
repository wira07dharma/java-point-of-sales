/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.masterdata;

import com.dimata.aiso.db.DBException;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.I_DBInterface;
import com.dimata.aiso.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author dwi
 */

public class PstBussinessCenter extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language{
    
    public static final String TBL_BUSSINESS_CENTER = "aiso_buss_center";
   
    public static final int FLD_BUSS_CENTER_ID = 0;
    public static final int FLD_BUSS_CENTER_NAME = 1;
    public static final int FLD_BUSS_GROUP_ID = 2;
    public static final int FLD_CONTACT_ID = 3;
    public static final int FLD_BUSS_CENTER_DESC = 4;
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING
    };
    
    public static String[] fieldNames = {
        "BUSS_CENTER_ID",
        "BUSS_CENTER_NAME",
        "BUSS_GROUP_ID",
        "CONTACT_ID",
        "BUSS_CENTER_DESC"
    };
    
     public PstBussinessCenter() {
    }

    public PstBussinessCenter(int i) throws DBException {
        super(new PstBussinessCenter());
    }


    public PstBussinessCenter(String sOid) throws DBException {
        super(new PstBussinessCenter(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }


    public PstBussinessCenter(long lOid) throws DBException {
        super(new PstBussinessCenter(0));
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
        return TBL_BUSSINESS_CENTER;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstBussinessCenter().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        BussinessCenter objBussinessCenter = PstBussinessCenter.fetchExc(ent.getOID());
        ent = (Entity) objBussinessCenter;
        return objBussinessCenter.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return PstBussinessCenter.insertExc((BussinessCenter) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((BussinessCenter) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw  new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static BussinessCenter fetchExc(long oid) throws DBException {
        try {
            BussinessCenter objBussinessCenter = new BussinessCenter();
            PstBussinessCenter pBussinessCenter = new PstBussinessCenter(oid);

            objBussinessCenter.setOID(oid);
            objBussinessCenter.setBussCenterDesc(pBussinessCenter.getString(FLD_BUSS_CENTER_DESC));
            objBussinessCenter.setBussGroupId(pBussinessCenter.getlong(FLD_BUSS_GROUP_ID));
            objBussinessCenter.setContactId(pBussinessCenter.getlong(FLD_CONTACT_ID));
            objBussinessCenter.setBussCenterName(pBussinessCenter.getString(FLD_BUSS_CENTER_NAME));
            
            return objBussinessCenter;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBussinessCenter(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(BussinessCenter objBussinessCenter) throws DBException {
        try {
            PstBussinessCenter pBussinessCenter = new PstBussinessCenter(0);

            pBussinessCenter.setString(FLD_BUSS_CENTER_DESC, objBussinessCenter.getBussCenterDesc());
            pBussinessCenter.setLong(FLD_BUSS_GROUP_ID, objBussinessCenter.getBussGroupId());
            pBussinessCenter.setLong(FLD_CONTACT_ID, objBussinessCenter.getContactId());
            pBussinessCenter.setString(FLD_BUSS_CENTER_NAME, objBussinessCenter.getBussCenterName());
            pBussinessCenter.insert();

            objBussinessCenter.setOID(pBussinessCenter.getlong(FLD_BUSS_CENTER_ID));
            
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBussinessCenter(0), DBException.UNKNOWN);
        }
        return objBussinessCenter.getOID();
    }

    public static long updateExc(BussinessCenter objBussinessCenter) throws DBException {
        try {
            if (objBussinessCenter.getOID() != 0) {
                PstBussinessCenter pBussinessCenter = new PstBussinessCenter(objBussinessCenter.getOID());
                
                pBussinessCenter.setString(FLD_BUSS_CENTER_DESC, objBussinessCenter.getBussCenterDesc());
                pBussinessCenter.setLong(FLD_BUSS_GROUP_ID, objBussinessCenter.getBussGroupId());
                pBussinessCenter.setLong(FLD_CONTACT_ID, objBussinessCenter.getContactId());
                pBussinessCenter.setString(FLD_BUSS_CENTER_NAME, objBussinessCenter.getBussCenterName());
                
                pBussinessCenter.update();

                return objBussinessCenter.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBussinessCenter(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstBussinessCenter pBussinessCenter = new PstBussinessCenter(oid);
            pBussinessCenter.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBussinessCenter(0), DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_BUSSINESS_CENTER;
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
                    break;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                BussinessCenter objBussinessCenter = new BussinessCenter();
                resultToObject(rs, objBussinessCenter);
                lists.add(objBussinessCenter);
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

    private static void resultToObject(ResultSet rs, BussinessCenter objBussinessCenter) {
        try {
            objBussinessCenter.setOID(rs.getLong(fieldNames[FLD_BUSS_CENTER_ID]));
            objBussinessCenter.setBussCenterDesc(rs.getString(fieldNames[FLD_BUSS_CENTER_DESC]));
            objBussinessCenter.setBussGroupId(rs.getLong(fieldNames[FLD_BUSS_GROUP_ID]));
            objBussinessCenter.setContactId(rs.getLong(fieldNames[FLD_CONTACT_ID]));
            objBussinessCenter.setBussCenterName(rs.getString(fieldNames[FLD_BUSS_CENTER_NAME]));
            
        } catch (Exception e) {
            System.out.println("Exc when resultToObject : " + e.toString());
        }
    }

    
}
