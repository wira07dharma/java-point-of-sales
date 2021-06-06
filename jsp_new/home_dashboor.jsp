<%-- 
    Document   : home_dashboor
    Created on : Aug 6, 2014, 12:36:35 PM
    Author     : dimata005
--%>
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
<%
        //int vecSize=0;

        int appObjCodeKasirPayment = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_KASIR, AppObjInfo.OBJ_KASIR_PAYMENT) ;
        boolean privApprovalKasirPayment = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeKasirPayment, AppObjInfo.COMMAND_VIEW));

        //werehouse menu
        boolean privPurchaseRequestWarehouse = userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_PURCHASE_REQUEST_WAREHOUSE), AppObjInfo.COMMAND_VIEW));
        boolean privPurchaseRequestOutlet = userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_PURCHASE_REQUEST_OUTLET), AppObjInfo.COMMAND_VIEW));
        boolean privApprovalPurchaseOrder =userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_PURCHASE_ORDER), AppObjInfo.COMMAND_VIEW));

        boolean privApprovalReceiveFromPo = userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_RECEIVE_FROM_PO), AppObjInfo.COMMAND_VIEW));
        boolean privApprovalReceiveWithoutPo = userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_RECEIVE_WITHOUT_PO), AppObjInfo.COMMAND_VIEW));

        boolean privApprovalReturnWithPO= userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_RETURN_WITH_PO), AppObjInfo.COMMAND_VIEW));
        boolean privApprovalReturnWithoutPO = userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_RETURN_WITHOUT_PO), AppObjInfo.COMMAND_VIEW));

        boolean privApprovalRequestTransfer =  userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_REQUEST_TRANSFER), AppObjInfo.COMMAND_VIEW));
        boolean privApprovalTransfer = userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo. OBJ_MENU_HOME_TRANSFER ), AppObjInfo.COMMAND_VIEW));
        boolean privApprovalTransferUnit = userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo. OBJ_MENU_HOME_TRANSFER_UNIT ), AppObjInfo.COMMAND_VIEW)); 
        boolean privApprovalReceiveTransfer =userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_RECEIVE_TRANSFER ), AppObjInfo.COMMAND_VIEW));

        boolean privApprovalCosting =userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_COSTING ), AppObjInfo.COMMAND_VIEW));

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
        int vecSizeOpnema =0;
        
        SessMatReceive sessMatReceive = new SessMatReceive();
        SessMatDispatch sessMatDispatch = new SessMatDispatch();
        SessMatReturn sessMatReturn = new SessMatReturn();
        SessMatCosting sessMatCosting = new SessMatCosting();
        SessPurchaseOrder sessPurchaseOrder = new SessPurchaseOrder();
        SessMatStockOpname sessMatStockOpname = new SessMatStockOpname();

        //update opie-eyek 20130805
        if(MODUS_TRANSFER == MODUS_TRANSFER_SERVER){
            String whereClausex = " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");
            
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
            
        }
    
    %>

