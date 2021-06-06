<%-- 
    Document   : add_marketing
    Created on : Nov 7, 2019, 4:05:53 PM
    Author     : Sunima
--%>

<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.marketing.PstMarketingManagement"%>
<%@page import="com.dimata.posbo.entity.marketing.MarketingManagement"%>
<%@page import="com.dimata.util.Command"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../main/javainit.jsp" %>

<%
    int iCommand = FRMQueryString.requestCommand(request);
    long oid = FRMQueryString.requestLong(request, "oid");
    // get data first
    String marketingTitle = "";
    String marketingDescription = "";
    Date marketingStartDate = new Date(), createDate = new Date(), checkedDate = new Date(), editedDate =  new Date();
    Date marketingEndDate = new Date();
    int marketingType = 0;
    int marketingStatus = 0;
    String selected = "", selected1 = "", selected2 = "";
    String selectStatus1 = "", selectStatus2 = "", selectStatus3 = "", marketingNote = "";
    
    if(oid != 0){
        Vector listform = PstMarketingManagement.list(0, 0, PstMarketingManagement.fieldNames[PstMarketingManagement.FLD_MARKETING_MANAGEMENT_ID]+"="+oid , "");
       MarketingManagement entMarketingManagement = new MarketingManagement();
       entMarketingManagement = PstMarketingManagement.fetchExc(oid);
       
       marketingTitle = entMarketingManagement.getMarketing_title(); 
       marketingDescription = entMarketingManagement.getMarketing_description();
       marketingStartDate = entMarketingManagement.getStart_date();
       marketingEndDate = entMarketingManagement.getEnd_date();
       marketingStatus = entMarketingManagement.getMarketing_status();
       createDate = entMarketingManagement.getCreate_date();
       marketingNote = entMarketingManagement.getMarketing_note();
       checkedDate = entMarketingManagement.getChecked_date();
       editedDate = entMarketingManagement.getEdited_date();
       
       // selected type
       if(marketingType ==1){
          selected = "selected";
       }else if(marketingType == 2){
           selected1 = "selected";
       }else if(marketingType == 3){
           selected2 = "selected";
       }
       // selected Status
       if(marketingStatus == 0){
           selectStatus1 = "selected";
       }else if(marketingStatus == 1){
           selectStatus2 = "selected";
       }else if(marketingStatus == 2){
           selectStatus3 = "selected";
       }
       
   }
    
    

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Marketing</title>
        
         <link rel="stylesheet" type="text/css" href="../styles/bootstrap/css/bootstrap.min.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/bootstrap/css/bootstrap-theme.min.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/jquery-ui-1.12.1/jquery-ui.min.css"/>
                 <link rel="stylesheet" type="text/css" href="../styles/dist/css/AdminLTE.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/dist/css/skins/_all-skins.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/font-awesome/font-awesome.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/iCheck/flat/blue.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/iCheck/all.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/select2/css/select2.min.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
                <link rel="stylesheet" type="text/css" href="../styles/plugin/datatables/dataTables.bootstrap.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/bootstrap-notify.css"/>
                <link rel="stylesheet" type="text/css" href="../styles/JavaScript-autoComplete-master/auto-complete.css"/>
                <style>
                    
                    .row1{
                        margin-top: 10px;
                    }
                    .preview{
                        width: 100px;
                        height: 100px;
                        display: grid;
                        justify-content: center;
                        align-items: center;
                        margin: 0 auto;
                        background: #eee;
                        border-radius: 6px;
                    }
                    .card-form {
    box-shadow: 0px 0px 20px #eee;
    border-radius: 6px;
    padding: 20px;
}
button#save-form {
    background: #3dda99;
    border: none;
    margin-top: 40px;
}
.btn-upload {
    background: #0400ff;
    color: #fff;
}
.col-md-9.main-bar {
    border-right: 1px solid #eee;
}
div#side-bar {
    padding-left: 30px;
}
h4.title {
    margin-top: 50px;
    margin-bottom: 20px;
    font-weight: bold;
}
              .modal-content {
    border-radius: 10px;
    width: 54%;
    margin: 30% auto;
}    
button#btn-add-promotion {
    background: #3dda99;
    color: #fff;
}
#title-status{
   color:#ffb100;
}

