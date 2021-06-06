
<div class="row">
    <div class="col-md-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #4FC1E9">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Promosi" : "Promotion"%></h3>
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
                        <a href="<%=approot%>/master/payment/voucher.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Voucher" : "Voucher"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/promotion/newsinfo.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Berita dan Informasi" : "News and Info"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/promotion/marketing-promotion.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Promosi" : "Promotion"%>
                        </a>
                    </li>
                    <li>
                        <a href="<%=approot%>/promotion/email_promotion.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Email Promosi" : "Promotion Email"%>
                        </a>
                    </li>
                </ul>
            </div>
                        
        </div>
    </div>
                        <div class="col-md-4">
        <div class="box box-solid">
            <div class="box-header with-border" style="background-color: #4fe9b0">
                <h3 style="color: white" class="box-title"><i class="fa fa-file"></i>&nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Manajemen Pemasaran" : "Marketing Management"%></h3>
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
                        <a href="<%=approot%>/marketing_management/marketing_price.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "E - Price":"E - Price"%>
                        </a>
                    </li>
                     <li>
                        <a href="<%=approot%>/marketing_management/marketing_katalog.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "E - Katalog":"E - Katalog"%>
                        </a>
                    </li>
                     <li>
                        <a href="<%=approot%>/marketing_management/marketing_brosur.jsp">
                            <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "E - Brosur":"E - Brosur"%>
                        </a>
                    </li>
              
                </ul>
            </div>
                        
        </div>
    </div>
</div>