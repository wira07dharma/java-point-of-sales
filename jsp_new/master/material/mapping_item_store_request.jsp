<%-- 
    Document   : mapping_item_store_request
    Created on : Oct 3, 2016, 3:48:47 PM
    Author     : Witar
--%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcReportSale"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.common.entity.location.Location"%>
<%@page import="com.dimata.common.entity.location.PstLocation"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMappingProductLocationStoreRequest"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.form.search.FrmSrcMaterial" %>

<%@ page import = "com.dimata.posbo.session.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ include file = "../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_MAPPING_ITEM_STORE_REQUEST);%>
<%@ include file = "../../main/checkuser.jsp" %>
<%!
    public static final int ADD_TYPE_SEARCH = 0;
    public static final int ADD_TYPE_LIST = 1;

    public static final String textListHeader[][] = 
    {
        {"Lokasi","Kategori"},
        {"Location","Category"}
    };

    public static final String textButton[][] = 
    {
        {"Filter"},
        {"Filter"}
    };

    public String getJspTitle(int index, int language, String prefiks, boolean addBody){
        String result = "";
        if(addBody){
            if(language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){	
                result = textListHeader[language][index] + " " + prefiks;
            }else{
                result = prefiks + " " + textListHeader[language][index];		
            }
        }
        else{
            result = textListHeader[language][index];
        } 
        return result;
    }

