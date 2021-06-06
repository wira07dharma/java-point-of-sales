<%-- 
    Document   : create_transferfromtransferequest
    Created on : Apr 23, 2014, 2:37:13 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.posbo.form.purchasing.FrmPurchaseRequestItem"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmMatDispatch"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatch"%>
<%@page import="com.dimata.posbo.session.masterdata.SessPosting"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatDispatch"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatch"%>
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
<%  int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_MINIMUM_STOCK);
  int appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%  
	boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
<%!  
	public static final int ADD_TYPE_LIST = 1;

  public static final String textListGlobal[][] = {
    {"Buat Transfer", "Kembali ke List", "Daftar Transfer", "Buat PO"},
    {"Create Dispatch", "Back to Minimum Stock", "Dispatch List", "Create PO"}
  };
  /* this constant used to list text of listHeader */
  public static final String textListMaterialHeader[][]= {
            {"No", "Kode Barang", "Nama Barang", "Qty Request", "Nama Supplier", "Store Request", "Lokasi Transfer", "Lokasi Request", "Qty Transfer", "Total Harga", "Approve", "Note Approve", "Stok", "Minimum Stok", "Unit", "Harga", "Name Supplier"},
            {"No", "Code", "Product Name", "Qty Request", "Supplier Name", "Store Request", "Dispatch Location", "Location Request", "Qty Dispatch", "Price Total", "Approve", "Note Approve", "Stock", "Minimum Stock", "Unit", "Price", "Supplier Name"}
          };

  public String drawList(Vector list, int language, long location_oid_select, long supplier_id_select, long periodeId, boolean privShowQtyPrice, long location_transfer_id, PurchaseRequest pr, String useForRaditya) {
    ControlList ctrlist = new ControlList();

    // for lokasi
    boolean useSelectedLoc = true;
    String locationSelected = "";
    String locationTransferSelected = "";
    long locationIdSelected = 0;
    long locationTransferIdSelected = 0;
    Vector val_lokasi = new Vector(1, 1);
    Vector key_lokasi = new Vector(1, 1);
	Vector vt_lokasi = new Vector(1,1);
    String where = "";
    if (location_oid_select != 0) {
      where = PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + "='" + location_oid_select + "'";
    }
    vt_lokasi = PstLocation.list(0, 0, where, PstLocation.fieldNames[PstLocation.FLD_NAME]);
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
    ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][1] + "</div>", "20%");
    ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][2] + "</div>", "20%");
    ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][7] + "</div>", "15%");
    ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][6] + "</div>", "15%");
    ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][16] + "</div>", "15%");
    ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][16] + "</div>", "15%");
    ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][3] + "</div>", "5%");
    if (useSelectedLoc) {
      //qty stock
      ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][12] + "</div>", "5%");
      //qty minimum
      //ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][13]+"</div>","5%");
    }
    ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][8] + "</div>", "5%");
    ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][14] + "</div>", "4%");//unit
if(useForRaditya.equals("1")){}else{
    if (privShowQtyPrice) {
      ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][15] + "</div>", "4%");//unit harga
    }
    
    if (privShowQtyPrice) {
      ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][9] + "</div>", "10%");
    }
}
      ctrlist.addHeader("<div align=\"center\">Term</div>", "10%");

