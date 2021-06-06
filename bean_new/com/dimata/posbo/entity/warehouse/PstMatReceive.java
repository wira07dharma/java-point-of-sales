package com.dimata.posbo.entity.warehouse;

/* package java */
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;

/* package garment */
import com.dimata.posbo.entity.purchasing.*;
import com.dimata.posbo.session.warehouse.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.common.entity.system.PstSystemProperty;
import org.json.JSONObject;

public class PstMatReceive extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_Document {

    public static final String TBL_MAT_RECEIVE = "pos_receive_material";

    public static final int FLD_RECEIVE_MATERIAL_ID = 0;
    public static final int FLD_LOCATION_ID = 1;
    public static final int FLD_RECEIVE_FROM = 2;
    public static final int FLD_LOCATION_TYPE = 3;
    public static final int FLD_RECEIVE_DATE = 4;
    public static final int FLD_REC_CODE = 5;
    public static final int FLD_REC_CODE_CNT = 6;
    public static final int FLD_RECEIVE_STATUS = 7;
    public static final int FLD_RECEIVE_SOURCE = 8;
    public static final int FLD_SUPPLIER_ID = 9;
    public static final int FLD_PURCHASE_ORDER_ID = 10;
    public static final int FLD_DISPATCH_MATERIAL_ID = 11;
    public static final int FLD_RETURN_MATERIAL_ID = 12;
    public static final int FLD_REMARK = 13;
    public static final int FLD_INVOICE_SUPPLIER = 14;
    public static final int FLD_TOTAL_PPN = 15;
    public static final int FLD_TRANSFER_STATUS = 16;
    public static final int FLD_REASON = 17;
    public static final int FLD_TERM_OF_PAYMENT = 18;
    public static final int FLD_CREDIT_TIME = 19;
    public static final int FLD_EXPIRED_DATE = 20;
    public static final int FLD_DISCOUNT = 21;
    public static final int FLD_LAST_UPDATE = 22;
    public static final int FLD_CURRENCY_ID = 23;
    public static final int FLD_TRANS_RATE = 24;
    public static final int FLD_INCLUDE_PPN = 25;
    public static final int FLD_RECEIVE_ITEM_TYPE = 26;
    public static final int FLD_RECEIVE_TYPE = 27;
    public static final int FLD_HEL = 28;
    public static final int FLD_NILAI_TUKAR = 29;
    public static final int FLD_BERAT = 30;
    public static final int FLD_TOTAL_NOTA = 31;
    public static final int FLD_KEPEMILIKAN_ID = 32;
    public static final int FLD_BERAT24K = 33;
    public static final int FLD_GONDOLA_ID = 34;
    public static final int FLD_GONDOLA_TO_ID = 35;
    public static final int FLD_TOTAL_ONGKOS = 36;
    public static final int FLD_TOTAL_HE = 37;
    public static final int FLD_ID_BILL_MAIN_SALES_ORDER = 38;
    public static final int FLD_NOMOR_BC = 39;
    public static final int FLD_JENIS_DOKUMEN = 40;
    public static final int FLD_TANGGAL_BC = 41;
    public static final int FLD_LOCATION_PABEAN = 42;
    
    public static final String[] fieldNames = {
        "RECEIVE_MATERIAL_ID",
        "LOCATION_ID",
        "RECEIVE_FROM",
        "LOCATION_TYPE",
        "RECEIVE_DATE",
        "REC_CODE",
        "REC_CODE_CNT",
        "RECEIVE_STATUS",
        "RECEIVE_SOURCE",
        "SUPPLIER_ID",
        "PURCHASE_ORDER_ID",
        "DISPATCH_MATERIAL_ID",
        "RETURN_MATERIAL_ID",
        "REMARK",
        "INVOICE_SUPPLIER",
        "TOTAL_PPN",
        "TRANSFER_STATUS",
        "REASON",
        "TERM_OF_PAYMENT",
        "CREDIT_TIME",
        "EXPIRED_DATE",
        "DISCOUNT",
        "LAST_UPDATE",
        "CURRENCY_ID",
        "TRANS_RATE",
        "INCLUDE_PPN",
        "RECIEVE_ITEM_TYPE",
        "RECEIVE_TYPE",
        "HELL",
        "NILAI_TUKAR",
        "BERAT",
        "TOTAL_NOTA",
        "KEPEMILIKAN_ID",
        "BERAT24K",
        "GONDOLA_ID",
        "GONDOLA_TO_ID",
        "TOTAL_ONGKOS",
        "TOTAL_HE",
        "ID_BILL_MAIN_SALES_ORDER",
        "NOMOR_BC",
        "JENIS_DOKUMEN",
        "TANGGAL_BC",
        "LOCATION_PABEAN"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_DATE,
        TYPE_FLOAT,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_LONG
    };

    public static final int SOURCE_FROM_SUPPLIER = 0;
    public static final int SOURCE_FROM_SUPPLIER_PO = 1;
    public static final int SOURCE_FROM_DISPATCH = 2;
    public static final int SOURCE_FROM_RETURN = 3;

    //Pengiriman Transfer Unit
    public static final int SOURCE_FROM_DISPATCH_UNIT = 4;

    //added by dewok 2018-05-15 for litama
    public static final int SOURCE_FROM_BUYBACK = 5;

    //added by dewok 20191004 for duty free
    public static final int SOURCE_FROM_RECEIVE_BACK = 6;

    public static String[][] strReceiveSourceList = {
        {"Supplier tanpa PO", "PO Supplier", "Pengiriman", "Pengembalian", "Pengiriman Unit", "Beli Kembali", "Penerimaan Kembali"},
        {"Supplier w/o PO", "Purchase Order", "Dispatch", "Return", "Dispatch Unit", "Buy Back", "Receive Back"}
    };

    public static final int EXCLUDE_PPN = 0;
    public static final int INCLUDE_PPN = 1;

    public static final String requiredNames[][] = {
        {"Exclude Ppn", "Include Ppn"},
        {"Exclude Ppn", "Include Ppn"}
    };

    // Receive Payment Type
    public static final int REC_PAYMENT_TYPE_CASH = 0;
    public static final int REC_PAYMENT_TYPE_CREDIT = 1;
    public static final String[] fieldsPaymentType
            = {
                "Cash",
                "Credit"
            };

    public static Vector getReceiveSourceType(int language) {
        Vector result = new Vector(1, 1);
        for (int i = 0; i < strReceiveSourceList[0].length; i++) {
            result.add(strReceiveSourceList[language][i]);
        }
        return result;
    }

    // DI PAKAI UNTUK ALASAN RECEIVE
    public static final int REASON_BONUS = 0;
    public static final int REASON_TUKAR_GULING = 1;
    public static final int REASON_ORDERING = 2;

    /**
     * gadnyana array untuk ALASAN penerimaan barang
     */
    public static String[][] strReason = {
        {"BONUS", "TUKAR GULING", "BELI"},
        {"BONUS", "BARTER", "ORDER"}
    };

    /**
     * gadnyana untuk get semua alasan penerimaan
     *
     * @param language
     * @return vector of reason
     */
    public static Vector getReceiveReason(int language) {
        Vector result = new Vector(1, 1);
        for (int i = 0; i < strReason[0].length; i++) {
            result.add(strReason[language][i]);
        }
        return result;
    }

    public PstMatReceive() {
    }

    public PstMatReceive(int i) throws DBException {
        super(new PstMatReceive());
    }

