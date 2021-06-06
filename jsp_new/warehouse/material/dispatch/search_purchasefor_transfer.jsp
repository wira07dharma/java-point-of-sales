<%-- 
    Document   : search_purchasefor_transfer
    Created on : Feb 6, 2020, 9:05:47 AM
    Author     : Regen
--%>
<%@page import="com.dimata.posbo.entity.purchasing.PstPurchaseRequest"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseRequest"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcPurchaseRequest"%>
<%@page import="com.dimata.posbo.session.purchasing.SessPurchaseRequest"%>
<%@page import="com.dimata.posbo.entity.search.SrcPurchaseRequest"%>
<%@page import="com.dimata.posbo.form.purchasing.CtrlPurchaseRequest"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.qdep.entity.I_PstDocType"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.posbo.form.purchasing.FrmPurchaseRequest"%>
<%@page import="com.dimata.util.Command"%>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!

public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;
%>
<%  /**
   * get approval status for create document
   */
  I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
  I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
  I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
  int systemName = I_DocType.SYSTEM_MATERIAL;
  int docType = i_pstDocType.composeDocumentType(systemName, I_DocType.MAT_DOC_TYPE_POR);
%>


<%
  /**
   * get data from 'hidden form'
   */
  int iCommand = FRMQueryString.requestCommand(request);
  long oidPurchaseRequest = FRMQueryString.requestLong(request, " hidden_material_request_id");

  int iErrCode = FRMMessage.ERR_NONE;
  String msgStr = "";
  int vectSize = 0;

  CtrlPurchaseRequest ctrlPurchaseRequest = new CtrlPurchaseRequest(request);
  SrcPurchaseRequest srcPurchaseRequest = new SrcPurchaseRequest();
  SessPurchaseRequest sessPurchaseRequest = new SessPurchaseRequest();
  FrmSrcPurchaseRequest frmSrcPurchaseRequest = new FrmSrcPurchaseRequest(request, srcPurchaseRequest);
  srcPurchaseRequest.setSupplierWarehouse(-2);


  iErrCode = ctrlPurchaseRequest.action(iCommand, oidPurchaseRequest, userName, userId);
  FrmPurchaseRequest frmpo = ctrlPurchaseRequest.getForm();
  PurchaseRequest po = ctrlPurchaseRequest.getPurchaseRequest();

  String number = FRMQueryString.requestString(request, "FRM_FIELD_PRMNUMBER");
  String tglAwal = FRMQueryString.requestString(request, "FRM_FIELD_PRMDATEFROM");
  String tglAkhir = FRMQueryString.requestString(request, "FRM_FIELD_PRMDATETO");
  long oidLocationAsal = FRMQueryString.requestLong(request, "FRM_FIELD_LOCATION_ASAL");
  long oidLocationTujuan = FRMQueryString.requestLong(request, "FRM_FIELD_LOCATION_TUJUAN");
  String sortBy = FRMQueryString.requestString(request, "FRM_FIELD_SORTBY");
  String[] action =  FRMQueryString.requestStringValues(request, "ACTION");
  String[] pomaterial =  FRMQueryString.requestStringValues(request, "pomaterial");
  
  int start = 0; 
  int record = 0; 
  String whereClause = ""; 
  String order = ""; 

  Vector <Location> listLocation = PstLocation.list(start, record, whereClause, order);

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

        $('#tambah').click(function (){
          if($(".pomaterial").is(':checked')){
            cmdAdd();
          }else{
            alert('Please select Document Transfer first');
          }
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

      });

      function cmdAdd() {
        document.frmPurchaseRequest.command.value = "<%=Command.ADD%>";
        document.frmPurchaseRequest.approval_command.value = "<%=Command.SAVE%>";
        document.frmPurchaseRequest.add_type.value = "<%=ADD_TYPE_SEARCH%>";
        document.frmPurchaseRequest.action = "create_transferfromtransferequest.jsp";
        if (compareDateForAdd() == true)
          document.frmPurchaseRequest.submit();
      }
      function cmdEdit(oid) {
        var strvalue = "<%=approot%>/purchasing/material/pom/prtowarehousematerial_edit.jsp?hidden_material_request_id=" + oid + "&command=3";
        winSrcMaterial = window.open(strvalue, "", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
        if (window.focus) {
          winSrcMaterial.focus();
        }
      }
    </script>
  </head>
  <body>
    <section class="content-header">
      <h1>Transfer<small> dengan Transfer Request</small> </h1>
      <ol class="breadcrumb">
        <li>Menu</li>
        <li>Transfer</li>
      </ol>
    </section>
    <section class="content">
      <div class="box box-prochain">
        <div class="box-header with-border">
          <label class="box-title pull-left">Pencarian</label>
        </div>
        <div class="box-body">
          <form name="frmPurchaseRequest" id="frmPurchaseRequest" method="post" action="" class="form-horizontal">
                  <input type="hidden" name="command" value="<%=iCommand%>">
                  <input type="hidden" name="add_type" value="">
                  <input type="hidden" name="approval_command">
                  <input type="hidden" name="<%=frmSrcPurchaseRequest.fieldNames[frmSrcPurchaseRequest.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">
                  <div class="row">
              <div class="col-md-12">
                
                <div class="col-md-7">
                  <div class="form-group">
                    <label class="col-sm-4">Nomor</label>
                    <div class=" col-sm-8">
                        <input type="text" class="form-control input-sm"   value="" placeholder="Transfer Number" >
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4">Lokasi Asal</label>
                    <div class=" col-sm-8">
                      <select class="form-control input-sm select2" name="FRM_FIELD_LOCATION_ASAL">
                        <option value="0">Semua</option>
                        <%
                          for (int i = 0; i < listLocation.size(); i++) {
                            Location loc = (Location) listLocation.get(i);
                            %>
                        <option value="<%=loc.getOID()%>" <%=loc.getOID()  == oidLocationAsal ? "selected" : ""%>><%=loc.getName()%></option>
                        <%}
                        %>
                      </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4">Lokasi Tujuan</label>
                    <div class=" col-sm-8">
                      <select class="form-control input-sm select2" name="FRM_FIELD_LOCATION_TUJUAN">
                        <option value="0">Semua</option>
                        <%
                          for (int i = 0; i < listLocation.size(); i++) {
                            Location loc = (Location) listLocation.get(i);
                            %>
                        <option value="<%=loc.getOID()%>" <%=loc.getOID()  == oidLocationTujuan ? "selected" : ""%>><%=loc.getName()%></option>
                        <%}
                        %>
                      </select>
                    </div>
                  </div>
                </div>
                
                <div class="col-md-5">
                  <div class="form-group">
                    <label class="col-sm-4">Tanggal</label>
                    <div class="input-group col-sm-8" style="padding-left: 15px; padding-right: 15px">
                      <div class="bootstrap-timepicker">                                              
                          <input type="text" class="form-control input-sm datePicker" name="<%=frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMDATEFROM] %>" style="width: 40%;"  value="" placeholder="semua tanggal">
                      </div>
                      <span class="input-group-addon" style="float: left;height: 30px;width: 20%;border-radius: 0px !important;"> s/d </span>
                      <div class="bootstrap-timepicker">           
                        <input type="text" class="form-control input-sm datePicker" name="<%=frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMDATETO] %>" style="width: 40%;" value="" placeholder="semua tanggal">
                      </div>
                  </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4">Urut Berdasar</label>
                    <div class=" col-sm-8">
                        <select class="form-control input-sm select2" name="<%=frmSrcPurchaseRequest.fieldNames[frmSrcPurchaseRequest.FRM_FIELD_SORTBY]%>" >
                        <%
                            for (int i = 0; i < SrcPurchaseRequest.textListSortRequestKey.length; i++) {
                           long oidSort = Long.parseLong(SrcPurchaseRequest.textListSortRequestValue[i]);
                            String selected = "";
                            %>
                        <option value="<%=SrcPurchaseRequest.textListSortRequestValue[i] %>" <%=SrcPurchaseRequest.textListSortRequestValue[i]  == sortBy ? "selected" : ""%>><%=SrcPurchaseRequest.textListSortRequestKey[i] %></option>
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

      <div class="box box-prochain">
        <div class="box-header with-border">
          <label class="box-title pull-left">List Request Transfer</label>
        </div>
        <div class="box-body">
          <div class="row">
            <div class="col-md-12">
              <div id="requestTableElement">
                <table id="listRequestTransfer" class="table table-striped table-bordered table-responsive" style="font-size: 14px">
                    <thead>
                        <tr>
                            <th>No.</th>                                    
                            <th>Kode</th>
                            <th>Lokasi Asal</th>
                            <th>Lokasi Tujuan</th>
                            <th>Tanggal</th>
                            <th>Status</th>
                            <th>Keterangan</th>
                            <th class="aksi">Aksi</th>
                        </tr>
                    </thead>
                    <tbody>
                      <%
                        whereClause += " MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID]+" <> 0";
                        whereClause += " AND MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS]+" IN ('"+I_DocStatus.DOCUMENT_STATUS_FINAL+"', '"+I_DocStatus.DOCUMENT_STATUS_CLOSED+"')";
                        
                        if(oidLocationAsal != 0){
                        whereClause += " AND MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID]+" = "+oidLocationAsal;
                        }
                        if(oidLocationTujuan != 0){
                        whereClause += " AND MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID]+" = "+oidLocationTujuan;
                        }
                        if(!number.isEmpty()){
                        whereClause += " AND MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE]+" = '"+number+"'";
                        }
                        if(!tglAwal.isEmpty()){
                        whereClause += " AND MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE]+" BETWEEN '"+tglAwal+"' AND '"+tglAkhir+"'";
                        }
                        String stt = "";
                        if (srcPurchaseRequest.getPrmstatus() != null && srcPurchaseRequest.getPrmstatus().size() > 0) {
                            for (int a = 0; a < srcPurchaseRequest.getPrmstatus().size(); a++) {
                                if (stt.length() != 0) {
                                    stt = stt + " OR " + "(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] + " =" + srcPurchaseRequest.getPrmstatus().get(a) + ")";
                                } else {
                                    stt = "(MAT." + PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS] + " =" + srcPurchaseRequest.getPrmstatus().get(a) + ")";
                                }
                            }
                            stt = "(" + stt + ")";
                            whereClause += " AND "+stt;
                        }
                            order = " MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS]+", MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE]+ " ASC";
                        
                        if(sortBy.equals("0")){
                            order = " MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS]+", MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PO_CODE]+ " ASC";
                        }else  if(sortBy.equals("1")){
                            order = " MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS]+", MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_LOCATION_ID]+ " ASC";
                        }else  if(sortBy.equals("2")){
                            order = " MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS]+", MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_SUPPLIER_WAREHOUSE_ID]+ " ASC";
                        }else  if(sortBy.equals("3")){
                            order = " MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS]+", MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PURCH_REQUEST_DATE]+ " ASC";
                        }else  if(sortBy.equals("4")){
                            order = "MAT."+PstPurchaseRequest.fieldNames[PstPurchaseRequest.FLD_PR_STATUS]+ " ASC";
                        }

                        Vector listPurchase = SessPurchaseRequest.listPurchaseForPO(start, record, whereClause, order);
                        
                         for(int i = 0; i < listPurchase.size(); i++){   
                            PurchaseRequest pr = (PurchaseRequest) listPurchase.get(i);
                            Location locA = new Location();
                            Location locT = new Location();
                            String asal = "";
                            String tujuan = "";
                            oidPurchaseRequest = pr.getOID();
                            try {
                                    locA = PstLocation.fetchExc(pr.getLocationId());
                                    locT = PstLocation.fetchExc(pr.getWarehouseSupplierId());
                                    asal = locA.getName();
                                    tujuan = locT.getName();
                                } catch (Exception e) {
                                }
                        %>
                        <tr>
                            <td class="text-center"><%=i+1 %></td>
                            <td class="text-center"><a href="javascript:cmdEdit('<%=pr.getOID() %>')"><%=pr.getPrCode() %></a></td>
                            <td><%=asal %></td>
                            <td><%=tujuan %></td>
                            <td class="text-center"><%=pr.getPurchRequestDate() %></td>
                            <td class="text-center"><%=i_status.getDocStatusName(docType,pr.getPrStatus()) %></td>
                            <td><%=pr.getRemark() %></td>
                            <td style="text-align: center;">
                              <input type="checkbox" form="frmPurchaseRequest" class="flat-blue pomaterial" name="pomaterial_<%=pr.getOID() %>" value="1">
                              <input type="hidden" form="frmPurchaseRequest" class="pomaterialId" name="pomaterial" value="<%=pr.getOID() %>">
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
    </section>
    <script>
        
      $(document).ready(function () {
         $('#listRequestTransfer').DataTable({
          "paging":   true,
          "ordering": false,
          "info":     true
          });
      });
    </script>
  </body>
</html>