//	ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][10] + "</div>", "5%");
//    ctrlist.addHeader("<div align=\"center\">" + textListMaterialHeader[language][11] + "</div>", "15%");

    ctrlist.setLinkRow(0);
    ctrlist.setLinkSufix("");
    Vector lstData = ctrlist.getData();
    Vector lstLinkData = ctrlist.getLinkData();
    ctrlist.setLinkPrefix("javascript:cmdEdit('");
    ctrlist.setLinkSufix("')");
    ctrlist.reset(); 
    int index = -1;

    long oidNewSupplier = Long.parseLong(PstSystemProperty.getValueByName("MAPPING_NEW_SUPPLIER_ID"));
    long oidSupplierSys = Long.parseLong(PstSystemProperty.getValueByName("DEFAULT_SUPPLIER_PO")); 
    Vector val_supplier = new Vector(1, 1);
    Vector key_supplier = new Vector(1, 1);

    for (int i = 0; i < list.size(); i++) {
      Vector v1 = (Vector) list.get(i);
      SessPurchaseWithPurchaseRequets material = (SessPurchaseWithPurchaseRequets) v1.get(0);
      double qty = Double.parseDouble((String) v1.get(1));
      String locationName = (String) v1.get(2);

      Vector rowx = new Vector();
	  String hidden = "<input type=\"hidden\" name=\"approval_request_order\" id=\"approval_request_order\" value=\"1\">"
					+ "<input type=\"hidden\" name=\"note\" id=\"note\" value=\"\">";
      rowx.add("" + (i + 1) + hidden);
      rowx.add(String.valueOf(material.getSku()));
      rowx.add(String.valueOf(material.getName()));

      //lokasi request
//      if (useSelectedLoc) {
//        rowx.add("<div class=\"lokasi\">" + locationSelected + "</div>");
//      } else {
//        rowx.add("<div class=\"lokasi\">" + locationName + "</div>");
//      }
		Location asal = new Location();
		Location tujuan = new Location();
	  try {
		  asal = PstLocation.fetchExc(location_oid_select);
		  tujuan = PstLocation.fetchExc(location_transfer_id);

	  } catch (Exception e) {
	  }
            
	  rowx.add("<div class=\"lokasi\"><input type=\"hidden\" name=\"location_order_name\" id=\"location_order_name\" value=\"" + asal.getOID() + "\">" + asal.getName() + "</div>");
	  rowx.add("<div class=\"lokasi\">" + tujuan.getName() + "</div>");
      //rowx.add(ControlCombo.draw("location_order_name", null, String.valueOf(0), val_lokasi, key_lokasi, "", "formElemen"));
                        // ini untuk pencarian Supplier
                        // terhadap 1 barang
                        String whereSupp= PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]+"='"+oidSupplierSys+"'";
//                        Vector vt_supp = PstContactList.list(0,0,"","");

                        String oid_supplier = "0";
                        String namaSupplier="";
                        double priceContract=material.getTotalPriceBuying();
