package com.dimata.aiso.session.arap;

import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.entity.search.SrcArApEntry;
import com.dimata.aiso.entity.arap.*;
import com.dimata.aiso.entity.jurnal.JurnalUmum;
import com.dimata.aiso.entity.jurnal.JurnalDetail;
import com.dimata.aiso.entity.jurnal.PstJurnalDetail;
import com.dimata.aiso.entity.aktiva.PstReceiveAktiva;
import com.dimata.aiso.entity.aktiva.ReceiveAktiva;
import com.dimata.aiso.entity.aktiva.SellingAktiva;
import com.dimata.aiso.entity.aktiva.PstSellingAktiva;
import com.dimata.aiso.entity.jurnal.JournalDistribution;
import com.dimata.aiso.entity.jurnal.PstJournalDistribution;
import com.dimata.aiso.entity.jurnal.PstJurnalUmum;
import com.dimata.aiso.entity.periode.PstPeriode;
import com.dimata.aiso.entity.periode.Periode;
import com.dimata.aiso.session.jurnal.SessJurnal;
import com.dimata.aiso.form.jurnal.CtrlJurnalUmum;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.contact.ContactList;
import com.dimata.util.Formater;
import com.dimata.interfaces.journal.I_JournalType;
import com.dimata.qdep.entity.I_DocStatus;

import java.sql.Connection;
import java.util.Vector;
import java.util.Date;
import java.util.Enumeration;
import java.sql.ResultSet;

