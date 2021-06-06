<%--
    Document   : menu_i
    Created on : Aug 26, 2013, 1:51:17 PM
    Author     : user
--%>


<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
int  appObjHeaderHome =AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_HEADER, AppObjInfo.OBJ_MENU_HEADER_HOME);
int  appObjHeaderPenjualan =AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_HEADER, AppObjInfo.OBJ_MENU_HEADER_PENJUALAN);
int  appObjHeaderPembelian =AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_HEADER, AppObjInfo.OBJ_MENU_HEADER_PEMBELIAN);
int  appObjHeaderPenerimaan =AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_HEADER, AppObjInfo.OBJ_MENU_HEADER_PENERIMAAN);
int  appObjHeaderRetun =AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_HEADER, AppObjInfo.OBJ_MENU_HEADER_RETURN);
int  appObjHeaderTransfer =AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_HEADER, AppObjInfo.OBJ_MENU_HEADER_TRANSFER);
int  appObjHeaderPembiayaan =AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_HEADER, AppObjInfo.OBJ_MENU_HEADER_PEMBIAYAAN);
int  appObjHeaderStok =AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_HEADER, AppObjInfo.OBJ_MENU_HEADER_STOK);
int  appObjHeaderSistem =AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_HEADER, AppObjInfo.OBJ_MENU_HEADER_SISTEM);
int  appObjHeaderMasterdata =AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_HEADER, AppObjInfo.OBJ_MENU_HEADER_MASTERDATA);

int appObjectCashier = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_KASIR, AppObjInfo.OBJ_INVOICE);

boolean privApprovalHeaderHome = userSession.checkPrivilege(AppObjInfo.composeCode(appObjHeaderHome, AppObjInfo.COMMAND_VIEW));
boolean privApprovalHeaderPenjualan = userSession.checkPrivilege(AppObjInfo.composeCode(appObjHeaderPenjualan, AppObjInfo.COMMAND_VIEW));
boolean privApprovalHeaderPembelian = userSession.checkPrivilege(AppObjInfo.composeCode(appObjHeaderPembelian, AppObjInfo.COMMAND_VIEW));
boolean privApprovalHeaderPenerimaan = userSession.checkPrivilege(AppObjInfo.composeCode(appObjHeaderPenerimaan, AppObjInfo.COMMAND_VIEW));
boolean privApprovalHeaderReturn = userSession.checkPrivilege(AppObjInfo.composeCode(appObjHeaderRetun, AppObjInfo.COMMAND_VIEW));
boolean privApprovalHeaderTransfer = userSession.checkPrivilege(AppObjInfo.composeCode(appObjHeaderTransfer, AppObjInfo.COMMAND_VIEW));
boolean privApprovalHeaderPembiayaan = userSession.checkPrivilege(AppObjInfo.composeCode(appObjHeaderPembiayaan, AppObjInfo.COMMAND_VIEW));
boolean privApprovalHeaderStok = userSession.checkPrivilege(AppObjInfo.composeCode(appObjHeaderStok, AppObjInfo.COMMAND_VIEW));
boolean privApprovalHeaderSistem = userSession.checkPrivilege(AppObjInfo.composeCode(appObjHeaderSistem, AppObjInfo.COMMAND_VIEW));
boolean privApprovalHeaderMasterdata = userSession.checkPrivilege(AppObjInfo.composeCode(appObjHeaderMasterdata, AppObjInfo.COMMAND_VIEW));