//                        if(oidSupplierSys == material.getSupplierId()){
//                            if(vt_supp!=null && vt_supp.size()>0){
//                                for(int d=0; d<vt_supp.size(); d++){
//                                    ContactList cnt = (ContactList)vt_supp.get(d);
//                                    String cntName = cnt.getCompName();
//                                    if(cntName.length()==0){
//                                        cntName = cnt.getPersonName()+" "+cnt.getPersonLastname();
//                                    }
//                                    if (cntName.compareToIgnoreCase("'") >= 0) {
//                                        cntName = cntName.replace('\'','`');
//                                    }
//                                    val_supplier.add(String.valueOf(cnt.getOID()));
//
//                                    key_supplier.add(cntName);
//                                    namaSupplier=cntName;
//                                }
//                            }
//                        }else{
//                                val_supplier.add(""+oidSupplierSys);
//                                key_supplier.add("New Supplier");
//                                namaSupplier=material.getSupplierName();
//                        }
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
                        rowx.add(ControlCombo.draw("supplier_order_name",null,""+material.getSupplierId(),val_supplier,key_supplier,"onChange=\"javascript:changeContractPrice(this.value,'"+material.getOidMaterial()+"')\"","formElemen"));
                        
                        rowx.add("<div align = \"center\">"+namaSupplier+"</div>"+
                                 " <input type=\"hidden\" name=\"nameSupplier"+material.getPurchaseRequestID()+"\" class=\"formElemen\" size=\"6\" value="+material.getSupplierId()+ ">");
                        
      rowx.add("<div align = \"center\">" + qty + "</div>");

      if (useSelectedLoc) {
        double currentStock = SessPurchaseRequest.getCurrentStok(locationIdSelected, material.getOidMaterial(), periodeId);
        rowx.add("" + currentStock);
//                            double minimumStok = SessPurchaseRequest.getMinimumStock(locationIdSelected, material.getOidMaterial(), periodeId);
//                            rowx.add(""+minimumStok);
      }

      if (privShowQtyPrice) {
        rowx.add("<div align = \"center\"><input type=\"text\" class=\"formElemen\" name=\"" + material.getPurchaseRequestID() + "\" size=\"4\" value=" + qty + " align=\"left\">"
                + "<input type=\"hidden\" class=\"formElemen\" name=\"" + material.getOidMaterial() + "\" size=\"10\" value=" + qty + " align=\"left\">"
                + " <input type=\"hidden\" name=\"pomaterial\" class=\"formElemen\" size=\"6\" value=" + material.getOidMaterial() + "> "
                + " <input type=\"hidden\" name=\"pomaterialID\" class=\"formElemen\" size=\"6\" value="+material.getPurchaseRequestID()+ "> "
                + " <input type=\"hidden\" name=\"pomaterial_"+ pr.getOID() +"\" class=\"formElemen\" size=\"6\" value=\"1\"> "
                + " <input type=\"hidden\" name=\"txt_unit" + material.getPurchaseRequestID() + "\" class=\"formElemen\" size=\"6\" value=" + material.getStockUnitId() + "></div>"
                + " <input type=\"hidden\" name=\"txtunit" + material.getOidMaterial() + "\" class=\"formElemen\" size=\"6\" value=" + material.getStockUnitId() + "></div>"
                + " <input type=\"hidden\" name=\"location_transfer" + material.getOidMaterial() + "\" class=\"formElemen\" size=\"6\" value=" + location_transfer_id + "></div>"
                + " <input type=\"hidden\" name=\"txt_unit_req" + material.getOidMaterial() + "\" class=\"formElemen\" size=\"6\" value=" + material.getStockUnitId() + "></div>"
                + " <input type=\"hidden\" name=\"txt_unit_request" + material.getPurchaseRequestID() + "\" class=\"formElemen\" size=\"6\" value=" + material.getStockUnitId() + "></div>"
                + " <input type=\"hidden\" name=\"price" + material.getOidMaterial()+ "\" class=\"formElemen\" size=\"6\" value=" + material.getPriceBuying() + ">");
      } else {
        double totalPrice = qty * material.getPriceBuying();
        rowx.add("<div align = \"center\"><input type=\"text\" class=\"formElemen\" name=\"" + material.getPurchaseRequestID() + "\" size=\"10\" value=" + qty + " align=\"left\">"
                + "<input type=\"hidden\" class=\"formElemen\" name=\"" + material.getOidMaterial() + "\" size=\"10\" value=" + qty + " align=\"left\">"
                + " <input type=\"hidden\" name=\"pomaterial\" class=\"formElemen\" size=\"6\" value=" + material.getOidMaterial() + "> " 
                + " <input type=\"hidden\" name=\"pomaterialID\" class=\"formElemen\" size=\"6\" value="+material.getPurchaseRequestID()+ "> "
                + " <input type=\"hidden\" name=\"pomaterial_"+ pr.getOID() +"\" class=\"formElemen\" size=\"6\" value=\"1\"> "
                + " <input type=\"hidden\" name=\"txt_unit" + material.getPurchaseRequestID() + "\" class=\"formElemen\" size=\"6\" value=" + material.getStockUnitId() + "></div>"
                + " <input type=\"hidden\" name=\"txtunit" + material.getOidMaterial() + "\" class=\"formElemen\" size=\"6\" value=" + material.getStockUnitId() + "></div>"
                + " <input type=\"hidden\" name=\"price" + material.getPurchaseRequestID() + "\" class=\"formElemen\" size=\"6\" value=" + material.getPriceBuying() + ">"
                + "<input type=\"text\" class=\"formElemen\" name=\"priceContract" + material.getOidMaterial() + "\" size=\"7\" value=\"" + Formater.formatNumber(totalPrice, "###,###") + "\" align=\"left\">"
                + " <input type=\"hidden\" name=\"location_transfer" + material.getOidMaterial() + "\" class=\"formElemen\" size=\"6\" value=" + location_transfer_id + "></div>"
                + " <input type=\"hidden\" name=\"txt_unit_req" + material.getOidMaterial() + "\" class=\"formElemen\" size=\"6\" value=" + material.getStockUnitId() + "></div>"
                + " <input type=\"hidden\" name=\"txt_unit_request" + material.getPurchaseRequestID() + "\" class=\"formElemen\" size=\"6\" value=" + material.getStockUnitId() + "></div>");
      }
 
      rowx.add("<div align = \"center\">" + material.getUnitKode() + "</div>");
