<%@page import="com.dimata.common.entity.system.PstSystemProperty"%>
<div class="row">
    <div class="col-md-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #FFCE54">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Dari Pembelian" : "From PO"%></h3>
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
                        <a href="<%=approot%>/warehouse/material/receive/search_receive_material.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah" : "Update/Add"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
                        
    <div class="col-md-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #FC6E51">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Dari Pengembalian" : "From Return"%></h3>
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
                        <a href="<%=approot%>/warehouse/material/receive/search_receive_return_material.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah" : "Update/Add"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>

    <div class="col-md-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #E9573F">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Dari Toko / Gudang" : "From Store / Warehouse"%></h3>
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
                        <a href="<%=approot%>/warehouse/material/receive/srcreceive_store_wh_material.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah" : "Update/Add"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
                        
    <%if(PstSystemProperty.getValueByName("ENABLE_DUTY_FREE").equals("1")) {%>
    <div class="col-md-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #AC92EC">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Dari Penerimaan Kembali" : "From Receive Back"%></h3>
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
                        <a href="<%=approot%>/warehouse/material/receive/src_receive_back_material.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah" : "Update/Add"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <%}%>
</div>

<div class="row">
    <div class="col-md-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #A0D468">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Dari Pembelian" : "Report From PO"%></h3>
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
                        <a href="<%=approot%>/warehouse/material/report/src_reportreceive_all.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Global" : "Global"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportreceiveinvoice.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Nota" : "By Invoice"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportreceive.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Supplier" : "By Supplier"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportreceivekategori.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Kategori" : "By Category"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportreceiveinvoicesummary.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Summary Per Nota" : "Summary By Invoice"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>    
                        
    <div class="col-md-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #AC92EC">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Dari Pengembalian" : "Report From Return"%></h3>
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
                        <a href="<%=approot%>/warehouse/material/report/src_reportreceive_return.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Global" : "Global"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>    

    <div class="col-md-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #4FC1E9">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Dari Toko / Gudang" : "Report From Store / Warehouse"%></h3>
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
                        <a href="<%=approot%>/warehouse/material/report/src_reportreceiveinternal.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Global" : "Global"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportreceiveinternalinvoice.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Nota" : "By Receipt"%>
                        </a>
                    </li>
                    <li>                    
                        <a href="<%=approot%>/warehouse/material/report/src_reportreceiveinternalkategori.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Kategory" : "By Category"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>    
</div>