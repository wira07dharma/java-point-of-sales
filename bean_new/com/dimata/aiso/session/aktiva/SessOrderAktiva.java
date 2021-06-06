/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Sep 3, 2005
 * Time: 9:50:44 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.aiso.session.aktiva;

import com.dimata.aiso.entity.search.SrcOrderAktiva;
import com.dimata.aiso.entity.aktiva.PstOrderAktiva;
import com.dimata.aiso.entity.aktiva.OrderAktiva;
import com.dimata.aiso.entity.jurnal.JurnalUmum;
import com.dimata.aiso.entity.jurnal.JurnalDetail;
import com.dimata.aiso.entity.jurnal.PstJurnalDetail;
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.form.jurnal.CtrlJurnalUmum;
import com.dimata.aiso.session.jurnal.SessJurnal;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.contact.ContactList;
import com.dimata.util.Formater;
import com.dimata.interfaces.journal.I_JournalType;

import java.util.Vector;
import java.util.Date;
import java.sql.ResultSet;

public class SessOrderAktiva {
    public static final String SESS_SEARCH_ORDER_AKTIVA = "SESS_SEARCH_ORDER_AKTIVA";

    /** gadnyana
     * untuk menampilkan list aktiva
     * @param srcModulAktiva
     * @param start
     * @param recordToGet
     * @return
     */
    public static Vector listOrderAktiva(SrcOrderAktiva srcOrderAktiva, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_ORDER_AKTIVA_ID] +
                    ", A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_NOMOR_ORDER] +
                    ", A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_TANGGAL_ORDER] +
                    ", A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_ID_PERKIRAAN_DP] +
                    ", A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_DOWN_PAYMENT] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    " FROM " + PstOrderAktiva.TBL_ORDER_AKTIVA + " AS A " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_SUPPLIER_ID];

            String where = "";

            if (srcOrderAktiva.getNomorOrder().length() > 0) {
                where = " A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_NOMOR_ORDER] + " LIKE '%" + srcOrderAktiva.getNomorOrder() + "%'";
            }