/**
 * Created by IntelliJ IDEA.
 * User: nengock
 * Date: Oct 12, 2005
 * Time: 10:11:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class SessArApEntry {
    public static final String SESS_SEARCH_ARAP_ENTRY = "SESS_SEARCH_ARAP_ENTRY";

    public static Vector listArApMain(SrcArApEntry srcArApEntry, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID] +
                    ", A." + PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO] +
                    ", A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO] +
                    ", A." + PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_DATE] +
		    ", A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE] +
                    ", A." + PstArApMain.fieldNames[PstArApMain.FLD_AMOUNT] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", A." + PstArApMain.fieldNames[PstArApMain.FLD_RATE] +
                    ", A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_DOC_STATUS] +
                    " FROM " + PstArApMain.TBL_ARAP_MAIN + " AS A " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstArApMain.fieldNames[PstArApMain.FLD_CONTACT_ID];

            String where = "";
            String sLike = " LIKE ";
            if(DBHandler.DBSVR_TYPE == DBHandler.DBSVR_POSTGRESQL)
                sLike = " ILIKE ";
             
            if (srcArApEntry.getVoucherNo().length() > 0) {
                where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO] + sLike +"'%" + srcArApEntry.getVoucherNo() + "%'";
            }

            if (srcArApEntry.getNotaNo().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]+ sLike + "'%" + srcArApEntry.getNotaNo() + "%'";
                } else {
                    where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO] + sLike + "'%" + srcArApEntry.getNotaNo() + "%'";
                }
            }


            if (where.length() > 0) {
                where = where + " AND A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_TYPE] + " = " + srcArApEntry.getArApType();
            } else {
                where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_TYPE] + " = " + srcArApEntry.getArApType();
            }
            
           
          
            if (srcArApEntry.getContactName().length() > 0) {
                if (where.length() > 0) {
                        where = where + " AND CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+ sLike +"'%" + srcArApEntry.getContactName() + "%'";
                } else {
                    where = " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+ sLike +"'%" + srcArApEntry.getContactName() + "%'";
                }
            }

            if (srcArApEntry.getType() != 0) {
                if (where.length() > 0) {
                    where = where + " AND A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE] + " BETWEEN '" + Formater.formatDate(srcArApEntry.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApEntry.getUntilDate(), "yyyy-MM-dd") + "'";
                } else {
                    where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE] + " BETWEEN '" + Formater.formatDate(srcArApEntry.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApEntry.getUntilDate(), "yyyy-MM-dd") + "'";
                }
            }
            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " ORDER BY A." + PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO];

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
                ArApMain arApMain = new ArApMain();
                arApMain.setOID(rs.getLong(PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID]));
                arApMain.setVoucherNo(rs.getString(PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO]));
                arApMain.setVoucherDate(rs.getDate(PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_DATE]));
		arApMain.setNotaDate(rs.getDate(PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE]));
                arApMain.setNotaNo(rs.getString(PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]));
                arApMain.setAmount(rs.getDouble(PstArApMain.fieldNames[PstArApMain.FLD_AMOUNT]));
                arApMain.setRate(rs.getDouble(PstArApMain.fieldNames[PstArApMain.FLD_RATE]));
                arApMain.setArApDocStatus(rs.getInt(PstArApMain.fieldNames[PstArApMain.FLD_ARAP_DOC_STATUS]));

                ContactList contactList = new ContactList();
                contactList.setCompName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]));
                contactList.setPersonName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]));

                vt.add(arApMain);
                vt.add(contactList);
                list.add(vt);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApEntry - listArApEntry " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    /**
     * gadnyana
     * untuk mencari jumlah row di daftar aktiva
     *
     * @param srcArApEntry
     * @return
     */
    public static int countArApMain(SrcArApEntry srcArApEntry) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(" +
                    " A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID] + ") AS CNT " +
                    " FROM " + PstArApMain.TBL_ARAP_MAIN + " AS A " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstArApMain.fieldNames[PstArApMain.FLD_CONTACT_ID];

            String where = "";
            if (srcArApEntry.getVoucherNo().length() > 0) {
                where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO] + " LIKE '%" + srcArApEntry.getVoucherNo() + "%'";
            }


            if (srcArApEntry.getNotaNo().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO] + " LIKE '%" + srcArApEntry.getNotaNo() + "%'";
                } else {
                    where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO] + " LIKE '%" + srcArApEntry.getNotaNo() + "%'";
                }
            }

            if (where.length() > 0) {
                where = where + " AND A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_TYPE] + " = " + srcArApEntry.getArApType();
            } else {
                where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_TYPE] + " = " + srcArApEntry.getArApType();
            }

            if (srcArApEntry.getContactName().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApEntry.getContactName() + "%'";
                } else {
                    where = " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApEntry.getContactName() + "%'";
                }
            }

            if (srcArApEntry.getType() != 0) {
                if (where.length() > 0) {
                    where = where + " AND A." + PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_DATE] + " BETWEEN '" + Formater.formatDate(srcArApEntry.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApEntry.getUntilDate(), "yyyy-MM-dd") + "'";
                } else {
                    where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_DATE] + " BETWEEN '" + Formater.formatDate(srcArApEntry.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApEntry.getUntilDate(), "yyyy-MM-dd") + "'";
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
            System.out.println("err.SessArApEntry - countArApMain " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    public int postingArApMain(long accountingBookType, long userOID, long currentPeriodOid, ArApMain arApMain) {
        int result = 0;
        /**
         * object untuk jurnal umum
         */
       
        if (arApMain != null && arApMain.getVoucherDate().compareTo(new Date()) <= 0) {
            JurnalUmum jurnalUmum = new JurnalUmum();
            jurnalUmum.setTglTransaksi(arApMain.getNotaDate());
            jurnalUmum.setTglEntry(arApMain.getVoucherDate());
            jurnalUmum.setKeterangan(arApMain.getDescription());
            jurnalUmum.setBookType(accountingBookType);
            jurnalUmum.setCurrType(arApMain.getIdCurrency());
            jurnalUmum.setUserId(userOID);
            jurnalUmum.setPeriodeId(currentPeriodOid);
            jurnalUmum.setReferenceDoc(arApMain.getVoucherNo());
            jurnalUmum.setJurnalType(I_JournalType.TIPE_JURNAL_UMUM);
            //String strVoucher = SessJurnal.generateVoucherNumber(currentPeriodOid, jurnalUmum.getTglTransaksi());
            //jurnalUmum.setSJurnalNumber(strVoucher);
            jurnalUmum.setContactOid(arApMain.getContactId());
            //jurnalUmum.setVoucherNo(strVoucher.substring(0, 4));
            //jurnalUmum.setVoucherCounter(Integer.parseInt(strVoucher.substring(5)));

            
            
            
            JurnalDetail jurnaldetail = new JurnalDetail();
            jurnaldetail.setIndex(0);
            if (arApMain.getArApType() == PstArApMain.TYPE_AR)
                jurnaldetail.setIdPerkiraan(arApMain.getIdPerkiraan());
            else
                jurnaldetail.setIdPerkiraan(arApMain.getIdPerkiraanLawan());
            jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
            jurnaldetail.setDebet(arApMain.getAmount());
            jurnaldetail.setCurrType(arApMain.getIdCurrency());
            jurnaldetail.setCurrAmount(arApMain.getRate());
            jurnaldetail.setNote(arApMain.getDescription());
            jurnaldetail.setKredit(0);
            jurnalUmum.addDetails(jurnaldetail.getIndex(), jurnaldetail);
            /**
             * object untuk jurnal detail
             */
            //Proses journal distribution
            
            Vector vJDistribution = (Vector)getJDistribution(arApMain);
            if(vJDistribution != null && vJDistribution.size() > 0){
                for(int i = 0; i < vJDistribution.size(); i++){
                    JournalDistribution objJDistribution = (JournalDistribution)vJDistribution.get(i);
                    jurnaldetail.addJDistributions(i, objJDistribution);
                }
            }
            
           
            // item lawan
            jurnaldetail = new JurnalDetail();
            jurnaldetail.setIndex(1);
            if (arApMain.getArApType() == PstArApMain.TYPE_AP)
                jurnaldetail.setIdPerkiraan(arApMain.getIdPerkiraan());
            else
                jurnaldetail.setIdPerkiraan(arApMain.getIdPerkiraanLawan());
            jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
            jurnaldetail.setCurrType(arApMain.getIdCurrency());
            jurnaldetail.setCurrAmount(arApMain.getRate());            
            jurnaldetail.setNote(arApMain.getDescription());
            jurnaldetail.setDebet(0);
            jurnaldetail.setKredit(arApMain.getAmount());
            jurnalUmum.addDetails(jurnaldetail.getIndex(), jurnaldetail);

            
            CtrlJurnalUmum ctrlJurnalUmum = new CtrlJurnalUmum();
            try {
                
                result = ctrlJurnalUmum.JournalPosted(CtrlJurnalUmum.POSTED_JD, 0, jurnalUmum);
		System.out.println("result 0 ::::::::::::::::::::::::::;; "+result);
                arApMain.setArApDocStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                arApMain.setArApMainStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                PstArApMain.updateExc(arApMain);
            } catch (Exception e) {
                result = 1;
            }

        } else {
	    System.out.println("result 1 ::::::::::::::::::::::::::;; "+result);
            result = 1;
        }
	
	System.out.println("result 2 ::::::::::::::::::::::::::;; "+result);
        if (result == 1) {
            try {
                arApMain.setArApDocStatus(0);
                PstArApMain.updateExc(arApMain);
            } catch (Exception e) {
                System.out.println("err Fail Update ArApMain: " + e.toString());
            }
        }

        return result;
    }
    
    public static long postingArApPaymentWithJD(long accountingBookType, long userOID, long currentPeriodOid, ArApPayment arApPayment, JurnalDetail jurnaldetail) {
        long result = 0;
        if(jurnaldetail==null){
            jurnaldetail = new JurnalDetail();
        }
        try {
            // cek tanggal transaksi dan entry

            Date startDate = null;
            Date finishDate = null;
            Date lastDate = null;
            Vector sessperiode = PstPeriode.getCurrPeriod();
            if (sessperiode != null && sessperiode.size() > 0) {
                Periode periode = (Periode) sessperiode.get(0);
                startDate = periode.getTglAwal();
                finishDate = periode.getTglAkhir();
                lastDate = periode.getTglAkhirEntry();
            }

            if (arApPayment != null && arApPayment.getPaymentDate().compareTo(startDate)>=0 && arApPayment.getPaymentDate().compareTo(finishDate)<=0) {
                String whereItem = PstArApItem.fieldNames[PstArApItem.FLD_ARAP_MAIN_ID] + "=" + arApPayment.getArapMainId() +
                        " AND " + PstArApItem.fieldNames[PstArApItem.FLD_SELLING_AKTIVA_ID] + "=" + arApPayment.getSellingAktivaId() +
                        " AND " + PstArApItem.fieldNames[PstArApItem.FLD_RECEIVE_AKTIVA_ID] + "=" + arApPayment.getReceiveAktivaId() +
                        " AND " + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + " > 0";
                String orderItem = PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE];
                Vector vectItem = PstArApItem.list(0, 0, whereItem, orderItem);
                Enumeration enumItem = vectItem.elements();
                double dPay = arApPayment.getAmount();
                double dItem = 0;
                boolean isClose = false;
                while (enumItem.hasMoreElements() && dPay > 0) {
                    ArApItem item = (ArApItem) enumItem.nextElement();

                    if (dPay > item.getLeftToPay()) {
                        dPay = dPay - item.getLeftToPay();
                        item.setLeftToPay(0);
                        item.setArApItemStatus(PstArApMain.STATUS_CLOSED);
                    } else {
                        item.setLeftToPay(item.getLeftToPay() - dPay);
                        dPay = 0;
                    }
                    dItem = item.getLeftToPay();
                    try {
                        PstArApItem.updateExc(item);
                    } catch (Exception e) {
                        System.out.println("err on update item: " + e.toString());
                        result = CtrlJurnalUmum.RSLT_ERR_ON_UPDATE;
                    }
                }
                if (!enumItem.hasMoreElements()) {
                    isClose = (dItem == 0);
                }

                if (arApPayment.getLeftToPay() > arApPayment.getAmount()) {
                    arApPayment.setLeftToPay(arApPayment.getLeftToPay() - arApPayment.getAmount());
                } else {
                    arApPayment.setLeftToPay(0);
                }
                long idPerkiraanMain = 0;
                String description = "Payment ";
                if (arApPayment.getArapMainId() > 0) {
                    try {
                        ArApMain main = PstArApMain.fetchExc(arApPayment.getArapMainId());
                        idPerkiraanMain = main.getIdPerkiraan();
                        arApPayment.setArApType(main.getArApType());
                        description = description + main.getVoucherNo() + "[" + main.getDescription() + "]";
                        if (isClose) {
                            main.setArApMainStatus(PstArApMain.STATUS_CLOSED);
                            PstArApMain.updateExc(main);
                        }
                    } catch (Exception e) {
                        System.out.println("err on get Main: " + e.toString());
                        result = CtrlJurnalUmum.RSLT_ERR_ON_UPDATE;
                    }
                } else if (arApPayment.getReceiveAktivaId() > 0) {
                    try {
                        ReceiveAktiva main = PstReceiveAktiva.fetchExc(arApPayment.getReceiveAktivaId());
                        idPerkiraanMain = main.getIdPerkiraanPayment();
                        arApPayment.setArApType(PstArApMain.TYPE_AP);
                        description = description + main.getNomorReceive();
                        if (isClose) {
                            main.setArApMainStatus(PstArApMain.STATUS_CLOSED);
                            PstReceiveAktiva.updateExc(main);
                        }
                    } catch (Exception e) {
                        System.out.println("err on get Main: " + e.toString());
                        result = CtrlJurnalUmum.RSLT_ERR_ON_UPDATE;
                    }
                } else if (arApPayment.getSellingAktivaId() > 0) {
                    try {
                        SellingAktiva main = PstSellingAktiva.fetchExc(arApPayment.getSellingAktivaId());
                        idPerkiraanMain = main.getIdPerkiraanPayment();
                        arApPayment.setArApType(PstArApMain.TYPE_AR);
                        description = description + main.getNomorSelling();
                        if (isClose) {
                            main.setArApMainStatus(PstArApMain.STATUS_CLOSED);
                            PstSellingAktiva.updateExc(main);
                        }
                    } catch (Exception e) {
                        System.out.println("err on get Main: " + e.toString());
                        result = CtrlJurnalUmum.RSLT_ERR_ON_UPDATE;
                    }
                }

                PstArApPayment.createOrderNomor(arApPayment);
                System.out.println("IN>>>> ");
                /**
                 * object untuk jurnal umum
                 */
                JurnalUmum jurnalUmum = new JurnalUmum();
                jurnalUmum.setTglTransaksi(arApPayment.getPaymentDate());
                jurnalUmum.setTglEntry(new Date());
                jurnalUmum.setKeterangan(description);
                jurnalUmum.setBookType(accountingBookType);
                jurnalUmum.setCurrType(arApPayment.getIdCurrency());
                jurnalUmum.setUserId(userOID);
                jurnalUmum.setPeriodeId(currentPeriodOid);
                jurnalUmum.setReferenceDoc(arApPayment.getPaymentNo());
                jurnalUmum.setJurnalType(I_JournalType.TIPE_JURNAL_UMUM);
                //String strVoucher = SessJurnal.generateVoucherNumber(currentPeriodOid, jurnalUmum.getTglTransaksi());
                jurnalUmum.setContactOid(arApPayment.getContactId());
                //jurnalUmum.setSJurnalNumber(strVoucher);
                //jurnalUmum.setVoucherNo(strVoucher.substring(0, 4));
                //jurnalUmum.setVoucherCounter(Integer.parseInt(strVoucher.substring(5)));

                //System.out.println("IN>>>> ");
                /**
                 * object untuk jurnal detail
                 */
                jurnaldetail = new JurnalDetail();
                jurnaldetail.setIndex(0);
                if (arApPayment.getArApType() == PstArApMain.TYPE_AR)
                    jurnaldetail.setIdPerkiraan(arApPayment.getIdPerkiraanPayment());
                else
                    jurnaldetail.setIdPerkiraan(idPerkiraanMain);
                jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                jurnaldetail.setDebet(arApPayment.getAmount());
                jurnaldetail.setCurrType(arApPayment.getIdCurrency());
                jurnaldetail.setCurrAmount(arApPayment.getRate());
                jurnaldetail.setKredit(0);
                jurnalUmum.addDetails(jurnaldetail.getIndex(), jurnaldetail);

                System.out.println("IN>>>> ");
                // item lawan
                jurnaldetail = new JurnalDetail();
                jurnaldetail.setIndex(1);
                if (arApPayment.getArApType() == PstArApMain.TYPE_AP)
                    jurnaldetail.setIdPerkiraan(arApPayment.getIdPerkiraanPayment());
                else
                    jurnaldetail.setIdPerkiraan(idPerkiraanMain);
                jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                jurnaldetail.setCurrType(arApPayment.getIdCurrency());
                jurnaldetail.setCurrAmount(arApPayment.getRate());
                jurnaldetail.setDebet(0);
                jurnaldetail.setKredit(arApPayment.getAmount());
                jurnalUmum.addDetails(jurnaldetail.getIndex(), jurnaldetail);

                //System.out.println("IN>>>> ");

                CtrlJurnalUmum ctrlJurnalUmum = new CtrlJurnalUmum();
                int iResult = ctrlJurnalUmum.JournalPosted(CtrlJurnalUmum.POSTED_JD, 0, jurnalUmum);
                
                if(iResult == CtrlJurnalUmum.RSLT_OK){
                    jurnaldetail = (JurnalDetail)jurnalUmum.getJurnalDetail(1);
                    //result = jurnaldetail.getOID();
                }

                if (iResult == CtrlJurnalUmum.RSLT_OK) {
                    try {
                        arApPayment.setPaymentStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                        PstArApPayment.insertExc(arApPayment);
                    } catch (Exception e) {
                        System.out.println("err on insert Payment : " + e.toString());
                        result = CtrlJurnalUmum.RSLT_ERR_ON_INSERT;
                    }
                }
            } else {
                result = CtrlJurnalUmum.RSLT_ERR_TRANS_DATE;
            }
        } catch (Exception e) {
            System.out.println("err on Posting Payment: " + e.toString());
            result = CtrlJurnalUmum.RSLT_UNKNOWN_ERROR;
        }

        return result;
    }

    public static int postingArApPayment(long accountingBookType, long userOID, long currentPeriodOid, ArApPayment arApPayment) {
        int result = 0;
        try {
            // cek tanggal transaksi dan entry

            Date startDate = null;
            Date finishDate = null;
            Date lastDate = null;
            Vector sessperiode = PstPeriode.getCurrPeriod();
            if (sessperiode != null && sessperiode.size() > 0) {
                Periode periode = (Periode) sessperiode.get(0);
                startDate = periode.getTglAwal();
                finishDate = periode.getTglAkhir();
                lastDate = periode.getTglAkhirEntry();
            }

            if (arApPayment != null && arApPayment.getPaymentDate().compareTo(startDate)>=0 && arApPayment.getPaymentDate().compareTo(finishDate)<=0) {
                String whereItem = PstArApItem.fieldNames[PstArApItem.FLD_ARAP_MAIN_ID] + "=" + arApPayment.getArapMainId() +
                        " AND " + PstArApItem.fieldNames[PstArApItem.FLD_SELLING_AKTIVA_ID] + "=" + arApPayment.getSellingAktivaId() +
                        " AND " + PstArApItem.fieldNames[PstArApItem.FLD_RECEIVE_AKTIVA_ID] + "=" + arApPayment.getReceiveAktivaId() +
                        " AND " + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + " > 0";
                String orderItem = PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE];
                Vector vectItem = PstArApItem.list(0, 0, whereItem, orderItem);
                Enumeration enumItem = vectItem.elements();
                double dPay = arApPayment.getAmount();
                double dItem = 0;
                boolean isClose = false;
                while (enumItem.hasMoreElements() && dPay > 0) {
                    ArApItem item = (ArApItem) enumItem.nextElement();

                    if (dPay > item.getLeftToPay()) {
                        dPay = dPay - item.getLeftToPay();
                        item.setLeftToPay(0);
                        item.setArApItemStatus(PstArApMain.STATUS_CLOSED);
                    } else {
                        item.setLeftToPay(item.getLeftToPay() - dPay);
                        dPay = 0;
                    }
                    dItem = item.getLeftToPay();
                    try {
                        PstArApItem.updateExc(item);
                    } catch (Exception e) {
                        System.out.println("err on update item: " + e.toString());
                        result = 1;
                    }
                }
                if (!enumItem.hasMoreElements()) {
                    isClose = (dItem == 0);
                }

                if (arApPayment.getLeftToPay() > arApPayment.getAmount()) {
                    arApPayment.setLeftToPay(arApPayment.getLeftToPay() - arApPayment.getAmount());
                } else {
                    arApPayment.setLeftToPay(0);
                }
                long idPerkiraanMain = 0;
                String description = "Payment ";
                if (arApPayment.getArapMainId() > 0) {
                    try {
                        ArApMain main = PstArApMain.fetchExc(arApPayment.getArapMainId());
                        idPerkiraanMain = main.getIdPerkiraan();
                        arApPayment.setArApType(main.getArApType());
                        description = description + main.getVoucherNo() + "[" + main.getDescription() + "]";
                        if (isClose) {
                            main.setArApMainStatus(PstArApMain.STATUS_CLOSED);
                            PstArApMain.updateExc(main);
                        }
                    } catch (Exception e) {
                        System.out.println("err on get Main: " + e.toString());
                        result = 1;
                    }
                } else if (arApPayment.getReceiveAktivaId() > 0) {
                    try {
                        ReceiveAktiva main = PstReceiveAktiva.fetchExc(arApPayment.getReceiveAktivaId());
                        idPerkiraanMain = main.getIdPerkiraanPayment();
                        arApPayment.setArApType(PstArApMain.TYPE_AP);
                        description = description + main.getNomorReceive();
                        if (isClose) {
                            main.setArApMainStatus(PstArApMain.STATUS_CLOSED);
                            PstReceiveAktiva.updateExc(main);
                        }
                    } catch (Exception e) {
                        System.out.println("err on get Main: " + e.toString());
                        result = 1;
                    }
                } else if (arApPayment.getSellingAktivaId() > 0) {
                    try {
                        SellingAktiva main = PstSellingAktiva.fetchExc(arApPayment.getSellingAktivaId());
                        idPerkiraanMain = main.getIdPerkiraanPayment();
                        arApPayment.setArApType(PstArApMain.TYPE_AR);
                        description = description + main.getNomorSelling();
                        if (isClose) {
                            main.setArApMainStatus(PstArApMain.STATUS_CLOSED);
                            PstSellingAktiva.updateExc(main);
                        }
                    } catch (Exception e) {
                        System.out.println("err on get Main: " + e.toString());
                        result = 1;
                    }
                }

                PstArApPayment.createOrderNomor(arApPayment);
                System.out.println("IN>>>> ");
                /**
                 * object untuk jurnal umum
                 */
                JurnalUmum jurnalUmum = new JurnalUmum();
                jurnalUmum.setTglTransaksi(arApPayment.getPaymentDate());
                jurnalUmum.setTglEntry(new Date());
                jurnalUmum.setKeterangan(description);
                jurnalUmum.setBookType(accountingBookType);
                jurnalUmum.setCurrType(arApPayment.getIdCurrency());
                jurnalUmum.setUserId(userOID);
                jurnalUmum.setPeriodeId(currentPeriodOid);
                jurnalUmum.setReferenceDoc(arApPayment.getPaymentNo());
                jurnalUmum.setJurnalType(I_JournalType.TIPE_JURNAL_UMUM);
                //String strVoucher = SessJurnal.generateVoucherNumber(currentPeriodOid, jurnalUmum.getTglTransaksi());
                jurnalUmum.setContactOid(arApPayment.getContactId());
                //jurnalUmum.setSJurnalNumber(strVoucher);
                //jurnalUmum.setVoucherNo(strVoucher.substring(0, 4));
                //jurnalUmum.setVoucherCounter(Integer.parseInt(strVoucher.substring(5)));

                System.out.println("IN>>>> ");
                /**
                 * object untuk jurnal detail
                 */
                JurnalDetail jurnaldetail = new JurnalDetail();
                jurnaldetail.setIndex(0);
                if (arApPayment.getArApType() == PstArApMain.TYPE_AR)
                    jurnaldetail.setIdPerkiraan(arApPayment.getIdPerkiraanPayment());
                else
                    jurnaldetail.setIdPerkiraan(idPerkiraanMain);
                jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                jurnaldetail.setDebet(arApPayment.getAmount());
                jurnaldetail.setCurrType(arApPayment.getIdCurrency());
                jurnaldetail.setCurrAmount(arApPayment.getRate());
                jurnaldetail.setKredit(0);
                jurnalUmum.addDetails(jurnaldetail.getIndex(), jurnaldetail);

                System.out.println("IN>>>> ");
                // item lawan
                jurnaldetail = new JurnalDetail();
                jurnaldetail.setIndex(1);
                if (arApPayment.getArApType() == PstArApMain.TYPE_AP)
                    jurnaldetail.setIdPerkiraan(arApPayment.getIdPerkiraanPayment());
                else
                    jurnaldetail.setIdPerkiraan(idPerkiraanMain);
                jurnaldetail.setDataStatus(PstJurnalDetail.DATASTATUS_ADD);
                jurnaldetail.setCurrType(arApPayment.getIdCurrency());
                jurnaldetail.setCurrAmount(arApPayment.getRate());
                jurnaldetail.setDebet(0);
                jurnaldetail.setKredit(arApPayment.getAmount());
                jurnalUmum.addDetails(jurnaldetail.getIndex(), jurnaldetail);

                //System.out.println("IN>>>> ");

                CtrlJurnalUmum ctrlJurnalUmum = new CtrlJurnalUmum();
                int iResult = ctrlJurnalUmum.JournalPosted(CtrlJurnalUmum.POSTED_JD, 0, jurnalUmum);

                if (iResult == CtrlJurnalUmum.RSLT_OK) {
                    try {
                        arApPayment.setPaymentStatus(I_DocStatus.DOCUMENT_STATUS_POSTED);
                        PstArApPayment.insertExc(arApPayment);
                    } catch (Exception e) {
                        System.out.println("err on insert Payment : " + e.toString());
                        result = CtrlJurnalUmum.RSLT_UNKNOWN_ERROR;
                    }
                }
            } else {
                result = CtrlJurnalUmum.RSLT_ERR_TRANS_DATE;
            }
        } catch (Exception e) {
            System.out.println("err on Posting Payment: " + e.toString());
            result = CtrlJurnalUmum.RSLT_UNKNOWN_ERROR;
        }

        return result;
    }

