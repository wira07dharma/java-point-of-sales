<%-- 
    Document   : export_excel_return_penerimaan
    Created on : Jan 14, 2019, 3:56:46 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.warehouse.PstMatReturn"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.qdep.form.FRMHandler"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterType"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialMappingType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterType"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstColor"%>
<%@page import="com.dimata.posbo.entity.masterdata.Color"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReturnItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReturnItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReturn"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatReturn"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="java.util.Date"%>
<%@page import="com.dimata.util.Formater"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%//
    int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RETURN, AppObjInfo.G2_SUPPLIER_RETURN, AppObjInfo.OBJ_SUPPLIER_RETURN);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%    long oidReturnMaterial = FRMQueryString.requestLong(request, "return_id");

    CtrlMatReturn ctrlMatReturn = new CtrlMatReturn(request);
    int iErrCode = ctrlMatReturn.action(Command.EDIT, oidReturnMaterial, userName, userId);
    String errMsg = ctrlMatReturn.getMessage();

    MatReturn ret = ctrlMatReturn.getMatReturn();
    String whereClauseItem = "" + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] + "=" + oidReturnMaterial;
    Vector<Vector> listMatReturnItem = PstMatReturnItem.list(0, 0, whereClauseItem);
    
    Location returnLocation = new Location();
    ContactList supplier = new ContactList();
    try {
        returnLocation = PstLocation.fetchExc(ret.getLocationId());
        supplier = PstContactList.fetchExc(ret.getSupplierId());
    } catch (Exception e) {
        
    }
	
	String signRet1 = PstSystemProperty.getValueByName("SIGN_RETURN_1");
    String signRet2 = PstSystemProperty.getValueByName("SIGN_RETURN_2");
    String signRet3 = PstSystemProperty.getValueByName("SIGN_RETURN_3");
	
	String nameSignRet1 = PstSystemProperty.getValueByName("NAME_SIGN_RETURN_1");
    String nameSignRet2 = PstSystemProperty.getValueByName("NAME_SIGN_RETURN_2");
    String nameSignRet3 = PstSystemProperty.getValueByName("NAME_SIGN_RETURN_3");
    
	AppUser appUser = userSession.getAppUser();
	
    response.setContentType("application/x-msexcel"); 
    response.setHeader("Content-Disposition","attachment; filename=" + "Return_Penerimaan_"+ret.getRetCode()+".xls" );
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Export Return Penerimaan To Excel</title>
        <style>
            .doc_header td {font-size: 12px; font-family: arial; text-align: left}
            .doc_data th {font-size: 12px; font-family: arial}
            .doc_data td {font-size: 12px; font-family: arial}
            .doc_data th, .doc_data td {
                border-collapse: collapse;
                border-color: black;
                border-style: solid;
                border-width: thin
            }
        </style>
    </head>
    <body>
        
        <table>
            <tr><td style="color: white">___</td></tr>
            <tr>
                <td></td>
                <td>
                    <table class="doc_header">
                        
                        <tr><td colspan="9">Nomor Dokumen : <%= ret.getRetCode()%></td></tr>
                        <tr><td colspan="9">Lokasi : <%= returnLocation.getName()%></td></tr>
                        <tr><td colspan="9">Tanggal : <%= Formater.formatDate(ret.getReturnDate(), "yyyy-MM-dd HH:mm:ss") %></td></tr>
                        <tr><td colspan="9">Supplier : <%= supplier.getCompName()%> (<%= supplier.getContactCode() %>)</td></tr>
                        <tr><td colspan="9">Alasan : <%= PstMatReturn.strReturnReasonList[SESS_LANGUAGE][ret.getReturnReason()]%></td></tr>
                        <tr><td colspan="9">Keterangan : <%= ret.getRemark()%></td></tr>
                        <tr><td colspan="9">Status Dokumen : <%= I_DocStatus.fieldDocumentStatus[ret.getReturnStatus()]%></td></tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <br>
                    <table class="doc_data">
                        <tr>
                            <th>No.</th>
                            <th>SKU</th>
                            <th>Nama Barang</th>
                            <th>Barcode</th>
                            <th>Color</th>
                            <th>Size</th>
                            <th>Qty</th>
                            <th>Price</th>
                            <th>Total Price</th>
                        </tr>

                        <% if (listMatReturnItem.isEmpty()) {%>
                        <tr><td colspan="9" style="text-align: center">Tidak ada data return</td></tr>
                        <% } %>

                        <%
                            int i = 0;
                            double totalQty = 0;
                            double grandTotalPrice = 0;

                            for (Vector v : listMatReturnItem) {
                                i++;
                                MatReturnItem retItem = (MatReturnItem) v.get(0);
                                Material mat = (Material) v.get(1);

                                Material newmat = new Material();
                                Color color = new Color();
                                MasterType masterTypeSize = new MasterType();
                                try {
                                    newmat = PstMaterial.fetchExc(retItem.getMaterialId());
                                    color = PstColor.fetchExc(newmat.getPosColor());
                                    long oidMappingSize = PstMaterialMappingType.getSelectedTypeId(1, newmat.getOID());
                                    masterTypeSize = PstMasterType.fetchExc(oidMappingSize);
                                } catch (Exception e) {

                                }

                                totalQty += retItem.getQty();
                                grandTotalPrice += retItem.getTotal();

                        %>

                        <tr>
                            <td><%= i %>.&nbsp;</td>
                            <td><%= mat.getSku() %></td>
                            <td><%= mat.getName()%></td>
                            <td><%= mat.getBarCode()%>&nbsp;</td>
                            <td><%= color.getColorName()%></td>
                            <td><%= masterTypeSize.getMasterName()%></td>
                            <td style="text-align: center"><%= retItem.getQty() %></td>
                            <td style="text-align: right"><%= FRMHandler.userFormatStringDecimal(retItem.getHargaJual()) %></td>
                            <td style="text-align: right"><%= FRMHandler.userFormatStringDecimal(retItem.getTotal()) %></td>
                        </tr>

                        <%
                            }
                        %>

                        <% if (!listMatReturnItem.isEmpty()) {%>
                        <tr>
                            <th colspan="6" style="text-align: right">TOTAL</th>
                            <th><%= totalQty %></th>
                            <th></th>
                            <th style="text-align: right"><%= FRMHandler.userFormatStringDecimal(grandTotalPrice) %></th>
                        </tr>
                        <% } %>

                    </table>
                </td>
            </tr>
			<tr>
                <td></td>
                <td>
                    <table style="border: none">
                        <tr align="left" valign="top"> 
							<td height="40" valign="middle" colspan="9"></td>
						</tr>
						<tr>
							<%if (signRet1.equals(signRet1) && !signRet1.equals("Not initialized")) {%>  
							<td width="34%" align="center" colspan="2" nowrap><%=signRet1%></td>
							<%} else {%>  
							<td width="34%" align="center"colspan="2" nowrap>Pengirim,</td>
							<%}%>
							<%if (signRet2.equals(signRet2) && !signRet2.equals("Not initialized")) {%>  
							<td width="34%" align="center" colspan="3" nowrap><%=signRet2%></td>
							<%} else {%> 
							<td align="center" valign="bottom" colspan="3" width="33%">Mengetahui,</td>
							<%}%>
							<%if (signRet3.equals(signRet3) && !signRet3.equals("Not initialized")) {%>  
							<td width="34%" align="center" colspan="4" nowrap><%=signRet3%></td>
							<%} else {%> 
							<td width="33%" colspan="4" align="center">Penerima,</td> 
							<%}%>
						</tr>
						<tr align="left" valign="top"> 
							<td height="75" valign="middle" colspan="9"></td>
						</tr>
						<tr>
							<%if (nameSignRet1.equals(nameSignRet1) && !nameSignRet1.equals("Not initialized") && !nameSignRet1.equals("0")) {%>
								<td width="34%" align="center" colspan="2" nowrap>( <u><%=nameSignRet1%></u> )</td>
							<%} else {%> 
							<td width="34%" align="center" colspan="2" nowrap>
								( <u><%=appUser.getFullName()%></u> )
							</td>
							<%} if (nameSignRet2.equals(nameSignRet2) && !nameSignRet2.equals("Not initialized") && !nameSignRet2.equals("0")) {%>
								<td width="34%" align="center" colspan="3" nowrap>( <u><%=nameSignRet2%></u> )</td>
							<%} else {%> 
							<td align="center" valign="bottom" colspan="3" width="33%">
								( <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u> )
							</td>
							<%} if (nameSignRet3.equals(nameSignRet3) && !nameSignRet3.equals("Not initialized") && !nameSignRet2.equals("0")) {%>
								<td width="34%" align="center" colspan="4" nowrap>( <u><%=nameSignRet3%></u> )</td>
							<%} else {%>  
							<td width="33%" colspan="4" align="center">
								( <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u> )
							</td> 
							<% } %>
						</tr>
                    </table>
                </td>
            </tr>
        </table>
        
        
        
    </body>
</html>
