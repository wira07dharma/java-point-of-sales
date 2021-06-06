<%--
    Document   : home
    Created on : Jul 29, 2013, 2:16:39 PM
    Author     : user
--%>
<%@page import="com.dimata.aplikasi.entity.uploadpicture.PictureBackground"%>
<%@page import="com.dimata.aplikasi.entity.uploadpicture.PstPictureBackground"%>
<%@page import="com.dimata.aplikasi.form.mastertemplate.CtrlTempDinamis"%>
<%@page import="com.dimata.aplikasi.entity.mastertemplate.PstTempDinamis"%>
<%@page import="com.dimata.aplikasi.entity.mastertemplate.TempDinamis"%>
<%@page import="java.util.ResourceBundle.Control"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.aplikasi.form.mastertemplate.FrmTempDinamis"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../main/javainit.jsp" %>

<%
    int iCommand = FRMQueryString.requestCommand(request);
    //long oidTempDinamis = FRMQueryString.requestLong(request, "hidden_temp_dinamis_id");
    int iErrCode = FRMMessage.NONE;

    CtrlTempDinamis ctrlTempDinamis = new CtrlTempDinamis(request);
    FrmTempDinamis frmTempDinamis = ctrlTempDinamis.getForm();

    iErrCode = ctrlTempDinamis.action(iCommand, tempDinamis != null ? tempDinamis.getOID() : 0);

