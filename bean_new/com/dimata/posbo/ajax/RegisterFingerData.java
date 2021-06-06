package com.dimata.posbo.ajax;

import com.dimata.posbo.entity.admin.FingerPatern;
import com.dimata.posbo.entity.admin.PstFingerPatern;
import com.dimata.posbo.form.admin.CtrlFingerPatern;
import com.dimata.posbo.form.admin.FrmFingerPatern;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterFingerData 
extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        int command = FRMQueryString.requestInt(request, "command");
        if (command==Command.LOAD){
            String re = checkFingerPatern(request);
            response.getWriter().write(re);
        } else if (command==Command.STOP){
            String re = deleteFingerPatern(request);
            response.getWriter().write(re);
        }else{
            processRequest(request, response);
        }
        
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
        
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");

        String timeLimitReg = "15";
        String timeLimitVer = "10";
        int command = FRMQueryString.requestInt(request, "command");
        
        if (command==Command.SAVE){
            Long employeeOid = FRMQueryString.requestLong(request, "user_id");
            String baseUrl = FRMQueryString.requestString(request,"baseUrl");
            int typeFinger = FRMQueryString.requestInt(request, "typeFinger");

            String union =""+employeeOid+"A"+typeFinger+"";

            if (employeeOid!=0){
                try {
                    String urlProcess=""+baseUrl+"masterdata/employee_finger_register.jsp;"+baseUrl+"system/getac.jsp";
                    response.getWriter().print(""+union+";SecurityKey;"+timeLimitReg+";"+urlProcess+"");

                } catch (Exception e) {
                }


            }
        }else if (command==Command.GET){
            Long employeeOid = FRMQueryString.requestLong(request, "user_id");
            String where = " "+PstFingerPatern.fieldNames[PstFingerPatern.FLD_EMPLOYEE_ID]+"="+employeeOid+"";
            
            Vector listFingerPatern = PstFingerPatern.list(0, 0, where, "");
            int count = listFingerPatern.size();
            try {
               response.getWriter().write(String.valueOf(count)); 
            } catch (Exception e) {
            }
            
            
        }else if (command==Command.DELETE){
            long fingerOid = FRMQueryString.requestLong(request, "oidFinger");
            FingerPatern entFingerPatern = new FingerPatern();
            try {
                entFingerPatern = PstFingerPatern.fetchExc(fingerOid);
            } catch (Exception e) {
            }
            entFingerPatern.setFingerPatern("");
            CtrlFingerPatern ctrlFingerPatern = new CtrlFingerPatern(request);
            int iError = ctrlFingerPatern.action(Command.SAVE, entFingerPatern);
            
            try {
                response.getWriter().write(iError); 
            } catch (Exception e) {
                
            }
            
        }else if (command==Command.DELETE){
            
        }
          
        
        
        
        
    }
    
    private String checkFingerPatern(HttpServletRequest request){
            String result = "";
            long fingerOid = FRMQueryString.requestLong(request, "oidFinger");
            FingerPatern entFingerPatern = new FingerPatern();
            try {
                entFingerPatern = PstFingerPatern.fetchExc(fingerOid);
            } catch (Exception e) {
            }
            
            if (entFingerPatern.getFingerPatern().equals("")){
                result ="0";
            }else{
               result = "1"; 
            }
            return result;
    }
    
    private String deleteFingerPatern(HttpServletRequest request){
        String result = "";
        
        long fingerOid = FRMQueryString.requestLong(request, "oidFinger");
        FingerPatern entFingerPatern = new FingerPatern();
        try {
            entFingerPatern = PstFingerPatern.fetchExc(fingerOid);
        } catch (Exception e) {
        }
        
        CtrlFingerPatern ctrlFingerPatern = new CtrlFingerPatern(request);
        int iError = ctrlFingerPatern.action(Command.DELETE, entFingerPatern);
        
        result = String.valueOf(iError);
        
        return result;
    }
    
    
}
