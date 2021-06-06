<%-- 
    Document   : template_sign
    Created on : Jun 21, 2018, 2:49:35 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.common.entity.system.PstSystemProperty"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%!//variabel
    int KETERANGAN = 0;
    int JABATAN = 1;
    int NAMA = 2;
    int KODE = 3;

    String checkArrayValue(String arrayValue[], int typeValue) {
        String value = "";
        try {
            value = arrayValue[typeValue];
        } catch (Exception e) {
            value = "";
        }
        return value;
    }
%>

<%
    String syspropSign1 = "";
    String syspropSign2 = "";
    String syspropSign3 = "";
    String syspropSign4 = "";

    switch (signType) {
        case 0://penerimaan
            syspropSign1 = PstSystemProperty.getValueByName("SIGN_RECEIVE_1");
            syspropSign2 = PstSystemProperty.getValueByName("SIGN_RECEIVE_2");
            syspropSign3 = PstSystemProperty.getValueByName("SIGN_RECEIVE_3");
            syspropSign4 = PstSystemProperty.getValueByName("SIGN_RECEIVE_4");
            break;
        case 1://penjualan
            syspropSign1 = PstSystemProperty.getValueByName("SIGN_SALES_1");
            syspropSign2 = PstSystemProperty.getValueByName("SIGN_SALES_2");
            syspropSign3 = PstSystemProperty.getValueByName("SIGN_SALES_3");
            syspropSign4 = PstSystemProperty.getValueByName("SIGN_SALES_4");
            break;
    }

    String arraySign1[] = syspropSign1.split("-");
    String arraySign2[] = syspropSign2.split("-");
    String arraySign3[] = syspropSign3.split("-");
    String arraySign4[] = syspropSign4.split("-");

%>
<!DOCTYPE html>

<div>    
    <table style="width: 100%; font-size: 12px;">
        <tr>
            <th><%=checkArrayValue(arraySign1, KETERANGAN)%></th>
            <th></th>
            <th></th>
            <th class="text-center"><%=checkArrayValue(arraySign2, KETERANGAN)%><%=checkArrayValue(arraySign3, KETERANGAN)%></th>
            <th></th>
            <th></th>
            <th><%=checkArrayValue(arraySign4, KETERANGAN)%></th>
        </tr>
        <tr>
            <th style="width: 15%"><%=checkArrayValue(arraySign1, JABATAN)%></th>
            <th style="width: 10%"></th>
            <th style="width: 15%"><%=checkArrayValue(arraySign2, JABATAN)%></th>
            <th style="width: 15%"></th>
            <th style="width: 15%"><%=checkArrayValue(arraySign3, JABATAN)%></th>
            <th style="width: 10%"></th>
            <th style="width: 15%"><%=checkArrayValue(arraySign4, JABATAN)%></th>
        </tr>
        <tr>
            <td><br><br><br><br></td>
            <td></td>
            <td><br><br><br><br></td>
            <td></td>
            <td><br><br><br><br></td>
            <td></td>
            <td><br><br><br><br></td>
        </tr>
        <tr>
            <th><%=checkArrayValue(arraySign1, NAMA)%></th>
            <th></th>
            <th><%=checkArrayValue(arraySign2, NAMA)%></th>
            <th></th>
            <th><%=checkArrayValue(arraySign3, NAMA)%></th>
            <th></th>
            <th><%=checkArrayValue(arraySign4, NAMA)%></th>
        </tr>
        <tr>
            <td><hr style="margin: 5px; border-bottom-width: 2px; border-style: solid"></td>
            <td></td>
            <td><hr style="margin: 5px; border-bottom-width: 2px; border-style: solid"></td>
            <td></td>
            <td><hr style="margin: 5px; border-bottom-width: 2px; border-style: solid"></td>
            <td></td>
            <td><hr style="margin: 5px; border-bottom-width: 2px; border-style: solid"></td>
        </tr>
        <tr>
            <th><%=checkArrayValue(arraySign1, KODE)%></th>
            <th></th>
            <th><%=checkArrayValue(arraySign2, KODE)%></th>
            <th></th>
            <th><%=checkArrayValue(arraySign3, KODE)%></th>
            <th></th>
            <th><%=checkArrayValue(arraySign4, KODE)%></th>
        </tr>
    </table>
</div>