%>
<%
    int iCommand = FRMQueryString.requestCommand(request);
    FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Dimata - Prochain POS</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%if(menuUsed == MENU_ICON){%>
        <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>      
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
    </head>
    <body class="" style="background-color:beige"  leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <div class="nonprint">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <%if(menuUsed == MENU_PER_TRANS){%>
            <tr>
                <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
                    <%@ include file = "../../main/header.jsp" %>
                </td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                    <%@ include file = "../../main/mnmain.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
            <%}else{%>
            <tr bgcolor="#FFFFFF">
                <td height="10" ID="MAINMENU">
                    <%@include file="../../styletemplate/template_header.jsp" %>
                </td>
            </tr>
            <%}%>
            <tr> 
                <link href="../../styles/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
                <link href="../../styles/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />
                <link href="../../styles/dist/css/AdminLTE.css" rel="stylesheet" type="text/css" />
                <link href="../../styles/dist/css/skins/_all-skins.css" rel="stylesheet" type="text/css" />
                <link href="../../styles/font-awesome/font-awesome.css" rel="stylesheet" type="text/css" />
                <link href="../../styles/iCheck/flat/blue.css" rel="stylesheet" type="text/css" />
                <link href="../../styles/iCheck/all.css" rel="stylesheet" type="text/css" />
                <link href="../../styles/select2/css/select2.min.css" rel="stylesheet" type="text/css" />
                <link href="../../styles/datepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
                
             
                <td valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                        <tr> 
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
                                <div   class="row" style="margin-left:0px; margin-right:0px; margin-top:10px; ">
                                    <div class="col-md-8">                                       
                                        <div class="box box-primary">
                                            <div class="box-header">
                                                <h3 class="box-title">Filter</h3>
                                                <div class="box-tools pull-right">
                                                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                                                </div>
                                            </div>
                                            <div class="box-body">
                                                <form name="frmsrcmappinglocation" id="frmsrcmappinglocation" class="form-horizontal" role="form">
                                                    <input type="hidden" name="language" id="language" value="<%= SESS_LANGUAGE%>">                                                 
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-2" for="pwd">
                                                            <%=getJspTitle(0,SESS_LANGUAGE,"",true)%>
                                                        </label>
                                                        <div class="col-sm-10">
                                                            <% 
                                                                Vector val_locationid = new Vector(1,1);
                                                                Vector key_locationid = new Vector(1,1); 

                                                               
                                                                String whereLocation = "" 
                                                                    +" "+PstLocation.fieldNames[PstLocation.FLD_DEPARTMENT_ID]+"='0'";
                                                                Vector vt_loc = PstLocation.list(0, 0, whereLocation, "");

                                                                
                                                                for(int d=0;d<vt_loc.size();d++){
                                                                    Location loc = (Location)vt_loc.get(d);
                                                                    val_locationid.add(""+loc.getOID()+"");
                                                                    key_locationid.add(loc.getName());
                                                                }
                                                            %> 
                                                            <%=ControlCombo.draw(frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_LOCATION_ID], null, "", val_locationid, key_locationid, "", "form-control")%>  
                                                        </div>
                                                    </div> 
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-2" for="pwd">
                                                            <%=getJspTitle(1,SESS_LANGUAGE,"",true)%>
                                                        </label>
                                                        <div class="col-sm-10">
                                                            <select id="<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CATEGORYID]%>"  name="<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CATEGORYID]%>" class="form-control">
                                                                <option value="-1">Semua Category</option>
                                                                <%
                                                                Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);                                                                
                                                                Vector materGroup = PstCategory.structureList(masterCatAcak) ;
                                                                Vector vectGroupVal = new Vector(1,1);
                                                                Vector vectGroupKey = new Vector(1,1);
                                                                if(materGroup!=null && materGroup.size()>0) {
                                                                    String parent="";
                                                                    Vector<Long> levelParent = new Vector<Long>();
                                                                    for(int i=0; i<materGroup.size(); i++) {
                                                                        Category mGroup = (Category)materGroup.get(i);
                                                                        if(mGroup.getCatParentId()!=0){
                                                                            for(int lv=levelParent.size()-1; lv > -1; lv--){
                                                                                long oidLevel=levelParent.get(lv);
                                                                                if(oidLevel==mGroup.getCatParentId()){
                                                                                    break;
                                                                                }else{
                                                                                    levelParent.remove(lv);
                                                                                }
                                                                            }
                                                                            parent="";
                                                                            for(int lv=0; lv<levelParent.size(); lv++){
                                                                               parent=parent+"&nbsp;&nbsp;";
                                                                            }
                                                                            levelParent.add(mGroup.getOID());

                                                                        }else{
                                                                            levelParent.removeAllElements();
                                                                            levelParent.add(mGroup.getOID());
                                                                            parent="";
                                                                        }
                                                                        %>
                                                                            <option value="<%=mGroup.getOID()%>"><%=parent%><%=mGroup.getName()%></option>
                                                                        <%
                                                                    }
                                                                } else {
                                                                    vectGroupVal.add("Tidak Ada Category");
                                                                    vectGroupKey.add("-1");
                                                                }  
                                                                %>
                                                           </select>
                                                        </div>
                                                    </div> 
                                                    <div class="form-group">
                                                        <div class="col-sm-offset-2 col-sm-10">
                                                            <button id="btnSearch" type="button" class="btn btn-primary">
                                                                <i class="fa fa-filter"></i>
                                                                <%= textButton[SESS_LANGUAGE][0]%>
                                                            </button>
                                                        </div>
                                                    </div>
                                                </form>   
                                            </div>
                                            <div class="box-footer">
                                                
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div id="dataPlace" class="col-md-12"></div>
                                
                               
                            </td>
                        </tr>
                        
                    </table>
                </td>
            </tr>
            <tr> 
                <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
                    <%if(menuUsed == MENU_ICON){%>
                    <%@include file="../../styletemplate/footer.jsp" %>

                    <%}else{%>
                    <%@ include file = "../../main/footer.jsp" %>
                    <%}%>
                    <!-- #EndEditable --> </td>
            </tr>
        </table>
        </div>
    </body>
    <script src="../../styles/bootstrap/js/jquery.min.js" type="text/javascript"></script>
    <script src="../../styles/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>  
    <script src="../../styles/dimata-app.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            var dataSend;
            var command;
            var dataFor;
            var approot = "<%= approot%>";
            var language = $("#language").val();
            var modalSetting = function(elementId, backdrop, keyboard, show){
                $(elementId).modal({
                    backdrop	: backdrop,
                    keyboard	: keyboard,
                    show	: show
                });
            };
                
            var getDataFunction = function(onDone, onSuccess, approot, command, dataSend, servletName, dataAppendTo, notification, dataType){
                /*
                 * getDataFor	: # FOR PROCCESS FILTER
                 * onDone	: # ON DONE FUNCTION,
                 * onSuccess	: # ON ON SUCCESS FUNCTION,
                 * approot	: # APPLICATION ROOT,
                 * dataSend	: # DATA TO SEND TO THE SERVLET,
                 * servletName  : # SERVLET'S NAME,
                 * dataType	: # Data Type (JSON, HTML, TEXT)
                 */
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
            
            var filterData = function(elementId){
                $(elementId).click(function(){
                    loadData();
                });
            };
            
            var loadData = function(){
                var oidLocation = $("#FRM_FIELD_LOCATION_ID").val();
                var categoryId = $("#FRM_FIELD_CATEGORYID").val();
                command ="<%= Command.LIST%>";
                dataFor = "listItemMapping";
                dataSend = {
                    "FRM_FIELD_DATA_FOR"      : dataFor,
                    "FRM_FIELD_OID_LOCATION"  : oidLocation,
                    "FRM_FIELD_LANGUAGE"      : language,
                    "FRM_FIELD_CATEGORYID"    : categoryId,
                    "command"		      : command
                };
                onSuccess = function(data){};
                onDone = function(data){
                    saveMapping("#saveMapping");
                };
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMappingItemStore", "#dataPlace",true, "json");
            };
            
            var saveMapping = function(elementId){
                $(elementId).click(function(){
                    var vals = "";
                    $(".checkValue").each(function(){
                        if($(this).is(":checked")){
                            if(vals.length == 0){
                                vals += ""+$(this).val();
                            }else{
                                vals += ","+$(this).val();
                            }
                        }
                    });
                    if (vals.length>0){
                        command = "<%= Command.SAVE%>";
                        dataFor = "saveItemMapping";
                        var mainLocation = $("#FRM_FIELD_LOCATION_ID").val();
                        dataSend = {
                            "FRM_FIELD_DATA_FOR"           : dataFor,
                            "FRM_FIELD_OID_ITEM_LOCATION"  : vals,
                            "FRM_FIELD_LANGUAGE"           : language,
                            "FRM_FIELD_MAIN_LOCATION"      : mainLocation,
                            "command"		           : command
                        };
                        onSuccess = function(data){};
                        onDone = function(data){
                            loadData();
                        };
                        getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxMappingItemStore", "",true, "json");
                    }else{
                        alert ("There is no item mapping...");
                    }
                });
            };
            
            filterData("#btnSearch");
        });
    </script>
</html>
