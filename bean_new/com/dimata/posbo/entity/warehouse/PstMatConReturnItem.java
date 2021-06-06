package com.dimata.posbo.entity.warehouse;

/* package java */
import com.dimata.common.entity.custom.PstDataCustom;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PstCurrencyType;

/* package garment */
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.warehouse.*;
import org.json.JSONObject;

public class PstMatConReturnItem extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    public static final  String TBL_MAT_RETURN_ITEM = "pos_return_con_material_item";
    
    public static final  int FLD_RETURN_MATERIAL_ITEM_ID    = 0;
    public static final  int FLD_RETURN_MATERIAL_ID         = 1;
    public static final  int FLD_MATERIAL_ID                = 2;
    public static final  int FLD_UNIT_ID                    = 3;
    public static final  int FLD_COST                       = 4;
    public static final  int FLD_CURRENCY_ID                = 5;
    public static final  int FLD_QTY                        = 6;
    public static final  int FLD_TOTAL                      = 7;
    public static final  int FLD_RESIDUE_QTY                = 8;
    
    public static final  String[] fieldNames = {
        "RETURN_CON_MATERIAL_ITEM_ID",
        "RETURN_CON_MATERIAL_ID",
        "MATERIAL_ID",
        "UNIT_ID",
        "COST",
        "CURRENCY_ID",
        "QTY",
        "TOTAL",
        "RESIDUE_QTY"
    };
    
    public static final  int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT
    };
    
    public PstMatConReturnItem(){
    }
    
    public PstMatConReturnItem(int i) throws DBException {
        super(new PstMatConReturnItem());
    }
    
    public PstMatConReturnItem(String sOid) throws DBException {
        super(new PstMatConReturnItem(0));
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstMatConReturnItem(long lOid) throws DBException {
        super(new PstMatConReturnItem(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        }catch(Exception e) {
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public int getFieldSize(){
        return fieldNames.length;
    }
    
    public String getTableName(){
        return TBL_MAT_RETURN_ITEM;
    }
    
    public String[] getFieldNames(){
        return fieldNames;
    }
    
    public int[] getFieldTypes(){
        return fieldTypes;
    }
    
    public String getPersistentName(){
        return new PstMatConReturnItem().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception{
        MatConReturnItem matreturnitem = fetchExc(ent.getOID());
        ent = (Entity)matreturnitem;
        return matreturnitem.getOID();
    }
    
    synchronized public long insertExc(Entity ent) throws Exception{
        return insertExc((MatConReturnItem) ent);
    }
    
    synchronized public long updateExc(Entity ent) throws Exception{
        return updateExc((MatConReturnItem) ent);
    }
    
    synchronized public long deleteExc(Entity ent) throws Exception{
        if(ent==null){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public static MatConReturnItem fetchExc(long oid) throws DBException {
        try {
            MatConReturnItem matreturnitem = new MatConReturnItem();
            PstMatConReturnItem pstMatConReturnItem = new PstMatConReturnItem(oid);
            matreturnitem.setOID(oid);
            
            matreturnitem.setReturnMaterialId(pstMatConReturnItem.getlong(FLD_RETURN_MATERIAL_ID));
            matreturnitem.setMaterialId(pstMatConReturnItem.getlong(FLD_MATERIAL_ID));
            matreturnitem.setUnitId(pstMatConReturnItem.getlong(FLD_UNIT_ID));
            matreturnitem.setCost(pstMatConReturnItem.getdouble(FLD_COST));
            matreturnitem.setCurrencyId(pstMatConReturnItem.getlong(FLD_CURRENCY_ID));
            matreturnitem.setQty(pstMatConReturnItem.getdouble(FLD_QTY));
            matreturnitem.setTotal(pstMatConReturnItem.getdouble(FLD_TOTAL));
            matreturnitem.setResidueQty(pstMatConReturnItem.getdouble(FLD_RESIDUE_QTY));
            
            return matreturnitem;
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatConReturnItem(0),DBException.UNKNOWN);
        }
    }
    
    synchronized public static long insertExc(MatConReturnItem matreturnitem) throws DBException {
        try {
            PstMatConReturnItem pstMatConReturnItem = new PstMatConReturnItem(0);
            
            pstMatConReturnItem.setLong(FLD_RETURN_MATERIAL_ID, matreturnitem.getReturnMaterialId());
            pstMatConReturnItem.setLong(FLD_MATERIAL_ID, matreturnitem.getMaterialId());
            pstMatConReturnItem.setLong(FLD_UNIT_ID, matreturnitem.getUnitId());
            pstMatConReturnItem.setDouble(FLD_COST, matreturnitem.getCost());
            pstMatConReturnItem.setLong(FLD_CURRENCY_ID, matreturnitem.getCurrencyId());
            pstMatConReturnItem.setDouble(FLD_QTY, matreturnitem.getQty());
            pstMatConReturnItem.setDouble(FLD_TOTAL, matreturnitem.getTotal());
            pstMatConReturnItem.setDouble(FLD_RESIDUE_QTY, matreturnitem.getResidueQty());
            
            pstMatConReturnItem.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatConReturnItem.getInsertSQL());
            matreturnitem.setOID(pstMatConReturnItem.getlong(FLD_RETURN_MATERIAL_ITEM_ID));
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatConReturnItem(0),DBException.UNKNOWN);
        }
        return matreturnitem.getOID();
    }
    
   synchronized public static long updateExc(MatConReturnItem matreturnitem) throws DBException {
        try {
            if(matreturnitem.getOID() != 0) {
                PstMatConReturnItem pstMatConReturnItem = new PstMatConReturnItem(matreturnitem.getOID());
                
                pstMatConReturnItem.setLong(FLD_RETURN_MATERIAL_ID, matreturnitem.getReturnMaterialId());
                pstMatConReturnItem.setLong(FLD_MATERIAL_ID, matreturnitem.getMaterialId());
                pstMatConReturnItem.setLong(FLD_UNIT_ID, matreturnitem.getUnitId());
                pstMatConReturnItem.setDouble(FLD_COST, matreturnitem.getCost());
                pstMatConReturnItem.setLong(FLD_CURRENCY_ID, matreturnitem.getCurrencyId());
                pstMatConReturnItem.setDouble(FLD_QTY, matreturnitem.getQty());
                pstMatConReturnItem.setDouble(FLD_TOTAL, matreturnitem.getTotal());
                pstMatConReturnItem.setDouble(FLD_RESIDUE_QTY, matreturnitem.getResidueQty());
                
                pstMatConReturnItem.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMatConReturnItem.getUpdateSQL());
                return matreturnitem.getOID();
                
            }
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatConReturnItem(0),DBException.UNKNOWN);
        }
        return 0;
    }
    
   synchronized public static long deleteExc(long oid) throws DBException{
        try{
            PstMatConReturnItem pstMatConReturnItem = new PstMatConReturnItem(oid);
            pstMatConReturnItem.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatConReturnItem.getDeleteSQL());
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstMatConReturnItem(0),DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static Vector listAll(){
        return list(0, 500, "","");
    }
    
    public static Vector list(int limitStart,int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_RETURN_ITEM;
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
                MatConReturnItem matreturnitem = new MatConReturnItem();
                resultToObject(rs, matreturnitem);
                lists.add(matreturnitem);
            }
            rs.close();
            
        }
        catch(Exception e) {
            System.out.println(e);
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }
    
    public static Vector list(int limitStart,int recordToGet, String whereClause) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT RMI." + fieldNames[FLD_RETURN_MATERIAL_ITEM_ID] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            " , UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            " , RMI." + fieldNames[FLD_COST] +
            " , CUR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE] +
            " , RMI." + fieldNames[FLD_QTY] +
            " , RMI." + fieldNames[FLD_TOTAL] +
            " , RMI." + fieldNames[FLD_MATERIAL_ID] +
            " , RMI." + fieldNames[FLD_UNIT_ID] +
            " , RMI." + fieldNames[FLD_CURRENCY_ID] +
            " , RMI." + fieldNames[FLD_RESIDUE_QTY] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_REQUIRED_SERIAL_NUMBER] +
            " FROM ((" + TBL_MAT_RETURN_ITEM + " RMI" +
            " LEFT JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON RMI." + fieldNames[FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ")" +
            " LEFT JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON RMI." + fieldNames[FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] + ")" +
            " LEFT JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CUR" +
            " ON RMI." + fieldNames[FLD_CURRENCY_ID] +
            " = CUR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID];
            
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            
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
                MatConReturnItem matreturnitem = new MatConReturnItem();
                Material mat = new Material();
                Unit unit = new Unit();
                CurrencyType cur = new CurrencyType();
                
                matreturnitem.setOID(rs.getLong(1));
                matreturnitem.setCost(rs.getDouble(5));
                matreturnitem.setQty(rs.getDouble(7));
                matreturnitem.setTotal(rs.getDouble(8));
                matreturnitem.setMaterialId(rs.getLong(9));
                matreturnitem.setUnitId(rs.getLong(10));
                matreturnitem.setCurrencyId(rs.getLong(11));
                matreturnitem.setResidueQty(rs.getDouble(12));
                temp.add(matreturnitem);
                
                mat.setSku(rs.getString(2));
                mat.setName(rs.getString(3));
                mat.setDefaultPrice(rs.getDouble(13));
                mat.setDefaultCost(rs.getDouble(14));
                mat.setRequiredSerialNumber(rs.getInt(15));
                temp.add(mat);
                
                unit.setCode(rs.getString(4));
                temp.add(unit);
                
                cur.setCode(rs.getString(6));
                temp.add(cur);
                
                lists.add(temp);
            }
            rs.close();
            
        }
        catch(Exception e) {
            System.out.println(e);
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }
    
    public static void resultToObject(ResultSet rs, MatConReturnItem matreturnitem) {
        try {
            matreturnitem.setOID(rs.getLong(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_RETURN_MATERIAL_ITEM_ID]));
            matreturnitem.setReturnMaterialId(rs.getLong(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_RETURN_MATERIAL_ID]));
            matreturnitem.setMaterialId(rs.getLong(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_MATERIAL_ID]));
            matreturnitem.setUnitId(rs.getLong(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_UNIT_ID]));
            matreturnitem.setCost(rs.getDouble(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_COST]));
            matreturnitem.setCurrencyId(rs.getLong(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_CURRENCY_ID]));
            matreturnitem.setQty(rs.getDouble(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_QTY]));
            matreturnitem.setTotal(rs.getDouble(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_TOTAL]));
            matreturnitem.setResidueQty(rs.getDouble(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_RESIDUE_QTY]));
        }
        catch(Exception e)
        { }
    }
    
    public static boolean checkOID(long matReturnItemId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_RETURN_ITEM +
            " WHERE " + fieldNames[FLD_RETURN_MATERIAL_ITEM_ID] +
            " = " + matReturnItemId;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                result = true;
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println("err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT("+ fieldNames[FLD_RETURN_MATERIAL_ITEM_ID] +
            ") FROM " + TBL_MAT_RETURN_ITEM;
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
    
    
    /* This method used to find current data */
    public static int findLimitStart( long oid, int recordToGet, String whereClause, String orderClause){
        int size = getCount(whereClause);
        int start = 0;
        boolean found =false;
        for(int i=0; (i < size) && !found ; i=i+recordToGet){
            Vector list =  list(i,recordToGet, whereClause, orderClause);
            start = i;
            if(list.size()>0){
                for(int ls=0;ls<list.size();ls++){
                    MatConReturnItem matreturnitem = (MatConReturnItem)list.get(ls);
                    if(oid == matreturnitem.getOID())
                        found=true;
                }
            }
        }
        if((start >= size) && (size > 0))
            start = start - recordToGet;
        
        return start;
    }
    /* This method used to find command where current data */
    public static int findLimitCommand(int start, int recordToGet, int vectSize){
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + (recordToGet - mdl);
        if(start == 0)
            cmd =  Command.FIRST;
        else{
            if(start == (vectSize-recordToGet))
                cmd = Command.LAST;
            else{
                start = start + recordToGet;
                if(start <= (vectSize - recordToGet)){
                    cmd = Command.NEXT;
                }else{
                    start = start - recordToGet;
                    if(start > 0){
                        cmd = Command.PREV;
                    }
                }
            }
        }
        
        return cmd;
    }
    
    
    /**
     * this method used to check if specify material already exist in current returnItem
     * return "true" ---> if material already exist in returnItem
     * return "false" ---> if material not available in returnItem
     */
    public static boolean materialExist(long oidMaterial, long oidReturnMaterial) {
        DBResultSet dbrs = null;
        boolean bool = false;
        try {
            String sql = "SELECT ITM." + fieldNames[FLD_MATERIAL_ID] +
            " FROM " + TBL_MAT_RETURN_ITEM + " AS ITM " +
            " INNER JOIN " + PstMatConReturn.TBL_MAT_RETURN + " AS MAT " +
            " ON ITM." + fieldNames[FLD_RETURN_MATERIAL_ID] +
            " = MAT." + PstMatConReturn.fieldNames[PstMatConReturn.FLD_RETURN_MATERIAL_ID] +
            " WHERE ITM." + fieldNames[FLD_RETURN_MATERIAL_ID] + "=" + oidReturnMaterial +
            " AND ITM." + fieldNames[FLD_MATERIAL_ID] + "=" + oidMaterial;
            dbrs  = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                bool = true;
                break;
            }
        }
        catch(Exception e) {
            System.out.println("PstMatConReturnItem.materialExist.err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return bool;
    }
    
    public static double getTotal(String whereClause) {
        DBResultSet dbrs = null;
        double amount = 0;
        try {
            String sql = "SELECT SUM("+ fieldNames[FLD_TOTAL] +
            ") AS MNT FROM " + TBL_MAT_RETURN_ITEM;
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                amount = rs.getDouble("MNT");
            }
            
            rs.close();
        }
        catch(Exception e) {
            return 0;
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return amount;
    }
    
    public static long deleteExcByParent(long oid) throws DBException {
        long hasil = 0;
        try {
            String sql = "DELETE FROM " + TBL_MAT_RETURN_ITEM+
            " WHERE " + fieldNames[FLD_RETURN_MATERIAL_ID] +
            " = "  + oid;
            int result = execUpdate(sql);
            hasil = oid;
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatConReturnItem(0),DBException.UNKNOWN);
        }
        return hasil;
    }
    
    /**
     * untuk mencari object return item
     * @param oidMaterial
     * @param oidReturn
     * @return
     */    
    public static MatConReturnItem getObjectReturnItem(long oidMaterial, long oidReturn){
        String whereClause = fieldNames[FLD_RETURN_MATERIAL_ID]+"="+oidReturn+
            " AND "+fieldNames[FLD_MATERIAL_ID]+"="+oidMaterial;
        Vector vt = list(0,0,whereClause,"");
        
        MatConReturnItem matReturnItem = new MatConReturnItem();
        if(vt!=null && vt.size()>0)
            matReturnItem = (MatConReturnItem)vt.get(0);
        
        return matReturnItem;
    }
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MatConReturnItem matConReturnItem = PstMatConReturnItem.fetchExc(oid);
         object.put(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_RETURN_MATERIAL_ITEM_ID], matConReturnItem.getOID());
         object.put(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_RETURN_MATERIAL_ID], matConReturnItem.getReturnMaterialId());
         object.put(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_MATERIAL_ID], matConReturnItem.getMaterialId());
         object.put(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_UNIT_ID], matConReturnItem.getUnitId());
         object.put(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_COST], matConReturnItem.getCost());
         object.put(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_CURRENCY_ID], matConReturnItem.getCurrencyId());
         object.put(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_QTY], matConReturnItem.getQty());
         object.put(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_TOTAL], matConReturnItem.getTotal());
         object.put(PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_RESIDUE_QTY], matConReturnItem.getResidueQty());
      }catch(Exception exc){}
      return object;
   }
}
