<%@page import="com.dimata.common.entity.payment.StandartRate"%>
<%@page import="com.dimata.common.entity.payment.PstStandartRate"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.common.entity.payment.PriceType" %>
<%@ page import = "com.dimata.common.entity.payment.PstPriceType" %>
<%@ page import = "com.dimata.posbo.form.search.*"%>
<%@ page import = "com.dimata.posbo.entity.search.*"%>
<%@ page import = "com.dimata.posbo.session.masterdata.*"%>


<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG); %>
<%@ include file = "../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!
public static final String textListHeader[][] =
{
	{"DAFTAR KATALOG BARANG"},
	{"CATALOG LIST"}
};

public static final String textListGlobalHeader[][] =
{
	{"DAFTAR","PRINT LABEL HARGA","PILIH TIPE HARGA"},
	{"DAFTAR","PRINT PRICE TAG","CHOOSE PRICE TYPE"}
};



%>
<%

int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidMaterial = FRMQueryString.requestLong(request, "hidden_material_id");
long oidCodeRange = FRMQueryString.requestLong(request, "hidden_range_code_id");
int type = FRMQueryString.requestInt(request, "hidden_type");
String merkTitle = textListHeader[SESS_LANGUAGE][0]; //com.dimata.posbo.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.posbo.jsp.JspInfo.MATERIAL_UNIT];
String saveTitle = textListGlobalHeader[SESS_LANGUAGE][1];
long selectCurrencyId =FRMQueryString.requestLong(request, FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CURRENCY_TYPE_ID]);
/*variable declaration*/
int recordToGet = 8;
String msgString = "";
int iErrCode = FRMMessage.NONE;
int iErrCode2 = FRMMessage.NONE;

ControlLine ctrLine = new ControlLine();

SrcMaterial srcMaterial = new SrcMaterial();
FrmSrcMaterial frmSrcMaterial = new FrmSrcMaterial();

try {
       srcMaterial = (SrcMaterial) session.getValue(SessMaterial.SESS_SRC_MATERIAL);

        if (srcMaterial == null) {
                 srcMaterial = new SrcMaterial();
        }
} catch (Exception e) {
                //out.println(textListTitleHeader[SESS_LANGUAGE][3]);
     srcMaterial = new SrcMaterial();
}
 session.putValue(SessMaterial.SESS_SRC_MATERIAL, srcMaterial);
 
 

long categoryId = srcMaterial.getCategoryId();

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">


function cmdBack()
{
        self.close();
}

function cmdSearch()
{	
	document.frmmaterial.command.value="<%=Command.LIST%>";
	document.frmmaterial.action="buffer_price_tag.jsp";
	document.frmmaterial.submit();
}

function printPriceTag() {
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.masterdata.PrintAllListPriceTagPdf?tms=<%=System.currentTimeMillis()%>&","","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
    document.frmmaterial.submit();
    self.close();
}

function printForm() {
   document.frmmaterial.action="buffer_price_tag.jsp";
   document.frmmaterial.submit();
}



function setChecked(val) {
	dml=document.frmmaterial;
	len = dml.elements.length;

	var i=0;
	for( i=0 ; i<len ; i++) {
            <%
               Vector vt_priceType = PstPriceType.list(0,0,"",PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID]);
               for(int i=0; i<vt_priceType.size(); i++) {
                   PriceType priceType = (PriceType)vt_priceType.get(i);
            %>
                   if (dml.elements[i].name == '<%=frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_PRICE_TYPE_ID]%>') {
			if(val==1)
                            dml.elements[i].checked = <%=i+1%>;
			else
                            dml.elements[i].checked = val;
		   }
            <%}%>
	}
}




