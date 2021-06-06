<%-- 
    Document   : dokumen_pengiriman
    Created on : Mar 23, 2020, 5:09:20 PM
    Author     : WiraDharma
--%>

<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="org.json.JSONArray"%>
<%@page import="com.dimata.harisma.entity.employee.PstEmployee"%>
<%@page import="com.dimata.services.WebServices"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.dimata.util.Command"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%!    public static final String dataTableTitle[][] = {
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
<%    
    String oidDelivery = PstSystemProperty.getValueByName("STAFF_DELIVERY_OID");
    String urlAppSedana = PstSystemProperty.getValueByName("SEDANA_APP_URL");
    String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
    String arrayNasabah[] = request.getParameterValues("petugas_delivery");
    System.out.println("Data : " + arrayNasabah);
    String nasabah = "";
    String addSql = "";
    long id = 0;
    JSONObject temp = new JSONObject();
    if (arrayNasabah != null) {
        String idNasabah = "";
        for (int i = 0; i < arrayNasabah.length; i++) {
            if (i > 0) {
                idNasabah += ",";
                nasabah += ", ";
            }
            idNasabah += "" + arrayNasabah[i];
            String urlEmp = hrApiUrl + "/employee/employee-fetch/" + Long.valueOf(arrayNasabah[i]);
            JSONObject jo = WebServices.getAPI("", urlEmp);
            if (jo.length() > 0) {
                nasabah = jo.optString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]);
                id = jo.optLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]);
            }
            try {
                temp.put("id", id);
            } catch (Exception e) {
            }
        }
        if (!idNasabah.equals("")) {
            addSql += " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DO_PERSON_ID] + " IN (" + idNasabah + ")";
        }
    } else {
        nasabah = "Semua Delivery";
    }
