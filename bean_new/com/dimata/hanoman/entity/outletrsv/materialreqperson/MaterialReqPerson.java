/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.entity.outletrsv.materialreqperson;

import com.dimata.hanoman.entity.outletrsv.materialreqpersonoption.MaterialReqPersonOption;
import com.dimata.qdep.entity.Entity;
import java.util.Vector;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Dewa
 */
public class MaterialReqPerson extends Entity {

    private long materialReqLocationId = 0;
    private int numberOfPerson = 0;
    private String jobdesc = "";
    private float jobWeight = 0;
    private Vector<MaterialReqPersonOption> reqPersonOptions = null;
    
    public int getReqPersonOptionSize() {
      if (reqPersonOptions == null) {
        return 0;
      }
      return this.reqPersonOptions.size();
    }
    
    public MaterialReqPersonOption getReqPersonOption(int i) {
      if (this.reqPersonOptions == null || i < 0 || i >= this.reqPersonOptions.size()) {
        return null;
      }
      return this.reqPersonOptions.get(i);
    }
    
    public void addReqPersonOption(MaterialReqPersonOption rpo) {
      if (rpo != null) {
        if (this.reqPersonOptions == null) {
          this.reqPersonOptions = new Vector<MaterialReqPersonOption>();
        }
        this.reqPersonOptions.add(rpo);
      }
    }

    public long getMaterialReqLocationId() {
        return materialReqLocationId;
    }

    public void setMaterialReqLocationId(long materialReqLocationId) {
        this.materialReqLocationId = materialReqLocationId;
    }

    public int getNumberOfPerson() {
        return numberOfPerson;
    }

    public void setNumberOfPerson(int numberOfPerson) {
        this.numberOfPerson = numberOfPerson;
    }

    public String getJobdesc() {
        return jobdesc;
    }

    public void setJobdesc(String jobdesc) {
        this.jobdesc = jobdesc;
    }

    public float getJobWeight() {
        return jobWeight;
    }

    public void setJobWeight(float jobWeight) {
        this.jobWeight = jobWeight;
    }
    
    public static String getLog(MaterialReqPerson current, MaterialReqPerson input) {
      JSONObject o = new JSONObject();
      try {
      
        //New
        if (current == null) {
          o.put("Number of Person", input.getNumberOfPerson());
          o.put("Jobdesc", input.getJobdesc());
          o.put("Jobweight", input.getJobWeight());
          return o.toString();
        }

        //Update Number of person
        if (current.getNumberOfPerson()!= input.getNumberOfPerson()) {
          o.put("Number of Person", "From "+current.getNumberOfPerson()+" changed to "+input.getNumberOfPerson());
        }
        
        //Update Jobdesc
        if (current.getJobdesc() != input.getJobdesc()) {
          o.put("Jobdesc", "From "+current.getJobdesc()+" changed to "+input.getJobdesc());
        }
        
        //Update Jobweight
        if (current.getJobWeight() != input.getJobWeight()) {
          o.put("Jobweight", "From "+current.getJobWeight()+" changed to "+input.getJobWeight());
        }
        
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      }
      
      return o.toString();
    }

}
