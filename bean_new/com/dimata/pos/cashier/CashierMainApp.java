/*
 * CashierMainApp.java
 *
 * Created on November 13, 2004, 1:50 PM
 */

package com.dimata.pos.cashier;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.pos.entity.balance.CashCashier;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.entity.masterCashier.CashMaster;
import com.dimata.pos.entity.masterCashier.CashUser;
import com.dimata.pos.entity.masterCashier.PstCashMaster;
import com.dimata.pos.printman.DSJ_PrinterService;
import com.dimata.pos.xmlparser.DSJ_CashierConfig;
import com.dimata.pos.xmlparser.DSJ_CashierXML;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.posbo.entity.masterdata.Shift;


import com.dimata.qdep.form.FRMHandler;
import com.dimata.util.Formater;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author  wpradnyana
 */
public class CashierMainApp {
    
    
    private static int integrationWith =0;
    private static boolean enableOpenBill=false;
    private static boolean enablePendingOrder=false;
    private static boolean enableGiftTrans=false;
    private static boolean enableCreditPayment=false;
    private static boolean enableSaleEntry=false;
    private static boolean enableMembership=false;
    private static boolean enableService=false;
    private static boolean enableTax=false;
    private static boolean enableCardCost=false;
    private static boolean enableOtherCost=false;
    private static boolean enableContactInput=false;
    private static boolean enablePriceMapSelect=false;
    private static boolean autoNewSale=false;
    private static boolean enablePriceCurrSelect=false;
    private static boolean enableRateUpdate=false;
    private static boolean usingBigInvoice=false;
    private static boolean enableProductImage=false;
    private static boolean usingProductColor=false;
    private static boolean enablePriceEdit=false;
    private static boolean enableLocationSelect=false;
    private static boolean enableHalfInvoice=false;
    
    public static int ENABLED=1;
    public static int DISABLED=0;
    public static final int INTEGRATE_WITH_POS=0;
    public static final int INTEGRATE_WITH_HANOMAN=1;
    public static final int INTEGRATE_WITH_RESTAURANT=2;
    public static final int INTEGRATE_WITH_MANUFACTURE=3;
    private static int transactionPublished = 0 ;
    public static final int PUBLISH_LOCAL=0;
    public static final int PUBLISH_MULTICAST=1;
    
    
    private static DSJ_CashierConfig cashierConfig;
    
    private static DSJ_CashierXML cashierXML;
    private long currencyTypeId;
    private static CashMaster cashMaster;
    private static CashCashier cashCashier;
    private static CashUser cashUser;
    private static Shift shift;
    private static Hashtable hashCurrencyType;
    private static Hashtable hashCurrencyId;
    private static Hashtable currencyCodeMap=null;
    
    private static Hashtable hashPaymentTypeCardCost = new Hashtable();
    
    private static Vector vectFloatingUnit = new Vector(); 
    
    private static Vector vectFixUnit = new Vector(); 
    
    static DSJ_PrinterService prnSvc = DSJ_PrinterService.getInstance();
    /** added by wpulantara for currency formating */
    private static FRMHandler frameHandler = new FRMHandler();
    
    public static final int DIGIT_SEPARATOR = 0;
    public static final int DECIMAL_SEPARATOR = 1;
    
    /** stSeparator[lang][separator type] */
    public static final String[][] stSeparator = {
        {".",","},
        {",","."}
    };
    
    public static double getDoubleFromFormated(String sValue){
        double value = 0;
        try{
            value = Double.parseDouble(CashierMainApp.getFrameHandler().deFormatStringDecimal(sValue));
        }catch(Exception e){
            
        }
        return value;
    }
    /** methode use for validate number input from textfield
     *  @param stInput String stInput
     *  @return boolean
     */
    public static boolean validateInput(String stInput){
        try{
            double temp = Double.parseDouble(CashierMainApp.getFrameHandler().deFormatStringDecimal(stInput));
            //Double.parseDouble(stInput);
            return true;
        }
        catch(NumberFormatException nfe){
            return false;
        }
    }
    public long getCurrencyTypeId(){
        return currencyTypeId;
    }
    public void setCurrencyTypeId(long currencyTypeId){
        this.currencyTypeId = currencyTypeId;
    }
    public static DSJ_CashierConfig getDSJ_CashierConfig(){
        
        cashierConfig = CashierMainApp.getDSJ_CashierXML().getConfig(0);
       
        return cashierConfig;
    }
    