            if (srcOrderAktiva.getNameSupplier().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcOrderAktiva.getNameSupplier() + "%'";
                } else {
                    where = " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcOrderAktiva.getNameSupplier() + "%'";
                }
            }

            if (srcOrderAktiva.getType() != 0) {
                if (where.length() > 0) {
                    where = where + " AND A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_TANGGAL_ORDER] + " BETWEEN '" + Formater.formatDate(srcOrderAktiva.getTanggalAwal(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcOrderAktiva.getTanggalakhir(), "yyyy-MM-dd") + "'";
                } else {
                    where = " A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_TANGGAL_ORDER] + " BETWEEN '" + Formater.formatDate(srcOrderAktiva.getTanggalAwal(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcOrderAktiva.getTanggalakhir(), "yyyy-MM-dd") + "'";
                }
            }
            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " ORDER BY A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_NOMOR_ORDER];

            System.out.println(sql);
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + start + "," + recordToGet;
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;

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
                Vector vt = new Vector();
                OrderAktiva orderAktiva = new OrderAktiva();
                orderAktiva.setOID(rs.getLong(PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_ORDER_AKTIVA_ID]));
                orderAktiva.setNomorOrder(rs.getString(PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_NOMOR_ORDER]));
                orderAktiva.setTanggalOrder(rs.getDate(PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_TANGGAL_ORDER]));
                orderAktiva.setIdPerkiraanDp(rs.getLong(PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_ID_PERKIRAAN_DP]));
                orderAktiva.setDownPayment(rs.getDouble(PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_DOWN_PAYMENT]));

                ContactList contactList = new ContactList();
                contactList.setCompName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));

                vt.add(orderAktiva);
                vt.add(contactList);
                list.add(vt);
            }

        } catch (Exception e) {
            System.out.println("err.SessModulAktiva - listModulAktiva " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    /** gadnyana
     * untuk mencari jumlah row di daftar aktiva
     * @param srcOrderAktiva
     * @return
     */
    public static int countModulAktiva(SrcOrderAktiva srcOrderAktiva) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(" +
                    " A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_ORDER_AKTIVA_ID] + ") AS CNT " +
                    " FROM " + PstOrderAktiva.TBL_ORDER_AKTIVA + " AS A " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_SUPPLIER_ID];

            String where = "";
            if (srcOrderAktiva.getNomorOrder().length() > 0) {
                where = " A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_NOMOR_ORDER] + " LIKE '%" + srcOrderAktiva.getNomorOrder() + "%'";
            }

            if (srcOrderAktiva.getNameSupplier().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcOrderAktiva.getNameSupplier() + "%'";
                } else {
                    where = " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcOrderAktiva.getNameSupplier() + "%'";
                }
            }

            if (srcOrderAktiva.getType() != 0) {
                if (where.length() > 0) {
                    where = where + " AND A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_TANGGAL_ORDER] + " BETWEEN '" + Formater.formatDate(srcOrderAktiva.getTanggalAwal(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcOrderAktiva.getTanggalakhir(), "yyyy-MM-dd") + "'";
                } else {
                    where = " A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_TANGGAL_ORDER] + " BETWEEN '" + Formater.formatDate(srcOrderAktiva.getTanggalAwal(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcOrderAktiva.getTanggalakhir(), "yyyy-MM-dd") + "'";
                }
            }
            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("err.SessModulAktiva - listModulAktiva " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }


    /**
     * untuk memposting order aktiva
     */
    public void postingOrderAktiva(long bookType, long userOID, long periodeOID, long oidOrder) {
        try {
            OrderAktiva orderAktiva = new OrderAktiva();
            orderAktiva = PstOrderAktiva.fetchExc(oidOrder);

            /**
             * object untuk jurnal umum
             */
            JurnalUmum jurnalUmum = new JurnalUmum();
            jurnalUmum.setTglTransaksi(orderAktiva.getTanggalOrder());
            jurnalUmum.setTglEntry(new Date());
            jurnalUmum.setKeterangan("Pembayaran DP u/ Order Nomor : " + orderAktiva.getNomorOrder());
            jurnalUmum.setBookType(bookType);
            jurnalUmum.setCurrType(orderAktiva.getIdCurrency());
            jurnalUmum.setUserId(userOID);
            jurnalUmum.setPeriodeId(periodeOID);
            jurnalUmum.setReferenceDoc(orderAktiva.getNomorOrder());
            jurnalUmum.setJurnalType(I_JournalType.TIPE_JURNAL_UMUM);
            /*String strVoucher = SessJurnal.generateVoucherNumber(periodeOID, jurnalUmum.getTglTransaksi());
            jurnalUmum.setSJurnalNumber(strVoucher);
            jurnalUmum.setVoucherNo(strVoucher.substring(0,4));
            jurnalUmum.setVoucherCounter(Integer.parseInt(strVoucher.substring(5)));
             */
            jurnalUmum.setContactOid(orderAktiva.getSupplierId());

            /**
             * object untuk jurnal detail
             */
            JurnalDetail jurnaldetail = new JurnalDetail();
            jurnaldetail.setIndex(0);
            jurnaldetail.setIdPerkiraan(orderAktiva.getIdPerkiraanDp());
            jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
            jurnaldetail.setCurrType(orderAktiva.getIdCurrency());
            jurnaldetail.setCurrAmount(orderAktiva.getValueRate());
            jurnaldetail.setDebet(orderAktiva.getDownPayment());
            jurnaldetail.setKredit(0);
            jurnalUmum.addDetails(jurnaldetail.getIndex(), jurnaldetail);

            // item lawan
            jurnaldetail = new JurnalDetail();
            jurnaldetail.setIndex(1);
            jurnaldetail.setIdPerkiraan(orderAktiva.getIdPerkiraanPayment());
            jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
            jurnaldetail.setCurrType(orderAktiva.getIdCurrency());
            jurnaldetail.setCurrAmount(orderAktiva.getValueRate());
            jurnaldetail.setDebet(0);
            jurnaldetail.setKredit(orderAktiva.getDownPayment());
            jurnalUmum.addDetails(jurnaldetail.getIndex(), jurnaldetail);

            CtrlJurnalUmum ctrlJurnalUmum = new CtrlJurnalUmum();
            ctrlJurnalUmum.JournalPosted(CtrlJurnalUmum.POSTED_JD, 0, jurnalUmum);
        } catch (Exception e) {
        }
    }
}
