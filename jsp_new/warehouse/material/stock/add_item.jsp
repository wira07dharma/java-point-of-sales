
<%@ page language="java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   javax.mail.Session,
                   com.dimata.posbo.entity.warehouse.MatStockOpnameItem,
                   com.dimata.posbo.entity.masterdata.*,
                   com.dimata.posbo.entity.warehouse.MatStockOpname,
                   com.dimata.posbo.entity.warehouse.PstMatStockOpname,
                   com.dimata.posbo.entity.warehouse.PstMatStockOpnameItem" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package garment -->

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final String textListGlobal[][] = {
    {"Stok","Opname","Pencarian","Daftar","Edit","Tidak ada data opname ....","Cetak Opname","Batal"},
    {"Stock","Opname","Search","List","Edit","No opname data available ....","Print Opname","Cancel"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
    {"No","Lokasi","Tanggal","Waktu","Status","Supplier","Kategori","Sub Kategori","Keterangan","Status Dokumen"},
    {"No","Location","Date","Time","Status","Supplier","Category","Sub Category","Remark","Document Status"}
};

public static final String textListTableHeader[][] = {
    {"SKU","Nama Barang","Qty"},
    {"SKU","Goods Name","Qty"}
};

public static final String textListHotKey[][] = {
    {"HotKey","SKU - Mencari Barang","Qty - Simpan Barang",
     "Hapus Barang","Simpan dan kembali ke halaman utama","Pencarian Barang","Kosongkan SKU"},
    {"HotKey","SKU - Search Goods","Qty - Save Goods",
     "Delete Goods","Save and back to main page","Search Goods","Empty SKU"}
};

public Vector cekDelete(Vector vt, long oidMat){
    if(vt!=null && vt.size()>0){
        for(int i=0;i<vt.size();i++){
            Vector vtx = (Vector)vt.get(i);
            Material mat = (Material)vtx.get(0);
            if(mat.getOID()==oidMat){
                vt.removeElementAt(i);
                break;
            }
        }
    }
    return vt;
}

public int cekUpdate(Vector vt, long oidMat){
    int idx = -1;
    if(vt!=null && vt.size()>0){
        for(int i=0;i<vt.size();i++){
            Vector vtx = (Vector)vt.get(i);
            Material mat = (Material)vtx.get(0);
            if(mat.getOID()==oidMat){
                idx = i;
                break;
            }
        }
    }
    return idx;
}

public boolean cekVector(Vector itemVect, Material mat1){
    if(itemVect!=null && itemVect.size()>0){
        Vector vt = (Vector)itemVect.get(itemVect.size()-1);
        Material mat = (Material)vt.get(0);
        if(mat.getOID()==mat1.getOID())
            return false;
    }
    return true;
}
%>

<%
int iCommand = FRMQueryString.requestCommand(request);
String strBarcode = FRMQueryString.requestString(request, "TXT_BARCODE");
long oidProduct = FRMQueryString.requestLong(request, "TXT_PROD_OID");
int idx = FRMQueryString.requestInt(request, "TXT_IDX");
int recQty = FRMQueryString.requestInt(request, "TXT_QTY");
long oidMcdStockOpname = FRMQueryString.requestLong(request, "hidden_opname_id");
int type = FRMQueryString.requestInt(request, "type");
//iCommand=Command.LIST;
//oidMcdStockOpname = 504404241928410776L;

String SESS_ITEM = "SESS_ITEM";
String SESS_BARANG = "SESS_BARANG";
String SESS_OP_MAIN = "SESS_OP_MAIN";
String SESS_OP_DB = "SESS_OP_DB";

// variable
Vector vtitem = new Vector(1,1);
Vector vtBarang = new Vector(1,1);
Vector vtMain = new Vector(1,1);
Vector vtDb = new Vector(1,1);
Vector vtInsert = new Vector(1,1);
Vector vtUpdate = new Vector(1,1);
Vector vtDelete = new Vector(1,1);
MatStockOpname stockOpname = new MatStockOpname();
Category categ = new Category();

try{
    vtDb = (Vector)session.getValue(SESS_OP_DB);
    if(vtDb==null){
        vtDb = new Vector(1,1);
    }else{
        vtInsert = (Vector)vtDb.get(0);
        vtUpdate = (Vector)vtDb.get(1);
        vtDelete = (Vector)vtDb.get(2);
    }
}catch(Exception e){
    System.out.println(e.toString());
}

try{
    vtMain = (Vector)session.getValue(SESS_OP_MAIN);
    if(vtMain!=null){
        stockOpname = (MatStockOpname)vtMain.get(0);
        categ = (Category)vtMain.get(1);
    }
}catch(Exception e){
    stockOpname = new MatStockOpname();
    categ = new Category();
    System.out.println(e.toString());
}

if(vtMain==null || vtMain.size()==0 || stockOpname.getOID()!=oidMcdStockOpname){
    vtMain = new Vector(1,1);
    try{
        stockOpname = PstMatStockOpname.fetchExc(oidMcdStockOpname);
        categ = PstCategory.fetchExc(stockOpname.getCategoryId());
    }catch(Exception e){}
    vtMain.add(stockOpname);
    vtMain.add(categ);
    session.putValue(SESS_OP_MAIN,vtMain);
}
// ----------------------------
try{
    vtitem = (Vector)session.getValue(SESS_ITEM);
}catch(Exception e){
    System.out.println("=>>> Error get session ."+e.toString());
}
if(vtitem==null)
    vtitem = new Vector(1,1);

/**
 * ini pengambilan item opname ke database
 * yang aka di simpan ke vector dan session
 * dan siap di edit/yang mengikuti aturan di jsp.
 */
if(iCommand==Command.LIST){
    session.removeValue(SESS_ITEM);
    vtitem = new Vector(1,1);
    
    String where = PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID]+"="+oidMcdStockOpname;
    Vector dbItem = PstMatStockOpnameItem.list(0,0,where,"");
    if(dbItem!=null && dbItem.size()>0){
        for(int k=0;k<dbItem.size();k++){
            MatStockOpnameItem matStOpnameItem = (MatStockOpnameItem)dbItem.get(k);
            Material dbMat = new Material();
            try{
                dbMat = PstMaterial.fetchExc(matStOpnameItem.getMaterialId());
            }catch(Exception e){}
            Vector item = new Vector(1,1);
            item.add(dbMat);
            item.add(""+matStOpnameItem.getQtyOpname());
            vtitem.add(item);
        }
    }
    session.putValue(SESS_ITEM,vtitem);
}
//-------------

