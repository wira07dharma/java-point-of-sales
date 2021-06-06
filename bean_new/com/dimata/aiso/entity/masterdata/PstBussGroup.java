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
public class PstBussGroup extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_BUSS_GROUP = "aiso_buss_group";
   
    /*
     buss_group_id bigint NOT NULL DEFAULT 0,
  buss_group_name character(50),
  buss_group_code character(25),
  buss_group_address text,
  buss_group_city character(25),
  buss_group_phone character(15),
  buss_group_fax character(15),
     */
    public static final int FLD_BUSS_GROUP_ID = 0;
    public static final int FLD_BUSS_GROUP_NAME = 1;
    public static final int FLD_BUSS_GROUP_CODE = 2;
    public static final int FLD_BUSS_GROUP_ADDRESS = 3;
    public static final int FLD_BUSS_GROUP_CITY = 4;
    public static final int FLD_BUSS_GROUP_PHONE = 5;
    public static final int FLD_BUSS_GROUP_FAX = 6;
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_STRING,       
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
    };
    
    public static String[] fieldNames = {
        "BUSS_GROUP_ID",
        "BUSS_GROUP_NAME",
        "BUSS_GROUP_CODE",
        "BUSS_GROUP_ADDRESS",
        "BUSS_GROUP_CITY",
        "BUSS_GROUP_PHONE",
        "BUSS_GROUP_FAX",
    };
    
     public PstBussGroup() {
    }

    public PstBussGroup(int i) throws DBException {
        super(new PstBussGroup());
    }


    public PstBussGroup(String sOid) throws DBException {
        super(new PstBussGroup(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }


    public PstBussGroup(long lOid) throws DBException {
        super(new PstBussGroup(0));
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
        return TBL_BUSS_GROUP;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstBussGroup().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        BussinessGroup bussinessGroup = PstBussGroup.fetchExc(ent.getOID());
        ent = (Entity) bussinessGroup;
        return bussinessGroup.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return PstBussGroup.insertExc((BussinessGroup) ent);
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

    public static BussinessGroup fetchExc(long oid) throws DBException {
        try {
            BussinessGroup objBussinessGroup = new BussinessGroup();
            PstBussGroup pBussGroup = new PstBussGroup(oid);

            objBussinessGroup.setOID(oid);
            objBussinessGroup.setBussGroupCode(pBussGroup.getString(FLD_BUSS_GROUP_CODE));
            objBussinessGroup.setBussGroupName(pBussGroup.getString(FLD_BUSS_GROUP_NAME));            
            objBussinessGroup.setBussGroupAddress(pBussGroup.getString(FLD_BUSS_GROUP_ADDRESS));
            objBussinessGroup.setBussGroupCity(pBussGroup.getString(FLD_BUSS_GROUP_CITY));
            objBussinessGroup.setBussGroupPhone(pBussGroup.getString(FLD_BUSS_GROUP_PHONE));
            objBussinessGroup.setBussGroupFax(pBussGroup.getString(FLD_BUSS_GROUP_FAX));
            
            return objBussinessGroup;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBussGroup(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(BussinessGroup objBussinessGroup) throws DBException {
        try {
            PstBussGroup pBussGroup = new PstBussGroup(0);

            pBussGroup.setString(FLD_BUSS_GROUP_CODE, objBussinessGroup.getBussGroupCode());
            pBussGroup.setString(FLD_BUSS_GROUP_NAME, objBussinessGroup.getBussGroupName());
            pBussGroup.setString(FLD_BUSS_GROUP_ADDRESS, objBussinessGroup.getBussGroupAddress());
            pBussGroup.setString(FLD_BUSS_GROUP_CITY, objBussinessGroup.getBussGroupCity());
            pBussGroup.setString(FLD_BUSS_GROUP_PHONE, objBussinessGroup.getBussGroupPhone());
            pBussGroup.setString(FLD_BUSS_GROUP_FAX, objBussinessGroup.getBussGroupFax());
            pBussGroup.insert();

            objBussinessGroup.setOID(pBussGroup.getlong(FLD_BUSS_GROUP_ID));
            
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBussGroup(0), DBException.UNKNOWN);
        }
        return objBussinessGroup.getOID();
    }

    public static long updateExc(BussinessGroup objBussinessGroup) throws DBException {
        try {
            if (objBussinessGroup.getOID() != 0) {
                PstBussGroup pBussGroup = new PstBussGroup(objBussinessGroup.getOID());
                
                pBussGroup.setString(FLD_BUSS_GROUP_CODE, objBussinessGroup.getBussGroupCode());
                pBussGroup.setString(FLD_BUSS_GROUP_NAME, objBussinessGroup.getBussGroupName());
                pBussGroup.setString(FLD_BUSS_GROUP_ADDRESS, objBussinessGroup.getBussGroupAddress());
                pBussGroup.setString(FLD_BUSS_GROUP_CITY, objBussinessGroup.getBussGroupCity());
                pBussGroup.setString(FLD_BUSS_GROUP_PHONE, objBussinessGroup.getBussGroupPhone());
                pBussGroup.setString(FLD_BUSS_GROUP_FAX, objBussinessGroup.getBussGroupFax());
                
                pBussGroup.update();

                return objBussinessGroup.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBussGroup(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstBussGroup pBussinessCenter = new PstBussGroup(oid);
            pBussinessCenter.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstBussGroup(0), DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_BUSS_GROUP;
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
                BussinessGroup objBussinessGroup = new BussinessGroup();
                resultToObject(rs, objBussinessGroup);
                lists.add(objBussinessGroup);
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

    private static void resultToObject(ResultSet rs, BussinessGroup objBussinessGroup) {
        try {
            objBussinessGroup.setOID(rs.getLong(fieldNames[FLD_BUSS_GROUP_ID]));
            objBussinessGroup.setBussGroupCode(rs.getString(fieldNames[FLD_BUSS_GROUP_CODE]));
            objBussinessGroup.setBussGroupName(rs.getString(fieldNames[FLD_BUSS_GROUP_NAME]));
            objBussinessGroup.setBussGroupAddress(rs.getString(fieldNames[FLD_BUSS_GROUP_ADDRESS]));
            objBussinessGroup.setBussGroupCity(rs.getString(fieldNames[FLD_BUSS_GROUP_CITY]));
            objBussinessGroup.setBussGroupPhone(rs.getString(fieldNames[FLD_BUSS_GROUP_PHONE]));
            objBussinessGroup.setBussGroupFax(rs.getString(fieldNames[FLD_BUSS_GROUP_FAX]));
            
        } catch (Exception e) {
            System.out.println("Exc when resultToObject : " + e.toString());
        }
    }

}
