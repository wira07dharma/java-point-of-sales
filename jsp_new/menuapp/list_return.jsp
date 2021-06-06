<div class="row">
    <div class="col-md-6">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #EC87C0">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Return Dari Pembelian" : "Return"%></h3>
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
                        <a href="<%=approot%>/warehouse/material/return/src_return_material.jsp">                        
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah" : "Update/Add"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #AC92EC">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan" : "Report"%></h3>
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
                        <a href="<%=approot%>/warehouse/material/report/src_reportreturninvoice.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Invoice" : "By Invoice"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportreturn.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Supplier" : "By Supplier"%>
                        </a>
                    </li>
                    <%if (typeOfBusiness.equals("3")) {%>
                    <li>
                        <a href="#">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Return Dari Penjualan" : "Return From Sale"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/conreturn/src_return_from_sale.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah / Tambah" : "Ubah / Tambah"%>
                        </a>
                    </li>
                    <% }%>
                </ul>
            </div>
        </div>
    </div>
</div>