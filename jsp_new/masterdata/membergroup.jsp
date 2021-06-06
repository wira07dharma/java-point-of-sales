<% 
/* 
 * Page Name  		:  membergroup.jsp
 * Created on 		:  [date] [time] AM/PM 
 * 
 * @author  		:  [authorName] 
 * @version  		:  [version] 
 */

/*******************************************************************
 * Page Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 			: [output ...] 
 *******************************************************************/
%>
<%@ page language = "java" %>
<!-- package java -->                           
<%@ page import = "java.util.*,
         com.dimata.common.entity.payment.DiscountType,
         com.dimata.common.entity.payment.PstDiscountType,
         com.dimata.common.entity.payment.PriceType,
         com.dimata.common.entity.payment.PstPriceType" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package posbo -->
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.masterdata.*" %>
<%//@ page import = "com.dimata.posbo.entity.admin.*" %>

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_MEMBER_TYPE); %>
<%@ include file = "../main/checkuser.jsp" %>


<!-- Jsp Block -->
<%!

        public static final String textListTitle[][] =
    {
        {"Tipe Member","Harus diisi"},
        {"Member Type","required"}
    };
	
        public static final String textListHeader[][] =
    {
        {"Kode","Nama","Tipe Potongan","Tipe Harga","Keterangan","Tipe","Tipe Poin","Poin in Calculate","View Customer Type"},
        {"Code","Name","Discount Type","Price Type","Description","Type","Type Point","Point in Calculate","View Customer Type"}
    };
	
        public String drawList(Vector objectClass ,  long memberGroupId, int languange)

        {
                ControlList ctrlist = new ControlList();
                ctrlist.setAreaWidth("100%");
                ctrlist.setListStyle("listgen");
                ctrlist.setTitleStyle("tableheader");
                ctrlist.setCellStyle("cellStyle");
                ctrlist.setHeaderStyle("tableheader");
                ctrlist.setListStyle("listgen");
                ctrlist.setTitleStyle("listgentitle");
                ctrlist.setCellStyle("listgensell");
                ctrlist.setHeaderStyle("listgentitle");
                ctrlist.addHeader(textListHeader[languange][0],"10%");
                ctrlist.addHeader(textListHeader[languange][1],"10%");
                ctrlist.addHeader(textListHeader[languange][2],"10%");
                ctrlist.addHeader(textListHeader[languange][3],"15%");
                ctrlist.addHeader(textListHeader[languange][4],"15%");
                ctrlist.addHeader(textListHeader[languange][5],"5%");
                ctrlist.addHeader(textListHeader[languange][6],"5%");
                ctrlist.addHeader(textListHeader[languange][7],"5%");
                ctrlist.addHeader(textListHeader[languange][8],"5%");
		
                //ctrlist.addHeader("Discount Type Id","20%");
                //ctrlist.addHeader("Price Type Id","20%");
                //ctrlist.addHeader("Code","20%");
                //ctrlist.addHeader("Name","20%");
                //ctrlist.addHeader("Description","20%");

                ctrlist.setLinkRow(0);
                ctrlist.setLinkSufix("");
                Vector lstData = ctrlist.getData();
                Vector lstLinkData = ctrlist.getLinkData();
                ctrlist.setLinkPrefix("javascript:cmdEdit('");
                ctrlist.setLinkSufix("')");
                ctrlist.reset();
                int index = -1;

                for (int i = 0; i < objectClass.size(); i++) {
                        MemberGroup memberGroup = (MemberGroup)objectClass.get(i);
                         Vector rowx = new Vector();
                         if(memberGroupId == memberGroup.getOID())
                                 index = i;
			
                        rowx.add(memberGroup.getCode());
                        rowx.add(memberGroup.getName());
                        //rowx.add(String.valueOf(memberGroup.getDiscountTypeId()));
                        DiscountType dsType = new DiscountType();
                        String codeDsc = "";
                        try{
                                dsType = PstDiscountType.fetchExc(memberGroup.getDiscountTypeId());
                                codeDsc = dsType.getCode();
                        }catch(Exception e){
                        }
                        rowx.add(codeDsc);
                        //rowx.add(String.valueOf(memberGroup.getPriceTypeId()));
                        PriceType prType = new PriceType();
                        String codePr = "";
                        try{
                                prType = PstPriceType.fetchExc(memberGroup.getPriceTypeId());
                                codePr = prType.getCode();
                        }catch(Exception e){
                        }
                        rowx.add(codePr);
                        rowx.add(memberGroup.getDescription());
                        //rowx.add(""+memberGroup.getGroupType());
                        try{
                            rowx.add(PstMemberGroup.typeNames[memberGroup.getGroupType()-1]);
                        }catch(Exception e){
                            rowx.add("");
                        }
                        rowx.add(""+PstMemberGroup.typePointNames[memberGroup.getTypePoint()]);
                        rowx.add(""+memberGroup.getPointInCalculate());
                        rowx.add(""+PstMemberGroup.viewMemberType[memberGroup.getViewCustomerType()]);
                        lstData.add(rowx);
                        lstLinkData.add(String.valueOf(memberGroup.getOID()));
                }

                //return ctrlist.drawList(index);
                return ctrlist.draw(index);
        }

