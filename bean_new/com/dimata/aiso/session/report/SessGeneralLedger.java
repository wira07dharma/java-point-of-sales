/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Aug 3, 2005
 * Time: 10:18:03 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.aiso.session.report;

import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.entity.jurnal.PstJurnalUmum;
import com.dimata.aiso.entity.jurnal.PstJurnalDetail;
import com.dimata.aiso.entity.jurnal.PstSaldoAkhirPeriode;
import com.dimata.aiso.entity.jurnal.SaldoAkhirPeriode;
import com.dimata.aiso.entity.masterdata.PstPerkiraan; 
import com.dimata.aiso.entity.masterdata.Perkiraan;
import com.dimata.aiso.entity.masterdata.PstAccountLink;
import com.dimata.aiso.entity.masterdata.PstActivity;
import com.dimata.aiso.entity.report.GeneralLedger;
import com.dimata.aiso.entity.periode.PstPeriode;
import com.dimata.aiso.entity.periode.Periode;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalDetail;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalDetailAssignt;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalMain;
import com.dimata.interfaces.chartofaccount.I_ChartOfAccountGroup;
import com.dimata.interfaces.journal.I_JournalType;
import com.dimata.util.Formater;
import com.dimata.util.lang.I_Language;

import java.util.Vector;
import java.sql.ResultSet;

public class SessGeneralLedger implements I_Language {

    public static final String FRM_NAMA_PERKIRAAN = "FRM_NAMA_PERKIRAAN";
    public static final String FRM_NAMA_PERIODE = "FRM_NAMA_PERIODE";    
   
    
    public static String[] openingBalance = {
        "Saldo Awal","Opening Balance"
    };
    /** GADNYANA
     * untuk mencari laporan general ledger/buku besar
     * @param idPerkiraan
     * @param oidPeriod
     * @return
     */
    public static Vector listGeneralLedgerNonDept(long idPerkiraan, long oidPeriod) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            double totalSaldo = 0.0;
            Perkiraan perk = PstPerkiraan.fetchExc(idPerkiraan);
            if (perk.getAccountGroup() == PstPerkiraan.ACC_GROUP_LIQUID_ASSETS ||
		perk.getAccountGroup() == PstPerkiraan.ACC_GROUP_FIXED_ASSETS ||
		perk.getAccountGroup() == PstPerkiraan.ACC_GROUP_OTHER_ASSETS ||
		perk.getAccountGroup() == PstPerkiraan.ACC_GROUP_CURRENCT_LIABILITIES ||
		perk.getAccountGroup() == PstPerkiraan.ACC_GROUP_LONG_TERM_LIABILITIES ||
		perk.getAccountGroup() == PstPerkiraan.ACC_GROUP_EQUITY) {
                totalSaldo = getSaldoAwal(idPerkiraan, oidPeriod);
                GeneralLedger generalLedger = new GeneralLedger();
                generalLedger.setGlDate(null);
                generalLedger.setGlKeterangan(openingBalance[LANGUAGE_DEFAULT]);
                generalLedger.setGlSaldo(totalSaldo);

                result.add(generalLedger);
            }

            /* End method account opening balance */
            
