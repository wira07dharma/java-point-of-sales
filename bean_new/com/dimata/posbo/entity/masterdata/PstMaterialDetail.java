/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author dimata005
 */
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;
import org.json.JSONObject;

public class PstMaterialDetail extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_MATERIALDETAIL = "pos_material_detail";
    public static final int FLD_MATERIAL_DETAIL_ID = 0;
    public static final int FLD_MATERIAL_ID = 1;
    public static final int FLD_QTY = 2;
    public static final int FLD_BERAT = 3;
    public static final int FLD_HARGA_BELI = 4;
    public static final int FLD_HARGA_JUAL = 5;
    public static final int FLD_RATE = 6;
    public static final int FLD_FAKTOR_JUAL = 7;
    public static final int FLD_UPHET_PERSENTASE = 8;
    public static final int FLD_UPHET_VALUE = 9;
    public static final int FLD_UPHET_PERSENTASETOT = 10;
    public static final int FLD_UPHET_VALUE_TOT = 11;
    public static final int FLD_ONGKOS = 12;

    public static String[] fieldNames = {
        "MATERIAL_DETAIL_ID",
        "MATERIAL_ID",
        "QTY",
        "BERAT",
        "HARGA_BELI",
        "HARGA_JUAL",
        "RATE",
        "FAKTOR_JUAL",
        "UPHET_PERSENTASE",
        "UPHET_VALUE",
        "UPHET_PERSENTASE_TOT",
        "UPHET_VALUE_TOTAL",
        "ONGKOS"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT
    };

    public PstMaterialDetail() {
    }

    public PstMaterialDetail(int i) throws DBException {
        super(new PstMaterialDetail());
    }

    public PstMaterialDetail(String sOid) throws DBException {
        super(new PstMaterialDetail(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMaterialDetail(long lOid) throws DBException {
        super(new PstMaterialDetail(0));
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
        return TBL_MATERIALDETAIL;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMaterialDetail().getClass().getName();
    }

    public static MaterialDetail fetchExc(long oid) throws DBException {
        try {
            MaterialDetail entMaterialDetail = new MaterialDetail();
            PstMaterialDetail pstMaterialDetail = new PstMaterialDetail(oid);
            entMaterialDetail.setOID(oid);
            entMaterialDetail.setMaterialId(pstMaterialDetail.getlong(FLD_MATERIAL_ID));
            entMaterialDetail.setQty(pstMaterialDetail.getdouble(FLD_QTY));
            entMaterialDetail.setBerat(pstMaterialDetail.getdouble(FLD_BERAT));
            entMaterialDetail.setHargaBeli(pstMaterialDetail.getdouble(FLD_HARGA_BELI));
            entMaterialDetail.setHargaJual(pstMaterialDetail.getdouble(FLD_HARGA_JUAL));
            entMaterialDetail.setRate(pstMaterialDetail.getdouble(FLD_RATE));
            entMaterialDetail.setFaktorJual(pstMaterialDetail.getdouble(FLD_FAKTOR_JUAL));
            entMaterialDetail.setUphetPersentase(pstMaterialDetail.getdouble(FLD_UPHET_PERSENTASE));
            entMaterialDetail.setUphetValue(pstMaterialDetail.getdouble(FLD_UPHET_VALUE));
            entMaterialDetail.setUphetPersentaseTot(pstMaterialDetail.getdouble(FLD_UPHET_PERSENTASETOT));
            entMaterialDetail.setUphetValueTot(pstMaterialDetail.getdouble(FLD_UPHET_VALUE_TOT));                        
            entMaterialDetail.setBerat(pstMaterialDetail.getdouble(FLD_BERAT));
            entMaterialDetail.setOngkos(pstMaterialDetail.getdouble(FLD_ONGKOS));
            return entMaterialDetail;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialDetail(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        MaterialDetail entMaterialDetail = fetchExc(entity.getOID());
        entity = (Entity) entMaterialDetail;
        return entMaterialDetail.getOID();
    }

    public static synchronized long updateExc(MaterialDetail entMaterialDetail) throws DBException {
        try {
            if (entMaterialDetail.getOID() != 0) {
                PstMaterialDetail pstMaterialDetail = new PstMaterialDetail(entMaterialDetail.getOID());
                pstMaterialDetail.setLong(FLD_MATERIAL_ID, entMaterialDetail.getMaterialId());
                pstMaterialDetail.setDouble(FLD_QTY, entMaterialDetail.getQty());
                pstMaterialDetail.setDouble(FLD_BERAT, entMaterialDetail.getBerat());
                pstMaterialDetail.setDouble(FLD_HARGA_BELI, entMaterialDetail.getHargaBeli());
                pstMaterialDetail.setDouble(FLD_HARGA_JUAL, entMaterialDetail.getHargaJual());
                pstMaterialDetail.setDouble(FLD_RATE, entMaterialDetail.getRate());
                pstMaterialDetail.setDouble(FLD_FAKTOR_JUAL, entMaterialDetail.getFaktorJual());
                pstMaterialDetail.setDouble(FLD_UPHET_PERSENTASE, entMaterialDetail.getUphetPersentase());
                pstMaterialDetail.setDouble(FLD_UPHET_VALUE, entMaterialDetail.getUphetValue());
                pstMaterialDetail.setDouble(FLD_UPHET_PERSENTASETOT, entMaterialDetail.getUphetPersentaseTot());
                pstMaterialDetail.setDouble(FLD_UPHET_VALUE_TOT, entMaterialDetail.getUphetValueTot());
                pstMaterialDetail.setDouble(FLD_ONGKOS, entMaterialDetail.getOngkos());
                pstMaterialDetail.update();
                return entMaterialDetail.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialDetail(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((MaterialDetail) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstMaterialDetail pstMaterialDetail = new PstMaterialDetail(oid);
            pstMaterialDetail.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialDetail(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(MaterialDetail entMaterialDetail) throws DBException {
        try {
            PstMaterialDetail pstMaterialDetail = new PstMaterialDetail(0);
            pstMaterialDetail.setLong(FLD_MATERIAL_ID, entMaterialDetail.getMaterialId());
            pstMaterialDetail.setDouble(FLD_QTY, entMaterialDetail.getQty());
            pstMaterialDetail.setDouble(FLD_BERAT, entMaterialDetail.getBerat());
            pstMaterialDetail.setDouble(FLD_HARGA_BELI, entMaterialDetail.getHargaBeli());
            pstMaterialDetail.setDouble(FLD_HARGA_JUAL, entMaterialDetail.getHargaJual());
            pstMaterialDetail.setDouble(FLD_RATE, entMaterialDetail.getRate());
            pstMaterialDetail.setDouble(FLD_FAKTOR_JUAL, entMaterialDetail.getFaktorJual());
            pstMaterialDetail.setDouble(FLD_UPHET_PERSENTASE, entMaterialDetail.getUphetPersentase());
            pstMaterialDetail.setDouble(FLD_UPHET_VALUE, entMaterialDetail.getUphetValue());
            pstMaterialDetail.setDouble(FLD_UPHET_PERSENTASETOT, entMaterialDetail.getUphetPersentaseTot());
            pstMaterialDetail.setDouble(FLD_UPHET_VALUE_TOT, entMaterialDetail.getUphetValueTot());
            pstMaterialDetail.setDouble(FLD_ONGKOS, entMaterialDetail.getUphetValueTot());
            pstMaterialDetail.insert();
            entMaterialDetail.setOID(pstMaterialDetail.getlong(FLD_MATERIAL_DETAIL_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMaterialDetail(0), DBException.UNKNOWN);
        }
        return entMaterialDetail.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((MaterialDetail) entity);
    }

    public static void resultToObject(ResultSet rs, MaterialDetail entMaterialDetail) {
        try {
            entMaterialDetail.setOID(rs.getLong(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_DETAIL_ID]));
            entMaterialDetail.setMaterialId(rs.getLong(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_ID]));
            entMaterialDetail.setQty(rs.getDouble(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_QTY]));
            entMaterialDetail.setBerat(rs.getDouble(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_BERAT]));
            entMaterialDetail.setHargaBeli(rs.getDouble(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_HARGA_BELI]));
            entMaterialDetail.setHargaJual(rs.getDouble(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_HARGA_JUAL]));
            entMaterialDetail.setRate(rs.getDouble(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_RATE]));
            entMaterialDetail.setFaktorJual(rs.getDouble(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_FAKTOR_JUAL]));
            entMaterialDetail.setUphetPersentase(rs.getDouble(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_UPHET_PERSENTASE]));
            entMaterialDetail.setUphetValue(rs.getDouble(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_UPHET_VALUE]));
            entMaterialDetail.setUphetPersentaseTot(rs.getDouble(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_UPHET_PERSENTASETOT]));
            entMaterialDetail.setUphetValueTot(rs.getDouble(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_UPHET_VALUE_TOT]));
            entMaterialDetail.setOngkos(rs.getDouble(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_ONGKOS]));
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
            String sql = "SELECT * FROM " + TBL_MATERIALDETAIL;
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
                MaterialDetail entMaterialDetail = new MaterialDetail();
                resultToObject(rs, entMaterialDetail);
                lists.add(entMaterialDetail);
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

    public static boolean checkOID(long entMaterialDetailId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MATERIALDETAIL + " WHERE "
                    + PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_DETAIL_ID] + " = " + entMaterialDetailId;
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
    
    public static long checkOIDMaterialDetailId(long entMaterialId) {
        DBResultSet dbrs = null;
        long result = 0;
        try {
            String sql = "SELECT * FROM " + TBL_MATERIALDETAIL + " WHERE "
                    + PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_ID] + " = " + entMaterialId;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getLong("MATERIAL_DETAIL_ID");
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
            String sql = "SELECT COUNT(" + PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_DETAIL_ID] + ") FROM " + TBL_MATERIALDETAIL;
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
                    MaterialDetail entMaterialDetail = (MaterialDetail) list.get(ls);
                    if (oid == entMaterialDetail.getOID()) {
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
        } else if (start == (vectSize - recordToGet)) {
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
        return cmd;
    }
    
    public static MaterialDetail fetchExcMaterialDetailId(long entMaterialId) {
            DBResultSet dbrs = null;
            MaterialDetail materialDetail = new MaterialDetail();
            try {
                    String sql = "SELECT " + PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_DETAIL_ID] + " FROM " + TBL_MATERIALDETAIL + " WHERE "
                                    + PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_ID] + " = " + entMaterialId;
                    dbrs = DBHandler.execQueryResult(sql);
                    ResultSet rs = dbrs.getResultSet();
                    while (rs.next()) {
                            long materialDetailId = rs.getLong(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_DETAIL_ID]);
                            materialDetail = fetchExc(materialDetailId);
                    }
                    rs.close();
            } catch (Exception e) {
                    System.out.println("err : " + e.toString());
            } finally {
                    DBResultSet.close(dbrs);
                    return materialDetail;
            }
    }
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                MaterialDetail materialDetail = PstMaterialDetail.fetchExc(oid);
                object.put(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_DETAIL_ID], materialDetail.getOID());
                object.put(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_BERAT], materialDetail.getBerat());
                object.put(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_FAKTOR_JUAL], materialDetail.getFaktorJual());
                object.put(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_HARGA_BELI], materialDetail.getHargaBeli());
                object.put(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_ID], materialDetail.getMaterialId());
                object.put(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_ONGKOS], materialDetail.getOngkos());
                object.put(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_QTY], materialDetail.getQty());
                object.put(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_RATE], materialDetail.getRate());
                object.put(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_UPHET_PERSENTASE], materialDetail.getUphetPersentase());
                object.put(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_UPHET_PERSENTASETOT], materialDetail.getUphetPersentaseTot());
                object.put(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_UPHET_VALUE], materialDetail.getUphetValue());
                object.put(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_UPHET_VALUE_TOT], materialDetail.getUphetValueTot());
                object.put(PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_HARGA_JUAL], materialDetail.getHargaJual());
            }catch(Exception exc){}

            return object;
        }    
}