boolean privViewCashierMenu =  userSession.checkPrivilege(AppObjInfo.composeCode(appObjectCashier, AppObjInfo.COMMAND_VIEW));
%>
<%
  String spliturlMenu[] = request.getServletPath().toString().trim().split("/");

 String urlMenu = null;
    if(spliturlMenu!=null && spliturlMenu.length>0){
        int idxLnght=spliturlMenu.length -1;
           try{
            urlMenu  = spliturlMenu[idxLnght];
           }catch(Exception exc){
           }
    }

 String homeActive="";
 String employeeActive="";
 String trainingActive="";
 String reportsActive="";
 String canteenActive="";
 String clinicActive="";
 String lockerActive="";
 String masterDataActive="";
 String systemActive="";
 String payrollSetupActive="";
 String overtimeActive="";
 String payrollProcessActive="";
 String helpActive="";

 if(urlMenu!=null && urlMenu.length()>0 && urlMenu.equalsIgnoreCase("home.jsp")){
     homeActive = "class=\"current\"";
 }else if(urlMenu!=null && urlMenu.length()>0 && urlMenu.equalsIgnoreCase("employee.jsp")){
     employeeActive = "class=\"current\"";
 }else if(urlMenu!=null && urlMenu.length()>0 && urlMenu.equalsIgnoreCase("training.jsp")){
     trainingActive = "class=\"current\"";
 }else if(urlMenu!=null && urlMenu.length()>0 && urlMenu.equalsIgnoreCase("reports.jsp")){
     reportsActive = "class=\"current\"";
 }else if(urlMenu!=null && urlMenu.length()>0 && urlMenu.equalsIgnoreCase("canteen.jsp")){
     canteenActive = "class=\"current\"";
 }else if(urlMenu!=null && urlMenu.length()>0 && urlMenu.equalsIgnoreCase("clinic.jsp")){
     clinicActive = "class=\"current\"";
 }else if(urlMenu!=null && urlMenu.length()>0 && urlMenu.equalsIgnoreCase("locker.jsp")){
     lockerActive = "class=\"current\"";
 }else if(urlMenu!=null && urlMenu.length()>0 && urlMenu.equalsIgnoreCase("master_data.jsp")){
     masterDataActive = "class=\"current\"";
 }else if(urlMenu!=null && urlMenu.length()>0 && urlMenu.equalsIgnoreCase("system.jsp")){
     systemActive = "class=\"current\"";
 }else if(urlMenu!=null && urlMenu.length()>0 && urlMenu.equalsIgnoreCase("payroll_setup.jsp")){
     payrollSetupActive = "class=\"current\"";
 }else if(urlMenu!=null && urlMenu.length()>0 && urlMenu.equalsIgnoreCase("overtime.jsp")){
     overtimeActive = "class=\"current\"";
 }else if(urlMenu!=null && urlMenu.length()>0 && urlMenu.equalsIgnoreCase("payroll_process.jsp")){
     payrollProcessActive = "class=\"current\"";
 }else if(urlMenu!=null && urlMenu.length()>0 && urlMenu.equalsIgnoreCase("help.jsp")){
     helpActive = "class=\"current\"";
 }

