
package com.dimata.posbo.ajax;

/**
 *
 * @author Witar
 */

import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;

import com.dimata.posbo.entity.admin.FingerPatern;
import com.dimata.posbo.entity.admin.PstFingerPatern;

import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VerificationFingerData 
extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
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
        String where ="";
        try {
            String userId= FRMQueryString.requestString(request, "user_id");
            String data[] = userId.split("=");
            long employeeOID = Long.parseLong(data[0]);
            int fingerType = Integer.parseInt(data[1]);
            String base = data[2];

            where = " "+PstFingerPatern.fieldNames[PstFingerPatern.FLD_EMPLOYEE_ID]+"="+employeeOID+" and "+PstFingerPatern.fieldNames[PstFingerPatern.FLD_FINGER_TYPE]+"="+fingerType+" ";
            Vector listPatern = PstFingerPatern.list(0,0, where, "");
            FingerPatern fingerPatern = (FingerPatern)listPatern.get(0);

            String urlDestination=""+String.valueOf(employeeOID)+";"+""+fingerPatern.getFingerPatern()+""+";SecurityKey;"+timeLimitVer+";"+base+"process_verification.jsp;"+base+"system/getac.jsp;extraParams";
            response.getWriter().write(urlDestination);  
        } catch (Exception e) {
        }
        
        
        
    }
}
