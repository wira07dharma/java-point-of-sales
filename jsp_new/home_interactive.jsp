<%-- 
    Document   : home_interactive
    Created on : Jun 26, 2014, 9:46:01 AM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.posbo.session.masterdata.SessSales"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatStockOpname"%>
<%@page import="com.dimata.posbo.session.purchasing.SessPurchaseOrder"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatCosting"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatReturn"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatDispatch"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatReceive"%>
<%@page import="java.util.Vector"%>
<%@ include file="main/javainit_frame.jsp"%>
<%@ include file="cashieronline/CheckUserCashier.jsp"%>
<html>
    <head>
                <meta charset="UTF-8">
                <title>AdminLTE | Dashboard</title>
                <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
                <!-- bootstrap 3.0.2 -->
                <link href="styles/bootstrap3.1/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
                <!-- font Awesome -->
                <link href="styles/bootstrap3.1/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
                <!-- Ionicons -->
                <!-- Theme style -->
                <link href="styles/bootstrap3.1/css/AdminLTE.css" rel="stylesheet" type="text/css" />
    </head>
     <%
        //int vecSize=0;
        Vector purchaseRequest = new Vector(1, 1);
        Vector ReceiveMaterialInformation = new Vector(1, 1);
        Vector ReceiveMaterialInformationNonPO = new Vector(1, 1);
        Vector ReceiveMaterialNonSupplierInformation = new Vector(1, 1);

        Vector matDispatchInformation = new Vector(1, 1);
        Vector matDispatchUnitInformation = new Vector(1, 1);
        
        Vector returnInformation = new Vector(1, 1);
        Vector returnInformationNonPO = new Vector(1, 1);
        Vector pembiayaanInformation = new Vector(1, 1);
        Vector orderPembelianInformation = new Vector(1, 1);
        Vector transferRequest = new Vector(1, 1);
        Vector salesInformation = new Vector(1, 1);
        Vector itemInformation = new Vector(1, 1);
        int vecSizeOpnema =0;
        
        SessMatReceive sessMatReceive = new SessMatReceive();
        SessMatDispatch sessMatDispatch = new SessMatDispatch();
        SessMatReturn sessMatReturn = new SessMatReturn();
        SessMatCosting sessMatCosting = new SessMatCosting();
        SessPurchaseOrder sessPurchaseOrder = new SessPurchaseOrder();
        SessMatStockOpname sessMatStockOpname = new SessMatStockOpname();
        SessSales sessSales = new SessSales();
        SessMaterial sessMaterial = new SessMaterial();
        //update opie-eyek 20130805
            String whereClausex = " AND "+PstDataCustom.whereLocReportView(userIdframe, "user_create_document_location");
            
            purchaseRequest= sessMatReceive.getListPurchaseRequestMaterialInformation(whereClausex);
            
            orderPembelianInformation= sessPurchaseOrder.getPOMaterialListInformation(whereClausex); 
            ReceiveMaterialInformation = sessMatReceive.getListReceiveMaterialInformation(whereClausex);
            ReceiveMaterialInformationNonPO = sessMatReceive.getListReceiveMaterialWithoutPO(whereClausex);
            
            ReceiveMaterialNonSupplierInformation = sessMatReceive.getListReceiveMaterialNonSupplierInformation(whereClausex);
                    
            matDispatchInformation=sessMatDispatch.getMatDispatchInformation(whereClausex);
            matDispatchUnitInformation=sessMatDispatch.getMatDispatchUnitInformation(whereClausex);
            transferRequest=sessMatDispatch.getTransferRequestInformation(whereClausex);
            
            returnInformation=sessMatReturn.getListReturnMaterialInformation(whereClausex);
            returnInformationNonPO =sessMatReturn.getListReturnMaterialWithoutPOInformation(whereClausex);
            
            pembiayaanInformation=sessMatCosting.getMatCostingInformation(whereClausex);
            vecSizeOpnema =sessMatStockOpname.getListOpnameInformation(whereClausex);
            
            salesInformation = sessMatStockOpname.getListSales(whereClausex);
            itemInformation  = sessMaterial.getListSales("");
    
    %>
    
    <body class="skin-blue">
        <div class="row-fluid">
            <!-- Right side column. Contains the navbar and content of the page -->
            <div class="content-wrapper">
                <!-- Main content -->
                <section class="content">
                    <div class="row">
                        <section class="content">
                            <%if(vecSizeOpnema>0){%>
                            <h3 class="page-header">
                                    Document Opname dengan status Final <%=vecSizeOpnema%> <a href="<%=approotFrame%>/warehouse/material/stock/mat_stock_correction_src.jsp"><blink>Click Here</blink></a>
                            </h3>
                           <%}%>         
                            <div class="row">
                                <div class="col-xs-12">
                                     <div class="box box-danger" id="loading-example">
                                        <div class="box-header">
                                            <i class="fa fa-cloud"></i>
                                        <h3 class="box-title"><%=privPurchaseRequestOutletFrame==true?"Store":"Warehouse"%></h3>
                                        </div><!-- /.box-header -->
                                        <div class="box-footer">
                                            <div class="row">
                                                <% if(privPurchaseRequestOutletFrame==true){%>
                                                 <div class="col-xs-1 text-center" style="border-right: 1px solid #f4f4f4">
                                                    <div class="knob-label">Purchase Order</div><br>
                                                    <%if((orderPembelianInformation != null) && (orderPembelianInformation.size() > 0)) {%>
                                                    <%
                                                    for (int i = 0; i < orderPembelianInformation.size(); i++) {
                                                         Vector temp = (Vector)orderPembelianInformation.get(i);
                                                    %>
                                                            <%if(i==0 && temp.get(1).equals(0)){%>
                                                                <a href="<%=approotFrame%>/purchasing/material/pom/srcstorerequest.jsp?command=1&FRM_FIELD_PRMSTATUS=0"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#008000"/>
                                                                <div class="knob-label">Draft</div></a><br><br> 
                                                            <%}else if (temp.get(1).equals(1)){%>
                                                                <a href="<%=approotFrame%>/purchasing/material/pom/srcstorerequest.jsp?command=1&FRM_FIELD_PRMSTATUS=1"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#f56954"/>
                                                                <div class="knob-label">To Be Approve</div></a><br>
                                                            <%}else if (temp.get(1).equals(10)){%>
                                                                <a href="<%=approotFrame%>/purchasing/material/pom/srcstorerequest.jsp?command=1&FRM_FIELD_PRMSTATUS=10"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#FFA500"/>
                                                                <div class="knob-label">Approve</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(2)){%>
                                                                <a href="<%=approotFrame%>/purchasing/material/pom/srcstorerequest.jsp?command=1&FRM_FIELD_PRMSTATUS=2"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#FFA500"/>
                                                                <div class="knob-label">Final</div></a><br>
                                                            <%}%>
                                                    <%
                                                    }
                                                    %>
                                                  <%}%>  
                                                  <div class="knob-label"><a href="<%=approotFrame%>/purchasing/material/pom/storerequest_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a></div><br>
                                                  <%--<div class="knob-label"><a href="<%=approotFrame%>/purchasing/material/pom/srcpomaterial.jsp"><button class="btn btn-warning btn-flat">Search</button></a></div>--%>
                                                </div><!-- ./col -->
                                                <%}%>
                                                <% if(privPurchaseRequestWarehouseFrame==true){%>
                                                <div class="col-xs-1 text-center" style="border-right: 1px solid #f4f4f4">
                                                    <div class="knob-label">Purchase Request</div><br>
                                                    <%if((purchaseRequest != null) && (purchaseRequest.size() > 0) ) {%>
                                                    <%
                                                    for (int i = 0; i < purchaseRequest.size(); i++) {
                                                         Vector temp = (Vector)purchaseRequest.get(i);
                                                    %>
                                                            <%if(i==0 && temp.get(1).equals(0)){%>
                                                            <a href="<%=approotFrame%>/purchasing/material/pom/srcprmaterial.jsp?command=1&FRM_FIELD_PRMSTATUS=0"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#008000"/>
                                                            <div class="knob-label">Draft</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(10)){%>
                                                              <a href="<%=approotFrame%>/purchasing/material/pom/srcprmaterial.jsp?command=1&FRM_FIELD_PRMSTATUS=10"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#FFA500"/>
                                                              <div class="knob-label">Approve</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(1)){%>
                                                             <a href="<%=approotFrame%>/purchasing/material/pom/srcprmaterial.jsp?command=1&FRM_FIELD_PRMSTATUS=1"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#f56954"/>
                                                            <div class="knob-label">To Be Approve</div></a><br>
                                                             <%}else if (temp.get(1).equals(2)){%>
                                                             <a href="<%=approotFrame%>/purchasing/material/pom/srcprmaterial.jsp?command=1&FRM_FIELD_PRMSTATUS=2"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#f56954"/>
                                                            <div class="knob-label">Final</div></a><br>
                                                            
                                                            <%}%>
                                                    <%
                                                    }
                                                    %>
                                                  <%}%>  
                                                  <div class="knob-label"><a href="<%=approotFrame%>/purchasing/material/pom/prmaterial_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a></div><br>
                                                  <%--<div class="knob-label"><a href="<%=approotFrame%>/purchasing/material/pom/srcprmaterial.jsp"><button class="btn btn-warning btn-flat">Search</button></a></div>--%>
                                                </div><!-- ./col -->
                                                <%}%>
                                                <%-- PO --%>
                                                <% if(privApprovalPurchaseOrderFrame==true){%>
                                                <div class="col-xs-1 text-center" style="border-right: 1px solid #f4f4f4">
                                                    <div class="knob-label">Purchase Order</div><br>
                                                    <%if((orderPembelianInformation != null) && (orderPembelianInformation.size() > 0)) {%>
                                                    <%
                                                    for (int i = 0; i < orderPembelianInformation.size(); i++) {
                                                         Vector temp = (Vector)orderPembelianInformation.get(i);
                                                    %>
                                                            <%if(i==0 && temp.get(1).equals(0)){%>
                                                                <a href="<%=approotFrame%>/purchasing/material/pom/srcpomaterial.jsp?command=1&FRM_FIELD_PRMSTATUS=0"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#008000"/>
                                                                <div class="knob-label">Draft</div></a><br><br> 
                                                            <%}else if (temp.get(1).equals(1)){%>
                                                                <a href="<%=approotFrame%>/purchasing/material/pom/srcpomaterial.jsp?command=1&FRM_FIELD_PRMSTATUS=1"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#f56954"/>
                                                                <div class="knob-label">To Be Approve</div></a><br>
                                                            <%}else if (temp.get(1).equals(10)){%>
                                                                <a href="<%=approotFrame%>/purchasing/material/pom/srcpomaterial.jsp?command=1&FRM_FIELD_PRMSTATUS=10"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#FFA500"/>
                                                                <div class="knob-label">Approve</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(2)){%>
                                                                <a href="<%=approotFrame%>/purchasing/material/pom/srcpomaterial.jsp?command=1&FRM_FIELD_PRMSTATUS=2"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#FFA500"/>
                                                                <div class="knob-label">Final</div></a><br>
                                                            <%}%>
                                                    <%
                                                    }
                                                    %>
                                                  <%}%>  
                                                  <div class="knob-label"><a href="<%=approotFrame%>/purchasing/material/pom/pomaterial_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a></div><br>
                                                  <%--<div class="knob-label"><a href="<%=approotFrame%>/purchasing/material/pom/srcpomaterial.jsp"><button class="btn btn-warning btn-flat">Search</button></a></div>--%>
                                                </div><!-- ./col -->
                                                <%}%>
                                                
                                               
                                                
                                                <%--Receive From PO--%>
                                                <% if(privApprovalReceiveFromPoFrame==true){%>
                                                <div class="col-xs-1 text-center" style="border-right: 1px solid #f4f4f4">
                                                    <div class="knob-label">Receive From PO</div><br>
                                                    <%if((ReceiveMaterialInformation != null) && (ReceiveMaterialInformation.size() > 0)) {%>
                                                    <%
                                                    for (int i = 0; i < ReceiveMaterialInformation.size(); i++) {
                                                         Vector temp = (Vector)ReceiveMaterialInformation.get(i);
                                                    %>
                                                            <%if(i==0 && temp.get(1).equals(0)){%>
                                                              <a href="<%=approotFrame%>/warehouse/material/receive/src_receive_material.jsp?command=1&FRM_FIELD_RECEIVESTATUS=0&PurchaseID=-2"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#008000"/>
                                                              <div class="knob-label">Draft</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(10)){%>
                                                              <a href="<%=approotFrame%>/warehouse/material/receive/src_receive_material.jsp?command=1&FRM_FIELD_RECEIVESTATUS=10&PurchaseID=-2"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#FFA500"/>
                                                              <div class="knob-label">Approve</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(1)){%>
                                                             <a href="<%=approotFrame%>/warehouse/material/receive/src_receive_material.jsp?command=1&FRM_FIELD_RECEIVESTATUS=1&PurchaseID=-2"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#f56954"/>
                                                            <div class="knob-label">To Be Approve</div></a><br>
                                                            <%}%>
                                                    <%
                                                    }
                                                    %>
                                                  <%}%>  
                                                  <div class="knob-label"><a href="<%=approotFrame%>/warehouse/material/receive/receive_wh_supp_po_material_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a></div><br>
                                                </div><!-- ./col -->
                                                <%}%>
                                                
                                                
                                                
                                                <% if(privApprovalReturnWithPOFrame==true){%>
                                                <div class="col-xs-1 text-center" style="border-right: 1px solid #f4f4f4">
                                                    <div class="knob-label">Return From PO</div><br>
                                                     <%if((returnInformation != null) && (returnInformation.size() > 0)) {%>
                                                    <%
                                                    for (int i = 0; i < returnInformation.size(); i++) {
                                                         Vector temp = (Vector)returnInformation.get(i);
                                                    %>
                                                            <%if(i==0 && temp.get(1).equals(0)){%>
                                                            <a href="<%=approotFrame%>/warehouse/material/return/src_return_material.jsp?command=1&FRM_FIELD_RETURNSTATUS=0&PurchaseID=-2"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#008000"/>
                                                            <div class="knob-label">Draft</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(10)){%>
                                                              <a href="<%=approotFrame%>/warehouse/material/return/src_return_material.jsp?command=1&FRM_FIELD_RETURNSTATUS=10&PurchaseID=-2"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#FFA500"/>
                                                              <div class="knob-label">Approve</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(1)){%>
                                                             <a href="<%=approotFrame%>/warehouse/material/return/src_return_material.jsp?command=1&FRM_FIELD_RETURNSTATUS=1&PurchaseID=-2"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#f56954"/>
                                                            <div class="knob-label">To Be Approve</div></a><br>
                                                            <%}%>
                                                    <%
                                                    }
                                                    %>
                                                  <%}%>  
                                                  <div class="knob-label"><a href="<%=approotFrame%>/warehouse/material/return/return_wh_material_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a></div><br>
                                                  <%--<div class="knob-label"><a href="<%=approotFrame%>/warehouse/material/return/src_return_material.jsp"><button class="btn btn-warning btn-flat">Search</button></a></div>--%>
                                                </div><!-- ./col -->
                                                <%}%>
                                                
                                                <% if(privApprovalReceiveWithoutPoFrame==true){%>
                                                <div class="col-xs-1 text-center" style="border-right: 1px solid #f4f4f4">
                                                    <div class="knob-label">Receive Without PO</div><br>
                                                    <%if((ReceiveMaterialInformationNonPO != null) && (ReceiveMaterialInformationNonPO.size() > 0)) {%>
                                                    <%
                                                    for (int i = 0; i < ReceiveMaterialInformationNonPO.size(); i++) {
                                                         Vector temp = (Vector)ReceiveMaterialInformationNonPO.get(i);
                                                    %>
                                                            <%if(i==0 && temp.get(1).equals(0)){%>
                                                              <a href="<%=approotFrame%>/warehouse/material/receive/src_receive_material.jsp?command=1&FRM_FIELD_RECEIVESTATUS=0&PurchaseID=-1"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#008000"/>
                                                              <div class="knob-label">Draft</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(10)){%>
                                                              <a href="<%=approotFrame%>/warehouse/material/receive/src_receive_material.jsp?command=1&FRM_FIELD_RECEIVESTATUS=10&PurchaseID=-1"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#FFA500"/>
                                                              <div class="knob-label">Approve</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(1)){%>
                                                             <a href="<%=approotFrame%>/warehouse/material/receive/src_receive_material.jsp?command=1&FRM_FIELD_RECEIVESTATUS=1&PurchaseID=-1"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#f56954"/>
                                                            <div class="knob-label">To Be Approve</div></a><br>
                                                            <%}%>
                                                    <%
                                                    }
                                                    %>
                                                  <%}%>   
                                                  <div class="knob-label"><a href="<%=approotFrame%>/warehouse/material/receive/receive_wh_supp_material_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a></div><br>
                                                  <%--<div class="knob-label"><a href="<%=approotFrame%>/warehouse/material/receive/src_receive_material.jsp"><button class="btn btn-warning btn-flat">Search</button></a></div>--%>
                                                </div><!-- ./col -->
                                                <%}%>
                                                <% if(privApprovalReturnWithoutPOFrame==true){%>
                                                <div class="col-xs-1 text-center" style="border-right: 1px solid #f4f4f4">
                                                    <div class="knob-label">Return Without PO</div><br>
                                                    <%if((returnInformationNonPO != null) && (returnInformationNonPO.size() > 0)) {%>
                                                    <%
                                                    for (int i = 0; i < returnInformationNonPO.size(); i++) {
                                                         Vector temp = (Vector)returnInformationNonPO.get(i);
                                                    %>
                                                           <%if(i==0 && temp.get(1).equals(0)){%>
                                                            <a href="<%=approotFrame%>/warehouse/material/return/src_return_material.jsp?command=1&FRM_FIELD_RETURNSTATUS=0&PurchaseID=-1"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#008000"/>
                                                            <div class="knob-label">Draft</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(10)){%>
                                                              <a href="<%=approotFrame%>/warehouse/material/return/src_return_material.jsp?command=1&FRM_FIELD_RETURNSTATUS=10&PurchaseID=-1"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#FFA500"/>
                                                              <div class="knob-label">Approve</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(1)){%>
                                                             <a href="<%=approotFrame%>/warehouse/material/return/src_return_material.jsp?command=1&FRM_FIELD_RETURNSTATUS=1&PurchaseID=-1"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#f56954"/>
                                                            <div class="knob-label">To Be Approve</div></a><br>
                                                            <%}%>
                                                    <%
                                                    }
                                                    %>
                                                    <%}%>  
                                                 <div class="knob-label"><a href="<%=approotFrame%>/warehouse/material/return/return_wh_supp_material_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a></div><br>
                                                </div><!-- ./col -->
                                                <%}%>
                                                
                                                <%--Transfer Request --%>
                                                <% if(privApprovalRequestTransferFrame==true){%>
                                                <div class="col-xs-1 text-center" style="border-right: 1px solid #f4f4f4">
                                                    <div class="knob-label">Transfer Request</div><br>
                                                    <%if((transferRequest != null) && (transferRequest.size() > 0) ) {%>
                                                    <%
                                                    for (int i = 0; i < transferRequest.size(); i++) {
                                                         Vector temp = (Vector)transferRequest.get(i);
                                                    %>
                                                            <%if(i==0 && temp.get(1).equals(0)){%>
                                                            <a href="<%=approotFrame%>/purchasing/material/pom/srcprtowarehousematerial.jsp?command=1&FRM_FIELD_PRMSTATUS=0"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#008000"/>
                                                            <div class="knob-label">Draft</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(10)){%>
                                                              <a href="<%=approotFrame%>/purchasing/material/pom/srcprtowarehousematerial.jsp?command=1&FRM_FIELD_PRMSTATUS=10"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#FFA500"/>
                                                              <div class="knob-label">Approve</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(1)){%>
                                                             <a href="<%=approotFrame%>/purchasing/material/pom/srcprtowarehousematerial.jsp?command=1&FRM_FIELD_PRMSTATUS=1"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#f56954"/>
                                                            <div class="knob-label">To Be Approve</div></a><br>
                                                            <%}%>
                                                    <%
                                                    }
                                                    %>
                                                  <%}%>  
                                                  <div class="knob-label"><a href="<%=approotFrame%>/purchasing/material/pom/prtowarehousematerial_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a></div><br>
                                                  <%--<div class="knob-label"><a href="<%=approotFrame%>/purchasing/material/pom/srcprmaterial.jsp"><button class="btn btn-warning btn-flat">Search</button></a></div>--%>
                                                </div><!-- ./col -->
                                                <%}%>
                                                
                                                <%--Transfer--%>
                                                <% if(privApprovalTransferFrame==true){%>
                                                <div class="col-xs-1 text-center" style="border-right: 1px solid #f4f4f4">
                                                    <div class="knob-label">Transfer</div><br>
                                                    <%if((matDispatchInformation != null) && (matDispatchInformation.size() > 0) ) {%>
                                                    <%
                                                    for (int i = 0; i < matDispatchInformation.size(); i++) {
                                                         Vector temp = (Vector)matDispatchInformation.get(i);
                                                    %>
                                                            <%if(i==0 && temp.get(1).equals(0)){%>
                                                            <a href="<%=approotFrame%>/purchasing/material/pom/srcprtowarehousematerial.jsp?command=1&FRM_FIELD_PRMSTATUS=0"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#008000"/>
                                                            <div class="knob-label">Draft</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(10)){%>
                                                              <a href="<%=approotFrame%>/purchasing/material/pom/srcprtowarehousematerial.jsp?command=1&FRM_FIELD_PRMSTATUS=10"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#FFA500"/>
                                                              <div class="knob-label">Approve</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(1)){%>
                                                             <a href="<%=approotFrame%>/purchasing/material/pom/srcprtowarehousematerial.jsp?command=1&FRM_FIELD_PRMSTATUS=1"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#f56954"/>
                                                            <div class="knob-label">To Be Approve</div></a><br>
                                                            <%}%>
                                                    <%
                                                    }
                                                    %>
                                                  <%}%>  
                                                  <div class="knob-label"><a href="<%=approotFrame%>/warehouse/material/receive/receive_store_wh_material_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a></div><br>
                                                </div><!-- ./col -->
                                                <%}%>
                                                
                                                <%--Receive From Transfer--%>
                                                <% if(privApprovalReceiveTransferFrame==true){%>
                                                <div class="col-xs-1 text-center" style="border-right: 1px solid #f4f4f4">
                                                    <div class="knob-label">Receive From Transfer</div><br>
                                                    <%if((ReceiveMaterialNonSupplierInformation != null) && (ReceiveMaterialNonSupplierInformation.size() > 0)) {%>
                                                    <%
                                                    for (int i = 0; i < ReceiveMaterialNonSupplierInformation.size(); i++) {
                                                         Vector temp = (Vector)ReceiveMaterialNonSupplierInformation.get(i);
                                                    %>
                                                            <%if(i==0 && temp.get(1).equals(0)){%>
                                                              <a href="<%=approotFrame%>/warehouse/material/receive/srcreceive_store_wh_material.jsp?command=1&FRM_FIELD_RECEIVESTATUS=0&PurchaseID=-2"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#008000"/>
                                                              <div class="knob-label">Draft</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(10)){%>
                                                              <a href="<%=approotFrame%>/warehouse/material/receive/srcreceive_store_wh_material.jsp?command=1&FRM_FIELD_RECEIVESTATUS=10&PurchaseID=-2"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#FFA500"/>
                                                              <div class="knob-label">Approve</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(1)){%>
                                                             <a href="<%=approotFrame%>/warehouse/material/receive/srcreceive_store_wh_material.jsp?command=1&FRM_FIELD_RECEIVESTATUS=1&PurchaseID=-2"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#f56954"/>
                                                            <div class="knob-label">To Be Approve</div></a><br>
                                                            <%}%>
                                                    <%
                                                    }
                                                    %>
                                                  <%}%>  
                                                  <div class="knob-label"><a href="<%=approotFrame%>/warehouse/material/receive/receive_store_wh_material_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a></div><br>
                                                </div><!-- ./col -->
                                                <%}%>
                                                
                                                <%--Transfer Unit--%>
                                                <% if(privApprovalTransferUnitFrame==true){%>
                                                <div class="col-xs-1 text-center" style="border-right: 1px solid #f4f4f4">
                                                    <div class="knob-label">Transfer Unit</div><br>
                                                    <%if((matDispatchUnitInformation != null) && (matDispatchUnitInformation.size() > 0) ) {%>
                                                    <%
                                                    for (int i = 0; i < matDispatchUnitInformation.size(); i++) {
                                                         Vector temp = (Vector)matDispatchUnitInformation.get(i);
                                                    %>
                                                            <%if(i==0 && temp.get(1).equals(0)){%>
                                                            <a href="<%=approotFrame%>/purchasing/material/pom/srcprtowarehousematerial.jsp?command=1&FRM_FIELD_PRMSTATUS=0"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#008000"/>
                                                            <div class="knob-label">Draft</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(10)){%>
                                                              <a href="<%=approotFrame%>/purchasing/material/pom/srcprtowarehousematerial.jsp?command=1&FRM_FIELD_PRMSTATUS=10"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#FFA500"/>
                                                              <div class="knob-label">Approve</div></a><br><br>
                                                            <%}else if (temp.get(1).equals(1)){%>
                                                             <a href="<%=approotFrame%>/purchasing/material/pom/srcprtowarehousematerial.jsp?command=1&FRM_FIELD_PRMSTATUS=1"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#f56954"/>
                                                            <div class="knob-label">To Be Approve</div></a><br>
                                                            <%}%>
                                                    <%
                                                    }
                                                    %>
                                                  <%}%>  
                                                  <div class="knob-label"><a href="<%=approotFrame%>/warehouse/material/receive/receive_store_wh_material_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a></div><br>
                                                </div><!-- ./col -->
                                                <%}%>
                                                
                                                <%--Costing--%>
                                                <% if(privApprovalCostingFrame==true){%>
                                                    <div class="col-xs-1 text-center" style="border-right: 1px solid #f4f4f4">
                                                        <div class="knob-label">Costing</div><br>
                                                        <%if((pembiayaanInformation != null) && (pembiayaanInformation.size() > 0) ) {%>
                                                            <%
                                                            for (int i = 0; i < pembiayaanInformation.size(); i++) {
                                                                 Vector temp = (Vector)pembiayaanInformation.get(i);
                                                            %>
                                                                    <%if(i==0 && temp.get(1).equals(0)){%>
                                                                    <a href="<%=approotFrame%>/warehouse/material/dispatch/srccosting_material.jsp?command=1&FRM_FIELD_PRMSTATUS=0&FRM_FIELD_IGNORE_DATE=0"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#008000"/>
                                                                    <div class="knob-label">Draft</div></a><br><br>
                                                                    <%}else if (temp.get(1).equals(1)){%>
                                                                     <a href="<%=approotFrame%>/warehouse/material/dispatch/srccosting_material.jsp?command=1&FRM_FIELD_PRMSTATUS=1&FRM_FIELD_IGNORE_DATE=0"><input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#f56954"/>
                                                                    <div class="knob-label">To Be Approve</div></a><br>
                                                                    <%}%>
                                                            <%
                                                            }
                                                            %>
                                                      <%}%>  
                                                      <div class="knob-label"><a href="<%=approotFrame%>/warehouse/material/dispatch/costing_material_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a></div><br>
                                                    </div><!-- ./col -->
                                                <%}%>
                                                
                                                <%-- sales --%>
                                                <div class="col-xs-1 text-center" style="border-right: 1px solid #f4f4f4">
                                                        <div class="knob-label">Penjualan</div><br>
                                                        <%if((salesInformation != null) && (salesInformation.size() > 0) ) {%>
                                                            <%
                                                            for (int i = 0; i < salesInformation.size(); i++) {
                                                                 Vector temp = (Vector)salesInformation.get(i);
                                                            %>
                                                                    <%if(i==0 && temp.get(1).equals(0)){%>
                                                                        <input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#008000"/>
                                                                        <div class="knob-label">Draft</div><br><br>
                                                                    <%}else if (temp.get(1).equals(2)){%>
                                                                       <input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#f56954"/>
                                                                       <div class="knob-label">Final</div><br>
                                                                    <%}%>
                                                            <%
                                                            }
                                                            %>
                                                      <%}%>  
                                                      <div class="knob-label"><a href="<%=approotFrame%>/master/posting/posting_stock_new.jsp"><button class="btn btn-info btn-flat">Posting</button></a></div><br>
                                                </div><!-- ./col -->
                                                
                                                <%-- Material Item --%>
                                                <div class="col-xs-1 text-center" style="border-right: 1px solid #f4f4f4">
                                                        <div class="knob-label">Informasi</div><br>
                                                        <%if((itemInformation != null) && (itemInformation.size() > 0) ) {%>
                                                            <%
                                                            for (int i = 0; i < itemInformation.size(); i++) {
                                                                 Vector temp = (Vector)itemInformation.get(i);
                                                                 Integer tympe = (Integer) temp.get(1);
                                                            %>
                                                                    <%if(tympe==-1){%>
                                                                    <a href="<%=approotFrame%>/master/material/material_list.jsp?typehome=1">
                                                                        <input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#008000"/>
                                                                        <div class="knob-label">Item hpp = 0</div>
                                                                    </a><br><br>
                                                                    <%}else if (tympe == 1){%>
                                                                    <a href="<%=approotFrame%>/master/material/material_list.jsp?typehome=2">
                                                                       <input type="text" class="knob" data-readonly="true" value="<%=temp.get(0)%>" data-width="60" data-height="60" data-fgColor="#f56954"/>
                                                                       <div class="knob-label">Hpp > Price (<%=temp.get(2)%>)</div>
                                                                    </a><br>
                                                                    <%}%>
                                                            <%
                                                            }
                                                            %>
                                                      <%}%> 
                                                </div><!-- ./col -->
                                            </div><!-- /.row -->
                                        </div><!-- /.box-footer -->
                                    </div><!-- /.box --> 
                                </div>
                            </div>
                        </section>
                    </div>
                </section><!-- /.content -->
            </div><!-- /.right-side -->
        </div><!-- ./wrapper -->
        <!-- add new calendar event modal -->
        <!-- jQuery 2.0.2 -->
        <script src="styles/bootstrap3.1/js/jquery.min.js"></script>
        <!-- jQuery UI 1.10.3 -->
        <script src="styles/bootstrap3.1/js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
        <!-- Bootstrap -->
        <script src="styles/bootstrap3.1/js/bootstrap.min.js" type="text/javascript"></script>
        <!-- Sparkline -->
        <script src="styles/bootstrap3.1/js/plugins/sparkline/jquery.sparkline.min.js" type="text/javascript"></script>
        <!-- jQuery Knob Chart -->
        <script src="styles/bootstrap3.1/js/plugins/jqueryKnob/jquery.knob.js" type="text/javascript"></script>
        <!-- daterangepicker -->
        <script src="styles/bootstrap3.1/js/plugins/daterangepicker/daterangepicker.js" type="text/javascript"></script>
        <!-- Bootstrap WYSIHTML5 -->
        <script src="styles/bootstrap3.1/js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" type="text/javascript"></script>
        <!-- AdminLTE App -->
        <script src="styles/bootstrap3.1/js/AdminLTE/app.js" type="text/javascript"></script>
        <!-- AdminLTE dashboard demo (This is only for demo purposes) -->
        <script src="styles/bootstrap3.1/js/AdminLTE/dashboard.js" type="text/javascript"></script>    
    </body>
</html>