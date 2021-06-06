<%@page import="com.dimata.common.entity.system.PstSystemProperty"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%!
  public static int getStrDutyFree(){
	String strDutyFree = PstSystemProperty.getValueByName("ENABLE_DUTY_FREE");
	System.out.println("#Duty Free: " + strDutyFree);
	int dutyFree = Integer.parseInt(strDutyFree);
	return dutyFree;
}
  %>
<div class="row">
    <div class="col-sm-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #FFCE54">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Transfer" : "Dispatch"%></h3>
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
                        <a href="<%=approot%>/warehouse/material/dispatch/srcdf_stock_wh_material.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah / Tambah" : "Update/Change"%>
                        </a>
                    </li>
                    <%if (privApprovalRequestTransfer) {%>  
                    <li>
                        <a href="<%=approot%>/warehouse/material/dispatch/search_purchasefor_transfer.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Dengan Store Request" : "With Store Request"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportstockmin_transfer.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Transfer Minimum Stok" : "Transfer With Minimum Stock"%>
                        </a>
                    </li>
                    <% }%>
                </ul>
            </div>
        </div>
    </div>

    <%if ((typeOfBusiness.equals("0") || typeOfBusiness.equals("2"))) {%>
    <div class="col-sm-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #A0D468">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; 
                    <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Transfer Unit" : "Unit Dispatch"%>
                </h3>
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
                        <a href="<%=approot%>/warehouse/material/dispatch/srcdf_unit_wh_material.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah" : "Update/Add"%>
                        </a>
                    </li>

                </ul>
            </div>
        </div>
    </div>
    <% }%>

<%if(showProduksi.equals("1")){%>
    <%if ((typeOfBusiness.equals("0") || typeOfBusiness.equals("2"))) {%>
    <div class="col-sm-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #FC6E51">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; 
                    <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Produksi" : "Production"%>
                </h3>
                <div class="box-tools pull-right">
                    <button style="color: white" type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                    </button>
                </div>
                <!-- /.box-tools -->
            </div>
            <!-- /.box-header -->
            <div class="box-body">
                <ul class="nav nav-stacked">
                    <% if (typeOfBusinessDetail != 2) {%>
                    <li>
                        <a href="<%=approot%>/warehouse/material/dispatch/src_produksi.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah" : "Update/Add"%>
                        </a>
                    </li>
                    <%} else {%>
                    <% if (privViewProductionEmas) {%>
                    <li>
                        <a href="<%=approot%>/warehouse/material/dispatch/src_produksi_emas.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Produksi Sales Order" : "Sales Order Production"%>
                        </a>
                    </li>
                    <%}%>
                    <%}%>
                </ul>
            </div>
        </div>
    </div>
    <% }}%>  
</div>
<div class="row">

    <%if (typeOfBusinessDetail == 2) {%>
    <div class="col-sm-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #AC92EC">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; 
                    <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Lebur" : "Lebur"%>
                </h3>
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
                        <a href="<%=approot%>/warehouse/material/dispatch/df_unit_material_lebur.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah Emas & Berlian" : "Update/Add"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/dispatch/df_unit_material_lebur.jsp?emas_lantakan=1">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah Emas Lantakan" : "Update/Add"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <% }%> 

    <%if (privApprovalRequestTransfer) {%>
    <div class="col-sm-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #A0D468">
            <% if (getStrDutyFree() == 1) {%>
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Store Transfer" : "Store Transfer"%></h3>
            <%}else{%>
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Request Transfer" : "Request Transfer"%></h3>
            <%}%>
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
                        <a href="<%=approot%>/purchasing/material/pom/search_request_transfer.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah/Tambah Request" : "Update/Add"%>
                        </a>
                    </li>

                </ul>
            </div>
        </div>
    </div>
    <% }%>

    <div class="col-sm-4">
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
                        <a href="<%=approot%>/warehouse/material/report/src_reportdispatch.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Semua" : "All"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportdispatchinvoice.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Invoice" : "By Invoice"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportdispatchsupplier.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Supplier" : "By Supplier"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportdispatchkategori.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Per Kategori" : "By Category"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/warehouse/material/report/src_reportdispatchinvoicesummary.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Summary Per Nota" : "Summary By Invoice"%>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
						
	<% 
		int dutyFree = Integer.parseInt(PstSystemProperty.getValueByName("ENABLE_DUTY_FREE"));
		if(dutyFree == 1){ 
	%>
		<div class="col-sm-4">
			<div class="box box-solid">
				<div class="box-header with-border" style="background-color: #000099">
					<h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Transfer Bea Cukai" : "Customs Dispatch"%></h3>
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
							<a href="<%=approot%>/warehouse/material/dispatch/srcdf_bc_wh_material.jsp">
								<%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Ubah / Tambah" : "Update/Change"%>
							</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
	<%}%>
</div>