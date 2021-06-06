<%@page import = "java.util.*,
    com.dimata.posbo.entity.warehouse.PstSourceStockCode,
    com.dimata.gui.jsp.ControlList,
    com.dimata.posbo.entity.masterdata.*,
    com.dimata.posbo.entity.warehouse.MatStockOpnameItem,
    com.dimata.qdep.entity.I_PstDocType,
    com.dimata.qdep.form.FRMHandler,
    com.dimata.qdep.form.FRMQueryString,
    com.dimata.posbo.entity.search.SrcMatStockOpname,
    com.dimata.posbo.session.warehouse.SessMatStockOpname,
    com.dimata.qdep.form.FRMMessage,
    com.dimata.posbo.entity.warehouse.MatStockOpname,
    com.dimata.posbo.form.warehouse.FrmMatStockOpname,
    com.dimata.posbo.form.warehouse.CtrlMatStockOpname,
    com.dimata.gui.jsp.ControlLine,
    com.dimata.util.Command,
    com.dimata.posbo.entity.warehouse.PstMatStockOpnameItem,
    com.dimata.common.entity.contact.PstContactList,
    com.dimata.common.entity.contact.ContactList,
    com.dimata.common.entity.contact.PstContactClass,
    com.dimata.gui.jsp.ControlCombo,
    com.dimata.common.entity.location.PstLocation,
    com.dimata.common.entity.location.Location,
    com.dimata.gui.jsp.ControlDate" 
%>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
    public static final String textListGlobal[][] = {
        {"Stok","Opname","Pencarian","Daftar","Edit","Tidak ada data opname","Cetak Opname","Material dengan barcode yang sama atau mirip lebih dari 1","Verifikasi","Verifikasi berhasil","Tutup","Simpan","Daftar Item","Lihat Tabel Histori"},
        {"Stock","Opname","Search","List","Edit","No opname data available","Print Opname","Material with same barcode more than 1","Verification", "Verifivation success","Close","Save","List Item","View History Table"}
    };
    public static final String textListOrderHeader[][] = {
        {"Nomor","Lokasi","Tanggal","Waktu","Status","Supplier","Kategori","Sub Kategori","Keterangan","Semua","Tabel Histori"},
        {"Number","Location","Date","Jam","Status","Supplier","Category","Sub Category","Remark","All","History Table"}
    };
    public static final String textListOrderItem[][] = {
        {"No","Sku","Nama Barang","Unit","Kategori","Sub Kategori","Qty Opname","Hapus","Kembali ke Daftar Opname"},
        {"No","Code","Name","Unit","Category","Sub Category","Qty Opname","Delete","Back to Opname List"}
    };
    public static final String textDelete[][] = {
        {"Apakah Anda Yakin Akan Menghapus Data ?","Apakah Anda Yakin Akan Menghapus Data Opname?"},
        {"Are You Sure to Delete This Data? ","Are You Sure to Delete This Opname Data? "}
    };
    
    public static final String textListState[][] ={
	{"Tunggu","Konfirmasi","Ya","Tidak","Silahkan selesaikan proses yang sedang berlangsung!","Kolom masih kosong","Barang Tidak Ditemukan"},
	{"Wait","Confirmation","yes","No","Please finish the curent process","This input required","Material cant found"}
    };
%>
<%
    //APROVAL
    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
    I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
    I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
    int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_OPN);
    //FORM REQUEST
    int iCommand = FRMQueryString.requestCommand(request);
    int prevCommand = FRMQueryString.requestInt(request,"prev_command");
    int startItem = FRMQueryString.requestInt(request,"start_item");
    int cmdItem = FRMQueryString.requestInt(request,"command_item");
    int appCommand = FRMQueryString.requestInt(request,"approval_command");
    long oidStockOpname = FRMQueryString.requestLong(request, "hidden_opname_id");
    long oidLocation = FRMQueryString.requestLong(request, "hidden_location_id");
    
    int typeharian = FRMQueryString.requestInt(request,"typeharian");
    
    SrcMatStockOpname srcMatStockOpname = new SrcMatStockOpname();
    if(session.getValue(SessMatStockOpname.SESS_SRC_MATSTOCKOPNAME)!=null){
        srcMatStockOpname = (SrcMatStockOpname)session.getValue(SessMatStockOpname.SESS_SRC_MATSTOCKOPNAME);
        session.putValue(SessMatStockOpname.SESS_SRC_MATSTOCKOPNAME, srcMatStockOpname);
    }
    
    String errMsg = "";
    int iErrCode = FRMMessage.ERR_NONE;
    String soCode = i_pstDocType.getDocCode(docType);
    String opnTitle = "Opname"; //i_pstDocType.getDocTitle(docType);
    String soItemTitle = opnTitle + " Item";

    ControlLine ctrLine = new ControlLine();
    CtrlMatStockOpname ctrlMatStockOpname = new CtrlMatStockOpname(request);

    iErrCode = ctrlMatStockOpname.action(iCommand , oidStockOpname, userName, userId,typeharian);
    FrmMatStockOpname frmso = ctrlMatStockOpname.getForm();
    MatStockOpname so = ctrlMatStockOpname.getMatStockOpname();
    errMsg = ctrlMatStockOpname.getMessage();
    String dateTimes = ""+Formater.formatDate(so.getStockOpnameDate(),"MM/dd/yyyy")+" "+so.getStockOpnameTime()+"";
    
    String dateOpnames = ""+Formater.formatDate(so.getStockOpnameDate(),"MM/dd/yyyy");
    String timeOpnames = ""+so.getStockOpnameTime()+"";
    
    boolean privManageData = true;
    if(so.getStockOpnameStatus()!= I_DocStatus.DOCUMENT_STATUS_DRAFT){
        privManageData=false;
    }
    
    oidStockOpname = so.getOID();
    oidLocation = so.getLocationId();
    int recordToGetItem = 20;
    int vectSizeItem = PstMatStockOpnameItem.getCount(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] + " = " + oidStockOpname);
    Vector listMatStockOpnameItem = PstMatStockOpnameItem.list(startItem,recordToGetItem,oidStockOpname);
    System.out.println("===>>>>>>>>> proses 2");
    int getCounter = SessMatStockOpname.getCountCounterList(oidStockOpname);

    String subcategory_name = "";
    try
    {
        System.out.println("===>>>>>>>>> proses 3");
        SubCategory scat = new SubCategory(); 
        subcategory_name = scat.getName();
        System.out.println("===>>>>>>>>> proses 4");
    }catch(Exception yyy){
        System.out.println(yyy);
        subcategory_name = "";
    }

    //list opie-eyek 20131216
    //Vector list = drawListOpnameItem(SESS_LANGUAGE,listMatStockOpnameItem,startItem,privManageData,approot); 
    //out.println(""+list.get(0));
    //Vector listError = (Vector)list.get(1);

    //Verification type (using finger or not)
    String verificationType = PstSystemProperty.getValueByName("KASIR_LOGIN_TYPE");
