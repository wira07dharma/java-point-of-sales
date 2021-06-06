
package com.dimata.posbo.session.masterdata;

import com.dimata.pos.entity.balance.Balance;
import com.dimata.pos.entity.balance.CashCashier;
import com.dimata.pos.entity.balance.PstBalance;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.entity.billing.BillDetailCode;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.OtherCost;
import com.dimata.pos.entity.billing.PendingOrder;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillDetailCode;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.billing.PstOtherCost;
import com.dimata.pos.entity.billing.PstPendingOrder;
import com.dimata.pos.entity.billing.PstRecipe;
import com.dimata.pos.entity.billing.Recipe;
import com.dimata.pos.entity.payment.CashCreditCard;
import com.dimata.pos.entity.payment.CashCreditPaymentInfo;
import com.dimata.pos.entity.payment.CashCreditPaymentsDinamis;
import com.dimata.pos.entity.payment.CashPayments1;
import com.dimata.pos.entity.payment.CashReturn;
import com.dimata.pos.entity.payment.CreditPaymentMain;
import com.dimata.pos.entity.payment.PstCashCreditCard;
import com.dimata.pos.entity.payment.PstCashCreditPayment;
import com.dimata.pos.entity.payment.PstCashCreditPaymentDinamis;
import com.dimata.pos.entity.payment.PstCashCreditPaymentInfo;
import com.dimata.pos.entity.payment.PstCashPayment;
import com.dimata.pos.entity.payment.PstCashPayment1;
import com.dimata.pos.entity.payment.PstCashReturn;
import com.dimata.pos.entity.payment.PstCreditPaymentMain;
import com.dimata.posbo.db.DBHandler;
import java.io.Serializable;
import com.dimata.posbo.entity.masterdata.OutletConnection;
import com.dimata.posbo.entity.masterdata.TransferToServer;
import com.dimata.posbo.entity.warehouse.MatCosting;
import com.dimata.posbo.entity.warehouse.MatCostingItem;
import com.dimata.posbo.entity.warehouse.PstMatCosting;
import com.dimata.posbo.entity.warehouse.PstMatCostingItem;
import com.dimata.util.Formater;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Vector;
/* 
 * @author rahde, ktanjana
 * @version 1.0
 * Copyright : PT. Dimata Sora Jayate , 2010
 */


public class TransferDataToServerThread  implements Runnable, Serializable {
    private OutletConnection conOb=null;
    private Connection dbConn = null;
    private int threadSleep=40;
    private int maxNumber=10;
    private boolean runThread=true;
    private int errCode=0;
    private boolean pauseTransfer=false;
    private int jumlah_shift = 0;
    private int jumlah_sales_bill =0;
    private String statusTxt="";
    private String addStatusText="";

    private TransferToServer transfer=null;

    private DataProgress dataProgres = null;


    public TransferDataToServerThread(){

    }

    public TransferDataToServerThread(OutletConnection conOb, TransferToServer transfer) {
        try{
            this.conOb = conOb;
            //this.dateFrom = transfer.getDateFrom();
            //this.dateTo = transfer.getDateTo();
            this.transfer = transfer;
            this.dataProgres= new DataProgress();
            
        }catch(Exception e){
            //System.out.println(" ! EXC : initiate thread =  "+e.toString());
            addStatusText("SQLException when PstCashCashier : "+e.toString());
        }

    }

private void createConn()
        throws SQLException
    {
        try
        {
            Class.forName(this.conOb.getDbdriver()).newInstance();
            setStatusTxt("Try to connect" + this.conOb.getDburl() + " " + this.conOb.getDbuser());
            //System.out.println("");
            dbConn = DriverManager.getConnection(this.conOb.getDburl(), this.conOb.getDbuser(), this.conOb.getDbpasswd());
        }
        catch(ClassNotFoundException _ex) {
           //System.out.println(" ! EXC :   "+_ex.toString());
           setStatusTxt("connection failed");
        }
        catch(Exception e) {
            //System.out.println(" ! EXC :  "+e.toString());
            setStatusTxt("connection failed");
        }
    }

