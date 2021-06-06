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
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.entity.warehouse.PstMatReturnItem;
import com.dimata.posbo.entity.warehouse.PstMatReturn;
import com.dimata.posbo.db.*;

import java.util.Vector;
import java.util.Hashtable;
import java.sql.ResultSet;
import org.json.JSONObject;

public class PstReturnStockCode extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_POS_RETURN_MATERIAL_CODE = "pos_return_material_item_code";

    public static final int FLD_RETURN_MATERIAL_CODE_ID = 0;
    public static final int FLD_RETURN_MATERIAL_ITEM_ID = 1;
    public static final int FLD_RETURN_MATERIAL_ID = 2;
    public static final int FLD_STOCK_CODE = 3;
    public static final int FLD_STOCK_VALUE = 4;

    public static final String[] fieldNames = {
        "RETURN_MATERIAL_ITEM_CODE_ID",
        "RETURN_MATERIAL_ITEM_ID",
        "RETURN_MATERIAL_ID",
        "STOCK_CODE",
        "VALUE"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_FLOAT
    };

    public PstReturnStockCode() {
    }

    public PstReturnStockCode(int i) throws DBException {
        super(new PstReturnStockCode());
    }

    public PstReturnStockCode(String sOid) throws DBException {
        super(new PstReturnStockCode(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstReturnStockCode(long lOid) throws DBException {
        super(new PstReturnStockCode(0));
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
        return TBL_POS_RETURN_MATERIAL_CODE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstReturnStockCode().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        ReturnStockCode stockCode = fetchExc(ent.getOID());
        ent = (Entity) stockCode;
        return stockCode.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((ReturnStockCode) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((ReturnStockCode) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static ReturnStockCode fetchExc(long oid) throws DBException {
        try {
            ReturnStockCode returnStockCode = new ReturnStockCode();
            PstReturnStockCode pstReturnStockCode = new PstReturnStockCode(oid);
            returnStockCode.setOID(oid);

            returnStockCode.setReturnMaterialItemId(pstReturnStockCode.getlong(FLD_RETURN_MATERIAL_ITEM_ID));

            //dyas 20131202
            returnStockCode.setReturnMaterialId(pstReturnStockCode.getlong(FLD_RETURN_MATERIAL_ID));
            returnStockCode.setStockCode(pstReturnStockCode.getString(FLD_STOCK_CODE));
            returnStockCode.setStockValue(pstReturnStockCode.getdouble(FLD_STOCK_VALUE));
            
            return returnStockCode;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReturnStockCode(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(ReturnStockCode materialStockCode) throws DBException {
        try {
            PstReturnStockCode pstReturnStockCode = new PstReturnStockCode(0);

            pstReturnStockCode.setLong(FLD_RETURN_MATERIAL_ITEM_ID, materialStockCode.getReturnMaterialItemId());

            //dyas 20131202
            pstReturnStockCode.setLong(FLD_RETURN_MATERIAL_ID, materialStockCode.getReturnMaterialId());
            pstReturnStockCode.setString(FLD_STOCK_CODE, materialStockCode.getStockCode());
            pstReturnStockCode.setDouble(FLD_STOCK_VALUE, materialStockCode.getStockValue());
            
            pstReturnStockCode.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstReturnStockCode.getInsertSQL());
            materialStockCode.setOID(pstReturnStockCode.getlong(FLD_RETURN_MATERIAL_CODE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReturnStockCode(0), DBException.UNKNOWN);
        }
        return materialStockCode.getOID();
    }

    public static long updateExc(ReturnStockCode materialStockCode) throws DBException {
        try {
            if (materialStockCode.getOID() != 0) {
                PstReturnStockCode pstReturnStockCode = new PstReturnStockCode(materialStockCode.getOID());

                pstReturnStockCode.setLong(FLD_RETURN_MATERIAL_ITEM_ID, materialStockCode.getReturnMaterialItemId());

                //dyas 20131202
                pstReturnStockCode.setLong(FLD_RETURN_MATERIAL_ID, materialStockCode.getReturnMaterialId());
                pstReturnStockCode.setString(FLD_STOCK_CODE, materialStockCode.getStockCode());
                pstReturnStockCode.setDouble(FLD_STOCK_VALUE, materialStockCode.getStockValue());
                
                pstReturnStockCode.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstReturnStockCode.getUpdateSQL());
                return materialStockCode.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReturnStockCode(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstReturnStockCode pstReturnStockCode = new PstReturnStockCode(oid);
            pstReturnStockCode.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstReturnStockCode.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstReturnStockCode(0), DBException.UNKNOWN);
        }
        return oid;
    }


    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_RETURN_MATERIAL_CODE;
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
                ReturnStockCode materialStockCode = new ReturnStockCode();
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
     * untuk mencari data serial code return yang terpakai
     * di gunakan untuk cek waktu list data serial code di stock
     * @param materialOid
     * @param returnOid
     * @return
     */
    public static Hashtable getSerialCodePreviousUsed(long materialOid, long returnOid) {
        DBResultSet dbrs = null;
        Hashtable has = new Hashtable();
        try {
            String sql = "SELECT CD." + fieldNames[FLD_RETURN_MATERIAL_CODE_ID] +
                    ", CD." + fieldNames[FLD_RETURN_MATERIAL_ITEM_ID] +
                    ", CD." + fieldNames[FLD_STOCK_CODE] +
                    " FROM " + TBL_POS_RETURN_MATERIAL_CODE + " AS CD " +
                    " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " AS ITM " +
                    " ON CD." + fieldNames[FLD_RETURN_MATERIAL_ITEM_ID] + " = ITM." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ITEM_ID] +
                    " INNER JOIN " + PstMatReturn.TBL_MAT_RETURN + " AS RET " +
                    " ON ITM." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] + " = RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                    " WHERE ITM." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] + "=" + materialOid +
                    " AND RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_STATUS] + " = " + I_DocStatus.DOCUMENT_STATUS_DRAFT;

            if (returnOid != 0) {
                sql = sql + " AND RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] + " != " + returnOid;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ReturnStockCode returnStockCode = new ReturnStockCode();
                resultToObject(rs, returnStockCode);
                has.put(returnStockCode.getStockCode(), returnStockCode);
            }
            rs.close();
            //return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return has;
    }

    public static Vector getSerialCode(long materialOid, long returnOid) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT CD." + fieldNames[FLD_RETURN_MATERIAL_CODE_ID] +
                    ", CD." + fieldNames[FLD_RETURN_MATERIAL_ITEM_ID] +
                    ", CD." + fieldNames[FLD_STOCK_CODE] +
                    " FROM " + TBL_POS_RETURN_MATERIAL_CODE + " AS CD " +
                    " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " AS ITM " +
                    " ON CD." + fieldNames[FLD_RETURN_MATERIAL_ITEM_ID] + " = ITM." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ITEM_ID] +
                    " INNER JOIN " + PstMatReturn.TBL_MAT_RETURN + " AS RET " +
                    " ON ITM." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] + " = RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] +
                    " WHERE ITM." + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] + "=" + materialOid;

            if (returnOid != 0) {
                sql = sql + " AND RET." + PstMatReturn.fieldNames[PstMatReturn.FLD_RETURN_MATERIAL_ID] + " = " + returnOid;
            }

            System.out.println("Sql >>> : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ReturnStockCode returnStockCode = new ReturnStockCode();
                resultToObject(rs, returnStockCode);
                list.add(returnStockCode);
            }
            rs.close();
            //return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    private static void resultToObject(ResultSet rs, ReturnStockCode materialStockCode) {
        try {
            materialStockCode.setOID(rs.getLong(PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_CODE_ID]));
            materialStockCode.setReturnMaterialItemId(rs.getLong(PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_ITEM_ID]));

            //dyas 20131202
            materialStockCode.setReturnMaterialId(rs.getLong(PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_ID]));
            materialStockCode.setStockCode(rs.getString(PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_STOCK_CODE]));
            materialStockCode.setStockValue(rs.getDouble(PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_STOCK_VALUE]));
            
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long oid) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_POS_RETURN_MATERIAL_CODE + " WHERE " +
                    PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_CODE_ID] + " = " + oid;

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
            String sql = "SELECT COUNT(" + PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_CODE_ID] + ") FROM " + TBL_POS_RETURN_MATERIAL_CODE;
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
                    ReturnStockCode materialStockCode = (ReturnStockCode) list.get(ls);
                    if (oid == materialStockCode.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }

    public static Vector getSerialCodeReturnBy(long oidMat, long oidRec) {
        Vector list = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DCODE .* FROM " + TBL_POS_RETURN_MATERIAL_CODE + " AS DCODE " +
                    " INNER JOIN " + PstMatReturnItem.TBL_MAT_RETURN_ITEM + " AS IT " +
                    " ON IT."+PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ITEM_ID]+" = DCODE."+fieldNames[FLD_RETURN_MATERIAL_ITEM_ID]+
                    " WHERE IT."+PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ITEM_ID]+"='" + oidRec+"'"+
                    " AND IT."+PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID]+" ='"+oidMat+"'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ReturnStockCode  returnStockCode = new ReturnStockCode();
                resultToObject(rs, returnStockCode);
                list.add(returnStockCode);
            }
        } catch (Exception e) {
            System.out.println("");
        }
        return list;
    }


    public static Vector listPostingReturnSerialCode(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT PRMIC.*,PRMI.MATERIAL_ID FROM " + TBL_POS_RETURN_MATERIAL_CODE+" AS PRMIC"+
                         " INNER JOIN pos_return_material_item AS PRMI ON PRMI.RETURN_MATERIAL_ITEM_ID=PRMIC.RETURN_MATERIAL_ITEM_ID "+
                         " INNER JOIN pos_return_material AS PRM ON PRM.RETURN_MATERIAL_ID=PRMI.RETURN_MATERIAL_ID ";
            
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
                SourceStockCode sourceStockCode = new SourceStockCode();//(SourceStockCode)
                resultToObjectToPosting(rs, sourceStockCode);
                lists.add(sourceStockCode);  
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


    private static void resultToObjectToPosting(ResultSet rs, SourceStockCode materialStockCode) {
        try {
            materialStockCode.setOID(rs.getLong(PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_CODE_ID]));
            materialStockCode.setSourceId(rs.getLong(PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID]));
            materialStockCode.setStockCode(rs.getString(PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_STOCK_CODE]));
            materialStockCode.setStockValue(rs.getDouble(PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_STOCK_VALUE]));
        } catch (Exception e) {
        }
    }

    
     synchronized public static boolean automaticInsertSerialNumber(double cnt, long oid,double strVal, long locationId, long returnMaterialItemId){
          boolean insert = true;
          try{
              //int counter = Integer.parseInt(cnt);
              int qty = (int) cnt;
              String where = PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID]+"="+locationId+
                " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID]+"="+oid+
                " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS]+"="+PstMaterialStockCode.FLD_STOCK_STATUS_GOOD;
              Vector list = PstMaterialStockCode.list(0,qty,where,PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_DATE]+" ASC ");
              
              if (list != null && list.size() > 0) {
                   for(int k=0;k<list.size();k++){      
                        MaterialStockCode materialStockCode = (MaterialStockCode)list.get(k);
                        ReturnStockCode returnStockCode = new ReturnStockCode();
                        returnStockCode.setReturnMaterialItemId(returnMaterialItemId);
                        returnStockCode.setStockCode(""+materialStockCode.getStockCode());
                        returnStockCode.setStockValue(strVal);
                        //dyas 20131130
                        //tambah setReceiveMaterialId untuk mengambil nilai yang dibawa oleh oidReceive
                        returnStockCode.setReturnMaterialId(oid);
                        try{
                            PstReturnStockCode.insertExc(returnStockCode);
                        }catch(Exception e){}
                  }
              }
              
          }catch(Exception e){
              System.out.print("upss"+e);
          }
          return insert;
     }
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         ReturnStockCode returnStockCode = PstReturnStockCode.fetchExc(oid);
         object.put(PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_CODE_ID], returnStockCode.getOID());
         object.put(PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_ITEM_ID], returnStockCode.getReturnMaterialItemId());
         object.put(PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_ID], returnStockCode.getReturnMaterialId());
         object.put(PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_STOCK_CODE], returnStockCode.getStockCode());
         object.put(PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_STOCK_VALUE], returnStockCode.getStockValue());
      }catch(Exception exc){}
      return object;
   }
}
