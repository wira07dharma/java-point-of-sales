/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice;

import com.dimata.hanoman.entity.masterdata.Contact;
import com.dimata.hanoman.entity.masterdata.PstContact;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.BillMainCustomeData;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.billing.PstBillMainCustomeData;
import com.dimata.pos.entity.payment.CashPayments;
import com.dimata.pos.entity.payment.CashPayments1;
import com.dimata.pos.entity.payment.CashReturn;
import com.dimata.pos.entity.payment.PstCashPayment1;
import com.dimata.pos.entity.payment.PstCashReturn;
import com.dimata.pos.session.billing.SessBilling;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.qdep.form.FRMQueryString;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author dimata005
 */
public class sendDataInvoice extends HttpServlet {

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
        String cashBillMainId = FRMQueryString.requestString(request, "CASH_BILL_MAIN_ID");
        String locationId = FRMQueryString.requestString(request, "LOCATION_ID");
        String billDate = FRMQueryString.requestString(request, "BILL_DATE");
        String appUserId = FRMQueryString.requestString(request, "APP_USER_ID");
        String customerId = FRMQueryString.requestString(request, "CUSTOMER_ID");
        String image = FRMQueryString.requestString(request, "IMAGE");
        String imageName = FRMQueryString.requestString(request, "IMAGE_NAME");
        String latitude = FRMQueryString.requestString(request, "LATITUDE");
        String longitude = FRMQueryString.requestString(request, "LONGITUDE");
        String voiceName = FRMQueryString.requestString(request, "VOICE_NAME");
        
        //data dari cashbilldetail
        String cashBillDetailId = FRMQueryString.requestString(request, "CASH_BILL_DETAIL_ID");
        String itemId = FRMQueryString.requestString(request, "ITEM_ID");
        String qty = FRMQueryString.requestString(request, "QTY");
        String harga = FRMQueryString.requestString(request, "HARGA");
        
        String docType = FRMQueryString.requestString(request, "DOC_TYPE");
        String transType = FRMQueryString.requestString(request, "TRANSACTION_TYPE");
        String transStatus = FRMQueryString.requestString(request, "TRANSACTION_STATUS");
        String invoiceNumberCash = FRMQueryString.requestString(request, "INVOICE_NUMBER");
        String amount = FRMQueryString.requestString(request, "AMOUNT");
        String cashCashierId = FRMQueryString.requestString(request, "CASH_CASHIER_ID");
        
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
            
            if(transType.equals("")){
                isDataComplate=false;
                isOk=false;
                message=message+" transType kosong";
            }
            
            if(transStatus.equals("")){
                isDataComplate=false;
                isOk=false;
                message=message+" transStatus kosong";
            }
            
            
            //image
            try
            {
                String webAppPath = getServletContext().getRealPath("/imgupload");    
                //This will decode the String which is encoded by using Base64 class
                BufferedImage bImageFromConvert = decodeToImage(image);
                 //write the image to a new location with a different file name(optionally)
                ImageIO.write(bImageFromConvert, "jpg", new File(webAppPath+"/"+imageName+".jpg"));
            }
            catch(Exception e){
                //isDataComplate=false;
                //isOk=false;
                message=message+" Image tidak bisa di upload "+e;
            }
            
