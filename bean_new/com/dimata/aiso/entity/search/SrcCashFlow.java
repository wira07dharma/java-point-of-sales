/*
 * SrcCashFlow.java
 *
 * Created on December 17, 2007, 2:32 PM
 */

package com.dimata.aiso.entity.search;

/**
 *
 * @author  dwi
 */
import java.util.*;

public class SrcCashFlow {
    
   private Date dateFrom;
   private Date dateTo;
   
   public Date getDateFrom(){
        return dateFrom;
   }
   
   public void setDateFrom(Date dateFrom){
        this.dateFrom = dateFrom;
   }
   
   public Date getDateTo(){
        return dateTo;
   }
   
   public void setDateTo(Date dateTo){
        this.dateTo = dateTo;
   }
}
