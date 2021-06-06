<%-- 
    Document   : menu_horizontal_drop_down_menu_ina
    Created on : Sep 30, 2013, 1:36:55 PM
    Author     : dimata005
--%>

<!DOCTYPE html>
<html lang="en" class="no-js">
	<body>
		<div class="container">
			<div class="main">
				<nav id="cbp-hrmenu" class="cbp-hrmenu">
					<ul class="tgh">
						<li>
                                                    <a href="#"><center><img src="horizontaldropdownmenu/images/penjualan.png"><br>Penjualan</center></a>
							<div class="cbp-hrsub">
								<div class="cbp-hrsub-inner">
									<div>
										<h4>Laporan</h4>
										<ul>
                                                                                    <li><a href="<%=approot%>/store/sale/report/src_reportsale_global.jsp">01. Penjualan Global</a></li>
											<li><a href="<%=approot%>/store/sale/report/src_reportsale_detail_global.jsp">02. Penjualan Detail</a></li>
											<li><a href="<%=approot%>/store/sale/report/src_reportsale_global_margin.jsp">03. Gross Margin</a></li>
											<li><a href="<%=approot%>/store/sale/report/src_reportsalerekaptanggal.jsp">04. Rekap Harian</a></li>
											<li><a href="<%=approot%>/store/sale/report/src_reportpendingorder.jsp">05. Pending Order (DP)</a></li>
											<li><a href="<%=approot%>/store/sale/report/src_reportsale.jsp">06. Per Invoice</a></li>
                                                                                        <li><a href="<%=approot%>/store/sale/report/src_reportsale_detail.jsp">07. Invoice Detail</a></li>
                                                                                        <li><a href="#">08. Retur Per Invoice</a></li>
                                                                                        <li><a href="#">09. Rekapitulasi Report</a></li>
                                                                                        <li><a href="#">10. Rekap Per KSG</a></li>
                                                                                        <li><a href="#">11. Per Cash Opening </a></li>
										</ul>
									</div>
									<div>
										<h4>Piutang</h4>
										<ul>
											<li><a href="#">01. Pelunasan Piutang</a></li>
											<li><a href="#">02. Rekap Piutang</a></li>
										</ul>
									</div>
								</div><!-- /cbp-hrsub-inner -->
							</div><!-- /cbp-hrsub -->
						</li>
						<li>
							<a href="#"><center><img src="horizontaldropdownmenu/images/pembelian.png"><br>Pembelian</center></a>
							<div class="cbp-hrsub">
								<div class="cbp-hrsub-inner">
									<div>
                                                                            <a href="#"><h4>Order Pembelian</h4></a>
									</div>
									<div>
										<a href="#"><h4>Minimum Stok</h4></a>
									</div>
									<div>
										<h4>Hutang</h4>
										<ul>
											<li><a href="#">01. Rekap</a></li>
											<li><a href="#">02. Rekap Detail</a></li>
										</ul>
									</div>
								</div><!-- /cbp-hrsub-inner -->
							</div><!-- /cbp-hrsub -->
						</li>
						<li>
							<a href="#"><center><img src="horizontaldropdownmenu/images/penerimaan.png"><br>Penerimaan</center></a>
							<div class="cbp-hrsub">
								<div class="cbp-hrsub-inner">
									<div>
										<h4>Dari Pembelian</h4>
										<ul>
											<li><a href="#">01. Ubah/Tambah</a></li>
											<li><a href="#">02. Import(from file)</a></li>
										</ul>

                                                                                <h4>Dari Toko/Gudang</h4>
										<ul>
											<li><a href="#">01. Ubah/Tambah</a></li>
											<li><a href="#">02. Import(from file)</a></li>
										</ul>
										
									</div>
									<div>
										<h4>Laporan Dari Pembelian</h4>
										<ul>
                                                                                    <li><a href="#">01. Global</a></li>
                                                                                    <li><a href="#">02. Per Nota</a></li>
                                                                                    <li><a href="#">03. Per Supplier</a></li>
                                                                                    <li><a href="#">04. Per Kategori</a></li>
										</ul>
                                                                                <h4>Laporan Dari Toko/Gudang</h4>
										<ul>
                                                                                    <li><a href="#">01. Global</a></li>
                                                                                    <li><a href="#">02. Per Nota</a></li>
                                                                                    <li><a href="#">03. Per Supplier</a></li>
                                                                                    <li><a href="#">04. Per Kategori</a></li>
										</ul>
									</div>
								</div><!-- /cbp-hrsub-inner -->
							</div><!-- /cbp-hrsub -->
						</li>
						<li>
							<a href="#"><center><img src="horizontaldropdownmenu/images/retur.png"><br>Retur</center></a>
							<div class="cbp-hrsub">
								<div class="cbp-hrsub-inner">
									<div>
										<h4>Retur</h4>
										<ul>
											<li><a href="#">01. Ubah/Tambah</a></li>
											<li><a href="#">02. Laporan</a></li>
										</ul>
									</div>
                                                                        <div>
										<h4>Laporan</h4>
										<ul>
											<li><a href="#">01. Per Invoice</a></li>
											<li><a href="#">02. Per Supplie</a></li>
										</ul>
									</div>
								</div><!-- /cbp-hrsub-inner -->
							</div><!-- /cbp-hrsub -->
						</li>
						<li>
							<a href="#"><center><img src="horizontaldropdownmenu/images/transfer.png"><br>Transfer</center></a>
							<div class="cbp-hrsub">
								<div class="cbp-hrsub-inner">
									<div>
										<h4>Transfer</h4>
										<ul>
											<li><a href="#">01. Ubah/Tambah</a></li>
                                                                                        <li><a href="#">02. Import(from file)</a></li>
                                                                                        <li><a href="#">03. Transfer Unit</a></li>
										</ul>
									</div>
                                                                        <div>
										<h4>Laporan</h4>
										<ul>
											<li><a href="#">01. Semua</a></li>
                                                                                        <li><a href="#">02. Per Invoice</a></li>
											<li><a href="#">03. Per Supplier</a></li>
                                                                                        <li><a href="#">04. Per Kategori</a></li>
										</ul>
									</div>
								</div><!-- /cbp-hrsub-inner -->
							</div><!-- /cbp-hrsub -->
						</li>
                                                <li>
							<a href="#"><center><img src="horizontaldropdownmenu/images/stok.png"><br>Stok</center></a>
							<div class="cbp-hrsub">
								<div class="cbp-hrsub-inner">
									<div>
										<h4>Stok</h4>
										<ul>
											<li><a href="#">01. Opname</a></li>
											<li><a href="#">02. Opname(used file)</a></li>
											<li><a href="#">03. Koreksi Stok</a></li>
                                                                                        <li><a href="#">04. Posting Stok</a></li>
                                                                                        <li><a href="#">05. RePosting Stok</a></li>
										</ul>
									</div>
									<div>
										<h4>Laporan</h4>
										<ul>
											<li><a href="#">01. Global</a></li>
											<li><a href="#">02. Per Kategori</a></li>
                                                                                        <li><a href="#">03. Per Supplier</a></li>
											<li><a href="#">04. Kartu Stok</a></li>
											<li><a href="#">05. Posisi Stok</a></li>
											<li><a href="#">06. Selisih Koreksi Stok</a></li>
										</ul>
									</div>
								</div><!-- /cbp-hrsub-inner -->
							</div><!-- /cbp-hrsub -->
						</li>
                                                <li>
							<a href="#"><center><img src="horizontaldropdownmenu/images/masterdata.png"><br>Masterdata</center></a>
							<div class="cbp-hrsub">
								<div class="cbp-hrsub-inner">
									<div>
										<h4>Masterdata</h4>
										<ul>
											<li><a href="#">01. Daftar Barang</a></li>
											<li><a href="#">03. Daftar Supplier</a></li>
											<li><a href="#">04. Daftar Lokasi</a></li>
											<li><a href="#">05. Tipe Customer</a></li>
											<li><a href="#">06. Daftar Member</a></li>
											<li><a href="#">07. Poin Member</a></li>
                                                                                </ul>
									</div>
									<div>
										<h4>&nbsp;</h4>
										<ul>
											<li><a href="#">08. Daftar Periode</a></li>
											<li><a href="#">09. Kategori Barang</a></li>
                                                                                        <li><a href="#">10. <%=merkName%></a></li>
                                                                                        <li><a href="#">11. Daftar Satuan</a></li>
										</ul>
									</div>
									<div>
										<h4>&nbsp;</h4>
										<ul>
											<li><a href="#">12. Master Kasir</a></li>
											<li><a href="#">13. Shift Kerja</a></li>
											<li><a href="#">14. Daftar Sales</a></li>
											<li><a href="#">15. Jenis Mata Uang</a></li>
											<li><a href="#">16. Nilai Tukar Harian</a></li>
											<li><a href="#">17. Nilai Tukar Standar</a></li>
										</ul>
									</div>
                                                                       <div>
										<ul>
											<li><a href="#">18. Tipe Harga</a></li>
											<li><a href="#">19. Tipe Potongan</a></li>
											<li><a href="#">20. Kode Grup</a></li>
											<li><a href="#">21. Sistem Pembayaran</a></li>
											<li><a href="#">22. Tipe Biaya</a></li>
											<li><a href="#">23. Daftar Ruangan</a></li>
                                                                                        <li><a href="#">24. Daftar Meja</a></li>
										</ul>
									</div>
								</div><!-- /cbp-hrsub-inner -->
							</div><!-- /cbp-hrsub -->
						</li>
                                                <li>
							<a href="#"><center><img src="horizontaldropdownmenu/images/sistem.png"><br>Sistem</center></a>
							<div class="cbp-hrsub">
								<div class="cbp-hrsub-inner">
									<div>
										<h4>Sistem</h4>
										<ul>
											<li><a href="#">01. Pemakai</a></li>
											<li><a href="#">02. Grup</a></li>
											<li><a href="#">03. Hak Akses</a></li>
										</ul>
										<ul>
											<li><a href="#">05. Pengaturan Aplikasi</a></li>
											<li><a href="#">06. Tutup Periode</a></li>
											<li><a href="#">07. Internet connection</a></li>
                                                                                        <li><a href="#">08. Transfer Ke Outlet</a></li>
                                                                                        <li><a href="#">09. Transfer Penjualan</a></li>
										</ul>
									</div>
								</div><!-- /cbp-hrsub-inner -->
							</div><!-- /cbp-hrsub -->
						</li>
                                                <li>
							<a href="<%=approot%>/logout.jsp"><center><img src="horizontaldropdownmenu/images/keluar.png"><br>Keluar</center></a>
						</li>
					</ul>
				</nav>
			</div>
		</div>
		<script src="horizontaldropdownmenu/js/jquery.min.js"></script>
		<script src="horizontaldropdownmenu/js/cbpHorizontalMenu.min.js"></script>
		<script>
			$(function() {
				cbpHorizontalMenu.init();
			});
		</script>
	</body>
</html>
