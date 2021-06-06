
<%@page import="com.lowagie.text.pdf.codec.Base64"%>
<%@ page language="java" %>
<%@ page import = "java.util.*,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.util.Command,
                   com.dimata.harisma.entity.employee.Employee,
                   com.dimata.harisma.entity.employee.PstEmployee"
%>

<%@page import="com.dimata.posbo.form.admin.CtrlFingerPatern"%>
<%@page import="com.dimata.posbo.entity.admin.PstFingerPatern"%>
<%@page import="com.dimata.posbo.entity.admin.FingerPatern"%>

				   
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_ADMIN , AppObjInfo.G2_ADMIN_USER, AppObjInfo.OBJ_ADMIN_USER_USER); %>
<%@ include file = "../main/checkuser.jsp" %>

<!-- JSP Block -->
<%!
public static final String textListTitleHeader[][] ={ 
    {"No.","Keterangan","Status"},
    {"No.","Description","Status"}
};
public static final String textListFinger[][]={
    {"Kelingking Kiri","Jari Manis Kiri","Jari Tengah Kiri","Telunjuk Kiri","Ibu Jari Kiri","Ibu Jari Kanan","Telunjuk Kanan","Jari Tengah Kanan","Jari Manis Kanan","Kelingking Kanan"},
    {"Left Little Finger","Left Ring Finger","Left Middle Finger","Left Fore Finger","Left Thumb","Right Thumb","Right Fore Finger","Right Middle Finger","Right Ring Finger","Right Little Finger"}   
};
public static final String textListTitle[][]={
    {"Sistem","Daftar Pegawai","Detil Data Sidik Jari","Registrasi Jari Baru","Update","Kembali","Hapus"},
    {"System","Employee List","Detail Finger Data","Register for New Finger","Update","Back","Delete"}   
};

public static final String textListresult[][]={
    {"Data dan pola jari sudah berhasil didaftarkan","Anda yakin menghapus data ini?"},
    {"Your finger patern has successfull registered","Are you sure to delete these data?"}
};


public String drawListFinger(int language,Vector objectClass,String baseURL){
    
    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("90%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");
    
    ctrlist.addHeader(textListTitleHeader[language][0],"10%");
    ctrlist.addHeader(textListTitleHeader[language][1],"60%");
    ctrlist.addHeader(textListTitleHeader[language][2],"30%");
   
    ctrlist.setLinkRow(0);
    ctrlist.setLinkSufix("");
    Vector lstData = ctrlist.getData();
    Vector lstLinkData 	= ctrlist.getLinkData();

    ctrlist.setLinkPrefix("javascript:cmdEdit('");
    ctrlist.setLinkSufix("')");
    ctrlist.reset();

    for (int i = 0; i < objectClass.size(); i++) {
        FingerPatern fingerPatern = (FingerPatern)objectClass.get(i);
        int no = i+1;
        
        // untuk link sidik jari
        //url sidik jari 
        //String urlRegister =""+baseURL+"register.php?user_id="+fingerPatern.getEmployeeId()+"&baseUrl="+baseURL+"&typeFinger="+fingerPatern.getFingerType()+"&command="+Command.SAVE+"";
        String urlRegister ="register&"+fingerPatern.getEmployeeId()+"&"+fingerPatern.getFingerType()+"&"+baseURL+"masterdata/employee_finger_register.jsp";
         //url sidik jari melalui proses enkripsi base 64    
        //byte[] uRegister = urlRegister.getBytes();
        //String uRegister64= new String(Base64.encodeBytes(uRegister));
        String uRegister64 = urlRegister;
        
        Vector rowx = new Vector();
        rowx.add(String.valueOf(no));
        rowx.add(String.valueOf(textListFinger[language][fingerPatern.getFingerType()]));
        rowx.add(String.valueOf(""
            + " <a data-oid ='"+fingerPatern.getOID()+"' class='fingerSpot' href='findspot:findspot protocol;"+uRegister64+"'>"+textListTitle[language][4]+"</a>"
            + " &nbsp; &nbsp; <a class='fingerSpotDelete' data-oid ='"+fingerPatern.getOID()+"' style='cursor:pointer'>"+textListTitle[language][6]+"</a>"));
       
        lstData.add(rowx);
        lstLinkData.add(String.valueOf(fingerPatern.getOID()));
    }						

    return ctrlist.draw();
}

