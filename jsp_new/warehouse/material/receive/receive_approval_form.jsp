<%-- 
    Document   : receive_approval_form
    Created on : Dec 21, 2017, 3:11:16 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceiveItem"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceive"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%//
    int iCommand = FRMQueryString.requestCommand(request);
    long oidReceiveMaterial = FRMQueryString.requestLong(request, "rec_mat_id");
    String userName = FRMQueryString.requestString(request, "FRM_USERNAME");
    String password = FRMQueryString.requestString(request, "FRM_PASSWORD");

    MatReceive receive = new MatReceive();
    try {
        receive = PstMatReceive.fetchExc(oidReceiveMaterial);
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }

    String message = "";
    int statusPerubahan = 0;
    if (iCommand == Command.LIST) {
        Vector listSpv = PstAppUser.listPartObj(0, 0,
                PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID] + " = '" + userName + "'"
                + " AND " + PstAppUser.fieldNames[PstAppUser.FLD_PASSWORD] + " = '" + password + "'"
                + " AND " + PstAppUser.fieldNames[PstAppUser.FLD_USER_GROUP_NEW] + " = '1'", "");

        if (!listSpv.isEmpty()) {
            double nilaiNota = receive.getTotalNota();
            Vector listRecItem = PstMatReceiveItem.list(0, 0, "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + " = '" + receive.getOID() + "'", "");
            //get total berat semua item
            double totalBeratItem = 0;
            for (int i = 0; i < listRecItem.size(); i++) {
                MatReceiveItem item = (MatReceiveItem) listRecItem.get(i);
                totalBeratItem += item.getBerat();
            }
            //update per item
            for (int i = 0; i < listRecItem.size(); i++) {
                MatReceiveItem item = (MatReceiveItem) listRecItem.get(i);
                MatReceiveItem newItem = PstMatReceiveItem.fetchExc(item.getOID());
                double newHarga = (nilaiNota / totalBeratItem) * item.getBerat();
                newItem.setTotal(newHarga);
                try {
                    PstMatReceiveItem.updateExc(newItem);
                    message = "Penyesuaian data item berhasil.";
                    statusPerubahan = 1;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    message = "Penyesuaian data item gagal!<br>" + e.getMessage();
                    statusPerubahan = 0;
                }
            }            
        } else {
            message = "Username atau Password tidak dikenali.";
            statusPerubahan = 0;
        }
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">        
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <script type="text/javascript" src="../../../styles/bootstrap/js/bootstrap.min.js"></script>
        <script>
            <% if (statusPerubahan == 1) {%>
                self.close();
            <%}%>
        </script>
    </head>
    <body>
        <div class="container">
            <br>
            <div class="col-sm-12">
                <form class="form-horizontal" method="post" action="receive_approval_form.jsp?command=<%=Command.LIST%>">
                    <input type="hidden" value="<%=oidReceiveMaterial%>" name="rec_mat_id">
                    <div class="form-group">
                        <label class="col-sm-2">Supervisor Username</label>
                        <div class="col-sm-4">
                            <input type="text" required="" placeholder="Username" class="form-control input-sm" name="FRM_USERNAME">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2">Supervisor Password</label>
                        <div class="col-sm-4">
                            <input type="password" required="" placeholder="Password" class="form-control input-sm" name="FRM_PASSWORD">
                        </div>
                    </div>
                    <!--div class="form-group">
                        <label class="col-sm-2">Keterangan</label>
                        <div class="col-sm-4">
                            <textarea style="resize: none" class="form-control input-sm"></textarea>
                        </div>
                    </div-->
                    <div class="form-group">
                        <label class="col-sm-2"><%=message%></label>
                    </div>
                    <div class="pull-right">
                        <button type="submit" class="btn btn-sm btn-primary">Ok</button>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
