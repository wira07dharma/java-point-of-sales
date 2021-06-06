
package com.dimata.harisma.ajax.employee;

import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.harisma.entity.masterdata.PstReligion;
import com.dimata.harisma.entity.masterdata.Religion;
import com.dimata.posbo.entity.admin.PstFingerPatern;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.services.WebServices;
import com.dimata.util.Command;
import java.util.ArrayList;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class AjaxEmployee 
extends HttpServlet{
    
    public static final String textListTitleHeader[][] = {
        {"Nama Lengkap","Alamat","Telepon","Jenis Kelamin","Tempat Lahir","Tgl Lahir","Agama","Daftar Pegawai","Tutup","Pencarian"},
        {"Full Name","Address","Phone","Sex","Birth Place","Birth Date","Religion","Employee List","Close","Search"}
    };
    
    public static final String textTitleSex [][]={
        {"Perempuan","Laki-laki"},
        {"Female","Male"}
    };
     
    private String drawListEmployee(int language,Vector objectClass){
        String result ="";
        
        result += "<table style='width :100%;' class='table table-striped'>";
        result += "<thead>";
       
        result += "<th>"+textListTitleHeader[language][0]+"</th>";
        result += "<th>"+textListTitleHeader[language][1]+"</th>"; 
        result += "<th>"+textListTitleHeader[language][2]+"</th>"; 
        result += "<th>"+textListTitleHeader[language][3]+"</th>"; 
        result += "<th>"+textListTitleHeader[language][4]+"</th>"; 
        result += "<th>"+textListTitleHeader[language][5]+"</th>"; 
        result += "<th>"+textListTitleHeader[language][6]+"</th>"; 
        result += "</tr>";
        result += "</thead>";
        result += "<tbody>";
        for (int i = 0; i < objectClass.size(); i++) {
            Employee entEmployee = (Employee)objectClass.get(i);
            Religion entReligion = new Religion();
            //get employee Religion 
            try{
                entReligion = PstReligion.fetchExc(entEmployee.getReligionId());
            }catch(Exception e){
                System.out.print(e);
            }
            result += "<tr style='cursor:pointer' class='employeeSelect' data-nama='"+entEmployee.getFullName()+"' data-oid='"+entEmployee.getOID()+"'>";
            result += "<td>"+entEmployee.getFullName()+"</td>";
            result += "<td>"+entEmployee.getAddress()+"</td>";
            result += "<td>"+entEmployee.getPhone()+"</td>";
            result += "<td>"+textTitleSex[language][entEmployee.getSex()]+"</td>";
            result += "<td>"+entEmployee.getBirthPlace()+"</td>";
            result += "<td>"+entEmployee.getBirthDate()+"</td>";
            result += "<td>"+entReligion.getReligion()+"</td>";
            result += "</tr>";
            
        }
        result += "</tbody>";
        return result;
    }
    
    private String drawListEmployeeApi(int language, JSONArray listArray){
        JSONArray array = new JSONArray();
        JSONObject empObj = new JSONObject();
        String result ="";
        Religion entReligion = new Religion();
        
        result += "<table style='width: 95%;margin: auto;' class='table table-striped'>";
        result += "<thead>";
       
        result += "<th>"+textListTitleHeader[language][0]+"</th>";
        result += "<th>"+textListTitleHeader[language][1]+"</th>"; 
        result += "<th>"+textListTitleHeader[language][2]+"</th>"; 
        result += "<th>"+textListTitleHeader[language][3]+"</th>"; 
        result += "<th>"+textListTitleHeader[language][4]+"</th>"; 
        result += "<th>"+textListTitleHeader[language][5]+"</th>"; 
        result += "<th>"+textListTitleHeader[language][6]+"</th>"; 
        result += "</tr>";
        result += "</thead>";
        result += "<tbody>";
        try {
            for (int i = 0; i < listArray.length(); i++) {
                empObj = listArray.optJSONObject(i);
                ArrayList data = new ArrayList();
                //get employee Religion 
                try{
                    entReligion = PstReligion.fetchExc(empObj.optLong(com.dimata.harisma.entity.employee.PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_EMPLOYEE_ID]));
                }catch(Exception e){
                    System.out.print(e);
                }
                result += "<tr style='cursor:pointer' class='employeeSelect' data-nama='"
                        + ""+empObj.optString(com.dimata.harisma.entity.employee.PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_FULL_NAME])+"' data-oid='"
                        + ""+empObj.optLong(com.dimata.harisma.entity.employee.PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_EMPLOYEE_ID])+"'>";
                result += "<td>"+empObj.optString(com.dimata.harisma.entity.employee.PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_FULL_NAME])+"</td>";
                result += "<td>"+empObj.optString(com.dimata.harisma.entity.employee.PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_ADDRESS], "-")+"</td>";
                result += "<td>"+empObj.optString(com.dimata.harisma.entity.employee.PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_PHONE], "-")+"</td>";
                result += "<td>"+textTitleSex[language][Integer.parseInt(empObj.optString(com.dimata.harisma.entity.employee.PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_SEX], "-"))]+"</td>";
                result += "<td>"+empObj.optString(com.dimata.harisma.entity.employee.PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_BIRTH_PLACE], "-")+"</td>";
                result += "<td>"+empObj.optString(com.dimata.harisma.entity.employee.PstEmployee.fieldNames[com.dimata.harisma.entity.employee.PstEmployee.FLD_BIRTH_DATE], "-")+"</td>";
                result += "<td>"+entReligion.getReligion()+"</td>";
                result += "</tr>";

            }
        } catch (Exception e) {
        }
        result += "</tbody>";
        return result;
    }
    
    private String getListEmployee(HttpServletRequest request){
        
        String useForRaditya = PstSystemProperty.getValueByName("USE_FOR_RADITYA"); 
        String result ="";
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        String whereClause ="";
        String search = FRMQueryString.requestString(request,"search");
        int language = FRMQueryString.requestInt(request,"language");
        String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
        
        if (!search.equals("")){
            whereClause  =" ("+PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]+" like '%"+ search +"%')";
            whereClause +=" or ("+PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS]+" like '%"+search+"%')";
            whereClause +=" or ("+PstEmployee.fieldNames[PstEmployee.FLD_PHONE]+" like '%"+search+"%')";
            whereClause +=" or ("+PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_PLACE]+" like '%"+search+"%')";
            whereClause +=" or ("+PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]+" like '%"+search+"%')";
        }
        

          if(useForRaditya.equals("1")){
              try {
                String tempUrl = hrApiUrl + "/employee/employee-officer/";
                String param = "whereClause=" + WebServices.encodeUrl(whereClause);
                  JSONObject tempObj = WebServices.getAPIWithParam("", tempUrl, param);
                int err = tempObj.optInt("ERROR", 0);

                if (err == 0) {
                    array = tempObj.getJSONArray("DATA");
                }
              } catch (Exception e) {
              }

            result += "<div class='row'>";
            result += "<div class='col-md-12'>";
            result += ""+drawListEmployeeApi(language, array)+"";
            result += "</div>";
            result += "</div>";
            result += "</div>";
          }else{
            Vector listEmployee = PstEmployee.list(0,10, whereClause, "");

            result += "<div class='row'>";
            result += "<div class='col-md-12'>";
            result += ""+drawListEmployee(language, listEmployee)+"";
            result += "</div>";
            result += "</div>";
            result += "</div>";
          }
        return result;
    }
    
    private String getSearchControl(HttpServletRequest request){
        String result ="";
        int languange = FRMQueryString.requestInt(request,"language");
        
        result += "<div class=\"modal-header\">";
        result += "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>";
        result += "<div class=\"modal-title\" id=\"modal-title\">"+textListTitleHeader[languange][7]+"</div>";
        result += "</div>";
        result += "<div class=\"modal-body\" id=\"modal-body\">";
        result += "<div class=\"row\">";
        result += "<div class='col-md-12'><input type=\"text\" placeholder='"+textListTitleHeader[languange][9]+"...' name='searchEmployee' id='searchEmployee'  class='form-control formSearch'></div>";
        result += "</div>";
        return result;
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        String result ="";
        int command = FRMQueryString.requestInt(request,"command");
        response.setContentType("text/html;charset=UTF-8");
        
        switch (command) {
            case Command.FIRST :
                result= getSearchControl(request);
                response.getWriter().write(result);
                break;
            case Command.LIST :
                result = getListEmployee(request);
                response.getWriter().write(result);
                break;
            default:

        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        
    }
    
    
}
