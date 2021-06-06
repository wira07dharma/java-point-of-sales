<%@ page language="java" %>
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_ADMIN, AppObjInfo.G2_ADMIN_SYSTEM, AppObjInfo.OBJ_ADMIN_SYSTEM_APP_SET); %>
<%@ include file = "../main/checkuser.jsp" %>

<!-- JSP Block -->

<!-- End of JSP Block -->  

<html>
<!-- DW6 -->
<head>
<title>Dimata - ProChain POS</title>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<link rel="stylesheet" href="../styles/main.css" type="text/css"> 
<link rel="stylesheet" href="../styles/tab.css" type="text/css"> 


<SCRIPT language=JavaScript>
function hideObjectForMarketing(){    
} 
	 
function hideObjectForWarehouse(){ 
}
	
function hideObjectForProduction(){
}
	
function hideObjectForPurchasing(){
}

function hideObjectForAccounting(){
}

function hideObjectForHRD(){
}

function hideObjectForGallery(){
}

function hideObjectForMasterData(){
}

</SCRIPT>
 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td width="88%" valign="top" align="center"> 
      <table width="99%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader">Daftar Setting Aplikasi yang harus di install </td>
        </tr>
        <tr> 
          <td>&nbsp;            
            <TABLE class=listgen cellSpacing=1 width="100%" border=0>
              <TBODY> 
              <TR> 
                <TD width="20%" class=listgentitle>Nama Kostanta</TD>
                <TD width="25%" class=listgentitle><strong>Nilai Kostanta</strong></TD>
                <TD width="55%" class=listgentitle><strong>Keterangan</strong></TD>
              </TR>
              <TR vAlign=top> 
                <TD class=listgensell>DATABASE_HOME</TD>
                <TD class=listgensell>D:/mysql/data/posanugerah</TD>
                <TD class=listgensell>Lokasi database </TD>
              </TR>
              <TR vAlign=top> 
                <TD class=listgensell>PATH_DATA_IN</TD>
                <TD class=listgensell>D;/transferin/</TD>
                <TD class=listgensell>Lokasi data yang akan di restore </TD>
              </TR>
              <TR vAlign=top> 
                <TD class=listgensell>PATH_DATA_OUT</TD>
                <TD class=listgensell>D:/transferout/</TD>
                <TD class=listgensell>Lokasi data yang akan di transfer </TD>
              </TR>
              <TR vAlign=top> 
                <TD class=listgensell>PATH_MYSQL_BIN</TD>
                <TD class=listgensell>D:/mysql/bin/</TD>
                <TD class=listgensell>Lokasi folder bin di database </TD>
              </TR>
              <TR vAlign=top> 
                <TD class=listgensell>DESIGN_MATERIAL_FOR</TD>
                <TD class=listgensell>0</TD>
                <TD class=listgensell>0 = Untuk material sendiri<br>
                  1 = Untuk Hanoman </TD>
              </TR>
			  <TR vAlign=top> 
                <TD class=listgensell>DISC_RETAIL</TD>
                <TD class=listgensell>OID dari diskon untuk tipe RETAIL</TD>
                <TD class=listgensell>Property ini digunakan pada process update catalog</TD>
              </TR>
			  <TR vAlign=top> 
                <TD class=listgensell>DISC_VIP</TD>
                <TD class=listgensell>OID dari diskon untuk tipe VIP</TD>
                <TD class=listgensell>Property ini digunakan pada process update catalog</TD>
              </TR>
			  <TR vAlign=top> 
                <TD class=listgensell>DISC_WHOLESALE</TD>
                <TD class=listgensell>OID dari diskon untuk tipe WHOLESALE</TD>
                <TD class=listgensell>Property ini digunakan pada process update catalog</TD>
              </TR>
			  <TR vAlign=top> 
                <TD class=listgensell>PRICE_TYPE_RETAIL</TD>
                <TD class=listgensell>OID dari price type untuk tipe RETAIL</TD>
                <TD class=listgensell>Property ini digunakan pada process update catalog</TD>
              </TR>
			  <TR vAlign=top> 
                <TD class=listgensell>PRICE_TYPE_VIP</TD>
                <TD class=listgensell>OID dari price type untuk tipe VIP</TD>
                <TD class=listgensell>Property ini digunakan pada process update catalog</TD>
              </TR>
			  <TR vAlign=top> 
                <TD class=listgensell>PRICE_TYPE_WHOLESALE</TD>
                <TD class=listgensell>OID dari price type untuk tipe WHOLESALE</TD>
                <TD class=listgensell>Property ini digunakan pada process update catalog</TD>
              </TR>
			  <TR vAlign=top> 
                <TD class=listgensell>SHIPPING_INFORMATION_LEFT</TD>
                <TD class=listgensell>Informasi Shipping yang digunakan. Posisi LEFT</TD>
                <TD class=listgensell>Property ini digunakan pada print out PO</TD>
              </TR>
			  <TR vAlign=top> 
                <TD class=listgensell>SHIPPING_INFORMATION_RIGHT</TD>
                <TD class=listgensell>Informasi Shipping yang digunakan. Posisi RIGHT</TD>
                <TD class=listgensell>Property ini digunakan pada print out PO</TD>
              </TR>
			  <TR vAlign=top> 
                <TD class=listgensell>DEF_CAS_CASH_MASTER</TD>
                <TD class=listgensell>OID dari Master Kasir</TD>
                <TD class=listgensell>Property ini berhubungan dengan ...</TD>
              </TR>
			  <TR vAlign=top> 
                <TD class=listgensell>DEF_CAS_LOCATION</TD>
                <TD class=listgensell>OID dari Lokasi Kasir</TD>
                <TD class=listgensell>Property ini berhubungan dengan lokasi transaksi kasir</TD>
              </TR>
			  <TR vAlign=top> 
                <TD class=listgensell>DEF_CAS_NONMEMBER_OID</TD>
                <TD class=listgensell>OID dari member yang difungsikan sebagai member umum</TD>
                <TD class=listgensell>Property ini digunakan untuk menetukkan member mana yang digunakan sebagai member umum</TD>
              </TR>
			  <TR vAlign=top> 
                <TD class=listgensell>PRICE_TRANSACTION_USED_HPP</TD>
                <TD class=listgensell>0 atau 1</TD>
                <TD class=listgensell>Property ini digunakan untuk menetukkan sumber dari nilai item, pada modul Transfer dan Return Supplier.
                <br>0 jika nilai item diambil dari harga HPP pada masterdata
                <br>1 jika nilai item diambil dari harga Jual pada masterdata
                </TD>
              </TR>
              </TBODY> 
            </TABLE>
          </td> 
        </tr> 
      </table>
    </td>
  </tr>
</table>
</body> 
</html>