public static synchronized long getIdJournalByRefDoc(String refDoc){
    DBResultSet dbrs = null;
    long lResult = 0;
    try{
	String sql = " SELECT "+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]+
		     " FROM "+PstJurnalUmum.TBL_JURNAL_UMUM+
		     " WHERE "+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_REFERENCE_DOCUMENT]+
		     " = '"+refDoc+"' ";
	
	//System.out.println("SQL SessArApEntry.getIdJournalByRefDoc ::::::::::: "+sql);
	dbrs = DBHandler.execQueryResult(sql);
	ResultSet rs = dbrs.getResultSet();
	while(rs.next()){
	    lResult = rs.getLong(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]);
	}
	rs.close();
    }catch(Exception e){}	
    return lResult;
}

public static synchronized Vector getListObjJournalDetailByIdMain(long lIdJournalMain){
    DBResultSet dbrs = null;
    Vector vResult = new Vector(1,1);
    try{
	String sql = " SELECT "+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JDETAILID]+
		     " FROM "+PstJurnalDetail.TBL_JURNAL_DETAIL+
		     " WHERE "+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID]+
		     " = "+lIdJournalMain;
	
	//System.out.println("SQL SessArApEntry.getListObjJournalDetailByIdMain ::::::::::: "+sql);
	dbrs = DBHandler.execQueryResult(sql);
	ResultSet rs = dbrs.getResultSet();
	while(rs.next()){
	    long lIdJournalDetail = 0;
	    JurnalDetail objJurnalDetail = new JurnalDetail();
	    lIdJournalDetail = rs.getLong(PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JDETAILID]);
	    if(lIdJournalDetail != 0){
		try{
		    objJurnalDetail = PstJurnalDetail.fetchExc(lIdJournalDetail);
		    vResult.add(objJurnalDetail);
		}catch(Exception e){}
	    }
	}
	rs.close();
    }catch(Exception e){}
    return vResult;
}

