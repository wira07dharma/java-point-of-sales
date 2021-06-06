<%@page import="com.dimata.posbo.session.warehouse.SessStockCard"%>
<%@page import="com.dimata.posbo.entity.search.SrcStockCard"%>
<%@page import="com.dimata.posbo.form.purchasing.FrmPurchaseRequestItem"%>
<%@page import="com.dimata.posbo.session.purchasing.SessPurchaseWithPurchaseRequets"%>
<%@page import="com.dimata.posbo.session.purchasing.SessPurchaseRequest"%>
<%@page contentType="text/html"%>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.session.masterdata.SessMaterial,
                   com.dimata.posbo.form.purchasing.FrmPurchaseOrder,
				   com.dimata.posbo.entity.search.SrcMinimumStock,
				   com.dimata.posbo.session.warehouse.SessMinimumStock" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.purchasing.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_MINIMUM_STOCK); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
	public static final String textListGlobal[][] = {
		{"Buat PO", "Kembali ke List", "Daftar PO"},
		{"Create PO", "Back to Minimum Stock", "PO List"}
	};

    /* this constant used to list text of listHeader */
    public static final String textListMaterialHeader[][] =
    {
        {"No","Kode Barang","Nama Barang","Qty Request","Nama Supplier","Daftar Barang Order","Lokasi Order","Lokasi Request","Qty Order","Total Harga","Approve","Note Approve","Stok","Minimum Stok","Unit","Harga","Name Supplier","Term"},
        {"No","Code","Product Name","Qty Request","Supplier Name","Product Order List","Order Location","Location Request","Qty Order","Price Total","Approve","Note Approve","Stock","Minimum Stock","Unit","Price","Supplier Name","Term"}
    };

	public String drawList(Vector list, int language, long location_oid_select, long supplier_id_select, long periodeId, String useForRaditya) {
     ControlList ctrlist = new ControlList();

     long oidOrderLocation = Long.parseLong(PstSystemProperty.getValueByName("ORDER_RADITYA_LOCATION"));
     // for lokasi
     boolean useSelectedLoc = false;
     String locationSelected = "";
     long locationIdSelected = 0;
     Vector val_lokasi = new Vector(1, 1);
     Vector key_lokasi = new Vector(1, 1);
     Location loca = new Location();
     String where = "";
     if (useForRaditya.equals("1")) {
       if (oidOrderLocation != 0) {
         where = PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + "='" + oidOrderLocation + "'";
       }
       try {
         loca = PstLocation.fetchExc(location_oid_select);
       } catch (Exception e) {
       }
     } else {
       if (location_oid_select != 0) {
         where = PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + "='" + location_oid_select + "'";
       }
     }
     Vector vt_lokasi = PstLocation.list(0, 0, where, PstLocation.fieldNames[PstLocation.FLD_NAME]);
     if (vt_lokasi != null && vt_lokasi.size() > 0) {
       useSelectedLoc = true;
       for (int d = 0; d < vt_lokasi.size(); d++) {
         Location loc = (Location) vt_lokasi.get(d);
         String cntName = loc.getName();
         if (cntName.compareToIgnoreCase("'") >= 0) {
           cntName = cntName.replace('\'', '`');
         }
         val_lokasi.add(String.valueOf(loc.getOID()));
         key_lokasi.add(cntName);
         locationSelected = cntName;
         locationIdSelected = loc.getOID();
       }
     }

     ctrlist.setAreaWidth("100%");
     ctrlist.setListStyle("listgen");
     ctrlist.setTitleStyle("listgentitle");
     ctrlist.setCellStyle("listgensell");
     ctrlist.setHeaderStyle("listgentitle");

     ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][0] + "</div>", "2%");
     ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][1] + "</div>", "8%");
     ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][2] + "</div>", "10%");
     ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][7] + "</div>", "9%");
     ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][3] + "</div>", "5%");
     if (useSelectedLoc) {
       //qty stock
       ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][12] + "</div>", "5%");
       //qty minimum
       ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][13] + "</div>", "5%");
     }

     ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][8] + "</div>", "5%");
     ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][14] + "</div>", "4%");//unit
     ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][15] + "</div>", "4%");//unit
     ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][6] + "</div>", "4%");
     if (useForRaditya.equals("1")) {
     } else {
       ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][4] + "</div>", "10%");
     }
     ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][16] + "</div>", "10%");

     ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][9] + "</div>", "10%");
     ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][10] + "</div>", "5%");
     ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][11] + "</div>", "25%");
     ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][16] + "</div>", "25%");

     ctrlist.setLinkRow(0);
     ctrlist.setLinkSufix("");
     Vector lstData = ctrlist.getData();
     Vector lstLinkData = ctrlist.getLinkData();
     ctrlist.setLinkPrefix("javascript:cmdEdit('");
     ctrlist.setLinkSufix("')");
     ctrlist.reset();
     int index = -1;

     long oidNewSupplier = Long.parseLong(PstSystemProperty.getValueByName("DEFAULT_SUPPLIER_PO"));

     Vector val_supplier = new Vector(1, 1);
     Vector key_supplier = new Vector(1, 1);
     for (int i = 0; i < list.size(); i++) {
       Vector v1 = (Vector) list.get(i);
       SessPurchaseWithPurchaseRequets material = (SessPurchaseWithPurchaseRequets) v1.get(0);
       double qty = Double.parseDouble((String) v1.get(1));
       String locationName = (String) v1.get(2);

       Vector rowx = new Vector();
       rowx.add("" + (i + 1));
       rowx.add(String.valueOf(material.getSku()));
       rowx.add(String.valueOf(material.getName()));

       if (useForRaditya.equals("1")) {

         rowx.add(locationName);

       } else {
         //lokasi request
         if (useSelectedLoc) {
           rowx.add(locationSelected);
         } else {
           rowx.add(locationName);
         }
       }
       rowx.add("<div align = \"center\">" + qty + "</div>");
       if (useSelectedLoc) {
         double currentStock = 0;//SessPurchaseRequest.getCurrentStok(locationIdSelected, material.getOidMaterial(), periodeId);
         try {
            SrcStockCard srcStockCard = new SrcStockCard();
            Vector vectSt = new Vector(1, 1);
            vectSt.add("2");
            vectSt.add("5");
            vectSt.add("7");
            srcStockCard.setDocStatus(vectSt);
            srcStockCard.setMaterialId(material.getOidMaterial());
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, 1);
            srcStockCard.setStardDate(cal.getTime());
            currentStock = SessStockCard.qtyStock(srcStockCard);//sumQtyStockAllLocation(oidMaterial);
        } catch (Exception es) {
        }
         rowx.add("" + currentStock);
         double minimumStok = SessPurchaseRequest.getMinimumStock(locationIdSelected, material.getOidMaterial(), periodeId);
         rowx.add("" + minimumStok);
       }

       rowx.add("<div align = \"center\"><input type=\"text\" class=\"formElemen\" id='qty"+material.getPurchaseRequestID()+"' name=\"" + material.getPurchaseRequestID() + "\" size=\"10\" value=" + qty + " align=\"left\">"
               + " <input type=\"hidden\" name=\"pomaterial\" class=\"formElemen\" size=\"6\" value=" + material.getOidMaterial() + "> "
               + " <input type=\"hidden\" name=\"pomaterialID\" class=\"formElemen\" size=\"6\" value=" + material.getPurchaseRequestID() + "> "
               + " <input type=\"hidden\" name=\"txt_unit" + material.getPurchaseRequestID() + "\" class=\"formElemen\" size=\"6\" value=" + material.getStockUnitId() + "></div>"
               + " <input type=\"hidden\" name=\"txt_unit_request" + material.getPurchaseRequestID() + "\" class=\"formElemen\" size=\"6\" value=" + material.getStockUnitRequestId() + "></div>");

       rowx.add("<div align = \"center\">" + material.getUnitKode() + "</div>");
       rowx.add("<div align = \"center\"><input type=\"text\" class=\"formElemen hargaItem "+material.getOidMaterial()+" harga"+material.getPurchaseRequestID()+"\" data-oid='"+material.getPurchaseRequestID()+"' data-materialId='"+material.getOidMaterial()+"' id=\"hargaItemSupplier\" size=\"10\" name=\"hargaItem\"  value=\"" + Formater.formatNumber(material.getPriceBuying(), "###,###") + "\" align=\"left\"></div>"
               + " <input type=\"hidden\" name=\"price" + material.getPurchaseRequestID() + "\" id=\"price" + material.getPurchaseRequestID() + "\" class=\"formElemen\" size=\"6\" value=" + material.getPriceBuying() + "> ");
       if (useForRaditya.equals("1")) {
         rowx.add(ControlCombo.draw("location_order_name", null, "" + oidOrderLocation, val_lokasi, key_lokasi, "", "formElemen"));
       } else {
         rowx.add(ControlCombo.draw("location_order_name", null, "" + location_oid_select, val_lokasi, key_lokasi, "", "formElemen"));
       }
       // ini untuk pencarian Supplier
       // terhadap 1 barang
       val_supplier = new Vector(1, 1);
       key_supplier = new Vector(1, 1);
       String whereSupp = PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + "='" + oidNewSupplier + "'";