<div class="col-lg-12">
<%--Store Request --%>    
<%if(privPurchaseRequestOutlet==true){%>
    <div class="col-lg-3">
        <div class="panel panel-default">
            <div class="panel-heading">
                <i class="fa fa-bar-chart-o fa-fw"></i> Store Request
            </div>
            <div class="panel-body">
                <%if((orderPembelianInformation != null) && (orderPembelianInformation.size() > 0)) {%>
                    <%
                    for (int i = 0; i < orderPembelianInformation.size(); i++) {
                         Vector temp = (Vector)orderPembelianInformation.get(i);
                    %>
                        <%if(i==0 && temp.get(1).equals(0)){%>
                            <div class="circleStatsItem orange">
                                <div style="width:60px;display:inline">
                                    <span class="percent">Draft</span>
                                    <canvas width="60" height="60"></canvas>
                                    <a href="<%=approot%>/purchasing/material/pom/srcstorerequest.jsp?command=1&FRM_FIELD_PRMSTATUS=0"><input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                    </a>    
                                </div>
                            </div> 
                        <%}else if (temp.get(1).equals(1)){%>
                            <div class="circleStatsItem orange">
                                 <div style="width:60px;display:inline">
                                    <span class="percent">ToBe Apprv</span>
                                    <canvas width="60" height="60"></canvas><a href="<%=approot%>/purchasing/material/pom/srcstorerequest.jsp?command=1&FRM_FIELD_PRMSTATUS=1">
                                    <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                </a></div>
                            </div>
                        <%}else if (temp.get(1).equals(10)){%>
                            <div class="circleStatsItem orange">
                                <div style="width:60px;display:inline">
                                    <span class="percent">Approve</span>
                                    <canvas width="60" height="60"></canvas><a href="<%=approot%>/purchasing/material/pom/srcstorerequest.jsp?command=1&FRM_FIELD_PRMSTATUS=10">
                                    <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                </a></div>
                            </div>
                        <%}else if (temp.get(1).equals(2)){%>
                            <div class="circleStatsItem orange">
                                 <div style="width:60px;display:inline">
                                    <span class="percent">Final</span>
                                    <canvas width="60" height="60"></canvas> <a href="<%=approot%>/purchasing/material/pom/srcstorerequest.jsp?command=1&FRM_FIELD_PRMSTATUS=2">
                                    <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                </div>
                            </div>
                        <%}%>
                
                    <%
                    }
                    %>
              <%}%> 
              <a href="<%=approot%>/purchasing/material/pom/storerequest_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a>
            </div>
        </div>
    </div>
<%}%>

<%--Purchase Request --%>    
<%if(privPurchaseRequestWarehouse==true){%>
    <div class="col-lg-3">
        <div class="panel panel-default">
            <div class="panel-heading">
                <i class="fa fa-bar-chart-o fa-fw"></i> Purchase Request
            </div>
            <div class="panel-body">
                <%if((purchaseRequest != null) && (purchaseRequest.size() > 0)) {%>
                    <%
                    for (int i = 0; i < purchaseRequest.size(); i++) {
                         Vector temp = (Vector)purchaseRequest.get(i);
                    %>
                        <div class="circleStatsItem orange">
                            <%if(i==0 && temp.get(1).equals(0)){%>
                                    <div style="width:60px;display:inline">
                                        <span class="percent">Draft</span>
                                        <canvas width="60" height="60"></canvas>
                                        <a href="<%=approot%>/purchasing/material/pom/srcprmaterial.jsp?command=1&FRM_FIELD_PRMSTATUS=0"><input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                        </a>    
                                    </div>
                            <%}else if (temp.get(1).equals(1)){%>
                                     <div style="width:60px;display:inline">
                                        <span class="percent">Approve</span>
                                        <canvas width="60" height="60"></canvas><a href="<%=approot%>/purchasing/material/pom/srcprmaterial.jsp?command=1&FRM_FIELD_PRMSTATUS=10">
                                        <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                    </a></div>
                            <%}else if (temp.get(1).equals(10)){%>
                                    <div style="width:60px;display:inline">
                                        <span class="percent">ToBe Approve</span>
                                        <canvas width="60" height="60"></canvas><a href="<%=approot%>/purchasing/material/pom/srcprmaterial.jsp?command=1&FRM_FIELD_PRMSTATUS=1">
                                        <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                    </a></div>
                            <%}else if (temp.get(1).equals(2)){%>
                                     <div style="width:60px;display:inline">
                                        <span class="percent">Final</span>
                                        <canvas width="60" height="60"></canvas> <a href="<%=approot%>/purchasing/material/pom/srcprmaterial.jsp?command=1&FRM_FIELD_PRMSTATUS=2">
                                        <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                    </div>
                            <%}%>
                        </div> 
                    <%
                    }
                    %>
              <%}%> 
              <a href="<%=approot%>/purchasing/material/pom/prmaterial_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a>
            </div>
        </div>
    </div>
<%}%>    

