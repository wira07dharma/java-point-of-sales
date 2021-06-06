/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author dimata005
 */
public class PstPriceProtection extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    public static final String TBL_POS_PRICE_PROTECTION = "pos_price_protection";

    public static final int FLD_POS_PRICE_PROTECTION_ID = 0;
    public static final int FLD_NOMOR_PRICE_PROTECTION = 1;
    public static final int FLD_COUNTER = 2;
    public static final int FLD_LOCATION_ID = 3;
    public static final int FLD_CREATE_DATE=4;
    public static final int FLD_APPROVAL_DATE=5;
    public static final int FLD_TOTAL_AMOUNT=6;
    public static final int FLD_EXCHANGE_RATE=7;
    public static final int FLD_STATUS=8;
    public static final int FLD_REMARK=9;
    
    public static final String[] fieldNames = {
        "POS_PRICE_PROTECTION_ID",
        "NOMOR_PRICE_PROTECTION",
        "COUNTER",
        "LOCATION_ID",
        "CREATE_DATE",
        "APPROVAL_DATE",
        "TOTAL_AMOUNT",
        "EXCHANGE_RATE",
        "STATUS",
        "REMARK"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,//0
        TYPE_STRING, //1
        TYPE_INT, //2
        TYPE_LONG, //3
        TYPE_DATE, //4
        TYPE_DATE, //5
        TYPE_FLOAT, //6
        TYPE_FLOAT, //7
        TYPE_INT, //8
        TYPE_STRING //9
    };


    public PstPriceProtection() {
    }

    public PstPriceProtection(int i) throws DBException {
        super(new PstPriceProtection());
    }

    public PstPriceProtection(String sOid) throws DBException {
        super(new PstPriceProtection(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstPriceProtection(long lOid) throws DBException {
        super(new PstPriceProtection(0));
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
        return TBL_POS_PRICE_PROTECTION;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPriceProtection().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        PriceProtection materialStockCode = fetchExc(ent.getOID());
        ent = (Entity) materialStockCode;
        return materialStockCode.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((PriceProtection) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((PriceProtection) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static PriceProtection fetchExc(long oid) throws DBException {
        try {
            PriceProtection priceProtection = new PriceProtection();
            PstPriceProtection pstPriceProtection = new PstPriceProtection(oid);
            priceProtection.setOID(oid);

            
            priceProtection.setTotalAmount(pstPriceProtection.getdouble(FLD_TOTAL_AMOUNT));
            priceProtection.setExchangeRate(pstPriceProtection.getdouble(FLD_EXCHANGE_RATE));
            priceProtection.setStatus(pstPriceProtection.getInt(FLD_STATUS));
            priceProtection.setLocationId(pstPriceProtection.getlong(FLD_LOCATION_ID));
            priceProtection.setNumberPP(pstPriceProtection.getString(FLD_NOMOR_PRICE_PROTECTION));
            priceProtection.setDateCreated(pstPriceProtection.getDate(FLD_CREATE_DATE));
            priceProtection.setDateApproved(pstPriceProtection.getDate(FLD_APPROVAL_DATE));
            priceProtection.setPpCounter(pstPriceProtection.getInt(FLD_COUNTER));
            priceProtection.setRemark(pstPriceProtection.getString(FLD_REMARK));
            
            
            return priceProtection;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceProtection(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(PriceProtection priceProtection) throws DBException {
        try {
            PstPriceProtection pstPriceProtection = new PstPriceProtection(0);

            pstPriceProtection.setDouble(FLD_TOTAL_AMOUNT, priceProtection.getTotalAmount());
            pstPriceProtection.setDouble(FLD_EXCHANGE_RATE, priceProtection.getExchangeRate());
            pstPriceProtection.setInt(FLD_STATUS, priceProtection.getStatus());
            pstPriceProtection.setLong(FLD_LOCATION_ID, priceProtection.getLocationId());
            pstPriceProtection.setString(FLD_NOMOR_PRICE_PROTECTION, priceProtection.getNumberPP());        
            pstPriceProtection.setDate(FLD_CREATE_DATE, priceProtection.getDateCreated());        
            pstPriceProtection.setDate(FLD_APPROVAL_DATE, priceProtection.getDateApproved());        
            pstPriceProtection.setInt(FLD_COUNTER, priceProtection.getPpCounter());
            pstPriceProtection.setString(FLD_REMARK, priceProtection.getRemark());
            
            pstPriceProtection.insert();
            //kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstPriceProtection.getInsertSQL());
            priceProtection.setOID(pstPriceProtection.getlong(FLD_POS_PRICE_PROTECTION_ID));
            
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceProtection(0), DBException.UNKNOWN);
        }
        return priceProtection.getOID();
    }

    public static long updateExc(PriceProtection priceProtection) throws DBException {
        try {
            if (priceProtection.getOID() != 0) {
                PstPriceProtection pstPriceProtection = new PstPriceProtection(priceProtection.getOID());

                //pstPriceProtection.setLong(FLD_SUPPLIER_ID, priceProtection.getSupplierId());
                pstPriceProtection.setDouble(FLD_TOTAL_AMOUNT, priceProtection.getTotalAmount());
                pstPriceProtection.setDouble(FLD_EXCHANGE_RATE, priceProtection.getExchangeRate());
                pstPriceProtection.setInt(FLD_STATUS, priceProtection.getStatus());
                pstPriceProtection.setLong(FLD_LOCATION_ID, priceProtection.getLocationId());
                pstPriceProtection.setString(FLD_NOMOR_PRICE_PROTECTION, priceProtection.getNumberPP());        
                pstPriceProtection.setDate(FLD_CREATE_DATE, priceProtection.getDateCreated());        
                pstPriceProtection.setDate(FLD_APPROVAL_DATE, priceProtection.getDateApproved());
                pstPriceProtection.setInt(FLD_COUNTER, priceProtection.getPpCounter());
                pstPriceProtection.setString(FLD_REMARK, priceProtection.getRemark());
                
                pstPriceProtection.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstPriceProtection.getUpdateSQL());
                return priceProtection.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceProtection(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstPriceProtection pstMaterialStockCode = new PstPriceProtection(oid);
            pstMaterialStockCode.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMaterialStockCode.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceProtection(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_PRICE_PROTECTION;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            if (limitStart == 0 && recordToGet == 0)
                sql = sql + "";
            else
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                PriceProtection materialStockCode = new PriceProtection();
                resultToObject(rs, materialStockCode);
                lists.add(materialStockCode);
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

    public static Vector list(String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_PRICE_PROTECTION;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                PriceProtection materialStockCode = new PriceProtection();
                resultToObject(rs, materialStockCode);
                lists.add(materialStockCode);
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
     * bobo
     * @param whereClause
     * @param order
     * @return
     */
    public static int deleteStockCode(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "DELETE FROM " + TBL_POS_PRICE_PROTECTION;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            return 1;
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }


    private static void resultToObject(ResultSet rs, PriceProtection priceProtection) {
        try {
            
            priceProtection.setOID(rs.getLong(PstPriceProtection.fieldNames[PstPriceProtection.FLD_POS_PRICE_PROTECTION_ID]));
            priceProtection.setTotalAmount(rs.getDouble(PstPriceProtection.fieldNames[PstPriceProtection.FLD_TOTAL_AMOUNT]));
            priceProtection.setStatus(rs.getInt(PstPriceProtection.fieldNames[PstPriceProtection.FLD_STATUS]));
            priceProtection.setExchangeRate(rs.getDouble(PstPriceProtection.fieldNames[PstPriceProtection.FLD_EXCHANGE_RATE]));
            //
            priceProtection.setLocationId(rs.getLong(PstPriceProtection.fieldNames[PstPriceProtection.FLD_LOCATION_ID]));
            priceProtection.setNumberPP(rs.getString(PstPriceProtection.fieldNames[PstPriceProtection.FLD_NOMOR_PRICE_PROTECTION]));
            priceProtection.setDateCreated(rs.getDate(PstPriceProtection.fieldNames[PstPriceProtection.FLD_CREATE_DATE]));
            priceProtection.setDateApproved(rs.getDate(PstPriceProtection.fieldNames[PstPriceProtection.FLD_APPROVAL_DATE]));
            priceProtection.setPpCounter(rs.getInt(PstPriceProtection.fieldNames[PstPriceProtection.FLD_COUNTER]));
            priceProtection.setRemark(rs.getString(PstPriceProtection.fieldNames[PstPriceProtection.FLD_REMARK]));
            
            
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long discountTypeId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_PRICE_PROTECTION + " WHERE " +
                    PstPriceProtection.fieldNames[PstPriceProtection.FLD_POS_PRICE_PROTECTION_ID] + " = " + discountTypeId;

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
            String sql = "SELECT COUNT(" + PstPriceProtection.fieldNames[PstPriceProtection.FLD_POS_PRICE_PROTECTION_ID] + ") FROM " + TBL_POS_PRICE_PROTECTION;
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
                    PriceProtection materialStockCode = (PriceProtection) list.get(ls);
                    if (oid == materialStockCode.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }

    
    public static int updateTotAmount(long ppId, double totAmount) {
        DBResultSet dbrs = null;
        try
            {
                String sql = "UPDATE " + TBL_POS_PRICE_PROTECTION +
                    " SET " + fieldNames[FLD_TOTAL_AMOUNT] +
                    " = '" + totAmount +"'"+
                    " WHERE " + fieldNames[FLD_POS_PRICE_PROTECTION_ID] +
                    " = '" + ppId+"'";
                DBHandler.execUpdate(sql);
            return 0;
        } catch (Exception e) {
            System.out.println("");
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    
//    public static int requestPayment(HttpServletRequest req, SrcPriceProtection srcPriceProtection){
//      String where=" DATE_CREATED BETWEEN '" + Formater.formatDate(srcPriceProtection.getDateFrom(), "yyyy-MM-dd") + " 00:00:01'" +
//             " AND '" + Formater.formatDate(srcPriceProtection.getDateTo(), "yyyy-MM-dd") + " 23:59:29'"+
//             " AND LOCATION_ID='"+srcPriceProtection.getLocationId()+"'";
//
//        if(srcPriceProtection.getSupplierId()!=0){
//            where=where+" AND SUPPLIER_ID='"+srcPriceProtection.getSupplierId()+"'";
//        }
//      Vector records = PstPriceProtection.list(0, 0, where, "");
//      if(records!=null && records.size()>0){
//                for(int i=0; i<records.size(); i++){
//                      PriceProtection priceProtection = (PriceProtection) records.get(i);
//                     if(FRMQueryString.requestInt(req, "invoice_"+priceProtection.getOID())==1){
//                         Date dateUpdate = FRMQueryString.requestDate(req, "date_update");
//                         int x = updateStatusPP(priceProtection.getOID(),I_DocStatus.DOCUMENT_STATUS_FINAL,dateUpdate);
//                     }
//                }
//      }
//      return 0;
//     } 
    
    
    
   
    
    
   
    
    
    public static int updateTotPaid(long ppId, double totAmount) {
        DBResultSet dbrs = null;
        try
            {
//                String sql = "UPDATE " + TBL_POS_PRICE_PROTECTION +
//                    " SET " + fieldNames[FLD_PAID] +
//                    " = ("+ fieldNames[FLD_PAID]+"+"+ totAmount +")"+
//                    " WHERE " + fieldNames[FLD_POS_PRICE_PROTECTION_ID] +
//                    " = '" + ppId+"'";
//                DBHandler.execUpdate(sql);
            return 0;
        } catch (Exception e) {
            System.out.println("");
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    
    public static double getPaidPP(String number) {
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT SUM(papd.AMOUNT) FROM pos_acc_payable_detail AS papd"+
                            " INNER JOIN payment_info AS pin "+
                            " ON pin.purch_payment_id=papd.ACC_PAYABLE_DETAIL_ID "+
                            " INNER JOIN pos_price_protection AS ppp "+
                            " ON ppp.NUMBER_PP=pin.card_number "+
                            " WHERE ppp.NUMBER_PP='"+number+"'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            double count = 0.0;
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
    
    
}
