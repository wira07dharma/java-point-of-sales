
<div class="row">
    <div class="col-md-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #A0D468">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Opname" : "Opname"%></h3>
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
                        <a href="<%=approot%>/warehouse/material/stock/mat_opname_src.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah" : "Update/Add"%>
                        </a>
                    </li>
                    <%if (typeOfBusinessDetail == 2) {%>
                    <li>
                        <a href="<%=approot%>/warehouse/material/stock/mat_opname_src.jsp?emas_lantakan=1">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah Emas Lantakan" : "Update/Add"%>
                        </a>
                    </li>
                    <%}%>
                    <li>
                        <a href="<%=approot%>/barcode/importtextfilesrc.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Opname (Used File)" : "Opname (Used File)"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/stock/mat_stock_correction_src.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Koreksi Stok" : "Correction Stock"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>

    <div class="col-md-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #4FC1E9">
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
                        <a href="<%=approot%>/warehouse/material/report/src_reportstock.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Global" : "Global"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportstockkategori.jsp?tipe=1">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Kategori" : "By Category"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportstocksupplier.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Supplier" : "By Supplier"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_stockcard.jsp?type=1">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Kartu Stok" : "Stock Card"%>
                        </a>
                    </li>
                  <% if (useForRaditya.equals("0")) {%> 
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportposisistock.jsp?type=1">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Posisi Stok" : "Stock Position"%>
                        </a>
                    </li>
                    
                    <%}%>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportposisistock_history.jsp?type=1">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Histori Posisi Stok" : "Stock Position History"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportselisihkoreksistok.jsp?type=1">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Selisih Koreksi Stok" : "List Lost Correction Stock"%>
                        </a>
                    </li>
                  <% if (!showDailyKoreksiStok.equals("0")) {%> 
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportselisihkoreksistok_daily.jsp?type=1&daily=1">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daily Selisih Koreksi Stok" : "List Lost Correction Stock"%>
                        </a>
                    </li>
                    <%}%> 
                    <%if (typeOfBusinessDetail != 0) {%>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_per_etalase.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Etalase" : "Per Etalase"%>
                        </a>
                    </li>
                    <%} if (false) {%>
                    <li> 
                        <a href="<%=approot%>/warehouse/material/report/src_reportstocktracking.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Penelusuran Stok" : "Stock Tracking"%>
                        </a>
                    </li>
                    <% }%>
                    <% if (!showUseForGreenbowl.equals("0")){%>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_stock_store_warehouse.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pencarian Stok" : "Stock Search"%>
                        </a>
                    </li>
                    <%}%>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_stock_position.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Posisi Stok" : "Report Stock Posistion"%>
                        </a>
                    </li>
                </ul> 
            </div>
        </div>
    </div>

    <div class="col-md-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #EC87C0">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Posting" : "Posting"%></h3>
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
                        <a href="<%=approot%>/master/posting/posting_stock_new.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Posting Stok" : "Posting Stock"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/master/posting/srcmaterial_reposting_stock.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Re Posting Stok" : "Re Posting Stock"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>
