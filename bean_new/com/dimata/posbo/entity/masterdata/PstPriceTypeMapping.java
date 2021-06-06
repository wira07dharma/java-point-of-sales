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
import java.io.*
        ;
import java.sql.*
        ;
import java.util.*
        ;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;

/* package posbo */
//import com.dimata.posbo.db.DBHandler;
//import com.dimata.posbo.db.DBException;
//import com.dimata.posbo.db.DBLogger;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PstStandartRate;
import com.dimata.common.entity.payment.StandartRate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.json.JSONObject;

public class PstPriceTypeMapping extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    //public static final  String TBL_POS_PRICE_TYPE_MAPPING = "POS_PRICE_TYPE_MAPPING";
    public static final String TBL_POS_PRICE_TYPE_MAPPING = "pos_price_type_mapping";

    public static final int FLD_PRICE_TYPE_ID = 0;
    public static final int FLD_MATERIAL_ID = 1;
    public static final int FLD_STANDART_RATE_ID = 2;
    public static final int FLD_PRICE = 3;

    public static final String[] fieldNames = {
        "PRICE_TYPE_ID",
        "MATERIAL_ID",
        "STANDART_RATE_ID",
        "PRICE"
    };

    public static final int[] fieldTypes = {
        TYPE_PK + TYPE_FK + TYPE_LONG,
        TYPE_PK + TYPE_FK + TYPE_LONG,
        TYPE_PK + TYPE_FK + TYPE_LONG,
        TYPE_FLOAT
    };

    public PstPriceTypeMapping() {
    }

    public PstPriceTypeMapping(int i) throws DBException {
        super(new PstPriceTypeMapping());
    }

    public PstPriceTypeMapping(String sOid) throws DBException {
        super(new PstPriceTypeMapping(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstPriceTypeMapping(long priceTypeId, long materialId, long standartRateId) throws DBException {
        super(new PstPriceTypeMapping(0));
        long[] arrId = {priceTypeId, materialId, standartRateId};
        if (!locate(arrId))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_POS_PRICE_TYPE_MAPPING;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPriceTypeMapping().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        PriceTypeMapping pricetypemapping = fetchExc(ent.getOID(0), ent.getOID(1), ent.getOID(2));
        ent = (Entity) pricetypemapping;
        return pricetypemapping.getOID(0);
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((PriceTypeMapping) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((PriceTypeMapping) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent);
    }

    public static PriceTypeMapping fetchExc(long priceTypeId, long materialId, long standartRateId) throws DBException {
        try {
            PriceTypeMapping pricetypemapping = new PriceTypeMapping();
            PstPriceTypeMapping pstPriceTypeMapping = new PstPriceTypeMapping(priceTypeId, materialId, standartRateId);
            pricetypemapping.setPriceTypeId(priceTypeId);
            pricetypemapping.setMaterialId(materialId);
            pricetypemapping.setStandartRateId(standartRateId);

            pricetypemapping.setPrice(pstPriceTypeMapping.getdouble(FLD_PRICE));

            return pricetypemapping;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceTypeMapping(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(PriceTypeMapping pricetypemapping) throws DBException {
        try {
            PstPriceTypeMapping pstPriceTypeMapping = new PstPriceTypeMapping(0);

            pstPriceTypeMapping.setLong(FLD_PRICE_TYPE_ID, pricetypemapping.getPriceTypeId());
            pstPriceTypeMapping.setLong(FLD_MATERIAL_ID, pricetypemapping.getMaterialId());
            pstPriceTypeMapping.setLong(FLD_STANDART_RATE_ID, pricetypemapping.getStandartRateId());
            pstPriceTypeMapping.setDouble(FLD_PRICE, pricetypemapping.getPrice());

           // pstPriceTypeMapping.setData_sync(true);
            pstPriceTypeMapping.insert();

            long oidDataSync = PstDataSyncSql.insertExc(pstPriceTypeMapping.getInsertSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstPriceTypeMapping.getInsertSQL());

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceTypeMapping(0), DBException.UNKNOWN);
        }
        return pricetypemapping.getOID();
    }

    public static long updateExc(PriceTypeMapping pricetypemapping) throws DBException {
        try {
            if (pricetypemapping.getOID() != 0) {
                PstPriceTypeMapping pstPriceTypeMapping = new PstPriceTypeMapping(pricetypemapping.getPriceTypeId(), pricetypemapping.getMaterialId(), pricetypemapping.getStandartRateId());

                pstPriceTypeMapping.setDouble(FLD_PRICE, pricetypemapping.getPrice());

                pstPriceTypeMapping.update();
                long oidDataSync=PstDataSyncSql.insertExc(pstPriceTypeMapping.getUpdateSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstPriceTypeMapping.getUpdateSQL());
                return pricetypemapping.getPriceTypeId();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceTypeMapping(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long priceTypeId, long materialId, long standartRateId) throws DBException {
        try {
            PstPriceTypeMapping pstPriceTypeMapping = new PstPriceTypeMapping(priceTypeId, materialId, standartRateId);
            pstPriceTypeMapping.delete();

            long oidDataSync = PstDataSyncSql.insertExc(pstPriceTypeMapping.getDeleteSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstPriceTypeMapping.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPriceTypeMapping(0), DBException.UNKNOWN);
        }
        return priceTypeId;
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_PRICE_TYPE_MAPPING;
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
                PriceTypeMapping pricetypemapping = new PriceTypeMapping();
                resultToObject(rs, pricetypemapping);
                lists.add(pricetypemapping);
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

    private static void resultToObject(ResultSet rs, PriceTypeMapping pricetypemapping) {
        try {
            pricetypemapping.setPriceTypeId(rs.getLong(PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE_TYPE_ID]));
            pricetypemapping.setMaterialId(rs.getLong(PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_MATERIAL_ID]));
            pricetypemapping.setStandartRateId(rs.getLong(PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_STANDART_RATE_ID]));
            pricetypemapping.setPrice(rs.getDouble(PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long priceTypeId, long materialId, long standartRateId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_PRICE_TYPE_MAPPING + " WHERE " +
                    PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE_TYPE_ID] + " = " + priceTypeId + " AND " +
                    PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_MATERIAL_ID] + " = " + materialId + " AND " +
                    PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_STANDART_RATE_ID] + " = " + standartRateId;

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
            String sql = "SELECT COUNT(" + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_STANDART_RATE_ID] + ") FROM " + TBL_POS_PRICE_TYPE_MAPPING;
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


    public static long deleteByPriceTypeId(long priceTypeId) {
        try {
            String sql = "DELETE FROM " + TBL_POS_PRICE_TYPE_MAPPING +
                    " WHERE " + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE_TYPE_ID] +
                    " = " + priceTypeId;

            int i = DBHandler.execUpdate(sql);

            long oidDataSync = PstDataSyncSql.insertExc(sql);
                PstDataSyncStatus.insertExc(oidDataSync);
            return priceTypeId;
        } catch (Exception exc) {
            System.out.println(" exception delete by Owner ID " + exc.toString());
        }
        return 0;
    }

    public static long deleteByMaterialId(long materialId) {
        try {
            String sql = "DELETE FROM " + TBL_POS_PRICE_TYPE_MAPPING +
                    " WHERE " + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_MATERIAL_ID] +
                    " = " + materialId;
            
            int delete = DBHandler.execUpdate(sql);
            long oidDataSync = PstDataSyncSql.insertExc(sql);
            PstDataSyncStatus.insertExc(oidDataSync);

            return materialId;
        } catch (Exception exc) {
            System.out.println(" exception delete by Owner ID " + exc.toString());
        }
        return 0;
    }

    public static long deleteByStandartRateId(long standartRateId) {
        try {
            String sql = "DELETE FROM " + TBL_POS_PRICE_TYPE_MAPPING +
                    " WHERE " + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_STANDART_RATE_ID] +
                    " = " + standartRateId;

            int i = DBHandler.execUpdate(sql);

            long oidDataSync = PstDataSyncSql.insertExc(sql);
                PstDataSyncStatus.insertExc(oidDataSync);
            return standartRateId;
        } catch (Exception exc) {
            System.out.println(" exception delete by Owner ID " + exc.toString());
        }
        return 0;
    }

    /* This method used to find current data */
    public static int findLimitStart(long priceTypeId, long materialId, long standartRateId, int recordToGet, String whereClause) {
        String order = "";
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, order);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    PriceTypeMapping pricetypemapping = (PriceTypeMapping) list.get(ls);
                    //if(oid == pricetypemapping.getOID())
                    if (priceTypeId == pricetypemapping.getPriceTypeId() && materialId == pricetypemapping.getMaterialId() && standartRateId == pricetypemapping.getStandartRateId())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }

    /* get value for price mapping */
    public static double getPrice(Vector result, long oidMaterial, long oidPriceType, long oidStandarRate) {
        if (result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                PriceTypeMapping prMapping = (PriceTypeMapping) result.get(i);
                if (oidMaterial == prMapping.getMaterialId() && oidPriceType == prMapping.getPriceTypeId() && oidStandarRate == prMapping.getStandartRateId()) {
                    return prMapping.getPrice();
                }
            }
        }
        return 0;
    }

    /** gadnyana
     * untuk mencari object price mapping
     * @param oidMaterial
     * @param curr
     * @return
     */
    public static PriceTypeMapping getSellPriceRupiah(long oidMaterial, String curr) {
        DBResultSet dbrs = null;
        PriceTypeMapping priceTypeMapping = new PriceTypeMapping();
        try {
            Vector listCurr = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS] + "=1", "");
            CurrencyType currencyType = new CurrencyType();
            if (listCurr != null && listCurr.size() > 0) {
                for (int k = 0; k < listCurr.size(); k++) {
                    currencyType = (CurrencyType) listCurr.get(k);
                    if (currencyType.getCode().toUpperCase() == curr.toUpperCase()) {
                        break;
                    }
                }
                // standart rate
                String where = PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] + "=" + currencyType.getOID() +
                        " AND " + PstStandartRate.fieldNames[PstStandartRate.FLD_STATUS] + "=" + PstStandartRate.ACTIVE;
                Vector list = PstStandartRate.list(0, 0, where, "");
                StandartRate standartRate = new StandartRate();
                if (list != null && list.size() > 0) {
                    standartRate = (StandartRate) list.get(0);
                }
                // currency type mapping
                where = PstPriceTypeMapping.fieldNames[FLD_STANDART_RATE_ID] + " = " + standartRate.getOID() +
                        " AND " + PstPriceTypeMapping.fieldNames[FLD_MATERIAL_ID] + "=" + oidMaterial;
                list = PstPriceTypeMapping.list(0, 0, where, "");
                if (list != null && list.size() > 0) {
                    priceTypeMapping = (PriceTypeMapping) list.get(0);
                }
            }
        } catch (Exception e) {
            System.out.println("==>>> Err getSellPriceRupiah : " + e.toString());
        }
        return priceTypeMapping;
    }

    /**
     * untuk mencari harga jual barang yang retail
     * @return
     */
    public static double getSellPrice(long oidMaterial, long standartRateOid, long priceTypeOid) {
        DBResultSet dbrs = null;
        double price = 0;
        try {
            String sql = "SELECT MAX(" + fieldNames[FLD_PRICE] + ") AS PRICE" +
                    " FROM " + TBL_POS_PRICE_TYPE_MAPPING +
                    " WHERE " + fieldNames[FLD_MATERIAL_ID] + "=" + oidMaterial +
                    " AND " + fieldNames[FLD_STANDART_RATE_ID] + "=" + standartRateOid +
                    " AND " + fieldNames[FLD_PRICE_TYPE_ID] + "=" + priceTypeOid;

            System.out.println("getSellPrice sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                price = rs.getDouble("PRICE");
            }
        } catch (Exception e) {
            System.out.println("Error get getSellPrice : " + e.toString());
        }
        return price;
    }

    /**
     *
     * @return
     */
    public static long getOidStandartRate() {
        long oidStandarRate = 0;
        try {
            Vector listCurr = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS] + "=1", "");
            CurrencyType currencyType = new CurrencyType();
            if (listCurr != null && listCurr.size() > 0) {
                for (int k = 0; k < listCurr.size(); k++) {
                    currencyType = (CurrencyType) listCurr.get(k);
                    if (currencyType.getCode().toUpperCase().equals("Rp".toUpperCase())) {
                        System.out.println("Code : "+currencyType.getCode()+" oid : "+currencyType.getOID());
                        break;
                    }
                }
            }

            // standart rate
            String where = PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] + "=" + currencyType.getOID() +
                    " AND " + PstStandartRate.fieldNames[PstStandartRate.FLD_STATUS] + "=" + PstStandartRate.ACTIVE;
            Vector list = PstStandartRate.list(0, 0, where, "");
            StandartRate standartRate = new StandartRate();
            if (list != null && list.size() > 0) {
                standartRate = (StandartRate) list.get(0);
            }
            oidStandarRate = standartRate.getOID();
        } catch (Exception e) {
            System.out.println();
        }
        return oidStandarRate;
    }

    /**
     * untuk mencari price type yang tipe umum / yang retail
     * di group member
     * @return
     */
    public static long getOidPriceType() {
        MemberGroup memberGroup = new MemberGroup();
        try {
            Vector listMemberGroup = PstMemberGroup.list(0, 0, PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] + "=" + PstMemberGroup.UMUM, "");

            if (listMemberGroup != null && listMemberGroup.size() > 0) {
                memberGroup = (MemberGroup) listMemberGroup.get(0);
            }
        } catch (Exception e) {
            System.out.println();
        }
        return memberGroup.getPriceTypeId();
    }
    
   /**
     * get price add opie-eyek 20140428
     * @param oidMaterial
     * @param standartRateOid
     * @param priceTypeOid
     * @return 
     */
       public static double getPrice(long oidMaterial, long standartRateOid, long priceTypeOid) {
        DBResultSet dbrs = null;
        double price = 0;
        try {
            String sql = "SELECT " + fieldNames[FLD_PRICE] + " AS PRICE" +
                    " FROM " + TBL_POS_PRICE_TYPE_MAPPING +
                    " WHERE " + fieldNames[FLD_MATERIAL_ID] + "=" + oidMaterial +
                    " AND " + fieldNames[FLD_STANDART_RATE_ID] + "=" + standartRateOid +
                    " AND " + fieldNames[FLD_PRICE_TYPE_ID] + "=" + priceTypeOid;

            System.out.println("getSellPrice sql : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                price = rs.getDouble("PRICE");
            }
        } catch (Exception e) {
            System.out.println("Error get getSellPrice : " + e.toString());
        }
        return price;
    }
    public static JSONObject fetchJSON(long priceTypeId, long materialId, long standartRateId){
            JSONObject object = new JSONObject();
            try {
                PriceTypeMapping priceTypeMapping = PstPriceTypeMapping.fetchExc(priceTypeId, materialId, standartRateId);
                object.put(PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE_TYPE_ID], priceTypeMapping.getPriceTypeId());
                object.put(PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_MATERIAL_ID], priceTypeMapping.getMaterialId());
                object.put(PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE], priceTypeMapping.getPrice());
                object.put(PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_STANDART_RATE_ID], priceTypeMapping.getStandartRateId());
            }catch(Exception exc){}

            return object;
        }
    
    public static double getValue(String formula) {
        DBResultSet dbrs = null;
        double compValueX = 0;
        try {
            String sql = "SELECT (" + formula + ")";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                compValueX = rs.getDouble(1);
            }

            rs.close();
            return compValueX;
        } catch (Exception e) {
            return 0;            
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    
    public int convertInteger(int scale, double val){
        BigDecimal bDecimal = new BigDecimal(val);
        bDecimal = bDecimal.setScale(scale, RoundingMode.HALF_UP);
        return bDecimal.intValue();
    }
    
    public static int checkString(String strObject, String toCheck) {
        if (toCheck == null || strObject == null) {
            return -1;
        }
        if (strObject.startsWith("=")) {
            strObject = strObject.substring(1);
        }

        String[] parts = strObject.split(" ");
        if (parts.length > 0) {
            for (int i = 0; i < parts.length; i++) {
                String p = parts[i];
                if (toCheck.trim().equalsIgnoreCase(p.trim())) {
                    return i;
                };
            }
        }
        return -1;
    }
}
