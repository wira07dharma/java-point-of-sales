/*
 * SessJurnal.java
 *
 * Created on July 29, 2005, 3:08 PM
 */

package com.dimata.aiso.session.jurnal;

// import java

import java.util.*;
import java.util.Date; 
import java.sql.*;

// import qdep
import com.dimata.aiso.db.*;
import com.dimata.util.*;
import com.dimata.common.entity.contact.*;

// import aiso
import com.dimata.aiso.entity.admin.*;
import com.dimata.aiso.entity.masterdata.*;
import com.dimata.aiso.entity.jurnal.*;
import com.dimata.aiso.entity.periode.*;
import com.dimata.aiso.entity.search.*;
import com.dimata.aiso.form.search.*;
import com.dimata.aiso.session.specialJournal.SessSpecialJurnal;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.qdep.form.FRMHandler;

/**
 * @author gedhy
 */
public class SessJurnal {

    public static final String SESS_JURNAL_MAIN = "SESS_JURNAL_UMUM";

    /*
     * This method used to check if transaction date already valid or not
     */
    public static boolean isValidTransactionDate(Date transactionDate, Date entryDate, Date startDate, Date dueDate) {
        if (transactionDate.compareTo(startDate) == 0 || transactionDate.compareTo(dueDate) == 0 || (transactionDate.after(startDate) && transactionDate.before(dueDate))) {
            if (entryDate.compareTo(transactionDate) == 0 || entryDate.after(transactionDate)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean checkTransactionDate(Date transDate){
        Periode objPeriod = new Periode();
        long lPeriodId = 0;
        Date startDate = new Date();
        Date endDate = new Date();
        Date endEntryDate = new Date();
        Date lastPeriod = new Date();
        Date lastEntryDate = new Date();
        try{
            lPeriodId = PstPeriode.getCurrPeriodId();
            if(lPeriodId != 0){
                objPeriod = PstPeriode.fetchExc(lPeriodId);
               if(objPeriod != null){
                    startDate = (Date)objPeriod.getTglAwal();
                    endDate = (Date)objPeriod.getTglAkhir();
                    endEntryDate = (Date)objPeriod.getTglAkhirEntry();
                    lastPeriod = (Date)endDate.clone();
                    int iDate = lastPeriod.getDate();
                    lastPeriod.setDate(iDate - 1);
                    lastEntryDate = (Date)endEntryDate.clone();
                    int iLastDate = lastEntryDate.getDate();
                    lastEntryDate.setDate(iLastDate + 1);
                }
            }
        }catch(Exception e){}
        if(transDate.after(startDate) && transDate.before(lastEntryDate)){
           return false;
        }else{
           return true;
        }
    }

    /**
     * this method used to check if vaoucher no already exist in database
     */
    public static boolean isThereVoucherNo(long periodId, String voucherNo) {
        DBResultSet dbrs = null;
        boolean result = false;
        int jum = 0;
        try {
            String sql = "SELECT COUNT(" + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] + ") " +
                    " AS " + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                    " FROM " + PstJurnalUmum.TBL_JURNAL_UMUM +
                    " WHERE " + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID] +
                    " = " + periodId +
                    " AND " + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_VOUCHER] +
                    " = \"" + voucherNo + "\"";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                jum = rs.getInt(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]);
            }

