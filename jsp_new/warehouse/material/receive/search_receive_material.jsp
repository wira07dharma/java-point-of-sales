<%-- 
    Document   : search_receive_material
    Created on : Jan 25, 2020, 10:03:31 PM
    Author     : WiraDharma
--%>

<%@page import="com.dimata.common.entity.contact.PstContactClassAssign"%>
<%@page import="com.dimata.common.entity.contact.ContactClassAssign"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceive"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseOrder"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcMatReceive"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatReceive"%>
<%@page import="com.dimata.posbo.entity.search.SrcMatReceive"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatReceive"%>
<%@page import="com.dimata.qdep.entity.I_PstDocType"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!
   public static final int ADD_TYPE_SEARCH = 0;
   public static final int ADD_TYPE_LIST = 1;
%>
<%    
  int appObjCodeWithoutPO = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_WITHOUT_PO);
  boolean privAddWithoutPO = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeWithoutPO, AppObjInfo.COMMAND_ADD));
  I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
  I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
  I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
  int systemName = I_DocType.SYSTEM_MATERIAL;
  int docType = i_pstDocType.composeDocumentType(systemName, I_DocType.MAT_DOC_TYPE_LMRR);
  boolean privManageData = true;
  
  int prochainMenuBootstrap = Integer.parseInt(PstSystemProperty.getValueByName("PROCHAIN_MENU_BOOTSTRAP"));
  String vendorOId = PstSystemProperty.getValueByName("VENDOR_OID");
  int iCommand = FRMQueryString.requestCommand(request);
  int excCode = FRMMessage.NONE;
  
  long oidMatReceive = FRMQueryString.requestLong(request, "hidden_receive_id");
  String number = FRMQueryString.requestString(request, "FRM_FIELD_RECEIVENUMBER");
  String tglAwal = FRMQueryString.requestString(request, "FRM_FIELD_RECEIVEFROMDATE");
  String tglAkhir = FRMQueryString.requestString(request, "FRM_FIELD_RECEIVETODATE");
  String sortBy = FRMQueryString.requestString(request, "FRM_FIELD_RECEIVESORTBY");
  long oidLocation = FRMQueryString.requestLong(request, "FRM_FIELD_LOCATION_ID");
  long oidVendor = FRMQueryString.requestLong(request, "FRM_FIELD_VENDORNAME");
  String invoice = FRMQueryString.requestString(request, "FRM_FIELD_INVOICE_SUPPLIER");
  String[] status =  FRMQueryString.requestStringValues(request, "FRM_FIELD_RECEIVESTATUS");

 
  
ControlLine ctrLine = new ControlLine();
CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
SrcMatReceive srcMatReceive = new SrcMatReceive();
SessMatReceive sessMatReceive = new SessMatReceive();
FrmSrcMatReceive frmSrcMatReceive = new FrmSrcMatReceive(request, srcMatReceive);

  int start = 0; 
  int record = 0; 
  String whereClause = ""; 
  String order = ""; 

