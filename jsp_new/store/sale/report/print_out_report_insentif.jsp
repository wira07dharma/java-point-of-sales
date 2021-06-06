<%-- 
    Document   : print_out_report_insentif
    Created on : Mar 15, 2018, 4:08:51 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.masterdata.*"%>
<%@page import="com.dimata.pos.entity.billing.*"%>
<%@page import="com.dimata.posbo.session.masterdata.*"%>
<%@page import="com.dimata.harisma.entity.employee.*"%>
<%@page import="com.dimata.harisma.entity.masterdata.*"%>
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
    Vector<Company> company = PstCompany.list(0, 0, "", "");
    String compName = "";
    if (!company.isEmpty()) {
        compName = company.get(0).getCompanyName().toUpperCase();
    }
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

    String tglShow = "";
    String sortShow = "";
    String groupShow = "";
    String posisiShow = "";
    String pegawaiShow = "";

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

    if (sortBy == 1) {
        sortShow = "Tanggal";
    } else if (sortBy == 2) {
        sortShow = "Nama pegawai";
    }

    if (groupBy == 1) {
        groupShow = "Tanggal";
    } else if (groupBy == 2) {
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

    //hasil pencarian
    Vector listSalesInsentif = new Vector();
    if (iCommand == Command.LIST) {
        listSalesInsentif = SessSales.getReportInsentif(0, 0, where, order);
    }
    
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>     
        <style>
            
            .tabel_header {width: 100%; font-size: 12px}
            .tabel_header td {padding-bottom: 10px}

            .tabel_data {border-color: black !important; font-size: 11px}
            
            .tabel_data th {
                //text-align: center;
                border-color: black !important;                
                border-bottom-width: thin !important;
                padding: 4px 8px !important;
            }
            .tabel_data td {
                border-color: black !important;
                padding: 4px 8px !important
            }
        </style>
    </head>
    <body>
        <div style="margin: 10px">
            
            <br>
            <div>
                <img style="width: 100px" src="../../../images/litama.jpeg">
                <span style="font-size: 24px; font-family: TimesNewRomans"><b><%=compName%></b> <small><i>Gallery</i></small></span>
            </div>
            
            <div>
                <h4><b>Laporan Insentif</b></h4>
            </div>
            
            <table class="tabel_header">
                <tr>
                    <td style="width: 10%">Tanggal</td>
                    <td style="width: 1%">:</td>
                    <td><%=tglShow%></td>
                    
                    <td>Posisi</td>
                    <td>:</td>
                    <td><%=posisiShow%></td>
                    
                    <td>Sort By</td>
                    <td>:</td>
                    <td><%=sortShow%></td>
                </tr>
                <tr>
                    <td style="width: 10%">Tanggal Dicetak</td>
                    <td style="width: 1%">:</td>
                    <td><%=Formater.formatDate(new Date(), "dd MMMM yyyy") %></td>
                    
                    <td>Pegawai</td>
                    <td>:</td>
                    <td><%=pegawaiShow%></td>
                    
                    <td>Group By</td>
                    <td>:</td>
                    <td><%=groupShow%></td>
                </tr>
            </table>
                
            <hr style="border-width: medium; border-color: black; margin-top: 5px">
            
            <% if (listSalesInsentif.isEmpty()) {%>
                <b>Data tidak ditemukan...</b>
            <% } %>
                
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

                        if (groupBy == 1) {

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

                        } else if (groupBy == 2) {

                            if (lGroupBy != p.getOID()) {                                        
                                lGroupBy = p.getOID();
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
                <tr><td colspan="6" style="background-color: lightgrey"><b><%=judul%></b></td></tr>
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

                <tr>
                    <th colspan="3" class="text-right">Grand Total :</th>
                    <th class="text-center" style="background-color: lightgrey"><%=String.format("%,.2f", grandTotalPE)%></th>
                    <th class="text-center" style="background-color: lightgrey"><%=String.format("%,.2f", grandTotalPB)%></th>
                    <th class="text-right" style="background-color: lightgrey"><%=String.format("%,.0f", grandTotalNI)%>.00</th>
                </tr>
                <%}%>
                
            </table>                
                
                <!--//START TEMPLATE TANDA TANGAN//-->                
                <% int signType = 0; %>
                <%@ include file = "../../../templates/template_sign.jsp" %>
                <!--//END TEMPLATE TANDA TANGAN//-->
                           
        </div>
    </body>
</html>
