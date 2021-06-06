/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.entity.outletrsv.materialreqpersonoption;

import com.dimata.harisma.entity.masterdata.Competency;
import com.dimata.harisma.entity.masterdata.Department;
import com.dimata.harisma.entity.masterdata.Position;
import com.dimata.harisma.entity.masterdata.PstCompetency;
import com.dimata.harisma.entity.masterdata.PstDepartment;
import com.dimata.harisma.entity.masterdata.PstLevel;
import com.dimata.harisma.entity.masterdata.PstPosition;
import com.dimata.harisma.entity.masterdata.PstSection;
import com.dimata.harisma.entity.masterdata.Section;
import com.dimata.harisma.entity.masterdata.Level;
import com.dimata.qdep.entity.Entity;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Dewa
 */
public class MaterialReqPersonOption extends Entity {

    private long materialReqPersonId = 0;
    private long departmentId = 0;
    private long sectionId = 0;
    private long positionId = 0;
    private long competencyId = 0;
    private long levelId = 0;
    private int priorityIndex = 0;
    private int availablePerson = 0;

    public long getMaterialReqPersonId() {
        return materialReqPersonId;
    }

    public void setMaterialReqPersonId(long materialReqPersonId) {
        this.materialReqPersonId = materialReqPersonId;
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public long getSectionId() {
        return sectionId;
    }

    public void setSectionId(long sectionId) {
        this.sectionId = sectionId;
    }

    public long getPositionId() {
        return positionId;
    }

    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }

    public long getLevelId() {
        return levelId;
    }

    public void setLevelId(long levelId) {
        this.levelId = levelId;
    }

    public int getPriorityIndex() {
        return priorityIndex;
    }

    public void setPriorityIndex(int priorityIndex) {
        this.priorityIndex = priorityIndex;
    }

  /**
   * @return the availablePerson
   */
  public int getAvailablePerson() {
    return availablePerson;
  }

  /**
   * @param availablePerson the availablePerson to set
   */
  public void setAvailablePerson(int availablePerson) {
    this.availablePerson = availablePerson;
  }

  /**
   * @return the competencyId
   */
  public long getCompetencyId() {
    return competencyId;
  }

  /**
   * @param competencyId the competencyId to set
   */
  public void setCompetencyId(long competencyId) {
    this.competencyId = competencyId;
  }
  
  public static String getLog(MaterialReqPersonOption current, MaterialReqPersonOption input) {
    JSONObject o = new JSONObject();
      try {
      
        //New
        if (current == null) {
          Department d = PstDepartment.fetchExc(input.getDepartmentId());
          Section s = PstSection.fetchExc(input.getSectionId());
          Position p = PstPosition.fetchExc(input.getPositionId());
          Competency c = PstCompetency.fetchExc(input.getCompetencyId());
          Level l = PstLevel.fetchExc(input.getLevelId());
          o.put("Department", d.getDepartment());
          o.put("Section", s.getSection());
          o.put("Position", p.getPosition());
          o.put("Competency", c.getCompetencyName());
          o.put("Level", l.getLevel());
          o.put("Priority Index", input.getPriorityIndex());
          return o.toString();
        }

        //Update Department
        if (current.getDepartmentId()!= input.getDepartmentId()) {
          Department od = PstDepartment.fetchExc(current.getDepartmentId());
          Department nd = PstDepartment.fetchExc(input.getDepartmentId());
          o.put("Department", "From "+od.getDepartment()+" changed to "+nd.getDepartment());
        }

        //Update Section
        if (current.getSectionId()!= input.getSectionId()) {
          Section os = PstSection.fetchExc(current.getSectionId());
          Section ns = PstSection.fetchExc(input.getSectionId());
          o.put("Section", "From "+os.getSection()+" changed to "+ns.getSection());
        }

        //Update Position
        if (current.getPositionId()!= input.getPositionId()) {
          Position op = PstPosition.fetchExc(current.getPositionId());
          Position np = PstPosition.fetchExc(input.getPositionId());
          o.put("Position", "From "+op.getPosition()+" changed to "+np.getPosition());
        }

        //Update Competency
        if (current.getCompetencyId()!= input.getCompetencyId()) {
          Competency oc = PstCompetency.fetchExc(current.getCompetencyId());
          Competency nc = PstCompetency.fetchExc(input.getCompetencyId());
          o.put("Competency", "From "+oc.getCompetencyName()+" changed to "+nc.getCompetencyName());
        }

        //Update Level
        if (current.getLevelId()!= input.getLevelId()) {
          Level ol = PstLevel.fetchExc(current.getLevelId());
          Level nl = PstLevel.fetchExc(input.getLevelId());
          o.put("Level", "From "+ol.getLevel()+" changed to "+nl.getLevel());
        }

        //Update Priority Index
        if (current.getPriorityIndex()!= input.getPriorityIndex()) {
          o.put("Priority Index", "From "+current.getPriorityIndex()+" changed to "+input.getPriorityIndex());
        }
        
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      } catch (com.dimata.posbo.db.DBException ex) {
        Logger.getLogger(MaterialReqPersonOption.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      } catch (com.dimata.qdep.db.DBException ex) {
        Logger.getLogger(MaterialReqPersonOption.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      
      return o.toString();
  }

}