button#btn-preview {
    display: inline-block;
    margin-top: 38px;
}
#btn-delete{
    display: inline-block;
    margin-top: 38px;
    background: #ff5f5f;
    color: #fff;
}
.btn-delete-modal {
    background: #ff5f5f;
    color: #fff;
}
h4.modal_title_preview {
    font-weight: bold;
    text-align: center;
    font-size: 20px;
}
.image_preview {
    height: 200px;
}
.desc_preview {
    margin-top: 20px;
    padding-bottom: 10px;
    border-bottom: 1px solid #e4d2d2;
    margin-bottom: 20px;
}
.date_preview {
    font-weight: bold;
}
.image-content{
    width:100%;
    height: 100%;
    object-fit:cover
}
img.image-content-preview {
    width: 100%;
    object-fit: cover;
    overflow: hidden;
    height: 200px;
}
                </style>
    </head>
    <body>
         <script type="text/javascript" src="../styles/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../styles/jquery-ui-1.12.1/jquery-ui.min.js"></script>
    <script type="text/javascript" src="../styles/bootstrap/js/bootstrap.min.js"></script>  
    <script type="text/javascript" src="../styles/dimata-app.js"></script>
    <script type="text/javascript" src="../styles/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="../styles/JavaScript-autoComplete-master/auto-complete.min.js"></script>
    <script type="text/javascript" src="../styles/iCheck/icheck.min.js"></script>
    <script type="text/javascript" src="../styles/plugin/datatables/jquery.dataTables.js"></script>
    <script type="text/javascript" src="../styles/plugin/datatables/dataTables.bootstrap.js"></script>
    <script type="text/javascript" src="../styles/bootstrap-notify.js"></script>
    <script type="text/javascript" src="../styles/select2/js/select2.min.js"></script>
    
        <div class="container">            
            <h4 class="title">Tambah Baru</h4>
            <div class="card-form"> 
                <div class="row">
                    
            <form class="form1" id="main-form">
             <div class="col-md-9 main-bar">
                    <div class="row row1">
                        <div class="col-md-3">
                            <label>Title</label>
                        </div>
                        <div class="col-md-9">
                                <input type="text" name="FRM_MARKETING_MANAGEMENT_TITLE" class="form-control" value="<%=marketingTitle%>" style="width: 100%;"/>
                        </div>
                    </div>
                    
                    <div class="row row1">
                        <div class="col-md-3">
                            <label>Description</label>
                        </div>
                        <div class="col-md-9">
                            <textarea name="FRM_MARKETING_MANAGEMENT_DESCRIPTION" class="form-control" rowpsan="4"><%=marketingDescription%></textarea>
                        </div>
                    </div>
                    <div class="row row1">
                        <div class="col-md-3">
                            <label>File</label>
                        </div>
                        <div class="col-md-3 prevImage">
                            <div class="preview">
                                <i class="fa fa-search"></i>
                               
                            </div>
                        </div>
                          <div class="col-md-4">
                              <input type="file" id='filename' name='filename' class="form-control"/>
                              <input type='hidden' name='basename' id='basename' value=''>
                              <input type="hidden" class="real_path"name="FRM_FIELD_REAL_PATH" value="<%= request.getRealPath("imgupload/marketing")%>">
                          </div>
                    </div>
                    <div class="row row1">
                        <div class="col-md-3">
                            <label>Tanggal Berlaku</label>
                        </div>
                        <div class="col-md-9">
                            <div class="row">
                                <div class="col-md-3">
                                        <span>Dari</span>
                                         <div class="input-group">
                                    <span class="input-group-addon" id="sizing-addon2"><i class="fa fa-calendar"></i></span>
                                    <input type="date" name="FRM_MARKETING_MANAGEMENT_START_DATE" class="form-control" placeholder="03/06/00" aria-describedby="sizing-addon2" value="<%=Formater.formatDate(marketingStartDate, "yyyy-MM-dd")%>">
</div>
                                </div>
                                <div class="col-md-3" style="margin-left: 6em;">
                                        <span>Sampai</span>
                                         <div class="input-group">
                                    <span class="input-group-addon" id="sizing-addon2"><i class="fa fa-calendar"></i></span>
                                    <input type="date" name="FRM_MARKETING_MANAGEMENT_END_DATE" class="form-control" placeholder="03/06/00" aria-describedby="sizing-addon2" value="<%=Formater.formatDate(marketingEndDate, "yyyy-MM-dd")%>">
