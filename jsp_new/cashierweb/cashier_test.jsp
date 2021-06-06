<%-- 
    Document   : cashier_test
    Created on : 05 Jun 13, 16:32:44
    Author     : Wiweka
--%>

<%@ page language="java" %>

<!--%!
/* this constant used to list text of listHeader */
public static final String textMaterialHeader[][] = {
    {"Tipe Nota","Nomor","Tipe Tamu","CIN","Nama","Cover Quest Name","Cover Number"},
    {"Nota Type","Number","Quest Type","CIN","Nama","Cover Quest Name","Cover Number"}
};
%>
<!-- JSP Block -->

<!-- End of JSP Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata ProChain - </title>
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <!-- #BeginEditable "styles" -->
        <link rel="stylesheet" href="../styles/main.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../styles/tab.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "headerscript" -->
        <SCRIPT language=JavaScript>
            function hideObjectForMarketing(){
            }

            function hideObjectForWarehouse(){
            }

            function hideObjectForProduction(){
            }

            function hideObjectForPurchasing(){
            }

            function hideObjectForAccounting(){
            }

            function hideObjectForHRD(){
            }

            function hideObjectForGallery(){
            }

            function hideObjectForMasterData(){
            }

        </SCRIPT>
        <style type="text/css">
            <!--
            .style1 {
                color: #009900;
                font-weight: bold;
            }
            .style3 {font-size: 24px}
            -->
        </style>
        <script type="text/javascript">
            function disablefield(){
                if (document.getElementById('tipe').value==0) {
                    document.getElementById('cin').disabled='disabled';
                    document.getElementById('nama_tamu').disabled='disabled';
                    document.getElementById('cover_name').disabled='disabled';
                    document.getElementById('cover_number').disabled='disabled';

                }else{
                    document.getElementById('cin').disabled='';
                    document.getElementById('nama_tamu').disabled='';
                    document.getElementById('cover_name').disabled='';
                    document.getElementById('cover_number').disabled='';}
            }
        </script>

        <script type="text/javascript" src=""></script>
        <script>
            $(document).ready(function(){
                $('input').keyup(function(e){
                    if(e.which==39)
                        $(this).closest('td').next().find('input').focus();
                    else if(e.which==37)
                        $(this).closest('td').prev().find('input').focus();
                    else if(e.which==40)
                        $(this).closest('tr').next().find('td:eq('+$(this).closest('td').index()+')').find('input').focus();
                    else if(e.which==38)
                        $(this).closest('tr').prev().find('td:eq('+$(this).closest('td').index()+')').find('input').focus();
                    });
            });
        </script>
        
        <script type="text/javascript" src="jquery-1.6.1.min.js"></script>
        <script type="text/javascript">

           $(document).ready(function(){
                $('#nama_tamu').keyup(function(e){
                   if(e.which==13)

                //open popup
                    $("#overlay_form").fadeIn(1000);
                    positionPopup();
              
             });

                //close popup
                $("#close").focus(function(){
                    $("#overlay_form").fadeOut(500);
                });
            });

            //position the popup at the center of the page
            function positionPopup(){
                if(!$("#overlay_form").is(':visible')){
                    return;
                }
                $("#overlay_form").css({
                    left: ($(window).width() - $('#overlay_form').width()) / 2,
                    top: ($(window).width() - $('#overlay_form').width()) / 7,
                    position:'absolute'
                });
            }

            //maintain the popup at center of the page when browser resized
            $(window).bind('resize',positionPopup);

        </script>
        <script>

var c=document.getElementById("myCanvas");
var ctx=c.getContext("2d");
ctx.shadowBlur=10;
ctx.shadowColor="black";

