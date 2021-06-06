/*
 * SessSaldoAkhirPeriode.java
 *
 * Created on August 25, 2005, 10:48 AM
 */

package com.dimata.aiso.session.jurnal;

// import java

import java.util.*;
import java.util.Date;
import java.sql.*;

// import qdep
import com.dimata.aiso.db.*;
import com.dimata.util.*;

// import aiso
import com.dimata.aiso.entity.masterdata.*; 
import com.dimata.aiso.entity.periode.*;
import com.dimata.aiso.entity.jurnal.*;
import com.dimata.aiso.entity.report.PstReportFixedAssets;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalDetail;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalMain;
import com.dimata.aiso.session.periode.*;

// import harisma
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.interfaces.journal.I_JournalType;

/**
 *
 * @author  gedhy
 */
public class SessSaldoAkhirPeriode {

    /**
     * mengambil vector of object AccountBalance utk account2 yang termasuk Laba(Rugi)
     * @param lDepartmentOid
     * @param lSelectedPeriodOid
     * @return
     */
    private Vector getVectOfObjPLAccountBalance(long lDepartmentOid, long lSelectedPeriodOid) {
        Vector vResult = new Vector(1, 1);

        DBResultSet dbrs = null;
        try {
            String sql = "SELECT UN.ACCID AS ACCID, UN.ACCASSIGN AS ACCASSIGN, UN.ACCNO AS ACCNO, SUM(UN.DEBET) AS DEBET"+ 
                    " , SUM(UN.KREDIT) AS KREDIT" +
                    " FROM (" + getQueryPLGeneralJournal(lDepartmentOid,lSelectedPeriodOid) + 
                    " UNION "+ getQuerySpecialJournalExpense(lDepartmentOid,lSelectedPeriodOid) +
                    " UNION "+ getQuerySpecialJournalRevenue(lDepartmentOid,lSelectedPeriodOid) +
                    ") AS UN "+
                    " GROUP BY ACCID, ACCASSIGN, ACCNO "+
                    " ORDER BY ACCNO";

            //System.out.println("com.dimata.aiso.session.jurnal.getVectOfObjPLAccountBalance sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                AccountBalance objAccountBalance = new AccountBalance();

                objAccountBalance.setLPerkiraanOid(rs.getLong("ACCID"));
                objAccountBalance.setINormalSign(rs.getInt("ACCASSIGN"));
                objAccountBalance.setDDebet(rs.getDouble("DEBET"));
                objAccountBalance.setDCredit(rs.getDouble("KREDIT"));

                vResult.add(objAccountBalance);
            }
        } catch (Exception e) {
            System.out.println("");
        } finally {
            DBResultSet.close(dbrs);
        }

