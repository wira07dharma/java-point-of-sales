
/* Created on 	:  [date] [time] AM/PM
 *
 * @author  	: lkarunia
 * @version  	: 01
 */
/**
 * *****************************************************************
 * Class Description : [project description ... ] Imput Parameters : [input
 * parameter ...] Output : [output ...]
 ******************************************************************
 */
package com.dimata.common.entity.payment;


/* package java */
import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;

import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.payment.PstCashPayment;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;
import java.sql.*;
import java.util.*;

public class PstPaymentSystem extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_PersistentExcSynch {

    public static final String TBL_P2_PAYMENT_SYSTEM = "payment_system";

    public static final int FLD_PAYMENT_SYSTEM_ID = 0;
    public static final int FLD_PAYMENT_SYSTEM = 1;
    public static final int FLD_DESCRIPTION = 2;
    public static final int FLD_BANK_INFO_OUT = 3;
    public static final int FLD_CARD_INFO = 4;
    public static final int FLD_DUE_DATE_INFO = 5;
    public static final int FLD_DAYS = 6;
    public static final int FLD_CHECK_BG_INFO = 7;
    public static final int FLD_BANK_INFO_IN = 8;
    public static final int FLD_BANK_NAME = 9;
    public static final int FLD_BANK_ADDRESS = 10;
    public static final int FLD_SWIFT_CODE = 11;
    public static final int FLD_ACCOUNT_NAME = 12;
    public static final int FLD_ACCOUNT_NUMBER = 13;
    public static final int FLD_FIXED = 14;
    public static final int FLD_CLEARED_REF_ID = 15;
    public static final int FLD_PAYMENT_TYPE = 16;

    //add opie-eyek 20022013untuk setting cost bank and charge to customer percent
    public static final int FLD_CHARGE_TO_CUSTOMER_PERCENT = 17;
    public static final int FLD_BANK_COST_PERCENT = 18;
    public static final int FLD_COSTING_TO = 19;

    //added by dewok 20190309 for payment type return
    public static final int FLD_ENABLE_FOR_RETURN = 20;

    public static final String[] fieldNames = {
        "PAYMENT_SYSTEM_ID",//0
        "PAYMENT_SYSTEM",//1
        "DESCRIPTION",//2
        "BANK_INFO_OUT",//3
        "CARD_INFO",//4
        "DUE_DATE_INFO",//5
        "DAYS",//6
        "CHECK_BG_INFO",//7
        "BANK_INFO_IN",//8
        "BANK_NAME",//9
        "BANK_ADDRESS",//10
        "SWIFT_CODE",//11
        "ACCOUNT_NAME",//12
        "ACCOUNT_NUMBER",//13
        "FIXED",//14
        "CLEARED_REF_ID",//15
        "PAYMENT_TYPE", //16
        "CHARGE_TO_CUSTOMER_PERCENT", //17
        "BANK_COST_PERCENT",
        "COSTING_TO",
        "ENABLE_FOR_RETURN"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,//0
        TYPE_STRING,//1
        TYPE_STRING,//2
        TYPE_BOOL,//3
        TYPE_BOOL,//4
        TYPE_BOOL,//5
        TYPE_INT,//6
        TYPE_BOOL,//7
        TYPE_BOOL,//8
        TYPE_STRING,//9
        TYPE_STRING,//10
        TYPE_STRING,//11
        TYPE_STRING,//12
        TYPE_STRING,//13
        TYPE_BOOL,//14
        TYPE_LONG,//15
        TYPE_INT, //16
        TYPE_FLOAT, //17
        TYPE_FLOAT, //18
        TYPE_LONG,//19
        TYPE_INT//20
    };

    //TYPE PAYMENT
    public static final int TYPE_CASH = 0;
    public static final int TYPE_CHEQUE = 1;
    public static final int TYPE_LC = 2;
    public static final int TYPE_CREDIT_CARD = 3;
    public static final int TYPE_BANK = 4;
    public static final int TYPE_DEBIT_CARD = 5;
    public static final int TYPE_GIRO = 6;

    //using payment dinamis or not by mirahu 20120416
    public static final int NOT_USING_PAYMENT_DINAMIS = 0;
    public static final int USING_PAYMENT_DINAMIS = 1;

    public PstPaymentSystem() {
    }

    public PstPaymentSystem(int i) throws DBException {
        super(new PstPaymentSystem());
    }

