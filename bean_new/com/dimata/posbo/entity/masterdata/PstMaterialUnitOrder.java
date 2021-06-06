/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author dimata005
 */
/* package java */
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

public class PstMaterialUnitOrder extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    //public static final  String TBL_MAT_UNIT_ORDER = "POS_MaterialUnitOrder";
    public static final String TBL_MAT_UNIT_ORDER = "pos_material_unit_order";
    public static final int FLD_MATERIAL_UNIT_ORDER_ID = 0;
    public static final int FLD_MATERIAL_ID = 1;
    public static final int FLD_UNIT_ID = 2;
    //adding status ditampilkan atau tidak by mirahu 20120511
    public static final int FLD_MINIMUM_QTY_ORDER = 3;
    public static final String[] fieldNames = {
        "MATERIAL_UNIT_BUY_ID",
        "MATERIAL_ID",
        "UNIT_ID",
    //adding status ditampilkan atau tidak by mirahu 20120511
        "MINIMUM_QTY_ORDER"
        
            
    };
    
     //adding status ditampilkan atau tidak by mirahu 20120511
        public static int USE_NO_SHOW = 0 ;
        public static int USE_SHOW = 1;
    
    public static final int[] fieldTypes = {
         TYPE_LONG + TYPE_PK + TYPE_ID, TYPE_LONG, TYPE_LONG, TYPE_FLOAT
    };

    public PstMaterialUnitOrder() {
    }

    public PstMaterialUnitOrder(int i) throws DBException {
        super(new PstMaterialUnitOrder());
    }

    public PstMaterialUnitOrder(String sOid) throws DBException {
        super(new PstMaterialUnitOrder(0));
        try {
            if (!locate(sOid)) {
                throw new DBException(this, DBException.RECORD_NOT_FOUND);
            } else {
                return;
            }
        } catch (Exception e) {
        }
    }

    public PstMaterialUnitOrder(long lOid) throws DBException {
        super(new PstMaterialUnitOrder(0));
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
        return TBL_MAT_UNIT_ORDER;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMaterialUnitOrder().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        MaterialUnitOrder matMaterialUnitOrder = fetchExc(ent.getOID());
        ent = (Entity) matMaterialUnitOrder;
        return matMaterialUnitOrder.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((MaterialUnitOrder) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((MaterialUnitOrder) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static MaterialUnitOrder fetchExc(long oid) throws DBException {
        try {
            MaterialUnitOrder matMaterialUnitOrder = new MaterialUnitOrder();
            PstMaterialUnitOrder PstMaterialUnitOrder = new PstMaterialUnitOrder(oid);
            matMaterialUnitOrder.setOID(oid);

            matMaterialUnitOrder.setMaterialID(PstMaterialUnitOrder.getlong(FLD_MATERIAL_ID));
            matMaterialUnitOrder.setUnitID(PstMaterialUnitOrder.getlong(FLD_UNIT_ID));
            //adding status ditampilkan atau tidak by mirahu 20120511
            matMaterialUnitOrder.setMinimumQtyOrder(PstMaterialUnitOrder.getdouble(FLD_MINIMUM_QTY_ORDER));

            return matMaterialUnitOrder;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialUnitOrder(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(MaterialUnitOrder matMaterialUnitOrder) throws DBException {
        try {
            PstMaterialUnitOrder PstMaterialUnitOrder = new PstMaterialUnitOrder(0);

            PstMaterialUnitOrder.setLong(FLD_MATERIAL_ID, matMaterialUnitOrder.getMaterialID());
            PstMaterialUnitOrder.setLong(FLD_UNIT_ID, matMaterialUnitOrder.getUnitID());
            //adding status ditampilkan atau tidak by mirahu 20120511
            PstMaterialUnitOrder.setDouble(FLD_MINIMUM_QTY_ORDER, matMaterialUnitOrder.getMinimumQtyOrder());

            PstMaterialUnitOrder.insert();

            long oidDataSync=PstDataSyncSql.insertExc(PstMaterialUnitOrder.getInsertSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
            matMaterialUnitOrder.setOID(PstMaterialUnitOrder.getlong(FLD_MATERIAL_UNIT_ORDER_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialUnitOrder(0), DBException.UNKNOWN);
        }
        return matMaterialUnitOrder.getOID();
    }

    public static long updateExc(MaterialUnitOrder matMaterialUnitOrder) throws DBException {
        try {
            if (matMaterialUnitOrder.getOID() != 0) {
                PstMaterialUnitOrder PstMaterialUnitOrder = new PstMaterialUnitOrder(matMaterialUnitOrder.getOID());

                PstMaterialUnitOrder.setLong(FLD_MATERIAL_ID, matMaterialUnitOrder.getMaterialID());
                PstMaterialUnitOrder.setLong(FLD_UNIT_ID, matMaterialUnitOrder.getUnitID());
                //adding status ditampilkan atau tidak by mirahu 20120511
                PstMaterialUnitOrder.setDouble(FLD_MINIMUM_QTY_ORDER, matMaterialUnitOrder.getMinimumQtyOrder());

                PstMaterialUnitOrder.update();

                long oidDataSync=PstDataSyncSql.insertExc(PstMaterialUnitOrder.getUpdateSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
                return matMaterialUnitOrder.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialUnitOrder(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstMaterialUnitOrder PstMaterialUnitOrder = new PstMaterialUnitOrder(oid);
            PstMaterialUnitOrder.delete();

            long oidDataSync = PstDataSyncSql.insertExc(PstMaterialUnitOrder.getDeleteSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialUnitOrder(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_UNIT_ORDER;
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
                MaterialUnitOrder matMaterialUnitOrder = new MaterialUnitOrder();
                resultToObject(rs, matMaterialUnitOrder);
                lists.add(matMaterialUnitOrder);
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

    public static void resultToObject(ResultSet rs, MaterialUnitOrder matMaterialUnitOrder) {
        try {
            matMaterialUnitOrder.setOID(rs.getLong(PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_MATERIAL_UNIT_ORDER_ID]));
            matMaterialUnitOrder.setMaterialID(rs.getLong(PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_MATERIAL_ID]));
            matMaterialUnitOrder.setUnitID(rs.getLong(PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_UNIT_ID]));
            //adding status ditampilkan atau tidak by mirahu 20120511
            matMaterialUnitOrder.setMinimumQtyOrder(rs.getDouble(PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_MINIMUM_QTY_ORDER]));

        } catch (Exception e) {
        }
    }
    
    
    public static void resultToObjectJoin(ResultSet rs, MaterialUnitOrder matMaterialUnitOrder) {
        try {
            matMaterialUnitOrder.setOID(rs.getLong(PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_MATERIAL_UNIT_ORDER_ID]));
            matMaterialUnitOrder.setMaterialID(rs.getLong(PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_MATERIAL_ID]));
            matMaterialUnitOrder.setUnitID(rs.getLong(PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_UNIT_ID]));
            //adding status ditampilkan atau tidak by mirahu 20120511
            matMaterialUnitOrder.setMinimumQtyOrder(rs.getDouble(PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_MINIMUM_QTY_ORDER]));
            matMaterialUnitOrder.setUnitKode(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long MaterialUnitOrderId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_UNIT_ORDER + " WHERE " +
                    PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_MATERIAL_UNIT_ORDER_ID] + " = " + MaterialUnitOrderId;

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
            String sql = "SELECT COUNT(" + PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_MATERIAL_UNIT_ORDER_ID] + ") FROM " + TBL_MAT_UNIT_ORDER;
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
                    MaterialUnitOrder matMaterialUnitOrder = (MaterialUnitOrder) list.get(ls);
                    if (oid == matMaterialUnitOrder.getOID()) {
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
    
    
    
    public static Vector listJoin(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            /**
             * SELECT * FROM pos_material_unit_order AS PMU
INNER JOIN pos_unit AS PU
ON PMU.UNIT_ID=PU.UNIT_ID
             */
            String sql = " SELECT PMU.*, PU.CODE FROM " + TBL_MAT_UNIT_ORDER+" AS PMU "+
                         " INNER JOIN pos_unit AS PU "+
                         " ON PMU.UNIT_ID=PU.UNIT_ID ";
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
                MaterialUnitOrder matMaterialUnitOrder = new MaterialUnitOrder();
                resultToObjectJoin(rs, matMaterialUnitOrder);
                lists.add(matMaterialUnitOrder);
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
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                MaterialUnitOrder materialUnitOrder = PstMaterialUnitOrder.fetchExc(oid);
                object.put(PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_MATERIAL_UNIT_ORDER_ID], materialUnitOrder.getOID());
                object.put(PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_MATERIAL_ID], materialUnitOrder.getMaterialID());
                object.put(PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_MINIMUM_QTY_ORDER], materialUnitOrder.getMinimumQtyOrder());
                object.put(PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_UNIT_ID], materialUnitOrder.getUnitID());
            }catch(Exception exc){}

            return object;
        }
}
