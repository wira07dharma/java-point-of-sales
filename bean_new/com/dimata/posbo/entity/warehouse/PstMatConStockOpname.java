package com.dimata.posbo.entity.warehouse;

import com.dimata.common.entity.custom.PstDataCustom;
import java.sql.*;
import java.util.*;
import java.util.Date;
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;

import com.dimata.posbo.entity.warehouse.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.warehouse.PstSourceStockCode;
import com.dimata.posbo.entity.warehouse.SourceStockCode;
import com.dimata.posbo.entity.warehouse.PstMaterialStockCode;
import com.dimata.posbo.entity.warehouse.MaterialStockCode;
import com.dimata.common.entity.periode.PstPeriode;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import org.json.JSONObject;

public class PstMatConStockOpname extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_Document {

    public static final String TBL_MAT_STOCK_OPNAME = "pos_stock_con_opname";

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

    public static final String[] fieldNames =
            {
                "STOCK_CON_OPNAME_ID",
                "LOCATION_ID",
                "STOCK_OPNAME_DATE",
                "STOCK_OPNAME_TIME",
                "STOCK_OPNAME_NUMBER",
                "STOCK_OPNAME_STATUS",
                "SUPPLIER_ID",
                "CATEGORY_ID",
                "SUB_CATEGORY_ID",
                "REMARK"
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
                TYPE_STRING
            };

            public PstMatConStockOpname() {
    }

            public PstMatConStockOpname(int i) throws DBException {
        super(new PstMatConStockOpname());
    }

