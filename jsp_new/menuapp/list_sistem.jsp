
<div class="row">
    <div class="col-md-3">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #EC87C0">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Sistem" : "System"%></h3>
                <div class="box-tools pull-right">
                    <button style="color: white" type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                    </button>
                </div>
                <!-- /.box-tools -->
            </div>
            <!-- /.box-header -->
            <div class="box-body">
                <ul class="nav nav-stacked">                
                    <li>
                        <a href="<%=approot%>/system/user_list.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pemakai" : "Users"%>
                        </a>
                    </li>                
                    <li>
                        <a href="<%=approot%>/system/grouplist.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Group" : "Group"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/system/privilegelist.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Hak Akses" : "Privilege"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/system/sysprop.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pengaturan Aplikasi" : "System Setting"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/styletemplate/chage_template.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Modif Template" : "Modif Template"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/system/open_document.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Open Status Dokument" : "Open Document Status"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>

    <div class="col-md-3">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #4FC1E9">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Tutup Periode" : "Close Period"%></h3>
                <div class="box-tools pull-right">
                    <button style="color: white" type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                    </button>
                </div>
                <!-- /.box-tools -->
            </div>
            <!-- /.box-header -->
            <div class="box-body">
                <ul class="nav nav-stacked">               
                    <li>
                        <a href="<%=approot%>/master/closing/closing_monthly.jsp">

                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Setting Tutup Periode" : "Close Period"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
<%if(useForRaditya.equals("")){ %>
    <div class="col-md-3">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #A0D468">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pertukaran Data" : "Data Exchange"%></h3>
                <div class="box-tools pull-right">
                    <button style="color: white" type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                    </button>
                </div>
                <!-- /.box-tools -->
            </div>
            <!-- /.box-header -->
            <div class="box-body">
                <ul class="nav nav-stacked">                
                    <li>
                        <a href="<%=approot%>/master/DBconnection/connection.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Internet Connection" : "Internet Connection"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/master/data_sync/transfer_data.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Transfer Ke Outlet" : "Transfer To Outlet"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/master/data_sync/transfer_data_to_server.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Transfer Penjualan" : "Sales Transfer"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <%} %>
                        
    <div class="col-md-3">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #FFCE54">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Bantuan" : "Help"%></h3>
                <div class="box-tools pull-right">
                    <button style="color: white" type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                    </button>
                </div>
                <!-- /.box-tools -->
            </div>
            <!-- /.box-header -->
            <div class="box-body">
                <ul class="nav nav-stacked">                
                    <li>
                        <a href="javascript:openHelp()">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Bantuan" : "Help"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>