        return vResult;
    }

    public static String getQueryPLGeneralJournal(long lDepartmentOid, long lSelectedPeriodOid){
        String sql = "";
            try{
                sql = "SELECT ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +" AS ACCID"+
                    " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +" AS ACCASSIGN"+
                    " , ACC."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]+" AS ACCNO"+
                    " , SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET] + ") AS DEBET" +
                    " , SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT] + ") AS KREDIT" +
                    " FROM " + PstJurnalUmum.TBL_JURNAL_UMUM + " AS JU" +
                    " INNER JOIN " + PstJurnalDetail.TBL_JURNAL_DETAIL + " AS JD" +
                    " ON JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                    " = JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID] +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID] +
                    " = " + lSelectedPeriodOid +
                    " AND JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE] +
                    " = " + PstJurnalUmum.TIPE_JURNAL_UMUM +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " IN(" + PstPerkiraan.ACC_GROUP_REVENUE +
                    " ," + PstPerkiraan.ACC_GROUP_COST_OF_SALES +
                    " ," + PstPerkiraan.ACC_GROUP_EXPENSE +
                    " ,"+ PstPerkiraan.ACC_GROUP_OTHER_REVENUE +
                    " ,"+ PstPerkiraan.ACC_GROUP_OTHER_EXPENSE + ")";                
                
                String groupBy = " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                        " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                        " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]; 
                
                    if(lDepartmentOid != 0){
                        String where = " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_DEPARTMENT_ID] +
                                       " = " + lDepartmentOid;                                
                        sql = sql + where + groupBy;
                    }else{
                        sql = sql + groupBy;
                    }
            }catch(Exception e){
                System.out.println("Exception on getQueryPLGeneralJournal ::: "+e.toString());
            }
        return sql;
    }

    public static String getQuerySpecialJournalExpense(long lDepartmentOid, long lSelectedPeriodOid){
        String sql = "";
            try{
                sql = "SELECT ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +" AS ACCID"+
                    " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +" AS ACCASSIGN"+
                    " , ACC."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]+" AS ACCNO"+
                    " , SUM(SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT] + ") AS DEBET" +
                    " , 0 AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +
                    " INNER JOIN " + PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL + " AS SD" +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID] +
                    " = SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID] +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " IN(" + PstSpecialJournalMain.TIPE_SPECIAL_JOURNAL_PETTY_CASH +
                    ", "+PstSpecialJournalMain.TIPE_SPECIAL_JOURNAL_NON_CASH+
                    ", "+PstSpecialJournalMain.TIPE_SPECIAL_JOURNAL_BANK+") "+
                    " AND SD."+ PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT_STATUS]+
                    " = "+PstSpecialJournalMain.STS_DEBET+
                    " AND ACC."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " IN(" + PstPerkiraan.ACC_GROUP_COST_OF_SALES +
                    " ," + PstPerkiraan.ACC_GROUP_EXPENSE +
                    " ," + PstPerkiraan.ACC_GROUP_OTHER_EXPENSE + ")" +
                    " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];  
                 
            }catch(Exception e){
                System.out.println("Exception on getQuerySpecialJournalExpense :::: "+e.toString());
            }
        return sql;
    }
    
     public static String getQuerySpecialJournalRevenue(long lDepartmentOid, long lSelectedPeriodOid){
        String sql = "";
            try{
                sql = "SELECT ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +" AS ACCID"+
                    " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +" AS ACCASSIGN"+
                    " , ACC."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]+" AS ACCNO"+
                    " , 0 AS DEBET" +
                    " , SUM(SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT] + ") AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +
                    " INNER JOIN " + PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL + " AS SD" +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID] +
                    " = SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID] +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + PstSpecialJournalMain.TIPE_SPECIAL_JOURNAL_CASH +
                    " AND ACC."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " IN(" + PstPerkiraan.ACC_GROUP_REVENUE +
                    " ," + PstPerkiraan.ACC_GROUP_OTHER_REVENUE+ ")" +
                    " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]; 
                
                    //System.out.println("SQL getQuerySpecialJournalRevenue ==> "+sql);
            }catch(Exception e){
                System.out.println("Exception on getQuerySpecialJournalRevenue :::: "+e.toString());
            }
        return sql;
    }
    
    /**
     * simpan account balance yang diambil dari SA ke table temporary
     * @param lSelectedPeriodOid
     * @return
     */
    private int insertSAAccountBalanceToBuffer(long lSelectedPeriodOid) {
        int iResult = 0;
        try {
            String sql = "INSERT INTO " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR_BUFFER +
                    "(periode_id,id_perkiraan,tanda_debet_kredit,debet,kredit)" +
                    " SELECT " +
                    " SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] +
                    " ,ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    " , SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET] +
                    " , SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT] +
                    " FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR + " AS SA" +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] +
                    " = " + lSelectedPeriodOid;

            //System.out.println("com.dimata.aiso.session.jurnal.insertSAAccountBalanceToBuffer sql : " + sql);
            iResult = DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println("Exc when try to insert SA account balance to table buffer : " + e.toString());
        }

        return iResult;
    }


    /**
     * simpan account balance yang diambil dari Jurnal ke table temporary
     * @param lSelectedPeriodOid
     * @return
     */
    private int insertJUAccountBalanceToBuffer(long lSelectedPeriodOid) {
        int iResult = 0;
        try {
            String sql = "INSERT INTO " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR_BUFFER +
                    "(periode_id,id_perkiraan,tanda_debet_kredit,debet,kredit)" +
                    " SELECT " +
                    " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID] +
                    " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    " , SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET] + ") AS DEBET" +
                    " , SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT] + ") AS KREDIT" +
                    " FROM " + PstJurnalUmum.TBL_JURNAL_UMUM + " AS JU" +
                    " INNER JOIN " + PstJurnalDetail.TBL_JURNAL_DETAIL + " AS JD" +
                    " ON JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                    " = JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID] +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID] +
                    " = " + lSelectedPeriodOid +
                    " AND (JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE] +
                    " = " + PstJurnalUmum.TIPE_JURNAL_UMUM +
                    " OR JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE] +
                    " = " + PstJurnalUmum.TIPE_JURNAL_PENUTUP_1 +
                    " OR JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE] +
                    " = " + PstJurnalUmum.TIPE_JURNAL_PENUTUP_2 +
                    " OR JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE] +
                    " = " + PstJurnalUmum.TIPE_JURNAL_PENUTUP_3 + ")" +
                    " AND (ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +
                    " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_FIXED_ASSETS +
                    " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_OTHER_ASSETS +
                    " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_CURRENCT_LIABILITIES +
                    " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_EQUITY +
                    " OR ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LONG_TERM_LIABILITIES + ")" +
                    " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    " , JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID] +
                    " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                    " ORDER BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];

            //System.out.println("com.dimata.aiso.session.jurnal.insertJUAccountBalanceToBuffer sql : " + sql);
            iResult = DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println("Exc when try to insert JU account balance to table buffer : " + e.toString());
        }

        return iResult;
    }
    

    private int insertCashIn(long lSelectedPeriodOid){
        int result = 0;
        String sql = "";
            try{
                sql = querySelect()+
                    " , SUM(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] + ") AS DEBET" +
                    " , 0 AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_CASH +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +queryGroupBy();
                
                    //System.out.println("SQL insertCashIn() ::: "+sql);
                    result = DBHandler.execUpdate(sql);
            }catch(Exception e){
                System.out.println("Exception on insertCashIn() ::: "+e.toString());
            }
        return result;
    }
    
     private int insertCashOut(long lSelectedPeriodOid){
        int result = 0;
        String sql = "";
            try{
                sql = querySelect()+
                    " , 0 AS DEBET" +
                    " , SUM(SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT] + ") AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +                    
                    " INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+
                    " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " = SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_BANK +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT_STATUS] +
                    " = "+PstSpecialJournalMain.STS_DEBET+
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +
                    queryGroupBy();
                
                    //System.out.println("SQL insertCashOut() ::: "+sql);
                    result = DBHandler.execUpdate(sql);
            }catch(Exception e){
                System.out.println("Exception on insertCashOut() ::: "+e.toString());
            }
        return result;
    }
     
     private int insertPettyCashIn(long lSelectedPeriodOid){
        int result = 0;
        String sql = "";
            try{
                sql = querySelect()+
                    " , SUM(SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT] + ") AS DEBET" +
                    " , 0 AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +
                    " INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+
                    " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " = SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_REPLACEMENT +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +
                    queryGroupBy();
                
                    //System.out.println("SQL insertPettyCashIn() ::: "+sql);
                    result = DBHandler.execUpdate(sql);
            }catch(Exception e){
                System.out.println("Exception on insertPettyCashIn() ::: "+e.toString());
            }
        return result;
    }
     
      private int insertPettyCashOut(long lSelectedPeriodOid){
        int result = 0;
        String sql = "";
            try{
                sql = querySelect()+
                    " , 0 AS DEBET" +
                    " , SUM(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] + ") AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +                    
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_PETTY_CASH +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +
                    queryGroupBy();
                
                    //System.out.println("SQL insertPettyCashOut() ::: "+sql);
                    result = DBHandler.execUpdate(sql);
            }catch(Exception e){
                System.out.println("Exception on insertPettyCashOut() ::: "+e.toString());
            }
        return result;
    }
      
      private int insertBankDeposite(long lSelectedPeriodOid){
        int result = 0;
        String sql = "";
            try{
                sql = querySelect()+
                    " , SUM(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] + ") AS DEBET" +
                    " , 0 AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +                    
                     " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_BANK +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT_STATUS] +
                    " = "+PstSpecialJournalMain.STS_DEBET+
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +
                    queryGroupBy();
                
                    //System.out.println("SQL insertBankDeposite() ::: "+sql);
                    result = DBHandler.execUpdate(sql);
            }catch(Exception e){
                System.out.println("Exception on insertBankDeposite() ::: "+e.toString());
            }
        return result;
    }
      
       private int insertBankReceiveFund(long lSelectedPeriodOid){
        int result = 0;
        String sql = "";
            try{
                sql = querySelect()+
                    " , SUM(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] + ") AS DEBET" +
                    " , 0 AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +                    
                     " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_FUND +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +
                    queryGroupBy();
                
                    //System.out.println("SQL insertBankReceiveFund() ::: "+sql);
                    result = DBHandler.execUpdate(sql);
            }catch(Exception e){
                System.out.println("Exception on insertBankReceiveFund() ::: "+e.toString());
            }
        return result;
    }
       
   private int insertBankTransferIn(long lSelectedPeriodOid){
        int result = 0;
        String sql = "";
            try{
                sql = querySelect()+
                    " , SUM(SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT] + ") AS DEBET" +
                    " , 0 AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +                    
                    " INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+
                    " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " = SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_BANK_TRANSFER +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +
                    queryGroupBy();
                
                    //System.out.println("SQL insertBankTransferIn() ::: "+sql);
                    result = DBHandler.execUpdate(sql);
            }catch(Exception e){
                System.out.println("Exception on insertBankTransferIn() ::: "+e.toString());
            }
        return result;
    }
   
    private int insertBankTransferOut(long lSelectedPeriodOid){
        int result = 0;
        String sql = "";
            try{
                sql = querySelect()+
                    " , 0 AS DEBET" +
                    " , SUM(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] + ") AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +                    
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_BANK_TRANSFER +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +
                    queryGroupBy();
                
                    //System.out.println("SQL insertBankTransferOut() ::: "+sql);
                    result = DBHandler.execUpdate(sql);
            }catch(Exception e){
                System.out.println("Exception on insertBankTransferOut() ::: "+e.toString());
            }
        return result;
    }
    
     private int insertChequeRequest(long lSelectedPeriodOid){
        int result = 0;
        String sql = "";
            try{
                sql = querySelect()+
                    " , 0 AS DEBET" +
                    " , SUM(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] + ") AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +                    
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_BANK +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT_STATUS] +
                    " = "+PstSpecialJournalMain.STS_KREDIT+
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +
                    queryGroupBy();
                
                    //System.out.println("SQL insertChequeRequest() ::: "+sql);
                    result = DBHandler.execUpdate(sql);
            }catch(Exception e){
                System.out.println("Exception on insertChequeRequest() ::: "+e.toString());
            }
        return result;
    }
     
      private int insertBankPayment(long lSelectedPeriodOid){
        int result = 0;
        String sql = "";
            try{
                sql = querySelect()+
                    " , 0 AS DEBET" +
                    " , SUM(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] + ") AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +                    
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_PAYMENT +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_LIQUID_ASSETS +
                    queryGroupBy();
                
                    //System.out.println("SQL insertBankPayment() ::: "+sql);
                    result = DBHandler.execUpdate(sql);
            }catch(Exception e){
                System.out.println("Exception on insertBankPayment() ::: "+e.toString());
            }
        return result;
    }
      
      private int insertFunding(long lSelectedPeriodOid){
        int result = 0;
        String sql = "";
            try{
                sql = querySelect()+
                    " , 0 AS DEBET" +
                    " , SUM(SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT] + ") AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +                    
                    " INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+
                    " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " = SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_FUND +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_EQUITY + 
                    queryGroupBy();
                
                    //System.out.println("SQL insertFunding() ::: "+sql);
                    result = DBHandler.execUpdate(sql);
            }catch(Exception e){
                System.out.println("Exception on insertFunding() ::: "+e.toString());
            }
        return result;
    }
      
      private int insertNonCash(long lSelectedPeriodOid){
        int result = 0;
        String sql = "";
            try{
                sql = querySelect()+
                    " , 0 AS DEBET" +
                    " , SUM(JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] + ") AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +                    
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_NON_CASH +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_CURRENCT_LIABILITIES + 
                    queryGroupBy();
                
                    //System.out.println("SQL insertNonCash() ::: "+sql);
                    result = DBHandler.execUpdate(sql);
            }catch(Exception e){
                System.out.println("Exception on insertNonCash() ::: "+e.toString());
            }
        return result;
    }
      
       private int insertPayment(long lSelectedPeriodOid){
        int result = 0;
        String sql = "";
            try{
                sql = querySelect()+
                    " , SUM(SD." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT] + ") AS DEBET" +
                    " , 0 AS KREDIT" +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN + " AS JM" +                    
                     " INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+
                    " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                    " = SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON SD." + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + lSelectedPeriodOid +
                    " AND JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE] +
                    " = " + I_JournalType.TIPE_SPECIAL_JOURNAL_PAYMENT +
                    " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    " = " + PstPerkiraan.ACC_GROUP_CURRENCT_LIABILITIES + 
                    queryGroupBy();
                
                    //System.out.println("SQL insertPayment() ::: "+sql);
                    result = DBHandler.execUpdate(sql);
            }catch(Exception e){
                System.out.println("Exception on insertPayment() ::: "+e.toString());
            }
        return result;
    }
     
     private String querySelect(){
        String sql = "";
            try{
                
                sql = " INSERT INTO " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR_BUFFER +
                    "(periode_id,id_perkiraan,tanda_debet_kredit,debet,kredit)" +
                    " SELECT " +
                    " JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] + 
                    " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] + 
                    " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT];
                
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on querySelect() ::: "+e.toString());
            }
        return sql;
     }
     
     private String queryGroupBy(){
        String sql = "";
        try{
            
                sql = " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    " , JM." + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                    " ORDER BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];   
                
        }catch(Exception e){
            sql = "";
            System.out.println("Exception on queryGroupBy() ::: "+e.toString());
        }
        return sql;
     }
     
    /**
     * hapus account balance yang di ada table temporary
     * @param lSelectedPeriodOid
     * @return
     */
    private int deleteAccountBalanceFromBuffer(long lSelectedPeriodOid) {
        int iResult = 0;
        try {
            String sql = "DELETE FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR_BUFFER +
                    " WHERE " + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] +
                    " = " + lSelectedPeriodOid;

            //System.out.println("com.dimata.aiso.session.jurnal.deleteAccountBalanceFromBuffer sql : " + sql);
            iResult = DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println("Exc when try to delete account balance from table buffer : " + e.toString());
        }

        return iResult;
    }

    private int insertDataSpecialJournal(long lSelectedPeriodOid){
        int iResult = 0;
            try{
                iResult = insertCashIn(lSelectedPeriodOid);
                iResult = insertCashOut(lSelectedPeriodOid);
                iResult = insertPettyCashIn(lSelectedPeriodOid);
                iResult = insertPettyCashOut(lSelectedPeriodOid);
                iResult = insertBankDeposite(lSelectedPeriodOid);
                iResult = insertBankTransferIn(lSelectedPeriodOid);
                iResult = insertBankReceiveFund(lSelectedPeriodOid);
                iResult = insertBankPayment(lSelectedPeriodOid);
                iResult = insertChequeRequest(lSelectedPeriodOid);
                iResult = insertBankTransferOut(lSelectedPeriodOid);
                iResult = insertFunding(lSelectedPeriodOid);
                iResult = insertPayment(lSelectedPeriodOid);
                iResult = insertNonCash(lSelectedPeriodOid);
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on insertDataSpecialJournal() ::: "+e.toString());
            }
        return iResult;
    }

    /**
     * mengambil vector of object AccountBalance utk semua account
     * @param lSelectedPeriodOid
     * @return
     */
    private Vector getVectOfObjAccountBalance(long lSelectedPeriodOid) {
        Vector vResult = new Vector(1, 1);

        // delete account balance from table buffer
        int iAccountBalance = deleteAccountBalanceFromBuffer(lSelectedPeriodOid);

        // insert SA balance to table buffer
        int iSAAccountBalance = insertSAAccountBalanceToBuffer(lSelectedPeriodOid);

        // insert SA balance to table buffer
        int iJUAccountBalance = insertJUAccountBalanceToBuffer(lSelectedPeriodOid);
        
        int iInsertDataSpecialJournal = insertDataSpecialJournal(lSelectedPeriodOid);

        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " +
                    " SA.id_perkiraan,SA.tanda_debet_kredit,SUM(SA.debet) AS DEBET ,SUM(SA.kredit) AS KREDIT" +                    
                    " FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR_BUFFER + " AS SA" +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] +
                    " = " + lSelectedPeriodOid +
                    " GROUP BY SA.id_perkiraan " + 
                    " , SA.tanda_debet_kredit" + 
                    " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                    " ORDER BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];

            //System.out.println(" sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                AccountBalance objAccountBalance = new AccountBalance();
                objAccountBalance.setLPerkiraanOid(rs.getLong(PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]));
                objAccountBalance.setINormalSign(rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]));

                // get saldo awal
                SaldoAkhirPeriode saldoAkhirPeriode = getSaldoPrevPeriodSelected(objAccountBalance.getLPerkiraanOid(), lSelectedPeriodOid);
                objAccountBalance.setDDebet(rs.getDouble("DEBET") + saldoAkhirPeriode.getDebet());
                objAccountBalance.setDCredit(rs.getDouble("KREDIT") + saldoAkhirPeriode.getKredit());
                vResult.add(objAccountBalance);
            }
            // delete account balance from table buffer
            iAccountBalance = deleteAccountBalanceFromBuffer(lSelectedPeriodOid);
        } catch (Exception e) {
            System.out.println("");
        } finally {
            DBResultSet.close(dbrs);
        }

        return vResult;
    }

    /**
     * Generate jurnal closing periode yaitu menutup account2 Laba(Rugi) dan saldonya dibawa ke account Laba(Rugi) Periode Berjalan
     * @param lSelectedPeriodOid
     * @param lBookTypeOid
     * @return saldo account Laba(Rugi) Periode Berjalan
     */
    public double generateJournalOfClosingPeriod1(long lSelectedPeriodOid, long lBookTypeOid, long lUserOid) {
        double dResult = 0;

        Periode objPeriod = new Periode();
        try {
            objPeriod = PstPeriode.fetchExc(lSelectedPeriodOid);
        } catch (Exception e) {
            System.out.println("Exc when fetch period  : " + e.toString());
        }

        if (objPeriod.getOID() != 0) {
            // --- start iterasi sebanyak department, karena jurnal akan dilakukan per department            
            Vector vDepartment = new Vector(1,1);
            
            vDepartment = PstDepartment.list(0, 0, "", "");            
            if (vDepartment != null && vDepartment.size() > 0) {
                int iDepartmentCount = vDepartment.size();
                for (int j = 0; j < iDepartmentCount; j++) {
                    Department objDepartment = (Department) vDepartment.get(j);

                    // --- start proses jurnal ---
                    Vector vPLAccountBalance = getVectOfObjPLAccountBalance(objDepartment.getOID(), lSelectedPeriodOid);
                    if (vPLAccountBalance != null && vPLAccountBalance.size() > 0) {
                        // --- start membuat jurnal umum ---
                        long lJurnalUmumOid = 0;
                        try {
                            //String stringVoucher = SessJurnal.generateVoucherNumber(lSelectedPeriodOid, objPeriod.getTglAkhir());
                            JurnalUmum objJurnalUmumNew = new JurnalUmum();
                            objJurnalUmumNew.setUserId(lUserOid);
                            //objJurnalUmumNew.setSJurnalNumber(stringVoucher);
                            //objJurnalUmumNew.setVoucherNo(stringVoucher.substring(0, 4));
                            //objJurnalUmumNew.setVoucherCounter(Integer.parseInt(stringVoucher.substring(5)));
                            objJurnalUmumNew.setTglTransaksi(objPeriod.getTglAkhir());
                            objJurnalUmumNew.setTglEntry(objPeriod.getTglAkhir());
                            objJurnalUmumNew.setPeriodeId(lSelectedPeriodOid);
                            objJurnalUmumNew.setBookType(lBookTypeOid);
                            objJurnalUmumNew.setCurrType(lBookTypeOid);
                            objJurnalUmumNew.setReferenceDoc("-");
                            objJurnalUmumNew.setKeterangan("Closing Period Journal Step 1");
                            objJurnalUmumNew.setJurnalType(PstJurnalUmum.TIPE_JURNAL_PENUTUP_1);
                            lJurnalUmumOid = PstJurnalUmum.insertExcGenerateVoucher(objJurnalUmumNew);
                        } catch (Exception e) {
                            System.out.println("Exc when try to generate jurnal umum : " + e.toString());
                        }
                        // --- end membuat jurnal umum ---


                        // --- start membuat jurnal detail ---
                        if (lJurnalUmumOid != 0) {
                            double dDebetAmount = 0;
                            double dCreditAmount = 0;
                            Vector vJurnalDetail = new Vector(1, 1);
                            int iVectCount = vPLAccountBalance.size();
                            for (int i = 0; i < iVectCount; i++) {
                                AccountBalance objAccountBalance = (AccountBalance) vPLAccountBalance.get(i);

                                try {
                                    long lJurnalDetailOid = 0;
                                    double dDetailAmount = 0;
                                    if (objAccountBalance.getINormalSign() == PstPerkiraan.ACC_DEBETSIGN) {
                                        dDetailAmount = objAccountBalance.getDDebet() - objAccountBalance.getDCredit();

                                        if (dDetailAmount > 0) {
                                            JurnalDetail objJurnalDetailCredit = new JurnalDetail();
                                            objJurnalDetailCredit.setJurnalIndex(lJurnalUmumOid);
                                            objJurnalDetailCredit.setIdPerkiraan(objAccountBalance.getLPerkiraanOid());
                                            objJurnalDetailCredit.setCurrType(lBookTypeOid);
                                            objJurnalDetailCredit.setCurrAmount(1);
                                            objJurnalDetailCredit.setDebet(0);
                                            objJurnalDetailCredit.setKredit(dDetailAmount);
                                            lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailCredit);

                                            dCreditAmount = dCreditAmount + dDetailAmount;

                                        } else {
                                            JurnalDetail objJurnalDetailDebet = new JurnalDetail();
                                            objJurnalDetailDebet.setJurnalIndex(lJurnalUmumOid);
                                            objJurnalDetailDebet.setIdPerkiraan(objAccountBalance.getLPerkiraanOid());
                                            objJurnalDetailDebet.setCurrType(lBookTypeOid);
                                            objJurnalDetailDebet.setCurrAmount(1);
                                            objJurnalDetailDebet.setDebet(dDetailAmount);
                                            objJurnalDetailDebet.setKredit(0);
                                            lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailDebet);

                                            dDebetAmount = dDebetAmount + dDetailAmount;
                                        }
                                    } else {
                                        dDetailAmount = objAccountBalance.getDCredit() - objAccountBalance.getDDebet();

                                        if (dDetailAmount > 0) {
                                            JurnalDetail objJurnalDetailDebet = new JurnalDetail();
                                            objJurnalDetailDebet.setJurnalIndex(lJurnalUmumOid);
                                            objJurnalDetailDebet.setIdPerkiraan(objAccountBalance.getLPerkiraanOid());
                                            objJurnalDetailDebet.setCurrType(lBookTypeOid);
                                            objJurnalDetailDebet.setCurrAmount(1);
                                            objJurnalDetailDebet.setDebet(dDetailAmount);
                                            objJurnalDetailDebet.setKredit(0);
                                            lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailDebet);

                                            dDebetAmount = dDebetAmount + dDetailAmount;
                                        } else {
                                            JurnalDetail objJurnalDetailCredit = new JurnalDetail();
                                            objJurnalDetailCredit.setJurnalIndex(lJurnalUmumOid);
                                            objJurnalDetailCredit.setIdPerkiraan(objAccountBalance.getLPerkiraanOid());
                                            objJurnalDetailCredit.setCurrType(lBookTypeOid);
                                            objJurnalDetailCredit.setCurrAmount(1);
                                            objJurnalDetailCredit.setDebet(0);
                                            objJurnalDetailCredit.setKredit(dDetailAmount);
                                            lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailCredit);

                                            dCreditAmount = dCreditAmount + dDetailAmount;
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("Exc when generate jurnal : " + e.toString());
                                }
                            }


                            // --- start mencari dan membuat jurnal Laba(Rugi) Periode Berjalan ---
                            AccountLink objAccountLink = PstAccountLink.getObjAccountLink(objDepartment.getOID(), PstPerkiraan.ACC_GROUP_PERIOD_EARNING);
                            long lAccLinkPeriodEarningOid = objAccountLink.getFirstId();
                            double dPeriodEarningAmount = dDebetAmount - dCreditAmount; // pendapatan dikurangi biaya
                            dResult = dPeriodEarningAmount;
                            if (dPeriodEarningAmount > 0) {
                                try {
                                    JurnalDetail objJurnalDetailCredit = new JurnalDetail();
                                    objJurnalDetailCredit.setJurnalIndex(lJurnalUmumOid);
                                    objJurnalDetailCredit.setIdPerkiraan(lAccLinkPeriodEarningOid);
                                    objJurnalDetailCredit.setCurrType(lBookTypeOid);
                                    objJurnalDetailCredit.setCurrAmount(1);
                                    objJurnalDetailCredit.setDebet(0);
                                    objJurnalDetailCredit.setKredit(dPeriodEarningAmount);
                                    long lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailCredit);
                                    dResult = dPeriodEarningAmount;
                                } catch (Exception e) {
                                    System.out.println("Exc when try to insert jurnal period earning credit : " + e.toString());
                                }
                            } else {
                                try {
                                    JurnalDetail objJurnalDetailDebet = new JurnalDetail();
                                    objJurnalDetailDebet.setJurnalIndex(lJurnalUmumOid);
                                    objJurnalDetailDebet.setIdPerkiraan(lAccLinkPeriodEarningOid);
                                    objJurnalDetailDebet.setCurrType(lBookTypeOid);
                                    objJurnalDetailDebet.setCurrAmount(1);
                                    dPeriodEarningAmount = (-1) * dPeriodEarningAmount;
                                    objJurnalDetailDebet.setDebet(dPeriodEarningAmount);
                                    objJurnalDetailDebet.setKredit(0);
                                    long lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailDebet);

                                } catch (Exception e) {
                                    System.out.println("Exc when try to insert jurnal period earning debet : " + e.toString());
                                }
                            }
                            // --- end mencari dan membuat jurnal Laba(Rugi) Periode Berjalan ---

                        }
                        // --- end membuat jurnal detail ---

                    }
                    // --- end proses jurnal ---
                }
            }
            // --- end iterasi sebanyak department, karena jurnal akan dilakukan per department
        }

        return dResult;
    }
    
     public double generateJournalOfClosingPrd1(long lSelectedPeriodOid, long lBookTypeOid, long lUserOid) {
        double dResult = 0;

        Periode objPeriod = new Periode();
        try {
            objPeriod = PstPeriode.fetchExc(lSelectedPeriodOid);
        } catch (Exception e) {
            System.out.println("Exc when fetch period  : " + e.toString());
        }
        
        if (objPeriod.getOID() != 0) {
                    // --- start proses jurnal ---
                    Vector vPLAccountBalance = getVectOfObjPLAccountBalance(0, lSelectedPeriodOid);
                    if (vPLAccountBalance != null && vPLAccountBalance.size() > 0) {
                        // --- start membuat jurnal umum ---
                        long lJurnalUmumOid = 0;
                        try {
                            //String stringVoucher = SessJurnal.generateVoucherNumber(lSelectedPeriodOid, objPeriod.getTglAkhir());
                            JurnalUmum objJurnalUmumNew = new JurnalUmum();
                            objJurnalUmumNew.setUserId(lUserOid);
                            //objJurnalUmumNew.setSJurnalNumber(stringVoucher);
                            //objJurnalUmumNew.setVoucherNo(stringVoucher.substring(0, 4));
                            //objJurnalUmumNew.setVoucherCounter(Integer.parseInt(stringVoucher.substring(5)));
                            objJurnalUmumNew.setTglTransaksi(objPeriod.getTglAkhir());
                            objJurnalUmumNew.setTglEntry(objPeriod.getTglAkhir());
                            objJurnalUmumNew.setPeriodeId(lSelectedPeriodOid);
                            objJurnalUmumNew.setBookType(lBookTypeOid);
                            objJurnalUmumNew.setCurrType(lBookTypeOid);
                            objJurnalUmumNew.setReferenceDoc("-");
                            objJurnalUmumNew.setKeterangan("Closing Period Journal Step 1");
                            objJurnalUmumNew.setJurnalType(PstJurnalUmum.TIPE_JURNAL_PENUTUP_1);
                            lJurnalUmumOid = PstJurnalUmum.insertExcGenerateVoucher(objJurnalUmumNew);
                        } catch (Exception e) {
                            System.out.println("Exc when try to generate jurnal umum : " + e.toString());
                        }
                        // --- end membuat jurnal umum ---


                        // --- start membuat jurnal detail ---
                        if (lJurnalUmumOid != 0) {
                            double dDebetAmount = 0;
                            double dCreditAmount = 0;
                            Vector vJurnalDetail = new Vector(1, 1);
                            int iVectCount = vPLAccountBalance.size();
                            for (int i = 0; i < iVectCount; i++) {
                                AccountBalance objAccountBalance = (AccountBalance) vPLAccountBalance.get(i);

                                try {
                                    long lJurnalDetailOid = 0;
                                    double dDetailAmount = 0;
                                    if (objAccountBalance.getINormalSign() == PstPerkiraan.ACC_DEBETSIGN) {
                                        dDetailAmount = objAccountBalance.getDDebet() - objAccountBalance.getDCredit();

                                        if (dDetailAmount > 0) {
                                            JurnalDetail objJurnalDetailCredit = new JurnalDetail();
                                            objJurnalDetailCredit.setJurnalIndex(lJurnalUmumOid);
                                            objJurnalDetailCredit.setIdPerkiraan(objAccountBalance.getLPerkiraanOid());
                                            objJurnalDetailCredit.setCurrType(lBookTypeOid);
                                            objJurnalDetailCredit.setCurrAmount(1);
                                            objJurnalDetailCredit.setDebet(0);
                                            objJurnalDetailCredit.setKredit(dDetailAmount);
                                            lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailCredit);

                                            dCreditAmount = dCreditAmount + dDetailAmount;

                                        } else {
                                            JurnalDetail objJurnalDetailDebet = new JurnalDetail();
                                            objJurnalDetailDebet.setJurnalIndex(lJurnalUmumOid);
                                            objJurnalDetailDebet.setIdPerkiraan(objAccountBalance.getLPerkiraanOid());
                                            objJurnalDetailDebet.setCurrType(lBookTypeOid);
                                            objJurnalDetailDebet.setCurrAmount(1);
                                            objJurnalDetailDebet.setDebet(dDetailAmount);
                                            objJurnalDetailDebet.setKredit(0);
                                            lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailDebet);

                                            dDebetAmount = dDebetAmount + dDetailAmount;
                                        }
                                    } else {
                                        dDetailAmount = objAccountBalance.getDCredit() - objAccountBalance.getDDebet();

                                        if (dDetailAmount > 0) {
                                            JurnalDetail objJurnalDetailDebet = new JurnalDetail();
                                            objJurnalDetailDebet.setJurnalIndex(lJurnalUmumOid);
                                            objJurnalDetailDebet.setIdPerkiraan(objAccountBalance.getLPerkiraanOid());
                                            objJurnalDetailDebet.setCurrType(lBookTypeOid);
                                            objJurnalDetailDebet.setCurrAmount(1);
                                            objJurnalDetailDebet.setDebet(dDetailAmount);
                                            objJurnalDetailDebet.setKredit(0);
                                            lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailDebet);

                                            dDebetAmount = dDebetAmount + dDetailAmount;
                                        } else {
                                            JurnalDetail objJurnalDetailCredit = new JurnalDetail();
                                            objJurnalDetailCredit.setJurnalIndex(lJurnalUmumOid);
                                            objJurnalDetailCredit.setIdPerkiraan(objAccountBalance.getLPerkiraanOid());
                                            objJurnalDetailCredit.setCurrType(lBookTypeOid);
                                            objJurnalDetailCredit.setCurrAmount(1);
                                            objJurnalDetailCredit.setDebet(0);
                                            objJurnalDetailCredit.setKredit(dDetailAmount);
                                            lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailCredit);

                                            dCreditAmount = dCreditAmount + dDetailAmount;
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("Exc when generate jurnal : " + e.toString());
                                }
                            }


                            // --- start mencari dan membuat jurnal Laba(Rugi) Periode Berjalan ---
                            AccountLink objAccountLink = PstAccountLink.getObjAccountLink(0, PstPerkiraan.ACC_GROUP_PERIOD_EARNING);
                            long lAccLinkPeriodEarningOid = objAccountLink.getFirstId();
                            double dPeriodEarningAmount = dDebetAmount - dCreditAmount; // pendapatan dikurangi biaya
                            dResult = dPeriodEarningAmount;
                            if (dPeriodEarningAmount > 0) {
                                try {
                                    JurnalDetail objJurnalDetailCredit = new JurnalDetail();
                                    objJurnalDetailCredit.setJurnalIndex(lJurnalUmumOid);
                                    objJurnalDetailCredit.setIdPerkiraan(lAccLinkPeriodEarningOid);
                                    objJurnalDetailCredit.setCurrType(lBookTypeOid);
                                    objJurnalDetailCredit.setCurrAmount(1);
                                    objJurnalDetailCredit.setDebet(0);
                                    objJurnalDetailCredit.setKredit(dPeriodEarningAmount);
                                    long lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailCredit);
                                    dResult = dPeriodEarningAmount;
                                } catch (Exception e) {
                                    System.out.println("Exc when try to insert jurnal period earning credit : " + e.toString());
                                }
                            } else {
                                try {
                                    JurnalDetail objJurnalDetailDebet = new JurnalDetail();
                                    objJurnalDetailDebet.setJurnalIndex(lJurnalUmumOid);
                                    objJurnalDetailDebet.setIdPerkiraan(lAccLinkPeriodEarningOid);
                                    objJurnalDetailDebet.setCurrType(lBookTypeOid);
                                    objJurnalDetailDebet.setCurrAmount(1);
                                    dPeriodEarningAmount = (-1) * dPeriodEarningAmount;
                                    objJurnalDetailDebet.setDebet(dPeriodEarningAmount);
                                    objJurnalDetailDebet.setKredit(0);
                                    long lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailDebet);

                                } catch (Exception e) {
                                    System.out.println("Exc when try to insert jurnal period earning debet : " + e.toString());
                                }
                            }
                            // --- end mencari dan membuat jurnal Laba(Rugi) Periode Berjalan ---

                        }
                        // --- end membuat jurnal detail ---

                    }
                    // --- end proses jurnal ---
                
        }

        return dResult;
    }


    /**
     * Generate jurnal closing periode yaitu menutup account Laba(Rugi) Periode Berjalan dan saldonya dibawa ke account Laba(Rugi) Tahun Berjalan
     * @param lSelectedPeriodOid
     * @param lBookTypeOid
     * @return saldo account Laba(Rugi) Tahun Berjalan
     */
    public double generateJournalOfClosingPeriod2(long lSelectedPeriodOid, long lBookTypeOid, long lUserOid, double dAccPeriodEarningBalance) {
        double dResult = 0;

        Periode objPeriod = new Periode();
        try {
            objPeriod = PstPeriode.fetchExc(lSelectedPeriodOid);
        } catch (Exception e) {
            System.out.println("Exc when fetch period  : " + e.toString());
        }

        if (objPeriod.getOID() != 0) {
            // --- start iterasi sebanyak department, karena jurnal akan dilakukan per department
            Vector vDepartment = PstDepartment.list(0, 0, "", "");
            if (vDepartment != null && vDepartment.size() > 0) {
                int iDepartmentCount = vDepartment.size();
                for (int j = 0; j < iDepartmentCount; j++) {
                    Department objDepartment = (Department) vDepartment.get(j);

                    // --- start proses jurnal ---
                    Vector vPLAccountBalance = getVectOfObjPLAccountBalance(objDepartment.getOID(), lSelectedPeriodOid);
                    if (vPLAccountBalance != null && vPLAccountBalance.size() > 0) {
                        // --- start membuat jurnal umum ---
                        long lJurnalUmumOid = 0;
                        try {
                            //String stringVoucher = SessJurnal.generateVoucherNumber(lSelectedPeriodOid, objPeriod.getTglAkhir());
                            JurnalUmum objJurnalUmumNew = new JurnalUmum();
                            objJurnalUmumNew.setUserId(lUserOid);
                            //objJurnalUmumNew.setSJurnalNumber(stringVoucher);
                            //objJurnalUmumNew.setVoucherNo(stringVoucher.substring(0, 4));
                            //objJurnalUmumNew.setVoucherCounter(Integer.parseInt(stringVoucher.substring(5)));
                            objJurnalUmumNew.setTglTransaksi(objPeriod.getTglAkhir());
                            objJurnalUmumNew.setTglEntry(objPeriod.getTglAkhir());
                            objJurnalUmumNew.setPeriodeId(lSelectedPeriodOid);
                            objJurnalUmumNew.setBookType(lBookTypeOid);
                            objJurnalUmumNew.setCurrType(lBookTypeOid);
                            objJurnalUmumNew.setReferenceDoc("-");
                            objJurnalUmumNew.setKeterangan("Closing Period Journal Step 2");
                            objJurnalUmumNew.setJurnalType(PstJurnalUmum.TIPE_JURNAL_PENUTUP_2);
                            lJurnalUmumOid = PstJurnalUmum.insertExcGenerateVoucher(objJurnalUmumNew);
                        } catch (Exception e) {
                            System.out.println("Exc when try to generate jurnal umum : " + e.toString());
                        }
                        // --- end membuat jurnal umum ---


                        // --- start membuat jurnal detail ---
                        if (lJurnalUmumOid != 0) {
                            // --- start mencari dan membuat jurnal Laba(Rugi) Periode Berjalan ---
                            AccountLink objAccountLinkPeriodEarn = PstAccountLink.getObjAccountLink(objDepartment.getOID(), PstPerkiraan.ACC_GROUP_PERIOD_EARNING);
                            long lAccLinkPeriodEarningOid = objAccountLinkPeriodEarn.getFirstId();

                            // --- start mencari dan membuat jurnal Laba(Rugi) Tahun Berjalan ---
                            AccountLink objAccountLinkYearEarn = PstAccountLink.getObjAccountLink(objDepartment.getOID(), PstPerkiraan.ACC_GROUP_YEAR_EARNING);
                            long lAccLinkYearEarningOid = objAccountLinkYearEarn.getFirstId();

                            if (dAccPeriodEarningBalance > 0) {
                                try {
                                    JurnalDetail objJurnalDetailDebet = new JurnalDetail();
                                    objJurnalDetailDebet.setJurnalIndex(lJurnalUmumOid);
                                    objJurnalDetailDebet.setIdPerkiraan(lAccLinkPeriodEarningOid);
                                    objJurnalDetailDebet.setCurrType(lBookTypeOid);
                                    objJurnalDetailDebet.setCurrAmount(1);
                                    objJurnalDetailDebet.setDebet(dAccPeriodEarningBalance);
                                    objJurnalDetailDebet.setKredit(0);
                                    long lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailDebet);

                                    JurnalDetail objJurnalDetailCredit = new JurnalDetail();
                                    objJurnalDetailCredit.setJurnalIndex(lJurnalUmumOid);
                                    objJurnalDetailCredit.setIdPerkiraan(lAccLinkYearEarningOid);
                                    objJurnalDetailCredit.setCurrType(lBookTypeOid);
                                    objJurnalDetailCredit.setCurrAmount(1);
                                    objJurnalDetailCredit.setDebet(0);
                                    objJurnalDetailCredit.setKredit(dAccPeriodEarningBalance);
                                    lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailCredit);

                                    dResult = dAccPeriodEarningBalance;
                                } catch (Exception e) {
                                    System.out.println("Exc when try to insert jurnal closing period 2 position debet : " + e.toString());
                                }
                            } else {
                                try {
                                    JurnalDetail objJurnalDetailDebet = new JurnalDetail();
                                    objJurnalDetailDebet.setJurnalIndex(lJurnalUmumOid);
                                    objJurnalDetailDebet.setIdPerkiraan(lAccLinkYearEarningOid);
                                    objJurnalDetailDebet.setCurrType(lBookTypeOid);
                                    objJurnalDetailDebet.setCurrAmount(1);
                                    objJurnalDetailDebet.setDebet((-1) * dAccPeriodEarningBalance);
                                    objJurnalDetailDebet.setKredit(0);
                                    long lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailDebet);

                                    JurnalDetail objJurnalDetailCredit = new JurnalDetail();
                                    objJurnalDetailCredit.setJurnalIndex(lJurnalUmumOid);
                                    objJurnalDetailCredit.setIdPerkiraan(lAccLinkPeriodEarningOid);
                                    objJurnalDetailCredit.setCurrType(lBookTypeOid);
                                    objJurnalDetailCredit.setCurrAmount(1);
                                    objJurnalDetailCredit.setDebet(0);
                                    objJurnalDetailCredit.setKredit((-1) * dAccPeriodEarningBalance);
                                    lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailCredit);

                                    dResult = dAccPeriodEarningBalance;
                                } catch (Exception e) {
                                    System.out.println("Exc when try to insert jurnal closing period 2 position credit : " + e.toString());
                                }
                            }
                        }
                        // --- end membuat jurnal detail ---
                    }
                    // --- end proses jurnal ---
                }
            }
            // --- end iterasi sebanyak department, karena jurnal akan dilakukan per department
        }

        return dResult;
    }    
   

    // pengecekan total saldo bulanan
    public double getTotalYearEarning(long lSelectedPeriodOid, Department objDepartment) {
        double dResult = 0;
        DBResultSet dbrs = null;
        try {
            AccountLink objAccountLinkPeriodEarn = PstAccountLink.getObjAccountLink(objDepartment.getOID(), PstPerkiraan.ACC_GROUP_YEAR_EARNING);
            Periode objPeriod = PstPeriode.fetchExc(lSelectedPeriodOid);
            String where = PstPeriode.fieldNames[PstPeriode.FLD_TGLAKHIR] + " < '" + Formater.formatDate(objPeriod.getTglAwal(), "yyyy-MM-dd") + "'";
            String order = PstPeriode.fieldNames[PstPeriode.FLD_TGLAKHIR] + " DESC ";
            Vector list = PstPeriode.list(0, 0, where, order);
            Periode perd = new Periode();
            if (list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    perd = (Periode) list.get(k);
                    break;
                }
            }

            String sql = "SELECT SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET] + ", SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT] +
                    " FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR + " AS SP " +
                    " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS P "+
                    " ON P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                    " = SP."+PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN]+
                    " WHERE SP." +PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
                    " = " + objAccountLinkPeriodEarn.getFirstId() +
                    " AND SP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] +
                    " = " + perd.getOID()+
                    " AND P." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_DEPARTMENT_ID] +
                    " = " + objDepartment.getOID();

            System.out.println("===================>>>>>>>>>>>>>>>>>>>>>> : sql "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            double debet = 0;
            double kredit = 0;
            while (rs.next()) {
                debet = rs.getDouble(1);
                kredit = rs.getDouble(2);
            }
            rs.close();

            if(debet!=0){
                dResult = debet * -1;
            }else{
                dResult = kredit;
            }

          
            System.out.println("===>>>>> dResult "+dResult);

        } catch (Exception e) {
            System.out.println("Exc when fetch period  : " + e.toString());
        }
        return dResult;
    }

    /**
     * Generate jurnal closing periode yaitu menutup account Laba(Rugi) Tahun Berjalan dan saldonya dibawa ke account Laba Ditahan
     * @param lSelectedPeriodOid
     * @param lBookTypeOid
     * @return saldo account Laba Ditahan
     */
    public double generateJournalOfClosingPeriod3(long lSelectedPeriodOid, long lBookTypeOid, long lUserOid, double dAccYearEarningBalance) {
        double dResult = 0;

        Periode objPeriod = new Periode();
        try {
            objPeriod = PstPeriode.fetchExc(lSelectedPeriodOid);
        } catch (Exception e) {
            System.out.println("Exc when fetch period  : " + e.toString());
        }

        if (objPeriod.getOID() != 0) {
            // --- start iterasi sebanyak department, karena jurnal akan dilakukan per department
            Vector vDepartment = PstDepartment.list(0, 0, "", "");
            if (vDepartment != null && vDepartment.size() > 0) {
                int iDepartmentCount = vDepartment.size();
                for (int j = 0; j < iDepartmentCount; j++) {
                    Department objDepartment = (Department) vDepartment.get(j);

                    double dAccYearEarningBalance1 = getTotalYearEarning(lSelectedPeriodOid, objDepartment);
                    dAccYearEarningBalance = dAccYearEarningBalance + dAccYearEarningBalance1;

                    System.out.println("Jurnal 3 : "+dAccYearEarningBalance);
                    // --- start proses jurnal ---
                    Vector vPLAccountBalance = getVectOfObjPLAccountBalance(objDepartment.getOID(), lSelectedPeriodOid);
                    if (vPLAccountBalance != null && vPLAccountBalance.size() > 0) {
                        // --- start membuat jurnal umum ---
                        long lJurnalUmumOid = 0;
                        try {
                            //String stringVoucher = SessJurnal.generateVoucherNumber(lSelectedPeriodOid, objPeriod.getTglAkhir());
                            JurnalUmum objJurnalUmumNew = new JurnalUmum();
                            objJurnalUmumNew.setUserId(lUserOid);
                            //objJurnalUmumNew.setSJurnalNumber(stringVoucher);
                            //objJurnalUmumNew.setVoucherNo(stringVoucher.substring(0, 4));
                            //objJurnalUmumNew.setVoucherCounter(Integer.parseInt(stringVoucher.substring(5)));
                            objJurnalUmumNew.setTglTransaksi(objPeriod.getTglAkhir());
                            objJurnalUmumNew.setTglEntry(objPeriod.getTglAkhir());
                            objJurnalUmumNew.setPeriodeId(lSelectedPeriodOid);
                            objJurnalUmumNew.setBookType(lBookTypeOid);
                            objJurnalUmumNew.setCurrType(lBookTypeOid);
                            objJurnalUmumNew.setReferenceDoc("-");
                            objJurnalUmumNew.setKeterangan("Jurnal tutup periode 3");
                            objJurnalUmumNew.setJurnalType(PstJurnalUmum.TIPE_JURNAL_PENUTUP_3);
                            lJurnalUmumOid = PstJurnalUmum.insertExcGenerateVoucher(objJurnalUmumNew);
                        } catch (Exception e) {
                            System.out.println("Exc when try to generate jurnal umum : " + e.toString());
                        }
                        // --- end membuat jurnal umum ---


                        // --- start membuat jurnal detail ---
                        if (lJurnalUmumOid != 0) {
                            // --- start mencari dan membuat jurnal Laba(Rugi) Tahun Berjalan ---
                            AccountLink objAccountLinkYearEarn = PstAccountLink.getObjAccountLink(objDepartment.getOID(), PstPerkiraan.ACC_GROUP_YEAR_EARNING);
                            long lAccLinkYearEarningOid = objAccountLinkYearEarn.getFirstId();

                            // --- start mencari dan membuat jurnal Laba Ditahan ---
                            AccountLink objAccountLinkRetainedEarn = PstAccountLink.getObjAccountLink(objDepartment.getOID(), PstPerkiraan.ACC_GROUP_RETAINED_EARNING);
                            long lAccLinkRetainEarningOid = objAccountLinkRetainedEarn.getFirstId();

                            if (dAccYearEarningBalance > 0) {
                                try {
                                    JurnalDetail objJurnalDetailDebet = new JurnalDetail();
                                    objJurnalDetailDebet.setJurnalIndex(lJurnalUmumOid);
                                    objJurnalDetailDebet.setIdPerkiraan(lAccLinkYearEarningOid);
                                    objJurnalDetailDebet.setCurrType(lBookTypeOid);
                                    objJurnalDetailDebet.setCurrAmount(1);
                                    objJurnalDetailDebet.setDebet(dAccYearEarningBalance);
                                    objJurnalDetailDebet.setKredit(0);
                                    long lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailDebet);

                                    JurnalDetail objJurnalDetailCredit = new JurnalDetail();
                                    objJurnalDetailCredit.setJurnalIndex(lJurnalUmumOid);
                                    objJurnalDetailCredit.setIdPerkiraan(lAccLinkRetainEarningOid);
                                    objJurnalDetailCredit.setCurrType(lBookTypeOid);
                                    objJurnalDetailCredit.setCurrAmount(1);
                                    objJurnalDetailCredit.setDebet(0);
                                    objJurnalDetailCredit.setKredit(dAccYearEarningBalance);
                                    lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailCredit);

                                    dResult = dAccYearEarningBalance;
                                } catch (Exception e) {
                                    System.out.println("Exc when try to insert jurnal closing period 3 position debet : " + e.toString());
                                }
                            } else {
                                try {
                                    JurnalDetail objJurnalDetailDebet = new JurnalDetail();
                                    objJurnalDetailDebet.setJurnalIndex(lJurnalUmumOid);
                                    objJurnalDetailDebet.setIdPerkiraan(lAccLinkRetainEarningOid);
                                    objJurnalDetailDebet.setCurrType(lBookTypeOid);
                                    objJurnalDetailDebet.setCurrAmount(1);
                                    objJurnalDetailDebet.setDebet((-1) * dAccYearEarningBalance);
                                    objJurnalDetailDebet.setKredit(0);
                                    long lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailDebet);

                                    JurnalDetail objJurnalDetailCredit = new JurnalDetail();
                                    objJurnalDetailCredit.setJurnalIndex(lJurnalUmumOid);
                                    objJurnalDetailCredit.setIdPerkiraan(lAccLinkYearEarningOid);
                                    objJurnalDetailCredit.setCurrType(lBookTypeOid);
                                    objJurnalDetailCredit.setCurrAmount(1);
                                    objJurnalDetailCredit.setDebet(0);
                                    objJurnalDetailCredit.setKredit((-1) * dAccYearEarningBalance);
                                    lJurnalDetailOid = PstJurnalDetail.insertExc(objJurnalDetailCredit);

                                    dResult = (-1) * dAccYearEarningBalance;
                                } catch (Exception e) {
                                    System.out.println("Exc when try to insert jurnal closing period 3 position credit : " + e.toString());
                                }
                            }
                        }
                        // --- end membuat jurnal detail ---
                    }
                    // --- end proses jurnal ---
                }
            }
            // --- end iterasi sebanyak department, karena jurnal akan dilakukan per department
        }

        return dResult;
    }


    /**
     * @param lSelectedPeriodOid
     * @return
     */
    private boolean getTransferAccBalanceStatus(long lSelectedPeriodOid) {
        boolean bResult = false;

        Vector vAccountBalance = getVectOfObjAccountBalance(lSelectedPeriodOid);
        if (vAccountBalance != null && vAccountBalance.size() > 0) {    
            int iCounter = 0;
            int iAccountBalanceCount = vAccountBalance.size();
            for (int i = 0; i < iAccountBalanceCount; i++) {
                AccountBalance objAccountBalance = (AccountBalance) vAccountBalance.get(i);
                try {
                    SaldoAkhirPeriode objSaldoAkhirPeriode = new SaldoAkhirPeriode();

                    // jika saldo normal adalah DEBET
                    if (objAccountBalance.getINormalSign() == PstPerkiraan.ACC_DEBETSIGN) {
                        double dAccBalance = objAccountBalance.getDDebet() - objAccountBalance.getDCredit();
                        if (dAccBalance > 0) {
                            objSaldoAkhirPeriode.setPeriodeId(lSelectedPeriodOid);
                            objSaldoAkhirPeriode.setIdPerkiraan(objAccountBalance.getLPerkiraanOid());
                            objSaldoAkhirPeriode.setDebet(dAccBalance);
                            objSaldoAkhirPeriode.setKredit(0);
                            long lPeriodOid = PstSaldoAkhirPeriode.insertExc(objSaldoAkhirPeriode);
                            iCounter++;
                        } else {
                            objSaldoAkhirPeriode.setPeriodeId(lSelectedPeriodOid);
                            objSaldoAkhirPeriode.setIdPerkiraan(objAccountBalance.getLPerkiraanOid());
                            objSaldoAkhirPeriode.setDebet(0);
                            objSaldoAkhirPeriode.setKredit((-1) * dAccBalance);
                            long lPeriodOid = PstSaldoAkhirPeriode.insertExc(objSaldoAkhirPeriode);
                            iCounter++;
                        }
                    } else {// jika saldo normal adalah KREDIT
                        double dAccBalance = objAccountBalance.getDCredit() - objAccountBalance.getDDebet();
                        if (dAccBalance > 0) {
                            objSaldoAkhirPeriode.setPeriodeId(lSelectedPeriodOid);
                            objSaldoAkhirPeriode.setIdPerkiraan(objAccountBalance.getLPerkiraanOid());
                            objSaldoAkhirPeriode.setDebet(0);
                            objSaldoAkhirPeriode.setKredit(dAccBalance);
                            long lPeriodOid = PstSaldoAkhirPeriode.insertExc(objSaldoAkhirPeriode);
                            iCounter++;
                        } else {
                            objSaldoAkhirPeriode.setPeriodeId(lSelectedPeriodOid);
                            objSaldoAkhirPeriode.setIdPerkiraan(objAccountBalance.getLPerkiraanOid());
                            objSaldoAkhirPeriode.setDebet((-1) * dAccBalance);
                            objSaldoAkhirPeriode.setKredit(0);
                            long lPeriodOid = PstSaldoAkhirPeriode.insertExc(objSaldoAkhirPeriode);
                            iCounter++;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Exc when try to insert data to saldo akhir period : " + e.toString());
                    return false;
                }
            }
            // insert saldo data sebelumnya yang tidak transaksi di periode sekarang
            getDataSaldoAkhirPeriodeSebelumnya(lSelectedPeriodOid, vAccountBalance);
        }

        return true;
    }


    /**
     * @param toDay
     * @param periodId
     * @return
     */
    public static boolean isValidCloseBookTime(Date toDay, long periodId) {
        String strWhClause = PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE] + " = " + periodId;
        Vector tempPeriod = PstPeriode.list(0, 0, strWhClause, "");

        Periode per = new Periode();
        if (tempPeriod != null && tempPeriod.size() > 0) {
            per = (Periode) tempPeriod.get(0);
        } else {
            return true;
        }

        toDay = new Date(toDay.getYear(), toDay.getMonth(), toDay.getDate());
        if (toDay.before(per.getTglAkhir())) {
            return false;
        }

        return true;
    }

    public long closingBookPeriodic(Date dToday, long lPeriodeId, int iMonthInterval, int iLastEntryDuration, long lBookTypeOid, long lUserOid) {
        long result = 0;
        try{
            result = closingBookPeriodic(dToday, lPeriodeId, iMonthInterval, iLastEntryDuration, lBookTypeOid, lUserOid, false);
        }catch(Exception e){
            System.out.println("Exception on closingBookPeriodic() ::: "+e.toString());
        }
        return result;
    }

    /**
     * @param dToday
     * @param lPeriodeId
     * @param iMonthInterval
     * @param iLastEntryDuration
     * @return
     */
    public long closingBookPeriodic(Date dToday, long lPeriodeId, int iMonthInterval, int iLastEntryDuration, long lBookTypeOid, long lUserOid, boolean bolDept) {
        long lResult = 0;

        if (isValidCloseBookTime(dToday, lPeriodeId)) { 
            // 1. generate journal closing periode (jurnal penutup utk "pendapatan dan biaya" dengan "Laba(Rugi) Periode Berjalan"
            double dPeriodEarnAmount = 0;
            
            if(!bolDept){                
                dPeriodEarnAmount = generateJournalOfClosingPeriod1(lPeriodeId, lBookTypeOid, lUserOid);
            }else{                 
                dPeriodEarnAmount = generateJournalOfClosingPrd1(lPeriodeId, lBookTypeOid, lUserOid);
            }
            
            // 2. generate journal closing periode (jurnal penutup utk "Laba(Rugi) Periode Berjalan" dengan "Laba(Rugi) Tahun Berjalan"            
            double dYearEarnAmount = generateJournalOfClosingPeriod2(lPeriodeId, lBookTypeOid, lUserOid, dPeriodEarnAmount);

            // 3. process transfer saldo2 account current period
            //if(dYearEarnAmount > 0)
            //{
            boolean bCloseBookSuccess = getTransferAccBalanceStatus(lPeriodeId);
            boolean bCloseJurnalDistribute= SessJournalDistribution.closeJournalDistribution(lPeriodeId);
            if(!bCloseJurnalDistribute){
                System.out.println("CLOSING JOURNAL DISTRIBUTION GAGAL");
            }
            if (bCloseBookSuccess) {
                PstPeriode.setPeriodPosted(lPeriodeId);
		long lNewPeriodId = 0;
                if (!PstPeriode.openPrepareOpenPeriod(lPeriodeId)) {
                    // 4. membuat periode baru
                    SessPeriode objSessPeriode = new SessPeriode();
                    lResult = objSessPeriode.generateNewPeriod(lPeriodeId, iMonthInterval, iLastEntryDuration);
		    lNewPeriodId = PstPeriode.getCurrPeriodId();
		    if(lPeriodeId != 0 &&  lNewPeriodId != 0){
			try{
			    lResult = PstReportFixedAssets.closingFAReport(lPeriodeId, lNewPeriodId);
			}catch(Exception e){}
		    }
                }
            } else {
                System.out.println("Transfer saldo ke SAP gagal ...");
                return PstSaldoAkhirPeriode.ERR_TRANSFER_BALANCE_TO_SA;
            }
            //}
        } else {
            System.out.println("Not valid date to close period ...");
            return PstSaldoAkhirPeriode.ERR_INVALID_DATE_TO_CLOSE_PERIOD;
        }

        return lResult;
    }


    /**
     * @param dToday
     * @param lPeriodeId
     * @param iMonthInterval
     * @param iLastEntryDuration
     * @return
     */
    public long closingBookYearly(Date dToday, long lPeriodeId, int iMonthInterval, int iLastEntryDuration, long lBookTypeOid, long lUserOid) {
        long lResult = 0;

        if (isValidCloseBookTime(dToday, lPeriodeId)) {
            // 1. generate journal closing periode (jurnal penutup utk "pendapatan dan biaya" dengan "Laba(Rugi) Periode Berjalan"
            double dPeriodEarnAmount = generateJournalOfClosingPeriod1(lPeriodeId, lBookTypeOid, lUserOid);

            // 2. generate journal closing periode (jurnal penutup utk "Laba(Rugi) Periode Berjalan" dengan "Laba(Rugi) Tahun Berjalan"
            double dYearEarnAmount = generateJournalOfClosingPeriod2(lPeriodeId, lBookTypeOid, lUserOid, dPeriodEarnAmount);

            // 3. generate journal closing periode (jurnal penutup utk "Laba(Rugi) Tahun Berjalan" dengan "Laba Ditahan"
            double dRetainEarnAmount = generateJournalOfClosingPeriod3(lPeriodeId, lBookTypeOid, lUserOid,dYearEarnAmount);

            boolean bCloseBookSuccess = getTransferAccBalanceStatus(lPeriodeId);
            // 4. process transfer saldo2 account current period
            //if(dYearEarnAmount > 0)
            // {
            // pengecekan data yang sudah masuk ke saldo akhir
            if (bCloseBookSuccess) {
                PstPeriode.setPeriodPosted(lPeriodeId);
		long lNewPeriodId = 0;
                if (!PstPeriode.openPrepareOpenPeriod(lPeriodeId)) {
                    // 5. membuat periode baru
                    SessPeriode objSessPeriode = new SessPeriode();
                    lResult = objSessPeriode.generateNewPeriod(lPeriodeId, iMonthInterval, iLastEntryDuration);		    
		    lNewPeriodId = PstPeriode.getCurrPeriodId();
		     if(lPeriodeId != 0 &&  lNewPeriodId != 0){
			try{
			    lResult = PstReportFixedAssets.closingFAReport(lPeriodeId, lNewPeriodId);
			}catch(Exception e){}
		    }
                }
            } else {
                System.out.println("Transfer saldo ke SAP gagal ...");
                return PstSaldoAkhirPeriode.ERR_TRANSFER_BALANCE_TO_SA;
            }
            // }
        } else {
            System.out.println("Not valid date to close period ...");
            return PstSaldoAkhirPeriode.ERR_INVALID_DATE_TO_CLOSE_PERIOD;
        }

        return lResult;
    }


    /**
     * ngambil saldo account dari saldo akhir periode berdasar OID perkiraan dan OID periode
     * @param lAccountOid
     * @param lPeriodOid
     * @return x jika saldonya plus di normalsign-nya; -x jika saldonya min di normalsign-nya
     */
    public double getAccountBalance(long lAccountOid, long lPeriodOid) {
        double dResult = 0;

        DBResultSet dbrs = null;
        try {
            String sql = "SELECT ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " , ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    " , SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET] + " AS DEBET" +
                    " , SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT] + " AS KREDIT" +
                    " FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR + " AS SA" +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                    " ON SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
                    " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
                    " = " + lAccountOid +
                    " AND SA." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] +
                    " = " + lPeriodOid;

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
     *
     * @param PeriodOid
     * @return
     */
    public SaldoAkhirPeriode getSaldoPrevPeriodSelected(long lAccountOid, long PeriodOid) {
        SaldoAkhirPeriode saldoAkhirPeriode = new SaldoAkhirPeriode();
        DBResultSet dbrs = null;
        try {
            Periode period = PstPeriode.fetchExc(PeriodOid);
            String where = PstPeriode.fieldNames[PstPeriode.FLD_TGLAKHIR] + " < '" + Formater.formatDate(period.getTglAwal(), "yyyy-MM-dd") + "'";
            String order = PstPeriode.fieldNames[PstPeriode.FLD_TGLAKHIR] + " DESC ";
            Vector list = PstPeriode.list(0, 0, where, order);
            Periode perd = new Periode();
            if (list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    perd = (Periode) list.get(k);
                    break;
                }
            }
            String sql = "SELECT * FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR +
                    " WHERE " + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] +
                    " = " + lAccountOid +
                    " AND " + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] +
                    " = " + perd.getOID();
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                saldoAkhirPeriode.setIdPerkiraan(lAccountOid);
                saldoAkhirPeriode.setPeriodeId(PeriodOid);
                saldoAkhirPeriode.setDebet(rs.getDouble(PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET]));
                saldoAkhirPeriode.setKredit(rs.getDouble(PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT]));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Exc when getAccountBalance : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return saldoAkhirPeriode;
    }


    public void getDataSaldoAkhirPeriodeSebelumnya(long PeriodOid, Vector vSaldoAkhirPeriod) {
        SaldoAkhirPeriode saldoAkhirPeriode = new SaldoAkhirPeriode();
        DBResultSet dbrs = null;
        try {
            Periode period = PstPeriode.fetchExc(PeriodOid);
            String where = PstPeriode.fieldNames[PstPeriode.FLD_TGLAKHIR] + " < '" + Formater.formatDate(period.getTglAwal(), "yyyy-MM-dd") + "'";
            String order = PstPeriode.fieldNames[PstPeriode.FLD_TGLAKHIR] + " DESC ";
            Vector list = PstPeriode.list(0, 0, where, order);
            Periode perd = new Periode();
            if (list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    perd = (Periode) list.get(k);
                    break;
                }
            }
            String sql = "SELECT * FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR +
                    " WHERE " + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] +
                    " = " + perd.getOID();
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            boolean status = false;
            while (rs.next()) {
                long oidPerdLalu = rs.getLong(PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN]);
                for (int k = 0; k < vSaldoAkhirPeriod.size(); k++) {
                    AccountBalance saldoAkhir = (AccountBalance) vSaldoAkhirPeriod.get(k);
                    if (oidPerdLalu == saldoAkhir.getLPerkiraanOid()) {
                        status = true;
                        break;
                    } else {
                        status = false;
                    }
                }

                // jika di vector belum ada tambahkan
                if (!status) {
                    SaldoAkhirPeriode saldoAkhir = new SaldoAkhirPeriode();
                    saldoAkhir.setIdPerkiraan(oidPerdLalu);
                    saldoAkhir.setPeriodeId(PeriodOid);
                    saldoAkhir.setDebet(rs.getDouble(PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET]));
                    saldoAkhir.setKredit(rs.getDouble(PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT]));
                    long lPeriodOid = PstSaldoAkhirPeriode.insertExc(saldoAkhir);
                    status = false;
                }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Exc when getAccountBalance : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static synchronized  long closingFAOnly(long lPeriodeId, int iMonthInterval, int iLastEntryDuration){
	long lResult = 0;
	long lNewPeriodId = 0;
	try{
	    PstPeriode.setPeriodPosted(lPeriodeId);
	    lNewPeriodId = SessPeriode.generateNewPeriod(lPeriodeId, iMonthInterval, iLastEntryDuration);
	    if(lPeriodeId != 0 && lNewPeriodId != 0){
		try{
		    lResult = PstReportFixedAssets.closingFAReport(lPeriodeId, lNewPeriodId);
		}catch(Exception e){}
	    }
	}catch(Exception e){System.out.println("Exp SessSaldoAkhirPeriode.closingFAOnly :::::::::::::: "+e.toString());}
	return lResult;
    }
}
