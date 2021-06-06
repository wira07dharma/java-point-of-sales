/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Aug 23, 2005
 * Time: 4:16:32 PM
 * To change this template use Options | File Templates.
 */
package com.dimata.aiso.session.report;

import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.entity.masterdata.PstPerkiraan;
import com.dimata.aiso.entity.masterdata.PstAisoBudgeting;
import com.dimata.aiso.entity.jurnal.PstJurnalDetail;
import com.dimata.aiso.entity.jurnal.PstJurnalUmum;
import com.dimata.aiso.entity.jurnal.SaldoAkhirPeriode;
import com.dimata.aiso.entity.jurnal.PstSaldoAkhirPeriode;
import com.dimata.aiso.entity.masterdata.Perkiraan;
import com.dimata.aiso.entity.periode.Periode;
import com.dimata.aiso.entity.periode.PstPeriode;
import com.dimata.aiso.entity.report.WorkSheet;
import com.dimata.aiso.entity.report.Neraca;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalDetail;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalMain;
import com.dimata.interfaces.chartofaccount.I_ChartOfAccountGroup;
import com.dimata.interfaces.journal.I_JournalType;
import com.dimata.util.Formater;

import java.util.Vector;
import java.sql.ResultSet;
import java.util.Date;

public class SessNeraca {
    
    
    /** gadnyana
     * untuk menampilkan data
     * @param oidPeriod
     * @param departmentOid
     * @return
     */
    synchronized public static Vector getListNeraca(long oidPeriod, long departmentOid) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            
            
