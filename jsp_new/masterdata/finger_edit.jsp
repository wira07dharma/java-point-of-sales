<%@page import="com.lowagie.text.pdf.codec.Base64"%>
<%@page import="com.dimata.posbo.form.admin.CtrlFingerPatern"%>
<%@ page language="java" %>
<%@ page import = "java.util.*,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.util.Command,
                   com.dimata.gui.jsp.ControlDate,
                   com.dimata.harisma.entity.employee.Employee,
                   com.dimata.harisma.entity.employee.PstEmployee
                  
                   " 
%>
<%@page import="com.dimata.posbo.form.admin.CtrlFingerPatern"%>
<%@page import="com.dimata.posbo.form.admin.FrmFingerPatern"%>
<%@page import="com.dimata.posbo.entity.admin.PstFingerPatern"%>
<%@page import="com.dimata.posbo.entity.admin.FingerPatern"%>


<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_ADMIN , AppObjInfo.G2_ADMIN_USER, AppObjInfo.OBJ_ADMIN_USER_GROUP); %>
<%@ include file = "../main/checkuser.jsp" %>


<%!
public static final String listTextGlobal[][] = {
    {"Detil Data Sidik Jari","Edit","Harus diiisi"},
    {"Detail of Finger Data","Edit","Required"}
};

public static final String listTextTableHeader[][] = {
    {"Tipe Jari","Pilih","Scan dan Simpan","Kembali","Hasil"},
    {"Finger Type","Select","Save","Back","Result"}
};

public static final String textListFinger[][]={
    {"Kelingking Kiri","Jari Manis Kiri","Jari Tengah Kiri","Telunjuk Kiri","Ibu Jari Kiri","Ibu Jari Kanan","Telunjuk Kanan","Jari Tengah Kanan","Jari Manis Kanan","Kelingking Kanan"},
    {"Left Little Finger","Left Ring Finger","Left Middle Finger","Left Fore Finger","Left Thumb","Right Thumb","Right Fore Finger","Right Middle Finger","Right Ring Finger","Right Little Finger"}   
};

public static final String textListresult[][]={
    {"Data dan pola jari sudah berhasil didaftarkan"},
    {"Your finger patern has successfull registered"}
};

%>
<%

/* VARIABLE DECLARATION */ 
ControlLine ctrLine = new ControlLine();
Vector listFingerPatern = new Vector(1,1);
String whereClause ="";
String order ="";
Hashtable<Integer, Boolean> fingerType = new Hashtable<Integer, Boolean>();

/* GET REQUEST FROM HIDDEN TEXT */
int iCommand = FRMQueryString.requestCommand(request);
long employeeOid = FRMQueryString.requestLong(request,"employee_oid");
long fingerOID = FRMQueryString.requestLong(request,"finger_oid");

/* ACTION */
CtrlFingerPatern ctrlFingerPatern = new CtrlFingerPatern(request);
FrmFingerPatern frmFingerPatern = ctrlFingerPatern.getForm();
Employee employee = new Employee();
int iErrCode = ctrlFingerPatern.action(iCommand,fingerOID,employeeOid);

//get list finger patern yang sudah didaftarkan oleh employee ini sebelumnya
whereClause = " "+PstFingerPatern.fieldNames[PstFingerPatern.FLD_EMPLOYEE_ID]+"="+employeeOid+" ";
order =" "+PstFingerPatern.fieldNames[PstFingerPatern.FLD_FINGER_TYPE]+" ";
listFingerPatern = PstFingerPatern.list(0, 0, whereClause, order);
if (listFingerPatern.size()>0){
    for (int i = 0; i<listFingerPatern.size();i++){
        FingerPatern fingerPatern = (FingerPatern)listFingerPatern.get(i);
        fingerType.put(fingerPatern.getFingerType(), true);
    }
}


%>