//       Vector vt_supp = PstContactList.list(0, 0, "", "");

       String oid_supplier = "0";
       String namaSupplier = "";
       double priceContract = material.getTotalPriceBuying();
       ContactList con = new ContactList();
//                          if(material.getSupplierId() != 0){
//                          try {
//                            con = PstContactList.fetchExc(material.getSupplierId());
//                            } catch (Exception e) {
//                            }
//                                    String cntName = con.getCompName();
//                                    if(cntName.length()==0){
//                                        cntName = con.getPersonName()+" "+con.getPersonLastname();
//                                    }
//                                    if (cntName.compareToIgnoreCase("'") >= 0) {
//                                        cntName = cntName.replace('\'','`');
//                                    }
//                                    val_supplier.add(String.valueOf(con.getOID()));
//
//                                    key_supplier.add(cntName);
//                                    namaSupplier=cntName;
//                          }else{
//                        if(oidNewSupplier!=material.getSupplierId()){
//       if (vt_supp != null && vt_supp.size() > 0) {
//         for (int d = 0; d < vt_supp.size(); d++) {
//           ContactList cnt = (ContactList) vt_supp.get(d);
//           String cntName = cnt.getCompName();
//           if (cntName.length() == 0) {
//             cntName = cnt.getPersonName() + " " + cnt.getPersonLastname();
//           }
//           if (cntName.compareToIgnoreCase("'") >= 0) {
//             cntName = cntName.replace('\'', '`');
//           }
//           val_supplier.add(String.valueOf(cnt.getOID()));
//
//           key_supplier.add(cntName);
//           namaSupplier = cntName;
//         }
//                            }
//                        }else{
//                                val_supplier.add(""+oidNewSupplier);
//                                key_supplier.add("New Supplier");
//                                namaSupplier=material.getSupplierName();
//                        }
//       }
                  String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]
                          + " = " + PstContactClass.CONTACT_TYPE_SUPPLIER
                          + " AND " + PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]
                          + " != " + PstContactList.DELETE;
                  Vector vt_supp = PstContactList.listContactByClassType(0, 0, wh_supp, PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);

                  for (int d = 0; d < vt_supp.size(); d++) {
                      ContactList cnt = (ContactList) vt_supp.get(d);
                      String cntName = cnt.getCompName();
                      if (cntName.length() == 0) {
                          cntName = cnt.getPersonName() + " " + cnt.getPersonLastname();
                      }
                      val_supplier.add(String.valueOf(cnt.getOID()));
                      key_supplier.add(cnt.getContactCode()+" - "+cntName);
                  }

       rowx.add(ControlCombo.draw("supplier_order_name", null, "" + material.getSupplierId(), val_supplier, key_supplier, "onChange=\"javascript:changeContractPrice(this.value,'" + material.getOidMaterial() + "')\" data-materialId='"+material.getOidMaterial()+"'", "formElemen select2 supplier"+material.getPurchaseRequestID()));
       if (useForRaditya.equals("1")) {
       } else {
         rowx.add("<div align = \"center\">" + namaSupplier + "</div>"
                 + " <input type=\"hidden\" name=\"nameSupplier" + material.getPurchaseRequestID() + "\" class=\"formElemen\" size=\"6\" value=" + namaSupplier + ">"
                 + " <input type=\"hidden\" name=\"" + FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_SUPPLIER_ID] + "\" class=\"formElemen\" size=\"6\" value=" + oidNewSupplier + "> ");
       }
       //cek harga
       rowx.add("<div align = \"center\"><input type=\"text\" class=\"formElemen total"+material.getOidMaterial()+""+material.getSupplierId()+"\" id='priceContract" + material.getPurchaseRequestID() +"' name=\"priceContract" + material.getPurchaseRequestID() + "\" size=\"7\" value=\"" + Formater.formatNumber(priceContract, "###,###") + "\" align=\"left\" readonly></div>");

       //buat kondisi yes/no
       Vector val_terms = new Vector(1, 1);
       Vector key_terms = new Vector(1, 1);
       for (int d = 1; d < PstPurchaseOrder.fieldsApprovalType.length; d++) {
         val_terms.add(String.valueOf(d));
         key_terms.add(PstPurchaseOrder.fieldsApprovalType[d]);
       }

       rowx.add(ControlCombo.draw("approval_request_order", null, "1", val_terms, key_terms, "", "formElemen"));
       if (useForRaditya.equals("1")) {
         rowx.add("<div align = \"center\"><input type=\"text\" class=\"formElemen\" name=\"note\" size=\"50\" value=\"\" align=\"left\"></div>"
                 + " <input type=\"hidden\" name=\"nameSupplier" + material.getPurchaseRequestID() + "\" class=\"formElemen\" size=\"6\" value=" + namaSupplier + ">"
                 + " <input type=\"hidden\" name=\"" + FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_SUPPLIER_ID] + "\" class=\"formElemen\" size=\"6\" value=" + oidNewSupplier + "> ");
       } else {
         rowx.add("<div align = \"center\"><input type=\"text\" class=\"formElemen\" name=\"note\" size=\"50\" value=\"\" align=\"left\"></div>");
       }

       rowx.add("" + PstPurchaseOrder.fieldsPurchaseRequestType[material.getTermOf()] + ""
               + "<input type=\"hidden\" name=\"term_of_payment\" class=\"formElemen\" size=\"6\" value=" + material.getTermOf() + ">");

       lstData.add(rowx);
     }
     return ctrlist.draw();
   }


	public boolean cekValue(Vector vect){
		for(int k=0;k<vect.size();){
			Vector vt = (Vector)vect.get(k);
			String strSKU = (String)vt.get(0);
			String strQty = (String)vt.get(1);                        
			if(!strSKU.equals("") && !strQty.equals("")){
				return true;
			}
		}
		return false;
	}

