
package com.dimata.posbo.ajax;

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactClass;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AjaxReceive 
extends HttpServlet{
    
    public static final String textListTitle[][]={
        {"No.","Nama Suplier","No Telepon"},
        {"No.","Suplier Name","Telepon Number"}   
    };
    
    private String drawListMaterial2(Vector objectClass,int language){
        String result ="";
        
        result += "<div class='row'>";
        result += "<div class='col-md-12'>";
        result += "<table style='width :100%;' class='table table-striped table-bordered table-hover'>";
        result += "<thead>";
        result += "<tr>";
        result += "<th><h5>"+textListTitle[language][0]+"</h5></th>";
        result += "<th><h5>"+textListTitle[language][1]+"</h5></th>";
        result += "<th><h5>"+textListTitle[language][2]+"</h5></th>";
        result += "</tr>";
        result += "</thead>";
        result += "<tbody>";
        for(int i=0; i<objectClass.size(); i++){
            ContactList contactList = (ContactList)objectClass.get(i);
            int number = i+1;
            String suplierName="";
            if (contactList.getCompName()!=""){
                suplierName = contactList.getCompName();
            }else{
                suplierName = contactList.getPersonName()+" "+contactList.getPersonLastname();
            }
            
            result += "<tr style='cursor:pointer' class='selectSuplier' data-phone='"+contactList.getTelpMobile()+"' data-address='"+contactList.getHomeAddr()+"' data-contactname='"+contactList.getPersonName()+"' data-oidsuplier ='"+contactList.getOID()+"' data-namasuplier='"+suplierName+"'>";
            result += "<td>"+number+"</td>";
            result += "<td>"+suplierName+"</td>";
            result += "<td>"+contactList.getTelpMobile()+"</td>";
            result += "</tr>";
        }
        
        result += "</tbody>";
        result += "</table>";
        result += "</div>";
        result += "</div>";
           
        return result;
    }
    
    private String searchTool(){
        String container ="";
        container   += "<div class='row'>" 
                        + "<div class='col-md-12'>"
                            + "<input type='text' id='searchSuplier' class='form-control'>"
                        + "</div> "
                    +  "</div>"
                    +  "<br>";
        return container;
    }
    private String getListSuplier(HttpServletRequest request){
        String container ="";
        String typeText = FRMQueryString.requestString(request, "typeText");
        int language = FRMQueryString.requestInt(request, "language");
        
        String whereSupp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                        " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
                        " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
                        " != "+PstContactList.DELETE ;
        whereSupp += " AND ("+PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]+" LIKE '%"+typeText+"%' OR "
                  +PstContactList.fieldNames[PstContactList.FLD_PERSON_LASTNAME]+" LIKE '%"+typeText+"%' OR "
                  +PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+" LIKE '%"+typeText+"%' OR "
                  +PstContactList.fieldNames[PstContactList.FLD_TELP_MOBILE]+" LIKE '%"+typeText+"%' )";
        Vector listSuplier = PstContactList.listContactByClassType(0,10,whereSupp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
        container = drawListMaterial2(listSuplier,language);
        return container;
    }
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        String result ="";
        int command = FRMQueryString.requestInt(request,"command");
        response.setContentType("text/html;charset=UTF-8");
        
        switch (command) {
            case Command.LIST :
                result = getListSuplier(request);
                response.getWriter().write(result);
                break;
            case Command.SEARCH:
                result = searchTool();
                response.getWriter().write(result);
                break;          
            default:

        }
    }
}
