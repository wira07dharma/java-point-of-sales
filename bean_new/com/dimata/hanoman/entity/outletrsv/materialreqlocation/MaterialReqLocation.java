/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.entity.outletrsv.materialreqlocation;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.entity.masterdata.PstRoomClass;
import com.dimata.posbo.entity.masterdata.RoomClass;
import com.dimata.qdep.entity.Entity;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Dewa
 */
public class MaterialReqLocation extends Entity {

    private long materialId = 0;
    private long posRoomClassId = 0;
    private float duration = 0;
    private int orderIndex = 0;
    private int ignorePIC = 0;
    
    public static int PIC_HANDLE_SINGLE = 0;
    public static int PIC_HANDLE_MULTIPLE = 1;

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public long getPosRoomClassId() {
        return posRoomClassId;
    }

    public void setPosRoomClassId(long posRoomClassId) {
        this.posRoomClassId = posRoomClassId;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
    
    public static String getLog(MaterialReqLocation input) {
      return getLog(null, input);
    }
    
    public static String getLog(MaterialReqLocation current, MaterialReqLocation input) {
      JSONObject o = new JSONObject();
      try {
      
        //New
        if (current == null) {
          RoomClass roomClass = PstRoomClass.fetchExc(input.getPosRoomClassId());
          o.put("Room Class", roomClass.getClassName());
          o.put("Duration", input.getDuration());
          o.put("Order Index", input.getOrderIndex());
          return o.toString();
        }

        //Update Room Class
        if (current.getPosRoomClassId() != input.getPosRoomClassId()) {
          RoomClass oldRoomClass = current.getPosRoomClassId() == 0 ? new RoomClass() : PstRoomClass.fetchExc(current.getPosRoomClassId());
          RoomClass newRoomClass = input.getPosRoomClassId() == 0 ? new RoomClass() : PstRoomClass.fetchExc(input.getPosRoomClassId());
          oldRoomClass.setClassName(oldRoomClass.getClassName() == "" ? "-" : oldRoomClass.getClassName());
          newRoomClass.setClassName(newRoomClass.getClassName() == "" ? "-" : newRoomClass.getClassName());
          o.put("Room Class", "From "+oldRoomClass.getClassName()+" changed to "+newRoomClass.getClassName());
        }
        
        //Update Duration
        if (current.getDuration() != input.getDuration()) {
          o.put("Duration", "From "+current.getDuration()+" changed to "+input.getDuration());
        }
        
        //Update Duration
        if (current.getOrderIndex() != input.getOrderIndex()) {
          o.put("Order Index", "From "+current.getOrderIndex()+" changed to "+input.getOrderIndex());
        }
        
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      } catch (DBException ex) {
        Logger.getLogger(MaterialReqLocation.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      return o.toString();
    }

  /**
   * @return the ignorePIC
   */
  public int getIgnorePIC() {
    return ignorePIC;
  }

  /**
   * @param ignorePIC the ignorePIC to set
   */
  public void setIgnorePIC(int ignorePIC) {
    this.ignorePIC = ignorePIC;
  }

}
