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
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_REPORT_BY_SUPPLIER); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!
    public static final String textListHeader[][] = 
    {
        {"Tanggal","Lokasi","Kategori","Item","User Doc","Kasir","Opsi Dokumen","Opsi Grafik","Opsi Tabel","Opsi Filter","Opsi Nilai","Opsi Data","Grafik Multiple","Grafik Tunggal","Tampilkan Tabel","Sembunyikan Tabel","Nilai Tertinggi","Qty Tertinggi","Nilai Terendah","Qty Terendah","Tampilkan Akumulasi","Tampilkan Per Tangga","Tampilkan Qty","Tampilkan Nilai"},
        {"Date","Location","Category","Item","User Doc","Cashier","Doc Option","Chart Option","Table Option","Filter Option","Value Option","Data Option","Multiple Chart","One Chart","Show Table","Hide Table","Most Value","Most Quantity","Least Value","Least Quantity","Show Accumulation","Show Per Date","Show Qty","Show Value"}
    };
%>
<html>
<head>
    <title>Dimata - ProChain POS</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
    <%if(menuUsed == MENU_ICON){%>
        <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
    <%}%>
    
    <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
</head> 
<%
    SrcReportStock srcReportStock = new SrcReportStock();
    FrmSrcReportStock frmSrcReportStock = new FrmSrcReportStock();
    try {
            srcReportStock = (SrcReportStock)session.getValue(SessReportStock.SESS_SRC_REPORT_STOCK);
    }
    catch(Exception e) {
            srcReportStock = new SrcReportStock();
    }


    if(srcReportStock==null) {
            srcReportStock = new SrcReportStock();
    }

    try {
            session.removeValue(SessReportStock.SESS_SRC_REPORT_STOCK_LIST);
    }
    catch(Exception e){
    }
