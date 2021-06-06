/*
 * SessSpecialJurnal.java
 *
 * Created on February 7, 2007, 2:03 PM
 */

package com.dimata.aiso.session.specialJournal;
 
/* import package java util */
import java.util.*;
import java.util.Date;

/* import package java sql */
import java.sql.*;

/* import package aiso */
import com.dimata.aiso.db.*;
import com.dimata.aiso.entity.specialJournal.*;
import com.dimata.aiso.entity.masterdata.*;
import com.dimata.aiso.entity.admin.*;
import com.dimata.aiso.entity.jurnal.PstJurnalDetail;
import com.dimata.aiso.entity.jurnal.PstJurnalUmum;
import com.dimata.aiso.entity.periode.*;
import com.dimata.aiso.entity.search.*;
import com.dimata.aiso.form.search.*;
import com.dimata.aiso.session.masterdata.SessActivity;
import com.dimata.aiso.session.report.SessWorkSheet;

/* import package qdep form */
import com.dimata.qdep.form.FRMHandler;

/* import package dimata util */
import com.dimata.util.*;

/* import package dimata common */
import com.dimata.common.entity.contact.*;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.interfaces.chartofaccount.I_ChartOfAccountGroup;
import com.dimata.interfaces.journal.I_JournalType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author  dwi
 */
public class SessSpecialJurnal {
    
   public static final String SESS_SPECIAL_JOURNAL = "SESS_SPECIAL_JOURNAL";
   public static final String SESS_SPECIAL_JOURNAL_DETAIL = "SESS_SPECIAL_JOURNAL_DETAIL";
   
