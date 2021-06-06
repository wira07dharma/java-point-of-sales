package com.dimata.sedana.entity.masterdata;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.entity.Entity;
import com.dimata.sedana.session.json.JSONObject;
import com.dimata.util.Formater;
import java.util.Date;

public class SedanaShift extends Entity implements Cloneable {

  private String name = "";
  private Date endTime = null;
  private Date startTime = null;
  private String remark = "";
  private int status = 0;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  /**
   * @return the status
   */
  public int getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(int status) {
    this.status = status;
  }

  public JSONObject historyNew() {
    JSONObject j = new JSONObject();
    j.put("Nama", getName());
    j.put("Waktu Akhir", Formater.formatDate(getEndTime(),"HH:mm:ss"));
    j.put("Waktu Mulai", Formater.formatDate(getStartTime(),"HH:mm:ss"));
    j.put("Remark", getRemark());
    j.put("Status", getStatus());
    return j;
  }

  public JSONObject historyCompare(SedanaShift o) {
    JSONObject j = new JSONObject();
    if (!getName().equals(o.getName())) {
      j.put("Nama", "Dari " + getName() + " menjadi " + o.getName());
    }
    if (!getEndTime().equals(o.getEndTime())) {
      j.put("Pendidikan", "Dari " + Formater.formatDate(getEndTime(),"HH:mm:ss") + " menjadi " + Formater.formatDate(o.getEndTime(),"HH:mm:ss"));
    }
    if (!getStartTime().equals(o.getStartTime())) {
      j.put("Pendidikan", "Dari " + Formater.formatDate(getStartTime(),"HH:mm:ss") + " menjadi " + Formater.formatDate(o.getStartTime(),"HH:mm:ss"));
    }
    if (!getRemark().equals(o.getRemark())) {
      j.put("Remark", "Dari " + getRemark() + " menjadi " + o.getRemark());
    }
    if (getStatus()!=o.getStatus()) {
      j.put("Status", "Dari " + getStatus() + " menjadi " + o.getStatus());
    }
    return j;
  }

  public SedanaShift clone() {
    Object o = null;
    try {
      o = super.clone();;
    } catch (CloneNotSupportedException ex) {
      System.err.println(ex);
    }
    return (SedanaShift) o;
  }

}