%>
<%

    /* VARIABLE DECLARATION */
    int recordToGet = 15;
    String order = " " + PstFingerPatern.fieldNames[PstFingerPatern.FLD_FINGER_TYPE];
    Vector listFinger = new Vector(1,1);
    Vector listEmployee = new Vector(1,1);
    ControlLine ctrLine = new ControlLine();
    String whereClause = "";
    String whereClauseEmployee ="";

    /* GET REQUEST FROM HIDDEN TEXT */
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start"); 
    long employeeOID = FRMQueryString.requestLong(request,"employee_oid");
    long fingerOID = FRMQueryString.requestLong(request, "finger_oid");
    int listCommand = FRMQueryString.requestInt(request, "list_command");
    String search = FRMQueryString.requestString(request, "search");
    if(listCommand==Command.NONE)
        listCommand = Command.LIST;
    
    whereClause = " "+PstFingerPatern.fieldNames[PstFingerPatern.FLD_EMPLOYEE_ID]+" = "+employeeOID+"";
        
    CtrlFingerPatern ctrlFingerPatern = new CtrlFingerPatern(request);

    int vectSize = PstFingerPatern.getCount(whereClause); 
    start = ctrlFingerPatern.actionList(listCommand, start,vectSize,recordToGet);

    listFinger = PstFingerPatern.list(start,recordToGet, whereClause , order);
    
    //mendapatkan detil data employee
    Employee employee = new Employee();
    try{
        employee = PstEmployee.fetchExc(employeeOID);
    }catch(Exception e){
    
    }

%>
<!-- End of JSP Block -->
<html>
<head>
    <title>Dimata - ProChain POS</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
    <%if(menuUsed == MENU_ICON){%>
        <link href="../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
    <%}%>
    <link rel="stylesheet" href="../styles/main.css" type="text/css">
    <link rel="stylesheet" href="../styles/tab.css" type="text/css">
    <script language="JavaScript">
       
        function addNew(oid){
            document.frmFingerPatern.employee_oid.value=oid;
            document.frmFingerPatern.finger_oid.value=0;
            document.frmFingerPatern.list_command.value="<%=listCommand%>";
            document.frmFingerPatern.command.value="<%=Command.ADD%>";
            document.frmFingerPatern.action="finger_edit.jsp";
            document.frmFingerPatern.submit();
        }
        
        function cmdListFirst(){
            document.frmFingerPatern.command.value="<%=Command.FIRST%>";
            document.frmFingerPatern.list_command.value="<%=Command.FIRST%>";
            document.frmFingerPatern.action="master_finger_data.jsp";
            document.frmFingerPatern.submit();
        }

        function cmdListPrev(){
            document.frmFingerPatern.command.value="<%=Command.PREV%>";
            document.frmFingerPatern.list_command.value="<%=Command.PREV%>";
            document.frmFingerPatern.action="master_finger_data.jsp";
            document.frmFingerPatern.submit();
        }

        function cmdListNext(){
            document.frmFingerPatern.command.value="<%=Command.NEXT%>";
            document.frmFingerPatern.list_command.value="<%=Command.NEXT%>";
            document.frmFingerPatern.action="master_finger_data.jsp";
            document.frmFingerPatern.submit();
        }

        function cmdListLast(){
            document.frmFingerPatern.command.value="<%=Command.LAST%>";
            document.frmFingerPatern.list_command.value="<%=Command.LAST%>";
            document.frmFingerPatern.action="master_finger_data.jsp";
            document.frmFingerPatern.submit();
        }
        
        function cmdSearch(){
            document.frmFingerPatern.command.value="<%=Command.NONE%>";
            document.frmFingerPatern.list_command.value="<%=listCommand%>";
            document.frmFingerPatern.action="master_finger_data.jsp";
            document.frmFingerPatern.submit();
        }
        
        
        function cmdBack(){
            document.frmFingerPatern.command.value="<%=Command.LIST%>";
            document.frmFingerPatern.action="master_employee.jsp";
            document.frmFingerPatern.submit();
        }
    </script>
