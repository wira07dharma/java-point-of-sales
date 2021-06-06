<%-- 
    Document   : add_katalog
    Created on : Nov 23, 2019, 8:47:13 AM
    Author     : Sunima
--%>

<%@page import="com.dimata.posbo.entity.marketing.MarketingCatalog"%>
<%@page import="com.dimata.posbo.entity.marketing.PstMarketingCatalog"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../main/javainit.jsp" %>
<%!
 public static final String dataTableTitle[][] = {
        {
            "Tampilkan _MENU_ data per halaman",
            "Data Tidak Ditemukan",
            "Menampilkan halaman _PAGE_ dari _PAGES_",
            "Belum Ada Data",
            "(Disaring dari _MAX_ data)",
            "Pencarian :",
            "Awal",
            "Akhir",
            "Berikutnya",
            "Sebelumnya"
        }, {
            "Display _MENU_ records per page",
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
<%  int command = FRMQueryString.requestCommand(request);
  long oidCatalog = FRMQueryString.requestLong(request, "oid");
  String title = "", startDate = "", endDate = "";
  int status = 0;
  if (command == Command.EDIT) {
    MarketingCatalog marketingCatalog = new MarketingCatalog();
    marketingCatalog = PstMarketingCatalog.fetchExc(oidCatalog);
    title = marketingCatalog.getMarketing_katalog_title();
    startDate = Formater.formatDate(marketingCatalog.getMarketing_katalog_start_date(), "yyyy-MM-dd");
    endDate = Formater.formatDate(marketingCatalog.getMarketing_katalog_end_date(), "yyyy-MM-dd");
    status = marketingCatalog.getMarketing_katalog_status();
  }


%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Marketing Katalog</title>

    <link rel="stylesheet" type="text/css" href="../styles/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="../styles/bootstrap/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" type="text/css" href="../styles/jquery-ui-1.12.1/jquery-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="../styles/dist/css/AdminLTE.css"/>
    <link rel="stylesheet" type="text/css" href="../styles/dist/css/skins/_all-skins.css"/>
    <link rel="stylesheet" type="text/css" href="../styles/font-awesome/font-awesome.css"/>
    <link rel="stylesheet" type="text/css" href="../styles/iCheck/flat/blue.css"/>
    <link rel="stylesheet" type="text/css" href="../styles/iCheck/all.css"/>
    <link rel="stylesheet" type="text/css" href="../styles/select2/css/select2.min.css"/>
    <link rel="stylesheet" type="text/css" href="../styles/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" type="text/css" href="../styles/plugin/datatables/dataTables.bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="../styles/bootstrap-notify.css"/>
    <link rel="stylesheet" type="text/css" href="../styles/JavaScript-autoComplete-master/auto-complete.css"/>
    <style>
      .input-group{
        display:inherit!important;
      }
      label {
        margin-top: 15px;
      }
      #btn-add-item{
        color: #fff;
        padding: 11px 30px;
        font-weight: bold;
        border: none;
        background: #23e4af;
      }
      .btn-wrapper {
        text-align: center;
        margin-top: 30px
      }
      #form-katalog{
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0px 0px 20px #eee;
      }
      #table{
        width: 100%;
      }
      #item-menu {
        margin: 30px;
        border: 1px solid #eee;
        border-radius: 3px;
        padding: 20px;
      }
      .container {
        padding-bottom: 100px;
      }
    </style>
  </head>
  <body>
    <script type="text/javascript" src="../styles/bootstrap/js/jquery.min.js"></script>
    <script type="text/javascript" src="../styles/bootstrap/js/bootstrap.min.js"></script>  
    <script type="text/javascript" src="../styles/dimata-app.js"></script>
    <script type="text/javascript" src="../styles/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="../styles/JavaScript-autoComplete-master/auto-complete.min.js"></script>
    <script type="text/javascript" src="../styles/iCheck/icheck.min.js"></script>
    <script type="text/javascript" src="../styles/plugin/datatables/jquery.dataTables.js"></script>
    <script type="text/javascript" src="../styles/datatables/dataTables.bootstrap.js"></script>
    <script type="text/javascript" src="../styles/bootstrap-notify.js"></script>
    <script type="text/javascript" src="../styles/select2/js/select2.min.js"></script>
    <script>

    </script>
    <div class="container">
      <div class="row">
        <div class="col-md-12">
          <h3>Add Katalog</h3>
        </div>
      </div>
      <div class="row">
        <div class="col-md-12">
          <form id="form-katalog">
            <div class="row">
              <div class="col-md-12">
                <input type="hidden" id="oid_catalog" value="<%=oidCatalog%>" name="FRM_OID_CATALOG">
                <label for="input-title">Title</label>
                <div class="input-group">
                  <input type="text" class="form-control" id="input-title" name="FRM_CATALOG_TITLE" value="<%=title%>">
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-6">
                <label for="input-start">Start Date</label>
                <div class="input-group">
                  <input type="date" class="form-control" id="input-start" name="FRM_START_DATE" value="<%=startDate%>">
                </div>
              </div>
              <div class="col-md-6">
                <label for="input-end">End Date</label>
                <div class="input-group">
                  <input type="date" class="form-control" id="input-end" name="FRM_END_DATE" value="<%=endDate%>">
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-12">
                <label for="input-status">Status</label>
                <div class="input-group">
                  <select class="form-control" id="input-status" name="FRM_STATUS">
                    <option value="0">Draft</option>
                    <option value="1">Cancel</option>
                    <option value="2">Approve</option>

                  </select>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="btn-wrapper">
                <button class="btn" id="btn-add-item">Add Item</button>
              </div>

            </div>
            <div class="row">

              <div id="item-menu">
                <table id="table" class="table table-striped table-bordered" style="width:100%">
                  <thead>
                    <tr>
                      <th>No</th>
                      <th>Nama Barang</th>
                      <th>SKU</th>
                      <th>Kategori</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                  </tbody>
                </table
              </div>
            </div>
        </div>

        <div class="row">
          <div class="col-md-12 text-right">
            <button class="btn btn-save btn-primary" id="btn_save">Save</button>
            <button class="btn btn-cancel btn-danger" id="btn_cancel">Cancel</button>
          </div>
        </div>
        </form>
      </div>
    </div>
    <form name="frm_material" method="post" action=""> 
      <input type="hidden" name="command" value="">
      <input type="hidden" name="add_type" value="">			  			  			  			  
      <input type="hidden" name="start" value="0">
      <input type="hidden" name="start2" value="0">
      <input type="hidden" name="start_search" value="0">
      <input type="hidden" name="start_search2" value="0">
      <input type="hidden" name="hidden_material_id" value="0">
      <input type="hidden" name="hidden_range_code_id" value="0">
      <input type="hidden" name="hidden_type" value="0">
      <input type="hidden" name="typemenu" value="0">
      <!--for litama-->
      <input type="hidden" name="FRM_KODE_START" value="0">
      <input type="hidden" name="FRM_KODE_END" value="0">
      <input type="hidden" name="oid_catalog" value="0">
      <!---->
      <input type="hidden" name="expand_search" value="0">
      <input type="hidden" name="approval_command">
    </form>

    <!-- launch modal -->

    <div class="modal fade" id="modal-item" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
      <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <label class="modal-title">Pilih Item</label>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>

          <div class="modal-body" id="container">
            <table id="table-katalog" class="table table-striped table-bordered" style="width:100%">
              <thead>
                <tr>
                  <th>No</th>
                  <th>Nama Barang</th>
                  <th>SKU</th>
                  <th>Kategori</th>
                  <th>Action</th>
                </tr>
              </thead>
            </table>
          </div>
        </div>
      </div>
    </div>
    <script>
      $(document).ready(function () {
        // initial data table
        
        
        function runPengirimaTable() {
            var elementidparent = 'container';
            var servlet = 'AjaxMaterial';
            var dataFor = 'listMaterialCatalog';
            var elementid = 'table-katalog';
            runDataTable(elementidparent, elementid, servlet, dataFor, null);
        }
        
        runPengirimaTable();
        
        $("#btn-add-item").click(function (event) {
          event.preventDefault();
          $("#modal-item").modal("show");
        });
        var base = "<%=approot%>";

        // initialize the data table

        var getDataFunction = function (onDone, onSuccess, approot, command, dataSend, servletName, dataAppendTo, notification) {
          /*
           * getDataFor	: # FOR PROCCESS FILTER
           * onDone	: # ON DONE FUNCTION,
           * onSuccess	: # ON ON SUCCESS FUNCTION,
           * approot	: # APPLICATION ROOT,
           * dataSend	: # DATA TO SEND TO THE SERVLET,
           * servletName  : # SERVLET'S NAME
           */
          $(this).getData({
            onDone: function (data) {
              onDone(data);
            },
            onSuccess: function (data) {
              onSuccess(data);
            },
            approot: approot,
            dataSend: dataSend,
            servletName: servletName,
            dataAppendTo: dataAppendTo,
            notification: notification
          });
        };

        // DATABLE SETTING
        function dataTablesOptions(elementIdParent, elementId, servletName, dataFor, callBackDataTables) {
          var oid = $("#oid_catalog").val();
          var datafilter = "";
          var privUpdate = "";
          $(elementIdParent).find('table').addClass('table-bordered table-striped table-hover').attr({'id': elementId});
          $("#" + elementId).dataTable({
            "bDestroy": true,
            "ordering": false,
            "iDisplayLength": 10,
            "bProcessing": true,
            "oLanguage": {
              "sProcessing": "<div class='col-sm-12'><div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div></div>"
            },
            "bServerSide": true,
            "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FILTER=" + datafilter + "&FRM_FIELD_DATA_FOR=" + dataFor + "&privUpdate=" + privUpdate + "&FRM_FLD_APP_ROOT=<%=approot%>" + "&SEND_OID_KATALOG=" + oid,
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
              $(".choose").unbind("click").click(function () {
                var url = "" + base + "/AjaxMarketing";
                var oidMaterial = $(this).data("oid");
                var oidCategory = $(this).data("oid_category");
                var oid_catalog = $("#oid_catalog").val();


                var data = "command=<%=Command.SAVE%>&FRM_FIELD_DATA_FOR=saveKatalogItem&MATERIAL_OID=" + oidMaterial + "&CATEGORY_OID=" + oidCategory + "&OID_CATALOG=" + oid_catalog;

                $.ajax({
                  url: "" + url + "",
                  data: "" + data + "",
                  type: "POST",
                  dataType: "json",
                  async: false,
                  cache: false,
                  success: function (data) {

                  },
                  error: function (data) {

                  }

                }).done(function (data) {
                  $("#modal-item").modal('hide');
                  $("#oid_catalog").val(data.FRM_FIELD_HTML);
                  $("#item-menu").show();
                  runDataTable("#item-menu", "table_item_catalog", "AjaxMarketing", "list");
                });

              });
            },
            "fnPageChange": function (oSettings) {

            }
          });
          $(elementIdParent).find("#" + elementId + "_filter").find("input").addClass("form-control");
          $(elementIdParent).find("#" + elementId + "_length").find("select").addClass("form-control");
          $("#" + elementId).css("width", "100%");
          
        }

        function runDataTable(tableElement, tableName, servletName, dataFor) {
          dataTablesOptions(tableElement, tableName, servletName, dataFor, null);
        }

        // validation element
        $("#item-menu").hide();

        validasi();
        function validasi() {
          if (<%=command%> == <%=Command.EDIT%>) {
            $("#btn_save").text("Update");
            // selected Status
            $("#input-status option").each(function () {
              if ($(this).val() == "<%=status%>")
                $(this).attr("selected", "selected");
            });
            $("#item-menu").show();
            runDataTable("#item-menu", "table_item_catalog", "AjaxMarketing", "list");
          }
        }

        

        $("#btn_save").click(function (event) {
          event.preventDefault();
          var url = "" + base + "/AjaxMarketing";
          var oid_catalog = $("#oid_catalog").val();
          var mainData = $("#form-katalog").serialize();



          var data = "command=<%=Command.SAVE%>&FRM_FIELD_DATA_FOR=saveKatalog&OID_CATALOG=" + oid_catalog + "&" + mainData;

          $.ajax({
            url: "" + url + "",
            data: "" + data + "",
            type: "POST",
            dataType: "json",
            async: false,
            cache: false,
            success: function (data) {

            },
            error: function (data) {

            }

          }).done(function (data) {
            $("#modal-item").modal('hide');
            $("#oid_catalog").val(data.FRM_FIELD_HTML);
            $("#item-menu").show();
            window.location = "marketing_katalog.jsp";
          });
        });
        $("#btn_cancel").click(function (event) {
          event.preventDefault();
          var oid_catalog = $("#oid_catalog").val();
          if (<%=command%> == <%=Command.EDIT%>) {
            window.location = "marketing_katalog.jsp";
          }
          if (<%=command%> == <%=Command.ADD%>) {
            var url = "" + base + "/AjaxMarketing";

            var data = "command=<%=Command.DELETE%>&FRM_FIELD_DATA_FOR=deleteCatalog&OID_CATALOG=" + oid_catalog;

            $.ajax({
              url: "" + url + "",
              data: "" + data + "",
              type: "POST",
              dataType: "json",
              async: false,
              cache: false,
              success: function (data) {

              },
              error: function (data) {

              }

            }).done(function (data) {
              window.location = "marketing_katalog.jsp";
            });
          } else {

            window.location = "marketing_katalog.jsp";
          }
        });

        $('body').on("click", ".btn_delete_item", function () {
          var url = "" + base + "/AjaxMarketing";
          var oidCatalogItem = $(this).data("oid");


          var data = "command=<%=Command.DELETE%>&FRM_FIELD_DATA_FOR=deleteItemCatalog&ITEM_CATALOG_OID=" + oidCatalogItem;

          $.ajax({
            url: "" + url + "",
            data: "" + data + "",
            type: "POST",
            dataType: "json",
            async: false,
            cache: false,
            success: function (data) {

            },
            error: function (data) {

            }

          }).done(function (data) {
            runDataTable("#item-menu", "table_item_catalog", "AjaxMarketing", "list");
          });
        });
        $('body').on("click", ".btn_edit_item", function () {
          var oidMaterial = $(this).data("oid");
          var oidCatalog = $("#oid_catalog").val();
          cmdEdit(oidMaterial, oidCatalog);
        });


        function cmdEdit(oid, oidCatalog)
        {
          document.frm_material.hidden_material_id.value = oid;
          document.frm_material.start.value = 0;
          document.frm_material.oid_catalog.value = oidCatalog;
          document.frm_material.approval_command.value = "<%=Command.APPROVE%>";
          document.frm_material.command.value = "<%=Command.EDIT%>";
          document.frm_material.typemenu.value = "0";
          document.frm_material.action = "<%=approot%>/master/material/material_main.jsp?source=katalog";
          document.frm_material.submit();
        }

      });
    </script>
  </body>
</html>