    public PstPaymentSystem(String sOid) throws DBException {
        super(new PstPaymentSystem(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstPaymentSystem(long lOid) throws DBException {
        super(new PstPaymentSystem(0));
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
        return TBL_P2_PAYMENT_SYSTEM;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPaymentSystem().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        //updated by : eka
        //date : 1 feb 2004
        try {
            PaymentSystem paymentsystem = (PaymentSystem) ent;
            long oid = ent.getOID();

            PstPaymentSystem pstPaymentSystem = new PstPaymentSystem(oid);
            paymentsystem.setOID(oid);

            paymentsystem.setPaymentSystem(pstPaymentSystem.getString(FLD_PAYMENT_SYSTEM));
            paymentsystem.setDescription(pstPaymentSystem.getString(FLD_DESCRIPTION));
            paymentsystem.setAccountName(pstPaymentSystem.getString(FLD_ACCOUNT_NAME));
            paymentsystem.setAccountNumber(pstPaymentSystem.getString(FLD_ACCOUNT_NUMBER));
            paymentsystem.setBankAddress(pstPaymentSystem.getString(FLD_BANK_ADDRESS));
            if (pstPaymentSystem.getString(FLD_BANK_INFO_IN) != null) {
                paymentsystem.setBankInfoIn(pstPaymentSystem.getboolean(FLD_BANK_INFO_IN));
            } else {
                paymentsystem.setBankInfoIn(false);
            }

            if (pstPaymentSystem.getString(FLD_BANK_INFO_OUT) != null) {
                paymentsystem.setBankInfoOut(pstPaymentSystem.getboolean(FLD_BANK_INFO_OUT));
            } else {
                paymentsystem.setBankInfoOut(false);
            }
            paymentsystem.setBankName(pstPaymentSystem.getString(FLD_BANK_NAME));
            if (pstPaymentSystem.getString(FLD_CARD_INFO) != null) {
                paymentsystem.setCardInfo(pstPaymentSystem.getboolean(FLD_CARD_INFO));
            } else {
                paymentsystem.setCardInfo(false);
            }
            if (pstPaymentSystem.getString(FLD_CHECK_BG_INFO) != null) {
                paymentsystem.setCheckBGInfo(pstPaymentSystem.getboolean(FLD_CHECK_BG_INFO));
            } else {
                paymentsystem.setCheckBGInfo(false);
            }

            paymentsystem.setDays(pstPaymentSystem.getInt(FLD_DAYS));

            if (pstPaymentSystem.getString(FLD_DUE_DATE_INFO) != null) {
                paymentsystem.setDueDateInfo(pstPaymentSystem.getboolean(FLD_DUE_DATE_INFO));
            } else {
                paymentsystem.setDueDateInfo(false);
            }
            paymentsystem.setSwiftCode(pstPaymentSystem.getString(FLD_SWIFT_CODE));

            if (pstPaymentSystem.getString(FLD_FIXED) != null) {
                paymentsystem.setFixed(pstPaymentSystem.getboolean(FLD_FIXED));
            } else {
                paymentsystem.setFixed(false);
            }

            paymentsystem.setClearedRefId(pstPaymentSystem.getlong(FLD_CLEARED_REF_ID));

            //add opie-eyek 20022013untuk setting cost bank and charge to customer percent
            paymentsystem.setChargeToCustomerPercent(pstPaymentSystem.getdouble(FLD_CHARGE_TO_CUSTOMER_PERCENT));
            paymentsystem.setBankCostPercent(pstPaymentSystem.getdouble(FLD_BANK_COST_PERCENT));

            paymentsystem.setCostingTo(pstPaymentSystem.getlong(FLD_COSTING_TO));
            
            //added by dewok 20190309 for payment type return
            paymentsystem.setEnableForReturn(pstPaymentSystem.getInt(FLD_ENABLE_FOR_RETURN));
            
            return ent.getOID();

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPaymentSystem(0), DBException.UNKNOWN);
        }
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((PaymentSystem) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((PaymentSystem) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static PaymentSystem fetchExc(long oid) throws DBException {
        try {
            PaymentSystem paymentsystem = new PaymentSystem();

            PstPaymentSystem pstPaymentSystem = new PstPaymentSystem(oid);
            paymentsystem.setOID(oid);

            paymentsystem.setPaymentSystem(pstPaymentSystem.getString(FLD_PAYMENT_SYSTEM));
            paymentsystem.setDescription(pstPaymentSystem.getString(FLD_DESCRIPTION));
            paymentsystem.setAccountName(pstPaymentSystem.getString(FLD_ACCOUNT_NAME));
            paymentsystem.setAccountNumber(pstPaymentSystem.getString(FLD_ACCOUNT_NUMBER));
            paymentsystem.setBankAddress(pstPaymentSystem.getString(FLD_BANK_ADDRESS));
            if (pstPaymentSystem.getString(FLD_BANK_INFO_IN) != null) {
                paymentsystem.setBankInfoIn(pstPaymentSystem.getboolean(FLD_BANK_INFO_IN));
            } else {
                paymentsystem.setBankInfoIn(false);
            }

            if (pstPaymentSystem.getString(FLD_BANK_INFO_OUT) != null) {
                paymentsystem.setBankInfoOut(pstPaymentSystem.getboolean(FLD_BANK_INFO_OUT));
            } else {
                paymentsystem.setBankInfoOut(false);
            }
            paymentsystem.setBankName(pstPaymentSystem.getString(FLD_BANK_NAME));
            if (pstPaymentSystem.getString(FLD_CARD_INFO) != null) {
                paymentsystem.setCardInfo(pstPaymentSystem.getboolean(FLD_CARD_INFO));
            } else {
                paymentsystem.setCardInfo(false);
            }
            if (pstPaymentSystem.getString(FLD_CHECK_BG_INFO) != null) {
                paymentsystem.setCheckBGInfo(pstPaymentSystem.getboolean(FLD_CHECK_BG_INFO));
            } else {
                paymentsystem.setCheckBGInfo(false);
            }

            paymentsystem.setDays(pstPaymentSystem.getInt(FLD_DAYS));

            if (pstPaymentSystem.getString(FLD_DUE_DATE_INFO) != null) {
                paymentsystem.setDueDateInfo(pstPaymentSystem.getboolean(FLD_DUE_DATE_INFO));
            } else {
                paymentsystem.setDueDateInfo(false);
            }

            paymentsystem.setSwiftCode(pstPaymentSystem.getString(FLD_SWIFT_CODE));

            if (pstPaymentSystem.getString(FLD_FIXED) != null) {
                paymentsystem.setFixed(pstPaymentSystem.getboolean(FLD_FIXED));
            } else {
                paymentsystem.setFixed(false);
            }

            paymentsystem.setClearedRefId(pstPaymentSystem.getlong(FLD_CLEARED_REF_ID));
            //adding payment_type
            //by mirahu 12032012
            paymentsystem.setPaymentType(pstPaymentSystem.getInt(FLD_PAYMENT_TYPE));

            //add opie-eyek 20022013untuk setting cost bank and charge to customer percent
            paymentsystem.setChargeToCustomerPercent(pstPaymentSystem.getdouble(FLD_CHARGE_TO_CUSTOMER_PERCENT));
            paymentsystem.setBankCostPercent(pstPaymentSystem.getdouble(FLD_BANK_COST_PERCENT));

            paymentsystem.setCostingTo(pstPaymentSystem.getlong(FLD_COSTING_TO));
            
            //added by dewok 20190309 for payment type return
            paymentsystem.setEnableForReturn(pstPaymentSystem.getInt(FLD_ENABLE_FOR_RETURN));
            
            return paymentsystem;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DBException(new PstPaymentSystem(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(PaymentSystem paymentsystem) throws DBException {
        try {
            PstPaymentSystem pstPaymentSystem = new PstPaymentSystem(0);

            pstPaymentSystem.setString(FLD_PAYMENT_SYSTEM, paymentsystem.getPaymentSystem());
            pstPaymentSystem.setString(FLD_DESCRIPTION, paymentsystem.getDescription());
            pstPaymentSystem.setString(FLD_ACCOUNT_NAME, paymentsystem.getAccountName());
            pstPaymentSystem.setString(FLD_ACCOUNT_NUMBER, paymentsystem.getAccountNumber());
            pstPaymentSystem.setString(FLD_BANK_ADDRESS, paymentsystem.getBankAddress());
            pstPaymentSystem.setString(FLD_BANK_NAME, paymentsystem.getBankName());
            pstPaymentSystem.setString(FLD_SWIFT_CODE, paymentsystem.getSwiftCode());
            pstPaymentSystem.setboolean(FLD_BANK_INFO_IN, paymentsystem.isBankInfoIn());
            pstPaymentSystem.setboolean(FLD_BANK_INFO_OUT, paymentsystem.isBankInfoOut());
            pstPaymentSystem.setboolean(FLD_CARD_INFO, paymentsystem.isCardInfo());
            pstPaymentSystem.setboolean(FLD_CHECK_BG_INFO, paymentsystem.isCheckBGInfo());
            pstPaymentSystem.setboolean(FLD_DUE_DATE_INFO, paymentsystem.isDueDateInfo());
            pstPaymentSystem.setInt(FLD_DAYS, paymentsystem.getDays());
            pstPaymentSystem.setboolean(FLD_FIXED, paymentsystem.isFixed());
            pstPaymentSystem.setLong(FLD_CLEARED_REF_ID, paymentsystem.getClearedRefId());
            //adding payment_type
            //by mirahu 12032012
            pstPaymentSystem.setInt(FLD_PAYMENT_TYPE, paymentsystem.getPaymentType());
            //add opie-eyek 20022013untuk setting cost bank and charge to customer percent
            pstPaymentSystem.setDouble(FLD_CHARGE_TO_CUSTOMER_PERCENT, paymentsystem.getChargeToCustomerPercent());
            pstPaymentSystem.setDouble(FLD_BANK_COST_PERCENT, paymentsystem.getBankCostPercent());
            pstPaymentSystem.setLong(FLD_COSTING_TO, paymentsystem.getCostingTo());
            
            //added by dewok 20190309 for payment type return
            pstPaymentSystem.setInt(FLD_ENABLE_FOR_RETURN, paymentsystem.getEnableForReturn());

            pstPaymentSystem.insert();

            long oidDataSync = PstDataSyncSql.insertExc(pstPaymentSystem.getInsertSQL());

            PstDataSyncStatus.insertExc(oidDataSync);

            paymentsystem.setOID(pstPaymentSystem.getlong(FLD_PAYMENT_SYSTEM_ID));
            //kebutuhan untuk service transfer katalog
            PstDataCustom.insertDataForSyncAllLocation(pstPaymentSystem.getInsertSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPaymentSystem(0), DBException.UNKNOWN);
        }
        return paymentsystem.getOID();
    }

    public static long updateExc(PaymentSystem paymentsystem) throws DBException {
        try {
            if (paymentsystem.getOID() != 0) {
                PstPaymentSystem pstPaymentSystem = new PstPaymentSystem(paymentsystem.getOID());

                pstPaymentSystem.setString(FLD_PAYMENT_SYSTEM, paymentsystem.getPaymentSystem());
                pstPaymentSystem.setString(FLD_DESCRIPTION, paymentsystem.getDescription());
                pstPaymentSystem.setString(FLD_ACCOUNT_NAME, paymentsystem.getAccountName());
                pstPaymentSystem.setString(FLD_ACCOUNT_NUMBER, paymentsystem.getAccountNumber());
                pstPaymentSystem.setString(FLD_BANK_ADDRESS, paymentsystem.getBankAddress());
                pstPaymentSystem.setString(FLD_BANK_NAME, paymentsystem.getBankName());
                pstPaymentSystem.setString(FLD_SWIFT_CODE, paymentsystem.getSwiftCode());
                pstPaymentSystem.setboolean(FLD_BANK_INFO_IN, paymentsystem.isBankInfoIn());
                pstPaymentSystem.setboolean(FLD_BANK_INFO_OUT, paymentsystem.isBankInfoOut());
                pstPaymentSystem.setboolean(FLD_CARD_INFO, paymentsystem.isCardInfo());
                pstPaymentSystem.setboolean(FLD_CHECK_BG_INFO, paymentsystem.isCheckBGInfo());
                pstPaymentSystem.setboolean(FLD_DUE_DATE_INFO, paymentsystem.isDueDateInfo());
                pstPaymentSystem.setInt(FLD_DAYS, paymentsystem.getDays());
                pstPaymentSystem.setboolean(FLD_FIXED, paymentsystem.isFixed());
                pstPaymentSystem.setLong(FLD_CLEARED_REF_ID, paymentsystem.getClearedRefId());
                //adding payment_type
                //by mirahu 12032012
                pstPaymentSystem.setInt(FLD_PAYMENT_TYPE, paymentsystem.getPaymentType());

                //add opie-eyek 20022013untuk setting cost bank and charge to customer percent
                pstPaymentSystem.setDouble(FLD_CHARGE_TO_CUSTOMER_PERCENT, paymentsystem.getChargeToCustomerPercent());
                pstPaymentSystem.setDouble(FLD_BANK_COST_PERCENT, paymentsystem.getBankCostPercent());
                pstPaymentSystem.setLong(FLD_COSTING_TO, paymentsystem.getCostingTo());
                
                //added by dewok 20190309 for payment type return
                pstPaymentSystem.setInt(FLD_ENABLE_FOR_RETURN, paymentsystem.getEnableForReturn());

                pstPaymentSystem.update();

                long oidDataSync = PstDataSyncSql.insertExc(pstPaymentSystem.getUpdateSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
                //kebutuhan untuk service transfer katalog
                PstDataCustom.insertDataForSyncAllLocation(pstPaymentSystem.getUpdateSQL());
                return paymentsystem.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPaymentSystem(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstPaymentSystem pstPaymentSystem = new PstPaymentSystem(oid);
            pstPaymentSystem.delete();

            long oidDataSync = PstDataSyncSql.insertExc(pstPaymentSystem.getDeleteSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
            //kebutuhan untuk service transfer katalog
            PstDataCustom.insertDataForSyncAllLocation(pstPaymentSystem.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPaymentSystem(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_P2_PAYMENT_SYSTEM;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }

                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }

                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                PaymentSystem paymentsystem = new PaymentSystem();
                resultToObject(rs, paymentsystem);
                lists.add(paymentsystem);
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

    private static void resultToObject(ResultSet rs, PaymentSystem paymentsystem) {
        try {
            paymentsystem.setOID(rs.getLong(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID]));
            paymentsystem.setPaymentSystem(rs.getString(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM]));
            paymentsystem.setDescription(rs.getString(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_DESCRIPTION]));
            paymentsystem.setAccountName(rs.getString(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_ACCOUNT_NAME]));
            paymentsystem.setAccountNumber(rs.getString(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_ACCOUNT_NUMBER]));
            paymentsystem.setBankAddress(rs.getString(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_BANK_ADDRESS]));
            paymentsystem.setBankInfoIn(rs.getBoolean(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_BANK_INFO_IN]));
            paymentsystem.setBankInfoOut(rs.getBoolean(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_BANK_INFO_OUT]));
            paymentsystem.setBankName(rs.getString(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_BANK_NAME]));
            paymentsystem.setCardInfo(rs.getBoolean(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CARD_INFO]));
            paymentsystem.setCheckBGInfo(rs.getBoolean(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CHECK_BG_INFO]));
            paymentsystem.setDays(rs.getInt(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_DAYS]));
            paymentsystem.setDueDateInfo(rs.getBoolean(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_DUE_DATE_INFO]));
            paymentsystem.setSwiftCode(rs.getString(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_SWIFT_CODE]));
            paymentsystem.setFixed(rs.getBoolean(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_FIXED]));
            paymentsystem.setClearedRefId(rs.getLong(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CLEARED_REF_ID]));
            //adding payment_type
            //by mirahu 12032012
            paymentsystem.setPaymentType(rs.getInt(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_TYPE]));

