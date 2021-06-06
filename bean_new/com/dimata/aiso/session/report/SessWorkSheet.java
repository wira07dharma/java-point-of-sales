/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Aug 5, 2005
 * Time: 9:48:12 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.aiso.session.report;

import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.entity.masterdata.*;
import com.dimata.aiso.entity.jurnal.PstJurnalDetail;
import com.dimata.aiso.entity.jurnal.PstJurnalUmum;
import com.dimata.aiso.entity.jurnal.SaldoAkhirPeriode;
import com.dimata.aiso.entity.jurnal.PstSaldoAkhirPeriode;
import com.dimata.aiso.entity.report.WorkSheet;
import com.dimata.aiso.entity.periode.Periode;
import com.dimata.aiso.entity.periode.PstPeriode;
import com.dimata.aiso.entity.report.CashFlow;
import com.dimata.aiso.entity.search.SrcCashFlow;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalDetail;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalMain;
import com.dimata.harisma.entity.masterdata.PstDepartment;
import com.dimata.util.Formater;
import com.dimata.interfaces.chartofaccount.I_ChartOfAccountGroup;
import com.dimata.interfaces.journal.I_JournalType;

import java.util.Vector;
import java.sql.ResultSet;
import java.util.Date;

public class SessWorkSheet {

    public static final String FRM_NAMA_PERIODE = "FRM_NAMA_PERIODE";

