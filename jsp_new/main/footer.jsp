<%
if(menuUsed==3||menuUsed==4){%>
<%}else{%>
<footer class="main-footer">
  <div class="pull-right hidden-xs">
    <b>Version</b> 4.0.1
  </div>
  <strong>Copyright &copy; 2017 <a href="http://dimata.com">Dimata IT Solutions</a>.</strong> All rights
  reserved.
</footer>
<%}%>
<%--
<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#E1CA9F">
  <%
  Date dtmn = new Date();
      System.out.println("matPeriodeInit.getEndDate()");
  if(false){
  %>
  <tr>
    <td height="1" nowrap bgcolor="#A73701" class="formElement" colspan="3"><img 
            src="home_files/spacer.gif" width=1 height=1></td>
  </tr>
  <tr>
    <td height="15" align="center" nowrap class="warning" ID=WRNG colspan="3">&nbsp;</td>
  </tr>
    <SCRIPT LANGUAGE="JavaScript">
        var message="Periode '<i class=fielderror><%=matPeriodeInit.getPeriodeName()%></i>' sudah berakhir, silakan tutup periode ini !!!";
    var speed=1500;
    var visible=0;
    function Flash() {
        if (visible == 0) {
            document.all.WRNG.innerHTML = message;
            visible=1;
        } else {
            document.all.WRNG.innerHTML = "&nbsp;";
            visible=0;
        }
        setTimeout('Flash()', speed);
    }
    Flash();
    </SCRIPT>
  <%}else{%>
  <tr>
    <td height="1" bgcolor="#A73701" colspan="3"><img height=1 src="home_files/spacer.gif" width=1></td>
  </tr>
  <tr> 
        <td width="25%" height="15" bgcolor="#E1CA9F"><font color="#FFFFFF" align="left"><%="Login as \""+userName+"\""%></font></td>
    <td width="50%" height="15" bgcolor="#E1CA9F">
       <% if(MODUS_PRODUCT!=MODUS_PRODUCT_CLIENT){%>
            <div align="center" class="menus">copyright © Dimata® Solutions 2007</div>
       <%}else{%>
            <div align="center" class="menus"></div>
       <%}%>
          
    </td>
        <td width="25%" height="15" bgcolor="#E1CA9F">&nbsp;</td>
  </tr>
  <%}%>
</table>
--%>