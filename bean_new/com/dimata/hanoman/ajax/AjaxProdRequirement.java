/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.ajax;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.hanoman.entity.outletrsv.materialreqfacility.MaterialReqFacility;
import com.dimata.hanoman.entity.outletrsv.materialreqfacility.PstMaterialReqFacility;
import com.dimata.hanoman.entity.outletrsv.materialreqlocation.MaterialReqLocation;
import com.dimata.hanoman.entity.outletrsv.materialreqlocation.PstMaterialReqLocation;
import com.dimata.hanoman.entity.outletrsv.materialreqperson.MaterialReqPerson;
import com.dimata.hanoman.entity.outletrsv.materialreqperson.PstMaterialReqPerson;
import com.dimata.hanoman.entity.outletrsv.materialreqpersonoption.MaterialReqPersonOption;
import com.dimata.hanoman.entity.outletrsv.materialreqpersonoption.PstMaterialReqPersonOption;
import com.dimata.hanoman.form.outletrsv.materialreqfacility.CtrlMaterialReqFacility;
import com.dimata.hanoman.form.outletrsv.materialreqlocation.CtrlMaterialReqLocation;
import com.dimata.hanoman.form.outletrsv.materialreqlocation.FrmMaterialReqLocation;
import com.dimata.hanoman.form.outletrsv.materialreqperson.CtrlMaterialReqPerson;
import com.dimata.hanoman.form.outletrsv.materialreqpersonoption.CtrlMaterialReqPersonOption;
import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.harisma.entity.masterdata.Competency;
import com.dimata.harisma.entity.masterdata.Position;
import com.dimata.harisma.entity.masterdata.PstCompetency;
import com.dimata.harisma.entity.masterdata.PstPosition;
import com.dimata.harisma.entity.masterdata.PstSection;
import com.dimata.harisma.entity.masterdata.Section;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.masterdata.Alternatif;
import com.dimata.posbo.entity.masterdata.PstAlternatif;
import com.dimata.posbo.entity.masterdata.PstRoomClass;
import com.dimata.posbo.entity.masterdata.RoomClass;
import com.dimata.posbo.session.admin.SessUserSession;
import com.dimata.qdep.db.DBException;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.DateCalc;
import com.dimata.util.Formater;
import java.io.IOException;
import java.sql.Time;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Adi
 */
public class AjaxProdRequirement extends HttpServlet {
  
  //DATATABLES
    private String searchTerm;
    private String colName;
    private int colOrder;
    private String dir;
    private int start;
    private int amount;

    //OBJECT
    // private JSONObject jSONObject = new JSONObject();
    // private JSONArray jSONArray = new JSONArray();

    //LONG
    private long oid = 0;
    private long oidReturn = 0;

    //STRING
    private String dataFor = "";
    private String oidDelete = "";
    private String approot = "";
    private String htmlReturn = "";
    private String dateStart = "";
    private String dateEnd = "";

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;
      
    private String username = "";
    private long userId   = 0;
    
    // HTTP Variables will be accessed by all methods in this class.
    private HttpServletRequest req = null;
    private HttpServletResponse res = null;
    
