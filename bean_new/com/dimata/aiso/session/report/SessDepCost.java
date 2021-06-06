/*
 * SessDepCost.java
 *
 * Created on June 21, 2007, 4:03 PM
 */

package com.dimata.aiso.session.report;

/**
 *
 * @author  dwi
 */

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
import com.dimata.aiso.entity.report.DepartmentalCost;
import com.dimata.aiso.entity.report.WorkSheet;
import com.dimata.aiso.entity.report.Neraca;
import com.dimata.aiso.entity.report.ProfitLoss;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalDetail;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalMain;
import com.dimata.harisma.entity.masterdata.PstDepartment;
import com.dimata.interfaces.chartofaccount.I_ChartOfAccountGroup;
import com.dimata.interfaces.journal.I_JournalType;
import com.dimata.util.Formater;

import java.util.Vector;
import java.sql.ResultSet;
import java.util.Date;

public class SessDepCost {
    
    synchronized public static Vector listDepartmentalCost(long startPeriodId, long endPeriodId, long departmentId){
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1,1);
            try{
                //long endPeriodId = getIdCurrPeriod();
                int iDelete = deleteDataBuffer();
                int iInsert = insertExpensesData(startPeriodId, endPeriodId, departmentId);
                String sql = getDataFromBuffer();
                
                System.out.println("SQL listDepartmentalCost() :::: "+sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()){
                    DepartmentalCost objDepCost = new DepartmentalCost();
                    vResult.add(resultToObject(objDepCost, rs));
                }
            }catch(Exception e){
                vResult = new Vector(1,1);
                System.out.println("Exception on listDepartmentalCost() ::: "+e.toString());
            }
        return vResult;
    }
    
    private static int deleteDataBuffer(){
        int iResult = 0;
            try{
                String sql = " DELETE FROM AISO_DEP_COST_BUFFER";
                iResult = DBHandler.execUpdate(sql);
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on deleteDataBuffer() ::: "+e.toString());
            }
        return iResult;
    }
    
    private static String getDataFromBuffer(){
        String sql = "";
            try{
                sql = " SELECT ID_PARENT, ACC_NAME, ACC_NAME_ENGLISH, DEBIT_CREDIT_ASSIGNT, SUM(MTD_BUDGET) AS MTD_BUDGET," +
                " SUM(YTD_BUDGET) AS YTD_BUDGET, SUM(MTD_ACTUAL) AS MTD_ACTUAL, SUM(YTD_ACTUAL) AS YTD_ACTUAL " +
                " FROM AISO_DEP_COST_BUFFER GROUP BY ID_PARENT, ACC_NAME, ACC_NAME_ENGLISH, DEBIT_CREDIT_ASSIGNT"+
                " ORDER BY ID_PARENT";
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on getDataFromBuffer() ::: "+e.toString());
            }
        return sql;
    }
    
    private static DepartmentalCost resultToObject(DepartmentalCost objDepCost, ResultSet rs){
        try{
                objDepCost.setIdParent(rs.getLong(1));
                objDepCost.setAccName(rs.getString(2));
                objDepCost.setAccNameEnglish(rs.getString(3));
                objDepCost.setDebitCreditAssignt(rs.getInt(4));
                objDepCost.setMtdBudget(rs.getDouble(5));
                objDepCost.setMtdActual(rs.getDouble(7));
                objDepCost.setYtdBudget(rs.getDouble(6));
                objDepCost.setYtdActual(rs.getDouble(8));           
        }catch(Exception e){
            objDepCost = new DepartmentalCost();
            System.out.println("Exception on resultToObject() ::: "+e.toString());
        }
        return objDepCost;
    }
    
    private static int insertExpensesData(long startPeriodId, long endPeriodId, long departmentId){
        int iResult = 0;
            try{
                iResult = insertMTDExpenseJU(startPeriodId, endPeriodId, departmentId);
                iResult = insertMTDExpenseSJ(startPeriodId, endPeriodId, departmentId);
                iResult = insertYTDExpenseJU(startPeriodId, endPeriodId, departmentId);
                iResult = insertYTDExpenseSJ(startPeriodId, endPeriodId, departmentId);
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on insertExpensesDate() ::: "+e.toString());
            }
        return iResult;
    }
    
    private static int insertMTDExpenseJU(long startPeriodId, long endPeriodId, long departmentId){
        int iResult = 0;
            try{
                String sql = queryInsert("MTD_BUDGET", "MTD_ACTUAL")+
                            querySelect("MTD_BUDGET", "MTD_ACTUAL", I_JournalType.TIPE_JURNAL_UMUM, 1)+
                            queryFrom(PstJurnalUmum.TBL_JURNAL_UMUM, PstJurnalDetail.TBL_JURNAL_DETAIL, 
                            PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID], 
                            PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID], 
                            PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN], 
                            PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE],  
                            PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID],I_JournalType.TIPE_JURNAL_UMUM)+
                            whereClause(startPeriodId, endPeriodId, departmentId, 
                            PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE], 
                            "", "", PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI])+
                            queryGroupBy();
                
                System.out.println("SQL insertMTDExpenseJU ::: "+sql);
                iResult = DBHandler.execUpdate(sql);
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on insertMTDExpenseJU() ::: "+e.toString());
            }
        return iResult;
    }
    
     private static int insertYTDExpenseJU(long startPeriodId, long endPeriodId, long departmentId){
        int iResult = 0;
         Periode objPeriode = new Periode();
            if(endPeriodId != 0){
                try{
                    objPeriode = PstPeriode.fetchExc(endPeriodId);
                }catch(Exception e){
                    objPeriode = new Periode();
                    System.out.println("Exception on fetchObjPeriodeYTDExpenseJU ::: "+e.toString());
                }
            }
            Date startPeriodDate = (Date)objPeriode.getTglAwal();
            int month = startPeriodDate.getMonth();
            Date startYearDate = (Date)startPeriodDate.clone();
            startYearDate.setDate(1);
            startYearDate.setMonth(0);
            Date endYearDate = new Date();
            if(objPeriode.getPosted() == PstPeriode.PERIOD_CLOSED)
                endYearDate = objPeriode.getTglAkhir();
            String stSYD = Formater.formatDate(startYearDate,"yyyy-MM-dd");
            String stNYD = Formater.formatDate(endYearDate, "yyyy-MM-dd");
            try{ 
                String sql = queryInsert("YTD_BUDGET", "YTD_ACTUAL")+
                            querySelect("YTD_BUDGET", "YTD_ACTUAL", I_JournalType.TIPE_JURNAL_UMUM, month)+
                            queryFrom(PstJurnalUmum.TBL_JURNAL_UMUM, PstJurnalDetail.TBL_JURNAL_DETAIL, 
                            PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID], 
                            PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID], 
                            PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN], 
                            PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE],  
                            PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID],I_JournalType.TIPE_JURNAL_UMUM)+
                            whereClause(0, 0, departmentId, 
                            PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE], 
                            stSYD, stNYD, PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI])+
                            queryGroupBy();
                System.out.println("SQL insertYTDExpenseJU ::: "+sql);
                iResult = DBHandler.execUpdate(sql);
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on insertYTDExpenseJU() ::: "+e.toString());
            }
        return iResult;
    }
     
      private static int insertMTDExpenseSJ(long startPeriodId, long endPeriodId, long departmentId){
        int iResult = 0;
            try{
                String sql = queryInsert("MTD_BUDGET", "MTD_ACTUAL")+
                            querySelect("MTD_BUDGET", "MTD_ACTUAL", I_JournalType.TIPE_SPECIAL_JOURNAL_BANK, 1)+
                            queryFrom(PstSpecialJournalMain.TBL_AISO_SPC_JMAIN, PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL, 
                            PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID], 
                            PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID], 
                            PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN], 
                            PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE],  
                            PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID],1)+
                            whereClause(startPeriodId, endPeriodId, departmentId, 
                            PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE], 
                            "", "", PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE])+
                            queryGroupBy();
                System.out.println("SQL insertMTDExpenseSJ ::: "+sql);
                iResult = DBHandler.execUpdate(sql);
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on insertMTDExpenseSJ() ::: "+e.toString());
            }
        return iResult;
    }
    
     private static int insertYTDExpenseSJ(long startPeriodId, long endPeriodId, long departmentId){
        int iResult = 0;
         Periode objPeriode = new Periode();
            if(endPeriodId != 0){
                try{
                    objPeriode = PstPeriode.fetchExc(endPeriodId);
                }catch(Exception e){
                    objPeriode = new Periode();
                    System.out.println("Exception on fetchObjPeriodeYTDExpenseJU ::: "+e.toString());
                }
            }
            Date startPeriodDate = (Date)objPeriode.getTglAwal();
            int month = startPeriodDate.getMonth();
            System.out.println("NILAI BULAN INI ::::::::::::::::::: "+month);
            Date startYearDate = (Date)startPeriodDate.clone();
            startYearDate.setDate(1);
            startYearDate.setMonth(0);
            Date endYearDate = new Date();
            if(objPeriode.getPosted() == PstPeriode.PERIOD_CLOSED)
                endYearDate = objPeriode.getTglAkhir();
            String stSYD = Formater.formatDate(startYearDate,"yyyy-MM-dd");
            String stNYD = Formater.formatDate(endYearDate, "yyyy-MM-dd");
            try{ 
                String sql = queryInsert("YTD_BUDGET", "YTD_ACTUAL")+
                            querySelect("YTD_BUDGET", "YTD_ACTUAL", I_JournalType.TIPE_SPECIAL_JOURNAL_BANK, month)+
                            queryFrom(PstSpecialJournalMain.TBL_AISO_SPC_JMAIN, PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL,
                            PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID], 
                            PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID],
                            PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN], 
                            PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE],  
                            PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID],1)+
                            whereClause(0, 0, departmentId, 
                            PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE],
                            stSYD, stNYD, PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE])+
                            queryGroupBy();                
                System.out.println("SQL insertYTDExpenseSJ ::: "+sql);
                iResult = DBHandler.execUpdate(sql);
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on insertYTDExpenseSJ() ::: "+e.toString());
            }
        return iResult;
    }
    
    private static String queryInsert(String budget, String amount){
        String sql = "";
            try{
                sql = " INSERT INTO AISO_DEP_COST_BUFFER(ID_PARENT, ACC_NAME, ACC_NAME_ENGLISH, DEBIT_CREDIT_ASSIGNT,"+
                    budget+", "+amount+") ";
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on queryInsert() ::: "+e.toString());
            }
        return sql;
    }
    
    private static String querySelect(String budget, String amount, int journalType, int month){
        String sql = "";
        float fMonth = Float.parseFloat(String.valueOf(month));
            try{
                sql = " SELECT PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]+
                    ", SUM("+Float.parseFloat(String.valueOf(fMonth / 12))+" * BUD."+
                    PstActivityPeriodLink.fieldNames[PstActivityPeriodLink.FLD_BUDGET]+") AS "+budget;
                if(journalType == I_JournalType.TIPE_JURNAL_UMUM){
                        sql = sql +", SUM(JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET]+
                            " - JD."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT]+") AS "+amount;
                }else{
                        sql = sql +", SUM(JD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT]+
                        ") AS "+amount;
                }
                    
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on querySelect() ::: "+e.toString());
            }
        return sql;
    }
    
    private static String queryGroupBy(){
        String sql = "";
            try{
                sql = " GROUP BY PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
                    ", PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT];
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on queryGroupBy() ::: "+e.toString());
            }
        return sql;
    }
    
    private static String whereClause(long startPeriodId, long endPeriodId, long departmentId, String fieldJournalType, String startDate, 
    String endDate, String fieldDate){
        String whereClause = "";
            try{
                whereClause = " WHERE PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
                             " IN("+I_ChartOfAccountGroup.ACC_GROUP_EXPENSE+
                             ", "+I_ChartOfAccountGroup.ACC_GROUP_OTHER_EXPENSE+") "+
                             " AND JU."+fieldJournalType+
                             " IN("+I_JournalType.TIPE_JURNAL_UMUM+
                             ", "+I_JournalType.TIPE_SPECIAL_JOURNAL_BANK+
                             ", "+I_JournalType.TIPE_SPECIAL_JOURNAL_NON_CASH+
                             ", "+I_JournalType.TIPE_SPECIAL_JOURNAL_PETTY_CASH+") "+
                             " AND DEP."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]+
                             " = "+departmentId;
                if(startPeriodId != 0 && endPeriodId != 0){
                        whereClause = whereClause +" AND PRD."+PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE]+
                        " BETWEEN "+startPeriodId+" AND "+endPeriodId;
                }else{
                         whereClause = whereClause +" AND JU."+fieldDate+" BETWEEN '"+startDate+"' AND '"+endDate+"' ";
                }        
                
            }catch(Exception e){
                whereClause = "";
                System.out.println("Exception on whereClause() ::: "+e.toString());
            }
        return whereClause;
    }
    
    private static String queryFrom(String tblJournalMain, String tblJournalDetail, String fieldMainJournalId, 
    String fieldDetailJournalId, String fieldAccId, String fieldPeriodId, String fieldDepartmentId, int journalType){
        String sql = "";
            try{
                sql = " FROM "+tblJournalMain+" AS JU "+
                    " INNER JOIN "+tblJournalDetail+" AS JD ON JU."+fieldMainJournalId+" = JD."+fieldDetailJournalId+
                    " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS PRK "+
                    " ON JD."+fieldAccId+" = PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                    " INNER JOIN "+PstPeriode.TBL_PERIODE+" AS PRD "+
                    " ON JU."+fieldPeriodId+" = PRD."+PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE]+
                    " INNER JOIN "+PstDepartment.TBL_HR_DEPARTMENT+" AS DEP ";
                    if(journalType == I_JournalType.TIPE_JURNAL_UMUM){
                    sql = sql +" ON JU."+fieldDepartmentId+" = DEP."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID];
                    }else{
                    sql = sql +" ON JD."+fieldDepartmentId+" = DEP."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID];
                    }
                    sql = sql + " LEFT JOIN "+PstActivityAccountLink.TBL_AISO_ACTIVITY_ACCOUNT+" AS ACT "+
                    " ON PRK."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                    " = ACT."+PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACCOUNT_ID]+
                    " LEFT JOIN "+PstActivityPeriodLink.TBL_AISO_ACT_PERIOD_LINK+" AS BUD "+
                    " ON ACT."+PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACTIVITY_ID]+
                    " = BUD."+PstActivityPeriodLink.fieldNames[PstActivityPeriodLink.FLD_ACTIVITY_ID];
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on queryFrom() ::: "+e.toString());
            }
        return sql;
    }
    
    private static boolean cekNol(long lOid){
        if(lOid != 0)
            return true;
        else
            return false;
    }
    
    private static long getIdCurrPeriod(){
        DBResultSet dbrs = null;
        long periodId = 0;
        try{
            String sql = " SELECT "+PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE]+
                        " FROM "+PstPeriode.TBL_PERIODE+
                        " WHERE "+PstPeriode.fieldNames[PstPeriode.FLD_POSTED]+
                        " = "+PstPeriode.PERIOD_OPEN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                periodId = rs.getLong(1);
            }
        }catch(Exception e){
            periodId = 0;
            System.out.println("Exception on getIdCurrPeriod() ::: "+e.toString());
        }
        return periodId;
    }
    
    public static void main(String[] arg){
        SessDepCost objSessDepCost = new SessDepCost();
        boolean cekNol = objSessDepCost.cekNol(0);
        System.out.println("cekNol :::: "+cekNol);
    }
}
