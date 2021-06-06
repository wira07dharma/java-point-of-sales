<%-- 
    Document   : receive_material
    Created on : Jan 28, 2020, 11:43:36 AM
    Author     : Regen
--%>

<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceive"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmMatReceive"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatReceive"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.ij.session.engine.SessPosting"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceiveItem"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstDailyRate"%>
<%@page import="com.dimata.common.entity.contact.PstContactClass"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%
    int iCommand = FRMQueryString.requestCommand(request);
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int startItem = FRMQueryString.requestInt(request, "start_item");
    int cmdItem = FRMQueryString.requestInt(request, "command_item");
    int appCommand = FRMQueryString.requestInt(request, "approval_command");
    long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");
    long oidForwarderInfo = FRMQueryString.requestLong(request, "hidden_forwarder_id");
    int iCommandPosting = FRMQueryString.requestInt(request, "iCommandPosting");
    int cekBeratStatus = FRMQueryString.requestInt(request, "cek_berat");
//
//   if(iCommand == Command.SAVE && oidReceiveMaterial != 0){
//        iCommand = Command.NONE;
//    }
    String syspropDiscount1 = PstSystemProperty.getValueByName("SHOW_DISCOUNT_1");
    String syspropDiscount2 = PstSystemProperty.getValueByName("SHOW_DISCOUNT_2");
    String syspropDiscountNominal = PstSystemProperty.getValueByName("SHOW_DISCOUNT_NOMINAL");
    String syspropOngkosKirim = PstSystemProperty.getValueByName("SHOW_ONGKOS_KIRIM");
    String syspropBonus = PstSystemProperty.getValueByName("SHOW_BONUS");
    String syspropHargaBeli = PstSystemProperty.getValueByName("SHOW_HARGA_BELI");
    String syspropRecTypeDefault = PstSystemProperty.getValueByName("DEFAULT_RECEIVE_TYPE");
    String syspropColor = PstSystemProperty.getValueByName("SHOW_COLOR");
    String syspropEtalase = PstSystemProperty.getValueByName("SHOW_ETALASE");
    String syspropTotalBeli = PstSystemProperty.getValueByName("SHOW_TOTAL_BELI");
    String syspropHargaJual = PstSystemProperty.getValueByName("SHOW_HARGA_JUAL");
    String syspropHSCode = PstSystemProperty.getValueByName("SHOW_HS_CODE");
    String syspropKeterangan = PstSystemProperty.getValueByName("SHOW_KETERANGAN");
    String syspropPenerimaanHpp = PstSystemProperty.getValueByName("SHOW_PENERIMAAN_HPP");
    int includePpn = FRMQueryString.requestInt(request, "include_ppn");
    double defaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));
    long oidCurrencyRec = FRMQueryString.requestLong(request, "hidden_currency_id");
    String receiveDate = FRMQueryString.requestString(request, "FRM_FIELD_RECEIVE_DATE");

    String errMsg = "";
    int iErrCode = FRMMessage.ERR_NONE;

    ControlLine ctrLine = new ControlLine();
    CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
    iErrCode = ctrlMatReceive.action(iCommand, oidReceiveMaterial, userName, userId, request);
    FrmMatReceive frmMatReceive = ctrlMatReceive.getForm();
    MatReceive rec = ctrlMatReceive.getMatReceive();
    errMsg = ctrlMatReceive.getMessage();
//posting
//proses posting stock
    int typeOfBussiness = Integer.parseInt(PstSystemProperty.getValueByName("TYPE_OF_BUSINESS"));


    String priceCode = "Rp.";
    int start = 0; 
    int record = 0; 
    String whereClause = ""; 
    String order = ""; 

    long oidCurrencyx = 0;
    double resultKonversi = 0.0; 
    oidCurrencyRec = rec.getCurrencyId();
    oidReceiveMaterial = rec.getOID();
    int recordToGetItem = 0;
    String whereClauseItem = "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial
            + " AND " + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS] + "=0";

    String orderClauseItem = " RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID];
    if (typeOfBusinessDetail == 2) {
        orderClauseItem = " RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",7) ASC";
    }
    int vectSizeItem = PstMatReceiveItem.getCount(whereClauseItem);

    Vector listMatReceiveItem = new Vector();

    if (rec != null && rec.getOID() != 0) {
        listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(startItem, recordToGetItem, whereClauseItem, orderClauseItem);
    }

    double totalBeratItem = 0;

    String enableInput = "";
    boolean privManageData = true;
    if (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT) {
        privManageData = false;
        enableInput = "readonly";
    }
    
     String input = "";
          if ((rec.getCurrencyId() == 0)) {
          resultKonversi = PstDailyRate.getCurrentDailyRateSales(oidCurrencyx);
        } else {
          input = "disabled";
        }
    Vector <Location> listLocation = PstLocation.list(start, record, whereClause, order);
    String where  = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]
                    + " = " + PstContactClass.CONTACT_TYPE_SUPPLIER
                    + " AND " + PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]
                    + " != " + PstContactList.DELETE;
    Vector <ContactList> listSupplier = PstContactList.listContactByClassType(0, 0, where, PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);

    Vector <CurrencyType> listCurr = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS] + "=" + PstCurrencyType.INCLUDE, "");

