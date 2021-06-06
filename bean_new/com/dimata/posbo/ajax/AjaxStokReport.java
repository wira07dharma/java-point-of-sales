package com.dimata.posbo.ajax;

import com.dimata.gui.jsp.ControlCombo;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.form.search.FrmSrcReportStock;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AjaxStokReport extends HttpServlet{
    //OBJECT
    private JSONObject jSONObject = new JSONObject();
    private JSONArray jSONArray = new JSONArray();
    
    //STRING
    private String dataFor = "";
    private String oidDelete="";
    private String approot = "";
    private String address  = "";
    private String htmlReturn = "";
  
    //LONG
    private long oidReturn = 0;

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
        this.iCommand = FRMQueryString.requestCommand(request);       
        this.iErrCode = 0;        
        this.jSONObject = new JSONObject();
        
        switch(this.iCommand){
	    case Command.SAVE :
		//commandSave(request);
	    break;
                
            case Command.LIST :
                //commandList(request);
            break;
		
	    case Command.DELETE :
                //commandDelete(request);
            break;
   
	    default : commandNone(request);
	}
        
        try{
	    
	    this.jSONObject.put("FRM_FIELD_HTML", this.htmlReturn);
	    this.jSONObject.put("FRM_FIELD_RETURN_OID", this.oidReturn);
	}catch(JSONException jSONException){
	    jSONException.printStackTrace();
	}
        
        response.getWriter().print(this.jSONObject);
        
    }
    
    public void commandNone(HttpServletRequest request){
	if(this.dataFor.equals("getItemByCategory")){
	    this.htmlReturn = getItemByCategory(request);
	}else if (this.dataFor.equals("getUserDoc")){
            this.htmlReturn = getUserDoc(request);
        }else if (this.dataFor.equals("getCashier")){
            this.htmlReturn = getCashier(request);
        }
    }
    
    private String getItemByCategory(HttpServletRequest request){
        String htmlReturn="";
        
        String categoryMultiple[] = request.getParameterValues(""+FrmSrcReportStock.fieldNames[FrmSrcReportStock.FRM_FIELD_CATEGORY_ID]+"");
        String categoryInText="";
        try {
            for (int a = 0; a<categoryMultiple.length;a++){
                if (categoryInText.length()>0){
                    categoryInText = categoryInText + "," + categoryMultiple[a];
                }else{
                    categoryInText =  categoryMultiple[a];
                }
            }
        } catch (Exception e) {
        }
        String whereMaterial = "";
        if (categoryInText.length()>0){
            whereMaterial = ""
            + " "+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]+" IN ("
            + " "+categoryInText+")";
        }
        
        Vector listMaterial = PstMaterial.list(0, 0, whereMaterial, "");
        Vector materialVal = new Vector(1,1);
        Vector materialKey = new Vector(1,1);
        for(int i=0;i<listMaterial.size();i++) {
            Material entMaterial = new Material();
            entMaterial = (Material)listMaterial.get(i);
            materialVal.add(""+entMaterial.getOID()+"");
            materialKey.add(""+entMaterial.getName()+"");
        }
        
        htmlReturn += ""
            + ""+ControlCombo.draw(FrmSrcReportStock.fieldNames[FrmSrcReportStock.FRM_FIELD_LOCATION_ID], null, "", materialVal, materialKey, " multiple='multiple'", "form-control selects2")+"";
        
        return htmlReturn;
    }
    
    private String getUserDoc(HttpServletRequest request){
        String htmlReturn="";
        
        Vector val_docuserid = new Vector(1,1);
        Vector key_docuserid = new Vector(1,1);
        String whereDocUser = ""
            + " "+PstAppUser.fieldNames[PstAppUser.FLD_USER_GROUP_NEW]+"='4'";
        Vector listDocUser = PstAppUser.listFullObj(0, 0, whereDocUser, "");
        for (int i=0; i<listDocUser.size();i++ ){
            AppUser appUser = (AppUser) listDocUser.get(i);
            val_docuserid.add(""+appUser.getOID()+"");
            key_docuserid.add(""+appUser.getFullName()+"");
        }
        
        htmlReturn += ""
            + ""+ControlCombo.draw(FrmSrcReportStock.fieldNames[FrmSrcReportStock.FRM_FIELD_USER_DOC], null, "", val_docuserid, key_docuserid, " multiple='multiple' ", "form-control selects2")+"";
        
        return htmlReturn;
    }
    
    private String getCashier(HttpServletRequest request){
        String htmlReturn="";
        
        Vector val_docuserid = new Vector(1,1);
        Vector key_docuserid = new Vector(1,1);
        String whereCashier = ""
            + " "+PstAppUser.fieldNames[PstAppUser.FLD_USER_GROUP_NEW]+"<>'4'";
        Vector listDocUser = PstAppUser.listFullObj(0, 0, whereCashier, "");
        for (int i=0; i<listDocUser.size();i++ ){
            AppUser appUser = (AppUser) listDocUser.get(i);
            val_docuserid.add(""+appUser.getOID()+"");
            key_docuserid.add(""+appUser.getFullName()+"");
        }
        
        htmlReturn += ""
            + ""+ControlCombo.draw(FrmSrcReportStock.fieldNames[FrmSrcReportStock.FRM_FIELD_USER_ID], null, "", val_docuserid, key_docuserid, " multiple='multiple' ", "form-control selects2")+"";
        
        return htmlReturn;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
