<%-- 
    Document   : search_dispatch_warehouse
    Created on : Jan 17, 2020, 1:53:25 PM
    Author     : WiraDharma
--%>

<%@page import="com.dimata.util.LogicParser"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseOrder"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstPurchaseOrder"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcPurchaseOrder"%>
<%@page import="com.dimata.posbo.session.purchasing.SessPurchaseOrder"%>
<%@page import="com.dimata.posbo.entity.search.SrcPurchaseOrder"%>
<%@page import="com.dimata.posbo.form.purchasing.CtrlPurchaseOrder"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.qdep.entity.I_PstDocType"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!

public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;
%>
<%
boolean privView=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_VIEW));
boolean privSubmit=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_SUBMIT));
  I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
  I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
  I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
  int systemName = I_DocType.SYSTEM_MATERIAL;
  int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_POR);
  int iCommand = FRMQueryString.requestCommand(request);
  int excCode = FRMMessage.NONE;
  
  int dutyFree = SessPurchaseOrder.getStrDutyFree();
  long oidPurchaseOrder = FRMQueryString.requestLong(request, "hidden_material_order_id");
  String number = FRMQueryString.requestString(request, "FRM_FIELD_PRMNUMBER");
  String tglAwal = FRMQueryString.requestString(request, "FRM_FIELD_PRMDATEFROM");
  String tglAkhir = FRMQueryString.requestString(request, "FRM_FIELD_PRMDATETO");
String vendor = FRMQueryString.requestString(request, "FRM_FIELD_VENDORNAME");
  String sortBy = FRMQueryString.requestString(request, "FRM_FIELD_SORTBY");
  String[] status =  FRMQueryString.requestStringValues(request, "FRM_FIELD_PRMSTATUS");
  
//  GetFormat Date
//    if (tglAwal.isEmpty()) {
//        tglAwal = Formater.formatDate(new java.util.Date(), "yyyy-MM-dd");
//    }
//    if (tglAkhir.isEmpty()) {
//        tglAkhir = Formater.formatDate(new java.util.Date(), "yyyy-MM-dd");
//    }
  
ControlLine ctrLine = new ControlLine();
CtrlPurchaseOrder ctrlPurchaseOrder = new CtrlPurchaseOrder(request);
SrcPurchaseOrder srcPurchaseOrder = new SrcPurchaseOrder();
SessPurchaseOrder sessPurchaseOrder = new SessPurchaseOrder();
FrmSrcPurchaseOrder frmSrcPurchaseOrder = new FrmSrcPurchaseOrder(request, srcPurchaseOrder);

  int start = 0; 
  int record = 0; 
  String whereClause = ""; 
  String order = ""; 

