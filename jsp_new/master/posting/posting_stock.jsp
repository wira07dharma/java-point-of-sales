<%@ page import="com.dimata.qdep.form.FRMQueryString,
                 com.dimata.posbo.session.masterdata.SessPosting,
                 com.dimata.util.Command,
                 com.dimata.posbo.entity.warehouse.PstMatStockOpname,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.gui.jsp.ControlCombo"%>
<%@page contentType="text/html"%>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_POSTING); %>
<%@ include file = "../../main/checkuser.jsp" %>


<!-- JSP Block -->
<%!
public static final String textListTitleHeader[][] = {
    {"Stok","Tanggal Transaksi","Lokasi","Posting Stok","Keterangan",
             "Proses posting adalah proses tahap akhir dari proses semua transaksi untuk pengurangan atau penambahan stok,<br>dokumen yang akan di posting dalam proses ini adalah dokumen yang memiliki status <strong>FINAL</strong> dan bila proses berhasil otomatis akan<br>mengubah status dokumen menjadi <strong>CLOSED</strong> yang artinya dokumen sudah di proses dan tidak bisa di ubah atau di hapus",
             "Proses posting transaksi tidak dapat dilanjutkan, karena masih ada Dokumen Opname yang belum terposted"
    },
    {"Stock","Transaction Date","Location","Posting Stock","Description",
             "Posting proces is last process for all transaction document. This process is used to stock mutation (+/-). Just <strong>FINAL</strong> document status will be process. <br>If process sucess system automatically change document status to be <strong>CLOSED</strong>. It means document cannot be changed and deleted",
             ""
    }
};
%>

<%
int iCommand = FRMQueryString.requestCommand(request);
int finish = FRMQueryString.requestInt(request, "finish");
long TranLoc = FRMQueryString.requestLong(request, "TranLoc");
Date TranDate = FRMQueryString.requestDate(request, "TranDate");
int updateStock = FRMQueryString.requestInt(request, "update_stock");

/*
if (finish == 1) {
    // flag ini menentukan apakah daily posting ditentukan oleh tanggal posting ????
    boolean postedDateCheck = false;
    
    boolean result = SessPosting.ProcessStore(new Date(),TranLoc,postedDateCheck);
    if (result == true) {
        //Go to next page
        response.sendRedirect("posting_success.jsp");
    } else {
        response.sendRedirect("posting_fail.jsp");
    }
}
*/

/**
 * untuk pengecekan data di opname
 */
String msg = "";
boolean posting = false;
try {
    if(TranLoc!=0) {
        String where = PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_STOCK_OPNAME_STATUS]+
                "="+I_DocStatus.DOCUMENT_STATUS_DRAFT+
                " AND "+PstMatStockOpname.fieldNames[PstMatStockOpname.FLD_LOCATION_ID]+"="+TranLoc;
        
        Vector cList = PstMatStockOpname.list(0,0,where,"");
        if(cList!=null && cList.size()>0) {
            posting = false;
            msg = "Proses posting transaksi tidak dapat dilanjutkan, karena masih ada Dokumen Opname yang belum terposted";
        }
        else {
            posting = true; // Proses posting dapat dilanjutkan, karena tidak terdapat dokumen Opname yang berstatus DRAFT
        }
    }
    else {
        posting = false;
    }
} catch(Exception e) {
    System.out.println("Exc. : "+e.toString());
}

if(posting) {
    if (finish == 1) {
        // flag ini menentukan apakah daily posting ditentukan oleh tanggal posting atau tidak ????
        boolean postedDateCheck = false;
        
        Periode objPeriode = PstPeriode.getPeriodeRunning();
        
        SessPosting objSessPosting = new SessPosting();
        boolean bPostingSuccess = objSessPosting.postingTransDocument(new Date(), TranLoc, postedDateCheck);
        
        if (!bPostingSuccess) {
            response.sendRedirect("posting_success.jsp");
        } else {
            try {
                if( session.getValue("DAILY_POSTING") != null) {
                    session.removeValue("DAILY_POSTING");
                }
                
                Vector vSessionDate = new Vector(1,1);
                vSessionDate.add(objSessPosting.getVListUnPostedLGRDoc());
                vSessionDate.add(objSessPosting.getVListUnPostedReturnDoc());
                vSessionDate.add(objSessPosting.getVListUnPostedDFDoc());
                vSessionDate.add(objSessPosting.getVListUnPostedCostDoc());
                vSessionDate.add(objSessPosting.getVListUnPostedSalesDoc());
                
                session.putValue("DAILY_POSTING", vSessionDate);
            }catch(Exception e){
                System.out.println("Exc when manage SESSION : " + e.toString());
            }
            response.sendRedirect("posting_fail.jsp");
        }
    }
}

if(iCommand == Command.SAVE && updateStock == 1) {
    try {
        int result = com.dimata.posbo.entity.masterdata.PstMaterialStock.updateStockByCode();
    } catch(Exception e) {
        System.out.println(e.toString());
    }
    response.sendRedirect("posting_success.jsp");
}


/** get current date + one day */
java.util.Calendar objCalendar = Calendar.getInstance();

objCalendar.add(java.util.Calendar.DAY_OF_MONTH, 1); // add one day

