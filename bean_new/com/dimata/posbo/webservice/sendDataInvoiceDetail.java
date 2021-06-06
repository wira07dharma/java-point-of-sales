/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice;

import com.dimata.hanoman.entity.masterdata.Contact;
import com.dimata.hanoman.entity.masterdata.PstContact;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.session.billing.SessBilling;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
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
public class sendDataInvoiceDetail extends HttpServlet {

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
        
        sentDataInvoiceDetail(request);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(this.jSONArray);
    }
    
    public void sentDataInvoiceDetail(HttpServletRequest request){
            prosesSent(request);
    }
    
    public void prosesSent(HttpServletRequest request){
        //WHERE CLAUSE
        String whereClause = "";
        
        //data dari bill main
        String cashBillMainId = FRMQueryString.requestString(request, "CASH_BILL_MAIN_ID");
        String locationId = FRMQueryString.requestString(request, "LOCATION_ID");
        String billDate = FRMQueryString.requestString(request, "BILL_DATE");
        String appUserId = FRMQueryString.requestString(request, "APP_USER_ID");
        String customerId = FRMQueryString.requestString(request, "CUSTOMER_ID");
        String docType = FRMQueryString.requestString(request, "DOC_TYPE");
        
        
        //data dari cashbilldetail
        String cashBillDetailId = FRMQueryString.requestString(request, "CASH_BILL_DETAIL_ID");
        String itemId = FRMQueryString.requestString(request, "ITEM_ID");
        String qty = FRMQueryString.requestString(request, "QTY");
        String harga = FRMQueryString.requestString(request, "HARGA");
        
        boolean isMainInvoice=false;
        boolean isDetailInvoice=false;
        boolean isOk=true;
        boolean isDataComplate=true;
        String message="";
        boolean step1=false;
        boolean step2=false;
        //input cash_bill_main
        if(!cashBillMainId.equals("") && cashBillDetailId.equals("")){
             isMainInvoice=true;
        }
        if(!cashBillMainId.equals("") && !cashBillDetailId.equals("")){
             isDetailInvoice=true;
        }
        
        if(!isMainInvoice && !isDetailInvoice){
            isOk=false;
        }
        
        if(!cashBillMainId.equals("") && cashBillDetailId.equals("")){
            if(locationId.equals("")){
                isDataComplate=false;
                isOk=false;
                message=message+" Location ID kosong";
            }
            
            if(billDate.equals("")){
                isDataComplate=false;
                isOk=false;
                message=message+" Bill Date kosong";
            }
            
            if(appUserId.equals("")){
                isDataComplate=false;
                isOk=false;
                message=message+" App User ID kosong";
            }
            
            if(customerId.equals("")){
                isDataComplate=false;
                isOk=false;
                message=message+" Customer ID kosong";
            }
            
            if(docType.equals("")){
                isDataComplate=false;
                isOk=false;
                message=message+" Doc type kosong";
            }
            if(isDataComplate & isOk){
                //proses insert data
                long cashBillMainIdL = 0;
                long locationIdL=0;
                Date billDateD= new Date();
                long appUserIdL=0;
                long customerIdL=0;
                
                try{
                    cashBillMainIdL=Long.parseLong(cashBillMainId);
                    locationIdL=Long.parseLong(locationId);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    billDateD = formatter.parse(billDate);
                    appUserIdL=Long.parseLong(appUserId);
                    customerIdL=Long.parseLong(customerId);
                    
                }catch(Exception es){
                    isOk=false;
                    message=message+"parse item dari string gagal";
                }
                
                Contact contact = new Contact();
                try{
                    if(customerIdL!=0){
                        contact = PstContact.fetchExc(customerIdL);
                    }
                }catch(Exception es){
                }
                
                if(isOk){
                    try {
                        BillMain billMain = new BillMain();
                        billMain.setOID(cashBillMainIdL);
                        billMain.setCashCashierId(1);
                        billMain.setLocationId(locationIdL);
                        billMain.setBillDate(billDateD);
                        
                        //isikan proses pencarian next number
                        String noInvoice ="";
                        //membuat open bill
                        billMain.setInvoiceCounter(SessBilling.getIntCode(billMain, billDateD, billMain.getOID(), 0, true));
                        noInvoice=SessBilling.getCodeOrderMaterial(billMain);
                        billMain.setInvoiceNumber(noInvoice);
                        billMain.setInvoiceNo(noInvoice); // billMain.getPoCode()
                        
                        billMain.setGuestName(contact.getPersonName());
                        billMain.setDocType(0);
                        billMain.setTransType(0);
                        billMain.setTransactionStatus(1);
                        billMain.setCustomerId(customerIdL);
                        billMain.setCurrencyId(1);
                        billMain.setRate(1);
                        billMain.setStockLocationId(locationIdL);
                        
                        
                        long xxx =PstBillMain.insertExcByOid(billMain);
                    } catch (DBException ex) {
                       isOk=false;
                       message=message+"insert cash bill main gagal";
                    }
                }
                
                
            }else{
                 isOk=false;
                 message=message+"Data tidak lengkap";
            }
        }
        
        //input cash_bill_detail
        if(!cashBillMainId.equals("") && !cashBillDetailId.equals("")){
           
            
            if(itemId.equals("")){
                isDataComplate=false;
                isOk=false;
                message=message+" Item ID kosong";
            }
            
            if(qty.equals("")){
                isDataComplate=false;
                isOk=false;
                message=message+" qty kosong";
            }
            
            if(harga.equals("")){
                isDataComplate=false;
                isOk=false;
                message=message+" harga kosong";
            }
            
            if(isDataComplate & isOk){
                //proses data
                long cashBillMainIdL = 0;
                long cashBillDetailIdL = 0;
                long materialId = 0;
                int qtyItem = 0;
                double priceItem = 0.0;
                
                try{
                    cashBillMainIdL=Long.parseLong(cashBillMainId);
                    if(cashBillMainIdL==0){
                        isOk=false;
                        message=message+"parse long cash bill main id gagal";
                    }
                    cashBillDetailIdL =Long.parseLong(cashBillDetailId);
                    if(cashBillDetailIdL==0){
                        isOk=false;
                        message=message+"parse long cash bill detail id gagal";
                    }
                    materialId = Long.parseLong(itemId);
                    if(materialId==0){
                        isOk=false;
                        message=message+"parse long cash bill detail id gagal";
                    }
                    qtyItem = Integer.parseInt(qty);
                    priceItem = Double.parseDouble(harga);
                }catch(Exception es){
                    isOk=false;
                    message=message+"parse item dari string gagal";
                }
                
                if(materialId!=0 && isOk){
                    Material material = new Material();
                    try{
                         material = PstMaterial.fetchExc(materialId);
                    }catch(Exception ex){
                        isOk=false;
                        message=message+"parse material menggunakan material id gagal";
                    }
                    try {
                        Billdetail billdetail = new Billdetail();
                        billdetail.setOID(cashBillDetailIdL);
                        billdetail.setBillMainId(cashBillMainIdL);
                        billdetail.setUnitId(material.getDefaultStockUnitId());
                        billdetail.setMaterialId(materialId);
                        billdetail.setQty(qtyItem);
                        billdetail.setItemPrice(priceItem);
                        billdetail.setDisc(0);
                        billdetail.setTotalAmount(billdetail.getQty()*billdetail.getItemPrice());
                        billdetail.setTotalPrice(billdetail.getQty()*billdetail.getItemPrice());
                        billdetail.setSku(material.getSku());
                        billdetail.setItemName(material.getName());
                        billdetail.setDiscType(0);
                        billdetail.setMaterialType(material.getMaterialType());
                        billdetail.setNote("");
                        Date lengofOrder =  new Date();
                        billdetail.setLengthOrder(lengofOrder);
                        long xxx =PstBillDetail.insertExcByOid(billdetail);
                    } catch (DBException ex) {
                       isOk=false;
                       message=message+"insert cash bill detail gagal";
                    }
                }else{
                    isOk=false;
                    message=message+"Data tidak lengkap";
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
                jSONObjectArray.put("CASH_BILL_MAIN_ID", ""+cashBillMainId);
                jSONObjectArray.put("CASH_BILL_DETAIL_ID", ""+cashBillDetailId);
                jSONObjectArray.put("MESSAGE", ""+message);
            }else{
                jSONObjectArray.put("STATUS", "false");
                jSONObjectArray.put("CASH_BILL_MAIN_ID", ""+cashBillMainId);
                jSONObjectArray.put("CASH_BILL_DETAIL_ID", ""+cashBillDetailId);
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
