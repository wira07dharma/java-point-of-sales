/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.session.analysis;

import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.entity.analysis.ChartAccountBalance;
import com.dimata.aiso.entity.analysis.PieDataSet;
import com.dimata.aiso.entity.jurnal.AccountBalance;
import com.dimata.aiso.entity.jurnal.PstJurnalDetail;
import com.dimata.aiso.entity.jurnal.PstJurnalUmum;
import com.dimata.aiso.entity.masterdata.PstAccountLink;
import com.dimata.aiso.entity.masterdata.PstPerkiraan;
import com.dimata.aiso.entity.periode.PstPeriode;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalDetail;
import com.dimata.aiso.entity.specialJournal.PstSpecialJournalMain;
import com.dimata.aiso.report.analisa.ChartGenerator;
import com.dimata.interfaces.chartofaccount.I_ChartOfAccountGroup;
import com.dimata.interfaces.journal.I_JournalType;
import com.dimata.util.Formater;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author dwi
 */
public class SessChartDataSet {
    
    public static final int SHORT_ASC = 0;
    public static final int SHORT_DESC = 1;
    
    public static final int NON_WORKING_CAPITAL = 0;
    public static final int CASH_IN = 1;
    public static final int CASH_OUT = 2;
    public static final int BANK_IN = 3;
    public static final int BANK_OUT = 4;
    public static final int BANK_TRANSFER_IN = 5;
    public static final int BANK_TRANSFER_OUT = 6;
    public static final int BANK_PAYMENT = 7;
    public static final int FUND = 8;
    public static final int PETTY_CASH_IN = 9;
    public static final int PETTY_CASH_OUT = 10;
    
    public static synchronized DefaultPieDataset getDataSet(int iRecordToGet, Date startDate, Date endDate, 
	    boolean isLoadNewData, double dInAmount, int iShortOrientation, 
	    String sOtherValue, int iAccountGroup, int iChartType, int iChartRevType){
	DefaultPieDataset pd = new DefaultPieDataset();
	Vector vDataSet = new Vector(1,1);
	double dTotal = 0.0;
	double dTotalDataView = 0.0;
	try{
	    vDataSet = getPieChartDataSet(iRecordToGet, iShortOrientation, startDate, endDate,isLoadNewData,iAccountGroup);
	    if(vDataSet != null && vDataSet.size() > 0){
		dTotal = getTotal();
	    }
	
	    for(int i = 0; i < vDataSet.size(); i++){
	      PieDataSet dataSet = (PieDataSet)vDataSet.get(i);	
	      dTotalDataView += dataSet.getAmount();
	      if(iChartRevType == ChartGenerator.PIE_CHART_REVENUE_CONTRIBUTION){
		    pd.setValue(dataSet.getLabel().toLowerCase().trim()+" = "+Formater.formatNumber((dataSet.getAmount() / dTotal) * 100, "##,###.##")+"%",(dataSet.getAmount() / dTotal) * 100);
	      }else{
		     pd.setValue(dataSet.getLabel().toLowerCase().trim()+" = "+Formater.formatNumber((dataSet.getAmount() / dInAmount), "##,###.##"),dataSet.getAmount());
	      }
	   }
	    
	    System.out.println("Others ::::::::::::::::::: "+dTotal+", "+dTotalDataView);
	    if(iShortOrientation == SHORT_DESC && dTotal > 0){
		double dTotalOthers = dTotal - dTotalDataView;
		if(iChartRevType == ChartGenerator.PIE_CHART_REVENUE_CONTRIBUTION){
		    pd.setValue(sOtherValue +" = "+ Formater.formatNumber((dTotalOthers / dTotal) * 100, "##,###.##")+"%", (dTotalOthers / dTotal) * 100);		    
		}else{
		    pd.setValue(sOtherValue +" = "+ Formater.formatNumber((dTotalOthers / dInAmount), "##,###.##"), (dTotalOthers));
		}
	    }
	}catch(Exception e){}
	return pd;
    }
    