public static synchronized void updateArapPayment(long lArapPaymentId, ArApPayment oArApPayment){
    int iResult = 0;
    int iUpdateArapPayment = 0;
    long lUpdateArapItem = 0;
    int iUpdateJournalMain = 0;
    int iUpdateJournalDetail = 0;
    long lIdJournalMain = 0;
    ArApMain objArApMain = new ArApMain();
    ArApPayment objArApPayment = new ArApPayment();
    if(lArapPaymentId != 0){
	try{
	    objArApPayment = PstArApPayment.fetchExc(lArapPaymentId);
	}catch(Exception e){}
    }
    
    if(objArApPayment.getArapMainId() != 0){
	try{
	    objArApMain = PstArApMain.fetchExc(objArApPayment.getArapMainId());
	}catch(Exception e){}
	double dLeftToPay = 0;		
	try{	    
	    objArApPayment.setPaymentDate(oArApPayment.getPaymentDate());
	    objArApPayment.setAmount(oArApPayment.getAmount());
	    objArApPayment.setIdCurrency(oArApPayment.getIdCurrency());
	    objArApPayment.setIdPerkiraanPayment(oArApPayment.getIdPerkiraanPayment());
	    objArApPayment.setRate(oArApPayment.getRate());
	    long lUpdate = PstArApPayment.updateExc(objArApPayment);
	    iUpdateArapPayment = 1;
	    if(iUpdateArapPayment == 1){
		dLeftToPay = getLeftToPay(objArApMain, objArApPayment, 0);	
		objArApPayment.setLeftToPay(dLeftToPay);
		long lNextUpdate = PstArApPayment.updateExc(objArApPayment);
	    }
	}catch(Exception e){
	    //System.out.println("Exception on updateArapPayment ::::: "+e.toString());
	}
	
	if(iUpdateArapPayment == 1){
	    try{
		lUpdateArapItem = updateArapItem(objArApPayment.getArapMainId(), dLeftToPay);
	    }catch(Exception e){
		//System.out.println("Exception on updateArapItem ::::: "+e.toString());
	    }
	}
	
	if(lUpdateArapItem != 0){
	    try{
		iUpdateJournalMain = updateJournalMain(objArApPayment, lUpdateArapItem);
	    }catch(Exception e){
		//System.out.println("Exception on updateJournalMain ::::: "+e.toString());
	    }
	}
	
	if(iUpdateJournalMain == 1){
	    try{
		iUpdateJournalDetail = updateJournalDetail(objArApPayment, iUpdateJournalMain);
	    }catch(Exception e){
		//System.out.println("Exception on updateJournalDetail ::::: "+e.toString());
	    }
	}
    }    
   
}

