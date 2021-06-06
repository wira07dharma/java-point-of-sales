<%-- 
    Document   : list_beacukai
    Created on : Sep 30, 2019, 1:45:21 PM
    Author     : Dimata 007
--%>

<div class="row">
    <div class="col-md-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #FC6E51">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Transit" : "Transit"%></h3>
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
                        <a href="<%=approot%>/management/bea_cukai_customs.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Update Status Barang" : "Update Status Barang"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
	<div class="col-md-4">
		<div class="box box-solid">
			<div class="box-header with-border" style="background-color: #FFCE54">
				<h3 style="color: white" class="box-title"><i class="fa fa-file"></i> &nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan" : "Report"%></h3>
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
						<a href="<%=approot%>/dutyfree/report/in_out_item.jsp">
							<%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Pemasukan dan Pengeluaran Barang" : "Laporan Pemasukan dan Pengeluaran Barang"%>
						</a>
					</li>
					<li>
						<a href="<%=approot%>/dutyfree/report/sale_report.jsp">
							<%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Penjualan Barang" : "Laporan Penjualan Barang"%>
						</a>
					</li>
					<li>
						<a href="<%=approot%>/dutyfree/report/stock_report.jsp">
							<%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Laporan Persediaan Barang" : "Laporan Persediaan Barang"%>
						</a>
					</li>
				</ul>
			</div>    
		</div> 
	</div>
</div>