<%--Purchase Order --%>    
<%if(privApprovalPurchaseOrder==true){%>
    <div class="col-lg-3">
        <div class="panel panel-default">
            <div class="panel-heading">
                <i class="fa fa-bar-chart-o fa-fw"></i> Purchase Order
            </div>
            <div class="panel-body">
                <%if((orderPembelianInformation != null) && (orderPembelianInformation.size() > 0)) {%>
                    <%
                    for (int i = 0; i < orderPembelianInformation.size(); i++) {
                         Vector temp = (Vector)orderPembelianInformation.get(i);
                    %>
                        <div class="circleStatsItem orange">
                            <%if(i==0 && temp.get(1).equals(0)){%>
                                    <div style="width:60px;display:inline">
                                        <span class="percent">Draft</span>
                                        <canvas width="60" height="60"></canvas>
                                        <a href="<%=approot%>/purchasing/material/pom/srcpomaterial.jsp?command=1&FRM_FIELD_PRMSTATUS=0"><input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                        </a>    
                                    </div>
                            <%}else if (temp.get(1).equals(1)){%>
                                     <div style="width:60px;display:inline">
                                        <span class="percent">ToBe Approve</span>
                                        <canvas width="60" height="60"></canvas><a href="<%=approot%>/purchasing/material/pom/srcpomaterial.jsp?command=1&FRM_FIELD_PRMSTATUS=1">
                                        <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                    </a></div>
                            <%}else if (temp.get(1).equals(10)){%>
                                    <div style="width:60px;display:inline">
                                        <span class="percent">Approve</span>
                                        <canvas width="60" height="60"></canvas><a href="<%=approot%>/purchasing/material/pom/srcpomaterial.jsp?command=1&FRM_FIELD_PRMSTATUS=10">
                                        <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                    </a></div>
                            <%}else if (temp.get(1).equals(2)){%>
                                     <div style="width:60px;display:inline">
                                        <span class="percent">Final</span>
                                        <canvas width="60" height="60"></canvas> <a href="<%=approot%>/purchasing/material/pom/srcpomaterial.jsp?command=1&FRM_FIELD_PRMSTATUS=2">
                                        <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                    </div>
                            <%}%>
                        </div> 
                    <%
                    }
                    %>
              <%}%> 
              <a href="<%=approot%>/purchasing/material/pom/pomaterial_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a>
            </div>
        </div>
    </div>
<%}%>  

 <%--Receive From PO--%>   
<%if(privApprovalReceiveFromPo==true){%>
    <div class="col-lg-3">
        <div class="panel panel-default">
            <div class="panel-heading">
                <i class="fa fa-bar-chart-o fa-fw"></i> Receive From PO
            </div>
            <div class="panel-body">
                <%if((ReceiveMaterialInformation != null) && (ReceiveMaterialInformation.size() > 0)) {%>
                    <%
                    for (int i = 0; i < ReceiveMaterialInformation.size(); i++) {
                         Vector temp = (Vector)ReceiveMaterialInformation.get(i);
                    %>
                        <div class="circleStatsItem orange">
                            <%if(i==0 && temp.get(1).equals(0)){%>
                                    <div style="width:60px;display:inline">
                                        <span class="percent">Draft</span>
                                        <canvas width="60" height="60"></canvas>
                                        <a href="<%=approot%>/warehouse/material/receive/src_receive_material.jsp?command=1&FRM_FIELD_RECEIVESTATUS=0&PurchaseID=-2"><input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                        </a>    
                                    </div>
                            <%}else if (temp.get(1).equals(10)){%>
                                    <div style="width:60px;display:inline">
                                        <span class="percent">Approve</span>
                                        <canvas width="60" height="60"></canvas><a href="<%=approot%>/warehouse/material/receive/src_receive_material.jsp?command=1&FRM_FIELD_RECEIVESTATUS=10&PurchaseID=-2">
                                        <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                    </a></div>
                            <%}else if (temp.get(1).equals(1)){%>
                                     <div style="width:60px;display:inline">
                                        <span class="percent">ToBe Approv</span>
                                        <canvas width="60" height="60"></canvas> <a href="<%=approot%>/warehouse/material/receive/src_receive_material.jsp?command=1&FRM_FIELD_RECEIVESTATUS=1&PurchaseID=-2">
                                        <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                    </div>
                            <%}%>
                        </div> 
                    <%
                    }
                    %>
              <%}%> 
              <a href="<%=approot%>/warehouse/material/receive/receive_wh_supp_po_material_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a>
            </div>
        </div>
    </div>
<%}%>  

 <%--Return From PO--%>   