/*
 try {            
        Connection con = makeDatabaseConnection();
        con.setAutoCommit(false);           
        //  Do whatever database transaction functionality
        //  is necessary           
        con.commit();          
    } catch (Exception ex) {
        try {
            con.rollback();
        } catch (SQLException sqx) {
            throw new EJBException("Rollback failed: " +
                sqx.getMessage());
        }
    } finally {
        releaseDatabaseConnection();
    }
 */

public static synchronized void deleteArapPayment(long lIdArapPayment){
    Connection con = DBHandler.getDBConnection();
    ArApPayment objArApPayment = new ArApPayment();
    ArApPayment oArApPayment = new ArApPayment();
    ArApMain objArApMain = new ArApMain();
    double dPaymentToDelete = 0;
    double dLeftToPay = 0;
    if(lIdArapPayment != 0){
	try{
	    objArApPayment = PstArApPayment.fetchExc(lIdArapPayment);	    
	    dPaymentToDelete = objArApPayment.getAmount();
	}catch(Exception e){}
    }
    
    if(objArApPayment.getArapMainId() != 0){
	try{
	    objArApMain = PstArApMain.fetchExc(objArApPayment.getArapMainId());
	}catch(Exception e){}
    }
    
    try{
	long lUpdateDB = 0;
	con.setAutoCommit(false);
	lUpdateDB = PstArApPayment.deleteExc(lIdArapPayment,con);
	
	long lIdJournalMain = 0;
	lIdJournalMain = getIdJournalByRefDoc(objArApPayment.getPaymentNo());
	if(lIdJournalMain != 0){
	    try{
		lUpdateDB = PstJurnalUmum.deleteExc(lIdJournalMain,con);
		lUpdateDB = deleteJournalDetail(lIdJournalMain,con);
	    }catch(Exception e){}
	}
	
	oArApPayment = (ArApPayment)getObjArApPayment(objArApMain.getOID());	
	dLeftToPay = getLeftToPay(objArApMain, oArApPayment, 1);
	//System.out.println("dLeftToPay ::::::: "+dLeftToPay);
	if(objArApMain.getOID() != 0){	    
	    oArApPayment.setLeftToPay(dLeftToPay);
	    try{
		lUpdateDB = PstArApPayment.updateExc(oArApPayment, con);
		lUpdateDB = updateArapItem(objArApMain.getOID(), dLeftToPay, con);
	    }catch(Exception e){}
	}
	
	if(lUpdateDB != 0){
	    con.commit();
	}else{
	    con.rollback();
	}
    }catch(Exception e){}
}

