/*
 * PstAppPriv.java
 *
 * Created on April 3, 2002, 9:29 AM
 */

package com.dimata.aiso.entity.admin;

/**
 *
 * @author  ktanjana
 * @version 
 */

import java.util.*;
import com.dimata.qdep.entity.*;

public class AppPriv  extends Entity {
 private String PrivName="";
 private Date RegDate=new Date();
 private String Descr=""; 
 
 /** Creates new AppPriv */
 public AppPriv() {
     super();
 }

 public void setPrivName(String newName){
     this.PrivName = newName;
 }
 
 public String getPrivName(){
     return (this.PrivName==null) ? "" : this.PrivName;
 }

 public void setDescr(String newDescr){
     this.Descr = newDescr;
 }
 
 public String getDescr(){
     return (this.Descr==null) ? "" : this.Descr;
 }
 
 public void setRegDate(Date newDate){
     this.RegDate=newDate;
 }
 
 public Date getRegDate(){
     return (this.RegDate==null) ? new Date() : this.RegDate;
 }
 
 
}

