/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.entity.purchasing;

import com.dimata.common.entity.location.PstLocation;
import java.sql.*;
import java.util.*;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import org.json.JSONObject;


/**
 *
 * @author dimata005
 */
public class PstDistributionPurchaseOrder extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language{
    public static final String TBL_DISTRIBUTION_PURCHASE_ORDER = "pos_purchase_order_distribution";

    public static final int FLD_PURCHASE_DISTRIBUTION_ORDER_ID = 0;
    public static final int FLD_PURCHASE_ORDER_ID = 1;
    public static final int FLD_LOCATION_ID = 2;
    public static final int FLD_QTY = 3;

    public static final String[] fieldNames =
            {
                "PURCHASE_DISTRIBUTION_ORDER_ID",
                "PURCHASE_ORDER_ID",
                "LOCATION_ID",
                "QTY"
            };

     public static final int[] fieldTypes =
            {
                TYPE_LONG + TYPE_PK + TYPE_ID,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_FLOAT
            };


   public PstDistributionPurchaseOrder() {
    }

    public PstDistributionPurchaseOrder(int i) throws DBException {
        super(new PstDistributionPurchaseOrder());
    }

    public PstDistributionPurchaseOrder(String sOid) throws DBException {
        super(new PstDistributionPurchaseOrder(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstDistributionPurchaseOrder(long lOid) throws DBException {
        super(new PstDistributionPurchaseOrder(0));
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
        return TBL_DISTRIBUTION_PURCHASE_ORDER;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPurchaseOrder().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        DistributionPurchaseOrder dpo = fetchExc(ent.getOID());
        ent = (Entity) dpo;
        return dpo.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((PurchaseOrder) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((PurchaseOrder) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

     public static DistributionPurchaseOrder fetchExc(long oid) throws DBException {
        try {
            DistributionPurchaseOrder dpo = new DistributionPurchaseOrder();
            PstDistributionPurchaseOrder pstdpo = new PstDistributionPurchaseOrder(oid);
            dpo.setOID(oid);

            dpo.setPurchaseOrderId(pstdpo.getlong(FLD_PURCHASE_ORDER_ID));
            dpo.setLocationId(pstdpo.getlong(FLD_LOCATION_ID));
            dpo.setQty(pstdpo.getInt(FLD_QTY));
            
            return dpo;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPurchaseOrder(0), DBException.UNKNOWN);
        }
    }

     public static long insertExc(DistributionPurchaseOrder dpo) throws DBException {
        try {
            PstDistributionPurchaseOrder pstdpo = new PstDistributionPurchaseOrder(0);

            pstdpo.setLong(FLD_PURCHASE_ORDER_ID, dpo.getPurchaseOrderId());
            pstdpo.setLong(FLD_LOCATION_ID, dpo.getLocationId());
            pstdpo.setDouble(FLD_QTY, dpo.getQty());

            pstdpo.insert();
            dpo.setOID(pstdpo.getlong(FLD_PURCHASE_DISTRIBUTION_ORDER_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPurchaseOrder(0), DBException.UNKNOWN);
        }
        return dpo.getOID();
    }

     public static long updateExc(DistributionPurchaseOrder dpo) throws DBException {
        try {
            if (dpo.getOID() != 0) {
                PstDistributionPurchaseOrder pstdpo = new PstDistributionPurchaseOrder(dpo.getOID());

                pstdpo.setLong(FLD_PURCHASE_ORDER_ID, dpo.getPurchaseOrderId());
                pstdpo.setLong(FLD_LOCATION_ID, dpo.getLocationId());
                pstdpo.setDouble(FLD_QTY, dpo.getQty());

                pstdpo.update();
                return dpo.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPurchaseOrder(0), DBException.UNKNOWN);
        }
        return 0;
    }

   public static long deleteExc(long oid) throws DBException {
        try {
           PstDistributionPurchaseOrder pstdpo = new PstDistributionPurchaseOrder(oid);
           pstdpo.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPurchaseOrder(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_DISTRIBUTION_PURCHASE_ORDER;
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
                DistributionPurchaseOrder dpo = new DistributionPurchaseOrder();
                resultToObject(rs, dpo);
                lists.add(dpo);
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

    public static void resultToObject(ResultSet rs, DistributionPurchaseOrder dpo) {
        try {
            dpo.setOID(rs.getLong(PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_PURCHASE_DISTRIBUTION_ORDER_ID]));
            dpo.setPurchaseOrderId(rs.getLong(PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_PURCHASE_ORDER_ID]));
            dpo.setLocationId(rs.getLong(PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_LOCATION_ID]));
            dpo.setQty(rs.getDouble(PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_QTY]));

        } catch (Exception e) {
        }
    }

    public static void resultToObjectWithNameLocation(ResultSet rs, DistributionPurchaseOrder dpo) {
        try {
            dpo.setOID(rs.getLong(PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_PURCHASE_DISTRIBUTION_ORDER_ID]));
            dpo.setPurchaseOrderId(rs.getLong(PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_PURCHASE_ORDER_ID]));
            dpo.setLocationId(rs.getLong(PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_LOCATION_ID]));
            dpo.setQty(rs.getDouble(PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_QTY]));
            dpo.setLocationName(rs.getString(PstLocation.fieldNames[PstLocation.FLD_NAME]));

        } catch (Exception e) {
        }
    }

   public static Vector getListWithLocationName(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT dpo.*, lc."+PstLocation.fieldNames[PstLocation.FLD_NAME]+" FROM " + TBL_DISTRIBUTION_PURCHASE_ORDER + " AS dpo"+
                         " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS lc ON "+
                         " dpo."+fieldNames[PstDistributionPurchaseOrder.FLD_LOCATION_ID]+"= lc."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];
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
                DistributionPurchaseOrder dpo = new DistributionPurchaseOrder();
                resultToObjectWithNameLocation(rs, dpo);
                lists.add(dpo);
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

    public static DistributionPurchaseOrder getDistWithLocation(long locationId, long purchaseOrderID){
        DistributionPurchaseOrder dpo = new DistributionPurchaseOrder();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_DISTRIBUTION_PURCHASE_ORDER;
            sql = sql + " WHERE " + PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_LOCATION_ID]+"='"+locationId+"'"+
                        " AND " + PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_PURCHASE_ORDER_ID]+"='"+purchaseOrderID+"'";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                dpo.setOID(rs.getLong(PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_PURCHASE_DISTRIBUTION_ORDER_ID]));
                dpo.setPurchaseOrderId(rs.getLong(PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_PURCHASE_ORDER_ID]));
                dpo.setLocationId(rs.getLong(PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_LOCATION_ID]));
                dpo.setQty(rs.getDouble(PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_QTY]));
            }
            rs.close();
            return dpo;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new DistributionPurchaseOrder();
    }


   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         DistributionPurchaseOrder distributionPurchaseOrder = PstDistributionPurchaseOrder.fetchExc(oid);
         object.put(PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_PURCHASE_DISTRIBUTION_ORDER_ID], distributionPurchaseOrder.getOID());
         object.put(PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_PURCHASE_ORDER_ID], distributionPurchaseOrder.getPurchaseOrderId());
         object.put(PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_LOCATION_ID], distributionPurchaseOrder.getLocationId());
         object.put(PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_QTY], distributionPurchaseOrder.getQty());
      }catch(Exception exc){}
      return object;
   }
}
