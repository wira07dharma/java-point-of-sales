/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.warehouse;




import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Vector;
import org.json.JSONObject;
/**
 *
 * @author dimata005
 */

public class PstPriceProtectionDistribution extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    public static final String TBL_POS_PRICE_PROTECTION_DISTRIBUTION = "pos_price_protection_distribution";

    public static final int FLD_POS_PRICE_PROTECTION_DISTRIBUTION_ID = 0;
    public static final int FLD_POS_PRICE_PROTECTION_ID = 1;
    public static final int FLD_SUPPLIER_ID = 2;
    public static final int FLD_AMOUNT_ISSUE = 3;
    public static final int FLD_EXCHANGE_RATE = 4;
    public static final int FLD_STATUS = 5;

    public static final String[] fieldNames = {
        "POS_PRICE_PROTECTION_DISTRIBUTION_ID",
        "POS_PRICE_PROTECTION_ID",
        "SUPPLIER_ID",
        "AMOUNT_ISSUE",
        "EXCHANGE_RATE",
        "STATUS"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT
    };


    public PstPriceProtectionDistribution() {
    }

    public PstPriceProtectionDistribution(int i) throws DBException {
        super(new PstPriceProtectionDistribution());
    }

    public PstPriceProtectionDistribution(String sOid) throws DBException {
        super(new PstPriceProtectionDistribution(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstPriceProtectionDistribution(long lOid) throws DBException {
        super(new PstPriceProtectionDistribution(0));
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
        return TBL_POS_PRICE_PROTECTION_DISTRIBUTION;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPriceProtectionDistribution().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        PriceProtectionDistribution priceProtectionDistribution = fetchExc(ent.getOID());
        ent = (Entity) priceProtectionDistribution;
        return priceProtectionDistribution.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((PriceProtectionDistribution) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((PriceProtectionDistribution) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static PriceProtectionDistribution fetchExc(long oid) throws DBException {
        try {
            PriceProtectionDistribution priceProtectionDistribution = new PriceProtectionDistribution();
            PstPriceProtectionDistribution pstPriceProtectionDistribution = new PstPriceProtectionDistribution(oid);
            
            priceProtectionDistribution.setOID(oid);
            priceProtectionDistribution.setPriceProtectionId(pstPriceProtectionDistribution.getlong(FLD_POS_PRICE_PROTECTION_ID));
            priceProtectionDistribution.setSupplierId(pstPriceProtectionDistribution.getlong(FLD_SUPPLIER_ID));
            priceProtectionDistribution.setAmountIssue(pstPriceProtectionDistribution.getdouble(FLD_AMOUNT_ISSUE));
            priceProtectionDistribution.setExchangeRate(pstPriceProtectionDistribution.getdouble(FLD_EXCHANGE_RATE));
            priceProtectionDistribution.setStatus(pstPriceProtectionDistribution.getInt(FLD_STATUS));
            return priceProtectionDistribution;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceProtectionDistribution(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(PriceProtectionDistribution priceProtectionDistribution) throws DBException {
        try {
            PstPriceProtectionDistribution pstPriceProtectionDistribution = new PstPriceProtectionDistribution(0);

            pstPriceProtectionDistribution.setLong(FLD_POS_PRICE_PROTECTION_ID, priceProtectionDistribution.getPriceProtectionId());
            pstPriceProtectionDistribution.setLong(FLD_SUPPLIER_ID, priceProtectionDistribution.getSupplierId());
            pstPriceProtectionDistribution.setDouble(FLD_AMOUNT_ISSUE, priceProtectionDistribution.getAmountIssue());
            pstPriceProtectionDistribution.setDouble(FLD_EXCHANGE_RATE, priceProtectionDistribution.getExchangeRate());
           // pstPriceProtectionDistribution.setDouble(FLD_TOTAL_AMOUNT, priceProtectionDistribution.getTotalAmount());
            pstPriceProtectionDistribution.setInt(FLD_STATUS, priceProtectionDistribution.getStatus());
            
            pstPriceProtectionDistribution.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstPriceProtectionDistribution.getInsertSQL());
            priceProtectionDistribution.setOID(pstPriceProtectionDistribution.getlong(FLD_POS_PRICE_PROTECTION_DISTRIBUTION_ID));
            
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceProtectionDistribution(0), DBException.UNKNOWN);
        }
        return priceProtectionDistribution.getOID();
    }

    public static long updateExc(PriceProtectionDistribution priceProtectionDistribution) throws DBException {
        try {
            if (priceProtectionDistribution.getOID() != 0) {
                PstPriceProtectionDistribution pstPriceProtectionDistribution = new PstPriceProtectionDistribution(priceProtectionDistribution.getOID());

                pstPriceProtectionDistribution.setLong(FLD_POS_PRICE_PROTECTION_ID, priceProtectionDistribution.getPriceProtectionId());
                pstPriceProtectionDistribution.setLong(FLD_SUPPLIER_ID, priceProtectionDistribution.getSupplierId());
                pstPriceProtectionDistribution.setDouble(FLD_AMOUNT_ISSUE, priceProtectionDistribution.getAmountIssue());
                pstPriceProtectionDistribution.setDouble(FLD_EXCHANGE_RATE, priceProtectionDistribution.getExchangeRate());
                //pstPriceProtectionDistribution.setDouble(FLD_TOTAL_AMOUNT, priceProtectionDistribution.getTotalAmount());
                pstPriceProtectionDistribution.setInt(FLD_STATUS, priceProtectionDistribution.getStatus());
                
                pstPriceProtectionDistribution.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstPriceProtectionDistribution.getUpdateSQL());
                return priceProtectionDistribution.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceProtectionDistribution(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstPriceProtectionDistribution pstPriceProtectionDistribution = new PstPriceProtectionDistribution(oid);
            pstPriceProtectionDistribution.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstPriceProtectionDistribution.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceProtectionDistribution(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_PRICE_PROTECTION_DISTRIBUTION;
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
                PriceProtectionDistribution priceProtectionDistribution = new PriceProtectionDistribution();
                resultToObject(rs, priceProtectionDistribution);
                lists.add(priceProtectionDistribution);
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
    
    
    
    public static Vector listInnerJoinSupplier(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
           // String sql = "SELECT * FROM " + TBL_POS_PRICE_PROTECTION_ITEM + " AS PP";
            
           String sql = " SELECT PPPI.*,CL.CONTACT_ID, CL.CONTACT_CODE, CL.COMP_NAME FROM pos_price_protection_distribution PPPI"+
                        " INNER JOIN contact_list AS CL"+
                        " ON CL.CONTACT_ID=PPPI.SUPPLIER_ID";

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
                Vector vct = new Vector();
                PriceProtectionDistribution priceProtectionDistribution = new PriceProtectionDistribution();
                ContactList contactList = new ContactList();
                
                priceProtectionDistribution.setOID(rs.getLong(PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_POS_PRICE_PROTECTION_DISTRIBUTION_ID]));
                priceProtectionDistribution.setPriceProtectionId(rs.getLong(PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_POS_PRICE_PROTECTION_ID]));
                priceProtectionDistribution.setSupplierId(rs.getLong(PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_SUPPLIER_ID])); 
                priceProtectionDistribution.setAmountIssue(rs.getDouble(PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_AMOUNT_ISSUE]));  
                priceProtectionDistribution.setExchangeRate(rs.getDouble(PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_EXCHANGE_RATE]));  
                priceProtectionDistribution.setStatus(rs.getInt(PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_STATUS]));
                
                contactList.setOID(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
                contactList.setContactCode(rs.getString(PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]));
                contactList.setCompName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));
                
                vct.add(priceProtectionDistribution);
                vct.add(contactList);
                lists.add(vct);
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
            String sql = "SELECT * FROM " + TBL_POS_PRICE_PROTECTION_DISTRIBUTION;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                PriceProtectionDistribution priceProtectionDistribution = new PriceProtectionDistribution();
                resultToObject(rs, priceProtectionDistribution);
                lists.add(priceProtectionDistribution);
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
            String sql = "DELETE FROM " + TBL_POS_PRICE_PROTECTION_DISTRIBUTION;
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


    private static void resultToObject(ResultSet rs, PriceProtectionDistribution priceProtectionDistribution) {
        try {
            
            priceProtectionDistribution.setOID(rs.getLong(PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_POS_PRICE_PROTECTION_DISTRIBUTION_ID]));
            priceProtectionDistribution.setPriceProtectionId(rs.getLong(PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_POS_PRICE_PROTECTION_ID]));
            priceProtectionDistribution.setSupplierId(rs.getLong(PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_SUPPLIER_ID]));
            priceProtectionDistribution.setAmountIssue(rs.getDouble(PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_AMOUNT_ISSUE]));
            priceProtectionDistribution.setExchangeRate(rs.getDouble(PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_EXCHANGE_RATE]));
            priceProtectionDistribution.setStatus(rs.getInt(PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_STATUS]));
            
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long discountTypeId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_PRICE_PROTECTION_DISTRIBUTION + " WHERE " +
                    PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_POS_PRICE_PROTECTION_ID] + " = " + discountTypeId;

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
            String sql = "SELECT COUNT(" + PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_POS_PRICE_PROTECTION_ID] + ") FROM " + TBL_POS_PRICE_PROTECTION_DISTRIBUTION;
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
    
    
     public static double getSum(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(" + PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_AMOUNT_ISSUE] + ") FROM " + TBL_POS_PRICE_PROTECTION_DISTRIBUTION;
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
                    PriceProtectionDistribution priceProtectionDistribution = (PriceProtectionDistribution) list.get(ls);
                    if (oid == priceProtectionDistribution.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }

    
     public static int updateStatusPaymentPP(long ppId, int status) {
        DBResultSet dbrs = null;
        try
            {
                String sql = "UPDATE " + TBL_POS_PRICE_PROTECTION_DISTRIBUTION+
                    " SET " + fieldNames[FLD_STATUS] +
                    " = '" + status +"'"+
                    " WHERE " + fieldNames[FLD_POS_PRICE_PROTECTION_DISTRIBUTION_ID] +
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
 

   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         PriceProtectionDistribution priceProtectionDistribution = PstPriceProtectionDistribution.fetchExc(oid);
         object.put(PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_POS_PRICE_PROTECTION_DISTRIBUTION_ID], priceProtectionDistribution.getOID());
         object.put(PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_POS_PRICE_PROTECTION_ID], priceProtectionDistribution.getPriceProtectionId());
         object.put(PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_SUPPLIER_ID], priceProtectionDistribution.getSupplierId());
         object.put(PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_AMOUNT_ISSUE], priceProtectionDistribution.getAmountIssue());
         object.put(PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_EXCHANGE_RATE], priceProtectionDistribution.getExchangeRate());
         object.put(PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_STATUS], priceProtectionDistribution.getStatus());
      }catch(Exception exc){}
      return object;
   }   
    
    
}