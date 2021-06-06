/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;
import org.json.JSONObject;

/**
 *
 * @author Dimata 007
 */
public class PstInsentifMaster extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_INSENTIF_MASTER = "insentif_master";
    public static final int FLD_INSENTIF_MASTER_ID = 0;
    public static final int FLD_TITLE = 1;
    public static final int FLD_PERIODE_START = 2;
    public static final int FLD_PERIODE_END = 3;
    public static final int FLD_PERIODE_FOREVER = 4;
    public static final int FLD_MATERIAL_MAIN = 5;
    public static final int FLD_INCLUDE_SALES_PROFIT = 6;
    public static final int FLD_INCLUDE_COST_OF_SALES = 7;
    public static final int FLD_DIVISION_POINT = 8;
    public static final int FLD_FAKTOR_NOMINAL_INSENTIF = 9;
    public static final int FLD_DEPEND_ON_POSITION = 10;
    public static final int FLD_STATUS = 11;
    public static final int FLD_CATEGORY_ID = 12;

    public static String[] fieldNames = {
        "INSENTIF_MASTER_ID",
        "TITLE",
        "PERIODE_START",
        "PERIODE_END",
        "PERIODE_FOREVER",
        "MATERIAL_MAIN",
        "INCLUDE_SALES_PROFIT",
        "INCLUDE_COST_OF_SALES",
        "DIVISION_POINT",
        "FAKTOR_NOMINAL_INSENTIF",
        "DEPEND_ON_POSITION",
        "STATUS",
        "CATEGORY_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG
    };
    
    public static final int PERIODE_RANGE = 0;
    public static final int PERIODE_FOREVER = 1;

    public PstInsentifMaster() {
    }

    public PstInsentifMaster(int i) throws DBException {
        super(new PstInsentifMaster());
    }

    public PstInsentifMaster(String sOid) throws DBException {
        super(new PstInsentifMaster(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstInsentifMaster(long lOid) throws DBException {
        super(new PstInsentifMaster(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_INSENTIF_MASTER;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstInsentifMaster().getClass().getName();
    }

    public static InsentifMaster fetchExc(long oid) throws DBException {
        try {
            InsentifMaster entInsentifMaster = new InsentifMaster();
            PstInsentifMaster pstInsentifMaster = new PstInsentifMaster(oid);
            entInsentifMaster.setOID(oid);
            entInsentifMaster.setTitle(pstInsentifMaster.getString(FLD_TITLE));
            entInsentifMaster.setPeriodeStart(pstInsentifMaster.getDate(FLD_PERIODE_START));
            entInsentifMaster.setPeriodeEnd(pstInsentifMaster.getDate(FLD_PERIODE_END));
            entInsentifMaster.setPeriodeForever(pstInsentifMaster.getInt(FLD_PERIODE_FOREVER));
            entInsentifMaster.setMaterialMain(pstInsentifMaster.getInt(FLD_MATERIAL_MAIN));
            entInsentifMaster.setIncludeSalesProfit(pstInsentifMaster.getInt(FLD_INCLUDE_SALES_PROFIT));
            entInsentifMaster.setIncludeCostOfSales(pstInsentifMaster.getInt(FLD_INCLUDE_COST_OF_SALES));
            entInsentifMaster.setDivisionPoint(pstInsentifMaster.getdouble(FLD_DIVISION_POINT));
            entInsentifMaster.setFaktorNominalInsentif(pstInsentifMaster.getdouble(FLD_FAKTOR_NOMINAL_INSENTIF));
            entInsentifMaster.setDependOnPosition(pstInsentifMaster.getInt(FLD_DEPEND_ON_POSITION));
            entInsentifMaster.setStatus(pstInsentifMaster.getInt(FLD_STATUS));
            entInsentifMaster.setCategoryId(pstInsentifMaster.getlong(FLD_CATEGORY_ID));
            return entInsentifMaster;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstInsentifMaster(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        InsentifMaster entInsentifMaster = fetchExc(entity.getOID());
        entity = (Entity) entInsentifMaster;
        return entInsentifMaster.getOID();
    }

    public static synchronized long updateExc(InsentifMaster entInsentifMaster) throws DBException {
        try {
            if (entInsentifMaster.getOID() != 0) {
                PstInsentifMaster pstInsentifMaster = new PstInsentifMaster(entInsentifMaster.getOID());
                pstInsentifMaster.setString(FLD_TITLE, entInsentifMaster.getTitle());
                pstInsentifMaster.setDate(FLD_PERIODE_START, entInsentifMaster.getPeriodeStart());
                pstInsentifMaster.setDate(FLD_PERIODE_END, entInsentifMaster.getPeriodeEnd());
                pstInsentifMaster.setInt(FLD_PERIODE_FOREVER, entInsentifMaster.getPeriodeForever());
                pstInsentifMaster.setInt(FLD_MATERIAL_MAIN, entInsentifMaster.getMaterialMain());
                pstInsentifMaster.setInt(FLD_INCLUDE_SALES_PROFIT, entInsentifMaster.getIncludeSalesProfit());
                pstInsentifMaster.setInt(FLD_INCLUDE_COST_OF_SALES, entInsentifMaster.getIncludeCostOfSales());
                pstInsentifMaster.setDouble(FLD_DIVISION_POINT, entInsentifMaster.getDivisionPoint());
                pstInsentifMaster.setDouble(FLD_FAKTOR_NOMINAL_INSENTIF, entInsentifMaster.getFaktorNominalInsentif());
                pstInsentifMaster.setInt(FLD_DEPEND_ON_POSITION, entInsentifMaster.getDependOnPosition());
                pstInsentifMaster.setInt(FLD_STATUS, entInsentifMaster.getStatus());
                pstInsentifMaster.setLong(FLD_CATEGORY_ID, entInsentifMaster.getCategoryId());
                pstInsentifMaster.update();
                return entInsentifMaster.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstInsentifMaster(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((InsentifMaster) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstInsentifMaster pstInsentifMaster = new PstInsentifMaster(oid);
            pstInsentifMaster.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstInsentifMaster(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(InsentifMaster entInsentifMaster) throws DBException {
        try {
            PstInsentifMaster pstInsentifMaster = new PstInsentifMaster(0);
            pstInsentifMaster.setString(FLD_TITLE, entInsentifMaster.getTitle());
            pstInsentifMaster.setDate(FLD_PERIODE_START, entInsentifMaster.getPeriodeStart());
            pstInsentifMaster.setDate(FLD_PERIODE_END, entInsentifMaster.getPeriodeEnd());
            pstInsentifMaster.setInt(FLD_PERIODE_FOREVER, entInsentifMaster.getPeriodeForever());
            pstInsentifMaster.setInt(FLD_MATERIAL_MAIN, entInsentifMaster.getMaterialMain());
            pstInsentifMaster.setInt(FLD_INCLUDE_SALES_PROFIT, entInsentifMaster.getIncludeSalesProfit());
            pstInsentifMaster.setInt(FLD_INCLUDE_COST_OF_SALES, entInsentifMaster.getIncludeCostOfSales());
            pstInsentifMaster.setDouble(FLD_DIVISION_POINT, entInsentifMaster.getDivisionPoint());
            pstInsentifMaster.setDouble(FLD_FAKTOR_NOMINAL_INSENTIF, entInsentifMaster.getFaktorNominalInsentif());
            pstInsentifMaster.setInt(FLD_DEPEND_ON_POSITION, entInsentifMaster.getDependOnPosition());
            pstInsentifMaster.setInt(FLD_STATUS, entInsentifMaster.getStatus());
            pstInsentifMaster.setLong(FLD_CATEGORY_ID, entInsentifMaster.getCategoryId());
            pstInsentifMaster.insert();
            entInsentifMaster.setOID(pstInsentifMaster.getlong(FLD_INSENTIF_MASTER_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstInsentifMaster(0), DBException.UNKNOWN);
        }
        return entInsentifMaster.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((InsentifMaster) entity);
    }

    public static void resultToObject(ResultSet rs, InsentifMaster entInsentifMaster) {
        try {
            entInsentifMaster.setOID(rs.getLong(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_INSENTIF_MASTER_ID]));
            entInsentifMaster.setTitle(rs.getString(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_TITLE]));
            entInsentifMaster.setPeriodeStart(rs.getDate(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_PERIODE_START]));
            entInsentifMaster.setPeriodeEnd(rs.getDate(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_PERIODE_END]));
            entInsentifMaster.setPeriodeForever(rs.getInt(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_PERIODE_FOREVER]));
            entInsentifMaster.setMaterialMain(rs.getInt(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_MATERIAL_MAIN]));
            entInsentifMaster.setIncludeSalesProfit(rs.getInt(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_INCLUDE_SALES_PROFIT]));
            entInsentifMaster.setIncludeCostOfSales(rs.getInt(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_INCLUDE_COST_OF_SALES]));
            entInsentifMaster.setDivisionPoint(rs.getDouble(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_DIVISION_POINT]));
            entInsentifMaster.setFaktorNominalInsentif(rs.getDouble(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_FAKTOR_NOMINAL_INSENTIF]));
            entInsentifMaster.setDependOnPosition(rs.getInt(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_DEPEND_ON_POSITION]));
            entInsentifMaster.setStatus(rs.getInt(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_STATUS]));
            entInsentifMaster.setCategoryId(rs.getLong(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_CATEGORY_ID]));
        } catch (Exception e) {
        }
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_INSENTIF_MASTER;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                InsentifMaster entInsentifMaster = new InsentifMaster();
                resultToObject(rs, entInsentifMaster);
                lists.add(entInsentifMaster);
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

    public static boolean checkOID(long entInsentifMasterId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_INSENTIF_MASTER + " WHERE "
                    + PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_INSENTIF_MASTER_ID] + " = " + entInsentifMasterId;
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
            String sql = "SELECT COUNT(" + PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_INSENTIF_MASTER_ID] + ") FROM " + TBL_INSENTIF_MASTER;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
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

    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    InsentifMaster entInsentifMaster = (InsentifMaster) list.get(ls);
                    if (oid == entInsentifMaster.getOID()) {
                        found = true;
                    }
                }
            }
        }
        if ((start >= size) && (size > 0)) {
            start = start - recordToGet;
        }
        return start;
    }

    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + (recordToGet - mdl);
        if (start == 0) {
            cmd = Command.FIRST;
        } else {
            if (start == (vectSize - recordToGet)) {
                cmd = Command.LAST;
            } else {
                start = start + recordToGet;
                if (start <= (vectSize - recordToGet)) {
                    cmd = Command.NEXT;
                    System.out.println("next.......................");
                } else {
                    start = start - recordToGet;
                    if (start > 0) {
                        cmd = Command.PREV;
                        System.out.println("prev.......................");
                    }
                }
            }
        }
        return cmd;
    }
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                InsentifMaster insentifMaster = PstInsentifMaster.fetchExc(oid);
                object.put(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_INSENTIF_MASTER_ID], insentifMaster.getOID());
                object.put(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_CATEGORY_ID], insentifMaster.getCategoryId());
                object.put(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_DEPEND_ON_POSITION], insentifMaster.getDependOnPosition());
                object.put(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_DIVISION_POINT], insentifMaster.getDivisionPoint());
                object.put(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_FAKTOR_NOMINAL_INSENTIF], insentifMaster.getFaktorNominalInsentif());
                object.put(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_INCLUDE_COST_OF_SALES], insentifMaster.getIncludeCostOfSales());
                object.put(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_INCLUDE_SALES_PROFIT], insentifMaster.getIncludeSalesProfit());
                object.put(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_STATUS], insentifMaster.getStatus());
                object.put(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_MATERIAL_MAIN], insentifMaster.getMaterialMain());
                object.put(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_PERIODE_END], insentifMaster.getPeriodeEnd());
                object.put(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_PERIODE_FOREVER], insentifMaster.getPeriodeForever());
                object.put(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_PERIODE_START], insentifMaster.getPeriodeStart());
                object.put(PstInsentifMaster.fieldNames[PstInsentifMaster.FLD_TITLE], insentifMaster.getTitle());
            }catch(Exception exc){}

            return object;
        }
}
