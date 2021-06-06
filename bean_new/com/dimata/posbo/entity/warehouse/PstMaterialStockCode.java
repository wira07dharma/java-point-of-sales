/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Dec 9, 2004
 * Time: 11:29:15 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.Entity;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;

import com.dimata.posbo.session.warehouse.SessPriceProtection;
import java.util.Vector;
import java.sql.ResultSet;
import java.util.Hashtable;

public class PstMaterialStockCode extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_POS_MATERIAL_STOCK_CODE = "pos_material_stock_code";

    public static final int FLD_MATERIAL_STOCK_CODE_ID = 0;
    public static final int FLD_MATERIAL_ID = 1;
    public static final int FLD_LOCATION_ID = 2;
    public static final int FLD_STOCK_CODE = 3;
    public static final int FLD_STOCK_STATUS = 4;
    public static final int FLD_STOCK_VALUE = 5;
    public static final int FLD_STOCK_DATE = 6;

    public static final String[] fieldNames = {
        "MATERIAL_STOCK_CODE_ID",
        "MATERIAL_ID",
        "LOCATION_ID",
        "STOCK_CODE",
        "STOCK_STATUS",
        "VALUE",
        "STOCK_DATE"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_DATE
    };


    public static final int FLD_STOCK_STATUS_GOOD = 0;
    public static final int FLD_STOCK_STATUS_BAD = 1;
    public static final int FLD_STOCK_STATUS_LOSS = 2;
    public static final int FLD_STOCK_STATUS_SOLED = 3;
    public static final int FLD_STOCK_STATUS_PROCESS = 4;
    public static final int FLD_STOCK_STATUS_DELIVERED = 5;
    public static final int FLD_STOCK_STATUS_RETURN = 6;

    public static final String[] fieldStatus = {
        "Good Stock",
        "Bad Stock",
        "Loss Stock",
        "Soled",
        "Process",
        "Delivered",
        "Return"
    };


    public PstMaterialStockCode() {
    }

    public PstMaterialStockCode(int i) throws DBException {
        super(new PstMaterialStockCode());
    }

    public PstMaterialStockCode(String sOid) throws DBException {
        super(new PstMaterialStockCode(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstMaterialStockCode(long lOid) throws DBException {
        super(new PstMaterialStockCode(0));
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
        return TBL_POS_MATERIAL_STOCK_CODE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMaterialStockCode().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        MaterialStockCode materialStockCode = fetchExc(ent.getOID());
        ent = (Entity) materialStockCode;
        return materialStockCode.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((MaterialStockCode) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((MaterialStockCode) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static MaterialStockCode fetchExc(long oid) throws DBException {
        try {
            MaterialStockCode materialStockCode = new MaterialStockCode();
            PstMaterialStockCode pstMaterialStockCode = new PstMaterialStockCode(oid);
            materialStockCode.setOID(oid);

            materialStockCode.setMaterialId(pstMaterialStockCode.getlong(FLD_MATERIAL_ID));
            materialStockCode.setLocationId(pstMaterialStockCode.getlong(FLD_LOCATION_ID));
            materialStockCode.setStockCode(pstMaterialStockCode.getString(FLD_STOCK_CODE));
            materialStockCode.setStockStatus(pstMaterialStockCode.getInt(FLD_STOCK_STATUS));
            materialStockCode.setStockValue(pstMaterialStockCode.getdouble(FLD_STOCK_VALUE));
            materialStockCode.setDateStock(pstMaterialStockCode.getDate(FLD_STOCK_DATE));
            return materialStockCode;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialStockCode(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(MaterialStockCode materialStockCode) throws DBException {
        try {
            PstMaterialStockCode pstMaterialStockCode = new PstMaterialStockCode(0);

            pstMaterialStockCode.setLong(FLD_MATERIAL_ID, materialStockCode.getMaterialId());
            pstMaterialStockCode.setLong(FLD_LOCATION_ID, materialStockCode.getLocationId());
            pstMaterialStockCode.setString(FLD_STOCK_CODE, materialStockCode.getStockCode());
            pstMaterialStockCode.setInt(FLD_STOCK_STATUS, materialStockCode.getStockStatus());
            pstMaterialStockCode.setDouble(FLD_STOCK_VALUE, materialStockCode.getStockValue());
            pstMaterialStockCode.setDate(FLD_STOCK_DATE, materialStockCode.getDateStock());
            pstMaterialStockCode.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMaterialStockCode.getInsertSQL());
            materialStockCode.setOID(pstMaterialStockCode.getlong(FLD_MATERIAL_STOCK_CODE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialStockCode(0), DBException.UNKNOWN);
        }
        return materialStockCode.getOID();
    }

    public static long updateExc(MaterialStockCode materialStockCode) throws DBException {
        try {
            if (materialStockCode.getOID() != 0) {
                PstMaterialStockCode pstMaterialStockCode = new PstMaterialStockCode(materialStockCode.getOID());

                pstMaterialStockCode.setLong(FLD_MATERIAL_ID, materialStockCode.getMaterialId());
                pstMaterialStockCode.setLong(FLD_LOCATION_ID, materialStockCode.getLocationId());
                pstMaterialStockCode.setString(FLD_STOCK_CODE, materialStockCode.getStockCode());
                pstMaterialStockCode.setInt(FLD_STOCK_STATUS, materialStockCode.getStockStatus());
                pstMaterialStockCode.setDouble(FLD_STOCK_VALUE, materialStockCode.getStockValue());
                pstMaterialStockCode.setDate(FLD_STOCK_DATE, materialStockCode.getDateStock());
                
                pstMaterialStockCode.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMaterialStockCode.getUpdateSQL());
                return materialStockCode.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialStockCode(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstMaterialStockCode pstMaterialStockCode = new PstMaterialStockCode(oid);
            pstMaterialStockCode.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMaterialStockCode.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialStockCode(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_MATERIAL_STOCK_CODE;
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
                MaterialStockCode materialStockCode = new MaterialStockCode();
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
            String sql = "SELECT * FROM " + TBL_POS_MATERIAL_STOCK_CODE;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MaterialStockCode materialStockCode = new MaterialStockCode();
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
            String sql = "DELETE FROM " + TBL_POS_MATERIAL_STOCK_CODE;
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


    private static void resultToObject(ResultSet rs, MaterialStockCode materialStockCode) {
        try {
            
            materialStockCode.setOID(rs.getLong(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_STOCK_CODE_ID]));
            materialStockCode.setMaterialId(rs.getLong(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID]));
            materialStockCode.setLocationId(rs.getLong(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID]));
            materialStockCode.setStockCode(rs.getString(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_CODE]));
            materialStockCode.setStockStatus(rs.getInt(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS]));
            materialStockCode.setStockValue(rs.getDouble(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_VALUE]));
            materialStockCode.setDateStock(rs.getDate(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_DATE]));
            
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long discountTypeId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_MATERIAL_STOCK_CODE + " WHERE " +
                    PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_STOCK_CODE_ID] + " = " + discountTypeId;

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
            String sql = "SELECT COUNT(" + PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_STOCK_CODE_ID] + ") FROM " + TBL_POS_MATERIAL_STOCK_CODE;
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
                    MaterialStockCode materialStockCode = (MaterialStockCode) list.get(ls);
                    if (oid == materialStockCode.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }


    /** adnyana
     * untuk pengecekan data code di stock barang
     * ini di gunakan pada saat stock corection
     * @param code
     * @param materialId
     * @return
     */
    public static MaterialStockCode cekExistByCode(String code, long materialId) {
        MaterialStockCode materialStockCode = new MaterialStockCode();
        try {
            String where = fieldNames[FLD_STOCK_CODE] + "='" + code + "' AND " + fieldNames[FLD_MATERIAL_ID] + "=" + materialId;
            // " AND " + fieldNames[FLD_STOCK_STATUS] + "=" + FLD_STOCK_STATUS_GOOD;
            Vector list = list(0, 0, where, "");
            if (list != null && list.size() > 0) {
                materialStockCode = (MaterialStockCode) list.get(0);
            }
        } catch (Exception e) {
        }
        return materialStockCode;
    }


    public static int deleteStockCodeBySerialCode(String serialCode, long locationId) {
        DBResultSet dbrs = null;
        try {
            String sql = " DELETE FROM " + TBL_POS_MATERIAL_STOCK_CODE+
                         " WHERE " + PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_CODE]+"='"+serialCode+"'"+
                         " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID]+"='"+locationId+"'";
           
            DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
            return 1;
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }
    
    
     public static Vector getPosProtectionVendor(long materialId, long locationId) {
        Vector vlistPP = new Vector();
        Hashtable <String, Boolean> listPP = null;
        Vector allListSerialCode = new  Vector();
        DBResultSet dbrs = null;
        try {
            
            //cari jumlah supplier
            
            String sqlCount = " SELECT DISTINCT cl.CONTACT_ID,cl.PERSON_NAME "+
                         " FROM pos_material_stock_code AS pmsc "+
                         " INNER JOIN pos_receive_material_item_code AS prmic "+
                         " ON pmsc.STOCK_CODE=prmic.STOCK_CODE "+
                         " INNER JOIN pos_receive_material AS prm "+
                         " ON prmic.RECEIVE_MATERIAL_ID=prm.RECEIVE_MATERIAL_ID "+
                         " INNER JOIN contact_list AS cl "+
                         " ON cl.CONTACT_ID=prm.SUPPLIER_ID "+
                         " WHERE pmsc.LOCATION_ID='"+locationId+"'"+
                         " AND pmsc.MATERIAL_ID='"+materialId+"'"+
                         " AND pmsc.STOCK_STATUS=0";
            dbrs = DBHandler.execQueryResult(sqlCount);
            ResultSet rsx = dbrs.getResultSet();
            Vector supplierCount= new Vector();
            while (rsx.next()) {
                     ContactList contact = new ContactList();
                     contact.setOID(rsx.getLong(1));
                     contact.setPersonName(rsx.getString(2));
                     supplierCount.add(contact);
            }
            vlistPP.add(supplierCount);
            
            String sql = " SELECT DISTINCT pmsc.STOCK_CODE,pmsc.VALUE, prm.SUPPLIER_ID,cl.PERSON_NAME,pmsc.MATERIAL_STOCK_CODE_ID "+
                         " FROM pos_material_stock_code AS pmsc "+
                         " INNER JOIN pos_receive_material_item_code AS prmic "+
                         " ON pmsc.STOCK_CODE=prmic.STOCK_CODE "+
                         " INNER JOIN pos_receive_material AS prm "+
                         " ON prmic.RECEIVE_MATERIAL_ID=prm.RECEIVE_MATERIAL_ID "+
                         " INNER JOIN contact_list AS cl "+
                         " ON cl.CONTACT_ID=prm.SUPPLIER_ID "+
                         " WHERE pmsc.LOCATION_ID='"+locationId+"'"+
                         " AND pmsc.MATERIAL_ID='"+materialId+"'"+
                         " AND pmsc.STOCK_STATUS=0";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                     if(listPP==null)
                     listPP = new Hashtable();
                     
                     SessPriceProtection sessPriceProtection = new SessPriceProtection();
                     sessPriceProtection.setStockCode(rs.getString(1));
                     sessPriceProtection.setValue(rs.getDouble(2));
                     sessPriceProtection.setSupplierID(rs.getLong(3));
                     sessPriceProtection.setPersonName(rs.getString(4));
                     sessPriceProtection.setStockCodeId(rs.getLong(5));
                     
                     String keyPrice=""+rs.getString(1)+"_"+sessPriceProtection.getSupplierID();
                     listPP.put(keyPrice,true);   
                     allListSerialCode.add(sessPriceProtection);
            }
            
            vlistPP.add(listPP);
            vlistPP.add(allListSerialCode);
            
            rs.close();
        }catch (Exception e) {
            System.out.println("err get pp record : " + e.toString());
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
            return vlistPP;
        }
     }
     
     
     public static int updateAmountAfterGetPP(String stockCode, double totAmount) {
        DBResultSet dbrs = null;
        try
            {
                String sql = "UPDATE " + TBL_POS_MATERIAL_STOCK_CODE+
                    " SET " + fieldNames[FLD_STOCK_VALUE] +
                    " = ("+fieldNames[FLD_STOCK_VALUE]+" - "+totAmount+")"+
                    " WHERE " + fieldNames[FLD_STOCK_CODE] +
                    " = '" + stockCode+"'";
                DBHandler.execUpdate(sql);
            return 0;
        } catch (Exception e) {
            System.out.println("");
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
     
    public static int updateAmountAfterGetPP(long stockCodeOid, double totAmount) {
        DBResultSet dbrs = null;
        try
            {
                String sql = "UPDATE " + TBL_POS_MATERIAL_STOCK_CODE+
                    " SET " + fieldNames[FLD_STOCK_VALUE] +
                    " = ("+fieldNames[FLD_STOCK_VALUE]+" - "+totAmount+")"+
                    " WHERE " + fieldNames[FLD_MATERIAL_STOCK_CODE_ID] +
                    " = '" + stockCodeOid+"'";
                DBHandler.execUpdate(sql);
            return 0;
        } catch (Exception e) {
            System.out.println("");
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

}