public static synchronized long deleteJournalDetail(long lIdJournalMain, Connection con){
    long lResult = 0;
    if(lIdJournalMain != 0){
	try{
	    String sql = " DELETE FROM "+PstJurnalDetail.TBL_JURNAL_DETAIL+
			 " WHERE "+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID]+
			 " = "+lIdJournalMain;
	    DBHandler.deleteTran(con, sql);
	    lResult = lIdJournalMain;
	}catch(Exception e){}
    }
    return lResult;
}

public static synchronized Vector getListObjArapPayment(long lArapMainId){
    DBResultSet dbrs = null;
    Vector vResult = new Vector(1,1);
    try{
	String sql = " SELECT "+PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_PAYMENT_ID]+
		     " FROM "+PstArApPayment.TBL_ARAP_PAYMENT+
		     " WHERE "+PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_MAIN_ID]+
		     " = "+lArapMainId+
		     " ORDER BY "+PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_PAYMENT_ID]+" DESC ";
	
	System.out.println("SQL SessArApEntry.getListObjArapPayment :::::::: "+sql);
	dbrs = DBHandler.execQueryResult(sql);
	ResultSet rs = dbrs.getResultSet();
	while(rs.next()){
	    ArApPayment objArApPayment = new ArApPayment();
	    long lArapPaymentId = 0;
	    lArapPaymentId = rs.getLong(PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_PAYMENT_ID]);
	    if(lArapPaymentId != 0){
		objArApPayment = PstArApPayment.fetchExc(lArapPaymentId);
		vResult.add(objArApPayment);
	    }	    
	}
    }catch(Exception e){}
    return vResult;
}

