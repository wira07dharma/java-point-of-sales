/*
 * SessAnalysis.java
 * @author  rusdianta
 * Created on April 11, 2005, 4:43 PM
 */

package com.dimata.aiso.session.analysis;

import java.util.*;
import java.sql.*;

import com.dimata.util.Formater;

import com.dimata.aiso.db.*;
import com.dimata.aiso.entity.masterdata.*;
import com.dimata.aiso.entity.jurnal.*; 
import com.dimata.aiso.entity.periode.*;
import com.dimata.aiso.entity.analysis.*;
import com.dimata.aiso.session.periode.*;
import com.dimata.aiso.session.jurnal.*;

import com.dimata.harisma.entity.masterdata.Department;
import com.dimata.harisma.entity.masterdata.PstDepartment;

public class SessAnalysis 
{    
    private static final String TBL_ANALYSIS = "aiso_report_analysis";
    
    private static final int FLD_ACCOUNT_OID = 0;
    private static final int FLD_ACCOUNT_NAME = 1;
    private static final int FLD_ACCOUNT_ENGLISH = 2;
    private static final int FLD_DEBET = 3;
    private static final int FLD_KREDIT = 4;
    private static final int FLD_ACCOUNT_SIGN = 5;
    
    private static final String fieldNames[] = {
        "ACCOUNT_OID",
        "ACCOUNT_NAME",
        "ACCOUNT_ENGLISH",
        "DEBET",
        "KREDIT",
        "ACCOUNT_SIGN" 
    };       

    
    /**      
     * @param <CODE>iAccountType</CODE>account group like "ACTIVA", "BANK" or others
     * @param <CODE>lPeriodOid</CODE>oid of period
     * @param <CODE>language</CODE>language: 0=indonesia, 1=english
     * @return vector of ReportAnalysis object
     */        
    public static Vector generateReportPeriod(int iAccountType, long lPeriodOid, int language)
    {
        Vector vectResults = new Vector();        
        if (clearTable()) 
        {
            getAccountSaldoPeriod(iAccountType, lPeriodOid);
            getAccountValuePeriod(iAccountType, lPeriodOid);
            vectResults = getTotalAccountValue(iAccountType, language);
            clearTable();
        }
        else 
        {
            System.out.println(".:: SessAnalysis.generateReportPeriod() : Cannot clear temporary table ...");
        }
        return vectResults;
    }

    
    /** 
     * @param <CODE>iAccountType</CODE>type of account like "ACTIVA", "PETTY CASH" or others
     * @param <CODE>iYear</CODE>year of report
     * @param <CODE>language</CODE>0=indonesia, 1=english
     * @return vector of ReportAnalysis object
     */        
    public static Vector generateReportYear(int iAccountType, int iYear, int language)
    {
        Vector vectResults = new Vector();        
        if (clearTable()) 
        {
            getAccountSaldoYear(iAccountType, iYear);
            getAccountValueYear(iAccountType, iYear);
            vectResults = getTotalAccountValue(iAccountType, language);
            clearTable();
        }
        else 
        {
            System.out.println(".:: SessAnalysis.generateReportYear() : Cannot clear temporary table ...");
        }
        return vectResults;
    }
    
    
    /** 
     * @param <CODE>iAccountType</CODE>account group like "ACTIVA", "BANK" or others
     * @param <CODE>lPeriodOid</CODE>oid of actual period
     */        
    private static void getAccountSaldoPeriod(int iAccountType, long lPeriodOid)
    {
        java.util.Date date = PstPeriode.getFirstDateOfPeriod(lPeriodOid);
        getAccountSaldo(iAccountType, date);
    }
    
    
    
    /** 
     * @param <CODE>iAccountType</CODE>account group like "CURRENT LIABILITIES", "ACTIVA" or others
     * @param <CODE>lPeriodOid</CODE>oid of period
     */        
    private static void getAccountValuePeriod(int iAccountType, long lPeriodOid)
    {
        java.util.Date dateRanges[] = PstPeriode.getPeriodRange(lPeriodOid);
        java.util.Date firstDate = dateRanges[0];
        java.util.Date lastDate = dateRanges[1];        
        getAccountValue(iAccountType, firstDate, lastDate);
    }

    

