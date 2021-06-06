<%@ page language="java" %>
<%@ include file = "../main/javainit.jsp" %>
<!-- JSP Block -->

<!-- End of JSP Block -->  

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->  
<title>Dimata ProChain - Open Shift </title>
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
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" --><!-- #EndEditable --></td> 
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Cashier - Close Shift <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
		  <table width="100%" cellspacing="1" cellpadding="1">
            <tr>
              <td>List of Opened Shift </td>
            </tr>
            <tr>
              <td><table width="100%" cellspacing="1" cellpadding="1">
                <tr>
                  <td width="3%" class="listgentitle">No.</td>
                  <td width="9%" class="listgentitle">User</td>
                  <td class="listgentitle">Supervisor</td>
                  <td class="listgentitle">Open at </td>
                  <td class="listgentitle">Location</td>
                  </tr>
                <tr>
                  <td>1</td>
                  <td>Luh Gitawati </td>
                  <td>Made Seken </td>
                  <td>4 April 2013 9:00 </td>
                  <td>Outlet Rimo 1 - Diponegoro </td>
                  </tr>
                <tr>
                  <td>2</td>
                  <td>Putu Sukasari </td>
                  <td>Gde Suryawan </td>
                  <td>4 April 2013 8:45</td>
                  <td>Outlet RTC Ubung </td>
                  </tr>
              </table></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>Close  Shift for user 
                <input type="text" name="textfield6"> 
                password 
                <input type="text" name="textfield7"></td>
            </tr>
            <tr>
              <td><table width="800" cellspacing="1" cellpadding="1">
                <tr>
                  <td width="6%" class="listgentitle">No.</td>
                  <td width="6%" class="listgentitle">Currency</td>
                  <td width="25%" class="listgentitle">Amount</td>
                  <td width="63%" class="listgentitle">Exch.Rate</td>
                  <td width="63%" class="listgentitle"> Subtotal </td>
                </tr>
                <tr>
                  <td>1</td>
                  <td><label>
                    <select name="select">
                      <option selected>Rp.</option>
                      <option>USD</option>
                                        </select>
                  </label></td>
                  <td><label>
                    <input name="textfield" type="text" value="7.920.000">
                  </label></td>
                  <td>1</td>
                  <td>7.920.000</td>
                </tr>
                <tr>
                  <td>2</td>
                  <td><label>
                    <select name="select">
                      <option>Rp.</option>
                      <option>USD</option>
                    </select>
                  </label></td>
                  <td><label>
                    <input type="text" name="textfield">
                  </label></td>
                  <td>9.650,-</td>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td><div align="right">Total</div></td>
                  <td> 7.920.000,-</td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td><div align="right">Opening</div></td>
                  <td>1.000.000,-</td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td><div align="right">Total</div></td>
                  <td>8.920.000,-</td>
                </tr>
              </table>
                <br>
                <p>Approve by supervisor : user name :
                  <input type="text" name="textfield4">
                  password
  <input type="password" name="textfield5">
                </p>
                <p>
                  <label>
                  <input type="submit" name="Submit" value="Submit">
                  </label>
                </p></td>
            </tr>
          </table>
		  <p>&nbsp;</p>
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
</body>
<!-- #EndTemplate --></html>
