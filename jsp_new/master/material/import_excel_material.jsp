<%-- 
    Document   : import_excel_material
    Created on : Oct 23, 2018, 11:51:10 AM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.Ksg"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstKsg"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.common.entity.contact.PstContactClass"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<%@ page import="com.dimata.gui.jsp.*"%>
<!DOCTYPE html>
<%!
    public String drawCheck() {
        ControlCheckBox chkBx=new ControlCheckBox();
        chkBx.setWidth(4);
        chkBx.setCellSpace("0");
        chkBx.setCellStyle("");
        chkBx.setTableAlign("center");

        Vector checkName = new Vector(1,1);
        Vector checkValue = new Vector(1,1);
        Vector checkCaption = new Vector(1,1);
        Vector check = new Vector(1,1);

        Vector sttRsv = PstLocation.list(0,0, PstLocation.fieldNames[PstLocation.FLD_TYPE]+"="+PstLocation.TYPE_LOCATION_STORE, null);
        Vector vname = new Vector(1,1);
        for(int i = 0;i < sttRsv.size();i++){
            Location temp = (Location)sttRsv.get(i);
            //int val = Integer.parseInt((String)temp.get(1));
            checkValue.add(""+temp.getOID());
            checkCaption.add(temp.getName());
            vname.add(""+temp.getOID()+"_"+i);
        }

        checkName = vname; //checkValue;
        return chkBx.draw(checkName,checkValue,checkCaption,check);    
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Import Data</title>
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" --> 
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <script type="text/javascript" src="../../styles/jquery-1.4.2.min.js"></script>
        <script src="../../styles/jquery.autocomplete.js"></script>
        <link rel="stylesheet" type="text/css" href="../../styles/style.css" />
        <%@ include file = "../../styles/lte_head.jsp" %>
        <style>
            body {font-family: Arial;}

            /* Style the tab */
            .tab {
                overflow: hidden;
                border: 1px solid #ccc;
                background-color: #f1f1f1;
            }

            /* Style the buttons inside the tab */
            .tab button {
                background-color: inherit;
                float: left;
                border: none;
                outline: none;
                cursor: pointer;
                padding: 14px 16px;
                transition: 0.3s;
                font-size: 17px;
            }

            /* Change background color of buttons on hover */
            .tab button:hover {
                background-color: #ddd;
            }

            /* Create an active/current tablink class */
            .tab button.active {
                background-color: #ccc;
            }

            /* Style the tab content */
            .tabcontent {
                display: none;
                padding: 6px 12px;
                border: 1px solid #ccc;
                border-top: none;
            }
        </style>
        <script language="JavaScript">

            function cmdAdd() {
                document.form1.submit();
            }
        </script>
    </head>
    <body class="flexbox" style="background-color: rgb(245, 247, 255) !important;">
        <section class="content-header">
            <h1>
              Register Item
              <small></small>
            </h1>
            <ol class="breadcrumb">
              <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
              <li class="active">Register Item</li>
            </ol>
        </section>
        <div class="content">
            <div class="tab">
                <button class="tablinks" onclick="openTab(event, 'template1')" id="defaultOpen">Template 1</button>
                <button class="tablinks" onclick="openTab(event, 'template2')">Template 2</button>
            </div>
            <div id="template1" class="tabcontent">    
                <form name="form1" method="post" action="import_excel_material_proccess.jsp" enctype="multipart/form-data">
                    <input type="hidden" name="template" value="1">
                    <table width="100%" border="0" cellspacing="2" cellpadding="2">
                        <tr align="left">
                            <td height="21" valign="" width="13%">Location</td>
                            <td height="21" width="2%" valign="">:</td>
                            <td height="21" width="85%" valign="">
                                <%
                                    ControlCheckBox controlCheckBox = new ControlCheckBox();
                                     Vector sttRsv = PstLocation.list(0,0, PstLocation.fieldNames[PstLocation.FLD_TYPE]+"="+PstLocation.TYPE_LOCATION_STORE, null);
                                     Vector locationSellVal = new Vector(1,1);
                                     Vector locationSellKey = new Vector(1,1);
                                     for(int i=0;i<sttRsv.size();i++) {
                                            Location temp = (Location)sttRsv.get(i);
                                            locationSellVal.add(""+temp.getOID()+"");
                                            locationSellKey.add(""+temp.getName()+"");
                                    }
                                  controlCheckBox.setWidth(5);
                                  out.println(controlCheckBox.draw("location",locationSellVal,locationSellKey,new Vector()));
                              %>
                            </td>
                        </tr>
                        <tr align="left">
                            <td height="21" valign="" width="13%">Supplier</td>
                            <td height="21" width="2%" valign="">:</td>
                            <td height="21" width="85%" valign="">
                                <% 
                                    Vector obj_supplier = new Vector(1,1);
                                    Vector val_supplier = new Vector(1,1);
                                    Vector key_supplier = new Vector(1,1);
                                    String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                                            " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+" AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
                                            " != "+PstContactList.DELETE;
                                    Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
                                    if(vt_supp!=null && vt_supp.size()>0){
                                        int maxSupp = vt_supp.size();
                                        for(int d=0; d<maxSupp; d++){
                                            ContactList cnt = (ContactList)vt_supp.get(d);
                                            String cntName = cnt.getCompName();
                                            val_supplier.add(String.valueOf(cnt.getOID()));
                                            key_supplier.add(cntName);
                                        }
                                    }
                                    %>
                                    <%=ControlCombo.draw("supplier","formElemen",null,null,val_supplier,key_supplier," tabindex=\"3\" onkeydown=\"javascript:fnTrapKD(event)\"")%> 
                            </td>
                        </tr>
						<tr align="left">
                            <td height="21" valign="" width="13%">Konsinyasi</td>
                            <td height="21" width="2%" valign="">:</td>
                            <td height="21" width="85%" valign="">
                                <% 
                                    Vector vectValue = new Vector(1,1);
                                    Vector vectKey = new Vector(1,1);
                                    
                                    vectKey.add(PstMaterial.strTypeCatalogConsigment[SESS_LANGUAGE][PstMaterial.CATALOG_TYPE_CONSIGMENT]);
                                    vectValue.add(""+PstMaterial.CATALOG_TYPE_CONSIGMENT);
									
									vectKey.add(PstMaterial.strTypeCatalogConsigment[SESS_LANGUAGE][PstMaterial.CATALOG_TYPE_OTHER]);
                                    vectValue.add(""+PstMaterial.CATALOG_TYPE_OTHER);
                                    %>
                                    <%=ControlCombo.draw("consignment","formElemen",null,null,vectValue,vectKey," tabindex=\"3\" onkeydown=\"javascript:fnTrapKD(event)\"")%> 
                            </td>
                        </tr>
                    </table>
                    <input type="file" name="file">
                    <input type="submit" name="Submit" value="Submit">
                </form>
                <div>&nbsp;</div>
                <div><a href="javascript:cmdDownload()">Download file example</a></div>
            </div>
            <div id="template2" class="tabcontent">    
                <form name="form2" method="post" action="import_excel_material_proccess.jsp" enctype="multipart/form-data">
                    <input type="hidden" name="template" value="2">
                    <table width="100%" border="0" cellspacing="2" cellpadding="2">
                        <tr align="left">
                            <td height="21" valign="" width="13%">Location</td>
                            <td height="21" width="2%" valign="">:</td>
                            <td height="21" width="85%" valign="">
                                <%
                                  out.println(controlCheckBox.draw("location2",locationSellVal,locationSellKey,new Vector()));
                              %>
                            </td>
                        </tr>
                        <tr align="left">
                            <td height="21" valign="" width="13%">Supplier</td>
                            <td height="21" width="2%" valign="">:</td>
                            <td height="21" width="85%" valign="">
                                    <%=ControlCombo.draw("supplier2","formElemen",null,null,val_supplier,key_supplier," tabindex=\"3\" onkeydown=\"javascript:fnTrapKD(event)\"")%> 
                            </td>
                        </tr>
						<tr align="left">
                            <td height="21" valign="" width="13%">Konsinyasi</td>
                            <td height="21" width="2%" valign="">:</td>
                            <td height="21" width="85%" valign="">
                                    <%=ControlCombo.draw("consignment2","formElemen",null,null,vectValue,vectKey," tabindex=\"3\" onkeydown=\"javascript:fnTrapKD(event)\"")%> 
                            </td>
                        </tr>
                    </table>
                    <input type="file" name="file">
                    <input type="submit" name="Submit" value="Submit">
                </form>
                <div>&nbsp;</div>
                <div><a href="javascript:cmdDownload2()">Download file example</a></div>
            </div>
                            <br>
            <a href="<%=approot%>/master/material/material.jsp" class="btn btn-default text-black">Kembali ke pencarian</a>
        </div>
        <script>
            document.getElementById("defaultOpen").click();
            function openTab(evt, cityName) {
                var i, tabcontent, tablinks;
                tabcontent = document.getElementsByClassName("tabcontent");
                for (i = 0; i < tabcontent.length; i++) {
                    tabcontent[i].style.display = "none";
                }
                tablinks = document.getElementsByClassName("tablinks");
                for (i = 0; i < tablinks.length; i++) {
                    tablinks[i].className = tablinks[i].className.replace(" active", "");
                }
                document.getElementById(cityName).style.display = "block";
                evt.currentTarget.className += " active";
            }
        </script>
    </body>
</html>
