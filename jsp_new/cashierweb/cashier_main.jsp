<%@ page language="java" %>
<%@ include file = "../main/javainit.jsp" %>
<!-- JSP Block -->

<!-- End of JSP Block -->  

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->  
<title>Dimata ProChain - </title>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" --> 
<link rel="stylesheet" href="../styles/main.css" type="text/css"> 
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../styles/tab.css" type="text/css"> 
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
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

<!-- Js untuk PopUp -->
        <script type="text/javascript" src="jquery-1.6.1.min.js"></script>
        <script type="text/javascript">

            // $(document).ready(function(){
            function popUp(){

                //open popup
                $("#overlay_form").fadeIn(1000);
                positionPopup();

            }

            //position the popup at the center of the page
            function positionPopup(){
                if(!$("#overlay_form").is(':visible')){
                    return;
                }
                $("#overlay_form").css({
                    left: ($(window).width() - $('#overlay_form').width()) / 2,
                    top: ($(window).width() - $('#overlay_form').width()) / 2.5,
                    position:'absolute'
                });
            }

            //maintain the popup at center of the page when browser resized
            $(window).bind('resize',positionPopup);

        </script>

        <style>
            #overlay_form{
                position: absolute;
                border: 5px solid gray;
                padding: 10px;
                background: white;
                width: auto;
                height: auto;
            }
            #pop{
                display:block;
                border: 1px solid gray;
                width:auto;
                text-align: center;
                padding: 6px;
                border-radius: 5px;
                text-decoration: none;
                margin:auto;
            }
        </style>