%>
<!-- JSP Block -->
<%
    long location_id_select = FRMQueryString.requestLong(request, "location_id_select");
    long supplier_id_select = FRMQueryString.requestLong(request, "supplier_id_select");
    int start = FRMQueryString.requestInt(request, "start");
    int iCommand = FRMQueryString.requestCommand(request);
    int finish = FRMQueryString.requestInt(request, "finish");
    
    //cari periode yang lagi running
    long periodeId=PstPeriode.cekExistPeriode(2);
    String pomaterial[] = request.getParameterValues("pomaterial");
    String pomaterialID[] = request.getParameterValues("pomaterialID");
    String txtlokasi[] = request.getParameterValues("location_order_name");
    String txtsupplier[] = request.getParameterValues("supplier_order_name");
    String note[]= request.getParameterValues("note");
    String hargaItem[]= request.getParameterValues("hargaItem");
    String approval[]=request.getParameterValues("approval_request_order");
    String txtterm[] = request.getParameterValues("term_of_payment");
    long documentPrId=FRMQueryString.requestLong(request, "documentPrId");
    int termOfPayment= FRMQueryString.requestInt(request, "termOfPayment");
	String createFrom = FRMQueryString.requestString(request, "create_from");
    
    double exchangeRate= FRMQueryString.requestDouble(request, "exchangeRate");
    long currencyId=FRMQueryString.requestLong(request, "currencyId");
    
    // ini proses pengambilan qty po automatic creation
    Vector list = new Vector(1,1);
    Vector listProses = new Vector(1,1);
    if(pomaterial.length > 0){
           list = SessPurchaseRequest.getPurchaseRequestForPo(request);     
    }
    // pencarian supplier untuk masing2 barang
    Vector<Long>vSelected = new Vector();
    if(pomaterial.length>0){
        for(int k=0;k < pomaterial.length;k++){
            long oidMaterial = Long.parseLong(pomaterial[k]);
                        if(FRMQueryString.requestInt(request, "pomaterial_"+oidMaterial)==1){
                            vSelected.add(oidMaterial);
                        }
        }
    }
    
	if(documentPrId != 0){
		vSelected.add(documentPrId);
	}
	
    //jika yang ditemukan cuma 1 row
    if(vSelected.size()==1){
         for(int v=0;v < vSelected.size();v++){
              long oidMaterial = (Long)vSelected.get(v);
              PurchaseRequest purchaseRequest = PstPurchaseRequest.fetchExc(oidMaterial);
              location_id_select=purchaseRequest.getLocationId();
              documentPrId=purchaseRequest.getOID();
              termOfPayment=purchaseRequest.getTermOfPayment();
              currencyId=purchaseRequest.getCurrencyId();
              exchangeRate=purchaseRequest.getExhangeRate();
         }
    }
    
    
   if(finish==1){
        list = new Vector();
        if(pomaterial.length > 0){
            for(int k=0;k < pomaterial.length;k++){
                long oidMaterial = Long.parseLong(pomaterial[k]);
                long purcaseID = Long.parseLong(pomaterialID[k]);
                double qtypo = FRMQueryString.requestDouble(request, ""+purcaseID+"");
                long oidUnit = FRMQueryString.requestLong(request,"txt_unit"+purcaseID+""); 
                long oidUnitRequest=FRMQueryString.requestLong(request, "txt_unit_request"+purcaseID+"");
                
                double price = FRMQueryString.requestDouble(request,"price"+purcaseID+""); 
                double totalprice = FRMQueryString.requestDouble(request,"priceContract"+purcaseID+""); 
                String nameSupplier = FRMQueryString.requestString(request,"nameSupplier"+purcaseID+""); 
                if(qtypo > 0){
                    
                    long oidlokasi = Long.parseLong(txtlokasi[k]);
                    long oidsupplier = Long.parseLong(txtsupplier[k]);
                    int approvalx = Integer.parseInt(approval[k]);
                    int term = Integer.parseInt(txtterm[k]);
                    String result = hargaItem[k].replaceAll("[,]", "");
                    double harga = Double.parseDouble(result);
                    String notex = note[k];
                    
                    Vector vt = new Vector();

                    vt.add(String.valueOf(oidMaterial));//0
                    
                    vt.add(String.valueOf(qtypo));//1
                    
                    vt.add(String.valueOf(oidlokasi));//2
                    vt.add(String.valueOf(oidsupplier));//3
                    vt.add(new Long(oidUnit));//4
                    
                    //tambahan untuk approval and note
                    vt.add(String.valueOf(approvalx));//5
                    vt.add(String.valueOf(notex));//6
                    
                    //tambahan untuk price and total price
                    vt.add(String.valueOf(price));//7
                    
                    
                    vt.add(String.valueOf(termOfPayment));//8
                    
                    //tambah untuk nama supplier
                    vt.add(String.valueOf(nameSupplier));//9
                    
                    //untuk currency
                    vt.add(String.valueOf(currencyId));//10
                    vt.add(String.valueOf(exchangeRate));//11
                    //unit 
                    vt.add(new Long(oidUnitRequest));//12
                    //qty
                    vt.add(String.valueOf(qtypo));//13
                    
                    //term
                    vt.add(String.valueOf(term));//14
                    vt.add(String.valueOf(harga));//15
                    
                    if(approvalx==1){
                        listProses.add(vt);
                    }
                    
                    list.add(vt);
                }
            }
            
            if(list.size() > 0){
                //disini di check jika pr 
                //type cash = peneriman tanpa po 
                //type credit = document po
                
                PstPurchaseOrder.autoInsertPoFromPr(listProses,list,documentPrId);
                String redLoc = "srcpomaterial.jsp?command="+Command.LIST;
                response.sendRedirect(redLoc);
                
            }else{
                if(pomaterial.length > 0){
                    for(int k=0;k < pomaterial.length;k++){
                        long oidMaterial = Long.parseLong(pomaterial[k]);
                        int qtypo = FRMQueryString.requestInt(request, ""+oidMaterial+"");
                        long oidUnit = FRMQueryString.requestLong(request,"txt_unit"+oidMaterial+"");       
                        if(qtypo!=0){
                            Vector vt = new Vector();
                            vt.add(String.valueOf(oidMaterial));
                            vt.add(String.valueOf(qtypo));
                            vt.add(String.valueOf(oidUnit));
                            list.add(vt);
                        }
                    }
                    if(list!=null && list.size()>0){
                        session.putValue("sess_new_po", list);
                    }
                }
            }
            
        }
    }
