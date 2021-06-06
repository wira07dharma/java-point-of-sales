/*
 * I_Aiso.java
 *
 * Created on December 29, 2004, 11:55 AM
 */

package com.dimata.ij.iaiso;
   
// import core java package
import java.util.Vector;
import java.util.Date;
import java.util.Hashtable;

// interfaces package
import com.dimata.qdep.entity.I_IJGeneral;

/**
 *
 * @author  Administrator
 * @version 
 */
public interface I_Aiso {

    // Class name that will implement this Interface (on AISO)
    public static final String implClassName = "com.dimata.aiso.ijimpl.IjImplementation";       
    
    /**
     * this method used to get list account chart defend on 'account group' selected
     * @param <CODE>vectOfGroupAccount</CODE> Vector of account chart group
     * return 'vector of obj account chart'
     */                                                       
    public Vector getListAccountChart(Vector vectOfGroupAccount);


    /**
     * this method used to get account chart object defend on 'oid' selected
     * @param <CODE>accountChartOid</CODE> oid of selected account chart
     * return 'obj of account chart'
     */                                                       
    public IjAccountChart getAccountChart(long accountChartOid);

    
    /**
     * this method used to save object journal into AISO. 
     * this process will trigger if 'journal engine conf' set into 'full automatic'
     * @param <CODE>vectOfObjJournal</CODE> vector of object journal that wil be save into AISO
     * return 'status of save journal process' 0 ==>no journal inserted; >0 ==> oid journal inserted
     */                                                       
    public int saveJournal(Vector vectOfObjJournal, Date dStartDatePeriode, Date dEndDatePeriode, Date dLastEntryDatePeriode);  
    

    /**
     * this method used to save object journal into AISO. 
     * this process will trigger if 'journal engine conf' set into 'full automatic'
     * @param <CODE>objIjJournalMain</CODE>object journal that wil be save into AISO
     * return 'status of save journal process' 0 ==>no journal inserted; >0 ==> oid journal inserted
     */                                                       
    public long saveJournal(IjJournalMain objIjJournalMain, Date dStartDatePeriode, Date dEndDatePeriode, Date dLastEntryDatePeriode);  
    
    
    /**
     * this method used to get OID of book type used in AISO system.
     * return OID of aiso's book type
     */                                                       
    public long getBookType();  

    
    /**
     * this method used to check 'multi department report' used in AISO system.
     * return boolean of flag, "1" if using multi department report and "0" for not
     */                                                       
    public boolean isMultiDepartmentReport();  
    
    
    /**
     * this method used to check flag if 'coa in diffrerent department' used in AISO system.
     * return boolean of flag, "true" if 'coa in diffrerent department' report and opposite
     */                                                       
    public boolean isCoaInDifferentDepartment(long firstCoaId, long secondCoaId);    
    
    
    /**
     * this method used to check if two transaction date is in same period or not.
     * return boolean of flag, "true" if trans date on same period and opposite
     */                                                       
    public boolean isTransactionOnSamePeriod(Date firstTransDate, Date secondTransDate);
    
    /**
     * this method used to get A/P balance for specified contact
     * return 
     */                                                       
    public double getApBalance(long lContactOid);    
    
    /**
     * this method used to get A/R balance for specified contact
     * return 
     */                                                       
    public double getArBalance(long lContactOid);        
}

