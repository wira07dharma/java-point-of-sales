

package com.dimata.posbo.integrasidb.ajax;

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.entity.masterdata.PstRoom;
import com.dimata.posbo.entity.masterdata.Room;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.apache.tomcat.jni.User.username;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AjaxReportListSale extends HttpServlet {
    private JSONObject jSONObject = new JSONObject();
    private JSONArray jSONArray = new JSONArray();
    
    private String dataFor = "";
    private String oidDelete="";
    private String approot = "";
    private String address  = "";
    private String htmlReturn = "";
    private String bookby = "Cashier";
    private String bookfrom = "Marketing";
    
    private long oidReturn = 0;

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;
    
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
	this.oidDelete = FRMQueryString.requestString(request, "FRM_FIELD_OID_SEND");
	this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
        this.address = FRMQueryString.requestString(request, "FRM_FIELD_ADDRESS");
        this.iCommand = FRMQueryString.requestCommand(request);
        this.iErrCode = 0;
        
        this.jSONObject = new JSONObject();
        
        switch(this.iCommand){
	    case Command.SAVE :
		commandSave(request);
	    break;
		
	    
	    default : commandNone(request);
	}
        
        try{
	    
	    this.jSONObject.put("FRM_FIELD_HTML", this.htmlReturn);
	    this.jSONObject.put("FRM_FIELD_RETURN_OID", this.oidReturn);
            this.jSONObject.put("dataObject", this.jSONArray);
	}catch(JSONException jSONException){
	    jSONException.printStackTrace();
	}
        
        response.getWriter().print(this.jSONObject);
    
    }
    
    public void commandNone(HttpServletRequest request){
	
    }
    
    public void commandSave(HttpServletRequest request){
        
        if (this.dataFor.equals("inserttoother")){
            double taxIntegrasi = 0;
            try{
                taxIntegrasi = Double.parseDouble(PstSystemProperty.getValueByName("INTEGRASI_TAX_AR"));
            }catch(Exception ex){
                taxIntegrasi = 0;
            }
            try{
                this.jSONArray = new JSONArray();
                String data = FRMQueryString.requestString(request, "FRM_FIELD_OID_SEND");
                String dataTemp[] = data.split(",");
                String idKasir = PstSystemProperty.getValueByName("INTEGRASI_ID_CASHIER_AR");
                if(data.length() > 0){
                    if(dataTemp.length > 0){
                        for(int i = 0; i < dataTemp.length; i++){
                            String where = "CBM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+"="+dataTemp[i]+"";
                            
                            String order = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
                            Vector records = PstBillMain.listPerCashier(0, 0, where, order);
                            if(records.size() > 0){
                                BillMain billMain = (BillMain) records.get(0);
                                JSONObject jsondata = new JSONObject();
                                double hargaPokok = PstBillDetail.getTotalItemPrice(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"='"+dataTemp[i]+"' AND "+PstBillDetail.fieldNames[PstBillDetail.FLD_STATUS]+"='0'");
                                double taxService = PstBillDetail.getTotalTaxService(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"='"+dataTemp[i]+"' AND "+PstBillDetail.fieldNames[PstBillDetail.FLD_STATUS]+"='0'");//
                                double grandTotal = hargaPokok;//PstBillDetail.getTotalPrices(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"='"+dataTemp[i]+"' AND "+PstBillDetail.fieldNames[PstBillDetail.FLD_STATUS]+"='0'");
                                //GET USERNAME
                                AppUser appUser = new AppUser();
                                if(billMain.getAppUserId() != 0){
                                    try{
                                        appUser = PstAppUser.fetch(billMain.getAppUserId());
                                    }catch(Exception ex){
                                        
                                    }
                                }
                                
                                //buatkan fungsi untuk split data event name dan type group
                                String eventName="";
                                String groupType="";
                                String paymentType="CRD";
                                String guide="";
                                try{
                                    if(!billMain.getEventName().equals("")){
                                        String allEvent = billMain.getEventName();
                                        String[] parts = allEvent.split("\\|");
                                        try{
                                            if(parts.length>0){
                                                eventName = parts[0];
                                                groupType = parts[1];
                                                if(!groupType.equals("FIT")){
                                                    groupType="GIT";
                                                }
                                                if(parts.length>2){
                                                    paymentType = parts[2];
                                                }
                                                if(parts.length>3){
                                                    guide = parts[3];
                                                }
                                            }else{
                                                eventName = parts[0];
                                            }
                                        }catch(Exception ex){
                                        }
                                    }else{
                                        String allEvent = billMain.getNotes();
                                        String[] parts = allEvent.split("\\|");
                                        if(parts.length>0){
                                            eventName = parts[0];
                                            groupType = parts[1];
                                            if(!groupType.equals("FIT")){
                                                groupType="GIT";
                                            }
                                        }else{
                                            eventName = parts[0];
                                        }
                                    
                                    }
                                }catch(Exception ex){
                                }
                                
                                
                                jsondata.put("0", ""+String.valueOf(billMain.getOID())+"");
                                jsondata.put("1", ""+idKasir+"");

                                Location location = new Location();
                                try {
                                    location = PstLocation.fetchExc(billMain.getLocationId());
                                } catch (Exception e) {
                                }                           
                                jsondata.put("2", ""+location.getCode()+"");
                                jsondata.put("3", ""+Formater.formatDate(billMain.getBillDate(),"yyyy-MM-dd hh:mm:ss")+"");
                                jsondata.put("4", ""+Formater.formatDate(billMain.getBillDate(),"yyyy-MM-dd hh:mm:ss")+"");
                                //jsondata.put("5", "NULL");

                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(billMain.getBillDate());
                                calendar.add(Calendar.DATE, 30);
                                Date tglTemp = calendar.getTime();

                                jsondata.put("6", ""+Formater.formatDate(tglTemp,"yyyy-MM-dd")+"");
                                jsondata.put("7", ""+billMain.getInvoiceNumber()+"");
                                jsondata.put("8", ""+billMain.getCoverNumber());

                                //GET CUSTOMER DETAIL 
                                ContactList contactList = new ContactList();
                                try {
                                    contactList = PstContactList.fetchExc(billMain.getCustomerId());
                                } catch (Exception e) {
                                }
                                
                                jsondata.put("9", ""+contactList.getContactCode()+"");
                                jsondata.put("10", ""+contactList.getCompName()+"");
                                jsondata.put("11", ""+guide);
                                
                                String guestNameS="";
                                String namaTravel="";
                                try{
                                    String allEvent = billMain.getGuestName();
                                        String[] parts = allEvent.split("\\/");
                                        if(parts.length>1){
                                            namaTravel = parts[0];
                                            guestNameS = parts[1];
                                        }else{
                                            guestNameS = parts[0];
                                        }
                                }catch(Exception ex){
                                }
                                
                                jsondata.put("12", ""+guestNameS+"");
                                
                                jsondata.put("13", ""+billMain.getPaxNumber()+"");
                                jsondata.put("15", ""+eventName);//event
                                jsondata.put("16", bookby);
                                jsondata.put("17", bookfrom);
                                //GET TOTAL COST , KEMUDIAN DIBAGI JUMLAH PAX
                                double TotalCost = PstBillDetail.getSummaryCost(" "+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID()+"");
                                if(billMain.getPaxNumber() > 0){
                                    jsondata.put("18", ""+hargaPokok+"");
                                }else{
                                    jsondata.put("18", ""+0+"");
                                }

                                double totalPrice = PstBillDetail.getTotalPrice(" "+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID()+"");
                                double totals = (grandTotal-billMain.getDiscount())+taxService;//totalPrice + billMain.getTaxValue() + billMain.getServiceValue();
                                double alltotal=0;
                                if(billMain.getPaxNumber() > 0){
                                    alltotal=(totals/billMain.getPaxNumber());
                                    jsondata.put("19", ""+alltotal+"");
                                }else{
                                    jsondata.put("19", ""+0+"");
                                    alltotal=0;
                                }
                                
                                jsondata.put("20", ""+billMain.getServicePct()+"");
                                jsondata.put("21", ""+taxIntegrasi+"");
                                jsondata.put("22", ""+totals+"");
                                //jsondata.put("23", "0");
                                //jsondata.put("24", ""+totals+"");
                                //jsondata.put("25", "0");
                                //jsondata.put("26", "0");
                                //jsondata.put("27", "");
                                jsondata.put("28", ""+billMain.getInvoiceNumber()+" | "+billMain.getNotes());
                                jsondata.put("29", ""+Formater.formatDate(billMain.getBillDate(),"yyyy-MM-dd")+"");
                                jsondata.put("30", "CREDIT");
                                jsondata.put("31", "NEW DATA");
                                jsondata.put("32", "NOT PAID");
                                //jsondata.put("33", "YES");
                                jsondata.put("35", groupType);
                                jsondata.put("36", paymentType);
                                jSONArray.put(jsondata);
                                //int rowsInserted = statement2.executeUpdate();
                            }
                        }
                    }
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
            
        }
    }
    
    
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