if(useForRaditya.equals("1")){}else{
      if (privShowQtyPrice) {
        rowx.add("<div align = \"center\">" + Formater.formatNumber(material.getPriceBuying(), "###,###") + "</div>"
                + " <input type=\"hidden\" name=\"price" + material.getOidMaterial() + "\" class=\"formElemen\" size=\"6\" value=" + material.getPriceBuying() + "> ");
      }


      //cek harga
      if (privShowQtyPrice) {
        double totalPrice = qty * material.getPriceBuying();
        rowx.add("<div align = \"center\"><input type=\"text\" class=\"formElemen\" name=\"priceContract" + material.getOidMaterial() + "\" size=\"7\" value=\"" + Formater.formatNumber(totalPrice, "###,###") + "\" align=\"left\"></div>");
      }
}
        rowx.add(""+PstPurchaseOrder.fieldsPurchaseRequestType[PstPurchaseOrder.PR_DIRECT]+""
                +  "<input type=\"hidden\" name=\"term_of_payment\" class=\"formElemen\" size=\"6\" value="+PstPurchaseOrder.PR_DIRECT+ ">" );

	//buat kondisi yes/no
      Vector val_terms = new Vector(1, 1);
      Vector key_terms = new Vector(1, 1);
      for (int d = 1; d < PstPurchaseOrder.fieldsApprovalType.length; d++) {
        val_terms.add(String.valueOf(d));
        key_terms.add(PstPurchaseOrder.fieldsApprovalType[d]);
      }

//      rowx.add(ControlCombo.draw("approval_request_order", null, "1", val_terms, key_terms, "", "formElemen"));
//      rowx.add("<div align = \"center\"><input type=\"text\" class=\"formElemen\" name=\"note\" size=\"10\" value=\"\" align=\"left\"></div>");

      lstData.add(rowx);
    }
    return ctrlist.draw();
  }

  public boolean cekValue(Vector vect) {
    for (int k = 0; k < vect.size();) {
      Vector vt = (Vector) vect.get(k);
      String strSKU = (String) vt.get(0);
      String strQty = (String) vt.get(1);
      if (!strSKU.equals("") && !strQty.equals("")) {
        return true;
      }
    }
    return false;
  }