<%if(privApprovalReturnWithPO==true){%>
    <div class="col-lg-3">
        <div class="panel panel-default">
            <div class="panel-heading">
                <i class="fa fa-bar-chart-o fa-fw"></i> Return From PO
            </div>
            <div class="panel-body">
                <%if((returnInformation != null) && (returnInformation.size() > 0)) {%>
                    <%
                    for (int i = 0; i < returnInformation.size(); i++) {
                         Vector temp = (Vector)returnInformation.get(i);
                    %>
                        <div class="circleStatsItem orange">
                            <%if(i==0 && temp.get(1).equals(0)){%>
                                    <div style="width:60px;display:inline">
                                        <span class="percent">Draft</span>
                                        <canvas width="60" height="60"></canvas>
                                        <a href="<%=approot%>/warehouse/material/return/src_return_material.jsp?command=1&FRM_FIELD_RETURNSTATUS=0&PurchaseID=-2"><input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                        </a>    
                                    </div>
                            <%}else if (temp.get(1).equals(10)){%>
                                    <div style="width:60px;display:inline">
                                        <span class="percent">Approve</span>
                                        <canvas width="60" height="60"></canvas><a href="<%=approot%>/warehouse/material/return/src_return_material.jsp?command=1&FRM_FIELD_RETURNSTATUS=10&PurchaseID=-2">
                                        <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                    </a></div>
                            <%}else if (temp.get(1).equals(1)){%>
                                     <div style="width:60px;display:inline">
                                        <span class="percent">ToBe Approv</span>
                                        <canvas width="60" height="60"></canvas> <a href="<%=approot%>/warehouse/material/return/src_return_material.jsp?command=1&FRM_FIELD_RETURNSTATUS=1&PurchaseID=-2">
                                        <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                    </div>
                            <%}%>
                        </div> 
                    <%
                    }
                    %>
              <%}%> 
              <a href="<%=approot%>/warehouse/material/return/return_wh_material_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a>
            </div>
        </div>
    </div>
<%}%>  
</div>