    /** 
     * @param <CODE>iAccountType</CODE>account group like "ACTIVA", "PETTY CASH" or others
     * @param <CODE>iYear</CODE>actual year
     */        
    private static void getAccountSaldoYear(int iAccountType, int iYear)
    {
        java.util.Date date = new java.util.Date(iYear - 1900, 0, 1);
        getAccountSaldo(iAccountType, date);
    }

    
    
    /** 
     * @param <CODE>iAccountType</CODE>account group like "CURRENT LIABILITIES", "ACTIVA" or others
     * @param <CODE>iYear</CODE>year of transaction
     */        
    private static void getAccountValueYear(int iAccountType, int iYear)
    {
        java.util.Date firstDate = new java.util.Date(iYear - 1900, 0, 1);
        java.util.Date lastDate = new java.util.Date(iYear - 1900, 11, 31);        
        getAccountValue(iAccountType, firstDate, lastDate);
    }    

    
    
    /**
     * @return true if all data in "analysis" table have removed
     */        
    private static boolean clearTable() 
    {
        boolean isClear = false;
        try 
        {
            String sql = "DELETE FROM " + SessAnalysis.TBL_ANALYSIS;
            int status = DBHandler.execUpdate(sql);
            isClear = true;
        }
        catch (Exception error) 
        {
            System.out.println(".:: " + new SessAnalysis().getClass().getName() + ".clearTable() : " + error.toString());
        }
        return isClear;
    }
    
    
    