public static synchronized ArApPayment getObjArApPayment(long lArapMainId){
    ArApPayment objArApPayment = new ArApPayment();
    Vector vListObjArapPayment = new Vector(1,1);
    if(lArapMainId != 0){
	vListObjArapPayment = getListObjArapPayment(lArapMainId);	
    }    
    if(vListObjArapPayment != null && vListObjArapPayment.size() > 0){
	for(int i = 0; i < vListObjArapPayment.size(); i++){
	    objArApPayment = (ArApPayment)vListObjArapPayment.get(0);	   
	}
    }
    return objArApPayment;
}

public static synchronized double getTotalPayment(ArApPayment objArApPayment, int iUpdate){
    DBResultSet dbrs = null;
    double dResult = 0;
    if(objArApPayment.getArapMainId() != 0){
	try{
	    String sql = " SELECT SUM("+PstArApPayment.fieldNames[PstArApPayment.FLD_AMOUNT]+") AS AMOUNT "+
			 " FROM "+PstArApPayment.TBL_ARAP_PAYMENT+
			 " WHERE "+PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_MAIN_ID]+
			 " = "+objArApPayment.getArapMainId();
	     if(iUpdate == 1){
		     sql += " AND "+PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_PAYMENT_ID]+	   
			    " != "+objArApPayment.getOID();
	    }
	    
	    //System.out.println("SQL SessArApEntry.getTotalPayment ::::::::::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		dResult = rs.getDouble("AMOUNT");
	    }
	    rs.close();
	}catch(Exception e){}
    }
    return dResult;
}

public static synchronized double getLeftToPay(ArApMain objArApMain, ArApPayment objArApPayment, int iUpdate){
    double dResult = 0;
    double dArApAmount = 0;
    double dTotalPayment = 0;
    
    //System.out.println("objArApPayment.getArapMainId() ::::::::::::: "+objArApPayment.getArapMainId());
    if(objArApPayment.getArapMainId() != 0){
	dTotalPayment = getTotalPayment(objArApPayment, iUpdate);
	//System.out.println("dTotalPayment ::::::::::::: "+dTotalPayment);
    }
    
    //System.out.println("objArApMain.getAmount() ::::::::::::: "+objArApMain.getAmount());
    if(objArApMain.getAmount() > 0){
	dArApAmount = objArApMain.getAmount();
    }
    dResult = dArApAmount - dTotalPayment;
    if(dResult == 0){
	dResult = dArApAmount;
    }
    return dResult;
}

public static synchronized Vector getListObjArapItem(long lArapMainId){
    DBResultSet dbrs = null;
    Vector vResult = new Vector(1,1);    
    
    if(lArapMainId != 0){
	try{
	    String sql = " SELECT "+PstArApItem.fieldNames[PstArApItem.FLD_ARAP_ITEM_ID]+
			 " FROM "+PstArApItem.TBL_ARAP_ITEM+
			 " WHERE "+PstArApItem.fieldNames[PstArApItem.FLD_ARAP_MAIN_ID]+
			 " = "+lArapMainId+
			 " ORDER BY "+PstArApItem.fieldNames[PstArApItem.FLD_ARAP_ITEM_ID]+" DESC ";
	    
	    //System.out.println("SQL SessArApEntry.getListObjArapItem ::::::::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		long lIdArapItem = 0;
		ArApItem objArApItem = new ArApItem();
		lIdArapItem = rs.getLong(PstArApItem.fieldNames[PstArApItem.FLD_ARAP_ITEM_ID]);
		if(lIdArapItem != 0){
		    try{
			objArApItem = PstArApItem.fetchExc(lIdArapItem);
			vResult.add(objArApItem);
		    }catch(Exception e){}
		}
	    }
	    rs.close();
	}catch(Exception e){}
    }
    return vResult;
}