<div class="col-lg-12">
<%--Receive without PO--%>   
<%if(privApprovalReceiveWithoutPo==true){%>
    <div class="col-lg-3">
        <div class="panel panel-default">
            <div class="panel-heading">
                <i class="fa fa-bar-chart-o fa-fw"></i> Return Without PO
            </div>
            <div class="panel-body">
                <%if((ReceiveMaterialInformationNonPO != null) && (ReceiveMaterialInformationNonPO.size() > 0)) {%>
                    <%
                    for (int i = 0; i < ReceiveMaterialInformationNonPO.size(); i++) {
                         Vector temp = (Vector)ReceiveMaterialInformationNonPO.get(i);
                    %>
                        <div class="circleStatsItem orange">
                            <%if(i==0 && temp.get(1).equals(0)){%>
                                    <div style="width:60px;display:inline">
                                        <span class="percent">Draft</span>
                                        <canvas width="60" height="60"></canvas>
                                        <a href="<%=approot%>/warehouse/material/receive/src_receive_material.jsp?command=1&FRM_FIELD_RECEIVESTATUS=0&PurchaseID=-1"><input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                        </a>    
                                    </div>
                            <%}else if (temp.get(1).equals(10)){%>
                                    <div style="width:60px;display:inline">
                                        <span class="percent">Approve</span>
                                        <canvas width="60" height="60"></canvas>
                                        <a href="<%=approot%>/warehouse/material/receive/src_receive_material.jsp?command=1&FRM_FIELD_RECEIVESTATUS=10&PurchaseID=-1">
                                        <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                    </a></div>
                            <%}else if (temp.get(1).equals(1)){%>
                                     <div style="width:60px;display:inline">
                                        <span class="percent">ToBe Approv</span>
                                        <canvas width="60" height="60"></canvas> 
                                        <a href="<%=approot%>/warehouse/material/receive/src_receive_material.jsp?command=1&FRM_FIELD_RECEIVESTATUS=1&PurchaseID=-1">
                                        <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                    </a></div>
                            <%}%>
                        </div> 
                    <%
                    }
                    %>
              <%}%> 
              <a href="<%=approot%>/warehouse/material/receive/receive_wh_supp_material_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a>
            </div>
        </div>
    </div>
<%}%>  