//  Location List
//  whereClause = " (" + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
//                         + " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE + ")"
//                         + " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");
Vector listLocation = PstLocation.list(start, record, whereClause, order);
Vector vectSt = new Vector(1,1);
String[] strStatus = request.getParameterValues("FRM_FIELD_PRMSTATUS");
if(strStatus!=null && strStatus.length>0){
     for(int i=0; i<strStatus.length; i++){
        try{
                vectSt.add(strStatus[i]);
        }catch(Exception exc){
                System.out.println("err");
        }
     }
}
	 srcPurchaseOrder.setPrmstatus(vectSt);
  if (vendor.length()>0){
    srcPurchaseOrder.setVendorname(vendor);
}
     Vector vectOrderVendor = LogicParser.textSentence(srcPurchaseOrder.getVendorname());
  for (int i = 0; i < vectOrderVendor.size(); i++) {
      String name = (String) vectOrderVendor.get(i);
      if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
          vectOrderVendor.remove(i);
  }
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
        $('#tambah').click(function (){
            cmdAdd();
        });
      });
      function cmdAdd(){
            document.frmPurchaseRequest.command.value="<%=Command.ADD%>";
            document.frmPurchaseRequest.approval_command.value="<%=Command.SAVE%>";
            document.frmPurchaseRequest.add_type.value="<%=ADD_TYPE_SEARCH%>";
            document.frmPurchaseRequest.action="pomaterial_edit.jsp";
            if(compareDateForAdd()==true)
                document.frmPurchaseRequest.submit();
        }

        function cmdEdit(oid){
            //alert("xxx " +oid);
            document.frmPurchaseRequest.hidden_material_order_id.value=oid;
            document.frmPurchaseRequest.start.value=0;
            document.frmPurchaseRequest.approval_command.value="<%=Command.APPROVE%>";
            document.frmPurchaseRequest.command.value="<%=Command.EDIT%>";
            document.frmPurchaseRequest.action="pomaterial_edit.jsp";
            document.frmPurchaseRequest.submit();
        }
    
  
    </script>
  </head>
  <body>
    <section class="content-header">
      <h1>Pembelian<small> Pencarian</small> </h1>
      <ol class="breadcrumb">
        <li>Order Pembelian</li>
        <li>Gudang</li>
      </ol>
    </section>
    <section class="content">
      <div class="box box-prochain">
        <div class="box-header with-border">
          <label class="box-title pull-left">Pencarian</label>
        </div>
        <div class="box-body">
            <form name="frmPurchaseRequest" id="frmPurchaseRequest" method="post" action="">
            <input type="hidden" name="command" value="<%=Command.LIST%>">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="approval_command">
              <input type="hidden" name="hidden_material_order_id" value="<%=oidPurchaseOrder %>">
              <input type="hidden" name="start" value="<%=start%>">
            <div class="row">
              <div class="col-md-12">
                
                <div class="col-md-6">
                  <div class="form-group">
                    <label class="col-sm-4">Nomor</label>
                    <div class="input-group col-sm-8">
                        <input type="text" class="form-control input-sm" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_PRMNUMBER] %>"  value="<%=number %>" placeholder="Purchase Number" >
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4">Supplier</label>
                    <div class="input-group col-sm-8">
                        <input type="text" class="form-control input-sm" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_VENDORNAME] %>"  value="<%=vendor %>" placeholder="Supplier" >
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4">Tanggal</label>
                    <div class="input-group col-sm-8">
                      <div class="bootstrap-timepicker">                                              
                          <input type="text" class="form-control input-sm datePicker" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_PRMDATEFROM] %>" style="width: 40%;"  value="" placeholder="semua tanggal">
                      </div>
                      <span class="input-group-addon" style="float: left;height: 30px;width: 20%;border-radius: 0px !important;"> s/d </span>
                      <div class="bootstrap-timepicker">           
                        <input type="text" class="form-control input-sm datePicker" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_PRMDATETO] %>" style="width: 40%;" value="" placeholder="semua tanggal">
                      </div>
                    </div>
                  </div>
                </div>
                
                <div class="col-md-6">
                  <div class="form-group">
                    <label class="col-sm-4">Urut Berdasar</label>
                    <div class="input-group col-sm-8">
                        <select class="form-control input-sm select2" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_SORTBY]%>" >
                        <%
                            for (int i = 0; i < SrcPurchaseOrder.textListSortByKey.length; i++) {
                           long oidSort = Long.parseLong(SrcPurchaseOrder.textListSortByValue[i]);
                            String selected = "";
                            %>
                        <option value="<%=SrcPurchaseOrder.textListSortByValue[i] %>" <%=SrcPurchaseOrder.textListSortByValue[i]  == sortBy ? "selected" : ""%>><%=SrcPurchaseOrder.textListSortByKey[i] %></option>
                        <%}
                        %>
                      </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4">Status</label>
                    <div class="input-group col-sm-8">
                      <select class="form-control input-sm select2" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_PRMSTATUS]%>" multiple="" id="doc_status">
                        <%
                            for (int i = 0; i < SrcPurchaseOrder.statusKey.length; i++) {
                           long oidStatus = Long.parseLong(SrcPurchaseOrder.statusValue[i]);
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
                        <option value="<%=SrcPurchaseOrder.statusValue[i] %>" <%=selected %>><%=SrcPurchaseOrder.statusKey[i]%></option>
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
            <button type="submit" form="frmPurchaseRequest"  class="btn btn-prochain" id="cari-transfer"><i class="fa fa-search"> </i> Cari Data</button>
            <button type="button" class="btn btn-primary" id="tambah"><i class="fa fa-plus"> </i> Tambah</button>
          </div>
        </div>
      </div>
       <%
       if(iCommand == Command.LIST){
       %>
      <div class="box box-prochain">
        <div class="box-header with-border">
          <label class="box-title pull-left">List Pembelian</label>
        </div>
        <div class="box-body">
          <div class="row">
            <div class="col-md-12">
              <div id="purchaseTableElement">
                <table id="listPurchase" class="table table-striped table-bordered table-responsive" style="font-size: 14px">
                    <thead>
                        <tr>
                            <th>No.</th>                                    
                            <th>Kode</th>
                            <th>Tanggal</th>
                            <%if(dutyFree == 1){%>
                            <th>Lokasi</th>
                            <th>Kode Bea Cukai</th>
                            <%}%>
                            <th>Mata Uang</th>
                            <th>Supplier</th>
                            <th>Status</th>
                            <th>Keterangan</th>
                            <th class="aksi">Aksi</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                        whereClause = " MAT." + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCHASE_ORDER_ID]+" <> 0";
                        if(!number.isEmpty()){
                        whereClause += " AND MAT." + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE]+" = '"+number+"'";
                        }

                        if(!tglAwal.isEmpty()){
                        whereClause += " AND MAT." + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCH_DATE]+" BETWEEN '"+tglAwal+"' AND '"+tglAkhir+"'";
                        }
                        String stt = "";
                        if (srcPurchaseOrder.getPrmstatus() != null && srcPurchaseOrder.getPrmstatus().size() > 0) {
                            for (int a = 0; a < srcPurchaseOrder.getPrmstatus().size(); a++) {
                                if (stt.length() != 0) {
                                    stt = stt + " OR " + "(MAT." + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS] + " =" + srcPurchaseOrder.getPrmstatus().get(a) + ")";
                                } else {
                                    stt = "(MAT." + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS] + " =" + srcPurchaseOrder.getPrmstatus().get(a) + ")";
                                }
                            }
                            stt = "(" + stt + ")";
                            whereClause += " AND "+stt;
                        }
                          String strVendor = "";
                          if (vectOrderVendor != null && vectOrderVendor.size() > 0) {
                              for (int b = 0; b < vectOrderVendor.size(); b++) {
                                  if (strVendor.length() != 0) {
                                      strVendor = strVendor + " OR " + "(CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + vectOrderVendor.get(b) + "%') " +
                                              " OR " + "(CNT." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + vectOrderVendor.get(b) + "%') " +
                                              " OR " + "(CNT." + PstContactList.fieldNames[PstContactList.FLD_PERSON_LASTNAME] + " LIKE '%" + vectOrderVendor.get(b) + "%')";
                                  } else {
                                      strVendor = "(CNT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + vectOrderVendor.get(b) + "%') " +
                                              " OR " + "(CNT." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + vectOrderVendor.get(b) + "%')" +
                                              " OR " + "(CNT." + PstContactList.fieldNames[PstContactList.FLD_PERSON_LASTNAME] + " LIKE '%" + vectOrderVendor.get(b) + "%')";
                                  }
                              }
                              strVendor = "(" + strVendor + ")";
                            whereClause += " AND "+strVendor;
                          }
                        if(sortBy.equals("0")){
                            order = "MAT."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE]+ " ASC";
                        }else  if(sortBy.equals("1")){
                            order = "MAT."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCH_DATE]+ " ASC";
                        }else  if(sortBy.equals("2")){
                            order = "MAT."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS]+ " ASC";
                        }else{
                            order = "MAT."+PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_SUPPLIER_ID]+ " ASC";
                        }

                        Vector listPurchase = SessPurchaseOrder.listPurchase(start, record, whereClause, order);
                        
                         for(int i = 0; i < listPurchase.size(); i++){ 
                           Vector vt = (Vector)listPurchase.get(i);
                           PurchaseOrder pr = (PurchaseOrder)vt.get(0);
                           ContactList contact = (ContactList)vt.get(1);		               
                           CurrencyType currType = new CurrencyType();
                            Location loca = new Location();
                            oidPurchaseOrder = pr.getOID();
                            try {
                                    currType = PstCurrencyType.fetchExc(pr.getCurrencyId());
                                    if(pr.getLocationId() != 0){
                                    loca = PstLocation.fetchExc(pr.getLocationId());
                                    }
                                } catch (Exception e) {
                                }            
                        %>
                        <tr>
                            <td class="text-center"><%=i+1 %></td>
                            <td><%=pr.getPoCode() %></td>
                            <td><%=pr.getPurchDate() %></td>
                            <%if(dutyFree == 1){%>
                            <td><%=loca.getName() %></td>
                            <td>-</td>
                            <%}%>
                            <td><%=currType.getCode()%></td>
                            <td><%=contact.getCompName() %></td>
                            <td><%=i_status.getDocStatusName(docType,pr.getPoStatus()) %></td>
                            <td><%=pr.getRemark() %></td>
                            <td style="text-align: center;">
                                <a href="javascript:cmdEdit('<%=oidPurchaseOrder %>')" id="edit" class="btn btn-primary center text-center" ><i class="fa fa-pencil"> </i></a>
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
         $('#listPurchase').DataTable({
          "paging":   true,
          "ordering": false,
          "info":     true
          });
      });
    </script>
  </body>
</html>
