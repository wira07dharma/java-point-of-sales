<%@page import="com.dimata.marketing.form.marketingpromotiondetailobject.FrmMarketingPromotionDetailObject"%>
<%@page import="com.dimata.marketing.form.marketingpromotiondetailsubject.FrmMarketingPromotionDetailSubject"%>
<%@page import="com.dimata.marketing.form.marketingpromotiondetail.FrmMarketingPromotionDetail"%>
<%@page import="com.dimata.marketing.form.marketingpromotionpricetype.FrmMarketingPromotionPriceType"%>
<%@page import="com.dimata.marketing.form.marketingpromotionmembertype.FrmMarketingPromotionMemberType"%>
<%@page import="com.dimata.marketing.form.marketingpromotionlocation.FrmMarketingPromotionLocation"%>
<%@page import="com.dimata.marketing.form.marketingpromotion.FrmMarketingPromotion"%>
<%@page import="com.dimata.marketing.form.marketingnewsinfo.FrmMarketingNewsInfo"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ page language = "java" %>
<%@ page import = "java.util.*" %>
<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ include file = "../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_REPORT_BY_SUPPLIER);%>
<%@ include file = "../main/checkuser.jsp" %>

<html>
    <head>
        <title>Dimata - ProChain POS</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
        <%if (menuUsed == MENU_ICON) {%>
        <link href="../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>

        <link rel="stylesheet" href="../styles/tab.css" type="text/css">

    </head> 
    <%

    %>
    <body class="" style="background-color:beige">
        <div class="nonprint">
            <table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FCFDEC" height="100%"g>
                <%if (menuUsed == MENU_PER_TRANS) {%>
                <tr>
                    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
                        <%@ include file = "../main/header.jsp" %>
                    </td>
                </tr>
                <tr>
                    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                        <%@ include file = "../main/mnmain.jsp" %>
                        <!-- #EndEditable --> </td>
                </tr>
                <%} else {%>
                <tr bgcolor="#FFFFFF">
                    <td height="10" ID="MAINMENU">
                        <%@include file="../styletemplate/template_header.jsp" %>
                    </td>
                </tr>
                <%}%>
                <tr>
                <link rel="stylesheet" type="text/css" href="../styles/bootstrap/css/bootstrap.min.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/bootstrap/css/bootstrap-theme.min.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/jquery-ui-1.12.1/jquery-ui.min.css"/>
                <!--link rel="stylesheet" type="text/css" href="../styles/jquery-ui-1.12.1/jquery-ui.theme.min.css"/-->
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
                    .large {
                        width: 99%;
                    }

                    body {
                        //overflow-y: hidden;
                    }

                    .modal {
                        overflow-y: auto;
                    }

                    th {
                        background-color: lightgrey;
                    }

                    /*.panel-heading:hover * {
                        //background-color: #dfdfdf !important;
                    }*/

                    .table, .panel {
                        font-size: 12px;
                    }

                    .modal-header{
                        padding-left: 20px;
                        padding-right: 20px;
                        padding-top: 5px;
                        padding-bottom: 5px; 
                        border-bottom-color: lightgray;
                        background-color: #eee;
                    }

                    .modal-body {
                        padding-top: 10px;
                        padding-left: 10px;
                        padding-right: 10px;
                        padding-bottom: 0px;
                        font-size: 13px;
                    }

                    .modal-footer {
                        margin-top: 0px;
                        padding-top: 10px;
                        padding-bottom: 10px; 
                        border-top-color: lightgrey;
                        background-color: #eee;
                    }

                    .panel-group {
                        margin-bottom: 10px;
                    }

                    form {
                        margin-bottom: 5px;
                    }

                    ul.ui-autocomplete.ui-menu {
                        z-index: 9999;
                    }

                    hr {                        
                        border-color: lightgray;
                    }

                    a:hover,a:focus {
                        color: #333;
                    }

                    .select2-selection__rendered{
                        //padding-bottom: 5px;
                    }

                    .select2-selection__choice{
                        margin-bottom: 5px;
                    }

                    .btn-deletepromo {
                        padding-top:10px;
                        padding-right:15px;
                        padding-bottom:11px;
                        padding-left:10px
                    }

                    /*.nonprint{
                        display: none;
                        visibility: hidden;
                    }*/

                </style>
                <td valign="top" align="left"> 

                    <div class="row" style="margin-left:0px; margin-right:0px; margin-top:16px; ">
                        <div id="promotionpage" class="col-md-12">
                            <div class="box box-primary">
                                <div class="box-header">
                                    <h3 class="box-title">Marketing Promotion</h3>
                                </div>
                                
                                <div class='box-footer'>
                                    <button class="btn btn-primary btn-addevent btn-sm" data-oid="0" data-for="showform" data-command="<%= Command.NONE%>"><i class="fa fa-plus"></i> New Promotion</button>
                                    <button class="btn btn-primary btn-showformsearch btn-sm pull-right" data-oid="0" data-for="showformsearch" data-command="<%= Command.NONE%>"><i class="fa fa-search"></i> Search by</button>
                                </div>
                                
                                <div class="box-body">
                                    <div id="marketingPromotionElement" class="table-responsive">
                                        <table class="table table-bordered table-striped">
                                            <thead>
                                                <tr>
                                                    <th>No.</th>
                                                    <th>Purposed on</th>
                                                    <th>Objectives</th>
                                                    <th>Event</th>
                                                    <th>Start on</th>  
                                                    <th>End on</th>
                                                    <th>Recurring</th>
                                                    <th>Standard Rate</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>    
                                        </table>
                                    </div>
                                </div>
                                
                            </div>
                        </div>
                    </div>
                </td>
                </tr>
                <tr> 
                    <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
                        <%if (menuUsed == MENU_ICON) {%>
                        <%@include file="../styletemplate/footer.jsp" %>

                        <%} else {%>
                        <%@ include file = "../main/footer.jsp" %>
                        <%}%>
                        <!-- #EndEditable --> </td>
                </tr>
            </table>
        </div>

        <div id="modalevent" class="modal fade" >
            <div class="modal-dialog large">
                <div class="modal-content">
                    <div class="modal-header">
                        <div class="form-inline">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modalevent-title"></h4>
                        </div>
                        <div class="">
                            <label>oid : </label><label style="color: red" id="setoidgeneral"></label>
                            <label>oid-event : </label><label style="color: red" id="setoidevent"></label>
                            <label>oid-promo : </label><label style="color: red" id="setoidpromo"></label>
                            <label>oid-promoitem : </label><label style="color: red" id="setoidpromoitem"></label>
                            <label>oid-subject : </label><label style="color: red" id="setoidsubject"></label>
                            <label>oid-object : </label><label style="color: red" id="setoidobject"></label>
                            <label>datafor : </label><label style="color: red" id="setdatafor"></label>
                            <label>command : </label><label style="color: red" id="setcommand"></label>
                        </div>
                    </div>
                    <!--form id="generalform" enctype="multipart/form-data"-->
                    <input type="hidden" name="FRM_FIELD_DATA_FOR" id="generaldatafor">
                    <input type="hidden" name="command" id="generalcommand"
                           <input type="hidden" name="FRM_FIELD_OID" id="generaloid">
                    <div class="modal-body ">
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="box-body modalevent-body">

                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" data-dismiss="modal" class="btn btn-box-tool btn-sm" style="font-weight: bold; color: black">Close</button>
                    </div>
                    <!--/form-->
                </div>
            </div>
        </div>

        <div id="modalpromotype" class="modal fade" tabindex="-1">
            <div class="modal-dialog modal-sm">
                <div class="modal-content">
                    <div class="modal-header">
                        <div class="form-inline">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modalpromotype-title"></h4>
                        </div>
                    </div>
                    <form id="formpromotype" enctype="multipart/form-data">                        
                        <div class="modal-body ">
                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="box-body modalpromotype-body">

                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary btn-sm" id="btn-savepromotype" data-oid="0" data-for="savepromotype" data-command="<%= Command.SAVE%>"><i class="fa fa-plus"></i></button>
                            <button type="button" data-dismiss="modal" class="btn btn-danger btn-sm"><i class="fa fa-close"></i></button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div id="modalsearch" class="modal fade" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <div class="form-inline">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modalsearch-title">Search Data by Category</h4>
                        </div>
                    </div>
                    <!--form id="formpromotype"-->             
                    <div class="modal-body ">
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="box-body modalsearch-body">

                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-primary btn-sm" id="btn-searchpromo" data-oid="0" data-for="searchpromo" data-command="<%= Command.SAVE%>"><i class="fa fa-search"></i></button>
                        <button type="button" data-dismiss="modal" class="btn btn-danger btn-sm"><i class="fa fa-close"></i></button>
                    </div>
                    <!--/form-->
                </div>
            </div>
        </div>

        <div class='col-md-4 notifications bottom-right' id='notifications'></div>

    </body>
    <script type="text/javascript" src="../styles/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../styles/jquery-ui-1.12.1/jquery-ui.min.js"></script>
    <script type="text/javascript" src="../styles/bootstrap/js/bootstrap.min.js"></script>  
    <script type="text/javascript" src="../styles/dimata-app.js"></script>
    <script type="text/javascript" src="../styles/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="../styles/JavaScript-autoComplete-master/auto-complete.min.js"></script>
    <script type="text/javascript" src="../styles/iCheck/icheck.min.js"></script>
    <script type="text/javascript" src="../styles/plugin/datatables/jquery.dataTables.js"></script>
    <script type="text/javascript" src="../styles/plugin/datatables/dataTables.bootstrap.js"></script>
    <script type="text/javascript" src="../styles/bootstrap-notify.js"></script>
    <script type="text/javascript" src="../styles/select2/js/select2.min.js"></script>

    <script type="text/javascript">
        $(document).ready(function () {
            var dataSend;
            var command;
            var dataFor;
            var approot = "<%= approot%>";
            var oid = 0;
            var onDone = null;
            var onSuccess = null;
            var callBackDataTables = null;
            var iCheckBox = null;
            var areaTypeForm = null;
            var language = "<%= SESS_LANGUAGE%>";
            var oidEvent = 0;
            var oidPromo = 0;
            var oidPromoItem = 0;
            var oidSubject = 0;
            var oidObject = 0;
            var oidLocation = 0;
            var oidMember = 0;
            var oidPrice = 0;
            var modalSetting = function (elementId, backdrop, keyboard, show) {
                $(elementId).modal({
                    backdrop: backdrop,
                    keyboard: keyboard,
                    show: show
                });
            };
            var getDataFunction = function (onDone, onSuccess, approot, command, dataSend, servletName, dataAppendTo, notification, dataType) {
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
                    notification: notification,
                    ajaxDataType: dataType
                });
            };
            function dataTablesOptions(elementIdParent, elementId, servletName, dataFor, callBackDataTables) {
                var datafilter = "";
                var privUpdate = "";
                $(elementIdParent).find('table').addClass('table-bordered table-striped table-hover').attr({'id': elementId});
                $("#" + elementId).dataTable({"bDestroy": true,
                    "iDisplayLength": 10,
                    "bProcessing": true,
                    "oLanguage": {
                        "sProcessing": "<div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div>"
                    },
                    "bServerSide": true,
                    "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FILTER=" + datafilter + "&FRM_FIELD_DATA_FOR=" + dataFor + "&privUpdate=" + privUpdate + "&FRM_FLD_APP_ROOT=<%=approot%>",
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

            function dataTablesSearch(elementIdParent, elementId, servletName, dataFor, callBackDataTables, tagline, reason, item, location, member, price, subcomb, obcomb) {
                var datafilter = "";
                var privUpdate = "";
                $(elementIdParent).find('table').addClass('table-bordered table-striped table-hover').attr({'id': elementId});
                $("#" + elementId).dataTable({"bDestroy": true,
                    "iDisplayLength": 10,
                    "bProcessing": true,
                    "oLanguage": {
                        "sProcessing": "<div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div>"
                    },
                    "bServerSide": true,
                    "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FILTER=" + datafilter + "&FRM_FIELD_DATA_FOR=" + dataFor + "&privUpdate=" + privUpdate + "&FRM_FLD_APP_ROOT=<%=approot%>"
                            + "&term-tagline=" + tagline + "&term-reason=" + reason + "&term-item=" + item + "&term-location=" + location + "&term-member=" + member + "&term-price=" + price + "&term-subcomb=" + subcomb + "&term-obcomb=" + obcomb,
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

            function dataTablesOptionsItem(elementIdParent, elementId, servletName, dataFor, callBackDataTables, promoId) {
                var datafilter = "";
                var privUpdate = "";
                $(elementIdParent).find('table').addClass('table-bordered table-striped table-hover').attr({'id': elementId});
                $("#" + elementId).dataTable({"bDestroy": true,
                    "iDisplayLength": 10,
                    "bProcessing": true,
                    "oLanguage": {
                        "sProcessing": "<div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div>"
                    },
                    "bServerSide": true,
                    "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FILTER=" + datafilter + "&FRM_FIELD_DATA_FOR=" + dataFor + "&privUpdate=" + privUpdate + "&FRM_FLD_APP_ROOT=<%=approot%>" + "&FRM_FLD_PROMO_OID=" + promoId,
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

            function validateOptions(elementId, checkType, classError, minLength, matchValue) {

                /* OPTIONS
                 * minLength    : INT VALUE,
                 * matchValue   : STRING OR INT VALUE,
                 * classError   : STRING VALUE,
                 * checkType    : STRING VALUE ('text' for default, 'email' for email check
                 */

                $(elementId).validate({
                    minLength: minLength,
                    matchValue: matchValue,
                    classError: classError,
                    checkType: checkType
                });
            }

            function selectitem(placeholder, selector, servletName, datafor) {
                $(selector).select2({
                    //multiple: true,
                    placeholder: placeholder,
                    minimumInputLength: 2,
                    //maximumSelectionLength: 1,
                    ajax: {
                        url: "<%= approot%>/" + servletName,
                        dataType: 'json',
                        type: "POST",
                        quietMillis: 50,
                        data: function (params) {
                            return {
                                term: params.term,
                                FRM_FIELD_DATA_FOR: datafor,
                                command: 0
                            };
                        },
                        processResults: function (data) {
                            return {
                                results: $.map(data.FRM_FIELD_COMBO, function (item) {
                                    return {
                                        text: item.data_value,
                                        id: item.data_key
                                    };
                                })
                            };
                        }
                    }
                });
            }

            function selectmulti(placeholder, selector, servletName, datafor) {
                $(selector).select2({
                    placeholder: placeholder,
                    minimumInputLength: 1,
                    width: '100%',
                    ajax: {
                        url: "<%= approot%>/" + servletName,
                        dataType: 'json',
                        type: "POST",
                        quietMillis: 50,
                        data: function (params) {
                            return {
                                term: params.term,
                                FRM_FIELD_DATA_FOR: datafor,
                                command: 0
                            };
                        },
                        processResults: function (data) {
                            return {
                                results: $.map(data.FRM_FIELD_COMBO, function (item) {
                                    return {
                                        text: item.data_value,
                                        id: item.data_key
                                    };
                                })
                            };
                        }
                    }
                });
            }

            function selectEnter(elementId) {
                $(elementId).focus().on('keyup', function (e) {
                    if (e.keyCode === 13) {
                        $('#btn-saveevent').trigger("click");
                    }
                });
            }

            function inputSelect(elementId, id, name) {
                var o = $("<option/>", {value: id, text: name});
                $(elementId).append(o);
                $(elementId + ' option[value="' + id + '"]').prop('selected', true);
                $(elementId).trigger('change');
            }

            function dateChange(getDate1, getDate2) {
                if (getDate1 !== null && getDate2 !== null) {
                    $("#datestart").change(function () {
                        var getDate1 = document.getElementById("datestart").value;
                        var getDate2 = document.getElementById("dateend").value;
                        var date1 = new Date(getDate1);
                        var date2 = new Date(getDate2);
                        var timeDiff = Math.abs(date2.getTime() - date1.getTime());
                        var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
                        var diffHour = Math.floor((timeDiff % 86400000) / 3600000);
                        $("#duration").html(diffDays + " Hari " + diffHour + " Jam");
                    });
                    $("#dateend").change(function () {
                        var getDate1 = document.getElementById("datestart").value;
                        var getDate2 = document.getElementById("dateend").value;
                        var date1 = new Date(getDate1);
                        var date2 = new Date(getDate2);
                        var timeDiff = Math.abs(date2.getTime() - date1.getTime());
                        var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
                        var diffHour = Math.floor((timeDiff % 86400000) / 3600000);
                        $("#duration").html(diffDays + " Hari " + diffHour + " Jam");
                    });
                }
            }

            function checkAlert(action) {
                //alert(action + " :\n- oid = " + oid + " :\n- oidEvent = " + oidEvent + " :\n- oidPromo = " + oidPromo + " :\n- oidSubject = " + oidSubject + " :\n- oidObject = " + oidObject + "\n- datafor = " + dataFor + "\n- command = " + command);
            }

            function setOid() {
                $('#setoidgeneral').html(oid);
                $('#setoidevent').html(oidEvent);
                $('#setoidpromo').html(oidPromo);
                $('#setoidpromoitem').html(oidPromoItem);
                $('#setoidsubject').html(oidSubject);
                $('#setoidobject').html(oidObject);
                $('#setdatafor').html(dataFor);
                $('#setcommand').html(command);
            }

            function addEditEvent(elementId, elementId2) {
                $(elementId).on("click", elementId2, function () {
                    $("#modalevent").modal("show");
                    oidEvent = $(this).data('oid');
                    dataFor = $(this).data('for');
                    command = $(this).data('command');

                    //SET TITLE MODAL
                    if (oidEvent === 0) {
                        if (dataFor === 'showform') {
                            $(".modalevent-title").html("Add New Marketing Promotion");
                        }
                    } else if (oidEvent !== 0) {
                        if (dataFor === 'showform') {
                            $(".modalevent-title").html("Edit Marketing Promotion");
                        }
                    }
                    dataSend = {
                        "FRM_FIELD_DATA_FOR": dataFor,
                        "FRM_FIELD_OID": oidEvent,
                        "command": command
                    };
                    onDone = function (data) {
                        if (oidEvent !== 0) {
                            $("#showaddpromo").removeClass("hidden");
                            $("hr").removeClass("hidden");
                            $('.reasonpromo').focus();
                        } else if (oidEvent === 0) {
                            $('#FRM_FIELD_MARKETING_PROMOTION_OBJECTIVES').focus();
                        }

                        $('.dates').datetimepicker({
                            format: 'yyyy-mm-dd hh:ii:ss',
                            todayBtn: true,
                            autoclose: true,
                            minView: 2
                        });
                        var getDate1 = document.getElementById("datestart").value;
                        var getDate2 = document.getElementById("dateend").value;
                        var date1 = new Date(getDate1);
                        var date2 = new Date(getDate2);
                        var timeDiff = Math.abs(date2.getTime() - date1.getTime());
                        var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
                        var diffHour = Math.floor((timeDiff % 86400000) / 3600000);
                        $("#duration").html(diffDays + " Hari " + diffHour + " Jam");
                        dateChange(getDate1, getDate2);

                        $('#duration').click(function () {
                            var hours = Math.abs(getDate2.getTime() - getDate1.getTime()) / 3600000;
                            alert(hours);
                        });

                        selectmulti("Location", ".selectlocation", "AjaxMarketingPromotion", "getlocation");
                        selectmulti("Member type", ".selectmember", "AjaxMarketingPromotion", "getmembertype");
                        selectmulti("Price type", ".selectprice", "AjaxMarketingPromotion", "getpricetype");

                        //selectEnter('.select2-search__field');
                        selectEnter('.select2-selection__rendered');

                        $.map(data.FRM_FIELD_COMBO, function (item) {
                            var idLocation = item.location_id;
                            var nameLocation = item.location_name;
                            inputSelect(".selectlocation", idLocation, nameLocation);

                            var idMember = item.member_id;
                            var nameMember = item.member_name;
                            inputSelect(".selectmember", idMember, nameMember);

                            var idPrice = item.price_id;
                            var namePrice = item.price_name;
                            inputSelect(".selectprice", idPrice, namePrice);
                        });

                        var oidpromotype = $('.combopromo').val();
                        $('.btn-editpromotype').data('oid', oidpromotype);
                        $('.combopromo').change(function () {
                            var oidpromotype = $(this).val();
                            $('.btn-editpromotype').data('oid', oidpromotype);
                        });

                        checkAlert("on click event");
                        setOid();

                        //addComboPromo();
                        showPromo();

                        save("#btn-saveevent");
                        submit("#formevent", "#btn-saveevent");

                        save("#btn-savepromo");
                        submit("#formpromo", "#btn-savepromo");

                        addeditpromotype(".btn-addpromotype");
                        addeditpromotype(".btn-editpromotype");

                        showHideCollapse();
                        expand("#expandAll");
                        collapse("#collapseAll");
                    };
                    onSuccess = function (data) {

                    };
                    getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMarketingPromotion", ".modalevent-body", false, "json");
                });
            }

            function addComboPromo() {
                dataSend = {
                    "FRM_FIELD_DATA_FOR": "getPromotionType",
                    "command": 0
                };
                onDone = function (data) {
                    $('.combopromo').html(data.FRM_FIELD_HTML);
                };
                onSuccess = function (data) {

                };
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMarketingPromotion", null, false, "json");
            }

            function showPromo() {
                dataFor = "showpromo";
                command = 0;
                dataSend = {
                    "FRM_FIELD_DATA_FOR": dataFor,
                    "FRM_FIELD_OID": oidEvent,
                    "command": command
                };
                onDone = function (data) {
                    if (data.FRM_FIELD_HTML !== "") {
                        $('.allpromo').removeClass("hidden");
                    } else {
                        $('.allpromo').addClass("hidden");
                    }
                    $(".list-promo").html(data.FRM_FIELD_HTML);

                    $(".panel-heading").click(function () {
                        var idPromo = $(this).data('oid');
                        checkAlert("on click panel promo");
                        setOid();
                        showDetail(idPromo);
                    });
                    deleteItem(".btn-deletepromo");
                    hover();
                };
                onSuccess = function (data) {

                };
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMarketingPromotion", null, false, "json");
            }

            function showDetail(idPromo) {
                oidPromoItem = 0;
                $(".promo-" + idPromo).html("");
                dataFor = "showdetail";
                dataSend = {
                    "FRM_FIELD_DATA_FOR": dataFor,
                    "FRM_FIELD_OID": oidEvent,
                    "FRM_FLD_PROMO_OID": idPromo,
                    "command": 0
                };
                onDone = function (data) {
                    $(".promo-" + idPromo).html(data.FRM_FIELD_HTML);
                    runDataTablesSubject(idPromo, idPromo);
                    clearSubject(idPromo);
                    runDataTablesObject(idPromo, idPromo);
                    clearObject(idPromo);
                    showAnalysis(idPromo);
                    //showRealization(idPromo);                    
                    $('.btn-refresh').click(function () {
                        var idPromo = $(this).data('oidpromo');
                        showRealization(idPromo);
                    });

                    selectitem("Item name", ".itemsubjectname-" + idPromo, "AjaxMarketingPromotion", "getitemname");
                    $('.itemsubjectname-' + idPromo).on("select2:select", function (e) {
                        var itemId = $(this).val().toString();
                        getItemBy("getitembysubject", oidEvent, idPromo, itemId);
                    });
                    selectitem("Item barcode", ".itemsubjectbarcode-" + idPromo, "AjaxMarketingPromotion", "getitembarcode");
                    $('.itemsubjectbarcode-' + idPromo).on("select2:select", function (e) {
                        var itemId = $(this).val().toString();
                        getItemBy("getitembysubject", oidEvent, idPromo, itemId);
                    });
                    selectitem("Item SKU", ".itemsubjectsku-" + idPromo, "AjaxMarketingPromotion", "getitemsku");
                    $('.itemsubjectsku-' + idPromo).on("select2:select", function (e) {
                        var itemId = $(this).val().toString();
                        getItemBy("getitembysubject", oidEvent, idPromo, itemId);
                    });
                    //----------------------------------------------------------
                    selectitem("Item name", ".itemobjectname-" + idPromo, "AjaxMarketingPromotion", "getitemname");
                    $('.itemobjectname-' + idPromo).on("select2:select", function (e) {
                        var itemId = $(this).val().toString();
                        getItemBy("getitembyobject", oidEvent, idPromo, itemId);
                    });
                    selectitem("Item barcode", ".itemobjectbarcode-" + idPromo, "AjaxMarketingPromotion", "getitembarcode");
                    $('.itemobjectbarcode-' + idPromo).on("select2:select", function (e) {
                        var itemId = $(this).val().toString();
                        getItemBy("getitembyobject", oidEvent, idPromo, itemId);
                    });
                    selectitem("Item SKU", ".itemobjectsku-" + idPromo, "AjaxMarketingPromotion", "getitemsku");
                    $('.itemobjectsku-' + idPromo).on("select2:select", function (e) {
                        var itemId = $(this).val().toString();
                        getItemBy("getitembyobject", oidEvent, idPromo, itemId);
                    });
                    //----------------------------------------------------------
                    $('.itemsubjectname-' + idPromo).on("select2:unselect", function () {
                        clearSubject(idPromo);
                    });
                    $('.itemsubjectbarcode-' + idPromo).on("select2:unselect", function () {
                        clearSubject(idPromo);
                    });
                    $('.itemsubjectsku-' + idPromo).on("select2:unselect", function () {
                        clearSubject(idPromo);
                    });
                    $('.itemobjectname-' + idPromo).on("select2:unselect", function () {
                        clearObject(idPromo);
                    });
                    $('.itemobjectbarcode-' + idPromo).on("select2:unselect", function () {
                        clearObject(idPromo);
                    });
                    $('.itemobjectsku-' + idPromo).on("select2:unselect", function () {
                        clearObject(idPromo);
                    });
                    //----------------------------------------------------------
                    $('.itemsubjectcategory-' + idPromo).dblclick(function () {
                        clearSubject(idPromo);
                    });
                    $('.itemobjectcategory-' + idPromo).dblclick(function () {
                        clearObject(idPromo);
                    });

                    var status = $('.approvestatus').val();
                    if (status === "1") {
                        $('.btn-approve').attr({"disabled": "true"});
                        $('.btn-disapprove').removeAttr("disabled");
                    } else if (status === "0") {
                        $('.btn-disapprove').attr({"disabled": "true"});
                        $('.btn-approve').removeAttr("disabled");
                    }

                    approvePromo(".btn-approve");
                    disapprovePromo(".btn-disapprove");
                    editPromo(".btn-editpromo");

                    save(".btn-savesubject-" + idPromo);
                    submit(".formsubject-" + idPromo, ".btn-savesubject-" + idPromo);

                    save(".btn-saveobject-" + idPromo);
                    submit(".formobject-" + idPromo, ".btn-saveobject-" + idPromo);

                    $('.btn-refresh').trigger('click');
                    $('.itemsubjectbarcode-' + idPromo).focus();
                };
                onSuccess = function (data) {

                };
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMarketingPromotion", null, false, "json");
            }
            //------------------------------------------------------------------
            function save(elementId) {
                $(elementId).click(function () {
                    oidPromoItem = $(this).data('oidpromo');
                    dataFor = $(this).data('for');
                    command = $(this).data('command');
                    if (dataFor === "saveevent") {
                        oid = oidEvent;
                    } else if (dataFor === "savepromo") {
                        oid = oidPromo;
                    } else if (dataFor === "savesubject") {
                        oid = oidSubject;
                    } else if (dataFor === "saveobject") {
                        oid = oidObject;
                    }
                    oidLocation = $(".selectlocation").val();
                    oidMember = $(".selectmember").val();
                    oidPrice = $(".selectprice").val();

                    setOid();
                    checkAlert("on save");
                });
            }

            function submit(form, button) {
                $("form" + form).submit(function () {
                    var currentBtnHtml = $(button).html();
                    $(button).html("Saving...").attr({"disabled": "true"});
                    checkAlert("on submit");
                    setOid();
                    onDone = function (data) {
                        if (dataFor === "saveevent") {
                            runDataTables();
                            var msg = data.FRM_FIELD_HTML;
                            var oidbaru = data.FRM_FIELD_OID_BARU;
                            if (msg === "Data have been saved") {
                                $("#showaddpromo").removeClass("hidden");
                                $("hr").removeClass("hidden");
                                $("#oidbaru").val(oidbaru);
                                oidEvent = oidbaru;
                            }
                        } else if (dataFor === "savepromo") {
                            showPromo();
                            clearPromo();
                            oidPromo = 0;
                        } else if (dataFor === "savesubject") {
                            runDataTablesSubject(oidPromoItem, oidPromoItem);
                            showAnalysis(oidPromoItem);
                            showRealization(oidPromoItem);
                            clearSubject(oidPromoItem);
                            oidSubject = 0;
                            oidPromoItem = 0;
                        } else if (dataFor === "saveobject") {
                            runDataTablesObject(oidPromoItem, oidPromoItem);
                            showAnalysis(oidPromoItem);
                            showRealization(oidPromoItem);
                            clearObject(oidPromoItem);
                            oidObject = 0;
                            oidPromoItem = 0;
                        }
                        oid = 0;
                        setOid();
                        checkAlert("on done submit");
                    };
                    onSuccess = function (data) {
                        $(button).removeAttr("disabled").html(currentBtnHtml);
                    };
                    var data = $(this).serialize();
                    dataSend = "" + data + "&FRM_FIELD_DATA_FOR=" + dataFor + "&FRM_FIELD_OID=" + oid + "&command=" + command + "&FRM_FLD_LOCATION_ID=" + oidLocation + "&FRM_FLD_MEMBER_TYPE_ID=" + oidMember + "&FRM_FLD_PRICE_TYPE_ID=" + oidPrice + "";
                    getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMarketingPromotion", null, true, "json");
                    return false;
                });
            }
            //------------------------------------------------------------------
            function getItemBy(datafor, oidEvent, idPromo, itemId) {
                dataSend = {
                    "FRM_FIELD_DATA_FOR2": datafor,
                    "FRM_FIELD_DATA_FOR": "getitemby",
                    "command": 0,
                    "termEvent": oidEvent,
                    "termItem": itemId
                };
                onDone = function (data) {
                    if (datafor === "getitembysubject") {
                        $.map(data.FRM_FIELD_ITEM, function (item) {
                            $('.itemsubjectsku-' + idPromo).empty();
                            $('.itemsubjectbarcode-' + idPromo).empty();
                            $('.itemsubjectname-' + idPromo).empty();
                            inputSelect('.itemsubjectsku-' + idPromo, item.item_id, item.item_sku);
                            inputSelect('.itemsubjectbarcode-' + idPromo, item.item_id, item.item_barcode);
                            inputSelect('.itemsubjectname-' + idPromo, item.item_id, item.item_name);
                            $('.itemsubjectcategory-' + idPromo).val(item.item_category);
                            $('.itemsubjectsales-' + idPromo).val(item.item_sales);
                            $('.itemsubjectpurchase-' + idPromo).val(item.item_purchase);
                            $('.itemsubjectprofit-' + idPromo).val(item.item_profit);
                            $('.itemsubjectquantity-' + idPromo).focus();
                        });
                    } else if (datafor === "getitembyobject") {
                        $.map(data.FRM_FIELD_ITEM, function (item) {
                            $('.itemobjectsku-' + idPromo).empty();
                            $('.itemobjectbarcode-' + idPromo).empty();
                            $('.itemobjectname-' + idPromo).empty();
                            inputSelect('.itemobjectsku-' + idPromo, item.item_id, item.item_sku);
                            inputSelect('.itemobjectbarcode-' + idPromo, item.item_id, item.item_barcode);
                            inputSelect('.itemobjectname-' + idPromo, item.item_id, item.item_name);
                            $('.itemobjectcategory-' + idPromo).val(item.item_category);
                            $('.itemobjectregular-' + idPromo).val(item.item_regular);
                            $('.itemobjectcost-' + idPromo).val(item.item_cost);
                            $('.itemobjectquantity-' + idPromo).focus();
                        });
                    }
                };
                onSuccess = function (data) {

                };
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMarketingPromotion", null, true, "json");
            }

            function showAnalysis(promoId) {
                $('.analysis-' + promoId).html("");
                dataSend = {
                    "FRM_FIELD_DATA_FOR": "getanalysis",
                    "FRM_FLD_PROMO_OID": promoId,
                    "command": 0
                };
                //alert(JSON.stringify(dataSend));
                onDone = function (data) {
                    $('.analysis-' + promoId).html(data.FRM_FIELD_HTML);
                };
                onSuccess = function (data) {

                };
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMarketingPromotion", null, false, "json");
            }

            function showRealization(promoId) {
                $('.realization-' + promoId).html("<div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please wait...</b></div></div>");
                dataSend = {
                    "FRM_FIELD_DATA_FOR": "getrealization",
                    "FRM_FIELD_OID": oidEvent,
                    "FRM_FLD_PROMO_OID": promoId,
                    "command": 0
                };
                //alert(JSON.stringify(dataSend));
                onDone = function (data) {
                    $('.realization-' + promoId).html(data.FRM_FIELD_HTML);
                };
                onSuccess = function (data) {

                };
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMarketingPromotion", null, false, "json");
            }

            //------------------------------------------------------------------
            function deleteEvent(tableId, elementId) {
                $(tableId).on("click", elementId, function () {
                    oid = $(this).data("oid");
                    dataFor = $(this).data("for");
                    command = $(this).data("command");
                    checkAlert("on delete event");
                    setOid();
                    var confirmText = "Are you sure want to delete these data?";
                    if (confirm(confirmText)) {
                        var currentHtml = $(this).html();
                        $(this).html("Deleting...").attr({"disabled": true});
                        dataSend = {
                            "FRM_FIELD_DATA_FOR": dataFor,
                            "FRM_FIELD_OID": oid,
                            "command": command
                        };
                        onDone = function (data) {
                            runDataTables();
                            oid = 0;
                            checkAlert("on success delete event");
                            setOid();
                        };
                        onSuccess = function (data) {

                        };

                        getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMarketingPromotion", null, true, "json");
                        $(this).removeAttr("disabled").html(currentHtml);
                    }

                });
            }
            function deleteItem(elementId) {
                $(elementId).unbind("click").click(function () {
                    oid = $(this).data('oid');
                    dataFor = $(this).data('for');
                    command = $(this).data('command');
                    oidPromoItem = $(this).data('oidpromo');
                    checkAlert("on delete item");
                    setOid();
                    var confirmText = "Are you sure want to delete these data?";
                    if (confirm(confirmText)) {
                        dataSend = {
                            "FRM_FIELD_DATA_FOR": dataFor,
                            "FRM_FIELD_OID": oid,
                            "command": command
                        };
                        onDone = function (data) {
                            if (dataFor === "deletesubject") {
                                runDataTablesSubject(oidPromoItem, oidPromoItem);
                                showAnalysis(oidPromoItem);
                                showRealization(oidPromoItem);
                                oidSubject = 0;
                                oidPromoItem = 0;
                            } else if (dataFor === "deleteobject") {
                                runDataTablesObject(oidPromoItem, oidPromoItem);
                                showAnalysis(oidPromoItem);
                                showRealization(oidPromoItem);
                                oidObject = 0;
                                oidPromoItem = 0;
                            } else if (dataFor === "deletepromo") {
                                showPromo();
                                oidPromo = 0;
                            }
                            oid = 0;
                            checkAlert("on done delete item");
                            setOid();
                        };
                        onSuccess = function (data) {

                        };
                        getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMarketingPromotion", null, true, "json");
                    }
                });
            }
            //------------------------------------------------------------------
            function editPromo(elementId) {
                $(elementId).click(function () {
                    oidPromo = $(this).data('oid');
                    dataFor = $(this).data('for');
                    command = $(this).data('command');
                    checkAlert("on edit promo");
                    setOid();
                    $('.reasonpromo').focus();
                    dataSend = {
                        "FRM_FIELD_DATA_FOR": dataFor,
                        "FRM_FIELD_OID": oidPromo,
                        "command": command
                    };
                    onDone = function (data) {
                        $.map(data.FRM_FIELD_PROMO, function (item) {
                            var promotypeid = item.promo_type;
                            var promoreason = item.promo_reason;
                            var promotagline = item.promo_tagline;
                            var subjectcomb = item.subject_combination;
                            var objectcomb = item.object_combination;
                            var discountquantity = item.discount_quantity;
                            var status = item.status_approve;
                            $('.combopromo').val(promotypeid);
                            $('.reasonpromo').val(promoreason);
                            $('.taglinepromo').val(promotagline);
                            $('.combosubjectcomb').val(subjectcomb);
                            $('.comboobjectcomb').val(objectcomb);
                            if (discountquantity === "1") {
                                $('.discountquantity').prop("checked", true);
                            } else {
                                $('.discountquantity').prop("checked", false);
                            }
                        });
                    };
                    onSuccess = function (data) {

                    };
                    getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMarketingPromotion", null, false, "json");
                });
            }

            function editItem(elementId) {
                $(elementId).unbind("click").click(function () {
                    var idItem = $(this).data('oid');
                    dataFor = $(this).data('for');
                    command = $(this).data('command');
                    oidPromoItem = $(this).data('oidpromo');
                    if (dataFor === "editsubject") {
                        oidSubject = idItem;
                    } else if (dataFor === "editobject") {
                        oidObject = idItem;
                    }
                    checkAlert("on edit item");
                    setOid();
                    dataSend = {
                        "FRM_FIELD_DATA_FOR": dataFor,
                        "FRM_FIELD_OID": idItem,
                        "command": command
                    };
                    onDone = function (data) {
                        if (dataFor === "editsubject") {
                            $.map(data.FRM_FIELD_ITEM, function (item) {
                                inputSelect('.itemsubjectsku-' + oidPromoItem, item.item_id, item.item_sku);
                                inputSelect('.itemsubjectbarcode-' + oidPromoItem, item.item_id, item.item_barcode);
                                inputSelect('.itemsubjectname-' + oidPromoItem, item.item_id, item.item_name);
                                $('.itemsubjectcategory-' + oidPromoItem).val(item.item_category);
                                $('.itemsubjectquantity-' + oidPromoItem).val(item.item_quantity);
                                $('.itemsubjecttargetquantity-' + oidPromoItem).val(item.item_target);
                                $('.itemsubjectvalid-' + oidPromoItem).val(item.item_valid);
                                $('.itemsubjectsales-' + oidPromoItem).val(item.item_sales);
                                $('.itemsubjectpurchase-' + oidPromoItem).val(item.item_purchase);
                                $('.itemsubjectprofit-' + oidPromoItem).val(item.item_profit);
                            });
                        } else if (dataFor === "editobject") {
                            $.map(data.FRM_FIELD_ITEM, function (item) {
                                inputSelect('.itemobjectsku-' + oidPromoItem, item.item_id, item.item_sku);
                                inputSelect('.itemobjectbarcode-' + oidPromoItem, item.item_id, item.item_barcode);
                                inputSelect('.itemobjectname-' + oidPromoItem, item.item_id, item.item_name);
                                $('.itemobjectcategory-' + oidPromoItem).val(item.item_category);
                                $('.itemobjectquantity-' + oidPromoItem).val(item.item_quantity);
                                $('.itemobjectpromotype-' + oidPromoItem).val(item.item_promotype);
                                $('.itemobjectvalid-' + oidPromoItem).val(item.item_valid);
                                $('.itemobjectregular-' + oidPromoItem).val(item.item_regular);
                                $('.itemobjectpromotion-' + oidPromoItem).val(item.item_promotion);
                                $('.itemobjectcost-' + oidPromoItem).val(item.item_cost);
                            });
                        }
                    };
                    onSuccess = function (data) {

                    };
                    getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMarketingPromotion", null, false, "json");
                });
            }

            function approvePromo(elementId) {
                $(elementId).click(function () {
                    var currentBtnHtml = $(this).html();
                    $(elementId).html("Processing...").attr({"disabled": "true"});
                    oid = $(this).data('oid');
                    dataFor = $(this).data('for');
                    command = $(this).data('command');

                    dataSend = {
                        "FRM_FIELD_DATA_FOR": dataFor,
                        "FRM_FIELD_OID": oid,
                        "command": command
                    };
                    onDone = function (data) {
                        var msg = data.FRM_FIELD_HTML;
                        var status = data.FRM_FIELD_RETURN_OID;
                        alert(msg + " " + status);
                        if (msg === "Data have been updated") {
                            if (status === "1") {
                                $(elementId).html(currentBtnHtml);
                                $('.btn-disapprove').removeAttr("disabled");
                                showPromo();
                            } else {
                                $(elementId).removeAttr("disabled").html(currentBtnHtml);
                            }
                        } else {
                            $(elementId).removeAttr("disabled").html(currentBtnHtml);
                        }
                        checkAlert("on approve");
                        setOid();
                    };
                    onSuccess = function (data) {

                    };
                    getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMarketingPromotion", null, false, "json");
                });
            }

            function disapprovePromo(elementId) {
                $(elementId).click(function () {
                    var currentBtnHtml = $(this).html();
                    $(elementId).html("Processing...").attr({"disabled": "true"});
                    oid = $(this).data('oid');
                    dataFor = $(this).data('for');
                    command = $(this).data('command');

                    checkAlert("on disapprove");
                    setOid();

                    dataSend = {
                        "FRM_FIELD_DATA_FOR": dataFor,
                        "FRM_FIELD_OID": oid,
                        "command": command
                    };
                    onDone = function (data) {
                        var msg = data.FRM_FIELD_HTML;
                        var status = data.FRM_FIELD_RETURN_OID;
                        alert(msg + " " + status);
                        if (msg === "Data have been updated") {
                            if (status === "0") {
                                $(elementId).html(currentBtnHtml);
                                $('.btn-approve').removeAttr("disabled");
                                showPromo();
                            } else {
                                $(elementId).removeAttr("disabled").html(currentBtnHtml);
                            }
                        } else {
                            $(elementId).removeAttr("disabled").html(currentBtnHtml);
                        }
                    };
                    onSuccess = function (data) {

                    };
                    getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMarketingPromotion", null, false, "json");
                });
            }

            //------------------------------------------------------------------
            function addeditpromotype(elementId) {
                $(elementId).click(function () {
                    $("#modalpromotype").modal("show");
                    dataFor = $(this).data('for');
                    command = $(this).data('command');
                    if (elementId === ".btn-editpromotype") {
                        oid = $('.combopromo').val();
                    } else if (elementId === '.btn-addpromotype') {
                        oid = 0;
                    }

                    //SET TITLE MODAL
                    if (oid === 0) {
                        if (dataFor === 'showformpromotype') {
                            $(".modalpromotype-title").html("Add New Promotion Type");
                        }
                    } else if (oid !== 0) {
                        if (dataFor === 'showformpromotype') {
                            $(".modalpromotype-title").html("Edit Promotion Type");
                        }
                    }
                    dataSend = {
                        "FRM_FIELD_DATA_FOR": dataFor,
                        "FRM_FIELD_OID": oid,
                        "command": command
                    };
                    onDone = function (data) {
                        $('.promotypename').focus();
                        checkAlert("on click promotype");
                        setOid();
                    };
                    onSuccess = function (data) {
                        //$('.promotypename').focus();
                    };

                    getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMarketingPromotionType", ".modalpromotype-body", false, "json");
                });
            }

            $("#btn-savepromotype").click(function () {
                dataFor = $(this).data('for');
                command = $(this).data('command');
                checkAlert("on save promotype");
                setOid();
                $("#promotypeoid").val(oid);
                $("#promotypedatafor").val(dataFor);
                $("#promotypecommand").val(command);
            });

            $("form#formpromotype").submit(function () {
                var currentBtnHtml = $("#btn-savepromotype").html();
                $("#btn-savepromotype").html("Saving...").attr({"disabled": "true"});

                checkAlert("on submit promotype");
                setOid();

                onDone = function (data) {
                    addComboPromo();
                    $('.combopromo').val(data.FRM_FIELD_RETURN_OID);
                    oid = 0;
                    checkAlert("on success submit promotype");
                    setOid();
                };
                onSuccess = function (data) {
                    $("#btn-savepromotype").removeAttr("disabled").html(currentBtnHtml);
                    $("#modalpromotype").modal("hide");
                };
                var data = $(this).serialize();
                dataSend = "" + data + "&FRM_FIELD_DATA_FOR=" + dataFor + "&FRM_FIELD_OID=" + oid + "&command=" + command + "";
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMarketingPromotionType", null, true, "json");
                return false;
            });
            //------------------------------------------------------------------
            function searchCategory(elementId) {
                $(elementId).click(function () {
                    $("#modalsearch").modal("show");
                    dataFor = $(this).data('for');
                    command = $(this).data('command');

                    dataSend = {
                        "FRM_FIELD_DATA_FOR": dataFor,
                        "FRM_FIELD_OID": oid,
                        "command": command
                    };
                    onDone = function (data) {

                    };

                    onSuccess = function (data) {

                    };

                    getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMarketingPromotion", ".modalsearch-body", false, "json");
                });
            }

            $('#btn-searchpromo').click(function () {
                var tagline = $('#search-tagline').val();
                var reason = $('#search-reason').val();
                var item = $('#search-item').val();
                var location = $('#search-location').val();
                var member = $('#search-member').val();
                var price = $('#search-price').val();
                var subcomb = $('#search-subcomb').val();
                var obcomb = $('#search-obcomb').val();
                runDataTablesSearch(tagline, reason, item, location, member, price, subcomb, obcomb);
                $("#modalsearch").modal("hide");
            });

            //------------------------------------------------------------------
            function clearPromo() {
                $('.reasonpromo').val("");
                $('.taglinepromo').val("");
                $('.reasonpromo').focus();
                $('.discountquantity').prop('checked', false);
                oidPromo = 0;
                setOid();
            }

            function clearSubject(idPromo) {
                $('.itemsubjectsku-' + idPromo).empty();
                $('.itemsubjectbarcode-' + idPromo).empty();
                $('.itemsubjectname-' + idPromo).empty();
                $('.itemsubjectcategory-' + idPromo).val("");
                $('.itemsubjectquantity-' + idPromo).val("");
                $('.itemsubjecttargetquantity-' + idPromo).val("");
                $('.itemsubjectsales-' + idPromo).val("");
                $('.itemsubjectpurchase-' + idPromo).val("");
                $('.itemsubjectprofit-' + idPromo).val("");
                $('.itemsubjectbarcode-' + idPromo).focus();
                oidSubject = 0;
                setOid();
            }

            function clearObject(idPromo) {
                $('.itemobjectsku-' + idPromo).empty();
                $('.itemobjectbarcode-' + idPromo).empty();
                $('.itemobjectname-' + idPromo).empty();
                $('.itemobjectcategory-' + idPromo).val("");
                $('.itemobjectquantity-' + idPromo).val("");
                $('.itemobjectregular-' + idPromo).val("");
                $('.itemobjectpromotion-' + idPromo).val("");
                $('.itemobjectcost-' + idPromo).val("");
                $('.itemobjectbarcode-' + idPromo).focus();
                oidObject = 0;
                setOid();
            }

            //------------------------------------------------------------------
            function runDataTables() {
                dataTablesOptions("#marketingPromotionElement", "tableMarketingPromotionElement", "AjaxMarketingPromotion", "listevent", callBackDataTables);
            }
            
            function runDataTablesSearch(tagline, reason, item, location, member, price, subcomb, obcomb){
                dataTablesSearch("#marketingPromotionElement", "tableMarketingPromotionElement", "AjaxMarketingPromotion", "listevent", callBackDataTables, tagline, reason, item, location, member, price, subcomb, obcomb);
            }

            function runDataTablesSubject(i, idPromo) {
                dataTablesOptionsItem(".detailSubjectElement" + i, "tableDetailSubjectElement" + i, "AjaxMarketingPromotion", "listsubject", callBackDataTables, idPromo);
            }

            function runDataTablesObject(i, idPromo) {
                dataTablesOptionsItem(".detailObjectElement" + i, "tableDetailObjectElement" + i, "AjaxMarketingPromotion", "listobject", callBackDataTables, idPromo);
            }

            //==================================================================
            callBackDataTables = function () {
                deleteItem(".btn-deletesubject");
                deleteItem(".btn-deleteobject");
                editItem('.btn-editsubject');
                editItem('.btn-editobject');
            };
            //------------------------------------------------------------------
            runDataTables();
            addEditEvent("#promotionpage", ".btn-addevent");
            addEditEvent("#promotionpage", ".btn-editevent");
            searchCategory('.btn-showformsearch');
            deleteEvent("#tableMarketingPromotionElement", ".btn-deleteevent");
            modalSetting("#modalevent", "static", false, false);
            modalSetting("#modalpromotype", "static", false, false);
            //==================================================================

            function showHideCollapse() {
                $('a[data-toggle="collapse"]').on('click', function () {
                    var objectID = $(this).attr('href');
                    if ($(objectID).hasClass('in')) {
                        $(objectID).collapse('hide');
                    } else {
                        $(objectID).collapse('show');
                    }
                });
            }
            function expand(elementId) {
                $(elementId).on('click', function () {
                    $('a[data-toggle="collapse"]').each(function () {
                        var objectID = $(this).attr('href');
                        if ($(objectID).hasClass('in') === false) {
                            $(objectID).collapse('show');
                        }
                    });
                });
            }
            function collapse(elementId) {
                $(elementId).on('click', function () {
                    $('a[data-toggle="collapse"]').each(function () {
                        var objectID = $(this).attr('href');
                        $(objectID).collapse('hide');
                    });
                });
            }
            function hover() {
                $(".panel-heading").on("mouseover", function () {
                    $(this).css({"background": "lightsteelblue", "cursor": "pointer"});
                }).on("mouseout", function () {
                    $(this).css({"background": ""});
                });
            }
        });
    </script>

</html>