    public void run() {
        try{
            addStatusText("");
            createConn();
            String sql =null;
            Statement statement=null;
            Vector vecCashCashier = new Vector();
            statement = this.dbConn.createStatement();
            long start =0;
            long recordToGet=10;

            //String startDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd");
            String dateFrom = Formater.formatDate(transfer.getDateFrom(), "yyyy-MM-dd") + " 00:00:00";
            String dateTo = Formater.formatDate(transfer.getDateTo(), "yyyy-MM-dd")+ " 23:59:59";

            setStatusTxt("try to get shift cashier...");

            
            //untuk mengambil jumlah shift yang ada di outlet
            sql="select count("+PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] +") as jumlah " + " from " + PstCashCashier.TBL_CASH_CASHIER + " where " + PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID] + " > 1 " + " and "+ PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE] + " between '" + dateFrom + "' and '" + dateTo + "'" ;
            ResultSet rsCount= statement.executeQuery(sql);
            while (rsCount.next()){
                dataProgres.setJmlShift(rsCount.getInt("jumlah"));
            }
            rsCount.close();


            //untuk mengambil jumlah shift yang sudah masuk ke server
            sql="select count("+PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] +") as jumlah " + " from " + PstCashCashier.TBL_CASH_CASHIER + " where " + PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID] + " > 1 " + " and " + PstCashCashier.fieldNames[PstCashCashier.FLD_CASHMASTER_ID] + " = " + this.conOb.getCash_master_id() + " and "+ PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE] + " between '" + dateFrom + "' and '" + dateTo + "'" ;
            Connection connection=null;
            connection = DBHandler.getDBConnection();
            statement = connection.createStatement();
            rsCount = statement.executeQuery(sql);
            while (rsCount.next()){
                dataProgres.setJmlShiftDone(rsCount.getInt("jumlah"));
            }
            rsCount.close();

            //proses menghitung detail data penjualan yg ada di outlet
            statement = this.dbConn.createStatement();
            sql="select * from " + PstCashCashier.TBL_CASH_CASHIER + " where " + PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID] + " > 1 " + " and "+ PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE] + " between '" + dateFrom + "' and '" + dateTo + "'" ;
            ResultSet rs = statement.executeQuery(sql);
            
	    while(rs.next()){
		CashCashier cashCashier = new CashCashier();
		PstCashCashier.resultToObject(rs, cashCashier);
		vecCashCashier.add(cashCashier);
            }
            rs.close();
            for(int i=0;i<vecCashCashier.size();i++){

                    CashCashier cashCashier= (CashCashier) vecCashCashier.get(i);

                    //cash_bill_main untuk cash
                    sql="select count(" +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ") as jumlah from " + PstBillMain.TBL_CASH_BILL_MAIN + " where " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + cashCashier.getOID() + " and " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = 0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = 0" ;
                    rsCount = statement.executeQuery(sql);
                    while(rsCount.next()){
                        dataProgres.setJmlhBill(rsCount.getInt("jumlah") + dataProgres.getJmlhBill());
                    }

                    //cash_bill_main untuk other open bill dan batal
                    sql="select count(" +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ") as jumlah from " + PstBillMain.TBL_CASH_BILL_MAIN + " where ((" + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " <> 0) or  (" + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=0 and (" + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "= 1 or "+ PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "= 2) )) and " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + cashCashier.getOID() ;
                    rsCount = statement.executeQuery(sql);
                    while(rsCount.next()){
                        dataProgres.setJmlOtherBill(rsCount.getInt("jumlah") + dataProgres.getJmlOtherBill());
                    }
                    //rsCount.close();

                    sql="select count(" +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ") as jumlah from " + PstBillMain.TBL_CASH_BILL_MAIN + " where " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + cashCashier.getOID() + " and " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = 0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 1 " ;
                    rsCount = statement.executeQuery(sql);
                    while(rsCount.next()){
                        dataProgres.setJmlhCredit(rsCount.getInt("jumlah") + dataProgres.getJmlhCredit());
                    }
                    
                    //menghitung costing di outlet
                    sql="select count(" +PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] + ") as jumlah from " + PstMatCosting.TBL_COSTING + " where " + PstMatCosting.fieldNames[PstMatCosting.FLD_CASH_CASHIER_ID] + " = " + cashCashier.getOID() ;
                    rsCount = statement.executeQuery(sql);
                    while(rsCount.next()){
                        dataProgres.setJmlCosting(rsCount.getInt("jumlah") + dataProgres.getJmlCosting());
                    }

                    rsCount.close();
            }

            //proses menghitung detail data penjualan yang sudah masuk ke server
            connection=null;
            connection = DBHandler.getDBConnection();
            statement = connection.createStatement();
            for(int i=0;i<vecCashCashier.size();i++){
                CashCashier cashCashier = (CashCashier) vecCashCashier.get(i);
                    sql="select count(" +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ") as jumlah from " + PstBillMain.TBL_CASH_BILL_MAIN + " where " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + cashCashier.getOID() + " and " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = 0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = 0" ;
                    rsCount = statement.executeQuery(sql);
                    while (rsCount.next()){
                        dataProgres.setJmlhBillDone(rsCount.getInt("jumlah") + dataProgres.getJmlhBillDone());
                    }
                    //rsCount.close();
                    sql="select count(" +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ") as jumlah from " + PstBillMain.TBL_CASH_BILL_MAIN + " where ((" + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " <> 0) or  (" + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=0 and (" + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "= 1 or "+ PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "= 2) )) and " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + cashCashier.getOID() ;
                    rsCount = statement.executeQuery(sql);
                    while (rsCount.next()){
                        dataProgres.setJmlOtherBillDone(rsCount.getInt("jumlah") + dataProgres.getJmlOtherBillDone());
                    }
                    //rsCount.close();

                    sql="select count(" +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ") as jumlah from " + PstBillMain.TBL_CASH_BILL_MAIN + " where " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + cashCashier.getOID() + " and " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = 0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 1 " ;
                    rsCount = statement.executeQuery(sql);
                    while (rsCount.next()){
                        dataProgres.setJmlhCreditDone(rsCount.getInt("jumlah") + dataProgres.getJmlhCreditDone());
                    }
                    
                    sql="select count(" +PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_MATERIAL_ID] + ") as jumlah from " + PstMatCosting.TBL_COSTING+ " where " + PstMatCosting.fieldNames[PstMatCosting.FLD_CASH_CASHIER_ID] + " = " + cashCashier.getOID();
                    rsCount = statement.executeQuery(sql);
                    while (rsCount.next()){
                        dataProgres.setJmlCostingDone(rsCount.getInt("jumlah") + dataProgres.getJmlCostingDone());
                    }
                    //rsCount.close();
            }

           
            statement = this.dbConn.createStatement();
            vecCashCashier.clear();
            sql="select * from " + PstCashCashier.TBL_CASH_CASHIER + " where " + PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID] + " > 1 " + " and "+ PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE] + " between '" + dateFrom + "' and '" + dateTo + "' limit " + start + "," + recordToGet ;
            rs = statement.executeQuery(sql);
            
	    while(rs.next()) {
		CashCashier cashCashier = new CashCashier();
		PstCashCashier.resultToObject(rs, cashCashier);
		vecCashCashier.add(cashCashier);
            }
            rs.close();
                                
            while(isRunThread() && vecCashCashier!=null && vecCashCashier.size() > 0){
                if(!isPauseTransfer()){
                for(int idx=0;idx<vecCashCashier.size();idx++){
                    try{
                       CashCashier cashCashier = (CashCashier) vecCashCashier.get(idx);
                       setStatusTxt("Try to get shift...");
                        try{
                            PstCashCashier.insertExcByOid(cashCashier);
                            dataProgres.setJmlShiftDone(dataProgres.getJmlShiftDone() + 1);
                        }catch(Exception exc){
                             addStatusText("SQLException when PstCashCashier : "+exc);
                        }
                                     
                        try{
                            transferCashBalance(cashCashier.getOID());
                        }catch(Exception exc){
                            addStatusText("SQLException when transferCashBalance : "+exc);
                        }

                        try{
                            transferBillMainTransTunai(cashCashier.getOID());
                        }catch(Exception exc){
                            addStatusText("SQLException when transferBillMainTransTunai : "+exc);
                        }

                        try{
                            transferBillMainOtherBill(cashCashier.getOID());
                        }catch(Exception exc){
                            addStatusText("SQLException when transferBillMainOtherBill : "+exc);
                        }

                        try{
                            transferBillMainTransCredit(cashCashier.getOID());
                        }catch(Exception exc){
                            addStatusText("SQLException when transferBillMainTransCredit : "+exc);
                        }

                        try{
                            transferCreditPaymentMain(cashCashier.getOID());
                        }catch(Exception exc){
                            System.out.println(exc);
                        }
                       
                        //untuk transfer costing update opie-eyek
                        try{
                            transferCosting(cashCashier.getOID());
                        }catch(Exception exc){
                            addStatusText("SQLException when transferCosting : "+exc);
                        }
                        
                    } catch(Exception exc){
                        System.out.println(exc);
                    } finally{
                         statement.close();
                    }
                    Thread.sleep(threadSleep);
                }
                
                statement = this.dbConn.createStatement();
                start = start + recordToGet ;
                sql="select * from " + PstCashCashier.TBL_CASH_CASHIER + " where " + PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID] + " > 1 " + " and "+ PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE] + " between '" + dateFrom + "' and '" + dateTo + "' limit " + start + "," + recordToGet ;
                rs = statement.executeQuery(sql);
                vecCashCashier.clear();
                while(rs.next()) {
                    CashCashier cashCashier = new CashCashier();
                    PstCashCashier.resultToObject(rs, cashCashier);
                    vecCashCashier.add(cashCashier);
                }
                rs.close();
                }
                Thread.sleep(threadSleep);
                
            }
            setStatusTxt("finished...");
            this.dbConn.close();


        }catch(Exception e){
            //System.out.println(" ! EXC : PrinterDriverLoader > run =  "+e.toString());
            addStatusText("! EXC : PrinterDriverLoader > run : "+e.toString());
            try{
                Thread.sleep(threadSleep);
                if(isRunThread()){
                    run();
                }
            }catch(Exception ex){
                //System.out.println(ex);
                addStatusText("! EXC : PrinterDriverLoader > run : "+ ex);
            }
        }
    }

    public void transferCashBalance(long oidCashCashier){
        try{
            String sql =null;
            Statement statement=null;
            Vector vecCashBalance = new Vector();
            statement = this.dbConn.createStatement();
            long start =0;
            long recordToGet=10;

           
            sql="select * from " + PstBalance.TBL_BALANCE + " where " + PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " limit " + start + "," + recordToGet ;
            ResultSet rs = statement.executeQuery(sql);

	    while(rs.next()) {
		Balance balance = new Balance();
                PstBalance.resultToObject(rs, balance);
		vecCashBalance.add(balance);
            }
            rs.close();

            while(isRunThread() && vecCashBalance!=null && vecCashBalance.size() > 0  ){
                if(!isPauseTransfer()){
                for(int idx=0;idx<vecCashBalance.size();idx++){
                    try{
                    Balance balance = (Balance) vecCashBalance.get(idx);
                    PstBalance.insertExcByOid(balance);
                    } catch(Exception exc){
                         addStatusText("SQLException when  PstBalance.insertExcByOid : "+exc);
                    } finally{
                         statement.close();
                    }
                    Thread.sleep(threadSleep);
                }
                // dataSychSql = PstDataSyncStatus.listQuery(0, maxNumber, PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION] + "=" +conOb.getOID() , null);
              
                statement = this.dbConn.createStatement();
                start = start + recordToGet;
                sql="select * from " + PstBalance.TBL_BALANCE + " where " + PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " limit " + start + "," + recordToGet ;
                rs = statement.executeQuery(sql);
                vecCashBalance.clear();
                while(rs.next()) {
                    Balance balance = new Balance();
                    PstBalance.resultToObject(rs, balance);
                    vecCashBalance.add(balance);
                }
                rs.close();
                }
                Thread.sleep(threadSleep);

            }
            
        }catch(Exception e){
            addStatusText("SQLException when  transferCashBalance : "+e);
            try{
                Thread.sleep(threadSleep);
                if(isRunThread()){
                createConn();
                transferCashBalance(oidCashCashier);
            }
            }catch(Exception ex){
               addStatusText("SQLException when transferCashBalance : "+ex);
            }
            


        }
    }
   
    public void transferBillMainTransTunai(long oidCashCashier){
        try{
            String sql =null;
            Statement statement=null;
            Vector vecBillMain = new Vector();
            statement = this.dbConn.createStatement();
            long start =0;
            long recordToGet=10;

            //mengambil jumlah bill yang ada di outlet
            //sql="select count(" +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ") as jumlah from " + PstBillMain.TBL_CASH_BILL_MAIN + " where " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " and " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = 0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = 0" ;
            //ResultSet rsCount = statement.executeQuery(sql);
            //while(rsCount.next()){
            //    dataProgres.setJmlhBill(rsCount.getInt("jumlah") + dataProgres.getJmlhBill());
           // }
            //rsCount.close();

            //mengambil jumlah bill yang sudah masuk ke server
            //sql="select count(" +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ") as jumlah from " + PstBillMain.TBL_CASH_BILL_MAIN + " where " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " and " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = 0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = 0" ;
            //Connection connection=null;
            //connection = DBHandler.getDBConnection();
            //statement = connection.createStatement();
            //rsCount = statement.executeQuery(sql);
            //while(rsCount.next()){
            //    dataProgres.setJmlhBillDone(rsCount.getInt("jumlah") + dataProgres.getJmlhBillDone());
            //}
            //rsCount.close();

            
            //statement = this.dbConn.createStatement();
           // while(dbConn.isValid(getTimeOut())==false && isRunThread()){
            //        createConn();
           //     }
            sql="select * from " + PstBillMain.TBL_CASH_BILL_MAIN + " where " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " and " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = 0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = 0" + " limit " + start + "," + recordToGet ;
            //sql="select * from " + PstBillMain.TBL_CASH_BILL_MAIN + " where " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " limit " + start + "," + recordToGet ;
            ResultSet rs = statement.executeQuery(sql);

	    while(rs.next()) {
		BillMain billMain = new BillMain();
                PstBillMain.resultToObject(rs, billMain);
		vecBillMain.add(billMain);
            }
            rs.close();

            //statement = null;
            while(isRunThread() && vecBillMain!=null && vecBillMain.size() > 0  ){
                if(!isPauseTransfer()){
                for(int idx=0;idx<vecBillMain.size();idx++){
                    try{
                    BillMain billMain = (BillMain) vecBillMain.get(idx);
                        setStatusTxt(" try to get cash bill...");
                        try{
                            PstBillMain.insertExcByOid(billMain);
                            dataProgres.setJmlhBillDone(dataProgres.getJmlhBillDone() + 1);
                        }catch(Exception exc){
                            //addStatusText("SQLException when PstBillMain.insertExcByOid : "+exc);
                        }

                        try{
                            transferBillDetail(billMain.getOID());//insert data ke tabel cash_bill_detail
                        }catch(Exception exc){
                          // addStatusText("SQLException when transferBillDetail : "+exc);
                        }

                        try{
                            transferCashPayment(billMain.getOID());//insert data ke tabel cash_payment
                        }catch(Exception exc){
                           // addStatusText("SQLException when transferCashPayment : "+exc);
                        }
                    
                        try{
                            transferCashReturnPayment(billMain.getOID());//insert data ke tabel cash_retur_payment
                        }catch(Exception exc){
                           // addStatusText("SQLException when transferCashReturnPayment : "+exc);
                        }

                        try{
                            transferCashRecipe(billMain.getOID());
                        }catch(Exception exc){
                            // addStatusText("SQLException when transferCashRecipe : "+exc);
                        }

                        try{
                            transferCashOtherCost(billMain.getOID());
                        }catch(Exception exc){
                           //  addStatusText("SQLException when transferCashOtherCost : "+exc);
                        }
                        
                    } catch(Exception exc){
                         //addStatusText("SQLException when transferBillMainTransTunai : "+exc);
                    } finally{
                         statement.close();
                    }
                    Thread.sleep(threadSleep);
                }
                // dataSychSql = PstDataSyncStatus.listQuery(0, maxNumber, PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION] + "=" +conOb.getOID() , null);
              //  while(dbConn.isValid(getTimeOut())==false && isRunThread()){
              //      createConn();
              //  }
                statement = this.dbConn.createStatement();
                start = start + recordToGet;
                sql="select * from " + PstBillMain.TBL_CASH_BILL_MAIN + " where " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " and " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = 0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = 0" + " limit " + start + "," + recordToGet ;
                rs = statement.executeQuery(sql);
                vecBillMain.clear();
                while(rs.next()) {
                    BillMain billMain = new BillMain();
                    PstBillMain.resultToObject(rs, billMain);
                    vecBillMain.add(billMain);
                }
                rs.close();
                }
                Thread.sleep(threadSleep);
            }
        }catch(Exception e){
            System.out.println(e);
            try{
                Thread.sleep(threadSleep);
                if(isRunThread()){
                createConn();
                transferBillMainTransTunai(oidCashCashier);
                }
            }catch(Exception ex){
                addStatusText("SQLException when transferBillMainTransTunai : "+ex);
            }
        }
    }

