/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.common.session.json;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author Regen
 */
public class SessJSON {
  protected Vector<String> keyset = new Vector<String>();
  protected HashMap<String, Object> data = new HashMap<String, Object>();
  public int type = 0;
  protected final int OBJECT = 0;
  protected final int ARRAY = 1;
  protected String json = "";
  
  @Override
  public String toString() {
    json = (json != "") ? json : extract(this.data, type, keyset);
    return json;
  }
  
  public int length() {
    return this.data.size();
  }
  
  public HashMap<String, Object> getData() {
    return data;
  }
  
  private String extract(Object o, int type, Vector<String> keyset) {
    String data = "";
    if(o != null) {
      if(o instanceof HashMap) {
        String k = "";
        if(type == OBJECT) {
          data = "{";
          HashMap<String, Object> d = (HashMap<String, Object>) o;
          int i=0;
          for (String key : keyset) {
            try {
              k = key;
              Object entry = d.get(key);
              if(entry != null) {
                data+=(i>0)?",":"";
                if(entry.getClass() == JSONObject.class) {
                  JSONObject tObj = (JSONObject) entry;
                  data+="\""+key+"\":"+(extract(tObj, OBJECT));
                } else if(entry.getClass() == JSONArray.class) {
                  JSONArray tArr = (JSONArray) entry;
                  data+="\""+key+"\":"+(extract(tArr, ARRAY));
                } else {
                  data+="\""+key+"\":\""+((entry == null) ? "" : ((String) entry))+"\"";
                }

                i++;
              }
            } catch (Exception e) { 
              System.err.println("JSON Error on "+k+" : "+e);
            }
          }
          data += "}";
        } else if(type == ARRAY) {
          data = "[";
          HashMap<String, Object> d = (HashMap<String, Object>) o;
          int i=0;
          for(String key : keyset){
            try {
              k = key;
              Object entry = d.get(key);
              if(entry != null) {
                data+=(i>0)?",":"";
                if(entry.getClass() == JSONObject.class) {
                  JSONObject tObj = (JSONObject) entry;
                  data+=(String) extract(tObj, OBJECT);
                } else if(entry.getClass() == JSONArray.class) {
                  JSONArray tArr = (JSONArray) entry;
                  data+=extract(tArr, ARRAY);
                } else {
                  data+="\""+((entry == null) ? "" : ((String) entry))+"\"";
                }

                i++;
              }
            } catch (Exception e) {
              System.err.println("JSON Error on "+k+" : "+e);
            }
          }
          data += "]";
        }
      } else if (o.getClass() == JSONObject.class) {
        JSONObject n = (JSONObject) o;
        return extract(n.getData(), OBJECT, n.keyset);
      } else if(o.getClass() == JSONArray.class) {
        JSONArray n = (JSONArray) o;
        return extract(n.getData(), ARRAY, n.keyset);
      }
    }
    return data;
  }
  
  private String extract(Object o, int type) {
    return extract(o, type, null);
  }
  
}