%>
<!DOCTYPE html>
<!--<link href="../menustylesheet/sample_menu_i.css" rel="stylesheet" type="text/css"> 1155px-->
<style>
    #tabs25{
        position:relative;
        height:90px;
        width: 100%;
        font-size:11px;
        background-color: <%=bgMenu%>;
        font-family:Verdana, Arial, Helvetica, sans-serif;
        overflow:auto;
        /*font-weight: bold;*/
        margin:auto;
    }
    ul.flatflipbuttons{
        margin:0;
        padding-right:0px;
        list-style:none;
        -webkit-perspective: 10000px; /* larger the value, the less pronounced the 3D effect */
        -moz-perspective: 10000px;
        perspective: 10000px;
    }

    ul.flatflipbuttons li{
    /*padding: -10px;*/
    display: inline-block;
    width: 90px; /* dimensions of buttons. */
    /*height: 100px;*/
    /*margin-right: 4px; /* spacing between buttons */
    background: <%=bgMenu%>;
    color: <%=warnaFont%>;
    /*text-transform: uppercase;*/
    text-align: center;
   
    }
    .border_left_menu{
       /* border-right: 1px solid <%=garis2%>;*/
    }

    ul.flatflipbuttons li a{
    display:table;
    font: bold 36px Arial; /* font size, pertains to icon fonts specifically */
    width: 100%;
    height: 65%;
    /*margin-bottom: 4px;*/
    color: <%=fontMenu%>;
    background: #3B9DD5;
    text-decoration: none;
    outline: none;
    -webkit-transition:all 300ms ease-out; /* CSS3 transition. Last value is pause before transition play */
    -moz-transition:all 300ms ease-out;
    transition:all 300ms ease-out;


    }

    ul.flatflipbuttons li:nth-of-type(1) a{
    color: <%=bgMenu%>;
    background: <%=bgMenu%>;


    }

    ul.flatflipbuttons li:nth-of-type(2) a{
    background: <%=bgMenu%>;
    }

    ul.flatflipbuttons li:nth-of-type(3) a{
    background:  <%=bgMenu%>;
    }

    ul.flatflipbuttons li:nth-of-type(4) a{
    color: <%=bgMenu%>;
    background:  <%=bgMenu%>;
    }

    ul.flatflipbuttons li:nth-of-type(5) a{
    background:  <%=bgMenu%>;
    }

    ul.flatflipbuttons li:nth-of-type(6) a{
    color: <%=bgMenu%>;
    background:  <%=bgMenu%>;
    }

    ul.flatflipbuttons li:nth-of-type(7) a{
    background:  <%=bgMenu%>;
    }

    ul.flatflipbuttons li:nth-of-type(8) a{
    background:  <%=bgMenu%>;
    }

    ul.flatflipbuttons li:nth-of-type(9) a{
    color: <%=bgMenu%>;
    background:  <%=bgMenu%>;
    }

    ul.flatflipbuttons li:nth-of-type(10) a{
    background:  <%=bgMenu%>;
    }


    ul.flatflipbuttons li:nth-of-type(11) a{
    color: <%=bgMenu%>;
    background:  <%=bgMenu%>;
    }

    ul.flatflipbuttons li:nth-of-type(12) a{
    background:  <%=bgMenu%>;
    }

    ul.flatflipbuttons li:nth-of-type(13) a{
    background:  <%=bgMenu%>;
    }

    ul.flatflipbuttons li:nth-of-type(14) a{
    color: <%=bgMenu%>;
    background:  <%=bgMenu%>;
    }

    ul.flatflipbuttons li:nth-of-type(15) a{
    background:  <%=bgMenu%>;
    }
    
    ul.flatflipbuttons li a span{
    -moz-box-sizing: border-box;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    display: table-cell;

    width: 100%;
    height: 100%;
    -webkit-transition: all 300ms ease-out; /* CSS3 transition. */
    -moz-transition: all 300ms ease-out;
    transition: all 300ms ease-out;

    }

ul.flatflipbuttons li a img{ /* CSS for image if defined inside button */
border-width: 0;
/*vertical-align: middle;*/
}


ul.flatflipbuttons li:hover a{
    -webkit-transform: rotateY(180deg); /* flip horizontally 180deg*/
    -moz-transform: rotateY(180deg);
    transform: rotateY(180deg);
    /*background: <%=hoverMenu%>;*/ /* bgcolor of button onMouseover*/
    -webkit-transition-delay: 0.2s;
    -moz-transition-delay: 0.2s;
    transition-delay: 0.2s;
}

ul.flatflipbuttons li:hover a span{
    color: <%=fontMenu%>; /* color of icon font onMouseover */
    -webkit-transform: rotateY(180deg);
    -moz-transform: rotateY(180deg); /* flip horizontally 180deg*/
    transform: rotateY(180deg);
    -webkit-transition-delay: 0.2s;
    -moz-transition-delay: 0.2s;
    transition-delay: 0.2s;
}

ul.flatflipbuttons li:hover b{
opacity: 1;
}

/* CSS for 2nd menu below specifically */

ul.second li a{
background: #eee !important;
}

ul.second li a:hover{
background: #ddd !important;
}