            public PstMatConStockOpname(String sOid) throws DBException {
        super(new PstMatConStockOpname(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else 
            return;
    }

            public PstMatConStockOpname(long lOid) throws DBException {
        super(new PstMatConStockOpname(0));
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
        return new PstMatConStockOpname().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        MatConStockOpname matstockopname = fetchExc(ent.getOID());
        ent = (Entity) matstockopname;
        return matstockopname.getOID();
    }

    synchronized public long insertExc(Entity ent) throws Exception {
        return insertExc((MatConStockOpname) ent);
    }

    synchronized public long updateExc(Entity ent) throws Exception {
        return updateExc((MatConStockOpname) ent);
    }

    synchronized public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static MatConStockOpname fetchExc(long oid) throws DBException {
        try {
            MatConStockOpname matstockopname = new MatConStockOpname();
            PstMatConStockOpname pstMatConStockOpname = new PstMatConStockOpname(oid);
            matstockopname.setOID(oid);

            matstockopname.setLocationId(pstMatConStockOpname.getlong(FLD_LOCATION_ID));
            matstockopname.setStockOpnameDate(pstMatConStockOpname.getDate(FLD_STOCK_OPNAME_DATE));
            matstockopname.setStockOpnameTime(pstMatConStockOpname.getString(FLD_STOCK_OPNAME_TIME));
            matstockopname.setStockOpnameNumber(pstMatConStockOpname.getString(FLD_STOCK_OPNAME_NUMBER));
            matstockopname.setStockOpnameStatus(pstMatConStockOpname.getInt(FLD_STOCK_OPNAME_STATUS));
            matstockopname.setSupplierId(pstMatConStockOpname.getlong(FLD_SUPPLIER_ID));
            matstockopname.setCategoryId(pstMatConStockOpname.getlong(FLD_CATEGORY_ID));
            matstockopname.setSubCategoryId(pstMatConStockOpname.getlong(FLD_SUB_CATEGORY_ID));
            matstockopname.setRemark(pstMatConStockOpname.getString(FLD_REMARK));

            return matstockopname;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatConStockOpname(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(MatConStockOpname matstockopname) throws DBException {
        try {
            PstMatConStockOpname pstMatConStockOpname = new PstMatConStockOpname(0);
            matstockopname.setStockOpnameNumber(generateStockOpnameNumber(matstockopname));

            pstMatConStockOpname.setLong(FLD_LOCATION_ID, matstockopname.getLocationId());
            pstMatConStockOpname.setDate(FLD_STOCK_OPNAME_DATE, matstockopname.getStockOpnameDate());
            pstMatConStockOpname.setString(FLD_STOCK_OPNAME_TIME, matstockopname.getStockOpnameTime());
            pstMatConStockOpname.setString(FLD_STOCK_OPNAME_NUMBER, matstockopname.getStockOpnameNumber());
            pstMatConStockOpname.setInt(FLD_STOCK_OPNAME_STATUS, matstockopname.getStockOpnameStatus());
            pstMatConStockOpname.setLong(FLD_SUPPLIER_ID, matstockopname.getSupplierId());
            pstMatConStockOpname.setLong(FLD_CATEGORY_ID, matstockopname.getCategoryId());
            pstMatConStockOpname.setLong(FLD_SUB_CATEGORY_ID, matstockopname.getSubCategoryId());
            pstMatConStockOpname.setString(FLD_REMARK, matstockopname.getRemark());

            pstMatConStockOpname.insert();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatConStockOpname.getInsertSQL());
            matstockopname.setOID(pstMatConStockOpname.getlong(FLD_STOCK_OPNAME_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatConStockOpname(0), DBException.UNKNOWN);
        }
        return matstockopname.getOID();
    }

    public static long updateExc(MatConStockOpname matstockopname) throws DBException {
        try {
            if (matstockopname.getOID() != 0) {
                PstMatConStockOpname pstMatConStockOpname = new PstMatConStockOpname(matstockopname.getOID());

                pstMatConStockOpname.setLong(FLD_LOCATION_ID, matstockopname.getLocationId());
                pstMatConStockOpname.setDate(FLD_STOCK_OPNAME_DATE, matstockopname.getStockOpnameDate());
                pstMatConStockOpname.setString(FLD_STOCK_OPNAME_TIME, matstockopname.getStockOpnameTime());
                pstMatConStockOpname.setString(FLD_STOCK_OPNAME_NUMBER, matstockopname.getStockOpnameNumber());
                pstMatConStockOpname.setInt(FLD_STOCK_OPNAME_STATUS, matstockopname.getStockOpnameStatus());
                pstMatConStockOpname.setLong(FLD_SUPPLIER_ID, matstockopname.getSupplierId());
                pstMatConStockOpname.setLong(FLD_CATEGORY_ID, matstockopname.getCategoryId());
                pstMatConStockOpname.setLong(FLD_SUB_CATEGORY_ID, matstockopname.getSubCategoryId());
                pstMatConStockOpname.setString(FLD_REMARK, matstockopname.getRemark());

                pstMatConStockOpname.update();
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMatConStockOpname.getUpdateSQL());
                return matstockopname.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatConStockOpname(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstMatConStockOpname pstMatConStockOpname = new PstMatConStockOpname(oid);
            //Delete Item First
            PstMatConStockOpnameItem pstSOItem = new PstMatConStockOpnameItem();
            long result = pstSOItem.deleteExcByParent(oid);
            pstMatConStockOpname.delete();
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMatConStockOpname.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatConStockOpname(0), DBException.UNKNOWN);
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
                MatConStockOpname matstockopname = new MatConStockOpname();
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

    public static void resultToObject(ResultSet rs, MatConStockOpname matstockopname) {
        try {
            matstockopname.setOID(rs.getLong(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_STOCK_OPNAME_ID]));
            matstockopname.setLocationId(rs.getLong(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_LOCATION_ID]));
            matstockopname.setStockOpnameDate(rs.getDate(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_STOCK_OPNAME_DATE]));
            matstockopname.setStockOpnameTime(rs.getString(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_STOCK_OPNAME_TIME]));
            matstockopname.setStockOpnameNumber(rs.getString(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_STOCK_OPNAME_NUMBER]));
            matstockopname.setStockOpnameStatus(rs.getInt(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_STOCK_OPNAME_STATUS]));
            matstockopname.setSupplierId(rs.getLong(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_SUPPLIER_ID]));
            matstockopname.setCategoryId(rs.getLong(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_CATEGORY_ID]));
            matstockopname.setSubCategoryId(rs.getLong(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_SUB_CATEGORY_ID]));
            matstockopname.setRemark(rs.getString(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_REMARK]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long matStockOpnameId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_STOCK_OPNAME + " WHERE " +
                    PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_STOCK_OPNAME_ID] + " = " + matStockOpnameId;

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
            String sql = "SELECT COUNT(" + PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_STOCK_OPNAME_ID] + ") FROM " + TBL_MAT_STOCK_OPNAME;
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
                    MatConStockOpname matstockopname = (MatConStockOpname) list.get(ls);
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
            MatConStockOpname matStockOpname = fetchExc(documentId);
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
			String sql = "UPDATE " + PstMatConStockOpname.TBL_MAT_STOCK_OPNAME +
                         " SET " + PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_STOCK_OPNAME_STATUS] + " = " + indexStatus +
                	     " WHERE " + PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_STOCK_OPNAME_ID] + " = " + documentId;
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

    public static String generateStockOpnameNumber(MatConStockOpname matstockopname) {
        String hasil = "";
        String number = "";
        String front_like = "";
        String tmp = String.valueOf(new Date().getYear());
        //Year
        if (tmp.length() >= 2) tmp = tmp.substring(tmp.length() - 2, tmp.length());
        number = tmp;
        //Month
        tmp = String.valueOf(new Date().getMonth() + 1);
        if (tmp.length() < 2) tmp = "0" + tmp;
        front_like = number + tmp;
        number = number + tmp + "-" + "SO" + "-";
        DBResultSet dbrs = null;
        try {
            /** get location code; gwawan@21juni2007 */
            Vector vctLocation = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"="+matstockopname.getLocationId(), "");
            Location location = (Location)vctLocation.get(0);
            
            String sql = "SELECT MAX(" + fieldNames[FLD_STOCK_OPNAME_NUMBER] +
                    ") AS MAXNUM FROM " + TBL_MAT_STOCK_OPNAME +
                    " WHERE " + fieldNames[FLD_STOCK_OPNAME_NUMBER] +
                    " LIKE '" + front_like +
                    "%'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count += 1;
                hasil = rs.getString("MAXNUM");
            }
            rs.close();
            //Counter SO
            if ((count != 0) && (hasil != null)) {
                hasil = hasil.substring(8, 11);
                int sem = Integer.parseInt(hasil) + 1;
                hasil = String.valueOf(sem);
                int panjang = hasil.length();
                switch (panjang) {
                    case 1:
                        hasil = "00" + hasil;
                        break;
                    case 2:
                        hasil = "0" + hasil;
                        break;
                    default:
                        break;
                }
            } else {
                //Set counter = 1
                hasil = "001";
            }
            hasil = location.getCode() + "- " + number + hasil;
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
            String sql = "SELECT * FROM " + PstMatConStockOpnameItem.TBL_STOCK_OPNAME_ITEM +
                    " WHERE " + PstMatConStockOpnameItem.fieldNames[PstMatConStockOpnameItem.FLD_STOCK_OPNAME_ID] +
                    " = " + oidStockOpname; // +
            //" AND (QTY_SYSTEM - (QTY_SOLD + QTY_OPNAME)) <> 0";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                MatConStockOpnameItem soi = new MatConStockOpnameItem();
                soi.setOID(rs.getLong(PstMatConStockOpnameItem.fieldNames[PstMatConStockOpnameItem.FLD_STOCK_OPNAME_ITEM_ID]));
                soi.setStockOpnameId(rs.getLong(PstMatConStockOpnameItem.fieldNames[PstMatConStockOpnameItem.FLD_STOCK_OPNAME_ID]));
                soi.setMaterialId(rs.getLong(PstMatConStockOpnameItem.fieldNames[PstMatConStockOpnameItem.FLD_MATERIAL_ID]));
                soi.setUnitId(rs.getLong(PstMatConStockOpnameItem.fieldNames[PstMatConStockOpnameItem.FLD_UNIT_ID]));
                soi.setQtyOpname(rs.getDouble(PstMatConStockOpnameItem.fieldNames[PstMatConStockOpnameItem.FLD_QTY_OPNAME]));
                soi.setQtySold(rs.getDouble(PstMatConStockOpnameItem.fieldNames[PstMatConStockOpnameItem.FLD_QTY_SOLD]));
                soi.setQtySystem(rs.getDouble(PstMatConStockOpnameItem.fieldNames[PstMatConStockOpnameItem.FLD_QTY_SYSTEM]));
                tampung.add(soi);
            }
            rs.close();

            //Process OutStandingItem, update Material Stock
            hasil = true;
            for (int i = 0; i < tampung.size(); i++) {
                MatConStockOpnameItem opnItem = (MatConStockOpnameItem) tampung.get(i);
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
                MatConStockOpname sop = PstMatConStockOpname.fetchExc(oidStockOpname);
                sop.setStockOpnameStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                long has = PstMatConStockOpname.updateExc(sop);
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
    public static boolean StockCorrectionForFixing(long oidStockOpname) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            Vector tampung = new Vector();
            MatConStockOpname matStockOpname = PstMatConStockOpname.fetchExc(oidStockOpname);
            //Select OutStandingItem in Stock Opname Item
            String sql = "SELECT * FROM " + PstMatConStockOpnameItem.TBL_STOCK_OPNAME_ITEM +
                    " WHERE " + PstMatConStockOpnameItem.fieldNames[PstMatConStockOpnameItem.FLD_STOCK_OPNAME_ID] +
                    " = " + oidStockOpname;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                MatConStockOpnameItem soi = new MatConStockOpnameItem();
                soi.setOID(rs.getLong(PstMatConStockOpnameItem.fieldNames[PstMatConStockOpnameItem.FLD_STOCK_OPNAME_ITEM_ID]));
                soi.setStockOpnameId(rs.getLong(PstMatConStockOpnameItem.fieldNames[PstMatConStockOpnameItem.FLD_STOCK_OPNAME_ID]));
                soi.setMaterialId(rs.getLong(PstMatConStockOpnameItem.fieldNames[PstMatConStockOpnameItem.FLD_MATERIAL_ID]));
                soi.setUnitId(rs.getLong(PstMatConStockOpnameItem.fieldNames[PstMatConStockOpnameItem.FLD_UNIT_ID]));
                soi.setQtyOpname(rs.getDouble(PstMatConStockOpnameItem.fieldNames[PstMatConStockOpnameItem.FLD_QTY_OPNAME]));
                soi.setQtySold(rs.getDouble(PstMatConStockOpnameItem.fieldNames[PstMatConStockOpnameItem.FLD_QTY_SOLD]));
                soi.setQtySystem(rs.getDouble(PstMatConStockOpnameItem.fieldNames[PstMatConStockOpnameItem.FLD_QTY_SYSTEM]));
                tampung.add(soi);
            }
            rs.close();

            //Process OutStandingItem, update Material Stock
            hasil = true;
            for (int i = 0; i < tampung.size(); i++) {
                MatConStockOpnameItem opnItem = (MatConStockOpnameItem) tampung.get(i);

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
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MatConStockOpname matConStockOpname = PstMatConStockOpname.fetchExc(oid);
         object.put(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_STOCK_OPNAME_ID], matConStockOpname.getOID());
         object.put(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_LOCATION_ID], matConStockOpname.getLocationId());
         object.put(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_STOCK_OPNAME_DATE], matConStockOpname.getStockOpnameDate());
         object.put(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_STOCK_OPNAME_TIME], matConStockOpname.getStockOpnameTime());
         object.put(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_STOCK_OPNAME_NUMBER], matConStockOpname.getStockOpnameNumber());
         object.put(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_STOCK_OPNAME_STATUS], matConStockOpname.getStockOpnameStatus());
         object.put(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_SUPPLIER_ID], matConStockOpname.getSupplierId());
         object.put(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_CATEGORY_ID], matConStockOpname.getCategoryId());
         object.put(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_SUB_CATEGORY_ID], matConStockOpname.getSubCategoryId());
         object.put(PstMatConStockOpname.fieldNames[PstMatConStockOpname.FLD_REMARK], matConStockOpname.getRemark());
      }catch(Exception exc){}
      return object;
   }
}