<%--Return without PO--%>   
<%if(privApprovalReturnWithoutPO==true){%>
    <div class="col-lg-3">
        <div class="panel panel-default">
            <div class="panel-heading">
                <i class="fa fa-bar-chart-o fa-fw"></i> Return Without PO
            </div>
            <div class="panel-body">
                <%if((returnInformationNonPO != null) && (returnInformationNonPO.size() > 0)) {%>
                    <%
                    for (int i = 0; i < returnInformationNonPO.size(); i++) {
                         Vector temp = (Vector)returnInformationNonPO.get(i);
                    %>
                        <div class="circleStatsItem orange">
                            <%if(i==0 && temp.get(1).equals(0)){%>
                                    <div style="width:60px;display:inline">
                                        <span class="percent">Draft</span>
                                        <canvas width="60" height="60"></canvas>
                                        <a href="<%=approot%>/warehouse/material/return/src_return_material.jsp?command=1&FRM_FIELD_RETURNSTATUS=0&PurchaseID=-1"><input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                        </a>    
                                    </div>
                            <%}else if (temp.get(1).equals(10)){%>
                                    <div style="width:60px;display:inline">
                                        <span class="percent">Approve</span>
                                        <canvas width="60" height="60"></canvas>
                                        <a href="<%=approot%>/warehouse/material/return/src_return_material.jsp?command=1&FRM_FIELD_RETURNSTATUS=10&PurchaseID=-1">
                                        <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                    </a></div>
                            <%}else if (temp.get(1).equals(1)){%>
                                     <div style="width:60px;display:inline">
                                        <span class="percent">ToBe Approv</span>
                                        <canvas width="60" height="60"></canvas> 
                                        <a href="<%=approot%>/warehouse/material/return/src_return_material.jsp?command=1&FRM_FIELD_RETURNSTATUS=1&PurchaseID=-1">
                                        <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                    </a></div>
                            <%}%>
                        </div> 
                    <%
                    }
                    %>
              <%}%> 
              <a href="<%=approot%>/warehouse/material/return/return_wh_supp_material_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a>
            </div>
        </div>
    </div>
<%}%>  
</div>
<div class="col-lg-12">
    <%--Transfer Request--%>   
    <%if(privApprovalRequestTransfer==true){%>
        <div class="col-lg-3">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <i class="fa fa-bar-chart-o fa-fw"></i> Transfer Request
                </div>
                <div class="panel-body">
                    <%if((transferRequest!= null) && (transferRequest.size() > 0)) {%>
                        <%
                        for (int i = 0; i < transferRequest.size(); i++) {
                             Vector temp = (Vector)transferRequest.get(i);
                        %>
                            <div class="circleStatsItem orange">
                                <%if(i==0 && temp.get(1).equals(0)){%>
                                        <div style="width:60px;display:inline">
                                            <span class="percent">Draft</span>
                                            <canvas width="60" height="60"></canvas>
                                            <a href="<%=approot%>/purchasing/material/pom/srcprtowarehousematerial.jsp?command=1&FRM_FIELD_PRMSTATUS=0"><input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                            </a>    
                                        </div>
                                <%}else if (temp.get(1).equals(10)){%>
                                        <div style="width:60px;display:inline">
                                            <span class="percent">Approve</span>
                                            <canvas width="60" height="60"></canvas>
                                            <a href="<%=approot%>/purchasing/material/pom/srcprtowarehousematerial.jsp?command=1&FRM_FIELD_PRMSTATUS=10">
                                            <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                        </a></div>
                                <%}else if (temp.get(1).equals(1)){%>
                                         <div style="width:60px;display:inline">
                                            <span class="percent">ToBe Approv</span>
                                            <canvas width="60" height="60"></canvas> 
                                            <a href="<%=approot%>/purchasing/material/pom/srcprtowarehousematerial.jsp?command=1&FRM_FIELD_PRMSTATUS=1">
                                            <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                        </a></div>
                                <%}%>
                            </div> 
                        <%
                        }
                        %>
                  <%}%> 
                  <a href="<%=approot%>/purchasing/material/pom/prtowarehousematerial_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a>
                </div>
            </div>
        </div>
    <%}%> 

    <%--Transfer--%>   
    <%if(privApprovalTransfer==true){%>
        <div class="col-lg-3">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <i class="fa fa-bar-chart-o fa-fw"></i> Transfer
                </div>
                <div class="panel-body">
                    <%if((matDispatchInformation!= null) && (matDispatchInformation.size() > 0)) {%>
                        <%
                        for (int i = 0; i < matDispatchInformation.size(); i++) {
                             Vector temp = (Vector)matDispatchInformation.get(i);
                        %>
                            <div class="circleStatsItem orange">
                                <%if(i==0 && temp.get(1).equals(0)){%>
                                        <div style="width:60px;display:inline">
                                            <span class="percent">Draft</span>
                                            <canvas width="60" height="60"></canvas>
                                            <a href="<%=approot%>/purchasing/material/pom/srcprtowarehousematerial.jsp?command=1&FRM_FIELD_PRMSTATUS=0"><input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                            </a>    
                                        </div>
                                <%}else if (temp.get(1).equals(10)){%>
                                        <div style="width:60px;display:inline">
                                            <span class="percent">Approve</span>
                                            <canvas width="60" height="60"></canvas>
                                            <a href="<%=approot%>/purchasing/material/pom/srcprtowarehousematerial.jsp?command=1&FRM_FIELD_PRMSTATUS=10">
                                            <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                        </a></div>
                                <%}else if (temp.get(1).equals(1)){%>
                                         <div style="width:60px;display:inline">
                                            <span class="percent">ToBe Approv</span>
                                            <canvas width="60" height="60"></canvas> 
                                            <a href="<%=approot%>/purchasing/material/pom/srcprtowarehousematerial.jsp?command=1&FRM_FIELD_PRMSTATUS=1">
                                            <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                        </a></div>
                                <%}%>
                            </div> 
                        <%
                        }
                        %>
                  <%}%> 
                  <a href="<%=approot%>/warehouse/material/receive/receive_store_wh_material_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a>
                </div>
            </div>
        </div>
    <%}%>  

    <%-- Receive Transfer--%>   
    <%if(privApprovalReceiveTransfer==true){%>
        <div class="col-lg-3">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <i class="fa fa-bar-chart-o fa-fw"></i>Receive From Transfer
                </div>
                <div class="panel-body">
                    <%if((ReceiveMaterialNonSupplierInformation!= null) && (ReceiveMaterialNonSupplierInformation.size() > 0)) {%>
                        <%
                        for (int i = 0; i < ReceiveMaterialNonSupplierInformation.size(); i++) {
                             Vector temp = (Vector)ReceiveMaterialNonSupplierInformation.get(i);
                        %>
                            <div class="circleStatsItem orange">
                                <%if(i==0 && temp.get(1).equals(0)){%>
                                        <div style="width:60px;display:inline">
                                            <span class="percent">Draft</span>
                                            <canvas width="60" height="60"></canvas>
                                            <a href="<%=approot%>/warehouse/material/receive/srcreceive_store_wh_material.jsp?command=1&FRM_FIELD_RECEIVESTATUS=0&PurchaseID=-2"><input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                            </a>    
                                        </div>
                                <%}else if (temp.get(1).equals(10)){%>
                                        <div style="width:60px;display:inline">
                                            <span class="percent">Approve</span>
                                            <canvas width="60" height="60"></canvas>
                                            <a href="<%=approot%>/warehouse/material/receive/srcreceive_store_wh_material.jsp?command=1&FRM_FIELD_RECEIVESTATUS=10&PurchaseID=-2">
                                            <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                        </a></div>
                                <%}else if (temp.get(1).equals(1)){%>
                                         <div style="width:60px;display:inline">
                                            <span class="percent">ToBe Approv</span>
                                            <canvas width="60" height="60"></canvas> 
                                            <a href="<%=approot%>/warehouse/material/receive/srcreceive_store_wh_material.jsp?command=1&FRM_FIELD_RECEIVESTATUS=1&PurchaseID=-2">
                                            <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                        </a></div>
                                <%}%>
                            </div> 
                        <%
                        }
                        %>
                  <%}%> 
                  <a href="<%=approot%>/warehouse/material/receive/receive_store_wh_material_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a>
                </div>
            </div>
        </div>
    <%}%>