// vector barang
try{
    //vtBarang = (Vector)session.getValue(SESS_BARANG);
}catch(Exception e){
    System.out.println("=>>> Error get session barang."+e.toString());
}
/** list barang tidak disimpan pada session,
 *  ini untuk menghindari session yang berisi list barang yang tidak sesuai dengan kategori
 */
//if(vtBarang==null){
String whereClaueKategori = "";
if(stockOpname.getCategoryId() != 0) whereClaueKategori = PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]+"="+stockOpname.getCategoryId();
out.println(stockOpname.getCategoryId());
vtBarang = PstMaterial.list(0, 0, whereClaueKategori, "");
//session.putValue(SESS_BARANG,vtBarang);
//}

String namaBarang = "&nbsp;";
String strMsg = "";
if(iCommand==Command.ADD){
    boolean bool = false;
    for(int k=0;k<vtBarang.size();k++){
        Material mat = (Material)vtBarang.get(k);
        if(strBarcode.equals(mat.getSku())){
            namaBarang = mat.getName();
            oidProduct = mat.getOID();
            bool = true;
            break;
        }
    }
    if(!bool)
        strMsg = "SKU Barang tidak ada...";
    
}else if(iCommand==Command.SAVE){
    MatStockOpnameItem  matopnameItem = new MatStockOpnameItem();
    Material mat = new Material();
    try{
        mat = PstMaterial.fetchExc(oidProduct);
    }catch(Exception e){}
    
    Vector item = new Vector(1,1);
    item.add(mat);
    item.add(""+recQty);
    // simpan di vector dan di sesion
    if(idx<0){
        if(cekVector(vtitem,mat)){
            vtitem.add(item);
            // insert data yang di add/ data item baru
            vtInsert.add(item);
            vtDelete = cekDelete(vtDelete,oidProduct);
        }
    }else{
        vtitem.setElementAt(item,idx);
        // cek data yang di update
        int idxxx = cekUpdate(vtUpdate,oidProduct);
        if(idxxx<0){
            vtUpdate.add(vtitem.get(idx));
        }else{
            vtUpdate.setElementAt(vtitem.get(idx),idxxx);
        }
    }
    
    session.putValue(SESS_ITEM,vtitem);
    oidProduct = 0;
    strBarcode = "";
}else if(iCommand==Command.DELETE){
    vtDelete.add(vtitem.get(idx));
    vtitem.removeElementAt(idx);
    // cek data item yang akan di delete
    vtInsert = cekDelete(vtInsert,oidProduct);
    vtUpdate = cekDelete(vtUpdate,oidProduct);
    
    oidProduct = 0;
    strBarcode = "";
}