    /* --- Main Process ----------------------------------------------------- */
    public String baseUrl(String uri) {
      String scheme = this.req.getScheme() + "://";
      String serverName = this.req.getServerName();
      String serverPort = (this.req.getServerPort() == 80) ? "" : ":" + this.req.getServerPort();
      String contextPath = this.req.getContextPath();
      return scheme + serverName + serverPort + contextPath + "/" + uri;
    }
    protected void processRequest( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
      // HTTP variables initialization
      this.req = request;
      this.res = response;
      
      HttpSession session = request.getSession(false);
      SessUserSession userSession = (SessUserSession) session.getValue(SessUserSession.HTTP_SESSION_NAME);
      if(userSession.isLoggedIn()==true){
        AppUser appUser = userSession.getAppUser();
        username = appUser.getLoginId();
        userId = appUser.getOID();
      } else {
        response.sendRedirect(this.req.getContextPath()+"/");
      }

      //LONG
      this.oid = FRMQueryString.requestLong( request, "FRM_FIELD_OID" );
      this.oidReturn = 0;

      //STRING
      this.dataFor = FRMQueryString.requestString( request, "FRM_FIELD_DATA_FOR" );
      this.oidDelete = FRMQueryString.requestString( request, "FRM_FIELD_OID_DELETE" );
      this.approot = FRMQueryString.requestString( request, "FRM_FIELD_APPROOT" );
      this.htmlReturn = "";
      this.dateStart = Formater.formatDate( new Date(), "yyyy-MM-dd" );
      this.dateEnd = Formater.formatDate( new Date(), "yyyy-MM-dd" );

      //INT
      this.iCommand = FRMQueryString.requestCommand( request );
      this.iErrCode = 0;

      if (this.dataFor.equals("requiredlocaction")) {
        if (this.iCommand != Command.LIST && this.iCommand != Command.NONE) {
          reqLocationSubmitChanges();
        }
      } else if (this.dataFor.equals("requiredPerson")) {
        if (this.iCommand != Command.LIST && this.iCommand != Command.NONE) {
          reqPersonSubmitChanges();
        }
      } else if (this.dataFor.equals("requiredPersonOption")) {
        if (this.iCommand != Command.LIST && this.iCommand != Command.NONE) {
          reqPersonOptionSubmitChanges();
        }
      } else if (this.dataFor.equals("requiredFacility")) {
        if (this.iCommand != Command.LIST && this.iCommand != Command.NONE) {
          reqFacilitySubmitChanges();
        }
      } else if (this.dataFor.equals("showlistprodrequirements")) {
        productReqList();
      } else if (this.dataFor.equals("throwsection")) {
        throwSection();
      } else if (this.dataFor.equals("throwposition")) {
        throwPosition();
      } else if (this.dataFor.equals("throwcompetency")) {
        throwCompetency();
      }
    }
    /* ---------------------------------------------------------------------- */
    
    /* --- Preparation ------------------------------------------------------ */
    public String callJSP() {
      //This function will direct to JSP file based on FRM_FIELD_DATA FOR request variable.
      String uri = "/outlet/reservation/ajaxResponse/";
      String condition = this.dataFor;
      
      if ( condition.equals( "showformreservation" )) { return uri+"reservation/reservationForm.jsp"; }
      if ( condition.equals( "showformcustomer" )) { return uri+"customer/showCustomerForm.jsp"; }
      if ( condition.equals( "showcalendar" )){ return uri+"calendar/calendarForm.jsp"; }
      if ( condition.equals( "showcalendarchart" )) { return uri+"calendar/calendarChart.jsp"; }
      if ( condition.equals( "getajaxcapacitylist" )) { return uri+"calendar/calendarCapacityDetails.jsp"; }
      if ( condition.equals( "showreservationsearchresult" )) { return uri+"calendar/reservationResult.jsp"; }
      
      return uri+"reservation/product-ajax.jsp";
      
    }
    
    public String req2String( String variable ) {
      //This function will return Request variable and parse it as String.
      return FRMQueryString.requestString( this.req, variable );
    }
    /* --- End of Preparation ----------------------------------------------- */

    /* --- Action ----------------------------------------------------------- */
    public void commandList() {
        if ( this.dataFor.equals( "" )) {

        }
    }

    public void commandDelete() {
      if ( dataFor.equals( "" )) {
      
      }
    }

    public void commandSave() throws IOException {
        this.write(this.dataFor);
    }

    public void commandApprove() {
        
    }

    public void commandNone() {
      
    }

    protected void write(String response) {
      try {
        this.res.getWriter().print(response);
      } catch (IOException iOException) {
        iOException.printStackTrace();
      }
    }
    
    public void showPage() throws ServletException, IOException {
      try {
        String jspPath = this.callJSP();
        RequestDispatcher dispatcher = this.req.getRequestDispatcher( jspPath );
        dispatcher.forward( this.req, this.res );
      } catch (Error e) {
        System.out.println(e);
      }
    }
    /* --- End of Action ---------------------------------------------------- */
    
