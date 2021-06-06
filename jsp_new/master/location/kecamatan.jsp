<%-- 
    Document   : kabupaten
    Created on : Jan 24, 2014, 3:45:09 PM
    Author     : Fitra
--%>



<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package prochain -->
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.form.location.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_AREA); %>
<%@ include file = "../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%! public static final String textListTitleHeader[][] =
{
	{"Lokasi Kecamatan"},
	{"Area"}
};  

public static final String textListTitleMainHeader[][] =
{
    {"Tambah","Ubah","Kecamatan","Kembali ke Daftar Kecamatan","Simpan Kecamatan","Negara","Provinsi","Kabupaten/Kota","Kode Kecamatan","Kecamatan"},
    {"Add","Edit","Sub Regency","Back to Sub Regency List","Save Sub Regency","Country","Province","Regency","Sub Regency Code","Sub Regency"}
};  

public String drawList(int language, Vector objectClass, long idKecamatan) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("60%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader("Regency", "25%");
        ctrlist.addHeader("Sub.Reg Code", "25%");
        ctrlist.addHeader("Sub Regency", "50%");

        ctrlist.setLinkRow(0);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        int index = -1;

        for (int i = 0; i < objectClass.size(); i++) {
            Kecamatan kecamatan = (Kecamatan) objectClass.get(i);
            Vector rowx = new Vector();
            if (idKecamatan == kecamatan.getOID()) {
                index = i;
            }

            Kabupaten kabupaten = new Kabupaten();
            try {
                kabupaten = PstKabupaten.fetchExc(kecamatan.getIdKabupaten());
            } catch (Exception e) {
            }

            rowx.add(kabupaten.getNmKabupaten());
            rowx.add(kecamatan.getKdKecamatan());
            rowx.add(kecamatan.getNmKecamatan());

            lstData.add(rowx);
            lstLinkData.add(String.valueOf(kecamatan.getOID()));
        }

        //return ctrlist.drawList(index);

        return ctrlist.draw(index);
    }

%>
<%
            int iCommand = FRMQueryString.requestCommand(request);
            int start = FRMQueryString.requestInt(request, "start");
            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            long oidKecamatan = FRMQueryString.requestLong(request, "hidden_kecamatan_id");
            long oidNegara = FRMQueryString.requestLong(request, FrmKecamatan.fieldNames[FrmKecamatan.FRM_FIELD_ID_NEGARA]);
            long oidProvinsi = FRMQueryString.requestLong(request, FrmKecamatan.fieldNames[FrmKecamatan.FRM_FIELD_ID_PROPINSI]);
            long oidKabupaten = FRMQueryString.requestLong(request, FrmKecamatan.fieldNames[FrmKecamatan.FRM_FIELD_ID_KABUPATEN]);

            /*variable declaration*/
            int recordToGet = 10;
            String msgString = "";
            int iErrCode = FRMMessage.NONE;
            String whereClause = "";
            String orderClause = "k." + PstKabupaten.fieldNames[PstKabupaten.FLD_NM_KABUPATEN] + ", kec." + PstKecamatan.fieldNames[PstKecamatan.FLD_NM_KECAMATAN];
             String locationTitle = textListTitleHeader[SESS_LANGUAGE][0];
            CtrlKecamatan ctrlKecamatan = new CtrlKecamatan(request);

            ControlLine ctrLine = new ControlLine();
            Vector listKecamatan = new Vector(1, 1);

            /*switch statement */
            iErrCode = ctrlKecamatan.action(iCommand, oidKecamatan);
            /* end switch*/
            FrmKecamatan frmKecamatan = ctrlKecamatan.getForm();

            /*count list All Kecamatan*/
            int vectSize = PstKecamatan.getCount(whereClause);

            Kecamatan kecamatan = ctrlKecamatan.getKecamatan();
            //Provinsi provinsi = ctrlProvinsi.getPropinsi();
            msgString = ctrlKecamatan.getMessage();
            int commandRefresh = FRMQueryString.requestInt(request, "commandRefresh");
            if (commandRefresh == 1) {
                kecamatan.setIdNegara(oidNegara);
                kecamatan.setIdPropinsi(oidProvinsi);
                kecamatan.setIdKabupaten(oidKabupaten);
            }

            /*switch list Kecamatan*/
            if ((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE)) {
                start = PstKecamatan.findLimitStart(kecamatan.getOID(), recordToGet, whereClause, orderClause);
                oidKecamatan = kecamatan.getOID();
            }

            if ((iCommand == Command.FIRST || iCommand == Command.PREV)
                    || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                start = ctrlKecamatan.actionList(iCommand, start, vectSize, recordToGet);
            }
            /* end switch list*/

            /* get record to display */
            listKecamatan = PstKecamatan.listJoinKec(start, recordToGet, whereClause, orderClause);

            /*handle condition if size of record to display = 0 and start > 0 	after delete*/
            if (listKecamatan.size() < 1 && start > 0) {
                if (vectSize - recordToGet > recordToGet) {
                    start = start - recordToGet;   //go to Command.PREV
                } else {
                    start = 0;
                    iCommand = Command.FIRST;
                    prevCommand = Command.FIRST; //go to Command.FIRST
                }
                listKecamatan = PstKecamatan.list(start, recordToGet, whereClause, orderClause);
            }




