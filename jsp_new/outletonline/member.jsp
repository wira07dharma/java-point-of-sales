<%-- 
    Document   : member
    Created on : Jun 16, 2015, 1:37:28 PM
    Author     : dimata005
--%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.common.entity.location.Negara"%>
<%@page import="com.dimata.common.entity.location.PstNegara"%>
<%@page import="com.dimata.posbo.entity.masterdata.MemberReg"%>
<%@page import="com.dimata.posbo.entity.search.SrcMemberReg"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMemberReg"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.posbo.entity.masterdata.Room"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstRoom"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.pos.form.billing.CtrlOpenBillMain"%>
<%@page import="com.dimata.pos.form.billing.FrmBillMain"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>

<%@ include file = "../main/javainit.jsp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
     
        <link href="../styles/takingorder/css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <script src="../styles/takingorder/js/jquery.js"></script>
        <script type="text/javascript" src="../styles/jquery.min.js"></script>
        <!-- Tablesorter: required -->
        <link href="../styles/takingorder/css/theme.css" rel="stylesheet">
        <script src="../styles/takingorder/js/jquery_002.js"></script>
        <script src="../styles/takingorder/js/jquery_003.js"></script>
        <!-- Tablesorter: pager -->
        <link rel="stylesheet" href="../styles/takingorder/js/jquery.css">
        <script src="../styles/takingorder/js/widget-pager.js"></script>
        
        <script type="text/javascript">
            function myFunction() {
                var key = document.frmsrcsalesorder.key.value;
                $("#member").html("");
                    $.ajax({
                        url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CreateTableMember?key="+key+"",
                        type : "POST",
                        async : false,
                        cache: false,
                        success : function(data) {
                                content=data;
                                $(content).appendTo("#member");
                        }
                    });
            }
            
             function cmdCheckGuest(nama,address,oid,nasionality){
                    alert("a"+nama);
                    /*document.frmsrcsalesorder.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>.value=nama;
                    document.frmsrcsalesorder.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_COUNTRY]%>.value=nasionality;
                    document.frmsrcsalesorder.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]%>.value=oid;
                    alert("b");
                    document.frmsrcsalesorder.action="tableorder.jsp";
                    alert("c");
                    document.frmsrcsalesorder.submit();
                    alert("d");*/
                    
                    //self.opener.document.forms.frm_recmaterial..value = poOid;
                    alert("b");
                    self.opener.document.frmsrcsalesorder.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>.value=nama;
                    self.close();
               }
        </script>
    </head>
    <body>
    <form name="frmsrcsalesorder" method ="post" action="" role="form">
            <div>
              <div>
                <div class="modal-content">
                  <div class="modal-body">
                      <%-- body--%>
                      <div class="guest" style="margin:10px 0px;">
                       <div class="form-group">
                            <div class=row>
                                <div class="col-md-8">
                                    <label>Member Search</label>
                                    <div class="input-group">   
                                        <input name="key" type="text" class="form-control" placeholder="Type Member Name" onkeyup="myFunction()" value="" >
                                    </div>
                                </div>
                                <div class="col-md-4">
                                </div>    
                           </div>
                       </div> 
                       <div class="form-group">
                           <div class="col-md-12" id="member"></div>
                       </div>
                        <p>
                            <a href="javascript:parent.jQuery.fancybox.close();">Close iframe parent</a>|
                            <a href="javascript:parent.jQuery.fancybox.open({href : 'tableorder.jsp', title : 'My title'});">Change content</a>
                        </p>             
                     </div> 
                  </div>
                </div>
              </div>
            </div>
      </form>
<script language="JavaScript">
   $("#member").html("");
                    $.ajax({
                        url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CreateTableMember?key=",
                        type : "POST",
                        async : false,
                        cache: false,
                        success : function(data) {
                                content=data;
                                $(content).appendTo("#member");
                        }
                    });
</script>
    </body>
</html>
