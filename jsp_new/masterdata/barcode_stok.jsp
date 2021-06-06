<%-- 
    Document   : barcode_stok
    Created on : Aug 15, 2015, 11:22:03 AM
    Author     : dimata005
--%>


<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.common.entity.periode.Periode"%>
<%@page import="com.dimata.common.entity.periode.PstPeriode"%>
<%@page import="com.dimata.common.entity.location.PstLocation"%>
<%@page import="com.dimata.common.entity.location.Location"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcReportStock"%>
<%@page import="com.dimata.posbo.session.warehouse.SessReportStock"%>
<%@page import="com.dimata.posbo.entity.search.SrcReportStock"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.posbo.entity.search.SrcMaterial"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.StandartRate"%>
<%@page import="com.dimata.common.entity.payment.PstStandartRate"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcMaterial"%>
<%@page import="com.dimata.gui.jsp.ControlCheckBox"%>
<%@page import="com.dimata.common.entity.payment.PriceType"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@page import="java.util.Vector"%>
<%@ include file = "../../main/javainit.jsp" %>
<%
    
    //String xx = request.getParameterValues("txtinput");
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request,"start");
    /** start fetch data */
    SrcReportStock srcReportStock = new SrcReportStock();
    SessReportStock sessReportStock = new SessReportStock();
    FrmSrcReportStock frmSrcReportStock = new FrmSrcReportStock(request, srcReportStock);
    String[] strPriceTypeId = request.getParameterValues(FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_PRICE_TYPE_ID]);
    long currencyID = FRMQueryString.requestLong(request, FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CURRENCY_TYPE_ID]);//request.getParameterValues(FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CURRENCY_TYPE_ID]);
    Vector vectPriceTypeId = new Vector(1, 1);
    
     if (strPriceTypeId != null && strPriceTypeId.length > 0) {
        for (int i = 0; i < strPriceTypeId.length; i++) {
            try {
                vectPriceTypeId.add(strPriceTypeId[i]);
            } catch (Exception exc) {
                System.out.println("err");
            }
        }
    }
    
    if(iCommand!=Command.SUBMIT){
    try {
            srcReportStock = (SrcReportStock)session.getValue(SessReportStock.SESS_SRC_REPORT_STOCK);
            session.putValue("SessPrintBarcode", srcReportStock);
            if (srcReportStock == null) srcReportStock = new SrcReportStock();
        } catch(Exception e) {
            srcReportStock = new SrcReportStock();
        }
    }else{
        try {
             srcReportStock = (SrcReportStock)session.getValue(SessReportStock.SESS_SRC_REPORT_STOCK);
            if (srcReportStock == null) srcReportStock = new SrcReportStock();
        } catch(Exception e) {
            srcReportStock = new SrcReportStock();
        }
    }
       

    session.putValue(SessReportStock.SESS_SRC_REPORT_STOCK, srcReportStock);


    Location location = new Location();
    if (srcReportStock.getLocationId() != 0) {
        try	{
            location = PstLocation.fetchExc(srcReportStock.getLocationId());
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    } else {
        location.setName("All");
    }

    Periode periode = new Periode();
    try {
        periode = PstPeriode.fetchExc(srcReportStock.getPeriodeId());
    } catch(Exception e){
        System.out.println(e.toString());
    }
    Vector<String> vSelected = new Vector();
    Vector listStockAll =  new Vector();
    int recordToGet = 500;

    if(iCommand==Command.SUBMIT){
        boolean calculateQtyNull = false;
       // listStockAll = sessReportStock.getReportStockAll(srcReportStock, calculateQtyNull, 0, 0);
        listStockAll = sessReportStock.getReportStockAllSummary(srcReportStock, calculateQtyNull, start, recordToGet);
        if(listStockAll.size()>0){
            for(int i=0; i<listStockAll.size(); i++) {
                Vector vt = (Vector)listStockAll.get(i);
                Material material = (Material)vt.get(0);
                vSelected.add(""+material.getOID());
            }
        }
    }
     
    SrcMaterial srcMaterial = new SrcMaterial();
    try {
       
        srcMaterial = (SrcMaterial) session.getValue(SessMaterial.SESS_SRC_MATERIAL);
        srcMaterial.setJenisCode(1);
        srcMaterial.setvNameMaterial(vSelected);
         srcMaterial.setPriceTypeId(vectPriceTypeId);
         srcMaterial.setSupplierId(-1);
         srcMaterial.setSelectCurrencyTypeId(currencyID);
        if (srcMaterial == null) {
            srcMaterial = new SrcMaterial();
        }
    } catch (Exception e) {
        srcMaterial = new SrcMaterial();
    }
    session.putValue(SessMaterial.SESS_SRC_MATERIAL, srcMaterial);

%>
<html>
    <head>
        <style type="text/css">
            .needsfilled {
                color:red;
            }
        </style>
        <link rel="stylesheet" type="text/css" href="table.css" />
        <script type ="text/javascript" src="../styles/jquery-1.4.3.min.js"></script>
        <script type ="text/javascript">   
            $(document).ready(function(){
                var i=parseInt($("#hidd").val());
                $("#btn_add").click(function(){
                    i=i+1;
                    $("#hidd").val(""+(i));
                    $("#table_1").append("<tr id='tr_"+i+"'><td id='index_"+i+"'>"+i+"</td><td><input type='checkbox' id='chk_"+i+"'/></td><td><input type='text' name='txtinput' id='txt_"+i+"'/></td></tr>");
                });
           
                $("#btn_remove").click(function(){
                    $("#hidd").val(""+(i));
                    var arr_chk_remove=new Array();
                    var arr_chk_exists =new Array();
                    for(j=1;j<=i;j++)
                    {
                        if($("#chk_"+j).is(':checked')){
                            var k = j+1;
                            arr_chk_remove.push(j);
                            $("#tr_"+j).remove();
                        }
                        else{
                            arr_chk_exists.push(j);
                        }
                    }
                    for(l=0;l<arr_chk_exists.length;l++){
                        var arr_element = arr_chk_exists[l];
                        if(arr_element == 1 ){
                            continue;
                        }
                        else {
                            chk_val = arr_element;
                            $("#tr_"+chk_val).attr('id','tr_'+(l+1));
                            $("#index_"+chk_val).replaceWith("<td id='index_"+(l+1)+"'>"+(l+1)+"</td>");
                            $("#chk_"+chk_val).attr('id','chk_'+(l+1));
                            $("#txt_"+chk_val).attr('id','txt_'+(l+1)).attr('name','txt_'+(l+1));
                        }
                    }
                    i=arr_chk_exists.length;
                });
           
                $("#myForm").submit(function(){
                    var row_length = i;
                    var bool= false;
                    for(j=1;j<=row_length;j++){
                   
                        if($('#txt_'+j).val() == null || $('#txt_'+j).val()== ""){
                            $("#txt_"+j).addClass("needsfilled");
                            $("#txt_"+j).val("Please Enter Item name");
                            return false;
                        }
                        if($('#cmb_'+j).val()=="")
                        {
                            //$('#cmb_'+j).parent().append('<font class="needsfilled">Please select prize</font>');
                            $('#cmb_'+j).addClass("needsfilled");
                           
                           
                            return false;
                        }
                        else{
                            continue;
                        }
                   
                    }
                    if($(":input").hasClass("needsfilled"))
                    {
                        return false;
                    }
                    else{
                        return true;
                    }
               
                   
                });
           
                $(":input").focus(function(){
                    if($(this).hasClass("needsfilled")){
                        $(this).val("");
                        $(this).removeClass("needsfilled");
                    }
                });
                $(":input").live("click",function(){
                    if($(this).hasClass("needsfilled")){
                        $(this).val("");
                        $(this).removeClass("needsfilled");
                    }
                });
           
           
            });
       
            function cmdSubmit() {
                    document.myForm.command.value="<%=Command.SUBMIT%>";
                    document.myForm.action="barcode_stok.jsp";
                    document.myForm.submit();
            }
           
        </script>

        <style type="text/css" >
            span.flip ,div.ex{
                background-color :#e5eecc;
                padding: 7px;
                border:solid 1px #c3c3c3;
            }
        </style>
    </head>
    <body>



        <form align=center name="myForm" id = "myForm">
            <table id="table_1" align=center>
                <input type="hidden" id="hidd" value="1" />
                <input type="hidden" name="command" value="0" />
                <input type="hidden" name="start" value="<%=start%>" />
                <tr><th colspan="4">Add/Remove Row With Validation</th></tr>
                <tr><th colspan="4">
                        <%
                            Vector val_PriceTypeid = new Vector(1, 1);
                            Vector key_PriceTypeid = new Vector(1, 1);
                            Vector vt_prc = PstPriceType.list(0, 0, "", "");
                            for (int d = 0; d < vt_prc.size(); d++) {
                                PriceType prc = (PriceType) vt_prc.get(d);
                                val_PriceTypeid.add("" + prc.getOID() + "");
                                key_PriceTypeid.add(prc.getCode());
                            }
                            ControlCheckBox controlCheckBox = new ControlCheckBox();
                            controlCheckBox.setWidth(5);

                        %>
                        <%=controlCheckBox.draw(FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_PRICE_TYPE_ID], val_PriceTypeid, key_PriceTypeid, new Vector())%>
                    </th></tr>
                <tr><th colspan="4">
                        <%
                            //add opie-eyek 20130805
                            Vector listCurr = PstStandartRate.listStandartRate(0, 0, "", "");
                            Vector vectCurrVal = new Vector(1, 1);
                            Vector vectCurrKey = new Vector(1, 1);
                            vectCurrKey.add("All");
                            vectCurrVal.add("-1");
                            for (int i = 0; i < listCurr.size(); i++) {
                                StandartRate standartRate = (StandartRate) listCurr.get(i);
                                CurrencyType crType = new CurrencyType();
                                String codeCurrency = "";
                                vectCurrVal.add("" + standartRate.getOID());
                                try {
                                    crType = PstCurrencyType.fetchExc(standartRate.getCurrencyTypeId());
                                    codeCurrency = crType.getCode();
                                } catch (Exception e) {
                                }
                                vectCurrKey.add(codeCurrency);
                            }
                        %> <%=ControlCombo.draw(FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CURRENCY_TYPE_ID], "formElemen", null, "", vectCurrVal, vectCurrKey, "")%>
                    </th></tr>
                <tr><th colspan="4"></th></tr>
                <tr><th colspan="4"><hr></th></tr>
                <tr>
                    <td></td>       
                </tr>
            </table>
            <table align=center>
                <tr>
                    <th>
                        <a href="javascript:cmdSubmit()" class="command" >Print</a>
                    </th>
                </tr>    
            </table>
        </form>
    </body>
<% if (listStockAll.size()!=0){%>
     <script language="JavaScript">
        window.open("<%=approot%>/servlet/com.dimata.posbo.report.masterdata.PrintAllListPriceTagPdf?tms=<%=System.currentTimeMillis()%>&","","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
        document.frmmaterial.submit();
        self.close();
    </script>   
<%}
%>    

</html>