    public PstMatReceive(String sOid) throws DBException {
        super(new PstMatReceive(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstMatReceive(long lOid) throws DBException {
        super(new PstMatReceive(0));
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
        return TBL_MAT_RECEIVE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMatReceive().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        MatReceive matreceive = fetchExc(ent.getOID());
        ent = (Entity) matreceive;
        return matreceive.getOID();
    }

    synchronized public long insertExc(Entity ent) throws Exception {
        return insertExc((MatReceive) ent);
    }

    synchronized public long updateExc(Entity ent) throws Exception {
        return updateExc((MatReceive) ent);
    }

    synchronized public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static MatReceive fetchExc(long oid) throws DBException {
        try {
            MatReceive matreceive = new MatReceive();
            PstMatReceive pstMatReceive = new PstMatReceive(oid);
            matreceive.setOID(oid);

            matreceive.setLocationId(pstMatReceive.getlong(FLD_LOCATION_ID));
            matreceive.setReceiveFrom(pstMatReceive.getlong(FLD_RECEIVE_FROM));
            matreceive.setLocationType(pstMatReceive.getInt(FLD_LOCATION_TYPE));
            matreceive.setReceiveDate(pstMatReceive.getDate(FLD_RECEIVE_DATE));
            matreceive.setRecCode(pstMatReceive.getString(FLD_REC_CODE));
            matreceive.setRecCodeCnt(pstMatReceive.getInt(FLD_REC_CODE_CNT));
            matreceive.setReceiveStatus(pstMatReceive.getInt(FLD_RECEIVE_STATUS));
            matreceive.setReceiveSource(pstMatReceive.getInt(FLD_RECEIVE_SOURCE));
            matreceive.setSupplierId(pstMatReceive.getlong(FLD_SUPPLIER_ID));
            matreceive.setPurchaseOrderId(pstMatReceive.getlong(FLD_PURCHASE_ORDER_ID));
            matreceive.setDispatchMaterialId(pstMatReceive.getlong(FLD_DISPATCH_MATERIAL_ID));
            matreceive.setReturnMaterialId(pstMatReceive.getlong(FLD_RETURN_MATERIAL_ID));
            matreceive.setRemark(pstMatReceive.getString(FLD_REMARK));
            matreceive.setInvoiceSupplier(pstMatReceive.getString(FLD_INVOICE_SUPPLIER));
            matreceive.setTotalPpn(pstMatReceive.getdouble(FLD_TOTAL_PPN));
            matreceive.setTransferStatus(pstMatReceive.getInt(FLD_TRANSFER_STATUS));

            // new
            matreceive.setReason(pstMatReceive.getInt(FLD_REASON));
            matreceive.setTermOfPayment(pstMatReceive.getInt(FLD_TERM_OF_PAYMENT));
            matreceive.setCreditTime(pstMatReceive.getInt(FLD_CREDIT_TIME));
            matreceive.setExpiredDate(pstMatReceive.getDate(FLD_EXPIRED_DATE));
            matreceive.setDiscount(pstMatReceive.getdouble(FLD_DISCOUNT));
            matreceive.setLastUpdate(pstMatReceive.getDate(FLD_LAST_UPDATE));
            matreceive.setCurrencyId(pstMatReceive.getlong(FLD_CURRENCY_ID));
            matreceive.setTransRate(pstMatReceive.getdouble(FLD_TRANS_RATE));

            //include ppn
            matreceive.setIncludePpn(pstMatReceive.getInt(FLD_INCLUDE_PPN));
            matreceive.setReceiveItemType(pstMatReceive.getInt(FLD_RECEIVE_ITEM_TYPE));
            matreceive.setReceiveType(pstMatReceive.getInt(FLD_RECEIVE_TYPE));
            matreceive.setHel(pstMatReceive.getdouble(FLD_HEL));
            matreceive.setNilaiTukar(pstMatReceive.getdouble(FLD_NILAI_TUKAR));
            matreceive.setBerat(pstMatReceive.getdouble(FLD_BERAT));
            matreceive.setTotalNota(pstMatReceive.getdouble(FLD_TOTAL_NOTA));
            matreceive.setKepemilikanId(pstMatReceive.getlong(FLD_KEPEMILIKAN_ID));
            matreceive.setBerat24k(pstMatReceive.getdouble(FLD_BERAT24K));

            matreceive.setGondolaId(pstMatReceive.getlong(FLD_GONDOLA_ID));
            matreceive.setGondolaToId(pstMatReceive.getlong(FLD_GONDOLA_TO_ID));

            matreceive.setTotalOngkos(pstMatReceive.getdouble(FLD_TOTAL_ONGKOS));
            matreceive.setTotalHe(pstMatReceive.getdouble(FLD_TOTAL_HE));

            //added by dewok 2018-04-27 for litama
            matreceive.setIdBillMainSalesOrder(pstMatReceive.getlong(FLD_ID_BILL_MAIN_SALES_ORDER));

            matreceive.setNomorBc(pstMatReceive.getString(FLD_NOMOR_BC));
			matreceive.setJenisDokumen(pstMatReceive.getString(FLD_JENIS_DOKUMEN));
			matreceive.setTglBc(pstMatReceive.getDate(FLD_TANGGAL_BC));
            matreceive.setLocationPabean(pstMatReceive.getlong(FLD_LOCATION_PABEAN));

            return matreceive;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatReceive(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(MatReceive matreceive) throws DBException {
        try {
            PstMatReceive pstMatReceive = new PstMatReceive(0);

            pstMatReceive.setLong(FLD_LOCATION_ID, matreceive.getLocationId());
            pstMatReceive.setLong(FLD_RECEIVE_FROM, matreceive.getReceiveFrom());
            pstMatReceive.setInt(FLD_LOCATION_TYPE, matreceive.getLocationType());

            pstMatReceive.setDate(FLD_RECEIVE_DATE, matreceive.getReceiveDate());

            pstMatReceive.setString(FLD_REC_CODE, matreceive.getRecCode());
            pstMatReceive.setInt(FLD_REC_CODE_CNT, matreceive.getRecCodeCnt());
            pstMatReceive.setInt(FLD_RECEIVE_STATUS, matreceive.getReceiveStatus());
            pstMatReceive.setInt(FLD_RECEIVE_SOURCE, matreceive.getReceiveSource());
            pstMatReceive.setLong(FLD_SUPPLIER_ID, matreceive.getSupplierId());
            pstMatReceive.setLong(FLD_PURCHASE_ORDER_ID, matreceive.getPurchaseOrderId());
            pstMatReceive.setLong(FLD_DISPATCH_MATERIAL_ID, matreceive.getDispatchMaterialId());
            pstMatReceive.setLong(FLD_RETURN_MATERIAL_ID, matreceive.getReturnMaterialId());
            pstMatReceive.setString(FLD_REMARK, matreceive.getRemark());
            pstMatReceive.setString(FLD_INVOICE_SUPPLIER, matreceive.getInvoiceSupplier());
            pstMatReceive.setDouble(FLD_TOTAL_PPN, matreceive.getTotalPpn());
            pstMatReceive.setInt(FLD_TRANSFER_STATUS, matreceive.getTransferStatus());
            // new
            pstMatReceive.setInt(FLD_REASON, matreceive.getReason());
            pstMatReceive.setInt(FLD_CREDIT_TIME, matreceive.getCreditTime());
            pstMatReceive.setDate(FLD_EXPIRED_DATE, matreceive.getExpiredDate());
            pstMatReceive.setInt(FLD_TERM_OF_PAYMENT, matreceive.getTermOfPayment());
            pstMatReceive.setDouble(FLD_DISCOUNT, matreceive.getDiscount());
            pstMatReceive.setDate(FLD_LAST_UPDATE, matreceive.getLastUpdate());
            pstMatReceive.setLong(FLD_CURRENCY_ID, matreceive.getCurrencyId());
            pstMatReceive.setDouble(FLD_TRANS_RATE, matreceive.getTransRate());

            //include ppn
            pstMatReceive.setInt(FLD_INCLUDE_PPN, matreceive.getIncludePpn());
            pstMatReceive.setInt(FLD_RECEIVE_ITEM_TYPE, matreceive.getReceiveItemType());
            pstMatReceive.setInt(FLD_RECEIVE_TYPE, matreceive.getReceiveType());
            pstMatReceive.setDouble(FLD_HEL, matreceive.getHel());
            pstMatReceive.setDouble(FLD_NILAI_TUKAR, matreceive.getNilaiTukar());
            pstMatReceive.setDouble(FLD_BERAT, matreceive.getBerat());
            pstMatReceive.setDouble(FLD_TOTAL_NOTA, matreceive.getTotalNota());

            pstMatReceive.setLong(FLD_KEPEMILIKAN_ID, matreceive.getKepemilikanId());
            pstMatReceive.setDouble(FLD_BERAT24K, matreceive.getBerat24k());

            pstMatReceive.setLong(FLD_GONDOLA_ID, matreceive.getGondolaId());
            pstMatReceive.setLong(FLD_GONDOLA_TO_ID, matreceive.getGondolaToId());

            pstMatReceive.setDouble(FLD_TOTAL_ONGKOS, matreceive.getTotalOngkos());
            pstMatReceive.setDouble(FLD_TOTAL_HE, matreceive.getTotalHe());

            //added by dewok 2018-04-27 for litama
            pstMatReceive.setLong(FLD_ID_BILL_MAIN_SALES_ORDER, matreceive.getIdBillMainSalesOrder());

            pstMatReceive.setString(FLD_NOMOR_BC, matreceive.getNomorBc());
			pstMatReceive.setString(FLD_JENIS_DOKUMEN, matreceive.getJenisDokumen());
			pstMatReceive.setDate(FLD_TANGGAL_BC, matreceive.getTglBc());
            pstMatReceive.setLong(FLD_LOCATION_PABEAN, matreceive.getLocationPabean());

            pstMatReceive.insert();
            matreceive.setOID(pstMatReceive.getlong(FLD_RECEIVE_MATERIAL_ID));
            //kebutuhan untuk service transfer katalog
            PstDataCustom.insertDataForSyncAllLocation(pstMatReceive.getInsertSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatReceive(0), DBException.UNKNOWN);
        }
        return matreceive.getOID();
    }

    public static long updateExc(MatReceive matreceive) throws DBException {
        try {
            if (matreceive.getOID() != 0) {
                PstMatReceive pstMatReceive = new PstMatReceive(matreceive.getOID());

                pstMatReceive.setLong(FLD_LOCATION_ID, matreceive.getLocationId());
                pstMatReceive.setLong(FLD_RECEIVE_FROM, matreceive.getReceiveFrom());
                pstMatReceive.setInt(FLD_LOCATION_TYPE, matreceive.getLocationType());
                pstMatReceive.setDate(FLD_RECEIVE_DATE, matreceive.getReceiveDate());
                pstMatReceive.setString(FLD_REC_CODE, matreceive.getRecCode());
                pstMatReceive.setInt(FLD_REC_CODE_CNT, matreceive.getRecCodeCnt());
                pstMatReceive.setInt(FLD_RECEIVE_STATUS, matreceive.getReceiveStatus());
                pstMatReceive.setInt(FLD_RECEIVE_SOURCE, matreceive.getReceiveSource());
                pstMatReceive.setLong(FLD_SUPPLIER_ID, matreceive.getSupplierId());
                pstMatReceive.setLong(FLD_PURCHASE_ORDER_ID, matreceive.getPurchaseOrderId());
                pstMatReceive.setLong(FLD_DISPATCH_MATERIAL_ID, matreceive.getDispatchMaterialId());
                pstMatReceive.setLong(FLD_RETURN_MATERIAL_ID, matreceive.getReturnMaterialId());
                pstMatReceive.setString(FLD_REMARK, matreceive.getRemark());
                pstMatReceive.setString(FLD_INVOICE_SUPPLIER, matreceive.getInvoiceSupplier());
                pstMatReceive.setDouble(FLD_TOTAL_PPN, matreceive.getTotalPpn());
                pstMatReceive.setInt(FLD_TRANSFER_STATUS, matreceive.getTransferStatus());

                // new
                pstMatReceive.setInt(FLD_REASON, matreceive.getReason());
                pstMatReceive.setInt(FLD_CREDIT_TIME, matreceive.getCreditTime());
                pstMatReceive.setDate(FLD_EXPIRED_DATE, matreceive.getExpiredDate());
                pstMatReceive.setInt(FLD_TERM_OF_PAYMENT, matreceive.getTermOfPayment());
                pstMatReceive.setDouble(FLD_DISCOUNT, matreceive.getDiscount());
                pstMatReceive.setDate(FLD_LAST_UPDATE, matreceive.getLastUpdate());
                pstMatReceive.setLong(FLD_CURRENCY_ID, matreceive.getCurrencyId());
                pstMatReceive.setDouble(FLD_TRANS_RATE, matreceive.getTransRate());

                //inlude ppn
                pstMatReceive.setInt(FLD_INCLUDE_PPN, matreceive.getIncludePpn());
                pstMatReceive.setInt(FLD_RECEIVE_ITEM_TYPE, matreceive.getReceiveItemType());
                pstMatReceive.setInt(FLD_RECEIVE_TYPE, matreceive.getReceiveType());
                pstMatReceive.setDouble(FLD_HEL, matreceive.getHel());
                pstMatReceive.setDouble(FLD_NILAI_TUKAR, matreceive.getNilaiTukar());
                pstMatReceive.setDouble(FLD_BERAT, matreceive.getBerat());
                pstMatReceive.setDouble(FLD_TOTAL_NOTA, matreceive.getTotalNota());
                pstMatReceive.setLong(FLD_KEPEMILIKAN_ID, matreceive.getKepemilikanId());
                pstMatReceive.setDouble(FLD_BERAT24K, matreceive.getBerat24k());

                pstMatReceive.setLong(FLD_GONDOLA_ID, matreceive.getGondolaId());
                pstMatReceive.setLong(FLD_GONDOLA_TO_ID, matreceive.getGondolaToId());

                pstMatReceive.setDouble(FLD_TOTAL_ONGKOS, matreceive.getTotalOngkos());
                pstMatReceive.setDouble(FLD_TOTAL_HE, matreceive.getTotalHe());

                //added by dewok 2018-04-27 for litama
                pstMatReceive.setLong(FLD_ID_BILL_MAIN_SALES_ORDER, matreceive.getIdBillMainSalesOrder());

                pstMatReceive.setString(FLD_NOMOR_BC, matreceive.getNomorBc());
				pstMatReceive.setString(FLD_JENIS_DOKUMEN, matreceive.getJenisDokumen());
				pstMatReceive.setDate(FLD_TANGGAL_BC, matreceive.getTglBc());
                pstMatReceive.setLong(FLD_LOCATION_PABEAN, matreceive.getLocationPabean());

                pstMatReceive.update();
                //kebutuhan untuk service transfer katalog
                PstDataCustom.insertDataForSyncAllLocation(pstMatReceive.getUpdateSQL());
                return matreceive.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatReceive(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstMatReceive pstMatReceive = new PstMatReceive(oid);
            //Delete Item First
            PstMatReceiveItem pstRecItem = new PstMatReceiveItem();
            long result = pstRecItem.deleteExcByParent(oid);
            pstMatReceive.delete();
            //kebutuhan untuk service transfer katalog
            PstDataCustom.insertDataForSyncAllLocation(pstMatReceive.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatReceive(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_RECEIVE;
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
                    ;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MatReceive matreceive = new MatReceive();
                resultToObject(rs, matreceive);
                lists.add(matreceive);
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

    //list anti same data for consignment doc
    public static Vector listCompare(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_RECEIVE + " AS prm "
                    + " INNER JOIN " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM + " AS prmi "
                    + " ON prm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] + " = prmi." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID];
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
                    ;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MatReceive matreceive = new MatReceive();
                resultToObject(rs, matreceive);
                lists.add(matreceive);
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

    //getListBySupplier
    public static Vector listBySupplier(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT REC." + fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]
                    + " ,REC." + fieldNames[PstMatReceive.FLD_REC_CODE]
                    + " ,REC." + fieldNames[PstMatReceive.FLD_RECEIVE_DATE]
                    + " ,CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]
                    + " ,REC." + fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]
                    + " ,REC." + fieldNames[PstMatReceive.FLD_REMARK]
                    + " ,REC." + fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE]
                    + " ,REC." + fieldNames[PstMatReceive.FLD_REASON]
                    + " ,REC." + fieldNames[PstMatReceive.FLD_CURRENCY_ID]
                    + " ,CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]
                    + " ,PO." + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCHASE_ORDER_ID]
                    + " ,PO." + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE]
                    + " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " REC"
                    + " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT"
                    + " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID]
                    + " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
                    + " LEFT JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CT"
                    + " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_CURRENCY_ID]
                    + " = CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]
                    + " LEFT JOIN " + PstPurchaseOrder.TBL_PURCHASE_ORDER + " PO "
                    + " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_PURCHASE_ORDER_ID]
                    + " = PO." + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCHASE_ORDER_ID];

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
                    ;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vect = new Vector(1, 1);
                MatReceive matreceive = new MatReceive();
                ContactList contactList = new ContactList();
                CurrencyType currencyType = new CurrencyType();
                PurchaseOrder purchaseOrder = new PurchaseOrder();

                matreceive.setOID(rs.getLong(1));
                matreceive.setRecCode(rs.getString(2));
                matreceive.setReceiveDate(rs.getDate(3));
                matreceive.setReceiveStatus(rs.getInt(5));
                matreceive.setRemark(rs.getString(6));
                matreceive.setReceiveSource(rs.getInt(7));
                matreceive.setReason(rs.getInt(8));
                matreceive.setCurrencyId(rs.getLong(9));
                vect.add(matreceive);

                contactList.setCompName(rs.getString(4));
                vect.add(contactList);

                currencyType.setCode(rs.getString("CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]));
                vect.add(currencyType);

                purchaseOrder.setOID(rs.getLong("PO." + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCHASE_ORDER_ID]));
                purchaseOrder.setPoCode(rs.getString("PO." + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE]));
                vect.add(purchaseOrder);

                lists.add(vect);
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

    public static void resultToObject(ResultSet rs, MatReceive matreceive) {
        try {
            matreceive.setOID(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]));
            matreceive.setLocationId(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID]));
            matreceive.setReceiveFrom(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM]));
            matreceive.setLocationType(rs.getInt(PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_TYPE]));
            matreceive.setReceiveDate(rs.getDate(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]));
            matreceive.setRecCode(rs.getString(PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE]));
            matreceive.setRecCodeCnt(rs.getInt(PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE_CNT]));
            matreceive.setReceiveStatus(rs.getInt(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]));
            matreceive.setReceiveSource(rs.getInt(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE]));
            matreceive.setSupplierId(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID]));
            matreceive.setPurchaseOrderId(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_PURCHASE_ORDER_ID]));
            matreceive.setDispatchMaterialId(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_DISPATCH_MATERIAL_ID]));
            matreceive.setReturnMaterialId(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_RETURN_MATERIAL_ID]));
            matreceive.setRemark(rs.getString(PstMatReceive.fieldNames[PstMatReceive.FLD_REMARK]));
            matreceive.setInvoiceSupplier(rs.getString(PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER]));
            matreceive.setTotalPpn(rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_PPN]));
            matreceive.setTransferStatus(rs.getInt(PstMatReceive.fieldNames[PstMatReceive.FLD_TRANSFER_STATUS]));
            // new
            matreceive.setReason(rs.getInt(PstMatReceive.fieldNames[PstMatReceive.FLD_REASON]));
            matreceive.setCreditTime(rs.getInt(PstMatReceive.fieldNames[PstMatReceive.FLD_CREDIT_TIME]));
            matreceive.setDiscount(rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_DISCOUNT]));
            matreceive.setExpiredDate(rs.getDate(PstMatReceive.fieldNames[PstMatReceive.FLD_EXPIRED_DATE]));
            matreceive.setTermOfPayment(rs.getInt(PstMatReceive.fieldNames[PstMatReceive.FLD_TERM_OF_PAYMENT]));
            matreceive.setCurrencyId(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_CURRENCY_ID]));
            matreceive.setTransRate(rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE]));

            matreceive.setIncludePpn(rs.getInt(PstMatReceive.fieldNames[PstMatReceive.FLD_INCLUDE_PPN]));
            matreceive.setReceiveItemType(rs.getInt(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_ITEM_TYPE]));
            matreceive.setReceiveType(rs.getInt(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_TYPE]));
            matreceive.setHel(rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_HEL]));
            matreceive.setNilaiTukar(rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_NILAI_TUKAR]));
            matreceive.setBerat(rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_BERAT]));
            matreceive.setTotalNota(rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_NOTA]));

            matreceive.setKepemilikanId(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_KEPEMILIKAN_ID]));
            matreceive.setBerat24k(rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_BERAT24K]));

            matreceive.setGondolaId(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_GONDOLA_ID]));
            matreceive.setGondolaToId(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_GONDOLA_TO_ID]));

            matreceive.setTotalOngkos(rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_ONGKOS]));
            matreceive.setTotalHe(rs.getDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_HE]));

            //added by dewok 2018-04-27 for litama
            matreceive.setIdBillMainSalesOrder(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_ID_BILL_MAIN_SALES_ORDER]));

            matreceive.setNomorBc(rs.getString(PstMatReceive.fieldNames[PstMatReceive.FLD_NOMOR_BC]));
			matreceive.setJenisDokumen(rs.getString(PstMatReceive.fieldNames[PstMatReceive.FLD_JENIS_DOKUMEN]));
			matreceive.setTglBc(rs.getDate(PstMatReceive.fieldNames[PstMatReceive.FLD_TANGGAL_BC]));
            matreceive.setLocationPabean(rs.getLong(PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_PABEAN]));

            Date dates = DBHandler.convertDate(rs.getDate(PstMatReceive.fieldNames[PstMatDispatch.FLD_LAST_UPDATE]), rs.getTime(PstMatReceive.fieldNames[PstMatDispatch.FLD_LAST_UPDATE]));
            matreceive.setLastUpdate(dates);

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long matReceiveId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MAT_RECEIVE + " WHERE "
                    + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] + " = " + matReceiveId;

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
            String sql = "SELECT COUNT(" + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] + ") FROM " + TBL_MAT_RECEIVE;
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

    //getCountListBySupplier
    public static int getCountListBySupplier(String whereClause) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] + ") AS CNT"
                    + " FROM " + PstMatReceive.TBL_MAT_RECEIVE + " REC"
                    + " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CNT"
                    + " ON REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID]
                    + " = CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count2 = 0;
            while (rs.next()) {
                count2 = rs.getInt(1);
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
                    MatReceive matreceive = (MatReceive) list.get(ls);
                    if (oid == matreceive.getOID()) {
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
     * this method used to get number/code of 'document' return String of
     * number/document code
     */
    /*public String getDocumentNumber(long documentId){
     DBResultSet dbrs = null;
     try{
     MatReceive matReceive = fetchExc(documentId);
     return matReceive.getRecCode();
     }catch(Exception e){
     System.out.println("Err : "+e.toString());
     }finally{
     DBResultSet.close(dbrs);
     }
     return "";
     }*/
    /**
     * this method used to get status of 'document' return int of
     * currentDocumentStatus
     */
    public int getDocumentStatus(long documentId) {
        DBResultSet dbrs = null;
        try {
            MatReceive matReceive = fetchExc(documentId);
            return matReceive.getReceiveStatus();
        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return -1;
    }

    /**
     * this method used to get oid Receive Material return long of
     * currentDocumentStatus
     */
    public static long getOidReceiveMaterial(long oidMatDispatch) {
        DBResultSet dbrs = null;
        long hasil = 0;
        try {
            String sql = "SELECT " + fieldNames[FLD_RECEIVE_MATERIAL_ID]
                    + " FROM " + TBL_MAT_RECEIVE
                    + " WHERE " + fieldNames[FLD_DISPATCH_MATERIAL_ID] + "=" + oidMatDispatch;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            long oid = 0;
            while (rs.next()) {
                oid = rs.getLong(1);
            }

            rs.close();
            hasil = oid;
        } catch (Exception e) {
            hasil = 0;
        } finally {
            DBResultSet.close(dbrs);
        }
        return hasil;
    }

    /**
     * this method used to set status of 'document' return int of
     * currentDocumentStatus
     */
    public int setDocumentStatus(long documentId, int indexStatus) {
        /*DBResultSet dbrs = null;
         try
         {
         String sql = "UPDATE " + PstMatReceive.TBL_MAT_RECEIVE +
         " SET " + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] + " = " + indexStatus +
         " WHERE " + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID] + " = " + documentId;
         dbrs = DBHandler.execQueryResult(sql);*/
        return indexStatus;
        /*}
         catch(Exception e)
         {
         System.out.println("Err : "+e.toString());
         }
         finally
         {
         DBResultSet.close(dbrs);
         }
         return -1;*/
    }

    public static long AutoInsertReceive(Vector listItem, long oidLocation, long oidSupplier,
            int locType, long receiveFrom, int receiveSource) {
        long hasil = 0;
        try {
            MatReceive myrec = new MatReceive();
            myrec.setLocationId(oidLocation);
            myrec.setLocationType(locType);
            myrec.setReceiveFrom(receiveFrom);
            myrec.setSupplierId(oidSupplier);
            myrec.setReceiveDate(new Date());
            myrec.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
            myrec.setReceiveSource(receiveSource);
            myrec.setRemark("");
            myrec.setRecCodeCnt(SessMatReceive.getIntCode(myrec, new Date(), 0, 1));
            myrec.setRecCode(SessMatReceive.getCodeReceive(myrec));
            //Insert return
            hasil = PstMatReceive.insertExc(myrec);
            if (hasil != 0) {
                //Insert return item
                for (int i = 0; i < listItem.size(); i++) {
                    long oidRECItem = 0;
                    Vector temp = (Vector) listItem.get(i);
                    String sku = (String) temp.get(0);
                    if (sku.length() > 0) {
                        //Fetch material info
                        Material myMat = PstMaterial.fetchBySku(sku);
                        boolean masuk = false;
                        //Cek supplier utk material tsb (toSupplier), jika tidak sama lewati saja
                        if (myMat.getSupplierId() == oidSupplier) {
                            masuk = true;
                        }
                        //Cek apakah return to Warehouse
                        if ((oidSupplier == 0) && (receiveFrom != 0)) {
                            masuk = true;
                        }
                        if (masuk == true) {
                            int quantity = Integer.parseInt((String) temp.get(1));
                            MatReceiveItem recItem = new MatReceiveItem();
                            recItem.setReceiveMaterialId(hasil);
                            recItem.setMaterialId(myMat.getOID());
                            recItem.setUnitId(myMat.getDefaultStockUnitId());
                            recItem.setQty(quantity);
                            recItem.setCost(myMat.getDefaultCost());
                            recItem.setCurrencyId(myMat.getDefaultCostCurrencyId());
                            recItem.setTotal(quantity * myMat.getDefaultCost());
                            oidRECItem = PstMatReceiveItem.insertExc(recItem);
                        } else {
                            oidRECItem = 1;
                        }
                    } else {
                        oidRECItem = 1;
                    }
                    if (oidRECItem == 0) {
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("AutoInsertReceive : " + ex);
        }
        return hasil;
    }

    public static long AutoInsertReceivePO(long oidPurchaseOrder, int receiveSource, int locType) {
        long hasil = 0;
        Vector listItem = new Vector();
        try {
            PurchaseOrder po = PstPurchaseOrder.fetchExc(oidPurchaseOrder);
            MatReceive myrec = new MatReceive();
            myrec.setLocationId(po.getLocationId());
            myrec.setLocationType(locType);
            myrec.setReceiveFrom(0);
            myrec.setSupplierId(po.getSupplierId());
            myrec.setReceiveDate(new Date());
            myrec.setPurchaseOrderId(oidPurchaseOrder);
            myrec.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
            myrec.setReceiveSource(receiveSource);
            myrec.setRemark("");
            myrec.setRecCodeCnt(SessMatReceive.getIntCode(myrec, new Date(), 0, 1));
            myrec.setRecCode(SessMatReceive.getCodeReceive(myrec));
            //Insert return
            hasil = PstMatReceive.insertExc(myrec);
            if (hasil != 0) {
                String whereClause = PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]
                        + " = " + oidPurchaseOrder;
                String orderClause = PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ITEM_ID];
                listItem = PstPurchaseOrderItem.list(0, 0, whereClause, orderClause);
                //Insert return item
                for (int i = 0; i < listItem.size(); i++) {
                    long oidRECItem = 0;
                    //Fetch material info
                    PurchaseOrderItem poi = (PurchaseOrderItem) listItem.get(i);

                    MatReceiveItem recItem = new MatReceiveItem();
                    recItem.setReceiveMaterialId(hasil);
                    recItem.setMaterialId(poi.getMaterialId());
                    recItem.setUnitId(poi.getUnitId());
                    recItem.setQty(poi.getQuantity());
                    recItem.setExpiredDate(new Date());
                    recItem.setCost(poi.getPrice());
                    recItem.setCurrencyId(poi.getCurrencyId());
                    recItem.setTotal(poi.getQuantity() * poi.getPrice());
                    oidRECItem = PstMatReceiveItem.insertExc(recItem);
                    if (oidRECItem == 0) {
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("AutoInsertReceivePO : " + ex);
        }
        return hasil;
    }

    public static long AutoInsertReceiveDF(long oidDispatchMaterial, int receiveSource, int locType) {
        long hasil = 0;
        Vector listItem = new Vector();
        try {
            MatDispatch df = PstMatDispatch.fetchExc(oidDispatchMaterial);
            MatReceive myrec = new MatReceive();
            myrec.setLocationId(df.getDispatchTo());
            myrec.setLocationType(locType);
            myrec.setReceiveFrom(df.getLocationId());
            myrec.setSupplierId(0);
            myrec.setReceiveDate(new Date());
            myrec.setDispatchMaterialId(oidDispatchMaterial);
            myrec.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
            myrec.setReceiveSource(receiveSource);
            myrec.setRemark("");
            myrec.setRecCodeCnt(SessMatReceive.getIntCode(myrec, new Date(), 0, 1));
            myrec.setRecCode(SessMatReceive.getCodeReceive(myrec));
            //Insert receive
            hasil = PstMatReceive.insertExc(myrec);
            if (hasil != 0) {
                String whereClause = PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID]
                        + " = " + oidDispatchMaterial;
                String orderClause = PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID];
                listItem = PstMatDispatchItem.list(whereClause, orderClause);
                //Insert receive item
                for (int i = 0; i < listItem.size(); i++) {
                    long oidRECItem = 0;
                    //Fetch material info
                    Vector temp = (Vector) listItem.get(i);
                    MatDispatchItem dfi = (MatDispatchItem) temp.get(0);
                    Material mat = (Material) temp.get(1);

                    MatReceiveItem recItem = new MatReceiveItem();
                    recItem.setReceiveMaterialId(hasil);
                    recItem.setMaterialId(dfi.getMaterialId());
                    recItem.setUnitId(dfi.getUnitId());
                    recItem.setQty(dfi.getQty());
                    recItem.setExpiredDate(new Date());
                    recItem.setCost(mat.getDefaultCost());
                    recItem.setCurrencyId(mat.getDefaultCostCurrencyId());
                    recItem.setTotal(dfi.getQty() * mat.getDefaultCost());
                    oidRECItem = PstMatReceiveItem.insertExc(recItem);
                    if (oidRECItem == 0) {
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("AutoInsertReceivePO : " + ex);
        }
        return hasil;
    }

    public static long AutoInsertReceiveRET(long oidReturnMaterial, int receiveSource, int locType) {
        long hasil = 0;
        Vector listItem = new Vector();
        try {
            MatReturn ret = PstMatReturn.fetchExc(oidReturnMaterial);
            MatReceive myrec = new MatReceive();
            myrec.setLocationId(ret.getReturnTo());
            myrec.setLocationType(locType);
            myrec.setReceiveFrom(ret.getLocationId());
            myrec.setSupplierId(0);
            myrec.setReceiveDate(new Date());
            myrec.setReturnMaterialId(oidReturnMaterial);
            myrec.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
            myrec.setReceiveSource(receiveSource);
            myrec.setRemark("");
            myrec.setRecCodeCnt(SessMatReceive.getIntCode(myrec, new Date(), 0, 1));
            myrec.setRecCode(SessMatReceive.getCodeReceive(myrec));
            //Insert return
            hasil = PstMatReceive.insertExc(myrec);
            if (hasil != 0) {
                String whereClause = PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID]
                        + " = " + oidReturnMaterial;
                listItem = PstMatReturnItem.list(0, 0, whereClause, "");
                //Insert return item
                for (int i = 0; i < listItem.size(); i++) {
                    long oidRECItem = 0;
                    //Fetch material info
                    MatReturnItem roi = (MatReturnItem) listItem.get(i);

                    MatReceiveItem recItem = new MatReceiveItem();
                    recItem.setReceiveMaterialId(hasil);
                    recItem.setMaterialId(roi.getMaterialId());
                    recItem.setUnitId(roi.getUnitId());
                    recItem.setQty(roi.getQty());
                    recItem.setExpiredDate(new Date());
                    recItem.setCost(roi.getCost());
                    recItem.setCurrencyId(roi.getCurrencyId());
                    recItem.setTotal(roi.getTotal());
                    oidRECItem = PstMatReceiveItem.insertExc(recItem);
                    if (oidRECItem == 0) {
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("AutoInsertReceiveRET : " + ex);
        }
        return hasil;
    }

    /*-------------------- end implements I_Document --------------------------*/
    /**
     * digunakan untuk update status transfer data secara otomatis. jika residue
     * qty item semuanya 0 maka status transfer CLOSED. jika residue qty masih
     * ada yang lebih dari 0 maka status transfer DRAFT.
     *
     * @param oid
     */
    public static void processUpdate(long oid) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RESIDUE_QTY] + ") "
                    + " AS SUM_" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RESIDUE_QTY]
                    + " FROM " + PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM
                    + " WHERE " + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + " = " + oid;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int sumqty = 0;
            while (rs.next()) {
                sumqty = rs.getInt("SUM_" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RESIDUE_QTY]);
            }

            MatReceive matReceive = new MatReceive();
            matReceive = PstMatReceive.fetchExc(oid);
            if (sumqty == 0) {
                matReceive.setTransferStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
            } else {
                matReceive.setTransferStatus(I_DocStatus.DOCUMENT_STATUS_DRAFT);
            }
            // update transfer status
            PstMatReceive.updateExc(matReceive);

        } catch (Exception e) {
        }
    }

    public static Vector getRekapForPurchaseMainFromPr(Vector list) {
        Vector vpo = new Vector();
        long oidNewSupplier = Long.parseLong(PstSystemProperty.getValueByName("MAPPING_NEW_SUPPLIER_ID"));
        try {
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    Vector v1 = (Vector) list.get(k);
                    long oidmaterial = Long.parseLong((String) v1.get(0));

                    long oidUnit = ((Long) v1.get(4)).longValue();

                    long unitRequestId = ((Long) v1.get(12)).longValue();

                    double qtyRequest = Double.parseDouble((String) v1.get(13));

                    double valueKonfersi = PstUnit.getQtyPerBaseUnit(unitRequestId, oidUnit);

                    double totalQty = valueKonfersi * qtyRequest;

                    double qty = totalQty;

                    long oidlokasi = Long.parseLong((String) v1.get(2));

                    long oidsupplier = Long.parseLong((String) v1.get(3));

                    double priceKonv = Double.parseDouble((String) v1.get(7));

                    double price = priceKonv / valueKonfersi;

                    //double totalPrice = Double.parseDouble((String) v1.get(8));
                    int termOfPayment = 0;//Integer.parseInt((String) v1.get(8));

                    //term po
                    int termPO = Integer.parseInt((String) v1.get(14));

                    String nameSupplier = "";
                    if (oidNewSupplier == oidsupplier) {
                        nameSupplier = nameSupplier + " New Supplier : " + String.valueOf((String) v1.get(9));
                    }
                    int status = 0;

                    long currencyId = Long.parseLong((String) v1.get(10));
                    double exchangeRate = Double.parseDouble((String) v1.get(11));

                    //unit request dan unit qty
                    if (vpo.size() > 0) {
                        boolean bool = false;
                        for (int i = 0; i < vpo.size(); i++) {
                            bool = false;
                            PurchaseOrder purchOrder = (PurchaseOrder) vpo.get(i);

                            if ((purchOrder.getLocationId() == oidlokasi) && (purchOrder.getSupplierId() == oidsupplier) && (purchOrder.getTermOfPayment() == termPO)) {
                                bool = true;
                                PurchaseOrderItem poItem = new PurchaseOrderItem();
                                poItem.setMaterialId(oidmaterial);
                                poItem.setQuantity(qty);
                                MatVendorPrice matVendorPrice = PstMatVendorPrice.getVendorObject(oidmaterial, oidsupplier);
                                //purchOrder.setCurrencyId(matVendorPrice.getPriceCurrency());
                                poItem.setUnitId(matVendorPrice.getBuyingUnitId());
                                poItem.setPrice(price);
                                poItem.setOrgBuyingPrice(price);
                                poItem.setCurrencyId(matVendorPrice.getPriceCurrency());
                                poItem.setDiscount(matVendorPrice.getLastDiscount());
                                poItem.setTotal((qty * price) * (1 - (matVendorPrice.getLastDiscount() / 100)));
                                poItem.setCurBuyingPrice(price);
                                poItem.setUnitId(oidUnit);

                                poItem.setUnitRequestId(unitRequestId);
                                poItem.setQtyRequest(qtyRequest);
                                poItem.setPriceKonv(priceKonv);

                                purchOrder.setListItem(poItem);
                                vpo.setElementAt(purchOrder, i);
                            }

                        }
                        if (!bool) {
                            PurchaseOrder mypo = new PurchaseOrder();
                            if (termPO == PstPurchaseOrder.PR_DIRECT) {
                                termOfPayment = PstPurchaseOrder.PO_PAYMENT_TYPE_CASH;//8
                                status = I_DocStatus.DOCUMENT_STATUS_APPROVED;
                            } else if (termPO == PstPurchaseOrder.PR_SUPPLIER_CASH) {
                                termOfPayment = PstPurchaseOrder.PO_PAYMENT_TYPE_CASH;//8
                                status = I_DocStatus.DOCUMENT_STATUS_FINAL;
                            } else {
                                termOfPayment = PstPurchaseOrder.PO_PAYMENT_TYPE_CREDIT;//8
                                status = I_DocStatus.DOCUMENT_STATUS_FINAL;
                            }

                            mypo.setPoStatus(status);
                            mypo.setPurchDate(new Date());
                            mypo.setSupplierId(oidsupplier);
                            mypo.setLocationId(oidlokasi);
                            mypo.setLocationType(PstLocation.TYPE_LOCATION_WAREHOUSE);
                            mypo.setRemark("");
                            mypo.setTermOfPayment(termOfPayment);
                            mypo.setRemark(nameSupplier);

                            //mypo.setPoCodeCounter(SessPurchaseOrder.getIntCode(mypo, new Date(), 0, 0, true));
                            //mypo.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(mypo));
                            PurchaseOrderItem poItem = new PurchaseOrderItem();
                            poItem.setMaterialId(oidmaterial);
                            poItem.setQuantity(qty);

                            MatVendorPrice matVendorPrice = PstMatVendorPrice.getVendorObject(oidmaterial, oidsupplier);

                            if (matVendorPrice.getPriceCurrency() != 0) {
                                mypo.setCurrencyId(matVendorPrice.getPriceCurrency());
                                mypo.setExchangeRate(exchangeRate);
                            } else {
                                mypo.setCurrencyId(currencyId);
                                mypo.setExchangeRate(exchangeRate);
                            }

                            poItem.setUnitId(matVendorPrice.getBuyingUnitId());
                            poItem.setPrice(price);
                            poItem.setOrgBuyingPrice(price);
                            poItem.setCurrencyId(matVendorPrice.getPriceCurrency());
                            poItem.setDiscount(matVendorPrice.getLastDiscount());
                            poItem.setTotal((qty * price) * (1 - (matVendorPrice.getLastDiscount() / 100)));
                            poItem.setCurBuyingPrice(price);
                            poItem.setUnitId(oidUnit);

                            poItem.setUnitRequestId(unitRequestId);
                            poItem.setQtyRequest(qtyRequest);
                            poItem.setPriceKonv(priceKonv);

                            mypo.setListItem(poItem);
                            vpo.add(mypo);
                        }

                    } else {

                        PurchaseOrder mypo = new PurchaseOrder();

                        if (termPO == PstPurchaseOrder.PR_DIRECT) {
                            termOfPayment = PstPurchaseOrder.PO_PAYMENT_TYPE_CASH;//8
                            status = I_DocStatus.DOCUMENT_STATUS_APPROVED;
                        } else if (termPO == PstPurchaseOrder.PR_SUPPLIER_CASH) {
                            termOfPayment = PstPurchaseOrder.PO_PAYMENT_TYPE_CASH;//8
                            status = I_DocStatus.DOCUMENT_STATUS_FINAL;
                        } else {
                            termOfPayment = PstPurchaseOrder.PO_PAYMENT_TYPE_CREDIT;//8
                            status = I_DocStatus.DOCUMENT_STATUS_FINAL;
                        }

                        mypo.setPoStatus(status);
                        mypo.setPurchDate(new Date());
                        mypo.setSupplierId(oidsupplier);
                        mypo.setLocationId(oidlokasi);
                        mypo.setLocationType(PstLocation.TYPE_LOCATION_WAREHOUSE);
                        mypo.setTermOfPayment(termOfPayment);
                        mypo.setRemark(nameSupplier);
                        //mypo.setPoCodeCounter(SessPurchaseOrder.getIntCode(mypo, new Date(), 0, 0, true));
                        //mypo.setPoCode(SessPurchaseOrder.getCodeOrderMaterial(mypo));

                        PurchaseOrderItem poItem = new PurchaseOrderItem();
                        poItem.setMaterialId(oidmaterial);
                        poItem.setQuantity(qty);
                        MatVendorPrice matVendorPrice = PstMatVendorPrice.getVendorObject(oidmaterial, oidsupplier);

                        //mypo.setCurrencyId(matVendorPrice.getPriceCurrency());
                        if (matVendorPrice.getPriceCurrency() != 0) {
                            mypo.setCurrencyId(matVendorPrice.getPriceCurrency());
                            mypo.setExchangeRate(exchangeRate);
                        } else {
                            mypo.setCurrencyId(currencyId);
                            mypo.setExchangeRate(exchangeRate);
                        }

                        poItem.setUnitId(matVendorPrice.getBuyingUnitId());
                        poItem.setPrice(price);
                        poItem.setOrgBuyingPrice(price);
                        poItem.setCurrencyId(matVendorPrice.getPriceCurrency());
                        poItem.setDiscount(matVendorPrice.getLastDiscount());
                        poItem.setTotal((qty * price) * (1 - (matVendorPrice.getLastDiscount() / 100)));
                        poItem.setCurBuyingPrice(price);
                        poItem.setUnitId(oidUnit);

                        poItem.setUnitRequestId(unitRequestId);
                        poItem.setQtyRequest(qtyRequest);
                        poItem.setPriceKonv(priceKonv);

                        mypo.setListItem(poItem);

                        vpo.add(mypo);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getRekapForPurchaseMain : " + e.toString());
        }
        return vpo;
    }
    
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MatReceive matReceive = PstMatReceive.fetchExc(oid);
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID], matReceive.getOID());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID], matReceive.getLocationId());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM], matReceive.getReceiveFrom());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_TYPE], matReceive.getLocationType());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE], matReceive.getReceiveDate());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE], matReceive.getRecCode());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE_CNT], matReceive.getRecCodeCnt());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS], matReceive.getReceiveStatus());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE], matReceive.getReceiveSource());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID], matReceive.getSupplierId());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_PURCHASE_ORDER_ID], matReceive.getPurchaseOrderId());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_DISPATCH_MATERIAL_ID], matReceive.getDispatchMaterialId());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_RETURN_MATERIAL_ID], matReceive.getReturnMaterialId());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_REMARK], matReceive.getRemark());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER], matReceive.getInvoiceSupplier());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_PPN], matReceive.getTotalPpn());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_TRANSFER_STATUS], matReceive.getTransferStatus());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_REASON], matReceive.getReason());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_TERM_OF_PAYMENT], matReceive.getTermOfPayment());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_CREDIT_TIME], matReceive.getCreditTime());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_EXPIRED_DATE], matReceive.getExpiredDate());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_DISCOUNT], matReceive.getDiscount());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_LAST_UPDATE], matReceive.getLastUpdate());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_CURRENCY_ID], matReceive.getCurrencyId());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE], matReceive.getTransRate());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_INCLUDE_PPN], matReceive.getIncludePpn());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_ITEM_TYPE], matReceive.getReceiveItemType());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_TYPE], matReceive.getReceiveType());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_HEL], matReceive.getHel());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_NILAI_TUKAR], matReceive.getNilaiTukar());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_BERAT], matReceive.getBerat());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_NOTA], matReceive.getTotalNota());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_KEPEMILIKAN_ID], matReceive.getKepemilikanId());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_BERAT24K], matReceive.getBerat24k());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_GONDOLA_ID], matReceive.getGondolaId());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_GONDOLA_TO_ID], matReceive.getGondolaToId());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_ONGKOS], matReceive.getTotalOngkos());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_HE], matReceive.getTotalHe());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_ID_BILL_MAIN_SALES_ORDER], matReceive.getIdBillMainSalesOrder());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_NOMOR_BC], matReceive.getNomorBc());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_JENIS_DOKUMEN], matReceive.getJenisDokumen());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_TANGGAL_BC], matReceive.getTglBc());
         object.put(PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_PABEAN], matReceive.getLocationPabean());
      }catch(Exception exc){}
      return object;
   }
   
   public static long syncExc(JSONObject jSONObject){
	   long oid = 0;
	   if (jSONObject != null) {
		   oid = jSONObject.optLong(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID], 0);
		   if (oid > 0) {
			   MatReceive matReceive = new MatReceive();
			   matReceive.setOID(jSONObject.optLong(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID], 0));

			   matReceive.setLocationId(jSONObject.optLong(PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID], 0));
			   matReceive.setReceiveFrom(jSONObject.optLong(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_FROM], 0));
			   matReceive.setLocationType(jSONObject.optInt(PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_TYPE], 0));
			   matReceive.setReceiveDate(Formater.formatDate(jSONObject.optString(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE], "0000-00-00 00:00:00"), "yyyy-MM-dd HH:mm:ss"));
			   matReceive.setRecCode(jSONObject.optString(PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE], ""));
			   matReceive.setRecCodeCnt(jSONObject.optInt(PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE_CNT], 0));
			   matReceive.setReceiveStatus(jSONObject.optInt(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS], 0));
			   matReceive.setReceiveSource(jSONObject.optInt(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE], 0));
			   matReceive.setSupplierId(jSONObject.optLong(PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID], 0));
			   matReceive.setPurchaseOrderId(jSONObject.optLong(PstMatReceive.fieldNames[PstMatReceive.FLD_PURCHASE_ORDER_ID], 0));
			   matReceive.setDispatchMaterialId(jSONObject.optLong(PstMatReceive.fieldNames[PstMatReceive.FLD_DISPATCH_MATERIAL_ID], 0));
			   matReceive.setReturnMaterialId(jSONObject.optLong(PstMatReceive.fieldNames[PstMatReceive.FLD_RETURN_MATERIAL_ID], 0));
			   matReceive.setRemark(jSONObject.optString(PstMatReceive.fieldNames[PstMatReceive.FLD_REMARK], ""));
			   matReceive.setInvoiceSupplier(jSONObject.optString(PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER], ""));
			   matReceive.setTotalPpn(jSONObject.optDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_PPN], 0));
			   matReceive.setTransferStatus(jSONObject.optInt(PstMatReceive.fieldNames[PstMatReceive.FLD_TRANSFER_STATUS], 0));

			   // new
			   matReceive.setReason(jSONObject.optInt(PstMatReceive.fieldNames[PstMatReceive.FLD_REASON], 0));
			   matReceive.setTermOfPayment(jSONObject.optInt(PstMatReceive.fieldNames[PstMatReceive.FLD_TERM_OF_PAYMENT], 0));
			   matReceive.setCreditTime(jSONObject.optInt(PstMatReceive.fieldNames[PstMatReceive.FLD_CREDIT_TIME], 0));
			   matReceive.setExpiredDate(Formater.formatDate(jSONObject.optString(PstMatReceive.fieldNames[PstMatReceive.FLD_EXPIRED_DATE], "0000-00-00"), "yyyy-MM-dd"));
			   matReceive.setDiscount(jSONObject.optDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_DISCOUNT], 0));
			   matReceive.setLastUpdate(Formater.formatDate(jSONObject.optString(PstMatReceive.fieldNames[PstMatReceive.FLD_LAST_UPDATE], "0000-00-00"), "yyyy-MM-dd"));
			   matReceive.setCurrencyId(jSONObject.optLong(PstMatReceive.fieldNames[PstMatReceive.FLD_CURRENCY_ID], 0));
			   matReceive.setTransRate(jSONObject.optDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_TRANS_RATE], 0));

			   //include ppn
			   matReceive.setIncludePpn(jSONObject.optInt(PstMatReceive.fieldNames[PstMatReceive.FLD_INCLUDE_PPN], 0));
			   matReceive.setReceiveItemType(jSONObject.optInt(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_ITEM_TYPE], 0));
			   matReceive.setReceiveType(jSONObject.optInt(PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_TYPE], 0));
			   matReceive.setHel(jSONObject.optDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_HEL], 0));
			   matReceive.setNilaiTukar(jSONObject.optDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_NILAI_TUKAR], 0));
			   matReceive.setBerat(jSONObject.optDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_BERAT], 0));
			   matReceive.setTotalNota(jSONObject.optDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_NOTA], 0));
			   matReceive.setKepemilikanId(jSONObject.optLong(PstMatReceive.fieldNames[PstMatReceive.FLD_KEPEMILIKAN_ID], 0));
			   matReceive.setBerat24k(jSONObject.optDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_BERAT24K], 0));

			   matReceive.setGondolaId(jSONObject.optLong(PstMatReceive.fieldNames[PstMatReceive.FLD_GONDOLA_ID], 0));
			   matReceive.setGondolaToId(jSONObject.optLong(PstMatReceive.fieldNames[PstMatReceive.FLD_GONDOLA_TO_ID], 0));

			   matReceive.setTotalOngkos(jSONObject.optDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_ONGKOS], 0));
			   matReceive.setTotalHe(jSONObject.optDouble(PstMatReceive.fieldNames[PstMatReceive.FLD_TOTAL_HE], 0));

			   //added by dewok 2018-04-27 for litama
			   matReceive.setIdBillMainSalesOrder(jSONObject.optLong(PstMatReceive.fieldNames[PstMatReceive.FLD_ID_BILL_MAIN_SALES_ORDER], 0));

			   matReceive.setNomorBc(jSONObject.optString(PstMatReceive.fieldNames[PstMatReceive.FLD_NOMOR_BC], ""));
			   matReceive.setJenisDokumen(jSONObject.optString(PstMatReceive.fieldNames[PstMatReceive.FLD_JENIS_DOKUMEN], ""));
			   matReceive.setTglBc(Formater.formatDate(jSONObject.optString(PstMatReceive.fieldNames[PstMatReceive.FLD_TANGGAL_BC], "0000-00-00"), "yyyy-MM-dd"));
			   matReceive.setLocationPabean(jSONObject.optLong(PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_PABEAN], 0));

			   boolean checkOidMatReceive = PstMatReceive.checkOID(oid);
			   try {
				   if (checkOidMatReceive) {
					   oid = PstMatReceive.updateExc(matReceive);
				   } else {
					   oid = PstMatReceive.insertByOid(matReceive);
				   }
			   } catch (Exception exc) {
				   oid = 0;
			   }
		   }
	   }
	   return oid;
   }
   
   public static long insertByOid(MatReceive matreceive) throws DBException {
        try {
            PstMatReceive pstMatReceive = new PstMatReceive(0);

            pstMatReceive.setLong(FLD_LOCATION_ID, matreceive.getLocationId());
            pstMatReceive.setLong(FLD_RECEIVE_FROM, matreceive.getReceiveFrom());
            pstMatReceive.setInt(FLD_LOCATION_TYPE, matreceive.getLocationType());

            pstMatReceive.setDate(FLD_RECEIVE_DATE, matreceive.getReceiveDate());

            pstMatReceive.setString(FLD_REC_CODE, matreceive.getRecCode());
            pstMatReceive.setInt(FLD_REC_CODE_CNT, matreceive.getRecCodeCnt());
            pstMatReceive.setInt(FLD_RECEIVE_STATUS, matreceive.getReceiveStatus());
            pstMatReceive.setInt(FLD_RECEIVE_SOURCE, matreceive.getReceiveSource());
            pstMatReceive.setLong(FLD_SUPPLIER_ID, matreceive.getSupplierId());
            pstMatReceive.setLong(FLD_PURCHASE_ORDER_ID, matreceive.getPurchaseOrderId());
            pstMatReceive.setLong(FLD_DISPATCH_MATERIAL_ID, matreceive.getDispatchMaterialId());
            pstMatReceive.setLong(FLD_RETURN_MATERIAL_ID, matreceive.getReturnMaterialId());
            pstMatReceive.setString(FLD_REMARK, matreceive.getRemark());
            pstMatReceive.setString(FLD_INVOICE_SUPPLIER, matreceive.getInvoiceSupplier());
            pstMatReceive.setDouble(FLD_TOTAL_PPN, matreceive.getTotalPpn());
            pstMatReceive.setInt(FLD_TRANSFER_STATUS, matreceive.getTransferStatus());
            // new
            pstMatReceive.setInt(FLD_REASON, matreceive.getReason());
            pstMatReceive.setInt(FLD_CREDIT_TIME, matreceive.getCreditTime());
            pstMatReceive.setDate(FLD_EXPIRED_DATE, matreceive.getExpiredDate());
            pstMatReceive.setInt(FLD_TERM_OF_PAYMENT, matreceive.getTermOfPayment());
            pstMatReceive.setDouble(FLD_DISCOUNT, matreceive.getDiscount());
            pstMatReceive.setDate(FLD_LAST_UPDATE, matreceive.getLastUpdate());
            pstMatReceive.setLong(FLD_CURRENCY_ID, matreceive.getCurrencyId());
            pstMatReceive.setDouble(FLD_TRANS_RATE, matreceive.getTransRate());

            //include ppn
            pstMatReceive.setInt(FLD_INCLUDE_PPN, matreceive.getIncludePpn());
            pstMatReceive.setInt(FLD_RECEIVE_ITEM_TYPE, matreceive.getReceiveItemType());
            pstMatReceive.setInt(FLD_RECEIVE_TYPE, matreceive.getReceiveType());
            pstMatReceive.setDouble(FLD_HEL, matreceive.getHel());
            pstMatReceive.setDouble(FLD_NILAI_TUKAR, matreceive.getNilaiTukar());
            pstMatReceive.setDouble(FLD_BERAT, matreceive.getBerat());
            pstMatReceive.setDouble(FLD_TOTAL_NOTA, matreceive.getTotalNota());

            pstMatReceive.setLong(FLD_KEPEMILIKAN_ID, matreceive.getKepemilikanId());
            pstMatReceive.setDouble(FLD_BERAT24K, matreceive.getBerat24k());

            pstMatReceive.setLong(FLD_GONDOLA_ID, matreceive.getGondolaId());
            pstMatReceive.setLong(FLD_GONDOLA_TO_ID, matreceive.getGondolaToId());

            pstMatReceive.setDouble(FLD_TOTAL_ONGKOS, matreceive.getTotalOngkos());
            pstMatReceive.setDouble(FLD_TOTAL_HE, matreceive.getTotalHe());

            //added by dewok 2018-04-27 for litama
            pstMatReceive.setLong(FLD_ID_BILL_MAIN_SALES_ORDER, matreceive.getIdBillMainSalesOrder());

            pstMatReceive.setString(FLD_NOMOR_BC, matreceive.getNomorBc());
			pstMatReceive.setString(FLD_JENIS_DOKUMEN, matreceive.getJenisDokumen());
			pstMatReceive.setDate(FLD_TANGGAL_BC, matreceive.getTglBc());
            pstMatReceive.setLong(FLD_LOCATION_PABEAN, matreceive.getLocationPabean());

            pstMatReceive.insertByOid(matreceive.getOID());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMatReceive(0), DBException.UNKNOWN);
        }
        return matreceive.getOID();
    }
   
}
