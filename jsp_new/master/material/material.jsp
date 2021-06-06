<%-- 
    Document   : material
    Created on : Mar 3, 2020, 10:02:33 AM
    Author     : Wiradarma
--%>

<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
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
<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);%>
<%@ include file = "../../main/checkuser.jsp" %>
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
    long oid = FRMQueryString.requestLong(request, "FRM_FIELD_OID");
    long oidLocation = FRMQueryString.requestLong(request, "FRM_FIELD_LOCATION");
    String number = FRMQueryString.requestString(request, "FRM_FIELD_PRMNUMBER");
    String tglAwal = FRMQueryString.requestString(request, "FRM_FIELD_PRMDATEFROM");
    String tglAkhir = FRMQueryString.requestString(request, "FRM_FIELD_PRMDATETO");
    String sortBy = FRMQueryString.requestString(request, "FRM_FIELD_SORTBY");
    String[] status =  FRMQueryString.requestStringValues(request, "FRM_FIELD_PRMSTATUS");
    String vendorOId = PstSystemProperty.getValueByName("VENDOR_OID");
  int start = 0; 
  int record = 0; 
  String whereClause = ""; 
  String order = ""; 
  
  Vector <Location> listLocation = PstLocation.list(start, record, whereClause, order);
  Vector <Category> listCategory = PstCategory.list(start, record, whereClause, order);
  String whereVendor = PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID]+" = "+vendorOId;
    Vector <ContactClassAssign> listVendor = PstContactClassAssign.list(start, record, whereVendor, order);
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dimata - ProChain POS</title>
        <%@include file="../../styles/plugin_component.jsp" %>
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
    <script language="javascript">
            $(document).ready(function () {

                var searchResBox = $('#search-result-box');
                var approot = "<%= approot%>";
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
                function dataTablesOptions(elementIdParent, elementId, servletName, dataFor, callBackDataTables, oid) {
                    var datafilter = "";
                    var privUpdate = "";
                    var searchForm = $('#frmMaterial');
                    $(elementIdParent).find('table').addClass('table-bordered table-striped table-hover').attr({'id': elementId});
                    $("#" + elementId).dataTable({
                        "bDestroy": true,
                        "ordering": false,
                        "iDisplayLength": 25,
                        "bProcessing": true,
                        "info": false,

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
                                "sLast ": "<%=dataTableTitle[SESS_LANGUAGE][7]%>",
                                "sNext ": "<%=dataTableTitle[SESS_LANGUAGE][8]%>",
                                "sPrevious ": "<%=dataTableTitle[SESS_LANGUAGE][9]%>"
                            }
                        },
                        "bServerSide": true,
                        "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FILTER=" + datafilter + "&FRM_FIELD_DATA_FOR=" + dataFor + "&privUpdate=" + privUpdate + "&FRM_FLD_APP_ROOT=<%=approot%>" + "&oid=" + oid+ "&" + searchForm.serialize(),
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
                        },
                        "fnPageChange": function (oSettings) {

                        }
                    });
                    $(elementIdParent).find("#" + elementId + "_filter").find("input").addClass("form-control");
                    $(elementIdParent).find("#" + elementId + "_length").find("select").addClass("form-control");
                    $("#" + elementId).css("width", "100%");
                }

                function runDataTable() {
                    dataTablesOptions("#materialtableElement", "tableMaterialtableElement", "AjaxMaterial", "listMaterial", null, 0);
                }
                
                runDataTable();
                
                $('body').on('click', '#search-form-btn', function () {
                    searchResBox.show();
                    runDataTable();
                });
                
                //MODAL SETTING
                var modalSetting = function (elementId, backdrop, keyboard, show) {
                    $(elementId).modal({
                        backdrop: backdrop,
                        keyboard: keyboard,
                        show: show
                    });
                };
                
                $('#tambah-barang').click(function(){
                    var url = "material_main.jsp";
                    window.location = url+"?command=<%=Command.ADD%>";
                });
                
                $('#export-Excel').click(function(){
                     window.open("material_list_excel.jsp?tms=<%=System.currentTimeMillis()%>&iCommand=14","sale","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                });
                
                $('#print-pdf').click(function(){
                    window.open("<%=approot%>/servlet/com.dimata.posbo.report.masterdata.PrintAllListPdf?tms=<%=System.currentTimeMillis()%>&","sale","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                });
                
                $('#print-list').click(function(){
                    cmdPrintAll();
                });
                
                $('#import-barang').click(function(){
                    var url = "import_excel_material.jsp";
                    window.location = url;
                });
                
                $('#print-price').click(function(){
                   var strvalue = "<%=approot%>" + "/masterdata/barcode.jsp?command=<%=Command.ADD%>";
                    winSrcMaterial = window.open(strvalue, "searchtipeharga", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                    if (window.focus) {
                        winSrcMaterial.focus();
                    }
                });
                
		$('body').on('click', '.edit-material', function(){
                    var oid = $(this).data('oid');
                    var url = "material_main.jsp";
                    window.location = url+"?command=<%=Command.EDIT%>&hidden_material_id="+oid+"&start="+0+"&approval_command=<%=Command.APPROVE%>&typemenu="+0;
                });
                
                //Select2
                $('.select2').select2({placeholder: "Semua"});
                $('.selectAll').select2({placeholder: "Semua"});
                
        //        Date Picker
                $('.datePicker').datetimepicker({
                    format: "yyyy-mm-dd",
                    todayBtn: true,
                    autoclose: true,
                    minView: 2
                });
                function cmdPrintAll(){
                    document.frmMaterial.command.value="<%=Command.LIST%>";
                    document.frmMaterial.target = "print_catalog";
                    document.frmMaterial.action="material_list_print.jsp";
                    document.frmMaterial.submit();
                }

                function cmdPrintAllWithoutPrice(){
                    document.frmMaterial.command.value="<%=Command.LIST%>";
                    document.frmMaterial.target = "print_catalog";
                    document.frmMaterial.action="material_list_print_without_price.jsp";
                    document.frmMaterial.submit();
                }

                function printForm() {
                    window.open("<%=approot%>/servlet/com.dimata.posbo.report.masterdata.PrintAllListWoPricePdf?tms=<%=System.currentTimeMillis()%>&","sale","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                        //window.open("reportsaleinvoice_form_print.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                }
            });
        </script>
    </head>
  <body>
    <section class="content-header">
      <h3>Material Menu<small> Pencarian</small> </h3>
      <ol class="breadcrumb">
        <li>Master</li>
        <li>Material</li>
      </ol>
    </section>
    <section class="content">
      <div class="box box-prochain">
        <div class="box-header with-border">
          <label class="box-title pull-left">Pencarian</label>
        </div>
        <div class="box-body">
            <form class="form-horizontal" name="frmMaterial" id="frmMaterial" method="post" action="">
              <input type="hidden" name="command" value="<%=Command.LIST%>">
            <div class="row">
              <div class="col-md-12">
                <div class="col-md-6">
                    <div class="form-group">
                    <label class="col-sm-4">Nomor SKU</label>
                    <div class="col-sm-8">
                        <input type="text" autocomplete="off" required="" class="form-control input-sm" name="form_nomor">
                    </div>
                    </div>
                    <div class="form-group">
                    <label class="col-sm-4">Barcode</label>
                    <div class="col-sm-8">
                        <input type="text" autocomplete="off" required="" class="form-control input-sm" name="form_barcode">
                    </div>
                    </div>
                    <div class="form-group">
                    <label class="col-sm-4">Nama Item</label>
                    <div class="col-sm-8">
                        <input type="text" autocomplete="off" required="" class="form-control input-sm" name="form_nama">
                    </div>
                    </div>
                  <div class="form-group">
                    <label class="col-sm-4">Supplier</label>
                    <div class="col-sm-8">
                      <select class="form-control input-sm select2" name="form_supplier">
                        <option value="0">Semua</option>
                        <%
                          for (ContactClassAssign con : listVendor) {
                            ContactList col = new ContactList();
                            try {
                                col = PstContactList.fetchExc(con.getContactId());
                              } catch (Exception e) {
                              }
                        %>
                        <option value="<%=col.getOID()%>"><%=col.getCompName()%></option>
                        <%}
                        %>
                      </select>
                    </div>
                  </div>
                </div>
                
                <div class="col-md-6">
                  <div class="form-group">
                    <label class="col-sm-4">Tanggal</label>
                    <div class="input-group col-sm-8" style="padding-left: 15px;padding-right: 15px;">
                    <input type="text" autocomplete="off" style="float: left;width: 45%;" required="" class="form-control datePicker" aria-describedby="basic-addon2" name="start_date" value="" placeholder="semua tanggal">
                      <span class="input-group-addon" id="basic-addon2" style="float: left;height: 34px;text-align: center;width: 10%;padding: 8px 0px 0px 0px;display: inline-block;">s/d</span>
                    <input type="text" autocomplete="off" style="float: left;width: 45%;" required="" class="form-control datePicker" aria-describedby="basic-addon2" name="end_date" value="" placeholder="semua tanggal">
                    </div>
                  </div>

                  <div class="form-group">
                    <label class="col-sm-4">Urut Berdasar</label>
                    <div class="col-sm-8">
                        <select class="form-control input-sm select2" name="order_by" >
                            <option value="-1">Semua</option>
                        <%
                            for (int i = 0; i < Material.orderBy.length; i++) {
                           long oidSort = Long.parseLong(Material.orderByValue[i]);
                            String selected = "";
                            %>
                        <option value="<%=Material.orderByValue[i] %>" <%=Material.orderByValue[i]  == sortBy ? "selected" : ""%>><%=Material.orderBy[i] %></option>
                        <%}
                        %>
                      </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4">Lokasi Jual</label>
                    <div class="col-sm-8">
                        <select class="form-control input-sm select2" multiple="" name="form_location_sell">
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
                  <div class="form-group">
                    <label class="col-sm-4">Category</label>
                    <div class="col-sm-8">
                      <select class="form-control input-sm select2" name="form_category">
                        <option value="0">Semua</option>
                        <%
                          for (Category cat : listCategory) {
                        %>
                        <option value="<%=cat.getOID()%>"><%=cat.getName()%></option>
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
          <div class="pull-right">
            <button class="btn btn-prochain" id="search-form-btn"><i class="fa fa-search"> </i> Cari Data</button>
            <button type="button" class="btn btn-primary" id="tambah-barang"><i class="fa fa-plus"> </i> Barang</button>
          </div>
        </div>
      </div>
         <br>             
         <br>             
       <div id="search-result-box">         
            <div class="box box-outline box-prochain">
                <div class="box-header with-border">
                  <div class="pull-right">
                    <button type="button" class="btn btn-default" id="print-list"><i class="fa fa-print"> </i> Print List</button>
                    <button type="button" class="btn btn-default" id="export-Excel"><i class="fa fa-file-archive-o"> </i> Export Excel</button>
                    <button type="button" class="btn btn-default" id="print-pdf"><i class="fa fa-print"> </i> Print PDF</button>
                    <button type="button" class="btn btn-default" id="import-barang"><i class="fa fa-file-archive-o"> </i> Import</button>
                    <button type="button" class="btn btn-default" id="print-price"><i class="fa fa-print"> </i> Print Price Tag</button>
                  </div>
                </div>
                <div class="box-body">
                    <div class="row">
                        <div class="col-md-12">
                            <div id="materialtableElement">
                                <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important">
                                    <thead>
                                        <tr>
                                            <th>No</th>
                                            <th>SKU</th>
                                            <th>Barcode</th>
                                            <th>Name</th>
                                            <th>Merk</th>
                                            <th>Category</th>
                                            <th>Unit</th>
                                            <th>Average HPP</th>
                                            <th>Harga Beli Terakhir</th>
                                            <th>Harga Jual</th>
                                            <th>Action</th>
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
        </div>
    </section>

  </body>
</html>
