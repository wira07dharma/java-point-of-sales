/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.qdep.db;

import com.dimata.qdep.db.*;
import java.sql.Connection;
/**
 *
 * @author ktanjana
 */
public class DBHandlerLinker extends DBHandler {
  public static Connection getDBHandlerConnection(){
      return getDBConnection();
  }
}
