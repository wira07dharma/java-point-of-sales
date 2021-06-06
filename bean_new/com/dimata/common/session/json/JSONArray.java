/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.common.session.json;

import java.util.Vector;

/**
 *
 * @author Regen
 */
public class JSONArray extends SessJSON {
  
  public JSONArray() {
    this.type = ARRAY;
  }
  
  public void put(Object value) {
    this.json = "";
    String key = String.valueOf(this.data.size());
    try {
      if(value == null) {
        this.data.put(key, "");
        this.keyset.add(key);
      } else if(value.getClass() == JSONArray.class || value.getClass() == JSONObject.class) {
        this.data.put(key, value);
        this.keyset.add(key);
      } else {
        this.data.put(key, String.valueOf(value));
        this.keyset.add(key);
      }

    } catch (Exception e) {
      System.err.println("JSONArray put error on "+String.valueOf(this.data.size())+" : "+e);
    }
  }
}