public static synchronized ArApItem getObjArapItem(long lArapMainId){
    ArApItem objArApItem = new ArApItem();
    Vector vListArapItem = new Vector(1,1);
    vListArapItem = (Vector)getListObjArapItem(lArapMainId);   
    if(vListArapItem != null && vListArapItem.size() > 0){
	for(int i = 0; i < vListArapItem.size(); i++){
	    objArApItem = (ArApItem)vListArapItem.get(0);
	}
    }
    return objArApItem;
}

public static synchronized long updateArapItem(long lArapMainId, double dLeftToPay){
    return updateArapItem(lArapMainId, dLeftToPay,null);
}

public static synchronized long updateArapItem(long lArapMainId, double dLeftToPay, Connection con){
    long lResult = 0;
    ArApItem objArApItem = new ArApItem();
    try{
	
	objArApItem = getObjArapItem(lArapMainId);	
	if(objArApItem != null){
	    objArApItem.setLeftToPay(dLeftToPay);
	    try{
		if(con == null){
		    lResult = PstArApItem.updateExc(objArApItem);
		}else{
		    lResult = PstArApItem.updateExc(objArApItem, con);
		}
	    }catch(Exception e){}
	}    
    }catch(Exception e){}
    return lResult;
}

public static synchronized int updateJournalMain(ArApPayment objArApPayment, long lUpdateArapPayment){
    int iResult = 0;
    long lIdJournalMain = 0;
    JurnalUmum objJurnalUmum = new JurnalUmum();
    if(objArApPayment.getPaymentNo() != null && objArApPayment.getPaymentNo().length() > 0){
	try{
	    lIdJournalMain = getIdJournalByRefDoc(objArApPayment.getPaymentNo());
	}catch(Exception e){}
    }
    
    if(lIdJournalMain != 0){	
	try{
	    objJurnalUmum = PstJurnalUmum.fetchExc(lIdJournalMain);	    
	}catch(Exception e){}
    }
    
    if(lUpdateArapPayment != 0){
	if(objJurnalUmum != null){
	    try{
		objJurnalUmum.setTglTransaksi(objArApPayment.getPaymentDate());
		long lUpdateJournalUmum = PstJurnalUmum.updateExc(objJurnalUmum);
		iResult = 1;
	    }catch(Exception e){}
	}
    }
    
    return iResult;
}

public static synchronized int updateJournalDetail(ArApPayment objArApPayment, int iUpdateJournal){
    int iResult = 0;
    Vector vJurnalDetail = new Vector(1,1);
    JurnalDetail objJurnalDetail = new JurnalDetail();
    ArApMain objArApMain = new ArApMain();
    long lJournalId = 0;
    if(objArApPayment.getPaymentNo() != null && objArApPayment.getPaymentNo().length() > 0){
	lJournalId = getIdJournalByRefDoc(objArApPayment.getPaymentNo());
    }
    
    if(objArApPayment.getArapMainId() != 0){
	try{
	    objArApMain = PstArApMain.fetchExc(objArApPayment.getArapMainId());
	}catch(Exception e){}
    }
    
    if(lJournalId != 0){
	try{
	    vJurnalDetail = getListObjJournalDetailByIdMain(lJournalId);
	}catch(Exception e){}
    }
    
    if(iUpdateJournal != 0){
	if(vJurnalDetail != null && vJurnalDetail.size() > 0){
	    for(int i = 0; i < vJurnalDetail.size(); i++){
		objJurnalDetail = (JurnalDetail)vJurnalDetail.get(i);
		if(objJurnalDetail != null){
		    if(objArApMain.getArApType() == PstArApMain.TYPE_AR){
			if(i == 0){
			    objJurnalDetail.setIdPerkiraan(objArApPayment.getIdPerkiraanPayment());
			    objJurnalDetail.setDebet(objArApPayment.getAmount());
			    try{
				long lUpdateDetailDebet = PstJurnalDetail.updateExc(objJurnalDetail);
				iResult = 1;
			    }catch(Exception e){}
			}else{
			    objJurnalDetail.setKredit(objArApPayment.getAmount());
			     try{
				long lUpdateDetailKredit = PstJurnalDetail.updateExc(objJurnalDetail);
				iResult = 1;
			    }catch(Exception e){}
			}
		    }else{
			if(i == 0){			    
			    objJurnalDetail.setDebet(objArApPayment.getAmount());
			     try{
				long lUpdateDetailDebet = PstJurnalDetail.updateExc(objJurnalDetail);
				iResult = 1;
			    }catch(Exception e){}
			}else{
			    objJurnalDetail.setIdPerkiraan(objArApPayment.getIdPerkiraanPayment());
			    objJurnalDetail.setKredit(objArApPayment.getAmount());
			     try{
				long lUpdateDetailKredit = PstJurnalDetail.updateExc(objJurnalDetail);
				iResult = 1;
			    }catch(Exception e){}
			}
		    }
		}
	    }
	}
    }
    return iResult;
}

public static synchronized String getStringDelete(String strTblName, String StrwhClause, long lOid){
    String sResult = "";
    try{
	sResult = " DELETE FROM "+strTblName+" WHERE "+StrwhClause+" = "+lOid;
    }catch(Exception e){}
    return sResult;
}

public static synchronized Vector getJDistribution(ArApMain objArApMain){
    Vector vResult = new Vector();
    try{
        String whClause = PstJournalDistribution.fieldNames[PstJournalDistribution.FLD_ARAP_MAIN_ID]+" = "+objArApMain.getOID()+
                          " AND "+PstJournalDistribution.fieldNames[PstJournalDistribution.FLD_JOURNAL_DETAIL_ID]+" = 0 ";
        vResult = ( (objArApMain==null ) || (objArApMain.getOID()==0) ) ? new Vector() : PstJournalDistribution.list(0, 0, whClause, PstJournalDistribution.fieldNames[PstJournalDistribution.FLD_JOURNAL_DISTRIBUTION_ID]);
    }catch(Exception e){}
    return vResult;
}

}