            Vector listOidJu = getListJurnalUmum(idPerkiraan, oidPeriod);
            if (listOidJu.size() > 0) {
                String sql = "SELECT " +
                        " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI] + "," +
                        " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] + "," +
                        " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] + "," +
                        " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] + "," +
                        " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] + "," +
                        " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] + "," +
                        " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_KETERANGAN] + "," +
                        " SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET] + ") AS DBT ," +
                        " SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT] + ") AS KRDT " +
                        " FROM " + PstJurnalUmum.TBL_JURNAL_UMUM + " AS JU " +
                        " INNER JOIN " + PstJurnalDetail.TBL_JURNAL_DETAIL + " AS JD " +
                        " ON JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] + " = " +
                        " JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID] +
                        " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC " +
                        " ON JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] + " = " +
                        " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN];

                String where = "";
                for (int k = 0; k < listOidJu.size(); k++) {
                    long oid = Long.parseLong((String) listOidJu.get(k));
                    if (where.length() == 0) {
                        where = " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] + "=" + oid;
                    } else {
                        where = where + " OR JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] + "=" + oid;
                    }
                }
                sql = sql + " WHERE " + where;
                sql = sql + " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                        " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                        " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                        " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                        " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                        " ,JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_KETERANGAN] +
                        " ,JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI];
                sql = sql + " ORDER BY " + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI];

                //System.out.println("sql : "+sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while (rs.next()) {
                    if (rs.getLong(PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]) != idPerkiraan) {
                        GeneralLedger generalLedger = new GeneralLedger();
                        generalLedger.setGlDate(rs.getDate(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI]));
                        generalLedger.setGlKeterangan(rs.getString(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_KETERANGAN]));
                        generalLedger.setGlNomor(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]));
                        generalLedger.setGlNama(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]));

                        double totDebet = rs.getDouble("DBT");
                        double totKredit = rs.getDouble("KRDT");
                        if (totDebet != 0) {
                            generalLedger.setGlDebet(0);
                            generalLedger.setGlKredit(totDebet);
                        }                         
                        
                        if(totKredit != 0){
                            generalLedger.setGlDebet(totKredit);
                            generalLedger.setGlKredit(0);
                        }
                        
                        if(totDebet != 0 && totKredit != 0){
                            generalLedger.setGlDebet(totKredit);
                            generalLedger.setGlKredit(totDebet);
                        }
                        
                        generalLedger.setTandaDebetKredit(rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]));

                        // get saldo
                        if (perk.getTandaDebetKredit() == PstPerkiraan.ACC_DEBETSIGN) {
                            //totalSaldo = totalSaldo  + (totDebet - totKredit);
                            totalSaldo = totalSaldo + (generalLedger.getGlDebet() - generalLedger.getGlKredit());
                            generalLedger.setGlSaldo(totalSaldo);
                        } else {
                            //totalSaldo = totalSaldo  + (totKredit - totDebet);
                            totalSaldo = totalSaldo + (generalLedger.getGlKredit() - generalLedger.getGlDebet());
                            generalLedger.setGlSaldo(totalSaldo);
                        }
                        if(generalLedger.getGlDate() != null)
                        result.add(generalLedger);
                    }
                }
                rs.close();
            }
        } catch (Exception e) {
            System.out.println("SessGeneralLedger.listGeneralLedger() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);

        }
        return result;
    }

    /**gadnyana 
     * menampilkan data buku besar tapi
     * ada tanbahan dengan department
     * @param long idPerkiraan
     * @param long oidPeriod
     * @param long departmentOid 
     * @return Vector vListGeneralLedger
     */
    public static synchronized Vector listGeneralLedger(long idPerkiraan, long oidPeriod, long departmentOid) {
       
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            /* This method to count opening balance */
            double totalSaldo = 0.0;
            Perkiraan perk = PstPerkiraan.fetchExc(idPerkiraan);
           if (perk.getAccountGroup() == PstPerkiraan.ACC_GROUP_LIQUID_ASSETS ||
		perk.getAccountGroup() == PstPerkiraan.ACC_GROUP_FIXED_ASSETS ||
		perk.getAccountGroup() == PstPerkiraan.ACC_GROUP_OTHER_ASSETS ||
		perk.getAccountGroup() == PstPerkiraan.ACC_GROUP_CURRENCT_LIABILITIES ||
		perk.getAccountGroup() == PstPerkiraan.ACC_GROUP_LONG_TERM_LIABILITIES ||
		perk.getAccountGroup() == PstPerkiraan.ACC_GROUP_EQUITY) {
                totalSaldo = getSaldoAwal(idPerkiraan, oidPeriod);
                
                GeneralLedger generalLedger = new GeneralLedger();
                generalLedger.setGlDate(null);
                generalLedger.setGlKeterangan("Saldo Awal");
                generalLedger.setGlSaldo(totalSaldo);

                result.add(generalLedger);
            }
            /* End method count opening balance */
            
            
            /* Method to found id perkiraan on list journal */
            Vector listOidJu = getListJurnalUmum(idPerkiraan, oidPeriod);
            
            if (listOidJu.size() > 0) {
                String sql = 
                        " SELECT UN.TRANSDATE AS TRANSDATE, UN.NOVOUCHER AS NOVOUCHER, UN.JOURNALID AS JOURNALID, " +
                        " UN.VOUCHERCOUNTER AS VOUCHERCOUNTER, UN.ACCOUNTNUMBER AS ACCOUNTNUMBER,UN.ACCOUNTID AS ACCOUNTID, " +
                        " UN.NAMEINDO AS NAMEINDO, UN.NAMEENGLISH AS NAMEENGLISH, UN.DEBETCREDITASS AS DEBETCREDITASS," +
                        " UN.REMARK AS REMARK, UN.AMOUNTDEBET AS AMOUNTDEBET, UN.AMOUNTCREDIT AS AMOUNTCREDIT, UN.AMOUNT AS AMOUNT, " +
                        " UN.DESCR AS DESCR, UN.ACCLINKTYPE AS ACCLINKTYPE, UN.JTYPE AS JTYPE," +
                        " UN.DET_ACC_ID AS DET_ACC_ID, UN.MAIN_ACC_ID AS MAIN_ACC_ID FROM("+
                        " SELECT " +
                        " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI] + " AS TRANSDATE," +
                        " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_VOUCHER] + " AS NOVOUCHER," +
                        " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] + " AS JOURNALID," +
                        " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_COUNTER] + " AS VOUCHERCOUNTER," +
                        " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] + " AS ACCOUNTNUMBER," +
                        " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] + " AS ACCOUNTID," +
                        " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] + " AS NAMEINDO," +
                        " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] + " AS NAMEENGLISH," +
                        " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] + " AS DEBETCREDITASS," +
                        " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_KETERANGAN] + " AS REMARK," +
                        " SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET] + ") AS AMOUNTDEBET ," +
                        " SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT] + ") AS AMOUNTCREDIT, " +
                        " 0 AS AMOUNT,"+                        
                        " '0' AS DESCR,"+                       
                        " 0 AS ACCLINKTYPE,"+
                        " 0 AS JTYPE,"+
                        " 0 AS DET_ACC_ID, 0 AS  MAIN_ACC_ID"+
                        " FROM " + PstJurnalUmum.TBL_JURNAL_UMUM + " AS JU " +
                        " INNER JOIN " + PstJurnalDetail.TBL_JURNAL_DETAIL + " AS JD " +
                        " ON JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] + " = " +
                        " JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID] +
                        " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC " +
                        " ON JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] + " = " +
                        " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN];
                        
                
                String where = "";
                if (listOidJu.size() > 0) {
                for (int k = 0; k < listOidJu.size(); k++) {
                    long oid = Long.parseLong((String) listOidJu.get(k));
                    if (where.length() == 0) {
                        where = " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] + " = " + oid;
                    } else {
                        where = where + " OR JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] + " = " + oid;
                    }
                    where = " ("+where+") ";
                }
                }
                if(where.length()>0)
                    where = where + " AND JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+"="+idPerkiraan;
                else
                    where = " JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+"="+idPerkiraan;
               

               sql = sql + " WHERE " + where;//+ whereDepartement; 
               sql = sql + " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                        " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                        " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                        " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                        " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                        " ,JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_VOUCHER] +
                        " ,JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                        " ,JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_COUNTER] +
                        " ,JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_KETERANGAN] +
                        " ,JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI];               
               
                sql = sql + " UNION "+
                    " SELECT "+
                    " JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE]+" AS TRANSDATE "+
                    ", JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_VOUCHER_NUMBER]+" AS NOVOUCHER "+
                    ", JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+" AS JOURNALID "+
                    ", JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_VOUCHER_COUNTER]+" AS VOUCHERCOUNTER "+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]+" AS ACCOUNTNUMBER "+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+" AS ACCOUNTID "+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+" AS NAMEINDO "+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+" AS NAMEENGLISH "+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+" AS DEBETCREDITASS "+
                    ", SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_DESCRIPTION]+" AS REMARK "+
                    ", 0 AS AMOUNTDEBET "+
                    ", 0 AS AMOUNTCREDIT "+
                    ", SUM(SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT]+") AS AMOUNT "+                    
                    ", ACT."+PstActivity.fieldNames[PstActivity.FLD_DESCRIPTION]+" AS DESCR "+                    
                    ", AL."+PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE]+" AS ACCLINKTYPE "+
                    ", JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]+" AS JTYPE "+
                    ", SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN]+" AS DET_ACC_ID "+
                    ", JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+" AS MAIN_ACC_ID "+
                    " FROM "+PstSpecialJournalMain.TBL_AISO_SPC_JMAIN+ " AS JM "+
                    " INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+
                    " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+" = "+
                    " SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                    " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS PRK "+
                    " ON SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN]+" = "+
                    " PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                    " LEFT JOIN "+PstSpecialJournalDetailAssignt.TBL_AISO_JDETAIL_ASSIGNT+" AS DA "+
                    " ON SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_DETAIL_ID]+" = "+
                    " DA."+PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_JOURNAL_DETAIL_ID]+
                    " LEFT JOIN "+PstActivity.TBL_AISO_ACTIVITY+" AS ACT "+
                    " ON ACT."+PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]+" = "+
                    " DA."+PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_ACTIVITY_ID]+
                    " LEFT JOIN "+PstAccountLink.TBL_ACCOUNT_LINK+" AS AL "+
                    " ON SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN]+" = "+
                    " AL."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID];
                
                String whr = "";
                if (listOidJu.size() > 0) {
                for (int k = 0; k < listOidJu.size(); k++) {
                    long oidJournal = Long.parseLong((String) listOidJu.get(k));
                    if (whr.length() == 0) {
                        whr = " JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID] + " = " + oidJournal;
                    } else {
                        whr = whr + " OR JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID] + " = " + oidJournal;
                    }
                    whr = " ("+whr+") ";
                }
                
                if(whr.length()>0){
                    whr += " AND JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID]+" = "+oidPeriod;
                }else{
                    whr += " JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID]+" = "+oidPeriod;
                }
                
                if(whr.length()>0){
                    whr = whr + " AND (JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+" = "+idPerkiraan;
                    whr = whr + " OR SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN]+" = "+idPerkiraan+")";
                }else{
                    whr = " (JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+" = "+idPerkiraan;
                    whr = whr + " OR SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN]+" = "+idPerkiraan+")";
                }
                }else{
                    if(whr.length()>0)
                        whr = whr + " AND SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN]+" = "+idPerkiraan;
                else
                        whr = " SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN]+" = "+idPerkiraan;
                }
                
                
                
               sql = sql + " WHERE " + whr;
               
               sql = sql + " GROUP BY "+
                    " PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+
                    ", JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_VOUCHER_NUMBER]+
                    ", JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    ", JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_VOUCHER_COUNTER]+
                    ", SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_DESCRIPTION]+
                    ", JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE]+                                    
                    ", ACT."+PstActivity.fieldNames[PstActivity.FLD_DESCRIPTION]+
                    ", AL."+PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE]+
                    ", JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]+ 
                    ", JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+
                    ", SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN];
              
                sql = sql + ") AS UN " +
                    " GROUP BY UN.TRANSDATE, UN.NOVOUCHER, UN.JOURNALID, UN.VOUCHERCOUNTER," +
                    " UN.ACCOUNTNUMBER,UN.ACCOUNTID, UN.NAMEINDO, UN.NAMEENGLISH, " +
                    " UN.DEBETCREDITASS,UN.REMARK,UN.AMOUNTDEBET, UN.AMOUNTCREDIT, UN.DESCR, " +
                    " UN.ACCLINKTYPE, UN.JTYPE, UN.DET_ACC_ID, UN.AMOUNT, UN.MAIN_ACC_ID "+
                    " ORDER BY UN.TRANSDATE, UN.VOUCHERCOUNTER"; 
                
                System.out.println("SQL LIST GENERAL LEDGER ====> "+sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                
                while (rs.next()) {
                        GeneralLedger generalLedger = new GeneralLedger();
                        String strDesc = "";
                        String strRemark = "";
                        strDesc = rs.getString("DESCR");
                        strRemark = rs.getString("REMARK");
                        
                        if(strDesc != null && strDesc.length() > 1)
                            generalLedger.setGlKeterangan(strDesc);
                        else
                            generalLedger.setGlKeterangan(strRemark);
                        
                        generalLedger.setGlDate(rs.getDate("TRANSDATE"));
                        generalLedger.setGlNomor(rs.getString("ACCOUNTNUMBER"));
                        generalLedger.setGlNama(rs.getString("NAMEINDO"));
                        generalLedger.setAccountNameEnglish(rs.getString("NAMEENGLISH"));

                        // new
                        generalLedger.setNoVoucher(rs.getString("NOVOUCHER"));
                        generalLedger.setJurnalOid(rs.getLong("JOURNALID"));
                        generalLedger.setCounter(rs.getInt("VOUCHERCOUNTER"));
                        // ---                        
                        double totDebet = rs.getDouble("AMOUNTDEBET");
                        double totKredit = rs.getDouble("AMOUNTCREDIT");
                        double totAmount = rs.getDouble("AMOUNT");                        
                        int linkType = rs.getInt("ACCLINKTYPE");
                        int journalType = rs.getInt("JTYPE");
                        long idPerkDetail = rs.getLong("DET_ACC_ID");
                        long idPerkMain = rs.getLong("MAIN_ACC_ID");
                       
                        if(totDebet != 0 && totAmount == 0){    
                            generalLedger.setGlDebet(totDebet);
                        }else{
                            switch(journalType){
                                case I_JournalType.TIPE_SPECIAL_JOURNAL_CASH:                                    
                                    if(idPerkDetail != idPerkiraan){
                                        generalLedger.setGlDebet(totAmount);
                                    }
                                    break;
                                case I_JournalType.TIPE_SPECIAL_JOURNAL_PETTY_CASH:                                    
                                    if(idPerkDetail == idPerkiraan && linkType == 0){
                                        generalLedger.setGlDebet(totAmount);
                                    }                                    
                                    break;
                                case I_JournalType.TIPE_SPECIAL_JOURNAL_BANK:                                    
                                    if(idPerkDetail != idPerkiraan && linkType == I_ChartOfAccountGroup.ACC_GROUP_CASH){
                                        generalLedger.setGlDebet(totAmount);
                                    }else{
                                        if(idPerkDetail == idPerkiraan && linkType == 0){
                                            generalLedger.setGlDebet(totAmount);
                                        }
                                        
                                        if(idPerkDetail == idPerkiraan && linkType == I_ChartOfAccountGroup.ACC_GROUP_BANK){
                                            generalLedger.setGlDebet(totAmount);
                                        }
                                    }
                                    break;
                                case I_JournalType.TIPE_SPECIAL_JOURNAL_REPLACEMENT:                                    
                                    if(idPerkDetail == idPerkiraan){
                                        generalLedger.setGlDebet(totAmount);
                                    }
                                    break;
                                case I_JournalType.TIPE_SPECIAL_JOURNAL_BANK_TRANSFER:                                     
                                    if(idPerkDetail == idPerkiraan){
                                        generalLedger.setGlDebet(totAmount);
                                    }
                                    break;
                                case I_JournalType.TIPE_SPECIAL_JOURNAL_FUND:                                    
                                    if(idPerkDetail != idPerkiraan){
                                        generalLedger.setGlDebet(totAmount);
                                    }
                                    break;
                                case I_JournalType.TIPE_SPECIAL_JOURNAL_NON_CASH:                                    
                                    if(idPerkDetail == idPerkiraan){
                                        generalLedger.setGlDebet(totAmount);
                                    }
                                    break;
                                case I_JournalType.TIPE_SPECIAL_JOURNAL_PAYMENT:
                                    if(idPerkDetail == idPerkiraan){
                                        generalLedger.setGlDebet(totAmount);
                                    }
                                    break;
                            }
                        }
                                                         
                        
                        if(totKredit != 0 && totAmount == 0){
                            generalLedger.setGlKredit(totKredit);
                        }else{
                            switch(journalType){
                                case I_JournalType.TIPE_SPECIAL_JOURNAL_CASH:
                                    if(idPerkDetail == idPerkiraan){
                                        generalLedger.setGlKredit(totAmount);
                                    }
                                break;
                                case I_JournalType.TIPE_SPECIAL_JOURNAL_PETTY_CASH:                                     
                                    if(idPerkDetail != idPerkiraan){
                                        generalLedger.setGlKredit(totAmount);
                                    }
                                    if(idPerkDetail == idPerkiraan && linkType == I_ChartOfAccountGroup.ACC_GROUP_CURRENCT_LIABILITIES){
                                        generalLedger.setGlKredit(totAmount);
                                    }
                                break;
                                case I_JournalType.TIPE_SPECIAL_JOURNAL_BANK:                                    
                                     if(idPerkDetail == idPerkiraan && linkType == I_ChartOfAccountGroup.ACC_GROUP_CASH){
                                        generalLedger.setGlKredit(totAmount);
                                    }else{
                                        if(idPerkDetail != idPerkiraan){ 
                                            switch(linkType){
                                                case I_ChartOfAccountGroup.ACC_GROUP_ALL:
                                                    generalLedger.setGlKredit(totAmount);
                                                break;
                                                case I_ChartOfAccountGroup.ACC_GROUP_BANK:
                                                    generalLedger.setGlKredit(totAmount);
                                                break;
                                                case I_ChartOfAccountGroup.ACC_GROUP_EXPENSE:
                                                    generalLedger.setGlKredit(totAmount);
                                                break;    
                                            }
                                        }
                                    }
                                break;
                                case I_JournalType.TIPE_SPECIAL_JOURNAL_REPLACEMENT:                                    
                                    if(idPerkDetail != idPerkiraan){
                                        generalLedger.setGlKredit(totAmount);
                                    }
                                break;
                                case I_JournalType.TIPE_SPECIAL_JOURNAL_BANK_TRANSFER:                                    
                                    if(idPerkDetail != idPerkiraan){
                                        generalLedger.setGlKredit(totAmount);
                                    }
                                    if((idPerkDetail == idPerkiraan) && (totAmount != 0) && (idPerkMain == idPerkDetail)){
                                        generalLedger.setGlKredit(totAmount);
                                    }
                                break;
                                case I_JournalType.TIPE_SPECIAL_JOURNAL_FUND:                                    
                                     if(idPerkDetail == idPerkiraan){
                                        generalLedger.setGlKredit(totAmount);
                                    }
                                break;
                                case I_JournalType.TIPE_SPECIAL_JOURNAL_NON_CASH:                                    
                                    if(idPerkDetail != idPerkiraan){
                                        generalLedger.setGlKredit(totAmount);
                                    }
                                break;
                                case I_JournalType.TIPE_SPECIAL_JOURNAL_PAYMENT:                                   
                                    if(idPerkDetail != idPerkiraan){
                                        generalLedger.setGlKredit(totAmount);
                                    }
                                break;
                            }
                        }
                           

                        generalLedger.setTandaDebetKredit(rs.getInt("DEBETCREDITASS"));                       
                        if (perk.getTandaDebetKredit() == PstPerkiraan.ACC_DEBETSIGN) {
                            totalSaldo = totalSaldo + (generalLedger.getGlDebet() - generalLedger.getGlKredit());
                            generalLedger.setGlSaldo(totalSaldo);                            
                        } else {
                            totalSaldo = totalSaldo + (generalLedger.getGlKredit() - generalLedger.getGlDebet());
                            generalLedger.setGlSaldo(totalSaldo);                            
                        }
                        result.add(generalLedger);
                }
                
                rs.close();
            } //end if
        } catch (Exception e) {
            System.out.println("SessGeneralLedger.listGeneralLedger() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);

        }
        return result;
    }

    /** gadnyana
     * @param idPerkiraan
     * @param oidPeriod
     * @return
     */
    public static Vector getListJurnalUmum(long idPerkiraan, long oidPeriod) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = 
                    "SELECT UN.JOURNAL_ID AS JOURNALID FROM("+
                    " SELECT " +
                    " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +" AS JOURNAL_ID "+
                    " FROM " + PstJurnalUmum.TBL_JURNAL_UMUM + " AS JU " +
                    " INNER JOIN " + PstJurnalDetail.TBL_JURNAL_DETAIL + " AS JD " +
                    " ON JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] + " = " +
                    " JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID] +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC " +
                    " ON JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] + " = " +
                    " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] + " = " + idPerkiraan +
                    " AND JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID] + " = " + oidPeriod +
                    " AND JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE] + " = " + PstJurnalUmum.TIPE_JURNAL_UMUM +
                    " GROUP BY JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]+
                    " UNION "+
                    " SELECT "+
                    " JM."+ PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+" AS JOURNAL_ID "+
                    " FROM "+ PstSpecialJournalMain.TBL_AISO_SPC_JMAIN +" AS JM "+
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS PRK " +
                    " ON JM."+ PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+" = "+                    
                    " PRK."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +                   
                    " WHERE PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+" = "+ idPerkiraan +
                    " AND JM."+ PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +" = "+ oidPeriod +
                    " AND JM."+ PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]+
                    " NOT IN("+ PstSpecialJournalMain.TIPE_JURNAL_PENUTUP_1 +
                    " ,"+PstSpecialJournalMain.TIPE_JURNAL_PENUTUP_2+
                    " ,"+PstSpecialJournalMain.TIPE_JURNAL_PENUTUP_3+
                    " ,"+PstSpecialJournalMain.TIPE_JURNAL_UMUM+
                    ") GROUP BY JM."+ PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " UNION "+
                    " SELECT "+
                    " JM."+ PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+" AS JOURNAL_ID "+
                    " FROM "+ PstSpecialJournalMain.TBL_AISO_SPC_JMAIN +" AS JM "+
                    " INNER JOIN "+ PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS JD "+
                    " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " = JD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+               
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS PRK " +
                    " ON JD."+ PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN]+" = "+                    
                    " PRK."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +                   
                    " WHERE PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+" = "+ idPerkiraan +
                    " AND JM."+ PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +" = "+ oidPeriod +
                    " AND JM."+ PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]+
                    " NOT IN("+ PstSpecialJournalMain.TIPE_JURNAL_PENUTUP_1 +
                    " ,"+PstSpecialJournalMain.TIPE_JURNAL_PENUTUP_2+
                    " ,"+PstSpecialJournalMain.TIPE_JURNAL_PENUTUP_3+
                    " ,"+PstSpecialJournalMain.TIPE_JURNAL_UMUM+
                    ") GROUP BY JM."+ PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " )AS UN GROUP BY UN.JOURNAL_ID";
                   
                    
            System.out.println("SQL TO GET ID JOURNAL =====> "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result.add(String.valueOf(rs.getLong("JOURNALID")));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("SessGeneralLedger.getListJurnalUmum() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    

    /** gadnyana
     * untuk mencari saldo awal satu perkiraan
     * pada periode sebelumnya
     * @param oidPerkiraan
     * @param oidPeriod
     * @return
     */
    public static double getSaldoAwal(long oidPerkiraan, long oidPeriod) {
        double saldoAwal = 0.0;
        try {
            Periode periode = PstPeriode.fetchExc(oidPeriod);
            Perkiraan perk = PstPerkiraan.fetchExc(oidPerkiraan);
            String where = PstPeriode.fieldNames[PstPeriode.FLD_TGLAKHIR] + " < '" + Formater.formatDate(periode.getTglAwal(), "yyyy-MM-dd") + "'";
            String order = PstPeriode.fieldNames[PstPeriode.FLD_TGLAKHIR] + " DESC ";
            Vector list = PstPeriode.list(0, 0, where, order);
            if (list.size() > 0) {
                Periode prd = (Periode) list.get(0);               
                where = PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] + "=" + prd.getOID() +
                        " AND " + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] + "=" + oidPerkiraan;
                Vector listSaldo = PstSaldoAkhirPeriode.list(0, 0, where, "");
                if (listSaldo.size() > 0) {
                    SaldoAkhirPeriode saldoAkhirPeriode = (SaldoAkhirPeriode) listSaldo.get(0);                     
                    if(perk.getTandaDebetKredit() == PstPerkiraan.ACC_DEBETSIGN){
                        if (saldoAkhirPeriode.getDebet() == 0)
                            saldoAwal = saldoAkhirPeriode.getKredit() * -1;
                        else
                            saldoAwal = saldoAkhirPeriode.getDebet();
                    }else{
                        if (saldoAkhirPeriode.getKredit() == 0)
                            saldoAwal = saldoAkhirPeriode.getDebet() * -1;
                        else
                            saldoAwal = saldoAkhirPeriode.getKredit();
                    }
                }
            }
        } catch (Exception e) {
        }
        return saldoAwal;
    }
    
   
}
