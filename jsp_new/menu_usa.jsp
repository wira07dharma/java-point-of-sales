<script language=JavaScript>
/**
 * This function used to make fulldown menu only
 */
function fwLoadMenus()
{
  if(window.fw_menu_0) return;

	  // START MENU STORE ----------------
		      	  //sub menu receiving from supplier
				  window.fw_menu_1_0_1_0 = new Menu("02. Report",130,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");	
				  fw_menu_1_0_1_0.addMenuItem("01. Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportreceive_in_store.jsp'");		  				  			  
				  fw_menu_1_0_1_0.addMenuItem("02. Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinvoice_in_store.jsp'");		  
				  fw_menu_1_0_1_0.addMenuItem("03. Bonus Goods", "location='<%=approot%>/warehouse/material/report/src_reportreceive_bonus.jsp'");
				  fw_menu_1_0_1_0.addMenuItem("04. Goods Exchange", "location='<%=approot%>/warehouse/material/report/src_reportreceive_exchange.jsp'");
				  fw_menu_1_0_1_0.hideOnMouseOut=true;		   	  	  	  						  
				  
			  window.fw_menu_1_0_1 = new Menu("01. From Supplier",120,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			  fw_menu_1_0_1.addMenuItem("01. With PO", "location='<%=approot%>/warehouse/material/receive/srcreceive_store_supp_po_material.jsp'");
			  fw_menu_1_0_1.addMenuItem("02. Without PO", "location='<%=approot%>/warehouse/material/receive/srcreceive_store_supp_material.jsp'");
			  fw_menu_1_0_1.addMenuItem(fw_menu_1_0_1_0);		  			  			  
			  fw_menu_1_0_1.childMenuIcon="<%=approot%>/images/arrows.gif"; 	  	  	  		  						  
			  fw_menu_1_0_1.hideOnMouseOut=true;	 		   	  	  	  

				  //sub menu receiving from warehouse
				  window.fw_menu_1_0_2_0 = new Menu("02. Report",160,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
				  fw_menu_1_0_2_0.addMenuItem("01. Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinternalinvoice_in_store.jsp'");		  
				  fw_menu_1_0_2_0.addMenuItem("02. Summary Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinternalinvoicerekap_in_store.jsp'");		  												  
				  fw_menu_1_0_2_0.hideOnMouseOut=true;	 
				  		   	  	  	     		
			  window.fw_menu_1_0_2 = new Menu("01. From Warehouse",120,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			  fw_menu_1_0_2.addMenuItem("01. Edit/Add", "location='<%=approot%>/warehouse/material/receive/srcreceive_store_wh_material.jsp'");		  
			  fw_menu_1_0_2.addMenuItem(fw_menu_1_0_2_0);
			  fw_menu_1_0_2.childMenuIcon="<%=approot%>/images/arrows.gif"; 			  			  			  			  
			  fw_menu_1_0_2.hideOnMouseOut=true;	 		   	  	  	  
				  

		  //sub menu receiving
		  window.fw_menu_1_0 = new Menu("01. Goods Receiving",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  //fw_menu_1_0.addMenuItem(fw_menu_1_0_1);
		  fw_menu_1_0.addMenuItem(fw_menu_1_0_2);
		  fw_menu_1_0.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_1_0.hideOnMouseOut=true;
		  
				  //sub menu receiving to supplier
				  window.fw_menu_1_1_1_0 = new Menu("02. Report",160,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");				  
				  fw_menu_1_1_1_0.addMenuItem("01. Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportreturn_in_store.jsp'");		  
				  fw_menu_1_1_1_0.addMenuItem("02. Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreturninvoice_in_store.jsp'");		  
				  fw_menu_1_1_1_0.addMenuItem("03. Summary Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreturninvoicerekap_in_store.jsp'");		  												  
				  fw_menu_1_1_1_0.hideOnMouseOut=true;	 		   	  	  	  		

			  //sub menu returning to supplier			  
			  window.fw_menu_1_1_1 = new Menu("01. To Supplier",120,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084"); 
			  fw_menu_1_1_1.addMenuItem("01. Edit/Add", "location='<%=approot%>/warehouse/material/return/srcreturn_store_supp_material.jsp'");			  
			  fw_menu_1_1_1.addMenuItem(fw_menu_1_1_1_0);
			  fw_menu_1_1_1.childMenuIcon="<%=approot%>/images/arrows.gif"; 			  			  			  			  			  			  			  
			  fw_menu_1_1_1.hideOnMouseOut=true;	 		   	  	  	  

				  //sub menu receiving from warehouse
				  window.fw_menu_1_1_2_0 = new Menu("02. Report",160,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");				  
				  fw_menu_1_1_2_0.addMenuItem("01. Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreturninternalinvoice_in_store.jsp'");		  
				  fw_menu_1_1_2_0.addMenuItem("02. Summary Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreturninternalinvoicerekap_in_store.jsp'");		  												  
				  fw_menu_1_1_2_0.hideOnMouseOut=true;	 		   	  	  	  		
				  
			  //sub menu returning to warehouse			  
			  window.fw_menu_1_1_2 = new Menu("01. To Warehouse",120,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084"); 
			  fw_menu_1_1_2.addMenuItem("01. With Invoice", "location='<%=approot%>/warehouse/material/return/srcreturn_store_wh_material.jsp'");
              fw_menu_1_1_2.addMenuItem("02. Without Invoice", "location='<%=approot%>/warehouse/material/return/srcreturn_store_wh_outinvoice.jsp'");
			  fw_menu_1_1_2.addMenuItem(fw_menu_1_1_2_0);
			  fw_menu_1_1_2.childMenuIcon="<%=approot%>/images/arrows.gif";
			  fw_menu_1_1_2.hideOnMouseOut=true;	 		   	  	  	  	

		  //sub menu return
		  window.fw_menu_1_1 = new Menu("02. Goods Returning",140,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  //fw_menu_1_1.addMenuItem(fw_menu_1_1_1);
		  fw_menu_1_1.addMenuItem(fw_menu_1_1_2);
		  fw_menu_1_1.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_1_1.hideOnMouseOut=true;


                    //sales report
		  //window.fw_menu_1_2 = new Menu("03. Laporan Penjualan",210,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  //fw_menu_1_2.addMenuItem("01. Global Sale Report", "location='<%=approot%>/store/sale/report/src_reportsale_global.jsp'"); //_bydoc
		  //fw_menu_1_2.addMenuItem("02. Detail Sale Report", "location='<%=approot%>/store/sale/report/src_reportsale_detail_global.jsp'");
		  ///fw_menu_1_2.addMenuItem("03. Gross Margin Report", "location='<%=approot%>/store/sale/report/src_reportsale_global_margin.jsp'");
           //       fw_menu_1_2.childMenuIcon="<%=approot%>/images/arrows.gif";
		  //fw_menu_1_2.hideOnMouseOut=true;


                  //sales report
		  window.fw_menu_1_2 = new Menu("03. Sale Reports",230,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  fw_menu_1_2.addMenuItem("01. Sale Global Report", "location='<%=approot%>/store/sale/report/src_reportsale_global.jsp'"); //_bydoc
		  fw_menu_1_2.addMenuItem("02. Sale Detail Report ", "location='<%=approot%>/store/sale/report/src_reportsale_detail_global.jsp'");
		  fw_menu_1_2.addMenuItem("03. Sale Detail Invoice Report", "location='<%=approot%>/store/sale/report/src_reportsale_detail.jsp'");
		  fw_menu_1_2.addMenuItem("03. Gross Margin Report", "location='<%=approot%>/store/sale/report/src_reportsale_global_margin.jsp'");
          fw_menu_1_2.addMenuItem("04. Daily Sale Summary Report", "location='<%=approot%>/warehouse/material/report/src_reportsalerekaptanggal.jsp'");
          fw_menu_1_2.addMenuItem("05. Down Payment Report", "location='<%=approot%>/store/sale/report/src_reportpendingorder.jsp'");
		  fw_menu_1_2.addMenuItem("06. Sale Invoice Report", "location='<%=approot%>/store/sale/report/src_reportsale.jsp'");
		  fw_menu_1_2.addMenuItem("07. Sale Return Report", "location='<%=approot%>/store/sale/report/src_reportreturn.jsp'");
          //fw_menu_1_2.addMenuItem("06. Laporan Pembayaran Pending Order", "location='<%=approot%>/store/sale/report/src_reportpendingorderused.jsp'");
          //fw_menu_1_2.addMenuItem("07. Sale Creadit Report", "location='<%=approot%>/store/sale/report/src_reportsalecredit.jsp'");
          fw_menu_1_2.addMenuItem("08. Payable Payment Report", "location='<%=approot%>/store/sale/report/src_reportsalepaymentcredit.jsp'");
		  fw_menu_1_2.addMenuItem("09. Payable Summary Report", "location='<%=approot%>/store/sale/report/src_reportar.jsp'");
		  fw_menu_1_2.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_1_2.hideOnMouseOut=true;

		  //sales report
		  //window.fw_menu_1_2 = new Menu("03. Sales Report",170,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  //fw_menu_1_2.addMenuItem("01. Shift Recapitulation", "location='<%=approot%>/warehouse/material/report/src_reportsalerekap_bydoc.jsp'");
		  //fw_menu_1_2.addMenuItem("02. Regular Sales", "location='<%=approot%>/warehouse/material/report/src_reportsale_reguler_bydoc.jsp'");
		  //fw_menu_1_2.addMenuItem("03. Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportsaleinvoice_bydoc.jsp'");
	      //fw_menu_1_2.addMenuItem("04. Summary per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportsaleinvoicerekap_bydoc.jsp'");
		  //fw_menu_1_2.addMenuItem("05. Summary per Kategory",  "location='<%=approot%>/warehouse/material/report/src_reportsalerekapkategori.jsp'");
		  //fw_menu_1_2.addMenuItem("06. Summary per Supplier",  "location='<%=approot%>/warehouse/material/report/src_reportsalerekapsupplier.jsp'");
		  //fw_menu_1_2.addMenuItem("07. Summary CC Sales",  "location='<%=approot%>/warehouse/material/report/src_reportsalerekapcc.jsp'");
		  //fw_menu_1_2.addMenuItem("08. Sales Chart Monthly",  "location='<%=approot%>/warehouse/material/report/src_reportsalegrafikkategori.jsp'");
  		  //fw_menu_1_2.childMenuIcon="<%=approot%>/images/arrows.gif";
		  //fw_menu_1_2.hideOnMouseOut=true;

	        //sub menu opname
			window.fw_menu_1_3_0 = new Menu("01. Opname",90,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		    fw_menu_1_3_0.addMenuItem("01. Normal", "location='<%=approot%>/warehouse/material/stock/mat_opname_store_src.jsp'");
		    //fw_menu_1_3_0.addMenuItem("02. Quick", "location='<%=approot%>/warehouse/material/stock/mat_opname_store_quick_src.jsp'");
			fw_menu_1_3_0.hideOnMouseOut=true;

	        //sub menu laporan stok
			window.fw_menu_1_3_1 = new Menu("03. Report",185,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		    fw_menu_1_3_1.addMenuItem("01. Stock All", "location='<%=approot%>/warehouse/material/report/src_reportstocklist.jsp'");
		    fw_menu_1_3_1.addMenuItem("02. Stock by Kategory", "location='<%=approot%>/warehouse/material/report/src_reportstockkategori.jsp?tipe=0'");
		    fw_menu_1_3_1.addMenuItem("03. Stock by Supplier", "location='<%=approot%>/warehouse/material/report/src_reportstocksupplier.jsp?type=1'");
			fw_menu_1_3_1.addMenuItem("04. Stock Card", "location='<%=approot%>/warehouse/material/report/src_stockcard.jsp?type=0'");
		    fw_menu_1_3_1.addMenuItem("");			
		    fw_menu_1_3_1.addMenuItem("05. Stock Position All", "location='<%=approot%>/warehouse/material/report/src_reportstock.jsp'");
		    fw_menu_1_3_1.addMenuItem("06. Stock Position by Category", "location='<%=approot%>/warehouse/material/report/src_reportposisistockkategori.jsp'");
		    //fw_menu_1_3_1.addMenuItem("07. Stock Position by Supplier", "location='<%=approot%>/warehouse/material/report/src_reportposisistocksupplier.jsp'");
			fw_menu_1_3_1.hideOnMouseOut=true;

	      //sub menu stok
		  window.fw_menu_1_3 = new Menu("04. Stok Management",135,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  fw_menu_1_3.addMenuItem("01. Opname", "location='<%=approot%>/warehouse/material/stock/mat_opname_store_src.jsp'");
		  fw_menu_1_3.addMenuItem("02. Stock Corection", "location='<%=approot%>/warehouse/material/stock/mat_stock_store_correction_src.jsp'");
	  	  fw_menu_1_3.addMenuItem(fw_menu_1_3_1);
		  fw_menu_1_3.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_1_3.hideOnMouseOut=true;

	  window.fw_menu_1 = new Menu("root",170,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  fw_menu_1.addMenuItem(fw_menu_1_0);
	  fw_menu_1.addMenuItem(fw_menu_1_1);
	  fw_menu_1.addMenuItem(fw_menu_1_2);
	  //  STO fw_menu_1.addMenuItem("04. Internal Invoice","location='<%=approot%>/store/sale/src_reportsale.jsp'");
	  //fw_menu_1.addMenuItem("05. Invoice Deletion","location='<%=approot%>/store/sale/cancel/src_invoice.jsp'");
	  fw_menu_1.addMenuItem(fw_menu_1_3);
	  fw_menu_1.addMenuItem("05. Daily Posting", "location='<%=approot%>/master/posting/posting_store.jsp'");
	  fw_menu_1.childMenuIcon = "<%=approot%>/images/arrows.gif";
	  fw_menu_1.hideOnMouseOut = true;
	  // END MENU STORE ----------------


	  			// START MENU WAREHOUSE ----------------
				window.fw_menu_2_1_0_2 = new Menu("03. Report",153,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
				fw_menu_2_1_0_2.addMenuItem("01. All", "location='<%=approot%>/warehouse/material/report/src_reportreceive_all.jsp'");
				fw_menu_2_1_0_2.addMenuItem("02. By Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinvoice.jsp'");
				fw_menu_2_1_0_2.addMenuItem("03. By Supplier", "location='<%=approot%>/warehouse/material/report/src_reportreceive.jsp'");
				fw_menu_2_1_0_2.addMenuItem("04. By Kategory", "location='<%=approot%>/warehouse/material/report/src_reportreceivekategori.jsp'");
				fw_menu_2_1_0_2.addMenuItem("05. Detail AP Summary", "location='<%=approot%>/arap/payable/ap_summary_search.jsp.jsp'");
				fw_menu_2_1_0_2.hideOnMouseOut=true;

			window.fw_menu_2_1_0 = new Menu("01. From Order",140,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			//fw_menu_2_1_0.addMenuItem("01. Without PO", "location='<%=approot%>/warehouse/material/receive/srcreceive_wh_supp_material.jsp'");
			//fw_menu_2_1_0.addMenuItem("02. With PO", "location='<%=approot%>/warehouse/material/receive/srcreceive_wh_supp_po_material.jsp'");
			fw_menu_2_1_0.addMenuItem("01. Update/Add", "location='<%=approot%>/warehouse/material/receive/src_receive_material.jsp'");
			fw_menu_2_1_0.addMenuItem("02. AP Summary", "location='<%=approot%>/arap/payable/payable_search.jsp'");
		  	fw_menu_2_1_0.addMenuItem(fw_menu_2_1_0_2);
			fw_menu_2_1_0.childMenuIcon="<%=approot%>/images/arrows.gif";
			fw_menu_2_1_0.hideOnMouseOut=true;

				window.fw_menu_2_1_1_0 = new Menu("02. Report",110,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
				fw_menu_2_1_1_0.addMenuItem("01. All", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinternal.jsp'");
				fw_menu_2_1_1_0.addMenuItem("02. By Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinternalinvoice.jsp'");
				fw_menu_2_1_1_0.addMenuItem("03. By Kategory", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinternalkategori.jsp'"); // src_reportreceiverekaptanggalkategoriinternal
				fw_menu_2_1_1_0.childMenuIcon="<%=approot%>/images/arrows.gif";
				fw_menu_2_1_1_0.hideOnMouseOut=true;

			window.fw_menu_2_1_1 = new Menu("02. From Store Return",140,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			fw_menu_2_1_1.addMenuItem("01. Update/Add", "location='<%=approot%>/warehouse/material/receive/srcreceive_wh_store_return_material.jsp'");
			fw_menu_2_1_1.addMenuItem(fw_menu_2_1_1_0);
		    fw_menu_2_1_1.childMenuIcon="<%=approot%>/images/arrows.gif";
			fw_menu_2_1_1.hideOnMouseOut=true;


		  window.fw_menu_2_1 = new Menu("02. Goods Receiving",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  fw_menu_2_1.addMenuItem(fw_menu_2_1_0);
		  fw_menu_2_1.addMenuItem(fw_menu_2_1_1);
		  fw_menu_2_1.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_2_1.hideOnMouseOut=true;

				  //sub menu receiving from warehouse
				  window.fw_menu_2_2_0 = new Menu("02. Report",145,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
				  fw_menu_2_2_0.addMenuItem("01. By Supplier", "location='<%=approot%>/warehouse/material/report/src_reportreturn.jsp'");
				  fw_menu_2_2_0.addMenuItem("02. By Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreturninvoice.jsp'");
				  fw_menu_2_2_0.addMenuItem("03. Rekap. by Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreturninvoicerekap.jsp'");
				  fw_menu_2_2_0.hideOnMouseOut=true;

		  // sub menu return
		  window.fw_menu_2_2 = new Menu("03. Goods Returning",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  //fw_menu_2_2.addMenuItem("01. With Invoice", "location='<%=approot%>/warehouse/material/return/srcreturn_wh_supp_material.jsp'"); // srcreturn_wh_material.jsp
          //fw_menu_2_2.addMenuItem("02. Without Invoice", "location='<%=approot%>/warehouse/material/return/srcreturn_wh_material.jsp'"); // srcreturn_wh_material.jsp
		  fw_menu_2_2.addMenuItem("01. Update/Add", "location='<%=approot%>/warehouse/material/return/src_return_material.jsp'");
          fw_menu_2_2.addMenuItem(fw_menu_2_2_0);
		  fw_menu_2_2.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_2_2.hideOnMouseOut=true;


			window.fw_menu_2_4_0 = new Menu("02. Report",140,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			fw_menu_2_4_0.addMenuItem("01. All", "location='<%=approot%>/warehouse/material/report/src_reportdispatch.jsp'");
			fw_menu_2_4_0.addMenuItem("02. Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportdispatchinvoice.jsp'");
			fw_menu_2_4_0.addMenuItem("03. Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportdispatchsupplier.jsp'");
			fw_menu_2_4_0.addMenuItem("04. Per Category", "location='<%=approot%>/warehouse/material/report/src_reportdispatchkategori.jsp'");
			fw_menu_2_4_0.childMenuIcon="<%=approot%>/images/arrows.gif";
			fw_menu_2_4_0.hideOnMouseOut=true;

	      // sub menu transfer data
		  window.fw_menu_2_4 = new Menu("04. Goods Dispatching",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  fw_menu_2_4.addMenuItem("01. Update/Add", "location='<%=approot%>/warehouse/material/dispatch/srcdf_stock_wh_material.jsp'");
		  fw_menu_2_4.addMenuItem(fw_menu_2_4_0);
		  fw_menu_2_4.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_2_4.hideOnMouseOut=true;


			// sub menu report
			window.fw_menu_2_5_0 = new Menu("03. Report",185,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		    fw_menu_2_5_0.addMenuItem("01. Stock All", "location='<%=approot%>/warehouse/material/report/src_reportstockwhlist.jsp'");
		    fw_menu_2_5_0.addMenuItem("02. Stock By Kategory", "location='<%=approot%>/warehouse/material/report/src_reportstockkategori.jsp?tipe=1'");
		    fw_menu_2_5_0.addMenuItem("03. Stock By Supplier", "location='<%=approot%>/warehouse/material/report/src_reportstocklist.jsp'");
			fw_menu_2_5_0.addMenuItem("04. Stock Card", "location='<%=approot%>/warehouse/material/report/src_stockcard.jsp?type=1'");						
		    fw_menu_2_5_0.addMenuItem("05. Stock Position All Goods", "location='<%=approot%>/warehouse/material/report/src_reportstock.jsp?type=1'");
		    fw_menu_2_5_0.addMenuItem("06. Stock Position by Kategory", "location='<%=approot%>/warehouse/material/report/src_reportposisistockkategori.jsp?type=1'");
		    //fw_menu_2_5_0.addMenuItem("07. Stock Position by Supplier", "location='<%=approot%>/warehouse/material/report/src_reportposisistocksupplier.jsp?type=1'");
			fw_menu_2_5_0.hideOnMouseOut=true;

	      // sub menu stok
		  window.fw_menu_2_5 = new Menu("06. Stock Management",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  	  fw_menu_2_5.addMenuItem("01. Opname", "location='<%=approot%>/warehouse/material/stock/mat_opname_src.jsp'");
		  fw_menu_2_5.addMenuItem("02. Stock Corection", "location='<%=approot%>/warehouse/material/stock/mat_stock_correction_src.jsp'");
		  fw_menu_2_5.addMenuItem(fw_menu_2_5_0);
          fw_menu_2_5.addMenuItem("04. Stock Minimum", "location='<%=approot%>/warehouse/material/report/src_reportstockmin.jsp'");
		  fw_menu_2_5.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_2_5.hideOnMouseOut=true;

			window.fw_menu_2_7_0 = new Menu("02. Report",140,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			fw_menu_2_7_0.addMenuItem("01. Goods All", "location='<%=approot%>/warehouse/material/report/src_reportcosting.jsp'");
			fw_menu_2_7_0.addMenuItem("02. Per Invoice", "location='<%=approot%>/warehouse/material/report/src_reportcostinginvoice.jsp'");
			// fw_menu_2_4_0.addMenuItem("03. Per Supplier", "location='<%=approot%>/warehouse/material/report/src_reportdispatchkategori.jsp'");
			// fw_menu_2_4_0.addMenuItem("04. Per Kategori", "location='<%=approot%>/warehouse/material/report/src_reportdispatchsupplier.jsp'");
			fw_menu_2_7_0.childMenuIcon="<%=approot%>/images/arrows.gif";
			fw_menu_2_7_0.hideOnMouseOut=true;

	      // sub menu transfer data
		  window.fw_menu_2_7 = new Menu("05. Costing",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  	  fw_menu_2_7.addMenuItem("01. Update/Add", "location='<%=approot%>/warehouse/material/dispatch/srccosting_material.jsp'");
		  fw_menu_2_7.addMenuItem(fw_menu_2_7_0); // "02. Report", "location='<%=approot%>/home.jsp'");
		  fw_menu_2_7.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_2_7.hideOnMouseOut=true;

	  window.fw_menu_2 = new Menu("root",170,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  fw_menu_2.addMenuItem("01. Purchase Order", "location='<%=approot%>/purchasing/material/pom/srcpomaterial.jsp'");
	  fw_menu_2.addMenuItem(fw_menu_2_1);
	  fw_menu_2.addMenuItem(fw_menu_2_2);
	  fw_menu_2.addMenuItem(fw_menu_2_4);
	  fw_menu_2.addMenuItem(fw_menu_2_7);
	  fw_menu_2.addMenuItem(fw_menu_2_5);
	  fw_menu_2.addMenuItem("07. Daily Posting", "location='<%=approot%>/master/posting/posting_warehouse.jsp'");
	  fw_menu_2.childMenuIcon="<%=approot%>/images/arrows.gif";
	  fw_menu_2.hideOnMouseOut=true;
	  // END MENU WAREHOUSE ----------------

	      // sub menu transfer data
		  window.fw_menu_3_1 = new Menu("19. Import Data",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  	  fw_menu_3_1.addMenuItem("01. Product", "location='<%=approot%>/excel/upload/importcatalogsrc.jsp'");
		  fw_menu_3_1.addMenuItem("02. Supplier", "location='<%=approot%>/excel/upload/importsuppliersrc.jsp'");
          //fw_menu_3_1.addMenuItem("03. Customer", "location='<%=approot%>/excel/upload/importcustomersrc.jsp'");
          fw_menu_3_1.addMenuItem("03. Member", "location='<%=approot%>/excel/upload/importmembersrc.jsp'");
		  //fw_menu_3_1.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_3_1.hideOnMouseOut=true;

	  // START MENU MASTER DATA ----------------	  
	  window.fw_menu_3 = new Menu("root",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  fw_menu_3.addMenuItem("01. Catalog", "location='<%=approot%>/master/material/srcmaterial.jsp'");
      fw_menu_3.addMenuItem("02. Input Catalog", "location='<%=approot%>/master/material/simple_main.jsp'");
	  fw_menu_3.addMenuItem("03. Supplier", "location='<%=approot%>/master/contact/srccontactcompany.jsp'");
	  fw_menu_3.addMenuItem("04. Location", "location='<%=approot%>/master/location/location.jsp'");
	  fw_menu_3.addMenuItem("", "");	  
	  fw_menu_3.addMenuItem("05. Member Type", "location='<%=approot%>/masterdata/membergroup.jsp'");
	  fw_menu_3.addMenuItem("06. Member", "location='<%=approot%>/masterdata/srcmemberreg.jsp'");
	  fw_menu_3.addMenuItem("", "");	  	  	  	  
	  fw_menu_3.addMenuItem("07. Period", "location='<%=approot%>/master/periode/period.jsp'");
	  fw_menu_3.addMenuItem("08. Group", "location='<%=approot%>/master/material/matcategory.jsp'");
	  fw_menu_3.addMenuItem("09. Category", "location='<%=approot%>/master/material/matmerk.jsp'");
	  fw_menu_3.addMenuItem("10. Unit", "location='<%=approot%>/master/material/matunit.jsp'");
	  fw_menu_3.addMenuItem("", "");	  	  
	  fw_menu_3.addMenuItem("11. Cashier Master", "location='<%=approot%>/master/cashier/master_kasir.jsp'");
	  fw_menu_3.addMenuItem("12. Working Shift", "location='<%=approot%>/master/material/matshift.jsp'");
	  fw_menu_3.addMenuItem("13. Sales", "location='<%=approot%>/master/material/matsales.jsp'");
	  fw_menu_3.addMenuItem("", "");	  
	  fw_menu_3.addMenuItem("14. Currency Type", "location='<%=approot%>/masterdata/currencytype.jsp'");
	  fw_menu_3.addMenuItem("15. Payment System", "location='<%=approot%>/master/payment/paymentsystem.jsp'");
	  fw_menu_3.addMenuItem("16. Daily Rate", "location='<%=approot%>/masterdata/dailyrate_list.jsp'");
	  fw_menu_3.addMenuItem("17. Standart Rate", "location='<%=approot%>/masterdata/standartrate.jsp'");
	  fw_menu_3.addMenuItem("18. Price Type", "location='<%=approot%>/masterdata/pricetype.jsp'");
  	  fw_menu_3.addMenuItem("19. Discount Type", "location='<%=approot%>/masterdata/discounttype.jsp'");
	fw_menu_3.addMenuItem("20. Code Range", "location='<%=approot%>/master/material/rangecode.jsp'");

	  fw_menu_3.addMenuItem(fw_menu_3_1);
	  fw_menu_3.childMenuIcon="<%=approot%>/images/arrows.gif";
	  fw_menu_3.hideOnMouseOut=true;
	  // END MENU MASTER DATA ----------------	  	  

	  // START MENU SYSTEM ----------------	  
	  	// sub menu transfer data
		  window.fw_menu_4_1 = new Menu("05. Transfer Data",120,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  	  fw_menu_4_1.addMenuItem("01. Master Data", "location='<%=approot%>/transferdata/transferalltoclient.jsp'");
		  //fw_menu_4_1.addMenuItem("02. Master Data", "location='<%=approot%>/transferdata/transfertoclient.jsp'");
		  fw_menu_4_1.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_4_1.hideOnMouseOut=true;

		  // sub menu transfer data
		  window.fw_menu_4_2 = new Menu("06. Restore Data",120,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  	  fw_menu_4_2.addMenuItem("01. Selling", "location='<%=approot%>/transferdata/restoreallinclient.jsp'");
		  //fw_menu_4_2.addMenuItem("02. Master Data", "location='<%=approot%>/transferdata/restoreinclient.jsp'");
		  fw_menu_4_2.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_4_2.hideOnMouseOut=true;

	  window.fw_menu_4 = new Menu("root",140,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
      fw_menu_4.addMenuItem("01. User", "location='<%=approot%>/admin/userlist.jsp'");
      fw_menu_4.addMenuItem("02. Group", "location='<%=approot%>/admin/grouplist.jsp'");
      fw_menu_4.addMenuItem("03. Privilege", "location='<%=approot%>/admin/privilegelist.jsp'");
	  fw_menu_4.addMenuItem("04. Backup Database", "location='<%=approot%>/service/service_center.jsp'");
      //fw_menu_4.addMenuItem("03. Masterdata", "location='<%=approot%>/transferdata/restoreallinclient.jsp'");
      //fw_menu_4.addMenuItem("04. Selling", "location='<%=approot%>/transferdata/transferalltoclient.jsp'");
	  fw_menu_4.addMenuItem(fw_menu_4_1);
	  fw_menu_4.addMenuItem(fw_menu_4_2);
	  fw_menu_4.addMenuItem("07. Aplication Setting", "location='<%=approot%>/system/sysprop.jsp'");
      fw_menu_4.addMenuItem("08. Close Period", "location='<%=approot%>/master/closing/closing_monthly.jsp'");
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
  <tr>
    <td height="1" nowrap bgcolor="#A73701"><img src="home_files/spacer.gif" width=1 height=1></td>
  </tr>
    <td nowrap>
            <div align="center">
			  <A id="divMenu0" href="#" onclick="javascript:cordYMenu0(document.all.M0.value);" onmouseout=FW_startTimeout(); class="menus">Store</A>
              | <A id="divMenu1" href="#" onclick="javascript:cordYMenu1(document.all.M1.value);" onmouseout=FW_startTimeout(); class="menus">Warehouse</A>
			  | <A id="divMenu2" href="#" onclick="javascript:cordYMenu2(document.all.M2.value);" onmouseout=FW_startTimeout(); class="menus">Masterdata</A>
			  | <A id="divMenu3" href="#" onclick="javascript:cordYMenu3(document.all.M3.value);" onmouseout=FW_startTimeout(); class="menus">System</A>
              | <A href="<%=approot%>/logout.jsp" class="menus">Logout</A>
            </div>
    </td>
  <tr>
    <td height="1" nowrap bgcolor="#A73701"><img src="home_files/spacer.gif" width=1 height=1></td>
  </tr>
  </tr>
</table>