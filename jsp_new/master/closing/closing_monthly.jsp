<%@page contentType="text/html"%>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.session.masterdata.SessClosing" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %> 
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_ADMIN, AppObjInfo.G2_ADMIN_SYSTEM, AppObjInfo.OBJ_ADMIN_SYSTEM_CLOSING_PERIOD); %>
<%@ include file = "../../main/checkuser.jsp" %>



<!-- JSP Block -->
<%!
public static final String textListTitleHeader[][] =
{
	{"Tutup Periode","Periode","Tutup Periode"},
	{"Closing Period","Period","Closing Period"}
};
%>

<%
int iCommand = FRMQueryString.requestCommand(request);
int finish = FRMQueryString.requestInt(request, "finish");
long MatPer = FRMQueryString.requestLong(request, "MatPer");


if (finish == 1)
{
	SessClosing objSessClosing = new SessClosing();
	int iClosingSuccess = objSessClosing.Process(MatPer,SESS_LANGUAGE);
		
	System.out.println("iClosingSuccess : " + iClosingSuccess);
		
	if (iClosingSuccess == objSessClosing.CLOSING_OK)
	{		
		response.sendRedirect("periode_success.jsp");	
//		response.sendRedirect("closing_success.jsp");	
	}
	else
	{
		try
		{
			if( session.getValue("MONTHLY_CLOSING") != null)
			{
				session.removeValue("MONTHLY_CLOSING");
			}			
					
			Vector vSessionDate = new Vector(1,1);
			vSessionDate.add(String.valueOf(iClosingSuccess));
			vSessionDate.add(objSessClosing.getMessage());
			vSessionDate.add(objSessClosing.getVListUnPostedLGRDoc());
			vSessionDate.add(objSessClosing.getVListUnPostedReturnDoc());
			vSessionDate.add(objSessClosing.getVListUnPostedDFDoc());
			vSessionDate.add(objSessClosing.getVListUnPostedSalesDoc());						
			 		
			session.putValue("MONTHLY_CLOSING", vSessionDate);
		}
		catch(Exception e)
		{
			System.out.println("Exc when manage SESSION : " + e.toString());
		}
		
		response.sendRedirect("periode_fail.jsp");			
//		response.sendRedirect("closing_fail.jsp");			
	}
}	
%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdFinish()
{
	document.frm_closingmonthly.command.value="<%=Command.SAVE%>";
	document.frm_closingmonthly.finish.value="1";
	document.frm_closingmonthly.action="closing_monthly.jsp";
	document.frm_closingmonthly.submit();
}

function MM_swapImgRestore() { //v3.0
    var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
    var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++) 
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImage() { //v3.0
    var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
    if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}		

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>

<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" --> 
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <%if(menuUsed == MENU_PER_TRANS){%>
  <tr>
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
      <%@ include file = "../../main/header.jsp" %>
      <!-- #EndEditable --></td>
  </tr>
  <tr>
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
  <%}else{%>
   <tr bgcolor="#FFFFFF">
    <td height="10" ID="MAINMENU">
      <%@include file="../../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
            <%=textListTitleHeader[SESS_LANGUAGE][0]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_closingmonthly" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="finish" value="">
              <table width="100%" border="0" cellspacing="2" cellpadding="2">
                <tr> 
                  <td colspan="3"> 
                    <hr size="1">
                  </td>
                </tr>
                <tr> 
                  <td colspan="3"> <b>&gt;&gt; Proses tutup periode digunakan 
                    untuk :</b> </td>
                </tr>
                <tr> 
                  <td width="6%">&nbsp;</td>
                  <td colspan="2">1. Menutup periode stock berjalan dan membuat 
                    periode stock baru untuk bulan berikutnya. </td>
                </tr>
                <tr> 
                  <td width="6%">&nbsp;</td>
                  <td colspan="2">2. Memindahkan stock akhir pada periode berjalan 
                    menjadi stock awal periode baru.</td>
                </tr>
                <tr> 
                  <td colspan="3"><b>&gt;&gt; Catatan</b> : Pastikan seluruh dokumen 
                    transaksi sudah di-posting. Jika masih ada dokumen dengan 
                    status DRAFT maka proses tutup periode akan gagal.</td>
                </tr>
                <tr> 
                  <td colspan="3">&nbsp;</td>
                </tr>
                <%
				if (finish != 1)
				{
				%>
                <tr> 
                  <td width="6%"><%=textListTitleHeader[SESS_LANGUAGE][1]%> </td>
                  <td width="1%">:</td>
                  <td width="93%"> 
                    <%  
					Vector val_mpid = new Vector(1,1); 
					Vector key_mpid = new Vector(1,1); 
					
					String sWhereClause = PstPeriode.fieldNames[PstPeriode.FLD_STATUS] + " = " + PstPeriode.FLD_STATUS_RUNNING;
					String sOrderBy = PstPeriode.fieldNames[PstPeriode.FLD_START_DATE] + " DESC";
					Vector vMatPeriod = PstPeriode.list(0, 0, sWhereClause, sOrderBy);
					if(vMatPeriod!=null && vMatPeriod.size()>0)
					{
						Periode mp = (Periode) vMatPeriod.get(0);
						val_mpid.add(""+mp.getOID()+"");
						key_mpid.add(mp.getPeriodeName());
					}
				  %>
                    <%=ControlCombo.draw("MatPer", null, ""+MatPer, val_mpid, key_mpid, "", "formElemen")%></td>
                </tr>
                <tr> 
                  <td colspan="3"> 
                    <table width="17%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <!--td width="20%"><a href="javascript:cmdFinish()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSaveOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSave.jpg" width="24" height="24" alt="<%=textListTitleHeader[SESS_LANGUAGE][2]%>"></a></td-->
                        <td width="80%" class="command" nowrap> <b> <a class="btn btn-primary" href="javascript:cmdFinish()"><i class="fa fa-check"></i> 
                          <%=textListTitleHeader[SESS_LANGUAGE][2]%> </a></b> </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <%
				}
				%>
              </table>
            </form>
<!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
       <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../styletemplate/footer.jsp" %>

        <%}else{%>
            <%@ include file = "../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