java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd"); // use the pattern: day_of_month

String str = sdf.format(objCalendar.getTime()); // fromat the date to string

//out.println(str); // print it at the console
/** end */
%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdFinish() {
	document.frm_posting_store.command.value="<%=Command.SAVE%>";
	document.frm_posting_store.finish.value="1";
	document.frm_posting_store.action="posting_stock.jsp";
	document.frm_posting_store.submit();
}

function cmdBack() {
	document.frm_posting_store.command.value="<%=Command.NONE%>";
	document.frm_posting_store.action="../../homepage.jsp";
	document.frm_posting_store.submit();
}

function cmdUpdateStockByExcel() {
	document.frm_posting_store.command.value="<%=Command.SAVE%>";
	document.frm_posting_store.update_stock.value="1";
	document.frm_posting_store.action="posting_stock.jsp";
	document.frm_posting_store.submit();
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
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
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
      <%@ include file = "../../main/header.jsp" %>
      <!-- #EndEditable --></td> 
  </tr> 
  <tr> 
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td> 
  </tr>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            <%=textListTitleHeader[SESS_LANGUAGE][0]%> &gt; <%=textListTitleHeader[SESS_LANGUAGE][3]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frm_posting_store" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="finish" value="">
			  <input type="hidden" name="update_stock" value="">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td>
                    <hr size="1">
                  </td>
                </tr>
                <tr>
                  <td>
                    <table width="100%" border="0" cellspacing="1" cellpadding="0">
                      <tr ID="PRODUCT">
                        <td colspan="2">
                          <table width="100%" border="0" cellspacing="2" cellpadding="2">
                            <tr>
                              <td width="5%"> </td>
                              <td width="1%"></td>
                              <td width="94%"> </td>
                            </tr>
                            <tr>
                              <td colspan="3" height="20"><b>Aturan opname dan posting :</b></td>
                            </tr>
                            <tr>
                              <td colspan="3" height="20">1. Semua dokumen transaksi stock (penerimaan, sale, dsb.) harus sudah diposting sebelum tanggal opname</td>
                            </tr>
                            <tr>
                              <td colspan="3" height="20">2. Sebelum hari opname, Tanggal dan jam opname harus didaftarkan di sistem prochain dengan membuat
                                  <br> dokumen opname  status draft dengan mengisi dengan lengkap data-data dokumen opname tanpa opname item.
                              <br>Tujuannya : untuk menghindari dokumen transaksi setelah tgl dan jam opname bisa diposting sebelum
                                    dokumen opname real dientry ke sistem.</td>
                            </tr>
                            <tr>
                              <td colspan="3" height="20">3. Dokumen opname yang tidak dipakai harus dihapus dari sistem.</td>
                            </tr>

                            <tr>
                              <td colspan="3" height="20">4. Setelah dokumen opname dientry dan diposting maka dokumen transaksi lainnya baru bisa diposting.</td>
                            </tr>

                            <tr>
                              <td width="5%"> </td>
                              <td width="1%"></td>
                              <td width="94%"> </td>
                            </tr>

                            <tr>
                              <td colspan="3" height="20"><b><%=textListTitleHeader[SESS_LANGUAGE][4]%></b></td>
                            </tr>
                            <tr>
                              <td colspan="3" height="20"><%=textListTitleHeader[SESS_LANGUAGE][5]%><a href="javascript:cmdUpdateStockByExcel()">.</a><!--Ini link yang digunakan untuk melakukan triger update stock berdasarkan data dari excel--></td>
                            </tr>
                            <tr>
                              <td colspan="3" height="20"></td>
                            </tr>
                            <tr>
                              <td colspan="3" height="20">&nbsp;</td>
                            </tr>
                            <tr>
                              <td width="5%"><%=textListTitleHeader[SESS_LANGUAGE][2]%></td>
                              <td width="1%">:</td>
                              <td width="94%">
                                <%
									Vector val_location = new Vector(1,1); //hidden values that will be deliver on request (oids)
									Vector key_location = new Vector(1,1); //texts that displayed on combo box
									Vector vectLocation = PstLocation.list(0,0,"",PstLocation.fieldNames[PstLocation.FLD_NAME]);
									for(int i=0;i<vectLocation.size();i++){
										Location location = (Location)vectLocation.get(i);
										val_location.add(""+location.getOID()+"");
										key_location.add(location.getName());
									}
								%>
                                <%=ControlCombo.draw("TranLoc", null, "", val_location, key_location, "", "formElemen")%>
								</td>
                            </tr>
                            <tr>
                              <td colspan="3"><%=msg%></td>
                            </tr>
                            <tr>
                              <td colspan="3">
                                <table width="15%" border="0" cellspacing="0" cellpadding="0">
                                  <tr>
                                    <td nowrap width="13%"><a href="javascript:cmdFinish()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1001','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image1001" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Posting Transaksi"></a></td>
                                    <td nowrap width="6%">&nbsp;</td>
                                    <td class="command" nowrap width="81%"><a href="javascript:cmdFinish()"><%=textListTitleHeader[SESS_LANGUAGE][3]%></a></td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </form>
            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
      <%@ include file = "../../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>