   /**
    * This method is used to check validity of transaction date
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
     
     /**
      * This method is used to make sure there is no double voucher data on database.      * 
      */
     public static boolean isThereVoucherNo(long periodId, String voucherNo) {
        DBResultSet dbrs = null;
        boolean result = false;
        int amountId = 0;
        try {
            String sql = "SELECT COUNT(" + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID] + ") " +
                    " AS " + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID] +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN +
                    " WHERE " + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + periodId +
                    " AND " + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_NUMBER] +
                    " = \"" + voucherNo + "\"";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                amountId = rs.getInt(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]);
            }

            if (amountId > 0) {
                result = true;
            }
        } catch (Exception e) {
            System.out.println("SessJurnalUmum.isThereVoucherNo() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
     
     /*
      * This method is used to get max voucher counter
      **/
      public static int getLastCounter(long periodId) {
        DBResultSet dbrs = null;
        int result = 0;
        try {
            String sql = "SELECT MAX(" + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_VOUCHER_COUNTER] + ") " +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN +
                    " WHERE " + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + periodId;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getInt(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("SessJurnalUmum.getLastCounter() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
      
      /**
     * This method used to get prefiks Voucher Number 
     */
    public static String generatePrefiksVoucher(Date transactionDate) {
        return getLocationCode() +"-"+intToStr(transactionDate.getYear(), 2) + intToStr(transactionDate.getMonth() + 1, 2);
    }
    
    /**
     *return String location code
     *To get location code from system property
     */
    public static String getLocationCode(){
        String locCode = "";
        try{
            locCode = PstSystemProperty.getValueByName("LOC_CODE");
        }catch(Exception e){
            locCode = "";
            System.out.println("Exception on getLocationCode() ::: "+e.toString());
        }
        return locCode;
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
     * This method used to generate journal number
     */
    public static String generateVoucherNumber(long periodId, Date transactionDate) {
        if (periodId != 0) {
            String result = "";
            int lastCounter = 0;
            String strPrefiks = generatePrefiksVoucher(transactionDate);
            if (PstPeriode.getCurrPeriodId() == periodId) {
                lastCounter = getLastCounter(periodId);                
                result = strPrefiks + "-" + intToStr((lastCounter + 1), 4);                
            } else {
                result = strPrefiks + "-0001";
            }
            return result;
        } else {
            return "";
        }
    } 

    
    /**
     * This method is used to checked balance between debet side and kredit side 
     */
    public static Vector getUnBalanceJournal(long periodId) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID] +
                    ", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_VOUCHER_NUMBER] +
                    ", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_VOUCHER_COUNTER] +
                    ", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_DESCRIPTION] +                    
                    ", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] +
                    ", SUM(JD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT] + ") " +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM " +
                    " INNER JOIN " + PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL + " AS JD " +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID] +
                    " = JD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID] +
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + periodId +
                    " GROUP BY JU." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID];
                    
            System.out.println("Sql on method getUnBalanceJournal ==> "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {                
                if (rs.getInt(5) == 1) {
                    Vector vectTemp = new Vector(1, 1);
                    SpecialJournalMain objSpecialJournalMain = new SpecialJournalMain();
                    SpecialJournalDetail objSpecialJournalDetail = new SpecialJournalDetail();

                    objSpecialJournalMain.setOID(rs.getLong(1));
                    objSpecialJournalMain.setVoucherNumber(rs.getString(2));
                    objSpecialJournalMain.setVoucherCounter(rs.getInt(3));
                    objSpecialJournalMain.setDescription(rs.getString(4));
                    objSpecialJournalMain.setAmount(rs.getFloat(6));
                    vectTemp.add(objSpecialJournalMain);
                    
                    objSpecialJournalDetail.setAmount(rs.getFloat(7));
                    vectTemp.add(objSpecialJournalDetail);

                    result.add(vectTemp);
                }
            }
        } catch (Exception e) {
            System.out.println("SessSpecialJournalMain.getUnBalanceJournal() exc : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    public static Vector listJurnalUmum(SrcJurnalUmum srcJurnalUmum, int start, int recordToGet, int journalType, int amountStatus) {
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
                    " JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID] + 
                    ", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_VOUCHER_NUMBER] +
                    ", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_VOUCHER_COUNTER] +
                    ", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ENTRY_DATE] +
                    ", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE] +
                    ", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_DESCRIPTION] +
                    ", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_REFERENCE] +
                    ", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_NUMBER] +
                    ", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] +                    
                    ", CONT." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CONT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", PE." + PstPeriode.fieldNames[PstPeriode.FLD_NAMA] +
                    ", USR." + PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID] +                     
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM " +
                    " INNER JOIN " + PstPeriode.TBL_PERIODE + " AS PE ON " +
                    " JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] + " = " +
                    " PE." + PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE] +
                    " INNER JOIN " + PstAppUser.TBL_APP_USER + " AS USR ON " +
                    " JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_USER_ID] + " = " +
                    " USR." + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] +
                    " INNER JOIN " + PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL + " AS JD ON " +
                    " JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID] + " = " +
                    " JD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID] +                   
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CONT ON " +
                    " JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_CONTACT_ID] + " = " +
                    " CONT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]; 

            String datecondition = "";
            if (srcJurnalUmum.getDateStatus() == FrmSrcJurnalUmum.DATEPARAM_PERIOD) {
                String startDate = Formater.formatDate(currStartDate, "yyyy-MM-dd");
                String endDate = Formater.formatDate(currDueDate, "yyyy-MM-dd");
                datecondition = "(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "') ";
            }

            if (srcJurnalUmum.getDateStatus() == FrmSrcJurnalUmum.DATEPARAM_DATE) {
                String startDate = Formater.formatDate(srcJurnalUmum.getStartDate(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcJurnalUmum.getEndDate(), "yyyy-MM-dd");
                datecondition = "(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "') ";
            }

            String vouchercondition = "";
            String countercondition = "";
            if (srcJurnalUmum.getVoucherNo() != null && srcJurnalUmum.getVoucherNo().length() > 0) {                            
                    vouchercondition = "(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_VOUCHER_NUMBER] + " LIKE '%" + srcJurnalUmum.getVoucherNo() + "%')";               
                
            }

            String refdoccondition = "";
            if (srcJurnalUmum.getInvoice() != null && srcJurnalUmum.getInvoice().length() > 0) {
                refdoccondition = "(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_REFERENCE] + " LIKE '%" + srcJurnalUmum.getInvoice() + "%')";
            }

            String operatorcondition = "";
            if (srcJurnalUmum.getOperatorId() != 0) {
                operatorcondition = "(USR." + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] + " = " + srcJurnalUmum.getOperatorId() + ")";
            }

            String contact_id = "";
            if (srcJurnalUmum.getContactOid() != 0) {
                contact_id = "(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_CONTACT_ID] + " = " + srcJurnalUmum.getContactOid() + ")";
            }
            
            String strJournalType = "";
            if(journalType > 0){
                strJournalType = "(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] + " = " + journalType + ")";
            }
            
            String strAmountStatus = "";
            if(amountStatus != 0){
                strAmountStatus = "(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT_STATUS] + " = " + amountStatus + ")";
            }
            
            String ordercondition = "";
            System.out.println("srcJurnalUmum.getOrderBy() =====> "+srcJurnalUmum.getOrderBy());
            switch (srcJurnalUmum.getOrderBy()) {
                case FrmSrcJurnalUmum.SORT_BY_VOUCHER_NUMBER:
                    ordercondition = " ORDER BY JM." + FrmSrcJurnalUmum.sortFieldKey[FrmSrcJurnalUmum.SORT_BY_VOUCHER_NUMBER];
                            //", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_VOUCHER_COUNTER];
                            
                    break;

                case FrmSrcJurnalUmum.SORT_BY_TRANS_DATE:
                    ordercondition = " ORDER BY JM." + FrmSrcJurnalUmum.sortFieldKey[FrmSrcJurnalUmum.SORT_BY_TRANS_DATE];
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
    
            /*if ((countercondition != null) && (countercondition.length() > 0)) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + countercondition;
                } else {
                    allCondition = countercondition;
                }
            }*/

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
            
            if ((strJournalType != null) && (strJournalType.length() > 0)) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + strJournalType;
                } else {
                    allCondition = strJournalType;
                }
            }
            
             if ((strAmountStatus != null) && (strAmountStatus.length() > 0)) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + strAmountStatus;
                } else {
                    allCondition = strAmountStatus;
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

           System.out.println("Sql ListSpecialJournal =====>" + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector vect = new Vector(1, 1);
                SpecialJournalMain objSpecialJournalMain = new SpecialJournalMain();
                Periode periode = new Periode();
                AppUser appUser = new AppUser();
                ContactList cont = new ContactList();
                Perkiraan perk = new Perkiraan(); 
                
                objSpecialJournalMain.setOID(rs.getLong(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID])); 
                objSpecialJournalMain.setVoucherNumber(rs.getString(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_VOUCHER_NUMBER]));
                objSpecialJournalMain.setVoucherCounter(rs.getInt(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_VOUCHER_COUNTER]));
                objSpecialJournalMain.setEntryDate(rs.getDate(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ENTRY_DATE]));
                objSpecialJournalMain.setTransDate(rs.getDate(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE]));
                objSpecialJournalMain.setDescription(rs.getString(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_DESCRIPTION]));
                objSpecialJournalMain.setReference(rs.getString(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_REFERENCE]));
                objSpecialJournalMain.setJournalNumber(rs.getString(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_NUMBER]));
                objSpecialJournalMain.setAmount(rs.getDouble(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT]));
                
                String whDetail = PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID] + "=" + objSpecialJournalMain.getOID();
                String ordDetail = PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT];
                Vector listJournalDetail = PstSpecialJournalDetail.list(0, 0, whDetail, ordDetail);
                if (listJournalDetail != null && listJournalDetail.size() > 0) {
                    int iDetailCount = listJournalDetail.size();
                    for (int i = 0; i < iDetailCount; i++) {
                        SpecialJournalDetail objSpecialJournalDetail = (SpecialJournalDetail) listJournalDetail.get(i);
                        objSpecialJournalMain.addDetails(i, objSpecialJournalDetail);
                    }
                }
                vect.add(objSpecialJournalMain);

                periode.setNama(rs.getString(PstPeriode.fieldNames[PstPeriode.FLD_NAMA]));
                vect.add(periode);

                appUser.setLoginId(rs.getString(PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID]));
                vect.add(appUser);
                
                //Jika nama perusahaan tidak ditemukan, maka yang ditampilkan adalah nama orangnya.
                String kontak = rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
                
                if(kontak.length() > 0 && kontak != null){
                    cont.setCompName(kontak);
                }else{
                    cont.setPersonName(rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]));
                }
                
                vect.add(cont);             
                
                
                              
                result.add(vect);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
        public static Vector listPayable(String strWhClause) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        
        try {
            String sql = "SELECT UN.VOUCHER_NUMBER AS VOUCHER_NUMBER, UN.TRANS_DATE AS TRANS_DATE, " +
                    " UN.DESCRIPTION AS DESCRIPTION, UN.REFERENCE AS REFERENCE, UN.AMOUNT AS AMOUNT, " +
                    " UN.ID_PERKIRAAN AS ID_PERKIRAAN, UN.CONTACT_ID AS CONTACT_ID, UN.PERSON_NAME AS PERSON_NAME, " +
                    " UN.COMP_NAME AS COMP_NAME FROM( "+
                    " SELECT DISTINCT " +
                    "  JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_VOUCHER_NUMBER] +" AS VOUCHER_NUMBER "+     
                    ", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE] +" AS TRANS_DATE "+
                    ", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_DESCRIPTION] +" AS DESCRIPTION "+
                    ", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_REFERENCE] +" AS REFERENCE "+                    
                    ", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] +" AS AMOUNT "+
                    ", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN] +" AS ID_PERKIRAAN "+
                    ", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_CONTACT_ID] +" AS CONTACT_ID "+
                    ", CONT." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +" AS PERSON_NAME "+
                    ", CONT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +" AS COMP_NAME "+                                      
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM " +                                       
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " CONT ON " +
                    " JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_CONTACT_ID] + " = " +
                    " CONT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]+
                    " WHERE JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]+
                    " = "+I_JournalType.TIPE_SPECIAL_JOURNAL_NON_CASH +
                    " AND JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_REFERENCE]+
                    " NOT IN( SELECT SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_DESCRIPTION]+
                    " FROM "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+
                    " INNER JOIN "+PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS SM "+
                    " ON SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+" = "+
                    " SM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " WHERE SM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]+
                    " = "+I_JournalType.TIPE_SPECIAL_JOURNAL_PAYMENT+" )"+
                    " UNION "+
                    " SELECT DISTINCT JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_NUMBER]+" AS VOUCHER_NUMBER "+
                    ", JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI]+" AS TRANS_DATE "+
                    ", JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_KETERANGAN]+" AS DESCRIPTION "+ 
                    ", JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_REFERENCE_DOCUMENT]+" AS REFERENCE "+ 
                    ", JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT]+" AS AMOUNT "+
                    ", JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+" AS ID_PERKIRAAN "+
                    ", JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_CONTACT_ID]+" AS CONTACT_ID "+
                    ", CONT."+PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]+" AS PERSON_NAME "+
                    ", CONT."+PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+" AS COMP_NAME "+
                    " FROM "+PstJurnalUmum.TBL_JURNAL_UMUM+" AS JU "+
                    " INNER JOIN "+PstContactList.TBL_CONTACT_LIST+" AS CONT "+
                    " ON JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_CONTACT_ID]+" = CONT."+PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]+
                    " INNER JOIN "+PstJurnalDetail.TBL_JURNAL_DETAIL+" AS JD "+
                    " ON JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]+" = JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID]+
                    " LEFT JOIN "+PstAccountLink.TBL_ACCOUNT_LINK+" AS LK "+
                    " ON LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+" = JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+
                    " WHERE LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE]+" = "+ I_ChartOfAccountGroup.ACC_GROUP_CURRENCT_LIABILITIES +
                    " AND JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT]+" > 0 "+
                    " AND JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_REFERENCE_DOCUMENT]+
                    " NOT IN( SELECT SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_DESCRIPTION]+
                    " FROM "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+
                    " INNER JOIN "+PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS SM "+
                    " ON SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+" = "+
                    " SM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " WHERE SM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]+
                    " = "+I_JournalType.TIPE_SPECIAL_JOURNAL_PAYMENT+" )) AS UN ";
            
            if(strWhClause != null && strWhClause.length() > 0){
                sql += strWhClause;
            }
            
            System.out.println("SQL SessSpecialJurnal Payable List ::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector vect = new Vector(1, 1);
                SpecialJournalMain objSpecialJournalMain = new SpecialJournalMain();               
                ContactList cont = new ContactList();
                Perkiraan perk = new Perkiraan(); 
                
                long lContactId = rs.getLong("CONTACT_ID");
               
                objSpecialJournalMain.setVoucherNumber(rs.getString("VOUCHER_NUMBER")); 
                objSpecialJournalMain.setTransDate(rs.getDate("TRANS_DATE"));
                objSpecialJournalMain.setDescription(rs.getString("DESCRIPTION"));
                objSpecialJournalMain.setReference(rs.getString("REFERENCE"));                
                objSpecialJournalMain.setAmount(rs.getDouble("AMOUNT"));
                objSpecialJournalMain.setIdPerkiraan(rs.getLong("ID_PERKIRAAN"));               
                objSpecialJournalMain.setContactId(lContactId);                
                
                vect.add(objSpecialJournalMain);
                
                //Jika nama perusahaan tidak ditemukan, maka yang ditampilkan adalah nama orangnya.
                String kontak = rs.getString("COMP_NAME");
                
                if(kontak.length() > 0 && kontak != null){
                    cont.setCompName(kontak);
                }else{
                    cont.setPersonName(rs.getString("PERSON_NAME"));
                }
                
                vect.add(cont);     
                result.add(vect);
            }
            
            System.out.println("result.size() =====> "+result.size());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
     // This method is used to checked chart account to make sure there is no double account
    public static int isAccountExist(Vector vectJurDetail, SpecialJournalDetail objSpecialJournalDetail) {
        if (!vectJurDetail.isEmpty()) {
            for (int i = 0; i < vectJurDetail.size(); i++) {
                SpecialJournalDetail objSpcJournalDetail = (SpecialJournalDetail) vectJurDetail.get(i);
                if (objSpcJournalDetail.getIdPerkiraan() == objSpecialJournalDetail.getIdPerkiraan()) {
                    return objSpcJournalDetail.getIndex();
                }
            }
        }
        return -1;
    }
    
     public static boolean checkBalance(long oid) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + "JD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT] +                    
                    " , JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM " +
                    " INNER JOIN " + PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL + " AS JD ON " +
                    " JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID] +
                    " = JD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID] +
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID] +
                    " = " + oid;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            double amounMain = 0;
            double amountDetail = 0;
            double result = 0;
            while (rs.next()) {
                amountDetail = amountDetail + rs.getFloat(1);
                amounMain = rs.getFloat(2);
            }
            result = amountDetail - amounMain;

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

     
     public static double getSaldoAwal(long perkiraanOid){
         DBResultSet dbrs = null;
        double result = 0;
        try {
            String sql = "SELECT LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+                    
                    ", SUM(JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET]+") AS SUMDEBET"+
                    ", SUM(JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT]+") AS SUMKREDIT "+
                    " FROM " + PstAccountLink.TBL_ACCOUNT_LINK + " AS LK " +                    
                    " LEFT JOIN "+PstJurnalDetail.TBL_JURNAL_DETAIL+" AS JD "+
                    " ON LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+
                    " = JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN];
            
            if (perkiraanOid != 0) {
                sql = sql + " WHERE LK." + PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID] +
                " = " + perkiraanOid;
            }
                sql = sql + " group by LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID];                

            System.out.println("SQL HITUNG SALDO AWAL ====> "+sql);    
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Perkiraan objPerkiraan = new Perkiraan();
                double totalDebet = rs.getDouble("SUMDEBET");
                double totalKredit = rs.getDouble("SUMKREDIT");
                objPerkiraan = PstPerkiraan.fetchExc(rs.getLong(PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]));
                if(objPerkiraan.getTandaDebetKredit() == PstPerkiraan.ACC_DEBETSIGN)
                    result = totalDebet - totalKredit;
                else
                    result = totalKredit - totalDebet;                
            }
            
            System.out.println("saldoAwal =======> "+result);
            
        } catch (Exception e) {
            System.out.println("---> PstAccountLink.getSaldoAwal err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
     }
     
     public static double getReplacement(long perkiraanOid){
         DBResultSet dbrs = null;
        double result = 0;
        try {
            String sql = "SELECT LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+                    
                    ", SUM(JD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT]+
                    " * JD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT_STATUS]+") AS SUMDETAIL "+
                    " FROM " + PstAccountLink.TBL_ACCOUNT_LINK + " AS LK " +                    
                    " LEFT JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS JD "+
                    " ON LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+
                    " = JD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN];
            
            if (perkiraanOid != 0) {
                sql = sql + " WHERE LK." + PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID] +
                " = " + perkiraanOid;
            }
                sql = sql + " group by LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID];                

            System.out.println("SQL HITUNG REPLACEMENT ====> "+sql);    
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                
                    result = rs.getDouble("SUMDETAIL");
                             
            }
            
            
            System.out.println("replacement =======> "+result);
            
        } catch (Exception e) {
            System.out.println("---> PstAccountLink.getSaldoAwal err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
     }
     
    public static Vector getTotalSaldoAccountLink(long lPeriodId, long idPerkiraan) {
        long preOidPeriod = 0;
        try{
            preOidPeriod = SessWorkSheet.getOidPeriodeLalu(lPeriodId);
        }catch(Exception e){
            preOidPeriod = 0;
            System.out.println("Exception on getPerOidPeriod ::: "+e.toString());
            e.printStackTrace();
        }
        
        DBResultSet dbrs = null;
        Vector vectResult = new Vector(1, 1);
        try {
           String sql = "SELECT UN.NAMA, UN.IDPERKIRAAN AS ACCOUNTID, SUM(UN.DEBET) AS TOTALDEBET, SUM(UN.KREDIT) AS TOTALKREDIT, SUM(UN.SPECIALJOURNAL) AS SPECIAL, UN.TDK AS TDK FROM ("+
                    "  SELECT PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                    ", LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+" AS IDPERKIRAAN"+
                    ", 0 AS DEBET"+
                    ", 0 AS KREDIT"+
                    ", SUM(SJ."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT]+
                    " * SJ."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT_STATUS]+") AS SPECIALJOURNAL "+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+" AS TDK "+
                    " FROM " + PstAccountLink.TBL_ACCOUNT_LINK + " AS LK " +                    
                    " LEFT JOIN "+ PstSpecialJournalMain.TBL_AISO_SPC_JMAIN+ " AS SJ "+
                    " ON LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+
                    " = SJ."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+
                    " LEFT JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS PRK "+
                    " ON PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+" = "+
                    " LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+
                    " WHERE SJ."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID]+" = "+lPeriodId+
                    " GROUP BY LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                    " UNION "+
                    " SELECT PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    ", LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+                    
                    ", SUM(JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET]+") AS DEBET"+
                    ", SUM(JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT]+") AS KREDIT"+
                    ", 0 AS SPECIALJOURNAL"+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+" AS TDK "+
                    " FROM " + PstAccountLink.TBL_ACCOUNT_LINK + " AS LK " +
                    " LEFT JOIN "+PstJurnalDetail.TBL_JURNAL_DETAIL+" AS JD "+
                    " ON LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+
                    " = JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+
                    " LEFT JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS PRK "+
                    " ON PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+" = "+
                    " LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+
                    " INNER JOIN "+PstJurnalUmum.TBL_JURNAL_UMUM+" AS JU "+
                    " ON JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]+" = "+
                    " JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID]+
                    " WHERE JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID]+" = "+lPeriodId+
                    " AND JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE]+
                    " NOT IN("+I_JournalType.TIPE_JURNAL_PENUTUP_1+", "+I_JournalType.TIPE_JURNAL_PENUTUP_2+
                    ", "+I_JournalType.TIPE_JURNAL_PENUTUP_3+") "+
                    " GROUP BY LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    " UNION "+
                    " SELECT PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    ", LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+
                    ", 0 AS DEBET"+
                    ", 0 AS KREDIT"+
                    ", SUM(ACD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT]+
                    " * ACD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT_STATUS]+") AS SPECIALJOURNAL "+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+" AS TDK "+
                    " FROM " + PstAccountLink.TBL_ACCOUNT_LINK + " AS LK " +   
                    " LEFT JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS ACD "+
                    " ON LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+
                    " = ACD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN]+
                    " LEFT JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS PRK "+
                    " ON PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+" = "+
                    " LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+
                    " INNER JOIN "+PstSpecialJournalMain.TBL_AISO_SPC_JMAIN+ " AS SJM "+
                    " ON SJM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " = ACD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                    " WHERE SJM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID]+" = "+lPeriodId+
                    " GROUP BY LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA];
                    if(preOidPeriod != 0){
                        sql += " UNION "+
                                " SELECT PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                                ", LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+                    
                                ", SUM(EN.DEBET) AS DEBET"+
                                ", SUM(EN.KREDIT) AS KREDIT"+
                                ", 0 AS SPECIALJOURNAL"+
                                ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+" AS TDK "+
                                " FROM " + PstAccountLink.TBL_ACCOUNT_LINK + " AS LK " +
                                " LEFT JOIN AISO_SALDO_AKHIR_PERD AS EN "+
                                " ON LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+
                                " = EN.ID_PERKIRAAN"+
                                " LEFT JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS PRK "+
                                " ON PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+" = "+
                                " LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+
                                " WHERE EN.PERIODE_ID = "+preOidPeriod+           
                                " GROUP BY LK."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+
                                ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+
                                ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                                ") AS UN";  
                    }else{
                        sql += ") AS UN";
                    }
            
                    if(idPerkiraan != 0){
                        sql += " WHERE UN.IDPERKIRAAN = "+idPerkiraan+" GROUP BY UN.IDPERKIRAAN,UN.TDK, UN.NAMA"; 
                    }else{
                        sql += " GROUP BY UN.IDPERKIRAAN,UN.TDK, UN.NAMA"; 
                    }
           
            //System.out.println("SQL HITUNG SALDO AKHIR ====> "+sql);    
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
           
            
            
            while (rs.next()) { 
                Perkiraan objPerkiraan = new Perkiraan();
                Vector temp = new Vector();
                long oidPerkiraan = 0; 
                double total = 0;
                double debet = rs.getDouble("TOTALDEBET");
                double kredit = rs.getDouble("TOTALKREDIT");
                double specialJournal = rs.getDouble("SPECIAL");                
                oidPerkiraan = rs.getLong("ACCOUNTID");
                int iTandaDebetKredit = rs.getInt("TDK");
                
                
                if((debet + kredit + specialJournal) != 0 ){
                    objPerkiraan = PstPerkiraan.fetchExc(oidPerkiraan);                
                }
                
                if(iTandaDebetKredit == PstPerkiraan.ACC_DEBETSIGN){
                     total = specialJournal + debet - kredit;    
                }else{                   
                    total = specialJournal + kredit - debet;                
                }
                temp.add(""+oidPerkiraan); 
                temp.add(""+total);                
                vectResult.add(temp);
            }    
        } catch (Exception e) {
            System.out.println("---> PstAccountLink.getTotalSaldoAccountLink() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return vectResult;
    }
    
     public static Vector getTotalSaldoActivity(String whClause, int iStart, int iRecordToGet) {
        DBResultSet dbrs = null;
        Vector vectResult = new Vector(1, 1);
        try {
            String sql = "SELECT UN.IDACTIVITY AS ACTIVITY_ID, ATY.CODE AS CODE, ATY.DESCRIPTION AS DESCRIPTION, ACC.ID_PERKIRAAN AS ID_PERKIRAAN, SUM(UN.BUDGET) AS BUDGET, SUM(UN.ACTUAL) AS ACTUAL FROM("+
                    " SELECT "+PstActivityDonorComponentLink.fieldNames[PstActivityDonorComponentLink.FLD_ACTIVITY_ID]+" AS IDACTIVITY"+
                    ", SUM("+PstActivityDonorComponentLink.fieldNames[PstActivityDonorComponentLink.FLD_SHARE_BUDGET]+") AS BUDGET"+
                    ", 0 AS ACTUAL"+
                    " FROM "+PstActivityDonorComponentLink.TBL_AISO_ACTIVITY_ASSIGN+
                    " GROUP BY "+PstActivityDonorComponentLink.fieldNames[PstActivityDonorComponentLink.FLD_ACTIVITY_ID]+
                    " UNION"+
                    " SELECT "+PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_ACTIVITY_ID]+" AS IDACTIVITY"+
                    ", 0 AS BUDGET"+
                    ", SUM("+PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_AMOUNT]+") AS ACTUAL"+                    
                    " FROM " + PstSpecialJournalDetailAssignt.TBL_AISO_JDETAIL_ASSIGNT + 
                    " GROUP BY " + PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_ACTIVITY_ID] + ") AS UN"+
                    " INNER JOIN "+PstActivity.TBL_AISO_ACTIVITY+" AS ATY "+
                    " ON UN.IDACTIVITY = "+PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]+
                    " INNER JOIN "+PstActivityAccountLink.TBL_AISO_ACTIVITY_ACCOUNT+" AS ACC "+
                    " ON ATY."+PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]+" = ACC."+PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACTIVITY_ID]+
                    " WHERE UN.IDACTIVITY IN("+SessActivity.getIDActivity()+")";
                if(whClause != null && whClause.length() > 0){
                    sql += " AND "+whClause+" GROUP BY UN.IDACTIVITY,ATY.CODE,ATY.DESCRIPTION,ACC.ID_PERKIRAAN";
                }else{
                    sql += " GROUP BY UN.IDACTIVITY,ATY.CODE,ATY.DESCRIPTION,ACC.ID_PERKIRAAN";
                }    
           
                switch(DBHandler.DBSVR_TYPE){ 
                case DBHandler.DBSVR_MYSQL:
                    if(iStart == 0 && iRecordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + "LIMIT" +iStart+","+iRecordToGet;               
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if(iStart == 0 && iRecordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + iRecordToGet+ " OFFSET " +iStart;                
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_SYBASE:
                    break; 
                    
              }
                
            //System.out.println("Get Saldo Activity ====> "+sql);    
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {                         
                Vector vTemp = new Vector();             
                long idActivity = 0;
                double actual = 0;
                double budget = 0;
                long idPerkiraan = 0;
                actual = rs.getDouble("ACTUAL");
                budget = rs.getDouble("BUDGET");
                idActivity = rs.getLong("ACTIVITY_ID");
                idPerkiraan = rs.getLong("ID_PERKIRAAN");
                Activity objActivity = new Activity();
                Perkiraan objPerkiraan = new Perkiraan();
                if(idActivity != 0){
                    try{
                        objActivity = PstActivity.fetchExc(idActivity);
                    }catch(Exception e){objActivity = new Activity();}
                }
                
                if(idPerkiraan != 0){
                    try{
                        objPerkiraan = PstPerkiraan.fetchExc(idPerkiraan);
                    }catch(Exception e){objPerkiraan = new Perkiraan();}
                }
                
                double total = budget - actual;                
                if(total > 0){                
                    vTemp.add(objActivity);
                    vTemp.add(""+total);
                    vTemp.add(objPerkiraan);
                    vectResult.add(vTemp);
                }
                
            }
            
        } catch (Exception e) {
            System.out.println("---> SessSpecialJurnal.getTotalSaldoActivity() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return vectResult;
    }
    
    

      public static int getCount(SrcJurnalUmum srcJurnalUmum, int journalType, int amountStatus) {
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

            String sql = "SELECT COUNT(DISTINCT JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID] + ")" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM " +
                    " INNER JOIN " + PstPeriode.TBL_PERIODE + " AS PE ON " +
                    " JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] + " = " +
                    " PE." + PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE] +
                    " INNER JOIN " + PstAppUser.TBL_APP_USER + " AS USR ON " +
                    " JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_USER_ID] + " = " +
                    " USR." + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID];

            String datecondition = "";
            if (srcJurnalUmum.getDateStatus() == 0) {
                String startDate = Formater.formatDate(currStartDate, "yyyy-MM-dd");
                String endDate = Formater.formatDate(currDueDate, "yyyy-MM-dd");
                datecondition = "(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "') ";
            }

            if (srcJurnalUmum.getDateStatus() == 2) {
                String startDate = Formater.formatDate(srcJurnalUmum.getStartDate(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcJurnalUmum.getEndDate(), "yyyy-MM-dd");
                datecondition = "(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "') ";
            }

            String vouchercondition = "";
            String countercondition = "";
            if (srcJurnalUmum.getVoucherNo() != null && srcJurnalUmum.getVoucherNo().length() > 0) {                            
                    vouchercondition = "(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_VOUCHER_NUMBER] + " LIKE '%" + srcJurnalUmum.getVoucherNo() + "%')";               
                
            }


            String refdoccondition = "";
            if (srcJurnalUmum.getInvoice() != null && srcJurnalUmum.getInvoice().length() > 0) {
                refdoccondition = "(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_REFERENCE] + " LIKE '%" + srcJurnalUmum.getInvoice() + "%')";
            }

            String operatorcondition = "";
            if (srcJurnalUmum.getOperatorId() != 0) {
                operatorcondition = "(USR." + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] + " = " + srcJurnalUmum.getOperatorId() + ")";
            }

            String contact_id = "";
            if (srcJurnalUmum.getContactOid() != 0) {
                contact_id = "(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_CONTACT_ID] + " = " + srcJurnalUmum.getContactOid() + ")";
            }
            
            String strJournalType = "";
            if (journalType > 0) {
                strJournalType = "(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] + " = " + journalType + ")";
            }

            String strAmountStatus = "";
            if (amountStatus != 0) {
                strAmountStatus = "(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT_STATUS] + " = " + amountStatus + ")";
            }    
            
            String ordercondition = "";
            switch (srcJurnalUmum.getOrderBy()) {
                /*case FrmSrcJurnalUmum.SORT_BY_VOUCHER:
                    ordercondition = " ORDER BY JU." + FrmSrcJurnalUmum.sortFieldKey[FrmSrcJurnalUmum.SORT_BY_VOUCHER] +
                            ", JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_VOUCHER_COUNTER];
                    break;*/

                case FrmSrcJurnalUmum.SORT_BY_DATE:
                    ordercondition = " ORDER BY JM." + FrmSrcJurnalUmum.sortFieldKey[FrmSrcJurnalUmum.SORT_BY_DATE];
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
           
            if ((strJournalType != null) && (strJournalType.length() > 0)) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + strJournalType;
                } else {
                    allCondition = strJournalType;
                }
            }
            
            if ((strAmountStatus != null) && (strAmountStatus.length() > 0)) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + strAmountStatus;
                } else {
                    allCondition = strAmountStatus;
                }
            }
            
            if (allCondition.length() > 0) {
                sql = sql + " WHERE  " + allCondition;
            }           
           
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
      
      public static void main(String[] args){
        SessSpecialJurnal sessSpecialJurnal = new SessSpecialJurnal();
        long oidPerkiraan = 504404329879989614L;
        Vector vSaldo = new Vector();
        //vSaldo = sessSpecialJurnal.getTotalSaldoAccountLink(oidPerkiraan);
        
        System.out.println("vSaldo.size() ===> "+vSaldo.size());
      }
}
