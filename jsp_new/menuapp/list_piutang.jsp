<div class="box box-solid">
    <div class="box-header with-border" style="background-color: #FFCE54">
        <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Piutang" : "A/R"%></h3>
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
                <a href="<%=approot%>/store/sale/report/src_reportsalepaymentcredit.jsp">
                    <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pelunasan Piutang" : "A/R Payment"%>
                </a>
            </li>
            <li>
                <a href="<%=approot%>/store/sale/report/src_reportar.jsp">
                    <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Rekap Piutang" : "A/R Summary"%>
                </a>
            </li>
        </ul>
    </div>
</div>
