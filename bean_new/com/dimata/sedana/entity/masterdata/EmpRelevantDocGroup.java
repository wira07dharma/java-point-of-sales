/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.masterdata;

import com.dimata.qdep.entity.Entity;
import com.dimata.sedana.session.json.JSONObject;

/**
 *
 * @author dedy_blinda
 */
public class EmpRelevantDocGroup extends Entity implements Cloneable{
    private String docGroup = "";
    private String docGroupDesc = "";

    /**
     * @return the docGroup
     */
    public String getDocGroup() {
        return docGroup;
    }

    /**
     * @param docGroup the docGroup to set
     */
    public void setDocGroup(String docGroup) {
        this.docGroup = docGroup;
    }

    /**
     * @return the docGroupDesc
     */
    public String getDocGroupDesc() {
        return docGroupDesc;
    }

    /**
     * @param docGroupDesc the docGroupDesc to set
     */
    public void setDocGroupDesc(String docGroupDesc) {
        this.docGroupDesc = docGroupDesc;
    }

  public JSONObject historyNew() {
    JSONObject j = new JSONObject();
    j.put("Grup Dokumen", getDocGroup());
    j.put("Deskripsi", getDocGroupDesc());
    return j;
  }

  public JSONObject historyCompare(EmpRelevantDocGroup o) {
    JSONObject j = new JSONObject();
    if (!getDocGroup().equals(o.getDocGroup())) {
      j.put("Grup Dokumen", "Dari " + getDocGroup() + " menjadi " + o.getDocGroup());
    }
    if (!getDocGroupDesc().equals(o.getDocGroupDesc())) {
      j.put("Deskripsi", "Dari " + getDocGroupDesc() + " menjadi " + o.getDocGroupDesc());
    }
    return j;
  }

  public EmpRelevantDocGroup clone() {
    Object o = null;
    try {
      o = super.clone();;
    } catch (CloneNotSupportedException ex) {
      System.err.println(ex);
    }
    return (EmpRelevantDocGroup) o;
  }
}