</div>

<div class="col-lg-12">
    <%-- Costing--%>   
    <%if(privApprovalCosting==true){%>
        <div class="col-lg-3">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <i class="fa fa-bar-chart-o fa-fw"></i>Costing
                </div>
                <div class="panel-body">
                    <%if((pembiayaanInformation!= null) && (pembiayaanInformation.size() > 0)) {%>
                        <%
                        for (int i = 0; i < pembiayaanInformation.size(); i++) {
                             Vector temp = (Vector)pembiayaanInformation.get(i);
                        %>
                            <div class="circleStatsItem orange">
                                <%if(i==0 && temp.get(1).equals(0)){%>
                                        <div style="width:60px;display:inline">
                                            <span class="percent">Draft</span>
                                            <canvas width="60" height="60"></canvas>
                                            <a href="<%=approot%>/warehouse/material/dispatch/srccosting_material.jsp?command=1&FRM_FIELD_PRMSTATUS=0"><input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                            </a>    
                                        </div>
                                <%}else if (temp.get(1).equals(1)){%>
                                         <div style="width:60px;display:inline">
                                            <span class="percent">ToBe Approv</span>
                                            <canvas width="60" height="60"></canvas> 
                                            <a href="<%=approot%>/warehouse/material/dispatch/srccosting_material.jsp?command=1&FRM_FIELD_PRMSTATUS=1">
                                            <input readonly="readonly" style="width: 60px; position: absolute; margin-top: -10px; margin-left: -110px; font-size: 30px; border: medium none; background: none repeat scroll 0% 0% transparent; font-family: Arial; font-weight: bold; text-align: center; color: rgb(250, 96, 61); padding: 0px;" value="<%=temp.get(0)%>" class="circleChart" type="text">
                                        </a></div>
                                <%}%>
                            </div> 
                        <%
                        }
                        %>
                  <%}%> 
                  <a href="<%=approot%>/warehouse/material/dispatch/costing_material_edit.jsp?command=<%=Command.ADD%>&approval_command=<%=Command.SAVE%>&add_type=0"><button class="btn btn-info btn-flat">Add</button></a>
                </div>
            </div>
        </div>
    <%}%>
</div>