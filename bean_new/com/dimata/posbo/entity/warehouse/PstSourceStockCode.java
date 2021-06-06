/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Dec 9, 2004
 * Time: 11:29:15 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.Entity;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;

import java.util.Vector;
import java.sql.ResultSet;
import org.json.JSONObject;

public class PstSourceStockCode extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_POS_SOURCE_STOCK_CODE = "pos_source_stock_code";

    public static final int FLD_SOURCE_STOCK_CODE_ID = 0;
    public static final int FLD_SOURCE_ID = 1;
    public static final int FLD_STOCK_CODE = 2;
    public static final int FLD_STOCK_VALUE=3;
    public static final int FLD_STOCK_OPNAME_ID=4;

    public static final String[] fieldNames = {
        "SOURCE_STOCK_CODE_ID",
        "SOURCE_ID",
        "STOCK_CODE",
        "VALUE",
        "STOCK_OPNAME_ID"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_LONG
    };

    public PstSourceStockCode() {
    }

    public PstSourceStockCode(int i) throws DBException {
        super(new PstSourceStockCode());
    }

    public PstSourceStockCode(String sOid) throws DBException {
        super(new PstSourceStockCode(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstSourceStockCode(long lOid) throws DBException {
        super(new PstSourceStockCode(0));
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
        return TBL_POS_SOURCE_STOCK_CODE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstSourceStockCode().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        SourceStockCode stockCode = fetchExc(ent.getOID());
        ent = (Entity) stockCode;
        return stockCode.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((SourceStockCode) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((SourceStockCode) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static SourceStockCode fetchExc(long oid) throws DBException {
        try {
            SourceStockCode sourceStockCode = new SourceStockCode();
            PstSourceStockCode pstSourceStockCode = new PstSourceStockCode(oid);
            sourceStockCode.setOID(oid);

            sourceStockCode.setSourceId(pstSourceStockCode.getlong(FLD_SOURCE_ID));
            sourceStockCode.setStockCode(pstSourceStockCode.getString(FLD_STOCK_CODE));
            sourceStockCode.setStockValue(pstSourceStockCode.getdouble(FLD_STOCK_VALUE));
            sourceStockCode.setStockOpnameId(pstSourceStockCode.getlong(FLD_STOCK_OPNAME_ID));
            
            return sourceStockCode;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSourceStockCode(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(SourceStockCode materialStockCode) throws DBException {
        try {
            PstSourceStockCode pstSourceStockCode = new PstSourceStockCode(0);

            pstSourceStockCode.setLong(FLD_SOURCE_ID, materialStockCode.getSourceId());
            pstSourceStockCode.setString(FLD_STOCK_CODE, materialStockCode.getStockCode());
            pstSourceStockCode.setDouble(FLD_STOCK_VALUE, materialStockCode.getStockValue());
            pstSourceStockCode.setLong(FLD_STOCK_OPNAME_ID, materialStockCode.getStockOpnameId());
            
            pstSourceStockCode.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstSourceStockCode.getInsertSQL());
            materialStockCode.setOID(pstSourceStockCode.getlong(FLD_SOURCE_STOCK_CODE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSourceStockCode(0), DBException.UNKNOWN);
        }
        return materialStockCode.getOID();
    }

    public static long updateExc(SourceStockCode materialStockCode) throws DBException {
        try {
            if (materialStockCode.getOID() != 0) {
                PstSourceStockCode pstSourceStockCode = new PstSourceStockCode(materialStockCode.getOID());

                pstSourceStockCode.setLong(FLD_SOURCE_ID, materialStockCode.getSourceId());
                pstSourceStockCode.setString(FLD_STOCK_CODE, materialStockCode.getStockCode());
                pstSourceStockCode.setDouble(FLD_STOCK_VALUE, materialStockCode.getStockValue());
                pstSourceStockCode.setLong(FLD_STOCK_OPNAME_ID, materialStockCode.getStockOpnameId());
                
                pstSourceStockCode.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstSourceStockCode.getUpdateSQL());
                return materialStockCode.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSourceStockCode(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstSourceStockCode pstSourceStockCode = new PstSourceStockCode(oid);
            pstSourceStockCode.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstSourceStockCode.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSourceStockCode(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_SOURCE_STOCK_CODE;
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
                SourceStockCode materialStockCode = new SourceStockCode();
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

    private static void resultToObject(ResultSet rs, SourceStockCode materialStockCode) {
        try {
            materialStockCode.setOID(rs.getLong(PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_SOURCE_STOCK_CODE_ID]));
            materialStockCode.setSourceId(rs.getLong(PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_SOURCE_ID]));
            materialStockCode.setStockCode(rs.getString(PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_STOCK_CODE]));
            materialStockCode.setStockValue(rs.getDouble(PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_STOCK_VALUE]));
            materialStockCode.setStockOpnameId(rs.getLong(PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_STOCK_OPNAME_ID]));
            
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long discountTypeId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_SOURCE_STOCK_CODE+ " WHERE " +
                    PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_SOURCE_STOCK_CODE_ID] + " = " + discountTypeId;

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
            String sql = "SELECT COUNT(" + PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_SOURCE_STOCK_CODE_ID] + ") FROM " + TBL_POS_SOURCE_STOCK_CODE;
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
                    SourceStockCode materialStockCode = (SourceStockCode) list.get(ls);
                    if (oid == materialStockCode.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         SourceStockCode sourceStockCode = PstSourceStockCode.fetchExc(oid);
         object.put(PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_SOURCE_STOCK_CODE_ID], sourceStockCode.getOID());
         object.put(PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_SOURCE_ID], sourceStockCode.getSourceId());
         object.put(PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_STOCK_CODE], sourceStockCode.getStockCode());
         object.put(PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_STOCK_VALUE], sourceStockCode.getStockValue());
         object.put(PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_STOCK_OPNAME_ID], sourceStockCode.getStockOpnameId());
      }catch(Exception exc){}
      return object;
   }
}
