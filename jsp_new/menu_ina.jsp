<%@ page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<script language=JavaScript>
 /**
 * This function used to make fulldown menu only
 */
function fwLoadMenus()
{
  if(window.fw_menu_0) return;

    // START MENU STORE ----------------
                  //sub menu receiving from supplier
                  window.fw_menu_1_0_1_0 = new Menu("02. Laporan",145,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
                  fw_menu_1_0_1_0.addMenuItem("01. Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportreceive_in_store.jsp'");
				  fw_menu_1_0_1_0.addMenuItem("02. Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinvoice_in_store.jsp'");
				  fw_menu_1_0_1_0.addMenuItem("03. Barang Bonus", "location='<%=approot%>/warehouse/material/report/src_reportreceive_bonus.jsp'");
				  fw_menu_1_0_1_0.addMenuItem("04. Tukar Guling", "location='<%=approot%>/warehouse/material/report/src_reportreceive_exchange.jsp'");
				  fw_menu_1_0_1_0.hideOnMouseOut=true;

			  window.fw_menu_1_0_1 = new Menu("01. Dari Supplier",120,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			  fw_menu_1_0_1.addMenuItem("01. Dengan PO", "location='<%=approot%>/warehouse/material/receive/srcreceive_store_supp_po_material.jsp'");
			  fw_menu_1_0_1.addMenuItem("02. Tanpa PO", "location='<%=approot%>/warehouse/material/receive/srcreceive_store_supp_material.jsp'");
			  fw_menu_1_0_1.addMenuItem(fw_menu_1_0_1_0);
			  fw_menu_1_0_1.childMenuIcon="<%=approot%>/images/arrows.gif";
			  fw_menu_1_0_1.hideOnMouseOut=true;

				  //sub menu receiving from warehouse
				  window.fw_menu_1_0_2_0 = new Menu("02. Laporan",145,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
				  fw_menu_1_0_2_0.addMenuItem("01. Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinternalinvoice_in_store.jsp'");
				  fw_menu_1_0_2_0.addMenuItem("02. Rekap Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinternalinvoicerekap_in_store.jsp'");
				  fw_menu_1_0_2_0.hideOnMouseOut=true;

			  window.fw_menu_1_0_2 = new Menu("01. Dari Gudang",120,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			  fw_menu_1_0_2.addMenuItem("01. Ubah/Tambah", "location='<%=approot%>/warehouse/material/receive/srcreceive_store_wh_material.jsp'");
			  fw_menu_1_0_2.addMenuItem(fw_menu_1_0_2_0);
			  fw_menu_1_0_2.childMenuIcon="<%=approot%>/images/arrows.gif";
			  fw_menu_1_0_2.hideOnMouseOut=true;


		  //sub menu receiving
		  window.fw_menu_1_0 = new Menu("01. Penerimaan Barang",120,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  //fw_menu_1_0.addMenuItem(fw_menu_1_0_1);
		  fw_menu_1_0.addMenuItem(fw_menu_1_0_2);
		  fw_menu_1_0.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_1_0.hideOnMouseOut=true;


				  //sub menu receiving to supplier
				  window.fw_menu_1_1_1_0 = new Menu("02. Laporan",145,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
				  fw_menu_1_1_1_0.addMenuItem("01. Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportreturn_in_store.jsp'");
				  fw_menu_1_1_1_0.addMenuItem("02. Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreturninvoice_in_store.jsp'");
				  fw_menu_1_1_1_0.addMenuItem("03. Rekap Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreturninvoicerekap_in_store.jsp'");
				  fw_menu_1_1_1_0.hideOnMouseOut=true;

			  //sub menu returning to supplier
			  window.fw_menu_1_1_1 = new Menu("01. Ke Supplier",120,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			  fw_menu_1_1_1.addMenuItem("01. Ubah/Tambah", "location='<%=approot%>/warehouse/material/return/srcreturn_store_supp_material.jsp'");
			  fw_menu_1_1_1.addMenuItem(fw_menu_1_1_1_0);
			  fw_menu_1_1_1.childMenuIcon="<%=approot%>/images/arrows.gif";
			  fw_menu_1_1_1.hideOnMouseOut=true;

				  //sub menu retuning from warehouse
				  window.fw_menu_1_1_2_0 = new Menu("03. Laporan",145,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
				  fw_menu_1_1_2_0.addMenuItem("01. Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreturninternalinvoice_in_store.jsp'");
				  fw_menu_1_1_2_0.addMenuItem("02. Rekap Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreturninternalinvoicerekap_in_store.jsp'");
				  fw_menu_1_1_2_0.hideOnMouseOut=true;

			  //sub menu returning to warehouse
			  window.fw_menu_1_1_2 = new Menu("01. Ke Gudang",140,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			  fw_menu_1_1_2.addMenuItem("01. Dengan Invoice", "location='<%=approot%>/warehouse/material/return/srcreturn_store_wh_material.jsp'");
              fw_menu_1_1_2.addMenuItem("02. Tanpa Invoice", "location='<%=approot%>/warehouse/material/return/srcreturn_store_wh_outinvoice.jsp'");
			  fw_menu_1_1_2.addMenuItem(fw_menu_1_1_2_0);
			  fw_menu_1_1_2.childMenuIcon="<%=approot%>/images/arrows.gif";
			  fw_menu_1_1_2.hideOnMouseOut=true;

		  //sub menu return
		  window.fw_menu_1_1 = new Menu("02. Retur Barang",120,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  //fw_menu_1_1.addMenuItem(fw_menu_1_1_1);
		  fw_menu_1_1.addMenuItem(fw_menu_1_1_2);
		  fw_menu_1_1.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_1_1.hideOnMouseOut=true;

                  //sales report
		  window.fw_menu_1_2 = new Menu("03. Laporan Penjualan",230,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  fw_menu_1_2.addMenuItem("01. Laporan Penjualan Global", "location='<%=approot%>/store/sale/report/src_reportsale_global.jsp'"); //_bydoc
		  fw_menu_1_2.addMenuItem("02. Laporan Penjualan Detail", "location='<%=approot%>/store/sale/report/src_reportsale_detail_global.jsp'");
		  fw_menu_1_2.addMenuItem("03. Laporan Penjualan Invoice Detail", "location='<%=approot%>/store/sale/report/src_reportsale_detail.jsp'");
		  fw_menu_1_2.addMenuItem("04. Laporan Gross Margin", "location='<%=approot%>/store/sale/report/src_reportsale_global_margin.jsp'");
          fw_menu_1_2.addMenuItem("05. Laporan Rekap Penjualan Harian", "location='<%=approot%>/warehouse/material/report/src_reportsalerekaptanggal.jsp'");
          fw_menu_1_2.addMenuItem("06. Laporan Pending Order (DP)", "location='<%=approot%>/store/sale/report/src_reportpendingorder.jsp'");
		  fw_menu_1_2.addMenuItem("07. Laporan Penjualan Per Invoice", "location='<%=approot%>/store/sale/report/src_reportsale.jsp'");
		  fw_menu_1_2.addMenuItem("08. Laporan Retur Per Invoice", "location='<%=approot%>/store/sale/report/src_reportreturn.jsp'");
          //fw_menu_1_2.addMenuItem("06. Laporan Pembayaran Pending Order", "location='<%=approot%>/store/sale/report/src_reportpendingorderused.jsp'");
          //fw_menu_1_2.addMenuItem("07. Laporan Penjualan Kredit", "location='<%=approot%>/store/sale/report/src_reportsalecredit.jsp'");
          fw_menu_1_2.addMenuItem("08. Laporan Pelunasan Piutang", "location='<%=approot%>/store/sale/report/src_reportsalepaymentcredit.jsp'");
		  fw_menu_1_2.addMenuItem("09. Laporan Rekap Piutang", "location='<%=approot%>/store/sale/report/src_reportar.jsp'");
		  fw_menu_1_2.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_1_2.hideOnMouseOut=true;

                  //window.fw_menu_1_2a = new Menu("04. Pembatalan Penjualan","location='<%=approot%>/store/sale/cancel/src_invoice.jsp'");
		  //fw_menu_1_2.addMenuItem("01. Laporan Penjualan Global", "location='<%=approot%>/store/sale/report/src_reportsale_global.jsp'"); //_bydoc
		  //fw_menu_1_2.addMenuItem("02. Laporan Penjualan Detail", "location='<%=approot%>/store/sale/report/src_reportsale_detail_global.jsp'");
		  //fw_menu_1_2.addMenuItem("03. Laporan Gross Margin", "location='<%=approot%>/store/sale/report/src_reportsale_global_margin.jsp'");
                  //fw_menu_1_2a.childMenuIcon="<%=approot%>/images/arrows.gif";
		  //fw_menu_1_2a.hideOnMouseOut=true;


		  //sales report
		  //window.fw_menu_1_2 = new Menu("03. Laporan Penjualan",210,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  //fw_menu_1_2.addMenuItem("01. Rekap harian Per Shift", "location='<%=approot%>/warehouse/material/report/src_reportsalerekap_bydoc.jsp'"); //_bydoc
		  //fw_menu_1_2.addMenuItem("02. Penjualan Regular", "location='<%=approot%>/warehouse/material/report/src_reportsale_reguler_bydoc.jsp'");
		  //fw_menu_1_2.addMenuItem("03. Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportsaleinvoice_bydoc.jsp'");
                  //fw_menu_1_2.addMenuItem("04. Rekap Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportsaleinvoicerekap_bydoc.jsp'");
		  //fw_menu_1_2.addMenuItem("05. Rekap Per Kategori",  "location='<%=approot%>/warehouse/material/report/src_reportsalerekapkategori_bydoc.jsp'");
		  //fw_menu_1_2.addMenuItem("06. Rekap Per Supplier",  "location='<%=approot%>/warehouse/material/report/src_reportsalerekapsupplier_bydoc.jsp'");
		  //fw_menu_1_2.addMenuItem("07. Rekap Penjualan CC",  "location='<%=approot%>/warehouse/material/report/src_reportsalerekapcc_bydoc.jsp'");
		  //fw_menu_1_2.addMenuItem("08. Grafik Penjualan Per Bulan",  "location='<%=approot%>/warehouse/material/report/src_reportsalegrafikkategori_bydoc.jsp'");
  		  //fw_menu_1_2.childMenuIcon="<%=approot%>/images/arrows.gif";
		  //fw_menu_1_2.hideOnMouseOut=true;

	        //sub menu opname
			window.fw_menu_1_3_0 = new Menu("01. Opname",90,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		    fw_menu_1_3_0.addMenuItem("01. Normal", "location='<%=approot%>/warehouse/material/stock/mat_opname_store_src.jsp'");
		    fw_menu_1_3_0.addMenuItem("02. Cepat", "location='<%=approot%>/warehouse/material/stock/mat_opname_store_quick_src.jsp'");
			fw_menu_1_3_0.hideOnMouseOut=true;

	        //sub menu laporan stok
			window.fw_menu_1_3_1 = new Menu("03. Laporan",185,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		    fw_menu_1_3_1.addMenuItem("01. Daftar Semua Stok", "location='<%=approot%>/warehouse/material/report/src_reportstocklist.jsp'");
		    fw_menu_1_3_1.addMenuItem("02. Daftar Stok Per Kategori", "location='<%=approot%>/warehouse/material/report/src_reportstockkategori.jsp?tipe=0'");
		    fw_menu_1_3_1.addMenuItem("03. Daftar Stok Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportstocksupplier.jsp?type=1'");
			fw_menu_1_3_1.addMenuItem("04. Stock Card", "location='<%=approot%>/warehouse/material/report/src_stockcard.jsp?type=0'");
		    fw_menu_1_3_1.addMenuItem("05. Posisi Semua Stok", "location='<%=approot%>/warehouse/material/report/src_reportstock.jsp'");
		    fw_menu_1_3_1.addMenuItem("06. Posisi Stok Per Kategori", "location='<%=approot%>/warehouse/material/report/src_reportposisistockkategori.jsp'");
		    //fw_menu_1_3_1.addMenuItem("07. Posisi Stok Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportposisistocksupplier.jsp'");
			fw_menu_1_3_1.hideOnMouseOut=true;

	      //sub menu stok
		  window.fw_menu_1_3 = new Menu("04. Manajemen Stok",135,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  fw_menu_1_3.addMenuItem("01. Opname", "location='<%=approot%>/warehouse/material/stock/mat_opname_store_src.jsp'");
		  fw_menu_1_3.addMenuItem("02. Koreksi Stok", "location='<%=approot%>/warehouse/material/stock/mat_stock_store_correction_src.jsp'");
	  	  fw_menu_1_3.addMenuItem(fw_menu_1_3_1);
		  fw_menu_1_3.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_1_3.hideOnMouseOut=true;

	  window.fw_menu_1 = new Menu("root",170,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  fw_menu_1.addMenuItem(fw_menu_1_0);
	  fw_menu_1.addMenuItem(fw_menu_1_1);
	  fw_menu_1.addMenuItem(fw_menu_1_2);
	  // STO fw_menu_1.addMenuItem("04. Internal Invoice","location='<%=approot%>/store/sale/src_reportsale.jsp'");
	  //fw_menu_1.addMenuItem("05. Pembatalan Penjualan","location='<%=approot%>/store/sale/cancel/src_invoice.jsp'");
	  fw_menu_1.addMenuItem(fw_menu_1_3);
	  fw_menu_1.addMenuItem("05. Posting Harian", "location='<%=approot%>/master/posting/posting_store.jsp'");
	  fw_menu_1.childMenuIcon = "<%=approot%>/images/arrows.gif";
	  fw_menu_1.hideOnMouseOut = true;
	  // END MENU STORE ----------------


	  			// START MENU WAREHOUSE ----------------
				window.fw_menu_2_1_0_2 = new Menu("03. Laporan",153,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
				fw_menu_2_1_0_2.addMenuItem("01. Semua", "location='<%=approot%>/warehouse/material/report/src_reportreceive_all.jsp'");
				fw_menu_2_1_0_2.addMenuItem("02. Per Nota", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinvoice.jsp'");
				fw_menu_2_1_0_2.addMenuItem("03. Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportreceive.jsp'");
				fw_menu_2_1_0_2.addMenuItem("04. Per Kategori", "location='<%=approot%>/warehouse/material/report/src_reportreceivekategori.jsp'");
				fw_menu_2_1_0_2.addMenuItem("05. Rekap Hutang Detail", "location='<%=approot%>/arap/payable/ap_summary_search.jsp'");
				fw_menu_2_1_0_2.hideOnMouseOut=true;

			window.fw_menu_2_1_0 = new Menu("01. Dari Pembelian",130,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			//fw_menu_2_1_0.addMenuItem("01. Tanpa PO", "location='<%=approot%>/warehouse/material/receive/srcreceive_wh_supp_material.jsp'");
			//fw_menu_2_1_0.addMenuItem("02. Dengan PO", "location='<%=approot%>/warehouse/material/receive/srcreceive_wh_supp_po_material.jsp'");
			fw_menu_2_1_0.addMenuItem("01. Ubah/Tambah", "location='<%=approot%>/warehouse/material/receive/src_receive_material.jsp'");
			fw_menu_2_1_0.addMenuItem("02. Rekap Hutang", "location='<%=approot%>/arap/payable/payable_search.jsp'");
		  	fw_menu_2_1_0.addMenuItem(fw_menu_2_1_0_2);
			fw_menu_2_1_0.childMenuIcon="<%=approot%>/images/arrows.gif";
			fw_menu_2_1_0.hideOnMouseOut=true;

				window.fw_menu_2_1_1_0 = new Menu("02. Laporan",110,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
				fw_menu_2_1_1_0.addMenuItem("01. Semua", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinternal.jsp'");
				fw_menu_2_1_1_0.addMenuItem("02. Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinternalinvoice.jsp'");
				fw_menu_2_1_1_0.addMenuItem("03. Per Kategori", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinternalkategori.jsp'"); // src_reportreceiverekaptanggalkategoriinternal
				fw_menu_2_1_1_0.childMenuIcon="<%=approot%>/images/arrows.gif";
				fw_menu_2_1_1_0.hideOnMouseOut=true;

			window.fw_menu_2_1_1 = new Menu("02. Dari Retur Toko",130,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			fw_menu_2_1_1.addMenuItem("01. Ubah/Tambah", "location='<%=approot%>/warehouse/material/receive/srcreceive_wh_store_return_material.jsp'");
			fw_menu_2_1_1.addMenuItem(fw_menu_2_1_1_0);
		    fw_menu_2_1_1.childMenuIcon="<%=approot%>/images/arrows.gif";
			fw_menu_2_1_1.hideOnMouseOut=true;

		  window.fw_menu_2_1 = new Menu("02. Penerimaan Barang",130,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  fw_menu_2_1.addMenuItem(fw_menu_2_1_0);
		  fw_menu_2_1.addMenuItem(fw_menu_2_1_1);
		  fw_menu_2_1.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_2_1.hideOnMouseOut=true;

				  //sub menu receiving from warehouse
				  window.fw_menu_2_2_0 = new Menu("02. Laporan",145,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
				  fw_menu_2_2_0.addMenuItem("01. Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportreturn.jsp'");
				  fw_menu_2_2_0.addMenuItem("02. Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreturninvoice.jsp'");
				  fw_menu_2_2_0.addMenuItem("03. Rekap Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreturninvoicerekap.jsp'");
				  fw_menu_2_2_0.hideOnMouseOut=true;

		  // sub menu return
		  window.fw_menu_2_2 = new Menu("03. Retur Barang",130,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  //fw_menu_2_2.addMenuItem("01. Tanpa Nota", "location='<%=approot%>/warehouse/material/return/srcreturn_wh_supp_material.jsp'"); // srcreturn_wh_material.jsp
          //fw_menu_2_2.addMenuItem("02. Dengan Nota", "location='<%=approot%>/warehouse/material/return/srcreturn_wh_material.jsp'"); // srcreturn_wh_material.jsp
		  fw_menu_2_2.addMenuItem("01. Ubah/Tambah", "location='<%=approot%>/warehouse/material/return/src_return_material.jsp'");
		  fw_menu_2_2.addMenuItem(fw_menu_2_2_0);
		  fw_menu_2_2.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_2_2.hideOnMouseOut=true;


			window.fw_menu_2_4_0 = new Menu("02. Laporan",140,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			fw_menu_2_4_0.addMenuItem("01. Semua", "location='<%=approot%>/warehouse/material/report/src_reportdispatch.jsp'");
			fw_menu_2_4_0.addMenuItem("02. Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportdispatchinvoice.jsp'");
			fw_menu_2_4_0.addMenuItem("03. Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportdispatchsupplier.jsp'");
			fw_menu_2_4_0.addMenuItem("04. Per Kategori", "location='<%=approot%>/warehouse/material/report/src_reportdispatchkategori.jsp'");
			fw_menu_2_4_0.childMenuIcon="<%=approot%>/images/arrows.gif";
			fw_menu_2_4_0.hideOnMouseOut=true;

	      // sub menu transfer data
		  window.fw_menu_2_4 = new Menu("04. Transfer Barang",130,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  fw_menu_2_4.addMenuItem("01. Ubah/Tambah", "location='<%=approot%>/warehouse/material/dispatch/srcdf_stock_wh_material.jsp'");
		  fw_menu_2_4.addMenuItem(fw_menu_2_4_0);
		  fw_menu_2_4.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_2_4.hideOnMouseOut=true;


			window.fw_menu_2_5_0 = new Menu("03. Laporan",185,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		    fw_menu_2_5_0.addMenuItem("01. Daftar Semua Stok", "location='<%=approot%>/warehouse/material/report/src_reportstockwhlist.jsp'");
		    fw_menu_2_5_0.addMenuItem("02. Daftar Stok Per Kategori", "location='<%=approot%>/warehouse/material/report/src_reportstockkategori.jsp?tipe=1'");
		    fw_menu_2_5_0.addMenuItem("03. Daftar Stok Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportstocksupplier.jsp'");
			fw_menu_2_5_0.addMenuItem("04. Stock Card", "location='<%=approot%>/warehouse/material/report/src_stockcard.jsp?type=1'");
		    fw_menu_2_5_0.addMenuItem("05. Posisi Semua Stok", "location='<%=approot%>/warehouse/material/report/src_reportstock.jsp?type=1'");
		    fw_menu_2_5_0.addMenuItem("06. Posisi Stok Per Kategori", "location='<%=approot%>/warehouse/material/report/src_reportposisistockkategori.jsp?type=1'");
		    //fw_menu_2_5_0.addMenuItem("07. Posisi Stok Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportposisistocksupplier.jsp?type=1'");
			fw_menu_2_5_0.hideOnMouseOut=true;

	      // sub menu stok
		  window.fw_menu_2_5 = new Menu("06. Manajemen Stok",130,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  	  fw_menu_2_5.addMenuItem("01. Opname", "location='<%=approot%>/warehouse/material/stock/mat_opname_src.jsp'");
		  fw_menu_2_5.addMenuItem("02. Koreksi Stok", "location='<%=approot%>/warehouse/material/stock/mat_stock_correction_src.jsp'");
		  fw_menu_2_5.addMenuItem(fw_menu_2_5_0);
          fw_menu_2_5.addMenuItem("04. Minimum Stok", "location='<%=approot%>/warehouse/material/report/src_reportstockmin.jsp'");
		  fw_menu_2_5.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_2_5.hideOnMouseOut=true;

			window.fw_menu_2_7_0 = new Menu("02. Laporan",140,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			fw_menu_2_7_0.addMenuItem("01. Semua Barang", "location='<%=approot%>/warehouse/material/report/src_reportcosting.jsp'");
			fw_menu_2_7_0.addMenuItem("02. Per Kategori", "location='<%=approot%>/warehouse/material/report/src_reportcostingkategori.jsp'");
			//fw_menu_2_4_0.addMenuItem("03. Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportdispatchkategori.jsp'");
			//fw_menu_2_4_0.addMenuItem("04. Per Kategori", "location='<%=approot%>/warehouse/material/report/src_reportdispatchsupplier.jsp'");
			fw_menu_2_7_0.childMenuIcon="<%=approot%>/images/arrows.gif";
			fw_menu_2_7_0.hideOnMouseOut=true;

	      // sub menu transfer data
		  window.fw_menu_2_7 = new Menu("05. Costing",130,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  	  fw_menu_2_7.addMenuItem("01. Ubah/Tambah", "location='<%=approot%>/warehouse/material/dispatch/srccosting_material.jsp'");
		  fw_menu_2_7.addMenuItem(fw_menu_2_7_0); //"02. Laporan", "location='<%=approot%>/home.jsp'");
		  fw_menu_2_7.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_2_7.hideOnMouseOut=true;

	  window.fw_menu_2 = new Menu("root",170,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  fw_menu_2.addMenuItem("01. Order Pembelian", "location='<%=approot%>/purchasing/material/pom/srcpomaterial.jsp'");
	  fw_menu_2.addMenuItem(fw_menu_2_1); // penerimaan barang
	  fw_menu_2.addMenuItem(fw_menu_2_2); // Retur Barang
	  fw_menu_2.addMenuItem(fw_menu_2_4); // transfer barang
	  fw_menu_2.addMenuItem(fw_menu_2_7); // costing
	  fw_menu_2.addMenuItem(fw_menu_2_5); // stock management
	  fw_menu_2.addMenuItem("07. Posting Harian", "location='<%=approot%>/master/posting/posting_warehouse.jsp'"); // transaksi
	  fw_menu_2.childMenuIcon="<%=approot%>/images/arrows.gif";
	  fw_menu_2.hideOnMouseOut=true;
	  // END MENU WAREHOUSE ----------------


	      // sub menu transfer data
		  window.fw_menu_3_1 = new Menu("19. Import Data",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  	  fw_menu_3_1.addMenuItem("01. Barang", "location='<%=approot%>/excel/upload/importcatalogsrc.jsp'");
		  fw_menu_3_1.addMenuItem("02. Supplier", "location='<%=approot%>/excel/upload/importsuppliersrc.jsp'");
          //fw_menu_3_1.addMenuItem("03. Customer", "location='<%=approot%>/excel/upload/importcustomersrc.jsp'");
          fw_menu_3_1.addMenuItem("03. Member", "location='<%=approot%>/excel/upload/importmembersrc.jsp'");
		  fw_menu_3_1.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_3_1.hideOnMouseOut=true;

	      // sub menu transfer data
		  window.fw_menu_3_0 = new Menu("06. Member",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  	  fw_menu_3_0.addMenuItem("01. Ubah/Tambah", "location='<%=approot%>/masterdata/srcmemberreg.jsp'");
		  fw_menu_3_0.addMenuItem("02. Ubah Poin", "location='<%=approot%>/masterdata/srcmemberreg_point.jsp'");
		  fw_menu_3_0.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_3_0.hideOnMouseOut=true;


	  // START MENU MASTER DATA ----------------
	  window.fw_menu_3 = new Menu("root",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  fw_menu_3.addMenuItem("01. Daftar Barang", "location='<%=approot%>/master/material/srcmaterial.jsp'");
      fw_menu_3.addMenuItem("02. Simple Catalog", "location='<%=approot%>/master/material/simple_main.jsp'");
	  fw_menu_3.addMenuItem("03. Daftar Supplier", "location='<%=approot%>/master/contact/srccontactcompany.jsp'");
	  fw_menu_3.addMenuItem("04. Daftar Lokasi", "location='<%=approot%>/master/location/location.jsp'");
	  fw_menu_3.addMenuItem("", "");
	  fw_menu_3.addMenuItem("05. Tipe Member", "location='<%=approot%>/masterdata/membergroup.jsp'");
	  //fw_menu_3.addMenuItem("04. Daftar Member", "location='<%=approot%>/masterdata/srcmemberreg.jsp'");
      fw_menu_3.addMenuItem(fw_menu_3_0);
	  fw_menu_3.addMenuItem("", "");
	  fw_menu_3.addMenuItem("07. Daftar Periode", "location='<%=approot%>/master/periode/period.jsp'");
	  fw_menu_3.addMenuItem("08. Kategori Barang", "location='<%=approot%>/master/material/matcategory.jsp'");
	  fw_menu_3.addMenuItem("09. Daftar Merk", "location='<%=approot%>/master/material/matmerk.jsp'");
	  fw_menu_3.addMenuItem("10. Daftar Satuan", "location='<%=approot%>/master/material/matunit.jsp'");
	  fw_menu_3.addMenuItem("", "");
	  fw_menu_3.addMenuItem("11. Master Kasir", "location='<%=approot%>/master/cashier/master_kasir.jsp'");
	  fw_menu_3.addMenuItem("12. Shift Kerja", "location='<%=approot%>/master/material/matshift.jsp'");
	  fw_menu_3.addMenuItem("13. Daftar Sales", "location='<%=approot%>/master/material/matsales.jsp'");
	  fw_menu_3.addMenuItem("", "");
	  fw_menu_3.addMenuItem("14. Jenis Mata Uang", "location='<%=approot%>/masterdata/currencytype.jsp'");
	  fw_menu_3.addMenuItem("15. Sistem Pembayaran", "location='<%=approot%>/master/payment/paymentsystem.jsp'");
	  fw_menu_3.addMenuItem("16. Nilai Tukar Harian", "location='<%=approot%>/masterdata/dailyrate_list.jsp'");
	  fw_menu_3.addMenuItem("17. Nilai Tukar Standar", "location='<%=approot%>/masterdata/standartrate.jsp'");
	  fw_menu_3.addMenuItem("18. Tipe Harga", "location='<%=approot%>/masterdata/pricetype.jsp'");
  	  fw_menu_3.addMenuItem("19. Tipe Potongan", "location='<%=approot%>/masterdata/discounttype.jsp'");
	fw_menu_3.addMenuItem("20. Kode Grup", "location='<%=approot%>/master/material/coderange.jsp'");

      fw_menu_3.addMenuItem(fw_menu_3_1);

      fw_menu_3.childMenuIcon="<%=approot%>/images/arrows.gif";
	  fw_menu_3.hideOnMouseOut=true;
	  // END MENU MASTER DATA ----------------

	  // START MENU SYSTEM ----------------
	  	// sub menu transfer data
		  window.fw_menu_4_1 = new Menu("05. Transfer Data",120,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  	  fw_menu_4_1.addMenuItem("01. Data Master", "location='<%=approot%>/transferdata/transferalltoclient.jsp'");
		  //fw_menu_4_1.addMenuItem("02. Data Master", "location='<%=approot%>/transferdata/transfertoclient.jsp'");
		  fw_menu_4_1.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_4_1.hideOnMouseOut=true;

		  // sub menu transfer data
		  window.fw_menu_4_2 = new Menu("06. Restore Data",120,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  	  fw_menu_4_2.addMenuItem("01. Penjualan", "location='<%=approot%>/transferdata/restoreallinclient.jsp'");
		  //fw_menu_4_2.addMenuItem("02. Data Master", "location='<%=approot%>/transferdata/restoreinclient.jsp'");
		  fw_menu_4_2.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_4_2.hideOnMouseOut=true;

	  window.fw_menu_4 = new Menu("root",140,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
      fw_menu_4.addMenuItem("01. Pemakai", "location='<%=approot%>/admin/userlist.jsp'");
      fw_menu_4.addMenuItem("02. Grup", "location='<%=approot%>/admin/grouplist.jsp'");
      fw_menu_4.addMenuItem("03. Privilege", "location='<%=approot%>/admin/privilegelist.jsp'");
	  fw_menu_4.addMenuItem("04. Backup Data", "location='<%=approot%>/service/service_center.jsp'");
      //fw_menu_4.addMenuItem("03. Data Master", "location='<%=approot%>/transferdata/restoreallinclient.jsp'");
      //fw_menu_4.addMenuItem("04. Penjualan", "location='<%=approot%>/transferdata/transferalltoclient.jsp'");
	  fw_menu_4.addMenuItem(fw_menu_4_1);
	  fw_menu_4.addMenuItem(fw_menu_4_2);
	  fw_menu_4.addMenuItem("05. Setting Aplikasi", "location='<%=approot%>/system/sysprop.jsp'");
      fw_menu_4.addMenuItem("06. Tutup Periode", "location='<%=approot%>/master/closing/closing_monthly.jsp'");
	  fw_menu_4.childMenuIcon="<%=approot%>/images/arrows.gif";
      fw_menu_4.hideOnMouseOut=true;
	  // END MENU SYSTEM ----------------

	  fw_menu_4.writeMenus();
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
</script>

<script language=JavaScript src="<%=approot%>/main/fw_menu.js"></script>
<script language=JavaScript1.2>fwLoadMenus();</script>

<link rel="stylesheet" href="../styles/tab.css" type="text/css">
<input type="hidden" id="M0">
<input type="hidden" id="M1">
<input type="hidden" id="M2">
<input type="hidden" id="M3">

<script>
function setScr() {
	document.all.M0.value = document.all.divMenu0.offsetLeft + 0;
	document.all.M1.value = document.all.divMenu1.offsetLeft + 0;
	document.all.M2.value = document.all.divMenu2.offsetLeft + 0;
	document.all.M3.value = document.all.divMenu3.offsetLeft + 0;
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
            <div align="center">
              <%
                switch(userGroupNewStatus){
                    case PstAppUser.GROUP_STORE:
                        %>
                        <A id="divMenu0" href="#" onclick="javascript:cordYMenu0(document.all.M0.value);" onmouseout=FW_startTimeout(); class="menus">Toko</A>
                        <A id="divMenu1" href="#" onclick="javascript:cordYMenu1(document.all.M1.value);" onmouseout=FW_startTimeout(); class="menus"></A>
                        <A id="divMenu2" href="#" onclick="javascript:cordYMenu2(document.all.M2.value);" onmouseout=FW_startTimeout(); class="menus"></A>
                        <A id="divMenu3" href="#" onclick="javascript:cordYMenu3(document.all.M3.value);" onmouseout=FW_startTimeout(); class="menus"></A>
                        | <A href="<%=approot%>/logout.jsp" class="menus">Keluar</A>
                        <%
                break;
                    case PstAppUser.GROUP_WAREHOUSE:
                        %>
                        <A id="divMenu0" href="#" onclick="javascript:cordYMenu0(document.all.M0.value);" onmouseout=FW_startTimeout(); class="menus"></A>
                        <A id="divMenu1" href="#" onclick="javascript:cordYMenu1(document.all.M1.value);" onmouseout=FW_startTimeout(); class="menus">Gudang</A>
                        <A id="divMenu2" href="#" onclick="javascript:cordYMenu2(document.all.M2.value);" onmouseout=FW_startTimeout(); class="menus"></A>
                        <A id="divMenu3" href="#" onclick="javascript:cordYMenu3(document.all.M3.value);" onmouseout=FW_startTimeout(); class="menus"></A>
                        | <A href="<%=approot%>/logout.jsp" class="menus">Keluar</A>
                        <%
                        break;
                    case PstAppUser.GROUP_SUPERVISOR:
                        break;
                    case PstAppUser.GROUP_ADMINISTRATOR:
                        %>
                        <A id="divMenu0" href="#" onclick="javascript:cordYMenu0(document.all.M0.value);" onmouseout=FW_startTimeout(); class="menus">Toko</A>
                        | <A id="divMenu1" href="#" onclick="javascript:cordYMenu1(document.all.M1.value);" onmouseout=FW_startTimeout(); class="menus">Gudang</A>
                        | <A id="divMenu2" href="#" onclick="javascript:cordYMenu2(document.all.M2.value);" onmouseout=FW_startTimeout(); class="menus">Masterdata</A>
                        | <A id="divMenu3" href="#" onclick="javascript:cordYMenu3(document.all.M3.value);" onmouseout=FW_startTimeout(); class="menus">Sistem</A>
                        | <A href="<%=approot%>/logout.jsp" class="menus">Keluar</A>
                        <%
                        break;
                    case PstAppUser.GROUP_CASHIER:
                        break;
                }
              %>
            </div>
    </td>
  </tr>
  <tr>
    <td height="1" nowrap bgcolor="#A73701"><img src="home_files/spacer.gif" width=1 height=1></td>
  </tr>
</table>
