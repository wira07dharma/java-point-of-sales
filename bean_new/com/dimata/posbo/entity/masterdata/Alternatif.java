/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Regen
 */
public class Alternatif extends Entity {

  private long itemId = 0;
  private long roomClassId = 0;
  private int priority = 0;

  public long getItemId() {
    return itemId;
  }

  public void setItemId(long itemId) {
    this.itemId = itemId;
  }

  public long getRoomClassId() {
    return roomClassId;
  }

  public void setRoomClassId(long roomClassId) {
    this.roomClassId = roomClassId;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

}
