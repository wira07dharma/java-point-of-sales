/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.ajax;

/**
 *
 * @author Ed
 */
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.common.entity.payment.PstStandartRate;
import com.dimata.common.entity.payment.StandartRate;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.masterdata.Color;
import com.dimata.posbo.entity.masterdata.EmasLantakan;
import com.dimata.posbo.entity.masterdata.Kadar;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MaterialDetail;
import com.dimata.posbo.entity.masterdata.PstColor;
import com.dimata.posbo.entity.masterdata.PstEmasLantakan;
import com.dimata.posbo.entity.masterdata.PstKadar;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMaterialDetail;
import com.dimata.posbo.entity.warehouse.MatReceive;
import com.dimata.posbo.entity.warehouse.MatReceiveItem;
import com.dimata.posbo.entity.warehouse.PstMatReceive;
import com.dimata.posbo.entity.warehouse.PstMatReceiveItem;
import com.dimata.posbo.session.admin.SessUserSession;
import com.dimata.posbo.session.warehouse.SessMatReceive;
import com.dimata.qdep.entity.I_DocType;
import com.dimata.qdep.entity.I_PstDocType;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Ed
 */
public class AjaxConsignmentSold extends HttpServlet {

    //DATATABLES
    private String searchTerm;
    private String colName;
    private int colOrder;
    private String dir;
    private int start;
    private int amount;

    //OBJECT
    private JSONObject jSONObject = new JSONObject();
    private JSONArray jSONArray = new JSONArray();

    //LONG
    private long oid = 0;
    private long oidReturn = 0;

    //STRING
    private String dataFor = "";
    private String oidDelete = "";
    private String approot = "";
    private String htmlReturn = "";
    private String dateStart = "";
    private String dateEnd = "";
    private String username = "";
    private String userid = "";  
    private String openDate = "";  
    private String closeDate = "";  
    private String locationId = ""; 
    private String receiveDate = ""; 
    private String msg = ""; 
    

    //BOOLEAN
    private boolean privUpdate = false;
    private boolean privDelete = false;

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;
    
    //BillMain billMain = new BillMain();
    
    long oidMatReceive = 0;
    String cashCashierID = "";
    Billdetail billdetail = new Billdetail();
   // Vector listBillDetailCash = new Vector();
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //LONG
        this.oid = FRMQueryString.requestLong(request, "FRM_FIELD_OID");
        this.oidReturn = 0;

        //STRING
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.oidDelete = FRMQueryString.requestString(request, "FRM_FIELD_OID_DELETE");
        this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
        this.username = FRMQueryString.requestString(request, "FRM_FIELD_USER_NAME");
        this.userid = FRMQueryString.requestString(request, "FRM_FIELD_USER_ID");
        
        if(dataFor.equals("listResult")){
             this.locationId = FRMQueryString.requestString(request, "FRM_FIELD_NAME");
            // matReceive.setLocationId(Long.parseLong(this.locationId));
        }
        
        this.htmlReturn = "";
        this.msg = "";

        //INT
        this.iCommand = FRMQueryString.requestCommand(request);
        this.iErrCode = 0;
        //BOOLEAN
        this.privDelete = FRMQueryString.requestBoolean(request, "privdelete");
        this.privUpdate = FRMQueryString.requestBoolean(request, "privupdate");
        
        //DATE
        this.openDate = FRMQueryString.requestString(request, "FRM_FIELD_OPEN_DATE");
        this.closeDate = FRMQueryString.requestString(request, "FRM_FIELD_CLOSE_DATE");
        this.receiveDate = FRMQueryString.requestString(request, "FRM_FIELD_RECEIVE_DATE");

        //OBJECT
        this.jSONObject = new JSONObject();

        switch (this.iCommand) {
            case Command.SAVE:
                commandSave(request);
                break;

            case Command.LIST:
                commandList(request, response);
                break;

            case Command.DELETEALL:
                commandDeleteAll(request);
                break;

            case Command.DELETE:
                commandDelete(request);
                break;

            default:
                commandNone(request);
        }
        try {
            
            this.jSONObject.put("FRM_FIELD_MSG", this.msg);
            this.jSONObject.put("FRM_FIELD_HTML", this.htmlReturn);
            this.jSONObject.put("FRM_FIELD_RETURN_OID", this.oidReturn);
            this.jSONObject.put("FRM_FIELD_DATE_START", this.dateStart);
            this.jSONObject.put("FRM_FIELD_DATE_END", this.dateEnd);
        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }

