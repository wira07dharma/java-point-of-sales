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
import com.dimata.aiso.entity.masterdata.PstActivityAccountLink;
import com.dimata.aiso.entity.masterdata.PstActivityPeriodLink;
import com.dimata.aiso.entity.periode.Periode;
import com.dimata.aiso.entity.periode.PstPeriode;
import com.dimata.aiso.entity.report.WorkSheet;
import com.dimata.aiso.entity.report.Neraca;
import com.dimata.aiso.entity.report.ProfitLoss;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalDetail;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalMain;
import com.dimata.interfaces.chartofaccount.I_ChartOfAccountGroup;
import com.dimata.interfaces.journal.I_JournalType;
import com.dimata.util.Formater;

import java.util.Vector;
import java.sql.ResultSet;
import java.util.Date;

public class SessProfitLoss {


    /** gadnyana
     * untuk menampilkan data
     * @param oidPeriod
     * @param departmentOid
     * @return
     */
    synchronized public static Vector getListLoss(long oidPeriod, long departmentOid) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            long preOidPeriod = SessWorkSheet.getOidPeriodeLalu(oidPeriod);
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
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_DEPARTMENT_ID] + "=" + departmentOid +
                    " AND (ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_REVENUE +
                    " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_COST_OF_SALES +
                    " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_EXPENSE +
                    " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_OTHER_REVENUE +
                    " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + "=" + PstPerkiraan.ACC_GROUP_OTHER_EXPENSE + ")" +
                    " AND JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE] + "=" + I_JournalType.TIPE_JURNAL_UMUM +
                    " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " ORDER BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];

            System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            // untuk menyimpan masing masing group account
            Vector listReg = new Vector(1, 1);
            Vector listCos = new Vector(1, 1);
            Vector listExp = new Vector(1, 1);
            Vector listORev = new Vector(1, 1);
            Vector listOExp = new Vector(1, 1);

            while (rs.next()) {
                WorkSheet workSheet = new WorkSheet();
                workSheet.setNomorPerkiraan(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]));
                workSheet.setNamaPerkiraan(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]));
                workSheet.setAccountNameEnglish((rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH])));
                workSheet.setAnggaran(rs.getDouble("BGT"));

                // mutasi
                double totDebet = rs.getDouble("DBT");
                double totKredit = rs.getDouble("KRDT");
                workSheet.setDebetMutasi(totDebet);
                workSheet.setKreditMutasi(totKredit);
                workSheet.setTandaDebetKredit(rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]));

                // saldo awal
                SaldoAkhirPeriode saldoAkhirPeriode = SessWorkSheet.getSaldoAwal(preOidPeriod, rs.getLong(PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]));
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

                workSheet.setDebetNeraca(workSheet.getDebetNeracaSaldo());
                workSheet.setKreditNeraca(workSheet.getKreditNeracaSaldo());

                ProfitLoss profitLoss = new ProfitLoss();
                profitLoss.setNamaPerkiraan(workSheet.getNamaPerkiraan());
                profitLoss.setAccountNameEnglish(workSheet.getAccountNameEnglish());
                if (workSheet.getTandaDebetKredit() == PstPerkiraan.ACC_DEBETSIGN){
                    if(workSheet.getDebetNeraca()<0)
                        profitLoss.setValue(workSheet.getDebetNeraca() * -1);
                    else
                        profitLoss.setValue(workSheet.getDebetNeraca());
                }else{
                    if(workSheet.getKreditNeraca()<0)
                        profitLoss.setValue(workSheet.getKreditNeraca() * -1);
                    else
                        profitLoss.setValue(workSheet.getKreditNeraca());
                }

                profitLoss.setBudget(workSheet.getAnggaran());

                switch (rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP])) {
                    case PstPerkiraan.ACC_GROUP_REVENUE: // pendapatan operasional
                        if (workSheet.getDebetNeraca() > 0)
                            profitLoss.setValue(profitLoss.getValue() * -1);
                        listReg.add(profitLoss);
                        break;
                    case PstPerkiraan.ACC_GROUP_COST_OF_SALES: // harga pokok penjualan
                        if (workSheet.getKreditNeraca() > 0)
                            profitLoss.setValue(profitLoss.getValue() * -1);
                        listCos.add(profitLoss);
                        break;
                    case PstPerkiraan.ACC_GROUP_EXPENSE: // biaya operasional
                        if (workSheet.getKreditNeraca() > 0)
                            profitLoss.setValue(profitLoss.getValue() * -1);
                        listExp.add(profitLoss);
                        break;
                    case PstPerkiraan.ACC_GROUP_OTHER_REVENUE: // pendapatan non operasional
                        if (workSheet.getDebetNeraca() > 0)
                            profitLoss.setValue(profitLoss.getValue() * -1);
                        listORev.add(profitLoss);
                        break;
                    case PstPerkiraan.ACC_GROUP_OTHER_EXPENSE: // biaya non operasional
                        if (workSheet.getDebetNeraca() > 0)
                            profitLoss.setValue(profitLoss.getValue() * -1);
                        listOExp.add(profitLoss);
                        break;
                }
            }

            result.add(listReg);
            result.add(listCos);
            result.add(listExp);
            result.add(listORev);
            result.add(listOExp);

            rs.close();

        } catch (Exception e) {
        }
        return result;
    }
    
    /**
     *return vector list profit loss
     *@OID selected period
     *@OID selected department
     *constructor DWI 2007-07-02
     */
    synchronized public static Vector getListProfitLoss(long oidPeriod, long departmentOid) {
        DBResultSet dbrs = null;
        Vector vecList = (Vector)getListPL(oidPeriod, departmentOid);
        return vecList;
    }
    
    /**
     *return vector list profit loss
     *@OID selected period
     *@OID selected department
     *constructor DWI 2007-07-02
     */
     synchronized public static Vector getListPL(long oidPeriod, long departmentOid) {
        DBResultSet dbrs = null;
        Vector vecList = new Vector();
        try{
            String sql = "";
            
            //Mengosongkan table temprorary
            sql = " DELETE FROM aiso_rl_buffer ";
            DBHandler.execUpdate(sql);   
          
            int insertDataProfitLoss = insertDataProfitLoss(oidPeriod,departmentOid);
            
            sql = " SELECT ID_PERKIRAAN, NOMOR_PERKIRAAN, NAMA, TANDA_DEBET_KREDIT, SUM(YTD_DEBET) AS YTD_DEBET, " +
                " SUM(YTD_KREDIT) AS YTD_KREDIT, SUM(MTD_DBT) AS MTD_DBT, SUM(MTD_KRDT) AS MTD_KRDT, GROUP_ACCOUNT, " +
                " ACCOUNT_NAME_ENGLISH, ID_PARENT, SUM(MTD_BGT) AS MTD_BGT, SUM(YTD_BGT) AS YTD_BGT FROM aiso_rl_buffer " +
                " GROUP BY ID_PERKIRAAN, NOMOR_PERKIRAAN, NAMA, TANDA_DEBET_KREDIT, GROUP_ACCOUNT, ACCOUNT_NAME_ENGLISH, " +
                " ID_PARENT ORDER BY NOMOR_PERKIRAAN ";
            
            //System.out.println(" SQL LIST PROFIT AND LOSS =====> "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            Vector vecRevenue = new Vector(1,1);
            Vector vecHPP = new Vector(1,1);
            Vector vecExpenses = new Vector(1,1);
            Vector vecOtherRevenue = new Vector(1,1);
            Vector vecOtherExpenses = new Vector(1,1);
            Vector vecTax = new Vector(1,1);
            
            while (rs.next()) {
                ProfitLoss profitLoss = new ProfitLoss();
                
                int iAccountGroup = rs.getInt("GROUP_ACCOUNT");
                double valYtdDebet = rs.getDouble("YTD_DEBET");
                double valYtdKredit = rs.getDouble("YTD_KREDIT");
                double valMtdDebet = rs.getDouble("MTD_DBT");
                double valMtdKredit = rs.getDouble("MTD_KRDT");
                
                switch(iAccountGroup){
                    case I_ChartOfAccountGroup.ACC_GROUP_REVENUE :                        
                            profitLoss.setAccountId(rs.getLong("ID_PERKIRAAN"));
                            profitLoss.setAccNumber(rs.getString("NOMOR_PERKIRAAN"));
                            profitLoss.setNamaPerkiraan(rs.getString("NAMA"));
                             if( valMtdDebet > 0 && valMtdKredit == 0)
                                profitLoss.setValue(valMtdDebet);
                            if( valMtdKredit > 0 && valMtdDebet == 0)
                                profitLoss.setValue(valMtdKredit);
			    if( valMtdKredit > 0 && valMtdDebet > 0)
                                profitLoss.setValue(valMtdKredit - valMtdDebet);
                            if( valYtdDebet > 0 && valYtdKredit == 0)
                                profitLoss.setYtdValue(valYtdDebet);
                            if( valYtdKredit > 0 && valYtdDebet == 0)
                                profitLoss.setYtdValue(valYtdKredit);
			    if( valYtdKredit > 0 && valYtdDebet > 0)
                                profitLoss.setYtdValue(valYtdKredit - valYtdDebet);
                            profitLoss.setYtdValue(valYtdKredit);
                            profitLoss.setBudget(rs.getDouble("MTD_BGT"));
                            profitLoss.setYtdBudget(rs.getDouble("YTD_BGT"));
                            profitLoss.setAccountNameEnglish(rs.getString("ACCOUNT_NAME_ENGLISH"));
                            profitLoss.setIdParent(rs.getLong("ID_PARENT"));
                            vecRevenue.add(profitLoss);
                    break;
                    case I_ChartOfAccountGroup.ACC_GROUP_COST_OF_SALES :                        
                            profitLoss.setAccountId(rs.getLong("ID_PERKIRAAN"));
                            profitLoss.setAccNumber(rs.getString("NOMOR_PERKIRAAN"));
                            profitLoss.setNamaPerkiraan(rs.getString("NAMA"));
                            if( valMtdDebet > 0 && valMtdKredit == 0)
                                profitLoss.setValue(valMtdDebet);
                            if( valMtdKredit > 0 && valMtdDebet == 0)
                                profitLoss.setValue(valMtdKredit);
			    if( valMtdKredit > 0 && valMtdDebet > 0)
                                profitLoss.setValue(valMtdDebet - valMtdKredit);
                            if( valYtdDebet > 0 && valYtdKredit == 0)
                                profitLoss.setYtdValue(valYtdDebet);
                            if( valYtdKredit > 0 && valYtdDebet == 0)
                                profitLoss.setYtdValue(valYtdKredit);
			    if( valYtdKredit > 0 && valYtdDebet > 0)
                                profitLoss.setYtdValue(valYtdDebet - valYtdKredit);
                            profitLoss.setBudget(rs.getDouble("MTD_BGT"));
                            profitLoss.setYtdBudget(rs.getDouble("YTD_BGT"));
                            profitLoss.setAccountNameEnglish(rs.getString("ACCOUNT_NAME_ENGLISH"));
                            profitLoss.setIdParent(rs.getLong("ID_PARENT"));
                            vecHPP.add(profitLoss);
                    break;
                    case I_ChartOfAccountGroup.ACC_GROUP_EXPENSE:
                            profitLoss.setAccountId(rs.getLong("ID_PERKIRAAN"));
                            profitLoss.setAccNumber(rs.getString("NOMOR_PERKIRAAN"));
                            profitLoss.setNamaPerkiraan(rs.getString("NAMA"));
                            if( valMtdDebet > 0 && valMtdKredit == 0)
                                profitLoss.setValue(valMtdDebet);
                            if( valMtdKredit > 0 && valMtdDebet == 0)
                                profitLoss.setValue(valMtdKredit);
			    if( valMtdKredit > 0 && valMtdDebet > 0)
                                profitLoss.setValue(valMtdDebet - valMtdKredit);
                            if( valYtdDebet > 0 && valYtdKredit == 0)
                                profitLoss.setYtdValue(valYtdDebet);
                            if( valYtdKredit > 0 && valYtdDebet == 0)
                                profitLoss.setYtdValue(valYtdKredit);
			    if( valYtdKredit > 0 && valYtdDebet > 0)
                                profitLoss.setYtdValue(valYtdDebet - valYtdKredit);
                            profitLoss.setBudget(rs.getDouble("MTD_BGT"));
                            profitLoss.setYtdBudget(rs.getDouble("YTD_BGT"));
                            profitLoss.setAccountNameEnglish(rs.getString("ACCOUNT_NAME_ENGLISH"));
                            profitLoss.setIdParent(rs.getLong("ID_PARENT"));
                            vecExpenses.add(profitLoss);                        
                        break;
                    case I_ChartOfAccountGroup.ACC_GROUP_OTHER_REVENUE:
                            profitLoss.setAccountId(rs.getLong("ID_PERKIRAAN"));
                            profitLoss.setAccNumber(rs.getString("NOMOR_PERKIRAAN"));
                            profitLoss.setNamaPerkiraan(rs.getString("NAMA"));
                            if( valMtdDebet > 0 && valMtdKredit == 0)
                                profitLoss.setValue(valMtdDebet);
                            if( valMtdKredit > 0 && valMtdDebet == 0)
                                profitLoss.setValue(valMtdKredit);
			    if( valMtdKredit > 0 && valMtdDebet > 0)
                                profitLoss.setValue(valMtdKredit - valMtdDebet);
                            if( valYtdDebet > 0 && valYtdKredit == 0)
                                profitLoss.setYtdValue(valYtdDebet);
                            if( valYtdKredit > 0 && valYtdDebet == 0)
                                profitLoss.setYtdValue(valYtdKredit);
			    if( valYtdKredit > 0 && valYtdDebet > 0)
                                profitLoss.setYtdValue(valYtdKredit - valYtdDebet);
                            profitLoss.setBudget(rs.getDouble("MTD_BGT"));
                            profitLoss.setYtdBudget(rs.getDouble("YTD_BGT"));
                            profitLoss.setAccountNameEnglish(rs.getString("ACCOUNT_NAME_ENGLISH"));
                            profitLoss.setIdParent(rs.getLong("ID_PARENT"));
                            vecOtherRevenue.add(profitLoss);   
                        break;
                    case I_ChartOfAccountGroup.ACC_GROUP_OTHER_EXPENSE:
                            profitLoss.setAccountId(rs.getLong("ID_PERKIRAAN"));
                            profitLoss.setAccNumber(rs.getString("NOMOR_PERKIRAAN"));
                            profitLoss.setNamaPerkiraan(rs.getString("NAMA"));
                           if( valMtdDebet > 0 && valMtdKredit == 0)
                                profitLoss.setValue(valMtdDebet);
                            if( valMtdKredit > 0 && valMtdDebet == 0)
                                profitLoss.setValue(valMtdKredit);
			    if( valMtdKredit > 0 && valMtdDebet > 0)
                                profitLoss.setValue(valMtdDebet - valMtdKredit);
                            if( valYtdDebet > 0 && valYtdKredit == 0)
                                profitLoss.setYtdValue(valYtdDebet);
                            if( valYtdKredit > 0 && valYtdDebet == 0)
                                profitLoss.setYtdValue(valYtdKredit);
			    if( valYtdKredit > 0 && valYtdDebet > 0)
                                profitLoss.setYtdValue(valYtdDebet - valYtdKredit);
                            profitLoss.setBudget(rs.getDouble("MTD_BGT"));
                            profitLoss.setYtdBudget(rs.getDouble("YTD_BGT"));
                            profitLoss.setAccountNameEnglish(rs.getString("ACCOUNT_NAME_ENGLISH"));
                            profitLoss.setIdParent(rs.getLong("ID_PARENT"));
                            vecOtherExpenses.add(profitLoss);
                        break;
                    case I_ChartOfAccountGroup.ACC_GROUP_INCOME_TAXES:
                            profitLoss.setAccountId(rs.getLong("ID_PERKIRAAN"));
                            profitLoss.setAccNumber(rs.getString("NOMOR_PERKIRAAN"));
                            profitLoss.setNamaPerkiraan(rs.getString("NAMA"));
                            if( valMtdDebet > 0 && valMtdKredit == 0)
                                profitLoss.setValue(valMtdDebet);
                            if( valMtdKredit > 0 && valMtdDebet == 0)
                                profitLoss.setValue(valMtdKredit);
			    if( valMtdKredit > 0 && valMtdDebet > 0)
                                profitLoss.setValue(valMtdDebet - valMtdKredit);
                            if( valYtdDebet > 0 && valYtdKredit == 0)
                                profitLoss.setYtdValue(valYtdDebet);
                            if( valYtdKredit > 0 && valYtdDebet == 0)
                                profitLoss.setYtdValue(valYtdKredit);
			    if( valYtdKredit > 0 && valYtdDebet > 0)
                                profitLoss.setYtdValue(valYtdDebet - valYtdKredit);
                            profitLoss.setBudget(rs.getDouble("MTD_BGT"));
                            profitLoss.setYtdBudget(rs.getDouble("YTD_BGT"));
                            profitLoss.setAccountNameEnglish(rs.getString("ACCOUNT_NAME_ENGLISH"));
                            profitLoss.setIdParent(rs.getLong("ID_PARENT"));
                            vecTax.add(profitLoss);
                        break;
                }
            } 
            
            vecList.add(vecRevenue);            
            vecList.add(vecHPP);
            vecList.add(vecExpenses);
            vecList.add(vecOtherRevenue);
            vecList.add(vecOtherExpenses);
            vecList.add(vecTax);
            
            rs.close();
            
            //System.out.println("vecList.size() ====> "+vecList.size());
        }catch(Exception e){
            System.out.println("Exception on getListProfitLoss =====> "+e.toString());
        }
        return vecList;
    }
    
     /**
     *return vector list profit loss summary
     *@OID selected period
     *@OID selected department
     *constructor DWI 2007-07-05
     */
     synchronized public static Vector getSummaryPL(long oidPeriod, long departmentOid){
        DBResultSet dbrs = null;
        Vector vResult = new Vector();
        String sql = "";
        try{
            //Mengosongkan table temprorary
            //sql = " DELETE FROM AISO_RL_BUFFER ";
              sql = " DELETE FROM aiso_rl_buffer ";
            DBHandler.execUpdate(sql);   
          
            int insertDataProfitLoss = insertDataProfitLoss(oidPeriod,departmentOid);
            sql = querySummaryPL();
            
            //System.out.println("SQL getSummaryPL ::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            Vector vRevenue = new Vector(1,1);
            Vector vCoGS = new Vector(1,1);
            Vector vExpenses = new Vector(1,1);
            Vector vOthRevenue = new Vector(1,1);
            Vector vOthExpenses = new Vector(1,1);
            Vector vTax = new Vector(1,1);
           
            
            while(rs.next()){
                int iAccountGroup = rs.getInt(4);
                double dYtdDebet  = rs.getDouble(6); 
                double dYtdKredit = rs.getDouble(7); 
                double dMtdDebet  = rs.getDouble(8); 
                double dMtdKredit = rs.getDouble(9);
                
                ProfitLoss objProfitLoss = new ProfitLoss();
                switch(iAccountGroup){
                    case I_ChartOfAccountGroup.ACC_GROUP_REVENUE:
                        objProfitLoss = resultToObject(objProfitLoss, rs, dYtdDebet, dYtdKredit, dMtdDebet, dMtdKredit,iAccountGroup);
                        vRevenue.add(objProfitLoss);
                        break;
                    case I_ChartOfAccountGroup.ACC_GROUP_COST_OF_SALES:
                         objProfitLoss = resultToObject(objProfitLoss, rs, dYtdDebet, dYtdKredit, dMtdDebet, dMtdKredit,iAccountGroup);
                        vCoGS.add(objProfitLoss);
                        break;
		    case I_ChartOfAccountGroup.ACC_GROUP_EXPENSE:
                         objProfitLoss = resultToObject(objProfitLoss, rs, dYtdDebet, dYtdKredit, dMtdDebet, dMtdKredit,iAccountGroup);
                        vExpenses.add(objProfitLoss);
                        break;
                    case I_ChartOfAccountGroup.ACC_GROUP_OTHER_REVENUE:
                         objProfitLoss = resultToObject(objProfitLoss, rs, dYtdDebet, dYtdKredit, dMtdDebet, dMtdKredit,iAccountGroup);
                        vOthRevenue.add(objProfitLoss);
                        break;   
                    case I_ChartOfAccountGroup.ACC_GROUP_OTHER_EXPENSE:
                         objProfitLoss = resultToObject(objProfitLoss, rs, dYtdDebet, dYtdKredit, dMtdDebet, dMtdKredit,iAccountGroup);
                        vOthExpenses.add(objProfitLoss);
                        break;
                    case I_ChartOfAccountGroup.ACC_GROUP_INCOME_TAXES:
                         objProfitLoss = resultToObject(objProfitLoss, rs, dYtdDebet, dYtdKredit, dMtdDebet, dMtdKredit,iAccountGroup);
                        vTax.add(objProfitLoss);
                        break;        
                }
            }
            
            
            vResult.add(vRevenue);
            vResult.add(vCoGS);
            vResult.add(vExpenses);
            vResult.add(vOthRevenue);
            vResult.add(vOthExpenses);
            vResult.add(vTax);
            
        }catch(Exception e){
            vResult = new Vector();
            System.out.println("Exception on getSummaryPL() ::: "+e.toString());
        }
        return vResult;
     }
     
     /**
     *return object ProfitLoss
     *@param object ProfitLoss
     *@param object ResultSet
     *@param double Debet Year to Date
     *@param double Credit Year to Date
     *@param double Debet Month to Date
     *@param double Credit Month to Date
     *constructor DWI 2007-07-02
     */
     private static ProfitLoss resultToObject(ProfitLoss objProfitLoss, ResultSet rs, double dYtdDebet, double dYtdKredit,
     double dMtdDebet, double dMtdKredit, int iAccountGroup){
        try{
            objProfitLoss.setNamaPerkiraan(rs.getString(1));
            objProfitLoss.setAccountNameEnglish(rs.getString(2));
            objProfitLoss.setAccNumber(rs.getString(3));
            objProfitLoss.setBudget(rs.getDouble(10));
            objProfitLoss.setYtdBudget(rs.getDouble(11));
	    boolean isRevenue = false;
	    switch(iAccountGroup){
		case I_ChartOfAccountGroup.ACC_GROUP_REVENUE:
		    isRevenue = true;
		    break;
		case I_ChartOfAccountGroup.ACC_GROUP_OTHER_REVENUE:
		    isRevenue = true;
		    break;
		default:
		    isRevenue = false;
	    }
	    
            if( dMtdDebet > 0 && dMtdKredit == 0)
                objProfitLoss.setValue(dMtdDebet);
            if( dMtdKredit > 0 && dMtdDebet == 0)
                objProfitLoss.setValue(dMtdKredit);
	    if( dMtdKredit > 0 && dMtdDebet > 0){
		if(isRevenue){
		    objProfitLoss.setValue(dMtdKredit - dMtdDebet);
		}else{
		    objProfitLoss.setValue(dMtdDebet - dMtdKredit);
		}
	    }
            if( dYtdDebet > 0 && dYtdKredit == 0)
                objProfitLoss.setYtdValue(dYtdDebet);
            if( dYtdKredit > 0 && dYtdDebet == 0)
                objProfitLoss.setYtdValue(dYtdKredit);
	    if( dYtdKredit > 0 && dYtdDebet > 0){
		if(isRevenue){
		    objProfitLoss.setYtdValue(dYtdKredit - dYtdDebet);
		}else{
		    objProfitLoss.setYtdValue(dYtdDebet - dYtdKredit);
		}
	    }
        }catch(Exception e){
            objProfitLoss = new ProfitLoss();
            System.out.println("Exception on resultToObject ::: "+e.toString());
        }
         return objProfitLoss;
     }
     
     /**
     *return String sql summary profit loss
     *constructor DWI 2007-07-02
     */
     private static String querySummaryPL(){
        String sql = "";
            try{
                sql = " SELECT PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                      ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
                      ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]+
                      ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
                      ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+
                      ", SUM(BUF.YTD_DEBET) AS YTD_DEBET, SUM(BUF.YTD_KREDIT) AS YTD_KREDIT"+
                      ", SUM(BUF.MTD_DBT) AS MTD_DBT, SUM(BUF.MTD_KRDT) AS MTD_KRDT"+
                      ", SUM(BUF.MTD_BGT) AS MTD_BGT, SUM(BUF.YTD_BGT) AS YTD_BGT"+
                      //" FROM AISO_RL_BUFFER AS BUF"+
                      " FROM aiso_rl_buffer AS BUF"+
                      " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS PRK"+
                      " ON BUF.ID_PARENT = PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                      " GROUP BY PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                      ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
                      ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]+
                      ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
                      ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+
                      " ORDER BY PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on querySummaryPL() ::: "+e.toString());
            }
        return sql;
     }
     
      /**
     *return int insertDataProfitLoss
       *@param long OID selected period
       *@param long OID selected department
     *constructor DWI 2007-07-02
     */
    private static int insertDataProfitLoss(long periodId, long departmentId){
        int iResult = 0;
            try{
                iResult = insertMTDProfitLossJU(periodId, departmentId);
                iResult = insertMTDRevenueSJ(periodId, departmentId);
                iResult = insertMTDExpensesSJBank(periodId, departmentId);
                iResult = insertMTDExpensesSJNonBank(periodId, departmentId);
                
                iResult = insertYTDProfitLossJU(periodId, departmentId);
                iResult = insertYTDRevenueSJ(periodId, departmentId);
                iResult = insertYTDExpensesSJBank(periodId, departmentId);
                iResult = insertYTDExpensesSJNonBank(periodId, departmentId);
                
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on insertDataProfitLoss() ::: "+e.toString());
            }
        return iResult;
    }
    
    
     /**
     *return int insertMTDProfitLossJU
       *@param long OID selected period
       *@param long OID selected department
     *constructor DWI 2007-07-02
     */
    private static int insertMTDProfitLossJU(long periodId, long departmentId){
        DBResultSet dbrs = null;
        int iResult = 0;
        
        String whereClause = " WHERE JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID]+
                            " = "+periodId+" AND PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
                            " IN("+I_ChartOfAccountGroup.ACC_GROUP_REVENUE+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_EXPENSE+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_COST_OF_SALES+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_OTHER_REVENUE+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_OTHER_EXPENSE+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_INCOME_TAXES+") "+
                            " AND JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE]+
                            " = "+I_JournalType.TIPE_JURNAL_UMUM;
                            if(departmentId != 0){
                                whereClause += " AND "+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_DEPARTMENT_ID]+
                                    " = "+departmentId;
                            }
        
            try{
                String sql = queryInsertProfitLossData("MTD_DBT","MTD_KRDT","MTD_BGT")+
                            selectProfitLossDataJU(whereClause, queryGroupBy(), Float.parseFloat(String.valueOf(1)),  "DEBET", "KREDIT","BUDGET",I_JournalType.TIPE_JURNAL_UMUM);
                
                System.out.println(" SQL insertMTDProfitLossJU :::: "+sql);
                iResult = DBHandler.execUpdate(sql);
                
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on insertMTDProfitLossJU() ::: "+e.toString());
            }
        return iResult;
    }
    
    /**
     *return int insertYTDProfitLossJU
       *@param long OID selected period
       *@param long OID selected department
     *constructor DWI 2007-07-02
     */
    private static int insertYTDProfitLossJU(long periodId, long departmentId){
        DBResultSet dbrs = null;
        int iResult = 0;
        
        Periode objPeriod = new Periode();
        try{
            objPeriod = PstPeriode.fetchExc(periodId);
        }catch(Exception e){
            objPeriod = new Periode();
            System.out.println("Exception on fetch objPeriode ::: "+e.toString());
        }
        
        Date yearStartDate = new Date();        
        Date dEndDate = new Date();
        
        if(objPeriod != null){
            if(objPeriod.getPosted() == PstPeriode.PERIOD_CLOSED){
                dEndDate = (Date)objPeriod.getTglAkhir();
            }
            yearStartDate = (Date)objPeriod.getTglAwal();
        }
        
        String endDate = Formater.formatDate(dEndDate, "yyyy-MM-dd"); 
        
        yearStartDate.setDate(1);
        yearStartDate.setMonth(0);
        
        String whereClause = " WHERE JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI]+
                            " BETWEEN '"+yearStartDate+"' AND '"+endDate+"' "+
                            " AND PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
                            " IN("+I_ChartOfAccountGroup.ACC_GROUP_REVENUE+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_EXPENSE+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_COST_OF_SALES+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_OTHER_REVENUE+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_OTHER_EXPENSE+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_INCOME_TAXES+") "+
                            " AND JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE]+
                            " = "+I_JournalType.TIPE_JURNAL_UMUM;
                            if(departmentId != 0){
                                whereClause += " AND "+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_DEPARTMENT_ID]+
                                    " = "+departmentId;
                            }
        
            try{
                String sql = queryInsertProfitLossData("YTD_DEBET","YTD_KREDIT","YTD_BGT")+
                            selectProfitLossDataJU(whereClause, queryGroupBy(), Float.parseFloat(String.valueOf(dEndDate.getMonth() + 1)),  "DEBET", "KREDIT","BUDGET",I_JournalType.TIPE_JURNAL_UMUM);
                
                //System.out.println(" SQL insertYTDProfitLossJU :::: "+sql);
                iResult = DBHandler.execUpdate(sql);
                
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on insertYTDProfitLossJU() ::: "+e.toString());
            }
        return iResult;
    }
    
     /**
     *return int insertMTDRevenueSJ
       *@param long OID selected period
       *@param long OID selected department
     *constructor DWI 2007-07-02
     */
    
    private static int insertMTDRevenueSJ(long periodId, long departmentId){
        DBResultSet dbrs = null;
        int iResult = 0;
        
        String whereClause = " WHERE JU."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID]+
                            " = "+periodId+" AND PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
                            " IN("+I_ChartOfAccountGroup.ACC_GROUP_REVENUE+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_OTHER_REVENUE+") "+
                            " AND JU."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]+
                            " = "+I_JournalType.TIPE_SPECIAL_JOURNAL_CASH;
                            if(departmentId != 0){
                                whereClause += " AND "+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_DEPARTMENT_ID]+
                                    " = "+departmentId;
                            }
        
            try{
                String sql = queryInsertProfitLossData("MTD_DBT","MTD_KRDT","MTD_BGT")+
                            selectProfitLossDataSJ(whereClause, queryGroupBy(), Float.parseFloat(String.valueOf(1)),  "AMOUNT", "KREDIT","BUDGET",I_JournalType.TIPE_SPECIAL_JOURNAL_CASH);
                
                //System.out.println(" SQL insertMTDRevenueSJ :::: "+sql);
                iResult = DBHandler.execUpdate(sql);
                
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on insertMTDRevenueSJ() ::: "+e.toString());
            }
        return iResult;
    }
    
    /**
     *return int insertYTDRevenueSJ
       *@param long OID selected period
       *@param long OID selected department
     *constructor DWI 2007-07-02
     */
     private static int insertYTDRevenueSJ(long periodId, long departmentId){
        DBResultSet dbrs = null;
        int iResult = 0;
        
        Periode objPeriod = new Periode();
        try{
            objPeriod = PstPeriode.fetchExc(periodId);
        }catch(Exception e){
            objPeriod = new Periode();
            System.out.println("Exception on fetch objPeriode ::: "+e.toString());
        }
        
        Date yearStartDate = new Date();        
        Date dEndDate = new Date();
        
        if(objPeriod != null){
            if(objPeriod.getPosted() == PstPeriode.PERIOD_CLOSED){
                dEndDate = (Date)objPeriod.getTglAkhir();
            }
            yearStartDate = (Date)objPeriod.getTglAwal();
        }
        
        String endDate = Formater.formatDate(dEndDate, "yyyy-MM-dd"); 
        yearStartDate.setDate(1);
        yearStartDate.setMonth(0);
        
        String whereClause = " WHERE JU."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE]+
                            " BETWEEN '"+yearStartDate+"' AND '"+endDate+"' "+
                            " AND PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
                            " IN("+I_ChartOfAccountGroup.ACC_GROUP_REVENUE+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_OTHER_REVENUE+") "+
                            " AND JU."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]+
                            " = "+I_JournalType.TIPE_SPECIAL_JOURNAL_CASH;
                            if(departmentId != 0){
                                whereClause += " AND "+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_DEPARTMENT_ID]+
                                    " = "+departmentId;
                            }
        
            try{
                String sql = queryInsertProfitLossData("YTD_DEBET","YTD_KREDIT","YTD_BGT")+
                            selectProfitLossDataSJ(whereClause, queryGroupBy(), Float.parseFloat(String.valueOf(dEndDate.getMonth() + 1)),  "AMOUNT", "KREDIT","BUDGET",I_JournalType.TIPE_SPECIAL_JOURNAL_CASH);
                
                //System.out.println(" SQL insertYTDRevenueSJ :::: "+sql);
                iResult = DBHandler.execUpdate(sql);
                
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on insertYTDRevenueSJ() ::: "+e.toString());
            }
        return iResult;
    }
     
     /**
     *return int insertMTDExpensesSJBank
       *@param long OID selected period
       *@param long OID selected department
     *constructor DWI 2007-07-02
     */
      private static int insertMTDExpensesSJBank(long periodId, long departmentId){
        DBResultSet dbrs = null;
        int iResult = 0;
        
        String whereClause = " WHERE JU."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID]+
                            " = "+periodId+" AND PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
                            " IN("+I_ChartOfAccountGroup.ACC_GROUP_EXPENSE+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_OTHER_EXPENSE+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_COST_OF_SALES+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_INCOME_TAXES+") "+
                            " AND JU."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]+
                            " = "+I_JournalType.TIPE_SPECIAL_JOURNAL_BANK+
                            " AND JU."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT_STATUS]+
                            " = "+PstSpecialJournalMain.STS_KREDIT;
                            if(departmentId != 0){
                                whereClause += " AND "+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_DEPARTMENT_ID]+
                                    " = "+departmentId;
                            }
        
            try{
                String sql = queryInsertProfitLossData("MTD_DBT","MTD_KRDT","MTD_BGT")+
                            selectProfitLossDataSJ(whereClause, queryGroupBy(), Float.parseFloat(String.valueOf(1)),  "AMOUNT", "KREDIT","BUDGET",I_JournalType.TIPE_SPECIAL_JOURNAL_BANK);
                
                //System.out.println(" SQL insertMTDExpensesSJBank :::: "+sql);
                iResult = DBHandler.execUpdate(sql);
                
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on insertMTDExpensesSJBank() ::: "+e.toString());
            }
        return iResult;
    }
      
      /**
     *return int insertYTDExpensesSJBank
       *@param long OID selected period
       *@param long OID selected department
     *constructor DWI 2007-07-02
     */
      private static int insertYTDExpensesSJBank(long periodId, long departmentId){
        DBResultSet dbrs = null;
        int iResult = 0;
        
        Periode objPeriod = new Periode();
        try{
            objPeriod = PstPeriode.fetchExc(periodId);
        }catch(Exception e){
            objPeriod = new Periode();
            System.out.println("Exception on fetch objPeriode ::: "+e.toString());
        }
        
        Date yearStartDate = new Date();        
        Date dEndDate = new Date();
        
        if(objPeriod != null){
            if(objPeriod.getPosted() == PstPeriode.PERIOD_CLOSED){
                dEndDate = (Date)objPeriod.getTglAkhir();
            }
            yearStartDate = (Date)objPeriod.getTglAwal();
        }
        
        String endDate = Formater.formatDate(dEndDate, "yyyy-MM-dd"); 
        
        yearStartDate.setDate(1);
        yearStartDate.setMonth(0);
        
        String whereClause = " WHERE JU."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE]+
                            " BETWEEN '"+yearStartDate+"' AND '"+endDate+"' "+
                            " AND PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
                            " IN("+I_ChartOfAccountGroup.ACC_GROUP_EXPENSE+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_OTHER_EXPENSE+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_COST_OF_SALES+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_INCOME_TAXES+") "+
                            " AND JU."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]+
                            " = "+I_JournalType.TIPE_SPECIAL_JOURNAL_BANK+
                            " AND JU."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT_STATUS]+
                            " = "+PstSpecialJournalMain.STS_KREDIT;
                            if(departmentId != 0){
                                whereClause += " AND "+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_DEPARTMENT_ID]+
                                    " = "+departmentId;
                            }
        
            try{
                String sql = queryInsertProfitLossData("YTD_DEBET","YTD_KREDIT","YTD_BGT")+
                            selectProfitLossDataSJ(whereClause, queryGroupBy(), Float.parseFloat(String.valueOf(dEndDate.getMonth() + 1)),  "AMOUNT", "KREDIT","BUDGET",I_JournalType.TIPE_SPECIAL_JOURNAL_BANK);
                
                //System.out.println(" SQL insertYTDExpensesSJBank :::: "+sql);
                iResult = DBHandler.execUpdate(sql);
                
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on insertYTDExpensesSJBank() ::: "+e.toString());
            }
        return iResult;
    }
     
       /**
     *return int insertMTDExpensesSJNonBank
       *@param long OID selected period
       *@param long OID selected department
     *constructor DWI 2007-07-02
     */
       private static int insertMTDExpensesSJNonBank(long periodId, long departmentId){
        DBResultSet dbrs = null;
        int iResult = 0;
        
        String whereClause = " WHERE JU."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID]+
                            " = "+periodId+" AND PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
                            " IN("+I_ChartOfAccountGroup.ACC_GROUP_EXPENSE+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_OTHER_EXPENSE+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_COST_OF_SALES+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_INCOME_TAXES+") "+
                            " AND JU."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]+
                            " IN("+I_JournalType.TIPE_SPECIAL_JOURNAL_PETTY_CASH+
                            ", "+I_JournalType.TIPE_SPECIAL_JOURNAL_NON_CASH+")";
                            if(departmentId != 0){
                                whereClause += " AND "+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_DEPARTMENT_ID]+
                                    " = "+departmentId;
                            }
        
            try{
                String sql = queryInsertProfitLossData("MTD_DBT","MTD_KRDT","MTD_BGT")+
                            selectProfitLossDataSJ(whereClause, queryGroupBy(), Float.parseFloat(String.valueOf(1)),  "AMOUNT", "KREDIT","BUDGET",I_JournalType.TIPE_SPECIAL_JOURNAL_PETTY_CASH);
                
                //System.out.println(" SQL insertMTDExpensesSJBank :::: "+sql);
                iResult = DBHandler.execUpdate(sql);
                
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on insertMTDExpensesSJBank() ::: "+e.toString());
            }
        return iResult;
    }
     
        /**
     *return int insertYTDExpensesSJNonBank
       *@param long OID selected period
       *@param long OID selected department
     *constructor DWI 2007-07-02
     */
      private static int insertYTDExpensesSJNonBank(long periodId, long departmentId){
        DBResultSet dbrs = null;
        int iResult = 0;
        
        Periode objPeriod = new Periode();
        try{
            objPeriod = PstPeriode.fetchExc(periodId);
        }catch(Exception e){
            objPeriod = new Periode();
            System.out.println("Exception on fetch objPeriode ::: "+e.toString());
        }
        
        Date yearStartDate = new Date();        
        Date dEndDate = new Date();
        
        if(objPeriod != null){
            if(objPeriod.getPosted() == PstPeriode.PERIOD_CLOSED){
                dEndDate = (Date)objPeriod.getTglAkhir();
            }
            yearStartDate = (Date)objPeriod.getTglAwal();
        }
        
        String endDate = Formater.formatDate(dEndDate, "yyyy-MM-dd"); 
        
        yearStartDate.setDate(1);
        yearStartDate.setMonth(0);
        
        String whereClause = " WHERE JU."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE]+
                            " BETWEEN '"+yearStartDate+"' AND '"+endDate+"' "+
                            " AND PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
                            " IN("+I_ChartOfAccountGroup.ACC_GROUP_EXPENSE+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_OTHER_EXPENSE+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_COST_OF_SALES+
                            ", "+I_ChartOfAccountGroup.ACC_GROUP_INCOME_TAXES+") "+
                            " AND JU."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]+
                            " IN("+I_JournalType.TIPE_SPECIAL_JOURNAL_PETTY_CASH+
                            ", "+I_JournalType.TIPE_SPECIAL_JOURNAL_NON_CASH+")";
                            if(departmentId != 0){
                                whereClause += " AND "+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_DEPARTMENT_ID]+
                                    " = "+departmentId;
                            }
        
            try{
                String sql = queryInsertProfitLossData("YTD_DEBET","YTD_KREDIT","YTD_BGT")+
                            selectProfitLossDataSJ(whereClause, queryGroupBy(), Float.parseFloat(String.valueOf(dEndDate.getMonth() + 1)),  "AMOUNT", "KREDIT","BUDGET",I_JournalType.TIPE_SPECIAL_JOURNAL_PETTY_CASH);
                
                //System.out.println(" SQL insertYTDExpensesSJNonBank :::: "+sql);
                iResult = DBHandler.execUpdate(sql);
                
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on insertYTDExpensesSJNonBank() ::: "+e.toString());
            }
        return iResult;
    }
    
          /**
     *return int queryInsertProfitLossData
       *@param long OID selected period
       *@param long OID selected department
     *constructor DWI 2007-07-02
     */
    private static String queryInsertProfitLossData(String strAmount, String strAmountCredit, String strBudget){
        String sql = "";
            try{
                
                sql = "INSERT INTO aiso_rl_buffer(ID_PERKIRAAN, NOMOR_PERKIRAAN, TANDA_DEBET_KREDIT," +
                " GROUP_ACCOUNT, NAMA, ACCOUNT_NAME_ENGLISH, ID_PARENT,"+strAmount+", "+strAmountCredit+", "+strBudget+")";
                
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on insertProfitLossDataJU() ::: "+e.toString());
            }
        return sql;
    }
    
     /**
     *return int selectProfitLossDataJU
       *@param long OID selected period
       *@param long OID selected department
     *constructor DWI 2007-07-02
     */
    private static String selectProfitLossDataJU(String where, String groupBy, float month, 
    String strAmount, String strCredit, String budget, int journalType){
        String sql = "";
            try{
                sql = querySelectProfitLoss(month,journalType,strAmount,strCredit,budget)+
                    " FROM "+PstJurnalUmum.TBL_JURNAL_UMUM+" AS JU "+
                    " INNER JOIN "+PstJurnalDetail.TBL_JURNAL_DETAIL+" AS JD "+
                    " ON JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]+
                    " = JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID]+
                    " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS PRK "+
                    " ON JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+
                    " = PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                    " LEFT JOIN "+PstActivityAccountLink.TBL_AISO_ACTIVITY_ACCOUNT+" AS ACT "+
                    " ON ACT."+PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACCOUNT_ID]+
                    " = PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                    " LEFT JOIN "+PstActivityPeriodLink.TBL_AISO_ACT_PERIOD_LINK+" AS BUD "+
                    " ON ACT."+PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACTIVITY_ID]+
                    " = BUD."+PstActivityPeriodLink.fieldNames[PstActivityPeriodLink.FLD_ACTIVITY_ID]+
                    where + groupBy;
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on selectProfitLossDataJU() ::: "+e.toString());
            }
        return sql;
    }
    
    /**
     *return int selectProfitLossDataSJ
       *@param long OID selected period
       *@param long OID selected department
     *constructor DWI 2007-07-02
     */
    private static String selectProfitLossDataSJ(String where, String groupBy, float month, 
    String strAmount, String strCredit, String budget,int journalType){
        String sql = "";
            try{
                sql = querySelectProfitLoss(month,journalType,strAmount,strCredit,budget)+
                    " FROM "+PstSpecialJournalMain.TBL_AISO_SPC_JMAIN+" AS JU "+
                    " INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS JD "+
                    " ON JU."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " = JD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                    " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS PRK "+
                    " ON JD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN]+
                    " = PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                    " LEFT JOIN "+PstActivityAccountLink.TBL_AISO_ACTIVITY_ACCOUNT+" AS ACT "+
                    " ON ACT."+PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACCOUNT_ID]+
                    " = PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                    " LEFT JOIN "+PstActivityPeriodLink.TBL_AISO_ACT_PERIOD_LINK+" AS BUD "+
                    " ON ACT."+PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACTIVITY_ID]+
                    " = BUD."+PstActivityPeriodLink.fieldNames[PstActivityPeriodLink.FLD_ACTIVITY_ID]+
                    where + groupBy;
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on selectProfitLossDataJU() ::: "+e.toString());
            }
        return sql;
    }
    
    /**
     *return String querySelectProfitLoss
       *@param long OID selected period
       *@param long OID selected department
     *constructor DWI 2007-07-02
     */
     private static String querySelectProfitLoss(float month, int journalType, String strAmount, String strCredit, String budget){
        String sql = "";
            try{
                sql = "SELECT PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT];  
                
                   if(journalType == I_JournalType.TIPE_JURNAL_UMUM){
                                sql = sql + ", SUM(JD."+strAmount+") AS "+strAmount+
                                    ", SUM(JD."+strCredit+") AS "+strCredit;
                   }
                
                  if(journalType == I_JournalType.TIPE_SPECIAL_JOURNAL_CASH){
                        sql = sql + ", 0 AS DEBET "+
                                ", SUM(JD."+strAmount+") AS "+strCredit;
                      }
                
                   if(journalType == I_JournalType.TIPE_SPECIAL_JOURNAL_BANK){
                        sql = sql + ", SUM(JD."+strAmount+") AS DEBET "+
                                ", 0 AS "+strCredit;
                       }
                
                if(journalType == I_JournalType.TIPE_SPECIAL_JOURNAL_PETTY_CASH){
                        sql = sql + ", SUM(JD."+strAmount+") AS DEBET "+
                                ", 0 AS "+strCredit;
                       }
                
               
                float selMonth = month / 12;
                
                 sql = sql+", SUM("+selMonth+" * BUD."+budget+") AS "+budget;
                 
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on querySelectProfitLoss() ::: "+e.toString());
            }
        return sql;
    }
    
     private static String queryGroupBy(){
        String sql = "";
            try{
                sql = " GROUP BY PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT];
                
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on queryGroupBy() ::: "+e.toString());
            }
        return sql;
     }
     
        public static void main(String[] arg){
           Vector a = new Vector();
           a.add(""+100);
           a.add(""+200);
           double b = 0.0;
           b = Double.parseDouble(a.get(0).toString());
           System.out.println("Double hasil vector :::: "+b);
        }
} 