    public static void exitTrigger(){
        
        prnSvc.running = false;
        if(prnSvc.readyToStop()) {
            System.exit(0);
        }else{
            JOptionPane jOption = new JOptionPane("Printer in use",JOptionPane.INFORMATION_MESSAGE);
            jOption.showMessageDialog(jOption,"Printer in use","Printing Status",JOptionPane.INFORMATION_MESSAGE);
            //jOption.showMessageDialog();
        }
        
    }
    public static DSJ_CashierXML getDSJ_CashierXML(){
        
        if(cashierXML ==null){
            cashierXML = new DSJ_CashierXML();
            cashierXML.getData();
        }
        
        return cashierXML;
    }
    public CashierMainApp() {
        
        
        try {
            String className="com.dimata.material.DsjCashier";
            
            //I_DsjCashierMain sess=(I_DsjCashierMain)Class.forName(className).newInstance();
            //objShift = sess.getObjShift();
        }catch(Exception e){
            
        }
        
    }
    
    public static void runCashier() {
       
        activateMainFrame();
        
        activateLoginDialog();
        
    }
    private Vector userSession ;
    private static CashierLoginDialog loginDialog;
    private static CashierMainFrame mainFrame;
    private static CashBalanceDialog balanceDialog;
    private static CashClosingBalanceDialog closingBalanceDialog;
    
    public static void activateClosingBalanceDialog(){
        
        closingBalanceDialog=null;
        closingBalanceDialog= new CashClosingBalanceDialog(mainFrame,true);
        double x = closingBalanceDialog.getToolkit().getScreenSize().getWidth()/2;
        double y = closingBalanceDialog.getToolkit().getScreenSize().getHeight()/2;
        double xlength = (double)closingBalanceDialog.getWidth()/2;
        double ylength = (double)closingBalanceDialog.getHeight()/2;
        double locx = x - xlength;
        double locy = y - ylength;
        closingBalanceDialog.setLocation((int)locx,(int)locy);
        
        closingBalanceDialog.setCashCashierId(CashierMainApp.getCashCashier().getOID());
        closingBalanceDialog.setDisplayRequest(CashClosingBalanceDialog.DISPLAY_CLOSING);
        closingBalanceDialog.show();
        
    }
    
    
    public static void activateBalanceDialog(){
        
        balanceDialog=null;
        //if(balanceDialog==null) {
        balanceDialog= new CashBalanceDialog(mainFrame,true);
        double x = balanceDialog.getToolkit().getScreenSize().getWidth()/2;
        double y = balanceDialog.getToolkit().getScreenSize().getHeight()/2;
        double xlength = (double)balanceDialog.getWidth()/2;
        double ylength = (double)balanceDialog.getHeight()/2;
        double locx = x - xlength;
        double locy = y - ylength;
        balanceDialog.setLocation((int)locx,(int)locy);
        
        
        //}
        balanceDialog.show();
        
        
    }
    public static void activateLoginDialog() {
        
        if(loginDialog==null) {
            loginDialog= new CashierLoginDialog(mainFrame,true);
            double x = loginDialog.getToolkit().getScreenSize().getWidth()/2;
            double y = loginDialog.getToolkit().getScreenSize().getHeight()/2;
            double xlength = (double)loginDialog.getWidth()/2;
            double ylength = (double)loginDialog.getHeight()/2;
            double locx = x - xlength;
            double locy = y - ylength;
            loginDialog.setLocation((int)locx,(int)locy);
            
        }
        
        loginDialog.initAllFields();
        loginDialog.show();
        
    }
    
    
    public static void activateMainFrame() {
        
        if(mainFrame==null) {
            mainFrame = new CashierMainFrame();
            mainFrame.setSize(mainFrame.getToolkit().getScreenSize());
        }
        mainFrame.initAllFields();
        mainFrame.show();
        
       
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            
            UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        CashierMainApp app = new CashierMainApp();
       
        app.getFrameHandler().setDigitSeparator(app.stSeparator[app.getDSJ_CashierXML().getConfig(0).language][DIGIT_SEPARATOR]);
        
        app.getFrameHandler().setDecimalSeparator(app.stSeparator[app.getDSJ_CashierXML().getConfig(0).language][DECIMAL_SEPARATOR]);
        
        app.setIntegrationWith(app.getDSJ_CashierXML().getConfig(0).integrationType);
        
        app.setEnableOpenBill(app.getDSJ_CashierXML().getConfig(0).enableOpenBill==ENABLED);
        app.setEnableGiftTrans(app.getDSJ_CashierXML().getConfig(0).enableGiftTrans==ENABLED);
        app.setEnablePendingOrder(app.getDSJ_CashierXML().getConfig(0).enablePendingOrder==ENABLED);
        app.setEnableCreditPayment(app.getDSJ_CashierXML().getConfig(0).enableCreditPayment==ENABLED);
        app.setEnableService(app.getDSJ_CashierXML().getConfig(0).enableService==ENABLED);
        app.setEnableTax(app.getDSJ_CashierXML().getConfig(0).enableTax==ENABLED);
        app.setEnableSaleEntry(app.getDSJ_CashierXML().getConfig(0).salesentry==ENABLED);
        app.setEnableMembership(app.getDSJ_CashierXML().getConfig(0).enableMembership==ENABLED);
        app.setEnableCardCost(app.getDSJ_CashierXML().getConfig(0).enableCardCost==ENABLED);
        app.setEnableOtherCost(app.getDSJ_CashierXML().getConfig(0).enableOtherCost==ENABLED);
        app.setEnableContactInput(app.getDSJ_CashierXML().getConfig(0).enableContactInput==ENABLED);
        app.setEnablePriceMapSelect(app.getDSJ_CashierXML().getConfig(0).enablePriceMapSelect==ENABLED);
        app.setAutoNewSale(app.getDSJ_CashierXML().getConfig(0).autoNewSale==ENABLED);
        app.setEnablePriceCurrSelect(app.getDSJ_CashierXML().getConfig(0).enablePriceCurrSelect==ENABLED);
        app.setEnableRateUpdate(app.getDSJ_CashierXML().getConfig(0).enableRateUpdate==ENABLED);
        app.setUsingBigInvoice(app.getDSJ_CashierXML().getConfig(0).usingBigInvoice==ENABLED);
        app.setEnableProductImage(app.getDSJ_CashierXML().getConfig(0).enableProductImage==ENABLED);
        app.setUsingProductColor(app.getDSJ_CashierXML().getConfig(0).usingProductColor==ENABLED);
        app.setEnablePriceEdit(app.getDSJ_CashierXML().getConfig(0).enablePriceEdit==ENABLED);
        app.setEnableLocationSelect(app.getDSJ_CashierXML().getConfig(0).enableLocationSelect==ENABLED);
        app.setEnableHalfInvoice(app.getDSJ_CashierXML().getConfig(0).enableHalfInvoice==ENABLED);
        
        StringTokenizer st = new StringTokenizer(app.getDSJ_CashierXML().getConfig(0).paymentTypeCardCost,",");
        while(st.hasMoreTokens()){
            hashPaymentTypeCardCost.put(st.nextToken(), "");
        }
        
        st = new StringTokenizer(app.getDSJ_CashierXML().getConfig(0).floatingUnit,",");
        while(st.hasMoreTokens()){
            vectFloatingUnit.add(st.nextToken());
        }
        
        st = new StringTokenizer(app.getDSJ_CashierXML().getConfig(0).fixUnit,",");
        while(st.hasMoreTokens()){
            vectFixUnit.add(st.nextToken());
        }
        
        app.runCashier();
        
    }
    private String currentUserName ;
    private String currentUserId;
    