%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidMemberGroup = FRMQueryString.requestLong(request, "hidden_member_group_id");
/*variable declaration*/
boolean privManageData = true;
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = ""+PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE];

CtrlMemberGroup ctrlMemberGroup = new CtrlMemberGroup(request);
ControlLine ctrLine = new ControlLine();
Vector listMemberGroup = new Vector(1,1);

/*switch statement */
iErrCode = ctrlMemberGroup.action(iCommand , oidMemberGroup);
/* end switch*/
FrmMemberGroup frmMemberGroup = ctrlMemberGroup.getForm();

/*count list All MemberGroup*/
int vectSize = PstMemberGroup.getCount(whereClause);

MemberGroup memberGroup = ctrlMemberGroup.getMemberGroup();
msgString =  ctrlMemberGroup.getMessage();

/*switch list MemberGroup*/
if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE))
        start = PstMemberGroup.findLimitStart(memberGroup.getOID(),recordToGet, whereClause);

if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
                start = ctrlMemberGroup.actionList(iCommand, start, vectSize, recordToGet);
 } 
/* end switch list*/

/* get record to display */
listMemberGroup = PstMemberGroup.list(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listMemberGroup.size() < 1 && start > 0)
{
         if (vectSize - recordToGet > recordToGet)
                        start = start - recordToGet;   //go to Command.PREV
         else{
                 start = 0 ;
                 iCommand = Command.FIRST;
                 prevCommand = Command.FIRST; //go to Command.FIRST
         }
         listMemberGroup = PstMemberGroup.list(start,recordToGet, whereClause , orderClause);
}
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">


            function cmdAdd() {
                document.frmmembergroup.hidden_member_group_id.value = "0";
                document.frmmembergroup.command.value = "<%=Command.ADD%>";
                document.frmmembergroup.prev_command.value = "<%=prevCommand%>";
                document.frmmembergroup.action = "membergroup.jsp";
                document.frmmembergroup.submit();
            }

            function cmdAsk(oidMemberGroup) {
                document.frmmembergroup.hidden_member_group_id.value = oidMemberGroup;
                document.frmmembergroup.command.value = "<%=Command.ASK%>";
                document.frmmembergroup.prev_command.value = "<%=prevCommand%>";
                document.frmmembergroup.action = "membergroup.jsp";
                document.frmmembergroup.submit();
            }

            function cmdConfirmDelete(oidMemberGroup) {
                document.frmmembergroup.hidden_member_group_id.value = oidMemberGroup;
                document.frmmembergroup.command.value = "<%=Command.DELETE%>";
                document.frmmembergroup.prev_command.value = "<%=prevCommand%>";
                document.frmmembergroup.action = "membergroup.jsp";
                document.frmmembergroup.submit();
            }
            function cmdSave() {
                document.frmmembergroup.command.value = "<%=Command.SAVE%>";
                document.frmmembergroup.prev_command.value = "<%=prevCommand%>";
                document.frmmembergroup.action = "membergroup.jsp";
                document.frmmembergroup.submit();
            }

            function cmdEdit(oidMemberGroup) {
                document.frmmembergroup.hidden_member_group_id.value = oidMemberGroup;
                document.frmmembergroup.command.value = "<%=Command.EDIT%>";
                document.frmmembergroup.prev_command.value = "<%=prevCommand%>";
                document.frmmembergroup.action = "membergroup.jsp";
                document.frmmembergroup.submit();
            }

            function cmdCancel(oidMemberGroup) {
                document.frmmembergroup.hidden_member_group_id.value = oidMemberGroup;
                document.frmmembergroup.command.value = "<%=Command.EDIT%>";
                document.frmmembergroup.prev_command.value = "<%=prevCommand%>";
                document.frmmembergroup.action = "membergroup.jsp";
                document.frmmembergroup.submit();
            }

            function cmdBack() {
                document.frmmembergroup.command.value = "<%=Command.BACK%>";
                document.frmmembergroup.action = "membergroup.jsp";
                document.frmmembergroup.submit();
            }

            function cmdListFirst() {
                document.frmmembergroup.command.value = "<%=Command.FIRST%>";
                document.frmmembergroup.prev_command.value = "<%=Command.FIRST%>";
                document.frmmembergroup.action = "membergroup.jsp";
                document.frmmembergroup.submit();
            }

            function cmdListPrev() {
                document.frmmembergroup.command.value = "<%=Command.PREV%>";
                document.frmmembergroup.prev_command.value = "<%=Command.PREV%>";
                document.frmmembergroup.action = "membergroup.jsp";
                document.frmmembergroup.submit();
            }

            function cmdListNext() {
                document.frmmembergroup.command.value = "<%=Command.NEXT%>";
                document.frmmembergroup.prev_command.value = "<%=Command.NEXT%>";
                document.frmmembergroup.action = "membergroup.jsp";
                document.frmmembergroup.submit();
            }

            function cmdListLast() {
                document.frmmembergroup.command.value = "<%=Command.LAST%>";
                document.frmmembergroup.prev_command.value = "<%=Command.LAST%>";
                document.frmmembergroup.action = "membergroup.jsp";
                document.frmmembergroup.submit();
            }

            //-------------- script control line -------------------
            function MM_swapImgRestore() { //v3.0
                var i, x, a = document.MM_sr;
                for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++)
                    x.src = x.oSrc;
            }

            function MM_preloadImages() { //v3.0
                var d = document;
                if (d.images) {
                    if (!d.MM_p)
                        d.MM_p = new Array();
                    var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
                    for (i = 0; i < a.length; i++)
                        if (a[i].indexOf("#") != 0) {
                            d.MM_p[j] = new Image;
                            d.MM_p[j++].src = a[i];
                        }
                }
            }

            function MM_findObj(n, d) { //v4.0
                var p, i, x;
                if (!d)
                    d = document;
                if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
                    d = parent.frames[n.substring(p + 1)].document;
                    n = n.substring(0, p);
                }
                if (!(x = d[n]) && d.all)
                    x = d.all[n];
                for (i = 0; !x && i < d.forms.length; i++)
                    x = d.forms[i][n];
                for (i = 0; !x && d.layers && i < d.layers.length; i++)
                    x = MM_findObj(n, d.layers[i].document);
                if (!x && document.getElementById)
                    x = document.getElementById(n);
                return x;
            }

            function MM_swapImage() { //v3.0
                var i, j = 0, x, a = MM_swapImage.arguments;
                document.MM_sr = new Array;
                for (i = 0; i < (a.length - 2); i += 3)
                    if ((x = MM_findObj(a[i])) != null) {
                        document.MM_sr[j++] = x;
                        if (!x.oSrc)
                            x.oSrc = x.src;
                        x.src = a[i + 2];
                    }
            }

        </script>
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
        <!-- #BeginEditable "styles" -->
        <%if(menuUsed == MENU_ICON){%>
        <link href="../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>
        <link rel="stylesheet" href="../styles/main.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" --> 
        <link rel="stylesheet" href="../styles/tab.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "headerscript" --> 
        <SCRIPT language=JavaScript>
            function hideObjectForMarketing() {
            }

            function hideObjectForWarehouse() {
            }

            function hideObjectForProduction() {
            }

            function hideObjectForPurchasing() {
            }

            function hideObjectForAccounting() {
            }

            function hideObjectForHRD() {
            }

            function hideObjectForGallery() {
            }

            function hideObjectForMasterData() {
            }

        </SCRIPT>
        <!-- #EndEditable --> 
    </head> 

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <%if(menuUsed == MENU_PER_TRANS){%>
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
            <%}else{%>
            <tr bgcolor="#FFFFFF">
                <td height="10" ID="MAINMENU">
                    <%@include file="../styletemplate/template_header.jsp" %>
                </td>
            </tr>
            <%}%>
            <tr> 
                <td valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                        <tr> 
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
                                Master Data &gt; <%=textListTitle[SESS_LANGUAGE][0]%> <!-- #EndEditable --></td>
                        </tr>
                        <tr> 
                            <td><!-- #BeginEditable "content" --> 
                                <form name="frmmembergroup" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="hidden_member_group_id" value="<%=oidMemberGroup%>">
                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top"> 
                                            <td height="8"  colspan="3"> 
                                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr align="left" valign="top"> 
                                                        <td height="8" valign="middle" colspan="3"> 
                                                            <hr>
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                        <td height="14" valign="middle" colspan="3" class="comment"><%=textListTitle[SESS_LANGUAGE][0]%></td>
                                                    </tr>
                                                    <%
                                                                                      try{
                                                                                              if (listMemberGroup.size()>0){
                                                    %>
                                                    <tr align="left" valign="top"> 
                                                        <td height="22" valign="middle" colspan="3"> <%= drawList(listMemberGroup,oidMemberGroup,SESS_LANGUAGE)%> </td>
                                                    </tr>
                                                    <%  } 
                                                                                }catch(Exception exc){ 
                                                                                }%>
                                                    <tr align="left" valign="top"> 
                                                        <td height="8" align="left" colspan="3" class="command"> 
                                                            <span class="command"> 
                                                                <% 
                                                                                                         int cmd = 0;
                                                                                                                 if ((iCommand == Command.FIRST || iCommand == Command.PREV )|| 
                                                                                                                      (iCommand == Command.NEXT || iCommand == Command.LAST))
                                                                                                                              cmd =iCommand; 
                                                                                                         else{
                                                                                                                if(iCommand == Command.NONE || prevCommand == Command.NONE)
                                                                                                                      cmd = Command.FIRST;
                                                                                                                else 
                                                                                                                      cmd =prevCommand; 
                                                                                                         } 
                                                                %>
                                                                <% ctrLine.setLocationImg(approot+"/images");
                                                                                                      ctrLine.initDefault();
                                                                %>
                                                                <%=ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet)%> </span> </td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                        <td height="22" valign="middle" colspan="3"> 
                                                            <br>
                                                            <table width="20%" border="0" cellspacing="0" cellpadding="0">
                                                                <tr> 
                                                                    <%if(privAdd && privManageData){%>
                                                                    <%if(iCommand==Command.NONE||iCommand==Command.FIRST||iCommand==Command.PREV||iCommand==Command.NEXT||iCommand==Command.LAST||iCommand==Command.BACK||(iCommand==Command.SAVE)&&(frmMemberGroup.errorSize()==0)||(iCommand==Command.DELETE)&&(frmMemberGroup.errorSize()==0)){%>
                                                                    <!--td width="19%" nowrap><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true)%>"></a></td-->
                                                                    <td width="81%" nowrap class="command"><a class="btn btn-lg btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true)%></a></td>
                                                                        <%}}%>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr align="left" valign="top"> 
                                            <td height="8" valign="middle" colspan="3"> 
                                                <%if((iCommand ==Command.ADD)||(iCommand==Command.SAVE)&&(frmMemberGroup.errorSize()>0) || (iCommand==Command.DELETE)&&(frmMemberGroup.errorSize()>0) ||(iCommand==Command.EDIT)||(iCommand==Command.ASK)){%>
                                                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                                    <tr align="left" valign="top"> 
                                                        <td height="21" valign="middle" width="2%">&nbsp;</td>
                                                        <td height="21" valign="middle" width="12%">&nbsp;</td>
                                                        <td height="21" valign="middle" width="2%">&nbsp;</td>
                                                        <td height="21" colspan="2" width="84%" class="comment">*)= 
                                                            <%=textListTitle[SESS_LANGUAGE][1]%></td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                        <td height="21" valign="top" width="2%">&nbsp;</td>
                                                        <td height="21" valign="top" width="12%"><%=textListHeader[SESS_LANGUAGE][2]%></td>
                                                        <td height="21" valign="top" width="2%">:</td>
                                                        <td height="21" colspan="2" width="84%"> 
                                                            <% Vector discounttypeid_value = new Vector(1,1);
                                                                                  Vector discounttypeid_key = new Vector(1,1);
                                                                                  String sel_discounttypeid = ""+memberGroup.getDiscountTypeId();
                                                                             discounttypeid_key.add("");
                                                                             discounttypeid_value.add("---Pilih---");
					   
                                                                             Vector list = PstDiscountType.list(0,0,"","");
                                                                                                  if(list!=null&&list.size()>0){
                                                                                                          for(int i=0;i<list.size();i++){
                                                                                                                  DiscountType obj =(DiscountType)list.get(i);
                                                                                                                  discounttypeid_value.add(obj.getCode());
                                                                                                                  discounttypeid_key.add(""+obj.getOID());
                                                                                                          }
                                                                                                  }
                                                            %>
                                                            <%= ControlCombo.draw(frmMemberGroup.fieldNames[FrmMemberGroup.FRM_FIELD_DISCOUNT_TYPE_ID],null, sel_discounttypeid, discounttypeid_key, discounttypeid_value, "", "formElemen") %> 
                                                    <tr align="left" valign="top"> 
                                                        <td height="21" valign="top" width="2%">&nbsp;</td>
                                                        <td height="21" valign="top" width="12%"><%=textListHeader[SESS_LANGUAGE][3]%></td>
                                                        <td height="21" valign="top" width="2%">:</td>
                                                        <td height="21" colspan="2" width="84%"> 
                                                            <% Vector pricetypeid_value= new Vector(1,1);
                                                                                  Vector pricetypeid_key = new Vector(1,1);
                                                                                  String sel_pricetypeid = ""+memberGroup.getPriceTypeId();
                                                                             pricetypeid_key.add("");
                                                                             pricetypeid_value.add("---Pilih---");
					   
                                                                             list = PstPriceType.list(0,0,"","");
                                                                                                  if(list!=null&&list.size()>0){
                                                                                                          for(int i=0;i<list.size();i++){
                                                                                                                  PriceType obj =(PriceType)list.get(i);
                                                                                                                  pricetypeid_value.add(obj.getCode());
                                                                                                                  pricetypeid_key.add(""+obj.getOID());
                                                                                                          }
                                                                                                  }
                                                            %>
                                                            <%= ControlCombo.draw(frmMemberGroup.fieldNames[FrmMemberGroup.FRM_FIELD_PRICE_TYPE_ID],null, sel_pricetypeid, pricetypeid_key, pricetypeid_value, "", "formElemen") %> 
                                                    <tr align="left" valign="top"> 
                                                        <td height="21" valign="top" width="2%">&nbsp;</td>
                                                        <td height="21" valign="top" width="12%"><%=textListHeader[SESS_LANGUAGE][5]%></td>
                                                        <td height="21" valign="top" width="2%">:</td>
                                                        <td height="21" colspan="2" width="84%"> 
                                                            <% Vector grouptype_value= new Vector(1,1);
                                                                                  Vector grouptype_key = new Vector(1,1);
                                                                                  String sel_grouptype = ""+memberGroup.getGroupType();
                                                                             grouptype_key.add("");
                                                                             grouptype_value.add("---Pilih---");					   
					   
                                                                                                  if(PstMemberGroup.typeNames!=null&&PstMemberGroup.typeNames.length>0){
                                                                                                          for(int i=0;i<PstMemberGroup.typeNames.length;i++){										
                                                                                                                  grouptype_value.add(PstMemberGroup.typeNames[i]);
                                                                                                                  grouptype_key.add(""+(i+1));
                                                                                                          }
                                                                                                  }
                                                            %>
                                                            <%= ControlCombo.draw(frmMemberGroup.fieldNames[FrmMemberGroup.FRM_FIELD_GROUP_TYPE],null, sel_grouptype, grouptype_key, grouptype_value, "", "formElemen") %> 
                                                            <!--input type="text" name="<%=frmMemberGroup.fieldNames[FrmMemberGroup.FRM_FIELD_GROUP_TYPE] %>"  value="<%= memberGroup.getGroupType() %>" class="formElemen" size="2" maxlength="2"-->
                                                    <tr align="left" valign="top"> 
                                                        <td height="21" valign="top" width="2%">&nbsp;</td>
                                                        <td height="21" valign="top" width="12%"><%=textListHeader[SESS_LANGUAGE][0]%></td>
                                                        <td height="21" valign="top" width="2%">:</td>
                                                        <td height="21" colspan="2" width="84%"> 
                                                            <input type="text" name="<%=frmMemberGroup.fieldNames[FrmMemberGroup.FRM_FIELD_CODE] %>"  value="<%= memberGroup.getCode() %>" class="formElemen">
                                                            * <%= frmMemberGroup.getErrorMsg(FrmMemberGroup.FRM_FIELD_CODE) %> 
                                                    <tr align="left" valign="top"> 
                                                        <td height="21" valign="top" width="2%">&nbsp;</td>
                                                        <td height="21" valign="top" width="12%"><%=textListHeader[SESS_LANGUAGE][1]%></td>
                                                        <td height="21" valign="top" width="2%">:</td>
                                                        <td height="21" colspan="2" width="84%"> 
                                                            <input type="text" name="<%=frmMemberGroup.fieldNames[FrmMemberGroup.FRM_FIELD_NAME] %>"  value="<%= memberGroup.getName() %>" class="formElemen" size="40">
                                                            * <%= frmMemberGroup.getErrorMsg(FrmMemberGroup.FRM_FIELD_NAME) %> 
                                                    <tr align="left" valign="top"> 
                                                        <td height="21" valign="top" width="2%">&nbsp;</td>
                                                        <td height="21" valign="top" width="12%"><%=textListHeader[SESS_LANGUAGE][4]%></td>
                                                        <td height="21" valign="top" width="2%">:</td>
                                                        <td height="21" colspan="2" width="84%"> 
                                                            <textarea name="<%=frmMemberGroup.fieldNames[FrmMemberGroup.FRM_FIELD_DESCRIPTION] %>" class="formElemen" cols="30"><%= memberGroup.getDescription() %></textarea>
                                                    <tr align="left" valign="top"> 
                                                        <td height="8" valign="middle" width="2%">&nbsp;</td>
                                                        <td height="8" valign="middle" width="12%"><%=textListHeader[SESS_LANGUAGE][5]%></td>
                                                        <td height="8" valign="middle" width="2%">:</td>
                                                        <td height="8" colspan="2" width="84%">&nbsp; </td>
                                                    </tr>
                                                    <%
                                                        Vector tipepoint_value= new Vector(1,1);
                                                        Vector tipepoint_key = new Vector(1,1);

                                                        tipepoint_value.add(""+PstMemberGroup.POINT_IN_CATEGORY);
                                                        tipepoint_key.add(""+PstMemberGroup.typePointNames[PstMemberGroup.POINT_IN_CATEGORY]);
                                                        tipepoint_value.add(""+PstMemberGroup.POINT_ON_TOTAL_SALES);
                                                        tipepoint_key.add(""+PstMemberGroup.typePointNames[PstMemberGroup.POINT_ON_TOTAL_SALES]);

                                                    %>
                                                    <tr align="left" valign="top"> 
                                                        <td height="8" valign="middle" width="2%">&nbsp;</td>
                                                        <td height="8" valign="middle" width="12%"><%=textListHeader[SESS_LANGUAGE][6]%></td>
                                                        <td height="8" valign="middle" width="2%">:</td>
                                                        <td height="8" colspan="2" width="84%">
                                                            
                                                            <%= ControlCombo.draw(frmMemberGroup.fieldNames[FrmMemberGroup.FRM_FIELD_TYPE_POINT],null, ""+memberGroup.getTypePoint(),tipepoint_value,tipepoint_key, "", "formElemen") %> 
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                        <td height="8" valign="middle" width="2%">&nbsp;</td>
                                                        <td height="8" valign="middle" width="12%"><%=textListHeader[SESS_LANGUAGE][7]%> (1 Poin)</td>
                                                        <td height="8" valign="middle" width="2%">:</td>
                                                        <td height="8" colspan="2" width="84%">
                                                            <input type="text" name="<%=frmMemberGroup.fieldNames[FrmMemberGroup.FRM_FIELD_POINT_IN_CALCULATE] %>" id="<%=frmMemberGroup.fieldNames[FrmMemberGroup.FRM_FIELD_POINT_IN_CALCULATE] %>"  value="<%= memberGroup.getPointInCalculate() %>" class="formElemen" size="40"> *
                                                             
                                                        </td>
                                                    </tr>
                                                    <%
                                                        Vector tipeview_value= new Vector(1,1);
                                                        Vector tipeview_key = new Vector(1,1);

                                                        tipeview_value.add(""+PstMemberGroup.VIEW_ON_CATALOG);
                                                        tipeview_key.add(""+PstMemberGroup.viewMemberType[PstMemberGroup.VIEW_ON_CATALOG]);
                                                        tipeview_value.add(""+PstMemberGroup.VIEW_ON_MEMBER);
                                                        tipeview_key.add(""+PstMemberGroup.viewMemberType[PstMemberGroup.VIEW_ON_MEMBER]);
                                                        tipeview_value.add(""+PstMemberGroup.VIEW_ON_MEMBER_AND_CATALOG);
                                                        tipeview_key.add(""+PstMemberGroup.viewMemberType[PstMemberGroup.VIEW_ON_MEMBER_AND_CATALOG]);

                                                    %>
                                                    <tr align="left" valign="top"> 
                                                        <td height="8" valign="middle" width="2%">&nbsp;</td>
                                                        <td height="8" valign="middle" width="12%"><%=textListHeader[SESS_LANGUAGE][8]%></td>
                                                        <td height="8" valign="middle" width="2%">:</td>
                                                        <td height="8" colspan="2" width="84%">
                                                            
                                                            <%= ControlCombo.draw(frmMemberGroup.fieldNames[FrmMemberGroup.FRM_FIELD_VIEW_CUSTOMER_TYPE],null, ""+memberGroup.getViewCustomerType(),tipeview_value,tipeview_key, "", "formElemen") %> 
                                                        </td>
                                                    </tr>
                                                    
                                                    <tr align="left" valign="top" > 
                                                        <td colspan="5" class="command"> 
                                                            <%
                                                                                          ctrLine.setLocationImg(approot+"/images");
                                                                                          ctrLine.initDefault(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0]);
                                                                                          ctrLine.setTableWidth("100%");
                                                                                          ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_SAVE,true));
                                                                                          ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true)+" List");
                                                                                          ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ASK,true));
                                                                                          ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_CANCEL,false));
							
                                                                                          String scomDel = "javascript:cmdAsk('"+oidMemberGroup+"')";
                                                                                          String sconDelCom = "javascript:cmdConfirmDelete('"+oidMemberGroup+"')";
                                                                                          String scancel = "javascript:cmdEdit('"+oidMemberGroup+"')";
                                                                                          //ctrLine.setBackCaption("Back to List");
                                                                                          //ctrLine.setCommandStyle("buttonlink");
                                                                                                  //ctrLine.setDeleteCaption("Delete");
                                                                                                  //ctrLine.setSaveCaption("Save");
                                                                                                  //ctrLine.setAddCaption("");
								
                                                                                          ctrLine.setCommandStyle("command");
                                                                                          ctrLine.setColCommStyle("command");
							
                                                                                          // set command caption
                                                                                          ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true));
                                                                                          ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_SAVE,true));
                                                                                          ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true)+" List");
                                                                                          ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ASK,true));
                                                                                          ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_DELETE,true));
                                                                                          ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_CANCEL,false));
	
                                                                                          if (privDelete){
                                                                                                  ctrLine.setConfirmDelCommand(sconDelCom);
                                                                                                  ctrLine.setDeleteCommand(scomDel);
                                                                                                  ctrLine.setEditCommand(scancel);
                                                                                          }else{ 
                                                                                                  ctrLine.setConfirmDelCaption("");
                                                                                                  ctrLine.setDeleteCaption("");
                                                                                                  ctrLine.setEditCaption("");
                                                                                          }
	
                                                                                          if(iCommand == Command.EDIT && privUpdate == false){
                                                                                                  ctrLine.setSaveCaption("");
                                                                                          }
	
                                                                                          if (privAdd == false){
                                                                                                  ctrLine.setAddCaption("");
                                                                                          }
                                                            %>
                                                            <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%> </td>
                                                    </tr>
                                                </table>
                                                <%}%>
                                            </td>
                                        </tr>
                                    </table>
                                </form>
                                <%
                                if(iCommand==Command.ADD || iCommand==Command.EDIT)
                                {
                                %>
                                <script language="javascript">
                                    document.frmmembergroup.<%=FrmMemberGroup.fieldNames[FrmMemberGroup.FRM_FIELD_DISCOUNT_TYPE_ID]%>.focus();
                                </script>
                                <%
                                }
                                %>												
                                <!-- #EndEditable --></td> 
                        </tr> 
                    </table>
                </td>
            </tr>
            <tr> 
                <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
                    <%if(menuUsed == MENU_ICON){%>
                    <%@include file="../styletemplate/footer.jsp" %>

                    <%}else{%>
                    <%@ include file = "../main/footer.jsp" %>
                    <%}%>
                    <!-- #EndEditable --> </td>
            </tr>
        </table>
        <script type="text/javascript" src="../styles/bootstrap/js/jquery.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
                $("#TYPE_POINT").change(function(){
                    var value = $(this).val();

                    if (value=="0"){
                        //alert("kesini");
                        $("#<%=frmMemberGroup.fieldNames[FrmMemberGroup.FRM_FIELD_POINT_IN_CALCULATE]%>").val("1");
                        $("#<%=frmMemberGroup.fieldNames[FrmMemberGroup.FRM_FIELD_POINT_IN_CALCULATE]%>").attr("readonly","true");
                    }else{
                        $("#<%=frmMemberGroup.fieldNames[FrmMemberGroup.FRM_FIELD_POINT_IN_CALCULATE]%>").val("");
                        $("#<%=frmMemberGroup.fieldNames[FrmMemberGroup.FRM_FIELD_POINT_IN_CALCULATE]%>").removeAttr("readonly");
                        //alert("atau ini");
                    }
                });
            });
        </script>
    </body>
    <!-- #EndTemplate --></html>
