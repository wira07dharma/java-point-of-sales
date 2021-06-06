
<%!




/* this constant used to list text of listHeader */

public static final String textListHeaderDetail[][] = {
	{"Tanggal","Lokasi","Sistem Pembayaran","Mata Uang","Rate","Jumlah dalam Mata Uang","Jumlah"},
	{"Date","Location","Payment System","Currency","Rate","Amount in Currency","Amount"}
};

%>



<%
/**
 * get title for purchasing(pr) document
 */


/**
* get request data from current form
*/
long oidMatReceive1 = FRMQueryString.requestLong(request, "hidden_receive_id");
long oidCurrency = FRMQueryString.requestLong(request, "hidden_currency_id");

/**
* initialitation some variable
*/
/**
* instantiate some object used in this page
*/


/**
* handle current search data session 
*/


/**String whereClause2 = PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_TYPE_ID]+" = "+oidCurrency;
String orderClause2 = PstDailyRate.fieldNames[PstDailyRate.FLD_ROSTER_DATE]+" DESC";
Vector listDailyRate2 = PstDailyRate.list(0, 0, whereClause2, orderClause2);
DailyRate dr2 = (DailyRate)listDailyRate2.get(0);
double dailyRate = dr2.getSellingRate();*/
/**
* get vectSize, start and data to be display in this page
*/

Vector apDetail = SessAccPayable.getListApDetail(oidMatReceive1);
%>
<!-- End of Jsp Block -->

<script language="JavaScript">
    
function cmdEditPayment(oidPayment, oidPaymentDetail){
	document.frm_recmaterial.command.value="<%=Command.EDIT%>";
	document.frm_recmaterial.oid_payment_selected.value=oidPayment;
	document.frm_recmaterial.oid_payment_detail_selected.value=oidPaymentDetail;
	document.frm_recmaterial.action="../../../arap/payable/payable_view.jsp";
	document.frm_recmaterial.submit();
}

//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
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
//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
</script>

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

   
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" class="listgen" cellspacing="1" cellpadding="1">
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_recmaterial" method="retst" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="hidden_receive_id" value="<%=oidMatReceive1%>">
              <input type="hidden" name="approval_command">
		<tr align="left" valign="top"> 
                                  <tr class="listgentitle" align="center">
                                    <td width="15%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][0]%></strong></td>
                                    <td width="20%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][2]%></strong></td>
                                    <td width="10%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][3]%></strong></td>
				    <td width="15%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][4]%></strong></td>
                                    <td width="20%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][5]%></strong></td>
			            <td width="20%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][6]%></strong></td>
                                  </tr>
                  <%

                                            if(apDetail.size() > 0) { //kondisi untuk menampilkan list detail dari payment
                                                for(int j=0; j<apDetail.size(); j++) {
                                                    Vector temp2 = (Vector)apDetail.get(j);
                                                    AccPayable accPayable = (AccPayable)temp2.get(0);
                                                    PaymentSystem paymentSystem = (PaymentSystem)temp2.get(1);
                                                    CurrencyType currType = (CurrencyType)temp2.get(2);
                                                    AccPayableDetail accPayableDetail = (AccPayableDetail)temp2.get(3);

                                                    long oidPayment = accPayable.getOID();
						    long oidPaymentDetail = accPayableDetail.getOID();
                                                    //double amount = (accPayableDetail.getRate()*accPayableDetail.getAmount())/dailyRate;
                                                    double amount = (accPayableDetail.getRate()*accPayableDetail.getAmount());
                                    %>
                                                    <tr class="listgensell">
                                                        <td><a href="javascript:cmdEditPayment('<%=oidPayment%>', '<%=oidPaymentDetail%>')"><%=Formater.formatDate(accPayable.getPaymentDate(), "dd-MM-yyyy")%></a></td>
                                                        <td><%=paymentSystem.getPaymentSystem()%></td>
                                                        <td align="center"><%=currType.getCode()%></td>
                                                        <td align="right"><%=FRMHandler.userFormatStringDecimal(accPayableDetail.getRate())%></td>
                                                        <td align="right"><%=FRMHandler.userFormatStringDecimal(accPayableDetail.getAmount())%></td>
							<td align="right"><%=FRMHandler.userFormatStringDecimal(amount)%></td>
                                                    </tr>
                                              <% }%>
                                        <% } else {%>
						<tr  class="listgensell">
                                                     <td colspan="6" class="comment" align="center">Tidak ada list pembayaran</td>
                                                </tr>
					<% }/*end if untuk kondisi menampilkan list detail dari payment*/%>
                                        <!--add Payment-->
                                        
                                   
                </tr>
                <tr align="left" valign="top"> 
                  <td height="18" valign="top" colspan="3"> 
                 
                  </td>
                </tr>
            </form>
            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
</table>


