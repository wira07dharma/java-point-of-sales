package com.dimata.posbo.entity.warehouse;

/*
import java.sql.*;
import java.util.*;
import java.lang.*;
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.posbo.entity.masterdata.*;*/
import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import java.util.Vector;
import com.dimata.util.Command;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.*;
import org.json.JSONObject;

public class PstMatCostingItem extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final  String TBL_MAT_COSTING_ITEM = "pos_costing_material_item";

    public static final  int FLD_COSTING_MATERIAL_ITEM_ID  = 0;
    public static final  int FLD_COSTING_MATERIAL_ID       = 1;
    public static final  int FLD_MATERIAL_ID               = 2;
    public static final  int FLD_UNIT_ID                   = 3;
    public static final  int FLD_QTY                       = 4;
    public static final  int FLD_RESIDUE_QTY               = 5;
    public static final  int FLD_HPP                       = 6;  
    public static final int FLD_BALANCE_QTY                = 7;
    public static final int FLD_QTY_COMPOSITE              = 8;
    public static final int FLD_PARENT_ID                  = 9;
    public static final int FLD_SPESIAL_NOTE               = 10;
    public static final int FLD_WEIGHT                     = 11;
    public static final int FLD_COST                       = 12;

    public static final  String[] fieldNames = {
        "COSTING_MATERIAL_ITEM_ID",
        "COSTING_MATERIAL_ID",
        "MATERIAL_ID",
        "UNIT_ID",
        "QTY",
        "RESIDUE_QTY",
        "HPP",
        "BALANCE_QTY",
        "QTY_COMPOSITE",
        "PARENT_ID",
        "SPESIAL_NOTE",
        "WEIGHT",
        "COST",
    };

    public static final  int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_FLOAT
        
    };

    public PstMatCostingItem() {
    }

    public PstMatCostingItem(int i) throws DBException {
        super(new PstMatCostingItem());
    }

    public PstMatCostingItem(String sOid) throws DBException {
        super(new PstMatCostingItem(0));
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstMatCostingItem(long lOid) throws DBException {
        super(new PstMatCostingItem(0));
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
        return TBL_MAT_COSTING_ITEM;
    }

    public String[] getFieldNames(){
        return fieldNames;
    }

    public int[] getFieldTypes(){
        return fieldTypes;
    }

    public String getPersistentName(){
        return new PstMatCostingItem().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception{
        MatCostingItem matCostingItem = fetchExc(ent.getOID());
        ent = (Entity)matCostingItem;
        return matCostingItem.getOID();
    }

    synchronized public long insertExc(Entity ent) throws Exception{
        return insertExc((MatCostingItem) ent);
    }

    synchronized public long updateExc(Entity ent) throws Exception{
        return updateExc((MatCostingItem) ent);
    }

    synchronized public long deleteExc(Entity ent) throws Exception{
        if(ent==null){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static MatCostingItem fetchExc(long oid) throws DBException {
        try {
            MatCostingItem matCostingItem = new MatCostingItem();
            PstMatCostingItem pstMatCostingItem = new PstMatCostingItem(oid);
            matCostingItem.setOID(oid);

            matCostingItem.setCostingMaterialId(pstMatCostingItem.getlong(FLD_COSTING_MATERIAL_ID));
            matCostingItem.setMaterialId(pstMatCostingItem.getlong(FLD_MATERIAL_ID));
            matCostingItem.setUnitId(pstMatCostingItem.getlong(FLD_UNIT_ID));
            matCostingItem.setQty(pstMatCostingItem.getdouble(FLD_QTY));
            matCostingItem.setResidueQty(pstMatCostingItem.getdouble(FLD_RESIDUE_QTY));
            matCostingItem.setHpp(pstMatCostingItem.getdouble(FLD_HPP));
           //adding qty balance
           //by mirahu 30032012
            matCostingItem.setBalanceQty(pstMatCostingItem.getdouble(FLD_BALANCE_QTY));
            matCostingItem.setQtyComposite(pstMatCostingItem.getdouble(FLD_QTY_COMPOSITE));
            matCostingItem.setParentId(pstMatCostingItem.getlong(FLD_PARENT_ID));
			matCostingItem.setSpesialNote(pstMatCostingItem.getString(FLD_SPESIAL_NOTE));
			matCostingItem.setWeight(pstMatCostingItem.getdouble(FLD_WEIGHT));
			matCostingItem.setCost(pstMatCostingItem.getdouble(FLD_COST));
            
            
            return matCostingItem;
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatCostingItem(0),DBException.UNKNOWN);
        }
    }

    public static long insertExc(MatCostingItem matCostingItem) throws DBException {
        try {
            PstMatCostingItem pstMatCostingItem = new PstMatCostingItem(0);

            pstMatCostingItem.setLong(FLD_COSTING_MATERIAL_ID, matCostingItem.getDispatchMaterialId());
            pstMatCostingItem.setLong(FLD_MATERIAL_ID, matCostingItem.getMaterialId());
            pstMatCostingItem.setLong(FLD_UNIT_ID, matCostingItem.getUnitId());
            pstMatCostingItem.setDouble(FLD_QTY, matCostingItem.getQty());
            pstMatCostingItem.setDouble(FLD_RESIDUE_QTY, matCostingItem.getResidueQty());
            pstMatCostingItem.setDouble(FLD_HPP, matCostingItem.getHpp());
           //adding qty balance
           //by mirahu 30032012
            pstMatCostingItem.setDouble(FLD_BALANCE_QTY , matCostingItem.getBalanceQty());
            pstMatCostingItem.setDouble(FLD_QTY_COMPOSITE, matCostingItem.getQtyComposite());
            pstMatCostingItem.setLong(FLD_PARENT_ID, matCostingItem.getParentId());
			pstMatCostingItem.setString(FLD_SPESIAL_NOTE, matCostingItem.getSpesialNote());
			pstMatCostingItem.setDouble(FLD_WEIGHT, matCostingItem.getWeight());
			pstMatCostingItem.setDouble(FLD_COST, matCostingItem.getCost());
            
            pstMatCostingItem.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatCostingItem.getInsertSQL());
            matCostingItem.setOID(pstMatCostingItem.getlong(FLD_COSTING_MATERIAL_ITEM_ID));
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatCostingItem(0),DBException.UNKNOWN);
        }
        return matCostingItem.getOID();
    }
    
    
    public static long insertExcByOid(MatCostingItem matCostingItem) throws DBException {
        try {
            PstMatCostingItem pstMatCostingItem = new PstMatCostingItem(0);

            pstMatCostingItem.setLong(FLD_COSTING_MATERIAL_ID, matCostingItem.getDispatchMaterialId());
            pstMatCostingItem.setLong(FLD_MATERIAL_ID, matCostingItem.getMaterialId());
            pstMatCostingItem.setLong(FLD_UNIT_ID, matCostingItem.getUnitId());
            pstMatCostingItem.setDouble(FLD_QTY, matCostingItem.getQty());
            pstMatCostingItem.setDouble(FLD_RESIDUE_QTY, matCostingItem.getResidueQty());
            pstMatCostingItem.setDouble(FLD_HPP, matCostingItem.getHpp());
           //adding qty balance
           //by mirahu 30032012
            pstMatCostingItem.setDouble(FLD_BALANCE_QTY , matCostingItem.getBalanceQty());
            pstMatCostingItem.setDouble(FLD_QTY_COMPOSITE, matCostingItem.getQtyComposite());
            pstMatCostingItem.setLong(FLD_PARENT_ID, matCostingItem.getParentId());
			pstMatCostingItem.setString(FLD_SPESIAL_NOTE, matCostingItem.getSpesialNote());
			pstMatCostingItem.setDouble(FLD_WEIGHT, matCostingItem.getWeight());
			pstMatCostingItem.setDouble(FLD_COST, matCostingItem.getCost());
            
            pstMatCostingItem.insertByOid(matCostingItem.getOID());
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatCostingItem(0),DBException.UNKNOWN);
        }
        return matCostingItem.getOID();
    }
    

    public static long updateExc(MatCostingItem matCostingItem) throws DBException {
        long result = 0;
        try {
            if(matCostingItem.getOID() != 0) {
                PstMatCostingItem pstMatCostingItem = new PstMatCostingItem(matCostingItem.getOID());

                pstMatCostingItem.setLong(FLD_COSTING_MATERIAL_ID, matCostingItem.getDispatchMaterialId());
                pstMatCostingItem.setLong(FLD_MATERIAL_ID, matCostingItem.getMaterialId());
                pstMatCostingItem.setLong(FLD_UNIT_ID, matCostingItem.getUnitId());
                pstMatCostingItem.setDouble(FLD_QTY, matCostingItem.getQty());
                pstMatCostingItem.setDouble(FLD_RESIDUE_QTY, matCostingItem.getResidueQty());
                pstMatCostingItem.setDouble(FLD_HPP, matCostingItem.getHpp());
                 //adding qty balance
                //by mirahu 30032012
                pstMatCostingItem.setDouble(FLD_BALANCE_QTY, matCostingItem.getBalanceQty());
                pstMatCostingItem.setDouble(FLD_QTY_COMPOSITE, matCostingItem.getQtyComposite());
                pstMatCostingItem.setLong(FLD_PARENT_ID, matCostingItem.getParentId());
				pstMatCostingItem.setString(FLD_SPESIAL_NOTE, matCostingItem.getSpesialNote());
				pstMatCostingItem.setDouble(FLD_WEIGHT, matCostingItem.getWeight());
				pstMatCostingItem.setDouble(FLD_COST, matCostingItem.getCost());
                
                pstMatCostingItem.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMatCostingItem.getUpdateSQL());
                result = matCostingItem.getOID();
            }
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatCostingItem(0),DBException.UNKNOWN);
        }
        return result;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstMatCostingItem pstMatCostingItem = new PstMatCostingItem(oid);
            pstMatCostingItem.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatCostingItem.getDeleteSQL());
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatCostingItem(0),DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart,int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_COSTING_ITEM;
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
                MatCostingItem matCostingItem = new MatCostingItem();
                resultToObject(rs, matCostingItem);
                lists.add(matCostingItem);
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
        return list(limitStart,recordToGet,oidMatDispatch,"");
    }
    
    //Untuk menampilkan komplit dengan material dan unit
    public static Vector list(int limitStart,int recordToGet, long oidMatDispatch, String where) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DFI." + fieldNames[FLD_COSTING_MATERIAL_ITEM_ID] +
            " , DFI." + fieldNames[FLD_MATERIAL_ID] +
            " , DFI." + fieldNames[FLD_UNIT_ID] +
            " , DFI." + fieldNames[FLD_QTY] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            " , UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_REQUIRED_SERIAL_NUMBER] +
            " , DFI." + fieldNames[FLD_HPP] +
            //for composit
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE]+
             //adding stock balance and stock fisik by mirahu 02042012
            " , DFI." + fieldNames[FLD_RESIDUE_QTY] +
            " , DFI." + fieldNames[FLD_BALANCE_QTY] +
             //adding barcode
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] +
            ", DFI." + fieldNames[FLD_QTY_COMPOSITE] +        
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
            ", MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " , DFI." + fieldNames[FLD_WEIGHT] +
            " , DFI." + fieldNames[FLD_COST] +
            " FROM (" + TBL_MAT_COSTING_ITEM + " DFI" +
            " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON DFI." + fieldNames[FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ")" +
            " INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON DFI." + fieldNames[FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " WHERE DFI." + fieldNames[FLD_COSTING_MATERIAL_ID] +
            " = " + oidMatDispatch;
            
            if(where!=""){
                sql = sql+" AND " + where;
            }
             
            sql = sql+ " ORDER BY MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];

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
                MatCostingItem dfItem = new MatCostingItem();
                Material mat = new Material();
                Unit unit = new Unit();

                dfItem.setOID(rs.getLong(1));
                dfItem.setMaterialId(rs.getLong(2));
                dfItem.setUnitId(rs.getLong(3));
                dfItem.setQty(rs.getDouble(4));
                dfItem.setHpp(rs.getDouble(12));
                //adding stock balance & stock fisik by mirahu 02042012
                dfItem.setResidueQty(rs.getDouble(14));
                dfItem.setBalanceQty(rs.getDouble(15));
                dfItem.setWeight(rs.getDouble(20));
                dfItem.setCost(rs.getDouble(21));
                dfItem.setQtyComposite(rs.getDouble(fieldNames[FLD_QTY_COMPOSITE]));
                
                //jika dia composite cari hpp dan totalnya
                if(rs.getInt(13)==PstMaterial.MAT_TYPE_COMPOSITE || rs.getInt(13)==PstMaterial.MAT_TYPE_SERVICE){
                    //cek ada component atau tidak
                    int withcomponent = PstMaterialComposit.getCount("MC."+ PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID]+"='"+dfItem.getMaterialId()+"'");
                    if(withcomponent==0){
                        dfItem.setHppComposite(dfItem.getHpp());
                        dfItem.setTotalHppComposite(dfItem.getHpp()* dfItem.getQty());
                    }else{
                        String whereSum = PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_PARENT_ID]+"="+dfItem.getOID();
                            Vector vect = getSumHpp(whereSum);
                            if(vect.size()!=0&&vect.size()==2){
                                double hpp = (Double) vect.get(0);
                                double total =(Double) vect.get(1);
                                dfItem.setHppComposite(hpp);
                                dfItem.setTotalHppComposite(total);
                            }
                    }
                }
                
                temp.add(dfItem);

                mat.setSku(rs.getString(5));
                mat.setName(rs.getString(6));
                mat.setDefaultCost(rs.getDouble(8));
                mat.setDefaultCostCurrencyId(rs.getLong(9));
                mat.setDefaultPrice(rs.getDouble(10));
                mat.setRequiredSerialNumber(rs.getInt(11));
                mat.setMaterialType(rs.getInt(13));
                mat.setBarCode(rs.getString(16));
                mat.setDefaultStockUnitId(rs.getLong(18));
                mat.setOID(rs.getLong(19));
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

    //Untuk menampilkan komplit dengan material dan unit
    public static Vector list(String whereClause, String orderClause) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DFI." + fieldNames[FLD_COSTING_MATERIAL_ID] +
            " , DFI." + fieldNames[FLD_MATERIAL_ID] +
            " , DFI." + fieldNames[FLD_UNIT_ID] +
            " , DFI." + fieldNames[FLD_QTY] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
            " , UNT." + PstUnit.fieldNames[PstUnit.FLD_CODE] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
            " , MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] +
            " , CURR." + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CODE] +
                    " , DFI." + fieldNames[FLD_HPP] +
            " FROM ((" + TBL_MAT_COSTING_ITEM + " DFI" +
            " INNER JOIN " + PstMaterial.TBL_MATERIAL + " MAT" +
            " ON DFI." + fieldNames[FLD_MATERIAL_ID] +
            " = MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
            " ) INNER JOIN " + PstUnit.TBL_P2_UNIT + " UNT" +
            " ON DFI." + fieldNames[FLD_UNIT_ID] +
            " = UNT." + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
            " ) INNER JOIN " + PstMatCurrency.TBL_CURRENCY + " CURR" +
            " ON MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] +
            " = CURR." + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CURRENCY_ID];

            if (whereClause.length() > 0)
                sql += " WHERE " + whereClause;

            if (orderClause.length() > 0)
                sql += " ORDER BY " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                Vector temp = new Vector();
                MatCostingItem dfItem = new MatCostingItem();
                Material mat = new Material();
                Unit unit = new Unit();
                MatCurrency curr = new MatCurrency();

                dfItem.setOID(rs.getLong(1));
                dfItem.setMaterialId(rs.getLong(2));
                dfItem.setUnitId(rs.getLong(3));
                dfItem.setQty(rs.getDouble(4));
                dfItem.setHpp(rs.getDouble(11));
                temp.add(dfItem);

                mat.setSku(rs.getString(5));
                mat.setName(rs.getString(6));
                mat.setDefaultCost(rs.getDouble(8));
                mat.setDefaultCostCurrencyId(rs.getLong(9));
                temp.add(mat);

                unit.setCode(rs.getString(7));
                temp.add(unit);

                curr.setCode(rs.getString(10));
                temp.add(curr);

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

    public static void resultToObject(ResultSet rs, MatCostingItem matCostingItem) {
        try {
            matCostingItem.setOID(rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ITEM_ID]));
            matCostingItem.setCostingMaterialId(rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID]));
            matCostingItem.setMaterialId(rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID]));
            matCostingItem.setUnitId(rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID]));
            matCostingItem.setQty(rs.getDouble(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY]));
            matCostingItem.setResidueQty(rs.getDouble(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_RESIDUE_QTY]));
            matCostingItem.setHpp(rs.getDouble(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_HPP]));
            //adding stok balance
            matCostingItem.setBalanceQty(rs.getDouble(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_BALANCE_QTY]));
            matCostingItem.setQtyComposite(rs.getDouble(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY_COMPOSITE]));
            matCostingItem.setParentId(rs.getLong(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_PARENT_ID]));
			matCostingItem.setSpesialNote(rs.getString(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_SPESIAL_NOTE]));
			matCostingItem.setWeight(rs.getDouble(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_WEIGHT]));
			matCostingItem.setCost(rs.getDouble(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COST]));
        }
        catch(Exception e)
        { }
    }

    public static boolean checkOID(long materialDispatchItemId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_COSTING_ITEM +
            " WHERE " + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ITEM_ID] +
            " = " + materialDispatchItemId;

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
        int count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT("+ PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ITEM_ID] +
            ") AS CNT FROM " + TBL_MAT_COSTING_ITEM;

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

    public static Vector getSumHpp(String whereClause) {
        //Vector sumHpp = new Vector();
        Vector sumHpp = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM("+ PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_HPP] +
            ") AS SUMHPP,  SUM("+ PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_HPP] +"*"+ PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY]+ ") TOTAL FROM " + TBL_MAT_COSTING_ITEM;

            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                sumHpp.add(rs.getDouble("SUMHPP"));
                sumHpp.add(rs.getDouble("TOTAL"));
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return sumHpp;
    }

    /* This method used to find current data */
    public static int findLimitStart( long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found =false;
        for(int i=0; (i < size) && !found ; i=i+recordToGet){
            Vector list =  list(i,recordToGet, whereClause, orderClause);
            start = i;
            if(list.size()>0){
                for(int ls=0;ls<list.size();ls++){
                    MatCostingItem matCostingItem = (MatCostingItem)list.get(ls);
                    if(oid == matCostingItem.getOID())
                        found=true;
                }
            }
        }
        if((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }
    /* This method used to find command where current data */
    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
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
                    //System.out.println("next.......................");
                }else{
                    start = start - recordToGet;
                    if(start > 0){
                        cmd = Command.PREV;
                        //System.out.println("prev.......................");
                    }
                }
            }
        }

        return cmd;
    }

    public static long deleteExcByParent(long oid) throws DBException {
        long hasil = 0;
        try {
            if(oid!=0){
                String sql = "DELETE FROM " + TBL_MAT_COSTING_ITEM+
                " WHERE " + fieldNames[FLD_COSTING_MATERIAL_ID] +
                " = "  + oid;
                int result = execUpdate(sql);
                hasil = oid;
            }
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatCostingItem(0),DBException.UNKNOWN);
        }
        return hasil;
    }
    
    public static long deleteComponentByParentId(long oid) throws DBException {
        long hasil = 0;
        try {
            if(oid!=0){
                String sql = "DELETE FROM " + TBL_MAT_COSTING_ITEM+
                " WHERE " + fieldNames[FLD_PARENT_ID] +
                " = "  + oid;
                int result = execUpdate(sql);
                hasil = oid;
            }
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMatCostingItem(0),DBException.UNKNOWN);
        }
        return hasil;
    }

    /** di gunakan untuk mencari object receive item
     * @param invoice = invoice supplier
     * @param oidMaterial = oid material/barang
     * @return
     */
    public static MatCostingItem getObjectDispatchItem(long oidMaterial, long oidDispatch){
        MatCostingItem matDispatchItem = new MatCostingItem();
        try{
            String whereClause = PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID]+"="+oidDispatch+
                                 " AND "+PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID]+"="+oidMaterial;
            Vector vect = PstMatCostingItem.list(0,0,whereClause,"");
            if(vect!=null && vect.size()>0){
                matDispatchItem = (MatCostingItem)vect.get(0);
            }

        }catch(Exception e){}
        return matDispatchItem;
    }
    
    /**
     * gadnyana
     * get total costing ( hpp )
     * @param oidCosting
     * @return
     */
    public static double getTotalCosting(long oidCosting) {
        double total = 0.0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM("+fieldNames[FLD_HPP]+" *"+fieldNames[FLD_QTY]+") FROM "+TBL_MAT_COSTING_ITEM+
                    " WHERE "+fieldNames[FLD_COSTING_MATERIAL_ID]+"="+oidCosting+
                    " GROUP BY "+fieldNames[FLD_COSTING_MATERIAL_ID];
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                total = rs.getDouble(1);
            }
        }catch(Exception e){

        }
        return total;
    }
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MatCostingItem matCostingItem = PstMatCostingItem.fetchExc(oid);
         object.put(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ITEM_ID], matCostingItem.getOID());
         object.put(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID], matCostingItem.getDispatchMaterialId());
         object.put(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_MATERIAL_ID], matCostingItem.getMaterialId());
         object.put(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_UNIT_ID], matCostingItem.getUnitId());
         object.put(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY], matCostingItem.getQty());
         object.put(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_RESIDUE_QTY], matCostingItem.getResidueQty());
         object.put(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_HPP], matCostingItem.getHpp());
         object.put(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_BALANCE_QTY], matCostingItem.getBalanceQty());
         object.put(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_QTY_COMPOSITE], matCostingItem.getQtyComposite());
         object.put(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_PARENT_ID], matCostingItem.getParentId());
         object.put(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_SPESIAL_NOTE], matCostingItem.getSpesialNote());
         object.put(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_WEIGHT], matCostingItem.getWeight());
         object.put(PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COST], matCostingItem.getCost());
      }catch(Exception exc){}
      return object;
   }
}
