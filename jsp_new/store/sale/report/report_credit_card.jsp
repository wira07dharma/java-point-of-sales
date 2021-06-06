<%@page import="com.dimata.posbo.entity.search.SrcReportSale"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcReportSale"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_DAILY_SUMMARY); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListHeader[][] = 
{
    {"Lokasi","Shift","Kasir","Supplier","Kategori","Tanggal","Urut Berdasar","Kode Sales","Semua Lokasi","Search By"},
    {"Location","Shift","Cashier","Supplier","Category","Sale Date","Sort By","Sales Code","All Location","Search By"}
};

public static final String textButton[][] = 
{
    {"Rekap Harian","Laporan Kartu Kredit","--Pilih--","Cari Laporan"},
    {"Daily Report","Credit Card Report" ,"--Select--","Get Report"}
};

public String getJspTitle(int index, int language, String prefiks, boolean addBody)
{
    String result = "";
    if(addBody){
        if(language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){	
            result = textListHeader[language][index] + " " + prefiks;
        }else{
            result = prefiks + " " + textListHeader[language][index];		
        }
    }else{
        result = textListHeader[language][index];
    } 
    return result;
}
%>
<%
    FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale();
    ControlLine ctrLine = new ControlLine();
    SrcReportSale srcReportSale = new SrcReportSale();
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>

<script language="JavaScript">

</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css"
rel="stylesheet" />
<%}%>

<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<!-- #EndEditable --> 
<style>
    @media print {
        body{
            visibility: hidden;
        }
        #reportPlace{
            visibility: visible;
            margin-top :-250px;
        }
        .printReport{
           visibility: hidden; 
        }
    }
</style>
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <%if(menuUsed == MENU_PER_TRANS){%>
  <tr>
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
      <%@ include file = "../../../main/header.jsp" %>
      <!-- #EndEditable --></td>
  </tr>
  <tr>
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
  <%}else{%>
   <tr bgcolor="#FFFFFF">
    <td height="10" ID="MAINMENU">
      <%@include file="../../../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --><%=textButton[SESS_LANGUAGE][1]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
            <td><!-- #BeginEditable "content" --> 
                <form id="frmsrcreportsale" name="frmsrcreportsale" method="post" action="">
                    <input type="hidden" name="command" id="command" value="">
                    <table width="100%" border="0">
                        <tr> 
                          <td colspan="2"> 
                            <hr size="1">
                          </td>
                        </tr>
                        <tr> 
                          <td colspan="2"> 
                            <table width="100%" border="0" cellspacing="1" cellpadding="1">
                                <tr> 
                                    <td height="21" width="9%"><%=getJspTitle(0,SESS_LANGUAGE,"",true)%></td>
                                    <td height="21" width="1%">:</td>
                                    <td height="21" width="90%">&nbsp; 
                                        <% 
                                            Vector val_locationid = new Vector(1,1);
                                            Vector key_locationid = new Vector(1,1); 

                                            boolean checkDataAllLocReportView = PstDataCustom.checkDataAllLocReportView(userId, "user_view_sale_stock_report_location","0");
                                            String whereLocViewReport = PstDataCustom.whereLocReportView(userId, "user_view_sale_stock_report_location","");
                                            Vector vt_loc = PstLocation.listLocationStore(0,0,whereLocViewReport,PstLocation.fieldNames[PstLocation.FLD_NAME]);

                                            if(checkDataAllLocReportView){
                                                val_locationid.add("0");
                                                key_locationid.add(textListHeader[SESS_LANGUAGE][8]);
                                            }
                                            for(int d=0;d<vt_loc.size();d++){
                                                Location loc = (Location)vt_loc.get(d);
                                                val_locationid.add(""+loc.getOID()+"");
                                                key_locationid.add(loc.getName());
                                            }
                                        %> 
                                        <%=ControlCombo.draw(frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_LOCATION_ID], null, ""+srcReportSale.getLocationId(), val_locationid, key_locationid, "", "formElemen")%> 
                                    </td>
                                </tr>
                                <tr> 
                                    <td valign="top" width="9%" align="left"><%=getJspTitle(5,SESS_LANGUAGE,"",true)%></td>
                                    <td valign="top" width="1%" align="left">:</td>
                                    <td width="90%" valign="top" align="left"> &nbsp; <%=ControlDate.drawDate(frmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_DATE_FROM], srcReportSale.getDateFrom(),"formElemen",1,-5)%> s/d <%=	ControlDate.drawDate(frmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_DATE_TO], srcReportSale.getDateTo(),"formElemen",1,-5) %> </td>
                                </tr>
                                
                                
                                <tr> 
                                    <td height="10" valign="top" width="9%" align="left">&nbsp;</td>
                                    <td height="10" valign="top" width="1%" align="left">&nbsp;</td>
                                    <td height="10" width="90%" valign="top" align="left"></td>
                                </tr> 
                                <tr> 
                                    <td height="21" valign="top" width="9%" align="left">&nbsp;</td>
                                    <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                                    <td height="21" width="90%" valign="top" align="left"> 
                                        <table width="40%" border="0" cellspacing="0" cellpadding="0">
                                            <tr> 
                                              <td nowrap width="7%">&nbsp;
                                                  <a class="btn-primary btn-lg" style="cursor:pointer" id="searchReport" ><i class="fa fa-search"> &nbsp;<%= textButton[SESS_LANGUAGE][3]%></i></a>
                                              </td>
                                              <td nowrap width="3%">&nbsp;</td>
                                              <td class="command" nowrap width="90%"></td>
                                            </tr>
                                        </table>
                                  </td>
                                </tr>
                            </table>
                            <br>
                            <div id="reportPlace">

                            </div>
                          </td>
                        </tr>
                    </table>
                </form>
            </td> 
        </tr> 
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
       <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../../styletemplate/footer.jsp" %>

        <%}else{%>
            <%@ include file = "../../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>

<script type="text/javascript" src="../../../styles/jquery.min.js"></script>
<script type="text/javascript" src="../../../styles/bootstrap3.1/js/bootstrap.min.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
        
        var base ="<%=approot%>";
        var language ="<%= SESS_LANGUAGE%>";
        
        function ajaxSalesReportHandler(url,data,type,append,title){
            $.ajax({
                url : ""+url+"",
                data: ""+data+"",
                type : ""+type+"",
                async : false,
                cache: false,
                success : function(data) {   
                    $(''+append+'').html(data);
                    onSuccess(data,title);
                },
                error : function(data){
                    //alert('error');
                }
            }).done(function(data){
                onDone(data,title);    
            });
        }
        
        function onSuccess(data,title){
          
        }
        
        function onDone(data,title){
           if (title=="getCCReport") {
               printReport();
           }
        }
        
        function getReport(){
            $('#searchReport').click(function(){
                $('#command').val(<%= Command.LIST%>);
                var url = ""+base+"/SaleReportHandler";
                var data = $('#frmsrcreportsale').serialize();
                data = data + "&lang="+language+"";
                
                ajaxSalesReportHandler(url,data,"POST","#reportPlace","getCCReport");
                
            });
        }
        
        function printReport(){               
            $('.printReport').click(function(){
                window.print();
            });
        }
        
        getReport();
    });
</script>

</body>
<!-- #EndTemplate --></html>
