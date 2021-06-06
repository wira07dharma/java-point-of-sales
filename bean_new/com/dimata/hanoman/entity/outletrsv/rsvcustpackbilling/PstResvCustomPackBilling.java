/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.entity.outletrsv.rsvcustpackbilling;

import com.dimata.hanoman.entity.outletrsv.rsvcustpackschedule.PstResvCustomPackSchedule;
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

/**
 *
 * @author Dewa
 */
public class PstResvCustomPackBilling extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_RESV_CUSTOME_PACK_BILLING = "rsv_custome_package_billing";
    public static final int FLD_CUSTOME_PACK_BILLING_ID = 0;
    public static final int FLD_BILLING_TYPE_ITEM_ID = 1;
    public static final int FLD_BILLING_TYPE_ID = 2;
    public static final int FLD_TRAVEL_PACK_TYPE_ID = 3;
    public static final int FLD_QUANTITY = 4;
    public static final int FLD_QUANTITY_TYPE = 5;
    public static final int FLD_PRICE_RP = 6;
    public static final int FLD_PRICE_USD = 7;
    public static final int FLD_TOTAL_PRICE_RP = 8;
    public static final int FLD_TOTAL_PRICE_USD = 9;
    public static final int FLD_DISCOUNT_TYPE = 10;
    public static final int FLD_DISCOUNT_PERCENTAGE = 11;
    public static final int FLD_DISCOUNT_AMOUNT_RP = 12;
    public static final int FLD_DISCOUNT_AMOUNT_USD = 13;
    public static final int FLD_SERVICE_RP = 14;
    public static final int FLD_SERVICE_USD = 15;
    public static final int FLD_TAX_RP = 16;
    public static final int FLD_TAX_USD = 17;
    public static final int FLD_CONTRACT_ID = 18;
    public static final int FLD_ROOM_CLASS_ID = 19;
    public static final int FLD_USE_DEFAULT = 20;
    public static final int FLD_TYPE = 21;
    public static final int FLD_DATE_TAKEN = 22;
    public static final int FLD_TYPE_SALES = 23;
    public static final int FLD_TYPE_COMPLIMENT = 24;
    public static final int FLD_RESERVATION_ID = 25;
    public static final int FLD_RATE = 26;
    public static final int FLD_CONSUME = 27;
    public static final int FLD_DURATION = 28;
    public static final int FLD_PAX_MALE = 29;
    public static final int FLD_PAX_FEMALE = 30;
    public static final int FLD_NOTE = 31;
    public static final int FLD_ROOM_ID = 32;
    public static final int FLD_TABLE_ID = 33;
    public static final int FLD_CUSTOMER_ID_TO_CHARGE = 34;
    public static final int FLD_CUSTOMER_ID_CONSUME = 35;
    public static final int FLD_FIX_ROOM = 36;
    public static final int FLD_FIX_PIC = 37;
    public static final int FLD_CURRENCY_TYPE = 38;
    public static final int FLD_SERVICE_INCLUDE = 39;
    public static final int FLD_TAX_INCLUDE = 40;

    public static String[] fieldNames = {
        "CUSTOME_PACK_BILLING_ID",
        "BILLING_TYPE_ITEM_ID",
        "BILLING_TYPE_ID",
        "TRAVEL_PACK_TYPE_ID",
        "QUANTITY",
        "QUANTITY_TYPE",
        "PRICE_RP",
        "PRICE_USD",
        "TOTAL_PRICE_RP",
        "TOTAL_PRICE_USD",
        "DISCOUNT_TYPE",
        "DISCOUNT_PERCENTAGE",
        "DISCOUNT_AMOUNT_RP",
        "DISCOUNT_AMOUNT_USD",
        "SERVICE_RP",
        "SERVICE_USD",
        "TAX_RP",
        "TAX_USD",
        "CONTRACT_ID",
        "ROOM_CLASS_ID",
        "USE_DEFAULT",
        "TYPE",
        "DATE_TAKEN",
        "TYPE_SALES",
        "TYPE_COMPLIMENT",
        "RESERVATION_ID",
        "RATE",
        "CONSUME",
        "DURATION",
        "PAX_MALE",
        "PAX_FEMALE",
        "NOTE",
        "ROOM_ID",
        "TABLE_ID",
        "CUSTOMER_ID_TO_CHARGE",
        "CUSTOMER_ID_CONSUME",
        "FIX_ROOM",
        "FIX_PIC",
        "CURRENCY_TYPE",
        "SERVICE_INCLUDE",
        "TAX_INCLUDE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_LONG + TYPE_FK,
        TYPE_LONG + TYPE_FK,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT
    };

    public PstResvCustomPackBilling() {
    }

    public PstResvCustomPackBilling(int i) throws DBException {
        super(new PstResvCustomPackBilling());
    }

    public PstResvCustomPackBilling(String sOid) throws DBException {
        super(new PstResvCustomPackBilling(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstResvCustomPackBilling(long lOid) throws DBException {
        super(new PstResvCustomPackBilling(0));
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
        return TBL_RESV_CUSTOME_PACK_BILLING;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstResvCustomPackBilling().getClass().getName();
    }

    public static ResvCustomPackBilling fetchExc(long oid) throws DBException {
        try {
            ResvCustomPackBilling entResvCustomePackBilling = new ResvCustomPackBilling();
            PstResvCustomPackBilling pstResvCustomePackBilling = new PstResvCustomPackBilling(oid);
            entResvCustomePackBilling.setOID(oid);
            entResvCustomePackBilling.setBillingTypeItemId(pstResvCustomePackBilling.getlong(FLD_BILLING_TYPE_ITEM_ID));
            entResvCustomePackBilling.setBillingTypeId(pstResvCustomePackBilling.getlong(FLD_BILLING_TYPE_ID));
            entResvCustomePackBilling.setTravelPackTypeId(pstResvCustomePackBilling.getlong(FLD_TRAVEL_PACK_TYPE_ID));
            entResvCustomePackBilling.setQuantity(pstResvCustomePackBilling.getdouble(FLD_QUANTITY));
            entResvCustomePackBilling.setQuantityType(pstResvCustomePackBilling.getInt(FLD_QUANTITY_TYPE));
            entResvCustomePackBilling.setPriceRp(pstResvCustomePackBilling.getdouble(FLD_PRICE_RP));
            entResvCustomePackBilling.setPriceUsd(pstResvCustomePackBilling.getdouble(FLD_PRICE_USD));
            entResvCustomePackBilling.setTotalPriceRp(pstResvCustomePackBilling.getdouble(FLD_TOTAL_PRICE_RP));
            entResvCustomePackBilling.setTotalPriceUsd(pstResvCustomePackBilling.getdouble(FLD_TOTAL_PRICE_USD));
            entResvCustomePackBilling.setDiscountType(pstResvCustomePackBilling.getInt(FLD_DISCOUNT_TYPE));
            entResvCustomePackBilling.setDiscountPercentage(pstResvCustomePackBilling.getdouble(FLD_DISCOUNT_PERCENTAGE));
            entResvCustomePackBilling.setDiscountAmountRp(pstResvCustomePackBilling.getdouble(FLD_DISCOUNT_AMOUNT_RP));
            entResvCustomePackBilling.setDiscountAmountUsd(pstResvCustomePackBilling.getdouble(FLD_DISCOUNT_AMOUNT_USD));
            entResvCustomePackBilling.setServiceRp(pstResvCustomePackBilling.getdouble(FLD_SERVICE_RP));
            entResvCustomePackBilling.setServiceUsd(pstResvCustomePackBilling.getdouble(FLD_SERVICE_USD));
            entResvCustomePackBilling.setTaxRp(pstResvCustomePackBilling.getdouble(FLD_TAX_RP));
            entResvCustomePackBilling.setTaxUsd(pstResvCustomePackBilling.getdouble(FLD_TAX_USD));
            entResvCustomePackBilling.setContractId(pstResvCustomePackBilling.getlong(FLD_CONTRACT_ID));
            entResvCustomePackBilling.setRoomClassId(pstResvCustomePackBilling.getlong(FLD_ROOM_CLASS_ID));
            entResvCustomePackBilling.setUseDefault(pstResvCustomePackBilling.getInt(FLD_USE_DEFAULT));
            entResvCustomePackBilling.setType(pstResvCustomePackBilling.getInt(FLD_TYPE));
            entResvCustomePackBilling.setDateTaken(pstResvCustomePackBilling.getDate(FLD_DATE_TAKEN));
            entResvCustomePackBilling.setTypeSales(pstResvCustomePackBilling.getlong(FLD_TYPE_SALES));
            entResvCustomePackBilling.setTypeCompliment(pstResvCustomePackBilling.getlong(FLD_TYPE_COMPLIMENT));
            entResvCustomePackBilling.setReservationId(pstResvCustomePackBilling.getlong(FLD_RESERVATION_ID));
            entResvCustomePackBilling.setRate(pstResvCustomePackBilling.getdouble(FLD_RATE));
            entResvCustomePackBilling.setConsume(pstResvCustomePackBilling.getInt(FLD_CONSUME));
            entResvCustomePackBilling.setDuration(pstResvCustomePackBilling.getInt(FLD_DURATION));
            entResvCustomePackBilling.setPaxMale(pstResvCustomePackBilling.getInt(FLD_PAX_MALE));
            entResvCustomePackBilling.setPaxFemale(pstResvCustomePackBilling.getInt(FLD_PAX_FEMALE));
            entResvCustomePackBilling.setNote(pstResvCustomePackBilling.getString(FLD_NOTE));
            entResvCustomePackBilling.setRoomId(pstResvCustomePackBilling.getlong(FLD_ROOM_ID));
            entResvCustomePackBilling.setTableId(pstResvCustomePackBilling.getlong(FLD_TABLE_ID));
            entResvCustomePackBilling.setCustomerIdToCharge(pstResvCustomePackBilling.getlong(FLD_CUSTOMER_ID_TO_CHARGE));
            entResvCustomePackBilling.setCustomerIdConsume(pstResvCustomePackBilling.getlong(FLD_CUSTOMER_ID_CONSUME));
            entResvCustomePackBilling.setFixRoom(pstResvCustomePackBilling.getInt(FLD_FIX_ROOM));
            entResvCustomePackBilling.setFixPic(pstResvCustomePackBilling.getInt(FLD_FIX_PIC));
            entResvCustomePackBilling.setCurrencyType(pstResvCustomePackBilling.getlong(FLD_CURRENCY_TYPE));
            entResvCustomePackBilling.setServiceInclude(pstResvCustomePackBilling.getInt(FLD_SERVICE_INCLUDE));
            entResvCustomePackBilling.setTaxInclude(pstResvCustomePackBilling.getInt(FLD_TAX_INCLUDE));
            return entResvCustomePackBilling;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstResvCustomPackBilling(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        ResvCustomPackBilling entResvCustomePackBilling = fetchExc(entity.getOID());
        entity = (Entity) entResvCustomePackBilling;
        return entResvCustomePackBilling.getOID();
    }

    public static synchronized long updateExc(ResvCustomPackBilling entResvCustomePackBilling) throws DBException {
        try {
            if (entResvCustomePackBilling.getOID() != 0) {
                PstResvCustomPackBilling pstResvCustomePackBilling = new PstResvCustomPackBilling(entResvCustomePackBilling.getOID());
                pstResvCustomePackBilling.setLong(FLD_BILLING_TYPE_ITEM_ID, entResvCustomePackBilling.getBillingTypeItemId());
                pstResvCustomePackBilling.setLong(FLD_BILLING_TYPE_ID, entResvCustomePackBilling.getBillingTypeId());
                pstResvCustomePackBilling.setLong(FLD_TRAVEL_PACK_TYPE_ID, entResvCustomePackBilling.getTravelPackTypeId());
                pstResvCustomePackBilling.setDouble(FLD_QUANTITY, entResvCustomePackBilling.getQuantity());
                pstResvCustomePackBilling.setInt(FLD_QUANTITY_TYPE, entResvCustomePackBilling.getQuantityType());
                pstResvCustomePackBilling.setDouble(FLD_PRICE_RP, entResvCustomePackBilling.getPriceRp());
                pstResvCustomePackBilling.setDouble(FLD_PRICE_USD, entResvCustomePackBilling.getPriceUsd());
                pstResvCustomePackBilling.setDouble(FLD_TOTAL_PRICE_RP, entResvCustomePackBilling.getTotalPriceRp());
                pstResvCustomePackBilling.setDouble(FLD_TOTAL_PRICE_USD, entResvCustomePackBilling.getTotalPriceUsd());
                pstResvCustomePackBilling.setInt(FLD_DISCOUNT_TYPE, entResvCustomePackBilling.getDiscountType());
                pstResvCustomePackBilling.setDouble(FLD_DISCOUNT_PERCENTAGE, entResvCustomePackBilling.getDiscountPercentage());
                pstResvCustomePackBilling.setDouble(FLD_DISCOUNT_AMOUNT_RP, entResvCustomePackBilling.getDiscountAmountRp());
                pstResvCustomePackBilling.setDouble(FLD_DISCOUNT_AMOUNT_USD, entResvCustomePackBilling.getDiscountAmountUsd());
                pstResvCustomePackBilling.setDouble(FLD_SERVICE_RP, entResvCustomePackBilling.getServiceRp());
                pstResvCustomePackBilling.setDouble(FLD_SERVICE_USD, entResvCustomePackBilling.getServiceUsd());
                pstResvCustomePackBilling.setDouble(FLD_TAX_RP, entResvCustomePackBilling.getTaxRp());
                pstResvCustomePackBilling.setDouble(FLD_TAX_USD, entResvCustomePackBilling.getTaxUsd());
                pstResvCustomePackBilling.setLong(FLD_CONTRACT_ID, entResvCustomePackBilling.getContractId());
                pstResvCustomePackBilling.setLong(FLD_ROOM_CLASS_ID, entResvCustomePackBilling.getRoomClassId());
                pstResvCustomePackBilling.setInt(FLD_USE_DEFAULT, entResvCustomePackBilling.getUseDefault());
                pstResvCustomePackBilling.setInt(FLD_TYPE, entResvCustomePackBilling.getType());
                pstResvCustomePackBilling.setDate(FLD_DATE_TAKEN, entResvCustomePackBilling.getDateTaken());
                pstResvCustomePackBilling.setLong(FLD_TYPE_SALES, entResvCustomePackBilling.getTypeSales());
                pstResvCustomePackBilling.setLong(FLD_TYPE_COMPLIMENT, entResvCustomePackBilling.getTypeCompliment());
                pstResvCustomePackBilling.setLong(FLD_RESERVATION_ID, entResvCustomePackBilling.getReservationId());
                pstResvCustomePackBilling.setDouble(FLD_RATE, entResvCustomePackBilling.getRate());
                pstResvCustomePackBilling.setInt(FLD_CONSUME, entResvCustomePackBilling.getConsume());
                pstResvCustomePackBilling.setInt(FLD_DURATION, entResvCustomePackBilling.getDuration());
                pstResvCustomePackBilling.setInt(FLD_PAX_MALE, entResvCustomePackBilling.getPaxMale());
                pstResvCustomePackBilling.setInt(FLD_PAX_FEMALE, entResvCustomePackBilling.getPaxFemale());
                pstResvCustomePackBilling.setString(FLD_NOTE, entResvCustomePackBilling.getNote());
                pstResvCustomePackBilling.setLong(FLD_ROOM_ID, entResvCustomePackBilling.getRoomId());
                pstResvCustomePackBilling.setLong(FLD_TABLE_ID, entResvCustomePackBilling.getTableId());
                pstResvCustomePackBilling.setLong(FLD_CUSTOMER_ID_TO_CHARGE, entResvCustomePackBilling.getCustomerIdToCharge());
                pstResvCustomePackBilling.setLong(FLD_CUSTOMER_ID_CONSUME, entResvCustomePackBilling.getCustomerIdConsume());
                pstResvCustomePackBilling.setInt(FLD_FIX_ROOM, entResvCustomePackBilling.getFixRoom());
                pstResvCustomePackBilling.setInt(FLD_FIX_PIC, entResvCustomePackBilling.getFixPic());
                pstResvCustomePackBilling.setLong(FLD_CURRENCY_TYPE, entResvCustomePackBilling.getCurrencyType());
                pstResvCustomePackBilling.setInt(FLD_SERVICE_INCLUDE, entResvCustomePackBilling.getServiceInclude());
                pstResvCustomePackBilling.setInt(FLD_TAX_INCLUDE, entResvCustomePackBilling.getTaxInclude());
                pstResvCustomePackBilling.update();
                return entResvCustomePackBilling.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstResvCustomPackBilling(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((ResvCustomPackBilling) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstResvCustomPackBilling pstResvCustomePackBilling = new PstResvCustomPackBilling(oid);
            pstResvCustomePackBilling.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstResvCustomPackBilling(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(ResvCustomPackBilling entResvCustomePackBilling) throws DBException {
        try {
            PstResvCustomPackBilling pstResvCustomePackBilling = new PstResvCustomPackBilling(0);
            pstResvCustomePackBilling.setLong(FLD_BILLING_TYPE_ITEM_ID, entResvCustomePackBilling.getBillingTypeItemId());
            pstResvCustomePackBilling.setLong(FLD_BILLING_TYPE_ID, entResvCustomePackBilling.getBillingTypeId());
            pstResvCustomePackBilling.setLong(FLD_TRAVEL_PACK_TYPE_ID, entResvCustomePackBilling.getTravelPackTypeId());
            pstResvCustomePackBilling.setDouble(FLD_QUANTITY, entResvCustomePackBilling.getQuantity());
            pstResvCustomePackBilling.setInt(FLD_QUANTITY_TYPE, entResvCustomePackBilling.getQuantityType());
            pstResvCustomePackBilling.setDouble(FLD_PRICE_RP, entResvCustomePackBilling.getPriceRp());
            pstResvCustomePackBilling.setDouble(FLD_PRICE_USD, entResvCustomePackBilling.getPriceUsd());
            pstResvCustomePackBilling.setDouble(FLD_TOTAL_PRICE_RP, entResvCustomePackBilling.getTotalPriceRp());
            pstResvCustomePackBilling.setDouble(FLD_TOTAL_PRICE_USD, entResvCustomePackBilling.getTotalPriceUsd());
            pstResvCustomePackBilling.setInt(FLD_DISCOUNT_TYPE, entResvCustomePackBilling.getDiscountType());
            pstResvCustomePackBilling.setDouble(FLD_DISCOUNT_PERCENTAGE, entResvCustomePackBilling.getDiscountPercentage());
            pstResvCustomePackBilling.setDouble(FLD_DISCOUNT_AMOUNT_RP, entResvCustomePackBilling.getDiscountAmountRp());
            pstResvCustomePackBilling.setDouble(FLD_DISCOUNT_AMOUNT_USD, entResvCustomePackBilling.getDiscountAmountUsd());
            pstResvCustomePackBilling.setDouble(FLD_SERVICE_RP, entResvCustomePackBilling.getServiceRp());
            pstResvCustomePackBilling.setDouble(FLD_SERVICE_USD, entResvCustomePackBilling.getServiceUsd());
            pstResvCustomePackBilling.setDouble(FLD_TAX_RP, entResvCustomePackBilling.getTaxRp());
            pstResvCustomePackBilling.setDouble(FLD_TAX_USD, entResvCustomePackBilling.getTaxUsd());
            pstResvCustomePackBilling.setLong(FLD_CONTRACT_ID, entResvCustomePackBilling.getContractId());
            pstResvCustomePackBilling.setLong(FLD_ROOM_CLASS_ID, entResvCustomePackBilling.getRoomClassId());
            pstResvCustomePackBilling.setInt(FLD_USE_DEFAULT, entResvCustomePackBilling.getUseDefault());
            pstResvCustomePackBilling.setInt(FLD_TYPE, entResvCustomePackBilling.getType());
            pstResvCustomePackBilling.setDate(FLD_DATE_TAKEN, entResvCustomePackBilling.getDateTaken());
            pstResvCustomePackBilling.setLong(FLD_TYPE_SALES, entResvCustomePackBilling.getTypeSales());
            pstResvCustomePackBilling.setLong(FLD_TYPE_COMPLIMENT, entResvCustomePackBilling.getTypeCompliment());
            pstResvCustomePackBilling.setLong(FLD_RESERVATION_ID, entResvCustomePackBilling.getReservationId());
            pstResvCustomePackBilling.setDouble(FLD_RATE, entResvCustomePackBilling.getRate());
            pstResvCustomePackBilling.setInt(FLD_CONSUME, entResvCustomePackBilling.getConsume());
            pstResvCustomePackBilling.setInt(FLD_DURATION, entResvCustomePackBilling.getDuration());
            pstResvCustomePackBilling.setInt(FLD_PAX_MALE, entResvCustomePackBilling.getPaxMale());
            pstResvCustomePackBilling.setInt(FLD_PAX_FEMALE, entResvCustomePackBilling.getPaxFemale());
            pstResvCustomePackBilling.setString(FLD_NOTE, entResvCustomePackBilling.getNote());
            pstResvCustomePackBilling.setLong(FLD_ROOM_ID, entResvCustomePackBilling.getRoomId());
            pstResvCustomePackBilling.setLong(FLD_TABLE_ID, entResvCustomePackBilling.getTableId());
            pstResvCustomePackBilling.setLong(FLD_CUSTOMER_ID_TO_CHARGE, entResvCustomePackBilling.getCustomerIdToCharge());
            pstResvCustomePackBilling.setLong(FLD_CUSTOMER_ID_CONSUME, entResvCustomePackBilling.getCustomerIdConsume());
            pstResvCustomePackBilling.setInt(FLD_FIX_ROOM, entResvCustomePackBilling.getFixRoom());
            pstResvCustomePackBilling.setInt(FLD_FIX_PIC, entResvCustomePackBilling.getFixPic());
            pstResvCustomePackBilling.setLong(FLD_CURRENCY_TYPE, entResvCustomePackBilling.getCurrencyType());
            pstResvCustomePackBilling.setInt(FLD_SERVICE_INCLUDE, entResvCustomePackBilling.getServiceInclude());
            pstResvCustomePackBilling.setInt(FLD_TAX_INCLUDE, entResvCustomePackBilling.getTaxInclude());
            pstResvCustomePackBilling.insert();
            entResvCustomePackBilling.setOID(pstResvCustomePackBilling.getlong(FLD_CUSTOME_PACK_BILLING_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstResvCustomPackBilling(0), DBException.UNKNOWN);
        }
        return entResvCustomePackBilling.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((ResvCustomPackBilling) entity);
    }

    public static void resultToObject(ResultSet rs, ResvCustomPackBilling entResvCustomePackBilling) {
        try {
            entResvCustomePackBilling.setOID(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_CUSTOME_PACK_BILLING_ID]));
            entResvCustomePackBilling.setBillingTypeItemId(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_BILLING_TYPE_ITEM_ID]));
            entResvCustomePackBilling.setBillingTypeId(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_BILLING_TYPE_ID]));
            entResvCustomePackBilling.setTravelPackTypeId(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TRAVEL_PACK_TYPE_ID]));
            entResvCustomePackBilling.setQuantity(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_QUANTITY]));
            entResvCustomePackBilling.setQuantityType(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_QUANTITY_TYPE]));
            entResvCustomePackBilling.setPriceRp(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_PRICE_RP]));
            entResvCustomePackBilling.setPriceUsd(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_PRICE_USD]));
            entResvCustomePackBilling.setTotalPriceRp(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TOTAL_PRICE_RP]));
            entResvCustomePackBilling.setTotalPriceUsd(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TOTAL_PRICE_USD]));
            entResvCustomePackBilling.setDiscountType(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_DISCOUNT_TYPE]));
            entResvCustomePackBilling.setDiscountPercentage(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_DISCOUNT_PERCENTAGE]));
            entResvCustomePackBilling.setDiscountAmountRp(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_DISCOUNT_AMOUNT_RP]));
            entResvCustomePackBilling.setDiscountAmountUsd(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_DISCOUNT_AMOUNT_USD]));
            entResvCustomePackBilling.setServiceRp(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_SERVICE_RP]));
            entResvCustomePackBilling.setServiceUsd(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_SERVICE_USD]));
            entResvCustomePackBilling.setTaxRp(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TAX_RP]));
            entResvCustomePackBilling.setTaxUsd(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TAX_USD]));
            entResvCustomePackBilling.setContractId(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_CONTRACT_ID]));
            entResvCustomePackBilling.setRoomClassId(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_ROOM_CLASS_ID]));
            entResvCustomePackBilling.setUseDefault(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_USE_DEFAULT]));
            entResvCustomePackBilling.setType(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TYPE]));
            entResvCustomePackBilling.setDateTaken(rs.getDate(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_DATE_TAKEN]));
            entResvCustomePackBilling.setTypeSales(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TYPE_SALES]));
            entResvCustomePackBilling.setTypeCompliment(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TYPE_COMPLIMENT]));
            entResvCustomePackBilling.setReservationId(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_RESERVATION_ID]));
            entResvCustomePackBilling.setRate(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_RATE]));
            entResvCustomePackBilling.setConsume(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_CONSUME]));
            entResvCustomePackBilling.setDuration(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_DURATION]));
            entResvCustomePackBilling.setPaxMale(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_PAX_MALE]));
            entResvCustomePackBilling.setPaxFemale(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_PAX_FEMALE]));
            entResvCustomePackBilling.setNote(rs.getString(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_NOTE]));
            entResvCustomePackBilling.setRoomId(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_ROOM_ID]));
            entResvCustomePackBilling.setTableId(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TABLE_ID]));
            entResvCustomePackBilling.setCustomerIdToCharge(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_CUSTOMER_ID_TO_CHARGE]));
            entResvCustomePackBilling.setCustomerIdConsume(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_CUSTOMER_ID_CONSUME]));
            entResvCustomePackBilling.setFixRoom(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_FIX_ROOM]));
            entResvCustomePackBilling.setFixPic(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_FIX_PIC]));
            entResvCustomePackBilling.setCurrencyType(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_CURRENCY_TYPE]));
            entResvCustomePackBilling.setServiceInclude(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_SERVICE_INCLUDE]));
            entResvCustomePackBilling.setTaxInclude(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TAX_INCLUDE]));
        } catch (Exception e) {
        }
    }

    public static void resultToObjectJoin(ResultSet rs, ResvCustomPackBilling entResvCustomePackBilling) {
        try {
            entResvCustomePackBilling.setOID(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_CUSTOME_PACK_BILLING_ID]));
            entResvCustomePackBilling.setBillingTypeItemId(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_BILLING_TYPE_ITEM_ID]));
            entResvCustomePackBilling.setBillingTypeId(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_BILLING_TYPE_ID]));
            entResvCustomePackBilling.setTravelPackTypeId(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TRAVEL_PACK_TYPE_ID]));
            entResvCustomePackBilling.setQuantity(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_QUANTITY]));
            entResvCustomePackBilling.setQuantityType(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_QUANTITY_TYPE]));
            entResvCustomePackBilling.setPriceRp(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_PRICE_RP]));
            entResvCustomePackBilling.setPriceUsd(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_PRICE_USD]));
            entResvCustomePackBilling.setTotalPriceRp(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TOTAL_PRICE_RP]));
            entResvCustomePackBilling.setTotalPriceUsd(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TOTAL_PRICE_USD]));
            entResvCustomePackBilling.setDiscountType(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_DISCOUNT_TYPE]));
            entResvCustomePackBilling.setDiscountPercentage(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_DISCOUNT_PERCENTAGE]));
            entResvCustomePackBilling.setDiscountAmountRp(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_DISCOUNT_AMOUNT_RP]));
            entResvCustomePackBilling.setDiscountAmountUsd(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_DISCOUNT_AMOUNT_USD]));
            entResvCustomePackBilling.setServiceRp(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_SERVICE_RP]));
            entResvCustomePackBilling.setServiceUsd(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_SERVICE_USD]));
            entResvCustomePackBilling.setTaxRp(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TAX_RP]));
            entResvCustomePackBilling.setTaxUsd(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TAX_USD]));
            entResvCustomePackBilling.setContractId(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_CONTRACT_ID]));
            entResvCustomePackBilling.setRoomClassId(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_ROOM_CLASS_ID]));
            entResvCustomePackBilling.setUseDefault(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_USE_DEFAULT]));
            entResvCustomePackBilling.setType(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TYPE]));
            entResvCustomePackBilling.setDateTaken(rs.getDate(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_DATE_TAKEN]));
            entResvCustomePackBilling.setTypeSales(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TYPE_SALES]));
            entResvCustomePackBilling.setTypeCompliment(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TYPE_COMPLIMENT]));
            entResvCustomePackBilling.setReservationId(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_RESERVATION_ID]));
            entResvCustomePackBilling.setRate(rs.getDouble(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_RATE]));
            entResvCustomePackBilling.setConsume(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_CONSUME]));
            entResvCustomePackBilling.setDuration(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_DURATION]));
            entResvCustomePackBilling.setPaxMale(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_PAX_MALE]));
            entResvCustomePackBilling.setPaxFemale(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_PAX_FEMALE]));
            entResvCustomePackBilling.setNote(rs.getString(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_NOTE]));
            entResvCustomePackBilling.setRoomId(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_ROOM_ID]));
            entResvCustomePackBilling.setTableId(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TABLE_ID]));
            entResvCustomePackBilling.setCustomerIdToCharge(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_CUSTOMER_ID_TO_CHARGE]));
            entResvCustomePackBilling.setCustomerIdConsume(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_CUSTOMER_ID_CONSUME]));
            entResvCustomePackBilling.setFixRoom(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_FIX_ROOM]));
            entResvCustomePackBilling.setFixPic(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_FIX_PIC]));
            entResvCustomePackBilling.setCurrencyType(rs.getLong(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_CURRENCY_TYPE]));
            entResvCustomePackBilling.setServiceInclude(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_SERVICE_INCLUDE]));
            entResvCustomePackBilling.setTaxInclude(rs.getInt(PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_TAX_INCLUDE]));

            entResvCustomePackBilling.setLocationName(rs.getString("locname"));
            entResvCustomePackBilling.setItemName(rs.getString("itemname"));
            entResvCustomePackBilling.setStartDate(rs.getTimestamp(PstResvCustomPackSchedule.fieldNames[PstResvCustomPackSchedule.FLD_START_DATE]));
            entResvCustomePackBilling.setEndDate(rs.getTimestamp(PstResvCustomPackSchedule.fieldNames[PstResvCustomPackSchedule.FLD_END_DATE]));
            entResvCustomePackBilling.setRoomName(rs.getString("roomname"));
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
            String sql = "SELECT * FROM " + TBL_RESV_CUSTOME_PACK_BILLING;
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
                ResvCustomPackBilling entResvCustomePackBilling = new ResvCustomPackBilling();
                resultToObject(rs, entResvCustomePackBilling);
                lists.add(entResvCustomePackBilling);
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
            String sql = "SELECT location.NAME AS locname, material.NAME AS itemname, packschedule.START_DATE, packschedule.END_DATE, room.NAME AS roomname, packbill.*"
                    + " FROM rsv_custome_package_billing AS packbill"
                    + " INNER JOIN rsv_custome_package_schedule AS packschedule"
                    + " ON packschedule.CUSTOME_PACK_BILLING_ID = packbill.CUSTOME_PACK_BILLING_ID"
                    + " INNER JOIN location AS location"
                    + " ON location.LOCATION_ID = packbill.BILLING_TYPE_ID"
                    + " INNER JOIN pos_material AS material"
                    + " ON material.MATERIAL_ID = packbill.BILLING_TYPE_ITEM_ID"
                    + " INNER JOIN pos_room AS room"
                    + " ON room.ROOM_ID = packschedule.ROOM_ID";
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
                ResvCustomPackBilling entResvCustomePackBilling = new ResvCustomPackBilling();
                resultToObjectJoin(rs, entResvCustomePackBilling);
                lists.add(entResvCustomePackBilling);
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

    public static boolean checkOID(long entResvCustomePackBillingId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_RESV_CUSTOME_PACK_BILLING + " WHERE "
                    + PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_CUSTOME_PACK_BILLING_ID] + " = " + entResvCustomePackBillingId;
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
            String sql = "SELECT COUNT(" + PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_CUSTOME_PACK_BILLING_ID] + ") FROM " + TBL_RESV_CUSTOME_PACK_BILLING;
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

    public static int getCountJoin(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(packbill." + PstResvCustomPackBilling.fieldNames[PstResvCustomPackBilling.FLD_CUSTOME_PACK_BILLING_ID] + ")"
                    + " FROM " + TBL_RESV_CUSTOME_PACK_BILLING + " AS packbill"
                    + " INNER JOIN rsv_custome_package_schedule AS packschedule"
                    + " ON packschedule.CUSTOME_PACK_BILLING_ID = packbill.CUSTOME_PACK_BILLING_ID"
                    + " INNER JOIN location AS location"
                    + " ON location.LOCATION_ID = packbill.BILLING_TYPE_ID"
                    + " INNER JOIN pos_material AS material"
                    + " ON material.MATERIAL_ID = packbill.BILLING_TYPE_ITEM_ID"
                    + " INNER JOIN pos_room AS room"
                    + " ON room.ROOM_ID = packschedule.ROOM_ID";
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
                    ResvCustomPackBilling entResvCustomePackBilling = (ResvCustomPackBilling) list.get(ls);
                    if (oid == entResvCustomePackBilling.getOID()) {
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
