<%-- 
    Document   : ajaxDataSource
    Created on : Jul 27, 2015, 3:10:28 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.posbo.entity.masterdata.TableRoom"%>
<%@page import="com.dimata.posbo.entity.masterdata.Room"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.MemberReg"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMemberReg"%>
<%@page import="com.dimata.posbo.entity.search.SrcMemberReg"%>
<%@page import="com.dimata.pos.form.billing.FrmBillMain"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@ include file = "../main/javainit.jsp" %>

<%!
    public String drawList(Vector objectClass){
        //2
        ControlList ctrlist = new ControlList();

        ctrlist.setAreaWidth("100%"); //untuk mengatur width(lebar) table
        ctrlist.setAreaStyle("listgen"); //untuk mengatur nama class table
        ctrlist.setTitleStyle("tableheader"); 
	//untuk mengatur nama class didalam kolom dalam baris table
	
        ctrlist.setCellStyle("cellStyle");
        ctrlist.setHeaderStyle("tableheader"); //mengatur nama class baris table
        ctrlist.addHeader("No", "20%");
        ctrlist.addHeader("Name", "20%");
        ctrlist.addHeader("Company", "20%");
	ctrlist.addHeader("");
        ctrlist.setLinkRow(0);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        ctrlist.reset();
        int index = -1;
        Vector rowx = new Vector(1, 1);
        for (int i = 0; i < objectClass.size(); i++) {
            Vector temp = (Vector)objectClass.get(i);
            MemberReg memberReg = (MemberReg)temp.get(0);
            String name = "";
            if(memberReg.getPersonName()!=null&&memberReg.getPersonName().length()>0)
            {
                    name = memberReg.getPersonName();
            }
            else
            {
                    name = memberReg.getCompName();
            }
	    int no = i+1;
            rowx = new Vector(1, 1);
            rowx.add(""+no);
            rowx.add(""+name);
            rowx.add(""+memberReg.getCompName());
            rowx.add("<center><input type='radio' data-options='{"
		    + "\"name\":\""+name+"\","
		    + "\"company\":\""+memberReg.getCompName()+"\","
		    + "\"oid\":\""+memberReg.getOID()+"\","
		    + "\"nationality\":\""+memberReg.getNationality()+"\"}\' "
		    + "value='"+name+"' name='memberInfo'></center>");
            lstData.add(rowx);
        }
        return ctrlist.drawBootstrapStripted();
    }


    public String drawListTakingOrder(Vector objectClass){
        //2
        ControlList ctrlist = new ControlList();

        ctrlist.setAreaWidth("100%"); //untuk mengatur width(lebar) table
        ctrlist.setAreaStyle("listgen"); //untuk mengatur nama class table
        ctrlist.setTitleStyle("tableheader"); 
	//untuk mengatur nama class didalam kolom dalam baris table
	
        ctrlist.setCellStyle("cellStyle");
        ctrlist.setHeaderStyle("tableheader"); //mengatur nama class baris table
        ctrlist.addHeader("No", "20%");
        ctrlist.addHeader("Name", "20%");
        ctrlist.addHeader("Telp", "20%");
	ctrlist.addHeader("");
        ctrlist.setLinkRow(0);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        ctrlist.reset();
        int index = -1;
        Vector rowx = new Vector(1, 1);
        for (int i = 0; i < objectClass.size(); i++) {
            Vector temp = (Vector)objectClass.get(i);
            MemberReg memberReg = (MemberReg)temp.get(0);
            String name = "";
            if(memberReg.getPersonName()!=null&&memberReg.getPersonName().length()>0)
            {
                    name = memberReg.getPersonName();
            }
            else
            {
                    name = memberReg.getCompName();
            }
	    int no = i+1;
            rowx = new Vector(1, 1);
            rowx.add(""+no);
            rowx.add(""+name);
            rowx.add(""+memberReg.getCompName());
            rowx.add("<center><input type='radio' data-options='{"
		    + "\"name\":\""+name+"\","
		    + "\"telp\":\""+memberReg.getHomeTelp()+"\","
		    + "\"oid\":\""+memberReg.getOID()+"\","
		    + "\"nationality\":\""+memberReg.getNationality()+"\"}\' "
		    + "value='"+name+"' name='memberInfo'></center>");
            lstData.add(rowx);
        }
        return ctrlist.drawBootstrapStripted();
    }

    public String drawListOpenBill(Vector objectClass){
        //2
        ControlList ctrlist = new ControlList();

        ctrlist.setAreaWidth("100%"); //untuk mengatur width(lebar) table
        ctrlist.setAreaStyle("listgen"); //untuk mengatur nama class table
        ctrlist.setTitleStyle("tableheader"); 
        ctrlist.setCellStyle("cellStyle");
        ctrlist.setHeaderStyle("tableheader"); //mengatur nama class baris table
        ctrlist.addHeader("No", "20%");
        ctrlist.addHeader("Name", "20%");
        ctrlist.addHeader("Telp", "20%");
	ctrlist.addHeader("Address");
        ctrlist.addHeader("");
        ctrlist.setLinkRow(0);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        ctrlist.reset();
        int index = -1;
        Vector rowx = new Vector(1, 1);
        for (int i = 0; i < objectClass.size(); i++) {
            Vector temp = (Vector)objectClass.get(i);
            MemberReg memberReg = (MemberReg)temp.get(0);
            String name = "";
            if(memberReg.getPersonName()!=null&&memberReg.getPersonName().length()>0)
            {
                    name = memberReg.getPersonName();
            }
            else
            {
                    name = memberReg.getCompName();
            }
	    int no = i+1;
            rowx = new Vector(1, 1);
            rowx.add(""+no);
            rowx.add(""+name);
            rowx.add(""+memberReg.getCompName());
            rowx.add(""+memberReg.getHomeAddr());
            rowx.add("<center><input type='radio' data-options='{"
		    + "\"name\":\""+name+"\","
		    + "\"telp\":\""+memberReg.getHomeTelp()+"\","
		    + "\"address\":\""+memberReg.getHomeAddr()+"\","
                    + "\"oid\":\""+memberReg.getOID()+"\","
		    + "\"nationality\":\""+memberReg.getNationality()+"\"}\' "
		    + "value='"+name+"' name='memberInfo'></center>");
            lstData.add(rowx);
        }
        return ctrlist.drawBootstrapStripted();
    }
    
    public String drawListPrintBill(Vector objectClass, int mul){
        //2
        ControlList ctrlist = new ControlList();

        ctrlist.setAreaWidth("100%"); //untuk mengatur width(lebar) table
        ctrlist.setAreaStyle("listgen"); //untuk mengatur nama class table
        ctrlist.setTitleStyle("tableheader"); 
	//untuk mengatur nama class didalam kolom dalam baris table
	
        ctrlist.setCellStyle("cellStyle");
        ctrlist.setHeaderStyle("tableheader"); //mengatur nama class baris table
        ctrlist.addHeader("No", "20%");
        ctrlist.addHeader("Name", "20%");
        ctrlist.addHeader("Qty", "20%");
        ctrlist.addHeader("Price", "20%");
        ctrlist.addHeader("Total", "20%");
        ctrlist.addHeader("Note", "20%");
        ctrlist.addHeader("Status", "20%");
        ctrlist.setLinkRow(0);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        ctrlist.reset();
        int index = -1;
        int startorderx=0;
        double amountTotalx=0.0;
        Vector rowx = new Vector(1, 1);
        for (int i = 0; i < objectClass.size(); i++) {
            Vector vt = (Vector) objectClass.get(i);
            Billdetail billdetail = (Billdetail) vt.get(0);
            startorderx = startorderx + 1;
            amountTotalx = amountTotalx + billdetail.getTotalPrice();
            String[] smartPhonesSplits = billdetail.getItemName().split("\\;");
            String nameMat = "";
            if (mul == 1) {
                try {
                    nameMat = smartPhonesSplits[0];
                } catch (Exception ex) {
                }
            } else {
                nameMat = billdetail.getItemName();
            }
            String statusItem = "";
            if (billdetail.getStatus() == 0) {
                statusItem = "Order";
            } else if (billdetail.getStatus() == 1) {
                statusItem = "Check Out";
            } else {
                statusItem = "Reserved";
            }
            rowx = new Vector(1, 1);
            rowx.add(""+startorderx);
            rowx.add(""+nameMat);
            rowx.add(""+billdetail.getQty());
            rowx.add(""+billdetail.getItemPrice());
            rowx.add(""+billdetail.getTotalPrice());
            rowx.add(""+billdetail.getNote());
            rowx.add(""+statusItem);
            lstData.add(rowx);
        }
        rowx = new Vector(1, 1);
        rowx.add(""+startorderx);
        rowx.add("<b>Total</b>");
        rowx.add("");
        rowx.add("");
        rowx.add(""+amountTotalx);
        rowx.add("");
        rowx.add("");
        lstData.add(rowx);
        return ctrlist.drawBootstrapStripted();
    }