    /* --- ReqLocation ------------------------------------------------------ */
    public void reqLocationSubmitChanges(){
      try{
        CtrlMaterialReqLocation ctrlMaterialReqLocation = new CtrlMaterialReqLocation(this.req);
        ctrlMaterialReqLocation.logUrl = "product_req.jsp";
        ctrlMaterialReqLocation.logUsername = this.username;
        ctrlMaterialReqLocation.logUserId = this.userId;
        int r       = ctrlMaterialReqLocation.action(this.iCommand, this.oid);
        String err  = FRMMessage.getErr(r);
        String info = FRMMessage.getMessage(r);
        info        = info.equals("") ? "OK" : info;
        if (r == CtrlMaterialReqLocation.RSLT_OK) {
          MaterialReqLocation mrl = ctrlMaterialReqLocation.getMaterialReqLocation();
          Vector<Alternatif> vAlternatif = PstAlternatif.list(0, 0, PstAlternatif.fieldNames[PstAlternatif.FLD_ITEM_ID]+"="+mrl.getOID(), "");
          for(Alternatif a: vAlternatif) {PstAlternatif.deleteExc(a.getOID());}
          String jaltRoom = this.req.getParameter("alt_room");
          JSONArray altRoom = new JSONArray(jaltRoom);
          for(int i=0; i<altRoom.length(); i++) {
            Alternatif a = new Alternatif();
            a.setItemId(mrl.getOID());
            a.setRoomClassId(altRoom.getLong(i));
            a.setPriority(i+1);
            PstAlternatif.insertExc(a);
          }
          ctrlMaterialReqLocation.setLog();
        }

        long newOidReqLocation = ctrlMaterialReqLocation.getMaterialReqLocationId();
        this.write("[{\"status\" : \""+(err.equals("") ? info : err)+"\", \"oid\" : \""+(newOidReqLocation != 0 ? newOidReqLocation : this.oid)+"\"}]");
      }catch(Exception xe){
      }
      
    }
    