    public static long getUserLogin(String userName,String password) {
        
        long userId = 0;
        
        return userId;
    }
    
    
    /**
     * @return Returns the mainFrame.
     */
    public static CashierMainFrame getMainFrame() {
        
        if(mainFrame==null) {
            mainFrame = new CashierMainFrame();
        }
        
        return mainFrame;
    }
    /**
     * @param mainFrame The mainFrame to set.
     */
    public void setMainFrame(CashierMainFrame mainFrameI) {
        
        mainFrame = mainFrameI;
        
    }
    /**
     * @return Returns the currentUserId.
     */
    public String getCurrentUserId() {
        
        return currentUserId;
    }
    /**
     * @param currentUserId The currentUserId to set.
     */
    public void setCurrentUserId(String currentUserId) {
        
        this.currentUserId = currentUserId;
        
    }
    /**
     * @return Returns the currentUserName.
     */
    public String getCurrentUserName() {
        
        return currentUserName;
    }
    /**
     * @param currentUserName The currentUserName to set.
     */
    public void setCurrentUserName(String currentUserName) {
        
        this.currentUserName = currentUserName;
        
    }
    /**
     * @return Returns the userSession.
     */
    public Vector getUserSession() {
        
        return userSession;
    }
    /**
     * @param userSession The userSession to set.
     */
    public void setUserSession(Vector userSession) {
        
        this.userSession = userSession;
        
    }
    
    private static boolean successLogin = false;
    
    public static boolean supervisorLoginTrigger(){
        
        if(SessionManager.getSupervisorLogin()!=null) {
            loginDialog.hide();
            boolean returnboolean = successLogin = true;
            return returnboolean;
        }
        else {
            boolean returnboolean = successLogin = false;
            return returnboolean;
        }
    }
    
