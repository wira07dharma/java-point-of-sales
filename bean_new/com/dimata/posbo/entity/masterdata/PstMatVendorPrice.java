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

import java.sql.*;
import java.util.*;
/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;

import com.dimata.posbo.entity.masterdata.*;
import com.dimata.common.entity.contact.*;
import org.json.JSONObject;

public class PstMatVendorPrice extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    //public static final String TBL_MATERIAL_VENDOR_PRICE = "POS_VENDOR_PRICE";
    public static final String TBL_MATERIAL_VENDOR_PRICE = "pos_vendor_price";

    public static final int FLD_VENDOR_PRICE_ID = 0;
    public static final int FLD_MATERIAL_ID = 1;
    public static final int FLD_BUYING_UNIT_ID = 2;
    public static final int FLD_VENDOR_ID = 3;
    public static final int FLD_VENDOR_PRICE_CODE = 4;
    public static final int FLD_VENDOR_PRICE_BARCODE = 5;
    public static final int FLD_PRICE_CURRENCY = 6;
    public static final int FLD_ORG_BUYING_PRICE = 7;
    public static final int FLD_LAST_DISCOUNT = 8;
    public static final int FLD_LAST_VAT = 9;
    public static final int FLD_CURR_BUYING_PRICE = 10;
    public static final int FLD_DESCRIPTION = 11;
    public static final int FLD_LAST_COST_CARGO = 12;
    /*
     * Add Discount % from receive material
     * By Mirahu
     */
    public static final int FLD_LAST_DISCOUNT_1 = 13;
    public static final int FLD_LAST_DISCOUNT_2 = 14;

    public static final String[] fieldNames = {
        "VENDOR_PRICE_ID",
        "MATERIAL_ID",
        "BUYING_UNIT_ID",
        "VENDOR_ID",
        "VENDOR_PRICE_CODE",
        "VENDOR_PRICE_BARCODE",
        "PRICE_CURRENCY",
        "ORG_BUYING_PRICE",
        "LAST_DISCOUNT",
        "LAST_VAT",
        "CURR_BUYING_PRICE",
        "DESCRIPTION",
        "LAST_COST_CARGO",
        /* Add Discount % from receive material 
         * By Mirahu
         */
        "LAST_DISCOUNT_1",
        "LAST_DISCOUNT_2"

    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_FLOAT,
         /* Add Discount % from receive material
         * By Mirahu
         */
        TYPE_FLOAT,
        TYPE_FLOAT
    };

    public static final int CURRENCY_IDR = 0;
    public static final int CURRENCY_USD = 1;

    public static final String[] currencyText = {
        "IDR",
        "USD"
    };

    public static Vector getVectorCurrencyText() {
        Vector result = new Vector(1, 1);
        for (int i = 0; i < currencyText.length; i++) {
            result.add(currencyText[i]);
        }
        return result;
    }

    public PstMatVendorPrice() {
    }

    public PstMatVendorPrice(int i) throws DBException {
        super(new PstMatVendorPrice());
    }

    public PstMatVendorPrice(String sOid) throws DBException {
        super(new PstMatVendorPrice(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstMatVendorPrice(long loid) throws DBException {
        super(new PstMatVendorPrice(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(loid);
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
        return TBL_MATERIAL_VENDOR_PRICE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMatVendorPrice().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        MatVendorPrice vendormaterialprice = fetchExc(ent.getOID());
        ent = (Entity) vendormaterialprice;
        return vendormaterialprice.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((MatVendorPrice) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((MatVendorPrice) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent);
    }

    public static MatVendorPrice fetchExc(long oid) throws DBException {
        try {
            MatVendorPrice vendormaterialprice = new MatVendorPrice();
            PstMatVendorPrice pstVendorMaterialPrice = new PstMatVendorPrice(oid);
            vendormaterialprice.setOID(oid);

            vendormaterialprice.setMaterialId(pstVendorMaterialPrice.getlong(FLD_MATERIAL_ID));
            vendormaterialprice.setBuyingUnitId(pstVendorMaterialPrice.getlong(FLD_BUYING_UNIT_ID));
            vendormaterialprice.setVendorId(pstVendorMaterialPrice.getlong(FLD_VENDOR_ID));
            vendormaterialprice.setVendorPriceCode(pstVendorMaterialPrice.getString(FLD_VENDOR_PRICE_CODE));
            vendormaterialprice.setVendorPriceBarcode(pstVendorMaterialPrice.getString(FLD_VENDOR_PRICE_BARCODE));
            vendormaterialprice.setPriceCurrency(pstVendorMaterialPrice.getlong(FLD_PRICE_CURRENCY));
            vendormaterialprice.setOrgBuyingPrice(pstVendorMaterialPrice.getdouble(FLD_ORG_BUYING_PRICE));
            vendormaterialprice.setLastDiscount(pstVendorMaterialPrice.getdouble(FLD_LAST_DISCOUNT));
            vendormaterialprice.setLastVat(pstVendorMaterialPrice.getdouble(FLD_LAST_VAT));
            vendormaterialprice.setCurrBuyingPrice(pstVendorMaterialPrice.getdouble(FLD_CURR_BUYING_PRICE));
            vendormaterialprice.setDescription(pstVendorMaterialPrice.getString(FLD_DESCRIPTION));
            vendormaterialprice.setLastCostCargo(pstVendorMaterialPrice.getdouble(FLD_LAST_COST_CARGO));
            /* Add Discount % from receive material
             * By Mirahu
             */
            vendormaterialprice.setLastDiscount1(pstVendorMaterialPrice.getdouble(FLD_LAST_DISCOUNT_1));
            vendormaterialprice.setLastDiscount2(pstVendorMaterialPrice.getdouble(FLD_LAST_DISCOUNT_2));

            return vendormaterialprice;
        } catch (DBException dbe) {
            System.out.println("DBException");
            throw dbe;
        } catch (Exception e) {
            System.out.println("Exception");
            throw new DBException(new PstMatVendorPrice(0), DBException.UNKNOWN);
        }
    }

public static long insertUpdateExc(MatVendorPrice matVdrPrc) throws DBException {
    long oid=0;
    if( (matVdrPrc==null) || (matVdrPrc.getVendorId()==0) || (matVdrPrc.getMaterialId()==0)){
     return oid;   
    }
    
    String strWhere = ""+fieldNames[FLD_VENDOR_ID]+"='"+matVdrPrc.getVendorId()+"' AND "+
            fieldNames[FLD_MATERIAL_ID]+"='"+matVdrPrc.getMaterialId()+"' ";
    Vector lst = list(0, 100, strWhere, "" );
    if(lst==null || lst.size()==0){
        return insertExc(matVdrPrc);
    } else{
        for(int i=0;i<lst.size();i++){
            MatVendorPrice tempVdr = (MatVendorPrice) lst.get(i);
            if(tempVdr.getBuyingUnitId()==matVdrPrc.getBuyingUnitId()){
                tempVdr.setOrgBuyingPrice(matVdrPrc.getOrgBuyingPrice());
                tempVdr.setCurrBuyingPrice(matVdrPrc.getCurrBuyingPrice());
                tempVdr.setLastDiscount(matVdrPrc.getLastDiscount());     
                tempVdr.setPriceCurrency(matVdrPrc.getPriceCurrency());
                tempVdr.setBuyingUnitId(matVdrPrc.getBuyingUnitId());
                //set Disc1 & Disc2
                tempVdr.setLastDiscount1(matVdrPrc.getLastDiscount1());
                tempVdr.setLastDiscount2(matVdrPrc.getLastDiscount2());
                //set Forwarder + Ppn by Mirahu 25 Mei 2011
                tempVdr.setLastCostCargo(matVdrPrc.getLastCostCargo());
                tempVdr.setLastVat(matVdrPrc.getLastVat());
                updateExc(tempVdr);
                return tempVdr.getOID();
            }            
        }
        return insertExc(matVdrPrc);
    }    
}    
    
    public static long insertExc(MatVendorPrice vendormaterialprice) throws DBException {
        try {
            PstMatVendorPrice pstVendorMaterialPrice = new PstMatVendorPrice(0);

            pstVendorMaterialPrice.setLong(FLD_MATERIAL_ID, vendormaterialprice.getMaterialId());
            pstVendorMaterialPrice.setLong(FLD_BUYING_UNIT_ID, vendormaterialprice.getBuyingUnitId());
            pstVendorMaterialPrice.setLong(FLD_VENDOR_ID, vendormaterialprice.getVendorId());
            pstVendorMaterialPrice.setString(FLD_VENDOR_PRICE_CODE, vendormaterialprice.getVendorPriceCode());
            pstVendorMaterialPrice.setString(FLD_VENDOR_PRICE_BARCODE, vendormaterialprice.getVendorPriceBarcode());
            pstVendorMaterialPrice.setLong(FLD_PRICE_CURRENCY, vendormaterialprice.getPriceCurrency());
            pstVendorMaterialPrice.setDouble(FLD_LAST_DISCOUNT, vendormaterialprice.getLastDiscount());
            pstVendorMaterialPrice.setDouble(FLD_LAST_VAT, vendormaterialprice.getLastVat());
            pstVendorMaterialPrice.setDouble(FLD_CURR_BUYING_PRICE, vendormaterialprice.getCurrBuyingPrice());
            pstVendorMaterialPrice.setDouble(FLD_ORG_BUYING_PRICE, vendormaterialprice.getOrgBuyingPrice());
            pstVendorMaterialPrice.setString(FLD_DESCRIPTION, vendormaterialprice.getDescription());
            pstVendorMaterialPrice.setDouble(FLD_LAST_COST_CARGO, vendormaterialprice.getLastCostCargo());

            /* Add Discount % from receive material
             * By Mirahu
             */
            pstVendorMaterialPrice.setDouble(FLD_LAST_DISCOUNT_1, vendormaterialprice.getLastDiscount1());
            pstVendorMaterialPrice.setDouble(FLD_LAST_DISCOUNT_2, vendormaterialprice.getLastDiscount2());

            pstVendorMaterialPrice.insert();
            vendormaterialprice.setOID(pstVendorMaterialPrice.getlong(FLD_VENDOR_PRICE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatVendorPrice(0), DBException.UNKNOWN);
        }
        return vendormaterialprice.getOID();
    }

    public static long updateExc(MatVendorPrice vendormaterialprice) throws DBException {
        try {
            if (vendormaterialprice.getOID() != 0) {
                PstMatVendorPrice pstVendorMaterialPrice = new PstMatVendorPrice(vendormaterialprice.getOID());

                pstVendorMaterialPrice.setLong(FLD_MATERIAL_ID, vendormaterialprice.getMaterialId());
                pstVendorMaterialPrice.setLong(FLD_BUYING_UNIT_ID, vendormaterialprice.getBuyingUnitId());
                pstVendorMaterialPrice.setLong(FLD_VENDOR_ID, vendormaterialprice.getVendorId());
                pstVendorMaterialPrice.setString(FLD_VENDOR_PRICE_CODE, vendormaterialprice.getVendorPriceCode());
                pstVendorMaterialPrice.setString(FLD_VENDOR_PRICE_BARCODE, vendormaterialprice.getVendorPriceBarcode());
                pstVendorMaterialPrice.setLong(FLD_PRICE_CURRENCY, vendormaterialprice.getPriceCurrency());
                pstVendorMaterialPrice.setDouble(FLD_ORG_BUYING_PRICE, vendormaterialprice.getOrgBuyingPrice());
                pstVendorMaterialPrice.setDouble(FLD_LAST_DISCOUNT, vendormaterialprice.getLastDiscount());
                pstVendorMaterialPrice.setDouble(FLD_LAST_VAT, vendormaterialprice.getLastVat());
                pstVendorMaterialPrice.setDouble(FLD_CURR_BUYING_PRICE, vendormaterialprice.getCurrBuyingPrice());
                pstVendorMaterialPrice.setString(FLD_DESCRIPTION, vendormaterialprice.getDescription());
                pstVendorMaterialPrice.setDouble(FLD_LAST_COST_CARGO, vendormaterialprice.getLastCostCargo());

                /* Add Discount % from receive material
                 * By Mirahu
                 */
                pstVendorMaterialPrice.setDouble(FLD_LAST_DISCOUNT_1, vendormaterialprice.getLastDiscount1());
                pstVendorMaterialPrice.setDouble(FLD_LAST_DISCOUNT_2, vendormaterialprice.getLastDiscount2());

                pstVendorMaterialPrice.update();
                return vendormaterialprice.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatVendorPrice(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstMatVendorPrice pstVendorMaterialPrice = new PstMatVendorPrice(oid);
            pstVendorMaterialPrice.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatVendorPrice(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MATERIAL_VENDOR_PRICE;
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
                MatVendorPrice vendormaterialprice = new MatVendorPrice();
                resultToObject(rs, vendormaterialprice);
                lists.add(vendormaterialprice);
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
    
    
     public static Vector listJoin(int limitStart, int recordToGet, String whereClause, String order) {
         Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT MV.*, FROM " + TBL_MATERIAL_VENDOR_PRICE +" AS MV "+
                         " INNER JOIN "+PstMaterial.TBL_MATERIAL+ " AS MT "+
                         " ON MV."+fieldNames[FLD_MATERIAL_ID]+"= MT."+PstMaterial.fieldNames[FLD_MATERIAL_ID];
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
                MatVendorPrice vendormaterialprice = new MatVendorPrice();
                resultToObject(rs, vendormaterialprice);
                lists.add(vendormaterialprice);
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

    private static void resultToObject(ResultSet rs, MatVendorPrice vendormaterialprice) {
        try {
            vendormaterialprice.setOID(rs.getLong(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_PRICE_ID]));
            vendormaterialprice.setMaterialId(rs.getLong(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID]));
            vendormaterialprice.setBuyingUnitId(rs.getLong(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_BUYING_UNIT_ID]));
            vendormaterialprice.setVendorId(rs.getLong(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID]));
            vendormaterialprice.setVendorPriceCode(rs.getString(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_PRICE_CODE]));
            vendormaterialprice.setVendorPriceBarcode(rs.getString(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_PRICE_BARCODE]));
            vendormaterialprice.setPriceCurrency(rs.getLong(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_PRICE_CURRENCY]));
            vendormaterialprice.setLastDiscount(rs.getDouble(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_LAST_DISCOUNT]));
            vendormaterialprice.setLastVat(rs.getDouble(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_LAST_VAT]));
            vendormaterialprice.setOrgBuyingPrice(rs.getDouble(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_ORG_BUYING_PRICE]));
            vendormaterialprice.setCurrBuyingPrice(rs.getDouble(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_CURR_BUYING_PRICE]));
            vendormaterialprice.setDescription(rs.getString(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_DESCRIPTION]));
            vendormaterialprice.setLastCostCargo(rs.getDouble(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_LAST_COST_CARGO]));
            /* Add Discount % from receive material
             * By Mirahu
             */
            vendormaterialprice.setLastDiscount1(rs.getDouble(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_LAST_DISCOUNT_1]));
            vendormaterialprice.setLastDiscount2(rs.getDouble(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_LAST_DISCOUNT_2]));
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long oid) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MATERIAL_VENDOR_PRICE +
                    " WHERE " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_PRICE_ID] + " = " + oid;

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

    public static boolean checkOIDs(long idMaterial, long idVendor, long currency, long unitId, long vendorId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MATERIAL_VENDOR_PRICE +
                    " WHERE " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] + " = " + idMaterial +
                    " AND " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] + " = " + idVendor +
                    " AND " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_PRICE_CURRENCY] + " = " + currency +
                    " AND " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_BUYING_UNIT_ID] + " = " + unitId +
                    " AND " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_PRICE_ID] + " != " + vendorId;

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
    
    public static boolean checkOIDsVendor(long idMaterial, long idVendor) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MATERIAL_VENDOR_PRICE +
                    " WHERE " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] + " = " + idMaterial +
                    " AND " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] + " = " + idVendor;

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
            String sql = "SELECT COUNT(" + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] + ") FROM " + TBL_MATERIAL_VENDOR_PRICE;
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

    public static long deleteByMaterialId(long materialId) {
        try {
            String sql = "DELETE FROM " + TBL_MATERIAL_VENDOR_PRICE +
                    " WHERE " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] +
                    " = " + materialId;

             int i = DBHandler.execUpdate(sql);
            return materialId;
        } catch (Exception exc) {
            System.out.println(" exception delete by Owner ID " + exc.toString());
        }
        return 0;
    }

    public static long deleteByVendorId(long vendorId) {
        try {
            String sql = "DELETE FROM " + TBL_MATERIAL_VENDOR_PRICE +
                    " WHERE " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] +
                    " = " + vendorId;

            int i = DBHandler.execUpdate(sql);
            return vendorId;
        } catch (Exception exc) {
            System.out.println(" exception delete by Owner ID " + exc.toString());
        }
        return 0;
    }

    /* This method used to find current data */
    public static int findLimitStart(long materialId, long vendorId, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    MatVendorPrice vdrmaterialprice = (MatVendorPrice) list.get(ls);

                    if ((materialId == vdrmaterialprice.getMaterialId()) && (vendorId == vdrmaterialprice.getVendorId()))
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }

    /* This method used to find command where current data */
    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + (recordToGet - mdl);
        if (start == 0)
            cmd = Command.FIRST;
        else {
            if (start == (vectSize - recordToGet))
                cmd = Command.LAST;
            else {
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

    public static String getVendorName(long oid) {
        ContactList contactList = new ContactList();
        String result = "";
        try {
            contactList = PstContactList.fetchExc(oid);
            if (contactList.getContactType() == PstContactList.EXT_COMPANY || contactList.getContactType() == PstContactList.OWN_COMPANY) {
                result = contactList.getCompName();
            }
            if (contactList.getContactType() == PstContactList.EXT_PERSONEL) {
                result = contactList.getPersonName() + " " + contactList.getPersonLastname();
            }
        } catch (Exception e) {
        }
        return result;
    }


    /**
     * ini pencarian untuk object contact atau supplier
     * yang berdasarkan oid material, pencariannya di table
     * vendor price
     * @param materialOid
     * @return
     */
    public static Vector getContactByOidMaterial(long materialOid) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "select distinct cl." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    ", cl." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE] +
                    ", cl." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    " from " + PstMaterial.TBL_MATERIAL + " as p " +
                    " inner join " + TBL_MATERIAL_VENDOR_PRICE + " as pv on p.material_id = pv.material_id " +
                    " inner join " + PstContactList.TBL_CONTACT_LIST + " as cl on pv.vendor_id = cl.contact_id ";
            if(materialOid != 0) {
                sql += " where p.material_id = " + materialOid;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ContactList contact = new ContactList();
                contact.setOID(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
                contact.setContactCode(rs.getString(PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]));
                contact.setCompName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));

                list.add(contact);
            }
        } catch (Exception e) {
            System.out.println("getContactByOidMaterial : "+e.toString());
        }
        return list;
    }
    
    /**
     * 
     * @param materialOid
     * @return 
     */
     public static Vector getContactAndPriceContractByOidMaterial(long materialOid) {
        DBResultSet dbrs = null;
        Vector listComb = new Vector();
        try {
            String sql = "select cl." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    ", cl." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE] +
                    ", cl." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", pv." + fieldNames[FLD_CURR_BUYING_PRICE] +
                    " from " + PstMaterial.TBL_MATERIAL + " as p " +
                    " inner join " + TBL_MATERIAL_VENDOR_PRICE + " as pv on p.material_id = pv.material_id " +
                    " inner join " + PstContactList.TBL_CONTACT_LIST + " as cl on pv.vendor_id = cl.contact_id ";
            if(materialOid != 0) {
                sql += " where p.material_id = " + materialOid;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector list = new Vector();
                ContactList contact = new ContactList();
                contact.setOID(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
                contact.setContactCode(rs.getString(PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]));
                contact.setCompName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));

                list.add(contact);
                
                MatVendorPrice matVendor = new MatVendorPrice();
                matVendor.setCurrBuyingPrice(rs.getDouble(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_CURR_BUYING_PRICE]));
                matVendor.setVendorId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
                
                list.add(matVendor);
                
                listComb.add(list);
            }
        } catch (Exception e) {
            System.out.println("getContactByOidMaterial : "+e.toString());
        }
        return listComb;
    }

    /*-- get price matrial rp or usd --*/
    /**
     public static double getPrice(long oidMaterial, long oidVendor, int currType, double curr) {
     double price = 0;
     String where = PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] + "=" + oidMaterial +
     " AND " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] + "=" + oidVendor;
     Vector vectprice = PstMatVendorPrice.list(0, 0, where, "");
     if (vectprice != null && vectprice.size() > 0) {
     MatVendorPrice vdrMaterialPrice = (MatVendorPrice) vectprice.get(0);
     switch (currType) {
     case PstCurrencyRate.RATE_CODE_RP:
     if (vdrMaterialPrice.getPriceCurrency() != PstCurrencyRate.RATE_CODE_RP) {
     price = vdrMaterialPrice.getPriceUsd() * curr;
     } else {
     price = vdrMaterialPrice.getPriceIdr();
     }
     break;
     case PstCurrencyRate.RATE_CODE_USD:
     if (vdrMaterialPrice.getPriceCurrency() != PstCurrencyRate.RATE_CODE_USD) {
     price = vdrMaterialPrice.getPriceIdr() / curr;
     } else {
     price = vdrMaterialPrice.getPriceUsd();
     }
     break;
     }
     }
     return price;
     }*/

    public static String getVdrCode(long oidMaterial, long oidVendor) {
        String strCode = "";
        String where = "" + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] + "=" + oidMaterial +
                " AND " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] + "=" + oidVendor;
        Vector vectprice = PstMatVendorPrice.list(0, 0, where, "");
        if (vectprice != null && vectprice.size() > 0) {
            MatVendorPrice vdrMaterialPrice = (MatVendorPrice) vectprice.get(0);
            if (vdrMaterialPrice.getVendorPriceBarcode().length() == 0)
                strCode = vdrMaterialPrice.getVendorPriceBarcode();
            else
                strCode = vdrMaterialPrice.getVendorPriceCode();
        }
        return strCode;
    }


    public static MatVendorPrice getVendorObject(long oidMaterial, long oidVendor) {
        MatVendorPrice vdrMaterialPrice = new MatVendorPrice();
        String where = "" + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] + "=" + oidMaterial +
                " AND " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID] + "=" + oidVendor;
        Vector vectprice = PstMatVendorPrice.list(0, 0, where, "");
        if (vectprice != null && vectprice.size() > 0) {
            vdrMaterialPrice = (MatVendorPrice) vectprice.get(0);
        }
        return vdrMaterialPrice;
    }
    
    
    /**
     * mencari kontrak harga supplier berdasarkan contract id dan material id
     * @param whereClause
     * @return 
     */
    public static double getContractPriceWithContactIdAndMaterialId(long contactId, long materialId) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_CURR_BUYING_PRICE] + " FROM " + TBL_MATERIAL_VENDOR_PRICE+
                         " WHERE " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID]+"='"+contactId+"'"+
                         " AND " +PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID]+"='"+materialId+"'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            double contractPrice = 0;
            while (rs.next()) {
                contractPrice = rs.getDouble(1);
            }

            rs.close();
            return contractPrice;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    
    
    public static double getContractPriceWithVendorIdAndUnitId(long vendorId, long unitId) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_ORG_BUYING_PRICE] + " FROM " + TBL_MATERIAL_VENDOR_PRICE+
                         " WHERE " + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID]+"='"+vendorId+"'"+
                         " AND " +PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_BUYING_UNIT_ID]+"='"+unitId+"'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            double contractPrice = 0;
            while (rs.next()) {
                contractPrice = rs.getDouble(1);
            }

            rs.close();
            return contractPrice;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    
    
    public static Vector listFiltter(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DISTINCT VENDOR_ID FROM " + TBL_MATERIAL_VENDOR_PRICE;
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
                MatVendorPrice vendormaterialprice = new MatVendorPrice();
                resultToObjectFilter(rs, vendormaterialprice);
                lists.add(vendormaterialprice);
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
    
    private static void resultToObjectFilter(ResultSet rs, MatVendorPrice vendormaterialprice) {
        try {
            vendormaterialprice.setVendorId(rs.getLong(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID]));
        } catch (Exception e) {
        }
    }
    
     public static Vector listJoinUnit(int limitStart, int recordToGet, String whereClause, String order) {
         Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT  DISTINCT MV.BUYING_UNIT_ID, PU.CODE, MV.ORG_BUYING_PRICE FROM " + TBL_MATERIAL_VENDOR_PRICE +" AS MV "+
                         " INNER JOIN "+PstUnit.TBL_P2_UNIT+ " AS PU "+
                         " ON MV."+fieldNames[FLD_BUYING_UNIT_ID]+"= PU."+PstUnit.fieldNames[PstUnit.FLD_UNIT_ID];
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
                MatVendorPrice vendormaterialprice = new MatVendorPrice();
                resultToObjectJoinUnit(rs, vendormaterialprice);
                lists.add(vendormaterialprice);
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
     
     private static void resultToObjectJoinUnit(ResultSet rs, MatVendorPrice vendormaterialprice) {
        try {
            vendormaterialprice.setBuyingUnitId(rs.getLong(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_BUYING_UNIT_ID]));
            vendormaterialprice.setBuyingUnitName(rs.getString(PstUnit.fieldNames[PstUnit.FLD_CODE]));
            vendormaterialprice.setOrgBuyingPrice(rs.getDouble(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_ORG_BUYING_PRICE]));

        } catch (Exception e) {
        }
    }
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                MatVendorPrice matVendorPrice = PstMatVendorPrice.fetchExc(oid);
                object.put(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_PRICE_ID], matVendorPrice.getOID());
                object.put(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_BUYING_UNIT_ID], matVendorPrice.getBuyingUnitId());
                object.put(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_CURR_BUYING_PRICE], matVendorPrice.getCurrBuyingPrice());
                object.put(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_DESCRIPTION], matVendorPrice.getDescription());
                object.put(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_LAST_COST_CARGO], matVendorPrice.getLastCostCargo());
                object.put(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_LAST_DISCOUNT], matVendorPrice.getLastDiscount());
                object.put(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_LAST_DISCOUNT_1], matVendorPrice.getLastDiscount1());
                object.put(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_LAST_DISCOUNT_2], matVendorPrice.getLastDiscount2());
                object.put(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_LAST_VAT], matVendorPrice.getLastVat());
                object.put(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID], matVendorPrice.getMaterialId());
                object.put(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_ORG_BUYING_PRICE], matVendorPrice.getOrgBuyingPrice());
                object.put(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_PRICE_CURRENCY], matVendorPrice.getPriceCurrency());
                object.put(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_PRICE_BARCODE], matVendorPrice.getVendorPriceBarcode());
                object.put(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_PRICE_CODE], matVendorPrice.getVendorPriceCode());
                object.put(PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID], matVendorPrice.getVendorId());
            }catch(Exception exc){}

            return object;
        }
}