</script>
        <style>
            #overlay_form{
                position:absolute;
                border: 5px solid gray;
                padding: 10px;
                background: white;
                width: auto;
                height: auto;
            }
            #pop{
                display:block;
                border: 1px solid gray;
                width:auto;
                text-align: center;
                padding: 6px;
                border-radius: 5px;
                text-decoration: none;
                margin: 0 auto;
            }
        </style>
        <!-- #EndEditable -->
    </head>

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            
            <tr>
                <td valign="top" align="left">
                   
                    <table align="center" width="80%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td align="left" height="20" class="mainheader" ><!-- #BeginEditable "contenttitle" --><!-- #EndEditable --></td>
                        </tr>
                        <tr>
                            <td ><!-- #BeginEditable "content" -->

                                <fieldset>
                                <table width="75%" border="0" cellspacing="0" cellpadding="0" align="center">
                                    <tr>
                                        <td>
                                            <table width="100%" border="0" cellspacing="1" cellpadding="1" >
                                                <tr>
                                                    <td width="10%" rowspan="4" bgcolor="#FFFFCC">&nbsp;</td>
                                                    <td colspan="2">b</td>
                                                    <td width="22%" rowspan="4"><span class="style3 style3"><div align="right" ></div>
                                                        </span></td>
                                                    <td width="46%" rowspan="4"><span class="style3 style3"><div align="right" >Rp. 7.920.000,- </div></span></td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2"><label>
                                                            <select name="tipe_nota" tabindex="1">
                                                                <option value="0" selected>Invoice</option>
                                                                <option value="1">Retur</option>
                                                                <option value="2">Open Bill</option>
                                                                <option value="3">Credit Payment</option>
                                                            </select>
                                                        </label></td>
                                                </tr>
                                                <tr >
                                                    <td width="7%">&nbsp;</td>
                                                    <td width="15%">&nbsp;</td>
                                                </tr>
                                                <tr>
                                                    <td width="7%">a</td>
                                                    <td width="15%">: - </td>
                                                </tr>
                                            </table>

                                        </td>
                                    </tr>
                                    <tr>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td><table width="100%" border="0" cellspacing="2" cellpadding="0">
                                                <tr>
                                                    <td width="15%">c</td>
                                                    <td width="14%">b</td>
                                                    <td width="22%">a</td>
                                                    <td width="17%">&nbsp;</td>
                                                    <td width="16%">Cover Guest Name</td>
                                                    <td width="16%">Cover Number</td>
                                                </tr>
                                                <tr>
                                                    <td><label>
                                                            <select name="tipe" id="tipe" tabindex="2" onChange='disablefield();'>
                                                                <option value="0" selected>OnGuest</option>
                                                                <option value="1">InGuest</option>
                                                            </select>
                                                        </label></td>
                                                    <td>
                                                        <label>
                                                            <input name="cin" id="cin" type="text" size="20" tabindex="3">
                                                        </label></td>
                                                    <td><label>
                                                            <input name="nama" id="nama_tamu" type="text" size="32" tabindex="4">
                                                        </label></td>
                                                    <td><label>
                                                            <input name="nama"  size="4" maxlength="9" type="hidden" tabindex="5">
                                                        </label></td>
                                                    <td><label>
                                                            <input name="textfield8" id="cover_name" type="text" size="20" tabindex="6">
                                                        </label></td>
                                                    <td><label>
                                                            <input name="textfield8" id="cover_number" type="text" size="20" tabindex="7">
                                                        </label></td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                <tr>
                                                    <td width="12%">Kode</td>
                                                    <td width="27%">Nama</td>
                                                    <td width="13%">Harga</td>
                                                    <td width="9%">Tipe Pot</td>
                                                    <td width="17%">Pot</td>
                                                    <td width="6%">Qty</td>
                                                    <td width="16%">Total</td>
                                                </tr>
                                                <tr>
                                                    <td><label>
                                                            <input name="textfield8" type="text" size="13" maxlength="9" tabindex="8">
                                                        </label></td>
                                                    <td><label>
                                                            <input name="textfield8" type="text" size="35" maxlength="9" tabindex="10">
                                                        </label></td>
                                                    <td><label>
                                                            <input name="textfield8" type="text" size="15" maxlength="9" tabindex="11">
                                                        </label></td>
                                                    <td><label>
                                                            <select name="tipe_pot" tabindex="12">
                                                                <option value="0" selected>%</option>
                                                                <option value="1">value</option>
                                                            </select>
                                                        </label></td>
                                                    <td><label>
                                                            <input name="textfield8" type="text" size="20" maxlength="9" tabindex="13">
                                                        </label></td>
                                                    <td><label>
                                                            <input name="textfield8" type="text" size="7" maxlength="3" tabindex="14">
                                                        </label></td>
                                                    <td><label>
                                                            <input name="textfield8" type="text" size="20" maxlength="9" tabindex="15">
                                                        </label></td>
                                                </tr>
                                            </table></td>
                                    </tr>
                                    <tr>
                                        <td height="95">&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                <tr>
                                                    <td width="67%">&nbsp;</td>
                                                    <td width="11%">Total Nota </td>
                                                    <td width="22%"><label>
                                                            <input name="textfield8" type="text" size="25" maxlength="9" tabindex="16">
                                                        </label></td>
                                                </tr>
                                                <tr>
                                                    <td>&nbsp;</td>
                                                    <td>&nbsp;</td>
                                                    <td>&nbsp;</td>
                                                </tr>
                                            </table></td>
                                    </tr>
                                    <tr>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                <tr>
                                                    <td width="17%"><span class="style3 style3">Total Item : </span></td>
                                                    <td width="50%"><span class="style3 style3">&nbsp;</span></td>
                                                    <td width="33%">&nbsp;</td>
                                                </tr>
                                                <tr>
                                                    <td>&nbsp;</td>
                                                    <td>&nbsp;</td>
                                                    <td>&nbsp;</td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table></fieldset>
                                <!-- #EndEditable --></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>

        <form id="overlay_form" style="display:none">

            <a href="#" id="close" >Close</a>
        </form>
    </body>
    <!-- #EndTemplate --></html>