%>
<html>
<head>
    <title>Dimata - ProChain POS</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <%if(menuUsed == MENU_ICON){%>
        <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
    <%}%>
    
    <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
    <link type="text/css" rel="stylesheet" href="../../../styles/bootstrap3.1/css/bootstrap.css">
    <link type="text/css" rel="stylesheet" href="../../../styles/daterangepicker.css">
    
    <style>
        .hasErr{
            border-color: #843534;
            box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.075) inset, 0px 0px 6px #CE8483;   
        }
        .hasErr:focus{
            border-color: #843534;
            box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.075) inset, 0px 0px 6px #CE8483;   
        }
        
        
    </style>
    <script type="text/javascript" src="../../../styles/jquery.min.js"></script>
    <script type="text/javascript" src="../../../styles/bootstrap3.1/js/bootstrap.js"></script>
    <script type="text/javascript" src="../../../styles/moment.js"></script>
    <script type="text/javascript" src="../../../styles/daterangepicker.js"></script>
    <script type="text/javascript" src="../../../styles/bootstrap-typeahead.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){        
            var base ="<%=approot%>";
            var language ="<%= SESS_LANGUAGE%>";
            var idMatItem = "";
            function ajaxOpname(url,data,type,append,title){
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

                    }
                }).done(function(data){
                    onDone(data,title);    
                });
            }
        
            function onSuccess(data,title){
                
                if (title=="saveOpname"){
                    if (data!=""){
                        $('#hidden_opname_id').val(data);
                        $("#saveOpname").removeAttr('disabled').html('<%=textListGlobal[SESS_LANGUAGE][11]%>');
                        
                        getCodeOpname(data);
                    }
                }else if (title=="getCodeOpname"){                    
                    $('#oidOpnamePlace').html("<b>"+data+"</b>");
                    $('#stokOpnameNumber').val(data);
                    getListOpname();
                }else if (title=="getHistoryOpname"){
                    $('#viewHistory').modal('show');
                }
            }
        
            function onDone(data,title){
                
                if (title=="getListOpname"){
                    loadListItem();
                    saveItemList();
                    deleteItem();
                    editItem();
                    keyboardAdd();
                    tableControl();
                    getAutoComplete();
                    getPrintButton();
                    setTimeout(function(){
                        $('#skuInput').focus();
                    }, 400);
                    $('#statusNow').val('none');
                }else if (title=="getListItemModal"){
                    selectMaterial();
                    textual();
                }else if (title=="saveItemList"){
                    loadListItem();
                    getListOpname();
                    getListControlSuplier();
                    getListControlCategory();
                    getListControlStatus();
                    getDeleteButton();
                    getSaveButton();
                    
                    $('.saveItem').removeAttr('disabled').html('<%=textListGlobal[SESS_LANGUAGE][11]%>');
                }else if (title=="deleteItemList"){
                    getListOpname();
                    getListControlSuplier();
                    getListControlCategory();
                    getListControlStatus();
                    getDeleteButton();
                    getSaveButton();
                    $('#btnDeleteItemConfirmation').removeAttr('disabled').html('<%=textListState[SESS_LANGUAGE][2]%>');
                    $('#deleteOrderConfirmation').modal('hide');
                }else if (title=="editItem"){
                    loadListItem();
                    saveEditItem();
                    setTimeout(function(){
                        $('#skuInput').focus();
                    }, 400);
                    keyboardAdd();
                    getAutoComplete();
                    
                }else if (title=="saveEditItem"){
                    getListOpname();
                    getListControlSuplier();
                    getListControlCategory();
                    getListControlStatus();
                    getDeleteButton();
                    getSaveButton();
                    $('.saveEditItem').removeAttr('disabled').html('<%=textListGlobal[SESS_LANGUAGE][11]%>');
                }else if (title=="getSaveButton"){
                    saveOpname();
                }else if (title=="getDeleteButton"){
                    deleteOpname();
                }else if (title=="deleteOpname"){
                    $('#btnDeleteOpnameConfirmation').removeAttr('disabled').html('<%=textListState[SESS_LANGUAGE][2]%>');
                    if (data=="0"){
                        window.location = "<%=approot%>/warehouse/material/stock/mat_opname_src.jsp";
                    }
                }else if (title=="saveOpname"){
                    getDeleteButton();
                    getSaveButton();
                }else if(title=="getMatItemBySKU"){
                    var datas = data;
                    var temp = datas.split("=");
                    if (temp[0]=="0"){
                        alert("<%=textListState[SESS_LANGUAGE][6]%>");
                        $('#skuInput').focus();
                    }else{
                        var oidMaterial= temp[0];
                        var materialName = temp[2];
                        var unitId =temp[1]
                        var unit = temp[3];

                        $('#matOidInput').val(oidMaterial);
                        $('#materialInput').val(materialName);
                        $('#unitOidInput').val(unitId);
                        $('#unitInput').val(unit);

                        $('#qtyInput').focus();
                    }
                }else if (title=="getPrintButton"){
                    printButtonAction();
                }
            }
            
            function getListOpname(){
                var url = ""+base+"/AjaxOpname";
                var command = "<%= Command.LIST%>";
                var idOpname = $('#hidden_opname_id').val();
                var data="command="+command+"&idOpname="+idOpname+"&lang="+language+"&loadType=getListOpname";
                ajaxOpname(url,data,"POST","#dynamicContainer","getListOpname");
            }
            
            function saveOpname(){
                $('#saveOpname').click(function(){
                    var dateTime = $('#datetime').val();
                    var temp = dateTime.split(" ");
                    $('#opnameDate').val(temp[0]);
                    $('#opnameTime').val(temp[1]);
                    $('#command').val("<%=Command.SAVE%>");
                    var url = ""+base+"/AjaxOpname";
                    var data = $('#frmMatOpname').serialize();
                    $("#saveOpname").attr({"disabled":true}).html("<%=textListState[SESS_LANGUAGE][0]%>");
                    data = data + "&lang="+language+"&loadType=saveMasterOpname&userName=<%=userName%>&userId=<%=userId%>";
                    ajaxOpname(url,data,"POST","","saveOpname");
                });
                
            }
            
            function getCodeOpname(oidOpname){
                var url = ""+base+"/AjaxOpname";
                var data = "loadType=getCodeOpname&command=<%=Command.GET%>&oidOpname="+oidOpname+"";
                //alert(data);
                ajaxOpname(url,data,"POST","","getCodeOpname");
            }
            
            function loadListItem(){
                $('.loadListItem').click(function(){ 
                    var searchType = 0;
                    var sku = "";
                    var namaBarang = "";
                    var oidOpname = $('#hidden_opname_id').val();
                    if (oidOpname!=""){
                        $('#modalReport #sku').val('');
                        $('#modalReport #namaBarang').val('');
                        getListItemModal(searchType,sku,namaBarang,oidOpname);
                        $('#modalReport').modal('show');
                    }else{
                        alert("Please save first");
                    }
                    
                });
            }
            
            function getListItemModal(searchType,sku,namaBarang,oidOpname){
                var url = ""+base+"/AjaxOpname";
                var data = "loadType=getListItemModal&command=<%=Command.LIST%>&searchType="+searchType+"&sku="+sku+"&namaBarang="+namaBarang+"&oidOpname="+oidOpname+"&lang="+language+"";
                ajaxOpname(url,data,"POST","#modal-body-low","getListItemModal");
            }
            
            function searchModal(){
                $('.searchModal').click(function(){                   
                    var searchType = $(this).data('searchcategory');
                    var sku = $('#modalReport #sku').val();
                    var namaBarang = $('#modalReport #namaBarang').val();
                    var oidOpname = $('#hidden_opname_id').val();
                    getListItemModal(searchType,sku,namaBarang,oidOpname);
                });
            }
            
            function selectMaterial(){
                $('.selectMaterial').click(function(){
                    var oidMaterial=$(this).data('oidmaterial');
                    var materialName = $(this).data('materialname');
                    var sku = $(this).data('sku');
                    var unitId =$(this).data('unitid');
                    var unit = $(this).data('unit');
                    
                    $('#matOidInput').val(oidMaterial);
                    $('#skuInput').val(sku);
                    $('#materialInput').val(materialName);
                    $('#unitOidInput').val(unitId);
                    $('#unitInput').val(unit);
                    
                    $('#modalReport').modal('hide');
                    
                    $('#qtyInput').focus();
                });                
            }
 
            function validate(selectorTag){
                var errValidate = false;
                var errorIndex = -1;
                var idError = "";
                $(''+selectorTag+' .required').each(function(i){
                    var id = this.id;
                    var value = $(this).val();
                    $(this).parent().find('input').removeClass('hasErr');
                    $(this).parent().find('button').removeClass('hasErr');
                    if (value.length==0 || value ==0){
                        errValidate = true;
                        $(this).parent().find('input').addClass('hasErr');
                        $(this).parent().find('button').addClass('hasErr');
                        if (errorIndex==-1){
                            errorIndex = i;
                            idError = $(this).attr('id');
                        }
                    }                 
                });
               setTimeout(function(){
                    $('#'+idError+'').focus();
                }, 300);
               return errValidate;
            }
            
            function saveItemList(){
                $('.saveItem').click(function(){
                    if (validate("#formAdd")==false){  
                        $(this).attr('disabled','true').html("<%=textListState[SESS_LANGUAGE][0]%>");
                        var url = ""+base+"/AjaxOpname";
                        var data = $('#formAdd').serialize();
                        var oidOpname = $('#hidden_opname_id').val();
                        data = data + "&oidOpname="+oidOpname+"&command=<%=Command.SAVE%>&loadType=saveItemList&lang="+language+"&userName=<%=userName%>&userId=<%=userId%>";
                        ajaxOpname(url,data,"POST","","saveItemList");
                    }
                    
                });
            }
            
            function deleteItem(){
                $('.deleteItem').click(function(){
                    var oid = $(this).data('oidopnameitem');
                    idMatItem = oid;
                    $('#deleteOrderConfirmation').modal('show');
                });
            }
            
            function btnDeleteItemConfirmation(){
                $('#btnDeleteItemConfirmation').click(function(){
                    $(this).attr('disabled','true').html("<%=textListState[SESS_LANGUAGE][0]%>");
                    var url = ""+base+"/AjaxOpname";
                    var data = "command=<%=Command.DELETE%>&oidOpnameItem="+idMatItem+"&loadType=deleteItemList&userName=<%=userName%>&userId=<%=userId%>";
                    ajaxOpname(url,data,"POST","","deleteItemList");                    
                });
            }
            
            function editItem(){
                $('.editItem').click(function(){
                    var statusNow = $('#statusNow').val();
                    if (statusNow=="none"){
                        $('#statusNow').val('edit');
                        var id = $(this).attr('id');
                        var targetId="#listOpname-"+id+"";
                        var oIdOpname =$(this).data('oidopname');
                        var matOidInput =$(this).data('matoidinput');
                        var unitOidInput =$(this).data('unitoidinput');
                        var skuInput =$(this).data('skuinput');
                        var materialInput =$(this).data('materialinput');
                        var unitInput =$(this).data('unitinput');
                        var qtyInput =$(this).data('qtyinput');
                        var oidItemOpname =$(this).data('oiditemopname');                    
                        var url = ""+base+"/AjaxOpname";                    
                        var data = "command=<%=Command.LIST%>&loadType=editItem&oIdOpname="+oIdOpname+"&matOidInput="+matOidInput+"&unitOidInput="+unitOidInput+"&skuInput="+skuInput+"&materialInput="+materialInput+"&unitInput="+unitInput+"&qtyInput="+qtyInput+"&oidItemOpname="+oidItemOpname+"&i="+id+"&lang="+language+"";
                        
                        $('#addForm').remove();
                        $('.editItem').remove();
                        $('.deleteItem').remove();
                        ajaxOpname(url,data,"POST",targetId,"editItem"); 
                    }else{
                        alert('<%=textListState[SESS_LANGUAGE][4]%>');
                    }
                                        
                });
            }
            function saveEditItem(){
                $('.saveEditItem').click(function(){
                    if (validate("#formAdd")==false){   
                        $(this).attr('disabled','true').html("<%=textListState[SESS_LANGUAGE][0]%>");
                        var url = ""+base+"/AjaxOpname";
                        var data = $('#formAdd').serialize();
                        data = data + "&command=<%=Command.SAVE%>&loadType=saveEditItem&lang="+language+"&userName=<%=userName%>&userId=<%=userId%>";
                        ajaxOpname(url,data,"POST","","saveEditItem");
                    }
                });
            }
            
            function getListControlSuplier(){
                var url = ""+base+"/AjaxOpname";
                var command = "<%= Command.LIST%>";
                var idOpname = $('#hidden_opname_id').val();
                var data="command="+command+"&idOpname="+idOpname+"&lang="+language+"&loadType=getListControlSuplier";
                ajaxOpname(url,data,"POST","#suplierPlace","getListControlSuplier");
            }
            
            function getListControlCategory(){
                var url = ""+base+"/AjaxOpname";
                var command = "<%= Command.LIST%>";
                var idOpname = $('#hidden_opname_id').val();
                var data="command="+command+"&idOpname="+idOpname+"&lang="+language+"&loadType=getListControlCategory";
                ajaxOpname(url,data,"POST","#categoryPlace","getListControlCategory");
            }
            
            function getListControlStatus(){
                var url = ""+base+"/AjaxOpname";
                var command = "<%= Command.LIST%>";
                var idOpname = $('#hidden_opname_id').val();
                var data="command="+command+"&idOpname="+idOpname+"&lang="+language+"&loadType=getListControlStatus";
                ajaxOpname(url,data,"POST","#statusPlace","getListControlStatus");
            }
            
            function getDeleteButton(){
                var url = ""+base+"/AjaxOpname";
                var command = "<%= Command.LIST%>";
                var idOpname = $('#hidden_opname_id').val();
                var privDelete = "<%= privDelete%>";
                var data="command="+command+"&idOpname="+idOpname+"&lang="+language+"&loadType=getDeleteButton&privDelete="+privDelete+"";
                ajaxOpname(url,data,"POST","#deleteButtonPlace","getDeleteButton");
            }
            
            function getSaveButton(){
                var url = ""+base+"/AjaxOpname";
                var command = "<%= Command.LIST%>";
                var idOpname = $('#hidden_opname_id').val();
                var privAdd = "<%= privAdd%>";
                var data="command="+command+"&idOpname="+idOpname+"&lang="+language+"&loadType=getSaveButton&privAdd="+privAdd+"";
                ajaxOpname(url,data,"POST","#saveButtonPlace","getSaveButton");               
            }
            
            function deleteOpname(){
                $('#deleteOpname').click(function(){                  
                    $('#deleteOpnameConfirmation').modal('show');
                });
            }
            
            function btnDeleteOpnameConfirmation(){
                $('#btnDeleteOpnameConfirmation').click(function(){
                    $(this).attr('disabled','true').html("<%=textListState[SESS_LANGUAGE][0]%>");
                    var idOpname = $('#hidden_opname_id').val();
                    var url = ""+base+"/AjaxOpname";
                    var command = "<%= Command.DELETE%>";
                    var data="command="+command+"&idOpname="+idOpname+"&loadType=deleteOpname&userName=<%=userName%>&userId=<%=userId%>";
                    ajaxOpname(url,data,"POST","","deleteOpname"); 
                });
            }
            
            function getMatItemBySKU(sku,oidOpname){
                var url = ""+base+"/AjaxOpname";
                var command = "<%= Command.LIST%>";              
                var data = "loadType=getMatItemBySKU&command=<%=Command.LIST%>&sku="+sku+"&oidOpname="+oidOpname+"&lang="+language+"";
                ajaxOpname(url,data,"POST","","getMatItemBySKU");
            }    
            
            function getAutoComplete(){
                
                $('#skuInput').typeahead({
                    select :function(item){
                        alert('test');
                    }, 
                    updater: function (item) {                     
                        return item.value;
                    },
                    onSelect: function(item) {
                        var tempData = item.value;
                        var temp = tempData.split("=");
                        
                        $('#matOidInput').val(temp[0]);
                        $('#skuInput').val(temp[2]);
                        $('#materialInput').val(temp[1]);
                        $('#unitOidInput').val(temp[3]);
                        $('#unitInput').val(temp[4]);
                        setTimeout(function(){
                            $('#qtyInput').focus();
                        }, 400);
                        
                       
                        
                    },
                    ajax: {
                        url: ""+base+"/AjaxOpname",
                        timeout: 500,
                        displayField: "data_value",
                        valueField: "data_key",  
                        method: "get",
                        alignWidth: false,
                        menu: '<ul class="typeahead dropdown-menu" style="width:auto"></ul>',
                        item: '<li class="test"><a href="#"></a></li>',
                        loadingClass: "loading-circle", 
                        preDispatch: function (query) { 
                            var idOpname = $('#hidden_opname_id').val();
                            return {
                                search: query,
                                oidopname :idOpname,
                                command :<%=Command.SEARCH%>  
                            }
                        },
                        preProcess: function (data) {
                            if (data.success === false) {
                                // Hide the list, there was some error
                                return false;
                            }
                           
                            return data;
                            
                        }
                                             
                    }
                });
            }
            
            function btnHistory(){
                $('#btnHistory').click(function(){
                    var url = ""+base+"/AjaxOpname";
                    var command = "<%= Command.LIST%>";
                    var idOpname = $('#hidden_opname_id').val();
                    var data="command="+command+"&oidDocHistory="+idOpname+"&loadType=getListHistory&lang="+language+"";
                    ajaxOpname(url,data,"POST","#hitoryDynamic","getHistoryOpname");
                });
            }
            
            function getPrintButton(){
                var url = ""+base+"/AjaxOpname";
                var command = "<%= Command.LIST%>";
                var idOpname = $('#hidden_opname_id').val();
                var data="command="+command+"&idOpname="+idOpname+"&lang="+language+"&loadType=getPrintButton";
                ajaxOpname(url,data,"POST","#btnHistoryPlace","getPrintButton");               
            }
            
            function printButtonAction(){
                $('#printReport').click(function(){
                    var idOpname = $('#hidden_opname_id').val();
                    window.open("mat_opname_print_form.jsp?hidden_opname_id="+idOpname+"&command=<%=Command.EDIT%>","pireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                });
            }
            
            //KEYBOARD BELLOW
            function keyboardAdd(){
                $('#skuInput').keydown(function(e){
                    if (e.keyCode==13){
                        var value = $(this).val();
                        if (value.length>0){
                            var oidOpname = $('#hidden_opname_id').val();
                            getMatItemBySKU(value,oidOpname);
                        }
                    }else if (e.keyCode==113){
                        $('.loadListItem').trigger('click');
                    }else if (e.keyCode==38){
                        $('.listOpname .firstFocus').focus();
                        $('.listOpname .lastFocus').focus();
                    }else if (e.keyCode==39){
                        $('#materialInput').focus();
                    }
                });
                
                $('#materialInput').keydown(function(e){
                    if (e.keyCode==13){
                        $('#qtyInput').focus();
                    }else if (e.keyCode==113){
                        $('.loadListItem').trigger('click');
                    }else if (e.keyCode==39){
                        $('#qtyInput').focus();
                    }else if (e.keyCode==37){
                        $('#skuInput').focus();
                    }
                });
                
                $('#qtyInput').keydown(function(e){
                    if (e.keyCode==13){
                        $('.saveEditItem').focus();
                        $('.saveItem').focus();
                        
                    }else if (e.keyCode==39){
                        $('.saveItem').focus();
                    }else if (e.keyCode==37){
                        $('#materialInput').focus();
                    }
                });
                
                $('.saveItem').keydown(function(e){
                    if (e.keyCode==39){
                        $('#skuInput').focus();
                    }else if (e.keyCode==37){
                        $('#qtyInput').focus();
                    }
                });
            }
            
            function textual(){
                $('.textual').keydown(function(e){
                    var id = $(this).attr('id');
                    var temp = id.split("-");
                    var ids  = temp[1];
                    if (e.keyCode==40){
                        if ($(this).hasClass('lastFocus')){
                            $('#modalReport #sku').focus();
                        }else{
                            var idNext = parseInt(ids)+1;
                            $('#modalReport #textual-'+idNext+'').focus();
                        }
                    }else if (e.keyCode==38){  
                        if ($(this).hasClass('firstFocus')){
                            $('#modalReport .searchModal').focus();
                        }else{
                            var idPrev = parseInt(ids)-1;
                            $('#modalReport #textual-'+idPrev+'').focus();
                        }
                        
                    }else if (e.keyCode==13){
                        $(this).trigger('click');
                    }
                    
                });
                
                
                
                $('#modalReport #sku').keydown(function(e){
                    if (e.keyCode==13){
                        $('#modalReport .searchModal').focus();
                    }else if (e.keyCode==40){
                        $('#modalReport #namaBarang').focus();
                    }
                });
                
                $('#modalReport #namaBarang').keydown(function(e){
                    if (e.keyCode==13){
                        $('#modalReport .searchModal').focus();
                    }else if (e.keyCode==40){
                        $('#modalReport .searchModal').focus();
                    }else if (e.keyCode==38){
                        $('#modalReport #sku').focus();
                    }
                });
                
                $('#modalReport .searchModal').keydown(function(e){
                    if (e.keyCode==40){
                        $('#modalReport .firstFocus').focus();
                    }else if (e.keyCode==38){
                        $('#modalReport #namaBarang').focus();
                    }
                });
            }
            
            function tableControl(){
                $('.table-control').keydown(function(e){
                    if (e.keyCode==40){
                        $(this).parent().parent().next('tr').find('input').focus();
                    }else if (e.keyCode==38){
                        $(this).parent().parent().prev('tr').find('input').focus();
                    }else if (e.keyCode==69){
                        var ids = $(this).attr('id');
                        var temp = ids.split("-");
                        var id = temp[1];
                        $('#'+id+'').trigger('click');
                    }else if (e.keyCode==68){
                        var ids = $(this).attr('id');
                        var temp = ids.split("-");
                        var id = temp[1];
                        $('#del-'+id+'').trigger('click');
                    }
                });
            }
                    
            $(".modal").on('shown.bs.modal', function() {
                var id = $(this).attr('id');
                if (id=="modalReport"){
                    setTimeout(function(){
                         $('#modalReport #sku').focus();
                    }, 400);
                }else if (id=="deleteOrderConfirmation"){
                    setTimeout(function(){
                         $('#deleteOrderConfirmation #btnDeleteItemConfirmation').focus();
                    }, 400);
                }else{
                    $(this).find('button:visible:first').focus();
                    $(this).find('input:text:visible:first').focus();
                }
                
            });
            
            $('.modal').on('hidden.bs.modal', function () {
                var id = $(this).attr('id');
                setTimeout(function(){
                    $('#skuInput').focus();
                }, 400);
            });
            
            $('#deleteOrderConfirmation button').keydown(function(e){
                var id = $(this).attr('id');
                if (e.keyCode==39){
                    if (id=="btnDeleteItemConfirmation"){
                        $('#btnDeleteItemClose').focus();
                    }else if (id=="btnDeleteItemClose"){
                        $('#btnDeleteItemConfirmation').focus();
                    }
                }else if (e.keyCode==37){
                    if (id=="btnDeleteItemConfirmation"){
                        $('#btnDeleteItemClose').focus();
                    }else if (id=="btnDeleteItemClose"){
                        $('#btnDeleteItemConfirmation').focus();
                    }
                }
                
            });
            
            //
            getPrintButton();
            btnHistory();
            getSaveButton();
            getDeleteButton();
            getListControlStatus();
            getListControlCategory();
            getListControlSuplier();
            getListOpname();        
            searchModal();
            btnDeleteItemConfirmation();
            btnDeleteOpnameConfirmation();
            $('[data-toggle="tooltip"]').tooltip(); 

            $('#datetime').daterangepicker({
                "showDropdowns": true,
                "singleDatePicker": true,
                "timePicker": true,
                "timePicker24Hour": true,
                "timePickerSeconds": true,
                "autoApply": true,
                "dateLimit": {
                    "days": 7
                },
                "locale": {
                    "format": "MM/DD/YYYY HH:mm:ss",
                    "separator": "/",
                    "applyLabel": "Use",
                    "cancelLabel": "Cancel",
                    "fromLabel": "From",
                    "toLabel": "To",
                    "customRangeLabel": "Custom",
                    
                    "firstDay": 1
                },
                "autoUpdateInput": true
    
            }, function(start, end, label) {
              console.log('New date range selected: ' + start.format('YYYY-MM-DD hh:mm:ss') + ' to ' + end.format('YYYY-MM-DD hh:mm:ss') + ' (predefined range: ' + label + ')');
            });
            
            $('#btnBack').click(function(){
                window.history.back();
            });
            
        });     
    </script>
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
    <%if(menuUsed == MENU_PER_TRANS){%>
    <tr>
        <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
            <%@ include file = "../../../main/header.jsp" %>
        </td>
    </tr>
    <tr>
        <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
            <%@ include file = "../../../main/mnmain.jsp" %>
        </td>
    </tr>
    <%}else{%>
    <tr bgcolor="#FFFFFF">
        <td height="10" ID="MAINMENU">
            <%@include file="../../../styletemplate/template_header.jsp" %>
        </td>
    </tr>
    <%}%>
    <tr> 
        <td valign="top" align="left" style="background-color:beige"> 
            <form id="frmMatOpname" name="frm_matopname" method="post" action="">
                <input type="hidden" id="command" name="command" value="">
                <input type="hidden" id="statusNow" name="statusNow" value="none">
                <input type="hidden" id="editableList" name="editableList" value="0">
                <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                <input type="hidden" name="start_item" value="<%=startItem%>">
                <input type="hidden" name="command_item" value="<%=cmdItem%>">
                <input type="hidden" name="approval_command" value="<%=appCommand%>">
                <input type="hidden" id="hidden_opname_id" name="hidden_opname_id" value="<%=oidStockOpname%>">
                <input type="hidden" name="hidden_opname_item_id" value="">
                <input type="hidden" name="hidden_location_id" value="<%=oidLocation %>">
                <input type="hidden" name="type_doc" value="1">
                <input type="hidden" name="typeharian" value="<%=typeharian%>">
                <input type="hidden" id="stokOpnameNumber" name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_NUMBER]%>" value="<%=so.getStockOpnameNumber()%>">
                <input type="hidden" name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_SUB_CATEGORY_ID]%>" value="<%=so.getSubCategoryId()%>">
                <input type="hidden" id="opnameDate" name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_DATE]%>" value="<%=dateOpnames%>">
                <input type="hidden" id="opnameTime" name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_TIME]%>" value="<%=timeOpnames%>">
                <br>
                <div class="row" style="margin-right:0px; margin-left:0px;">
                    <div class="col-md-12">
                        <ol class="breadcrumb" style="border: thin solid rgb(221, 221, 221)">
                            <li><%=textListGlobal[SESS_LANGUAGE][0]%></li>
                            <li><%=textListGlobal[SESS_LANGUAGE][1]%></li>
                            <li class="active"><%=textListGlobal[SESS_LANGUAGE][4]%></li>
                        </ol>
                    </div>
                </div>
                
                <div class="row" style="margin-right:0px; margin-left:0px;">
                <div class="col-md-12">                  
                    <div class="panel panel-default">
                        <div class="panel-heading" id="oidOpnamePlace">
                            &nbsp;
                            <b>   
                                                        
                                <%
                                if((so.getStockOpnameNumber()).length() > 0 && iErrCode==0) {
                                    out.println(so.getStockOpnameNumber());
                                }else{
                                    out.println("");
                                }
                                %>

                            </b>
                        </div>
                        <div class="panel-body" id="dynamiontrol">                           
                            <div class="col-md-4">
                                <div class="row">
                                    <div class="col-md-3"><label><%=textListOrderHeader[SESS_LANGUAGE][1]%></label></div>
                                    <div class="col-md-9">
                                        <%    
                                        Vector val_locationid = new Vector(1,1);
                                        Vector key_locationid = new Vector(1,1);
                                        String whereClause = "";
                                        Vector vt_loc = PstLocation.list(0,0,whereClause, PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                        for(int d=0;d<vt_loc.size();d++){
                                            Location loc = (Location)vt_loc.get(d);
                                            val_locationid.add(""+loc.getOID()+"");
                                            key_locationid.add(loc.getName());
                                        }                             
                                        String select_locationid = ""+so.getLocationId();                            
                                        %>
                                        <%=ControlCombo.draw(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "form-control")%>
                                    </div>
                                </div>
                                <div class="row" style="margin-top:5px;">   
                                    <div class="col-md-3"><label><%=textListOrderHeader[SESS_LANGUAGE][2]%></label></div>
                                    <div class="col-md-9">
                                        <div class="input-group"> 
                                            <input type="text" id="datetime" value="<%= dateTimes%>"  class="form-control" aria-describedby="sizing-addon1">
                                            <span class="input-group-addon" id="sizing-addon1"><i class="glyphicon glyphicon-calendar"></i></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="row" style="margin-top:5px;">
                                    <div class="col-md-3"><label><%=textListOrderHeader[SESS_LANGUAGE][5]%></label></div>
                                    <div class="col-md-9" id="suplierPlace">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="row">
                                    <div class="col-md-3"><label><%=textListOrderHeader[SESS_LANGUAGE][6]%></label> </div>
                                    <div class="col-md-9" id="categoryPlace">                                        
                                    </div>
                                </div>
                                <div class="row" style="margin-top:5px;">
                                    <div class="col-md-3"><label><%=textListOrderHeader[SESS_LANGUAGE][4]%></label></div>
                                    <div class="col-md-9" id="statusPlace">                                        
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="row">
                                    <div class="col-md-3"><label><%=textListOrderHeader[SESS_LANGUAGE][8]%></label> </div>
                                    <div class="col-md-9">
                                        <textarea name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_REMARK]%>" class="form-control" wrap="VIRTUAL" rows="4"><%=so.getRemark()%></textarea>
                                    </div>
                                </div>
                                <div class="row" style="margin-top:5px;">
                                    <div class="col-md-3">
                                        &nbsp;
                                    </div>
                                    
                                    <div class="col-md-9" >
                                        <span id="saveButtonPlace">                               
                                        </span>
                                        <span id="deleteButtonPlace">                                           
                                        </span>
                                    </div>
                                    
                                </div>
                            </div>                                     
                        </div>
                    </div>
                </div>
            </div>
            </form>
            <div class="row" style="margin-right:0px; margin-left:0px;">
                <div class="col-md-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">&nbsp;</div>
                        <a href="#" data-toggle="tooltip" id="myTooltip" title="This is default title"></a>
                        <div class="panel-body">
                            <form name='formAdd' id='formAdd'>
                                <div id="dynamicContainer"></div>  
                            </form>
                        </div>
                    </div>
                </div>
            </div> 
            <div class="row" style="margin-right:0px; margin-left:0px;">
                <div class="col-md-12" style="margin-bottom:10px;">
                    <button style="margin-right: 5px;" class="btn btn-default" type="button" id="btnBack">
                        <i class="glyphicon glyphicon-chevron-left"></i> <%= textListOrderItem[SESS_LANGUAGE][8]%>
                    </button>
                    <span id ="btnHistoryPlace"></span>
                    
                    <button style="margin-right: 5px;" class="btn btn-success pull-right" type="button" id="btnHistory">
                        <i class="glyphicon glyphicon-list-alt"></i> <%= textListGlobal[SESS_LANGUAGE][13]%>
                    </button>
                    
                </div>
            </div>  
        </td>
        
    </tr>
    <tr>
        
    </tr>
    </table> 
    
    