        response.getWriter().print(this.jSONObject);

    }

    public void commandNone(HttpServletRequest request) {
        if (this.dataFor.equals("showform")) {
            //this.htmlReturn = showForm(request);
        }
    }

    public void commandSave(HttpServletRequest request) {
        if (this.dataFor.equals("senddata")) {
           // this.htmlReturn = saveKondisi(request);
        }
    }

    public void commandDeleteAll(HttpServletRequest request) {
        if (this.dataFor.equals("deleteAll")) {
            this.htmlReturn = deleteAll(request);
        } 
    }

    public void commandList(HttpServletRequest request, HttpServletResponse response) {
        if (this.dataFor.equals("listResult")){
            this.htmlReturn = listResult(request);
        } else if (this.dataFor.equals("createDoc")){
            this.htmlReturn = createDoc(request);
        }
    }

    public void commandDelete(HttpServletRequest request) {
        if (this.dataFor.equals("delete")) {
            this.htmlReturn = deleteAll(request);
        }
    }
    
    public String listResult(HttpServletRequest request){
        Vector listBillMainCash = new Vector();
        
        String returnData = "";
        
        String whereBillMainCash = "";
        cashCashierID = PstBillMain.listCashCashierPerDay(openDate, closeDate, 0, " cash_master.LOCATION_ID='"+this.locationId+"'");
        
        whereBillMainCash = ""
                + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                + " pm."+PstMaterial.fieldNames[PstMaterial.FLD_CONSIGMENT_TYPE]+"= "+MatReceive.TYPE_RECEIVE_PENITIPAN+" AND"
                + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

            whereBillMainCash += ""
                + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) AND ("+cashCashierID+") AND "
                + " cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]+" NOT IN(SELECT "
                + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]+" FROM "+PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM +")";
            
            listBillMainCash = PstBillMain.listMaterialItemConsigment(0, 0, whereBillMainCash, null);
        
        returnData = ""
                + "<div class='row'>"
                    + "<div class='col-sm-12'>";
                    if (!listBillMainCash.isEmpty()){
                     returnData += "<div>"
                        + "<div>"
                        + "<a class='btn btn-primary' data-for='createDoc' id='btnCreate'><i class='fa fa-file-text-o'></i>&nbsp; Create Document <a/>"
                        + "</div>"
                        + "<span class='showSuccess'>"
                        + "</span>"
                    + "</div>";
                    } else {
                        returnData += "<div>"
                        + "<br>"
                    + "</div>";
                    }
                        returnData += "<table class='table table-bordered table-striped table-info'>"
                            + "<thead class='thead-inverse'>"
                            + "<tr>"
                                + "<th> No.</th>"
                                + "<th> Item Name</th>"
                                + "<th> Item Type</th>"
                                + "<th> Qty</th>"
                                + "<th> Item Price</th>"
                                + "<th> Disc</th>"
                                + "<th> Total Price</th>"
                                + "<th> SKU</th>"
                                + "<th> Berat</th>"
                                + "<th> Supplier</th>"
                            + "</tr>"
                            + "</thead>"
                
                            + "<tbody>";
                        billdetail = new Billdetail();
                        for (int i = 0; i < listBillMainCash.size(); i++) {
                            billdetail = (Billdetail) listBillMainCash.get(i);
                            ContactList cl = new ContactList();
                                try {
                                    cl = PstContactList.fetchExc(billdetail.getSupplierId());
                                } catch (DBException ex) {
                                }
                            returnData+= "<tr>"
                                    + "<td>"+(i + 1) +"</td>"
                                    + "<td>"+ billdetail.getItemName()+"</td>"
                                    + "<td>"+ Material.MATERIAL_TYPE_TITLE[billdetail.getMaterial_jenis()]+"</td>"
                                    + "<td>"+ billdetail.getQty()+"</td>"
                                    + "<td>"+ billdetail.getItemPrice()+"</td>"
                                    + "<td>"+ billdetail.getDisc()+"</td>"
                                    + "<td>"+ billdetail.getTotalPrice()+"</td>"
                                    + "<td>"+ billdetail.getSku()+"</td>"
                                    + "<td>"+ billdetail.getBerat()+"</td>"
                                    + "<td>"+ cl.getCompName()+"</td>"
                            + "</tr>";
                            }
                        
                            if(listBillMainCash.isEmpty()){
                                returnData+= "<tr>"
                                    + "<td id='nodata' colspan='10' class='text-center'> Data tidak tersedia! </td>"
                            + "</tr>";
                            }
                                returnData += "</tbody>"
                        
                        + "</table>"
                    + "</div>"
                + "</div>";
                                
        return returnData;
    }
    
    public void listData(){
        
       
    }
    
    private String createDoc(HttpServletRequest request) {
        
        Vector listGroupBy = new Vector();
        //Vector listCheck = new Vector();
        String returnData = "";
        String whereGroupBy = "";
        String whereCheckList = "";
        
        MatReceive matReceive = new MatReceive();
        MatReceiveItem matreceiveitem = new MatReceiveItem();           
        
        whereGroupBy = ""
                + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                + " pm."+PstMaterial.fieldNames[PstMaterial.FLD_CONSIGMENT_TYPE]+"= "+MatReceive.TYPE_RECEIVE_PENITIPAN+ " AND"
                + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

        whereGroupBy += ""
                + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) AND ("+cashCashierID+") AND "
                + " cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]+" NOT IN(SELECT "
                + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]+" FROM "+PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM +")"
                + " GROUP BY pm."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE]+", pm."+PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID]+" ";
                
            //Gropu by supplier and material type (emas/berlian) 
            listGroupBy = PstBillMain.listGroupBy(0, 0, whereGroupBy, " pm."+PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID]+ ","+ " pm."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE]);
            
        //listData();
        boolean success = false;
        
        
        for (int i = 0; i < listGroupBy.size(); i++) {
            
            Billdetail b = new Billdetail();
            b = (Billdetail) listGroupBy.get(i);
            String whereCount = "";
        
         whereCount = ""
                + " pm."+PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID]+"="+b.getSupplierId()+" AND "
                + " pm."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE]+"="+b.getMaterial_jenis()+" AND "
                + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                + " pm."+PstMaterial.fieldNames[PstMaterial.FLD_CONSIGMENT_TYPE]+"= "+MatReceive.TYPE_RECEIVE_PENITIPAN+ " AND"
                + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

            whereCount += ""
                + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) AND ("+cashCashierID+") AND "
                + " cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]+" NOT IN(SELECT "
                + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]+" FROM "+PstMatReceiveItem.TBL_MAT_RECEIVE_ITEM +")";
            
            //Avoid same datas
