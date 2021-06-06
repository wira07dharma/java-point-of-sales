<%if(useForRaditya.equals("0")){ %>
<div class="box box-solid">
    <div class="box-header with-border" style="background-color:  #A0D468">
        <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Penjualan" : "Sales Report"%></h3>
        <div class="box-tools pull-right">
            <button style="color: white" type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
            </button>
        </div>
        <!-- /.box-tools -->
    </div>
    <!-- /.box-header -->
    <div class="box-body">
        <div class="col-md-4">
            <ul class="nav nav-stacked">
                <li>
                    <a href="<%=approot%>/store/sale/report/src_reportsale_global.jsp">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Penjualan Global" : "Sales Global"%>
                    </a>
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/src_reportsale_detail_global.jsp">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Penjualan Detail" : "Sales Detail"%>
                    </a>
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/src_reportsale_global_margin.jsp">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Gross Margin" : "Gross Margin"%>
                    </a>
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/src_reportsalerekaptanggal.jsp">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Rekap Harian " : "Daily Summary"%>
                    </a>
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/src_reportsale_detail.jsp?sale_type=6">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? " Invoice Detail" : "Detail Invoice "%>
                    </a>
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/src_reportpendingorder.jsp">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pending Order" : "Pending Order"%>
                    </a>
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/src_reportsale.jsp?sale_type=6">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Invoice" : "By Invoice"%>
                    </a>
                </li>
                 <li>
                    <a href="<%=approot%>/store/sale/report/src_report_cash_opening.jsp">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Cash Opening" : "Per Cash Opening"%>
                    </a>
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/src_reportsale_detail_global_100.jsp?urutan=1">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "100 Item Terlaris" : "100 Best Seller Items"%>
                    </a>
                </li>
                    <%if (typeOfBusinessDetail != 0) {%>
                 <li>
                    <a href="<%=approot%>/store/sale/report/src_reportrekap.jsp">
                         <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Rekap Per KSG" : "Rekap Per KSG"%>
                    </a>
                </li>
                <%}%>
                <% if (privViewReportIncentive) {%>
                <li>
                    <a href="<%=approot%>/store/sale/report/report_insentif.jsp">
                         <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Insentif" : "Incentive"%>
                    </a>
                </li>
                <%}%>
               <%if (typeOfBusinessDetail != 0) {%>
                <li>
                    <a href="<%=approot%>/store/sale/report/report_trend_penjualan.jsp">
                         <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Tren Penjualan" : "Sales Trend"%>
                    </a>
                </li>
                <%}%>
				<% if (typeOfBusinessDetail == 2){ %>
                <li>
                    <a href="<%=approot%>/store/sale/report/src_report_sales_profit_lost.jsp">
                         <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Penjualan Laba/Rugi" : "Profit/Lost Sales"%>
                    </a>
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/src_report_detail_order.jsp">
                         <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pemesanan" : "Order"%>
                    </a> 
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/src_report_service.jsp">
                         <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Service" : "Service"%>
                    </a>
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/src_report_sales_return.jsp">
                         <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Retur Penjualan" : "Sales Return"%>
                    </a>
                </li>
				<% } %>
            </ul>
        </div>

        <div class="col-md-4">
            <ul class="nav nav-stacked">
                <% if (!typeOfBusiness.equals("0")) {%>
                <li>
                    <a href="<%=approot%>/store/sale/report/report_daily_cashier.jsp">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Harian Kasir" : "Daily Cashier Report"%>
                    </a>
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/report_summary_sales.jsp">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Rangkuman Penjualan" : "Summary Sales Report"%>
                    </a>
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/report_top_menu.jsp">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Top Menu" : "Top Menu Report"%>
                    </a>
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/report_credit_card.jsp">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Kartu Kredit" : "Credit Card Report"%>
                    </a>
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/report_compliment.jsp">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Compliment" : "Compliment Report"%>
                    </a>
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/src_reportsale_spesial_request.jsp">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Report Spesial Request" : "Report Spesial Request"%>
                    </a>
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/src_report_void.jsp">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Report Void" : "Report Void"%>
                    </a>
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/src_report_err.jsp">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Report Error" : "Report Error"%>
                    </a>
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/src_report_cancel.jsp">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Report Cancel" : "Report Cancel"%>
                    </a>
                </li>
                <% } %>
                <%--
                <%if (typeOfBusiness.equals("2")) {%>
                    <li>
                        <a href="<%=approot%>/store/sale/report/src_cashier_sales_sum.jsp">
                            Cashier Sales Summary
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/store/sale/report/custome_report.jsp">
                            Custome Report
                        </a>
                    </li>
                <% }%>--%>
            </ul>
        </div>
            
        <div class="col-md-4">
             <ul class="nav nav-stacked">
                  <%if (typeOfBusiness.equals("2")) {%>
                    <li>
                        <a href="<%=approot%>/store/sale/report/src_cashier_sales_sum.jsp">
                            Cashier Sales Summary
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/store/sale/report/custome_report.jsp">
                            Custome Report
                        </a>
                    </li>
                     <li>
                        <a href="<%=approot%>/store/sale/report/src_reportsale_ar_integ.jsp?sale_type=6">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? " AR Integration Report" : "AR Integration Report"%>
                        </a>    
                    </li>
                <% }%>
             </ul>
        </div>     
    </div>
    <!-- /.box-body -->
</div>
<%}else{%>
<div class="col-md-4">
<div class="box box-solid">
    <div class="box-header with-border" style="background-color:  #7468d4">
        <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Penjualan" : "Sales Report"%></h3>
        <div class="box-tools pull-right">
            <button style="color: white" type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
            </button>
        </div>
        <!-- /.box-tools -->
    </div>
    <!-- /.box-header -->
    <div class="box-body">
        <div class="col-md-12">
            <ul class="nav nav-stacked">
                <li>
                    <a href="<%=approot%>/store/sale/report/list_report_sales.jsp">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Penjualan" : "Report Sales"%>
                    </a>
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/sale/list_report_sales_marketing.jsp">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Penjualan Per Marketing" : "Top Sale Per Marketing"%>
                    </a>
                </li>
                <li>
                    <a href="<%=approot%>/store/sale/report/sale/src_omzet_report.jsp">
                        <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Omzet" : "Omzet Report"%>
                    </a>
                </li>
            </ul>
        </div>
    </div>
    <!-- /.box-body -->
</div>
</div>
<%}%>
<!-- /.box -->