</body>
<div id="modalReport" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modal-title"><%=textListGlobal[SESS_LANGUAGE][12]%></h4>
                
            </div>
            <div class="modal-body" id="modal-body">
                <div id="modal-body-up">
                    <div class="row" style="margin-bottom:3px;margin-top:4px;">                        
                        <div class="col-md-3"><%= textListOrderItem[SESS_LANGUAGE][1]%></div>
                        <div class="col-md-9"><input id="sku" type='text' class='form-control' placeholder='<%= textListOrderItem[SESS_LANGUAGE][1]%>'></div>
                    </div>
                    
                    <div class="row" style="margin-bottom:3px;">
                        <div class="col-md-3"><%=textListOrderItem[SESS_LANGUAGE][2]%></div>
                        <div class="col-md-9"><input id="namaBarang" type='text' class='form-control' placeholder='<%=textListOrderItem[SESS_LANGUAGE][2]%>'></div>
                    </div>
                    <div class="row" style="margin-bottom:3px;">
                        <div class="col-md-12">
         
                            <button style="margin-left:5px;" type="button" data-searchcategory="2"  data-toggle="tooltip" title="Cari Item barang belum diopname pada periode Opname ini" class="btn btn-default pull-right searchModal">
                                <i class="glyphicon glyphicon-repeat"></i> 
                            </button>
                            <button style="margin-left:5px;" type="button" data-searchcategory="1"  data-toggle="tooltip" title="Cari Item Stok >0 & periode Opname barang belum diopname" class="btn btn-default pull-right searchModal">
                                <i class="glyphicon glyphicon-zoom-in"></i> 
                            </button>
                            
                            <button style="margin-left:5px;" data-searchcategory="0" type="button" data-toggle="tooltip" title="<%= textListGlobal[SESS_LANGUAGE][2]%>" class="btn btn-default pull-right searchModal">
                                <i class="glyphicon glyphicon-search"></i> 
                            </button>
                        </div>
                        
                    </div>
                </div>
                <div id="modal-body-low"></div>
            </div>
            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn btn-danger">Close</button>
            </div>
        </div>
    </div>