            //add opie-eyek 20022013untuk setting cost bank and charge to customer percent
            paymentsystem.setChargeToCustomerPercent(rs.getDouble(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CHARGE_TO_CUSTOMER_PERCENT]));
            paymentsystem.setBankCostPercent(rs.getDouble(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_BANK_COST_PERCENT]));
            paymentsystem.setCostingTo(rs.getLong(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_COSTING_TO]));
            
            //added by dewok 20190309 for payment type return
            paymentsystem.setEnableForReturn(rs.getInt(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_ENABLE_FOR_RETURN]));

        } catch (Exception e) {
        }
    }

    public static Vector listDistinctCashPaymentJoinPaymentSystem(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;

        try {
            String sql = ""
                    + "SELECT"
                    + " DISTINCT(ps." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID] + "),"
                    + " ps." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM] + ","
                    + " ps." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_TYPE] + ""
                    + " FROM"
                    + " " + PstPaymentSystem.TBL_P2_PAYMENT_SYSTEM + " ps"
                    + " INNER JOIN " + PstCashPayment.TBL_PAYMENT + " cp "
                    + " ON cp." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE] + " = ps." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID] + ""
                    + " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " cbm"
                    + " ON cp." + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID] + "=cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ""
                    + "";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }

                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }

                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                PaymentSystem paymentsystem = new PaymentSystem();
                paymentsystem.setOID(rs.getLong(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID]));
                paymentsystem.setPaymentSystem(rs.getString(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM]));
                paymentsystem.setPaymentType(rs.getInt(PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_TYPE]));

                lists.add(paymentsystem);
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

    public static boolean checkOID(long paymentSystemId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_P2_PAYMENT_SYSTEM + " WHERE "
                    + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID] + " = " + paymentSystemId;

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
            String sql = "SELECT COUNT(" + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID] + ") FROM " + TBL_P2_PAYMENT_SYSTEM;
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

    /* This method used to find current data */
    public static int findLimitStart(long oid, int recordToGet, String whereClause, String order) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, order);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    PaymentSystem paymentsystem = (PaymentSystem) list.get(ls);
                    if (oid == paymentsystem.getOID()) {
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

    /* This method used to find command where current data */
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

    //updated by : eka
    //date : 1 feb 2004
    /**
     * * function for data synchronization **
     */
    public long insertExcSynch(Entity ent) throws Exception {
        return insertExcSynch((PaymentSystem) ent);
    }

    public static long insertExcSynch(PaymentSystem theObj) throws DBException {
        long newOID = 0;
        long originalOID = theObj.getOID();
        try {
            newOID = insertExc(theObj);
            if (newOID != 0) {  // sukses insert ?
                updateSynchOID(newOID, originalOID);
                return originalOID;
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPaymentSystem(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long updateSynchOID(long newOID, long originalOID) throws DBException {
        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE " + TBL_P2_PAYMENT_SYSTEM + " SET "
                    + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID] + " = " + originalOID
                    + " WHERE " + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID]
                    + " = " + newOID;

            int Result = DBHandler.execUpdate(sql);

            return originalOID;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    /**
     * This methode use to set payment system
     */
    public static PaymentSystem setInfoPayment(PaymentSystem paymentSystem) {
        int status = paymentSystem.getInfoStatus();
        switch (status) {
            case 1:
                paymentSystem.setBankInfoOut(true);
                paymentSystem.setCardInfo(false);
                paymentSystem.setCheckBGInfo(false);
                break;
            case 2:
                paymentSystem.setCardInfo(true);
                paymentSystem.setCheckBGInfo(false);
                paymentSystem.setBankInfoOut(false);
                break;
            case 3:
                paymentSystem.setCheckBGInfo(true);
                paymentSystem.setCardInfo(false);
                paymentSystem.setBankInfoOut(false);
                break;
            default:
                paymentSystem.setCheckBGInfo(false);
                paymentSystem.setCardInfo(false);
                paymentSystem.setBankInfoOut(false);
                break;
        }
        return paymentSystem;
    }

    public static PaymentSystem getInfoPayment(PaymentSystem paymentSystem) {
        if (paymentSystem.isBankInfoOut() == true) {
            paymentSystem.setInfoStatus(1);
        } else if (paymentSystem.isCardInfo() == true) {
            paymentSystem.setInfoStatus(2);
        } else if (paymentSystem.isCheckBGInfo() == true) {
            paymentSystem.setInfoStatus(3);
        }
        return paymentSystem;
    }

    /**
     * Get payment dinamis by mirahu 20120416
     *
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @param order
     * @return
     */
    public static Vector listDinamis(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT PAY." + fieldNames[FLD_CARD_INFO]
                    + ", PAY." + fieldNames[FLD_CHECK_BG_INFO]
                    + ", PAY." + fieldNames[FLD_PAYMENT_TYPE]
                    + " FROM " + TBL_P2_PAYMENT_SYSTEM + " PAY ";

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                PaymentSystem paymentsystem = new PaymentSystem();
                resultToObject(rs, paymentsystem);
                lists.add(paymentsystem);
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

    /**
     * Get PaymentType Dinamis by mirahu 20120416
     *
     * @param oidPaymentSystem
     * @return
     */
    public static String getTypePayment(long oidPaymentSystem) {
        String typePayment = "";
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT PAY." + fieldNames[FLD_PAYMENT_SYSTEM]
                    + " FROM " + TBL_P2_PAYMENT_SYSTEM + " PAY "
                    + " WHERE " + " PAY." + fieldNames[FLD_PAYMENT_SYSTEM_ID]
                    + " = " + oidPaymentSystem;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                typePayment = rs.getString(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return typePayment;

    }

}
