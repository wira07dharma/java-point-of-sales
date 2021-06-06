<script language=JavaScript>
/**
 * This function used to make fulldown menu only
 */
function fwLoadMenus() {
  if(window.fw_menu_0) return;

	  /** START MENU PENJUALAN / SALES */
		  window.fw_menu_1_1 = new Menu("01. Laporan",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  fw_menu_1_1.addMenuItem("01. Penjualan Global", "location='<%=approot%>/store/sale/report/src_reportsale_global.jsp'");
		  fw_menu_1_1.addMenuItem("02. Penjualan Detail", "location='<%=approot%>/store/sale/report/src_reportsale_detail_global.jsp'");
		  fw_menu_1_1.addMenuItem("03. Gross Margin", "location='<%=approot%>/store/sale/report/src_reportsale_global_margin.jsp'");
		  fw_menu_1_1.addMenuItem("04. Rekap Harian", "location='<%=approot%>/store/sale/report/src_reportsalerekaptanggal.jsp'");
		  fw_menu_1_1.addMenuItem("05. Pending Order (DP)", "location='<%=approot%>/store/sale/report/src_reportpendingorder.jsp'");
		  fw_menu_1_1.addMenuItem("06. Per Invoice", "location='<%=approot%>/store/sale/report/src_reportsale.jsp'");
		  fw_menu_1_1.addMenuItem("07. Invoice Detail", "location='<%=approot%>/store/sale/report/src_reportsale_detail.jsp'");
		  fw_menu_1_1.addMenuItem("08. Retur Per Invoice", "location='<%=approot%>/store/sale/report/src_reportreturn.jsp'");

		  fw_menu_1_1.addMenuItem("09. Rekapitulasi Report", "location='<%=approot%>/store/sale/report/src_reportrekapkategori.jsp'");
		  fw_menu_1_1.addMenuItem("10. Rekap Per KSG", "location='<%=approot%>/store/sale/report/src_reportrekap.jsp'");
                  fw_menu_1_1.addMenuItem("11. Per Cash Opening ", "location='<%=approot%>/store/sale/report/src_report_cash_opening.jsp'");
		  //fw_menu_1_1.addMenuItem("07. Penjualan Kredit", "location='<%=approot%>/store/sale/report/src_reportsalecredit.jsp'");
		  fw_menu_1_1.hideOnMouseOut=true;

		  window.fw_menu_1_2 = new Menu("02. Piutang",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  fw_menu_1_2.addMenuItem("01. Pelunasan Piutang", "location='<%=approot%>/store/sale/report/src_reportsalepaymentcredit.jsp'");
		  fw_menu_1_2.addMenuItem("02. Rekap Piutang", "location='<%=approot%>/store/sale/report/src_reportar.jsp'");
		  fw_menu_1_2.hideOnMouseOut=true;

	  window.fw_menu_1 = new Menu("root",170,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  //fw_menu_1.addMenuItem("01. Internal Invoice","location='<%=approot%>/store/sale/src_reportsale.jsp'");
	  //fw_menu_1.addMenuItem("02. Pembatalan Penjualan","location='<%=approot%>/store/sale/cancel/src_invoice.jsp'");
	  fw_menu_1.addMenuItem(fw_menu_1_1);
	  fw_menu_1.addMenuItem(fw_menu_1_2);
	  fw_menu_1.childMenuIcon = "<%=approot%>/images/arrows.gif";
	  fw_menu_1.hideOnMouseOut = true;
	  /** END MENU SALES / PENJUALAN */


      /** START MENU PURCHASING / PEMBELIAN */
	  	  window.fw_menu_2_1 = new Menu("03. Hutang",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  fw_menu_2_1.addMenuItem("01. Rekap", "location='<%=approot%>/arap/payable/payable_search.jsp'");
		  fw_menu_2_1.addMenuItem("02. Rekap Detail", "location='<%=approot%>/arap/payable/ap_summary_search.jsp'");
		  //fw_menu_2_1.addMenuItem("03. Umur Hutang", "location='<%=approot%>/arap/payable/#'");
		  fw_menu_2_1.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_2_1.hideOnMouseOut=true;

	  window.fw_menu_2 = new Menu("root",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  fw_menu_2.addMenuItem("01. Order Pembelian", "location='<%=approot%>/purchasing/material/pom/srcpomaterial.jsp'");
          
          <%if(typeOfBusiness.equals("2") || typeOfBusiness.equals("0")){%>
	  fw_menu_2.addMenuItem("02. Minimum Stok", "location='<%=approot%>/warehouse/material/report/src_reportstockmin.jsp'");
          <%}%>

	  fw_menu_2.addMenuItem(fw_menu_2_1);
	  fw_menu_2.childMenuIcon="<%=approot%>/images/arrows.gif";
	  fw_menu_2.hideOnMouseOut=true;
	  /** END MENU PURCHASING / PEMBELIAN */


      /** START MENU RECEIVING / PENERIMAAN */
	  			window.fw_menu_3_1_0 = new Menu("03. Laporan",110,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
				fw_menu_3_1_0.addMenuItem("01. Global", "location='<%=approot%>/warehouse/material/report/src_reportreceive_all.jsp'");
				fw_menu_3_1_0.addMenuItem("02. Per Nota", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinvoice.jsp'");
				fw_menu_3_1_0.addMenuItem("03. Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportreceive.jsp'");
				fw_menu_3_1_0.addMenuItem("04. Per Kategori", "location='<%=approot%>/warehouse/material/report/src_reportreceivekategori.jsp'");
				fw_menu_3_1_0.hideOnMouseOut=true;

			window.fw_menu_3_1 = new Menu("01. Dari Pembelian",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			fw_menu_3_1.addMenuItem("01. Ubah/Tambah", "location='<%=approot%>/warehouse/material/receive/src_receive_material.jsp'");
                        <%if(typeOfBusiness.equals("1")){%>
                            fw_menu_3_1.addMenuItem("02. Import(from file)", "location='<%=approot%>/barcode/importtextfilereceivesrc.jsp'");
                        <%}%>
                        fw_menu_3_1.addMenuItem(fw_menu_3_1_0);
		  	fw_menu_3_1.childMenuIcon="<%=approot%>/images/arrows.gif";
			fw_menu_3_1.hideOnMouseOut=true;

				window.fw_menu_3_2_0 = new Menu("02. Laporan",110,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
				fw_menu_3_2_0.addMenuItem("01. Global", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinternal.jsp'");
				fw_menu_3_2_0.addMenuItem("02. Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinternalinvoice.jsp'");
				fw_menu_3_2_0.addMenuItem("03. Per Kategori", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinternalkategori.jsp'");
				fw_menu_3_2_0.childMenuIcon="<%=approot%>/images/arrows.gif";
				fw_menu_3_2_0.hideOnMouseOut=true;

			window.fw_menu_3_2 = new Menu("02. Dari Toko/Gudang",130,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			fw_menu_3_2.addMenuItem("01. Ubah/Tambah", "location='<%=approot%>/warehouse/material/receive/srcreceive_store_wh_material.jsp'");
			fw_menu_3_2.addMenuItem(fw_menu_3_2_0);
			fw_menu_3_2.childMenuIcon="<%=approot%>/images/arrows.gif";
			fw_menu_3_2.hideOnMouseOut=true;

	  window.fw_menu_3 = new Menu("root",170,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  fw_menu_3.addMenuItem(fw_menu_3_1);
	  fw_menu_3.addMenuItem(fw_menu_3_2);
	  fw_menu_3.childMenuIcon="<%=approot%>/images/arrows.gif";
	  fw_menu_3.hideOnMouseOut=true;
	  /** END MENU RECEIVING / PENERIMAAN */


      /** START MENU RETURN / RETUR */
	  		//sub menu report return
			window.fw_menu_4_1 = new Menu("02. Laporan",145,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			fw_menu_4_1.addMenuItem("01. Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreturninvoice.jsp'");
			fw_menu_4_1.addMenuItem("02. Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportreturn.jsp'");
			//fw_menu_4_1.addMenuItem("03. Rekap Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreturninvoicerekap.jsp'");
			fw_menu_4_1.hideOnMouseOut=true;

	  window.fw_menu_4 = new Menu("root",170,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  fw_menu_4.addMenuItem("01. Ubah/Tambah", "location='<%=approot%>/warehouse/material/return/src_return_material.jsp'");
	  fw_menu_4.addMenuItem(fw_menu_4_1);
	  fw_menu_4.childMenuIcon="<%=approot%>/images/arrows.gif";
	  fw_menu_4.hideOnMouseOut=true;
	  /** END MENU RETURN/PENGEMBALIAN */


	  /** START MENU TRANSFER BARANG */
                        window.fw_menu_5_0 = new Menu("03. Laporan",140,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
                        fw_menu_5_0.addMenuItem("01. Semua", "location='<%=approot%>/warehouse/material/report/src_reportdispatch.jsp'");
                        fw_menu_5_0.addMenuItem("02. Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportdispatchinvoice.jsp'");
                        fw_menu_5_0.addMenuItem("03. Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportdispatchsupplier.jsp'");
                        fw_menu_5_0.addMenuItem("04. Per Kategori", "location='<%=approot%>/warehouse/material/report/src_reportdispatchkategori.jsp'");
                        fw_menu_5_0.childMenuIcon="<%=approot%>/images/arrows.gif";
                        fw_menu_5_0.hideOnMouseOut=true;

                        window.fw_menu_5 = new Menu("root",170,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
                        fw_menu_5.addMenuItem("01. Ubah/Tambah", "location='<%=approot%>/warehouse/material/dispatch/srcdf_stock_wh_material.jsp'");
                        <%if(typeOfBusiness.equals("1")){%>
                            fw_menu_5.addMenuItem("02. Import(from file)", "location='<%=approot%>/barcode/importtextfiletransfersrc.jsp'");
                        <%}%>
                        // fw_menu_5.addMenuItem("02. Dari Barcode Scanner (File)", "location='<%=approot%>/warehouse/material/dispatch/srcdf_stock_wh_material.jsp'");
                        fw_menu_5.addMenuItem(fw_menu_5_0);
                        <%if(typeOfBusiness.equals("0") || typeOfBusiness.equals("2")){%>
                            fw_menu_5.addMenuItem("04. Transfer Unit", "location='<%=approot%>/warehouse/material/dispatch/srcdf_unit_wh_material.jsp'");
                           
                        <%}%>
                         fw_menu_5.childMenuIcon="<%=approot%>/images/arrows.gif";
                        fw_menu_5.hideOnMouseOut=true;
     /** END MENU TRANSFER BARANG */


     /** START MENU Pengiriman BARANG */
                        window.fw_menu_10_0 = new Menu("03. Laporan",140,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
                        fw_menu_10_0.addMenuItem("01. Semua", "location='<%=approot%>/warehouse/material/report/src_reportdispatch.jsp'");
                        fw_menu_10_0.addMenuItem("02. Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportdispatchinvoice.jsp'");
                        fw_menu_10_0.addMenuItem("03. Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportdispatchsupplier.jsp'");
                        fw_menu_10_0.addMenuItem("04. Per Kategori", "location='<%=approot%>/warehouse/material/report/src_reportdispatchkategori.jsp'");
                        fw_menu_10_0.childMenuIcon="<%=approot%>/images/arrows.gif";
                        fw_menu_10_0.hideOnMouseOut=true;

                        window.fw_menu_10 = new Menu("root",170,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
                        fw_menu_10.addMenuItem("01. Ubah/Tambah", "location='<%=approot%>/warehouse/material/dispatch/src_delivery_order_sales.jsp'");
                        //fw_menu_10.addMenuItem(fw_menu_10_0); jangan di buat dlu
                        fw_menu_10.childMenuIcon="<%=approot%>/images/arrows.gif";
                        fw_menu_10.hideOnMouseOut=true;
     /** END MENU TRANSFER BARANG */


	 /** START MENU COSTING */
			window.fw_menu_6_0 = new Menu("02. Laporan",140,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			fw_menu_6_0.addMenuItem("01. Global", "location='<%=approot%>/warehouse/material/report/src_reportcosting.jsp'");
			fw_menu_6_0.addMenuItem("02. Per Kategori", "location='<%=approot%>/warehouse/material/report/src_reportcostingkategori.jsp'");
                        fw_menu_6_0.addMenuItem("03. Per Nota", "location='<%=approot%>/warehouse/material/report/src_reportcostinginvoice.jsp'");
			//fw_menu_6_0.addMenuItem("03. Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportdispatchkategori.jsp'");
			//fw_menu_6_0.addMenuItem("04. Per Kategori", "location='<%=approot%>/warehouse/material/report/src_reportdispatchsupplier.jsp'");
			fw_menu_6_0.childMenuIcon="<%=approot%>/images/arrows.gif";
			fw_menu_6_0.hideOnMouseOut=true;

	 window.fw_menu_6 = new Menu("root",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	 fw_menu_6.addMenuItem("01. Ubah/Tambah", "location='<%=approot%>/warehouse/material/dispatch/srccosting_material.jsp'");
	 fw_menu_6.addMenuItem(fw_menu_6_0);
	 fw_menu_6.childMenuIcon="<%=approot%>/images/arrows.gif";
	 fw_menu_6.hideOnMouseOut=true;
	 /** END MENU COSTING */


	 /** START MENU MANAJEMEN STOK */
			window.fw_menu_7_0 = new Menu("04. Laporan",175,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
                        fw_menu_7_0.addMenuItem("01. Global", "location='<%=approot%>/warehouse/material/report/src_reportstock.jsp'");
                        fw_menu_7_0.addMenuItem("02. Per Kategori", "location='<%=approot%>/warehouse/material/report/src_reportstockkategori.jsp?tipe=1'");
                        fw_menu_7_0.addMenuItem("03. Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportstocksupplier.jsp'");
                        fw_menu_7_0.addMenuItem("04. Kartu Stok", "location='<%=approot%>/warehouse/material/report/src_stockcard.jsp?type=1'");
                        fw_menu_7_0.addMenuItem("05. Posisi Stok", "location='<%=approot%>/warehouse/material/report/src_reportposisistock.jsp?type=1'");
                        fw_menu_7_0.addMenuItem("06. Selisih Koreksi Stok", "location='<%=approot%>/warehouse/material/report/src_reportselisihkoreksistok.jsp?type=1'");
                        //fw_menu_7_0.addMenuItem("06. Posisi Stok Per Kategori", "location='<%=approot%>/warehouse/material/report/src_reportposisistockkategori.jsp?type=1'");
                        //fw_menu_7_0.addMenuItem("07. Posisi Stok Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportposisistocksupplier.jsp?type=1'");
                        fw_menu_7_0.hideOnMouseOut=true;

	 window.fw_menu_7 = new Menu("root",175,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	 fw_menu_7.addMenuItem("01. Opname", "location='<%=approot%>/warehouse/material/stock/mat_opname_src.jsp'");
         fw_menu_7.addMenuItem("02. Opname(used file)", "location='<%=approot%>/barcode/importtextfilesrc.jsp'");
	 //fw_menu_7.addMenuItem("03. Opname Cepat", "location='<%=approot%>/warehouse/material/stock/mat_opname_store_quick_src.jsp'");
	 fw_menu_7.addMenuItem("03. Koreksi Stok", "location='<%=approot%>/warehouse/material/stock/mat_stock_correction_src.jsp'");
	 fw_menu_7.addMenuItem(fw_menu_7_0);
	 //fw_menu_7.addMenuItem("05. Posting Stok", "location='<%=approot%>/master/posting/posting_stock.jsp'");
         fw_menu_7.addMenuItem("05. Posting Stok", "location='<%=approot%>/master/posting/posting_stock_new.jsp'");
         fw_menu_7.addMenuItem("06. RePosting Stok ", "location='<%=approot%>/master/posting/srcmaterial_reposting_stock.jsp'");


	 fw_menu_7.childMenuIcon="<%=approot%>/images/arrows.gif";
	 fw_menu_7.hideOnMouseOut=true;
	 /** END MENU MANAJEMEN STOK */


	  /** START MENU MASTER DATA */
          // sub menu master data
         
              window.fw_menu_8_1 = new Menu("21. Import Data",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
              fw_menu_8_1.addMenuItem("01. Barang", "location='<%=approot%>/excel/upload/importcatalogsrc.jsp'");
              fw_menu_8_1.addMenuItem("02. Supplier", "location='<%=approot%>/excel/upload/importsuppliersrc.jsp'");
              //fw_menu_3_1.addMenuItem("03. Customer", "location='<%=approot%>/excel/upload/importcustomersrc.jsp'");
              fw_menu_8_1.addMenuItem("03. Member", "location='<%=approot%>/excel/upload/importmembersrc.jsp'");

              fw_menu_8_1.childMenuIcon="<%=approot%>/images/arrows.gif";
              fw_menu_8_1.hideOnMouseOut=true;
         
	  window.fw_menu_8 = new Menu("root",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  fw_menu_8.addMenuItem("01. Daftar Barang", "location='<%=approot%>/master/material/srcmaterial.jsp'");
	  //fw_menu_8.addMenuItem("02. Simple Catalog", "location='<%=approot%>/master/material/simple_main.jsp'");
	  fw_menu_8.addMenuItem("03. Daftar Supplier", "location='<%=approot%>/master/contact/srccontactcompany.jsp'");
	  fw_menu_8.addMenuItem("04. Daftar Lokasi", "location='<%=approot%>/master/location/location.jsp'");
	  fw_menu_8.addMenuItem("", "");
	  fw_menu_8.addMenuItem("05. Tipe Customer", "location='<%=approot%>/masterdata/membergroup.jsp'");
	  fw_menu_8.addMenuItem("06. Daftar Member", "location='<%=approot%>/masterdata/srcmemberreg.jsp'");
	  fw_menu_8.addMenuItem("07. Poin Member", "location='<%=approot%>/masterdata/srcmemberreg_point.jsp'");
	  fw_menu_8.addMenuItem("", "");
	  fw_menu_8.addMenuItem("08. Daftar Periode", "location='<%=approot%>/master/periode/period.jsp'");
	  fw_menu_8.addMenuItem("09. Kategori Barang", "location='<%=approot%>/master/material/matcategory.jsp'");
	  fw_menu_8.addMenuItem("10. <%=merkName%>", "location='<%=approot%>/master/material/matmerk.jsp'");
	  fw_menu_8.addMenuItem("11. Daftar Satuan", "location='<%=approot%>/master/material/matunit.jsp'");
	  fw_menu_8.addMenuItem("", "");
	  fw_menu_8.addMenuItem("12. Master Kasir", "location='<%=approot%>/master/cashier/master_kasir.jsp'");
	  fw_menu_8.addMenuItem("13. Shift Kerja", "location='<%=approot%>/masterdata/shift.jsp'");
	  fw_menu_8.addMenuItem("14. Daftar Sales", "location='<%=approot%>/masterdata/sales.jsp'");
	  fw_menu_8.addMenuItem("", "");
	  fw_menu_8.addMenuItem("15. Jenis Mata Uang", "location='<%=approot%>/masterdata/currencytype.jsp'");
	  fw_menu_8.addMenuItem("16. Nilai Tukar Harian", "location='<%=approot%>/masterdata/dailyrate_list.jsp'");
	  fw_menu_8.addMenuItem("17. Nilai Tukar Standar", "location='<%=approot%>/masterdata/standartrate.jsp'");
	  fw_menu_8.addMenuItem("18. Tipe Harga", "location='<%=approot%>/masterdata/pricetype.jsp'");
	  fw_menu_8.addMenuItem("19. Tipe Potongan", "location='<%=approot%>/masterdata/discounttype.jsp'");
          <%if(typeOfBusiness.equals("2")){%>
            fw_menu_8.addMenuItem("20. Kode Grup", "location='<%=approot%>/master/material/coderange.jsp'");
	  <%}%>
          fw_menu_8.addMenuItem("21. Sistem Pembayaran", "location='<%=approot%>/master/payment/pay_system.jsp'");
          fw_menu_8.addMenuItem("22. Tipe Biaya", "location='<%=approot%>/master/material/matcosting.jsp'");
          //adding menu restoran by mirahu 20120517
          //update opie-eyek 09012013 untuk
          //0= retail, 2 = restaurant, 3 distributor
          <%if(typeOfBusiness.equals("2")){%>
             fw_menu_8.addMenuItem("23. Daftar Ruangan", "location='<%=approot%>/master/location/room.jsp'");
             fw_menu_8.addMenuItem("24. Daftar Meja", "location='<%=approot%>/master/location/tableroom.jsp'");
          <%}%>

          <%if(typeOfBusiness.equals("1")){%>
             fw_menu_8.addMenuItem(fw_menu_8_1);
          <%}%>
	  fw_menu_8.childMenuIcon="<%=approot%>/images/arrows.gif";
	  fw_menu_8.hideOnMouseOut=true;
	  /** END MENU MASTER DATA */

	  /** START MENU SYSTEM */
		  window.fw_menu_4_1 = new Menu("04. Pertukaran Data",120,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  	  fw_menu_4_1.addMenuItem("01. Expor Data", "location='<%=approot%>/transferdata/transferalltoclient.jsp'");
		  fw_menu_4_1.addMenuItem("02. Impor Data", "location='<%=approot%>/transferdata/restoreallinclient.jsp'");
		  fw_menu_4_1.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_4_1.hideOnMouseOut=true;

	  window.fw_menu_9 = new Menu("root",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  fw_menu_9.addMenuItem("01. Pemakai", "location='<%=approot%>/system/userlist.jsp'");
	  fw_menu_9.addMenuItem("02. Grup", "location='<%=approot%>/system/grouplist.jsp'");
	  fw_menu_9.addMenuItem("03. Hak Akses", "location='<%=approot%>/system/privilegelist.jsp'");
	  //fw_menu_9.addMenuItem("04. Backup Data", "location='<%=approot%>/service/service_center.jsp'");
	  <%if(typeOfBusiness.equals("1")){%>
            fw_menu_9.addMenuItem(fw_menu_4_1);
          <%}%>

          fw_menu_9.addMenuItem("05. Pengaturan Aplikasi", "location='<%=approot%>/system/sysprop.jsp'");
	  fw_menu_9.addMenuItem("06. Tutup Periode", "location='<%=approot%>/master/closing/closing_monthly.jsp'");
	  fw_menu_9.addMenuItem("07. Internet connection", "location='<%=approot%>/master/DBconnection/connection.jsp'");
          fw_menu_9.addMenuItem("08. Transfer Ke Outlet", "location='<%=approot%>/master/data_sync/transfer_data.jsp'");
          fw_menu_9.addMenuItem("09. Transfer Penjualan", "location='<%=approot%>/master/data_sync/transfer_data_to_server.jsp'");
	  fw_menu_9.childMenuIcon="<%=approot%>/images/arrows.gif";

          fw_menu_9.hideOnMouseOut=true;
	  /** END MENU SYSTEM */
	  fw_menu_9.writeMenus();
}

function MM_jumpMenu(targ,selObj,restore){
  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
  if (restore) selObj.selectedIndex=0;
}
</script>


<script language="JavaScript">
function cordYMenu0(cordX){
	posY = Math.abs(document.all.TOPTITLE.height) + Math.abs(document.all.MAINMENU.height) + 20;
	window.FW_showMenu(window.fw_menu_1,cordX,posY);
}

function cordYMenu1(cordX){
	posY = Math.abs(document.all.TOPTITLE.height) + Math.abs(document.all.MAINMENU.height) + 20;
	window.FW_showMenu(window.fw_menu_2,cordX,posY);
}

function cordYMenu2(cordX){
	posY = Math.abs(document.all.TOPTITLE.height) + Math.abs(document.all.MAINMENU.height) + 20;
	window.FW_showMenu(window.fw_menu_3,cordX,posY);
}

function cordYMenu3(cordX){
	posY = Math.abs(document.all.TOPTITLE.height) + Math.abs(document.all.MAINMENU.height) + 20;
	window.FW_showMenu(window.fw_menu_4,cordX,posY);
}

function cordYMenu4(cordX){
	posY = Math.abs(document.all.TOPTITLE.height) + Math.abs(document.all.MAINMENU.height) + 20;
	window.FW_showMenu(window.fw_menu_5,cordX,posY);
}

function cordYMenu10(cordX){
	posY = Math.abs(document.all.TOPTITLE.height) + Math.abs(document.all.MAINMENU.height) + 20;
	window.FW_showMenu(window.fw_menu_10,cordX,posY);
}

function cordYMenu5(cordX){
	posY = Math.abs(document.all.TOPTITLE.height) + Math.abs(document.all.MAINMENU.height) + 20;
	window.FW_showMenu(window.fw_menu_6,cordX,posY);
}


function cordYMenu6(cordX){
	posY = Math.abs(document.all.TOPTITLE.height) + Math.abs(document.all.MAINMENU.height) + 20;
	window.FW_showMenu(window.fw_menu_7,cordX,posY);
}

function cordYMenu7(cordX){
	posY = Math.abs(document.all.TOPTITLE.height) + Math.abs(document.all.MAINMENU.height) + 20;
	window.FW_showMenu(window.fw_menu_8,cordX,posY);
}

function cordYMenu8(cordX){
	posY = Math.abs(document.all.TOPTITLE.height) + Math.abs(document.all.MAINMENU.height) + 20;
	window.FW_showMenu(window.fw_menu_9,cordX,posY);
}

</script>

<script language=JavaScript src="<%=approot%>/main/fw_menu.js"></script>
<script language=JavaScript1.2>fwLoadMenus();</script>

<link rel="stylesheet" href="../styles/tab.css" type="text/css">
<input type="hidden" id="M0">
<input type="hidden" id="M1">
<input type="hidden" id="M2">
<input type="hidden" id="M3">
<input type="hidden" id="M4">
<input type="hidden" id="M10">
<input type="hidden" id="M5">
<input type="hidden" id="M6">
<input type="hidden" id="M7">
<input type="hidden" id="M8">

<script>
function setScr() {
	document.all.M0.value = document.all.divMenu0.offsetLeft + 0;
	document.all.M1.value = document.all.divMenu1.offsetLeft + 0;
	document.all.M2.value = document.all.divMenu2.offsetLeft + 0;
	document.all.M3.value = document.all.divMenu3.offsetLeft + 0;
	document.all.M4.value = document.all.divMenu4.offsetLeft + 0;
        document.all.M10.value = document.all.divMenu10.offsetLeft + 0;
	document.all.M5.value = document.all.divMenu5.offsetLeft + 0;
	document.all.M6.value = document.all.divMenu6.offsetLeft + 0;
	document.all.M7.value = document.all.divMenu7.offsetLeft + 0;
	document.all.M8.value = document.all.divMenu8.offsetLeft + 0;
}
window.onload = setScr;
window.onresize = setScr;
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#E1CA9F">
  <tr>
    <td height="1" nowrap bgcolor="#A73701"><img src="home_files/spacer.gif" width=1 height=1></td>
  </tr>
  <tr>
    <td nowrap>
      <%//update opie-eyek 20130805
      if(MODUS_TRANSFER == MODUS_TRANSFER_SERVER){%>
            <div align="center">
              <A id="divMenu0" href="#" onclick="javascript:cordYMenu0(document.all.M0.value);" onmouseout=FW_startTimeout(); class="menus">Penjualan</A>
            | <A id="divMenu1" href="#" onclick="javascript:cordYMenu1(document.all.M1.value);" onmouseout=FW_startTimeout(); class="menus">Pembelian</A>
            | <A id="divMenu2" href="#" onclick="javascript:cordYMenu2(document.all.M2.value);" onmouseout=FW_startTimeout(); class="menus">Penerimaan</A>
            | <A id="divMenu3" href="#" onclick="javascript:cordYMenu3(document.all.M3.value);" onmouseout=FW_startTimeout(); class="menus">Retur</A>
            | <A id="divMenu4" href="#" onclick="javascript:cordYMenu4(document.all.M4.value);" onmouseout=FW_startTimeout(); class="menus">Transfer</A>
            | <A id="divMenu10" href="#" onclick="javascript:cordYMenu10(document.all.M10.value);" onmouseout=FW_startTimeout(); class="menus">Pengiriman Barang</A>
            | <A id="divMenu5" href="#" onclick="javascript:cordYMenu5(document.all.M5.value);" onmouseout=FW_startTimeout(); class="menus">Pembiayaan</A>
            | <A id="divMenu6" href="#" onclick="javascript:cordYMenu6(document.all.M6.value);" onmouseout=FW_startTimeout(); class="menus">Stok</A>
            | <A id="divMenu7" href="#" onclick="javascript:cordYMenu7(document.all.M7.value);" onmouseout=FW_startTimeout(); class="menus">Masterdata</A>
            | <A id="divMenu8" href="#" onclick="javascript:cordYMenu8(document.all.M8.value);" onmouseout=FW_startTimeout(); class="menus">Sistem</A>
            | <A href="<%=approot%>/logout.jsp" class="menus">Keluar</A>
         </div>
      <%}else{%>
            <div align="center">
              <A id="divMenu0" href="<%=approot%>/master/data_sync/transfer_sale_server_to_outlet.jsp" class="menus">Transfer Penjualan</A>
            | <A id="divMenu1" href="<%=approot%>/master/data_sync/transfer_catalog_outlet_to_server.jsp" class="menus">Ambil Katalog</A>
            | <A id="divMenu1" href="<%=approot%>/master/material/src_update_harga.jsp" class="menus">Edit Catalog</A>
            | <A href="<%=approot%>/logout.jsp" class="menus">Keluar</A>
         </div>
      <%}
      %>
    </td>
  </tr>
  <tr>
    <td height="1" nowrap bgcolor="#A73701"><img src="home_files/spacer.gif" width=1 height=1></td>
  </tr>
</table>
