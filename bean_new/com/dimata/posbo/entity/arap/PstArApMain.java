package com.dimata.posbo.entity.arap;

import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.Entity;
import com.dimata.util.Formater;
import com.dimata.posbo.db.*;

import java.util.Vector;
import java.sql.ResultSet;
import org.json.JSONObject;

public class PstArApMain extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc {

    public static final String TBL_ARAP_MAIN = "aiso_arap_main";
    public static final int FLD_ARAP_MAIN_ID = 0;
    public static final int FLD_VOUCHER_NO = 1;
    public static final int FLD_VOUCHER_DATE = 2;
    public static final int FLD_CONTACT_ID = 3;
    public static final int FLD_NUMBER_OF_PAYMENT = 4;
    public static final int FLD_ID_PERKIRAAN_LAWAN = 5;
    public static final int FLD_ID_PERKIRAAN = 6;
    public static final int FLD_ID_CURRENCY = 7;
    public static final int FLD_COUNTER = 8;
    public static final int FLD_RATE = 9;
    public static final int FLD_AMOUNT = 10;
    public static final int FLD_NOTA_NO = 11;
    public static final int FLD_NOTA_DATE = 12;
    public static final int FLD_DESCRIPTION = 13;
    public static final int FLD_ARAP_MAIN_STATUS = 14;
    public static final int FLD_ARAP_TYPE = 15;
    public static final int FLD_ARAP_DOC_STATUS = 16;

    public static String[] fieldNames = {
        "ARAP_MAIN_ID",
        "VOUCHER_NO",
        "VOUCHER_DATE",
        "CONTACT_ID",
        "NUMBER_OF_PAYMENT",
        "ID_PERKIRAAN_LAWAN",
        "ID_PERKIRAAN",
        "ID_CURRENCY",
        "COUNTER",
        "RATE",
        "AMOUNT",
        "NOTA_NO",
        "NOTA_DATE",
        "DESCRIPTION",
        "ARAP_MAIN_STATUS",
        "ARAP_TYPE",
        "ARAP_DOC_STATUS"
    };

    public static int[] fieldTypes = {
        TYPE_PK + TYPE_ID + TYPE_LONG,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT
    };

    // document status untuk ArAp Main
    public static final int STATUS_OPEN = 0;
    public static final int STATUS_CLOSED = 1;

    // type ar/ap
    public static final int TYPE_AR = 0;
    public static final int TYPE_AP = 1;

    public static final String[][] stTypeArAp = {
        {
            "Piutang",
            "Hutang"
        }   ,
        {
            "Receivable",
            "Payable"
        }
    };

    public PstArApMain() {
    }

    public PstArApMain(int i) throws DBException {
        super(new PstArApMain());
    }