    public void reqPersonSubmitChanges() throws IOException {
      CtrlMaterialReqPerson ctrlMaterialReqPerson = new CtrlMaterialReqPerson(this.req);
      int r       = ctrlMaterialReqPerson.action(this.iCommand, this.oid);
      String err  = FRMMessage.getErr(r);
      String info = FRMMessage.getMessage(r);
      info        = info.equals("") ? "OK" : info;
      
      long newOidReqPerson = ctrlMaterialReqPerson.getMaterialReqPersonId();
      this.write("[{\"status\" : \""+(err.equals("") ? info : err)+"\", \"oid\" : \""+(newOidReqPerson != 0 ? newOidReqPerson : this.oid)+"\"}]");
    }
    public void reqPersonOptionSubmitChanges() throws IOException {
      CtrlMaterialReqPersonOption ctrlMaterialReqPersonOption = new CtrlMaterialReqPersonOption(this.req);
      int r       = ctrlMaterialReqPersonOption.action(this.iCommand, this.oid);
      String err  = FRMMessage.getErr(r);
      String info = FRMMessage.getMessage(r);
      info        = info.equals("") ? "OK" : info;
      
      long newOidReqPersonOption = ctrlMaterialReqPersonOption.getMaterialReqPersonOptionId();
      this.write("[{\"status\" : \""+(err.equals("") ? info : err)+"\", \"oid\" : \""+(newOidReqPersonOption != 0 ? newOidReqPersonOption : this.oid)+"\"}]");
    }
    public void reqFacilitySubmitChanges() throws IOException {
      CtrlMaterialReqFacility ctrlMaterialReqFacility = new CtrlMaterialReqFacility(this.req);
      int f       = ctrlMaterialReqFacility.action(this.iCommand, this.oid);
      String err  = FRMMessage.getErr(f);
      String info = FRMMessage.getMessage(f);
      info        = info.equals("") ? "OK" : info;
      
      long newOidReqFacility = ctrlMaterialReqFacility.getMaterialReqFacilityId();
      this.write("[{\"status\" : \""+(err.equals("") ? info : err)+"\", \"oid\" : \""+(newOidReqFacility != 0 ? newOidReqFacility : this.oid)+"\"}]");
    }
    public void productReqList() {
      try {
        JSONArray wrapper = new JSONArray();
        JSONArray jsonArray = new JSONArray();

        Vector rcs = PstMaterialReqLocation.list(0, 0, PstMaterialReqLocation.fieldNames[PstMaterialReqLocation.FLD_MATERIAL_ID]+" = "+this.oid, "ORDER_INDEX");
        for(int i=0; i<rcs.size(); i++) {
          MaterialReqLocation mloc = (MaterialReqLocation) rcs.get(i);
          JSONObject jMLoc = new JSONObject();
          
          jMLoc.put("oid", String.valueOf(mloc.getOID()));
          jMLoc.put("materialId", String.valueOf(mloc.getMaterialId()));
          jMLoc.put("roomClassId", String.valueOf(mloc.getPosRoomClassId()));
          jMLoc.put("duration", mloc.getDuration());
          jMLoc.put("index", mloc.getOrderIndex());
          jMLoc.put("ignorepic", mloc.getIgnorePIC());
          
          JSONArray jArrayAlternative = new JSONArray();
          Vector<Alternatif> alt = PstAlternatif.list(0, 0, PstAlternatif.fieldNames[PstAlternatif.FLD_ITEM_ID]+"="+mloc.getOID(), PstAlternatif.fieldNames[PstAlternatif.FLD_PRIORITY]);
          for(Alternatif a: alt) {
            try {
              RoomClass r = PstRoomClass.fetchExc(a.getRoomClassId());
              JSONObject jAlternative = new JSONObject();
              jAlternative.put("oid", String.valueOf(a.getOID()));
              jAlternative.put("itemId", String.valueOf(a.getRoomClassId()));
              jAlternative.put("priotity", a.getPriority());
              jAlternative.put("itemName", r.getClassName());
              jArrayAlternative.put(jAlternative);
            } catch (com.dimata.posbo.db.DBException ex) {
              Logger.getLogger(AjaxProdRequirement.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
          
          jMLoc.put("alternative", jArrayAlternative);
          
          JSONArray jPrs = new JSONArray();
          Vector prs = PstMaterialReqPerson.list(0, 0, PstMaterialReqPerson.fieldNames[PstMaterialReqPerson.FLD_MATERIAL_REQ_LOCATION_ID]+" = "+mloc.getOID(), "JOB_WEIGHT");
          for (int j=0; j<prs.size(); j++) {
            JSONObject jPr = new JSONObject();
            MaterialReqPerson pr = (MaterialReqPerson) prs.get(j);
            
            jPr.put("oid", String.valueOf(pr.getOID()));
            jPr.put("reqLocationId", String.valueOf(pr.getMaterialReqLocationId()));
            jPr.put("number", pr.getNumberOfPerson());
            jPr.put("jobdesc", pr.getJobdesc());
            jPr.put("jobweight", pr.getJobWeight());
            
            JSONArray jPOs = new JSONArray();
            Vector pOs = PstMaterialReqPersonOption.list(0, 0, PstMaterialReqPersonOption.fieldNames[PstMaterialReqPersonOption.FLD_MATERIAL_REQ_PERSON_ID] + " = " + pr.getOID(), "PRIORITY_INDEX");
            for(int k=0; k<pOs.size(); k++) {
              JSONObject jPO = new JSONObject();
              MaterialReqPersonOption pO = (MaterialReqPersonOption) pOs.get(k);
              
              jPO.put("oid", String.valueOf(pO.getOID()));
              jPO.put("reqPersonId", String.valueOf(pO.getMaterialReqPersonId()));
              jPO.put("departmentId", String.valueOf(pO.getDepartmentId()));
              jPO.put("sectionId", String.valueOf(pO.getSectionId()));
              jPO.put("sectionId", String.valueOf(pO.getSectionId()));
              jPO.put("positionId", String.valueOf(pO.getPositionId()));
              jPO.put("competencyId", String.valueOf(pO.getCompetencyId()));
              jPO.put("levelId", String.valueOf(pO.getLevelId()));
              jPO.put("priority", pO.getPriorityIndex());
              jPOs.put(jPO);
            }
            jPr.put("reqPersonOptions", jPOs);
            jPrs.put(jPr);
          }
          jMLoc.put("reqPersons", jPrs);
          
          JSONArray jFacs = new JSONArray();
          Vector facs = PstMaterialReqFacility.list(0, 0, PstMaterialReqFacility.fieldNames[PstMaterialReqFacility.FLD_MATERIAL_REQ_LOCATION_ID]+" = "+mloc.getOID(), "");
          for (int j=0; j<facs.size(); j++) {
            MaterialReqFacility fac = (MaterialReqFacility) facs.get(j);
            JSONObject jFac = new JSONObject();
            
            jFac.put("oid", String.valueOf(fac.getOID()));
            jFac.put("reqLocationId", String.valueOf(fac.getMaterialReqLocationId()));
            jFac.put("aktivaId", String.valueOf(fac.getAktivaId()));
            jFac.put("materialId", String.valueOf(fac.getMaterialId()));
            jFac.put("number", fac.getNumber());
            jFac.put("note", fac.getNote());
            jFac.put("index", fac.getOrderIndex());
            jFac.put("duration", fac.getDuration());
            jFacs.put(jFac);
          }
          jMLoc.put("reqFacilities", jFacs);
          
          jsonArray.put(jMLoc);
        }
        wrapper.put(jsonArray);
        String result = wrapper.toString();
        write(result);
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      }
    }
    
    public void throwSection() {
      try {
        JSONArray wrapper = new JSONArray();
        JSONArray jsonArray = new JSONArray();

        long oid = FRMQueryString.requestLong(this.req, "FRM_FIELD_DEPARTMENT_ID");
        Vector sections = PstSection.list(0, 0, (oid > 0 ? PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID]+" = "+oid : ""), PstSection.fieldNames[PstSection.FLD_SECTION]);
        for(int i=0; i<sections.size(); i++) {
          Section s = (Section) sections.get(i);
          JSONObject jSection = new JSONObject();
          
          jSection.put("oid", String.valueOf(s.getOID()));
          jSection.put("name", s.getSection());
          
          jsonArray.put(jSection);
        }
        wrapper.put(jsonArray);
        String result = wrapper.toString();
        write(result);
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      }
    }
    
    public void throwPosition() {
      try {
        JSONArray wrapper = new JSONArray();
        JSONArray jsonArray = new JSONArray();

        long departmentId = FRMQueryString.requestLong(this.req, "FRM_FIELD_DEPARTMENT_ID");
        long sectionId = FRMQueryString.requestLong(this.req, "FRM_FIELD_SECTION_ID");
        long levelId = FRMQueryString.requestLong(this.req, "FRM_FIELD_LEVEL_ID");
        Vector positions = PstPosition.listByFunctional(departmentId, sectionId, levelId,  PstPosition.fieldNames[PstPosition.FLD_POSITION]);
        for(int i=0; i<positions.size(); i++) {
          Position p = (Position) positions.get(i);
          JSONObject jSection = new JSONObject();
          
          jSection.put("oid", String.valueOf(p.getOID()));
          jSection.put("name", p.getPosition());
          
          jsonArray.put(jSection);
        }
        wrapper.put(jsonArray);
        String result = wrapper.toString();
        write(result);
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      }
    }
    
    public void throwCompetency() {
      try {
        JSONArray wrapper = new JSONArray();
        JSONArray jsonArray = new JSONArray();

        long positionId = FRMQueryString.requestLong(this.req, "FRM_FIELD_POSITION_ID");
        Vector competencies = PstCompetency.list(
                0,
                0,
                (positionId != 0 ? PstPosition.fieldNames[PstPosition.FLD_POSITION_ID]+"="+positionId : ""),
                " INNER JOIN `hr_pos_competency_req` USING (`"+PstCompetency.fieldNames[PstCompetency.FLD_COMPETENCY_ID]+"`) ", PstCompetency.fieldNames[PstCompetency.FLD_COMPETENCY_NAME]
        );
        for(int i=0; i<competencies.size(); i++) {
          Competency c = (Competency) competencies.get(i);
          JSONObject jSection = new JSONObject();
          
          jSection.put("oid", String.valueOf(c.getOID()));
          jSection.put("name", c.getCompetencyName());
          
          jsonArray.put(jSection);
        }
        wrapper.put(jsonArray);
        String result = wrapper.toString();
        write(result);
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      }
    }
    /* ---------------------------------------------------------------------- */
    
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
  
}