%>

<%
    int pageShow = FRMQueryString.requestInt(request, "pageShow");
    String name = FRMQueryString.requestString(request, "studentName");
    String searchType = FRMQueryString.requestString(request, "searchType");
    long oid = FRMQueryString.requestLong(request, "oid");
    switch(pageShow){
	case 1:
            SrcMemberReg srcMemberReg = new SrcMemberReg();
            srcMemberReg.setName(name);
            Vector listMemberReg = SessMemberReg.searchMemberReg(srcMemberReg,0,10);
            
            if(searchType.equals("parent")){
            %>
                    <div class="row">
                            <div class="col-md-12">
                            <form id="searchList" name="searchList">
                                <input type="hidden" name="searchType" value="child">
                                <input type="hidden" name="pageShow" value="1">
                                <div class="col-md-1">
                                    <h5><b>Name</b></h5>
                                </div>
                                <div class="col-md-4">
                                    <input type="text" name="studentName" value="<%=name%>" class="form-control">
                                </div>
                                <div class="col-md-1">
                                    <button type="submit" class="btn btn-primary" id="searchBtn">
                                        <img src="../images/search.jpg" border="none" />
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="row">&nbsp;</div>
                <%}%>
                <div class="col-md-12">
                    <div class="row" id="resultSearch">
                        <%=drawList(listMemberReg)%>
                    </div>
                </div>
            <%
            break;
            
	case 2: 
            srcMemberReg = new SrcMemberReg();
            srcMemberReg.setName(name);
            listMemberReg = SessMemberReg.searchMemberDeliveryOrder(srcMemberReg,0,10);//PstBillMain.listOpenBill(0, 0,whereOpenBill, "");
            if(searchType.equals("parent")){
            %>
                    <div class="row">
                            <div class="col-md-12">
                            <form id="searchList" name="searchList">
                                <input type="hidden" name="searchType" value="child">
                                <input type="hidden" name="pageShow" value="2">
                                <div class="col-md-1">
                                    <h5><b>Name</b></h5>
                                </div>
                                <div class="col-md-4">
                                    <input type="text" name="studentName" value="<%=name%>" class="form-control">
                                </div>
                                <div class="col-md-1">
                                    <button type="submit" class="btn btn-primary" id="searchBtn">
                                        <img src="../images/search.jpg" border="none" />
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="row">&nbsp;</div>
                <%}else{%>
                    <link href="../styles/takingorder/css/bootstrap.css" rel="stylesheet" type="text/css">
                    <link href="../styles/takingorder/css/bootstrap_002.css" rel="stylesheet" type="text/css">
                <%}%>
                <div class="col-md-12">
                    <div class="row" id="resultSearch">
                        <%=drawListTakingOrder(listMemberReg)%>
                    </div>
                </div>
            <%
            break;
            
        case 3:  
            srcMemberReg = new SrcMemberReg();
            srcMemberReg.setName(name);
            listMemberReg = SessMemberReg.searchMemberDeliveryOrder(srcMemberReg,0,10);//PstBillMain.listOpenBill(0, 0,whereOpenBill, "");
            if(searchType.equals("parent")){
            %>
                 <div class="row">
                            <div class="col-md-12">
                            <form id="searchList" name="searchList">
                                <input type="hidden" name="searchType" value="child">
                                <input type="hidden" name="pageShow" value="3">
                                <div class="col-md-1">
                                    <h5><b>Name</b></h5>
                                </div>
                                <div class="col-md-4">
                                    <input type="text" name="studentName" value="<%=name%>" class="form-control">
                                </div>
                                <div class="col-md-1">
                                    <button type="submit" class="btn btn-primary" id="searchBtn">
                                        <img src="../images/search.jpg" border="none" />
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="row">&nbsp;</div>
                <%}else{%>
                    <link href="../styles/takingorder/css/bootstrap.css" rel="stylesheet" type="text/css">
                    <link href="../styles/takingorder/css/bootstrap_002.css" rel="stylesheet" type="text/css">
                <%}%>
                <div class="col-md-12">
                    <div class="row" id="resultSearch">
                        <%=drawListOpenBill(listMemberReg)%>
                    </div>
                </div>
            <%
           break;
        case 4:
            BillMain billMain = new BillMain();
            Vector listTransaction = new Vector();
            try {
                billMain = PstBillMain.fetchExc(oid);
                String whereOpenBill = " cbm.DOC_TYPE=0 AND cbm.TRANSACTION_TYPE=0 AND cbm.TRANSACTION_STATUS=1 AND cbm.TABLE_ID!=0 ";
                if (billMain.getTableId() != 0) {
                    whereOpenBill = whereOpenBill + " AND cbm.TABLE_ID=" + billMain.getTableId();
                }
                if (billMain.getRoomID() != 0) {
                    whereOpenBill = whereOpenBill + " AND cbm.ROOM_ID=" + billMain.getRoomID();
                }
                listTransaction = PstBillMain.listOpenBill(0, 0, whereOpenBill, "");
            } catch (Exception ex) {
            }
            Room room = new Room();
            TableRoom table = new TableRoom();
            for(int i = 0; i < listTransaction.size(); i++) {
            Vector dataBill = (Vector) listTransaction.get(0);
            billMain = (BillMain)dataBill.get(0);
            room = (Room) dataBill.get(1);
            table =(TableRoom)dataBill.get(2);
            }
            Vector listOrder = new Vector(1, 1);
            String whereOrder = "CD.CASH_BILL_MAIN_ID='" +oid+ "'";
            listOrder = PstBillDetail.listMat(0, 0, whereOrder, "");
            int startorderx = 0;
            double amountTotalx = 0;
            int multiLanguageName = Integer.parseInt((String) com.dimata.system.entity.PstSystemProperty.getValueIntByName("NAME_MATERIAL_MULTI_LANGUAGE"));
            %>
            <div class="guest" style="margin:10px 0px;">
            Name Guest :<%=billMain.getGuestName()%>
            <br>Rooom : <%=room.getName()%>
            <br>Meja :<%=table.getTableNumber()%>
            <br><br>
            <div class="col-md-12">
                <div class="row" id="resultSearch">
                    <%=drawListPrintBill(listOrder, multiLanguageName)%>
                </div>
            </div>
            <%--<div class="form-group">
                <div class="col-md-12" style="margin-bottom:10px;">
                    <table role="grid" class="tablesorter-dropbox tablesorter hasFilters">
                        <thead>
                            <tr role="row" class="tablesorter-headerRow">
                                <th aria-label="#: Ascending sort applied, activate to apply a descending sort" aria-sort="ascending" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerAsc" data-column="0" width="10%"><div class="tablesorter-header-inner">#<i class="tablesorter-icon"></i></div></th>
                                <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="1" width="50%"><div class="tablesorter-header-inner">Nama<i class="tablesorter-icon"></i></div></th>
                                <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="2" width="10%"><div class="tablesorter-header-inner">Qty<i class="tablesorter-icon"></i></div></th>
                                <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="3" width="15%"><div class="tablesorter-header-inner">Price<i class="tablesorter-icon"></i></div></th>
                                <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="4" width="20%"><div class="tablesorter-header-inner">Tot<i class="tablesorter-icon"></i></div></th>
                                <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="3" width="10%"><div class="tablesorter-header-inner">Note<i class="tablesorter-icon"></i></div></th>
                                <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="4" width="10%"><div class="tablesorter-header-inner">Status<i class="tablesorter-icon"></i></div></th>
                                <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="5" width="10%"><div class="tablesorter-header-inner">#<i class="tablesorter-icon"></i></div></th>
                            </tr>
                        </thead>
                        <tfoot>
                            <tr>
                                <th class="tablesorter-headerAsc" data-column="0" width="10%">#</th>
                                <th data-column="1" width="50%">Name</th>
                                <th data-column="2" width="10%">Qty</th>
                                <th data-column="3" width="15%">Price</th>
                                <th data-column="4" width="20%">Total</th>
                                <th data-column="3" width="10%">Note</th>
                                <th data-column="4" width="10%">Status</th>
                                <th data-column="5" width="10%">#</th>
                            </tr>
                        </tfoot>
                        <tbody aria-relevant="all" aria-live="polite">
                            <%
                                Vector listOrder = new Vector(1, 1);
                                String whereOrder = "CD.CASH_BILL_MAIN_ID='" +oid+ "'";
                                listOrder = PstBillDetail.listMat(0, 0, whereOrder, "");
                                int startorderx = 0;
                                double amountTotalx = 0;
                                int multiLanguageName = Integer.parseInt((String) com.dimata.system.entity.PstSystemProperty.getValueIntByName("NAME_MATERIAL_MULTI_LANGUAGE"));
                                for (int i = 0; i < listOrder.size(); i++) {
                                    Vector vt = (Vector) listOrder.get(i);
                                    Billdetail billdetail = (Billdetail) vt.get(0);
                                    startorderx = startorderx + 1;
                                    amountTotalx = amountTotalx + billdetail.getTotalPrice();
                                    String[] smartPhonesSplits = billdetail.getItemName().split("\\;");
                                    String nameMat = "";
                                    if (multiLanguageName == 1) {
                                        try {
                                            nameMat = smartPhonesSplits[0];
                                        } catch (Exception ex) {
                                        }
                                    } else {
                                        nameMat = billdetail.getItemName();
                                    }
                                    String statusItem = "";
                                    if (billdetail.getStatus() == 0) {
                                        statusItem = "Order";
                                    } else if (billdetail.getStatus() == 1) {
                                        statusItem = "Check Out";
                                    } else {
                                        statusItem = "Reserved";
                                    }
                            %>
                            <tr style="" class="tablesorter-hasChildRow odd">
                                <td style="text-align:center;"><a href="#" class="toggle"><%=startorderx%></a></td>
                                <td align="left"><a href="#" class="toggle"><%=nameMat%></a></td>
                                <td align="left"><a href="#" class="toggle"><%=billdetail.getQty()%></td>
                                <td align="left"><a href="#" class="toggle"><%=billdetail.getItemPrice()%></a></td>
                                <td align="left"><a href="#" class="toggle"><%=billdetail.getTotalPrice()%></a></td>
                                <td align="left"><a href="#" class="toggle"><%=billdetail.getNote()%></a></td>
                                <td align="left"><a href="#" class="toggle"><%=statusItem%></a></td>
                                <td align="left"></td>
                            </tr>
                            <%
                                }
                            %>
                            <tr style="" class="tablesorter-hasChildRow odd">
                                <td style="text-align:center;"><a href="#" class="toggle"></a></td>
                                <td align="left"><a href="#" class="toggle"></a>TOTAL PRICE</td>
                                <td align="left"><a href="#" class="toggle"></a></td>
                                <td style="text-align:left;"><a href="#" class="toggle"></a></td>
                                <td style="text-align:left;"><a href="#" class="toggle"></a><%=amountTotalx%></td>
                                <td style="text-align:left;"><a href="#" class="toggle"></a></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>--%>
        </div> 
            <%
            break;
	default:
	    
	break;
    }
%>

