/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.sedana.entity.masterdata;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author Regen
 */
public class TellerShift extends Entity {
  
  public static int STATUS_OPEN  = 2;
  public static int STATUS_CLOSE = 5;
  
  private Date openDate = null;
  private Date closeDate = null;
  private String spvOpenName = "";
  private String spvCloseName = "";
  private String username = "";
  private String name = "";
  private String status = "";
  private int statusVal = 0;
  private double openingValue = 0;
  private double computedValue = 0;
  private double closingValue = 0;
  private long appUserId = 0;

  /**
   * @return the openDate
   */
  public Date getOpenDate() {
    return openDate;
  }

  /**
   * @param openDate the openDate to set
   */
  public void setOpenDate(Date openDate) {
    this.openDate = openDate;
  }

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * @return the openingValue
   */
  public double getOpeningValue() {
    return openingValue;
  }

  /**
   * @param openingValue the openingValue to set
   */
  public void setOpeningValue(double openingValue) {
    this.openingValue = openingValue;
  }

  /**
   * @return the computedValue
   */
  public double getComputedValue() {
    return computedValue;
  }

  /**
   * @param computedValue the computedValue to set
   */
  public void setComputedValue(double computedValue) {
    this.computedValue = computedValue;
  }

  /**
   * @return the closingValue
   */
  public double getClosingValue() {
    return closingValue;
  }

  /**
   * @param closingValue the closingValue to set
   */
  public void setClosingValue(double closingValue) {
    this.closingValue = closingValue;
  }

  /**
   * @return the closeDate
   */
  public Date getCloseDate() {
    return closeDate;
  }

  /**
   * @param closeDate the closeDate to set
   */
  public void setCloseDate(Date closeDate) {
    this.closeDate = closeDate;
  }

  /**
   * @return the statusVal
   */
  public int getStatusVal() {
    return statusVal;
  }

  /**
   * @param statusVal the statusVal to set
   */
  public void setStatusVal(int statusVal) {
    this.statusVal = statusVal;
  }

  /**
   * @return the spvOpenName
   */
  public String getSpvOpenName() {
    return spvOpenName;
  }

  /**
   * @param spvOpenName the spvOpenName to set
   */
  public void setSpvOpenName(String spvOpenName) {
    this.spvOpenName = spvOpenName;
  }

  /**
   * @return the spvCloseName
   */
  public String getSpvCloseName() {
    return spvCloseName;
  }

  /**
   * @param spvCloseName the spvCloseName to set
   */
  public void setSpvCloseName(String spvCloseName) {
    this.spvCloseName = spvCloseName;
  }

  /**
   * @return the appUserId
   */
  public long getAppUserId() {
    return appUserId;
  }

  /**
   * @param appUserId the appUserId to set
   */
  public void setAppUserId(long appUserId) {
    this.appUserId = appUserId;
  }
}