%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">


/*------------- start location function ---------------*/
  function cmdUpdateKec(){
                document.frmkecamatan.command.value="<%=iCommand%>";
                document.frmkecamatan.commandRefresh.value= "1";
                document.frmkecamatan.action="kecamatan.jsp";
                document.frmkecamatan.submit();
            }



            function cmdAdd(){
                document.frmkecamatan.hidden_kecamatan_id.value="0";
                document.frmkecamatan.command.value="<%=Command.ADD%>";
                document.frmkecamatan.prev_command.value="<%=prevCommand%>";
                document.frmkecamatan.action="kecamatan.jsp";
                document.frmkecamatan.submit();
            }

            function cmdAsk(oidKecamatan){
                document.frmkecamatan.hidden_kecamatan_id.value=oidKecamatan;
                document.frmkecamatan.command.value="<%=Command.ASK%>";
                document.frmkecamatan.prev_command.value="<%=prevCommand%>";
                document.frmkecamatan.action="kecamatan.jsp";
                document.frmkecamatan.submit();
            }

            function cmdConfirmDelete(oidKecamatan){
                document.frmkecamatan.hidden_kecamatan_id.value=oidKecamatan;
                document.frmkecamatan.command.value="<%=Command.DELETE%>";
                document.frmkecamatan.prev_command.value="<%=prevCommand%>";
                document.frmkecamatan.action="kecamatan.jsp";
                document.frmkecamatan.submit();
            }
            function cmdSave(){
                document.frmkecamatan.command.value="<%=Command.SAVE%>";
                document.frmkecamatan.prev_command.value="<%=prevCommand%>";
                document.frmkecamatan.action="kecamatan.jsp";
                document.frmkecamatan.submit();
            }

            function cmdEdit(oidKecamatan){
                document.frmkecamatan.hidden_kecamatan_id.value=oidKecamatan;
                document.frmkecamatan.command.value="<%=Command.EDIT%>";
                document.frmkecamatan.prev_command.value="<%=prevCommand%>";
                document.frmkecamatan.action="kecamatan.jsp";
                document.frmkecamatan.submit();
            }

            function cmdCancel(oidKecamatan){
                document.frmkecamatan.hidden_kecamatan_id.value=oidKecamatan;
                document.frmkecamatan.command.value="<%=Command.EDIT%>";
                document.frmkecamatan.prev_command.value="<%=prevCommand%>";
                document.frmkecamatan.action="kecamatan.jsp";
                document.frmkecamatan.submit();
            }

            function cmdBack(){
                document.frmkecamatan.command.value="<%=Command.BACK%>";
                document.frmkecamatan.action="kecamatan.jsp";
                document.frmkecamatan.submit();
            }

            function cmdListFirst(){
                document.frmkecamatan.command.value="<%=Command.FIRST%>";
                document.frmkecamatan.prev_command.value="<%=Command.FIRST%>";
                document.frmkecamatan.action="kecamatan.jsp";
                document.frmkecamatan.submit();
            }

            function cmdListPrev(){
                document.frmkecamatan.command.value="<%=Command.PREV%>";
                document.frmkecamatan.prev_command.value="<%=Command.PREV%>";
                document.frmkecamatan.action="kecamatan.jsp";
                document.frmkecamatan.submit();
            }

            function cmdListNext(){
                document.frmkecamatan.command.value="<%=Command.NEXT%>";
                document.frmkecamatan.prev_command.value="<%=Command.NEXT%>";
                document.frmkecamatan.action="kecamatan.jsp";
                document.frmkecamatan.submit();
            }

            function cmdListLast(){
                document.frmkecamatan.command.value="<%=Command.LAST%>";
                document.frmkecamatan.prev_command.value="<%=Command.LAST%>";
                document.frmkecamatan.action="kecamatan.jsp";
                document.frmkecamatan.submit();
            }

           
          
            

            