<style type="text/css">
<!--
.style1 {
	color: #009900;
	font-weight: bold;
}
.style3 {font-size: 24px}
-->
</style>
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" --> 
      <%@ include file = "../main/header.jsp" %>
      <!-- #EndEditable --></td> 
  </tr> 
  <tr> 
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td> 
  </tr>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Cashier by Ayu Wandira ( Sales Admin ) <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <table width="100%" cellspacing="1" cellpadding="1">
              <tr>
                <td><fieldset><table align="center" width="95%" cellspacing="1" cellpadding="1">
                  <tr>
                    <td width="9%">Type</td>
                    <td width="18%"><label>
                      <select name="select">
                        <option value="0" selected>Sales Order</option>
                        <option value="1">Sales Return</option>
                        <option value="2">Gift ( Hadiah )</option>
                      </select>
                    </label></td>
					<td></td>
                    <td width="7%">Number</td>
                    <td width="33%" nowrap>: 20130404.001</td>
					<td></td>
                    <td width="14%">Date</td>
                    <td width="19%">4 April 2013 10:15 </td>
                  </tr>
                  <tr>
                    <td>Quest Type </td>
                    <td><label>
                      <select name="select13">
                        <option value="0">Public</option>
                        <option value="1">Member</option>
                      </select>
                    </label></td>
					<td></td>
                    <td>Customer</td>
                    <td><label>
                      <input name="textfield" type="text" value="DSJ" size="5" maxlength="5">
                      <input name="textfield2" type="text" value="PT. Dimata S.J" size="15" maxlength="64">
                      <input type="button" name="Submit2" value="List">
                    </label></td>
					<td></td>
                    <td>Person</td>
                    <td><input name="textfield23" type="text" value="Dian" size="15" maxlength="64"></td>
                    </tr>
                  <tr>
                    <td>Payment</td>
                    <td nowrap><label>
                      <select name="select2">
                        <option value="0">-none-</option>
                        <option value="1">Cash</option>
                        <option value="2" selected>Credit</option>
                      </select>
                    </label></td>
					<td></td>
                    <td>&nbsp;</td>
                    <td valign="top"><p>Jl. Imam Bonjol, Cipta Selaras no.12, Denpasar <br>
                    Tel.(361) 499029 Fax.(0361) 482431 <br>
                    marketing@dimata.com</p>                      </td>
					<td></td>
                    <td valign="top" nowrap><p>Credit Limit <br>
                      Outstanding<br>
                    Available</p>                      </td>
                    <td valign="top"><div align="left">Rp. 10.000.000,- <br>
                      Rp. 4.000.000,-<br>
                      <span class="style1">Rp. 6.000.000,-</span></div></td>
                    </tr>
                  <tr>
                    <td >Bookkeeping Currency </td>
                    <td >Rp. at 9600/USD</td>
					<td></td>
                    <td >Delivery Address </td>
                    <td ><label>
                      <textarea name="textfield3" cols="32" rows="2"></textarea>
                    </label></td>
					<td></td>
                    <td >Tel/Hp/Fax</td>
                    <td ><label>
                      <input name="textfield5" type="text" size="15">
                      /
  <input name="textfield52" type="text" size="15">
                      /                    </label></td>
                    </tr>
                  <tr>
                    <td >Sales Currency </td>
                    <td ><label>
                      <select name="select6">
                        <option selected>Rp.</option>
                        <option>USD</option>
                        </select>
                      at 9650/USD </label></td>
					  <td></td>
                    <td >&nbsp;</td>
                    <td nowrap >City
                      <label>
                        <select name="select3">
                          <option selected>Denpasar</option>
                          <option>Gianyar</option>
                        </select>
                        ,
                        <select name="select5">
                        <option selected>Bali</option>
                        <option>NTB</option>
                      </select>
                      ,
                      
                      <select name="select4">
                        <option>Indonesia</option>
                        <option>Papua</option>
                      </select>
                      </label></td>
					  <td></td>
                    <td >Zip</td>
                    <td ><input name="textfield4" type="text" size="10" maxlength="10"></td>
                    </tr>
                  <tr>
                    <td bgcolor="#FFFF66"><span class="style3 style3">Total </span></td>
                    <td nowrap bgcolor="#FFFF66"><span class="style3 style3">Rp. 7.920.000,-</span></td>
					<td></td>
                    <td bgcolor="#FFFF66"><span class="style3 style3">Paid</span></td>
                    <td bgcolor="#FFFF66"><span class="style3 style3">Rp. 0,- </span></td>
					<td></td>
                    <td bgcolor="#FFFF66"><span class="style3 style3">Outstanding</span></td>
                    <td nowrap bgcolor="#FFFF66"><span class="style3 style3">Rp. 7.920.000,- </span></td>
                    </tr>
                </table></fieldset></td>
              </tr>
              <tr>
                   <td></td>
              </tr>
              <tr>
                <td><table width="100%" cellspacing="1" cellpadding="1">
                  <tr>
                    <td width="2%" class="listgentitle">No.</td>
                    <td width="10%" class="listgentitle">SKU/Barcode</td>
                    <td width="24%" class="listgentitle">Item Name</td>
                    <td width="8%" class="listgentitle">Quantity</td>
                    <td width="5%" class="listgentitle">Price</td>
                    <td width="4%" class="listgentitle">Disc</td>
                    <td width="8%" class="listgentitle">1.Disc% </td>
                    <td width="7%" class="listgentitle">2.Disc%</td>
                    <td width="5%" class="listgentitle">Disc.</td>
                    <td width="9%" class="listgentitle">Disc.Total</td>
                    <td width="8%" class="listgentitle">Net Price </td>
                    <td width="5%" class="listgentitle">Note</td>
                    </tr>
                  <tr>
                    <td>&gt;</td>
                    <td><label>
                      <input name="textfield7" type="text"  size="15" maxlength="20" onFocus="popUp(this)">
                    </label></td>
                    <td><input name="textfield72" type="text"  size="30"></td>
                    <td width="8%" height="7"><label>
                      <input name="textfield8" type="text" size="8" maxlength="9">
                    </label></td>
                    <td><input name="textfield613" type="text" size="16"></td>
                    <td><label>
                      <input type="checkbox" name="checkbox" value="checkbox">
                    </label></td>
                    <td><input name="textfield82" type="text" size="3" maxlength="3"></td>
                    <td><input name="textfield822" type="text" size="3" maxlength="3"></td>
                    <td><input name="textfield61332" type="text" size="8"></td>
                    <td><input name="textfield6133" type="text" size="8"></td>
                    <td><input name="textfield6134" type="text" size="16"></td>
                    <td><input name="textfield6135" type="text" size="16"></td>
                    </tr>
                  <tr>
                    <td>&nbsp;</td>
                    <td colspan="11"><table width="100%" cellspacing="1" cellpadding="1">
                      <tr>
                        <td width="2%" class="listgentitle">No.</td>
                        <td width="15%" class="listgentitle">Ser.Number </td>
                        <td width="19%" class="listgentitle">Note</td>
                        <td width="11%" class="listgentitle">Status</td>
                        <td width="2%"><label>
                          <input type="image" name="imageField" src="../images/spacer.gif" width="10">
                        </label></td>
                        <td width="3%" class="listgentitle">No.</td>
                        <td width="11%" class="listgentitle">Ser.Number </td>
                        <td width="26%" class="listgentitle">Note</td>
                        <td width="11%" class="listgentitle">Status</td>
                      </tr>
                      <tr>
                        <td>1</td>
                        <td><label>
                          <input name="textfield6" type="text" size="16">
                        </label></td>
                        <td><input name="textfield67" type="text" size="32"></td>
                        <td><select name="select8">
                          <option selected>Open</option>
                          <option>Reserved</option>
                          <option>On The Way</option>
                          <option>Delivered</option>
                        </select></td>
                        <td>&nbsp;</td>
                        <td>4</td>
                        <td><input name="textfield64" type="text" size="16"></td>
                        <td><input name="textfield610" type="text" size="32"></td>
                        <td><label>
                          <select name="select7">
                            <option selected>Open</option>
                            <option>Reserved</option>
                            <option>On The Way</option>
                            <option>Delivered</option>
                          </select>
                        </label></td>
                      </tr>
                      <tr>
                        <td>2</td>
                        <td><input name="textfield62" type="text" size="16"></td>
                        <td><input name="textfield68" type="text" size="32"></td>
                        <td><select name="select11">
                          <option selected>Open</option>
                          <option>Reserved</option>
                          <option>On The Way</option>
                          <option>Delivered</option>
                        </select></td>
                        <td>&nbsp;</td>
                        <td>5</td>
                        <td><input name="textfield65" type="text" size="16"></td>
                        <td><input name="textfield611" type="text" size="32"></td>
                        <td><select name="select9">
                          <option selected>Open</option>
                          <option>Reserved</option>
                          <option>On The Way</option>
                          <option>Delivered</option>
                        </select></td>
                      </tr>
                      <tr>
                        <td>3</td>
                        <td><input name="textfield63" type="text" size="16"></td>
                        <td><input name="textfield69" type="text" size="32"></td>
                        <td><select name="select12">
                          <option selected>Open</option>
                          <option>Reserved</option>
                          <option>On The Way</option>
                          <option>Delivered</option>
                        </select></td>
                        <td>&nbsp;</td>
                        <td>6</td>
                        <td><input name="textfield66" type="text" size="16"></td>
                        <td><input name="textfield612" type="text" size="32"></td>
                        <td><select name="select10">
                          <option selected>Open</option>
                          <option>Reserved</option>
                          <option>On The Way</option>
                          <option>Delivered</option>
                        </select></td>
                      </tr>
                    </table>
                      <br></td>
                    </tr>
                  
                </table></td>
              </tr>
              <tr>
                <td>Payment Schedule </td>
              </tr>
              <tr>
                <td><table width="100%" cellspacing="1" cellpadding="1">
                  <tr>
                    <td width="4%" class="listgentitle">No.</td>
                    <td width="34%" class="listgentitle">Due Date </td>
                    <td width="29%" class="listgentitle">Amount</td>
                    <td width="21%" class="listgentitle">Status</td>
                    <td width="12%"><label>
                      <input type="image" name="imageField2" src="../images/spacer.gif" width="10">
                    </label></td>
                    </tr>
                  <tr>
                    <td>1</td>
                    <td><label>
                      <input type="text" name="textfield9">
                    </label></td>
                    <td><input type="text" name="textfield92"></td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    </tr>
                  <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    </tr>
                </table></td>
              </tr>
              <tr>
                <td><label>
                  <input type="submit" name="Submit3" value="Save">
                  <input type="submit" name="Submit32" value="Delete">
                </label></td>
              </tr>
            </table>
            <form name="form1" method="post" action="">
            </form>
          <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
      <%@ include file = "../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
       <form id="overlay_form" style="display:none"  >
            <table width="1000"  border="0" cellspacing="0" cellpadding="0" >
                <tr >
                    <td><span class="style3 style3">NAMA BARANG</span></td>
                    <td><span class="style3 style3">HARGA</span></td>
                </tr>
            </table>
            <a href="" id="close" >Close</a>
        </form>
</body>
<!-- #EndTemplate --></html>
