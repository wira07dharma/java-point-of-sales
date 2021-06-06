/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Aug 22, 2005
 * Time: 4:14:04 PM
 * To change this template use Options | File Templates.
 */
package com.dimata.aiso.session.report;
 
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.entity.masterdata.PstPerkiraan;
import com.dimata.aiso.entity.masterdata.PstAccountLink;
import com.dimata.aiso.entity.masterdata.Perkiraan;
import com.dimata.aiso.entity.jurnal.PstJurnalDetail;
import com.dimata.aiso.entity.jurnal.PstJurnalUmum;
import com.dimata.aiso.entity.report.CashFlow;
import com.dimata.aiso.entity.search.SrcCashFlow;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalDetail;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalMain;
import com.dimata.aiso.session.jurnal.SessJurnal;
import com.dimata.interfaces.chartofaccount.I_ChartOfAccountGroup;
import com.dimata.interfaces.journal.I_JournalType;
import com.dimata.util.Formater;

import java.util.Vector;
import java.sql.ResultSet;
import java.util.*;

public class SessCashFlow {

    public static String[][] fieldNames = {
        {"1", "2"}, {"1", "2"}
    };

    public static String[][] reportFieldNames = {
        {"1", "2"}, {"1", "2"}
    };

    /** gadnyana
     * Untuk mencari laporan cash
     * flow yang berdasarkan oid department
     * yang menampilkan nomor jurnalnya
     * @param oidPeriod
     * @param departmentOid
     * @param fieldColumDebetOrKredit
     * @return
     */
    synchronized public static Vector getListCashFlow(long oidPeriod, long departmentOid, int fieldColumDebetOrKredit) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_VOUCHER] +
                    " ,JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_COUNTER] +
                    " ,JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                    " ,JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_KETERANGAN] +
                    " ,DT." + PstJurnalDetail.fieldNames[fieldColumDebetOrKredit] +
                    " FROM " + PstJurnalUmum.TBL_JURNAL_UMUM + " AS JU " +
                    " INNER JOIN " + PstJurnalDetail.TBL_JURNAL_DETAIL + " AS DT " +
                    " ON JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                    " = DT." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID] +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS P ON " +
                    " DT." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] +
                    " = P." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " INNER JOIN " + PstAccountLink.TBL_ACCOUNT_LINK + " AS L " +
                    " ON DT." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] +
                    " = L." + PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID];
            sql = sql + " WHERE L." + PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE] +
                    " = " + PstPerkiraan.ACC_GROUP_CASH +
                    " AND P." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_DEPARTMENT_ID] + " = " + departmentOid +
                    " AND JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID] + " = " + oidPeriod +
                    " AND JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE] + " = " + PstJurnalUmum.TIPE_JURNAL_UMUM +
                    " AND DT." + PstJurnalDetail.fieldNames[fieldColumDebetOrKredit] + " > 0 ";
            sql = sql + " ORDER BY JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_COUNTER];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                CashFlow cashFlow = new CashFlow();
                cashFlow.setKeterangan(rs.getString(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_KETERANGAN]));
                String nmr = rs.getString(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_VOUCHER]) + "-" + SessJurnal.intToStr(rs.getInt(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_COUNTER]), 4);
                cashFlow.setNoVoucher(nmr);
                cashFlow.setValue(rs.getDouble(PstJurnalDetail.fieldNames[fieldColumDebetOrKredit]));

                result.add(cashFlow);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("SessCashFlow - getListWorkSheet : " + e.toString());
        }
        return result;
    }

    /** gadnyana
     * untuk mencari data cash flow yang
     * menampilkan dengan account2 nya.
     * @param oidPeriod
     * @param departmentOid
     * @param fieldColumDebetOrKredit
     * @return
     */
    synchronized public static Vector getListAccountCashFlow(long oidPeriod,
                                                             long departmentOid,
                                                             int fieldColumDebetOrKredit) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT " +
                    " JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_VOUCHER] +
                    " ,JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_COUNTER] +
                    " ,JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                    " ,JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_KETERANGAN] +
                    " ,DT." + PstJurnalDetail.fieldNames[fieldColumDebetOrKredit] +
                    " ,P." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                    " ,P." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    " ,P." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                    " FROM " + PstJurnalUmum.TBL_JURNAL_UMUM + " AS JU " +
                    " INNER JOIN " + PstJurnalDetail.TBL_JURNAL_DETAIL + " AS DT " +
                    " ON JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID] +
                    " = DT." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID] +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS P ON " +
                    " DT." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] +
                    " = P." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " INNER JOIN " + PstAccountLink.TBL_ACCOUNT_LINK + " AS L " +
                    " ON DT." + PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN] +
                    " = L." + PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID];
            sql = sql + " WHERE L." + PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE] +
                    " = " + PstPerkiraan.ACC_GROUP_CASH +
                    " AND P." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_DEPARTMENT_ID] + " = " + departmentOid +
                    " AND JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID] + " = " + oidPeriod +
                    " AND JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE] + " = " + PstJurnalUmum.TIPE_JURNAL_UMUM +
                    " AND DT." + PstJurnalDetail.fieldNames[fieldColumDebetOrKredit] + " > 0 ";
            sql = sql + " ORDER BY JU." + PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_COUNTER];

            //System.out.println("INI SQL UNTUK METHOD getListAccountCashFlow"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vt = new Vector();
                CashFlow cashFlow = new CashFlow();
                cashFlow.setKeterangan(rs.getString(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_KETERANGAN]));
                String nmr = rs.getString(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_VOUCHER]) + "-" + SessJurnal.intToStr(rs.getInt(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_COUNTER]), 4);
                cashFlow.setNoVoucher(nmr);
                cashFlow.setValue(rs.getDouble(PstJurnalDetail.fieldNames[fieldColumDebetOrKredit]));
                vt.add(cashFlow);

                Perkiraan perkiraan = new Perkiraan();
                perkiraan.setNoPerkiraan(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]));
                perkiraan.setNama(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]));
                perkiraan.setAccountNameEnglish(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]));
                vt.add(perkiraan);

                result.add(vt);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("SessCashFlow - getListWorkSheet : " + e.toString());
        }
        return result;
    }
    
    synchronized public static Vector getListAccountCashFlowDebetCash(long oidPeriod, long departmentOid, SrcCashFlow srcCashFlow){
        DBResultSet dbrs = null;
        Vector addCash = new Vector(1, 1);
        
        try{
            String sql = "SELECT TGL_TRANSAKSI, REFERENCE_DOCUMENT, JOURNAL_NUMBER" +
                        " ,NOMOR_PERKIRAAN, NAMA, ACCOUNT_NAME_ENGLISH,KETERANGAN, KREDIT, ID_PERKIRAAN" +
                        " FROM v_cash_flow_debet" +
                        " WHERE PERIODE_ID = "+oidPeriod+" AND LINK_TYPE = "+I_ChartOfAccountGroup.ACC_GROUP_CASH;
            if(departmentOid != 0){            
                    sql +=" AND DEPARTMENT_ID = "+departmentOid;                       
            }
            
            if(srcCashFlow.getDateFrom() != null && srcCashFlow.getDateTo() != null){
                    if(srcCashFlow.getDateTo().after(srcCashFlow.getDateFrom()) || srcCashFlow.getDateTo().equals(srcCashFlow.getDateFrom())){
                        sql += " AND TGL_TRANSAKSI BETWEEN '"+Formater.formatDate(srcCashFlow.getDateFrom(),"yyyy-MM-dd")+"' "+
                                " AND '"+Formater.formatDate(srcCashFlow.getDateTo(),"yyyy-MM-dd")+"' ";
                    }
             }
            
                sql +=  " ORDER BY TGL_TRANSAKSI, REFERENCE_DOCUMENT, JOURNAL_NUMBER" +
                        " ,NOMOR_PERKIRAAN, NAMA, KETERANGAN";
                
            //System.out.println("SQL UNTUK METHOD getListAccountCashFlowDebetCash ==> "+sql);
            dbrs = DBHandler.execQueryResult(sql);             
            ResultSet rs = dbrs.getResultSet();
             
            
            while (rs.next()) {                 
                CashFlow cashFlw = new CashFlow();
                cashFlw.setTglTransaksi(rs.getDate("TGL_TRANSAKSI"));  
                cashFlw.setNoDocRef(rs.getString("REFERENCE_DOCUMENT"));  
                cashFlw.setNoVoucher(rs.getString("JOURNAL_NUMBER"));
                cashFlw.setNoPerkiraan(rs.getString("NOMOR_PERKIRAAN"));  
                cashFlw.setNamaPerkiraan(rs.getString("NAMA")); 
                cashFlw.setAccountNameEnglish(rs.getString("ACCOUNT_NAME_ENGLISH"));
                cashFlw.setKeterangan(rs.getString("KETERANGAN"));
                cashFlw.setValue(rs.getDouble("KREDIT"));   
                cashFlw.setIdPerkiraan(rs.getLong("ID_PERKIRAAN"));
                  
                addCash.add(cashFlw);
                
      
               
            }
        
        }catch(Exception e){
            System.out.println("SessCashFlow - getListAccountCashFlowDebetCash : " + e.toString());
        }
        return addCash;
    }
    
     synchronized public static Vector getListAccountCashFlowDebetBank(long oidPeriod, long departmentOid, SrcCashFlow srcCashFlow){
        DBResultSet dbrs = null;
        Vector addBank = new Vector(1, 1);
        
        try{
            String sql = "SELECT TGL_TRANSAKSI, REFERENCE_DOCUMENT, JOURNAL_NUMBER" +
                        " ,NOMOR_PERKIRAAN, NAMA, ACCOUNT_NAME_ENGLISH,KETERANGAN, KREDIT, ID_PERKIRAAN" +
                        " FROM v_cash_flow_debet" +
                        " WHERE PERIODE_ID = "+oidPeriod+" AND LINK_TYPE = "+I_ChartOfAccountGroup.ACC_GROUP_BANK;
            if(departmentOid != 0){            
               sql = sql +" AND DEPARTMENT_ID = "+departmentOid;
            }
            
             if(srcCashFlow.getDateFrom() != null && srcCashFlow.getDateTo() != null){
                    if(srcCashFlow.getDateTo().after(srcCashFlow.getDateFrom()) || srcCashFlow.getDateTo().equals(srcCashFlow.getDateFrom())){
                        sql += " AND TGL_TRANSAKSI BETWEEN '"+Formater.formatDate(srcCashFlow.getDateFrom(),"yyyy-MM-dd")+"' "+
                                " AND '"+Formater.formatDate(srcCashFlow.getDateTo(),"yyyy-MM-dd")+"' ";
                    }
             }
            
                sql +=  " ORDER BY TGL_TRANSAKSI, REFERENCE_DOCUMENT, JOURNAL_NUMBER" +
                        " ,NOMOR_PERKIRAAN, NAMA, KETERANGAN";
            
            //System.out.println("SQL UNTUK METHOD getListAccountCashFlowDebetBank ==> "+sql);
            dbrs = DBHandler.execQueryResult(sql);             
            ResultSet rs = dbrs.getResultSet();
             
            
            while (rs.next()) {                 
                CashFlow cashFlw = new CashFlow();
                cashFlw.setTglTransaksi(rs.getDate("TGL_TRANSAKSI"));  
                cashFlw.setNoDocRef(rs.getString("REFERENCE_DOCUMENT"));  
                cashFlw.setNoVoucher(rs.getString("JOURNAL_NUMBER"));
                cashFlw.setNoPerkiraan(rs.getString("NOMOR_PERKIRAAN"));  
                cashFlw.setNamaPerkiraan(rs.getString("NAMA")); 
                cashFlw.setAccountNameEnglish(rs.getString("ACCOUNT_NAME_ENGLISH"));
                cashFlw.setKeterangan(rs.getString("KETERANGAN"));
                cashFlw.setValue(rs.getDouble("KREDIT"));
                cashFlw.setIdPerkiraan(rs.getLong("ID_PERKIRAAN"));
                  
                addBank.add(cashFlw);
            }
        
        }catch(Exception e){
            System.out.println("SessCashFlow - getListAccountCashFlowDebetBank : " + e.toString());
        }
        return addBank;
    }
     
      synchronized public static Vector getListAccountCashFlowDebetPettyCash(long oidPeriod, long departmentOid, SrcCashFlow srcCashFlow){
        DBResultSet dbrs = null;
        Vector addPettyCash = new Vector(1, 1);
        
        try{
            String sql = "SELECT TGL_TRANSAKSI, REFERENCE_DOCUMENT, JOURNAL_NUMBER" +
                        " ,NOMOR_PERKIRAAN, NAMA, ACCOUNT_NAME_ENGLISH,KETERANGAN, KREDIT, ID_PERKIRAAN" +
                        " FROM v_cash_flow_debet" +
                        " WHERE PERIODE_ID = "+oidPeriod+" AND LINK_TYPE = "+I_ChartOfAccountGroup.ACC_GROUP_PETTY_CASH;
            if(departmentOid != 0){            
               sql = sql +" AND DEPARTMENT_ID = "+departmentOid;
            }
            
             if(srcCashFlow.getDateFrom() != null && srcCashFlow.getDateTo() != null){
                    if(srcCashFlow.getDateTo().after(srcCashFlow.getDateFrom()) || srcCashFlow.getDateTo().equals(srcCashFlow.getDateFrom())){
                        sql += " AND TGL_TRANSAKSI BETWEEN '"+Formater.formatDate(srcCashFlow.getDateFrom(),"yyyy-MM-dd")+"' "+
                                " AND '"+Formater.formatDate(srcCashFlow.getDateTo(),"yyyy-MM-dd")+"' ";
                    }
             }
            
                sql +=  " ORDER BY TGL_TRANSAKSI, REFERENCE_DOCUMENT, JOURNAL_NUMBER" +
                        " ,NOMOR_PERKIRAAN, NAMA, KETERANGAN";
            
            //System.out.println("SQL UNTUK METHOD getListAccountCashFlowDebetPettyCash ==> "+sql);
            dbrs = DBHandler.execQueryResult(sql);             
            ResultSet rs = dbrs.getResultSet();
             
            
            while (rs.next()) {                 
                CashFlow cashFlw = new CashFlow();
                cashFlw.setTglTransaksi(rs.getDate("TGL_TRANSAKSI"));  
                cashFlw.setNoDocRef(rs.getString("REFERENCE_DOCUMENT"));  
                cashFlw.setNoVoucher(rs.getString("JOURNAL_NUMBER"));
                cashFlw.setNoPerkiraan(rs.getString("NOMOR_PERKIRAAN"));  
                cashFlw.setNamaPerkiraan(rs.getString("NAMA")); 
                cashFlw.setAccountNameEnglish(rs.getString("ACCOUNT_NAME_ENGLISH"));
                cashFlw.setKeterangan(rs.getString("KETERANGAN"));
                cashFlw.setValue(rs.getDouble("KREDIT")); 
                cashFlw.setIdPerkiraan(rs.getLong("ID_PERKIRAAN"));
                  
                addPettyCash.add(cashFlw);
                
      
               
            }
        
        }catch(Exception e){
            System.out.println("SessCashFlow - getListAccountCashFlowDebetPettyCash : " + e.toString());
        }
        return addPettyCash;
    }
    
     synchronized public static Vector getListAccountCashFlowKreditCash(long oidPeriod, long departmentOid, SrcCashFlow srcCashFlow){
        DBResultSet dbrs = null;
        Vector deductCash = new Vector(1, 1);          
        try{
            String sql = "SELECT TGL_TRANSAKSI, REFERENCE_DOCUMENT, JOURNAL_NUMBER" +
                        " ,NOMOR_PERKIRAAN, NAMA, ACCOUNT_NAME_ENGLISH,KETERANGAN, DEBET, ID_PERKIRAAN" +
                        " FROM v_cash_flow_kredit" +
                        " WHERE PERIODE_ID = "+oidPeriod+" AND LINK_TYPE = "+I_ChartOfAccountGroup.ACC_GROUP_CASH;
            if(departmentOid != 0){
               sql = sql +" AND DEPARTMENT_ID = "+departmentOid;
            }
            
             if(srcCashFlow.getDateFrom() != null && srcCashFlow.getDateTo() != null){
                    if(srcCashFlow.getDateTo().after(srcCashFlow.getDateFrom()) || srcCashFlow.getDateTo().equals(srcCashFlow.getDateFrom())){
                        sql += " AND TGL_TRANSAKSI BETWEEN '"+Formater.formatDate(srcCashFlow.getDateFrom(),"yyyy-MM-dd")+"' "+
                                " AND '"+Formater.formatDate(srcCashFlow.getDateTo(),"yyyy-MM-dd")+"' ";
                    }
             }
            
                sql +=  " ORDER BY TGL_TRANSAKSI, REFERENCE_DOCUMENT, JOURNAL_NUMBER" +
                        " ,NOMOR_PERKIRAAN, NAMA, KETERANGAN";
            
            //System.out.println("SQL UNTUK METHOD getListAccountCashFlowDebet ==> "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                CashFlow casFlw = new CashFlow();
                casFlw.setTglTransaksi(rs.getDate("TGL_TRANSAKSI"));
                casFlw.setNoDocRef(rs.getString("REFERENCE_DOCUMENT"));
                casFlw.setNoVoucher(rs.getString("JOURNAL_NUMBER"));
                casFlw.setNoPerkiraan(rs.getString("NOMOR_PERKIRAAN"));
                casFlw.setNamaPerkiraan(rs.getString("NAMA"));
                casFlw.setAccountNameEnglish(rs.getString("ACCOUNT_NAME_ENGLISH"));
                casFlw.setKeterangan(rs.getString("KETERANGAN"));
                casFlw.setValue(rs.getDouble("DEBET"));
                casFlw.setIdPerkiraan(rs.getLong("ID_PERKIRAAN"));
                
                deductCash.add(casFlw);
            }
        
        }catch(Exception e){
            System.out.println("SessCashFlow - getListAccountCashFlowKredit : " + e.toString());
        }
        return deductCash;
    }
     
     synchronized public static Vector getListAccountCashFlowKreditBank(long oidPeriod, long departmentOid, SrcCashFlow srcCashFlow){
        DBResultSet dbrs = null;
        Vector deductBank = new Vector(1, 1);
        
        try{
            String sql = "SELECT TGL_TRANSAKSI, REFERENCE_DOCUMENT, JOURNAL_NUMBER" +
                        " ,NOMOR_PERKIRAAN, NAMA, ACCOUNT_NAME_ENGLISH,KETERANGAN, DEBET, ID_PERKIRAAN" +
                        " FROM v_cash_flow_kredit" +
                        " WHERE PERIODE_ID = "+oidPeriod+" AND LINK_TYPE = "+I_ChartOfAccountGroup.ACC_GROUP_BANK;
            if(departmentOid != 0){
               sql = sql +" AND DEPARTMENT_ID = "+departmentOid;
            }
            
            if(srcCashFlow.getDateFrom() != null && srcCashFlow.getDateTo() != null){
                    if(srcCashFlow.getDateTo().after(srcCashFlow.getDateFrom()) || srcCashFlow.getDateTo().equals(srcCashFlow.getDateFrom())){
                        sql += " AND TGL_TRANSAKSI BETWEEN '"+Formater.formatDate(srcCashFlow.getDateFrom(),"yyyy-MM-dd")+"' "+
                                " AND '"+Formater.formatDate(srcCashFlow.getDateTo(),"yyyy-MM-dd")+"' ";
                    }
             }
            
                sql +=  " ORDER BY TGL_TRANSAKSI, REFERENCE_DOCUMENT, JOURNAL_NUMBER" +
                        " ,NOMOR_PERKIRAAN, NAMA, KETERANGAN";
            
            //System.out.println("SQL UNTUK METHOD getListAccountCashFlowDebet ==> "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                CashFlow casFlw = new CashFlow();
                casFlw.setTglTransaksi(rs.getDate("TGL_TRANSAKSI"));
                casFlw.setNoDocRef(rs.getString("REFERENCE_DOCUMENT"));
                casFlw.setNoVoucher(rs.getString("JOURNAL_NUMBER"));
                casFlw.setNoPerkiraan(rs.getString("NOMOR_PERKIRAAN"));
                casFlw.setNamaPerkiraan(rs.getString("NAMA"));
                casFlw.setAccountNameEnglish(rs.getString("ACCOUNT_NAME_ENGLISH"));
                casFlw.setKeterangan(rs.getString("KETERANGAN"));
                casFlw.setValue(rs.getDouble("DEBET"));
                casFlw.setIdPerkiraan(rs.getLong("ID_PERKIRAAN"));
                
                deductBank.add(casFlw);
            }
        
        }catch(Exception e){
            System.out.println("SessCashFlow - getListAccountCashFlowKredit : " + e.toString());
        }
        return deductBank;
    }
     
     synchronized public static Vector getListAccountCashFlowKreditPettyCash(long oidPeriod, long departmentOid, SrcCashFlow srcCashFlow){
        DBResultSet dbrs = null;
        Vector deductPettyCash = new Vector(1, 1);
        
        try{
            String sql = "SELECT TGL_TRANSAKSI, REFERENCE_DOCUMENT, JOURNAL_NUMBER" +
                        " ,NOMOR_PERKIRAAN, NAMA, ACCOUNT_NAME_ENGLISH,KETERANGAN, DEBET, ID_PERKIRAAN" +
                        " FROM v_cash_flow_kredit" +
                        " WHERE PERIODE_ID = "+oidPeriod+" AND LINK_TYPE = "+I_ChartOfAccountGroup.ACC_GROUP_PETTY_CASH;
            if(departmentOid != 0){
               sql = sql +" AND DEPARTMENT_ID = "+departmentOid;
            }
            
             if(srcCashFlow.getDateFrom() != null && srcCashFlow.getDateTo() != null){
                    if(srcCashFlow.getDateTo().after(srcCashFlow.getDateFrom()) || srcCashFlow.getDateTo().equals(srcCashFlow.getDateFrom())){
                        sql += " AND TGL_TRANSAKSI BETWEEN '"+Formater.formatDate(srcCashFlow.getDateFrom(),"yyyy-MM-dd")+"' "+
                                " AND '"+Formater.formatDate(srcCashFlow.getDateTo(),"yyyy-MM-dd")+"' ";
                    }
             }
            
                sql +=  " ORDER BY TGL_TRANSAKSI, REFERENCE_DOCUMENT, JOURNAL_NUMBER" +
                        " ,NOMOR_PERKIRAAN, NAMA, KETERANGAN";
            
            //System.out.println("SQL UNTUK METHOD getListAccountCashFlowDebet ==> "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                CashFlow casFlw = new CashFlow();
                casFlw.setTglTransaksi(rs.getDate("TGL_TRANSAKSI"));
                casFlw.setNoDocRef(rs.getString("REFERENCE_DOCUMENT"));
                casFlw.setNoVoucher(rs.getString("JOURNAL_NUMBER"));
                casFlw.setNoPerkiraan(rs.getString("NOMOR_PERKIRAAN"));
                casFlw.setNamaPerkiraan(rs.getString("NAMA"));
                casFlw.setAccountNameEnglish(rs.getString("ACCOUNT_NAME_ENGLISH"));
                casFlw.setKeterangan(rs.getString("KETERANGAN"));
                casFlw.setValue(rs.getDouble("DEBET"));
                casFlw.setIdPerkiraan(rs.getLong("ID_PERKIRAAN"));               
                deductPettyCash.add(casFlw);
            }
        
        }catch(Exception e){
            System.out.println("SessCashFlow - getListAccountCashFlowKredit : " + e.toString());
        }
        return deductPettyCash;
    }

    public static synchronized Vector getListCashFlow(long oidPeriod, long departmentOid, SrcCashFlow srcCashFlow){
	Vector vResult = new Vector(1,1);
	if(DBHandler.DBSVR_TYPE == DBHandler.DBSVR_MYSQL){
	    //vResult = (Vector)getContentReportCashFlow(oidPeriod, srcCashFlow);
            //try it
             vResult = (Vector)getContentReportCashFlow(oidPeriod, departmentOid, srcCashFlow);
	}else{
	    vResult = (Vector)getContentReportCashFlow(oidPeriod, departmentOid, srcCashFlow);
	}
	return vResult;
    }
    
    /** gadnyana
     * untuk pencarian laporan cash flow
     * @param oidPeriod
     * @param departmentOid
     * @return
     */
    public static Vector getContentReportCashFlow(long oidPeriod, long departmentOid, SrcCashFlow srcCashFlow) {
        Vector list = new Vector();
        try {
            // pencarian untuk penambahan kas, pembedanya adalah parameter : PstJurnalDetail.FLD_DEBET
            //Vector listDebet = getListAccountCashFlow(oidPeriod, departmentOid, PstJurnalDetail.FLD_DEBET);
            Vector listDebetCash = getListAccountCashFlowDebetCash(oidPeriod, departmentOid,srcCashFlow);
            Vector listDebetBank = getListAccountCashFlowDebetBank(oidPeriod, departmentOid,srcCashFlow);
            Vector listDebetPettyCash = getListAccountCashFlowDebetPettyCash(oidPeriod, departmentOid,srcCashFlow);
            list.add(listDebetCash);
            list.add(listDebetBank);
            list.add(listDebetPettyCash);
            
            // pencarian untuk pengurangan data, pembedanya adalah parameter : PstJurnalDetail.FLD_KREDIT
            //Vector listKredit = getListAccountCashFlow(oidPeriod, departmentOid, PstJurnalDetail.FLD_KREDIT);
            Vector listKreditCash = getListAccountCashFlowKreditCash(oidPeriod, departmentOid,srcCashFlow);
            Vector listKreditBank = getListAccountCashFlowKreditBank(oidPeriod, departmentOid,srcCashFlow);
            Vector listKreditPettyCash = getListAccountCashFlowKreditPettyCash(oidPeriod, departmentOid,srcCashFlow);
            list.add(listKreditCash);
            list.add(listKreditBank);
            list.add(listKreditPettyCash);
            
        } catch (Exception e) {list = new Vector(1,1);}
        return list;
    }
    
    public static synchronized Vector getContentReportCashFlow(long oidPeriod,SrcCashFlow srcCashFlow){
	Vector vList = new Vector(1,1);
	Vector vDebetCash = new Vector(1,1);
	Vector vDebetBank = new Vector(1,1);
	Vector vDebetPettyCash = new Vector(1,1);
	Vector vCreditCash = new Vector(1,1);
	Vector vCreditBank = new Vector(1,1);
	Vector vCreditPettyCash = new Vector(1,1);
	try{
	    //Get Debet Cash Flow
	    vDebetCash = (Vector)getDebetCash(srcCashFlow, oidPeriod);
	    vDebetBank = (Vector)getDebetBank(srcCashFlow, oidPeriod);
	    vDebetPettyCash = (Vector)getDebetPettyCash(srcCashFlow, oidPeriod);
	    
	    //Get Credit Cash Flow
	    vCreditCash = (Vector)getCreditCash(srcCashFlow, oidPeriod);
	    vCreditBank = (Vector)getCreditBank(srcCashFlow, oidPeriod);
	    vCreditPettyCash = (Vector)getCreditPettyCash(srcCashFlow, oidPeriod);
	    
	    //Add Debet and Credit Cash Flow vector to temprorary vector
	    vList.add(vDebetCash);
	    vList.add(vDebetBank);
	    vList.add(vDebetPettyCash);
	    vList.add(vCreditCash);
	    vList.add(vCreditBank);
	    vList.add(vCreditPettyCash);
	    
	}catch(Exception e){}
	return vList;
    }
    
    public static synchronized Vector getCreditCFFromJU(long departmentOid, SrcCashFlow srcCashFlow){
	Vector vResult = new Vector(1,1);
	    try{
		vResult = (Vector)getDataCFfromJU(PstPerkiraan.ACC_KREDITSIGN, departmentOid, srcCashFlow);
	    }catch(Exception e){}
	return vResult;
    }
    
    public static synchronized Vector getDebetCFFromJU(long departmentOid, SrcCashFlow srcCashFlow){
	Vector vResult = new Vector(1,1);
	    try{
		vResult = (Vector)getDataCFfromJU(PstPerkiraan.ACC_DEBETSIGN, departmentOid, srcCashFlow);
	    }catch(Exception e){}
	return vResult;
    }
    
    public static synchronized Vector getDebetPettyCash(SrcCashFlow srcCashFlow, long departmentOid){
	DBResultSet dbrs = null;
	Vector vResult = new Vector(1,1);
	Vector vDebetJU = new Vector(1,1);
	if(departmentOid != 0){
	    try{
		vDebetJU = (Vector)getDebetCFFromJU(departmentOid, srcCashFlow);
		if(vDebetJU != null && vDebetJU.size() > 0){
		    vResult = (Vector)vDebetJU.get(2);
		}
	    }catch(Exception e){}
	}
	try{
	    String sql = "";
	    sql = (String)getQuerySpecialJournal(3, String.valueOf(I_JournalType.TIPE_SPECIAL_JOURNAL_REPLACEMENT), 
		    PstPerkiraan.ACC_KREDITSIGN, PstPerkiraan.ACC_DEBETSIGN, srcCashFlow, I_ChartOfAccountGroup.ACC_GROUP_PETTY_CASH);
	    //System.out.println("SQL SessCashFlow.getDebetPettyCash ::::::::::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		CashFlow objCashFlow = new CashFlow();
		objCashFlow = (CashFlow)resultToObject(rs, objCashFlow);
		vResult.add(objCashFlow);
	    }
	    rs.close();
	}catch(Exception e){}
	return vResult;
    }
    
    public static synchronized Vector getDebetCash(SrcCashFlow srcCashFlow, long departmentOid){
	DBResultSet dbrs = null;
	Vector vResult = new Vector(1,1);
	Vector vDebetJU = new Vector(1,1);
	if(departmentOid != 0){
	    try{
		vDebetJU = (Vector)getDebetCFFromJU(departmentOid, srcCashFlow);
		if(vDebetJU != null && vDebetJU.size() > 0){
		    vResult = (Vector)vDebetJU.get(0);
		}
	    }catch(Exception e){}
	}
	try{
	    String sql = "";
	    sql = (String)getQuerySpecialJournal(1, String.valueOf(I_JournalType.TIPE_SPECIAL_JOURNAL_CASH), 
		  PstPerkiraan.ACC_DEBETSIGN, PstPerkiraan.ACC_DEBETSIGN, srcCashFlow, I_ChartOfAccountGroup.ACC_GROUP_CASH);
	    //System.out.println("SQL SessCashFlow.getDebetCash :::::::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		CashFlow objCashFlow = new CashFlow();
		objCashFlow = (CashFlow)resultToObject(rs, objCashFlow);
		vResult.add(objCashFlow);
	    }
	    rs.close();
	}catch(Exception e){}
	return vResult;
    }
    
    public static synchronized Vector getDebetBank(Vector vDebet, SrcCashFlow srcCashFlow){
	DBResultSet dbrs = null;	
	try{
	    String sql = "";
	    String sJournalType = String.valueOf(I_JournalType.TIPE_SPECIAL_JOURNAL_BANK)+
				  ", "+String.valueOf(I_JournalType.TIPE_SPECIAL_JOURNAL_BANK_TRANSFER)+
				  ", "+String.valueOf(I_JournalType.TIPE_SPECIAL_JOURNAL_FUND);
	    sql = (String)getQuerySpecialJournal(1, sJournalType, PstPerkiraan.ACC_DEBETSIGN, 
		    PstPerkiraan.ACC_DEBETSIGN, srcCashFlow, I_ChartOfAccountGroup.ACC_GROUP_BANK);
	    //System.out.print("SQL SessCashFlow.getDebetBank1 ::::::::::::::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		CashFlow objCashFlow = new CashFlow();
		objCashFlow = (CashFlow)resultToObject(rs, objCashFlow);
		vDebet.add(objCashFlow);
	    }
	    rs.close();
	}catch(Exception e){}
	return vDebet;
    }
    
    public static synchronized Vector getDebetBank(SrcCashFlow srcCashFlow, long departmentOid){
	DBResultSet dbrs = null;
	Vector vResult = new Vector(1,1);
	Vector vTemp = new Vector(1,1);
	Vector vDebetJU = new Vector(1,1);
	if(departmentOid != 0){
	    try{
		vDebetJU = (Vector)getDebetCFFromJU(departmentOid, srcCashFlow);
		if(vDebetJU != null && vDebetJU.size() > 0){
		    vTemp = (Vector)vDebetJU.get(1);
		}
	    }catch(Exception e){}
	}
	try{
	    String sql = "";
	    sql = (String)getQuerySpecialJournal(2, String.valueOf(I_JournalType.TIPE_SPECIAL_JOURNAL_BANK_TRANSFER), 
		    0, PstPerkiraan.ACC_DEBETSIGN, srcCashFlow, I_ChartOfAccountGroup.ACC_GROUP_BANK);
	    //System.out.println("SQL SessCashFlow.getDebetBank :::::::::::::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		CashFlow objCashFlow = new CashFlow();
		objCashFlow = (CashFlow)resultToObject(rs, objCashFlow);
		vTemp.add(objCashFlow);
	    }
	    rs.close();
	    vResult = (Vector)getDebetBank(vTemp, srcCashFlow);
	}catch(Exception e){}
	return vResult;
    }
    
    public static synchronized Vector getCreditPettyCash(SrcCashFlow srcCashFlow, long departmentOid){
	DBResultSet dbrs = null;
	Vector vResult = new Vector(1,1);
	Vector vCreditJU = new Vector(1,1);
	if(departmentOid != 0){
	    try{
		vCreditJU = (Vector)getCreditCFFromJU(departmentOid, srcCashFlow);
		if(vCreditJU != null && vCreditJU.size() > 0){
		    vResult = (Vector)vCreditJU.get(2);
		}
	    }catch(Exception e){}
	}
	try{
	    String sql = "";
	    sql = (String)getQuerySpecialJournal(1, String.valueOf(I_JournalType.TIPE_SPECIAL_JOURNAL_PETTY_CASH), 
		    PstPerkiraan.ACC_KREDITSIGN, PstPerkiraan.ACC_KREDITSIGN, srcCashFlow, I_ChartOfAccountGroup.ACC_GROUP_PETTY_CASH);
	    //System.out.println("SQL SessCashFlow.getCreditPettyCash ::::::::::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		CashFlow objCashFlow = new CashFlow();
		objCashFlow = (CashFlow)resultToObject(rs, objCashFlow);
		vResult.add(objCashFlow);
	    }
	    rs.close();
	}catch(Exception e){}
	return vResult;
    }
    
    public static synchronized Vector getCreditCash(SrcCashFlow srcCashFlow, long departmentOid){
	DBResultSet dbrs = null;
	Vector vResult = new Vector(1,1);
	Vector vCreditJU = new Vector(1,1);
	if(departmentOid != 0){
	    try{
		vCreditJU = (Vector)getCreditCFFromJU(departmentOid, srcCashFlow);
		if(vCreditJU != null && vCreditJU.size() > 0){
		    vResult = (Vector)vCreditJU.get(0);
		}
	    }catch(Exception e){}
	}
	try{
	    String sql = "";
	    sql = (String)getQuerySpecialJournal(2, String.valueOf(I_JournalType.TIPE_SPECIAL_JOURNAL_BANK), 
		  PstPerkiraan.ACC_DEBETSIGN, PstPerkiraan.ACC_KREDITSIGN, srcCashFlow, I_ChartOfAccountGroup.ACC_GROUP_CASH);
	    //System.out.println("SQL SessCashFlow.getCreditCash() ::::::::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		CashFlow objCashFlow = new CashFlow();
		objCashFlow = (CashFlow)resultToObject(rs, objCashFlow);
		vResult.add(objCashFlow);
	    }
	    rs.close();
	}catch(Exception e){}
	return vResult;
    }
    
    public static synchronized Vector getCreditBank(SrcCashFlow srcCashFlow, long departmentOid){
	DBResultSet dbrs = null;
	Vector vResult = new Vector(1,1);
	Vector vCreditJU = new Vector(1,1);
	if(departmentOid != 0){
	    try{
		vCreditJU = (Vector)getCreditCFFromJU(departmentOid, srcCashFlow);
		if(vCreditJU != null && vCreditJU.size() > 0){
		    vResult = (Vector)vCreditJU.get(1);
		}
	    }catch(Exception e){}
	}
	try{
	    String sql = "";
	    String sJournalType = String.valueOf(I_JournalType.TIPE_SPECIAL_JOURNAL_BANK)+
				  ", "+String.valueOf(I_JournalType.TIPE_SPECIAL_JOURNAL_BANK_TRANSFER)+
				  ", "+String.valueOf(I_JournalType.TIPE_SPECIAL_JOURNAL_REPLACEMENT)+
				  ", "+String.valueOf(I_JournalType.TIPE_SPECIAL_JOURNAL_PAYMENT);
	    sql = (String)getQuerySpecialJournal(1, sJournalType, PstPerkiraan.ACC_KREDITSIGN, 
		    PstPerkiraan.ACC_KREDITSIGN, srcCashFlow, I_ChartOfAccountGroup.ACC_GROUP_BANK);
	    //System.out.println("SQL SessCashFlow.getCreditBank :::::::::::::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		CashFlow objCashFlow = new CashFlow();
		objCashFlow = (CashFlow)resultToObject(rs, objCashFlow);
		vResult.add(objCashFlow);
	    }
	    rs.close();
	}catch(Exception e){}
	return vResult;
    }
    
    public static synchronized Vector getDataCFfromJU(int iStatus, long departmentOid, SrcCashFlow srcCashFlow){
	DBResultSet dbrs = null;
	Vector vResult = new Vector(1,1);
	Vector vCash = new Vector(1,1);
	Vector vBank = new Vector(1,1);
	Vector vPettyCash = new Vector(1,1);
	try{
	    String sql = "";
	    sql = (String)getStringQueryJU(iStatus, departmentOid, srcCashFlow);
	    
	    //System.out.println("SQL SessCashFlow.getDebetCFfronJU :::::::::::::::::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		CashFlow objCashFlow = new CashFlow();
		int linkType = 0;
		linkType = rs.getInt(PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE]);
		objCashFlow = (CashFlow)resultToObject(rs, objCashFlow);
		switch(linkType){
		    case I_ChartOfAccountGroup.ACC_GROUP_CASH:
			vCash.add(objCashFlow);
			break;
		    case I_ChartOfAccountGroup.ACC_GROUP_BANK:
			vBank.add(objCashFlow);
			break;
		    case I_ChartOfAccountGroup.ACC_GROUP_PETTY_CASH:
			vPettyCash.add(objCashFlow);
			break;
		}
	    }
	    rs.close();
	    vResult.add(vCash);
	    vResult.add(vBank);
	    vResult.add(vPettyCash);
	}catch(Exception e){}
	return vResult;
    }
    
    public static synchronized CashFlow resultToObject(ResultSet rs, CashFlow objCashFlow){
	try{
	    objCashFlow.setAccountNameEnglish(rs.getString("ENGLISH"));
	    objCashFlow.setIdPerkiraan(rs.getLong("IDACC"));
	    objCashFlow.setKeterangan(rs.getString("DESC"));
	    objCashFlow.setNamaPerkiraan(rs.getString("NAMA"));
	    objCashFlow.setNoDocRef(rs.getString("REF"));
	    objCashFlow.setNoPerkiraan(rs.getString("ACCCODE"));
	    objCashFlow.setNoVoucher(rs.getString("JOURNALNO"));
	    objCashFlow.setTglTransaksi(rs.getDate("TRANSDATE"));
	    objCashFlow.setValue(rs.getDouble("VALUE"));
	}catch(Exception e){}
	return objCashFlow;	
    }
    
    public static synchronized String getIdJournalUmum(int iStatus, long departmentOid, SrcCashFlow srcCashFlow){
	DBResultSet dbrs = null;
	String sResult = "";
	try{
	    String sql = " SELECT JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]+
			 " FROM "+PstJurnalUmum.TBL_JURNAL_UMUM+" AS JU "+
			 " INNER JOIN "+PstJurnalDetail.TBL_JURNAL_DETAIL+" AS DT "+
			 " ON JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]+
			 " = DT."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID]+
			 " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS P "+
			 " ON DT."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+
			 " = P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
			 " INNER JOIN "+PstAccountLink.TBL_ACCOUNT_LINK+" AS L "+
			 " ON DT."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+
			 " = L."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+
			 " WHERE L."+PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE]+
			 " IN( "+I_ChartOfAccountGroup.ACC_GROUP_CASH+
			 ", "+I_ChartOfAccountGroup.ACC_GROUP_PETTY_CASH+
			 ", "+I_ChartOfAccountGroup.ACC_GROUP_BANK+") "+
			 " AND JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE]+
			 " = "+PstJurnalUmum.TIPE_JURNAL_UMUM;
	    if(iStatus == PstPerkiraan.ACC_DEBETSIGN){
		    sql += " AND DT."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET]+" > 0";
	    }else{
		    sql += " AND DT."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT]+" > 0";
	    }
	    
	    if(departmentOid != 0){
		    sql += " AND JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_DEPARTMENT_ID]+" = "+departmentOid;
	    }
	    
	    if(srcCashFlow.getDateFrom().before(srcCashFlow.getDateTo())){
		    sql += " AND JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI]+
			   " BETWEEN '"+Formater.formatDate(srcCashFlow.getDateFrom(),"yyyy-MM-dd")+
			   "' AND '"+Formater.formatDate(srcCashFlow.getDateTo(),"yyyy-MM-dd")+"' ";
	    }
	    
	    //System.out.println("SQL SessCashFlow.getIdJurnalUmum ::::::::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		long lJournalId = 0;
		lJournalId = rs.getLong(PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]);
		if(lJournalId != 0){
		    if(rs.isLast()){
			sResult += String.valueOf(lJournalId)+") ";
		    }else{
			sResult += String.valueOf(lJournalId)+", ";
		    }
		}
	    }
	}catch(Exception e){}
	return sResult;
    }
    
    public static synchronized String getStringQueryJU(int iStatus, long departmentOid, SrcCashFlow srcCashFlow){	
	String sResult = "";	
	String sJournalId = "";
	try{
	    sJournalId = (String)getIdJournalUmum(iStatus, departmentOid, srcCashFlow);
	}catch(Exception e){}
	try{
	    String sql = " SELECT JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI]+" AS TRANSDATE "+
			 ", JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_REFERENCE_DOCUMENT]+" AS REF "+
			 ", JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_NUMBER]+" AS JOURNALNO "+
			 ", P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]+" AS ACCCODE "+
			 ", DT."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+" AS IDACC "+
			 ", P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+" AS NAMA "+
			 ", P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+" AS ENGLISH "+
			 //", JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_KETERANGAN]+" AS DESC ";
                         //Fix By Mirahu 21Mei2011
                         ", JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_KETERANGAN]+" AS DESCR ";
	    if(iStatus == PstPerkiraan.ACC_DEBETSIGN){
		    sql += ", DT."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET]+" AS VALUE ";
	    }else{
		    sql += ", DT."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT]+" AS VALUE ";
	    }			 
		    sql += ", JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_PERIODEID]+
			 ", JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_DEPARTMENT_ID]+
			 ", L."+PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE]+
			 ", JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE]+
			 " FROM "+PstJurnalUmum.TBL_JURNAL_UMUM+" AS JU "+
			 " INNER JOIN "+PstJurnalDetail.TBL_JURNAL_DETAIL+" AS DT "+
			 " ON JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]+
			 " = DT."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID]+
			 " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS P "+
			 " ON DT."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+
			 " = P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN];
	    if(iStatus == PstPerkiraan.ACC_DEBETSIGN){
		    sql += " INNER JOIN "+PstAccountLink.TBL_ACCOUNT_LINK+" AS L ";
	    }else{
		    sql += " LEFT JOIN "+PstAccountLink.TBL_ACCOUNT_LINK+" AS L ";
	    }
		    sql += " ON DT."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+
			   " = L."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID];
	    
	    String whClause = "";
	    if(sJournalId != null && sJournalId.length() > 0){
		if(whClause != null && whClause.length() > 0){
		    whClause += " AND JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]+	    
				" IN ("+sJournalId;
		}else{
		    whClause +=	" WHERE JU."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]+	    
				" IN ("+sJournalId;
		}	 
	    }
	    
	    if(iStatus == PstPerkiraan.ACC_DEBETSIGN){
		if(whClause != null && whClause.length() > 0){
		    whClause += " AND DT."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT]+	    
				" = 0";
		}else{
		    whClause +=	" WHERE DT."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT]+	    
				" = 0";
		}	
	    }else{
		if(whClause != null && whClause.length() > 0){
		    whClause += " AND DT."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET]+	    
				" = 0";
		}else{
		    whClause +=	" WHERE DT."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET]+	    
				" = 0";
		}	
	    }
	    
	    if(whClause != null && whClause.length() > 0){
		sql += whClause;
	    }
	    
	    if(sql != null && sql.length() > 0){
		sResult = sql;
	    }
	}catch(Exception e){}
	return sResult;
    }
    
    public static synchronized String getQuerySpecialJournal(int iQueryType, String sJournalType, 
	    int iAmountStatus, int iDebetCredit, SrcCashFlow srcCashFlow, int iLinkType){
	String sResult = "";
	try{
	    String sql = " SELECT JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE]+" AS TRANSDATE "+
			 ", JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_REFERENCE]+" AS REF "+
			 ", JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_VOUCHER_NUMBER]+" AS JOURNALNO "+
			 ", P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]+" AS ACCCODE "+
			 ", JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+" AS IDACC "+
			 ", P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+" AS NAMAE "+
			 ", P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+" AS ENGLISH "+
			 //", SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_DESCRIPTION]+" AS DESC "+
                         //Fix by Mirahu 210511
                         ", SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_DESCRIPTION]+" AS DESCR "+
			 ", SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT]+" AS VALUE "+
			 ", JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID]+
			 ", L."+PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE]+
			 ", JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]+
			 " FROM "+PstSpecialJournalMain.TBL_AISO_SPC_JMAIN+" AS JM "+
			 " INNER JOIN "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS SD "+
			 " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
			 " = SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_DETAIL_ID];
	    if(iDebetCredit == PstPerkiraan.ACC_DEBETSIGN){
		switch(iQueryType){
			case 1:
			    sql += " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS P "+
				   " ON SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN]+
				   " = P."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+
				   " INNER JOIN "+PstAccountLink.TBL_ACCOUNT_LINK+" AS L "+
				   " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+
				   " = L."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID];
			break;
			case 2:
			    sql += " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS P "+
				   " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+
				   " = P."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+
				   " INNER JOIN "+PstAccountLink.TBL_ACCOUNT_LINK+" AS L "+
				   " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+
				   " = L."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID];
			break;
			case 3:
			      sql += " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS P "+
				   " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+
				   " = P."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+
				   " INNER JOIN "+PstAccountLink.TBL_ACCOUNT_LINK+" AS L "+
				   " ON SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN]+
				   " = L."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID];
			break;
		}		
	    }else{
		switch(iQueryType){
			case 1:
			    sql += " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS P "+
				   " ON SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN]+
				   " = P."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+
				   " INNER JOIN "+PstAccountLink.TBL_ACCOUNT_LINK+" AS L "+
				   " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+
				   " = L."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID];
			break;
			case 2:
			    sql += " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS P "+
				   " ON JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+
				   " = P."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]+
				   " INNER JOIN "+PstAccountLink.TBL_ACCOUNT_LINK+" AS L "+
				   " ON SD."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN]+
				   " = L."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID];
			break;			
		}		
	    }
	    String strWhClause = "";
	    if(sJournalType != null && sJournalType.length() > 0){
		if(strWhClause != null && strWhClause.length() > 0){
		    strWhClause += " AND JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]+
				   " IN("+sJournalType+") "; 
		}else{
		    strWhClause += " WHERE JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]+
				   " IN("+sJournalType+") "; 
		}
	    }
	    
	    if(iAmountStatus != 0){
		if(strWhClause != null && strWhClause.length() > 0){
		    strWhClause += " AND JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT_STATUS]+
				   " = "+iAmountStatus; 
		}else{
		    strWhClause += " WHERE JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT_STATUS]+
				    " = "+iAmountStatus; 
		}
	    }
	    
	    if(srcCashFlow.getDateFrom().before(srcCashFlow.getDateTo())){
		if(strWhClause != null && strWhClause.length() > 0){
		    strWhClause += " AND JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE]+
				   " BETWEEN '"+Formater.formatDate(srcCashFlow.getDateFrom(), "yyyy-MM-dd")+
				   "' AND '"+Formater.formatDate(srcCashFlow.getDateTo(), "yyyy-MM-dd")+"' ";
		}else{
		    strWhClause += " WHERE JM."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE]+
				   " BETWEEN '"+Formater.formatDate(srcCashFlow.getDateFrom(), "yyyy-MM-dd")+
				   "' AND '"+Formater.formatDate(srcCashFlow.getDateTo(), "yyyy-MM-dd")+"' ";
		}
	    }
	    
	    if(iLinkType != 0){		
		if(strWhClause != null && strWhClause.length() > 0){
		    strWhClause += " AND L."+PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE]+
				   " = "+iLinkType;
		}else{
		    strWhClause += " WHERE L."+PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE]+
				   " = "+iLinkType;
		}
	    }
	    
	    if(strWhClause != null && strWhClause.length() > 0){
		sql += strWhClause;
	    }
	    
	    if(sql != null && sql.length() > 0){
		sResult = sql;
	    }
	}catch(Exception e){}
	return sResult;
    }
}