            String sql = "DELETE FROM aiso_tmp_buffer";
            DBHandler.execUpdate(sql);
            Periode objPeriod = PstPeriode.fetchExc(oidPeriod);
            long preOidPeriod = SessWorkSheet.getOidPeriodeLalu(oidPeriod);
            sql = " INSERT INTO aiso_tmp_buffer (ID_PERKIRAAN,ID_PARENT,NOMOR_PERKIRAAN,NAMA,ACCOUNT_NAME_ENGLISH," +
            "TANDA_DEBET_KREDIT," +
            "GROUP_ACCOUNT,BGT,DBT,KRDT)" +           
            " SELECT " +
            " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
            ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT] +
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
            " AND (ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +
            " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_FIXED_ASSETS +
            " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_OTHER_ASSETS +
            " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_CURRENCT_LIABILITIES +
            " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_LONG_TERM_LIABILITIES +
            " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_EQUITY + ")" +
            " AND (JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE] + "=" + I_JournalType.TIPE_JURNAL_UMUM +
            " OR JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE] + "=" + I_JournalType.TIPE_JURNAL_PENUTUP_1 + ")";
            
            String groupBy = " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                        " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT] +
                        ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                        ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                        ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                        ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                        ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP];
            
            if(departmentOid != 0){
                String where = " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_DEPARTMENT_ID] + "=" + departmentOid;
                sql = sql + where + groupBy;
            }else{
                sql = sql + groupBy;
            }
       
            DBHandler.execUpdate(sql);
            
            insertCashIn(oidPeriod);
            insertCashOut(oidPeriod);
            insertPettyCashIn(oidPeriod);
            insertPettyCashOut(oidPeriod);
            insertBankDeposite(oidPeriod);
            insertBankReceiveFund(oidPeriod);
            insertBankTransferIn(oidPeriod);
            insertChequeRequest(oidPeriod);
            insertBankTransferOut(oidPeriod);
            insertBankPayment(oidPeriod);
            insertPettyCashReplacement(oidPeriod);
            insertFunding(oidPeriod);
            insertPayment(oidPeriod);
            insertNonCash(oidPeriod);
            if(objPeriod.getPosted() == PstPeriode.PERIOD_OPEN){
                queryInsertPL(departmentOid,oidPeriod);
            }
            
            sql = " INSERT INTO aiso_tmp_buffer (ID_PERKIRAAN,ID_PARENT,NOMOR_PERKIRAAN,NAMA,ACCOUNT_NAME_ENGLISH,TANDA_DEBET_KREDIT," +
            "GROUP_ACCOUNT,BGT,SALDO_AWAL_DEBET,SALDO_AWAL_KREDIT)" +
            " SELECT " +
            " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
            ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT] +
            ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
            ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
            ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
            ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
            ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
            ", SUM(BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGET] + ") AS BGT" +
            ", SUM(SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET] + ") AS DBT" +
            ", SUM(SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT] + ") AS KRDT " +
            " FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR + " AS SA " +
            " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC " +
            " ON SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
            " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
            " LEFT JOIN " + PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD " +
            " ON SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
            " = BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN] +
            " WHERE SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] + "= " + preOidPeriod +            
            " AND (ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +
            " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_FIXED_ASSETS +
            " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_OTHER_ASSETS +
            " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_CURRENCT_LIABILITIES +
            " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_LONG_TERM_LIABILITIES +
            " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_EQUITY + ")";
            
            String grpBy = " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                        " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT] +
                        ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                        ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                        ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                        ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                        ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP];
            
            if(departmentOid != 0){
                String where = " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_DEPARTMENT_ID] + "=" + departmentOid;
                sql = sql + where + grpBy;
            }else{
                sql = sql + grpBy;
            }
            
            DBHandler.execUpdate(sql);
            
            double dProfitLossPrevPeriod = 0.0;
            if(preOidPeriod != 0){
                dProfitLossPrevPeriod = getProfitLoss(preOidPeriod);
            }            
            
            Perkiraan objAccPeriodEarningPrev = (Perkiraan)SessWorkSheet.getObjectAccountEarning(departmentOid, I_ChartOfAccountGroup.ACC_GROUP_PERIOD_EARNING);             
            long idAccPeriodEarningPrev = objAccPeriodEarningPrev.getOID();
            
            Perkiraan objAccYearEarningPrev = (Perkiraan)SessWorkSheet.getObjectAccountEarning(departmentOid, I_ChartOfAccountGroup.ACC_GROUP_YEAR_EARNING); 
            long idAccYearEarningPrev = objAccYearEarningPrev.getOID();
            
            Perkiraan objAccRetainedEarningPrev = (Perkiraan)SessWorkSheet.getObjectAccountEarning(departmentOid, I_ChartOfAccountGroup.ACC_GROUP_RETAINED_EARNING);  
            long idAccRetainedEarningPrev = objAccRetainedEarningPrev.getOID();
            
            sql = " SELECT ID_PERKIRAAN,ID_PARENT,NOMOR_PERKIRAAN,NAMA,ACCOUNT_NAME_ENGLISH,TANDA_DEBET_KREDIT," +
            "GROUP_ACCOUNT,SUM(BGT) AS BGT" +
            ", SUM(DBT) AS TOT_DBT" +
            ", SUM(KRDT) AS TOT_KRDT " +
            ", SUM(SALDO_AWAL_DEBET) AS TOT_SADBT" +
            ", SUM(SALDO_AWAL_KREDIT) AS TOT_SAKRDT " +
            " FROM aiso_tmp_buffer " +
            " GROUP BY ID_PERKIRAAN " +
            ", NOMOR_PERKIRAAN" +
            ", NAMA" +
            ", ACCOUNT_NAME_ENGLISH" +
            ", TANDA_DEBET_KREDIT" +
            ", GROUP_ACCOUNT" +
            ", ID_PARENT "+
            " ORDER BY NOMOR_PERKIRAAN";
            
            System.out.println("sql neraca : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            // untuk menyimpan masing masing group account
            Vector listAL = new Vector(1, 1);
            Vector listAT = new Vector(1, 1);
            Vector listALL = new Vector(1, 1);
            Vector listHPendek = new Vector(1, 1);
            Vector listHPanjang = new Vector(1, 1);
            Vector listModal = new Vector(1, 1);
            
            boolean status = false;
            while (rs.next()) {
                status = true;
                
                WorkSheet workSheet = new WorkSheet();
                workSheet.setNomorPerkiraan(rs.getString("NOMOR_PERKIRAAN"));
                
                // mutasi
                double totDebet = rs.getDouble("TOT_DBT");
                double totKredit = rs.getDouble("TOT_KRDT");
                workSheet.setDebetMutasi(totDebet);
                workSheet.setKreditMutasi(totKredit);
                workSheet.setTandaDebetKredit(rs.getInt("TANDA_DEBET_KREDIT")); 
                
                // saldo awal                
                double totSalDebet = rs.getDouble("TOT_SADBT"); 
                double totSalKredit = rs.getDouble("TOT_SAKRDT"); 
                workSheet.setDebetNeracaPeriodeLalu(totSalDebet);
                workSheet.setKreditNeracaPeriodeLalu(totSalKredit);
                // -- end ----
                
                // get neraca saldo
                double totNeracaPerLaluDebet = 0.0;
                double totNeracaPerLaluCredit = 0.0;
                double totMutasi = 0.0;
                double totNeracaSaldo = 0.0;
                if (workSheet.getTandaDebetKredit() == PstPerkiraan.ACC_DEBETSIGN) {
                    totNeracaPerLaluDebet = workSheet.getDebetNeracaPeriodeLalu() - workSheet.getKreditNeracaPeriodeLalu();
                    totMutasi = workSheet.getDebetMutasi() - workSheet.getKreditMutasi();
                    totNeracaSaldo = totNeracaPerLaluDebet + totMutasi;
                    workSheet.setDebetNeracaSaldo(totNeracaSaldo);
                } else {
                    totNeracaPerLaluCredit = workSheet.getKreditNeracaPeriodeLalu() - workSheet.getDebetNeracaPeriodeLalu();
                    totMutasi = workSheet.getKreditMutasi() - workSheet.getDebetMutasi();
                    totNeracaSaldo = totNeracaPerLaluCredit + totMutasi;
                    workSheet.setKreditNeracaSaldo(totNeracaSaldo);
                } // -- end ----
                
                workSheet.setDebetNeraca(workSheet.getDebetNeracaSaldo());
                workSheet.setKreditNeraca(workSheet.getKreditNeracaSaldo());
                
                long idPerkiraan = rs.getLong("ID_PERKIRAAN");
                Neraca neraca = new Neraca();
                neraca.setNamaPerkiraan(rs.getString("NAMA"));
                neraca.setAccountNameEnglish(rs.getString("ACCOUNT_NAME_ENGLISH")); 
                neraca.setIdParent(rs.getLong("ID_PARENT"));
                neraca.setIdPerkiraan(idPerkiraan);
                double yearEarning = 0.0;
                Date startDateCurrPeriod = (Date)objPeriod.getTglAwal();
                
                if(totNeracaPerLaluDebet != 0)
                    neraca.setPrevValue(totNeracaPerLaluDebet);
                else
                    neraca.setPrevValue(totNeracaPerLaluCredit);
                
                 
                     if(idPerkiraan == idAccPeriodEarningPrev){                         
                         neraca.setPrevValue(dProfitLossPrevPeriod);                         
                     }
                
                    if(idPerkiraan == idAccYearEarningPrev){
                        yearEarning = neraca.getPrevValue() - dProfitLossPrevPeriod;
                        neraca.setPrevValue(yearEarning);
                    }
                
                    if(idPerkiraan == idAccRetainedEarningPrev){
                        if(startDateCurrPeriod.getMonth() == 0){
                            neraca.setPrevValue(neraca.getPrevValue() - yearEarning);
                        }
                    }
                
                
                if (workSheet.getDebetNeraca() != 0)
                    neraca.setValue(workSheet.getDebetNeraca());
                else {
                    neraca.setValue(workSheet.getKreditNeraca());
                }
                
                neraca.setBudget(rs.getDouble("BGT"));
                
                switch (rs.getInt("GROUP_ACCOUNT")) { 
                    case PstPerkiraan.ACC_GROUP_LIQUID_ASSETS:
                        if (workSheet.getKreditNeraca() > 0){
                            neraca.setValue(neraca.getValue() * -1);
                            neraca.setPrevValue(neraca.getPrevValue() * -1);
                        }
                        listAL.add(neraca);
                        break;
                    case PstPerkiraan.ACC_GROUP_FIXED_ASSETS:
                        if (workSheet.getKreditNeraca() > 0){
                            neraca.setValue(neraca.getValue() * -1);
                            neraca.setPrevValue(neraca.getPrevValue() * -1);
                        }
                        listAT.add(neraca);
                        break;
                    case PstPerkiraan.ACC_GROUP_OTHER_ASSETS:
                        if (workSheet.getKreditNeraca() > 0){
                            neraca.setValue(neraca.getValue() * -1);
                            neraca.setPrevValue(neraca.getPrevValue() * -1);
                        }
                        listALL.add(neraca);
                        break;
                    case PstPerkiraan.ACC_GROUP_CURRENCT_LIABILITIES:
                        if (workSheet.getDebetNeraca() > 0){
                            neraca.setValue(neraca.getValue() * -1);
                            neraca.setPrevValue(neraca.getPrevValue() * -1);
                        }
                        listHPendek.add(neraca);
                        break;
                    case PstPerkiraan.ACC_GROUP_LONG_TERM_LIABILITIES:
                        if (workSheet.getDebetNeraca() > 0){
                            neraca.setValue(neraca.getValue() * -1);
                            neraca.setPrevValue(neraca.getPrevValue() * -1);
                        }
                        listHPanjang.add(neraca);
                        break;
                    case PstPerkiraan.ACC_GROUP_EQUITY:
                        if (workSheet.getDebetNeraca() > 0){
                            neraca.setValue(neraca.getValue() * -1);
                            neraca.setPrevValue(neraca.getPrevValue() * -1);
                        }
                        listModal.add(neraca);
                        break;
                }
            }
            
            result.add(listAL);
            result.add(listAT);
            result.add(listALL);
            result.add(listHPendek);
            result.add(listHPanjang);
            result.add(listModal);
            
            rs.close();
            
        } catch (Exception e) {
            System.out.println("getListNeraca : "+e.toString());
        }
        return result;
    }
    
    synchronized public static Vector listSummaryBalanceSheet(long oidPeriod, long departmentOid){
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1,1);
            try{
                String sql = "DELETE FROM aiso_tmp_buffer";
                DBHandler.execUpdate(sql);
                
                Periode objPeriod = PstPeriode.fetchExc(oidPeriod);
                long preOidPeriod = SessWorkSheet.getOidPeriodeLalu(oidPeriod);
                
                Date startDateCurrPeriod = (Date)objPeriod.getTglAwal();
                
                insertDataToBuffer(oidPeriod,preOidPeriod,departmentOid);
                
                if(objPeriod.getPosted() == PstPeriode.PERIOD_OPEN){
                    queryInsertPL(departmentOid,oidPeriod);
                }
                
                sql = queryDataFromBuffer();
                
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                
                Vector vLiqAssets = new Vector(1,1);
                Vector vFixedAssets = new Vector(1,1);
                Vector vOthAssets = new Vector(1,1);
                Vector vCurrLiabilities = new Vector(1,1);
                Vector vLongTermLiabilities = new Vector(1,1);
                Vector vEquity = new Vector(1,1);
                
                while(rs.next()){
                    int iAccGroup = rs.getInt(4);
                    int iDebitCreditAss = rs.getInt(5);
                    double soAwalDebet = rs.getDouble(7);
                    double soAwalCredit = rs.getDouble(8);
                    double dDebet = rs.getDouble(9);
                    double dCredit = rs.getDouble(10);
                    long idPerkiraan = rs.getLong(6);
                   
                    double soPrevAccountDebet = soAwalDebet - soAwalCredit;
                    double soPrevAccountCredit = soAwalCredit - soAwalDebet;
                    double soCurrAccountDebet = soPrevAccountDebet + dDebet - dCredit;
                    double soCurrAccountCredit = soPrevAccountCredit + dCredit - dDebet;
                    double yearEarning = 0.0;
                    
                    Neraca objNeraca = new Neraca();
                    
                    switch(iAccGroup){
                         case I_ChartOfAccountGroup.ACC_GROUP_LIQUID_ASSETS:
                                vLiqAssets = checkDebetCreditAccount(vLiqAssets, soPrevAccountDebet, soCurrAccountDebet, 
                                            soPrevAccountCredit, soCurrAccountCredit, objNeraca, iDebitCreditAss, rs);
                            break;
                         case I_ChartOfAccountGroup.ACC_GROUP_FIXED_ASSETS:
                                vFixedAssets = checkDebetCreditAccount(vFixedAssets, soPrevAccountDebet, soCurrAccountDebet, 
                                            soPrevAccountCredit, soCurrAccountCredit, objNeraca, iDebitCreditAss, rs);
                            break;    
                         case I_ChartOfAccountGroup.ACC_GROUP_OTHER_ASSETS:
                             vOthAssets = checkDebetCreditAccount(vOthAssets, soPrevAccountDebet, soCurrAccountDebet, 
                                            soPrevAccountCredit, soCurrAccountCredit, objNeraca, iDebitCreditAss, rs);
                            break;
                         case I_ChartOfAccountGroup.ACC_GROUP_CURRENCT_LIABILITIES:
                             vCurrLiabilities = checkDebetCreditAccount(vCurrLiabilities, soPrevAccountDebet, soCurrAccountDebet, 
                                                soPrevAccountCredit, soCurrAccountCredit, objNeraca, iDebitCreditAss, rs);
                            break; 
                         case I_ChartOfAccountGroup.ACC_GROUP_LONG_TERM_LIABILITIES:
                             vLongTermLiabilities = checkDebetCreditAccount(vLongTermLiabilities, soPrevAccountDebet, soCurrAccountDebet, 
                                                    soPrevAccountCredit, soCurrAccountCredit, objNeraca, iDebitCreditAss, rs);
                            break;
                         case I_ChartOfAccountGroup.ACC_GROUP_EQUITY:
                             vEquity = checkDebetCreditAccount(vEquity, soPrevAccountDebet, soCurrAccountDebet, 
                                       soPrevAccountCredit, soCurrAccountCredit, objNeraca, iDebitCreditAss, rs);
                            break; 
                    }
                }
                
                vResult.add(vLiqAssets);
                vResult.add(vFixedAssets);
                vResult.add(vOthAssets);
                vResult.add(vCurrLiabilities);
                vResult.add(vLongTermLiabilities);
                vResult.add(vEquity);
            
            }catch(Exception e){
                System.out.println("Exception on listSummaryBalanceSheet() ::: "+e.toString());
            }
        return vResult;
    }
    
    private static Neraca resultToObject(Neraca objNeraca, ResultSet rs, double dPrevValue, double dCurrValue){
        try{            
            objNeraca.setAccNumber(rs.getString(1));
            objNeraca.setNamaPerkiraan(rs.getString(2));
            objNeraca.setAccountNameEnglish(rs.getString(3));
            objNeraca.setPrevValue(dPrevValue);
            objNeraca.setValue(dCurrValue);
        }catch(Exception e){
            objNeraca = new Neraca();
            System.out.println("Exception on resultToObject ::: "+e.toString());
        }
        return objNeraca;
    }
    
    private static Vector checkDebetCreditAccount(Vector vResult, double prevDebet, double currDebet, 
    double prevCredit, double currCredit, Neraca objNeraca, int debCreditAss, ResultSet rs){
        try{
            if(debCreditAss == PstPerkiraan.ACC_DEBETSIGN){
                objNeraca = resultToObject(objNeraca, rs, prevDebet, currDebet);
                vResult.add(objNeraca);
            }else{
                objNeraca = resultToObject(objNeraca, rs, prevCredit, currCredit);
                vResult.add(objNeraca);
            }
        }catch(Exception e){
            vResult = new Vector(1,1);
            System.out.println("Exception on checkDebetCreditAccount() ::: "+e.toString());
        }
        return vResult;
    }
    
    private static void insertDataToBuffer(long periodId, long prevPeriodId, long departmentId){
            try{
                insertCashIn(periodId);
                insertCashOut(periodId);
                insertPettyCashIn(periodId);
                insertPettyCashOut(periodId);
                insertBankDeposite(periodId);
                insertBankReceiveFund(periodId);
                insertBankTransferIn(periodId);
                insertChequeRequest(periodId);
                insertBankTransferOut(periodId);
                insertBankPayment(periodId);
                insertPettyCashReplacement(periodId);
                insertFunding(periodId);
                insertPayment(periodId);
                insertNonCash(periodId);
                int insertDataJU = insertDataJU(periodId, departmentId);
                int insertSaldoAwal = insertSaldoAwal(prevPeriodId, departmentId);
            }catch(Exception e){
                System.out.println("Exception on insertDataToBuffer() ::: "+e.toString());
            }
    }
    
    /**
     * adnyana
     * pencarian neraca di khusus saldo awal
     * jika belum ada mutasi
     * @param oidPeriod
     * @param departmentOid
     * @return
     */
    synchronized public static Vector getListNeracaInSaldoAwal(long oidPeriod, long departmentOid) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            //long preOidPeriod = SessWorkSheet.getOidPeriodeLalu(oidPeriod);
            String sql = "SELECT " +
            " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
            ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
            ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
            ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
            ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
            ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
            ", SUM(BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGET] + ") AS BGT" +
            ", SUM(SA." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET] + ") AS DBT" +
            ", SUM(SA." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT] + ") AS KRDT " +
            " FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR + " AS SA " +
            " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC " +
            " ON SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
            " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
            " LEFT JOIN " + PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD " +
            " ON SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
            " = BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN] +
            " WHERE SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] + "= " + oidPeriod +
            " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_DEPARTMENT_ID] + "=" + departmentOid +
            " AND (ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +
            " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_FIXED_ASSETS +
            " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_OTHER_ASSETS +
            " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_CURRENCT_LIABILITIES +
            " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_LONG_TERM_LIABILITIES +
            " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_EQUITY + ")" +
            " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
            ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
            ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
            ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
            ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
            ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
            " ORDER BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];
            
            //System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            // untuk menyimpan masing masing group account
            Vector listAL = new Vector(1, 1);
            Vector listAT = new Vector(1, 1);
            Vector listALL = new Vector(1, 1);
            Vector listHPendek = new Vector(1, 1);
            Vector listHPanjang = new Vector(1, 1);
            Vector listModal = new Vector(1, 1);
            
            while (rs.next()) {
                WorkSheet workSheet = new WorkSheet();
                workSheet.setNomorPerkiraan(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]));
                workSheet.setNamaPerkiraan(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]));
                workSheet.setAccountNameEnglish(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]));
                workSheet.setAnggaran(rs.getDouble("BGT"));
                
                // mutasi
                double totDebet = 0;
                double totKredit = 0;
                workSheet.setDebetMutasi(totDebet);
                workSheet.setKreditMutasi(totKredit);
                workSheet.setTandaDebetKredit(rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]));
                
                // saldo awal
                // SaldoAkhirPeriode saldoAkhirPeriode = SessWorkSheet.getSaldoAwal(preOidPeriod, rs.getLong(PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]));
                double totSalDebet = rs.getDouble("DBT");
                double totSalKredit = rs.getDouble("KRDT");
                /*if (workSheet.getTandaDebetKredit() == PstPerkiraan.ACC_DEBETSIGN) {
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
                }*/
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
                    workSheet.setDebetNeracaSaldo(totNeracaSaldo);
                } else {
                    totNeracaPerLalu = workSheet.getKreditNeracaPeriodeLalu() - workSheet.getDebetNeracaPeriodeLalu();
                    totMutasi = workSheet.getKreditMutasi() - workSheet.getDebetMutasi();
                    totNeracaSaldo = totNeracaPerLalu + totMutasi;
                    workSheet.setKreditNeracaSaldo(totNeracaSaldo);
                } // -- end ----
                
                workSheet.setDebetNeraca(workSheet.getDebetNeracaSaldo());
                workSheet.setKreditNeraca(workSheet.getKreditNeracaSaldo());
                
                // taruh hasil neraca ke object neraca view
                Neraca neraca = new Neraca();
                neraca.setNamaPerkiraan(workSheet.getNamaPerkiraan());
                neraca.setAccountNameEnglish(workSheet.getAccountNameEnglish());
                if (workSheet.getDebetNeraca() != 0)
                    neraca.setValue(workSheet.getDebetNeraca());
                else {
                    neraca.setValue(workSheet.getKreditNeraca());
                    /*if (workSheet.getKreditNeraca() > 0)
                        neraca.setValue(workSheet.getKreditNeraca());
                    else
                        neraca.setValue(workSheet.getKreditNeraca());*/
                }
                neraca.setBudget(workSheet.getAnggaran());
                
                switch (rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP])) {
                    case PstPerkiraan.ACC_GROUP_LIQUID_ASSETS:
                        if (workSheet.getKreditNeraca() > 0)
                            neraca.setValue(neraca.getValue() * -1);
                        listAL.add(neraca);
                        break;
                    case PstPerkiraan.ACC_GROUP_FIXED_ASSETS:
                        if (workSheet.getKreditNeraca() > 0)
                            neraca.setValue(neraca.getValue() * -1);
                        listAT.add(neraca);
                        break;
                    case PstPerkiraan.ACC_GROUP_OTHER_ASSETS:
                        if (workSheet.getKreditNeraca() > 0)
                            neraca.setValue(neraca.getValue() * -1);
                        listALL.add(neraca);
                        break;
                    case PstPerkiraan.ACC_GROUP_CURRENCT_LIABILITIES:
                        if (workSheet.getDebetNeraca() > 0)
                            neraca.setValue(neraca.getValue() * -1);
                        listHPendek.add(neraca);
                        break;
                    case PstPerkiraan.ACC_GROUP_LONG_TERM_LIABILITIES:
                        if (workSheet.getDebetNeraca() > 0)
                            neraca.setValue(neraca.getValue() * -1);
                        listHPanjang.add(neraca);
                        break;
                    case PstPerkiraan.ACC_GROUP_EQUITY:
                        if (workSheet.getDebetNeraca() > 0)
                            neraca.setValue(neraca.getValue() * -1);
                        listModal.add(neraca);
                        break;
                }
            }
            
            result.add(listAL);
            result.add(listAT);
            result.add(listALL);
            result.add(listHPendek);
            result.add(listHPanjang);
            result.add(listModal);
            
            rs.close();
            
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        }
        return result;
    }
    
    synchronized public static Vector getListNeracaWithLevel(long oidPeriod, long departmentOid, int level) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "";
            for(int i = 1; i < 7; i++){
                sql = "DELETE FROM aiso_buffer_level_"+ i +"";
                DBHandler.execUpdate(sql);
            }
            
            long preOidPeriod = SessWorkSheet.getOidPeriodeLalu(oidPeriod);
            
            for (int i = 1; i < 7 ; i++){
                sql = " INSERT INTO aiso_buffer_level_"+ i +"(ID_INDUK_PERKIRAAN," +
                " ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,TANDA_DEBET_KREDIT," +
                " GROUP_ACCOUNT,BGT,DBT,KRDT)" +
                " SELECT " +
                " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT] +
                ",ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
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
                " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_DEPARTMENT_ID] + "=" + departmentOid +
                " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_LEVEL] + "=" + i +
                " AND (ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +
                " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_FIXED_ASSETS +
                " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_OTHER_ASSETS +
                " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_CURRENCT_LIABILITIES +
                " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_LONG_TERM_LIABILITIES +
                " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_EQUITY + ")" +
                " AND (JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE] + "=" + I_JournalType.TIPE_JURNAL_UMUM +
                " OR JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE] + "=" + I_JournalType.TIPE_JURNAL_PENUTUP_1 + ")" +
                " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT] +
                ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP];
                DBHandler.execUpdate(sql);
                
                sql = " INSERT INTO aiso_buffer_level_"+ i +"(ID_INDUK_PERKIRAAN, ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,TANDA_DEBET_KREDIT," +
                "GROUP_ACCOUNT,BGT,SALDO_AWAL_DEBET,SALDO_AWAL_KREDIT)" +
                " SELECT " +
                " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT] +
                " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                ", SUM(BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGET] + ") AS BGT" +
                ", SUM(SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET] + ") AS DBT" +
                ", SUM(SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT] + ") AS KRDT " +
                " FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR + " AS SA " +
                " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC " +
                " ON SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
                " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                " LEFT JOIN " + PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD " +
                " ON SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
                " = BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN] +
                " WHERE SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] + "= " + preOidPeriod +
                " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_DEPARTMENT_ID] + "=" + departmentOid +
                " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_LEVEL] + "=" + i +
                " AND (ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +
                " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_FIXED_ASSETS +
                " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_OTHER_ASSETS +
                " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_CURRENCT_LIABILITIES +
                " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_LONG_TERM_LIABILITIES +
                " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_EQUITY + ")" +
                " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT] +
                ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP];
                DBHandler.execUpdate(sql);
                
            }//end for (int i = 1; i < 7 ; i++);
            
            
            //String lev = String.valueOf(lvl);
            
            /**
             * This query is used to get below level table record
             * after that updating next table (one level above) if between table has same id_perkiraan
             *and then insert to next table id_induk_perkiraan
             */
            for(int i = 6; i > 0; i--){
                /** Check level from search parameter
                 * if level deferent with int looping
                 * get record from table (one level below)
                 */
                if(level != i){
                    /**
                     * Table in series start from up to below is 1 to 6
                     * This sql is used to get record from table (one level below)
                     */
                    sql = "select lev"+(i-1)+".id_perkiraan, sum(lev"+ i +".dbt + lev"+(i-1)+".dbt) as debet,"+
                    "sum(lev"+ i +".krdt + lev"+(i-1)+".krdt) as kredit"+
                    " from aiso_buffer_level_"+i+" as lev"+i+""+
                    " inner join aiso_buffer_level_"+(i-1)+" as lev"+(i-1)+" on lev"+ i +".id_induk_perkiraan = lev"+( i - 1) +".id_perkiraan"+
                    " group by lev"+(i-1)+".id_perkiraan";
                    
                    /**
                     * Catch query result by result set object
                     */
                    dbrs = DBHandler.execQueryResult(sql);
                    ResultSet rs = dbrs.getResultSet();
                    
                    while(rs.next()){
                        
                        /**
                         * Define variable to get value from result set object
                         */
                        long id_perk = rs.getLong("id_perkiraan");
                        double kredit = rs.getDouble("kredit");
                        double debet = rs.getDouble("debet");
                        
                        /**
                         * This sql is used to update record next table (one level above)
                         */
                        sql =   "update aiso_buffer_level_"+(i-1)+""+
                        "set dbt ="+ debet +", krdt ="+ kredit +""+
                        "where id_perkiraan ="+ id_perk +"";
                    }
                    System.out.println("INI SQL YANG DIGUNAKAN UNTUK INSERT DATA KE LEVEL DIATASNYA ::::: " + sql);
                }else{
                    
                    break;
                    
                }//End if
            }//End for (int i = 6; i > 0; i--);
            
            /**
             * Check level, if level < 1, insert id_induk_perkiraan to
             * next table (one level above)
             */
            if(level > 1){
                for(int i = level; i > 0; i-- ){
                    
                    sql = "insert into aiso_buffer_level_"+(i-1)+"(id_induk_perkiraan,"+
                    "id_perkiraan, nomor_perkiraan,nama,tanda_debet_kredit, "+
                    "group_account,bgt,saldo_awal_debet,saldo_awal_kredit)"+
                    "select per.id_perkiraan,lev"+ i +".id_induk_perkiraan,per.nomor_perkiraan, per.nama,per.tanda_debet_kredit, per.group_account,0,0,0"+
                    "from aiso_buffer_level_"+ i +" as lev"+ i +""+
                    " inner join aiso_perkiraan as per on lev"+ i +".id_induk_perkiraan = per.id_perkiraan"+
                    "where per.id_perkiraan not in " +
                    "(select lev"+(i-1)+".id_perkiraan from aiso_buffer_level_"+(i-1)+" as lev"+(i-1)+"";
                }//End for (int i = 6; i > 1; i--);
            }//End if
            
            // System.out.println("sql : " + sql);
            
            
            
            Vector listAL = new Vector(1, 1);
            Vector listAT = new Vector(1, 1);
            Vector listALL = new Vector(1, 1);
            Vector listHPendek = new Vector(1, 1);
            Vector listHPanjang = new Vector(1, 1);
            Vector listModal = new Vector(1, 1);
            
            listAL = prosesGetByGroupAccount(PstPerkiraan.ACC_GROUP_LIQUID_ASSETS,listAL);
            listAT = prosesGetByGroupAccount(PstPerkiraan.ACC_GROUP_LIQUID_ASSETS,listAT);
            listALL = prosesGetByGroupAccount(PstPerkiraan.ACC_GROUP_FIXED_ASSETS,listALL);
            listHPendek = prosesGetByGroupAccount(PstPerkiraan.ACC_GROUP_OTHER_ASSETS,listHPendek);
            listHPanjang = prosesGetByGroupAccount(PstPerkiraan.ACC_GROUP_CURRENCT_LIABILITIES,listHPanjang);
            listModal = prosesGetByGroupAccount(PstPerkiraan.ACC_GROUP_LONG_TERM_LIABILITIES,listModal);
            listModal = prosesGetByGroupAccount(PstPerkiraan.ACC_GROUP_EQUITY,listModal);
            
            result.add(listAL);
            result.add(listAT);
            result.add(listALL);
            result.add(listHPendek);
            result.add(listHPanjang);
            result.add(listModal);
        } catch (Exception e) {
            System.out.println("getListNeracaWithTotal : "+e.toString());
        }
        return result;
    }
    
        private static void insertCashIn(long lSelectedPeriodOid){
        String sql = "";
            try{
                sql = getStringSelectQuery() +
                    " , SUM(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] + ") AS DEBET" +
                    " , 0 AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " LEFT JOIN "+ PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD "+
                    " ON ACC."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = BUD."+ PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]+
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_CASH +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +getStringGroupByQuery();   
                
                    //System.out.println("SQL insertSJPartOne() ::: "+sql);
                    DBHandler.execUpdate(sql);
            }catch(Exception e){
                System.out.println("Exception on insertCashIn() ::: "+e.toString());
            }
    }
        
         private static void insertCashOut(long lSelectedPeriodOid){
        String sql = "";
            try{
                sql = getStringSelectQuery()+
                    " , 0 AS DEBET" +
                    " , SUM(SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT] + ") AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +                    
                    " INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+
                    " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " = SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " LEFT JOIN "+ PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD "+
                    " ON ACC."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = BUD."+ PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]+
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_BANK +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT_STATUS] +
                    " = "+PstSpecialJournalMain.STS_DEBET+
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +getStringGroupByQuery(); 
                
                    //System.out.println("SQL insertSJPartOne() ::: "+sql);
                    DBHandler.execUpdate(sql);
            }catch(Exception e){
                System.out.println("Exception on insertCashOut() ::: "+e.toString());
            }
    }
    
         private static void insertPettyCashIn(long lSelectedPeriodOid){
        String sql = "";
            try{
                sql = getStringSelectQuery() +
                    " , SUM(SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT] + ") AS DEBET" +
                    " , 0 AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +
                    " INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+
                    " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " = SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " LEFT JOIN "+ PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD "+
                    " ON ACC."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = BUD."+ PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]+
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_REPLACEMENT +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +getStringGroupByQuery();  
                
                    //System.out.println("SQL insertPettyCashIn() ::: "+sql);
                    DBHandler.execUpdate(sql);
                
            }catch(Exception e){
                System.out.println("Exception on insertPettyCashIn() ::: "+e.toString());
            }
    }
         
    private static void insertPettyCashOut(long lSelectedPeriodOid){        
        String sql = "";
            try{
                sql = getStringSelectQuery() +
                    " , 0 AS DEBET" +
                    " , SUM(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] + ") AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " LEFT JOIN "+ PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD "+
                    " ON ACC."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = BUD."+ PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]+
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_PETTY_CASH +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +getStringGroupByQuery();                 
                
                    //System.out.println("SQL insertSJPartTwo() ::: "+sql);
                    DBHandler.execUpdate(sql);
            }catch(Exception e){
                System.out.println("Exception on insertPettyCashOut() ::: "+e.toString());
            }
    }
    
     private static void insertBankDeposite(long lSelectedPeriodOid){
        String sql = "";
            try{
                sql = getStringSelectQuery()+
                    " , SUM(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] + ") AS DEBET" +
                    " , 0 AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " LEFT JOIN "+ PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD "+
                    " ON ACC."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = BUD."+ PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]+
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_BANK +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT_STATUS] +
                    " = "+PstSpecialJournalMain.STS_DEBET+
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +getStringGroupByQuery(); 
                    
                System.out.println("SQL insertBankDeposite() ::: "+sql);
                DBHandler.execUpdate(sql);
                
            }catch(Exception e){
                System.out.println("Exception on insertBankDeposite() ::: "+e.toString());
            }
    }
     
     private static void insertBankReceiveFund(long lSelectedPeriodOid){
        String sql = "";
            try{
                sql = getStringSelectQuery()+
                    " , SUM(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] + ") AS DEBET" +
                    " , 0 AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +                    
                    /*" INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+                    
                    " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " = SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+*/
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " LEFT JOIN "+ PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD "+
                    " ON ACC."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = BUD."+ PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]+
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_FUND +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +getStringGroupByQuery();  
                
                    System.out.println("SQL insertBankReceiveFund() ::: "+sql);
                    DBHandler.execUpdate(sql);
                    
            }catch(Exception e){
                System.out.println("Exception on insertBankReceiveFund() ::: "+e.toString());
            }
    }
     
     private static void insertBankTransferIn(long lSelectedPeriodOid){
        String sql = "";
            try{
                sql = getStringSelectQuery()+
                    " , SUM(SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT] + ") AS DEBET" +
                    " , 0 AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +                    
                    " INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+                    
                    " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " = SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " LEFT JOIN "+ PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD "+
                    " ON ACC."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = BUD."+ PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]+
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_BANK_TRANSFER +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +getStringGroupByQuery();  
                
                    System.out.println("SQL insertBankTransferIn() ::: "+sql);
                    DBHandler.execUpdate(sql);
                    
            }catch(Exception e){
                System.out.println("Exception on insertBankTransferIn() ::: "+e.toString());
            }
    }
     
    private static void insertChequeRequest(long lSelectedPeriodOid){
        String sql = "";
            try{
                sql = getStringSelectQuery() +
                    " , 0 AS DEBET" +
                    " , SUM(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] + ") AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " LEFT JOIN "+ PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD "+
                    " ON ACC."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = BUD."+ PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]+
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_BANK +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT_STATUS] +
                    " = "+PstSpecialJournalMain.STS_KREDIT+
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +getStringGroupByQuery();  
                
                    System.out.println("SQL insertChequeRequest() ::: "+sql);
                    DBHandler.execUpdate(sql);
                
            }catch(Exception e){
                System.out.println("Exception on insertChequeRequest() ::: "+e.toString());
            }
    }
    
    private static void insertPettyCashReplacement(long lSelectedPeriodOid){
        String sql = "";
            try{
                sql = getStringSelectQuery() +
                    " , 0 AS DEBET" +
                    " , SUM(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] + ") AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " LEFT JOIN "+ PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD "+
                    " ON ACC."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = BUD."+ PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]+
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_REPLACEMENT +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +getStringGroupByQuery();  
                
                    System.out.println("SQL insertPettyCashReplacement() ::: "+sql);
                    DBHandler.execUpdate(sql);
                
            }catch(Exception e){
                System.out.println("Exception on insertPettyCashReplacement() ::: "+e.toString());
            }
    }
    
    private static void insertBankTransferOut(long lSelectedPeriodOid){
        String sql = "";
            try{
                sql = getStringSelectQuery() +
                    " , 0 AS DEBET" +
                    " , SUM(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] + ") AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " LEFT JOIN "+ PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD "+
                    " ON ACC."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = BUD."+ PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]+
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_BANK_TRANSFER +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +getStringGroupByQuery();  
                
                    System.out.println("SQL insertBankTransferOut() ::: "+sql);
                    DBHandler.execUpdate(sql);
                
            }catch(Exception e){
                System.out.println("Exception on insertBankTransferOut() ::: "+e.toString());
            }
    }
    
     private static void insertBankPayment(long lSelectedPeriodOid){
        String sql = "";
            try{
                sql = getStringSelectQuery() +
                    " , 0 AS DEBET" +
                    " , SUM(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] + ") AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " LEFT JOIN "+ PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD "+
                    " ON ACC."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = BUD."+ PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]+
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_PAYMENT +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +getStringGroupByQuery();  
                
                    System.out.println("SQL insertBankPayment() ::: "+sql);
                    DBHandler.execUpdate(sql);
                
            }catch(Exception e){
                System.out.println("Exception on insertBankPayment() ::: "+e.toString());
            }
    }
     
    private static void insertNonCash(long lSelectedPeriodOid){
        String sql = "";
            try{
                sql = getStringSelectQuery()+
                    " , 0 AS DEBET" +
                    " , SUM(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] + ") AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +                    
                    " INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+                    
                    " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " = SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " LEFT JOIN "+ PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD "+
                    " ON ACC."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = BUD."+ PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]+
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_NON_CASH+
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_CURRENCT_LIABILITIES +getStringGroupByQuery();  
                
                    //System.out.println("SQL insertSJPartFive() ::: "+sql);
                    DBHandler.execUpdate(sql);
                    
            }catch(Exception e){
                System.out.println("Exception on insertNonCash() ::: "+e.toString());
            }
    }
     
     private static void insertFunding(long lSelectedPeriodOid){
        String sql = "";
            try{
                sql = getStringSelectQuery()+
                    " , 0 AS DEBET " +
                    " , SUM(SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT] + ") AS KREDIT " +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +                    
                    " INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+
                    " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " = SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " LEFT JOIN "+ PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD "+
                    " ON ACC."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = BUD."+ PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]+
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_FUND +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_EQUITY +getStringGroupByQuery();                 
                
                    System.out.println("SQL insertFunding() ::: "+sql);
                    
                    DBHandler.execUpdate(sql);
            }catch(Exception e){
                System.out.println("Exception on insertFunding() ::: "+e.toString());
            }
    }
    
      private static void insertPayment(long lSelectedPeriodOid){
        String sql = "";
            try{
                sql = getStringSelectQuery() +
                    " , SUM(SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT] + ") AS DEBET" +
                    " ,  0 AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +
                     " INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+
                    " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " = SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " LEFT JOIN "+ PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD "+
                    " ON ACC."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = BUD."+ PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]+
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_PAYMENT +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_CURRENCT_LIABILITIES +getStringGroupByQuery();  
                
                    System.out.println("SQL insertPayment() ::: "+sql);
                    DBHandler.execUpdate(sql);
                
            }catch(Exception e){
                System.out.println("Exception on insertPayment() ::: "+e.toString());
            }
    }
      
    private static String getStringSelectQuery(){
        String sql = "";
            try{
                sql = " INSERT INTO aiso_tmp_buffer" + 
                    "(ID_PERKIRAAN,ID_PARENT,NOMOR_PERKIRAAN,NAMA,ACCOUNT_NAME_ENGLISH,TANDA_DEBET_KREDIT," +
                    " GROUP_ACCOUNT,BGT,DBT,KRDT)"+
                    " SELECT " +
                    " ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT] +
                    " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                    " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                    " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " , SUM(BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGET] +") AS BGT ";                  
            }catch(Exception e){
                sql = "";
                System.out.println(""+e.toString());
            }
        return sql;
    }
    
     private static String getStringGroupByQuery(){
        String sql = "";
            try{
                sql =  " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT] +
                    " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                    " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                    " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " ORDER BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];                  
            }catch(Exception e){
                sql = "";
                System.out.println(""+e.toString());
            }
        return sql;
    }
    
     private static String queryInsertPL(long departmentId, long lSelectedPeriodOid){
        String sql = "";        
            try{
                Perkiraan objPerkiraan = SessWorkSheet.getNamaSaldoPeriodeBerjalan(departmentId);
                double profitLoss = getProfitLoss(lSelectedPeriodOid);
                sql =" INSERT INTO aiso_tmp_buffer" + 
                    "(ID_PERKIRAAN,ID_PARENT,NOMOR_PERKIRAAN,NAMA,ACCOUNT_NAME_ENGLISH,TANDA_DEBET_KREDIT," +
                    " GROUP_ACCOUNT,BGT,DBT,KRDT) VALUES("+objPerkiraan.getOID()+", "+objPerkiraan.getIdParent()+
                    ", '"+objPerkiraan.getNoPerkiraan()+"', '"+objPerkiraan.getNama()+"', '"+objPerkiraan.getAccountNameEnglish()+
                    "', "+objPerkiraan.getTandaDebetKredit()+", "+objPerkiraan.getAccountGroup()+", 0";
                if(profitLoss < 0){
                    sql = sql + ", "+(profitLoss * -1)+", 0)";
                }else{
                    sql = sql + ", 0, "+profitLoss+")";
                }
                                
                DBHandler.execUpdate(sql);
                
            }catch(Exception e){
               sql = "";
               System.out.println("Exception on queryInsertPL() :::: "+e.toString());
            }
        return sql;
     }
     
     private static double getProfitLoss(long lSelectedPeriodOid){
        double dResult = 0.0;
            try{
                double revenueSJ = getRevenueSJ(lSelectedPeriodOid);
                double expensesSJ = getExpensesSJ(lSelectedPeriodOid);
                double profitLossJU = getProfitLossJU(lSelectedPeriodOid);
                
                dResult = profitLossJU + revenueSJ - expensesSJ;
                
            }catch(Exception e){
                dResult = 0.0;
                System.out.println("Exception on getProfitLoss() ::: "+e.toString());
            }
        return dResult;
     }
     
     private static double getRevenueSJ(long lSelectedPeriodOid){
        DBResultSet dbrs = null;
         double dResult = 0.0;
         String sql = "";
            try{
                sql = " SELECT SUM(SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT] + ") AS TOTREVENUE " +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +
                     " INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+
                    " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " = SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " LEFT JOIN "+ PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD "+
                    " ON ACC."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = BUD."+ PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]+
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_CASH +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_REVENUE;  
                
                    //System.out.println("SQL getRevenueSJ() ::: "+sql);
                    dbrs = DBHandler.execQueryResult(sql);
                    ResultSet rs = dbrs.getResultSet();
                    
                    while(rs.next()){
                        dResult = rs.getDouble("TOTREVENUE");
                    }
            }catch(Exception e){
                dResult = 0.0;
                System.out.println("Exception on getStringQueryRevenueSJ()"+e.toString());
            }
        return dResult;
     }
    
     private static double getExpensesSJ(long lSelectedPeriodOid){
        DBResultSet dbrs = null;
        double dResult = 0.0;       
            try{
             String sql = " SELECT SUM(SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT] + ") AS TOTEXPENSES " +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +
                     " INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+
                    " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " = SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " LEFT JOIN "+ PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD "+
                    " ON ACC."+ PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " = BUD."+ PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]+
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " IN(" + I_JournalType.TIPE_SPECIAL_JOURNAL_PETTY_CASH +
                    ", "+I_JournalType.TIPE_SPECIAL_JOURNAL_NON_CASH+
                    ", "+I_JournalType.TIPE_SPECIAL_JOURNAL_BANK+") "+                    
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_EXPENSE;  
                
                    //System.out.println("SQL getExpensesSJ() ::: "+sql);
                    dbrs = DBHandler.execQueryResult(sql);
                    ResultSet rs = dbrs.getResultSet();
                    
                    while(rs.next()){
                        dResult = rs.getDouble("TOTEXPENSES");
                    }
            }catch(Exception e){
                dResult = 0.0;
                System.out.println("Exception on getExpensesSJ() ::: "+e.toString());
            }
        return dResult;
     }
     
     private static double getProfitLossJU(long lSelectedPeriodOid){
        DBResultSet dbrs = null;
        double dResult = 0.0;
            try{
                String sql = " SELECT ACC." +PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+" AS ACCGROUP "+
                            ", SUM(JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET]+") AS DEBET "+
                            ", SUM(JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT]+") AS KREDIT "+
                            " FROM "+PstJurnalUmum.TBL_JURNAL_UMUM+" AS JU "+
                            " INNER JOIN "+PstJurnalDetail.TBL_JURNAL_DETAIL+" AS JD "+
                            " ON JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]+
                            " = JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID]+
                            " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS ACC "+
                            " ON JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+
                            " = ACC."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                            " WHERE JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE]+
                            " = "+I_JournalType.TIPE_JURNAL_UMUM+
                            " AND JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID]+
                            " = "+lSelectedPeriodOid+
                            " AND ACC."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
                            " IN("+PstPerkiraan.ACC_GROUP_REVENUE+", "+PstPerkiraan.ACC_GROUP_EXPENSE+
                            ", "+PstPerkiraan.ACC_GROUP_COST_OF_SALES+", "+PstPerkiraan.ACC_GROUP_OTHER_REVENUE+
                            ", "+PstPerkiraan.ACC_GROUP_OTHER_EXPENSE+", "+PstPerkiraan.ACC_GROUP_INCOME_TAXES+")"+
                            " GROUP BY ACC."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP];
                
                System.out.println("SessNeraca.getProfitLossJU ::::::: "+sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                double totrevenue = 0.0;                    
                double totcogs = 0.0;
                double totexpenses = 0.0;
                double totothRevenue = 0.0;
                double totothExpense = 0.0;
                double totincomeTax = 0.0;
                while(rs.next()){
                    int accGroup = rs.getInt("ACCGROUP");
                    double revenue = 0.0;                    
                    double cogs = 0.0;
                    double expenses = 0.0;
                    double othRevenue = 0.0;
                    double othExpense = 0.0;
                    double incomeTax = 0.0;
                    double debet = rs.getDouble("DEBET");
                    double credit = rs.getDouble("KREDIT");
                    
                    switch(accGroup){
                        case PstPerkiraan.ACC_GROUP_REVENUE :
                            revenue = credit - debet;
                            break;
                        case PstPerkiraan.ACC_GROUP_COST_OF_SALES :
                            cogs = debet - credit;
                            break;
                        case PstPerkiraan.ACC_GROUP_EXPENSE :
                            expenses = debet - credit;
                            break;
                        case PstPerkiraan.ACC_GROUP_OTHER_REVENUE :
                            othRevenue = credit - debet;
                            break;
                        case PstPerkiraan.ACC_GROUP_OTHER_EXPENSE :
                            othExpense = debet - credit;
                            break;
                        case PstPerkiraan.ACC_GROUP_INCOME_TAXES :
                            incomeTax = debet - credit;
                            break;     
                    }
                    
                    totrevenue += revenue;                    
                    totcogs += cogs;
                    totexpenses += expenses;
                    totothRevenue += othRevenue;
                    totothExpense += othExpense;
                    totincomeTax += incomeTax;
                    
                }
                
                dResult = totrevenue - totcogs - totexpenses + totothRevenue - totothExpense - totincomeTax;
                
            }catch(Exception e){
                dResult = 0.0;
                System.out.println("Exception on getRevenueJU()"+e.toString());
            }
        return dResult;
     }
     
     private static String queryDataFromBuffer(){
        String sql = "";
            try{
                sql = " SELECT PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                    ", SUM(BUF.SALDO_AWAL_DEBET) AS SALDO_AWAL_DEBET"+
                    ", SUM(BUF.SALDO_AWAL_KREDIT) AS SALDO_AWAL_KREDIT"+
                    ", SUM(BUF.DBT) AS DBT, SUM(BUF.KRDT) AS KRDT"+
                    //" FROM AISO_TMP_BUFFER AS BUF INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS PRK "+
                    //replace table with capital words with small words
                    " FROM aiso_tmp_buffer AS BUF INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS PRK "+
                    " ON BUF.ID_PARENT = PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                    " GROUP BY PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                    " ORDER BY PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];
                    
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on queryDataFromBuffer ::: "+e.toString());
            }
        return sql;
     }
     
    private static int insertSaldoAwal(long lPeriodId, long lDepartmentId){
    int iResult = 0;
            try{
                String sql = queryInsert("SALDO_AWAL_DEBET", "SALDO_AWAL_KREDIT")+
			querySelect(PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET], 
                        PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT],"JU.")+
                        " FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR + " AS JU " +
                        " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC " +
                        " ON JU." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
                        " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                        " LEFT JOIN " + PstAisoBudgeting.TBL_AISO_BUDGETING + " AS BUD " +
                        " ON JU." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
                        " = BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN] +
                        queryWhereClause(PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE], 
                        1, lPeriodId, lDepartmentId)+
                        queryGroupBy();
                
                System.out.println("SQL insertSaldoAwal() ::: "+sql);
                iResult = DBHandler.execUpdate(sql);
                
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on insertSaldoAwal() ::: "+e.toString());
            }
        return iResult;
    }
    
    private static int insertDataJU(long lPeriodId, long lDepartmentId){
        int iResult = 0;
            try{
                String sql = queryInsert("DBT", "KRDT")+
			querySelect(PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET], 
                        PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT],"JD.")+
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
                        queryWhereClause(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID], 
                        I_JournalType.TIPE_JURNAL_UMUM, lPeriodId, lDepartmentId)+
                        queryGroupBy();
                iResult = DBHandler.execUpdate(sql);
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on insertDataJU() ::: "+e.toString());
            }
        return iResult;
    }
    
    private static String queryInsert(String strDebet, String strCredit){
        String sql = "";
            try{
                //sql = " INSERT INTO AISO_TMP_BUFFER (ID_PERKIRAAN,ID_PARENT,NOMOR_PERKIRAAN,NAMA,ACCOUNT_NAME_ENGLISH," +
                  sql = " INSERT INTO aiso_tmp_buffer (ID_PERKIRAAN,ID_PARENT,NOMOR_PERKIRAAN,NAMA,ACCOUNT_NAME_ENGLISH," +
                    "TANDA_DEBET_KREDIT, GROUP_ACCOUNT,"+strDebet+", "+strCredit+")";
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on queryInsert ::: "+e.toString());
            }
        return sql;
    } 
    
    private static String querySelect(String fieldDebet, String fieldCredit, String strAlias){
        String sql = "";
            try{
                sql =  " SELECT ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    ", SUM(" + strAlias + fieldDebet + ") AS DBT" +
                    ", SUM(" + strAlias + fieldCredit + ") AS KRDT ";
                
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on querySelect() ::: "+e.toString());
            }
        return sql;
    }
    
    private static String queryWhereClause(String fieldPeriod, int iJournalType, long lPeriodId, long lDepartmentId){
        String sql = "";
            try{
                 sql =  " WHERE JU." + fieldPeriod + "= " + lPeriodId +
                        " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + 
                        " IN(" + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +", "+ PstPerkiraan.ACC_GROUP_FIXED_ASSETS +
                        ", "+ PstPerkiraan.ACC_GROUP_OTHER_ASSETS +", "+ PstPerkiraan.ACC_GROUP_CURRENCT_LIABILITIES +
                        ", "+ PstPerkiraan.ACC_GROUP_LONG_TERM_LIABILITIES +", "+ PstPerkiraan.ACC_GROUP_EQUITY + ")";
                 if(iJournalType == 0){
                   sql = sql + " AND JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE] + 
                        " IN(" + I_JournalType.TIPE_JURNAL_UMUM +", " + I_JournalType.TIPE_JURNAL_PENUTUP_1 + ")";
                 }
                 
                 if(lDepartmentId != 0){
                    sql = sql +" AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_DEPARTMENT_ID] + "=" + lDepartmentId;
                 }
                
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on queryWhereClause() ::: "+e.toString());
            }
        return sql;
    }
    
    private static String queryGroupBy(){
        String sql = "";
            try{
                
                sql = " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                      " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT] +
                      ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                      ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                      ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                      ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                      ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP];
                
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on queryGroupBy() ::: "+e.toString());
            }
        return sql;
    }
    
    public static Vector prosesGetByGroupAccount(int groupAccount, Vector list){
        DBResultSet dbrs = null;
        try{
            String sql = " SELECT ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,TANDA_DEBET_KREDIT," +
            "GROUP_ACCOUNT,BGT" +
            ", SUM(DBT) AS TOT_DBT" +
            ", SUM(KRDT) AS TOT_KRDT " +
            ", SUM(SALDO_AWAL_DEBET) AS TOT_SADBT" +
            ", SUM(SALDO_AWAL_KREDIT) AS TOT_SAKRDT " +
            " FROM aiso_buffer_level_1" +
            " WHERE GROUP_ACCOUNT = "+groupAccount+
            " GROUP BY ID_PERKIRAAN " +
            ", NOMOR_PERKIRAAN" +
            ", NAMA" +
            ", TANDA_DEBET_KREDIT" +
            ", GROUP_ACCOUNT,BGT " +
            " ORDER BY NOMOR_PERKIRAAN";
                
            // untuk menyimpan masing masing group account
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                list = prosesRsGetValue(rs,list); 
                sql = " SELECT ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,TANDA_DEBET_KREDIT," +
                "GROUP_ACCOUNT,BGT" +
                ", SUM(DBT) AS TOT_DBT" +
                ", SUM(KRDT) AS TOT_KRDT " +
                ", SUM(SALDO_AWAL_DEBET) AS TOT_SADBT" +
                ", SUM(SALDO_AWAL_KREDIT) AS TOT_SAKRDT " +
                " FROM aiso_buffer_level_2" +
                " WHERE GROUP_ACCOUNT = "+groupAccount+
                " GROUP BY ID_PERKIRAAN " +
                ", NOMOR_PERKIRAAN" +
                ", NAMA" +
                ", TANDA_DEBET_KREDIT" +
                ", GROUP_ACCOUNT,BGT " +
                " ORDER BY NOMOR_PERKIRAAN";

                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs2 = dbrs.getResultSet();
                while(rs2.next()){
                    list = prosesRsGetValue(rs2,list); 
                    sql = " SELECT ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,TANDA_DEBET_KREDIT," +
                    "GROUP_ACCOUNT,BGT" +
                    ", SUM(DBT) AS TOT_DBT" +
                    ", SUM(KRDT) AS TOT_KRDT " +
                    ", SUM(SALDO_AWAL_DEBET) AS TOT_SADBT" +
                    ", SUM(SALDO_AWAL_KREDIT) AS TOT_SAKRDT " +
                    " FROM aiso_buffer_level_3" +
                    " WHERE GROUP_ACCOUNT = "+groupAccount+
                    " GROUP BY ID_PERKIRAAN " +
                    ", NOMOR_PERKIRAAN" +
                    ", NAMA" +
                    ", TANDA_DEBET_KREDIT" +
                    ", GROUP_ACCOUNT,BGT " +
                    " ORDER BY NOMOR_PERKIRAAN";

                    dbrs = DBHandler.execQueryResult(sql);
                    ResultSet rs3 = dbrs.getResultSet();
                    while(rs3.next()){
                        list = prosesRsGetValue(rs3,list); 
                        
                        sql = " SELECT ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,TANDA_DEBET_KREDIT," +
                        "GROUP_ACCOUNT,BGT" +
                        ", SUM(DBT) AS TOT_DBT" +
                        ", SUM(KRDT) AS TOT_KRDT " +
                        ", SUM(SALDO_AWAL_DEBET) AS TOT_SADBT" +
                        ", SUM(SALDO_AWAL_KREDIT) AS TOT_SAKRDT " +
                        " FROM aiso_buffer_level_4" +
                        " WHERE GROUP_ACCOUNT = "+groupAccount+
                        " GROUP BY ID_PERKIRAAN " +
                        ", NOMOR_PERKIRAAN" +
                        ", NAMA" +
                        ", TANDA_DEBET_KREDIT" +
                        ", GROUP_ACCOUNT,BGT " +
                        " ORDER BY NOMOR_PERKIRAAN";

                    dbrs = DBHandler.execQueryResult(sql);
                    ResultSet rs4 = dbrs.getResultSet();
                    
                    while (rs4.next()){
                            list = prosesRsGetValue(rs4,list); 
                        
                      sql = " SELECT ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,TANDA_DEBET_KREDIT," +
                            "GROUP_ACCOUNT,BGT" +
                            ", SUM(DBT) AS TOT_DBT" +
                            ", SUM(KRDT) AS TOT_KRDT " +
                            ", SUM(SALDO_AWAL_DEBET) AS TOT_SADBT" +
                            ", SUM(SALDO_AWAL_KREDIT) AS TOT_SAKRDT " +
                            " FROM aiso_buffer_level_5" +
                            " WHERE GROUP_ACCOUNT = "+groupAccount+
                            " GROUP BY ID_PERKIRAAN " +
                            ", NOMOR_PERKIRAAN" +
                            ", NAMA" +
                            ", TANDA_DEBET_KREDIT" +
                            ", GROUP_ACCOUNT,BGT " +
                            " ORDER BY NOMOR_PERKIRAAN";

                            dbrs = DBHandler.execQueryResult(sql);
                            ResultSet rs5 = dbrs.getResultSet();
                            
                            while (rs5.next()){
                                list = prosesRsGetValue(rs5,list); 
                        
                                sql =   " SELECT ID_PERKIRAAN,NOMOR_PERKIRAAN,NAMA,TANDA_DEBET_KREDIT," +
                                        "GROUP_ACCOUNT,BGT" +
                                        ", SUM(DBT) AS TOT_DBT" +
                                        ", SUM(KRDT) AS TOT_KRDT " +
                                        ", SUM(SALDO_AWAL_DEBET) AS TOT_SADBT" +
                                        ", SUM(SALDO_AWAL_KREDIT) AS TOT_SAKRDT " +
                                        " FROM aiso_buffer_level_5" +
                                        " WHERE GROUP_ACCOUNT = "+groupAccount+
                                        " GROUP BY ID_PERKIRAAN " +
                                        ", NOMOR_PERKIRAAN" +
                                        ", NAMA" +
                                        ", TANDA_DEBET_KREDIT" +
                                        ", GROUP_ACCOUNT,BGT " +
                                        " ORDER BY NOMOR_PERKIRAAN";

                            dbrs = DBHandler.execQueryResult(sql);
                            ResultSet rs6 = dbrs.getResultSet();
                            
                            list = prosesRsGetValue(rs6,list); 
                            
                            }
                        }
                    }
                }
            }
        }catch(Exception e){}
        return list;
    }
    
    public static Vector prosesRsGetValue(ResultSet rs, Vector list){
        try{
            boolean status = true;
            WorkSheet workSheet = new WorkSheet();
            workSheet.setNomorPerkiraan(rs.getString("NOMOR_PERKIRAAN")); // PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]));
            workSheet.setNamaPerkiraan(rs.getString("NAMA")); // PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]));            
            workSheet.setAnggaran(rs.getDouble("BGT"));

            // mutasi
            double totDebet = rs.getDouble("TOT_DBT");
            double totKredit = rs.getDouble("TOT_KRDT");
            workSheet.setDebetMutasi(totDebet);
            workSheet.setKreditMutasi(totKredit);
            workSheet.setTandaDebetKredit(rs.getInt("TANDA_DEBET_KREDIT")); // PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]));

            double totSalDebet = rs.getDouble("TOT_SADBT"); //saldoAkhirPeriode.getDebet();
            double totSalKredit = rs.getDouble("TOT_SAKRDT"); //saldoAkhirPeriode.getKredit();
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
                workSheet.setDebetNeracaSaldo(totNeracaSaldo);
            } else {
                totNeracaPerLalu = workSheet.getKreditNeracaPeriodeLalu() - workSheet.getDebetNeracaPeriodeLalu();
                totMutasi = workSheet.getKreditMutasi() - workSheet.getDebetMutasi();
                totNeracaSaldo = totNeracaPerLalu + totMutasi;
                workSheet.setKreditNeracaSaldo(totNeracaSaldo);
            } // -- end ----

            workSheet.setDebetNeraca(workSheet.getDebetNeracaSaldo());
            workSheet.setKreditNeraca(workSheet.getKreditNeracaSaldo());
            Neraca neraca = new Neraca();
            neraca.setNamaPerkiraan(workSheet.getNamaPerkiraan());
            if (workSheet.getDebetNeraca() != 0)
                neraca.setValue(workSheet.getDebetNeraca());
            else {
                neraca.setValue(workSheet.getKreditNeraca());
            }
            neraca.setBudget(workSheet.getAnggaran());

            switch (rs.getInt("GROUP_ACCOUNT")) { //PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP])) {
                case PstPerkiraan.ACC_GROUP_LIQUID_ASSETS:
                    if (workSheet.getKreditNeraca() > 0)
                        neraca.setValue(neraca.getValue() * -1);
                    list.add(neraca);
                    break;
                case PstPerkiraan.ACC_GROUP_FIXED_ASSETS:
                    if (workSheet.getKreditNeraca() > 0)
                        neraca.setValue(neraca.getValue() * -1);
                    list.add(neraca);
                    break;
                case PstPerkiraan.ACC_GROUP_OTHER_ASSETS:
                    if (workSheet.getKreditNeraca() > 0)
                        neraca.setValue(neraca.getValue() * -1);
                    list.add(neraca);
                    break;
                case PstPerkiraan.ACC_GROUP_CURRENCT_LIABILITIES:
                    if (workSheet.getDebetNeraca() > 0)
                        neraca.setValue(neraca.getValue() * -1);
                    list.add(neraca);
                    break;
                case PstPerkiraan.ACC_GROUP_LONG_TERM_LIABILITIES:
                    if (workSheet.getDebetNeraca() > 0)
                        neraca.setValue(neraca.getValue() * -1);
                    list.add(neraca);
                    break;
                case PstPerkiraan.ACC_GROUP_EQUITY:
                    if (workSheet.getDebetNeraca() > 0)
                        neraca.setValue(neraca.getValue() * -1);
                    list.add(neraca);
                    break;
            }//End switch
        }catch(Exception e){
        
        }//End Catch
        return list;
    } //End methode
    
    public static void main(String[] arg){
        String[] x = {"a","b","c","d","e"};
        Vector v = new Vector(1,1);
        for(int i = 0; i < x.length; i++){
            v.add(x[i]);
        }
        
        for(int a = 0; a < v.size(); a++){
            if(a > 0)
            System.out.println("isi vector v indek ke a - 1 ::: "+v.get(a-1).toString());
            
            System.out.println("isi vector v indek ke a ::: "+v.get(a).toString());
            
        }
    }
}
