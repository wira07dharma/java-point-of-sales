<%--
    Document   : bill_detail_stockcode.jsp
    Created on : 02 Jul 13, 11:44:26
    Author     : Wiweka
--%>

<%@ page import = "com.dimata.gui.jsp.ControlList" %>
<%@ page import = "com.dimata.pos.entity.billing.*" %>
<%@ page import = "com.dimata.pos.form.billing.*" %>
<%@ page import = "com.dimata.gui.jsp.ControlList" %>
<%@ page language = "java" %>
<%@ page import = "java.util.*" %>
<%@ page import= "com.dimata.util.*" %>
<%@page import= "com.dimata.gui.jsp.*" %>
<%@page import= "com.dimata.qdep.form.*" %>
<%@page import= "com.dimata.pos.entity.masterCashier.*"%>
<%@page import = "com.dimata.pos.form.masterCashier.*" %>
<%@page import= "com.dimata.common.entity.location.*" %>


<%@ include file = "../main/javainit.jsp" %>
<!--% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CASHIER); %>
<!--%@ include file = "../main/checkuser.jsp" %>
<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListHeader[][] =
{
        {"No","Nama Item","Serial Number"},
        {"No","Item Name","Serial Number"}
};
public static final String textListTitleHeader[][] =
{
        {"Nama Barang"},
        {"Item Name"}
};
/* this method used to list material master cashier */
public String drawList(int language, int iCommand, FrmBillDetailCode frmObject, BillDetailCode objEntity, Vector objectClass, long billDetailCodeId, int start)
{
    ControlList ctrlist = new ControlList();

    try{

        ctrlist.setAreaWidth("70%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListHeader[language][0],"5%");
       //ctrlist.addHeader(textListHeader[language][1],"20%");
        ctrlist.addHeader(textListHeader[language][2],"17%");
        //ctrlist.addHeader(textListHeader[language][5],"10%");

        ctrlist.setLinkRow(0);
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        Vector rowx = new Vector(1,1);
        ctrlist.reset();
        int index = -1;
        if(start<0)
            start = 0;

        for(int i = 0; i < objectClass.size(); i++)
        {
                BillDetailCode billDetailCode = (BillDetailCode)objectClass.get(i);
                rowx = new Vector();
                if(billDetailCodeId == billDetailCode.getOID())
                         index = i;

                start = start + 1;

                if(index == i && (iCommand == Command.EDIT || iCommand == Command.ASK)){
                        rowx.add(""+start);
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"17\" name=\""+frmObject.fieldNames[FrmBillDetailCode.FRM_FIELD_STOCK_CODE] +"\" value=\""+billDetailCode.getStockCode()+"\" style=\"text-align:right\" class=\"formElemen\">  <a href=\"javascript:openCheck()\">CHK</a></div>");
                }else{
                        rowx.add("" + start);
                        rowx.add("<div align=\"left\">"+String.valueOf(billDetailCode.getStockCode())+"</div>");
                }
                lstData.add(rowx);

        }
        rowx = new Vector();
        if(iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0))
        {
                        rowx.add(""+(start+1));
                        rowx.add("<div align=\"left\"><input type=\"text\" size=\"17\" name=\""+frmObject.fieldNames[FrmBillDetailCode.FRM_FIELD_STOCK_CODE] +"\" value=\"\"  class=\"formElemen\">  <a href=\"javascript:openCheck()\">CHK</a></div>");
                        lstData.add(rowx);
        }

    }catch(Exception e){
        System.out.println("err"+e.toString());
    }
        return ctrlist.draw();
}
%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidBillDetailCode = FRMQueryString.requestLong(request, "hidden_bill_code_id");
long oidBillDetail = FRMQueryString.requestLong(request, "hidden_bill_detail_id");
long oidMaterial = FRMQueryString.requestLong(request, "materialId");

// variable declaration
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = "";

CtrlBillDetailCode ctrlBillDetailCode = new CtrlBillDetailCode(request);
ControlLine ctrLine = new ControlLine();
Vector listBillDetailCode = new Vector(1,1);

iErrCode = ctrlBillDetailCode.action(iCommand, oidBillDetailCode, oidBillDetail);

FrmBillDetailCode frmBillDetailCode = ctrlBillDetailCode.getForm();


int vectSize = PstBillDetailCode.getCount(whereClause);
if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST))
  {
                start = ctrlBillDetailCode.actionList(iCommand, start, vectSize, recordToGet);
  }

BillDetailCode billDetailCode = ctrlBillDetailCode.getBillDetailCode();
msgString = ctrlBillDetailCode.getMessage();

whereClause = PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_SALE_ITEM_ID] +" = "+ oidBillDetail ;
listBillDetailCode = PstBillDetailCode.list(start, recordToGet, whereClause, orderClause);