    public static void logoutTrigger(){
        
        if(!isAnyCashier()){
            SessionManager.getLogOutUser();
            setCashCashier(null);
            setCashUser(null);
            runCashier();
            //return true;
        }else{
            //activateBalanceDialog ();
            activateClosingBalanceDialog();
            //return false;
        }
        
    }
    public static boolean loginTrigger() {
        
        getCashMaster();
        
        CashierMainApp.setCashUser(SessionManager.getLoginUser());
        if(CashierMainApp.cashUser !=null) {
            loginDialog.hide();
            //getCashUser ();
            //cashUser.setUserName (userName
            //aktifkan di bawah ini setelah form balance tampil
            CashierMainApp.getCashCashier();
            
            Date start = new Date();
            System.out.println("start trigger "+start.getTime());
            
            /* this use to print */
            Thread thr = new Thread(prnSvc);
            prnSvc.running = true;
            thr.setDaemon(false);
            thr.start();
            
            Date end = new Date();
            System.out.println("end trigger "+end.getTime());
            
            boolean returnboolean = successLogin = true;
            
            return returnboolean;
        }
        else {
            boolean returnboolean = successLogin = false;
            
            return returnboolean;
        }
        
    }
    public static void systemExit() {
        
        prnSvc.running = false;
        if(prnSvc.readyToStop()) {
            System.exit(0);
        }else{
            JOptionPane jOption = new JOptionPane("Printer in use",JOptionPane.INFORMATION_MESSAGE);
            jOption.showMessageDialog(jOption,"Printer in use","Printing Status",JOptionPane.INFORMATION_MESSAGE);
            //jOption.showMessageDialog();
        }
        
    }
    
    
    
    /**
     * Getter for property cashMaster.
     * @return Value of property cashMaster.
     */
    public static CashMaster getCashMaster() {
        
        if(cashMaster==null){
            try{
                cashMaster = new CashMaster();
                    /*cashMaster.setCashierNumber (Integer.parseInt (CashierMainApp.getDSJ_CashierXML ().getConfig (0).cashierNumber));
                    cashMaster.setCashService (Double.parseDouble (CashierMainApp.getDSJ_CashierXML ().getConfig (0).cashierService));
                    cashMaster.setCashTax (Double.parseDouble (CashierMainApp.getDSJ_CashierXML ().getConfig (0).cashierTax));
                    cashMaster.setPriceType (CashierMainApp.getDSJ_CashierXML ().getConfig (0).pricetype);
                    cashMaster.setLocationId (Long.parseLong (CashierMainApp.getDSJ_CashierXML ().getConfig (0).locationId));
                    long OID = PstCashMaster.insertExc (cashMaster);
                    cashMaster.setOID (OID);*/
                
                cashMaster = PstCashMaster.fetchExc(Long.parseLong(CashierMainApp.getDSJ_CashierXML().getConfig(0).master));
                
            }catch(DBException dbe){
                System.out.println("err on getCashMaster: "+dbe.toString());
            }
        }
        
        return cashMaster;
    }
    
    /**
     * Setter for property cashMaster.
     * @param acashMaster New value of property cashMaster.
     */
    public static void setCashMaster(CashMaster acashMaster) {
        cashMaster = acashMaster;
    }
    
    /**
     * Getter for property cashCashier.
     * @return Value of property cashCashier.
     */
    public static CashCashier getCashCashier() {
        
        if(cashCashier==null){
            CashCashier temp = CashierMainApp.findLastCashierOpened();
            if(temp==null){
                activateBalanceDialog();
            }
            else{
                JOptionPane.showMessageDialog(mainFrame,"Using unclosed session by "+temp.getSpvName()+"\n at "+Formater.formatDate(temp.getCashDate(),"yyyy MMM dd"));
                CashierMainApp.setCashCashier(temp);
                cashCashier = temp;
            }
        }
        
        
        return cashCashier;
    }
    
    /**
     * Setter for property cashCashier.
     * @param acashCashier New value of property cashCashier.
     */
    public static void setCashCashier(CashCashier acashCashier) {
        
        cashCashier = acashCashier;
        activateMainFrame();
        
    }
    
    /**
     * Getter for property cashUser.
     * @return Value of property cashUser.
     */
    public static CashUser getCashUser() {
        
        if(cashUser==null){
            activateLoginDialog();
        }
        
        return cashUser;
    }
    
    /**
     * Setter for property cashUser.
     * @param acashUser New value of property cashUser.
     */
    public static void setCashUser(CashUser acashUser) {
        cashUser = acashUser;
    }
    
    /**
     * Getter for property shift.
     * @return Value of property shift.
     */
    public static Shift getShift() {
        
        if(shift==null){
            shift = new Shift();
        }
        
        return shift;
    }
    
    private static String currencyCodeUsed=null;
    /**
     * Setter for property shift.
     * @param ashift New value of property shift.
     */
    public static void setShift(Shift ashift) {
        shift = ashift;
    }
    