<html>
<head>
    <title>Dimata - ProChain POS</title>
    <script language="JavaScript">
        function cmdCancel(){
            //document.frmAppGroup.group_oid.value=oid;
            document.frmAppGroup.command.value="<%=Command.EDIT%>";
            document.frmAppGroup.action="groupedit.jsp";
            document.frmAppGroup.submit();
        }

        <% if(privAdd || privUpdate) {%>
            function cmdSave(){
                document.frmEmployee.command.value="<%=Command.SAVE%>";
                document.frmEmployee.employee_oid.value="0";
                document.frmEmployee.action="employee_edit.jsp";
                document.frmEmployee.submit();
            }
        <%}%>

        function cmdBack(oid){
            
           document.frmFingerPatern.command.value="<%=Command.LIST%>";
           document.frmFingerPatern.employee_oid.value=oid;
           document.frmFingerPatern.action="master_finger_data.jsp";
           document.frmFingerPatern.submit();
        }
    </script>
    
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
    <%if(menuUsed == MENU_ICON){%>
        <link href="../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
    <%}%>
    <link rel="stylesheet" href="../styles/main.css" type="text/css">
    <link rel="stylesheet" href="../styles/tab.css" type="text/css">
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
                        <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
                            <%=listTextGlobal[SESS_LANGUAGE][0]%>
                            > <%=listTextGlobal[SESS_LANGUAGE][1]%><!-- #EndEditable -->
                        </td>
                    </tr>
                <tr> 
                    <td><!-- #BeginEditable "content" --> 
                        <form id="frmFingerPatern" name="frmFingerPatern" method="post" action="">
                            
                            <input type="hidden" name="command" value="<%=Command.SAVE%>">
                            <input id="employee_oid" type="hidden" name="employee_oid" value="<%=employeeOid%>">
                            
                            <table style="width: 100%">
                                <tr>
                                    <td style="width : 15%"><%= listTextTableHeader[SESS_LANGUAGE][0]%></td>
                                    <td style="width : 2%">:</td>
                                    <td style="width : 80%">
                                        <select name="" id="fingerId" style='width:300px;'>
                                            <option value="">--<%=listTextTableHeader[SESS_LANGUAGE][1]%>--</option>
                                        <%
                                        for (int i=0;i<9;i++){
                                           Boolean found = false;
                                           try{
                                               if (fingerType.size()>0){
                                                   found = fingerType.containsKey(i);
                                               }
 
                                           }catch(Exception ex){
                                               
                                           }
                                           
                                           // untuk link sidik jari
                                           //url sidik jari 
                                            
                                            //String urlRegister =""+baseURL+"employee_finger_register.jsp?user_id="+employeeOid+"&baseUrl="+baseURL+"&typeFinger="+i+"&command="+Command.SAVE+"";
                                            String urlRegister ="register&"+employeeOid+"&"+i+"&"+baseURL+"masterdata/employee_finger_register.jsp";
                                            //url sidik jari melalui proses enkripsi base 64    
                                            //byte[] uRegister = urlRegister.getBytes();
                                            String uRegister64= urlRegister;

                                           if (found==false){
                                               out.println("<option value='"+uRegister64+"'>"+textListFinger[SESS_LANGUAGE][i]+"</option>");
                                           }
                                           
                                        }
                                        %> 
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
                                <tr>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
                                <tr>
                                    <td style="width : 15%">&nbsp;</td>
                                    <td style="width : 2%">&nbsp;</td>
                                    <td style="width : 80%">
                                        <table>
                                            <tr>
                                                <td>
                                                    <a class="fingerSpot" href="#"><img name="Image10011" border="0" src="<%=approot%>/images/BtnSave.jpg" width="24" height="24" alt="Tambah Baru User"></a>
                                                </td>
                                                <td>
                                                    <a class="fingerSpot" href="#"><%=listTextTableHeader[SESS_LANGUAGE][2]%></a>
                                                </td>
                                                <td>
                                                    <a class="kembali" href="javascript:cmdBack('<%= employeeOid%>')""><img name="Image10011" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="Tambah Baru User"></a>
                                                </td>
                                                <td>
                                                    <a class="kembali" href="javascript:cmdBack('<%= employeeOid%>')""><%=listTextTableHeader[SESS_LANGUAGE][3]%></a>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                    </td>
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
</table>
<!-- Untuk Bootstrap Modal -->
<link type="text/css" rel="stylesheet" href="../styles/bootstrap3.1/css/bootstrap.css">
<script type="text/javascript" src="../styles/jquery.min.js"></script>
<script type="text/javascript" src="../styles/bootstrap3.1/js/bootstrap.min.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
        var interval =0;
        var currentCount = <%= listFingerPatern.size()%>   
        function ajax(url,data,type,appendTo,another){
            $.ajax({
                url : ""+url+"",
                data: ""+data+"",
                type : ""+type+"",
                async : false,
                cache: false,
                success : function(data) {
                    
                    if (another=="getCount"){
                        if (currentCount<data){
                            currentCount = data;
                            //alert(currentCount);
                            //$('#modalReport').modal({backdrop: 'static', keyboard: false});
                            clearInterval(interval);
                            alert('<%= textListresult[SESS_LANGUAGE][0]%>');
                            $('#employee_oid').val('<%= employeeOid%>');
                            $('#frmFingerPatern').attr('action','master_finger_data.jsp');
                            $('#frmFingerPatern').submit();
                        }
                    }
                    
                },
                error : function(data){
                    alert('error');
                }
            }).done(function(){

            });

        }
        
        function cekCountFinger(){
            var url ="<%=approot%>/register.php";
            var data = "command=<%= Command.GET%>&user_id=<%= employeeOid%>";
            //alert(data);
            ajax(url,data,"POST","","getCount");
        }
        
        
                
        $('.fingerSpot').click(function(){
            interval = setInterval(function() {
               cekCountFinger();
            },5000);
        });
                    
        $('#fingerId').change(function(){
            var nilai = $(this).val();
            if (nilai!=""){
                $('.fingerSpot').attr({'href':'findspot:findspot protocol;'+nilai+''});
            }else{
                $('.fingerSpot').attr({'href':'#'});
            }

        });
        
        $('.tutup').click(function(){
            $('#employee_oid').val('<%= employeeOid%>');
            $('#frmFingerPatern').attr('action','master_finger_data.jsp');
            $('#frmFingerPatern').submit();
        });
      
    }); 
 </script>
<div id="modalReport" class="modal fade" tabindex="-1">
     <div class="modal-dialog modal-sm" style="max-width: 400px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close tutup" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modal-title"><%= listTextTableHeader[SESS_LANGUAGE][4]%>...</h4>
            </div>
            <div class="modal-body" id="modal-body">
                <%= textListresult[SESS_LANGUAGE][0]%>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger tutup">Close</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