    /**
     * @param <CODE>iAccountType</CODE>type of account like ACTIVA, PETTY CASH, CURRENT LIABILITIES or others
     * @param <CODE>firstDate</CODE>first date of transaction
     * @param <CODE>lastDate</CODE>last date of transaction
     */        
    private static void getAccountValue(int iAccountType, java.util.Date firstDate, java.util.Date lastDate)
    {
        try 
        {   
            String sql = "";     
            
            String sFirstDate = "";
            String sLastDate = "";                        
            if( firstDate!=null && lastDate!=null )
            {
                sFirstDate = Formater.formatDate(firstDate,"yyyy-MM-dd");
                sLastDate = Formater.formatDate(lastDate,"yyyy-MM-dd");                        
                   
                if( (sFirstDate!=null && sFirstDate.length()>0) && (sLastDate!=null && sLastDate.length()>0) )
                {
                    // jika account group adalah KAS atau BANK, maka select dari account link
                    if( iAccountType==PstPerkiraan.ACC_GROUP_CASH || iAccountType==PstPerkiraan.ACC_GROUP_BANK )
                    {
                       sql = "INSERT INTO " + SessAnalysis.TBL_ANALYSIS + 
                             " SELECT ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                             ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                             ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                             ", SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET] + ") AS DEBET" +
                             ", SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT] + ") KREDIT" +
                             ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                             " FROM " + PstJurnalDetail.TBL_JURNAL_DETAIL + " AS JD" + 
                             " INNER JOIN " + PstJurnalUmum.TBL_JURNAL_UMUM + " AS JU" +
                             " ON JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                             " = JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID] +
                             " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" + 
                             " ON ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                             " = JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] +
                             " INNER JOIN " + PstAccountLink.TBL_ACCOUNT_LINK + " AS LNK" + 
                             " ON LNK." + PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID] +
                             " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                             " WHERE LNK." + PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE] +
                             " = " + iAccountType +
                             " AND JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI] +
                             " BETWEEN '" + sFirstDate + "'" + 
                             " AND '" + sLastDate + "'" + 
                             " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];                
                    }


                    // jika account group adalah selain KAS atau BANK, maka select dari perkiraan
                    else
                    {
                       sql = "INSERT INTO " + SessAnalysis.TBL_ANALYSIS + 
                             " SELECT ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                             ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                             ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                             ", SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET] + ") AS DEBET" +
                             ", SUM(JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT] + ") KREDIT" +
                             ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                             " FROM " + PstJurnalDetail.TBL_JURNAL_DETAIL + " AS JD" + 
                             " INNER JOIN " + PstJurnalUmum.TBL_JURNAL_UMUM + " AS JU" +
                             " ON JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                             " = JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID] +
                             " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" + 
                             " ON ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                             " = JD." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] +
                             " WHERE ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                             " = " + iAccountType +
                             " AND JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI] +
                             " BETWEEN '" + sFirstDate + "'" + 
                             " AND '" + sLastDate + "'" + 
                             " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];                                
                    }            
                }
                
                if(sql!=null && sql.length()>0)
                {
                    DBHandler.execUpdate(sql);      
                }
            }
        }
        catch (Exception error) 
        {
            System.out.println(".:: " + new SessAnalysis().getClass().getName() + ".getAccountValue() : " + error.toString());
        }
    }
    
    
    
    /** 
     * @param <CODE>iAccountType</CODE>account group like "ACTIVA", "CURRENT LIABILITIES" or others
     * @param <CODE>actualDate</CODE>actual date
     */        
    private static void getAccountSaldo(int iAccountType, java.util.Date actualDate)
    {
        long lPeriodOid = PstPeriode.getPeriodOidBefore(actualDate);
        if (lPeriodOid != 0) 
        {
            try 
            {
                String sql = "";
                
                // jika account group adalah KAS atau BANK, maka select dari account link
                if( iAccountType==PstPerkiraan.ACC_GROUP_CASH || iAccountType==PstPerkiraan.ACC_GROUP_BANK )
                {                
                    sql = "INSERT INTO " + SessAnalysis.TBL_ANALYSIS + 
                          " SELECT ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] + 
                          ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] + 
                          ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] + 
                          ", SUM(SAP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET] + ") AS DEBET" +
                          ", SUM(SAP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT] + ") AS KREDIT" + 
                          ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] + 
                          " FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR + " AS SAP" +
                          " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                          " ON ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] + 
                          " = SAP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] + 
                          " INNER JOIN " + PstAccountLink.TBL_ACCOUNT_LINK + " AS AL" + 
                          " ON AL." + PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID] + 
                          " = ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] + 
                          " WHERE SAP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] + 
                          " = " + lPeriodOid + 
                          " AND AL." + PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE] + 
                          " = " + iAccountType + 
                          " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] + 
                          " ORDER BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];
                }
                
                // jika account group adalah selain KAS atau BANK, maka select dari perkiraan
                else
                {
                    sql = "INSERT INTO " + SessAnalysis.TBL_ANALYSIS + 
                          " SELECT ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] + 
                          ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] + 
                          ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] + 
                          ", SUM(SAP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_DEBET] + ") AS DEBET" +
                          ", SUM(SAP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_KREDIT] + ") AS KREDIT" + 
                          ", ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] + 
                          " FROM " + PstSaldoAkhirPeriode.TBL_SALDO_AKHIR + " AS SAP" +
                          " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                          " ON ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] + 
                          " = SAP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERKIRAAN] + 
                          " WHERE SAP." + PstSaldoAkhirPeriode.fieldNames[PstSaldoAkhirPeriode.FLD_IDPERIODE] + 
                          " = " + lPeriodOid + 
                          " AND ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] + 
                          " = " + iAccountType + 
                          " GROUP BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] + 
                          " ORDER BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];
                }
                
                if( sql!=null && sql.length()>0 )
                {
                    DBHandler.execUpdate(sql);                              
                }                
            }
            catch (Exception error) 
            {
                System.out.println(".:: " + new SessAnalysis().getClass().getName() + ".getAccountSaldo() : " + error.toString());
            }
            
        }    
    }
    
    
    
    /**      
     * @param <CODE>iAccountType</CODE>
     * @param <CODE>language</CODE>0=indonesia, 1=english
     * @return vector of ReportAnalysis object
     */        
    private static Vector getTotalAccountValue(int iAccountType, int language)
    {
        Vector vectResults = new Vector();
        
        DBResultSet dbrs = null;        
        try 
        {
            String fieldAccountName = SessAnalysis.fieldNames[SessAnalysis.FLD_ACCOUNT_NAME];
            if (language == com.dimata.util.lang.I_Language.LANGUAGE_FOREIGN)
            {
                fieldAccountName = SessAnalysis.fieldNames[SessAnalysis.FLD_ACCOUNT_ENGLISH];
            }
            
            String sql = ""; 

            // jika account group adalah KAS atau BANK, maka select dari account link
            if( iAccountType==PstPerkiraan.ACC_GROUP_CASH || iAccountType==PstPerkiraan.ACC_GROUP_BANK )
            {                
               sql = "SELECT A." + fieldAccountName +
                     ", SUM(A." + SessAnalysis.fieldNames[SessAnalysis.FLD_DEBET] + ") AS DEBET" +
                     ", SUM(A." + SessAnalysis.fieldNames[SessAnalysis.FLD_KREDIT] + ") AS KREDIT" +
                     ", A." + SessAnalysis.fieldNames[SessAnalysis.FLD_ACCOUNT_SIGN] +
                     " FROM " + SessAnalysis.TBL_ANALYSIS + " AS A" +
                     " INNER JOIN " + PstAccountLink.TBL_ACCOUNT_LINK + " AS AL" +
                     " ON AL." + PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID] +
                     " = A." + SessAnalysis.fieldNames[SessAnalysis.FLD_ACCOUNT_OID] +
                     " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                     " ON ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                     " = A." + SessAnalysis.fieldNames[SessAnalysis.FLD_ACCOUNT_OID] + 
                     " WHERE AL." + PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE] +
                     " = " + iAccountType +
                     " GROUP BY A." + SessAnalysis.fieldNames[SessAnalysis.FLD_ACCOUNT_OID] +
                     " ORDER BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];                
            }
            
            
            // jika account group adalah selain KAS atau BANK, maka select dari perkiraan
            else
            {
               sql = "SELECT A." + fieldAccountName +
                     ", SUM(A." + SessAnalysis.fieldNames[SessAnalysis.FLD_DEBET] + ") AS DEBET" +
                     ", SUM(A." + SessAnalysis.fieldNames[SessAnalysis.FLD_KREDIT] + ") AS KREDIT" +
                     ", A." + SessAnalysis.fieldNames[SessAnalysis.FLD_ACCOUNT_SIGN] +
                     " FROM " + SessAnalysis.TBL_ANALYSIS + " AS A" +
                     " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS ACC" +
                     " ON ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                     " = A." + SessAnalysis.fieldNames[SessAnalysis.FLD_ACCOUNT_OID] + 
                     " WHERE ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                     " = " + iAccountType +
                     " GROUP BY A." + SessAnalysis.fieldNames[SessAnalysis.FLD_ACCOUNT_OID] +
                     " ORDER BY ACC." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN];                                
            }            
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) 
            {                
                String accountName = rs.getString(fieldAccountName);
                double debet = rs.getDouble(SessAnalysis.fieldNames[SessAnalysis.FLD_DEBET]);
                double kredit = rs.getDouble(SessAnalysis.fieldNames[SessAnalysis.FLD_KREDIT]);
                int accountSign = rs.getInt(SessAnalysis.fieldNames[SessAnalysis.FLD_ACCOUNT_SIGN]);
                
                ReportAnalysis ra = new ReportAnalysis();
                ra.setAccountName(accountName);
                ra.setAccountSign(accountSign);
                
                if (accountSign == PstPerkiraan.ACC_DEBETSIGN) 
                {
                    debet -= kredit;
                    if (debet == 0)
                    {
                        continue;
                    }
                    
                    ra.setAmount(debet);
                }
                else 
                {
                    kredit -= debet;
                    if (kredit == 0)
                    {
                        continue;
                    }
                    
                    ra.setAmount(kredit);
                }
                
                vectResults.add(ra);
            }
            
        }
        catch (Exception error) 
        {
            System.out.println(".:: " + new SessAnalysis().getClass().getName() + ".getTotalAccountValue() : " + error.toString());
        }
        finally 
        {
            DBResultSet.close(dbrs);
        }
        return vectResults;
    }    
    

    
    /**
     * mencari saldo account "Laba (Rugi) Tahun Berjalan" pada periode tertentu
     * @param <CODE>lPeriodOid</CODE>
     * @param <CODE>iBookType</CODE>
     * @param <CODE>language</CODE>
     * @return
     */    
    public static ReportAnalysis getProfitPeriod(long lPeriodOid, int language)
    {
        // algoritma : 
        // 1. looping sebanyak department yg ada
        // 2. cari saldo account "Current Earning Year" dari saldo akhir periode pada periode "terakhir" pada tahun ini
        // 3. cari saldo account "Current Earning Year" dari jurnal pada periode ini
        // 4. jumlahkan saldonya
        // 5. return object ReportAnalysis menggunakan nilai saldo (no 4) yang diperoleh        
        ReportAnalysis ra = new ReportAnalysis();
        
        Vector vDept = PstDepartment.list(0, 0, "", "");
        if(vDept!=null && vDept.size()>0)
        {
            double dProfitBalance = 0;
            int iDeptCount = vDept.size();
            for(int i=0; i<iDeptCount; i++)
            {
                Department objDepartment = (Department) vDept.get(i);
                
                AccountLink objAccountLink = PstAccountLink.getObjAccountLink(objDepartment.getOID(), PstPerkiraan.ACC_GROUP_YEAR_EARNING);
                long lCurrEarnYearAccOid = objAccountLink.getFirstId();                

 
                // 1. cari saldo account "Current Earning Year" dari saldo akhir periode pada periode "sebelum" periode ini        
                double dEarningYearBalanceBeforePeriod = 0;
                long lPeriodOidJustBefore = PstPeriode.getPeriodIdJustBefore(lPeriodOid);
                if(lPeriodOidJustBefore != 0)
                {
                    SessSaldoAkhirPeriode objSessSaldoAkhirPeriode = new SessSaldoAkhirPeriode();
                    dEarningYearBalanceBeforePeriod = objSessSaldoAkhirPeriode.getAccountBalance(lPeriodOidJustBefore, lPeriodOid);
                }

                // 2. cari saldo account "Current Earning Year" dari jurnal pada periode ini
                Vector vJurnalType = new Vector(1,1);
                vJurnalType.add(""+PstJurnalUmum.TIPE_JURNAL_UMUM);
                vJurnalType.add(""+PstJurnalUmum.TIPE_JURNAL_PENUTUP_2);                               
                SessJurnal objSessJurnal = new SessJurnal();                
                double dEarningYearBalanceCurrPeriod = objSessJurnal.getAccountBalance(lCurrEarnYearAccOid, vJurnalType, lPeriodOid);
                
                
                // 3. jumlahkan saldonya
                dProfitBalance = dProfitBalance + dEarningYearBalanceBeforePeriod + dEarningYearBalanceCurrPeriod;
            }

            if (language == com.dimata.util.lang.I_Language.LANGUAGE_FOREIGN)
            {
                ra.setAccountName("Current Earning Year");
            }
            else
            {
                ra.setAccountName("Laba Tahun Berjalan");
            }
            ra.setAmount(dProfitBalance);
            ra.setAccountSign(PstPerkiraan.ACC_KREDITSIGN);
        }
        
        return ra;
    }
    
    
    
    /**
     * mencari saldo account "Laba (Rugi) Tahun Berjalan" pada tahun tertentu
     * @param <CODE>iYear</CODE>
     * @param <CODE>iBookType</CODE>
     * @param <CODE>language</CODE>
     * @return
     */    
    public static ReportAnalysis getProfitYear(int iYear, int language)
    {           
        java.util.Date selectedDate = new java.util.Date(iYear - 1900, 0, 1);   
        long lPeriodOid = PstPeriode.getLastPeriodOidOnYear(selectedDate);         
        if( lPeriodOid != 0)
        {
            return getProfitPeriod(lPeriodOid, language);                
        }
        else
        {
            return new ReportAnalysis();
        }        
    }
}
