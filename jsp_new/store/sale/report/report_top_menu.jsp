<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
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
       //0      1       2        3          4            5              6            7             8               9             10             11           12      13
    {"Lokasi","Shift","Kasir","Kategori","Tanggal","Urut Berdasar","Kode Sales","Semua Lokasi","Search By","Kategori Order","Pembayaran","Kewarganegaraan","Item","Waiter"},
    {"Location","Shift","Cashier","Category","Sale Date","Sort By","Sales Code","All Location","Search By","Order Category","Payment","Nationality","Item","Waiter"}
};

public static final String textButton[][] = 
{
    {"Rekap Harian","Laporan Top Menu","--Pilih--","Cari Laporan","Semua"},
    {"Daily Report","Top Menu Report" ,"--Select--","Get Report","All"}
};

public static final String comboBox[][] = 
{
    {"Dine In","Take Away","Delivery Order","Lunas","Kredit"},
    {"Dine In","Take Away" ,"Delivery Order","Cash","Credit"}
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
    /**
    get data from 'hidden form'
    */
    int iCommand = FRMQueryString.requestCommand(request);
    ControlLine ctrLine = new ControlLine();
    SrcReportSale srcReportSale = new SrcReportSale();
    FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale();
    Vector listMatCategory = new Vector(1,1);
    
    try{
        srcReportSale = (SrcReportSale)session.getValue(SessReportSale.SESS_SRC_REPORT_SALE_REKAP);
    }catch(Exception e){
        srcReportSale = new SrcReportSale();
    }

    if(srcReportSale==null){
        srcReportSale = new SrcReportSale();
    }
    
    try{
        session.removeValue(SessReportSale.SESS_SRC_REPORT_SALE_REKAP);
    }catch(Exception e){
    }
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>

<script language="JavaScript">
function cmdSearch(){
    document.frmsrcreportsale.command.value="<%=Command.LIST%>";
    document.frmsrcreportsale.action="reportsalerekaptanggal_list.jsp";
    document.frmsrcreportsale.submit();
}
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
            margin-top :-300px;
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
                    <input type="hidden" name="command" id="command" value="<%=iCommand%>">
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
                                    <td height="21" width="40%">&nbsp; 
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
                                    <td width="9%"><%=getJspTitle(11,SESS_LANGUAGE,"",true)%></td>
                                    <td width="1%">:</td>
                                    <td width="40%">
                                        <%
                                        Vector listCountry = new Vector(1,1);
                                        Vector val_countryid = new Vector(1,1);
                                        Vector key_countryid = new Vector(1,1);
                                        
                                        val_countryid.add("0");
                                        key_countryid.add("--"+textButton[SESS_LANGUAGE][4]+"--");
                                        
                                        listCountry = PstNegara.list(0,0,"","");
                                        for (int a=0;a<listCountry.size();a++){
                                            Negara negara = (Negara)listCountry.get(a);
                                            val_countryid.add(""+negara.getOID());
                                            key_countryid.add(""+negara.getNmNegara());
                                            
                                        }
                                        %>
                                        <%=ControlCombo.draw("country", null, "0", val_countryid, key_countryid, "", "formElemen")%> 
                                        
                                    </td>
                                </tr>
                                <tr> 
                                    <td valign="top" width="9%" align="left"><%=getJspTitle(5,SESS_LANGUAGE,"",true)%></td>
                                    <td valign="top" width="1%" align="left">:</td>
                                    <td width="40%" valign="top" align="left"> &nbsp; <%=ControlDate.drawDate(frmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_DATE_FROM], srcReportSale.getDateFrom(),"formElemen",1,-5)%> s/d <%=	ControlDate.drawDate(frmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_DATE_TO], srcReportSale.getDateTo(),"formElemen",1,-5) %> </td>
                                    
                                    <td width="9%"><%=getJspTitle(3,SESS_LANGUAGE,"",true)%></td>
                                    <td width="1%">:</td>
                                    <td width="40%">
                                        <%
                                            Vector val_materialid = new Vector(1,1);
                                            Vector key_materialid = new Vector(1,1);
                                            Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                                            Vector materGroup = PstCategory.structureList(masterCatAcak) ;
                                            
                                            val_materialid.add("0");
                                            key_materialid.add("--"+textButton[SESS_LANGUAGE][4]+"--");
                                            
                                            if(materGroup!=null && materGroup.size()>0) {
                                                String parent="";
                                                Vector<Long>levelParent = new Vector<Long>();
                                                for(int i=0; i<materGroup.size(); i++) {
                                                    Category mGroup = (Category)materGroup.get(i);
                                                    if(mGroup.getCatParentId()!=0){
                                                        for(int lv=levelParent.size()-1; lv > -1; lv--){
                                                            long oidLevel=levelParent.get(lv);
                                                            if(oidLevel==mGroup.getCatParentId()){
                                                                break;
                                                            }else{
                                                                levelParent.remove(lv);
                                                            }
                                                        }
                                                        parent="";
                                                        for(int lv=0; lv<levelParent.size(); lv++){
                                                           parent=parent+"&nbsp;&nbsp;&nbsp;&nbsp;";
                                                        }
                                                        levelParent.add(mGroup.getOID());
                                                        
                                                    }else{
                                                        levelParent.removeAllElements();
                                                        levelParent.add(mGroup.getOID());
                                                        parent="";
                                                    }
                                                    val_materialid.add(""+mGroup.getOID()+"");
                                                    key_materialid.add(""+parent+mGroup.getName()+"");
                                                }
                                                %>
                                                <%=ControlCombo.draw("categoryId", null, "0", val_materialid, key_materialid, "", "formElemen")%>
                                        <%
                                            }
                                        %>
                                    </td>
                                </tr>
                                <tr> 
                                    <td valign="top" width="9%" align="left"><%=getJspTitle(9,SESS_LANGUAGE,"",true)%></td>
                                    <td valign="top" width="1%" align="left">:</td>
                                    <td width="40%" valign="top" align="left"> &nbsp;
                                        <% 
                                            Vector val_group_reportid = new Vector(1,1);
                                            Vector key_group_reportid = new Vector(1,1); 
                                            
                                            val_group_reportid.add("4");
                                            key_group_reportid.add("--"+textButton[SESS_LANGUAGE][4]+"--");
                                            val_group_reportid.add("0");
                                            key_group_reportid.add(""+comboBox[SESS_LANGUAGE][0]+"");
                                            val_group_reportid.add("1");
                                            key_group_reportid.add(""+comboBox[SESS_LANGUAGE][1]+"");
                                            val_group_reportid.add("2");
                                            key_group_reportid.add(""+comboBox[SESS_LANGUAGE][2]+"");
                                            
                                        %> 
                                        <%=ControlCombo.draw(frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_SORT_BY], null, "0", val_group_reportid, key_group_reportid, "", "formElemen")%> 
                                        
                                    </td>
                                    <td width="9%"><%=getJspTitle(13,SESS_LANGUAGE,"",true)%></td>
                                    <td width="1%">:</td>
                                    <td width="40%">
                                        <%
                                        Vector listSalesPerson = new Vector(1, 1);
                                          Vector val_salesid = new Vector(1, 1);
                                          Vector key_salesid = new Vector(1, 1);
                                          String whereClause = PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_GROUP_USER_ID] + " = 6";
                                          Vector sale = PstMappingUserGroup.list(0, 0, whereClause, "");

                                          val_salesid.add("0");
                                          key_salesid.add("--" + textButton[SESS_LANGUAGE][4] + "--");
                                          for (int d = 0; d < sale.size(); d++) {
                                            MappingUserGroup mg = (MappingUserGroup) sale.get(d);
                                            AppUser ap = new AppUser();
                                            try {
                                              ap = PstAppUser.fetch(mg.getUserId());;
                                            } catch (Exception e) {
                                            }
                                            val_salesid.add("" + ap.getOID() + "");
                                            key_salesid.add(ap.getFullName());
                                          }
                                        %>
                                        <%=ControlCombo.draw("sales", null, "0", val_salesid, key_salesid, "", "formElemen")%> 
                                        
                                    </td>
                                </tr>
                                <tr> 
                                    <td valign="top" width="9%" align="left"><%=getJspTitle(10,SESS_LANGUAGE,"",true)%></td>
                                    <td valign="top" width="1%" align="left">:</td>
                                    <td width="40%" valign="top" align="left"> &nbsp;
                                        <% 
                                            Vector val_group_paymentid = new Vector(1,1);
                                            Vector key_group_paymentid = new Vector(1,1); 
                                            
                                            val_group_paymentid.add("3");
                                            key_group_paymentid.add("--"+textButton[SESS_LANGUAGE][4]+"--");
                                            val_group_paymentid.add("0");
                                            key_group_paymentid.add(""+comboBox[SESS_LANGUAGE][3]+"");
                                            val_group_paymentid.add("1");
                                            key_group_paymentid.add(""+comboBox[SESS_LANGUAGE][4]+"");                     
                                        %> 
                                        <%=ControlCombo.draw("paymentType", null, "0", val_group_paymentid, key_group_paymentid, "", "formElemen")%> 
                                    
                                    </td>
                                    <td width="9%">
                                        &nbsp;
                                    </td>
                                    <td width="1%">&nbsp;</td>
                                    <td width="40%">
                                        
                                    </td>
                                </tr>
                                <tr> 
                                    <td valign="top" width="9%" align="left">&nbsp;</td>
                                    <td valign="top" width="1%" align="left">&nbsp;</td>
                                    <td width="40%" valign="top" align="left">&nbsp;</td>
                                    <td width="9%">&nbsp;</td>
                                    <td width="1%">&nbsp;</td>
                                    <td width="40%">&nbsp;</td>
                                </tr>
                                <tr> 
                                    <td valign="top" width="9%" align="left"></td>
                                    <td valign="top" width="1%" align="left"></td>
                                    <td width="40%" valign="top" align="left">
                                        &nbsp;
                                        <a class="btn-primary btn-lg" style="cursor:pointer" id="searchReport" ><i class="fa fa-search"> &nbsp;<%= textButton[SESS_LANGUAGE][3]%></i></a></td>
                                    <td width="9%"></td>
                                    <td width="1%"></td>
                                    <td width="40%"></td>
                                </tr>
                                <tr> 
                                    <td height="10" valign="top" width="9%" align="left">&nbsp;</td>
                                    <td height="10" valign="top" width="1%" align="left">&nbsp;</td>
                                    <td height="10" width="40%" valign="top" align="left"></td>
                                    <td height="10" valign="top" width="9%" align="left">&nbsp;</td>
                                    <td height="10" valign="top" width="1%" align="left">&nbsp;</td>
                                    <td height="10" width="40%" valign="top" align="left"></td>
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
<style>
    @media print {
        body{
            visibility: hidden;
        }
        #reportPlace{
            visibility: visible;
            margin-top :-300px;
        }
        .printReport{
           visibility: hidden; 
        }
    }
</style>
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
            if (title=="getReport"){
                printReport();
            }
        }
        
        function searchReport(){
            $('#searchReport').click(function(){
                $('#command').val('<%=Command.GET%>');
                var url = ""+base+"/SaleReportHandler";
                var data = $('#frmsrcreportsale').serialize();
                var data = data + "&lang="+language+"";
                //alert(data);
                ajaxSalesReportHandler(url,data,"POST","#reportPlace","getReport");
            });
        }
        
        function printReport(){
            $('.printReport').click(function(){
                window.print();
            });
        }
        searchReport();
    });
</script>

</body>
<!-- #EndTemplate --></html>
