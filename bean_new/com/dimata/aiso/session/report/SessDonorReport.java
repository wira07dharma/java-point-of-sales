/*
 * SessDonorReport.java
 *
 * Created on June 1, 2007, 9:59 AM
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
import com.dimata.aiso.entity.jurnal.PstSaldoAkhirPeriode;
import com.dimata.aiso.entity.masterdata.PstActivity;
import com.dimata.aiso.entity.masterdata.PstActivityPeriodLink;
import com.dimata.aiso.entity.periode.Periode;
import com.dimata.aiso.entity.report.WorkSheet;
import com.dimata.aiso.entity.report.Neraca;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalDetail;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalDetailAssignt;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalMain;
import com.dimata.aiso.entity.periode.PstPeriode;
import com.dimata.aiso.entity.report.DonorReport;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.interfaces.journal.I_JournalType;
import com.dimata.util.Formater;

import java.util.Vector;
import java.sql.ResultSet;
import java.util.Date;

public class SessDonorReport {
    
   /**
     * @param long oidPeriod
     * return Vector Data Report Donor
     */
    public static Vector getDataReportDonor(long oidPeriod){
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        
        int prnLevel01 = PstActivity.LEVEL_SUB_MODULE;
        int prnLevel02 = PstActivity.LEVEL_HEADER;        
        
        int actTypeProgramatic = PstActivity.ACT_PROGRAMATIC;
        int actTypeSupport = PstActivity.ACT_SUPPORT;
        
        double totTypeProgramatic = 0;
        double totTypeSupport = 0;
        double supportAmount = 0;
        
        Date startYearDate = new Date();
        startYearDate.setDate(1);
        startYearDate.setMonth(0);
        String stYearDate = Formater.formatDate(startYearDate, "yyyy-MM-dd");
       
        Periode objPeriod = new Periode();
            if(oidPeriod != 0){
                try{
                    objPeriod = PstPeriode.fetchExc(oidPeriod);
                }catch(Exception e){
                    objPeriod = new Periode(); 
                    System.out.println("Exception on fetch objPeriode :::: "+e.toString());
                }
            }
        
        String startDate = Formater.formatDate(objPeriod.getTglAwal(), "yyyy-MM-dd");
        String endDate = Formater.formatDate(objPeriod.getTglAkhir(), "yyyy-MM-dd");
        int currMonth = Integer.parseInt(startDate.substring(6,7));
        String periodInterval = "";
        try{
            periodInterval = PstSystemProperty.getValueByName("PERIOD_INTERVAL");
        }catch(Exception e){
            periodInterval = "";
            System.out.println("Exception on getPeriodInterval fr System Property :::: "+e.toString());
        }
        String endYearDate = "";
        Date endYrDate = new Date();
        if(periodInterval.equalsIgnoreCase("1")){
            if(objPeriod.getPosted() == PstPeriode.PERIOD_CLOSED){
                endYearDate = endDate;
            }else{                
                endYearDate = Formater.formatDate(endYrDate, "yyyy-MM-dd");
                endDate = endYearDate;
            }
        }else{
             endYearDate = Formater.formatDate(endYrDate, "yyyy-MM-dd");
             endDate = endYearDate;
        }
        
        
       try{
            totTypeProgramatic = getTotAmount(startDate, endDate, actTypeProgramatic);
            totTypeSupport = getTotAmount(startDate, endDate, actTypeSupport);
            
            String sql = getAllLevelData(startDate, endDate, stYearDate, endYearDate, prnLevel01, prnLevel02, currMonth, actTypeProgramatic);
            
            System.out.println("SQL getDataReportDonor() ::: "+sql);
            
            System.out.println("totTypeProgramatic ::: "+Formater.formatNumber(totTypeProgramatic, "##,###.##"));
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                
                DonorReport objDonorReport = new DonorReport();                
                
                objDonorReport.setActCode(rs.getString("CODE"));
                objDonorReport.setActDescription(rs.getString("DESCRIPTION"));
                objDonorReport.setMtdBudget(rs.getDouble("MTDBUDGET"));
                objDonorReport.setYtdBudget(rs.getDouble("YTDBUDGET"));
                objDonorReport.setMtdAmount(rs.getDouble("MTDAMOUNT"));
                objDonorReport.setYtdAmount(rs.getDouble("YTDAMOUNT"));
                objDonorReport.setLevel(rs.getInt("LEVEL"));
                
                double mtdAmount = objDonorReport.getMtdAmount();
                
                
                if(totTypeProgramatic > 0){
                    supportAmount = (mtdAmount / totTypeProgramatic) * totTypeSupport;
                    objDonorReport.setSptAmount(supportAmount);
                }
                
                result.add(objDonorReport);
            }
       }catch(Exception e){
            result = new Vector();
            System.out.println("Exception on getDataReportDonor() ::: "+e.toString());
       }        
        return result;
    }
   
   public static Vector getDonorReportSupport(long oidPeriod){
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1,1);
        
        Date endDate = new Date();        
        
        Periode objPeriode = new Periode();
        if(oidPeriod != 0){
            try{
                objPeriode = PstPeriode.fetchExc(oidPeriod);
            }catch(Exception e){
                objPeriode = new Periode();
                System.out.println("Exception on fetch objPeriod ::: "+e.toString());
            }            
        }
        
        if(objPeriode != null){
            if(objPeriode.getPosted() == PstPeriode.PERIOD_CLOSED){
                endDate = objPeriode.getTglAkhir();
            }
        }
        
        Date startDate = (Date)endDate.clone();
        startDate.setDate(1);        
        String stDate = Formater.formatDate(startDate, "yyyy-MM-dd");
        String ndDate = Formater.formatDate(endDate, "yyyy-MM-dd");
        
        Date startYrDate = (Date)startDate.clone();
        startYrDate.setMonth(0);
        String stYrDate = Formater.formatDate(startYrDate, "yyyy-MM-dd");
        int actType = PstActivity.ACT_SUPPORT;
        int currMonth = endDate.getMonth();
        int prtLevel01 = PstActivity.LEVEL_SUB_MODULE;
        int prtLevel02 = PstActivity.LEVEL_HEADER;
        
        try{
            String sql = "SELECT VI.CODE AS CODE, VI.DESCRIPTION AS DESCRIPTION, VI.ACT_LEVEL AS LEVEL, SUM(IO.MTDBUDGET) AS MTDBUDGET, SUM(IO.YTDBUDGET) AS YTDBUDGET, "+
                        " SUM(IO.MTDAMOUNT) AS MTDAMOUNT, SUM(IO.YTDAMOUNT) AS YTDAMOUNT"+
                        " FROM("+getDataLevelModule(stDate,ndDate,stYrDate,ndDate,prtLevel01,prtLevel02,currMonth,actType)+
                        ") AS IO INNER JOIN "+PstActivity.TBL_AISO_ACTIVITY+
                        " AS VI ON IO.PARENT_ID = VI."+PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]+
                        " GROUP BY VI.CODE, VI.DESCRIPTION,  VI.ACT_LEVEL ORDER BY VI.CODE";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                DonorReport objDonorReport = new DonorReport();
                
                objDonorReport.setActCode(rs.getString("CODE"));
                objDonorReport.setActDescription(rs.getString("DESCRIPTION"));
                objDonorReport.setMtdAmount(rs.getDouble("MTDAMOUNT"));
                
                vResult.add(objDonorReport);
            }
        }catch(Exception e){
            vResult = new Vector(1,1);
            System.out.println("Exception on getDonorReportSupport() ::: "+e.toString());
        }
        return vResult;
   } 
   
   private static String getAllLevelData(String startDate, String endDate, String stYearDate, String endYearDate, 
   int prnLevel01, int prnLevel02, int currMonth, int actType){
       String sql = "";
        try{
            sql = " SELECT VI.CODE AS CODE, VI.DESCRIPTION AS DESCRIPTION, VI.ACT_LEVEL AS LEVEL, SUM(IO.MTDBUDGET) AS MTDBUDGET, SUM(IO.YTDBUDGET) AS YTDBUDGET, "+
                        " SUM(IO.MTDAMOUNT) AS MTDAMOUNT, SUM(IO.YTDAMOUNT) AS YTDAMOUNT"+
                        " FROM("+getDataLevelHeader(startDate,endDate,stYearDate,endYearDate,prnLevel02, currMonth, actType)+
                        " UNION "+getDataLevelSubModule(startDate, endDate, stYearDate,endYearDate, prnLevel01, currMonth, actType)+
                        " UNION "+getDataLevelModule(startDate,endDate,stYearDate,endYearDate,prnLevel01,prnLevel02,currMonth, actType)+
                        ") AS IO INNER JOIN "+PstActivity.TBL_AISO_ACTIVITY+
                        " AS VI ON IO.PARENT_ID = VI."+PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]+
                        " GROUP BY VI.CODE, VI.DESCRIPTION,  VI.ACT_LEVEL ORDER BY VI.CODE";
        }catch(Exception e){
            sql = "";
            System.out.println("Exception on getAllLevelData() ::: "+e.toString());
        }
       return sql;
   } 
   
   private static String getDataLevelModule(String startDate, String endDate, String stYearDate, String endYearDate, 
   int prnLevel01, int prnLevel02, int currMonth, int actType){
        
       String sql = "";
        try{            
            sql = " SELECT TY.PARENT_ID AS PARENT_ID, SUM(LEV0.MTDBUDGET) AS MTDBUDGET, SUM(LEV0.YTDBUDGET) AS YTDBUDGET,"+
                " SUM(LEV0.MTDAMOUNT) AS MTDAMOUNT, SUM(LEV0.YTDAMOUNT) AS YTDAMOUNT"+
                " FROM("+getDataLevelSubModule(startDate, endDate, stYearDate, endYearDate, prnLevel01, currMonth, actType)+
                " UNION "+getDataLevelHeader(startDate, endDate, stYearDate, endYearDate,prnLevel02, currMonth, actType)+
                ") AS LEV0 INNER JOIN "+PstActivity.TBL_AISO_ACTIVITY+" AS TY "+
                " ON LEV0.PARENT_ID = TY."+PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]+
                " GROUP BY TY."+PstActivity.fieldNames[PstActivity.FLD_PARENT_ID];                
        }catch(Exception e){
            sql = "";
            System.out.println("Exception on getDataLevelModule() ::: "+e.toString());
        }
    return sql;
   }
   
   private static String getDataLevelHeader(String startDate, String endDate, String startYearDate, String endYearDate, 
   int parentLevel, int currMonth, int actType){
        String sql = "";
            try{                
                sql = " SELECT UN.PARENT_ID AS PARENT_ID, SUM(UN.MTDBUDGET) AS MTDBUDGET, SUM(UN.YTDBUDGET) AS YTDBUDGET,"+
                    " SUM(UN.MTDAMOUNT) AS MTDAMOUNT, SUM(UN.YTDAMOUNT)"+
                    " AS YTDAMOUNT FROM("+toUpLevel(startDate, endDate, startYearDate, endYearDate, parentLevel,currMonth,actType)+
                    ") AS UN GROUP BY UN.PARENT_ID ";
            }catch(Exception e){
                sql = "";
                System.out.println("Exception getDataLevelHeader() ::: "+e.toString());
            }
        return sql;
   }
   
   private static String getDataLevelSubModule(String startDate, String endDate, String startYearDate, 
   String endYearDate, int parentLevel, int currMonth, int actType){
    String sql = "";
        try{            
            sql = " SELECT UN.PARENT_ID AS PARENT_ID, SUM(UN.MTDBUDGET) AS MTDBUDGET, SUM(UN.YTDBUDGET) AS YTDBUDGET,"+
                " SUM(UN.MTDAMOUNT) AS MTDAMOUNT, SUM(UN.YTDAMOUNT) AS YTDAMOUNT"+
                " FROM("+getMTDData(startDate,endDate,parentLevel,actType)+
                " UNION "+getYTDData(startYearDate,endYearDate,parentLevel,currMonth,actType)+
                ") AS UN GROUP BY UN.PARENT_ID ";
        }catch(Exception e){
            sql = "";
            System.out.println(" Exception on getDataLevelSubModule() ::: "+e.toString());
        }
    return sql;
   }
   
    private static String toUpLevel(String startDate, String endDate, String startYearDate, String endYearDate, 
    int parentLevel, int currMonth, int actType){ 
        String sql = "";
            try{
                sql = " SELECT CT."+PstActivity.fieldNames[PstActivity.FLD_PARENT_ID]+
                    ", SUM(UO.MTDBUDGET) AS MTDBUDGET, SUM(UO.YTDBUDGET) AS YTDBUDGET, SUM(UO.MTDAMOUNT) AS MTDAMOUNT "+
                    ", SUM(UO.YTDAMOUNT) AS YTDAMOUNT FROM("+getMTDData(startDate,endDate,parentLevel,actType)+
                    " UNION "+getYTDData(startYearDate,endYearDate, parentLevel,currMonth, actType)+
                    ") AS UO INNER JOIN "+PstActivity.TBL_AISO_ACTIVITY+
                    " AS CT ON UO.PARENT_ID = "+PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]+
                    " GROUP BY CT."+PstActivity.fieldNames[PstActivity.FLD_PARENT_ID];
                
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on toUpLevel() ::: "+e.toString());
            }
        return sql;
    }
   
     /**
     * @param Date startDate
     * @param Date endDate
     * @param Date parentLevel
     * return String SQL 
     */
   private static String getMTDData(String startDate, String endDate, int parentLevel, int actType){
        String sql = "";
            try{                
                sql = " SELECT ACT."+PstActivity.fieldNames[PstActivity.FLD_PARENT_ID]+
                      ", SUM(BUD."+PstActivityPeriodLink.fieldNames[PstActivityPeriodLink.FLD_BUDGET]+
                      " / 12) AS MTDBUDGET, 0 AS YTDBUDGET"+
                      ", SUM(SDA."+PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_AMOUNT]+
                      ") AS MTDAMOUNT, 0 AS YTDAMOUNT "+
                      " FROM "+PstSpecialJournalDetailAssignt.TBL_AISO_JDETAIL_ASSIGNT+" AS SDA "+
                      " INNER JOIN "+PstActivity.TBL_AISO_ACTIVITY+" AS ACT "+
                      " ON SDA."+PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_ACTIVITY_ID]+" = "+
                      " ACT."+PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]+
                      " INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+
                      " ON SDA."+PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_JOURNAL_DETAIL_ID]+" = "+
                      " SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_DETAIL_ID]+
                      " INNER JOIN "+PstSpecialJournalMain.TBL_AISO_SPC_JMAIN+" AS JM "+
                      " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+" = "+
                      " SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                      " LEFT JOIN "+PstActivityPeriodLink.TBL_AISO_ACT_PERIOD_LINK+" AS BUD "+
                      " ON BUD."+PstActivityPeriodLink.fieldNames[PstActivityPeriodLink.FLD_ACTIVITY_ID]+" = "+
                      " ACT."+PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]+
                      " WHERE ACT."+PstActivity.fieldNames[PstActivity.FLD_PARENT_ID]+
                      " IN(SELECT "+PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]+
                      " FROM "+PstActivity.TBL_AISO_ACTIVITY+" WHERE "+PstActivity.fieldNames[PstActivity.FLD_ACT_LEVEL]+
                      " = "+parentLevel+") AND "+
                      " JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE]+
                      " BETWEEN '"+startDate+"' AND '"+endDate+"' ";
                
                if((actType == PstActivity.ACT_PROGRAMATIC) || (actType == PstActivity.ACT_SUPPORT)){
                    sql = sql +" AND ACT."+PstActivity.fieldNames[PstActivity.FLD_ACT_TYPE]+" = " +actType+
                        " GROUP BY ACT."+PstActivity.fieldNames[PstActivity.FLD_PARENT_ID];
                }else{
                    sql = sql + " GROUP BY ACT."+PstActivity.fieldNames[PstActivity.FLD_PARENT_ID];
                }
                
                //System.out.println("SQL getMTDData() ::: "+sql);
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on getMTDData() ::: "+e.toString());
            }
        return sql;
    }
    
    /**
     * @param Date startYearDate
     * @param int parentLevel
     * return String SQL 
     */
    private static String getYTDData(String startYearDate, String endYearDate, int parentLevel, int currMonth, int actType){
        String sql = "";
            try{                 
                sql = " SELECT ACT."+PstActivity.fieldNames[PstActivity.FLD_PARENT_ID]+
                      ", 0 AS MTDBUDGET "+
                      ", SUM((BUD."+PstActivityPeriodLink.fieldNames[PstActivityPeriodLink.FLD_BUDGET]+
                      " * "+currMonth+") / 12) AS YTDBUDGET, 0 AS MTDAMOUNT "+                      
                      ", SUM(SDA."+PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_AMOUNT]+
                      ") AS YTDAMOUNT FROM "+PstSpecialJournalDetailAssignt.TBL_AISO_JDETAIL_ASSIGNT+" AS SDA "+
                      " INNER JOIN "+PstActivity.TBL_AISO_ACTIVITY+" AS ACT "+
                      " ON SDA."+PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_ACTIVITY_ID]+" = "+
                      " ACT."+PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]+
                      " INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+
                      " ON SDA."+PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_JOURNAL_DETAIL_ID]+" = "+
                      " SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_DETAIL_ID]+
                      " INNER JOIN "+PstSpecialJournalMain.TBL_AISO_SPC_JMAIN+" AS JM "+
                      " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+" = "+
                      " SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                      " LEFT JOIN "+PstActivityPeriodLink.TBL_AISO_ACT_PERIOD_LINK+" AS BUD "+
                      " ON BUD."+PstActivityPeriodLink.fieldNames[PstActivityPeriodLink.FLD_ACTIVITY_ID]+" = "+
                      " ACT."+PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]+
                      " WHERE ACT."+PstActivity.fieldNames[PstActivity.FLD_PARENT_ID]+
                      " IN(SELECT "+PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]+
                      " FROM "+PstActivity.TBL_AISO_ACTIVITY+" WHERE "+PstActivity.fieldNames[PstActivity.FLD_ACT_LEVEL]+
                      " = "+parentLevel+") AND "+
                      " JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE]+
                      " BETWEEN '"+startYearDate+"' AND '"+endYearDate+"' ";
                
                if((actType == PstActivity.ACT_PROGRAMATIC) || (actType == PstActivity.ACT_SUPPORT)){
                    sql = sql +" AND ACT."+PstActivity.fieldNames[PstActivity.FLD_ACT_TYPE]+" = " +actType+
                        " GROUP BY ACT."+PstActivity.fieldNames[PstActivity.FLD_PARENT_ID];
                }else{
                    sql = sql + " GROUP BY ACT."+PstActivity.fieldNames[PstActivity.FLD_PARENT_ID];
                }
                
            }catch(Exception e){
                sql = "";
                System.out.println("Exception on getYTDData() ::: "+e.toString());
            }
        return sql;
    }
    
   private static double getTotAmount(String startDate, String endDate, int actType){
        DBResultSet dbrs = null;
        double dResult = 0;
        try{            
            String sql = " SELECT SUM(JA."+PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_AMOUNT]+") AS TOTPMAMOUNT "+
                        " FROM "+PstSpecialJournalDetailAssignt.TBL_AISO_JDETAIL_ASSIGNT+" AS JA"+
                        " INNER JOIN "+PstActivity.TBL_AISO_ACTIVITY+" AS IV "+
                        " ON IV."+PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]+" = JA."+PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_ACTIVITY_ID]+
                        " INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS ASJ "+
                        " ON JA."+PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_JOURNAL_DETAIL_ID]+
                        " = ASJ."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_DETAIL_ID]+
                        " INNER JOIN "+PstSpecialJournalMain.TBL_AISO_SPC_JMAIN+" AS ASM "+
                        " ON ASM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                        " = ASJ."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                        " WHERE IV."+PstActivity.fieldNames[PstActivity.FLD_ACT_TYPE]+" = "+actType+
                        " AND ASM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE]+
                        " BETWEEN '"+startDate+"' AND '"+endDate+"'";
               
               System.out.println("SQL getTotAmount() ::: "+sql);         
               dbrs = DBHandler.execQueryResult(sql);
               ResultSet rs = dbrs.getResultSet();
               while(rs.next()){
                    dResult = rs.getDouble("TOTPMAMOUNT");
               }
        }catch(Exception e){
            dResult = 0;
            System.out.println("Exception on getTotAmount() ::: "+ e.toString());
        }
        return dResult;
   }
   
   public static Vector getSupportData(){
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1,1);
        Date endDate = new Date();
        Date startDate = (Date)endDate.clone();
        startDate.setDate(1);
        String stDate = Formater.formatDate(startDate, "yyyy-MM-dd");
        String ndDate = Formater.formatDate(endDate, "yyyy-MM-dd");
        
        try{
            String sql = " SELECT IV."+PstActivity.fieldNames[PstActivity.FLD_CODE]+" AS CODE "+
                        ", IV."+PstActivity.fieldNames[PstActivity.FLD_DESCRIPTION]+" AS DESCRIPTION "+
                        ", SUM(JA."+PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_AMOUNT]+" AS AMOUNT "+
                        "  FROM "+PstSpecialJournalDetailAssignt.TBL_AISO_JDETAIL_ASSIGNT+" AS JA "+
                        "  INNER JOIN "+PstActivity.TBL_AISO_ACTIVITY+" AS IV"+
                        "  ON IV."+PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]+
                        "  = JA."+PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_ACTIVITY_ID]+
                        "  INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS ASJ "+
                        "  ON JA."+PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_JOURNAL_DETAIL_ID]+
                        "  = ASJ."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_DETAIL_ID]+
                        "  INNER JOIN "+PstSpecialJournalMain.TBL_AISO_SPC_JMAIN+" AS ASM "+
                        "  ON ASM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
                        "  = ASJ."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]+
                        "  WHERE IV."+PstActivity.fieldNames[PstActivity.FLD_ACT_TYPE]+
                        "  = "+PstActivity.ACT_SUPPORT+
                        "  AND "+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE]+
                        "  BETWEEN '"+stDate+"' AND '"+ndDate+"' "+
                        "  GROUP BY VI."+PstActivity.fieldNames[PstActivity.FLD_CODE]+
                        ", VI."+PstActivity.fieldNames[PstActivity.FLD_DESCRIPTION]+
                        "  ORDER BY "+PstActivity.fieldNames[PstActivity.FLD_CODE];
            
            System.out.println("SQL getSupportData() ::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                DonorReport objDonorReport = new DonorReport();
                
                objDonorReport.setActCode(rs.getString("CODE"));
                objDonorReport.setActDescription(rs.getString("DESCRIPTION"));
                objDonorReport.setMtdAmount(rs.getDouble("AMOUNT"));
                
                vResult.add(objDonorReport);
            }
        }catch(Exception e){
            vResult = new Vector(1,1);
            System.out.println("Exception on getSupportData() ::: "+e.toString());
        }
        return vResult;
   }
}
