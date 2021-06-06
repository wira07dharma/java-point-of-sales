<div class="row">
    <% if (privPurchaseRequestWarehouse) {%>
    <div class="col-md-3">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #FFCE54">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Request Order" : "Purchase Request"%></h3>
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
                        <a href="<%=approot%>/purchasing/material/pom/search_purchase_material.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah Request" : "Add/Update Request"%>
                        </a>
                    </li>
<!--                    <li>
                        <a href="<%=approot%>/purchasing/material/pom/srcprmaterial.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah Request" : "Add/Update Request"%>
                        </a>
                    </li>-->
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportstockmin.jsp?typeRequest=1">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Minimum Stok" : "Minimum Stok"%>
                        </a>
                    </li>
                </ul>

            </div>
        </div>
    </div>
    <% }%>

    <div class="col-md-3">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #A0D468">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Order Pembelian" : "Purchase Order"%></h3>
                <div class="box-tools pull-right">
                    <button style="color: white" type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                    </button>
                </div>
                <!-- /.box-tools -->
            </div>
            <div class="box-body">
                <ul class="nav nav-stacked">                
                    <li>
                        <a href="<%=approot%>/purchasing/material/pom/search_po_material.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah Tambah" : "Add/Update"%>
                        </a>
                    </li>
<!--                    <li>
                        <a href="<%=approot%>/purchasing/material/pom/srcpomaterial.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah Tambah" : "Add/Update"%>
                        </a>
                    </li>-->
                    <%if (privPurchaseRequestWarehouse) {%>
                    <li>
                        <a href="<%=approot%>/purchasing/material/pom/srcprforpomaterial.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Dengan Purchase Request" : "With Purchase Request"%>
                        </a>
                    </li>
                    <% }%>
                </ul>
            </div>
        </div>
    </div>

    <div class="col-md-3">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #4FC1E9">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Minimum Stok" : "Minimum Stock"%></h3>
                <div class="box-tools pull-right">
                    <button style="color: white" type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                    </button>
                </div>
                <!-- /.box-tools -->
            </div>
            <div class="box-body">
                <ul class="nav nav-stacked">
                    <%if (typeOfBusiness.equals("2") || typeOfBusiness.equals("0")) {%>                
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportstockmin.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah" : "Update/Add"%>
                        </a>
                    </li>
                    <% }%>
                </ul>
            </div>
        </div>
    </div>

    <div class="col-md-3">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #EC87C0">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Hutang" : "A/P"%></h3>
                <div class="box-tools pull-right">
                    <button style="color: white" type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                    </button>
                </div>
                <!-- /.box-tools -->
            </div>
            <div class="box-body">
                <ul class="nav nav-stacked">                
                    <li>
                        <a href="<%=approot%>/arap/payable/payable_search.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Rekap" : "Summary"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/arap/payable/ap_summary_search.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Rekap Detail" : "Detail Summary"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>