    /** gadnyana
     * untuk mencari data neraca lajur
     * @param oidPeriod
     * @param typePrice
     * @return
     */
    synchronized public static Vector getListWorkSheet(long oidPeriod, long typePrice) {
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
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
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
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " ORDER BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];

            System.out.println("sql getListWorkSheet ::: " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                WorkSheet workSheet = new WorkSheet();
                workSheet.setNomorPerkiraan(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]));
                workSheet.setNamaPerkiraan(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]));
                workSheet.setAccountNameEnglish(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]));
                workSheet.setAnggaran(rs.getDouble("BGT"));

                // mutasi
                double totDebet = rs.getDouble("DBT");
                double totKredit = rs.getDouble("KRDT");
                workSheet.setDebetMutasi(totDebet);
                workSheet.setKreditMutasi(totKredit);
                workSheet.setTandaDebetKredit(rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]));

                // saldo awal
                SaldoAkhirPeriode saldoAkhirPeriode = getSaldoAwal(preOidPeriod, rs.getLong(PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]));
                double totSalDebet = saldoAkhirPeriode.getDebet();
                double totSalKredit = saldoAkhirPeriode.getKredit();
                if (workSheet.getTandaDebetKredit() == PstPerkiraan.ACC_DEBETSIGN) {
                    if (totSalDebet != 0) {
                        workSheet.setDebetNeracaPeriodeLalu(totSalDebet);
                    } else if (totSalKredit != 0) {
                        workSheet.setDebetNeracaPeriodeLalu(totSalKredit);
                    }
                } else {
                    if (totSalDebet != 0) {
                        workSheet.setKreditNeracaPeriodeLalu(totSalDebet);
                    } else if (totSalKredit != 0) {
                        workSheet.setKreditNeracaPeriodeLalu(totSalKredit);
                    }
                } // -- end ----

                // get neraca saldo
                double totNeracaPerLalu = 0.0;
                double totMutasi = 0.0;
                double totNeracaSaldo = 0.0;
                if (workSheet.getTandaDebetKredit() == PstPerkiraan.ACC_DEBETSIGN) {
                    totNeracaPerLalu = workSheet.getDebetNeracaPeriodeLalu() - workSheet.getKreditNeracaPeriodeLalu();
                    totMutasi = workSheet.getDebetMutasi() - workSheet.getKreditMutasi();
                    totNeracaSaldo = totNeracaPerLalu + totMutasi;
                    workSheet.setDebetNeracaSaldo(totNeracaSaldo);
                } else {
                    totNeracaPerLalu = workSheet.getKreditNeracaPeriodeLalu() - workSheet.getDebetNeracaPeriodeLalu();
                    totMutasi = workSheet.getKreditMutasi() - workSheet.getDebetMutasi();
                    totNeracaSaldo = totNeracaPerLalu + totMutasi;
                    workSheet.setKreditNeracaSaldo(totNeracaSaldo);
                } // -- end ----

                // get laba rugi
                if (rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]) == PstPerkiraan.ACC_GROUP_REVENUE ||
                        rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]) == PstPerkiraan.ACC_GROUP_EXPENSE ||
                        rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]) == PstPerkiraan.ACC_GROUP_COST_OF_SALES ||
                        rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]) == PstPerkiraan.ACC_GROUP_OTHER_REVENUE ||
                        rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]) == PstPerkiraan.ACC_GROUP_OTHER_EXPENSE) {

                    workSheet.setDebetLabaRugi(workSheet.getDebetNeracaSaldo());
                    workSheet.setKreditLabaRugi(workSheet.getKreditNeracaSaldo());
                } else {
                    workSheet.setDebetNeraca(workSheet.getDebetNeracaSaldo());
                    workSheet.setKreditNeraca(workSheet.getKreditNeracaSaldo());
                }
                result.add(workSheet);
            }
            rs.close();

        } catch (Exception e) {
        }
        return result;
    }

    /** gadnyana
     * untuk mencari laporan work
     * sheet dengan parameter per department
     * @param oidPeriod
     * @param typePrice
     * @return
     */
   synchronized public static Vector getListWorkSheet(long oidPeriod, long bookType, long departmentOid) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            long preOidPeriod = getOidPeriodeLalu(oidPeriod);
            String sql = "";            
            sql = "DELETE FROM aiso_tmpn_buffer";
            DBHandler.execUpdate(sql);

            sql = " INSERT INTO aiso_tmpn_buffer (ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,ACCOUNT_NAME_ENGLISH,TANDA_DEBET_KREDIT,GROUP_ACCOUNT," +
                    " SALDO_AWAL_DEBET,SALDO_AWAL_KREDIT) " +
                    " SELECT AP." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    ", AP." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                    ", AP." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    ", AP." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                    ", AP." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    ", AP." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    ", SAP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET] +
                    ", SAP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT] +
                    " FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR + " AS SAP " +
                    "  INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS AP " +
                    " ON AP." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = SAP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
                    " WHERE SAP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] + "=" + preOidPeriod;
            if(departmentOid != 0){
               sql = sql +" AND AP." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_DEPARTMENT_ID] + " = " + departmentOid;
            }    
            //System.out.println("SQL INSERT SALDO AWAL ===> " + sql);
            DBHandler.execUpdate(sql);

            sql = " INSERT INTO aiso_tmpn_buffer (ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,ACCOUNT_NAME_ENGLISH,TANDA_DEBET_KREDIT,GROUP_ACCOUNT,BGT,DBT,KRDT) " +
                    " SELECT ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
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
                    " WHERE JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID] + " = " + oidPeriod +
                    " AND JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE]+" = "+I_JournalType.TIPE_JURNAL_UMUM;
            if(departmentOid != 0){
             sql = sql + " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_DEPARTMENT_ID] + " = " + departmentOid +                    
                    " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP];
            }else{
             sql = sql + " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP];
            }
            
            //System.out.println("SQL INSERT MUTASI DARI JURNAL UMUM : " + sql);
            DBHandler.execUpdate(sql);

            sql = " INSERT INTO aiso_tmpn_buffer (ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,ACCOUNT_NAME_ENGLISH,TANDA_DEBET_KREDIT,BGT,AMOUNT_MAIN,AMOUNT_DETAIL,GROUP_ACCOUNT)" +
                  " SELECT ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+
                  ", SUM(BUD." +PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGET]+") AS BGT "+
                  ", SUM(JM." +PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT_STATUS]+" * "+
                  "  JM." +PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT]+") AS AMOUNT_MAIN "+
                  ", 0 AS AMOUNT_DETAIL "+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
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
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
                  "  ORDER BY ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];
            
            //System.out.println("Insert Mutasi dari Special Journal Main ===> " + sql);
            DBHandler.execUpdate(sql);
            
            sql = "  INSERT INTO aiso_tmpn_buffer (ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,ACCOUNT_NAME_ENGLISH,TANDA_DEBET_KREDIT,BGT,AMOUNT_MAIN,AMOUNT_DETAIL,GROUP_ACCOUNT)" +
                  "  SELECT ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+
                  ", SUM(BUD." +PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGET]+") AS BGT "+
                  ", 0 AS AMOUNT_MAIN "+
                  ", SUM(SD." +PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT_STATUS]+" * "+
                  "  SD." +PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT]+") AS AMOUNT_DETAIL "+
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
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
                  ", ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
                  "  ORDER BY ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];
                  
            //System.out.println("Insert Mutasi dari Special Journal Detail ===> " + sql);
            DBHandler.execUpdate(sql);
            
            sql = "SELECT ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,ACCOUNT_NAME_ENGLISH, TANDA_DEBET_KREDIT,GROUP_ACCOUNT," +
                    " SUM(BGT),SUM(SALDO_AWAL_DEBET),SUM(SALDO_AWAL_KREDIT)," +
                    " SUM(DBT),SUM(KRDT),SUM(AMOUNT_MAIN),SUM(AMOUNT_DETAIL) FROM aiso_tmpn_buffer" +
                    " GROUP BY ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,ACCOUNT_NAME_ENGLISH,TANDA_DEBET_KREDIT,GROUP_ACCOUNT ORDER BY NOMOR_PERKIRAAN";
            
            System.out.println("SQL VIEW LIST WORKSHEET ===> " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                WorkSheet workSheet = new WorkSheet();
                workSheet.setNomorPerkiraan(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]));
                workSheet.setNamaPerkiraan(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]));
                workSheet.setAccountNameEnglish(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]));
                workSheet.setAnggaran(rs.getDouble(7));

                // mutasi                
                double totDebet = 0.0;
                double totKredit = 0.0;
                double amountMain = 0.0;
                double amountDetail = 0.0;
                int intDebetCreditAss = rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]);
                totDebet = rs.getDouble(10);
                totKredit = rs.getDouble(11);
                amountMain = rs.getDouble(12);
                amountDetail = rs.getDouble(13);
                
                if(intDebetCreditAss == PstPerkiraan.ACC_DEBETSIGN){
                       if(amountMain < 0 && amountMain != 0){
                            workSheet.setDebetMutasi(totDebet+amountDetail);
                            workSheet.setKreditMutasi(totKredit+(amountMain * -1));
                       }
                       if(amountDetail < 0 && amountDetail != 0){
                            workSheet.setDebetMutasi(totDebet + amountMain);
                            workSheet.setKreditMutasi(totKredit +(amountDetail * -1));
                       }
                       
                       if(amountDetail == 0 && amountMain > 0){
                            workSheet.setDebetMutasi(totDebet + amountMain);
                            workSheet.setKreditMutasi(totKredit);
                       }
                       
                       if(amountDetail > 0 && amountMain == 0){
                            workSheet.setDebetMutasi(totDebet + amountDetail);
                            workSheet.setKreditMutasi(totKredit);
                       }
                       
                       if(amountDetail == 0 && amountMain == 0){
                            workSheet.setDebetMutasi(totDebet);
                            workSheet.setKreditMutasi(totKredit);
                       }
                       
                       if(amountDetail > 0 && amountMain > 0){
                            workSheet.setDebetMutasi(totDebet+amountDetail+amountMain);
                            workSheet.setKreditMutasi(totKredit);
                       }
                       
                       if(amountDetail > 0 && amountMain < 0){
                            workSheet.setDebetMutasi(totDebet+amountDetail);
                            workSheet.setKreditMutasi(totKredit+(amountMain * -1));
                       }
                       
                       if(amountDetail < 0 && amountMain > 0){
                            workSheet.setDebetMutasi(totDebet+amountMain);
                            workSheet.setKreditMutasi(totKredit+(amountDetail * -1));
                       }
                       
                   }else{  
                         if(amountMain > 0 && amountDetail == 0){
                            workSheet.setDebetMutasi(totDebet);
                            workSheet.setKreditMutasi(totKredit + amountMain);
                        }
                        if(amountMain < 0 && amountDetail == 0){
                            workSheet.setDebetMutasi(totDebet + (amountMain * -1));
                            workSheet.setKreditMutasi(totKredit);
                        }
                        if(amountMain > 0 && amountDetail < 0){
                            workSheet.setDebetMutasi(totDebet + (amountDetail * -1));
                            workSheet.setKreditMutasi(totKredit + amountMain);
                        }
                        if(amountMain > 0 && amountDetail > 0){
                            workSheet.setDebetMutasi(totDebet );
                            workSheet.setKreditMutasi(totKredit + amountMain + amountDetail);
                        }
                        if(amountMain < 0 && amountDetail < 0){
                            workSheet.setDebetMutasi(totDebet + (amountMain * -1) + (amountDetail * -1) );
                            workSheet.setKreditMutasi(totKredit );
                        }
                        if(amountMain == 0 && amountDetail > 0){
                            workSheet.setDebetMutasi(totDebet );
                            workSheet.setKreditMutasi(totKredit + amountDetail);
                        }
                        if(amountMain == 0 && amountDetail < 0){
                            workSheet.setDebetMutasi(totDebet + (amountDetail * -1));
                            workSheet.setKreditMutasi(totKredit);
                        }
                        if(amountMain == 0 && amountDetail == 0){
                            workSheet.setDebetMutasi(totDebet );
                            workSheet.setKreditMutasi(totKredit);
                        }
                       } 
              
                workSheet.setTandaDebetKredit(intDebetCreditAss);

                // saldo awal                
                double totSalDebet = rs.getDouble(8);
                double totSalKredit = rs.getDouble(9);

                workSheet.setDebetNeracaPeriodeLalu(totSalDebet);
                workSheet.setKreditNeracaPeriodeLalu(totSalKredit);
                // -- end ----

                // get neraca saldo
                double totNeracaPerLalu = 0.0;
                double totMutasi = 0.0;
                double totNeracaSaldo = 0.0;
                if (workSheet.getTandaDebetKredit() == PstPerkiraan.ACC_DEBETSIGN) {
                    totNeracaPerLalu = workSheet.getDebetNeracaPeriodeLalu() - workSheet.getKreditNeracaPeriodeLalu();
                    totMutasi = workSheet.getDebetMutasi() - workSheet.getKreditMutasi();
                    totNeracaSaldo = totNeracaPerLalu + totMutasi;
                    if(totNeracaSaldo<0)
                        workSheet.setKreditNeracaSaldo(totNeracaSaldo * -1);
                    else
                        workSheet.setDebetNeracaSaldo(totNeracaSaldo);

                } else {
                    totNeracaPerLalu = workSheet.getKreditNeracaPeriodeLalu() - workSheet.getDebetNeracaPeriodeLalu();
                    totMutasi = workSheet.getKreditMutasi() - workSheet.getDebetMutasi();
                    totNeracaSaldo = totNeracaPerLalu + totMutasi;
                    if(totNeracaSaldo<0)
                        workSheet.setDebetNeracaSaldo(totNeracaSaldo * -1);
                    else
                        workSheet.setKreditNeracaSaldo(totNeracaSaldo);
                    //workSheet.setKreditNeracaSaldo(totNeracaSaldo);
                } // -- end ----

                // get laba rugi
                if (rs.getInt("GROUP_ACCOUNT") == PstPerkiraan.ACC_GROUP_REVENUE ||
                        rs.getInt("GROUP_ACCOUNT") == PstPerkiraan.ACC_GROUP_EXPENSE ||
                        rs.getInt("GROUP_ACCOUNT") == PstPerkiraan.ACC_GROUP_COST_OF_SALES ||
                        rs.getInt("GROUP_ACCOUNT") == PstPerkiraan.ACC_GROUP_OTHER_REVENUE ||
                        rs.getInt("GROUP_ACCOUNT") == PstPerkiraan.ACC_GROUP_OTHER_EXPENSE) {

                    workSheet.setDebetLabaRugi(workSheet.getDebetNeracaSaldo());
                    workSheet.setKreditLabaRugi(workSheet.getKreditNeracaSaldo());
                } else {
                    workSheet.setDebetNeraca(workSheet.getDebetNeracaSaldo());
                    workSheet.setKreditNeraca(workSheet.getKreditNeracaSaldo());
                }
                result.add(workSheet);
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
            //System.out.println("oidPeriod dalam method ====>"+oidPeriod);
            Periode periode = PstPeriode.fetchExc(oidPeriod);
            //System.out.println("objperiode ===> "+periode);            
            String where = PstPeriode.fieldNames[PstPeriode.FLD_TGLAKHIR] + " < '" + Formater.formatDate(periode.getTglAwal(), "yyyy-MM-dd") + "'";
            String order = PstPeriode.fieldNames[PstPeriode.FLD_TGLAKHIR] + " DESC ";           
            Vector list = PstPeriode.list(0, 0, where, order);
            
            //System.out.println("Besar vector list pada method getOidPeriodeLalu ===> "+list.size());
            if (list.size() > 0) {
                Periode prd = (Periode) list.get(0);
                where = PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] + "=" + prd.getOID();
                Vector listSaldo = PstSaldoAkhirPeriode.list(0, 0, where, "");
                
                if (listSaldo.size() > 0) {
                    SaldoAkhirPeriode saldoAkhirPeriode = (SaldoAkhirPeriode) listSaldo.get(0);
                    oidLalu = saldoAkhirPeriode.getOID();
                    //System.out.println("Besar  oidLalu pada method getOidPeriodeLalu ===> "+oidLalu);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception pada method get periode lalu =====> "+e.toString());
        }
        //System.out.println("OidLalu pada method getOidPeriodeLalu on return ===> "+oidLalu);
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

    /** gadnyana
     * ini untuk mecari salso akhir periode
     * dengan meng-group id perkiraan, khusus untuk laporan cash flow
     * menjadi saldo awal periode pencarian
     * @param oidPeriod
     * @param oidMaterial
     * @return
     */
    public static SaldoAkhirPeriode getSaldoAwal(long oidPeriod) {
        DBResultSet dbrs = null;
        SaldoAkhirPeriode saldoAkhirPeriode = new SaldoAkhirPeriode();
        try {
            String sql = "SELECT SUM(" + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET] + ")," +
                    " SUM(" + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET] + ") " + 
                    " FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR + " AS SP " +
                    " INNER JOIN " + PstAccountLink.TBL_ACCOUNT_LINK + " AS LK " +
                    " ON SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
                    " = LK." + PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID] +
                    " WHERE SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] + "=" + oidPeriod +
                    " AND LK." + PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE] + "=" + I_ChartOfAccountGroup.ACC_GROUP_CASH +
                    " GROUP BY SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE];

            //System.out.println("SQL METHOD GET SALDO AWAL ===> "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                saldoAkhirPeriode.setDebet(rs.getDouble(1));
                saldoAkhirPeriode.setKredit(rs.getDouble(2));
            }
        } catch (Exception e) {
            System.out.println("getSaldoAwal : " + e.toString());
        }
        return saldoAkhirPeriode;
    }
    
     public static SaldoAkhirPeriode getSoAwalCash(long oidPeriod, long oidDepartment, SrcCashFlow srcCashFlow) {
         
         long oidLastPeriod = 0;
         try{
            oidLastPeriod = getOidPeriodeLalu(oidPeriod);
         }catch(Exception e){}
         
         Periode objPeriod = new Periode();
        try{
            objPeriod = PstPeriode.fetchExc(oidPeriod);
        }catch(Exception e){
            System.out.println("Exception on fetch objPerod : "+e.toString());
            objPeriod = new Periode();
        }
        
        DBResultSet dbrs = null;
        SaldoAkhirPeriode saldoAkhirPeriode = new SaldoAkhirPeriode();
        try {
            String sql = "SELECT SUM(" + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET] + ")," +
                    " SUM(" + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT] + ") " + 
                    " FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR + " AS SP " +
                    " INNER JOIN " + PstAccountLink.TBL_ACCOUNT_LINK + " AS LK " +
                    " ON SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
                    " = LK." + PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID] +  
                    " INNER JOIN "+ PstPerkiraan.TBL_PERKIRAAN+ " AS PERK" +
                    " ON SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
                    " = PERK." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                    " INNER JOIN "+PstDepartment.TBL_HR_DEPARTMENT+ " AS DEP "+ 
                    " ON DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] +
                    " = PERK."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_DEPARTMENT_ID]+ 
                    " WHERE SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] + "=" + oidLastPeriod +                    
                    " AND LK." + PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE] + "=" + I_ChartOfAccountGroup.ACC_GROUP_CASH;
            if(oidDepartment != 0){
            sql = sql + " AND DEP."+ PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]+" = "+oidDepartment+
                    " GROUP BY SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE];
            }else{
               sql = sql +" GROUP BY SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE];
            }

            //System.out.println("SQL METHOD GET SALDO AWAL CASH FLOW CASH ===> "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                saldoAkhirPeriode.setDebet(rs.getDouble(1));
                saldoAkhirPeriode.setKredit(rs.getDouble(2));
            }
            double soCash = 0;
            double soCashDebet = 0;
            double soCashCredit = 0;
            
            soCashDebet = saldoAkhirPeriode.getDebet();
            soCashCredit = saldoAkhirPeriode.getKredit();
            Date dateFr = (Date)srcCashFlow.getDateFrom();
            Date dateTo = (Date)srcCashFlow.getDateTo();
            if(objPeriod.getPosted() == PstPeriode.PERIOD_OPEN){
                if(srcCashFlow.getDateFrom().after(objPeriod.getTglAwal())){
                    srcCashFlow.setDateFrom(objPeriod.getTglAwal());
                    srcCashFlow.setDateTo(srcCashFlow.getDateFrom()); 
                    soCash = getSaldoCash(oidPeriod, oidDepartment, srcCashFlow);
                    srcCashFlow.setDateFrom(dateFr);
                    srcCashFlow.setDateTo(dateTo); 
                    if(soCash > 0){
                        saldoAkhirPeriode.setDebet(soCashDebet + soCash);
                    }else{
                        saldoAkhirPeriode.setKredit(soCashCredit + (soCash * -1));
                    }
                 }
            }
          
            rs.close();
        } catch (Exception e) {
            System.out.println("getSaldoAwalCashFlowCash : " + e.toString());
        }finally{
            dbrs.close(dbrs);
        }
        return saldoAkhirPeriode;
    }
     
      public static SaldoAkhirPeriode getSoAwalBank(long oidPeriod, long oidDepartment, SrcCashFlow srcCashFlow) {        
         long oidLastPeriod = 0;
         try{
            oidLastPeriod = getOidPeriodeLalu(oidPeriod);
         }catch(Exception e){}
         
        Periode objPeriod = new Periode();
        try{
            objPeriod = PstPeriode.fetchExc(oidPeriod);
        }catch(Exception e){objPeriod = new Periode();}  
        DBResultSet dbrs = null;
        SaldoAkhirPeriode saldoAkhirPeriode = new SaldoAkhirPeriode();
        try {
            String sql = "SELECT SUM(" + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET] + ")," +
                    " SUM(" + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT] + ") " + 
                    " FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR + " AS SP " +
                    " INNER JOIN " + PstAccountLink.TBL_ACCOUNT_LINK + " AS LK " +
                    " ON SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
                    " = LK." + PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID] +  
                    " INNER JOIN "+ PstPerkiraan.TBL_PERKIRAAN+ " AS PERK" +
                    " ON SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
                    " = PERK." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                    " INNER JOIN "+PstDepartment.TBL_HR_DEPARTMENT+ " AS DEP "+ 
                    " ON DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] +
                    " = PERK."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_DEPARTMENT_ID]+ 
                    " WHERE SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] + "=" + oidLastPeriod +                    
                    " AND LK." + PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE] + "=" + I_ChartOfAccountGroup.ACC_GROUP_BANK;
            if(oidDepartment != 0){
                sql = sql + " AND DEP."+ PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]+" = "+oidDepartment+
                    " GROUP BY SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE];
            }else{
                sql = sql + " GROUP BY SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE];
            }

            //System.out.println("SQL METHOD GET SALDO AWAL CASH FLOW BANK ===> "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                saldoAkhirPeriode.setDebet(rs.getDouble(1));
                saldoAkhirPeriode.setKredit(rs.getDouble(2));
            }
            
            double soBank = 0;
            double soBankDebet = 0;
            double soBankCredit = 0;
            
            soBankDebet = saldoAkhirPeriode.getDebet();
            soBankCredit = saldoAkhirPeriode.getKredit();
            
            Date dateFr = (Date)srcCashFlow.getDateFrom();
            Date dateTo = (Date)srcCashFlow.getDateTo();
             if(objPeriod.getPosted() == PstPeriode.PERIOD_OPEN){
                if(srcCashFlow.getDateFrom().after(objPeriod.getTglAwal())){
                    srcCashFlow.setDateFrom(objPeriod.getTglAwal());
                    srcCashFlow.setDateTo(srcCashFlow.getDateFrom()); 
                    soBank = getSaldoBank(oidPeriod, oidDepartment, srcCashFlow);
                    srcCashFlow.setDateFrom(dateFr);
                    srcCashFlow.setDateTo(dateTo); 
                    if(soBank > 0){
                        saldoAkhirPeriode.setDebet(soBankDebet + soBank);
                    }else{
                        saldoAkhirPeriode.setKredit(soBankCredit + (soBank * -1));
                    }
                 }
            }
            
            rs.close();
        } catch (Exception e) {
            System.out.println("getSaldoAwalCashFlowBank : " + e.toString());
        }finally{
            dbrs.close(dbrs);
        }
        return saldoAkhirPeriode;
    }
      
       public static SaldoAkhirPeriode getSoAwalPettyCash(long oidPeriod, long oidDepartment, SrcCashFlow srcCashFlow) {
         long oidLastPeriod = 0;
         try{
            oidLastPeriod = getOidPeriodeLalu(oidPeriod);
         }catch(Exception e){}
         
        Periode objPeriod = new Periode();
        try{
            objPeriod = PstPeriode.fetchExc(oidPeriod);
        }catch(Exception e){objPeriod = new Periode();}     
        DBResultSet dbrs = null;
        SaldoAkhirPeriode saldoAkhirPeriode = new SaldoAkhirPeriode();
        try {
            String sql = "SELECT SUM(" + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET] + ")," +
                    " SUM(" + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT] + ") " + 
                    " FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR + " AS SP " +
                    " INNER JOIN " + PstAccountLink.TBL_ACCOUNT_LINK + " AS LK " +
                    " ON SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
                    " = LK." + PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID] +  
                    " INNER JOIN "+ PstPerkiraan.TBL_PERKIRAAN+ " AS PERK" +
                    " ON SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
                    " = PERK." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                    " INNER JOIN "+PstDepartment.TBL_HR_DEPARTMENT+ " AS DEP "+ 
                    " ON DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] +
                    " = PERK."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_DEPARTMENT_ID]+ 
                    " WHERE SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] + "=" + oidLastPeriod +                    
                    " AND LK." + PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE] + "=" + I_ChartOfAccountGroup.ACC_GROUP_PETTY_CASH;
            if(oidDepartment != 0){
               sql = sql +" AND DEP."+ PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]+" = "+oidDepartment+
                    " GROUP BY SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE];
            }else{
               sql = sql +" GROUP BY SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE];
            }

            //System.out.println("SQL METHOD GET SALDO AWAL CASH FLOW PETTY CASH ===> "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                saldoAkhirPeriode.setDebet(rs.getDouble(1));
                saldoAkhirPeriode.setKredit(rs.getDouble(2));
            }
           
            double soPettyCash = 0;
            double soPettyCashDebet = 0;
            double soPettyCashCredit = 0;
            
            soPettyCashDebet = saldoAkhirPeriode.getDebet();
            soPettyCashCredit = saldoAkhirPeriode.getKredit();
            
            Date dateFr = (Date)srcCashFlow.getDateFrom();
            Date dateTo = (Date)srcCashFlow.getDateTo();
            
             if(objPeriod.getPosted() == PstPeriode.PERIOD_OPEN){
                if(srcCashFlow.getDateFrom().after(objPeriod.getTglAwal())){
                    srcCashFlow.setDateFrom(objPeriod.getTglAwal());
                    srcCashFlow.setDateTo(srcCashFlow.getDateFrom()); 
                    soPettyCash = getSaldoPettyCash(oidPeriod, oidDepartment, srcCashFlow);
                    System.out.println("soPettyCash : "+soPettyCash);
                    srcCashFlow.setDateFrom(dateFr);
                    srcCashFlow.setDateTo(dateTo);
                    if(soPettyCash > 0){
                        saldoAkhirPeriode.setDebet(soPettyCashDebet + soPettyCash);
                    }else{
                        saldoAkhirPeriode.setKredit(soPettyCashCredit + (soPettyCash * -1));
                    }
                 }
            }
            
           rs.close();
        } catch (Exception e) {
            System.out.println("getSaldoAwalCashFlowPettyCash : " + e.toString());
        }finally{
            dbrs.close(dbrs);
        }
        return saldoAkhirPeriode;
    }
     
     public static Vector getSaldoAwalCashFlow(long oidPeriod, long oidDepartment, SrcCashFlow srcCashFlow){
        Vector vSaldoAwal = new Vector();
        try{
            vSaldoAwal.add(getSoAwalCash(oidPeriod, oidDepartment, srcCashFlow));
            vSaldoAwal.add(getSoAwalBank(oidPeriod, oidDepartment, srcCashFlow));
            vSaldoAwal.add(getSoAwalPettyCash(oidPeriod, oidDepartment, srcCashFlow));
        }catch(Exception e){
            System.out.println("Exception on get saldo awal cash, bank, petty cash"+e.toString());
        }
        return vSaldoAwal;
     }

    /**
     *
     * @param oidPeriod
     * @return
     */
    public static AisoBudgeting getBudgetAwal(long oidPeriod) {
        DBResultSet dbrs = null;
        AisoBudgeting aisoBudgeting = new AisoBudgeting();
        try {
            String sql = "SELECT SUM(" + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGET] + ") " +
                    " FROM " + PstAisoBudgeting.TBL_AISO_BUDGETING + " AS SP " +
                    " INNER JOIN " + PstAccountLink.TBL_ACCOUNT_LINK + " AS LK " +
                    " ON SP." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN] +
                    " = LK." + PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID] +
                    " WHERE SP." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_PERIODE_ID] + "=" + oidPeriod +
                    " AND LK." + PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE] + "=" + I_ChartOfAccountGroup.ACC_GROUP_CASH +
                    " GROUP BY SP." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_PERIODE_ID];

            //System.out.println("getSaldoAwalBudget : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                aisoBudgeting.setBudget(rs.getDouble(1));
            }
        } catch (Exception e) {
            System.out.println("getSaldoAwal : " + e.toString());
        }
        return aisoBudgeting;
    }

    /** gadnyana
     * untuk mencari nama
     * lain dari laba periode berjalan
     * @return
     */
    public static Perkiraan getNamaSaldoPeriodeBerjalan(long departmentOid) {
        Perkiraan perk = new Perkiraan();
        try {
            Vector list = PstAccountLink.getVectObjListAccountLink(departmentOid,I_ChartOfAccountGroup.ACC_GROUP_PERIOD_EARNING);
            if (list != null && list.size() > 0) {
                perk = (Perkiraan) list.get(0);                
            }
        } catch (Exception e) {
            System.out.println("Exception on fetch perkiraan laba rugi periode berjalan ===> "+e.toString());
        }
        return perk;
    }
    
    /** dwi
     * @param long idDepartment
     * @param int account group 
     * @return object perkiraan earning
     */
    
    public static Perkiraan getObjectAccountEarning(long departmentOid, int accGroup) {
        Perkiraan objAccountEarning = new Perkiraan();
        try {
            Vector list = PstAccountLink.getVectObjListAccountLink(departmentOid,accGroup);
            if (list != null && list.size() > 0) {
                objAccountEarning = (Perkiraan) list.get(0);                
            }
        } catch (Exception e) {
            objAccountEarning = new Perkiraan();
            System.out.println("Exception on getObjectAccountEarning ===> "+e.toString());
        }
        return objAccountEarning;
    }
    
    public static double getSaldoCash(long lPeriodId, long lDepartmentId, SrcCashFlow srcCashFlow){
        Vector vDebetCash = new Vector(1,1);
        Vector vCreditCash = new Vector(1,1);
        double dDebet = 0;
        double dKredit = 0;
            try{
                vDebetCash = (Vector)SessCashFlow.getListAccountCashFlowDebetCash(lPeriodId, lDepartmentId, srcCashFlow);
                vCreditCash = (Vector)SessCashFlow.getListAccountCashFlowKreditCash(lPeriodId, lDepartmentId, srcCashFlow);
                dDebet = getSaldo(vDebetCash);
                dKredit = getSaldo(vCreditCash);
                return dDebet - dKredit;
            }catch(Exception e){
            }
        return 0;
    }
    
     public static double getSaldoPettyCash(long lPeriodId, long lDepartmentId, SrcCashFlow srcCashFlow){
        Vector vDebetPettyCash = new Vector(1,1);
        Vector vCreditPettyCash = new Vector(1,1);
        double dDebet = 0;
        double dKredit = 0;
            try{
                vDebetPettyCash = (Vector)SessCashFlow.getListAccountCashFlowDebetPettyCash(lPeriodId, lDepartmentId, srcCashFlow);
                vCreditPettyCash = (Vector)SessCashFlow.getListAccountCashFlowKreditPettyCash(lPeriodId, lDepartmentId, srcCashFlow);
                dDebet = getSaldo(vDebetPettyCash);
                dKredit = getSaldo(vCreditPettyCash);
                System.out.println("dDebet - dKredit petty cash "+(dDebet - dKredit));
                return dDebet - dKredit;
            }catch(Exception e){
            }
        return 0;
    }
    
     public static double getSaldoBank(long lPeriodId, long lDepartmentId, SrcCashFlow srcCashFlow){
        Vector vDebetBank = new Vector(1,1);
        Vector vCreditBank = new Vector(1,1);
        double dDebet = 0;
        double dKredit = 0;
            try{
                vDebetBank = (Vector)SessCashFlow.getListAccountCashFlowDebetBank(lPeriodId, lDepartmentId, srcCashFlow);
                vCreditBank = (Vector)SessCashFlow.getListAccountCashFlowKreditBank(lPeriodId, lDepartmentId, srcCashFlow);
                dDebet = getSaldo(vDebetBank);
                dKredit = getSaldo(vCreditBank);
                return dDebet - dKredit;
            }catch(Exception e){
            }
        return 0;
    }
     
    public static double getSaldo(Vector vSaldo){
        CashFlow objCashFlow = new CashFlow();
        double saldo = 0;
        if(vSaldo != null && vSaldo.size() > 0){
            for(int i = 0; i < vSaldo.size(); i++){
                objCashFlow = (CashFlow)vSaldo.get(i);
                saldo += objCashFlow.getValue();
            }
            return saldo;
        }
        return 0;
    }
    
    public static void main(String[] args){
        SessWorkSheet sessWorkSheet = new SessWorkSheet();
        SaldoAkhirPeriode objSaldoAkhirPeriode = new SaldoAkhirPeriode();
        //objSaldoAkhirPeriode = sessWorkSheet.getSoAwalCash(504404311048387164L, 22000);
        System.out.println("objSaldoAkhirPeriode"+objSaldoAkhirPeriode.getDebet());
    }

}