%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Dimata - ProChain POS</title>
    <%@include file="../../../styles/plugin_component.jsp" %>
    <style>
      body {
        padding: 1em;
        background-color: #ddd;
        font-family: 'Quicksand', sans-serif;
        /*font-size: 12px;*/
      }
      .select2-container .select2-selection--single {
          box-sizing: border-box;
          cursor: pointer;
          display: block;
          height: 34px;
          user-select: none;
          -webkit-user-select: none;
      }
      .input-group-addon {
          padding: 6px 12px;
          font-size: 14px;
          font-weight: 100;
          line-height: 1;
          color: #555;
          text-align: center;
          background-color: #fff !important;
          border: 1px solid #ccc !important;
          height: 34px;
      }

      .input-group-addon.btn.btn-primary:hover{
          color: #f7942f !important;
        
      }
      label {
        display: inline-block;
        margin-bottom: 5px;
        font-weight: 600;
        margin-top: 8px;
        font-family: sans-serif;
    }
    </style>
  </head>
  <body   style="background-color: #eaf3df;">
    <section class="content-header">
      <h1>Penerimaan<small> Edit</small> </h1>
      <ol class="breadcrumb">
        <li>Penerimaan</li>
        <li>Non PO</li>
      </ol>
    </section>
    <section class="content">
      <div class="box box-prochain">
        <div class="box-header with-border">
          <label class="box-title pull-left">Input Penerimaan</label>
        </div>
        <div class="box-body">
          <form name="frmReceive" id="frmReceive" method="post" action="" class="form-horizontal">               
            <div class="row">
              <div class="col-md-12">
                <div class="col-md-7">
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Kode</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control" placeholder="Automatic Code" >
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Supplier</label>
                    <div class="col-sm-8">
                      <div class="col-sm-10" style="padding: 0px !important;">
                      <select class="form-control select2" id="supplier">
                        <%
                          for (ContactList con : listSupplier) {
                            long oidSupplier = rec.getSupplierId();
                            String cntName = con.getContactCode() + " - " + con.getCompName();
                            if (cntName.length() == 0) {
                              cntName = con.getPersonName() + " " + con.getPersonLastname();
                            }
                        %>
                        <option value="<%=con.getOID()%>" <%=con.getOID() == oidSupplier ? "selected" : ""%>><%=cntName%></option>
                        <%}
                        %>
                      </select>
                        </div>
                        <div class="col-sm-2" style="padding: 0px !important;">
                            <div class="input-group-addon btn btn-primary" id="btnOpenSearchSuplier"><i class="fa fa-plus"></i></div>
                        </div>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Lokasi</label>
                    <div class="col-sm-8">
                      <select class="form-control select2" name="<%=frmMatReceive.fieldNames[frmMatReceive.FRM_FIELD_LOCATION_ID]%>">
                          <%
                            for (Location loc : listLocation) {
                            long oidLocation = rec.getLocationId();
                          %>
                          <option value="<%=loc.getOID()%>" <%=loc.getOID()  == oidLocation ? "selected" : ""%>><%=loc.getName()%></option>
                          <%}
                          %>
                      </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Tanggal</label>
                    <div class="col-sm-8">
                        <input type="text" autocomplete="off" class="form-control datePicker">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Status</label>
                    <div class="col-sm-8">
                      <select class="form-control select2" >
                      <%
                          for (int i = 0; i < MatReceive.receiveStatusKey.length; i++) {
                             String status = ""+rec.getReceiveStatus();
                          %>
                      <option value="<%=MatReceive.receiveStatusValue[i]%>" <%=MatReceive.receiveStatusValue[i]  == status ? "selected" : ""%>><%=MatReceive.receiveStatusKey[i] %></option>
                      <%}
                      %>
                      </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Terms</label>
                    <div class="col-sm-8">
                        <select class="form-control select2" >
                        <%
                            for (int i = 0; i < PstMatReceive.fieldsPaymentType.length; i++) {
                               String sortBy = ""+rec.getTermOfPayment();
                            %>
                        <option value="<%=i%>" <%=PstMatReceive.fieldsPaymentType[i]  == sortBy ? "selected" : ""%>><%=PstMatReceive.fieldsPaymentType[i] %></option>
                        <%}
                        %>
                        </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Waktu</label>
                    <div class="col-sm-8">
                      <div class="bootstrap-timepicker">          
                        <input type="text" class="form-control timepicker">
                    </div>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small"></label>
                    <div class="col-sm-8">
                      <input type="checkbox" class="flat-blue" value="1"> <span class="p"> Include PPN</span>
                    </div>
                  </div>

                </div>
                <div class="col-md-5">
                  
                  <div class="form-group">
                    <label class="col-sm-4 font-small">No Invoice</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control" placeholder="Invoice" >
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Tipe Receive</label>
                    <div class="col-sm-8">
                        <select class="form-control select2" id="tipe">
                            <%
                                for (int i = 0; i < MatReceive.tipePenerimaanKey.length; i++) {
                                   String oidTipe = ""+rec.getReceiveType();
                            %>
                            <option value="<%=MatReceive.tipePenerimaanValue[i] %>" <%=MatReceive.tipePenerimaanValue[i]  == oidTipe ? "selected" : ""%>><%=MatReceive.tipePenerimaanKey[i] %></option>
                            <%}%>
                        </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Days</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control"  value="0" placeholder="Automatic Code" >
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Mata Uang</label>
                    <div class="col-sm-8">
                      <select class="form-control select2" id="currency">
                          <%
                            for (CurrencyType cur : listCurr) {
                            oidCurrencyx = cur.getOID();

                          %>
                          <option value="<%=cur.getOID()%>" <%=cur.getOID()  == rec.getCurrencyId() ? "selected" : ""%>><%=cur.getCode()%></option>
                          <%}
                          %>
                      </select>
                      <input type="hidden">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Rate</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control"  value="0" placeholder="Automatic Code" >
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Keterangan</label>
                    <div class="col-sm-8">
                      <textarea class="form-control" ></textarea>
                    </div>
                  </div>

                </div>
              </div>
            </div>
          </form>
        </div>
        <div class="box-footer">
          <div class="pull-right">
            <button type="submit" form="frmReceive"  class="btn btn-prochain" id="simpan-receive"><i class="fa fa-save"> </i> Simpan</button>
            <button type="button" class="btn btn-danger" id="hapus-receive"><i class="fa fa-trash"> </i> Hapus</button>
            <button type="button" class="btn btn-primary" id="kembali-list"><i class="fa fa-arrow-left"> </i> Kembali</button>
          </div>
        </div>
      </div>

      <div class="tab-group">
        <section id="tab1" title="Data Penerimaan">
          <div class="box box-prochain">
            <div class="box-header with-border">
              <div class="pull-right">
                <button type="button" class="btn btn-primary" id="tambah-tanpa"><i class="fa fa-plus"> </i> </button>
              </div>
            </div>
            <div class="box-body">
              <div class="row">
                <div class="col-md-12">
                  <div id="receiveTableElement">
                    <table id="listReceive" class="table table-striped table-bordered table-responsive" style="font-size: 14px">
                      <thead>
                        <tr>
                          <th>No.</th>                                    
                          <th>SKU</th>
                          <th>Barcode</th>
                          <th>Nama Barang</th>
                          <th>Unit</th>
                          <th>Qty</th>
                          <th class="aksi">Aksi</th>
                        </tr>
                      </thead>
                    </table>
                  </div>
                </div>
              </div>
            </div>
            <div class="box-footer">
              <div class="pull-right">
                  <button class="btn btn-secondary"><strong>Total Qty :</strong> 3</button>
              </div>
            </div>
          </div>
        </section>

        <section id="tab2" title="Rincian Pembayaran">
          <div class="box box-prochain">
            <div class="box-header with-border">
              <div class="pull-right">
                <button type="button" class="btn btn-primary" id="tambah-tanpa"><i class="fa fa-plus"> </i> </button>
              </div>
            </div>
            <div class="box-body">            
              <div class="row">
                <div class="col-md-12"> 
                  <div class="col-md-8">
                      <div id="pembayaranTableElement">
                          <table id="listPembayaran" class="table table-striped table-bordered table-responsive" style="font-size: 14px">
                              <thead>
                                  <tr>
                                      <th>Tanggal</th>                                    
                                      <th>System Pembayaran</th>
                                      <th>Mata Uang</th>
                                      <th>Rate</th>
                                      <th>Jumlah Dalam Mata Uang</th>
                                      <th>Jumlah</th>
                                  </tr>
                              </thead>
                              <tbody>
                                  <tr>
                                      <td class="text-center"></td>
                                      <td class="text-center"></td>
                                      <td class="text-center"></td>
                                      <td class="text-center"></td>
                                      <td class="text-center"></td>
                                      <td class="text-center"></td>
                                  </tr>
                                  <tr>
                                      <td colspan="5" class="text-center">Total Pembayaran</td>
                                      <td class="text-center"></td>
                                  </tr>
                                  <tr>
                                      <td colspan="5" class="text-center">Saldo Piutang</td>
                                      <td class="text-center"></td>
                                  </tr>
                              </tbody>
                          </table>
                      </div>
                  </div>
                  <div class="col-md-4">

                      <div class="col-sm-6">
                          <h6>Sub Total Harga Beli</h6>
                          <h6>Include PPN</h6>
                          <h6>Sub Total</h6>
                          <h6>Sub Total Ongkos Kirim</h6>
                          <h6>Grand Total</h6>
                      </div>
                      <div class="col-sm-1">
                          <h6>:</h6>
                          <h6>:</h6>
                          <h6>:</h6>
                          <h6>:</h6>
                          <h6>:</h6>
                      </div>
                      <div class="col-sm-4">
                          <h6></h6>
                          <h6></h6>
                          <h6></h6>
                          <h6></h6>
                          <h6></h6>

                      </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="box-footer">
            </div>
          </div>
        </section>

        <section id="tab3" title="Info Forwarder">
          <div class="box box-prochain">
            <div class="box-header with-border">
              <div class="pull-right">
                <button type="button" class="btn btn-primary" id="tambah-tanpa"><i class="fa fa-plus"> </i> </button>
              </div>
            </div>
            <div class="box-body">         
              <div class="row">
                <div class="col-md-12">
                  <div id="forwardTableElement">
                    <table id="listForwardInfo" class="table table-striped table-bordered table-responsive" style="font-size: 14px">
                      <thead>
                        <tr>
                          <th>No.</th>                                    
                          <th>Nama Perusahaan</th>
                          <th>Tanggal</th>
                          <th>Total Biaya</th>
                          <th>Keterangan</th>
                        </tr>
                      </thead>

                    </table>
                  </div>
                </div>
              </div>
            </div>
            <div class="box-footer">
            </div>
          </div>
        </section>
      </div>
    </section>
    <script>
      ;
      (function (defaults, $, window, document, undefined) {

        'use strict';

        $.extend({
          tabifySetup: function (options) {
            return $.extend(defaults, options);
          }
        }).fn.extend({
          tabify: function (options) {
            options = $.extend({}, defaults, options);
            return $(this).each(function () {
              var $element, tabHTML, $tabs, $sections;
              $element = $(this);
              $sections = $element.children();
              // Build tabHTML
              tabHTML = '<ul class="tab-nav">';
              $sections.each(function () {
                if ($(this).attr("title") && $(this).attr("id")) {
                  tabHTML += '<li><a href="#' + $(this).attr("id") + '">' + $(this).attr("title") + '</a></li>';
                }
              });
              tabHTML += '</ul>';
              // Prepend navigation
              $element.prepend(tabHTML);
              // Load tabs
              $tabs = $element.find('.tab-nav li');
              // Functions
              var activateTab = function (id) {
                $tabs.filter('.active').removeClass('active');
                $sections.filter('.active').removeClass('active');
                $tabs.has('a[href="' + id + '"]').addClass('active');
                $sections.filter(id).addClass('active');
              }
              // Setup events
              $tabs.on('click', function (e) {
                activateTab($(this).find('a').attr('href'));
                e.preventDefault();
              });
              // Activate first tab
              activateTab($tabs.first().find('a').attr('href'));
            });
          }
        });
      })({
        property: "value",
        otherProperty: "value"
      }, jQuery, window, document);

      // Calling the plugin
      $('.tab-group').tabify();
      
      $(document).ready(function () {
        
        $('input[type="checkbox"].flat-blue').iCheck({
          checkboxClass: 'icheckbox_square-blue'
        });

        $('.select2').select2({placeholder: "Semua"});
        $('.selectAll').select2({placeholder: "Semua"});

        $('.datePicker').datetimepicker({
            format: "yyyy-mm-dd",
            todayBtn: true,
            autoclose: true,
            minView: 2
        });
        
        //Timepicker
        $('.timepicker').timepicker({
          showInputs : false,
           showMeridian: false,
           format: 'hh:mm'
        });

      });
    </script>
  </body>
</html>