%>    
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
            .form-group {
                margin-bottom: 0px;
            }
            .input-group-addon {
                padding: 6px 12px;
                font-size: 14px;
                font-weight: 400;
                line-height: 1;
                color: #555;
                text-align: center;
                background-color: #eee;
                border: 1px solid #e4e4e4 !important;
            }

            form#frmDokumenPengiriman {
                margin-top: 10px;
            }

            print-area { visibility: hidden; display: none; }
            print-area.preview { visibility: visible; display: block; }
            @media print
            {
                body .main-page * { visibility: hidden; display: none; } 
                body print-area * { visibility: visible; }
                body print-area   { visibility: visible; display: unset !important; position: static; top: 0; left: 0; }
            }
        </style>
        <script>
            $(document).ready(function () {
                let nasabahJarr = JSON.parse('<%= temp.toString()%>');
                let placeholderNasabah = JSON.parse('{ "id": "0", "text": "-- Semua Delivery --"}');
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
                function dataTablesOptions(elementIdParent, elementId, servletName, dataFor, callBackDataTables) {
                    var datafilter = "";
                    var privUpdate = "";
                    var searchForm = $('#frmDokumenPengiriman');
                    $(elementIdParent).find('table').addClass('table-bordered table-striped table-hover').attr({'id': elementId});
                    $("#" + elementId).dataTable({
                        "bDestroy": true,
                        "ordering": false,
                        "iDisplayLength": 10,
                        "bProcessing": true,
                        //  "info":     true,
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
                        "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FILTER=" + datafilter + "&FRM_FIELD_DATA_FOR=" + dataFor + "&privUpdate=" + privUpdate + "&FRM_FLD_APP_ROOT=<%=approot%>" + "&SEND_USER_ID=<%=userId%>&" + searchForm.serialize()+"&ADD_SQL=<%=addSql%>",
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

                function runDataTable(elementidparent, elementid, servlet, dataFor, callBack) {
                    dataTablesOptions("#" + elementidparent, elementid, servlet, dataFor, callBack);
                }

                function runDokuemenProduksiTable() {
                    var elementidparent = 'requestTableElement';
                    var servlet = 'AjaxProduksi';
                    var dataFor = 'listDokumenPengiriman';
                    var elementid = 'listDokumenPengiriman';
                    runDataTable(elementidparent, elementid, servlet, dataFor, null);
                }
                runDokuemenProduksiTable();

                //        Date Picker
                $('.datePicker').datetimepicker({
                    format: "yyyy-mm-dd",
                    todayBtn: true,
                    autoclose: true,
                    minView: 2
                });

                $('body').on('click', '#search-form-btn', function () {
                    runDokuemenProduksiTable();
                });

                $('.select2').select2({placeholder: "Semua"});
                $('.selectAll').select2({placeholder: "Semua"});

                $('body').on('click', '.dokumen-produksi-btn', function () {
                    var oid = $(this).data('oid');
                    window.open("<%= approot%>/PrintDocumentProduction?approot=<%= approot%>&sess_language=<%= SESS_LANGUAGE%>&bill_main_oid=" + oid);
                });

                $('body').on('click', '.dokumen-kw-btn', function () {
                    var oid = $(this).data('oid');
                    var pinjamanId = $(this).data('pinjaman');
                    var docType = $(this).data('doc-type');
                    var sedanaAppUrl = "<%= urlAppSedana%>";
                    var url = sedanaAppUrl + "/masterdata/masterdokumen/dokumen_edit.jsp?oid_pinjaman=" + pinjamanId + "&type=" + docType;
                    window.open(url, '_blank', 'location=yes,height=650,width=1000,scrollbars=yes,status=yes');
                });


                function selectMulti(id, placeholder) {
                    $(id).select2({
                        placeholder: placeholder
                    });
                }

                function setDeliveryValue() {
                    let newOption;
                    if (!jQuery.isEmptyObject(nasabahJarr)) {
                        newOption = new Option(nasabahJarr.text, nasabahJarr.id, true, true);
                    } else {
                        newOption = new Option(placeholderNasabah.text, placeholderNasabah.id, true, true);
                    }
                    $('#id_nasabah').append(newOption).trigger('change');
                }

                selectMulti('.select2delivery', "Semua Delivery");
                setDeliveryValue();

                $('#id_nasabah').select2({
                    placeholder: placeholderNasabah,
                    ajax: {
                        url: "<%= approot%>/AjaxProduksi?command=<%= Command.LIST%>&FRM_FIELD_DATA_FOR=select2Delivery",
                        type: "POST",
                        dataType: 'json',
                        delay: 250,
                        data: function (params) {
                            return {
                                searchTerm: params.term,
                                limitStart: params.limitStart || 0,
                                recordToGet: 7
                            };
                        },
                        processResults: function (data, params) {
                            var ph = data.PAGINATION_HEADER;

                            params.limitStart = ph.CURRENT_PAGE || 0;
                            return {
                                results: data.ANGGOTA_DATA,
                                pagination: {
                                    more: ph.CURRENT_PAGE < data.ANGGOTA_TOTAL
                                }
                            };
                        },
                        cache: true
                    }
                });

            });

        </script>
    </head>
    <body>
        <div class="main-page">
            <section class="content-header">
                <h1>Produksi<small> Pengiriman</small> </h1>
                <ol class="breadcrumb">
                    <li>Dokumen</li>
                    <li>Pengiriman</li>
                </ol>
            </section>
            <section class="content">
                <div class="box box-prochain">
                    <form name="frmDokumenPengiriman" id="frmDokumenPengiriman" method="post" action="" class="form-horizontal">
                        <input type="hidden" name="userId" value="<%=userId%>">
                        <div class="row">
                            <div class="col-md-12">

                                <div class="col-md-8">
                                    <div class=" col-sm-3">
                                        <div class="form-group">
                                            <input type="text" name="no_invoice" id="no_invoice" class="form-control input-sm" placeholder="No PK / No Invoice">
                                        </div>
                                    </div>
                                    <div class=" col-sm-3">
                                        <div class="form-group">
                                            <div class="input-group col-md-12" style="padding-left: 15px; padding-right: 15px">
                                                <select id="id_nasabah" class="form-control input-sm" name="petugas_delivery"></select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class=" col-sm-6">
                                        <div class="form-group">
                                            <div class="input-group col-md-12" style="padding-left: 15px; padding-right: 15px">
                                                <div class="bootstrap-timepicker">                                              
                                                    <input type="text" class="form-control input-sm datePicker" name="start_date" style="width: 40%;"  value="<%= Formater.formatDate(new Date(), "yyyy-MM-dd")%>">
                                                </div>
                                                <span class="input-group-addon" style="float: left;height: 30px;width: 20%;border-radius: 0px !important;"> s/d </span>
                                                <div class="bootstrap-timepicker">           
                                                    <input type="text" class="form-control input-sm datePicker" name="end_date" style="width: 40%;" value="<%= Formater.formatDate(new Date(), "yyyy-MM-dd")%>">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="pull-right">
                                        <button type="button" class="btn btn-prochain" id="search-form-btn"><i class="fa fa-search"> </i> Cari Data</button>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </form>
                    <div class="box-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div id="requestTableElement">
                                    <table id="listDokumenPengiriman" class="table table-striped table-bordered table-responsive" style="font-size: 14px">
                                        <thead>
                                            <tr>
                                                <th>No.</th>                            
                                                <th>No Kredit/Invoice</th>
                                                <th>Petugas Delivery</th>
                                                <th>Tanggal Pengiriman</th>
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
        </div>
    <print-area>
        <div style="size: A4" id="printOut" class="">
        </div>
    </print-area>
</body>
</html>



















