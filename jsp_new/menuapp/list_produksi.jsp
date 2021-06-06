<%-- 
    Document   : list_produksi
    Created on : Sep 11, 2019, 1:47:08 PM
    Author     : Dimata 007
--%>
<%@page import="com.dimata.common.entity.system.PstSystemProperty"%>
<div class="row">
<%
    String useProduction1 = PstSystemProperty.getValueByName("USE_PRODUCTION");
    
    if(showProduksi.equals("1")){%>
    <div class="col-md-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #AC92EC">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Produksi" : "Production"%></h3>
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
                        <a href="<%=approot%>/warehouse/material/production/production_src.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah" : "Update/Add"%>
                        </a>
                    </li>                 
                </ul>
            </div>
        </div>
    </div><%} if (useProduction1.equals("1")){%>
    <div class="col-md-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #94ff87">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Penjualan" : "Sale"%></h3>
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
                        <a href="<%=approot%>/warehouse/material/production/src_penjualan.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Penjualan" : "List Sale"%>
                        </a>
                    </li>                
                    <li>
                        <a href="<%=approot%>/warehouse/material/production/laporan_barang.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Barang" : "Item Report"%>
                        </a>
                    </li>                
                    <li>
                        <a href="<%=approot%>/warehouse/material/production/dokumen_pengiriman.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Dokumen Pengiriman" : "Document Delivery"%>
                        </a>
                    </li>                
                </ul>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #93a6ff">
                <h3 style="color: white" class="box-title"><i class="fa fa-truck"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pengiriman" : "Dispatch"%></h3>
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
                        <a href="<%=approot%>/warehouse/material/production/src_pengiriman.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pengiriman" : "Dispatch"%>
                        </a>
                    </li>                
                    <li>
                        <a href="<%=approot%>/warehouse/material/production/status_pengiriman.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Status Pengiriman" : "Dispatch Status"%>
                        </a>
                    </li>               
                </ul>
            </div>
        </div>
    </div>
    <%}%>
</div>







