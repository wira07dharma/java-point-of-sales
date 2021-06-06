/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice;


import com.dimata.pos.entity.balance.Balance;
import com.dimata.pos.entity.balance.PstBalance;
import com.dimata.qdep.form.FRMQueryString;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author dimata005
 */
public class sendBalance extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   
    //OBJECT
    private JSONObject jSONObject = new JSONObject();
    private JSONArray jSONArray = new JSONArray();
    
    //STRING
    private String dataFor = "";
    private String oidDelete="";
    private String approot = "";
    private String address  = "";
    private String htmlReturn = "";
    private String searchTerm = "";
    private int amount = 0;
    private String colName = "";
    private String dir = "";
    private int start = 0;
    private int colOrder = 0;
    //LONG
    private long oidReturn = 0;

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;
    private int language = 0; 
    
    public static final String textListOrderHeader[][] = {
        {"No","Lokasi","Nama Item","Jml","Unit Stok","Jml Sekarang","Kode","Jumlah Request","Jumlah Kirim"},
        {"No","Location","Item Name","Qty","Stock Unit","Curent Qty","Code","Request Qty","Send Request"}
    };
    
    public static final String textListButton[][] = {
        {"Simpan","Hapus","Ubah","Selanjutnya","Kembali","Pencarian"},
        {"Save","Delete","Update","Next","Back","Search"}
    };
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.iCommand = FRMQueryString.requestCommand(request); 
        try {
            this.language = FRMQueryString.requestInt(request, "FRM_FIELD_LANGUAGE");
        } catch (Exception e) {
        }
        
        sentDataInvoice(request);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(this.jSONArray);
    }
    
    public void sentDataInvoice(HttpServletRequest request){
            prosesSent(request);
    }
    
    public void prosesSent(HttpServletRequest request){
        //WHERE CLAUSE
        String whereClause = "";
        
        //data dari bill main
        String cashBalanceId = FRMQueryString.requestString(request, "CASH_BALANCE_ID");
        String cashCashierId = FRMQueryString.requestString(request, "CASH_CASHIER_ID");
        String type = FRMQueryString.requestString(request, "TYPE");
        String balanceDate = FRMQueryString.requestString(request, "BALANCE_DATE");
        String balanceValue = FRMQueryString.requestString(request, "BALANCE_VALUE");
        String shouldValue = FRMQueryString.requestString(request, "SHOULD_VALUE");
        String currencyId = FRMQueryString.requestString(request, "CURRENCY_ID");
        
        
        boolean isMainInvoice=false;
        boolean isDetailInvoice=false;
        boolean isOk=true;
        boolean isDataComplate=true;
        String message="";
        boolean step1=false;
        boolean step2=false;
        //input cash_bill_main
        if(!cashCashierId.equals("") && cashCashierId.equals("")){
             isMainInvoice=true;
        }
        if(!cashBalanceId.equals("") && !cashBalanceId.equals("")){
             isDetailInvoice=true;
        }
        
        if(!isMainInvoice && !isDetailInvoice){
            isOk=false;
        }
        
        if(!cashCashierId.equals("") && !cashBalanceId.equals("")){
      
            if(isDataComplate & isOk){
                //proses insert data
                long lCashBalanceId = 0;
                long lCashCashierId = 0;
                int lType = 0;
                Date lBalanceDate = new Date();
                double lBalanceValue = 0.0;
                double lShouldValue = 0.0;
                long lCurrencyId = 0;
                
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                
                try{
                    lCashCashierId = Long.parseLong(cashCashierId);
                    lCashBalanceId =  Long.parseLong(cashBalanceId);//= FRMQueryString.requestString(request, "CASH_MASTER_ID");
                    lType = Integer.valueOf(type);//= FRMQueryString.requestString(request, "APP_USER_ID");
                    lBalanceDate = formatter.parse(balanceDate);
                    lBalanceValue = Double.parseDouble(balanceValue);//= FRMQueryString.requestString(request, "SPV_OPEN_NAME");
                    lShouldValue = Double.parseDouble(shouldValue);//= FRMQueryString.requestString(request, "SPV_CLOSE_ID");
                    lCurrencyId = Long.parseLong(currencyId);//= FRMQueryString.requestString(request, "SHIFT_ID");
                }catch(Exception ex){
                }
                
                if(isOk){
                    try {
                        //proses untuk Cash Cashier 
                        Balance balance = new Balance();
                        balance.setOID(lCashBalanceId);
                        balance.setCashCashierId(lCashCashierId);
                        balance.setPaymentType(lType);
                        balance.setBalanceValue(lBalanceValue);
                        balance.setBalanceDate(lBalanceDate);
                        balance.setShouldValue(lShouldValue);
                        balance.setCurrencyOid(lCurrencyId);
                        try {
                            long customeData = PstBalance.insertExc(balance);
                        } catch (Exception ex) {
                        }
                        
                    } catch (Exception ex) {
                       isOk=false;
                       message=message+"insert cash bill main gagal";
                    }
                }
                
                
            }else{
                 isOk=false;
                 message=message+"Data tidak lengkap";
            }
        }
        
        try {
            jSONArray = new JSONArray();
            JSONObject jSONObjectArray = new JSONObject();
            //buatkan proses penginputan data cash_bill_main dan cash_bill_detail
            if(isOk){
                jSONObjectArray.put("STATUS", "true");
                jSONObjectArray.put("BALANCE_ID", ""+cashBalanceId);
                jSONObjectArray.put("CASH_CASHIER_ID", ""+cashCashierId);
                jSONObjectArray.put("MESSAGE", ""+message);
            }else{
                jSONObjectArray.put("STATUS", "false");
                jSONObjectArray.put("BALANCE_ID", ""+cashBalanceId);
                jSONObjectArray.put("CASH_CASHIER_ID", ""+cashCashierId);
                jSONObjectArray.put("MESSAGE", ""+message);
            }
            jSONArray.put(jSONObjectArray);
        } catch (Exception e) {
        }
        
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
}