%>
<body class="" style="background-color:beige"  leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
    <div class="nonprint">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <%if(menuUsed == MENU_PER_TRANS){%>
            <tr>
                <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
                    <%@ include file = "../../../../main/header.jsp" %>
                </td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                    <%@ include file = "../../../../main/mnmain.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
            <%}else{%>
            <tr bgcolor="#FFFFFF">
                <td height="10" ID="MAINMENU">
                    <%@include file="../../../../styletemplate/template_header.jsp" %>
                </td>
            </tr>
            <%}%>
            <tr> 
                <link href="../../../styles/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
                <link href="../../../styles/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />
                <link href="../../../styles/dist/css/AdminLTE.css" rel="stylesheet" type="text/css" />
                <link href="../../../styles/dist/css/skins/_all-skins.css" rel="stylesheet" type="text/css" />
                <link href="../../../styles/font-awesome/font-awesome.css" rel="stylesheet" type="text/css" />
                <link href="../../../styles/iCheck/flat/blue.css" rel="stylesheet" type="text/css" />
                <link href="../../../styles/iCheck/all.css" rel="stylesheet" type="text/css" />
                <link href="../../../styles/select2/css/select2.min.css" rel="stylesheet" type="text/css" />
                <link href="../../../styles/datepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
                <td valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                        <tr> 
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
                                <div   class="row" style="margin-left:0px; margin-right:0px; margin-top:10px; ">
                                    <div class="col-md-12">                                       
                                        <div class="box box-primary">
                                            <div class="box-header">
                                                <h3 class="box-title">Filter</h3>
                                                <div class="box-tools pull-right">
                                                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                                                </div>
                                            </div>
                                            <div class="box-body">
                                                <form name="frmsrcreportstocktracking" id="frmsrcreportstocktracking" class="form-horizontal" role="form">
                                                    <input type="hidden" name="language" id="language" value="<%= SESS_LANGUAGE%>">
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-3" for="email">
                                                                <%= textListHeader[SESS_LANGUAGE][0]%>
                                                            </label>
                                                            <div class="col-sm-4">
                                                            <div class="input-group">
                                                                <div class="input-group-addon">
                                                                    <i class="fa fa-calendar"></i>
                                                                </div>
                                                                <input  id="dateFrom" name="<%=frmSrcReportStock.fieldNames[frmSrcReportStock.FRM_FIELD_DATE_FROM]%>" class="form-control dates" type="text">
                                                            </div>
                                                        </div>
                                                        <div class="col-sm-5">
                                                            <div class="input-group">
                                                                <div class="input-group-addon">
                                                                    <i class="fa fa-calendar"></i>
                                                                </div>
                                                                <input id="dateTo" name="<%=frmSrcReportStock.fieldNames[frmSrcReportStock.FRM_FIELD_DATE_TO]%>" class="form-control dates" type="text">
                                                            </div>
                                                        </div> 
                                                        </div>  
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-3" for="email">
                                                                <%= textListHeader[SESS_LANGUAGE][1]%>
                                                            </label>
                                                            <div class="col-sm-9">
                                                                <% 
                                                                Vector val_locationid = new Vector(1,1);
                                                                Vector key_locationid = new Vector(1,1); 

                                                                boolean checkDataAllLocReportView = PstDataCustom.checkDataAllLocReportView(userId, "user_view_sale_stock_report_location","0");
                                                                String whereLocViewReport = PstDataCustom.whereLocReportView(userId, "user_view_sale_stock_report_location","");
                                                                Vector vt_loc = PstLocation.listLocationStore(0,0,whereLocViewReport,PstLocation.fieldNames[PstLocation.FLD_NAME]);

                                                                if(checkDataAllLocReportView){
                                                                    val_locationid.add("0");
                                                                    key_locationid.add(textListHeader[SESS_LANGUAGE][1]);
                                                                }
                                                                for(int d=0;d<vt_loc.size();d++){
                                                                    Location loc = (Location)vt_loc.get(d);
                                                                    val_locationid.add(""+loc.getOID()+"");
                                                                    key_locationid.add(loc.getName());
                                                                }
                                                            %> 
                                                            <%=ControlCombo.draw(frmSrcReportStock.fieldNames[frmSrcReportStock.FRM_FIELD_LOCATION_ID], null, ""+srcReportStock.getLocationId(), val_locationid, key_locationid, " multiple='multiple'", "form-control selects2")%> 
                                                            </div>
                                                        </div>
                                                         <div class="form-group">
                                                            <label class="control-label col-sm-3" for="email">
                                                                <%= textListHeader[SESS_LANGUAGE][2]%>
                                                            </label>
                                                            <div class="col-sm-9">
                                                                <%
                                                                    Vector val_categoryid = new Vector(1,1);
                                                                    Vector key_categoryid = new Vector(1,1);

                                                                    Vector listCategory = PstCategory.list(0, 0, "", "");
                                                                    for (int i=0; i<listCategory.size();i++ ){
                                                                        Category entCategory = (Category) listCategory.get(i);
                                                                        val_categoryid.add(""+entCategory.getOID()+"");
                                                                        key_categoryid.add(""+entCategory.getName()+"");
                                                                    }
                                                                %>
                                                                <%=ControlCombo.draw(FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_CATEGORY_ID], null, "", val_categoryid, key_categoryid, " multiple='multiple' ", "form-control selects2")%>
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-3" for="email">
                                                                <%= textListHeader[SESS_LANGUAGE][3]%>
                                                            </label>
                                                            <div class="col-sm-9">
                                                                <div class="itemPlace"></div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-3" for="email">
                                                                <%= textListHeader[SESS_LANGUAGE][4]%>
                                                            </label>
                                                            <div class="col-sm-9">                                            
                                                                <div id="userDocPlace"></div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-3" for="email">
                                                                <%= textListHeader[SESS_LANGUAGE][5]%>
                                                            </label>
                                                            <div class="col-sm-9">
                                                                <div id="cashierPlace"></div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-3" for="email">
                                                                <%= textListHeader[SESS_LANGUAGE][6]%>
                                                            </label>
                                                            <div class="col-sm-9">
                                                                <label class="checkbox-inline"><input type="checkbox" name='FRM_FIELD_DOC_OPTION' value="1">Stock In</label>
                                                                <label class="checkbox-inline"><input type="checkbox" name='FRM_FIELD_DOC_OPTION' value="2">Stock Out</label>
                                                                <label class="checkbox-inline"><input type="checkbox" name='FRM_FIELD_DOC_OPTION' value="3">Delta</label>
                                                                <label class="checkbox-inline"><input type="checkbox" name='FRM_FIELD_DOC_OPTION' value="4">Opname</label>
                                                            </div>
                                                        </div> 
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-3" for="email">
                                                                <%= textListHeader[SESS_LANGUAGE][7]%>
                                                            </label>
                                                            <div class="col-sm-9">
                                                                <label class="radio-inline"><input type="radio" name="FRM_FIELD_CHART_OPTION"><%= textListHeader[SESS_LANGUAGE][12] %></label>
                                                                <label class="radio-inline"><input type="radio" name="FRM_FIELD_CHART_OPTION"><%= textListHeader[SESS_LANGUAGE][13] %></label>
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-3" for="email">
                                                                <%= textListHeader[SESS_LANGUAGE][8]%>
                                                            </label>
                                                            <div class="col-sm-9">
                                                                <label class="radio-inline"><input type="radio" name="FRM_FIELD_TABLE_OPTION"><%= textListHeader[SESS_LANGUAGE][14] %></label>
                                                                <label class="radio-inline"><input type="radio" name="FRM_FIELD_TABLE_OPTION"><%= textListHeader[SESS_LANGUAGE][15] %></label>
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-3" for="email">
                                                                <%= textListHeader[SESS_LANGUAGE][9]%>
                                                            </label>
                                                            <div class="col-sm-9">
                                                                <label class="radio-inline"><input type="radio" name="FRM_FIELD_FILTER_OPTION"><%= textListHeader[SESS_LANGUAGE][16] %></label>
                                                                <label class="radio-inline"><input type="radio" name="FRM_FIELD_FILTER_OPTION"><%= textListHeader[SESS_LANGUAGE][17] %></label>
                                                                <label class="radio-inline"><input type="radio" name="FRM_FIELD_FILTER_OPTION"><%= textListHeader[SESS_LANGUAGE][18] %></label>
                                                                <label class="radio-inline"><input type="radio" name="FRM_FIELD_FILTER_OPTION"><%= textListHeader[SESS_LANGUAGE][19] %></label>
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-3" for="email">
                                                                <%= textListHeader[SESS_LANGUAGE][10]%>
                                                            </label>
                                                            <div class="col-sm-9">
                                                                <label class="radio-inline"><input type="radio" name="FRM_FIELD_VALUE_OPTION"><%= textListHeader[SESS_LANGUAGE][20] %></label>
                                                                <label class="radio-inline"><input type="radio" name="FRM_FIELD_VALUE_OPTION"><%= textListHeader[SESS_LANGUAGE][21] %></label>
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label class="control-label col-sm-3" for="email">
                                                                <%= textListHeader[SESS_LANGUAGE][11]%>
                                                            </label>
                                                            <div class="col-sm-9">
                                                                <label class="radio-inline"><input type="radio" name="FRM_FIELD_DATA_OPTION"><%= textListHeader[SESS_LANGUAGE][22] %></label>
                                                                <label class="radio-inline"><input type="radio" name="FRM_FIELD_DATA_OPTION"><%= textListHeader[SESS_LANGUAGE][23] %></label>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </form>   
                                            </div>
                                            <div class="box-footer">
                                                
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div  class="row" style="margin-left:0px; margin-right:0px; margin-top:10px;">
                                    <div class="col-md-12">
                                        <div id="grafik" >
                                            <div  class="box box-primary">
                                                <div class="box-header">
                                                    <h3 class="box-title"></h3>
                                                    <div class="box-tools pull-right">
                                                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                                                    </div>
                                                </div>
                                                <div class="box-body" >
                                                    <div id="chartPlace" class="col-md-12"></div>
                                                    
                                                </div>
                                                <div class="box-footer">
                                                       &nbsp; 
                                                </div>
                                            </div>
                                        </div>
                                        
                                    </div>
                                </div>
                                <div id="dynamicReportTable" class="row" style="margin-left:0px; margin-right:0px; margin-top:10px;">
                                    
                                </div>
                                                    
                                <div id="dynamicReportTablePerLocation" class="row" style="margin-left:0px; margin-right:0px; margin-top:10px;">
                                    
                                </div>
                            </td>
                        </tr>
                        
                    </table>
                </td>
            </tr>
            <tr> 
                <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
                    <%if(menuUsed == MENU_ICON){%>
                    <%@include file="../../../../styletemplate/footer.jsp" %>

                    <%}else{%>
                    <%@ include file = "../../../../main/footer.jsp" %>
                    <%}%>
                    <!-- #EndEditable --> </td>
            </tr>
        </table>
        </div>
    </body>
    <script src="../../../styles/bootstrap/js/jquery.min.js" type="text/javascript"></script>
    <script src="../../../styles/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>  
    <script src="../../../styles/dimata-app.js" type="text/javascript"></script>
    <script type="text/javascript" src="../../../styles/datepicker/bootstrap-datetimepicker.js" charset="UTF-8"></script>
    <script type="text/javascript" src="../../../styles/datepicker/bootstrap-datetimepicker.id.js" charset="UTF-8"></script>
    <script src="../../../styles/iCheck/icheck.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="../../../styles/select2/js/select2.min.js"></script>
    <script type="text/javascript" src="../../../styles/chart/highcharts.js"></script>
    <script type="text/javascript">
    $(document).ready(function(){   
        var dataSend;
        var command;
        var dataFor;
        var approot = "<%= approot%>";
        
        var modalSetting = function(elementId, backdrop, keyboard, show){
                $(elementId).modal({
                    backdrop	: backdrop,
                    keyboard	: keyboard,
                    show	: show
                });
            };
                
        var getDataFunction = function(onDone, onSuccess, approot, command, dataSend, servletName, dataAppendTo, notification, dataType){		
            $(this).getData({
               onDone	: function(data){
                   onDone(data);
               },
               onSuccess	: function(data){
                    onSuccess(data);
               },
               approot	: approot,
               dataSend	: dataSend,
               servletName	: servletName,
               dataAppendTo	: dataAppendTo,
               notification : notification,
               ajaxDataType	: dataType
            });
        };
        
        var getItemByCategory = function(){          
            var value = $("#FRM_FIELD_CATEGORY_ID").val();
            dataSend = "command=<%=Command.NONE%>&FRM_FIELD_DATA_FOR=getItemByCategory&FRM_FIELD_CATEGORY_ID="+value+"";
            onSuccess = function(data){

            };
            onDone = function(data){
                $('.selects2').select2();
            };
            getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxStokReport", ".itemPlace", true, "json");
            
        };
        
        var getUserDoc = function(){
            dataSend = "command=<%=Command.NONE%>&FRM_FIELD_DATA_FOR=getUserDoc";
            onSuccess = function(data){};
            onDone = function(data){ $('.selects2').select2();};
            
            getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxStokReport", "#userDocPlace", true, "json");
        };
        
        var getCashier = function(){
            dataSend = "command=<%=Command.NONE%>&FRM_FIELD_DATA_FOR=getCashier";
            onSuccess = function(data){};
            onDone = function(data){ $('.selects2').select2();};
           
            getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxStokReport", "#cashierPlace", true, "json");
        };
        
        
        
        //-------------------------------
        getItemByCategory("#FRM_FIELD_CATEGORY_ID");
        getUserDoc();
        getItemByCategory();
        getCashier();
        $('.selects2').select2();
        $("#FRM_FIELD_CATEGORY_ID").change(function(){
            getItemByCategory();
        });
    });
    </script>
    <script type="text/javascript">
        
        $(function () {
            $('.dates').datetimepicker({
                autoclose: true,
                todayBtn: true,
                format: 'mm/dd/yyyy',
                minView:2
            });
        });
    </script>
</html>
