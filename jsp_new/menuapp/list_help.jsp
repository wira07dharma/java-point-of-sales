  <div class="box box-danger">
    <div class="box-header with-border">
      <h3 class="box-title"><i class="fa fa-file"></i> &nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Penjualan" : "Sales"%></h3>
      <div class="box-tools pull-right">
        <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
        </button>
      </div>
      <!-- /.box-tools -->
    </div>
    <!-- /.box-header -->
    <div class="box-body">
      <div class="col-md-6">
        <ul class="menu-list">
          <li>
            <a href="#">
              <i class="fa fa-circle-o"></i>
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Sistem" : "System"%>
            </a>
          </li>
          <li>
            <a href="javascript:openHelp()">
              <i class="fa fa-circle-o"></i>
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Pemakai" : "Users"%>
            </a>
          </li>
        </ul>
      </div>
    </div>
  </div>