</div>
                                </div>
                            </div>
                            
                        </div>
                    </div>
            </div>
            <div class="col-md-3" id="side-bar">
                <div class="row">
                    <label>Status : </label>
                <select class="form-control" id="main-status" name="FRM_MARKETING_MANAGEMENT_STATUS">
                    <option <%=selectStatus1%> value="0" style="color:#ffb100; font-weight:bold">Draft</option>
                    <option <%=selectStatus2%> value="1" style="color:#ff5656; font-weight:bold">Cancel</option>
                    <option <%=selectStatus3%> value="2" style="color:#21d087; font-weight:bold">Approve</option>
                </select>
                <br/>
                </div>
                
                <div class="row create">
                    <label>Created : </label>
                    <input type="text" class="form-control" value="<%=Formater.formatDate(createDate, "dd-MM-yyyy")%>" disabled="true"/>
                </div>
                <br/>
                <div class="row checked">
                    <label>Checked : </label>
                    <input type="text" class="form-control" value="<%=Formater.formatDate(checkedDate, "dd-MM-yyyy")%>" disabled="true"/>
                </div>
                <br/>
                <div class="row edited">
                    <label>Edited : </label>
                    <input type="text" class="form-control" value="<%=Formater.formatDate(editedDate, "dd-MM-yyyy")%>" disabled="true"/>
                </div>
                <br/>
                <div class="row note">
                    <label>Note : </label>
                    <textarea class="form-control" rowspan="5" name="FRM_MARKETING_MANAGEMENT_NOTE"><%=marketingNote%></textarea>
                </div>
                
                <br/>
            </div>
             </form>
            
                </div>
                <div class="row">
                    <button id="save-form" class="btn">Simpan</button>
                    <button id="btn-preview" class="btn">Preview</button>
                    <button id="btn-delete" class="btn">Delete</button>
                </div>
            </div>
            
                 
        </div>
        
        <!-- launc an modal here -->
       <!-- Modal -->
<div class="modal fade text-center" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-body">
          <h2 id="title-modal">Berhasil Ditambahkan</h2>
          <h4>Status : <span id="title-status">Draft</span></h4>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn" id="btn-add-promotion">Tambah Baru</button>
        <button type="button" class="btn" id="btn-go-dashboard">Dashboard</button>
      </div>
    </div>
  </div>
</div>
       
       <!-- modal delete -->
       <div class="modal fade bs-example-modal-sm" id="modal_delete" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
  <div class="modal-dialog modal-md" role="document">
    <div class="modal-content">
        <div class="modal-body">
        <h4>Are you sure want to Delete ?</h4>
        </div>
        <div class="modal-footer">
            <button class="btn btn-delete-modal">Yes, Delete</button>
        </div>
    </div>
  </div>
</div>
       
       <!-- modal preview --->
       <div class="modal fade" id="modal_preview" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Preview</h4>
      </div>
      <div class="modal-body">
          <div class="row">
              <div class="col-md-12">
                 
                  <div class="image_preview"></div>
                   <h4 class="modal_title_preview"><%=marketingTitle%></h4>
                  <div class="desc_preview"><%=marketingDescription%></div>
                  <div class="date_preview">
                      <div class="row">
                          <div class="col-md-6">
                              Berakhir pada tanggal
                          </div>
                          <div class="col-md-4">
                              <input type="date" readonly value="<%=Formater.formatDate(marketingEndDate, "yyyy-MM-dd")%>">
                          </div>
                      </div>
                  </div>
              </div>
          </div>
      </div>
      <div class="modal-footer">
        
      </div>
    </div>
  </div>