            if(isDataComplate & isOk){
                //proses insert data
                long cashBillMainIdL = 0;
                long locationIdL=0;
                Date billDateD= new Date();
                long appUserIdL=0;
                long customerIdL=0;
                int docTypeT=0;
                int transStatusT=0;
                int transTypeT=0;
                double dAmount = 0;
                long paymentSystemId=0;
                long lcashCashierId=0;
                try{
                    cashBillMainIdL=Long.parseLong(cashBillMainId);
                    locationIdL=Long.parseLong(locationId);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    billDateD = formatter.parse(billDate);
                    appUserIdL=Long.parseLong(appUserId);
                    customerIdL=Long.parseLong(customerId);
                    docTypeT=Integer.parseInt(docType);
                    transStatusT=Integer.parseInt(transStatus);
                    transTypeT=Integer.parseInt(transType);
                    dAmount= Double.parseDouble(amount);
                    paymentSystemId = Long.parseLong("504404634093158004");
                    lcashCashierId = Long.parseLong(cashCashierId);
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
                       
                        billMain.setLocationId(locationIdL);
                        billMain.setBillDate(billDateD);
                        
                        billMain.setDocType(docTypeT);
                        billMain.setTransctionType(transTypeT);
                        billMain.setTransactionStatus(transStatusT);
                        billMain.setBillDate(billDateD);
                        //isikan proses pencarian next number
                        if(billMain.getDocType()==0 && billMain.getTransactionStatus()==1 && billMain.getTransctionType()==0){
                            String noInvoice ="";
                            //membuat open bill
                            billMain.setInvoiceCounter(SessBilling.getIntCode(billMain, billDateD, billMain.getOID(), 0, true));
                            noInvoice=SessBilling.getCodeOrderMaterial(billMain);
                            billMain.setInvoiceNumber(noInvoice);
                            billMain.setInvoiceNo(noInvoice); // billMain.getPoCode()
                            billMain.setStatusInv(1);
                            billMain.setCashCashierId(1);
                        }else{
                            if(invoiceNumberCash.equals("")){
                                isDataComplate=false;
                                isOk=false;
                                message=message+" transStatus kosong";
                            }
                            billMain.setInvoiceNumber(invoiceNumberCash);
                            billMain.setInvoiceNo(invoiceNumberCash); // billMain.getPoCode()
                            billMain.setStatusInv(0);
                            billMain.setCashCashierId(lcashCashierId);
                        }
                        billMain.setGuestName(contact.getPersonName());
                        billMain.setCustomerId(customerIdL);
                        billMain.setCurrencyId(1);
                        billMain.setRate(1);
                        billMain.setStockLocationId(locationIdL);
                        
                        if(isOk){
                            long cashBillMainOid =PstBillMain.insertExcByOid(billMain);

                            if(cashBillMainOid!=0){
                                //proses untuk insert latitude dan gambar dan voice
                                BillMainCustomeData billMainCustomeData = new BillMainCustomeData();
                                try{
                                    billMainCustomeData.setLatitude(Double.parseDouble(latitude));
                                    billMainCustomeData.setLongitude(Double.parseDouble(longitude));
                                }catch(Exception ex){
                                    billMainCustomeData.setLatitude(0);
                                    billMainCustomeData.setLongitude(0);
                                }
                                billMainCustomeData.setType(2);
                                billMainCustomeData.setName("Lokasi");
                                billMainCustomeData.setValue("Lokasi");
                                billMainCustomeData.setCashBillMainId(cashBillMainIdL);
                                try {
                                    long customeData = PstBillMainCustomeData.insertExc(billMainCustomeData);
                                } catch (Exception ex) {
                                }

                                BillMainCustomeData billMainCustomeData2= new BillMainCustomeData();
                                billMainCustomeData2.setLatitude(Double.parseDouble(latitude));
                                billMainCustomeData2.setLongitude(Double.parseDouble(longitude));
                                billMainCustomeData2.setType(0);
                                billMainCustomeData2.setName(""+imageName);
                                billMainCustomeData2.setValue("");
                                billMainCustomeData.setCashBillMainId(cashBillMainIdL);
                                try {
                                    long customeData = PstBillMainCustomeData.insertExc(billMainCustomeData2);
                                } catch (Exception ex) {
                                }
                            }  

                            if(cashBillMainOid!=0){
                                if(billMain.getDocType()==0 && billMain.getTransactionStatus()==0 && billMain.getTransctionType()==0){
                                    //proses untuk input payment
                                    CashPayments1 cashPayment = new CashPayments1();
                                    cashPayment.setBillMainId(cashBillMainOid);
                                    cashPayment.setCurrencyId(1);
                                    cashPayment.setAmount(dAmount);
                                    cashPayment.setPayDateTime(billDateD);
                                    cashPayment.setPaymentStatus(0);
                                    cashPayment.setPaymentType(paymentSystemId);
                                    long paymentSystemOid = PstCashPayment1.insertExc(cashPayment);
                                    if(paymentSystemOid!=0){
                                        CashReturn cashReturn = new CashReturn();
                                        cashReturn.setAmount(0);
                                        cashReturn.setBillMainId(cashBillMainOid);
                                        cashReturn.setCurrencyId(1);
                                        cashReturn.setRate(1);
                                        long cashReturnOid = PstCashReturn.insertExc(cashReturn);
                                    } 
                                }
                            }
                        }
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
    
    public static BufferedImage decodeToImage(String imageString)
    {
        BufferedImage image = null;
        byte[] imageByte;
        try
        {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return image;
    }
    
    public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
 
        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();
 
            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);
 
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }
    
}