</style>
<script language="JavaScript">
    function openHelp(){
        var strvalue  = "http://dimata.com/logbook.php?company=<%=USER_LOGBOOK%>&pass=<%=PASSWORD_LOGBOOK%>";
        winSrcMaterial = window.open(strvalue,"material", "height=600,width=800,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
        if (window.focus) { winSrcMaterial.focus();}
}
</script>
<%if(typeOfBusiness.equals("3")){//distribusi (RTC)%>
    <div id="tabs25">
        <ul class="flatflipbuttons">
            <%if(privApprovalHeaderHome){%>
                    <li><a href="<%=approot%>/homepage_menuicon_rtc.jsp?menu=home"><img src="<%=approot%>/menustylesheet/icon_pos/home_pos.png" /></a><%=homeActive.length()==0?"Home":"<b>Home</b>"%></li>
            <%}else{%>
                    <li><a href="#"><img src="<%=approot%>/menustylesheet/icon_pos/home_pos_enable.png" /></a><%=homeActive.length()==0?"Home":"<b>Home</b>"%></li>
            <%}%>
            <%if(privApprovalHeaderPenjualan){%>
                <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/homepage_menuicon_rtc.jsp?menu=penjualan"><span><img src="<%=approot%>/menustylesheet/icon_pos/sales_pos.png" /></span></a><%=employeeActive.length()==0?"Penjualan":"<b>Penjualan</b>"%></li>
            <%}else{%>
                <li style="border-left: 1px solid <%=garis2%>;"><a href="#"><span><img src="<%=approot%>/menustylesheet/icon_pos/sales_pos_enable.png" /></span></a><%=employeeActive.length()==0?"Penjualan":"<b>Penjualan</b>"%></li>
            <%}%>
            <%if(privApprovalHeaderPembelian){%>
                <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/homepage_menuicon_rtc.jsp?menu=pembelian"><span><img src="<%=approot%>/menustylesheet/icon_pos/purchase_pos.png" /></span></a><%=trainingActive.length()==0?"Pembelian":"<b>Pembelian</b>"%></li>
            <%}else{%>
                <li style="border-left: 1px solid <%=garis2%>;"><a href="#"><span><img src="<%=approot%>/menustylesheet/icon_pos/purchase_pos_enable.png" /></span></a><%=trainingActive.length()==0?"Pembelian":"<b>Pembelian</b>"%></li>
            <%}%>
            <%if(privApprovalHeaderPenerimaan){%>
                 <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/homepage_menuicon_rtc.jsp?menu=penerimaan"><span><img src="<%=approot%>/menustylesheet/icon_pos/receive_pos.png" /></span></a><%=reportsActive.length()==0?"Gudang":"<b>Gudang</b>"%></li>
            <%}else{%>
                 <li style="border-left: 1px solid <%=garis2%>;"><a href="#"><span><img src="<%=approot%>/menustylesheet/icon_pos/receive_pos_enable.png" /></span></a><%=reportsActive.length()==0?"Gudang":"<b>Gudang</b>"%></li>
            <%}%>
            <%if(privApprovalHeaderPembiayaan){%>
                <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/homepage_menuicon_rtc.jsp?menu=pembiayaan"><span><img src="<%=approot%>/menustylesheet/icon_pos/costing_pos.png" /></span></a><%=lockerActive.length()==0?"Pembiayaan":"<b>Pembiayaan</b>"%></li>
            <%}else{%>
                <li style="border-left: 1px solid <%=garis2%>;"><a href="#"><span><img src="<%=approot%>/menustylesheet/icon_pos/costing_pos_enable.png" /></span></a><%=lockerActive.length()==0?"Pembiayaan":"<b>Pembiayaan</b>"%></li>
            <%}%>
            <%if(privApprovalHeaderStok){%>
                <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/homepage_menuicon_rtc.jsp?menu=stock"><span><img src="<%=approot%>/menustylesheet/icon_pos/stok_pos.png" /></span></a><%=masterDataActive.length()==0?"Stok":"<b>Stok</b>"%></li>
            <%}else{%>
                <li style="border-left: 1px solid <%=garis2%>;"><a href="#"><span><img src="<%=approot%>/menustylesheet/icon_pos/stok_pos_enable.png" /></span></a><%=masterDataActive.length()==0?"Stok":"<b>Stok</b>"%></li>
            <%}%>
            <%if(privApprovalHeaderMasterdata){%>
                 <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/homepage_menuicon_rtc.jsp?menu=masterdata"><span><img src="<%=approot%>/menustylesheet/icon_pos/masterdata.png" /></span></a><%=systemActive.length()==0?"Masterdata":"<b>Masterdata</b>"%></li>
            <%}else{%>
                 <li style="border-left: 1px solid <%=garis2%>;"><a href="#"><span><img src="<%=approot%>/menustylesheet/icon_pos/masterdata_enable.png" /></span></a><%=systemActive.length()==0?"Masterdata":"<b>Masterdata</b>"%></li>
            <%}%>
            <%if(privApprovalHeaderSistem){%>
                 <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/homepage_menuicon_rtc.jsp?menu=sistem"><span><img src="<%=approot%>/menustylesheet/icon_pos/sistem_pos.png" /></span></a><%=payrollSetupActive.length()==0?"Sistem":"<b>Sistem</b>"%></li>
            <%}else{%>
                 <li style="border-left: 1px solid <%=garis2%>;"><a href="#"><span><img src="<%=approot%>/menustylesheet/icon_pos/sistem_pos_enable.png" /></span></a><%=payrollSetupActive.length()==0?"Sistem":"<b>Sistem</b>"%></li>
            <%}%>
            <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/logout.jsp"><span><img src="<%=approot%>/menustylesheet/icon_pos/keluar_pos.png" /></span></a><%=overtimeActive.length()==0?"Keluar":"<b>Keluar</b>"%></li>
        </ul>
    </div>
<%}else{%>
    <div id="tabs25">
        <ul class="flatflipbuttons">
            <li><a href="<%=approot%>/homepage_menuicon.jsp?menu=home"><img src="<%=approot%>/menustylesheet/icon_pos/home_pos.png" /></a><%=homeActive.length()==0?"Home":"<b>Home</b>"%></li>
            <%if(privViewCashierMenu){%>
            <%--<li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/homepage_menuicon.jsp?menu=kasir"><span><img src="<%=approot%>/menustylesheet/icon_pos/cashier.png" /></span></a><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Kasir":"Cashier"%></li>--%>
            <%}%>
            <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/homepage_menuicon.jsp?menu=penjualan"><span><img src="<%=approot%>/menustylesheet/icon_pos/sales_pos.png" /></span></a><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?" Penjualan":"Sales"%></li>
            <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/homepage_menuicon.jsp?menu=pembelian"><span><img src="<%=approot%>/menustylesheet/icon_pos/purchase_pos.png" /></span></a><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Pembelian":"Purchase"%></li>
            <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/homepage_menuicon.jsp?menu=penerimaan"><span><img src="<%=approot%>/menustylesheet/icon_pos/receive_pos.png" /></span></a><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Penerimaan":"Receive"%></li>
            <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/homepage_menuicon.jsp?menu=return"><span><img src="<%=approot%>/menustylesheet/icon_pos/return_pos.png" /></span></a><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Return":"Return"%></li>
            <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/homepage_menuicon.jsp?menu=transfer"><span><img src="<%=approot%>/menustylesheet/icon_pos/transfer_pos.png" /></span></a><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Transfer":"Dispatch"%></li>
            <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/homepage_menuicon.jsp?menu=pembiayaan"><span><img src="<%=approot%>/menustylesheet/icon_pos/costing_pos.png" /></span></a><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Pembiayaan":"Costing"%></li>
            <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/homepage_menuicon.jsp?menu=stock"><span><img src="<%=approot%>/menustylesheet/icon_pos/stok_pos.png" /></span></a><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Stok":"Stock"%></li>
            <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/homepage_menuicon.jsp?menu=masterdata"><span><img src="<%=approot%>/menustylesheet/icon_pos/masterdata.png" /></span></a><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Data Master":"Masterdata"%></li>
            <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/homepage_menuicon.jsp?menu=promosi"><span><img src="<%=approot%>/menustylesheet/icon_pos/promosi.png" /></span></a><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Promosi":"Promotion"%></li>
            <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/homepage_menuicon.jsp?menu=sistem"><span><img src="<%=approot%>/menustylesheet/icon_pos/sistem_pos.png" /></span></a><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Sistem":"System"%></li>
            <!-- <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/homepage_menuicon.jsp?menu=help"><span><img src="<%=approot%>/menustylesheet/icon_pos/help.png" /></span></a><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Help":"Help"%></li>-->
            <li style="border-left: 1px solid <%=garis2%>;"><a href="<%=approot%>/logout.jsp"><span><img src="<%=approot%>/menustylesheet/icon_pos/keluar_pos.png" /></span></a><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ?"Keluar":"Exit"%></li>
        </ul>
    </div>
<%}%>
