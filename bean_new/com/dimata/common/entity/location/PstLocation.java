
/* Created on 	:  [date] [time] AM/PM
 *
 * @author  	: karya
 * @version  	: 01
 */
/** *****************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 ****************************************************************** */
package com.dimata.common.entity.location;

import java.sql.*;
import java.util.*;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.*;
import com.dimata.common.entity.contact.*;

//integrasi cashier vs hanoman
import com.dimata.ObjLink.BOPos.OutletLink;
import com.dimata.interfaces.BOPos.I_Outlet;
import com.dimata.common.entity.custom.*;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.common.entity.payment.PstPriceType;
import com.dimata.common.entity.payment.PstStandartRate;
import com.dimata.common.session.contact.SupplierFromWarehouse;
import com.dimata.harisma.entity.masterdata.PstDepartment;
import com.dimata.pos.entity.masterCashier.PstCashMaster;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.warehouse.PstMatDispatch;
import org.json.JSONObject;

public class PstLocation extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_PersistentExcSynch, I_Outlet {

    //public static final  String TBL_P2_LOCATION = "LOCATION";
    public static final String TBL_P2_LOCATION = "location";
    public static final int FLD_LOCATION_ID = 0;
    public static final int FLD_NAME = 1;
    public static final int FLD_CONTACT_ID = 2;
    public static final int FLD_DESCRIPTION = 3;
    public static final int FLD_CODE = 4;
    public static final int FLD_ADDRESS = 5;
    public static final int FLD_TELEPHONE = 6;
    public static final int FLD_FAX = 7;
    public static final int FLD_PERSON = 8;
    public static final int FLD_EMAIL = 9;
    public static final int FLD_TYPE = 10;
    public static final int FLD_PARENT_LOCATION_ID = 11;
    public static final int FLD_WEBSITE = 12;

    // tambahan untuk proses di prochain opie 13-06-2012
    public static final int FLD_SERVICE_PERCENT = 13;
    public static final int FLD_TAX_PERCENT = 14;

    // tambahan untuk proses di hanoman
    public static final int FLD_DEPARTMENT_ID = 15;
    public static final int FLD_USED_VAL = 16;
    public static final int FLD_SERVICE_VAL = 17;
    public static final int FLD_TAX_VALUE = 18;
    public static final int FLD_SERVICE_VAL_USD = 19;
    public static final int FLD_TAX_VALUE_USD = 20;
    public static final int FLD_REPORT_GROUP = 21;
    public static final int FLD_LOC_INDEX = 22;

    // tambah prochain add opie 03-06-2012
    public static final int FLD_TAX_SVC_DEFAULT = 23;
    public static final int FLD_PERSENTASE_DISTRIBUTION_PURCHASE_ORDER = 24;
    //add fitra 29-01-2014
    public static final int FLD_COMPANY_ID = 25;

    //add opie 20151012 untuk outlet web dan cashier
    public static final int FLD_PRICE_TYPE_ID = 26;
    public static final int FLD_STANDART_RATE_ID = 27;
    public static final int FLD_LOCATION_USED_TABLE = 28;

    //add by de koyo 20160930
    public static final int FLD_ACCOUNTING_EMAIL = 29;
    public static final int FLD_LOCATION_IP = 30;
    public static final int FLD_SISTEM_ADDRESS_HISTORY_OUTLET = 31;

    // added by dewok++ 2017-03-21
    public static final int FLD_OPENING_TIME = 32;
    public static final int FLD_CLOSING_TIME = 33;

    // added by dewok++ 2019-02-05
    public static final int FLD_DISCOUNT_TYPE_ID = 34;
    public static final int FLD_MEMBER_GROUP_ID = 35;
    public static final int FLD_MAX_EXCHANGE_DAY = 36;
    public static final int FLD_REGENCY_OID = 37;