public void transferBillMainOtherBill(long oidCashCashier){
        try{
            String sql =null;
            Statement statement=null;
            Vector vecBillMain = new Vector();
            statement = this.dbConn.createStatement();
            long start =0;
            long recordToGet=10;
            
            //untuk mengambil jumlah otherbill yang ada di outlet
            //sql="select count(" +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ") as jumlah from " + PstBillMain.TBL_CASH_BILL_MAIN + " where ((" + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " <> 0) or  (" + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "= 1 )) and " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier ;
            //ResultSet rsCount = statement.executeQuery(sql);
            //while(rsCount.next()){
            //    dataProgres.setJmlOtherBill(rsCount.getInt("jumlah") + dataProgres.getJmlOtherBill());
           // }
           // rsCount.close();


            //untuk mengambil jumlah otherbill yang sudah masuk ke server
            //Connection connection=null;
            //connection = DBHandler.getDBConnection();
            //statement = connection.createStatement();
            //sql="select count(" +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ") as jumlah from " + PstBillMain.TBL_CASH_BILL_MAIN + " where ((" + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " <> 0) or  (" + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "= 1 )) and " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier ;
            //rsCount = statement.executeQuery(sql);
            //while(rsCount.next()){
            //    dataProgres.setJmlOtherBillDone(rsCount.getInt("jumlah") + dataProgres.getJmlOtherBillDone());
            //}
            //rsCount.close();

            
            //statement = this.dbConn.createStatement();
          //  while(dbConn.isValid(getTimeOut())==false && isRunThread()){
          //          createConn();
          //      }
            sql="select * from " + PstBillMain.TBL_CASH_BILL_MAIN + " where ((" + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " <> 0) or  (" + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=0 and (" + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "= 1 or "+ PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "= 2) )) and " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " limit " + start + "," + recordToGet;
            ResultSet rs = statement.executeQuery(sql);

	    while(rs.next()) {
		BillMain billMain = new BillMain();
                PstBillMain.resultToObject(rs, billMain);
		vecBillMain.add(billMain);
            }
            rs.close();

            while(isRunThread() && vecBillMain!=null && vecBillMain.size() > 0  ){
                if(!isPauseTransfer()){
                for(int idx=0;idx<vecBillMain.size();idx++){
                    try{
                        BillMain billMain = (BillMain) vecBillMain.get(idx);
                        try{
                            PstBillMain.insertExcByOid(billMain);
                            dataProgres.setJmlOtherBillDone(dataProgres.getJmlOtherBillDone() + 1);
                        }catch(Exception exc){
                            addStatusText("SQLException when  PstBalance.insertExcByOid : "+exc);
                        }
                              
                        try{
                            transferBillDetail(billMain.getOID());//insert data ke tabel cash_bill_detail
                        }catch(Exception exc){
                            addStatusText("SQLException when  PstBalance.insertExcByOid : "+exc);
                        }

                        try{
                            transferCashPayment(billMain.getOID());//insert data ke tabel cash_payment
                        }catch(Exception exc){
                            addStatusText("SQLException when  PstBalance.insertExcByOid : "+exc);
                        }

                        try{
                            transferCashReturnPayment(billMain.getOID());//insert data ke tabel cash_retur_payment
                        }catch(Exception exc){
                           addStatusText("SQLException when  PstBalance.insertExcByOid : "+exc);
                        }

                        try{
                            transferCashRecipe(billMain.getOID());
                        }catch(Exception exc){
                           addStatusText("SQLException when  PstBalance.insertExcByOid : "+exc);
                        }

                        try{
                            transferCashOtherCost(billMain.getOID());
                        }catch(Exception exc){
                            addStatusText("SQLException when  PstBalance.insertExcByOid : "+exc);
                        }
                   
                    } catch(Exception exc){
                         addStatusText("SQLException when  PstBalance.insertExcByOid : "+exc);
                    } finally{
                         statement.close();
                    }
                    Thread.sleep(threadSleep);
                }
                // dataSychSql = PstDataSyncStatus.listQuery(0, maxNumber, PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION] + "=" +conOb.getOID() , null);
              
                statement = this.dbConn.createStatement();
                start = start + recordToGet;
                sql="select * from " + PstBillMain.TBL_CASH_BILL_MAIN + " where ((" + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " <> 0) or  (" + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "= 1 )) and " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " limit " + start + "," + recordToGet;
                rs = statement.executeQuery(sql);
                vecBillMain.clear();
                while(rs.next()) {
                    BillMain billMain = new BillMain();
                    PstBillMain.resultToObject(rs, billMain);
                    vecBillMain.add(billMain);
                }
                rs.close();
                }
                Thread.sleep(threadSleep);
                
            }
        }catch(Exception e){
            addStatusText("SQLException when  PstBalance.insertExcByOid : "+e);
            try{
                Thread.sleep(threadSleep);
                if(isRunThread()){
                createConn();
                transferBillMainOtherBill(oidCashCashier);

            }
            }catch(Exception ex){
                addStatusText("SQLException when  PstBalance.insertExcByOid : "+ex);
            }
        }
    }

