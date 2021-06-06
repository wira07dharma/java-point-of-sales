<%@ page import="com.dimata.posbo.entity.search.SrcStockCard,
                 com.dimata.posbo.form.search.FrmSrcStockCard,
                 com.dimata.posbo.session.warehouse.SessStockCard,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.posbo.entity.masterdata.Material,
                 com.dimata.posbo.entity.masterdata.PstMaterial,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlDate"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = 1; //AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_CARD); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListHeader[][] =
{
	{"Lokasi","Kode/Nama Barang","Kategori","Periode","Urut Berdasar","Sub Kategori","Supplier"},
	{"Warehouse","Goods Name","Category","Periode","Sort By","Sub Category","Supplier"}
};

public String getJspTitle(int index, int language, String prefiks, boolean addBody)
{
	String result = "";
	if(addBody)
	{
		if(language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){
			result = textListHeader[language][index] + " " + prefiks;
		}
		else
		{
			result = prefiks + " " + textListHeader[language][index];
		}
	}
	else
	{
		result = textListHeader[language][index];
	}
	return result;
}

%>


<!-- Jsp Block -->
<%
/**
* get data from 'hidden form'
*/
int iCommand = FRMQueryString.requestCommand(request);
int index = FRMQueryString.requestInt(request,"type");

//System.out.println("index : "+index);
ControlLine ctrLine = new ControlLine();
String txtKode = "";
String txtNama = "";
    SrcStockCard srcStockCard = new SrcStockCard();
FrmSrcStockCard frmSrcStockCard = new FrmSrcStockCard();

try
{
	srcStockCard = (SrcStockCard)session.getValue(SessStockCard.SESS_STOCK_CARD);
    if(srcStockCard.getMaterialId()!=0){
        Material material = PstMaterial.fetchExc(srcStockCard.getMaterialId());
        txtKode = material.getSku();
        txtNama = material.getName();
    }
}catch(Exception e){
	srcStockCard = new SrcStockCard();
}

if(srcStockCard==null){
	srcStockCard = new SrcStockCard();
}

try{
	session.removeValue(SessStockCard.SESS_STOCK_CARD);
}catch(Exception e){}

%>
<html>
<!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function cmdSearch()
{
	<%
	String msg = getJspTitle(1,SESS_LANGUAGE,"",true);
	%>
	if(document.frmsrcstockcard.txtkode.value != "" || document.frmsrcstockcard.txtnama.value != "") {
		document.frmsrcstockcard.command.value="<%=Command.LIST%>";
		document.frmsrcstockcard.action="stockcard_list.jsp";
		document.frmsrcstockcard.submit();
	}
	else {
		alert("<%=msg%>tidak boleh kosong!");
	}
}

function cekbrg(){
    window.open("matstockcardsearch.jsp?mat_code='"+document.frmsrcstockcard.txtkode.value+"'","srhmatcard", "height=500,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
	var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
	var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
	if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.0
	var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
	d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
	if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
	for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
	if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
	var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
	if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" -->
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnSearchOn.jpg')">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr>
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
      <%@ include file = "../../../main/header.jsp" %>
      <!-- #EndEditable --></td>
  </tr>
  <tr>
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
  <tr>
    <td width="88%" valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            &nbsp;Konsinyasi > Stock Card<!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form name="frmsrcstockcard" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="type" value="<%=index%>">
              <input type="hidden" name="<%=FrmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_MATERIAL_ID]%>" value="<%=srcStockCard.getMaterialId()%>">
              <table width="100%" border="0">
                <tr>
                  <td colspan="2">
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td height="21">&nbsp;</td>
                        <td height="21">&nbsp;</td>
                        <td height="21">&nbsp;</td>
                      </tr>
                      <tr>
                        <td height="21" width="15%"><%=getJspTitle(0,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="84%">&nbsp; <%
							Vector val_locationid1 = new Vector(1,1);
							Vector key_locationid1 = new Vector(1,1);
                            String locWhereClause = "";
                            if(index ==0 ){
                                locWhereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
                            }else{
                                locWhereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
                            }
							String locOrderBy = PstLocation.fieldNames[PstLocation.FLD_CODE];
							//Vector vt_loc1 = PstLocation.list(0,0,locWhereClause,locOrderBy);
							Vector vt_loc1 = PstLocation.list(0,0,"",locOrderBy);
							for(int d=0;d<vt_loc1.size();d++)
							{
								Location loc = (Location)vt_loc1.get(d);
								val_locationid1.add(""+loc.getOID()+"");
								key_locationid1.add(loc.getName());
							}
						  %> <%=ControlCombo.draw(frmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_LOCATION_ID], null, ""+srcStockCard.getLocationId(), val_locationid1, key_locationid1, "", "formElemen")%> </td>
                      </tr>
                      <tr>
                        <td valign="top" align="left"><%=getJspTitle(1,SESS_LANGUAGE,"",true)%></td>
                        <td valign="top" align="left">:</td>
                        <td valign="top" align="left"> &nbsp;&nbsp;<input name="txtkode" type="text" value="<%=txtKode%>" class="formElemen" id="txtkode" size="10">
                          /
                          <input name="txtnama" type="text" readonly class="formElemen" id="txtnama" value="<%=txtNama%>" size="40">
                          <a href="javascript:cekbrg()">CHK</a> * </td>
                      </tr>
                      <tr>
                        <td valign="top" width="15%" align="left"><%=getJspTitle(3,SESS_LANGUAGE,"",true)%></td>
                        <td valign="top" width="1%" align="left">:</td>
                        <td width="84%" valign="top" align="left"> &nbsp; <%=ControlDate.drawDate(frmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_START_DATE], srcStockCard.getStardDate(),"formElemen",1,-5)%> s/d <%=	ControlDate.drawDate(frmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_END_DATE], srcStockCard.getEndDate(),"formElemen",1,-5) %> </td>
                      </tr>
                      <tr>
                        <td height="19" valign="top" width="15%" align="left"><%=""%></td>
                        <td height="19" valign="top" width="1%" align="left"></td>
                        <td height="19" width="84%" valign="top" align="left">
                        </td>
                      </tr>
                      <tr>
                        <td height="21" valign="top" width="15%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="84%" valign="top" align="left">&nbsp;</td>
                      </tr>
                      <tr>
                        <td height="21" valign="top" width="15%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="84%" valign="top" align="left">
                          <table width="40%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <td width="10%" nowrap><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,"Stock Card",ctrLine.CMD_SEARCH,true)%>"></a></td>
                              <td width="90%" nowrap class="command"><a href="javascript:cmdSearch()"><%=ctrLine.getCommand(SESS_LANGUAGE,"Stock Card",ctrLine.CMD_SEARCH,true)%></a></td>
                            </tr>
                          </table></td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </form>
            <SCRIPT language=JavaScript>

</SCRIPT>
            <!-- #EndEditable --></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
      <%@ include file = "../../../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate -->
</html>
