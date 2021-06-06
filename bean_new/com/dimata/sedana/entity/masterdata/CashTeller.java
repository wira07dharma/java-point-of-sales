/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.masterdata;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.entity.Entity;
import java.util.Date;

public class CashTeller extends Entity {

  private long masterLoketId = 0;
  private long appUserId = 0;
  private Date openDate = null;
  private long spvOpenId = 0;
  private String spvOpenName = "";
  private long spvCloseId = 0;
  private String spvCloseName = "";
  private long shiftId = 0;
  private Date closeDate = null;
  private int status = 2;

  public long getMasterLoketId() {
    return masterLoketId;
  }

  public void setMasterLoketId(long masterLoketId) {
    this.masterLoketId = masterLoketId;
  }

  public long getAppUserId() {
    return appUserId;
  }

  public void setAppUserId(long appUserId) {
    this.appUserId = appUserId;
  }

  public Date getOpenDate() {
    return openDate;
  }

  public void setOpenDate(Date openDate) {
    this.openDate = openDate;
  }

  public long getSpvOpenId() {
    return spvOpenId;
  }

  public void setSpvOpenId(long spvOpenId) {
    this.spvOpenId = spvOpenId;
  }

  public String getSpvOpenName() {
    return spvOpenName;
  }

  public void setSpvOpenName(String spvOpenName) {
    this.spvOpenName = spvOpenName;
  }

  public long getSpvCloseId() {
    return spvCloseId;
  }

  public void setSpvCloseId(long spvCloseId) {
    this.spvCloseId = spvCloseId;
  }

  public String getSpvCloseName() {
    return spvCloseName;
  }

  public void setSpvCloseName(String spvCloseName) {
    this.spvCloseName = spvCloseName;
  }

  public long getShiftId() {
    return shiftId;
  }

  public void setShiftId(long shiftId) {
    this.shiftId = shiftId;
  }

  public Date getCloseDate() {
    return closeDate;
  }

  public void setCloseDate(Date closeDate) {
    this.closeDate = closeDate;
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

}
