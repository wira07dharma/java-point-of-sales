/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.anggota;

import com.dimata.qdep.entity.Entity;
import com.dimata.sedana.session.json.JSONObject;

/**
 *
 * @author HaddyPuutraa (PKL)
 * Create Kamis, 21 Pebruari 2013
 */
public class Education extends Entity implements Cloneable{
    private String education = "";
    private String educationDesc = "";
    private String educationCode="";
    
    public Education() {
    }

    
    public String getEducation(){ 
        return education; 
    } 

    public void setEducation(String education){ 
        if ( education == null ) {
            education = ""; 
	} 
	this.education = education; 
    } 

    public String getEducationDesc(){ 
        return educationDesc; 
    } 

    public void setEducationDesc(String educationDesc){ 
	if ( educationDesc == null ) {
            educationDesc = ""; 
        } 
	this.educationDesc = educationDesc; 
    }

    /**
     * @return the educationCode
     */
    public String getEducationCode() {
        return educationCode;
    }

    /**
     * @param educationCode the educationCode to set
     */
    public void setEducationCode(String educationCode) {
        this.educationCode = educationCode;
    }

  public JSONObject historyCompare(Education o) {
    JSONObject j = new JSONObject();
    if(!getEducation().equals(o.getEducation()))
      j.put("Pendidikan", "Dari "+getEducation()+" menjadi "+o.getEducation());
    if(!getEducationDesc().equals(o.getEducationDesc()))
      j.put("Deskripsi", "Dari "+getEducationDesc()+" menjadi "+o.getEducation());
    if(!getEducationCode().equals(o.getEducationCode()))
      j.put("Kode", "Dari "+getEducationCode()+" menjadi "+o.getEducation());
    return j;
  }

  public JSONObject historyNew() {
    JSONObject j = new JSONObject();
    j.put("Pendidikan", getEducation());
    j.put("Deskripsi", getEducationDesc());
    j.put("Kode", getEducationCode());
    return j;
  }

  public Education clone() {
    Object o = null;
    try {
      o = super.clone();;
    } catch (CloneNotSupportedException ex) {
      System.err.println(ex);
    }
    return (Education) o;
  }

}
