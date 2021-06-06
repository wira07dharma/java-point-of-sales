<script language=JavaScript>
/**
 * This function used to make fulldown menu only
 */
function fwLoadMenus() {
  if(window.fw_menu_0) return;

	  /** START MENU PENJUALAN / SALES */
		  window.fw_menu_1_1 = new Menu("01. Report",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  fw_menu_1_1.addMenuItem("01. Sales Global ", "location='<%=approot%>/store/sale/report/src_reportsale_global.jsp'");
		  fw_menu_1_1.addMenuItem("02. Sales Detail", "location='<%=approot%>/store/sale/report/src_reportsale_detail_global.jsp'");
		  fw_menu_1_1.addMenuItem("03. Gross Margin", "location='<%=approot%>/store/sale/report/src_reportsale_global_margin.jsp'");
		  fw_menu_1_1.addMenuItem("04. Daily Summary", "location='<%=approot%>/store/sale/report/src_reportsalerekaptanggal.jsp'");
		  fw_menu_1_1.addMenuItem("05. Pending Order (DP)", "location='<%=approot%>/store/sale/report/src_reportpendingorder.jsp'");
		  fw_menu_1_1.addMenuItem("06. By Invoice", "location='<%=approot%>/store/sale/report/src_reportsale.jsp'");
		  fw_menu_1_1.addMenuItem("07. Detail Invoice", "location='<%=approot%>/store/sale/report/src_reportsale_detail.jsp'");
		  fw_menu_1_1.addMenuItem("08. Return by Invoice", "location='<%=approot%>/store/sale/report/src_reportreturn.jsp'");
		  fw_menu_1_1.addMenuItem("09. Rekapitulasi Report", "location='<%=approot%>/store/sale/report/src_reportrekapkategori.jsp'");
		  fw_menu_1_1.addMenuItem("10. Rekap Per KSG", "location='<%=approot%>/store/sale/report/src_reportrekap.jsp'");
                  fw_menu_1_1.addMenuItem("11. Per Cash Opening ", "location='<%=approot%>/store/sale/report/src_report_cash_opening.jsp'");
                  //fw_menu_1_1.addMenuItem("07. Sales Credit", "location='<%=approot%>/store/sale/report/src_reportsalecredit.jsp'");
		  fw_menu_1_1.hideOnMouseOut=true;

		  window.fw_menu_1_2 = new Menu("02. A/R",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
		  fw_menu_1_2.addMenuItem("01. A/R Payment", "location='<%=approot%>/store/sale/report/src_reportsalepaymentcredit.jsp'");
		  fw_menu_1_2.addMenuItem("02. A/R Summary", "location='<%=approot%>/store/sale/report/src_reportar.jsp'");
		  fw_menu_1_2.hideOnMouseOut=true;
	  
	  window.fw_menu_1 = new Menu("root",170,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  //fw_menu_1.addMenuItem("01. Internal Invoice","location='<%=approot%>/store/sale/src_reportsale.jsp'");
	  //fw_menu_1.addMenuItem("02. Sales cancelation","location='<%=approot%>/store/sale/cancel/src_invoice.jsp'");
	  fw_menu_1.addMenuItem(fw_menu_1_1);
	  fw_menu_1.addMenuItem(fw_menu_1_2);
	  fw_menu_1.childMenuIcon = "<%=approot%>/images/arrows.gif";
	  fw_menu_1.hideOnMouseOut = true;
	  /** END MENU SALES / PENJUALAN */
	  

      /** START MENU PURCHASING / PEMBELIAN */
	  	  window.fw_menu_2_1 = new Menu("03. A/P",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  	  fw_menu_2_1.addMenuItem("01. Summary", "location='<%=approot%>/arap/payable/payable_search.jsp'");
		  fw_menu_2_1.addMenuItem("01. A/P Detail", "location='<%=approot%>/arap/payable/ap_summary_search.jsp'");
		  fw_menu_2_1.addMenuItem("02. A/P Aging", "location='<%=approot%>/arap/payable/#'");
		  //fw_menu_2_1.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_2_1.hideOnMouseOut=true;
	  
	  window.fw_menu_2 = new Menu("root",170,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  fw_menu_2.addMenuItem("01. Purchase Order", "location='<%=approot%>/purchasing/material/pom/srcpomaterial.jsp'");
	  fw_menu_2.addMenuItem("02. Minimum Stok", "location='<%=approot%>/warehouse/material/report/src_reportstockmin.jsp'");
	  fw_menu_2.addMenuItem(fw_menu_2_1);
	  fw_menu_2.childMenuIcon="<%=approot%>/images/arrows.gif";
	  fw_menu_2.hideOnMouseOut=true;
	  /** END MENU PURCHASING / PEMBELIAN */
	  
	  
      /** START MENU RECEIVING / PENERIMAAN */
				window.fw_menu_3_1_0 = new Menu("02. Report",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
				fw_menu_3_1_0.addMenuItem("01. Global", "location='<%=approot%>/warehouse/material/report/src_reportreceive_all.jsp'");
				fw_menu_3_1_0.addMenuItem("02. By Receipt", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinvoice.jsp'");
				fw_menu_3_1_0.addMenuItem("03. By Supplier", "location='<%=approot%>/warehouse/material/report/src_reportreceive.jsp'");
				fw_menu_3_1_0.addMenuItem("04. By Category", "location='<%=approot%>/warehouse/material/report/src_reportreceivekategori.jsp'");
				fw_menu_3_1_0.hideOnMouseOut=true;
				
			window.fw_menu_3_1 = new Menu("01. From Purchase",170,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			fw_menu_3_1.addMenuItem("01. Add/Update", "location='<%=approot%>/warehouse/material/receive/src_receive_material.jsp'");
			fw_menu_3_1.addMenuItem(fw_menu_3_1_0);
		  	fw_menu_3_1.childMenuIcon="<%=approot%>/images/arrows.gif";
			fw_menu_3_1.hideOnMouseOut=true;
			
				window.fw_menu_3_2_0 = new Menu("02. Report",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
				fw_menu_3_2_0.addMenuItem("01. Global", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinternal.jsp'");
				fw_menu_3_2_0.addMenuItem("02. By Receipt", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinternalinvoice.jsp'");
				fw_menu_3_2_0.addMenuItem("03. By Category", "location='<%=approot%>/warehouse/material/report/src_reportreceiveinternalkategori.jsp'"); // src_reportreceiverekaptanggalkategoriinternal
				fw_menu_3_2_0.childMenuIcon="<%=approot%>/images/arrows.gif";
				fw_menu_3_2_0.hideOnMouseOut=true;	 
				
			window.fw_menu_3_2 = new Menu("02. From Store/Warehouse",170,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			fw_menu_3_2.addMenuItem("01. Add/Update", "location='<%=approot%>/warehouse/material/receive/srcreceive_store_wh_material.jsp'");
			fw_menu_3_2.addMenuItem(fw_menu_3_2_0);
			fw_menu_3_2.childMenuIcon="<%=approot%>/images/arrows.gif";
			fw_menu_3_2.hideOnMouseOut=true;				  	  
	  
	  window.fw_menu_3 = new Menu("root",170,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  fw_menu_3.addMenuItem(fw_menu_3_1);
	  fw_menu_3.addMenuItem(fw_menu_3_2);
	  fw_menu_3.childMenuIcon="<%=approot%>/images/arrows.gif";
	  fw_menu_3.hideOnMouseOut=true;
	  /** END MENU RECEIVING / PENERIMAAN */
	  
	  
      /** START MENU RETURN / PENGEMBALIAN */
			  //sub menu report return
			  window.fw_menu_4_1 = new Menu("02. Report",145,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			  fw_menu_4_1.addMenuItem("01. By Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreturninvoice.jsp'");
			  fw_menu_4_1.addMenuItem("02. By Supplier", "location='<%=approot%>/warehouse/material/report/src_reportreturn.jsp'");
			  //fw_menu_4_1.addMenuItem("03. Summary By Invoice", "location='<%=approot%>/warehouse/material/report/src_reportreturninvoicerekap.jsp'");
			  fw_menu_4_1.hideOnMouseOut=true;
		  
	  window.fw_menu_4 = new Menu("root",170,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  fw_menu_4.addMenuItem("01. Add/Update", "location='<%=approot%>/warehouse/material/return/src_return_material.jsp'");
	  fw_menu_4.addMenuItem(fw_menu_4_1);
	  fw_menu_4.childMenuIcon="<%=approot%>/images/arrows.gif";
	  fw_menu_4.hideOnMouseOut=true;
	  /** END MENU RETURN/PENGEMBALIAN */


	  /** START MENU TRANSFER BARANG */
			window.fw_menu_5_0 = new Menu("02. Report",140,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			fw_menu_5_0.addMenuItem("01. All", "location='<%=approot%>/warehouse/material/report/src_reportdispatch.jsp'");
			fw_menu_5_0.addMenuItem("02. By Invoice", "location='<%=approot%>/warehouse/material/report/src_reportdispatchinvoice.jsp'");
			fw_menu_5_0.addMenuItem("03. By Supplier", "location='<%=approot%>/warehouse/material/report/src_reportdispatchsupplier.jsp'");
			fw_menu_5_0.addMenuItem("04. By Category", "location='<%=approot%>/warehouse/material/report/src_reportdispatchkategori.jsp'");
			fw_menu_5_0.childMenuIcon="<%=approot%>/images/arrows.gif";
			fw_menu_5_0.hideOnMouseOut=true;
	  
	  window.fw_menu_5 = new Menu("root",170,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  fw_menu_5.addMenuItem("01. Add/Update", "location='<%=approot%>/warehouse/material/dispatch/srcdf_stock_wh_material.jsp'");
	  fw_menu_5.addMenuItem(fw_menu_5_0);
          fw_menu_5.addMenuItem("04. Transfer Unit", "location='<%=approot%>/warehouse/material/dispatch/srcdf_unit_wh_material.jsp'");
	  fw_menu_5.childMenuIcon="<%=approot%>/images/arrows.gif";
	  fw_menu_5.hideOnMouseOut=true;
     /** END MENU TRANSFER BARANG */

     /** START MENU Pengiriman BARANG */
                        window.fw_menu_10_0 = new Menu("02. Report",140,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			fw_menu_10_0.addMenuItem("01. All", "location='<%=approot%>/warehouse/material/report/src_reportdispatch.jsp'");
			fw_menu_10_0.addMenuItem("02. By Invoice", "location='<%=approot%>/warehouse/material/report/src_reportdispatchinvoice.jsp'");
			fw_menu_10_0.addMenuItem("03. By Supplier", "location='<%=approot%>/warehouse/material/report/src_reportdispatchsupplier.jsp'");
			fw_menu_10_0.addMenuItem("04. By Category", "location='<%=approot%>/warehouse/material/report/src_reportdispatchkategori.jsp'");
			fw_menu_10_0.childMenuIcon="<%=approot%>/images/arrows.gif";
			fw_menu_10_0.hideOnMouseOut=true;

                        window.fw_menu_10 = new Menu("root",170,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
                        fw_menu_10.addMenuItem("01.Add/Update", "location='<%=approot%>/warehouse/material/dispatch/src_delivery_order_sales.jsp'");
                        //fw_menu_10.addMenuItem(fw_menu_10_0); jangan di buat dlu
                        fw_menu_10.childMenuIcon="<%=approot%>/images/arrows.gif";
                        fw_menu_10.hideOnMouseOut=true;
     /** END MENU TRANSFER BARANG */
	 
     /** START MENU COSTING */
			window.fw_menu_6_0 = new Menu("02. Report",140,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
			fw_menu_6_0.addMenuItem("01. Global", "location='<%=approot%>/warehouse/material/report/src_reportcosting.jsp'");
			fw_menu_6_0.addMenuItem("02. By Category", "location='<%=approot%>/warehouse/material/report/src_reportcostingkategori.jsp'");
			//fw_menu_6_0.addMenuItem("03. By Supplier", "location='<%=approot%>/warehouse/material/report/src_reportdispatchkategori.jsp'");
			//fw_menu_6_0.addMenuItem("04. By Kategori", "location='<%=approot%>/warehouse/material/report/src_reportdispatchsupplier.jsp'");
			fw_menu_6_0.childMenuIcon="<%=approot%>/images/arrows.gif";
			fw_menu_6_0.hideOnMouseOut=true;
			
                     window.fw_menu_6 = new Menu("root",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
                     fw_menu_6.addMenuItem("01. Add/Update", "location='<%=approot%>/warehouse/material/dispatch/srccosting_material.jsp'");
                     fw_menu_6.addMenuItem(fw_menu_6_0);
                     fw_menu_6.childMenuIcon="<%=approot%>/images/arrows.gif";
                     fw_menu_6.hideOnMouseOut=true;
     /** END MENU COSTING */
	 
	 
	 /** START MENU MANAJEMEN STOK */
			window.fw_menu_7_0 = new Menu("04. Report",175,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
                        fw_menu_7_0.addMenuItem("01. Global", "location='<%=approot%>/warehouse/material/report/src_reportstock.jsp'");
                        fw_menu_7_0.addMenuItem("02. By Category", "location='<%=approot%>/warehouse/material/report/src_reportstockkategori.jsp?tipe=1'");
                        fw_menu_7_0.addMenuItem("03. By Supplier", "location='<%=approot%>/warehouse/material/report/src_reportstocksupplier.jsp'");
			fw_menu_7_0.addMenuItem("04. Stock Card", "location='<%=approot%>/warehouse/material/report/src_stockcard.jsp?type=1'");
                        fw_menu_7_0.addMenuItem("05. Stock Position", "location='<%=approot%>/warehouse/material/report/src_reportposisistock.jsp?type=1'");
                        fw_menu_7_0.addMenuItem("06. List Lost Correction Stock", "location='<%=approot%>/warehouse/material/report/src_reportselisihkoreksistok.jsp?type=1'");
                        //fw_menu_7_0.addMenuItem("06. Stock Position By Kategori", "location='<%=approot%>/warehouse/material/report/src_reportposisistockkategori.jsp?type=1'");
                        //fw_menu_7_0.addMenuItem("07. Stock Position By Supplier", "location='<%=approot%>/warehouse/material/report/src_reportposisistocksupplier.jsp?type=1'");
			fw_menu_7_0.hideOnMouseOut=true;
	 
	 window.fw_menu_7 = new Menu("root",175,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	 fw_menu_7.addMenuItem("01. Opname", "location='<%=approot%>/warehouse/material/stock/mat_opname_src.jsp'");
	 //fw_menu_7.addMenuItem("02. Quick Opname", "location='<%=approot%>/warehouse/material/stock/mat_opname_store_quick_src.jsp'");
	 fw_menu_7.addMenuItem("02. Stock Correction", "location='<%=approot%>/warehouse/material/stock/mat_stock_correction_src.jsp'");	 
	 fw_menu_7.addMenuItem(fw_menu_7_0);
	 //fw_menu_7.addMenuItem("05. Posting Stock", "location='<%=approot%>/master/posting/posting_stock.jsp'");
         fw_menu_7.addMenuItem("05. Posting Stock", "location='<%=approot%>/master/posting/posting_stock_new.jsp'");
         fw_menu_7.addMenuItem("06. Re Posting Stock ", "location='<%=approot%>/master/posting/srcmaterial_reposting_stock.jsp'");
	 fw_menu_7.childMenuIcon="<%=approot%>/images/arrows.gif";
	 fw_menu_7.hideOnMouseOut=true;	 	 
	 /** END MENU MANAJEMEN STOK */
	 
	 
	  /** START MENU MASTER DATA */
	  // sub menu master data
	  window.fw_menu_8_1 = new Menu("21. Import Data",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
          fw_menu_8_1.addMenuItem("01. Items", "location='<%=approot%>/excel/upload/importcatalogsrc.jsp'");
          fw_menu_8_1.addMenuItem("02. Suppliers", "location='<%=approot%>/excel/upload/importsuppliersrc.jsp'");;
          fw_menu_8_1.addMenuItem("03. Members", "location='<%=approot%>/excel/upload/importmembersrc.jsp'");
          fw_menu_8_1.childMenuIcon="<%=approot%>/images/arrows.gif";
          fw_menu_8_1.hideOnMouseOut=true;
		  
	  window.fw_menu_8 = new Menu("root",150,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  fw_menu_8.addMenuItem("01. Items List", "location='<%=approot%>/master/material/srcmaterial.jsp'");
	  //fw_menu_8.addMenuItem("02. Picture's Items", "location='<%=approot%>/master/material/simple_main.jsp'");
	  fw_menu_8.addMenuItem("03. Supplier List", "location='<%=approot%>/master/contact/srccontactcompany.jsp'");
	  fw_menu_8.addMenuItem("04. Location List", "location='<%=approot%>/master/location/location.jsp'");
	  fw_menu_8.addMenuItem("", "");
	  fw_menu_8.addMenuItem("05. Member Type", "location='<%=approot%>/masterdata/membergroup.jsp'");
	  fw_menu_8.addMenuItem("06. Member List", "location='<%=approot%>/masterdata/srcmemberreg.jsp'");
	  fw_menu_8.addMenuItem("07. Member Points", "location='<%=approot%>/masterdata/srcmemberreg_point.jsp'");
	  fw_menu_8.addMenuItem("", "");	  	  
	  fw_menu_8.addMenuItem("08. Stock Period", "location='<%=approot%>/master/periode/period.jsp'");
	  fw_menu_8.addMenuItem("09. Category", "location='<%=approot%>/master/material/matcategory.jsp'");
	  fw_menu_8.addMenuItem("10. Sub Category", "location='<%=approot%>/master/material/matmerk.jsp'");
	  fw_menu_8.addMenuItem("11. Unit List", "location='<%=approot%>/master/material/matunit.jsp'");
	  fw_menu_8.addMenuItem("", "");
	  fw_menu_8.addMenuItem("12. Cashier Master", "location='<%=approot%>/master/cashier/master_kasir.jsp'");
	  fw_menu_8.addMenuItem("13. Working Shift", "location='<%=approot%>/masterdata/shift.jsp'");
	  fw_menu_8.addMenuItem("14. Sales List", "location='<%=approot%>/masterdata/sales.jsp'");
	  fw_menu_8.addMenuItem("", "");
	  fw_menu_8.addMenuItem("15. Currency List", "location='<%=approot%>/masterdata/currencytype.jsp'");
	  fw_menu_8.addMenuItem("16. Daily Rate", "location='<%=approot%>/masterdata/dailyrate_list.jsp'");
	  fw_menu_8.addMenuItem("17. Standart Rate", "location='<%=approot%>/masterdata/standartrate.jsp'");
	  fw_menu_8.addMenuItem("18. Price Type", "location='<%=approot%>/masterdata/pricetype.jsp'");
  	  fw_menu_8.addMenuItem("19. Discount Type", "location='<%=approot%>/masterdata/discounttype.jsp'");
  	  //fw_menu_8.addMenuItem("20. Group Code", "location='<%=approot%>/master/material/coderange.jsp'");
	  fw_menu_8.addMenuItem("21. Payment System", "location='<%=approot%>/master/payment/pay_system.jsp'");
          fw_menu_8.addMenuItem("22. Costing Type ", "location='<%=approot%>/master/material/matcosting.jsp'");
          //adding menu restoran by mirahu 20120517
           fw_menu_8.addMenuItem("23. Room List", "location='<%=approot%>/master/location/room.jsp'");
           fw_menu_8.addMenuItem("24. Table List", "location='<%=approot%>/master/location/tableroom.jsp'");
          fw_menu_8.addMenuItem(fw_menu_8_1);
  	  fw_menu_8.childMenuIcon="<%=approot%>/images/arrows.gif";
	  fw_menu_8.hideOnMouseOut=true;
	  /** END MENU MASTER DATA */

	  /** START MENU SYSTEM */
		  window.fw_menu_4_1 = new Menu("04. Data Exchange",120,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
	  	  fw_menu_4_1.addMenuItem("01. Export Data", "location='<%=approot%>/transferdata/transferalltoclient.jsp'");
		  fw_menu_4_1.addMenuItem("02. Import Data", "location='<%=approot%>/transferdata/restoreallinclient.jsp'");
		  fw_menu_4_1.childMenuIcon="<%=approot%>/images/arrows.gif";
		  fw_menu_4_1.hideOnMouseOut=true;	  
		  
		  
	  window.fw_menu_9 = new Menu("root",140,17,"Verdana, Arial, Helvetica, sans-serif",10,"#000000","#ffffff","#ffffff","#000084");
          fw_menu_9.addMenuItem("01. Users", "location='<%=approot%>/system/userlist.jsp'");
          fw_menu_9.addMenuItem("02. Group", "location='<%=approot%>/system/grouplist.jsp'");
          fw_menu_9.addMenuItem("03. Privilege", "location='<%=approot%>/system/privilegelist.jsp'");
	  //fw_menu_9.addMenuItem("04. Data Backup", "location='<%=approot%>/service/service_center.jsp'");
	  fw_menu_9.addMenuItem(fw_menu_4_1);
	  fw_menu_9.addMenuItem("05. System Setting ", "location='<%=approot%>/system/sysprop.jsp'");
	  fw_menu_9.addMenuItem("06. Close Period", "location='<%=approot%>/master/closing/closing_monthly.jsp'");
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
      <div align="center"> <A id="divMenu0" href="#" onclick="javascript:cordYMenu0(document.all.M0.value);" onmouseout=FW_startTimeout(); class="menus">Sales</A> 
        | <A id="divMenu1" href="#" onclick="javascript:cordYMenu1(document.all.M1.value);" onmouseout=FW_startTimeout(); class="menus">Purchase</A> 
        | <A id="divMenu2" href="#" onclick="javascript:cordYMenu2(document.all.M2.value);" onmouseout=FW_startTimeout(); class="menus">Receive</A> 
        | <A id="divMenu3" href="#" onclick="javascript:cordYMenu3(document.all.M3.value);" onmouseout=FW_startTimeout(); class="menus">Return</A> 
        | <A id="divMenu4" href="#" onclick="javascript:cordYMenu4(document.all.M4.value);" onmouseout=FW_startTimeout(); class="menus">Dispatch</A>
        | <A id="divMenu10" href="#" onclick="javascript:cordYMenu10(document.all.M10.value);" onmouseout=FW_startTimeout(); class="menus">Deliver Order</A>
        | <A id="divMenu5" href="#" onclick="javascript:cordYMenu5(document.all.M5.value);" onmouseout=FW_startTimeout(); class="menus">Costing</A> 
        | <A id="divMenu6" href="#" onclick="javascript:cordYMenu6(document.all.M6.value);" onmouseout=FW_startTimeout(); class="menus">Stock</A> 
        | <A id="divMenu7" href="#" onclick="javascript:cordYMenu7(document.all.M7.value);" onmouseout=FW_startTimeout(); class="menus">Masterdata</A> 
        | <A id="divMenu8" href="#" onclick="javascript:cordYMenu8(document.all.M8.value);" onmouseout=FW_startTimeout(); class="menus">System</A> 
        | <A href="<%=approot%>/logout.jsp" class="menus">Logout</A> </div>
    </td>
  </tr>
  <tr>
    <td height="1" nowrap bgcolor="#A73701"><img src="home_files/spacer.gif" width=1 height=1></td>
  </tr>
</table>