/*------------- end vendor price function -----------------*/


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
<%if(menuUsed == MENU_ICON){%>
    <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>

<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
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
            Masterdata &gt; <%=locationTitle%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
             <form name="frmkecamatan" method ="post" action="">
            <input type="hidden" name="command" value="<%=iCommand%>">
            <input type="hidden" name="vectSize" value="<%=vectSize%>">
            <input type="hidden" name="start" value="<%=start%>">
            <input type="hidden" name="prev_command" value="<%=prevCommand%>">
            <input type="hidden" name="commandRefresh" value= "0">
            <input type="hidden" name="hidden_kecamatan_id" value="<%=oidKecamatan%>">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr align="left" valign="top"> 
					<td height="8" valign="middle" colspan="3"> 
					  <hr size="1">
					</td>
                                        
				</tr>                							  
                <tr align="left" valign="top"> 
                  <td height="8"  colspan="3"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top"> 
                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar "+locationTitle : locationTitle+" List"%></u></td>                      
					  </tr>
                      <tr align="left" valign="top"> 
                        <td height="22" valign="middle" colspan="3"> <%= drawList(SESS_LANGUAGE,listKecamatan, oidKecamatan)%> </td>
                      </tr>
                      <tr align="left" valign="top"> 
                        <td height="8" align="left" colspan="3" class="command"> 
                          <span class="command"> 
                          <% 
						  int cmd = 0;
						  if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
							  cmd =iCommand; 
						  }else{
							  if(iCommand==Command.NONE || prevCommand==Command.NONE)
								cmd = Command.FIRST;
							  else 
								cmd =prevCommand; 
						  } 						  
                          ctrLine.setLocationImg(approot+"/images");
						  ctrLine.initDefault();							
                          out.println(ctrLine.drawImageListLimit(cmd, vectSize, start, recordToGet));
						  %> 
						  </span>
						</td>
                      </tr>
					  <%
					     if(iCommand==Command.LIST || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand == Command.LAST
						   || iCommand==Command.NONE
						   || iCommand==Command.BACK 
						   || (iCommand==Command.SAVE&&iErrCode==0) || (iCommand==Command.DELETE && iErrCode==0)){
					  %>					  
                      <tr align="left" valign="top"> 
                        <td> 
                          <%if(((iCommand!=Command.ADD)&&(iCommand!=Command.EDIT)&&(iCommand!=Command.ASK))||(iCommand==Command.SAVE&&iErrCode==0) || (iCommand==Command.DELETE && iErrCode==0)){%>
                          <table cellpadding="0" cellspacing="0" border="0">
                            <tr> <br>
                              <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                              <!--td width="24"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,locationTitle,ctrLine.CMD_ADD,true)%>"></a></td>
                              <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td-->
                              <%if(privAdd){%>
                              <td height="22" valign="middle" colspan="3" ><a href="javascript:cmdAdd()" class="btn-lg btn-primary"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,locationTitle,ctrLine.CMD_ADD,true)%></a></td>
                              <%}%>
                            </tr>
                          </table>
                          <%}%>
                        </td>
                      </tr>
					  <%}%>					  
                    </table>
                  </td>
                </tr>
				
                <tr align="left" valign="top"> 
                  <td height="8" valign="middle" colspan="3"> 
				  
                    <%
					  if((iCommand ==Command.ADD)
						||(iCommand==Command.EDIT)
						||(iCommand==Command.ASK)					  
					    ||((iCommand==Command.SAVE) && iErrCode>0) || (iCommand==Command.DELETE && iErrCode>0))
					  {
					%>
                    <table width="100%" border="0" cellspacing="2" cellpadding="2">
                        <tr>
                            <td><b class="listtitle"><%=oidKecamatan == 0 ? ""+textListTitleMainHeader[SESS_LANGUAGE][0]+"" : ""+textListTitleMainHeader[SESS_LANGUAGE][1]+" "%> <%=textListTitleMainHeader[SESS_LANGUAGE][2]%></b></td>
                        </tr>
                        <tr>
                            <td height="100%">
                                <table border="0" cellspacing="2" cellpadding="2" width="50%">
                                    <tr align="left" valign="top">
                                        <td valign="top" width="18%">&nbsp;</td>
                                        <td width="82%" class="comment">*)entry required </td>
                                    </tr>
                                    <tr align="left" valign="top">
                                        <td valign="top" width="18%"><%=textListTitleMainHeader[SESS_LANGUAGE][5] %></td>
                                        <td width="82%"><%
                                            Vector neg_value = new Vector(1, 1);
                                            Vector neg_key = new Vector(1, 1);
                                            neg_value.add("0");
                                            neg_key.add("select ...");
                                            Vector listNeg = PstNegara.list(0, 0, "", " NAMA_NGR ");
                                            for (int i = 0; i < listNeg.size(); i++) {
                                                Negara neg = (Negara) listNeg.get(i);
                                                neg_key.add(neg.getNmNegara());
                                                neg_value.add(String.valueOf(neg.getOID()));
                                            }
                                            %>
                                            <%= ControlCombo.draw(frmKecamatan.fieldNames[FrmKecamatan.FRM_FIELD_ID_NEGARA], "formElemen", null, "" + kecamatan.getIdNegara(), neg_value, neg_key, "onChange=\"javascript:cmdUpdateKec()\"")%>
                                        </td>
                                    </tr>
                                    <tr align="left" valign="top">
                                        <td valign="top" width="18%"><%=textListTitleMainHeader[SESS_LANGUAGE][6] %></td>
                                        <td width="82%"><%
                                            Vector pro_value = new Vector(1, 1);
                                            Vector pro_key = new Vector(1, 1);
                                            Provinsi pro = new Provinsi();
                                            pro_value.add("0");
                                            pro_key.add("select ...");
                                            //Vector listPro = PstProvinsi.list(0, 0, "", " NAMA_PROP ");
                                            String strWhere = PstProvinsi.fieldNames[PstProvinsi.FLD_ID_NEGARA] + "=" +kecamatan.getIdNegara();
                                            Vector listPro = PstProvinsi.list(0, 0, strWhere, "NAMA_PROP");
                                            for (int i = 0; i < listPro.size(); i++) {
                                                Provinsi prov = (Provinsi) listPro.get(i);
                                                pro_key.add(prov.getNmProvinsi());
                                                pro_value.add(String.valueOf(prov.getOID()));
                                            }
                                            %>
                                            <%= ControlCombo.draw(frmKecamatan.fieldNames[FrmKecamatan.FRM_FIELD_ID_PROPINSI], "formElemen", null, "" + kecamatan.getIdPropinsi(), pro_value, pro_key, "onChange=\"javascript:cmdUpdateKec()\"")%>
                                        </td>
                                    </tr>
                                    <tr align="left" valign="top">
                                        <td valign="top" width="18%"><%=textListTitleMainHeader[SESS_LANGUAGE][7] %></td>
                                        <td width="82%"><%
                                            Vector kab_value = new Vector(1, 1);
                                            Vector kab_key = new Vector(1, 1);
                                            kab_value.add("0");
                                            kab_key.add("select ...");
                                            //Vector listKab = PstKabupaten.list(0, 0, "", " NAMA_KAB ");
                                            String strWhereKab = PstKabupaten.fieldNames[PstKabupaten.FLD_ID_PROPINSI] + "=" + kecamatan.getIdPropinsi();
                                            Vector listKab = PstKabupaten.list(0, 0, strWhereKab, "NAMA_KAB");
                                            for (int i = 0; i < listKab.size(); i++) {
                                                Kabupaten kab = (Kabupaten) listKab.get(i);
                                                kab_key.add(kab.getNmKabupaten());
                                                kab_value.add(String.valueOf(kab.getOID()));
                                            }
                                            %> <%= ControlCombo.draw(frmKecamatan.fieldNames[FrmKecamatan.FRM_FIELD_ID_KABUPATEN], "formElemen", null, "" + kecamatan.getIdKabupaten(), kab_value, kab_key, "onChange=\"javascript:cmdUpdateKec()\"")%>
                                        </td>
                                    </tr>
                                    <tr align="left" valign="top">
                                        <td valign="top" width="18%"><%=textListTitleMainHeader[SESS_LANGUAGE][8] %></td>
                                        <td width="82%">
                                            <input type="text" name="<%=frmKecamatan.fieldNames[FrmKecamatan.FRM_FIELD_KD_KECAMATAN]%>"  value="<%= kecamatan.getKdKecamatan()%>" class="elemenForm" size="35">
                                            * <%=frmKecamatan.getErrorMsg(FrmKecamatan.FRM_FIELD_KD_KECAMATAN)%>
                                        </td>
                                    </tr>
                                    <tr align="left" valign="top">
                                        <td valign="top" width="18%"> <%=textListTitleMainHeader[SESS_LANGUAGE][9] %></td>
                                        <td width="82%">
                                            <input type="text" name="<%=frmKecamatan.fieldNames[FrmKecamatan.FRM_FIELD_NM_KECAMATAN]%>"  value="<%= kecamatan.getNmKecamatan()%>" class="elemenForm" size="35">
                                            * <%=frmKecamatan.getErrorMsg(FrmKecamatan.FRM_FIELD_NM_KECAMATAN)%></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr align="left" valign="top" >
                            <td class="command">
                                <%
                                    ctrLine.setLocationImg(approot + "/images");
                                    ctrLine.initDefault();
                                    ctrLine.setTableWidth("80%");
                                    String scomDel = "javascript:cmdAsk('" + oidKecamatan + "')";
                                    String sconDelCom = "javascript:cmdConfirmDelete('" + oidKecamatan + "')";
                                    String scancel = "javascript:cmdEdit('" + oidKecamatan + "')";
                                    ctrLine.setBackCaption("Back to List");
                                    ctrLine.setCommandStyle("command");

                                    ctrLine.setColCommStyle("command");
                                    ctrLine.setBackCaption(""+textListTitleMainHeader[SESS_LANGUAGE][3]+"");
                                    ctrLine.setSaveCaption(""+textListTitleMainHeader[SESS_LANGUAGE][4]+"");
                                    ctrLine.setConfirmDelCaption("Yes Delete Sub Regency");
                                    ctrLine.setDeleteCaption("Delete Sub Regency");

                                    if (privDelete) {
                                        ctrLine.setConfirmDelCommand(sconDelCom);
                                        ctrLine.setDeleteCommand(scomDel);
                                        ctrLine.setEditCommand(scancel);
                                    } else {
                                        ctrLine.setConfirmDelCaption("");
                                        ctrLine.setDeleteCaption("");
                                        ctrLine.setEditCaption("");
                                    }

                                    if (iCommand == Command.EDIT && privUpdate == false) {
                                        ctrLine.setSaveCaption("");
                                    }

                                    if (privAdd == false) {
                                        ctrLine.setAddCaption("");
                                    }

                                    if (iCommand == Command.ASK) {
                                        ctrLine.setDeleteQuestion(msgString);
                                    }
                                %>
                                <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%>
                            </td>
                        </tr>
                        </table>      <%}%>
					
                  </td>
                </tr>
              </table>
            </form>
            <!-- #EndEditable -->
          </td> 
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

