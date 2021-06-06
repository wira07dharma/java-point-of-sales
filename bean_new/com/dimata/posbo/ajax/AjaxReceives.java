package com.dimata.posbo.ajax;

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactClass;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.DailyRate;
import com.dimata.common.entity.payment.PaymentSystem;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.common.entity.payment.PstDailyRate;
import com.dimata.common.entity.payment.PstStandartRate;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.gui.jsp.ControlCombo;
import com.dimata.posbo.entity.arap.AccPayable;
import com.dimata.posbo.entity.arap.AccPayableDetail;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.MatCurrency;
import com.dimata.posbo.entity.masterdata.MatVendorPrice;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MaterialStock;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.purchasing.PstPurchaseOrder;
import com.dimata.posbo.entity.purchasing.PstPurchaseOrderItem;
import com.dimata.posbo.entity.purchasing.PurchaseOrder;
import com.dimata.posbo.entity.purchasing.PurchaseOrderItem;
import com.dimata.posbo.entity.search.SrcAccPayable;
import com.dimata.posbo.entity.search.SrcMaterial;
import com.dimata.posbo.entity.warehouse.ForwarderInfo;
import com.dimata.posbo.entity.warehouse.MatReceive;
import com.dimata.posbo.entity.warehouse.MatReceiveItem;
import com.dimata.posbo.entity.warehouse.PstMatReceive;
import com.dimata.posbo.entity.warehouse.PstMatReceiveItem;
import com.dimata.posbo.entity.warehouse.PstReceiveStockCode;
import com.dimata.posbo.form.warehouse.CtrlForwarderInfo;
import com.dimata.posbo.form.warehouse.CtrlMatReceive;
import com.dimata.posbo.form.warehouse.CtrlMatReceiveItem;
import com.dimata.posbo.form.warehouse.FrmForwarderInfo;
import com.dimata.posbo.form.warehouse.FrmMatReceive;
import com.dimata.posbo.form.warehouse.FrmMatReceiveItem;
import com.dimata.posbo.session.arap.SessAccPayable;
import com.dimata.posbo.session.warehouse.SessDoSearch;
import com.dimata.posbo.session.warehouse.SessForwarderInfo;
import com.dimata.posbo.session.warehouse.SessMatReturn;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AjaxReceives
extends HttpServlet{
    
    public static final String textListOrderItem[][] = {
        {"No","Sku","Nama Barang","Kadaluarsa","Unit","Harga Beli","Ongkos Kirim","Mata Uang","Qty","Total Beli","Diskon Terakhir %",//10
         "Diskon1 %","Diskon2 %","Discount Nominal","Qty Entri","Unit Konversi","Aksi","Bonus"},//17
        {"No","Code","Name","Expired Date","Unit","Cost","Delivery Cost","Currency","Qty","Total Cost","last Discount %","Discount1 %",
         "Discount2 %","Disc. Nominal","Qty Entri","Unit Konversi","Action","Bonus"}
    };
    
    public static final String textListOrderHeader[][] = {
        {"Nomor","Lokasi","Tanggal","Supplier","Status","Keterangan","Nomor Invoice","Ppn","Waktu","Mata Uang","Sub Total","Grand Total","Include", "%","Terms","Days","Rate"},
        {"Number","Location","Date","Supplier","Status","Remark","Supplier Invoice","VAT","Time","Currency","Sub Total","Grand Total","Include", "%","Terms","Days","Rate"}
    };

    public static final String textListButton[][]={
        {"Simpan","Tutup","Hapus","Tambah Semua Item"},
        {"Save","Close","Delete","Add All Item"}
    };
    
    public static final String textListMaterialHeader[][] = {
        {"No","Grup","Sku","Nama Barang","Unit","Harga Beli","Qty"},
        {"No","Category","Code","Item","Unit","Cost","Qty"}
    };
    
    public static final String textListForwarderInfo[][] = {
        {"Nomor", "Nama Perusahaan", "Tanggal", "Total Biaya", "Keterangan", "Informasi Forwarder", "Tidak Ada Informasi Forwarder!","Sebelum Status Final Pastikan Informasi Forwarder diisi jika ada !","Tambahkan Info","Ubah Info","Hapus Info"},
        {"Number", "Company Name", "Date", "Total Cost", "Remark", "Forwarder Information", "No Forwarder Information!","Prior to final status, make sure forwarder information is entered if required !","Add Info","Edit Info","Delete Info"}
    };
    
    public static final String textListDetailPayment[][] = {
        {"Nomor","Rincian Pembayaran","Pembayaran","Total Penerimaan"},
        {"Number","Detail Of Payment","Payment","Receiving Summary"}
    };
    
    public static final String textListHeaderDetail[][] = {
	{"Tanggal","Lokasi","Sistem Pembayaran","Mata Uang","Rate","Jumlah dalam Mata Uang","Jumlah"},
	{"Date","Location","Payment System","Currency","Rate","Amount in Currency","Amount"}
    };
    
    public static final String textListHeader[][] = {
	{"Total Pembayaran","Saldo Hutang","Tambah Pembayaran"},
	{"Total", "AP Balance","Add Payment"}
    };
    
    public static final String textListPurchaseOrderHeader[][] = { 
	{"No","Kode","Tanggal","Status","Keterangan","Mata Uang"},
	{"No","Code","Date","Status","Remark","Currency"}
    };
    
    public static final String textListOrderItem2[][] = {
        {"No","Sku","Nama Barang","Kadaluarsa","Unit","Harga/Stok","Ongkos Kirim","Mata Uang","Qty @stock","Total Beli","Diskon Terakhir %",//10
         "Diskon1 %","Diskon2 %","Discount Nominal","Qty Entri","Unit Order","Harga Total", "Hapus","Bonus"},//17
       {"No","Code","Name","Expired Date","Unit","Price/Stock","Delivery Cost","Currency","Qty","Total Cost","last Discount %","Discount1 %",
        "Discount2 %","Disc. Nominal","Qty Entri","Unit Order","Total Price", "Delete","Bonus"}
     };
    
    
    public static final String textListGlobal[][] = {
        {"Tidak ada item order pembelian"},
        {"There is no goods purchase order item"}
    };

    public static final String textMaterialHeader[][] = {
        {"Kategori","Sku","Nama Barang","Daftar Purchase Order"},
        {"Category","Code","Name","Purchase Order List"}
    };

    public static final String textListOrderItem3[][] = {
        {"No","Sku","Nama Barang","Unit Stok","Hrg. Beli","Mata Uang","Qty","Total Beli","Unit Order"},
        {"No","Code","Name","Unit","Buy Price","Currency","Quantity","Sub Total","Unit Request"}
    };
    
    private String listProccess(HttpServletRequest request){
        String result ="";
        String loadType= FRMQueryString.requestString(request, "loadType");
        if (loadType.equals("getListReceiveItem")){
            result = getListReceiveItem(request);
        }else if (loadType.equals("getListReceiveItemBonus")){
            result = getListReceiveItemBonus(request);
        }else if (loadType.equals("getListError")){
            result = getListError(request);
        }else if (loadType.equals("getListStatus")){
            result = getListStatus(request);
        }else if (loadType.equals("getListCurrency")){
            result = getListCurrency(request);
        }else if (loadType.equals("getListItemModal")){
            result = getListItemModal(request);
        }else if (loadType.equals("getListPoBySuplier")){
            result = getListPoBySuplier(request);
        }else if (loadType.equals("getListItemModalPo")){
            result = getListItemModalPo(request);
        }else if (loadType.equals("getListItemModalPoBySkuName")){
            result = getListItemModalPoBySkuName(request);
        }
        return result;
    }
    
    private String getProcess(HttpServletRequest request){
        String result ="";
        String loadType= FRMQueryString.requestString(request, "loadType");
        if (loadType.equals("getSaveButton")){
            result = getSaveButton(request);
        }else if (loadType.equals("getCodeReceive")){
            result = getCodeReceive(request);
        }else if (loadType.equals("showEditControl")){
            result = showEditControl(request);
        }else if (loadType.equals("getForwaderInfo")){
            result = getForwaderInfo(request);
        }else if (loadType.equals("getForwaderControl")){
            result = getForwaderControl(request);
        }else if (loadType.equals("getListPayment")){
            result = getListPayment(request); 
        }else if (loadType.equals("getSummaryReceive")){
            result = getSummaryReceive(request);
        }else if (loadType.equals("getTotalByOidReceiveMaterial")){
            result = getTotalByOidReceiveMaterial(request);
        }else if (loadType.equals("getItemBySkuOrName")){
            result = getItemBySkuOrName(request);
        }else if (loadType.equals("getDeleteButton")){
            result = getDeleteButton(request);
        }else if (loadType.equals("getButtonAddAll")){
            result = getButtonAddAll(request);
        }else if (loadType.equals("getAddAllItem")){
            result = getAddAllItem(request);
        }else if (loadType.equals("showEditControlPo")){
            result = showEditControlPo(request);
        }
        return result ;
    }
    
    private String saveProcess(HttpServletRequest request){
        String result ="";
        String loadType= FRMQueryString.requestString(request, "loadType");
        if (loadType.equals("saveMasterReceiving")){
            result = saveMasterReceiving(request);
        }else if (loadType.equals("saveReceiveItem")){
            result = saveReceiveItem(request);
        }else if (loadType.equals("saveEditReceiveItem")){
            result = saveEditReceiveItem(request);
        }else if (loadType.equals("saveForwarder")){
            result = saveForwarder(request);
        }else if (loadType.equals("saveAllItem")){
            result = saveAllItem(request);
        }else if (loadType.equals("saveReceiveItemPo")){
            result = saveReceiveItemPo(request);
        }else if (loadType.equals("saveEditReceiveItemPo")){
            result = saveEditReceiveItemPo(request);
        }
        return result ;
    }
    
    private String deleteProcess(HttpServletRequest request){
        String result ="";

        String loadType= FRMQueryString.requestString(request, "loadType");
        if (loadType.equals("deleteReceiveItem")){
            result = deleteReceiveItem(request);
        }else if (loadType.equals("deleteForwarder")){
            result = deleteForwarder(request);
        }else if (loadType.equals("deleteReceiving")){
            result = deleteReceiving(request);
        }

        return result;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        String result ="";
        int command = FRMQueryString.requestInt(request,"command");
        response.setContentType("text/html;charset=UTF-8");
        
        switch (command) {
            case Command.LIST :
                result = listProccess(request);
                response.getWriter().write(result);
                break;
            case Command.GET:
                result = getProcess(request);
                response.getWriter().write(result);
                break;
            case Command.SAVE:
                result = saveProcess(request);
                response.getWriter().write(result);
                break;
            case Command.DELETE:
                result = deleteProcess(request);
                response.getWriter().write(result);
                break;            
            default:

        }
    }
    
    private String getListReceiveItem(HttpServletRequest request){
        String result ="";
        Vector listMatReceiveItem = new Vector();
        int collspan =0;
        int start=0;
        int i=0;
        FrmMatReceive frmMatReceive = new FrmMatReceive();
        FrmMatReceiveItem frmMatReceiveItem = new FrmMatReceiveItem();
        String whereUnit = "";
        boolean btnAddSHow = true;
        MatReceive rec = new MatReceive();
        
        Vector listBuyUnit = PstUnit.list(0,1000,whereUnit,"");
        Vector index_value = new Vector(1,1);
        Vector index_key = new Vector(1,1);
        index_key.add("-");
        index_value.add("0");
        for(int j=0;j<listBuyUnit.size();j++){
            Unit mateUnit = (Unit)listBuyUnit.get(j);
            index_key.add(mateUnit.getCode());
            index_value.add(""+mateUnit.getOID());
        }
        
        //REQUEST FORM
        long oidReceiveMaterial = FRMQueryString.requestLong(request, "oidReceiveMaterial");
        long oidCurrencyRec = FRMQueryString.requestLong(request, "oidCurrencyRec");
        boolean privManageData = FRMQueryString.requestBoolean(request, "privManageData");
        boolean privShowQtyPrice = FRMQueryString.requestBoolean(request, "privShowQtyPrice");
        int receiveStatus = FRMQueryString.requestInt(request, "receiveStatus");
        boolean bEnableExpiredDate = FRMQueryString.requestBoolean(request, "bEnableExpiredDate");
        int language = FRMQueryString.requestInt(request, "lang");
      
                
        String whereClauseItem = ""
            + " "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial+""
            + " AND "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS]+"=0";
        String orderClauseItem = ""
            + " RMI."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]+"";
        
        listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(0,0,whereClauseItem, orderClauseItem);
        
        boolean privAdd = FRMQueryString.requestBoolean(request, "privAdd");
        
        try {
            rec = PstMatReceive.fetchExc(oidReceiveMaterial);
        } catch (Exception e) {
        }
        
        if (rec.getPurchaseOrderId()==0){
            //INPUT FORM SHOW TERM CONDITION
            if (rec.getOID()!=0){
                if(privAdd==false || rec.getReceiveStatus()!=I_DocStatus.DOCUMENT_STATUS_DRAFT){
                   btnAddSHow = false;
                }
                if(rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL || rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
                   btnAddSHow = false;       
                }
            }else{
                btnAddSHow = false;
            }

            Vector listError = getListErrorVect(oidReceiveMaterial);

            result +=""
            + "<table class='table table-hover table-striped table-bordered' width='100%'>"
                + "<thead style='font-size:13px;'>"
                    + "<tr>"
                        + "<th style='width:1%'>"+textListOrderItem[language][0]+"</th>"  
                        + "<th style='width:10%'>"+textListOrderItem[language][1]+"</th>" 
                        + "<th style='width:10%'>"+textListOrderItem[language][2]+"</th>" ;
                        if(bEnableExpiredDate){
                            result += "<th style='width:5%'>"+textListOrderItem[language][3]+"</th>";
                            collspan = collspan + 4;
                        }else{
                            collspan = collspan + 3;
                        }
            result +=""
                        + "<th style='width:5%'>"+textListOrderItem[language][14]+"</th>" 
                        + "<th style='width:8%'>"+textListOrderItem[language][15]+"</th>" 
                        + "<th style='width:7%'>"+textListOrderItem[language][4]+"</th>" ;
                        collspan = collspan + 3;
                        if(privShowQtyPrice){
                            result += "<th style='width:10%'>"+textListOrderItem[language][5]+"</th>";
                            result += "<th style='width:5%'>"+textListOrderItem[language][11]+"</th>";
                            result += "<th style='width:5%'>"+textListOrderItem[language][12]+"</th>";
                            result += "<th style='width:8%'>"+textListOrderItem[language][13]+"</th>";
                            result += "<th style='width:8%'>"+textListOrderItem[language][6]+"</th>";
                            result += "<th style='width:5%'>"+textListOrderItem[language][8]+"</th>";
                            result += "<th style='width:5%'>"+textListOrderItem[language][17]+"</th>"; 
                            result += "<th style='width:10%'>"+textListOrderItem[language][9]+"</th>"; 
                            collspan = collspan + 8;
                        }else{
                            result += ""
                                + "<th style='width:5%'>"+textListOrderItem[language][8]+"</th>"
                                + "<th style='width:5%'>"+textListOrderItem[language][17]+"</th>";
                            collspan = collspan + 1;
                        }
            result +=""
                        + "<th>"+textListOrderItem[language][16]+"</th>"            
                    + "</tr>"
                + "</thead>"
                + "<tbody>";
                if (listMatReceiveItem.size()>0){
                    for(i=0; i<listMatReceiveItem.size(); i++){
                        Vector temp = (Vector)listMatReceiveItem.get(i);
                        MatReceiveItem recItem = (MatReceiveItem)temp.get(0);
                        Material mat = (Material)temp.get(1);
                        Unit unit = (Unit)temp.get(2);

                        Unit untiKonv = new Unit();
                        try{
                            untiKonv = PstUnit.fetchExc(recItem.getUnitKonversi());
                        }catch(Exception ex){

                        }

                        start = start + 1;
                        double totalForwarderCost = recItem.getForwarderCost() * recItem.getQty();
                        result += "<tr id='receive-"+i+"'>";
                        result += "<td>"+start+"</td>";
                        result += "<td>"+mat.getSku()+"</td>";
                        if(privShowQtyPrice){
                            if(mat.getCurrBuyPrice()<recItem.getCost()){
                                result += "<td style='cursor:pointer;' class='editItems' data-name='"+mat.getName()+"' data-code='"+mat.getSku()+"' data-oid='"+recItem.getMaterialId()+"'>"+mat.getName()+" &nbsp;<img src='../../../images/DOTreddotANI.gif'><font color='#FF0000'>[Edit]</font></td>";
                            }else{
                                result += "<td style='cursor:pointer;' class='editItems' data-name='"+mat.getName()+"' data-code='"+mat.getSku()+"' data-oid='"+recItem.getMaterialId()+"'>"+mat.getName()+" &bspb; [Edit]</td>";
                            }
                        }else{
                            result += "<td>"+mat.getName()+"</td>";
                        }

                        if(bEnableExpiredDate){
                            result += "<td>"+Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy")+"</td>";
                        }
                        result += "<td>"+recItem.getQtyEntry()+"</td>";
                        result += "<td>"+untiKonv.getCode()+"</td>";
                        result += "<td>"+unit.getCode()+"</td>";
                        if(privShowQtyPrice){
                            result += "<td>"+FRMHandler.userFormatStringDecimal(recItem.getCost())+"</td>";
                            result += "<td>"+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"</td>";
                            result += "<td>"+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"</td>";
                            result += "<td>"+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"</td>";
                            result += "<td>"+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"</td>";
                        }
                        if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){   
                            String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID]+"="+recItem.getOID();
                            int cnt = PstReceiveStockCode.getCount(where);
                            double recQtyPerBuyUnit = recItem.getQty();
                            double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(mat.getBuyUnitId(), mat.getDefaultStockUnitId());
                            double recQty = recQtyPerBuyUnit * qtyPerSellingUnit;
                            double max = recQty;

                            result += "<td>&nbsp;</td>";               
                            result += "<td>"+String.valueOf(recItem.getOID())+""+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</tr>";
                        }else{
                            result +="<td>"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</td>";
                            result +="<td>&nbsp;</td>";
                        }

                        if(privShowQtyPrice){
                            result += "<td>"+FRMHandler.userFormatStringDecimal(recItem.getTotal()+totalForwarderCost)+"</td>";
                        }
                        //Action
                        result += ""
                            + "<td>";
                                //BUTTON EDIT
                                if((privManageData || privShowQtyPrice)&& (receiveStatus==I_DocStatus.DOCUMENT_STATUS_DRAFT)){
                                     result += ""
                                        + "<button id='edit-"+i+"' data-recitem='"+recItem.getOID()+"' class='btn btn-warning editItem' type='button' style='height: 34px;'>"
                                            + "<i class='glyphicon glyphicon-pencil'></i> [e]"
                                        + "</button>";

                                    result +=""
                                    + "<button id='del-"+i+"' data-recitem='"+recItem.getOID()+"' class='btn btn-danger deleteItem' type='button' style='height: 34px;'>"
                                        + "<i class='glyphicon glyphicon-remove'></i> [d]"
                                    + "</button>";                                                        
                                }
                                //BUTTON DELETE
                        result +=""    
                            + "</td>";
                        result += "</tr>";
                    }
                }else{
                    collspan = collspan + 1;
                    result += ""
                        + "<tr>"
                            + "<td colspan='"+collspan+"'><center>No Data</center></td>"
                        + "</tr>";
                }

            //INPUT FORM
            int noAdd = i +1;
            if (btnAddSHow==true){    
            result += ""
            + "<tr id='addForm'>"
                + "<td>"+noAdd+"</td>"
                + "<td>"
                    + "<input id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_MATERIAL_ID]+"' type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_MATERIAL_ID]+"' value=''>"
                    + "<div class='input-group'>"
                        + "<input style='font-size: 11px;' type='text' name='matCode' id='matCode' value='' class='form-control'>"
                        + "<span class='input-group-btn'>"
                            + "<button class='btn btn-default loadListItem' type='button' style='height: 34px;'>"
                                + "<i class='glyphicon glyphicon-search'></i>"
                            + "</button>"
                        + "</span>"
                    + "</div>"
                + "</td>"
                + "<td>"
                    + "<input style='font-size: 11px;' type='text' size='30' name='matItem' value='' readonly class='form-control' id='txt_materialname'>"
                + "</td>";
                if(bEnableExpiredDate){
                    result += "<td><input style='font-size: 11px;' type='text' name='"+frmMatReceive.fieldNames[frmMatReceive.FRM_FIELD_EXPIRED_DATE]+"' value='' class='form-control' id='"+frmMatReceive.fieldNames[frmMatReceive.FRM_FIELD_EXPIRED_DATE]+"'></td>";
                }
            result += ""
                + "<td>"
                    + "<input type='hidden' name='hidden_qty_input' id='hidden_qty_input' value='0'>"
                    + "<input style='font-size: 11px;' type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY_INPUT] +"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY_INPUT] +"' value='' class='form-control'> "
                + "</td>"
                + "<td>"
                    + ""+ControlCombo.draw(frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI], null, ""+0, index_value, index_key,"","form-control")+""
                + "</td>"
                + "<td>"
                    + "<input style='font-size: 11px;' type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_UNIT_ID]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_UNIT_ID]+"' value=''>"
                    + "<input style='font-size: 11px;' type='text' id='matUnit' name='matUnit' value='' class='hiddenText form-control' readOnly>"
                + "</td>";
            if(privShowQtyPrice){
                result += ""
                + "<td>"
                    + "<input style='font-size: 11px;' type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_CURRENCY_ID]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_CURRENCY_ID]+"' value='"+rec.getCurrencyId()+"'>"
                    + "<input style='font-size: 11px;' type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_COST]+"'  name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_COST]+"' value='' class='form-control fieldCost'>"
                + "</td>"
                + "<td>"                
                    + "<input style='font-size: 11px;' type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT]+"'  name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT]+"' value='' class='form-control fieldCost'>"
                + "</td>"
                + "<td>"                
                    + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT2]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT2]+"' value='' class='form-control fieldCost' style='text-align:right;font-size: 11px;'>"
                + "</td>"
                + "<td>"                
                    + "<input  type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]+"' value='' class='form-control fieldCost' style='text-align:right; font-size: 11px;'>"
                + "</td>"
                + "<td>"                
                    + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_FORWARDER_COST]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_FORWARDER_COST]+"' value='' class='form-control fieldCost' style='text-align:right;font-size: 11px;'>"
                + "</td>"
                + "<td>"                
                    + "<input type='text' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY] +"' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY]+"' value='' class='form-control fieldCost' style='text-align:right;font-size: 11px;'>"
                + "</td>"
                + "<td>"                
                    + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_BONUS]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_BONUS] +"' value=''>"
                    + "<input type='checkbox' id='typeBonus'  name='typeBonus' value='1'> Bonus"
                + "</td>"
                 + "<td>"                
                    + "<input style='font-size: 11px;' type='text' id='total_cost' name='total_cost' value='' class='hiddenText form-control' readOnly>"
                    + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_TOTAL]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_TOTAL]+"' value='' class='hiddenText form-control' readOnly>"
                + "</td>";
            }else{
                result +=""
                + "<td>"
                    + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY] +"' value='' class='form-control' style='text-align:right;font-size: 11px;'>"
                    + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_CURRENCY_ID]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_CURRENCY_ID]+"' value=''>"
                    + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_COST]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_COST]+"' value=''>"
                    + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT]+"' value=''>"
                    + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT2]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT2]+"' value=''>"
                    + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]+"' value=''>"
                    + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_FORWARDER_COST]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_FORWARDER_COST]+"' value=''>"
                    + "<input type='hidden' id='total_cost' name='total_cost' value='' readOnly>"
                    + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_TOTAL]+"'  name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_TOTAL]+"' value='' readOnly>"
                + "</td>"
                + "<td>"
                    + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_BONUS]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_BONUS]+"' value='0'>"
                    + "<input type='checkbox' id='typeBonus' name='typeBonus' value='1'> Bonus"
                + "</td>"
                + "";

                }    
            result += ""
                + "<td>"
                    + "<button class='btn btn-primary saveReceiveItem' type='button' style='height: 34px;'>"
                    + "<i class='glyphicon glyphicon-floppy-disk'></i> "               
                + "</button>"
                + "</td>"  ;              
                //END OF INPUT FORM
            }
            result +=""
                + "</tbody>"
            + "</table>";
        }else{
            result += getListReceiveItemByPO(request);
        }
 
        
        
        return result;
    }
    
    private String getListReceiveItemBonus(HttpServletRequest request){
        String result ="";
        MatReceive rec = new MatReceive();
        Vector listMatReceiveItem = new Vector();
        int collspan =0;
        int start=0;
        int i = 0;
        
        //REQUEST FORM
        long oidReceiveMaterial = FRMQueryString.requestLong(request, "oidReceiveMaterial");
        long oidCurrencyRec = FRMQueryString.requestLong(request, "oidCurrencyRec");
        boolean privManageData = FRMQueryString.requestBoolean(request, "privManageData");
        boolean privShowQtyPrice = FRMQueryString.requestBoolean(request, "privShowQtyPrice");
        int receiveStatus = FRMQueryString.requestInt(request, "receiveStatus");
        boolean bEnableExpiredDate = FRMQueryString.requestBoolean(request, "bEnableExpiredDate");
        int language = FRMQueryString.requestInt(request, "lang");
        
        try {
            rec = PstMatReceive.fetchExc(oidReceiveMaterial);
        } catch (Exception e) {
        }
        
        if (rec.getPurchaseOrderId()==0){
            String whereClauseBonusItem = ""
                + " "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial
                + " AND "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS]+"=1";
            String orderClauseItem = ""
                + " RMI."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]+"";

            Vector listMatReceiveBonusItem = new Vector();

            listMatReceiveBonusItem = PstMatReceiveItem.listVectorRecItemComplete(0,0,whereClauseBonusItem, orderClauseItem);


            //BUTTON SHOW CONDITION

            Vector listError = getListErrorVect(oidReceiveMaterial);

            result +=""
            + "<table class='table table-hover table-striped table-bordered' width='100%'>"
                + "<thead style='font-size:13px;'>"
                    + "<tr>"
                        + "<th style='width:1%'>"+textListOrderItem[language][0]+"</th>"  
                        + "<th style='width:10%'>"+textListOrderItem[language][1]+"</th>" 
                        + "<th style='width:10%'>"+textListOrderItem[language][2]+"</th>" ;
                        if(bEnableExpiredDate){
                            result += "<th style='width:5%'>"+textListOrderItem[language][3]+"</th>";
                            collspan = collspan + 4;
                        }else{
                            collspan = collspan + 3;
                        }
            result +=""
                        + "<th style='width:5%'>"+textListOrderItem[language][14]+"</th>" 
                        + "<th style='width:8%'>"+textListOrderItem[language][15]+"</th>" 
                        + "<th style='width:7%'>"+textListOrderItem[language][4]+"</th>" ;
                        collspan = collspan + 3;
                        if(privShowQtyPrice){
                            result += "<th style='width:10%'>"+textListOrderItem[language][5]+"</th>";
                            result += "<th style='width:5%'>"+textListOrderItem[language][11]+"</th>";
                            result += "<th style='width:5%'>"+textListOrderItem[language][12]+"</th>";
                            result += "<th style='width:8%'>"+textListOrderItem[language][13]+"</th>";
                            result += "<th style='width:8%'>"+textListOrderItem[language][6]+"</th>";
                            result += "<th style='width:5%'>"+textListOrderItem[language][8]+"</th>";
                            result += "<th style='width:5%'>"+textListOrderItem[language][17]+"</th>"; 
                            result += "<th style='width:10%'>"+textListOrderItem[language][9]+"</th>"; 
                            collspan = collspan + 8;
                        }else{
                            result += ""
                                + "<th style='width:5%'>"+textListOrderItem[language][8]+"</th>"
                                + "<th style='width:5%'>"+textListOrderItem[language][17]+"</th>";
                            collspan = collspan + 1;
                        }
            result +=""
                        + "<th>"+textListOrderItem[language][16]+"</th>"            
                    + "</tr>"
                + "</thead>"
                + "<tbody>";
                if (listMatReceiveBonusItem.size()>0){
                    for(i=0; i<listMatReceiveBonusItem.size(); i++){
                        Vector temp = (Vector)listMatReceiveBonusItem.get(i);
                        MatReceiveItem recItem = (MatReceiveItem)temp.get(0);
                        Material mat = (Material)temp.get(1);
                        Unit unit = (Unit)temp.get(2);

                        Unit untiKonv = new Unit();
                        try{
                            untiKonv = PstUnit.fetchExc(recItem.getUnitKonversi());
                        }catch(Exception ex){

                        }

                        start = start + 1;
                        double totalForwarderCost = recItem.getForwarderCost() * recItem.getQty();
                        result += "<tr id='receiveBonus-"+i+"'>";
                        result += "<td>"+start+"</td>";
                        result += "<td>"+mat.getSku()+"</td>";
                        if(privShowQtyPrice){
                            if(mat.getCurrBuyPrice()<recItem.getCost()){
                                result += "<td style='cursor:pointer;' class='editItems' data-name='"+mat.getName()+"' data-code='"+mat.getSku()+"' data-oid='"+recItem.getMaterialId()+"'>"+mat.getName()+" &nbsp;<img src='../../../images/DOTreddotANI.gif'><font color='#FF0000'>[Edit]</font></td>";
                            }else{
                                result += "<td style='cursor:pointer;' class='editItems' data-name='"+mat.getName()+"' data-code='"+mat.getSku()+"' data-oid='"+recItem.getMaterialId()+"'>"+mat.getName()+" &bspb; [Edit]</td>";
                            }
                        }else{
                            result += "<td>"+mat.getName()+"</td>";
                        }

                        if(bEnableExpiredDate){
                            result += "<td>"+Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy")+"</td>";
                        }
                        result += "<td>"+recItem.getQtyEntry()+"</td>";
                        result += "<td>"+untiKonv.getCode()+"</td>";
                        result += "<td>"+unit.getCode()+"</td>";
                        if(privShowQtyPrice){
                            result += "<td>"+FRMHandler.userFormatStringDecimal(recItem.getCost())+"</td>";
                            result += "<td>"+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"</td>";
                            result += "<td>"+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"</td>";
                            result += "<td>"+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"</td>";
                            result += "<td>"+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"</td>";
                        }
                        if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){   
                            String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID]+"="+recItem.getOID();
                            int cnt = PstReceiveStockCode.getCount(where);
                            double recQtyPerBuyUnit = recItem.getQty();
                            double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(mat.getBuyUnitId(), mat.getDefaultStockUnitId());
                            double recQty = recQtyPerBuyUnit * qtyPerSellingUnit;
                            double max = recQty;

                            result += "<td>&nbsp;</td>";               
                            result += "<td>"+String.valueOf(recItem.getOID())+""+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</tr>";
                        }else{
                            result +="<td>"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</td>";
                            result +="<td>&nbsp;</td>";
                        }

                        if(privShowQtyPrice){
                            result += "<td>"+FRMHandler.userFormatStringDecimal(recItem.getTotal()+totalForwarderCost)+"</td>";
                        }
                        //Action
                        result += ""
                            + "<td>";
                                //BUTTON EDIT
                                if((privManageData || privShowQtyPrice)&&(receiveStatus==I_DocStatus.DOCUMENT_STATUS_DRAFT)){
                                    result += ""
                                        + "<button id='editBonus-"+i+"' data-recitem='"+recItem.getOID()+"' class='btn btn-warning editItem bonus' type='button' style='height: 34px;'>"
                                            + "<i class='glyphicon glyphicon-pencil'></i> [e]"
                                        + "</button>";
                                    result +=""
                                        + "<button id='delBonus-"+i+"' data-recitem='"+recItem.getOID()+"' class='btn btn-danger deleteItem' type='button' style='height: 34px;'>"
                                            + "<i class='glyphicon glyphicon-remove'></i> [d]"
                                        + "</button>";
                                }
                                //BUTTON DELETE
                        result +=""   
                            + "</td>";
                        result += "</tr>";
                    }
                }else{
                    collspan = collspan + 1;
                    result += ""
                        + "<tr>"
                            + "<td colspan='"+collspan+"'><center>No Data</center></td>"
                        + "</tr>";
                }


            result += ""
                    + ""
                + "</tbody>"
            + "</table>";
        }else{
            result += getListReceiveItemByPOBonus(request);
        }
        
        
        
        
        return result ;
    }
    
    private String getListStatus(HttpServletRequest request){
        String result ="";
        Vector obj_status = new Vector(1,1);
        Vector val_status = new Vector(1,1);
        Vector key_status = new Vector(1,1);
        MatReceive rec = new MatReceive();
                
        String typeOfBusiness = FRMQueryString.requestString(request, "typeOfBusiness");
        long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");
        long userId = FRMQueryString.requestLong(request, "userId");
        int listErrSize = FRMQueryString.requestInt(request, "listErrSize");
        boolean privApprovalApprove = FRMQueryString.requestBoolean(request, "privApprovalApprove");
        boolean privApprovalFinal = FRMQueryString.requestBoolean(request, "privApprovalFinal");
        
        String whereClauseItem = ""
            + " "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial+""
            + " AND "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS]+"=0";
        String orderClauseItem = ""
            + " RMI."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]+"";
        
        Vector listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(0,0,whereClauseItem, orderClauseItem);
        
        try {
            rec = PstMatReceive.fetchExc(oidReceiveMaterial);
        } catch (Exception e) {
        }
        
        if(typeOfBusiness.equals("3")){
            if(rec.getReceiveStatus()!=I_DocStatus.DOCUMENT_STATUS_APPROVED) {
                val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
                if(listMatReceiveItem.size()>0){
                    val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                }
            } 
        }else{
            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
            key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
            if(listMatReceiveItem.size()>0){
                val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
            }
        }
        
        boolean locationAssign=false;
        locationAssign  = PstDataCustom.checkDataCustom(userId, "user_location_map",rec.getLocationId());
        if(listMatReceiveItem.size()>0 && listErrSize==0 && privApprovalApprove==true){
            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_APPROVED));
            key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_APPROVED]);  
        }
        if(listMatReceiveItem.size()>0 && listErrSize==0 && privApprovalFinal==true && locationAssign==true){
            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
            key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
        }
        String select_status = ""+rec.getReceiveStatus();
        if(rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
            result += "<input type='text' readonly='readonly' class='form-control' value='"+I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]+"'>";
        }else if(rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED){
            result += "<input type='text' readonly='readonly' class='form-control' value='"+I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]+"'>";
        }else{
            result +=""+ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_STATUS],null,select_status,val_status,key_status,"","form-control");
        }
        
        return result;
    }
    
    private String getListError(HttpServletRequest request){
        String result ="";
        Vector listError = new Vector(1,1);
        
        long oidReceiveMaterial = FRMQueryString.requestLong(request, "oidReceiveMaterial");
        
        String whereClauseItem = ""
            + " "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial+""
            + " AND "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS]+"=0";
        String orderClauseItem = ""
            + " RMI."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]+"";
        
        Vector listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(0,0,whereClauseItem, orderClauseItem);
        
        for (int i =0; i<listMatReceiveItem.size();i++){
            Vector temp = (Vector)listMatReceiveItem.get(i);
            MatReceiveItem recItem = (MatReceiveItem)temp.get(0);
            Material mat = (Material)temp.get(1);
            Unit unit = (Unit)temp.get(2);
            
            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID]+"="+recItem.getOID();
                int cnt = PstReceiveStockCode.getCount(where);
                
                double recQtyPerBuyUnit = recItem.getQty();
                double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(mat.getBuyUnitId(), mat.getDefaultStockUnitId());
                double recQty = recQtyPerBuyUnit * qtyPerSellingUnit;
                double max = recQty;
                if(cnt<max){
                    if(listError.size()==0){
                        listError.add("Silahkan cek :");
                    }
                    listError.add(""+listError.size()+". Jumlah serial kode stok "+mat.getName()+" tidak sama dengan qty terima");
                }
            }
        }
        result = String.valueOf(listError.size());
        return result ;
    }
    
    private Vector getListErrorVect(long oidReceiveMaterial){
        Vector listError = new Vector(1,1);
               
        String whereClauseItem = ""
            + " "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial+""
            + " AND "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS]+"=0";
        String orderClauseItem = ""
            + " RMI."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]+"";
        
        Vector listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(0,0,whereClauseItem, orderClauseItem);
        
        for (int i =0; i<listMatReceiveItem.size();i++){
            Vector temp = (Vector)listMatReceiveItem.get(i);
            MatReceiveItem recItem = (MatReceiveItem)temp.get(0);
            Material mat = (Material)temp.get(1);
            Unit unit = (Unit)temp.get(2);
            
            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID]+"="+recItem.getOID();
                int cnt = PstReceiveStockCode.getCount(where);
                
                double recQtyPerBuyUnit = recItem.getQty();
                double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(mat.getBuyUnitId(), mat.getDefaultStockUnitId());
                double recQty = recQtyPerBuyUnit * qtyPerSellingUnit;
                double max = recQty;
                if(cnt<max){
                    if(listError.size()==0){
                        listError.add("Silahkan cek :");
                    }
                    listError.add(""+listError.size()+". Jumlah serial kode stok "+mat.getName()+" tidak sama dengan qty terima");
                }
            }
        }
        return listError;
    }
    
    private String getListCurrency(HttpServletRequest request){
        String result =""; 
        long oidCurrencyx=0;
        double resultKonversi = 0.0;
        MatReceive rec = new MatReceive();
        String enableInput="";
        
        long oidReceiveMaterial = FRMQueryString.requestLong(request, "oidReceiveMaterial");
        int lang = FRMQueryString.requestInt(request, "lang");

        String whereClauseItem = ""+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial+
            " AND "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS]+"=0";
        int vectSizeItem = PstMatReceiveItem.getCount(whereClauseItem);
        
        try {
            rec = PstMatReceive.fetchExc(oidReceiveMaterial);
        } catch (Exception e) {
        }
        
        if(rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT){
            enableInput="readonly";
        }

        Vector listCurr = PstCurrencyType.list(0,0,PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE,"");
        Vector vectCurrVal = new Vector(1,1);
        Vector vectCurrKey = new Vector(1,1);
        for(int i=0; i<listCurr.size(); i++){
            CurrencyType currencyType = (CurrencyType)listCurr.get(i);
            if(i==0){
                    oidCurrencyx=currencyType.getOID();
                }
            vectCurrKey.add(currencyType.getCode());
            vectCurrVal.add(""+currencyType.getOID());
        }
        result +=""
        + "<div class='col-md-3'>";
        if((vectSizeItem == 0) || (rec.getCurrencyId()==0) ) {
            resultKonversi = PstDailyRate.getCurrentDailyRateSales(oidCurrencyx);
            result += ""+ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID],"form-control clickCurrency", null, ""+rec.getCurrencyId(), vectCurrVal, vectCurrKey, "");
        }
        else {
           result += ""+ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID]+"_sel","form-control ", null, ""+rec.getCurrencyId(), vectCurrVal, vectCurrKey, "disabled");
           result += "<input type='hidden' name='"+FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID]+"' value='"+rec.getCurrencyId()+"'>";
        }
        result += ""
        + "</div>"
        + "<div class='col-md-1'>"
            + "<label>"+textListOrderHeader[lang][16]+"</label>"
        + "</div>"
        + "<div class='col-md-5'>";
            double valueTemp = 0;
            if (rec.getTransRate()!=0){
                valueTemp = rec.getTransRate();
            }else{
                valueTemp = resultKonversi;
            }
            
        result+=""
            + "<input id='transRate' name='"+FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TRANS_RATE]+"' type='text' class='form-control' value='"+valueTemp+"' "+enableInput+">"
        + "</div>";
        return result;
    }
    
    private String getSaveButton(HttpServletRequest request){
        String result ="";
        boolean showButton = true; 
        MatReceive rec = new MatReceive();
        
        boolean privAdd= FRMQueryString.requestBoolean(request, "privAdd");
        boolean privUpdate = FRMQueryString.requestBoolean(request, "privUpdate");
        boolean privApprovalFinal = FRMQueryString.requestBoolean(request, "privApprovalFinal");
        boolean privApprovalApprove = FRMQueryString.requestBoolean(request, "privApprovalApprove");
        String typeOfBusiness = FRMQueryString.requestString(request, "typeOfBusiness");
        long oidReceiveMaterial = FRMQueryString.requestLong(request, "oidReceiveMaterial");
        int lang = FRMQueryString.requestInt(request, "lang");
        
        try {
            rec = PstMatReceive.fetchExc(oidReceiveMaterial);
        } catch (Exception e) {
        }
        
        if(privAdd==false && privUpdate==false){
            showButton = false;
        }
        
        if(typeOfBusiness.equals("3")){
            if(privApprovalFinal==true && (rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT || rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED)){
                showButton = false;
            }
            if(privApprovalApprove==true && rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_APPROVED && privApprovalFinal==false){
                showButton = false;
            }
            if(rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL || rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
                showButton = false;
            }
        }
        
        if (showButton==true){
            result += ""
            + "<button style='width:49%' class='btn btn-primary' style='padding: 5px; width: 75px;' type='button' id='saveButton'>"
                + ""+textListButton[lang][0]+""
            + "</button>";
        }
        
        return result;
    }
    
    private String getDeleteButton(HttpServletRequest request){
        String result ="";
        boolean showButton = true; 
        MatReceive rec = new MatReceive();
        
        boolean privDelete= FRMQueryString.requestBoolean(request, "privDelete");
        boolean privManageData = FRMQueryString.requestBoolean(request, "privManageData");    
        boolean privApprovalFinal = FRMQueryString.requestBoolean(request, "privApprovalFinal");
        String typeOfBusiness = FRMQueryString.requestString(request, "typeOfBusiness");
        long oidReceiveMaterial = FRMQueryString.requestLong(request, "oidReceiveMaterial");
        int lang = FRMQueryString.requestInt(request, "lang");
        
        try {
            rec = PstMatReceive.fetchExc(oidReceiveMaterial);
        } catch (Exception e) {
        }
        
        if(privDelete && privManageData){
            showButton = true;
        }
        
        if(typeOfBusiness.equals("3")){
            if(privApprovalFinal==true && (rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT || rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED)){
               showButton = false;
            }           
        }
        
        if (showButton==true){
            result += ""
            + "<button style='width:49%' class='btn btn-danger' style='padding: 5px; width: 75px;' type='button' id='deleteButton'>"
                + ""+textListButton[lang][2]+""
            + "</button>";
        }
        
        return result;
    }
    
    private String saveMasterReceiving(HttpServletRequest request){
        String result ="";
        long oidOpnameNew=0;
        
        int iCommand= FRMQueryString.requestInt(request, "command");
        long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");
        long userId = FRMQueryString.requestLong(request, "userId");
        String userName = FRMQueryString.requestString(request, "userName");
        
        CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
        int iErrCode = ctrlMatReceive.action(iCommand , oidReceiveMaterial, userName, userId);
        
        if (iErrCode==0){
            //MatStockOpname so = ctrlMatStockOpname.getMatStockOpname();
            oidOpnameNew=ctrlMatReceive.getNewOid();
        }
                
        result += String.valueOf(oidOpnameNew);
        
        
        return result;
    }
    
    private String getCodeReceive(HttpServletRequest request){
        String result ="";
        MatReceive rec = new MatReceive();
        
        long oidReceiveMaterial= FRMQueryString.requestLong(request, "oidReceiveMaterial");
        try {
            rec = PstMatReceive.fetchExc(oidReceiveMaterial);
        } catch (Exception e) {
        }
        
        if (rec.getOID()!=0){
            result = ""+ rec.getRecCode();
        }else{
            result = "&nbsp;";
        }
        
        return result;
    }
    
    private String getListItemModal(HttpServletRequest request){
        String result ="";
        MatReceive rec = new MatReceive();
        
        long oidReceive = FRMQueryString.requestLong(request, "oidReceive");
        int check = FRMQueryString.requestInt(request, "check");
        String sku = FRMQueryString.requestString(request, "sku");
        String materialName = FRMQueryString.requestString(request, "materialName");
        long matGroupId = FRMQueryString.requestLong(request, "matGroupId");
        int lang = FRMQueryString.requestInt(request, "lang");       
        
        try{
            rec = PstMatReceive.fetchExc(oidReceive);
        }catch(Exception ex){
        }
        
        long currencyId = rec.getCurrencyId();
        double transRate = rec.getTransRate();
        
        String orderBy = "MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
        Material objMaterial = new Material();
        objMaterial.setCategoryId(matGroupId);
        objMaterial.setSku(sku);
        objMaterial.setBarCode(sku);
        objMaterial.setName(materialName);
        objMaterial.setDefaultCostCurrencyId(currencyId);

        SrcMaterial objSrcMaterial = new SrcMaterial();
        objSrcMaterial.setLocationId(rec.getLocationId());
        objSrcMaterial.setCategoryId(matGroupId);
        objSrcMaterial.setMatcode(sku);
        objSrcMaterial.setMatname(materialName);
        
        Vector vect = new Vector();
        if(check==1){
            vect = PstMaterial.getListMaterialItem(0,objMaterial,0,10, orderBy);
        }else{
            vect = SessDoSearch.listMaterialStockCurrPeriodAll(objSrcMaterial, 0, 10, "");
        }
        
        if (check==1){
            result += drawListAllMaterial(lang,vect,true,1);
        }else{
            result += drawListMaterial(currencyId, transRate, lang, vect, 0);
        }
        return result;
    }
    
    private String getItemBySkuOrName(HttpServletRequest request){
        String result = "";
        MatReceive rec = new MatReceive();
        int check=0;
        
        long oidReceive = FRMQueryString.requestLong(request, "oidReceive");
        String search = FRMQueryString.requestString(request, "search");
        
        try{
            rec = PstMatReceive.fetchExc(oidReceive);
        }catch(Exception ex){
        }
        
        long currencyId = rec.getCurrencyId();
        double transRate = rec.getTransRate();
        
        String orderBy = "MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
        
        SrcMaterial objSrcMaterial = new SrcMaterial();
        objSrcMaterial.setLocationId(rec.getLocationId());
        objSrcMaterial.setMatcode(search);
        objSrcMaterial.setMatname(search);
        
        Vector vect = new Vector();     
        vect = SessDoSearch.listMaterialStockCurrPeriodAll2(objSrcMaterial, 0, 1, "");
        
        if (vect.size()>0){
            Vector vt = (Vector)vect.get(0);
            Material material = (Material)vt.get(0);            
            MaterialStock materialStock = (MaterialStock)vt.get(1);
            
            Unit unit = new Unit();
            try {
                unit = PstUnit.fetchExc(material.getDefaultStockUnitId());
            }
            catch(Exception e) {
                System.out.println("Exc when PstUnit.fetchExc() : " + e.toString());
            }
            
            result += ""+material.getOID()+"=="+material.getSku()+"=="+material.getName()+"=="+unit.getCode()+"=="+material.getDefaultCostCurrencyId()+"=="+unit.getOID()+"";
        }
        
        
        return result;
    }
    
    private String drawListAllMaterial(int lang,Vector objectClass,boolean sts, double curr){
        String result ="";
        
        result += ""
        + "<table class='table table-hover table-striped table-bordered'>"
            + "<thead style='font-size: 13px;'>"
                + "<tr>"
                    + "<th>"+textListMaterialHeader[lang][0]+"</th>"
                    + "<th>"+textListMaterialHeader[lang][1]+"</th>"
                    + "<th>"+textListMaterialHeader[lang][2]+"</th>"
                    + "<th>"+textListMaterialHeader[lang][3]+"</th>"
                + "</tr"
            + "</thead>"
            + "<tbody>";
            for(int i=0; i<objectClass.size(); i++){
                
                Vector vt = (Vector)objectClass.get(i);
                Material material = (Material)vt.get(0);
                Category categ = (Category)vt.get(1);
                Unit unit = (Unit)vt.get(2);
                MatCurrency matCurr = (MatCurrency)vt.get(3);
                MatVendorPrice matVendorPrice = (MatVendorPrice)vt.get(4);
                int start = i + 1;
                Vector rowx = new Vector();
                double exchangeRateLastReceive=1;
                String orderLastReceiveBy = PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]+" DESC ";
                Vector listLastReceive = PstMatReceiveItem.listLastReceive(material.getOID(),0,1,"",orderLastReceiveBy);
                for(int r=0; r<listLastReceive.size(); r++){
                    MatReceiveItem matreceiveitem = (MatReceiveItem)listLastReceive.get(r);//new MatReceiveItem();
                    if(curr==1){
                        if(matreceiveitem.getExchangeRate()==0){
                            exchangeRateLastReceive=1;
                        }else{
                            exchangeRateLastReceive=matreceiveitem.getExchangeRate();
                        }   
                    }  
                }
                result += ""
                    + " <tr class='selectModalItems1' style='cursor:pointer;' data-oid='"+material.getOID()+"' data-sku='"+material.getSku()+"' data-materialname='"+material.getName()+"' "
                    + " data-unitcode='"+unit.getCode()+"' data-buyingexchange ='"+FRMHandler.userFormatStringDecimal((matVendorPrice.getOrgBuyingPrice()*exchangeRateLastReceive))+"'"
                    + " data-unitoid='"+unit.getOID()+"' data-defaultcostcurrency='"+material.getDefaultCostCurrencyId()+"' data-getLastDisc='"+FRMHandler.userFormatStringDecimal(matVendorPrice.getLastDiscount())+"'"
                    + " data-currbuyinglastreceive='"+matVendorPrice.getCurrBuyingPrice()*exchangeRateLastReceive+"'>";
                result += "<td>"+start+"</td>";
                result += "<td>"
                    + "<input data-oid='"+material.getOID()+"' data-sku='"+material.getSku()+"' data-materialname='"+material.getName()+"' "
                    + " data-unitcode='"+unit.getCode()+"' data-buyingexchange ='"+FRMHandler.userFormatStringDecimal((matVendorPrice.getOrgBuyingPrice()*exchangeRateLastReceive))+"'"
                    + " data-unitoid='"+unit.getOID()+"' data-defaultcostcurrency='"+material.getDefaultCostCurrencyId()+"' data-getLastDisc='"+FRMHandler.userFormatStringDecimal(matVendorPrice.getLastDiscount())+"'"
                    + " data-currbuyinglastreceive='"+matVendorPrice.getCurrBuyingPrice()*exchangeRateLastReceive+"' type='text' readonly value='"+material.getSku()+"' class='selectModalItems1 form-control'>"
                    + "</td>";
                result += "<td>"+material.getName()+"</td>";
                result += "<td>"+unit.getCode()+"</td>";
                result += "</tr>";
            }
        result +=""
            + "</tbody>"
        + "</table>";
   
        
        return result;
    }
    
    private String drawListMaterial(long currency, double transRate, int language,Vector objectClass,int start){
        String result ="";
        
        result +=""
        + "<table style='font-size: 13px;' class='table table-hover table-striped table-bordered'>"
            + "<thead>"
                + "<tr>"
                    + "<th>"+textListMaterialHeader[language][0]+"</th>"
                    + "<th>"+textListMaterialHeader[language][2]+"</th>"
                    + "<th>"+textListMaterialHeader[language][3]+"</th>"
                    + "<th>"+textListMaterialHeader[language][4]+"</th>"
                    + "<th>"+textListMaterialHeader[language][5]+"</th>"
                    + "<th>"+textListMaterialHeader[language][6]+"</th>"
                + "</tr>"
            + "</thead>"
            + "<tbody>";
        if(start<0) start = 0;
        Hashtable objActiveStandardRate = PstStandartRate.getActiveStandardRate();
        for(int i=0; i<objectClass.size(); i++) {
            Vector vt = (Vector)objectClass.get(i);
            Material material = (Material)vt.get(0);            
            MaterialStock materialStock = (MaterialStock)vt.get(1);
            
            try {
                material = PstMaterial.fetchExc(materialStock.getMaterialUnitId());
            }
            catch(Exception e) {
                System.out.println("Exc when PstMaterial.fetchExc() : " + e.toString());
            }
            Unit unit = new Unit();
            try {
                unit = PstUnit.fetchExc(material.getDefaultStockUnitId());
            }
            catch(Exception e) {
                System.out.println("Exc when PstUnit.fetchExc() : " + e.toString());
            }
                        
            start = start + 1;
            double standardRate = 0;
            try{standardRate = Double.parseDouble((String)objActiveStandardRate.get(""+material.getDefaultCostCurrencyId()));}catch(Exception exc){;}            
            double defaultCost = 0; //mendapatkan besaran default cost dalam mata uang transaksi
            //try{defaultCost = (material.getDefaultCost() * standardRate) / transRate;}catch(Exception exc){;}
            try{               
                defaultCost = (material.getCurrBuyPrice() * standardRate) / transRate;
            }catch(Exception exc){}
            
            String dCost="";
            if (defaultCost>0){
                dCost = FRMHandler.userFormatStringDecimal(defaultCost);
            }else{
                dCost="";
            }
            
            result += ""
                + " <tr class='selectModalItems2' style='cursor:pointer;' data-oid='"+material.getOID()+"'"
                + " data-sku='"+material.getSku()+"' data-materialname='"+material.getName()+"' "
                + " data-unitcode='"+unit.getCode()+"' data-cost ='"+dCost+"'"
                + " data-unitoid='"+unit.getOID()+"' data-defaultcurrency = '"+material.getDefaultCostCurrencyId()+"'"
                + " data-stokqty='"+materialStock.getQty()+"'>";
            result += "<td>"+start+"</td>";
            result += ""
                + " <td>"
                + " <input type='text' readonly value='"+material.getSku()+"' class='selectModalItems2 form-control'"
                + " data-oid='"+material.getOID()+"'"
                + " data-sku='"+material.getSku()+"' data-materialname='"+material.getName()+"' "
                + " data-unitcode='"+unit.getCode()+"' data-cost ='"+dCost+"'"
                + " data-unitoid='"+unit.getOID()+"' data-defaultcurrency = '"+material.getDefaultCostCurrencyId()+"'"
                + " data-stokqty='"+materialStock.getQty()+"'>"
                + " </td>";
            result += "<td>"+material.getName()+"</td>";
            result += "<td>"+unit.getCode()+"</td>";
            result += "<td>"+FRMHandler.userFormatStringDecimal(defaultCost)+"</td>";
            result += "<td>"+FRMHandler.userFormatStringDecimal(materialStock.getQty())+"</td>";   
            result += "</tr>";
        }
        result += ""
            + "</tbody>"           
        + "</table>";
                
        return result;
    }
    
    private String saveReceiveItem(HttpServletRequest request){
        String result ="";
        int iError = 0;
                
        long oidReceive = FRMQueryString.requestLong(request, "oidReceive");
        long oidReceiveMatItem = 0;
        String userName = FRMQueryString.requestString(request, "userName");
        long userId = FRMQueryString.requestLong(request, "userId");
        int command = FRMQueryString.requestInt(request, "command");
        
        CtrlMatReceiveItem ctrlMatReceiveItem = new CtrlMatReceiveItem(request);
        iError = ctrlMatReceiveItem.action2(command,oidReceiveMatItem,oidReceive,userName, userId,request);
        
        result = String.valueOf(iError);
        
        return result;
    }
    
    private String deleteReceiveItem(HttpServletRequest request){
        String result ="";
        int iError = 0;
        
        long oidReceiveMaterialItem = FRMQueryString.requestLong(request, "oidReceiveMaterialItem");
        long oidReceiveMaterial = FRMQueryString.requestLong(request, "oidReceiveMaterial");
        int command = FRMQueryString.requestInt(request, "command");
        String userName = FRMQueryString.requestString(request, "userName");
        long userId = FRMQueryString.requestLong(request, "userId");
        
        CtrlMatReceiveItem ctrlMatReceiveItem = new CtrlMatReceiveItem(request);
        iError = ctrlMatReceiveItem.action(command,oidReceiveMaterialItem,oidReceiveMaterial,userName, userId);
        
        result = String.valueOf(iError);
        
        return result;
    }
    
    private String showEditControl(HttpServletRequest request){
        String result = "";
        int start = 0;
        String whereUnit = "";
        FrmMatReceiveItem frmMatReceiveItem = new FrmMatReceiveItem();
        String addClass = "non";
        String checked = "";

        //REQUEST FORM
        long oidReceiveMaterialItem = FRMQueryString.requestLong(request, "oidReceiveMaterialItem");
        long oidReceiveMaterial = FRMQueryString.requestLong(request, "oidReceiveMaterial");
        int orderData = FRMQueryString.requestInt(request, "orderData");
        boolean privManageData = FRMQueryString.requestBoolean(request, "privManageData");
        boolean privShowQtyPrice = FRMQueryString.requestBoolean(request, "privShowQtyPrice");
        int receiveStatus = FRMQueryString.requestInt(request, "receiveStatus");
        boolean bEnableExpiredDate = FRMQueryString.requestBoolean(request, "bEnableExpiredDate");
        int language = FRMQueryString.requestInt(request, "lang");
        String readOnlyQty = FRMQueryString.requestString(request, "readOnlyQty");
        int bonus = FRMQueryString.requestInt(request, "bonus");
        
        if (bonus==1){
            addClass = "bonus";
        }
        
        start = orderData +1;
        MatReceive rec = new MatReceive();
        MatReceiveItem recItem = new MatReceiveItem();
        Material mat = new Material();
        Unit unit = new Unit();
        CurrencyType currencyType = new CurrencyType();
        
        Vector listBuyUnit = PstUnit.list(0,1000,whereUnit,"");
        Vector index_value = new Vector(1,1);
        Vector index_key = new Vector(1,1);
        index_key.add("-");
        index_value.add("0");
        for(int j=0;j<listBuyUnit.size();j++){
            Unit mateUnit = (Unit)listBuyUnit.get(j);
            index_key.add(mateUnit.getCode());
            index_value.add(""+mateUnit.getOID());
        }
        
        try {
            rec = PstMatReceive.fetchExc(oidReceiveMaterial);
            recItem = PstMatReceiveItem.fetchExc(oidReceiveMaterialItem);
            mat = PstMaterial.fetchExc(recItem.getMaterialId());
            unit = PstUnit.fetchExc(recItem.getUnitId());
            currencyType = PstCurrencyType.fetchExc(rec.getCurrencyId());
        } catch (Exception e) {
        }
        
        double totalForwarderCost = recItem.getForwarderCost() * recItem.getQty();
        Unit untiKonv = new Unit();
        try{
            untiKonv = PstUnit.fetchExc(recItem.getUnitKonversi());
        }catch(Exception ex){

        }
        
        if (recItem.getBonus()==1){
            checked="checked";       
        }else{
            checked="";
        }
      
        
        result +=""
        + "<td>"+start+"</td>"
        + "<td>"
            + "<input id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_RECEIVE_MATERIAL_ID]+"' type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_RECEIVE_MATERIAL_ID]+"' value='"+recItem.getOID()+"'>"
            + "<input id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_MATERIAL_ID]+"' type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_MATERIAL_ID]+"' value='"+recItem.getMaterialId()+"'>"
            + "<div class='input-group'>"
                + "<input style='font-size: 11px;' type='text' name='matCode' id='matCode' value='"+mat.getSku()+"' class='form-control'>"
                + "<span class='input-group-btn'>"
                    + "<button class='btn btn-default loadListItem' type='button' style='height: 34px;'>"
                        + "<i class='glyphicon glyphicon-search'></i>"
                    + "</button>"
                + "</span>"
            + "</div>"
        + "</td>";
        if(privShowQtyPrice){
            if(mat.getCurrBuyPrice()<recItem.getCost()){
                result +=""
                + "<td>"
                    + "<input style='font-size: 11px;' type='text' id='txt_materialname' name='matItem' value='"+mat.getName()+"' class='hiddenText form-control' readonly>"
                + "</td>";    
            }else{
                result +=""
                + "<td>"
                    + "<input style='font-size: 11px;' type='text' id='txt_materialname' name='matItem' value='"+mat.getName()+"' class='hiddenText form-control' readonly>"
                + "</td>";
            }
        }else{
           result +=""
            + "<td>"        
                + "<input style='font-size: 11px;' type='text' name='matItem' id='txt_materialname' value='"+mat.getName()+"' class='hiddenText form-control' readonly>"
            + "</td>"; 
        }      
        if(bEnableExpiredDate){
            result += ""
            + "<td>"
                + "<input style='font-size: 11px;' type='text' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_EXPIRED_DATE]+"' value='"+Formater.formatDate(recItem.getExpiredDate(), "MM/dd/yyyy") +"' class='form-control' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_EXPIRED_DATE]+"'>"
            + "</td>"; 
        }
        if(readOnlyQty=="readonly"){
            result +=""
            + "<td>"
                + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY_INPUT]+"'  name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY_INPUT] +"' value='"+recItem.getQtyEntry()+"' class='form-control' "+readOnlyQty+">"          
                + "<input style='font-size: 11px;' type='text' name='ccc' value='"+recItem.getQtyEntry()+"' class='form-control' "+readOnlyQty+" >"
            + "</td>";
        }else{
            result +=""
            + "<td>"
                + "<input style='font-size: 11px;' type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY_INPUT]+"'  name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY_INPUT] +"' value='"+recItem.getQtyEntry()+"' class='form-control fieldCost'"
            + "</td>";                   
        }
        result += ""
        + "<td>"
            + ""+ControlCombo.draw(frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI], null, ""+0, index_value, index_key,"","form-control")+""
        + "</td>"
        + "<td>"
            + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_UNIT_ID]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_UNIT_ID]+"' value='"+recItem.getUnitId()+"'>"
            + "<input style='font-size: 11px;' type='text' id='matUnit'  name='matUnit' value='"+unit.getCode()+"' class='hiddenText form-control' readOnly>"
        + "</td>";
        if(privShowQtyPrice){
            result +=""
            + "<td>"
                + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_CURRENCY_ID]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_CURRENCY_ID]+"' value='"+recItem.getCurrencyId()+"'>"
                + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_COST]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_COST]+"' value='"+FRMHandler.userFormatStringDecimal(recItem.getCost())+"' class='form-control fieldCost' style='text-align:right;font-size: 11px;'>"
            + "</td>"
            + "<td>"
                + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT]+"' value='"+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"' class='form-control fieldCost' style='text-align:right;font-size: 11px;'>"
            + "</td>"
            + "<td>"
                + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT2]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT2]+"' value='"+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"' class='form-control fieldCost' style='text-align:right';font-size: 11px;>"
            + "</td>"
            + "<td>"
                + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]+"' value='"+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"' class='form-control fieldCost' style='text-align:right';font-size: 11px;>"
            + "</td>"    
            + "<td>"
                + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_FORWARDER_COST]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_FORWARDER_COST]+"' value='"+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"' class='form-control fieldCost' style='text-align:right;font-size: 11px;'>"
            + "</td>";                                               
            if(readOnlyQty=="readonly"){
                result += ""
                + "<td>"
                    + "<input style='font-size: 11px;' type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY] +"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY] +"' value='"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"' class='form-control fieldCost' "+readOnlyQty+" >"
                + "</td>";
               
            }else{
                result +=""
                + "<td>"
                    + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY] +"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY] +"' value='"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"' class='form-control fieldCost' style='text-align:right;font-size:11px;'>"
                + "</td>";
            }
            result += ""
            + "<td>"
                + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_BONUS]+"'  name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_BONUS]+"' value='"+recItem.getBonus()+"'> "
                + "<input type='checkbox' id='typeBonus' "+checked+"  name='typeBonus' value='1'> Bonus"
            + "</td>"
            + "<td>"
                + "<input type='text' style='font-size:11px;' id='total_cost' name='total_cost' value='"+FRMHandler.userFormatStringDecimal(recItem.getTotal()+totalForwarderCost)+"' class='hiddenText form-control' readOnly>"
                + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_TOTAL]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_TOTAL]+"' value='"+FRMHandler.userFormatStringDecimal(recItem.getTotal())+"'>"
            + "</td>";  
        }else{
            result += ""
            + "<td>"
                + "<input type='text' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY] +"' value='"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"' class='form-control' style='text-align:right;font-size:11px;'>"
                + "<input type='hidden' name='total_cost' value='"+FRMHandler.userFormatStringDecimal(recItem.getTotal()+totalForwarderCost)+"' class='hiddenText' readOnly>"
                + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_TOTAL]+"' value='"+FRMHandler.userFormatStringDecimal(recItem.getTotal())+"'>"
                + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_CURRENCY_ID]+"' value='"+recItem.getCurrencyId()+">"
                + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_COST]+"' value='"+FRMHandler.userFormatStringDecimal(recItem.getCost())+"'>"
                + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT]+"' value='"+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"' >"
                + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT2]+"' value='"+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"'>"
                + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]+"' value='"+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"'>"
                + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_FORWARDER_COST]+"' value='"+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"'>"
            + "</td>"
            + "<td>"
                + "<input type='hidden'  name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_BONUS]+"' value='0'> "
                + "<input type='checkbox' id='typeBonus'  name='typeBonus' value='1'> Bonus"
            + "</td>";    
        }
        result +=""
        + "<td>"
            + "<button class='btn btn-primary saveEditReceiveItem "+addClass+"' type='button' style='height: 34px;'>"
                + "<i class='glyphicon glyphicon-floppy-disk'></i> "               
            + "</button>"
        + "</td>"  ; 
                
               
                
                     
            

        return result;
    }
    
    private String saveEditReceiveItem(HttpServletRequest request){
        String result ="";

        int iError = 0;
                
        long oidReceive = FRMQueryString.requestLong(request, "oidReceive");
        long oidReceiveMatItem = FRMQueryString.requestLong(request, ""+FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_RECEIVE_MATERIAL_ID]+"");
        String userName = FRMQueryString.requestString(request, "userName");
        long userId = FRMQueryString.requestLong(request, "userId");
        int command = FRMQueryString.requestInt(request, "command");
        
        CtrlMatReceiveItem ctrlMatReceiveItem = new CtrlMatReceiveItem(request);
        iError = ctrlMatReceiveItem.action2(command,oidReceiveMatItem,oidReceive,userName, userId,request);
        
        result = String.valueOf(iError);
                
        return result;
    }
    
    private String getForwaderInfo(HttpServletRequest request){
        String result ="";
        
        long oidReceive = FRMQueryString.requestLong(request, "oidReceive");
        int lang = FRMQueryString.requestInt(request, "lang");
        MatReceive rec = new MatReceive();
        
        String whereClauseItem = ""
            + " "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceive+"";
        String orderClauseItem = ""
            + " RMI."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]+"";
        
        Vector listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(0,0,whereClauseItem, orderClauseItem);
        
        if (listMatReceiveItem.size()>0){
            result +=""
            + "<div class='panel panel-default'>"
                + "<div class='panel-heading'>"+textListForwarderInfo[lang][5]+"</div>"
                + "<div class='panel-body'>";
            
            Vector vctForwarderInfo = new Vector(1,1);
            try {
                if(oidReceive != 0) {
                    vctForwarderInfo = SessForwarderInfo.getObjForwarderInfo(oidReceive);
                    rec = PstMatReceive.fetchExc(oidReceive);
                }
            } catch(Exception e) {
            }
            
            if(vctForwarderInfo.size() > 0) {
                Vector temp = (Vector)vctForwarderInfo.get(0);
                ForwarderInfo forwarderInfo = (ForwarderInfo)temp.get(0);
                ContactList contactList = (ContactList)temp.get(1);
                double totalForwarderCost = SessForwarderInfo.getTotalCost(oidReceive);
                String cntName = contactList.getCompName();
                if(cntName.length()==0){
                    cntName = contactList.getPersonName()+" "+contactList.getPersonLastname();
                }
                
                result += ""
                + "<div class='row'>"
                    + "<div class='col-md-3'><label>"+textListForwarderInfo[lang][0]+"</label></div>"
                    + "<div class='col-md-1'>:</div>"
                    + "<div class='col-md-7'>"+forwarderInfo.getDocNumber()+"</div>"
                + "</div>"
                + "<div class='row' style='margin-top:10px;'>"
                    + "<div class='col-md-3'><label>"+textListForwarderInfo[lang][1]+"</label></div>"
                    + "<div class='col-md-1'>:</div>"
                    + "<div class='col-md-7'>"+cntName+"</div>"
                + "</div>"
                + "<div class='row' style='margin-top:10px;'>"
                    + "<div class='col-md-3'><label>"+textListForwarderInfo[lang][2]+"</label></div>"
                    + "<div class='col-md-1'>:</div>"
                    + "<div class='col-md-7'>"+Formater.formatDate(forwarderInfo.getDocDate(),"dd/MM/yyyy")+"</div>"
                + "</div>"
                + "<div class='row' style='margin-top:10px;'>"
                    + "<div class='col-md-3'><label>"+textListForwarderInfo[lang][3]+"</label></div>"
                    + "<div class='col-md-1'>:</div>"
                    + "<div class='col-md-7'>"+FRMHandler.userFormatStringDecimal(totalForwarderCost)+"</div>"
                + "</div>"
                + "<div class='row' style='margin-top:10px;'>"
                    + "<div class='col-md-3'><label>"+textListForwarderInfo[lang][4]+"</label></div>"
                    + "<div class='col-md-1'>:</div>"
                    + "<div class='col-md-7'>"+forwarderInfo.getNotes()+"</div>"
                + "</div>"
                + "<div class='row' style='margin-top:10px;'>"
                    + "<div class='col-md-6'>"
                        + "<button type='button' id='addForwader' class='btn btn-warning' style='width:100%;'>"
                            + ""+textListForwarderInfo[lang][9]+""
                        + "</button>"
                    + "</div>"
                    + "<div class='col-md-6'>"
                        + "<button type='button' id='deleteForwarder' class='btn btn-danger' style='width:100%;'>"
                            + ""+textListForwarderInfo[lang][10]+""
                        + "</button>"
                    + "</div>"                   
                + "</div>";
                
            }else{
                result +=""
                + "<div class='row'>"
                    + "<div class='col-md-12'><center>"+textListForwarderInfo[lang][6]+"</center></div>"
                + "</div>";
                if(rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {
                    result +=""        
                    + "<div class='row' style='margin-top:5px;'>"
                        + "<div class='col-md-12'>"
                            + "<button type='button' id='addForwader' class='btn btn-primary' style='width:100%;'>"
                            + ""+textListForwarderInfo[lang][8]+""
                            + "</button>"
                        + "</div>"
                    + "</div>";
                }
            }
            
            result +=""
                + "</div>"
            + "</div>";
        }
           
        return result;
    }
    
    private String getForwaderControl(HttpServletRequest request){
        String result ="";
        ForwarderInfo forwarderInfo = new ForwarderInfo();
        ContactList contactList = new ContactList();
        MatReceive rec = new MatReceive();
        
        long oidReceive = FRMQueryString.requestLong(request, "oidReceive");
        int lang = FRMQueryString.requestInt(request, "lang");
        
        Vector vctForwarderInfo = new Vector(1,1);
        try {
            if(oidReceive != 0) {
                vctForwarderInfo = SessForwarderInfo.getObjForwarderInfo(oidReceive);
            }
        } catch(Exception e) {
        }
        
        if(vctForwarderInfo.size() > 0) {
            Vector temp = (Vector)vctForwarderInfo.get(0);
            forwarderInfo = (ForwarderInfo)temp.get(0);
            contactList = (ContactList)temp.get(1);
        }
        
        try{
            rec = PstMatReceive.fetchExc(oidReceive);
        }catch(Exception ex){
        }
        
        Vector val_supplier = new Vector(1,1);
        Vector key_supplier = new Vector(1,1);
        String wh_supp =""
            + " "+PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]
            + " = "+PstContactClass.CONTACT_TYPE_SUPPLIER
            + " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]
            + " != "+PstContactList.DELETE;
        Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);

        for(int d=0; d<vt_supp.size(); d++) {
            ContactList cnt = (ContactList)vt_supp.get(d);
            String cntName = cnt.getCompName();
            if(cntName.length()==0){
                cntName = cnt.getPersonName()+" "+cnt.getPersonLastname();
            }
            val_supplier.add(String.valueOf(cnt.getOID()));
            key_supplier.add(cntName);
        }
        
        double totalForwarderCost = SessForwarderInfo.getTotalCost(oidReceive);
        
        String tgl="";
        Date tglTemp = forwarderInfo.getDocDate();
        if (tglTemp==null){
            tgl = "";
        }else{
            tgl = Formater.formatDate(forwarderInfo.getDocDate(),"dd/MM/yyyy");
        }
       
        
        result +=""
        + "<form name='forwarderForm' id='forwarderForm'>"        
        + "<div class='modal-body'>"
            + "<input type='hidden' value='"+rec.getLocationId()+"' name ='"+FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_FI_LOCATION_ID]+"'>"
            + "<input type='hidden' value='"+forwarderInfo.getOID()+"' name='"+FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_FORWARDER_INFO_ID]+"'>"
            + "<input type='hidden' value='"+oidReceive+"' name='"+FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_RECEIVE_ID]+"' >"        
            + "<input type='hidden' value='"+rec.getRecCode()+"' name='"+FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_DOC_NUMBER]+"' >";
            if (vctForwarderInfo.size()>0){  
                result +=""
                + "<div class='row'>"
                    + "<div class='col-md-3'><label>"+textListForwarderInfo[lang][0]+"</label></div>"
                    + "<div class='col-md-9'><b>"+forwarderInfo.getDocNumber()+"</b></div>"
                + "</div>";
            }
        result +=""
            + "<div class='row' style='margin-top: 8px;'>"
                + "<div class='col-md-3'><label>"+textListForwarderInfo[lang][1]+"</label></div>"
                + "<div class='col-md-9'>"
                    + ControlCombo.draw(FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_CONTACT_ID],null,String.valueOf(forwarderInfo.getContactId()),val_supplier,key_supplier,"","form-control required")
                + "</div>"
            + "</div>"
            + "<div class='row' style='margin-top: 8px;'>"
                + "<div class='col-md-3'><label>"+textListForwarderInfo[lang][2]+"<label></div>"
                + "<div class='col-md-9'>" 
                    + "<div class='input-group'>"
                        + "<input type='text' class='form-control datesPic required' id='dateForwarder' name='"+FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_DOC_DATE]+"' value='"+tgl+"'>"
                        + "<span class='input-group-addon'>"
                            + "<i class='glyphicon glyphicon-calendar'></i>"   
                        + "</span>"
                    + "</div>"
                + "</div>"
            + "</div>"
            + "<div class='row' style='margin-top: 8px;'>"
                + "<div class='col-md-3'><label>"+textListForwarderInfo[lang][3]+"</label></div>"
                + "<div class='col-md-9'><b>"+FRMHandler.userFormatStringDecimal(totalForwarderCost)+"</b></div>"
            + "</div>"
            + "<div class='row' style='margin-top: 8px;'>"
                + "<div class='col-md-3'><label>"+textListForwarderInfo[lang][4]+"</label></div>"
                + "<div class='col-md-9'>"
                    + "<textarea name='"+FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_NOTES]+"' class='form-control' wrap='VIRTUAL'>"+forwarderInfo.getNotes()+"</textarea>"
                + "</div>"
            + "</div>"
        + "</div>"
        + "</form>"        
        + "<div class='modal-footer'>";
            if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)){
                result += ""
                + "<button id='btnSaveForwarder' class='btn btn-primary' type='button' data-dismiss='modal'>"
                    + "<i class='glyphicon glyphicon-ok'></i> "+textListButton[lang][0]+""
                + "</button>";
            }
        result +=""    
            + "<button id='btnCloseForwarder' class='btn btn-danger' type='button' data-dismiss='modal'>"
                + "<i class='glyphicon glyphicon-remove'></i> "+textListButton[lang][1] +"</button>"
        + "</div>";   
        return result;
    }
    
    private String saveForwarder(HttpServletRequest request){
        String result ="";
        int iErrCode=0;
        
        int command= FRMQueryString.requestInt(request, "command");
        long oidForwarderInfo = FRMQueryString.requestInt(request, ""+FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_FORWARDER_INFO_ID]);
        
        CtrlForwarderInfo ctrlForwarderInfo = new CtrlForwarderInfo(request);
        iErrCode = ctrlForwarderInfo.action2(command , oidForwarderInfo,request);
        return result;
    }
    
    private String deleteForwarder(HttpServletRequest request){
        String result ="";
        int iErrCode=0;
        ForwarderInfo forwarderInfo = new ForwarderInfo();      
        
        long oidReceive = FRMQueryString.requestLong(request, "oidReceive");
        int command = FRMQueryString.requestInt(request, "command");
        
        Vector vctForwarderInfo = new Vector(1,1);
        try {
            if(oidReceive != 0) {
                vctForwarderInfo = SessForwarderInfo.getObjForwarderInfo(oidReceive);
            }
        } catch(Exception e) {
        }
        
        if(vctForwarderInfo.size() > 0) {
            Vector temp = (Vector)vctForwarderInfo.get(0);
            forwarderInfo = (ForwarderInfo)temp.get(0);
            
            CtrlForwarderInfo ctrlForwarderInfo = new CtrlForwarderInfo(request);
            iErrCode = ctrlForwarderInfo.action(command , forwarderInfo.getOID());
        }
        
        return result;
    } 
    
    private String getListPayment(HttpServletRequest request){
        String result ="";
        MatReceive rec = new MatReceive();
        
        int lang = FRMQueryString.requestInt(request, "lang");
        long oidMatReceive1 = FRMQueryString.requestLong(request, "oidReceive");
        //long oidInvoiceSelected = FRMQueryString.requestLong(request, "oid_invoice_selected");
        //long oidPaymentSelected = FRMQueryString.requestLong(request, "oid_payment_selected");
        //long oidPaymentDetailSelected = FRMQueryString.requestLong(request, "oid_payment_detail_selected");
        String whereClauseItem = ""
            + " "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidMatReceive1+"";
        String orderClauseItem = ""
            + " RMI."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]+"";
        
        Vector listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(0,0,whereClauseItem, orderClauseItem);

        try {
            rec = PstMatReceive.fetchExc(oidMatReceive1);
        } catch (Exception e) {
        }
        
        long oidCurrency = rec.getCurrencyId();
        String codeRec = rec.getRecCode();
        long oidPurchaseorder2 = rec.getPurchaseOrderId();
        
        String whereClause2 = PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_TYPE_ID]+" = "+ oidCurrency;
        String orderClause2 = PstDailyRate.fieldNames[PstDailyRate.FLD_ROSTER_DATE]+" DESC";
        Vector listDailyRate2 = PstDailyRate.list(0, 0, whereClause2, orderClause2);
        DailyRate dr2 = (DailyRate)listDailyRate2.get(0);
        double dailyRate = dr2.getSellingRate();
        
        Vector apDetail = SessAccPayable.getListApDetail(oidMatReceive1);
        SrcAccPayable srcAccPayable = new SrcAccPayable();
        Vector records = SessAccPayable.getListAP(srcAccPayable, 0, 0,oidMatReceive1);
        
        if (listMatReceiveItem.size()>0){
            if (rec.getTermOfPayment()== 0){       
                result +=""
                + "<div class='panel panel-default'>"
                    + "<div class='panel-heading'>"+textListDetailPayment[lang][1]+"</div>"
                    + "<div class='panel-body'>"
                        + "<form id='frm_recmaterial2' name='frm_recmaterial2'  action=''>"
                            + "<input type='hidden' name='command' id='commands' value=''>" 
                            + "<input type='hidden' id='add_type' name='add_type' value=''>" 
                            + "<input type='hidden' id='hidden_receive_ids' name='hidden_receive_id' value='"+oidMatReceive1+"'>" 
                            + "<input type='hidden' id='oid_invoice_selected' name='oid_invoice_selected' value=''>" 
                            + "<input type='hidden' id='oid_payment_selected' name='oid_payment_selected' value=''>"
                            + "<input type='hidden' id='oid_payment_detail_selected' name='oid_payment_detail_selected' value=''>"
                            + "<input type='hidden' id='oid_currency_type' name='oid_currency_type' value='"+oidCurrency+"'>" 
                            + "<input type='hidden' id='code_receive' name='code_receive' value='"+rec.getRecCode()+"'>" 
                            + "<input type='hidden' id='hidden_purchase_order_id' name='hidden_purchase_order_id' value=''>"
                            + "<input type='hidden' id='approval_command' name='approval_command'>"
                            + "<table style='font-size: 13px;' class='table table-hover table-striped table-bordered'>"
                                + "<thead>"
                                    + "<tr>"
                                        + "<td>"+textListHeaderDetail[lang][0]+"</td>"
                                        + "<td>"+textListHeaderDetail[lang][2]+"</td>"
                                        + "<td>"+textListHeaderDetail[lang][3]+"</td>"
                                        + "<td>"+textListHeaderDetail[lang][4]+"</td>"
                                        + "<td>"+textListHeaderDetail[lang][5]+"</td>"
                                        + "<td>"+textListHeaderDetail[lang][6]+"</td>"
                                    + "</tr>"
                                + "</thead>"
                                + "<tbody>";
                                for(int i=0; i<records.size(); i++) {
                                    Vector temp = (Vector)records.get(i);
                                    long oidInvoice = Long.parseLong((String)temp.get(0));
                                    double total = Double.parseDouble((String)temp.get(5));
                                    double tax = Double.parseDouble((String)temp.get(6));
                                    //double retur = SessMatReturn.getTotalReturnByReceive(oidInvoice);
                                    double retur = SessMatReturn.getTotalReturnByReceive(srcAccPayable, oidInvoice);
                                    double apPayment = (SessAccPayable.getTotalAPPayment(srcAccPayable, oidInvoice))/dailyRate;
                                    double apSaldo = (total + tax) - apPayment - retur;

                                    if(apDetail.size() > 0) { //kondisi untuk menampilkan list detail dari payment
                                        for(int j=0; j<apDetail.size(); j++) {
                                            Vector temp2 = (Vector)apDetail.get(j);
                                            AccPayable accPayable = (AccPayable)temp2.get(0);
                                            PaymentSystem paymentSystem = (PaymentSystem)temp2.get(1);
                                            CurrencyType currType = (CurrencyType)temp2.get(2);
                                            AccPayableDetail accPayableDetail = (AccPayableDetail)temp2.get(3);

                                            long oidPayment = accPayable.getOID();
                                            long oidPaymentDetail = accPayableDetail.getOID();                                     
                                            double amount = (accPayableDetail.getRate()*accPayableDetail.getAmount());

                                            result += ""
                                            + "<tr>"
                                                + " <td class='editPayments' data-oidpayment='"+oidPayment+"' data-oidpaymentdetail='"+oidPaymentDetail+"'"
                                                + " data-oidcurrency='"+oidCurrency+"' data-coderec='"+codeRec+"' "
                                                + " data-oidmatreceive1='"+oidMatReceive1+"' data-oidpurchaseorder2='"+oidPurchaseorder2+"'"
                                                + " style='cursor:pointer; text-decoration: underline;'>"
                                                    + ""+Formater.formatDate(accPayable.getPaymentDate(), "dd-MM-yyyy")+""
                                                + "</td>"
                                                + "<td>"+paymentSystem.getPaymentSystem()+"</td>"
                                                + "<td>"+currType.getCode()+"</td>"
                                                + "<td>"+FRMHandler.userFormatStringDecimal(accPayableDetail.getRate())+"</td>"
                                                + "<td>"+FRMHandler.userFormatStringDecimal(accPayableDetail.getAmount())+"</td>"
                                                + "<td>"+FRMHandler.userFormatStringDecimal(amount)+"</td>"
                                            + "</tr>";
                                        }
                                    }else{
                                        result +=""
                                        + "<tr>"
                                            + "<td colspan='6' style='text-align:center'>Tidak ada list pembayaran</td>"
                                        + "</tr>";
                                    }

                                    result +=""
                                    + "<tr>"
                                        + "<td colspan='5'><strong>"+textListHeader[lang][0]+"</strong></td>"
                                        + "<td><strong>"+FRMHandler.userFormatStringDecimal(apPayment)+"</strong></td>"
                                    + "</tr>"
                                    + "<tr>"
                                        + "<td colspan='5'><strong>"+textListHeader[lang][1]+"</strong></td>"
                                        + "<td>"+FRMHandler.userFormatStringDecimal(apSaldo)+"</td>"
                                    + "</tr>";
                                }

                result += ""                  
                                + "</tbody>"
                            + "</table>"
                            + "<br>"
                            + "<button id='addPayments' type='button' class='btn btn-primary'>"
                                + "<i class='glyphicon glyphicon-plus-sign'></i> "
                                + ""+textListHeader[lang][2]+""
                            + "</button>"
                        + "</form>"
                    + "</div>"
                + "</div>";
            }
        }
        return result;
    }
    
    private String getSummaryReceive(HttpServletRequest request){
        String result="";
        MatReceive rec = new MatReceive();
        String check = "";
        
        int lang = FRMQueryString.requestInt(request, "lang");
        long oidReceive = FRMQueryString.requestLong(request, "oidReceive");
        
        String whereClauseItem = ""
            + " "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceive+"";
       
        Vector listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(0,0,whereClauseItem, "");
        
        if (listMatReceiveItem.size()>0){
            double defaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));
        
            try{
                rec = PstMatReceive.fetchExc(oidReceive);
            }catch(Exception ex){

            }
            
            double totalForwarderCost = SessForwarderInfo.getTotalCost(oidReceive);

            String whereItem = ""+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceive;
            double totalBeli = PstMatReceiveItem.getTotal(whereItem);
            double ppn = rec.getTotalPpn();
            if(ppn == 0){
                ppn  = defaultPpn;
             }
            double totalBeliWithPPN = (totalBeli * (ppn / 100)) + totalBeli;
            double valuePpn = 0.0;
            if(rec.getIncludePpn()== 1){
                valuePpn =totalBeli - (totalBeli /1.1);
            }else {
                valuePpn = totalBeli * (ppn / 100);
            }
            
            if (rec.getIncludePpn()==1){
                check ="checked";
            }else{
                check ="";
            }
            
            result += ""
            + "<div class='panel panel-default'>"
                + "<div class='panel-heading'>"+textListDetailPayment[lang][3]+"</div>"
                + "<div class='panel-body'>"
                    + "<div class='row'>"
                        + "<div class='col-md-8'>"+textListOrderHeader[lang][10]+" "+textListOrderItem[lang][5]+"</div>"
                        + "<div class='col-md-4'>"+FRMHandler.userFormatStringDecimal(totalBeli)+"</div>"
                    + "</div>"
                    + "<div class='row' style='margin-top:10px;'>"                        
                        + "<div class='col-md-8'>"
                            + "<input type='checkbox' id='includePPN' "+check+"> &nbsp;"
                            + ""+textListOrderHeader[lang][12]+"&nbsp;"+textListOrderHeader[lang][7]+"(%) &nbsp;"                         
                            + "<input type='text' style='width:25%;font-size: 11px;'  class='form-control pull-right' id='valuePPN' value='"+FRMHandler.userFormatStringDecimal(ppn)+"'> &nbsp;"
                        + "</div>"                                       
                        + "<div class='col-md-4'>"+FRMHandler.userFormatStringDecimal(valuePpn)+"</div>"
                    + "</div>"
                    + "<div class='row' style='margin-top:10px;'>"
                        + "<div class='col-md-8'>"+textListOrderHeader[lang][10]+"</div>";
                        if (rec.getIncludePpn()==1) {
                            result +=""
                            + "<div class='col-md-4'>"+FRMHandler.userFormatStringDecimal(totalBeli)+"</div>";
                        }else{
                            result +=""
                            + "<div class='col-md-4'>"+FRMHandler.userFormatStringDecimal(totalBeliWithPPN)+"</div>";
                        }
            
            result +=""
                        
                    + "</div>"
                    + "<div class='row' style='margin-top:10px;'>"
                        + "<div class='col-md-8'>"+textListOrderHeader[lang][10]+" "+textListOrderItem[lang][6]+"</div>"
                        + "<div class='col-md-4'>"+FRMHandler.userFormatStringDecimal(totalForwarderCost)+"</div>"
                    + "</div>"
                    + "<div class='row' style='margin-top:10px;'>"
                        + "<div class='col-md-8'>"+textListOrderHeader[lang][11]+"</div>";
                        if (rec.getIncludePpn()==1){
                            result += "<div class='col-md-4'>"+FRMHandler.userFormatStringDecimal(totalBeli+totalForwarderCost)+"</div>";
                        }else{
                            result += "<div class='col-md-4'>"+FRMHandler.userFormatStringDecimal(totalBeliWithPPN+totalForwarderCost)+"</div>";
                        }
            result +=""
                    + "</div>"
                + "</div>"
            + "</div>";
        }
        
        
        
        
        return result;
    }
    
    private String getTotalByOidReceiveMaterial(HttpServletRequest request){
        String result ="";
        long oidReceive = FRMQueryString.requestLong(request, "oidReceive");
        
        String whereClauseItem = ""
            + " "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceive+"";
       
        Vector listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(0,0,whereClauseItem, "");
        
        result = String.valueOf(listMatReceiveItem.size());
        
        return result;
    }
    
    private String deleteReceiving(HttpServletRequest request){
        String result ="";
        
        int iCommand= FRMQueryString.requestInt(request, "command");
        long oidReceiveMaterial = FRMQueryString.requestLong(request, "oidReceive");
        long userId = FRMQueryString.requestLong(request, "userId");
        String userName = FRMQueryString.requestString(request, "userName");
        
        CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
        int iErrCode = ctrlMatReceive.action(iCommand , oidReceiveMaterial, userName, userId);
        
        return result;
    }
    
    private String getListPoBySuplier(HttpServletRequest request){
        String result="";
        String whereClause="";
        int start = 0;
        
        
        long oidVendor = FRMQueryString.requestLong(request, "oidVendor");
        String monthOfPO = FRMQueryString.requestString(request, "monthOfPO");
        String yearOfPO= FRMQueryString.requestString(request, "yearOfPO");
        int lang = FRMQueryString.requestInt(request, "language");
        
        whereClause = ""
            + " "+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_SUPPLIER_ID] + " = " + oidVendor 
            + " AND Month(" + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCH_DATE] + " ) = " + monthOfPO 
            + " AND Year(" + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCH_DATE] + " ) = " + yearOfPO 
            + " AND (" + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS] + " = 2 )";
        
        Vector listPo = PstPurchaseOrder.list(0, 0, whereClause, "");
        
        result +=""
        + "<table style='font-size: 13px;' class='table table-hover table-striped table-bordered'>"
            + "<thead>"
                + "<tr>"
                    + "<td>"+textListPurchaseOrderHeader[lang][0]+"</td>"
                    + "<td>"+textListPurchaseOrderHeader[lang][1]+"</td>"
                    + "<td>"+textListPurchaseOrderHeader[lang][2]+"</td>" 
                    + "<td>"+textListPurchaseOrderHeader[lang][5]+"</td>"   
                    + "<td>"+textListPurchaseOrderHeader[lang][4]+"</td>"
                + "</tr>"
            + "</thead>"
            + "<tbody>";
            if (listPo.size()>0){
                for (int i = 0; i<listPo.size();i++){
                    PurchaseOrder po = (PurchaseOrder)listPo.get(i);
                    start = start + 1;	
                    
                    String str_dt_PurchDate = ""; 
                    try	{
                        Date dt_PurchDate = po.getPurchDate();
                        if(dt_PurchDate==null) {
                            dt_PurchDate = new Date();
                        }	
                        str_dt_PurchDate = Formater.formatDate(dt_PurchDate, "dd-MM-yyyy");
                    }catch(Exception e) {
                        str_dt_PurchDate = "";
                    }
                    
                    CurrencyType currencyType = new CurrencyType();
                    try {
                        currencyType = PstCurrencyType.fetchExc(po.getCurrencyId());
                    }catch(Exception e) {
                    }

                    result +=""
                    + "<tr style='cursor:pointer' class='selectPo' data-pooid='"+po.getOID()+"' data-pocode='"+po.getPoCode()+"'>"
                        + "<td>"+start+"</td>"
                        + "<td>"+po.getPoCode()+"</td>"             
                        + "<td>"+str_dt_PurchDate+"</td>"             
                        + "<td>"+currencyType.getCode()+"</td>"
                        + "<td>"+po.getRemark()+"</td>"
                    + "</tr>";  
                }
                
            }else {
                result += ""
                + "<tr><td colspan='5'><center>No Data</center></td></tr>";
            }
            result += ""
                + "</tbody>"
            + "</table>";
                    
        
        return result;
    }
    
    private String getButtonAddAll(HttpServletRequest request){
        String result="";
        MatReceive rec = new MatReceive();
        
        int lang = FRMQueryString.requestInt(request, "language");
        long oidReceiving = FRMQueryString.requestLong(request, "oidReceiving");
        
        String whereClauseItem = ""
            + " "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiving+"";
        String orderClauseItem = ""
            + " RMI."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID]+"";
        
        Vector listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(0,0,whereClauseItem, orderClauseItem);

        try {
            rec = PstMatReceive.fetchExc(oidReceiving);
        } catch (Exception e) {
        }
        
        if (rec.getPurchaseOrderId()!=0 && listMatReceiveItem.size()==0){
            result += ""
            + "<button type='button' class='btn btn-primary' id='addAllItem'>"
                + "<i class='glyphicon glyphicon-circle-arrow-down'></i> "+textListButton[lang][3]+""
            + "</button>";
        }
        
        return result;
    }
    
    private String getAddAllItem(HttpServletRequest request){
        String result ="";
        MatReceive rec = new MatReceive();
        int collspan =0;
        int start=0;
        int i=0;
        FrmMatReceive frmMatReceive = new FrmMatReceive();
        FrmMatReceiveItem frmMatReceiveItem = new FrmMatReceiveItem();
        
        long oidReceive = FRMQueryString.requestLong(request, "oidReceive");
        int language = FRMQueryString.requestInt(request, "language");
        boolean privShowQtyPrice = FRMQueryString.requestBoolean(request, "privShowQtyPrice");
        boolean bEnableExpiredDate = FRMQueryString.requestBoolean(request, "bEnableExpiredDate");
        
        try {
            rec = PstMatReceive.fetchExc(oidReceive);
        } catch (Exception e) {
        }
        
        String whereUnit = "";
        Vector listBuyUnit = PstUnit.list(0,1000,whereUnit,"");
        Vector index_value = new Vector(1,1);
        Vector index_key = new Vector(1,1);
        index_key.add("-");
        index_value.add("0");
        for(int j=0;j<listBuyUnit.size();j++){
            Unit mateUnit = (Unit)listBuyUnit.get(j);
            index_key.add(mateUnit.getCode());
            index_value.add(""+mateUnit.getOID());
        }
        
        String whereClause = ""
            + " POI." + PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID] 
            + " = " + rec.getPurchaseOrderId()+ " AND POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_RESIDU_QTY]+" "
            + " < POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_QUANTITY];
        Vector listPo = PstPurchaseOrderItem.listPO(0,0,whereClause); 
        
        result +=""
        + "<table class='table table-hover table-striped table-bordered' width='100%'>"
            + "<thead style='font-size:13px;'>"
                + "<tr>"
                    + "<th style='width:1%'>"+textListOrderItem2[language][0]+"</th>"  
                    + "<th style='width:8%'>"+textListOrderItem2[language][1]+"</th>" 
                    + "<th style='width:8%'>"+textListOrderItem2[language][2]+"</th>" ;
                    if(bEnableExpiredDate){
                        result += "<th style='width:5%'>"+textListOrderItem2[language][3]+"</th>";
                        collspan = collspan + 4;
                    }else{
                        collspan = collspan + 3;
                    }
        result +=""
                    + "<th style='width:5%'>"+textListOrderItem2[language][14]+"</th>" 
                    + "<th style='width:8%'>"+textListOrderItem2[language][15]+"</th>" 
                    + "<th style='width:7%'>"+textListOrderItem[language][4]+"</th>" ;
                    collspan = collspan + 3;
                    if(privShowQtyPrice){
                        result += "<th style='width:5%'>"+textListOrderItem2[language][16]+"</th>";
                        result += "<th style='width:5%'>"+textListOrderItem2[language][5]+"</th>";
                        result += "<th style='width:5%'>"+textListOrderItem2[language][11]+"</th>";
                        result += "<th style='width:5%'>"+textListOrderItem2[language][12]+"</th>";
                        result += "<th style='width:5%'>"+textListOrderItem2[language][13]+"</th>";
                        result += "<th style='width:5%'>"+textListOrderItem2[language][6]+"</th>";                       
                        collspan = collspan + 6;
                    }
        result +=""
                    + "<th>"+textListOrderItem2[language][8]+"</th>"   
                    + "<th>"+textListOrderItem2[language][18]+"</th>" ;
                    collspan = collspan + 3;
                    if(privShowQtyPrice){
                        result += "<th>"+textListOrderItem2[language][9]+"</th>" ;
                        collspan = collspan + 1;
                    }
        result += ""        
                + "</tr>"
            + "</thead>"
            + "<tbody>";
            if (listPo.size()>0){
                for(i=0; i<listPo.size(); i++){

                    Vector temp = (Vector)listPo.get(i);
                    PurchaseOrderItem poItem = (PurchaseOrderItem)temp.get(0);
                    Material mat = (Material)temp.get(1);
                    Unit unit = (Unit)temp.get(2);
                    MatCurrency matCurrency = (MatCurrency)temp.get(3);
                    Vector rowx = new Vector();
                    
                    Unit untiKonv = new Unit();
                    try{
                        untiKonv = PstUnit.fetchExc(poItem.getUnitRequestId());
                    }catch(Exception ex){

                    }                    
                    start = start + 1;
                    double sisa = poItem.getQuantity() - poItem.getResiduQty();
                                       
                    result += "<tr id='receive-"+i+"'>";
                    result += "<td>"+start+"</td>";
                    result += ""
                        + "<td>"
                            + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_MATERIAL_ID]+"-"+i+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_MATERIAL_ID]+"_"+i+"' value='"+mat.getOID()+"'>"
                            + "<input style='font-size: 11px;' type='text' readonly name='matCode' value='"+mat.getSku()+"' class='form-control'>"
                        + "</td>"                 
                        + "<td>"
                            + "<input style='font-size: 11px;' type='text' readonly name='matItem' value='"+mat.getName()+"' class='form-control'>"
                        + "</td>";
                    
                    if(bEnableExpiredDate){
                        result += "<td><input style='font-size: 11px;' type='text' class='form-control datesPic' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_EXPIRED_DATE]+"-"+i+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_EXPIRED_DATE]+"_"+i+"'></td>";
                    }
                    result += ""
                        + "<td>"
                            +"<input style='font-size: 11px;' type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY_INPUT]+"-"+i+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY_INPUT]+"_"+i+"' value='"+poItem.getQtyRequest()+"' class='form-control changeQty'>"
                        + "</td>";                                     
                    result +=""
                        + "<td>"                            
                            + ""+ControlCombo.draw(frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]+"_"+i, null, ""+poItem.getUnitRequestId(), index_value, index_key,"","form-control")+""
                        + "</td>";
                    if(privShowQtyPrice){
                        result += ""
                            + "<td>"
                                + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_UNIT_ID]+"-"+i+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_UNIT_ID]+"_"+i+"' value='"+poItem.getUnitId()+"'>"
                                + "<input style='font-size: 11px;' type='text' name='matUnit' value='"+unit.getCode()+"' class='form-control' readonly>"
                            + "</td>"
                            + "<td>"
                                + "<input type='text' style='font-size: 11px;' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]+"-"+i+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]+"_"+i+"' value='"+poItem.getPriceKonv()+"' class='form-control changePriceKonvEvent'>"
                            + "</td>"
                            + "<td>"
                                + "<input style='font-size: 11px;' type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_COST]+"-"+i+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_COST]+"_"+i+"' value='"+poItem.getOrgBuyingPrice()+"' class='form-control changePriceKonvEvent2' >"
                                + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_CURRENCY_ID]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_CURRENCY_ID]+"' value='"+matCurrency.getOID()+"'>"
                            + "</td>"                        
                            + "<td>"
                                + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT]+"-"+i+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT]+"_"+i+"' value='"+poItem.getDiscount()+"' class='form-control cntTotalAllEvent' style='text-align:right;font-size: 11px;'>"
                            + "</td>"
                            + "<td>"
                                + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT2]+"-"+i+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT2]+"_"+i+"' value='"+poItem.getDiscount2()+"' class='form-control cntTotalAllEvent'  style='text-align:right;font-size: 11px;'>"
                            + "</td>"                   
                            + "<td>"
                                + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]+"-"+i+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]+"_"+i+"' value='"+poItem.getDiscNominal()+"' class='form-control cntTotalAllEvent' style='text-align:right;font-size: 11px;'>"
                            + "</td>"
                            + "<td>"
                                + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_FORWARDER_COST]+"-"+i+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_FORWARDER_COST]+"_"+i+"' value='' class='form-control cntTotalAllEvent' style='text-align:right;font-size: 11px;'>"
                            + "</td>";
                    }else{
                        result += ""
                            + "<td>"
                                + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_UNIT_ID]+"_"+i+"' value='"+poItem.getUnitId()+"'>"
                                + "<input type='text' name='matUnit' value='"+unit.getCode()+"' class='form-control' readonly>"
                                + "<input type='hidden' size='7' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]+"_"+i+"' value='"+poItem.getPriceKonv()+"' class='form-control'>"
                                + "<input type='hidden' readonly='readonly' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_COST]+"_"+i+"' value='"+poItem.getOrgBuyingPrice()+"' class='form-control' >"
                                + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_CURRENCY_ID]+"' value='"+matCurrency.getOID()+"'>"                          
                                + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT]+"_"+i+"' value='"+poItem.getDiscount()+"' class='form-control' style='text-align:right'>"                          
                                + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT2]+"_"+i+"' value='"+poItem.getDiscount2()+"' class='form-control'  style='text-align:right'>"                          
                                + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]+"_"+i+"' value='"+poItem.getDiscNominal()+"' class='form-control' style='text-align:right'>"                          
                                + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_FORWARDER_COST]+"_"+i+"' value='' class='form-control' style='text-align:right'>"
                            + "</td>";
                    }
                    result += ""
                        + "<td>"
                            + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY]+"-"+i+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY]+"_"+i+"' value='"+poItem.getQuantity()+"' class='form-control' style='text-align:right; font-size:11px;'>"
                            + "<input type='hidden' id='FRM_FIELD_RESIDUE_QTY"+"-"+i+"' name='FRM_FIELD_RESIDUE_QTY"+"_"+i+"' value='"+sisa+"'>"
                        + "</td>";
                    
                    if(poItem.getBonus()==1) {
                        result +=""
                            + "<td>"
                                + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_BONUS]+"-"+i+"'  name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_BONUS]+"_"+i+"' value='1'>"
                                + "<input class='checksBoxs' type='checkbox' id='checksBoxs-"+i+"'  name='chekBonus' checked value='1'> Bonus"
                            + "</td>";
                    }else{
                        result += ""
                            + "<td>"
                                + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_BONUS]+"-"+i+"'  name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_BONUS]+"_"+i+"' value='0'>"
                                + "<input class='checksBoxs' type='checkbox' id='checksBoxs-"+i+"'   name='chekBonus' value='0'> Bonus"
                            + "</td>";
                    }
                    
                    if(privShowQtyPrice){
                        result +=""
                            + "<td>"
                                + "<input style='font-size:11px' type='text' id='total_cost"+"-"+i+"' name='total_cost"+"_"+i+"' value='"+poItem.getTotal()+"' class='form-control' readonly>"
                                + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_TOTAL]+"-"+i+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_TOTAL]+"_"+i+"' value='"+poItem.getTotal()+"' class='hiddenText'  readonly>"
                            + "</td>"; 
                    }
                    
                }
            }else{
                collspan = collspan + 1;
                result += ""
                    + "<tr>"
                        + "<td colspan='"+collspan+"'><center>No Data</center></td>"
                    + "</tr>";
            }
            
            result += ""
                + "</tbody>"
            + "</table>";
        
    return result;
    }
    
    private String saveAllItem(HttpServletRequest request){
        String result ="";
        MatReceive rec = new MatReceive();
        int iError = 0;
        
        long oidReceive = FRMQueryString.requestLong(request, "oidReceive");
        
        try {
            rec = PstMatReceive.fetchExc(oidReceive);
        } catch (Exception e) {
        }
 
        String whereClause = ""
            + " POI." + PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID] 
            + " = " + rec.getPurchaseOrderId()+ " AND POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_RESIDU_QTY]+" "
            + " < POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_QUANTITY];
        Vector listPo = PstPurchaseOrderItem.listPO(0,0,whereClause); 
        
        CtrlMatReceiveItem ctrlMatReceiveItem = new CtrlMatReceiveItem(request);
        iError = ctrlMatReceiveItem.actionSaveAll(request,listPo,oidReceive);
        
        return result;
    }
    
    private String getListReceiveItemByPO(HttpServletRequest request){
        String result ="";

        MatReceive rec = new MatReceive();
        int collspan =0;
        int start=0;
        int i=0;
        FrmMatReceive frmMatReceive = new FrmMatReceive();
        FrmMatReceiveItem frmMatReceiveItem = new FrmMatReceiveItem();
        
        long oidReceive = FRMQueryString.requestLong(request, "oidReceiveMaterial");
        int language = FRMQueryString.requestInt(request, "lang");
        boolean privManageData = FRMQueryString.requestBoolean(request, "privManageData");
        boolean privShowQtyPrice = FRMQueryString.requestBoolean(request, "privShowQtyPrice");
        int receiveStatus = FRMQueryString.requestInt(request, "receiveStatus");
        boolean bEnableExpiredDate = FRMQueryString.requestBoolean(request, "bEnableExpiredDate");
        
        
        String whereClauseItem = ""
            + " "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceive
            + " AND "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS]+"=0";
        String orderClauseItem = "";
        
        Vector listMatReceiveItem = PstMatReceiveItem.list(0,0,whereClauseItem);
        
        String whereUnit = "";
        Vector listBuyUnit = PstUnit.list(0,1000,whereUnit,"");
        Vector index_value = new Vector(1,1);
        Vector index_key = new Vector(1,1);
        index_key.add("-");
        index_value.add("0");
        for(int j=0;j<listBuyUnit.size();j++){
            Unit mateUnit = (Unit)listBuyUnit.get(j);
            index_key.add(mateUnit.getCode());
            index_value.add(""+mateUnit.getOID());
        }
        
        Vector listError = getListErrorVect(oidReceive);
       
        result +=""
        + "<table class='table table-hover table-striped table-bordered' width='100%'>"
            + "<thead style='font-size:13px;'>"
                + "<tr>"
                    + "<th style='width:1%'>"+textListOrderItem2[language][0]+"</th>"  
                    + "<th style='width:8%'>"+textListOrderItem2[language][1]+"</th>" 
                    + "<th style='width:8%'>"+textListOrderItem2[language][2]+"</th>" ;
                    if(bEnableExpiredDate){
                        result += "<th style='width:5%'>"+textListOrderItem2[language][3]+"</th>";
                        collspan = collspan + 4;
                    }else{
                        collspan = collspan + 3;
                    }
        result +=""
                    + "<th style='width:5%'>"+textListOrderItem2[language][14]+"</th>" 
                    + "<th style='width:8%'>"+textListOrderItem2[language][15]+"</th>" 
                    + "<th style='width:7%'>"+textListOrderItem[language][4]+"</th>" ;
                    collspan = collspan + 3;
                    if(privShowQtyPrice){
                        result += "<th style='width:8%'>"+textListOrderItem2[language][16]+"</th>";
                        result += "<th style='width:5%'>"+textListOrderItem2[language][5]+"</th>";
                        result += "<th style='width:5%'>"+textListOrderItem2[language][11]+"</th>";
                        result += "<th style='width:5%'>"+textListOrderItem2[language][12]+"</th>";
                        result += "<th style='width:5%'>"+textListOrderItem2[language][13]+"</th>";
                        result += "<th style='width:5%'>"+textListOrderItem2[language][6]+"</th>";                       
                        collspan = collspan + 6;
                    }
        result +=""
                    + "<th>"+textListOrderItem2[language][8]+"</th>"   
                    + "<th>"+textListOrderItem2[language][18]+"</th>" ;
                    collspan = collspan + 3;
                    if(privShowQtyPrice){
                        result += "<th>"+textListOrderItem2[language][9]+"</th>" ;
                        collspan = collspan + 1;
                    }
        result += ""
                    + "<th>"+textListOrderItem2[language][17]+"</th>"
                + "</tr>"
            + "</thead>"
            + "<tbody>";
            if (listMatReceiveItem.size()>0){
                for(i=0; i<listMatReceiveItem.size(); i++) {
                    Vector temp = (Vector)listMatReceiveItem.get(i);
                    MatReceiveItem recItem = (MatReceiveItem)temp.get(0);
                    Material mat = (Material)temp.get(1);
                    Unit unit = (Unit)temp.get(2);
                    Unit untiKonv = new Unit();
                    try{
                        untiKonv = PstUnit.fetchExc(recItem.getUnitKonversi());
                    }catch(Exception ex){

                    }

                    start = start + 1;
                    double totalForwarderCost = recItem.getForwarderCost() * recItem.getQty();
                    result += ""
                    + "<tr id='receivePo-"+i+"'>"
                        + "<td>"+start+"</td>"
                        + "<td>"+mat.getSku()+"</td>";
                        if(privShowQtyPrice){
                            if(mat.getCurrBuyPrice()<recItem.getCost()){
                                result += "<td style='cursor:pointer;' class='editItems' data-name='"+mat.getName()+"' data-code='"+mat.getSku()+"' data-oid='"+recItem.getMaterialId()+"'>"+mat.getName()+" &nbsp;<img src='../../../images/DOTreddotANI.gif'><font color='#FF0000'>[Edit]</font></td>";
                            }else{
                                result += "<td style='cursor:pointer;' class='editItems' data-name='"+mat.getName()+"' data-code='"+mat.getSku()+"' data-oid='"+recItem.getMaterialId()+"'>"+mat.getName()+" &bspb; [Edit]</td>";
                            }
                        }else{
                            result += "<td>"+mat.getName()+"</td>";
                        }
                        
                        if(bEnableExpiredDate){
                            result += ""
                            + "<td>"+Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy")+"</td>";                          
                        }
                        result += ""
                        + "<td>"+recItem.getQtyEntry()+"</td>"
                        + "<td>"+untiKonv.getCode()+"</td>"
                        + "<td>"+unit.getCode()+"</td>";
                        if(privShowQtyPrice){
                            result +=""
                            + "<td>"+FRMHandler.userFormatStringDecimal(recItem.getPriceKonv())+"</td>"
                            + "<td>"+FRMHandler.userFormatStringDecimal(recItem.getCost())+"</td>"
                            + "<td>"+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"</td>"
                            + "<td>"+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"</td>"
                            + "<td>"+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"</td>"
                            + "<td>"+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"</td>";                                                            
                        }  
                        if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                            String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID]+"="+recItem.getOID();
                            int cnt = PstReceiveStockCode.getCount(where);
                
                            double recQtyPerBuyUnit = recItem.getQty();
                            double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(mat.getBuyUnitId(), mat.getDefaultStockUnitId());
                            double recQty = recQtyPerBuyUnit * qtyPerSellingUnit;
                            double max = recQty;
                            /*
                            if(cnt<max){
                                if(listError.size()==0){
                                    listError.add("Silahkan cek :");
                                }
                                listError.add(""+listError.size()+". Jumlah serial kode stok "+mat.getName()+" tidak sama dengan qty terima");
                            }*/
                            result += ""
                            + "<td>"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</td>"
                            + "<td></td>";                           
                        }else{
                            result += ""
                            + "<td>"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</td>"
                            + "<td></td>";                
                        }
                        if(privShowQtyPrice){
                            result +=""
                            + "<td>"+FRMHandler.userFormatStringDecimal((recItem.getTotal()+totalForwarderCost))+"</td>";
                        }
                        
                        //Action
                        result += ""
                            + "<td>";
                                //BUTTON EDIT
                                if((privManageData || privShowQtyPrice)&& (receiveStatus==I_DocStatus.DOCUMENT_STATUS_DRAFT)){
                                     result += ""
                                        + "<button id='edit-"+i+"' data-recitem='"+recItem.getOID()+"' data-bonus='0' class='btn btn-warning editItemPo' type='button' style='height: 34px;'>"
                                            + "<i class='glyphicon glyphicon-pencil'></i> [e]"
                                        + "</button>";

                                    result +=""
                                    + "<button id='del-"+i+"' data-recitem='"+recItem.getOID()+"' class='btn btn-danger deleteItem' type='button' style='height: 34px;'>"
                                        + "<i class='glyphicon glyphicon-remove'></i> [d]"
                                    + "</button>";                                                        
                                }
                                //BUTTON DELETE
                        result +=""    
                            + "</td>";
                }
            }else{
                result += ""
                    + "<tr>"
                        + "<td colspan="+collspan+"><center>No Data...</center></td>"
                    + "</tr>";
            }
            
            //INPUT FORM
            int no = i +1;
            result += "<tr id='addForm'>";
            result += "<td>"+no+"</td>";
            result += ""
                + "<td>"
                    + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_MATERIAL_ID]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_MATERIAL_ID]+"_0' value=''>"
                    + "<div class='input-group'>"
                        + "<input style='font-size: 11px;' type='text' name='matCode' id='matCode' value='' class='form-control matCodes'>"
                        + "<span class='input-group-btn'>"
                            + "<button class='btn btn-default loadListItemPo' type='button' style='height: 34px;'>"
                                + "<i class='glyphicon glyphicon-search'></i>"
                            + "</button>"
                        + "</span>"
                    + "</div>"
                + "</td>"                 
                + "<td>"
                    + "<input style='font-size: 11px;' type='text' readonly id='matItem' name='matItem' value='' class='form-control'>"
                + "</td>";
                if(bEnableExpiredDate){
                    result += "<td><input style='font-size: 11px;' type='text' class='form-control datesPic' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_EXPIRED_DATE]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_EXPIRED_DATE]+"_0'></td>";
                }
                result += ""
                    + "<td>"
                        +"<input style='font-size: 11px;' type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY_INPUT]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY_INPUT]+"_0' value='' class='form-control changeQty'>"
                    + "</td>"                                     
                    + "<td>"                            
                        + ""+ControlCombo.draw(frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]+"_0", null, "", index_value, index_key,"","form-control changeUnitIdKonv")+""
                    + "</td>";
                    if(privShowQtyPrice){
                        result += ""
                        + "<td>"
                            + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_UNIT_ID]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_UNIT_ID]+"_0' value=''>"
                            + "<input style='font-size: 11px;' type='text' id='matUnit' name='matUnit' value='' class='form-control' readonly>"
                        + "</td>"
                        + "<td>"
                            + "<input type='text' style='font-size: 11px;' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]+"_0' value='' class='form-control changePriceKonvEvent'>"
                        + "</td>"
                        + "<td>"
                            + "<input style='font-size: 11px;' type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_COST]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_COST]+"_0' value='' class='form-control changePriceKonvEvent2' >"
                            + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_CURRENCY_ID]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_CURRENCY_ID]+"' value=''>"
                        + "</td>"                        
                        + "<td>"
                            + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT]+"_0' value='' class='form-control cntTotalAllEvent' style='text-align:right;font-size: 11px;'>"
                        + "</td>"
                        + "<td>"
                            + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT2]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT2]+"_0' value='' class='form-control cntTotalAllEvent'  style='text-align:right;font-size: 11px;'>"
                        + "</td>"                   
                        + "<td>"
                            + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]+"_0' value='' class='form-control cntTotalAllEvent' style='text-align:right;font-size: 11px;'>"
                        + "</td>"
                        + "<td>"
                            + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_FORWARDER_COST]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_FORWARDER_COST]+"_0' value='' class='form-control cntTotalAllEvent' style='text-align:right;font-size: 11px;'>"
                        + "</td>";
                    }else{
                        result += ""
                        + "<td>"
                            + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_UNIT_ID]+"_0' value=''>"
                            + "<input type='text' name='matUnit' value='' class='form-control' readonly>"
                            + "<input type='hidden' size='7' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]+"_0' value='' class='form-control'>"
                            + "<input type='hidden' readonly='readonly' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_COST]+"_0' value='' class='form-control' >"
                            + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_CURRENCY_ID]+"' value=''>"                          
                            + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT]+"_0' value='' class='form-control' style='text-align:right'>"                          
                            + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT2]+"_0' value='' class='form-control'  style='text-align:right'>"                          
                            + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]+"_0' value='' class='form-control' style='text-align:right'>"                          
                            + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_FORWARDER_COST]+"_0' value='' class='form-control' style='text-align:right'>"
                        + "</td>";
                    }
                    result += ""
                    + "<td>"
                        + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY]+"_0' value='' class='form-control' style='text-align:right; font-size:11px;'>"
                        + "<input type='hidden' id='FRM_FIELD_RESIDUE_QTY"+"-0' name='FRM_FIELD_RESIDUE_QTY"+"_0' value=''>"
                    + "</td>"
                    + "<td>"
                        + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_BONUS]+"-0'  name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_BONUS]+"_0' value='0'>"
                        + "<input class='checksBoxs' id='checksBoxs-0' type='checkbox'  name='chekBonus' value='1'> Bonus"
                    + "</td>";
                    if(privShowQtyPrice){
                        result +=""
                        + "<td>"
                            + "<input style='font-size:11px' type='text' id='total_cost"+"-0' name='total_cost"+"_0' value='' class='form-control' readonly>"
                            + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_TOTAL]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_TOTAL]+"_0' value='' class='hiddenText'  readonly>"
                        + "</td>"; 
                    }
                    result += ""
                    + "<td>"
                        + "<button class='btn btn-primary saveReceiveItemPo' type='button' style='height: 34px;'>"
                        + "<i class='glyphicon glyphicon-floppy-disk'></i> "               
                    + "</button>"
                    + "</td>"     
                + "</tr>";            
            result += ""
                + "</tbody>"
            + "</table>";
            
        return result;
    }
    
    private String getListItemModalPo(HttpServletRequest request){
        String result ="";
    
        long oidReceive = FRMQueryString.requestLong(request, "oidReceiveMaterial");
        int lang = FRMQueryString.requestInt(request, "lang");
        boolean privShowQtyPrice = FRMQueryString.requestBoolean(request, "privShowQtyPrice");
        
        result += ""
        + "<div class='modal-header'>"
            + "<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>"
            + "<h4 class='modal-title' id='modal-title'>"+textMaterialHeader[lang][3]+"</h4>"
        + "</div>"
        + "<div class='modal-body' id='modal-body'>"
            + "<div class='row'>"
                + "<div class='col-md-12'>"
                    + "<div class='panel panel-default'>" 
                        + "<div class='panel-heading'>&nbsp;</div>" 
                        + "<div class='panel-body'>"
                            + "" +listMaterialPo(oidReceive,lang,privShowQtyPrice)
                        + "</div>"
                    + "</div>"
                + "</div>"
            + "</div>"
            
            //LIST BONUS
            + "<div class='row'>"
                + "<div class='col-md-12'>"
                    + "<div class='panel panel-default'>" 
                        + "<div class='panel-heading'>Bonus Item</div>" 
                        + "<div class='panel-body'>"
                            + "" +listMaterialBonusPo(oidReceive,lang,privShowQtyPrice)
                        + "</div>"
                    + "</div>"
                + "</div>"
            + "</div>"           
        + "</div>";
        return result;
    }
    
    private String listMaterialPo(long oidReceive,int lang,boolean privShowQtyPrice ){
        String result ="";
        MatReceive rec = new MatReceive();
        int start = 0;
        int colspan = 0;
        int exchangeRate=1;
        
        try {
            rec = PstMatReceive.fetchExc(oidReceive);
        } catch (Exception e) {
        }
        
        String whereClause = " "
            + "POI." + PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+ " = " + rec.getPurchaseOrderId() 
            + " AND POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_RESIDU_QTY]+" < POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_QUANTITY]
            + " AND POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_BONUS]+"=0";
        
        Vector vectCount = PstPurchaseOrderItem.list(0, 0, whereClause);
        
        result+=""
        + "<table style='font-size: 13px;' class='table table-hover table-striped table-bordered'>"
            + "<thead>"
                + "<tr>"
                    + "<td>"+textListOrderItem3[lang][0]+"</td>"
                    + "<td>"+textListOrderItem3[lang][1]+"</td>"
                    + "<td>"+textListOrderItem3[lang][2]+"</td>" 
                    + "<td>"+textListOrderItem3[lang][3]+"</td>"   
                    + "<td>"+textListOrderItem3[lang][8]+"</td>";
                    colspan += 5;
                    if(privShowQtyPrice){
                        result+=""
                        + "<td>"+textListOrderItem3[lang][4]+"</td>"
                        + "<td>"+textListOrderItem3[lang][6]+"</td>"
                        + "<td>"+textListOrderItem3[lang][7]+"</td>"; 
                        colspan += 3;
                    }else{
                        result +=""
                        + "<td>"+textListOrderItem3[lang][6]+"</td>";
                        colspan += 1;
                    }
                   result +=""
                + "</tr>"
            + "</thead>"
            + "<tbody>";
            if (vectCount.size()>0){
                for(int i=0; i<vectCount.size(); i++){
                    Vector temp = (Vector)vectCount.get(i);
                    PurchaseOrderItem poItem = (PurchaseOrderItem)temp.get(0);
                    Material mat = (Material)temp.get(1);
                    Unit unit = (Unit)temp.get(2);
                    MatCurrency matCurrency = (MatCurrency)temp.get(3);
                    Vector rowx = new Vector();
                    start = start + 1;
                    Unit unitRequest = new Unit();
                    try{
                        unitRequest = PstUnit.fetchExc(poItem.getUnitRequestId());
                    }catch(Exception e){}

                    String name = "";
                    name = mat.getName();
                    name = name.replace('\"','`');
                    name = name.replace('\'','`');

                    result += ""
                    + "<tr data-materialid='"+poItem.getMaterialId()+"' data-matsku='"+mat.getSku()+"' "
                    + "data-materialname='"+name+"' data-unitcode='"+unit.getCode()+"' "
                    + "data-orgbuyingrate='"+FRMHandler.userFormatStringDecimal(poItem.getOrgBuyingPrice()/exchangeRate)+"' "
                    + "data-unitoid='"+poItem.getUnitId()+"' data-getQtyResidu='"+(poItem.getQuantity() - poItem.getResiduQty())+"' "
                    + "data-currencyid='"+poItem.getCurrencyId()+"' data-curencycode='"+matCurrency.getCode()+"' "
                    + "data-totaly='"+FRMHandler.userFormatStringDecimal((poItem.getCurBuyingPrice() * (poItem.getQuantity() - poItem.getResiduQty()))/exchangeRate)+"' "
                    + "data-unitrequestid='"+poItem.getUnitRequestId()+"' data-pricekonv='"+poItem.getPriceKonv()+"' "
                    + "data-bonus = '"+poItem.getBonus()+"'"
                    + "style='cursor:pointer' class='selectItemByPo'> "
                        + "<td>"+start+"</td>"
                        + "<td>"+mat.getSku()+"</td>"
                        + "<td>"+mat.getName()+"</td>"
                        + "<td>"+unit.getCode()+"</td>"
                        + "<td>"+unitRequest.getCode()+"</td>";
                        if(privShowQtyPrice){
                            result +=""
                            + "<td>"+FRMHandler.userFormatStringDecimal(poItem.getOrgBuyingPrice()/exchangeRate)+"</td>"
                            + "<td>"+String.valueOf(poItem.getQuantity() - poItem.getResiduQty())+"</td>"
                            + "<td>"+FRMHandler.userFormatStringDecimal(((poItem.getQuantity() - poItem.getResiduQty()) * poItem.getOrgBuyingPrice())/exchangeRate)+"</td>";
                        }else{
                            result += ""
                            + "<td>"+String.valueOf(poItem.getQuantity() - poItem.getResiduQty())+"</td>";
                        }
                    result +=""
                    + "</tr>";


                }
            }else{
                result +=""
                + "<tr>"
                    + "<td colspan='"+colspan+"'><center>No Data</center></td>"
                + "</tr>";
            }
            result += ""
            + "</tbody>"
        + "</table>";
        
        return result;
    }
    
    private String listMaterialBonusPo(long oidReceive,int lang,boolean privShowQtyPrice ){
        String result ="";
        MatReceive rec = new MatReceive();
        int start = 0;
        int colspan = 0;
        int exchangeRate=1;
        
        try {
            rec = PstMatReceive.fetchExc(oidReceive);
        } catch (Exception e) {
        }
        
        String whereClause = " "
            + "POI." + PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+ " = " + rec.getPurchaseOrderId() 
            + " AND POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_RESIDU_QTY]+" < POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_QUANTITY]
            + " AND POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_BONUS]+"=1";
        
        Vector vectCount = PstPurchaseOrderItem.list(0, 0, whereClause);
        
        result+=""
        + "<table style='font-size: 13px;' class='table table-hover table-striped table-bordered'>"
            + "<thead>"
                + "<tr>"
                    + "<td>"+textListOrderItem3[lang][0]+"</td>"
                    + "<td>"+textListOrderItem3[lang][1]+"</td>"
                    + "<td>"+textListOrderItem3[lang][2]+"</td>" 
                    + "<td>"+textListOrderItem3[lang][3]+"</td>"   
                    + "<td>"+textListOrderItem3[lang][8]+"</td>";
                    colspan += 5;
                    if(privShowQtyPrice){
                        result+=""
                        + "<td>"+textListOrderItem3[lang][4]+"</td>"
                        + "<td>"+textListOrderItem3[lang][6]+"</td>"
                        + "<td>"+textListOrderItem3[lang][7]+"</td>"; 
                        colspan += 3;
                    }else{
                        result +=""
                        + "<td>"+textListOrderItem3[lang][6]+"</td>";
                        colspan += 1;
                    }
                   result +=""
                + "</tr>"
            + "</thead>"
            + "<tbody>";
            if (vectCount.size()>0){
                for(int i=0; i<vectCount.size(); i++){
                    Vector temp = (Vector)vectCount.get(i);
                    PurchaseOrderItem poItem = (PurchaseOrderItem)temp.get(0);
                    Material mat = (Material)temp.get(1);
                    Unit unit = (Unit)temp.get(2);
                    MatCurrency matCurrency = (MatCurrency)temp.get(3);
                    Vector rowx = new Vector();
                    start = start + 1;
                    Unit unitRequest = new Unit();
                    try{
                        unitRequest = PstUnit.fetchExc(poItem.getUnitRequestId());
                    }catch(Exception e){}

                    String name = "";
                    name = mat.getName();
                    name = name.replace('\"','`');
                    name = name.replace('\'','`');

                    result += ""
                    + "<tr data-materialid='"+poItem.getMaterialId()+"' data-matsku='"+mat.getSku()+"' "
                    + "data-materialname='"+name+"' data-unitcode='"+unit.getCode()+"' "
                    + "data-orgbuyingrate='"+FRMHandler.userFormatStringDecimal(poItem.getOrgBuyingPrice()/exchangeRate)+"' "
                    + "data-unitoid='"+unit.getOID()+"' data-getQtyResidu='"+(poItem.getQuantity() - poItem.getResiduQty())+"' "
                    + "data-currencyid='"+poItem.getCurrencyId()+"' data-curencycode='"+matCurrency.getCode()+"' "
                    + "data-totaly='"+FRMHandler.userFormatStringDecimal((poItem.getCurBuyingPrice() * (poItem.getQuantity() - poItem.getResiduQty()))/exchangeRate)+"' "
                    + "data-unitrequestid='"+poItem.getUnitRequestId()+"' data-pricekonv='"+poItem.getPriceKonv()+"' "
                    + "data-bonus = '"+poItem.getBonus()+"'"
                    + "style='cursor:pointer' class='selectItemByPo'> "
                        + "<td>"+start+"</td>"
                        + "<td>"+mat.getSku()+"</td>"
                        + "<td>"+mat.getName()+"</td>"
                        + "<td>"+unit.getCode()+"</td>"
                        + "<td>"+unitRequest.getCode()+"</td>";
                        if(privShowQtyPrice){
                            result +=""
                            + "<td>"+FRMHandler.userFormatStringDecimal(poItem.getOrgBuyingPrice()/exchangeRate)+"</td>"
                            + "<td>"+String.valueOf(poItem.getQuantity() - poItem.getResiduQty())+"</td>"
                            + "<td>"+FRMHandler.userFormatStringDecimal(((poItem.getQuantity() - poItem.getResiduQty()) * poItem.getOrgBuyingPrice())/exchangeRate)+"</td>";
                        }else{
                            result += ""
                            + "<td>"+String.valueOf(poItem.getQuantity() - poItem.getResiduQty())+"</td>";
                        }
                    result +=""
                    + "</tr>";


                }
            }else{
                result +=""
                + "<tr>"
                    + "<td colspan='"+colspan+"'><center>No Data</center></td>"
                + "</tr>";
            }
            result += ""
            + "</tbody>"
        + "</table>";
            
        return result;
    }
    
    private String saveReceiveItemPo(HttpServletRequest request){
        String result = "";
        int iError=0;
        
        long oidReceive = FRMQueryString.requestLong(request, "oidReceive");
        
        CtrlMatReceiveItem ctrlMatReceiveItem = new CtrlMatReceiveItem(request);
        iError = ctrlMatReceiveItem.actionSaveAll2(request,oidReceive);
        
        return result;
    }
    
    private String getListReceiveItemByPOBonus(HttpServletRequest request){
        String result ="";       

        MatReceive rec = new MatReceive();
        int collspan =0;
        int start=0;
        int i=0;
        FrmMatReceive frmMatReceive = new FrmMatReceive();
        FrmMatReceiveItem frmMatReceiveItem = new FrmMatReceiveItem();
        
        long oidReceive = FRMQueryString.requestLong(request, "oidReceiveMaterial");
        int language = FRMQueryString.requestInt(request, "lang");
        boolean privManageData = FRMQueryString.requestBoolean(request, "privManageData");
        boolean privShowQtyPrice = FRMQueryString.requestBoolean(request, "privShowQtyPrice");
        int receiveStatus = FRMQueryString.requestInt(request, "receiveStatus");
        boolean bEnableExpiredDate = FRMQueryString.requestBoolean(request, "bEnableExpiredDate");
        
        
        String whereClauseItem = ""
            + " "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceive
            + " AND "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS]+"=1";
        String orderClauseItem = "";
        
        Vector listMatReceiveItem = PstMatReceiveItem.list(0,0,whereClauseItem);
        
        String whereUnit = "";
        Vector listBuyUnit = PstUnit.list(0,1000,whereUnit,"");
        Vector index_value = new Vector(1,1);
        Vector index_key = new Vector(1,1);
        index_key.add("-");
        index_value.add("0");
        for(int j=0;j<listBuyUnit.size();j++){
            Unit mateUnit = (Unit)listBuyUnit.get(j);
            index_key.add(mateUnit.getCode());
            index_value.add(""+mateUnit.getOID());
        }
        
        Vector listError = getListErrorVect(oidReceive);
       
        result +=""
        + "<table class='table table-hover table-striped table-bordered' width='100%'>"
            + "<thead style='font-size:13px;'>"
                + "<tr>"
                    + "<th style='width:1%'>"+textListOrderItem2[language][0]+"</th>"  
                    + "<th style='width:8%'>"+textListOrderItem2[language][1]+"</th>" 
                    + "<th style='width:8%'>"+textListOrderItem2[language][2]+"</th>" ;
                    if(bEnableExpiredDate){
                        result += "<th style='width:5%'>"+textListOrderItem2[language][3]+"</th>";
                        collspan = collspan + 4;
                    }else{
                        collspan = collspan + 3;
                    }
        result +=""
                    + "<th style='width:5%'>"+textListOrderItem2[language][14]+"</th>" 
                    + "<th style='width:8%'>"+textListOrderItem2[language][15]+"</th>" 
                    + "<th style='width:7%'>"+textListOrderItem[language][4]+"</th>" ;
                    collspan = collspan + 3;
                    if(privShowQtyPrice){
                        result += "<th style='width:8%'>"+textListOrderItem2[language][16]+"</th>";
                        result += "<th style='width:5%'>"+textListOrderItem2[language][5]+"</th>";
                        result += "<th style='width:5%'>"+textListOrderItem2[language][11]+"</th>";
                        result += "<th style='width:5%'>"+textListOrderItem2[language][12]+"</th>";
                        result += "<th style='width:5%'>"+textListOrderItem2[language][13]+"</th>";
                        result += "<th style='width:5%'>"+textListOrderItem2[language][6]+"</th>";                       
                        collspan = collspan + 6;
                    }
        result +=""
                    + "<th>"+textListOrderItem2[language][8]+"</th>"   
                    + "<th>"+textListOrderItem2[language][18]+"</th>" ;
                    collspan = collspan + 3;
                    if(privShowQtyPrice){
                        result += "<th>"+textListOrderItem2[language][9]+"</th>" ;
                        collspan = collspan + 1;
                    }
        result += ""
                    + "<th>"+textListOrderItem2[language][17]+"</th>"
                + "</tr>"
            + "</thead>"
            + "<tbody>";
            if (listMatReceiveItem.size()>0){
                for(i=0; i<listMatReceiveItem.size(); i++) {
                    Vector temp = (Vector)listMatReceiveItem.get(i);
                    MatReceiveItem recItem = (MatReceiveItem)temp.get(0);
                    Material mat = (Material)temp.get(1);
                    Unit unit = (Unit)temp.get(2);
                    Unit untiKonv = new Unit();
                    try{
                        untiKonv = PstUnit.fetchExc(recItem.getUnitKonversi());
                    }catch(Exception ex){

                    }

                    start = start + 1;
                    double totalForwarderCost = recItem.getForwarderCost() * recItem.getQty();
                    result += ""
                    + "<tr id='receiveBonusPo-"+i+"'>"
                        + "<td>"+start+"</td>"
                        + "<td>"+mat.getSku()+"</td>";
                        if(privShowQtyPrice){
                            if(mat.getCurrBuyPrice()<recItem.getCost()){
                                result += "<td style='cursor:pointer;' class='editItems' data-name='"+mat.getName()+"' data-code='"+mat.getSku()+"' data-oid='"+recItem.getMaterialId()+"'>"+mat.getName()+" &nbsp;<img src='../../../images/DOTreddotANI.gif'><font color='#FF0000'>[Edit]</font></td>";
                            }else{
                                result += "<td style='cursor:pointer;' class='editItems' data-name='"+mat.getName()+"' data-code='"+mat.getSku()+"' data-oid='"+recItem.getMaterialId()+"'>"+mat.getName()+" &bspb; [Edit]</td>";
                            }
                        }else{
                            result += "<td>"+mat.getName()+"</td>";
                        }
                        
                        if(bEnableExpiredDate){
                            result += ""
                            + "<td>"+Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy")+"</td>";                          
                        }
                        result += ""
                        + "<td>"+recItem.getQtyEntry()+"</td>"
                        + "<td>"+untiKonv.getCode()+"</td>"
                        + "<td>"+unit.getCode()+"</td>";
                        if(privShowQtyPrice){
                            result +=""
                            + "<td>"+FRMHandler.userFormatStringDecimal(recItem.getPriceKonv())+"</td>"
                            + "<td>"+FRMHandler.userFormatStringDecimal(recItem.getCost())+"</td>"
                            + "<td>"+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"</td>"
                            + "<td>"+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"</td>"
                            + "<td>"+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"</td>"
                            + "<td>"+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"</td>";                                                            
                        }  
                        if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                            String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID]+"="+recItem.getOID();
                            int cnt = PstReceiveStockCode.getCount(where);
                
                            double recQtyPerBuyUnit = recItem.getQty();
                            double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(mat.getBuyUnitId(), mat.getDefaultStockUnitId());
                            double recQty = recQtyPerBuyUnit * qtyPerSellingUnit;
                            double max = recQty;
                            /*
                            if(cnt<max){
                                if(listError.size()==0){
                                    listError.add("Silahkan cek :");
                                }
                                listError.add(""+listError.size()+". Jumlah serial kode stok "+mat.getName()+" tidak sama dengan qty terima");
                            }*/
                            result += ""
                            + "<td>"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</td>"
                            + "<td></td>";                           
                        }else{
                            result += ""
                            + "<td>"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</td>"
                            + "<td></td>";                
                        }
                        if(privShowQtyPrice){
                            result +=""
                            + "<td>"+FRMHandler.userFormatStringDecimal((recItem.getTotal()+totalForwarderCost))+"</td>";
                        }
                        
                        //Action
                        result += ""
                            + "<td>";
                                //BUTTON EDIT
                                if((privManageData || privShowQtyPrice)&& (receiveStatus==I_DocStatus.DOCUMENT_STATUS_DRAFT)){
                                     result += ""
                                        + "<button id='edit-"+i+"' data-recitem='"+recItem.getOID()+"' class='btn btn-warning editItemPo' data-bonus='1' type='button' style='height: 34px;'>"
                                            + "<i class='glyphicon glyphicon-pencil'></i> [e]"
                                        + "</button>";

                                    result +=""
                                    + "<button id='del-"+i+"' data-recitem='"+recItem.getOID()+"' class='btn btn-danger deleteItem' type='button' style='height: 34px;'>"
                                        + "<i class='glyphicon glyphicon-remove'></i> [d]"
                                    + "</button>";                                                        
                                }
                                //BUTTON DELETE
                        result +=""    
                            + "</td>";
                }
            }else{
                result += ""
                    + "<tr>"
                        + "<td colspan="+collspan+"><center>No Data...</center></td>"
                    + "</tr>";
            }
            result += ""
                + "</tbody>"
            + "</table>";
        return result;
    }
    
    private String showEditControlPo(HttpServletRequest request){
        String result ="";
        int start = 0;
        String whereUnit = "";
        FrmMatReceiveItem frmMatReceiveItem = new FrmMatReceiveItem();
        String addClass = "non";
        String checked = "";

        //REQUEST FORM
        long oidReceiveMaterialItem = FRMQueryString.requestLong(request, "oidReceiveMaterialItem");
        long oidReceiveMaterial = FRMQueryString.requestLong(request, "oidReceiveMaterial");
        int orderData = FRMQueryString.requestInt(request, "orderData");
        boolean privManageData = FRMQueryString.requestBoolean(request, "privManageData");
        boolean privShowQtyPrice = FRMQueryString.requestBoolean(request, "privShowQtyPrice");
        int receiveStatus = FRMQueryString.requestInt(request, "receiveStatus");
        boolean bEnableExpiredDate = FRMQueryString.requestBoolean(request, "bEnableExpiredDate");
        int language = FRMQueryString.requestInt(request, "lang");
        String readOnlyQty = FRMQueryString.requestString(request, "readOnlyQty");
        int bonus = FRMQueryString.requestInt(request, "bonus");
        
        if (bonus==1){
            addClass = "bonus";
        }
        
        start = orderData +1;
        MatReceive rec = new MatReceive();
        MatReceiveItem recItem = new MatReceiveItem();
        Material mat = new Material();
        Unit unit = new Unit();
        CurrencyType currencyType = new CurrencyType();
        
        Vector listBuyUnit = PstUnit.list(0,1000,whereUnit,"");
        Vector index_value = new Vector(1,1);
        Vector index_key = new Vector(1,1);
        index_key.add("-");
        index_value.add("0");
        for(int j=0;j<listBuyUnit.size();j++){
            Unit mateUnit = (Unit)listBuyUnit.get(j);
            index_key.add(mateUnit.getCode());
            index_value.add(""+mateUnit.getOID());
        }
        
        try {
            rec = PstMatReceive.fetchExc(oidReceiveMaterial);
            recItem = PstMatReceiveItem.fetchExc(oidReceiveMaterialItem);
            mat = PstMaterial.fetchExc(recItem.getMaterialId());
            unit = PstUnit.fetchExc(recItem.getUnitId());
            currencyType = PstCurrencyType.fetchExc(rec.getCurrencyId());
        } catch (Exception e) {
        }
        
        double totalForwarderCost = recItem.getForwarderCost() * recItem.getQty();
        Unit untiKonv = new Unit();
        try{
            untiKonv = PstUnit.fetchExc(recItem.getUnitKonversi());
        }catch(Exception ex){

        }
        
        if (recItem.getBonus()==1){
            checked="checked";       
        }else{
            checked="";
        }
        
             
        result += "<td>"+start+"</td>";
        result += ""
            + "<td>"
                + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_RECEIVE_MATERIAL_ITEM_ID]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_RECEIVE_MATERIAL_ITEM_ID]+"_0' value='"+recItem.getOID()+"'>"
                + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_MATERIAL_ID]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_MATERIAL_ID]+"_0' value='"+recItem.getMaterialId()+"'>"
                + "<div class='input-group'>"
                    + "<input style='font-size: 11px;' type='text' name='matCode' id='matCode' value='"+mat.getSku()+"' data-i='0' class='form-control matCodes'>"
                    + "<span class='input-group-btn'>"
                        + "<button class='btn btn-default loadListItemPo' type='button' style='height: 34px;'>"
                            + "<i class='glyphicon glyphicon-search'></i>"
                        + "</button>"
                    + "</span>"
                + "</div>"
            + "</td>"                 
            + "<td>"
                + "<input style='font-size: 11px;' type='text' readonly id='matItem' name='matItem' value='"+mat.getName()+"' class='form-control'>"
            + "</td>";
            if(bEnableExpiredDate){
                result += "<td><input style='font-size: 11px;' type='text' class='form-control datesPic' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_EXPIRED_DATE]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_EXPIRED_DATE]+"_0' value='"+Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy")+"'></td>";
            }
            result += ""
                + "<td>"
                    +"<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_RECEIVE_MATERIAL_ITEM_ID]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_RECEIVE_MATERIAL_ITEM_ID]+"_0' value='"+recItem.getOID()+"'>"
                    +"<input style='font-size: 11px;' type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY_INPUT]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY_INPUT]+"_0' value='"+recItem.getQtyEntry()+"' class='form-control changeQty'>"
                + "</td>"                                     
                + "<td>"                            
                    + ""+ControlCombo.draw(frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]+"_0", null, ""+untiKonv.getCode(), index_value, index_key,"","form-control changeUnitIdKonv")+""
                + "</td>";
                if(privShowQtyPrice){
                    result += ""
                    + "<td>"
                        + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_UNIT_ID]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_UNIT_ID]+"_0' value='"+recItem.getUnitId()+"'>"
                        + "<input style='font-size: 11px;' type='text' id='matUnit' name='matUnit' value='"+unit.getCode()+"' class='form-control' readonly>"
                    + "</td>"
                    + "<td>"
                        + "<input type='text' style='font-size: 11px;' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]+"_0' value='"+FRMHandler.userFormatStringDecimal(recItem.getPriceKonv())+"' class='form-control changePriceKonvEvent'>"
                    + "</td>"
                    + "<td>"
                        + "<input style='font-size: 11px;' type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_COST]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_COST]+"_0' value='"+recItem.getCost()+"' class='form-control changePriceKonvEvent2' >"
                        + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_CURRENCY_ID]+"' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_CURRENCY_ID]+"' value='"+recItem.getCurrencyId()+"'>"
                    + "</td>"                        
                    + "<td>"
                        + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT]+"_0' value='"+recItem.getDiscount()+"' class='form-control cntTotalAllEvent' style='text-align:right;font-size: 11px;'>"
                    + "</td>"
                    + "<td>"
                        + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT2]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT2]+"_0' value='"+recItem.getDiscount2()+"' class='form-control cntTotalAllEvent'  style='text-align:right;font-size: 11px;'>"
                    + "</td>"                   
                    + "<td>"
                        + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]+"_0' value='"+recItem.getDiscNominal()+"' class='form-control cntTotalAllEvent' style='text-align:right;font-size: 11px;'>"
                    + "</td>"
                    + "<td>"
                        + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_FORWARDER_COST]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_FORWARDER_COST]+"_0' value='"+recItem.getForwarderCost()+"' class='form-control cntTotalAllEvent' style='text-align:right;font-size: 11px;'>"
                    + "</td>";
                }else{
                    result += ""
                    + "<td>"
                        + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_UNIT_ID]+"_0' value='"+recItem.getUnitId()+"'>"
                        + "<input type='text' name='matUnit' value='"+unit.getCode()+"' class='form-control' readonly>"
                        + "<input type='hidden' size='7' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]+"_0' value='"+FRMHandler.userFormatStringDecimal(recItem.getPriceKonv())+"' class='form-control'>"
                        + "<input type='hidden' readonly='readonly' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_COST]+"_0' value='"+recItem.getCost()+"' class='form-control' >"
                        + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_CURRENCY_ID]+"' value='"+recItem.getCurrencyId()+"'>"                          
                        + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT]+"_0' value='"+recItem.getDiscount()+"' class='form-control' style='text-align:right'>"                          
                        + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISCOUNT2]+"_0' value='"+recItem.getDiscount2()+"' class='form-control'  style='text-align:right'>"                          
                        + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]+"_0' value='"+recItem.getDiscNominal()+"' class='form-control' style='text-align:right'>"                          
                        + "<input type='hidden' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_FORWARDER_COST]+"_0' value='"+recItem.getForwarderCost()+"' class='form-control' style='text-align:right'>"
                    + "</td>";
                }
                result += ""
                + "<td>"
                    + "<input type='text' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_QTY]+"_0' value='"+recItem.getQty()+"' class='form-control' style='text-align:right; font-size:11px;'>"
                    + "<input type='hidden' id='FRM_FIELD_RESIDUE_QTY"+"-0' name='FRM_FIELD_RESIDUE_QTY"+"_0' value='"+recItem.getResidueQty()+"'>"
                + "</td>"
                + "<td>"
                    + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_BONUS]+"-0'  name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_BONUS]+"_0' value='"+recItem.getBonus()+"'>"
                    + "<input class='checksBoxs' id='checksBoxs-0' type='checkbox' "+checked+"  name='chekBonus' value='1'> Bonus"
                + "</td>";
                if(privShowQtyPrice){
                    result +=""
                    + "<td>"
                        + "<input style='font-size:11px' type='text' id='total_cost"+"-0' name='total_cost"+"_0' value='"+FRMHandler.userFormatStringDecimal((recItem.getTotal()+totalForwarderCost))+"' class='form-control' readonly>"
                        + "<input type='hidden' id='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_TOTAL]+"-0' name='"+frmMatReceiveItem.fieldNames[frmMatReceiveItem.FRM_FIELD_TOTAL]+"_0' value='"+FRMHandler.userFormatStringDecimal((recItem.getTotal()+totalForwarderCost))+"' class='hiddenText'  readonly>"
                    + "</td>"; 
                }
                result += ""
                   + "<td>"
                       + "<button class='btn btn-primary saveEditReceiveItemPo' data-bonus='"+recItem.getBonus()+"' type='button' style='height: 34px;'>"
                       + "<i class='glyphicon glyphicon-floppy-disk'></i> "               
                   + "</button>"
                   + "</td>";   
        
        return result;
    }
    
    private String saveEditReceiveItemPo(HttpServletRequest request){
        String result ="";
        
        int iError = 0;
                
        long oidReceive = FRMQueryString.requestLong(request, "oidReceiveMaterial");
        long oidReceiveMatItem = FRMQueryString.requestLong(request, "FRM_FIELD_RECEIVE_MATERIAL_ITEM_ID");
        String userName = FRMQueryString.requestString(request, "userName");
        long userId = FRMQueryString.requestLong(request, "userId");
        int command = FRMQueryString.requestInt(request, "command");
        
        CtrlMatReceiveItem ctrlMatReceiveItem = new CtrlMatReceiveItem(request);
        iError = ctrlMatReceiveItem.action2(command,oidReceiveMatItem,oidReceive,userName, userId,request);
        
        result = String.valueOf(iError);
        
        return result;
    }
    
    private String getListItemModalPoBySkuName(HttpServletRequest request){
        String result ="";
        MatReceive rec = new MatReceive();
        double exchangeRate=0;
        long oidReceiveMaterial= FRMQueryString.requestLong(request, "oidReceiveMaterial");
        String search =  FRMQueryString.requestString(request, "search");
        boolean privShowQtyPrice = FRMQueryString.requestBoolean(request, "privShowQtyPrice");
        
        try {
            rec = PstMatReceive.fetchExc(oidReceiveMaterial);
        } catch (Exception e) {
        }
        
        String whereClause = " "
            + "POI." + PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+ " = " + rec.getPurchaseOrderId() 
            + " AND POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_RESIDU_QTY]+" < POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_QUANTITY]
            + " AND (MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU]+" LIKE '%"+search+"%'"
            + " OR MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" LIKE '%"+search+"%'"
            + " OR MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] +" LIKE '%"+search+"%')";
        
        Vector vectCount = PstPurchaseOrderItem.list(0, 0, whereClause);
        
        if (vectCount.size()>0){
            
            Vector temp = new Vector();           
            temp = (Vector)vectCount.get(0);
            PurchaseOrderItem poItem = (PurchaseOrderItem)temp.get(0);
            Material mat = (Material)temp.get(1);
            Unit unit = (Unit)temp.get(2); 
            MatCurrency matCurrency = (MatCurrency)temp.get(3); 
            
            String name = "";
            name = mat.getName();
            name = name.replace('\"','`');
            name = name.replace('\'','`');
            
            result += ""
                + "" +poItem.getMaterialId()+":"
                + "" +mat.getSku()+":"
                + "" +name+":"
                + "" +unit.getCode()+":"
                + "" +FRMHandler.userFormatStringDecimal(poItem.getOrgBuyingPrice()/exchangeRate)+":"
                + "" +poItem.getUnitId()+":"
                + "" +(poItem.getQuantity() - poItem.getResiduQty())+": "
                + "" +poItem.getCurrencyId()+":"
                + "" +matCurrency.getCode()+":"
                + "" +FRMHandler.userFormatStringDecimal((poItem.getCurBuyingPrice() * (poItem.getQuantity() - poItem.getResiduQty()))/exchangeRate)+": "
                + "" +poItem.getUnitRequestId()+": "
                + "" +poItem.getPriceKonv()+": "
                + "" +poItem.getBonus()+"";

        }
        return result;
    }
}