    public static final String[] fieldNames = {
        "LOCATION_ID",
        "NAME",
        "CONTACT_ID",
        "DESCRIPTION",
        "CODE",
        "ADDRESS",
        "TELEPHONE",
        "FAX",
        "PERSON",
        "EMAIL",
        "TYPE",
        "PARENT_ID",
        "WEBSITE",
        "SERVICE_PERCENTAGE",
        "TAX_PERCENTAGE",
        "DEPARTMENT_ID",
        "USED_VALUE",
        "SERVICE_VALUE",
        "TAX_VALUE",
        "SERVICE_VALUE_USD",
        "TAX_VALUE_USD",
        "REPORT_GROUP",
        "LOC_INDEX",
        //add opie prochain 13-06-2012
        "TAX_SVC_DEFAULT",
        "PERSENTASE_DISTRIBUTION_PURCHASE_ORDER",
        "COMPANY_ID",
        //add opie-eyek 20151012
        "PRICE_TYPE_ID",
        "STANDART_RATE_ID",
        "LOCATION_USED_TABLE",
        //add by de koyo 20160930
        "ACCOUNTING_EMAIL",
        "LOCATION_IP",
        "SISTEM_ADDRESS_HISTORY_OUTLET",
        // added by dewok++ 2017-03-21
        "OPENING_TIME",
        "CLOSING_TIME",
        // added by dewok++ 2019-02-05
        "DISCOUNT_TYPE_ID",
        "MEMBER_GROUP_ID",
        "MAX_EXCHANGE_DAY",
        "REGENCY_ID"

    };
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_LONG + TYPE_FK,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_LONG + TYPE_FK,
        TYPE_STRING,
        // INI DI GUNAKAN OLEH PROCHAIN 13-06-2012
        TYPE_FLOAT,
        TYPE_FLOAT,
        // INI DI GUNAKAN OLEH HANOMAN
        TYPE_LONG,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_INT,
        //add opie 13-06-2012
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_LONG,
        //add opie-eyek 20151012
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        //add by de koyo 20160930
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        // added by dewok++ 2017-03-21
        TYPE_DATE,
        TYPE_DATE,
        // added by dewok++ 2019-02-05
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_LONG
    };

    public static final int TYPE_LOCATION_WAREHOUSE = 0;
    public static final int TYPE_LOCATION_STORE = 1;
    public static final int TYPE_LOCATION_CARGO = 2;
    public static final int TYPE_LOCATION_VENDOR = 3;
    public static final int TYPE_LOCATION_TRANSFER = 4;
    public static final int TYPE_GALLERY_CUSTOMER = 5;
    public static final int TYPE_GALLERY_CONSIGNOR = 6;
    public static final int TYPE_LOCATION_DEPARTMENT = 7;
    public static final int TYPE_LOCATION_PROJECT = 8;
    public static final int TYPE_DUTY_FREE = 9;
    public static final int TYPE_LOCATION_PABEAN = 10;

    public static final String[] fieldLocationType = {
        "Warehouse",
        "Store",
        "Cargo",
        "Vendor",
        "Transfer",
        "Gallery Customer",
        "Gallery Consignor",
        "Department",
        "Project",
        "Duty Free",
        "Pabean"
    };

    //add opie eyek 12-06-2012 untuk tax n service default
    public static final int TSD_INCLUDE_NOTCHANGABLE = 0;
    public static final int TSD_NOTINCLUDE_NOTCHANGABLE = 1;
    public static final int TSD_INCLUDE_CHANGABLE = 2;
    public static final int TSD_NOTINCLUDE_CHANGABLE = 3;

    public static final String tsdNames[][] = {
        {"include - not changable", "notinclude - not changable", "include - changable", "not include - changable"},
        {"include - not changable", "notinclude - not changable", "include - changable", "not include - changable"}
    };

    public PstLocation() {
    }

    public PstLocation(int i) throws DBException {
        super(new PstLocation());
    }

    public PstLocation(String sOid) throws DBException {
        super(new PstLocation(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstLocation(long lOid) throws DBException {
        super(new PstLocation(0));
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
        return TBL_P2_LOCATION;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstLocation().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        try {
            Location location = (Location) ent;
            long oid = ent.getOID();
            PstLocation pstLocation = new PstLocation(oid);
            location.setOID(oid);

            location.setName(pstLocation.getString(FLD_NAME));
            location.setContactId(pstLocation.getlong(FLD_CONTACT_ID));
            location.setDescription(pstLocation.getString(FLD_DESCRIPTION));
            location.setCode(pstLocation.getString(FLD_CODE));
            location.setAddress(pstLocation.getString(FLD_ADDRESS));
            location.setTelephone(pstLocation.getString(FLD_TELEPHONE));
            location.setFax(pstLocation.getString(FLD_FAX));
            location.setPerson(pstLocation.getString(FLD_PERSON));
            location.setEmail(pstLocation.getString(FLD_EMAIL));
            location.setType(pstLocation.getInt(FLD_TYPE));
            location.setParentLocationId(pstLocation.getlong(FLD_PARENT_LOCATION_ID));
            location.setWebsite(pstLocation.getString(FLD_WEBSITE));
            location.setLocIndex(pstLocation.getInt(FLD_LOC_INDEX));

            // ini untuk tambahan prochain 13-06-2012
            location.setServicePersen(pstLocation.getdouble(FLD_SERVICE_PERCENT));
            location.setTaxPersen(pstLocation.getdouble(FLD_TAX_PERCENT));

            // ini untuk hanoman
            location.setDepartmentId(pstLocation.getlong(FLD_DEPARTMENT_ID));
            location.setTypeBase(pstLocation.getInt(FLD_USED_VAL));
            location.setServiceValue(pstLocation.getdouble(FLD_SERVICE_VAL));
            location.setTaxValue(pstLocation.getdouble(FLD_TAX_VALUE));
            location.setServiceValueUsd(pstLocation.getdouble(FLD_SERVICE_VAL_USD));
            location.setTaxValueUsd(pstLocation.getInt(FLD_TAX_VALUE_USD));
            location.setReportGroup(pstLocation.getInt(FLD_REPORT_GROUP));

            //ini untuk prochain add opie 13-06-2012
            location.setTaxSvcDefault(pstLocation.getInt(FLD_TAX_SVC_DEFAULT));
            location.setPersentaseDistributionPurchaseOrder(pstLocation.getdouble(FLD_PERSENTASE_DISTRIBUTION_PURCHASE_ORDER));
            //ad fitra
            location.setCompanyId(pstLocation.getlong(FLD_COMPANY_ID));
            //add opie-eyek
            location.setPriceTypeId(pstLocation.getlong(FLD_PRICE_TYPE_ID));
            location.setStandarRateId(pstLocation.getlong(FLD_STANDART_RATE_ID));
            location.setUseTable(pstLocation.getlong(FLD_LOCATION_USED_TABLE));

            //add by de Koyo 20160930
            location.setAcountingEmail(pstLocation.getString(FLD_ACCOUNTING_EMAIL));
            location.setLocationIp(pstLocation.getString(FLD_LOCATION_IP));
            location.setSistemAddressHistoryOutlet(pstLocation.getString(FLD_SISTEM_ADDRESS_HISTORY_OUTLET));

            // added by dewok++ 2017-03-21
            location.setOpeningTime(pstLocation.getDate(FLD_OPENING_TIME));
            location.setClosingTime(pstLocation.getDate(FLD_CLOSING_TIME));

            // added by dewok++ 2019-02-05
            location.setDiscountTypeId(pstLocation.getlong(FLD_DISCOUNT_TYPE_ID));
            location.setMemberGroupId(pstLocation.getlong(FLD_MEMBER_GROUP_ID));
            location.setMaxExchangeDay(pstLocation.getInt(FLD_MAX_EXCHANGE_DAY));
            location.setRegencyId(pstLocation.getlong(FLD_REGENCY_OID));

            return location.getOID();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstLocation(0), DBException.UNKNOWN);
        }
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((Location) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((Location) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static Location fetchExc(long oid) throws DBException {
        try {
            Location location = new Location();

            PstLocation pstLocation = new PstLocation(oid);
            location.setOID(oid);

            location.setName(pstLocation.getString(FLD_NAME));
            location.setContactId(pstLocation.getlong(FLD_CONTACT_ID));
            location.setDescription(pstLocation.getString(FLD_DESCRIPTION));
            location.setCode(pstLocation.getString(FLD_CODE));
            location.setAddress(pstLocation.getString(FLD_ADDRESS));
            location.setTelephone(pstLocation.getString(FLD_TELEPHONE));
            location.setFax(pstLocation.getString(FLD_FAX));
            location.setPerson(pstLocation.getString(FLD_PERSON));
            location.setEmail(pstLocation.getString(FLD_EMAIL));
            location.setType(pstLocation.getInt(FLD_TYPE));
            location.setParentLocationId(pstLocation.getlong(FLD_PARENT_LOCATION_ID));
            location.setWebsite(pstLocation.getString(FLD_WEBSITE));
            location.setLocIndex(pstLocation.getInt(FLD_LOC_INDEX));

            // ini untuk tambahan prochain add opie 13-06-2012
            location.setServicePersen(pstLocation.getdouble(FLD_SERVICE_PERCENT));
            location.setTaxPersen(pstLocation.getdouble(FLD_TAX_PERCENT));

            // ini untuk hanoman
            location.setDepartmentId(pstLocation.getlong(FLD_DEPARTMENT_ID));
            location.setTypeBase(pstLocation.getInt(FLD_USED_VAL));
            location.setServiceValue(pstLocation.getdouble(FLD_SERVICE_VAL));
            location.setTaxValue(pstLocation.getdouble(FLD_TAX_VALUE));
            location.setServiceValueUsd(pstLocation.getdouble(FLD_SERVICE_VAL_USD));
            location.setTaxValueUsd(pstLocation.getdouble(FLD_TAX_VALUE_USD));
            location.setReportGroup(pstLocation.getInt(FLD_REPORT_GROUP));

            //ini untuk prohchain add opie 13-06-2012
            location.setTaxSvcDefault(pstLocation.getInt(FLD_TAX_SVC_DEFAULT));
            location.setPersentaseDistributionPurchaseOrder(pstLocation.getdouble(FLD_PERSENTASE_DISTRIBUTION_PURCHASE_ORDER));

            //add fitra 29-01-2014
            location.setCompanyId(pstLocation.getlong(FLD_COMPANY_ID));
            location.setStandarRateId(pstLocation.getlong(FLD_STANDART_RATE_ID));
            location.setPriceTypeId(pstLocation.getlong(FLD_PRICE_TYPE_ID));
            location.setUseTable(pstLocation.getlong(FLD_LOCATION_USED_TABLE));

            //add by de Koyo 20160930
            location.setAcountingEmail(pstLocation.getString(FLD_ACCOUNTING_EMAIL));
            location.setLocationIp(pstLocation.getString(FLD_LOCATION_IP));
            location.setSistemAddressHistoryOutlet(pstLocation.getString(FLD_SISTEM_ADDRESS_HISTORY_OUTLET));

            // added by dewok++ 2017-03-21
            location.setOpeningTime(pstLocation.getDate(FLD_OPENING_TIME));
            location.setClosingTime(pstLocation.getDate(FLD_CLOSING_TIME));

            // added by dewok++ 2019-02-05
            location.setDiscountTypeId(pstLocation.getlong(FLD_DISCOUNT_TYPE_ID));
            location.setMemberGroupId(pstLocation.getlong(FLD_MEMBER_GROUP_ID));
            location.setMaxExchangeDay(pstLocation.getInt(FLD_MAX_EXCHANGE_DAY));
            location.setRegencyId(pstLocation.getlong(FLD_REGENCY_OID));

            String s = location.getName();
            return location;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstLocation(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(Location location) throws DBException {
        try {
            PstLocation pstLocation = new PstLocation(0);

            pstLocation.setString(FLD_NAME, location.getName());
            pstLocation.setLong(FLD_CONTACT_ID, location.getContactId());
            pstLocation.setString(FLD_DESCRIPTION, location.getDescription());
            pstLocation.setString(FLD_CODE, location.getCode());
            pstLocation.setString(FLD_ADDRESS, location.getAddress());
            pstLocation.setString(FLD_TELEPHONE, location.getTelephone());
            pstLocation.setString(FLD_FAX, location.getFax());
            pstLocation.setString(FLD_PERSON, location.getPerson());
            pstLocation.setString(FLD_EMAIL, location.getEmail());
            pstLocation.setInt(FLD_TYPE, location.getType());
            pstLocation.setLong(FLD_PARENT_LOCATION_ID, location.getParentLocationId());
            pstLocation.setString(FLD_WEBSITE, location.getWebsite());
            pstLocation.setInt(FLD_LOC_INDEX, location.getLocIndex());

            // ini tmbahan untuk prochain add opie 13-06-2012
            pstLocation.setDouble(FLD_SERVICE_PERCENT, location.getServicePersen());
            pstLocation.setDouble(FLD_TAX_PERCENT, location.getTaxPersen());

            // ini hanya untuk di gunakan oleh hsnoman
            pstLocation.setLong(FLD_DEPARTMENT_ID, location.getDepartmentId());
            pstLocation.setInt(FLD_USED_VAL, location.getTypeBase());
            pstLocation.setDouble(FLD_SERVICE_VAL, location.getServiceValue());
            pstLocation.setDouble(FLD_TAX_VALUE, location.getTaxValue());
            pstLocation.setDouble(FLD_SERVICE_VAL_USD, location.getServiceValueUsd());
            pstLocation.setDouble(FLD_TAX_VALUE_USD, location.getTaxValueUsd());
            pstLocation.setInt(FLD_REPORT_GROUP, location.getReportGroup());

            //ini untuk prohchain add opie 13-06-2012
            pstLocation.setInt(FLD_TAX_SVC_DEFAULT, location.getTaxSvcDefault());
            pstLocation.setDouble(FLD_PERSENTASE_DISTRIBUTION_PURCHASE_ORDER, location.getPersentaseDistributionPurchaseOrder());
            //add fitra 29-01-2014
            pstLocation.setLong(FLD_COMPANY_ID, location.getCompanyId());

            pstLocation.setLong(FLD_PRICE_TYPE_ID, location.getPriceTypeId());
            pstLocation.setLong(FLD_STANDART_RATE_ID, location.getStandarRateId());
            pstLocation.setLong(FLD_LOCATION_USED_TABLE, location.getUseTable());

            //add by de Koyo 20160930
            pstLocation.setString(FLD_ACCOUNTING_EMAIL, location.getAcountingEmail());
            pstLocation.setString(FLD_LOCATION_IP, location.getLocationIp());
            pstLocation.setString(FLD_SISTEM_ADDRESS_HISTORY_OUTLET, location.getSistemAddressHistoryOutlet());

            // added by dewok++ 2017-03-21
            pstLocation.setDate(FLD_OPENING_TIME, location.getOpeningTime());
            pstLocation.setDate(FLD_CLOSING_TIME, location.getClosingTime());

            // added by dewok++ 2019-02-05
            pstLocation.setLong(FLD_DISCOUNT_TYPE_ID, location.getDiscountTypeId());
            pstLocation.setLong(FLD_MEMBER_GROUP_ID, location.getMemberGroupId());
            pstLocation.setInt(FLD_MAX_EXCHANGE_DAY, location.getMaxExchangeDay());
            pstLocation.setLong(FLD_REGENCY_OID, location.getRegencyId());

            pstLocation.insert();

            long oidDataSync = PstDataSyncSql.insertExc(pstLocation.getInsertSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
            location.setOID(pstLocation.getlong(FLD_LOCATION_ID));
            //kebutuhan untuk service transfer katalog
            PstDataCustom.insertDataForSyncAllLocation(pstLocation.getInsertSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstLocation(0), DBException.UNKNOWN);
        }
        return location.getOID();
    }

    public static long updateExc(Location location) throws DBException {
        try {
            if (location.getOID() != 0) {
                PstLocation pstLocation = new PstLocation(location.getOID());

                pstLocation.setString(FLD_NAME, location.getName());
                pstLocation.setLong(FLD_CONTACT_ID, location.getContactId());
                pstLocation.setString(FLD_DESCRIPTION, location.getDescription());
                pstLocation.setString(FLD_CODE, location.getCode());
                pstLocation.setString(FLD_ADDRESS, location.getAddress());
                pstLocation.setString(FLD_TELEPHONE, location.getTelephone());
                pstLocation.setString(FLD_FAX, location.getFax());
                pstLocation.setString(FLD_PERSON, location.getPerson());
                pstLocation.setString(FLD_EMAIL, location.getEmail());
                pstLocation.setInt(FLD_TYPE, location.getType());
                pstLocation.setLong(FLD_PARENT_LOCATION_ID, location.getParentLocationId());
                pstLocation.setString(FLD_WEBSITE, location.getWebsite());
                pstLocation.setInt(FLD_LOC_INDEX, location.getLocIndex());

                // ini tambahan prochain add opie 13-06-2012
                pstLocation.setDouble(FLD_SERVICE_PERCENT, location.getServicePersen());
                pstLocation.setDouble(FLD_TAX_PERCENT, location.getTaxPersen());

                // ini hanya untuk di gunakan oleh hanoman
                pstLocation.setLong(FLD_DEPARTMENT_ID, location.getDepartmentId());
                pstLocation.setInt(FLD_USED_VAL, location.getTypeBase());
                pstLocation.setDouble(FLD_SERVICE_VAL, location.getServiceValue());
                pstLocation.setDouble(FLD_TAX_VALUE, location.getTaxValue());
                pstLocation.setDouble(FLD_SERVICE_VAL_USD, location.getServiceValueUsd());
                pstLocation.setDouble(FLD_TAX_VALUE_USD, location.getTaxValueUsd());
                pstLocation.setInt(FLD_REPORT_GROUP, location.getReportGroup());

                //ini untuk prohchain add opie 13-06-2012
                pstLocation.setInt(FLD_TAX_SVC_DEFAULT, location.getTaxSvcDefault());
                pstLocation.setDouble(FLD_PERSENTASE_DISTRIBUTION_PURCHASE_ORDER, location.getPersentaseDistributionPurchaseOrder());
                //add fitra 29-01-2014
                pstLocation.setLong(FLD_COMPANY_ID, location.getCompanyId());
                pstLocation.setLong(FLD_PRICE_TYPE_ID, location.getPriceTypeId());
                pstLocation.setLong(FLD_STANDART_RATE_ID, location.getStandarRateId());
                pstLocation.setLong(FLD_LOCATION_USED_TABLE, location.getUseTable());

                pstLocation.setString(FLD_ACCOUNTING_EMAIL, location.getAcountingEmail());
                pstLocation.setString(FLD_LOCATION_IP, location.getLocationIp());
                pstLocation.setString(FLD_SISTEM_ADDRESS_HISTORY_OUTLET, location.getSistemAddressHistoryOutlet());

                // added by dewok++ 2017-03-21
                pstLocation.setDate(FLD_OPENING_TIME, location.getOpeningTime());
                pstLocation.setDate(FLD_CLOSING_TIME, location.getClosingTime());

                // added by dewok++ 2019-02-05
                pstLocation.setLong(FLD_DISCOUNT_TYPE_ID, location.getDiscountTypeId());
                pstLocation.setLong(FLD_MEMBER_GROUP_ID, location.getMemberGroupId());
                pstLocation.setInt(FLD_MAX_EXCHANGE_DAY, location.getMaxExchangeDay());
                pstLocation.setLong(FLD_REGENCY_OID, location.getRegencyId());

                pstLocation.update();

                long oidDataSync = PstDataSyncSql.insertExc(pstLocation.getUpdateSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
                //kebutuhan untuk service transfer katalog
                PstDataCustom.insertDataForSyncAllLocation(pstLocation.getUpdateSQL());
                return location.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstLocation(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstLocation pstLocation = new PstLocation(oid);
            pstLocation.delete();

            long oidDataSync = PstDataSyncSql.insertExc(pstLocation.getDeleteSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
            //kebutuhan untuk service transfer katalog
            PstDataCustom.insertDataForSyncAllLocation(pstLocation.getDeleteSQL());

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstLocation(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_P2_LOCATION;
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

            System.out.println("List sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Location location = new Location();
                resultToObject(rs, location);
                lists.add(location);
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

    public static Vector listHpp(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_P2_LOCATION + " "
                    + " INNER JOIN " + PstMatDispatch.TBL_DISPATCH + ""
                    + " ON location.LOCATION_ID = pos_dispatch_material.DISPATCH_TO";
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

            System.out.println("List sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Location location = new Location();
                resultToObject(rs, location);
                lists.add(location);
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

    //update by fitra
    /*untuk menjoinkan tabel lokasi dan nama perusahaan
     *
     */
    public static Vector listJoin(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT loc.*, com." + PstCompany.fieldNames[PstCompany.FLD_COMPANY_NAME] + ", dep." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + ", ct.CODE AS CODE_CURR,  pt.NAME AS PRICE_TYPE_NAME FROM "
                    + TBL_P2_LOCATION + " AS loc LEFT JOIN "
                    + PstCompany.TBL_AISO_COMPANY + " AS com ON loc." + PstLocation.fieldNames[PstLocation.FLD_COMPANY_ID] + " = com." + PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID]
                    + " LEFT JOIN "
                    + PstDepartment.TBL_HR_DEPARTMENT + " AS dep ON loc." + PstLocation.fieldNames[PstLocation.FLD_DEPARTMENT_ID] + " = dep." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " LEFT JOIN " + PstStandartRate.TBL_POS_STANDART_RATE + " AS sr "
                    + " ON loc." + PstLocation.fieldNames[PstLocation.FLD_STANDART_RATE_ID] + "=sr." + PstStandartRate.fieldNames[PstStandartRate.FLD_STANDART_RATE_ID]
                    + " LEFT JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " AS ct "
                    + " ON sr." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] + " = ct." + PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID]
                    + " LEFT JOIN " + PstPriceType.TBL_POS_PRICE_TYPE + " AS pt "
                    + "ON loc." + PstLocation.fieldNames[PstLocation.FLD_PRICE_TYPE_ID] + " = pt." + PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID];

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

            System.out.println("List sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Location location = new Location();
                resultToObjectJoin(rs, location);
                lists.add(location);
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

    //add fitra 29-01-2014
    private static void resultToObjectJoin(ResultSet rs, Location location) {
        try {
            location.setOID(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]));
            location.setName(rs.getString(PstLocation.fieldNames[PstLocation.FLD_NAME]));
            location.setContactId(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_CONTACT_ID]));
            location.setDescription(rs.getString(PstLocation.fieldNames[PstLocation.FLD_DESCRIPTION]));
            location.setCode(rs.getString(PstLocation.fieldNames[PstLocation.FLD_CODE]));
            location.setAddress(rs.getString(PstLocation.fieldNames[PstLocation.FLD_ADDRESS]));
            location.setTelephone(rs.getString(PstLocation.fieldNames[PstLocation.FLD_TELEPHONE]));
            location.setFax(rs.getString(PstLocation.fieldNames[PstLocation.FLD_FAX]));
            location.setPerson(rs.getString(PstLocation.fieldNames[PstLocation.FLD_PERSON]));
            location.setEmail(rs.getString(PstLocation.fieldNames[PstLocation.FLD_EMAIL]));
            location.setType(rs.getInt(PstLocation.fieldNames[PstLocation.FLD_TYPE]));
            location.setParentLocationId(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_PARENT_LOCATION_ID]));
            location.setWebsite(rs.getString(PstLocation.fieldNames[PstLocation.FLD_WEBSITE]));
            location.setLocIndex(rs.getInt(PstLocation.fieldNames[PstLocation.FLD_LOC_INDEX]));

            // ini digunakan prochain add opie 13-06-2012
            location.setServicePersen(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_SERVICE_PERCENT]));
            location.setTaxPersen(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_TAX_PERCENT]));

            // ini digunakan oleh hanoman
            location.setDepartmentId(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_DEPARTMENT_ID]));
            location.setTypeBase(rs.getInt(PstLocation.fieldNames[PstLocation.FLD_USED_VAL]));
            location.setServiceValue(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_SERVICE_VAL]));
            location.setTaxValue(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_TAX_VALUE]));
            location.setServiceValueUsd(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_SERVICE_VAL_USD]));
            location.setTaxValueUsd(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_TAX_VALUE_USD]));
            location.setReportGroup(rs.getInt(PstLocation.fieldNames[PstLocation.FLD_REPORT_GROUP]));

            //ini untuk prohchain add opie 13-06-2012
            location.setTaxSvcDefault(rs.getInt(PstLocation.fieldNames[PstLocation.FLD_TAX_SVC_DEFAULT]));
            location.setPersentaseDistributionPurchaseOrder(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_PERSENTASE_DISTRIBUTION_PURCHASE_ORDER]));
            //add fitra 29-01-2014
            location.setCompanyId(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_COMPANY_ID]));
            location.setCompanyName(rs.getString(PstCompany.fieldNames[PstCompany.FLD_COMPANY_NAME]));
            location.setDepartmentName(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
            location.setPriceTypeName(rs.getString("PRICE_TYPE_NAME"));
            location.setSymbolCurr(rs.getString("CODE_CURR"));

            //add by de koyo 20160930
            location.setAcountingEmail(rs.getString(PstLocation.fieldNames[PstLocation.FLD_ACCOUNTING_EMAIL]));
            location.setLocationIp(rs.getString(PstLocation.fieldNames[PstLocation.FLD_LOCATION_IP]));
            location.setSistemAddressHistoryOutlet(rs.getString(PstLocation.fieldNames[PstLocation.FLD_SISTEM_ADDRESS_HISTORY_OUTLET]));

            // added by dewok++ 2017-03-21
            location.setOpeningTime(rs.getTime(PstLocation.fieldNames[PstLocation.FLD_OPENING_TIME]));
            location.setClosingTime(rs.getTime(PstLocation.fieldNames[PstLocation.FLD_CLOSING_TIME]));
            location.setMaxExchangeDay(rs.getInt(PstLocation.fieldNames[PstLocation.FLD_MAX_EXCHANGE_DAY]));

        } catch (Exception e) {
        }
    }

    public static Vector listLocationStore(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT DISTINCT lc.* FROM " + TBL_P2_LOCATION + " AS lc "
                    + " INNER JOIN " + PstCashMaster.TBL_CASH_MASTER + " AS cm"
                    + " ON lc." + fieldNames[FLD_LOCATION_ID] + "= cm." + PstCashMaster.fieldNames[PstCashMaster.FLD_LOCATION_ID];

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

            System.out.println("List sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Location location = new Location();
                resultToObject(rs, location);
                lists.add(location);
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

    public static Vector listLocationCreateDocument(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT DISTINCT LC.* FROM " + TBL_P2_LOCATION + " AS LC ";

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

            System.out.println("List sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Location location = new Location();
                resultToObject(rs, location);
                lists.add(location);
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

    private static void resultToObject(ResultSet rs, Location location) {
        try {
            location.setOID(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]));
            location.setName(rs.getString(PstLocation.fieldNames[PstLocation.FLD_NAME]));
            location.setContactId(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_CONTACT_ID]));
            location.setDescription(rs.getString(PstLocation.fieldNames[PstLocation.FLD_DESCRIPTION]));
            location.setCode(rs.getString(PstLocation.fieldNames[PstLocation.FLD_CODE]));
            location.setAddress(rs.getString(PstLocation.fieldNames[PstLocation.FLD_ADDRESS]));
            location.setTelephone(rs.getString(PstLocation.fieldNames[PstLocation.FLD_TELEPHONE]));
            location.setFax(rs.getString(PstLocation.fieldNames[PstLocation.FLD_FAX]));
            location.setPerson(rs.getString(PstLocation.fieldNames[PstLocation.FLD_PERSON]));
            location.setEmail(rs.getString(PstLocation.fieldNames[PstLocation.FLD_EMAIL]));
            location.setType(rs.getInt(PstLocation.fieldNames[PstLocation.FLD_TYPE]));
            location.setParentLocationId(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_PARENT_LOCATION_ID]));
            location.setWebsite(rs.getString(PstLocation.fieldNames[PstLocation.FLD_WEBSITE]));
            location.setLocIndex(rs.getInt(PstLocation.fieldNames[PstLocation.FLD_LOC_INDEX]));

            // ini digunakan prochain add opie 13-06-2012
            location.setServicePersen(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_SERVICE_PERCENT]));
            location.setTaxPersen(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_TAX_PERCENT]));

            // ini digunakan oleh hanoman
            location.setDepartmentId(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_DEPARTMENT_ID]));
            location.setTypeBase(rs.getInt(PstLocation.fieldNames[PstLocation.FLD_USED_VAL]));
            location.setServiceValue(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_SERVICE_VAL]));
            location.setTaxValue(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_TAX_VALUE]));
            location.setServiceValueUsd(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_SERVICE_VAL_USD]));
            location.setTaxValueUsd(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_TAX_VALUE_USD]));
            location.setReportGroup(rs.getInt(PstLocation.fieldNames[PstLocation.FLD_REPORT_GROUP]));

            //ini untuk prohchain add opie 13-06-2012
            location.setTaxSvcDefault(rs.getInt(PstLocation.fieldNames[PstLocation.FLD_TAX_SVC_DEFAULT]));
            location.setPersentaseDistributionPurchaseOrder(rs.getDouble(PstLocation.fieldNames[PstLocation.FLD_PERSENTASE_DISTRIBUTION_PURCHASE_ORDER]));
            //add fitra 29-01-2014
            location.setCompanyId(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_COMPANY_ID]));

            // added by dewok++ 2017-03-21
            location.setOpeningTime(rs.getTime(PstLocation.fieldNames[PstLocation.FLD_OPENING_TIME]));
            location.setClosingTime(rs.getTime(PstLocation.fieldNames[PstLocation.FLD_CLOSING_TIME]));

            location.setPriceTypeId(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_PRICE_TYPE_ID]));
            location.setStandarRateId(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_STANDART_RATE_ID]));

            // added by dewok++ 2019-02-05
            location.setDiscountTypeId(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_DISCOUNT_TYPE_ID]));
            location.setMemberGroupId(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_MEMBER_GROUP_ID]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long locationId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_P2_LOCATION + " WHERE "
                    + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " = " + locationId;

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
            String sql = "SELECT COUNT(" + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + ") FROM " + TBL_P2_LOCATION;
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

    //add by fitra 29-01-2014
    public static int getCountJoin(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + ") FROM " + TBL_P2_LOCATION + " AS loc LEFT JOIN "
                    + PstCompany.TBL_AISO_COMPANY + " AS com ON loc." + PstLocation.fieldNames[PstLocation.FLD_COMPANY_ID] + " = com." + PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID];
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
    public static int findLimitStart(long oid, int recordToGet, String whereClause) {
        String order = "";
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, order);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    Location location = (Location) list.get(ls);
                    if (oid == location.getOID()) {
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

    private static Vector list(long oidLocation) {

        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT LOC.* "
                    + ", CONT." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]
                    + ", CONT." + PstContactList.fieldNames[PstContactList.FLD_PERSON_LASTNAME]
                    + " FROM " + TBL_P2_LOCATION + " LOC "
                    + " LEFT JOIN " + PstContactList.TBL_CONTACT_LIST + " CONT "
                    + " ON LOC." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
                    + " = CONT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
                    + " WHERE LOC." + fieldNames[FLD_PARENT_LOCATION_ID] + (oidLocation == 0 ? " IS NULL" : (" = " + oidLocation));

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector temp = new Vector(1, 1);
                Location location = new Location();
                ContactList contact = new ContactList();
                resultToObject(rs, location);
                temp.add(location);

                contact.setPersonName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]));
                contact.setPersonLastname(rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_LASTNAME]));
                temp.add(contact);

                lists.add(temp);
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

    public static Vector getNestedLocation(long oid, Vector result) {
        try {
            Vector locations = PstLocation.list(oid);

            if ((locations.size() < 1) || (locations == null)) {
                return new Vector(1, 1);
            } else {
                for (int pd = 0; pd < locations.size(); pd++) {
                    Vector temp = (Vector) locations.get(pd);
                    Location location = (Location) temp.get(0);
                    oid = location.getOID();
                    long parent = location.getParentLocationId();
                    int indent = ifExist(result, parent);
                    location.setCode(indent + "/" + location.getCode());
                    temp.setElementAt(location, 0);
                    result.add(temp);
                    getNestedLocation(oid, result);
                }
            }
            return result;
        } catch (Exception exc) {
            return null;
        }
    }

    private static int ifExist(Vector result, long parent) {
        int indent = 0;
        for (int i = 0; i < result.size(); i++) {
            Vector temp = (Vector) result.get(i);
            Location location = (Location) temp.get(0);
            long oid = location.getOID();
            if (parent == oid) {
                String locCode = location.getCode();
                int idn = locCode.indexOf("/");
                int existIdn = 0;
                if (idn > 0) {
                    existIdn = Integer.parseInt(locCode.substring(0, idn));
                }
                indent = existIdn + 1;
            }
        }
        return indent;
    }

    /**
     * * function for data synchronization **
     */
    public long insertExcSynch(Entity ent) throws Exception {
        return insertExcSynch((Location) ent);
    }

    public static long insertExcSynch(Location location) throws DBException {
        long newOID = 0;
        long originalOID = location.getOID();
        try {
            newOID = insertExc(location);
            if (newOID != 0) {  // sukses insert ?
                updateSynchOID(newOID, originalOID);
                return originalOID;
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstLocation(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long updateSynchOID(long newOID, long originalOID) throws DBException {
        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE " + PstLocation.TBL_P2_LOCATION + " SET "
                    + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " = " + originalOID
                    + " WHERE " + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]
                    + " = " + newOID;

            int Result = DBHandler.execUpdate(sql);

            return originalOID;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static Location fetchByCode(String code) {
        com.dimata.posbo.db.DBResultSet dbrs = null;
        Location loc = new Location();
        try {
            String sql = "SELECT * FROM " + TBL_P2_LOCATION
                    + " WHERE " + fieldNames[FLD_CODE]
                    + " = '" + code + "'";
            dbrs = com.dimata.posbo.db.DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                resultToObject(rs, loc);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            com.dimata.posbo.db.DBResultSet.close(dbrs);
        }
        return loc;
    }

    //Id Mesin
    public static Location fetchByIdMachine(String codeMachine) {
        com.dimata.posbo.db.DBResultSet dbrs = null;
        Location loc = new Location();
        try {
            String sql = "SELECT * FROM " + TBL_P2_LOCATION
                    + " WHERE LEFT(" + fieldNames[FLD_DESCRIPTION] + ", 3)"
                    + " = '" + codeMachine + "'";
            dbrs = com.dimata.posbo.db.DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                resultToObject(rs, loc);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            com.dimata.posbo.db.DBResultSet.close(dbrs);
        }
        return loc;
    }

    //===============================================================
    //INTEGRASI HANOMAN VS POS
    public long deleteOutlet(OutletLink outletLink) {
        try {
            //long oid = deleteByLocationId(outletLink.getOutletId());
            PstLocation.deleteExc(outletLink.getOutletId());
        } catch (Exception e) {
            System.out.println("exception e :: " + e.toString());
        }
        return outletLink.getOutletId();
    }

    public long insertOutlet(OutletLink outletLink) {
        Location location = new Location();
        location.setName(outletLink.getName());
        location.setCode(outletLink.getCode());
        location.setDescription(outletLink.getDescription());
        location.setType(PstLocation.TYPE_LOCATION_STORE);
        location.setAddress("-");

        long oid = 0;
        try {
            oid = PstLocation.insertExc(location);
            oid = synchronizeOID(oid, outletLink.getOutletId());
        } catch (Exception e) {
        }

        return oid;
    }

    public long synchronizeOID(long oldOID, long newOID) {
        String sql = "UPDATE " + TBL_P2_LOCATION
                + " SET " + fieldNames[FLD_LOCATION_ID] + "=" + newOID
                + " WHERE " + fieldNames[FLD_LOCATION_ID] + "=" + oldOID;

        try {
            DBHandler.execUpdate(sql);
        } catch (Exception e) {
            return 0;
        }

        return newOID;
    }

    public long updateOutlet(OutletLink outletLink) {

        System.out.println("in - MATERIAL -update outlet || location");
        System.out.println("outletLink.getOutletId() : " + outletLink.getOutletId());

        Location location = new Location();
        long oid = 0;

        try {

            location = PstLocation.fetchExc(outletLink.getOutletId());

            location.setName(outletLink.getName());
            location.setCode(outletLink.getCode());
            location.setDescription(outletLink.getDescription());
            location.setType(PstLocation.TYPE_LOCATION_STORE);

            oid = PstLocation.updateExc(location);

        } catch (Exception e) {
            System.out.println("Exception e : " + e.toString());
        }

        return oid;

    }

    //end END INTEGRASI
    /**
     * * -------------------------- **
     */
    public static long getLocationByType(int type) {
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT " + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]
                    + " FROM " + TBL_P2_LOCATION
                    + " WHERE " + PstLocation.fieldNames[PstLocation.FLD_TYPE]
                    + " = " + type;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            long oid = 0;
            while (rs.next()) {
                oid = rs.getLong(1);
            }
            rs.close();
            return oid;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;

    }

    public static Vector listLocationAssign(long userId, String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT LOC.* "
                    + " FROM " + TBL_P2_LOCATION + " LOC "
                    + " INNER JOIN " + PstDataCustom.TBL_DATA_CUSTOM + " DC "
                    + " ON DC." + PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_VALUE]
                    + " = " + fieldNames[PstLocation.FLD_LOCATION_ID]
                    + " WHERE " + PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID]
                    + " = " + userId;

            if (whereClause != null && whereClause.length() > 0) {
                sql += " AND LOC." + whereClause;
            }

            sql = sql + " ORDER BY LOC." + fieldNames[PstLocation.FLD_NAME];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            Vector result = new Vector();
            while (rs.next()) {
                Location location = new Location();
                PstLocation.resultToObject(rs, location);
                result.add(location);
            }
            rs.close();
            return result;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();

    }

    /**
     * fungsi ini digunakan untuk mendapatkan list location dalam bentuk
     * hashtable create by: gwawan@dimata 26 Sep 2007
     *
     * @param
     * @return Hashtable
     */
    public static Hashtable getHashListLocation() {
        Hashtable hash = new Hashtable();
        try {
            Vector vctLocation = PstLocation.list(0, 0, "", "");
            for (int i = 0; i < vctLocation.size(); i++) {
                Location location = (Location) vctLocation.get(i);
                hash.put(String.valueOf(location.getOID()), location.getName());
            }
        } catch (Exception e) {
            System.out.println("Exc. in hashListLocation: " + e.toString());
        }
        return hash;
    }

    /*
//       * update opie-eyek 20131118
       * sql:  SELECT LC.LOCATION_ID FROM location AS LC
               INNER JOIN pos_sales_person AS PSP
               ON LC.LOCATION_ID=PSP.ASSIGN_LOCATION_WAREHOUSE
               WHERE PSP.SALES_CODE='S001';
     */
    public static long getLocationBySalesCode(String salesCode) {
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]
                    + " FROM " + TBL_P2_LOCATION + " AS LC "
                    + " INNER JOIN " + PstSales.TBL_SALES + " AS PSP "
                    + " ON LC." + fieldNames[FLD_LOCATION_ID] + "=PSP." + PstSales.fieldNames[PstSales.FLD_ASSIGN_LOCATION_WAREHOUSE]
                    + " WHERE PSP." + PstSales.fieldNames[PstSales.FLD_CODE]
                    + " = '" + salesCode + "'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            long oid = 0;
            while (rs.next()) {
                oid = rs.getLong(1);
            }
            rs.close();
            return oid;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;

    }

    public static long getLocationByUserId(long oidUser) {
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT LC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]
                    + " FROM " + TBL_P2_LOCATION + " AS LC "
                    + " INNER JOIN " + PstDataCustom.TBL_DATA_CUSTOM + " AS DC "
                    + " ON LC." + fieldNames[FLD_LOCATION_ID] + "= DC." + PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_VALUE]
                    + " WHERE DC." + PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID]
                    + " = '" + oidUser + "'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            long oid = 0;
            while (rs.next()) {
                oid = rs.getLong(1);
            }
            rs.close();
            return oid;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;

    }

    public static Vector listLocationContactSupplier(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT LC.LOCATION_ID, LC.NAME, CL.CONTACT_ID FROM contact_list CL INNER JOIN location AS LC "
                    + " ON CL.LOCATION_ID=LC.LOCATION_ID";

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

            System.out.println("List sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                SupplierFromWarehouse supplierFromWarehouse = new SupplierFromWarehouse();
                resultToObjectSupplierFromWarehouse(rs, supplierFromWarehouse);
                lists.add(supplierFromWarehouse);
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

    private static void resultToObjectSupplierFromWarehouse(ResultSet rs, SupplierFromWarehouse supplierFromWarehouse) {
        try {
            //location.setOID(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]));
            supplierFromWarehouse.setContactId(rs.getLong(PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]));
            supplierFromWarehouse.setLocationName(rs.getString(PstLocation.fieldNames[PstLocation.FLD_NAME]));
            supplierFromWarehouse.setSupplierId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
        } catch (Exception e) {
        }
    }

    public static JSONObject fetchJSON(long oid) {
        JSONObject object = new JSONObject();
        try {
            Location location = PstLocation.fetchExc(oid);
            object.put(PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID], "" + location.getOID());
            object.put(PstLocation.fieldNames[PstLocation.FLD_NAME], "" + location.getName());
            object.put(PstLocation.fieldNames[PstLocation.FLD_CONTACT_ID], "" + location.getContactId());
            object.put(PstLocation.fieldNames[PstLocation.FLD_DESCRIPTION], "" + location.getDescription());
            object.put(PstLocation.fieldNames[PstLocation.FLD_CODE], "" + location.getCode());
            object.put(PstLocation.fieldNames[PstLocation.FLD_ADDRESS], "" + location.getAddress());
            object.put(PstLocation.fieldNames[PstLocation.FLD_TELEPHONE], "" + location.getTelephone());
            object.put(PstLocation.fieldNames[PstLocation.FLD_FAX], "" + location.getFax());
            object.put(PstLocation.fieldNames[PstLocation.FLD_PERSON], "" + location.getPerson());
            object.put(PstLocation.fieldNames[PstLocation.FLD_EMAIL], "" + location.getEmail());
            object.put(PstLocation.fieldNames[PstLocation.FLD_TYPE], "" + location.getType());
            object.put(PstLocation.fieldNames[PstLocation.FLD_WEBSITE], "" + location.getWebsite());
            object.put(PstLocation.fieldNames[PstLocation.FLD_SERVICE_PERCENT], "" + location.getServicePersen());
            object.put(PstLocation.fieldNames[PstLocation.FLD_TAX_PERCENT], "" + location.getTaxPersen());
            object.put(PstLocation.fieldNames[PstLocation.FLD_DEPARTMENT_ID], "" + location.getDepartmentId());
            object.put(PstLocation.fieldNames[PstLocation.FLD_USED_VAL], "" + location.getTypeBase());
            object.put(PstLocation.fieldNames[PstLocation.FLD_SERVICE_VAL], "" + location.getServiceValue());
            object.put(PstLocation.fieldNames[PstLocation.FLD_TAX_VALUE], "" + location.getTaxValue());
            object.put(PstLocation.fieldNames[PstLocation.FLD_SERVICE_VAL_USD], "" + location.getServiceValue());
            object.put(PstLocation.fieldNames[PstLocation.FLD_TAX_VALUE_USD], "" + location.getTaxValueUsd());
            object.put(PstLocation.fieldNames[PstLocation.FLD_REPORT_GROUP], "" + location.getReportGroup());
            object.put(PstLocation.fieldNames[PstLocation.FLD_LOC_INDEX], "" + location.getLocIndex());
            object.put(PstLocation.fieldNames[PstLocation.FLD_TAX_SVC_DEFAULT], "" + location.getTaxSvcDefault());
            object.put(PstLocation.fieldNames[PstLocation.FLD_PERSENTASE_DISTRIBUTION_PURCHASE_ORDER], "" + location.getPersentaseDistributionPurchaseOrder());
            object.put(PstLocation.fieldNames[PstLocation.FLD_COMPANY_ID], "" + location.getCompanyId());
            object.put(PstLocation.fieldNames[PstLocation.FLD_PRICE_TYPE_ID], "" + location.getPriceTypeId());
            object.put(PstLocation.fieldNames[PstLocation.FLD_STANDART_RATE_ID], "" + location.getStandarRateId());
            object.put(PstLocation.fieldNames[PstLocation.FLD_LOCATION_USED_TABLE], "" + location.getUseTable());
            object.put(PstLocation.fieldNames[PstLocation.FLD_ACCOUNTING_EMAIL], "" + location.getAcountingEmail());
            object.put(PstLocation.fieldNames[PstLocation.FLD_LOCATION_IP], "" + location.getLocationIp());
            object.put(PstLocation.fieldNames[PstLocation.FLD_SISTEM_ADDRESS_HISTORY_OUTLET], "" + location.getSistemAddressHistoryOutlet());
            object.put(PstLocation.fieldNames[PstLocation.FLD_OPENING_TIME], "" + location.getOpeningTime());
            object.put(PstLocation.fieldNames[PstLocation.FLD_CLOSING_TIME], "" + location.getClosingTime());
            object.put(PstLocation.fieldNames[PstLocation.FLD_DISCOUNT_TYPE_ID], "" + location.getDiscountTypeId());
            object.put(PstLocation.fieldNames[PstLocation.FLD_MEMBER_GROUP_ID], "" + location.getMemberGroupId());
            object.put(PstLocation.fieldNames[PstLocation.FLD_MAX_EXCHANGE_DAY], "" + location.getMaxExchangeDay());
        } catch (Exception exc) {
        }

        return object;
    }

}