</head> 
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
    <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
    <%if(menuUsed == MENU_PER_TRANS){%>
        <tr>
            <td height="25" ID="TOPTITLE"> 
              <%@ include file = "../main/header.jsp" %>
            </td>
        </tr>
        <tr>
            <td height="20" ID="MAINMENU">
                <%@ include file = "../main/mnmain.jsp" %>
            </td>
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
                        <td height="20" class="mainheader">
                            <%=textListTitle[SESS_LANGUAGE][0]%>
                            > <%=textListTitle[SESS_LANGUAGE][1]%>
                            > <%=textListTitle[SESS_LANGUAGE][2]%>
                            > <%=employee.getFullName()%>
                        </td>
                    </tr>
                <tr> 
                    <td><!-- #BeginEditable "content" --> 
                        <form name="frmFingerPatern" method="post" action="">
                            
                            <input type="hidden" name="command" value="">
                            <input type="hidden" name="employee_oid" value="<%=employeeOID%>">
                            <input type="hidden" name="finger_oid" value="<%=fingerOID%>">
                            <input type="hidden" name="vectSize" value="<%=vectSize%>">
                            <input type="hidden" name="start" value="<%=start%>">
                            <input type="hidden" name="list_command" value="<%=listCommand%>">
                            <table width="100%" cellspacing="0" cellpadding="0">
                                <tr> 
                                    <td colspan="2" class="listtitle">
                                        <hr size="1">
                                        
                                    </td>
                                </tr>
                            </table>
                            <% if ((listFinger!=null)&&(listFinger.size()>0)){ %>
                            <%=drawListFinger(SESS_LANGUAGE, listFinger,baseURL)%> 
                            <%}%>
                            <table width="100%">
                                <tr> 
                                    <td colspan="2"> 
                                        <span class="command"> 
                                            <% 
                                             int cmd = 0;
                                             if ((iCommand == Command.FIRST || iCommand == Command.PREV )|| 
                                                (iCommand == Command.NEXT || iCommand == Command.LAST))
                                                    cmd =iCommand; 
                                             else{
                                                if(iCommand == Command.NONE)
                                                    cmd = Command.FIRST;
                                             } 
                                             ctrLine.setLocationImg(approot+"/images");
                                             ctrLine.initDefault();						   
                                            %>
                                            <%=ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet)%> 
                                        </span>				  
                                    </td>
                                </tr>
                                <% if (privAdd){%>
                                <tr valign="middle"> 
                                    <td colspan="2" class="command"> 
                                        <table width="20%" border="0" cellspacing="2" cellpadding="2">
                                            <tr> 
                                                <!--td width="20%">
                                                    <a href="javascript:addNew('<%= employeeOID%>')"><img name="Image10011" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Tambah Baru User"></a>
                                                </td-->
                                                <td ><a class="btn btn-primary" href="javascript:addNew('<%= employeeOID%>')" class="command"><i class="fa fa-plus-circle"></i> 
                                                    <%=textListTitle[SESS_LANGUAGE][3]%></a>
                                                </td>
                                                <!--td><a href="javascript:cmdBack()"><img name="Image10011" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="Tambah Baru User"></a></td-->
                                                <td><a class="btn btn-primary" href="javascript:cmdBack()"><i class="fa fa-arrow-left"></i> <%=textListTitle[SESS_LANGUAGE][5]%></a></td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <%}%>
                                
                                <tr> 
                                  <td width="13%">&nbsp;</td>
                                  <td width="87%">&nbsp;</td>
                                </tr>
                                <tr> 
                                  <td width="13%">&nbsp;</td>
                                  <td width="87%">&nbsp;</td>
                                </tr>
                            </table>
                        </form>
                        
                        <link rel="stylesheet" href="../css/default.css" type="text/css">
            <!-- #EndEditable -->
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
        <!-- #EndEditable --> 
        </td>
    </tr>
    <script type="text/javascript" src="../styles/jquery.min.js"></script>
    <script type="text/javascript" src="../styles/bootstrap3.1/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            var oidGeneral = "";
            var interval =0; 
            function ajax(url,data,type,appendTo,another){
                $.ajax({
                    url : ""+url+"",
                    data: ""+data+"",
                    type : ""+type+"",
                    async : false,
                    cache: false,
                    success : function(data) {

                       
                    },
                    error : function(data){
                        alert('error');
                    }
                }).done(function(data){
                        //alert(another);
                        if (another=="erase"){
                            //alert("test");
                            checkFingerPatern(oidGeneral);    
                        }else if (another=="checkFingerPatern"){
                            //alert(data);
                            if (data=="1"){
                                clearInterval(interval);
                                alert('<%= textListresult[SESS_LANGUAGE][0]%>');
                                location.reload();
                            }
                        }else if (another=="deleteFingerPatern"){
                            location.reload();
                        
                        }
                    }
                );

            }
            
            function fingerUpdateEmpty(){
                $(".fingerSpot").click(function(){
                   
                    var url ="<%=approot%>/register.php";
                    var oid = $(this).data("oid");
                    var data = "command=<%= Command.DELETE%>&oidFinger="+oid+"";
                  
                    oidGeneral = oid;
                    ajax(url,data,"POST","","erase");
                });

            }
            
            function checkFingerPatern(oidGeneral){
                interval = setInterval(function() {
                    var url ="<%=approot%>/register.php";
                    var data = "command=<%= Command.LOAD%>&oidFinger="+oidGeneral+"";
                    ajax(url,data,"POST","","checkFingerPatern");
                },5000);
                
            }
            
            function deleteFingerSpot(){
                $('.fingerSpotDelete').click(function(){
                    
                    var r = confirm("<%=textListresult[SESS_LANGUAGE][1]%>");
                    if (r == true) {
                        var url ="<%=approot%>/register.php";
                        var oid = $(this).data("oid");
                        var data = "command=<%= Command.STOP%>&oidFinger="+oid+"";
                        ajax(url,data,"POST","","deleteFingerPatern");
                    } 
                    
                   
                });
            }
            
            fingerUpdateEmpty();
            deleteFingerSpot();
        });
    </script>
</table>
<!--  -->
</body>
</html>
