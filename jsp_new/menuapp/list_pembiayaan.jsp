<div class="row">
    <div class="col-md-6">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #FFCE54">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pembiayaan" : "Costing"%></h3>
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
                        <a href="<%=approot%>/warehouse/material/dispatch/srccosting_material.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah" : "Update/Add"%>
                        </a>
                    </li>                
                </ul>
            </div>
        </div>
    </div>

    <div class="col-md-6">  
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #EC87C0">
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
                        <a href="<%=approot%>/warehouse/material/report/src_reportcosting.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Global" : "Global"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportcostingkategori.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Kategori" : "By Category"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportcostinginvoice.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Nota" : "By Receipt"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>