/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice;


import com.dimata.pos.entity.balance.CashCashier;
import com.dimata.pos.entity.balance.PstCashCashier;
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
public class sendDataCashier extends HttpServlet {

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
        String cashCashierId = FRMQueryString.requestString(request, "CASH_CASHIER_ID");
        String cashMasterId = FRMQueryString.requestString(request, "CASH_MASTER_ID");
        String appUserId = FRMQueryString.requestString(request, "APP_USER_ID");
        String openDate = FRMQueryString.requestString(request, "OPEN_DATE");
        String spvOpenId = FRMQueryString.requestString(request, "SPV_OPEN_ID");
        String spvOpenName = FRMQueryString.requestString(request, "SPV_OPEN_NAME");
        String spvCloseId = FRMQueryString.requestString(request, "SPV_CLOSE_ID");
        String spvCloseName = FRMQueryString.requestString(request, "SPV_CLOSE_NAME");
        String shiftId = FRMQueryString.requestString(request, "SHIFT_ID");
        String closeDate = FRMQueryString.requestString(request, "CLOSE_DATE");
        
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
        if(!cashMasterId.equals("") && !cashMasterId.equals("")){
             isDetailInvoice=true;
        }
        
        if(!isMainInvoice && !isDetailInvoice){
            isOk=false;
        }
        
        if(!cashCashierId.equals("") && !cashMasterId.equals("")){
//            
//            if(billDate.equals("")){
//                isDataComplate=false;
//                isOk=false;
//                message=message+" Bill Date kosong";
//            }
//            
//            if(appUserId.equals("")){
//                isDataComplate=false;
//                isOk=false;
//                message=message+" App User ID kosong";
//            }
//            
//            if(customerId.equals("")){
//                isDataComplate=false;
//                isOk=false;
//                message=message+" Customer ID kosong";
//            }
//            
//            if(docType.equals("")){
//                isDataComplate=false;
//                isOk=false;
//                message=message+" Doc type kosong";
//            }
//            
            
            if(isDataComplate & isOk){
                //proses insert data
                long lCashCashierId = 0;//
                long lCashMasterId = 0;//= FRMQueryString.requestString(request, "CASH_MASTER_ID");
                long lAppUserId = 0;//= FRMQueryString.requestString(request, "APP_USER_ID");
                Date lOpenDate = new Date();//= FRMQueryString.requestString(request, "OPEN_DATE");
                long lSpvOpenId = 0;//= FRMQueryString.requestString(request, "SPV_OPEN_ID");
                String lspvOpenName = "";//= FRMQueryString.requestString(request, "SPV_OPEN_NAME");
                long lSpvCloseId = 0;//= FRMQueryString.requestString(request, "SPV_CLOSE_ID");
                String lSpvCloseName = "";//= FRMQueryString.requestString(request, "SPV_CLOSE_NAME");
                long lShiftId = 0;//= FRMQueryString.requestString(request, "SHIFT_ID");
                Date lSloseDate = new Date();//= FRMQueryString.requestString(request, "CLOSE_DATE");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                spvCloseId="504404622951115262";
                spvOpenId="504404622951115262";
                try{
                    lCashCashierId = Long.parseLong(cashCashierId);
                    lCashMasterId =  Long.parseLong(cashMasterId);//= FRMQueryString.requestString(request, "CASH_MASTER_ID");
                    lAppUserId = Long.parseLong(appUserId);//= FRMQueryString.requestString(request, "APP_USER_ID");
                    lSpvOpenId = Long.parseLong(spvOpenId);//= FRMQueryString.requestString(request, "SPV_OPEN_ID");
                    lspvOpenName = spvOpenName;//= FRMQueryString.requestString(request, "SPV_OPEN_NAME");
                    lSpvCloseId = Long.parseLong(spvCloseId);//= FRMQueryString.requestString(request, "SPV_CLOSE_ID");
                    lSpvCloseName = spvCloseName;//= FRMQueryString.requestString(request, "SPV_CLOSE_NAME");
                    lShiftId = Long.parseLong(shiftId);//= FRMQueryString.requestString(request, "SHIFT_ID");
                    
                    lSloseDate = formatter.parse(closeDate);//= FRMQueryString.requestString(request, "CLOSE_DATE");
                    lOpenDate = formatter.parse(openDate);//= FRMQueryString.requestString(request, "OPEN_DATE");
                }catch(Exception ex){
                }
                
                if(isOk){
                    try {
                        //proses untuk Cash Cashier 
                        CashCashier cashCashier = new CashCashier();
                        cashCashier.setOID(lCashCashierId);
                        cashCashier.setCashMasterId(lCashMasterId);
                        cashCashier.setAppUserId(lAppUserId);
                        cashCashier.setOpenDate(lOpenDate);
                        cashCashier.setSpvOid(lSpvOpenId);
                        cashCashier.setSpvName(lspvOpenName);
                        cashCashier.setSpvCloseOid(lSpvCloseId);
                        cashCashier.setSpvCloseName(lSpvCloseName);
                        cashCashier.setShiftId(lShiftId);
                        cashCashier.setCloseDate(lSloseDate);
                        try {
                            long customeData = PstCashCashier.insertExc(cashCashier);
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
                jSONObjectArray.put("CASH_CASHIER_ID", ""+cashCashierId);
                jSONObjectArray.put("CASH_MASTER_ID", ""+cashMasterId);
                jSONObjectArray.put("MESSAGE", ""+message);
            }else{
                jSONObjectArray.put("STATUS", "false");
                jSONObjectArray.put("CASH_CASHIER_ID", ""+cashCashierId);
                jSONObjectArray.put("CASH_MASTER_ID", ""+cashMasterId);
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
