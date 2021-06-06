<div class="row">
  <div class="col-md-4">
    <div class="box box-solid">
      <div class="box-header with-border" style="background-color: #FFCE54">
        <h3 style="color: white" class="box-title"><i class="fa fa-file"></i> &nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Master Lokasi" : "Location"%></h3>
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
            <a href="<%=approot%>/masterdata/company.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Company" : "Company"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/master/location/country.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Country" : "Country"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/master/location/provinsi.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Provinsi" : "province"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/master/location/kabupaten.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Kota" : "Regency"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/master/location/kecamatan.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Area" : "Area"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/master/location/location.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Lokasi" : "Location"%>
            </a>
          </li>
          <%if (typeOfBusiness.equals("2")) {%>
          <li>
            <a href="<%=approot%>/master/location/roomclass.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Tipe Ruangan" : "Room Class"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/master/location/room.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Ruangan" : "Room List"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/master/location/tableroom.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Meja" : "Table List"%>
            </a>
          </li>
          <% }%>
        </ul>
      </div>    
    </div> 
  </div>
  <div class="col-md-4">
    <div class="box box-solid">
      <div class="box-header with-border" style="background-color: #A0D468">
        <h3 style="color: white" class="box-title"><i class="fa fa-file"></i> &nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Master Kategori" : "Category"%></h3>
        <div class="box-tools pull-right">
          <button style="color: white" type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
          </button>
        </div>
        <!-- /.box-tools -->
      </div>
      <!-- /.box-header -->
      <div class="box-body">
        <ul class="nav nav-stacked">

          <% if (useEtalase == 1) {%>
          <li>
            <a href="<%=approot%>/master/material/matksg.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Master Etalase" : "Etalase List"%>
            </a>
          </li>
          <% }%>
          <li>
            <a href="<%=approot%>/master/material/matcategory.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? kategoriName : kategoriName%>
            </a>
          </li>
          <%
                          int useSubCategory = Integer.parseInt((String) com.dimata.system.entity.PstSystemProperty.getValueIntByName("USE_SUB_CATEGORY"));
                          if (useSubCategory == 1) {%>
          <li>
            <a href="<%=approot%>/master/material/matsubcategory.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Sub Kategori " : "Sub Category"%>
            </a>
          </li>
          <% }%>
          <li>
            <a href="<%=approot%>/master/material/matmerk.jsp">
              <%=merkName%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/master/material/matunit.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Satuan" : "Unit List"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/master/material/color.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Warna" : "Color List"%>
            </a>
          </li>
          <%if (typeOfBusinessDetail == 2) {%>
          <li>
            <a href="<%=approot%>/master/material/kadar.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Kadar" : "Content List"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/master/material/emas_lantakan.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Emas Lantakan" : "Emas Lantakan"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/master/material/nilai_tukar_emas.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Nilai Tukar Emas" : "Nilai Tukar Emas"%>
            </a>
          </li>
          <%}%>
        </ul>
      </div>    
    </div> 
  </div>          
  <div class="col-md-4">      
    <div class="box box-solid">
      <div class="box-header with-border"  style="background-color: #4FC1E9">
        <h3 style="color: white" class="box-title"><i class="fa fa-file"></i> &nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Menu" : "Item"%></h3>
        <div class="box-tools pull-right">
          <button style="color: white" type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
          </button>
        </div>
        <!-- /.box-tools -->
      </div>
      <!-- /.box-header -->
      <div class="box-body">  
        <ul class="nav nav-stacked">
<!--          <li>
            <a href="<%=approot%>/master/material/srcmaterial.jsp?typemenu=0">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Item" : "Items"%>
            </a>
          </li>-->
          <li>
            <a href="<%=approot%>/master/material/material.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Material Item" : "Material Item"%>
            </a>
          </li>
          <% if (!typeOfBusiness.equals("0")) {%> 
          <li>
            <a href="<%=approot%>/master/material/srcmaterial.jsp?typemenu=1">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Menu" : "Set Menu"%>
            </a>
          </li>
          <% }%>
          <li>
            <a href="<%=approot%>/master/material/src_update_harga.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Edit Item" : "Edit Items"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/master/material/src_print_barcode.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Print Barcode" : "Print Barcode"%>
            </a>
          </li>
          <%if (typeOfBusinessDetail == 2) {%>
          <li>
            <a href="<%=approot%>/master/material/mapping_item_store_request.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Mapping Item SR" : "Mapping Item SR"%>                                
            </a>
          </li>
          <% } if(showProduksi.equals("1")){ %>
          <li>
            <a href="<%=approot%>/master/material/src_harga_pokok_item.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Harga Pokok Item" : "Harga Pokok Item"%>                                
            </a>
          </li>
          <li>
            <a href="<%=approot%>/master/material/src_perhitungan_hpp.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Penentuan HPP" : "Penentuan HPP"%>                                
            </a>
          </li>
          <li>
            <a href="<%=approot%>/master/material/list_production_chain.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Template Produksi" : "Production Template"%>                                
            </a>
          </li><%}%>
        </ul>
      </div>
    </div>  
  </div> 