    /**
     * Getter for property hashCurrencyType.
     * @return Value of property hashCurrencyType.
     */
    public static Hashtable getHashCurrencyType() {
        
        if(hashCurrencyType==null){
            hashCurrencyType = new Hashtable();
            String whereClause = PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+" = "+
            PstCurrencyType.INCLUDE;
            String orderClause = PstCurrencyType.fieldNames[PstCurrencyType.FLD_TAB_INDEX]+" ASC";
            Vector vctCurrencyType = PstCurrencyType.list(0,0,whereClause,orderClause);
            int size = vctCurrencyType.size();
            for(int i=0;i<size;i++){
                CurrencyType temp = (CurrencyType)vctCurrencyType.get(i);
                //hashCurrencyType.put (new Long(temp.getOID()), temp);
                hashCurrencyType.put(temp.getCode(), temp);
            }
        }
        return hashCurrencyType;
    }
    
    /**
     * Setter for property hashCurrencyType.
     * @param ahashCurrencyType New value of property hashCurrencyType.
     */
    public static void setHashCurrencyType(Hashtable ahashCurrencyType) {
        hashCurrencyType = ahashCurrencyType;
    }
    
    /**
     * Getter for property hashCurrencyType.
     * @return Value of property hashCurrencyType.
     */
    public static Hashtable getHashCurrencyId() {
        
        if(hashCurrencyId==null){
            hashCurrencyId = new Hashtable();
            String whereClause = PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+" = "+
            PstCurrencyType.INCLUDE;
            String orderClause = PstCurrencyType.fieldNames[PstCurrencyType.FLD_TAB_INDEX]+" ASC";
            Vector vctCurrencyType = PstCurrencyType.list(0,0,whereClause,orderClause);
            int size = vctCurrencyType.size();
            for(int i=0;i<size;i++){
                CurrencyType temp = (CurrencyType)vctCurrencyType.get(i);
                hashCurrencyId.put(new Long(temp.getOID()), temp);
            }
        }
        
        
        return hashCurrencyId;
    }
    
    /**
     * Setter for property hashCurrencyType.
     * @param ahashCurrencyId New value of property hashCurrencyType.
     */
    public static void setHashCurrencyId(Hashtable ahashCurrencyId) {
        hashCurrencyId = ahashCurrencyId;
    }
    
    public static boolean isAnyCashier(){
        if(CashierMainApp.cashCashier !=null){
            return true;
        }
        else {
            return false;
        }
    }
    
    public static boolean isAnyUser(){
        
        if(CashierMainApp.cashUser!=null)
            return true;
        else
            return false;
    }
    
