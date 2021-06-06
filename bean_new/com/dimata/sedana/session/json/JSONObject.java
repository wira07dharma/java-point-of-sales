/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.sedana.session.json;

import java.util.HashMap;

/**
 *
 * @author Regen
 */ 
public class JSONObject extends SessJSON {
  
  public JSONObject() {
    this.type = OBJECT;
  }
  
  public void put(String key, Object value) {
    this.json = "";
    try {
      if(value == null) {
        this.data.put(key, (value == null) ? "" : String.valueOf(value));
      } else if(value.getClass() == JSONArray.class || value.getClass() == JSONObject.class) {
        this.data.put(key, value);
      } else {
        this.data.put(key, String.valueOf(value));
      }

      if(!this.keyset.contains(String.valueOf(this.data.size())))
        this.keyset.add(key);
    } catch (Exception e) {
      System.err.println("JSONObject put error on "+key+" : "+e);
    }
  }
  
  public void combine(JSONObject o) {
    try {
      for(String k: o.keyset) {
        put(k, o.data.get(k));
      }
    } catch(Exception e){
      System.err.println(e);
    }
  }
  
}
