/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Aug 3, 2005
 * Time: 4:52:02 PM
 * To change this template use Options | File Templates.
 */
package com.dimata.aiso.session.report;

import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.entity.masterdata.PstPerkiraan;
import com.dimata.aiso.entity.masterdata.PstAisoBudgeting;
import com.dimata.aiso.entity.periode.Periode; 
import com.dimata.aiso.entity.periode.PstPeriode;
import com.dimata.aiso.entity.jurnal.PstJurnalDetail;
import com.dimata.aiso.entity.jurnal.PstJurnalUmum;
import com.dimata.aiso.entity.jurnal.SaldoAkhirPeriode;
import com.dimata.aiso.entity.jurnal.PstSaldoAkhirPeriode;
import com.dimata.aiso.entity.report.TrialBalance;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalDetail;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalMain;
import com.dimata.util.Formater;
import com.dimata.interfaces.journal.I_JournalType;

import java.util.Vector;
import java.sql.ResultSet;

public class SessTrialBalance {

    public static final String FRM_NAMA_PERIODE = "FRM_NAMA_PERIODE";

    /** gadnyana
     * untuk mencari laporan trial balance atau neraca percobaan
     * @param oidPeriod
     * @param typePrice
     * @return
     */
    synchronized public static Vector getListTrialBalance(long oidPeriod, long typePrice) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            long preOidPeriod = getOidPeriodeLalu(oidPeriod);
            String sql = "SELECT " +
                    " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    ", SUM(BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGET] + ") AS BGT" +
                    ", SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET] + ") AS DBT" +
                    ", SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT] + ") AS KRDT " +
                    " FROM " + PstJurnalUmum.TBL_JURNAL_UMUM + " AS JU " +
                    " INNER JOIN " + PstJurnalDetail.TBL_JURNAL_DETAIL + " AS JD " +
                    " ON JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                    " = JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID] +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC " +
                    " ON JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " LEFT JOIN " + PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD " +
                    " ON JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] +
                    " = BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN] +
                    " WHERE JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID] + "= " + oidPeriod +
                    " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    " ORDER BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];

            System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                TrialBalance trialBalance = new TrialBalance();
                trialBalance.setNomor(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]));
                trialBalance.setNama(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]));
                trialBalance.setAccountNameEnglish(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]));

                trialBalance.setAnggaran(rs.getDouble("BGT"));

                // mutasi
                double totDebet = rs.getDouble("DBT");
                double totKredit = rs.getDouble("KRDT");
                trialBalance.setDebet(totDebet);
                trialBalance.setKredit(totKredit);
                trialBalance.setTandaDebetKredit(rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]));

                // saldo awal
                SaldoAkhirPeriode saldoAkhirPeriode = getSaldoAwal(preOidPeriod, rs.getLong(PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]));
                double totSalDebet = saldoAkhirPeriode.getDebet();
                double totSalKredit = saldoAkhirPeriode.getKredit();

                if (totSalDebet != 0) {
                    trialBalance.setSaldoAwal(totSalDebet);
                } else if (totSalKredit != 0) {
                    trialBalance.setSaldoAwal(totSalKredit * -1);
                }

                // get saldo akhir
                if (trialBalance.getTandaDebetKredit() == PstPerkiraan.ACC_DEBETSIGN) {
                    trialBalance.setSaldoAkhir(trialBalance.getSaldoAwal() + (totDebet - totKredit));
                } else {
                    trialBalance.setSaldoAkhir(trialBalance.getSaldoAwal() + (totKredit - totDebet));
                }
                result.add(trialBalance);
            }
            rs.close();

        } catch (Exception e) {
        }
        return result;
    }


    /** gadnyana
     * untuk mencari laporan buku percobaan di
     * tambah berdasarkan department
     * @param oidPeriod
     * @param typePrice
     * @param departmentOid
     * @return
     */
    synchronized public static Vector getListTrialBalance(long oidPeriod, long typePrice, long departmentOid) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            long preOidPeriod = getOidPeriodeLalu(oidPeriod);
            String sql = "";

            /*sql = " CREATE TABLE tmp (ID_PERKIRAAN BIGINT(20)," +
                    " NOMOR_PERKIRAAN VARCHAR(15)," +
                    " NAMA VARCHAR(60) NULL, " +
                    " TANDA_DEBET_KREDIT INT(4) NULL, " +
                    " BGT DOUBLE(16,2) NULL, " +
                    " SALDO_AWAL_DEBET DOUBLE(16,2) NULL, " +
                    " SALDO_AWAL_KREDIT DOUBLE(16,2) NULL, " +
                    " DBT DOUBLE(16,2) NULL, " +
                    " KRDT DOUBLE(16,2) NULL);";*/
            sql = "DELETE FROM aiso_tmp_buffer";
            DBHandler.execUpdate(sql);

            sql = " INSERT INTO aiso_tmp_buffer (ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,ACCOUNT_NAME_ENGLISH,TANDA_DEBET_KREDIT," +
                    " SALDO_AWAL_DEBET,SALDO_AWAL_KREDIT) " +
                    " SELECT AP." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    ", AP." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                    ", AP." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    ", AP." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                    ", AP." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    ", SAP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET] +
                    ", SAP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT] +
                    " FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR + " AS SAP " +
                    "  INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS AP " +
                    " ON AP." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = SAP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
                    " WHERE SAP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] + "=" + preOidPeriod;
               
            if(departmentOid != 0)
            sql = sql+" AND AP." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_DEPARTMENT_ID] + "=" + departmentOid;
                    System.out.println("Insert saldo awal ===> "+sql);
                    
            DBHandler.execUpdate(sql);

            sql = " INSERT INTO aiso_tmp_buffer (ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,ACCOUNT_NAME_ENGLISH,TANDA_DEBET_KREDIT,BGT,DBT,KRDT) " +
                    " SELECT ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    ", SUM(BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGET] + ") AS BGT" +
                    ", SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET] + ") AS DBT" +
                    ", SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT] + ") AS KRDT " +
                    " FROM " + PstJurnalUmum.TBL_JURNAL_UMUM + " AS JU " +
                    " INNER JOIN " + PstJurnalDetail.TBL_JURNAL_DETAIL + " AS JD " +
                    " ON JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                    " = JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID] +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC " +
                    " ON JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " LEFT JOIN " + PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD " +
                    " ON JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] +
                    " = BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN] +
                    " WHERE JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID] + "= " + oidPeriod +                    
                    " AND JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE]+"="+I_JournalType.TIPE_JURNAL_UMUM;
            if(departmentOid != 0){
            sql = sql +" AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_DEPARTMENT_ID] + "=" + departmentOid +
                    // " OR JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE]+"="+I_JournalType.TIPE_JURNAL_PENUTUP_1+")"+
                    " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    " ORDER BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];
            }else{
            sql = sql + " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    " ORDER BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];
            }
            System.out.println("Insert mutasi dari Jurnal Umum ===> " + sql);
            DBHandler.execUpdate(sql);
            
            sql = " INSERT INTO aiso_tmp_buffer (ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,ACCOUNT_NAME_ENGLISH,TANDA_DEBET_KREDIT,BGT,AMOUNT_MAIN)" +
                  " SELECT ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+
                  ", SUM(BUD." +PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGET]+") AS BGT "+
                  ", SUM(JM." +PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT_STATUS]+" * "+
                  "  JM." +PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT]+") AS AMOUNT_MAIN "+                  
                  "  FROM " +PstSpecialJournalMain.TBL_AISO_SPC_JMAIN+" AS JM "+
                  "  INNER JOIN " +PstPerkiraan.TBL_PERKIRAAN+" AS ACC "+
                  "  ON JM." +PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+" = "+
                  "  ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                  "  LEFT JOIN " +PstAisoBudgeting.TBL_AISO_BUDGETING+" AS BUD "+ 
                  "  ON JM." +PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+" = "+
                  "  BUD." +PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]+
                  "  WHERE JM." +PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID]+" = "+oidPeriod+
                  "  AND JM." +PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]+
                  "  NOT IN("+I_JournalType.TIPE_JURNAL_UMUM+", "+I_JournalType.TIPE_JURNAL_PENUTUP_1+
                  ", "+I_JournalType.TIPE_JURNAL_PENUTUP_2+", "+I_JournalType.TIPE_JURNAL_PENUTUP_3+") "+            
                  "  GROUP BY ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+                  
                  "  ORDER BY ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];
            
            System.out.println("Insert Mutasi dari Special Journal Main ===> " + sql);
            DBHandler.execUpdate(sql);
            
            sql = "  INSERT INTO aiso_tmp_buffer (ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,ACCOUNT_NAME_ENGLISH,TANDA_DEBET_KREDIT,BGT,AMOUNT_DETAIL)" +
                  "  SELECT ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+
                  ", SUM(BUD." +PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGET]+") AS BGT "+
                  ", SUM(SD." +PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT_STATUS]+" * "+
                  "  SD." +PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT]+") AS AMOUNT_DETAIL "+                  
                  "  FROM " +PstSpecialJournalMain.TBL_AISO_SPC_JMAIN+" AS JM "+
                  "  INNER JOIN " +PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+
                  "  ON JM." +PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+" = "+
                  "  SD." +PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                  "  INNER JOIN " +PstPerkiraan.TBL_PERKIRAAN+" AS ACC "+
                  "  ON SD." +PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN]+" = "+
                  "  ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                  "  LEFT JOIN " +PstAisoBudgeting.TBL_AISO_BUDGETING+" AS BUD "+
                  "  ON SD." +PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN]+" = "+
                  "  BUD." +PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]+
                  "  WHERE JM." +PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID]+" = "+oidPeriod+
                  "  AND JM." +PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]+
                  "  NOT IN("+I_JournalType.TIPE_JURNAL_UMUM+", "+I_JournalType.TIPE_JURNAL_PENUTUP_1+
                  ",  "+I_JournalType.TIPE_JURNAL_PENUTUP_2+", "+I_JournalType.TIPE_JURNAL_PENUTUP_3+") "+
                  "  GROUP BY ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+                  
                  "  ORDER BY ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];
                  
            System.out.println("Insert Mutasi dari Special Journal Detail ===> " + sql);
            DBHandler.execUpdate(sql);
            
            sql = "SELECT ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,ACCOUNT_NAME_ENGLISH,TANDA_DEBET_KREDIT," +
                    " SUM(BGT),SUM(SALDO_AWAL_DEBET),SUM(SALDO_AWAL_KREDIT)," +
                    " SUM(DBT),SUM(KRDT),SUM(AMOUNT_MAIN),SUM(AMOUNT_DETAIL) FROM aiso_tmp_buffer" +
                    " GROUP BY ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,ACCOUNT_NAME_ENGLISH,TANDA_DEBET_KREDIT" +
                    " ORDER BY NOMOR_PERKIRAAN";
            
            System.out.println("SQL GET LIST TRIAL BALANCE ===> "+sql);
            
            int i = 0;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                TrialBalance trialBalance = new TrialBalance();
                trialBalance.setNomor(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]));
                trialBalance.setNama(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]));
                trialBalance.setAccountNameEnglish(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]));

                trialBalance.setAnggaran(rs.getDouble(6));

                // mutasi
                double totDebet = 0;
                double totKredit = 0;
                double amountMain = 0;
                double amountDetail = 0;
                double totAmountSpecial = 0;
                totDebet = rs.getDouble(9);
                totKredit = rs.getDouble(10); 
                amountMain = rs.getDouble(11);
                amountDetail = rs.getDouble(12);
                totAmountSpecial = amountMain + amountDetail;
                System.out.println("totAmountSpecial index ke : "+i+" = "+totAmountSpecial);
                int intDebetCreditAss = rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]);
                
                //if(intJournalType > I_JournalType.TIPE_JURNAL_PENUTUP_3){
                   if(intDebetCreditAss == PstPerkiraan.ACC_DEBETSIGN){
                       /*if(totAmountSpecial > 0){
                            trialBalance.setDebet(totDebet + totAmountSpecial);
                            trialBalance.setKredit(totKredit);
                       }else{
                            trialBalance.setDebet(totDebet);
                            trialBalance.setKredit(totKredit+(totAmountSpecial * -1));
                       }*/
                       if(amountMain < 0 && amountMain != 0){
                            trialBalance.setDebet(totDebet+amountDetail);
                            trialBalance.setKredit(totKredit+(amountMain * -1));
                       }
                       if(amountDetail < 0 && amountDetail != 0){
                            trialBalance.setDebet(totDebet + amountMain);
                            trialBalance.setKredit(totKredit +(amountDetail * -1));
                       }
                       
                       if(amountDetail == 0 && amountMain > 0){
                            trialBalance.setDebet(totDebet + amountMain);
                            trialBalance.setKredit(totKredit);
                       }
                       
                       if(amountDetail > 0 && amountMain == 0){
                            trialBalance.setDebet(totDebet + amountDetail);
                            trialBalance.setKredit(totKredit);
                       }
                       
                       if(amountDetail == 0 && amountMain == 0){
                            trialBalance.setDebet(totDebet);
                            trialBalance.setKredit(totKredit);
                       }
                       if(amountDetail > 0 && amountMain > 0){
                            trialBalance.setDebet(totDebet + amountDetail + amountMain);
                            trialBalance.setKredit(totKredit);
                       }
                   }else{ 
                        /*if(totAmountSpecial > 0){
                            trialBalance.setDebet(totDebet);
                            trialBalance.setKredit(totKredit + totAmountSpecial);
                       }else{
                            trialBalance.setDebet(totDebet +(totAmountSpecial * -1));
                            trialBalance.setKredit(totKredit);
                       }*/
                        if(amountMain > 0 && amountDetail == 0){
                            trialBalance.setDebet(totDebet);
                            trialBalance.setKredit(totKredit + amountMain);
                        }
                        if(amountMain < 0 && amountDetail == 0){
                            trialBalance.setDebet(totDebet + (amountMain * -1));
                            trialBalance.setKredit(totKredit);
                        }
                        if(amountMain > 0 && amountDetail < 0){
                            trialBalance.setDebet(totDebet + (amountDetail * -1));
                            trialBalance.setKredit(totKredit + amountMain);
                        }
                        if(amountMain > 0 && amountDetail > 0){
                            trialBalance.setDebet(totDebet );
                            trialBalance.setKredit(totKredit + amountMain + amountDetail);
                        }
                        if(amountMain < 0 && amountDetail < 0){
                            trialBalance.setDebet(totDebet + (amountMain * -1) + (amountDetail * -1) );
                            trialBalance.setKredit(totKredit );
                        }
                        if(amountMain == 0 && amountDetail > 0){
                            trialBalance.setDebet(totDebet );
                            trialBalance.setKredit(totKredit + amountDetail);
                        }
                        if(amountMain == 0 && amountDetail < 0){
                            trialBalance.setDebet(totDebet + (amountDetail * -1));
                            trialBalance.setKredit(totKredit);
                        }
                        if(amountMain == 0 && amountDetail == 0){
                            trialBalance.setDebet(totDebet );
                            trialBalance.setKredit(totKredit);
                        }
                       } 
                           
               
                trialBalance.setTandaDebetKredit(intDebetCreditAss);

                // saldo awal
                // trialBalance.setSaldoAwal(rs.getDouble(6));
                // SaldoAkhirPeriode saldoAkhirPeriode = getSaldoAwal(preOidPeriod, rs.getLong(PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]));
                double totSalDebet = rs.getDouble(7);
                double totSalKredit = rs.getDouble(8);

                // get saldo akhir
                if (trialBalance.getTandaDebetKredit() == PstPerkiraan.ACC_DEBETSIGN) {
                    if (totSalDebet != 0) {
                        trialBalance.setSaldoAwal(totSalDebet);
                    } else if (totSalKredit != 0) {
                        trialBalance.setSaldoAwal(totSalKredit * -1);
                    }
                    trialBalance.setSaldoAkhir(trialBalance.getSaldoAwal() + (trialBalance.getDebet() - trialBalance.getKredit()));
                } else {
                    if (totSalDebet != 0) {
                        trialBalance.setSaldoAwal(totSalDebet * -1);
                    } else if (totSalKredit != 0) {
                        trialBalance.setSaldoAwal(totSalKredit);
                    }
                    trialBalance.setSaldoAkhir(trialBalance.getSaldoAwal() + (trialBalance.getKredit() - trialBalance.getDebet()));
                }
                i++;
                result.add(trialBalance);
            }
            rs.close();

        } catch (Exception e) {
        }
        return result;
    }

    /** gadnyana
     * untuk mencari periode yang lalu
     * @param oidPeriod
     * @return
     */
    public static long getOidPeriodeLalu(long oidPeriod) {
        long oidLalu = 0;
        try {
            Periode periode = PstPeriode.fetchExc(oidPeriod);
            String where = PstPeriode.fieldNames[PstPeriode.FLD_TGLAKHIR] + " < '" + Formater.formatDate(periode.getTglAwal(), "yyyy-MM-dd") + "'";
            String order = PstPeriode.fieldNames[PstPeriode.FLD_TGLAKHIR] + " DESC ";
            Vector list = PstPeriode.list(0, 0, where, order);
            if (list.size() > 0) {
                Periode prd = (Periode) list.get(0);
                where = PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] + "=" + prd.getOID();
                Vector listSaldo = PstSaldoAkhirPeriode.list(0, 0, where, "");
                if (listSaldo.size() > 0) {
                    SaldoAkhirPeriode saldoAkhirPeriode = (SaldoAkhirPeriode) listSaldo.get(0);
                    oidLalu = saldoAkhirPeriode.getOID();
                }
            }
        } catch (Exception e) {
        }
        return oidLalu;
    }

    /**
     *
     * @param oidPeriod
     * @return
     */
    public static SaldoAkhirPeriode getSaldoAwal(long oidPeriod, long oidMaterial) {
        try {
            String where = PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] + " = " + oidPeriod +
                    " AND " + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] + " = " + oidMaterial;
            Vector list = PstSaldoAkhirPeriode.list(0, 0, where, "");
            if (list.size() > 0) {
                SaldoAkhirPeriode saldoAkhirPeriode = (SaldoAkhirPeriode) list.get(0);
                return saldoAkhirPeriode;
            }
        } catch (Exception e) {
        }
        return new SaldoAkhirPeriode();
    }
}