%>
<!-- JSP Block -->
<%
  long location_id_select = FRMQueryString.requestLong(request, "location_id_select");
  long location_transfer_id = FRMQueryString.requestLong(request, "location_transfer_id");

  long supplier_id_select = FRMQueryString.requestLong(request, "supplier_id_select");
  int start = FRMQueryString.requestInt(request, "start");
  int iCommand = FRMQueryString.requestCommand(request);
  int finish = FRMQueryString.requestInt(request, "finish");

  //cari periode yang lagi running
  long periodeId = PstPeriode.cekExistPeriode(2);
  String pomaterial[] = request.getParameterValues("pomaterial");
  String pomaterialID[] = request.getParameterValues("pomaterialID");
  String txtlokasi[] = request.getParameterValues("location_order_name");
  String txtterm[] = request.getParameterValues("term_of_payment");
  String txtsupplier[] = request.getParameterValues("supplier_order_name");
  String note[] = request.getParameterValues("note");
  String approval[] = request.getParameterValues("approval_request_order");
  long documentPrId = FRMQueryString.requestLong(request, "documentPrId");
  int termOfPayment = FRMQueryString.requestInt(request, "termOfPayment");

  double exchangeRate = FRMQueryString.requestDouble(request, "exchangeRate");
  long currencyId = FRMQueryString.requestLong(request, "currencyId");
  
  String dispatchCode = "";
  String locationType = FRMQueryString.requestString(request, FrmSrcPurchaseRequest.fieldNames[FrmMatDispatch.FRM_FIELD_LOCATION_TYPE]);
  PurchaseRequest purchaseRequest = null;
  Date date = new Date();
  String jenisDokumen = "";
  String nomorBC = "";



  int dispatchStatus = FRMQueryString.requestInt(request, FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_STATUS]); 
  
  // ini proses pengambilan qty po automatic creation
  Vector list = new Vector(1, 1);
  Vector listProses = new Vector(1, 1);
  int countPoMaterial = 0;
  try {
    countPoMaterial = pomaterial.length;
  } catch (Exception ex) {
  }
  if (countPoMaterial > 0) {
    list = SessPurchaseRequest.getPurchaseRequestForTransfer(request);
  }
  // pencarian supplier untuk masing2 barang
  Vector<Long> vSelected = new Vector();
  if (countPoMaterial > 0) {
    for (int k = 0; k < countPoMaterial; k++) {
      long oidMaterial = Long.parseLong(pomaterial[k]);
      if (FRMQueryString.requestInt(request, "pomaterial_" + oidMaterial) == 1) {
        vSelected.add(oidMaterial);
      }
    }
  }

  //jika yang ditemukan cuma 1 row
  Location location = new Location();
  if (vSelected.size() > 0) {
    for (int v = 0; v < vSelected.size(); v++) {
      long oidMaterial = (Long) vSelected.get(v);
      purchaseRequest = PstPurchaseRequest.fetchExc(oidMaterial);
      location_id_select = purchaseRequest.getLocationId();
      documentPrId = purchaseRequest.getOID();
      termOfPayment = purchaseRequest.getTermOfPayment();
      currencyId = purchaseRequest.getCurrencyId();
      exchangeRate = purchaseRequest.getExhangeRate();
      location_transfer_id = purchaseRequest.getWarehouseSupplierId();
	  dispatchCode = purchaseRequest.getPrCode();
	  date = purchaseRequest.getPurchRequestDate();
	  jenisDokumen = purchaseRequest.getJenisDocument();
	  nomorBC = purchaseRequest.getNomorBc();
      try {
        location = PstLocation.fetchExc(location_transfer_id);
      } catch (Exception ex) {
      }
    }
  }

  if (finish == 1) {
    list = new Vector();
    if (pomaterial.length > 0) {
      for (int k = 0; k < pomaterial.length; k++) {
        long oidMaterial = Long.parseLong(pomaterial[k]);
        double qtypo = FRMQueryString.requestDouble(request, "" + oidMaterial + "");
        long oidUnit = FRMQueryString.requestLong(request, "txtunit" + oidMaterial + "");
        long oidUnitRequest = FRMQueryString.requestLong(request, "txt_unit_req" + oidMaterial + "");
        long locationTransfer = FRMQueryString.requestLong(request, "location_transfer" + oidMaterial + "");

        double price = FRMQueryString.requestDouble(request, "price" + oidMaterial + "");
        double totalprice = FRMQueryString.requestDouble(request, "priceContract" + oidMaterial + "");
        String nameSupplier = FRMQueryString.requestString(request, "nameSupplier" + oidMaterial + "");
        if (qtypo > 0) {
          long oidlokasi = Long.parseLong(txtlokasi[k]);
          int approvalx = Integer.parseInt(approval[k]);
          String notex = note[k];

          Vector vt = new Vector();

          vt.add(String.valueOf(oidMaterial));//0

          vt.add(String.valueOf(qtypo));//1

          vt.add(String.valueOf(oidlokasi));//2
          vt.add(String.valueOf(0));//3
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

          //locationtransfer 
          vt.add(String.valueOf(locationTransfer));//14
          
          vt.add(String.valueOf(price));//7

          if (approvalx == 1) {
            listProses.add(vt);
          }

          list.add(vt);
        }
      }

      if (list.size() > 0) {
        //disini di check jika pr 
        //type cash = peneriman tanpa po 
        //type credit = document po
        long oidRes = PstMatDispatch.autoInsertPoFromPr(listProses, list, documentPrId);
//		MatDispatch matDispatch = PstMatDispatch.fetchExc(oidRes);
//		matDispatch.setDispatchStatus(I_DocStatus.DOCUMENT_STATUS_FINAL);
//		PstMatDispatch.updateExc(matDispatch);
//		CtrlMatDispatch ctrlMatDispatch = new CtrlMatDispatch(request);
//		ctrlMatDispatch.action(Command.SAVE, oidRes, userName, userId);
//		SessPosting sessPosting = new SessPosting();
//		sessPosting.postedDispatchDoc(oidRes);
		String redLoc = "df_stock_wh_material_edit.jsp?start=0"
				+ "&approval_command="+Command.APPROVE
				+ "&command="+Command.EDIT
				+ "&hidden_dispatch_id="+oidRes;
		response.sendRedirect(redLoc);
		
		
      } else {
        if (pomaterial.length > 0) {
          for (int k = 0; k < pomaterial.length; k++) {
            long oidMaterial = Long.parseLong(pomaterial[k]);
            int qtypo = FRMQueryString.requestInt(request, "" + oidMaterial + "");
            long oidUnit = FRMQueryString.requestLong(request, "txt_unit" + oidMaterial + "");
            if (qtypo != 0) {
              Vector vt = new Vector();
              vt.add(String.valueOf(oidMaterial));
              vt.add(String.valueOf(qtypo));
              vt.add(String.valueOf(oidUnit));
              list.add(vt);
            }
          }
          if (list != null && list.size() > 0) {
            session.putValue("sess_new_po", list);
          }
        }
      }
    } 
  } else if (finish==2){
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
    <script src="../../../styles/jquery.min.js"></script>
		<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/bootstrap/css/bootstrap.min.css">
		<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/plugins/font-awesome-4.7.0/css/font-awesome.min.css">
		<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/plugins/ionicons-2.0.1/css/ionicons.min.css">
		<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/dist/css/AdminLTE.min.css">
		<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/dist/css/skins/_all-skins.min.css">
		<link rel="stylesheet" type="text/css" href="../../../styles/plugin/datatables/dataTables.bootstrap.css">
    <script language="JavaScript">
      function changeContractPrice(contactId, materialId) {
        checkAjax(contactId, materialId);
      }

      function checkAjax(contactId, materialId) {
        var nameInput = "priceContract" + materialId;
//        alert("ccc " + nameInput);
        $.ajax({
          url: "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckPriceContract?contact_id=" + contactId + "&material_id=" + materialId + "",
          type: "POST",
          async: false,
          success: function (data) {
            alert("Harga : " + data);
            document.frm_newpo.priceContract.value = data;
          }
        });

      }

      function cmdSave()
      {
        document.frm_newpo.command.value = "<%=Command.SAVE%>";
        document.frm_newpo.action = "create_transferfromtransferequest.jsp";
        document.frm_newpo.submit();
      }

      function cmdFinish() {
        //var suppId = document.frm_newpo.supplier_order_name.value;
        var x = confirm("Are you sure ?");
        if (x) {
          document.frm_newpo.command.value = "<%=Command.SAVE%>";
	  document.frm_newpo.<%= FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_STATUS] %>.value = "<%= I_DocStatus.DOCUMENT_STATUS_FINAL %>";
          document.frm_newpo.finish.value = "1";
          document.frm_newpo.action = "create_transferfromtransferequest.jsp";
          document.frm_newpo.submit();
        }
      }      
      function cmdPO() {
        //var suppId = document.frm_newpo.supplier_order_name.value;
//        var suppId = document.frm_newpo.supplier_order_name.value;
//        var x = confirm("Are you sure ?");
//        if (x) {
//          document.frm_newpo.command.value = "<%=Command.SAVE%>";
//		  document.frm_newpo.<%= FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_STATUS] %>.value = "<%= I_DocStatus.DOCUMENT_STATUS_FINAL %>";
//          document.frm_newpo.finish.value = "2";
//          document.frm_newpo.action = "create_transferfromtransferequest.jsp";
//          document.frm_newpo.submit();
//        }
		document.frm_newpo.start.value = 0;
		document.frm_newpo.command.value = "<%= Command.ADD %>";
		document.frm_newpo.approval_command.value = "<%= Command.SAVE %>";
		document.frm_newpo.add_type.value = "<%= ADD_TYPE_LIST %>";
		document.frm_newpo.action="<%= approot %>/purchasing/material/pom/create_pofrompr.jsp";
		document.frm_newpo.submit();

      }

      function cmdBack() {
        document.frm_newpo.command.value = "<%=Command.BACK%>";
        document.frm_newpo.action = "search_purchasefor_transfer.jsp";// "srcpomaterial.jsp";
        document.frm_newpo.submit();
      }

      function cmdBackPo() {
        document.frm_newpo.command.value = "<%=Command.BACK%>";
        document.frm_newpo.action = "search_purchasefor_transfer.jsp";
        document.frm_newpo.submit();
      }

      function cmdListFirst()
      {
        document.frm_newpo.command.value = "<%=Command.FIRST%>";
        document.frm_newpo.action = "new_pomaterial.jsp";
        document.frm_newpo.submit();
      }

      function cmdListPrev()
      {
        document.frm_newpo.command.value = "<%=Command.PREV%>";
        document.frm_newpo.action = "new_pomaterial.jsp";
        document.frm_newpo.submit();
      }

      function cmdListNext()
      {
        document.frm_newpo.command.value = "<%=Command.NEXT%>";
        document.frm_newpo.action = "new_pomaterial.jsp";
        document.frm_newpo.submit();
      }

      function cmdListLast()
      {
        document.frm_newpo.command.value = "<%=Command.LAST%>";
        document.frm_newpo.action = "new_pomaterial.jsp";
        document.frm_newpo.submit();
      }

      function MM_swapImgRestore() { //v3.0
        var i, x, a = document.MM_sr;
        for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++)
          x.src = x.oSrc;
      }

      function MM_preloadImages() { //v3.0
        var d = document;
        if (d.images) {
          if (!d.MM_p)
            d.MM_p = new Array();
          var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
          for (i = 0; i < a.length; i++)
            if (a[i].indexOf("#") != 0) {
              d.MM_p[j] = new Image;
              d.MM_p[j++].src = a[i];
            }
        }
      }

      function MM_swapImage() { //v3.0
        var i, j = 0, x, a = MM_swapImage.arguments;
        document.MM_sr = new Array;
        for (i = 0; i < (a.length - 2); i += 3)
          if ((x = MM_findObj(a[i])) != null) {
            document.MM_sr[j++] = x;
            if (!x.oSrc)
              x.oSrc = x.src;
            x.src = a[i + 2];
          }
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
    <%if (menuUsed == MENU_ICON) {%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
    <%}%>
    <!-- #BeginEditable "headerscript" -->
    <SCRIPT language=JavaScript>
<!--
      function hideObjectForMarketing() {
      }

      function hideObjectForWarehouse() {
      }

      function hideObjectForProduction() {
      }

      function hideObjectForPurchasing() {
      }

      function hideObjectForAccounting() {
      }

      function hideObjectForHRD() {
      }

      function hideObjectForGallery() {
      }

      function hideObjectForMasterData() {
      }

      function hideObjectForSystem() {
      }

      function MM_swapImgRestore() { //v3.0
        var i, x, a = document.MM_sr;
        for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++)
          x.src = x.oSrc;
      }
