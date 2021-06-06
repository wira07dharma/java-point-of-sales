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

public class JangkaWaktu extends Entity {


private long jangkaWaktuId = 0;
private int jangkaWaktu = 0;

public int getJangkaWaktu(){
return jangkaWaktu;
}

public void setJangkaWaktu(int jangkaWaktu){
this.jangkaWaktu = jangkaWaktu;
}
  /**
   * @return the jangkaWaktuId
   */
  public long getJangkaWaktuId() {
    return jangkaWaktuId;
  }
  
  public void setJangkaWaktuId(long jangkaWaktuId) {
  this.jangkaWaktuId = jangkaWaktuId;
  }

}