    public static CashCashier findLastCashierOpened(){
        
        Date start = new Date();
        System.out.println("start find "+start.getTime());
        CashUser lCashUser = CashierMainApp.getCashUser();
        long cashUserId = lCashUser.getUserId();
        CashCashier lCashCashier = null;
        String whereClause = "";
        whereClause = PstCashCashier.fieldNames[PstCashCashier.FLD_APPUSER_ID]+"="+cashUserId+" " +
        "AND "+PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID]+"=1 ";
        String orderClause = " ";
        orderClause = PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE]+" DESC";
        Vector result = PstCashCashier.list(0,0, whereClause, orderClause);
        //System.out.println(whereClause+orderClause);
        if(result.size()>0){
            lCashCashier = (CashCashier ) result.get(0);
        }
        //CashierMainApp.checkCashier (cashUserId);
        Date end = new Date();
        System.out.println("end find "+end.getTime());
        
        return lCashCashier;
    }
    
    /**
     * Getter for property currencyCodeUsed.
     * @return Value of property currencyCodeUsed.
     */
    public static java.lang.String getCurrencyCodeUsed(int iCode) {
        
        String sCur = "";
        try{
            sCur = (String)CashierMainApp.getCurrencyCodeMap().get(new Integer(iCode+1));
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
        return sCur;
    }
    
    
    /**
     * Getter for property currencyCodeUsed.
     * @return Value of property currencyCodeUsed.
     */
    public static java.lang.String getCurrencyCodeUsed() {
        
        if(currencyCodeUsed==null){
            // gunakan ini jika configurasi menggunakan Id Currency
            //String sCur = CashierMainApp.getDSJ_CashierXML().getConfig(0).currencyId;
            //CurrencyType curType = (CurrencyType)CashierMainApp.getHashCurrencyId().get(new Long(CashierMainApp.getDSJ_CashierXML().getConfig(0).currencyId));
            //currencyCodeUsed = curType.getCode();
            
            // gunakan ini jika configurasi menggunakan Kode Currency
            String sCur = CashierMainApp.getDSJ_CashierXML().getConfig(0).defaultCurrencyCode;
            currencyCodeUsed = sCur;
            
        }
        
        return currencyCodeUsed;
    }
    
    /**
     * Setter for property currencyCodeUsed.
     * @param argCurrencyCodeUsed New value of property currencyCodeUsed.
     */
    public static void setCurrencyCodeUsed(java.lang.String argCurrencyCodeUsed) {
        currencyCodeUsed = argCurrencyCodeUsed;
    }
    
    public static Vector checkCashier(long userId){
        
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try{
            
            String sql="SELECT "+PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+","
            +PstCashCashier.fieldNames[PstCashCashier.FLD_APPUSER_ID]+
            " FROM "+PstCashCashier.TBL_CASH_CASHIER+
            " WHERE (("+PstCashCashier.fieldNames[PstCashCashier.FLD_APPUSER_ID]+"="+userId+") AND "+
            "("+PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID]+"=0))";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            lists = new Vector();
            while(rs.next()) {
                Vector cashier=new Vector();
                cashier.add(""+rs.getString(1));
                cashier.add(""+rs.getString(2));
                lists.add(cashier);
            }
            rs.close();
            return lists;
        }catch(Exception e) {
            System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
        Vector returnVector = new Vector();
        
        return returnVector;
    }
    
    /** check if inputed payment type exist in hashPaymentCardCost */
    public static boolean isNeedCardCost(int payType){
        return hashPaymentTypeCardCost.containsKey(""+payType); 
    }
    
    /**
     * Getter for property frameHandler.
     * @return Value of property frameHandler.
     */
    public static FRMHandler getFrameHandler() {
        return frameHandler;
    }
    
    /**
     * Setter for property frameHandler.
     * @param frame New value of property frameHandler.
     */
    public static void setFrameHandler(FRMHandler frame) {
        frameHandler = frame ;
    }
    
    /* this check format value in printing */
    public static String setFormatNumber(double val){
        if(!getCurrencyCodeUsed().equals("Rp")){
            return getFrameHandler().userFormatStringDecimal(val);
        }else {
            String result = getFrameHandler().userFormatStringDecimal(val);
            if(result.length()>3){
                result = result.substring(0,result.length()-3);
            }
            return result;
        }
    }
    
     /* this check format value in printing */
    public static String setFormatNumber(double val, double rate){
        if(rate!=1){
            return getFrameHandler().userFormatStringDecimal(val);
        }else {
            String result = getFrameHandler().userFormatStringDecimal(val);
            if(result.length()>3){
                result = result.substring(0,result.length()-3);
            }
            return result;
        }
    }
    
    /**
     * Getter for property integrationWith.
     * @return Value of property integrationWith.
     */
    public static int getIntegrationWith() {
        integrationWith = CashierMainApp.getDSJ_CashierXML().getConfig(0).integrationType;
        return integrationWith;
    }
    
    /**
     * Setter for property integrationWith.
     * @param argIntegrationWith New value of property integrationWith.
     */
    public static void setIntegrationWith(int argIntegrationWith) {
        integrationWith = argIntegrationWith;
    }
    
    /**
     * Getter for property transactionPublished.
     * @return Value of property transactionPublished.
     */
    public static int getTransactionPublished() {
        transactionPublished = CashierMainApp.getDSJ_CashierXML().getConfig(0).dataPublishMethod;
        return transactionPublished;
    }
    
    /**
     * Setter for property transactionPublished.
     * @param argTransactionPublished New value of property transactionPublished.
     */
    public static void setTransactionPublished(int argTransactionPublished) {
        transactionPublished = argTransactionPublished;
    }
    
    /**
     * Getter for property currencyCodeMap.
     * @return Value of property currencyCodeMap.
     */
    public static Hashtable getCurrencyCodeMap() {
        if(currencyCodeMap==null){
            currencyCodeMap = new Hashtable();
            currencyCodeMap = CashierMainApp.getDSJ_CashierXML().getConfig(0).currencyCodeMap;
        }
        return currencyCodeMap;
    }
    
    /**
     * Setter for property currencyCodeMap.
     * @param argCurrencyCodeMap New value of property currencyCodeMap.
     */
    public static void setCurrencyCodeMap(Hashtable argCurrencyCodeMap) {
        currencyCodeMap = argCurrencyCodeMap;
    }
    
    /**
     * Getter for property enableOpenBill.
     * @return Value of property enableOpenBill.
     */
    public static boolean isEnableOpenBill() {
        return enableOpenBill;
    }
    
    /**
     * Setter for property enableOpenBill.
     * @param enableOpenBill New value of property enableOpenBill.
     */
    public static void setEnableOpenBill(boolean enable) {
        enableOpenBill = enable;
    }
    
    /**
     * Getter for property enablePendingOrder.
     * @return Value of property enablePendingOrder.
     */
    public static boolean isEnablePendingOrder() {
        return enablePendingOrder;
    }
    
    /**
     * Setter for property enablePendingOrder.
     */
    public static void setEnablePendingOrder(boolean enable) {
        enablePendingOrder = enable;
    }
    
    /**
     * Getter for property enableGiftTrans.
     * @return Value of property enableGiftTrans.
     */
    public static boolean isEnableGiftTrans() {
        return enableGiftTrans;
    }
    
    /**
     * Setter for property enableGiftTrans.
     * @param enableGiftTrans New value of property enableGiftTrans.
     */
    public static void setEnableGiftTrans(boolean enable) {
        enableGiftTrans = enable;
    }
    
    /**
     * Getter for property enableCreditPayment.
     * @return Value of property enableCreditPayment.
     */
    public static boolean isEnableCreditPayment() {
        return enableCreditPayment;
    }
    
    /**
     * Setter for property enableCreditPayment.
     * @param enableCreditPayment New value of property enableCreditPayment.
     */
    public static void setEnableCreditPayment(boolean enable) {
        enableCreditPayment = enable;
    }
    
    /**
     * Getter for property enableSaleEntry.
     * @return Value of property enableSaleEntry.
     */
    public static boolean isEnableSaleEntry() {
        return enableSaleEntry;
    }
    
    /**
     * Setter for property enableSaleEntry.
     * @param enableSaleEntry New value of property enableSaleEntry.
     */
    public void setEnableSaleEntry(boolean enable) {
        enableSaleEntry = enable;
    }
    
    /**
     * Getter for property enableMembership.
     * @return Value of property enableMembership.
     */
    public static boolean isEnableMembership() {
        return enableMembership;
    }
    
    /**
     * Setter for property enableMembership.
     * @param enableMembership New value of property enableMembership.
     */
    public static void setEnableMembership(boolean enable) {
        enableMembership = enable;
    }
    
    /**
     * Getter for property enableService.
     * @return Value of property enableService.
     */
    public static boolean isEnableService() {
        return enableService;
    }
    
    /**
     * Setter for property enableService.
     * @param enableService New value of property enableService.
     */
    public static void setEnableService(boolean enable) {
        enableService = enable;
    }
    
    /**
     * Getter for property enableTax.
     * @return Value of property enableTax.
     */
    public static boolean isEnableTax() {
        return enableTax;
    }
    
    /**
     * Setter for property enableTax.
     * @param enableTax New value of property enableTax.
     */
    public static void setEnableTax(boolean enable) {
        enableTax = enable;
    }
    
    /**
     * Getter for property enableCardCost.
     * @return Value of property enableCardCost.
     */
    public static boolean isEnableCardCost() {
        return enableCardCost;
    }
    
    /**
     * Setter for property enableCardCost.
     * @param enableCardCost New value of property enableCardCost.
     */
    public static void setEnableCardCost(boolean enable) {
        enableCardCost = enable;
    }
    
    /**
     * Getter for property enableOtherCost.
     * @return Value of property enableOtherCost.
     */
    public static boolean isEnableOtherCost() {
        return enableOtherCost;
    }
    
    /**
     * Setter for property enableOtherCost.
     * @param enableOtherCost New value of property enableOtherCost.
     */
    public static void setEnableOtherCost(boolean enable) {
        enableOtherCost = enable;
    }
    
    /**
     * Getter for property enableContactInput.
     * @return Value of property enableContactInput.
     */
    public static boolean isEnableContactInput() {
        return enableContactInput;
    }
    
    /**
     * Setter for property enableContactInput.
     * @param enableContactInput New value of property enableContactInput.
     */
    public static void setEnableContactInput(boolean enable) {
        enableContactInput = enable;
    }
    
    /**
     * Getter for property enablePriceMapSelect.
     * @return Value of property enablePriceMapSelect.
     */
    public static boolean isEnablePriceMapSelect() {
        return enablePriceMapSelect;
    }
    
    /**
     * Setter for property enablePriceMapSelect.
     * @param enablePriceMapSelect New value of property enablePriceMapSelect.
     */
    public static void setEnablePriceMapSelect(boolean enable) {
        enablePriceMapSelect = enable;
    }
           
    /**
     * Getter for property hashPaymentTypeCardCost.
     * @return Value of property hashPaymentTypeCardCost.
     */
    public static Hashtable getHashPaymentTypeCardCost() {
        return hashPaymentTypeCardCost;
    }
    
    /**
     * Setter for property hashPaymentTypeCardCost.
     * @param hashPaymentTypeCardCost New value of property hashPaymentTypeCardCost.
     */
    public static void setHashPaymentTypeCardCost(Hashtable hash) {
        hashPaymentTypeCardCost = hash;
    }
    
    /**
     * Getter for property autoNewSale.
     * @return Value of property autoNewSale.
     */
    public static boolean isAutoNewSale() {
        return autoNewSale;
    }
    
    /**
     * Setter for property autoNewSale.
     * @param autoNewSale New value of property autoNewSale.
     */
    public static void setAutoNewSale(boolean auto) {
        autoNewSale = auto;
    }
    
    /**
     * Getter for property enablePriceCurrSelect.
     * @return Value of property enablePriceCurrSelect.
     */
    public static boolean isEnablePriceCurrSelect() {
        return enablePriceCurrSelect;
    }
    
    /**
     * Setter for property enablePriceCurrSelect.
     * @param enablePriceCurrSelect New value of property enablePriceCurrSelect.
     */
    public static void setEnablePriceCurrSelect(boolean enablePriceCurr) {
        enablePriceCurrSelect = enablePriceCurr;
    }
    
    /**
     * Getter for property enableRateUpdate.
     * @return Value of property enableRateUpdate.
     */
    public static boolean isEnableRateUpdate() {
        return enableRateUpdate;
    }
    
    /**
     * Setter for property enableRateUpdate.
     * @param enableRateUpdate New value of property enableRateUpdate.
     */
    public static void setEnableRateUpdate(boolean enableRate) {
        enableRateUpdate = enableRate;
    }
    
    /**
     * Getter for property usingBigInvoice.
     * @return Value of property usingBigInvoice.
     */
    public static boolean isUsingBigInvoice() {
        return usingBigInvoice;
    }
    
    /**
     * Setter for property usingBigInvoice.
     * @param usingBigInvoice New value of property usingBigInvoice.
     */
    public static void setUsingBigInvoice(boolean using) {
        usingBigInvoice = using;
    }
    
    /**
     * Getter for property enableProductImage.
     * @return Value of property enableProductImage.
     */
    public static boolean isEnableProductImage() {
        return enableProductImage;
    }
    
    /**
     * Setter for property enableProductImage.
     * @param enableProductImage New value of property enableProductImage.
     */
    public static void setEnableProductImage(boolean enable) {
        enableProductImage = enable;
    }
    
    /**
     * Getter for property usingProductColor.
     * @return Value of property usingProductColor.
     */
    public static boolean isUsingProductColor() {
        return usingProductColor;
    }
    
    /**
     * Setter for property usingProductColor.
     * @param usingProductColor New value of property usingProductColor.
     */
    public static void setUsingProductColor(boolean using) {
        usingProductColor = using;
    }
    
    /**
     * Getter for property enablePriceEdit.
     * @return Value of property enablePriceEdit.
     */
    public static boolean isEnablePriceEdit() {
        return enablePriceEdit;
    }
    
    /**
     * Setter for property enablePriceEdit.
     * @param enablePriceEdit New value of property enablePriceEdit.
     */
    public static void setEnablePriceEdit(boolean enable) {
        enablePriceEdit = enable;
    }
    
    /**
     * Getter for property vectFloatingUnit.
     * @return Value of property vectFloatingUnit.
     */
    public static java.util.Vector getVectFloatingUnit() {
        return vectFloatingUnit;
    }
    
    /**
     * Setter for property vectFloatingUnit.
     * @param vectFloatingUnit New value of property vectFloatingUnit.
     */
    public static void setVectFloatingUnit(java.util.Vector vect) {
        vectFloatingUnit = vect;
    }
    
    /**
     * Getter for property vectFixUnit.
     * @return Value of property vectFixUnit.
     */
    public static java.util.Vector getVectFixUnit() {
        return vectFixUnit;
    }
    
    /**
     * Setter for property vectFixUnit.
     * @param vectFixUnit New value of property vectFixUnit.
     */
    public static void setVectFixUnit(java.util.Vector vect) {
        vectFixUnit = vect;
    }
    
    /**
     * Getter for property enableLocationSelect.
     * @return Value of property enableLocationSelect.
     */
    public static boolean isEnableLocationSelect() {
        return enableLocationSelect;
    }
    
    /**
     * Setter for property enableLocationSelect.
     * @param enableLocationSelect New value of property enableLocationSelect.
     */
    public static void setEnableLocationSelect(boolean enable) {
        enableLocationSelect = enable;
    }
    
    /**
     * Getter for property enableHalfInvoice.
     * @return Value of property enableHalfInvoice.
     */
    public static boolean isEnableHalfInvoice() {
        return enableHalfInvoice;
    }
    
    /**
     * Setter for property enableHalfInvoice.
     * @param enableHalfInvoice New value of property enableHalfInvoice.
     */
    public static void setEnableHalfInvoice(boolean enable) {
        enableHalfInvoice = enable;
    }
}
