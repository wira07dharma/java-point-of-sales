<%-- 
    Document   : search_dispatch_warehouse
    Created on : Jan 17, 2020, 1:53:25 PM
    Author     : Regen
--%>

<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.qdep.entity.I_PstDocType"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcMatDispatch"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatDispatch"%>
<%@page import="com.dimata.posbo.entity.search.SrcMatDispatch"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatDispatch"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!
    public static final String dataTableTitle[][] = {
        {"Tampilkan _MENU_ data per halaman", 
        "Data Tidak Ditemukan", 
        "Menampilkan halaman _PAGE_ dari _PAGES_",
        "Belum Ada Data",
        "(Disaring dari _MAX_ data)",
        "Pencarian :",
        "Awal",
        "Akhir",
        "Berikutnya",
        "Sebelumnya"}, 
        
        {"Display _MENU_ records per page", 
        "Nothing found - sorry",
        "Showing page _PAGE_ of _PAGES_",
        "No records available",
        "(filtered from _MAX_ total records)",
        "Search :",
        "First",
        "Last",
        "Next",
        "Previous"}
    };
%>
<%
  int iCommand = FRMQueryString.requestCommand(request);
  int excCode = FRMMessage.NONE;
  I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
  I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
  I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
  int systemName = I_DocType.SYSTEM_MATERIAL;
  int docType = i_pstDocType.composeDocumentType(systemName, I_DocType.MAT_DOC_TYPE_DF);
  
  CtrlMatDispatch ctrlMatDispatch = new CtrlMatDispatch(request);
  SrcMatDispatch srcMatDispatch = new SrcMatDispatch();
  SessMatDispatch sessMatDispatch = new SessMatDispatch();
  FrmSrcMatDispatch frmSrcMatDispatch = new FrmSrcMatDispatch(request, srcMatDispatch);

  int start = 0; 
  int record = 0; 
  String whereClause = ""; 
  String order = ""; 

//  Location List
  whereClause = " (" + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
                         + " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE + ")"
                         + " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");;
   Vector listLocation = PstLocation.listLocationCreateDocument(start, record, whereClause, order);
   
