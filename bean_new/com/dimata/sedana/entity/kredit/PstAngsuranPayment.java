/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.kredit;

/**
 *
 * @author Regen
 */
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

public class PstAngsuranPayment extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_ANGSURANPAYMENT = "sedana_angsuran_payment";
    public static final int FLD_ANGSURAN_PAYMENT_ID = 0;
    public static final int FLD_PAYMENT_SYSTEM_ID = 1;
    public static final int FLD_JUMLAH = 2;
    public static final int FLD_CARD_NAME = 3;
    public static final int FLD_CARD_NO = 4;
    public static final int FLD_BANK_NAME = 5;
    public static final int FLD_VALIDATE_DATE = 6;
    public static final int FLD_STATUS = 7;
    public static final int FLD_TRANSAKSI_ID = 8;
    public static final int FLD_ID_SIMPANAN = 9;

    public static String[] fieldNames = {
        "ANGSURAN_PAYMENT_ID",
        "PAYMENT_SYSTEM_ID",
        "JUMLAH",
        "CARD_NAME",
        "CARD_NO",
        "BANK_NAME",
        "VALIDATE_DATE",
        "STATUS",
        "TRANSAKSI_ID",
        "ID_SIMPANAN"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG
    };

    public PstAngsuranPayment() {
    }

    public PstAngsuranPayment(int i) throws DBException {
        super(new PstAngsuranPayment());
    }

    public PstAngsuranPayment(String sOid) throws DBException {
        super(new PstAngsuranPayment(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstAngsuranPayment(long lOid) throws DBException {
        super(new PstAngsuranPayment(0));
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
        return TBL_ANGSURANPAYMENT;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstAngsuranPayment().getClass().getName();
    }

    public static AngsuranPayment fetchExc(long oid) throws DBException {
        try {
            AngsuranPayment entAngsuranPayment = new AngsuranPayment();
            PstAngsuranPayment pstAngsuranPayment = new PstAngsuranPayment(oid);
            entAngsuranPayment.setOID(oid);
            entAngsuranPayment.setPaymentSystemId(pstAngsuranPayment.getlong(FLD_PAYMENT_SYSTEM_ID));
            entAngsuranPayment.setJumlah(pstAngsuranPayment.getdouble(FLD_JUMLAH));
            entAngsuranPayment.setCardName(pstAngsuranPayment.getString(FLD_CARD_NAME));
            entAngsuranPayment.setCardNumber(pstAngsuranPayment.getString(FLD_CARD_NO));
            entAngsuranPayment.setBankName(pstAngsuranPayment.getString(FLD_BANK_NAME));
            entAngsuranPayment.setValidateDate(pstAngsuranPayment.getDate(FLD_VALIDATE_DATE));
            entAngsuranPayment.setStatus(pstAngsuranPayment.getInt(FLD_STATUS));
            entAngsuranPayment.setTransaksiId(pstAngsuranPayment.getlong(FLD_TRANSAKSI_ID));
            entAngsuranPayment.setIdSimpanan(pstAngsuranPayment.getlong(FLD_ID_SIMPANAN));
            return entAngsuranPayment;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAngsuranPayment(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        AngsuranPayment entAngsuranPayment = fetchExc(entity.getOID());
        entity = (Entity) entAngsuranPayment;
        return entAngsuranPayment.getOID();
    }

    public static synchronized long updateExc(AngsuranPayment entAngsuranPayment) throws DBException {
        try {
            if (entAngsuranPayment.getOID() != 0) {
                PstAngsuranPayment pstAngsuranPayment = new PstAngsuranPayment(entAngsuranPayment.getOID());
                pstAngsuranPayment.setLong(FLD_PAYMENT_SYSTEM_ID, entAngsuranPayment.getPaymentSystemId());
                pstAngsuranPayment.setDouble(FLD_JUMLAH, entAngsuranPayment.getJumlah());
                pstAngsuranPayment.setString(FLD_CARD_NAME, entAngsuranPayment.getCardName());
                pstAngsuranPayment.setString(FLD_CARD_NO, entAngsuranPayment.getCardNumber());
                pstAngsuranPayment.setString(FLD_BANK_NAME, entAngsuranPayment.getBankName());
                pstAngsuranPayment.setDate(FLD_VALIDATE_DATE, entAngsuranPayment.getValidateDate());
                pstAngsuranPayment.setInt(FLD_STATUS, entAngsuranPayment.getStatus());
                pstAngsuranPayment.setLong(FLD_TRANSAKSI_ID, entAngsuranPayment.getTransaksiId());
                if (entAngsuranPayment.getIdSimpanan() != 0) {
                    pstAngsuranPayment.setLong(FLD_ID_SIMPANAN, entAngsuranPayment.getIdSimpanan());
                }
                pstAngsuranPayment.update();
                return entAngsuranPayment.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAngsuranPayment(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((AngsuranPayment) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstAngsuranPayment pstAngsuranPayment = new PstAngsuranPayment(oid);
            pstAngsuranPayment.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAngsuranPayment(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(AngsuranPayment entAngsuranPayment) throws DBException {
        try {
            PstAngsuranPayment pstAngsuranPayment = new PstAngsuranPayment(0);
            pstAngsuranPayment.setLong(FLD_PAYMENT_SYSTEM_ID, entAngsuranPayment.getPaymentSystemId());
            pstAngsuranPayment.setDouble(FLD_JUMLAH, entAngsuranPayment.getJumlah());
            pstAngsuranPayment.setString(FLD_CARD_NAME, entAngsuranPayment.getCardName());
            pstAngsuranPayment.setString(FLD_CARD_NO, entAngsuranPayment.getCardNumber());
            pstAngsuranPayment.setString(FLD_BANK_NAME, entAngsuranPayment.getBankName());
            pstAngsuranPayment.setDate(FLD_VALIDATE_DATE, entAngsuranPayment.getValidateDate());
            pstAngsuranPayment.setInt(FLD_STATUS, entAngsuranPayment.getStatus());
            pstAngsuranPayment.setLong(FLD_TRANSAKSI_ID, entAngsuranPayment.getTransaksiId());
            if (entAngsuranPayment.getIdSimpanan() != 0) {
                pstAngsuranPayment.setLong(FLD_ID_SIMPANAN, entAngsuranPayment.getIdSimpanan());
            }
            pstAngsuranPayment.insert();
            entAngsuranPayment.setOID(pstAngsuranPayment.getlong(FLD_ANGSURAN_PAYMENT_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAngsuranPayment(0), DBException.UNKNOWN);
        }
        return entAngsuranPayment.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((AngsuranPayment) entity);
    }

    public static void resultToObject(ResultSet rs, AngsuranPayment entAngsuranPayment) {
        try {
            entAngsuranPayment.setOID(rs.getLong(PstAngsuranPayment.fieldNames[PstAngsuranPayment.FLD_ANGSURAN_PAYMENT_ID]));
            entAngsuranPayment.setPaymentSystemId(rs.getLong(PstAngsuranPayment.fieldNames[PstAngsuranPayment.FLD_PAYMENT_SYSTEM_ID]));
            entAngsuranPayment.setJumlah(rs.getDouble(PstAngsuranPayment.fieldNames[PstAngsuranPayment.FLD_JUMLAH]));
            entAngsuranPayment.setCardName(rs.getString(PstAngsuranPayment.fieldNames[PstAngsuranPayment.FLD_CARD_NAME]));
            entAngsuranPayment.setCardNumber(rs.getString(PstAngsuranPayment.fieldNames[PstAngsuranPayment.FLD_CARD_NO]));
            entAngsuranPayment.setBankName(rs.getString(PstAngsuranPayment.fieldNames[PstAngsuranPayment.FLD_BANK_NAME]));
            entAngsuranPayment.setValidateDate(rs.getDate(PstAngsuranPayment.fieldNames[PstAngsuranPayment.FLD_VALIDATE_DATE]));
            entAngsuranPayment.setStatus(rs.getInt(PstAngsuranPayment.fieldNames[PstAngsuranPayment.FLD_STATUS]));
            entAngsuranPayment.setTransaksiId(rs.getLong(PstAngsuranPayment.fieldNames[PstAngsuranPayment.FLD_TRANSAKSI_ID]));
            entAngsuranPayment.setIdSimpanan(rs.getLong(PstAngsuranPayment.fieldNames[PstAngsuranPayment.FLD_ID_SIMPANAN]));
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
            String sql = "SELECT * FROM " + TBL_ANGSURANPAYMENT;
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
                AngsuranPayment entAngsuranPayment = new AngsuranPayment();
                resultToObject(rs, entAngsuranPayment);
                lists.add(entAngsuranPayment);
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

    public static boolean checkOID(long entAngsuranPaymentId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_ANGSURANPAYMENT + " WHERE "
                    + PstAngsuranPayment.fieldNames[PstAngsuranPayment.FLD_ANGSURAN_PAYMENT_ID] + " = " + entAngsuranPaymentId;
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
            String sql = "SELECT COUNT(" + PstAngsuranPayment.fieldNames[PstAngsuranPayment.FLD_ANGSURAN_PAYMENT_ID] + ") FROM " + TBL_ANGSURANPAYMENT;
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
                    AngsuranPayment entAngsuranPayment = (AngsuranPayment) list.get(ls);
                    if (oid == entAngsuranPayment.getOID()) {
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
}