//            Vector checkItem = PstBillMain.list(0, 0, whereCount, null);

            //reccodecnt
                 Date rDate = new Date();

                 int docType = -1;
                     try {
                         I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(I_DocType.DOCTYPE_CLASSNAME).newInstance();
                         docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_LMRR);
                     } catch (Exception e) {
                         System.out.println("Exc : "+e.toString());
                     }

                     boolean incrementAllReceiveType = true;
                     int counter = 0;  

            //LIST FOR MATERIAL ITEM
            Vector listItem = PstBillMain.listMaterialItem(0, 0, whereCount, null);

                 // RATE
                 Vector listRate = new Vector();
                 listRate = PstStandartRate.list(0, 0, PstStandartRate.fieldNames[PstStandartRate.FLD_SELLING_RATE]+" = 1", null);
                 StandartRate standartRate = (StandartRate) listRate.get(0);
                 Material mat = new Material();
                 Vector vMat = new Vector();
                 vMat = PstMaterial.list(0,0,PstMaterial.fieldNames[PstMaterial.FLD_SKU]+" = '"+ b.getSku()+"'", null);
                 mat = (Material) vMat.get(0);
                 
                 Vector vHel = new Vector();
                 EmasLantakan el = new EmasLantakan();
                 
                 vHel = PstEmasLantakan.list(0, 0, null, null);
                 el = (EmasLantakan) vHel.get(0);
                 // MATRECEIVE
                 matReceive.setLocationId(Long.parseLong(locationId));
                 matReceive.setReceiveDate(Formater.formatDate(receiveDate, "yyyy-MM-dd"));
                 matReceive.setSupplierId(b.getSupplierId());
                 matReceive.setRecCodeCnt(SessMatReceive.getIntCode(matReceive, rDate, 0, docType, counter, incrementAllReceiveType));
                 matReceive.setRecCode(SessMatReceive.getCodeReceive(matReceive));
                 matReceive.setInvoiceSupplier(SessMatReceive.getCodeReceive(matReceive));
                 matReceive.setCurrencyId(standartRate.getCurrencyTypeId());
                 matReceive.setTransRate(standartRate.getSellingRate());
                 matReceive.setReceiveType(MatReceive.TYPE_RECEIVE_PENITIPAN);
                 matReceive.setDiscount(b.getDisc());
                 matReceive.setHel(matReceive.getHel());
                 matReceive.setBerat(b.getBerat());
                 matReceive.setReceiveItemType(b.getMaterial_jenis());
                 matReceive.setKepemilikanId(mat.getKepemilikanId());
                 matReceive.setRemark(mat.getMaterialDescription());
                 matReceive.setHel(el.getHargaBeli());

                 PstMatReceive pstMatReceive = new PstMatReceive();

                 try {
                      //Vector newww = PstMatReceive.getRekapForPurchaseMainFromPr(listBillMainCash);
                    oidMatReceive = pstMatReceive.insertExc(matReceive);
                     success = true;
                 } catch (DBException ex) {
                    success = false;
                 }
             
                //RECEIVE ITEM
                
                for (int j = 0; j < listItem.size(); j++) {
                    Billdetail billDetailItem = (Billdetail) listItem.get(j);
                    
                    Material mat2 = new Material();
                        try {
                            mat2 = PstMaterial.fetchExc(billDetailItem.getMaterialId());
                        } catch (DBException ex) {
                            Logger.getLogger(AjaxConsignmentSold.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    
                    matreceiveitem.setCurrencyId(standartRate.getCurrencyTypeId());
                    matreceiveitem.setReceiveMaterialId(oidMatReceive);
                    matreceiveitem.setRecCode(SessMatReceive.getCodeReceive(matReceive));
                    matreceiveitem.setSupplierId(billDetailItem.getSupplierId());
                    matreceiveitem.setReceiveDate(Formater.formatDate(receiveDate, "yyyy-MM-dd"));
                    matreceiveitem.setCashBillMainId(billDetailItem.getBillMainId());
                    matreceiveitem.setCashBillDetailId(billDetailItem.getBillDetailId());
                    matreceiveitem.setCost(billDetailItem.getCost());
                    matreceiveitem.setDiscount(billDetailItem.getDisc());
                    matreceiveitem.setMaterialId(billDetailItem.getMaterialId());
                    matreceiveitem.setUnitId(billDetailItem.getUnitId());
                    matreceiveitem.setBerat(billDetailItem.getBerat());
                    //matreceiveitem.setReceiveMaterialId(billdetail.getOID());
                    matreceiveitem.setQty(billDetailItem.getQty());
                    matreceiveitem.setRemark(mat2.getMaterialDescription());
                    
                    PstMatReceiveItem pstMatReceiveItem = new PstMatReceiveItem();
                        try {
                            pstMatReceiveItem.insertExc(matreceiveitem);
                            success = true;
                        } catch (DBException ex) {
                           success = false;
                        } 
                        }
            }  
        
        if (success){
        
        returnData = ""
                + "<p class='text-success'> Dokumen berhasil dibuat!</p>";
            //msg = "Dokumen berhasil dibuat";
        } else {
            returnData += ""
                + "<p class='text-danger'> Dokumen gagal dibuat!</p>";
        }
        return returnData;
    }


    public String deleteAll(HttpServletRequest request) {
        String returnData = "";
//        CtrlConsignmentSold ctrlConsignmentSold = new CtrlConsignmentSold(request);
//        try {
//            ctrlConsignmentSold.action(iCommand, oid);
//        } catch (Exception ex) {
//            Logger.getLogger(AjaxNilaiTukarEmas.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        returnData = ctrlConsignmentSold.getMessage();
        return returnData;
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
