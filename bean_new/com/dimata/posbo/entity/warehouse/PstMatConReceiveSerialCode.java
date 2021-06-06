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

public class PstMatConReceiveSerialCode extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_POS_RECEIVE_MATERIAL_CODE = "pos_receive_con_material_item_code";

    public static final int FLD_RECEIVE_MATERIAL_CODE_ID = 0;
    public static final int FLD_RECEIVE_MATERIAL_ITEM_ID = 1;
    public static final int FLD_STOCK_CODE = 2;

    public static final String[] fieldNames = {
        "RECEIVE_CON_MATERIAL_CODE_ID",
        "RECEIVE_CON_MATERIAL_ITEM_ID",
        "STOCK_CODE"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_STRING
    };

    public PstMatConReceiveSerialCode() {
    }

    public PstMatConReceiveSerialCode(int i) throws DBException {
        super(new PstMatConReceiveSerialCode());
    }

    public PstMatConReceiveSerialCode(String sOid) throws DBException {
        super(new PstMatConReceiveSerialCode(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstMatConReceiveSerialCode(long lOid) throws DBException {
        super(new PstMatConReceiveSerialCode(0));
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
        return TBL_POS_RECEIVE_MATERIAL_CODE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMatConReceiveSerialCode().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        MatConReceiveSerialCode stockCode = fetchExc(ent.getOID());
        ent = (Entity) stockCode;
        return stockCode.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((MatConReceiveSerialCode) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((MatConReceiveSerialCode) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static MatConReceiveSerialCode fetchExc(long oid) throws DBException {
        try {
            MatConReceiveSerialCode receiveStockCode = new MatConReceiveSerialCode();
            PstMatConReceiveSerialCode pstMatConReceiveSerialCode = new PstMatConReceiveSerialCode(oid);
            receiveStockCode.setOID(oid);

            receiveStockCode.setReceiveMaterialItemId(pstMatConReceiveSerialCode.getlong(FLD_RECEIVE_MATERIAL_ITEM_ID));
            receiveStockCode.setStockCode(pstMatConReceiveSerialCode.getString(FLD_STOCK_CODE));

            return receiveStockCode;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatConReceiveSerialCode(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(MatConReceiveSerialCode materialStockCode) throws DBException {
        try {
            PstMatConReceiveSerialCode pstMatConReceiveSerialCode = new PstMatConReceiveSerialCode(0);

            pstMatConReceiveSerialCode.setLong(FLD_RECEIVE_MATERIAL_ITEM_ID, materialStockCode.getReceiveMaterialItemId());
            pstMatConReceiveSerialCode.setString(FLD_STOCK_CODE, materialStockCode.getStockCode());

            pstMatConReceiveSerialCode.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatConReceiveSerialCode.getInsertSQL());
            materialStockCode.setOID(pstMatConReceiveSerialCode.getlong(FLD_RECEIVE_MATERIAL_CODE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatConReceiveSerialCode(0), DBException.UNKNOWN);
        }
        return materialStockCode.getOID();
    }

    public static long updateExc(MatConReceiveSerialCode materialStockCode) throws DBException {
        try {
            if (materialStockCode.getOID() != 0) {
                PstMatConReceiveSerialCode pstMatConReceiveSerialCode = new PstMatConReceiveSerialCode(materialStockCode.getOID());

                pstMatConReceiveSerialCode.setLong(FLD_RECEIVE_MATERIAL_ITEM_ID, materialStockCode.getReceiveMaterialItemId());
                pstMatConReceiveSerialCode.setString(FLD_STOCK_CODE, materialStockCode.getStockCode());

                pstMatConReceiveSerialCode.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMatConReceiveSerialCode.getUpdateSQL());
                return materialStockCode.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatConReceiveSerialCode(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstMatConReceiveSerialCode pstMatConReceiveSerialCode = new PstMatConReceiveSerialCode(oid);
            pstMatConReceiveSerialCode.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatConReceiveSerialCode.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatConReceiveSerialCode(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_RECEIVE_MATERIAL_CODE;
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
                MatConReceiveSerialCode materialStockCode = new MatConReceiveSerialCode();
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

    private static void resultToObject(ResultSet rs, MatConReceiveSerialCode materialStockCode) {
        try {
            materialStockCode.setOID(rs.getLong(PstMatConReceiveSerialCode.fieldNames[PstMatConReceiveSerialCode.FLD_RECEIVE_MATERIAL_CODE_ID]));
            materialStockCode.setReceiveMaterialItemId(rs.getLong(PstMatConReceiveSerialCode.fieldNames[PstMatConReceiveSerialCode.FLD_RECEIVE_MATERIAL_ITEM_ID]));
            materialStockCode.setStockCode(rs.getString(PstMatConReceiveSerialCode.fieldNames[PstMatConReceiveSerialCode.FLD_STOCK_CODE]));
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long oid) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_RECEIVE_MATERIAL_CODE + " WHERE " +
                    PstMatConReceiveSerialCode.fieldNames[PstMatConReceiveSerialCode.FLD_RECEIVE_MATERIAL_CODE_ID] + " = " + oid;

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
            String sql = "SELECT COUNT(" + PstMatConReceiveSerialCode.fieldNames[PstMatConReceiveSerialCode.FLD_RECEIVE_MATERIAL_CODE_ID] + ") FROM " + TBL_POS_RECEIVE_MATERIAL_CODE;
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
                    MatConReceiveSerialCode materialStockCode = (MatConReceiveSerialCode) list.get(ls);
                    if (oid == materialStockCode.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }


    /** gadnyana
     * get serial receive
     * @param oidMat
     * @param oidDf
     * @return
     */
    public static Vector getSerialCodeReceiveBy(long oidMat, long oidRec) {
        Vector list = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DCODE .* FROM " + TBL_POS_RECEIVE_MATERIAL_CODE + " AS DCODE " +
                    " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " AS IT " +
                    " ON IT.RECEIVE_MATERIAL_ITEM_ID = DCODE.RECEIVE_MATERIAL_ITEM_ID " +
                    " WHERE IT.RECEIVE_MATERIAL_ID = " + oidRec +
                    " AND IT.MATERIAL_ID = " + oidMat;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MatConReceiveSerialCode materialStockCode = new MatConReceiveSerialCode();
                resultToObject(rs, materialStockCode);
                list.add(materialStockCode);
            }
        } catch (Exception e) {
            System.out.println("");
        }
        return list;
    }

    // ini untuk men insert data jika berisi serial code (untuk sementara)
    public static void getInsertSerialFromReturn(long oidItem, long oidRet, long oidMat) {
        Vector list = new Vector();
        DBResultSet dbrs = null;
        try {
            String where = PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] + "=" + oidRet + " AND " +
                    PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] + "=" + oidMat;
            Vector vet = PstMatReturnItem.list(0, 0, where, "");
            if (vet != null && vet.size() > 0) {
                MatReturnItem matReturnItem = (MatReturnItem) vet.get(0);
                where = PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_ITEM_ID] + "=" + matReturnItem.getOID();
                Vector vCode = PstReturnStockCode.list(0, 0, where, "");
                if (vCode != null && vCode.size() > 0) {
                    for(int k=0;k<vCode.size();k++){
                        ReturnStockCode returnStockCode = (ReturnStockCode) vCode.get(k);
                        MatConReceiveSerialCode receiveStockCode = new MatConReceiveSerialCode();
                        receiveStockCode.setReceiveMaterialItemId(oidItem);
                        receiveStockCode.setStockCode(returnStockCode.getStockCode());
                        try{
                            PstMatConReceiveSerialCode.insertExc(receiveStockCode);
                        }catch(Exception e){}
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("");
        }
    }
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MatConReceiveSerialCode matConReceiveSerialCode = PstMatConReceiveSerialCode.fetchExc(oid);
         object.put(PstMatConReceiveSerialCode.fieldNames[PstMatConReceiveSerialCode.FLD_RECEIVE_MATERIAL_CODE_ID], matConReceiveSerialCode.getReceiveMaterialCodeId());
         object.put(PstMatConReceiveSerialCode.fieldNames[PstMatConReceiveSerialCode.FLD_RECEIVE_MATERIAL_ITEM_ID], matConReceiveSerialCode.getReceiveMaterialItemId());
         object.put(PstMatConReceiveSerialCode.fieldNames[PstMatConReceiveSerialCode.FLD_STOCK_CODE], matConReceiveSerialCode.getStockCode());
      }catch(Exception exc){}
      return object;
   }
}