%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script src="../../../styles/bootstrap/js/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="../../../styles/select2/css/select2.min.css" />
<link rel="stylesheet" href="../../../styles/AdminLTE-2.3.11/plugins/select2/select2.min.css" type="text/css">
<script language="JavaScript">
    
        $( document ).ready(function() {
            $( ".hargaItem" ).keyup(function() {
                var id = $(this).data("oid");
                var qty = $("#qty"+id).val();
                var harga = $(this).val();
                var totalPrice = +harga * +qty;
                var oidMat = $(this).data("materialid");
                var oidPo = $(this).data("oid");
                var oidSupp = $(".supplier"+oidPo).val();
                var value = $(this).val();
                $( "."+oidMat ).each(function() {
                    var po = $(this).data("oid");
                    var supp = $(".supplier"+po).val();
                    if (oidSupp===supp){
                        $("#priceContract"+po).val(totalPrice);
                        $("#price"+po).val(harga);
                        $(".harga"+po).val(value);
                    }
                });
                
                
              });
        });
    
        function changeContractPrice(contactId, materialId){
            checkAjax(contactId,materialId);
        }
        
        function checkAjax(contactId,materialId){
            var nameInput="priceContract"+materialId;
//            alert("ccc "+nameInput);
            $.ajax({
            url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckPriceContract?contact_id="+contactId+"&material_id="+materialId+"",
            type : "POST",
            async : false,
            success : function(data) {
                var els=document.getElementsByName("priceContract"+materialId);
                els.value=data;
            }
        });

        }
        
        
	function cmdSave()
	{
		document.frm_newpo.command.value="<%=Command.SAVE%>";
		document.frm_newpo.action="create_pofrompr.jsp";
		document.frm_newpo.submit();
	}

	function cmdFinish() {
		var suppId = document.frm_newpo.supplier_order_name.value;
                var harga = document.querySelectorAll('[id^=hargaItemSupplier]');
                var isValid = true;
                for(var i in harga){
                    if (harga[i].value === "0"){
                        isValid=false;
                    }
                }
                if (isValid){
                    var x = confirm("Are you sure ?");
			if(x){
				document.frm_newpo.command.value="<%=Command.SAVE%>";
				document.frm_newpo.finish.value="1";
        alert("Purchase Order Success");
         document.frm_newpo.action="create_pofrompr.jsp";
				document.frm_newpo.submit();
			}
                } else {
                    alert("Pastikan harga tidak 0!");
                }
    }

	function cmdBack() {
		document.frm_newpo.command.value="<%=Command.BACK%>";
		document.frm_newpo.action=  "pofromprmaterial_list.jsp";// "srcpomaterial.jsp";
		document.frm_newpo.submit();
	}
	
	function cmdBackPo() {
		document.frm_newpo.command.value="<%=Command.BACK%>";
		document.frm_newpo.action=  "../../../purchasing/material/pom/pomaterial_list.jsp";
		document.frm_newpo.submit();
	}

	function cmdListFirst()
    {
		document.frm_newpo.command.value="<%=Command.FIRST%>";
		document.frm_newpo.action="new_pomaterial.jsp";
		document.frm_newpo.submit();
	}

	function cmdListPrev()
    {
		document.frm_newpo.command.value="<%=Command.PREV%>";
		document.frm_newpo.action="new_pomaterial.jsp";
		document.frm_newpo.submit();
	}

	function cmdListNext()
    {
		document.frm_newpo.command.value="<%=Command.NEXT%>";
		document.frm_newpo.action="new_pomaterial.jsp";
		document.frm_newpo.submit();
	}

	function cmdListLast()
    {
		document.frm_newpo.command.value="<%=Command.LAST%>";
		document.frm_newpo.action="new_pomaterial.jsp";
		document.frm_newpo.submit();
	}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<!-- #BeginEditable "headerscript" -->
