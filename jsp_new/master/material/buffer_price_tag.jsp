<% 
/* 
 * Page Name  		:  buffer_ar_detail_pdf.jsp
 * Created on 		:  [date] [time] AM/PM 
 * 
 * @author  		:  gwawan 
 * @version  		:  [version] 
 */

/*******************************************************************
 * Page Description : [project description ... ] 
 * Imput Parameters : [input parameter ...] 
 * Output 			: [output ...] 
 *******************************************************************/
%>
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

<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!

%>

<html>
<head>
<title>Dimata - ProChain POS</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head> 

<body bgcolor="#FFFFFF" text="#000000">
Loading ... 
<script language="JavaScript">
	window.focus();
</script>
</body>
</html>

<!-- JSP Block -->
<%
int iCommand = FRMQueryString.requestCommand(request);
SrcMaterial srcMaterial = new SrcMaterial();
//FrmSrcMaterial frmSrcMaterial = new FrmSrcMaterial();

try {
       srcMaterial = (SrcMaterial) session.getValue(SessMaterial.SESS_SRC_MATERIAL);

        if (srcMaterial == null) {
                 srcMaterial = new SrcMaterial();
        }
} catch (Exception e) {
                //out.println(textListTitleHeader[SESS_LANGUAGE][3]);
     srcMaterial = new SrcMaterial();
}


 Vector vectPriceTypeId = new Vector(1, 1);
            String[] strPriceTypeId = request.getParameterValues(FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_PRICE_TYPE_ID]);
            if (strPriceTypeId != null && strPriceTypeId.length > 0) {
                for (int i = 0; i < strPriceTypeId.length; i++) {
                    try {
                        vectPriceTypeId.add(strPriceTypeId[i]);
                    } catch (Exception exc) {
                        System.out.println("err");
                    }
                }
                srcMaterial.setPriceTypeId(vectPriceTypeId);
}
 //add opie-eyek 20130805
long selectCurrencyId =FRMQueryString.requestLong(request, FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CURRENCY_TYPE_ID]);
srcMaterial.setSelectCurrencyTypeId(selectCurrencyId);
session.putValue(SessMaterial.SESS_SRC_MATERIAL, srcMaterial);
 
%>
<script language="JavaScript">
document.location="<%=approot%>/servlet/com.dimata.posbo.report.masterdata.PrintAllListPriceTagPdf?x";
</script>


