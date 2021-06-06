<%@ page import="com.dimata.posbo.entity.search.SrcMemberReg,
                 com.dimata.posbo.form.search.FrmSrcMemberReg,
                 com.dimata.posbo.session.masterdata.SessMemberReg,
                 com.dimata.posbo.entity.masterdata.PstMemberGroup,
                 com.dimata.posbo.entity.masterdata.MemberGroup,
                 com.dimata.posbo.entity.masterdata.PstMemberReg,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.harisma.entity.masterdata.PstReligion,
                 com.dimata.harisma.entity.masterdata.Religion,
                 com.dimata.gui.jsp.ControlDate"%>
<%
/* 
 * Page Name  		:  srcmemberreg.jsp
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

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_MEMBER_POINT); %>
<%@ include file = "../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%
int iCommand = FRMQueryString.requestCommand(request);

SrcMemberReg srcMemberReg = new SrcMemberReg();
    FrmSrcMemberReg frmSrcMemberReg = new FrmSrcMemberReg(request,srcMemberReg);
    try{
        if (iCommand==Command.SUBMIT){
            frmSrcMemberReg.requestEntityObject(srcMemberReg);
            srcMemberReg = frmSrcMemberReg.getEntityObject();
            session.putValue(SessMemberReg.SESS_SRC_MEMBERREG,srcMemberReg);
            response.sendRedirect(""+approot+"/masterdata/memberreg_list_point.jsp?command="+Command.LIST+"");
        }else{
            if(session.getValue(SessMemberReg.SESS_SRC_MEMBERREG)!=null){
                srcMemberReg = (SrcMemberReg)session.getValue(SessMemberReg.SESS_SRC_MEMBERREG);
            }
        }
    }catch(Exception e){
        srcMemberReg = new SrcMemberReg();
    }
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdSearch(){
	document.frmsrcmemberreg.command.value="<%=Command.SUBMIT%>";
	document.frmsrcmemberreg.action="srcmemberreg_point.jsp";
	document.frmsrcmemberreg.submit();
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

function MM_swapImage() { //v3.0
		var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
		if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
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
<!--
function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}
//-->
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
            Membership > Cari Member untuk masukkan point member <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmsrcmemberreg" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <table width="100%" border="0" cellspacing="2" cellpadding="2">
				<tr align="left" valign="top"> 
				  <td colspan="4" valign="middle"><hr size="1"></td> 
				</tr>								  
                <tr align="left" valign="top"> 
                  <td height="21" valign="middle" width="2%">&nbsp;</td>
                  <td height="21" valign="middle" width="14%">&nbsp;</td>
                  <td height="21" width="2%">&nbsp;</td>
                  <td width="82%" height="21"><span></span></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="21" valign="top" width="2%">&nbsp;</td>
                  <td height="21" valign="top" width="14%">Barcode</td>
                  <td height="21" width="2%">:</td>
                  <td height="21" width="82%"> 
                    <input type="text" name="<%=frmSrcMemberReg.fieldNames[FrmSrcMemberReg.FRM_FIELD_BARCODE] %>"  value="<%= srcMemberReg.getBarcode() %>" class="formElemen"> 
                  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="21" valign="top" width="2%">&nbsp;</td>
                  <td height="21" valign="top" width="14%">Nama</td>
                  <td height="21" width="2%">:</td>
                  <td height="21" width="82%"> 
                    <input type="text" name="<%=frmSrcMemberReg.fieldNames[FrmSrcMemberReg.FRM_FIELD_NAME] %>"  value="<%= srcMemberReg.getName() %>" class="formElemen" size="30">
                  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="21" valign="top" width="2%">&nbsp;</td>
                  <td height="21" valign="top" width="14%">Tipe</td>
                  <td height="21" width="2%">:</td>
                  <td height="21" width="82%"> 
                    <%	Vector groupmember_value = new Vector(1,1);
						Vector groupmember_key = new Vector(1,1);
						groupmember_value.add("0");
						groupmember_key.add("Semua.....");
					 	String selectValue = ""+srcMemberReg.getGroupmember();
						String ordGrpMember = " "+PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE]+", "+PstMemberGroup.fieldNames[PstMemberGroup.FLD_NAME]+", "+PstMemberGroup.fieldNames[PstMemberGroup.FLD_CODE];
						Vector listGroupMember = PstMemberGroup.list(0,0,"",ordGrpMember);
						if(listGroupMember!=null&&listGroupMember.size()>0){
							for(int i=0;i<listGroupMember.size();i++){
								MemberGroup memGroup = (MemberGroup)listGroupMember.get(i);
								groupmember_value.add(""+memGroup.getOID());
								groupmember_key.add(memGroup.getCode());
							}
						}
					  %>
                    <%= ControlCombo.draw(frmSrcMemberReg.fieldNames[FrmSrcMemberReg.FRM_FIELD_GROUPMEMBER],"formElemen", null, selectValue, groupmember_value, groupmember_key) %> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="21" valign="top" width="2%">&nbsp;</td>
                  <td height="21" valign="top" width="14%">Agama</td>
                  <td height="21" width="2%">:</td>
                  <td height="21" width="82%"> 
                    <% Vector memberreligionid_value = new Vector(1,1);
						Vector memberreligionid_key = new Vector(1,1);
					 	String sel_memberreligionid = ""+srcMemberReg.getReligion();
					   memberreligionid_key.add("0");
					   memberreligionid_value.add("Semua.....");
					   Vector listReligion = PstReligion.list(0,0,"","");
					   if(listReligion!=null&&listReligion.size()>0){
					   	for(int i=0;i<listReligion.size();i++){
							Religion religion = (Religion)listReligion.get(i);
							memberreligionid_key.add(""+religion.getOID());
					   		memberreligionid_value.add(religion.getReligion());
						}
					   }
					   
					   %>
                    <%= ControlCombo.draw(frmSrcMemberReg.fieldNames[FrmSrcMemberReg.FRM_FIELD_RELIGION],null, sel_memberreligionid, memberreligionid_key, memberreligionid_value, "", "formElemen") %> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="21" valign="top" width="2%">&nbsp;</td>
                  <td height="21" valign="top" width="14%">Tanggal Lahir</td>
                  <td height="21" width="2%">:</td>
                  <td height="21" width="82%"> 
                    <input type="checkbox" name="<%=frmSrcMemberReg.fieldNames[FrmSrcMemberReg.FRM_FIELD_ALL_BIRTHDATE]%>" value="true" <%if(srcMemberReg.isAllBirthDate()){%>checked<%}%>>
                    Semua tanggal lahir</td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="21" valign="top" width="2%">&nbsp;</td>
                  <td height="21" valign="top" width="14%">&nbsp;</td>
                  <td height="21" width="2%">&nbsp;</td>
                  <td height="21" width="82%"> <%=ControlDate.drawDateWithStyle(frmSrcMemberReg.fieldNames[FrmSrcMemberReg.FRM_FIELD_BIRTH_DATE_FROM], srcMemberReg.getBirthDateFrom()==null?new Date():srcMemberReg.getBirthDateFrom(), 1,-70, "formElemen", "") %> sampai <%=	ControlDate.drawDateWithStyle(frmSrcMemberReg.fieldNames[FrmSrcMemberReg.FRM_FIELD_BIRTH_DATE_TO], srcMemberReg.getBirthDateTo()==null?new Date():srcMemberReg.getBirthDateTo(), 1,-70, "formElemen", "") %> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="21" valign="top" width="2%">&nbsp;</td>
                  <td height="21" valign="top" width="14%">Tanggal Registrasi</td>
                  <td height="21" width="2%">:</td>
                  <td height="21" width="82%"> 
                    <input type="checkbox" name="<%=frmSrcMemberReg.fieldNames[FrmSrcMemberReg.FRM_FIELD_ALL_REGDATE]%>" value="true" <%if(srcMemberReg.isAllRegDate()){%>checked<%}%>>
                    Semua tanggal registrasi</td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="21" valign="top" width="2%">&nbsp;</td>
                  <td height="21" valign="top" width="14%">&nbsp;</td>
                  <td height="21" width="2%">&nbsp;</td>
                  <td height="21" width="82%"> <%=	ControlDate.drawDateWithStyle(frmSrcMemberReg.fieldNames[FrmSrcMemberReg.FRM_FIELD_REG_DATE_FROM], srcMemberReg.getRegDateFrom()==null?new Date():srcMemberReg.getRegDateFrom(), 2,-5, "formElemen", "") %> sampai <%=	ControlDate.drawDateWithStyle(frmSrcMemberReg.fieldNames[FrmSrcMemberReg.FRM_FIELD_REG_DATE_TO], srcMemberReg.getRegDateTo()==null?new Date():srcMemberReg.getRegDateTo(), 2,-5, "formElemen", "") %></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="21" valign="top" width="2%">&nbsp;</td>
                  <td height="21" valign="top" width="14%">Status</td>
                  <td height="21" width="2%">:</td>
                  <td height="21" width="82%"> 
                    <%
Vector status_value = new Vector(1,1);
Vector status_key = new Vector(1,1);
String selValueStatus = ""+(-1);//""+srcMemberReg.getStatus();
status_key.add("Semua.....");
status_value.add(""+(-1));
for(int i=0;i<PstMemberReg.statusNames.length;i++){
    status_key.add(PstMemberReg.statusNames[SESS_LANGUAGE][i]);
    status_value.add(""+i);
}   
					  %>
                    <%= ControlCombo.draw(frmSrcMemberReg.fieldNames[FrmSrcMemberReg.FRM_FIELD_STATUS],"formElemen", null, selValueStatus, status_value, status_key) %> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="21" valign="top" width="2%">&nbsp;</td>
                  <td height="21" valign="top" width="14%">Urut berdasarkan</td>
                  <td height="21" width="2%">:</td>
                  <td height="21" width="82%"> 
                    <%
Vector sortby_value = new Vector(1,1);
Vector sortby_key = new Vector(1,1);
selValueStatus = ""+srcMemberReg.getSortBy();
//sortby_key.add("Semua.....");
//sortby_value.add(""+(-1));
for(int i=0;i<SrcMemberReg.sortByName.length;i++){
    sortby_key.add(SrcMemberReg.sortByName[SESS_LANGUAGE][i]);
    sortby_value.add(""+i);
}   
					  %>
                    <%= ControlCombo.draw(frmSrcMemberReg.fieldNames[FrmSrcMemberReg.FRM_FIELD_SORT_BY],"formElemen", null, selValueStatus, sortby_value, sortby_key) %></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="21" valign="top" width="2%">&nbsp;</td>
                  <td height="21" valign="top" width="14%">&nbsp;</td>
                  <td height="21" width="2%">&nbsp;</td>
                  <td height="21" width="82%">&nbsp;</td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="21" valign="top" width="2%">&nbsp;</td>
                  <td height="21" valign="top" width="14%">&nbsp;</td>
                  <td height="21" width="2%">&nbsp;</td>
                  <td height="21" width="82%"> 
                    <table width="40%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <!--td nowrap width="7%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Cari Member"></a></td-->
                        <td class="command" nowrap width="93%"><a class="btn btn-lg btn-primary" href="javascript:cmdSearch()"><i class="fa fa-search"></i> Cari 
                          Member</a></td>
                        <% if(privAdd){%>
                        <%}%>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr> 
                  <td width="2%">&nbsp;</td>
                  <td width="14%">&nbsp;</td>
                  <td width="2%" class="command">&nbsp;</td>
                  <td width="82%" class="command"> </td>
                </tr>
                <tr> 
                  <td height="21" valign="top" width="2%">&nbsp;</td>
                  <td height="21" valign="top" width="14%">&nbsp;</td>
                  <td width="2%">&nbsp;</td>
                  <td width="82%">&nbsp;</td>
                </tr>
              </table>
            </form>
            <%//@ include file = "../main/menumain.jsp" %>
            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
       <%
        if(menuUsed == MENU_ICON){%>
            <%@include file="../styletemplate/footer.jsp" %>

        <%}else{%>
            <%@ include file = "../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