%>
<!DOCTYPE html>
<html>
    <head>
       
        <title>Change Template</title>
        <script language="javascript">
            
           if(<%=iErrCode==0 && iCommand==Command.SAVE%>){
               //location.reload(true);
               //window.location = window.location.href;
               window.location.href = window.location.pathname;
           }
            function disablefield() {
                if (document.getElementById('version_I').checked == 0) {
                    document.getElementById('colorbag').disabled = '';
                    document.getElementById('header1').disabled = '';
                    document.getElementById('header2').disabled = '';
                    document.getElementById('garis1').disabled = '';
                    document.getElementById('garis2').disabled = '';
                    document.getElementById('colorhead').disabled = '';
                    document.getElementById('colorcont').disabled = '';
                    document.getElementById('colorbgmenu').disabled = '';
                    document.getElementById('colorfont').disabled = '';
                    document.getElementById('colorhover').disabled = '';
                    document.getElementById('nav1').disabled = '';
                    document.getElementById('nav2').disabled = '';
                    document.getElementById('nav3').disabled = '';
                    document.getElementById('nav5').disabled = '';
                    document.getElementById('nav6').disabled = '';
                    document.getElementById('nav7').disabled = '';
                    document.getElementById('uploadpic').style.display="";
                } else {
                    document.getElementById('colorbag').disabled = 'disabled';
                    document.getElementById('colorhead').disabled = 'disabled';
                     document.getElementById('header1').disabled = 'disabled';
                    document.getElementById('header2').disabled = 'disabled';
                    document.getElementById('garis1').disabled = 'disabled';
                    document.getElementById('garis2').disabled = 'disabled';
                    document.getElementById('colorcont').disabled = 'disabled';
                    document.getElementById('colorbgmenu').disabled = 'disabled';
                    document.getElementById('colorfont').disabled = 'disabled';
                    document.getElementById('colorhover').disabled = 'disabled';
                    document.getElementById('nav1').disabled = 'disabled';
                    document.getElementById('nav2').disabled = 'disabled';
                    document.getElementById('nav3').disabled = 'disabled';
                    document.getElementById('nav5').disabled = 'disabled';
                    document.getElementById('nav6').disabled = 'disabled';
                    document.getElementById('nav7').disabled = 'disabled';
                   
                    document.getElementById('uploadpic').style.display='none';

                }
            }
            function cmdSave() {


                document.<%=FrmTempDinamis.FRM_TEMP_DINAMIS%>.command.value = "<%=Command.SAVE%>";

                document.<%=FrmTempDinamis.FRM_TEMP_DINAMIS%>.action = "chage_template.jsp";

                document.<%=FrmTempDinamis.FRM_TEMP_DINAMIS%>.submit();

            }
            function cmdEdit(oid, oidPicture) {


                window.open("<%=approot%>/styletemplate/proses_upload_picture.jsp?loginuser=" + oid + "&" + "hidden_picture_oid=" + oidPicture + "&" + "command=" + "<%=Command.POST%>",
                        "upload_Image", "height=550,width=600, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");

            }


            function uploadPicture(oid) {

                window.open("<%=approot%>/styletemplate/proses_upload_picture.jsp?loginuser=" + oid + "&" + "command=" + "<%=Command.POST%>",
                        "upload_Image", "height=550,width=600, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");

            }

            function autoRefresh(refreshPeriod) {
                setTimeout(window.location.reload(), refreshPeriod);
            }
        </script>
     
        <link href="<%=approot%>/stylesheets/colorpicker.css" type="text/css" rel="stylesheet">
        <script type="text/javascript" src="<%=approot%>/stylescript/jscolor.js"></script>
        <!-- Style tooltip -->
        <link rel="stylesheet" href="<%=approot%>/styletooltip/jquery.cluetip.css" type="text/css" />
        <script>!window.jQuery && document.write(unescape('%3Cscript src="<%=approot%>/styletooltip/jquery-1.7.1.min.js"%3E%3C/script%3E'));</script>
        <link href="<%=approot%>/stylesheets/general_template_style.css" type="text/css" rel="stylesheet">
    </head>
    <body bgcolor="<%=bgColorBody%>">
        <!-- <table width="100%">
         <tr>
         <td bgcolor="#0066FF">
             <table width="100%">
                     <tr>
                     <td>-->
        <div id="BaseContainer">
            <div id="header">
                <%@include file="template_header.jsp" %>
            </div>
            <div class="titlebig">News &amp; Event</div>
            <div id="MiddleMenu">
                <div id="ContentBase">
                    <table width="100%">
                        <tr>
                            <td bgcolor="#0066FF">
                                <form name="<%=FrmTempDinamis.FRM_TEMP_DINAMIS%>" method="post" action="">
                                    <input type="hidden" name="command" value="">
                                    <table  width="100%" height="100%" bgcolor="<%=bgColorContent%>" style="text-align: left; text-transform: capitalize;">
                                        <tr>
                                            <td width="10%" nowrap="nowrap">template</td>
                                            <td width="1%" nowrap="nowrap"> : </td>
                                            <td width="83%">
                                                <a id="sticky" rel="<%=approot%>/styletooltip/menu_icon.jsp?nilai=0" title="Information"><input type=radio name='<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_TEMP_VERSION_NO]%>' value='0'  id='version_I' onChange='disablefield();' ><%=FrmTempDinamis.JENIS_TEMPLATE_KEY[FrmTempDinamis.TEMPLATE_I]%></a>
                                                <a id="sticky1" rel="<%=approot%>/styletooltip/menu_icon.jsp?nilai=1" title="Information"><input type=radio name='<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_TEMP_VERSION_NO]%>' value='1'  id='version_II' onChange='disablefield();' checked="checked"><%=FrmTempDinamis.JENIS_TEMPLATE_KEY[FrmTempDinamis.TEMPLATE_II]%></a>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td nowrap="nowrap">Warna Font</td>
                                            <td> : </td>
                                            <td><a id="sticky6" rel="<%=approot%>/styletooltip/menu_icon.jsp?nilai=6" title="Information"><input id='colorfont' class="color" name="<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_FONT_MENU]%>" value="<%=tempDinamis.getFontMenu() == null ? "FFFFFF" : tempDinamis.getFontMenu()%>"></a></td>
                                        </tr>
                                        <tr>
                                            <td nowrap="nowrap">Header 1</td>
                                            <td> : </td>
                                            <td><a id="sticky6" rel="<%=approot%>/styletooltip/menu_icon.jsp?nilai=6" title="Information"><input id='header1' class="color" name="<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_HEAD_COLOR]%>" value="<%=tempDinamis.getHeaderColor() == null ? "42b6e8" : tempDinamis.getHeaderColor()%>"></a></td>
                                        </tr>
                                         <tr>
                                            <td nowrap="nowrap">Header 2</td>
                                            <td> : </td>
                                            <td><a id="sticky6" rel="<%=approot%>/styletooltip/menu_icon.jsp?nilai=6" title="Information"><input id='header2' class="color" name="<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_TEMP_COLOR_HEADER2]%>" value="<%=tempDinamis.getTempColorHeader() == null ? "42b6e8" : tempDinamis.getTempColorHeader()%>"></a>*</td>
                                        </tr>
                                         <tr>
                                            <td nowrap="nowrap">Garis Header 1</td>
                                            <td> : </td>
                                            <td><a id="sticky6" rel="<%=approot%>/styletooltip/menu_icon.jsp?nilai=6" title="Information"><input id='garis1' class="color" name="<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_GARIS_HAEADER1]%>" value="<%=tempDinamis.getGarisHeader1() == null ? "FFFFFF" : tempDinamis.getGarisHeader1()%>"></a></td>
                                        </tr>
                                        <tr>
                                            <td nowrap="nowrap">Garis Header 2</td>
                                            <td> : </td>
                                            <td><a id="sticky6" rel="<%=approot%>/styletooltip/menu_icon.jsp?nilai=6" title="Information"><input id='garis2' class="color" name="<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_GARIS_HEADER2]%>" value="<%=tempDinamis.getGarisHeader2() == null ? "FFFFFF" : tempDinamis.getGarisHeader2()%>"></a></td>
                                        </tr>
                                         <tr>
                                            <td nowrap="nowrap">Color Background Menu</td>
                                            <td> : </td>
                                            <td><a id="sticky5" rel="<%=approot%>/styletooltip/menu_icon.jsp?nilai=5" title="Information"><input id='colorbgmenu' class="color" name="<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_BG_MENU]%>" value="<%=tempDinamis.getBgMenu() == null ? "42b6e8" : tempDinamis.getBgMenu()%>"></a>*</td>
                                        </tr>
                                        
                                        <tr>
                                            <td nowrap="nowrap">Color Hover Menu</td>
                                            <td> : </td>
                                            <td><a id="sticky7" rel="<%=approot%>/styletooltip/menu_icon.jsp?nilai=7" title="Information"><input id='colorhover' class="color" name="<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_HOVER_MENU]%>" value="<%=tempDinamis.getHoverMenu() == null ? "c1e4ec" : tempDinamis.getHoverMenu()%>"></a></td>
                                        </tr>
                                        <tr>
                                            <td nowrap="nowrap">color Background</td>
                                            <td> : </td>
                                            <td><a id="sticky2" rel="<%=approot%>/styletooltip/menu_icon.jsp?nilai=2" title="Information"><input id='colorbag' class="color" name="<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_TEMP_COLOR]%>" value="<%=tempDinamis.getTempColor() == null ? "FFFFFF" : tempDinamis.getTempColor()%>"></a></td> 
                                        </tr>
                                        
                                        <tr>
                                            <td nowrap="nowrap">color Content</td>
                                            <td> : </td>
                                            <td><a id="sticky4" rel="<%=approot%>/styletooltip/menu_icon.jsp?nilai=4" title="Information"><input id='colorcont' class="color" name="<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_CONT_COLOR]%>" value="<%=tempDinamis.getContentColor() == null ? "FFFFFF" : tempDinamis.getContentColor()%>"></a></td>
                                        </tr>
                                        <!-- Navigation Color Here -->
                                       
                                        <!-- End Navigation Color -->
                                        <tr>
                                            <td nowrap="nowrap">Garis Content</td>
                                            <td> : </td>
                                            <td><a id="sticky4" rel="<%=approot%>/styletooltip/menu_icon.jsp?nilai=4" title="Information"><input id='colorcont' class="color" name="<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_GARIS_CONTENT]%>" value="<%=tempDinamis.getGarisContent() == null ? "FFFFFF" : tempDinamis.getGarisContent()%>"></a>*</td>
                                        </tr>
                                        <tr>
                                            <td nowrap="nowrap">Footer Garis</td>
                                            <td> : </td>
                                            <td><a id="sticky4" rel="<%=approot%>/styletooltip/menu_icon.jsp?nilai=4" title="Information"><input id='colorcont' class="color" name="<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_FOOTER_GARIS]%>" value="<%=tempDinamis.getFooterGaris() == null ? "FFFFFF" : tempDinamis.getFooterGaris()%>"></a></td>
                                        </tr>
                                        <tr>
                                            <td nowrap="nowrap">Footer Background</td>
                                            <td> : </td>
                                            <td><a id="sticky4" rel="<%=approot%>/styletooltip/menu_icon.jsp?nilai=4" title="Information"><input id='colorcont' class="color" name="<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_FOOTER_BACKGROUND]%>" value="<%=tempDinamis.getFooterBackground() == null ? "42b6e8" : tempDinamis.getFooterBackground()%>"></a>*</td>
                                        </tr>
                                        <tr>
                                            <td nowrap="nowrap">Table Header</td>
                                            <td> : </td>
                                            <td><a id="sticky4" rel="<%=approot%>/styletooltip/menu_icon.jsp?nilai=4" title="Information"><input id='colorcont' class="color" name="<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_TABLE_HEADER]%>" value="<%=tempDinamis.getTableHeader() == null ? "42b6e8" : tempDinamis.getTableHeader()%>"></a>*</td>
                                        </tr>
                                        <tr>
                                            <td nowrap="nowrap">Table Cell</td>
                                            <td> : </td>
                                            <td><a id="sticky4" rel="<%=approot%>/styletooltip/menu_icon.jsp?nilai=4" title="Information"><input id='colorcont' class="color" name="<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_TABLE_CELL]%>" value="<%=tempDinamis.getTableCell() == null ? "42b6e8" : tempDinamis.getTableCell()%>"></a>*</td>
                                        </tr>
                                        <tr>
                                            <td nowrap="nowrap" valign="top">navigation</td>
                                            <td valign="top"> : </td>
                                            <td>
                                                <table width="100%">
                                                    <%
                                                        String chekedMenu1 = "";
                                                        String chekedMenu2 = "";
                                                        String chekedMenu3 = "";
                                                        String chekedMenu4 = "";
                                                        String chekedMenu5 = "";
                                                        String chekedMenu6 = "";

                                                        if (tempDinamis.getNavigation() == null || tempDinamis.getNavigation().equalsIgnoreCase("menu_i")) {
                                                            chekedMenu1 = "checked";
                                                        /*} else if (tempDinamis.getNavigation().equalsIgnoreCase("menu_ii")) {
                                                            chekedMenu2 = "checked";
                                                        } else if (tempDinamis.getNavigation().equalsIgnoreCase("menu_iii")) {*/
                                                        }else{
                                                            chekedMenu3 = "checked";
                                                        /*} else if (tempDinamis.getNavigation().equalsIgnoreCase("menu_v")) {
                                                            chekedMenu4 = "checked";*/
                                                        /*} else if (tempDinamis.getNavigation().equalsIgnoreCase("menu_vi")) {
                                                            chekedMenu5 = "checked";
                                                        } else {
                                                            chekedMenu6 = "checked";*/
                                                        }
                                                    %>
                                                    <tr>
                                                        <td width="3%" nowrap><input id='nav1'type="radio" name="<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_TEMP_NAVIGATION]%>" value="menu_i" <%=chekedMenu1%> /></td>
                                                        <td width="97%" nowrap><a id="sticky8" title="Information" rel="<%=approot%>/styletooltip/menu_icon.jsp?nilai=8"><img src="<%=approot%>/imgstyle/menu_1.PNG" width="60%"></a></td>
                                                        <!--<td width="3%" nowrap><input id='nav2' type="radio" name="<%//=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_TEMP_NAVIGATION]%>" value="menu_ii" <%//=chekedMenu2%>/></td>
                                                        <td width="47%" nowrap><img src="../imgstyle/menu_2.png" width="100%"></td>-->
                                                    </tr>
                                                    <tr>
                                                        <td width="3%" nowrap><input id='nav3' type="radio" name="<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_TEMP_NAVIGATION]%>" value="menu_iii" <%=chekedMenu3%>/></td>
                                                        <td width="97%" nowrap><a  id="sticky9" title="Information" rel="<%=approot%>/styletooltip/menu_icon.jsp?nilai=9"><img src="<%=approot%>/imgstyle/menu_3.png" width="60%"></a></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td nowrap="nowrap">Background</td>
                                            <td> : </td>
                                            <td  id="uploadpic" colspan="3" class="listedittitle" height="14" valign="middle"> 
                                                <table >
                                                    <%
                                                        long oidUser = 0;
                                                        Vector listpicture = PstPictureBackground.list(0, 0, "", PstPictureBackground.fieldNames[PstPictureBackground.FLD_NAMA_PICTURE] + " ASC ");
                                                        String sPicture = "";
                                                        if (listpicture != null && listpicture.size() > 0) {
                                                            for (int idx = 0; idx < listpicture.size(); idx++) {
                                                                pictureBackground = (PictureBackground) listpicture.get(idx);
                                                                sPicture = sPicture + "<a href=javascript:cmdEdit(\"" + oidUser + "\"" + ",\"" + pictureBackground.getOID() + "\")>" + pictureBackground.getNamaPicture() + "</a>" + ",";
                                                            }

                                                            sPicture = sPicture.substring(0, sPicture.length() - 1);
                                                        }
                                                    %>
                                                    <tr>
                                                        <td>
                                                            <a href="javascript:uploadPicture('<%=oidUser%>')" title="Information" class="command" id="sticky10" rel="<%=approot%>/styletooltip/menu_icon.jsp?nilai=10">Upload Picture</a> :<%=sPicture%>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <%
                                            String language1 = "";
                                            String language2 = "";

                                            if (tempDinamis.getLanguage() == 0 || tempDinamis.getLanguage() != 1) {
                                                language1 = "checked";
                                            } else {
                                                language2 = "checked";
                                            }
                                        %>
                                        <tr>
                                            <td>Language</td>
                                            <td> : </td>
                                            <td>
                                                <table width="5%">
                                                    <tr>
                                                        <td width="1%" nowrap><input id='nav2'type="radio" name="<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_LANGUAGE]%>" value="French" <%=language1%> /></td>
                                                        <td width="1%" nowrap><a class="google_translate" href="http://www.blogger.com/post-create.g?blogID=7490537618574143540#" rel="nofollow" target="_blank" title="French"><img align="absbottom" alt="French" border="0" height="32" src="<%=approot%>/imgstyle/French.png" style="cursor: pointer; margin-right: 10px;" title="French" width="24" /></a></td>
                                                        <td width="1%" nowrap><input id='nav3' type="radio" name="<%=FrmTempDinamis.fieldNames[FrmTempDinamis.FRM_FLD_LANGUAGE]%>" value="German" <%=language2%> /></td>
                                                        <td width="1%" nowrap><a class="google_translate" href="http://www.blogger.com/post-create.g?blogID=7490537618574143540#" rel="nofollow" target="_blank" title="German"><img align="absbottom" alt="German" border="0" height="32" src="<%=approot%>/imgstyle/German.png" style="cursor: pointer; margin-right: 10px;" title="German" width="24" /></a></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>sortcurt</td>
                                            <td> : </td>
                                            <td>  </td>
                                        </tr>
                                        <tr>
                                            <td nowrap="nowrap"></td>
                                            <td nowrap="nowrap"></td> 
                                            <td nowrap="nowrap"><a  href="Javascript:cmdSave()"  class="savestyle">Save</a></td>
                                        </tr>
                                    </table>
                                </form>
                            </td> 
                        </tr> 
                    </table>
                </div>
            </div>
                                                    
        </div>
                                                    
        <!--         </td>
             </tr>
         </table>
     </td>
     </tr>
     </table>-->
        <script src="<%=approot%>/styletooltip/jquery.cluetip.js"></script>
  	<script src="<%=approot%>/styletooltip/demo.js"></script>
    </body>
</html>