if (listBillDetailCode.size() < 1 && start > 0)
{
         if (vectSize - recordToGet > recordToGet)
                        start = start - recordToGet;
         else
         {
                 start = 0 ;
                 iCommand = Command.FIRST;
                 prevCommand = Command.FIRST;
         }
         listBillDetailCode = PstBillDetailCode.list(start, recordToGet, whereClause, orderClause);
}
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            function openCheck(){
                var strvalue  = "stockcodedosearch.jsp?command=<%=Command.FIRST%>"+
                    "&material_id="+document.frmbillcode.materialId.value;
                window.open(strvalue,"stockcode2", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
            }

            function cmdAdd()
            {
                document.frmbillcode.hidden_bill_code_id.value="0";
                document.frmbillcode.command.value="<%=Command.ADD%>";
                document.frmbillcode.prev_command.value="<%=prevCommand%>";
                document.frmbillcode.action="bill_detail_stockcode.jsp";
                document.frmbillcode.submit();
            }

            function cmdAsk(oidBillDetailCode)
            {
                document.frmbillcode.hidden_bill_code_id.value=oidBillDetailCode;
                document.frmbillcode.command.value="<%=Command.ASK%>";
                document.frmbillcode.prev_command.value="<%=prevCommand%>";
                document.frmbillcode.action="bill_detail_stockcode.jsp";
                document.frmbillcode.submit();
            }

            function cmdConfirmDelete(oidBillDetailCode)
            {
                document.frmbillcode.hidden_bill_code_id.value=oidBillDetailCode;
                document.frmbillcode.command.value="<%=Command.DELETE%>";
                document.frmbillcode.prev_command.value="<%=prevCommand%>";
                document.frmbillcode.action="bill_detail_stockcode.jsp";
                document.frmbillcode.submit();
            }

            function cmdSave()
            {
                document.frmbillcode.command.value="<%=Command.SAVE%>";
                document.frmbillcode.prev_command.value="<%=prevCommand%>";
                document.frmbillcode.action="bill_detail_stockcode.jsp";
                document.frmbillcode.submit();
            }

            function cmdEdit(oidBillDetailCode)
            {
                document.frmbillcode.hidden_bill_code_id.value=oidBillDetailCode;
                document.frmbillcode.command.value="<%=Command.EDIT%>";
                document.frmbillcode.prev_command.value="<%=prevCommand%>";
                document.frmbillcode.action="bill_detail_stockcode.jsp";
                document.frmbillcode.submit();
            }

            function cmdCancel(oidBillDetailCode)
            {
                document.frmbillcode.hidden_bill_code_id.value=oidBillDetailCode;
                document.frmbillcode.command.value="<%=Command.EDIT%>";
                document.frmbillcode.prev_command.value="<%=prevCommand%>";
                document.frmbillcode.action="bill_detail_stockcode.jsp";
                document.frmbillcode.submit();
            }

            function cmdBack()
            {
                document.frmbillcode.command.value="<%=Command.BACK%>";
                document.frmbillcode.action="bill_detail_stockcode.jsp";
                document.frmbillcode.submit();
            }

            function cmdListFirst()
            {
                document.frmbillcode.command.value="<%=Command.FIRST%>";
                document.frmbillcode.prev_command.value="<%=Command.FIRST%>";
                document.frmbillcode.action="bill_detail_stockcode.jsp";
                document.frmbillcode.submit();
            }

            function cmdListPrev()
            {
                document.frmbillcode.command.value="<%=Command.PREV%>";
                document.frmbillcode.prev_command.value="<%=Command.PREV%>";
                document.frmbillcode.action="bill_detail_stockcode.jsp";
                document.frmbillcode.submit();
            }

            function cmdListNext()
            {
                document.frmbillcode.command.value="<%=Command.NEXT%>";
                document.frmbillcode.prev_command.value="<%=Command.NEXT%>";
                document.frmbillcode.action="bill_detail_stockcode.jsp";
                document.frmbillcode.submit();
            }

            function cmdListLast()
            {
                document.frmbillcode.command.value="<%=Command.LAST%>";
                document.frmbillcode.prev_command.value="<%=Command.LAST%>";
                document.frmbillcode.action="bill_detail_stockcode.jsp";
                document.frmbillcode.submit();
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
                <td valign="top" align="left">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> > <!-- #EndEditable --></td>
                        </tr>
                        <tr>
                            <td><!-- #BeginEditable "content" -->
                                <form name="frmbillcode" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="hidden_bill_code_id" value="<%=oidBillDetailCode%>">
                                    <input type="hidden" name="hidden_bill_detail_id" value="<%=oidBillDetail%>">

                                    <% //Mencari Material Id
                                    String itemName="" ;
                                    Vector listMaterialId = PstBillDetail.listTmp(0, 0, PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID] + " ='" + oidBillDetail + "'", "");
                                    for (int i = 0; i < listMaterialId.size(); i++) {
                                    Billdetail billdetail = (Billdetail) listMaterialId.get(i);
                                    oidMaterial = billdetail.getMaterialId();
                                    itemName = billdetail.getItemName();
                                    }
                                    %>

                                    <input type="hidden" name="materialId" value="<%=oidMaterial%>">
                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top">
                                            <td height="8"  colspan="3">
                                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr align="left" valign="top">
                                                        <td height="8" valign="middle" colspan="3">
                                                            <hr size="1">
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=textListTitleHeader[SESS_LANGUAGE][0]+" : "%><%=itemName%></u></td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3">
                                                            <%=drawList(SESS_LANGUAGE, iCommand, frmBillDetailCode, billDetailCode, listBillDetailCode, oidBillDetailCode, start)%>
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="8" align="left" colspan="2" class="command">
                                                            <% if(iCommand != Command.ADD){ %>
                                                            <a href="javascript:cmdAdd()" onKeyDown="javascript:keybrdPress(this)"><span >ADD</span></a>
                                                            <%}%>
                                                        </td>
                                                        <td>
                                                            <% if(iCommand == Command.ADD){ %>
                                                            <a href="javascript:cmdSave()" onKeyDown="javascript:keybrdPress(this)"><span >SAVE</span></a>
                                                            <%}%>
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3">
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr align="left" valign="top" >
                                            <td colspan="3" class="command">
                                            </td>
                                        </tr>
                                    </table>
                                </form>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </body>
    <!-- #EndTemplate --></html>