</div>
       
        <script>
            $(document).ready(function(){

                $("#main-status").on("change", function(){
                    var status = $(this).val();
                    if(status == 0){
                        $(this).css({"color" : "#ffb100", "font-weight":"bold"});
                        $("#title-status").text("Draft").css("color","#ffb100");
                    }if(status == 1){
                        $(this).css({"color" : "#ff5656", "font-weight":"bold"});
                        $("#title-status").text("Cancel").css("color","#ff5656");
                    }if(status == 2){
                        $(this).css({"color" : "#21d087", "font-weight":"bold"});
                        $("#title-status").text("Approve").css("color","#21d087");
                    }
                });   
                $(".prevImage").hide();
                $("#btn-preview").hide();
                $("#btn-delete").hide();
                
                validation();
                function validation(){
                    if(<%=iCommand%> == <%=Command.ADD%>){
                        $("#side-bar, .prevImage").hide();
                        $(".main-bar").css({"width" : "100%"});
                        $("#save-form").text("Tambah");
                    }
                    if(<%=iCommand%> == <%=Command.EDIT%>){
                        $("#side-bar").show();
                        $(".edited, .note").hide(); 
                        $(".prevImage").show();
                        $("#title-modal").text("Berhasil Disimpan");
                        $(".title").text("Edit");
                        loadImage();    
                    }
                    if(<%=iCommand%> == <%=Command.VIEW%>){
                        $(".create, .checked").hide();
                        $(".prevImage, #btn-add-promotion").show();
                        $("#title-modal").text("Berhasil Disimpan");
                        $(".title").text("Check");
                        $("#filename").hide();
                        $("#btn-preview").show();    
                        loadImage();                        
                    }
                    if(<%=iCommand%> == <%=Command.DELETE%>){
                        $("#save-form").hide();
                        $("#btn-delete").show();
                        loadImage();
                        $("#filename").hide();
                        $(".prevImage").show();
                    }
                }
                function loadImage(){
                    var imageUrl = "<%=approot+"/imgupload/marketing/"+oid+".jpg"%>";
                   $(".preview").prepend().html("<img class='image-content' src='"+imageUrl+"'/>");
                   $(".image_preview").html("<img class='image-content-preview' src='"+imageUrl+"'/>");
                }
                
                $("#btn-add-promotion").click(function(){
                    window.location='add_brosur.jsp?command=<%=Command.ADD%>';
                });
                
                $("#btn-go-dashboard").click(function(){
                   window.location='marketing_brosur.jsp';
                });    
                
                $("#btn-delete").click(function(){
                   $("#modal_delete").modal('show');
                });
                
                $("#btn-preview").click(function(){
                   $("#modal_preview").modal('show');
                });
                
                 var base = "<%=approot%>";
                
                $("#save-form").click(function(){
                    var mainForm = $("#main-form").serialize();
                    var url = ""+base+"/AjaxMarketing"; 
                    var comandView = 0;
                    if(<%=iCommand%> == <%=Command.VIEW%>){
                        comandView = <%=Command.VIEW%>;
                    }
                    
                    var data = "command=<%=Command.SAVE%>&FRM_FIELD_DATA_FOR=saveadd&OID=<%=oid%>&CMD_VIEW="+comandView+"&"+mainForm;
                    
                    $.ajax({
                        url : ""+url+"",
                        data : ""+data+"",
                        type : "POST",
                        dataType : "json",
                        async : false,
                        cache : false,
                        success: function (data) {
                        
                    },
                    error: function (data) {
                        
                    }
                    
                    }).done(function(data){
                        $("#myModal").modal('show');
                        loadImage();
                    });
                    
                    
                });
                
                $(".btn-delete-modal").click(function(){
                    var url = ""+base+"/AjaxMarketing";
                    
                    var data = "command=<%=Command.DELETE%>&FRM_FIELD_DATA_FOR=delete&OID=<%=oid%>";
                    
                    $.ajax({
                        url : ""+url+"",
                        data : ""+data+"",
                        type : "POST",
                        dataType : "json",
                        async : false,
                        cache : false,
                        success: function (data) {
                        
                    },
                    error: function (data) {
                        
                    }
                    
                    }).done(function(data){
                       window.location = "marketing_brosur.jsp";
                       
                    });
                });
                
                $("#filename").change(function(){
                    encodeImageFileAsURL();
                });
               
        function encodeImageFileAsURL() {
            var filesSelected = document.getElementById("filename").files;
            if (filesSelected.length > 0) {
              var fileToLoad = filesSelected[0];
              var fileReader = new FileReader();
              fileReader.onload = function(fileLoadedEvent) {
                var srcData = fileLoadedEvent.target.result; // <--- data: base64
                $("#basename").val(srcData);
              };
              fileReader.readAsDataURL(fileToLoad);

            }
        }
        
            });
           </script>
    </body>
</html>