//-------------- script control line -------------------
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
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<SCRIPT language=JavaScript>
</SCRIPT>
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td width="88%" valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
             <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frmmaterial" method ="GET" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
              <input type="hidden" name="hidden_type" value="<%=type%>">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top">
                  <td height="8"  colspan="3">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="14" valign="middle" colspan="4" align="center">
                          <h2><strong><%=textListGlobalHeader[SESS_LANGUAGE][0]%>&nbsp;<%=textListGlobalHeader[SESS_LANGUAGE][1]%></strong></h2>
                          <hr size="1">                        </td>
                      </tr>
                      <tr><td width="21%" height="14" valign="middle" class="command"><b><%=textListGlobalHeader[SESS_LANGUAGE][1]%></b> :</td>
                      </tr>
                      <tr>
                        <td width="48%" height="14" valign="left" class="command">
						   <%
                                Vector val_PriceTypeid = new Vector(1,1);
                                Vector key_PriceTypeid = new Vector(1,1);
                                Vector vt_prc = PstPriceType.list(0,0,"","");
                                for(int d=0;d<vt_prc.size();d++){
                                PriceType prc = (PriceType)vt_prc.get(d);
					val_PriceTypeid.add(""+prc.getOID()+"");
					key_PriceTypeid.add(prc.getCode());
				}
                              ControlCheckBox controlCheckBox = new ControlCheckBox();
                              controlCheckBox.setWidth(5);

                          %>
                          <%=controlCheckBox.draw(FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_PRICE_TYPE_ID],val_PriceTypeid,key_PriceTypeid,new Vector())%>
                          
                        </td>
                        <td width="30%" height="14" valign="middle" class="command">&nbsp;</td>
                      </tr>
                      <tr>
                           <td width="21%" height="14" valign="middle" class="command">&nbsp;
                           </td>
                      </tr>
                      <tr>
                           <td width="21%" height="14" valign="middle" class="command">
                           <b>Currency</b> :
                           </td><td width="21%" height="14" valign="middle" class="command"><%
                                    //add opie-eyek 20130805
                                    Vector listCurr = PstStandartRate.listStandartRate(start,recordToGet, "" , "");
                                    Vector vectCurrVal = new Vector(1,1);
                                    Vector vectCurrKey = new Vector(1,1);
                                    vectCurrKey.add("All");
                                    vectCurrVal.add("-1");
                                    for(int i=0; i<listCurr.size(); i++){
                                        StandartRate standartRate = (StandartRate)listCurr.get(i);
                                        CurrencyType crType = new CurrencyType();
                                        String codeCurrency = "";
                                        vectCurrVal.add(""+standartRate.getOID());
                                        try{
                                                crType = PstCurrencyType.fetchExc(standartRate.getCurrencyTypeId());
                                                codeCurrency = crType.getCode();
                                        }catch(Exception e){
                                        }
                                        vectCurrKey.add(codeCurrency);
                                    }
                                    %> <%=ControlCombo.draw(FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CURRENCY_TYPE_ID],"formElemen", null, ""+srcMaterial.getCurrencyTypeId(), vectCurrVal, vectCurrKey, "")%>
                                    </td>
                      </tr>
                      <tr>
                          <td width="21%" height="14" valign="middle" class="command">
                               <b>Stok</b> :
                           </td>
                           <td width="21%" height="14" valign="middle" class="command"><%
                                    //add opie-eyek 20130805
                                    Vector vectStockVal = new Vector(1,1);
                                    Vector vectStockKey = new Vector(1,1);
                                    vectStockKey.add("All");
                                    vectStockVal.add("-1");
                                    vectStockKey.add("Stock Lebih Dari 0");
                                    vectStockVal.add("0");
                                    vectStockKey.add("Stock Kurang Dari 0");
                                    vectStockVal.add("2");
                                    %> <%=ControlCombo.draw("StatusStock","formElemen", null, "-1", vectStockVal, vectStockKey, "")%>
                                    </td>
                      </tr>
                      <tr>
                           <td width="21%" height="14" valign="middle" class="command">&nbsp;
                           </td>
                      </tr>
                      <tr>
                        <td colspan="2" width="21%" height="14" valign="middle" class="command"><b><a href="javascript:setChecked(1)" class="menuLink">select all</a> | <a href="javascript:setChecked(null)" class="menuLink">release all</a></b></td>
                        <td width="1%" height="14" valign="middle" class="command">&nbsp</td>
                        <td width="48%" height="14" valign="left" class="command">&nbsp</td>
                       </tr>
                      
                       <tr>
                           <td width="5%" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobalHeader[SESS_LANGUAGE][1]%>"></a>
                              &nbsp; <a href="javascript:printForm()" class="command" > <%=textListGlobalHeader[SESS_LANGUAGE][1]%> </a></td>
                                </a></td>
                       </tr>

                      
                    
                    </table>
                  </td>
                </tr>
              </table>
            </form>
            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
  <!--<tr>
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
      <%//@ include file = "../../main/footer.jsp" %>
      <!-- #EndEditable --> <!--</td>-->
 <!--</tr>-->
</table>
</body>
<!-- #EndTemplate --></html>