            if (jum > 0) {
                result = true;
            }
        } catch (Exception e) {
            System.out.println("SessJurnalUmum.isThereVoucherNo() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * this method used to get prefiks Voucher Number 
     */
    public static String generatePrefiksVoucher(java.util.Date transactionDate) {
        return intToStr(transactionDate.getYear(), 2) + intToStr(transactionDate.getMonth() + 1, 2);
    }

    /**
     * this method used to generate String comparison of input int
     */
    public static String intToStr(int intToString, int maxLength) {
        String result = String.valueOf(intToString);
        if (result.length() < maxLength) {
            String temp = "";
            for (int i = 0; i < (maxLength - result.length()); i++) {
                temp = temp + "0";
            }
            result = temp + result;
        }

        if (result.length() > maxLength) {
            result = result.substring(result.length() - maxLength);
        }
        return result;
    }


    /**
     * this method used to syncronized voucher number to its transaction date
     */
    public static Vector getUnBalanceJournal(long periodId) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                    ", JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_VOUCHER] +
                    ", JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_COUNTER] +
                    ", JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_KETERANGAN] +
                    ", IF(SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET] + ") " +
                    "= SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT] + "),0,1) AS BALANCE " +
                    ", SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET] + ") " +
                    ", SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT] + ") " +
                    " FROM " + PstJurnalUmum.TBL_JURNAL_UMUM + " AS JU " +
                    " INNER JOIN " + PstJurnalDetail.TBL_JURNAL_DETAIL + " AS JD " +
                    " ON JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                    " = JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID] +
                    " WHERE JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID] +
                    " = " + periodId +
                    " GROUP BY JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                // sum(debet) <> sum(kredit)
                if (rs.getInt(5) == 1) {
                    Vector vectTemp = new Vector(1, 1);
                    JurnalUmum ju = new JurnalUmum();
                    JurnalDetail jd = new JurnalDetail();

                    ju.setOID(rs.getLong(1));
                    ju.setVoucherNo(rs.getString(2));
                    ju.setVoucherCounter(rs.getInt(3));
                    ju.setKeterangan(rs.getString(4));
                    vectTemp.add(ju);

                    jd.setDebet(rs.getDouble(6));
                    jd.setKredit(rs.getDouble(7));
                    vectTemp.add(jd);

                    result.add(vectTemp);
                }
            }
        } catch (Exception e) {
            System.out.println("SessJurnalUmum.getUnBalanceJournal() exc : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }


    // This method used to search journal with parameter below
    public static Vector listJurnalUmum(SrcJurnalUmum srcJurnalUmum, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        if (srcJurnalUmum == null) {
            srcJurnalUmum = new SrcJurnalUmum();
        }

        try {
            Date currStartDate = null;
            Date currDueDate = null;
            Vector currDate = PstPeriode.getCurrPeriod();
            if (currDate.size() == 1) {
                Periode per = (Periode) currDate.get(0);
                currStartDate = per.getTglAwal();
                currDueDate = per.getTglAkhir();
            }

            String sql = "SELECT DISTINCT " +
                    " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] + 
                    ", JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_VOUCHER] +
                    ", JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_COUNTER] +
                    ", JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLENTRY] +
                    ", JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI] +
                    ", JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_KETERANGAN] +
                    ", JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_REFERENCE_DOCUMENT] +
                    ", JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_NUMBER] +
                    //", AC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    ", CONT." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CONT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", PE." + PstPeriode.fieldNames[PstPeriode.FLD_NAMA] +
                    ", USR." + PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID] + 
                    //", AC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    //", JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JDETAILID]+
                    " FROM " + PstJurnalUmum.TBL_JURNAL_UMUM + " AS JU " +
                    " INNER JOIN " + PstPeriode.TBL_PERIODE + " AS PE ON " +
                    " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID] + " = " +
                    " PE." + PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE] +
                    " INNER JOIN " + PstAppUser.TBL_APP_USER + " AS USR ON " +
                    " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_USERID] + " = " +
                    " USR." + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] +
                    " INNER JOIN " + PstJurnalDetail.TBL_JURNAL_DETAIL + " AS JD ON " +
                    " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] + " = " +
                    " JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID] +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS AC ON " +
                    " AC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] + " = " +
                    " JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] +
                    " LEFT JOIN " + PstContactList.TBL_CONTACT_LIST + " CONT ON " +
                    " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_CONTACT_ID] + " = " +
                    " CONT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]; 

            String datecondition = "";
            if (srcJurnalUmum.getDateStatus() == FrmSrcJurnalUmum.DATEPARAM_PERIOD) {
                String startDate = Formater.formatDate(currStartDate, "yyyy-MM-dd");
                String endDate = Formater.formatDate(currDueDate, "yyyy-MM-dd");
                datecondition = "(JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI] + " BETWEEN '" + startDate + "' AND '" + endDate + "') ";
            }

            if (srcJurnalUmum.getDateStatus() == FrmSrcJurnalUmum.DATEPARAM_DATE) {
                String startDate = Formater.formatDate(srcJurnalUmum.getStartDate(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcJurnalUmum.getEndDate(), "yyyy-MM-dd");
                datecondition = "(JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI] + " BETWEEN '" + startDate + "' AND '" + endDate + "') ";
            }

            String vouchercondition = "";
            String countercondition = "";
            if (srcJurnalUmum.getStrJournalNumber() != null && srcJurnalUmum.getStrJournalNumber().length() > 0) {
               
                if(srcJurnalUmum.getStrJournalNumber() != null && srcJurnalUmum.getStrJournalNumber().length() > 0){
                    vouchercondition = "(JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_NUMBER] + " LIKE '%" + srcJurnalUmum.getStrJournalNumber() + "%')";
                }
            }

            String refdoccondition = "";
            if (srcJurnalUmum.getInvoice() != null && srcJurnalUmum.getInvoice().length() > 0) {
                refdoccondition = "(JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_REFERENCE_DOCUMENT] + " LIKE '%" + srcJurnalUmum.getInvoice() + "%')";
            }

            String operatorcondition = "";
            if (srcJurnalUmum.getOperatorId() != 0) {
                operatorcondition = "(USR." + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] + " = " + srcJurnalUmum.getOperatorId() + ")";
            }

            String contact_id = "";
            if (srcJurnalUmum.getContactOid() != 0) {
                contact_id = "(JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_CONTACT_ID] + " = " + srcJurnalUmum.getContactOid() + ")";
            }

            String ordercondition = "";
            switch (srcJurnalUmum.getOrderBy()) {
                case FrmSrcJurnalUmum.SORT_BY_VOUCHER:
                    ordercondition = " ORDER BY JU." + FrmSrcJurnalUmum.sortFieldKey[FrmSrcJurnalUmum.SORT_BY_VOUCHER] +
                            ", JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_COUNTER];                            
                    break;

                case FrmSrcJurnalUmum.SORT_BY_DATE:
                    ordercondition = " ORDER BY JU." + FrmSrcJurnalUmum.sortFieldKey[FrmSrcJurnalUmum.SORT_BY_DATE];
                    break;

                case FrmSrcJurnalUmum.SORT_BY_OPERATOR:
                    ordercondition = " ORDER BY USR." + FrmSrcJurnalUmum.sortFieldKey[FrmSrcJurnalUmum.SORT_BY_OPERATOR];
                    break;
            }
            

            String allCondition = "";
            if ((datecondition != null) && (datecondition.length() > 0)) {
                allCondition = datecondition;
            }

            if ((vouchercondition != null) && (vouchercondition.length() > 0)) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + vouchercondition;
                } else {
                    allCondition = vouchercondition;
                }
            }    
           

            if ((refdoccondition != null) && (refdoccondition.length() > 0)) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + refdoccondition;
                } else {
                    allCondition = refdoccondition;
                }
            }

            if ((operatorcondition != null) && (operatorcondition.length() > 0)) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + operatorcondition;
                } else {
                    allCondition = operatorcondition;
                }
            }

            if ((contact_id != null) && (contact_id.length() > 0)) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + contact_id;
                } else {
                    allCondition = contact_id;
                }
            }

            if (allCondition.length() > 0) {
                sql = sql + " WHERE  " + allCondition + ordercondition;
            } else {
                sql = sql + " " + ordercondition;
            }

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
	    
	    System.out.println("================>DBHandler.DBSVR_TYPE : " + DBHandler.DBSVR_TYPE); 

            System.out.println("================>sql listJurnalUmum : " + sql); 
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector vect = new Vector(1, 1);
                JurnalUmum jurnalUmum = new JurnalUmum();
                Periode periode = new Periode();
                AppUser appUser = new AppUser();
                ContactList cont = new ContactList();
                Perkiraan perk = new Perkiraan(); 
                
                
                jurnalUmum.setOID(rs.getLong(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID])); 
                jurnalUmum.setVoucherNo(rs.getString(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_VOUCHER]));
                jurnalUmum.setVoucherCounter(rs.getInt(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_COUNTER]));
                jurnalUmum.setTglEntry(rs.getDate(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLENTRY]));
                jurnalUmum.setTglTransaksi(rs.getDate(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI]));
                jurnalUmum.setKeterangan(rs.getString(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_KETERANGAN]));
                jurnalUmum.setReferenceDoc(rs.getString(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_REFERENCE_DOCUMENT]));
                jurnalUmum.setSJurnalNumber(rs.getString(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_NUMBER]));
                
                
                String whDetail = PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID] + "=" + jurnalUmum.getOID();
                String ordDetail = PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET]+ " DESC ";
                Vector listJournalDetail = PstJurnalDetail.listWithAccountObj(0, 0, whDetail, ordDetail);
                if (listJournalDetail != null && listJournalDetail.size() > 0) {
                    int iDetailCount = listJournalDetail.size();
                    for (int i = 0; i < iDetailCount; i++) {
                        JurnalDetail objJurnalDetail = (JurnalDetail) listJournalDetail.get(i);
                        jurnalUmum.addDetails(i, objJurnalDetail);
                    }
                }
                vect.add(jurnalUmum);

                periode.setNama(rs.getString(PstPeriode.fieldNames[PstPeriode.FLD_NAMA]));
                vect.add(periode);

                appUser.setLoginId(rs.getString(PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID]));
                vect.add(appUser);
                
                //Jika nama perusahaan tidak ditemukan, maka yang ditampilkan adalah nama orangnya.
                String compName = "";
                String contact = "-";
                String personName = "";                
                personName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]);
                compName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
                 
                 
                if(compName != null  && compName.length() > 0){
                    cont.setCompName(compName);                    
                }else{
                    if(personName != null && personName.length() > 0){
                    cont.setPersonName(personName);                    
                    }else{
                        cont.setCompName(contact);
                    }
                } 
                
                vect.add(cont);  
                              
                result.add(vect);
            }            
            
        } catch (Exception e) {
            System.out.println("Exception on list jurnal umum ====> "+e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }


    // This method used to get journal count with parameter same as search method above
    public static int getCount(SrcJurnalUmum srcJurnalUmum) {
        DBResultSet dbrs = null;

        int count = 0;
        try {
            Date currStartDate = null;
            Date currDueDate = null;
            Vector currDate = PstPeriode.getCurrPeriod();
            if (currDate.size() == 1) {
                Periode per = (Periode) currDate.get(0);
                currStartDate = per.getTglAwal();
                currDueDate = per.getTglAkhir();
            }

            String sql = "SELECT COUNT(DISTINCT JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] + ")" +
                    " FROM " + PstJurnalUmum.TBL_JURNAL_UMUM + " AS JU " +
                    " INNER JOIN " + PstPeriode.TBL_PERIODE + " AS PE ON " +
                    " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID] + " = " +
                    " PE." + PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE] +
                    " INNER JOIN " + PstAppUser.TBL_APP_USER + " AS USR ON " +
                    " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_USERID] + " = " +
                    " USR." + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] +
                    " INNER JOIN " + PstJurnalDetail.TBL_JURNAL_DETAIL + " AS JD ON " +
                    " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] + " = " +
                    " JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID]+
                    " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS PRK "+
                    " ON JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+" = "+
                    " PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN];

            String datecondition = "";
            if (srcJurnalUmum.getDateStatus() == 0) {
                String startDate = Formater.formatDate(currStartDate, "yyyy-MM-dd");
                String endDate = Formater.formatDate(currDueDate, "yyyy-MM-dd");
                datecondition = "(JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI] + " BETWEEN '" + startDate + "' AND '" + endDate + "') ";
            }

            if (srcJurnalUmum.getDateStatus() == 2) {
                String startDate = Formater.formatDate(srcJurnalUmum.getStartDate(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcJurnalUmum.getEndDate(), "yyyy-MM-dd");
                datecondition = "(JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI] + " BETWEEN '" + startDate + "' AND '" + endDate + "') ";
            }

            String vouchercondition = "";
            String countercondition = "";
             if (srcJurnalUmum.getStrJournalNumber() != null && srcJurnalUmum.getStrJournalNumber().length() > 0) {
               
                if(srcJurnalUmum.getStrJournalNumber() != null && srcJurnalUmum.getStrJournalNumber().length() > 0){
                    vouchercondition = "(JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_NUMBER] + " ILIKE '%" + srcJurnalUmum.getStrJournalNumber() + "%')";
                }
            }

            String refdoccondition = "";
            if (srcJurnalUmum.getInvoice() != null && srcJurnalUmum.getInvoice().length() > 0) {
                refdoccondition = "(JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_REFERENCE_DOCUMENT] + " ILIKE '%" + srcJurnalUmum.getInvoice() + "%')";
            }

            
            String operatorcondition = "";
            if (srcJurnalUmum.getOperatorId() != 0) {
                operatorcondition = "(USR." + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] + " = " + srcJurnalUmum.getOperatorId() + ")";
            }

            String contact_id = "";
            if (srcJurnalUmum.getContactOid() != 0) {
                contact_id = "(JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_CONTACT_ID] + " = " + srcJurnalUmum.getContactOid() + ")";
            }

            String ordercondition = "";
            switch (srcJurnalUmum.getOrderBy()) {
                case FrmSrcJurnalUmum.SORT_BY_VOUCHER:
                    ordercondition = " ORDER BY JU." + FrmSrcJurnalUmum.sortFieldKey[FrmSrcJurnalUmum.SORT_BY_VOUCHER] +
                            ", JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_COUNTER];
                    break;

                case FrmSrcJurnalUmum.SORT_BY_DATE:
                    ordercondition = " ORDER BY JU." + FrmSrcJurnalUmum.sortFieldKey[FrmSrcJurnalUmum.SORT_BY_DATE];
                    break;

                case FrmSrcJurnalUmum.SORT_BY_OPERATOR:
                    ordercondition = " ORDER BY USR." + FrmSrcJurnalUmum.sortFieldKey[FrmSrcJurnalUmum.SORT_BY_OPERATOR];
                    break;
            }

            String allCondition = "";
            if ((datecondition != null) && (datecondition.length() > 0)) {
                allCondition = datecondition;
            }

            if ((vouchercondition != null) && (vouchercondition.length() > 0)) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + vouchercondition;
                } else {
                    allCondition = vouchercondition;
                }
            }

            if ((countercondition != null) && (countercondition.length() > 0)) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + countercondition;
                } else {
                    allCondition = countercondition;
                }
            }

            if ((operatorcondition != null) && (operatorcondition.length() > 0)) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + operatorcondition;
                } else {
                    allCondition = operatorcondition;
                }
            }

            if ((contact_id != null) && (contact_id.length() > 0)) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + contact_id;
                } else {
                    allCondition = contact_id;
                }
            }
            
            if ((refdoccondition != null) && (refdoccondition.length() > 0)) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + refdoccondition;
                } else {
                    allCondition = refdoccondition;
                }
            }
            
            if (allCondition.length() > 0) {
                sql = sql + " WHERE  " + allCondition;
            }

            //System.out.println("================>sql getCount : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }


    // This method used to check if account exist or not yet, -1 if not exist yet
    public static int isAccountExist(Vector vectJurDetail, JurnalDetail jurDetail) {
        if (!vectJurDetail.isEmpty()) {
            for (int i = 0; i < vectJurDetail.size(); i++) {
                JurnalDetail jDetail = (JurnalDetail) vectJurDetail.get(i);
                if (jDetail.getIdPerkiraan() == jurDetail.getIdPerkiraan()) {
                    return jDetail.getIndex();
                }
            }
        }
        return -1;
    }


    // This method used to get total amount for Debet and Credit side
    public static double getBalanceSide(Vector vectDetails, int side) {
        if ((vectDetails != null) && (vectDetails.size() > 0)) {
            double debit = 0;
            double credit = 0;
            for (int i = 0; i < vectDetails.size(); i++) {
                JurnalDetail jurDetail = (JurnalDetail) vectDetails.get(i);
                debit = debit + jurDetail.getDebet();
                credit = credit + jurDetail.getKredit();
                if (jurDetail.getDetailLinks().size() > 0) {
                    int size = jurDetail.getDetailLinks().size();
                    for (int j = 0; j < size; j++) {
                        JurnalDetail dLink = jurDetail.getDetailLink(j);
                        debit = debit + dLink.getDebet();
                        credit = credit + dLink.getKredit();
                    }
                }
            }

            if (side == PstJurnalDetail.SIDE_DEBET) {
                return debit;
            } else {
                return credit;
            }
        } else {
            return 0;
        }
    }


    // This method used to check if journal have balance or haven't yet
    public static boolean checkBalance(Vector vectDetails) {
        if ((vectDetails != null) && (vectDetails.size() > 0)) {
            double debit = 0;
            double credit = 0;
            for (int i = 0; i < vectDetails.size(); i++) {
                JurnalDetail jurDetail = (JurnalDetail) vectDetails.get(i);
                debit = debit + jurDetail.getDebet();
                credit = credit + jurDetail.getKredit();
                if (jurDetail.getDetailLinks().size() > 0) {
                    int size = jurDetail.getDetailLinks().size();
                    for (int j = 0; j < size; j++) {
                        JurnalDetail dLink = jurDetail.getDetailLink(j);
                        debit = debit + dLink.getDebet();
                        credit = credit + dLink.getKredit();
                    }
                }
            }

            String strDebet = FRMHandler.userFormatStringDecimal(debit);
            String strCredit = FRMHandler.userFormatStringDecimal(credit);
            if (strDebet.equals(strCredit)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    // This method used to check if journal have balance or heven't yet
    public static boolean checkBalance(long oid) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + "JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET] +
                    " , JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT] +
                    " , JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                    " FROM " + PstJurnalUmum.TBL_JURNAL_UMUM + " AS JU " +
                    " INNER JOIN " + PstJurnalDetail.TBL_JURNAL_DETAIL + " AS JD ON " +
                    " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                    " = JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID] +
                    " AND JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                    " = " + oid;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            double debt = 0;
            double credit = 0;
            double result = 0;
            while (rs.next()) {
                debt = debt + rs.getDouble(1);
                credit = credit + rs.getDouble(2);
            }
            result = debt - credit;

            if (result == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    // This method used to check if journal have amount or haven't yet
    public static boolean checkAmount(long oid) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT " + "JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET] +
                    " , JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT] +
                    " , JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                    " FROM " + PstJurnalUmum.TBL_JURNAL_UMUM + " AS JU " +
                    " INNER JOIN " + PstJurnalDetail.TBL_JURNAL_DETAIL + " AS JD ON " +
                    " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                    " = JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID] +
                    " AND JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                    " = " + oid;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            double debt = 0;
            double credit = 0;
            while (rs.next()) {
                debt = debt + rs.getDouble(1);
                credit = credit + rs.getDouble(2);
            }

            if (debt > 0 || credit > 0) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * ngambil saldo account dari jurnal berdasar OID perkiraan dan OID periode
     *
     * @param lAccountOid
     * @param lPeriodOid
     * @return x jika saldonya plus di normalsign-nya; -x jika saldonya min di normalsign-nya
     */
    public double getAccountBalance(long lAccountOid, Vector vJournalType, long lPeriodOid) {
        double dResult = 0;

        String sJurnalType = "";
        if (vJournalType != null && vJournalType.size() > 0) {
            int iJournalTypeCount = vJournalType.size();
            for (int i = 0; i < iJournalTypeCount; i++) {
                sJurnalType = sJurnalType + "JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE] +
                        " = " + vJournalType.get(i) + " OR ";
            }

            if (sJurnalType != null && sJurnalType.length() > 4) {
                sJurnalType = "(" + sJurnalType.substring(0, sJurnalType.length() - 4) + ")";
            }
        }

        DBResultSet dbrs = null;
        try {
            String sql = "SELECT ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    " , SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET] + ") AS DEBET" +
                    " , SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT] + ") AS KREDIT" +
                    " FROM " + PstJurnalDetail.TBL_JURNAL_DETAIL + " AS JD" +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " INNER JOIN " + PstJurnalUmum.TBL_JURNAL_UMUM + " AS JU" +
                    " ON JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                    " = JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID] +
                    " WHERE JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] +
                    " = " + lAccountOid +
                    " AND JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID] +
                    " = " + lPeriodOid +
                    " AND " + sJurnalType +
                    " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN];

            System.out.println("com.dimata.aiso.session.jurnal.getAccountBalance sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                int iNormalSign = rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]);
                double dDebet = rs.getDouble("DEBET");
                double dKredit = rs.getDouble("KREDIT");
                if (iNormalSign == PstPerkiraan.ACC_DEBETSIGN) {
                    dResult = dDebet - dKredit;
                    return dResult;
                }

                if (iNormalSign == PstPerkiraan.ACC_KREDITSIGN) {
                    dResult = dKredit - dDebet;
                    return dResult;
                }
            }
        } catch (Exception e) {
            System.out.println("Exc when getAccountBalance : " + e.toString());
        }
        return dResult;
    }

    /**
     * methode use to check is the given account id is a general account
     * return 0 if not
     */
    public static int isGeneralAccount(long idPerkiraan) {
        if (idPerkiraan > 0) {
            String where = PstPerkiraan.fieldNames[PstPerkiraan.FLD_GENERAL_ACCOUNT_LINK] +
                    " = " + idPerkiraan + " AND " +
                    PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " <> " + idPerkiraan;
            return PstPerkiraan.getCount(where);
        } else {
            return 0;
        }
    }

    /**
     * methode use to get all linked account for given general account id
     */
    public static Vector getLinkAccount(long idPerkiraan) {
        if (idPerkiraan > 0) {
            String where = PstPerkiraan.fieldNames[PstPerkiraan.FLD_GENERAL_ACCOUNT_LINK] +
                    " = " + idPerkiraan + " AND " +
                    PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " <> " + idPerkiraan;
            return PstPerkiraan.list(0, 0, where, "");
        } else {
            return new Vector();
        }
    }

    /**
     * return true if the total of percentage of each account on vector of link account
     * is 100%, return false otherwise
     */
    public static boolean isTotalPercentageOk(Vector listAccount) {
        boolean result = false;
        if (listAccount != null) { // avoiding the NullPointerException
            int size = listAccount.size();
            Perkiraan perkiraan = new Perkiraan();
            double totalPercentage = 0;
            for (int i = 0; i < size; i++) {
                perkiraan = (Perkiraan) listAccount.get(i);
                totalPercentage = totalPercentage + perkiraan.getWeight();
            }
            if (totalPercentage == 100) {
                result = true;
            }
        }
        return result;
    }

    public static Hashtable getAllGeneralAccount() {
        Hashtable hashResult = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT DISTINCT " + PstPerkiraan.fieldNames[PstPerkiraan.FLD_GENERAL_ACCOUNT_LINK] +
                    " FROM " + PstPerkiraan.TBL_PERKIRAAN;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                String sOid = rs.getString(1);
                hashResult.put(sOid, sOid);
            }

        } catch (Exception e) {
            System.out.println("err on getAllGeneralAccount(): " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return hashResult;
    }
    
    public static void main(String[] arg){
        SessJurnal objSessJurnal = new SessJurnal();
        Date transDate = new Date();
       // String prefikVoucher = objSessJurnal.generatePrefiksVoucher(transDate);
        //System.out.println("prefikVoucher :::: "+prefikVoucher);
    }
}