public void transferBillMainTransCredit(long oidCashCashier){
        try{
            String sql =null;
            Statement statement=null;
            Vector vecBillMain = new Vector();
            statement = this.dbConn.createStatement();
            long start =0;
            long recordToGet=10;

            //untuk mengambil jumlah creditBill yang ada di outlet
           // sql="select count(" +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ") as jumlah from " + PstBillMain.TBL_CASH_BILL_MAIN + " where " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " and " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = 0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 1 " ;
            //ResultSet rsCount = statement.executeQuery(sql);
            //while(rsCount.next()){
           //     dataProgres.setJmlhCredit(rsCount.getInt("jumlah") + dataProgres.getJmlhCredit());
           // }
           // rsCount.close();


            //untuk mengambil jumlah creditBill yang sudah masuk ke server
            //Connection connection=null;
            //connection = DBHandler.getDBConnection();
            //statement = connection.createStatement();
            //sql="select count(" +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ") as jumlah from " + PstBillMain.TBL_CASH_BILL_MAIN + " where " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " and " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = 0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 1 " ;
            //rsCount = statement.executeQuery(sql);
            //while(rsCount.next()){
            //    dataProgres.setJmlhCreditDone(rsCount.getInt("jumlah") + dataProgres.getJmlhCreditDone());
            //}
            //rsCount.close();

            //statement = this.dbConn.createStatement();
           // while(dbConn.isValid(getTimeOut())==false && isRunThread()){
           //         createConn();
           //     }
            sql="select * from " + PstBillMain.TBL_CASH_BILL_MAIN + " where " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " and " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = 0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 1 limit " + start + "," + recordToGet ;
            //sql="select * from " + PstBillMain.TBL_CASH_BILL_MAIN + " where " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " limit " + start + "," + recordToGet ;
            ResultSet rs = statement.executeQuery(sql);

	    while(rs.next()) {
		BillMain billMain = new BillMain();
                PstBillMain.resultToObject(rs, billMain);
		vecBillMain.add(billMain);
            }
            rs.close();

            while(isRunThread() && vecBillMain!=null && vecBillMain.size() > 0  ){
                if(!isPauseTransfer()){
                for(int idx=0;idx<vecBillMain.size();idx++){
                    BillMain billMain = (BillMain) vecBillMain.get(idx);
                    try{
                        try{
                            PstBillMain.insertExcByOid(billMain);
                            dataProgres.setJmlhCreditDone(dataProgres.getJmlhCreditDone() + 1);
                        }catch(Exception exc){
                            addStatusText("SQLException when transferBillMainTransCredit  PstBillMain.insertExcByOid : "+exc);
                        }

                        try{
                            transferBillDetail(billMain.getOID());//insert data ke tabel cash_bill_detail
                        }catch(Exception exc){
                            addStatusText("SQLException when transferBillMainTransCredit  transferBillDetail : "+exc);
                        }

                        try{
                            transferCashPayment(billMain.getOID());//insert data ke tabel cash_payment
                        }catch(Exception exc){
                            addStatusText("SQLException when transferBillMainTransCredit transferCashPayment(billMain.getOID()) "+exc);
                        }

                        try{
                            transferCashReturnPayment(billMain.getOID());//insert data ke tabel cash_retur_payment
                        }catch(Exception exc){
                            addStatusText("SQLException when transferBillMainTransCredit transferCashReturnPayment "+exc);
                        }

                        try{
                            transferCashRecipe(billMain.getOID());
                        }catch(Exception exc){
                            addStatusText("SQLException when transferBillMainTransCredit transferCashRecipe "+exc);
                        }

                        try{
                            transferCashOtherCost(billMain.getOID());
                        }catch(Exception exc){
                             addStatusText("SQLException when transferBillMainTransCredit transferCashOtherCost "+exc);
                        }
                                       
                    } catch(Exception exc){
                        addStatusText("SQLException when transferBillMainTransCredit  "+exc);
                    } finally{
                         statement.close();
                    }
                    Thread.sleep(threadSleep);
                }
                // dataSychSql = PstDataSyncStatus.listQuery(0, maxNumber, PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION] + "=" +conOb.getOID() , null);
              // while(dbConn.isValid(getTimeOut())==false && isRunThread()){
              //      createConn();
              //  }
                statement = this.dbConn.createStatement();
                start = start + recordToGet;
                sql="select * from " + PstBillMain.TBL_CASH_BILL_MAIN + " where " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " and " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = 0 and " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 1 limit " + start + "," + recordToGet ;
                rs = statement.executeQuery(sql);
                vecBillMain.clear();
                while(rs.next()) {
                    BillMain billMain = new BillMain();
                    PstBillMain.resultToObject(rs, billMain);
                    vecBillMain.add(billMain);
                }
                rs.close();
                }
                Thread.sleep(threadSleep);

            }
        }catch(Exception e){
            addStatusText("SQLException when transferBillMainTransCredit  "+e);
            try{
                Thread.sleep(threadSleep);
                if(isRunThread()){
                createConn();
                transferBillMainTransCredit(oidCashCashier);
                }
            }catch(Exception ex){
                addStatusText("SQLException when transferBillMainTransCredit  "+ex);
            }
        }
    }


    public void transferBillDetail(long oidBillMain){
        try{
            String sql =null;
            Statement statement=null;
            Vector vecBillDetail = new Vector();
            statement = this.dbConn.createStatement();
            long start =0;
            long recordToGet=10;

           
            sql="select * from " + PstBillDetail.TBL_CASH_BILL_DETAIL + " where " +PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +" = " + oidBillMain + " limit " + start + "," + recordToGet ;
            ResultSet rs = statement.executeQuery(sql);

	    while(rs.next()) {
		Billdetail billDetail = new Billdetail();
                PstBillDetail.resultToObject(rs, billDetail);
		vecBillDetail.add(billDetail);
            }
            rs.close();

            while(isRunThread() && vecBillDetail!=null && vecBillDetail.size() > 0  ){
                if(!isPauseTransfer()){
                for(int idx=0;idx<vecBillDetail.size();idx++){
                    try{
                    Billdetail billdetail = (Billdetail) vecBillDetail.get(idx);
                        try{
                            PstBillDetail.insertExcByOid(billdetail);
                        } catch(Exception exc){
                            addStatusText("SQLException when PstBillDetail.insertExcByOid : "+exc);
                        }

                        try{
                            transferBillDetailcode(oidBillMain);
                        }catch(Exception exc){
                            //System.out.println(exc);
                            addStatusText("SQLException when transferBillDetailcode : "+exc);
                        }
                    
                    } catch(Exception exc){
                          addStatusText("SQLException when transferBillDetail : "+exc);
                    } finally{
                         statement.close();
                    }
                    Thread.sleep(threadSleep);
                }
                // dataSychSql = PstDataSyncStatus.listQuery(0, maxNumber, PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION] + "=" +conOb.getOID() , null);
               
                statement = this.dbConn.createStatement();
                start = start + recordToGet ;
                sql="select * from " + PstBillDetail.TBL_CASH_BILL_DETAIL + " where " +PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +" = " + oidBillMain + " limit " + start + "," + recordToGet ;
                rs = statement.executeQuery(sql);
                vecBillDetail.clear();
                while(rs.next()) {
                    Billdetail billdetail = new Billdetail();
                    PstBillDetail.resultToObject(rs, billdetail);
                    vecBillDetail.add(billdetail);
                }
                rs.close();
                }
                Thread.sleep(threadSleep);
                
            }
        }catch(Exception e){
            addStatusText("SQLException when transferBillDetail : "+e);
            try{
                Thread.sleep(threadSleep);
                if( isRunThread()){
                createConn();
                transferBillDetail(oidBillMain);

                }
            }catch(Exception ex){
                addStatusText("SQLException when transferBillDetail : "+ex);
            }
        }

    }

    public void transferCashPayment(long oidBillMain){
        try{
            String sql =null;
            Statement statement=null;
            Vector vecCashPayment = new Vector();
            statement = this.dbConn.createStatement();
            long start =0;
            long recordToGet=10;

            
            sql="select * from " + PstCashPayment.TBL_PAYMENT + " where " + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID] + " = " + oidBillMain + " limit " + start + "," + recordToGet ;
            ResultSet rs = statement.executeQuery(sql);

	    while(rs.next()) {
		CashPayments1 cashPayment = new CashPayments1();
                PstCashPayment1.resultToObject(rs, cashPayment);
		vecCashPayment.add(cashPayment);
            }
            rs.close();

            while(isRunThread() && vecCashPayment!=null && vecCashPayment.size() > 0  ){
                if(!isPauseTransfer()){
                for(int idx=0;idx<vecCashPayment.size();idx++){
                    try{
                    CashPayments1 cashpayment = (CashPayments1) vecCashPayment.get(idx);
                        try{
                            PstCashPayment1.insertExcByOid(cashpayment);
                        }catch(Exception exc){
                            addStatusText("SQLException when  PstBalance.insertExcByOid : "+exc);
                        }
                    
                        try{
                            transferCashCreditCard(cashpayment.getOID());
                        }catch(Exception exc){
                            addStatusText("SQLException when  transferCashCreditCard : "+exc);
                        }
                                        
                    } catch(Exception exc){
                         addStatusText("SQLException when transferCashPayment : "+exc);
                    } finally{
                         statement.close();
                    }
                    Thread.sleep(threadSleep);
                }
                // dataSychSql = PstDataSyncStatus.listQuery(0, maxNumber, PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION] + "=" +conOb.getOID() , null);
              
                statement = this.dbConn.createStatement();
                start = start + recordToGet;
                sql="select * from " + PstCashPayment.TBL_PAYMENT + " where " + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID] + " = " + oidBillMain + " limit " + start + "," + recordToGet ;
                rs = statement.executeQuery(sql);
                vecCashPayment.clear();
                while(rs.next()) {
                    CashPayments1 cashPayment = new CashPayments1();
                    PstCashPayment1.resultToObject(rs, cashPayment);
                    vecCashPayment.add(cashPayment);
                }
                rs.close();
                }
                Thread.sleep(threadSleep);
                
            }
        }catch(Exception e){
           addStatusText("SQLException when transferCashPayment : "+e);
            try{
                Thread.sleep(threadSleep);
                if(isRunThread()){
                createConn();
                transferCashPayment(oidBillMain);

                }
            }catch(Exception ex){
                addStatusText("SQLException when transferCashPayment : "+ex);
            }
        }
    }

    public void transferCashReturnPayment(long oidBillMain){
         try{
            String sql =null;
            Statement statement=null;
            Vector vecCashreturnPayment = new Vector();
            statement = this.dbConn.createStatement();
            long start =0;
            long recordToGet=10;

           
            sql="select * from " + PstCashReturn.TBL_RETURN +" where " + PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID] + " = " + oidBillMain + " limit " + start + "," + recordToGet ;
            ResultSet rs = statement.executeQuery(sql);

	    while(rs.next()) {
		CashReturn cashReturn = new CashReturn();
                PstCashReturn.resultToObject(rs, cashReturn);
		vecCashreturnPayment.add(cashReturn);
            }
            rs.close();

            while(isRunThread() && vecCashreturnPayment!=null && vecCashreturnPayment.size() > 0  ){
                if(!isPauseTransfer()){
                for(int idx=0;idx<vecCashreturnPayment.size();idx++){
                    try{
                    CashReturn cashReturn = (CashReturn) vecCashreturnPayment.get(idx);
                    PstCashReturn.insertExcByOid(cashReturn);
                    } catch(Exception exc){
                         addStatusText("SQLException when  PstCashReturn.insertExcByOid : "+exc);
                    } finally{
                         statement.close();
                    }
                    Thread.sleep(threadSleep);
                }
                // dataSychSql = PstDataSyncStatus.listQuery(0, maxNumber, PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION] + "=" +conOb.getOID() , null);
               
                statement = this.dbConn.createStatement();
                start = start + recordToGet;
                sql="select * from " + PstCashReturn.TBL_RETURN +" where " + PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID] + " = " + oidBillMain + " limit " + start + "," + recordToGet ;
                rs = statement.executeQuery(sql);
                vecCashreturnPayment.clear();
                while(rs.next()) {
                    CashReturn cashReturn = new CashReturn();
                    PstCashReturn.resultToObject(rs, cashReturn);
                    vecCashreturnPayment.add(cashReturn);
                }
                rs.close();
                }
                Thread.sleep(threadSleep);
                
            }
        }catch(Exception e){
            addStatusText("SQLException when transferCashReturnPayment : "+e);
            try{
                if(isRunThread()){
                Thread.sleep(threadSleep);
                createConn();
                transferCashReturnPayment(oidBillMain);

                }
            }catch(Exception ex){
                addStatusText("SQLException when transferCashReturnPayment : "+ex);
            }
        }
    }

    public void transferBillDetailcode(long oidSellItem){
         try{
            String sql =null;
            Statement statement=null;
            Vector vecBillDetailCode = new Vector();
            statement = this.dbConn.createStatement();
            long start =0;
            long recordToGet=10;

           
            sql="select * from " + PstBillDetailCode.TBL_CASH_BILL_DETAIL_CODE + " where " + PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_SALE_ITEM_ID] + " = " + oidSellItem + " limit " + start + "," + recordToGet ;
            ResultSet rs = statement.executeQuery(sql);

	    while(rs.next()) {
		BillDetailCode billDetailCode = new BillDetailCode();
                PstBillDetailCode.resultToObject(rs, billDetailCode);
		vecBillDetailCode.add(billDetailCode);
            }
            rs.close();

            while(isRunThread() && vecBillDetailCode!=null && vecBillDetailCode.size() > 0  ){
                if(!isPauseTransfer()){
                for(int idx=0;idx<vecBillDetailCode.size();idx++){
                    try{
                    BillDetailCode billDetailCode = (BillDetailCode) vecBillDetailCode.get(idx);
                    PstBillDetailCode.insertExcByOid(billDetailCode);
                    } catch(Exception exc){
                         addStatusText("SQLException when  PstBillDetailCode.insertExcByOid : "+exc);
                    } finally{
                         statement.close();
                    }
                    Thread.sleep(threadSleep);
                }
                // dataSychSql = PstDataSyncStatus.listQuery(0, maxNumber, PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION] + "=" +conOb.getOID() , null);
               
                statement = this.dbConn.createStatement();
                start = start + recordToGet;
                sql="select * from " + PstBillDetailCode.TBL_CASH_BILL_DETAIL_CODE + " where " + PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_SALE_ITEM_ID] + " = " + oidSellItem + " limit " + start + "," + recordToGet ;
                rs = statement.executeQuery(sql);
                vecBillDetailCode.clear();
                while(rs.next()) {
                    BillDetailCode billDetailCode = new BillDetailCode();
                    PstBillDetailCode.resultToObject(rs, billDetailCode);
                    vecBillDetailCode.add(billDetailCode);
                }
                rs.close();
                }
                Thread.sleep(threadSleep);

            }
        }catch(Exception e){
            System.out.println(e);
            try{
                if( isRunThread()){
                Thread.sleep(threadSleep);
                createConn();
                transferBillDetailcode(oidSellItem);

                }
            }catch(Exception ex){
                addStatusText("SQLException when  PstBillDetailCode.insertExcByOid : "+ex);
            }
        }

    }

    public void transferPendingOrder(long oidCashCashier){
         try{
            String sql =null;
            Statement statement=null;
            Vector vecPendingOrder = new Vector();
            statement = this.dbConn.createStatement();
            long start =0;
            long recordToGet=10;

           
            sql="select * from " + PstPendingOrder.TBL_CASH_PENDING_ORDER + " where " + PstPendingOrder.fieldNames[PstPendingOrder.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " limit " + start + "," + recordToGet ;
            ResultSet rs = statement.executeQuery(sql);

	    while(rs.next()) {
		PendingOrder pendingOrder = new PendingOrder();
                PstPendingOrder.resultToObject(rs, pendingOrder);
		vecPendingOrder.add(pendingOrder);
            }
            rs.close();

            while(isRunThread() && vecPendingOrder!=null && vecPendingOrder.size() > 0  ){
                if(!isPauseTransfer()){
                for(int idx=0;idx<vecPendingOrder.size();idx++){
                    try{
                    PendingOrder pendingOrder = (PendingOrder) vecPendingOrder.get(idx);
                    PstPendingOrder.insertExcByOid(pendingOrder);

                    } catch(Exception exc){
                         System.out.println(exc);
                    } finally{
                         statement.close();
                    }
                    Thread.sleep(threadSleep);
                }
                // dataSychSql = PstDataSyncStatus.listQuery(0, maxNumber, PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION] + "=" +conOb.getOID() , null);
              
                statement = this.dbConn.createStatement();
                start = start + recordToGet;
                sql="select * from " + PstPendingOrder.TBL_CASH_PENDING_ORDER + " where " + PstPendingOrder.fieldNames[PstPendingOrder.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " limit " + start + "," + recordToGet ;
                rs = statement.executeQuery(sql);
                vecPendingOrder.clear();
                while(rs.next()) {
                    PendingOrder pendingOrder = new PendingOrder();
                    PstPendingOrder.resultToObject(rs, pendingOrder);
                    vecPendingOrder.add(pendingOrder);
                }
                rs.close();
                }
                Thread.sleep(threadSleep);

            }
        }catch(Exception e){
            System.out.println(e);
            try{
                if( isRunThread()){
                Thread.sleep(threadSleep);
                createConn();
                transferPendingOrder(oidCashCashier);

                }
            }catch(Exception ex){
                System.out.println(ex);
            }
        }

    }


    public void transferCashOtherCost(long oidBillMain){
         try{
            String sql =null;
            Statement statement=null;
            Vector vecOtherCost = new Vector();
            statement = this.dbConn.createStatement();
            long start =0;
            long recordToGet=10;

            
            sql="select * from " + PstOtherCost.TBL_CASH_OTHER_COST +" where " + PstOtherCost.fieldNames[PstOtherCost.FLD_CASH_BILL_MAIN_ID] + " = " + oidBillMain + " limit " + start + "," + recordToGet ;
            ResultSet rs = statement.executeQuery(sql);

	    while(rs.next()) {
		OtherCost otherCost = new OtherCost();
                PstOtherCost.resultToObject(rs, otherCost);
		vecOtherCost.add(otherCost);
            }
            rs.close();

            while(isRunThread() && vecOtherCost!=null && vecOtherCost.size() > 0  ){
                if(!isPauseTransfer()){
                for(int idx=0;idx<vecOtherCost.size();idx++){
                    try{
                    OtherCost otherCost = (OtherCost) vecOtherCost.get(idx);
                    PstOtherCost.insertExc(otherCost);

                    } catch(Exception exc){
                        addStatusText("SQLException when PstOtherCost.insertExc(otherCost); : "+exc);
                    } finally{
                         statement.close();
                    }
                    Thread.sleep(threadSleep);
                }
                // dataSychSql = PstDataSyncStatus.listQuery(0, maxNumber, PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION] + "=" +conOb.getOID() , null);
               
                statement = this.dbConn.createStatement();
                start = start + recordToGet;
                sql="select * from " + PstOtherCost.TBL_CASH_OTHER_COST +" where " + PstOtherCost.fieldNames[PstOtherCost.FLD_CASH_BILL_MAIN_ID] + " = " + oidBillMain + " limit " + start + "," + recordToGet ;
                rs = statement.executeQuery(sql);
                vecOtherCost.clear();
                while(rs.next()) {
                    OtherCost otherCost = new OtherCost();
                    PstOtherCost.resultToObject(rs, otherCost);
                    vecOtherCost.add(otherCost);
                }
                rs.close();
                }
                Thread.sleep(threadSleep);

            }
        }catch(Exception e){
            addStatusText("SQLException when transferCashOtherCost : "+e);
            try{
                if(isRunThread()){
                Thread.sleep(threadSleep);
                createConn();
                transferCashOtherCost(oidBillMain);

                }
            }catch(Exception ex){
               addStatusText("SQLException when transferCashOtherCost : "+ex);
            }
        }
    }

    public void transferCashCreditCard(long oidPaymentMain){
         try{
            String sql =null;
            Statement statement=null;
            Vector vecCreditCard = new Vector();
            statement = this.dbConn.createStatement();
            long start =0;
            long recordToGet=10;

            
            sql="select * from " + PstCashCreditCard.TBL_CREDIT_CARD +" where " + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_PAYMENT_ID] + " = " + oidPaymentMain + " limit " + start + "," + recordToGet ;
            ResultSet rs = statement.executeQuery(sql);

	    while(rs.next()) {
		CashCreditCard cashCreditCard = new CashCreditCard();
                PstCashCreditCard.resultToObject(rs, cashCreditCard);
		vecCreditCard.add(cashCreditCard);
            }
            rs.close();

            while(isRunThread() && vecCreditCard!=null && vecCreditCard.size() > 0  ){
                if(!isPauseTransfer()){
                for(int idx=0;idx<vecCreditCard.size();idx++){
                    try{
                    CashCreditCard cashCreditCard = (CashCreditCard) vecCreditCard.get(idx);
                    PstCashCreditCard.insertExcByOid(cashCreditCard);

                    } catch(Exception exc){
                         addStatusText("SQLException when PstCashCreditCard.insertExcByOid : "+exc);
                    } finally{
                         statement.close();
                    }
                    Thread.sleep(threadSleep);
                }
                // dataSychSql = PstDataSyncStatus.listQuery(0, maxNumber, PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION] + "=" +conOb.getOID() , null);
              
                statement = this.dbConn.createStatement();
                start = start + recordToGet;
                sql="select * from " + PstCashCreditCard.TBL_CREDIT_CARD +" where " + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_PAYMENT_ID] + " = " + oidPaymentMain + " limit " + start + "," + recordToGet ;
                rs = statement.executeQuery(sql);
                vecCreditCard.clear();
                while(rs.next()) {
                    CashCreditCard cashCreditCard = new CashCreditCard();
                    PstCashCreditCard.resultToObject(rs, cashCreditCard);
                    vecCreditCard.add(cashCreditCard);
                }
                rs.close();
                }
                Thread.sleep(threadSleep);

            }
        }catch(Exception e){
             addStatusText("SQLException when  PstBalance.insertExcByOid : "+e);
            try{
                if(isRunThread()){
                Thread.sleep(threadSleep);
                createConn();
                transferCashCreditCard(oidPaymentMain);

                }
            }catch(Exception ex){
               addStatusText("SQLException when  PstBalance.insertExcByOid : "+ex);
            }
        }
    }

    public void transferCreditPaymentMain(long oidCashCashier){
         try{
            String sql =null;
            Statement statement=null;
            Vector vecCreditPaymentMain = new Vector();
            statement = this.dbConn.createStatement();
            long start =0;
            long recordToGet=10;

          
            sql="select * from " + PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN +" where " + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " limit " + start + "," + recordToGet ;
            ResultSet rs = statement.executeQuery(sql);

	    while(rs.next()) {
		CreditPaymentMain creditPaymentMain = new CreditPaymentMain();
                PstCreditPaymentMain.resultToObject(rs, creditPaymentMain);
		vecCreditPaymentMain.add(creditPaymentMain);
            }
            rs.close();

            while(isRunThread() && vecCreditPaymentMain!=null && vecCreditPaymentMain.size() > 0  ){
                if(!isPauseTransfer()){
                for(int idx=0;idx<vecCreditPaymentMain.size();idx++){
                    try{
                        CreditPaymentMain creditPaymentMain = (CreditPaymentMain) vecCreditPaymentMain.get(idx);
                        try{
                            PstCreditPaymentMain.insertExcByOid(creditPaymentMain);
                        }catch(Exception exc){
                            System.out.println(exc);
                        }

                        try{
                            transferCreditPayment(creditPaymentMain.getOID());
                        }catch(Exception exc){
                            System.out.println(exc);
                        }

                        try{
                            transferCreditPaymentInfo(creditPaymentMain.getOID());
                        }catch(Exception exc){
                            System.out.println(exc);
                        }
                        

                    } catch(Exception exc){
                         System.out.println(exc);
                    } finally{
                         statement.close();
                    }
                    Thread.sleep(threadSleep);
                }
                // dataSychSql = PstDataSyncStatus.listQuery(0, maxNumber, PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION] + "=" +conOb.getOID() , null);
              
                statement = this.dbConn.createStatement();
                start = start + recordToGet;
                sql="select * from " + PstCreditPaymentMain.TBL_CASH_CREDIT_PAYMENT_MAIN +" where " + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " limit " + start + "," + recordToGet ;
                rs = statement.executeQuery(sql);
                vecCreditPaymentMain.clear();
                while(rs.next()) {
                    CreditPaymentMain creditPaymentMain = new CreditPaymentMain();
                    PstCreditPaymentMain.resultToObject(rs, creditPaymentMain);
                    vecCreditPaymentMain.add(creditPaymentMain);
                }
                rs.close();
                }
                Thread.sleep(threadSleep);

            }
        }catch(Exception e){
            System.out.println(e);
            try{
                if( isRunThread()){
                Thread.sleep(threadSleep);
                createConn();
                transferCreditPaymentMain(oidCashCashier);

                }
            }catch(Exception ex){
                System.out.println(ex);
            }
        }
    }

    
    public void transferCosting(long oidCashCashier){
         try{
            String sql =null;
            Statement statement=null;
            Vector vecCosting = new Vector();
            statement = this.dbConn.createStatement();
            long start =0;
            long recordToGet=10;
            
            sql="select * from " + PstMatCosting.TBL_COSTING +" where " + PstMatCosting.fieldNames[PstMatCosting.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " limit " + start + "," + recordToGet ;
            ResultSet rs = statement.executeQuery(sql);

	    while(rs.next()) {
		MatCosting matCosting = new MatCosting();
                PstMatCosting.resultToObject(rs, matCosting);
		vecCosting.add(matCosting);
            }
            rs.close();

            while(isRunThread() && vecCosting!=null && vecCosting.size() > 0  ){
                if(!isPauseTransfer()){
                for(int idx=0;idx<vecCosting.size();idx++){
                    try{
                        MatCosting matCosting = (MatCosting) vecCosting.get(idx);
                        try{
                            PstMatCosting.insertExcByOid(matCosting);
                        }catch(Exception exc){
                            System.out.println(exc);
                        }

                        try{
                            transferDetailCosting(matCosting.getOID());
                        }catch(Exception exc){
                            System.out.println(exc);
                        }

                    } catch(Exception exc){
                         System.out.println(exc);
                    } finally{
                         statement.close();
                    }
                    Thread.sleep(threadSleep);
                }
                // dataSychSql = PstDataSyncStatus.listQuery(0, maxNumber, PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION] + "=" +conOb.getOID() , null);
              
                statement = this.dbConn.createStatement();
                start = start + recordToGet;
                sql="select * from " + PstMatCosting.TBL_COSTING +" where " + PstMatCosting.fieldNames[PstMatCosting.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " limit " + start + "," + recordToGet ;
                rs = statement.executeQuery(sql);
                vecCosting.clear();
                while(rs.next()) {
                    MatCosting matCosting = new MatCosting();
                    PstMatCosting.resultToObject(rs, matCosting);
                    vecCosting.add(matCosting);
                }
                rs.close();
                }
                Thread.sleep(threadSleep);

            }
        }catch(Exception e){
            System.out.println(e);
            try{
                if( isRunThread()){
                Thread.sleep(threadSleep);
                createConn();
                transferCosting(oidCashCashier);

                }
            }catch(Exception ex){
                System.out.println(ex);
            }
        }
    }
    
    
    
    
    public void transferCreditPayment(long oidCreditPaymentMain){
         try{
            String sql =null;
            Statement statement=null;
            Vector vecCreditPayment = new Vector();
            statement = this.dbConn.createStatement();
            long start =0;
            long recordToGet=10;

          
            sql="select * from " + PstCashCreditPayment.TBL_PAYMENT +" where " + PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CREDIT_MAIN_ID] + " = " + oidCreditPaymentMain + " limit " + start + "," + recordToGet ;
            ResultSet rs = statement.executeQuery(sql);

	    while(rs.next()) {
		CashCreditPaymentsDinamis cashCreditPayments = new CashCreditPaymentsDinamis();
                PstCashCreditPaymentDinamis.resultToObject(rs, cashCreditPayments);
		vecCreditPayment.add(cashCreditPayments);
            }
            rs.close();

            while(isRunThread() && vecCreditPayment!=null && vecCreditPayment.size() > 0  ){
                if(!isPauseTransfer()){
                for(int idx=0;idx<vecCreditPayment.size();idx++){
                    try{
                    CashCreditPaymentsDinamis cashCreditPayment = (CashCreditPaymentsDinamis) vecCreditPayment.get(idx);
                    PstCashCreditPaymentDinamis.insertExcByOid(cashCreditPayment);

                    } catch(Exception exc){
                         System.out.println(exc);
                    } finally{
                         statement.close();
                    }
                    Thread.sleep(threadSleep);
                }
                // dataSychSql = PstDataSyncStatus.listQuery(0, maxNumber, PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION] + "=" +conOb.getOID() , null);
              
                statement = this.dbConn.createStatement();
                start = start + recordToGet;
                sql="select * from " + PstCashCreditPayment.TBL_PAYMENT +" where " + PstCashCreditPayment.fieldNames[PstCashCreditPayment.FLD_CREDIT_MAIN_ID] + " = " + oidCreditPaymentMain + " limit " + start + "," + recordToGet ;
                rs = statement.executeQuery(sql);
                vecCreditPayment.clear();
               while(rs.next()) {
                    CashCreditPaymentsDinamis cashCreditPayments = new CashCreditPaymentsDinamis();
                    PstCashCreditPaymentDinamis.resultToObject(rs, cashCreditPayments);
                    vecCreditPayment.add(cashCreditPayments);
                }
                rs.close();
                }
                Thread.sleep(threadSleep);

            }
        }catch(Exception e){
            System.out.println(e);
            try{
                if(isRunThread()){
                Thread.sleep(threadSleep);
                createConn();
                transferCreditPayment(oidCreditPaymentMain);

                }
            }catch(Exception ex){
                System.out.println(ex);
            }
        }
    }
    
    public void transferDetailCosting(long oidCostingMaterial){
         try{
            String sql =null;
            Statement statement=null;
            Vector vecCostingMaterialDetail = new Vector();
            statement = this.dbConn.createStatement();
            long start =0;
            long recordToGet=10;

          
            sql="select * from " + PstMatCostingItem.TBL_MAT_COSTING_ITEM +" where " + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] + " = " + oidCostingMaterial + " limit " + start + "," + recordToGet ;
            ResultSet rs = statement.executeQuery(sql);

	    while(rs.next()) {
		MatCostingItem matCostingItem = new MatCostingItem();
                PstMatCostingItem.resultToObject(rs, matCostingItem);
		vecCostingMaterialDetail.add(matCostingItem);
            }
            rs.close();

            while(isRunThread() && vecCostingMaterialDetail!=null && vecCostingMaterialDetail.size() > 0  ){
                if(!isPauseTransfer()){
                for(int idx=0;idx<vecCostingMaterialDetail.size();idx++){
                    try{
                    MatCostingItem matCostingItem = (MatCostingItem) vecCostingMaterialDetail.get(idx);
                    PstMatCostingItem.insertExcByOid(matCostingItem);

                    } catch(Exception exc){
                         System.out.println(exc);
                    } finally{
                         statement.close();
                    }
                    Thread.sleep(threadSleep);
                }
                // dataSychSql = PstDataSyncStatus.listQuery(0, maxNumber, PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION] + "=" +conOb.getOID() , null);
              
                statement = this.dbConn.createStatement();
                start = start + recordToGet;
                sql="select * from " + PstMatCostingItem.TBL_MAT_COSTING_ITEM +" where " + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] + " = " + oidCostingMaterial + " limit " + start + "," + recordToGet ;
                rs = statement.executeQuery(sql);
                vecCostingMaterialDetail.clear();
                while(rs.next()) {
                    MatCostingItem matCostingItem = new MatCostingItem();
                    PstMatCostingItem.resultToObject(rs, matCostingItem);
                    vecCostingMaterialDetail.add(matCostingItem);
                }
                rs.close();
                }
                Thread.sleep(threadSleep);

            }
        }catch(Exception e){
            System.out.println(e);
            try{
                if(isRunThread()){
                Thread.sleep(threadSleep);
                createConn();
                transferDetailCosting(oidCostingMaterial);

                }
            }catch(Exception ex){
                System.out.println(ex);
            }
        }
    }

    public void transferCreditPaymentInfo(long oidCashCreditPayment){
         try{
            String sql =null;
            Statement statement=null;
            Vector vecCashCreditPayment = new Vector();
            statement = this.dbConn.createStatement();
            long start =0;
            long recordToGet=10;

           
            sql="select * from " + PstCashCreditPaymentInfo.TBL_CREDIT_PAYMENT_INFO +" where " + PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CASH_CREDIT_PAYMENT_ID] + " = " + oidCashCreditPayment + " limit " + start + "," + recordToGet ;
            ResultSet rs = statement.executeQuery(sql);

	    while(rs.next()) {
		CashCreditPaymentInfo cashCreditPaymentInfo = new CashCreditPaymentInfo();
                PstCashCreditPaymentInfo.resultToObject(rs, cashCreditPaymentInfo);
		vecCashCreditPayment.add(cashCreditPaymentInfo);
            }
            rs.close();

            while(isRunThread() && vecCashCreditPayment!=null && vecCashCreditPayment.size() > 0  ){
                if(!isPauseTransfer()){
                for(int idx=0;idx<vecCashCreditPayment.size();idx++){
                    try{
                    CashCreditPaymentInfo cashCreditPaymentInfo = (CashCreditPaymentInfo) vecCashCreditPayment.get(idx);
                    PstCashCreditPaymentInfo.insertExcByOid(cashCreditPaymentInfo);

                    } catch(Exception exc){
                         System.out.println(exc);
                    } finally{
                         statement.close();
                    }
                    Thread.sleep(threadSleep);
                }
                // dataSychSql = PstDataSyncStatus.listQuery(0, maxNumber, PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION] + "=" +conOb.getOID() , null);
               
                statement = this.dbConn.createStatement();
                start = start + recordToGet;
                sql="select * from " + PstCashCreditPaymentInfo.TBL_CREDIT_PAYMENT_INFO +" where " + PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CASH_CREDIT_PAYMENT_ID] + " = " + oidCashCreditPayment + " limit " + start + "," + recordToGet ;
                rs = statement.executeQuery(sql);
                vecCashCreditPayment.clear();
                while(rs.next()) {
                    CashCreditPaymentInfo cashCreditPaymentInfo = new CashCreditPaymentInfo();
                    PstCashCreditPaymentInfo.resultToObject(rs, cashCreditPaymentInfo);
                    vecCashCreditPayment.add(cashCreditPaymentInfo);
                }
                rs.close();
                }
                Thread.sleep(threadSleep);

            }
        }catch(Exception e){
            System.out.println(e);
            try{
                if(isRunThread()){
                Thread.sleep(threadSleep);
                createConn();
                transferCreditPaymentInfo(oidCashCreditPayment);

                }
            }catch(Exception ex){
                System.out.println(ex);
            }
        }
    }

    public void transferCashRecipe(long oidBillMain){
         try{
            String sql =null;
            Statement statement=null;
            Vector vecCashRecipe = new Vector();
            statement = this.dbConn.createStatement();
            long start =0;
            long recordToGet=10;

            
            sql="select * from " + PstRecipe.TBL_CASH_RECIPE +" where " + PstRecipe.fieldNames[PstRecipe.FLD_CASH_BILL_MAIN_ID] + " = " + oidBillMain + " limit " + start + "," + recordToGet ;
            ResultSet rs = statement.executeQuery(sql);

	    while(rs.next()) {
		Recipe recipe = new Recipe();
                PstRecipe.resultToObject(rs, recipe);
		vecCashRecipe.add(recipe);
            }
            rs.close();

            while(isRunThread() && vecCashRecipe!=null && vecCashRecipe.size() > 0  ){
                if(!isPauseTransfer()){
                for(int idx=0;idx<vecCashRecipe.size();idx++){
                    try{
                    Recipe recipe = (Recipe) vecCashRecipe.get(idx);
                    PstRecipe.insertExcByOid(recipe);
                    } catch(Exception exc){
                        addStatusText("SQLException when  PstRecipe.insertExcByOid(recipe) : "+exc);
                    } finally{
                         statement.close();
                    }
                    Thread.sleep(threadSleep);
                }
                // dataSychSql = PstDataSyncStatus.listQuery(0, maxNumber, PstDataSyncStatus.fieldNames[PstDataSyncStatus.FLD_ID_DBCONNECTION] + "=" +conOb.getOID() , null);
               
                statement = this.dbConn.createStatement();
                start = start + recordToGet;
                sql="select * from " + PstRecipe.TBL_CASH_RECIPE +" where " + PstRecipe.fieldNames[PstRecipe.FLD_CASH_BILL_MAIN_ID] + " = " + oidBillMain + " limit " + start + "," + recordToGet ;
                rs = statement.executeQuery(sql);
                vecCashRecipe.clear();
                while(rs.next()) {
                    Recipe recipe = new Recipe();
                    PstRecipe.resultToObject(rs, recipe);
                    vecCashRecipe.add(recipe);
                }
                rs.close();
                }
                Thread.sleep(threadSleep);

            }
        }catch(Exception e){
            addStatusText("SQLException when transferCashRecipe : "+e);
            try{
                if(isRunThread()){
                Thread.sleep(threadSleep);
                createConn();
                transferCashRecipe(oidBillMain);
                }
            }catch(Exception ex){
                 addStatusText("SQLException when transferCashRecipe : "+ex);
            }
        }
    }


    public int getThreadSleep(){
        return threadSleep;
    }
    
    public void setThreadSleep(int threadSleep){
        this.threadSleep = threadSleep;
    }
 
    public int getErrCode(){ return errCode; }
    
    public void setErrCode(int errCode){ this.errCode = errCode; }

    /**
     * @return the runThread
     */
    public boolean isRunThread() {
        return runThread;
    }

    /**
     * @param runThread the runThread to set
     */
    public void setRunThread(boolean runThread) {
        this.runThread = runThread;
    }

    /**
     * @return the pauseTransfer
     */
    public boolean isPauseTransfer() {
        return pauseTransfer;
    }

    /**
     * @param pauseTransfer the pauseTransfer to set
     */
    public void setPauseTransfer(boolean pauseTransfer) {
        this.pauseTransfer = pauseTransfer;
    }

    /**
     * @return the maxNumber
     */
    public int getMaxNumber() {
        return maxNumber;
    }

    /**
     * @param maxNumber the maxNumber to set
     */
    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    /**
     * @return the dataProgres
     */
    public DataProgress getDataProgres() {
        return dataProgres;
    }

    /**
     * @param dataProgres the dataProgres to set
     */
    public void setDataProgres(DataProgress dataProgres) {
        this.dataProgres = dataProgres;
    }

    /**
     * @return the statusTxt
     */
    public String getStatusTxt() {
        return statusTxt;
    }

    /**
     * @param statusTxt the statusTxt to set
     */
    public void setStatusTxt(String statusTxt) {
        this.statusTxt = statusTxt;
    }

    /**
     * @return the transfer
     */
    public TransferToServer getTransfer() {
        return transfer;
    }

    /**
     * @param transfer the transfer to set
     */
    public void setTransfer(TransferToServer transfer) {
        this.transfer = transfer;
    }

   public void addStatusText(String statusText) {
        if(statusText.equals("")){
            this.addStatusText="";
        }else{
            this.addStatusText = this.addStatusText + statusText + "<br>";
        }
    
    }
    
    public String getAddStatusText() {
        return addStatusText;
    }
        

}