vtDb = new Vector(1,1);
vtDb.add(vtInsert);
vtDb.add(vtUpdate);
vtDb.add(vtDelete);
try{
    session.putValue(SESS_OP_DB,vtDb);
}catch(Exception e){}
System.out.println("=>> Insert : "+vtInsert);
System.out.println("==>> Update : "+vtUpdate);
System.out.println("==>> Delete : "+vtDelete);

if(iCommand==Command.EDIT){
    PstMatStockOpnameItem.insertUpdateDeleteAutomatic(vtDb,oidMcdStockOpname);
    // remove session
    session.removeValue(SESS_ITEM);
    session.removeValue(SESS_BARANG);
    session.removeValue(SESS_OP_MAIN);
    session.removeValue(SESS_OP_DB);
    
    response.sendRedirect(""+approot+"/warehouse/material/stock/mat_opname_store_quick_edit.jsp?hidden_opname_id="+oidMcdStockOpname+"&command="+Command.EDIT+"");
}
%>


<html>
<!-- #BeginTemplate "/Templates/maindosearch.dwt" -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function cmdenter(event){
    if (event.keyCode== 13) {
        document.frmadditem.command.value = "<%=Command.ADD%>";
    }else if (event.keyCode== 45) {
        saveitem();
    }else if(event.keyCode==36){
        var br = document.frmadditem.TXT_BARCODE.value;
        window.open("orderdosearch.jsp?command=<%=Command.LIST%>&bar_code="+br+"&category_id=<%=stockOpname.getCategoryId()%>", "orderitem", "height=500,width=700,scrollbars=yes, titlebar=no,status=no,toolbar=no,menubar=no,location=no");
    }
}

function cmdDelete(){
    document.frmadditem.command.value = "<%=Command.DELETE%>";
    document.frmadditem.action="add_item.jsp";
    document.frmadditem.submit();
}

function saveitem(){
    document.frmadditem.command.value = "<%=Command.EDIT%>";
    document.frmadditem.action="add_item.jsp";
    document.frmadditem.submit();
}

function back(){
    document.frmadditem.command.value = "<%=Command.EDIT%>";
    document.frmadditem.action="mat_opname_store_quick_edit.jsp";
    document.frmadditem.submit();
}

function moveLeft(nmr){
    nmr = parseInt(nmr) - 1;
    if(nmr>=0)
        document.frmadditem.TXT[nmr].focus();
}

function moveRight(nmr){
    nmr = parseInt(nmr) + 1;
    if(nmr<8)
        document.frmadditem.TXT[nmr].focus();
}

