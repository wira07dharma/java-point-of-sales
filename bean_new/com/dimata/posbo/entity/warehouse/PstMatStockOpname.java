package com.dimata.posbo.entity.warehouse;

import com.dimata.common.entity.custom.PstDataCustom;
import java.sql.*;
import java.util.*;
import java.util.Date;
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.common.entity.periode.PstPeriode;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import org.json.JSONObject;


public class PstMatStockOpname extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_Document {

    public static final String TBL_MAT_STOCK_OPNAME = "pos_stock_opname";

    public static final int FLD_STOCK_OPNAME_ID = 0;
    public static final int FLD_LOCATION_ID = 1;
    public static final int FLD_STOCK_OPNAME_DATE = 2;
    public static final int FLD_STOCK_OPNAME_TIME = 3;
    public static final int FLD_STOCK_OPNAME_NUMBER = 4;
    public static final int FLD_STOCK_OPNAME_STATUS = 5;
    public static final int FLD_SUPPLIER_ID = 6;
    public static final int FLD_CATEGORY_ID = 7;
    public static final int FLD_SUB_CATEGORY_ID = 8;
    public static final int FLD_REMARK = 9;
    public static final int FLD_ETALASE_ID = 10;
    public static final int FLD_OPNAME_ITEM_TYPE = 11;
    public static final int FLD_CODE_COUNTER = 12;
    public static final int FLD_MATERIAL_TYPE_ID = 13;

    public static final String[] fieldNames =
            {
                "STOCK_OPNAME_ID",
                "LOCATION_ID",
                "STOCK_OPNAME_DATE",
                "STOCK_OPNAME_TIME",
                "STOCK_OPNAME_NUMBER",
                "STOCK_OPNAME_STATUS",
                "SUPPLIER_ID",
                "CATEGORY_ID",
                "SUB_CATEGORY_ID",
                "REMARK",
                "ETALASE_ID",
                "OPNAME_ITEM_TYPE",
                "CODE_COUNTER",
                "MATERIAL_TYPE_ID"
            };

    public static final int[] fieldTypes =
            {
                TYPE_LONG + TYPE_PK + TYPE_ID,
                TYPE_LONG,
                TYPE_DATE,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_INT,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_STRING,
                TYPE_LONG,
                TYPE_INT,
                TYPE_INT,
                TYPE_LONG
            };

    public PstMatStockOpname() {
    }

    public PstMatStockOpname(int i) throws DBException {
        super(new PstMatStockOpname());
    }