    public static Vector getPieChartDataSet(int iRecordToGet, int iShortOrientation, Date startDate, Date endDate, 
	    boolean isLoadNewData, int iAccountGroup){
	DBResultSet dbrs = null;
	Vector vResult = new Vector(1,1);
	try{
	    if(isLoadNewData){
		insertPieChartDataSet(startDate, endDate,iAccountGroup,ChartGenerator.PIE_CHART);
	    }
	    
	    String sql = " SELECT amount, label FROM aiso_tmp_chart "+
			 " WHERE chart_type = "+ChartGenerator.PIE_CHART+" AND data_type = "+iAccountGroup;
		    sql += " ORDER BY amount ";

			if(iShortOrientation == SHORT_DESC){
				    sql += " DESC ";
			}
		if(iRecordToGet > 0){
		    switch(PstJurnalDetail.DBSVR_TYPE){
			case PstJurnalDetail.DBSVR_MYSQL:
			    sql += " LIMIT 0,"+iRecordToGet;
			    break;
			case PstJurnalDetail.DBSVR_POSTGRESQL:
			    sql += " LIMIT "+iRecordToGet+" OFFSET 0";
			    break;
		    }
		}
			    
	    
	    //System.out.println("SQL SessChartDataSet.getPieChartDataSet ::::::::::::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		PieDataSet dataSet = new PieDataSet();
		dataSet.setAmount(rs.getDouble("amount"));
		dataSet.setLabel(rs.getString("label"));
		vResult.add(dataSet);
	    }
	    rs.close();
	}catch(Exception e){}
	return vResult;
    }
    
    public static synchronized double getTotal(){
	DBResultSet dbrs = null;
	double dTotal = 0.0;
	try{
	    String sql = " SELECT SUM(amount) as AMOUNT FROM aiso_tmp_chart";
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		dTotal = rs.getDouble("AMOUNT");
	    }
	    rs.close();
	}catch(Exception e){}
	return dTotal;
    }
    
    public static synchronized void insertPieChartDataSet(Date startDate, Date endDate, int iAccountGroup, int iChartType){
	try{
	    deleteTempPieChartDataSet();
	    insertPieChartDataSetFrJU(startDate, endDate,iAccountGroup,iChartType);
	    switch(iAccountGroup){
		case I_ChartOfAccountGroup.ACC_GROUP_EXPENSE:
		    insertPieDataSetFrSJChequeRequest(startDate, endDate,iChartType);
		    insertPieDataSetFrSJNonCash(startDate, endDate,iChartType);
		    insertPieDataSetFrSJPettyCash(startDate, endDate,iChartType);
		break;
		case I_ChartOfAccountGroup.ACC_GROUP_REVENUE:
		    insertPieDataSetFrSJCash(startDate, endDate,iChartType);
		break;
		case I_ChartOfAccountGroup.ACC_GROUP_EQUITY:
		    updateTempChart(iAccountGroup, iChartType);
		    insertDataWorking(startDate, endDate,iChartType);
		break;
	    }
	}catch(Exception e){}
    }
    
    public static synchronized void deleteTempPieChartDataSet(){
	try{
	    String sql = " DELETE FROM aiso_tmp_chart";
	    DBHandler.execUpdate(sql);
	}catch(Exception e){}
    }
    
    /**
     *Get data from General Journal
     * @param Date start transaction date
     * @param Date end transaction date
     * @return Vector data set
     */
    public static synchronized void insertPieChartDataSetFrJU(Date startDate, Date endDate, int iAccountGroup, int iChartType){
	try{
		String sql = getStringInsertQuery() + getStringSelectQuery(I_JournalType.TIPE_JURNAL_UMUM, PstSpecialJournalMain.STS_DEBET, iAccountGroup,  startDate, endDate, iChartType, NON_WORKING_CAPITAL);
		//System.out.println("SQL insertPieChartDataSetFrJU :::::: "+sql);
		DBHandler.execUpdate(sql);
	    }catch(Exception e){}
    }
    
    public static synchronized void insertPieDataSetFrSJPettyCash(Date startDate, Date endDate, int iChartType){
	try{
	    String sql = getStringInsertQuery() + getStringSelectQuery(I_JournalType.TIPE_SPECIAL_JOURNAL_PETTY_CASH, PstSpecialJournalMain.STS_DEBET, I_ChartOfAccountGroup.ACC_GROUP_EXPENSE,  startDate, endDate, iChartType, NON_WORKING_CAPITAL);
	    //System.out.println("SQL insertPieDataSetFrSJPettyCash :::::: "+sql);
	    DBHandler.execUpdate(sql);
	}catch(Exception e){}
    }
    
     public static synchronized void insertPieDataSetFrSJChequeRequest(Date startDate, Date endDate, int iChartType){
	try{
	    String sql = getStringInsertQuery() + getStringSelectQuery(I_JournalType.TIPE_SPECIAL_JOURNAL_BANK, PstSpecialJournalMain.STS_KREDIT, I_ChartOfAccountGroup.ACC_GROUP_EXPENSE,  startDate, endDate, iChartType, NON_WORKING_CAPITAL);
	    //System.out.println("SQL insertPieDataSetFrSJChequeRequest :::::: "+sql);
	    DBHandler.execUpdate(sql);
	}catch(Exception e){}
    }
     
      public static synchronized void insertPieDataSetFrSJNonCash(Date startDate, Date endDate, int iChartType){
	try{
	    String sql = getStringInsertQuery() + getStringSelectQuery(I_JournalType.TIPE_SPECIAL_JOURNAL_NON_CASH, PstSpecialJournalMain.STS_DEBET, I_ChartOfAccountGroup.ACC_GROUP_EXPENSE,  startDate, endDate, iChartType, NON_WORKING_CAPITAL);
	     //System.out.println("SQL insertPieDataSetFrSJNonCash :::::: "+sql);
	    DBHandler.execUpdate(sql);
	}catch(Exception e){}
    }
      
      public static synchronized void insertPieDataSetFrSJCash(Date startDate, Date endDate, int iChartType){
	try{
	    String sql = getStringInsertQuery() + getStringSelectQuery(I_JournalType.TIPE_SPECIAL_JOURNAL_CASH, PstSpecialJournalMain.STS_DEBET, I_ChartOfAccountGroup.ACC_GROUP_REVENUE,  startDate, endDate, iChartType, NON_WORKING_CAPITAL);
	    //System.out.println("SQL insertPieDataSetFrSJCash :::::: "+sql);
	    DBHandler.execUpdate(sql);
	}catch(Exception e){}
    }
      
      public static synchronized void insertDataWorking(Date startDate, Date endDate, int iChartType){
	    try{
		Vector listAccountBalance = listAccountBalance(startDate, endDate, iChartType);
		if(listAccountBalance != null && listAccountBalance.size() > 0){
		    for(int i = 0; i < listAccountBalance.size(); i++){
			ChartAccountBalance chartAccBlc = (ChartAccountBalance)listAccountBalance.get(i);
			String sInsert = getStringInsertQuery()+" VALUES("+chartAccBlc.getBalance()+", '"+
					chartAccBlc.getAccountName()+"', "+ChartGenerator.PIE_CHART+", "+
					I_ChartOfAccountGroup.ACC_GROUP_EQUITY+")";
			//System.out.println("SQL insertDataWorking :::::: "+sInsert);
			DBHandler.execUpdate(sInsert);
		    }
		}
	    }catch(Exception e){}
      }
      
      public static synchronized Vector listAccountBalance(Date startDate, Date endDate, int iChartType){
	    DBResultSet dbrs = null;
	    Vector vResult = new Vector(1,1);
	    Hashtable hResult = null;
	    try{
		String sql = getStringSelectQuery(I_JournalType.TIPE_SPECIAL_JOURNAL_BANK, PstSpecialJournalMain.STS_KREDIT, I_ChartOfAccountGroup.ACC_GROUP_PETTY_CASH, startDate, endDate, iChartType, CASH_IN);
		
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    String sAccountName = rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]);
		    double dAmount = rs.getDouble("amount");
		    int iDataType = rs.getInt("data_type");
		    ChartAccountBalance objCABalance = new ChartAccountBalance();
		    objCABalance.setAccountName(sAccountName);
		    objCABalance.setBalance(dAmount);
		    objCABalance.setDataType(iDataType);
		    
		    hResult.put(sAccountName, objCABalance);
		}
		rs.close();
		
		hResult = listAccBalanceFrBankDeposit(hResult, startDate, endDate, iChartType);
		hResult = listAccBalanceFrBankTransferIn(hResult, startDate, endDate, iChartType);
		hResult = listAccBalanceFrBankTransferOut(hResult, startDate, endDate, iChartType);
		hResult = listAccBalanceFrChequeRequest(hResult, startDate, endDate, iChartType);
		hResult = listAccBalanceFrCashOut(hResult, startDate, endDate, iChartType);
		hResult = listAccBalanceFrFund(hResult, startDate, endDate, iChartType);
		hResult = listAccBalanceFrPettyCashIn(hResult, startDate, endDate, iChartType);
		hResult = listAccBalanceFrPettyCashOut(hResult, startDate, endDate, iChartType);
		
		vResult = new Vector(hResult.values());
	    }catch(Exception e){}
	    return vResult;
      }
      
      public static synchronized Hashtable listAccBalanceFrCashOut(Hashtable hResult, Date startDate, Date endDate, int iChartType){
	    DBResultSet dbrs = null;	    
	    try{
		String sql = getStringSelectQuery(I_JournalType.TIPE_SPECIAL_JOURNAL_BANK, PstSpecialJournalMain.STS_DEBET, I_ChartOfAccountGroup.ACC_GROUP_CASH, startDate, endDate, iChartType, CASH_OUT);
		
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    String sAccountName = rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]);
		    double dAmount = rs.getDouble("amount");
		    int iDataType = rs.getInt("data_type");
		    double dBalance = 0.0;
		    ChartAccountBalance objCABalance = new ChartAccountBalance();
		    if(hResult.containsKey(sAccountName)){
			    objCABalance = (ChartAccountBalance)hResult.get(sAccountName);
			    dBalance = objCABalance.getBalance();
			    objCABalance.setBalance(dBalance - dAmount);
		    }else{
			    objCABalance.setAccountName(sAccountName);
			    objCABalance.setBalance(dAmount);
			    objCABalance.setDataType(iDataType);
		    }
		    
		     hResult.put(sAccountName, objCABalance);
		}
		rs.close();
	    }catch(Exception e){}
	    return hResult;
      }
      
      public static synchronized Hashtable listTempChart(int iAccountGroup, int iChartType){
	    DBResultSet dbrs = null;
	    Hashtable hTempChart = null;
	    
	    try{
		String sql = " SELECT amount, label FROM aiso_tmp_chart "+
			     " WHERE chart_type = "+iChartType+" AND data_type = "+iAccountGroup;
		System.out.println("SQL listTempChart ::::::::::::::: "+sql);
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    double dAmount = rs.getDouble("amount");
		    String sAccName = rs.getString("label");
		    ChartAccountBalance chartAB = new ChartAccountBalance();
		    chartAB.setAccountName(sAccName);
		    chartAB.setBalance(dAmount);
		    hTempChart.put(sAccName, chartAB);	
		}
		rs.close();
	    }catch(Exception e){}
	    return hTempChart;
      }
      
      public static synchronized void updateTempChart(int iAccountGroup, int iChartType){
	    DBResultSet dbrs = null;	
	    Vector hResult = null;
	    Hashtable hTempChart = null;
	    try{
		hTempChart = (Hashtable)listTempChart(iAccountGroup,iChartType);
	    }catch(Exception e){}
	    try{
		String sql = getStringQueryOpeningBalance();
		
		System.out.println("SQL updateTempChart ::::::::::::::: "+sql);
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    String sAccountName = rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]);
		    double dAmount = rs.getDouble("amount");
		    double dBalance = 0.0;
		    double dBalanceUpdate = 0.0;
		    if(hTempChart.containsKey(sAccountName)){
			 ChartAccountBalance chartAB = (ChartAccountBalance)hTempChart.get(sAccountName); 
			 dBalance = chartAB.getBalance();
			 dBalanceUpdate = dBalance + dAmount;
			 String sUpdate = " UPDATE aiso_tmp_chart set amount = "+dBalanceUpdate+" WHERE label = '"+sAccountName+"'";
			 System.out.println("SQL updateTempChart.sUpdate ::::::::::::::: "+sUpdate);
			 DBHandler.execUpdate(sUpdate);
		    }else{
			String sInsert = getStringInsertQuery()+" VALUES ("+dAmount+", "+sAccountName+", "+iChartType+", "+iAccountGroup+")";
			System.out.println("SQL updateTempChart.sInsert ::::::::::::::: "+sInsert);
			DBHandler.execUpdate(sInsert);
		    }
		}
		rs.close();
	    }catch(Exception e){}
      }
      
      public static synchronized Hashtable listAccBalanceFrPettyCashIn(Hashtable hResult, Date startDate, Date endDate, int iChartType){
	    DBResultSet dbrs = null;	    
	    try{
		String sql = getStringSelectQuery(I_JournalType.TIPE_SPECIAL_JOURNAL_BANK, PstSpecialJournalMain.STS_KREDIT, I_ChartOfAccountGroup.ACC_GROUP_PETTY_CASH, startDate, endDate, iChartType, PETTY_CASH_IN);
		
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    String sAccountName = rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]);
		    double dAmount = rs.getDouble("amount");
		    int iDataType = rs.getInt("data_type");
		    ChartAccountBalance objCABalance = new ChartAccountBalance();
		    objCABalance.setAccountName(sAccountName);
		    objCABalance.setBalance(dAmount);
		    objCABalance.setDataType(iDataType);
		    
		    hResult.put(sAccountName, objCABalance);
		}
		rs.close();
	    }catch(Exception e){}
	    return hResult;
      }
      
      public static synchronized Hashtable listAccBalanceFrPettyCashOut(Hashtable hResult, Date startDate, Date endDate, int iChartType){
	    DBResultSet dbrs = null;	    
	    try{
		String sql = getStringSelectQuery(I_JournalType.TIPE_SPECIAL_JOURNAL_PETTY_CASH, PstSpecialJournalMain.STS_KREDIT, I_ChartOfAccountGroup.ACC_GROUP_PETTY_CASH, startDate, endDate, iChartType, PETTY_CASH_OUT);
		
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    String sAccountName = rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]);
		    double dAmount = rs.getDouble("amount");
		    double dBalance = 0.0;
		    int iDataType = rs.getInt("data_type");
		    ChartAccountBalance objCABalance = new ChartAccountBalance();
		    if(hResult.containsKey(sAccountName)){
			    objCABalance = (ChartAccountBalance)hResult.get(sAccountName);
			    dBalance = objCABalance.getBalance();
			    objCABalance.setBalance(dBalance - dAmount);
		    }else{
			    objCABalance.setAccountName(sAccountName);
			    objCABalance.setBalance(dAmount);
			    objCABalance.setDataType(iDataType);
		    }
		    
		     hResult.put(sAccountName, objCABalance);
		}
		rs.close();
	    }catch(Exception e){}
	    return hResult;
      }
      
      public static synchronized Hashtable listAccBalanceFrBankDeposit(Hashtable hResult, Date startDate, Date endDate, int iChartType){
	    DBResultSet dbrs = null;	    
	    try{
		String sql = getStringSelectQuery(I_JournalType.TIPE_SPECIAL_JOURNAL_BANK, PstSpecialJournalMain.STS_DEBET, I_ChartOfAccountGroup.ACC_GROUP_BANK, startDate, endDate, iChartType, BANK_IN);
		
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    String sAccountName = rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]);
		    int iDataType = rs.getInt("data_type");
		    double dAmount = rs.getDouble("amount");
		    ChartAccountBalance objCABalance = new ChartAccountBalance();
		    objCABalance.setAccountName(sAccountName);
		    objCABalance.setBalance(dAmount);
		    objCABalance.setDataType(iDataType);
		    
		    hResult.put(sAccountName, objCABalance);
		}
		rs.close();
	    }catch(Exception e){}
	    return hResult;
      }
      
      public static synchronized Hashtable listAccBalanceFrChequeRequest(Hashtable hResult, Date startDate, Date endDate, int iChartType){
	    DBResultSet dbrs = null;	    
	    try{
		String sql = getStringSelectQuery(I_JournalType.TIPE_SPECIAL_JOURNAL_BANK, PstSpecialJournalMain.STS_KREDIT, I_ChartOfAccountGroup.ACC_GROUP_BANK, startDate, endDate, iChartType, BANK_OUT);
		
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    String sAccountName = rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]);
		    double dAmount = rs.getDouble("amount");
		    int iDataType = rs.getInt("data_type");
		    double dBalance = 0.0;
		    ChartAccountBalance objCABalance = new ChartAccountBalance();
		    if(hResult.containsKey(sAccountName)){
			    objCABalance = (ChartAccountBalance)hResult.get(sAccountName);
			    dBalance = objCABalance.getBalance();
			    objCABalance.setBalance(dBalance - dAmount);
		    }else{
			    objCABalance.setAccountName(sAccountName);
			    objCABalance.setBalance(dAmount);
			    objCABalance.setDataType(iDataType);
		    }
		    
		     hResult.put(sAccountName, objCABalance);
		}
		rs.close();
	    }catch(Exception e){}
	    return hResult;
      }
      
      public static synchronized Hashtable listAccBalanceFrBankTransferIn(Hashtable hResult, Date startDate, Date endDate, int iChartType){
	    DBResultSet dbrs = null;	    
	    try{
		String sql = getStringSelectQuery(I_JournalType.TIPE_SPECIAL_JOURNAL_BANK_TRANSFER, PstSpecialJournalMain.STS_KREDIT, I_ChartOfAccountGroup.ACC_GROUP_BANK, startDate, endDate, iChartType, BANK_TRANSFER_IN);
		
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    String sAccountName = rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]);
		    double dAmount = rs.getDouble("amount");
		    int iDataType = rs.getInt("data_type");
		    double dBalance = 0.0;
		    ChartAccountBalance objCABalance = new ChartAccountBalance();
		    if(hResult.containsKey(sAccountName)){
			    objCABalance = (ChartAccountBalance)hResult.get(sAccountName);
			    dBalance = objCABalance.getBalance();
			    objCABalance.setBalance(dBalance + dAmount);
		    }else{
			    objCABalance.setAccountName(sAccountName);
			    objCABalance.setBalance(dAmount);
			    objCABalance.setDataType(iDataType);
		    }
		    
		     hResult.put(sAccountName, objCABalance);
		}
		rs.close();
	    }catch(Exception e){}
	    return hResult;
      }
      
      public static synchronized Hashtable listAccBalanceFrBankTransferOut(Hashtable hResult, Date startDate, Date endDate, int iChartType){
	    DBResultSet dbrs = null;	    
	    try{
		String sql = getStringSelectQuery(I_JournalType.TIPE_SPECIAL_JOURNAL_BANK_TRANSFER, PstSpecialJournalMain.STS_KREDIT, I_ChartOfAccountGroup.ACC_GROUP_BANK, startDate, endDate, iChartType, BANK_TRANSFER_OUT);
		
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    String sAccountName = rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]);
		    double dAmount = rs.getDouble("amount");
		    int iDataType = rs.getInt("data_type");
		    double dBalance = 0.0;
		    ChartAccountBalance objCABalance = new ChartAccountBalance();
		    if(hResult.containsKey(sAccountName)){
			    objCABalance = (ChartAccountBalance)hResult.get(sAccountName);
			    dBalance = objCABalance.getBalance();
			    objCABalance.setBalance(dBalance - dAmount);
		    }else{
			    objCABalance.setAccountName(sAccountName);
			    objCABalance.setBalance(dAmount);
			    objCABalance.setDataType(iDataType);
		    }
		    
		     hResult.put(sAccountName, objCABalance);
		}
		rs.close();
	    }catch(Exception e){}
	    return hResult;
      }
      
      public static synchronized Hashtable listAccBalanceFrFund(Hashtable hResult, Date startDate, Date endDate, int iChartType){
	    DBResultSet dbrs = null;	    
	    try{
		String sql = getStringSelectQuery(I_JournalType.TIPE_SPECIAL_JOURNAL_FUND, PstSpecialJournalMain.STS_DEBET, I_ChartOfAccountGroup.ACC_GROUP_BANK, startDate, endDate, iChartType, FUND);
		
		dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();
		while(rs.next()){
		    String sAccountName = rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]);
		    double dAmount = rs.getDouble("amount");
		    int iDataType = rs.getInt("data_type");
		    double dBalance = 0.0;
		    ChartAccountBalance objCABalance = new ChartAccountBalance();
		    if(hResult.containsKey(sAccountName)){
			    objCABalance = (ChartAccountBalance)hResult.get(sAccountName);
			    dBalance = objCABalance.getBalance();
			    objCABalance.setBalance(dBalance + dAmount);
		    }else{
			    objCABalance.setAccountName(sAccountName);
			    objCABalance.setBalance(dAmount);
			    objCABalance.setDataType(iDataType);
		    }
		    
		     hResult.put(sAccountName, objCABalance);
		}
		rs.close();
	    }catch(Exception e){}
	    return hResult;
      }
      
      public static synchronized String getStringSelectQuery(int iJournalType, int iMountStatus, int iAccountGroup, Date startDate, Date endDate, int iChartType, int inOut){
	String sResult = "";
	try{
	    sResult = " SELECT ";
		    if(iJournalType == I_JournalType.TIPE_JURNAL_UMUM){
			    if(iAccountGroup == I_ChartOfAccountGroup.ACC_GROUP_REVENUE){
				sResult +=   " SUM(D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT]+" - "+
					     " D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET]+") AS amount ";
			    }else{
				sResult +=   " SUM(D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_DEBET]+" - "+
					     " D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_KREDIT]+") AS amount ";
			    }
				sResult +=   ", P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+" AS label "+
					     ", "+iChartType+", "+iAccountGroup+" "+
					     " FROM "+PstJurnalDetail.TBL_JURNAL_DETAIL+" AS D "+
					     " INNER JOIN "+PstJurnalUmum.TBL_JURNAL_UMUM+" AS U "+
					     " ON U."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JURNALID]+
					     " = D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_JURNALID]+
					     " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS P "+
					     " ON P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
					     " = D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+
					     " INNER JOIN "+PstAccountLink.TBL_ACCOUNT_LINK+" AS L "+
					     " ON D."+PstJurnalDetail.fieldNames[PstJurnalDetail.FLD_IDPERKIRAAN]+
					     " = L."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+
					     " WHERE U."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_JOURNAL_TYPE]+
					     " = "+iJournalType;
				switch(iAccountGroup){
				    case I_ChartOfAccountGroup.ACC_GROUP_EXPENSE :
					sResult += " AND P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
						   " IN("+I_ChartOfAccountGroup.ACC_GROUP_EXPENSE+", "+I_ChartOfAccountGroup.ACC_GROUP_OTHER_EXPENSE+") ";
					break;
				    case I_ChartOfAccountGroup.ACC_GROUP_REVENUE :
					sResult += " AND P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
						   " IN("+I_ChartOfAccountGroup.ACC_GROUP_REVENUE+", "+I_ChartOfAccountGroup.ACC_GROUP_OTHER_REVENUE+") ";
					break;
				    case I_ChartOfAccountGroup.ACC_GROUP_EQUITY :
					sResult += " AND L."+PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE]+
						   " IN("+I_ChartOfAccountGroup.ACC_GROUP_CASH+", "+I_ChartOfAccountGroup.ACC_GROUP_PETTY_CASH+
						   ", "+I_ChartOfAccountGroup.ACC_GROUP_BANK+", "+I_ChartOfAccountGroup.ACC_GROUP_PIUTANG+") ";
					break;
				}
		    }else{
			if(iChartType == ChartGenerator.PIE_CHART_NET_WORKING_CAPITAL){
				sResult += getStringAmount(inOut);
			}else{
				sResult +=   " SUM(D."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT]+") AS amount ";
			}
			sResult +=   ", P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
				     ", "+iChartType+" AS chart_type "+
				     ", L."+PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE]+" AS data_type "+
				     " FROM "+PstSpecialJournalDetail.TBL_AISO_SPC_JDETAIL+" AS D "+
				     " INNER JOIN "+PstSpecialJournalMain.TBL_AISO_SPC_JMAIN+" AS M "+
				     " ON M."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]+
				     " = D."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_DETAIL_ID]+
				     " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS P ";
			if(iChartType == ChartGenerator.PIE_CHART_NET_WORKING_CAPITAL){
				sResult += getStringInnerJoin(inOut);
			}else{
				 sResult += " ON D."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN];
			}
				 sResult += " = P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
					    " INNER JOIN "+PstAccountLink.TBL_ACCOUNT_LINK+" AS L "+
					   " ON D."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN]+
					   " = L."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+
					   " WHERE M."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]+
					   " = "+iJournalType;
			}
			if(iJournalType == I_JournalType.TIPE_SPECIAL_JOURNAL_BANK && iMountStatus == PstSpecialJournalMain.STS_KREDIT){
				sResult += " AND M."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT_STATUS]+
					   " = "+PstSpecialJournalMain.STS_KREDIT;
			}
				
				sResult += getWhereAccountGroup(iAccountGroup);
				
		    if(startDate != null && endDate != null){
			if(startDate.before(endDate)){
			    if(iJournalType == I_JournalType.TIPE_JURNAL_UMUM){
				sResult += " AND U."+PstJurnalUmum.fieldNames[PstJurnalUmum.FLD_TGLTRANSAKSI]+
					    " BETWEEN '"+Formater.formatDate(startDate, "yyyy-MM-dd")+
					    "' AND '"+Formater.formatDate(endDate, "yyyy-MM-dd")+"' ";
			    }else{
				sResult += " AND M."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE]+
					    " BETWEEN '"+Formater.formatDate(startDate, "yyyy-MM-dd")+
					    "' AND '"+Formater.formatDate(endDate, "yyyy-MM-dd")+"' ";
			    }
			}
		    }
	    
		    sResult += " GROUP BY P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
				", L."+PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE];
		    
	}catch(Exception e){}
	return sResult;
      }
      
      public static synchronized String getStringInsertQuery(){
	    String sResult = "";
	    try{
		sResult = " INSERT INTO aiso_tmp_chart(amount,label,chart_type,data_type) ";
	    }catch(Exception e){}
	    return sResult;
      }
      
      public static synchronized String getStringAmount(int inOut){
	  String sResult = "";  
	    try{
		switch(inOut){
		    case CASH_IN:
				sResult +=   " SUM(M."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT]+") AS amount ";
			break;
		    case CASH_OUT:
				sResult +=   " SUM(D."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT]+") AS amount ";
			break;
		    case BANK_IN:
				sResult +=   " SUM(M."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT]+") AS amount ";
			break;
		    case BANK_OUT:
				sResult +=   " SUM(M."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT]+") AS amount ";
			break;
		    case BANK_TRANSFER_IN:
				sResult +=   " SUM(D."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT]+") AS amount ";
			break;
		    case BANK_TRANSFER_OUT: 
				sResult +=   " SUM(M."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT]+") AS amount ";
			break;
		    case PETTY_CASH_OUT: 
				sResult +=   " SUM(M."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT]+") AS amount ";
			break;
		    case PETTY_CASH_IN: 
				sResult +=   " SUM(D."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT]+") AS amount ";
			break;
		    case FUND:
				sResult +=   " SUM(M."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT]+") AS amount ";
			break;
		}
	    }catch(Exception e){}
	  return sResult;
      }
      
      public static synchronized String getStringInnerJoin(int inOut){
	  String sResult = "";
	  try{
		switch(inOut){
		    case CASH_IN:
				sResult +=   " ON M."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN];
			break;
		    case CASH_OUT:
				sResult +=   " ON D."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN];
			break;
		    case BANK_IN:
				sResult +=   " ON M."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN];
			break;
		    case BANK_OUT:
				sResult +=   " ON M."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN];
			break;
		    case BANK_TRANSFER_IN:
				sResult +=   " ON D."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN];
			break;
		    case BANK_TRANSFER_OUT: 
				sResult +=   " ON M."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN];
			break;
		    case PETTY_CASH_OUT: 
				sResult +=   " ON M."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN];
			break;
		    case PETTY_CASH_IN: 
				sResult +=   " ON D."+PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN];
			break;
		    case FUND:
				sResult +=   " ON M."+PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN];
			break;
		}
	  }catch(Exception e){}
	  return sResult;
      }
      
      public static synchronized String getWhereAccountGroup(int iAccountGroup){
	    String sResult = "";
	    try{
		switch(iAccountGroup){	     
		     case I_ChartOfAccountGroup.ACC_GROUP_EXPENSE:
			    sResult += " AND P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
				       " IN("+I_ChartOfAccountGroup.ACC_GROUP_EXPENSE+", "+I_ChartOfAccountGroup.ACC_GROUP_OTHER_EXPENSE+") ";
			break;
		     case I_ChartOfAccountGroup.ACC_GROUP_REVENUE:
			    sResult += " AND P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]+
					" IN("+I_ChartOfAccountGroup.ACC_GROUP_REVENUE+", "+I_ChartOfAccountGroup.ACC_GROUP_OTHER_REVENUE+") ";
			break;
		     case I_ChartOfAccountGroup.ACC_GROUP_CASH:
			    sResult += " AND L."+PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE]+
					" = "+I_ChartOfAccountGroup.ACC_GROUP_CASH;
			 break;
		      case I_ChartOfAccountGroup.ACC_GROUP_BANK:
			    sResult += " AND L."+PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE]+
					" = "+I_ChartOfAccountGroup.ACC_GROUP_BANK;
			 break;
		      case I_ChartOfAccountGroup.ACC_GROUP_PETTY_CASH:
			    sResult += " AND L."+PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE]+
					" = "+I_ChartOfAccountGroup.ACC_GROUP_PETTY_CASH;
			 break;
		      case I_ChartOfAccountGroup.ACC_GROUP_FUND:
			    sResult += " AND L."+PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE]+
					" = "+I_ChartOfAccountGroup.ACC_GROUP_FUND;
			 break;
		 }
	    }catch(Exception e){}
	    return sResult;
      }
      
      public static synchronized String getStringQueryOpeningBalance(){
	    String sResult = "";
	    long lPeriodId = 0;
	    long lLastPeriod = 0;
	    try{
		lPeriodId = PstPeriode.getCurrPeriodId();
		
		if(lPeriodId > 0){
		    lLastPeriod = PstPeriode.getPrevPeriod(lPeriodId);
		}
		//System.out.println("lPeriodId ::::: "+lPeriodId+", lLastPeriod :::: "+lLastPeriod);
	    }catch(Exception e){}
	    try{
		sResult = " SELECT SUM(A.debet - A.kredit) as amount"+
			  ", P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
			  " FROM aiso_saldo_akhir_perd as A "+
			  " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" as P "+
			  " ON A.id_perkiraan = P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
			  " INNER JOIN "+PstAccountLink.TBL_ACCOUNT_LINK+" as L "+
			  " ON P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
			  " = L."+PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]+
			  " WHERE periode_id = "+lLastPeriod+" AND "+
			  " L."+PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE]+
			  " IN("+I_ChartOfAccountGroup.ACC_GROUP_CASH+", "+I_ChartOfAccountGroup.ACC_GROUP_BANK+
			  ", "+I_ChartOfAccountGroup.ACC_GROUP_PIUTANG+", "+I_ChartOfAccountGroup.ACC_GROUP_PETTY_CASH+") "+
			  " GROUP BY P."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH];
	    }catch(Exception e){}
	    return sResult;
      }
}
