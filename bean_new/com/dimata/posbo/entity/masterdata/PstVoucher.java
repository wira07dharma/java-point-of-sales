/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author dimata005
 */
import com.dimata.common.entity.location.PstLocation;
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;
import org.json.JSONObject;

public class PstVoucher extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_VOUCHER = "pos_voucher";
    public static final int FLD_VOUCHERID = 0;
    public static final int FLD_VOUCHERNO = 1;
    public static final int FLD_VOUCHERNAME = 2;
    public static final int FLD_VOUCHERNOMINAL = 3;
    public static final int FLD_VOUCHERTYPE = 4;
    public static final int FLD_VOUCHERCREATEDATE = 5;
    public static final int FLD_VOUCHERISSUEDDATE = 6;
    public static final int FLD_VOUCHEREXPIRED = 7;
    public static final int FLD_VOUCHEROUTLET = 8;
    public static final int FLD_VOUCHERAUTHORIZEDNAME = 9;
    public static final int FLD_VOUCHERAUTHORIZEDID = 10;
    public static final int FLD_VOUCHERSTATUS = 11;
    public static final int FLD_VOUCHERISSUEDTO = 12;
    public static final int FLD_VOUCHER_BARCODE = 13;
    public static final int FLD_VOUCHER_REF_CASH_BILL_MAIN_ID = 14;
    
    public static String[] fieldNames = {
        "VOUCHECR_ID",
        "VOUCHER_NO",
        "VOUCHER_NAME",
        "VOUCHER_NOMINAL",
        "VOUCHER_TYPE",
        "VOUCHER_CREATE_DATE",
        "VOUCHER_ISSUED_DATE",
        "VOUCHER_EXPIRED",
        "VOUCHER_OUTLET",
        "VOUCHER_AUTHORIZED_NAME",
        "VOUCHER_AUTHORIZED_ID",
        "VOUCHER_STATUS",
        "VOUCHER_ISSUED_TO",
        "VOUCHER_BARCODE",
        "REF_CASH_BILL_MAIN_ID"
    };
    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG
    };

    public PstVoucher() {
    }

    public PstVoucher(int i) throws DBException {
        super(new PstVoucher());
    }

    public PstVoucher(String sOid) throws DBException {
        super(new PstVoucher(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstVoucher(long lOid) throws DBException {
        super(new PstVoucher(0));
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
        return TBL_VOUCHER;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstVoucher().getClass().getName();
    }

    public static Voucher fetchExc(long oid) throws DBException {
        try {
            Voucher entVoucher = new Voucher();
            PstVoucher pstVoucher = new PstVoucher(oid);
            entVoucher.setOID(oid);
            entVoucher.setVoucherNo(pstVoucher.getString(FLD_VOUCHERNO));
            entVoucher.setVoucherName(pstVoucher.getString(FLD_VOUCHERNAME));
            entVoucher.setVoucherNominal(pstVoucher.getdouble(FLD_VOUCHERNOMINAL));
            entVoucher.setVoucherType(pstVoucher.getInt(FLD_VOUCHERTYPE));
            entVoucher.setVoucherCreateDate(pstVoucher.getDate(FLD_VOUCHERCREATEDATE));
            entVoucher.setVoucherIssuedDate(pstVoucher.getDate(FLD_VOUCHERISSUEDDATE));
            entVoucher.setVoucherExpired(pstVoucher.getDate(FLD_VOUCHEREXPIRED));
            entVoucher.setVoucherOutlet(pstVoucher.getlong(FLD_VOUCHEROUTLET));
            entVoucher.setVoucherAuthorizedName(pstVoucher.getString(FLD_VOUCHERAUTHORIZEDNAME));
            entVoucher.setVoucherAuthorizedId(pstVoucher.getlong(FLD_VOUCHERAUTHORIZEDID));
            entVoucher.setVoucherStatus(pstVoucher.getInt(FLD_VOUCHERSTATUS));
            entVoucher.setVoucherIssuedTo(pstVoucher.getString(FLD_VOUCHERISSUEDTO));
            entVoucher.setBarcode(pstVoucher.getString(FLD_VOUCHER_BARCODE));
            entVoucher.setRefCashBillMainId(pstVoucher.getlong(FLD_VOUCHER_REF_CASH_BILL_MAIN_ID));
            
            return entVoucher;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstVoucher(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        Voucher entVoucher = fetchExc(entity.getOID());
        entity = (Entity) entVoucher;
        return entVoucher.getOID();
    }

    public static synchronized long updateExc(Voucher entVoucher) throws DBException {
        try {
            if (entVoucher.getOID() != 0) {
                PstVoucher pstVoucher = new PstVoucher(entVoucher.getOID());
                pstVoucher.setString(FLD_VOUCHERNO, entVoucher.getVoucherNo());
                pstVoucher.setString(FLD_VOUCHERNAME, entVoucher.getVoucherName());
                pstVoucher.setDouble(FLD_VOUCHERNOMINAL, entVoucher.getVoucherNominal());
                pstVoucher.setInt(FLD_VOUCHERTYPE, entVoucher.getVoucherType());
                pstVoucher.setDate(FLD_VOUCHERCREATEDATE, entVoucher.getVoucherCreateDate());
                pstVoucher.setDate(FLD_VOUCHERISSUEDDATE, entVoucher.getVoucherIssuedDate());
                pstVoucher.setDate(FLD_VOUCHEREXPIRED, entVoucher.getVoucherExpired());
                pstVoucher.setLong(FLD_VOUCHEROUTLET, entVoucher.getVoucherOutlet());
                pstVoucher.setString(FLD_VOUCHERAUTHORIZEDNAME, entVoucher.getVoucherAuthorizedName());
                pstVoucher.setLong(FLD_VOUCHERAUTHORIZEDID, entVoucher.getVoucherAuthorizedId());
                pstVoucher.setInt(FLD_VOUCHERSTATUS, entVoucher.getVoucherStatus());
                pstVoucher.setString(FLD_VOUCHERISSUEDTO, entVoucher.getVoucherIssuedTo());
                pstVoucher.setString(FLD_VOUCHER_BARCODE, entVoucher.getBarcode());
                pstVoucher.setLong(FLD_VOUCHER_REF_CASH_BILL_MAIN_ID, entVoucher.getRefCashBillMainId());
                
                pstVoucher.update();
                return entVoucher.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstVoucher(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((Voucher) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstVoucher pstVoucher = new PstVoucher(oid);
            pstVoucher.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstVoucher(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(Voucher entVoucher) throws DBException {
        try {
            PstVoucher pstVoucher = new PstVoucher(0);
            pstVoucher.setString(FLD_VOUCHERNO, entVoucher.getVoucherNo());
            pstVoucher.setString(FLD_VOUCHERNAME, entVoucher.getVoucherName());
            pstVoucher.setDouble(FLD_VOUCHERNOMINAL, entVoucher.getVoucherNominal());
            pstVoucher.setInt(FLD_VOUCHERTYPE, entVoucher.getVoucherType());
            pstVoucher.setDate(FLD_VOUCHERCREATEDATE, entVoucher.getVoucherCreateDate());
            pstVoucher.setDate(FLD_VOUCHERISSUEDDATE, entVoucher.getVoucherIssuedDate());
            pstVoucher.setDate(FLD_VOUCHEREXPIRED, entVoucher.getVoucherExpired());
            pstVoucher.setLong(FLD_VOUCHEROUTLET, entVoucher.getVoucherOutlet());
            pstVoucher.setString(FLD_VOUCHERAUTHORIZEDNAME, entVoucher.getVoucherAuthorizedName());
            pstVoucher.setLong(FLD_VOUCHERAUTHORIZEDID, entVoucher.getVoucherAuthorizedId());
            pstVoucher.setInt(FLD_VOUCHERSTATUS, entVoucher.getVoucherStatus());
            pstVoucher.setString(FLD_VOUCHERISSUEDTO, entVoucher.getVoucherIssuedTo());
            pstVoucher.setString(FLD_VOUCHER_BARCODE, entVoucher.getBarcode());
            pstVoucher.setLong(FLD_VOUCHER_REF_CASH_BILL_MAIN_ID, entVoucher.getRefCashBillMainId());
                
            pstVoucher.insert();
            entVoucher.setOID(pstVoucher.getlong(FLD_VOUCHERID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstVoucher(0), DBException.UNKNOWN);
        }
        return entVoucher.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((Voucher) entity);
    }

    public static void resultToObject(ResultSet rs, Voucher entVoucher) {
        try {
            entVoucher.setOID(rs.getLong(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERID]));
            entVoucher.setVoucherNo(rs.getString(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERNO]));
            entVoucher.setVoucherName(rs.getString(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERNAME]));
            entVoucher.setVoucherNominal(rs.getDouble(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERNOMINAL]));
            entVoucher.setVoucherType(rs.getInt(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERTYPE]));
            entVoucher.setVoucherCreateDate(rs.getDate(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERCREATEDATE]));
            entVoucher.setVoucherIssuedDate(rs.getDate(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERISSUEDDATE]));
            entVoucher.setVoucherExpired(rs.getDate(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHEREXPIRED]));
            entVoucher.setVoucherOutlet(rs.getLong(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHEROUTLET]));
            entVoucher.setVoucherAuthorizedName(rs.getString(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERAUTHORIZEDNAME]));
            entVoucher.setVoucherAuthorizedId(rs.getLong(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERAUTHORIZEDID]));
            entVoucher.setVoucherStatus(rs.getInt(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERSTATUS]));
            entVoucher.setVoucherIssuedTo(rs.getString(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERISSUEDTO]));
            entVoucher.setBarcode(rs.getString(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHER_BARCODE]));
            entVoucher.setRefCashBillMainId(rs.getLong(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHER_REF_CASH_BILL_MAIN_ID]));
            
        } catch (Exception e) {
        }
    }
    
    public static void resultToObjectJoin(ResultSet rs, Voucher entVoucher) {
        try {
            entVoucher.setOID(rs.getLong(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERID]));
            entVoucher.setVoucherNo(rs.getString(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERNO]));
            entVoucher.setVoucherName(rs.getString(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERNAME]));
            entVoucher.setVoucherNominal(rs.getDouble(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERNOMINAL]));
            entVoucher.setVoucherType(rs.getInt(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERTYPE]));
            entVoucher.setVoucherCreateDate(rs.getDate(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERCREATEDATE]));
            entVoucher.setVoucherIssuedDate(rs.getDate(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERISSUEDDATE]));
            entVoucher.setVoucherExpired(rs.getDate(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHEREXPIRED]));
            entVoucher.setVoucherOutlet(rs.getLong(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHEROUTLET]));
            entVoucher.setVoucherAuthorizedName(rs.getString(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERAUTHORIZEDNAME]));
            entVoucher.setVoucherAuthorizedId(rs.getLong(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERAUTHORIZEDID]));
            entVoucher.setVoucherStatus(rs.getInt(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERSTATUS]));
            entVoucher.setVoucherIssuedTo(rs.getString(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERISSUEDTO]));
            entVoucher.setVoucherOutletName(rs.getString(PstLocation.fieldNames[PstLocation.FLD_NAME]));
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
            String sql = "SELECT * FROM " + TBL_VOUCHER;
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
                Voucher entVoucher = new Voucher();
                resultToObject(rs, entVoucher);
                lists.add(entVoucher);
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
    
    public static Vector listJoin(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            
            String sql =  " SELECT vc.*, lc."+PstLocation.fieldNames[PstLocation.FLD_NAME]  +" FROM " + TBL_VOUCHER + " AS vc "
                        + " INNER JOIN "+PstLocation.TBL_P2_LOCATION+" AS lc "
                        + " ON lc."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+" = vc."+PstVoucher.fieldNames[PstVoucher.FLD_VOUCHEROUTLET];
            
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
                Voucher entVoucher = new Voucher();
                resultToObjectJoin(rs, entVoucher);
                lists.add(entVoucher);
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
    

    public static boolean checkOID(long entVoucherId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_VOUCHER + " WHERE "
                    + PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERID] + " = " + entVoucherId;
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
    
    
    public static boolean checkKode(String entVoucherKode) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_VOUCHER + " WHERE "
                    + PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERNO] + " = " + entVoucherKode;
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
            String sql = "SELECT COUNT(" + PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERID] + ") FROM " + TBL_VOUCHER;
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
                    Voucher entVoucher = (Voucher) list.get(ls);
                    if (oid == entVoucher.getOID()) {
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
                Voucher voucher = PstVoucher.fetchExc(oid);
                object.put(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERID], voucher.getOID());
                object.put(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERAUTHORIZEDID], voucher.getVoucherAuthorizedId());
                object.put(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERAUTHORIZEDNAME], voucher.getVoucherAuthorizedName());
                object.put(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERCREATEDATE], voucher.getVoucherCreateDate());
                object.put(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHEREXPIRED], voucher.getVoucherExpired());
                object.put(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERISSUEDDATE], voucher.getVoucherIssuedDate());
                object.put(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERISSUEDTO], voucher.getVoucherIssuedTo());
                object.put(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERNAME], voucher.getVoucherName());
                object.put(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERNO], voucher.getVoucherNo());
                object.put(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERNOMINAL], voucher.getVoucherNominal());
                object.put(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHEROUTLET], voucher.getVoucherNominal());
                object.put(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERSTATUS], voucher.getVoucherStatus());
                object.put(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHERTYPE], voucher.getVoucherType());
                object.put(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHER_BARCODE], voucher.getBarcode());
                object.put(PstVoucher.fieldNames[PstVoucher.FLD_VOUCHER_REF_CASH_BILL_MAIN_ID], voucher.getRefCashBillMainId());
                
            }catch(Exception exc){}

            return object;
        }
}