//  Location List
//  whereClause = " (" + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
//                         + " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE + ")"
//                         + " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");
Vector <Location> listLocation = PstLocation.list(start, record, whereClause, order);
Vector vectSt = new Vector(1,1);
String[] strStatus = request.getParameterValues("FRM_FIELD_RECEIVESTATUS");
if(strStatus!=null && strStatus.length>0){
     for(int i=0; i<strStatus.length; i++){
        try{
                vectSt.add(strStatus[i]);
        }catch(Exception exc){
                System.out.println("err");
        }
     }
}
String whereVendor = PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID]+" = "+vendorOId;
Vector <ContactClassAssign> listVendor = PstContactClassAssign.list(start, record, whereVendor, order);
srcMatReceive.setReceiveSource(-1);
srcMatReceive.setReceivestatus(vectSt);
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dimata - ProChain POS</title>
        <%@include file="../../../styles/plugin_component.jsp" %>
    <style>
      
      .select2-container--default .select2-selection--multiple {
        background-color: white;
        border: 1px solid #d2d6de !important;
        border-radius: 0px !important;
        cursor: text;
      }
      .select2-container--default .select2-selection--single{
        background-color: white;
        border: 1px solid #d2d6de !important;
        border-radius: 0px !important;
        cursor: text;
      }
      .select2-container--default .select2-selection--multiple .select2-selection__choice {
        background-color: #6b7ae6 !important;
        border: 1px solid #ffffff00 !important;
        border-radius: 2px !important;
        color: white !important;
        cursor: pointer !important;
        float: left;
        margin-right: 5px;
        margin-top: 5px;
        padding: 0 5px;
      }
      .select2-container--default .select2-selection--multiple .select2-selection__choice__remove {
        color: #fff !important;
        cursor: pointer;
        display: inline-block;
        font-weight: bold;
        margin-right: 2px;
      }
      .select2-container {
          box-sizing: border-box;
          display: inline-block;
          width: 100% !important;
          margin: 0;
          position: relative;
          vertical-align: middle;
      }
      .input-group {
          position: relative;
          display: inline-block !important;
          border-collapse: separate;
      }
      
    </style>
    <script>
      $(document).ready(function () {
        
        //Timepicker
        $('.timepicker').timepicker({
          showInputs : false,
           showMeridian: false,
           format: 'hh:mm'
        });
//        Date Picker
        $('.datePicker').datetimepicker({
            format: "yyyy-mm-dd",
            todayBtn: true,
            autoclose: true,
            minView: 2
        });
        
        $('input[type="checkbox"].flat-blue').iCheck({
          checkboxClass: 'icheckbox_square-blue'
        });

        $('.select2').select2({placeholder: "Semua"});
        $('.selectAll').select2({placeholder: "Semua"});
        
        //MODAL SETTING
        var modalSetting = function (elementId, backdrop, keyboard, show) {
            $(elementId).modal({
                backdrop: backdrop,
                keyboard: keyboard,
                show: show
            });
        };
        $('#tambah-tanpa').click(function (){
            cmdAdd();
        });
        $('#tambah-po').click(function (){
            cmdAddPO();
        });
        $('#tambah-consignment').click(function (){
            cmdAddConsigment();
        });
        
        function cmdAddPO() {
            document.frmReceive.command.value = "<%=Command.ADD%>";
            document.frmReceive.approval_command.value = "<%=Command.SAVE%>";
            document.frmReceive.add_type.value = "<%=ADD_TYPE_SEARCH%>";

        <%if (prochainMenuBootstrap == 0) {%>
            document.frmReceive.action = "receive_wh_supp_po_material_edit.jsp";
            document.frmReceive.submit();
        <%} else {%>
            document.frmReceive.action = "receive_wh_supp_material_edit.jsp";
            document.frmReceive.submit();
        <%}%>
        }
        
        function cmdAdd() {
            document.frmReceive.command.value = "<%=Command.ADD%>";
            document.frmReceive.approval_command.value = "<%=Command.SAVE%>";
            document.frmReceive.add_type.value = "<%=ADD_TYPE_SEARCH%>";
        <%if (prochainMenuBootstrap == 0) {%>
            document.frmReceive.action = "receive_wh_supp_material_edit_old.jsp";
        <%} else {%>
            document.frmReceive.action = "receive_wh_supp_material_edit.jsp";
        <%}%>
            
        if (compareDateForAdd() == true)
            document.frmReceive.submit();
        }
        
        function cmdAddConsigment() {
            document.frmReceive.action = "consignment_sold.jsp";
            document.frmReceive.submit();
        }
      });
        function cmdEdit(idRcv, idPo) {
            document.frmReceive.hidden_receive_id.value = idRcv;
            document.frmReceive.start.value = 0;
            document.frmReceive.approval_command.value = "<%=Command.APPROVE%>";
            document.frmReceive.command.value = "<%=Command.EDIT%>";

            var prochainMenuBootstrap = "<%=prochainMenuBootstrap%>";
            if (parseInt(idPo) != 0) {
                if (prochainMenuBootstrap == 0) {
                    document.frmReceive.action = "receive_wh_supp_po_material_edit.jsp";
                } else {
                    document.frmReceive.action = "receive_wh_supp_material_edit.jsp";
                }
            } else {
                if (prochainMenuBootstrap == 0) {
                    document.frmReceive.action = "receive_wh_supp_material_edit_old.jsp";
                } else {
                    document.frmReceive.action = "receive_wh_supp_material_edit.jsp";
                }
            }
            document.frmReceive.submit();
        }
    </script>
    </head>
  <body>
    <section class="content-header">
      <h1>Penerimaan<small> Pencarian</small> </h1>
      <ol class="breadcrumb">
        <li>Penerimaan</li>
        <li>Pembelian</li>
      </ol>
    </section>
    <section class="content">
      <div class="box box-prochain">
        <div class="box-header with-border">
          <label class="box-title pull-left">Pencarian</label>
        </div>
        <div class="box-body">
            <form name="frmReceive" id="frmReceive" method="post" action="">
              <input type="hidden" name="command" value="<%=Command.LIST%>">
              <input type="hidden" name="hidden_receive_id" value="<%=oidMatReceive%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="add_type" value="">			
              <input type="hidden" name="approval_command">			    			  			  			  			  			  
              <input type="hidden" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">
              <input type="hidden" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVE_SOURCE]%>" value="<%=PstMatReceive.SOURCE_FROM_SUPPLIER%>">
            <div class="row">
              <div class="col-md-12">
                
                <div class="col-md-6">
                  <div class="form-group">
                    <label class="col-sm-4">Kode</label>
                    <div class="input-group col-sm-8">
                        <input type="text" class="form-control input-sm" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVENUMBER]%>"  value="" placeholder="Receive Code" >
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4">Supplier</label>
                    <div class="input-group col-sm-8">
                      <select class="form-control input-sm select2" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_VENDORNAME] %>">
                        <option value="0">Semua</option>
                        <%
                          for (ContactClassAssign con : listVendor) {
                            ContactList col = new ContactList();
                            try {
                                col = PstContactList.fetchExc(con.getContactId());
                              } catch (Exception e) {
                              }
                        %>
                        <option value="<%=col.getOID()%>" <%=col.getOID()  == oidVendor ? "selected" : ""%>><%=col.getCompName()%></option>
                        <%}
                        %>
                      </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4">Invoice Supplier</label>
                    <div class="input-group col-sm-8">
                        <input type="text" class="form-control input-sm" placeholder="Invoice" >
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4">Lokasi</label>
                    <div class="input-group col-sm-8">
                      <select class="form-control input-sm select2" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_LOCATION_ID] %>">
                        <option value="0">Semua</option>
                        <%
                          for (Location loc : listLocation) {
                        %>
                        <option value="<%=loc.getOID()%>" <%=loc.getOID()  == oidLocation ? "selected" : ""%>><%=loc.getName()%></option>
                        <%}
                        %>
                      </select>
                    </div>
                  </div>
                </div>
                
                <div class="col-md-6">
                  <div class="form-group">
                    <label class="col-sm-4">Tanggal</label>
                    <div class="input-group col-sm-8">
                      <div class="bootstrap-timepicker">                                              
                          <input type="text" autocomplete="off" class="form-control input-sm datePicker" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEFROMDATE]%>" style="width: 40%;"  value="" placeholder="semua tanggal">
                      </div>
                      <span class="input-group-addon" style="float: left;height: 30px;width: 20%;border-radius: 0px !important;"> s/d </span>
                      <div class="bootstrap-timepicker">           
                          <input type="text" autocomplete="off" class="form-control input-sm datePicker" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVETODATE]%>" style="width: 40%;" value="" placeholder="semua tanggal">
                      </div>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4">Urut Berdasar</label>
                    <div class="input-group col-sm-8">
                        <select class="form-control input-sm select2" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESORTBY] %>" >
                        <%
                            for (int i = 0; i < SrcMatReceive.textListSortByKey.length; i++) {
                           long oidSort = Long.parseLong(SrcMatReceive.textListSortByValue[i]);
                            String selected = "";
                            %>
                        <option value="<%=SrcMatReceive.textListSortByValue[i] %>" <%=SrcMatReceive.textListSortByValue[i]  == sortBy ? "selected" : ""%>><%=SrcMatReceive.textListSortByKey[i] %></option>
                        <%}
                        %>
                      </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4">Status</label>
                    <div class="input-group col-sm-8">
                      <select class="form-control input-sm select2" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]%>" multiple="" id="doc_status">
                        <%
                            for (int i = 0; i < SrcMatReceive.statusKey.length; i++) {
                           long oidStatus = Long.parseLong(SrcMatReceive.statusValue[i]);
                            String selected = "";
                            if(status != null && status.length > 0){
                            for (int ii = 0; ii < status.length; ii++) {
                              long oidCustom = Long.parseLong(status[ii]);
                              if (oidStatus == oidCustom) {
                                selected = "selected";
                              }
                          }
                        }
                            %>
                        <option value="<%=SrcMatReceive.statusValue[i] %>" <%=selected %>><%=SrcMatReceive.statusKey[i]%></option>
                        <%}
                        %>
                      </select>
                    </div>
                  </div>
                </div>

              </div>
            </div>
          </form>
        </div>
        <div class="box-footer">
          <div class="pull-left">
            <button type="submit" form="frmReceive"  class="btn btn-prochain" id="cari-transfer"><i class="fa fa-search"> </i> Cari Data</button>
            <button type="button" class="btn btn-primary" id="tambah-po"><i class="fa fa-plus"> </i> Penerimaan Dengan PO</button>
            <button type="button" class="btn btn-primary" id="tambah-tanpa"><i class="fa fa-plus"> </i> Penerimaan Tanpa PO</button>
            <button type="button" class="btn btn-primary" id="tambah-consignment"><i class="fa fa-plus"> </i> Penerimaan Consignment Sold</button>
          </div>
        </div>
      </div>
       <%
       if(iCommand == Command.LIST){
       %>
      <div class="box box-prochain">
        <div class="box-header with-border">
          <label class="box-title pull-left">List Penerimaan</label>
        </div>
        <div class="box-body">
          <div class="row">
            <div class="col-md-12">
              <div id="receiveTableElement">
                <table id="listReceive" class="table table-striped table-bordered table-responsive" style="font-size: 14px">
                    <thead>
                        <tr>
                            <th>No.</th>                                    
                            <th>Kode</th>
                            <th>Tanggal</th>
                            <th>Mata Uang</th>
                            <th>No PO</th>
                            <th>Supplier</th>
                            <th>Status</th>
                            <th>Term</th>
                            <th>Keterangan</th>
                            <th class="aksi">Aksi</th>
                        </tr>
                    </thead>
                    <tbody>
                    <%
                        whereClause = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID]+" <> 0 AND REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE]+" IN ("+PstMatReceive.SOURCE_FROM_SUPPLIER+","+PstMatReceive.SOURCE_FROM_SUPPLIER_PO+")";
                        if(oidLocation > 0){
                        whereClause = " REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID]+" = "+oidLocation;
                        }
                        if(!number.isEmpty()){
                        whereClause += " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE]+" = '"+number+"'";
                        }
                        if(!tglAwal.isEmpty()){
                        whereClause += " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]+" BETWEEN '"+tglAwal+" 00:00:00' AND '"+tglAkhir+" 23:59:59'";
                        }
                        if(oidVendor > 0){
                                whereClause += " AND CNT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]+" = "+oidVendor;
                        }
                        
                        if(!invoice.isEmpty()){
                        whereClause += " AND REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER] + " LIKE '%" + invoice + "%'";
                        }
                        String stt = "";
                        if (srcMatReceive.getReceivestatus() != null && srcMatReceive.getReceivestatus().size() > 0) {
                            for (int a = 0; a < srcMatReceive.getReceivestatus().size(); a++) {
                                if (stt.length() != 0) {
                                    stt = stt + " OR " + "(REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] + " =" + srcMatReceive.getReceivestatus().get(a) + ")";
                                } else {
                                    stt = "(REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] + " =" + srcMatReceive.getReceivestatus().get(a) + ")";
                                }
                            }
                            stt = "(" + stt + ")";
                            whereClause += " AND "+stt;
                        }
                        if(sortBy.equals("0")){
                            order = "REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE]+ " ASC";
                        }else  if(sortBy.equals("1")){
                            order = "REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]+ " ASC";
                        }else  if(sortBy.equals("2")){
                            order = "REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+ " ASC";
                        }else{
                            order = "CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+ " ASC";
                        }

                        Vector listReceive = SessMatReceive.listReceive(start, record, whereClause, order);
                        
                         for(int i = 0; i < listReceive.size(); i++){
                            Vector listData = (Vector) listReceive.get(i);
                            MatReceive rec = (MatReceive) listData.get(0);
                            ContactList contact = (ContactList) listData.get(1);
                            CurrencyType currencyType = (CurrencyType) listData.get(2);
                            PurchaseOrder purchaseOrder = (PurchaseOrder) listData.get(3);
                            String poCode = "-";
                            String terms = "-";
                            poCode =  purchaseOrder.getPoCode();
                            oidMatReceive = rec.getOID();
                            if (rec.getTermOfPayment() == 0) {
                                terms = "Cash";
                            } else {
                                terms = "Credit";
                            }
                            
                            String cntName = contact.getCompName();
                            if (cntName.length() == 0) {
                                cntName = String.valueOf(contact.getPersonName() + " " + contact.getPersonLastname());
                            }
                            String link = "'"+String.valueOf(rec.getOID()) + "', '" + purchaseOrder.getOID()+"'";
                        %>
                        <tr>
                            <td class="text-center"><%=i+1 %></td>
                            <td><%=rec.getRecCode()%></td>
                            <td><%=rec.getReceiveDate()%></td>
                            <td class="text-center"><%=currencyType.getCode()%></td>
                            <td><%=poCode%></td>
                            <td><%=cntName%></td>
                            <td><%=i_status.getDocStatusName(docType, rec.getReceiveStatus())%></td>
                            <td><%=terms%></td>
                            <td><%=rec.getRemark()%></td>
                            <td style="text-align: center;">
                                <a href="javascript:cmdEdit(<%=link%>)" id="edit" class="btn btn-primary center text-center" ><i class="fa fa-pencil"> </i></a>
                            </td>
                        </tr>
                        <%}%>
                    </tbody>
                </table>
            </div>
          </div>
          </div>
        </div>
        <div class="box-footer">
        </div>
      </div>
      <%}%>
    </section>
    <script>
        
      $(document).ready(function () {
         $('#listReceive').DataTable({
          "paging":   true,
          "ordering": false,
          "info":     true
          });
      });
    </script>
  </body>
</html>
