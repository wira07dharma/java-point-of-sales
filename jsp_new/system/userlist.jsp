<%@page import="com.dimata.common.entity.custom.DataCustom"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.lowagie.text.pdf.codec.Base64"%>
<%
/*
 * userlist.jsp
 *
 * Created on April 04, 2002, 11:30 AM
 *
 * @author  ktanjana
 * @version 
 */
%>
<%@ page language="java" %>
<%@ page import = "java.util.*,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.util.Command
                   ,
                   com.dimata.posbo.entity.admin.PstAppUser,
                   com.dimata.posbo.form.admin.CtrlAppUser,
                   com.dimata.posbo.entity.admin.AppUser" %>
				   
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_ADMIN , AppObjInfo.G2_ADMIN_USER, AppObjInfo.OBJ_ADMIN_USER_USER); %>
<%@ include file = "../main/checkuser.jsp" %>
<%
   /* privView = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_VIEW));
    privAdd = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
    privUpdate = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
    privDelete = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
    if(!privView){
        response.sendRedirect(approot + "/homepage.jsp");
    * setelah nikah kehidupanku aman2 aja sampai saat ini, namun kdng2 kesel itu kan selalu ada seperti perbedaan pendapat.
 *  * pernah kejadian
    }*/
%>

<!-- JSP Block -->
<%!
public static final String textListTitleHeader[][] =
{
    {"Sistem","Daftar Pemakai","ID Pemakai","Nama Lengkap","Status","Tambah Pemakai Baru","Status Sidik Jari"},
    {"System","User LIst","User ID","Full Name","Status","Add New User","Finger Data Status"}
};

public static final String statusFingData[][]=
{
    {"Belum Terdaftar","Terdaftar"},
    {"Unregistered","Registered"}
};

%>
<%

    /* VARIABLE DECLARATION */
    int recordToGet = 10;

    String order = " " + PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID];

    Vector listAppUser = new Vector(1,1);
    ControlLine ctrLine = new ControlLine();

    /* GET REQUEST FROM HIDDEN TEXT */
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start"); 
    long appUserOID = FRMQueryString.requestLong(request,"user_oid");
    int listCommand = FRMQueryString.requestInt(request, "list_command");
    if(listCommand==Command.NONE)
     listCommand = Command.LIST;

	
    CtrlAppUser ctrlAppUser = new CtrlAppUser(request);

    int vectSize = PstAppUser.getCount(PstAppUser.fieldNames[PstAppUser.FLD_COMPANY_ID] +" = "+userSession.getAppUser().getCompanyId()); 
    start = ctrlAppUser.actionList(listCommand, start,vectSize,recordToGet);

    listAppUser = PstAppUser.listPartObj(start,recordToGet, PstAppUser.fieldNames[PstAppUser.FLD_COMPANY_ID] +" = "+userSession.getAppUser().getCompanyId() , order);