//Status
  Vector listStatus = i_status.getDocStatusFor(docType);

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
        
          var getDataFunction = function (onDone, onSuccess, dataSend, servletName, dataAppendTo, notification) {
                    $(this).getData({
                        onDone: function (data) {
                            onDone(data);
                        },
                        onSuccess: function (data) {
                            onSuccess(data);
                        },
                        approot: "<%=approot%>",
                        dataSend: dataSend,
                        servletName: servletName,
                        dataAppendTo: dataAppendTo,
                        notification: notification
                    });
                };

                // DATABLE SETTING
                function dataTablesOptions(elementIdParent, elementId, servletName, dataFor, callBackDataTables, status) {
                    var datafilter = "";
                    var privUpdate = "";
                    $(elementIdParent).find('table').addClass('table-bordered table-striped table-hover').attr({'id': elementId});
                    $("#" + elementId).dataTable({"bDestroy": true,
                        "iDisplayLength": 10,
                        "bProcessing": true,
                        "oLanguage": {
                            "sProcessing": "<div class='col-sm-12'><div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div></div>",
                            "sLengthMenu": "<%=dataTableTitle[SESS_LANGUAGE][0]%>",
                            "sZeroRecords": "<%=dataTableTitle[SESS_LANGUAGE][1]%>",
                            "sInfo": "<%=dataTableTitle[SESS_LANGUAGE][2]%>",
                            "sInfoEmpty": "<%=dataTableTitle[SESS_LANGUAGE][3]%>",
                            "sInfoFiltered": "<%=dataTableTitle[SESS_LANGUAGE][4]%>",
                            "sSearch": "<%=dataTableTitle[SESS_LANGUAGE][5]%>",
                            "oPaginate": {
                                "sFirst ": "<%=dataTableTitle[SESS_LANGUAGE][6]%>",
                                "sLast ":  "<%=dataTableTitle[SESS_LANGUAGE][7]%>",
                                "sNext ":  "<%=dataTableTitle[SESS_LANGUAGE][8]%>",
                                "sPrevious ":   "<%=dataTableTitle[SESS_LANGUAGE][9]%>"
                            }
                        },
                        "bServerSide": true,
                        "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FILTER=" + datafilter + "&FRM_FIELD_DATA_FOR=" + dataFor 
                                + "&privUpdate=" + privUpdate + "&FRM_FLD_APP_ROOT=<%=approot%>" + "&SEND_STATUS_DOC=" + status,
                        aoColumnDefs: [
                            {
                                bSortable: false,
                                aTargets: [-1]
                            }
                        ],
                        "initComplete": function (settings, json) {
                            if (callBackDataTables !== null) {
                                callBackDataTables();
                            }
                        },
                        "fnDrawCallback": function (oSettings) {
                            if (callBackDataTables !== null) {
                                callBackDataTables();
                            }
                            $('#tableDispatchTableElement').find(".money").each(function () {
                                jMoney(this);
                            });
                        },
                        "fnPageChange": function (oSettings) {

                        }
                    });
                    $(elementIdParent).find("#" + elementId + "_filter").find("input").addClass("form-control");
                    $(elementIdParent).find("#" + elementId + "_length").find("select").addClass("form-control");
                    $("#" + elementId).css("width", "100%");
                }

                function runDataTable(status) {
                    dataTablesOptions("#dispatchTableElement", "tableDispatchTableElement", "AjaxDispatch", "listDispatch", null, status);
                }
                //MODAL SETTING
                var modalSetting = function (elementId, backdrop, keyboard, show) {
                    $(elementId).modal({
                        backdrop: backdrop,
                        keyboard: keyboard,
                        show: show
                    });
                };
                
                var status = $('#doc_status').val();
                runDataTable(status);

                $('#doc_status').change(function () {
                    var newStatus = $(this).val();
                    runDataTable(newStatus);
                });

      });
    </script>
  </head>
  <body>
    <section class="content-header">
      <h1>Transfer<small> Pencarian</small> </h1>
      <ol class="breadcrumb">
        <li>Transfer</li>
        <li>Pencarian</li>
      </ol>
    </section>
    <section class="content">
      <div class="box box-prochain">
        <div class="box-header with-border">
          <label class="box-title pull-left">Pencarian</label>
        </div>
        <div class="box-body">
          <form name="FrmAppUser" method="post" action="">
            <input type="hidden" name="command" value="">
            <div class="row">
              <div class="col-md-12">
                
                <div class="col-md-6">
                  <div class="form-group">
                    <label class="col-sm-4">Nomor</label>
                    <div class="input-group col-sm-8">
                      <input type="text" class="form-control input-sm"  value="" placeholder="Nomor Transfer">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4">Lokasi Asal</label>
                    <div class="input-group col-sm-8">
                      <select class="form-control input-sm select2" name="LOCATION_ASAL">
                        <%
                          for (int i = 0; i < listLocation.size(); i++) {
                            Location loc = (Location) listLocation.get(i);
                            long oidLocation = loc.getOID();
                            String selected = "";
                            %>
                        <option value="<%=loc.getOID()%>" <%=selected %>><%=loc.getName()%></option>
                        <%}
                        %>
                      </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4">Lokasi Tujuan</label>
                    <div class="input-group col-sm-8">
                      <select class="form-control input-sm select2" name="LOCATION_TUJUAN">
                        <%
                          for (int i = 0; i < listLocation.size(); i++) {
                            Location loc = (Location) listLocation.get(i);
                            long oidLocation = loc.getOID();
                            String selected = "";
                            %>
                        <option value="<%=loc.getOID()%>" <%=selected %>><%=loc.getName()%></option>
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
                        <input type="text" class="form-control input-sm datePicker" style="width: 40%;"  value="" placeholder="semua tanggal">
                      </div>
                      <span class="input-group-addon" style="float: left;height: 30px;width: 20%;border-radius: 0px !important;"> s/d </span>
                      <div class="bootstrap-timepicker">           
                        <input type="text" class="form-control input-sm datePicker" style="width: 40%;" value="" placeholder="semua tanggal">
                      </div>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4">Urut Berdasar</label>
                    <div class="input-group col-sm-8">
                      <select class="form-control input-sm select2" name="SORT_BY" >
                        <%
                            for (int i = 0; i < SrcMatDispatch.textListSortByKey.length; i++) {
                           long oidSort = Long.parseLong(SrcMatDispatch.textListSortByValue[i]);
                            String selected = "";
                            %>
                        <option value="<%=SrcMatDispatch.textListSortByValue[i] %>"><%=SrcMatDispatch.textListSortByKey[i] %></option>
                        <%}
                        %>
                      </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4">Status</label>
                    <div class="input-group col-sm-8">
                      <select class="form-control input-sm select2" name="STATUS" multiple="" id="doc_status">
                        <%
                          for (int i = 0; i < listStatus.size(); i++) {
                            Vector temp = (Vector) listStatus.get(i);
                            int indexPrStatus = Integer.parseInt(String.valueOf(temp.get(0)));
                            String strPrStatus = String.valueOf(temp.get(1));
                            String selected = "";
                            %>
                        <option value="<%=indexPrStatus %>" <%=selected %>><%=strPrStatus%></option>
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
            <button type="button" class="btn btn-prochain" id="cari-transfer"><i class="fa fa-search"> </i> Cari</button>
            <button type="button" class="btn btn-primary" id="tambah-transfer"><i class="fa fa-plus"> </i> Tambah</button>
          </div>
        </div>
      </div>
      <div class="box box-prochain">
        <div class="box-header with-border">
          <label class="box-title pull-left">List Transfer</label>
        </div>
        <div class="box-body">
          <div class="row">
            <div class="col-md-12">
              <div id="dispatchTableElement">
                <table class="table table-striped table-bordered table-responsive" style="font-size: 14px">
                    <thead>
                        <tr>
                            <th>No.</th>                                    
                            <th>Nomor Transfer</th>
                            <th>Tanggal</th>
                            <th>Lokasi Asal</th>
                            <th>Lokasi Tujuan</th>
                            <th>Status</th>
                            <th>Keterangan</th>
                            <th class="aksi">Aksi</th>
                        </tr>
                    </thead>
                </table>
            </div>
          </div>
          </div>
        </div>
        <div class="box-footer">

        </div>
      </div>
    </section>
    
  </body>
</html>