    public PstMatStockOpname(String sOid) throws DBException {
        super(new PstMatStockOpname(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstMatStockOpname(long lOid) throws DBException {
        super(new PstMatStockOpname(0));
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
        return TBL_MAT_STOCK_OPNAME;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMatStockOpname().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        MatStockOpname matstockopname = fetchExc(ent.getOID());
        ent = (Entity) matstockopname;
        return matstockopname.getOID();
    }

    synchronized public long insertExc(Entity ent) throws Exception {
        return insertExc((MatStockOpname) ent);
    }

    synchronized public long updateExc(Entity ent) throws Exception {
        return updateExc((MatStockOpname) ent);
    }

    synchronized public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static MatStockOpname fetchExc(long oid) throws DBException {
        try {
            MatStockOpname matstockopname = new MatStockOpname();
            PstMatStockOpname pstMatStockOpname = new PstMatStockOpname(oid);
            matstockopname.setOID(oid);

            matstockopname.setLocationId(pstMatStockOpname.getlong(FLD_LOCATION_ID));
            matstockopname.setStockOpnameDate(pstMatStockOpname.getDate(FLD_STOCK_OPNAME_DATE));
            matstockopname.setStockOpnameTime(pstMatStockOpname.getString(FLD_STOCK_OPNAME_TIME));
            matstockopname.setStockOpnameNumber(pstMatStockOpname.getString(FLD_STOCK_OPNAME_NUMBER));
            matstockopname.setStockOpnameStatus(pstMatStockOpname.getInt(FLD_STOCK_OPNAME_STATUS));
            matstockopname.setSupplierId(pstMatStockOpname.getlong(FLD_SUPPLIER_ID));
            matstockopname.setCategoryId(pstMatStockOpname.getlong(FLD_CATEGORY_ID));
            matstockopname.setSubCategoryId(pstMatStockOpname.getlong(FLD_SUB_CATEGORY_ID));
            matstockopname.setRemark(pstMatStockOpname.getString(FLD_REMARK));
            matstockopname.setEtalaseId(pstMatStockOpname.getlong(FLD_ETALASE_ID));
            matstockopname.setOpnameItemType(pstMatStockOpname.getInt(FLD_OPNAME_ITEM_TYPE));
            matstockopname.setCodeCounter(pstMatStockOpname.getInt(FLD_CODE_COUNTER));
            matstockopname.setMasterGroupId(pstMatStockOpname.getlong(FLD_MATERIAL_TYPE_ID));

            return matstockopname;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatStockOpname(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(MatStockOpname matstockopname) throws DBException {
        try {
            PstMatStockOpname pstMatStockOpname = new PstMatStockOpname(0);
            

            pstMatStockOpname.setLong(FLD_LOCATION_ID, matstockopname.getLocationId());
            pstMatStockOpname.setDate(FLD_STOCK_OPNAME_DATE, matstockopname.getStockOpnameDate());
            pstMatStockOpname.setString(FLD_STOCK_OPNAME_TIME, matstockopname.getStockOpnameTime());
            pstMatStockOpname.setString(FLD_STOCK_OPNAME_NUMBER, matstockopname.getStockOpnameNumber());
            pstMatStockOpname.setInt(FLD_STOCK_OPNAME_STATUS, matstockopname.getStockOpnameStatus());
            pstMatStockOpname.setLong(FLD_SUPPLIER_ID, matstockopname.getSupplierId());
            pstMatStockOpname.setLong(FLD_CATEGORY_ID, matstockopname.getCategoryId());
            pstMatStockOpname.setLong(FLD_SUB_CATEGORY_ID, matstockopname.getSubCategoryId());
            pstMatStockOpname.setString(FLD_REMARK, matstockopname.getRemark());
            pstMatStockOpname.setLong(FLD_ETALASE_ID, matstockopname.getEtalaseId());
            pstMatStockOpname.setInt(FLD_OPNAME_ITEM_TYPE, matstockopname.getOpnameItemType());
            pstMatStockOpname.setInt(FLD_CODE_COUNTER, matstockopname.getCodeCounter());
            pstMatStockOpname.setLong(FLD_MATERIAL_TYPE_ID, matstockopname.getMasterGroupId());

            pstMatStockOpname.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatStockOpname.getInsertSQL());
            matstockopname.setOID(pstMatStockOpname.getlong(FLD_STOCK_OPNAME_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatStockOpname(0), DBException.UNKNOWN);
        }
        return matstockopname.getOID();
    }

    public static long updateExc(MatStockOpname matstockopname) throws DBException {
        try {
            if (matstockopname.getOID() != 0) {
                PstMatStockOpname pstMatStockOpname = new PstMatStockOpname(matstockopname.getOID());

                pstMatStockOpname.setLong(FLD_LOCATION_ID, matstockopname.getLocationId());
                pstMatStockOpname.setDate(FLD_STOCK_OPNAME_DATE, matstockopname.getStockOpnameDate());
                pstMatStockOpname.setString(FLD_STOCK_OPNAME_TIME, matstockopname.getStockOpnameTime());
                pstMatStockOpname.setString(FLD_STOCK_OPNAME_NUMBER, matstockopname.getStockOpnameNumber());
                pstMatStockOpname.setInt(FLD_STOCK_OPNAME_STATUS, matstockopname.getStockOpnameStatus());
                pstMatStockOpname.setLong(FLD_SUPPLIER_ID, matstockopname.getSupplierId());
                pstMatStockOpname.setLong(FLD_CATEGORY_ID, matstockopname.getCategoryId());
                pstMatStockOpname.setLong(FLD_SUB_CATEGORY_ID, matstockopname.getSubCategoryId());
                pstMatStockOpname.setString(FLD_REMARK, matstockopname.getRemark());
                pstMatStockOpname.setLong(FLD_ETALASE_ID, matstockopname.getEtalaseId());
                pstMatStockOpname.setInt(FLD_OPNAME_ITEM_TYPE, matstockopname.getOpnameItemType());
                pstMatStockOpname.setInt(FLD_CODE_COUNTER, matstockopname.getCodeCounter());
                pstMatStockOpname.setLong(FLD_MATERIAL_TYPE_ID, matstockopname.getMasterGroupId());

                pstMatStockOpname.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMatStockOpname.getUpdateSQL());
                return matstockopname.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatStockOpname(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstMatStockOpname pstMatStockOpname = new PstMatStockOpname(oid);
            //Delete Item First
            PstMatStockOpnameItem pstSOItem = new PstMatStockOpnameItem();
            long result = pstSOItem.deleteExcByParent(oid);
            pstMatStockOpname.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatStockOpname.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatStockOpname(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_STOCK_OPNAME;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;

                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;

                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    ;
            }


            /*if(limitStart == 0 && recordToGet == 0)
                sql = sql + "";
            else
                sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;*/

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MatStockOpname matstockopname = new MatStockOpname();
                resultToObject(rs, matstockopname);
                lists.add(matstockopname);
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

    public static void resultToObject(ResultSet rs, MatStockOpname matstockopname) {
        try {
            matstockopname.setOID(rs.getLong(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID]));
            matstockopname.setLocationId(rs.getLong(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID]));
            matstockopname.setStockOpnameDate(rs.getDate(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]));
            matstockopname.setStockOpnameTime(rs.getString(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_TIME]));
            matstockopname.setStockOpnameNumber(rs.getString(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER]));
            matstockopname.setStockOpnameStatus(rs.getInt(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS]));
            matstockopname.setSupplierId(rs.getLong(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUPPLIER_ID]));
            matstockopname.setCategoryId(rs.getLong(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CATEGORY_ID]));
            matstockopname.setSubCategoryId(rs.getLong(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUB_CATEGORY_ID]));
            matstockopname.setRemark(rs.getString(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_REMARK]));
            matstockopname.setEtalaseId(rs.getLong(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_ETALASE_ID]));
            matstockopname.setOpnameItemType(rs.getInt(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_OPNAME_ITEM_TYPE]));
            matstockopname.setCodeCounter(rs.getInt(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CODE_COUNTER]));
            matstockopname.setMasterGroupId(rs.getLong(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_MATERIAL_TYPE_ID]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long matStockOpnameId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_STOCK_OPNAME + " WHERE " +
                    PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] + " = " + matStockOpnameId;

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
        }
        return result;
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] + ") FROM " + TBL_MAT_STOCK_OPNAME;
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
    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    MatStockOpname matstockopname = (MatStockOpname) list.get(ls);
                    if (oid == matstockopname.getOID())
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


    /*-------------------- start implements I_Document -------------------------*/
    /**
     * this method used to get status of 'document'
     * return int of currentDocumentStatus
     */
    public int getDocumentStatus(long documentId) {
        DBResultSet dbrs = null;
        try {
            MatStockOpname matStockOpname = fetchExc(documentId);
            return matStockOpname.getStockOpnameStatus();
        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return -1;
    }

    /**
     * this method used to set status of 'document'
     * return int of currentDocumentStatus
     */
    public int setDocumentStatus(long documentId, int indexStatus) {
        /*DBResultSet dbrs = null;
		try{
			String sql = "UPDATE " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME +
                         " SET " + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + " = " + indexStatus +
                	     " WHERE " + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID] + " = " + documentId;
            dbrs = DBHandler.execQueryResult(sql);*/
        return indexStatus;
        /*}catch(Exception e){
            System.out.println("Err : "+e.toString());
        }finally{
            DBResultSet.close(dbrs);
        }
return -1;*/
    }
    /*-------------------- end implements I_Document --------------------------*/

    public static String generateStockOpnameNumber(MatStockOpname matstockopname) {
        DBResultSet dbrs = null;
	String hasil = "";
        try {
            /** get location code; gwawan@21juni2007 */
            Vector vctLocation = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"="+matstockopname.getLocationId(), "");
            Location location = (Location)vctLocation.get(0);
            
            String sql = "SELECT COUNT(" + fieldNames[FLD_STOCK_OPNAME_NUMBER] +
                    ") AS MAXNUM FROM " + TBL_MAT_STOCK_OPNAME;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
 
            int count = 1;
            while (rs.next()) {
                count = rs.getInt("MAXNUM");
		count = count + 1;   
            }
            rs.close();
	    
	    if(count >= 100) {
		hasil = String.valueOf(count);
	    }else if(count >= 10 ){
		hasil = String.valueOf("0"+count);
	    }else if(count >= 1 ){
		hasil = String.valueOf("00"+count);
	    }
	     
            hasil = location.getCode() + "-" + Formater.formatDate(new Date(), "yyMMdd") +"-SO-"+hasil;
        } catch (Exception e) {
            hasil = null;
            System.out.println("Generating SO Number : " + e.toString());
        }
        return hasil;
    }
    
    
    public static String generateStockOpnameNumberHarian(MatStockOpname matstockopname) {
        DBResultSet dbrs = null;
	String hasil = "";
        try {
            /** get location code; gwawan@21juni2007 */
            Vector vctLocation = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"="+matstockopname.getLocationId(), "");
            Location location = (Location)vctLocation.get(0);
            
            String sql = "SELECT COUNT(" + fieldNames[FLD_STOCK_OPNAME_NUMBER] +
                    ") AS MAXNUM FROM " + TBL_MAT_STOCK_OPNAME;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
 
            int count = 1;
            while (rs.next()) {
                count = rs.getInt("MAXNUM");
		count = count + 1;   
            }
            rs.close();
	    
	    if(count >= 100) {
		hasil = String.valueOf(count);
	    }else if(count >= 10 ){
		hasil = String.valueOf("0"+count);
	    }else if(count >= 1 ){
		hasil = String.valueOf("00"+count);
	    }
	     
            hasil = location.getCode() + "-" + Formater.formatDate(new Date(), "yyMMdd") +"-DAILYSO-"+hasil;
        } catch (Exception e) {
            hasil = null;
            System.out.println("Generating SO Number : " + e.toString());
        }
        return hasil;
    }


    
    //Process stock opname menjadi stock correction
    public static boolean StockCorrection(long oidStockOpname, long oidLocation) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            Vector tampung = new Vector();

            //Select OutStandingItem in Stock Opname Item
            String sql = "SELECT * FROM " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM +
                    " WHERE " + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] +
                    " = " + oidStockOpname; // +
            //" AND (QTY_SYSTEM - (QTY_SOLD + QTY_OPNAME)) <> 0";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                MatStockOpnameItem soi = new MatStockOpnameItem();
                soi.setOID(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ITEM_ID]));
                soi.setStockOpnameId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID]));
                soi.setMaterialId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID]));
                soi.setUnitId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_UNIT_ID]));
                soi.setQtyOpname(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME]));
                soi.setQtySold(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SOLD]));
                soi.setQtySystem(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SYSTEM]));
                tampung.add(soi);
            }
            rs.close();

            //Process OutStandingItem, update Material Stock
            hasil = true;
            for (int i = 0; i < tampung.size(); i++) {
                MatStockOpnameItem opnItem = (MatStockOpnameItem) tampung.get(i);
                long oidMaterialStock = PstMaterialStock.fetchByMaterialLocation(opnItem.getMaterialId(), oidLocation);
                if (oidMaterialStock != 0) {
                    MaterialStock matStock = PstMaterialStock.fetchExc(oidMaterialStock);
                    matStock.setQty(opnItem.getQtyOpname());
                    oidMaterialStock = PstMaterialStock.updateExc(matStock);
                    if (oidMaterialStock == 0) {
                        hasil = false;
                        break;
                    } else {
                        hasil = true;
                    }
                } else //Create new Stock
                {
                    MaterialStock matStock = new MaterialStock();
                    matStock.setPeriodeId(getCurrentPeriode());
                    matStock.setMaterialUnitId(opnItem.getMaterialId());
                    matStock.setLocationId(oidLocation);
                    matStock.setOpeningQty(opnItem.getQtyOpname());
                    matStock.setQty(opnItem.getQtyOpname());
                    long oidMatStock = PstMaterialStock.insertExc(matStock);
                }

                // penyetujuan serial code barang
                String where = PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_SOURCE_ID] + "=" + opnItem.getOID();
                Vector vectCode = PstSourceStockCode.list(0, 0, where, "");
                PstMaterialStockCode.deleteStockCode(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID]+"="+opnItem.getMaterialId()+
                        " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS]+"="+PstMaterialStockCode.FLD_STOCK_STATUS_GOOD+
                        " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID]+"="+oidLocation);

                //PstMaterialStockCode.deleteStockCode(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID]+"="+opnItem.getMaterialId()+
                //    " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS]+"="+PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);

                if (vectCode != null && vectCode.size() > 0) {
                    for (int k = 0; k < vectCode.size(); k++) {
                        SourceStockCode sourceStockCode = (SourceStockCode) vectCode.get(k);
                        MaterialStockCode materialStockCode = new MaterialStockCode();
                        // materialStockCode = PstMaterialStockCode.cekExistByCode(sourceStockCode.getStockCode(), opnItem.getMaterialId());
                        try {
                            if (materialStockCode.getOID() == 0) {
                                materialStockCode = new MaterialStockCode();
                                materialStockCode.setLocationId(oidLocation);
                                materialStockCode.setMaterialId(opnItem.getMaterialId());
                                materialStockCode.setStockCode(sourceStockCode.getStockCode());
                                materialStockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);
                                PstMaterialStockCode.insertExc(materialStockCode);
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
            //Update Status Document menjadi Posted
            if (hasil == true) {
                MatStockOpname sop = PstMatStockOpname.fetchExc(oidStockOpname);
                sop.setStockOpnameStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                long has = PstMatStockOpname.updateExc(sop);
                if (has != 0)
                    hasil = true;
                else
                    hasil = false;
            }
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }

    //Process get lost Qty for opname
    public static boolean getMaterialOpname(long oidStockOpname, long oidLocation, Date startDate) {
        boolean hasil = false;
        double qtyLost = 0.0;
		double weightLost = 0.0;
        DBResultSet dbrs = null;
        try {
            Vector tampung = new Vector();

            //Select OutStandingItem in Stock Opname Item
            String sql = "SELECT * FROM " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM +
                    " WHERE " + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] +
                    " = " + oidStockOpname; // +
            //" AND (QTY_SYSTEM - (QTY_SOLD + QTY_OPNAME)) <> 0";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                MatStockOpnameItem soi = new MatStockOpnameItem();
                soi.setOID(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ITEM_ID]));
                soi.setStockOpnameId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID]));
                soi.setMaterialId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID]));
                soi.setUnitId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_UNIT_ID]));
                soi.setQtyOpname(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME]));
                soi.setQtySold(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SOLD]));
                soi.setQtySystem(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SYSTEM]));
                soi.setCost(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_COST]));
                soi.setPrice(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_PRICE]));
                soi.setStockOpnameCounter(rs.getInt(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_COUNTER]));
                soi.setKadarId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_KADAR_ID]));
                soi.setKadarOpnameId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_KADAR_OPNAME_ID]));
                soi.setBerat(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_BERAT]));
                soi.setBeratOpname(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_BERAT_OPNAME]));
                soi.setRemark(rs.getString(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_REMARK]));
                soi.setBeratSelisih(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_BERAT_SELISIH]));
                soi.setQtyItem(rs.getInt(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_ITEM]));
                soi.setQtySelisih(rs.getInt(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SELISIH]));

                tampung.add(soi);
            }
            rs.close();

            //Process OutStandingItem, update Material Stock
            hasil = true;

            for (int i = 0; i < tampung.size(); i++) {
                MatStockOpnameItem opnItem = (MatStockOpnameItem) tampung.get(i);
                    long oidStockOpnItem = opnItem.getOID();
                    long materialId = opnItem.getMaterialId();
                    //cek material
                    if(materialId==50442447){
                        System.out.println("com.dimata.posbo.entity.warehouse.PstMatStockOpname.getMaterialOpname()");
                    }
                    qtyLost = PstMatStockOpnameItem.lostQtyMaterial(materialId, oidLocation,startDate);
                    opnItem.setQtySystem(qtyLost);
                    oidStockOpnItem = PstMatStockOpnameItem.updateExc(opnItem);
                    if (oidStockOpnItem == 0) {
                       hasil = false;
                       break;
                   } else {
                      hasil = true;
                   }
            //Update Status Document menjadi Posted
            /*if (hasil == true) {
                MatStockOpname sop = PstMatStockOpname.fetchExc(oidStockOpname);
                sop.setStockOpnameStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                long has = PstMatStockOpname.updateExc(sop);
                if (has != 0)
                    hasil = true;
                else
                    hasil = false;
            }*/
         }
        }catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }

   
    //Process stock opname menjadi stock correction
    public static boolean StockCorrectionForFixing(long oidStockOpname) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            Vector tampung = new Vector();
            MatStockOpname matStockOpname = PstMatStockOpname.fetchExc(oidStockOpname);
            //Select OutStandingItem in Stock Opname Item
            String sql = "SELECT * FROM " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM +
                    " WHERE " + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] +
                    " = " + oidStockOpname;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                MatStockOpnameItem soi = new MatStockOpnameItem();
                soi.setOID(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ITEM_ID]));
                soi.setStockOpnameId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID]));
                soi.setMaterialId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID]));
                soi.setUnitId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_UNIT_ID]));
                soi.setQtyOpname(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME]));
                soi.setQtySold(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SOLD]));
                soi.setQtySystem(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SYSTEM]));
                tampung.add(soi);
            }
            rs.close();

            //Process OutStandingItem, update Material Stock
            hasil = true;
            for (int i = 0; i < tampung.size(); i++) {
                MatStockOpnameItem opnItem = (MatStockOpnameItem) tampung.get(i);

                // penyetujuan serial code barang
                String where = PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_SOURCE_ID] + "=" + opnItem.getOID();
                Vector vectCode = PstSourceStockCode.list(0, 0, where, "");

                PstMaterialStockCode.deleteStockCode(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID]+"="+opnItem.getMaterialId()+
                    " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS]+"="+PstMaterialStockCode.FLD_STOCK_STATUS_GOOD+
                    " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID]+"="+matStockOpname.getLocationId());

                if (vectCode != null && vectCode.size() > 0) {
                    for (int k = 0; k < vectCode.size(); k++) {
                        SourceStockCode sourceStockCode = (SourceStockCode) vectCode.get(k);
                        MaterialStockCode materialStockCode = new MaterialStockCode();
                        materialStockCode = PstMaterialStockCode.cekExistByCode(sourceStockCode.getStockCode(), opnItem.getMaterialId());
                        try {
                            if (materialStockCode.getOID() == 0) {
                                materialStockCode = new MaterialStockCode();
                                materialStockCode.setLocationId(matStockOpname.getLocationId());
                                materialStockCode.setMaterialId(opnItem.getMaterialId());
                                materialStockCode.setStockCode(sourceStockCode.getStockCode());
                                materialStockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);
                                PstMaterialStockCode.insertExc(materialStockCode);
                            }
                        } catch (Exception e) {
                            hasil = false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }

    //Get Current Stock Periode (There can be only one with status open !!!)
    private static long getCurrentPeriode() {
        long hasil = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID] +
                    " FROM " + PstPeriode.TBL_STOCK_PERIODE +
                    " WHERE " + PstPeriode.fieldNames[PstPeriode.FLD_STATUS] +
                    " = " + PstPeriode.FLD_STATUS_RUNNING;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                hasil = rs.getLong(1);
            }
            rs.close();
        } catch (Exception exc) {
            System.out.println("GetPeriode : " + exc);
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }

    /*
     * Get last Opname material for stock Opname
     * By Mirahu
     * 20110809
     */
    public static Vector getLastDateOpname(long oidLocation, long oidMaterial, Date dtstart){
        Vector list = new Vector();
        DBResultSet dbrs = null;
        //MatStockOpname matOpname = new MatStockOpname();
        try{
            String sql = " SELECT OPN." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]
                    + " , SUM(OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME] + ") AS SUM_QTY"
                    + " , OPN." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS]
                    + " FROM " + TBL_MAT_STOCK_OPNAME + " OPN "
                    + " INNER JOIN " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM + " OPNITEM "
                    + " ON OPN." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID]
                    + " = OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID]
                    + " WHERE OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID] + "=" + oidMaterial
                    + " AND OPN." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] + "=" + oidLocation
                    + " AND OPN." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + "<'" + dtstart + "'"
                    //added by dewok 2018-12-22 tambahan parameter status opname
                    + " AND OPN." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS]
                    + " IN (" + I_DocStatus.DOCUMENT_STATUS_CLOSED
                    + " ," + I_DocStatus.DOCUMENT_STATUS_POSTED
                    + " ," + I_DocStatus.PAYMENT_STATUS_POSTED_CLOSED
                    + " )"
                    + " GROUP BY " + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]
                    + " ORDER BY OPN. " + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + " DESC ";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                Vector vt = new Vector(1, 1);
                MatStockOpname matStockOpname = new MatStockOpname();
                MatStockOpnameItem matStockOpnameItem =new MatStockOpnameItem();

                Date date = DBHandler.convertDate(rs.getDate(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]), rs.getTime(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]));
                matStockOpname.setStockOpnameDate(date);
                vt.add(matStockOpname);

                matStockOpnameItem.setQtyOpname(rs.getDouble("SUM_QTY"));
                vt.add(matStockOpnameItem);

                list.add(vt);
            }
            rs.close();
        }catch(Exception e){
            System.out.println("err getLastOpnameDate : "+e.toString());
        }finally{
            DBResultSet.close(dbrs);
        }
        return list;
    }
    
    
    public static Vector getLastDateOpnameReposting(long oidLocation, long oidMaterial, Date dtstart){
        Vector list = new Vector();
        DBResultSet dbrs = null;
        //MatStockOpname matOpname = new MatStockOpname();
        try{
            String sql = " SELECT OPN." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] +
                         " , SUM(OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME] + ") AS SUM_QTY" +
                         " FROM "+ TBL_MAT_STOCK_OPNAME + " OPN " +
                         " INNER JOIN " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM + " OPNITEM " +
                         " ON OPN." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID]+
                         " = OPNITEM." + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID]+
                         " WHERE OPNITEM."+ PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID]+"="+oidMaterial+
                         " AND OPN." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID] + "=" + oidLocation +
                         " AND OPN." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE] + "<'"+dtstart+"'"+
                         " AND OPN." + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS] + "='7'" +
                         " GROUP BY " + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]+
                         " ORDER BY OPN. " + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]+ " DESC ";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                Vector vt = new Vector(1, 1);
                MatStockOpname matStockOpname = new MatStockOpname();
                MatStockOpnameItem matStockOpnameItem =new MatStockOpnameItem();

                Date date = DBHandler.convertDate(rs.getDate(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]), rs.getTime(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE]));
                matStockOpname.setStockOpnameDate(date);
                vt.add(matStockOpname);

                matStockOpnameItem.setQtyOpname(rs.getDouble("SUM_QTY"));
                vt.add(matStockOpnameItem);

                list.add(vt);
            }
            rs.close();
        }catch(Exception e){
            System.out.println("err getLastOpnameDate : "+e.toString());
        }finally{
            DBResultSet.close(dbrs);
        }
        return list;
    }

     //Process stock opname menjadi stock correction
    public static boolean StockCorrectionNew(long oidStockOpname, long oidLocation) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            Vector tampung = new Vector();

            //Select OutStandingItem in Stock Opname Item
            String sql = "SELECT * FROM " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM +
                    " WHERE " + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] +
                    " = " + oidStockOpname; // +
            //" AND (QTY_SYSTEM - (QTY_SOLD + QTY_OPNAME)) <> 0";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                MatStockOpnameItem soi = new MatStockOpnameItem();
                soi.setOID(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ITEM_ID]));
                soi.setStockOpnameId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID]));
                soi.setMaterialId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID]));
                soi.setUnitId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_UNIT_ID]));
                soi.setQtyOpname(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME]));
                soi.setQtySold(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SOLD]));
                soi.setQtySystem(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SYSTEM]));
                tampung.add(soi);
            }
            rs.close();

            //Process OutStandingItem, update Material Stock
            hasil = true;
            for (int i = 0; i < tampung.size(); i++) {
                MatStockOpnameItem opnItem = (MatStockOpnameItem) tampung.get(i);
                long oidMaterialStock = PstMaterialStock.fetchByMaterialLocation(opnItem.getMaterialId(), oidLocation);
                if (oidMaterialStock != 0) {
                    MaterialStock matStock = PstMaterialStock.fetchExc(oidMaterialStock);
                    double currentQty = matStock.getQty();
                    double lostQty = (opnItem.getQtyOpname() + opnItem.getQtySold()) - opnItem.getQtySystem();

                    matStock.setQty(currentQty+lostQty);
                    oidMaterialStock = PstMaterialStock.updateExc(matStock);
                    if (oidMaterialStock == 0) {
                        hasil = false;
                        break;
                    } else {
                        hasil = true;
                    }
                } else //Create new Stock
                {
                    MaterialStock matStock = new MaterialStock();
                    matStock.setPeriodeId(getCurrentPeriode());
                    matStock.setMaterialUnitId(opnItem.getMaterialId());
                    matStock.setLocationId(oidLocation);
                    matStock.setOpeningQty(opnItem.getQtyOpname());
                    matStock.setQty(opnItem.getQtyOpname());
                    long oidMatStock = PstMaterialStock.insertExc(matStock);
                }

                // penyetujuan serial code barang
                String where = PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_SOURCE_ID] + "=" + opnItem.getOID();
                Vector vectCode = PstSourceStockCode.list(0, 0, where, "");
                PstMaterialStockCode.deleteStockCode(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID]+"="+opnItem.getMaterialId()+
                        " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS]+"="+PstMaterialStockCode.FLD_STOCK_STATUS_GOOD+
                        " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID]+"="+oidLocation);

                //PstMaterialStockCode.deleteStockCode(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID]+"="+opnItem.getMaterialId()+
                //    " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS]+"="+PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);

                if (vectCode != null && vectCode.size() > 0) {
                    for (int k = 0; k < vectCode.size(); k++) {
                        SourceStockCode sourceStockCode = (SourceStockCode) vectCode.get(k);
                        MaterialStockCode materialStockCode = new MaterialStockCode();
                        // materialStockCode = PstMaterialStockCode.cekExistByCode(sourceStockCode.getStockCode(), opnItem.getMaterialId());
                        try {
                            if (materialStockCode.getOID() == 0) {
                                materialStockCode = new MaterialStockCode();
                                materialStockCode.setLocationId(oidLocation);
                                materialStockCode.setMaterialId(opnItem.getMaterialId());
                                materialStockCode.setStockCode(sourceStockCode.getStockCode());
                                materialStockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);
                                PstMaterialStockCode.insertExc(materialStockCode);
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
            //Update Status Document menjadi Posted
            if (hasil == true) {
                MatStockOpname sop = PstMatStockOpname.fetchExc(oidStockOpname);
                sop.setStockOpnameStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                long has = PstMatStockOpname.updateExc(sop);
                if (has != 0)
                    hasil = true;
                else
                    hasil = false;
            }
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }

    //Process stock opname menjadi stock correction
    //Use transaction Doc based on doc after opname
    // by Mirahu
    //07122011
    public static boolean StockCorrectionAfterOpname(long oidStockOpname, long oidLocation, Date startDate) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            Vector tampung = new Vector();

            //Select OutStandingItem in Stock Opname Item
            String sql = "SELECT * FROM " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM +
                    " WHERE " + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] +
                    " = " + oidStockOpname; // +
            //" AND (QTY_SYSTEM - (QTY_SOLD + QTY_OPNAME)) <> 0";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                MatStockOpnameItem soi = new MatStockOpnameItem();
                soi.setOID(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ITEM_ID]));
                soi.setStockOpnameId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID]));
                soi.setMaterialId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID]));
                soi.setUnitId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_UNIT_ID]));
                soi.setQtyOpname(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME]));
                soi.setQtySold(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SOLD]));
                soi.setQtySystem(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SYSTEM]));
				soi.setCost(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_COST]));
                soi.setPrice(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_PRICE]));
                soi.setStockOpnameCounter(rs.getInt(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_COUNTER]));
				soi.setKadarId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_KADAR_ID]));
				soi.setKadarOpnameId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_KADAR_OPNAME_ID]));
				soi.setBerat(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_BERAT]));
				soi.setBeratOpname(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_BERAT_OPNAME]));
				soi.setRemark(rs.getString(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_REMARK]));
				soi.setBeratSelisih(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_BERAT_SELISIH]));
				soi.setQtyItem(rs.getInt(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_ITEM]));
				soi.setQtySelisih(rs.getInt(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SELISIH]));
                tampung.add(soi);
            }
            rs.close();

            //Process OutStandingItem, update Material Stock
            hasil = true;
            for (int i = 0; i < tampung.size(); i++) {
                MatStockOpnameItem opnItem = (MatStockOpnameItem) tampung.get(i);
                long oidStockOpnItem = opnItem.getOID();
                long materialId = opnItem.getMaterialId();
                double qtyOpname = opnItem.getQtyOpname();
                double qtyTransactionAfterOpname = PstMatStockOpnameItem.TransactionAfterOpname(materialId, oidLocation, startDate,qtyOpname);

                long oidMaterialStock = PstMaterialStock.fetchByMaterialLocation(opnItem.getMaterialId(), oidLocation);
                if (oidMaterialStock != 0) {
                    MaterialStock matStock = PstMaterialStock.fetchExc(oidMaterialStock);
                    matStock.setQty(qtyOpname+qtyTransactionAfterOpname);
                    oidMaterialStock = PstMaterialStock.updateExc(matStock);
                    if (oidMaterialStock == 0) {
                        hasil = false;
                        break;
                    } else {
                        hasil = true;
                    }
                } else //Create new Stock
                {
                    MaterialStock matStock = new MaterialStock();
                    matStock.setPeriodeId(getCurrentPeriode());
                    matStock.setMaterialUnitId(opnItem.getMaterialId());
                    matStock.setLocationId(oidLocation);
                    matStock.setOpeningQty(opnItem.getQtyOpname());
                    matStock.setQty(opnItem.getQtyOpname());
                    long oidMatStock = PstMaterialStock.insertExc(matStock);
                }

                // penyetujuan serial code barang
                String where = PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_SOURCE_ID] + "=" + opnItem.getOID();
                Vector vectCode = PstSourceStockCode.list(0, 0, where, "");
                PstMaterialStockCode.deleteStockCode(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID]+"="+opnItem.getMaterialId()+
                        " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS]+"="+PstMaterialStockCode.FLD_STOCK_STATUS_GOOD+
                        " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID]+"="+oidLocation);

                //PstMaterialStockCode.deleteStockCode(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID]+"="+opnItem.getMaterialId()+
                //    " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS]+"="+PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);

                if (vectCode != null && vectCode.size() > 0) {
                    for (int k = 0; k < vectCode.size(); k++) {
                        SourceStockCode sourceStockCode = (SourceStockCode) vectCode.get(k);
                        MaterialStockCode materialStockCode = new MaterialStockCode();
                        // materialStockCode = PstMaterialStockCode.cekExistByCode(sourceStockCode.getStockCode(), opnItem.getMaterialId());
                        try {
                            if (materialStockCode.getOID() == 0) {
                                materialStockCode = new MaterialStockCode();
                                materialStockCode.setLocationId(oidLocation);
                                materialStockCode.setMaterialId(opnItem.getMaterialId());
                                materialStockCode.setStockCode(sourceStockCode.getStockCode());
                                materialStockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);
                                materialStockCode.setStockValue(sourceStockCode.getStockValue());
                                PstMaterialStockCode.insertExc(materialStockCode);
                            }
                        } catch (Exception e) {
                        }
                    }
                }
				
				String whereMatDetail = PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_ID]+"='"+opnItem.getMaterialId()+"'";
				Vector listMatDetail = PstMaterialDetail.list(0, 0, whereMatDetail, "");
				if (listMatDetail.size()>0){
					MaterialDetail matDetail = (MaterialDetail) listMatDetail.get(0);
					matDetail.setBerat(opnItem.getBeratOpname());
					PstMaterialDetail.updateExc(matDetail);
				}
            }
            //Update Status Document menjadi Posted
            if (hasil == true) {
                MatStockOpname sop = PstMatStockOpname.fetchExc(oidStockOpname);
                sop.setStockOpnameStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                long has = PstMatStockOpname.updateExc(sop);
                if (has != 0)
                    hasil = true;
                else
                    hasil = false;
            }
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }
    
    //Process stock opname menjadi stock correction
    //Use transaction Doc based on doc after opname
    // by Mirahu
    //29032012
    public static boolean ReStockCorrectionAfterOpname(long oidStockOpname, long oidLocation, Date startDate) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            Vector tampung = new Vector();

            //Select OutStandingItem in Stock Opname Item
            String sql = "SELECT * FROM " + PstMatStockOpnameItem.TBL_STOCK_OPNAME_ITEM +
                    " WHERE " + PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] +
                    " = " + oidStockOpname; // +
            //" AND (QTY_SYSTEM - (QTY_SOLD + QTY_OPNAME)) <> 0";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                MatStockOpnameItem soi = new MatStockOpnameItem();
                soi.setOID(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ITEM_ID]));
                soi.setStockOpnameId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID]));
                soi.setMaterialId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID]));
                soi.setUnitId(rs.getLong(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_UNIT_ID]));
                soi.setQtyOpname(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME]));
                soi.setQtySold(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SOLD]));
                soi.setQtySystem(rs.getDouble(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SYSTEM]));
                tampung.add(soi);
            }
            rs.close();

            //Process OutStandingItem, update Material Stock
            hasil = true;
            for (int i = 0; i < tampung.size(); i++) {
                MatStockOpnameItem opnItem = (MatStockOpnameItem) tampung.get(i);
                long oidStockOpnItem = opnItem.getOID();
                long materialId = opnItem.getMaterialId();
                double qtyOpname = opnItem.getQtyOpname();
                double qtyTransactionAfterOpname = PstMatStockOpnameItem.TransactionAfterOpname(materialId, oidLocation, startDate,qtyOpname);

                long oidMaterialStock = PstMaterialStock.fetchByMaterialLocation(opnItem.getMaterialId(), oidLocation);
                if (oidMaterialStock != 0) {
                    MaterialStock matStock = PstMaterialStock.fetchExc(oidMaterialStock);
                    matStock.setQty(qtyOpname+qtyTransactionAfterOpname);
                    oidMaterialStock = PstMaterialStock.updateExc(matStock);
                    if (oidMaterialStock == 0) {
                        hasil = false;
                        break;
                    } else {
                        hasil = true;
                    }
                } else //Create new Stock
                {
                    MaterialStock matStock = new MaterialStock();
                    matStock.setPeriodeId(getCurrentPeriode());
                    matStock.setMaterialUnitId(opnItem.getMaterialId());
                    matStock.setLocationId(oidLocation);
                    matStock.setOpeningQty(opnItem.getQtyOpname());
                    matStock.setQty(opnItem.getQtyOpname());
                    long oidMatStock = PstMaterialStock.insertExc(matStock);
                }

                // penyetujuan serial code barang
                String where = PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_SOURCE_ID] + "=" + opnItem.getOID();
                Vector vectCode = PstSourceStockCode.list(0, 0, where, "");
                PstMaterialStockCode.deleteStockCode(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID]+"="+opnItem.getMaterialId()+
                        " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS]+"="+PstMaterialStockCode.FLD_STOCK_STATUS_GOOD+
                        " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID]+"="+oidLocation);

                //PstMaterialStockCode.deleteStockCode(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID]+"="+opnItem.getMaterialId()+
                //    " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS]+"="+PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);

                if (vectCode != null && vectCode.size() > 0) {
                    for (int k = 0; k < vectCode.size(); k++) {
                        SourceStockCode sourceStockCode = (SourceStockCode) vectCode.get(k);
                        MaterialStockCode materialStockCode = new MaterialStockCode();
                        // materialStockCode = PstMaterialStockCode.cekExistByCode(sourceStockCode.getStockCode(), opnItem.getMaterialId());
                        try {
                            if (materialStockCode.getOID() == 0) {
                                materialStockCode = new MaterialStockCode();
                                materialStockCode.setLocationId(oidLocation);
                                materialStockCode.setMaterialId(opnItem.getMaterialId());
                                materialStockCode.setStockCode(sourceStockCode.getStockCode());
                                materialStockCode.setStockStatus(PstMaterialStockCode.FLD_STOCK_STATUS_GOOD);
                                PstMaterialStockCode.insertExc(materialStockCode);
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
            //Update Status Document menjadi Posted
            if (hasil == true) {
                MatStockOpname sop = PstMatStockOpname.fetchExc(oidStockOpname);
                sop.setStockOpnameStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                long has = PstMatStockOpname.updateExc(sop);
                if (has != 0)
                    hasil = true;
                else
                    hasil = false;
            }
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }

   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MatStockOpname matStockOpname = PstMatStockOpname.fetchExc(oid);
         object.put(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_ID], matStockOpname.getOID());
         object.put(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID], matStockOpname.getLocationId());
         object.put(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_DATE], matStockOpname.getStockOpnameDate());
         object.put(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_TIME], matStockOpname.getStockOpnameTime());
         object.put(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_NUMBER], matStockOpname.getStockOpnameNumber());
         object.put(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS], matStockOpname.getStockOpnameStatus());
         object.put(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUPPLIER_ID], matStockOpname.getSupplierId());
         object.put(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CATEGORY_ID], matStockOpname.getCategoryId());
         object.put(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_SUB_CATEGORY_ID], matStockOpname.getSubCategoryId());
         object.put(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_REMARK], matStockOpname.getRemark());
         object.put(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_ETALASE_ID], matStockOpname.getEtalaseId());
         object.put(PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_OPNAME_ITEM_TYPE], matStockOpname.getOpnameItemType());
      }catch(Exception exc){}
      return object;
   }
   public static int getIntCode(MatStockOpname mop, Date pDate, long oid, int counter, boolean isIncrement) {
        int max = 0;
        DBResultSet dbrs = null;
        Date date = new Date();
        try {
            String sql = "SELECT MAX(" + PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_CODE_COUNTER] +
                    ") AS PMAX FROM " + PstMatStockOpname.TBL_MAT_STOCK_OPNAME;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                max = rs.getInt("PMAX");
            }

            if (oid == 0) {
                max = max + 1;
            } else {
                if (mop.getStockOpnameDate() == pDate)
                    max = max + 1;
                else
                    max = counter;
            }
        } catch (Exception e) {
            System.out.println("!!!!!SessOrderMaterial.getIntCode() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return max;
    }

}
