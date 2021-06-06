<%-- 
    Document   : user_edit
    Created on : Dec 26, 2019, 2:07:01 PM
    Author     : Regen
--%>

<%@page import="org.json.JSONArray"%>
<%@page import="com.dimata.services.WebServices"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.dimata.posbo.entity.admin.PstMappingUserGroup"%>
<%@page import="com.dimata.posbo.entity.admin.MappingUserGroup"%>
<%@page import="com.dimata.posbo.session.admin.SessAppUser"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.common.entity.custom.DataCustom"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.harisma.entity.employee.PstEmployee"%>
<%@page import="com.dimata.harisma.entity.employee.Employee"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.common.form.location.CtrlLocation"%>
<%@page import="com.dimata.common.form.location.FrmLocation"%>
<%@page import="com.dimata.posbo.entity.masterdata.Company"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCompany"%>
<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@page import="com.dimata.posbo.entity.admin.AppGroup"%>
<%@page import="com.dimata.posbo.entity.admin.PstAppGroup"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.form.admin.FrmAppUser"%>
<%@page import="com.dimata.posbo.form.admin.CtrlAppUser"%>
<%@ include file = "../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_ADMIN, AppObjInfo.G2_ADMIN_USER, AppObjInfo.OBJ_ADMIN_USER_USER);%>
<%@ include file = "../main/checkuser.jsp" %>
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
    int iCommand = FRMQueryString.requestCommand(request);
    long apOID = FRMQueryString.requestLong(request, "user_oid");
    int alert = FRMQueryString.requestInt(request, "alert");
    long pegawaiId = FRMQueryString.requestLong(request, "pegawaiId");
    String namaPegawai = FRMQueryString.requestString(request, "namaPegawai");
    String[] groupPemakai = FRMQueryString.requestStringValues(request, "GROUP_PEMAKAI");
    String[] userGroup = request.getParameterValues("USER_GROUP");
    String[] namaPerusahaan = FRMQueryString.requestStringValues(request, "NAMA_PERUSAHAAN");

    //Untuk Assign Time Perlu Ditinjau lagi untuk kodingan, agar lebih efisiensi by WiraDharma :)
    String startTime = FRMQueryString.requestString(request, FrmAppUser.fieldNames[FrmAppUser.FRM_FIELD_START_TIME]);
    String endTime = FRMQueryString.requestString(request, FrmAppUser.fieldNames[FrmAppUser.FRM_FIELD_END_TIME]);
    String sTime = startTime + ":00";
    String eTime = endTime + ":00";
    String tglMulai = Formater.formatDate(new java.util.Date(), "yyyy-MM-dd");
    String tglAkhir = Formater.formatDate(new java.util.Date(), "yyyy-MM-dd");
    String timeStart = tglMulai + " " + sTime;
    String timeEnd = tglAkhir + " " + eTime;
    
    int start = 0;
    int recordToGet = 0;
    String whereClause = "";
    String order = "";
    int excCode = FRMMessage.NONE;
    Employee employee = new Employee();
    CtrlLocation ctrlLocation = new CtrlLocation(request);
    Location location = ctrlLocation.getLocation();

    CtrlAppUser ctrlAppUser = new CtrlAppUser(request);
    excCode = ctrlAppUser.action(iCommand, apOID, request);

    AppUser ap = ctrlAppUser.getAppUser();
    FrmAppUser frmAppUser = ctrlAppUser.getForm();
    long oidEmployee = PstEmployee.getEmployeeByName(namaPegawai);
    Employee emp = new Employee();
    apOID = ap.getOID();
    String PegawaiName = "";
    try {
        if (iCommand == Command.SAVE) {
            if (pegawaiId != 0) {
//                emp = PstEmployee.fetchExc(oidEmployee);
                ap.setEmployeeId(pegawaiId);
            }
            ap.setStartTime(Formater.formatDate(timeStart, "yyyy-MM-dd hh:mm:ss"));
            ap.setEndTime(Formater.formatDate(timeEnd, "yyyy-MM-dd hh:mm:ss"));
            long user = PstAppUser.update(ap);
        }

        String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
        String url = hrApiUrl + "/employee/employee-fetch/" + ap.getEmployeeId();
        JSONObject objAO = WebServices.getAPI("", url);
        if (objAO.length() > 0) {
            PegawaiName = objAO.optString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], "-");
        }

    } catch (Exception e) {
    }
    long employeeOID = 0;

    long oidCompany = userSession.getAppUser().getCompanyId();
    try {
        employeeOID = ap.getEmployeeId();
        employee = PstEmployee.fetchExc(employeeOID);
    } catch (Exception e) {
    }
    
    //Action Array Untuk Simpan Group Pemakai
    if (groupPemakai != null) {
        PstMappingUserGroup.deleteGroupByUser(apOID);
        for (int i = 0; i < groupPemakai.length; i++) {
            MappingUserGroup mg = new MappingUserGroup();
            try {
                mg.setGroupUserId(Long.parseLong(groupPemakai[i]));
                mg.setCompanyId(0);
                mg.setUserId(apOID);
                long oidMg = PstMappingUserGroup.insertExc(mg);

            } catch (Exception e) {
            }
        }
    }
    String whereGroup = PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_USER_ID] + " = " + apOID;
    Vector groupUse = PstMappingUserGroup.list(start, recordToGet, whereGroup, order);

    //Action Array Untuk Simpan Nama Perusahaan
    if (namaPerusahaan != null) {
        PstMappingUserGroup.deleteCompByUser(apOID);
        for (int i = 0; i < namaPerusahaan.length; i++) {
            MappingUserGroup mg = new MappingUserGroup();
            try {
                mg.setCompanyId(Long.parseLong(namaPerusahaan[i]));
                mg.setGroupUserId(0);
                mg.setUserId(apOID);
                long oidMg = PstMappingUserGroup.insertExc(mg);

            } catch (Exception e) {
            }
        }
    }
    String whereComp = PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_USER_ID] + " = " + apOID;
    Vector comp = PstMappingUserGroup.list(start, recordToGet, whereComp, order);

    //Untuk Mendapatkan Posisi User
    Vector listUserPosition = PstAppGroup.list(start, recordToGet, whereClause, order);
    Vector groups = SessAppUser.getUserGroup(apOID);
    //Untuk Nama Perusahaan
    String whereCompany = "";
    Vector listPerusahaan = PstCompany.list(start, recordToGet, whereCompany, order);
    //Get Location
    String whereLocation = "";
    String orderBy = PstLocation.fieldNames[PstLocation.FLD_NAME];
    Vector listLocation = PstLocation.list(start, recordToGet, whereLocation, orderBy);

    Vector userAssign1 = PstDataCustom.getDataCustomWhere(apOID, "user_location_map");
    Vector userAssign2 = PstDataCustom.getDataCustomWhere(apOID, "user_location_transfer");
    Vector userAssign3 = PstDataCustom.getDataCustomWhere(apOID, "user_view_sale_stock_report_location");
    Vector userAssign4 = PstDataCustom.getDataCustomWhere(apOID, "user_create_document_location");
    Vector userAssign5 = PstDataCustom.getDataCustomWhere(apOID, "user_credit_view");
    Vector userAssign6 = PstDataCustom.getDataCustomWhere(apOID, "day_assign");
    Vector userAssign7 = PstDataCustom.getDataCustomWhere(apOID, "sales_assign");
    Vector userAssign8 = PstDataCustom.getDataCustomWhere(apOID, "delivery_assign");
    Vector userAssign9 = PstDataCustom.getDataCustomWhere(apOID, "user_assign_single_location");

    String fldName1 = frmAppUser.fieldNames[frmAppUser.FRM_USER_GROUP] + "_DC";
    String fldName2 = frmAppUser.fieldNames[frmAppUser.FRM_USER_GROUP] + "_TF";
    String fldName3 = frmAppUser.fieldNames[frmAppUser.FRM_USER_GROUP] + "_SR";
    String fldName4 = frmAppUser.fieldNames[frmAppUser.FRM_USER_GROUP] + "_DT";
    String fldName5 = frmAppUser.fieldNames[frmAppUser.FRM_USER_GROUP] + "_CV";
    String fldName6 = frmAppUser.fieldNames[frmAppUser.FRM_USER_GROUP] + "_DY";
    String fldName7 = frmAppUser.fieldNames[frmAppUser.FRM_USER_GROUP] + "_SA";
    String fldName8 = frmAppUser.fieldNames[frmAppUser.FRM_USER_GROUP] + "_DA";
    String fldName9 = frmAppUser.fieldNames[frmAppUser.FRM_USER_GROUP] + "_AS";
    
    if (iCommand == Command.SAVE) {
        try {
            JSONArray groupArray = PstAppUser.checkUserGroup(userGroup);
            if(apOID != 0){
                String urlSedana = PstSystemProperty.getValueByName("SEDANA_APP_URL");
                
                JSONObject obj = new JSONObject();
                try {
                    obj = PstAppUser.fetchJSON(apOID);
                    if (userAssign9.size() > 0) {
                        for (int ii = 0; ii < userAssign9.size(); ii++) {
                            DataCustom dataCustom = (DataCustom) userAssign9.get(ii);
                            long oidCustom = Long.parseLong(dataCustom.getDataValue());
                            obj.put("ASSIGN_LOCATION_ID", oidCustom);
                        }
                    }
                    obj.put("USER_GROUP_ASSIGNED", groupArray);
                } catch (Exception e) {
                    System.out.println("Kenapa ya? : "+e.getMessage());
                }

                String url = urlSedana + "/kredit/user-admin/insert";
                System.out.println("URL User to Sedana : " + url);
                JSONObject objStatus = WebServices.postAPI(obj.toString(), url);
                boolean statusObj = objStatus.optBoolean("SUCCES", false);
                if (statusObj) {
                    System.out.println("Status Nambah User : SUCCESS");
                } else {
                    System.out.println("Status Nambah User : GAGAL");
                }
                
            }
            
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
            e.printStackTrace();
        }

    }
    String timeAwal = "00:01";
    String timeAkhir = "23:59";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="../styles/plugin_component.jsp" %>
        <title>Dimata - ProChain POS</title>
        <script>
            <%
                if (alert == 1) {
            %>
            alert("Data Berhasil Disimpan!");
            <%}%>
            function cmdSave() {
                document.FrmAppUser.command.value = "<%=Command.SAVE%>";
                document.FrmAppUser.action = "user_edit.jsp";
                document.FrmAppUser.submit();
            }

            function cmdDelete(oid) {
                document.FrmAppUser.user_oid.value = oid;
                document.FrmAppUser.command.value = "<%=Command.DELETE%>";
                document.FrmAppUser.action = "user_edit.jsp";
                document.FrmAppUser.submit();
            }

            function cmdBack(oid) {
                document.FrmAppUser.user_oid.value = oid;
                document.FrmAppUser.command.value = "<%=Command.LIST%>";
                document.FrmAppUser.action = "user_list.jsp";
                document.FrmAppUser.submit();
            }

            $('#FrmAppUser').submit(function () {
                var data = $(this).serialize();
                var command = "<%=Command.SAVE%>";
                window.location = "user_list.jsp?" + data + "&command=" + command + "&alert=1";
            });
        </script>
        <style>
            body {
                background-color: #eee;
                min-height: 1300;
            }
            textArea{
                min-height: 75px;
            }
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
            .p {
                margin: 10px 15px 10px 5px;
            }
            .col-sm-2 {
                width: 16.5%;
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
            .col-sm-12 {
                width: 100%;
                margin-bottom: 1px;
            }
            .input-group .input-group-addon {
                border-radius: 0px 4px 4px 0px !important;
                height: 30px;
            }
            .form-control[disabled], .form-control[readonly], fieldset[disabled] .form-control {
                cursor: not-allowed;
                background-color: #fff !important;
                opacity: 1;
            }
        </style>
    </head>
    <body>
        <section class="content-header">
            <h1>Manajemen Sistem<small> Pemakai</small> </h1>
            <ol class="breadcrumb">
                <li>Sistem</li>
                <li>Pemakai</li>
                <li>Detail</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-primary">
                <div class="box-header with-border">
                    <label class="box-title pull-left">User Detail</label>
                </div>
                <div class="box-body">
                    <form name="FrmAppUser" id="FrmAppUser" method="post" action="">
                        <input type="hidden" name="command" value="">
                        <input type="hidden" name="user_oid" value="<%=apOID%>">
                        <div class="row">
                            <div class="col-md-12">
                                <!--Left Side-->
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="col-sm-4">ID Pemakai</label>
                                        <div class="input-group col-sm-8">
                                            <input type="text" class="form-control input-sm" name="<%=frmAppUser.fieldNames[frmAppUser.FRM_LOGIN_ID]%>" value="<%=ap.getLoginId()%>">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4">Password</label>
                                        <div class="input-group col-sm-8">
                                            <input type="password" id="pass" class="form-control input-sm" name="<%=frmAppUser.fieldNames[frmAppUser.FRM_PASSWORD]%>" value="<%=ap.getPassword()%>">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4">Konfirmasi Password</label>
                                        <div class="input-group col-sm-8">
                                            <input type="password" id="confirmpass" class="form-control input-sm" name="<%=frmAppUser.fieldNames[frmAppUser.FRM_CFRM_PASSWORD]%>" value="<%=ap.getPassword()%>">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4">Nama Lengkap</label>
                                        <div class="input-group col-sm-8">
                                            <input type="text" class="form-control input-sm" name="<%=frmAppUser.fieldNames[frmAppUser.FRM_FULL_NAME]%>" value="<%=ap.getFullName()%>">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4">Status Pemakai</label>
                                        <div class="input-group col-sm-8">
                                            <select class="form-control input-sm select2" name="<%=frmAppUser.fieldNames[frmAppUser.FRM_USER_STATUS]%>">
                                                <option value="<%=ap.STATUS_NEW%>" <%= (ap.getUserStatus() == ap.STATUS_NEW ? "selected" : "")%>><%=ap.statusTxt[0]%></option>
                                                <option value="<%=ap.STATUS_LOGIN%>" <%= (ap.getUserStatus() == ap.STATUS_LOGIN ? "selected" : "")%>><%=ap.statusTxt[1]%></option>
                                                <option value="<%=ap.STATUS_LOGOUT%>" <%= (ap.getUserStatus() == ap.STATUS_LOGOUT ? "selected" : "")%>><%=ap.statusTxt[2]%></option>
                                                <option value="<%=ap.STATUS_NON_AKTIF %>" <%= (ap.getUserStatus() == ap.STATUS_NON_AKTIF ? "selected" : "")%>><%=ap.statusTxt[3]%></option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4">Nama Perusahaan</label>
                                        <div class="input-group col-sm-8">
                                            <select class="form-control input-sm col-sm-8 select2 col-sm-8" name="NAMA_PERUSAHAAN" multiple="">
                                                <%
                                                    for (int i = 0; i < listPerusahaan.size(); i++) {
                                                        Company company = (Company) listPerusahaan.get(i);
                                                        long oidCom = company.getOID();
                                                        String selected = "";
                                                        for (int x = 0; x < comp.size(); x++) {
                                                            MappingUserGroup mp = (MappingUserGroup) comp.get(x);
                                                            long oid = mp.getCompanyId();
                                                            if (oidCom == oid) {
                                                                selected = "selected";
                                                            }
                                                        }
                                                %>
                                                <option value="<%=company.getOID()%>" <%=selected%>><%=company.getCompanyName()%></option>
                                                <%}
                                                %>
                                            </select>
                                        </div>
                                    </div>

                                </div>

                                <!--Right Side-->
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="col-sm-4">ID Pegawai</label>
                                        <div class="input-group col-md-8">
                                            <input type="hidden" name="pegawaiId" id="pegawaiId" value="">
                                            <input type="hidden" name="<%=frmAppUser.fieldNames[frmAppUser.FRM_EMPLOYEE_ID]%>" value="<%=ap.getEmployeeId()%>">
                                            <input readonly="readonly" type="text" class="form-control input-sm" id="namaPegawai" name="namaPegawai" value="<%=PegawaiName%>" style="width: 85%;">
                                            <div class="input-group-addon btn btn-primary listPetugas" id="showPopUp"><i class="fa fa-search"></i></div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4">Email</label>
                                        <div class="input-group col-sm-8">
                                            <input type="text" id="email" class="form-control input-sm" name="<%=frmAppUser.fieldNames[frmAppUser.FRM_EMAIL]%>" value="<%=ap.getEmail()%>">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4">Keterangan</label>
                                        <div class="input-group col-sm-8">
                                            <textarea class="form-control input-sm" name="<%=frmAppUser.fieldNames[frmAppUser.FRM_DESCRIPTION]%>" placeholder="..."><%=ap.getDescription()%></textarea>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4">Group Pemakai</label>
                                        <div class="input-group col-sm-8">
                                            <select class="form-control input-sm select2 col-sm-8" name="GROUP_PEMAKAI" multiple="">
                                                <%

                                                    for (int x = 0; x < PstAppUser.GroupUsingKey.length; x++) {
                                                        long oidGroup = Long.parseLong(PstAppUser.GroupUsingValue[x]);
                                                        String selected = "";
                                                        for (int i = 0; i < groupUse.size(); i++) {
                                                            MappingUserGroup mp = (MappingUserGroup) groupUse.get(i);
                                                            long oid = mp.getGroupUserId();
                                                            if (oidGroup == oid) {
                                                                selected = "selected";
                                                            }
                                                        }
                                                %>
                                                <option value="<%=PstAppUser.GroupUsingValue[x]%>" <%=selected%>><%=PstAppUser.GroupUsingKey[x]%></option>
                                                <%}%>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4">Assign Time</label>
                                        <%if (apOID != 0) {%>
                                        <div class="input-group col-sm-8">
                                            <div class="bootstrap-timepicker">                                              
                                                <input type="text" class="form-control input-sm timepicker" style="width: 40%;" name="<%=frmAppUser.fieldNames[frmAppUser.FRM_FIELD_START_TIME]%>" value="<%=Formater.formatDate(ap.getStartTime(), "HH:mm")%>">
                                            </div>
                                            <span class="input-group-addon" style="float: left;width: 20%;border-radius: 0px !important;"> s/d </span>
                                            <div class="bootstrap-timepicker">           
                                                <input type="text" class="form-control input-sm timepicker" style="width: 40%;" name="<%=frmAppUser.fieldNames[frmAppUser.FRM_FIELD_END_TIME]%>" value="<%=Formater.formatDate(ap.getEndTime(), "HH:mm")%>">
                                            </div>
                                        </div>
                                        <%} else {%>
                                        <div class="input-group col-sm-8">
                                            <div class="bootstrap-timepicker">                                              
                                                <input type="text" class="form-control input-sm timepicker" style="width: 40%;" name="<%=frmAppUser.fieldNames[frmAppUser.FRM_FIELD_START_TIME]%>" value="<%=timeAwal%>">
                                            </div>
                                            <span class="input-group-addon" style="float: left;width: 20%;border-radius: 0px !important;"> s/d </span>
                                            <div class="bootstrap-timepicker">           
                                                <input type="text" class="form-control input-sm timepicker" style="width: 40%;" name="<%=frmAppUser.fieldNames[frmAppUser.FRM_FIELD_END_TIME]%>" value="<%=timeAkhir%>">
                                            </div>
                                        </div>
                                        <%}%>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!--Assign Location-->
                        <div class="row">
                            <div class="col-md-12">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label class="col-sm-2" >Group Assign</label>
                                        <div class="input-group col-sm-10 assign-position">
                                            <%
                                                for (int i = 0; i < listUserPosition.size(); i++) {
                                                    AppGroup ag = (AppGroup) listUserPosition.get(i);
                                                    long oidAppGroup = ag.getOID();
                                                    String checked = "";
                                                    for (int ii = 0; ii < groups.size(); ii++) {
                                                        AppGroup appGroup = (AppGroup) groups.get(ii);
                                                        long oidGroup = appGroup.getOID();
                                                        if (oidAppGroup == oidGroup) {
                                                            checked = "checked";
                                                        }
                                                    }
                                            %>
                                            <input type="checkbox" class="flat-blue" name="<%=frmAppUser.fieldNames[frmAppUser.FRM_USER_GROUP]%>" value="<%=oidAppGroup%>" <%=checked%>><span class="p"><%=ag.getGroupName()%></span>
                                            <% }
                                            %>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div><br>

                        <div class="box box-primary">
                            <div class="box-header with-border">
                                <label class="box-title pull-left">Assign Location</label>
                            </div>
                            <div class="box-body">
                                <div class="row">
                                    <div class="col-md-12">
                                        <!--Left Side-->
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label class="col-sm-12" style="margin-bottom: 10px;">Assign Main Location</label>
                                                <div class="col-sm-12">
                                                </div>
                                                <select class="form-control input-sm col-sm-8 select2 col-sm-8" name="<%=fldName9%>">
                                                    <%
                                                        String oidPusat = PstSystemProperty.getValueByName("ORDER_RADITYA_LOCATION");
                                                        for (int i = 0; i < listLocation.size(); i++) {
                                                            Location loc = (Location) listLocation.get(i);
                                                            long oidLocation = loc.getOID();
                                                            long oidCustom = 0;
                                                            String selected = "";
                                                            if (oidCustom == 0) {
                                                                oidCustom = Long.parseLong(oidPusat);
                                                            }
                                                            if (userAssign9.size() > 0) {
                                                                for (int ii = 0; ii < userAssign9.size(); ii++) {
                                                                    DataCustom dataCustom = (DataCustom) userAssign9.get(ii);
                                                                    oidCustom = Long.parseLong(dataCustom.getDataValue());
                                                                    if (oidLocation == oidCustom) {
                                                                        selected = "selected";
                                                                    }
                                                                }
                                                            } else {
                                                                if (oidLocation == oidCustom) {
                                                                    selected = "selected";
                                                                }
                                                            }
                                                    %>
                                                    <option value="<%=oidLocation%>" <%=selected%>><%=loc.getName()%></option>
                                                    <%}
                                                    %>
                                                </select>
                                            </div>
                                        </div>
                                        <!--Center Side-->
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label class="col-sm-12" style="margin-bottom: 10px;">Assign Delivery Courier Location</label>
                                                <div class="col-sm-12">
                                                </div>
                                                <select class="form-control input-sm col-sm-8 select2 col-sm-8" name="<%=fldName8%>">
                                                    <%
                                                        for (int i = 0; i < listLocation.size(); i++) {
                                                            Location loc = (Location) listLocation.get(i);
                                                            long oidLocation = loc.getOID();
                                                            String selected = "";
                                                            for (int ii = 0; ii < userAssign8.size(); ii++) {
                                                                DataCustom dataCustom = (DataCustom) userAssign8.get(ii);
                                                                long oidCustom = Long.parseLong(dataCustom.getDataValue());
                                                                if (oidLocation == oidCustom) {
                                                                    selected = "selected";
                                                                }
                                                            }
                                                    %>
                                                    <option value="<%=oidLocation%>" <%=selected%>><%=loc.getName()%></option>
                                                    <%}
                                                    %>
                                                </select>
                                            </div>
                                        </div>
                                        <!--Right Side-->
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label class="col-sm-12" style="margin-bottom: 10px;">Assign Sales Location</label>
                                                <div class="col-sm-12">
                                                </div>
                                                <select class="form-control input-sm col-sm-8 select2 col-sm-8" name="<%=fldName7%>">
                                                    <%
                                                        for (int i = 0; i < listLocation.size(); i++) {
                                                            Location loc = (Location) listLocation.get(i);
                                                            long oidLocation = loc.getOID();
                                                            String selected = "";
                                                            for (int ii = 0; ii < userAssign7.size(); ii++) {
                                                                DataCustom dataCustom = (DataCustom) userAssign7.get(ii);
                                                                long oidCustom = Long.parseLong(dataCustom.getDataValue());
                                                                if (oidLocation == oidCustom) {
                                                                    selected = "selected";
                                                                }
                                                            }
                                                    %>
                                                    <option value="<%=oidLocation%>" <%=selected%>><%=loc.getName()%></option>
                                                    <%}
                                                    %>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <hr><br>
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4">
                                            <!--Left Side-->
                                            <div class="form-group">
                                                <label class="col-sm-12" style="margin-bottom: 10px;">Assign View Sales/Stock Report Location </label>
                                                <div class="col-sm-12">
                                                </div>
                                                <%
                                                    for (int i = 0; i < listLocation.size(); i++) {
                                                        Location loc = (Location) listLocation.get(i);
                                                        long oidLocation = loc.getOID();
                                                        String checked = "";
                                                        for (int ii = 0; ii < userAssign1.size(); ii++) {
                                                            DataCustom dataCustom = (DataCustom) userAssign1.get(ii);
                                                            long oidCustom = Long.parseLong(dataCustom.getDataValue());
                                                            if (oidLocation == oidCustom) {
                                                                checked = "checked";
                                                            }
                                                        }
                                                %><div class="col-sm-12">
                                                    <div class="col-sm-12 assign-group">
                                                        <input type="checkbox" class="flat-blue" name="<%=fldName1%>" value="<%=oidLocation%>" <%=checked%>><span class="p"><%=loc.getName()%></span>
                                                    </div></div>
                                                    <%
                                                        }
                                                    %>
                                            </div>

                                        </div>
                                        <!--Center Side-->
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label class="col-sm-12" style="margin-bottom: 10px;">Assign Create Location Document </label>
                                                <div class="col-sm-12">
                                                </div>
                                                <%
                                                    for (int i = 0; i < listLocation.size(); i++) {
                                                        Location loc = (Location) listLocation.get(i);
                                                        long oidLocation = loc.getOID();
                                                        String checked = "";
                                                        for (int ii = 0; ii < userAssign2.size(); ii++) {
                                                            DataCustom dataCustom = (DataCustom) userAssign2.get(ii);
                                                            long oidCustom = Long.parseLong(dataCustom.getDataValue());
                                                            if (oidLocation == oidCustom) {
                                                                checked = "checked";
                                                            }
                                                        }
                                                %><div class="col-sm-12">
                                                    <div class="col-sm-12 assign-group2">
                                                        <input type="checkbox" class="flat-blue" name="<%=fldName2%>" value="<%=oidLocation%>"<%=checked%>><span class="p"><%=loc.getName()%></span>
                                                    </div></div>
                                                    <%
                                                        }
                                                    %>
                                            </div>

                                        </div>
                                        <!--Right Side-->
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label class="col-sm-12" style="margin-bottom: 10px;">Assign Final Document Transaction Location </label>
                                                <div class="col-sm-12">
                                                </div>
                                                <%
                                                    for (int i = 0; i < listLocation.size(); i++) {
                                                        Location loc = (Location) listLocation.get(i);
                                                        long oidLocation = loc.getOID();
                                                        String checked = "";
                                                        for (int ii = 0; ii < userAssign3.size(); ii++) {
                                                            DataCustom dataCustom = (DataCustom) userAssign3.get(ii);
                                                            long oidCustom = Long.parseLong(dataCustom.getDataValue());
                                                            if (oidLocation == oidCustom) {
                                                                checked = "checked";
                                                            }
                                                        }
                                                %><div class="col-sm-12">
                                                    <div class="col-sm-12 assign-group3">
                                                        <input type="checkbox" class="flat-blue" name="<%=fldName3%>" value="<%=oidLocation%>" <%=checked%>><span class="p"><%=loc.getName()%></span>
                                                    </div></div>
                                                    <%
                                                        }
                                                    %>
                                            </div>

                                        </div>
                                    </div>

                                </div>
                                <br><hr><br>
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="col-md-4">
                                            <!--Left Side-->
                                            <div class="form-group">
                                                <label class="col-sm-12" style="margin-bottom: 10px;">Assign Data Exchange Location </label>
                                                <div class="col-sm-12">
                                                </div>
                                                <%
                                                    for (int i = 0; i < listLocation.size(); i++) {
                                                        Location loc = (Location) listLocation.get(i);
                                                        long oidLocation = loc.getOID();
                                                        String checked = "";
                                                        for (int ii = 0; ii < userAssign4.size(); ii++) {
                                                            DataCustom dataCustom = (DataCustom) userAssign4.get(ii);
                                                            long oidCustom = Long.parseLong(dataCustom.getDataValue());
                                                            if (oidLocation == oidCustom) {
                                                                checked = "checked";
                                                            }
                                                        }
                                                %><div class="col-sm-12">
                                                    <div class="col-sm-12 assign-group4">
                                                        <input type="checkbox" class="flat-blue" name="<%=fldName4%>" value="<%=oidLocation%>" <%=checked%>><span class="p"><%=loc.getName()%></span>
                                                    </div></div>
                                                    <%
                                                        }
                                                    %>
                                            </div>

                                        </div>
                                        <!--Center Side-->
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label class="col-sm-12"  style="margin-bottom: 10px;">Assign Credit View </label>
                                                <div class="col-sm-12">
                                                </div>
                                                <%
                                                    for (int i = 0; i < listLocation.size(); i++) {
                                                        Location loc = (Location) listLocation.get(i);
                                                        long oidLocation = loc.getOID();
                                                        String checked = "";
                                                        for (int ii = 0; ii < userAssign5.size(); ii++) {
                                                            DataCustom dataCustom = (DataCustom) userAssign5.get(ii);
                                                            long oidCustom = Long.parseLong(dataCustom.getDataValue());
                                                            if (oidLocation == oidCustom) {
                                                                checked = "checked";
                                                            }
                                                        }
                                                %><div class="col-sm-12">
                                                    <div class="col-sm-12 assign-group5">
                                                        <input type="checkbox" class="flat-blue" name="<%=fldName5%>" value="<%=oidLocation%>" <%=checked%>><span class="p"><%=loc.getName()%></span>
                                                    </div></div>
                                                    <%
                                                        }
                                                    %>
                                            </div>

                                        </div>
                                        <!--Right Side-->
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label class="col-sm-12" style="margin-bottom: 10px;">Assign Day Login </label>
                                                <div class="col-sm-12">
                                                </div>
                                                <%
                                                    for (int i = 0; i < FrmAppUser.searchKey.length; i++) {
                                                        long oidDay = Long.parseLong(FrmAppUser.searchValue[i]);
                                                        String checked = "";
                                                        for (int ii = 0; ii < userAssign6.size(); ii++) {
                                                            DataCustom dataCustom = (DataCustom) userAssign6.get(ii);
                                                            long oidCustom = Long.parseLong(dataCustom.getDataValue());
                                                            if (oidDay == oidCustom) {
                                                                checked = "checked";
                                                            }
                                                        }
                                                %><div class="col-sm-12">
                                                    <div class="col-sm-12 assign-group6">
                                                        <input type="checkbox" class="flat-blue" name="<%=fldName6%>" value="<%=FrmAppUser.searchValue[i]%>"<%=checked%>><span class="p"><%=FrmAppUser.searchKey[i]%></span>
                                                    </div></div>
                                                    <%
                                                        }
                                                    %>
                                            </div>

                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="box-footer">
                    <div class="pull-right">
                        <a class="btn btn-primary" id="simpan-data">Simpan</a>
                        <a class="btn btn-danger" href="javascript:cmdBack('<%=apOID%>')">Kembali</a>
                    </div>
                </div>
            </div>
        </section>
        <div id="modalCheck" class="modal fade" tabindex="-1">
            <div class="modal-dialog modal-lg" style="max-width: 1000px; width: 80%;">
                <div class="modal-content">
                    <div id="dynamicModalHeader"></div>
                    <div id="dynamicModalContent"></div>
                    <div class="modal-footer">
                        <button style="float: right;" type="button" data-dismiss="modal" class="btn btn-danger">
                            Pilih
                        </button>              
                    </div>
                </div>
            </div>
        </div>


        <!--Modal Pegawai-->            
        <div class="modal fade" id="modalPetugas" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document" style="min-width: 1000px;">
                <div class="modal-content">
                    <div class="modal-header">
                        <label class="modal-title" id="exampleModalLabel">List Pegawai</label>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <table id="listOfficer" class="table table-striped table-bordered" style="width:100%">
                            <thead class="headerList">
                                <tr>
                                    <th>No</th>
                                    <th>Nama Lengkap</th>
                                    <th>Alamat</th>
                                    <th>Telepon</th>
                                    <th>Jenis Kelamin</th>
                                    <th>Tempat Lahir</th>
                                    <th>Action</th>
                                </tr>
                            </thead>

                        </table>
                    </div>
                    <div class="modal-footer">
                    </div>
                </div>
            </div>
        </div>
        <script>
            $(document).ready(function () {
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
                    $(elementIdParent).find('table').addClass('table-bordered table-striped table-hover').attr({'id': elementId});
                    $("#" + elementId).dataTable({
                        "bDestroy": true,
                        "ordering": false,
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
                                "sLast ": "<%=dataTableTitle[SESS_LANGUAGE][7]%>",
                                "sNext ": "<%=dataTableTitle[SESS_LANGUAGE][8]%>",
                                "sPrevious ": "<%=dataTableTitle[SESS_LANGUAGE][9]%>"
                            }
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

                function runDataTable(elementidparent, elementid, servlet, dataFor, callBack) {
                    dataTablesOptions("#" + elementidparent, elementid, servlet, dataFor, callBack);
                }
                $('body').on('click', '.listPetugas', function () {
                    var modalPetugas = $('#modalPetugas');
                    modalPetugas.modal('show');
                    modalPetugas.one('shown.bs.modal', function () {
                        var elementParentId = 'modalPetugas';
                        var elementId = 'listOfficer';
                        var servlet = 'AjaxPegawaiOfficer';
                        var dataFor = 'listPegawai';
                        runDataTable(elementParentId, elementId, servlet, dataFor, null);
                    });

                });
                //Timepicker
                $('.timepicker').timepicker({
                    showInputs: true,
                    showMeridian: false,
                    format: 'hh:mm'
                });

                $('input[type="checkbox"].flat-blue').iCheck({
                    checkboxClass: 'icheckbox_square-blue'
                });

                $('.select2').select2({placeholder: "Semua"});
                $('.selectAll').select2({placeholder: "Semua"});

                $('body').on('click', '.employeeSelect', function () {
                    var oid = $(this).data('oid');
                    var nama = $(this).data('nama');
                    $('#<%=FrmAppUser.fieldNames[FrmAppUser.FRM_EMPLOYEE_ID]%>').val(oid);
                    $('#namaPegawai').val(nama);
                    $('#pegawaiId').val(oid);
                    $('#modalPetugas').modal('hide');

                });
                
                
//                Validation Data Before Sending to Backend
                 $('body').on('click', '#simpan-data', function(){
                     $('#confirmpass').css('border-color', '#d2d6de');
                     var datapegawai = $('#pegawaiId').val();
                     var confirmpass = $('#confirmpass').val();
                     var pass = $('#pass').val();
                     var email = $('#email').val();
                     if(pass !== confirmpass){
                         alert("Konfirmasi password tidak sesuai!!");
                         $('#confirmpass').css('border-color', 'red');
                     }else if(email == '' || email == null){
                         alert("Pastikan Anda menyertakan email!!");
                     }else{
                         cmdSave();
                     }
                 });
            });
        </script>
    </body>
</html>

























