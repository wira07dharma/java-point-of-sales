/**
 * User: wardana
 * Date: Mar 23, 2004
 * Time: 2:22:11 PM
 * Version: 1.0
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.Entity;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.Unit;

import java.sql.ResultSet;
import java.util.Vector;
import org.json.JSONObject;

public class PstDispatchToProjectItem extends DBHandler implements I_DBInterface, I_DBType,
        I_PersintentExc, I_Language {

    public static final String TBL_DISPATCH_TO_PROJECT_ITEM = "dispatch_to_project_item";

    public static final int FLD_DISPATCH_MATERIAL_ITEM_ID = 0;
    public static final int FLD_DISPATCH_MATERIAL_ID = 1;
    public static final int FLD_MATERIAL_ID = 2;
    public static final int FLD_UNIT_ID = 3;
    public static final int FLD_QTY = 4;
    public static final int FLD_COST = 5;

    public static final String[] stFieldNames = {
        "DISPATCH_MATERIAL_ITEM_ID",
        "DISPATCH_MATERIAL_ID",
        "MATERIAL_ID",
        "UNIT_ID",
        "QTY",
        "COST"
    };

    public static final int[] iFieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT
    };

    public PstDispatchToProjectItem() {
    }

    public PstDispatchToProjectItem(int i) throws DBException {
        super(new PstDispatchToProjectItem());
    }

    public PstDispatchToProjectItem(String stOid) throws DBException {
        super(new PstDispatchToProjectItem(0));
        if (!locate(stOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstDispatchToProjectItem(long lOid) throws DBException {
        super(new PstDispatchToProjectItem(0));
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
        return stFieldNames.length;
    }

    public String getTableName() {
        return TBL_DISPATCH_TO_PROJECT_ITEM;
    }

    public String[] getFieldNames() {
        return stFieldNames;
    }

    public int[] getFieldTypes() {
        return iFieldTypes;
    }

    public String getPersistentName() {
        return new PstDispatchToProjectItem().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        DispatchToProjectItem objDspToProjectItem = fetchExc(ent.getOID());
        ent = objDspToProjectItem;
        return ent.getOID();
    }

    public static DispatchToProjectItem fetchExc(long lOid) throws DBException {
        try {
            DispatchToProjectItem objDspToProjectItem = new DispatchToProjectItem();
            PstDispatchToProjectItem objPstDspToProjectItem = new PstDispatchToProjectItem(lOid);

            objDspToProjectItem.setOID(lOid);
            objDspToProjectItem.setlDispatchMaterialId(objPstDspToProjectItem.getlong(FLD_DISPATCH_MATERIAL_ID));
            objDspToProjectItem.setlMaterialId(objPstDspToProjectItem.getlong(FLD_MATERIAL_ID));
            objDspToProjectItem.setlUnitId(objPstDspToProjectItem.getlong(FLD_UNIT_ID));
            objDspToProjectItem.setiQty(objPstDspToProjectItem.getdouble(FLD_QTY));
            objDspToProjectItem.setdCost(objPstDspToProjectItem.getdouble(FLD_COST));

            return objDspToProjectItem;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDispatchToProjectItem(0), DBException.UNKNOWN);
        }
    }

    public long updateExc(Entity ent) throws Exception {
        return 0;
    }

    public static long updateExc(DispatchToProjectItem objDspToProjectItem) throws DBException {
        try {
            if (objDspToProjectItem.getOID() != 0) {
                PstDispatchToProjectItem objPstDspToProjectItem
                        = new PstDispatchToProjectItem(objDspToProjectItem.getOID());
                objPstDspToProjectItem.setLong(FLD_DISPATCH_MATERIAL_ID, objDspToProjectItem.getlDispatchMaterialId());
                objPstDspToProjectItem.setLong(FLD_MATERIAL_ID, objDspToProjectItem.getlMaterialId());
                objPstDspToProjectItem.setLong(FLD_UNIT_ID, objDspToProjectItem.getlUnitId());
                objPstDspToProjectItem.setDouble(FLD_QTY, objDspToProjectItem.getiQty());
                objPstDspToProjectItem.setDouble(FLD_COST, objDspToProjectItem.getdCost());

                objPstDspToProjectItem.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(objPstDspToProjectItem.getUpdateSQL());
                return objDspToProjectItem.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDispatchToProjectItem(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long deleteExc(Entity ent) throws Exception {
        return deleteExc(ent.getOID());
    }

    public static long deleteExc(long lOid) throws DBException {
        try {
            PstDispatchToProjectItem objPstDspToProjectItem = new PstDispatchToProjectItem(lOid);
            objPstDspToProjectItem.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(objPstDspToProjectItem.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDispatchToProjectItem(0), DBException.UNKNOWN);
        }
        return lOid;
    }

    public static long deleteExcByParent(long lOid) throws DBException {
        long lResult = 0;

        try {
            String sql = "DELETE FROM " + TBL_DISPATCH_TO_PROJECT_ITEM +
                    " WHERE " + stFieldNames[FLD_DISPATCH_MATERIAL_ID] +
                    " = " + lOid;
            execUpdate(sql);
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(sql);
            lResult = lOid;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDispatchToProjectItem(0), DBException.UNKNOWN);
        }

        return lResult;
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((DispatchToProjectItem) ent);
    }

    public static long insertExc(DispatchToProjectItem objDspToProjectItem) throws DBException {
        try {
            PstDispatchToProjectItem objPstDspToProjectItem = new PstDispatchToProjectItem(0);

            objPstDspToProjectItem.setLong(FLD_DISPATCH_MATERIAL_ID, objDspToProjectItem.getlDispatchMaterialId());
            objPstDspToProjectItem.setLong(FLD_MATERIAL_ID, objDspToProjectItem.getlMaterialId());
            objPstDspToProjectItem.setLong(FLD_UNIT_ID, objDspToProjectItem.getlUnitId());
            objPstDspToProjectItem.setDouble(FLD_QTY, objDspToProjectItem.getiQty());
            objPstDspToProjectItem.setDouble(FLD_COST, objDspToProjectItem.getdCost());

            objPstDspToProjectItem.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(objPstDspToProjectItem.getInsertSQL());
            objDspToProjectItem.setOID(objPstDspToProjectItem.getlong(FLD_DISPATCH_MATERIAL_ITEM_ID));
        }
        catch(DBException dbe){
            throw dbe;
        }
        catch(Exception e){
            throw new DBException(new PstDispatchToProjectItem(0), DBException.UNKNOWN);
        }
        return objDspToProjectItem.getOID();
    }

    public static int getCount(String whereClause) {
        int count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT("+ PstDispatchToProjectItem.stFieldNames[PstDispatchToProjectItem.FLD_DISPATCH_MATERIAL_ITEM_ID] +
            ") AS CNT FROM " + TBL_DISPATCH_TO_PROJECT_ITEM;

            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    public static Vector list(int limitStart,int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_DISPATCH_TO_PROJECT_ITEM;
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if(order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL :
                    if(limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
                    break;

                case DBHandler.DBSVR_POSTGRESQL :
                    if(limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " +recordToGet + " OFFSET "+ limitStart ;
                    break;

                case DBHandler.DBSVR_SYBASE :
                    break;

                case DBHandler.DBSVR_ORACLE :
                    break;

                case DBHandler.DBSVR_MSSQL :
                    break;

                default:
                    ;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                DispatchToProjectItem objDspToPrjItem = new DispatchToProjectItem();
                resultToObject(rs, objDspToPrjItem);
                lists.add(objDspToPrjItem);
            }
            rs.close();
            return lists;

        }catch(Exception e) {
            System.out.println(e);
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    public static Vector list(int limitStart,int recordToGet, long oidMatDispatch) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DPI." + stFieldNames[FLD_DISPATCH_MATERIAL_ITEM_ID] +
            " , DPI." + stFieldNames[FLD_MATERIAL_ID] +
            " , DPI." + stFieldNames[FLD_UNIT_ID] +
            " , DPI." + stFieldNames[FLD_QTY] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            " , UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] +
            " , DPI." + stFieldNames[FLD_COST] +
            " FROM (" + TBL_DISPATCH_TO_PROJECT_ITEM + " DPI" +
            " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON DPI." + stFieldNames[FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ")" +
            " INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON DPI." + stFieldNames[FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " WHERE DPI." + stFieldNames[FLD_DISPATCH_MATERIAL_ID] +
            " = " + oidMatDispatch +
            " ORDER BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL :
                    if(limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
                    break;

                case DBHandler.DBSVR_POSTGRESQL :
                    if(limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " +recordToGet + " OFFSET "+ limitStart ;
                    break;

                case DBHandler.DBSVR_SYBASE :
                    break;

                case DBHandler.DBSVR_ORACLE :
                    break;

                case DBHandler.DBSVR_MSSQL :
                    break;

                default:
                    ;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                Vector temp = new Vector();
                DispatchToProjectItem objDspToPrj = new DispatchToProjectItem();
                Material mat = new Material();
                Unit unit = new Unit();

                objDspToPrj.setOID(rs.getLong(1));
                objDspToPrj.setlMaterialId(rs.getLong(2));
                objDspToPrj.setlUnitId(rs.getLong(3));
                objDspToPrj.setiQty(rs.getDouble(4));
                objDspToPrj.setdCost(rs.getDouble(10));
                temp.add(objDspToPrj);

                mat.setSku(rs.getString(5));
                mat.setName(rs.getString(6));
                mat.setDefaultCost(rs.getDouble(8));
                mat.setDefaultCostCurrencyId(rs.getLong(9));
                temp.add(mat);

                unit.setCode(rs.getString(7));
                temp.add(unit);

                lists.add(temp);
            }
            rs.close();

        }
        catch(Exception e) {
            lists = new Vector();
            System.out.println(e);
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    public static void resultToObject(ResultSet rs, DispatchToProjectItem objDspToPrjItem) {
        try {
            objDspToPrjItem.setOID(rs.getLong(PstDispatchToProjectItem.stFieldNames[PstDispatchToProjectItem.FLD_DISPATCH_MATERIAL_ITEM_ID]));
            objDspToPrjItem.setlDispatchMaterialId(rs.getLong(PstDispatchToProjectItem.stFieldNames[PstDispatchToProjectItem.FLD_DISPATCH_MATERIAL_ID]));
            objDspToPrjItem.setlMaterialId(rs.getLong(PstDispatchToProjectItem.stFieldNames[PstDispatchToProjectItem.FLD_MATERIAL_ID]));
            objDspToPrjItem.setlUnitId(rs.getLong(PstDispatchToProjectItem.stFieldNames[PstDispatchToProjectItem.FLD_UNIT_ID]));
            objDspToPrjItem.setiQty(rs.getDouble(PstDispatchToProjectItem.stFieldNames[PstDispatchToProjectItem.FLD_QTY]));
            objDspToPrjItem.setdCost(rs.getDouble(PstDispatchToProjectItem.stFieldNames[PstDispatchToProjectItem.FLD_COST]));
        }
        catch(Exception e)
        { }
    }


   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         DispatchToProjectItem dispatchToProjectItem = PstDispatchToProjectItem.fetchExc(oid);
         object.put(PstDispatchToProjectItem.stFieldNames[PstDispatchToProjectItem.FLD_DISPATCH_MATERIAL_ITEM_ID], dispatchToProjectItem.getOID());
         object.put(PstDispatchToProjectItem.stFieldNames[PstDispatchToProjectItem.FLD_DISPATCH_MATERIAL_ID], dispatchToProjectItem.getlDispatchMaterialId());
         object.put(PstDispatchToProjectItem.stFieldNames[PstDispatchToProjectItem.FLD_MATERIAL_ID], dispatchToProjectItem.getlMaterialId());
         object.put(PstDispatchToProjectItem.stFieldNames[PstDispatchToProjectItem.FLD_UNIT_ID], dispatchToProjectItem.getlUnitId());
         object.put(PstDispatchToProjectItem.stFieldNames[PstDispatchToProjectItem.FLD_QTY], dispatchToProjectItem.getiQty());
         object.put(PstDispatchToProjectItem.stFieldNames[PstDispatchToProjectItem.FLD_COST], dispatchToProjectItem.getdCost());
      }catch(Exception exc){}
      return object;
   }
}