%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
    <% if (privAdd){%>
        function addNew(){
            document.frmAppUser.user_oid.value="0";
            document.frmAppUser.list_command.value="<%=listCommand%>";
            document.frmAppUser.command.value="<%=Command.ADD%>";
            document.frmAppUser.action="user_edit.jsp";
            document.frmAppUser.submit();
        }
    <%}%>
 
    function cmdEdit(oid){
        document.frmAppUser.user_oid.value=oid;
        document.frmAppUser.list_command.value="<%=listCommand%>";
        document.frmAppUser.command.value="<%=Command.EDIT%>";
        document.frmAppUser.action="user_edit.jsp";
        document.frmAppUser.submit();
    }

    function cmdListFirst(){
        document.frmAppUser.command.value="<%=Command.FIRST%>";
        document.frmAppUser.list_command.value="<%=Command.FIRST%>";
        document.frmAppUser.action="userlist.jsp";
        document.frmAppUser.submit();
    }

    function cmdListPrev(){
        document.frmAppUser.command.value="<%=Command.PREV%>";
        document.frmAppUser.list_command.value="<%=Command.PREV%>";
        document.frmAppUser.action="userlist.jsp";
        document.frmAppUser.submit();
    }

    function cmdListNext(){
        document.frmAppUser.command.value="<%=Command.NEXT%>";
        document.frmAppUser.list_command.value="<%=Command.NEXT%>";
        document.frmAppUser.action="userlist.jsp";
        document.frmAppUser.submit();
    }

    function cmdListLast(){
        document.frmAppUser.command.value="<%=Command.LAST%>";
        document.frmAppUser.list_command.value="<%=Command.LAST%>";
        document.frmAppUser.action="userlist.jsp";
        document.frmAppUser.submit();
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

<%@include file="../styles/plugin_component.jsp" %>
<link rel="stylesheet" type="text/css" href="../styles/prochain.css"/>
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" style="background-color: #ffffff !important;"> 
  <section class="content-header">
    <h1><%=textListTitleHeader[SESS_LANGUAGE][0]%>
      <small><%=textListTitleHeader[SESS_LANGUAGE][1]%></small></h1>
    <ol class="ol">
      <li>
        <a>
          <li class="active">User</li>
        </a>
      </li>
    </ol>
  </section>
  <p class="border"></p>
  <div class="container-pos">     
    <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
      <%if (menuUsed == MENU_PER_TRANS) {
   %>
      <tr>
        <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
          <%@ include file = "../main/header.jsp" %>
          <!-- #EndEditable -->
        </td>
      </tr>
      <tr>
        <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
          <%@ include file = "../main/mnmain.jsp" %>
          <!-- #EndEditable --> 
        </td>
      </tr>
      <%} else {%>
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
              <td><!-- #BeginEditable "content" --> 
                <form name="frmAppUser" method="post" action="">
                  <input type="hidden" name="command" value="">
                  <input type="hidden" name="user_oid" value="<%=appUserOID%>">
                  <input type="hidden" name="vectSize" value="<%=vectSize%>">
                  <input type="hidden" name="start" value="<%=start%>">
                  <input type="hidden" name="list_command" value="<%=listCommand%>">
                  <div class="col-md-12">
                  <table id="example" class="table table-striped table-bordered" style="width:100%">
                    <thead>
                      <tr>
                        <th>No</th>
                        <th><%=textListTitleHeader[SESS_LANGUAGE][2]%></th>
                        <th><%=textListTitleHeader[SESS_LANGUAGE][3]%></th>
                        <th>Location</th>
                        <th><%=textListTitleHeader[SESS_LANGUAGE][4]%></th>
                      </tr>
                    </thead>
                    <tbody>
                    <%
                      long lokasinya = 0;
                      String where = PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID]+" = "+userId+" AND "
                             + "" + PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME]+ "= 'user_assign_single_location'";
                        try {
                            Vector <DataCustom> listData = PstDataCustom.list(0, 0, where, "");
                            Location loc = new Location();
                            for(DataCustom data : listData){
                                long oid = Long.parseLong(data.getDataValue());
                                lokasinya = oid;
                            }
                        } catch (Exception e) {
                        }
                      String lokasiUser = " dc."+PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME]+" = 'user_assign_single_location'"
                              + " AND dc."+PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_VALUE]+" = "+lokasinya;
                      Vector listCustomer = PstAppUser.listUser(0, 0, "", " ap."+PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID] + " ASC");
                      for (int i = 0; i < listCustomer.size(); i++) {
                        Vector list = (Vector) listCustomer.get(i);
                        AppUser appUser = (AppUser) list.get(0);
                        DataCustom dc = (DataCustom) list.get(1);
                        
                        where = PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID]+" = "+appUser.getOID()+" AND "
                                 + "" + PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME]+ "= 'user_assign_single_location'";
                        String location = "-";

                        try {
                            Vector <DataCustom> listData = PstDataCustom.list(0, 0, where, "");
                            Location loc = new Location();
                            for(DataCustom data : listData){
                                long oid = Long.parseLong(data.getDataValue());
                                loc = PstLocation.fetchExc(oid);
                                location = loc.getName();
                            }
                        } catch (Exception e) {
                        }
                    %>
                    <tr>
                      <td><%=i + 1%></td>
                      <td><a href="javascript:cmdEdit('<%=appUser.getOID()%>')"><%=appUser.getLoginId()%></a></td>
                      <td><%=appUser.getFullName()%></td>
                      <td><div class="text-center"><%=location %></div></td>
                      <td><%=appUser.getStatusTxt(appUser.getUserStatus())%></td>
                    </tr>
                    <%}%>
                    </tbody>
                  </table>
                  </div>
                  <% if (privAdd) {%>
                  <a class="btn btn-success command" href="javascript:addNew()"><li class="fa fa-plus"> <%=textListTitleHeader[SESS_LANGUAGE][5]%></li></a>
                      <%}%>
                </form>
                <script language="JavaScript">

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
                        //-->
                        </script>
                        <link rel="stylesheet" href="../css/default.css" type="text/css">
                    </td> 
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
        </td>
    </tr>
</table>
</div>

  <script>
      $(document).ready( function () {
    $('#example').DataTable({
			"ordering": true
    });
} );
  </script>
</body>
</html>