function cekKeyCode(nmr){
    //if(event.keyCode==37){
    //	moveLeft(nmr);
    //}else if(event.keyCode==39){
    //	moveRight(nmr);
    if(event.keyCode==13){
        document.frmadditem.command.value = "<%=Command.SAVE%>";
        document.frmadditem.action="add_item.jsp";
        document.frmadditem.submit();
    }else if(event.keyCode==46){
        document.frmadditem.command.value = "<%=Command.DELETE%>";
        document.frmadditem.action="add_item.jsp";
        document.frmadditem.submit();
    }else if(event.keyCode==110){
        document.frmadditem.command.value = "<%=Command.DELETE%>";
        document.frmadditem.action="add_item.jsp";
        document.frmadditem.submit();
    }else if(event.keyCode==45){
        saveitem();
    }
}

</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" --> <!-- #EndEditable --> <!-- #BeginEditable "stylestab" -->
<!-- #EndEditable --> <!-- #BeginEditable "headerscript" -->
<link rel="stylesheet" href="../../../styles/text.css" type="text/css">
<link href="../../../styles/main.css" rel="stylesheet" type="text/css">
<link href="../../../styles/tab.css" rel="stylesheet" type="text/css">
<!-- #EndEditable -->
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<!-- #BeginEditable "editfocus" --><!-- #EndEditable -->
<table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
  <tr>
    <td width="88%" valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20"><font color="#FF8080" face="Century Gothic"><big><strong><!-- #BeginEditable "contenttitle" -->
            <%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][4]%><br><hr size="1"><!-- #EndEditable --></strong></big></font></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form name="frmadditem" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="TXT_PROD_OID" value="<%=oidProduct%>">
              <input type="hidden" name="hidden_opname_id" value="<%=oidMcdStockOpname%>">
              <input type="hidden" name="type" value="<%=type%>">
              <div align="center">
              <table width="80%" border="0">
		<tr>
                  <td><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                  <td>: <b><%=stockOpname.getStockOpnameNumber()%></b></td>
                  <td><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                  <td>: <%=Formater.formatDate(stockOpname.getStockOpnameDate(),"dd-MMM-yyyy")%></td>
                </tr>
                <tr>
                  <td width="9%"><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
                  <td width="38%">: <%=categ.getName()%></td>
                  <td width="8%"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                  <td width="45%">: <%=stockOpname.getStockOpnameTime()%></td>
                </tr>
              </table>
              <table width="80%" border="0" cellpadding="0" cellspacing="1" class="listgen">
                <tr align="center" class="listgentitle">
                  <td><%=textListTableHeader[SESS_LANGUAGE][0]%></td>
                  <td><%=textListTableHeader[SESS_LANGUAGE][1]%></td>
                  <td><%=textListTableHeader[SESS_LANGUAGE][2]%></td>
                </tr>
                <%
        boolean bool = true;
        if(vtitem!=null && vtitem.size()>0){
            for(int i=0; i<vtitem.size();i++){
                Vector vtallses = (Vector)vtitem.get(i);
                Material material = (Material)vtallses.get(0);
                String qty = (String)vtallses.get(1);
                if(oidProduct!=material.getOID()){
		%>
                <tr class="listgensell">
                  <td width="152" align="left"><%=material.getSku()%></td>
                  <td width="444"><%=material.getName()%></td>
                  <td width="88" align="center">&nbsp;<%=qty%></td>
                </tr>
                <%
                }
                else {
                    bool = false;
                %>
                <input type="hidden" name="TXT_IDX" value="<%=i%>">
                <tr class="listgensell">
                  <td width="152" > <a name="TAD"></a> <input type="text" name="TXT_BARCODE2" readonly value="<%=material.getSku()%>" onKeyDown="javascript:cmdenter(event)" class="formElemen"></td>
                  <td width="444"><%=material.getName()%></td>
                  <td width="88" align="center">
                    <input type="text" name="TXT_QTY" size="10" onKeyDown="javascript:cekKeyCode(0)" value="<%=qty%>" class="formElemen">
                  </td>
                </tr>
                <%
                }
            }
        }       %>
                <%
                if(bool){
                %>
                <input type="hidden" name="TXT_IDX" value="-1">
                <tr class="listgensell">
                  <td width="152" > <a name="TAD"></a>
                    <input type="text" name="TXT_BARCODE" value="<%=strBarcode%>" onKeyDown="javascript:cmdenter(event)" class="formElemen">
                  </td>
                  <td width="444"><%=namaBarang%></td>
                  <td width="88" align="center">
                    <input type="text" name="TXT_QTY" size="10" onKeyDown="javascript:cekKeyCode(0)" class="formElemen"></td>
                </tr>
                <tr>
                  <td class="listgensell" colspan="2" >&nbsp;<i><%=strMsg%></i></td>
                  <td align="right" class="listgensell">&nbsp; <a href="javascript:back()"><font face="Courier New, Courier, mono" size="2">&lt;&lt; <%=textListGlobal[SESS_LANGUAGE][7]%></font> </a></td>
                </tr>
                <% } %>
                <input type="submit" value="Submit" style="width: 0; height: 0">
              </table>
              <div align="center">
                <table width="80%" border="0">
                  <tr>
                    <td width="15%">&nbsp;</td>
                    <td width="85%">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="15%"><font face="Arial, Helvetica, sans-serif" size="2"><strong><%=textListHotKey[SESS_LANGUAGE][0]%> :</strong></font></td>
                    <td width="85%">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="15%" align="right" valign="top"> <font face="Arial, Helvetica, sans-serif"><strong><font size="2">Enter</font></strong></font></td>
                    <td width="85%"><font face="Arial, Helvetica, sans-serif" size="2">=
                      <%=textListHotKey[SESS_LANGUAGE][1]%><br>
                      &nbsp;&nbsp;&nbsp;<%=textListHotKey[SESS_LANGUAGE][2]%></font>
                    </td>
                  </tr>
                  <tr>
                    <td width="15%" align="right" valign="top"> <font face="Arial, Helvetica, sans-serif"><strong><font size="2">Delete / Del</font></strong></font></td>
                    <td width="85%"><font face="Arial, Helvetica, sans-serif" size="2">= <%=textListHotKey[SESS_LANGUAGE][3]%></font></td>
                  </tr>
                  <tr>
                    <td width="15%" align="right" valign="top"><strong><font face="Arial, Helvetica, sans-serif" size="2">Insert / Ins</font></strong></td>
                    <td width="85%"><font face="Arial, Helvetica, sans-serif" size="2">= <%=textListHotKey[SESS_LANGUAGE][4]%></td>
                  </tr>
                  <tr>
                    <td width="15%" align="right" valign="top"><strong><font face="Arial, Helvetica, sans-serif" size="2">Home</font></strong></td>
                    <td width="85%"><font face="Arial, Helvetica, sans-serif" size="2">= <%=textListHotKey[SESS_LANGUAGE][5]%></font></td>
                  </tr>
                  <tr>
                    <td align="right" valign="top"><font face="Arial, Helvetica, sans-serif"><strong>Esc</strong></font></td>
                    <td><font face="Arial, Helvetica, sans-serif">= <%=textListHotKey[SESS_LANGUAGE][6]%></font></td>
                  </tr>
                </table>
              </div>
            </div>
            </form>
            <script language="JavaScript">
                window.location = "#TAD";
                if(<%=iCommand==Command.ADD && oidProduct!=0%>)
                    document.frmadditem.TXT_QTY.focus();
                else
                    document.frmadditem.TXT_BARCODE.focus();
                    
                if(<%=iCommand==Command.SAVE%>)
                    document.frmadditem.TXT_BARCODE.focus();
            </script>
            <!-- #EndEditable --></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
      <%//@ include file = "../../../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate -->
</html>