<SCRIPT language=JavaScript>
<!--
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

function hideObjectForSystem(){
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
//-->
</SCRIPT>
    <%@include file="../../../styles/plugin_component.jsp" %>
<!-- #EndEditable --> 
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --><strong>
                  <%=textListMaterialHeader[SESS_LANGUAGE][5]%> <%=PstPurchaseOrder.fieldsPurchaseRequestType[termOfPayment]%></strong><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <%
try
{
%>
            <form name="frm_newpo" method="post" action="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="finish" value="">
              <input type="hidden" name="location_id_select"  value="<%=location_id_select%>">
              <input type="hidden" name="supplier_id_select"  value="<%=supplier_id_select%>">
              <input type="hidden" name="documentPrId" value="<%=documentPrId%>">
              <input type="hidden" name="termOfPayment" value="<%=termOfPayment%>">
              
              <input type="hidden" name="exchangeRate" value="<%=exchangeRate%>">
              <input type="hidden" name="currencyId" value="<%=currencyId%>">
              
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <%
                if (finish != 1){
                	if(list != null && list.size()>0){
                    	try{
                            %>
                                    <tr>
                                        
                                      <td colspan="2">
                                          
                                          <%= drawList(list,SESS_LANGUAGE,location_id_select,supplier_id_select,periodeId, useForRaditya)%>
                                      
                                      </td>
                                      
                                    </tr>
                                    
                                    <tr>
                                      <td span class="command"></td>
                                    </tr>
                            <%
                    }
                    catch(Exception exc){
                            System.out.println("DrawList : " + exc.toString());
                    }
                }
                %>
                <tr>
                  <td class="command" colspan="2"><br>
                    <table width="62%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <a href="javascript:cmdFinish()" class="command btn btn-primary"><%=textListGlobal[SESS_LANGUAGE][0]%></a>
                        <a href="javascript:cmdBack()" class="btn btn-primary"><%=textListGlobal[SESS_LANGUAGE][1]%></a>
                      </tr>
                    </table>
                  </td>
                </tr>
				<%
					}else{
				%>
                <tr>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td align="center" span class="comment" bgcolor="#CCCCCC">Proses success ... </td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td>
				    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td nowrap width="4%"><a href="javascript:cmdBackPo()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image101','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image101" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=textListGlobal[SESS_LANGUAGE][2]%>"></a></td>
                        <td class="command" nowrap width="98%"><a href="javascript:cmdBackPo()"><%=textListGlobal[SESS_LANGUAGE][2]%></a></td>
                      </tr>
                    </table>
	           </td>
                </tr>
                <%
                }
                %>
              </table>
            </form>
            <%
                }
                catch(Exception eee)
                {
                    out.println("Form Exc : "+eee);
                }
            %>
            <script language="JavaScript">
	window.focus();
</script>
            <!-- #EndEditable --></td> 
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
      
      <script type="text/javascript" src="../../../styles/select2/js/select2.min.js"></script>
<script type="text/javascript" src="../../../styles/AdminLTE-2.3.11/plugins/select2/select2.full.min.js"></script>
<script>
    
  $(document).ready(function () {
  $('.select2').select2({placeholder: "Semua"});
  $('.selectAll').select2({placeholder: "Semua"});
  });
</script>
</body>
<!-- #EndTemplate --></html>

