
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
        {"Buat PO", "Kembali ke List", "Daftar PO","Mohon tunggu","Tutup"},
        {"Create PO", "Back to Minimum Stock", "PO List","Please wait","Close"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListMaterialHeader[][] =
    {//   0     1                  2              3            4                5                  6               7              8             9           10          11         12          13        14      15        16               17                                                                                  
        {"No","Kode Barang","Nama Barang","Qty Req.","Kode","Daftar Barang Order","Lokasi Order","Lokasi Request","Qty<br>Order","Harga","Approve&nbsp;&nbsp;","Note","Stok","Min. Stok","Unit","Harga","Supplier","Lokasi Request","Term"},
        {"No","Code Item","Product Name","Qty Req.","Code","Product Order List","Order Location","Location Request","Qty<br>Order","Price","Approve&nbsp;&nbsp;","Note","Stock","Min. Stock","Unit","Price","Vendor","Term"}
    };

    public String drawList(Vector list, int language, long location_oid_select, long supplier_id_select,long periodeId){
        ControlList ctrlist = new ControlList();

        // for lokasi
        boolean useSelectedLoc=false;
        String locationSelected="";
        long locationIdSelected=0;
        Vector val_lokasi = new Vector(1,1);
        Vector key_lokasi = new Vector(1,1);
        String where="";
        if (location_oid_select!=0){
            where= PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"='"+location_oid_select+"'";
        }
        Vector vt_lokasi = PstLocation.list(0, 0, where, PstLocation.fieldNames[PstLocation.FLD_NAME]);
        if(vt_lokasi!=null && vt_lokasi.size()>0){
            useSelectedLoc=true;
            for(int d=0; d<vt_lokasi.size(); d++){
                Location loc = (Location)vt_lokasi.get(d);
                String cntName = loc.getName();
                if (cntName.compareToIgnoreCase("'") >= 0) {
                    cntName = cntName.replace('\'','`');
                }
                val_lokasi.add(String.valueOf(loc.getOID()));
                key_lokasi.add(cntName);
                locationSelected=cntName;
                locationIdSelected=loc.getOID();
            }
        }

        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("form-control");
        ctrlist.setTitleStyle("form-control");
        ctrlist.setCellStyle("form-control");
        ctrlist.setHeaderStyle("form-control");

        ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][0]+"</div>","2%");
        ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][2]+"</div>","10%");
       // ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][7]+"</div>","9%");
        ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][3]+"</div>","5%");
        if(useSelectedLoc){
            //qty stock
            ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][12]+"</div>","5%");
            //qty minimum
            ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][13]+"</div>","5%");
        }

        ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][8]+"</div>","5%");
        ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][15]+"</div>","4%");//unit
        ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][4]+"</div>","10%");

        ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][16]+"</div>","10%");

        ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][9]+"</div>","10%");
        ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][10]+"</div>","5%");
        ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][11]+"</div>","15%");
        ctrlist.addHeader("<div align=\"center\">"+textListMaterialHeader[language][18]+"</div>","4%");//unit

        ctrlist.setLinkRow(0);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        int index = -1;

        long oidNewSupplier =Long.parseLong(PstSystemProperty.getValueByName("MAPPING_NEW_SUPPLIER_ID"));

        Vector val_supplier = new Vector(1,1);
        Vector key_supplier = new Vector(1,1);
        for (int i = 0; i < list.size(); i++){
                Vector v1 = (Vector)list.get(i);
                SessPurchaseWithPurchaseRequets material = (SessPurchaseWithPurchaseRequets)v1.get(0);
                double qty = Double.parseDouble((String)v1.get(1));
                String locationName=(String)v1.get(2);

                Vector rowx = new Vector();
                rowx.add(""+(i+1));
                rowx.add(String.valueOf("<div align = \"left\"><font size=\"2,5\">"+material.getName()+"</font></div>")); 

                rowx.add("<div align = \"center\">"+qty+"</div>");
                if(useSelectedLoc){ 
                    double currentStock = SessPurchaseRequest.getCurrentStok(locationIdSelected, material.getOidMaterial(), periodeId);
                    rowx.add("<div align = \"center\">"+currentStock+"</div>");   
                    double minimumStok = SessPurchaseRequest.getMinimumStock(locationIdSelected, material.getOidMaterial(), periodeId);
                    rowx.add("<div align = \"center\">"+minimumStok+"</div>");  
                }


                rowx.add("<div align = \"center\"><input type=\"text\" class=\"form-control-input-small\" name=\""+material.getPurchaseRequestID()+"\" size=\"10\" value="+qty+" align=\"left\"> " +material.getUnitKode()+
                                                " <input type=\"hidden\" name=\"pomaterial\" class=\"formElemen\" size=\"6\" value="+material.getOidMaterial()+ "> "+
                                                " <input type=\"hidden\" name=\"pomaterialID\" class=\"formElemen\" size=\"6\" value="+material.getPurchaseRequestID()+ "> "+
                                                " <input type=\"hidden\" name=\"txt_unit"+material.getPurchaseRequestID()+"\" class=\"form-control\" size=\"6\" value="+material.getStockUnitId()+"></div>"+
                                                " <input type=\"hidden\" name=\"txt_unit_request"+material.getPurchaseRequestID()+"\" class=\"form-control\" size=\"6\" value="+material.getStockUnitRequestId()+"></div>"
                                                + "<div class = \"hidden\">"+ControlCombo.drawBootsratap("location_order_name",null,String.valueOf(0),val_lokasi,key_lokasi,"hidden","form-control")+"</div>");

                //rowx.add("<div align = \"center\">"+material.getUnitKode()+"</div> " +"<div class = \"hidden\">"+ ControlCombo.drawBootsratap("location_order_name",null,String.valueOf(0),val_lokasi,key_lokasi,"hidden","form-control")+"</div>");

                rowx.add("<div align = \"center\">"+Formater.formatNumber(material.getPriceBuying(), "###,###")+"</div>"+
                         " <input type=\"hidden\" name=\"price"+material.getPurchaseRequestID()+"\" class=\"form-control\" size=\"10\" value="+material.getPriceBuying()+ "> ");

                //rowx.add(ControlCombo.drawBootsratap("location_order_name",null,String.valueOf(0),val_lokasi,key_lokasi,"","form-control"));

                // ini untuk pencarian Supplier
                // terhadap 1 barang
                val_supplier = new Vector(1,1);
                key_supplier = new Vector(1,1);
                long valSupplier=0;
                String keySupplier="";
                String whereSupp= PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]+"='"+material.getSupplierId()+"'";
                Vector vt_supp = PstContactList.list(0,0,whereSupp,"");

                String oid_supplier = "0";
                String namaSupplier="";
                String noSupplier ="";
                double priceContract=material.getTotalPriceBuying();
                if(oidNewSupplier!=material.getSupplierId()){
                    if(vt_supp!=null && vt_supp.size()>0){
                        for(int d=0; d<vt_supp.size(); d++){
                            ContactList cnt = (ContactList)vt_supp.get(d);
                            String cntName = cnt.getCompName();
                            String cntNo = cnt.getContactCode();
                            if(cntName.length()==0){
                                cntName = cnt.getPersonName()+" "+cnt.getPersonLastname();
                            }
                            if (cntName.compareToIgnoreCase("'") >= 0) {
                                cntName = cntName.replace('\'','`');
                            }
                            val_supplier.add(String.valueOf(cnt.getOID()));
                            key_supplier.add(cntNo);
                            namaSupplier=cntName;
                            noSupplier = cntNo;

                            //
                            valSupplier=cnt.getOID();
                            keySupplier=cntNo;
                        }
                    }
                }else{
                        val_supplier.add(""+oidNewSupplier);
                        key_supplier.add("New Supplier");
                        namaSupplier=material.getSupplierName();
                        valSupplier= oidNewSupplier;
                        keySupplier="New Supplier";
                }
                //rowx.add(ControlCombo.drawBootsratap("supplier_order_name",null,""+material.getSupplierId(),val_supplier,key_supplier,"onChange=\"javascript:changeContractPrice(this.value,'"+material.getOidMaterial()+"')\"","form-control"));
                rowx.add("<div align = \"center\">"+keySupplier+"</div>"+
                         " <input type=\"hidden\" name=\"supplier_order_name\" class=\"form-control\" size=\"6\" value="+valSupplier+ "> ");

                rowx.add("<div align = \"center\">"+namaSupplier+"</div>"+
                         " <input type=\"hidden\" name=\"nameSupplier"+material.getPurchaseRequestID()+"\" class=\"form-control\" size=\"6\" value="+namaSupplier+ "> ");

                //cek harga
                rowx.add("<div align = \"center\"><input type=\"text\" class=\"form-control\" name=\"priceContract"+material.getPurchaseRequestID()+"\" size=\"7\" value=\""+Formater.formatNumber(priceContract, "###,###") +"\" align=\"left\"></div>");

                //buat kondisi yes/no
                Vector val_terms = new Vector(1,1);
                Vector key_terms = new Vector(1,1);
                for(int d=1; d<PstPurchaseOrder.fieldsApprovalType.length; d++){
                    val_terms.add(String.valueOf(d));
                    key_terms.add(PstPurchaseOrder.fieldsApprovalType[d]);
                }

                rowx.add(ControlCombo.drawBootsratap("approval_request_order",null,"1",val_terms,key_terms,"","form-control"));

                rowx.add("<div align = \"center\"><textarea class=\"form-control\" name=\"note\" size=\"30\" value=\"\" align=\"left\"></textarea></div>");
                 rowx.add(""+PstPurchaseOrder.fieldsPurchaseRequestType[material.getTermOf()]+""
                        +  "<input type=\"hidden\" name=\"term_of_payment\" class=\"formElemen\" size=\"6\" value="+material.getTermOf()+ ">" );
                lstData.add(rowx);
        }
        return ctrlist.drawBootstrap();
    }
    
    public String drawList2(Vector list, int language, long location_oid_select, long supplier_id_select,long periodeId){
        ControlList ctrlist = new ControlList();
        String container="";
        // for lokasi
        boolean useSelectedLoc=false;
        String locationSelected="";
        long locationIdSelected=0;
        Vector val_lokasi = new Vector(1,1);
        Vector key_lokasi = new Vector(1,1);
        String where="";
        int no =0;
        if (location_oid_select!=0){
            where= PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"='"+location_oid_select+"'";
        }
        Vector vt_lokasi = PstLocation.list(0, 0, where, PstLocation.fieldNames[PstLocation.FLD_NAME]);
        if(vt_lokasi!=null && vt_lokasi.size()>0){
            useSelectedLoc=true;
            for(int d=0; d<vt_lokasi.size(); d++){
                Location loc = (Location)vt_lokasi.get(d);
                String cntName = loc.getName();
                if (cntName.compareToIgnoreCase("'") >= 0) {
                    cntName = cntName.replace('\'','`');
                }
                val_lokasi.add(String.valueOf(loc.getOID()));
                key_lokasi.add(cntName);
                locationSelected=cntName;
                locationIdSelected=loc.getOID();
            }
        }
        
        container += "<table class='table table-hover table-bordered' style='font-size:0.85em'>";
        container += "<thead style='font-weight:bold;text-align:center; '>";
        container += "<tr>";
        container += "<td>"+textListMaterialHeader[language][0]+"</td>";
        container += "<td>"+textListMaterialHeader[language][2]+"</td>";
        container += "<td>"+textListMaterialHeader[language][3]+"</td>";
        if(useSelectedLoc){
            container += "<td>"+textListMaterialHeader[language][12]+"</td>";
            container += "<td>"+textListMaterialHeader[language][13]+"</td>";
        }
        container += "<td>"+textListMaterialHeader[language][8]+"</td>";
        container += "<td>"+textListMaterialHeader[language][15]+"</td>";
        container += "<td>"+textListMaterialHeader[language][4]+"</td>";
        container += "<td>"+textListMaterialHeader[language][16]+"</td>";
        container += "<td>"+textListMaterialHeader[language][9]+"</td>";
        container += "<td>"+textListMaterialHeader[language][10]+"</td>";
        container += "<td>"+textListMaterialHeader[language][11]+"</td>";
        container += "<td>"+textListMaterialHeader[language][18]+"</td>";
        container += "</tr>";
        container += "</thead>";
        container += "<tbody>";
        
        long oidNewSupplier =Long.parseLong(PstSystemProperty.getValueByName("MAPPING_NEW_SUPPLIER_ID"));
        Vector val_supplier = new Vector(1,1);
        Vector key_supplier = new Vector(1,1);
        
        for (int i = 0; i < list.size(); i++){
            Vector v1 = (Vector)list.get(i);
            SessPurchaseWithPurchaseRequets material = (SessPurchaseWithPurchaseRequets)v1.get(0);
            double qty = Double.parseDouble((String)v1.get(1));
            String locationName=(String)v1.get(2);
            container += "<tr>";
            no =i + 1;
            container += "<td>"+no+"</td>";
            container += "<td>"+material.getName()+"</td>";
            container += "<td>"+qty+"</td>";
            if(useSelectedLoc){ 
                double currentStock = SessPurchaseRequest.getCurrentStok(locationIdSelected, material.getOidMaterial(), periodeId);
                container += "<td>"+currentStock+"</td>";
                double minimumStok = SessPurchaseRequest.getMinimumStock(locationIdSelected, material.getOidMaterial(), periodeId);
                container += "<td>"+minimumStok+"</td>";
            }
            container += "<td>"
  
                            + "<input  type=\"text\" style='width:35%'  name=\""+material.getPurchaseRequestID()+"\"  value="+qty+" align=\"left\"> " 
                            + "<span>"
                            +material.getUnitKode()+
                            "</span>"+
                            " <input type=\"hidden\" name=\"pomaterial\" class=\"formElemen\" size=\"6\" value="+material.getOidMaterial()+ "> "+
                            " <input type=\"hidden\" name=\"pomaterialID\" class=\"formElemen\" size=\"6\" value="+material.getPurchaseRequestID()+ "> "+
                            " <input type=\"hidden\" name=\"txt_unit"+material.getPurchaseRequestID()+"\" class=\"form-control\" size=\"6\" value="+material.getStockUnitId()+"></div>"+
                            " <input type=\"hidden\" name=\"txt_unit_request"+material.getPurchaseRequestID()+"\" class=\"form-control\" size=\"6\" value="+material.getStockUnitRequestId()+"></div>"
                            + "<div class = \"hidden\">"+ControlCombo.drawBootsratap("location_order_name",null,String.valueOf(0),val_lokasi,key_lokasi,"hidden","form-control")+""
                        + "</td>";
            container += "<td>"+Formater.formatNumber(material.getPriceBuying(), "###,###")+"</td><input type=\"hidden\" name=\"price"+material.getPurchaseRequestID()+"\" class=\"form-control\" size=\"10\" value="+material.getPriceBuying()+ "> ";
            val_supplier = new Vector(1,1);
            key_supplier = new Vector(1,1);
            long valSupplier=0;
            String keySupplier="";
            String whereSupp= PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]+"='"+material.getSupplierId()+"'";
            Vector vt_supp = PstContactList.list(0,0,whereSupp,"");

            String oid_supplier = "0";
            String namaSupplier="";
            String noSupplier ="";
            double priceContract=material.getTotalPriceBuying();
            if(oidNewSupplier!=material.getSupplierId()){
                if(vt_supp!=null && vt_supp.size()>0){
                    for(int d=0; d<vt_supp.size(); d++){
                        ContactList cnt = (ContactList)vt_supp.get(d);
                        String cntName = cnt.getCompName();
                        String cntNo = cnt.getContactCode();
                        if(cntName.length()==0){
                            cntName = cnt.getPersonName()+" "+cnt.getPersonLastname();
                        }
                        if (cntName.compareToIgnoreCase("'") >= 0) {
                            cntName = cntName.replace('\'','`');
                        }
                        val_supplier.add(String.valueOf(cnt.getOID()));
                        key_supplier.add(cntNo);
                        namaSupplier=cntName;
                        noSupplier = cntNo;
                        valSupplier=cnt.getOID();
                        keySupplier=cntNo;
                    }
                }
            }else{
                val_supplier.add(""+oidNewSupplier);
                key_supplier.add("New Supplier");
                namaSupplier=material.getSupplierName();
                valSupplier= oidNewSupplier;
                keySupplier="New Supplier";
            }
            
            container += "<td>"+keySupplier+"</td><input type=\"hidden\" name=\"supplier_order_name\"  size=\"6\" value="+valSupplier+ "> ";
            container += "<td>"+namaSupplier+"</td><input type=\"hidden\" name=\"nameSupplier"+material.getPurchaseRequestID()+"\" class=\"form-control\" size=\"6\" value="+namaSupplier+ "> ";
            container += "<td><input type=\"text\"  name=\"priceContract"+material.getPurchaseRequestID()+"\" size=\"7\" value=\""+Formater.formatNumber(priceContract, "###,###") +"\" align=\"left\"></td>";
            
            Vector val_terms = new Vector(1,1);
            Vector key_terms = new Vector(1,1);
            for(int d=1; d<PstPurchaseOrder.fieldsApprovalType.length; d++){
                val_terms.add(String.valueOf(d));
                key_terms.add(PstPurchaseOrder.fieldsApprovalType[d]);
            }
            container += "<td>"+ControlCombo.drawBootsratap("approval_request_order",null,"1",val_terms,key_terms,"","")+"</td>";
            container += "<td><textarea  name=\"note\" size=\"30\" value=\"\" align=\"left\"></textarea></td>";
            container += "<td>"+PstPurchaseOrder.fieldsPurchaseRequestType[material.getTermOf()]+"</td><input type=\"hidden\" name=\"term_of_payment\" class=\"formElemen\" size=\"6\" value="+material.getTermOf()+ ">";
            container += "</tr>";
        } 
        container += "</tbody>";
        container += "</table>";

        return container;
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
    String approval[]=request.getParameterValues("approval_request_order");
    String txtterm[] = request.getParameterValues("term_of_payment");
    long documentPrId=FRMQueryString.requestLong(request, "documentPrId");
    int termOfPayment= FRMQueryString.requestInt(request, "termOfPayment");
    
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
<script language="JavaScript">
        function changeContractPrice(contactId, materialId){
            checkAjax(contactId,materialId);
        }
        
        function checkAjax(contactId,materialId){
            var nameInput="priceContract"+materialId;
            alert("ccc "+nameInput);
            $.ajax({
            url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckPriceContract?contact_id="+contactId+"&material_id="+materialId+"",
            type : "POST",
            async : false,
            success : function(data) {
                alert("Harga : "+data);
                document.frm_newpo.priceContract.value=data;
            }
        });

        }
        
        
	function cmdSave()
	{
		document.frm_newpo.command.value="<%=Command.SAVE%>";
		document.frm_newpo.action="m_create_pofrompr.jsp";
		document.frm_newpo.submit();
	}

	function cmdFinish() {
		var suppId = document.frm_newpo.supplier_order_name.value;
			var x = confirm("Are you sure ?");
			if(x){
				document.frm_newpo.command.value="<%=Command.SAVE%>";
				document.frm_newpo.finish.value="1";
				document.frm_newpo.action="m_create_pofrompr.jsp";
				document.frm_newpo.submit();
			}
    }

	function cmdBack() {
		document.frm_newpo.command.value="<%=Command.BACK%>";
		document.frm_newpo.action=  "../../../warehouse/material/report/reportstockmin_list.jsp";// "srcpomaterial.jsp";
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
<meta charset="UTF-8"> 
    <title>AdminLTE | Dashboard</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
    <link href="../../../styles/bootstrap3.1/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <link href="../../../styles/bootstrap3.1/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <link href="../../../styles/bootstrap3.1/css/ionicons.min.css" rel="stylesheet" type="text/css" />
    <link href="../../../styles/bootstrap3.1/css/morris/morris.css" rel="stylesheet" type="text/css" />
    <link href="../../../styles/bootstrap3.1/css/jvectormap/jquery-jvectormap-1.2.2.css" rel="stylesheet" type="text/css" />
    <link href="../../../styles/bootstrap3.1/css/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" rel="stylesheet" type="text/css" />
    <link href="../../../styles/bootstrap3.1/css/AdminLTE.css" rel="stylesheet" type="text/css" />
    
</head> 

<body class="skin-blue">
    <%@ include file = "../../../header_mobile.jsp" %> 
    <div class="wrapper row-offcanvas row-offcanvas-left">    
    <!-- Left side column. contains the logo and sidebar -->
    <%@ include file = "../../../menu_left_mobile.jsp" %> 
    <aside class="right-side">
    <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                <small> <%=textListMaterialHeader[SESS_LANGUAGE][5]%> <%=PstPurchaseOrder.fieldsPurchaseRequestType[termOfPayment]%></small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                <li class="active">Dashboard</li>
            </ol>
        </section>
        <!-- Main content -->
        <section class="content">
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
                    <div class="box-body">
                        <div class="box-body">
                            <div class="row">
                                <div class="form-group">
                                    <%      
                                    boolean useSelectedLoc=false;
                                    String locationSelected="";
                                    long locationIdSelected=0;
                                    Vector val_lokasi = new Vector(1,1);
                                    Vector key_lokasi = new Vector(1,1);
                                    String where="";

                                    Vector vt_lokasi = PstLocation.list(0, 0, where, PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                    if(vt_lokasi!=null && vt_lokasi.size()>0){
                                        useSelectedLoc=true;
                                        for(int d=0; d<vt_lokasi.size(); d++){
                                            Location loc = (Location)vt_lokasi.get(d);
                                            String cntName = loc.getName();
                                            if (cntName.compareToIgnoreCase("'") >= 0) {
                                                cntName = cntName.replace('\'','`');
                                            }
                                            val_lokasi.add(String.valueOf(loc.getOID()));
                                            key_lokasi.add(cntName);
                                            locationSelected=cntName;
                                            locationIdSelected=loc.getOID();
                                        }
                                    }
                                    %>
                                   <% 
                                    if (finish != 1){
                                       for (int i = 0; i < list.size(); i++){
                                        Vector v1 = (Vector)list.get(i);
                                        SessPurchaseWithPurchaseRequets material = (SessPurchaseWithPurchaseRequets)v1.get(0);
                                        double qty = Double.parseDouble((String)v1.get(1));
                                        String locationName=(String)v1.get(2);
                                            if(useSelectedLoc){%>
                                                &nbsp;&nbsp;&nbsp;  <%=textListMaterialHeader[SESS_LANGUAGE][17]%> &nbsp;&nbsp;&nbsp; : &nbsp; <%=locationSelected %>

                                            <% 
                                            }else{%>
                                               &nbsp;&nbsp;&nbsp;<%=(String)v1.get(2)%>
                                            <%} 
                                        break;
                                      }
                                 }   
                                %>
                                </div>
                                <%
                                if (finish != 1){
                                        if(list != null && list.size()>0){
                                        try{
                                            %>
                                    <div class="col-md-12">
                                         <%= drawList2(list,SESS_LANGUAGE,location_id_select,supplier_id_select,periodeId)%>
                                    </div>
                                   
                                    <%
                                            }
                                        catch(Exception exc){
                                                System.out.println("DrawList : " + exc.toString());
                                        }
                                    }
                                %>
                            </div> 
                            <div class="row"></div>
                            <div class="box-footer">
                                <button id="createPo"  onclick="javascript:cmdFinish()" type="submit" class="btn btn-primary">Buat PO</button>
                                <button type="submit" onclick="javascript:cmdBack()" class="btn btn-primary pull-right" >Kembali Ke List</button>
                            </div>
                            <%
                                }else{
                            %>
                            <div class="col-md-12">

                                <span class="comment" bgcolor="#CCCCCC"> Proses success ... </span>
                            </div>
                            <div class="col-md-12">
                                <button  onclick="javascript:cmdBackPo()" type="submit" class="btn btn-primary">Back PO</button>
                            </div>
                       </div>
                    </div>
                                    <%
                                    }
                                    %>
                </form>

                                <script language="JavaScript">
                                window.focus();
                                </script>
            </section><!-- /.content -->

        </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->
        <!-- jQuery 2.0.2 -->
         <script src="../../../styles/bootstrap3.1/libs/jquery.min.js"></script>
        <!-- jQuery UI 1.10.3 -->
        <script src="../../../styles/bootstrap3.1/js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
        <!-- Bootstrap -->
        <script src="../../../styles/bootstrap3.1/js/bootstrap.min.js" type="text/javascript"></script>
        <style>
           
        </style>
        <script type="text/javascript">
            $(document).ready(function(){
                $('#createPo').click(function(){
                    $('#modalLoading').modal({backdrop: 'static', keyboard: false});
                });
            });
        </script>
        
        <div id="modalLoading" class="modal fade" tabindex="-1">
            <div class="modal-dialog modal-sm" style="max-width: 400px;">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title" id="modal-title"><%= textListGlobal[SESS_LANGUAGE][3]%>...</h4>
                    </div>
                    <div class="modal-body" id="modal-body">
                        <center><img src="../../../imgstyle/loading.gif"></center>
                    </div>
                    <div class="modal-footer">
                        
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

