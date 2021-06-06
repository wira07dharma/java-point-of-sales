<%-- 
    Document   : company
    Created on : Feb 4, 2020, 3:30:24 PM
    Author     : Regen
--%>

<%@ include file = "../main/javainit.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Dimata - ProChain POS</title>
    <%@include file="../styles/plugin_component.jsp" %>
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
      <h1>Masterdata<small> Company</small> </h1>
      <ol class="breadcrumb">
        <li>Company</li>
        <li>Data</li>
      </ol>
    </section>
    <section class="content">
      <div class="box box-prochain">
        <div class="box-header with-border">
          <label class="box-title pull-left">Input Company</label>
        </div>
        <div class="box-body">
          <form name="frmReceive" id="frmReceive" method="post" action="" class="form-horizontal">               
            <div class="row">
              <div class="col-md-12">
                <div class="col-md-7">
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Kode Perusahaan</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Nama Perusahaan</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Alamat</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Kota</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Provinsi</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Negara</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control">
                    </div>
                  </div>
                </div>
                
                <div class="col-md-5">
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Telepon</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">No Handphone</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Fax</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Email</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 font-small">Postal Code</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control">
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
    </section>
  </body>
</html>
