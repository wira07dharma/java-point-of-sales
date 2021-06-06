<%-- 
    Document   : report_insentif
    Created on : Mar 2, 2018, 2:18:28 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.masterdata.InsentifData"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstInsentifData"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.posbo.session.masterdata.SessSales"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstSales"%>
<%@page import="com.dimata.harisma.entity.employee.Employee"%>
<%@page import="com.dimata.harisma.entity.employee.PstEmployee"%>
<%@page import="com.dimata.harisma.entity.masterdata.Position"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstPosition"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!//    
    int sortByTgl = 1;
    int sortByNama = 2;
    int groupByTgl = 1;
    int groupByPosisi = 2;
%>

<%//    
    int iCommand = FRMQueryString.requestCommand(request);
    String tglAwal = FRMQueryString.requestString(request, "FRM_TGL_AWAL");
    String tglAkhir = FRMQueryString.requestString(request, "FRM_TGL_AKHIR");
    String selectedPosisi[] = request.getParameterValues("FRM_POSISI");
    String selectedPegawai[] = request.getParameterValues("FRM_PEGAWAI");
    int sortBy = FRMQueryString.requestInt(request, "FRM_SORT_BY");
    int groupBy = FRMQueryString.requestInt(request, "FRM_GROUP_BY");

    int error = 0;
    String messaage = "";

    String today = "" + Formater.formatDate(new Date(), "yyyy-MM-dd");
    if (iCommand == Command.NONE) {
        tglAwal = today;
        tglAkhir = today;
    }

    String tglShow = "";
    String sortShow = "";
    String groupShow = "";
    String posisiShow = "";
    String pegawaiShow = "";

    //hasil pencarian
    Vector listSalesInsentif = new Vector();
    if (iCommand == Command.LIST) {

        String where = "bm." + PstBillMain.fieldNames[PstBillMain.FLD_IS_SERVICE] + " <> 1";
        if (!tglAwal.equals("") && !tglAkhir.equals("")) {
            where += " AND DATE(bm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") BETWEEN '" + tglAwal + "' AND '" + tglAkhir + "'";
        }

        if (!tglAwal.equals("") && !tglAkhir.equals("")) {
            Date dAwal = Formater.formatDate(tglAwal, "yyyy-MM-dd");
            Date dAkhir = Formater.formatDate(tglAkhir, "yyyy-MM-dd");
            int compareDate = dAwal.compareTo(dAkhir);

            if (compareDate == 0) {
                tglShow = tglAwal;
            } else if (compareDate < 0) {
                tglShow = tglAwal + " s.d. " + tglAkhir;
            } else if (compareDate > 0) {
                tglShow = "<b class='text-red'>Tanggal awal tidak boleh lebih besar dari tanggal akhir</b>";
                error += 1;
            }
        } else {
            tglShow = "<b class='text-red'>Tanggal harus diisi</b>";
            error += 1;
        }

        if (sortBy == sortByTgl) {
            sortShow = "Tanggal";
        } else if (sortBy == sortByNama) {
            sortShow = "Nama pegawai";
        }

        if (groupBy == groupByTgl) {
            groupShow = "Tanggal";
        } else if (groupBy == groupByPosisi) {
            groupShow = "Posisi";
        }

        String wherePosition = "";
        if (selectedPosisi == null) {
            posisiShow = "Semua posisi";
        } else {
            for (int i = 0; i < selectedPosisi.length; i++) {
                try {
                    Position p = PstPosition.fetchExc(Long.valueOf(selectedPosisi[i]));
                    if (i > 0) {
                        posisiShow += ", " + p.getPosition();
                        wherePosition += "," + p.getOID();
                    } else {
                        posisiShow += "" + p.getPosition();
                        wherePosition += "" + p.getOID();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (wherePosition.length() > 0) {
                if (where.length() > 0) {
                    where += " AND ";
                }
                where += "e." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " IN (" + wherePosition + ")";
            }
        }

        String wherePegawai = "";
        if (selectedPegawai == null) {
            pegawaiShow = "Semua pegawai";
        } else {
            for (int i = 0; i < selectedPegawai.length; i++) {
                try {
                    Employee e = PstEmployee.fetchExc(Long.valueOf(selectedPegawai[i]));
                    if (i > 0) {
                        pegawaiShow += ", " + e.getFullName();
                        wherePegawai += "," + e.getOID();
                    } else {
                        pegawaiShow += "" + e.getFullName();
                        wherePegawai += "" + e.getOID();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (wherePegawai.length() > 0) {
                if (where.length() > 0) {
                    where += " AND ";
                }
                where += "e." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " IN (" + wherePegawai + ")";
            }
        }

        String group = "";
        if (groupBy == groupByTgl) {
            group += " DATE(bm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ")";
            group += ", e." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
        } else if (groupBy == groupByPosisi) {
            group += " e." + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID];
            group += ", e." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
        }

        String order = "";
        if (sortBy == sortByTgl) {
            order += " bm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
        } else if (sortBy == sortByNama) {
            order += " e." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME];
        }

        where += "GROUP BY " + group;

        listSalesInsentif = SessSales.getReportInsentif(0, 0, where, order);
    }

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/AdminLTE.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/font-awesome/font-awesome.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/select2/css/select2.min.css" />
        <link rel="stylesheet" media="screen" href="../../../styles/datepicker/bootstrap-datetimepicker.min.css"/>
        <style>
            th {font-size: 12px}
            td {font-size: 12px}
            
            #tabel_header td {padding-bottom: 10px;padding-right: 10px}
            
            .tabel_data th {padding: 4px 8px !important}
            .tabel_data td {padding: 4px 8px !important}
        </style>
        
        <script type="text/javascript" src="../../../styles/bootstrap/js/jquery.min.js"></script>
        <script type="text/javascript" src="../../../styles/bootstrap/js/bootstrap.min.js"></script>  
        <script type="text/javascript" src="../../../styles/dimata-app.js"></script>
        <script type="text/javascript" src="../../../styles/select2/js/select2.min.js"></script>
        <script type="text/javascript" src="../../../styles/datepicker/bootstrap-datetimepicker.js" charset="UTF-8"></script>
        <script>
            $(document).ready(function () {
                
                $('.datepick').datetimepicker({
                    autoclose: true,
                    todayBtn: true,
                    format: 'yyyy-mm-dd',
                    minView: 2
                });
                
                $('.selects2').select2({
                    placeholder: "Semua",                    
                });
                
                $('#btnPrint').click(function(){
                    $(this).attr({"disabled" : true}).html("<i class='fa fa-spinner fa-pulse'></i>&nbsp; Tunggu...");
                    var data = $('#formSearch').serialize();
                    window.open("print_out_report_insentif.jsp?"+data,"report_insentif");
                    $(this).removeAttr("disabled").html("<i class='fa fa-print'></i>&nbsp; Cetak");
                });
                
                $('#formSearch').submit(function() {
                    $('#btnSrc').attr({"disabled" : true}).html("<i class='fa fa-spinner fa-pulse'></i>&nbsp; Tunggu...");
                });
                
            });
        </script>
    </head>
    <body style="background-color: #eeeeee">
        <br>
        <div class="col-md-12">
            <div class="box box-primary">

                <div class="box-header with-border" style="border-color: lightgray">
                    <h3 class="box-title">Form Pencarian</h3>
                </div>

                <form method="POST" id="formSearch" class="form-horizontal" role="form" action="?command=<%=Command.LIST%>">
                    <input type="hidden" name="command" id="command" value="<%=iCommand%>">

                    <p></p>
                    <div class="box-body">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Tanggal</label>
                            <div class="col-sm-3">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>                                    
                                    <input type="text" placeholder="Tanggal awal" required="" name="FRM_TGL_AWAL" class="input-sm form-control datepick" value="<%=tglAwal%>">
                                </div>                                
                            </div>
                            <div class="col-sm-3">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" placeholder="Tanggal akhir" required="" name="FRM_TGL_AKHIR" class="input-sm form-control datepick" value="<%=tglAkhir%>">
                                </div>
                            </div>
                            <!---->
                            <label class="col-sm-1 control-label">Sort By</label>
                            <div class="col-sm-2">
                                <select name="FRM_SORT_BY" class="form-control input-sm">                                    
                                    <option <%=sortBy == sortByTgl ? "selected" : ""%> value="<%=sortByTgl%>">Tanggal</option>                                    
                                    <option <%=sortBy == sortByNama ? "selected" : ""%> value="<%=sortByNama%>">Nama Pegawai</option>
                                </select>
                            </div>
                        </div>
                        <!---->
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Posisi</label>
                            <div class="col-sm-6">
                                <select multiple="" name="FRM_POSISI" class="form-control input-sm selects2" style="width: 100%">
                                    <%
                                        Vector listPosisi = PstPosition.list(0, 0, "", ""+PstPosition.fieldNames[PstPosition.FLD_POSITION]);
                                        for (int i = 0; i < listPosisi.size(); i++) {
                                            Position p = (Position) listPosisi.get(i);
                                            String selected = "";
                                            if (selectedPosisi != null) {
                                                for (int j = 0; j < selectedPosisi.length; j++) {
                                                    Position p2 = PstPosition.fetchExc(Long.valueOf(selectedPosisi[j]));
                                                    if (p2.getOID() == p.getOID()) {
                                                        selected = "selected";
                                                    }
                                                }
                                            }
                                    %>
                                    <option <%=selected%> value="<%=p.getOID()%>"><%=p.getPosition()%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <!---->
                            <label class="col-sm-1 control-label">Group&nbsp;By</label>
                            <div class="col-sm-2">
                                <select name="FRM_GROUP_BY" class="form-control input-sm">                                    
                                    <option <%=groupBy == groupByTgl ? "selected" : ""%> value="<%=groupByTgl%>">Tanggal</option>                                    
                                    <option <%=groupBy == groupByPosisi ? "selected" : ""%> value="<%=groupByPosisi%>">Posisi</option>
                                </select>
                            </div>
                        </div>
                        <!---->
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Nama Pegawai</label>
                            <div class="col-sm-6">
                                <select multiple="" name="FRM_PEGAWAI" class="form-control input-sm selects2" style="width: 100%">
                                    <%
                                        Vector<Employee> listEmployee = PstEmployee.list(0, 0, "", "");
                                        for (Employee e : listEmployee) {
                                            String selected = "";
                                            if (selectedPegawai != null) {
                                                for (int j = 0; j < selectedPegawai.length; j++) {
                                                    Employee e2 = PstEmployee.fetchExc(Long.valueOf(selectedPegawai[j]));
                                                    if (e2.getOID() == e.getOID()) {
                                                        selected = "selected";
                                                    }
                                                }
                                            }
                                    %>
                                    <option <%=selected%> value="<%=e.getOID()%>"><%=e.getFullName()%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="box-footer" style="border-color: lightgray">
                        <button type="submit" id="btnSrc" class="btn btn-sm btn-primary"><i class="fa fa-search"></i>&nbsp; Cari</button>
                    </div>

                </form>
            </div>
            
            <%if(iCommand == Command.LIST) {%>
            
            <div class="box box-primary">
                <div class="box-header with-border" style="border-color: lightgray">
                    <h3 class="box-title">Hasil Pencarian</h3>
                </div>
                
                <div class="box-body">
                    
                    <%if (error == 0) {%>
                    
                    <% if (listSalesInsentif.isEmpty()) {%>
                    <b>Data tidak ditemukan...</b>
                    <%}%>
                    
                    <table class="table table-bordered tabel_data">

                        <%
                            long lGroupBy = 0;
                            Date dGroupBy = null;
                            int no = 0;
                            int groupSales = 0;
                            
                            double subTotalPE = 0;
                            double subTotalPB = 0;
                            double subTotalNI = 0;
                            double grandTotalPE = 0;
                            double grandTotalPB = 0;
                            double grandTotalNI = 0;
                    
                            for (int i = 0; i < listSalesInsentif.size(); i++) {
                                Vector v = (Vector) listSalesInsentif.get(i);
                                BillMain bm = (BillMain) v.get(0);
                                InsentifData id = (InsentifData) v.get(1);
                                Position p = PstPosition.fetchExc(id.getPositionId());
                                Employee e = PstEmployee.fetchExc(id.getEmployeeId());
                                
                                no++;
                                String judul = "";
                                
                                double poinEmas = 0;
                                double nominalEmas = 0;
                                double poinBerlian = 0;
                                double nominalBerlian = 0;
                                double totalInsentif = 0;
                                
                                if (groupBy == groupByTgl) {
                                    
                                    Date dTanggal = bm.getBillDate();
                                    String sTanggal = Formater.formatDate(dTanggal, "yyyy-MM-dd");
                                    Date d = Formater.formatDate(sTanggal, "yyyy-MM-dd");
                                    
                                    if (dGroupBy == null || dGroupBy.compareTo(d) != 0) {
                                        dGroupBy = d;
                                        judul = "" + Formater.formatDate(bm.getBillDate(), "yyyy-MM-dd");
                                        no = 1;
                                        groupSales++;
                                    }

                                    ArrayList<Double> insentifEmas = SessSales.getSalesPersonPointInsentif(id.getEmployeeId(), Material.MATERIAL_TYPE_EMAS, dTanggal, dTanggal);                                
                                    poinEmas = insentifEmas.get(0);
                                    nominalEmas = insentifEmas.get(1);

                                    ArrayList<Double> insentifBerlian = SessSales.getSalesPersonPointInsentif(id.getEmployeeId(), Material.MATERIAL_TYPE_BERLIAN, dTanggal, dTanggal);                                
                                    poinBerlian = insentifBerlian.get(0);
                                    nominalBerlian = insentifBerlian.get(1);

                                } else if (groupBy == groupByPosisi) {
                                    
                                    if (lGroupBy != id.getPositionId()) {                                        
                                        lGroupBy = id.getPositionId();                                        
                                        judul = "" + p.getPosition();
                                        no = 1;
                                        groupSales++;
                                    }

                                    Date dAwal = Formater.formatDate(tglAwal, "yyyy-MM-dd");
                                    Date dAkhir = Formater.formatDate(tglAkhir, "yyyy-MM-dd");
                                    
                                    ArrayList<Double> insentifEmas = SessSales.getSalesPersonPointInsentif(id.getEmployeeId(), Material.MATERIAL_TYPE_EMAS, dAwal, dAkhir);                                
                                    poinEmas = insentifEmas.get(0);
                                    nominalEmas = insentifEmas.get(1);

                                    ArrayList<Double> insentifBerlian = SessSales.getSalesPersonPointInsentif(id.getEmployeeId(), Material.MATERIAL_TYPE_BERLIAN, dAwal, dAkhir);                                
                                    poinBerlian = insentifBerlian.get(0);
                                    nominalBerlian = insentifBerlian.get(1);
                                    
                                }
                                
                                totalInsentif = nominalEmas + nominalBerlian;
                                
                                grandTotalPE += poinEmas;
                                grandTotalPB += poinBerlian;
                                grandTotalNI += totalInsentif;
                        %>
                        
                        <%if(no==1 && i>0){%>
                        <tr>
                            <th colspan="3" class="text-right">Sub Total :</th>
                            <th class="text-center"><%=String.format("%,.2f", subTotalPE)%></th>
                            <th class="text-center"><%=String.format("%,.2f", subTotalPB)%></th>
                            <th class="text-right"><%=String.format("%,.0f", subTotalNI)%>.00</th>
                        </tr>
                        <%}%>
                        
                        <%                            
                            if (no == 1 && i > 0) {
                                subTotalPE = 0;
                                subTotalPB = 0;
                                subTotalNI = 0;
                            }
                            subTotalPE += poinEmas;
                            subTotalPB += poinBerlian;
                            subTotalNI += totalInsentif;                            
                        %>
                        
                        <% if(no==1){%>                
                        <tr><td colspan="6" class="label-default"><b><%=judul%></b></td></tr>
                        <tr>
                            <th class="text-center" style="width: 1%">No.</th>
                            <% if(groupBy == groupByTgl) { %>
                            <th class="text-left">Posisi</th>
                            <% } else if(groupBy == groupByPosisi) {%>
                            <th class="text-center">Tanggal</th>
                            <% } %>
                            <th>Nama Pegawai</th>
                            <th class="text-center">Poin Emas</th>
                            <th class="text-center">Poin Berlian</th>
                            <th class="text-right">Nominal Insentif (Rp)</th>
                        </tr>
                        <%}%>
                        
                        <tr>
                            <td class="text-center"><%=no%></td>
                            <% if(groupBy == groupByTgl) { %>
                            <td class="text-left"><%=p.getPosition()%></td>
                            <% } else if(groupBy == groupByPosisi) {%>
                            <td class="text-center"><%=Formater.formatDate(bm.getBillDate(), "yyyy-MM-dd")%></td>
                            <% } %>
                            <td><%=e.getFullName()%></td>
                            <td class="text-center"><%=String.format("%,.2f", poinEmas)%></td>
                            <td class="text-center"><%=String.format("%,.2f", poinBerlian)%></td>
                            <td class="text-right"><%=String.format("%,.0f", totalInsentif)%>.00</td>
                        </tr>

                        <%
                            }
                        %>
                        
                        <%if(!listSalesInsentif.isEmpty()){%>
                        
                        <%if(groupSales > 1){%>
                        <tr>
                            <th colspan="3" class="text-right">Sub Total :</th>
                            <th class="text-center"><%=String.format("%,.2f", subTotalPE)%></th>
                            <th class="text-center"><%=String.format("%,.2f", subTotalPB)%></th>
                            <th class="text-right"><%=String.format("%,.0f", subTotalNI)%>.00</th>
                        </tr>
                        <%}%>
                        
                        <tr class="">
                            <th colspan="3" class="text-right">Grand Total :</th>
                            <th class="text-center label-default"><%=String.format("%,.2f", grandTotalPE)%></th>
                            <th class="text-center label-default"><%=String.format("%,.2f", grandTotalPB)%></th>
                            <th class="text-right label-default"><%=String.format("%,.0f", grandTotalNI)%>.00</th>
                        </tr>
                        <%}%>
                    
                    </table>
                    
                    <%}%>

                </div>
                    
                <% if (!listSalesInsentif.isEmpty()) {%>
                
                <div class="box-footer" style="border-color: lightgray">                                                                    
                    <div class=""><button type="button" id="btnPrint" class="btn btn-sm btn-primary pull-right"><i class="fa fa-print"></i>&nbsp; Cetak</button></div>                                        
                </div>
                
                <% } %>
                
            </div>
                        
            <%}%>
        </div>
    </body>
</html>
