<%!
public static final String txtListMainMenu[][]=
{
	{"Kembali ke Menu"},
	{"Back to Menu"}
};
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#E1CA9F">
  <tr>
    <td height="1" nowrap bgcolor="#A73701"><img src="home_files/spacer.gif" width=1 height=1></td>
  </tr>
  <tr>
    <td nowrap>
      <div align="center"> <A href="<%=approot%>/homepage.jsp" class="menus"><%=txtListMainMenu[SESS_LANGUAGE][0]%></A> </div>
    </td>
  </tr>
  <tr>
    <td height="1" nowrap bgcolor="#A73701"><img src="home_files/spacer.gif" width=1 height=1></td>
  </tr>
</table>
