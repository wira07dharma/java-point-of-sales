<%-- 
    Document   : list_cashless
    Created on : Jun 6, 2019, 5:16:35 PM
    Author     : Dimata 007
--%>
<div class="row">
    <div class="col-md-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #A0D468">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Master" : "Master"%></h3>
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
                        <a href="<%=approot%>/masterdata/company.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Company" : "Company"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/masterdata/master_employee.jsp?typemenu=0">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Employee" : "Employee"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/system/userlist.jsp?typemenu=0">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "User" : "User"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/master/location/location.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Location" : "Location"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/master/location/room.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Room/Bar" : "Room/Bar"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/master/material/srcmaterial.jsp?typemenu=0">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Item" : "Item"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #4FC1E9">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Event" : "Event"%></h3>
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
                        <a href="<%=approot%>/master/cashless/event_list.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Event Setup" : "Event Setup"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/master/cashless/mapping_event_item.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Map Item" : "Map Item"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/master/cashless/mapping_event_bar.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Map Bar" : "Map Bar"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/master/cashless/map_event_user.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Map User" : "Map User"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #AC92EC">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Report" : "Report"%></h3>
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
                        <a href="<%=approot%>/master/cashless/report_registration.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Cashier Summary and Detail" : "Cashier Summary and Detail"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/master/cashless/transaction_report.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Summary Transaction Item" : "Summary Transaction Item"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/master/cashless/sales_and_payment.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Sales And Payment Report" : "Sales and Payment Report"%>
                        </a>
                    </li>
					<li>
                        <a href="<%=approot%>/master/cashless/trans_cust.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Transaction By Customer ID" : "Transaction By Customer ID"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>