</div>
<!-- DELETE ITEM CONFIRMATION-->
<div id="deleteOrderConfirmation" class="modal nonprint">
    <div class="modal-dialog modal-sm" style="max-width:300px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="AUTHORIZE_TITLE"><b><%=textListState[SESS_LANGUAGE][1]%></b></h4>
            </div>
            <div class="modal-body">
                <%=textDelete[SESS_LANGUAGE][0]%>
            </div>
            <div class="modal-footer">               
              <button id="btnDeleteItemConfirmation" class="btn btn-primary" type="button" data-dismiss="modal"><i class="glyphicon glyphicon-ok"></i> <%=textListState[SESS_LANGUAGE][2]%></button>
              <button id="btnDeleteItemClose" class="btn btn-danger" type="button" data-dismiss="modal"><i class="glyphicon glyphicon-remove"></i> <%=textListState[SESS_LANGUAGE][3]%></button>
            </div>
        </div>
    </div>
</div>
<!-- DELETE OPNAME CONFIRMATION-->
<div id="deleteOpnameConfirmation" class="modal nonprint">
    <div class="modal-dialog modal-sm" style="max-width:300px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="AUTHORIZE_TITLE"><b><%=textListState[SESS_LANGUAGE][1]%></b></h4>
            </div>
            <div class="modal-body">
                <%=textDelete[SESS_LANGUAGE][1]%>
            </div>
            <div class="modal-footer">               
              <button id="btnDeleteOpnameConfirmation" class="btn btn-primary" type="button" data-dismiss="modal"><i class="glyphicon glyphicon-ok"></i> <%=textListState[SESS_LANGUAGE][2]%></button>
              <button class="btn btn-danger" type="button" data-dismiss="modal"><i class="glyphicon glyphicon-remove"></i> <%=textListState[SESS_LANGUAGE][3]%></button>
            </div>
        </div>
    </div>
</div>
<div id="viewHistory" class="modal nonprint">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="AUTHORIZE_TITLE"><b><%=textListOrderHeader[SESS_LANGUAGE][10]%></b></h4>
            </div>
            <div class="modal-body" >
                <div class="panel panel-default">
                    <div class="panel-heading">&nbsp;</div>
                    <div class="panel-body" id="hitoryDynamic">
                        
                    </div>
                </div>
            </div>
            <div class="modal-footer">                            
              <button type="button" data-dismiss="modal" class="btn btn-danger"><%=textListGlobal[SESS_LANGUAGE][10]%></button>
            </div>
        </div>
    </div>
</div>          
</html>