    public PstArApMain(String sOid) throws DBException {
        super(new PstArApMain(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstArApMain(long lOid) throws DBException {
        super(new PstArApMain(0));
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
        return TBL_ARAP_MAIN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstArApMain().getClass().getName();
    }

    public long fetchExc(Entity ent) throws DBException {
        ArApMain arap = fetchExc(ent.getOID());
        ent = (Entity) arap;
        return arap.getOID();
    }

    public long insertExc(Entity ent) throws DBException {
        return insertExc((ArApMain) ent);
    }

    public long updateExc(Entity ent) throws DBException {
        return updateExc((ArApMain) ent);
    }

    public long deleteExc(Entity ent) throws DBException {
        if (ent == null) {
            throw  new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static ArApMain fetchExc(long Oid) throws DBException {
        try {
            ArApMain arap = new ArApMain();
            PstArApMain pstArApMain = new PstArApMain(Oid);
            arap.setOID(Oid);

            arap.setVoucherNo(pstArApMain.getString(FLD_VOUCHER_NO));
            arap.setVoucherDate(pstArApMain.getDate(FLD_VOUCHER_DATE));
            arap.setNumberOfPayment(pstArApMain.getInt(FLD_NUMBER_OF_PAYMENT));
            arap.setContactId(pstArApMain.getlong(FLD_CONTACT_ID));
            arap.setIdPerkiraanLawan(pstArApMain.getlong(FLD_ID_PERKIRAAN_LAWAN));
            arap.setIdPerkiraan(pstArApMain.getlong(FLD_ID_PERKIRAAN));
            arap.setIdCurrency(pstArApMain.getlong(FLD_ID_CURRENCY));
            arap.setCounter(pstArApMain.getInt(FLD_COUNTER));
            arap.setRate(pstArApMain.getdouble(FLD_RATE));
            arap.setAmount(pstArApMain.getdouble(FLD_AMOUNT));
            arap.setNotaNo(pstArApMain.getString(FLD_NOTA_NO));
            arap.setNotaDate(pstArApMain.getDate(FLD_NOTA_DATE));
            arap.setDescription(pstArApMain.getString(FLD_DESCRIPTION));
            arap.setArApMainStatus(pstArApMain.getInt(FLD_ARAP_MAIN_STATUS));
            arap.setArApType(pstArApMain.getInt(FLD_ARAP_TYPE));
            arap.setArApDocStatus(pstArApMain.getInt(FLD_ARAP_DOC_STATUS));

            return arap;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstArApMain(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(ArApMain arap) throws DBException {
        try {
            PstArApMain pstArApMain = new PstArApMain(0);

            pstArApMain.setString(FLD_VOUCHER_NO, arap.getVoucherNo());
            pstArApMain.setInt(FLD_NUMBER_OF_PAYMENT, arap.getNumberOfPayment());
            pstArApMain.setDate(FLD_VOUCHER_DATE, arap.getVoucherDate());
            pstArApMain.setLong(FLD_CONTACT_ID, arap.getContactId());
            pstArApMain.setLong(FLD_ID_PERKIRAAN_LAWAN, arap.getIdPerkiraanLawan());
            pstArApMain.setLong(FLD_ID_PERKIRAAN, arap.getIdPerkiraan());
            pstArApMain.setLong(FLD_ID_CURRENCY, arap.getIdCurrency());
            pstArApMain.setInt(FLD_COUNTER, arap.getCounter());
            pstArApMain.setDouble(FLD_RATE, arap.getRate());
            pstArApMain.setDouble(FLD_AMOUNT, arap.getAmount());
            pstArApMain.setString(FLD_NOTA_NO, arap.getNotaNo());
            pstArApMain.setDate(FLD_NOTA_DATE, arap.getNotaDate());
            pstArApMain.setString(FLD_DESCRIPTION, arap.getDescription());
            pstArApMain.setInt(FLD_ARAP_MAIN_STATUS, arap.getArApMainStatus());
            pstArApMain.setInt(FLD_ARAP_TYPE, arap.getArApType());
            pstArApMain.setInt(FLD_ARAP_DOC_STATUS, arap.getArApDocStatus());

            pstArApMain.insert();
            arap.setOID(pstArApMain.getlong(FLD_ARAP_MAIN_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstArApMain(0), DBException.UNKNOWN);
        }
        return arap.getOID();
    }

    public static long updateExc(ArApMain arap) throws DBException {
        try {
            if (arap != null && arap.getOID() != 0) {
                PstArApMain pstArApMain = new PstArApMain(arap.getOID());

                pstArApMain.setString(FLD_VOUCHER_NO, arap.getVoucherNo());
                pstArApMain.setInt(FLD_NUMBER_OF_PAYMENT, arap.getNumberOfPayment());
                pstArApMain.setDate(FLD_VOUCHER_DATE, arap.getVoucherDate());
                pstArApMain.setLong(FLD_CONTACT_ID, arap.getContactId());
                pstArApMain.setLong(FLD_ID_PERKIRAAN_LAWAN, arap.getIdPerkiraanLawan());
                pstArApMain.setLong(FLD_ID_PERKIRAAN, arap.getIdPerkiraan());
                pstArApMain.setLong(FLD_ID_CURRENCY, arap.getIdCurrency());
                pstArApMain.setInt(FLD_COUNTER, arap.getCounter());
                pstArApMain.setDouble(FLD_RATE, arap.getRate());
                pstArApMain.setDouble(FLD_AMOUNT, arap.getAmount());
                pstArApMain.setString(FLD_NOTA_NO, arap.getNotaNo());
                pstArApMain.setDate(FLD_NOTA_DATE, arap.getNotaDate());
                pstArApMain.setString(FLD_DESCRIPTION, arap.getDescription());
                pstArApMain.setInt(FLD_ARAP_MAIN_STATUS, arap.getArApMainStatus());
                pstArApMain.setInt(FLD_ARAP_TYPE, arap.getArApType());
                pstArApMain.setInt(FLD_ARAP_DOC_STATUS, arap.getArApDocStatus());

                pstArApMain.update();
                return arap.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstArApMain(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long Oid) throws DBException {
        try {
            PstArApMain pstArApMain = new PstArApMain(Oid);
            pstArApMain.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstArApMain(0), DBException.UNKNOWN);
        }
        return Oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_ARAP_MAIN + " ";
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
                    break;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                ArApMain arap = new ArApMain();
                resultToObject(rs, arap);
                lists.add(arap);
            }
        } catch (Exception error) {
            System.out.println(".:: " + new PstArApMain().getClass().getName() + ".list() : " + error.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    private static void resultToObject(ResultSet rs, ArApMain arap) {
        try {
            arap.setOID(rs.getLong(PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID]));
            arap.setVoucherNo(rs.getString(PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO]));
            arap.setVoucherDate(rs.getDate(PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_DATE]));
            arap.setNumberOfPayment(rs.getInt(PstArApMain.fieldNames[PstArApMain.FLD_NUMBER_OF_PAYMENT]));
            arap.setContactId(rs.getLong(PstArApMain.fieldNames[PstArApMain.FLD_CONTACT_ID]));
            arap.setIdPerkiraanLawan(rs.getLong(PstArApMain.fieldNames[PstArApMain.FLD_ID_PERKIRAAN_LAWAN]));
            arap.setIdCurrency(rs.getLong(PstArApMain.fieldNames[PstArApMain.FLD_ID_CURRENCY]));
            arap.setCounter(rs.getInt(PstArApMain.fieldNames[PstArApMain.FLD_COUNTER]));
            arap.setRate(rs.getDouble(PstArApMain.fieldNames[PstArApMain.FLD_RATE]));
            arap.setAmount(rs.getDouble(PstArApMain.fieldNames[PstArApMain.FLD_AMOUNT]));
            arap.setNotaNo(rs.getString(PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]));
            arap.setNotaDate(rs.getDate(PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE]));
            arap.setDescription(rs.getString(PstArApMain.fieldNames[PstArApMain.FLD_DESCRIPTION]));
            arap.setArApMainStatus(rs.getInt(PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_STATUS]));
            arap.setArApType(rs.getInt(PstArApMain.fieldNames[PstArApMain.FLD_ARAP_TYPE]));
            arap.setArApDocStatus(rs.getInt(PstArApMain.fieldNames[PstArApMain.FLD_ARAP_DOC_STATUS]));

        } catch (Exception e) {
            System.out.println("resultToObject() " + e.toString());
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID] + ") " +
                    " FROM " + TBL_ARAP_MAIN;
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
            System.out.println(e);
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }


    /**
     * untuk pembuatan nomor doc arap
     */
    public static ArApMain createOrderNomor(ArApMain arApMain) {
        String nmr = "";
        try {
            nmr = Formater.formatDate(arApMain.getVoucherDate(), "yyMM");
            String s = PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_DATE];
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    s = "DATE_FORMAT(" + PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_DATE] + ", '%Y-%m')";
                    break;
                case DBHandler.DBSVR_POSTGRESQL:
                    s = "TO_CHAR(" + PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_DATE] + ", 'YYYY-MM')";
                    break;
                case DBHandler.DBSVR_SYBASE:
                    break;
                case DBHandler.DBSVR_ORACLE:
                    break;
                case DBHandler.DBSVR_MSSQL:
                    break;
                default:
                    s = "DATE_FORMAT(" + PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_DATE] + ", '%Y-%m')";
            }
            String where = s + " = '" + Formater.formatDate(arApMain.getVoucherDate(), "yyyy-MM") + "'";
            int cnt = getCount(where) + 1;
            arApMain.setCounter(cnt);
            switch (String.valueOf(cnt).length()) {
                case 1:
                    nmr = nmr + "-00" + cnt;
                    break;
                case 2:
                    nmr = nmr + "-0" + cnt;
                    break;
                default:
                    nmr = nmr + "-" + cnt;
            }
            arApMain.setVoucherNo(nmr);
        } catch (Exception e) {
            System.out.println(e);
            arApMain.setVoucherNo("0000-000");
        }
        return arApMain;
    }

    /**
         * update oid lama dengan yang baru
         * @param originalId
         * @param newId
         * @return
         */
        public static long updateSynchOID(long originalId, long newId){
            DBResultSet dbrs = null;
            try {
                String sql = "UPDATE " + TBL_ARAP_MAIN+ " SET " +
                PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID] + " = " + newId +
                " WHERE " + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID] +
                " = " + originalId;
                DBHandler.execUpdate(sql);
                return newId;
            }catch(Exception e) {
                return 0;
            }finally {
                DBResultSet.close(dbrs);
            }
        }
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         ArApMain arApMain = PstArApMain.fetchExc(oid);
         object.put(PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID], arApMain.getOID());
         object.put(PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO], arApMain.getVoucherNo());
         object.put(PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_DATE], arApMain.getVoucherDate());
         object.put(PstArApMain.fieldNames[PstArApMain.FLD_CONTACT_ID], arApMain.getContactId());
         object.put(PstArApMain.fieldNames[PstArApMain.FLD_NUMBER_OF_PAYMENT], arApMain.getNumberOfPayment());
         object.put(PstArApMain.fieldNames[PstArApMain.FLD_ID_PERKIRAAN_LAWAN], arApMain.getIdPerkiraanLawan());
         object.put(PstArApMain.fieldNames[PstArApMain.FLD_ID_PERKIRAAN], arApMain.getIdPerkiraan());
         object.put(PstArApMain.fieldNames[PstArApMain.FLD_ID_CURRENCY], arApMain.getIdCurrency());
         object.put(PstArApMain.fieldNames[PstArApMain.FLD_COUNTER], arApMain.getCounter());
         object.put(PstArApMain.fieldNames[PstArApMain.FLD_RATE], arApMain.getRate());
         object.put(PstArApMain.fieldNames[PstArApMain.FLD_AMOUNT], arApMain.getAmount());
         object.put(PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO], arApMain.getNotaNo());
         object.put(PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE], arApMain.getNotaDate());
         object.put(PstArApMain.fieldNames[PstArApMain.FLD_DESCRIPTION], arApMain.getDescription());
         object.put(PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_STATUS], arApMain.getArApMainStatus());
         object.put(PstArApMain.fieldNames[PstArApMain.FLD_ARAP_TYPE], arApMain.getArApType());
         object.put(PstArApMain.fieldNames[PstArApMain.FLD_ARAP_DOC_STATUS], arApMain.getArApDocStatus());
      }catch(Exception exc){}
      return object;
   }

}