</div>
<div class="row">                    
  <div class="col-md-4">
    <div class="box box-solid">
      <div class="box-header with-border" style="background-color: #AC92EC">
        <h3 style="color: white" class="box-title"><i class="fa fa-file"></i> &nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Kontak" : "Contact"%></h3>
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
            <a href="<%=approot%>/master/contact/srccontactcompany.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Supplier" : "Supplier List"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/masterdata/srcmemberreg.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Member" : "Member List"%>
            </a>
          </li>
          <% if (!typeOfBusiness.equals("0")) {%>  
          <li>
            <a href="<%=approot%>/masterdata/srctravel.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Travel" : "Travel List"%>
            </a>
          </li> 
          <li> 
            <a href="<%=approot%>/masterdata/srcMemberCompany.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Company" : "Company List"%>
            </a>
          </li> 
          <li>
            <a href="<%=approot%>/masterdata/srcguide.jsp">
              <%if (typeOfBusinessDetail == 2) {%>
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Kepemilikan" : "Kepemilikan List"%>
              <%} else {%>
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Guide" : "Guide List"%>
              <%}%>
            </a>
          </li> 
          <% }%>
          <li>
            <a href="<%=approot%>/masterdata/contactclass.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Contact Class" : "Contact Class"%>
            </a>
          </li> 
          <% if (typeOfBusinessDetail == 2) {%>
          <li>
            <a href="<%=approot%>/masterdata/srcguide.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Kepemilikan" : "Kepemilikan List"%>
            </a>
          </li>
          <% }%>
        </ul>
      </div>    
    </div> 
  </div>
  <div class="col-md-4">
    <div class="box box-solid">
      <div class="box-header with-border" style="background-color: #EC87C0">
        <h3 style="color: white" class="box-title"><i class="fa fa-file"></i> &nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Lain-lain" : "Other"%></h3>
        <div class="box-tools pull-right">
          <button style="color: white" type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
          </button>
        </div>
        <!-- /.box-tools -->
      </div>
      <!-- /.box-header -->
      <div class="box-body">
        <ul class="nav nav-stacked">
          <% if (daftarPeriode.equals("1")) {%> 
          <li>
            <a href="<%=approot%>/master/periode/period.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Periode" : "Stock Period"%>
            </a>
          </li>
          <%}%>
          <li>
            <a href="<%=approot%>/masterdata/pricetype.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Tipe Harga" : "Price Type"%>
            </a>
          </li>
          <% if (tipePotongan.equals("1")) {%>
          <li>
            <a href="<%=approot%>/masterdata/discounttype.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Tipe Potongan" : "Discount Type"%>
            </a>
          </li>
          <%} if (tipeCustomer.equals("1")) {%>
          <li>
            <a href="<%=approot%>/masterdata/membergroup.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Tipe Customer" : "Member Type"%>
            </a>
          </li>
          <%} if (pointMember.equals("1")) {%>
          <li>
            <a href="<%=approot%>/masterdata/srcmemberreg_point.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Point Member" : "Member Points"%>
            </a>
          </li>
          <%}%>
          <li>
            <a href="<%=approot%>/masterdata/currencytype.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Jenis Mata Uang" : "Currency List"%>
            </a>
          </li>
          <%if (nilaiTukarHarian.equals("1")) {%>
          <li>
            <a href="<%=approot%>/masterdata/dailyrate_list.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Nilai Tukar Harian" : "Daily Rate"%>
            </a>
          </li>
          <%} if (nilaiTukarStandart.equals("1")) {%>
          <li>
            <a href="<%=approot%>/masterdata/standartrate.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Nilai Tukar Standart" : "Standar Rate"%>
            </a>
          </li>
          <%}%>
          <li>
            <a href="<%=approot%>/master/material/matcosting.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Tipe Biaya" : "Costing Type"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/master/material/mat_type_sales.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Tipe Sales" : "Sales Type"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/master/payment/pay_system.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Sistem Pembayaran" : "Payment System"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/master/cashier/master_kasir.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Master Kasir" : "Cashier Master"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/masterdata/shift.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Shift Kerja" : "Working Shift"%>
            </a>
          </li>
          <%if (typeOfBusinessDetail == 2) {%>
          <li>
            <a href="<%=approot%>/masterdata/rate_pasar_berlian.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Rate Pasar Berlian" : "Diamond Market Rate"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/masterdata/rate_jual_berlian.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Rate Jual" : "Sell Rate"%>
            </a>
          </li>
          <% } %>
          <li>
            <a href="<%=approot%>/masterdata/perhitungan_poin.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Perhitungan Poin Member" : "Member Point Setting"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/masterdata/perhitungan_insentif.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Perhitungan Insentif" : "Incentive Setting"%>
            </a>
          </li>
          <% if (showAksesoris.equals("1")) {%>
          <li>
            <a href="<%=approot%>/masterdata/accessories.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Aksesoris" : "Accessories"%>
            </a>
          </li>
          <%}%>
        </ul>
      </div>    
    </div> 
  </div>
  <div class="col-md-4">
    <div class="box box-solid">
      <div class="box-header with-border" style="background-color: #FC6E51">
        <h3 style="color: white" class="box-title"><i class="fa fa-file"></i> &nbsp; <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Master Group" : "Master Group"%></h3>
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
            <a href="<%=approot%>/masterdata/master_group.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Master Group" : "Master Group"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/masterdata/master_type.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Master Type" : "Master Type"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/masterdata/master_group_mapping.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Master Group Mapping" : "Master Group Mapping"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/masterdata/master_employee.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Master Pegawai" : "Master Employee"%>
            </a>
          </li>
          <li>
            <a href="<%=approot%>/masterdata/master_position.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Master Posisi" : "Master Position"%>
            </a>
          </li>
          <%
          if(useForRaditya.equals("0")){
          %>
          <li>
            <a href="<%=approot%>/masterdata/sales.jsp">
              <%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Sales Person" : "Sales Person List"%>
            </a>
          </li>
          <%
         }
          %>
        </ul>
      </div>    
    </div> 
  </div>
</div>          