//-->

</SCRIPT>
<!-- #EndEditable --> 
<style>
      .listgentitle {
        font-size: 12px !important;
        font-style: normal;
        font-weight: bold;
        color: #FFFFFF;
        background-color: #3e85c3 !important;
        text-align: center;
        height: 25px !important;
        border: 1px solid !important;
      }
      table.listgen {
        text-align: center;
        width: 90% !important;
      }
      a.btn.btn-primary.store {
        margin: 25px 0px 0px 30px;
      }
      .listgensell {
        color: #000000;
        background-color: #ffffff !important;
        border: 1px solid #3e85c3;
      }
      .pull-left.btn-test {
    width: 100%;
    padding-top: 20px;
}
.cls {
    margin-right: 10px;
    margin-left: 0 !important;
    height: 30px;
    padding: 5px 10px 10px 10px;
}
table.daftar {
    margin-top: 20px;
    margin-left: 15px;
}
</style>
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
      <%if (menuUsed == MENU_PER_TRANS) {
      %>
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
    <%} else {%>
    <tr bgcolor="#FFFFFF">
      <td height="10" ID="MAINMENU">
        <%@include file="../../../styletemplate/template_header.jsp" %>
      </td>
    </tr>
    <%}%>
    <tr> 
      <td valign="top" align="left"> 
        <table width="100%" border="0" cellspacing="0" cellpadding="0">  
          <section class="content-header">
              <h1><%=textListMaterialHeader[SESS_LANGUAGE][5]%>
                <small>To <%=location.getName()%></small></h1>
            </section>
          <tr> 
            <td>
              <%
                try {
              %>
              <form name="frm_newpo" method="post" action="">
                <input type="hidden" name="start" value="<%=start%>">
                <input type="hidden" name="command" value="<%=iCommand%>">
                <input type="hidden" name="finish" value="">
                <input type="hidden" name="location_id_select"  value="<%=location_id_select%>">

                <input type="hidden" name="location_transfer_id"  value="<%=location_transfer_id%>">

                <input type="hidden" name="supplier_id_select"  value="<%=supplier_id_select%>">
                <input type="hidden" name="documentPrId" value="<%=documentPrId%>">
                <input type="hidden" name="termOfPayment" value="<%=termOfPayment%>">

                <input type="hidden" name="exchangeRate" value="<%=exchangeRate%>">
                <input type="hidden" name="currencyId" value="<%=currencyId%>">
				<input type="hidden" name="start" value="">
				<input type="hidden" name="add_type" value="">
				<input type="hidden" name="approval_command" value="">
				<input type="hidden" name="create_from" value="storeRequest">
				
                <input type="hidden" name="<%= FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_STATUS] %>" value="<%=dispatchStatus%>">
				
				
<!--				<input type="hidden" name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_CODE]%>" value="<%= dispatchCode %>">
				<input type="hidden" name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstMatDispatch.FLD_TYPE_DISPATCH_LOCATION_WAREHOUSE%>">
				<input type="hidden" name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_LOCATION_ID]%>" value="<%= location_id_select %>">
				<input type="hidden" name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_TO]%>" value="<%= location_transfer_id %>">-->
				
                <table class="daftar" width="100%" border="0" cellspacing="1" cellpadding="1">
                  <%
                    if (finish != 1) {
                      if (list != null && list.size() > 0) {
                        try {
                  %>
                  <tr>
                    <td colspan="2">
                      <%= drawList(list, SESS_LANGUAGE, location_id_select, supplier_id_select, periodeId, privShowQtyPrice, location_transfer_id, purchaseRequest, useForRaditya)%>
                    </td>
                  </tr>
                  <tr>
                    <td span class="command"></td>
                  </tr>
                  <%
                      } catch (Exception exc) {
                        System.out.println("DrawList : " + exc.toString());
                      }
                    }
                  %>
                  <tr>
                  <!--<td class="command" colspan="2">
                      <table width="62%" border="0" cellspacing="0" cellpadding="0">
                        <tr>-->
                          <!--td nowrap width="5%"><a href="javascript:cmdFinish()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1001','','<%=approot%>/images/BtnSaveOn.jpg',1)"><img name="Image1001" border="0" src="<%=approot%>/images/BtnSave.jpg" width="24" height="24" alt="<%=textListGlobal[SESS_LANGUAGE][0]%>"></a></td-->
                          <!--<td class="command" nowrap width="25%"><a class="btn btn-primary" href="javascript:cmdFinish()" class="command"><i class="fa fa-check"></i> <%=textListGlobal[SESS_LANGUAGE][0]%></a></td>-->
                          <!--td nowrap width="5%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image101','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image101" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=textListGlobal[SESS_LANGUAGE][1]%>"></a></td-->
                          <!--<td class="command" nowrap width="65%"><a class="btn btn-primary" href="javascript:cmdBack()"><i class="fa fa-arrow-left"></i> <%=textListGlobal[SESS_LANGUAGE][1]%></a></td>-->
                        <!--</tr>
                      </table>
                    </td>-->
                        <td>
                  <div class="pull-left btn-test">
                    <a href="javascript:cmdFinish()" class="cls command btn btn-success"><i class="fa fa-send"></i> <%=textListGlobal[SESS_LANGUAGE][0]%></a>
                    <a href="javascript:cmdPO()" class="cls command btn btn-success"><i class="fa fa-send"></i> <%=textListGlobal[SESS_LANGUAGE][3]%></a>
                    <a href="javascript:cmdBack()" class="cls command btn btn-primary"><i class="fa fa-arrow-left"></i> <%=textListGlobal[SESS_LANGUAGE][1]%></a>
                  </div>
                        </td>
                  </tr>
                  <%
                  } else {
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
                          <td nowrap width="4%"><a href="javascript:cmdBackPo()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image101', '', '<%=approot%>/images/BtnBackOn.jpg', 1)" id="aSearch"><img name="Image101" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=textListGlobal[SESS_LANGUAGE][2]%>"></a></td>
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
                } catch (Exception eee) {
                  out.println("Form Exc : " + eee);
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
        <%if (menuUsed == MENU_ICON) {%>
        <%@include file="../../../styletemplate/footer.jsp" %>
        <%} else {%>
        <%@ include file = "../../../main/footer.jsp" %>
        <%}%>
        <!-- #EndEditable --> </td>
    </tr>
  </table>
		<script src="../../../styles/AdminLTE-2.3.11/plugins/jQuery/jquery-2.2.3.min.js"></script>
		<script src="../../../styles/AdminLTE-2.3.11/plugins/jQueryUI/jquery-ui.min.js"></script>
		<script src="../../../styles/AdminLTE-2.3.11/bootstrap/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="../../../styles/plugin/datatables/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="../../../styles/plugin/datatables/dataTables.bootstrap.min.js"></script>
		<script src="../../../styles/AdminLTE-2.3.11/dist/js/app.min.js"></script>			
</body>
<!-- #EndTemplate --></html>

