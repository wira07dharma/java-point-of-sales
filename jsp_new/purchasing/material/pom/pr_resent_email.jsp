<%-- 
    Document   : pr_resent_email
    Created on : Jul 2, 2014, 12:03:38 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.posbo.session.purchasing.SessFormatEmailQueenTandoor"%>
<%@page import="com.dimata.common.entity.location.PstLocation"%>
<%@page import="com.dimata.common.entity.location.Location"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseRequest"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstPurchaseRequestItem"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstPurchaseRequest"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.common.entity.system.PstSystemProperty"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.common.session.email.SessEmail"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file = "../../../main/javainit.jsp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body> 
        <%
            int iCommand = FRMQueryString.requestCommand(request);
            int startItem = FRMQueryString.requestInt(request, "start_item");
            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            int appCommand = FRMQueryString.requestInt(request, "approval_command");
            long oidPurchaseRequest = FRMQueryString.requestLong(request,"hidden_material_request_id");
            long oidPurchaseRequestItem = FRMQueryString.requestLong(request,"hidden_mat_request_item_id");

            int typePrint = FRMQueryString.requestInt(request, "type_print_tranfer");
            double defaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));

            //add opie-eyek 20130812 untuk print price atau tidak
            int includePrintPrice = FRMQueryString.requestInt(request,"showprintprice");
    
        PurchaseRequest po = new PurchaseRequest();
        String whereClauseItem = PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ID]+"="+oidPurchaseRequest;
        try{
            po = PstPurchaseRequest.fetchExc(oidPurchaseRequest);
        }catch(Exception ex){
        
        }
        
        
        //sent email if document to be approve
        String contentEmailItem = "";
        int sentNotif =Integer.parseInt(PstSystemProperty.getValueByName("POS_EMAIL_NOTIFICATION"));
        String toEmail =PstSystemProperty.getValueByName("POS_EMAIL_TO");
        String urlOnline =PstSystemProperty.getValueByName("POS_URL_ONLINE");
        String hasilEmail="";
        if(sentNotif==1 && oidPurchaseRequest!=0){
                Vector listPurchaseRequestItemEmail = new Vector();
                listPurchaseRequestItemEmail = PstPurchaseRequestItem.listWithStokNMinStock(startItem,0,whereClauseItem,po.getLocationId());
                Location lokasiPO = new Location();
                try{
                    lokasiPO = PstLocation.fetchExc(po.getLocationId());
                }catch(Exception ex){
                }
                String AksesOnsite ="Akses Onsite Aplication :&nbsp; <a href=\"http://localhost:8080/"+approot+"/login_new.jsp?nodocument="+po.getOID()+"&typeView=1&deviceuse=1\">Onsite Aplication</a><br>";
                String AksesOnline ="Akses Online Aplication :&nbsp; <a href=\""+urlOnline+"/login_new.jsp?nodocument="+po.getOID()+"&typeView=2&deviceuse=1\">Online Aplication</a>";
                contentEmailItem= SessFormatEmailQueenTandoor.getContentEmailPR(listPurchaseRequestItemEmail, po, AksesOnsite,AksesOnline,lokasiPO);
                SessEmail sessEmail = new SessEmail();
                hasilEmail = sessEmail.sendEamil(toEmail, "Purchase Request Approval - "+lokasiPO.getName()+" - "+po.getPrCode(), contentEmailItem, "");
        }
        %>
        <h1><%=hasilEmail%></h1>
    </body>
</html>
